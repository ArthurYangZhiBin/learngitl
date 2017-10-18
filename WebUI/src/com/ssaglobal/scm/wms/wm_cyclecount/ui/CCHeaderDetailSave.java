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
package com.ssaglobal.scm.wms.wm_cyclecount.ui;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.metadata.interfaces.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.util.exceptions.EpiException;

public class CCHeaderDetailSave extends ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CCHeaderDetailSave.class);

	public CCHeaderDetailSave()
	{
		_log.info("EXP_1", "CCHeaderDetailSave Instantiated!!!", SuggestedCategory.NONE);
	}

	protected int execute(ActionContext context, ActionResult result) throws UserException, EpiException
	{
		_log.debug("LOG_SYSTEM_OUT","it is in CCHeaderDetailSave******DDDDkkkkkkkkkkkvvvvvv",100L);
		//Get user entered criteria
		String shellSlot1 = "list_slot_1"; //(String) getParameter("shellSlot1");
		String shellSlot2 = "list_slot_2"; //(String) getParameter("shellSlot2");
		String detailBiocollection = "CCDETAIL"; //(String) getParameter("detailBiocollection");
		_log.debug("LOG_SYSTEM_OUT","********shellSlot1=" + shellSlot1,100L);
		_log.debug("LOG_SYSTEM_OUT","********shellSlot2=" + shellSlot2,100L);
		_log.debug("LOG_SYSTEM_OUT","********detailBiocollection=" + detailBiocollection,100L);
		

		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();

		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);

		//get header data
		SlotInterface headerSlot = shellForm.getSubSlot(shellSlot1); 
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		_log.debug("LOG_SYSTEM_OUT","!@# " + headerForm.getName(),100L);
		if (headerForm.isListForm())
		{
			String[] desc = new String[1];
			desc[0] = "";
			throw new UserException("List_Save_Error", desc);
		}
		DataBean headerFocus = headerForm.getFocus();

		//get detail data
		SlotInterface detailSlot = shellForm.getSubSlot(shellSlot2);
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
		_log.debug("LOG_SYSTEM_OUT","!@# " + detailForm.getName(),100L);
		DataBean detailFocus = detailForm.getFocus();

		
		//Saving Record
		BioBean headerBioBean = null;
		if (headerFocus.isTempBio())
		{
			//it is for insert header
			_log.debug("LOG_SYSTEM_OUT","inserting header & detail ******",100L);
			headerBioBean = uow.getNewBio((QBEBioBean) headerFocus);

			headerBioBean.addBioCollectionLink(detailBiocollection, (QBEBioBean) detailFocus);
		}
		else
		{
			//it is for update header
			_log.debug("LOG_SYSTEM_OUT","updating header ******",100L);
			headerBioBean = (BioBean) headerFocus;

			_log.debug("LOG_SYSTEM_OUT","*****detaiFocus is tempbio=" + detailFocus.isTempBio(),100L);
			if (detailFocus != null && detailFocus.isTempBio())
			{
				_log.debug("LOG_SYSTEM_OUT","inserting detail ******",100L);
				headerBioBean.addBioCollectionLink(detailBiocollection, (QBEBioBean) detailFocus); 
			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","updating detail ******",100L);
			}

		}

		uow.saveUOW(true);
		uow.clearState();
		result.setFocus(headerBioBean);

		return RET_CONTINUE;

	}
}
