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
package com.ssaglobal.scm.wms.wm_task_details.ui;


import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;

public class InventoryAttrFormPrerender extends FormExtensionBase{
	private static String SKU = "SKU";
	private static String pack = "PACKKEY";
	
	private static String skuBio = "wm_sku";
	private static String[] errorParam = new String[2];
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)throws EpiException{
		StateInterface state = context.getState();
		Object ownerObj = form.getFormWidgetByName("STORERKEY").getValue();
		Object skuObj = form.getFormWidgetByName("SKU").getValue();
		if(ownerObj != null && skuObj != null 
				&& !"".equalsIgnoreCase(ownerObj.toString())
				&& !"".equalsIgnoreCase(skuObj.toString())){
			String owner = form.getFormWidgetByName("STORERKEY").getValue().toString();
			String sku = form.getFormWidgetByName("SKU").getValue().toString();
			//Food Enhancements - 3PL - Putaway - Krishna Dhulipala  - Dec-17-2010 – Starts
			if(!sku.equalsIgnoreCase("MIXED"))
			{
			//Food Enhancements - 3PL - Putaway - Krishna Dhulipala  - Dec-17-2010 – Ends
				form.getFormWidgetByName("PACKKEY").setDisplayValue(getPack(owner, sku, state.getDefaultUnitOfWork()));
			//Food Enhancements - 3PL - Putaway - Krishna Dhulipala  - Dec-17-2010 – Starts
			}
			//Food Enhancements - 3PL - Putaway - Krishna Dhulipala  - Dec-17-2010 – Ends
		}
		
		return RET_CONTINUE;
	}
	
	
	private String getPack(String ownerVal, String skuVal, UnitOfWorkBean uow) throws EpiException {
		   String queryStatement = null;
		   Query query = null;
		   BioCollection results = null;
		   String packVal = null;
			try
			{
			queryStatement = skuBio + ".STORERKEY = '" + ownerVal +"'" + 
							 " AND " +skuBio +"." +SKU +"='" +skuVal +"'";
			query = new Query(skuBio, queryStatement, null);
			results = uow.getBioCollectionBean(query);
			}catch (Exception e)
			{
			e.printStackTrace();
			throw new EpiException("QUERY_ERROR", "Error preparing search query" + queryStatement);
			}
			if(results.size() == 1)
			{
				   Bio resultBio = results.elementAt(0);
				   packVal = resultBio.get(pack).toString();

			}
			else
			{
				//Error msg Invalid Storer/SKU combination
				errorParam[0]= ownerVal;
				errorParam[1]= skuVal;
				throw new UserException("WMEXP_INVALID_OWNER_ITEM_COMB", errorParam);
			}

		   
		return packVal;
	}
}
