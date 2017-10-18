/**
 * ---Begin Copyright Notice---20090105T1353
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2010 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */
package com.ssaglobal.scm.wms.wm_shipmentorder.action.util;

import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.StringUtils;

public class ShipmentOrderPickActionUtil {

	/**
	 * To loc validation.
	 * 
	 * @param focus
	 *            the focus
	 * @param loc
	 *            the loc
	 * @throws EpiDataException
	 *             the epi data exception
	 * @throws UserException
	 *             the user exception
	 */
	public static void setToLoc(DataBean focus, String loc)
			throws EpiDataException, UserException {
		// 02/08/2010 FW: Added code to populate toloc, CARTONGROUP and
		// CARTONTYPE fields if pickdetail is added manually
		// (Incident3209733_Defect215086) -- Start
		// ToLoc Validation
		/*
		 * if(!(isEmpty(focus.getValue("TOLOC")) ||
		 * isEmpty(focus.getValue("LOC")))){ String toLoc =
		 * focus.getValue("TOLOC").toString(); //Query Loc Table, get Location
		 * Type //Compare with toLoc, should be PickTo or Staged //If they do
		 * not match throw exception
		 * 
		 * String query =
		 * "SELECT LOC.LOC, PUTAWAYZONE.PICKTOLOC FROM LOC INNER JOIN PUTAWAYZONE ON LOC.PUTAWAYZONE=PUTAWAYZONE.PUTAWAYZONE WHERE LOC.LOC='"
		 * +loc+"'"; EXEDataObject results =
		 * WmsWebuiValidationSelectImpl.select(query); if(results.getRowCount()
		 * == 1){ String locationType = results.getAttribValue(2).getAsString();
		 * if(!(locationType.equalsIgnoreCase(toLoc))){ parameter[0] = toLoc;
		 * throw new UserException("WMEXP_PICK_TOLOC", parameter); } } }
		 */
		String query = null;
		EXEDataObject results = null;
		if (!(StringUtils.isEmpty(BioAttributeUtil.getString(focus, "LOC")))) {
			query = "SELECT LOC.LOC, PUTAWAYZONE.PICKTOLOC FROM LOC INNER JOIN PUTAWAYZONE ON LOC.PUTAWAYZONE=PUTAWAYZONE.PUTAWAYZONE WHERE LOC.LOC='"
					+ loc + "'";
			results = WmsWebuiValidationSelectImpl.select(query);
			if (results.getRowCount() == 1) {
				String pickToLoc = results.getAttribValue(2).getAsString();
				if (!(StringUtils.isEmpty(BioAttributeUtil.getString(focus,
						"TOLOC")))) {
					String toLoc = focus.getValue("TOLOC").toString();
					if (!(pickToLoc.equalsIgnoreCase(toLoc))) {
						String[] parameter = new String[1];
						parameter[0] = toLoc;
						throw new UserException("WMEXP_PICK_TOLOC", parameter);
					}
				} else {
					// populate toloc if pickdetail is added manually
					focus.setValue("TOLOC", pickToLoc);
				}
			}
		}

		// populate CARTONGROUP and CARTONTYPE fields if pickdetail is added
		// manually
		if (StringUtils.isEmpty(BioAttributeUtil
				.getString(focus, "CARTONGROUP"))
				|| StringUtils.isEmpty(BioAttributeUtil.getString(focus,
						"CARTONTYPE"))) {
			// get CARTONGROUP and CARTONTYPE fields
			results = null;
			String sku = focus.getValue("SKU").toString();
			String storerkey = focus.getValue("STORERKEY").toString();
			query = "SELECT MAX(SKU.CARTONGROUP) CARTONGROUP, MAX(CARTONIZATION.CARTONTYPE) CARTONTYPE FROM SKU INNER JOIN CARTONIZATION ON SKU.CARTONGROUP = CARTONIZATION.CARTONIZATIONGROUP WHERE SKU.SKU='"
					+ sku + "' AND SKU.STORERKEY='" + storerkey + "'";
			results = WmsWebuiValidationSelectImpl.select(query);

			if (results.getRowCount() == 1) {
				if (StringUtils.isEmpty(BioAttributeUtil.getString(focus,
						"CARTONGROUP"))) {
					focus.setValue("CARTONGROUP", results.getAttribValue(1)
							.getAsString());
				}
				if (StringUtils.isEmpty(BioAttributeUtil.getString(focus,
						"CARTONTYPE"))) {
					focus.setValue("CARTONTYPE", results.getAttribValue(2)
							.getAsString());
				}
			}
		}
		results = null;
		// 02/08/2010 FW: Added code to populate toloc, CARTONGROUP and
		// CARTONTYPE fields if pickdetail is added manually
		// (Incident3209733_Defect215086) -- End
	}

	public static void toLocValidation(BioBean pickDetail) throws EpiDataException,
			UserException {
		String toLoc = BioAttributeUtil.getString( pickDetail ,"TOLOC");
		String loc = BioAttributeUtil.getString( pickDetail ,"LOC");
		if (StringUtils.isEmpty(toLoc)){
			//Validate current Location is of type PICKTO
			String locValue = StringUtils.isEmpty(loc) ?  "" : loc.toString();
			setToLoc(pickDetail, locValue);	
		}else{
			//Set ToLocation to Location and set ToLocation to empty
			pickDetail.set("LOC", toLoc);
			pickDetail.set("TOLOC", "");
		}
	}

}
