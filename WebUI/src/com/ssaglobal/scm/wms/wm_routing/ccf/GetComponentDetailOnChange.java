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
package com.ssaglobal.scm.wms.wm_routing.ccf;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_routing.ui.GetPKValOnPreRender;

public class GetComponentDetailOnChange extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(GetComponentDetailOnChange.class);
	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params)
	throws EpiException
	{
		 _log.debug("LOG_DEBUG_EXTENSION_ROUTING","Executing GetComponentDetailOnChange",100L);
		String sourceForm = form.getName();

		
		RuntimeFormWidgetInterface componentWidget = null;		
		RuntimeFormWidgetInterface storerWidget = null;
		RuntimeFormWidgetInterface compDetWidget = null;
		
		Object sku = null;
		Object owner= null;
		
		HashMap allWidgets = getWidgetsValues(params);
				
		for (Iterator it = allWidgets.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry) it.next();
			RuntimeFormWidgetInterface key = (RuntimeFormWidgetInterface) entry.getKey();
			Object value = entry.getValue();
			try
			{
				if ((key.getName().equals("DESCRIPTION")) && (key.getForm().getName().equals(sourceForm)))
				{				
				compDetWidget = key;		
				}
				else if ((key.getName().equals("SKU")) && (key.getForm().getName().equals(sourceForm)))
				{
					sku = value;		
				}

				//Header STORER
				else if ((key.getName().equals("STORERKEY")) && (key.getForm().getName().equals(sourceForm)))
				{
					owner = value;
				}

			} catch (NullPointerException e)
			{
				e.printStackTrace();
			}
			

			
		}
		

		try
		{
			//Query BIO wm_pack with SKU itemValue
			UnitOfWorkBean uow = form.getFocus().getUnitOfWorkBean();
			String queryStatement = "sku.SKU= '" + sku.toString() + "'";
			Query query = new Query("sku", queryStatement, null);
			BioCollection results = uow.getBioCollectionBean(query);
			if (results.size() == 1)
			{
				Bio resultBio = results.elementAt(0);
				Object descr = resultBio.get("DESCR");
				
				if( descr != null && !descr.toString().equalsIgnoreCase("")){
				setValue(compDetWidget, resultBio.get("DESCR").toString());
				}
			}
			else
			{
				//SKU does not exist

			}
		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();

		}
		
		 _log.debug("LOG_DEBUG_EXTENSION_ROUTING","Exiting GetComponentDetailOnChange",100L);		
		return RET_CONTINUE;
		
	}
}
