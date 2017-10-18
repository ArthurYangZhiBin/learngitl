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
import org.apache.commons.lang.StringUtils;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class CheckForUOMValueInPack extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CheckForUOMValueInPack.class);

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @exception EpiException
	 * 
	 * Modification History
	 * 
	 * 09/10/2008	AW	SDIS:SCM-00000-05605 Machine:2111399 Instead of the codelkup table, the 
	 * 					mapping is done from the pack table 
	 * 
	 */
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		StateInterface state = context.getState();
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork(); //AW
		
		RuntimeFormInterface parentForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_item_general_detail_view", state);
		BioBean parentFocus = (BioBean) parentForm.getFocus();
		String parentPackkey = null;
		parentFocus = parentFocus;
		parentPackkey = parentFocus.getString("PACKKEY");

		RuntimeListFormInterface form = (RuntimeListFormInterface) state.getCurrentRuntimeForm();
		BioBean selectedBioBean = state.getDefaultUnitOfWork().getBioBean(form.getSelectedListItem());
		String uomWidgetName = getParameterString("UOM");

		String currentUOMValue = selectedBioBean.getString(uomWidgetName);
		if (currentUOMValue == null)
		{
			return RET_CONTINUE;
		}
		_log.debug("LOG_DEBUG_EXTENSION_CheckForUOMValueInPack", "Current UOM " + currentUOMValue, SuggestedCategory.NONE);
		//Need to map that currentUOMValue to EA,CS,PL
		
		//start AW 09/10/2008 SDIS:SCM-00000-5605 Machine:2111399
		String uomAttr = UOMMappingUtil.getUOMAttribute(currentUOMValue); //AW
		String currentUOM = UOMMappingUtil.getUOM(uomAttr, parentPackkey, uow); //AW
		//end AW 09/10/2008 SDIS:SCM-00000-5605 Machine:2111399

		

		//parentPackKey contains currentUOMValue allow navigation
		//if not, change to null

		String query = "wm_pack.PACKKEY = '" + parentPackkey + "'";
		_log.debug("LOG_DEBUG_EXTENSION_CheckForUOMValueInPack", query, SuggestedCategory.NONE);
		BioCollectionBean results = state.getDefaultUnitOfWork().getBioCollectionBean(new Query("wm_pack", query, null));
		//_log.debug("LOG_DEBUG_EXTENSION_CheckForUOMValueInPack", "Results Size - " + results.size(), SuggestedCategory.NONE);

		BioBean packRecord = results.get("" + 0);
		//		for (int i = 1; i <= 9; i++)
		//		{
		//			_log.debug("LOG_DEBUG_EXTENSION_CheckForUOMValueInPack", i + " " + packRecord.getValue("PACKUOM" + i), SuggestedCategory.NONE);
		//		}
		if (currentUOM.equals(packRecord.getString("PACKUOM1")) || currentUOM.equals(packRecord.getString("PACKUOM2"))
				|| currentUOM.equals(packRecord.getString("PACKUOM3"))
				|| currentUOM.equals(packRecord.getString("PACKUOM4"))
				|| currentUOM.equals(packRecord.getString("PACKUOM5"))
				|| currentUOM.equals(packRecord.getString("PACKUOM6"))
				|| currentUOM.equals(packRecord.getString("PACKUOM7"))
				|| currentUOM.equals(packRecord.getString("PACKUOM8"))
				|| currentUOM.equals(packRecord.getString("PACKUOM9")))
		{
			// RM 8720. Server Error when opening Location in Assigned Locs - SafeGuard
			if (StringUtils.isBlank(currentUOM)) {
				// currentUOM value is blank
				// this will cause the dropdown to explode
				// setting it to null for safety
				_log.debug(	"LOG_DEBUG_EXTENSION_CheckForUOMValueInPack",
							"Setting result to null because currentUOM value is blank",
							SuggestedCategory.NONE);
				selectedBioBean.setValue("REPLENISHMENTUOM", null);
				result.setFocus(selectedBioBean);
				return RET_CONTINUE;
			}
			// RM 8720. Server Error when opening Location in Assigned Locs
			//do nothing
			_log.debug("LOG_DEBUG_EXTENSION_CheckForUOMValueInPack", "Not modifying result", SuggestedCategory.NONE);
			return RET_CONTINUE;
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION_CheckForUOMValueInPack", "Setting result to null", SuggestedCategory.NONE);
			selectedBioBean.setValue("REPLENISHMENTUOM", null);
			result.setFocus(selectedBioBean);
			return RET_CONTINUE;
		}
		

	}
}
