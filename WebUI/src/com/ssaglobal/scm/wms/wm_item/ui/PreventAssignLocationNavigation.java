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

package com.ssaglobal.scm.wms.wm_item.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
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

public class PreventAssignLocationNavigation extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PreventAssignLocationNavigation.class);
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

		//Ensure Pack exists in the warehouse before allowing navigation
		try
		{
			StateInterface state = context.getState();
			
			RuntimeFormInterface parentForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_item_general_detail_view", state);
			DataBean parentFocus = parentForm.getFocus();
			
			Object parentPackkey = null;
			if (parentFocus instanceof BioBean)
			{
				parentFocus = (BioBean) parentFocus;
				parentPackkey = parentFocus.getValue("PACKKEY");
				
			}
			else
			{
				parentFocus = (QBEBioBean) parentFocus;
				parentPackkey = parentForm.getFormWidgetByName("PACKKEY").getDisplayValue();
			}
			
			if(parentPackkey != null)
			{
				parentPackkey = parentPackkey.toString().toUpperCase();
				_log.debug("LOG_DEBUG_EXTENSION_PreventAssignLocationNavigation", "Packkey " + parentPackkey, SuggestedCategory.NONE);
				BioCollectionBean results = state.getDefaultUnitOfWork().getBioCollectionBean(new Query("wm_pack", "wm_pack.PACKKEY = '" + parentPackkey + "'", null));
				_log.debug("LOG_DEBUG_EXTENSION_PreventAssignLocationNavigation", "Results size " + results.size(), SuggestedCategory.NONE);
				if(results.size() == 0)
				{
					String[] parameters = new String[1];
					parameters[0] = parentPackkey.toString().toUpperCase();
					throw new UserException("WMEXP_ITEM_EXISTINGPACK", parameters);
				}
			}
			
			

		} catch (RuntimeException e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	
}
