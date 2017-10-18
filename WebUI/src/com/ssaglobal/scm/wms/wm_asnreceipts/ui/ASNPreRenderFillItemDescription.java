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

//Import 3rd party packages and classes

//Import Epiphany packages and classes
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class ASNPreRenderFillItemDescription extends FormWidgetExtensionBase{
	public int execute(StateInterface state, RuntimeFormWidgetInterface widget) throws EpiDataException{
		RuntimeFormInterface form = state.getCurrentRuntimeForm();
		DataBean focus = form.getFocus();
		if(focus.isTempBio()){
			String item = form.getFormWidgetByName("SKU").getDisplayValue();
			if(item!=null){
				String qry = "SELECT DESCR FROM SKU WHERE SKU='"+item+"'";
				EXEDataObject edoResults = WmsWebuiValidationSelectImpl.select(qry);
				if(edoResults.getCount()==1){
					String temp = edoResults.getAttribValue(1).getAsString();
					widget.setDisplayValue(temp);
				}else{
					widget.setDisplayValue("");
				}
			}
		}
		return RET_CONTINUE;
	}
}