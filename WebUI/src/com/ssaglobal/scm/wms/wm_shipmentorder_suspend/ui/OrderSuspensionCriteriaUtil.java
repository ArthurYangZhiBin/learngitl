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
package com.ssaglobal.scm.wms.wm_shipmentorder_suspend.ui;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.QueryHelper;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.date.DateUtil;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.StringUtils;

public class OrderSuspensionCriteriaUtil
{
	private static final String SUSPENDED_INDICATOR = "SuspendedIndicator";

	private static final String ORDER_DETAIL = "wm_orders.ORDER_DETAIL";

	protected static ILoggerCategory _log = LoggerFactory.getInstance(OrderSuspensionCriteriaVO.class);

	static final String ORDER = "wm_orders";

	final static HashMap<String, Boolean> attributeInOrderDetail = new HashMap<String, Boolean>(18);
	
	static
	{
		//is the attribute an orderdetail attribute
		attributeInOrderDetail.put("LOT", true);
		attributeInOrderDetail.put("LOTTABLE01", true);
		attributeInOrderDetail.put("LOTTABLE02", true);
		attributeInOrderDetail.put("LOTTABLE03", true);
		attributeInOrderDetail.put("LOTTABLE04", true);
		attributeInOrderDetail.put("LOTTABLE05", true);
		attributeInOrderDetail.put("LOTTABLE06", true);
		attributeInOrderDetail.put("LOTTABLE07", true);
		attributeInOrderDetail.put("LOTTABLE08", true);
		attributeInOrderDetail.put("LOTTABLE09", true);
		attributeInOrderDetail.put("LOTTABLE10", true);
		attributeInOrderDetail.put("LOTTABLE11", true);
		attributeInOrderDetail.put("LOTTABLE12", true);
		attributeInOrderDetail.put("ORDERKEY", false);
		attributeInOrderDetail.put("SKU", true);
		attributeInOrderDetail.put("STATUS", false);
		attributeInOrderDetail.put("CONSIGNEEKEY", false);
		attributeInOrderDetail.put("SuspendedIndicator", false);
	}

	/***
	 * Set the values of a {@link OrderSuspensionCriteriaVO} based on a search form
	 * @param searchCriteria
	 * @param searchForm
	 */
	public static void loadFromSearchForm(OrderSuspensionCriteriaVO searchCriteria, RuntimeFormInterface searchForm)
	{
		Field[] fields = searchCriteria.getClass().getDeclaredFields();   
		ArrayList<Field> fieldsList = removeNonSearchFormFields(fields);
		for (Field f : fieldsList)
		{
			_log.debug("LOG_DEBUG_EXTENSION_OrderSuspensionCriteriaUtil_loadFromSearchForm", f.getName(), SuggestedCategory.NONE);
			RuntimeFormWidgetInterface formWidgetByName = searchForm.getFormWidgetByName(f.getName().toUpperCase());

			if (formWidgetByName.getType().startsWith("date"))
			{
				Calendar value = formWidgetByName.getCalendarValue();
				_log.debug("LOG_DEBUG_EXTENSION_OrderSuspensionCriteriaUtil_loadFromSearchForm", "Date " + f.getName() + " " + value, SuggestedCategory.NONE);
				setValue(searchCriteria, f, value);

			}
			else
			{
				String value = formWidgetByName.getValue() == null ? null : formWidgetByName.getValue().toString();
				_log.debug("LOG_DEBUG_EXTENSION_OrderSuspensionCriteriaUtil_loadFromSearchForm", f.getName() + " " + value, SuggestedCategory.NONE);
				setValue(searchCriteria, f, value);

			}
		}
	}

	public static ArrayList<Field> removeNonSearchFormFields(Field[] fields)
	{
		ArrayList<Field> fieldsList = new ArrayList<Field>(fields.length);
		for (Field f : fields)
		{
			if (!(f.getName().equals("serialVersionUID")))
			{
				fieldsList.add(f);
			}
		}
		return fieldsList;

	}

	public static ArrayList<Field> removeNonSearchFormAndNullFields(OrderSuspensionCriteriaVO searchCriteria, Field[] fields)
	{
		ArrayList<Field> fieldsList = new ArrayList<Field>(fields.length);
		for (Field f : fields)
		{
			if (!(f.getName().equals("serialVersionUID")))
			{
				try
				{
					Method getter = searchCriteria.getClass().getMethod("get" + StringUtils.capitalise(f.getName()));
					if (getter.invoke(searchCriteria) != null)
					{
						fieldsList.add(f);
					}
				} catch (SecurityException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return fieldsList;

	}

	private static <T> void setValue(OrderSuspensionCriteriaVO searchCriteria, Field f, T value)
	{
		if (value != null)
		{
			try
			{
				Method setter = searchCriteria.getClass().getMethod("set" + StringUtils.capitalise(f.getName()), value.getClass());
				setter.invoke(searchCriteria, value);
			} catch (SecurityException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Generates a BIO Query based on a {@link OrderSuspensionCriteriaVO} object
	 * @param searchCriteria
	 * @return
	 */
	static Query generateBioQuery(OrderSuspensionCriteriaVO searchCriteria)
	{
		_log.debug("LOG_DEBUG_EXTENSION_OrderSuspensionCriteriaUtil_loadFromSearchForm", "Generating Query for Orders", SuggestedCategory.NONE);
		String query = "";
		query += OrderSuspensionCriteriaUtil.ORDER + ".STATUS != '95' ";
		_log.debug("LOG_DEBUG_EXTENSION_OrderSuspensionCriteriaUtil_loadFromSearchForm", "Query " + query, SuggestedCategory.NONE);

		//Gets list of fields and removes special fields and fields that doesn't have a value
		Field[] declaredFields = searchCriteria.getClass().getDeclaredFields();
		ArrayList<Field> fieldsList = removeNonSearchFormAndNullFields(searchCriteria, declaredFields);

		//Iterates through list of fields
		for (Field f : fieldsList)
		{
			final String name = f.getName();
			final Method getter;
			try
			{
				//an attribute name is in most cases the field name converted to uppercase
				//there is one exception, and that is with SuspendedIndicator
				String attributeName = getAttributeName(name);

				//get the value of the attribute
				getter = searchCriteria.getClass().getMethod("get" + StringUtils.capitalise(f.getName()));
				final Object rawResult = getter.invoke(searchCriteria);

				//if the attribute belongs to wm_orderdetail, special syntax is needed
				if (attributeInOrderDetail(attributeName))
				{
					if (rawResult instanceof String)
					{
						//add string to query
						final String stringResult = (String) rawResult;
						if (!StringUtils.isEmpty(stringResult))
						{
							query += " and EXISTS x IN " + ORDER_DETAIL + ":" + " (x." + attributeName + " = '" + stringResult.toUpperCase() + "') ";
							_log.debug("LOG_DEBUG_EXTENSION_OrderSuspensionCriteriaUtil_loadFromSearchForm", "Query " + query, SuggestedCategory.NONE);
						}
					}
					else if (rawResult instanceof GregorianCalendar)
					{
						GregorianCalendar calResult = (GregorianCalendar) rawResult;
						DateFormat date_format = DateUtil.DATE_FORMAT;
						//add date to query
						query += " and EXISTS x IN " + ORDER_DETAIL + ":" + " (x." + attributeName + " = " + QueryHelper.getDateString(calResult) + ") ";
						_log.debug("LOG_DEBUG_EXTENSION_OrderSuspensionCriteriaUtil_loadFromSearchForm", "Query " + query, SuggestedCategory.NONE);
					}
				}
				else
				{

					if (rawResult instanceof String)
					{
						//add string to query
						final String stringResult = (String) rawResult;
						if (!StringUtils.isEmpty(stringResult))
						{
							query += " and " + OrderSuspensionCriteriaUtil.ORDER + "." + attributeName + " = '" + stringResult.toUpperCase() + "' ";
							_log.debug("LOG_DEBUG_EXTENSION_OrderSuspensionCriteriaUtil_loadFromSearchForm", "Query " + query, SuggestedCategory.NONE);
						}
					}

				}
			} catch (SecurityException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		_log.debug("LOG_DEBUG_EXTENSION_OrderSuspensionCriteriaUtil_loadFromSearchForm", "Final Query -  " + query, SuggestedCategory.NONE);
		return new Query(OrderSuspensionCriteriaUtil.ORDER, query, null);
	}

	private static String getAttributeName(String name)
	{
		//handle naming exception
		if (name.equalsIgnoreCase(SUSPENDED_INDICATOR))
		{
			return SUSPENDED_INDICATOR;
		}
		return name.toUpperCase();
	}

	static boolean attributeInOrderDetail(String attributeName)
	{
		return attributeInOrderDetail.get(attributeName);
	}

}
