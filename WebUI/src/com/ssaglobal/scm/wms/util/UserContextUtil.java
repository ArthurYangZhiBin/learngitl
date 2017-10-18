/**
 * ---Begin Copyright Notice---20090105T1353
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2010 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */
package com.ssaglobal.scm.wms.util;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.state.StateInterface;

/**
 * The Class UserContextUtil.
 */
public class UserContextUtil {

	static public void setInteractionUserContextAttribute(String key, Object value, StateInterface state)
	{
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		HashMap interactionSession = (HashMap) userContext.get(state.getInteractionId());
		if (interactionSession == null)
		{
			interactionSession = new HashMap();
		}
		interactionSession.put(key, value);
		userContext.put(state.getInteractionId(), interactionSession);
	}

	static public Object getInteractionUserContextAttribute(String key, StateInterface state)
	{
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		HashMap interactionSession = (HashMap) userContext.get(state.getInteractionId());
		if (interactionSession == null)
		{
			return null;
		}
		return interactionSession.get(key);
	}

	
}
