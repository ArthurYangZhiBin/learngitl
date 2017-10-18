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
package com.ssaglobal.scm.wms.wm_order_seq_strategy.ui;

import com.epiphany.shr.data.bio.BioAttributeTypes;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_invoice_processing.ui.EnableActionButtonOnRunBillling;

public class PreSaveValidateOrderSeqStrategy extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PreSaveValidateOrderSeqStrategy.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException
	{
	  	 _log.debug("LOG_DEBUG_EXTENSION_ORDER_SEQ_STRATEGY","Executing PreSaveValidateOrderSeqStrategy",100L);

		StateInterface state = context.getState();	
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);		
	    SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);		
		DataBean detailFocus= detailForm.getFocus();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();		
				
		RuntimeFormInterface subForm1 = state.getRuntimeForm(detailForm.getSubSlot("FORMSLOT_PG1"), "wm_order_seq_strategy_pg1"  );
		RuntimeFormInterface subForm2 = state.getRuntimeForm(detailForm.getSubSlot("FORMSLOT_PG2"), "wm_order_seq_strategy_pg2"  );
		RuntimeFormInterface subForm3 = state.getRuntimeForm(detailForm.getSubSlot("FORMSLOT_PG3"), "wm_order_seq_strategy_pg3"  );

		
		//Validate Duplicate Primary Key
		if(detailFocus.isTempBio())
		{
			RuntimeFormWidgetInterface primaryKey = detailForm.getFormWidgetByName("OPPORDERSTRATEGYKEY");
			String value = primaryKey.getDisplayValue();
			
			String queryString = "opporderstrategy.OPPORDERSTRATEGYKEY='" +value+"'";
			Query query = new Query("opporderstrategy", queryString, null);
			BioCollectionBean bioCollection = uow.getBioCollectionBean(query);
			
			try{
				if(bioCollection.size() >=1)
				{
					String [] param = new String[1];
					param[0] = value;
					throw new UserException("WMEXP_DUP_PK", param);	
				}
			}
			catch(EpiDataException exp)
			{
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
		
		}
		
		//Validate Greater Than or equal to zero
		for(int i=1; i<=3; i++)
		{
			RuntimeFormInterface subForm= null;
			
			if(i==1)
			{
				subForm = subForm1;
			}
			else if(i==2)
			{
				subForm = subForm2;
			}
			else if(i==3)
			{
				subForm = subForm3;
			}
			
			String pFlag = "PRIORITYGROUP" +i +"FLAG";	
			validateWidget(pFlag, detailForm, i, subForm);
			validateRequired(pFlag,detailForm, subForm, i);
		}
		 
		
		//Validate required
		
		
		_log.debug("LOG_DEBUG_EXTENSION_ORDER_SEQ_STRATEGY","Exiting PreSaveValidateOrderSeqStrategy",100L);
		return RET_CONTINUE;
	}

	private void validateRequired(String flag, RuntimeFormInterface detailForm, RuntimeFormInterface subForm, int i) throws UserException{
		// TODO Auto-generated method stub
		_log.debug("LOG_DEBUG_EXTENSION_ORDER_SEQ_STRATEGY","Executing validateRequired",100L);
		RuntimeFormWidgetInterface priorFlag= subForm.getFormWidgetByName(flag);
		String priorFrom = "PRIORITYFROM0" +i;
		String priorTo = "PRIORITYTO0" +i;
		String orderFrom = "ORDERDATESTARTDAYS0" +i;
		String orderTo = "ORDERDATEENDDAYS0" +i;
		String shipFrom = "REQSHIPDATESTARTDAYS0" +i;
		String shipTo = "REQSHIPDATEENDDAYS0" +i;
		String orderType = "ORDERTYPEINCLUDE0" +i;
		
		RuntimeFormWidgetInterface priorStart = subForm.getFormWidgetByName(priorFrom);
		RuntimeFormWidgetInterface priorEnd = subForm.getFormWidgetByName(priorTo);
		RuntimeFormWidgetInterface orderStart = subForm.getFormWidgetByName(orderFrom);
		RuntimeFormWidgetInterface orderend = subForm.getFormWidgetByName(orderTo);
		RuntimeFormWidgetInterface shipStart = subForm.getFormWidgetByName(shipFrom);
		RuntimeFormWidgetInterface shipEnd = subForm.getFormWidgetByName(shipTo);
		RuntimeFormWidgetInterface orderT = subForm.getFormWidgetByName(orderType);
		
		

			if(priorFlag.getValue().equals("1"))
			{
				checkNull(priorStart, "Priority- From", "Priority", i);
				checkNull(priorEnd, "Priority- To", "Priority", i);
			}
			else if(priorFlag.getValue().equals("2"))
			{
				checkNull(orderStart, "From: Today +/- ", "Order Date", i);
				checkNull(orderend, "To: Today +/- ", "Order Date", i);
			}
			else if(priorFlag.getValue().equals("3"))
			{
				checkNull(shipStart, "From: Today +/- ", "Ship Date", i);
				checkNull(shipEnd, "To: Today +/- ", "Ship Date", i);
			}
			else if(priorFlag.getValue().equals("4"))
			{
				checkNull(orderT, "An Order Type ", "Order Type Inclusion", i);
			}
	}

	private void checkNull(RuntimeFormWidgetInterface widget, String label, String checkName, int i) throws UserException{
		// TODO Auto-generated method stub
		String [] param = new String[3];
		String prior= "Priority Group " +i;
		if((widget.getValue()==null)||(widget.getValue().equals("") || (widget.getValue().equals(null))))
		{
			param[0] = label;
			param[1] = checkName;
			param[2] = prior;
	 		_log.debug("LOG_SYSTEM_OUT","\n***" +label +" cannot be null",100L);
			throw new UserException("WMEXP_NULL", param);
		}
		
	}

	private void validateWidget(String flag, RuntimeFormInterface detailForm, int pgNum, RuntimeFormInterface subForm) throws UserException{
		// TODO Auto-generated method stub
		
		RuntimeFormWidgetInterface priorFlagTemp = subForm.getFormWidgetByName(flag);
		
		String orderFrom = "ORDERDATESTARTDAYS0" +pgNum;
		String orderTo = "ORDERDATEENDDAYS0" +pgNum;
		String shipFrom = "REQSHIPDATESTARTDAYS0" +pgNum;
		String shipTo = "REQSHIPDATEENDDAYS0" +pgNum;
		
		
		RuntimeFormWidgetInterface orderStart = subForm.getFormWidgetByName(orderFrom);
		RuntimeFormWidgetInterface orderend = subForm.getFormWidgetByName(orderTo);
		RuntimeFormWidgetInterface shipStart = subForm.getFormWidgetByName(shipFrom);
		RuntimeFormWidgetInterface shipEnd = subForm.getFormWidgetByName(shipTo);
		
	
		if(priorFlagTemp.getValue().equals("2"))
		{
			checkNonNegative(orderStart, "Order Date- From");
			checkNonNegative(orderend, "Order Date- To");
		}
		else if(priorFlagTemp.getValue().equals("3"))
		{
			 checkNonNegative(shipStart, "Requested Ship Date- From");
			 checkNonNegative(shipEnd, "Requested Ship Date- To");
		}
		
		
		
		
	}

	private void checkNonNegative(RuntimeFormWidgetInterface widget, String label) throws UserException{
		// TODO Auto-generated method stub
		String val = widget.getDisplayValue();
		String [] param = new String[1];
		
		
			if(widget.getAttributeType()==BioAttributeTypes.INT_TYPE)
			{
				if(val!=null)
				{
					if(val.matches("[0-9]+"))
					{
						int value = Integer.parseInt(val);
						if(value<1){
							//Negative Number	
                	 		param[0] = label;
                	 		throw new UserException("WMEXP_NEG_NUM", param);
						}
					}
					else{
						param[0] = label;
						throw new UserException("WMEXP_NON_NUMERIC", param);
						}				
				}
			}
}
}
