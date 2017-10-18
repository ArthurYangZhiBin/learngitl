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

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.logging.*;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.epiphany.shr.ui.state.StateInterface;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ValidateOwnerOnChange extends ActionExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateOwnerOnChange.class);

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
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{

		_log.debug("LOG_DEBUG_EXTENSION_ValidateNoSpace", "Executing ValidateNoSpace", 100L);		
		String displayValue = context.getSourceWidget().getDisplayValue();
		if(displayValue != null){
			String storerKey = displayValue.trim().toUpperCase();
//			Validate Storer Key, if present must be present in STORER table and of type 1
			if(storerKey.length() > 0){
				try {
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATEWSDEFAULT","Validating STORERKEY...",100L);					
					Query loadBiosQry = new Query("wm_storer", "wm_storer.STORERKEY = '"+storerKey.toUpperCase()+"' AND wm_storer.TYPE = '1'", "");
					UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
					BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
					if(bioCollection == null || bioCollection.size() == 0){
						RuntimeFormInterface FormName = context.getState().getCurrentRuntimeForm();
				    	String WidgetName = context.getSourceWidget().getName();
				    	String[] ErrorParem = new String[2];
			     	   	ErrorParem[0]= displayValue;
			     	   	ErrorParem[1] = context.getSourceWidget().getLabel("label",context.getState().getUser().getLocale()).replaceAll(":","");;
			     	   	FieldException UsrExcp = new FieldException(FormName, WidgetName,"NotValidEntry", ErrorParem);
			     	   	throw UsrExcp;						
					}
				} catch (EpiDataException e) {										
					e.printStackTrace();					
				} 
			}
		}
		return RET_CONTINUE;
	}

}
