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

package com.ssaglobal.scm.wms.wm_outbound_activity.bio;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.beans.BioService;
import com.epiphany.shr.data.beans.ejb.BioServiceUtil;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;

// TODO: Auto-generated Javadoc
/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * .
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class OutboundActivityOrderBio extends com.epiphany.shr.data.bio.extensions.BioExtensionBase {

	protected static ILoggerCategory log = LoggerFactory
			.getInstance(OutboundActivityOrderBio.class);

	/**
	 * {@inheritDoc}
	 */
	protected int bioAfterUpdate(EpnyServiceContext context, BioRef bioChanged) throws EpiException {
		outboundWorkFlowAction(context, bioChanged);

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		// Leave the line if the code is not going to handle
		// bioAfterUpdate events

		return RET_CONTINUE;
	}

	/**
	 * Outbound work flow action.
	 * 
	 * @param context
	 *            the context
	 * @param bioChanged
	 *            the bio changed
	 * @throws EpiException
	 *             the epi exception
	 */
	private void outboundWorkFlowAction(EpnyServiceContext context, BioRef bioChanged)
			throws EpiException {
		BioService service = null;
		UnitOfWork uow = null;
		Bio bio = null;
		try {
			String appName = context.getAppName();
			service = BioServiceUtil.getBioService(appName);
			uow = service.getUnitOfWork();
			bio = uow.fetchBio(bioChanged);
			String orderkey = bio.getString("ORDERKEY");
			log.info("OutboundActivityOrderBio_outboundWorkFlowAction", "Orderkey " + orderkey,
					SuggestedCategory.APP_EXTENSION);
			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
			Array params = new Array();
			// Store parameters for stored procedure call
			params.add(new TextData(orderkey));
			// Set actionProperties for stored procedure call
			actionProperties.setProcedureParameters(params);
			actionProperties.setProcedureName("NSPOUTBOUNDWORKFLOW");
			try {
				// Run stored procedure
				log.info("OutboundActivityOrderBio_outboundWorkFlowAction", "Calling SP " + actionProperties.getProcedureName() + " with " + actionProperties.getProcedureParametes().get(1),
						SuggestedCategory.APP_EXTENSION);
				WmsWebuiActionsImpl.doAction(actionProperties);
			} catch (WebuiException e) {
				throw new EpiException(e.getMessage(), e.getMessage(), new Object[] {});
			}
		} finally {
			if (uow != null) {
				uow.close();
			}
			if (service != null) {
				service.remove();
			}
		}

	}

	/**
	 * {@inheritDoc}
	 */
	protected int bioAfterInsert(EpnyServiceContext context, BioRef bioInserted)
			throws EpiException {
		outboundWorkFlowAction(context, bioInserted);

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		// Leave the line if the code is not going to handle
		// bioAfterInsert events

		return RET_CONTINUE;
	}

}
