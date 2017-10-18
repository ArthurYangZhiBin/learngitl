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

import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.epiphany.shr.data.beans.BioService;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.HelperBio;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.wms.util.datalayer.WMSDataLayerException;
import com.infor.scm.wms.util.validation.WMSValidationContext;

public class UnitOfWorkInterface {
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(UnitOfWorkInterface.class);
	private static boolean unstable = false;
	public BioCollection findByQuery(Query query, WMSValidationContext context) throws WMSDataLayerException{		
		
		DataLayerConnection.testConnection(context.getConnection());
		
		UnitOfWork uow = context.getConnection().getUnitOfWork();						
		try {
			return uow.findByQuery(query);
		} catch (EpiDataException e) {
			throw new WMSDataLayerException(e);
		} finally{
			uow.close();
		}
		
	}
	
	public HelperBio createHelperBioWithDefaults(String bioName, WMSValidationContext context) throws WMSDataLayerException{		
				
		DataLayerConnection.testConnection(context.getConnection());
		
		UnitOfWork uow = context.getConnection().getUnitOfWork();	
		try {
			return uow.createHelperBioWithDefaults(bioName);
		} catch (EpiDataException e) {
			throw new WMSDataLayerException(e);
		} finally{
			uow.close();			
		}
		
	}
	
	public void saveHelperBio(HelperBio bio, WMSValidationContext context) throws WMSDataLayerException{
		
		DataLayerConnection.testConnection(context.getConnection());
		
		UnitOfWork uow = context.getConnection().getUnitOfWork();	
		try {
			uow.createBio(bio);
			
			GregorianCalendar gc1 = new GregorianCalendar();
			uow.save();
			GregorianCalendar gc2 = new GregorianCalendar();
			_log.debug("LOG_SYSTEM_OUT","\n\n Save Time:"+(gc2.getTimeInMillis() - gc1.getTimeInMillis()),100L);			
		} catch (EpiDataException e) {
			throw new WMSDataLayerException(e);
		} finally{
			uow.close();	
		}		
	}
	
	public void saveHelperBios(ArrayList bios, WMSValidationContext context) throws WMSDataLayerException{
		
		DataLayerConnection.testConnection(context.getConnection());
		
		UnitOfWork uow = context.getConnection().getUnitOfWork();	
		try {
			for(int i = 0; i < bios.size(); i++){
				uow.createBio((HelperBio)bios.get(i));			
			}
			GregorianCalendar gc1 = new GregorianCalendar();
			uow.save();
			GregorianCalendar gc2 = new GregorianCalendar();
			_log.debug("LOG_SYSTEM_OUT","\n\n Save Time:"+(gc2.getTimeInMillis() - gc1.getTimeInMillis()),100L);			
		} catch (EpiDataException e) {
			throw new WMSDataLayerException(e);
		} finally{
			uow.close();	
		}		
	}
	
}
