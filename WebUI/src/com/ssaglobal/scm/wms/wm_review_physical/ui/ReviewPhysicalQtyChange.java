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
package com.ssaglobal.scm.wms.wm_review_physical.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeListRowInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.exceptions.EpiException;//AW 10/14/2008 Machine#:2093019 SDIS:SCM-00000-05440
import com.ssaglobal.scm.wms.util.UOMMappingUtil;//AW 10/14/2008 Machine#:2093019 SDIS:SCM-00000-05440
import com.epiphany.shr.ui.state.StateInterface;//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530

public class ReviewPhysicalQtyChange extends FormExtensionBase{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ReviewPhysicalQtyChange.class);
	//Static globally referenced variables
	
	//Static attribute names
	private static String QTY = "QTY";
	private static String UOM = "UOM";
	private static String PACK = "PACKKEY";
	
	
	@Override
	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form) throws UserException,EpiException{
		RuntimeListRowInterface[] rows = form.getRuntimeListRows(); 
		for(int i=0; i<rows.length; i++){
			String uom = rows[i].getFormWidgetByName(UOM).getDisplayValue();
			String pack = rows[i].getFormWidgetByName(PACK).getDisplayValue();//AW 10/14/2008 Machine#:2093019 SDIS:SCM-00000-05440
			String uom3 = UOMMappingUtil.getPACKUOM3Val(pack, context.getState().getDefaultUnitOfWork());//AW 10/14/2008 Machine#:2093019 SDIS:SCM-00000-05440
			//AW 10/14/2008 Machine#:2093019 SDIS:SCM-00000-05440
			if(!uom.equals(uom3)){
				//get values for changes
				String qty = commaStrip(rows[i].getFormWidgetByName(QTY).getDisplayValue());
				
				//change values
				rows[i].getFormWidgetByName(QTY).setDisplayValue(nonZero(uom, qty, pack, context.getState()));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
			}
		}
		return RET_CONTINUE;
	}
	
	/**
	 * Modification History
	 * 04/14/2009	AW  Machine#:2093019 SDIS:SCM-00000-05440
	 * 05/19/2009   AW  SDIS:SCM-00000-06871 Machine:2353530
	 * 					UOM conversion is now done in the front end.
	 *                  Changes were made accordingly. 
	 */
	public String nonZero(String uom, String qty, String pack, StateInterface state) throws UserException{
		double temp = Double.parseDouble(qty);
		if(temp!=0.0){
			return UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA, uom, qty, pack, state, UOMMappingUtil.uowNull, true);//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
		}else{
			return "0";
		}
	}
	
	//Stored Procedure

	
	private String commaStrip(String number){
		Pattern pattern = Pattern.compile("\\,");
		Matcher matcher = pattern.matcher(number);
		return matcher.replaceAll("");
	}
}