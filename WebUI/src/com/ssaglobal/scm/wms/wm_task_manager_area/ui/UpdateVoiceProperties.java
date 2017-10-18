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
package com.ssaglobal.scm.wms.wm_task_manager_area.ui;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epiphany.shr.data.bio.BioAttributeTypes;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationInsertImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationUpdateImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class UpdateVoiceProperties extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	private static final int MAX_NUMBER_OF_WORK_ID = 99;

	private static final int MAX_SPOKEN_CONTAINER_VALIDATION = 5;

	protected static ILoggerCategory _log = LoggerFactory.getInstance(PreRenderFormSlot.class);

	protected String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);

	protected MetaDataAccess mda = MetaDataAccess.getInstance();

	protected LocaleInterface locale = mda.getLocale(userLocale);

	private static final String ERROR_MESSAGE_NONINT = "WMEXP_NON_INTEGER";

	private static final String ERROR_MESSAGE_LTZ = "WMEXP_LESS_THAN_ZERO";

	private static final String ERROR_MESSAGE_NGTZ = "WMEXP_NOT_GREATER_THAN_ZERO";

	private static final String ERROR_MESSAGE_LTE = "WMEXP_ZONE_LESSTHANEXPECTED";

	private static final String ERROR_MESSAGE_INVALID_EFZ = "WMEXP_ZONE_INVALID_EFZ";

	private static final String ERROR_MESSAGE_LTM = "WMEXP_MAX_VALUES";

	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException, UserException
	{
		_log.debug("LOG_DEBUG_EXTENSION_AREA", "Executing UpdateVoiceProperties", 100L);

		String shellSlot2 = "list_slot_2";
		String newVal = null;

		StateInterface state = context.getState();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm(); //get the toolbar
		RuntimeFormInterface shellForm = toolbar.getParentForm(state); //get the Shell form

		SlotInterface detailSlot = shellForm.getSubSlot(shellSlot2); //Set slot 2
		RuntimeFormInterface tabGroupForm = state.getRuntimeForm(detailSlot, null); //Get the form at slot2
		SlotInterface tabGroupSlot = tabGroupForm.getSubSlot("tbgrp_slot");
		RuntimeFormInterface detailFormSubSlot = state.getRuntimeForm(tabGroupSlot, "tab 0");
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailFormSubSlot.getSlot(), null);
		RuntimeFormInterface subSlotForm = state.getRuntimeForm(detailForm.getSubSlot("wm_task_manager_area_form_slot"), null);
		RuntimeFormInterface areaForm = state.getRuntimeForm(tabGroupSlot, "tab 1");
		RuntimeFormInterface locationSchemaForm = state.getRuntimeForm(tabGroupSlot, "tab 2");

		DataBean areaFocus = areaForm.getFocus();

		//		Object currAisleVal = areaFocus.getValue("CURRENTAISLE");
		//		Object currSlotVal = areaFocus.getValue("CURRENTSLOT");
		//		Object maxAssignmentsVal = areaFocus.getValue("MAXNUMWORKIDS");
		//
		//		boolean currAisleIsNull = checkNull(currAisleVal);
		//		boolean currSlotIsNull = checkNull(currSlotVal);
		//		boolean maxAssignmentsIsNull = checkNull(maxAssignmentsVal);
		//
		//		if (!currAisleIsNull)
		//		{
		//			newVal = checkXValidation(currAisleVal, "Current Bay Length ");
		//			areaFocus.setValue("CURRENTAISLE", newVal);
		//		}
		//		if (!currSlotIsNull)
		//		{
		//			newVal = checkXValidation(currSlotVal, "Current Location Length ");
		//			areaFocus.setValue("CURRENTSLOT", newVal);
		//		}
		//		if (!maxAssignmentsIsNull)
		//		{
		//			validateNegNumber(maxAssignmentsVal);
		//		}

		//Area Header Validation
		String errorTextMax = "", errorTextNN = "";
		RuntimeFormWidgetInterface spokenContainerValidation = areaForm.getFormWidgetByName("SPOKENCONTAINERLENGTH");
		RuntimeFormWidgetInterface maxNumOfWorkID = areaForm.getFormWidgetByName("MAXNUMWORKIDS");
		errorTextNN = nonNegative(spokenContainerValidation, errorTextNN);
		errorTextNN = nonNegative(maxNumOfWorkID, errorTextNN);
		errorTextMax = lessThan(spokenContainerValidation, MAX_SPOKEN_CONTAINER_VALIDATION, errorTextMax);
		errorTextMax = lessThan(maxNumOfWorkID, MAX_NUMBER_OF_WORK_ID, errorTextMax);
		if (!(errorTextMax.equals("")))
		{
			throw new FormException(ERROR_MESSAGE_LTM, new String[] { errorTextMax });
		}
		if (!(errorTextNN.equals("")))
		{
			throw new FormException(ERROR_MESSAGE_LTZ, new String[] { errorTextNN });
		}

		//Location Schema Validation
		String errorText = "";
		RuntimeFormWidgetInterface aisleStart = locationSchemaForm.getFormWidgetByName("AISLESTART");
		RuntimeFormWidgetInterface aisleEnd = locationSchemaForm.getFormWidgetByName("AISLEEND");
		RuntimeFormWidgetInterface bayStart = locationSchemaForm.getFormWidgetByName("BAYSTART");
		RuntimeFormWidgetInterface bayEnd = locationSchemaForm.getFormWidgetByName("BAYEND");
		RuntimeFormWidgetInterface slotStart = locationSchemaForm.getFormWidgetByName("SLOTSTART");
		RuntimeFormWidgetInterface slotEnd = locationSchemaForm.getFormWidgetByName("SLOTEND");
		errorText = greaterThanZero(aisleStart, errorText);
		errorText = greaterThanValue(aisleEnd, aisleStart, errorText);
		errorText = greaterThanZero(bayStart, errorText);
		//jp.answerlink.275614.begin
		errorText = greaterThanValue(bayEnd, bayStart,  errorText);
		//jp.answerlink.275614.end
		errorText = greaterThanZero(slotStart, errorText);
		errorText = greaterThanValue(slotEnd, slotStart, errorText);
		if (!(errorText.equals("")))
		{
			throw new FormException(ERROR_MESSAGE_NGTZ, new String[] { errorText });
		}


		saveAreaHeader(subSlotForm, areaForm, locationSchemaForm, areaFocus);
//WM92_VoiceChanges.b
		updateDefaultVoiceArea(areaFocus);
//WM92_VoiceChanges.e

		_log.debug("LOG_DEBUG_EXTENSION_AREA", "Exiting UpdateVoiceProperties", 100L);

		return RET_CONTINUE;
	}

	private void saveAreaHeader(RuntimeFormInterface subSlotForm, RuntimeFormInterface areaForm, RuntimeFormInterface locationSchemaForm, DataBean areaFocus) throws UserException
	{
		String val;
		boolean widgetIsNull;
		String areaKeyStr;
		RuntimeFormWidgetInterface areaKey;
		if (subSlotForm.isListForm())
		{
			//context.getSourceWidget().setProperty(RuntimeFormInterface.PROP_READONLY, Boolean.TRUE);
			areaKeyStr = areaFocus.getValue("AREAKEY").toString();
			areaKey = subSlotForm.getFormWidgetByName("AREAKEY");
			_log.debug("LOG_SYSTEM_OUT", "\n\n*** IsListForm: " + areaKeyStr, 100L);
			//throw new UserException("SKU_VALIDATION", new Object[1]);
		}
		else
		{
			DataBean focus = subSlotForm.getFocus();
			areaKey = subSlotForm.getFormWidgetByName("AREAKEY");
			areaKeyStr = focus.getValue("AREAKEY").toString();
		}//new end else

		String newArea = checkAreaKey(areaKeyStr);
		areaKey.setDisplayValue(newArea);
		areaFocus.setValue("AREAKEY", newArea);

		String updateSql = "UPDATE area SET ";
		String insertSql = "INSERT INTO area ";
		String insertAtt = "(";
		String insertVals = "(";
		Object temp = null;

		// Build SQL
		//	if(exists)
		//		{
//WM92_VoiceChanges.b
		Iterator areaWidgets = areaForm.getFormWidgets();
		while (areaWidgets.hasNext())
		{
			Object obj = areaWidgets.next();
			RuntimeFormWidgetInterface headerWidget = (RuntimeFormWidgetInterface) obj;
//			if (!headerWidget.getName().equals("LocDesc") && !headerWidget.getName().equals("AisleDesc") && !headerWidget.getName().equals("placeholder"))
			if (!headerWidget.getName().equals("common_voice_label") && !headerWidget.getName().equals("nonetask_label"))
			{
				areaFocus.setValue(headerWidget.getName(), (areaFocus.getValue(headerWidget.getName())));
				temp = areaFocus.getValue(headerWidget.getName());
				widgetIsNull = checkNull(temp);
				if (!widgetIsNull)
				{
					//val= headerWidget.getValue().toString();
					val = areaFocus.getValue(headerWidget.getName()).toString();
					updateSql = updateSql + headerWidget.getName() + "='" + val + "', ";
					insertAtt = insertAtt + headerWidget.getName() + ", ";
					insertVals = insertVals + "'" + val + "',";
				}
			}
		} 
		//end while
		//	}//end if
		Iterator locSchemaWidgets = locationSchemaForm.getFormWidgets();
		while (locSchemaWidgets.hasNext())
		{
			Object obj = locSchemaWidgets.next();
			RuntimeFormWidgetInterface headerWidget = (RuntimeFormWidgetInterface) obj;
			String type = headerWidget.getType();
			if (!("label".equals(type) || "caption".equals(type)))
			{
				areaFocus.setValue(headerWidget.getName(), (areaFocus.getValue(headerWidget.getName())));
				temp = areaFocus.getValue(headerWidget.getName());
				widgetIsNull = checkNull(temp);
				if (!widgetIsNull)
				{
					//val= headerWidget.getValue().toString();
					val = areaFocus.getValue(headerWidget.getName()).toString();
					updateSql = updateSql + headerWidget.getName() + "='" + val + "', ";
					insertAtt = insertAtt + headerWidget.getName() + ", ";
					insertVals = insertVals + "'" + val + "',";
				}
			}
		}

		boolean exists = recordExists("area", newArea);

		if (exists)
		{
			//update
			String test = updateSql.substring(updateSql.length() - 2, updateSql.length() - 1);
			if (test.equals(","))
			{
				updateSql = updateSql.substring(0, updateSql.length() - 2) + updateSql.substring(updateSql.length() - 1);
				updateSql = updateSql + "WHERE AREAKEY=" + "'" + newArea + "'";
				EXEDataObject dataObject;
				try
				{
					dataObject = WmsWebuiValidationUpdateImpl.update(updateSql);
				} catch (DPException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else
		{
			//insert
			insertAtt = insertAtt + "AREAKEY)";
			//insertAtt= insertAtt.substring(0, insertAtt.length()-2)+ ")";
			insertVals = insertVals + "'" + newArea + "'" + ")";
			//insertVals= insertVals.substring(0, insertVals.length()-1)+ ")";
			insertSql = insertSql + insertAtt + " VALUES " + insertVals;
			_log.debug("LOG_SYSTEM_OUT", "\n\n****Insert Sql: " + insertSql, 100L);

			EXEDataObject dataInsertObject;
			try
			{
				dataInsertObject = WmsWebuiValidationInsertImpl.insert(insertSql);
			} catch (DPException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
//WM92_VoiceChanges.b
	private void updateDefaultVoiceArea(DataBean areaFocus){
		if ((!checkNull(areaFocus.getValue("DEFAULTVOICEAREA")) && areaFocus.getValue("DEFAULTVOICEAREA").equals("1"))){
			String updateStr = "UPDATE area SET DEFAULTVOICEAREA = '0' WHERE DEFAULTVOICEAREA = '1' AND AREAKEY != '"+ areaFocus.getValue("AREAKEY").toString()+"'";
			EXEDataObject dataObject;
			try
			{
				dataObject = WmsWebuiValidationUpdateImpl.update(updateStr);
			} catch (DPException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
//WM92_VoiceChanges.e
	
	private void validateNegNumber(Object maxAssignmentsVal) throws UserException
	{
		// TODO Auto-generated method stub

		double widgetValue = Double.parseDouble(maxAssignmentsVal.toString());

		// If value < 0, return RET_CANCEL
		if (widgetValue < 0)
		{
			String[] parameters = new String[2];

			parameters[0] = "Maximum # of Assignments";
			parameters[1] = maxAssignmentsVal.toString();

			throw new UserException("WMEXP_NONNEG", parameters);
		}

	}

	private String checkXValidation(Object value, String attName) throws UserException
	{
		// TODO Auto-generated method stub
		String upperVal = null;
		;
		//headerForm.getLabel("title", locale);
		String upper = value.toString().trim();
		for (int i = 0; i < upper.length(); i++)
		{
			if (upper.matches("[a-zA-Z0-9]+"))
			{
				if (upper.toUpperCase().charAt(i) != 'X')
				{
					String[] param = new String[1];
					param[0] = attName;
					throw new UserException("WMEXP_MUST_BE_X", param);
				}
				else
				{
					upperVal = upper.toUpperCase();
				}
			}
			else
			{
				String[] param = new String[1];
				param[0] = attName;
				throw new UserException("WMEXP_MUST_BE_X", param);
			}
		}
		return upperVal;
	}

	private String checkAreaKey(String keyStr) throws UserException
	{

		// TODO Auto-generated method stub
		/* kkuchipu SCM-00000-04386
		 *  commented if loop entirely and added below line to convert Area to upper string. 
		 *  User will be able to enter special characters in Area Key.
		 */
		keyStr = keyStr.toUpperCase();
		/*
		if(keyStr.matches("[a-zA-Z0-9]+"))
		{
			keyStr= keyStr.toUpperCase();
		}
		else 
		{
		 String [] param = new String[2];
		 param[0] = keyStr;
		 param[1] = "Area Key";
		 throw new UserException("WMEXP_SP_CHARS", param);
		}
		*/

		return keyStr;
	}

	private boolean checkNull(Object obj)
	{
		// TODO Auto-generated method stub
		if (obj == null)
		{
			return true;
		}
		else if (obj.toString().matches("\\s*"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private boolean recordExists(String table, String areaKeyStr)
	{
		// TODO Auto-generated method stub
		String sql = "select * from " + table + " where AREAKEY='" + areaKeyStr + "'";
		EXEDataObject dataObject;
		try
		{
			dataObject = WmsWebuiValidationSelectImpl.select(sql);
			if (dataObject.getCount() > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		} catch (DPException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public String nonNegative(RuntimeFormWidgetInterface widget, String text) throws FormException
	{
		String widgetLabel = colonStrip(readLabel(widget));
		String number = widget.getDisplayValue();
		if (number != null)
		{
			int value = Integer.parseInt(number);
			if (value < 0)
			{
				text = addLabel(text, widgetLabel);
			}
		}
		return text;
	}

	public String greaterThanValue(RuntimeFormWidgetInterface widget, RuntimeFormWidgetInterface widget2, String text) throws FormException
	{
		String[] parameter = new String[2];
		String widgetLabel = colonStrip(readLabel(widget));
		String number = widget.getDisplayValue();
		String number2 = widget2.getDisplayValue();
		if (number != null)
		{
			if (number.matches(".*\\..*"))
			{
				parameter[0] = widgetLabel;
				throw new FormException(ERROR_MESSAGE_NONINT, parameter);
			}
			int value = Integer.parseInt(number);
			int compareTo = Integer.parseInt(number2);
			if (value < compareTo)
			{
				parameter[0] = widgetLabel;
				parameter[1] = colonStrip(readLabel(widget2));
				throw new FormException(ERROR_MESSAGE_LTE, parameter);
			}
			if (value < 0)
			{
				text = addLabel(text, widgetLabel);
			}
		}
		return text;
	}

	private String lessThan(RuntimeFormWidgetInterface widget, final int max, String text) throws FormException
	{
		String widgetLabel = colonStrip(readLabel(widget)) + " (" + max + ")";
		String number = widget.getDisplayValue();

		if (number != null)
		{
			int value = Integer.parseInt(number);
			if (value > max)
			{
				text = addLabel(text, widgetLabel);
			}
		}
		return text;
	}

	//Validate value is greater than zero
	public String greaterThanZero(RuntimeFormWidgetInterface widget, String text) throws FormException
	{
		String[] parameter = new String[1];
		String widgetLabel = colonStrip(readLabel(widget));
		String number = widget.getDisplayValue();

		if (widget.getAttributeType() == BioAttributeTypes.INT_TYPE)
		{
			//Widget value is of type INT
			if (number != null)
			{
				if (number.matches(".*\\..*"))
				{
					//Input value has decimals
					parameter[0] = widgetLabel;
					throw new FormException(ERROR_MESSAGE_NONINT, parameter);
				}
				int value = Integer.parseInt(number);
				if (value <= 0)
				{
					text = addLabel(text, widgetLabel);
				}
			}
		}
		else
		{
			//Widget value allows decimals
			if (number != null)
			{
				float value = Float.parseFloat(number);
				if (value <= 0)
				{
					text = addLabel(text, widgetLabel);
				}
			}
		}
		return text;
	}

	//Read Widget Label for error message
	public String readLabel(RuntimeFormWidgetInterface widget)
	{
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widget.getLabel("label", locale);
	}

	//Append widget label for error message
	public String addLabel(String text, String widgetLabel)
	{
		if (text.equals(""))
		{
			text += widgetLabel;
		}
		else
		{
			text += ", " + widgetLabel;
		}
		return text;
	}

	//Remove colon from widget labels for error message
	public String colonStrip(String label)
	{
		Pattern pattern = Pattern.compile("\\:");
		Matcher matcher = pattern.matcher(label);
		return matcher.replaceAll("");
	}

}
