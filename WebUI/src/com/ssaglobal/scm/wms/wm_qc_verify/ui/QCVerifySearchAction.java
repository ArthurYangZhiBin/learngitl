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
import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
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

public class QCVerifySearchAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(QCVerifySearchAction.class);
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
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "QCVerifySearchAction" + "\n", SuggestedCategory.NONE);;

		StateInterface state = context.getState();
		//
		HttpSession session = state.getRequest().getSession();
		session.removeAttribute("QCSTATE");
		session.removeAttribute("QCSELECTED");
		session.removeAttribute("QCCONTAINERID");
		session.removeAttribute("QCTYPE");
		session.removeAttribute("QCSTATUS");
		String sContainerId;
		String sType;
		String sStatus = "";
		//
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		RuntimeFormInterface searchForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_qc_verify_template",
															"wm_qc_verify_search_view", state);

		//		RuntimeFormInterface searchForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_qc_verify_template",
		//															"wm_qc_verify_search", state);

		String containerID = searchForm.getFormWidgetByName("CONTAINERID").getDisplayValue();
		containerID = containerID == null ? null : containerID.toUpperCase();
		sContainerId = containerID;
		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + containerID + "\n", SuggestedCategory.NONE);;

		//Search based on caseid
		Query caseQuery = new Query("wm_pickdetail", "wm_pickdetail.CASEID = '" + containerID + "'", null);
		BioCollectionBean caseCollection = uow.getBioCollectionBean(caseQuery);
		if (caseCollection.size() > 0)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "CASE " + caseCollection.size() + "\n", SuggestedCategory.NONE);;
			sType = "CASE";
			Object status = determineStatus(caseCollection);
			if(status == null)
			{
				throw new UserException("WMEXP_QC_NOTPICKED", new Object [] {});
			}
			sStatus = (String) status;
			result.setFocus(caseCollection);

		}
		else
		{
			//Search based on dropid		
			Query dropQuery = new Query("wm_pickdetail", "wm_pickdetail.DROPID = '" + containerID + "'", null);
			BioCollectionBean dropCollection = uow.getBioCollectionBean(dropQuery);
			if (dropCollection.size() > 0)
			{
				_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "DROP " + dropCollection.size() + "\n", SuggestedCategory.NONE);;
				sType = "DROP";
				Object status = determineStatus(dropCollection);
				if(status == null)
				{
					throw new UserException("WMEXP_QC_NOTPICKED", new Object [] {});
				}
				sStatus = (String) status;
				result.setFocus(dropCollection);
			}
			else
			{
				context.setNavigation("menuClickEvent392");
				_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "No Results" + "\n", SuggestedCategory.NONE);;
				sType = "";
				sStatus = "";
				throw new UserException("WMEXP_QC_NOTFOUND", new Object[] {});
			}
		}

		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "END" + "\n", SuggestedCategory.NONE);;
		//Set Session
		session.setAttribute("QCCONTAINERID", sContainerId);
		session.setAttribute("QCTYPE", sType);
		session.setAttribute("QCSTATUS", sStatus);
		context.setNavigation("menuClickEvent390");
		return RET_CONTINUE;
	}

	String determineStatus(BioCollectionBean collection) throws EpiDataException
	{
		boolean verified = true;
		boolean shipped = true;
		boolean picked = true;

		for (int i = 0; i < collection.size(); i++)
		{
			BioBean selected = collection.get(String.valueOf(i));

			int status = (Integer.valueOf(selected.getValue("STATUS").toString())).intValue();
			if (!(status >= 5))
			{
				//set picked to false because one record is not picked
				picked = false;
			}

			if (status != 9)
			{
				//set shipped to false because one record is not shipped
				shipped = false;
			}

			if (isEmpty(selected.getValue("VERIFIEDQTY")))
			{
				//set verified to false because one record is not verified
				verified = false;
			}

		}

		if (picked == true)
		{
			if (shipped == true)
			{
				return "9";
			}
			else if (verified == true)
			{
				return "1";
			}
			else
			{
				return "0";
			}
		}
		else
		{
			//invalid results
			return null;
		}

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
