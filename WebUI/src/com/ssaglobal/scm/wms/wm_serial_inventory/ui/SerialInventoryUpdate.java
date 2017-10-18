/**
 * ---Begin Copyright Notice---20090105T1353
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2010 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */
package com.ssaglobal.scm.wms.wm_serial_inventory.ui;

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.error.UnitOfWorkErrorAfterCommitException;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;

/**
 * TODO Document SerialInventoryUpdate class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class SerialInventoryUpdate  extends ActionExtensionBase{
	protected int execute(ActionContext context, ActionResult result)throws UserException, EpiException{
		StateInterface state= null;
		try{
			state = context.getState();			 
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			 
			RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
			RuntimeFormInterface headerForm = toolbar.getParentForm(state);
			
			if(headerForm.isListForm()){
		    	String [] desc = new String[1];
		    	desc[0] = "";
		    	throw new UserException("List_Save_Error",desc);			
			}
			DataBean headerFocus = headerForm.getFocus();
			
			String name = headerForm.getName();
			
			BioBean headerBioBean = null;
			headerBioBean = (BioBean)headerFocus;
			headerBioBean.save();
			 			 
			uow.saveUOW();
			uow.clearState();
		    result.setFocus(headerBioBean);
		}
		//catch(Exception e){
			//e.printStackTrace();
		//}
		catch(UnitOfWorkException ex)
		{
			
				
			Throwable nested = ((UnitOfWorkException) ex).getDeepestNestedException();
			
			if(nested instanceof ServiceObjectException)
			{
				String reasonCode = nested.getMessage();
				//replace terms like Storer and Commodity				
				throwUserException(ex, reasonCode, null);
			}
			else if(nested instanceof UnitOfWorkErrorAfterCommitException)
			{
				String error = nested.getMessage();
				throwUserException(ex, error, null);				
			}
			else
			{
				throwUserException(ex, "ERROR", null);
			}
		
		}	
		return RET_CONTINUE;
	}

}
