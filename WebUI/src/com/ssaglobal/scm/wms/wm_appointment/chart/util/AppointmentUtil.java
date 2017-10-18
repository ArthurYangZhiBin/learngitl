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
package com.ssaglobal.scm.wms.wm_appointment.chart.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.ssaglobal.scm.wms.util.CalendarUtil;
import com.ssaglobal.scm.wms.util.StringUtils;

public class AppointmentUtil
{

	public static int calculateSlots(Calendar gmtStart, Calendar gmtEnd)
	{
	
		Date gmtStartDate = gmtStart.getTime();
		Date gmtEndDate = gmtEnd.getTime();
		Calendar gmtStartCal = Calendar.getInstance();
		Calendar gmtEndCal = Calendar.getInstance();
		gmtStartCal.setTime(gmtStartDate);
		gmtEndCal.setTime(gmtEndDate);
		int minute = CalendarUtil.fieldDifference(gmtStartCal, gmtEndCal, Calendar.MINUTE);
		int slots = (int) Math.round(minute / 15.0);
		return slots;
	
	}

	public static void incrementKeyInMap(Map<String, Integer> doorMap, String key)
	{
		incrementKeyInMap(doorMap, key, 1);
	}

	public static void incrementKeyInMap(Map<String, Integer> doorMap, String key, int amount)
	{
		int existingValue = 0;
		if (doorMap.containsKey(key))
		{
			existingValue = doorMap.get(key);
		}
		existingValue += amount;
		doorMap.put(key, existingValue);
	}

	public static void incrementKeyInMap(Map<Integer, Integer> doorMap, int key, int amount)
	{
		int existingValue = 0;
		if (doorMap.containsKey(key))
		{
			existingValue = doorMap.get(key);
		}
		existingValue += amount;
		doorMap.put(key, existingValue);

	}
	
	public static String doorFilterTitle(String doorStart, String doorEnd)
	{
		String doorFilter = "";
		if (!StringUtils.isEmpty(doorStart) && !StringUtils.isEmpty(doorEnd))
		{
			doorFilter += " ( " + doorStart + " - " + doorEnd + " ) ";
		}
		else if (!StringUtils.isEmpty(doorStart))
		{
			doorFilter += " ( " + doorStart + " ) ";
		}
		else if (!StringUtils.isEmpty(doorEnd))
		{
			doorFilter += " ( " + doorEnd + " ) ";
		}
		return doorFilter;
	}

}
