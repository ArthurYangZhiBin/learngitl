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
package com.ssaglobal.scm.wms.wm_shipmentorder.ui;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeMenuInterface;
import com.epiphany.shr.ui.view.RuntimeMenuItemInterface;
import com.epiphany.shr.ui.view.customization.MenuExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;

public class PreRenderShipmentOrderActions extends MenuExtensionBase{
	public PreRenderShipmentOrderActions(){
	}
	
	@Override
	protected int execute(StateInterface state, RuntimeMenuItemInterface menuItem) throws EpiException{
		//Get Monitor Labor value	
		if(menuItem.getName().equals("Allocate") || menuItem.getName().equals("Release") || menuItem.getName().equals("AllocateFT") || menuItem.getName().equals("ReleaseFT")){
			DataBean focus = null;
			if(menuItem.getName().equals("Allocate") || menuItem.getName().equals("Release") ){
				focus = state.getRuntimeForm(state.getCurrentRuntimeForm().getParentForm(state).getSubSlot("list_slot_1"), null).getFocus();	
			}else{
				focus = state.getRuntimeForm(state.getCurrentRuntimeForm().getParentForm(state).getSubSlot("slot1"), null).getFocus();
			}
			
			if(focus.isBioCollection() || focus.isTempBio()){
				menuItem.setBooleanProperty(RuntimeMenuInterface.PROP_HIDDEN, true);
			}else{
				BioBean bio = (BioBean) focus;
				String status = bio.get("STATUS").toString();
				if(status.equalsIgnoreCase("95")){
					menuItem.setBooleanProperty(RuntimeMenuInterface.PROP_HIDDEN, true);
				}else{
					menuItem.setBooleanProperty(RuntimeMenuInterface.PROP_HIDDEN, false);					
				}
			}
		}else{
			Query mlQuery = new Query("wm_system_settings", "wm_system_settings.CONFIGKEY = 'MONITORPRODUCTIVITY'", null);
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			BioCollectionBean bioColl = uow.getBioCollectionBean(mlQuery);
			
			//Disable/Enable menu item if necessary
			if(bioColl.get("0").get("NSQLVALUE").equals("0")){
				menuItem.setProperty(RuntimeMenuItemInterface.PROP_HIDDEN, Boolean.TRUE);		
			}
		}
		return RET_CONTINUE;
	}
}