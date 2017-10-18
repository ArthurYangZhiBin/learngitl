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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.data.bio.Query;

public class BatchPickingSelectionValidation extends ActionExtensionBase{
	
	private final static String OSKEY = "ORDERSELECTIONKEY";
	private final static String OSCONFIG = "CONFIGKEY";
	private final static String BSKEY = "BATCHSELECTIONKEY";
	
	private final static String OS_TABLE = "wm_orderselection";
	private final static String BS_TABLE = "wm_batchselection";
	
	private final static String ERROR_MESSAGE_EMPTY_KEYS = "WMEXP_NULL_PRIMARY_FIELD";
	private final static String ERROR_MESSAGE_EXISTS = "WMEXP_CCPS_EXISTS";
		
	protected int execute(ModalActionContext context, ActionResult result) throws EpiException{
		//Initialize general variables
		StateInterface state = context.getState();
		String[] parameter = new String[1];
		parameter[0]="";

		DataBean focus = state.getFocus();
		String bioType = focus.getDataType();
		RuntimeFormInterface form = state.getCurrentRuntimeForm().getParentForm(state);
		RuntimeFormWidgetInterface osKey=null, osConfig=null, bsKey=null;
		if(bioType.equals(OS_TABLE)){
			osKey = form.getFormWidgetByName(OSKEY);
			osConfig = form.getFormWidgetByName(OSCONFIG);
		}else{
			bsKey = form.getFormWidgetByName(BSKEY);
		}

		
		//Validate keys for first save
		if(focus.isTempBio()){
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			String queryString = null;
			String table = null;
			QBEBioBean qbe = (QBEBioBean)focus;
			if(bioType.equals(OS_TABLE)){
				String osKeyValue = osKey.getDisplayValue();
				//Catch null pointer exceptions
				if(osKeyValue==null){
					parameter[0] = colonStrip(readLabel(osKey));
				}else{
					osKeyValue = osKeyValue.toUpperCase();
					qbe.set(OSKEY, osKeyValue);
				}
				if(qbe.get(OSCONFIG)==null){
					if(!parameter[0].equalsIgnoreCase("")){
						parameter[0]=parameter[0]+", ";
					}
					parameter[0]=parameter[0]+colonStrip(readLabel(osConfig));
				}
				if(!parameter[0].equalsIgnoreCase("")){
					throw new FormException(ERROR_MESSAGE_EMPTY_KEYS, parameter);
				}
				//Search for duplicate keys
				queryString = OS_TABLE+"."+OSKEY+"='"+osKeyValue+"' and "+OS_TABLE+"."+OSCONFIG+"='"+qbe.get(OSCONFIG).toString().toUpperCase()+"'";
				table = OS_TABLE;
				parameter[0] = colonStrip(readLabel(osKey))+", "+colonStrip(readLabel(osConfig));
			}else{
				String bsKeyValue = bsKey.getDisplayValue();
				//Catch null pointer exceptions
				if(bsKeyValue==null){
					parameter[0]=colonStrip(readLabel(bsKey));
					throw new FormException(ERROR_MESSAGE_EMPTY_KEYS, parameter);
				}else{
					bsKeyValue = bsKeyValue.toUpperCase();
					qbe.set(BSKEY, bsKeyValue);
				}
				//Search for duplicate keys
				queryString = BS_TABLE+"."+BSKEY+"='"+bsKeyValue.toUpperCase()+"'";
				table = BS_TABLE;
				parameter[0] = colonStrip(readLabel(bsKey));
			}
			Query query = new Query(table, queryString, null);
			BioCollectionBean list = uowb.getBioCollectionBean(query);
			if(list.size()>0){
				throw new FormException(ERROR_MESSAGE_EXISTS, parameter);
			}
		}

		return RET_CONTINUE;
	}
	
	private String colonStrip(String label){
		Pattern pattern = Pattern.compile("\\:");
		Matcher matcher = pattern.matcher(label);
		return 	matcher.replaceAll("");
	}
	
	private String readLabel(RuntimeFormWidgetInterface widgetName){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widgetName.getLabel("label",locale);
	}
}
