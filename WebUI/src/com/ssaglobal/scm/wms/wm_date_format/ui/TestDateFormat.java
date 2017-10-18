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

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.ssaglobal.scm.wms.service.dutilitymanagement.DateFormatCustomDAO;
import com.ssaglobal.scm.wms.service.dutilitymanagement.DateFormatDetailDAO;
import com.ssaglobal.scm.wms.service.dutilitymanagement.datecodeformat.DateFormatConversion;
import com.ssaglobal.scm.wms.service.dutilitymanagement.datecodeformat.DateFormatCustomDTO;
import com.ssaglobal.scm.wms.service.dutilitymanagement.datecodeformat.DateFormatDetailDTO;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class TestDateFormat extends com.epiphany.shr.ui.action.ActionExtensionBase {


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
	protected static ILoggerCategory _log = LoggerFactory.getInstance(TestDateFormat.class);
	protected int execute( ActionContext context, ActionResult result ) throws EpiException 
	{
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();			
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		SlotInterface header = shellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(header, null);
		DataBean headerFocus = headerForm.getFocus();
		
		// does exmaple widget contain anything?
		String exampleValue = (String)headerFocus.getValue("EXAMPLEDATE");
		if ( exampleValue == null || exampleValue.matches("\\s*") )
		{
			throw new UserException("WM_DF_EXAMPLEBLANK", new Object[0]);
		}

		GregorianCalendar calendar = null;   
        ArrayList<DateFormatCustomDTO> dateFormatCustomList = null;        
        ArrayList<DateFormatDetailDTO> dateFormatDetails = DateFormatDetailDAO.getDateFormatDetail(context, (String)headerFocus.getValue("DATECODEFORMAT") );
 		
        boolean haveMonth = false;
        boolean haveYear = false;
        boolean haveDay = false;
        String dateType = "";
        if ( dateFormatDetails.size() > 0 )
        {
        	dateType = dateFormatDetails.get(0).getDateType();
        }
        
		// do we have all the proper date elements set yet?
        for ( int idx = 0; idx < dateFormatDetails.size(); ++idx)
        {
        	haveMonth = dateFormatDetails.get(idx).getDateElement().equals("M") ? true : haveMonth;
           	haveYear = dateFormatDetails.get(idx).getDateElement().equals("Y") ? true : haveYear;
           	haveDay = dateFormatDetails.get(idx).getDateElement().equals("D") ? true : haveDay;       	
        }
        
        for ( int idx = 0; idx < dateFormatDetails.size(); ++idx)
        {       
        	if ( ( dateFormatDetails.get(idx).getDateType().equals("G") && !(haveMonth&&haveYear&&haveDay)) ||
        		 ( dateFormatDetails.get(idx).getDateType().equals("J") && !(haveYear&&haveDay)) )
        	{
           		throw new UserException("WMEXP_DF_NOTCONFIGURED",new Object[0]);
        	}
        }
        BioCollectionBean listCollection = null;
    	String sQueryString = "(wm_dateformat.DATECODEFORMAT = '"+(String)headerFocus.getValue("DATECODEFORMAT")+"' ) ";
    	Query bioQuery = new Query( "wm_dateformat", sQueryString, null );
    	listCollection = uowb.getBioCollectionBean(bioQuery);
    		
    	for ( int idx = 0; idx < listCollection.size(); ++idx ) 
    	{
    		_log.debug("LOG_SYSTEM_OUT","idx/size :"+idx +"/"+listCollection.size()+":",100L);
            BioBean row = (BioBean)listCollection.elementAt(idx);     	

    		if ( row.getString("CUSTOMCODEFORMAT") == null )
            {
            	dateFormatCustomList = null;
            }
            else
            {
            	dateFormatCustomList = DateFormatCustomDAO.getDateFormatCustoms(context, row.getString("CUSTOMCODEFORMAT"));
            }            
     	}

        DateFormatConversion converter = new DateFormatConversion();
        _log.debug("LOG_SYSTEM_OUT",exampleValue,100L);
        _log.debug("LOG_SYSTEM_OUT",(String)headerFocus.getValue("DATECODEFORMAT"),100L);
        calendar = converter.setDateFromCodeFormat(exampleValue, (String)headerFocus.getValue("DATECODEFORMAT"), dateFormatDetails, dateFormatCustomList);
		
        if ( calendar != null )
        {
        	_log.debug("LOG_SYSTEM_OUT", calendar.getTime().toString() ,100L);
 			headerFocus.setValue("CONVERTEDDATE", calendar);
 			toolbar.getFormWidgetByName("ACTIVATE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);								
			uowb.saveUOW(true);
        }
        else
        {
        	headerForm.getFormWidgetByName("EXAMPLEDATE").setProperty("cursor focus widget","true");		
        	throw new UserException("WMEXP_INVALIDDATESTRING", new Object[0]);
        }		
        
        // verify the length in example widget against highest date code format.

		// convert the string and display the string to converted IF it converted well.
		return RET_CONTINUE;
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
    protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {

       try {
           // Add your code here to process the event
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }
}
