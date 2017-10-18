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
package com.infor.scm.wms.util.datalayer.driver;

import java.rmi.RemoteException;
import java.security.InvalidParameterException;
import java.util.GregorianCalendar;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;

import com.epiphany.shr.authn.EpnyAuthenticationService;
import com.epiphany.shr.authn.EpnyAuthenticationServiceHome;
import com.epiphany.shr.data.beans.BioService;
import com.epiphany.shr.data.beans.BioServiceFactory;
import com.epiphany.shr.data.beans.ejb.BioServiceExtended;
import com.epiphany.shr.data.beans.ejb.BioServiceExtendedHome;
import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.EpiSecurityException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.wms.util.datalayer.WMSDataLayerException;
import com.infor.scm.wms.util.validation.WMSValidationContext;
import com.ssaglobal.SsaException;
import com.ssaglobal.util.EjbHelper;

public class DataLayerConnection{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(DataLayerConnection.class);
	public static String DB_CONNECTION = "dbConnectionName";
	public static String DB_USERID =	"dbUserName";
	public static String DB_PASSWORD =	"dbPassword";
	public static String DB_DATABASE = "dbDatabase";
	public static String DB_ISENTERPRISE = "dbIsEnterprise";
	public static String DB_SERVER = "dbServer";
	public static String DB_TYPE = "dbType";
	public static String LOG_IN_USER_ID = "logInUserId";
	Object bioService = null;
	
	public DataLayerConnection(WMSValidationContext context) throws WMSDataLayerException{
		_log.debug("LOG_SYSTEM_OUT","\n\n Creating Connection!!!\n\n",100L);
		this.bioService = getBioService(context);
	}
	
	private Object getBioService(WMSValidationContext context) throws WMSDataLayerException{
		try {
			BioServiceFactory serviceFactory = BioServiceFactory.getInstance();
			BioService bioService = serviceFactory.create("webui");
			//idleBioServiceConnections.add(bioService);
			return bioService;
		} catch (EpiException e) {					
			try {
				_log.debug("LOG_SYSTEM_OUT","[DataLayerConnection]jpdebug:Factory:"+context.getNamingFactory(),100L);
				/*
				EpnyAuthenticationService authnService = (EpnyAuthenticationService) EjbHelper.getSession(context.getOaServerConnectionString(), null, null, 
						"org.jnp.interfaces.NamingContextFactory" ,"EpnyAuthenticationService", EpnyAuthenticationServiceHome.class);
						*/
				EpnyAuthenticationService authnService = (EpnyAuthenticationService) EjbHelper.getSession(context.getOaServerConnectionString(), null, null, 
						context.getNamingFactory() ,"EpnyAuthenticationService", EpnyAuthenticationServiceHome.class);

				EpnyUserContext epnyUserCtx = authnService.getCredentials(context.getUserName(), context.getPassword(), null);						
				epnyUserCtx.put(DB_ISENTERPRISE, context.getIsEnterprise());
				epnyUserCtx.put(DB_CONNECTION, context.getFacilityConnection());
				epnyUserCtx.put(DB_DATABASE, context.getDatabaseName());
				epnyUserCtx.put(DB_USERID, context.getDatabaseUser());
				epnyUserCtx.put(DB_PASSWORD, context.getDatabasePassword());
				epnyUserCtx.put(LOG_IN_USER_ID, context.getUserName());
				/*
				BioServiceExtendedHome bioServiceHome = (BioServiceExtendedHome) EjbHelper.getEntityHome(context.getOaServerConnectionString(), 
						 null, null, "org.jnp.interfaces.NamingContextFactory" ,"BioServiceExtended", BioServiceExtendedHome.class);
						 */
				_log.debug("LOG_SYSTEM_OUT","[DataLayerConnection]jpdebug:BioServiceExtended:Factory:"+context.getNamingFactory(),100L);
				BioServiceExtendedHome bioServiceHome = (BioServiceExtendedHome) EjbHelper.getEntityHome(context.getOaServerConnectionString(), 
						 null, null, context.getNamingFactory() ,"BioServiceExtended", BioServiceExtendedHome.class);

				 BioServiceExtended bioService =  bioServiceHome.create(epnyUserCtx, "webui");				 
				 return bioService;
				 //idleBioServiceConnections.add(bioService);							 
			} catch (RemoteException e1) {
				throw new WMSDataLayerException(e1);
			} catch (SsaException e1) {
				throw new WMSDataLayerException(e1);
			} catch (CreateException e1) {
				throw new WMSDataLayerException(e1);
			} catch (EpiSecurityException e1) {
				throw new WMSDataLayerException(e1);
			} catch (EpiException e1) {
				throw new WMSDataLayerException(e1);
			}
		} 									
	}
	
	public UnitOfWork getUnitOfWork() throws WMSDataLayerException{
		UnitOfWork uow = null;
		
		if(bioService == null)
			return null;
		
		if(bioService instanceof BioService){
			GregorianCalendar gc1 = new GregorianCalendar();
			uow = ((BioService)bioService).getUnitOfWork();	
			GregorianCalendar gc2 = new GregorianCalendar();
			_log.debug("LOG_SYSTEM_OUT","\n\n UnitOfWorkCreation Time:"+(gc2.getTimeInMillis() - gc1.getTimeInMillis()),100L);
			
		}
		else{
			try {
				uow = ((BioServiceExtended)bioService).getUnitOfWork();				
			} catch (EpiSecurityException e) {
				throw new WMSDataLayerException(e);
			} catch (RemoteException e) {
				throw new WMSDataLayerException(e);
			}		
		}
		return uow;
	}
	
	public void cleanUp() throws WMSDataLayerException{
		try {
			if(bioService instanceof BioService)
				((BioService)bioService).remove();
			else if(bioService instanceof BioServiceExtended)
				((BioServiceExtended)bioService).remove();
		} catch (RemoteException e) {
			throw new WMSDataLayerException(e);
		} catch (RemoveException e) {
			throw new WMSDataLayerException(e);
		}
		bioService = null;
	}
	
	public static void testConnection(DataLayerConnection conn) throws WMSDataLayerException{
		
		if(conn == null)
			throw new WMSDataLayerException(new InvalidParameterException("Connection is null"));
		
	}
}