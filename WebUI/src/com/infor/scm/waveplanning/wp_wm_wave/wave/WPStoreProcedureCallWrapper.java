package com.infor.scm.waveplanning.wp_wm_wave.wave;

import com.agileitp.forte.framework.GenericException;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.EJBRemote;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.service.exeprocessmanager.TransactionServiceSORemote;

public class WPStoreProcedureCallWrapper {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPStoreProcedureCallWrapper.class);
	public static EXEDataObject doAction(WaveInputObj waveInputObj) throws WebuiException{
    	EXEDataObject edo =null;
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		String locale = WPUtil.getBaseLocale(userContext);
		_log.debug("WPStoreProcedureCallWrapper", "Locale is " + locale, SuggestedCategory.NONE);
   		String db_connection = userContext.get(SetIntoHttpSessionAction.DB_CONNECTION).toString();
   		String wmWhseID = (userContext.get("logInUserId")).toString();
		try{
				TransactionServiceSORemote remote = EJBRemote.getRemote();				
				_log.debug("LOG_SYSTEM_OUT","wmWhseID="+wmWhseID+" db_connection="+db_connection
						+"ProcedureName="+waveInputObj.getProcedureName(),100L);
				edo = remote.executeProcedure(new TextData(wmWhseID),db_connection,new TextData(waveInputObj.getProcedureName()),waveInputObj.getProcedureParametes(),null,WPConstants.INTERNAL_CALLER,locale);		
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
                _log.debug("LOG_SYSTEM_OUT","it is in action exception. EJB message as follows:\n"+reasonCode,100L);
                throw new WebuiException(reasonCode); 
        }
	}
}
