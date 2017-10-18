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


package com.ssaglobal.scm.wms.wm_order_bio.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.eai.exception.EAIError;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.metadata.objects.FormSlot;
import com.epiphany.shr.metadata.objects.ScreenSlot;
import com.epiphany.shr.metadata.objects.TabIdentifier;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class UnallocateShipmentOrdersAction extends ListSelector {


   /**
    * The code within the execute method will be run from a UIAction specified in metadata.
    * <P>
    * @param context The ActionContext for this extension
    * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
    *
    * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
    *
    * @exception EpiException
    */
   protected int execute( ActionContext context, ActionResult result ) throws EpiException {	 
	   String didCloseModal = (String)getParameter("didCloseModal");
		StateInterface state = context.getState();		 		
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();	  
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);		  		
		
		if(didCloseModal.equalsIgnoreCase("true")){			
			SlotInterface headerSlot = shellForm.getSubSlot("slot_one");		//HC
			if(headerSlot == null){
				context.setNavigation("closeModalDialog20");
				return RET_CONTINUE;
			}
			else{
				context.setNavigation("closeModalDialog19");
			}
			RuntimeListFormInterface listForm = (RuntimeListFormInterface)state.getRuntimeForm(headerSlot, null);
			ArrayList selectedItems = listForm.getAllSelectedItems(); 				
			if(selectedItems != null && selectedItems.size() > 0)
			{		  		 
				Iterator bioBeanIter = selectedItems.iterator();
				UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
				try
				{
					BioBean bio;
					for(; bioBeanIter.hasNext();){            	
		                  bio = (BioBean)bioBeanIter.next();                  
		                  BioCollection orders = bio.getBioCollection("ORDER_DETAIL");
		                  if(orders != null){
		                	  for(int i = 0; i < orders.size(); i++){
		                		  Bio subBio = orders.elementAt(i);
		                		  String orderKey = subBio.getString("ORDERKEY");
		                		  String orderLineNumber = subBio.getString("ORDERLINENUMBER");                		 
		                		  WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		                		  Array parms = new Array(); 
		                		  parms.add(new TextData(orderKey));
		                		  parms.add(new TextData(orderLineNumber));
		                		  actionProperties.setProcedureParameters(parms);
		                		  actionProperties.setProcedureName("NSPUNALLOCATEORDERS");
		                		  try {
									WmsWebuiActionsImpl.doAction(actionProperties);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
		                	  }
		                  }
		              }
					uowb.saveUOW();  					
					result.setSelectedItems(null);
					listForm.setSelectedItems(null);
					
				}
				catch(EpiException ex)
				{
					throwUserException(ex, "ERROR_EXECUTING_ACTION", null);
				}
			}
			state.getRequest().getSession().removeAttribute("unallocateShipmentOrdersSelectedItemsSize");
		}
		else{	
			SlotInterface headerSlot = shellForm.getSubSlot("slot_one");		//HC
			if(headerSlot == null)		
				return RET_CONTINUE;			
			RuntimeListFormInterface listForm = (RuntimeListFormInterface)state.getRuntimeForm(headerSlot, null);
			ArrayList selectedItems = listForm.getAllSelectedItems(); 
			if(selectedItems == null)
				state.getRequest().getSession().setAttribute("unallocateShipmentOrdersSelectedItemsSize","0");
			else
				state.getRequest().getSession().setAttribute("unallocateShipmentOrdersSelectedItemsSize",selectedItems.size()+"");			
		}
		
	   
//	  if(selectedItems != null && selectedItems.size() > 0)
//      {		  
//          Iterator bioBeanIter = selectedItems.iterator();
//          UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
//          try
//          {
//              BioBean bio;
//              for(; bioBeanIter.hasNext();){            	
//                  bio = (BioBean)bioBeanIter.next();                  
//                  BioCollection orders = bio.getBioCollection("ORDER_DETAIL");
//                  if(orders != null){
//                	  for(int i = 0; i < orders.size(); i++){
//                		  Bio subBio = orders.elementAt(i);
//                		  String orderKey = subBio.getString("ORDERKEY");
//                		  String orderLineNumber = subBio.getString("ORDERLINENUMBER");                		 
//                		  WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
//                		  Array parms = new Array(); 
//                		  parms.add(new TextData(orderKey));
//                		  parms.add(new TextData(orderLineNumber));
//                		  actionProperties.setProcedureParameters(parms);
//                		  actionProperties.setProcedureName("NSPUNALLOCATEORDERS");
//                		  try {
//							WmsWebuiActionsImpl.doAction(actionProperties);
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//                	  }
//                  }
//              }
//              uowb.saveUOW();             
//              result.setSelectedItems(null);
//              headerForm.setSelectedItems(null);
//          }
//          catch(EpiException ex)
//          {
//              throwUserException(ex, "ERROR_EXECUTING_ACTION", null);
//          }
//      }	 
      return RET_CONTINUE;
      
   }
   
   /**
    * Fires in response to a UI action event, such as when a widget is clicked or
    * a value entered in a form in a modal dialog
    * Write code here if u want this to be called when the UI Action event is fired from a modal window
    * <ul>
    * <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext} exposes information about the
    * event, including the service and the user interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
    * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes information about the results
    * of the action that has occurred, and enables your extension to modify them.</li>
    * </ul>
    */
    protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {    	
       try {
           // Add your code here to process the event
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }
}
