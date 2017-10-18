package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

//3rd Party Classes
import javax.servlet.http.HttpSession;

//Epiphany Classes
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;

//Added this class for fixing Incident4334565_Defect302819
public class ASNReceiptHeaderListPreRender extends FormExtensionBase{

	String sessionVariable;
	String sessionObjectValue;	

	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)throws UserException{		
		try {
			//SRG: Set the page start value in the session which can be used in the SaveASNReceipt extension.
			int winStart = form.getWindowStart();			
			String interactionID = context.getState().getInteractionId();
			String contextVariableSuffix = "WINDOWSTART";
			sessionVariable = interactionID + contextVariableSuffix;
			sessionObjectValue = "" + winStart;
			HttpSession session = context.getState().getRequest().getSession();
			session.setAttribute(sessionVariable, sessionObjectValue);			
		} catch (Exception e) {
			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}
		return RET_CONTINUE;
	}

	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form) throws EpiException {	 
		try {
			//SRG: Set the page start value in the session which can be used in the SaveASNReceipt extension.
			int winStart = form.getWindowStart();			
			String interactionID = context.getState().getInteractionId();
			String contextVariableSuffix = "WINDOWSTART";
			sessionVariable = interactionID + contextVariableSuffix;
			sessionObjectValue = "" + winStart;
			HttpSession session = context.getState().getRequest().getSession();
			session.setAttribute(sessionVariable, sessionObjectValue);			
		} catch (Exception e) {
			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}
		return RET_CONTINUE;
	}
}
