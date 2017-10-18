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
package com.infor.scm.waveplanning.wp_query_builder.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import java.text.ParseException;
import java.util.TimeZone;
import com.ssaglobal.scm.wms.util.ReportUtil;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.BioBean;

public class QueryBuilderDetailListFormPreRender extends FormExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(QueryBuilderDetailListFormPreRender.class);
	
	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) throws EpiException {
		if(!form.getFocus().isTempBio()){
			BioCollectionBean dataBean = (BioCollectionBean)form.getFocus();
			if(dataBean != null && dataBean.size() != 0){
				BioBean bioBean = null;
				
				dataBean.refresh();
//				System.out.println("*****ma biocollectionbean size="+dataBean.size());
				for(int i=0; i<dataBean.size(); i++){
					bioBean = dataBean.get(""+i);
					String firstValue = bioBean.getString("FIRSTVALUE");
					String secondValue = bioBean.getString("SECONDVALUE");
					String localTimeStr = "";
					String fieldId = bioBean.getString("ORDERFIELD");
					if("orderdate".equalsIgnoreCase(fieldId)
							|| "rsd".equalsIgnoreCase(fieldId)
							|| "dlvrdate".equalsIgnoreCase(fieldId)
							|| "dlvrdate2".equalsIgnoreCase(fieldId)){
						TimeZone localTimeZone = ReportUtil.getTimeZone(context.getState());
						if(firstValue != null){
								localTimeStr = convertDateToLocalTime(firstValue.trim(), localTimeZone);
								bioBean.set("FIRSTVALUE", localTimeStr);
	//			System.out.println("firstValue="+localTimeStr);
						}
						
						if(secondValue != null){
								localTimeStr = convertDateToLocalTime(secondValue.trim(), localTimeZone);
								bioBean.set("SECONDVALUE", localTimeStr);
						}
					}
				}
			}
		}

		return RET_CONTINUE;
	}
	
	public static String convertDateToLocalTime(String gmtTime, TimeZone localTimeZone){
		try{
			DateFormat dateFormatLong = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");  
			Date utcDate = dateFormatLong.parse(gmtTime);
			long utcMiliseconds = utcDate.getTime();
			GregorianCalendar cal = new GregorianCalendar(localTimeZone); 
			cal.setTimeInMillis(utcMiliseconds);			
			String localDate = dateFormatLong.format(new Date(utcMiliseconds + cal.get(Calendar.ZONE_OFFSET) + cal.get(Calendar.DST_OFFSET)));
			return localDate;
		}catch(ParseException e){
			return gmtTime;
		}
	}
}
