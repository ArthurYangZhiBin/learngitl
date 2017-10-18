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

package com.ssaglobal.scm.wms.wm_pickdetail.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.BioUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.SessionUtil;
import com.ssaglobal.scm.wms.wm_asnreceipts.ui.CatchWeightDataDefaultValues;
import com.ssaglobal.scm.wms.wm_asnreceipts.ui.CatchWeightSaveNumDuplicatesInSession;
import com.ssaglobal.scm.wms.wm_asnreceipts.util.LotXIdDetailKeys;

/*
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected to be
 * provided from the meta <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class SOCatchWeightDuplicate extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(SOCatchWeightDuplicate.class);

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
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		StateInterface state = context.getState();

		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		uow.clearState();
		Object numOfDuplicatesObj = SessionUtil.getInteractionSessionAttribute(CatchWeightSaveNumDuplicatesInSession.ASN_CATCH_W_D_NUM_DUPLICATES, state);
		SessionUtil.setInteractionSessionAttribute(CatchWeightSaveNumDuplicatesInSession.ASN_CATCH_W_D_NUM_DUPLICATES, null, state);

		ArrayList<String> skipList = new ArrayList<String>();
		skipList.add("SERIALKEY");
		skipList.add("ADDDATE");
		skipList.add("EDITDATE");
		skipList.add("ADDWHO");
		skipList.add("EDITWHO");
		//		skipList.add("LOTXIDKEY");
		skipList.add("LOTXIDLINENUMBER");
		skipList.add("OOTHER1");
		skipList.add("SERIALNUMBERLONG");

		if (numOfDuplicatesObj instanceof String)
		{
			int numOfDuplicates = Integer.parseInt((String) numOfDuplicatesObj);
			LotXIdDetailKeys keys = new LotXIdDetailKeys();
			BioBean cwcdToBeDuplicated = null;
			//get selected record
			cwcdToBeDuplicated = getSelectedCWCDToBeDuplicated(state, cwcdToBeDuplicated);

			if (cwcdToBeDuplicated != null && numOfDuplicates > 0)
			{

				for (int i = 0; i < numOfDuplicates; i++)
				{
					//duplicate cwcd
					QBEBioBean copiedCWCD = BioUtil.copyBIO(cwcdToBeDuplicated, uow, skipList);
					//update lotxidlinenumber
					String soKey = copiedCWCD.getValue("SOURCEKEY") == null ? null : copiedCWCD.getValue("SOURCEKEY").toString();
					String soLineNum = copiedCWCD.getValue("SOURCELINENUMBER") == null ? null : copiedCWCD.getValue("SOURCELINENUMBER").toString();
					CatchWeightDataDefaultValues.setLOTXIDLINENUMBER(copiedCWCD, uow, soKey, soLineNum, CatchWeightDataDefaultValues.TYPE_OUTBOUND);
					//					//blank out IOTHER1
					//					copiedCWCD.setValue("IOTHER1", "");

					_log.debug("LOG_DEBUG_EXTENSION_SOCatchWeightDuplicate_execute", "Going to Save " + BioUtil.getBioAsString(copiedCWCD, uow.getBioMetadata(copiedCWCD.getDataType())),
							SuggestedCategory.NONE);
					//keep record of LOTXIDKEY && LOTXIDLINENUMBER for building a query at the end
					Object lotxidkey = copiedCWCD.getValue("LOTXIDKEY");
					Object lotxidlinenumber = copiedCWCD.getValue("LOTXIDLINENUMBER");
					keys.add(lotxidkey, lotxidlinenumber);
					uow.saveUOW(true);
				}
				final String queryString = keys.getQueryString();
				_log.debug("LOG_DEBUG_EXTENSION_SOCatchWeightDuplicate_execute", "Query : " + queryString, SuggestedCategory.NONE);
				context.setNavigation("closeModalDialog166");
				BioCollectionBean rs = uow.getBioCollectionBean(new Query("lotxiddetail", queryString, null));
				_log.debug("LOG_DEBUG_EXTENSION_SOCatchWeightDuplicate_execute", "" + rs.size(), SuggestedCategory.NONE);
				result.setFocus(rs);
				return RET_CONTINUE;

			}
			

		}
		_log.debug("LOG_DEBUG_EXTENSION_SOCatchWeightDuplicate_execute", "no records to duplicate, refreshing list", SuggestedCategory.NONE);
		context.setNavigation("closeModalDialog165");

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}

	private BioBean getSelectedCWCDToBeDuplicated(StateInterface state, BioBean cwcdToBeDuplicated)
	{
		RuntimeFormInterface lotListForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_pickdetail_catchweight_data_list_view", "wm_pickdetail_catchweight_data_list_view", state);
		if (lotListForm != null && lotListForm.isListForm())
		{
			RuntimeListFormInterface lotList = (RuntimeListFormInterface) lotListForm;
			ArrayList<BioBean> selectedCWCDs = lotList.getSelectedItems();

			if (selectedCWCDs.size() > 0)
			{
				cwcdToBeDuplicated = selectedCWCDs.get(0);
			}
			lotList.setSelectedItems(null);

		}
		return cwcdToBeDuplicated;
	}

}
