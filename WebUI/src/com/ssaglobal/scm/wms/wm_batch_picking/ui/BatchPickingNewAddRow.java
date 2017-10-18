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
package com.ssaglobal.scm.wms.wm_batch_picking.ui;

import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;

public class BatchPickingNewAddRow extends FormExtensionBase{
	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form){
		RuntimeFormWidgetInterface source = context.getSourceWidget();
		UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();
		if(source!=null){
			String sourceName = source.getName();
			String sourceForm = source.getForm().getName();
			if(sourceName.equals("NEW") && sourceForm.equals("wm_batchpickingdetail_toggle_view Toolbar")){
				form.setProperty("quick-add row", "bottom");
				QBEBioBean newBio;
				try{
					 newBio = uowb.getQBEBioWithDefaults("wm_wavedetail");
					 form.setQuickAddRowBean(newBio);
				}catch(DataBeanException dbe){
				}

			}
		}
		return RET_CONTINUE;
	}
}