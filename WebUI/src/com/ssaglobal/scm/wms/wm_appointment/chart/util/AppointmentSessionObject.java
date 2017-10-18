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

public class AppointmentSessionObject
{
	public static String APP_SESSION_OBJECT = "APP_SESSION_OBJECT";
	
	Calendar dateCal;

	Calendar timeCal;

	String door;
	
	GraphWidget widget;

	public Calendar getDateCal()
	{
		return dateCal;
	}

	public void setDateCal(Calendar dateCal)
	{
		this.dateCal = dateCal;
	}

	public Calendar getTimeCal()
	{
		return timeCal;
	}

	public void setTimeCal(Calendar timeCal)
	{
		this.timeCal = timeCal;
	}

	public String getDoor()
	{
		return door;
	}

	public void setDoor(String door)
	{
		this.door = door;
	}

	public AppointmentSessionObject(Calendar dateCal, Calendar timeCal, String door)
	{
		super();
		this.dateCal = dateCal;
		this.timeCal = timeCal;
		this.door = door;
	}

	public AppointmentSessionObject(Calendar dateCal, Calendar timeCal)
	{
		super();
		this.dateCal = dateCal;
		this.timeCal = timeCal;
	}

	public AppointmentSessionObject(Calendar dateCal, String door)
	{
		super();
		this.dateCal = dateCal;
		this.door = door;
	}
	
	public AppointmentSessionObject()
	{

	}

	public GraphWidget getWidget()
	{
		return widget;
	}

	public void setWidget(GraphWidget widget)
	{
		this.widget = widget;
	}


}
