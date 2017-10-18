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
package com.infor.scm.waveplanning.wp_wavemgmt.action;

import java.util.ArrayList;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.infor.scm.waveplanning.common.WavePlanningConstants;
import com.infor.scm.waveplanning.common.WavePlanningException;
import com.infor.scm.waveplanning.common.WavePlanningUtils;
import com.infor.scm.waveplanning.common.util.WPFormUtil;
import com.infor.scm.waveplanning.common.util.WPUserUtil;
import com.infor.scm.waveplanning.common.util.WPUtil;
import com.infor.scm.waveplanning.wp_wave.util.WPWaveUtil;
import com.infor.scm.waveplanning.wp_wavemgmt_confirmwave.action.ConfirmWaveAddOrderNavSelect;

/**
 * TODO Document OrdersNotInWaveFilter class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class OrdersNotInWaveFilter extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(OrdersNotInWaveFilter.class);

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @exception EpiException
	 */
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{

		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE", "Executing OrdersNotInWaveFilter", 100L);
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		String qry = "SELECT DISTINCT ORDERKEY FROM WAVEDETAIL";
		Query query = new Query("wm_wp_orders","DPE('SQL','@[wm_wp_orders.ORDERKEY] NOT IN ("+qry+")')","wm_wp_orders.ORDERKEY DESC");
		BioCollectionBean bcb = uow.getBioCollectionBean(query);
		Query queryExcludeShipped = new Query("wm_wp_orders","wm_wp_orders.STATUS != '95'","wm_wp_orders.ORDERKEY DESC");
		result.setFocus(bcb.filterBean(queryExcludeShipped));
		uow.clearState();
		return RET_CONTINUE;
	}
}
