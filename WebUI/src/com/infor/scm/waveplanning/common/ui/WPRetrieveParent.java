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

package com.infor.scm.waveplanning.common.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.HashMap;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class WPRetrieveParent extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPRetrieveParent.class);

	private String CONTEXT_WAVEMGMT_WAVEKEY = "WAVEMGMT_WAVEKEY";

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

		//retrieve Current Form
		StateInterface state = context.getState();
		RuntimeFormInterface currentRuntimeForm = state.getCurrentRuntimeForm();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();

		//Retrieve Focus
		DataBean focus;
		if (result.getFocus() == null)
		{
			focus = currentRuntimeForm.getFocus();
		}
		else
		{
			focus = result.getFocus();
		}

		//Get Parameters

		//Source Attribute Names
		ArrayList sourceAttrNames = (ArrayList) getParameter("SOURCEATTRNAMES");
		HashMap sourceAttrValues = new HashMap(sourceAttrNames.size());

		//Target Attribute Names
		String targetBio = getParameterString("TARGETBIO");
		ArrayList targetAttrNames = (ArrayList) (getParameter("TARGETATTRNAMES") == null ? getParameter("SOURCEATTRNAMES")
				: getParameter("TARGETATTRNAMES"));

		//Should we use the context?
		boolean useContext = getParameterBoolean("USECONTEXT", false);

		

		//construct query
		String queryString = "";
		if (useContext == false)
		{
			
			//get values
			for (int i = 0; i < sourceAttrNames.size(); i++)
			{
				sourceAttrValues.put(sourceAttrNames.get(i), focus.getValue((String) sourceAttrNames.get(i)));
			}
			
			for (int i = 0; i < sourceAttrNames.size(); i++)
			{
				if (i >= 1)
				{
					queryString += " and ";
				}
				queryString += targetBio + "." + targetAttrNames.get(i) + " = '"
						+ sourceAttrValues.get((String) sourceAttrNames.get(i)) + "' ";
			}
		}
		else
		{
			//get value from context
			EpnyUserContext userContext = context.getServiceManager().getUserContext();
			String contextKey = sourceAttrNames.get(0) + context.getState().getInteractionId();
			_log.debug("LOG_DEBUG_EXTENSION_ConfirmWaveRemoveOrder_retrieveWaveKey", "ContextKey " + contextKey,
					SuggestedCategory.NONE);
			Integer waveKey = (Integer) userContext.get(contextKey);
			queryString += targetBio + "." + targetAttrNames.get(0) + " = '" + waveKey + "' ";
		}


		_log.debug("LOG_DEBUG_EXTENSION_ConfirmWaveRetrieveOrderheaderParent", queryString, SuggestedCategory.NONE);
		Query query = new Query(targetBio, queryString, null);
		BioCollectionBean results = uow.getBioCollectionBean(query);
		_log.debug("LOG_DEBUG_EXTENSION_ConfirmWaveRetrieveOrderheaderParent", "Results Size for Query = "
				+ results.size(), SuggestedCategory.NONE);
		if (results.size() == 1)
		{
			result.setFocus(results.get(String.valueOf(0)));
			return RET_CONTINUE;
		}


		_log.error("LOG_ERROR_EXTENSION_ConfirmWaveRetrieveOrderheaderParent", "Unable to find Parent Record, Size "
				+ results.size(), SuggestedCategory.NONE);
		return RET_CANCEL;

	}

	
}
