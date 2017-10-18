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
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.GenericException;
import com.agileitp.forte.framework.TextData;
import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.metadata.objects.FormSlot;
import com.epiphany.shr.metadata.objects.ScreenSlot;
import com.epiphany.shr.metadata.objects.TabIdentifier;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.SsaException;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.exeprocessmanager.TransactionServiceSORemote;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.EJBRemote;
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

public class UnallocatePickLinesAction extends ActionExtensionBase {

	protected static final String internalCaller = "internalCall";
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
	protected static ILoggerCategory _log = LoggerFactory.getInstance(UnallocatePickLinesAction.class);

	protected int execute( ActionContext context, ActionResult result ) throws EpiException {	 
	   String didCloseModal = (String)getParameter("didCloseModal");
	  StateInterface state = context.getState();
	  String wmWhseID =  null;		 
	  EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
 	  String db_connection = userContext.get(SetIntoHttpSessionAction.DB_CONNECTION).toString();
 	  StringTokenizer dbstring = new StringTokenizer(db_connection,"_");
 	  while (dbstring.hasMoreTokens()) {
 		wmWhseID = dbstring.nextToken();
 	  }
 	  RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();	  
	  RuntimeFormInterface shellForm = toolbar.getParentForm(state);	  	 
	  SlotInterface headerSlot = shellForm.getSubSlot("slot_one");		//HC
	  RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
	  
	  if(didCloseModal.equalsIgnoreCase("true")){			
			SlotInterface compositeSlot = headerForm.getSubSlot("detail_view");		//HC
			if(compositeSlot == null){
				context.setNavigation("closeModalDialog18");
				return RET_CONTINUE;
			}
			else{
				context.setNavigation("closeModalDialog17");
			}
			RuntimeListFormInterface listForm = (RuntimeListFormInterface)state.getRuntimeForm(compositeSlot, null);
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
		                  String pickDetailKey = bio.getString("PICKDETAILKEY");                                   
		                  String sql = "DELETE FROM PICKDETAIL WHERE pickdetailkey = '"+pickDetailKey.toUpperCase()+"'";
		                  try {
		                	  TransactionServiceSORemote remote = EJBRemote.getRemote();
							  remote.delete(new TextData(wmWhseID),db_connection,new TextData(sql), null, true,internalCaller,null);
		                  } catch (Exception exc) {   			
		                  	exc.printStackTrace();
		                	EpiException x = new EpiException("EXP_1", "Unknown", exc);		                	
		                	String reasonCode = "";
		                    if (exc instanceof GenericException) {
		                        reasonCode = ((GenericException) exc).reasonCodeString();
		                    } else {
		                        reasonCode = exc.getMessage();
		                    }             
		                    _log.debug("LOG_SYSTEM_OUT","it is in action exception. EJB message as follows:\n"+reasonCode,100L);
		                    throw new UserException(reasonCode,new Object[0]);
		                	//return null;
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
			state.getRequest().getSession().removeAttribute("unallocatePicksSelectedItemsSize");
		}
		else{	
			SlotInterface compositeSlot = headerForm.getSubSlot("detail_view");		//HC
			if(compositeSlot == null)		
				return RET_CONTINUE;			
			RuntimeListFormInterface listForm = (RuntimeListFormInterface)state.getRuntimeForm(compositeSlot, null);
			ArrayList selectedItems = listForm.getAllSelectedItems(); 
			if(selectedItems == null)
				state.getRequest().getSession().setAttribute("unallocatePicksSelectedItemsSize","0");
			else
				state.getRequest().getSession().setAttribute("unallocatePicksSelectedItemsSize",selectedItems.size()+"");
		}
	  
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
