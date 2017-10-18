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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class ValidatePackOnChange extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	 protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidatePackOnChange.class);
	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @exception EpiException
	 */
	protected int execute(ActionContext context, ActionResult result)
			throws EpiException, UserException
	{
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFERUPDATE","Executing ValidatePackOnChange",100L);
		RuntimeFormWidgetInterface sourceWidget = context.getSourceWidget();
		String sourceWidgetValue = (String) sourceWidget.getValue();
		

		// Query to see if Attribute exists

		String sql ="select * from pack where packkey" +"='"+sourceWidgetValue+"'";
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFERUPDATE","Query: " +sql,100L);
		EXEDataObject dataObject;
		try {
			dataObject = WmsWebuiValidationSelectImpl.select(sql);
			if(dataObject.getCount()<= 0)
			{
				_log.debug("LOG_SYSTEM_OUT","\nERROR",100L);
				String[] param = new String[1];
				param[0]= sourceWidgetValue;
				_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFERUPDATE","Throwing Exception: Invalid PAckkey",100L);
				throw new FormException("WMEXP_PACK", param);
				
			}
		} catch (DPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new EpiException("QUERY_ERROR", "Error preparing search query" + sql);
		}
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFERUPDATE","Exiting ValidatePackOnChange",100L);
		return RET_CONTINUE;
	}
	


}
