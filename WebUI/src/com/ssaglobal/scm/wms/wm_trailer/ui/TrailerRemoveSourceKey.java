package com.ssaglobal.scm.wms.wm_trailer.ui;


//Import 3rd party packages and classes

//Import Epiphany packages and classes
import java.util.ArrayList;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;

/*
* Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected to be
* provided from the meta <P>
* 
* @return int RET_CONTINUE, RET_CANCEL
*/

public class TrailerRemoveSourceKey extends com.epiphany.shr.ui.action.ActionExtensionBase
{

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
		//RuntimeFormInterface detailForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_trailer_detail_view", state);
		RuntimeFormInterface trailerDetailForm = state.getCurrentRuntimeForm().getParentForm(state);
		RuntimeListFormInterface listForm = (RuntimeListFormInterface) trailerDetailForm;

		//ArrayList keys = new ArrayList();

		ArrayList<BioBean> selectedItems = (listForm).getSelectedItems();
		String dataType = listForm.getFocus().getDataType();
		for (int i = 0; i < selectedItems.size(); i++)
		{
			BioBean selectedItem = selectedItems.get(i);
			String sourceKey = null;
			if (dataType.equals("receipt"))
			{
				
				sourceKey = (String) selectedItem.getValue("RECEIPTKEY");
				WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				Array params = new Array();
				params.add(new TextData((String) "RECEIPTKEY"));
				params.add(new TextData((String) sourceKey));
				params.add(new TextData((String) ""));
				params.add(new TextData((String) ""));
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
				sourceKey = (String) selectedItem.getValue("ORDERKEY");
				
				WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				Array params = new Array();
				params.add(new TextData((String) "ORDERKEY"));
				params.add(new TextData((String) sourceKey));
				params.add(new TextData((String) ""));
				params.add(new TextData((String) ""));
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
			// Add your code here to process the event

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
