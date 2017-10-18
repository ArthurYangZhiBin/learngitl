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

import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.data.bio.Query;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;

public class BatchPickingNewOrderSelection extends ActionExtensionBase{
	private final static String TABLE = "wm_orderselection";
	private final static String OS_KEY = "ORDERSELECTIONKEY";
	private final static String CONFIGKEY = "CONFIGKEY";
	private final static String STATUSSTART = "ORDERSTATUSSTART";
	private final static String STATUSEND = "ORDERSTATUSEND";
	private final static String TYPESTART = "ORDERTYPESTART";
	private final static String TYPEEND = "ORDERTYPEEND";
	private final static String KEYSTART = "ORDERKEYSTART";
	private final static String KEYEND = "ORDERKEYEND";
	private final static String EXKEYSTART = "EXTERNORDERKEYSTART";
	private final static String EXKEYEND = "EXTERNORDERKEYEND";
	private final static String OWNERSTART = "STORERKEYSTART";
	private final static String OWNEREND = "STORERKEYEND";
	private final static String OD_START = "ORDERDATESTART";
	private final static String OD_END = "ORDERDATEEND";
	private final static String DD_START = "DELIVERYDATESTART";
	private final static String DD_END = "DELIVERYDATEEND";
	private final static String PRIORITYSTART = "ORDERPRIORITYSTART";
	private final static String PRIORITYEND = "ORDERPRIORITYEND";
	private final static String ITEMSTART = "SKUSTART";
	private final static String ITEMEND = "SKUEND";
	private final static String CKEYSTART = "CONSIGNEEKEYSTART";
	private final static String CKEYEND = "CONSIGNEEKEYEND";
	private final static String CARRIERSTART = "CARRIERKEYSTART";
	private final static String CARRIEREND = "CARRIERKEYEND";
	private final static String ROUTESTART = "ROUTESTART";
	private final static String ROUTEEND = "ROUTEEND";
	private final static String STOPSTART = "STOPSTART";
	private final static String STOPEND = "STOPEND";
	private final static String LOADIDSTART = "LOADIDSTART";
	private final static String LOADIDEND = "LOADIDEND";
	
	private final static String KEY = "STD";
	private final static String WAVETYPE = "EXW";
	
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		String queryString = TABLE+"."+OS_KEY+"='"+KEY+"' and "+TABLE+"."+CONFIGKEY+"='"+WAVETYPE+"'";
		Query qry = new Query(TABLE, queryString, null);
		BioCollectionBean list = uowb.getBioCollectionBean(qry);
		BioBean bio = list.get("0");
		QBEBioBean qbe = uowb.getQBEBioWithDefaults(TABLE);
		qbe.set(CONFIGKEY , bio.get(CONFIGKEY));
		qbe.set(STATUSSTART , bio.get(STATUSSTART));
		qbe.set(STATUSEND , bio.get(STATUSEND));
		qbe.set(TYPESTART , bio.get(TYPESTART));
		qbe.set(TYPEEND , bio.get(TYPEEND));
		qbe.set(KEYSTART , bio.get(KEYSTART));
		qbe.set(KEYEND , bio.get(KEYEND));
		qbe.set(EXKEYSTART, bio.get(EXKEYSTART));
		qbe.set(EXKEYEND , bio.get(EXKEYEND));
		if(WSDefaultsUtil.isOwnerLocked(state)){
			qbe.set(OWNERSTART , WSDefaultsUtil.getPreFilterValueByType("STORER", state));
			qbe.set(OWNEREND , WSDefaultsUtil.getPreFilterValueByType("STORER", state));
		}else{
			qbe.set(OWNERSTART , bio.get(OWNERSTART));
			qbe.set(OWNEREND , bio.get(OWNEREND));	
		}		
		qbe.set(OD_START , bio.get(OD_START));
		qbe.set(OD_END , bio.get(OD_END));
		qbe.set(DD_START , bio.get(DD_START));
		qbe.set(DD_END , bio.get(DD_END));
		qbe.set(PRIORITYSTART , bio.get(PRIORITYSTART));
		qbe.set(PRIORITYEND , bio.get(PRIORITYEND));
		qbe.set(ITEMSTART , bio.get(ITEMSTART));
		qbe.set(ITEMEND , bio.get(ITEMEND));
		if(WSDefaultsUtil.isOwnerLocked(state)){
			qbe.set(CKEYSTART , WSDefaultsUtil.getPreFilterValueByType("CUSTOM", state));
			qbe.set(CKEYEND , WSDefaultsUtil.getPreFilterValueByType("CUSTOM", state));
		}else{
			qbe.set(CKEYSTART , bio.get(CKEYSTART));
			qbe.set(CKEYEND , bio.get(CKEYEND));
		}
		
		if(WSDefaultsUtil.isOwnerLocked(state)){
			qbe.set(CARRIERSTART , WSDefaultsUtil.getPreFilterValueByType("CARRIER", state));
			qbe.set(CARRIEREND , WSDefaultsUtil.getPreFilterValueByType("CARRIER", state));
		}else{
			qbe.set(CARRIERSTART , bio.get(CARRIERSTART));
			qbe.set(CARRIEREND , bio.get(CARRIEREND));	
		}			
		qbe.set(ROUTESTART , bio.get(ROUTESTART));
		qbe.set(ROUTEEND , bio.get(ROUTEEND));
		qbe.set(STOPSTART , bio.get(STOPSTART));
		qbe.set(STOPEND , bio.get(STOPEND));
		qbe.set(LOADIDSTART , bio.get(LOADIDSTART));
		qbe.set(LOADIDEND , bio.get(LOADIDEND));
		result.setFocus(qbe);
		return RET_CONTINUE;
	}
}