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

package com.ssaglobal.scm.wms.wm_pack_charge.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.text.ParseException;
import java.util.ArrayList;

import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
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

public class PackChargeSaveValidationAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PackChargeSaveValidationAction.class);
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

		//		Detail Validation
		RuntimeFormInterface pcDetail = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_pack_charge_detail_view", state);
		if (!isNull(pcDetail))
		{
			PCDetail pcDetailValidation = new PCDetail(pcDetail, state);
			pcDetailValidation.run();
		}

		return RET_CONTINUE;
	}

	public class PCDetail extends FormValidation
	{
		String chargeCode;

		String packKey;

		public PCDetail(RuntimeFormInterface f, StateInterface st)
		{
			super(f, st);
			chargeCode = (String) focus.getValue("CHARGECODE") == null ? null : focus.getValue("CHARGECODE").toString().toUpperCase();
			packKey = (String) focus.getValue("PACKKEY");
		}

		public void run() throws DPException, UserException
		{
			if (isInsert == true)
			{
				pcDuplication();
			}
			nullValidation();
			greaterThanValidation();

		}

		private void nullValidation() throws UserException
		{
			boolean error = false;
			String widgetLabels = " ";
			ArrayList prefix = new ArrayList();
			prefix.add(0, "CASE");
			prefix.add(1, "INNERPACK");
			prefix.add(2, "EACH");
			prefix.add(3, "PALLET");
			prefix.add(4, "CUBE");
			prefix.add(5, "GROSSWGT");
			prefix.add(6, "NETWGT");
			prefix.add(7, "OTHERUNIT1");
			prefix.add(8, "OTHERUNIT2");

			for (int i = 0; i < 9; i++)
			{
				String uomPrefix = (String) prefix.get(i);
				String uom = form.getFormWidgetByName(uomPrefix + "PACKUOM").getDisplayValue(); //Name from the Attribute Domain
				//if UOM is not empty
				//ensure the Charges are not empty
				if (!isEmpty(focus.getValue(uomPrefix + "PACKUOM")))
				{
					for (int j = 1; j <= 3; j++)
					{
						RuntimeFormWidgetInterface widget = form.getFormWidgetByName(uomPrefix + "CHARGE" + j);
						if (isEmpty(widget.getDisplayValue()))
						{
							error = true;
							widgetLabels += uom + " " + widget.getLabel("label", state.getLocale()) + ", ";
						}
					}

				}
			}
			if (error == true)
			{
				String args[] = { widgetLabels };
				String errorMsg = getTextMessage("WMEXP_SPECIAL_NO_NULLS", args, state.getLocale());

				throw new UserException(errorMsg, new Object[0]);
			}
		}

		private void greaterThanValidation() throws UserException
		{
			ArrayList prefix = new ArrayList();
			prefix.add(0, "CASE");
			prefix.add(1, "INNERPACK");
			prefix.add(2, "EACH");
			prefix.add(3, "PALLET");
			prefix.add(4, "CUBE");
			prefix.add(5, "GROSSWGT");
			prefix.add(6, "NETWGT");
			prefix.add(7, "OTHERUNIT1");
			prefix.add(8, "OTHERUNIT2");

			for (int i = 0; i < 9; i++)
			{
				String uomPrefix = (String) prefix.get(i);
				//if UOM is not empty
				//check the FROMs and TOs are in order
				if (!isEmpty(focus.getValue(uomPrefix + "PACKUOM")))
				{
					String uom = form.getFormWidgetByName(uomPrefix + "PACKUOM").getDisplayValue();

					//from1 < to1
					greaterThanValidation(uomPrefix + "TO1", uomPrefix + "FROM1", uom);
					//to1 < from2
					greaterThanValidation(uomPrefix + "FROM2", uomPrefix + "TO1", uom);
					//from2 < to2
					greaterThanValidation(uomPrefix + "TO2", uomPrefix + "FROM2", uom);
					//to2 < from3
					greaterThanValidation(uomPrefix + "FROM3", uomPrefix + "TO3", uom);

					//charges
					greaterThanOrEqualToZero(uomPrefix + "CHARGE1", uom);
					greaterThanOrEqualToZero(uomPrefix + "CHARGE2", uom);
					greaterThanOrEqualToZero(uomPrefix + "CHARGE3", uom);

					greaterThanOrEqualToZero(uomPrefix + "COST", uom);
				}
			}

		}

		protected void greaterThanOrEqualToZero(String greaterName, String uom) throws UserException
		{
			double greater;
			Object greaterValue = null;
			try
			{
				if (focus.getValue(greaterName) == null)
				{
					//set to 0
					focus.setValue(greaterName, "0");
				}
				greaterValue = focus.getValue(greaterName);
				greater = parseNumber(greaterValue.toString());
			} catch (ParseException e)
			{
				Object[] parameters = new Object[2];
				parameters[0] = uom + " " + retrieveLabel(greaterName);
				parameters[1] = greaterValue;
				throw new UserException("WMEXP_FORM_NON_NUMERIC", parameters);
			}

			if (greater < 0)
			{
				//throw exception
				Object[] parameters = new Object[1];
				parameters[0] = uom + " " + retrieveLabel(greaterName);
				throw new UserException("WMEXP_GREATER_THAN_ZERO", parameters);
			}
		}

		protected void greaterThanValidation(String greaterName, String lesserName, String uom) throws UserException
		{
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + uom + "\n", SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + greaterName + "!\n", SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + lesserName + "!\n", SuggestedCategory.NONE);
			double greater;
			Object greaterValue = null;
			try
			{
				//greaterValue = focus.getValue(greaterName) == null ? new Double(0) : focus.getValue(greaterName);
				if (focus.getValue(greaterName) == null)
				{
					//set to 0
					focus.setValue(greaterName, "0");
				}
				greaterValue = focus.getValue(greaterName);
				greater = parseNumber(greaterValue.toString());
			} catch (ParseException e)
			{
				Object[] parameters = new Object[2];
				parameters[0] = uom + " " + retrieveLabel(greaterName);
				parameters[1] = greaterValue;
				throw new UserException("WMEXP_FORM_NON_NUMERIC", parameters);
			}

			double lesser;
			Object lesserValue = null;
			try
			{
				//lesserValue = focus.getValue(lesserName) == null ? new Double(0) : focus.getValue(lesserName);
				if (focus.getValue(lesserName) == null)
				{
					//set to 0
					focus.setValue(lesserName, "0");
				}
				lesserValue = focus.getValue(lesserName);
				lesser = parseNumber(lesserValue.toString());
			} catch (ParseException e)
			{
				Object[] parameters = new Object[2];
				parameters[0] = uom + " " + retrieveLabel(lesserName);
				parameters[1] = lesserValue;
				throw new UserException("WMEXP_FORM_NON_NUMERIC", parameters);
			}

			if (greater <= lesser)
			{
				//throw exception
				Object[] parameters = new Object[2];
				parameters[0] = uom + " " + retrieveLabel(greaterName);
				parameters[1] = uom + " " + retrieveLabel(lesserName);
				throw new UserException("WMEXP_GREATER_THAN", parameters);
			}
		}

		void pcDuplication() throws DPException, UserException
		{
			String query = "SELECT * FROM PACKCHARGE WHERE CHARGECODE = '" + chargeCode + "' AND PACKKEY = '" + packKey
					+ "'";
			_log.debug("LOG_DEBUG_EXTENSION", "///QUERY " + query, SuggestedCategory.NONE);
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if (results.getRowCount() >= 1)
			{
				//				value exists, throw exception
				throw new UserException("WMEXP_PC_DUPLICATE", new Object[] {});
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
