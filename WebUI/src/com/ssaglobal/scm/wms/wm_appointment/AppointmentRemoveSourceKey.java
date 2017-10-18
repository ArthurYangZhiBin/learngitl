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

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.util.FormUtil;

/*
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected to be
 * provided from the meta <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class AppointmentRemoveSourceKey extends com.epiphany.shr.ui.action.ActionExtensionBase
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
		RuntimeFormInterface detailForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_appointment_detail_view", state);
		RuntimeFormInterface appointmentDetailForm = state.getCurrentRuntimeForm().getParentForm(state);
		RuntimeListFormInterface listForm = (RuntimeListFormInterface) appointmentDetailForm;

		ArrayList keys = new ArrayList();

		ArrayList<BioBean> selectedItems = (listForm).getSelectedItems();
		String dataType = listForm.getFocus().getDataType();
		for (int i = 0; i < selectedItems.size(); i++)
		{
			BioBean selectedItem = selectedItems.get(i);
			if (dataType.equals("receipt"))
			{
				keys.add(selectedItem.getValue("RECEIPTKEY"));
			}
			else
			{
				keys.add(selectedItem.getValue("ORDERKEY"));
			}
		}

		if (keys.size() > 0)
		{
			//Delete from APPOINTMENTDETAIL
			DataBean detailFocus = detailForm.getFocus();
			String appointmentKey = (String) detailFocus.getValue("APPOINTMENTKEY");
			//build query 
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			String query = "wm_appointmentdetail.APPOINTMENTKEY = '" + appointmentKey + "' AND ( ";
			for (int i = 0; i < keys.size(); i++)
			{
				if (i > 0)
				{
					query += " OR ";
				}
				query += "wm_appointmentdetail.SOURCEKEY = '" + keys.get(i) + "'";
			}
			query += " ) ";

			BioCollectionBean rs = uow.getBioCollectionBean(new Query("wm_appointmentdetail", query, null));
			for (int i = 0; i < rs.size(); i++)
			{
				BioBean appDetail = rs.get("" + i);

				appDetail.delete();
			}
			uow.saveUOW(false);
		}

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
