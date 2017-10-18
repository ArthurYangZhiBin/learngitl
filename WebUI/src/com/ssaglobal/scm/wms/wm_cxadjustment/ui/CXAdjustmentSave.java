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
package com.ssaglobal.scm.wms.wm_cxadjustment.ui;

import java.math.BigDecimal;
import java.util.List;

import com.agileitp.forte.framework.internal.ServiceObjectException; //AW 07/06/2009 Machine: 1949820 SDIS: SCM-00000-04527
import com.epiphany.shr.data.error.UnitOfWorkException; //AW 07/06/2009 Machine: 1949820 SDIS: SCM-00000-04527
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.dao.AdjustmentDetailSerialDAO;
import com.ssaglobal.scm.wms.util.dto.AdjustmentDetailSerialDTO;

public class CXAdjustmentSave extends SaveAction{
    protected static ILoggerCategory _log = LoggerFactory.getInstance(CXAdjustmentSave.class);

    public CXAdjustmentSave() { 
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

		BioBean headerBioBean = null;
		try{
			

			if (headerFocus.isTempBio()) {//it is for insert header
					headerBioBean = uowb.getNewBio((QBEBioBean)headerFocus);				
					DataBean detailFocus = detailForm.getFocus();
					if(detailFocus.isTempBio()){
						//jp Begin
						QBEBioBean detailBioBean = (QBEBioBean)detailFocus;
						detailBioBean.set("CXADJUSTMENTKEY", (String)headerForm.getFormWidgetByName("CXADJUSTMENTKEY").getValue());
						detailBioBean.set("STORERKEY", (String)headerForm.getFormWidgetByName("STORERKEY").getValue());
						headerBioBean.addBioCollectionLink("ADJUSTMENTDETAIL", detailBioBean);
					}
				} else {
					
					RuntimeFormInterface detailTab = state.getRuntimeForm(detailForm.getSubSlot("wm_cxadjustmentdetail_toggle_slot"), "Detail");
					DataBean detailFocus = detailTab.getFocus();
					
					/*
					//			get detail data
					detailForm = state.getRuntimeForm(shellForm.getSubSlot("list_slot_2"), null);
					DataBean detailFocus = detailForm.getFocus();
					*/
					headerBioBean = (BioBean)headerFocus;
					if (detailFocus.isTempBio()) {
						QBEBioBean qbe = (QBEBioBean)detailFocus;		
						qbe.set("CXADJUSTMENTKEY", headerBioBean.get("CXADJUSTMENTKEY").toString());
						qbe.set("STORERKEY", headerBioBean.get("STORERKEY").toString());
					    uowb.getNewBio(qbe);
					} 		    
				}

				
		uowb.saveUOW(true);
		}//AW 07/06/2009 Machine: 1949820 SDIS: SCM-00000-04527
		catch(UnitOfWorkException e){
			e.printStackTrace();
			Throwable nested = ((UnitOfWorkException) e).getDeepestNestedException();
			if(nested instanceof ServiceObjectException)
			{
				String reasonCode = nested.getMessage();
				throwUserException(e, reasonCode, null);
			}
			else
			{
				throwUserException(e, "ERROR_SAVING_BIO", null);
			}

		}//AW 07/06/2009 Machine: 1949820 SDIS: SCM-00000-04527 end
		catch(EpiException e){	
			e.printStackTrace();
			
			_log.debug("LOG_DEBUG_EXTENSION_AdjustmentSave", e.getErrorMessage(), SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION_AdjustmentSave", e.getErrorName(), SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION_AdjustmentSave", e.getFullErrorName(), SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION_AdjustmentSave", e.getStackTraceAsString(), SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION_AdjustmentSave", e.toString(), SuggestedCategory.NONE);
			
			throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
		}

		uowb.clearState();
		result.setFocus(headerBioBean);
	    
		return RET_CONTINUE;
	}
/*
	private QBEBioBean populateSerial(UnitOfWorkBean uowb, BioBean serialTmpBio ) throws DataBeanException{
		QBEBioBean newSerial = uowb.getQBEBioWithDefaults("wm_adjustmentdetailserial");
		
		
		newSerial.set("adjustmentkey", serialTmpBio.get("adjustmentkey") );
		newSerial.set("adjustmentlinenumber", serialTmpBio.get("adjustmentlinenumber"));
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
	
	private void buildAdjustmentDetailSerials(UnitOfWorkBean uowb, String adjustmentKey, String adjustmentLineNumber){
		AdjustmentDetailSerialTmpDAO adjDetailSerialTmpDAO = new AdjustmentDetailSerialTmpDAO();
		
		List<AdjustmentDetailSerialTmpDTO> list = adjDetailSerialTmpDAO.findAdjustmentDetailSerialTmp(adjustmentKey, adjustmentLineNumber);
		
		
		for(AdjustmentDetailSerialTmpDTO adjDetailSerialTmp : list){
			String serialnumber =  adjDetailSerialTmp.getSerialnumber();
			
			if(Double.parseDouble(adjDetailSerialTmp.getQty()) > 0){
				AdjustmentDetailSerialDAO adjDetailSerialDAO = new AdjustmentDetailSerialDAO();
				AdjustmentDetailSerialDTO adjDetailSerial = adjDetailSerialDAO.findAdjustmentDetailSerialTmp(adjustmentKey, adjustmentLineNumber, serialnumber);
				
				if(adjDetailSerial==null){
					//insert into adjustDetailSerial
					adjDetailSerial.setAdjustmentkey(adjDetailSerialTmp.getAdjustmentkey());
					adjDetailSerial.setAdjustmentlinenumber(adjDetailSerialTmp.getAdjustmentlinenumber());
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
				AdjustmentDetailSerialDTO adjDetailSerial = adjDetailSerialDAO.findAdjustmentDetailSerialTmp(adjustmentKey, adjustmentLineNumber, serialnumber);

				if(adjDetailSerial!=null){
					//delete
				}
			}
		}
	}
*/	
}
