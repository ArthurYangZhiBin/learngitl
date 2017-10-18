/*******************************************************************************
 *                         NOTICE                            
 *                                                                                
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS             
 * CONFIDENTIAL INFORMATION OF INFOR AND/OR ITS AFFILIATES   
 * OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED WITHOUT PRIOR  
 * WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND       
 * ADAPT THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH  
 * THE TERMS OF THEIR SOFTWARE LICENSE AGREEMENT.            
 * ALL OTHER RIGHTS RESERVED.                                                     
 *                                                           
 * (c) COPYRIGHT 2009 INFOR.  ALL RIGHTS RESERVED.           
 * THE WORD AND DESIGN MARKS SET FORTH HEREIN ARE            
 * TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR          
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS        
 * RESERVED.  ALL OTHER TRADEMARKS LISTED HEREIN ARE         
 * THE PROPERTY OF THEIR RESPECTIVE OWNERS.                  
 *******************************************************************************/


package com.ssaglobal.scm.wms.util.upload;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.ui.action.ccf.AbstractProcessingTask;
import com.epiphany.shr.ui.action.ccf.JSCommandExtensionBase;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.util.export.ExportInfo;
import com.epiphany.shr.ui.view.RuntimeForm;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.WebuiConfigUtil;


/**
* Builds an XLS file with all attributes in BIO
* @return int RET_CONTINUE, RET_CANCEL
*/

/*
 * You should extend com.epiphany.shr.ui.action.ccf.JSCommandExtensionBase if your client extension is
 * not a CCFSendAllWidgetsValuesExtension extension
 */
//public class ExportToXLSExtended extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase {
public class ExportToXLSExtended extends AbstractProcessingTask {
    
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ExportToXLSExtended.class);
	
	private RuntimeListForm listForm;
    private List selectedItems;
    private BioCollectionBean bioCollectionBean;
    private int size;
    
    
    //jp 8925.begin
    public UnitOfWorkBean uowb =null;
    public UnitOfWork uow =null;
    //jp 8925.end
    
	protected void initialize(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params)
		throws EpiException {

        //jp 8925.begin
        Boolean isTempData = getParameterBoolean("isTempData");
    	String template = getParameterString("template");
        //jp 8925.end

    	_log.debug("LOG_DEBUG_EXTENSION_EXPORTOXLSEXT","jpdebug:Template"+template, 100L);
    	
		RuntimeForm parentForm = (RuntimeForm)form.getParentForm(state);
        if("LIST".equals(parentForm.getFormType()))
            listForm = (RuntimeListForm)parentForm;
        else
            listForm = (RuntimeListForm)state.getRuntimeForm(parentForm.getSubSlot("List_Slot"), 0);
        
        selectedItems=null;
        size=0;

        DataBean d = null;

        if (isTempData==null || !isTempData.booleanValue()){
        	_log.debug("LOG_DEBUG_EXTENSION_EXPORTOXLSEXT","Loading databean from Focus", 100L);

        	d = listForm.getFocus();
        	bioCollectionBean = (BioCollectionBean)d;
        	
        }else{
      
        	_log.debug("LOG_DEBUG_EXTENSION_EXPORTOXLSEXT","Loading databean from TempData", 100L); 
       
        	ExportTempFactory tempFactory = new ExportTempFactory();
     
        	
        	uowb = state.getTempUnitOfWork();
        	uow = uowb.getUOW();
        	bioCollectionBean = tempFactory.getTempCollection(template, getCCFActionContext().getState(), 
        			uowb, uow);
        	
        	
        	
        }

        size = bioCollectionBean.getSize(false);
        _log.debug("LOG_DEBUG_EXTENSION_EXPORTOXLSEXT","initialize::biocollection size:"+size, 100L);

        if(size == 0)
            throw new FormException("ERRMSG_EXPORT_DATA_MISSING", new Object[0]);
        else{
        	Integer maxExportRecords = WebuiConfigUtil.maxExcelExportRecords();
        	if (maxExportRecords<=0){
        		_log.debug("LOG_DEBUG_EXTENSION_EXPORTOXLSEXT","Webui parameter maxExcelExportRecords was zero", 100L);
        		maxExportRecords = getParameterInt("maxExcelExportRecords");
        	}
        	if(size > maxExportRecords){
        		_log.debug("LOG_DEBUG_EXTENSION_EXPORTOXLSEXT","Exceeded max records to export" +maxExportRecords, 100L);
        		
        		throw new FormException("WMEXP_MAX_EXPORT_RECORDS", 
        				new String[]{String.valueOf(maxExportRecords)});
        	}else

            return;
        }
		
	
	}
	
	
	
    protected boolean showPopup(HashMap hashMap)
    {
        int popupThreshold = getParameterInt("popupThreshold", 1000);
        return super.showPopup(hashMap) && popupThreshold != -1 && size > popupThreshold;
    }

    protected int processLongRunningRequest(RuntimeFormInterface runtimeform, RuntimeFormWidgetInterface formWidget, HashMap hashMap)
        throws EpiException
    {
        
    	
    	String template = getParameterString("template");
    	String detailCollectionName = getParameterString("detailCollectionName"); //Name of bioCollection attribute in parent BIO
    	
    	
    	Boolean useOldExportMethod = getParameterBoolean("useOldExportMethod");
    	if (useOldExportMethod==null)
    		useOldExportMethod=false;
    	
    	
    	//System.out.println("EXPORTTOXLSEXTENDED:"+template);
    	_log.debug("LOG_DEBUG_EXTENSION_EXPORTOXLSEXT","Template:"+template, 100L);
    	
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	
    	//jp 91EE Begin
    	//System.out.println("Calling exportData, Master value:"+useOldExportMethod);
    	_log.debug("LOG_DEBUG_EXTENSION_EXPORTOXLSEXT","Calling exportData, Master value:"+useOldExportMethod, 100L);
    	if (useOldExportMethod)
    		exportData(baos, template);
    	else{
    		
    		//jp.146693.begin
    		//If temp data is being used, point to the proper temp UnitOfWork
    		//if(uowb!=null)
        		//uow = uowb.getUOW();
    		//jp.146693.end

    		//jp 8925.begin
    		new ExportToXLSExtendedImpl(state, listForm, template, detailCollectionName).exportData(baos, bioCollectionBean, uow);
    		//jp 8925.end
    	}
        //jp 91EE end
    	_log.debug("LOG_DEBUG_EXTENSION_EXPORTOXLSEXT","After exportData", 100L);
    	
        ExportInfo exportInfo = new ExportInfo();
        exportInfo.setContentType("application/vnd.ms-excel");
        exportInfo.setContentDisposition("inline; filename=export.xls");

        exportInfo.setData(baos.toByteArray());
        state.getRequest().getSession().setAttribute(exportInfo.getId(), exportInfo);
        String queryString = "EXPORT_REQUEST=true&EXPORT_INFO_ID=" + exportInfo.getId();
        String jsCode = " if (window.epnyProcessingWindow) {window.epnyProcessingWindow.close();} ";
        jsCode = jsCode + "window.open('" + prepareDynamicURL(queryString) + "');";
        setResult("jsCode", jsCode);
        setStatus(JSCommandExtensionBase.STATUS_SUCCESS);
        
        return 0;
    }

    protected void exportData(ByteArrayOutputStream baos, String template){
        ExportHelperExtended exportHelper = new ExportHelperExtended(state, listForm, template);
        exportHelper.setExportHandler(new ExcelExportHandlerExtended(baos, ExportHelperExtended.getTemplateFilename(template)));
        
        if(selectedItems != null)
            exportHelper.exportBioBeanList(selectedItems);
        else
            exportHelper.exportBioCollectionBean(bioCollectionBean);

    	
    }
    
        
}    
    
	



