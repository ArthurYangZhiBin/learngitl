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
package com.ssaglobal.scm.wms.wm_inventory_move.ui;

//Import 3rd party packages and classes
import javax.servlet.http.HttpSession;

//Import Epiphany packages and classes
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.ssaglobal.scm.wms.util.FormUtil;

//SM 07/31/07 ISSUE SCM-00000-02843 Multiple Execute Moves actions fail. (CLASS CREATED)
public class InventoryMoveVerifyFormChange extends ActionExtensionBase{
	
	EpnyUserContext userContext;
	String contextVariable;
	RuntimeListFormInterface listForm;
	BioBean selectedMove;
	String sessionVariable;
	String sessionObjectValue;
	static String TO_QTY = "QUANTITYTOMOVE";
	
	protected int execute(ActionContext context, ActionResult result) throws EpiDataException{
		StateInterface state = context.getState();
		
		userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		
		String interactionID = state.getInteractionId();
		String contextVariablePrefix = "INVVERIFYFORMCHANGE";
		contextVariable = contextVariablePrefix + interactionID;

		listForm = (RuntimeListFormInterface) FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_inventory_move__view", state);

		//Identify list position from session object
		String contextVariableSuffix = "WINDOWSTART";
		sessionVariable = interactionID + contextVariableSuffix;
		HttpSession session = context.getState().getRequest().getSession();
		sessionObjectValue = (String)session.getAttribute(sessionVariable);
		int winStart = Integer.parseInt(sessionObjectValue);
		
		int winSize = listForm.getWindowSize();
		BioCollectionBean bcb = (BioCollectionBean)listForm.getFocus();
		int bcSize = bcb.size();
		int cycle = (bcSize-winStart)<winSize ? (bcSize-winStart) : winSize ;

		int index=0;
		boolean changesFound = false;
		
		while(!changesFound && index<cycle){
			selectedMove = (BioBean)bcb.elementAt(index+winStart);
			double toQty = Double.parseDouble(selectedMove.getValue(TO_QTY).toString());
			if(toQty>0)
				changesFound=true;
			index++;
		}
		if(changesFound){
			String nav = findNavigation(context.getNavigation().getName());
			userContext.put(contextVariable, "WMEXP_VERIFY_FORM_CHANGE");
			context.setNavigation(nav);
		}
		return RET_CONTINUE;
	}
	
	private String findNavigation(String name){
		return name+"Modal";
	}
}