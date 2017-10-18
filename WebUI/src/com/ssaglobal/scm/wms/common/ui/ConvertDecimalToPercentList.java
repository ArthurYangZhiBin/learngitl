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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.objects.bio.FieldMappedAttributeType;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;

public class ConvertDecimalToPercentList extends FormExtensionBase
{
	public ConvertDecimalToPercentList()
    {
    }
	
	protected int preRenderListForm(UIRenderContext arg0, RuntimeListFormInterface form) throws EpiException {
		try {
			String attrField = getAttributeForConversion();
			String percentField = getPercentAttribute();	
			int precision = getPrecision();			
			if(attrField == null || percentField == null)
				return RET_CANCEL;			
			populatePercent((BioCollectionBean)form.getFocus(), attrField, percentField,precision);			
			return RET_CONTINUE;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return RET_CANCEL;
		} 
	}
	
	private void populatePercent(BioCollectionBean bioCollection,String attrName, String percentAttrName, int precision){
		try {
			if(bioCollection != null){				
				for(int i = 0; i < bioCollection.size(); i++){					
					Bio bio = bioCollection.elementAt(i);					
					BigDecimal percent = (BigDecimal)bio.get(attrName);					
					percent = new BigDecimal(percent.doubleValue() * 100);					
					String percentString = percent.toString();					
					if(percentString.indexOf(".") < 0)
						percentString += ".";
					for(int j = 0; j < precision; j++){
						percentString += "0";
					}				
					percentString = percentString.substring(0,percentString.indexOf(".")) + percentString.substring(percentString.indexOf("."),percentString.indexOf(".")+3) + "%";					
					bio.set(percentAttrName,percentString);
				}
			}
		} catch (EpiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private String getAttributeForConversion() throws Exception {
		Object attrObject = getParameter("attributeForConversion");		
		if (attrObject == null) {			
			return null;
		}	
		Method getName = attrObject.getClass().getMethod("getName",new Class[0]);
		String name = (String)getName.invoke(attrObject,new Class[0]);
		return name;
	}
	private String getPercentAttribute() throws Exception {		
		
		Object attrObject = getParameter("percentAttribute");		
		if (attrObject == null) {			
			return null;
		}		
		Method getName = attrObject.getClass().getMethod("getName",new Class[0]);
		String name = (String)getName.invoke(attrObject,new Class[0]);		
		return name;
	}
	private int getPrecision(){
		Integer precision = (Integer)getParameter("precision");
		if(precision == null)
			return 0;
		else
			return precision.intValue();
	}
}