package com.ssaglobal.scm.wms.wm_cyclecount.ui;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;

public class SetEditable extends com.epiphany.shr.ui.action.ActionExtensionBase {

	protected int execute(ActionContext context, ActionResult result)
	throws EpiException
	{
	
		StateInterface state = null;
		DataBean currentFormFocus = null;
		RuntimeFormInterface headerForm = null;
		DataBean headerFormFocus = null;
		//String ERROR_MSG = null;
	
		try
		{
			// Get handle on the form
			state = context.getState();
			currentFormFocus = state.getFocus();
		
			RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
			RuntimeFormInterface shellForm = toolbar.getParentForm(state);	
		
		    if (shellForm.getName().equals("wms_list_shell"))
		    {			
		    	SlotInterface headerSlot = shellForm.getSubSlot("list_slot_2");
		    	headerForm = state.getRuntimeForm(headerSlot, null);
		    	headerFormFocus = state.getRuntimeForm(headerSlot, null).getFocus();	
		    }
		    
		    RuntimeFormWidgetInterface isAllowActiveCountCheckBox = headerForm.getFormWidgetByName("ISALLOWACTIVECOUNT");
		    RuntimeFormWidgetInterface isActiveCheckBox = headerForm.getFormWidgetByName("ISACTIVE");
		    RuntimeFormWidgetInterface starttimeInput = headerForm.getFormWidgetByName("STARTTIME");
		    RuntimeFormWidgetInterface endtimeInput = headerForm.getFormWidgetByName("ENDTIME");
		 
		
			if (headerFormFocus instanceof BioBean)
			{
				headerFormFocus = (BioBean) headerFormFocus;
			}
			
			
			if(isAllowActiveCountCheckBox.getValue().toString().equals("1"))
			{
				isActiveCheckBox.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				starttimeInput.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				endtimeInput.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			}else {	
				isActiveCheckBox.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				starttimeInput.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				endtimeInput.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			}
			return RET_CONTINUE;
		} catch (Exception e) {
			e.printStackTrace();
			return RET_CANCEL;
		}
	}
	
}
