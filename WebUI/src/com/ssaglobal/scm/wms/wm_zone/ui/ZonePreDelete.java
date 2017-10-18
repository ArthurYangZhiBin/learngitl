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
package com.ssaglobal.scm.wms.wm_zone.ui;

import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.exceptions.FormException;
import java.util.ArrayList;
import java.util.Iterator;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
public class ZonePreDelete extends ActionExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ZonePreDelete.class);
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		String queryString = null;
		Query qry = null;
		BioCollectionBean testList = null;
		String[] parameter = new String[1];

		RuntimeListFormInterface form = (RuntimeListFormInterface) state.getRuntimeForm(state.getCurrentRuntimeForm().getParentForm(state).getSubSlot("list_slot_1"), null);

		ArrayList items;
		try
		{
			items = form.getAllSelectedItems();
			if (items.isEmpty())
			{
				throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
			}
		} catch (NullPointerException e)
		{
			throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
		}
		for (Iterator it = items.iterator(); it.hasNext();)
		{
			DataBean key = (DataBean) it.next();
			String keyValue = key.getValue("PUTAWAYZONE") == null ? null : key.getValue("PUTAWAYZONE").toString().toUpperCase();
			parameter[0] = keyValue;
			queryString = "wm_location.PUTAWAYZONE='" + keyValue + "'";
			qry = new Query("wm_location", queryString, null);
			_log.debug("LOG_DEBUG_EXTENSION_ZonePreDelete", qry.getQueryExpression(), SuggestedCategory.NONE);
			testList = uow.getBioCollectionBean(qry);
			if (testList != null)
			{
				if (testList.size() != 0)
				{
					throw new FormException("WMEXP_ZONE_FKLOC", parameter);
				}
			}

			queryString = "wm_pazoneequipmentexcludedetail.PUTAWAYZONE='" + keyValue + "'";
			qry = new Query("wm_pazoneequipmentexcludedetail", queryString, null);
			_log.debug("LOG_DEBUG_EXTENSION_ZonePreDelete", qry.getQueryExpression(), SuggestedCategory.NONE);
			testList = uow.getBioCollectionBean(qry);
			if (testList != null)
			{
				if (testList.size() != 0)
				{
					throw new FormException("WMEXP_ZONE_FKEFZ", parameter);
				}
			}
			
			//checking if user has already deleted all zonestds			
			queryString = "wm_zonestds.PUTAWAYZONE='" + keyValue + "'";
			qry = new Query("wm_zonestds", queryString, null);
			testList = uow.getBioCollectionBean(qry);
			if (testList != null)
			{
				if (testList.size() != 0)
				{
					throw new FormException("WMEXP_ZONE_STDS", parameter);
				}
			}
			
		}

		return RET_CONTINUE;
	}
}