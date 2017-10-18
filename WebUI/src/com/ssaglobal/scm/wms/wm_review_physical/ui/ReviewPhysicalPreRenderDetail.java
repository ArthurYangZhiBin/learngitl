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


import javax.servlet.http.HttpSession;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;
import com.epiphany.shr.ui.state.StateInterface;//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530

public class ReviewPhysicalPreRenderDetail extends FormExtensionBase{


	protected static ILoggerCategory _log = LoggerFactory.getInstance(ReviewPhysicalPreRenderDetail.class);
	private static String DEC_FORMAT = "#.00000";
	
	@Override
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws UserException, EpiException
    {
		//Pre Render Review Physical Detail form 
		DataBean focus = form.getFocus();
		//If record is unsaved, mark fields required
		if(focus.isTempBio()){
			setAsRequired(form.getFormWidgetByName("TEAM"));
			setAsRequired(form.getFormWidgetByName("STORERKEY"));
			setAsRequired(form.getFormWidgetByName("SKU"));
			setAsRequired(form.getFormWidgetByName("LOT"));
			setAsRequired(form.getFormWidgetByName("LOC"));
			setAsRequired(form.getFormWidgetByName("PACKKEY"));
			setAsRequired(form.getFormWidgetByName("UOM"));
			setAsRequired(form.getFormWidgetByName("QTY"));
		}else{
			//If record is saved mark fields read-only
			form.getFormWidgetByName("TEAM").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("STORERKEY").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("SKU").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("LOT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("LOC").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("ID").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("INVENTORYTAG").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			BioBean bio = (BioBean)focus;
			String uom = bio.get("UOM").toString();
			Object pack = bio.get("PACKKEY");	//AW 10/14/2008 Machine#:2093019 SDIS:SCM-00000-05440
		    if(pack !=null){	//AW 10/14/2008 Machine#:2093019 SDIS:SCM-00000-05440
			String uom3 = UOMMappingUtil.getPACKUOM3Val(pack.toString(), context.getState().getDefaultUnitOfWork());//AW 10/14/2008 Machine#:2093019 SDIS:SCM-00000-05440
			HttpSession session = context.getState().getRequest().getSession();
			Object tempObj = session.getAttribute("RPPREVUOM");
//			AW 10/14/2008 Machine#:2093019 SDIS:SCM-00000-05440
			if(!uom.equals(uom3) && tempObj==null){
				String qty = bio.get("QTY").toString();	
				form.getFormWidgetByName("QTY").setDisplayValue(nonZero(uom, qty, pack.toString(), context.getState()));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
			}
		    }//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
		}		
		return RET_CONTINUE;
    }
	
	//Method to set widget properties as required
	public void setAsRequired(RuntimeFormWidgetInterface widget){
		widget.setProperty("input type", "required");
		widget.setProperty("label class", "epnyRequired");
		widget.setProperty("label clickable class", "epnyLabelClickableRequired");
		widget.setProperty("label clickable mouseover class", "epnyLabelClickableRequiredMouseover");
	}
	
	/**
	 * Modification History
	 * 04/14/2009	AW  Machine#:2093019 SDIS:SCM-00000-05440
	 * 05/19/2009   	AW  SDIS:SCM-00000-06871 Machine:2353530
	 * 			    UOM conversion is now done in the front end.
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

}
