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
package com.ssaglobal.scm.wms.wm_codes.ui;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.EpnyControllerState;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.epiphany.shr.data.bio.Query;

/**
 * TODO Document ReturnToListEnterprise class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class ReturnToListEnterprise extends ActionExtensionBase{
	private static final String IS_ENTERPRISE_USER = "1";
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		EpnyControllerState state = (EpnyControllerState) context.getState();
		HttpSession session = state.getRequest().getSession();

		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		String isEnterprise = null;
		try
		{
			isEnterprise = userContext.get(SetIntoHttpSessionAction.DB_ISENTERPRISE).toString();
		} catch (java.lang.NullPointerException e)
		{
			isEnterprise = session.getAttribute(SetIntoHttpSessionAction.DB_ISENTERPRISE).toString();
		}

		if (IS_ENTERPRISE_USER.equalsIgnoreCase(isEnterprise)){//only for enterprise user
			Query qry = new Query("wm_codes", "wm_codes.ENTERPRISECODE='1'", null);
			UnitOfWorkBean uow = state.getTempUnitOfWork();

			BioCollectionBean rs = uow.getBioCollectionBean(qry);
			result.setFocus(rs);		
		}else{//warehouse user
			Query qry = new Query("wm_codes", null, null);
			UnitOfWorkBean uow = state.getTempUnitOfWork();

			BioCollectionBean rs = uow.getBioCollectionBean(qry);
			result.setFocus(rs);		
			
		}
		return RET_CONTINUE;
	}

}
