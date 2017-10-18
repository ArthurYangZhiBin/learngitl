/****************************************************************************
 *               Copyright (c) 1999-2003 E.piphany, Inc.                    *
 *                          ALL RIGHTS RESERVED                             *
 *                                                                          *
 *     THIS PROGRAM CONTAINS PROPRIETARY INFORMATION OF E.PIPHANY, INC.     *
 *     ----------------------------------------------------------------     *
 *                                                                          *
 * THIS PROGRAM CONTAINS THE CONFIDENTIAL AND/OR PROPRIETARY INFORMATION    *
 * OF E.PIPHANY, INC.  ANY DUPLICATION, MODIFICATION, DISTRIBUTION, PUBLIC  *
 * PERFORMANCE, OR PUBLIC DISPLAY OF THIS PROGRAM, OR ANY PORTION OR        *
 * DERIVATION THEREOF WITHOUT THE EXPRESS WRITTEN CONSENT OF                *
 * E.PIPHANY, INC. IS STRICTLY PROHIBITED.  USE OR DISCLOSURE OF THIS       *
 * PROGRAM DOES NOT CONVEY ANY RIGHTS TO REPRODUCE, DISCLOSE OR DISTRIBUTE  *
 * ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT CONTAINS IN  *
 * WHOLE OR IN PART ANY ASPECT OF THIS PROGRAM.                             *
 *                                                                          *
 ****************************************************************************
 */


package com.ssaglobal.scm.wms.uiextensions;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
* Removes spaces from a widget
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class TrimSpaces extends com.epiphany.shr.ui.action.ActionExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(TrimSpaces.class);

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
		
		StateInterface state = context.getState();
		
		String shellFormName  = (String)getParameter("shellForm");
		String targetFormName = (String)getParameter("targetForm");
		ArrayList tabIdList = (ArrayList)getParameter("tabs");
		ArrayList bioAttrs = (ArrayList)getParameter("bioAttrs");
		
		
		RuntimeFormInterface targetForm = FormUtil.findForm(state.getCurrentRuntimeForm(), shellFormName, targetFormName, tabIdList, state);

		
		SlotInterface slot = targetForm.getSubSlot("wm_shipmentorder_pickdetail_toggle_view");
		
		RuntimeFormInterface tbgrpForm =state.getRuntimeForm(slot, "wm_shipmentorderdetail_detail_view");
		
		if (tbgrpForm != null){
			if (!tbgrpForm.getName().equalsIgnoreCase("blank")){
				
				SlotInterface tbgrpSlot = tbgrpForm.getSubSlot("tbgrp_slot");
				//wm_pickdetail_detail_view
				//wm_pickdetail_load_notes_view
				RuntimeFormInterface pickForm = state.getRuntimeForm(tbgrpSlot, "tab 1");
				
				
				DataBean dataBean = pickForm.getFocus();
				
				for(int idx = 0; idx < bioAttrs.size(); idx++){
					String bioAttrName = (String)bioAttrs.get(idx);
					String value = (String)dataBean.getValue(bioAttrName);
					
					_log.debug("DEBUG","Value::"+value==null?"NULL":value, 100L);
					
					if(value!=null){
						value = value.trim();
						if(value.length()==0)
							value = " ";
						
						
						
						dataBean.setValue(bioAttrName, value);
					}else{
						dataBean.setValue(bioAttrName," ");
					}
				}
				
				_log.debug("DEBUG","Form in slot::"+tbgrpForm.getName(),100L);
				
				
			}else{
				RuntimeListFormInterface pickListForm = (RuntimeListFormInterface) state.getRuntimeForm(slot, "wm_shipmentorderdetail_list_view");
				
				BioCollectionBean bcb = (BioCollectionBean)pickListForm.getFocus();
				for (int listIdx = 0; listIdx < bcb.size(); listIdx++){
					Bio bio = bcb.elementAt(listIdx);
					
					
					for(int idx = 0; idx < bioAttrs.size(); idx++){
						String bioAttrName = (String)bioAttrs.get(idx);
						String value = (String)bio.get(bioAttrName);
						
						_log.debug("DEBUG","Value::"+value==null?"NULL":value, 100L);
						
						
						if(value!=null){
							value = value.trim();
							if(value.length()==0)
								value = " ";
							
							_log.debug("DEBUG","Value::::"+value, 100L);
							
							bio.set(bioAttrName, value);
						}else{
							bio.set(bioAttrName, " ");
						}
						
					}
					
				}
				_log.debug("DEBUG","Form in slot:"+pickListForm.getName(),100L);
			}
		}
		
		
		return super.execute( context, result );
	}
   
  
}
