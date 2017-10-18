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

package com.ssaglobal.scm.wms.wm_pack.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.metadata.objects.EpnyLocale;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.EpnyControllerState;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RenderableObject;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeSlot;

import com.ssaglobal.scm.wms.common.ui.UOMMapping;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.wm_item.ui.Tab;
import com.ssaglobal.scm.wms.wm_table_validation.ccf.NumericValidationCCF;
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

public class PackSaveValidationAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	HashMap packMapping;

	protected static ILoggerCategory _log = LoggerFactory.getInstance(PackSaveValidationAction.class);

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

		_log.debug("LOG_SYSTEM_OUT","\n\t" + "Entering New Pack Save Validation" + "\n",100L);
		_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of PackSaveValidationAction", SuggestedCategory.NONE);
		

		packMapping = new HashMap();
		packMapping.put("QTY", "PACKUOM3");
		packMapping.put("CASECNT", "PACKUOM1");
		packMapping.put("INNERPACK", "PACKUOM2");
		packMapping.put("PALLET", "PACKUOM4");
		packMapping.put("CUBE", "PACKUOM5");
		packMapping.put("GROSSWGT", "PACKUOM6");
		packMapping.put("NETWGT", "PACKUOM7");
		packMapping.put("OTHERUNIT1", "PACKUOM8");
		packMapping.put("OTHERUNIT2", "PACKUOM9");

		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);

		StateInterface state = context.getState();
		RuntimeFormInterface shellForm = retrieveShellForm(state);
		//Detail
		SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
		_log.debug("LOG_DEBUG_EXTENSION", "\n3'''Current form  = " + detailForm.getName(), SuggestedCategory.NONE);
		
		SlotInterface tabGroupSlot = detailForm.getSubSlot("tbgrp_slot");
		if (isNull(tabGroupSlot))
		{
			return RET_CANCEL;
		}

		boolean isInsert = false;
		DataBean detailFocus = detailForm.getFocus();
		if (detailFocus instanceof BioBean)
		{
			isInsert = false;
			detailFocus = (BioBean) detailFocus;
		}
		else if (detailFocus instanceof QBEBioBean)
		{
			isInsert = true;
			detailFocus = (QBEBioBean) detailFocus;
		}
		
		
		
		
		
		HttpSession session = state.getRequest().getSession();

		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		String isEnterprise = null;
		try
		{
			isEnterprise = userContext.get(SetIntoHttpSessionAction.DB_ISENTERPRISE).toString();
		} catch (java.lang.NullPointerException e)
		{
			isEnterprise = session.getAttribute(SetIntoHttpSessionAction.DB_ISENTERPRISE).toString();
		}

		if ("1".equalsIgnoreCase(isEnterprise)){

			//Tab 0 - Pack Tab Validations
			{
	
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Pack Tab Validations", SuggestedCategory.NONE);
				
				RuntimeFormInterface packTabForm = state.getRuntimeForm(tabGroupSlot, "tab 0");
	
				if (isInsert)
				{
					if (detailFocus.getValue("PACKKEY") == null)
					{
						String[] params = new String[1];
						params[0] = removeTrailingColon(packTabForm.getFormWidgetByName("PACKKEY").getLabel("label", locale));
						throw new UserException("WMEXP_BLANK_STORER", params);
					}
					//prevent duplicates
					String pack = detailFocus.getValue("PACKKEY").toString().toUpperCase();
					String query = "SELECT * FROM PACK WHERE PACKKEY = '" + pack + "'";
					_log.debug("LOG_DEBUG_EXTENSION", "///QUERY " + query, SuggestedCategory.NONE);
				
					EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
					if (results.getRowCount() >= 1)
					{
						String[] parameters = new String[2];
						parameters[0] = packTabForm.getFormWidgetByName("PACKKEY").getLabel("label", locale);
						parameters[1] = pack;
						//parameters[2] = packTabForm.getLabel("title", locale);
						throw new UserException("WMEXP_DEFAULT_DUPLICATE", parameters);
					}
				}
				packTabValidation("QTY", detailFocus, packTabForm, locale);
				packTabValidation("CASECNT", detailFocus, packTabForm, locale);
				packTabValidation("INNERPACK", detailFocus, packTabForm, locale);
				packTabValidation("PALLET", detailFocus, packTabForm, locale);
				packTabValidation("CUBE", detailFocus, packTabForm, locale);
				packTabValidation("GROSSWGT", detailFocus, packTabForm, locale);
				packTabValidation("NETWGT", detailFocus, packTabForm, locale);
				packTabValidation("OTHERUNIT1", detailFocus, packTabForm, locale);
				packTabValidation("OTHERUNIT2", detailFocus, packTabForm, locale);
	
				//prevent duplicate UoMs
				{
					//Collection
					ArrayList duplicateList = new ArrayList();
					for (int i = 1; i <= 9; i++)
					{
						String widgetName = "PACKUOM" + String.valueOf(i);
						Object packUOM = detailFocus.getValue(widgetName);
						if (!isEmpty(packUOM))
						{
							_log.debug("LOG_DEBUG_EXTENSION", "UOM " + packUOM, SuggestedCategory.NONE);
						
							//if packUOM is not null try to add it to the duplicateList
							//if a key exists, throw exception
							if (duplicateList.contains(packUOM.toString()))
							{
								//throw exception
								String[] parameters = new String[2];
								parameters[0] = packTabForm.getFormWidgetByName(widgetName).getDisplayValue();
								parameters[1] = packTabForm.getLabel("title", locale);
								_log.debug("LOG_DEBUG_EXTENSION", "~!~! " + parameters[0] + " " + parameters[1], SuggestedCategory.NONE);
							
								throw new UserException("WMEXP_PACK_DUPLICATE_UOM", parameters);
	
							}
							else
							{
								duplicateList.add(packUOM.toString());
							}
						}
					}
				}
			}
	
			//Tab 1 - Master Unit Tab Validations
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Master Unit Tab Validations", SuggestedCategory.NONE);
				RuntimeFormInterface packDimensionForm = state.getRuntimeForm(tabGroupSlot, "tab 1");
				
	
					_log.debug("LOG_DEBUG_EXTENSION", "\n5'''Current form " + packDimensionForm.getName(), SuggestedCategory.NONE);
				
					nonnegativeValidation("LENGTHUOM3", detailFocus, packDimensionForm, locale);
					nonnegativeValidation("WIDTHUOM3", detailFocus, packDimensionForm, locale);
					nonnegativeValidation("HEIGHTUOM3", detailFocus, packDimensionForm, locale);
	
	
	
	
			//tab 1 Case Tab Validations
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Case Tab Validations", SuggestedCategory.NONE);
			
					_log.debug("LOG_DEBUG_EXTENSION", "\n5'''Current form " + packDimensionForm.getName(), SuggestedCategory.NONE);
					
					nonnegativeValidation("LENGTHUOM1", detailFocus, packDimensionForm, locale);
					nonnegativeValidation("WIDTHUOM1", detailFocus, packDimensionForm, locale);
					nonnegativeValidation("HEIGHTUOM1", detailFocus, packDimensionForm, locale);
	
	
			//		Tab 1 - Innerpack Tab Validations
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Innerpack Tab Validations", SuggestedCategory.NONE);
			
					_log.debug("LOG_DEBUG_EXTENSION", "\n5'''Current form " + packDimensionForm.getName(), SuggestedCategory.NONE);
					
					nonnegativeValidation("LENGTHUOM2", detailFocus, packDimensionForm, locale);
					nonnegativeValidation("WIDTHUOM2", detailFocus, packDimensionForm, locale);
					nonnegativeValidation("HEIGHTUOM2", detailFocus, packDimensionForm, locale);
	
	
	
			// Tab 1 - Pallet Tab Validations
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Pallet Tab Validations", SuggestedCategory.NONE);
				
					_log.debug("LOG_DEBUG_EXTENSION", "\n5'''Current form " + packDimensionForm.getName(), SuggestedCategory.NONE);
					
					nonnegativeValidation("LENGTHUOM4", detailFocus, packDimensionForm, locale);
					nonnegativeValidation("WIDTHUOM4", detailFocus, packDimensionForm, locale);
					nonnegativeValidation("HEIGHTUOM4", detailFocus, packDimensionForm, locale);
	
					_log.debug("LOG_DEBUG_EXTENSION", "\n5'''Current form " + packDimensionForm.getName(), SuggestedCategory.NONE);
					
					nonnegativeValidation("PALLETHI", detailFocus, packDimensionForm, locale);
					nonnegativeValidation("PALLETTI", detailFocus, packDimensionForm, locale);
	
					_log.debug("LOG_DEBUG_EXTENSION", "\n5'''Current form " + packDimensionForm.getName(), SuggestedCategory.NONE);
					
					nonnegativeValidation("PALLETWOODLENGTH", detailFocus, packDimensionForm, locale);
					nonnegativeValidation("PALLETWOODWIDTH", detailFocus, packDimensionForm, locale);
					nonnegativeValidation("PALLETWOODHEIGHT", detailFocus, packDimensionForm, locale);
	
	
	
			// Tab 1 - Layer Tab Validations
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Layer Tab Validations", SuggestedCategory.NONE);
				
					_log.debug("LOG_DEBUG_EXTENSION", "\n5'''Current form " + packDimensionForm.getName(), SuggestedCategory.NONE);
					
					nonnegativeValidation("LENGTHUOM8", detailFocus, packDimensionForm, locale);
					nonnegativeValidation("WIDTHUOM8", detailFocus, packDimensionForm, locale);
					nonnegativeValidation("HEIGHTUOM8", detailFocus, packDimensionForm, locale);
	
	
	
			// Tab 1 - Feet Tab Validations
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Feet Tab Validations", SuggestedCategory.NONE);
			
					_log.debug("LOG_DEBUG_EXTENSION", "\n5'''Current form " + packDimensionForm.getName(), SuggestedCategory.NONE);
					
					nonnegativeValidation("LENGTHUOM9", detailFocus, packDimensionForm, locale);
					nonnegativeValidation("WIDTHUOM9", detailFocus, packDimensionForm, locale);
					nonnegativeValidation("HEIGHTUOM9", detailFocus, packDimensionForm, locale);
	
		}else{//it is warehouse user		
			RuntimeFormInterface rfidForm = state.getRuntimeForm(tabGroupSlot, "tab 2");	
			if (!(isNull(rfidForm)))
			{
				_log.debug("LOG_DEBUG_EXTENSION", "\n5'''Current form " + rfidForm.getName(), SuggestedCategory.NONE);
				between0and7Validation("FilterValueUOM1", detailFocus, rfidForm, locale);
				between0and8Validation("IndicatorDigitUOM1", detailFocus, rfidForm, locale);
				between0and7Validation("FilterValueUOM2", detailFocus, rfidForm, locale);
				between0and8Validation("IndicatorDigitUOM2", detailFocus, rfidForm, locale);
				
				between0and7Validation("FilterValueUOM3", detailFocus, rfidForm, locale);
				between0and8Validation("IndicatorDigitUOM3", detailFocus, rfidForm, locale);
				between0and7Validation("FilterValueUOM4", detailFocus, rfidForm, locale);
				between0and8Validation("IndicatorDigitUOM4", detailFocus, rfidForm, locale);
				between0and7Validation("FilterValueUOM8", detailFocus, rfidForm, locale);
				between0and8Validation("IndicatorDigitUOM8", detailFocus, rfidForm, locale);
				between0and7Validation("FilterValueUOM9", detailFocus, rfidForm, locale);
				between0and8Validation("IndicatorDigitUOM9", detailFocus, rfidForm, locale);
	
				between0and7Validation("FilterValueUOM5", detailFocus, rfidForm, locale);
				between0and8Validation("IndicatorDigitUOM5", detailFocus, rfidForm, locale);
				between0and7Validation("FilterValueUOM6", detailFocus, rfidForm, locale);
				between0and8Validation("IndicatorDigitUOM6", detailFocus, rfidForm, locale);
				between0and7Validation("FilterValueUOM7", detailFocus, rfidForm, locale);
				between0and8Validation("IndicatorDigitUOM7", detailFocus, rfidForm, locale);
			
			}
		}		
		return RET_CONTINUE;
	}

	protected String removeTrailingColon(String label)
	{
		if (label.endsWith(":"))
		{
			label = label.substring(0, label.length() - 1);
		}
		return label;
	}

	private void between0and8Validation(String widgetName, DataBean detailFocus, RuntimeFormInterface form, LocaleInterface locale) throws EpiDataException, UserException
	{

		_log.debug("LOG_DEBUG_EXTENSION", "%^& " + widgetName + " @ " + form.getName(), SuggestedCategory.NONE);
		
		Object widgetValue = detailFocus.getValue(widgetName);
		//if null, return
		if (isNull(widgetValue))
		{
			return;
		}
		else
		{
			//Check <0 and >8
			_log.debug("LOG_DEBUG_EXTENSION", "&& " + form.getName(), SuggestedCategory.NONE);
			
			String label = form.getFormWidgetByName(widgetName).getLabel("label", locale);
			String tabName = form.getLabel("title", locale);
			double tempValue = NumericValidationCCF.parseNumber(widgetValue.toString());
			if (tempValue < 0 || tempValue > 8)
			{
				//throw exception
				//_log.debug("LOG_DEBUG_EXTENSION", "^%^^ " + tempValue + " - " + label + " - " + form.getName(), SuggestedCategory.NONE);;
				String[] parameters = new String[3];
				parameters[0] = widgetValue.toString();
				parameters[1] = label;
				parameters[2] = tabName; //tab name?
				throw new UserException("WMEXP_PACK_0TO8", "WMEXP_PACK_0TO8", parameters);
			}
		}
	}

	private void between0and7Validation(String widgetName, DataBean detailFocus, RuntimeFormInterface form, LocaleInterface locale) throws EpiDataException, UserException
	{

		_log.debug("LOG_DEBUG_EXTENSION", "%^& " + widgetName, SuggestedCategory.NONE);
		
		Object widgetValue = detailFocus.getValue(widgetName);
		//if null, return
		if (isNull(widgetValue))
		{
			return;
		}
		else
		{
			//Check <0 and >7
			_log.debug("LOG_DEBUG_EXTENSION", "^%^^ " + widgetName + " - " + form.getName(), SuggestedCategory.NONE);
			
			String label = form.getFormWidgetByName(widgetName).getLabel("label", locale);
			String tabName = form.getLabel("title", locale);
			_log.debug("LOG_DEBUG_EXTENSION", "^%^^ " + label + " - " + form.getName(), SuggestedCategory.NONE);
			
			double tempValue = NumericValidationCCF.parseNumber(widgetValue.toString());
			_log.debug("LOG_DEBUG_EXTENSION", "^%^^ " + tempValue + " - " + label + " - " + form.getName(), SuggestedCategory.NONE);
			
			if (tempValue < 0 || tempValue > 7)
			{
				//throw exception
				//_log.debug("LOG_DEBUG_EXTENSION", "^%^^ " + tempValue + " - " + label + " - " + form.getName(), SuggestedCategory.NONE);;
				String[] parameters = new String[3];
				parameters[0] = widgetValue.toString();
				parameters[1] = label;
				parameters[2] = tabName;
				throw new UserException("WMEXP_PACK_0TO7", "WMEXP_PACK_0TO7", parameters);
			}
		}
	}

	private void nonnegativeValidation(String widgetName, DataBean detailFocus, RuntimeFormInterface form, LocaleInterface locale) throws EpiDataException, UserException
	{

		//_log.debug("LOG_DEBUG_EXTENSION", "%^& " + widgetName + " @ " + form.getName(), SuggestedCategory.NONE);;
		Object widgetValue = detailFocus.getValue(widgetName);
		//if null, return
		if (isNull(widgetValue))
		{
			return;
		}
		else
		{
			//Check <0
			//_log.debug("LOG_DEBUG_EXTENSION", "&& " + form.getName(), SuggestedCategory.NONE);;
			RuntimeFormWidgetInterface widget = form.getFormWidgetByName(widgetName);
			String label = widget.getLabel("label", locale);
			String tabName = form.getLabel("title", locale);
			double tempValue = NumericValidationCCF.parseNumber(widgetValue.toString());
			//_log.debug("LOG_DEBUG_EXTENSION", "^%^^ " + tempValue + " - " + label + " - " + form.getName(), SuggestedCategory.NONE);;
			if (tempValue < 0)
			{
				//throw exception
				String[] parameters = new String[3];
				parameters[0] = label;
				parameters[1] = widgetValue.toString();
				parameters[2] = tabName;
				throw new UserException("WMEXP_TAB_NEG_VALIDATION", parameters);
			}
		}
	}

	private void packTabValidation(String widgetName, DataBean detailFocus, RuntimeFormInterface form, LocaleInterface locale) throws EpiDataException, UserException
	{
		//_log.debug("LOG_DEBUG_EXTENSION", "%^& " + widgetName, SuggestedCategory.NONE);;
		Object widgetValue = detailFocus.getValue(widgetName);
		String packDropdownName = packMapping.get(widgetName).toString();
		Object packDropdownValue = detailFocus.getValue(packDropdownName);
		//if null, set value to space
		if (isEmpty(widgetValue))
		{
			detailFocus.setValue(widgetName, " ");
		}
		else
		{
			//Check <0
			String label = form.getFormWidgetByName(widgetName).getLabel("label", locale);
			String tabName = form.getLabel("title", locale);
			double tempValue = FormValidation.parseNumber(widgetValue.toString(), label);
			if (tempValue < 0)
			{
				//throw exception
				_log.debug("LOG_DEBUG_EXTENSION", "^%^^ " + tempValue + " - " + label + " - " + form.getName(), SuggestedCategory.NONE);
			
				String[] parameters = new String[3];
				parameters[0] = label;
				parameters[1] = String.valueOf(tempValue);
				parameters[2] = tabName;
				throw new UserException("WMEXP_TAB_NEG_VALIDATION", parameters);
			}

			//Check to see if UOM Dropdown is defined
			if (tempValue > 0 && isEmpty(packDropdownValue))
			{
				//throw exception
				_log.debug("LOG_DEBUG_EXTENSION", "^%^^ " + tempValue + " - " + label + " - " + form.getName(), SuggestedCategory.NONE);
			
				String[] parameters = new String[3];
				parameters[0] = label;
				parameters[1] = String.valueOf(tempValue);
				parameters[2] = tabName;
				throw new UserException("WMEXP_PACK_UOM_UNDEF", parameters);
			}

		}
		_log.debug("LOG_DEBUG_EXTENSION_PackSaveValidationAction", "Going to Set " + packDropdownName
				+ " Dropdown to ' '", SuggestedCategory.NONE);
		//Set UOM Dropdown to Empty
		if (isEmpty(packDropdownValue))
		{
			_log.debug("LOG_DEBUG_EXTENSION_PackSaveValidationAction", "Space inserted for "
					+ packDropdownName, SuggestedCategory.NONE);
			detailFocus.setValue(packDropdownName, " ");
		}
	}

	private RuntimeFormInterface retrieveShellForm(StateInterface state)
	{
		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();

		RuntimeFormInterface shellForm = shellToolbar.getParentForm(state);

		return shellForm;
	}

	private RuntimeFormInterface retrieveDetailForm(StateInterface state, RuntimeFormInterface shellForm)
	{
		//Detail
		SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");

		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
		_log.debug("LOG_DEBUG_EXTENSION", "\n3'''Current form  = " + detailForm.getName(), SuggestedCategory.NONE);

		return detailForm;
	}

	private boolean isNull(Object attributeValue)
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
