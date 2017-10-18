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

package com.ssaglobal.scm.wms.wm_pallet_manifest.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.EpiDataInvalidAttrException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.wm_table_validation.ui.FormValidation;
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

public class PalletManifestSaveValidationAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PalletManifestSaveValidationAction.class);
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
		StateInterface state = context.getState();
		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();
		//Header Validation
		int status = 9;
		RuntimeFormInterface pmHeader = FormUtil.findForm(shellToolbar, "wms_list_shell",
															"wm_pallet_manifest_detail_view", state);
		PMHeader pmHeaderValidation = null;
		if (!isNull(pmHeader))
		{
			pmHeaderValidation = new PMHeader(pmHeader, state);
			pmHeaderValidation.run();
			status = pmHeaderValidation.getStatus();
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t!@# Header is blank, nothing to save\n", SuggestedCategory.NONE);
			return RET_CANCEL;

		}

		//Detail
		ArrayList slots = new ArrayList();
		slots.add("Detail");
		RuntimeFormInterface pmDetail = FormUtil.findForm(shellToolbar, "wms_list_shell",
															"wm_pallet_manifest_detail_detail_view", slots, state);
		if (!isNull(pmDetail))
		{

			if (!isNull(pmDetail) && !(pmDetail.getName().equals("Blank")) && !(pmDetail.getFocus().isBioCollection()))
			{
				

				PMDetail pmDetailValidation = new PMDetail(pmDetail, state, status);
				pmDetailValidation.run();

				//Set Header Storerkey
				if (isNull(pmHeaderValidation.getStorer()))
				{
					_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Header storer is null" + "\n", SuggestedCategory.NONE);
					pmHeaderValidation.setStorer(pmDetailValidation.getStorer());
				}
				else
				{
					_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Header storer is not null" + "\n", SuggestedCategory.NONE);
				}
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Detail Is Null", SuggestedCategory.NONE);
			}
		}

		return RET_CONTINUE;
	}

	public class PMDetail extends FormValidation
	{
		int status;

		public PMDetail(RuntimeFormInterface f, StateInterface st, int status)
		{
			super(f, st);

			this.status = status; 
		}

		public void run() throws EpiDataException, UserException
		{
			if(status == 9)
			{
				throw new UserException("WMEXP_PM_SHIPSTATUS", new Object[] {});
			}
			validation("CASEID", "CASEMANIFEST", "CASEID");
			ownerValidation("STORERKEY");
			validation("SKU", "SKU", "SKU");
			//validate Owner + Item is valid
			itemValidation("SKU", "STORERKEY");
			locValidation("LOC");
			greaterThanOrEqualToZeroValidation("QTY");

		}

		public String getStorer()
		{
			Object storer = focus.getValue("STORERKEY");
			if (isNull(storer))
			{
				return null;
			}
			else
			{
				return storer.toString().toUpperCase();
			}
		}
	}

	public class PMHeader extends FormValidation
	{
		String palletKeyValue;

		public PMHeader(RuntimeFormInterface f, StateInterface st)
		{
			super(f, st);
			palletKeyValue = focus.getValue("PALLETKEY") == null ? null : focus.getValue("PALLETKEY").toString().toUpperCase();
		}

		public int getStatus()
		{
			return Integer.parseInt(focus.getValue("STATUS").toString());
		}

		public void run() throws DPException, UserException, EpiDataInvalidAttrException
		{
			if (isInsert)
			{
				pmDuplication();
			}
			//verify status
			if (isUpdate && ((BioBean) focus).hasBeenUpdated("STATUS"))
			{
				_log.debug("LOG_DEBUG_EXTENSION", "STATUS HAS BEEN UPDATED", SuggestedCategory.NONE);
				statusValidation();
			}

		}

		private void statusValidation() throws DPException, UserException
		{
			/*
			 ls_select = "SELECT CaseManifest.Status " + &
			 "FROM PalletDetail, CaseManifest " + &
			 "WHERE PalletDetail.CaseId = CaseManifest.CaseId AND " + &
			 "PalletDetail.PalletKey = '" + as_palletkey + "'"
			 */
			String query = "SELECT CaseManifest.Status " + "FROM PalletDetail, CaseManifest "
					+ "WHERE PalletDetail.CaseId = CaseManifest.CaseId AND " + "PalletDetail.PalletKey = '"
					+ palletKeyValue + "'";
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query.toString());
			if (results.getRowCount() >= 1)
			{
				String value = results.getAttribValue(1).getAsString();
				if (value.equalsIgnoreCase("0"))
				{
					//throw exception
					throw new UserException("WMEXP_PALLET_STATUS_MARKED", new Object[] {});
				}
			}
		}

		private void pmDuplication() throws DPException, UserException
		{
			String query = "SELECT * FROM PALLET WHERE PALLETKEY = '" + palletKeyValue + "'";
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query.toString());
			if (results.getRowCount() >= 1)
			{
				//value exists, throw exception
				String[] parameters = new String[2];
				parameters[0] = retrieveLabel("PALLETKEY");
				parameters[1] = palletKeyValue;
				throw new UserException("WMEXP_DEFAULT_DUPLICATE", parameters);
			}
		}

		public String getStorer()
		{
			Object storer = focus.getValue("STORERKEY");
			if (isNull(storer))
			{
				return null;
			}
			else
			{
				return storer.toString().toUpperCase();
			}
		}

		public void setStorer(String storer)
		{
			focus.setValue("STORERKEY", storer);
		}
	}

	boolean isNull(Object attributeValue)
	{
		if (attributeValue == null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}
