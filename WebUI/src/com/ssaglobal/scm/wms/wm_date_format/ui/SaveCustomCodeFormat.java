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
 

package com.ssaglobal.scm.wms.wm_date_format.ui;

import java.util.Formatter;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationInsertImpl;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class SaveCustomCodeFormat extends com.epiphany.shr.ui.action.ActionExtensionBase 
{
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
	protected static ILoggerCategory _log = LoggerFactory.getInstance(SaveCustomCodeFormat.class);
	protected int execute( ActionContext context, ActionResult result ) throws EpiException 
	{
		return super.execute( context, result );
	}

	/**
	* Fires in response to a UI action event, such as when a widget is clicked or
	* a value entered in a form in a modal dialog
	* Write code here if u want this to be called when the UI Action event is fired from a modal window
	* <ul>
	* <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext} exposes information about the
	* event, including the service and the user interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	* <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes information about the results
	* of the action that has occurred, and enables your extension to modify them.</li>
	* </ul>
	*/
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException 
	{
		try 
		{
			_log.debug("LOG_SYSTEM_OUT","***** SAVE CLICKED *****",100L);
			StateInterface state = ctx.getState();

			String modalFormName = ctx.getModalBodyForm(0).getName();
			_log.debug("LOG_SYSTEM_OUT","***** modal " + modalFormName + " *****",100L);   

			RuntimeFormInterface shellForm = state.getCurrentRuntimeForm().getParentForm(state);
			RuntimeFormInterface form = state.getRuntimeForm(shellForm.getSubSlot(modalFormName),"Detail");

			String detailToggleName = form.getName();
			_log.debug("LOG_SYSTEM_OUT","***** detail " + detailToggleName + " *****",100L); 

			String customCodeFormat = (String) form.getFormWidgetByName("CUSTOMCODEFORMAT").getValue();
			RuntimeFormWidgetInterface [] monthDesc = new RuntimeFormWidgetInterface[12];
			RuntimeFormWidgetInterface [] monthNumber = new RuntimeFormWidgetInterface[12];

			if (  customCodeFormat == null || customCodeFormat.trim().length() == 0 )
			{
				_log.debug("LOG_SYSTEM_OUT","["+customCodeFormat+"]",100L);
				throw new UserException("WMEXP_DF_DESCISBLANK", new Object[0]);
			}	

			if ( customCodeFormat.trim().length() > 50 )
			{
				_log.debug("LOG_SYSTEM_OUT","["+customCodeFormat+"]",100L);
				throw new UserException("WMEXP_DF_DESCTOOLONG", new Object[0]);
			}	

			// perform validations on the month fields
			for(	int i = 0; i < 12; i++)
			{
				// populate the month arrays from the 12 form widgets
				//
				Formatter monthDescFmt = new Formatter();
				Formatter monthNumberFmt = new Formatter();

				monthDescFmt.format("MONTHDESCRIPTION%02d", i+1);
				monthNumberFmt.format("MONTHELEMENT%02d", i+1); 
				monthDesc[i] = form.getFormWidgetByName(monthDescFmt.toString());
				monthNumber[i] = form.getFormWidgetByName(monthNumberFmt.toString());

				_log.debug("LOG_SYSTEM_OUT","**** MONTH = " + monthNumberFmt + "/" + monthDescFmt,100L);
				String monthDescStr = (String) monthDesc[i].getValue();

				if ( monthDescStr == null || monthDescStr.length() == 0 )
				{
					// all month fields must be filled in
					_log.debug("LOG_SYSTEM_OUT","["+monthDescStr+"]",100L);
					throw new UserException("WMEXP_DF_DESCISBLANK", new Object[0]);
				}   	

				if ( monthDescStr.length() > 50 )
				{
					_log.debug("LOG_SYSTEM_OUT","["+monthDescStr+"]",100L);
					throw new UserException("WMEXP_DF_DESCTOOLONG", new Object[0]);
				}	
			}	

			// validation passed; perform update or insert
			for(	int i = 0; i < 12; i++)
			{
				EXEDataObject dataSelectObject;
				
				String query = "SELECT MONTHDESCRIPTION, MONTHELEMENT FROM dateformatcustom WHERE (customcodeformat = '"
				+customCodeFormat+"' and monthelement = " + (i+1) +")";	

				// first check to see if this month element exists
				dataSelectObject = WmsWebuiValidationSelectImpl.select(query);
				if(dataSelectObject.getCount() < 1)
				{
					// if not, insert it
					String insertQry= "INSERT INTO dateformatcustom (CUSTOMCODEFORMAT, MONTHELEMENT, MONTHDESCRIPTION ) "+
									  "VALUES ('"+customCodeFormat +"', " + (i+1) +",'"+monthDesc[i].getDisplayValue()+"')";
					WmsWebuiValidationInsertImpl.insert(insertQry);
				}
				else
				{
					// if it does exist, update it
					String updateQry= "UPDATE dateformatcustom SET MONTHDESCRIPTION = '"+monthDesc[i].getDisplayValue()+
									  "' WHERE ( CUSTOMCODEFORMAT = '" + customCodeFormat + "' AND MONTHELEMENT = " + (i+1) + ")";
					WmsWebuiValidationSelectImpl.select(updateQry);
				}
			}
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
			return RET_CANCEL;          
		} 

		return RET_CONTINUE;
	}
}

