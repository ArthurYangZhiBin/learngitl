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
package com.infor.scm.waveplanning.common.util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.model.data.DataBean;

public class BioAttributeUtil
{

	public static String getString(Bio bio, String attributeName)
	{
		final Object value;
		try
		{
			value = bio.get(attributeName);
		} catch (EpiDataException e)
		{
			e.printStackTrace();
			return null;
		}
		if (value == null)
		{
			return null;
		}
		else
		{
			return (String) value;
		}
	}

	public static int getInt(Bio bio, String attributeName)
	{
		final Object value;
		try
		{
			value = bio.get(attributeName);
		} catch (EpiDataException e)
		{
			e.printStackTrace();
			return 0;
		}
		if (value == null)
		{
			return 0;
		}
		else
		{
			return ((Integer) value).intValue();
		}
	}

	public static Date getDate(Bio bio, String attributeName)
	{
		final Object value;
		try
		{
			value = bio.get(attributeName);
		} catch (EpiDataException e)
		{
			e.printStackTrace();
			return null;
		}
		if (value == null)
		{
			return null;
		}
		else
		{
			return ((Calendar) value).getTime();
		}
	}

	public static int getInt(DataBean focus, String attributeName)
	{
		final Object value;
		value = focus.getValue(attributeName);
		if (value == null)
		{
			return 0;
		}
		else
		{
			return ((Integer) value).intValue();
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
			return (String) value;
		}
	}

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

	public static double getDouble(DataBean focus, String attributeName)
	{
		final Object value;
		value = focus.getValue(attributeName);
		if (value == null)
		{
			return 0;
		}
		else if(value instanceof BigDecimal)
		{
			return ((BigDecimal)value).doubleValue();
		}
		else if(value instanceof Double)
		{
			return ((Double)value).doubleValue();
		}
		else
		{
			return Double.parseDouble(value.toString());
		}

	}

	public static double getDouble(Bio focus, String attributeName)
	{
		final Object value;
		try
		{
			value = focus.get(attributeName);
			if (value == null)
			{
				return 0;
			}
			else if(value instanceof BigDecimal)
			{
				return ((BigDecimal)value).doubleValue();
			}
			else if(value instanceof Double)
			{
				return ((Double)value).doubleValue();
			}
			else
			{
				return Double.parseDouble(value.toString());
			}
		} catch (EpiDataException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}

	}

	//	public static Double getDouble(DataBean focus, String string)
	//	{
	//		// TODO Auto-generated method stub
	//		
	//	}
}
