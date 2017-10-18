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

import com.epiphany.shr.ui.action.ccf.AbstractProcessingTask;
import com.epiphany.shr.ui.action.ccf.JSCommandExtensionBase;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.util.export.ExcelExportHandler;
import com.epiphany.shr.ui.util.export.ExportHelper;
import com.epiphany.shr.ui.util.export.ExportInfo;
import com.epiphany.shr.ui.view.RuntimeForm;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.WebuiConfigUtil;



/*
 * You should extend com.epiphany.shr.ui.action.ccf.JSCommandExtensionBase if your client extension is
 * not a CCFSendAllWidgetsValuesExtension extension
 */
public class ExportToXLSThreshold extends AbstractProcessingTask {
    
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ExportToXLSThreshold.class);
	
	private RuntimeListForm listForm;
    private List selectedItems;
    private BioCollectionBean bioCollectionBean;
    private int size;
    


	protected void initialize(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params)
		throws EpiException {

		//System.out.println("jpdebug:ExportToXLSThreshold start");
		
		RuntimeForm parentForm = (RuntimeForm)form.getParentForm(state);
        if("LIST".equals(parentForm.getFormType()))
            listForm = (RuntimeListForm)parentForm;
        else
            listForm = (RuntimeListForm)state.getRuntimeForm(parentForm.getSubSlot("List_Slot"), 0);
        
         try
        {
            selectedItems = listForm.getSelectedItems();
            size = selectedItems.size();
        }
        catch(Exception e)
        {
            _log.debug("LOG_EXPORT_XLS_GET_SELECTED", "Error getting selected items", e);
        }
        if(size == 0)
        {
            com.epiphany.shr.ui.model.data.DataBean d = listForm.getFocus();
            bioCollectionBean = (BioCollectionBean)d;
            size = bioCollectionBean.getSize(false);
        }
        if(size == 0)
            throw new FormException("ERRMSG_EXPORT_DATA_MISSING", new Object[0]);
        else{
        	Integer maxExportRecords = WebuiConfigUtil.maxExcelExportRecords();
        	if (maxExportRecords<=0){
        		_log.debug("LOG_DEBUG_EXTENSION_EXPORTOXLSTHRES","Webui parameter maxExcelExportRecords was zero", 100L);
        		maxExportRecords = getParameterInt("maxExcelExportRecords");
        	}
        	if(size > maxExportRecords){
        		_log.debug("LOG_DEBUG_EXTENSION_EXPORTOXLSTHRES","Exceeded max records to export" +maxExportRecords, 100L);
        		
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
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ExportHelper exportHelper = new ExportHelper(state, listForm);
        exportHelper.setExportHandler(new ExcelExportHandler(baos));
        ExportInfo exportInfo = new ExportInfo();
        exportInfo.setContentType("application/vnd.ms-excel");
        exportInfo.setContentDisposition("inline; filename=export.xls");
        if(selectedItems != null)
            exportHelper.exportBioBeanList(selectedItems);
        else
            exportHelper.exportBioCollectionBean(bioCollectionBean);
        exportInfo.setData(baos.toByteArray());
        state.getRequest().getSession().setAttribute(exportInfo.getId(), exportInfo);
        String queryString = "EXPORT_REQUEST=true&EXPORT_INFO_ID=" + exportInfo.getId();
        String jsCode = " if (window.epnyProcessingWindow) {window.epnyProcessingWindow.close();} ";
        jsCode = jsCode + "window.open('" + prepareDynamicURL(queryString) + "');";
        setResult("jsCode", jsCode);
        setStatus(JSCommandExtensionBase.STATUS_SUCCESS);
        return 0;
    }
    
	
}
