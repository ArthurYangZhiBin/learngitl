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

public class BatchPickingNewBatchSelection extends ActionExtensionBase{
	private final static String TABLE = "wm_batchselection";
	private final static String BS_KEY = "BATCHSELECTIONKEY";
	private final static String BULKCARTGRP = "BULKCARTONGROUP";
	private final static String SORTLOC = "SORTLOCASSIGNMENT";
	private final static String CUSTTYPESTART = "CUSTOMERTYPESTART";
	private final static String CUSTTYPEEND = "CUSTOMERTYPEEND";
	private final static String CONSIGNSTART = "CONSIGNEESTART";
	private final static String CONSIGNEND = "CONSIGNEEEND";
	private final static String ORDTYPESTART = "ORDERTYPESTART";
	private final static String ORDTYPEEND = "ORDERTYPEEND";
	private final static String OHTYPESTART = "OHTYPESTART";
	private final static String OHTYPEEND = "OHTYPEEND";
	private final static String TOTQTYSTART = "TOTALQTYSTART";
	private final static String TOTQTYEND = "TOTALQTYEND";
	private final static String TOTLINESTART = "TOTALLINESSTART";
	private final static String TOTLINEEND = "TOTALLINESEND";
	private final static String TOTCUBESTART = "TOTALCUBESTART";
	private final static String TOTCUBEEND  = "TOTALCUBEEND";
	private final static String TOTWEIGHTSTART = "TOTALWEIGHTSTART";
	private final static String TOTWEIGHTEND = "TOTALWEIGHTEND";
	private final static String DEFAULTFLAG = "DEFAULTFLAG";
	
	private final static String KEY = "STD";
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		String queryString = TABLE+"."+BS_KEY+"='"+KEY+"'";
		Query qry = new Query(TABLE, queryString, null);
		BioCollectionBean list = uowb.getBioCollectionBean(qry);
		BioBean bio = list.get("0");
		QBEBioBean qbe = uowb.getQBEBioWithDefaults(TABLE);
		qbe.set(BULKCARTGRP , bio.get(BULKCARTGRP));
		qbe.set(SORTLOC , bio.get(SORTLOC));
		qbe.set(CUSTTYPESTART , bio.get(CUSTTYPESTART));
		qbe.set(CUSTTYPEEND , bio.get(CUSTTYPEEND));
		qbe.set(CONSIGNSTART , bio.get(CONSIGNSTART));
		qbe.set(CONSIGNEND , bio.get(CONSIGNEND));
		qbe.set(ORDTYPESTART, bio.get(ORDTYPESTART));
		qbe.set(ORDTYPEEND , bio.get(ORDTYPEEND));
		qbe.set(OHTYPESTART , bio.get(OHTYPESTART));
		qbe.set(OHTYPEEND , bio.get(OHTYPEEND));
		qbe.set(TOTQTYSTART , bio.get(TOTQTYSTART));
		qbe.set(TOTQTYEND , bio.get(TOTQTYEND));
		qbe.set(TOTLINESTART , bio.get(TOTLINESTART));
		qbe.set(TOTLINEEND , bio.get(TOTLINEEND));
		qbe.set(TOTCUBESTART , bio.get(TOTCUBESTART));
		qbe.set(TOTCUBEEND , bio.get(TOTCUBEEND));
		qbe.set(TOTWEIGHTSTART , bio.get(TOTWEIGHTSTART));
		qbe.set(TOTWEIGHTEND , bio.get(TOTWEIGHTEND));
		qbe.set(DEFAULTFLAG, "0");
		result.setFocus(qbe);
		return RET_CONTINUE;
	}
}