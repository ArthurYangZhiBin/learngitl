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
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.GenericException;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class MassShipOrdersAction extends com.epiphany.shr.ui.action.ActionExtensionBase {
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(MassShipOrdersAction.class);
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
		_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPORDERS","Executing MassShipOrdersAction",100L);			
		StateInterface state = context.getState();	
		HttpSession session = state.getRequest().getSession();
		RuntimeListFormInterface listForm = (RuntimeListFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(),"wm_list_shell_shipmentorder","wm_ship_order_list_view",state);
		if(listForm == null || listForm.getSelectedItems() == null || listForm.getSelectedItems().size() == 0){
			_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPORDERS","Cleaning Session...",100L);			
			session.removeAttribute("didCloseInitialConfModal");
			session.removeAttribute("didCloseBatchConfModal");
			session.removeAttribute("didCancelBatchConfModal");
			session.removeAttribute("didCloseFailedCountModal");
			session.removeAttribute("MASSSHIPREMAINING");			
			session.removeAttribute("MASSSHIPREMAININGCOUNT");
			session.removeAttribute("MASSSHIPFAILEDCOUNT");
			session.removeAttribute("MASSSHIPFAILEDMESSAGE");
			session.removeAttribute("MASSSHIPORDERKEY");
			String args[] = new String[0];							
			String errorMsg = getTextMessage("WMEXP_NOTHING_SELECTED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		
		Object didCloseInitialConfModalObj = session.getAttribute("didCloseInitialConfModal");
		Object didCloseBatchConfModalObj = session.getAttribute("didCloseBatchConfModal");
		Object didCancelBatchConfModalObj = session.getAttribute("didCancelBatchConfModal");
		Object didCloseFailedCountModalObj = session.getAttribute("didCloseFailedCountModal");
		_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPORDERS","Got Objects didCloseInitialConfModalObj:"+didCloseInitialConfModalObj+" And didCloseBatchConfModalObj:"+didCloseBatchConfModalObj+" From Session",100L);		
		session.removeAttribute("didCloseInitialConfModal");
		session.removeAttribute("didCloseBatchConfModal");
		session.removeAttribute("didCloseFailedCountModal");
		session.removeAttribute("didCancelBatchConfModal");
		String didCloseInitialConfModal = didCloseInitialConfModalObj==null?"false":didCloseInitialConfModalObj.toString();
		String didCloseBatchConfModal = didCloseBatchConfModalObj==null?"false":didCloseBatchConfModalObj.toString();
		String didCancelBatchConfModal = didCancelBatchConfModalObj==null?"false":didCancelBatchConfModalObj.toString();
		String didCloseFailedCountModal = didCloseFailedCountModalObj==null?"false":didCloseFailedCountModalObj.toString();
		_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPORDERS","Created Strings didCloseInitialConfModal:"+didCloseInitialConfModal+" And didCloseBatchConfModal:"+didCloseBatchConfModal,100L);		
		if(didCancelBatchConfModal.equals("true") || didCloseFailedCountModal.equals("true")){
			context.setNavigation("closeModalDialog31");
			return RET_CONTINUE;
		}
		if(didCloseBatchConfModal.equals("false") && didCloseInitialConfModal.equals("true")){
			ArrayList selectedItems = listForm.getAllSelectedItems();			
			_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPORDERS","Selected Items:"+selectedItems,100L);
			if(selectedItems != null && selectedItems.size() > 0)
			{		  		 
				Iterator bioBeanIter = selectedItems.iterator();
				UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
				ArrayList passedSelectedItems = new ArrayList();
				try
				{
					BioBean bio;
					int successfulShipmentCount = 0;
					int failedShipmentCount = 0;
					for(; bioBeanIter.hasNext();successfulShipmentCount++){            	 
						bio = (BioBean)bioBeanIter.next();
						String batchflag = bio.getString("BATCHFLAG");
						String orderKey = bio.getString("ORDERKEY");
						_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPORDERS","Got BATCHFLAG:"+batchflag,100L);
						_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPORDERS","Got ORDERKEY:"+orderKey,100L);

						//jp begin - Suspended Orders
						if (isSuspendedOrder(uowb, orderKey)){
							throw new UserException("WMEXP_SO_SUSPEND_SHIP",new String[] {orderKey});				

						}
						//jp end - Suspended Orders
						
						if(batchflag != null && batchflag.equals("1")){
							orderKey = orderKey.toUpperCase();
							Query loadBiosQry = new Query("wm_pickdetail", "wm_pickdetail.ORDERKEY = '"+orderKey+"' AND wm_pickdetail.STATUS < '7'", ""); 				
							UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
							BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);		
							if(bioCollection != null && bioCollection.size() > 0){	
								_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPORDERS","Removing Session Attr MASSSHIPORDERKEY...",100L);								
								session.removeAttribute("MASSSHIPORDERKEY");
								_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPORDERS","Setting Session Attr MASSSHIPORDERKEY:"+orderKey+"...",100L);								
								session.setAttribute("MASSSHIPORDERKEY",orderKey);
								String[] orderKeyAndBatchFlag = new String[2];
								orderKeyAndBatchFlag[0] = bio.getString("ORDERKEY");
								orderKeyAndBatchFlag[1] = bio.getString("BATCHFLAG");
								passedSelectedItems.add(orderKeyAndBatchFlag);
								for(;bioBeanIter.hasNext();){									
									bio = (BioBean)bioBeanIter.next();									
									orderKeyAndBatchFlag[0] = bio.getString("ORDERKEY");
									orderKeyAndBatchFlag[1] = bio.getString("BATCHFLAG");
									passedSelectedItems.add(orderKeyAndBatchFlag);
								}
								
								session.setAttribute("MASSSHIPREMAINING",passedSelectedItems);								
								session.setAttribute("MASSSHIPREMAININGCOUNT",new Integer(successfulShipmentCount));																								
								session.setAttribute("MASSSHIPFAILEDCOUNT",new Integer(failedShipmentCount));
								_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPORDERS","Added Session Attr MASSSHIPFAILEDCOUNT:"+session.getAttribute("MASSSHIPFAILEDCOUNT"),100L);
								_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPORDERS","Setting passedSelectedItems:"+passedSelectedItems+" In Session",100L);
								_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPORDERS","Setting remainingCount:"+passedSelectedItems+" In Session",100L);
								_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPORDERS","Setting Navigation closeModalDialog30",100L);
								context.setNavigation("closeModalDialog30");
								return RET_CONTINUE;
							}
						}
						
						WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
						Array parms = new Array(); 
						parms.add(new TextData(orderKey));
						actionProperties.setProcedureParameters(parms);
						actionProperties.setProcedureName("NSPMASSSHIPORDERS");
						try {
							EXEDataObject procResult = WmsWebuiActionsImpl.doAction(actionProperties);
							if(procResult.getRowCount() == -1)
								failedShipmentCount++;
						} catch (Exception e) {
							e.printStackTrace();							
							String reasonCode = "";
							if (e instanceof GenericException) {
								reasonCode = ((GenericException) e).reasonCodeString();
							} else {
								reasonCode = e.getMessage();
							}
							if(session.getAttribute("MASSSHIPFAILEDMESSAGE") == null){
								String[] args = {orderKey,reasonCode};
								String msg = getTextMessage("WMEXP_MASS_SHIP_ORDER_FAILED",args,context.getState().getLocale());
								session.setAttribute("MASSSHIPFAILEDMESSAGE",msg);
							}
							else{
								String[] args = {orderKey,reasonCode};
								String msg = getTextMessage("WMEXP_MASS_SHIP_ORDER_FAILED",args,context.getState().getLocale());
								session.setAttribute("MASSSHIPFAILEDMESSAGE",session.getAttribute("MASSSHIPFAILEDMESSAGE").toString() + "\r\n\r\n" + msg);
							}
							failedShipmentCount++;
						}
					}
					uowb.saveUOW();  					
					result.setSelectedItems(null);
					listForm.setSelectedItems(null);
					if(failedShipmentCount == 0)
						context.setNavigation("closeModalDialog31");
					else{
						session.setAttribute("MASSSHIPFAILEDCOUNT",new Integer(failedShipmentCount));
						context.setNavigation("closeModalDialog32");
					}
				}
				catch(EpiException ex)
				{
					throwUserException(ex, "ERROR_EXECUTING_ACTION", null);
				}
			}
		}
		if(didCloseBatchConfModal.equals("true") && didCloseInitialConfModal.equals("false")){
			//listForm = (RuntimeListFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_pick_detail_view",state);
			Object selectedItemsObj = session.getAttribute("MASSSHIPREMAINING");
			Object successfulShipmentCountObj = session.getAttribute("MASSSHIPREMAININGCOUNT");
			Object failedShipmentCountObj = session.getAttribute("MASSSHIPFAILEDCOUNT");
			int failedShipmentCount = ((Integer)failedShipmentCountObj).intValue();
			session.removeAttribute("MASSSHIPREMAINING");
			session.removeAttribute("MASSSHIPREMAININGCOUNT");
			session.removeAttribute("MASSSHIPFAILEDCOUNT");
			_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPORDERS","Got Objects selectedItemsObj:"+selectedItemsObj+" And successfulShipmentCountObj:"+successfulShipmentCountObj+" From Session",100L);			
			if(selectedItemsObj == null || successfulShipmentCountObj == null || failedShipmentCountObj == null){
				_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPORDERS","Exiting MassShipOrdersAction",100L);							
				String args[] = new String[0];
				String errorMsg = getTextMessage("ERROR_EXECUTING_ACTION",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);				
			}
			ArrayList selectedItems = (ArrayList)selectedItemsObj; 		
			_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPORDERS","Selected Items:"+selectedItems,100L);			
			if(selectedItems != null && selectedItems.size() > 0)
			{		  		 
				Iterator orderKeyIter = selectedItems.iterator();
				UnitOfWorkBean uowb = state.getDefaultUnitOfWork();			
				ArrayList passedSelectedItems = new ArrayList();
				try
				{
					String orderKey;
					int successfulShipmentCount = ((Integer)successfulShipmentCountObj).intValue();					
					for(; orderKeyIter.hasNext();successfulShipmentCount++){ 
						String[] orderKeyAndBatchFlag = (String[])orderKeyIter.next();
						orderKey = orderKeyAndBatchFlag[0];
						String batchFlag = orderKeyAndBatchFlag[1];
						_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPORDERS","Got BATCHFLAG:"+batchFlag,100L);
						_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPORDERS","Got ORDERKEY:"+orderKey,100L);

						//jp begin - Suspended Orders
						if (isSuspendedOrder(uowb, orderKey)){
							throw new UserException("WMEXP_SO_SUSPEND_SHIP",new String[] {orderKey});				

						}
						//jp end - Suspended Orders

						if(batchFlag != null && batchFlag.equals("1")){
							orderKey = orderKey.toUpperCase();
							Query loadBiosQry = new Query("wm_pickdetail", "wm_pickdetail.ORDERKEY = '"+orderKey+"' AND wm_pickdetail.STATUS < '7'", ""); 				
							UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
							BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);		
							if(bioCollection != null && bioCollection.size() > 0){										
								_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPORDERS","Removing Session Attr MASSSHIPORDERKEY...",100L);
								session.removeAttribute("MASSSHIPORDERKEY");
								_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPORDERS","Setting Session Attr MASSSHIPORDERKEY:"+orderKey+"...",100L);								
								session.setAttribute("MASSSHIPORDERKEY",orderKey);
								passedSelectedItems.add(orderKeyAndBatchFlag);
								for(;orderKeyIter.hasNext();){									
									passedSelectedItems.add(orderKeyIter.next());
								}								
								session.setAttribute("MASSSHIPREMAINING",passedSelectedItems);								
								session.setAttribute("MASSSHIPREMAININGCOUNT",new Integer(successfulShipmentCount));								
								session.setAttribute("MASSSHIPFAILEDCOUNT",new Integer(failedShipmentCount));
								_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPORDERS","Setting passedSelectedItems:"+passedSelectedItems+" In Session",100L);
								_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPORDERS","Setting remainingCount:"+passedSelectedItems+" In Session",100L);
								_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPORDERS","Setting Navigation closeModalDialog30",100L);
								context.setNavigation("closeModalDialog30");
								return RET_CONTINUE;
							}
						}
						
						WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
						Array parms = new Array(); 
						parms.add(new TextData(orderKey));
						actionProperties.setProcedureParameters(parms);
						actionProperties.setProcedureName("NSPMASSSHIPORDERS");
						try {
							EXEDataObject procResult = WmsWebuiActionsImpl.doAction(actionProperties);
							if(procResult.getRowCount() == -1)
								failedShipmentCount++;
						} catch (Exception e) {
							e.printStackTrace();							
							String reasonCode = "";
							if (e instanceof GenericException) {
								reasonCode = ((GenericException) e).reasonCodeString();
							} else {
								reasonCode = e.getMessage();
							}
							if(session.getAttribute("MASSSHIPFAILEDMESSAGE") == null){
								String[] args = {orderKey,reasonCode};
								String msg = getTextMessage("WMEXP_MASS_SHIP_ORDER_FAILED",args,context.getState().getLocale());
								session.setAttribute("MASSSHIPFAILEDMESSAGE",msg);
							}
							else{
								String[] args = {orderKey,reasonCode};
								String msg = getTextMessage("WMEXP_MASS_SHIP_ORDER_FAILED",args,context.getState().getLocale());
								session.setAttribute("MASSSHIPFAILEDMESSAGE",session.getAttribute("MASSSHIPFAILEDMESSAGE").toString() + "\r\n\r\n" + msg);
							}
							failedShipmentCount++;
						}									
					}
					uowb.saveUOW();  					
					result.setSelectedItems(null);
					listForm.setSelectedItems(null);
					_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPORDERS","Setting Navigation closeModalDialog31",100L);					
					if(failedShipmentCount == 0)
						context.setNavigation("closeModalDialog31");
					else{
						session.setAttribute("MASSSHIPFAILEDCOUNT",new Integer(failedShipmentCount));
						context.setNavigation("closeModalDialog32");
					}
				}
				catch(EpiException ex)
				{
					_log.debug("LOG_DEBUG_EXTENSION_MASSSHIPORDERS","Exiting MassShipOrdersAction",100L);					
					throwUserException(ex, "ERROR_EXECUTING_ACTION", null);
				}
			}
			else{
				result.setSelectedItems(null);
				listForm.setSelectedItems(null);
				if(failedShipmentCount == 0)
					context.setNavigation("closeModalDialog31");
				else
					context.setNavigation("closeModalDialog32");
			}
		}		
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Exiting MassShipOrdersAction",100L);
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
	
	private boolean isSuspendedOrder(UnitOfWorkBean uowb, String orderkey){
	
		boolean suspended = false;
		
		Query query = new Query("wm_orders","wm_orders.ORDERKEY = '" + orderkey+"'", "" );
		BioCollectionBean bioCollectionBean = uowb.getBioCollectionBean(query);
		
		try {
			if (bioCollectionBean==null  || bioCollectionBean.size()==0){
				_log.debug("LOG_SYSTEM_OUT","[MassShipOrdersAction:isSuspendedOrder] Collection is empty",100L);
				return suspended;
			}
			
			Bio shipmentOrderBean = (Bio) bioCollectionBean.elementAt(0);
			String suspendedIndicator = (String)shipmentOrderBean.get("SuspendedIndicator");
			if (suspendedIndicator!=null && suspendedIndicator.equalsIgnoreCase("1")){
				return true;
			}
		} catch (EpiDataException e) {
			_log.debug("LOG_SYSTEM_OUT","Error in isSuspendedOrder method. Message:"+ e.getErrorMessage(),100L);
		}
		
	
		
		return suspended;
	}
}
