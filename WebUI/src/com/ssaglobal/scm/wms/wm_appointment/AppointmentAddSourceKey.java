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
import java.util.ArrayList;
import java.util.HashSet;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.objects.Navigation;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionObjectInterface;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeForm;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.uiextensions.KeyGenBioWrapper;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.StringUtils;

/*
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected to be
 * provided from the meta <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class AppointmentAddSourceKey extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	

	protected static ILoggerCategory _log = LoggerFactory.getInstance(AppointmentAddSourceKey.class);

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
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{

		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		RuntimeFormInterface sourceForm = context.getSourceWidget().getForm();
		String beanType = sourceForm.getFocus().getDataType();
		Query query = new Query(beanType, "", null);
		if (beanType.equals("wm_orders"))
		{
			/*
			 * 5. While adding the Shipment Order to the appointment, if the status of Order is 'Shipped',
			 *  give an error message and do not proceed with appointment creation 
			 */
			// Solution, just filter out shipped complete orders			
			//Food Enhancements - 3PL - Trailer Temperature Capture - Sreedhar Kethireddy  - Nov-12-2010 - start
			//query = new Query(beanType, "wm_orders.STATUS != '95'", null);
			query = new Query(beanType, "wm_orders.STATUS != '95'", "wm_orders.REQUESTEDSHIPDATE desc");
			//Food Enhancements - 3PL - Trailer Temperature Capture - Sreedhar Kethireddy  - Nov-12-2010 - end
		}
		
		//Food Enhancements - 3PL - Trailer Temperature Capture - Sreedhar Kethireddy  - Nov-12-2010 - start
		if (beanType.equals("receipt"))
		{
			/*
			 * 5. While adding the Shipment Order to the appointment, if the status of Order is 'Shipped',
			 *  give an error message and do not proceed with appointment creation 
			 */
			// Solution, just filter out shipped complete orders			
			//defect1129 query = new Query(beanType, null, "receipt.EXPECTEDRECEIPTDATE desc");			
			query = new Query(beanType, "NOT receipt.STATUS IN ('9','11','15','20')" , "receipt.EXPECTEDRECEIPTDATE desc");		//defect 1129
		}
		//Food Enhancements - 3PL - Trailer Temperature Capture - Sreedhar Kethireddy  - Nov-12-2010 - End
		
		BioCollectionBean rs = uow.getBioCollectionBean(query);

		result.setFocus(rs);

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
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
	@Override
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
	{
		try
		{
			boolean showASNInReceiptWarning = false;
			boolean showDiffCarrierWarning = false;
			
			
			
			Navigation navigation = ctx.getNavigation();
			String navName = navigation.getName();
			ActionObjectInterface actionObject = ctx.getActionObject();
			String name2 = actionObject.getName();
			String navType = ctx.getNavType();
			RuntimeFormInterface sourceForm = ctx.getSourceForm();
			String name3 = sourceForm.getName();
			RuntimeFormWidgetInterface sourceWidget = ctx.getSourceWidget();
			String name4 = sourceWidget.getName();
			
			
			
			
			StateInterface state = ctx.getState();
			RuntimeFormInterface appointmentForm = ctx.getSourceForm().getParentForm(state).getParentForm(state);
			DataBean appointmentFocus = appointmentForm.getFocus();

			RuntimeForm listForm = ctx.getModalBodyForm(0);
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			ArrayList selectedItems = ((RuntimeListFormInterface) listForm).getSelectedItems();
			String dataType = listForm.getFocus().getDataType();
			//Pre Check
			HashSet<String> loadids = new HashSet<String>();
			for (int i = 0; i < selectedItems.size(); i++)
			{
				BioBean selected = (BioBean) selectedItems.get(i);

				String apptkey = BioAttributeUtil.getString(selected, "APPOINTMENTKEY");
				if (!StringUtils.isEmpty(apptkey))
				{
					//raise exception
					String sourceKey = null;
					
					if (dataType.equals("receipt"))
					{
						sourceKey = (String) selected.getValue("RECEIPTKEY");
					}
					else
					{
						sourceKey = (String) selected.getValue("ORDERKEY");
					}
					throw new UserException("WMEXP_APPT_EXISTING", new Object[] { sourceKey });
				}
				
				//either all selected records must be on the same load, or no loads
				if (dataType.equals("wm_orders"))
				{
					String loadid = BioAttributeUtil.getString(selected, "LOADID");
					loadids.add(loadid);
				}
			}
			
			if (loadids.size() > 1)
			{
				//throw error
				_log.debug("LOG_DEBUG_EXTENSION_AppointmentAddSourceKey_execute", "LoadIDs " + loadids, SuggestedCategory.NONE);
				throw new UserException("WMEXP_APPT_DIFF_LOAD", new Object[] {});

			}
			
			
			for (int i = 0; i < selectedItems.size(); i++)
			{
				BioBean selected = (BioBean) selectedItems.get(i);
				String sourceKey = null;
				String carrier;
				if (dataType.equals("receipt"))
				{
					sourceKey = (String) selected.getValue("RECEIPTKEY");
					String status = BioAttributeUtil.getString(selected, "STATUS");
					//Partial Receipt/ In Receiving
					/*
					 * 5
					 * 9
					 * 11
					 * 15
					 */
					if ("5".equals(status) || "9".equals(status) || "11".equals(status) || "15".equals(status))
					{
						//display asn warning
						showASNInReceiptWarning = true;
					}
					carrier = BioAttributeUtil.getString(selected, "CARRIERKEY");
				}
				else
				{
					sourceKey = (String) selected.getValue("ORDERKEY");
					carrier = BioAttributeUtil.getString(selected, "CarrierCode");
				}

				if (!StringUtils.isEmpty(carrier))
				{
					String appointmentCarrier = BioAttributeUtil.getString(appointmentFocus, "CARRIER");
					if (!appointmentCarrier.equals(carrier))
					{
						showDiffCarrierWarning = true;
					}
				}

				

				BioBean bio = uow.getNewBio("wm_appointmentdetail");
				bio.setValue("APPOINTMENTKEY", appointmentFocus.getValue("APPOINTMENTKEY"));
				bio.setValue("APPOINTMENTDETAILKEY", new KeyGenBioWrapper().getKey("APPOINTMENTKEYDETAIL"));
				bio.setValue("SOURCETYPE", appointmentFocus.getValue("APPOINTMENTTYPE"));
				bio.setValue("SOURCEKEY", sourceKey);
				bio.save();
			}
			
			//more important
			if (!"closeModalAdd".equals(navName) && showDiffCarrierWarning == true)
			{
				uow.clearState();
				EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
				userContext.put("APPT_WARNING" + state.getInteractionId(), "APPT_CARRIER_WARNING");
				ctx.setNavigation("clickEventConfirm");
				return RET_CONTINUE;
			}

			if (!"closeModalAdd".equals(navName) && showASNInReceiptWarning == true)
			{
				uow.clearState();
				EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
				userContext.put("APPT_WARNING" + state.getInteractionId(), "APPT_ASN_NEW_WARNING");
				ctx.setNavigation("clickEventConfirm");
				return RET_CONTINUE;
			}

			uow.saveUOW(true);

		} catch (RuntimeException e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
