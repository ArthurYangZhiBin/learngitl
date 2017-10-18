/******************************************************
 *
 *                       NOTICE
 *
 *   THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS
 *   CONFIDENTIAL INFORMATION OF INFOR AND SHALL NOT
 *   BE DISCLOSED WITHOUT PRIOR WRITTEN PERMISSION.
 *   LICENSED CUSTOMERS MAY COPY AND ADAPT THIS
 *   SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH
 *   THE TERMS OF THEIR SOFTWARE LICENSE AGREEMENT.
 *   ALL OTHER RIGHTS RESERVED.
 *
 *   COPYRIGHT (c) 2008 INFOR. ALL RIGHTS RESERVED.
 *   THE WORD AND DESIGN MARKS SET FORTH HEREIN ARE
 *   TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 *   AND/OR RELATED AFFILIATES AND SUBSIDIARIES. ALL
 *   RIGHTS RESERVED. ALL OTHER TRADEMARKS LISTED
 *   HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE
 *   OWNERS. WWW.INFOR.COM.
 *
 ******************************************************/



package com.ssaglobal.scm.wms.wm_shipmentorder.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.DateTimeData;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.uiextensions.KeyGenBioWrapper;



/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ValidateSOTrailerNumber extends com.epiphany.shr.ui.action.ActionExtensionBase {

	SimpleDateFormat mssqlFormat = new SimpleDateFormat("dd-MMM-yyyy kk:mm:ss");
	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @exception EpiException
	 */
	protected int execute( ActionContext context, ActionResult result ) throws EpiException {

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		final String bio1 = "wm_trailer";
		final String attribute1 = "TRAILERKEY";
		int size1=0;  
		final String bio2 = "wm_orders";
		final String attribute2 = "TrailerNumber";
		int size2=0;
		final String bio3 = "wm_orders";
		final String attribute3 = "ORDERKEY";
		int size3=0;
		BioCollectionBean listCollection1=null;
		BioCollectionBean listCollection2 = null;
		BioCollectionBean listCollection3 = null;


		StateInterface state = context.getState();
		RuntimeFormInterface form = state.getCurrentRuntimeForm();
		String trailerNumber = form.getFormWidgetByName("TrailerNumber").getDisplayValue() == null || form.getFormWidgetByName("TrailerNumber").getDisplayValue().toString().matches("\\s*") ? "" : form.getFormWidgetByName("TrailerNumber").getDisplayValue().toString();
		String carrier = form.getFormWidgetByName("CarrierCode").getDisplayValue() == null || form.getFormWidgetByName("CarrierCode").getDisplayValue().toString().matches("\\s*") ? "" : form.getFormWidgetByName("CarrierCode").getDisplayValue().toString();

		mssqlFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		String currentDate=mssqlFormat.format(Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTime());

		String displayValue;
		Object objDisplayValue = trailerNumber;
		if (objDisplayValue == null){
			displayValue = "";
		}else
		{
			displayValue = objDisplayValue.toString();
			try {
				String sQueryString = bio1 + "." + attribute1 + " = '" + displayValue + "'";
				Query BioQuery = new Query(bio1,sQueryString,null);
				UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
				listCollection1 = uow.getBioCollectionBean(BioQuery);
				size1=listCollection1.size();

			} catch(EpiException e) {
				// Handle Exceptions 
				e.printStackTrace();
				return RET_CANCEL;
			} 
		}


		//Check for existing trailer
		if(size1 > 0)
		{
			String receiptValue;
			Object objreceiptValue = trailerNumber;
			if (objreceiptValue == null){
				receiptValue = "";
			}else
			{
				receiptValue = objreceiptValue.toString();
				try {
					String sQueryString = bio2 + "." + attribute2 + " = '" + receiptValue + "'";
					Query BioQuery = new Query(bio2,sQueryString,null);
					UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
					listCollection2 = uow.getBioCollectionBean(BioQuery);
					size2=listCollection2.size();

				} catch(EpiException e) {
					// Handle Exceptions 
					e.printStackTrace();
					return RET_CANCEL;
				} 
			}
			//check for trailers associated with the ASN receipts
			if(size2> 0)
			{

				String orderKey = form.getFormWidgetByName("ORDERKEY").getDisplayValue() == null || form.getFormWidgetByName("ORDERKEY").getDisplayValue().toString().matches("\\s*") ? "" : form.getFormWidgetByName("ORDERKEY").getDisplayValue().toString();

				String receiptKeyValue;
				Object objreceiptKeyValue = orderKey;
				if (objreceiptKeyValue == null){
					receiptKeyValue = "";
				}else
				{
					receiptKeyValue = objreceiptKeyValue.toString();
					try {
						String sQueryString = bio3 + "." + attribute3 + " = '" + receiptKeyValue + "'";
						Query BioQuery = new Query(bio3,sQueryString,null);
						UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
						listCollection3 = uow.getBioCollectionBean(BioQuery);
						size3=listCollection3.size();

					} catch(EpiException e) {
						// Handle Exceptions 
						e.printStackTrace();
						return RET_CANCEL;
					} 
				}

				String rDate;
				if(!(form.getFocus() instanceof QBEBioBean))
				{
					DateTimeData dtd = new DateTimeData(((GregorianCalendar) listCollection3.get("0").getValue("REQUESTEDSHIPDATE")));
					rDate=dateFormatConvertor(dtd .toString());
				}
				else
				{
					rDate=dateFormatConvertor(currentDate);
				}

				state.getRequest().getSession().setAttribute("PREVTRAILERNUMBER", trailerNumber);
				state.getRequest().getSession().setAttribute("PREVCARRIER", carrier );

				int popValue=0;
				final int count = size2;

				for(int i=0;i<count;i++)
				{
					// comparing the date and raise a modal windows
					DateTimeData dtd = new DateTimeData(((GregorianCalendar) listCollection2.get(i+"").getValue("REQUESTEDSHIPDATE")));

					if(!dateFormatConvertor(dtd.toString()).equalsIgnoreCase(rDate))
					{
						popValue=1;
						break;
					}

				}
				//Raise a modal windows
				if(popValue==1)
				{
					context.setNavigation("changeEvent353");
				}
			}



		}
		return RET_CONTINUE;
	}

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked or
	 * a value entered in a form in a modal dialog
	 * Write code here if u want this to be called when the UI Action event is fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext} exposes information about the
	 * event, including the service and the user interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes information about the results
	 * of the action that has occurred, and enables your extension to modify them.</li>
	 * </ul>
	 */
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {

		StateInterface state = ctx.getState();
		String trailer = state.getRequest().getSession().getAttribute("PREVTRAILERNUMBER").toString();
		String carrier = state.getRequest().getSession().getAttribute("PREVCARRIER").toString();
		KeyGenBioWrapper key = new KeyGenBioWrapper();

		String newTrailerKey = key.getKey("TRAILERKEY");

		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		Array params = new Array();
		params.add(new TextData((String) newTrailerKey));
		params.add(new TextData((String) trailer));
		params.add(new TextData((String) carrier));
		actionProperties.setProcedureParameters(params);
		actionProperties.setProcedureName("nspTrailerNewInstance");

		try {
			WmsWebuiActionsImpl.doAction(actionProperties);
		} catch (Exception e) {
			e.getMessage();
			UserException UsrExcp = new UserException(e.getMessage(), new Object[]{});
			throw UsrExcp;
		}

		state.getRequest().getSession().setAttribute("TRAILERNUMBERAUTO", newTrailerKey);
		return RET_CONTINUE;
	}

	public String dateFormatConvertor(String inputdate)
	{
		/******************************************************************
		 * Programmer:     Sreedhar Kethireddy
		 * Created:        11/21/2010
		 * Purpose:        Convert the SQL Date time to SQL date
		 *******************************************************************
		 * Modification History
		 *********************************************************************************/

		String outdate="";
		SimpleDateFormat sourceFormat = new SimpleDateFormat("dd-MMM-yyyy kk:mm:ss");
		SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sourceFormat.parse(inputdate);
		} catch (ParseException e) {

			e.printStackTrace();
		}
		outdate=dFormat.format(date);

		return outdate;
	}
}
