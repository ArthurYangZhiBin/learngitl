/**
 * ---Begin Copyright Notice---20090105T1353
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2010 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */
package com.ssaglobal.scm.wms.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioAttributeMetadata;
import com.epiphany.shr.data.bio.BioMetadata;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

public class BioUtil
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(BioUtil.class);

	/***
	 * 
	 * @param newCWCD
	 * @param bioMetadata
	 * @return
	 */
	public static String getBioAsString(DataBean newCWCD, BioMetadata bioMetadata)
	{
		StringBuilder bioAsString = new StringBuilder();
		bioAsString.append(newCWCD.getDataType() + " : ");

		Set<String> attributes = bioMetadata.getAttributes();
		for (String attribute : attributes)
		{
			if (newCWCD.getValue(attribute) != null)
			{
				bioAsString.append("\n\t" + attribute + " = " + newCWCD.getValue(attribute));
			}
		}

		return bioAsString.toString();
	}

	/***
	 * 
	 * @param cwcdToBeDuplicated
	 * @param uow
	 * @param skipList
	 *            ArrayList of Attribute Names - skips copying this Attribute into the new bio
	 * @return
	 * @throws DataBeanException
	 */
	public static QBEBioBean copyBIO(BioBean cwcdToBeDuplicated, UnitOfWorkBean uow, ArrayList<String> skipList) throws DataBeanException
	{
		//create new HelperBio
		QBEBioBean newBio = uow.getQBEBioWithDefaults(cwcdToBeDuplicated.getDataType());
		BioAttributeMetadata[] bioAttributeMetadata = cwcdToBeDuplicated.getBioAttributeMetadata();
		for (BioAttributeMetadata attribute : bioAttributeMetadata)
		{
			if (!skipList.contains(attribute.getName()))
			{
				newBio.setValue(attribute.getName(), cwcdToBeDuplicated.getValue(attribute.getName()));
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_CatchWeightDuplicate_copyBIO", "Original BIO \n" + getBioAsString(cwcdToBeDuplicated, cwcdToBeDuplicated.getBioMetadata()), SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION_CatchWeightDuplicate_copyBIO", "Copied BIO \n" + getBioAsString(newBio, uow.getBioMetadata(newBio.getDataType())), SuggestedCategory.NONE);
		return newBio;
	}
	
	/**
	 * Copy bio restrictive.
	 * 
	 * @param bioToBeDuplicated
	 *            the cwcd to be duplicated
	 * @param uow
	 *            the uow
	 * @param copyList
	 *            the copy list
	 * @return the qBE bio bean
	 * @throws DataBeanException
	 *             the data bean exception
	 */
	public static QBEBioBean copyBIORestrictive(DataBean bioToBeDuplicated, UnitOfWorkBean uow, List<String> copyList) throws DataBeanException
	{
		//create new HelperBio
		QBEBioBean newBio = uow.getQBEBioWithDefaults(bioToBeDuplicated.getDataType());
		Set<String> attributes = MetaDataAccess.getInstance().getBioType(bioToBeDuplicated.getDataType()).getAttributeNames();
		
		for (String attribute : attributes)
		{
			if (copyList.contains(attribute))
			{
				newBio.setValue(attribute, bioToBeDuplicated.getValue(attribute));
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_CatchWeightDuplicate_copyBIORestrictive", "Original BIO \n" + getBioAsString(bioToBeDuplicated, uow.getBioMetadata(bioToBeDuplicated.getDataType())), SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION_CatchWeightDuplicate_copyBIORestrictive", "Copied BIO \n" + getBioAsString(newBio, uow.getBioMetadata(newBio.getDataType())), SuggestedCategory.NONE);
		return newBio;
	}

	/***
	 * 
	 * @param cwcdToBeDuplicated
	 * @param uow
	 * @return
	 * @throws DataBeanException
	 */
	public static QBEBioBean copyBIO(BioBean cwcdToBeDuplicated, UnitOfWorkBean uow) throws DataBeanException
	{
		ArrayList<String> skipList = new ArrayList<String>();
		return copyBIO(cwcdToBeDuplicated, uow, skipList);

	}

	public static String getString(Bio bio, String attributeName)
	{
		Object value = null;
		try
		{
			value = bio.get(attributeName);
		} catch (EpiDataException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (value == null)
		{
			return null;
		}
		else
		{
			if (value instanceof String)
			{
				return (String) value;
			}
			else
			{
				return value.toString();
			}

		}
	}

	public static int getInt(Bio bio, String attributeName)
	{
		Object value = null;
		try
		{
			value = bio.get(attributeName);
		} catch (EpiDataException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (value == null)
		{
			// uninitialized
			return 0;
		}
		else if (value instanceof Integer)
		{
			return ((Integer) value).intValue();
		}
		else
		{
			return Integer.parseInt(value.toString());
		}

	}
	
	public static double getDouble(Bio bio, String attributeName)
	{
		Object value = null;
		
		try {
			value = bio.get(attributeName);
		} catch (EpiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
		if (value == null)
		{
			return 0;
		}
		else if (value instanceof BigDecimal)
		{
			return ((BigDecimal) value).doubleValue();
		}
		else if (value instanceof Double)
		{
			return ((Double) value).doubleValue();
		}
		else
		{
			return Double.parseDouble(value.toString());
		}

	}

}
