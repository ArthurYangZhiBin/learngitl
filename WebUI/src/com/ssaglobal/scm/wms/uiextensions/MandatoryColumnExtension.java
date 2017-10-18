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
package com.ssaglobal.scm.wms.uiextensions;

import java.util.GregorianCalendar;
//import java.util.StringTokenizer;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioUtil;
import com.epiphany.shr.data.bio.extensions.BioExtensionBase;
import com.epiphany.shr.sf.EpnyServiceContext;
//import com.epiphany.shr.sf.EpnyServiceManagerFactory;
//import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

public class MandatoryColumnExtension extends BioExtensionBase {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(MandatoryColumnExtension.class);
	
	
	public MandatoryColumnExtension() {
		_log.info("EXP_1", "ManadatoryColumnExtension has been instantiated...", SuggestedCategory.NONE);
		
	}
	
	protected int bioBeforeDelete(EpnyServiceContext context, Bio bio ) throws EpiException {
		_log.info("EXP_1", "Delete has been instantiated...", SuggestedCategory.NONE);
		_log.debug("LOG_SYSTEM_OUT","pack key = " + bio.get("packkey"),100L);
		return super.bioBeforeDelete(context, bio);
		
	}
	/**
	 * Returning CANCEL from this will stop the update
	 * Creating new bios or changing bios in the Bio's unit of work
	 * will cause them to be added to the Bios to be processed 
	 * in this UnitOfWork.Insert();
         * <P>
	 * @param context The EpnyServiceContext for this BioExtension instance
	 * @param bio The Bio for this BioExtension instance
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
         * @exception EpiException 
	 */
	protected int bioBeforeUpdate( EpnyServiceContext context, Bio bio ) throws EpiException {

			// Replace the following line with your code,
			//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
			//	 as appropriate
			// Leave the line if the code is not going to handle 
			//  bioBeforeUpdate events
        	
		    // You can now access the bio normally
        	//newBio.set("Some Attribute Name", "Some Value");
        	// or
			
			String userId = (String)context.getUserContext().get("logInUserId");

			if ((BioUtil.getUninitializedNull(bio,"EDITWHO")) != null){
				bio.set("EDITWHO",userId);
				bio.set("EDITDATE",new GregorianCalendar());
			}
			
		return super.bioBeforeUpdate( context, bio );	
	}
	
	/**
	 * Returning CANCEL from this will stop the insert
	 * Creating new bios or changing bios in the Bio's unit of work
	 * will cause them to be added to the Bios to be processed 
	 * in this UnitOfWork.save();
         * <P>
	 * @param context The EpnyServiceContext for this BioExtension instance
	 * @param bio The Bio for this BioExtension instance
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
         * @exception EpiException 
	 */
	protected int bioBeforeInsert( EpnyServiceContext context, Bio bio ) throws EpiException {
		String userId = (String)context.getUserContext().get("logInUserId");
		if ((BioUtil.getUninitializedNull(bio,"ADDWHO")) == null){
			bio.set("ADDWHO",userId);
			bio.set("ADDDATE",new GregorianCalendar());
		}
		if ((BioUtil.getUninitializedNull(bio,"EDITWHO")) == null){
			bio.set("EDITWHO",userId);
			bio.set("EDITDATE",new GregorianCalendar());
		}

		return super.bioBeforeInsert( context, bio );		
	}
	
}
