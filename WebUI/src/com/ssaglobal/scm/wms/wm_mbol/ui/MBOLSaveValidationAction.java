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

package com.ssaglobal.scm.wms.wm_mbol.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
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

public class MBOLSaveValidationAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(MBOLSaveValidationAction.class);
	
	public String mbolKey;
	
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
		int shipStatus = 9;
		RuntimeFormInterface mbolHeader = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_mbol_detail_view",
															state);
		if (!isNull(mbolHeader))
		{
			MBOLHeader mbolHeaderValidation = new MBOLHeader(mbolHeader, state);
			mbolHeaderValidation.run();
			shipStatus  = mbolHeaderValidation.getShipStatus();
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION_MBOLSaveValidationAction", "Header is blank, nothing to save", SuggestedCategory.NONE);
			return RET_CANCEL;

		}

		//Detail
		_log.debug("LOG_DEBUG_EXTENSION_MBOLSaveValidationAction",("\n\n\nCHECKING DETAIL"),SuggestedCategory.NONE);
		ArrayList tabs = new ArrayList();
		tabs.add("Detail");
		RuntimeFormInterface mbolDetailForm = FormUtil.findForm(shellToolbar, "wms_list_shell",
																"wm_mboldetail_detail_view", tabs, state);
		//_log.debug("LOG_DEBUG_EXTENSION_MBOLSaveValidationAction",(mbolDetailForm.getName()),SuggestedCategory.NONE);
		if (!isNull(mbolDetailForm))
		{
			_log.debug("LOG_DEBUG_EXTENSION_MBOLSaveValidationAction",("!! " + mbolDetailForm.getName()),SuggestedCategory.NONE);
			MBOLDetail mbolDetailValidation = new MBOLDetail(mbolDetailForm, state, shipStatus);
			mbolDetailValidation.run();

		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION_MBOLSaveValidationAction",("!@#!@#!@#!@#  MBOL TOGGLE FORM IS NULL"),SuggestedCategory.NONE);
		}

		return RET_CONTINUE;
	}

	public class MBOLDetail extends FormValidation
	{
		String mbolLineValue;
		int shipStatus;

		public MBOLDetail(RuntimeFormInterface f, StateInterface st, int shipStatus)
		{
			super(f, st);
			_log.debug("LOG_DEBUG_EXTENSION_MBOLSaveValidationAction",("\n\n!@~ Start of " + form.getName() + " Validation"),SuggestedCategory.NONE);
			this.mbolLineValue = (String) focus.getValue("MBOLLINENUMBER") == null ? null : focus.getValue("MBOLLINENUMBER").toString().toUpperCase();
			this.shipStatus = shipStatus;
		}

		public void run() throws EpiDataException, UserException
		{
			if(isInsert)
			{
				mbolDetailDuplication();
			}
			if(shipStatus == 9)
			{
				throw new UserException("WMEXP_MBOL_SHIPSTATUS", new Object[] {});
			}
			validation("CONTAINERKEY", "CONTAINER", "CONTAINERKEY");
			validation("ORDERKEY", "ORDERS", "ORDERKEY");
			validation("PALLETKEY", "PALLET", "PALLETKEY");
			onlyOneRequired();

		}
		
		private void mbolDetailDuplication() throws DPException, UserException
		{
			
			String query = "SELECT * FROM MBOLDETAIL WHERE MBOLKEY = '" + mbolKey + "' AND MBOLLINENUMBER = '" + mbolLineValue + "'";
			_log.debug("LOG_DEBUG_EXTENSION_MBOLDetail", query, SuggestedCategory.NONE);
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query.toString());
			if (results.getRowCount() >= 1)
			{
				//value exists, throw exception
				String[] parameters = new String[2];
				parameters[0] = retrieveLabel("MBOLLINENUMBER");
				parameters[1] = mbolLineValue;
				throw new UserException("WMEXP_DEFAULT_DUPLICATE", parameters);
			}
		}

		private void onlyOneRequired() throws UserException
		{
			Object containerValue = focus.getValue("CONTAINERKEY");
			Object orderValue = focus.getValue("ORDERKEY");
			Object palletValue = focus.getValue("PALLETKEY");

			ArrayList attributes = new ArrayList();
			attributes.add(containerValue);
			attributes.add(orderValue);
			attributes.add(palletValue);

			int notBlankCount = 0; //# of attributes that are not blank
			for (int i = 0; i < attributes.size(); i++)
			{
				if (!isEmpty(attributes.get(i)))
				{
					notBlankCount++;
				}
			}

			if (notBlankCount != 1)
			{
				//throw exception
				//one and only one value is required
				form.getFormWidgetByName("CONTAINERKEY").setProperty("label class", "epnyReqdMissing");
				form.getFormWidgetByName("ORDERKEY").setProperty("label class", "epnyReqdMissing");
				form.getFormWidgetByName("PALLETKEY").setProperty("label class", "epnyReqdMissing");
				throw new UserException("WMEXP_MBOL_REQUIRED", new Object[] {});
			}

		}

	}

	public class MBOLHeader extends FormValidation
	{
		String mbolKeyValue;

		public MBOLHeader(RuntimeFormInterface f, StateInterface st)
		{
			super(f, st);
			_log.debug("LOG_DEBUG_EXTENSION_MBOLSaveValidationAction",("!@~ Start of " + form.getName() + " Validation"),SuggestedCategory.NONE);
			mbolKeyValue = focus.getValue("MBOLKEY") == null ? null : focus.getValue("MBOLKEY").toString().toUpperCase();
		}

		public int getShipStatus()
		{
			return Integer.parseInt(focus.getValue("STATUS").toString());
		}

		public void run() throws EpiDataException, UserException
		{
			if (isInsert)
			{
				mbolDuplication();
			}
			carrierValidation("CARRIERKEY");
			mbolKey = this.mbolKeyValue;
		}

		private void mbolDuplication() throws DPException, UserException
		{
			String query = "SELECT * FROM MBOL WHERE MBOLKEY = '" + mbolKeyValue + "'";
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query.toString());
			if (results.getRowCount() >= 1)
			{
				//value exists, throw exception
				String[] parameters = new String[2];
				parameters[0] = retrieveLabel("MBOLKEY");
				parameters[1] = mbolKeyValue;
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
