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


package com.ssaglobal.scm.wms.common.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.math.BigDecimal;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.impl.Attribute;
import com.epiphany.shr.metadata.objects.bio.AttributeType;
import com.epiphany.shr.metadata.objects.bio.FieldMappedAttributeType;
import com.epiphany.shr.metadata.objects.bio.RelMappedAttributeType;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;

/**
 * ConvertDecimalToPercent takes a decimal number and convert it to a percentage
 * by multiplying it by 100. It will also enforce a precision besed on a parameter
 * and it appends a % character to the end.
 * 
 * The preRenderWidget event triggers the ConvertDecimalToPercent.execute() method.
 * 
 * The parameters expected by the TallyPercentFieldAction.execute() method include the
 * 'attributeForConversion' parameter and the 'precision' parameter. The
 * 'attributeForConversion' property value specifies the name of the bio collection
 * attribute (associated with the form) to be converted. The 'precision' property
 * specifies the precision of the final string
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ConvertDecimalToPercent extends
		com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase {
	

	/**
	 * This method will take a decimal number and convert it to a percentage
	 * by multiplying it by 100. It will also enforce a precision besed on a parameter
	 * and it appends a % character to the end.
	 * <P>
	 * 
	 * @param state
	 *            The StateInterface for this extension
	 * @param widget
	 *            The RuntimeFormWidgetInterface for this extension's widget
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 */
	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget) {

		try {								
			if(!state.getFocus().isBio())
				return RET_CANCEL;
			
			FieldMappedAttributeType attrField = getAttributeForConversion();	
			AttributeType bioPctField = getBioPctAttribute();	
			int precision = getPrecision();
			if (attrField == null) {
				return RET_CANCEL;
			}				
			Bio bio = (Bio)state.getFocus();
			if (bio == null) {
				return RET_CANCEL;
			}		
			Object fieldValue = bio.get(attrField.getName());
			Double number = Double.valueOf(fieldValue.toString());
			String percent = convert(number,precision);
			if(bioPctField == null)
				widget.setValue(percent);
			else
				bio.set(bioPctField.getName(),percent);
		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;

		}

		return RET_CONTINUE;

	}

	private AttributeType getBioPctAttribute() {		
		Object attrObject = getParameter("bioPctAttribute");		
		if (attrObject == null) {					
			return null;
		}
		if (!(attrObject instanceof AttributeType)) {			
			return null;
		}
		AttributeType attrField = (AttributeType) attrObject;
		return attrField;				
	}

	private FieldMappedAttributeType getAttributeForConversion() {
		Object attrObject = getParameter("attributeForConversion");
		if (attrObject == null) {			
			return null;
		}
		if (!(attrObject instanceof FieldMappedAttributeType)) {			
			return null;
		}
		FieldMappedAttributeType attrField = (FieldMappedAttributeType) attrObject;
		return attrField;
	}

	private int getPrecision(){
		Integer precision = (Integer)getParameter("precision");
		if(precision == null)
			return 0;
		else
			return precision.intValue();
	}	
	private String convert(Double number, int precision){
				
		String percent = (number.doubleValue() * 100)+"";
		
		if(percent.indexOf(".") < 0)
			percent += ".";
		for(int i = 0; i < precision; i++){
			percent += "0";
		}		
		return (percent.substring(0,percent.indexOf(".")) + percent.substring(percent.indexOf("."),percent.indexOf(".")+precision+1) + "%");
	}
}
