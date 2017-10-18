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
package com.ssaglobal.scm.wms.wm_routing.ui;

//Import 3rd party packages and classes
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Import Epiphany packages and classes
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class PreSaveValidatePK extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PreSaveValidatePK.class);
	
	private String table, widgetName, valString;
	private boolean isHeader;
	private StateInterface state;
	private SlotInterface slot;
	private RuntimeFormInterface form, shellForm;
	private RuntimeFormWidgetInterface key;
	private DataBean focus;
	
	protected int execute(ActionContext context, ActionResult result) throws UserException, EpiException{
		_log.debug("LOG_DEBUG_EXTENSION_PRESAVE_PK_VALIDATION","**Executing PreSaveValidatePK",100L);
		state = context.getState();		
		shellForm = state.getCurrentRuntimeForm().getParentForm(state);
		widgetName = getParameterString("widgetName");
		table = getParameterString("tableName");
		isHeader = getParameterBoolean("isHeader");
		boolean skipSpecialCharCheck = getParameterBoolean("skipSpecialCharCheck");

		if(isHeader){
			getContextInfo("list_slot_1");
		}else{
			getContextInfo("list_slot_2");
		}

		if(focus.isTempBio()){
			key = form.getFormWidgetByName(widgetName);		
			Object val = key.getValue();
			if(val!=null){
				valString = val.toString();
				if(skipSpecialCharCheck){
					checkDuplicate();
				}else{
					if(Pattern.matches("[^'\",\\\\<>&; ]*", valString)){
						checkDuplicate();
					}else{
						_log.debug("LOG_DEBUG_EXTENSION_PRESAVE_PK_VALIDATION","**Special Chars found in PK- Exiting",100L);						
						buildError("WMEXP_SP_CHARS");
					}
				}
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_PRESAVE_PK_VALIDATION","**Leaving PreSaveValidatePK ",100L);
		return RET_CONTINUE;
	}
	
	private void buildError(String message) throws UserException{
		String [] param = new String[2];
		param[0] = valString;
		param[1] = colonStrip(readLabel(key));
		throw new UserException(getTextMessage(message,param,state.getLocale()),new Object[0]);
	}
	
	private void checkDuplicate() throws UserException, EpiDataException{
		//check for duplicates
		String query = "SELECT "+widgetName+" FROM "+table+" WHERE "+widgetName+"='"+valString.toUpperCase()+"'";
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if(results.getRowCount()>0){
			_log.debug("LOG_DEBUG_EXTENSION_PRESAVE_PK_VALIDATION","**Duplicate PK- Exiting",100L);
			buildError("WMEXP_DUPKEY");
		}
	}
	
	private void getContextInfo(String slotName){
		slot = shellForm.getSubSlot(slotName);
		form = state.getRuntimeForm(slot, null);
		focus = form.getFocus();
	}
	
	//Remove colons from label values
	private String colonStrip(String label){
		Pattern pattern = Pattern.compile("\\:");
		Matcher matcher = pattern.matcher(label);
		return 	matcher.replaceAll("");
	}
	
	//Find label value base on locale
	private String readLabel(RuntimeFormWidgetInterface widgetName){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widgetName.getLabel("label",locale);
	}
}