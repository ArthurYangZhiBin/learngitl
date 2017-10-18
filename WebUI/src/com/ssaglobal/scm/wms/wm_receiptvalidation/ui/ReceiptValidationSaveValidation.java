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

package com.ssaglobal.scm.wms.wm_receiptvalidation.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
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
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.wm_item.ui.Tab;
import com.ssaglobal.scm.wms.wm_table_validation.ccf.NumericValidationCCF;
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

public class ReceiptValidationSaveValidation extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	private static String PERFORM_QTY_VALIDATION = "PERFORMQTYVALIDATION";

	private static String OVERAGE_HARD_ERROR = "OVERAGEHARDERROR";

	private static String OVERAGE_HARD_ERROR_PERCENT = "OVERAGEHARDERRORPERCENT";

	private static String OVERAGE_MESSAGE = "OVERAGEMESSAGE";

	private static String OVERAGE_OVERIDE_PERCENT = "OVERAGEOVERIDEPERCENT";

	private static String OVERAGE_OVERRIDE = "OVERAGEOVERRIDE";

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ReceiptValidationSaveValidation.class);
	
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
		RuntimeFormInterface receiptDetail = FormUtil.findForm(shellToolbar, "wms_list_shell",
																"wm_receiptvalidation_template", state);
		if (receiptDetail != null)
		{
			ReceiptValidation rvValidation = new ReceiptValidation(receiptDetail, state);
			rvValidation.run();
		}
		else
		{
			return RET_CANCEL;
		}
		return RET_CONTINUE;
	}

	class ReceiptValidation extends Tab
	{
		RuntimeFormInterface overage;

		public ReceiptValidation(RuntimeFormInterface f, StateInterface st)
		{
			super(f, st);
			

			SlotInterface overageToggle = form.getSubSlot("wm_receiptvalidation_form_slot2");
			overage = state.getRuntimeForm(overageToggle, "wm_receiptvalidation_detail_overage_view");

		}

		public void run() throws UserException, EpiDataException
		{
			if (isInsert)
			{
				rvDuplication();
			}
			if (!isNull(overage))
			{
				greaterThanOrEqualToZeroValidation("OVERAGEHARDERRORPERCENT", overage);
				greaterThanOrEqualToZeroValidation("OVERAGEOVERIDEPERCENT", overage);
			}
			defaultFlagValidation();
		}

		private void rvDuplication() throws DPException, UserException
		{
			String rvKey = focus.getValue("RECEIPTVALIDATIONKEY") == null ? null : focus.getValue("RECEIPTVALIDATIONKEY").toString().toUpperCase()  ;
			String query = "SELECT * FROM RECEIPTVALIDATION WHERE (RECEIPTVALIDATIONKEY = '" + rvKey + "')";
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if (results.getRowCount() >= 1)
			{
				String[] parameters = new String[5];
				parameters[0] = retrieveLabel("RECEIPTVALIDATIONKEY");
				parameters[1] = rvKey;
				throw new UserException("WMEXP_RV_DUPLICATE", parameters);
			}

		}

		protected void greaterThanOrEqualToZeroValidation(String attributeName, RuntimeFormInterface f) throws UserException
		{
			_log.debug("LOG_DEBUG_EXTENSION", "Performing >0 Validation on " + attributeName + " " + f.getName(), SuggestedCategory.NONE);
			numericValidation(attributeName, f);
			//if it passes numericValidation, you can parse the number
			Object tempValue = focus.getValue(attributeName);
			if (isNull(tempValue))
			{
				return;
			}
			String attributeValue = tempValue.toString();
			double value = NumericValidationCCF.parseNumber(attributeValue);
			_log.debug("LOG_DEBUG_EXTENSION", "Value of " + attributeName + " - " + value, SuggestedCategory.NONE);
			if (Double.isNaN(value))
			{
				String[] parameters = new String[2];
				parameters[0] = retrieveLabel(attributeName, f);
				parameters[1] = attributeValue.toString();
				throw new UserException("WMEXP_FORM_NON_NUMERIC", parameters);
			}
			else if (value < 0)
			{
				String[] parameters = new String[2];
				parameters[0] = retrieveLabel(attributeName, f);
				parameters[1] = attributeValue.toString();
				throw new UserException("WMEXP_FORM_NONNEG_VALIDATION", parameters);
			}
		}

		protected void numericValidation(String attributeName, RuntimeFormInterface f) throws UserException
		{
			_log.debug("LOG_DEBUG_EXTENSION", "Performing # Validation on " + attributeName + " " + f.getName(), SuggestedCategory.NONE);
			Object attributeValue = focus.getValue(attributeName);
			if (!isEmpty(attributeValue) && (NumericValidationCCF.isNumber(attributeValue.toString()) == false))
			{
				//throw exception
				String[] parameters = new String[2];
				parameters[0] = retrieveLabel(attributeName, f);
				parameters[1] = attributeValue.toString();
				throw new UserException("WMEXP_FORM_NON_NUMERIC", parameters);
			}

		}

		private void defaultFlagValidation() throws EpiDataException
		{
			Object defaultFlag = focus.getValue("DEFAULTFLAG");
			if (isEmpty(defaultFlag))
			{
				//treat as unchecked
			}
			else if (defaultFlag.toString().equals("1"))
			{
				//disable all other DEFAULTFLAGs where it's not equal to the key
				String key = focus.getValue("RECEIPTVALIDATIONKEY").toString();
				String sqlQuery = "UPDATE RECEIPTVALIDATION SET DEFAULTFLAG = '0' WHERE (RECEIPTVALIDATIONKEY <> '"
						+ key + "')";
				_log.debug("LOG_DEBUG_EXTENSION", "Query " + sqlQuery, SuggestedCategory.NONE);
				/* Ask about Update statements */
				//EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
				UnitOfWorkBean uow = state.getDefaultUnitOfWork();
				String queryStatement = "wm_receiptvalidation" + "." + "RECEIPTVALIDATIONKEY" + " != '" + key + "'";
				_log.debug("LOG_DEBUG_EXTENSION", "!@| Query " + queryStatement, SuggestedCategory.NONE);
				Query query = new Query("wm_receiptvalidation", queryStatement, null);
				BioCollectionBean results = uow.getBioCollectionBean(query);
				for (int i = 0; i < results.size(); i++)
				{
					BioBean current = results.get(String.valueOf(i));
					_log.debug("LOG_DEBUG_EXTENSION", "|| " + i + " of " + results.size() + "  = "+ current.get("RECEIPTVALIDATIONKEY"), SuggestedCategory.NONE);
					current.set("DEFAULTFLAG", "0");
				}
				//_log.debug("LOG_DEBUG_EXTENSION", "!! Saving UOW ", SuggestedCategory.NONE);
				//uow.saveUOW();
			}
			else if (defaultFlag.toString().equals("0"))
			{
				//do nothing
			}
		}

	}

	private boolean isEmpty(Object attributeValue)
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
}
