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
/***************************************************************************
* Copyright (c) 2002 E.piphany, Inc.                                       *
* ALL RIGHTS RESERVED                                                      *
*                                                                          *
* THIS PROGRAM CONTAINS PROPRIETARY INFORMATION OF E.PIPHANY, INC.         *
* ----------------------------------------------------------------         *
*                                                                          *
* THIS PROGRAM CONTAINS THE CONFIDENTIAL AND/OR PROPRIETARY INFORMATION    *
* OF E.PIPHANY, INC. ANY DUPLICATION, MODIFICATION, DISTRIBUTION, PUBLIC   *
* PERFORMANCE, OR PUBLIC DISPLAY OF THIS PROGRAM, OR ANY PORTION OR        *
* DERIVATION THEREOF WITHOUT THE EXPRESS WRITTEN CONSENT OF                *
* E.PIPHANY, INC. IS STRICTLY PROHIBITED. USE OR DISCLOSURE OF THIS        *
* PROGRAM DOES NOT CONVEY ANY RIGHTS TO REPRODUCE, DISCLOSE OR DISTRIBUTE  *
* ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT CONTAINS IN  *
* WHOLE OR IN PART ANY ASPECT OF THIS PROGRAM.                             *
*                                                                          *
***************************************************************************/

package com.ssaglobal.scm.wms.wm_cycleclass.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class CycleClassPreDelete extends com.epiphany.shr.ui.action.ActionExtensionBase {


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
		String table = "wm_sku", widgetName = "CLASSID";
		String attribute = "ABC";
		String[] parameter = new String[1];
		//Find location value selected for delete
		RuntimeFormInterface shellForm = state.getCurrentRuntimeForm().getParentForm(state);
		RuntimeListFormInterface listForm = (RuntimeListFormInterface)state.getRuntimeForm(shellForm.getSubSlot("list_slot_1"),null);
		ArrayList selected = listForm.getAllSelectedItems();


		if (selected==null || selected.isEmpty() )
			throw new FormException("WMEXP_RECORD_NOT_SEL", null);


		
		DataBean bean = (DataBean)selected.get(0);
		String classId = bean.getValue(widgetName).toString();

		//Query against SKU table for dependent records
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		String queryString = table+"."+attribute+"='"+classId+"' ";
		Query qry = new Query(table, queryString, null);
		BioCollectionBean list = uow.getBioCollectionBean(qry);
		//If dependent records exist, block delete
		if(list.size()>0){
			parameter[0]=classId;
			throw new FormException("WMEXP_SO_PDRID", parameter);
		}
		
		return RET_CONTINUE;
	}
   
   /**
    * Fires in response to a UI action event, such as when a widget is clicked or
    * a value entered in a form in a modal dialog
    * Write code here if u want this to be called when the UI Action event is fired from a modal window
    * <ul>
    * <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext} exposes information about the
    * event, including the service and the user interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
    * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes information about the results
    * of the action that has occurred, and enables your extension to modify them.</li>
    * </ul>
    */
    protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {

       try {
           // Add your code here to process the event
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }
}
