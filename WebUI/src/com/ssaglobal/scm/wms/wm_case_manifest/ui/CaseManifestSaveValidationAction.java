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

package com.ssaglobal.scm.wms.wm_case_manifest.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;

import com.ssaglobal.scm.wms.wm_table_validation.ui.FormValidation;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class CaseManifestSaveValidationAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{

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
		//None

		//Detail
		RuntimeFormInterface cmDetail = FormUtil.findForm(shellToolbar, "wms_list_shell",
															"wm_case_manifest_detail_view", state);
		if (!isNull(cmDetail))
		{
			CMForm cmValidation = new CMForm(cmDetail, state);
			cmValidation.run();
		}
		else
		{
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	class CMForm extends FormValidation
	{
		String caseIDValue;

		public CMForm(RuntimeFormInterface f, StateInterface st)
		{
			super(f, st);

			caseIDValue = focus.getValue("CASEID") == null ? null : focus.getValue("CASEID").toString().toUpperCase();
			
			
		}

		public void run() throws UserException, EpiDataException
		{
			if (isInsert)
			{
				cmDuplication();
			}
			ownerValidation("STORERKEY");
			validation("SKU", "SKU", "SKU");
			//validate Owner + Item is valid
			itemValidation("SKU", "STORERKEY");
			greaterThanOrEqualToZeroValidation("QTY");

		}

		private void cmDuplication() throws DPException, UserException
		{
			String query = "SELECT * FROM CASEMANIFEST WHERE CASEID = '" + caseIDValue + "'";
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query.toString());
			if (results.getRowCount() >= 1)
			{
				//value exists, throw exception
				String[] parameters = new String[2];
				parameters[0] = retrieveLabel("CASEID");
				parameters[1] = caseIDValue;
				throw new UserException("WMEXP_DEFAULT_DUPLICATE", parameters);
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
