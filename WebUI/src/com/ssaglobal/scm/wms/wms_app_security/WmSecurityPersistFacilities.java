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


package com.ssaglobal.scm.wms.wms_app_security;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;



public class WmSecurityPersistFacilities extends SaveAction
{	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WmSecurityPersistFacilities.class);
	public static final String SESS_KEY_FAC_ADD_LIST = "session.key.facility.add.list";
	public static final String SESS_KEY_FAC_DEL_LIST = "session.key.facility.del.list";
	public WmSecurityPersistFacilities()
	{
	}
	
	protected int execute(ActionContext context, ActionResult result)
	throws UserException
	{
		_log.debug("LOG_DEBUG_EXTENSION_WMSECPERPRO","Executing WmSecurityPersistProfiles",100L);    	
		StateInterface state = context.getState();
		HttpSession session = state.getRequest().getSession();
		ArrayList tabs = new ArrayList();
		tabs.add("tab 2");        
		RuntimeFormInterface facilitysForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wms_app_security_facility_list",tabs,state);
		
		if(facilitysForm != null)
        	_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Found Facility Form:"+facilitysForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Found Facility Form:Null",100L);			
        			
		if(facilitysForm != null){
			BioCollectionBean focus = (BioCollectionBean)facilitysForm.getFocus();
			if(focus != null){
				
				try {
					ArrayList addList = (ArrayList)session.getAttribute(SESS_KEY_FAC_ADD_LIST);
					ArrayList remList = (ArrayList)session.getAttribute(SESS_KEY_FAC_DEL_LIST);
					if(addList == null)
						addList = new ArrayList();
					if(remList == null)
						remList = new ArrayList();
					for(int i = 0; i < focus.size(); i++){
						Bio bio = focus.elementAt(i);							
						if(bio.get("ISSELECTED") != null){
							_log.debug("LOG_DEBUG_EXTENSION_WMSECPERPRO","Got Changed Bio "+bio.get("db_key"),100L);								
							String facilityId = (String)bio.get("db_key");							
							String isSelected = (String)bio.get("ISSELECTED");
							if(isSelected.equals("0")){   
								_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","isSelected is 0...",100L);	
								if(addList.contains(facilityId))
									addList.remove(facilityId);
								if(!remList.contains(facilityId))
									remList.add(facilityId);																	
							}
							else if(isSelected.equals("1")){       
								_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","isSelected is 1...",100L);
								if(remList.contains(facilityId))
									remList.remove(facilityId);
								if(!addList.contains(facilityId))
									addList.add(facilityId);	
							}
						}
					}
					session.setAttribute(SESS_KEY_FAC_ADD_LIST,addList);
					session.setAttribute(SESS_KEY_FAC_DEL_LIST,remList);
				} catch (EpiDataException e) {						
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} 				
			}
		}		
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving WmSecurityRolesSaveAction",100L);		
		return RET_CONTINUE;
	}
}
