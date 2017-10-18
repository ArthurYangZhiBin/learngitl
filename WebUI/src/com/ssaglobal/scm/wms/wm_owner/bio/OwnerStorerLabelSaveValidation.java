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
import com.ssaglobal.scm.wms.wm_owner.ListDeleteOwner;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class OwnerStorerLabelSaveValidation extends com.epiphany.shr.data.bio.extensions.BioExtensionBase
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
	protected static ILoggerCategory _log = LoggerFactory.getInstance(OwnerStorerLabelSaveValidation.class);

	protected int bioBeforeUpdate(EpnyServiceContext context, Bio bio) throws EpiException
	{

		// Replace the following line with your code,
		//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		//	 as appropriate
		// Leave the line if the code is not going to handle 
		//  bioBeforeUpdate events

		return storerlabelSaveValidation(context, bio);
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

		return storerlabelSaveValidation(context, bio);
	}

	private int storerlabelSaveValidation(EpnyServiceContext context, Bio bio) throws EpiDataException, FormException
	{
		Object labelName = null;
		Object labelType = null;
		Object customer = null;
		Object storerKey = null;

		try
		{
			labelType = bio.get("LABELTYPE");
		} catch (EpiDataException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			_log.debug("LOG_SYSTEM_OUT","!@# Label Type is null",100L);
			throw new FormException("OWNER_LABEL_TYPE_NAME_REQD", new Object[] {});
		}

		try
		{
			labelName = bio.get("LABELNAME");
		} catch (EpiDataException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			_log.debug("LOG_SYSTEM_OUT","!@# Label Name is null",100L);
			throw new FormException("OWNER_LABEL_TYPE_NAME_REQD", new Object[] {});
		}

		try
		{
			customer = bio.get("CONSIGNEEKEY");
		} catch (EpiDataException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			_log.debug("LOG_SYSTEM_OUT","!@# customer is null, setting to default",100L);
			bio.set("CONSIGNEEKEY", "DEFAULT");
		}

		try
		{
			storerKey = bio.get("STORERKEY");
		} catch (EpiDataException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			_log.debug("LOG_SYSTEM_OUT","!@# STORERKEY is null",100L);
			return RET_CANCEL;
		}

		checkRequiredFields(labelName, labelType);

		checkCustomer(bio, customer);

		checkDuplicateLabels(labelName, labelType, customer, storerKey);

		return RET_CONTINUE;
	}

	private void checkCustomer(Bio bio, Object customer) throws EpiDataException
	{
		if (isNull(customer, "CUSTOMER"))
		{
			_log.debug("LOG_SYSTEM_OUT","!@# customer is null, setting to default",100L);
			bio.set("CONSIGNEEKEY", "DEFAULT");
		}
	}

	private void checkDuplicateLabels(Object labelName, Object labelType, Object customer, Object storerKey) throws EpiDataException, DPException, FormException
	{
		String customerKeyValue;
		String labelTypeValue;
		String labelNameValue;
		String storerKeyValue;
		boolean duplicateTypeAndCustomer;
		boolean duplicateTypeAndName;

		if (isNull(customer, "CONSIGNEEKEY"))
		{
			_log.debug("LOG_SYSTEM_OUT","!@# Setting customer to DEFAULT",100L);
			customerKeyValue = "DEFAULT";
		}
		else
		{
			customerKeyValue = customer.toString();
		}
		labelNameValue = labelName.toString();
		labelTypeValue = labelType.toString();
		storerKeyValue = storerKey.toString();

		labelNameValue = labelNameValue == null ? null : labelNameValue.toUpperCase();
		labelTypeValue = labelTypeValue == null ? null : labelTypeValue.toUpperCase();
		storerKeyValue = storerKeyValue == null ? null : storerKeyValue.toUpperCase();

		//Query Table

		//TYPE & NAME
		String queryTypeName = "SELECT * " + "FROM STORERLABELS " + "WHERE (LABELTYPE = '" + labelTypeValue + "') "
				+ "AND (LABELNAME = '" + labelNameValue + "') " + "AND (STORERKEY = '" + storerKeyValue + "')";
		_log.debug("LOG_SYSTEM_OUT","///QUERY \n" + queryTypeName,100L);
		EXEDataObject resultsTypeName = WmsWebuiValidationSelectImpl.select(queryTypeName);

		//If results >0. duplicate entry
		if (resultsTypeName.getRowCount() >= 1)
		{
			duplicateTypeAndName = true;
			_log.debug("LOG_SYSTEM_OUT","//// Duplicate Validation Failed",100L);
		}
		else
		{
			duplicateTypeAndName = false;
			_log.debug("LOG_SYSTEM_OUT","//// Duplicate Validation Passed - ",100L);
		}

		//TYPE & CUSTOMER
		String queryTypeCustomer = "SELECT * " + "FROM STORERLABELS " + "WHERE (LABELTYPE = '" + labelTypeValue + "') "
				+ "AND (CONSIGNEEKEY = '" + customerKeyValue + "')" + "AND (STORERKEY = '" + storerKeyValue + "')";
		_log.debug("LOG_SYSTEM_OUT","///QUERY \n" + queryTypeCustomer,100L);
		EXEDataObject resultsTypeCustomer = WmsWebuiValidationSelectImpl.select(queryTypeCustomer);

		//If results >0. duplicate entry
		if (resultsTypeCustomer.getRowCount() >= 1)
		{
			duplicateTypeAndCustomer = true;
			_log.debug("LOG_SYSTEM_OUT","//// Duplicate Validation Failed",100L);
		}
		else
		{
			duplicateTypeAndCustomer = false;
			_log.debug("LOG_SYSTEM_OUT","//// Duplicate Validation Passed - ",100L);
		}

		if (duplicateTypeAndCustomer || duplicateTypeAndName)
		{
			throw new FormException("OWNER_LABEL_DUPLICATE", new Object[] {});

		}
		else
		{
			//Do Nothing
		}
	}

	private void checkRequiredFields(Object labelName, Object labelType) throws EpiDataException, FormException
	{
		if (isNull(labelType, "Label Type") || isNull(labelName, "Label Name"))
		{
			_log.debug("LOG_SYSTEM_OUT","!@# Label Type and/or Label Name are/is Null",100L);
			throw new FormException("OWNER_LABEL_TYPE_NAME_REQD", new Object[] {});
		}
	}

	private boolean isNull(Object attributeValue, String attribute) throws EpiDataException
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
