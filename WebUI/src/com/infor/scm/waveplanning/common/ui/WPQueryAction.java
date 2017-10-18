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

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.infor.scm.waveplanning.common.util.WPUserUtil;
import com.infor.scm.waveplanning.common.util.WPUtil;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

public class WPQueryAction extends ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPQueryAction.class);
	
	protected int execute(ActionContext context, ActionResult result) throws UserException
	{

		StateInterface state = context.getState();
		DataBean focus = result.getFocus();
		if (focus.isBioCollection())
		{
			BioCollectionBean incomingCollection = (BioCollectionBean) focus;
			boolean filterFacility = getParameterBoolean("filterByCurrentFacility", true);
			if (filterFacility == true)
			{
				Query facilityQuery = null;
				String facility = WPUtil.getFacility(state.getRequest());
				if(facility == null)
				{
					_log.error("LOG_ERROR_EXTENSION_WPQueryAction_execute",
							"Facility is null - doing nothing", SuggestedCategory.NONE);
					return RET_CONTINUE;
				}
				String facilityName = getParameterString("facilityAttrName", "WHSEID");
				facilityQuery = new Query(incomingCollection.getBioTypeName(), incomingCollection.getBioTypeName()
						+ "." + facilityName + " = '" + facility + "'", null);
				try
				{
					incomingCollection.filterInPlace(facilityQuery);
				} catch (EpiDataException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			boolean filterUser = getParameterBoolean("filterByCurrentUser", false);
			if (filterUser == true)
			{
				Query userQuery = null;
				String userId = WPUserUtil.getUserId(state);
				if(userId == null)
				{
					_log.error("LOG_ERROR_EXTENSION_WPQueryAction_execute",
							"Userid is null - doing nothing", SuggestedCategory.NONE);
					return RET_CONTINUE;
				}
				String userIDName = getParameterString("userAttrName", "USERID");
				userQuery = new Query(incomingCollection.getBioTypeName(), incomingCollection.getBioTypeName() + "."
						+ userIDName + " = '" + userId + "'", null);
				try
				{
					incomingCollection.filterInPlace(userQuery);
				} catch (EpiDataException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			result.setFocus(incomingCollection);
		}

		return RET_CONTINUE;
	}

}
