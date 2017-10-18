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
package com.ssaglobal.scm.wms.util;

import java.math.BigDecimal;
import java.util.Calendar;

import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;

public class BioAttributeUtil
{
	public static String convertToString(DataBean focus, String attributeName)
	{
		final Object value;
		value = focus.getValue(attributeName);
		if (value == null)
		{
			return "";
		}
		else
		{
			return value.toString();
		}
	}

	public static Calendar getCalendar(DataBean focus, String attributeName)
	{
		final Object value;
		value = focus.getValue(attributeName);
		if (value instanceof Calendar)
		{
			return (Calendar) value;
		}
		return null;
	}

	public static double getDouble(DataBean focus, String attributeName)
	{
		final Object value;
		value = focus.getValue(attributeName);
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
		else if (value instanceof Number)
		{
			return ((Number) value).doubleValue();
		}
		else
		{
			return Double.parseDouble(value.toString());
		}

	}

	private static double getDouble(DataBean focus, String attributeName, double defaultValue)
	{

		final Object value;
		value = focus.getValue(attributeName);
		if (value == null)
		{
			return defaultValue;
		}
		else if (value instanceof BigDecimal)
		{
			return ((BigDecimal) value).doubleValue();
		}
		else if (value instanceof Double)
		{
			return ((Double) value).doubleValue();
		}
		else if (value instanceof Number)
		{
			return ((Number) value).doubleValue();
		}
		else
		{
			return Double.parseDouble(value.toString());
		}

	}

	public static double getDouble(BioBean bio, String attributeName, boolean previous) {
		final Object value;
		value = bio.getValue(attributeName, previous);
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
		else if (value instanceof Number)
		{
			return ((Number) value).doubleValue();
		}
		else
		{
			return Double.parseDouble(value.toString());
		}
	}

	public static int getInt(DataBean focus, String attributeName)
	{
		final Object value;
		value = focus.getValue(attributeName);
		if (value == null)
		{
			// uninitialized
			return 0;
		}
		else if (value instanceof Integer)
		{
			return ((Integer) value).intValue();
		}
		else if (value instanceof Number)
		{
			return ((Number) value).intValue();
		}
		else
		{
			return Integer.parseInt(value.toString());
		}
	}

	public static Integer getInteger(DataBean focus, String attributeName)
	{
		final Object value;
		value = focus.getValue(attributeName);
		if (value == null)
		{
			// uninitialized
			return null;
		}
		else if (value instanceof Integer)
		{
			return ((Integer) value);
		}
		else if (value instanceof Number)
		{
			return ((Number) value).intValue();
		}
		else
		{
			return new Integer(value.toString());
		}
	}
	
	public static String getString(DataBean focus, String attributeName)
	{
		final Object value;
		value = focus.getValue(attributeName);
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

	public static String getStringNoNull(DataBean focus, String attributeName)
	{
		final Object value;
		value = focus.getValue(attributeName);
		if (value == null)
		{
			return "";
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
	
	public static void setUppercase(DataBean bio, String attribute) {
		if (!StringUtils
				.isEmpty(BioAttributeUtil.getString(bio, attribute))) {
			bio.setValue(attribute, BioAttributeUtil.getString(bio,
					attribute).toUpperCase());
		}
	}
	
	public static void checkNull(DataBean focus, String attribute){
		if(StringUtils.isEmpty(BioAttributeUtil.getString(focus, attribute))) {
			focus.setValue(attribute, " ");
		}
	}


	DataBean dataBean;

	public BioAttributeUtil(DataBean dataBean)
	{
		this.dataBean = dataBean;
	}

	public double getDouble(String attribute)
	{
		if (dataBean == null)
		{
			return 0;
		}
		else
		{
			return BioAttributeUtil.getDouble(dataBean, attribute);
		}
	}

	public double getDouble(String attribute, double defaultValue)
	{
		if (dataBean == null)
		{
			return defaultValue;
		}
		else
		{
			return BioAttributeUtil.getDouble(dataBean, attribute, defaultValue);
		}
	}

	public String getString(String attribute)
	{
		if (dataBean == null)
		{
			return null;
		}
		else
		{
			return BioAttributeUtil.getString(dataBean, attribute);
		}
	}



}
