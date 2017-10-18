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

public class PackingValidationTemplateQueryRunner extends BaseQueryRunner{
	public static DataLayerResultWrapper getPackingValidationTemplateByKey(String key, WMSValidationContext context) throws WMSDataLayerException{
		DataLayerResultWrapper packvalidationtemplate = null;
		ArrayList parameters = new ArrayList();
		parameters.add(key);				
		packvalidationtemplate = DataLayerInterface.getResult(DataLayerQueryList.QUERY_PACKINGVALIDATIOINTEMPLATE_BY_PACKINGVALIDATIONTEMPLATEKEY,parameters, context);
		return packvalidationtemplate;
	}	
}