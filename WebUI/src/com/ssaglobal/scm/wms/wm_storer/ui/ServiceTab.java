/**
 * 
 */
package com.ssaglobal.scm.wms.wm_storer.ui;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.QueryHelper;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.StringUtils;
import com.ssaglobal.scm.wms.wm_item.ui.Tab;

public class ServiceTab extends Tab {
	DataBean storerFocus = null;

	public ServiceTab(RuntimeFormInterface f, StateInterface st,
			DataBean storerFocus) {
		super(f, st);
		this.storerFocus = storerFocus;
	}

	public void run() throws EpiException {
		requiredFields();
		if (isInsert) {
			verifyCarrier();
			serviceDuplication();
		}

	}

	private void verifyCarrier() throws UserException, EpiDataException {
		String storerValue = BioAttributeUtil.getString(focus, "CARRIER");
		if(verifyKShipCarrier(storerValue, 3) == false){
			//throw exception
			String[] parameters = new String[3];
			parameters[0] = retrieveLabel("CARRIER");
			parameters[1] = storerValue;
			parameters[2] = retrieveFormTitle();
			throw new UserException("WMEXP_DOES_NOT_EXIST", parameters);
		}
		
	}

	private boolean verifyKShipCarrier(String storerValue, int i) throws EpiDataException {
		storerValue = QueryHelper.escape(storerValue);
		storerValue = storerValue.toUpperCase();
		UnitOfWorkBean uow = state.getTempUnitOfWork();
		String query = "wm_storer.STORERKEY = '" + storerValue + "'";
		query += " and wm_storer.TYPE = '3' and wm_storer.KSHIP_CARRIER = '1'";
		BioCollectionBean rs = uow.getBioCollectionBean(new Query(
				"wm_storer", query, ""));
		if (rs.size() != 1) {
			return false;
		}
		return true;
	}

	private void serviceDuplication() throws EpiDataException, UserException {
		String storer = BioAttributeUtil.getString(storerFocus, "STORERKEY");
		String type = BioAttributeUtil.getString(storerFocus, "TYPE");
		String carrier = BioAttributeUtil.getString(focus, "CARRIER");
		String serviceCode = BioAttributeUtil.getString(focus, "SERVICECODE");
		UnitOfWorkBean tuow = state.getTempUnitOfWork();
		BioCollectionBean rs = tuow.getBioCollectionBean(new Query(
				"wm_spsspecialsvcs", "wm_spsspecialsvcs.STORERKEY = '" + storer
						+ "'" + " and wm_spsspecialsvcs.CARRIER = '" + carrier
						+ "'" + " and wm_spsspecialsvcs.SERVICECODE = '"
						+ serviceCode + "' and wm_spsspecialsvcs.TYPE = '" + type + "'", null));
		if (rs.size() >= 1) {

			throw new UserException(
					"DUPLICATE_GENERIC",
					new Object[] { removeTrailingColon(retrieveLabel("CARRIER"))
							+ ", "
							+ removeTrailingColon(retrieveLabel("SERVICECODE")) });
		}

	}

	private void requiredFields() throws UserException {
		String serviceCode = BioAttributeUtil.getString(focus, "SERVICECODE");

		if ("INS".equals(serviceCode)) {
			String displayValue = form.getFormWidgetByName("PARAMETERSTRING")
					.getDisplayValue();
			if (StringUtils.isEmpty(displayValue)) {
				// string is req'd
				throw new UserException(
						"WMEXP_REQ",
						new Object[] { removeTrailingColon(retrieveLabel("PARAMETERSTRING")) });
			}

		}
	}

	private UnitOfWorkBean uow() {
		return state.getDefaultUnitOfWork();
	}

}