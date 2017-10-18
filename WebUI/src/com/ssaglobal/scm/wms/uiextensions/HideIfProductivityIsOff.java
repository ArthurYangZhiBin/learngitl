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

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeMenuInterface;
import com.epiphany.shr.ui.view.RuntimeMenuItemInterface;

/**
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected
 * to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class HideIfProductivityIsOff extends com.epiphany.shr.ui.view.customization.MenuExtensionBase {

	/**
	 * The code within the execute method will be run on the FormRender.
	 * <P>
	 * 
	 * @param state
	 *            The StateInterface for this extension
	 * @param menu
	 *            the menu that is about to be rendered
	 * @return int RET_CONTINUE, RET_CANCEL
	 */
	@Override
	protected int execute(StateInterface state, RuntimeMenuInterface menu) {

		if (toHide(state) == true) {
			menu.setProperty(RuntimeMenuInterface.PROP_HIDDEN, true);
		} else {
			menu.setProperty(RuntimeMenuInterface.PROP_HIDDEN, false);
		}
		return RET_CONTINUE;
	}

	/**
	 * The code within the execute method will be run on the FormRender. events.
	 * 
	 * @param state
	 *            the state of the user's navigation
	 * @param menuItem
	 *            the menu item that is about to be rendered
	 * @return int RET_CONTINUE, RET_CANCEL
	 */
	@Override
	protected int execute(StateInterface state, RuntimeMenuItemInterface menuItem) {

		if (toHide(state) == true) {
			menuItem.setProperty(RuntimeMenuItemInterface.PROP_HIDDEN, true);
		} else {
			menuItem.setProperty(RuntimeMenuItemInterface.PROP_HIDDEN, false);
		}
		return RET_CONTINUE;
	}

	boolean toHide(StateInterface state) {
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		String qryStmt = "wm_system_settings.CONFIGKEY='MONITORPRODUCTIVITY'";
		Query query = new Query("wm_system_settings", qryStmt, null);
		BioCollectionBean results = uowb.getBioCollectionBean(query);
		try {
			if (results.size() > 0) {
				if (results.elementAt(0).get("NSQLVALUE").toString().equals("0")) {
					return true;
				} else {
					return false;
				}
			} else return true;
		} catch (Exception e) {
			return true;
		}
	}

}
