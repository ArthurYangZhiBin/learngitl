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
package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

import java.util.ArrayList;
import java.util.Iterator;

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.metadata.objects.FormSlot;
import com.epiphany.shr.metadata.objects.ScreenSlot;
import com.epiphany.shr.metadata.objects.TabIdentifier;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class PutAwaySaveAction extends ListSelector
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
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PutAwaySaveAction.class);
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
//		_log.debug("LOG_SYSTEM_OUT","\n\t" + "CALLING WEBUILISTSAVEACTION" + "\n",100L);
		//ListSaveAction test;
		RuntimeFormInterface currentRuntimeForm = context.getState().getCurrentRuntimeForm();
		RuntimeFormInterface parentForm = currentRuntimeForm.getParentForm(context.getState());
		RuntimeListFormInterface headerListForm = (RuntimeListFormInterface)parentForm;
		headerListForm.getFocus();
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		//SRG: Defect# 647 -- Start
		String toLoc = " ";
		BioCollectionBean taskList = (BioCollectionBean)headerListForm.getFocus();
		for(int index=0; index<taskList.size(); index++){
			toLoc = taskList.elementAt(index).get("TOLOC").toString().toUpperCase();
			taskList.elementAt(index).set("TOLOC", toLoc);
		}		
		//SRG: Defect# 647 -- End
		try{
			uowb.saveUOW(true);
		}catch (UnitOfWorkException e){
			_log.debug("LOG_SYSTEM_OUT","\n\t" + "IN UnitOfWorkException" + "\n",100L);
			
			Throwable nested = ((UnitOfWorkException) e).getDeepestNestedException();
			_log.debug("LOG_SYSTEM_OUT","\tNested " + nested.getClass().getName(),100L);
			_log.debug("LOG_SYSTEM_OUT","\tMessage " + nested.getMessage(),100L);
			
			if(nested instanceof ServiceObjectException)
			{
				String reasonCode = nested.getMessage();
				//replace terms like Storer and Commodity
				
				throwUserException(e, reasonCode, null);
			}

		}

		
/*		ArrayList listForms = new ArrayList();
		if (selectedItems != null && selectedItems.size() > 0)
		{
			Iterator bioBeanIter = selectedItems.iterator();
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			try
			{
				BioBean bio;
				for (; bioBeanIter.hasNext(); )
					bio = (BioBean) bioBeanIter.next();
//				_log.debug("LOG_SYSTEM_OUT","\n\t" + "BEFORE SAVE" + "\n",100L);
				
				if (listForms.size() <= 0)
					listForms = (ArrayList) getTempSpaceHash().get("SELECTED_LIST_FORMS");
				clearBuckets(listForms);
				result.setSelectedItems(null);
			} catch (UnitOfWorkException ex)
			{
				_log.debug("LOG_SYSTEM_OUT","\n\t" + "IN UnitOfWorkException" + "\n",100L);
				
				Throwable nested = ((UnitOfWorkException) ex).getDeepestNestedException();
				_log.debug("LOG_SYSTEM_OUT","\tNested " + nested.getClass().getName(),100L);
				_log.debug("LOG_SYSTEM_OUT","\tMessage " + nested.getMessage(),100L);
				
				if(nested instanceof ServiceObjectException)
				{
					String reasonCode = nested.getMessage();
					throwUserException(ex, reasonCode, null);
				}
				
			} catch (EpiException ex)
			{
				_log.debug("LOG_SYSTEM_OUT","\n\t" + "IN EPIEXCEPTION" + "\n",100L);
				throwUserException(ex, "ERROR_SAVEING_BIO", null);
			}
		} */
		return 0;
	}
}
