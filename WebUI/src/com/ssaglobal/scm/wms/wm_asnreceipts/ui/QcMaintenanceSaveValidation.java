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

// Import 3rd party packages and classes
import javax.servlet.http.HttpSession;

import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;



/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class QcMaintenanceSaveValidation extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(QcMaintenanceSave.class);

	EpnyUserContext userContext;
	ActionContext actionContext;
	String contextVariable;
	String contextVariableArgs;
	RuntimeListFormInterface imListForm;
	LocaleInterface locale;
	BioBean selectedLine;
	String sessionVariable;
	String sessionObjectValue;
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		actionContext = context;

		StateInterface state = context.getState();
		String interactionID = state.getInteractionId();
		String contextVariablePrefix = "INVMOVEERROR";
		contextVariable = contextVariablePrefix + interactionID;
		contextVariableArgs = contextVariablePrefix + "ARGS" + interactionID;

		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		locale = mda.getLocale(userLocale);

		imListForm = (RuntimeListFormInterface) FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_qc_maintenance_shell", "qc_maintenance_list_view", state);

		String contextVariableSuffix = "WINDOWSTART";
		sessionVariable = interactionID + contextVariableSuffix;
		HttpSession session = context.getState().getRequest().getSession();
		sessionObjectValue = (String)session.getAttribute(sessionVariable);
		int winStart = Integer.parseInt(sessionObjectValue);
		
		//Iterate viewed list records
		int winSize = imListForm.getWindowSize();
		BioCollectionBean bcb = (BioCollectionBean)imListForm.getFocus();
		int bcSize = bcb.size();
		int cycle = (bcSize-winStart)<winSize ? (bcSize-winStart) : winSize ;
		for( int index = 0; index < cycle; index++ )
		{
			selectedLine = (BioBean)bcb.elementAt(index+winStart);
			String receiptkey = selectedLine.getValue("RECEIPTKEY").toString();
			String receiptlinenumber = selectedLine.getValue("RECEIPTLINENUMBER").toString();
			double qtyqc1 = 0, qtyqc2 = 0;
			try{
				qtyqc1 = Double.parseDouble(selectedLine.getValue("QTYQC1").toString());
			}catch (Exception e){
				throw new UserException("收货单"+receiptkey+",行号"+receiptlinenumber+" 质检量不是数字", new Object[0]);
			}
			try{
				qtyqc2 = Double.parseDouble(selectedLine.getValue("QTYQC2").toString());
			}catch (Exception e){
				throw new UserException("收货单"+receiptkey+",行号"+receiptlinenumber+" 不合格量不是数字", new Object[0]);
			}
			
			double qtyexpected = Double.parseDouble(selectedLine.getValue("QTYEXPECTED").toString());
			if(qtyqc1>qtyexpected){
				throw new UserException("收货单"+receiptkey+",行号"+receiptlinenumber+" 质检量大于预期量", new Object[0]);
			}
			
			if(qtyqc2>qtyexpected){
				throw new UserException("收货单"+receiptkey+",行号"+receiptlinenumber+" 不合格量大于预期量", new Object[0]);
			}
			
		}
		//uow.clearState();
		//imListForm.setSelectedItems(null);
		result.setFocus(imListForm.getFocus());
		return RET_CONTINUE;
	}
}