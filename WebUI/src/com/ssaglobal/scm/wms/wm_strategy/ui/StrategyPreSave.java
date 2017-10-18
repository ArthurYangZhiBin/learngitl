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
package com.ssaglobal.scm.wms.wm_strategy.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.exceptions.FormException;

public class StrategyPreSave extends ActionExtensionBase{
	private final static String SHELL_SLOT_2 = "list_slot_2";
	private final static String TABLE = "wm_strategy";
	private final static String STRAT_KEY = "STRATEGYKEY";
	private final static String ERROR_MESSAGE="WMEXP_CCPS_EXISTS";
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		String[] parameter = new String[1];
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		RuntimeFormInterface form = state.getRuntimeForm(state.getCurrentRuntimeForm().getParentForm(state).getSubSlot(SHELL_SLOT_2), null);
		if(form.getFocus().isTempBio()){
			RuntimeFormWidgetInterface widget = form.getFormWidgetByName(STRAT_KEY); 
			String queryString = TABLE+"."+STRAT_KEY+"= '"+widget.getDisplayValue()+"'";
			Query qry = new Query(TABLE, queryString, null);
			BioCollectionBean stratList = uow.getBioCollectionBean(qry);
			if(stratList!=null){
				if(stratList.size()!=0){
					parameter[0] = colonStrip(readLabel(widget));
					throw new FormException(ERROR_MESSAGE, parameter);
				}	
			}
		}
		return RET_CONTINUE;
	}
	
	private String colonStrip(String label){
		Pattern pattern = Pattern.compile("\\:");
		Matcher matcher = pattern.matcher(label);
		return 	matcher.replaceAll("");
	}
	
	private String readLabel(RuntimeFormWidgetInterface widget){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widget.getLabel("label",locale);
	}
}