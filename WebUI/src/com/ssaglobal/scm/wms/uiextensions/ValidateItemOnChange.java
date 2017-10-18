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
import com.epiphany.shr.ui.view.RuntimeWidget;
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

public class ValidateItemOnChange extends ActionExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateItemOnChange.class);

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
		String formName = (String)getParameter("targetForm");
		String shellFormName = (String)getParameter("shellFormName");
		String ownerWidget = (String)getParameter("ownerWidget");
		shellFormName = shellFormName == null || shellFormName.length() == 0?"wms_list_shell":shellFormName;
		ArrayList tabs = (ArrayList)getParameter("tabs");
		RuntimeFormInterface form = null;
		StateInterface state = context.getState();
		if(formName != null && formName.length() > 0){
			form = FormUtil.findForm(state.getCurrentRuntimeForm(), shellFormName, formName, tabs,
					state);
		}		
		String owner = null;
		if(form != null){
			if(ownerWidget != null && ownerWidget.length() > 0){
				RuntimeWidget widget = (RuntimeWidget)form.getFormWidgetByName(ownerWidget);
				if(widget != null){
					owner = widget.getDisplayValue();
				}
			}
		}		
		String displayValue = context.getSourceWidget().getDisplayValue();
		if(displayValue != null){
			String sku = displayValue.trim();
//			Validate Storer Key, if present must be present in STORER table and of type 1
			if(sku != null){
				sku = sku.toUpperCase();
				//sku = sku.trim();
				if(sku.length() > 0){
					Query loadBiosQry = null;
					if(owner == null || owner.length() == 0){
						loadBiosQry = new Query("wm_sku", "wm_sku.SKU = '"+sku+"'", "");					
					}
					else{
						loadBiosQry = new Query("wm_sku", "wm_sku.SKU = '"+sku+"' AND wm_sku.STORERKEY = '"+owner+"'", "");
					}
					UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
					BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
					try {
						if(bioCollection.size() == 0){							
							RuntimeFormInterface FormName = context.getState().getCurrentRuntimeForm();
					    	String WidgetName = context.getSourceWidget().getName();
					    	String[] ErrorParem = new String[2];				     	   	
				     	    FieldException UsrExcp = null;
				     	   	if(owner == null || owner.length() == 0){
				     	   		ErrorParem[0]= displayValue;
				     	   		ErrorParem[1] = context.getSourceWidget().getLabel("label",context.getState().getUser().getLocale()).replaceAll(":","");;
				     	   		UsrExcp = new FieldException(FormName, WidgetName,"NotValidEntry", ErrorParem);
				     	   	}
				     	   	else{
				     	   		ErrorParem[0]= sku;
				     	   		ErrorParem[1] = owner;
				     	   		UsrExcp = new FieldException(FormName, WidgetName,"WMEXP_SKU_MUST_BELONG_TO_STORER", ErrorParem);
				     	   	}
				     	   	throw UsrExcp;		
						}
					} catch (EpiDataException e) {
						e.printStackTrace();		
					}
				}
			}			
		}
		return RET_CONTINUE;
	}

}
