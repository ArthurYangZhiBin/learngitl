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

import java.io.PrintWriter;
import java.io.StringWriter;

public class StringUtils
{

	public static String capitalise(String s)
	{
		if (s.length() == 0)
		{
			return s;
		}
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}

	public static boolean isEmpty(String stringResult)
	{
		if (stringResult == null)
		{
			return true;
		}
		else if (stringResult.toString().matches("\\s*"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static String getStackTraceAsString(Exception exception)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		pw.print(" [ ");
		pw.print(exception.getClass().getName());
		pw.print(" ] ");
		pw.print(exception.getMessage());
		exception.printStackTrace(pw);
		return sw.toString();
	}
	
	public static String removeTrailingColon(String label) {
		if (label.endsWith(":")) {
			label = label.substring(0, label.length() - 1);
		}
		return label;
	}

	public static boolean isNull(Object attributeValue) {

		if (attributeValue == null) {
			return true;
		} else {
			return false;
		}

	}
}
