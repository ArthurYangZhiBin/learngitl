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

import org.apache.commons.lang.StringUtils;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
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
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.wm_cyclecount.ui.CalculateAdvCatchWeightsHelper;

public class WeightAdjustmentSave extends SaveAction{
	private static final String REASONCODE = "REASONCODE";
	// Added for WM 9 3PL Enhancements - Catch weights - Weight Adjustment - Krishna Kuchipudi
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WeightAdjustmentSave.class);

	public WeightAdjustmentSave() { 
		_log.debug("EXP_1","AdjustmentSave Instantiated!!!",  SuggestedCategory.NONE);
	}

	protected int execute(ActionContext context, ActionResult result) throws UserException {	
		StateInterface state = context.getState();

		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();

		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);

		//get header data
		RuntimeFormInterface headerForm = state.getRuntimeForm(shellForm.getSubSlot("list_slot_1"), null);
		DataBean headerFocus = headerForm.getFocus();

		//get detail data
		RuntimeFormInterface detailForm = state.getRuntimeForm(shellForm.getSubSlot("list_slot_2"), null);

		//krishna kuchipudi- 3PL Enhancements -weight adjustments  - starts
		String wgtadjustmentKey =   (String)headerForm.getFormWidgetByName("WGTADJUSTMENTKEY").getValue();
		System.out.println(":::::::::::::::::"+detailForm.getName());

		if(detailForm != null && detailForm.getName().equalsIgnoreCase("wm_cust_weightadjustmentdetail_toggle_slotform"))
		{
			detailForm = state.findForm(detailForm,"wm_cust_weightadjustment_detail_detail_view");
		}
		String wgtadjustmentLineNumber =  (String)detailForm.getFormWidgetByName("WGTADJLINENUMBER").getValue();
		String storerkey=  (String)headerForm.getFormWidgetByName("STORERKEY").getValue();
		String SKU =  (String)detailForm.getFormWidgetByName("SKU").getValue();
		
		//This is stupid, why is the WeightAdjustmentPreSave disabled?
		//It doesn't make sense to combine validation and saving in the same extension
		//All this does is create hard to maintain code (see the ASN Save/Validation extension SaveASNReceipt)
		//So now I'm forced to add validation code to check that the Reason is not null
		//Ridiculous
		notNullValidation(context, detailForm);


		//check if this item is set up for inbound catch weight and end to end setial catch weight, if it is block, use 
		//should not change its weight. bug 662
		CalculateAdvCatchWeightsHelper helper = new CalculateAdvCatchWeightsHelper();
		if(helper.isEndToEndSerialInboundCatchWeight(storerkey, SKU)){
			String [] parameter = new String [1];
			parameter[0] = SKU;
			throw new UserException("WMEXP_WEIGHT_ADJUSTMENT_ENDTOEND_INBOUND", parameter);			
		}
		//bug 662 end


		String ADJGROSSWGT1 = null;

		RuntimeFormWidgetInterface copiesWidget = detailForm.getFormWidgetByName("ADJGROSSWGT");

		//		System.out.println(ADJGROSSWGT1);		
		//if (!((copiesWidget.getValue() == null)	&& !((String)copiesWidget.getValue() == ("")))
		ADJGROSSWGT1 = (String) copiesWidget.getValue().toString();

		//		System.out.println(ADJGROSSWGT1);	

		ADJGROSSWGT1=  (String)detailForm.getFormWidgetByName("ADJGROSSWGT").getValue().toString();
		//String ADJNETWGT11=(String)detailForm.getFormWidgetByName("ADJNETWGT1").getDisplayValue().toString();
		//detailForm.getFormWidgetByName("ADJNETWGT1").setValue(ADJNETWGT11);
		String ADJNETWGT1 =  (String)detailForm.getFormWidgetByName("ADJNETWGT").getValue().toString();
		String ADJTAREWGT1 =  (String)detailForm.getFormWidgetByName("ADJTAREWGT").getValue().toString();

		String	GROSSWGT1=  (String)detailForm.getFormWidgetByName("GROSSWGT").getValue().toString();

		String NETWGT1 =  (String)detailForm.getFormWidgetByName("NETWGT").getValue().toString();
		String TAREWGT1 =  (String)detailForm.getFormWidgetByName("TAREWGT").getValue().toString();
		String LOT =  (String)detailForm.getFormWidgetByName("LOT").getValue();
		String LOC =  (String)detailForm.getFormWidgetByName("LOC").getValue();
		String ID=(String)detailForm.getFormWidgetByName("ID").getValue();
		String QTY =  (String)detailForm.getFormWidgetByName("QTY").getValue().toString();

		//Weight Equilibrium check
		if(!(Double.parseDouble(ADJGROSSWGT1)==
			Double.parseDouble(ADJNETWGT1)+Double.parseDouble(ADJTAREWGT1)))
		{
			String[] errorParam = new String[1];
			errorParam[0]= "null";
			throw new UserException("WMEXP_VALIDATE_ADVCATCHWEIGHTS", errorParam);				
		}
		if(!(Double.parseDouble(GROSSWGT1)==
			Double.parseDouble(NETWGT1)+Double.parseDouble(TAREWGT1)))
		{
			String[] errorParam = new String[1];
			errorParam[0]= "null";
			throw new UserException("WMEXP_VALIDATE_ADVCATCHWEIGHTS", errorParam);				
		}
		validateQty(QTY, LOT,LOC, ID, storerkey, SKU, uowb,GROSSWGT1,NETWGT1,TAREWGT1);
		//	state.getDefaultUnitOfWork()
		//krishna kuchipudi-3PL Enhancements - weight adjustments  - end
		BioBean headerBioBean = null;
		try{
			if (headerFocus.isTempBio()) {//it is for insert header
				headerBioBean = uowb.getNewBio((QBEBioBean)headerFocus);				
				DataBean detailFocus = detailForm.getFocus();
				if(detailFocus.isTempBio()){
					//jp Begin
					QBEBioBean detailBioBean = (QBEBioBean)detailFocus;
					String adjustmentKey =   (String)headerForm.getFormWidgetByName("WGTADJUSTMENTKEY").getValue();
					String adjustmentLineNumber =  (String)detailForm.getFormWidgetByName("WGTADJLINENUMBER").getValue();
					String adjustmentLineNumber1 =  (String)detailForm.getFormWidgetByName("WGTADJUSTMENTKEY").getValue();
					detailBioBean.set("STORERKEY", headerBioBean.get("STORERKEY").toString());

					headerBioBean.addBioCollectionLink("WTADJDETAILBIOCOLLECTION", (QBEBioBean)detailFocus);
				}
			} else {
				//RuntimeFormInterface detailTab = state.getRuntimeForm(detailForm.getSubSlot("wm_cust_weightadjustmentdetail_toggle_slotform"), "Detail");

				//DataBean detailFocus = detailTab.getFocus();
				DataBean detailFocus = detailForm.getFocus();
				headerBioBean = (BioBean)headerFocus;
				if (detailFocus.isTempBio()) {
					QBEBioBean qbe = (QBEBioBean)detailFocus;		
					qbe.set("WGTADJUSTMENTKEY", headerBioBean.get("WGTADJUSTMENTKEY").toString());
					qbe.set("STORERKEY", headerBioBean.get("STORERKEY").toString());
					uowb.getNewBio(qbe);
				} 		    
			}
			uowb.saveUOW(true);
		}
		catch(UnitOfWorkException e){
			e.printStackTrace();
			_log.error("LOG_DEBUG_EXTENSION_AdjustmentSave", e.getStackTraceAsString(), SuggestedCategory.NONE);
			_log.error("LOG_DEBUG_EXTENSION_AdjustmentSave", "IN UnitOfWorkException", SuggestedCategory.NONE);
			Throwable nested = ((UnitOfWorkException) e).getDeepestNestedException();
			_log.error("LOG_DEBUG_EXTENSION_AdjustmentSave", "\tNested " + nested.getClass().getName(), SuggestedCategory.NONE);
			_log.error("LOG_DEBUG_EXTENSION_AdjustmentSave", "\tMessage " + nested.getMessage(), SuggestedCategory.NONE);
			if(nested instanceof ServiceObjectException)
			{
				String reasonCode = nested.getMessage();
				throwUserException(e, reasonCode, null);
			}
			else
			{
				throwUserException(e, "ERROR_SAVING_BIO", null);
			}

		}
		catch(EpiException e){	
			e.printStackTrace();

			_log.debug("LOG_DEBUG_EXTENSION_AdjustmentSave", e.getErrorMessage(), SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION_AdjustmentSave", e.getErrorName(), SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION_AdjustmentSave", e.getFullErrorName(), SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION_AdjustmentSave", e.getStackTraceAsString(), SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION_AdjustmentSave", e.toString(), SuggestedCategory.NONE);

			throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
		}
		//krishna-3PL Enhancements -Catch weight tracking -starts

		try {
			//EXEDataObject results = null;
			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();		 
			Array parms = new Array();

			parms.add(new TextData(wgtadjustmentKey));

			parms.add(new TextData(wgtadjustmentLineNumber));
			parms.add(new TextData(storerkey));
			parms.add(new TextData(SKU));

			parms.add(new TextData(ADJGROSSWGT1));
			parms.add(new TextData(ADJNETWGT1));
			parms.add(new TextData(ADJTAREWGT1));
			parms.add(new TextData(GROSSWGT1));
			parms.add(new TextData(NETWGT1));
			parms.add(new TextData(TAREWGT1));
			parms.add(new TextData(LOT));
			parms.add(new TextData(LOC));
			parms.add(new TextData(ID));
			parms.add(new TextData(QTY)); 
			parms.add(new TextData(SKU));
			//parms.add(new TextData("WeightADJ"));

			actionProperties.setProcedureParameters(parms);
			actionProperties.setProcedureName("WeightsAdjustment");
			//wgtadjustmentkey,weightadjustmentlinenumber,storerkey,sku,ADJGROSSWGT1,ADJNETWGT1,ADJTAREWGT1,GROSSWGT1,NETWGT1,TAREWGT1,LOT,LOC,ID,qty,trantype
			WmsWebuiActionsImpl.doAction(actionProperties);

		} catch (Exception e) {
			e.getMessage();
			UserException UsrExcp = new UserException(e.getMessage(), new Object[]{});
			throw UsrExcp;
		}
		//krishna-3PL Enhancements -ends
		uowb.clearState();
		result.setFocus(headerBioBean);

		return RET_CONTINUE;
	}

	private void notNullValidation(ActionContext context,
			RuntimeFormInterface detailForm) throws UserException {
		DataBean detailFocus1 = detailForm.getFocus();
		String reasonCode = BioAttributeUtil.getString(detailFocus1, REASONCODE);
		if(StringUtils.isBlank(reasonCode)){
			RuntimeFormWidgetInterface reasonCodeWidget = detailForm.getFormWidgetByName(REASONCODE);
			String widgetLabel = FormUtil.getWidgetLabel(context, reasonCodeWidget);
			widgetLabel = com.ssaglobal.scm.wms.util.StringUtils.removeTrailingColon(widgetLabel);
			throw new UserException("WMEXP_REQ", new Object[]{widgetLabel});
		}
	}
	/*
	private QBEBioBean populateSerial(UnitOfWorkBean uowb, BioBean serialTmpBio ) throws DataBeanException{
		QBEBioBean newSerial = uowb.getQBEBioWithDefaults("wm_adjustmentdetailserial");


		newSerial.set("WGTADJUSTMENTKEY", serialTmpBio.get("WGTADJUSTMENTKEY") );
		newSerial.set("WGTADJLINENUMBER", serialTmpBio.get("WGTADJLINENUMBER"));
		newSerial.set("qty",serialTmpBio.get("qty"));
		newSerial.set("lot",serialTmpBio.get("lot").toString().trim());
		newSerial.set("loc",serialTmpBio.get("loc"));
		newSerial.set("serialnumber",serialTmpBio.get("serialnumber"));
		newSerial.set("serialnumberlong",serialTmpBio.get("serialnumberlong"));
		newSerial.set("id", serialTmpBio.get("id"));
		newSerial.set("storerkey", serialTmpBio.get("storerkey"));
		newSerial.set("sku", serialTmpBio.get("sku"));


		newSerial.set("data2", serialTmpBio.get("data2"));
		newSerial.set("data3", serialTmpBio.get("data3"));
		newSerial.set("data4", serialTmpBio.get("data4"));
		newSerial.set("data5", serialTmpBio.get("data5"));




		Double netweight = (Double)serialTmpBio.get("netweight");

		if(netweight==null )
			newSerial.set("netweight", new Double(0.0));
		else
			newSerial.set("netweight", serialTmpBio.get("netweight"));

		return newSerial;

	}

	private void buildAdjustmentDetailSerials(UnitOfWorkBean uowb, String WGTADJUSTMENTKEY, String WGTADJLINENUMBER){
		AdjustmentDetailSerialTmpDAO adjDetailSerialTmpDAO = new AdjustmentDetailSerialTmpDAO();

		List<AdjustmentDetailSerialTmpDTO> list = adjDetailSerialTmpDAO.findAdjustmentDetailSerialTmp(WGTADJUSTMENTKEY, WGTADJLINENUMBER);


		for(AdjustmentDetailSerialTmpDTO adjDetailSerialTmp : list){
			String serialnumber =  adjDetailSerialTmp.getSerialnumber();

			if(Double.parseDouble(adjDetailSerialTmp.getQty()) > 0){
				AdjustmentDetailSerialDAO adjDetailSerialDAO = new AdjustmentDetailSerialDAO();
				AdjustmentDetailSerialDTO adjDetailSerial = adjDetailSerialDAO.findAdjustmentDetailSerialTmp(WGTADJUSTMENTKEY, WGTADJLINENUMBER, serialnumber);

				if(adjDetailSerial==null){
					//insert into adjustDetailSerial
					//adjDetailSerial.setWGTADJUSTMENTKEY(adjDetailSerialTmp.getADJUSTMENTKEY());
					//adjDetailSerial.setWGTADJLINENUMBER(adjDetailSerialTmp.getWGTADJLINENUMBER());
					adjDetailSerial.setData2(adjDetailSerialTmp.getData2());
					adjDetailSerial.setData3(adjDetailSerialTmp.getData3());
					adjDetailSerial.setData4(adjDetailSerialTmp.getData4());
					adjDetailSerial.setData5(adjDetailSerialTmp.getData5());
					adjDetailSerial.setGrossweight(adjDetailSerialTmp.getGrossweight());
					adjDetailSerial.setId(adjDetailSerialTmp.getId());
					adjDetailSerial.setLoc(adjDetailSerialTmp.getLoc());
					adjDetailSerial.setLot(adjDetailSerial.getLot());
					adjDetailSerial.setNetweight(adjDetailSerialTmp.getNetweight());
					adjDetailSerial.setQty(adjDetailSerialTmp.getQty());
					adjDetailSerial.setSerialnumber(adjDetailSerialTmp.getSerialnumber());
					adjDetailSerial.setSku(adjDetailSerialTmp.getSku());
					adjDetailSerial.setStorerkey(adjDetailSerialTmp.getStorerkey());

					AdjustmentDetailSerialDAO.insertAdjustmentDetailSerial(adjDetailSerial);

				}else{
					//update
					adjDetailSerial.setQty(adjDetailSerialTmp.getQty());

					AdjustmentDetailSerialDAO.updateAdjustmentDetailSerial(adjDetailSerial);

				}
			}else if(Double.parseDouble(adjDetailSerialTmp.getQty()) < 0){
				AdjustmentDetailSerialDAO adjDetailSerialDAO = new AdjustmentDetailSerialDAO();
				AdjustmentDetailSerialDTO adjDetailSerial = adjDetailSerialDAO.findAdjustmentDetailSerialTmp(WGTADJUSTMENTKEY, WGTADJLINENUMBER, serialnumber);

				if(adjDetailSerial!=null){
					//delete
				}
			}
		}
	} */

	private boolean validateQty(String qty, String lot, String loc, String id, String storerkey, String sku, UnitOfWorkBean uowb,String GrossWgt1,String NetWgt1,String TareWgt1) throws UserException{
		int size = 0;
		String qryStr = null;
		String[] params = new String[4];
		if(id == null){
			id = "";
		}
		//qryStr = "wm_lotxlocxid.LOT='"+lot+"' AND wm_lotxlocxid.LOC='"+loc+"' AND wm_lotxlocxid.ID='"+id+"' AND wm_lotxlocxid.STORERKEY='"+storerkey+"' AND wm_lotxlocxid.SKU='"+sku+"'"+" AND wm_lotxlocxid.GROSSWGT1= "+Double.parseDouble(GrossWgt1)+" AND wm_lotxlocxid.NETWGT1= "+Double.parseDouble(NetWgt1)+" AND wm_lotxlocxid.TAREWGT1= "+Double.parseDouble(TareWgt1);
		qryStr = "wm_cust_wt_lotxlocxid.LOT='"+lot+"' AND wm_cust_wt_lotxlocxid.LOC='"+loc+"' AND wm_cust_wt_lotxlocxid.ID='"+id+"' AND wm_cust_wt_lotxlocxid.STORERKEY='"+storerkey+"' AND wm_cust_wt_lotxlocxid.SKU='"+sku+"'";
		Query qry = new Query("wm_cust_wt_lotxlocxid", qryStr, null);
		BioCollectionBean bcBean = uowb.getBioCollectionBean(qry);


		try{
			if(bcBean != null )
			{
				size= bcBean.size();
				if(size != 1){
					//No LPN
					//if(element==null){

					throw new UserException("WMEXP_WRONG_PARAMETERS", new Object[] {});
					//	}
				}
			}
			//size =  bcBean.getSize();


			// BioBean element;
			//element = (BioBean)bcBean.elementAt(0);



		}catch(Exception e){
			e.printStackTrace();
			throw new UserException("WMEXP_WRONG_PARAMETERS", new Object[] {});
			//	throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
		}
		return true;
	}
}
