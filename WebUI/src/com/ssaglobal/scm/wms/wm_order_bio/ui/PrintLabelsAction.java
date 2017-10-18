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
import java.util.HashMap;
import java.util.Iterator;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
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
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
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

public class PrintLabelsAction extends com.epiphany.shr.ui.action.ActionExtensionBase {
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PrintLabelsAction.class);
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
		_log.debug("LOG_DEBUG_EXTENSION_PRINTLABELS","Executing PrintLabelsAction",100L);		
		StateInterface state = context.getState();	
		RuntimeListFormInterface listForm = (RuntimeListFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_container_details_detail_view",state);
		if(listForm == null || listForm.getSelectedItems() == null || listForm.getSelectedItems().size() == 0){
			state.getRequest().getSession().removeAttribute("didCloseSelectPrinterModal");
			state.getRequest().getSession().removeAttribute("didCancelSelectPrinterModal");
			state.getRequest().getSession().removeAttribute("CONTAINERIDSTOPRINT");
			String args[] = new String[0];							
			String errorMsg = getTextMessage("WMEXP_NOTHING_SELECTED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		
		Object didCloseSelectPrinterModalObj = state.getRequest().getSession().getAttribute("didCloseSelectPrinterModal");				
		Object didCancelSelectPrinterModalObj = state.getRequest().getSession().getAttribute("didCancelSelectPrinterModal");
		state.getRequest().getSession().removeAttribute("didCloseSelectPrinterModal");
		state.getRequest().getSession().removeAttribute("didCancelSelectPrinterModal");
		String didCloseSelectPrinterModal = didCloseSelectPrinterModalObj==null?"false":didCloseSelectPrinterModalObj.toString();
		String didCancelSelectPrinterModal = didCancelSelectPrinterModalObj==null?"false":didCancelSelectPrinterModalObj.toString();
		_log.debug("LOG_DEBUG_EXTENSION_PRINTLABELS","Created Strings didCloseSelectPrinterModal:"+didCloseSelectPrinterModal+"   didCancelSelectPrinterModal:"+didCancelSelectPrinterModal,100L);		
		if(didCancelSelectPrinterModal.equals("true")){
			state.getRequest().getSession().removeAttribute("didCloseSelectPrinterModal");
			state.getRequest().getSession().removeAttribute("didCancelSelectPrinterModal");
			state.getRequest().getSession().removeAttribute("CONTAINERIDSTOPRINT");	
			return RET_CONTINUE;
		}
		if(didCloseSelectPrinterModal.equals("false")){			
			ArrayList selectedItems = listForm.getAllSelectedItems();
			HashMap uniqueContainers = new HashMap();
			_log.debug("LOG_DEBUG_EXTENSION_PRINTLABELS","Selected Items:"+selectedItems,100L);			
			Boolean isRfidPrinter = Boolean.FALSE;
			Boolean isNonRfidPrinter = Boolean.FALSE;
			if(selectedItems != null && selectedItems.size() > 0)
			{		  		 
				Iterator bioBeanIter = selectedItems.iterator();				
				try
				{
					BioBean bio;
					int successfulShipmentCount = 0;
					for(; bioBeanIter.hasNext();successfulShipmentCount++){            	 
						bio = (BioBean)bioBeanIter.next();
						String rfidFlag = bio.getString("RFIDFLAG");
						String containerId = bio.getString("CONTAINERDETAILID");
						_log.debug("LOG_DEBUG_EXTENSION_PRINTLABELS","Got RFIDFLAG:"+rfidFlag,100L);							
						if(!isRfidPrinter.booleanValue() && rfidFlag.equals("1")){
							isRfidPrinter = Boolean.TRUE;
						}
						if(!isNonRfidPrinter.booleanValue() && rfidFlag.equals("0")){
							isNonRfidPrinter = Boolean.TRUE;
						}
						if(!uniqueContainers.containsKey(containerId)){
							uniqueContainers.put(containerId,containerId);
						}																													
					}
					state.getRequest().getSession().removeAttribute("CONTAINERIDSTOPRINT");
					state.getRequest().getSession().setAttribute("CONTAINERIDSTOPRINT",uniqueContainers);
					state.getRequest().getSession().removeAttribute("ISRFIDPRINTER");
					state.getRequest().getSession().setAttribute("ISRFIDPRINTER",isRfidPrinter);
					state.getRequest().getSession().removeAttribute("ISNONRFIDPRINTER");
					state.getRequest().getSession().setAttribute("ISNONRFIDPRINTER",isNonRfidPrinter);
					_log.debug("LOG_DEBUG_EXTENSION_PRINTLABELS","Setting CONTAINERIDSTOPRINT:"+uniqueContainers+" In Session",100L);
					_log.debug("LOG_DEBUG_EXTENSION_PRINTLABELS","Setting ISRFIDPRINTER:"+isRfidPrinter+" In Session",100L);
					_log.debug("LOG_DEBUG_EXTENSION_PRINTLABELS","Setting ISNONRFIDPRINTER:"+isNonRfidPrinter+" In Session",100L);
					return RET_CONTINUE;
				}
				catch(EpiException ex)
				{
					throwUserException(ex, "ERROR_EXECUTING_ACTION", null);
				}
			}
		}
		if(didCloseSelectPrinterModal.equals("true")){		
			Object uniqueContainersObj = state.getRequest().getSession().getAttribute("CONTAINERIDSTOPRINT");
			Object rfidPrinterObj = state.getRequest().getSession().getAttribute("RFIDPRINTERNAME");
			Object nonRfidPrinterObj = state.getRequest().getSession().getAttribute("NONRFIDPRINTERNAME");
			state.getRequest().getSession().removeAttribute("CONTAINERIDSTOPRINT");
			state.getRequest().getSession().removeAttribute("RFIDPRINTERNAME");			
			state.getRequest().getSession().removeAttribute("NONRFIDPRINTERNAME");
			_log.debug("LOG_DEBUG_EXTENSION_PRINTLABELS","Got Objects uniqueContainersObj:"+uniqueContainersObj+"   rfidPrinterObj:"+rfidPrinterObj+"    nonRfidPrinterObj:"+nonRfidPrinterObj+" From Session",100L);			
			String rfidPrinterName = rfidPrinterObj == null?"":rfidPrinterObj.toString();
			String nonRfidPrinterName = nonRfidPrinterObj == null?"":nonRfidPrinterObj.toString();
			if(uniqueContainersObj == null){
				_log.debug("LOG_DEBUG_EXTENSION_PRINTLABELS","Exiting PrintLabelsAction",100L);							
				String args[] = new String[0];
				String errorMsg = getTextMessage("ERROR_EXECUTING_ACTION",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);				
			}
			HashMap uniqueContainers = (HashMap)uniqueContainersObj; 					
			_log.debug("LOG_DEBUG_EXTENSION_PRINTLABELS","Unique Containers:"+uniqueContainers,100L);			
			if(uniqueContainers != null && uniqueContainers.size() > 0)
			{		  		 
				Iterator containerIDIter = uniqueContainers.values().iterator();
				UnitOfWorkBean uowb = state.getDefaultUnitOfWork();				
				try
				{						
					for(; containerIDIter.hasNext();){            	 
						String containerId = (String)containerIDIter.next();
						Array parms = new Array(); 
	                    parms.add(new TextData(containerId));
	                    parms.add(new TextData(nonRfidPrinterName));
	                    parms.add(new TextData(rfidPrinterName));
	                    WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
	          		  	actionProperties.setProcedureParameters(parms);
	          		  	actionProperties.setProcedureName("NSPPrintContainerLabel");												
						try {
							_log.debug("LOG_DEBUG_EXTENSION_PRINTLABELS","Printing Container:"+containerId+"   On RFID Printer:"+rfidPrinterName+"   and Standard Printer:"+nonRfidPrinterName+"...",100L);							
							WmsWebuiActionsImpl.doAction(actionProperties);
						} catch (Exception e) {
							_log.debug("LOG_DEBUG_EXTENSION_PRINTLABELS","Exiting PrintLabelsAction",100L);							
							throw new UserException(e.getLocalizedMessage(),new Object[0]);
						}										
					}
					uowb.saveUOW();  					
					result.setSelectedItems(null);
					listForm.setSelectedItems(null);					
				}
				catch(EpiException ex)
				{
					String args[] = new String[0];
					String errorMsg = getTextMessage("ERROR_EXECUTING_ACTION",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			}
		}		
		_log.debug("LOG_DEBUG_EXTENSION_PRINTLABELS","Exiting PrintLabelsAction",100L);
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
