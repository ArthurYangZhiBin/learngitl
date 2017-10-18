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

package com.ssaglobal.scm.wms.wm_wave_labels.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.wm_table_validation.ccf.NumericValidationCCF;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class WaveLabelsRunSP extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WaveLabelsRunSP.class);

	String loginId;

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
		EpnyUserContext userCtx = context.getServiceManager().getUserContext();
		loginId = (String) userCtx.get("logInUserId");

		RuntimeFormInterface form = state.getCurrentRuntimeForm();

		WaveLabels waveLabel = new WaveLabels(form, state);
		waveLabel.run();
		return RET_CONTINUE;
	}

	/*
	 Wave Case Labels	0
	 Wave/Order.
	 Validate Wavekey
	 WAVE
	 Batch Case Labels	1
	 Wave/Order
	 Validate Wavekey
	 BATCH
	 CL B A				2
	 Wave/Order & Assn
	 Wavekey and Assn
	 WAVE_ASSIGNMENT
	 CL f OA				3
	 Wave/Order & Assn
	 Orderkey & Assn
	 ORD_ASSIGNMENT
	 CL f OO				4
	 Wave/Order
	 Orderkey
	 ORD
	 */

	class WaveLabels
	{
		protected RuntimeFormInterface form;

		protected StateInterface state;

		UnitOfWorkBean uowb;

		int reportID;

		String keyType;
		
		String key;
		
		String assn = "";
		
		Object printer;
		
		Object rfidPrinter;

		public WaveLabels(RuntimeFormInterface f, StateInterface st)
		{
			form = f;
			state = st;
			uowb = state.getDefaultUnitOfWork();

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

		public void run() throws UserException, EpiDataException
		{

			reportID = Integer.parseInt(form.getFormWidgetByName("WAVELABELS").getValue().toString());
			key = "";
			assn = "";

			//Validation
			requiredPrinters();
			validateReportNumber();
			countValidation("COUNT");

			key = form.getFormWidgetByName("WAVEORDER").getDisplayValue().toUpperCase();
			printer = form.getFormWidgetByName("PRINTER").getValue();
			rfidPrinter = form.getFormWidgetByName("RFIDPRINTER").getValue();
			String count = form.getFormWidgetByName("COUNT").getDisplayValue();
			
			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
			Array params = new Array();
			params.add(new TextData(keyType)); //keytype
			params.add(new TextData(key)); //key
			params.add(new TextData(printer.toString())); //std printer name
			params.add(new TextData(rfidPrinter.toString())); //rfid printer name
			params.add(new TextData(count)); //copies
			params.add(new TextData(assn)); //assignment
			
			actionProperties.setProcedureParameters(params);
			actionProperties.setProcedureName("NSPPrintWaveLabel");
			_log.debug("LOG_DEBUG_EXTENSION", flattenParams(params), SuggestedCategory.NONE);
			try
			{
				EXEDataObject results = WmsWebuiActionsImpl.doAction(actionProperties);
			} catch (WebuiException e)
			{
				throw new UserException(e.getMessage(), new Object[] {});
			}

		}

		private void requiredPrinters() throws UserException
		{
			Object std = form.getFormWidgetByName("PRINTER").getValue();
			Object rfid = form.getFormWidgetByName("RFIDPRINTER").getValue();
			if (isEmpty(std) && isEmpty(rfid))
			{
				//throw exception
				throw new UserException("WMEXP_WL_REQPRINT", new Object[] {});
			}
			
		}

		private void validateReportNumber() throws UserException, EpiDataException
		{
			if (reportID == 0)
			{
				requiredField("WAVEORDER");
				validateWaveKey();
				keyType = "WAVE";
			}
			else if (reportID == 1)
			{
				requiredField("WAVEORDER");
				validateWaveKey();
				keyType = "BATCH";
			}
			else if (reportID == 2)
			{
				requiredField("WAVEORDER");
				requiredField("ASSNNUMBER");
				validateWaveKey();
				validateAssnNumber();
				keyType = "WAVE_ASSIGNMENT";
				assn = form.getFormWidgetByName("ASSNNUMBER").getDisplayValue();
			}
			else if (reportID == 3)
			{
				requiredField("WAVEORDER");
				requiredField("ASSNNUMBER");
				validateOrderKey();
				validateAssnNumber();
				keyType = "ORD_ASSIGNMENT";
				assn = form.getFormWidgetByName("ASSNNUMBER").getDisplayValue();
			}
			else if (reportID == 4)
			{
				requiredField("WAVEORDER");
				validateOrderKey();
				keyType = "ORD";
			}
			else
			{
				//throw exception 
				String[] parameters = new String[1];
				parameters[0] = removeTrailingColon(retrieveLabel("WAVELABELS"));
				throw new UserException("WMEXP_REQFIELD", parameters);
			}
		}

		private void countValidation(String attributeName) throws UserException
		{
			String attributeValue = form.getFormWidgetByName(attributeName).getDisplayValue();
			if (isEmpty(attributeValue))
			{
				return;
			}
			double value = NumericValidationCCF.parseNumber(attributeValue);
			if (Double.isNaN(value))
			{
				String[] parameters = new String[2];
				parameters[0] = retrieveLabel(attributeName);
				parameters[1] = attributeValue;
				throw new UserException("WMEXP_FORM_NON_NUMERIC", parameters);
			}

			if (value < 1 || value > 100)
			{
				String[] parameters = new String[2];
				parameters[0] = retrieveLabel(attributeName);
				parameters[1] = attributeValue;
				throw new UserException("WMEXP_SSCC_RANGE", parameters);
			}

		}

		private void validateAssnNumber() throws EpiDataException, UserException
		{
			String attributeName = "ASSNNUMBER";
			String attributeValue = form.getFormWidgetByName(attributeName).getDisplayValue().toUpperCase();
			String bio = "wm_useractivity";
			String attribute = "ASSIGNMENTNUMBER";
			
			String query1 = bio + "." + attribute + " = '" + attributeValue + "'";
			_log.debug("LOG_DEBUG_EXTENSION_WaveLabels", query1, SuggestedCategory.NONE);
			BioCollectionBean results1 = uowb.getBioCollectionBean(new Query(bio, query1, null));
			if (results1.size() == 0)
			{
				//valid assn
				String[] parameters = new String[2];
				parameters[0] = retrieveLabel(attributeName);
				parameters[1] = form.getFormWidgetByName(attributeName).getDisplayValue();
				throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
			}

			String query2 = bio + "." + attribute + " = '" + attributeValue + "' AND " + bio + ".STATUS < '9'";
			BioCollectionBean results2 = uowb.getBioCollectionBean(new Query(bio, query2, null));
			_log.debug("LOG_DEBUG_EXTENSION_WaveLabels", query2, SuggestedCategory.NONE);
			if (results2.size() == 0)
			{
				//status
				String[] parameters = new String[2];
				parameters[0] = retrieveLabel(attributeName);
				parameters[1] = form.getFormWidgetByName(attributeName).getDisplayValue();
				throw new UserException("WMEXP_WL_STATUS", parameters);

			}

			String query3 = bio + "." + attribute + " = '" + attributeValue + "' AND (" + bio + ".USERID = '" + loginId
					+ "' OR " + bio + ".USERID IS NULL)";
			_log.debug("LOG_DEBUG_EXTENSION_WaveLabels", query3, SuggestedCategory.NONE);
			BioCollectionBean results3 = uowb.getBioCollectionBean(new Query(bio, query3, null));
			if (results3.size() == 0)
			{
				//belongs to user
				String[] parameters = new String[2];
				parameters[0] = retrieveLabel(attributeName);
				parameters[1] = form.getFormWidgetByName(attributeName).getDisplayValue();
				throw new UserException("WMEXP_WL_USER", parameters);

			}

		}

		private void validateOrderKey() throws EpiDataException, UserException
		{

			if (verifyAttribute("WAVEORDER", "wm_orders", "ORDERKEY") == false)
			{
				//throw exception
				String[] parameters = new String[2];
				parameters[0] = retrieveLabel("WAVEORDER");
				parameters[1] = form.getFormWidgetByName("WAVEORDER").getDisplayValue();
				throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
			}
		}

		private void validateWaveKey() throws EpiDataException, UserException
		{
			if (verifyAttribute("WAVEORDER", "wm_wave", "WAVEKEY") == false)
			{
				//throw exception
				String[] parameters = new String[2];
				parameters[0] = retrieveLabel("WAVEORDER");
				parameters[1] = form.getFormWidgetByName("WAVEORDER").getDisplayValue();
				throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
			}
		}

		boolean verifyAttribute(String name, String bio, String attribute) throws EpiDataException, UserException
		{
			String attributeValue = form.getFormWidgetByName(name).getDisplayValue().toUpperCase();
			String query = bio + "." + attribute + " = '" + attributeValue + "'";
			_log.debug("LOG_DEBUG_EXTENSION_WaveLabels", query, SuggestedCategory.NONE);
			BioCollectionBean results = uowb.getBioCollectionBean(new Query(bio, query, null));
			if (results.size() == 1)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}

	private String flattenParams(Array params)
	{
		String list = "Parameters: ";
		for (int i = 1; i <= params.size(); i++)
		{
			list += i + " - " + params.get(i) + ", ";
		}
		return list;
	}

	boolean isEmpty(Object attributeValue)
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
