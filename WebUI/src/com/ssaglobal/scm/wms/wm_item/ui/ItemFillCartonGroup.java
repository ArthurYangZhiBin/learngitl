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
package com.ssaglobal.scm.wms.wm_item.ui;

//Import 3rd party packages and classes

//Import Epiphany packages and classes
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class ItemFillCartonGroup extends FormWidgetExtensionBase{
	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget) throws DPException{
		//Get current owner
		RuntimeFormInterface form = state.getCurrentRuntimeForm();
		DataBean focus = form.getFocus();
		if(focus.isTempBio()){
			QBEBioBean qbe = (QBEBioBean)form.getFocus();
			String owner = qbe.get("STORERKEY")==null ? null : qbe.get("STORERKEY").toString() ;
			
			//Find Owner's Carton Group
			if(owner!=null){
				String query = "SELECT CARTONGROUP FROM STORER WHERE STORERKEY = '"+owner+"'";
				EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
				if(results.getRowCount()>0){
					String cartonGroup = results.getAttribValue(1).getAsString();
					focus.setValue("CARTONGROUP", cartonGroup);	
				}				
			}
		}
		return RET_CONTINUE;
	}
}