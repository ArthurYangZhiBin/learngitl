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
package com.infor.scm.waveplanning.wp_graphical_filters.action;
import java.util.ArrayList;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.common.util.WPFormUtil;
import com.infor.scm.waveplanning.common.util.WPUserUtil;


public class WPGraphicalFilterPrevCriterionOnChange extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPGraphicalFilterPrevCriterionOnChange.class);
	
	@Override
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		System.out.println("\n\nNew Extension1\n\n");
		_log.debug("LOG_DEBUG_EXTENSION_WPGRPHFLTRPROCEED","Executing WPGraphicalFilterProceed",100L);		
		StateInterface state = context.getState();
		RuntimeFormInterface historyForm = WPFormUtil.findForm(state.getCurrentRuntimeForm(),"","wp_graphical_filters_history_dropdown",state);								
		if(historyForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_WPGRPHFLTRPROCEED","Found History Form:"+historyForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_WPGRPHFLTRPROCEED","Found History Form:Null",100L);			
		
		System.out.println("\n\nNew Extension2\n\n");
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();		
		
		//get widgets				
		RuntimeFormWidgetInterface criterionHistoryWidget = historyForm.getFormWidgetByName("HISTORY");		
		
		//get widget values
		String criterion = (String)criterionHistoryWidget.getValue();		
		ArrayList selectedCriterion = (ArrayList)WPUserUtil.getInteractionSessionAttribute(WPGraphicalFilterProceed.SESSION_KEY_GRAPH_QRY_BASE_CRITERION, state);
		System.out.println("\n\nselectedCriterion Before:"+selectedCriterion+"\n\n");
		//Remove criterion in history after the selected criterion
		String selectedCriterionDelimitedList = "'"+selectedCriterion.get(0).toString()+"'";
		for(int i = 0; i < selectedCriterion.size(); i++){
			selectedCriterionDelimitedList += ", '"+selectedCriterion.get(i).toString()+"'";
			if(selectedCriterion.get(i).equals(criterion)){							
				for(++i;i < selectedCriterion.size(); i++){					
					selectedCriterion.remove(i);	
					i--;
				}
			}					
		}			
		System.out.println("\n\nselectedCriterion After:"+selectedCriterion+"\n\n");
		//Set values in interaction session
		WPUserUtil.setInteractionSessionAttribute(WPGraphicalFilterProceed.SESSION_KEY_GRAPH_QRY_BASE_CRITERION, selectedCriterion, state);		
				
		//Update Temp Table		
		Query qry = new Query("wp_graphicalfilter_temp","wp_graphicalfilter_temp.INTERACTIONID = '"+state.getInteractionId()+"' AND NOT(wp_graphicalfilter_temp.FILTERTYPE IN ("+selectedCriterionDelimitedList+"))","");
		BioCollectionBean criteriaCollection = uow.getBioCollectionBean(qry);
		try {
			if(criteriaCollection != null){		
				for(int i = 0; i < criteriaCollection.size(); i++){
					criteriaCollection.elementAt(i).delete();					
				}
			}
			uow.saveUOW();
		} catch (EpiDataException e) {
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while saving...",100L);
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e.getStackTraceAsString(),100L);
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} catch (EpiException e) {
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while saving...",100L);
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e.getStackTraceAsString(),100L);
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		
		qry = new Query("wp_graphicalfilter_temp","wp_graphicalfilter_temp.INTERACTIONID = '"+state.getInteractionId()+"'","");
		criteriaCollection = uow.getBioCollectionBean(qry);
		try {
			if(criteriaCollection != null){		
				for(int i = 0; i < criteriaCollection.size(); i++){
					if(criteriaCollection.elementAt(i).get("FILTERTYPE").equals(selectedCriterion.get(selectedCriterion.size() - 1)))
						criteriaCollection.elementAt(i).set("DODISPLAY", "1");	
					else
						criteriaCollection.elementAt(i).set("DODISPLAY", "0");
				}
			}
			uow.saveUOW();
		} catch (EpiDataException e) {
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while saving...",100L);
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e.getStackTraceAsString(),100L);
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} catch (EpiException e) {
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while saving...",100L);
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e.getStackTraceAsString(),100L);
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
				
		qry = new Query("wp_graphicalfilter_temp", "wp_graphicalfilter_temp.INTERACTIONID = '" + state.getInteractionId() + "' AND wp_graphicalfilter_temp.DODISPLAY = '1'", "wp_graphicalfilter_temp.ADDITIONALFILTER ASC");
		result.setFocus(uow.getBioCollectionBean(qry));
		return RET_CONTINUE;	
	}
	
}
