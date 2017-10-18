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
package com.ssaglobal.scm.wms.wm_assigned_work.ui;

import java.io.Serializable;

public class AWSearch implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String waveKey;

	private String assignmentNumber;

	private String shipmentOrderNumber;

	private String route;

	private String stop;

	private String userid;

	public AWSearch(String waveKey, String assignmentNumber, String shipmentOrderNumber, String route, String stop, String userId)
	{
		this.waveKey = waveKey;
		this.assignmentNumber = assignmentNumber;
		this.shipmentOrderNumber = shipmentOrderNumber;
		this.route = route;
		this.stop = stop;
		this.userid = userId;
	}

	public AWSearch(String waveKey, String assignmentNumber, String shipmentOrderNumber, String route, String stop)
	{
		this.waveKey = waveKey;
		this.assignmentNumber = assignmentNumber;
		this.shipmentOrderNumber = shipmentOrderNumber;
		this.route = route;
		this.stop = stop;
	}

	public void setStop(String stop)
	{
		this.stop = stop;
	}

	public String getStop()
	{
		return stop;
	}

	public void setRoute(String route)
	{
		this.route = route;
	}

	public String getRoute()
	{
		return route;
	}

	public void setShipmentOrderNumber(String shipmentOrderNumber)
	{
		this.shipmentOrderNumber = shipmentOrderNumber;
	}

	public String getShipmentOrderNumber()
	{
		return shipmentOrderNumber;
	}

	public void setAssignmentNumber(String assignmentNumber)
	{
		this.assignmentNumber = assignmentNumber;
	}

	public String getAssignmentNumber()
	{
		return assignmentNumber;
	}

	public void setWaveKey(String waveKey)
	{
		this.waveKey = waveKey;
	}

	public String getWaveKey()
	{
		return waveKey;
	}

	public void setUserid(String userid)
	{
		this.userid = userid;
	}
	

	public String getUserid()
	{
		return userid;
	}
}
