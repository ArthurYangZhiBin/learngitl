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


package com.ssaglobal.scm.wms.wm_batch_picking.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
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

public class BatchPickingShipWave extends com.epiphany.shr.ui.action.ActionExtensionBase {


	private final static String PROC_NAME = "NSPMASSSHIPORDERS";
	private final static String WAVE = "WAVEKEY";
	private final static String SHELL_SLOT = "list_slot_1";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(BatchPickingShipWave.class);
	
	protected int execute( ActionContext context, ActionResult result ) throws EpiException {

	   //Find wavekey and doRelease variables
		StateInterface state = context.getState();
		//RuntimeFormInterface shell = state.getCurrentRuntimeForm().getParentForm(state).getParentForm(state);
		//RuntimeFormInterface header = state.getRuntimeForm(shell.getSubSlot(SHELL_SLOT), null);
		//String waveKey = header.getFormWidgetByName(WAVE).getDisplayValue();
		

		
		RuntimeFormInterface waveForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_batch_picking_detail_view",state);		
		if(waveForm != null && waveForm.getFocus() != null && waveForm.getFocus() instanceof BioBean){
			BioBean waveFocus = (BioBean)waveForm.getFocus();
			BioCollection waveDetails = (BioCollection)waveFocus.getBioCollection("WAVEDETAIL");			
			
			if(waveDetails != null){
			
				
				if(waveDetails.size() > 0){
					
					for (int i=0; i < waveDetails.size(); i++){
						String orderKey = waveDetails.elementAt(i).get("ORDERKEY").toString().toUpperCase();
						
						WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
						Array parms = new Array(); 
						parms.add(new TextData(orderKey));	
						_log.debug("LOG_SYSTEM_OUT","\n\n--------NSPMASSSHIPORDERS Params------------",100L);
						_log.debug("LOG_SYSTEM_OUT","Param 1:"+orderKey,100L);
						_log.debug("LOG_SYSTEM_OUT","--------End NSPMASSSHIPORDERS Params------------\n\n",100L);
						
						actionProperties.setProcedureParameters(parms);
						actionProperties.setProcedureName(PROC_NAME);
						try{
							WmsWebuiActionsImpl.doAction(actionProperties);	
						}catch(Exception e){
							throw new UserException(e.getMessage(), new Object[] {});
						}
						
					}//end for
					
				}				
			}
		}//waveDetails
			
			
		//result.setFocus(header.getFocus());
		
		//_log.debug("LOG_SYSTEM_OUT","End OF BatchPickingShipWave",100L);
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
