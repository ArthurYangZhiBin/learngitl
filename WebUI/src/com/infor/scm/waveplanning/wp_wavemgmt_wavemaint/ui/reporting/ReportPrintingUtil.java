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
package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.ui.reporting;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.util.ReportUtil;

public class ReportPrintingUtil {
	public static ArrayList<Order> getOrders(StateInterface state, String wmsWaveKey)throws EpiException{
		ArrayList<Order> orders = new ArrayList<Order>();
		Order order= null;
		Query qry = new Query("wp_vWaveOrders", "wp_vWaveOrders.WAVEKEY='"+wmsWaveKey+"'", null);
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		BioCollectionBean resultCollection = uowb.getBioCollectionBean(qry);
		if(resultCollection != null){
			int size = resultCollection.size();
			for(int i=0; i<size; i++){
				order = new Order();
				order.setOrderKey(resultCollection.elementAt(i).getString("ORDERKEY"));
				order.setRequestedShipDate(convertToGmt(((GregorianCalendar)resultCollection.elementAt(i).get("REQUESTEDSHIPDATE")).getTime()));
				order.setOrderDate(convertToGmt(((GregorianCalendar)resultCollection.elementAt(i).get("ORDERDATE")).getTime()));
				orders.add(order);
			}
		}
		uowb.clearState();
		
		return orders;
	}
	
	
	private static Date convertToGmt( Date date )
	{
	   TimeZone tz = TimeZone.getDefault();
	   Date ret = new Date( date.getTime() - tz.getRawOffset() );

	   // if we are now in DST, back off by the delta.  Note that we are checking the GMT date, this is the KEY.
	   if ( tz.inDaylightTime( ret ))
	   {
	      Date dstDate = new Date( ret.getTime() - tz.getDSTSavings() );

	      // check to make sure we have not crossed back into standard time
	      // this happens when we are on the cusp of DST (7pm the day before the change for PDT)
	      if ( tz.inDaylightTime( dstDate ))
	      {
	         ret = dstDate;
	      }
	   }

	   return ret;
   }
	
	public static String convertUtilDateToReportDate(Date date, StateInterface state){
		
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		calendar.setTime(date);
		DateFormat dateFormat= ReportUtil.retrieveDateFormat(state);
		int hours = calendar.get(Calendar.HOUR_OF_DAY);
		int minutes = calendar.get(Calendar.MINUTE);
		int seconds = calendar.get(Calendar.SECOND);
		String strDate = dateFormat.format(date)+ "%20"+convertHMS(hours)+"%3a"+convertHMS(minutes)+"%3a"+convertHMS(seconds)+".000";
		return strDate;
		
	}
	
	public static String convertHMS(int hms){
		if(hms<10){
			return "0"+hms;
		}else{
			return hms+"";
		}

	}
	
}
