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
package com.ssaglobal.scm.wms.formextensions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.EpiDataReadUninitializedException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.util.SecurityUtil;

public class CheckPermissionAndDisableWidgets extends FormExtensionBase
{
	public CheckPermissionAndDisableWidgets()
    {
    }
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CheckPermissionAndDisableWidgets.class);
	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) throws EpiException {
		StateInterface state = context.getState();
		List userProfiles = state.getServiceManager().getUserContext().getUserProfiles();		
		ArrayList warehouseEditablePermissions = (ArrayList)getParameter("warehousePermissions");
		ArrayList enterpriseEditablePermissions = (ArrayList)getParameter("enterprisePermissions");
		ArrayList doNotDisableList = (ArrayList)getParameter("doNotDisableList");
		String dbConn = (String)state.getRequest().getSession().getAttribute(SetIntoHttpSessionAction.DB_CONNECTION);
		
		_log.debug("LOG_DEBUG_EXTENSION_CHKPERMANDDIS","Checking For Edit Permissions...",100L);
		//If user has editing permissions then do nothing...
		
		if(SecurityUtil.isAdmin(state))
			return RET_CONTINUE;
		
		if(dbConn.equalsIgnoreCase("enterprise")){
			_log.debug("LOG_DEBUG_EXTENSION_CHKPERMANDDIS","connection is enterprise...",100L);
			for(int i = 0; i < enterpriseEditablePermissions.size(); i++){
				if(userProfiles.contains(enterpriseEditablePermissions.get(i))){
					_log.debug("LOG_DEBUG_EXTENSION_CHKPERMANDDIS","User Has Enterprise Editing Permissions...",100L);
					return RET_CONTINUE;
				}
			}
		}
		else{
			_log.debug("LOG_DEBUG_EXTENSION_CHKPERMANDDIS","connection is warehouse...",100L);
			for(int i = 0; i < warehouseEditablePermissions.size(); i++){
				if(userProfiles.contains(warehouseEditablePermissions.get(i))){					
					_log.debug("LOG_DEBUG_EXTENSION_CHKPERMANDDIS","User Has Warehouse Editing Permissions...",100L);
					return RET_CONTINUE;
				}
			}
		}
		
		_log.debug("LOG_DEBUG_EXTENSION_CHKPERMANDDIS","editing permissions not found, disabling widgets...",100L);
		//Else disable all widgets except the ones listed in doNotDisableList
		Iterator widgetItr = form.getFormWidgets();
		while(widgetItr.hasNext()){
			RuntimeFormWidgetInterface widget = (RuntimeFormWidgetInterface)widgetItr.next();
			if(doNotDisableList != null){
				if(!doNotDisableList.contains(widget.getName())){
					_log.debug("LOG_DEBUG_EXTENSION_CHKPERMANDDIS","disabling:"+widget.getName(),100L);
					widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
				}
			}
			else{
				_log.debug("LOG_DEBUG_EXTENSION_CHKPERMANDDIS","disabling:"+widget.getName(),100L);
				widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
			}
		}
		return RET_CONTINUE;
	}
	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
    throws UserException
    {	
		StateInterface state = context.getState();
		List userProfiles = state.getServiceManager().getUserContext().getUserProfiles();
		ArrayList warehouseEditablePermissions = (ArrayList)getParameter("warehousePermissions");
		ArrayList enterpriseEditablePermissions = (ArrayList)getParameter("enterprisePermissions");
		ArrayList doNotDisableList = (ArrayList)getParameter("doNotDisableList");
		String dbConn = (String)state.getRequest().getSession().getAttribute(SetIntoHttpSessionAction.DB_CONNECTION);
		
		if(SecurityUtil.isAdmin(state))
			return RET_CONTINUE;
		
		_log.debug("LOG_DEBUG_EXTENSION_CHKPERMANDDIS","Checking For Edit Permissions...",100L);
		//If user has editing permissions then do nothing...
		if(dbConn.equalsIgnoreCase("enterprise")){
			_log.debug("LOG_DEBUG_EXTENSION_CHKPERMANDDIS","connection is enterprise...",100L);
			for(int i = 0; i < enterpriseEditablePermissions.size(); i++){
				if(userProfiles.contains(enterpriseEditablePermissions.get(i))){
					_log.debug("LOG_DEBUG_EXTENSION_CHKPERMANDDIS","User Has Enterprise Editing Permissions...",100L);
					return RET_CONTINUE;
				}
			}
		}
		else{
			_log.debug("LOG_DEBUG_EXTENSION_CHKPERMANDDIS","connection is warehouse...",100L);
			for(int i = 0; i < warehouseEditablePermissions.size(); i++){
				if(userProfiles.contains(warehouseEditablePermissions.get(i))){					
					_log.debug("LOG_DEBUG_EXTENSION_CHKPERMANDDIS","User Has Warehouse Editing Permissions...",100L);
					return RET_CONTINUE;
				}
			}
		}
		
		_log.debug("LOG_DEBUG_EXTENSION_CHKPERMANDDIS","editing permissions not found, disabling widgets...",100L);
		//Else disable all widgets except the ones listed in doNotDisableList
		Iterator widgetItr = form.getFormWidgets();
		while(widgetItr.hasNext()){
			RuntimeFormWidgetInterface widget = (RuntimeFormWidgetInterface)widgetItr.next();
			if(doNotDisableList != null){
				if(!doNotDisableList.contains(widget.getName())){
					_log.debug("LOG_DEBUG_EXTENSION_CHKPERMANDDIS","disabling:"+widget.getName(),100L);
					widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
				}
			}
			else{
				_log.debug("LOG_DEBUG_EXTENSION_CHKPERMANDDIS","disabling:"+widget.getName(),100L);
				widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
			}
		}
		return RET_CONTINUE;
	}
		
}