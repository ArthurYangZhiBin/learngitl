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
package com.ssaglobal.scm.wms.wm_ums;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import com.epiphany.shr.sso.exception.InvalidData;
import com.epiphany.shr.sso.exception.NoServerAvailableException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public interface WmsUmsInterface
{
	public ArrayList<String> getAllUserIds() throws InvalidData, NoServerAvailableException, RemoteException;
	
	public HashMap<String, String> getTaskManagerUsers();

	public ArrayList<String> getUserIdsForSearch(String firstName, String lastName) throws EpiException;//for Indirectory activity

	public User getUser(String userId);

	public User getUser(String userId, StateInterface state);

	public HashMap getUsers(ActionContext context, ActionResult result) throws Exception;

	public HashMap getUsers(ModalActionContext ctx, ActionResult args) throws Exception;

	public HashMap getUsers(ActionContext context, ActionResult result, EXEDataObject collection);

	public HashMap getUsers(UIRenderContext context, RuntimeListFormInterface form) throws EpiException;

	public boolean isUserIdExist(String userId);
	
	public String getCurrentUserId(StateInterface state);
}
