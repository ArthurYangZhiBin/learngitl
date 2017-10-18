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
package com.ssaglobal.scm.wms.wm_taskdispatch.ui;

import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;

public class StepNumberGenerator extends FormExtensionBase{
	@Override
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiDataException{
		DataBean focus = form.getFocus();
		if(focus.isTempBio()){
			StateInterface state = context.getState();
			String max = null;
			QBEBioBean qbe = (QBEBioBean)focus;
			String toggleFormName = getParameterString("toggleFormName");
			String toggleFormSlot = getParameterString("toggleFormSlot");
			String field = getParameterString("fieldName");
			String tabID = getParameterString("tabID");
			RuntimeFormInterface parentForm = form.getParentForm(state);
			if(parentForm.getName().equals(toggleFormName)){
				SlotInterface slot = parentForm.getSubSlot(toggleFormSlot);
				BioCollectionBean listFocus = (BioCollectionBean)state.getRuntimeForm(slot, tabID).getFocus();
				max = findMax(listFocus, field);
			}else{
				max="10";
			}
			qbe.setValue(field , max);
		}
		return RET_CONTINUE;
	}
	
	private String findMax(BioCollectionBean focus, String field) throws EpiDataException{
		long temp = 0, longValue;
		for(int i=0; i<focus.size(); i++){
			Object value = focus.elementAt(i).get(field);
			if (value == null) {
				continue;
			}
			longValue = Long.parseLong(value.toString());
			if(longValue>temp){
				temp = longValue;
			}
		}
		temp = temp+10;
		return String.valueOf(temp);
	}
}