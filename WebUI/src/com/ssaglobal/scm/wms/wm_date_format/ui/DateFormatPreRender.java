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
import java.util.GregorianCalendar;

import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.ui.exceptions.FormException;
import com.ssaglobal.scm.wms.wm_cyclecount.ui.PopulateCCDetailSerialTmp;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class DateFormatPreRender extends com.epiphany.shr.ui.action.ActionExtensionBase {


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
	protected static ILoggerCategory _log = LoggerFactory.getInstance(DateFormatPreRender.class);
	protected int execute( ActionContext context, ActionResult result ) throws EpiException 
	{
		try 
		{
			StateInterface state = context.getState();
			RuntimeFormInterface headerForm = state.getCurrentRuntimeForm();
			_log.debug("LOG_SYSTEM_OUT",headerForm.getName(),100L);
			
            String bioRefString = state.getBucketValueString("listTagBucket");
            BioRef bioRef = BioRef.createBioRefFromString(bioRefString);
            UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
            com.epiphany.shr.ui.model.data.BioBean bioBean = null;

            try
            {
                   bioBean = uowb.getBioBean(bioRef);
                   result.setFocus(bioBean);
            } 
            catch (BioNotFoundException bioEx)
            {
                   _logger.error(bioEx);
                   throw new FormException("ERROR_GET_SEL_BIO_LIST", null);
            }

			RuntimeFormInterface shellForm = headerForm.getParentForm(state);
			SlotInterface slot = shellForm.getSubSlot("wms_list_shell Toolbar");
			RuntimeFormInterface toolbar = state.getRuntimeForm(slot, null);
            
			String dateCode = (String)bioBean.getValue("DATECODEFORMAT");
			if ( dateCode != null )
			{
				String dateFormatQuery = "wm_dateformatdetail.DATECODEFORMAT = '"+dateCode+"' ";
				Query bioQuery = new Query("wm_dateformatdetail", dateFormatQuery, null);
				UnitOfWorkBean uow = state.getDefaultUnitOfWork();
				BioCollectionBean list = uow.getBioCollectionBean(bioQuery);
				
				boolean haveYear = false;
				boolean haveMonth = false;
				boolean haveDay = false;
				String dateType = "";
				
				for ( int idx = 0; idx < list.size(); ++idx)
				{
					BioBean row = (BioBean)list.elementAt(idx);
					
					haveMonth = row.getString("DATEELEMENT").equals("M") ? true : haveMonth;
					haveYear = row.getString("DATEELEMENT").equals("Y") ? true : haveYear;
					haveDay = row.getString("DATEELEMENT").equals("D") ? true : haveDay;
					if (row.getString("DATETYPE").equals("J") || row.getString("DATETYPE").equals("G"))
					{
						dateType = row.getString("DATETYPE");
					}
				}
			
				if ( (dateType.equals("J") && haveDay && haveYear) || (dateType.equals("G") && haveDay && haveYear && haveMonth) )
				{			
					_log.debug("LOG_SYSTEM_OUT","ENABLE TEST",100L);
					toolbar.getFormWidgetByName("TEST").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					toolbar.getFormWidgetByName("ACTIVATE").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					
					if ( ((Integer)toolbar.getFormWidgetByName("TEST").getProperty(RuntimeFormWidgetInterface.PROP_READONLY)) == 1)
					{
						_log.debug("LOG_SYSTEM_OUT","TEST is readonly",100L);
					}
					else
					{
						_log.debug("LOG_SYSTEM_OUT","TEST is not readonly",100L);
					}
				}
				else
				{
					_log.debug("LOG_SYSTEM_OUT","DISABLE TEST",100L);
					toolbar.getFormWidgetByName("TEST").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					toolbar.getFormWidgetByName("ACTIVATE").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
				}				
			}	

			GregorianCalendar date = (GregorianCalendar)bioBean.getValue("CONVERTEDDATE");
			if ( date == null )
			{
				toolbar.getFormWidgetByName("ACTIVATE").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
				toolbar.getFormWidgetByName("DEACTIVATE").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
			}
			else
			{
				String dateCodeIsActive = bioBean.getValue("ACTIVE").toString();
				
				if (dateCodeIsActive.equals("1") )
				{
					toolbar.getFormWidgetByName("ACTIVATE").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					toolbar.getFormWidgetByName("DEACTIVATE").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
				}
				else
				{
					toolbar.getFormWidgetByName("ACTIVATE").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					toolbar.getFormWidgetByName("DEACTIVATE").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
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
