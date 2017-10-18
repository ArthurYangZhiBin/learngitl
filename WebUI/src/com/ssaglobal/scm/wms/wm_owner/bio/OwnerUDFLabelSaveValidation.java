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

package com.ssaglobal.scm.wms.wm_owner.bio;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.HashMap;

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class OwnerUDFLabelSaveValidation extends com.epiphany.shr.data.bio.extensions.BioExtensionBase
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
	protected static ILoggerCategory _log = LoggerFactory.getInstance(OwnerUDFLabelSaveValidation.class);

	protected int bioBeforeUpdate(EpnyServiceContext context, Bio bio)
			throws EpiException
	{

		// Replace the following line with your code,
		//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		//	 as appropriate
		// Leave the line if the code is not going to handle 
		//  bioBeforeUpdate events

		return udfLabelSaveValidation(context, bio);
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

		return udfLabelSaveValidation(context, bio);
	}

	private int udfLabelSaveValidation(EpnyServiceContext context, Bio bio)
			throws EpiDataException, FormException
	{
		_log.debug("LOG_SYSTEM_OUT","\n\n!@# Start of OwnerUDFLabelSaveValidation",100L);

		final String name = getParameterString("NAME");
		final String errorMessage = (getParameter("ERROR_MESSAGE") != null) ? getParameterString("ERROR_MESSAGE") : "Duplicate "
				+ name;

		//Retrieve Values
		//		UDF Label – STORER_UDF.UDFLABEL
		//		UDF Value – STORER_UDF.UDFVALUE
		//		UDF Notes – STORER_UDF.UDFNOTES

		Object tempUDFLabel = null;
		Object tempUDFNotes = null;
		Object tempUDFValue = null;
		Object tempStorerKeyValue = null;

		try
		{
			tempUDFLabel = bio.get("UDFLABEL");
		} catch (EpiDataException e)
		{
			e.printStackTrace();
			_log.debug("LOG_SYSTEM_OUT","### UDFLABEL is null",100L);
		}

		try
		{
			tempUDFValue = bio.get("UDFVALUE");
		} catch (EpiDataException e)
		{
			e.printStackTrace();
			_log.debug("LOG_SYSTEM_OUT","### UDFVALUE is null",100L);
		}

		try
		{
			tempUDFNotes = bio.get("UDFNOTES");
		} catch (EpiDataException e)
		{
			e.printStackTrace();
			_log.debug("LOG_SYSTEM_OUT","### UDFNOTES is null",100L);
		}

		try
		{
			tempStorerKeyValue = bio.get("STORERKEY");
		} catch (EpiDataException e)
		{
			e.printStackTrace();
			_log.debug("LOG_SYSTEM_OUT","### STORERKEY is null",100L);
		}

		checkRequiredFields(tempUDFLabel);

		//Determine SQL Statements
		String query = null;
		//if UDFVALUE and UDFNOTES is null
		//if UDFVALUE is null
		//if UDFNOTES is null

		//First check UDFLABEL
		return checkUDFLabelDuplicate(name, tempUDFLabel, tempStorerKeyValue);

		//		if (isNull(tempUDFValue, "UDFVALUE") && isNull(tempUDFNotes, "UDFNOTES"))
		//		{
		//			query = "SELECT * FROM STORER_UDF WHERE (UDFLABEL = '" + tempUDFLabel.toString() + "') AND (STORERKEY = '"
		//					+ tempStorerKeyValue.toString() + "')";
		//		}
		//		else if (isNull(tempUDFValue, "UDFVALUE"))
		//		{
		//			query = "SELECT * FROM STORER_UDF WHERE (UDFLABEL = '" + tempUDFLabel.toString() + "') AND (UDFNOTES = '"
		//					+ tempUDFNotes.toString() + "')" + " AND (STORERKEY = '" + tempStorerKeyValue.toString() + "')";
		//		}
		//		else if (isNull(tempUDFNotes, "UDFNOTES"))
		//		{
		//			query = "SELECT * FROM STORER_UDF WHERE (UDFVALUE = '" + tempUDFValue.toString() + "') AND (UDFLABEL = '"
		//					+ tempUDFLabel.toString() + "')  AND (STORERKEY = '" + tempStorerKeyValue.toString() + "')";
		//		}
		//		else
		//		{
		//			query = "SELECT * FROM STORER_UDF WHERE (UDFVALUE = '" + tempUDFValue.toString() + "') AND (UDFLABEL = '"
		//					+ tempUDFLabel.toString() + "') AND (UDFNOTES = '" + tempUDFNotes.toString() + "')" + " AND (STORERKEY = '"
		//					+ tempStorerKeyValue.toString() + "')";
		//		}
		//
		//		_log.debug("LOG_SYSTEM_OUT","^______________^",100L);
		//		//Query Table
		//		_log.debug("LOG_SYSTEM_OUT","///QUERY " + query,100L);
		//		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		//
		//		//If results >0. duplicate entry
		//		if (results.getRowCount() >= 1)
		//		{
		//			_log.debug("LOG_SYSTEM_OUT","//// " + name + " Duplicate Validation Failed",100L);
		//			throw new FormException("OWNER_LABEL_DUPLICATE", new Object[] {});
		//		}
		//		else
		//		{
		//			_log.debug("LOG_SYSTEM_OUT","//// " + name + " Duplicate Validation Passed - ",100L);
		//			//setErrorMessage(formWidget, "");
		//			return RET_CONTINUE;
		//
		//		}
	}

	private int checkUDFLabelDuplicate(final String name, Object tempUDFLabel, Object tempStorerKeyValue)
			throws DPException, FormException
	{
		String query;
		query = "SELECT * FROM STORER_UDF WHERE (UDFLABEL = '" + tempUDFLabel.toString() + "') AND (STORERKEY = '"
				+ tempStorerKeyValue.toString() + "')";
		_log.debug("LOG_SYSTEM_OUT","^______________^",100L);
		//Query Table
		_log.debug("LOG_SYSTEM_OUT","///QUERY " + query,100L);
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		//If results >0. duplicate entry
		if (results.getRowCount() >= 1)
		{
			_log.debug("LOG_SYSTEM_OUT","//// " + name + " Duplicate Validation Failed",100L);
			throw new FormException("OWNER_LABEL_DUPLICATE", new Object[] {});
		}
		else
		{
			_log.debug("LOG_SYSTEM_OUT","//// " + name + " Duplicate Validation Passed - ",100L);
			//setErrorMessage(formWidget, "");
			return RET_CONTINUE;
		}

	}

	private void checkRequiredFields(Object tempUDFLabel)
			throws EpiDataException, FormException
	{
		if (isNull(tempUDFLabel, "UDF Label"))
		{
			_log.debug("LOG_SYSTEM_OUT","!@# UDF Label is Null",100L);
			throw new FormException("OWNER_UDF_LABEL_REQD", new Object[] {});
		}

	}

	private boolean isNull(Object attributeValue, String attribute)
			throws EpiDataException
	{

		if (attributeValue == null)
		{
			_log.debug("LOG_SYSTEM_OUT","!@# " + attribute + " is null ",100L);
			return true;
		}
		else if (attributeValue.toString().matches("\\s*"))
		{
			_log.debug("LOG_SYSTEM_OUT","!@# " + attribute + " is blank ",100L);
			return true;
		}
		else
		{
			_log.debug("LOG_SYSTEM_OUT","!@# " + attribute + " is " + attributeValue.toString(),100L);
			return false;
		}

	}

}
