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
package com.ssaglobal.scm.wms.wm_task_manager_area.ui;

import java.util.Iterator;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.DataValue;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class OnChangeAreaKey extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(OnChangeAreaKey.class);
	protected int execute(ActionContext context, ActionResult result) throws EpiException{

		_log.debug("LOG_DEBUG_EXTENSION_AREA","Executing OnChangeAreaKey",100L);	
		String newArea= null;
		
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		RuntimeFormInterface subForm = state.getCurrentRuntimeForm();
		RuntimeFormInterface detailForm = subForm.getParentForm(state);
		RuntimeFormInterface tabShellForm = null;
		tabShellForm = detailForm.getParentForm(state);
		
		SlotInterface tabGroupSlot= tabShellForm.getSubSlot("tbgrp_slot");
		RuntimeFormInterface headerForm= state.getRuntimeForm(tabGroupSlot, "tab 1");
		
		RuntimeFormWidgetInterface areaKey= subForm.getFormWidgetByName("AREAKEY");
		boolean keyIsNull= checkNull(areaKey);
		if(keyIsNull)
		{
			String[] param = new String[1];
			param[0]= "Area Key";
			throw new UserException("WMEXP_REQ", param);
		}
		else
		{
		String value= subForm.getFormWidgetByName("AREAKEY").getDisplayValue();		
		newArea= checkAreaKey(value);
		areaKey.setDisplayValue(newArea);
		}
		RuntimeFormWidgetInterface allowWidget= headerForm.getFormWidgetByName("ALLOWVOICE");
		RuntimeFormWidgetInterface maxWidget= headerForm.getFormWidgetByName("MAXNUMWORKIDS");
		RuntimeFormWidgetInterface skipWidget= headerForm.getFormWidgetByName("SKIPLOCALLOWED");
		RuntimeFormWidgetInterface repickWidget= headerForm.getFormWidgetByName("REPICKSKIPS");
		RuntimeFormWidgetInterface speakWidget= headerForm.getFormWidgetByName("SPEAKDESCRIPTION");
		

		String sql ="select * from area where AREAKEY='"+newArea+"'";
		
		EXEDataObject dataObject;
		try {
			dataObject = WmsWebuiValidationSelectImpl.select(sql);
			if(dataObject.getCount()> 0){
				
				String qry = "wm_area.AREAKEY=" +"'" +newArea +"'";
    			Query BioQuery = new Query("wm_area", qry, null);
    			BioCollectionBean headerVal = uow.getBioCollectionBean(BioQuery);
    			int size = headerVal.size();
    			if  (size != 0){
    				if(headerForm.getFocus().isTempBio())
    				{
    					//_log.debug("LOG_SYSTEM_OUT","\n\n**** neantype: " +headerForm.getFocus().getBeanType(),100L);
    				/*	
    				QBEBioBean qbeHeader= (QBEBioBean)headerForm.getFocus();
    
    				
    				qbeHeader.set("ALLOWVOICE", headerVal.get("0").getValue("ALLOWVOICE"));
    				qbeHeader.set("MAXNUMWORKIDS", headerVal.get("0").getValue("MAXNUMWORKIDS"));
    				qbeHeader.set("SKIPLOCALLOWED", headerVal.get("0").getValue("SKIPLOCALLOWED"));
    				qbeHeader.set("REPICKSKIPS", headerVal.get("0").getValue("REPICKSKIPS"));
    				qbeHeader.set("SPEAKDESCRIPTION", headerVal.get("0").getValue("SPEAKDESCRIPTION"));
    				qbeHeader.set("PICKPROMPT", headerVal.get("0").getValue("PICKPROMPT"));
    				qbeHeader.set("SPEAKWORKID", headerVal.get("0").getValue("SPEAKWORKID"));
    				qbeHeader.set("SIGNOFFALLOWED", headerVal.get("0").getValue("SIGNOFFALLOWED"));
    				qbeHeader.set("DELIVERY", headerVal.get("0").getValue("DELIVERY"));
    				qbeHeader.set("CURRENTAISLE", headerVal.get("0").getValue("CURRENTAISLE"));
    				qbeHeader.set("CURRENTSLOT", headerVal.get("0").getValue("CURRENTSLOT"));
    				qbeHeader.set("VERIFYPUTQTY", headerVal.get("0").getValue("VERIFYPUTQTY"));
    				
    				_log.debug("LOG_SYSTEM_OUT","allowvoice: " +qbeHeader.get("ALLOWVOICE").toString(),100L);
    				_log.debug("LOG_SYSTEM_OUT","max num workids: " +qbeHeader.get("MAXNUMWORKIDS").toString(),100L);
    				_log.debug("LOG_SYSTEM_OUT","skip allowed:" +qbeHeader.get("SKIPLOCALLOWED").toString(),100L);
    				_log.debug("LOG_SYSTEM_OUT","repickskips: " +qbeHeader.get("REPICKSKIPS").toString(),100L);
    				_log.debug("LOG_SYSTEM_OUT","speak descp: " +qbeHeader.get("SPEAKDESCRIPTION").toString(),100L);
    				
    				allowWidget.setDisplayValue(qbeHeader.get("ALLOWVOICE").toString());
    				maxWidget.setDisplayValue(qbeHeader.get("MAXNUMWORKIDS").toString());
    				skipWidget.setDisplayValue(qbeHeader.get("SKIPLOCALLOWED").toString());
    				repickWidget.setDisplayValue(qbeHeader.get("REPICKSKIPS").toString());
    				speakWidget.setDisplayValue(qbeHeader.get("SPEAKDESCRIPTION").toString());
    				
    				
    				_log.debug("LOG_SYSTEM_OUT","\n\nNew VAl: " +maxWidget.getDisplayValue(),100L);
    				_log.debug("LOG_SYSTEM_OUT","\n\nDone!!!",100L);
    				*/
    				}
    				else
    				{
    					/*
    					_log.debug("LOG_SYSTEM_OUT","\n\n**** BIOBEAN: " +headerForm.getFocus().getBeanType(),100L);
    					BioBean bioHeader= (BioBean)headerForm.getFocus();
    					bioHeader.set("ALLOWVOICE", headerVal.get("0").getValue("ALLOWVOICE"));
        				bioHeader.set("MAXNUMWORKIDS", headerVal.get("0").getValue("MAXNUMWORKIDS"));
        				bioHeader.set("SKIPLOCALLOWED", headerVal.get("0").getValue("SKIPLOCALLOWED"));
        				bioHeader.set("REPICKSKIPS", headerVal.get("0").getValue("REPICKSKIPS"));
        				bioHeader.set("SPEAKDESCRIPTION", headerVal.get("0").getValue("SPEAKDESCRIPTION"));
        				bioHeader.set("PICKPROMPT", headerVal.get("0").getValue("PICKPROMPT"));
        				bioHeader.set("SPEAKWORKID", headerVal.get("0").getValue("SPEAKWORKID"));
        				bioHeader.set("SIGNOFFALLOWED", headerVal.get("0").getValue("SIGNOFFALLOWED"));
        				bioHeader.set("DELIVERY", headerVal.get("0").getValue("DELIVERY"));
        				bioHeader.set("CURRENTAISLE", headerVal.get("0").getValue("CURRENTAISLE"));
        				bioHeader.set("CURRENTSLOT", headerVal.get("0").getValue("CURRENTSLOT"));
        				bioHeader.set("VERIFYPUTQTY", headerVal.get("0").getValue("VERIFYPUTQTY"));
        				*/
    				}
    				
    			}

				
			}else{
				//_log.debug("LOG_SYSTEM_OUT","\n\n** Record Does Not Exists: " +sql,100L);
				}
		} catch (DPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DataBean focus = state.getFocus();
		QBEBioBean qbe = null;
		BioBean bio = null;
		

			

		_log.debug("LOG_DEBUG_EXTENSION_AREA","Exiting OnChangeAreaKey",100L);	
		return RET_CONTINUE;
	}

	private String checkAreaKey(String keyStr) throws UserException{
		// TODO Auto-generated method stub
		/* kkuchipu SCM-00000-04386
		 *  commented if loop entirely and added below line to convert Area to upper string. 
		 *  User will be able to enter special characters in Area Key.
		 */
			keyStr= keyStr.toUpperCase();
		/*
	       if(keyStr.matches("[a-zA-Z0-9]+"))
	        {
	        	keyStr= keyStr.toUpperCase();
	        }
	        else 
	        {
	       	 String [] param = new String[2];
	       	 param[0] = keyStr;
	       	 param[1] = "Area Key";
	       	 throw new UserException("WMEXP_SP_CHARS", param);
			}
			
		*/
			return keyStr;
	}

	private boolean checkNull(RuntimeFormWidgetInterface widget) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		if(widget.getDisplayValue()==null)
			return true;
		else if(widget.getValue().toString().matches("\\s*"))
			return true;
		else 
			return false;
	}
}
