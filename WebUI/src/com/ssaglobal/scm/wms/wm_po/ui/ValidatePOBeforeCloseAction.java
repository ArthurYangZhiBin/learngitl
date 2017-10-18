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
package com.ssaglobal.scm.wms.wm_po.ui;

import java.util.ArrayList;
import java.util.Iterator;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class ValidatePOBeforeCloseAction extends ListSelector {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidatePOBeforeCloseAction.class);

	protected int execute( ActionContext context, ActionResult result ) throws EpiException {
	StateInterface state = context.getState();
	RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();    //gets the toolbar
	RuntimeFormInterface shellForm = toolbar.getParentForm(state);
	_log.debug("LOG_SYSTEM_OUT","headerForm"+ shellForm.getName(),100L);
	SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");		//HC
	RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
	DataBean headerFocus = headerForm.getFocus();
	if (headerFocus instanceof BioCollection){
		_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Close PO Action \n\n",100L);
		//Executes SP name:NSPPOCLOSE params:POKEY
		RuntimeListFormInterface listviewForm = (RuntimeListFormInterface)headerForm;

		ArrayList items;
		try
		{
			items = listviewForm.getAllSelectedItems();
			if (isZero(items))
			{
				_log.debug("LOG_SYSTEM_OUT","\n\t" + "NOTHING SELECTED" + "\n",100L);
				throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
			}
		} catch (NullPointerException e)
		{
			throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
		}
		if(items != null && items.size() > 0)
		{
			_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Action2 \n\n",100L);
			Iterator bioBeanIter = items.iterator();
			try
			{
				BioBean bio;
				for(; bioBeanIter.hasNext();){
					_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Action3 \n\n",100L);
					bio = (BioBean)bioBeanIter.next();
					String POKey = bio.getString("POKEY");
					_log.debug("LOG_SYSTEM_OUT","\n\nPOKEY in list :"+POKey+"\n\n",100L);
					if (bio.get("STATUS").toString().equalsIgnoreCase("11")){
						String[] ErrorParem = new String[1];
						ErrorParem[0]=POKey;
						UserException UsrExcp = new UserException("WMEXP_STATUSCLOSE_02", ErrorParem);
			 	   		throw UsrExcp;
					}
				}
			}
			catch(EpiException ex)
			{
				throwUserException(ex, "ERROR_EXECUTING_ACTION", null);
			}
		}
	}else{
		callActionFromNormalForm(headerFocus);
	}
	
	
	
	_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Action10 \n\n",100L);
	return RET_CONTINUE;
}	
private void callActionFromNormalForm(DataBean headerFocus)throws EpiException {
	if (headerFocus instanceof BioBean){
			headerFocus = (BioBean)headerFocus;
			String POKey = headerFocus.getValue("POKEY").toString();
			_log.debug("LOG_SYSTEM_OUT","\n POKEY in form:"+POKey+"\n\n",100L);
			if (headerFocus.getValue("STATUS").toString().equalsIgnoreCase("11")){
				String[] ErrorParem = new String[1];
				ErrorParem[0]=POKey;
				UserException UsrExcp = new UserException("WMEXP_STATUSCLOSE_02", ErrorParem);
	 	   		throw UsrExcp;
			}
	}
}
boolean isZero(Object attributeValue)
{
	if (attributeValue == null)
	{
		return true;
	}
	else if (((ArrayList) attributeValue).size() == 0)
	{
		return false;
	}
	else
	{
		return false;
	}
}
}
