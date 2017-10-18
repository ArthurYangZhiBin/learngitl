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

package com.ssaglobal.scm.wms.wm_check_pack.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.metadata.DropdownContentsContext;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.ssaglobal.scm.wms.wm_codes.computed.CodesComputedAttrDom;

import java.util.List;

/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 */
public class CheckPackCarrierServices extends
		com.epiphany.shr.ui.metadata.AttributeDomainExtensionBase {

	/**
	 * Fires whenever the values and/or labels for an attribute domain are
	 * requested. The base list of labels and values are provided and may be
	 * filtered, modified, replaced, etc as desired.
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.metadata.DropdownContentsContext
	 * DropdownContentsContext} exposes information about the context in which
	 * the attribute domain is being used, including the service and the user
	 * interface {@link com.epiphany.shr.ui.state.StateInterface state} and
	 * {@link com.epiphany.shr.ui.view.RuntimeFormWidgetInterface form widget}.</li>
	 * 
	 * @param context
	 *            the
	 *            {@link com.epiphany.shr.ui.metadata.DropdownContentsContext
	 *            DropdownContentsContext}
	 * @param values
	 *            the list of values to be modified
	 * @param labels
	 *            the corresponding list of labels to be modified
	 */
	protected int execute(DropdownContentsContext context, List values,
			List labels) throws EpiException {
		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		return RET_CONTINUE;
	}

	/**
	 * Fires whenever the values and/or labels for a hierarchical attribute
	 * domain are requested. The base list of labels and values are provided and
	 * may be filtered, modified, replaced, etc as desired. The filter list
	 * provides the list of selections in the hierarchy to be used to determine
	 * the result.
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.metadata.DropdownContentsContext
	 * DropdowContentsContext} exposes information about the context in which
	 * the attribute domain is being used, including the service and the user
	 * interface {@link com.epiphany.shr.ui.state.StateInterface state} and
	 * {@link com.epiphany.shr.ui.view.RuntimeFormWidgetInterface form widget}.</li>
	 * 
	 * @param context
	 *            the
	 *            {@link com.epiphany.shr.ui.metadata.DropdownContentsContext
	 *            DropdownContentsContext}
	 * @param values
	 *            the list of values to be modified
	 * @param labels
	 *            the corresponding list of labels to be modified
	 * @param filter
	 *            the list of currently selected widget values on which this
	 *            dropdown depends
	 */
	protected int execute(DropdownContentsContext context, List values,
			List labels, List filter) throws EpiException {

		

		for (Object f : filter) {
			if (f instanceof String) {
				CodesComputedAttrDom codesComputedAttrDom = new CodesComputedAttrDom();
				String carrier = (String) f;
				codesComputedAttrDom
						.getCodeLookup(
								context,
								values,
								labels,
								null,
								carrier,
								null,
								"wm_codesdetail.SEQUENCE ASC, wm_codesdetail.DESCRIPTION ASC",
								"");
			}
			if(values.size() == 0){
				CodesComputedAttrDom codesComputedAttrDom = new CodesComputedAttrDom();
				String carrier = (String) f;
				codesComputedAttrDom
						.getCodeLookup(
								context,
								values,
								labels,
								null,
								"GEN",
								null,
								"wm_codesdetail.SEQUENCE ASC, wm_codesdetail.DESCRIPTION ASC",
								"");
			}

		}

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		return RET_CONTINUE;
	}
}