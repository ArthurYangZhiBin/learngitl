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

package com.ssaglobal.scm.wms.wm_container_manifest.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
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

public class ContainerManifestSaveValidationAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ContainerManifestSaveValidationAction.class);

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
		RuntimeFormInterface cmHeader = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_containermanifest_detail_view", state);
		if (!isNull(cmHeader))
		{
			CMHeader cmHeaderValidation = new CMHeader(cmHeader, state);
			cmHeaderValidation.run();
		}
		else
		{
			
			return RET_CANCEL;

		}



		//Detail
		RuntimeFormInterface cmToggleForm = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_containermanifestdetail_toggle_slot", state);
		if (!isNull(cmToggleForm))
		{
			SlotInterface cmToggleSlot = cmToggleForm.getSubSlot("wm_containermanifestdetail_toggle_slot");
			RuntimeFormInterface cmDetail = state.getRuntimeForm(cmToggleSlot, "Detail");
			if (!isNull(cmDetail) && !(cmDetail.getName().equals("Blank")) && !(cmDetail.getFocus().isBioCollection()))
			{

				CMDetail cmDetailValidation = new CMDetail(cmDetail, state);
				cmDetailValidation.run();
			}
			else
			{

			}
		}


		return RET_CONTINUE;
	}

	public class CMDetail extends FormValidation
	{

		public CMDetail(RuntimeFormInterface f, StateInterface st)
		{
			super(f, st);
			
		}

		public void run() throws EpiDataException, UserException
		{
			validation("PALLETKEY", "PALLET", "PALLETKEY");

		}

	}

	public class CMHeader extends FormValidation
	{

		public CMHeader(RuntimeFormInterface f, StateInterface st)
		{
			super(f, st);
			
		}

		public void run() throws EpiDataException, UserException
		{
			carrierValidation("CARRIERKEY");
			//			verify status
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
			 "FROM ContainerDetail, PalletDetail, CaseManifest " + &
			 "WHERE ContainerDetail.PalletKey = PalletDetail.PalletKey AND " + &
			 "Palletdetail.CaseId = CaseManifest.CaseId AND " + &
			 "ContainerDetail.ContainerKey = '" + as_containerkey + "'"
			 */
			String containerKeyValue = focus.getValue("CONTAINERKEY") == null ? null
					: focus.getValue("CONTAINERKEY").toString().toUpperCase();
			String query = "SELECT CaseManifest.Status " + "FROM ContainerDetail, PalletDetail, CaseManifest "
					+ "WHERE ContainerDetail.PalletKey = PalletDetail.PalletKey AND "
					+ "Palletdetail.CaseId = CaseManifest.CaseId AND " + "ContainerDetail.ContainerKey = '"
					+ containerKeyValue + "'";
			_log.debug("LOG_DEBUG_EXTENSION", query, SuggestedCategory.NONE);
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
