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


package com.ssaglobal.scm.wms.wm_allocation_management.ui;

//Import 3rd party packages and classes

//Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class AllocateInventoryAction extends com.epiphany.shr.ui.action.ActionExtensionBase {
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(AllocateInventoryAction.class);
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
		_log.debug("LOG_DEBUG_EXTENSION_ALLOCINVACTION","Executing AllocateInventoryAction",100L);			
		StateInterface state = context.getState();	
		RuntimeListFormInterface listForm = (RuntimeListFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_allocation_management_detail_list_view",state);
		if(listForm == null || listForm.getSelectedItems() == null || listForm.getSelectedItems().size() == 0){
			_log.debug("LOG_DEBUG_EXTENSION_ALLOCINVACTION","Exiting AllocateInventoryAction",100L);			
			String args[] = new String[0];							
			String errorMsg = getTextMessage("WMEXP_NOTHING_SELECTED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}		
		
		ArrayList selectedItems = listForm.getAllSelectedItems();
		_log.debug("LOG_DEBUG_EXTENSION_ALLOCINVACTION","Selected Items:"+selectedItems,100L);		
		if(selectedItems != null && selectedItems.size() > 0)
		{		  		 
			Iterator bioBeanIter = selectedItems.iterator();			
			try
			{
				BioBean bio;				
				for(; bioBeanIter.hasNext();){            	 
					bio = (BioBean)bioBeanIter.next();
					String xOrderKey = bio.getString("ORDERKEY");
					String orderLineNumber = bio.getString("ORDERLINENUMBER");
					String qtyToProc = bio.getString("QTYTOPROCESS");					
					_log.debug("LOG_DEBUG_EXTENSION_ALLOCINVACTION","Got xOrderKey:"+xOrderKey,100L);
					_log.debug("LOG_DEBUG_EXTENSION_ALLOCINVACTION","Got orderLineNumber:"+orderLineNumber,100L);
					_log.debug("LOG_DEBUG_EXTENSION_ALLOCINVACTION","Got qtyToProc:"+qtyToProc,100L);
					WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
					Array parms = new Array(); 
					parms.add(new TextData(xOrderKey));
					parms.add(new TextData(orderLineNumber));
					parms.add(new TextData(qtyToProc));
					actionProperties.setProcedureParameters(parms);
					actionProperties.setProcedureName("NSPFLOWTHROUGHALLOCATE");
					try {
						WmsWebuiActionsImpl.doAction(actionProperties);							
					} catch (Exception e) {
						_log.debug("LOG_DEBUG_EXTENSION_ALLOCINVACTION","Exiting AllocateInventoryAction",100L);								
						String errorMsg = e.getLocalizedMessage();
						throw new UserException(errorMsg,new Object[0]);
					}
				}					
				result.setSelectedItems(null);
				listForm.setSelectedItems(null);					
			}
			catch(EpiException ex)
			{
				_log.debug("LOG_DEBUG_EXTENSION_ALLOCINVACTION","Exiting AllocateInventoryAction",100L);
				String args[] = new String[0];							
				String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);				
			}
		}
		
		_log.debug("LOG_DEBUG_EXTENSION_ALLOCINVACTION","Exiting AllocateInventoryAction",100L);
		return RET_CONTINUE;
		
	}		
}
