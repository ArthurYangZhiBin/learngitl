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


package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class WidgetPreRenderASN extends com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase {


	/**
	 * The code within the execute method will be run on the WidgetRender.
	 * <P>
         * @param state The StateInterface for this extension
         * @param widget The RuntimeFormWidgetInterface for this extension's widget
         * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 */
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WidgetPreRenderASN.class);
	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget) { 

	    try {
	        
	        // Fetch any system properties needed
	    
	        // Fetch any parameter values needed
	    
	        // Process the event
	    	//SRG 09/22/10: Quality Center: Defect# 152: Added Duplicate button to the below condition
	    	//if(widget.getName().equals("NEW") || widget.getName().equals("DELETE")){
	    	if((widget.getName().equals("NEW")) || (widget.getName().equals("DELETE")) || (widget.getName().equals("Duplicate"))){
				RuntimeFormInterface shellForm = widget.getForm().getParentForm(state).getParentForm(state);
				RuntimeFormInterface headerForm = null;
				if(shellForm.getName().equals("wms_list_shell")){
					headerForm = state.getRuntimeForm(shellForm.getSubSlot("list_slot_1"), null);
				}else{
					while(!(shellForm.getName().equals("wms_tbgrp_shell"))){
						shellForm = shellForm.getParentForm(state);
					}
					headerForm = state.getRuntimeForm(shellForm.getSubSlot("tbgrp_slot"), "tab 0");
				}
				DataBean headerFocus = headerForm.getFocus();
				Object value = headerFocus.getValue("STATUS");
				if(value!=null){
					String status = value.toString();
					if (status.equalsIgnoreCase("11")|| status.equalsIgnoreCase("15") || status.equalsIgnoreCase("20")){
						widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					}
				}
			}else{
				RuntimeFormInterface tabForm = widget.getForm().getParentForm(state);
				SlotInterface tabSlot = tabForm.getSubSlot("tbgrp_slot");
				RuntimeFormInterface headerForm = state.getRuntimeForm(tabSlot, "tab 0");
				RuntimeFormWidgetInterface typeWidget = headerForm.getFormWidgetByName("TYPE");
				RuntimeFormWidgetInterface OwnerWidget = headerForm.getFormWidgetByName("STORERKEY");
				String type = typeWidget.getValue().toString();
				ValidateOwner(state, OwnerWidget.getDisplayValue().toString(),type,widget);	
			}
        } catch(Exception e) {
            
            // Handle Exceptions 
		    e.printStackTrace();
		    return RET_CANCEL;
		    
	    } 
	    
	    return RET_CONTINUE;
		
	}
	private void ValidateOwner(StateInterface state, String Owner, String rectype, RuntimeFormWidgetInterface widget)throws EpiException {
		int size = 0;
		BioCollectionBean listCollection = null;
		_log.debug("LOG_SYSTEM_OUT","************Widget name  ******"+widget.getName() ,100L);
		try {
    		String sQueryString = "(wm_storer.STORERKEY ~= '" + Owner + "' AND  wm_storer.TYPE ~= '1')";
    	   _log.debug("LOG_SYSTEM_OUT","sQueryString = "+ sQueryString,100L);
    	   Query BioQuery = new Query("wm_storer",sQueryString,null);
    	   UnitOfWorkBean uow = state.getDefaultUnitOfWork();
           listCollection = uow.getBioCollectionBean(BioQuery);
           size = listCollection.size();
		} catch(EpiException e) {
        
        // Handle Exceptions 
			e.printStackTrace();
	    
		} 
		_log.debug("LOG_SYSTEM_OUT","************size ="+ size ,100L);
		if (size != 0)
		{
			_log.debug("LOG_SYSTEM_OUT","************Widget name  ******"+widget.getName() ,100L);
			_log.debug("LOG_SYSTEM_OUT","************Widget Value  ******"+widget.getValue() ,100L);
			if (widget.getValue() == null){
				if (widget.getName().equalsIgnoreCase("TRACKINVENTORYBY")){
					_log.debug("LOG_SYSTEM_OUT","Inside Track Inventory",100L);
					if (rectype.equalsIgnoreCase("4") || rectype.equalsIgnoreCase("5")){
						_log.debug("LOG_SYSTEM_OUT","Type is 4 or 5",100L);
						state.getFocus().setValue("TRACKINVENTORYBY",listCollection.get("0").get("TRACKINVENTORYBY"));
						_log.debug("LOG_SYSTEM_OUT","Widget Value = "+ widget.getValue(),100L);	
					}
				}
				if (widget.getName().equalsIgnoreCase("ALLOWAUTORECEIPT")){
					_log.debug("LOG_SYSTEM_OUT","Inside Auto Receipt",100L);
					if (rectype.equalsIgnoreCase("4") || rectype.equalsIgnoreCase("5")){
						_log.debug("LOG_SYSTEM_OUT","Type is 4 or 5",100L);
						if (listCollection.get("0").get("ALLOWSINGLESCANRECEIVING").equals("1")){
							_log.debug("LOG_SYSTEM_OUT","Aut receipt is 1",100L);
							widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
							
						}
						else{
							_log.debug("LOG_SYSTEM_OUT","Aut receipt is 0",100L);
							widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
						}
					}
					else{
						_log.debug("LOG_SYSTEM_OUT","not type 4 or 5",100L);
						widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					}
				}
				
			}
		}
	}
}

