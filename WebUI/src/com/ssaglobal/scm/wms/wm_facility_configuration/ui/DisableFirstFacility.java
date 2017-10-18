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
package com.ssaglobal.scm.wms.wm_facility_configuration.ui;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeListRowInterface;
import com.epiphany.shr.util.exceptions.EpiException;

public class DisableFirstFacility extends
		com.epiphany.shr.ui.view.customization.FormExtensionBase
{


	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form) throws EpiException
	{
		try
		{
	        StateInterface state = context.getState();
	        UnitOfWorkBean uow = state.getDefaultUnitOfWork();
	    	Query qry = new Query("wm_pl_db","wm_pl_db.db_enterprise = '1'", null);
	    	BioCollectionBean bc = uow.getBioCollectionBean(qry);
	    	String ent_logid = bc.filter(qry).elementAt(0).getString("db_logid");
			RuntimeListRowInterface[] listRows = form.getRuntimeListRows();
			for (int i = 0; i < listRows.length; i++)
			{
				RuntimeFormWidgetInterface logid = listRows[i].getFormWidgetByName("db_logid");
				RuntimeFormWidgetInterface isActive = listRows[i].getFormWidgetByName("isActive");
				if (ent_logid.equalsIgnoreCase(logid.getDisplayValue())){
					isActive.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					return RET_CONTINUE ;
				}
			}
		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;

	}

	/**
	 * Called in response to the modifyListValues event on a list form in a modal dialog. Subclasses  must override this in order
	 * to customize the display values of a list form
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state},
	 * service information and modal dialog context
	 * @param form the form that is about to be rendered
	 */


}
