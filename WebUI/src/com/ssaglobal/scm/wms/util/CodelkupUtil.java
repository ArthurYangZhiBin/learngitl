package com.ssaglobal.scm.wms.util;

import java.util.ArrayList;

import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.ssaglobal.scm.wms.wm_codes.computed.CodesComputedAttrDom;

public class CodelkupUtil {
	
	public static BioCollectionBean getCodeLookupCollection(String listName, StateInterface state) {
		CodesComputedAttrDom codesComputedAttrDom = new CodesComputedAttrDom();
		return codesComputedAttrDom.getCodeLookupCollection(new ArrayList(), listName, new ArrayList<String>(), null, null, state);
	}

}
