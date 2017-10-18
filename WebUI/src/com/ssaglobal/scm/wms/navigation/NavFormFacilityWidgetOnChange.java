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
package com.ssaglobal.scm.wms.navigation;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.Metadata;
import com.epiphany.shr.metadata.objects.Form;
import com.epiphany.shr.metadata.objects.FormWidget;
import com.epiphany.shr.metadata.objects.Menu;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.action.client.ClientEventProperties;
import com.epiphany.shr.ui.action.client.ClientJSExtensionBase;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.EpnyControllerState;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeMenuFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;

public class NavFormFacilityWidgetOnChange extends ActionExtensionBase
{
	private final String ENTERPRISE_MENU_NAV = 			"changeEvent12";
	private final String WAREHOUSE_MENU_NAV = 			"changeEvent10";	
	private final String HOMEPAGE_MENU_NAV = 			"changeEvent15";	
	
	public NavFormFacilityWidgetOnChange()
    {
		
    }
	protected int execute(ActionContext context, ActionResult result){	
		EpnyControllerState state = (EpnyControllerState) context.getState();
		//SM 07-12-07 Changed findForm parameter "Service Top" to "WM Service Top" to match UI metadata adjustments for Solution Console fix
		RuntimeFormInterface form = FormUtil.findForm(state.getCurrentRuntimeForm(),"","WM Service Top",state);
		//SM End update
		RuntimeMenuFormWidgetInterface widgetFacility = (RuntimeMenuFormWidgetInterface)form.getFormWidgetByName("FACILITYMENU");
		
		BioRef selectedFacilityRef = widgetFacility.getSelectedItem();
		String facilityValue = "";
		try {
			BioBean facilityRecord = state.getDefaultUnitOfWork().getBioBean(selectedFacilityRef);
			facilityValue = facilityRecord.getString("db_name");
		} catch (BioNotFoundException e1) {			
			e1.printStackTrace();
			return RET_CANCEL;  
		} catch (EpiDataException e1) { 
			e1.printStackTrace();
			return RET_CANCEL;  
		}

		if(facilityValue.equalsIgnoreCase("ENTERPRISE")){								
			context.setNavigation(ENTERPRISE_MENU_NAV);
		}
		else if(facilityValue.length() > 0){									
			context.setNavigation(WAREHOUSE_MENU_NAV);
		}
		else{								
			context.setNavigation(HOMEPAGE_MENU_NAV);
		}
		
		return RET_CONTINUE;
		} 
}