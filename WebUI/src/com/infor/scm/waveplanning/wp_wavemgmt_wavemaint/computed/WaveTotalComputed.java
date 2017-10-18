package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.computed;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.epiphany.shr.data.error.EpiDataException;

public class WaveTotalComputed implements ComputedAttributeSupport{
	public Object get(Bio bio, String attributeName, boolean isOldValue) throws EpiDataException
	{
		BioCollection bc = bio.getBioCollection("ORDERBIOCOLLECTION");
		if("TOTALORDERS".equalsIgnoreCase(attributeName)){
			int size = bc.size();
			return new Integer(size);
		}
		if("TOTALLINES".equalsIgnoreCase(attributeName)){
			return bc.sum("TOTALORDERLINES");
		}
		if("TOTALQUANTITY".equalsIgnoreCase(attributeName)){
			return bc.sum("TOTALQTY");
		}
		if("TOTALWEIGHT".equalsIgnoreCase(attributeName)){
			return bc.sum("TOTALGROSSWGT");
		}
		if("TOTALCUBE".equalsIgnoreCase(attributeName)){
			return bc.sum("TOTALCUBE");
		}
		return new Double(0);
		
	}

	public void set(Bio arg0, String arg1, Object arg2, boolean arg3) throws EpiDataException
	{
		// TODO Auto-generated method stub

	}

	public boolean supportsSet(String arg0, String arg1)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
