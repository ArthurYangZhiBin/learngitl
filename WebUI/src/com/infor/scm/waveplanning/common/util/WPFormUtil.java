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
package com.infor.scm.waveplanning.common.util;

import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class WPFormUtil
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPFormUtil.class);
	public WPFormUtil()
    {
    }
	
	/**
	 * Traverses the form tree using the topLevelFormName as the root looking for the targetForm. Returns the targetForm if it can find it
	 * or null if it cannot. If the tree contains forms within a form slot that have more than one tab identifier, then the function will
	 * only traverse the form associated with the default tab identifier. 
	 * @param startingForm - the current runtime form
	 * @param topLevelFormName - the name of the form to be used as the root of the form tree
	 * @param targetFormName - the name of the form you are searching for
	 * @param state
	 * @return - the target form if it exists and is locatable without going though non-default tab identifiers, null otherwise.
	 */
	public static RuntimeFormInterface findForm(RuntimeFormInterface startingForm, String topLevelFormName, String targetFormName, StateInterface state){
		
		_log.debug("LOG_DEBUG_JAVACLASS_FORMUTIL","In findForm()",100L);
		_log.debug("LOG_DEBUG_JAVACLASS_FORMUTIL","startingForm:"+startingForm.getName(),100L);		
		
		if(startingForm.getName().equals(targetFormName))
			return startingForm;
				
		RuntimeFormInterface rootForm = startingForm;
		if(startingForm.getParentForm(state) != null){
			rootForm = startingForm.getParentForm(state);
		}
		while(rootForm.getParentForm(state) != null && !rootForm.getName().equals(topLevelFormName)){
			rootForm = rootForm.getParentForm(state);
		}
		
		if(rootForm.getName().equals(targetFormName))
			return rootForm;
		
		return traverseFormTree(rootForm,targetFormName,state);		
	}
	
	/**
	 * Traverses the form tree using the topLevelFormName as the root looking for the targetForm. Returns the targetForm if it can find it
	 * or null if it cannot. If the tree contains forms within a form slot that have more than one tab identifier, then the function will
	 * iterate through the ArrayList provided looking for tab identifiers that are defined for that form slot. 
	 * @param startingForm - the current runtime form
	 * @param topLevelFormName - the name of the form to be used as the root of the form tree
	 * @param targetFormName - the name of the form you are searching for
	 * @param tabIdentifiers - A list of Strings of all tab identifiers the function will need to locate the form.
	 * @param state
	 * @return - the target form if it exists and is locatable with the list of tab identifiers provided, null otherwise.
	 */
	public static RuntimeFormInterface findForm(RuntimeFormInterface startingForm, String topLevelFormName, String targetFormName, ArrayList tabIdentifiers,StateInterface state){
		
		_log.debug("LOG_DEBUG_JAVACLASS_FORMUTIL","In findForm()",100L);
		_log.debug("LOG_DEBUG_JAVACLASS_FORMUTIL","startingForm:"+startingForm.getName(),100L);
		
		RuntimeFormInterface rootForm = startingForm;
		if(startingForm.getParentForm(state) != null){
			rootForm = startingForm.getParentForm(state);
		}
		while(rootForm.getParentForm(state) != null && !rootForm.getName().equals(topLevelFormName)){
			rootForm = rootForm.getParentForm(state);
		}
		
		if(rootForm.getName().equals(targetFormName))
			return rootForm;
		
		if(tabIdentifiers != null && tabIdentifiers.size() > 0)
			return traverseFormTree(rootForm,targetFormName,tabIdentifiers,state);
		else
			return traverseFormTree(rootForm,targetFormName,state);
	}
	
	private static RuntimeFormInterface traverseFormTree(RuntimeFormInterface parentForm,String targetFormName, StateInterface state){
				
		_log.debug("LOG_DEBUG_JAVACLASS_FORMUTIL","in traverseFormTree",100L);	
		if(parentForm == null)
			return null;
		_log.debug("LOG_DEBUG_JAVACLASS_FORMUTIL","parentForm:"+parentForm.getName(),100L);		
		Iterator slotItr = parentForm.getSubSlotsIterator();	
		ArrayList forms = new ArrayList();
		
		while(slotItr.hasNext()){			
			SlotInterface slot = (SlotInterface)slotItr.next();
			_log.debug("LOG_DEBUG_JAVACLASS_FORMUTIL","Slot:"+slot.getName(),100L);			
			forms.add(state.getRuntimeForm(slot,null));			
		}
		
		for(int i = 0; i < forms.size(); i++){			
			RuntimeFormInterface form = (RuntimeFormInterface)forms.get(i);
			if(form != null){
				_log.debug("LOG_DEBUG_JAVACLASS_FORMUTIL","form:"+form.getName(),100L);				
				if(form.getName().equals(targetFormName))
					return form;
				else{
					form = traverseFormTree(form,targetFormName,state);
					if(form != null && form.getName().equals(targetFormName)){
						return form;
					}
				}
			}
			else{
				_log.debug("LOG_DEBUG_JAVACLASS_FORMUTIL","form:NULL",100L);				
			}
		}
		return null;
	}
	private static RuntimeFormInterface traverseFormTree(RuntimeFormInterface parentForm,String targetFormName, ArrayList tabIdentifiers, StateInterface state){
		
		
		_log.debug("LOG_DEBUG_JAVACLASS_FORMUTIL","in traverseFormTree",100L);
		if(parentForm == null)
			return null;
		_log.debug("LOG_DEBUG_JAVACLASS_FORMUTIL","parentForm:"+parentForm.getName(),100L);
		Iterator slotItr = parentForm.getSubSlotsIterator();	
		ArrayList forms = new ArrayList();		
		
		while(slotItr.hasNext()){			
			SlotInterface slot = (SlotInterface)slotItr.next();
			_log.debug("LOG_DEBUG_JAVACLASS_FORMUTIL","Slot:"+slot.getName(),100L);			
			RuntimeFormInterface tempForm = state.getRuntimeForm(slot, null);
			if (tempForm != null && !tempForm.getName().equalsIgnoreCase("blank")) {
				_log.debug("LOG_DEBUG_JAVACLASS_FORMUTIL","Adding " + tempForm.getName(),100L);				
				forms.add(tempForm);
			}
			for(int i = 0; i < tabIdentifiers.size(); i++){
				String tabIdentifier = (String)tabIdentifiers.get(i);				
				try {
					RuntimeFormInterface form = state.getRuntimeForm(slot,tabIdentifier);
					if(form != null && !form.getName().equalsIgnoreCase("blank")){
						_log.debug("LOG_DEBUG_JAVACLASS_FORMUTIL","Retrieved Form:"+form.getName()+" Using Tab Identifier:"+tabIdentifier+"   For Form Slot:"+slot.getName(),100L);						
						if(!forms.contains(form)){
							_log.debug("LOG_DEBUG_JAVACLASS_FORMUTIL","adding form...",100L);							
							forms.add(form);
						}
						else{
							_log.debug("LOG_DEBUG_JAVACLASS_FORMUTIL","form already has been found, skipping...",100L);							
						}
					}
					else{
						_log.debug("LOG_DEBUG_JAVACLASS_FORMUTIL","Retrieved NULL FORM Using Tab Identifier:"+tabIdentifier+"   For Form Slot:"+slot.getName(),100L);						
					}
				} catch (RuntimeException e) {
					_log.debug("LOG_DEBUG_JAVACLASS_FORMUTIL","Retrieved NULL FORM Using Tab Identifier:"+tabIdentifier+"   For Form Slot:"+slot.getName(),100L);					
				}
			}
		}
		
		for(int i = 0; i < forms.size(); i++){			
			RuntimeFormInterface form = (RuntimeFormInterface)forms.get(i);
			_log.debug("LOG_DEBUG_JAVACLASS_FORMUTIL","form:"+form.getName(),100L);			
			if(form.getName().equals(targetFormName))
				return form;
			else{
				form = traverseFormTree(form,targetFormName,tabIdentifiers,state);
				if(form != null && form.getName().equals(targetFormName)){
					return form;
				}
			}
		}
		return null;
	}

	static public String getWidgetLabel(ActionContext context, RuntimeFormWidgetInterface widget) {
		return widget.getLabel("label", context.getState().getLocale());
	}
}