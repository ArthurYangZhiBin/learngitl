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
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.SsaAccessBase;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.util.FormUtil;



/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class QcMaintenanceSave extends com.epiphany.shr.ui.action.ActionExtensionBase
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

		UnitOfWorkBean uow = state.getDefaultUnitOfWork();

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
			
			double qtyqc1 = Double.parseDouble(selectedLine.getValue("QTYQC1").toString());
			double qtyqc2 = Double.parseDouble(selectedLine.getValue("QTYQC2").toString());
			String receiptkey = selectedLine.getValue("RECEIPTKEY").toString();
			String receiptlinenumber = selectedLine.getValue("RECEIPTLINENUMBER").toString();
			doUpdate(session, qtyqc1, qtyqc2, receiptkey, receiptlinenumber);
		}
		uow.clearState();
		imListForm.setSelectedItems(null);
		result.setFocus(imListForm.getFocus());
		return RET_CONTINUE;
	}
	
	public void doUpdate(HttpSession session, double qtyqc1, double qtyqc2, String receiptkey, String receiptlinenumber) throws UserException{
		String facilityName = session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();
    	String theSQL = "UPDATE RECEIPTDETAIL SET QTYQC1="+qtyqc1+", QTYQC2="+qtyqc2+" WHERE RECEIPTKEY='"+receiptkey+"' AND RECEIPTLINENUMBER='"+receiptlinenumber+"' AND (QTYQC1<>"+qtyqc1+" OR QTYQC2<>"+qtyqc2+") ";
    	try 
    	{
    		SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();
    		appAccess.executeUpdate(facilityName.toUpperCase(), theSQL, new Object[0]);
    	} 
    	catch (SQLException e) 
    	{			
			e.printStackTrace();
			throw new UserException("WMEXP_RECEIPT_DUP_DELETE", new Object[1]);
		}
	}
}