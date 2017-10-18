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
package com.infor.scm.waveplanning.wp_query_builder.action;

//Import 3rd party packages and classes
import java.util.Calendar;
import java.util.GregorianCalendar;

//Import Epiphany packages and classes
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.common.util.WPFormUtil;
import com.infor.scm.waveplanning.common.util.WPUserUtil;

public class WPQueryBuilderProceed extends SaveAction{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPQueryBuilderProceed.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRPROCEED","Executing WPQueryBuilderProceed",100L);		
		StateInterface state = context.getState();
		RuntimeFormInterface queryBuilderInitialForm = WPFormUtil.findForm(state.getCurrentRuntimeForm(),"","wm_wp_query_builder_initial_screen",state);				
		String uid = WPUserUtil.getUserId(state);
		String interactionId = context.getState().getInteractionId();
		if(queryBuilderInitialForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Found Query Builder Initial Form:"+queryBuilderInitialForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Found Query Builder Initial Form:Null",100L);			
		
		
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		
		//Get focus				
		DataBean queryBuilderInitialFocus = queryBuilderInitialForm == null ? null : queryBuilderInitialForm.getFocus();	
		
		//Tag the record with the current interaction ID
		GregorianCalendar gc = new GregorianCalendar();
		queryBuilderInitialFocus.setValue("INTERACTIONID",interactionId);
		queryBuilderInitialFocus.setValue("ISHEADER","1");
		queryBuilderInitialFocus.setValue("USERID",uid);						
		queryBuilderInitialFocus.save();
		
		//Clean Out All Records Older Than 1 Day
		gc.add(Calendar.DAY_OF_WEEK,-1);			
		BioCollection bc = null;		
		Query qry = new Query("querybuildertemp","querybuildertemp.DATEADDED < @DATE['"+gc.getTimeInMillis()+"']","");
		bc = uow.getBioCollectionBean(qry);				
		try {
			if(bc != null){
				_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","bc.size():"+bc.size(),100L);
				for(int i = 0; i < bc.size(); i++){					
					bc.elementAt(i).delete();
				}
			}
		} catch (EpiDataException e) {
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		
		try {
			uow.saveUOW(true);
		} catch (EpiException e) {
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
			
		qry = new Query("querybuildertemp","querybuildertemp.ISHEADER = '0' AND querybuildertemp.INTERACTIONID = '"+interactionId+"'","");
		result.setFocus(uow.getBioCollectionBean(qry));
		return RET_CONTINUE;	
	}
}