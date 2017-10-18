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

import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.util.exceptions.EpiException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrategyPreDelete extends ActionExtensionBase{
	private final static String SHELL_SLOT = "list_slot_1";
	private final static String STRAT_KEY = "STRATEGYKEY";
	private final static String TABLE = "sku";
	private final static String ERROR_MESSAGE_FOREIGN = "WMEXP_FOREIGN_DEPENDENCY";
	private final static String ERROR_MESSAGE_NO_SELECT = "WMEXP_LIST_SELECT";
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		String[] parameter = new String[2];
		StateInterface state = context.getState();
		RuntimeFormInterface form = state.getRuntimeForm(state.getCurrentRuntimeForm().getParentForm(state).getSubSlot(SHELL_SLOT), null);
		RuntimeListFormInterface list = (RuntimeListFormInterface)form;
		ArrayList selected = list.getAllSelectedItems();
		if(selected!=null){
			for(int index=0; index<selected.size(); index++){
				BioBean bean = (BioBean)selected.get(index);
				UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
				String value = bean.get(STRAT_KEY).toString();
				String queryString = TABLE+"."+STRAT_KEY+"='"+value+"'";
				Query qry = new Query(TABLE, queryString, null);
				BioCollectionBean bcBean = uowb.getBioCollectionBean(qry);
				if(bcBean.size()>0){
					RuntimeFormWidgetInterface widget = form.getFormWidgetByName(STRAT_KEY);
					parameter[0]=colonStrip(readLabel(widget));
					parameter[1]=value;
					throw new FormException(ERROR_MESSAGE_FOREIGN, parameter);
				}
			}	
		}else{
			throw new FormException(ERROR_MESSAGE_NO_SELECT, null);
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