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

package com.ssaglobal.scm.wms.wm_pickdetail.bio;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.error.EpiDataReadUninitializedException;
import com.ssaglobal.scm.wms.wm_physical_parameters.ui.PhysicalParametersSearchAction;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class PickDetailPrintValues extends com.epiphany.shr.data.bio.extensions.BioExtensionBase
{

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
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PickDetailPrintValues.class);

	protected int bioBeforeUpdate(EpnyServiceContext context, Bio bio)
			throws EpiException
	{
		_log.debug("LOG_SYSTEM_OUT","\n\n\n\n!@# Start of Print Values - bioBeforeUpdate-------------------",100L);
		try
		{
			Object storerkey = bio.get("STORERKEY");
			if (storerkey != null)
			{
				_log.debug("LOG_SYSTEM_OUT","!@# StorerKey - " + storerkey.toString(),100L);
			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","StorerKey is null",100L);
			}
		} catch (EpiDataReadUninitializedException e)
		{
			_log.debug("LOG_SYSTEM_OUT","StorerKey caught Excepception",100L);
		}
		

		try
		{
			Object sku = bio.get("SKU");
			if (sku != null)
			{
				_log.debug("LOG_SYSTEM_OUT","!@# SKU - " + sku.toString(),100L);
			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","SKU is null",100L);
			}
		} catch (EpiDataReadUninitializedException e)
		{
			_log.debug("LOG_SYSTEM_OUT","SKU caught Excepception",100L);
		}
		
		try
		{
			Object toLoc = bio.get("TOLOC");
			if (toLoc != null)
			{
				_log.debug("LOG_SYSTEM_OUT","!@# TOLOC - " + toLoc.toString(),100L);
			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","TOLOC is null",100L);
			}
		} catch (EpiDataReadUninitializedException e)
		{
			_log.debug("LOG_SYSTEM_OUT","TOLOC caught Excepception",100L);
		}
		
		try
		{
			Object toLoc = bio.get("PICKHEADERKEY");
			if (toLoc != null)
			{
				_log.debug("LOG_SYSTEM_OUT","!@# PICKHEADERKEY - " + toLoc.toString(),100L);
			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","PICKHEADERKEY is null",100L);
			}
		} catch (EpiDataReadUninitializedException e)
		{
			_log.debug("LOG_SYSTEM_OUT","PICKHEADERKEY caught Excepception",100L);
		}
		_log.debug("LOG_SYSTEM_OUT","!@# End of Print Values - bioBeforeUpdate-------------------\n\n\n\n",100L);
		// Replace the following line with your code,
		//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		//	 as appropriate
		// Leave the line if the code is not going to handle 
		//  bioBeforeUpdate events

		return super.bioBeforeUpdate(context, bio);
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
	protected int bioFinalBeforeUpdate(EpnyServiceContext context, Bio bio)
			throws EpiException
	{
		_log.debug("LOG_SYSTEM_OUT","\n\n\n\n!@# Start of Print Values - bioFinalBeforeUpdate-------------------",100L);
		try
		{
			Object storerkey = bio.get("STORERKEY");
			if (storerkey != null)
			{
				_log.debug("LOG_SYSTEM_OUT","!@# StorerKey - " + storerkey.toString(),100L);
			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","StorerKey is null",100L);
			}
		} catch (EpiDataReadUninitializedException e)
		{
			_log.debug("LOG_SYSTEM_OUT","StorerKey caught Excepception",100L);
		}
		

		try
		{
			Object sku = bio.get("SKU");
			if (sku != null)
			{
				_log.debug("LOG_SYSTEM_OUT","!@# SKU - " + sku.toString(),100L);
			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","SKU is null",100L);
			}
		} catch (EpiDataReadUninitializedException e)
		{
			_log.debug("LOG_SYSTEM_OUT","SKU caught Excepception",100L);
		}
		
		try
		{
			Object toLoc = bio.get("TOLOC");
			if (toLoc != null)
			{
				_log.debug("LOG_SYSTEM_OUT","!@# TOLOC - " + toLoc.toString(),100L);
			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","TOLOC is null",100L);
			}
		} catch (EpiDataReadUninitializedException e)
		{
			_log.debug("LOG_SYSTEM_OUT","TOLOC caught Excepception",100L);
		}
		
		try
		{
			Object toLoc = bio.get("PICKHEADERKEY");
			if (toLoc != null)
			{
				_log.debug("LOG_SYSTEM_OUT","!@# PICKHEADERKEY - " + toLoc.toString(),100L);
			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","PICKHEADERKEY is null",100L);
			}
		} catch (EpiDataReadUninitializedException e)
		{
			_log.debug("LOG_SYSTEM_OUT","PICKHEADERKEY caught Excepception",100L);
		}
		_log.debug("LOG_SYSTEM_OUT","!@# End of Print Values - bioFinalBeforeUpdate-------------------\n\n\n\n",100L);
		// Replace the following line with your code,
		//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		//	 as appropriate
		// Leave the line if the code is not going to handle 
		//  bioBeforeUpdate events

		return super.bioFinalBeforeUpdate(context, bio);
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
	protected int bioBeforeInsert(EpnyServiceContext context, Bio bio)
			throws EpiException
	{
		_log.debug("LOG_SYSTEM_OUT","\n\n\n\n!@# Start of Print Values - bioBeforeInsert-------------------",100L);
		try
		{
			Object storerkey = bio.get("STORERKEY");
			if (storerkey != null)
			{
				_log.debug("LOG_SYSTEM_OUT","!@# StorerKey - " + storerkey.toString(),100L);
			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","StorerKey is null",100L);
			}
		} catch (EpiDataReadUninitializedException e)
		{
			_log.debug("LOG_SYSTEM_OUT","StorerKey caught Excepception",100L);
		}
		

		try
		{
			Object sku = bio.get("SKU");
			if (sku != null)
			{
				_log.debug("LOG_SYSTEM_OUT","!@# SKU - " + sku.toString(),100L);
			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","SKU is null",100L);
			}
		} catch (EpiDataReadUninitializedException e)
		{
			_log.debug("LOG_SYSTEM_OUT","SKU caught Excepception",100L);
		}
		
		try
		{
			Object toLoc = bio.get("TOLOC");
			if (toLoc != null)
			{
				_log.debug("LOG_SYSTEM_OUT","!@# TOLOC - " + toLoc.toString(),100L);
			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","TOLOC is null",100L);
			}
		} catch (EpiDataReadUninitializedException e)
		{
			_log.debug("LOG_SYSTEM_OUT","TOLOC caught Excepception",100L);
		}
		
		try
		{
			Object toLoc = bio.get("PICKHEADERKEY");
			if (toLoc != null)
			{
				_log.debug("LOG_SYSTEM_OUT","!@# PICKHEADERKEY - " + toLoc.toString(),100L);
			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","PICKHEADERKEY is null",100L);
			}
		} catch (EpiDataReadUninitializedException e)
		{
			_log.debug("LOG_SYSTEM_OUT","PICKHEADERKEY caught Excepception",100L);
		}
		_log.debug("LOG_SYSTEM_OUT","!@# End of Print Values - bioBeforeInsert-------------------\n\n\n\n",100L);
		// Replace the following line with your code,
		//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		//	 as appropriate
		// Leave the line if the code is not going to handle 
		//  bioBeforeInsert events

		return super.bioBeforeInsert(context, bio);
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
	protected int bioFinalBeforeInsert(EpnyServiceContext context, Bio bio)
			throws EpiException
	{
		_log.debug("LOG_SYSTEM_OUT","\n\n\n\n!@# Start of Print Values - bioFinalBeforeInsert-------------------",100L);
		
		try
		{
			Object storerkey = bio.get("STORERKEY");
			if (storerkey != null)
			{
				_log.debug("LOG_SYSTEM_OUT","!@# StorerKey - " + storerkey.toString(),100L);
			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","StorerKey is null",100L);
			}
		} catch (EpiDataReadUninitializedException e)
		{
			_log.debug("LOG_SYSTEM_OUT","StorerKey caught Excepception",100L);
		}
		

		try
		{
			Object sku = bio.get("SKU");
			if (sku != null)
			{
				_log.debug("LOG_SYSTEM_OUT","!@# SKU - " + sku.toString(),100L);
			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","SKU is null",100L);
			}
		} catch (EpiDataReadUninitializedException e)
		{
			_log.debug("LOG_SYSTEM_OUT","SKU caught Excepception",100L);
		}
		
		try
		{
			Object toLoc = bio.get("TOLOC");
			if (toLoc != null)
			{
				_log.debug("LOG_SYSTEM_OUT","!@# TOLOC - " + toLoc.toString(),100L);
			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","TOLOC is null",100L);
			}
		} catch (EpiDataReadUninitializedException e)
		{
			_log.debug("LOG_SYSTEM_OUT","TOLOC caught Excepception",100L);
		}
		
		try
		{
			Object toLoc = bio.get("PICKHEADERKEY");
			if (toLoc != null)
			{
				_log.debug("LOG_SYSTEM_OUT","!@# PICKHEADERKEY - " + toLoc.toString(),100L);
			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","PICKHEADERKEY is null",100L);
			}
		} catch (EpiDataReadUninitializedException e)
		{
			_log.debug("LOG_SYSTEM_OUT","PICKHEADERKEY caught Excepception",100L);
		}
		_log.debug("LOG_SYSTEM_OUT","!@# End of Print Values - bioFinalBeforeInsert-------------------\n\n\n\n",100L);
		// Replace the following line with your code,
		//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		//	 as appropriate
		// Leave the line if the code is not going to handle 
		//  bioBeforeInsert events

		return super.bioFinalBeforeInsert(context, bio);
	}

}
