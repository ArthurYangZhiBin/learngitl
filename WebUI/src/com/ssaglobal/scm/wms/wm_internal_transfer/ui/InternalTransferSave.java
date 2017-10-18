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
package com.ssaglobal.scm.wms.wm_internal_transfer.ui;

//Import 3rd party packages and classes

//Import Epiphany packages and classes
import java.util.ArrayList;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.wm_table_validation.ccf.NumericValidationCCF;


public class InternalTransferSave extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(InternalTransferSave.class);
	protected static String FROM_STORER = "FROMSTORERKEY";
	protected static String TO_STORER = "TOSTORERKEY";
	protected static String TRANSFER_KEY = "TRANSFERKEY";
	
	protected DataBean detailFocus;
	protected String fromStorer = "";
	protected String toStorer = "";
	protected String key = "";
	protected String fromItem = "";
	
	protected Double grossWgt1 = new Double(0);//		 WM 9 3PL Enhancements - Catch weights -Phani

	protected Double netWgt1 = new Double(0);//		 WM 9 3PL Enhancements - Catch weights -Phani

	protected Double tareWgt1 = new Double(0);//		 WM 9 3PL Enhancements - Catch weights -Phani

	protected double gross = 0; //		 WM 9 3PL Enhancements - Catch weights -Phani

	protected  double net = 0;//		 WM 9 3PL Enhancements - Catch weights -Phani

	protected double tare = 0;//		 WM 9 3PL Enhancements - Catch weights -Phani

	protected double sum = 0;//		 WM 9 3PL Enhancements - Catch weights -Phani

	protected DataBean detailXFocus;

	public InternalTransferSave(){
		_log.info("EXP_1","HeaderDetailSave Instantiated!!!",  SuggestedCategory.NONE);
	}

	protected int execute(ActionContext context, ActionResult result) throws UserException, EpiException{
		//Get user entered criteria
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();

		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);

		//get header data
	    SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		if(headerForm.isListForm()){
	    	throw new UserException("List_Save_Error", new Object[]{});
		}
		DataBean headerFocus = headerForm.getFocus();

		//get detail data
		SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
		RuntimeFormInterface detailForm = state
				.getRuntimeForm(detailSlot, null);

		// WM 9 3PL Enhancements - Catch weights - START
		/*
		 * This modification has been done as part of 3 PL wm913 - CatchWgt's
		 * tracking for Internal Transfer. Phani S - Dec 04 2009. This is for checking weights
		 * equilibrium before saving Internal Transfer Detail Record.
		 */

		if(headerFocus.isTempBio()){

		detailXFocus = detailForm.getFocus();
	    if(detailXFocus !=null)
	    {
		  grossWgt1 = Double.parseDouble(detailXFocus.getValue("GROSSWGT").toString());
		  netWgt1 =   Double.parseDouble(detailXFocus.getValue("NETWGT").toString());
		  tareWgt1 =  Double.parseDouble(detailXFocus.getValue("TAREWGT").toString());
		}
		}
		else{
			SlotInterface toggleXSlot = detailForm.getSubSlot("wm_internal_transfer_detail_toggle_slot");
	        RuntimeFormInterface detailTab = state.getRuntimeForm(toggleXSlot,"Detail");
	        detailXFocus = detailTab.getFocus();
	    if (detailXFocus != null && !(detailXFocus.isTempBio())) {
	          grossWgt1 = Double.parseDouble(detailXFocus.getValue("GROSSWGT").toString());
	  		  netWgt1 =   Double.parseDouble(detailXFocus.getValue("NETWGT").toString());
	  		  tareWgt1 =  Double.parseDouble(detailXFocus.getValue("TAREWGT").toString());
	   	}
		}

		gross = NumericValidationCCF.parseNumber(grossWgt1.toString());
		net = NumericValidationCCF.parseNumber(netWgt1.toString());
		tare = NumericValidationCCF.parseNumber(tareWgt1.toString());

		if ((gross+"").toString().equals("NaN")) {
			gross = 0.00;
		}

		if ((net+"").toString().equals("NaN")) {
			net = 0.00;
		}

		if ((tare+"").toString().equals("NaN")) {
			tare = 0.00;
		}

		sum = Math.round(net) + Math.round(tare);//Math.round(net + tare);
		System.out.println("CHECKCHECK gross=" + gross + " net=" + Math.round(net)
				+ " tare=" + Math.round(tare) + "     sum=" + sum);

		try {
			if (gross != sum) {
				throw new UserException("WMEXP_EQUILIBRIUM_MSG",
						new Object[] {});
			}
		} catch (Exception e) {
			throw new UserException("WMEXP_EQUILIBRIUM_MSG", new Object[] {});
		}

	   /* Ends  Phani S*/
		// WM 9 3PL Enhancements - Catch weights - END



		BioBean headerBioBean = null;
		if(headerFocus.isTempBio()){
			//it is for insert header
			headerFocus = (QBEBioBean)headerFocus;
			headerBioBean = uow.getNewBio((QBEBioBean)headerFocus);
			fromStorer = headerFocus.getValue(FROM_STORER).toString();
			toStorer = headerFocus.getValue(TO_STORER).toString();
			key = headerFocus.getValue(TRANSFER_KEY).toString();
			detailFocus = detailForm.getFocus();
			if(detailFocus != null){
				//insert detail
				saveDetail(context, headerBioBean);
			}
		}else{
			//it is for update header
			headerBioBean = (BioBean)headerFocus;
			fromStorer = headerBioBean.getValue(FROM_STORER).toString();
			toStorer = headerBioBean.getValue(TO_STORER).toString();
			key = headerBioBean.getValue(TRANSFER_KEY).toString();
			headerBioBean.set(FROM_STORER, fromStorer);
			headerBioBean.set(TO_STORER, toStorer);
			SlotInterface toggleSlot = detailForm.getSubSlot("wm_internal_transfer_detail_toggle_slot");
			RuntimeFormInterface detailTab = state.getRuntimeForm(toggleSlot, "Detail");
			detailFocus = detailTab.getFocus();
			if(detailFocus != null && detailFocus.isTempBio()){
				//insert detail
				saveDetail(context, headerBioBean);
			}
		}
		uow.saveUOW(true);
		uow.clearState();
		context.getServiceManager().getUserContext().remove("DetailObj");
		result.setFocus(headerForm.getFocus());
		return RET_CONTINUE;
	}
	// WM 9 3PL Enhancements - Catch weights - START
	/*
	 * This modification has been done as part of 3 PL wm913 - CatchWgt's
	 * tracking for Internal Transfer. Phani S - Dec 04 2009. This is for saving
	 * Internal Transfer Detail Record.
	 */
	private void saveDetail(ActionContext context, BioBean headerBioBean) {
		headerBioBean.addBioCollectionLink("INTERNALTRANSFERDETAIL",
				(QBEBioBean) detailFocus);
		grossWgt1 = Double.parseDouble(detailFocus.getValue("GROSSWGT")
				.toString());// WM 9 3PL Enhancements - Catch weights
		netWgt1 = Double
				.parseDouble(detailFocus.getValue("NETWGT").toString());// WM 9 3PL Enhancements - Catch weights
		tareWgt1 = Double.parseDouble(detailFocus.getValue("TAREWGT")
				.toString());// WM 9 3PL Enhancements - Catch weights
	
		detailFocus.setValue(FROM_STORER, fromStorer);
		detailFocus.setValue(TO_STORER, toStorer);
		detailFocus.setValue("GROSSWGT", grossWgt1);// WM 9 3PL Enhancements - Catch weights
		detailFocus.setValue("NETWGT", netWgt1);// WM 9 3PL Enhancements - Catch weights
		detailFocus.setValue("TAREWGT", tareWgt1);		// WM 9 3PL Enhancements - Catch weights
		detailFocus.setValue(TRANSFER_KEY, key);
		context.getServiceManager().getUserContext().put("IntTransfer", key);
	}
	// WM 9 3PL Enhancements - Catch weights - END
}