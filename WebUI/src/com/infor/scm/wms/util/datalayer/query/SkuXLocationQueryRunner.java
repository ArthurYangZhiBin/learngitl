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
package com.infor.scm.wms.util.datalayer.query;

import java.util.ArrayList;

import com.infor.scm.wms.util.datalayer.WMSDataLayerException;
import com.infor.scm.wms.util.datalayer.driver.DataLayerInterface;
import com.infor.scm.wms.util.datalayer.driver.DataLayerQueryList;
import com.infor.scm.wms.util.datalayer.resultwrappers.DataLayerResultWrapper;
import com.infor.scm.wms.util.validation.WMSValidationContext;

public class SkuXLocationQueryRunner extends BaseQueryRunner{
	public static DataLayerResultWrapper getSkuXLocationByLocSkuOwner(String loc,String sku,String owner, WMSValidationContext context) throws WMSDataLayerException{
		DataLayerResultWrapper storer = null;
		ArrayList parameters = new ArrayList();
		parameters.add(loc);
		parameters.add(sku);
		parameters.add(owner);
		storer = DataLayerInterface.getResult(DataLayerQueryList.QUERY_SKU_X_LOCATION_BY_LOC_SKU_STORERKEY,parameters, context);
		return storer;
	}
		
}