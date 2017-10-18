package com.ssaglobal.scm.wms.wm_trailer.ui;


//Import 3rd party packages and classes

//Import Epiphany packages and classes
import java.util.ArrayList;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeForm;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;

/*
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected to be
 * provided from the meta <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class TrailerAddSourceKey extends com.epiphany.shr.ui.action.ActionExtensionBase
{


	protected static ILoggerCategory _log = LoggerFactory.getInstance(TrailerAddSourceKey.class);

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
			query = new Query(beanType, "wm_orders.STATUS != '95'", "wm_orders.REQUESTEDSHIPDATE desc");
		}
		if (beanType.equals("receipt"))
		{
			/*
			 * 5. While adding the Shipment Order to the appointment, if the status of Order is 'Shipped',
			 *  give an error message and do not proceed with appointment creation 
			 */
			// Solution, just filter out shipped complete orders
			query = new Query(beanType, null, "receipt.EXPECTEDRECEIPTDATE desc");
		}

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
			StateInterface state = ctx.getState();
			RuntimeFormInterface trailerForm = ctx.getSourceForm().getParentForm(state).getParentForm(state);
			DataBean trailerFocus = trailerForm.getFocus();
			String trailerKey = (trailerFocus.getValue("TRAILERKEY")).toString();
			String carrier = trailerFocus.getValue("CARRIERCODE").toString();
			RuntimeForm listForm = ctx.getModalBodyForm(0);
			ArrayList selectedItems = ((RuntimeListFormInterface) listForm).getSelectedItems();
			String dataType = listForm.getFocus().getDataType();
			//Pre Check

			for (int i = 0; i < selectedItems.size(); i++)
			{
				BioBean selected = (BioBean) selectedItems.get(i);
				String sourceKey = null;

				if (dataType.equals("receipt"))
				{
					sourceKey = (String) selected.getValue("RECEIPTKEY");
					WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
					Array params = new Array();
					params.add(new TextData((String) "RECEIPTKEY"));
					params.add(new TextData((String) sourceKey));
					params.add(new TextData((String) carrier));
					params.add(new TextData((String) trailerKey));
					actionProperties.setProcedureParameters(params);
					actionProperties.setProcedureName("nspCarrierUpdate");

					try {
						WmsWebuiActionsImpl.doAction(actionProperties);
					} catch (Exception e) {
						e.getMessage();
						UserException UsrExcp = new UserException(e.getMessage(), new Object[]{});
						throw UsrExcp;
					}
				}
				else
				{
					sourceKey = (String) selected.getValue("ORDERKEY");
					WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
					Array params = new Array();
					params.add(new TextData((String) "ORDERKEY"));
					params.add(new TextData((String) sourceKey));
					params.add(new TextData((String) carrier));
					params.add(new TextData((String) trailerKey));
					actionProperties.setProcedureParameters(params);
					actionProperties.setProcedureName("nspCarrierUpdate");

					try {
						WmsWebuiActionsImpl.doAction(actionProperties);
					} catch (Exception e) {
						e.getMessage();
						UserException UsrExcp = new UserException(e.getMessage(), new Object[]{});
						throw UsrExcp;
					}
				}

			}

		} 


		catch (RuntimeException e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
