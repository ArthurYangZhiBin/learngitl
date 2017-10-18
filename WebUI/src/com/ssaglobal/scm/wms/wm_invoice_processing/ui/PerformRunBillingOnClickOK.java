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
package com.ssaglobal.scm.wms.wm_invoice_processing.ui;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.DataValue;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class PerformRunBillingOnClickOK extends com.epiphany.shr.ui.action.ActionExtensionBase implements Serializable
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PerformRunBillingOnClickOK.class);
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
	  	 _log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Executing PerformRunBillingOnClickOK",100L);

		RunBillingDataObject object = new RunBillingDataObject();

		String chargeType = "";
		//String billingStart = "";
		//String billingEnd = "";
		//String storerStart = "";
		//String storerEnd = "";
		//String date = "";
		int uncheckedCount = 0;
		Calendar dateVal;
		String endTime = "";

		StateInterface state = context.getState();
		RuntimeFormInterface form = state.getCurrentRuntimeForm();
		//RuntimeFormWidgetInterface widgetSource = context.getSourceWidget();
		//RuntimeFormInterface shellForm = form.getParentForm(state);

		RuntimeFormInterface subSlotForm = state.getRuntimeForm(form.getSubSlot("chargetype"), null);
		RuntimeFormInterface subSlotGroupSelector = state.getRuntimeForm(form.getSubSlot("groupselector"), null);
		RunBillingDataObject newObj = setGroupSelectorValues(subSlotGroupSelector, object);


		 _log.debug("LOG_DEBUG_EXTENSION_INV_PROC","subSlotGroupSelector:" + subSlotGroupSelector.getName(),100L);
		RuntimeFormWidgetInterface dateWidget = form.getFormWidgetByName("CUTOFFDATE");
		if (!((String) dateWidget.getValue() == null) && !((String) dateWidget.getValue() == ("")))
		{
			dateVal= dateWidget.getCalendarValue();		
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			SimpleDateFormat timeZone = new SimpleDateFormat("z");
			//SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss z");
			GregorianCalendar gc = new GregorianCalendar();
			String time = "23:59:59 " +timeZone.format(gc.getTime());
			//endTime = dateFormat.format(dateVal.getTime())+ " " +timeFormat.format(gc.getTime()) ;
			endTime = dateFormat.format(dateVal.getTime())+ " " +time ;
			 _log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Cut Off Date:" +time,100L);
		}
		else
		{
			String[] param = new String[1];
			param[0] = readLabel(form, dateWidget.getName());
			throw new UserException("WMEXP_OWNER_NOT_NULL", param);
		}

		Date test ;
		
		DateFormat oaDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");
		Date oaFormattedDate = null;
		Calendar cal = null;
		try
		{
			//_log.debug("LOG_SYSTEM_OUT","\n\nUSING: " +dateWidget.getValue().toString(),100L);
			//oaFormattedDate = oaDateFormat.parse(dateWidget.getValue().toString());
			oaFormattedDate = oaDateFormat.parse(endTime);
			test= oaFormattedDate;
			object.setDate(test);

			
		} catch (ParseException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		

		Iterator widgets = subSlotForm.getFormWidgets();
		while (widgets.hasNext())
		{
			Object obj = widgets.next();
			RuntimeFormWidgetInterface widget = (RuntimeFormWidgetInterface) obj;
			//_log.debug("LOG_SYSTEM_OUT","\n\nSelecting: " +widget.getName(),100L);
			if (!widget.getName().startsWith("place"))
			{
				if (widget.getValue().toString().equals("1"))
				{
					String label = readLabel(subSlotForm, widget.getName());
					// Amar: Merged the code changes done for fixing the SDIS issue SCM-00000-05727 Machine 2156120					
					label = widget.getName();
					// Amar: End of code changes done for fixing the SDIS issue SCM-00000-05727 Machine 2156120					
					String chType = setChargeType(label);
					chargeType = chargeType + chType + "x";
				}
				else
				{
					uncheckedCount++;
				}

			}
		}
		if (uncheckedCount == 8)
		{
			throw new UserException("WMEXP_MUST_CHECK_CT", new Object[1]);
		}
		chargeType = chargeType + "MT";
		object.setChargeType(chargeType);
		
		SimpleDateFormat finalDate= new SimpleDateFormat("dd-MMM-yyyy");
		SimpleDateFormat ejbFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		//_log.debug("LOG_SYSTEM_OUT","\n\t" + ejbFormat.format(oaFormattedDate) + "\n",100L);
		//_log.debug("LOG_SYSTEM_OUT","\n\t" + finalDate.format(oaFormattedDate) + "\n",100L);
		//object.setDate(finalDate.format(oaFormattedDate));
		
		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		Array parms = new Array();
		parms.add(new TextData(newObj.getBillingStart()));
		parms.add(new TextData(newObj.getBillingEnd()));
		parms.add(new TextData(newObj.getOwnerStart()));
		parms.add(new TextData(newObj.getOwnerEnd()));
		parms.add(new TextData(chargeType));
		// Amar:  Merged the issue SCM-00000-04510 solved for WM 902
		parms.add(new TextData(getDBSpecificDate(state,context,dateVal)));
		//parms.add(new TextData(ejbFormat.format(oaFormattedDate)));
		//Amar: End of Merging the issue SCM-00000-04510 solved for WM 902
		parms.add(new TextData("DoInvoiceRun"));

		actionProperties.setProcedureParameters(parms);
		for (int i = 1; i <= parms.size(); i++)
		{
			//_log.debug("LOG_SYSTEM_OUT","\n\t" + i + " -- +" + parms.get(i) + "|\n",100L);
			_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","\n\t" + i + " -- +" + parms.get(i) + "|\n",100L);
		}
		actionProperties.setProcedureName("NSPBILLINGRUNWRAPPER");
		/*NSPBILLINGRUNWRAPPER

		 {as_billinggroup_start, as_billinggroup_end, as_storer_start, as_storer_end, as_chargetypes, &gnv_app.of_GetForte().of_date(String(adt_cutoffdate)), 'DoInvoiceRun'

		 */
		try
		{
			EXEDataObject obj = WmsWebuiActionsImpl.doAction(actionProperties);
			displayResults(obj);
			if (obj._rowsReturned())
			{
				Array val = new Array();
				val = obj.getAttributes();
				DataValue dv1 = obj.getAttribValue(4);
				DataValue dv2 = obj.getAttribValue(5);

				//_log.debug("LOG_SYSTEM_OUT","\n\n ****VALUES: " + dv1.getAsString() + "\t" + dv2.getAsString(),100L);
				context.getServiceManager().getUserContext().put("InvoiceTot", dv1.getAsString());
				context.getServiceManager().getUserContext().put("CANCELCOUNT", dv1.getAsString());
			}

		} catch (Exception e)
		{
			e.getMessage();
			UserException UsrExcp = new UserException(e.getMessage(), new Object[] {});
			throw UsrExcp;

		}
		context.getServiceManager().getUserContext().put("InvoicePreFilter", object);
		_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Exiting PerformRunBillingOnClickOK",100L);
		return RET_CONTINUE;
		
	}
	//Amar:  Merged the issue SCM-00000-04510 solved for WM 902
	private String getDBSpecificDate(StateInterface state,ActionContext context, Calendar dateVal )	{
		
		HttpSession 	session 	= state.getRequest().getSession();
		EpnyUserContext userCtx 	= context.getServiceManager().getUserContext();
		String 			serverType 	= (String) session.getAttribute(SetIntoHttpSessionAction.DB_TYPE);
		
		SimpleDateFormat ejbDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		SimpleDateFormat timeZone = new SimpleDateFormat("z");
		GregorianCalendar gc = new GregorianCalendar();
		String time = "23:59:59 " +timeZone.format(gc.getTime());
		String dbSpecificDate = "";
		if (serverType == null)
		{
			serverType = (String) userCtx.get(SetIntoHttpSessionAction.DB_TYPE);
		}
		if (serverType.equalsIgnoreCase("O90"))
		{
			String endTime = ejbDateFormat.format(dateVal.getTime())+ " " +time;
			String ejbDate="";
			try {
				ejbDate = ejbDateFormat.format(ejbDateFormat.parse(endTime));
			} catch (ParseException e1) {
				e1.printStackTrace();
			} 
				
			dbSpecificDate = "TO_DATE('" + ejbDate + " 23:59:59 " + "','YYYY-MM-DD HH24:MI:SS')";
		}
		else if (serverType.equalsIgnoreCase("MSS")){
			String  endTime = ejbDateFormat.format(dateVal.getTime())+ " " +time;
			String ejbDate="";
			try {
				ejbDate = ejbDateFormat.format(ejbDateFormat.parse(endTime));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			dbSpecificDate = "CONVERT(DATETIME,'" + ejbDate + " 23:59:59:555 ',21)";
		}
		else if	(serverType.equalsIgnoreCase("ODBC")){
			String endTime = ejbDateFormat.format(dateVal.getTime())+ " " +time;
			String ejbDate="";
			try {
				ejbDate = ejbDateFormat.format(ejbDateFormat.parse(endTime));
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			dbSpecificDate = "'" + ejbDate + " 23:59:59 '";

		}
		return dbSpecificDate;
	}
	//Amar: End of Merging the issue SCM-00000-04510 solved for WM 902
	
	private RunBillingDataObject setGroupSelectorValues(RuntimeFormInterface form, RunBillingDataObject obj) throws UserException, DPException
	{
		_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","In setGroupSelectorValues",100L);
		// TODO Auto-generated method stub
		String ownerVal;
		String billingVal;
		RuntimeFormWidgetInterface radioButton = form.getFormWidgetByName("GROUPESELECTOR");
		checkNull(form, form.getFormWidgetByName("GROUPESELECTOR"));

		if (radioButton.getDisplayValue().equals("BillingGroup"))
		{
			obj.setOwnerStart("");
			obj.setOwnerEnd("");
			if (!checkNull(form, form.getFormWidgetByName("BILLINGFROM")))
			{
				billingVal = form.getFormWidgetByName("BILLINGFROM").getValue().toString().toUpperCase();
				checkBillingGroup(form, form.getFormWidgetByName("BILLINGFROM"), billingVal);
				obj.setBillingStart(billingVal);

			}

			if (!checkNull(form, form.getFormWidgetByName("BILLINGTO")))
			{
				billingVal = form.getFormWidgetByName("BILLINGTO").getValue().toString().toUpperCase();
				checkBillingGroup(form, form.getFormWidgetByName("BILLINGTO"), billingVal);
				obj.setBillingEnd(billingVal);
			}

		}
		else if (radioButton.getDisplayValue().equals("Owner"))
		{
			obj.setBillingStart("");
			obj.setBillingEnd("");
			if (!checkNull(form, form.getFormWidgetByName("OWNERFROM")))
			{
				ownerVal = form.getFormWidgetByName("OWNERFROM").getValue().toString().toUpperCase();
				checkOwner(form, form.getFormWidgetByName("OWNERFROM"), ownerVal);
				obj.setOwnerStart(ownerVal);
			}

			if (!checkNull(form, form.getFormWidgetByName("OWNERTO")))
			{
				ownerVal = form.getFormWidgetByName("OWNERTO").getValue().toString().toUpperCase();
				checkOwner(form, form.getFormWidgetByName("OWNERTO"), ownerVal);
				obj.setOwnerEnd(ownerVal);
			}

		}

		_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Leaving setGroupSelectorValues",100L);
		return obj;

	}

	private void checkBillingGroup(RuntimeFormInterface form, RuntimeFormWidgetInterface widget, String billingVal) throws DPException, UserException
	{
		_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","In checkBillingGroup",100L);
		if(!billingVal.equals("0") && !billingVal.equals("ZZZZZZZZZZZZZZZ"))
		{
		String sql = "SELECT * FROM STORERBILLING " + "WHERE BILLINGGROUP = '" + billingVal + "'";
		//_log.debug("LOG_SYSTEM_OUT","\n\t" + sql + "\n",100L);
		 _log.debug("LOG_DEBUG_EXTENSION_INV_PROC","***sql:" +sql,100L);
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(sql);
		if (results.getRowCount() < 1)
		{
			//invalid billinggroup
			String[] param = new String[2];
			param[0] = billingVal;
			param[1] = readLabel(form, widget.getName());
			throw new UserException("WMEXP_INV_OWNER", param);
		}
		}
		_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Leaving checkBillingGroup",100L);
	}

	private void checkOwner(RuntimeFormInterface form, RuntimeFormWidgetInterface widget, String ownerVal) throws UserException
	{
		_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","In checkOwner",100L);
		String owner = ownerVal;
		if(!owner.equals("0") && !owner.equals("ZZZZZZZZZZZZZZZ"))
		{
		String sql = "SELECT * FROM storer " + "WHERE (storerkey = '" + owner + "') " + "AND (type = '1') ";
		EXEDataObject dataObject;
		try
		{
			dataObject = WmsWebuiValidationSelectImpl.select(sql);
			if (dataObject.getCount() < 1)
			{
				//invalid owner
				String[] param = new String[2];
				param[0] = ownerVal;
				param[1] = readLabel(form, widget.getName());
				throw new UserException("WMEXP_INV_OWNER", param);
			}
		} catch (DPException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Leaving checkOwner",100L);
	}

	private boolean checkNull(RuntimeFormInterface form, RuntimeFormWidgetInterface widget) throws UserException
	{
		// TODO Auto-generated method stub
		if (widget.getDisplayValue() == null)
		{
			String[] param = new String[1];
			param[0] = readLabel(form, widget.getName());
			throw new UserException("WMEXP_OWNER_NOT_NULL", param);
		}
		else if (widget.getDisplayValue().equals(""))
		{
			String[] param = new String[1];
			param[0] = readLabel(form, widget.getName());
			throw new UserException("WMEXP_OWNER_NOT_NULL", param);
		}
		else
		{
			return false;
		}

	}

	private String setChargeType(String label)
	{
		// TODO Auto-generated method stub
		String str = null;
		// Amar: Merged the code changes done for fixing the SDIS issue SCM-00000-05727 Machine 2156120 
		if (label.equals("CONTAINEROUT"))
		{
			str = "CO";
		}
		else if (label.equals("CONTAINERIN"))
		{
			str = "CI";
		}
		else if (label.equals("ACCESSORIAL"))
		{
			str = "AC";
		}
		else if (label.equals("HANDLINGIN"))
		{
			str = "HI";
		}
		else if (label.equals("HANDLINGOUT"))
		{
			str = "HO";
		}
		else if (label.equals("INITIALSTORAGE"))
		{
			str = "IS";
		}
		else if (label.equals("RECURRINGSTORAGE"))
		{
			str = "RS";
		}
		else if (label.equals("SPECIALCHARGES"))
		{
			str = "SP";
		}
		// Amar: End of code changes done for fixing the SDIS issue SCM-00000-05727 Machine 2156120
		return str;
	}

	public String readLabel(RuntimeFormInterface form, String widgetName)
	{
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return form.getFormWidgetByName(widgetName).getLabel("label", locale);
	}

	private void displayResults(EXEDataObject results)
	{
		_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","In displayResults",100L);
		//_log.debug("LOG_SYSTEM_OUT","\n\t" + results.getRowCount() + " x " + results.getColumnCount() + "\n",100L);
		if (results.getColumnCount() != 0)
		{
			/*
			for (int i = 1; i < results.getColumnCount() + 1; i++)
			{
				try
				{
					System.out.println(" " + i + " @ " + results.getAttribute(i).name + " "
							+ results.getAttribute(i).value.getAsString());
				} catch (Exception e)
				{
					_log.debug("LOG_SYSTEM_OUT",e.getMessage(),100L);
				}
			}
			*/
			//_log.debug("LOG_SYSTEM_OUT","----------",100L);
		}
		_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Leaving displayResults",100L);
	}

}
