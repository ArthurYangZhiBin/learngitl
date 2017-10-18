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

import com.ssaglobal.scm.wms.util.UOMMappingUtil;
import com.ssaglobal.scm.wms.util.dao.SkuSNConfDAO;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;

public class AdjustmentUpdateQty extends ActionExtensionBase{
	protected int execute(ActionContext context, ActionResult result) throws UserException, EpiException{
		UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();//AW 10/03/2008 Machine#:2093019 SDIS:SCM-00000-05440
		DataBean focus = context.getState().getFocus();
		if(focus.isTempBio()){
			RuntimeFormInterface form = context.getSourceWidget().getForm();
			QBEBioBean qbe = (QBEBioBean)focus;
			
			//jp 9068.begin  - Discard change of UOM if this an End To End Serial capture Item
			//String uom = qbe.get("UOM").toString();
			//String pack = qbe.get("PACKKEY").toString();
			String uom = (String)qbe.get("UOM");
			String pack = (String)qbe.get("PACKKEY");
			String uom3 = UOMMappingUtil.getPACKUOM3Val(pack, uowb);//AW 10/03/2008 Machine#:2093019 SDIS:SCM-00000-05440

			if(uom==null ||pack==null)
				return RET_CONTINUE;
			
			if (context.getSourceWidget().getName().equalsIgnoreCase("UOM"))
				discardUOMChangeIfSKUIsEndToEnd(qbe, context.getSourceWidget(), uom3);
			//jp 9068.end
			
			//Update Current Quantity Field
			String qty = form.getFormWidgetByName("ORIGCURRQTY").getDisplayValue();
			if(!qty.equals("")){
				if(!uom.equals(uom3)){
					qty = findQty(uom3, uom, qty, pack, uom3, context.getState()); //05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
				}
				form.getFormWidgetByName("CURRENTQTY").setValue(qty);
			}
			
			//Update appropriate adjustment field
			Object toggle = form.getFormWidgetByName("ISADJ").getValue();
			String toggleValue = toggle.toString();
			if(!toggleValue.equals("")){
				String initUOM = form.getFormWidgetByName("CHANGEQTYUOM").getDisplayValue();
				String adj = form.getFormWidgetByName("ORIGCHANGEQTY").getDisplayValue();
				if(!initUOM.equals(uom)){
					adj = findQty(initUOM, uom, adj, pack, uom3, context.getState()); //05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
				}
				if(toggleValue.equals("1")){
					qbe.set("QTY",adj);
				}else{
					form.getFormWidgetByName("TARGETQTY").setValue(adj);
				}
			}
		}
		return RET_CONTINUE;
	}
	
	public String findQty(String initUOM, String currentUOM, String initQTY, String pack, String uom3, StateInterface state) throws UserException{
		String newValue = null;
//		AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
		if(initUOM.equals(uom3) || currentUOM.equals(uom3)){
			newValue = UOMMappingUtil.convertUOMQty(initUOM, currentUOM, initQTY, pack, state,UOMMappingUtil.uowNull, true);//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
		}
		else{
			newValue = UOMMappingUtil.convertUOMQty(initUOM, UOMMappingUtil.UOM_EA, initQTY, pack, state,UOMMappingUtil.uowNull, true);//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
			newValue = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA, currentUOM, newValue, pack, state,UOMMappingUtil.uowNull, true);//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
		}
		return newValue;
	}

	

	
	private void discardUOMChangeIfSKUIsEndToEnd(QBEBioBean qbe, RuntimeFormWidgetInterface sourceWidget, String uom3 ) throws UserException{
		
		String EACH = uom3; //AW 06/02/09 Modified for Machine#:2093019 SDIS:SCM-00000-05440
		String displayValue = sourceWidget.getDisplayValue();
		String sku = (String)qbe.get("SKU");
		String storerkey = (String)qbe.get("STORERKEY");

		if(sku==null || storerkey==null){
			return;
		}
		
		if(SkuSNConfDAO.isSkuSerialNumberEndtoEnd(storerkey, sku) && !displayValue.equalsIgnoreCase(EACH)){
			qbe.set("UOM", EACH);
			throw new UserException("WMEXP_INVALID_SERIAL_UOM", new String[]{EACH});
		}
	}
}


