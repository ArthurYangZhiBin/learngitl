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

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.QueryHelper;
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
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;


public class CXAdjustmentReset extends SaveAction{
    protected static ILoggerCategory _log = LoggerFactory.getInstance(CXAdjustmentReset.class);

    public CXAdjustmentReset() { 
        _log.debug("EXP_1","CXAdjustmentReset Instantiated!!!",  SuggestedCategory.NONE);
    }
    
	protected int execute(ActionContext context, ActionResult result) throws UserException {	
		StateInterface state = context.getState();
		 
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		 
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		 
		//get header data
		RuntimeFormInterface headerForm = state.getRuntimeForm(shellForm.getSubSlot("list_slot_1"), null);
		DataBean headerFocus = headerForm.getFocus();
		String storerKey=null;
		String cxadjKey=null;
		String type=null;
		
		BioCollectionBean listCollection = null;
		BioBean headerBioBean = null;
		QueryHelper qh=null;
		
		try{
			if (headerFocus.isTempBio()) 
			{//it is for insert header
					headerBioBean = uowb.getNewBio((QBEBioBean)headerFocus);
					storerKey=(String)headerForm.getFormWidgetByName("STORERKEY").getValue();
					cxadjKey=(String)headerForm.getFormWidgetByName("CXADJUSTMENTKEY").getValue();
					
					
					type=(String)headerForm.getFormWidgetByName("TYPE").getValue();
					/*
					if (headerForm.getFormWidgetByName("CONSIGNEEKEY")!=null)
						consigneekey=(String)headerForm.getFormWidgetByName("CONSIGNEEKEY").getValue();
					
					if (headerForm.getFormWidgetByName("CARRIERCODE")!=null)
						carriercode=(String)headerForm.getFormWidgetByName("CARRIERCODE").getValue();
					  
					if (headerForm.getFormWidgetByName("NOTES")!=null)
						notes=(String)headerForm.getFormWidgetByName("NOTES").getValue();
					
					if (headerForm.getFormWidgetByName("ENTEREDDATE")!=null)
					{
						enteredDate=QueryHelper.getDateString((java.util.GregorianCalendar)headerForm.getFormWidgetByName("ENTEREDDATE").getValue());
					}	
					
					if (headerForm.getFormWidgetByName("ACTUALDATE")!=null)
					{
						actualDate=QueryHelper.getDateString((java.util.GregorianCalendar)headerForm.getFormWidgetByName("ACTUALDATE").getValue());
					}	
					*/
					
					 
					/*
					DataBean detailFocus = detailForm.getFocus();
					if(detailFocus.isTempBio())
					{
						QBEBioBean detailBioBean = (QBEBioBean)detailFocus;
						detailBioBean.set("CXADJUSTMENTKEY", (String)headerForm.getFormWidgetByName("CXADJUSTMENTKEY").getValue());
						detailBioBean.set("STORERKEY", (String)headerForm.getFormWidgetByName("STORERKEY").getValue());
						detailBioBean.set("CARTONIZATIONKEY", "90002");
						detailBioBean.set("QTY_IN", "2");
						detailBioBean.set("QTY_OUT", "2");
						detailBioBean.set("QUALITY_IN","A");
						detailBioBean.set("QUALITY_OUT","B");
						headerBioBean.addBioCollectionLink("ADJUSTMENTDETAIL", detailBioBean);
					}
					*/
			} 
			else
			{
				headerBioBean = (BioBean)headerFocus;
				storerKey=headerBioBean.get("STORERKEY").toString();				
				cxadjKey=headerBioBean.get("CXADJUSTMENTKEY").toString();
				
				
				type=headerBioBean.get("TYPE").toString();
				/*
				if (headerBioBean.get("CONSIGNEEKEY")!=null)
					consigneekey=headerBioBean.get("CONSIGNEEKEY").toString();
				
				if (headerBioBean.get("CARRIERCODE")!=null)
					carriercode=headerBioBean.get("CARRIERCODE").toString();
				 
				if (headerBioBean.get("NOTES")!=null)
					notes=headerBioBean.get("NOTES").toString();
				
				if (headerBioBean.get("ENTEREDDATE")!=null)
					enteredDate=QueryHelper.getDateString((java.util.GregorianCalendar)headerBioBean.get("ENTEREDDATE"));
				
				if (headerBioBean.get("ACTUALDATE")!=null)
					actualDate=QueryHelper.getDateString((java.util.GregorianCalendar)headerBioBean.get("ACTUALDATE"));
				*/	
			}
				
		//uowb.saveUOW(true);
			
			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
			Array parms = new Array(); 
			parms.add(new TextData(cxadjKey));
			parms.add(new TextData(storerKey));
			parms.add(new TextData(type));
			actionProperties.setProcedureParameters(parms);
			actionProperties.setProcedureName("NSPCXAdjustmentDetailReset");
			try {
				WmsWebuiActionsImpl.doAction(actionProperties);
			} catch (Exception e) {
				e.getMessage();
				UserException UsrExcp = new UserException(e.getMessage(), new Object[]{});
	 	   		throw UsrExcp;
			}
			
			
	    	   String sQueryString = "(cxadjustment.CXADJUSTMENTKEY = '" + cxadjKey + "')";    	   
	    	   Query BioQuery = new Query("cxadjustment",sQueryString,null);
	    	   UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();	    	   
	           listCollection = uow.getBioCollectionBean(BioQuery);
	           
	           headerBioBean=(BioBean)listCollection.elementAt(0);
	           

			
		
		}
		catch(UnitOfWorkException e){
			e.printStackTrace();
			_log.error("LOG_DEBUG_EXTENSION_AdjustmentSave", e.getStackTraceAsString(), SuggestedCategory.NONE);
			_log.error("LOG_DEBUG_EXTENSION_AdjustmentSave", "IN UnitOfWorkException", SuggestedCategory.NONE);
			Throwable nested = ((UnitOfWorkException) e).findDeepestNestedException();
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
}
