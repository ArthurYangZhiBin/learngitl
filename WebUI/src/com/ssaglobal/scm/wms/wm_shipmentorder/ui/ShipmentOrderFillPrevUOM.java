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
import java.text.NumberFormat;
import java.text.ParseException;

import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;

public class ShipmentOrderFillPrevUOM extends FormExtensionBase{
	
	private static String TABLE = "ORDERDETAIL";
	private static String ORDER = "ORDERKEY";
	private static String ORDER_LN = "ORDERLINENUMBER";
	private static String OPEN_QTY = "OPENQTY";
	private static String UOM = "UOM";
	private static String PACK = "PACKKEY";
	private static String PREV_UOM = "PREVUOM";
	private static String DETAIL = "Detail";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ShipmentOrderFillPrevUOM.class);

	@Override
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws UserException, EpiDataException, EpiException{
		DataBean focus = form.getFocus();
		//Convert Open Qty for saved records
		if(focus.isBio()){
			BioBean bio = (BioBean)focus;
			String uom = bio.get(UOM).toString();
			RuntimeFormWidgetInterface openQtyWidget = form.getFormWidgetByName(OPEN_QTY);
			String qty = openQtyWidget.getDisplayValue();
			String formattedQty = LocaleUtil.formatValues(qty, LocaleUtil.TYPE_QTY);//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
			String action = context.getActionObject().getName();
			String pack;
			Object packVal = bio.get(PACK);//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
			if(packVal !=null){//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
				String uom3 = UOMMappingUtil.getPACKUOM3Val(packVal.toString(), context.getState().getDefaultUnitOfWork());//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440

			if(action.equals(DETAIL)){
				//Determine if user has changed the Open Qty in the list view and not saved
				pack = bio.get(PACK).toString();
				String orderKey = bio.get(ORDER).toString(), orderLN = bio.get(ORDER_LN).toString();
				String queryString = "SELECT "+OPEN_QTY+" FROM "+TABLE+" WHERE "+ORDER+"='"+orderKey+"' AND "+ORDER_LN+"='"+orderLN+"'";
				EXEDataObject edo = WmsWebuiValidationSelectImpl.select(queryString);
				
				//Get database value for comparision
				String oOQty = edo.getAttribValue(new TextData(OPEN_QTY)).toString();
				if(!uom.equals(uom3)){
					oOQty = nonZero(uom, oOQty, pack, context.getState());//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
				}
				
				NumberFormat nfDLocal = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW
				
				Number nOOQty = new Double(0.0);
				try {
					nOOQty = nfDLocal.parse(oOQty);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				double origOpenQty = nOOQty.doubleValue();

				Number nQty = new Double(0.0);
				try {
					nQty = nfDLocal.parse(qty);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				double openQty = nQty.doubleValue();
				
				//Convert if the Open Qty in the Bio matches the database value
				if(openQty!=origOpenQty){
					bio.set(OPEN_QTY, formattedQty);	//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530					
					form.getFormWidgetByName(PREV_UOM).setValue(uom);
				}else{
					form.getFormWidgetByName(PREV_UOM).setValue("-1"+uom);					
				}

			}else{
				//Ignore Eaches
				if(!uom.equals(uom3)){//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
					pack = packVal.toString();//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
					//Convert for refreshes
					qty = nonZero(uom, qty, pack, context.getState());//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
				}
			}//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
			bio.set(OPEN_QTY, LocaleUtil.checkLocaleAndParseQty(qty, LocaleUtil.TYPE_QTY));	//AW Infor365:217417 03/22/10	
			}
			
			//Set new value to bio and store the previous UOM for next update
			openQtyWidget.setDisplayValue(LocaleUtil.checkLocaleAndParseQty(qty, LocaleUtil.TYPE_QTY));//AW Infor365:217417 03/22/10


			
			_log.debug("LOG_SYSTEM_OUT","QTY: "+qty+" UOM: "+uom+" CHECK SET QTY: "+openQtyWidget.getDisplayValue(),100L);
		}else{
			//Is new record
			form.getFormWidgetByName(PREV_UOM).setValue(UOMMappingUtil.getPACKUOM3Val(UOMMappingUtil.PACK_STD, context.getState().getDefaultUnitOfWork()));//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
		}
		return RET_CONTINUE;
	}
	/**
	 * Modification History
	 * 10/15/2008	AW  Machine#:2093019 SDIS:SCM-00000-05440
	 * 04/14/2009	AW  Machine#:2093019 SDIS:SCM-00000-05440
     * 05/19/2009   AW      SDIS:SCM-00000-06871 Machine:2353530
	 * 		     UOM conversion is now done in the front end.
	 *                                  Changes were made accordingly.
	 */	
	public static String nonZero(String uom, String qty, String pack, StateInterface state) throws UserException{
		double temp = Double.parseDouble(qty);
		if(temp!=0.0){
			return UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA,uom, qty, pack, state, UOMMappingUtil.uowNull, true);//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
		}else{
			return "0";
		}
	}


}