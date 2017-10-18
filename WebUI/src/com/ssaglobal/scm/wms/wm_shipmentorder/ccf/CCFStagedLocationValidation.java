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
package com.ssaglobal.scm.wms.wm_shipmentorder.ccf;

// Import Epiphany packages and classes
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.exceptions.FormException;

public class CCFStagedLocationValidation extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{
	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params)
			throws EpiException
	{
		String[] parameter = new String[1];
		parameter[0] = colonStrip(readLabel(formWidget));
		String queryStatement = null;
		if(queryStatement!=params.get("fieldValue")){
			String widgetValue = params.get("fieldValue").toString();

			// Query Bio to see if Attribute exists
			Query query = null;
			BioCollection results = null;

			UnitOfWorkBean uow = form.getFocus().getUnitOfWorkBean();
			queryStatement = "wm_location.LOC = '" + widgetValue + "'" + " AND wm_location.LOCATIONTYPE = 'STAGED' ";
			query = new Query("wm_location", queryStatement, null);
			results = uow.getBioCollectionBean(query);

			// If BioCollection size equals 0, return RET_CANCEL
			if (results.size() == 0)
			{
				//throw new FormException("WMEXP_SO_STAGED_LOC", parameter);
				setErrorMessage(formWidget, getTextMessage("WMEXP_SO_STAGED_LOC", parameter, state.getLocale()));
			}
			else
			{
				setStatus(STATUS_SUCCESS);
			}
		}
		return RET_CONTINUE;
	}
	
	public String colonStrip(String label){
		Pattern pattern = Pattern.compile("\\:");
		Matcher matcher = pattern.matcher(label);
		return 	matcher.replaceAll("");
	}
	
	public String readLabel(RuntimeFormWidgetInterface widget){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widget.getLabel("label",locale);
	}
}