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
package com.ssaglobal.scm.wms.wm_load_maintenance;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.EpiRuntimeException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.UserUtil;
import com.ssaglobal.scm.wms.wm_facility.LoginNavigationPicker;


public class ReassignPrerenderAction extends FormExtensionBase{	

	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {		
		StateInterface state = context.getState();	
		HttpSession session = state.getRequest().getSession();
//		Get Header and Detail Forms
		form.getFormWidgetByName("FROMROUTE").setDisplayValue((String)session.getAttribute("REASSIGN_FROM_ROUTE"));
		form.getFormWidgetByName("FROMLOADID").setDisplayValue((String)session.getAttribute("REASSIGN_FROM_LOAD"));
		form.getFormWidgetByName("FROMSTOP").setDisplayValue((String)session.getAttribute("LOADSTOPID"));		
		//jp.answerlink.269293.begin
		String loadStopId = (String)session.getAttribute("LOADSTOPID");
		setStop(state, form, session, loadStopId);
		//jp.answerlink.269293.end

		return RET_CONTINUE;
	}
	
	private void setStop(StateInterface state, RuntimeNormalFormInterface form, 
			HttpSession session,String loadStopId) throws EpiException {
		
		if(loadStopId!=null && loadStopId.trim().length()>0){
			Query query = new Query("wm_loadstop","wm_loadstop.LOADSTOPID = " + loadStopId, null);
			BioCollectionBean bcb = state.getDefaultUnitOfWork().getBioCollectionBean(query);
			if(bcb!=null){
				Bio bio = bcb.elementAt(0);
				if(bio!=null){
					Integer stop = (Integer)bio.get("STOP");
					form.getFormWidgetByName("FROMSTOPVALUE").setDisplayValue(stop.toString());
				}
			}
			
		}
		
	}

}