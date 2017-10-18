/**
 * ---Begin Copyright Notice---20090105T1353
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2011 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */
package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;
import com.ssaglobal.scm.wms.wm_cyclecount.ui.CalculateAdvCatchWeightsHelper;

/**
 * TODO Document ConvertQtyWgtByUOM class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class ConvertQtyWgtByUOM extends com.epiphany.shr.ui.action.ActionExtensionBase{
	private static String OWNER = "STORERKEY";
	private static String ITEM = "SKU";
	private static String EXPECTEDQTY= "EXPECTEDQTY";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ConvertQtyWgtByUOM.class);
	   protected int execute( ActionContext context, ActionResult result ) throws EpiException {
			StateInterface state = context.getState();
			RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
			String ownerValue = currentForm.getFormWidgetByName(OWNER).getDisplayValue();
			String itemValue= currentForm.getFormWidgetByName(ITEM).getDisplayValue();
			String expectedQty = currentForm.getFormWidgetByName(EXPECTEDQTY).getDisplayValue();
		   
		   
		   DataBean headerBean = context.getState().getCurrentRuntimeForm().getFocus();
		   
		   if (headerBean instanceof BioBean){

			   String PackKeyWidget = (String)getParameter("PACKKEY_WIDGET");
			   ArrayList ScreenwidgetNames = (ArrayList) getParameter("ScreenQtyWidgetName");
			   ArrayList BiowidgetNames = (ArrayList) getParameter("BioQtyWidgetName");

			   RuntimeFormInterface CurrentForm = context.getState().getCurrentRuntimeForm();
			   String packKey = CurrentForm.getFormWidgetByName(PackKeyWidget).getDisplayValue();
			   String uom = context.getSourceWidget().getValue().toString();
			   for(int i = 0; i < ScreenwidgetNames.size(); i++){
				   String qty = headerBean.getValue(BiowidgetNames.get(i).toString()).toString();
				   _log.debug("LOG_SYSTEM_OUT","Qty from the bio = "+ qty,100L);
				   if (! qty.equalsIgnoreCase("0")){
					   String ConvExpQty = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA, uom,qty, packKey,context.getState(), UOMMappingUtil.uowNull, true); //05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
					   CurrentForm.getFormWidgetByName(ScreenwidgetNames.get(i).toString()).setValue(ConvExpQty);
				   }
				}
			}
		   if (headerBean instanceof QBEBioBean){//it is new ASN, we recalculate weight, not view qty

			   String PackKeyWidget = (String)getParameter("PACKKEY_WIDGET");

			   RuntimeFormInterface CurrentForm = context.getState().getCurrentRuntimeForm();
			   String packKey = CurrentForm.getFormWidgetByName(PackKeyWidget).getDisplayValue();
			   String uom = context.getSourceWidget().getValue().toString();
			   CalculateAdvCatchWeightsHelper helper = new CalculateAdvCatchWeightsHelper();
			   float expectedQtyFloat= Float.parseFloat(expectedQty);
			   HashMap actualWgts = null;
			   actualWgts=helper.getCalculatedWeightsLot( ownerValue, itemValue,expectedQtyFloat, uom, packKey);	
			   ((QBEBioBean) headerBean).setValue("GROSSWGT", (Double)actualWgts.get("GROSSWEIGHT"));
			   ((QBEBioBean) headerBean).setValue("NETWGT", (Double)actualWgts.get("NETWEIGHT"));
			   ((QBEBioBean) headerBean).setValue("TAREWGT", (Double)actualWgts.get("TAREWEIGHT"));
			   
			  
//				   CurrentForm.getFormWidgetByName("GROSSWGT").setValue(""+(Double)actualWgts.get("GROSSWEIGHT"));
//				   CurrentForm.getFormWidgetByName("NETWGT").setValue(""+(Double)actualWgts.get("NETWEIGHT"));
//				   CurrentForm.getFormWidgetByName("TAREWGT").setValue(""+(Double)actualWgts.get("TAREWEIGHT"));				   
			}
	      return super.execute( context, result );
	   }
}
