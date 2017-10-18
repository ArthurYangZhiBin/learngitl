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

import java.util.Calendar;

public class CalendarUtil
{

	public static int fieldDifference(Calendar from, Calendar to, int field)
	{
		int min = 0;
		long startMs = from.getTimeInMillis();
		long targetMs = to.getTimeInMillis();
		if (startMs < targetMs)
		{
			int max = 1;
			do
			{
				from.setTimeInMillis(startMs);
				from.add(field, max);
				long ms = from.getTimeInMillis();
				if (ms == targetMs)
					return max;
				if (ms > targetMs)
					break;
				max <<= 1;
				if (max < 0)
					throw new RuntimeException();
			} while (true);
			while (max - min > 1)
			{
				int t = (min + max) / 2;
				from.setTimeInMillis(startMs);
				from.add(field, t);
				long ms = from.getTimeInMillis();
				if (ms == targetMs)
					return t;
				if (ms > targetMs)
					max = t;
				else
					min = t;
			}
		}
		else if (startMs > targetMs)
		{
			int max = -1;
			do
			{
				from.setTimeInMillis(startMs);
				from.add(field, max);
				long ms = from.getTimeInMillis();
				if (ms == targetMs)
					return max;
				if (ms < targetMs)
					break;
				max <<= 1;
				if (max == 0)
					throw new RuntimeException();
			} while (true);
			while (min - max > 1)
			{
				int t = (min + max) / 2;
				from.setTimeInMillis(startMs);
				from.add(field, t);
				long ms = from.getTimeInMillis();
				if (ms == targetMs)
					return t;
				if (ms < targetMs)
					max = t;
				else
					min = t;
			}
		}
		from.setTimeInMillis(startMs);
		from.add(field, min);
		return min;
	}

}
