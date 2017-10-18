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
package com.ssaglobal.scm.wms.wm_codes.computed;

import java.util.List;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.metadata.DropdownContentsContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;


/**
 * TODO Document WaveOrderFieldsCodesComputedAttrDom class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class WaveOrderFieldsCodesComputedAttrDom extends com.epiphany.shr.ui.metadata.AttributeDomainExtensionBase{
	protected static ILoggerCategory log = LoggerFactory.getInstance(WaveOrderFieldsCodesComputedAttrDom.class);
	protected int execute(DropdownContentsContext context, List values,List labels) throws EpiException {

		StateInterface state = context.getState();
		String qry = "wm_codesdetail.LISTNAME = 'ORDRFLD' and wm_codesdetail.ACTIVE = '1'";
		UnitOfWorkBean uow = state.getTempUnitOfWork();
		BioCollectionBean rs = uow.getBioCollectionBean(new Query("wm_codesdetail", qry, "wm_codesdetail.SEQUENCE ASC, wm_codesdetail.DESCRIPTION ASC"));
		int size = rs.size();
		try {
			for (int i = 0; i < rs.size(); i++) {
				BioBean code = rs.get("" + i);
				values.add(code.getValue("SHORT"));
				labels.add(code.getValue("DESCRIPTION"));
			}
		} catch (com.epiphany.shr.data.error.BioCollInsufficientElementsException e) {
			log.error("LOG_ERROR_EXTENSION",
					"Out of bounds error purposely ignored"
							+ e.getErrorMessage(), SuggestedCategory.NONE);
			log.error("LOG_ERROR_EXTENSION", e.getStackTraceAsString(),
					SuggestedCategory.NONE);
		}
		return RET_CONTINUE;
	}


}
