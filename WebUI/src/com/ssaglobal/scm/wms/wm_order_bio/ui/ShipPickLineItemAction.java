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

//Import 3rd party packages and classes

//Import Epiphany packages and classes
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.eai.exception.EAIError;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsDataProviderImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ShipPickLineItemAction extends com.epiphany.shr.ui.action.ActionExtensionBase {
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ShipPickLineItemAction.class);
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
		_log.debug("LOG_DEBUG_EXTENSION_SHIPPICKLINEITEM","Executing ShipPickLineItemAction",100L);		
		StateInterface state = context.getState();	
		RuntimeListFormInterface listForm = (RuntimeListFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_pick_detail_view",state);
		if(listForm == null || listForm.getSelectedItems() == null || listForm.getSelectedItems().size() == 0){
			state.getRequest().getSession().removeAttribute("didCloseInitialConfModal");
			state.getRequest().getSession().removeAttribute("didCloseBatchConfModal");
			state.getRequest().getSession().removeAttribute("didCancelBatchConfModal");
			state.getRequest().getSession().removeAttribute("SHIPPICKLINESREMAINING");			
			state.getRequest().getSession().removeAttribute("SHIPPICKLINESREMAININGCOUNT");
			String args[] = new String[0];							
			String errorMsg = getTextMessage("WMEXP_NOTHING_SELECTED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		
		Object didCloseInitialConfModalObj = state.getRequest().getSession().getAttribute("didCloseInitialConfModal");
		Object didCloseBatchConfModalObj = state.getRequest().getSession().getAttribute("didCloseBatchConfModal");
		Object didCancelBatchConfModalObj = state.getRequest().getSession().getAttribute("didCancelBatchConfModal");
		_log.debug("LOG_DEBUG_EXTENSION_SHIPPICKLINEITEM","Got Objects didCloseInitialConfModalObj:"+didCloseInitialConfModalObj+" And didCloseBatchConfModalObj:"+didCloseBatchConfModalObj+" From Session",100L);				
		state.getRequest().getSession().removeAttribute("didCloseInitialConfModal");
		state.getRequest().getSession().removeAttribute("didCloseBatchConfModal");
		state.getRequest().getSession().removeAttribute("didCancelBatchConfModal");
		String didCloseInitialConfModal = didCloseInitialConfModalObj==null?"false":didCloseInitialConfModalObj.toString();
		String didCloseBatchConfModal = didCloseBatchConfModalObj==null?"false":didCloseBatchConfModalObj.toString();
		String didCancelBatchConfModal = didCancelBatchConfModalObj==null?"false":didCancelBatchConfModalObj.toString();
		_log.debug("LOG_DEBUG_EXTENSION_SHIPPICKLINEITEM","Created Strings didCloseInitialConfModal:"+didCloseInitialConfModal+" And didCloseBatchConfModal:"+didCloseBatchConfModal,100L);		
		if(didCancelBatchConfModal.equals("true")){
			context.setNavigation("closeModalDialog29");
			return RET_CONTINUE;
		}
		if(didCloseBatchConfModal.equals("false") && didCloseInitialConfModal.equals("true")){
			//listForm = (RuntimeListFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_pick_detail_view",state);
			ArrayList selectedItems = listForm.getAllSelectedItems();
			_log.debug("LOG_DEBUG_EXTENSION_SHIPPICKLINEITEM","Selected Items:"+selectedItems,100L);			
			if(selectedItems != null && selectedItems.size() > 0)
			{		  		 
				Iterator bioBeanIter = selectedItems.iterator();
				UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
				ArrayList passedSelectedItems = new ArrayList();
				try
				{
					BioBean bio;
					int successfulShipmentCount = 0;
					for(; bioBeanIter.hasNext();successfulShipmentCount++){            	 
						bio = (BioBean)bioBeanIter.next();
						String batchCartonId = bio.getString("BATCHCARTONID");
						String status = bio.getString("STATUS");
						_log.debug("LOG_DEBUG_EXTENSION_SHIPPICKLINEITEM","Got BATCHCARTONID:"+batchCartonId,100L);
						_log.debug("LOG_DEBUG_EXTENSION_SHIPPICKLINEITEM","Got Status:"+status,100L);
						
						if(batchCartonId != null && batchCartonId.length() > 0){
							if(status != null && status.length() > 0 && Integer.parseInt(status) < 7){
								passedSelectedItems.add(bio.getString("PICKDETAILKEY"));
								for(;bioBeanIter.hasNext();){
									bio = (BioBean)bioBeanIter.next();
									passedSelectedItems.add(bio.getString("PICKDETAILKEY"));
								}
								state.getRequest().getSession().removeAttribute("SHIPPICKLINESREMAINING");
								state.getRequest().getSession().setAttribute("SHIPPICKLINESREMAINING",passedSelectedItems);
								state.getRequest().getSession().removeAttribute("SHIPPICKLINESREMAININGCOUNT");
								state.getRequest().getSession().setAttribute("SHIPPICKLINESREMAININGCOUNT",new Integer(successfulShipmentCount));
								_log.debug("LOG_DEBUG_EXTENSION_SHIPPICKLINEITEM","Setting passedSelectedItems:"+passedSelectedItems+" In Session",100L);
								_log.debug("LOG_DEBUG_EXTENSION_SHIPPICKLINEITEM","Setting remainingCount:"+passedSelectedItems+" In Session",100L);
								_log.debug("LOG_DEBUG_EXTENSION_SHIPPICKLINEITEM","Setting Navigation closeModalDialog27",100L);
																								
								context.setNavigation("closeModalDialog27");
								return RET_CONTINUE;
							}
						}
						
						String sql = "UPDATE PICKDETAIL SET STATUS = '9' WHERE PICKDETAILKEY = '"+bio.getString("PICKDETAILKEY").toUpperCase()+"'";
						try {
							_log.debug("LOG_DEBUG_EXTENSION_SHIPPICKLINEITEM","Updating Status...",100L);							
							new WmsDataProviderImpl().executeUpdateSql(sql);
						} catch (RemoteException e) {
							_log.debug("LOG_DEBUG_EXTENSION_SHIPPICKLINEITEM","Exiting ShipPickLineItemAction",100L);							
							String args[] = new String[0];							
							String errorMsg = getTextMessage("ERROR_EXECUTING_ACTION",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);
						} catch (ServiceObjectException e) {							
							_log.debug("LOG_DEBUG_EXTENSION_SHIPPICKLINEITEM","Exiting ShipPickLineItemAction",100L);
							throw new UserException(e.getLocalizedMessage(),new Object[0]);
						}
					}
					uowb.saveUOW();  					
					result.setSelectedItems(null);
					listForm.setSelectedItems(null);
					context.setNavigation("closeModalDialog29");
				}
				catch(EpiException ex)
				{
					throwUserException(ex, "ERROR_EXECUTING_ACTION", null);
				}
			}
		}
		if(didCloseBatchConfModal.equals("true") && didCloseInitialConfModal.equals("false")){		
			Object selectedItemsObj = state.getRequest().getSession().getAttribute("SHIPPICKLINESREMAINING");
			Object successfulShipmentCountObj = state.getRequest().getSession().getAttribute("SHIPPICKLINESREMAININGCOUNT");
			state.getRequest().getSession().removeAttribute("SHIPPICKLINESREMAINING");
			state.getRequest().getSession().removeAttribute("SHIPPICKLINESREMAININGCOUNT");
			_log.debug("LOG_DEBUG_EXTENSION_SHIPPICKLINEITEM","Got Objects selectedItemsObj:"+selectedItemsObj+" And successfulShipmentCountObj:"+successfulShipmentCountObj+" From Session",100L);			
			if(selectedItemsObj == null || successfulShipmentCountObj == null){
				_log.debug("LOG_DEBUG_EXTENSION_SHIPPICKLINEITEM","Exiting ShipPickLineItemAction",100L);				
				state.getRequest().getSession().removeAttribute("SHIPPICKLINESREMAINING");
				state.getRequest().getSession().removeAttribute("SHIPPICKLINESREMAININGCOUNT");
				String args[] = new String[0];
				String errorMsg = getTextMessage("ERROR_EXECUTING_ACTION",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);				
			}
			ArrayList selectedItems = (ArrayList)selectedItemsObj; 		
			_log.debug("LOG_DEBUG_EXTENSION_SHIPPICKLINEITEM","Selected Items:"+selectedItems,100L);			
			if(selectedItems != null && selectedItems.size() > 0)
			{		  		 
				Iterator pickDetailKeyIter = selectedItems.iterator();
				UnitOfWorkBean uowb = state.getDefaultUnitOfWork();				
				try
				{
					String pickDetailKey;
					int successfulShipmentCount = ((Integer)successfulShipmentCountObj).intValue();
					for(; pickDetailKeyIter.hasNext();successfulShipmentCount++){            	 
						pickDetailKey = (String)pickDetailKeyIter.next();
						String sql = "UPDATE PICKDETAIL SET STATUS = '9' WHERE PICKDETAILKEY = '"+pickDetailKey.toUpperCase()+"'";
						try {
							_log.debug("LOG_DEBUG_EXTENSION_SHIPPICKLINEITEM","Updating Status",100L);							
							new WmsDataProviderImpl().executeUpdateSql(sql);
						} catch (RemoteException e) {
							_log.debug("LOG_DEBUG_EXTENSION_SHIPPICKLINEITEM","Exiting ShipPickLineItemAction",100L);							
							String args[] = new String[0];							
							String errorMsg = getTextMessage("ERROR_EXECUTING_ACTION",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);
						} catch (ServiceObjectException e) {		
							_log.debug("LOG_DEBUG_EXTENSION_SHIPPICKLINEITEM","Exiting ShipPickLineItemAction",100L);
							throw new UserException(e.getLocalizedMessage(),new Object[0]);
						}										
					}
					uowb.saveUOW();  					
					result.setSelectedItems(null);
					listForm.setSelectedItems(null);
					_log.debug("LOG_DEBUG_EXTENSION_SHIPPICKLINEITEM","Setting Navigation closeModalDialog29",100L);					
					context.setNavigation("closeModalDialog29");
				}
				catch(EpiException ex)
				{
					_log.debug("LOG_DEBUG_EXTENSION_SHIPPICKLINEITEM","Exiting ShipPickLineItemAction",100L);					
					throwUserException(ex, "ERROR_EXECUTING_ACTION", null);
				}
			}
		}		
		_log.debug("LOG_DEBUG_EXTENSION_SHIPPICKLINEITEM","Exiting ShipPickLineItemAction",100L);
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
