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
package com.ssaglobal.scm.wms.wm_transship_asn;
import java.util.ArrayList;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeWidget;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.util.FormUtil;


public class ValidateTransshipASN extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CleanSession.class);
	@Override
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Executing ValidateTransshipASN",100L);		
		StateInterface state = context.getState();	
		
		//Get Header and Detail Forms
		RuntimeFormInterface headerForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_transship_asn_header_detail_view",state);
		if(headerForm == null)
			headerForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_transship_asn_header_new_detail_view",state);
		ArrayList tabList = new ArrayList();		
		tabList.add("wm_transship_asn_detail");			
		RuntimeFormInterface detailForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_transship_asn_detail_detail_view",tabList,state);
		if(headerForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Found Header Form:"+headerForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Found Header Form:Null",100L);			
		if(detailForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Found Detail Form:"+detailForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Found Detail Form:Null",100L);			
		
		//validate header form fields		
		if(headerForm != null){
			Object transAsnKeyObj = headerForm.getFormWidgetByName("TRANSASNKEY");
			Object doorObj = headerForm.getFormWidgetByName("DOOR");			
			Object externASNKeyObj = headerForm.getFormWidgetByName("EXTERNTRANASNKEY");
			Object udf1Obj = headerForm.getFormWidgetByName("UDF1");
			Object udf2Obj = headerForm.getFormWidgetByName("UDF2");
			
			String transAsnKey = transAsnKeyObj == null || ((RuntimeWidget)transAsnKeyObj).getDisplayValue() == null?"":((RuntimeWidget)transAsnKeyObj).getDisplayValue();
			String door = doorObj == null || ((RuntimeWidget)doorObj).getDisplayValue() == null?"":((RuntimeWidget)doorObj).getDisplayValue();			
			String externASNKey = externASNKeyObj == null || ((RuntimeWidget)externASNKeyObj).getDisplayValue() == null?"":((RuntimeWidget)externASNKeyObj).getDisplayValue();
			String udf1 = udf1Obj == null || ((RuntimeWidget)udf1Obj).getDisplayValue() == null?"":((RuntimeWidget)udf1Obj).getDisplayValue();
			String udf2 = doorObj == null || ((RuntimeWidget)udf2Obj).getDisplayValue() == null?"":((RuntimeWidget)udf2Obj).getDisplayValue();
			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","transAsnKeyObj:"+transAsnKeyObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","doorObj:"+doorObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","externASNKeyObj:"+externASNKeyObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","udf1Obj:"+udf1Obj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","udf1Obj:"+udf1Obj,100L);
			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","transAsnKey:"+transAsnKey,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","door"+door,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","externASNKey:"+externASNKey,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","udf1:"+udf1,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","udf2:"+udf2,100L);
						
			//Validate Trans ASN Key, if present must be unique for new records
			if(transAsnKey.length() > 0 && headerForm.getFocus().isTempBio()){
				try {
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Validating TRANSASNKEY...",100L);					
					Query loadBiosQry = new Query("wm_transasn", "wm_transasn.TRANSASNKEY = '"+transAsnKey.toUpperCase()+"'", "");
					UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
					BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
					if(bioCollection != null && bioCollection.size() > 0){						
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","TRANSASNKEY "+transAsnKey+" is not unique...",100L);
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Leaving ValidateTransshipASN",100L);						
						String args[] = new String[1]; 						
						args[0] = transAsnKey;
						String errorMsg = getTextMessage("WMEXP_NON_UNIQUE_TRANSASNKEY",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (EpiDataException e) {					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Leaving ValidateTransshipASN",100L);
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 
			}
			
			//Validate Door, if present must be present in LOC table and Type STAGED
			if(door.length() > 0){
				try {					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Validating DOOR...",100L);
					Query loadBiosQry = new Query("wm_location", "wm_location.LOC = '"+door.toUpperCase()+"'", "");
					UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
					BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
					if(bioCollection == null || bioCollection.size() == 0){						
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","DOOR "+door+" is not valid...",100L);
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Leaving ValidateTransshipASN",100L);
						String args[] = new String[1]; 						
						args[0] = door;
						String errorMsg = getTextMessage("WMEXP_INVALID_LOC",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
					loadBiosQry = new Query("wm_location", "wm_location.LOC = '" + door.toUpperCase() + "' AND (wm_location.LOCATIONTYPE = 'STAGED' OR wm_location.LOCATIONTYPE = 'DOOR')", "");
					bioCollection = uow.getBioCollectionBean(loadBiosQry);
					if(bioCollection == null || bioCollection.size() == 0){						
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","DOOR "+door+" is not valid...",100L);
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Leaving ValidateTransshipASN",100L);
						String args[] = new String[1]; 						
						args[0] = door;
						String errorMsg = getTextMessage("WMEXP_INVALID_DOOR",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (EpiDataException e) {					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Leaving ValidateTransshipASN",100L);
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 
			}
			
			//Combination ExternalASNKey + UDF1 + UDF2 must be unique
			try {
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Validating Combination ExternalASNKey + UDF1 + UDF2 is unique...",100L);				
				String udf1condition = "";
				String udf2condition = "";
				if(udf1.length() == 0)
					udf1condition = " IS NULL ";
				else
					udf1condition = " = '"+udf1+"' ";
				if(udf2.length() == 0)
					udf2condition = " IS NULL ";
				else
					udf2condition = " = '"+udf2+"' ";
				Query loadBiosQry = new Query("wm_transasn", "wm_transasn.EXTERNTRANASNKEY = '"+externASNKey.toUpperCase()+"' AND wm_transasn.UDF1 "+udf1condition.toUpperCase()+" AND wm_transasn.UDF2 "+udf2condition.toUpperCase()+" AND wm_transasn.TRANSASNKEY != '"+transAsnKey.toUpperCase()+"'", "");
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Query:"+loadBiosQry.getQueryExpression(),100L);				
				UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
				BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
				if(bioCollection != null && bioCollection.size() > 0){					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Combination ExternalASNKey + UDF1 + UDF2 is not unique...",100L);
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Leaving ValidateTransshipASN",100L);
					String args[] = new String[0]; 											
					String errorMsg = getTextMessage("WMEXP_NON_UNIQUE_TRANS_ASN_HEADER",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			} catch (EpiDataException e) {					
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Leaving ValidateTransshipASN",100L);
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			} 
			
		}			
		
//		validate detail form fields		
		if(detailForm != null){
			Object statusObj = headerForm.getFormWidgetByName("STATUS");
			Object verifyFlagObj = headerForm.getFormWidgetByName("VERIFYFLG");	
			Object transAsnKeyObj = headerForm.getFormWidgetByName("TRANSASNKEY");
			Object storerObj = detailForm.getFormWidgetByName("STORERKEY");	
			Object skuObj = detailForm.getFormWidgetByName("SKU");	
			Object lineNumberObj = detailForm.getFormWidgetByName("LINENUMBER");
			Object expectedObj = detailForm.getFormWidgetByName("QTY");
			
			String status = statusObj == null || ((RuntimeWidget)statusObj).getDisplayValue() == null?"":((RuntimeWidget)statusObj).getDisplayValue();
			String verifyFlag = verifyFlagObj == null || ((RuntimeWidget)verifyFlagObj).getDisplayValue() == null?"":((RuntimeWidget)verifyFlagObj).getDisplayValue();			
			String storer = storerObj == null || ((RuntimeWidget)storerObj).getDisplayValue() == null?"":((RuntimeWidget)storerObj).getDisplayValue();
			String sku = skuObj == null || ((RuntimeWidget)skuObj).getDisplayValue() == null?"":((RuntimeWidget)skuObj).getDisplayValue();
			String transAsnKey = transAsnKeyObj == null || ((RuntimeWidget)transAsnKeyObj).getDisplayValue() == null?"":((RuntimeWidget)transAsnKeyObj).getDisplayValue();
			String lineNumber = lineNumberObj == null || ((RuntimeWidget)lineNumberObj).getDisplayValue() == null?"":((RuntimeWidget)lineNumberObj).getDisplayValue();
			String expected = expectedObj == null || ((RuntimeWidget)expectedObj).getDisplayValue() == null?"":((RuntimeWidget)expectedObj).getDisplayValue();
			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","statusObj:"+statusObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","verifyFlagObj:"+verifyFlagObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","storerObj:"+storerObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","skuObj:"+skuObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","transAsnKeyObj:"+transAsnKeyObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","lineNumberObj:"+lineNumberObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","expectedObj:"+expectedObj,100L);
			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","status:"+status,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","verifyFlag:"+verifyFlag,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","storer:"+storer,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","sku:"+sku,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","transAsnKey:"+transAsnKey,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","lineNumber:"+lineNumber,100L);
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","expected:"+expected,100L);						
			
//			Validate Storer Key, if present must be present in STORER table
			if(storer.length() > 0){
				try {					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Validating STORERKEY...",100L);
					Query loadBiosQry = new Query("wm_storer", "wm_storer.STORERKEY = '"+storer.toUpperCase()+"' AND wm_storer.TYPE = '1'", "");
					UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
					BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
					if(bioCollection == null || bioCollection.size() == 0){
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","STORERKEY "+storer+" is not valid...",100L);						
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Leaving ValidateTransship",100L);						
						String args[] = new String[1]; 						
						args[0] = storer;
						String errorMsg = getTextMessage("WMEXP_OWNER_VALID",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (EpiDataException e) {					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Leaving ValidateTransship",100L);
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 
			}
			
//			Validate SKU , if present must be present in SKU table
			if(sku.length() > 0){
				try {					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Validating SKU...",100L);
					Query loadBiosQry = new Query("wm_sku", "wm_sku.SKU = '"+sku.toUpperCase()+"'", "");
					UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
					BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
					if(bioCollection == null || bioCollection.size() == 0){
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","SKU "+sku+" is not valid...",100L);						
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Leaving ValidateTransship",100L);
						String args[] = new String[1]; 						
						args[0] = sku;
						String errorMsg = getTextMessage("WMEXP_INVAID_SKU",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (EpiDataException e) {					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Leaving ValidateTransship",100L);
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 
			}
//			Validate SKU , if present must belong to Storer
			if(sku.length() > 0){
				try {					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Validating SKU belongs to storer...",100L);
					Query loadBiosQry = new Query("wm_sku", "wm_sku.SKU = '"+sku.toUpperCase()+"' AND wm_sku.STORERKEY = '"+storer.toUpperCase()+"'", "");
					UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
					BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
					if(bioCollection == null || bioCollection.size() == 0){
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","SKU "+sku+" is not valid...",100L);						
						_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Leaving ValidateTransship",100L);
						String args[] = new String[2]; 						
						args[0] = sku;
						args[1] = storer;
						String errorMsg = getTextMessage("WMEXP_SKU_MUST_BELONG_TO_STORER",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (EpiDataException e) {					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Leaving ValidateTransship",100L);
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 
			}
//			Validate Expected QTY must be present and a valid number			
			_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Validating Expected QTY...",100L);
			if(expected.length() > 0){
				try {
					Double.parseDouble(expected);					
				} catch (NumberFormatException e) {
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Expected Qty NAN...",100L);					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Leaving ValidateTransship",100L);					
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_TRANS_ASN_EXPECTED_VALID_NUM",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 
			}
			else{				
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Expected Qty NAN...",100L);
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Leaving ValidateTransship",100L);			
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_TRANS_ASN_EXPECTED_VALID_NUM",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
			//Storer + Sku combination must be unique.
			try {				
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Validating Combination Storer + Sku is unique...",100L);
				Query loadBiosQry = new Query("wm_transasnd", "wm_transasnd.STORERKEY = '"+storer.toUpperCase()+"' AND wm_transasnd.SKU = '"+sku.toUpperCase()+"' AND wm_transasnd.TRANSASNKEY = '"+transAsnKey.toUpperCase()+"' AND wm_transasnd.LINENUMBER != '"+lineNumber.toUpperCase()+"'", "");
				UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
				BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
				if(bioCollection != null && bioCollection.size() > 0){
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Combination Storer + Sku is not unique...",100L);					
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Leaving ValidateTransshipASN",100L);
					String args[] = new String[0]; 											
					String errorMsg = getTextMessage("WMEXP_NON_UNIQUE_TRANS_ASN_DETAIL",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			} catch (EpiDataException e) {					
				_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Leaving ValidateTransshipASN",100L);
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			} 
		}		
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATETRANSASN","Leaving ValidateTransship",100L);
		return RET_CONTINUE;
		
	}	
}