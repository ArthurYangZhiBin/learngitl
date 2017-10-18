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
package com.ssaglobal.scm.wms.datalayer;

import java.lang.reflect.Array;
import java.util.ArrayList;

import com.agileitp.forte.framework.GenericException;
import com.agileitp.forte.framework.TextData;
import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.service.exeprocessmanager.TransactionServiceSORemote;
//import com.ssaglobal.scm.wms.uiextensions.KeyGenBioWrapper;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;;

public class WmsWebuiActionsImpl {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WmsWebuiActionsImpl.class);
	protected static final String internalCaller = "internalCall";	
	
	public static EXEDataObject doAction(WmsWebuiActionsProperties proParametes) throws WebuiException{
    	EXEDataObject edo =null;
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
//		String locale = userContext.getLocale();
		String locale = getBaseLocale(userContext);
		_log.debug("LOG_SYSTEM_OUT","\n\n:Locale " + locale,100L);
		_log.debug("LOG_DEBUG_EXTENSION_WmsWebuiActionsImpl", "Locale is " + locale, SuggestedCategory.NONE);
   		String db_connection = userContext.get(SetIntoHttpSessionAction.DB_CONNECTION).toString();
		//String wmWhseID = (userContext.get(SetIntoHttpSessionAction.DB_USERID)).toString();
   		String wmWhseID = (userContext.get("logInUserId")).toString();
//		StringTokenizer dbstring = new StringTokenizer(db_connection,"_");
//	     while (dbstring.hasMoreTokens()) {
//	    	 wmWhseID = dbstring.nextToken();
//	     }
		try{
				TransactionServiceSORemote remote = EJBRemote.getRemote();				
				_log.debug("LOG_SYSTEM_OUT","\n\nRemote:"+remote.getClass().getName()+"\n\n",100L);
				_log.debug("LOG_SYSTEM_OUT","\n\n:wmWhseID"+wmWhseID+"\n\n",100L);
				_log.debug("LOG_SYSTEM_OUT","\n\n:db_connection"+db_connection+"\n\n",100L);
				_log.debug("LOG_SYSTEM_OUT","\n\n:proParametes.getProcedureName()"+proParametes.getProcedureName()+"\n\n",100L);
				_log.debug("LOG_SYSTEM_OUT","\n\n:proParametes.getProcedureParametes()"+proParametes.getProcedureParametes()+"\n\n",100L);
				_log.debug("LOG_SYSTEM_OUT","\n\n:Locale " + locale,100L);
				
   				edo = remote.executeProcedure(new TextData(wmWhseID),db_connection,new TextData(proParametes.getProcedureName()),proParametes.getProcedureParametes(),null,internalCaller,locale);		//HC
   				_log.debug("LOG_SYSTEM_OUT","\n\n edo:"+edo+"\n\n",100L);
   				
   				return edo;
  		} catch (Exception exc) {   			
            	exc.printStackTrace();
            	EpiException x = new EpiException("EXP_1", "Unknown", exc);
            	_log.error(x);
            	String reasonCode = "";
                if (exc instanceof GenericException) {
                    reasonCode = ((GenericException) exc).reasonCodeString();
                } else if (exc instanceof ServiceObjectException) {
                	if(exc.getCause() != null && exc.getCause() instanceof GenericException){
                		reasonCode = ((GenericException) exc.getCause()).getMessage();
                	}
                	else {
                		reasonCode = exc.getMessage();
                	}
                }
                else {
                    reasonCode = exc.getMessage();
                }             
_log.debug("LOG_SYSTEM_OUT","it is in action exception. EJB message as follows:\n"+reasonCode,100L);
                throw new WebuiException(reasonCode);
            	//return null;
        }
	}
	public static EXEDataObject doMFAction(WmsWebuiActionsProperties proParametes) throws WebuiException{
		EXEDataObject edo =null;
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();  
//		String locale = userContext.getLocale();
		String locale = getBaseLocale(userContext);

		_log.debug("LOG_DEBUG_EXTENSION_WmsWebuiActionsImpl", "Locale is " + locale, SuggestedCategory.NONE);
		//String wmWhseID = (userContext.get(SetIntoHttpSessionAction.DB_USERID)).toString();
		String wmWhseID = (userContext.get("logInUserId")).toString();
		try{
			TransactionServiceSORemote remote = EJBRemote.getRemote();								
			edo = remote.executeMFProcedure(new TextData(proParametes.getProcedureName()),proParametes.getDbNames(),proParametes.getProcedureParametes(),wmWhseID,internalCaller,locale);		//HC   				
			return edo;
		} catch (Exception exc) {   			
			exc.printStackTrace();
			EpiException x = new EpiException("EXP_1", "Unknown", exc);
			_log.error(x);
			String reasonCode = "";
			if (exc instanceof GenericException) {
				reasonCode = ((GenericException) exc).reasonCodeString();
			} else {
				reasonCode = exc.getMessage();
			}
			throw new WebuiException(reasonCode);            	
		}
	}
/*	public static TransactionServiceSORemote getTransactionServiceRemote() throws SsaException {
   		_log.info("EXP_1","In side getTransactionServiceRemote---> ",SuggestedCategory.NONE);
		ResourceBundle rbundle = ResourceBundle.getBundle("com.epiphany.shr.data.dp.sql.datasource");
		String provider = rbundle.getString("transactionserviceprovider");
		String factory = rbundle.getString("transactionservicefactory");

		return (TransactionServiceSORemote) EjbHelper.getSession(provider, null, null,
				factory,"TransactionServiceSORemote", TransactionServiceSORemoteHome.class);
   	}
*/
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
