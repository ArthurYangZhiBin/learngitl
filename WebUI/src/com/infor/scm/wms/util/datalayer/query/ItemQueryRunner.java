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

import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.wms.util.datalayer.WMSDataLayerException;
import com.infor.scm.wms.util.datalayer.driver.DataLayerInterface;
import com.infor.scm.wms.util.datalayer.driver.DataLayerQueryList;
import com.infor.scm.wms.util.datalayer.driver.DataLayerTableList;
import com.infor.scm.wms.util.datalayer.driver.DataLayerUpdateData;
import com.infor.scm.wms.util.datalayer.resultwrappers.DataLayerResultWrapper;
import com.infor.scm.wms.util.validation.WMSValidationContext;
import com.infor.scm.wms.util.validation.screen.item.ItemScreenVO;

public class ItemQueryRunner extends BaseQueryRunner{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemQueryRunner.class);
	public static DataLayerResultWrapper getItemByKey(String key, WMSValidationContext context) throws WMSDataLayerException{
		DataLayerResultWrapper item = null;
		ArrayList parameters = new ArrayList();
		parameters.add(key);
		item = DataLayerInterface.getResult(DataLayerQueryList.QUERY_ITEM_BY_SKU,parameters,context);
		return item;
	}
	
	public static DataLayerResultWrapper getItemByKeyAndStorer(String key,String storer, WMSValidationContext context) throws WMSDataLayerException{
		DataLayerResultWrapper item = null;
		ArrayList parameters = new ArrayList();
		parameters.add(key);
		parameters.add(storer);		
		item = DataLayerInterface.getResult(DataLayerQueryList.QUERY_ITEM_BY_SKU_STORER,parameters,context);
		return item;
	}
	
	public static void insertItem(ItemScreenVO item, WMSValidationContext context) throws WMSDataLayerException{
		DataLayerUpdateData updateData = new DataLayerUpdateData(DataLayerTableList.TABLE_ITEM,item);		
		DataLayerInterface.insert(updateData, context);
	}
	
	public static void insertItem(ArrayList items, WMSValidationContext context) throws WMSDataLayerException{
		_log.debug("LOG_SYSTEM_OUT","\n\nEntering insertItem...\n\n",100L);
		ArrayList updateDataList = new ArrayList();
		for(int i = 0; i < items.size(); i++){
			updateDataList.add(new DataLayerUpdateData(DataLayerTableList.TABLE_ITEM,(ItemScreenVO)items.get(i)));
		}
		DataLayerInterface.insert(updateDataList, context);
	}
}