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
package com.ssaglobal.scm.wms.wm_global_preferences.ui;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ccf.AbstractProcessingTask;
import com.epiphany.shr.ui.action.ccf.JSCommandExtensionBase;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
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
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.wm_flow_thru_lane.FTLFormLaneTypeWidgetOnChange;

public class ExportGlobalPreferencesToXLS extends AbstractProcessingTask
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ExportGlobalPreferencesToXLS.class);

    public ExportGlobalPreferencesToXLS()
    {
    }

    protected void initialize(RuntimeFormInterface runtimeform, RuntimeFormWidgetInterface formWidget, HashMap hashMap)
        throws EpiException
    {
    	RuntimeListForm allocForm= null;
    	RuntimeListForm localForm = null;
    	
        RuntimeForm parentForm = (RuntimeForm)runtimeform.getParentForm(state);
        if("LIST".equals(parentForm.getFormType()))
            {listForm = (RuntimeListForm)parentForm;       	
            }
        else
            {
        	listForm = (RuntimeListForm)state.getRuntimeForm(parentForm.getSubSlot("List_Slot"), 0);
            //_log.debug("LOG_SYSTEM_OUT","\n\n 2nd form name: " +listForm.getName(),100L);
            }
        try
        {
            selectedItems = listForm.getSelectedItems();
            size = selectedItems.size();
        }
        catch(Exception e)
        {
            _logger.debug("LOG_EXPORT_XLS_GET_SELECTED", "Error getting selected items", e);
        }
        if(size == 0)
        {
        	//_log.debug("LOG_SYSTEM_OUT","\n\n NOTHING SELECTED IN HERE",100L);
            com.epiphany.shr.ui.model.data.DataBean d = listForm.getFocus();
            //DataBean allocBean = allocForm.getFocus();
           // DataBean localBean = localForm.getFocus();
            bioCollectionBean = (BioCollectionBean)d;
              
            size = bioCollectionBean.getSize(false);

            String sql= "select GLOBALPREFID from globalprefs where type='BOOLEAN' " +
            					"OR type='STRING' AND GLOBALPREFKEY='ALLOCATE_HOW ' " +
            					"OR GLOBALPREFKEY='LOCAL_EDITOR'";
            
			EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
			_log.debug("LOG_SYSTEM_OUT","\n\n** Records returned : " +dataObject.getCount(),100L);
            				
            
    
			
	
			int sizeTest = dataObject.getCount();
			String sqlString= "";
			
			for(int i =0; i< sizeTest ; i++)
			{
				//_log.debug("LOG_SYSTEM_OUT","\n " +dataObject.getAttribValue(new TextData("GLOBALPREFID")).toString(),100L);			
				if(i == 0)
				{
					//_log.debug("LOG_SYSTEM_OUT","\n***In index 0",100L);
					sqlString = sqlString + "wm_global_preferences.GLOBALPREFID ='" +dataObject.getAttribValue(new TextData("GLOBALPREFID")).toString() +"'";
				}
				else
				{
					sqlString = sqlString + " OR wm_global_preferences.GLOBALPREFID ='" +dataObject.getAttribValue(new TextData("GLOBALPREFID")).toString() +"'";
				}
				
				if(i< sizeTest-1)
				{
				dataObject.deleteCurrentRow();
				}
			}
			//_log.debug("LOG_SYSTEM_OUT","\n***Final String: " +sqlString,100L);
			
		
	    	UnitOfWorkBean uow = state.getDefaultUnitOfWork();
        	Query q= new Query("wm_global_preferences", sqlString, null);
			newFocus = uow.getBioCollectionBean(q);
			_log.debug("LOG_SYSTEM_OUT","\n\n Finally No. of records returned: " + newFocus.size(),100L);
            
        }
        if(size == 0)
            throw new FormException("ERRMSG_EXPORT_DATA_MISSING", new Object[0]);
        else
            return;
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
            exportHelper.exportBioCollectionBean(newFocus);
        exportInfo.setData(baos.toByteArray());
        state.getRequest().getSession().setAttribute(exportInfo.getId(), exportInfo);
        String queryString = "EXPORT_REQUEST=true&EXPORT_INFO_ID=" + exportInfo.getId();
        String jsCode = " if (window.epnyProcessingWindow) {window.epnyProcessingWindow.close();} ";
        jsCode = jsCode + "window.open('" + prepareDynamicURL(queryString) + "');";
        setResult("jsCode", jsCode);
        setStatus(JSCommandExtensionBase.STATUS_SUCCESS);
        return 0;
    }

    public static final String PARAM_POPUP_THRESHOLD = "popupThreshold";
    private RuntimeListForm listForm;
    private List selectedItems;
    private BioCollectionBean bioCollectionBean;
    private BioCollectionBean newFocus;
    private int size;
}
