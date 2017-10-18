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
package com.ssaglobal.scm.wms.wm_ws_defaults;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.EpiRuntimeException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.util.UserUtil;


public class WSNormalFormPrerenderAction extends FormExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WSNormalFormPrerenderAction.class);
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {
		_log.debug("LOG_DEBUG_EXTENSION_WSNORMFRMPREREN","Executing WSNormalFormPrerenderAction",100L);		
		StateInterface state = context.getState();			
		ArrayList  bioAttrUsingOwner = (ArrayList)getParameter("bioAttrUsingOwner");
		ArrayList bioAttrUsingReceiveLocation = (ArrayList)getParameter("bioAttrUsingReceiveLocation");
		ArrayList bioAttrUsingPackKey = (ArrayList)getParameter("bioAttrUsingPackKey");
		ArrayList bioAttrUsingPickCode = (ArrayList)getParameter("bioAttrUsingPickCode");
		ArrayList bioAttrUsingBayStart = (ArrayList)getParameter("bioAttrUsingBayStart");
		ArrayList bioAttrUsingBayEnd = (ArrayList)getParameter("bioAttrUsingBayEnd");
		ArrayList bioAttrUsingSlotStart = (ArrayList)getParameter("bioAttrUsingSlotStart");
		ArrayList bioAttrUsingSlotEnd = (ArrayList)getParameter("bioAttrUsingSlotEnd");	
		String noFocus = (String)getParameter("noFocus");
		boolean hasFocus = (noFocus != null && noFocus.equalsIgnoreCase("true"))?false:true;		
		Integer ownerLockFlag = WSDefaultsUtil.getOwnerLockFlag(state);
		boolean isOwnerLocked = (ownerLockFlag == null || ownerLockFlag.intValue() == 0)?false:true;
		
		
		if(hasFocus && form.getFocus() == null){			
			throw new InvalidParameterException("Focus cannot be NULL if noFocus is set to false.");	
		}
		
		//Assert that this is a new record if a focus is present
		if(hasFocus && !form.getFocus().isTempBio()){
			_log.debug("LOG_DEBUG_EXTENSION_WSNORMFRMPREREN","Not A New Record...",100L);			
			_log.debug("LOG_DEBUG_EXTENSION_WSNORMFRMPREREN","Exiting WSNormalFormPrerenderAction",100L);			
			return RET_CONTINUE;
		}
				
		QBEBioBean formFocus = (QBEBioBean)form.getFocus();

		if(!WSDefaultsUtil.isCacheAvaliable(state)){ 			
			_log.debug("LOG_DEBUG_EXTENSION_WSNORMFRMPREREN","Exiting Without Performing Any Actions Default Cache Not Found...",100L);
			_log.debug("LOG_DEBUG_EXTENSION_WSNORMFRMPREREN","Exiting WSNormalFormPrerenderAction",100L);
			return RET_CONTINUE;
		}			
		
		//If There is a default OWNER and there are bio attributes to populate with it then do so.
		
		//If owner is locked then get default from pre-filter values
		//else get it from default values
		String defaultStorerKey = null;
		if(!isOwnerLocked){
			defaultStorerKey = WSDefaultsUtil.getDefaultStorer(state);
		}
		else{
			defaultStorerKey = WSDefaultsUtil.getPreFilterValueByType("STORER", state);
		}
		if(		defaultStorerKey != null && 
				defaultStorerKey.length() > 0 && 
				bioAttrUsingOwner != null &&
				bioAttrUsingOwner.size() > 0){
			//iterate through bio attributes and populate
			for(int i = 0; i < bioAttrUsingOwner.size(); i++){
				if(hasFocus)
					formFocus.set(bioAttrUsingOwner.get(i).toString(),defaultStorerKey);
				else
					form.getFormWidgetByName(bioAttrUsingOwner.get(i).toString()).setDisplayValue(defaultStorerKey);
			}
		}

		//If There is a default LOCATION and there are bio attributes to populate with it then do so.
		String defaultLoc = WSDefaultsUtil.getDefaultRecieveLoc(state);
		if(		defaultLoc != null && 
				defaultLoc.length() > 0 && 
				bioAttrUsingReceiveLocation != null &&
				bioAttrUsingReceiveLocation.size() > 0){
//			iterate through bio attributes and populate
			for(int i = 0; i < bioAttrUsingReceiveLocation.size(); i++){
				if(hasFocus)
					formFocus.set(bioAttrUsingReceiveLocation.get(i).toString(),defaultLoc);
				else
					form.getFormWidgetByName(bioAttrUsingReceiveLocation.get(i).toString()).setDisplayValue(defaultLoc);
			}
		}
		//If There is a default PACK and there are bio attributes to populate with it then do so.
		String defaultPack = WSDefaultsUtil.getDefaultPack(state);
		if(		defaultPack != null && 
				defaultPack.length() > 0 && 
				bioAttrUsingPackKey != null &&
				bioAttrUsingPackKey.size() > 0){
//			iterate through bio attributes and populate
			for(int i = 0; i < bioAttrUsingPackKey.size(); i++){
				if(hasFocus)
					formFocus.set(bioAttrUsingPackKey.get(i).toString(),defaultPack);
				else
					form.getFormWidgetByName(bioAttrUsingPackKey.get(i).toString()).setDisplayValue(defaultPack);
			}
		}
		//If There is a default PICKCODE and there are bio attributes to populate with it then do so.
		String defaultPickCode = WSDefaultsUtil.getDefaultPickCode(state);
		if(		defaultPickCode != null && 
				defaultPickCode.length() > 0 && 
				bioAttrUsingPickCode != null &&
				bioAttrUsingPickCode.size() > 0){
//			iterate through bio attributes and populate
			for(int i = 0; i < bioAttrUsingPickCode.size(); i++){
				if(hasFocus)
					formFocus.set(bioAttrUsingPickCode.get(i).toString(),defaultPickCode);
				else
					form.getFormWidgetByName(bioAttrUsingPickCode.get(i).toString()).setDisplayValue(defaultPickCode);
			}
		}
		//If There is a default BAYSTART and there are bio attributes to populate with it then do so.
		String defaultStartBay = WSDefaultsUtil.getDefaultStartBay(state);
		if(		defaultStartBay != null && 
				defaultStartBay.length() > 0 && 
				bioAttrUsingBayStart != null &&
				bioAttrUsingBayStart.size() > 0){
//			iterate through bio attributes and populate
			for(int i = 0; i < bioAttrUsingBayStart.size(); i++){
				if(hasFocus)
					formFocus.set(bioAttrUsingBayStart.get(i).toString(),defaultStartBay);
				else
					form.getFormWidgetByName(bioAttrUsingBayStart.get(i).toString()).setDisplayValue(defaultStartBay);
			}
		}
		//If There is a default BAYEND and there are bio attributes to populate with it then do so.
		String defaultEndBay = WSDefaultsUtil.getDefaultEndBay(state);
		if(		defaultEndBay != null && 	
				defaultEndBay.length() > 0 && 
				bioAttrUsingBayEnd != null &&
				bioAttrUsingBayEnd.size() > 0){
//			iterate through bio attributes and populate
			for(int i = 0; i < bioAttrUsingBayEnd.size(); i++){
				if(hasFocus)
					formFocus.set(bioAttrUsingBayEnd.get(i).toString(),defaultEndBay);
				else
					form.getFormWidgetByName(bioAttrUsingBayEnd.get(i).toString()).setDisplayValue(defaultEndBay);
			}
		}
		//If There is a default SLOTSTART and there are bio attributes to populate with it then do so.
		String defaultStartSlot = WSDefaultsUtil.getDefaultStartSlot(state);
		if(		defaultStartSlot != null && 
				defaultStartSlot.length() > 0 &&
				bioAttrUsingSlotStart != null &&
				bioAttrUsingSlotStart.size() > 0){
//			iterate through bio attributes and populate
			for(int i = 0; i < bioAttrUsingSlotStart.size(); i++){
				if(hasFocus)
					formFocus.set(bioAttrUsingSlotStart.get(i).toString(),defaultStartSlot);
				else
					form.getFormWidgetByName(bioAttrUsingSlotStart.get(i).toString()).setDisplayValue(defaultStartSlot);
			}
		}
		//If There is a default SLOTEND and there are bio attributes to populate with it then do so.
		String defaultEndSlot = WSDefaultsUtil.getDefaultEndSlot(state);
		if(		defaultEndSlot != null && 	
				defaultEndSlot.length() > 0 &&
				bioAttrUsingSlotEnd != null &&
				bioAttrUsingSlotEnd.size() > 0){
//			iterate through bio attributes and populate
			for(int i = 0; i < bioAttrUsingSlotEnd.size(); i++){
				if(hasFocus)
					formFocus.set(bioAttrUsingSlotEnd.get(i).toString(),defaultEndSlot);
				else
					form.getFormWidgetByName(bioAttrUsingSlotEnd.get(i).toString()).setDisplayValue(defaultEndSlot);
			}
		}
		
		//If owner locking is on then Carrier, Bill To, and Customer will have default values defined in the pre-filter tab
		//the following code populates widgets with these values
		if(isOwnerLocked){
			ArrayList bioAttrUsingCarrier = (ArrayList)getParameter("bioAttrUsingCarrier");
			ArrayList bioAttrUsingBillTo = (ArrayList)getParameter("bioAttrUsingBillTo");
			ArrayList bioAttrUsingCustomer = (ArrayList)getParameter("bioAttrUsingCustomer");
			ArrayList bioAttrUsingOwnerIfLocked = (ArrayList)getParameter("bioAttrUsingOwnerIfLocked");
			ArrayList bioAttrUsingVendor = (ArrayList)getParameter("bioAttrUsingVendor");
			
			//If There is a default Carrier and there are bio attributes to populate with it then do so.
			String defaultCarrier = WSDefaultsUtil.getPreFilterValueByType("CARRIER", state);
			if(		defaultCarrier != null && 
					defaultCarrier.length() > 0 && 
					bioAttrUsingCarrier != null &&
					bioAttrUsingCarrier.size() > 0){
//				iterate through bio attributes and populate
				for(int i = 0; i < bioAttrUsingCarrier.size(); i++){
					if(hasFocus)
						formFocus.set(bioAttrUsingCarrier.get(i).toString(),defaultCarrier);
					else
						form.getFormWidgetByName(bioAttrUsingCarrier.get(i).toString()).setDisplayValue(defaultCarrier);
				}
			}
			
			//If There is a default BillTo and there are bio attributes to populate with it then do so.
			String defaultBillTo = WSDefaultsUtil.getPreFilterValueByType("BILLTO", state);
			if(		defaultBillTo != null && 
					defaultBillTo.length() > 0 && 
					bioAttrUsingBillTo != null &&
					bioAttrUsingBillTo.size() > 0){
//				iterate through bio attributes and populate
				for(int i = 0; i < bioAttrUsingBillTo.size(); i++){
					if(hasFocus)
						formFocus.set(bioAttrUsingBillTo.get(i).toString(),defaultBillTo);
					else
						form.getFormWidgetByName(bioAttrUsingBillTo.get(i).toString()).setDisplayValue(defaultBillTo);
				}
			}
			
			//If There is a default BillTo and there are bio attributes to populate with it then do so.
			String defaultCustomer = WSDefaultsUtil.getPreFilterValueByType("CUSTOM", state);
			if(		defaultCustomer != null && 
					defaultCustomer.length() > 0 && 
					bioAttrUsingCustomer != null &&
					bioAttrUsingCustomer.size() > 0){
//				iterate through bio attributes and populate
				for(int i = 0; i < bioAttrUsingCustomer.size(); i++){
					if(hasFocus)
						formFocus.set(bioAttrUsingCustomer.get(i).toString(),defaultCustomer);
					else
						form.getFormWidgetByName(bioAttrUsingCustomer.get(i).toString()).setDisplayValue(defaultCustomer);
				}
			}
			
			//If There is a default Vendor and there are bio attributes to populate with it then do so.
			String defaultVendor = WSDefaultsUtil.getPreFilterValueByType("VENDOR", state);
			if(		defaultVendor != null && 
					defaultVendor.length() > 0 && 
					bioAttrUsingVendor != null &&
					bioAttrUsingVendor.size() > 0){
//				iterate through bio attributes and populate
				for(int i = 0; i < bioAttrUsingVendor.size(); i++){
					if(hasFocus)
						formFocus.set(bioAttrUsingVendor.get(i).toString(),defaultVendor);
					else
						form.getFormWidgetByName(bioAttrUsingVendor.get(i).toString()).setDisplayValue(defaultVendor);
				}
			}
			
			//If There is a default Owner and there are bio attributes to populate with it then do so.
			String defaultOwner = WSDefaultsUtil.getPreFilterValueByType("STORER", state);
			if(		defaultOwner != null && 
					defaultOwner.length() > 0 && 
					bioAttrUsingOwnerIfLocked != null &&
					bioAttrUsingOwnerIfLocked.size() > 0){
//				iterate through bio attributes and populate
				for(int i = 0; i < bioAttrUsingOwnerIfLocked.size(); i++){
					if(hasFocus)
						formFocus.set(bioAttrUsingOwnerIfLocked.get(i).toString(),defaultOwner);
					else
						form.getFormWidgetByName(bioAttrUsingOwnerIfLocked.get(i).toString()).setDisplayValue(defaultOwner);
				}
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_WSNORMFRMPREREN","Exiting WSNormalFormPrerenderAction",100L);
		return RET_CONTINUE;
	}
}