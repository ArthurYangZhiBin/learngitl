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
package com.ssaglobal.scm.wms.wm_assign_accessorial_charges.ui;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.SsaException;
import com.ssaglobal.scm.wms.datalayer.EJBRemote;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.service.exeprocessmanager.TransactionServiceSORemote;
import com.ssaglobal.scm.wms.uiextensions.KeyGenBioWrapper;

public class CreateKeyVal extends FormExtensionBase {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CreateKeyVal.class);
	protected static final String internalCaller = "internalCall";
   	public CreateKeyVal(){
   	}
   	
   	
   	protected int preRenderForm(UIRenderContext context,RuntimeNormalFormInterface form) throws EpiException {
   	 _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Executing CreateKeyVal",100L);
   	 
   		try{
   			String keyName = getParameter("KEYNAME").toString();
   			String KeyField = getParameter("WIDGETNAME").toString();
   			AssignKey(context,keyName,KeyField);
   		}  catch (Exception x) {
   			x.printStackTrace();
   			return RET_CANCEL;
   		}
   		_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Exiting CreateKeyVal",100L);
   		return RET_CONTINUE;
   		
   	}
   		
   	public void AssignKey(UIRenderContext context, String keyname, String KeyField){
   		_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","In AssignKey",100L);
   		String widgetName = context.getActionObject().getName();	
   		if (widgetName.equals("Create Charge")) {
   			int rowCount = 0;
   			TextData myKey = null;
   			Array argArray = new Array();
   			EXEDataObject edo =null;
   			argArray.add(new TextData(keyname));
   			argArray.add(new TextData("10"));
   			argArray.add(new TextData("1"));
   			//   		Added following code to get the db_connection information from the usre context

   			EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
//			String locale = userContext.getLocale();
   			String locale = getBaseLocale(userContext);
   			String db_connection = userContext.get(SetIntoHttpSessionAction.DB_CONNECTION).toString();
   			String wmWhseID = (userContext.get(SetIntoHttpSessionAction.DB_USERID)).toString();

  			try {
   				TransactionServiceSORemote remote = EJBRemote.getRemote();
   				edo = remote.executeProcedure(new TextData(wmWhseID),db_connection,new TextData("GETKEYP1S1"),argArray,null,internalCaller,locale);		//HC
   				rowCount = edo.getRowCount();
   				if (edo._rowsReturned())
   				{
   				  myKey = edo.getAttribValue(new TextData("keyname")).asTextData();
   				}

   			} catch (SsaException x) {
   				_log.error("EXP_1","Could not get the remote...",SuggestedCategory.NONE);
   				_log.error(new EpiException("EXP_1", "SsaException nested in EpiException...", x.getCause()));
            
   			} catch (Exception exc) {
            	exc.printStackTrace();
            	EpiException x = new EpiException("EXP_1", "Unknown", exc);
            	_log.error(x);
            }

   			StateInterface state = context.getState();
   			DataBean db = state.getFocus();
   			if (db.getValue(KeyField) == null){ 
   	      		db.setValue(KeyField, myKey.toString()); 
   			}
   	}
   	} 	
   	
   	public String getKey(String keyname){   		
   		
   			int rowCount = 0;
   			TextData myKey = null;
   			Array argArray = new Array();
   			EXEDataObject edo =null;
   			argArray.add(new TextData(keyname));
   			argArray.add(new TextData("10"));
   			argArray.add(new TextData("1"));
   			//   		Added following code to get the db_connection information from the usre context
   			EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
//   			String locale = userContext.getLocale();
   			String locale = getBaseLocale(userContext);
   			_log.debug("LOG_SYSTEM_OUT","LOCALE = "+ locale,100L);
   			String db_connection = userContext.get(SetIntoHttpSessionAction.DB_CONNECTION).toString();
   			String wmWhseID = (userContext.get(SetIntoHttpSessionAction.DB_USERID)).toString();

  			try {
   				TransactionServiceSORemote remote = EJBRemote.getRemote();
   				edo = remote.executeProcedure(new TextData(wmWhseID),db_connection,new TextData("GETKEYP1S1"),argArray,null,internalCaller,locale);		//HC
   				rowCount = edo.getRowCount();
   				if (edo._rowsReturned())
   				{
   				  myKey = edo.getAttribValue(new TextData("keyname")).asTextData();
   				  return myKey.getAsString();
   				}

   			} catch (SsaException x) {
   				_log.error("EXP_1","Could not get the remote...",SuggestedCategory.NONE);
   				_log.error(new EpiException("EXP_1", "SsaException nested in EpiException...", x.getCause()));
            
   			} catch (Exception exc) {
            	exc.printStackTrace();
            	EpiException x = new EpiException("EXP_1", "Unknown", exc);
            	_log.error(x);
            }
   		return null;
   	}   	 
   	
   	//If the Locale does not have the country code attached to the language code then assign the default country for that language.
	public static String getBaseLocale(EpnyUserContext userContext){
		String locale = userContext.getLocale();
		if (locale.indexOf("_") == -1){
			if (locale.equalsIgnoreCase("en")){
				locale = locale + "_US";
			}
			if (locale.equalsIgnoreCase("de")){
				locale = locale + "_DE";
			}
			if (locale.equalsIgnoreCase("es")){
				locale = locale + "_ES";
			}
			if (locale.equalsIgnoreCase("nl")){
				locale = locale + "_NL";
			}
			if (locale.equalsIgnoreCase("ja")){
				locale = locale + "_JP";
			}
			if (locale.equalsIgnoreCase("pt")){
				locale = locale + "_BR";
			}
			if (locale.equalsIgnoreCase("zh")){
				locale = locale + "_CN";
			}
			if (locale.equalsIgnoreCase("fr")){
				locale = locale + "_FR";
			}
		}
		return locale;
	}
}

