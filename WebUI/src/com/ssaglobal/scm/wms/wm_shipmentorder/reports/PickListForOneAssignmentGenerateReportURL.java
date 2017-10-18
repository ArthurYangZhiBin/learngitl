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

package com.ssaglobal.scm.wms.wm_shipmentorder.reports;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.ReportUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class PickListForOneAssignmentGenerateReportURL extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(PickListGenerateReportURL.class);

	private static final String REPORTID = "CRPT54";

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
	@Override
	protected int execute(ModalActionContext context, ActionResult result) throws EpiException
	{

		StateInterface state = context.getState();
		RuntimeFormWidgetInterface assignmentWidget = context.getModalBodyForm(0).getFormWidgetByName("ASSIGNMENT");
		//String headerSlot = getParameter("SLOT") == null ? "list_slot_1" : getParameterString("SLOT");
		//DataBean focus = state.getRuntimeForm(state.getCurrentRuntimeForm().getParentForm(state).getSubSlot(headerSlot), null).getFocus();
		
		String assignmentNumber = assignmentWidget.getDisplayValue();
		
		String assignmentLabel = removeTrailingColon(assignmentWidget.getLabel("label", context.getState().getLocale()));
		if (isEmpty(assignmentNumber))
		{
			throw new UserException("WMEXP_REQFIELD", new Object[] { assignmentLabel });
		}
		else
		{
			BioCollectionBean results = state.getDefaultUnitOfWork().getBioCollectionBean(new Query("wm_useractivity", "wm_useractivity.ASSIGNMENTNUMBER = '"
					+ assignmentNumber + "'", null));
			if (results.size() == 0)
			{
				String[] parameters = new String[2];
				parameters[0] = assignmentNumber;
				parameters[1] = assignmentLabel;
				throw new UserException("WMEXP_NOT_A_VALID", parameters);
			}
		}

		String start = "0";
		String end = "ZZZZZZZZZZZZZZZ";

//		added for date format compatibility in BIRT
		DateFormat dateFormat= ReportUtil.retrieveDateFormat(state);
		String pDate = "";
		if (ReportUtil.isBIRT(state) == true) {
			pDate = dateFormat.format(new Date());
		} else {
			pDate = dateFormat.format(new Date()) + "T" + ReportUtil.cognosTimeFormat.format(new Date());
		}
		//p_DATABASE, p_SCHEMA, p_OrdStart, p_OrdEnd, p_WaveStart, p_WaveEnd, p_AssignStart, p_AssignEnd, p_pDetails, p_pDate, outputLocale
		HashMap parametersAndValues = new HashMap();
		parametersAndValues.put("p_OrdStart", start);
		parametersAndValues.put("p_OrdEnd", end);

		parametersAndValues.put("p_WaveStart", start);
		parametersAndValues.put("p_WaveEnd", end);

		//TODO: Find out Assignment from UserActivity
		parametersAndValues.put("p_AssignStart", assignmentNumber);
		parametersAndValues.put("p_AssignEnd", assignmentNumber);

		parametersAndValues.put("p_pDetails", "Printing by Order");

		
		parametersAndValues.put("p_pDate", pDate);

		String reportURL = ReportUtil.retrieveReportURL(state, REPORTID, parametersAndValues);
		_log.debug("LOG_DEBUG_EXTENSION_OrderSheetGenerateReportURL", reportURL, SuggestedCategory.NONE);

		EpnyUserContext userCtx = context.getServiceManager().getUserContext();
		userCtx.put(ReportUtil.REPORTURL, reportURL);
		return RET_CONTINUE;

	}

	boolean isEmpty(Object attributeValue)
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

	protected String removeTrailingColon(String label)
	{
		if (label.endsWith(":"))
		{
			label = label.substring(0, label.length() - 1);
		}
		return label;
	}

}
