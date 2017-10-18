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
package com.ssaglobal.scm.wms.wm_batch_picking.ui;

import javax.servlet.http.HttpSession;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;

public class BatchPickingPullFromSession extends ActionExtensionBase{
	private final static String BIOREF = "BIOREF";
	private final static String HEADER = "HEADER";
	private final static String WAVEKEY = "WAVEKEY";
	private final static String ERROR_MESSAGE = "ERROR_MESSAGE";
	private final static String IGNORE_CONFIRMED = "IGNORE_CONFIRMED";
//	private final static String STATUS = "STATUS";
	private final static String OSKEY = "ORDERSELECTIONKEY";
	private final static String BSKEY = "BATCHSELECTIONKEY";
	
	private final static String OS_TABLE = "wm_orderselection";
	private final static String WAVE_TABLE = "wm_wave";
	
	private final static String OS_PROC_NAME = "NSPBUILDWAVE";
	private final static String BS_PROC_NAME_1 = "BATCHCANDIDATES";
	private final static String BS_PROC_NAME_2 = "BATCHORDER";
	
	private final static String SHELL_SLOT = "list_slot_1";
	
	private final static String DEFAULT_NAV = "closeModalDialog71";
	private final static String ERROR_NAV = "closeModalDialog87";
	
	private final static String ERROR_MESSAGE_IGNORED = "WMEXP_BATCH_IGNORED";
	private final static String ERROR_MESSAGE_LOST_BIOREF = "WMEXP_LOST_BIOREF";

	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		//Initialize local variables
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		HttpSession session = state.getRequest().getSession();
		
		//Pull bioRef from session
		BioRef selectionRef = (BioRef)session.getAttribute(BIOREF);
		BioBean bio = null;
		
		//Get associated bio
		try{
			bio = uowb.getBioBean(selectionRef);
		}catch(Exception e){
			throw new FormException(ERROR_MESSAGE_LOST_BIOREF, null);
		}
		
		String bioType = bio.getDataType();
		
		//Build search query
		if(bioType.equals(OS_TABLE)){
			String headerKey = session.getAttribute(HEADER).toString();
			String queryString = WAVE_TABLE+"."+WAVEKEY+"='"+headerKey+"'";
			Query query = new Query(WAVE_TABLE, queryString, null);
			BioCollectionBean list = uowb.getBioCollectionBean(query);
			BioBean header = list.get("0");
			
			String waveKey = header.get(WAVEKEY).toString();
			String status = "%"; //header.get(STATUS).toString(); HARDCODED WILDCARD SEARCH
			String osKey = bio.get(OSKEY).toString();

			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
			Array parms = new Array(); 
			parms.add(new TextData(waveKey));	
			parms.add(new TextData(osKey));		
			parms.add(new TextData(status));		
			actionProperties.setProcedureParameters(parms);
			actionProperties.setProcedureName(OS_PROC_NAME);
			try {
				WmsWebuiActionsImpl.doAction(actionProperties);	
			} catch (WebuiException e) {
				throw new UserException(e.getMessage(), new Object[] {});
			}
			result.setFocus(header);
			session.removeAttribute(HEADER);
		}else{
			boolean ignoreConfirmed = ((Boolean)session.getAttribute(IGNORE_CONFIRMED)).booleanValue();
			String bsKey = bio.get(BSKEY).toString();
			
			RuntimeFormInterface shell = state.getCurrentRuntimeForm().getParentForm(state).getParentForm(state);
			RuntimeFormInterface header = state.getRuntimeForm(shell.getSubSlot(SHELL_SLOT), null);
			String waveKey = header.getFormWidgetByName(WAVEKEY).getDisplayValue();
			
			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
			Array parms = new Array(); 
			parms.add(new TextData(waveKey));	
			parms.add(new TextData(bsKey));				
			actionProperties.setProcedureParameters(parms);
			if(!ignoreConfirmed){
				actionProperties.setProcedureName(BS_PROC_NAME_1);
				try {
					EXEDataObject edo = WmsWebuiActionsImpl.doAction(actionProperties);	
					String numIgnored = edo.getAttribValue(new TextData("WillBeIgnored")).toString();
					if(!numIgnored.equals("0")){
						//Set display value to error message and post on popup
						String base = getTextMessage(ERROR_MESSAGE_IGNORED, new Object[] {numIgnored}, state.getLocale());
						session.setAttribute(ERROR_MESSAGE, base);
						context.setNavigation(ERROR_NAV);
						session.setAttribute(IGNORE_CONFIRMED, Boolean.TRUE);
						return RET_CONTINUE;
					}
				} catch (WebuiException e) {
					throw new UserException(e.getMessage(), new Object[] {});
				}
			}
			actionProperties.setProcedureName(BS_PROC_NAME_2);
			try {
				WmsWebuiActionsImpl.doAction(actionProperties);	
			} catch (WebuiException e) {
				throw new UserException(e.getMessage(), new Object[] {});
			}
			result.setFocus(header.getFocus());
			context.setNavigation(DEFAULT_NAV);
			session.removeAttribute(IGNORE_CONFIRMED);
		}

		//Build displayed list
		session.removeAttribute(BIOREF);
		return RET_CONTINUE;
	}
}