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

package com.ssaglobal.scm.wms.flowThru.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class FlowThruApportionmentResequenceAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(FlowThruApportionmentResequenceAction.class);
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
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		
		StateInterface state = context.getState();
		RuntimeListFormInterface listForm = (RuntimeListFormInterface) FormUtil.findForm(
																							state.getCurrentRuntimeForm(),
																							"wm_list_shell_apportionment_rules",
																							"wm_apportionment_rules_detail_list_view",
																							state);
		
		if(listForm == null)
		{
			throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
		}
		ArrayList items = listForm.getAllSelectedItems();
		if (items == null)
		{
			throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
		}
		else if (items.size() > 1)
		{
			throw new UserException("WMEXP_MORE_THAN_ONE_SELECTED", new Object[] {});
		}
		BioBean selected = (BioBean) items.get(0);
		result.setFocus(selected);

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
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
	{
		
		StateInterface state = ctx.getState();
		RuntimeFormInterface modalForm = ctx.getModalBodyForm(0);
		
		int newSequence = Integer.valueOf((String) modalForm.getFormWidgetByName("NEWSEQUENCE").getValue()).intValue();
		

		//

		RuntimeFormInterface ftaHeader = retrieveHeaderForm(ctx, state);
		//retrieve Header Storer Value
		String headerStorerValue = (String) ftaHeader.getFocus().getValue("STORERKEY");
		headerStorerValue = headerStorerValue == null ? null : headerStorerValue.toUpperCase();
		//query based on Storer
		String query = "wm_apportionstrategy.STORERKEY = '" + headerStorerValue + "'";
		Query detailQuery = new Query("wm_apportionstrategy", query, "wm_apportionstrategy.SEQUENCE ASC");
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		BioCollectionBean detailCollection = uowb.getBioCollectionBean(detailQuery);

		//fix new sequence number
		if (newSequence > detailCollection.size())
		{
			newSequence = detailCollection.size();
		}
		else if (newSequence <= 0)
		{
			newSequence = 1;
		}

		//Update selected record
		String selectedConsigneeKey = (String) modalForm.getFocus().getValue("CONSIGNEEKEY");
		selectedConsigneeKey = selectedConsigneeKey == null ? null : selectedConsigneeKey.toUpperCase();
		int selectedPreviousSequence = Integer.valueOf(modalForm.getFocus().getValue("SEQUENCE").toString()).intValue();
		if (newSequence == selectedPreviousSequence)
		{
			//Return if No Updates are required
			return RET_CONTINUE;
		}
		String updateSelectedQuery = "UPDATE apportionstrategy  SET SEQUENCE = " + (newSequence)
				+ " WHERE (((STORERKEY = '" + headerStorerValue + "') AND (CONSIGNEEKEY = '" + selectedConsigneeKey
				+ "')) AND (SEQUENCE = " + selectedPreviousSequence + "))";
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(updateSelectedQuery);

		//Requery
		detailCollection = uowb.getBioCollectionBean(detailQuery);

		//reorder but skip the selected record
		int counter = 1;
		for (int i = 0; i < detailCollection.size(); i++)
		{
			//prevent duplicates
			if (counter == newSequence)
			{
				counter++;
			}
			Object previousSequence = detailCollection.get(String.valueOf(i)).getValue("SEQUENCE");
			String consigneeKey = (String) detailCollection.get(String.valueOf(i)).getValue("CONSIGNEEKEY");
			consigneeKey = consigneeKey == null ? null : consigneeKey.toUpperCase();
			if (consigneeKey.equals(selectedConsigneeKey))
			{
				continue;
			}
			String updateQuery = "UPDATE apportionstrategy  SET SEQUENCE = " + (counter) + " WHERE (((STORERKEY = '"
					+ headerStorerValue + "') AND (CONSIGNEEKEY = '" + consigneeKey + "')) AND (SEQUENCE = "
					+ previousSequence + "))";
			results = WmsWebuiValidationSelectImpl.select(updateQuery);
			counter++;

		}

		return RET_CONTINUE;
	}

	private RuntimeFormInterface retrieveHeaderForm(ModalActionContext ctx, StateInterface state)
	{
		RuntimeFormInterface shellToolbar = ctx.getSourceForm();
		RuntimeFormInterface ftaHeader = FormUtil.findForm(shellToolbar, "wm_list_shell_apportionment_rules",
															"wm_apportionment_rules_header_view", state);
		return ftaHeader;
	}

}
