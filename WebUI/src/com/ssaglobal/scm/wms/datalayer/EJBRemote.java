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


import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.SsaException;
import com.ssaglobal.scm.wms.service.exeprocessmanager.TransactionServiceSORemote;
import com.ssaglobal.scm.wms.service.exeprocessmanager.TransactionServiceSORemoteHome;
import com.ssaglobal.util.EjbHelper;
import com.ssaglobal.SsaAccessBase;
import com.ssaglobal.util.EjbHelper;
import com.ssaglobal.base.Enterprise;




public class EJBRemote {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(EJBRemote.class);
	public static TransactionServiceSORemote getRemote() throws SsaException {
		try {
			_log.debug("LOG_SYSTEM_OUT","In side the getRemote",100L);

			Enterprise ent = SsaAccessBase.getEnterprise("webUI", "TransactionServiceSORemote");
			String provider = ent.getProvider();
			String factory = ent.getFactory();
			String jndiName = ent.getJndiName();
_log.debug("LOG_SYSTEM_OUT","@@@@@@@provider="+provider,100L);		
_log.debug("LOG_SYSTEM_OUT","@@@@@@@factory="+factory,100L);		
_log.debug("LOG_SYSTEM_OUT","@@@@@@@jndiName="+jndiName,100L);		
			return (TransactionServiceSORemote) EjbHelper.getSession(provider, null, null,factory, 
					jndiName, TransactionServiceSORemoteHome.class);

			 

			
			
/*			WebuiConfig webConfig = new WebuiPropertyMappingObject().getWebuiConfig();
			String provider = webConfig.getTransactionserviceprovider_server()+":"+webConfig.getTransactionserviceprovider_port();
			String factory = webConfig.getTransactionservicefactory();
			
			
			
			//			HC			return (TransactionServiceSORemote) EjbHelper.getSession("usdawvssawm2:6840", null, null,
			return (TransactionServiceSORemote) EjbHelper.getSession(provider, null, null,
					factory,"TransactionServiceSORemote", TransactionServiceSORemoteHome.class);
					*/
		} catch (Exception x) {
			x.printStackTrace();
			_log.info("*****"," it is in get remote exception ---->",SuggestedCategory.NONE);
			throw new SsaException("[getRemote]::Transaction service remote not accessible", x);
		}
	}
}
