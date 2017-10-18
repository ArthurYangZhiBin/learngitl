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
package com.ssaglobal.scm.wms.wm_setup_lottableValidation.ui;

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.error.UnitOfWorkException;
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

public class LottableValidationSave extends ActionExtensionBase{
    protected static ILoggerCategory _log = LoggerFactory.getInstance(LottableValidationSave.class);

    public LottableValidationSave() 
    { 
        _log.info("EXP_1","LottableValidationSave!!!",  SuggestedCategory.NONE);
    }
    
    protected int execute(ActionContext context, ActionResult result) throws UserException, EpiException 
    {	
    	_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Executing LottableValidationSave",100L);

		StateInterface state = context.getState();
		 
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		 
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		 
		//get header data
	    SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		
		//Food Enhancements - 3PL - Lot Generation - Sreedhar Kethireddy  - Nov-25-2010 - Starts		
		for(int mask=1;mask<10;mask++)
		{
			if(!(mask==4 || mask==5))
			{
				if(mask==10)
				{
					int size=headerForm.getFormWidgetByName("GENERATEMASK"+mask).getDisplayValue()==null ? 0:headerForm.getFormWidgetByName("GENERATEMASK"+mask).getDisplayValue().trim().length();
					String rule=headerForm.getFormWidgetByName("MaskGenRule"+mask).getValue()==null ? "":headerForm.getFormWidgetByName("MaskGenRule"+mask).getValue().toString();

					if(size>0)
					{
						if(rule.equalsIgnoreCase("0"))
						{
							throw new UserException("Some of the Mask rules need to be updated", new Object[1]);
						}
					}
				}
				else
				{
					int size=headerForm.getFormWidgetByName("GENERATEMASK0"+mask).getDisplayValue()==null ? 0:headerForm.getFormWidgetByName("GENERATEMASK0"+mask).getDisplayValue().trim().length();
					String rule=headerForm.getFormWidgetByName("MaskGenRule0"+mask).getValue()==null ? "":headerForm.getFormWidgetByName("MaskGenRule0"+mask).getValue().toString();

					if(size>0)
					{
						if(rule.equalsIgnoreCase("0"))
						{
							throw new UserException("Some of the Mask rules need to be updated", new Object[1]);
						}
					}
				}
			}
		}
		//Food Enhancements - 3PL - Lot Generation - Sreedhar Kethireddy  - Nov-25-2010 - End
		
		if(headerForm.isListForm())
		{
	    	String [] desc = new String[1];
	    	desc[0] = "";
	    	throw new UserException("List_Save_Error",desc);			
		}
		DataBean headerFocus = headerForm.getFocus();		

		//get detail data
		SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);		
		
		BioBean headerBioBean = null;
		if (headerFocus.isTempBio()) 
		{
			//it is for insert header
			_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Inserting header",100L);
		    headerBioBean = uow.getNewBio((QBEBioBean)headerFocus);
		    
			DataBean detailFocus = detailForm.getFocus();
			
			if(detailFocus != null)
			{
				_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","New header - Inserting detail",100L);
				detailFocus = (QBEBioBean)detailFocus;
			    detailFocus.setValue("SEQ", detailFocus.getValue("SEQ"));
			    for ( int idx = 1; idx <= 12; ++idx)
			    {
			    	String sIdx = Integer.toString(idx);
			    	if ( idx < 10 )
			    	{
			    		sIdx = "0"+sIdx;
			    	}
			    	String tempVal = (String)detailFocus.getValue("LOTTABLE"+sIdx+"RECEIPTVALIDATION");		
				    String newVal = tempVal.equals("1") ? "nspLV003" : "NONE";
				    _log.debug("LOG_SYSTEM_OUT","Setting LOTTABLE"+sIdx+"RECEIPTVALIDATION = " +newVal,100L);
				    detailFocus.setValue("LOTTABLE"+sIdx+"RECEIPTVALIDATION", newVal );
			    }
			    headerBioBean.addBioCollectionLink("LOTTABLEVALIDATIONDETAIL", (QBEBioBean)detailFocus);
			}
		} 
		else 
		{
			//it is for update header
			_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Updating detail",100L);
		    headerBioBean = (BioBean)headerFocus;
		    SlotInterface toggleSlot = detailForm.getSubSlot("wm_setup_lottable_validation_toggle");					
		    RuntimeFormInterface detailTab = state.getRuntimeForm(toggleSlot, "wm_setup_lottable_validation_detail_tab");
			DataBean detailFocus = detailTab.getFocus();
			
				if (detailFocus != null )
				{
					if(!detailFocus.isBioCollection()){//it is not detail list form
						if ( detailFocus.isTempBio()) 
						{
							_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Update Header- Inserting detail",100L);
						    detailFocus = (QBEBioBean)detailFocus;
						    detailFocus.setValue("SEQ", detailFocus.getValue("SEQ"));
						    
						    for ( int idx = 1; idx <= 12; ++idx)
						    {
						    	String sIdx = Integer.toString(idx);
						    	if ( idx < 10 )
						    	{
						    		sIdx = "0"+sIdx;
						    	}
						    	String tempVal = (String)detailFocus.getValue("LOTTABLE"+sIdx+"RECEIPTVALIDATION");		
							    String newVal = tempVal.equals("1") ? "nspLV003" : "NONE";
							    _log.debug("LOG_SYSTEM_OUT","Setting LOTTABLE"+sIdx+"RECEIPTVALIDATION = " +newVal,100L);
							    detailFocus.setValue("LOTTABLE"+sIdx+"RECEIPTVALIDATION", newVal );
						    }
						    headerBioBean.addBioCollectionLink("LOTTABLEVALIDATIONDETAIL", (QBEBioBean)detailFocus);		//HC
						} 
						else 
						{						
						    for ( int idx = 1; idx <= 12; ++idx)
						    {
						    	String sIdx = Integer.toString(idx);
						    	if ( idx < 10 )
						    	{
						    		sIdx = "0"+sIdx;
						    	}
						    	String tempVal = (String)detailFocus.getValue("LOTTABLE"+sIdx+"RECEIPTVALIDATION");		
							    String newVal = tempVal.equals("1") ? "nspLV003" : "NONE";
							    _log.debug("LOG_SYSTEM_OUT","Setting LOTTABLE"+sIdx+"RECEIPTVALIDATION = " +newVal,100L);
							    detailFocus.setValue("LOTTABLE"+sIdx+"RECEIPTVALIDATION", newVal );
						    }
						}		    
					}
				}
			
		}
		 
		try
		{ 
			uow.saveUOW(true);
			uow.clearState();
			result.setFocus(headerBioBean);
		}
		catch (UnitOfWorkException ex)
		{			 
			_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Throwing UOW exception",100L);				
			Throwable nested = ((UnitOfWorkException) ex).getDeepestNestedException();
				
			if(nested instanceof ServiceObjectException)
			{
				String reasonCode = nested.getMessage();
				throwUserException(ex, reasonCode, null);
			}
			else
			{
				throwUserException(ex, "ERROR_DELETING_BIO", null);
			}				
		} 
		catch (EpiException ex)
		{
			_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Throwing EPIEXCEPTION",100L);
			throwUserException(ex, "ERROR_DELETING_BIO", null);
		}
		
		_log.debug("LOG_DEBUG_EXTENSION_LOTTABLEVALIDATION","Exiting LottableValidationSave",100L);
		return RET_CONTINUE;
    }
}
