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

package com.ssaglobal.scm.wms.wm_cyclecount.bio;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.EpiDataReadUninitializedException;
import com.epiphany.shr.data.bio.Query;

/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class StorerTableValidation extends com.epiphany.shr.data.bio.extensions.BioExtensionBase
{
	private static String BIO = "wm_storer";

	private static String ATTRIBUTE = "STORERKEY";

	private static String ERROR_MESSAGE = "STORER_VALIDATION";

	protected static ILoggerCategory _log = LoggerFactory.getInstance(StorerTableValidation.class);

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
	protected int bioBeforeUpdate(EpnyServiceContext context, Bio bio) throws EpiException
	{

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		// Leave the line if the code is not going to handle
		// bioBeforeUpdate events

		return validateStorer(context, bio);
	}

	protected int bioFinalBeforeUpdate(EpnyServiceContext context, Bio bio) throws EpiException
	{

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		// Leave the line if the code is not going to handle
		// bioBeforeUpdate events

		return validateStorer(context, bio);
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
	protected int bioBeforeInsert(EpnyServiceContext context, Bio bio) throws EpiException
	{

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		// Leave the line if the code is not going to handle
		// bioBeforeInsert events

		return validateStorer(context, bio);
	}

	protected int bioFinalBeforeInsert(EpnyServiceContext context, Bio bio) throws EpiException
	{

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		// Leave the line if the code is not going to handle
		// bioBeforeInsert events

		return validateStorer(context, bio);
	}

	private int validateStorer(EpnyServiceContext context, Bio bio) throws EpiException
	{
		_log.info("EXP_1", "Start of StorerTableValidation", SuggestedCategory.NONE);
		ArrayList attributeNames = null;
		int typeValue;
		HashMap attributes = new HashMap();
		try
		{
			// Get Attributes
			attributeNames = (ArrayList) getParameter("ATTRTOVALIDATE");
			typeValue = getParameterInt("TYPE");
			for (Iterator it = attributeNames.iterator(); it.hasNext();)
			{
				String attributeName = it.next().toString();
				_log.debug("LOG_SYSTEM_OUT","///Attribute " + attributeName,100L);

				try
				{
					//Handle Empty Values
					Object tempValue = bio.get(attributeName);
					if (tempValue == null)
					{
						_log.debug("LOG_SYSTEM_OUT","\n!@# Value uninitialized for " + attributeName,100L);
						throw new EpiDataReadUninitializedException();
					}
					String attributeValue = (String) tempValue;
					if (attributeValue.equalsIgnoreCase(" ") || attributeValue.equalsIgnoreCase(""))
					{
						_log.debug("LOG_SYSTEM_OUT","!@# Space found",100L);
						throw new EpiDataReadUninitializedException();
					}
					_log.debug("LOG_SYSTEM_OUT","///Validating " + attributeName + ": " + attributeValue,100L);
					// Add attributeName,attributeValue pair to map
					attributes.put(attributeName, attributeValue);
				} catch (EpiDataReadUninitializedException e)
				{
					// e.printStackTrace();
					// Value uninitialized, do nothing
					_log.debug("LOG_SYSTEM_OUT","/////Exception " + e.toString() + " Caught ",100L);
					_log.debug("LOG_SYSTEM_OUT","/////Value Uninitialized, Doing Nothing",100L);

				}

			}
			
			if (attributes.size() == 0)
			{
				_log.debug("LOG_SYSTEM_OUT","!@# Nothing to validate, doing nothing",100L);
				return RET_CONTINUE;
			}

		} catch (Exception e)
		{

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		// Query Bio to see if Storer exists
		String queryStatement = null;
		Query query = null;
		BioCollection results = null;
		for (Iterator it = attributes.entrySet().iterator(); it.hasNext();)
		{
			// retrieve attributeName, attributeValue pair
			Map.Entry entry = (Map.Entry) it.next();
			String attributeName = (String) entry.getKey();
			String attributeValue = (String) entry.getValue();
			// perform query
			try
			{

				queryStatement = BIO + "." + ATTRIBUTE + " = '" + attributeValue + "'" + " AND " + BIO + ".TYPE = " + typeValue;
				_log.debug("LOG_SYSTEM_OUT","////query statement " + queryStatement,100L);
				query = new Query(BIO, queryStatement, null);
				results = bio.getUnitOfWork().findByQuery(query);
			} catch (Exception e)
			{
				e.printStackTrace();
				throw new EpiException("EXP_VALIDATE_STORER_ERROR", "Error preparing search query");
			}

			// If BioCollection size equals 0, return RET_CANCEL
			if (results.size() == 0)
			{
				_log.debug("LOG_SYSTEM_OUT","////StorerKey Invalid",100L);
				String[] parameters = new String[1];
				parameters[0] = attributeValue;
				if( getParameter("ERROR_MESSAGE") != null)
				{
					ERROR_MESSAGE = getParameterString("ERROR_MESSAGE"); 
				}
				throw new FormException(ERROR_MESSAGE , parameters);

			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","////StorerKey Valid " + attributeName + ": " + attributeValue,100L);
			}

		}

		// Result Found
		return RET_CONTINUE;
	}
}