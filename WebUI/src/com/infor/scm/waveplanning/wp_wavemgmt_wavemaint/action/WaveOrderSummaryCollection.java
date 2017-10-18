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
package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.action;
import java.util.*;

/**
 * TODO Document WaveOrderSummaryCollection class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class WaveOrderSummaryCollection {

	private ArrayList waveShipmentOrderSummaries = null;
	
	
	public void addShipmentOrderSummary(ShipmentOrderSummary shipmentOrderSummary)
	{
		if (waveShipmentOrderSummaries == null)
		{
			waveShipmentOrderSummaries = new ArrayList();
		}
		waveShipmentOrderSummaries.add(shipmentOrderSummary);
	}
	
	public int getNumOfShipmentOrderSummaries()
	{
		if (waveShipmentOrderSummaries == null)
		{
			return(0);
		}
		
		return(waveShipmentOrderSummaries.size());
	}
	
	public ShipmentOrderSummary getShipmentOrderSummary(int index)
	{
		if (waveShipmentOrderSummaries == null)
		{
			return(null);
		}
		
		return((ShipmentOrderSummary)waveShipmentOrderSummaries.get(index));
	}
	
	
}

