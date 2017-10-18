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

package com.ssaglobal.scm.wms.flowThru.ui;

// Import 3rd party packages and classes
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
// Import Epiphany packages and classes
import java.util.ArrayList;

import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
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

public class FlowThruApportionmentSaveValidation extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(FlowThruApportionmentSaveValidation.class);
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

		String storerValue;
		String apportionRule;
		//Header Validation
		RuntimeFormInterface ftaHeader = FormUtil.findForm(shellToolbar, "wm_list_shell_apportionment_rules",
															"wm_apportionment_rules_header_view", state);
		if (!isNull(ftaHeader))
		{
			FTAHeader ftaHeaderValidation = new FTAHeader(ftaHeader, state);
			storerValue = ftaHeaderValidation.getStorer();
			apportionRule = ftaHeaderValidation.getApportionRule();
		}
		else
		{
			
			return RET_CANCEL;

		}

		//Detail
		
		ArrayList tabs = new ArrayList();
		tabs.add("wm_apportionment_rules_detail tab");
		RuntimeFormInterface ftaDetailForm = FormUtil.findForm(shellToolbar, "wm_list_shell_apportionment_rules",
																"wm_apportionment_rules_detail_view", tabs, state);

		if (!isNull(ftaDetailForm))
		{
			
			FTADetail ftaDetailValidation = new FTADetail(ftaDetailForm, state, storerValue, apportionRule);
			ftaDetailValidation.run();

		}
		else
		{
			
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	public class FTAHeader extends FormValidation
	{

		public FTAHeader(RuntimeFormInterface f, StateInterface st)
		{
			super(f, st);
			

		}

		public String getApportionRule()
		{
			return (String) focus.getValue("APPORTIONRULE");
		}

		public String getStorer()
		{
			return (String) focus.getValue("STORERKEY");
		}

		public void run() throws EpiDataException, UserException
		{

		}

	}

	public class FTADetail extends FormValidation
	{
		String storerValue;

		int apportionRule;

		public FTADetail(RuntimeFormInterface f, StateInterface st, String sV, String aR)
		{
			super(f, st);
			
			this.storerValue = sV;
			apportionRule = Integer.valueOf(aR).intValue();
		}

		public void run() throws EpiDataException, UserException
		{
			//consignee
			if (apportionRule == 3 || apportionRule == 4)
			{
				customerValidation("CONSIGNEEKEY");
				
			}
			else
			{
				//Otherwise set to " "
				focus.setValue("CONSIGNEEKEY", " ");
			}
			if (isInsert)
			{
				//consignee and storer
				ftaDuplication("CONSIGNEEKEY", storerValue);
				//seq and storer
				ftaDuplication("SEQUENCE", storerValue);
			}

		}

		private void ftaDuplication(String attributeName, String storerAttributeValue) throws UserException, EpiDataException
		{

			if (verifyAttributeAndStorer(attributeName, storerAttributeValue) == true)
			{
				//value exists, throw exception
				Object[] parameters = new String[2];
				parameters[0] = retrieveLabel(attributeName);
				try
				{
					parameters[1] = focus.getValue(attributeName).toString();
				} catch (NullPointerException e)
				{
					parameters[1] = focus.getValue(attributeName);
				}
				throw new UserException("WMEXP_DEFAULT_DUPLICATE", parameters);
			}
		}

		protected boolean verifyAttributeAndStorer(String attributeName, String storerAttributeValue) throws EpiDataException
		{
			Object attributeValue = focus.getValue(attributeName);

			if (isEmpty(attributeValue) || (isEmpty(storerAttributeValue)))
			{
				return false; //Do Nothing
			}
			attributeValue = attributeValue.toString().toUpperCase();
			String query = "SELECT * " + " FROM " + "APPORTIONSTRATEGY" + " WHERE " + attributeName + " = '"
					+ attributeValue + "' " + " AND STORERKEY = '" + storerAttributeValue + "'";
			_log.debug("LOG_DEBUG_EXTENSION", "Query\n" + query, SuggestedCategory.NONE);
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if (results.getRowCount() == 1)
			{
				//value exists
				return true;
			}
			else
			{
				//value does not exist
				return false;
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
