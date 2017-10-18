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
package com.ssaglobal.scm.wms.util;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.ui.state.StateInterface;

public class SessionUtil
{

	static public void setInteractionSessionAttribute(String key, Object value, StateInterface state)
	{
		HttpSession session = state.getRequest().getSession();
		HashMap interactionSession = (HashMap) session.getAttribute(state.getInteractionId());
		if (interactionSession == null)
		{
			interactionSession = new HashMap();
		}
		interactionSession.put(key, value);
		session.setAttribute(state.getInteractionId(), interactionSession);
	}

	static public Object getInteractionSessionAttribute(String key, StateInterface state)
	{
		HttpSession session = state.getRequest().getSession();
		HashMap interactionSession = (HashMap) session.getAttribute(state.getInteractionId());
		if (interactionSession == null)
		{
			return null;
		}
		return interactionSession.get(key);
	}

}
