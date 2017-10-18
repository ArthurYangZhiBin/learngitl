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
package com.ssaglobal.scm.wms.wm_serial_inventory.ui;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;

public class SerialInventorySearchPreRender extends FormExtensionBase{	
	private boolean isEmpty(Object attributeValue) {
		if (attributeValue == null) {
			return true;
		} else if (attributeValue.toString().matches("\\s*")) {
			return true;
		} else if (attributeValue.toString().matches("null")) {
			return true;
		} else {
			return false;
		}
	}
	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException 
	{		
		//OWNER|STORERKEY|0|15
		//ITEM|SKU|0|50
		//LOT|LOT|0|10
		//LPN|ID|0|18
		//LOCATION|LOC|0|10
		//SERIAL|SERIALNUMBER|0|30
		//DATA2|DATA2|0|30
		//DATA3|DATA3|0|30
		//DATA4|DATA4|0|30
		//DATA5|DATA5|0|30
		//LONGSERIAL|SERIALNUMBERLONG|0|50
		//WEIGHT|GROSSWEIGHT|0|22,5
		//QTY|QTY|0|22,5

		if (isEmpty(form.getFormWidgetByName("OWNEREND").getDisplayValue()))
			form.getFormWidgetByName("OWNEREND").setDisplayValue("ZZZZZZZZZZZZZZZ");
		else
			form.getFormWidgetByName("OWNEREND").setDisplayValue(form.getFormWidgetByName("OWNEREND").getDisplayValue().toUpperCase());
		if (isEmpty(form.getFormWidgetByName("OWNERSTART").getDisplayValue()))
			form.getFormWidgetByName("OWNERSTART").setDisplayValue("0");
		else
			form.getFormWidgetByName("OWNERSTART").setDisplayValue(form.getFormWidgetByName("OWNERSTART").getDisplayValue().toUpperCase());

		if (isEmpty(form.getFormWidgetByName("ITEMEND").getDisplayValue()))
			form.getFormWidgetByName("ITEMEND").setDisplayValue("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
		else
			form.getFormWidgetByName("ITEMEND").setDisplayValue(form.getFormWidgetByName("ITEMEND").getDisplayValue().toUpperCase());
		if (isEmpty(form.getFormWidgetByName("ITEMSTART").getDisplayValue()))
			form.getFormWidgetByName("ITEMSTART").setDisplayValue("0");
		else
			form.getFormWidgetByName("ITEMSTART").setDisplayValue(form.getFormWidgetByName("ITEMSTART").getDisplayValue().toUpperCase());

		if (isEmpty(form.getFormWidgetByName("LOTEND").getDisplayValue()))
			form.getFormWidgetByName("LOTEND").setDisplayValue("ZZZZZZZZZZ");
		else
			form.getFormWidgetByName("LOTEND").setDisplayValue(form.getFormWidgetByName("LOTEND").getDisplayValue().toUpperCase());
		if (isEmpty(form.getFormWidgetByName("LOTSTART").getDisplayValue()))
			form.getFormWidgetByName("LOTSTART").setDisplayValue("0");
		else
			form.getFormWidgetByName("LOTSTART").setDisplayValue(form.getFormWidgetByName("LOTSTART").getDisplayValue().toUpperCase());

		if (isEmpty(form.getFormWidgetByName("LPNEND").getDisplayValue()))
			form.getFormWidgetByName("LPNEND").setDisplayValue("ZZZZZZZZZZZZZZZZZZ");
		else
			form.getFormWidgetByName("LPNEND").setDisplayValue(form.getFormWidgetByName("LPNEND").getDisplayValue().toUpperCase());
		
		if (! isEmpty(form.getFormWidgetByName("LPNSTART").getDisplayValue()))
			form.getFormWidgetByName("LPNSTART").setDisplayValue(form.getFormWidgetByName("LPNSTART").getDisplayValue().toUpperCase());

		if (isEmpty(form.getFormWidgetByName("LOCATIONEND").getDisplayValue()))
			form.getFormWidgetByName("LOCATIONEND").setDisplayValue("ZZZZZZZZZZ");
		else
			form.getFormWidgetByName("LOCATIONEND").setDisplayValue(form.getFormWidgetByName("LOCATIONEND").getDisplayValue().toUpperCase());
		if (isEmpty(form.getFormWidgetByName("LOCATIONSTART").getDisplayValue()))
			form.getFormWidgetByName("LOCATIONSTART").setDisplayValue("0");
		else
			form.getFormWidgetByName("LOCATIONSTART").setDisplayValue(form.getFormWidgetByName("LOCATIONSTART").getDisplayValue().toUpperCase());

		if (isEmpty(form.getFormWidgetByName("SERIALEND").getDisplayValue()))
			form.getFormWidgetByName("SERIALEND").setDisplayValue("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
		else
			form.getFormWidgetByName("SERIALEND").setDisplayValue(form.getFormWidgetByName("SERIALEND").getDisplayValue().toUpperCase());
		if (isEmpty(form.getFormWidgetByName("SERIALSTART").getDisplayValue()))
			form.getFormWidgetByName("SERIALSTART").setDisplayValue("0");
		else
			form.getFormWidgetByName("SERIALSTART").setDisplayValue(form.getFormWidgetByName("SERIALSTART").getDisplayValue().toUpperCase());

		if (isEmpty(form.getFormWidgetByName("DATA2END").getDisplayValue()))
			form.getFormWidgetByName("DATA2END").setDisplayValue("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
		else
			form.getFormWidgetByName("DATA2END").setDisplayValue(form.getFormWidgetByName("DATA2END").getDisplayValue().toUpperCase());
		
		if (! isEmpty(form.getFormWidgetByName("DATA2START").getDisplayValue()))
			form.getFormWidgetByName("DATA2START").setDisplayValue(form.getFormWidgetByName("DATA2START").getDisplayValue().toUpperCase());

		if (isEmpty(form.getFormWidgetByName("DATA3END").getDisplayValue()))
			form.getFormWidgetByName("DATA3END").setDisplayValue("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
		else
			form.getFormWidgetByName("DATA3END").setDisplayValue(form.getFormWidgetByName("DATA3END").getDisplayValue().toUpperCase());
		
		if (! isEmpty(form.getFormWidgetByName("DATA3START").getDisplayValue()))
			form.getFormWidgetByName("DATA3START").setDisplayValue(form.getFormWidgetByName("DATA3START").getDisplayValue().toUpperCase());

		if (isEmpty(form.getFormWidgetByName("DATA4END").getDisplayValue()))
			form.getFormWidgetByName("DATA4END").setDisplayValue("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
		else
			form.getFormWidgetByName("DATA4END").setDisplayValue(form.getFormWidgetByName("DATA4END").getDisplayValue().toUpperCase());
		
		if (! isEmpty(form.getFormWidgetByName("DATA4START").getDisplayValue()))
			form.getFormWidgetByName("DATA4START").setDisplayValue(form.getFormWidgetByName("DATA4START").getDisplayValue().toUpperCase());

		if (isEmpty(form.getFormWidgetByName("DATA5END").getDisplayValue()))
			form.getFormWidgetByName("DATA5END").setDisplayValue("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
		else
			form.getFormWidgetByName("DATA5END").setDisplayValue(form.getFormWidgetByName("DATA5END").getDisplayValue().toUpperCase());

		if (! isEmpty(form.getFormWidgetByName("DATA5START").getDisplayValue()))
			form.getFormWidgetByName("DATA5START").setDisplayValue(form.getFormWidgetByName("DATA5START").getDisplayValue().toUpperCase());

		if (isEmpty(form.getFormWidgetByName("LONGSERIALEND").getDisplayValue()))
			form.getFormWidgetByName("LONGSERIALEND").setDisplayValue("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
		else
			form.getFormWidgetByName("LONGSERIALEND").setDisplayValue(form.getFormWidgetByName("LONGSERIALEND").getDisplayValue().toUpperCase());
		
		if (! isEmpty(form.getFormWidgetByName("LONGSERIALSTART").getDisplayValue()))
			form.getFormWidgetByName("LONGSERIALSTART").setDisplayValue(form.getFormWidgetByName("LONGSERIALSTART").getDisplayValue().toUpperCase());

		if (isEmpty(form.getFormWidgetByName("WEIGHTEND").getDisplayValue()))
			form.getFormWidgetByName("WEIGHTEND").setDisplayValue("9999999999999999.99999");
		else
			form.getFormWidgetByName("WEIGHTEND").setDisplayValue(form.getFormWidgetByName("WEIGHTEND").getDisplayValue().toUpperCase());
		if (isEmpty(form.getFormWidgetByName("WEIGHTSTART").getDisplayValue()))
			form.getFormWidgetByName("WEIGHTSTART").setDisplayValue("0");
		else
			form.getFormWidgetByName("WEIGHTSTART").setDisplayValue(form.getFormWidgetByName("WEIGHTSTART").getDisplayValue().toUpperCase());

		if (isEmpty(form.getFormWidgetByName("QTYEND").getDisplayValue()))
			form.getFormWidgetByName("QTYEND").setDisplayValue("9999999999999999.99999");
		else
			form.getFormWidgetByName("QTYEND").setDisplayValue(form.getFormWidgetByName("QTYEND").getDisplayValue().toUpperCase());
		if (isEmpty(form.getFormWidgetByName("QTYSTART").getDisplayValue()))
			form.getFormWidgetByName("QTYSTART").setDisplayValue("0");
		else
			form.getFormWidgetByName("QTYSTART").setDisplayValue(form.getFormWidgetByName("QTYSTART").getDisplayValue().toUpperCase());
		
		return RET_CONTINUE;
	}
}

