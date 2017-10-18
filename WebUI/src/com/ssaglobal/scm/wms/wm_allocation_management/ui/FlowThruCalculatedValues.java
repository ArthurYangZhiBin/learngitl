package com.ssaglobal.scm.wms.wm_allocation_management.ui;

import java.math.BigDecimal;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.epiphany.shr.data.error.BioCollInsufficientElementsException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

public class FlowThruCalculatedValues implements ComputedAttributeSupport {

	protected static ILoggerCategory log = LoggerFactory
			.getInstance(FlowThruCalculatedValues.class);
	
	
	public Object get(Bio bio, String attributeName, boolean isOldValue)
			throws EpiDataException {
		if("DEMAND2".equals(attributeName)){
			String field = "QTYTOPROCESS";
			return sumOrderDetails(bio, field);
		}else if("RESERVE2".equals(attributeName))
		{
			String field = "PROCESSEDQTY";
			return sumOrderDetails(bio, field);
		}
		return null;
	}

	private Object sumOrderDetails(Bio bio, String field)
			throws EpiDataException {
		BigDecimal total = new BigDecimal(0);
		BioCollection orders = bio.getBioCollection("ORDERDETAILS");
		try {
			for(int i = 0; i < orders.size(); i++) {
				BioCollection orderDetail = orders.elementAt(i).getBioCollection("ORDER_DETAIL");
				Object sum = orderDetail.sum(field);
				total = total.add((BigDecimal)sum);
			}
		} catch (BioCollInsufficientElementsException e) {
			//ignoring
			e.printStackTrace();
			log.error("LOG_ERROR_EXTENSION", "Out of bounds error purposely ignored" + e.getErrorMessage(), SuggestedCategory.NONE);
			log.error("LOG_ERROR_EXTENSION", e.getStackTraceAsString(), SuggestedCategory.NONE);
		}
		return total;
	}

	
	public void set(Bio arg0, String arg1, Object arg2, boolean arg3)
			throws EpiDataException {
		// TODO Auto-generated method stub

	}

	
	public boolean supportsSet(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
