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
package com.ssaglobal.scm.wms.wm_invoice_processing.action;

import java.util.ArrayList;
import java.util.Iterator;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.DataValue;
import com.agileitp.forte.framework.TextData;
import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
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
import com.epiphany.shr.ui.view.RuntimeListRowInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.wm_invoice_processing.ui.EnableActionButtonOnRunBillling;

public class CancelBillingAction  extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CancelBillingAction.class);
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
	  	 _log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Executing CancelBillingAction- Non Modal",100L);
		return RET_CONTINUE;	
	}

	
	
	
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
	{
		StateInterface state = ctx.getState();
	  	 _log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Executing CancelBillingAction- Modal",100L);

			state.closeModal(true);
			RuntimeFormInterface modalForm = state.getCurrentRuntimeForm();
			RuntimeFormInterface toolbarForm = ctx.getSourceForm();
			RuntimeFormInterface shellForm = toolbarForm.getParentForm(state);
			
			SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
	
			RuntimeFormInterface testForm =state.getRuntimeForm(headerSlot, null);
			
			if(testForm.isListForm())
			{
			SlotInterface listSlot = testForm.getSubSlot("list_slot");
			RuntimeListFormInterface listForm = (RuntimeListFormInterface)state.getRuntimeForm(listSlot, null);
			
			if(listForm.getFocus() instanceof BioCollection)
			{
			BioCollection bioColl = (BioCollection)listForm.getFocus();
						
			String val = "" +bioColl.size();
			
			//setting CANCELCOUNT
			//ctx.getServiceManager().getUserContext().put("CANCELCOUNT", val);
			}
			
			if(state.getServiceManager().getUserContext().containsKey("CANCELCOUNT"))
			{
				//_log.debug("LOG_SYSTEM_OUT","\n\n&&&&Checking count " +state.getServiceManager().getUserContext().get("InvoiceTot").toString(),100L);
			}
			}
		
			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
			Array parms = new Array(); 
			parms.add(new TextData(""));
			parms.add(new TextData(""));
			parms.add(new TextData(""));
			parms.add(new TextData(""));
			parms.add(new TextData(""));
			parms.add(new TextData(""));
			parms.add(new TextData("CancelRun"));
			
			actionProperties.setProcedureParameters(parms);
			actionProperties.setProcedureName("NSPBILLINGRUNWRAPPER");
			try {
				EXEDataObject obj = WmsWebuiActionsImpl.doAction(actionProperties);
				//displayResults(obj);
				//String val = (String)state.getServiceManager().getUserContext().get("CANCELMSG");
				//_log.debug("LOG_SYSTEM_OUT","\n\n *******after cancel billing val: " +val,100L);
			} catch (Exception e) {
				e.getMessage();			
				throw new UserException(e.getMessage(), new Object[]{});
			}
			
		  	 _log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Exiting CancelBillingAction- Non Modal",100L);
			return RET_CONTINUE;
		}
	
	private void displayResults(EXEDataObject results)
	{
		//_log.debug("LOG_SYSTEM_OUT","\n\t" + results.getRowCount() + " x " + results.getColumnCount() + "\n",100L);
		if (results.getColumnCount() != 0)
		{

			_log.debug("LOG_SYSTEM_OUT","---Results",100L);
			for (int i = 1; i < results.getColumnCount() + 1; i++)
			{
				try
				{
					_log.debug("LOG_SYSTEM_OUT"," " + i + " @ " + results.getAttribute(i).name + " " + results.getAttribute(i).value.getAsString(),100L);
					//System.out.println(" " + i + " @ " + results.getAttribute(i).name + " "
					//		+ results.getAttribute(i).value.getAsString());
				} catch (Exception e)
				{
					_log.debug("LOG_SYSTEM_OUT",e.getMessage(),100L);
				}
			}
			_log.debug("LOG_SYSTEM_OUT","----------",100L);
		}
	}

}
