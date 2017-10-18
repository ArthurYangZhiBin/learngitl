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

import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.service.exeprocessmanager.TransactionServiceSORemote;

public class WmsWebuiValidationUpdateImpl {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WmsDataProviderImpl.class);
	protected static final String internalCaller = "internalCall";
	
	public static EXEDataObject update(String updateSql) throws DPException
	{		
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();		
		String db_connection = (userContext.get(SetIntoHttpSessionAction.DB_CONNECTION)).toString();		
		String wmWhseID = (userContext.get(SetIntoHttpSessionAction.DB_USERID)).toString();		
		try
		{
			TransactionServiceSORemote remote = EJBRemote.getRemote();			
			EXEDataObject obj = remote.update(new TextData(wmWhseID), db_connection, new TextData(updateSql), null, true, internalCaller, null );
			//EXEDataObject obj1 = remote.insert(new TextData(wmWhseID),db_connection, new TextData(updateSql), true, null,internalCaller,null);
			_log.debug("LOG_SYSTEM_OUT","it is in update ="+updateSql+" datasourceName="+db_connection+" rowCounts="+obj.getRowCount(),100L);
			return obj;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw new DPException(ex.getMessage(), ex);
		}

	}
	
	public static EXEDataObject update(String updateSql, String db_connection, String wmWhseID) throws DPException
	{				
		try
		{
			TransactionServiceSORemote remote = EJBRemote.getRemote();			
			//_log.debug("LOG_SYSTEM_OUT","\n\n wmWhseID = "+wmWhseID+" db_connection = "+db_connection+"  insertSql = "+insertSql+"\n\n",100L);
			EXEDataObject obj = remote.update(new TextData(wmWhseID),db_connection, new TextData(updateSql), null, true,internalCaller,null);
			//_log.debug("LOG_SYSTEM_OUT","it is in insert ="+insertSql+" datasourceName="+db_connection+" rowCounts="+obj.getRowCount(),100L);
			return obj;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			throw new DPException(ex.getMessage(), ex);
		}

	}
}
