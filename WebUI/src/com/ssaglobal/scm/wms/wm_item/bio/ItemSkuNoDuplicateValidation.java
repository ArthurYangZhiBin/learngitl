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
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.util.validation.ValidationExtensionContext;

/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ItemSkuNoDuplicateValidation extends com.epiphany.shr.data.bio.extensions.BioExtensionBase
{
	private static String BIO = "sku";

	private static String ATTRIBUTE = "SKU";

	private static String ERROR_MESSAGE = "Duplicate Item - ";

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemSkuNoDuplicateValidation.class);

	public ItemSkuNoDuplicateValidation()
	{
		_log.info("EXP_1", "ItemSkuNoDuplicateValidation has been instantiated...", SuggestedCategory.NONE);

	}

	/**
	 * Returning CANCEL from this will stop the update Creating new bios or
	 * changing bios in the Bio's unit of work will cause them to be added to
	 * the Bios to be processed in this UnitOfWork.Insert();
	 * <P>
	 * 
	 * @param context
	 *            The EpnyServiceContext for this BioExtension instance
	 * @param bio
	 *            The Bio for this BioExtension instance
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
	 * @exception EpiException
	 */
	protected int bioBeforeInsert(EpnyServiceContext context, Bio bio) throws EpiException
	{
		return validateItemSku(context, bio);
	}
	
	protected int bioFinalBeforeInsert(EpnyServiceContext context, Bio bio) throws EpiException
	{
		return validateItemSku(context, bio);
	}

	private int validateItemSku(EpnyServiceContext context, Bio bio)
	{
		_log.debug("LOG_SYSTEM_OUT","\n\n//// Start of validateBio",100L);
		String skuValue = null;
		try
		{
			// Retrieve value of SKU
			skuValue = bio.get("SKU").toString();
			_log.debug("LOG_SYSTEM_OUT","//// SKU: " + skuValue,100L);
			// Query Bio, see if SKU exists
			String queryStatement = BIO + "." + ATTRIBUTE + " = '" + skuValue + "'";
			_log.debug("LOG_SYSTEM_OUT","////query statement " + queryStatement,100L);
			Query query = new Query(BIO, queryStatement, null);
			BioCollection results = bio.getUnitOfWork().findByQuery(query);
			// If BioCollection size equals 0, return RET_CANCEL
			if (results.size() >= 1)
			{
				_log.debug("LOG_SYSTEM_OUT","////SKU Invalid",100L);

				throw new FormException(ERROR_MESSAGE + " SKU: " + skuValue, new Object[] {});

			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","////SKU Valid SKU: " + skuValue,100L);
			}
		} catch (Exception e)
		{

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

}
