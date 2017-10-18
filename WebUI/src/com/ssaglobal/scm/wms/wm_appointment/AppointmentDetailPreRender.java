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

package com.ssaglobal.scm.wms.wm_appointment;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.ReportUtil;
import com.ssaglobal.scm.wms.util.SessionUtil;
import com.ssaglobal.scm.wms.wm_appointment.chart.AppointmentGraphQuery;
import com.ssaglobal.scm.wms.wm_appointment.chart.util.AppointmentSessionObject;

/*
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected to be
 * provided from the meta <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class AppointmentDetailPreRender extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{

	/**
	 * Called in response to the pre-render event on a form. Write code to
	 * customize the properties of a form. All code that initializes the
	 * properties of a form that is being displayed to a user for the first time
	 * belong here. This is not executed even if the form is re-displayed to the
	 * end user on subsequent actions.
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	@Override
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException
	{

		try
		{
			// Add your code here to process the event
			StateInterface state = context.getState();
			DataBean focus = form.getFocus();

			convertDateToHMS(form, focus, state);

			setDefaultValues(form, state, focus);
			//defect1129.b
			if (! focus.isTempBio()){
				form.getFormWidgetByName("APPOINTMENTTYPE").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);	
			}
			//defect1129.e
		} catch (Exception e)
		{

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	private void convertDateToHMS(RuntimeNormalFormInterface form, DataBean focus, StateInterface state)
	{
		DateFormat shortF = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.LONG);
		DateFormat hourF = new SimpleDateFormat("HH");
		DateFormat minF = new SimpleDateFormat("mm");
		//defect1125.b
		TimeZone timeZone = ReportUtil.getTimeZone(state);
		hourF.setTimeZone(timeZone);
		minF.setTimeZone(timeZone);
		//defect1125.e
		NumberFormat nf = NumberFormat.getIntegerInstance();
		nf.setMinimumIntegerDigits(2);
		Calendar start = (Calendar) focus.getValue("GMTSTARTDATEANDTIME");
		if (start != null)
		{
			RuntimeFormWidgetInterface startDate = form.getFormWidgetByName("STARTDATE");
			RuntimeFormWidgetInterface startHour = form.getFormWidgetByName("STARTHOUR");
			RuntimeFormWidgetInterface startMin = form.getFormWidgetByName("STARTMIN");

			//			int hour = start.get(Calendar.HOUR_OF_DAY);
			//			int min = start.get(Calendar.MINUTE);
			int hour = Integer.parseInt(hourF.format(start.getTime()));
			int min = Integer.parseInt(minF.format(start.getTime()));

			startDate.setCalendarValue(start);
			startHour.setValue(String.format("%02d", hour));
			startMin.setValue(String.format("%02d", min));
		}
		Calendar end = (Calendar) focus.getValue("GMTENDDATEANDTIME");
		if (end != null)
		{
			RuntimeFormWidgetInterface endDate = form.getFormWidgetByName("ENDDATE");
			RuntimeFormWidgetInterface endHour = form.getFormWidgetByName("ENDHOUR");
			RuntimeFormWidgetInterface endMin = form.getFormWidgetByName("ENDMIN");

			//			int hour = end.get(Calendar.HOUR_OF_DAY);
			//			int min = end.get(Calendar.MINUTE);
			int hour = Integer.parseInt(hourF.format(end.getTime()));
			int min = Integer.parseInt(minF.format(end.getTime()));

			endDate.setCalendarValue(end);
			endHour.setValue(String.format("%02d", hour));
			endMin.setValue(String.format("%02d", min));
		}
	}

	private void setDefaultValues(RuntimeNormalFormInterface form, StateInterface state, DataBean focus)
	{
		if (focus.isTempBio())
		{
			//new record
			//set defaults if they exist

			AppointmentSessionObject appointmentObject = (AppointmentSessionObject) SessionUtil.getInteractionSessionAttribute(AppointmentGraphQuery.APPOINTMENT_DEFAULT, state);
			if (appointmentObject != null)
			{
				if (appointmentObject.getDateCal() != null)
				{
					RuntimeFormWidgetInterface startDate = form.getFormWidgetByName("STARTDATE");
					RuntimeFormWidgetInterface endDate = form.getFormWidgetByName("ENDDATE");
					startDate.setCalendarValue((Calendar) appointmentObject.getDateCal().clone());
					endDate.setCalendarValue((Calendar) appointmentObject.getDateCal().clone());
				}
				if (appointmentObject.getTimeCal() != null)
				{
					RuntimeFormWidgetInterface startHour = form.getFormWidgetByName("STARTHOUR");
					RuntimeFormWidgetInterface endHour = form.getFormWidgetByName("ENDHOUR");
					int hour = appointmentObject.getTimeCal().get(Calendar.HOUR_OF_DAY);
					startHour.setValue(String.format("%02d", hour));
					endHour.setValue(String.format("%02d", hour));
				}
				if (appointmentObject.getDoor() != null)
				{
					RuntimeFormWidgetInterface door = form.getFormWidgetByName("DOOR");
					door.setDisplayValue(appointmentObject.getDoor());
					focus.setValue("DOOR", appointmentObject.getDoor());
				}
			}
		}
	}

	@Override
	protected int modifySubSlots(UIRenderContext context, RuntimeFormExtendedInterface form) throws EpiException
	{
		StateInterface state = context.getState();
		SlotInterface subSlot = form.getSubSlot("LIST_SLOT");
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		DataBean focus = form.getFocus();
		if (focus.isTempBio())
		{
			form.setSpecialFormType(state, subSlot, "", "blank");
			return RET_CONTINUE;
		}
		int type = BioAttributeUtil.getInt(focus, "APPOINTMENTTYPE");
		BioCollectionBean appointmentDetails = null;

		appointmentDetails = (BioCollectionBean) focus.getValue("APPOINTMENTDETAIL");

		if (type == 0)
		{
			String query = "";
			for (int i = 0; i < appointmentDetails.size(); i++)
			{
				BioBean apptDetail = appointmentDetails.get("" + i);
				if (BioAttributeUtil.getInt(apptDetail, "SOURCETYPE") == 0)
				{
					if (i > 0)
					{
						query += " OR ";
					}
					query += " receipt.RECEIPTKEY = '" + apptDetail.getValue("SOURCEKEY") + "' ";
				}

			}

			if ("".equals(query) || appointmentDetails == null || appointmentDetails.size() == 0)
			{
				QBEBioBean bio = uow.getQBEBio("receipt");
				form.setFocus(state, subSlot, "", bio, "wm_appointment_asn_list_view");
			}
			else
			{
				BioCollectionBean rs = uow.getBioCollectionBean(new Query("receipt", query, null));
				form.setFocus(state, subSlot, "", rs, "wm_appointment_asn_list_view");
			}
		}
		else if (type == 1)
		{

			String query = "";
			for (int i = 0; i < appointmentDetails.size(); i++)
			{
				BioBean apptDetail = appointmentDetails.get("" + i);
				if (BioAttributeUtil.getInt(apptDetail, "SOURCETYPE") == 1)
				{
					if (i > 0)
					{
						query += " OR ";
					}
					query += " wm_orders.ORDERKEY = '" + apptDetail.getValue("SOURCEKEY") + "' ";
				}

			}

			if ("".equals(query) || appointmentDetails == null || appointmentDetails.size() == 0)
			{
				QBEBioBean bio = uow.getQBEBio("wm_orders");
				form.setFocus(state, subSlot, "", bio, "wm_appointment_order_list_view");
			}
			else
			{
				BioCollectionBean rs = uow.getBioCollectionBean(new Query("wm_orders", query, null));
				form.setFocus(state, subSlot, "", rs, "wm_appointment_order_list_view");
			}
		}
		else
		{
			form.setSpecialFormType(state, subSlot, "", "blank");
		}
		//defect1129.b
		if (! focus.isTempBio()){
			form.getFormWidgetByName("APPOINTMENTTYPE").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);	
		}
		//defect1129.e
		return RET_CONTINUE;
	}
}
