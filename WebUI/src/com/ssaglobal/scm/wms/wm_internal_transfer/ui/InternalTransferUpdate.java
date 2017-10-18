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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;




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
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.wm_facility.FacilityNavFormFacilityWidgetOnChange;

public class InternalTransferUpdate extends ActionExtensionBase{
    protected static ILoggerCategory _log = LoggerFactory.getInstance(InternalTransferUpdate.class);
    
    public InternalTransferUpdate() { 
        _log.info("EXP_1","InternalTransferUPdate Instantiated!!!",  SuggestedCategory.NONE);
        
    }
	protected int execute(ActionContext context, ActionResult result) throws UserException, EpiException {	 		
	_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFERUPDATE","Executing InternalTransferUpdate",100L);		
		InternalTransferDetailObject obj = new InternalTransferDetailObject();
		InternalTransferDetailObject newObj = null; 
		
				StateInterface state = context.getState();				 
	
				 
				RuntimeFormInterface shellForm = state.getCurrentRuntimeForm().getParentForm(state);
				 
				//get header data
			    SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
				RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
				if(headerForm.isListForm()){
			    	String [] desc = new String[1];
			    	desc[0] = "";
			    	throw new UserException("List_Save_Error",desc);			
				}
				DataBean headerFocus = headerForm.getFocus();
				
				//get detail data
				SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
				RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
				if(!detailForm.getName().equals("wm_internal_transfer_detail_detail_view"))
				{
				
				BioBean headerBioBean = null;
				if (!headerFocus.isTempBio()) {
					//it is for update header				
					_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFERUPDATE","Updating Header",100L);
				    headerBioBean = (BioBean)headerFocus;
				    SlotInterface toggleSlot = detailForm.getSubSlot("wm_internal_transfer_detail_toggle_slot");
				    RuntimeFormInterface detailTab = state.getRuntimeForm(toggleSlot, "Detail");
				    
					DataBean detailFocus = detailTab.getFocus();
					
					
					if (detailFocus != null && !detailFocus.isTempBio()) 
					{
						_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFERUPDATE","Updating detail",100L);
					    UnitOfWorkBean uow = state.getDefaultUnitOfWork();					
					    	try{					   
					    		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFERUPDATE","Saving transfer detail",100L);
					    		uow.saveUOW(true);
					    		}
					    	catch (UnitOfWorkException e){					    
					    		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFERUPDATE","UOW Exception",100L);
					    			Throwable nested = ((UnitOfWorkException) e).getDeepestNestedException();
					    			//_log.debug("LOG_SYSTEM_OUT","\tNested " + nested.getClass().getName(),100L);
					    			//_log.debug("LOG_SYSTEM_OUT","\tMessage " + nested.getMessage(),100L);					
					    			if(nested instanceof ServiceObjectException)
					    			{
					    				String reasonCode = nested.getMessage();
					    				//replace terms like Storer and Commodity						
					    				throwUserException(e, reasonCode, null);
					    			}
					    			else
					    			{
					    				throwUserException(e, "ERROR_SAVING_BIO", null);
					    			}
					    	 }
					    	uow.clearState();
					    	result.setFocus(headerBioBean);
					}
					else
					{
						 newObj= setObjectVal(detailFocus, obj, detailTab, state);	
						 context.getServiceManager().getUserContext().put("DetailObj", newObj);	 
					}
				}
				}
				else
				{					
					_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFERUPDATE","Do Nothing. Insert will be done next",100L);
				}
//			}catch(Exception e){
//				e.printStackTrace();
//			}
				
				
				
				
			return RET_CONTINUE;

		}
	
	private InternalTransferDetailObject setObjectVal(DataBean detailFocus, InternalTransferDetailObject obj, RuntimeFormInterface detailTab, StateInterface state) {
		// TODO Auto-generated method stub
		
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFERUPDATE","Entering setObjVal",100L);
		QBEBioBean qbe = null;
		if(detailFocus.isTempBio())
			{detailFocus = (QBEBioBean)detailFocus;
			qbe = (QBEBioBean)detailFocus;
			}
		else
			detailFocus = (BioBean)detailFocus;
			
			
		
		
		final String TOSKU= "TOSKU";
		final String TOLOT= "TOLOT";
		final String TOLOC= "TOLOC";
		final String TOLPN= "TOID";
		final String TOPACK= "TOPACKKEY";
		final String TOUOM= "TOUOM";
		final String TOQTY= "TOQTY";
	
		final String FROMSKU= "FROMSKU";
		final String FROMLOT= "FROMLOT";
		final String FROMLOC= "FROMLOC";
		final String FROMLPN= "FROMID";
		final String FROMPACK= "FROMPACKKEY";
		final String FROMUOM= "FROMUOM";
		final String FROMQTY= "FROMQTY";
		
		final String LOTT1= "LOTTABLE01";
		final String LOTT2= "LOTTABLE02";
		final String LOTT3= "LOTTABLE03";
		final String LOTT6= "LOTTABLE06";
		final String LOTT7= "LOTTABLE07";
		final String LOTT8= "LOTTABLE08";
		final String LOTT9= "LOTTABLE09";
		final String LOTT10= "LOTTABLE10";
		final String LOTT4= "LOTTABLE04";
		final String LOTT5= "LOTTABLE05";
		
		
		
		//_log.debug("LOG_SYSTEM_OUT","\n\n Detail Tab: " +detailTab.getName(),100L);
		SlotInterface slot = detailTab.getSubSlot("LottableSlot");
		RuntimeFormInterface subForm = state.getRuntimeForm(slot, null);
		
		RuntimeFormWidgetInterface lott04 = subForm.getFormWidgetByName(LOTT4);
		RuntimeFormWidgetInterface lott05 = subForm.getFormWidgetByName(LOTT5);

		if (!isNull(lott04.getValue()) && detailFocus.isTempBio())
		{
			GregorianCalendar newVal = (GregorianCalendar)qbe.get(LOTT4);
			String dateLott04= "";
			_log.debug("LOG_SYSTEM_OUT",qbe.get(LOTT4).toString(),100L);
				dateLott04 = convertDate(newVal) ;
				obj.setLott04(dateLott04);
		}

		if (!isNull(lott05.getValue()) && detailFocus.isTempBio())
		{
			GregorianCalendar newVal = (GregorianCalendar)qbe.get(LOTT5);
			String dateLott05= "";
			
				dateLott05 = convertDate(newVal) ;
				obj.setLott05(dateLott05);
		}

		
		obj.setToSku((!isNull(detailFocus.getValue(TOSKU))? (detailFocus.getValue(TOSKU).toString()) : (" ")));			
		obj.setToLot(!isNull(detailFocus.getValue(TOLOT))? (detailFocus.getValue(TOLOT).toString()) : (" "));
		obj.setToLoc(!isNull(detailFocus.getValue(TOLOC))? (detailFocus.getValue(TOLOC).toString()) : (" "));
		obj.setToID(!isNull(detailFocus.getValue(TOLPN))? (detailFocus.getValue(TOLPN).toString()) : (" "));
		obj.setToPack(!isNull(detailFocus.getValue(TOPACK))? (detailFocus.getValue(TOPACK).toString()) : (" "));
		obj.setToUOM(!isNull(detailFocus.getValue(TOUOM))? (detailFocus.getValue(TOUOM).toString()) : (" "));
		obj.setToQty(!isNull(detailFocus.getValue(TOQTY))? (detailFocus.getValue(TOQTY).toString()) : (" "));

		obj.setFromSku(!isNull(detailFocus.getValue(FROMSKU))? (detailFocus.getValue(FROMSKU).toString()) : (" "));
		obj.setFromLot(!isNull(detailFocus.getValue(FROMLOT))? (detailFocus.getValue(FROMLOT).toString()) : (" "));
		obj.setFromLoc(!isNull(detailFocus.getValue(FROMLOC))? (detailFocus.getValue(FROMLOC).toString()) : (" "));
		obj.setFromID(!isNull(detailFocus.getValue(FROMLPN))? (detailFocus.getValue(FROMLPN).toString()) : (" "));
		obj.setFromPack(!isNull(detailFocus.getValue(FROMPACK))? (detailFocus.getValue(FROMPACK).toString()) : (" "));
		obj.setFromUOM(!isNull(detailFocus.getValue(FROMUOM))? (detailFocus.getValue(FROMUOM).toString()) : (" "));
		obj.setFromQty(!isNull(detailFocus.getValue(FROMQTY))? (detailFocus.getValue(FROMQTY).toString()) : (" "));
//		 WM 9 3PL Enhancements - Catch weights -START
		/*
		 * This modification has been done as part of 3 PL wm913 - CatchWgt's
		 * tracking for Internal Transfer.
		 * Phani S - Dec 04 2009.
		 * This is for updating into Internal Transfer Detail
		 */
		obj.setGrossWgt1(!isNull(detailFocus.getValue("GROSSWGT"))? (detailFocus.getValue("GROSSWGT").toString()) : (" "));
		obj.setNetWgt1(!isNull(detailFocus.getValue("NETWGT"))? (detailFocus.getValue("NETWGT").toString()) : (" "));
		obj.setTareWgt1(!isNull(detailFocus.getValue("TAREWGT"))? (detailFocus.getValue("TAREWGT").toString()) : (" "));
//		 WM 9 3PL Enhancements - Catch weights -END
		
		obj.setLott01(!isNull(detailFocus.getValue(LOTT1))? (detailFocus.getValue(LOTT1).toString()) : (" "));
		obj.setLott02(!isNull(detailFocus.getValue(LOTT2))? (detailFocus.getValue(LOTT2).toString()) : (" "));
		obj.setLott03(!isNull(detailFocus.getValue(LOTT3))? (detailFocus.getValue(LOTT3).toString()) : (" "));
		//obj.setLott04(!isNull(detailFocus.getValue(LOTT4))? (detailFocus.getValue(LOTT4).toString()) : null);
		//obj.setLott05(!isNull(detailFocus.getValue(LOTT5))? (detailFocus.getValue(LOTT5).toString()) : null);
		obj.setLott06(!isNull(detailFocus.getValue(LOTT6))? (detailFocus.getValue(LOTT6).toString()) : (" "));
		obj.setLott07(!isNull(detailFocus.getValue(LOTT7))? (detailFocus.getValue(LOTT7).toString()) : (" "));
		obj.setLott08(!isNull(detailFocus.getValue(LOTT8))? (detailFocus.getValue(LOTT8).toString()) : (" "));
		obj.setLott09(!isNull(detailFocus.getValue(LOTT9))? (detailFocus.getValue(LOTT9).toString()) : (" "));
		obj.setLott10(!isNull(detailFocus.getValue(LOTT10))? (detailFocus.getValue(LOTT10).toString()) : (" "));
		
		obj.setlineNum(!isNull(detailFocus.getValue("TRANSFERLINENUMBER"))? (detailFocus.getValue("TRANSFERLINENUMBER").toString()) : (" "));
		
		
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFERUPDATE","Leaving setObjVal",100L);
		return obj;
	}
	private String convertDate(GregorianCalendar newVal) {
		// TODO Auto-generated method stub
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFERUPDATE","Entering convertDate",100L);
		int month = newVal.get(GregorianCalendar.MONTH) +1;
		int year = newVal.get(GregorianCalendar.YEAR);
		int day = newVal.get(GregorianCalendar.DAY_OF_MONTH) ;
		int hour = newVal.get(Calendar.HOUR_OF_DAY);
		int min = newVal.get(Calendar.MINUTE);
		
		String date = month + "/" +day +"/" +year +" " +hour +":" +min;
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFERUPDATE","Date Value: " +date,100L);		

		return date;
	}
	private boolean isNull(Object obj)
	{
		if (obj == null)
			return true;
		else if (obj.toString().matches("\\s*"))
			return true;
		else
			return false;
	}
	
	
		}