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
package com.ssaglobal.scm.wms.wm_shipmentorder.action;

// Import 3rd party packages and classes
import java.util.ArrayList;
import java.util.Iterator;

// Import Epiphany packages and classes
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;

public class SOPickUnpickAction extends ActionExtensionBase
{
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();

		RuntimeFormInterface tempHeaderForm = state.getCurrentRuntimeForm().getParentForm(state);

		RuntimeListFormInterface listForm = null;
		if (tempHeaderForm.isListForm())
		{
			listForm = (RuntimeListFormInterface) tempHeaderForm;
		}
		
		ArrayList items = listForm.getAllSelectedItems();
		if (items == null)
		{
			throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
		}
		
		for (Iterator it = items.iterator(); it.hasNext();)
		{
			BioBean selectedItem = (BioBean) it.next();
			int statusValue = Integer.parseInt(selectedItem.getValue("STATUS").toString());
		
			if ((statusValue >= 5) && (statusValue < 9))
			{
		
				selectedItem.setValue("STATUS", new Integer(0));
		//03/18/2010 FW: Comment out 'uow.saveUOW' and it is moved to the outside of loop for possible memory spick issue (Defect217952) -- Start
				//uow.saveUOW(true);
				//uow.clearState();
			}
		}
		
		uow.saveUOW(true);
		uow.clearState();
		//03/18/2010 FW: Comment out 'uow.saveUOW' and it is moved to the outside of loop for possible memory spick issue (Defect217952) -- End
		
		BioBean headerBioBean = (BioBean) tempHeaderForm.getParentForm(state).getParentForm(state).getFocus();
		result.setFocus(headerBioBean);
		
		return RET_CONTINUE;
	}

	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
	{
		return RET_CONTINUE;
	}
}
