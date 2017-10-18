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
package com.ssaglobal.scm.wms.wm_shipmentorder.ui;

//Import 3rd party packages and classes

//Import Epiphany packages and classes
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.util.*;;

public class ShipmentOrderSpecialCaseConvertUOM extends FormExtensionBase{
	private static String OPEN_QTY = "OPENQTY";
	private static String UOM = "UOM";
	private static String PACK = "PACKKEY";
	
	public int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws UserException, EpiException{
		DataBean focus = form.getFocus();
		if(context.getActionObject().getName().equals("DELETE") && focus.isBio()){
			BioBean bio = (BioBean)focus;
			String pack = bio.get(PACK).toString();
			String uom = bio.get(UOM).toString();
			String qty = bio.get(OPEN_QTY).toString();
			String uom3 = UOMMappingUtil.getPACKUOM3Val(pack, context.getState().getDefaultUnitOfWork());
			if(!uom.equals(uom3)){
				qty = ShipmentOrderFillPrevUOM.nonZero(uom, qty, pack, context.getState());//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
			}
			String formattedQty = LocaleUtil.formatValues(qty, LocaleUtil.TYPE_QTY);
			bio.set(OPEN_QTY, formattedQty); //AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
		}
		return RET_CONTINUE;
	}
}