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
package com.ssaglobal.scm.wms.wm_table_validation.bio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataReadUninitializedException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_storer.ui.storerSaveValidationAction;

public class DuplicateValidation extends com.epiphany.shr.data.bio.extensions.BioExtensionBase
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
	protected static ILoggerCategory _log = LoggerFactory.getInstance(DuplicateValidation.class);

	protected int bioBeforeUpdate(EpnyServiceContext context, Bio bio)
			throws EpiException
	{

		// Replace the following line with your code,
		//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		//	 as appropriate
		// Leave the line if the code is not going to handle 
		//  bioBeforeUpdate events

		return checkForDuplicates(context, bio);
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

		// Replace the following line with your code,
		//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		//	 as appropriate
		// Leave the line if the code is not going to handle 
		//  bioBeforeUpdate events

		return checkForDuplicates(context, bio);
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

		// Replace the following line with your code,
		//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		//	 as appropriate
		// Leave the line if the code is not going to handle 
		//  bioBeforeInsert events

		return checkForDuplicates(context, bio);
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

		// Replace the following line with your code,
		//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		//	 as appropriate
		// Leave the line if the code is not going to handle 
		//  bioBeforeInsert events

		return checkForDuplicates(context, bio);
	}

	private int checkForDuplicates(EpnyServiceContext context, Bio bio)
			throws EpiException
	{
		//Parameters
		final String BIO = getParameterString("BIO");
		final String ERROR_MESSAGE = getParameterString("ERROR_MESSAGE");
		final String NAME = getParameterString("NAME");
		ArrayList primaryKeyFieldNames = (ArrayList) getParameter("PRIMARY_KEY_FIELDS");

		//Print Params
		_log.debug("LOG_SYSTEM_OUT","!@# BIO " + BIO,100L);
		_log.debug("LOG_SYSTEM_OUT","!@# ERROR_MESSAGE " + ERROR_MESSAGE,100L);
		_log.debug("LOG_SYSTEM_OUT","!@# NAME " + NAME,100L);
		_log.debug("LOG_SYSTEM_OUT","!@# PRIMARY_KEY_FIELDS " + primaryKeyFieldNames.size(),100L);
		
		
		_log.debug("LOG_SYSTEM_OUT","\n\n~!@ START OF " + NAME + " DUPLICATE VALIDATION",100L);
		HashMap attributesToValidate = new HashMap();
		try
		{
			// Get keys and values

			for (Iterator it = primaryKeyFieldNames.iterator(); it.hasNext();)
			{
				String keyName = it.next().toString();
				System.out.print("///Key: " + keyName);
				String keyValue = (String) bio.get(keyName);
				System.out.print(" Value: " + keyValue + "\n");
				attributesToValidate.put(keyName, keyValue);

			}

		} catch (Exception e)
		{

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		// Query Bio to see if K,V exists
		String queryStatement = null;
		Query query = null;
		BioCollection results = null;

		// Prepare Query Statement
		try
		{
			queryStatement = prepareQueryStatement(BIO, attributesToValidate);
			_log.debug("LOG_SYSTEM_OUT","!@#! Query: " + queryStatement,100L);
			query = new Query(BIO, queryStatement, null);
		} catch (Exception e)
		{

			e.printStackTrace();
			throw new EpiException("QUERY_ERROR", "Error preparing search query" + queryStatement);
		}

		// Query Bio
		results = bio.getUnitOfWork().findByQuery(query);

		if (results.size() >= 1)
		{
			_log.debug("LOG_SYSTEM_OUT","//// " + NAME + " Duplication Check Failed\n\n",100L);
			String[] parameters = new String[1];
			parameters[0] = NAME;
			throw new FormException(ERROR_MESSAGE, parameters);

		}
		else
		{
			_log.debug("LOG_SYSTEM_OUT","//// " + NAME + " Duplication Check Passed\n\n",100L);
		}

		return RET_CONTINUE;
	}

	private String prepareQueryStatement(String bio, HashMap attributesToValidate)
	{
		StringBuffer query = new StringBuffer();

		//process attributesToValidate 
		//format: BIO.KEY1 = 'VALUE' and BIO.KEY2 = 'VALUE' ...
		int count = 0;
		for (Iterator it = attributesToValidate.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry keyValuePair = (Map.Entry) it.next();
			String key = (String) keyValuePair.getKey();
			String value = (String) keyValuePair.getValue();

			if (count >= 1)
			{
				query.append(" AND ");
			}

			query.append(bio + "." + key + " = '" + value + "'");

			count++;

		}
		return query.toString();
	}

}
