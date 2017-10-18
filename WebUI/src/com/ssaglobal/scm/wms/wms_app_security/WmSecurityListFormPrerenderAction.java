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
import java.util.HashMap;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;


public class WmSecurityListFormPrerenderAction extends FormExtensionBase{
	private static final String PERMISSION_VIEW = "94B00DF0EB954B1FB77FC9AE96C16D8C";
	private static final String PERMISSION_ACTIVE = "D0690C2A5FB84EF0B05B56A06D2A7C4C";
	private static final String PROFILE_NOBODY = "5F34928F3BF7441A8E0585172FE04055";
	private static final String PROFILE_GUEST = "94361E314AD74E719375A648BC7A4424";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WmSecurityListFormPrerenderAction.class);
	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) throws EpiException {
		_log.debug("LOG_DEBUG_EXTENSION_WMSECLISTFRMPREREN","Executing WmSecurityListFormPrerenderAction",100L);		
		StateInterface state = context.getState();       
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();       
		try
		{                      
			String objectId = (String)state.getRequest().getSession().getAttribute("WM_SECURITY_SELECTED");
			_log.debug("LOG_DEBUG_EXTENSION_WMSECLISTFRMPREREN","objectId:"+objectId,100L);			
			if(objectId != null && objectId.length() > 0){
				state.getRequest().getSession().setAttribute("WM_SECURITY_SELECTED",objectId);
				BioCollectionBean mappingCol = uowb.getBioCollectionBean(new Query("wm_meta_permission_profile","wm_meta_permission_profile.object_id = '"+objectId.toUpperCase()+"'",""));
				BioCollectionBean focus = (BioCollectionBean)form.getFocus();
				_log.debug("LOG_DEBUG_EXTENSION_WMSECLISTFRMPREREN","mappingCol:"+mappingCol,100L);
				_log.debug("LOG_DEBUG_EXTENSION_WMSECLISTFRMPREREN","mappingCol size:"+mappingCol.size(),100L);
				_log.debug("LOG_DEBUG_EXTENSION_WMSECLISTFRMPREREN","focus:"+focus,100L);
				//If no mappings then all User Profiles have full permissions except "Guest"
				if(mappingCol == null || mappingCol.size() == 0){            			
					for(int i = 0; i < focus.size(); i++){
						if(focus.elementAt(i).get("user_profile_id").equals(PROFILE_GUEST)){
							focus.elementAt(i).set("canEdit","0");
							focus.elementAt(i).set("canView","0");            				
						}
						else{
							_log.debug("LOG_DEBUG_EXTENSION_WMSECLISTFRMPREREN","setting both to 1...",100L);							
							focus.elementAt(i).set("canEdit","1");
							focus.elementAt(i).set("canView","1");
						}
					}
				}            		
				else{
					HashMap profilePermissionMap = new HashMap();
					for(int i = 0; i < focus.size(); i++){
						profilePermissionMap.put(focus.elementAt(i).get("user_profile_id"),new ArrayList());
					}
					_log.debug("LOG_DEBUG_EXTENSION_WMSECLISTFRMPREREN","profilePermissionMapA:"+profilePermissionMap,100L);					
					for(int i = 0; i < mappingCol.size(); i++){
						Bio bio = mappingCol.elementAt(i);
						_log.debug("LOG_DEBUG_EXTENSION_WMSECLISTFRMPREREN","bio.get(user_profile_id):"+bio.get("user_profile_id"),100L);						
						if(!bio.get("user_profile_id").equals(PROFILE_NOBODY)){
							ArrayList permissions = (ArrayList)profilePermissionMap.get(bio.get("user_profile_id"));
							if(!permissions.contains(bio.get("user_permission_id"))){
								permissions.add(bio.get("user_permission_id"));
							}            				
						}
					}
					_log.debug("LOG_DEBUG_EXTENSION_WMSECLISTFRMPREREN","profilePermissionMapB:"+profilePermissionMap,100L);				
					for(int i = 0; i < focus.size(); i++){
						Bio bio = focus.elementAt(i);
						if(!profilePermissionMap.containsKey(bio.get("user_profile_id"))){
							_log.debug("LOG_DEBUG_EXTENSION_WMSECLISTFRMPREREN","Setting both to 0...",100L);							
							focus.elementAt(i).set("canEdit","0");
							focus.elementAt(i).set("canView","0");
						}
						else{
							ArrayList permissions = (ArrayList)profilePermissionMap.get(bio.get("user_profile_id"));
							if(permissions.contains(PERMISSION_ACTIVE)){
								_log.debug("LOG_DEBUG_EXTENSION_WMSECLISTFRMPREREN","setting canEdit to 1...",100L);								
								focus.elementAt(i).set("canEdit","1");
							}
							else{
								_log.debug("LOG_DEBUG_EXTENSION_WMSECLISTFRMPREREN","setting canEdit to 0...",100L);								
								focus.elementAt(i).set("canEdit","0");
							}
							if(permissions.contains(PERMISSION_VIEW)){
								_log.debug("LOG_DEBUG_EXTENSION_WMSECLISTFRMPREREN","setting canView to 1...",100L);								
								focus.elementAt(i).set("canView","1");
							}
							else{
								_log.debug("LOG_DEBUG_EXTENSION_WMSECLISTFRMPREREN","setting canView to 0...",100L);								
								focus.elementAt(i).set("canView","0");
							}
							
						}
					}
				}
				form.setFocus(focus);
			}
			
		}
		catch(BioNotFoundException bioEx)
		{            
			throw new FormException("ERROR_GET_SEL_BIO_LIST", null);
		} catch (EpiDataException e) {        	
			e.printStackTrace();
			return RET_CANCEL;
		}
		_log.debug("LOG_DEBUG_EXTENSION_WMSECLISTFRMPREREN","Exiting WmSecurityListFormPrerenderAction",100L);	
		return RET_CONTINUE;
		
	}
	
}