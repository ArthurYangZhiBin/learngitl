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

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_shipmentorder.ui.ShipmentOrderUpdateQty;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440

public class ReviewPhysicalUpdateQty extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ShipmentOrderUpdateQty.class);
	
	//Static globally referenced variables
	//private static String DEC_FORMAT = "#.00000";
	/**
	 * Modification History
	 * 04/14/2009	AW  Machine#:2093019 SDIS:SCM-00000-05440
	 * 06/03/2009 AW	Machine#:2353530 SDIS:SCM-00000-06871
	 */
	protected int execute(ActionContext context, ActionResult result) throws UserException, EpiException{
		DataBean focus = context.getState().getFocus();
		UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
		RuntimeFormInterface form = context.getSourceWidget().getForm();
		String uom = "";
		String pack = "";	
		String uom3= "";//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
		String holder = null; //06/03/2009 AW	Machine#:2353530 SDIS:SCM-00000-06871
		RuntimeFormWidgetInterface qty = form.getFormWidgetByName("QTY");
		
		if(focus.isTempBio()){			
			QBEBioBean qbe = (QBEBioBean)focus;
			uom = (String) qbe.get("UOM");
			pack = (String) qbe.get("PACKKEY");
			uom3 = UOMMappingUtil.getPACKUOM3Val(pack, uowb); //AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
			if(uom==null){
				String[] parameter = new String[1];
				parameter[0] = colonStrip(readLabel(form.getFormWidgetByName("UOM")));
				throw new FormException("WMEXP_OWNER_NOT_NULL", parameter);
			}
		}else{
			BioBean bio = (BioBean)focus;
			uom = bio.get("UOM").toString();
			pack = bio.get("PACKKEY").toString();
			HttpSession session = context.getState().getRequest().getSession();
			Object tempObj = session.getAttribute("RPPREVUOM");
			String prevuom;
			if(tempObj!=null){
				prevuom = tempObj.toString();
			}else{
				prevuom = uom3; //AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
			}
			//Update Quantity Fields
			calcQty(holder, prevuom, uom, pack, qty, bio, uom3, context.getState());//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
			session.setAttribute("RPPREVUOM", uom);
		}

		return RET_CONTINUE;
	}
	/**
	 * Modification History
	 * 04/14/2009	AW  Machine#:2093019 SDIS:SCM-00000-05440
	 * 06/03/2009	AW	Machine#:2353530 SDIS:SCM-00000-06871 
	 * 03/22/2010	AW	Infor365:217417
	 * 					In certain locales, the precision was defaulting to 3
	 */
	public void calcQty(String holder, String prevuom, String uom, String pack, RuntimeFormWidgetInterface widget, QBEBioBean qbe, String uom3, StateInterface st) throws UserException{
		String qty = sanitizeNumber(widget.getDisplayValue());
		if(!qty.equals("")){
			holder = findQty(prevuom, uom, qty, pack, uom3, st); //AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
			qbe.set(widget.getName(), holder);//06/03/2009 AW	Machine#:2353530 SDIS:SCM-00000-06871
			widget.setDisplayValue(LocaleUtil.checkLocaleAndParseQty(holder, LocaleUtil.TYPE_QTY));//AW Infor365:217417 03/22/10
		}
	}
	
	/**
	 * 
	 * @param holder
	 * @param prevuom
	 * @param uom
	 * @param pack
	 * @param widget
	 * @param bio
	 * @param uom3
	 * @param st
	 * @throws UserException
	 * 
	 * Modification History
	 * 03/22/2010	AW		Infor365:217417
	 * 						In certain locales, the precision was defaulting to 3
	 */
	public void calcQty(String holder, String prevuom, String uom, String pack, RuntimeFormWidgetInterface widget, BioBean bio, String uom3, StateInterface st) throws UserException{
		String qty = sanitizeNumber(widget.getDisplayValue());
		double qtyValue = Double.parseDouble(qty);
		if(!qty.equals("") && qtyValue!=0){
			holder = findQty(prevuom, uom, qty, pack, uom3, st); //AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
			bio.set(widget.getName(), holder); //06/03/2009 AW	Machine#:2353530 SDIS:SCM-00000-06871
			widget.setDisplayValue(LocaleUtil.checkLocaleAndParseQty(holder, LocaleUtil.TYPE_QTY)); //AW Infor365:217417 03/22/10 
		}
	}
	/**
	 * Modification History
	 * 04/14/2009	AW  Machine#:2093019 SDIS:SCM-00000-05440
	 * 06/03/2009	AW	Machine#:2353530 SDIS:SCM-00000-06871 
	 */
	public String findQty(String initUOM, String currentUOM, String initQTY, String pack, String packuom3, StateInterface st) throws UserException{
		String newValue = null;
//		AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
		if(initUOM.equals(packuom3) || currentUOM.equals(packuom3)){
			newValue = UOMMappingUtil.convertUOMQty(initUOM, currentUOM, initQTY, pack, st, null, true);//06/03/2009 AW	Machine#:2353530 SDIS:SCM-00000-06871 
		}else{ 
			newValue = UOMMappingUtil.convertUOMQty(initUOM, UOMMappingUtil.UOM_EA, initQTY, pack, st, null, true);//06/03/2009	AW	Machine#:2353530 SDIS:SCM-00000-06871
			newValue = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA, currentUOM, newValue, pack, st, null, true); //06/03/2009	AW	Machine#:2353530 SDIS:SCM-00000-06871
		}

		return newValue;
	}

	
	private String sanitizeNumber(String number){
		NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW
		String numberS;
		try
		{
			numberS = nf.parse(number).toString();
		} catch (ParseException e)
		{
			// Auto-generated catch block
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