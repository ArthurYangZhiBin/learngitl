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


package com.ssaglobal.scm.wms.wm_adjustment.ui;


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
import com.ssaglobal.scm.wms.util.UOMMappingUtil;
import com.ssaglobal.scm.wms.uiextensions.UOMDefaultValue;
import com.ssaglobal.scm.wms.wm_cyclecount.ui.CalculateAdvCatchWeightsHelper;

public class HiddenAdjCatchWeightFieldsUI extends ActionExtensionBase{
	//Form name constants
	private static String TAB_GROUP = "tbgrp_slot";
	private static String TAB0 = "tab 0";
	
	
	//Table name constants

	
	//Widget name constants
	private static String OWNER = "STORERKEY";

	private static String ITEM = "SKU";	
	private static String enableadvcwgt="enableadvcwgt";
	
	private static String ADJGROSSWGT="ADJGROSSWGT";
	private static String ADJNETWGT="ADJNETWGT";
	private static String ADJTAREWGT="ADJTAREWGT";
	private static String ACTUALGROSSWGT="ACTUALGROSSWGT";
	private static String ACTUALNETWGT="ACTUALNETWGT";
	private static String ACTUALTAREWGT="ACTUALTAREWGT";
	
	
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		StateInterface state = context.getState();
		RuntimeFormWidgetInterface source = context.getSourceWidget();
		QBEBioBean focus = (QBEBioBean)source.getForm().getFocus();
		
			
			//Get the storer from the Header
			//String shellSlot1 = "list_slot_1";
			//String listShellForm = "wms_list_shell";
			String ownerValue = null;
			String itemValue=null;
			boolean enabledadvCatWgt=false;
			/*
			RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();				//get the toolbar
			RuntimeFormInterface shellForm = state.getCurrentRuntimeForm();
			while(!(shellForm.getName().equals(listShellForm))){
				shellForm = shellForm.getParentForm(state);
			}
			SlotInterface slot1 = shellForm.getSubSlot(shellSlot1);
			RuntimeFormInterface headerForm = state.getRuntimeForm(slot1, null);
			SlotInterface headerTbgrpSlot = headerForm.getSubSlot(TAB_GROUP);
			RuntimeFormInterface normalHeaderForm = state.getRuntimeForm(headerTbgrpSlot, TAB0);
			*/
			
			ownerValue = source.getForm().getFormWidgetByName("STORERKEY").getValue().toString();
			itemValue=source.getForm().getFormWidgetByName("SKU").getValue().toString();
			
			
			
	
			//Query sku table for data points
			RuntimeFormInterface currentForm = source.getForm();
			CalculateAdvCatchWeightsHelper helper = new CalculateAdvCatchWeightsHelper();
			
				try{
					String enabledAdvCatWght=helper.isAdvCatchWeightEnabled(ownerValue,itemValue);
					if((enabledAdvCatWght!=null)&&(enabledAdvCatWght.equalsIgnoreCase("1")))
					{
						enabledadvCatWgt=true;
					}
					
					if(enabledadvCatWgt)
					{
						enabledisableWgtWidgets(currentForm,Boolean.FALSE);
						
						HashMap enabledInfo=helper.getEnabledWeightInfo(ownerValue,itemValue);					
						
						if(((String)enabledInfo.get("catchgrosswgt")).equalsIgnoreCase("1")) currentForm.getFormWidgetByName(ACTUALGROSSWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.FALSE);
						else currentForm.getFormWidgetByName(ACTUALGROSSWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
						
						if(((String)enabledInfo.get("catchnetwgt")).equalsIgnoreCase("1")) currentForm.getFormWidgetByName(ACTUALNETWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.FALSE);
						else currentForm.getFormWidgetByName(ACTUALNETWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
						
						if(((String)enabledInfo.get("catchtarewgt")).equalsIgnoreCase("1")) currentForm.getFormWidgetByName(ACTUALTAREWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.FALSE);
						else currentForm.getFormWidgetByName(ACTUALTAREWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
						
						if(((String)enabledInfo.get("catchgrosswgt")).equalsIgnoreCase("1")) currentForm.getFormWidgetByName(ADJGROSSWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.FALSE);
						else currentForm.getFormWidgetByName(ADJGROSSWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
						
						if(((String)enabledInfo.get("catchnetwgt")).equalsIgnoreCase("1")) currentForm.getFormWidgetByName(ADJNETWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.FALSE);
						else currentForm.getFormWidgetByName(ADJNETWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
						
						if(((String)enabledInfo.get("catchtarewgt")).equalsIgnoreCase("1")) currentForm.getFormWidgetByName(ADJTAREWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.FALSE);
						else currentForm.getFormWidgetByName(ADJTAREWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);

						
					}	
					else
					{									
						currentForm.getFormWidgetByName(ACTUALGROSSWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
						currentForm.getFormWidgetByName(ACTUALNETWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
						currentForm.getFormWidgetByName(ACTUALTAREWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
						currentForm.getFormWidgetByName(ADJGROSSWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
						currentForm.getFormWidgetByName(ADJNETWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
						currentForm.getFormWidgetByName(ADJTAREWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, Boolean.TRUE);
						
					}	
					
					
				   
				    result.setFocus(focus);	   
					
									
				}catch(Exception e){
					e.printStackTrace();
					return RET_CANCEL;
				}

		return RET_CONTINUE;
	}
	
	public void enabledisableWgtWidgets(RuntimeFormInterface currentForm,Boolean bullObj)
	{
	}
	
	
}
