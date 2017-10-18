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
import java.util.ArrayList;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeWidget;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.UserUtil;


public class ValidateWSDefault extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateWSDefault.class);
	private final int PRE_FILTER_THRESHOLD = 30;
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Executing ValidateWSDefault",100L);		
		StateInterface state = context.getState();	
		ArrayList tabList = new ArrayList();
		tabList.add("tab 0");
		tabList.add("tab 1");	
		RuntimeFormInterface headerForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_ws_defaults_header_detail",tabList,state);			
		RuntimeFormInterface detailForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_ws_defaults_detail_detail",tabList,state);		
		RuntimeListFormInterface prefilterListForm = (RuntimeListFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_ws_defaults_detail_list",tabList,state);
		if(headerForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Found Header Form:"+headerForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Found Header Form:Null",100L);			
		if(detailForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Found Detail Form:"+detailForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Found Detail Form:Null",100L);			
		if(prefilterListForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Found PreFilter List Form:"+prefilterListForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Found PreFilter List Form:Null",100L);			
		//validate header form fields		
		if(headerForm != null){
			Object storerKeyObj = headerForm.getFormWidgetByName("STORERKEY");
			Object receiveLocObj = headerForm.getFormWidgetByName("RECEIVELOC");
			Object packObj = headerForm.getFormWidgetByName("PACK");			
			Object startBayObj = headerForm.getFormWidgetByName("STARTBAY");
			Object startSlotObj = headerForm.getFormWidgetByName("STARTSLOT");
			Object endBayObj = headerForm.getFormWidgetByName("ENDBAY");
			Object endSlotObj = headerForm.getFormWidgetByName("ENDSLOT");
			
			String storerKey = storerKeyObj == null || ((RuntimeWidget)storerKeyObj).getDisplayValue() == null?"":((RuntimeWidget)storerKeyObj).getDisplayValue();
			String receiveLoc = receiveLocObj == null || ((RuntimeWidget)receiveLocObj).getDisplayValue() == null?"":((RuntimeWidget)receiveLocObj).getDisplayValue();
			String pack = packObj == null || ((RuntimeWidget)packObj).getDisplayValue() == null?"":((RuntimeWidget)packObj).getDisplayValue();			
			String startBay = startBayObj == null || ((RuntimeWidget)startBayObj).getDisplayValue() == null?"":((RuntimeWidget)startBayObj).getDisplayValue();
			String startSlot = startSlotObj == null || ((RuntimeWidget)startSlotObj).getDisplayValue() == null?"":((RuntimeWidget)startSlotObj).getDisplayValue();			
			String endBay = endBayObj == null || ((RuntimeWidget)endBayObj).getDisplayValue() == null?"":((RuntimeWidget)endBayObj).getDisplayValue();
			String endSlot = endSlotObj == null || ((RuntimeWidget)endSlotObj).getDisplayValue() == null?"":((RuntimeWidget)endSlotObj).getDisplayValue();
			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","storerKeyObj:"+storerKeyObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","receiveLocObj:"+receiveLocObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","packObj:"+packObj,100L);			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","startBayObj:"+startBayObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","startSlotObj:"+startSlotObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","endBayObj:"+endBayObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","endSlotObj:"+endSlotObj,100L);			
			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","storerKey:"+storerKey,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","receiveLoc:"+receiveLoc,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","pack:"+pack,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","startBay:"+startBay,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","startSlot:"+startSlot,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","endBay:"+endBay,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","endSlot:"+endSlot,100L);
						
			
			//Validate Storer Key, if present must be present in STORER table and of type 1
			if(storerKey.length() > 0){
				try {
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Validating STORERKEY...",100L);					
					Query loadBiosQry = new Query("wm_storer", "wm_storer.STORERKEY = '"+storerKey.toUpperCase()+"' AND wm_storer.TYPE = '1'", "");
					UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
					BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
					if(bioCollection == null || bioCollection.size() == 0){
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","STORERKEY "+storerKey+" is not valid...",100L);						
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Leaving ValidateWSDefault",100L);						
						String args[] = new String[1]; 						
						args[0] = storerKey;
						String errorMsg = getTextMessage("WMEXP_OWNER_VALID",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (EpiDataException e) {					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Leaving ValidateWSDefault",100L);
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 
			}
			
			//Validate Receive Loc, if present must be present in LOC table
			if(receiveLoc.length() > 0){
				try {
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Validating RECEIVELOC...",100L);					
					Query loadBiosQry = new Query("wm_location", "wm_location.LOC = '"+receiveLoc.toUpperCase()+"'", "");
					UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
					BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
					if(bioCollection == null || bioCollection.size() == 0){
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","RECEIVELOC "+receiveLoc+" is not valid...",100L);						
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Leaving ValidateWSDefault",100L);
						String args[] = new String[1]; 						
						args[0] = receiveLoc;
						String errorMsg = getTextMessage("WMEXP_INVALID_LOC",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (EpiDataException e) {					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Leaving ValidateWSDefault",100L);
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 
			}
			
			//Validate Pack, if present must be present in PACK table
			if(pack.length() > 0){
				try {					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Validating PACK...",100L);
					Query loadBiosQry = new Query("wm_pack", "wm_pack.PACKKEY = '"+pack.toUpperCase()+"'", "");
					UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
					BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
					if(bioCollection == null || bioCollection.size() == 0){						
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","PACK "+pack+" is not valid...",100L);
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Leaving ValidateWSDefault",100L);
						String args[] = new String[1]; 						
						args[0] = pack;
						String errorMsg = getTextMessage("WMEXP_INVAID_PACK",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (EpiDataException e) {					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Leaving ValidateWSDefault",100L);
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 
			}					
			
			//Validate Start Bay, if present must be > 0
			if(startBay.length() > 0){
				try {
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Validating Start Bay...",100L);					
					int convertedValue = Integer.parseInt(startBay);					
					if(convertedValue < 1){
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Start Bay "+startBay+" is not valid...",100L);						
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Leaving ValidateWSDefault",100L);
						String args[] = new String[0]; 												
						String errorMsg = getTextMessage("WMEXP_INVAID_STARTBAY_A",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (NumberFormatException e) {					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Leaving ValidateWSDefault",100L);
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_INVAID_STARTBAY_B",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 
			}
			
			//Validate End Bay, if present must be > 0
			if(endBay.length() > 0){
				try {
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Validating End Bay...",100L);					
					int convertedValue = Integer.parseInt(endBay);					
					if(convertedValue < 1){
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","End Bay "+endBay+" is not valid...",100L);						
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Leaving ValidateWSDefault",100L);
						String args[] = new String[0]; 												
						String errorMsg = getTextMessage("WMEXP_INVAID_ENDBAY_A",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (NumberFormatException e) {					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Leaving ValidateWSDefault",100L);
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_INVAID_ENDBAY_B",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 
			}
			
			//Validate Start Slot, if present must be > 0
			if(startSlot.length() > 0){
				try {
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Validating Start Slot...",100L);					
					int convertedValue = Integer.parseInt(startSlot);					
					if(convertedValue < 1){
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Start Slot "+startSlot+" is not valid...",100L);						
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Leaving ValidateWSDefault",100L);
						String args[] = new String[0]; 												
						String errorMsg = getTextMessage("WMEXP_INVAID_STARTSLOT_A",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (NumberFormatException e) {					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Leaving ValidateWSDefault",100L);
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_INVAID_STARTSLOT_B",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 
			}
			
			//Validate End Slot, if present must be > 0
			if(endSlot.length() > 0){
				try {
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Validating End Slot...",100L);					
					int convertedValue = Integer.parseInt(endSlot);					
					if(convertedValue < 1){
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","End Slot "+endSlot+" is not valid...",100L);						
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Leaving ValidateWSDefault",100L);
						String args[] = new String[0]; 												
						String errorMsg = getTextMessage("WMEXP_INVAID_ENDSLOT_A",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (NumberFormatException e) {					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Leaving ValidateWSDefault",100L);
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_INVAID_ENDSLOT_B",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 
			}
		}
		
		//validate detail form fields		
		if(detailForm != null){
			String dbName = (String)state.getRequest().getSession().getAttribute(BuiildDefaultCache.DB_CONNECTION);
			Object typeObj = detailForm.getFormWidgetByName("TYPE");
			Object filterValueObj = detailForm.getFormWidgetByName("FILTERVALUE");			
			
			String type = typeObj == null || ((RuntimeWidget)typeObj).getDisplayValue() == null?"":((RuntimeWidget)typeObj).getDisplayValue().toString();
			String filterValue = filterValueObj == null || ((RuntimeWidget)filterValueObj).getDisplayValue() == null?"":((RuntimeWidget)filterValueObj).getDisplayValue();			
			String uid = UserUtil.getUserId(state);
			int maxNumRecords = 0;					
			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","typeObj:"+typeObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","filterValueObj:"+filterValueObj,100L);
			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","type:"+type,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","filterValue:"+filterValue,100L);			
			
			if(type.length() == 0 || filterValue.length() == 0){
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Leaving ValidateWSDefault",100L);
				return RET_CONTINUE;
			}
						
			//Number of records with this type must not exceed the limit set in the default table
			try {
				Query loadBiosQry = new Query("wsdefaults", "wsdefaults.USERID = 'XXXXXXXXXX'", "");
				UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
				BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
				if(bioCollection != null && bioCollection.size() > 0){
					Object maxRecordsObj = bioCollection.elementAt(0).get("NUMBEROFRECORDSTOKEEP");
					if(maxRecordsObj != null){
						maxNumRecords = Integer.parseInt(maxRecordsObj.toString());
						if(maxNumRecords > PRE_FILTER_THRESHOLD)
							maxNumRecords = PRE_FILTER_THRESHOLD;
					}					
				}
				else{	
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","No global record found...",100L);								
				}
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Validating If Max Number Of Records Exceded...",100L);				
				if(detailForm.getFocus().isTempBio()){
					if(dbName.equalsIgnoreCase("enterprise"))
						loadBiosQry = new Query("wsdefaultsdetail", "wsdefaultsdetail.USERID = '"+uid+"' AND wsdefaultsdetail.TYPE = '"+type.toUpperCase()+"' AND wsdefaultsdetail.ISENTERPRISE = '1'", "");
					else
						loadBiosQry = new Query("wsdefaultsdetail", "wsdefaultsdetail.USERID = '"+uid+"' AND wsdefaultsdetail.TYPE = '"+type.toUpperCase()+"' AND wsdefaultsdetail.ISENTERPRISE = '0'", "");					
				}
				else{
					String wsDetailKey = detailForm.getFocus().getValue("WSDEFAULTSDETAILKEY").toString();
					if(dbName.equalsIgnoreCase("enterprise"))
						loadBiosQry = new Query("wsdefaultsdetail", "wsdefaultsdetail.USERID = '"+uid+"' AND wsdefaultsdetail.TYPE = '"+type.toUpperCase()+"' AND wsdefaultsdetail.WSDEFAULTSDETAILKEY != '"+wsDetailKey.toUpperCase()+"' AND wsdefaultsdetail.ISENTERPRISE = '1'", "");
					else
						loadBiosQry = new Query("wsdefaultsdetail", "wsdefaultsdetail.USERID = '"+uid+"' AND wsdefaultsdetail.TYPE = '"+type.toUpperCase()+"' AND wsdefaultsdetail.WSDEFAULTSDETAILKEY != '"+wsDetailKey.toUpperCase()+"' AND wsdefaultsdetail.ISENTERPRISE = '0'", "");
				}
				bioCollection = uow.getBioCollectionBean(loadBiosQry);
				if(bioCollection != null && bioCollection.size() >= maxNumRecords){					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Max Number Of Records Exceded...",100L);
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Leaving ValidateWSDefault",100L);
					String args[] = new String[0]; 											
					String errorMsg = getTextMessage("WMEXP_MAX_DEAFAULT_NUM_REACHED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			} catch (EpiDataException e) {					
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Leaving ValidateWSDefault",100L);
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			} 
			
			//Type - value combination must be unique for this user.
			try {
				Query loadBiosQry = null;
				if(detailForm.getFocus().isTempBio()){
					if(dbName.equalsIgnoreCase("enterprise"))
						loadBiosQry = new Query("wsdefaultsdetail", "wsdefaultsdetail.USERID = '"+uid+"' AND wsdefaultsdetail.TYPE = '"+type.toUpperCase()+"' AND wsdefaultsdetail.FILTERVALUE = '"+filterValue.toUpperCase()+"' AND wsdefaultsdetail.ISENTERPRISE = '1'", "");
					else
						loadBiosQry = new Query("wsdefaultsdetail", "wsdefaultsdetail.USERID = '"+uid+"' AND wsdefaultsdetail.TYPE = '"+type.toUpperCase()+"' AND wsdefaultsdetail.FILTERVALUE = '"+filterValue.toUpperCase()+"' AND wsdefaultsdetail.ISENTERPRISE = '0'", "");
				}
				else{
					String wsDetailKey = detailForm.getFocus().getValue("WSDEFAULTSDETAILKEY").toString();
					if(dbName.equalsIgnoreCase("enterprise"))
						loadBiosQry = new Query("wsdefaultsdetail", "wsdefaultsdetail.USERID = '"+uid+"' AND wsdefaultsdetail.TYPE = '"+type.toUpperCase()+"' AND wsdefaultsdetail.WSDEFAULTSDETAILKEY != '"+wsDetailKey.toUpperCase()+"' AND wsdefaultsdetail.FILTERVALUE = '"+filterValue.toUpperCase()+"' AND wsdefaultsdetail.ISENTERPRISE = '1'", "");
					else
						loadBiosQry = new Query("wsdefaultsdetail", "wsdefaultsdetail.USERID = '"+uid+"' AND wsdefaultsdetail.TYPE = '"+type.toUpperCase()+"' AND wsdefaultsdetail.WSDEFAULTSDETAILKEY != '"+wsDetailKey.toUpperCase()+"' AND wsdefaultsdetail.FILTERVALUE = '"+filterValue.toUpperCase()+"' AND wsdefaultsdetail.ISENTERPRISE = '0'", "");
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Query:"+loadBiosQry.getQueryExpression(),100L);					
				}
				UnitOfWorkBean uow = context.getState().getTempUnitOfWork();			
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Query:"+loadBiosQry.getQueryExpression(),100L);				
				BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
				if(bioCollection != null && bioCollection.size() > 0){
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Type - Value combination is not unique...",100L);					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Leaving ValidateWSDefault",100L);
					String args[] = new String[0]; 											
					String errorMsg = getTextMessage("WMEXP_TYPE_VALUE_COMBINATION_UNIQUE",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			} catch (EpiDataException e) {					
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Leaving ValidateWSDefault",100L);
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
		}
//		validate prefilterListForm form fields		
		if(prefilterListForm != null && prefilterListForm.getFocus().isBioCollection()){		
			BioCollectionBean preFilterRecords = (BioCollectionBean)prefilterListForm.getFocus();
			try {
				if(preFilterRecords != null && preFilterRecords.size() > 0){
					ArrayList selectedTypes = new ArrayList();
					for(int i = 0; i < preFilterRecords.size(); i++){
						Bio record = preFilterRecords.elementAt(i);
						if(record.getString("ISSELECTED") != null && record.getString("ISSELECTED").equals("1")){
							if(selectedTypes.contains(record.getString("TYPE"))){								
								_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","found duplicate type...",100L);
								_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Leaving ValidateWSDefault",100L);
								String args[] = {}; 
								String errorMsg = getTextMessage("WMEXP_WSDEFAULT_DUP_TYPE_SELECTED",args,state.getLocale());
								throw new UserException(errorMsg,new Object[0]);
							}
							else{
								selectedTypes.add(record.getString("TYPE"));
							}
						}
					}
				}
			} catch (EpiDataException e) {
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Leaving ValidateWSDefault",100L);
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			} 
		}
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Leaving ValidateWSDefault",100L);
		return RET_CONTINUE;
		
	}	
}