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
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.EpiDataReadUninitializedException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.LocaleUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ItemCatchWeightPreSaveValidation extends com.epiphany.shr.data.bio.extensions.BioExtensionBase
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
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemCatchWeightPreSaveValidation.class);

	protected int bioBeforeUpdate(EpnyServiceContext context, Bio bio)
			throws EpiException
	{

		// Replace the following line with your code,
		//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		//	 as appropriate
		// Leave the line if the code is not going to handle 
		//  bioBeforeUpdate events

		return preSaveValidation(context, bio);
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

		return preSaveValidation(context, bio);
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

		return preSaveValidation(context, bio);
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

		return preSaveValidation(context, bio);
	}

	private int preSaveValidation(EpnyServiceContext context, Bio bio)
			throws EpiException
	{
		_log.debug("LOG_SYSTEM_OUT","\n\n !#$% Start of ItemCatchWeightPreSaveValidation",100L);
//		try
//		{
//			Object cwFlagValue = bio.get("CWFLAG");
//			if (cwFlagValue != null)
//			{
//				_log.debug("LOG_SYSTEM_OUT","@#$ CWFLAG Performing Non Null Validation?",100L);
//				nonNullValidation(bio);
//			}
//			else
//			{
//				_log.debug("LOG_SYSTEM_OUT","!@# CWFLAG null",100L);
//			}
//		} catch (EpiDataReadUninitializedException e)
//		{
//			_log.debug("LOG_SYSTEM_OUT","!@# EpiDataReadUninitializedException caught, doing nothing for CWFLAG",100L);
//		}

		//ICWFLAG
		try
		{
			Object icwFlag = bio.get("ICWFLAG");
			if (icwFlag != null && (!icwFlag.toString().equals("0")))
			{
				_log.debug("LOG_SYSTEM_OUT","@#$ ICWFLAG Checking for NonNegative " + icwFlag,100L);
				nonNegativeValidation("AVGCASEWEIGHT", "Average Weight", bio);
				nonNegativeValidation("TOLERANCEPCT", "Tolerance", bio);
			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","!@# ICWFLAG null",100L);
			}
		} catch (EpiDataReadUninitializedException e)
		{
			_log.debug("LOG_SYSTEM_OUT","!@# EpiDataReadUninitializedException caught, doing nothing for ICWFLAG",100L);
		}

		//OCWFLAG
		try
		{
			Object ocwFlag = bio.get("OCWFLAG");
			if (ocwFlag != null && (!ocwFlag.toString().equals("0")))
			{
				_log.debug("LOG_SYSTEM_OUT","@#$ OCWFLAG Checking for NonNegative " + ocwFlag,100L);
				nonNegativeValidation("OAVGCASEWEIGHT", "Average Weight", bio);
				nonNegativeValidation("OTOLERANCEPCT", "Tolearance", bio);
			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","!@# OCWFLAG null",100L);
			}
		} catch (EpiDataReadUninitializedException e)
		{
			_log.debug("LOG_SYSTEM_OUT","!@# EpiDataReadUninitializedException caught, doing nothing for OCWFLAG",100L);
		}

		//ICDFLAG
		try
		{
			Object icdFlag = bio.get("ICDFLAG");
			if (icdFlag != null && (!icdFlag.toString().equals("0")))
			{

				_log.debug("LOG_SYSTEM_OUT","@#$ OCWFLAG Checking for NonNull " + icdFlag,100L);
				String[] parameters = { "Serial #", "Other 2", "Other 3" };
				if (checkForNull("ICDLABEL1", bio) || checkForNull("ICDLABEL2", bio) || checkForNull("ICDLABEL3", bio))
				{
					_log.debug("LOG_SYSTEM_OUT","One of the ICDFLAG dependent attributes is not null",100L);
				}
				else
				{
					_log.debug("LOG_SYSTEM_OUT","All of the ICDFLAG dependent attributes are null",100L);
					throw new FormException("ITEM_NULL_CATCH_DATA_EXCEPTION", parameters);
				}

			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","!@# ICDFLAG null",100L);
			}
		} catch (EpiDataReadUninitializedException e)
		{
			_log.debug("LOG_SYSTEM_OUT","!@# EpiDataReadUninitializedException caught, doing nothing for ICDFLAG",100L);
		}

		//OCDFLAG
		try
		{
			Object ocdFlag = bio.get("OCDFLAG");
			if (ocdFlag != null && (!ocdFlag.toString().equals("0")))
			{
				_log.debug("LOG_SYSTEM_OUT","@#$ OCWFLAG Checking for NonNull " + ocdFlag,100L);
				String[] parameters = { "Serial #", "Other 2", "Other 3" };
				if (checkForNull("OCDLABEL1", bio) || checkForNull("OCDLABEL2", bio) || checkForNull("OCDLABEL3", bio))
				{
					_log.debug("LOG_SYSTEM_OUT","One of the ICDFLAG dependent attributes is not null",100L);
				}
				else
				{
					_log.debug("LOG_SYSTEM_OUT","All of the ICDFLAG dependent attributes are null",100L);
					throw new FormException("ITEM_NULL_CATCH_DATA_EXCEPTION", parameters);
				}

			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","!@# OCDFLAG null",100L);
			}
		} catch (EpiDataReadUninitializedException e)
		{
			_log.debug("LOG_SYSTEM_OUT","!@# EpiDataReadUninitializedException caught, doing nothing for OCDFLAG",100L);
		}

		return RET_CONTINUE;
	}

	private boolean checkForNull(String attribute, Bio bio)
			throws EpiDataException
	{

		Object attributeValue = bio.get(attribute);
		if (attributeValue == null)
		{
			_log.debug("LOG_SYSTEM_OUT","!@# " + attribute + " is null ",100L);
			return false;
		}
		else if (attributeValue.toString().matches("\\s*"))
		{
			_log.debug("LOG_SYSTEM_OUT","!@# " + attribute + " is blank ",100L);
			return false;
		}
		else
		{
			_log.debug("LOG_SYSTEM_OUT","!@# " + attribute + " is " + attributeValue.toString(),100L);
			return true;
		}

	}

	private void nonNullValidation(Bio bio)
			throws EpiDataException, FormException
	{
		ArrayList catchWeightWidgets = new ArrayList();
		catchWeightWidgets.add("ICWFLAG");
		catchWeightWidgets.add("ICDFLAG");
		catchWeightWidgets.add("OCWFLAG");
		catchWeightWidgets.add("OCDFLAG");

		int numberOfNulls = 0;

		for (Iterator it = catchWeightWidgets.iterator(); it.hasNext();)
		{
			String attribute = it.next().toString();
			Object attributeValue = bio.get(attribute);
			if (attributeValue == null)
			{
				_log.debug("LOG_SYSTEM_OUT","!@# " + attribute + " is null ",100L);
				numberOfNulls++;
			}
			else if (attributeValue.toString().equals("0"))
			{
				_log.debug("LOG_SYSTEM_OUT","!@# " + attribute + " is unchecked ",100L);
				numberOfNulls++;
			}
			else if (attributeValue.toString().matches("\\s*"))
			{
				_log.debug("LOG_SYSTEM_OUT","!@# " + attribute + " is blank ",100L);
				numberOfNulls++;
			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","!@# " + attribute + " is " + attributeValue.toString(),100L);
			}

		}

		_log.debug("LOG_SYSTEM_OUT","!@ Size " + catchWeightWidgets.size() + " # " + numberOfNulls,100L);
		if (catchWeightWidgets.size() == numberOfNulls)
		{
			_log.debug("LOG_SYSTEM_OUT","!@# All Catch Weight Widgets are unchecked, informing user ",100L);
			throw new FormException("ITEM_CW_MESSAGE", new Object[] {});
		}

	}

	private void nonNegativeValidation(String attributeName, String widgetName, Bio bio)
			throws EpiException
	{

		_log.debug("LOG_SYSTEM_OUT","\n\n!@# Start of nonNegativeValidation for " + attributeName,100L);

		double widgetValue = 0;
		Object temp = bio.get(attributeName);

		String tempValue = null;
		if (temp == null)
		{
			_log.debug("LOG_SYSTEM_OUT","$% Null, doing nothing ",100L);
			return;
		}
		else if (temp.toString().matches("\\s*"))
		{
			String[] parameters = new String[2];
			parameters[0] = widgetName;
			parameters[1] = " ";
			_log.debug("LOG_SYSTEM_OUT","//// NonNegativeValidationCCF Validation Failed",100L);
			throw new FormException("NUMERIC_NEGATIVE_VALIDATION", parameters);
		}
		else
		{
			tempValue = temp.toString();
		}

		try
		{
			NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW
			widgetValue = nf.parse(tempValue.toString()).doubleValue();

		} catch (ParseException e)
		{
			_log.debug("LOG_SYSTEM_OUT","!@# Exception Caught, trying to parse as Currency",100L);
						NumberFormat nfc = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_CURR, 0, 0); //AW
			try
			{
				widgetValue = nfc.parse(tempValue.toString()).doubleValue();
			} catch (ParseException e1)
			{
				e1.printStackTrace();
				String[] parameters = new String[1];
				parameters[0] = widgetName;

				throw new FormException("NUMERIC_PARSE_EXCEPTION", parameters);

			}
		} catch (NumberFormatException e)
		{
			String[] parameters = new String[1];
			parameters[0] = widgetName;
			throw new FormException("NUMERIC_PARSE_EXCEPTION", parameters);
		}

		_log.debug("LOG_SYSTEM_OUT","!@# Validating Widget: " + attributeName + " Value: " + widgetValue,100L);

		// If value < 0, return RET_CANCEL
		if (widgetValue < 0)
		{
			String[] parameters = new String[2];
			parameters[0] = widgetName;
			parameters[1] = Double.toString(widgetValue);
			_log.debug("LOG_SYSTEM_OUT","//// NonNegativeValidationCCF Validation Failed",100L);
			throw new FormException("NUMERIC_NEGATIVE_VALIDATION", parameters);

		}
		else
		{
			_log.debug("LOG_SYSTEM_OUT","//// NonNegativeValidationCCF Validation Passed - " + attributeName + " : " + tempValue,100L);

		}

	}
}
