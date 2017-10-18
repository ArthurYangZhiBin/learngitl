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

package com.ssaglobal.scm.wms.wm_item.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.text.NumberFormat;

import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeListRowInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.ssaglobal.scm.wms.uiextensions.UOMDefaultValue;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440

public class SubstituteItemPreRender extends com.epiphany.shr.ui.view.customization.FormExtensionBase {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(SubstituteItemPreRender.class);


	boolean isEmpty(Object attributeValue) {
		if(attributeValue == null) {
			return true;
		} else if(attributeValue.toString().matches("\\s*")) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * Modification HIstory
	 * 04/14/2009	AW	Machine#:2093019 SDIS:SCM-00000-05440
     * 05/19/2009   AW      SDIS:SCM-00000-06871 Machine:2353530
	 * 						UOM conversion is now done in the front end.
	 *                      Changes were made accordingly.
	 */
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {
		try	{
			StateInterface state = context.getState();
			DataBean focus = form.getFocus();
			NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW
			NumberFormat nfo = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW

			//Item
			String packKey = (String) focus.getValue("PACKKEY");
			Object unitQty = focus.getValue("QTY");
			String toUom = (String) focus.getValue("UOM");
			_log.debug("LOG_DEBUG_EXTENSION_SubstituteItemPreRender", "Item -- Pack - " + packKey + " unitQty - "
					+ unitQty + " toUom - " + toUom, SuggestedCategory.NONE);
			String units;
			if(!isEmpty(packKey) && !isEmpty(unitQty) && !isEmpty(toUom)) {
				units = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA, toUom, unitQty.toString(), packKey, state, UOMMappingUtil.uowNull, true);//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
				units = nfo.format(nf.parse(units));
			} else {
				units = "";
			}
			form.getFormWidgetByName("ITEMUNITS").setValue(units);
			_log.debug("LOG_DEBUG_EXTENSION_SubstituteItemPreRender", "Setting ITEMUNITS to " + units,
						SuggestedCategory.NONE);

			//SubItem
			String subPackKey = (String) focus.getValue("SUBPACKKEY");
			Object subUnitQty = focus.getValue("SUBQTY");
			UOMDefaultValue.fillDropdown(state, "SUBUOM", subPackKey); //AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
			String subToUom = (String) focus.getValue("SUBUOM");
			_log.debug("LOG_DEBUG_EXTENSION_SubstituteItemPreRender", "SubItem -- Pack - " + subPackKey + " unitQty - "
					+ subUnitQty + " toUom - " + subToUom, SuggestedCategory.NONE);
			String subUnits;
			if(!isEmpty(subPackKey) && !isEmpty(subUnitQty) && !isEmpty(subToUom)) {
				subUnits = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA,subToUom, subUnitQty.toString(), subPackKey, state, UOMMappingUtil.uowNull, true);//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
				subUnits = nfo.format(nf.parse(subUnits));
			} else {
				subUnits = "";
			}
			form.getFormWidgetByName("SUBUNITS").setValue(subUnits);
			_log.debug("LOG_DEBUG_EXTENSION_SubstituteItemPreRender", "Setting SUBUNITS to " + subUnits,
						SuggestedCategory.NONE);
		} catch (Exception e) {
			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}
		return RET_CONTINUE;
	}
	/**
	 * Modification HIstory
	 * 04/14/2009	AW	Machine#:2093019 SDIS:SCM-00000-05440
     * 05/19/2009   AW      SDIS:SCM-00000-06871 Machine:2353530
	 * 						UOM conversion is now done in the front end.
	 *                      Changes were made accordingly.
	 */
	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form) throws EpiException {
		NumberFormat nf = NumberFormat.getInstance();
		NumberFormat nfo = NumberFormat.getInstance();
		nfo.setMaximumFractionDigits(5);
		nfo.setMinimumFractionDigits(5);
		try {
			RuntimeListRowInterface[] rows = form.getRuntimeListRows();
			for(int i = 0; i < rows.length; i++) {
				//Item
				String packKey = rows[i].getFormWidgetByName("PACKKEY").getDisplayValue();
				Object unitQty = rows[i].getFormWidgetByName("QTY").getDisplayValue();
				String toUom = rows[i].getFormWidgetByName("UOM").getDisplayValue();
				//jp.answerlink.128149.begin
				RuntimeFormWidgetInterface itemUnitsWidget = rows[i].getFormWidgetByName("ITEMUNITS");
				RuntimeFormWidgetInterface subUnitsWidget = rows[i].getFormWidgetByName("SUBUNITS");
				//jp.answerlink.128149.end

				_log.debug("LOG_DEBUG_EXTENSION_SubstituteItemPreRender", "Item -- Pack - " + packKey + " unitQty - "
						+ unitQty + " toUom - " + toUom, SuggestedCategory.NONE);
				String units;
				if(!isEmpty(packKey) && !isEmpty(unitQty) && !isEmpty(toUom)) {
					unitQty = nf.parse(unitQty.toString()).toString();
					_log.debug("LOG_DEBUG_EXTENSION_SubstituteItemPreRender", "Passing Qty " + unitQty, SuggestedCategory.NONE);
					units = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA,toUom, unitQty.toString(), packKey, context.getState(), UOMMappingUtil.uowNull, true);//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
				} else {
					units = "";
				}
				//jp.answerlink.128149.begin
				//rows[i].getFormWidgetByName("ITEMUNITS").setDisplayValue(nfo.format(nfo.parse(units)));
				
				itemUnitsWidget.setDisplayValue(nfo.format(nfo.parse(units)));
				itemUnitsWidget.setValue(nfo.format(nfo.parse(units)));
				
				//jp.answerlink.128149.end

				_log.debug("LOG_DEBUG_EXTENSION_SubstituteItemPreRender", "Setting ITEMUNITS to " + units,
							SuggestedCategory.NONE);

				//SubItem
				String subPackKey = rows[i].getFormWidgetByName("SUBPACKKEY").getDisplayValue();
				Object subUnitQty = rows[i].getFormWidgetByName("SUBQTY").getDisplayValue();
				String subToUom = rows[i].getFormWidgetByName("SUBUOM").getDisplayValue();
				_log.debug("LOG_DEBUG_EXTENSION_SubstituteItemPreRender", "SubItem -- Pack - " + subPackKey
						+ " unitQty - " + subUnitQty + " toUom - " + subToUom, SuggestedCategory.NONE);
				String subUnits;
				if(!isEmpty(subPackKey) && !isEmpty(subUnitQty) && !isEmpty(subToUom)) {
					subUnitQty = nf.parse(subUnitQty.toString()).toString();
					_log.debug("LOG_DEBUG_EXTENSION_SubstituteItemPreRender", "Passing SubQty " + subUnitQty,
								SuggestedCategory.NONE);
					subUnits = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA,subToUom, subUnitQty.toString(), subPackKey, context.getState(), UOMMappingUtil.uowNull, true);//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
				} else {
					subUnits = "";
				}
				//jp.answerlink.128149.begin
				//rows[i].getFormWidgetByName("SUBUNITS").setDisplayValue(nfo.format(nfo.parse(subUnits)));
				subUnitsWidget.setDisplayValue(nfo.format(nfo.parse(subUnits)));
				subUnitsWidget.setValue(nfo.format(nfo.parse(subUnits)));
				//jp.answerlink.128149.end
				
				_log.debug("LOG_DEBUG_EXTENSION_SubstituteItemPreRender", "Setting SUBUNITS to-->" + subUnitsWidget.getDisplayValue(),
							SuggestedCategory.NONE);
			}
		} catch (Exception e) {
			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}
		return RET_CONTINUE;
	}
}