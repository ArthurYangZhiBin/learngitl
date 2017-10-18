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
package com.ssaglobal.scm.wms.wm_accumulated_charges.ui;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
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
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.wm_setup_billofmaterial.ui.PreSaveValidationsForSetupBillofMaterial;

public class PreSaveValidationsForAccumulatedCharges extends ActionExtensionBase{
	  protected static ILoggerCategory _log = LoggerFactory.getInstance(PreSaveValidationsForAccumulatedCharges.class);
	  
	  public PreSaveValidationsForAccumulatedCharges() { 
	      _log.info("EXP_1","PreSaveValidationsForSetupBillofMaterial!!!",  SuggestedCategory.NONE);
	  }
	  protected int execute(ActionContext context, ActionResult result) throws UserException, EpiException {
		  _log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Executing PreSaveValidationsForAccumulatedCharges",100L);

	  	final String OWNER = "STORERKEY";
	  	final String ITEM = "SKU";
	  	final String LOT = "LOT";
	  	final String CREDIT = "CREDIT";
	  	final String DEBIT = "DEBIT";
	  	final String LINETYPE = "LINETYPE";
	  	final String DESCRIPTION = "DESCRIP";
	  	boolean isAdjustment= false;
	  	
	  	StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		
	  	RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
	  	RuntimeFormInterface shellForm = toolbar.getParentForm(state);
	  	
	  	SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
	  	RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
	  	
	    DataBean focus = detailForm.getFocus();

	    if(focus.isTempBio())
	    	focus= (QBEBioBean)focus;
	    else
	    	focus = (BioBean)focus;
	  
	    
	    validateOwner(focus, uow, OWNER);
	    validateItem(focus, uow, ITEM);
	    validateItemOwnerComb(focus, uow, OWNER, ITEM);
	    validateLot(focus, uow, OWNER, ITEM, LOT);
	    validateGreaterThanZero(focus, DEBIT);
	    validateGreaterThanZero(focus, CREDIT);
	    validateDebitAndCredit(focus, DEBIT, CREDIT);	  	
	  	//checkAdjustment
	    isAdjustment = checkAdjust(focus, LINETYPE);
	    if(isAdjustment)
	    {
	    	checkRulesForAdjustment(focus, DESCRIPTION);
	    }
	  		//if Adjustment-> DESCRIP cannot be Blank

	    _log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Exiting PreSaveValidationsForAccumulatedCharges",100L);
	    return RET_CONTINUE;
	  }
	  
	 private void checkRulesForAdjustment(DataBean focus, String description) throws UserException{
		// TODO Auto-generated method stub
		 _log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Executing checkRulesForAdjustment",100L);
		Object descWidget = focus.getValue(description);
		boolean isNull = checkNull(descWidget);
		if(isNull)
		{
			//error description cannot be null
			throw new UserException("WMEXP_DESCRIP_NEEDED", new Object[1]);
		}
		else
		{
			String desc = descWidget.toString();
			if(!desc.startsWith("ADJUSTMENT"))
			{
			desc = "ADJUSTMENT" +desc;
			focus.setValue(description, desc);
			}
		}
		 _log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Exiting checkRulesForAdjustment",100L);
	}
	private boolean checkAdjust(DataBean focus, String linetype) {
		// TODO Auto-generated method stub
		 _log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Executing checkAdjust",100L);
		 boolean isNull;
		
		 Object lineTypeWidget = focus.getValue(linetype);
		 
		 isNull= checkNull(lineTypeWidget);

		 if(!isNull)
		 {
			 String lineVal = lineTypeWidget.toString();
			 if(lineVal.equals("NA") || lineVal.equals("TA"))
			 {
				 _log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Executing checkAdjust",100L);
				 return true;
			 }
		 }
		 _log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Executing checkAdjust",100L);
		return false;
	}
	private void validateGreaterThanZero(DataBean focus, String widget) throws UserException{
		// TODO Auto-generated method stub
		 _log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Executing validateGreaterThanZero",100L);
		 Object widgetObj = focus.getValue(widget);
		 if(widgetObj != null && !widgetObj.toString().equalsIgnoreCase(""))
		 {
			 double value = Double.parseDouble(widgetObj.toString());
			 if(value < 0)
			 {
				 String[] param = new String[1];
				 param[0] = widget;
				 throw new UserException("WMEXP_NONNEG" , param);
			 }

		 }
		 _log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Exiting validateGreaterThanZero",100L);
	}
	private void validateDebitAndCredit(DataBean focus, String debit, String credit) throws UserException{
		// TODO Auto-generated method stub
		_log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Executing validateDebitAndCredit",100L);
		boolean debitIsNull, creditIsNull;;
		
		 Object debitObj = focus.getValue(debit);
		 Object creditObj = focus.getValue(credit);
		
		 debitIsNull = checkIfNull(debitObj);
		 creditIsNull = checkIfNull(creditObj);
	
	
		 
		 	if(!debitIsNull && !creditIsNull)
		 	{ 
		 		String debitVal = debitObj.toString();
		 		String creditVal = creditObj.toString();
				double debitDB = Double.parseDouble(debitVal);
				double creditDB = Double.parseDouble(creditVal);
				

				 
		 		if((debitDB == 0) && (creditDB == 0))
		 		{	
		 			_log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Both Debit & Credit are 0 ",100L);
		 			String[] param = new String[2];
		 			param[0]= debit;
		 			param[1]= credit;
		 			throw new UserException("WMEXP_BOTH_CREDIT_AND_DEBIT", param);
		 		}
		 		else if((debitDB > 0) && (creditDB > 0))
		 		{
		 			_log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Both Debit & Credit have +ve vals",100L);
		 			String[] param = new String[2];
		 			param[0]= debit;
		 			param[1]= credit;
		 			throw new UserException("WMEXP_BOTH_CR_DEBIT_NOT_GREATER", param);
		 		}
		 	}
		 	else if(debitIsNull && creditIsNull)
		 	{
		 		String[] param = new String[2];
	 			param[0]= debit;
	 			param[1]= credit;
	 			throw new UserException("WMEXP_BOTH_CREDIT_AND_DEBIT", param);
		 	}
		 	else if(debitIsNull && !creditIsNull)
		 	{
		 		focus.setValue("DEBIT", "0");
		 	}
		 	else if(!debitIsNull && creditIsNull)
		 	{
		 		focus.setValue("CREDIT", "0");
		 	}
		 	_log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Exiting validateDebitAndCredit",100L);
	}
	private boolean checkIfNull(Object obj) {
		// TODO Auto-generated method stub
		if(obj == null)
			return true;
		else if(obj.toString().equalsIgnoreCase(""))
			return true;
		else if (Double.parseDouble(obj.toString()) == 0)
			return true;
		else 
			return false;

	}
	private boolean checkNull(Object obj) {
		// TODO Auto-generated method stub
		if(obj == null)
			return true;
		else if(obj.toString().equalsIgnoreCase(""))
			return true;
		else 
			return false;

	}
	private void validateLot(DataBean focus, UnitOfWorkBean uow, String owner, String item, String lot) throws EpiException{
		// TODO Auto-generated method stub
		_log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Executing validateLot",100L);
			String query = null;
			String BIO = "wm_lotxlocxid";
			Object itemVal = focus.getValue(item);
			Object storerVal = focus.getValue(owner);
			Object lotVal = focus.getValue(lot);
			
			if(itemVal != null && !itemVal.toString().equalsIgnoreCase(""))
			{
				if(storerVal != null && !storerVal.toString().equalsIgnoreCase(""))
				{
					if(lotVal != null && !lotVal.toString().equalsIgnoreCase(""))
					{
						String sku = itemVal.toString().toUpperCase();
						String storer = storerVal.toString().toUpperCase();
						String lotStr = lotVal.toString();
						
						query = BIO +".LOT='" +lotStr +"' and " +BIO + ".SKU='" +sku +"' and " +BIO +".STORERKEY='" +storer +"'";
						Query qry = new Query(BIO, query, null);
						BioCollectionBean newFocus = uow.getBioCollectionBean(qry);
						if(newFocus.size()<1){
							String[] param = new String[2];
							param[0] = storer;
							param[1] = sku;
							throw new FormException("WMEXP_INV_LOT_COMB", param);
							}
					}
				}
			}
			_log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Exiting validateLot",100L);
		}
	private void validateItemOwnerComb(DataBean focus, UnitOfWorkBean uow, String owner, String item) throws EpiException{
		// TODO Auto-generated method stub
		_log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Executing validateItemOwnerComb",100L);
			String query = null;
			String BIO = "sku";
			Object itemVal = focus.getValue(item);
			Object storerVal = focus.getValue(owner);
			
			if(itemVal != null && !itemVal.toString().equalsIgnoreCase(""))
			{
				if(storerVal != null && !storerVal.toString().equalsIgnoreCase(""))
				{
				String sku = itemVal.toString().toUpperCase();
				String storer = storerVal.toString().toUpperCase();
				query = BIO + ".SKU='" +sku +"' and " +BIO +".STORERKEY='" +storer +"'";
		
				Query qry = new Query(BIO, query, null);
				BioCollectionBean newFocus = uow.getBioCollectionBean(qry);
				if(newFocus.size()<1){
					String[] param = new String[2];
					param[0] = storer;
					param[1] = sku;
					throw new FormException("WMEXP_INVALID_OWNER_ITEM_COMB", param);
					}
				}
			}
			_log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Exiting validateItemOwnerComb",100L);	
		}
	/********************************************************************************************
	  * @param focus
	  * @param uow
	  * @param item
	  * @behaviour Validates Item
	  *******************************************************************************************/ 
	  
	  private void validateItem(DataBean focus, UnitOfWorkBean uow, String item) throws EpiException{
		// TODO Auto-generated method stub
		  _log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Executing validateItem",100L);
			String query = null;
			String BIO = "sku";
			Object itemVal = focus.getValue(item);
			
			if(itemVal != null && !itemVal.toString().equalsIgnoreCase(""))
			{
				String sku = itemVal.toString().toUpperCase();
				query = BIO + ".SKU='" +sku +"'";
				Query qry = new Query(BIO, query, null);
				BioCollectionBean newFocus = uow.getBioCollectionBean(qry);
				if(newFocus.size()<1){
					String[] param = new String[1];
					param[0] = sku;
					throw new FormException("WMEXP_INVALID_ITEM", param);
				}
				else
				{focus.setValue(item, sku);}
			}
			_log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Exiting validateItem",100L);
		}
	/************************************************************************************************
	   * 
	   * @param focus
	   * @param uow
	   * @param owner
	   * @throws UserException
	   * @behaviour Validates Owner
	   ************************************************************************************************/
	private void validateOwner(DataBean focus, UnitOfWorkBean uow, String owner) throws UserException, EpiException{
		_log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Executing validateOwner",100L);
		// TODO Auto-generated method stub
		String query = null;
		String BIO = "wm_storer";
		Object storerVal = focus.getValue(owner);
		
		if(storerVal != null && !storerVal.toString().equalsIgnoreCase(""))
		{
			String storer = storerVal.toString().toUpperCase();
			query = BIO + ".STORERKEY='" +storer +"' and " +BIO + ".TYPE ='1'";
			Query qry = new Query(BIO, query, null);
			BioCollectionBean newFocus = uow.getBioCollectionBean(qry);
			if(newFocus.size()<1){
				String[] param = new String[1];
				param[0] = storer;
				throw new FormException("WMEXP_INVALID_OWNER", param);
			}
			else
			{focus.setValue(owner, storer);}
		}
		_log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Exiting validateOwner",100L);
	}
}
