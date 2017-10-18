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
package com.ssaglobal.scm.wms.wm_internal_transfer.ui;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class ValidateStorerInInternalTransfer extends com.epiphany.shr.ui.action.ActionExtensionBase{
    protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateStorerInInternalTransfer.class);
	protected int execute(ActionContext context, ActionResult result)
	throws EpiException, UserException
{
		final String BIO = "wm_storer";
		final String ATTRIBUTE = context.getSourceWidget().getName();
		String sourceWidgetValue= null;
		
		StateInterface state = context.getState();
		DataBean currentFormFocus = state.getFocus();
		RuntimeFormInterface form = state.getCurrentRuntimeForm();

		RuntimeFormWidgetInterface sourceWidget = context.getSourceWidget();
		String sourceWidgetName = (String) sourceWidget.getName();
		
		// Query Bio to see if Attribute exists
		String queryStatement = null;
		Query query = null;
		BioCollection results = null;

		try
		{
			if(sourceWidget.getValue() != null && !sourceWidget.getValue().toString().equalsIgnoreCase(""))
			{
			sourceWidgetValue = (String) sourceWidget.getValue();
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
			queryStatement = BIO + ".STORERKEY= '" + sourceWidgetValue.toUpperCase() + "'" + " AND " + BIO + ".TYPE = 1";			
			_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFERUPDATE","Query: " +queryStatement,100L);
			query = new Query(BIO, queryStatement, null);
			results = uow.getBioCollectionBean(query);
	
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFERUPDATE","Throwing exception- Invalid Storer",100L);
				throw new UserException("WMEXP_OWNER_VALID", new Object[1]);
			}
		
		} catch (NullPointerException e)
		{
			e.printStackTrace();
			String[] param = new String[1];
			param[0] = sourceWidgetValue;
			_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFERUPDATE","Throwing exception- Invalid Storer",100L);
			throw new UserException("WMEXP_OWNER_VALID", param);
			//throw new EpiException("QUERY_ERROR", "Error preparing search query" + queryStatement);
		}

		if (results.size() == 0)
		{
		String[] param = new String[1];
		param[0] = sourceWidgetValue;
		throw new UserException("WMEXP_OWNER_VALID", param);
		}
		
		// If BioCollection size equals 0, return RET_CANCEL
	
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFERUPDATE","Exiting ValidateStorerInInternalTransfer",100L);
		return RET_CONTINUE;
}
}
