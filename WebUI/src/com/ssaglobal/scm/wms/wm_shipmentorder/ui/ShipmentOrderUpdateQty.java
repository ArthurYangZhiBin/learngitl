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

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;

public class ShipmentOrderUpdateQty extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ShipmentOrderUpdateQty.class);
	
	
	@Override
	protected int execute(ActionContext context, ActionResult result) throws UserException, EpiException{
		UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
		DataBean focus = context.getState().getFocus();
		RuntimeFormInterface form = context.getSourceWidget().getForm();
		String prevuom = form.getFormWidgetByName("PREVUOM").getValue().toString();
		String uom = "", pack = "";		
		String holder = ""; //06/03/2009 AW	Machine#:2353530 SDIS:SCM-00000-06871
		RuntimeFormWidgetInterface openQTY = form.getFormWidgetByName("OPENQTY");
		
		if(focus.isTempBio()){
			//bug 6113. 
			
			QBEBioBean qbe = (QBEBioBean)focus;
			uom = (String) qbe.get("UOM");
			pack = (String) qbe.get("PACKKEY");
			if(uom==null){
				String[] parameter = new String[1];
				parameter[0] = colonStrip(readLabel(form.getFormWidgetByName("UOM")));
				throw new FormException("WMEXP_OWNER_NOT_NULL", parameter);
			}
			
			//Update Quantity Field
			if(openQTY.getDisplayValue()!=null){
				//DISABLING DUE TO ISSUE 6113 - NO CONVERSION ON A NEW RECORD
				//calcQty(holder, prevuom, uom, pack, openQTY, qbe);
			}
		}else{
			BioBean bio = (BioBean)focus;
			uom = bio.get("UOM").toString();
			pack = bio.get("PACKKEY").toString();
			
			if(prevuom.startsWith("-1")){
				prevuom = prevuom.substring(2);
				//Commenting this out because it seems to be adding an extra conversion that is incorrect
				//				if(!prevuom.equals(UOMMappingUtil.getPACKUOM3Val(pack, uowb))){
				//					String temp = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA, prevuom, openQTY.getDisplayValue(), pack, context.getState(), null, true);
				//					openQTY.setDisplayValue(LocaleUtil.checkLocaleAndParseQty(temp));
				//				}
			}
			//Update Quantity Fields
			calcQty(holder, prevuom, uom, pack, openQTY, bio, uowb, context.getState());	//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
		}
		form.getFormWidgetByName("PREVUOM").setValue(uom);
		return RET_CONTINUE;
	}

//	public void calcQty(String[] holder, String prevuom, String uom, String pack, RuntimeFormWidgetInterface widget, QBEBioBean qbe) throws UserException{
//		String qty = sanitizeNumber(widget.getDisplayValue());
//		if(!qty.equals("")){
//			holder = findQty(prevuom, uom, qty, pack);
//			qbe.set(widget.getName(), holder[0]);
//			widget.setDisplayValue(holder[1]);
//		}
//	}
	/**
	 * Modification History
	 * 04/14/2009	AW  Machine#:2093019 SDIS:SCM-00000-05440
	 * 03/22/2009	AW		Infor365:217417
	 * 						In certain locales, the precision was defaulting to 3 
	 */
	public void calcQty(String holder, String prevuom, String uom, String pack, RuntimeFormWidgetInterface widget, BioBean bio, UnitOfWorkBean uowb, StateInterface st) throws UserException, EpiException{
		String disp = widget.getDisplayValue();
		_log.debug("LOG_SYSTEM_OUT","\n\nDISPLAY VALUE: "+disp+"\n\n",100L);
		String qty = sanitizeNumber(disp);
		double qtyValue = Double.parseDouble(qty);
		if(!qty.equals("") && qtyValue!=0){
			holder = findQty(prevuom, uom, qty, pack, uowb, st);//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
			bio.set(widget.getName(), holder);
			widget.setDisplayValue(LocaleUtil.checkLocaleAndParseQty(holder, LocaleUtil.TYPE_QTY)); //AW Infor365:217417 03/22/10			
		}
	}
	/**
	 * Modification History
	 * 04/14/2009	AW  Machine#:2093019 SDIS:SCM-00000-05440
	 * 
	 */
	public String findQty(String initUOM, String currentUOM, String initQTY, String pack, UnitOfWorkBean uowb, StateInterface st ) throws UserException, EpiException{
		String newValue = null; //06/03/2009 AW	Machine#:2353530 SDIS:SCM-00000-06871
		String uom3 = UOMMappingUtil.getPACKUOM3Val(pack, uowb);//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
		if(initUOM.equals(uom3) || currentUOM.equals(uom3)){//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
			newValue = UOMMappingUtil.convertUOMQty(initUOM, currentUOM, initQTY, pack, st, null, true);//06/03/2009 AW	Machine#:2353530 SDIS:SCM-00000-06871
		}
		else{
			newValue = UOMMappingUtil.convertUOMQty(initUOM, UOMMappingUtil.UOM_EA, initQTY, pack, st, null, true);//06/03/2009 AW	Machine#:2353530 SDIS:SCM-00000-06871
			newValue = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA, currentUOM,  newValue, pack, st, null, true);//06/03/2009 AW	Machine#:2353530 SDIS:SCM-00000-06871
		}
		return newValue;
	}
	
	private String sanitizeNumber(String number){
		NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW
		String numberS;
		try{
			numberS = nf.parse(number).toString();
		} catch (ParseException e){
			e.printStackTrace();
			Pattern pattern = Pattern.compile("\\,");
			Matcher matcher = pattern.matcher(number);
			return matcher.replaceAll("");
		}
		return numberS;
	}
	
	private String colonStrip(String label){
		Pattern pattern = Pattern.compile("\\:");
		Matcher matcher = pattern.matcher(label);
		return 	matcher.replaceAll("");
	}
	
	private String readLabel(RuntimeFormWidgetInterface widget){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widget.getLabel("label",locale);
	}
}
