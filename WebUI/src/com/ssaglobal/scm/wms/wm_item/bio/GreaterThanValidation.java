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
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.error.EpiDataReadUninitializedException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class GreaterThanValidation extends com.epiphany.shr.data.bio.extensions.BioExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(GreaterThanValidation.class);
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
	protected int bioBeforeUpdate(EpnyServiceContext context, Bio bio) throws EpiException
	{

		// Replace the following line with your code,
		//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		//	 as appropriate
		// Leave the line if the code is not going to handle 
		//  bioBeforeUpdate events

		return validateGreaterThan(context, bio);
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
	protected int bioFinalBeforeUpdate(EpnyServiceContext context, Bio bio) throws EpiException
	{

		// Replace the following line with your code,
		//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		//	 as appropriate
		// Leave the line if the code is not going to handle 
		//  bioBeforeUpdate events

		return validateGreaterThan(context, bio);
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
	protected int bioBeforeInsert(EpnyServiceContext context, Bio bio) throws EpiException
	{

		// Replace the following line with your code,
		//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		//	 as appropriate
		// Leave the line if the code is not going to handle 
		//  bioBeforeInsert events

		return validateGreaterThan(context, bio);
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
	protected int bioFinalBeforeInsert(EpnyServiceContext context, Bio bio) throws EpiException
	{

		// Replace the following line with your code,
		//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		//	 as appropriate
		// Leave the line if the code is not going to handle 
		//  bioBeforeInsert events

		return validateGreaterThan(context, bio);
	}

	private int validateGreaterThan(EpnyServiceContext context, Bio bio) throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION", "\n\n!@#$%^&* Start of GreaterThanValidation", SuggestedCategory.NONE);
		try
		{
			String greaterAttribute = getParameterString("GREATERATTR");
			String lesserAttribute = getParameterString("LESSERATTR");
			_log.debug("LOG_DEBUG_EXTENSION", "\n!@# Comparing " + greaterAttribute + " & " + lesserAttribute, SuggestedCategory.NONE);
			//retrieve values
			Object tempGreaterValue = bio.get(greaterAttribute);
			Object tempLesserValue = bio.get(lesserAttribute);
			if ((tempGreaterValue == null) || (tempLesserValue == null))
			{
				_log.debug("LOG_DEBUG_EXTENSION", "\n\n!@# Values Undeclared, doing Nothing", SuggestedCategory.NONE);
				return RET_CONTINUE;
			}
			_log.debug("LOG_DEBUG_EXTENSION", "Greater Type " + tempGreaterValue.getClass().getName(), SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION", "Lesser Type " + tempLesserValue.getClass().getName(), SuggestedCategory.NONE);
			int greaterValue = ((Integer) tempGreaterValue).intValue();
			int lesserValue = ((Integer) tempLesserValue).intValue();
			//compare
			_log.debug("LOG_DEBUG_EXTENSION", "\n!@# " + greaterValue + " > " + lesserValue, SuggestedCategory.NONE);
			if (greaterValue > lesserValue)
			{
				_log.debug("LOG_DEBUG_EXTENSION", "Values Verified", SuggestedCategory.NONE);

			}
			else
			{
				String[] parameters = new String[2];
				parameters[0] = greaterAttribute;
				parameters[1] = lesserAttribute;
				_log.debug("LOG_DEBUG_EXTENSION", "!@#" + greaterAttribute + " should be greater than " + lesserAttribute, SuggestedCategory.NONE);
				throw new UserException("Greater_Than_Validation", parameters);
			}
		}catch( EpiDataReadUninitializedException e)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "\n\n!@# Values Undeclared, doing Nothing", SuggestedCategory.NONE);
			return RET_CONTINUE;
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			return RET_CANCEL;
		}
		return RET_CONTINUE;
	}

}
