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
package com.ssaglobal.scm.wms.wm_thru_order;
import java.util.ArrayList;
import java.util.HashMap;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeWidget;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;


public class ValidateThruOrder extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateThruOrder.class);
	@Override
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Executing ValidateThruOrder",100L);			
		StateInterface state = context.getState();			
		RuntimeFormInterface headerForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_thru_order_header_detail_view",state);
		ArrayList tabList = new ArrayList();
		tabList.add("wm_thru_order_detail_toggle_view_detail");
		RuntimeFormInterface detailForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_thru_order_detail_detail_view",tabList,state);
		if(headerForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Found Header Form:"+headerForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Found Header Form:Null",100L);			
		if(detailForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Found Detail Form:"+detailForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Found Detail Form:Null",100L);			
		Object orderKeyObj = null;
		Object poObj = null;
		if(headerForm != null){
			Object storerObj = headerForm.getFormWidgetByName("STORERKEY");
			Object consigneeObj = headerForm.getFormWidgetByName("CONSIGNEEKEY");
			Object doorObj = headerForm.getFormWidgetByName("DOOR");
			poObj = headerForm.getFormWidgetByName("POKEY");
			orderKeyObj = headerForm.getFormWidgetByName("ORDERKEY");
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","consigneeObj:"+consigneeObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","StorerObj:"+storerObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","doorObj:"+doorObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","poObj:"+poObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","orderKeyObj:"+orderKeyObj,100L);
			//Storer must be valid if present
			if(storerObj != null && ((RuntimeWidget)storerObj).getDisplayValue() != null && ((RuntimeWidget)storerObj).getDisplayValue().length() > 0){
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Checking If Storer:"+((RuntimeWidget)storerObj).getDisplayValue()+" Is Valid",100L);				
				Query loadBiosQry = new Query("wm_storer", "wm_storer.STORERKEY = '"+((RuntimeWidget)storerObj).getDisplayValue().toUpperCase()+"' AND wm_storer.TYPE = '1'", "");
				UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
				BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
				try {
					if(bioCollection.size() == 0){
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Invalid Storer",100L);						
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Exiting ValidateThruOrder",100L);						
						String args[] = new String[1]; 						
						args[0] = ((RuntimeWidget)storerObj).getDisplayValue();
						String errorMsg = getTextMessage("WMEXP_INVALID_STORER",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (EpiDataException e) {
					_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Exiting ValidateThruOrder",100L);
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			}
			
//			Consignee must be valid if present
			if(consigneeObj != null && ((RuntimeWidget)consigneeObj).getDisplayValue() != null && ((RuntimeWidget)consigneeObj).getDisplayValue().length() > 0){
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Checking If Consignee:"+((RuntimeWidget)consigneeObj).getDisplayValue()+" Is Valid",100L);				
				Query loadBiosQry = new Query("wm_storer", "wm_storer.STORERKEY = '"+((RuntimeWidget)consigneeObj).getDisplayValue().toUpperCase()+"' AND wm_storer.TYPE = '2'", "");
				UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
				BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
				try {
					if(bioCollection.size() == 0){
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Invalid Storer",100L);						
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Exiting ValidateThruOrder",100L);
						String args[] = new String[1]; 						
						args[0] = ((RuntimeWidget)consigneeObj).getDisplayValue();
						String errorMsg = getTextMessage("WMEXP_INVALID_CUSTOMER",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (EpiDataException e) {
					_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Exiting ValidateThruOrder",100L);
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			}
			
			//Door must be valid location of type STAGED if present
			if(doorObj != null && ((RuntimeWidget)doorObj).getDisplayValue() != null && ((RuntimeWidget)doorObj).getDisplayValue().length() > 0){							
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Checking If Door:"+((RuntimeWidget)doorObj).getDisplayValue()+" Is Valid and of Type STAGED",100L);
				Query loadBiosQry = new Query("wm_loc_ftl", "wm_loc_ftl.LOC = '"+((RuntimeWidget)doorObj).getDisplayValue().toUpperCase()+"'", "");
				UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
				BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
				try {
					if(bioCollection.size() == 0){						
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Invalid Loc",100L);
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Exiting ValidateThruOrder",100L);
						String args[] = new String[1]; 						
						args[0] = ((RuntimeWidget)doorObj).getDisplayValue();
						String errorMsg = getTextMessage("WMEXP_INVALID_LOC",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (EpiDataException e) {
					_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Exiting ValidateThruOrder",100L);
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
				loadBiosQry = new Query("wm_loc_ftl", "(wm_loc_ftl.LOCATIONTYPE = 'STAGED' OR wm_loc_ftl.LOCATIONTYPE = 'DOOR') AND wm_loc_ftl.LOC = '"
						+ ((RuntimeWidget) doorObj).getDisplayValue().toUpperCase() + "'", "");									
				bioCollection = uow.getBioCollectionBean(loadBiosQry);
				try {
					if(bioCollection.size() == 0){
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Loc Valid But Not Type STAGED",100L);						
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Exiting ValidateThruOrder",100L);
						String args[] = new String[1]; 						
						args[0] = ((RuntimeWidget)doorObj).getDisplayValue();
						String errorMsg = getTextMessage("WMEXP_LOC_MUST_BE_STAGED",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (EpiDataException e) {
					_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Exiting ValidateThruOrder",100L);
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			}
			
			//PO must be valid or NOPO if present
			if(poObj != null && ((RuntimeWidget)poObj).getDisplayValue() != null && ((RuntimeWidget)poObj).getDisplayValue().length() > 0 && !(((RuntimeWidget)poObj).getDisplayValue().equalsIgnoreCase("nopo"))){
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Checking If PO:"+((RuntimeWidget)poObj).getDisplayValue()+" Is Valid",100L);				
				Query loadBiosQry = new Query("wm_po_to", "wm_po_to.POKEY = '"+((RuntimeWidget)poObj).getDisplayValue().toUpperCase()+"'", "");
				UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
				BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
				try {
					if(bioCollection.size() == 0){						
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","PO Invalid",100L);
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Exiting ValidateThruOrder",100L);
						String args[] = new String[1]; 						
						args[0] = ((RuntimeWidget)poObj).getDisplayValue();
						String errorMsg = getTextMessage("WMEXP_INVALID_PO",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (EpiDataException e) {
					_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Exiting ValidateThruOrder",100L);
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			}
			
			//set PO to NOPO if blank
			else{				
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Setting PO to NOPO",100L);
				((RuntimeWidget)poObj).setDisplayValue("NOPO");
				if(headerForm.getFocus().isTempBio()){
					QBEBioBean bio = (QBEBioBean)headerForm.getFocus();
					bio.set("POKEY","NOPO");
				}
				else{
					BioBean bio = (BioBean)headerForm.getFocus();
					bio.set("POKEY","NOPO");
				}
			}
			
			//if detail form is present then OrigQty validation is done after detail validation
			if(detailForm == null){
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","No Detail Update Pending... Validating OrigQty...",100L);				
				if(orderKeyObj != null && ((RuntimeWidget)orderKeyObj).getDisplayValue() != null && ((RuntimeWidget)orderKeyObj).getDisplayValue().length() > 0){
					if(poObj != null && ((RuntimeWidget)poObj).getDisplayValue() != null && ((RuntimeWidget)poObj).getDisplayValue().length() > 0){
						Query loadBiosQry = new Query("wm_xorderdetail_to", "wm_xorderdetail_to.ORDERKEY = '"+((RuntimeWidget)orderKeyObj).getDisplayValue().toUpperCase()+"'", "");
						UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
						BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
						HashMap openQtyTable = new HashMap();						
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Order Detail Records Found...",100L);						
						try {							
							if(bioCollection.size() > 0){
								for(int i = 0; i < bioCollection.size(); i++){
									_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Loading openQtyTable",100L);									
									Bio bio = bioCollection.elementAt(i);
									String sku = bio.get("SKU").toString();
									if(!openQtyTable.containsKey(sku)){
										loadBiosQry = new Query("wm_xorderdetail_to", "wm_xorderdetail_to.ORDERKEY = '"+((RuntimeWidget)orderKeyObj).getDisplayValue().toUpperCase()+"' AND wm_xorderdetail_to.SKU = '"+sku+"'", "");
										BioCollection xOrderDetailCollection = uow.getBioCollectionBean(loadBiosQry);
										String openQtyStr = xOrderDetailCollection.sum("OPENQTY").toString();
										_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Open Qty Sum For Detail Records with SKU:"+sku+" is "+openQtyStr,100L);										
										openQtyTable.put(new String(sku),new String(openQtyStr));
										
										loadBiosQry = new Query("wm_podetail", "wm_podetail.POKEY = '"+((RuntimeWidget)poObj).getDisplayValue().toUpperCase()+"' AND wm_podetail.SKU = '"+sku.toUpperCase()+"'", "");
										BioCollection poDetailCollection = uow.getBioCollectionBean(loadBiosQry);
										String qtyOrderedStr = "0";
										if(poDetailCollection != null && poDetailCollection.size() > 0){
											_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","PO Detail records found with POKEY:"+((RuntimeWidget)poObj).getDisplayValue()+" and SKU:"+sku,100L);											
											qtyOrderedStr = poDetailCollection.sum("QTYORDERED").toString();											
											double sumQtyOrdered = Double.parseDouble(qtyOrderedStr);
											double sumOpenQty = Double.parseDouble(openQtyStr);
											if(sumOpenQty > sumQtyOrdered){
												_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Open Qty is Greater than Qty Ordered",100L);												
												_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Exiting ValidateThruOrder",100L);
												String args[] = new String[0]; 																
												String errorMsg = getTextMessage("WMEXP_OPENQTY_GREATER_THAN_QTYORDERED",args,state.getLocale());
												throw new UserException(errorMsg,new Object[0]);
											}
										}
									}
								}
							}
						} catch (EpiDataException e) {
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Exiting ValidateThruOrder",100L);
							e.printStackTrace();
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);
						}
					}
				}
				else{
					_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","ORDERKEY Cannot Be Found",100L);					
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			}
		}
		if(detailForm != null){
			Object storerObj = headerForm.getFormWidgetByName("STORERKEY");
			Object origQtyObj = headerForm.getFormWidgetByName("ORIGINALQTY");
			Object cubicMeterWidg = detailForm.getFormWidgetByName("CUBICMETER");
			Object hundredWeightWidg = detailForm.getFormWidgetByName("HUNDREDWEIGHT");
			
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Storer:"+storerObj,100L);
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","origQtyObj:"+origQtyObj,100L);
			if(storerObj != null && ((RuntimeWidget)storerObj).getDisplayValue() != null && ((RuntimeWidget)storerObj).getDisplayValue().length() > 0){				
				Object skuObj = detailForm.getFormWidgetByName("SKU");
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Sku:"+skuObj,100L);				
				if(skuObj != null && ((RuntimeWidget)skuObj).getDisplayValue() != null && ((RuntimeWidget)skuObj).getDisplayValue().length() > 0){
					Query loadBiosQry = new Query("wm_sku_to", "wm_sku_to.STORERKEY = '"+((RuntimeWidget)storerObj).getDisplayValue().toUpperCase()+"' AND wm_sku_to.SKU = '"+((RuntimeWidget)skuObj).getDisplayValue().toUpperCase()+"'", "");
					UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
					BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);	
					try {
						if(bioCollection.size() == 0){							
							String args[] = new String[2]; 
							args[0] = ((RuntimeWidget)skuObj).getDisplayValue();
							args[1] = ((RuntimeWidget)storerObj).getDisplayValue();
							String errorMsg = getTextMessage("WMEXP_ITEM_DOES_NOT_BELONG_TO_STORER",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);
						}
					} catch (EpiDataException e) {
						e.printStackTrace();
						String args[] = new String[0]; 
						String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				}
			}
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Validating OrigQty with detail form present so taking values from detail form for validation...",100L);			
			if(orderKeyObj != null && ((RuntimeWidget)orderKeyObj).getDisplayValue() != null && ((RuntimeWidget)orderKeyObj).getDisplayValue().length() > 0){
				if(poObj != null && ((RuntimeWidget)poObj).getDisplayValue() != null && ((RuntimeWidget)poObj).getDisplayValue().length() > 0){
					String orderLineNumber = detailForm.getFormWidgetByName("ORDERLINENUMBER").getDisplayValue();
					Query loadBiosQry = new Query("wm_xorderdetail_to", "wm_xorderdetail_to.ORDERKEY = '"+((RuntimeWidget)orderKeyObj).getDisplayValue().toUpperCase()+"' and wm_xorderdetail_to.ORDERLINENUMBER != '"+orderLineNumber.toUpperCase()+"'", "");
					UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
					BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
					Object skuObj = detailForm.getFormWidgetByName("SKU");
					String skuDetail = ((RuntimeWidget)skuObj).getDisplayValue();
					double sumOpenQty = 0;
					double sumQtyOrdered = 0;
					HashMap openQtyTable = new HashMap();		
					try {
						if(bioCollection.size() > 0){
							for(int i = 0; i < bioCollection.size(); i++){
								_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Loading openQtyTable",100L);								
								Bio bio = bioCollection.elementAt(i);
								String sku = bio.get("SKU").toString();
								if(!openQtyTable.containsKey(sku)){
									loadBiosQry = new Query("wm_xorderdetail_to", "wm_xorderdetail_to.ORDERKEY = '"+((RuntimeWidget)orderKeyObj).getDisplayValue().toUpperCase()+"' AND wm_xorderdetail_to.SKU = '"+sku+"' AND wm_xorderdetail_to.ORDERLINENUMBER != '"+orderLineNumber.toUpperCase()+"'", "");
									BioCollection xOrderDetailCollection = uow.getBioCollectionBean(loadBiosQry);
									String openQtyStr = xOrderDetailCollection.sum("OPENQTY").toString();
									
									//If the sku is the same as the current detail then add openQty of current detail.
									if(sku.equals(skuDetail)){
										String sumStr = null;
										if(state.getRequest().getSession().getAttribute("THRUORDERORIGQTY") != null){
											sumStr = (String)state.getRequest().getSession().getAttribute("THRUORDERORIGQTY");
										}
										else{
											sumStr = detailForm.getFormWidgetByName("OPENQTY").getDisplayValue();
										}
										Double openQtyDouble = new Double(openQtyStr);
										Double sumDouble = new Double(sumStr);
										openQtyStr = (openQtyDouble.doubleValue() + sumDouble.doubleValue()) + "";
									}									
									_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Open Qty Sum For Detail Records with SKU:"+sku+" is "+openQtyStr,100L);									
									openQtyTable.put(new String(sku),new String(openQtyStr));
									
									loadBiosQry = new Query("wm_podetail", "wm_podetail.POKEY = '"+((RuntimeWidget)poObj).getDisplayValue().toUpperCase()+"' AND wm_podetail.SKU = '"+sku.toUpperCase()+"'", "");
									BioCollection poDetailCollection = uow.getBioCollectionBean(loadBiosQry);
									String qtyOrderedStr = "0";
									if(poDetailCollection != null && poDetailCollection.size() > 0){
										_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","PO Detail records found with POKEY:"+((RuntimeWidget)poObj).getDisplayValue()+" and SKU:"+sku,100L);										
										qtyOrderedStr = poDetailCollection.sum("QTYORDERED").toString();											
										sumQtyOrdered = Double.parseDouble(qtyOrderedStr);
										sumOpenQty = Double.parseDouble(openQtyStr);
										if(sumOpenQty > sumQtyOrdered){
											_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Open Qty is Greater than Qty Ordered",100L);											
											_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Exiting ValidateThruOrder",100L);
											String args[] = new String[0]; 																
											String errorMsg = getTextMessage("WMEXP_OPENQTY_GREATER_THAN_QTYORDERED",args,state.getLocale());
											throw new UserException(errorMsg,new Object[0]);
										}
									}
								}
							}										
						}	
						//If No other detail record is present with the SKU in the current detail form then validate the current detail form
						if(!openQtyTable.containsKey(skuDetail)){
							String sumStr = null;
							if(state.getRequest().getSession().getAttribute("THRUORDERORIGQTY") != null){
								sumStr = (String)state.getRequest().getSession().getAttribute("THRUORDERORIGQTY");
							}
							else{
								sumStr = detailForm.getFormWidgetByName("OPENQTY").getDisplayValue();
							}				
							_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Open Qty Sum For Detail Records with SKU:"+skuDetail+" is "+sumStr,100L);							
							openQtyTable.put(new String(skuDetail),new String(sumStr));
							loadBiosQry = new Query("wm_podetail", "wm_podetail.POKEY = '"+((RuntimeWidget)poObj).getDisplayValue().toUpperCase()+"' AND wm_podetail.SKU = '"+skuDetail.toUpperCase()+"'", "");
							BioCollection poDetailCollection = uow.getBioCollectionBean(loadBiosQry);
							String qtyOrderedStr = "0";
							if(poDetailCollection != null && poDetailCollection.size() > 0){
								_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","PO Detail records found with POKEY:"+((RuntimeWidget)poObj).getDisplayValue()+" and SKU:"+skuDetail,100L);								
								qtyOrderedStr = poDetailCollection.sum("QTYORDERED").toString();											
								sumQtyOrdered = Double.parseDouble(qtyOrderedStr);
								sumOpenQty = Double.parseDouble(sumStr);
								if(sumOpenQty > sumQtyOrdered){
									_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Open Qty is Greater than Qty Ordered",100L);									
									_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Exiting ValidateThruOrder",100L);
									String args[] = new String[0]; 																
									String errorMsg = getTextMessage("WMEXP_OPENQTY_GREATER_THAN_QTYORDERED",args,state.getLocale());
									throw new UserException(errorMsg,new Object[0]);
								}
							}
						}
					} catch (EpiDataException e) {
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Exiting ValidateThruOrder",100L);
						e.printStackTrace();
						String args[] = new String[0]; 
						String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				}
			}
			else{
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","ORDERKEY Cannot Be Found",100L);				
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
			
			if(origQtyObj != null && ((RuntimeWidget)origQtyObj).getDisplayValue() != null && ((RuntimeWidget)origQtyObj).getDisplayValue().length() > 0){
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Checking for non-negative ORIGQTY...",100L);				
				try {
					double origQty = Double.parseDouble(((RuntimeWidget)origQtyObj).getDisplayValue());
					if(origQty < 0){						
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","ORIGQTY is negative...",100L);
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Exiting ValidateThruOrder",100L);
						String args[] = new String[0]; 																
						String errorMsg = getTextMessage("WMEXP_NEGATIVE_ORIGQTY",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				} catch (NumberFormatException e) {					
					_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","ORIGQTY is NAN...",100L);
					_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Exiting ValidateThruOrder",100L);
					String args[] = new String[0]; 																
					String errorMsg = getTextMessage("WMEXP_ORIGQTY_NAN",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 
			}
			
			if (cubicMeterWidg != null && ((RuntimeWidget) cubicMeterWidg).getDisplayValue() != null && ((RuntimeWidget) cubicMeterWidg).getDisplayValue().length() > 0)
			{
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION", "Checking for non-negative cubicMeter...", 100L);
				try
				{
					double cubicMeter = Double.parseDouble(((RuntimeWidget) cubicMeterWidg).getDisplayValue());
					if (cubicMeter < 0)
					{
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION", "cubicMeter is negative...", 100L);
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION", "Exiting ValidateThruOrder", 100L);
						String args[] = new String[1];
						args[0] = detailForm.getFormWidgetByName("CUBICMETER").getLabel("label", state.getLocale());
						String errorMsg = getTextMessage("WMEXP_NONNEG", args, state.getLocale());
						throw new UserException(errorMsg, new Object[0]);
					}
				} catch (NumberFormatException e)
				{
					_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION", "cubicMeter is NAN...", 100L);
					_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION", "Exiting ValidateThruOrder", 100L);
					String args[] = new String[1];
					String errorMsg = getTextMessage("WMEXP_INVALID", args, state.getLocale());
					throw new UserException(errorMsg, new Object[0]);
				}
			}

			if (hundredWeightWidg != null && ((RuntimeWidget) hundredWeightWidg).getDisplayValue() != null && ((RuntimeWidget) hundredWeightWidg).getDisplayValue().length() > 0)
			{
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION", "Checking for non-negative hundredWeight...", 100L);
				try
				{
					double cubicMeter = Double.parseDouble(((RuntimeWidget) hundredWeightWidg).getDisplayValue());
					if (cubicMeter < 0)
					{
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION", "hundredWeight is negative...", 100L);
						_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION", "Exiting ValidateThruOrder", 100L);
						String args[] = new String[1];
						args[0] = detailForm.getFormWidgetByName("HUNDREDWEIGHT").getLabel("label", state.getLocale());
						String errorMsg = getTextMessage("WMEXP_NONNEG", args, state.getLocale());
						throw new UserException(errorMsg, new Object[0]);
					}
				} catch (NumberFormatException e)
				{
					_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION", "hundredWeight is NAN...", 100L);
					_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION", "Exiting ValidateThruOrder", 100L);
					String args[] = new String[0];
					String errorMsg = getTextMessage("WMEXP_INVALID", args, state.getLocale());
					throw new UserException(errorMsg, new Object[0]);
				}
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Exiting ValidateThruOrder",100L);
		return RET_CONTINUE;
		
	}	
}