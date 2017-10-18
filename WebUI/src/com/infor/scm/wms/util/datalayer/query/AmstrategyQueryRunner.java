package com.infor.scm.wms.util.datalayer.query;

import java.util.ArrayList;

import com.infor.scm.wms.util.datalayer.WMSDataLayerException;
import com.infor.scm.wms.util.datalayer.driver.DataLayerInterface;
import com.infor.scm.wms.util.datalayer.driver.DataLayerQueryList;
import com.infor.scm.wms.util.datalayer.resultwrappers.DataLayerResultWrapper;
import com.infor.scm.wms.util.validation.WMSValidationContext;

public class AmstrategyQueryRunner extends BaseQueryRunner {
	
	public static DataLayerResultWrapper getAmstrategyByAmstrategyKey(String amstrategyKey, WMSValidationContext context) throws WMSDataLayerException{
		DataLayerResultWrapper amstrategy = null;
		ArrayList parameters = new ArrayList();
		parameters.add(amstrategyKey);				
		amstrategy = DataLayerInterface.getResult(DataLayerQueryList.QUERY_AMSTRATEGY_BY_AMSTRATEGYKEY,parameters, context);
		return amstrategy;
	}

}
