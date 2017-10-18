package com.ssaglobal.scm.wms.util.attribute;

import java.util.HashMap;
import java.util.Map;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.QueryHelper;
import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.objects.bio.BioType;

public class StorerTypeAttribute implements ComputedAttributeSupport {

	static Map<String, TypeInfo> info = new HashMap<String, TypeInfo>();

	static {
		info.put("wm_loadscheduledetailCUSTOMERNAME", new TypeInfo(
				"CONSIGNEEKEY", "2", "COMPANY"));

		info.put("wm_ibactheaderFACILITYNAME", new TypeInfo("FACILITY", "7",
				"COMPANY"));
		info.put("wm_ibactheaderOWNERNAME", new TypeInfo("OWNER", "1",
				"COMPANY"));
		info.put("wm_ibactheaderSUPPLIERNAME", new TypeInfo("SUPPLIER", "12",
				"COMPANY"));

		info.put("wm_obactheaderCUSTOMERNAME", new TypeInfo("CUSTOMER", "2",
				"COMPANY"));
		info.put("wm_obactheaderFACILITYNAME", new TypeInfo("FACILITY", "7",
				"COMPANY"));
		info.put("wm_obactheaderOWNERNAME", new TypeInfo("OWNER", "1",
				"COMPANY"));

		info.put("wm_intransitSTORER_ADD1", new TypeInfo("STORERKEY", "1", "ADDRESS1"));
		info.put("wm_intransitSTORER_ADD2", new TypeInfo("STORERKEY", "1", "ADDRESS2"));
		info.put("wm_intransitSTORER_ADD3", new TypeInfo("STORERKEY", "1", "ADDRESS3"));
		info.put("wm_intransitSTORER_ADD4", new TypeInfo("STORERKEY", "1", "ADDRESS4"));
		info.put("wm_intransitSTORER_CITY", new TypeInfo("STORERKEY", "1", "CITY"));
		info.put("wm_intransitSTORER_STATE", new TypeInfo("STORERKEY", "1", "STATE"));
		info.put("wm_intransitSTORER_ZIP", new TypeInfo("STORERKEY", "1", "ZIP"));
		
		info.put("receiptADDRESS5", new TypeInfo("SupplierCode", "12", "ADDRESS5"));
		info.put("receiptADDRESS6", new TypeInfo("SupplierCode", "12", "ADDRESS6"));
	}

	public Object get(Bio bio, String attributeName, boolean isOldValue)
			throws EpiDataException {
		UnitOfWork uow = bio.getUnitOfWork();
		TypeInfo typeInfo = info.get(bio.getTypeName() + attributeName);
		if (typeInfo == null) {
			return null;
		}
		String storerkeyName = typeInfo.getStorerkey();
		String type = typeInfo.getType();
		String attribute = typeInfo.getAttribute();
		Query query = new Query("wm_storer", "wm_storer.TYPE = '"
				+ QueryHelper.escape(type) + "' and wm_storer.STORERKEY = '"
				+ bio.getString(storerkeyName) + "'", null);
		BioCollection rs = uow.findByQuery(query);
		for (int i = 0; i < rs.size(); i++) {
			Bio result = rs.elementAt(i);
			return result.get(attribute);
		}

		return null;
	}

	
	public void set(Bio bio, String attributeName, Object attributeValue,
			boolean isOldValue) throws EpiDataException {
		// TODO Auto-generated method stub

	}

	
	public boolean supportsSet(String bioTypeName, String attributeName) {
		// TODO Auto-generated method stub
		return true;
	}

	

}
