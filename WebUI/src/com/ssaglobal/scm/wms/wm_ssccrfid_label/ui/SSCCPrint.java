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

package com.ssaglobal.scm.wms.wm_ssccrfid_label.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.wm_sgtinrfid_label.ui.SGTINPrint;
import com.ssaglobal.scm.wms.wm_table_validation.ccf.NumericValidationCCF;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class SSCCPrint extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(SSCCPrint.class);

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
		RuntimeFormInterface form = context.getSourceWidget().getForm();
		_log.debug("LOG_DEBUG_EXTENSION_SSCCPrint", form.getName(), SuggestedCategory.NONE);

		SSCCForm ssccValidation = new SSCCForm(form, state);
		ssccValidation.run();
		return RET_CONTINUE;
	}

	class SSCCForm
	{
		protected RuntimeFormInterface form;

		protected StateInterface state;

		public SSCCForm(RuntimeFormInterface f, StateInterface st)
		{
			form = f;
			state = st;
		}
		
		public void run() throws UserException, EpiException
		{
			requiredField("STORERKEY");
			requiredField("COUNT");
			requiredField("PRINTERNAME");
			ownerValidation("STORERKEY");
			countValidation("COUNT");
			/* RunSP
			 * PRINTRFIDDROPIDP1S1  
				lsa_parms[1] = ls_storerkey
				lsa_parms[2] = ls_labelno
				lsa_parms[3] = ls_rfidprintername
			 */
			
			runSP();
			
		}
		
		private void runSP() throws UserException
		{
			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
			Array params = new Array();
			params.add(new TextData(form.getFormWidgetByName("STORERKEY").getDisplayValue().toUpperCase()));
			params.add(new TextData(form.getFormWidgetByName("COUNT").getDisplayValue())); 
			params.add(new TextData(((String)form.getFormWidgetByName("PRINTERNAME").getValue()))); 
			actionProperties.setProcedureParameters(params);
			actionProperties.setProcedureName("PRINTRFIDDROPIDP1S1");
			_log.debug("LOG_DEBUG_EXTENSION_SSCCForm", flattenParams(params), SuggestedCategory.NONE);
			try
			{
				EXEDataObject results = WmsWebuiActionsImpl.doAction(actionProperties);
			} catch (WebuiException e)
			{
				throw new UserException(e.getMessage(), new Object [] {});
			}
		}
		
		private String flattenParams(Array params)
		{
			String list = "Parameters: ";
			for(int i = 1 ; i <= params.size(); i++)
			{
				list += i + " - " + params.get(i) + ", "; 
			}
			return list;
		}

		private void countValidation(String attributeName) throws UserException
		{
			String attributeValue = form.getFormWidgetByName(attributeName).getDisplayValue();
			if(isEmpty(attributeValue))
			{
				return;
			}
			double value = NumericValidationCCF.parseNumber(attributeValue);
			_log.debug("LOG_SYSTEM_OUT","\n\t" + value + "\n",100L);
			if(Double.isNaN(value))
			{
				String[] parameters = new String[2];
				parameters[0] = retrieveLabel(attributeName);
				parameters[1] = attributeValue;
				throw new UserException("WMEXP_FORM_NON_NUMERIC", parameters);
			}
			
			if(value < 1 || value > 100)
			{
				String[] parameters = new String[2];
				parameters[0] = retrieveLabel(attributeName);
				parameters[1] = attributeValue;
				throw new UserException("WMEXP_SSCC_RANGE", parameters);
			}
			
		}
		

		private void requiredField(String widget) throws UserException
		{
			Object widgetValue = form.getFormWidgetByName(widget).getValue();
			if (isEmpty(widgetValue))
			{
				//throw exception
				String[] parameters = new String[1];
				parameters[0] = removeTrailingColon(retrieveLabel(widget));
				throw new UserException("WMEXP_REQFIELD", parameters);

			}
		}
		
		protected void ownerValidation(String attributeName) throws EpiDataException, UserException
		{
			if (verifyStorer(attributeName, 1) == false)
			{
				//throw exception
				String[] parameters = new String[2];
				parameters[0] = retrieveLabel(attributeName);
				parameters[1] = form.getFormWidgetByName(attributeName).getDisplayValue();
				throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
			}

		}

		protected boolean verifyStorer(String attributeName, int type) throws DPException
		{
			Object attributeValue = form.getFormWidgetByName(attributeName).getDisplayValue();
			if (isEmpty(attributeValue))
			{
				return true; //Do Nothing
			}
			attributeValue = attributeValue.toString().toUpperCase();
			String query = "SELECT * FROM STORER WHERE (STORERKEY = '" + attributeValue + "') AND (TYPE = '" + type
					+ "')";
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if (results.getRowCount() == 1)
			{
				//value exists, verified
				return true;
			}
			else
			{
				//value does not exist
				return false;
			}

		}
		
		protected boolean isEmpty(Object attributeValue)
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
		
		protected String removeTrailingColon(String label)
		{
			if (label.endsWith(":"))
			{
				label = label.substring(0, label.length() - 1);
			}
			return label;
		}
		
		protected String retrieveLabel(String widgetName)
		{

			return form.getFormWidgetByName(widgetName).getLabel("label", state.getLocale());
		}

	}

}
