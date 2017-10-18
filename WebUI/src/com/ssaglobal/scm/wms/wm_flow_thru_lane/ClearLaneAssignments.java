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


package com.ssaglobal.scm.wms.wm_flow_thru_lane;

//Import 3rd party packages and classes

//Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ClearLaneAssignments extends com.epiphany.shr.ui.action.ActionExtensionBase {
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ClearLaneAssignments.class);
	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @exception EpiException
	 */
	protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		_log.debug("LOG_DEBUG_EXTENSION_CLRLANEASSIGN","Executing ClearLaneAssignments",100L);			
		StateInterface state = context.getState();			
		RuntimeListFormInterface listForm = (RuntimeListFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_flow_thru_lane_list_view",state);
		if(listForm == null || listForm.getSelectedItems() == null || listForm.getSelectedItems().size() == 0){
			_log.debug("LOG_DEBUG_EXTENSION_CLRLANEASSIGN","Nothing Selected...",100L);						
			String args[] = new String[0];							
			String errorMsg = getTextMessage("WMEXP_NOTHING_SELECTED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		ArrayList selectedItems = listForm.getSelectedItems();
		for(int i = 0; i < selectedItems.size(); i++){
			BioBean selectedItem = (BioBean)selectedItems.get(i);
			selectedItem.set("LANETYPE","5");
			selectedItem.set("LANEASSG1","");
			selectedItem.set("LANEASSG2","");
			selectedItem.set("DOOR","");
			selectedItem.save();
		}
		listForm.setSelectedItems(null);
		state.getDefaultUnitOfWork().saveUOW();
		return RET_CONTINUE;
	}
}
