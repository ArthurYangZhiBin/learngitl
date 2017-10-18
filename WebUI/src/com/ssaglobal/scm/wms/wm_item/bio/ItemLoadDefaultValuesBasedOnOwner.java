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

package com.ssaglobal.scm.wms.wm_item.bio;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataReadUninitializedException;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ItemLoadDefaultValuesBasedOnOwner extends com.epiphany.shr.data.bio.extensions.BioExtensionBase
{

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
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemLoadDefaultValuesBasedOnOwner.class);

	protected int bioBeforeInsert(EpnyServiceContext context, Bio bio)
			throws EpiException
	{
		_log.debug("LOG_SYSTEM_OUT","\n\n\n@##$!@ Start of ItemLoadDefaultValuesBasedOnOwner",100L);
		//Retrieve Owner
		String owner;
		try
		{
			owner = bio.get("STORERKEY").toString();
		}
		catch(EpiDataReadUninitializedException e)
		{
			_log.debug("LOG_SYSTEM_OUT","\n!@# Owner not set, doing nothing",100L);
			return RET_CANCEL;
		}
		_log.debug("LOG_SYSTEM_OUT","! Owner : " + owner,100L);
		
		//Query STORER Bio to get Rotate By, Rotation, Strategy, Putaway Strategy, Receipt Validation
		final String BIO = "wm_storer";
		final String ATTRIBUTE = "STORERKEY";
		String queryStatement = BIO + "." + ATTRIBUTE + " = '" + owner + "'";
		_log.debug("LOG_SYSTEM_OUT","////query statement " + queryStatement,100L);
		Query query = new Query(BIO, queryStatement, null);
		BioCollection results = bio.getUnitOfWork().findByQuery(query);
		
		if( results.size() != 1)
		{
			_log.debug("LOG_SYSTEM_OUT","Invalid Owner, too many matching records returned",100L);
			return RET_CANCEL;
		}
		
		//Set values
		Bio ownerBio = results.elementAt(0);
		bio.set("PUTAWAYSTRATEGYKEY", ownerBio.get("DEFAULTPUTAWAYSTRATEGY"));
		bio.set("RECEIPTVALIDATIONTEMPLATE", ownerBio.get("RECEIPTVALIDATIONTEMPLATE"));
		bio.set("ROTATEBY", ownerBio.get("DEFAULTSKUROTATION"));
		bio.set("DEFAULTROTATION", ownerBio.get("DEFAULTROTATION"));
		bio.set("STRATEGYKEY", ownerBio.get("DEFAULTSTRATEGY"));
		

		return RET_CONTINUE;
	}
}
