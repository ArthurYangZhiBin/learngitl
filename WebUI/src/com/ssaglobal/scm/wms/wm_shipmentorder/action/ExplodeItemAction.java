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
package com.ssaglobal.scm.wms.wm_shipmentorder.action;

import java.util.ArrayList;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

public class ExplodeItemAction extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ExplodeItemAction.class);
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		StateInterface state = context.getState();
		//Ensure proper context for focus
		SlotInterface toggleSlot = state.getCurrentRuntimeForm().getParentForm(state).getSubSlot("wm_shipmentorderdetail_toggle_view");
		int selectedFormNo = state.getSelectedFormNumber(toggleSlot);
		RuntimeFormInterface currentForm = state.getRuntimeForm(toggleSlot, selectedFormNo);

		//01/29/2008 FW: Added code to get correct detail form (SDIS#3899_Cos#1056055) -- Start 
		if(currentForm.getName().equals("wm_shipmentorderdetail_normal_list_view")){
			currentForm = state.getRuntimeForm(currentForm.getSubSlot("LIST_SLOT"), null);
		}
		//01/29/2008 FW: Added code to get correct detail form (SDIS#3899_Cos#1056055) -- End 
		
		DataBean focus = currentForm.getFocus();
		//Error message arguments
		String[] parameters = new String[1];
		parameters[0]=null;
		
		if(focus.isBioCollection()){
			//Current form is detail list view, find selected records
			RuntimeListFormInterface list = (RuntimeListFormInterface)currentForm;
			ArrayList selected = list.getSelectedItems();
			if(selected!=null){
				for(int index=0; index<selected.size(); index++){
					//Explode record and record Order Line Number for error argument if necessary
					parameters[0] = explodeRecord(state, (DataBean)selected.get(index), parameters[0]);
				}
			}else{
				//Throw exception message, no records selected
				throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
			}
			
//			Removed for list view implementation - 5/9/07
//			_log.debug("LOG_DEBUG_EXTENSION", "**********Detail form not initialized", SuggestedCategory.NONE);;
//			return RET_CANCEL;
			
		}else{
			//Current form is the detail form view, perform action for single record
			parameters[0] = explodeRecord(state, focus, null);
		}
		if(parameters[0]!=null){
			//Encountered non-fatal errors during execution, alert user
			if(focus.isBioCollection()){
				throw new UserException("WMEXP_SO_EXPLODE_ERROR_LIST", parameters);
			}else{
				throw new UserException("WMEXP_SO_EXPLODE_ERROR", parameters);
			}
		}
		return RET_CONTINUE;
	}
	
	public Object figureValue(String original, double adjQty){
		double ori = Double.parseDouble(original);
		// 2012-07-04
		// Modified by Will Pu
		// 分解出来的商品数量向上取整
		Object newValue = Math.ceil(new Double(ori*adjQty));
		_log.debug("LOG_DEBUG_EXTENSION", "Calcuated value: "+newValue.toString(), SuggestedCategory.NONE);;
		return newValue;
	}
	
	public String explodeRecord(StateInterface state, DataBean focus, String args) throws EpiException{
//		Search billofmaterial table for corresponding components
		_log.debug("LOG_DEBUG_EXTENSION", "**********Detail Focus: "+focus.getDataType()+" "+focus.getBeanType(), SuggestedCategory.NONE);;
		String tableName = "wm_setup_billofmaterial";
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		BioBean bio = (BioBean)focus;
		_log.debug("LOG_DEBUG_EXTENSION", "Current order number and order line number: "+bio.get("ORDERKEY").toString()+" "+bio.get("ORDERLINENUMBER").toString(), SuggestedCategory.NONE);;
		int size = 0;
		Object storerKey = focus.getValue("STORERKEY"), sku = focus.getValue("SKU"), originalqty = focus.getValue("UOMADJORIGINALQTY"), lottable02 = focus.getValue("LOTTABLE02");
		BioCollectionBean bomList = null;
		if(storerKey!=null && sku!=null){
			String queryString = tableName+".STORERKEY='"+storerKey+"' and "+tableName+".SKU='"+sku+"'";
			Query qry = new Query(tableName, queryString, null);
			bomList = uow.getBioCollectionBean(qry);
			size=bomList.size();
		}else{
			_log.debug("LOG_DEBUG_EXTENSION", "Unable to perform query", SuggestedCategory.NONE);;
			
			throw new UserException("WMEXP_SO_STOP_ACTION", new Object[] {});
		}
		if(size>=1){
			int index=0;

			//Find maximum order line number of list view BioCollection

			//01/29/2008 FW: Added code to get correct detail form (SDIS#3899_Cos#1056055) -- Start 
			/* -- old code
			DataBean detailFocus = state.getRuntimeForm(state.getCurrentRuntimeForm().getParentForm(state).getSubSlot("wm_shipmentorderdetail_toggle_view"), "wm_shipmentorderdetail_list_view").getFocus();
			 */
			SlotInterface toggleSlot2 = state.getCurrentRuntimeForm().getParentForm(state).getSubSlot("wm_shipmentorderdetail_toggle_view");
			int selectedFormNo2 = state.getSelectedFormNumber(toggleSlot2);
			RuntimeFormInterface currentForm2 = state.getRuntimeForm(toggleSlot2, selectedFormNo2);
				
			if(currentForm2.getName().equals("wm_shipmentorderdetail_normal_list_view")){
				currentForm2 = state.getRuntimeForm(currentForm2.getSubSlot("LIST_SLOT"), null);
			}
			DataBean detailFocus = currentForm2.getFocus();
			//01/29/2008 FW: Added code to get correct detail form (SDIS#3899_Cos#1056055) -- End
			
			BioCollectionBean bioFocus = (BioCollectionBean)detailFocus;
			Object max = null;
			max = bioFocus.max("ORDERLINENUMBER");	
			String key="";
			int oln = Integer.parseInt(max.toString())+1;
			if(oln+size>99999){
				_log.error("EXPLODE_ITEM_ACTION_ERROR", "Unable to complete action, insufficent room in dataset", SuggestedCategory.NONE);;
				throw new UserException("WMEXP_SO_MAX_DETAIL_RECORDS", new Object[] {});
			}
			
		
			//Iterate through BioCollection creating new detail records replacing relevant attributes
			while(index<size){
				UnitOfWork unitOfWork = bio.getUnitOfWork();
				Bio explodedDetail = unitOfWork.createBio("wm_orderdetail");
				
				String queryString = "sku.STORERKEY='"+storerKey+"' and sku.SKU='"+bomList.get(""+index).get("COMPONENTSKU").toString()+"'";
				Query qry = new Query("sku", queryString, null);
				BioCollectionBean skuList = uow.getBioCollectionBean(qry);
				_log.debug("LOG_DEBUG_EXTENSION", "*****Sku query passed", SuggestedCategory.NONE);;
				
				queryString = "wm_pack.PACKKEY='"+skuList.get("0").get("PACKKEY").toString()+"'";
				qry = new Query("wm_pack", queryString, null);
				BioCollectionBean packList = uow.getBioCollectionBean(qry);
				_log.debug("LOG_DEBUG_EXTENSION", "*****Pack query passed", SuggestedCategory.NONE);;
				
				queryString = "wm_strategy.STRATEGYKEY='"+skuList.get("0").get("STRATEGYKEY").toString()+"'";
				qry = new Query("wm_strategy", queryString, null);
				BioCollectionBean strategyList = uow.getBioCollectionBean(qry);
				_log.debug("LOG_DEBUG_EXTENSION", "*****Strategy query passed", SuggestedCategory.NONE);;
				
				queryString = "wm_allocatestrategy.ALLOCATESTRATEGYKEY='"+strategyList.get("0").get("ALLOCATESTRATEGYKEY").toString()+"'";
				qry = new Query("wm_allocatestrategy", queryString, null);
				BioCollectionBean allocList = uow.getBioCollectionBean(qry);
				_log.debug("LOG_DEBUG_EXTENSION", "*****Allocate Strategy query passed", SuggestedCategory.NONE);;
	
				//store updated attributes
				explodedDetail.set("ORDERKEY", bio.get("ORDERKEY"));
				_log.debug("LOG_DEBUG_EXTENSION", "ExplodedDetail new Order Key: "+explodedDetail.get("ORDERKEY").toString(), SuggestedCategory.NONE);;
				explodedDetail.set("STORERKEY", bio.get("STORERKEY"));
				_log.debug("LOG_DEBUG_EXTENSION", "ExplodedDetail new Storer Key: "+explodedDetail.get("STORERKEY").toString(), SuggestedCategory.NONE);;
				explodedDetail.set("UNITPRICE", bio.get("UNITPRICE"));
				_log.debug("LOG_DEBUG_EXTENSION", "ExplodedDetail new Unit Price: "+explodedDetail.get("UNITPRICE").toString(), SuggestedCategory.NONE);;
				
				explodedDetail.set("SKU", bomList.get(""+index).get("COMPONENTSKU"));
				_log.debug("LOG_DEBUG_EXTENSION", "ExplodedDetail new Sku: "+explodedDetail.get("SKU").toString(), SuggestedCategory.NONE);;
				
				explodedDetail.set("PACKKEY", skuList.get("0").get("PACKKEY"));
				_log.debug("LOG_DEBUG_EXTENSION", "ExplodedDetail new Pack Key: "+explodedDetail.get("PACKKEY").toString(), SuggestedCategory.NONE);;
				
				explodedDetail.set("UOM", packList.get("0").get("PACKUOM3"));
				_log.debug("LOG_DEBUG_EXTENSION", "ExplodedDetail new UOM: "+explodedDetail.get("UOM").toString(), SuggestedCategory.NONE);;
				
				explodedDetail.set("ALLOCATESTRATEGYKEY", strategyList.get("0").get("ALLOCATESTRATEGYKEY"));
				_log.debug("LOG_DEBUG_EXTENSION", "ExplodedDetail new Allocate Strategy Key: "+explodedDetail.get("ALLOCATESTRATEGYKEY").toString(), SuggestedCategory.NONE);;
				explodedDetail.set("PREALLOCATESTRATEGYKEY", strategyList.get("0").get("PREALLOCATESTRATEGYKEY"));
				_log.debug("LOG_DEBUG_EXTENSION", "ExplodedDetail new Pre-Allocate Strategy Key: "+explodedDetail.get("PREALLOCATESTRATEGYKEY").toString(), SuggestedCategory.NONE);;
				
				explodedDetail.set("ALLOCATESTRATEGYTYPE", allocList.get("0").get("ALLOCATESTRATEGYTYPE"));
				_log.debug("LOG_DEBUG_EXTENSION", "ExplodedDetail new Allocate Strategy Type: "+explodedDetail.get("ALLOCATESTRATEGYTYPE").toString(), SuggestedCategory.NONE);;
				
				//Adjusted quantities
				double componentQty = Double.parseDouble(bomList.get(""+index).get("QTY").toString());
				
				// 2012-07-03
				// Modified by Will Pu
				// 写入原物料号
				// 2012-08-06
				// 拆分出来的明细行中写入库存地(LOTTABLE02)
				explodedDetail.set("LOTTABLE02", lottable02);
				explodedDetail.set("ORIGINALSKU", sku);
				explodedDetail.set("ORIGINALQTY1", originalqty);
				explodedDetail.set("OPENQTY", figureValue(bio.get("OPENQTY").toString(), componentQty));
				explodedDetail.set("SHIPPEDQTY", figureValue(bio.get("SHIPPEDQTY").toString(), componentQty));
				explodedDetail.set("ADJUSTEDQTY", figureValue(bio.get("ADJUSTEDQTY").toString(), componentQty));
				explodedDetail.set("QTYPREALLOCATED", figureValue(bio.get("QTYPREALLOCATED").toString(), componentQty));
				explodedDetail.set("QTYALLOCATED", figureValue(bio.get("QTYALLOCATED").toString(), componentQty));
				
				//Determine  current Order Line Number value
				if(oln<10000){
					key+="0";
					if(oln<1000){
						key+="0";
						if(oln<100){
							key+="0";
							if(oln<10){
								key+="0";
							}
						}
					}
				}
				key+=oln;
				explodedDetail.set("ORDERLINENUMBER", key);
				_log.debug("LOG_DEBUG_EXTENSION", "ExplodedDetail new orderlinenubmer: "+explodedDetail.get("ORDERLINENUMBER").toString(), SuggestedCategory.NONE);;
				oln++;
				key="";
				index++;
				unitOfWork.save();					//Save created bio
			}
			
			//replace/delete original record
			bio.delete();							//Delete exploded bio
			uow.saveUOW();							//Persist delete
			_log.debug("LOG_DEBUG_EXTENSION", "Completed Explode Item", SuggestedCategory.NONE);;
		}else{
			//Append selected order line number to arguments for error message
			_log.debug("LOG_DEBUG_EXTENSION", "No matching component records found", SuggestedCategory.NONE);;
			String temp = "";
			if(args!=null){
				temp = args+", "+bio.get("ORDERLINENUMBER").toString();
			}else{
				temp = bio.get("ORDERLINENUMBER").toString();
			}
			return temp;
		}
		return args;
	}
}

