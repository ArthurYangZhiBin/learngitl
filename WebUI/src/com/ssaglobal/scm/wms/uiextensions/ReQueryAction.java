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
package com.ssaglobal.scm.wms.uiextensions;

import java.util.ArrayList;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.FormUtil;

public class ReQueryAction extends ActionExtensionBase {
	

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ReQueryAction.class);
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException {

		String listFormParameter = getParameterString("ListForm");
		ArrayList<String> tabs = (ArrayList<String>) getParameter("Tabs");
		String sortOrder = getParameterString("Order");
		
		StateInterface state = context.getState();
		RuntimeFormInterface form = FormUtil.findForm(state.getCurrentRuntimeForm(), "", listFormParameter, tabs, state);
		if (form != null && form.isListForm()) {
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			RuntimeListForm listForm = (RuntimeListForm) form;
			QBEBioBean filter = listForm.getFilterRowBean();
			String query = null;
			
			BioCollectionBean collection = uowb.getBioCollectionBean(new Query(filter.getDataType(), query, sortOrder));
			_log.debug(	"LOG_DEBUG_EXTENSION_ReQueryAction",
						"Created QBEBioBean of Type:" + collection.getBioTypeName() + "..." + "Size " + collection.size(),
						SuggestedCategory.NONE);
			BioCollectionBean newCollection = (BioCollectionBean) collection.filter(filter.getQueryWithWildcards());
			BioCollectionBean newCollectionB = (BioCollectionBean) collection.filter(filter.getQueryWithWildcards());
			newCollectionB.copyFrom(collection);
			collection.copyFrom(newCollection);
			newCollection = collection;
			filter.setBaseBioCollectionForQuery(newCollectionB);
			newCollection.setQBEBioBean(filter);
			newCollection.filterInPlace(filter.getQueryWithWildcards());
			result.setFocus(newCollection);
		}

		// TODO Auto-generated method stub
		return RET_CONTINUE;
	}
}
