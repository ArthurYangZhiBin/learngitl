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
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.exceptions.FormException;

public class CCFVASDescriptionValidation extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{
	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params)
			throws EpiException
	{
		String queryStatement = null;
		if(queryStatement!=params.get("fieldValue")){
			String[] parameters = new String[3];
			String widgetValue = params.get("fieldValue").toString();
			String orderkey = form.getFormWidgetByName("ORDERKEY").getDisplayValue();
			String oln = form.getFormWidgetByName("ORDERLINENUMBER").getDisplayValue();
			parameters[0] = orderkey;
			parameters[1] = oln;
			parameters[2] = widgetValue;

			// Query Bio to see if Attribute exists
			Query query = null;
			BioCollection results = null;
			UnitOfWorkBean uow = form.getFocus().getUnitOfWorkBean();
			queryStatement = "wm_orderdetailxvas.ORDERKEY = '" + orderkey + "'" + " AND wm_orderdetailxvas.ORDERLINENUMBER = '"+oln+"' AND wm_orderdetailxvas.VAS1 = '"+widgetValue+"'";
			query = new Query("wm_orderdetailxvas", queryStatement, null);
			results = uow.getBioCollectionBean(query);

			// If BioCollection size is greater than 0, throw form exception
			if (results.size() > 0)
				throw new FormException("WMEXP_SO_VAS_DUPE", parameters);
			else
				setStatus(STATUS_SUCCESS);
		}
		return RET_CONTINUE;
	}
}