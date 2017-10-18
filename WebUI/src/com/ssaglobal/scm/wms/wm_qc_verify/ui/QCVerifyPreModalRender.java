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


package com.ssaglobal.scm.wms.wm_qc_verify.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class QCVerifyPreModalRender extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(QCVerifyPreModalRender.class);
	/**
	 * Called in response to the pre-render event on a form. Write code
	 * to customize the properties of a form. All code that initializes the properties of a form that is
	 * being displayed to a user for the first time belong here. This is not executed even if the form
	 * is re-displayed to the end user on subsequent actions.
	 *
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "QCVerifyPreModalRender" + "\n", SuggestedCategory.NONE);;
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		RuntimeFormInterface listForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_qc_verify_template",
															"wm_qcverify_picked_view", state);
		if (listForm != null)
		{
			Object containerID = ((BioCollectionBean) listForm.getFocus()).get("0").getValue("CONTAINERID");
			containerID = containerID == null ? null : containerID.toString().toUpperCase();
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + containerID + "\n", SuggestedCategory.NONE);;

			//Query qcverify to get display values
			Query qry = new Query("wm_qcverify", "wm_qcverify.CONTAINERID = '" + containerID + "'", null);
			BioCollectionBean results = uowb.getBioCollectionBean(qry);
			//containerid is the primary key
			BioBean focus = results.get("0");
			//form.getFormWidgetByName("CONTAINERID").setDisplayValue((String) focus.getValue("CONTAINERID"));
			form.getFormWidgetByName("CONTAINERID").setDisplayValue((String) focus.getValue("CONTAINERID"));
			form.getFormWidgetByName("CONTAINERTYPE").setValue((String) focus.getValue("TYPE"));
			form.getFormWidgetByName("STATUS").setValue((String) focus.getValue("STATUS"));
		}
		/*
		 HttpSession session = state.getRequest().getSession();

		 try
		 {
		 String sContainerId = (String) session.getAttribute("QCCONTAINERID");
		 String sType = (String) session.getAttribute("QCTYPE");
		 String sStatus = (String) session.getAttribute("QCSTATUS");

		 form.getFormWidgetByName("CONTAINERID").setValue(sContainerId);
		 form.getFormWidgetByName("CONTAINERID").setDisplayValue(sContainerId);
		 form.getFormWidgetByName("CONTAINERTYPE").setValue(sType);
		 form.getFormWidgetByName("STATUS").setValue(sStatus);
		 //			if (!isEmpty(sContainerId))
		 //			{
		 //				form.getFormWidgetByName("CONTAINERID").setValue(sContainerId);
		 //			}
		 //			
		 //			if (!isEmpty(sType))
		 //			{
		 //				form.getFormWidgetByName("CONTAINERTYPE").setValue(sType);
		 //				
		 //			}
		 //			else
		 //			{
		 //				form.getFormWidgetByName("CONTAINERTYPE").setValue(null);
		 //			}
		 //			
		 //			if (!isEmpty(sStatus))
		 //			{
		 //				form.getFormWidgetByName("STATUS").setValue(sStatus);
		 //			}
		 //			else
		 //			{
		 //				form.getFormWidgetByName("STATUS").setValue(null);
		 //			}

		 } catch (Exception e)
		 {

		 // Handle Exceptions 
		 e.printStackTrace();
		 return RET_CANCEL;
		 }
		 session.removeAttribute("QCCONTAINERID");
		 session.removeAttribute("QCTYPE");
		 session.removeAttribute("QCSTATUS"); */
		return RET_CONTINUE;
	}

	private boolean isEmpty(Object attributeValue)
	{

		if (attributeValue == null)
		{
			return true;
		}
		else if (attributeValue.toString().matches("\\s*"))
		{
			return true;
		}
		else
		{
			return false;
		}

	}

}
