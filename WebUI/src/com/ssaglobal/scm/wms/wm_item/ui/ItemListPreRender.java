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
package com.ssaglobal.scm.wms.wm_item.ui;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.objects.BioReference;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.element.IntBucket;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeListRowInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.util.SecurityUtil;
import com.ssaglobal.scm.wms.util.UserUtil;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultPersistScreensInContext;


public class ItemListPreRender extends FormExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemListPreRender.class);
	
	//SRG Begin: Incident3735455_Defect285007
	String sessionVariable;
	String sessionObjectValue;
	//SRG End: Incident3735455_Defect285007
	
	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) throws EpiException {	
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		String uid = UserUtil.getUserId(state);		
		boolean allowImage = true;
		Query bioQry = new Query("wsdefaults","wsdefaults.ENABLEITEMIMAGEGLOBAL = '1'","");
		BioCollection defaults = uow.getBioCollectionBean(bioQry);
		if(defaults == null || defaults.size() == 0){			
			allowImage = false;
		}
		
		if(allowImage){
			bioQry = new Query("wsdefaults","wsdefaults.USERID = '"+uid+"' AND wsdefaults.ENABLEITEMIMAGEUSER = '1'","");
			defaults = uow.getBioCollectionBean(bioQry);
			if(defaults == null || defaults.size() == 0){				
				allowImage = false;
			}
		}		
		if(!allowImage){					
			form.hideColumn("IMAGE");
		}		
		else{			
			form.showColumn("IMAGE");
		}
		

		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		String isEnterprise = userContext.get(SetIntoHttpSessionAction.DB_ISENTERPRISE).toString();

		if (isEnterprise.equals("1"))
		{	
			form.hideColumn("STRATEGYKEYWHSE");	
			form.hideColumn("PUTAWAYSTRATEGYKEYWHSE");
			form.hideColumn("PACKKEYWHSE");	
			form.hideColumn("CARTONGROUPWHSE");	
			form.hideColumn("LOTTABLEVALIDATIONKEYWHSE");
		}
		else
		{	
			form.hideColumn("STRATEGYKEYENT");	
			form.hideColumn("PUTAWAYSTRATEGYKEYENT");
			form.hideColumn("PACKKEYENT");	
			form.hideColumn("CARTONGROUPENT");	
			form.hideColumn("LOTTABLEVALIDATIONKEYENT");
		}
		
		//SRG Begin: Incident3735455_Defect285007: Set the page start value in the session which can be used in the 
		//ItemSaveValidationAction extension.
		int winStart = form.getWindowStart();
		_log.debug("LOG_SYSTEM_OUT","WINDOW START: "+winStart,100L);
		String interactionID = context.getState().getInteractionId();
		String contextVariableSuffix = "WINDOWSTART";
		sessionVariable = interactionID + contextVariableSuffix;
		sessionObjectValue = "" + winStart;
		HttpSession session = context.getState().getRequest().getSession();
		session.setAttribute(sessionVariable, sessionObjectValue);
		//SRG End: Incident3735455_Defect285007
		
		return RET_CONTINUE;
	}
	
	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form) throws EpiException {		
		RuntimeListRowInterface[] listRows = form.getRuntimeListRows();		
		ArrayList bios = ((RuntimeListForm)form).getBioRefsForCurrentWindow();	//records in the page	
		BioCollectionBean focus = (BioCollectionBean)form.getFocus();	//all records	

		UnitOfWorkBean unitOfWorkBean = focus.getUnitOfWorkBean();
		for(int i = 0; i < bios.size(); i++){			
			BioRef refFromString = BioRef.createBioRefFromString((String)bios.get(i));
			BioBean bio = unitOfWorkBean.getBioBean(refFromString);
			String hasImage = (String)bio.getValue("HASIMAGE");
			if(hasImage == null || hasImage.equals("0"))
				continue;			
			RuntimeFormWidgetInterface imageButton = listRows[i].getFormWidgetByName("IMAGE");			
			//imageButton.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, new Integer(0));
			imageButton.setProperty(RuntimeFormWidgetInterface.PROP_IMAGE, "images\\icon_camera2.png");
		}
		
		//SRG Begin: Incident3735455_Defect285007: Set the page start value in the session which can be used in the 
		//ItemSaveValidationAction extension.
		int winStart = form.getWindowStart();
		_log.debug("LOG_SYSTEM_OUT","WINDOW START: "+winStart,100L);
		String interactionID = context.getState().getInteractionId();
		String contextVariableSuffix = "WINDOWSTART";
		sessionVariable = interactionID + contextVariableSuffix;
		sessionObjectValue = "" + winStart;
		HttpSession session = context.getState().getRequest().getSession();
		session.setAttribute(sessionVariable, sessionObjectValue);
		//SRG End: Incident3735455_Defect285007
		
		return RET_CONTINUE;
	}
	
}