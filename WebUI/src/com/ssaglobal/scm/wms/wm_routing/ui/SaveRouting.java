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
package com.ssaglobal.scm.wms.wm_routing.ui;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.DataValue;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
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
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class SaveRouting extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(SaveRouting.class);
	protected int execute( ActionContext context, ActionResult result ) throws EpiException, UserException {

		 _log.debug("LOG_DEBUG_EXTENSION_ROUTING","Executing SaveRouting",100L);
		String shellSlot1 = "list_slot_1";
		String shellSlot2 = "list_slot_2";
		String toggleFormSlot = "wm_routingdetail_toggle_slot";
		String detailBiocollection = "ROUTINGDETAIL";
		String detailFormTab = "Detail";
		
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();				//get the toolbar
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);				//get the Shell form
		SlotInterface headerSlot = shellForm.getSubSlot(shellSlot1);				//Get slot1
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);	//Get form in slot1
		DataBean headerFocus = headerForm.getFocus();								//Get the header form focus
		
		SlotInterface detailSlot = shellForm.getSubSlot(shellSlot2);				//Set slot 2
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);	//Get the form at slot2
		
		RuntimeFormInterface detailTab= null;		
		if (detailForm.getName().equalsIgnoreCase(toggleFormSlot)){	//if the slot is populated by toggle form then
			SlotInterface toggleSlot = detailForm.getSubSlot(toggleFormSlot);		
			detailTab = state.getRuntimeForm(toggleSlot, "Detail");
		}
		SlotInterface tabGroupSlot = getTabGroupSlot(state);		
		DataBean CompDetailFocus = getCompDetailFocus(state, detailForm);
		DataBean detailFocus = null;
		QBEBioBean CompDetailQBEBioBean = null;
		BioBean CompDetailBioBean = null;
		
		BioBean headerBioBean = null;
		BioBean detailBioBean = null;
		Object ObjRecDetailLpn = null;
		QBEBioBean detailQBEBioBean = null; 
		RuntimeFormInterface RoutingDetail = null;
		
		
	if (headerFocus.isTempBio()) 
	{//it is for insert header
			headerBioBean = uow.getNewBio((QBEBioBean)headerFocus);
			detailFocus = detailForm.getFocus();
			
			//String headerID= headerForm.getFormWidgetByName("WORKORDERDEFNID").getDisplayValue();
			//_log.debug("LOG_SYSTEM_OUT","\n\n**** WORKORDER VAL: " +headerID,100L);
			//validateDetail(detailFocus, headerID);
			
			detailQBEBioBean = (QBEBioBean)detailFocus;			
			headerBioBean.addBioCollectionLink(detailBiocollection, detailQBEBioBean);
			//_log.debug("LOG_SYSTEM_OUT","\n### After validation comp focus type: " ,100L);
			if( (CompDetailFocus instanceof QBEBioBean))
			{
				CompDetailQBEBioBean = (QBEBioBean) CompDetailFocus;
				validateCompDetail(CompDetailFocus, detailFocus);
				detailQBEBioBean.addBioCollectionLink("ROUTINGCOMPONENTDETAIL", CompDetailQBEBioBean);
			
			}
	}
		
		
	else {//it is for update header
		headerBioBean = (BioBean)headerFocus;
		if(!(detailTab.getName().equals("Blank"))){
			detailFocus = detailTab.getFocus();
			SlotInterface detailTabGrpSlot = detailTab.getSubSlot("tbgrp_slot");
			RoutingDetail = state.getRuntimeForm(detailTabGrpSlot, "tab 0");

			//validate routing detail notes
			RuntimeFormWidgetInterface widgetNotes = RoutingDetail.getFormWidgetByName("NOTES");
			if(widgetNotes != null){
				Object widgetData = widgetNotes.getValue();
				if(widgetData != null && !widgetData.toString().equalsIgnoreCase(""))
				{
					if(widgetData.toString().indexOf("'") != -1)
					{
						String args[] = new String[1]; 						
						args[0] = "NOTES";
						throw new UserException("WMEXP_NO_QUOTES_ALLOWED",args);
					}
				}
			}
			
			String headerID= headerBioBean.getValue("WORKORDERDEFNID").toString();
			
			if (detailFocus.isTempBio()) 
			   {

					validateDetail(detailFocus, headerID);
					detailQBEBioBean = (QBEBioBean)detailFocus;
					//validateDetail(detailQBEBioBean);
					headerBioBean.addBioCollectionLink(detailBiocollection, detailQBEBioBean);
				
					if( (CompDetailFocus instanceof QBEBioBean))
					{
					CompDetailQBEBioBean = (QBEBioBean) CompDetailFocus;
					validateCompDetail(CompDetailFocus, detailFocus);
					detailQBEBioBean.addBioCollectionLink("ROUTINGCOMPONENTDETAIL", CompDetailQBEBioBean);
					}	

			   } 
			else {
					detailBioBean = (BioBean)detailFocus;
					validateDetail(detailFocus, headerID);
					if( (CompDetailFocus instanceof QBEBioBean))
					{
						CompDetailQBEBioBean = (QBEBioBean) CompDetailFocus;
						validateCompDetail(CompDetailFocus, detailFocus);
						detailBioBean.addBioCollectionLink("ROUTINGCOMPONENTDETAIL", CompDetailQBEBioBean);
					
					}
					else if (CompDetailFocus instanceof BioBean){
						detailBioBean = (BioBean)detailFocus;
						CompDetailBioBean = (BioBean) CompDetailFocus;
						validateCompDetail(CompDetailFocus, detailFocus);
					}
					else
					{	
						validateCompDetail(CompDetailFocus, detailFocus);						
					}

				}//else - update detail
		}
		 
	}	
		
		
		uow.saveUOW(true);
		uow.clearState();
		result.setFocus(headerBioBean);
		
		 _log.debug("LOG_DEBUG_EXTENSION_ROUTING","Exiting SaveRouting",100L);
		return RET_CONTINUE;
		
	}



	private void validateCompDetail(DataBean compDetailFocus, DataBean detailFocus) throws UserException, DPException{
		// TODO Auto-generated method stub
		 _log.debug("LOG_DEBUG_EXTENSION_ROUTING","Executing validateCompDetail",100L);
		/*
		if(compDetailFocus instanceof QBEBioBean)
		{compDetailFocus =(QBEBioBean)compDetailFocus;}
		else if(compDetailFocus instanceof BioBean)
		{compDetailFocus = (BioBean)compDetailFocus;}
		*/
	
		
		if(detailFocus instanceof QBEBioBean)
		{detailFocus =(QBEBioBean)detailFocus;}
		else if(detailFocus instanceof BioBean)
		{detailFocus = (BioBean)detailFocus;}
		
		String opClassID = detailFocus.getValue("OPCLASSID").toString();
		String opDefnID = detailFocus.getValue("OPERATIONDEFNID").toString();
		//
	       {   	   
		   		String query = "SELECT OPCLASSTYPE FROM OPERATIONCLASS " 
				+ "WHERE (OPCLASSID = '" + opClassID + "') ";
		        
		        EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);

				DataValue dv = results.getAttribValue(1);
				
		      	//Duplicate record
		    		if (dv.getAsString().equals("NOCHG"))
		    		{	
		    			if(compDetailFocus == null)
		    			{
		    		   		String queryComp = "SELECT * FROM OPSSKUDETDEFN " 
		    					+ "WHERE (OPERATIONDEFNID = '" + opDefnID + "') ";
		    			        
		    			        EXEDataObject resultComp = WmsWebuiValidationSelectImpl.select(queryComp);
		    			        if(resultComp.getRowCount() >0)
		    			        {
		    			        	throw new UserException("WMEXP_CHK_COMP", new Object[1]);	
		    			        }
		    			}
		    			else
		    			{
							throw new UserException("WMEXP_CHK_OPTYPE", new Object[1]);		
		    			}
			
		    		}
		       }
		
	       _log.debug("LOG_DEBUG_EXTENSION_ROUTING","Exiting validateCompDetail",100L);
	}

	private void validateDetail(DataBean detailBean, String headerID) throws UserException, DPException{
		// TODO Auto-generated method stub
		_log.debug("LOG_DEBUG_EXTENSION_ROUTING","Executing validateDetail",100L);
		
		
		if(detailBean instanceof QBEBioBean)
		{detailBean = (QBEBioBean)detailBean;}
		else if(detailBean instanceof BioBean)
		{
			detailBean = (BioBean)detailBean;
		}
		
		
		//Object workorderID = detailBean.getValue("WORKORDERDEFNID");
		Object isDynamic = detailBean.getValue("ISDYNAMIC");
		String dynamicCheck = null;
		
		if( isDynamic != null && !isDynamic.toString().equalsIgnoreCase("")){
			dynamicCheck = isDynamic.toString(); 
		}
		else{dynamicCheck = "0";}
				
		
		if(dynamicCheck.equals("1"))
	       {   	   
	   		String query = "SELECT * " + "FROM ROUTEOPSDEFN " 
			+ "WHERE (WORKORDERDEFNID = '" + headerID + "') "
			+ "AND (ISDYNAMIC = '1')";
	        
	        EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
	        
	      	//Duplicate record
	    		if (results.getRowCount() >= 1)
	    		{	
				throw new UserException("WMEXP_ISDYNAMIC_CHECK", new Object[1]);			
	    		}
	       }
		_log.debug("LOG_DEBUG_EXTENSION_ROUTING","Exiting validateDetail",100L);
	}

	private DataBean getCompDetailFocus(StateInterface state, RuntimeFormInterface detailForm) {
		// TODO Auto-generated method stub
		_log.debug("LOG_DEBUG_EXTENSION_ROUTING","Executing getCompDetailFocus",100L);
		DataBean focus = null;
		try
		{
			if (detailForm.getName().equalsIgnoreCase("wm_routingdetail_toggle_slot")){
				SlotInterface RoutingDetailSlot = detailForm.getSubSlot("wm_routingdetail_toggle_slot");
				RuntimeFormInterface tabgrpForm = state.getRuntimeForm(RoutingDetailSlot, "Detail");
				SlotInterface tabGrpSlot = tabgrpForm.getSubSlot("tbgrp_slot");
				RuntimeFormInterface CompDetailForm = state.getRuntimeForm(tabGrpSlot, "tab 1");
				SlotInterface CompDetailToggleSlot = CompDetailForm.getSubSlot("wm_routingcomponentdetail_toggle_slot");
				RuntimeFormInterface CompDetailDetailForm = state.getRuntimeForm(CompDetailToggleSlot, "Detail");			

				DataBean CompDetailDetailFocus = CompDetailDetailForm.getFocus();
				focus= CompDetailDetailFocus;
				//return CompDetailDetailFocus;
				}
			else if(detailForm.getName().equalsIgnoreCase("wms_tbgrp_shell"))
			{
				
				SlotInterface tabGrpSlot = detailForm.getSubSlot("tbgrp_slot");
				RuntimeFormInterface CompDetailForm = state.getRuntimeForm(tabGrpSlot, "tab 1");				
				SlotInterface CompDetailToggleSlot = CompDetailForm.getSubSlot("wm_routingcomponentdetail_toggle_slot");
				RuntimeFormInterface CompDetailDetailForm = state.getRuntimeForm(CompDetailToggleSlot, "Detail");
				
				DataBean DetailDetailFocus = CompDetailDetailForm.getFocus();
				focus= DetailDetailFocus;
				//return focus;
					//return null;
				}
		} catch (NullPointerException e)
		{
			//e.printStackTrace();
			return null;
		}
		_log.debug("LOG_DEBUG_EXTENSION_ROUTING","Exiting getCompDetailFocus",100L);
		return focus;
	}

	private SlotInterface getTabGroupSlot(StateInterface state) {
		// TODO Auto-generated method stub
		_log.debug("LOG_DEBUG_EXTENSION_ROUTING","Executing getTabGroupSlot",100L);
		//Common 
		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();	
		RuntimeFormInterface shellForm = shellToolbar.getParentForm(state);
		SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
		SlotInterface tabGroupSlot = detailForm.getSubSlot("tbgrp_slot");
		_log.debug("LOG_DEBUG_EXTENSION_ROUTING","Exiting getTabGroupSlot",100L);
		return tabGroupSlot;
	}
	
	
	
}


