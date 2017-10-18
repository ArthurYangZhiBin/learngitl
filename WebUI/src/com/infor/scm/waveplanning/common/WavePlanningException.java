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
package com.infor.scm.waveplanning.common;
import java.util.ArrayList;

/**
 * TODO Document WavePlanningException class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class WavePlanningException extends Exception {

	ArrayList errors;
	
	public WavePlanningException(String errorMessage)
	{
		addError(errorMessage);
	}
	
	
	
	
	public WavePlanningException(){};			// Empty Constructor.
	
	public void addError(String errorMessage)
	{
		if (errors == null)
			errors = new ArrayList();
		
		errors.add(errorMessage);
	}
	
	public int getNumberOfErrors()
	{
		if (errors == null)
		{
			return(0);
		}
		return(errors.size());
	}
	
	public String getError(int errorIndex)
	{
		
		if (errors == null)
			return("");
		
		return((String) errors.get(errorIndex));
	}
	
}

