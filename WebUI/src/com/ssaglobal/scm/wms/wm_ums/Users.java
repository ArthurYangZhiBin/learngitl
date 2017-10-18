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


public class Users implements WmsUmsInterface{
	public ArrayList<String> getAllUserIds() throws InvalidData,
									    NoServerAvailableException,
									    RemoteException{
		return null;
	}
	public ArrayList<String> getUserIdsForSearch(String firstName, String lastName) throws EpiException{
		return null;//for Indirectory activity
	}
	public User getUser(String userId){
		return null;
	}
	public HashMap getUsers(ActionContext context, ActionResult result) throws Exception{
		return null;
	}
	public HashMap getUsers(ModalActionContext ctx, ActionResult args) throws Exception{
		return null;
	}
	public HashMap getUsers(ActionContext context, ActionResult result, EXEDataObject collection){
		return null;
	}
	public HashMap getUsers(UIRenderContext context, RuntimeListFormInterface form) throws EpiException{
		return null;
	}
	public boolean isUserIdExist(String userId){
		return false;
	}
	public User getUser(String userId, StateInterface state){
		return null;
	}
	
	
	public boolean containsWildCards(String value)
	{
		if (value.indexOf('*') != -1 || value.indexOf('%') != -1 || value.indexOf('?') != -1
				|| value.indexOf('_') != -1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	String stripWildCards(String value)
	{
		String result = value;
		result = result.replaceAll("(\\*|%|\\?|_)", "");
		return result;

	}
	public HashMap<String, String> getTaskManagerUsers()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getCurrentUserId(StateInterface state) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
}
