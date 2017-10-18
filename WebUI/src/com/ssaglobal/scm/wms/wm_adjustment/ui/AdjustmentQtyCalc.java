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
package com.ssaglobal.scm.wms.wm_adjustment.ui;

import java.text.NumberFormat;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.util.exceptions.EpiException;//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
import com.ssaglobal.scm.wms.util.UOMMappingUtil;
import com.ssaglobal.scm.wms.util.LocaleUtil;//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530

public class AdjustmentQtyCalc extends FormExtensionBase{
	/**
	 * Modification History
	 * 04/14/2009	AW Machine#:2093019 SDIS:SCM-00000-05440
	 * 04/23/2009	AW		SDIS:SCM-00000-06871 Machine:2353530
	 *						Changed code to allow qty values for the Currency Locale
	 *						something other than dollar.
     * 05/19/2009   AW      SDIS:SCM-00000-06871 Machine:2353530
	 * 						UOM conversion is now done in the front end.
	 *                      Changes were made accordingly.	
	 * @throws EpiException 
	 * 
	 */

	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException{
		NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0);
		Object toggle = form.getFormWidgetByName("ISADJ").getValue();
		DataBean focus = form.getFocus();
		QBEBioBean qbe = null;
		BioBean bio = null;

		if(toggle!=null){
			String toggleValue = toggle.toString();
			if(toggleValue.equals("1") || toggleValue.equals("0")){
				String current = form.getFormWidgetByName("CURRENTQTY").getValue().toString();
				if(current.length()>0){
					//Initialize variables
					String target = null;
					String adj = null;

					if(focus.isTempBio()){
						qbe = (QBEBioBean)focus;
					}else{
						bio = (BioBean)focus;
					}
					double tQty = 0;
					double aQty = 0;
					double qty = 0;
					qty = Double.parseDouble(current);
					if(toggleValue.equals("1")){
						//Calculate target
						if(focus.isTempBio()){
							adj = qbe.get("QTY").toString();
						}else{
							adj = bio.get("QTY").toString();
						}
						aQty = Double.parseDouble(adj);
						tQty = qty+aQty;
					}else{
						//Calculate adjustment quantity
						target = form.getFormWidgetByName("TARGETQTY").getValue().toString();
						tQty = Double.parseDouble(LocaleUtil.resetQtyToDecimalForBackend(target));//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
						aQty = tQty - qty;
					}
					//Format target
					//target = decFormat.format(tQty);
					target = LocaleUtil.formatValues(Double.toString(tQty), LocaleUtil.TYPE_QTY);
					form.getFormWidgetByName("TARGETQTY").setValue(target);
					
					//Format adjustment quantity
					adj = nf.format(aQty);
					if(focus.isTempBio()){
						qbe.set("QTY", adj);
					}else{
						bio.set("QTY", adj);
					}
				}
			}
		}
//7291.b
		if (! (focus instanceof QBEBioBean)){
			String packkey = null;
			String uom = null;
			String adj= null;
			double tQty = 0;
			double aQty = 0;
			double qty = 0;
			String current = form.getFormWidgetByName("CURRENTQTY").getValue().toString();
			String target = form.getFormWidgetByName("TARGETQTY").getValue().toString();
			if(focus.isTempBio()){
				qbe = (QBEBioBean)focus;
			}else{
				bio = (BioBean)focus;
				adj = bio.get("QTY").toString();
				packkey = bio.get("PACKKEY").toString();
				uom = bio.get("UOM").toString();
			}
			qty = Double.parseDouble(current);
			aQty = Double.parseDouble(adj);
			qty = qty - aQty;
			tQty = qty + aQty;
			bio.set("QTY", UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),UOMMappingUtil.UOM_EA, uom,adj,packkey, context.getState(),UOMMappingUtil.uowNull, true));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
			form.getFormWidgetByName("CURRENTQTY").setValue(UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),UOMMappingUtil.UOM_EA, uom,qty+"",packkey, context.getState(),UOMMappingUtil.uowNull, true));	//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
			form.getFormWidgetByName("TARGETQTY").setValue(UOMMappingUtil.numberFormaterConverter(LocaleUtil.getCurrencyLocale(),UOMMappingUtil.UOM_EA, uom,tQty+"",packkey, context.getState(),UOMMappingUtil.uowNull, true));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530

		}
//7291.e
		return RET_CONTINUE;
	}

}