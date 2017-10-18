/**
 * 
 */
package com.ssaglobal.scm.wms.wm_item.ui;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.text.ParseException;
import com.ssaglobal.scm.wms.util.ReportUtil;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.data.bio.Query;

/**
 * @author mma
 *
 */
public class ItemHistoryFieldListTabClick extends com.epiphany.shr.ui.action.ActionExtensionBase{
	   protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemHistoryFieldListTabClick.class);
	   protected int execute( ActionContext context, ActionResult result ) throws EpiException {
			StateInterface state = context.getState();
			TimeZone localTimeZone = ReportUtil.getTimeZone(context.getState());
			RuntimeFormInterface auditFieldList = state.getCurrentRuntimeForm();
			String name = auditFieldList.getName();
			DataBean dataBean = auditFieldList.getFocus();
			BioCollectionBean bcb = null;
			if(dataBean.isBioCollection()){
				bcb = (BioCollectionBean)dataBean;
				Query query = new Query("field_level_audit", null,"field_level_audit.time_stamp desc");
				bcb.filterInPlace(query); 
				int size = bcb.size();
				BioBean bioBean = null;
				String newValue="";
				String oldValue = "";
				String newValueConverted="";
				String oldValueConverted = "";
				for(int i=0;i<size;i++){
					bioBean = bcb.get(i+"");
					newValue= bioBean.get("new_value")==null?"":bioBean.get("new_value").toString().trim();
					oldValue = bioBean.get("old_value")==null?"":bioBean.get("old_value").toString().trim();
					if(!"".equalsIgnoreCase(newValue)){
						try{
							newValueConverted = convertDateToLocalTime(newValue, localTimeZone);
							bioBean.set("new_value", newValueConverted);
						}catch(ParseException e){
							continue;
						}
					}
					if(!"".equalsIgnoreCase(oldValue)){
						try{
							oldValueConverted = convertDateToLocalTime(oldValue, localTimeZone);
							bioBean.set("old_value", oldValueConverted);
						}catch(ParseException e){
							continue;
						}
					}
				}
				
				result.setFocus(bcb);
			}

			return RET_CONTINUE;
	   }
		public static String convertDateToLocalTime(String gmtTime, TimeZone localTimeZone) throws ParseException{
				DateFormat dateFormatLong = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
				Date utcDate = dateFormatLong.parse(gmtTime.trim());
				long utcMiliseconds = utcDate.getTime();
				GregorianCalendar cal = new GregorianCalendar(localTimeZone); 
				cal.setTimeInMillis(utcMiliseconds);			
				String localDate = dateFormatLong.format(new Date(utcMiliseconds + cal.get(Calendar.ZONE_OFFSET) + cal.get(Calendar.DST_OFFSET)));
				return localDate;
		}
}
