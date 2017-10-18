/******************************************************
 *
 *                       NOTICE
 *
 *   THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS
 *   CONFIDENTIAL INFORMATION OF INFOR AND SHALL NOT
 *   BE DISCLOSED WITHOUT PRIOR WRITTEN PERMISSION.
 *   LICENSED CUSTOMERS MAY COPY AND ADAPT THIS
 *   SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH
 *   THE TERMS OF THEIR SOFTWARE LICENSE AGREEMENT.
 *   ALL OTHER RIGHTS RESERVED.
 *
 *   COPYRIGHT (c) 2008 INFOR. ALL RIGHTS RESERVED.
 *   THE WORD AND DESIGN MARKS SET FORTH HEREIN ARE
 *   TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 *   AND/OR RELATED AFFILIATES AND SUBSIDIARIES. ALL
 *   RIGHTS RESERVED. ALL OTHER TRADEMARKS LISTED
 *   HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE
 *   OWNERS. WWW.INFOR.COM.
 *
 ******************************************************/

/******************************************************
 *
 *                       NOTICE
 *
 *   THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS
 *   CONFIDENTIAL INFORMATION OF INFOR AND SHALL NOT
 *   BE DISCLOSED WITHOUT PRIOR WRITTEN PERMISSION.
 *   LICENSED CUSTOMERS MAY COPY AND ADAPT THIS
 *   SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH
 *   THE TERMS OF THEIR SOFTWARE LICENSE AGREEMENT.
 *   ALL OTHER RIGHTS RESERVED.
 *
 *   COPYRIGHT (c) 2008 INFOR. ALL RIGHTS RESERVED.
 *   THE WORD AND DESIGN MARKS SET FORTH HEREIN ARE
 *   TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 *   AND/OR RELATED AFFILIATES AND SUBSIDIARIES. ALL
 *   RIGHTS RESERVED. ALL OTHER TRADEMARKS LISTED
 *   HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE
 *   OWNERS. WWW.INFOR.COM.
 *
 ******************************************************/

package com.ssaglobal.scm.wms.wm_owner.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.List;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.util.BioUtil;

/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class InsertOwnerBillTo extends
		com.epiphany.shr.ui.action.ActionExtensionBase {

	private static final String _TYPE_OWNERBILLTO = "14";

	/**
	 * The code within the execute method will be run from a UIAction specified
	 * in metadata.
	 * <P>
	 * 
	 * @param context
	 *            The ActionContext for this extension
	 * @param result
	 *            The ActionResult for this extension (contains the focus and
	 *            perspective for this UI Extension)
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * 
	 * @exception EpiException
	 */
	protected int execute(ActionContext context, ActionResult result)
			throws EpiException {
		
		DataBean focus = result.getFocus();
		if(focus.isTempBio()){
			//insert a new Owner Bill To (Type = 14) Using this Owner 
			//At this point the owner is validated, so I'm not doing any checks
			StateInterface state = context.getState();
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			List<String> copyList = new ArrayList<String>();
			copyList.add("STORERKEY");
			copyList.add("DESCRIPTION");
			copyList.add("COMPANY");
			
			copyList.add("ADDRESS1");
			copyList.add("ADDRESS2");
			copyList.add("ADDRESS3");
			copyList.add("ADDRESS4");
			copyList.add("ADDRESS5");
			copyList.add("ADDRESS6");
			copyList.add("CITY");
			copyList.add("STATE");
			copyList.add("ZIP");
			copyList.add("COUNTRY");
			copyList.add("ISOCNTRYCODE");
			
			copyList.add("PHONE1");
			copyList.add("FAX1");
			copyList.add("EMAIL1");
			copyList.add("CONTACT1");
			
			copyList.add("PHONE2");
			copyList.add("FAX2");
			copyList.add("EMAIL2");
			copyList.add("CONTACT2");
			
			copyList.add("NOTES1");
			copyList.add("NOTES2");
			
			copyList.add("BFSURCHARGE");
			copyList.add("CUBEUOM");
			copyList.add("CURRCODE");
			copyList.add("DIMENUOM");
			copyList.add("DUNSID");
			copyList.add("INVOICELEVEL");
			copyList.add("INVOICETERMS");
			copyList.add("MEASURECODE");
			copyList.add("NONNEGLEVEL");
			copyList.add("QFSURCHARGE");
			copyList.add("RECURCODE");
			copyList.add("TAXEXEMPT");
			copyList.add("TAXEXEMPTCODE");
			copyList.add("TAXID");
			copyList.add("WGTUOM");
			
			
			
			
			
			
			QBEBioBean ownerBillTo = BioUtil.copyBIORestrictive(focus, uow, copyList);
			ownerBillTo.setValue("TYPE", _TYPE_OWNERBILLTO);
			ownerBillTo.save();
		}
//		StateInterface state = context.getState();
//		state.getDefaultUnitOfWork().saveUOW(true);
		
		

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}

}
