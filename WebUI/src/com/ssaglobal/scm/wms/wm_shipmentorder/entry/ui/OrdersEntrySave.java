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



package com.ssaglobal.scm.wms.wm_shipmentorder.entry.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

// TODO: Auto-generated Javadoc
/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * .
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class OrdersEntrySave extends
		com.epiphany.shr.ui.action.ActionExtensionBase {

	/** The log. */
	protected static ILoggerCategory log = LoggerFactory
			.getInstance(OrdersEntrySave.class);

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked
	 * or a value entered in a form in a modal dialog Write code here if u want
	 * this to be called when the UI Action event is fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext
	 * ModalActionContext} exposes information about the event, including the
	 * service and the user interface
	 * 
	 * @param ctx
	 *            the ctx
	 * @param args
	 *            the args
	 * @return the int
	 * @throws EpiException
	 *             the epi exception
	 *             {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 *             <li>{@link com.epiphany.shr.ui.action.ActionResult
	 *             ActionResult} exposes information about the results of the
	 *             action that has occurred, and enables your extension to
	 *             modify them.</li>
	 *             </ul>
	 */
	@Override
	protected int execute(ModalActionContext ctx, ActionResult args)
			throws EpiException {

		UnitOfWorkBean uow = ctx.getState().getDefaultUnitOfWork();
		if (OrdersEntryUtil.savingDetailOnly(ctx)) {
			DataBean orderDetail = OrdersEntryUtil.getDetailFocus(ctx);

			BioBean parentBioBean = (BioBean) OrdersEntryUtil
					.getOrdersHeaderForm(ctx).getFocus();
			if (orderDetail != null) {
				parentBioBean.addBioCollectionLink("ORDER_DETAIL",
						(QBEBioBean) orderDetail);
			}

		} else {
			DataBean orders = OrdersEntryUtil.getParentFocus(ctx);
			DataBean orderDetail = OrdersEntryUtil.getDetailFocus(ctx);
			
			BioBean parentBioBean = uow.getNewBio((QBEBioBean) orders);
			if (orderDetail != null) {
				// If the user didn't enter in an item, assume they didnt want
				// to
				// save a detail
				if (orderDetail.getValue("SKU") == null) {
					orderDetail.removeFromBeanManager();
				} else {
					parentBioBean.addBioCollectionLink("ORDER_DETAIL",
							(QBEBioBean) orderDetail);
				}
			}
		}
		

		try {
			uow.saveUOW(true);

		} catch (UnitOfWorkException e) {
			e.printStackTrace();
			Throwable nested = (e).findDeepestNestedException();
			if (nested instanceof ServiceObjectException) {
				String reasonCode = nested.getMessage();
				log.error("LOG_ERROR_EXTENSION_OwnerSave_execute", reasonCode,
						SuggestedCategory.NONE);
				throw new UserException(reasonCode, new Object[] {});
			} else {
				throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
			}
		}

		return RET_CONTINUE;
	}
}
