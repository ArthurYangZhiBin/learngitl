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

package com.ssaglobal.scm.wms.wm_sortationstation.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.logging.*;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ValidateSortLocation extends ActionExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateSortLocation.class);

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
			String location = displayValue.trim().toUpperCase();
			if(location != null && location.trim().length() > 0){
				Query loadBiosQry = new Query("wm_location", "wm_location.LOC = '"+location.toUpperCase()+"' and wm_location.LOCATIONTYPE = 'SORT'", null);
				UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
				BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
				if(bioCollection.size() == 0){
					RuntimeFormInterface FormName = context.getState().getCurrentRuntimeForm();
			    	String WidgetName = context.getSourceWidget().getName();
			    	String[] ErrorParem = new String[2];
		     	   	ErrorParem[0]= displayValue;
		     	   	ErrorParem[1] = context.getSourceWidget().getLabel("label",context.getState().getUser().getLocale()).replaceAll(":","");;
		     	   	FieldException UsrExcp = new FieldException(FormName, WidgetName,"NotValidEntry", ErrorParem);
		     	   	throw UsrExcp;
				}
			}	
			QBEBioBean focus = (QBEBioBean)context.getState().getFocus();
			focus.set("SORTATIONSTATIONKEY", location);
			result.setFocus(focus);
		}
		return RET_CONTINUE;
	}

}