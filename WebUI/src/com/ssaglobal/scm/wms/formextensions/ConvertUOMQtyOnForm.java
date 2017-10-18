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
package com.ssaglobal.scm.wms.formextensions;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeListRowInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;

public class ConvertUOMQtyOnForm extends FormExtensionBase
{
	public ConvertUOMQtyOnForm()
	{
	}
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ConvertUOMQtyOnForm.class);
	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {		
		ArrayList sourceWidgets = (ArrayList)getParameter("sourceWidgets");
		ArrayList targetWidgets = (ArrayList)getParameter("targetWidgets");			
		String PackKeyWidget = (String)getParameter("PackKeyWidget");
		String UOMWidget = (String)getParameter("UOMWidget");
		_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","sourceWidgets:"+sourceWidgets,100L);
		_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","targetWidgets:"+targetWidgets,100L);
		_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","PackKeyWidget:"+PackKeyWidget,100L);
		_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","UOMWidget:"+UOMWidget,100L);
		
		for(int j = 0; j < sourceWidgets.size(); j++){			
			_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","Getting Row Widgets...",100L);
			RuntimeFormWidgetInterface source = form.getFormWidgetByName((String)sourceWidgets.get(j));
			RuntimeFormWidgetInterface target = form.getFormWidgetByName((String)targetWidgets.get(j));
			String packKey = form.getFormWidgetByName(PackKeyWidget).getDisplayValue();
			String uom = form.getFormWidgetByName(UOMWidget).getDisplayValue();
			String qty = source.getDisplayValue().toString();
			_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","source:"+source,100L);
			_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","target:"+target,100L);
			_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","packKey:"+packKey,100L);
			_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","uom:"+uom,100L);
			_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","qty:"+qty,100L);
			if (!qty.equalsIgnoreCase("0") && !uom.equalsIgnoreCase("ea")){
				_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","Converting...",100L);
				String ConvExpQty = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA,uom,qty, packKey, context.getState(), UOMMappingUtil.uowNull, true); //AW
				UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA,uom,qty, packKey, context.getState(), UOMMappingUtil.uowNull, true); //AW
				_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","ConvExpQty:"+ConvExpQty,100L);
				target.setDisplayValue(ConvExpQty);
			}							
		}
		
		return RET_CONTINUE;
	}
	
	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form) throws EpiException {
		RuntimeListRowInterface[] listRows = form.getRuntimeListRows();
		ArrayList sourceWidgets = (ArrayList)getParameter("sourceWidgets");
		ArrayList targetWidgets = (ArrayList)getParameter("targetWidgets");			
		String PackKeyWidget = (String)getParameter("PackKeyWidget");
		String UOMWidget = (String)getParameter("UOMWidget");
		_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","sourceWidgets:"+sourceWidgets,100L);
		_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","targetWidgets:"+targetWidgets,100L);
		_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","PackKeyWidget:"+PackKeyWidget,100L);
		_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","UOMWidget:"+UOMWidget,100L);
		for (int i = 0; i < listRows.length; i++)
		{			
			_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","Getting Row...",100L);
			for(int j = 0; j < sourceWidgets.size(); j++){
				//Get List Widgets
				_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","Getting Row Widgets...",100L);
				RuntimeFormWidgetInterface source = listRows[i].getFormWidgetByName((String)sourceWidgets.get(j));
				RuntimeFormWidgetInterface target = listRows[i].getFormWidgetByName((String)targetWidgets.get(j));
				String packKey = listRows[i].getFormWidgetByName(PackKeyWidget).getDisplayValue();
				String uom = listRows[i].getFormWidgetByName(UOMWidget).getDisplayValue();
				String qty = source.getDisplayValue().toString();
				_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","source:"+source,100L);
				_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","target:"+target,100L);
				_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","packKey:"+packKey,100L);
				_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","uom:"+uom,100L);
				_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","qty:"+qty,100L);
				if (!qty.equalsIgnoreCase("0") && !uom.equalsIgnoreCase("ea")){
					_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","Converting...",100L);
					String ConvExpQty = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA,uom,qty, packKey, context.getState(), UOMMappingUtil.uowNull, true); //AW
					_log.debug("LOG_DEBUG_EXTENSION_CONVUOMONLIST","ConvExpQty:"+ConvExpQty,100L);
					target.setDisplayValue(ConvExpQty);
				}							
			}
		}
		return RET_CONTINUE;
	}

	
}