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

public class PackQueryRunner extends BaseQueryRunner{
	public static DataLayerResultWrapper getPackByKey(String key, WMSValidationContext context) throws WMSDataLayerException{
		DataLayerResultWrapper pack = null;
		ArrayList parameters = new ArrayList();
		parameters.add(key);
		pack = DataLayerInterface.getResult(DataLayerQueryList.QUERY_PACK_BY_PACKKEY,parameters,context);
		return pack;
	}
	
	public static DataLayerResultWrapper getPackByKeyAndUOM1(String key, String packUOM1, WMSValidationContext context) throws WMSDataLayerException{
		DataLayerResultWrapper pack = null;
		ArrayList parameters = new ArrayList();
		parameters.add(key);
		parameters.add(packUOM1);
		pack = DataLayerInterface.getResult(DataLayerQueryList.QUERY_PACK_BY_PACKKEY_PACKUOM1,parameters,context);
		return pack;
	}
	
	public static DataLayerResultWrapper getPackByKeyAndUOM2(String key, String packUOM2, WMSValidationContext context) throws WMSDataLayerException{
		DataLayerResultWrapper pack = null;
		ArrayList parameters = new ArrayList();
		parameters.add(key);
		parameters.add(packUOM2);
		pack = DataLayerInterface.getResult(DataLayerQueryList.QUERY_PACK_BY_PACKKEY_PACKUOM2,parameters,context);
		return pack;
	}
	
	public static DataLayerResultWrapper getPackByKeyAndUOM3(String key, String packUOM3, WMSValidationContext context) throws WMSDataLayerException{
		DataLayerResultWrapper pack = null;
		ArrayList parameters = new ArrayList();
		parameters.add(key);
		parameters.add(packUOM3);
		pack = DataLayerInterface.getResult(DataLayerQueryList.QUERY_PACK_BY_PACKKEY_PACKUOM3,parameters,context);
		return pack;
	}
	
	public static DataLayerResultWrapper getPackByKeyAndUOM4(String key, String packUOM4, WMSValidationContext context) throws WMSDataLayerException{
		DataLayerResultWrapper pack = null;
		ArrayList parameters = new ArrayList();
		parameters.add(key);
		parameters.add(packUOM4);
		pack = DataLayerInterface.getResult(DataLayerQueryList.QUERY_PACK_BY_PACKKEY_PACKUOM4,parameters,context);
		return pack;
	}
	
	public static DataLayerResultWrapper getPackByKeyAndUOM5(String key, String packUOM5, WMSValidationContext context) throws WMSDataLayerException{
		DataLayerResultWrapper pack = null;
		ArrayList parameters = new ArrayList();
		parameters.add(key);
		parameters.add(packUOM5);
		pack = DataLayerInterface.getResult(DataLayerQueryList.QUERY_PACK_BY_PACKKEY_PACKUOM5,parameters,context);
		return pack;
	}
	
	public static DataLayerResultWrapper getPackByKeyAndUOM6(String key, String packUOM6, WMSValidationContext context) throws WMSDataLayerException{
		DataLayerResultWrapper pack = null;
		ArrayList parameters = new ArrayList();
		parameters.add(key);
		parameters.add(packUOM6);
		pack = DataLayerInterface.getResult(DataLayerQueryList.QUERY_PACK_BY_PACKKEY_PACKUOM6,parameters,context);
		return pack;
	}
	
	public static DataLayerResultWrapper getPackByKeyAndUOM7(String key, String packUOM7, WMSValidationContext context) throws WMSDataLayerException{
		DataLayerResultWrapper pack = null;
		ArrayList parameters = new ArrayList();
		parameters.add(key);
		parameters.add(packUOM7);
		pack = DataLayerInterface.getResult(DataLayerQueryList.QUERY_PACK_BY_PACKKEY_PACKUOM7,parameters,context);
		return pack;
	}
	
	public static DataLayerResultWrapper getPackByKeyAndUOM8(String key, String packUOM8, WMSValidationContext context) throws WMSDataLayerException{
		DataLayerResultWrapper pack = null;
		ArrayList parameters = new ArrayList();
		parameters.add(key);
		parameters.add(packUOM8);
		pack = DataLayerInterface.getResult(DataLayerQueryList.QUERY_PACK_BY_PACKKEY_PACKUOM8,parameters,context);
		return pack;
	}
	
	public static DataLayerResultWrapper getPackByKeyAndUOM9(String key, String packUOM9, WMSValidationContext context) throws WMSDataLayerException{
		DataLayerResultWrapper pack = null;
		ArrayList parameters = new ArrayList();
		parameters.add(key);
		parameters.add(packUOM9);
		pack = DataLayerInterface.getResult(DataLayerQueryList.QUERY_PACK_BY_PACKKEY_PACKUOM9,parameters,context);
		return pack;
	}
}