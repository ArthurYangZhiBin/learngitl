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
package com.ssaglobal.scm.wms.wm_adjustment.ui;

import java.rmi.RemoteException;
import java.util.HashMap;

import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.agileitp.forte.framework.TextData;
import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.extensions.IExtension;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.common.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.datalayer.WmsDataProviderImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject.GetDoubleOutputParam;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject.GetStringOutputParam;
import com.ssaglobal.scm.wms.service.dutilitymanagement.dto.SkuSNConfDTO;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.SessionUtil;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;
import com.ssaglobal.scm.wms.util.dao.SkuSNConfDAO;
import com.ssaglobal.scm.wms.wm_cyclecount.ui.CalculateAdvCatchWeightsHelper;

public class AdjustmentSetChangeQty extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(AdjustmentSetChangeQty.class);
	protected int execute(ActionContext context, ActionResult result)throws EpiException{
		RuntimeFormWidgetInterface source = context.getSourceWidget();
		String sourceName = source.getName();
		String sourceValue = source.getDisplayValue();
		QBEBioBean focus = (QBEBioBean)source.getForm().getFocus();
		Object UOM = focus.get("UOM");
		String lot = BioAttributeUtil.getString(focus, "LOT");
		String loc = BioAttributeUtil.getString(focus, "LOC");
		String id = BioAttributeUtil.getString(focus, "ID");
		if(id == null || "".equalsIgnoreCase(id) || "null".equalsIgnoreCase(id)){
			id = " ";
		}
		
		
		String formname = source.getForm().getName();
		
		String currUOM =null;
		double dblAdjQty=0;
		double dblTrgtQty=0;
		double dblOrgQty=0;
		if(UOM!=null){
			currUOM = UOM.toString();			
		}else{
			currUOM = UOMMappingUtil.getPACKUOM3Val(UOMMappingUtil.PACK_STD, context.getState().getDefaultUnitOfWork()); //AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
		}
		
		source.getForm().getFormWidgetByName("CHANGEQTYUOM").setValue(currUOM);
		source.getForm().getFormWidgetByName("ORIGCHANGEQTY").setValue(sourceValue);
		if(sourceName.equals("QTY")){
			if(sourceValue==null || sourceValue==""){
				source.getForm().getFormWidgetByName("TARGETQTY").setValue("");
				source.getForm().getFormWidgetByName("ISADJ").setValue(null);
			}else{
				source.getForm().getFormWidgetByName("ISADJ").setValue("1");
				//08/12/2009--Sateesh Billa- 3PL Enhancements- adv catch weight starts
				Object qtyObj = source.getForm().getFormWidgetByName("QTY").getValue();
				if (qtyObj!=null && qtyObj.toString().trim().length() > 0){
					dblAdjQty= (new Double(qtyObj.toString())).doubleValue();

					qtyObj =source.getForm().getFormWidgetByName("ORIGCURRQTY").getValue();
					dblOrgQty=(new Double(qtyObj.toString())).doubleValue();
					dblTrgtQty=dblAdjQty+dblOrgQty;
				}
				//08/12/2009--Sateesh Billa - 3PL Enhancements  -adv catch weight -end

				}
		}else{
			if(sourceValue==null || sourceValue==""){
				focus.set("QTY", null);
				source.getForm().getFormWidgetByName("ISADJ").setValue(null);
			}else{
//				08/12/2009--Sateesh Billa -- 3PL Enhancements  -adv catch weight -START
				if(sourceName.equals("TARGETQTY")){

				Object qtyObj = source.getForm().getFormWidgetByName("TARGETQTY").getValue();
				if (qtyObj!=null && qtyObj.toString().trim().length() > 0)
					dblTrgtQty= (new Double(qtyObj.toString())).doubleValue();
					qtyObj =source.getForm().getFormWidgetByName("ORIGCURRQTY").getValue();
					dblOrgQty=(new Double(qtyObj.toString())).doubleValue();
					if(dblTrgtQty>dblOrgQty) dblAdjQty=dblTrgtQty-dblOrgQty;
					else
					{
						dblAdjQty=dblOrgQty-dblTrgtQty;
						dblAdjQty=dblAdjQty* (-1);
					}


				}
				//08/12/2009--Sateesh Billa -- 3PL Enhancements  -adv catch weight -END


				source.getForm().getFormWidgetByName("ISADJ").setValue("0");
			}
		}
		
		//jp SN Begin - Populate SerialInventory list


		String storerkey = source.getForm().getFormWidgetByName("STORERKEY").getValue().toString();
		String sku =source.getForm().getFormWidgetByName("SKU").getValue().toString();

		if(sourceName.equals("QTY")){
			double currentQty=0;
			double qty=0;

			Object currentQtyObj = source.getForm().getFormWidgetByName("CURRENTQTY").getValue();
			
			if (currentQtyObj!=null && currentQtyObj.toString().trim().length() > 0){
				currentQty = Double.parseDouble(currentQtyObj.toString());
			}
			
			Object qtyObj = source.getForm().getFormWidgetByName("QTY").getValue();
			if (qtyObj!=null && qtyObj.toString().trim().length() > 0){
				qty = Double.parseDouble(qtyObj.toString());
			}

			
			if(currentQty+qty>0  && isSkuSerialNumberEndtoEnd(storerkey, sku)){
				_log.debug("LOG_SYSTEM_OUT","[AdjustmentSetChangeQty]populatingAdjustmentDetailSerialFromSerialInventory",100L);
				populateAjustmentDetailSerialFromSerialInventory(context, source);
			}
			
			

		}else if (sourceName.equals("TARGETQTY")){
			double targetQty=0;

			Object targetQtyObj = source.getForm().getFormWidgetByName("TARGETQTY").getValue();
			if (targetQtyObj!=null && targetQtyObj.toString().trim().length() > 0){
				targetQty = Double.parseDouble(targetQtyObj.toString());
			}

			if(targetQty>0  && isSkuSerialNumberEndtoEnd(storerkey, sku)){
				_log.debug("LOG_SYSTEM_OUT","[AdjustmentSetChangeQty]populatingAdjustmentDetailSerialFromSerialInventory",100L);
				populateAjustmentDetailSerialFromSerialInventory(context, source);
			}

			
		}
		
		source.getForm().getFormWidgetByName("QTYSELECTED").setValue("0");
		//jp SN End.

		//8/12/2009 Sateesh Billa - 3PL Enhancements  -adv catch weight- Adjustment starts

		boolean enabledadvCatWgt=false;
		double trgtGwgt=0;
		double trgtNwgt=0;
		double trgtTwgt=0;

		double adjGwgt=0;
		double adjNwgt=0;
		double adjTwgt=0;
		String ACTUALGROSSWGT="ACTUALGROSSWGT";
		String ACTUALNETWGT="ACTUALNETWGT";
		String ACTUALTAREWGT="ACTUALTAREWGT";
		String ADJGROSSWGT="ADJGROSSWGT";
		String ADJNETWGT="ADJNETWGT";
		String ADJTAREWGT="ADJTAREWGT";



		CalculateAdvCatchWeightsHelper helper = new CalculateAdvCatchWeightsHelper();
			try{
				String enabledAdvCatWght=helper.isAdvCatchWeightEnabled(storerkey,sku);
				if((enabledAdvCatWght!=null)&&(enabledAdvCatWght.equalsIgnoreCase("1")))
				{
					enabledadvCatWgt=true;
				}
					//TODO calculate weights & assign to the variables

					   HashMap actualWgts = null;
//					   actualWgts=helper.getCalculatedWeightsLot( storerkey, sku,dblTrgtQty, currUOM, focus.get("PACKKEY").toString());
					   actualWgts=helper.getCalculatedWeightsByLotLocID( lot, loc, id, dblTrgtQty, currUOM, focus.get("PACKKEY").toString(),context.getState().getDefaultUnitOfWork());	//defect 1224
					   trgtGwgt= ((Double)actualWgts.get("GROSSWEIGHT")).doubleValue();
					   trgtNwgt= ((Double)actualWgts.get("NETWEIGHT")).doubleValue();
					   trgtTwgt= ((Double)actualWgts.get("TAREWEIGHT")).doubleValue();

					   actualWgts=helper.getCalculatedWeightsByLotLocID( lot, loc, id,dblAdjQty, currUOM, focus.get("PACKKEY").toString(),context.getState().getDefaultUnitOfWork());	//defect 1224
					   adjGwgt= ((Double)actualWgts.get("GROSSWEIGHT")).doubleValue();
					   adjNwgt= ((Double)actualWgts.get("NETWEIGHT")).doubleValue();
					   adjTwgt= ((Double)actualWgts.get("TAREWEIGHT")).doubleValue();

					   if (trgtTwgt==0) trgtTwgt=trgtGwgt-trgtNwgt;
					   if (trgtNwgt==0) trgtNwgt=trgtGwgt-trgtTwgt;
					   if (trgtGwgt==0) trgtGwgt=trgtNwgt+trgtTwgt;

					   if (adjTwgt==0) adjTwgt=adjGwgt-adjNwgt;
					   if (adjNwgt==0) adjNwgt=adjGwgt-adjTwgt;
					   if (adjGwgt==0) adjGwgt=adjNwgt+adjTwgt;
				


				if(focus.isTempBio()){
					QBEBioBean qbe = (QBEBioBean) focus;
					context.getState().getCurrentRuntimeForm();
					qbe.set(ACTUALGROSSWGT, trgtGwgt);
					qbe.set(ACTUALNETWGT, trgtNwgt);
					qbe.set(ACTUALTAREWGT, trgtTwgt);
					qbe.set(ADJGROSSWGT, adjGwgt);
					qbe.set(ADJNETWGT, adjNwgt);
					qbe.set(ADJTAREWGT, adjTwgt);

				}/*else{
					BioBean bio = (BioBean)focus;
					bio.set("ACTUALGROSSWGT", trgtGwgt);
					bio.set("ACTUALNETWGT", trgtNwgt);
					bio.set("ACTUALTAREWGT", trgtTwgt);
					bio.set("ADJGROSSWGT", adjGwgt);
					bio.set("ADJNETWGT", adjNwgt);
					bio.set("ADJTAREWGT", adjTwgt);
				}*/

				source.getForm().getFormWidgetByName(ACTUALGROSSWGT).setDisplayValue(""+trgtGwgt);
				source.getForm().getFormWidgetByName(ACTUALNETWGT).setDisplayValue(""+trgtNwgt);
				source.getForm().getFormWidgetByName(ACTUALTAREWGT).setDisplayValue(""+trgtTwgt);
				source.getForm().getFormWidgetByName(ADJGROSSWGT).setDisplayValue(""+adjGwgt);
				source.getForm().getFormWidgetByName(ADJNETWGT).setDisplayValue(""+adjNwgt);
				source.getForm().getFormWidgetByName(ADJTAREWGT).setDisplayValue(""+adjTwgt);



/*				if(enabledadvCatWgt)
				{
					HashMap enabledInfo=helper.getEnabledWeightInfo(storerkey,sku);

					if(((String)enabledInfo.get("catchgrosswgt")).equalsIgnoreCase("1")) 
						source.getForm().getFormWidgetByName(ACTUALGROSSWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					else 
						source.getForm().getFormWidgetByName(ACTUALGROSSWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);

					if(((String)enabledInfo.get("catchnetwgt")).equalsIgnoreCase("1")) 
						source.getForm().getFormWidgetByName(ACTUALNETWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					else 
						source.getForm().getFormWidgetByName(ACTUALNETWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);

					if(((String)enabledInfo.get("catchtarewgt")).equalsIgnoreCase("1")) 
						source.getForm().getFormWidgetByName(ACTUALTAREWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					else 
						source.getForm().getFormWidgetByName(ACTUALTAREWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);

					if(((String)enabledInfo.get("catchgrosswgt")).equalsIgnoreCase("1")) 
						source.getForm().getFormWidgetByName(ADJGROSSWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					else 
						source.getForm().getFormWidgetByName(ADJGROSSWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);

					if(((String)enabledInfo.get("catchnetwgt")).equalsIgnoreCase("1")) 
						source.getForm().getFormWidgetByName(ADJNETWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					else 
						source.getForm().getFormWidgetByName(ADJNETWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);

					if(((String)enabledInfo.get("catchtarewgt")).equalsIgnoreCase("1")) 
						source.getForm().getFormWidgetByName(ADJTAREWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					else 
						source.getForm().getFormWidgetByName(ADJTAREWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);

				}
				else
				{
					source.getForm().getFormWidgetByName(ACTUALGROSSWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					source.getForm().getFormWidgetByName(ACTUALNETWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					source.getForm().getFormWidgetByName(ACTUALTAREWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					source.getForm().getFormWidgetByName(ADJGROSSWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					source.getForm().getFormWidgetByName(ADJNETWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					source.getForm().getFormWidgetByName(ADJTAREWGT).setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);

				}
*/

			    focus.setValue(ACTUALGROSSWGT, trgtGwgt);
			    focus.setValue(ACTUALNETWGT, trgtNwgt);
			    focus.setValue(ACTUALTAREWGT, trgtTwgt);

			    focus.setValue(ADJGROSSWGT, adjGwgt);
			    focus.setValue(ADJNETWGT, adjNwgt);
			    focus.setValue(ADJTAREWGT, adjTwgt);
			    //defect 1224.b
			    SessionUtil.setInteractionSessionAttribute("OLDTRGWT", trgtGwgt, context.getState());
			    SessionUtil.setInteractionSessionAttribute("OLDTRNWT", trgtNwgt, context.getState());
			    SessionUtil.setInteractionSessionAttribute("OLDTRTWT", trgtTwgt, context.getState());
			    SessionUtil.setInteractionSessionAttribute("OLDADGWT", adjGwgt, context.getState());
			    SessionUtil.setInteractionSessionAttribute("OLDADNWT", adjNwgt, context.getState());
			    SessionUtil.setInteractionSessionAttribute("OLDADTWT", adjTwgt, context.getState());
			    //defect1224.e
			    result.setFocus(focus);
			}catch(Exception e){
				e.printStackTrace();
				return RET_CANCEL;
			}


			    //Sateesh Billa - 3PL Enhancements  -Adv Catch Weight Ends

		return RET_CONTINUE;
	}
	

	/***
	 * For the given lot, loc, id, it copies the SerialInventory records into AdjustmentDetailSerialTmp table
	 * @param context	Context
	 * @param source  	Widget
	 */
	private void populateAjustmentDetailSerialFromSerialInventory(ActionContext context, RuntimeFormWidgetInterface source){
		StateInterface state = context.getState();
		RuntimeFormExtendedInterface form = (RuntimeFormExtendedInterface)source.getForm();
		SlotInterface slotSerialInventory = (SlotInterface)  form.getSubSlot(AdjustmentHelper.SLOT_SERIALINVENTORYLIST);
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();

		String lot = (String) form.getFormWidgetByName("LOT").getValue();
		String loc = (String) form.getFormWidgetByName("LOC").getValue();
		String id =  (String) form.getFormWidgetByName("ID").getValue();
		String adjustmentLineNumber = (String)form.getFormWidgetByName("ADJUSTMENTLINENUMBER").getValue();

		
		//RuntimeFormInterface parent =form.getParentForm(state);
		RuntimeFormInterface parent = FormUtil.findForm((RuntimeFormInterface)form, "wms_list_shell", "wms_list_shell", state);
		SlotInterface headerSlot = parent.getSubSlot("list_slot_1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot,"wm_adjustment_detail_view");


		String adjustmentKey = headerForm.getFormWidgetByName("ADJUSTMENTKEY").getValue().toString();
		try {
			
			if(lot!=null && loc!=null && id!=null){
				
				//Delete previous data
				String stmt ="DELETE FROM adjustmentdetailserialtmp "+
					" WHERE adjustmentkey ='"+ adjustmentKey +"' "+
					" AND adjustmentlinenumber = '"+adjustmentLineNumber+"'";
					
				_log.debug("LOG_SYSTEM_OUT","stmt:"+stmt,100L);
				new WmsDataProviderImpl().executeUpdateSql(stmt);
				
				//Select data from SerialInventory
				stmt="SELECT serialnumber, storerkey, sku, " +
					" lot, id, loc, qty, data2, data3, data4, data5, grossweight, netweight, serialnumberlong "+ 
					" FROM serialinventory " +
					" WHERE lot='"+ lot+ "'" +
					" AND loc ='"+ loc + "' "+
					" AND id ='"+ id + "'";

				_log.debug("LOG_SYSTEM_OUT","stmt:"+stmt,100L);
				//Populate adjustmentDetailSerialTmp with data from SerialInventory
				EXEDataObject serialInventoryList = WmsWebuiValidationSelectImpl.select(stmt);
				for(int idx =0; idx < serialInventoryList.getRowCount(); idx++){
					buildInsertAdjustmentDetailSerial(serialInventoryList, adjustmentKey, adjustmentLineNumber);
					
				    /**
				    stmt = buildInsertAdjustmentDetailSerial(serialInventoryList, adjustmentKey, adjustmentLineNumber);
				    if(stmt==null)
				    	continue;
					
					new WmsDataProviderImpl().executeUpdateSql(stmt);
					**/
					serialInventoryList.getNextRow();
				}
				
				
				
				//Refresh list view of adjusmentDetailSerialTmp
				new AdjustmentHelper().refreshAdjustmentDetailSerialList(uowb, form, state, slotSerialInventory, adjustmentKey, adjustmentLineNumber);
				/**
				Query queryAdj = new Query("wm_adjustmentdetailserialtmp", 
						"wm_adjustmentdetailserialtmp.ADJUSTMENTKEY = '"+adjustmentKey+"' " +
						" AND wm_adjustmentdetailserialtmp.ADJUSTMENTLINENUMBER='"+adjustmentLineNumber+"' ", null);
				BioCollectionBean adjDetailSerialTmpList = uowb.getBioCollectionBean(queryAdj);
				form.setFocus(state, slotSerialInventory, "", adjDetailSerialTmpList, "wm_adjustmentdetailserialtmp_list_view_persp");
				**/
			}
		} catch (EpiException e) {
			_log.debug("LOG_SYSTEM_OUT","[SNumber]:error when setting focus for serial inventory list",100L);
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private boolean isSkuSerialNumberEndtoEnd(String storerkey, String sku){
		SkuSNConfDTO skuConf = SkuSNConfDAO.getSkuSNConf(storerkey, sku);
		
		_log.debug("LOG_SYSTEM_OUT","[isSkuSerialNumberEndToEnd]:"+skuConf.getSNum_EndToEnd(),100L);
		if(skuConf.getSNum_EndToEnd().equalsIgnoreCase("1"))
			return true;
		else
			return false;
		
	}
	
	/**
	private SkuSNConfDTO getSkuSNConf(String storerkey, String sku){
		SkuSNConfDTO skuSNConf = new SkuSNConfDTO();
		
		String stmt = "SELECT SNum_Position, SNum_Length, SNum_Delimiter, SNum_Delim_Count, " +
                      " SNum_Quantity, SNum_Incr_Pos, SNum_Incr_Length, SNum_EndToEnd, SNum_Mask " +
                      " FROM SKU " +
                      " WHERE Storerkey = '"+storerkey +"' " +
                      " AND Sku = '"+sku+"'";

		try {
			EXEDataObject skuConf = WmsWebuiValidationSelectImpl.select(stmt);
			GetIntegerOutputParam paramInt= null;
			GetStringOutputParam paramString = null;
			
			paramString= skuConf.getString(new TextData("SNum_Position"), new String());
			skuSNConf.setSnumPosition(paramString.pResult.trim().length()>0 ? Integer.parseInt(paramString.pResult) : 1);

			paramString= skuConf.getString(new TextData("SNum_Length"), new String());
			skuSNConf.setSnumLength(paramString.pResult.trim().length()>0 ? Integer.parseInt(paramString.pResult) : 0);

			paramString= skuConf.getString(new TextData("SNum_Delimiter"), skuSNConf.getSnumDelimiter());
			skuSNConf.setSnumDelimiter(paramString.pResult);

			paramString= skuConf.getString(new TextData("SNum_Delim_Count"), new String());
			skuSNConf.setSnumDelimCount(paramString.pResult.trim().length()>0 ? Integer.parseInt(paramString.pResult) : 0);

			paramString= skuConf.getString(new TextData("SNum_Quantity"), new String());
			skuSNConf.setSnumQuantity(paramString.pResult.trim().length()>0 ? Integer.parseInt(paramString.pResult) : 1);

			paramString= skuConf.getString(new TextData("SNum_Incr_Pos"), new String());
			skuSNConf.setSnumIncrPos(paramString.pResult.trim().length()>0 ? Integer.parseInt(paramString.pResult) : 0);

			paramString= skuConf.getString(new TextData("SNum_Incr_Length"), new String());
			skuSNConf.setSnumIncrLength(paramString.pResult.trim().length()>0 ? Integer.parseInt(paramString.pResult) : 0);

			paramString= skuConf.getString(new TextData("SNum_EndToEnd"), new String());
			skuSNConf.setSnumEndToEnd(paramString.pResult.trim().length()>0 ? Integer.parseInt(paramString.pResult) : 0);

			paramString= skuConf.getString(new TextData("SNum_Mask"), skuSNConf.getSnumMask());
			skuSNConf.setSnumMask(paramString.pResult);

		} catch (DPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return skuSNConf;
	}
	**/
	/***
	 * Populates AdjustmentDetailSerialTmpDTO object from SerialInventory record
	 * @param serialInventoryList
	 * @param adjustmentKey
	 * @param adjustmentLineNumber
	 */
	private void buildInsertAdjustmentDetailSerial(EXEDataObject serialInventoryList, String adjustmentKey, String adjustmentLineNumber){
		
		if (serialInventoryList == null || adjustmentKey==null || adjustmentLineNumber==null)
			return;
		
	
		AdjustmentDetailSerialTmpDTO adjDetailSerial = new AdjustmentDetailSerialTmpDTO();
		
		
		adjDetailSerial.setAdjustmentkey(adjustmentKey);
		adjDetailSerial.setAdjustmentlinenumber(adjustmentLineNumber);
		
		Object temp = null;
		GetStringOutputParam param = null;
		GetDoubleOutputParam paramDouble = null;
		param = serialInventoryList.getString(new TextData("serialnumber"), adjDetailSerial.getSerialnumber());
		adjDetailSerial.setSerialnumber(param.pResult);
		
		param = serialInventoryList.getString(new TextData("storerkey"), adjDetailSerial.getStorerkey());
		adjDetailSerial.setStorerkey(param.pResult);
		
		param = serialInventoryList.getString(new TextData("sku"), adjDetailSerial.getSku());
		adjDetailSerial.setSku(param.pResult);

		param = serialInventoryList.getString(new TextData("lot"), adjDetailSerial.getLot());
		adjDetailSerial.setLot(param.pResult);

		param = serialInventoryList.getString(new TextData("id"), adjDetailSerial.getId());
		adjDetailSerial.setId(param.pResult);
		
		param = serialInventoryList.getString(new TextData("loc"), adjDetailSerial.getLoc());
		adjDetailSerial.setLoc(param.pResult);

		double qty=0.0;
		paramDouble = serialInventoryList.getDouble(new TextData("qty"), qty);
		//qty = paramDouble.pResult;
		adjDetailSerial.setQty((new Double(qty)).toString());
		

		param = serialInventoryList.getString(new TextData("data2"), adjDetailSerial.getData2());
		adjDetailSerial.setData2(param.pResult);
		
		param = serialInventoryList.getString(new TextData("data3"), adjDetailSerial.getData3());
		adjDetailSerial.setData3(param.pResult);

		
		param = serialInventoryList.getString(new TextData("data4"), adjDetailSerial.getData4());
		adjDetailSerial.setData4(param.pResult);

		param = serialInventoryList.getString(new TextData("data5"), adjDetailSerial.getData5());
		adjDetailSerial.setData5(param.pResult);

		
		paramDouble = serialInventoryList.getDouble(new TextData("grossweight"), qty);
		qty = paramDouble.pResult;
		adjDetailSerial.setGrossweight((new Double(qty)).toString());

		
		paramDouble = serialInventoryList.getDouble(new TextData("netweight"), qty);
		qty = paramDouble.pResult;
		adjDetailSerial.setNetweight((new Double(qty)).toString());


		param = serialInventoryList.getString(new TextData("serialnumberlong"), adjDetailSerial.getSerialnumberlong());
		adjDetailSerial.setSerialnumberlong(param.pResult);

		_log.debug("LOG_SYSTEM_OUT","Inserting this object:" +adjDetailSerial.toString(),100L);
		AdjustmentDetailSerialTmpDAO.insertAdjustmentDetailSerialTmp(adjDetailSerial);

		
		return;
	}
	
	
	
	
}