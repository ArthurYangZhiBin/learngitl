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
package com.ssaglobal.scm.wms.wm_task_manager_area.bio;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.util.logging.SuggestedCategory;

/**
 * Validates that the BIO Attribute specified by ATTRIBUTENAME consists of only
 * X's Extension is fired Before Insert and Update events
 * 
 * @param ATTRIBUTENAME
 *            <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class XTextFieldValidation extends
		com.epiphany.shr.data.bio.extensions.BioExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory
			.getInstance(XTextFieldValidation.class);

	public XTextFieldValidation()
	{
		_log.info("EXP_1", "XFieldValidation has been instantiated...",
				SuggestedCategory.NONE);

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
	protected int bioBeforeUpdate(EpnyServiceContext context, Bio bio)
			throws EpiException
	{

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		// Leave the line if the code is not going to handle
		// bioBeforeUpdate events

		return validateTextField(context, bio);
	}

	/**
	 * Returning CANCEL from this will stop the insert Creating new bios or
	 * changing bios in the Bio's unit of work will cause them to be added to
	 * the Bios to be processed in this UnitOfWork.save();
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
	protected int bioBeforeInsert(EpnyServiceContext context, Bio bio)
			throws EpiException
	{

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		// Leave the line if the code is not going to handle
		// bioBeforeInsert events

		return validateTextField(context, bio);
	}

	/**
	 * Verifies that the text field specified consists of only X's
	 * 
	 * @param context
	 *            The EpnyServiceContext for this BioExtension instance
	 * @param bio
	 *            bio The Bio for this BioExtension instance
	 * @return RET_CONTINUE;
	 * @throws EpiException
	 */
	private int validateTextField(EpnyServiceContext context, Bio bio)
			throws EpiException
	{
		_log
				.info("EXP_1", "Start of validateTextField",
						SuggestedCategory.NONE);

		String attributeValue = null;
		String attributeName = null;
		// Retrieve value from BIO
		try
		{
			attributeName = getParameter("ATTRIBUTENAME").toString();
			//_log.debug("LOG_SYSTEM_OUT","BIO Attribute " + attributeName,100L);
			attributeValue = bio.get(attributeName).toString();
			//_log.debug("LOG_SYSTEM_OUT","Value = " + attributeValue,100L);
		} catch (Exception e)
		{
			e.printStackTrace();
			throw new EpiException("EXP_VALIDATE_X_ERROR",
					"There was an error retrieving value");
		}

		// clean up text entered by user
		attributeValue = attributeValue.toUpperCase().trim();

		// Verify that attributeValue is only X's
		for (int i = 0; i < attributeValue.length(); i++)
		{
			if (attributeValue.charAt(i) != 'X')
			{
				//_log.debug("LOG_SYSTEM_OUT","Character " + attributeValue.charAt(i),100L);
				throw new FormException(attributeName
						+ " must be represented by X's", new Object[] {});
			} else
			{
				//_log.debug("LOG_SYSTEM_OUT",attributeValue.charAt(i) + " is an X",100L);
			}
		}

		return RET_CONTINUE;
	}
}
