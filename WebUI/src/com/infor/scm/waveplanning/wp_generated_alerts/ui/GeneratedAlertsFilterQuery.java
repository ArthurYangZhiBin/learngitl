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
package com.infor.scm.waveplanning.wp_generated_alerts.ui;

import java.util.Calendar;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.infor.scm.waveplanning.common.util.WPFormUtil;



public class GeneratedAlertsFilterQuery extends ActionExtensionBase{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(GeneratedAlertsFilterQuery.class);
	
	public static String BIO = "wp_eventlog";
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		Calendar valFrom = null;
		Calendar valTo = null;
		BioCollectionBean focus = null;
		BioCollectionBean dateFocus = null;
		long dateFromMil, dateToMil;
		String fromQueryString = "";
		String toQueryString = "";
		String queryString = "";
		
		StateInterface state = context.getState();
		
		_log.debug("LOG_DEBUG_EXTENSION_GeneratedAlertsFilterQuery", "***Executing GeneratedAlertsFilterQuery", SuggestedCategory.NONE);
		
		try
		{
			if(state.getServiceManager().getUserContext().containsKey("EventDateFrom"))
			{
				valFrom = (Calendar)state.getServiceManager().getUserContext().get("EventDateFrom");
				System.out.println("\nWidget EventDateFrom" );
				System.out.println("\nMonth: " +valFrom.get(Calendar.MONTH));
				System.out.println("Day: " +valFrom.get(Calendar.DAY_OF_MONTH));
				System.out.println("Year: " +valFrom.get(Calendar.YEAR));
				state.getServiceManager().getUserContext().remove("EventDateFrom");
			}
			if(state.getServiceManager().getUserContext().containsKey("EventDateTo"))
			{
				valTo = (Calendar)state.getServiceManager().getUserContext().get("EventDateTo");
				System.out.println("\nWidget EventDateTo" );
				System.out.println("\nMonth: " +valTo.get(Calendar.MONTH));
				System.out.println("Day: " +valTo.get(Calendar.DAY_OF_MONTH));
				System.out.println("Year: " +valTo.get(Calendar.YEAR));
				state.getServiceManager().getUserContext().remove("EventDateTo");
			}
			else
			{
				//Sessions do not exist
			}

			
			if((valFrom !=null) || (valTo !=null))
			{
				//perform query
				RuntimeFormInterface listForm = (RuntimeFormInterface)WPFormUtil.findForm(
						state.getCurrentRuntimeForm(),
						"",
						"wp_generated_alerts_list_view", state);
				
				if(listForm.isListForm())
				{
					focus = (BioCollectionBean)((RuntimeListFormInterface)(listForm)).getFocus();
					System.out.println("\n\nFocus size: " +focus.size());
				}
				
				if(valFrom !=null)
				{
					dateFromMil = valFrom.getTimeInMillis();
					fromQueryString = "wp_eventlog.EVENTTIME >= @DATE['" +dateFromMil + "']";				
					_log.debug("LOG_DEBUG_EXTENSION_GeneratedAlertsFilterQuery", "***fromQuery: " +fromQueryString, SuggestedCategory.NONE);
				}
				if(valTo !=null)
				{
					dateToMil = valTo.getTimeInMillis();
					toQueryString = "wp_eventlog.EVENTTIME <= @DATE['" +dateToMil + "']"; 
				}
				
			 	if(fromQueryString.length() > 0 && toQueryString.length() >0)
			 		queryString = fromQueryString + " AND " + toQueryString;
			 	else if(fromQueryString.length() > 0 && toQueryString.length() == 0)
			 		queryString = fromQueryString;
			 	else if(fromQueryString.length() == 0 && toQueryString.length()> 0)
			 		queryString = toQueryString;
			 	
			 	_log.debug("LOG_DEBUG_EXTENSION_GeneratedAlertsFilterQuery", "***query: " +queryString, SuggestedCategory.NONE);
				Query dateQry= new Query("wp_eventlog", queryString, null);
				dateFocus= (BioCollectionBean)focus.filter(dateQry);
				_log.debug("LOG_DEBUG_EXTENSION_GeneratedAlertsFilterQuery", "***new focus size: " +dateFocus.size(), SuggestedCategory.NONE);

				result.setFocus(dateFocus);
				
			}
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
		}
		
		return RET_CONTINUE;
	}
}
