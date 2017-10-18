package com.ssaglobal.scm.wms.wm_cxstorer.ui;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.QueryHelper;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.wm_item.ui.Tab;

public class CXStorerTab extends Tab {

	DataBean storerFocus;

	public CXStorerTab(RuntimeFormInterface f, DataBean parentFocus,
			StateInterface st) {
		super(f, st);
		storerFocus = parentFocus;
	}

	public void run() throws EpiDataException, UserException {
		if (isInsert) {
			cxStorerDuplication();
		}

	}

	private void cxStorerDuplication() throws EpiDataException, UserException {
		String owner = BioAttributeUtil.getString(storerFocus, "STORERKEY");
		String type = BioAttributeUtil.getString(storerFocus, "TYPE");
		String accountNo = BioAttributeUtil.getString(focus, "ACCOUNTNO");
		String cartonType = BioAttributeUtil.getString(focus, "CARTONTYPE");

		UnitOfWorkBean tempUnitOfWork = state.getTempUnitOfWork();
		BioCollectionBean rs = tempUnitOfWork.getBioCollectionBean(new Query(
				"wm_storercontainerexchange",
				"wm_storercontainerexchange.STORERKEY = '"
						+ QueryHelper.escape(owner)
						+ "' and wm_storercontainerexchange.ACCOUNTNO = '"
						+ QueryHelper.escape(accountNo)
						+ "' and wm_storercontainerexchange.CARTONTYPE = '"
						+ QueryHelper.escape(cartonType)
						+ "' and wm_storercontainerexchange.TYPE = '"
						+ QueryHelper.escape(type) + "'", null));
		if (rs.size() > 0) {
			throw new UserException("WMEXP_DUP_CXSTORER", new String[] { owner,
					accountNo, cartonType });
		}

	}
}
