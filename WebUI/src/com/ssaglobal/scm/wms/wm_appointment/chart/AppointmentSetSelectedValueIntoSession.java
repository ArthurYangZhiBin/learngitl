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

package com.ssaglobal.scm.wms.wm_appointment.chart;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.SessionUtil;
import com.ssaglobal.scm.wms.wm_appointment.chart.util.AppointmentSessionObject;
import com.ssaglobal.scm.wms.wm_appointment.chart.util.GraphWidget;

/*
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected to be
 * provided from the meta <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class AppointmentSetSelectedValueIntoSession extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(AppointmentSetSelectedValueIntoSession.class);

	/**
	 * The code within the execute method will be run from a UIAction specified
	 * in metadata.
	 * <P>
	 * 
	 * @param context
	 *            The ActionContext for this extension
	 * @param result
	 *            The ActionResult for this extension (contains the focus and
	 *            perspective for this UI Extension)
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * 
	 * @exception EpiException
	 */
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{

		StateInterface state = context.getState();
		String tooltipParameter = state.getURLParameter("tooltip");
		String sourceWidget = context.getSourceWidget().getName();
		if ("SELECTDAY".equals(sourceWidget))
		{
			DateFormat shortDF = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, state.getUser().getLocale().getJavaLocale());
			DateFormat chartDF = new SimpleDateFormat("EEE " + ((SimpleDateFormat) shortDF).toPattern(), state.getUser().getLocale().getJavaLocale());
			Date selectedDate = null;
			try
			{
				selectedDate = chartDF.parse(tooltipParameter);
				_log.info("LOG_INFO_EXTENSION_AppointmentSetSelectedDateIntoSession_execute", "Parsed " + shortDF.format(selectedDate), SuggestedCategory.NONE);
			} catch (ParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			AppointmentSessionObject appObj = new AppointmentSessionObject();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(selectedDate);
			appObj.setDateCal(calendar);
			appObj.setWidget(GraphWidget.SELECTDAY);
			SessionUtil.setInteractionSessionAttribute(AppointmentSessionObject.APP_SESSION_OBJECT, appObj, state);
		}
		else if ("SELECTHOUR".equals(sourceWidget) || "SELECTDETAILHOUR".equals(sourceWidget))
		{
			DateFormat timeShortFormat = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT, state.getUser().getLocale().getJavaLocale());
			//get app session object from session
			AppointmentSessionObject appObj = (AppointmentSessionObject) SessionUtil.getInteractionSessionAttribute(AppointmentSessionObject.APP_SESSION_OBJECT, state);
			//set hour
			Date parsedTime = new Date();
			try
			{
				parsedTime = timeShortFormat.parse(tooltipParameter);
			} catch (ParseException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Calendar timeCal = Calendar.getInstance();
			timeCal.setTime(parsedTime);
			//set app session object into session
			appObj.setTimeCal(timeCal);
			if ("SELECTHOUR".equals(sourceWidget))
			{
				appObj.setWidget(GraphWidget.SELECTHOUR);
			}
			else
			{
				appObj.setWidget(GraphWidget.SELECTDETAILHOUR);
			}
			SessionUtil.setInteractionSessionAttribute(AppointmentSessionObject.APP_SESSION_OBJECT, appObj, state);
		}
		else if ("SELECTDOOR".equals(sourceWidget) || "SELECTDETAILDOOR".equals(sourceWidget))
		{
			AppointmentSessionObject appObj = (AppointmentSessionObject) SessionUtil.getInteractionSessionAttribute(AppointmentSessionObject.APP_SESSION_OBJECT, state);
			appObj.setDoor(tooltipParameter);
			if ("SELECTDOOR".equals(sourceWidget))
			{
				appObj.setWidget(GraphWidget.SELECTDOOR);
			}
			else
			{
				appObj.setWidget(GraphWidget.SELECTDETAILDOOR);
			}
			SessionUtil.setInteractionSessionAttribute(AppointmentSessionObject.APP_SESSION_OBJECT, appObj, state);
		}
		else
		{
			//Nothing
		}

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}
}
