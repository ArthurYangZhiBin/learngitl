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
package com.ssaglobal.scm.wms.wm_po.bio;

import java.math.BigDecimal;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;


public class validatePOBio extends com.epiphany.shr.data.bio.extensions.BioExtensionBase {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(validatePOBio.class);
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

		return super.bioBeforeUpdate( context, bio );	
	}

	/**
	 * Returning CANCEL from this will stop the operation 
	 * Cannot make any updates to the unit of work (create new bios/update existing bios) 
	 * during this extension
         * <P>
	 * @param context The EpnyServiceContext for this BioExtension instance
	 * @param bio The Bio for this BioExtension instance
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
         * @exception EpiException 
	 */
	protected int bioFinalBeforeUpdate( EpnyServiceContext context, Bio bio ) throws EpiException {

			// Replace the following line with your code,
			//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
			//	 as appropriate
			// Leave the line if the code is not going to handle 
			//  bioBeforeUpdate events

		return super.bioFinalBeforeUpdate( context, bio );	
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

			// Replace the following line with your code,
			//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
			//	 as appropriate
			// Leave the line if the code is not going to handle 
			//  bioBeforeInsert events

		return super.bioBeforeInsert( context, bio );		
	}
	
 	/**
	 * Returning CANCEL from this will stop the operation
	 * Cannot make any updates to the unit of work (create new bios/update existing bios)
	 * during this extension
         * <P>
	 * @param context The EpnyServiceContext for this BioExtension instance
	 * @param bio The Bio for this BioExtension instance
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
         * @exception EpiException 
	 */
        protected int bioFinalBeforeInsert( EpnyServiceContext context, Bio bio ) throws EpiException {

			// Replace the following line with your code,
			//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
			//	 as appropriate
			// Leave the line if the code is not going to handle 
			//  bioBeforeInsert events

		return super.bioFinalBeforeInsert( context, bio );		
	}
	
	/**
	 * Returning CANCEL from this will stop the delete
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
	protected int bioBeforeDelete( EpnyServiceContext context, Bio bio ) throws EpiException {
		_log.info("EXP_1", "Delete has been instantiated...", SuggestedCategory.NONE);
		_log.debug("LOG_SYSTEM_OUT","Inside bioBeforeDelete",100L);
		String pokey = bio.get("POKEY").toString();
		String[] process = new String[1];
		process[0] = pokey;
		BioCollection poDetailBiocollection = (BioCollection)bio.get("PODETAIL");
		int j = 0;
        for (j=0; j<poDetailBiocollection.size(); j++){
        	
        	Bio poDetailLine = (Bio)poDetailBiocollection.elementAt(j);
        	BigDecimal qtyReceived = (BigDecimal)poDetailLine.get("QTYRECEIVED");
        	if (qtyReceived.intValue() > 0 ){
        		throw new UserException("WMEXP_DOCUMENT_NODELETE_1", process);
        	}
        }
		return super.bioBeforeDelete( context, bio );		
	}

        /**
         * Returning CANCEL from this will stop the operation 
         * Cannot make any updates to the unit of work (create new bios/update existing bios) 
         * during this extension
         * <P>
	 * @param context The EpnyServiceContext for this BioExtension instance
	 * @param bio The Bio for this BioExtension instance
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
         * @exception EpiException 
	 */
        protected int bioFinalBeforeDelete( EpnyServiceContext context, Bio bio ) throws EpiException {
		return super.bioFinalBeforeDelete( context, bio );		
	}

	/**
	 * Returning CANCEL from these will NOT stop the update
	 * It is called after the transaction is committed
	 *  To do any saving,  the client must be called on a new UnitOfWork.
         * <P>
	 * @param context The EpnyServiceContext for this BioExtension instance
	 * @param bioChanged The BioRef for this BioExtension instance
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
         * @exception EpiException 
	 */
	protected int bioAfterUpdate(EpnyServiceContext context, BioRef bioChanged) throws EpiException {

			// Replace the following line with your code,
			//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
			//	 as appropriate
			// Leave the line if the code is not going to handle 
			//  bioAfterUpdate events

		return super.bioAfterUpdate( context, bioChanged );
	}

	/**
	 * Returning CANCEL from these will NOT stop the insert
	 * It is called after the transaction is committed
    	 *  To do any saving,  the client must be called on a new UnitOfWork.
         * <P>
	 * @param context The EpnyServiceContext for this BioExtension instance
	 * @param bioInserted The BioRef for this BioExtension instance
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
         * @exception EpiException 
	 */
	protected int bioAfterInsert(EpnyServiceContext context, BioRef bioInserted) throws EpiException {

			// Replace the following line with your code,
			//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
			//	 as appropriate
			// Leave the line if the code is not going to handle 
			//  bioAfterInsert events

		return super.bioAfterInsert( context, bioInserted );	
	}
	
	/**
	 * Returning CANCEL from these will NOT stop the delete
	 * It is called after the transaction is committed
	 *  To do any saving,  the client must be called on a new UnitOfWork.
         * <P>
	 * @param context The EpnyServiceContext for this BioExtension instance
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
         * @exception EpiException 
	 */
	protected int bioAfterDelete(EpnyServiceContext context) throws EpiException {

			// Replace the following line with your code,
			//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
			//	 as appropriate
			// Leave the line if the code is not going to handle 
			//  bioAfterDelete events

		return super.bioAfterDelete( context );
	}
}
