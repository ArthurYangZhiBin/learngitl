/****************************************************************************
 *               Copyright (c) 1999-2003 E.piphany, Inc.                    *
 *                          ALL RIGHTS RESERVED                             *
 *                                                                          *
 *     THIS PROGRAM CONTAINS PROPRIETARY INFORMATION OF E.PIPHANY, INC.     *
 *     ----------------------------------------------------------------     *
 *                                                                          *
 * THIS PROGRAM CONTAINS THE CONFIDENTIAL AND/OR PROPRIETARY INFORMATION    *
 * OF E.PIPHANY, INC.  ANY DUPLICATION, MODIFICATION, DISTRIBUTION, PUBLIC  *
 * PERFORMANCE, OR PUBLIC DISPLAY OF THIS PROGRAM, OR ANY PORTION OR        *
 * DERIVATION THEREOF WITHOUT THE EXPRESS WRITTEN CONSENT OF                *
 * E.PIPHANY, INC. IS STRICTLY PROHIBITED.  USE OR DISCLOSURE OF THIS       *
 * PROGRAM DOES NOT CONVEY ANY RIGHTS TO REPRODUCE, DISCLOSE OR DISTRIBUTE  *
 * ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT CONTAINS IN  *
 * WHOLE OR IN PART ANY ASPECT OF THIS PROGRAM.                             *
 *                                                                          *
 ****************************************************************************
 */


package com.ssaglobal.scm.wms.wm_asnreceipts.ui;


//Import 3rd party packages and classes

import java.util.HashMap;

//Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;
import com.ssaglobal.scm.wms.uiextensions.UOMDefaultValue;
import com.ssaglobal.scm.wms.wm_cyclecount.ui.CalculateAdvCatchWeightsHelper;
import com.ssaglobal.scm.wms.wm_table_validation.ccf.NumericValidationCCF;
//Added for WM 9 3PL Enhancements - Catch weight tracking
public class ValidateAdvCatchWeightUI extends ActionExtensionBase{
	//Form name constants
	private static String TAB_GROUP = "tbgrp_slot";
	private static String TAB0 = "tab 0";
	
	
	//Table name constants
	private static String TABLE_ITEM = "sku";
	
	//Widget name constants
	private static String OWNER = "STORERKEY";
	private static String UOM = "UOM";
	private static String ITEM = "SKU";
	private static String PACKKEY = "PACKKEY";	
	private static String enableadvcwgt="enableadvcwgt";
	private static String grosswgt1="GROSSWGT";
	private static String netwgt1="NETWGT";
	private static String tarewgt1="TAREWGT";
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		StateInterface state = context.getState();
		DataBean focus = state.getFocus();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		String strQtyReceived = context.getSourceWidget().getDisplayValue();
		float fltQtyReceived = new Float(NumericValidationCCF.parseNumber(strQtyReceived)).floatValue();
//		if((qtyReceived !=null)&&(Double.parseDouble(qtyReceived)>0 ))
//		{	
		
			//Get the storer from the Header
			String shellSlot1 = "list_slot_1";
			String listShellForm = "wms_list_shell";
			String ownerValue = null;
			String itemValue=null;
			String uomValue = null;
			String packKeyValue=null;			
			boolean enabledadvCatWgt=false;
			Double dblGrossWgt=new Double(0);
			Double dblNetWgt=new Double(0);
			Double dblTareWgt=new Double(0);
			
//			RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();				//get the toolbar
//			RuntimeFormInterface shellForm = state.getCurrentRuntimeForm();
//			while(!(shellForm.getName().equals(listShellForm))){
//				shellForm = shellForm.getParentForm(state);
//			}
//			SlotInterface slot1 = shellForm.getSubSlot(shellSlot1);
//			RuntimeFormInterface headerForm = state.getRuntimeForm(slot1, null);
//			SlotInterface headerTbgrpSlot = headerForm.getSubSlot(TAB_GROUP);
//			RuntimeFormInterface normalHeaderForm = state.getRuntimeForm(headerTbgrpSlot, TAB0);			
//			ownerValue = normalHeaderForm.getFormWidgetByName(OWNER).getDisplayValue();
			
	
			//Query sku table for data points
			RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
			ownerValue = currentForm.getFormWidgetByName(OWNER).getDisplayValue();
			itemValue= currentForm.getFormWidgetByName(ITEM).getDisplayValue();
			uomValue=currentForm.getFormWidgetByName(UOM).getDisplayValue();
			packKeyValue=currentForm.getFormWidgetByName(PACKKEY).getDisplayValue();
			CalculateAdvCatchWeightsHelper helper = new CalculateAdvCatchWeightsHelper();
			
			/*
			String qry = TABLE_ITEM+"."+OWNER+"='"+ownerValue+"' AND "+TABLE_ITEM+"."+ITEM+"='"+itemValue+"'";
			String packVal= null; //AW Machine#:2093019 SDIS:SCM-00000-05440 04/14/2009
			Query query = new Query(TABLE_ITEM, qry, null);		
			BioCollectionBean itemBio = uow.getBioCollectionBean(query);
			*/
				try{
					String enabledAdvCatWght=helper.isAdvCatchWeightEnabled(ownerValue,itemValue);
					if((enabledAdvCatWght!=null)&&(enabledAdvCatWght.equalsIgnoreCase("1")))
					{
						enabledadvCatWgt=true;
						//TODO calculate weights & assign to the variables

						   
						   HashMap actualWgts = null;
						   actualWgts=helper.getCalculatedWeightsLot( ownerValue, itemValue,fltQtyReceived, uomValue, packKeyValue);					   
						   dblGrossWgt= (Double)actualWgts.get("GROSSWEIGHT");
						   dblNetWgt= (Double)actualWgts.get("NETWEIGHT");
						   dblTareWgt= (Double)actualWgts.get("TAREWEIGHT");
						   
						   
						   if (dblTareWgt.doubleValue()==0) dblTareWgt=new Double(dblGrossWgt.doubleValue()-dblNetWgt.doubleValue());						   
						   if (dblNetWgt.doubleValue()==0) dblNetWgt=new Double(dblGrossWgt.doubleValue()-dblTareWgt.doubleValue());
						   if (dblGrossWgt.doubleValue()==0) dblGrossWgt=new Double(dblNetWgt.doubleValue()+dblTareWgt.doubleValue());  
						   
						   
					}
					
					if(focus.isTempBio()){
						QBEBioBean qbe = (QBEBioBean) focus;
						context.getState().getCurrentRuntimeForm();
						qbe.set(grosswgt1, dblGrossWgt);
						qbe.set(netwgt1, dblNetWgt);
						qbe.set(tarewgt1, dblTareWgt);
						
					}else{
						BioBean bio = (BioBean) focus;
						bio.set(grosswgt1, dblGrossWgt);
						bio.set(netwgt1, dblNetWgt);
						bio.set(tarewgt1, dblTareWgt);
						
						
					}					
					currentForm.getFormWidgetByName(grosswgt1).setDisplayValue(""+dblGrossWgt.doubleValue());
					currentForm.getFormWidgetByName(netwgt1).setDisplayValue(""+dblNetWgt.doubleValue());
					currentForm.getFormWidgetByName(tarewgt1).setDisplayValue(""+dblTareWgt.doubleValue());					
					
					if(enabledadvCatWgt)
					{
						enabledisableWgtWidgets(currentForm,Boolean.FALSE);
						
						HashMap enabledInfo=helper.getEnabledWeightInfo(ownerValue,itemValue);					
						
						if(((String)enabledInfo.get("catchgrosswgt")).equalsIgnoreCase("1")) currentForm.getFormWidgetByName(grosswgt1).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
						else currentForm.getFormWidgetByName(grosswgt1).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
						
						if(((String)enabledInfo.get("catchnetwgt")).equalsIgnoreCase("1")) currentForm.getFormWidgetByName(netwgt1).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
						else currentForm.getFormWidgetByName(netwgt1).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
						
						if(((String)enabledInfo.get("catchtarewgt")).equalsIgnoreCase("1")) currentForm.getFormWidgetByName(tarewgt1).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
						else currentForm.getFormWidgetByName(tarewgt1).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
						
						
					}	
					else
					{									
						currentForm.getFormWidgetByName(grosswgt1).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
						currentForm.getFormWidgetByName(netwgt1).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
						currentForm.getFormWidgetByName(tarewgt1).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					}	
					
					
				    focus.setValue(grosswgt1, dblGrossWgt.doubleValue());
				    focus.setValue(netwgt1, dblNetWgt.doubleValue());
				    focus.setValue(tarewgt1, dblTareWgt.doubleValue());
				   
				    result.setFocus(focus);	   
					
									
				}catch(Exception e){
					e.printStackTrace();
					return RET_CANCEL;
				}
//		}// if qty received >0
		return RET_CONTINUE;
	}
	
	public void enabledisableWgtWidgets(RuntimeFormInterface currentForm,Boolean bullObj)
	{
	}
	
	
}
