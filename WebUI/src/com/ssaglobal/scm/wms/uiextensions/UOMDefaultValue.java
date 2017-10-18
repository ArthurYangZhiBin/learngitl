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
package com.ssaglobal.scm.wms.uiextensions;

// Import 3rd party packages and classes
import java.util.List;

// Import Epiphany packages and classes
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean; //04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
import com.ssaglobal.scm.wms.util.UOMMappingUtil; //AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440

public class UOMDefaultValue extends ActionExtensionBase {
	
	public UOMDefaultValue() {
	}
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException {
		
		//retrieve value of the dropdown attribute
		StateInterface state = context.getState();
		String dropdownName = getParameterString("DROPDOWN");
		//AW 04/14/2009 start Machine#:2093019 SDIS:SCM-00000-05440
		String pack = context.getSourceWidget().getDisplayValue();		
		if(pack == null) 
		pack = UOMMappingUtil.PACK_STD; 
		//AW end 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440

		return fillDropdown(state, dropdownName, UOMMappingUtil.PACK_STD); //AW Machine#:2093019 SDIS:SCM-00000-05440 addedin 
	}

	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException{
		try{
			// Add your code here to process the event
		} catch (Exception e) {
			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
	/*
	 * 04/14/2009	AW	Machine#:2093019 SDIS:SCM-00000-05440
	 * 			Changed method signature. Added in pack
	 * 
	 * 
	 */
	public static int fillDropdown(StateInterface state, String dropdownName, String packVal)throws EpiException{
		
		DataBean formFocus = state.getFocus();
		RuntimeFormInterface form = state.getCurrentRuntimeForm();
		
		//AW start Machine#:2093019 SDIS:SCM-00000-05440
		UnitOfWorkBean uow =state.getDefaultUnitOfWork();
		String packuom3 = UOMMappingUtil.getPACKUOM3Val(packVal,uow); 
		//AW end Machine#:2093019 SDIS:SCM-00000-05440

		//retrieve dropdown contents
		RuntimeFormWidgetInterface dropdownWidget = form.getFormWidgetByName(dropdownName);
		String currentValue = formFocus.getValue(dropdownName) == null ? null : formFocus.getValue(dropdownName).toString(); 

		//get dropdown's current values
		List[] labelsAndValues = null;
		try	{
			labelsAndValues = dropdownWidget.getDropdownContents().getValuesAndLabels(dropdownWidget.getDropdownContext());
			int eaLocation = Integer.MIN_VALUE;
			int currentLocation = Integer.MIN_VALUE;
			for(int i = 0; i < labelsAndValues[1].size(); i++) {
				//if value equal current value mark index
				if(labelsAndValues[1].get(i).toString().equalsIgnoreCase(currentValue)) { 
					currentLocation = i;
				}
				
				//if value equals EA
				//remember position for setting the default value
				//AW Machine#:2093019 SDIS:SCM-00000-05440 edited
				if(labelsAndValues[1].get(i).toString().equalsIgnoreCase(packuom3)){
					eaLocation = i;
				}
			}
			if(currentLocation != Integer.MIN_VALUE) {
				formFocus.setValue(dropdownName, labelsAndValues[1].get(currentLocation));				
			}else {
				formFocus.setValue(dropdownName, labelsAndValues[1].get(eaLocation));
			} 
		} catch (Exception e) {
			e.printStackTrace();
			return RET_CANCEL;
		}
		return RET_CONTINUE;
	}
}
