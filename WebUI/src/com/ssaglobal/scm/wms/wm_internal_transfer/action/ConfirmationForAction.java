package com.ssaglobal.scm.wms.wm_internal_transfer.action;

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.agileitp.forte.genericdbms.DBResourceException;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceManager;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationDeleteImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationUpdateImpl;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.dutilitymanagement.dto.SkuSNConfDTO;
import com.ssaglobal.scm.wms.util.dao.SkuSNConfDAO;
import com.synnex.utils.SimpleJDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

public class ConfirmationForAction extends ActionExtensionBase {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ConfirmationForAction.class);
	protected static String FROM_STORER = "FROMSTORERKEY";
	protected static String FROM_ITEM = "FROMSKU";
	protected static String FROM_LOC = "FROMLOC";
	protected static String FROM_LOT = "FROMLOT";
	protected static String FROM_ID = "FROMID";
	protected static String TO_STORER = "TOSTORERKEY";
	protected static String TO_QTY = "TOQTY";
	protected static String TRANSFER_KEY = "TRANSFERKEY";
	protected static String TRANSFER_LINE_NUMBER = "TRANSFERLINENUMBER";
	protected static String STORERKEY = "STORERKEY";
	protected static String ITEM = "SKU";
	protected static String LOT = "LOT";
	protected static String LOC = "LOC";
	protected static String ID = "ID";
	protected static String SERIALNUMBER = "SERIALNUMBER";
	protected static String TDS_LINK = "TRANSFERDETAILSERIAL";
	protected static String QTY = "QTY";
	protected static String DATA2 = "DATA2";
	protected static String DATA3 = "DATA3";
	protected static String DATA4 = "DATA4";
	protected static String DATA5 = "DATA5";
	protected static String NETWEIGHT = "NETWEIGHT";
	protected static String SERIAL_BIO = "wm_serialinventory";
	protected static String ITRN_TRANS_SERIAL = "wm_internal_transfer_serial";
	protected static String TABLE = "wm_internal_transfer_serial_temp";
	protected static String LLI_BIO = "wm_lotxlocxid";

	protected String fromStorer = "";
	protected String fromItem = "";

	protected int execute(ActionContext context, ActionResult result) throws EpiException {
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER", "Executing ConfirmationForAction- Non Modal", 100L);
		System.out.println("in  here1");
		StateInterface state = context.getState();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		if (headerForm.isListForm()) {
			RuntimeListFormInterface listForm = (RuntimeListFormInterface) headerForm;
			ArrayList items = listForm.getAllSelectedItems();
			if (isZero(items)) {
				throw new UserException("WMEXP_NONE_SELECTED", new Object[0]);
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER", "Exiting ConfirmationForAction- Non Modal", 100L);
		return 0;
	}

	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER", "Executing ConfirmationForAction- Modal", 100L);
		System.out.println("in  here2");
		StateInterface state = ctx.getState();
		try {
			state.closeModal(true);
			RuntimeFormInterface toolbarForm = ctx.getSourceForm();
			RuntimeFormInterface shellForm = toolbarForm.getParentForm(state);
			SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
			RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
			if (!headerForm.isListForm()) {
				headerNormalForm(headerForm, ctx, state, args);
			} else {
				RuntimeListFormInterface listForm = (RuntimeListFormInterface) state.getRuntimeForm(headerSlot, null);
				DataBean listFocus = listForm.getFocus();
				if ((listFocus instanceof BioCollection)) {
					_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER", "Finalizing Record", 100L);
					ArrayList itemsSelected = listForm.getAllSelectedItems();

					if ((itemsSelected != null) && (itemsSelected.size() > 0)) {
						Iterator bioBeanIter = itemsSelected.iterator();
						UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
						try {
							BioBean bean = null;
							while (bioBeanIter.hasNext()) {
								bean = (BioBean) bioBeanIter.next();
								finalizeStatus(state, bean);
								String testKeyVal = bean.getValue("TRANSFERKEY").toString();
								changeSkuxlocType(state,testKeyVal);
							}
							try {
								_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER", "Setting object to session", 100L);
								ctx.getServiceManager().getUserContext().put("POSTFINALIZE", "closeModalDialog41");
								uowb.saveUOW(true);
								uowb.clearState();
								args.setFocus(bean);
							} catch (UnitOfWorkException ex) {
								_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER", "Throwing UOW Exception", 100L);
								Throwable nested = ex.getDeepestNestedException();
								if ((nested instanceof ServiceObjectException)) {
									String reasonCode = nested.getMessage();
									String errorMessage = getTextMessage("WMEXP_EJB_ERROR", new Object[] { reasonCode }, state.getLocale());

									state.getRequest().getSession().setAttribute("CAERRORMESSAGE", errorMessage);
								} else {
									_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER", "Error Finalizing", 100L);
									throwUserException(ex, "ERROR_FINALIZING", null);
								}
							}
						} catch (RuntimeException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (RuntimeException e1) {
			_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER", "Runtimg Exception", 100L);
			e1.printStackTrace();
		}
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER", "Exiting ConfirmationForAction()", 100L);
		return 0;
	}

	private void headerNormalForm(RuntimeFormInterface headerForm, ModalActionContext ctx, StateInterface state, ActionResult result) throws EpiException {
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER", "Executing headerNormalForm()", 100L);
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();

		DataBean focus = headerForm.getFocus();
		if (focus.isTempBio()) {
			QBEBioBean qbe = (QBEBioBean) focus;

			String testKeyVal = qbe.getValue("TRANSFERKEY").toString();
			changeSkuxlocType(state,testKeyVal);
			Query qry = new Query("wm_internal_transfer", "wm_internal_transfer.TRANSFERKEY='" + testKeyVal + "'", null);
			BioCollectionBean newFocus = uowb.getBioCollectionBean(qry);

			if (newFocus.size() == 0) {
				_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER", "Error: Please Save before finalizing", 100L);
				String errorMessage = getTextMessage("WMEXP_MUST_SAVE", new Object[0], state.getLocale());
				state.getRequest().getSession().setAttribute("CAERRORMESSAGE", errorMessage);
				result.setFocus(qbe);
			}
		} else {
			BioBean bean = (BioBean) focus;
			focus = (BioBean) focus;
			String keyVal = bean.getValue("TRANSFERKEY").toString();
			changeSkuxlocType(state,keyVal);
			Query qry = new Query("wm_internaltransferdetail", "wm_internaltransferdetail.TRANSFERKEY='" + keyVal + "'", null);
			BioCollectionBean newFocus = uowb.getBioCollectionBean(qry);
			_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER", "BioCollection size: " + newFocus.size(), 100L);
			if (newFocus.size() < 1) {
				String status = bean.get("STATUS").toString();
				if (!status.equals("9")) {
					bean.setValue("STATUS", "9");
					_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER", "New Status: " + bean.get("STATUS").toString(), 100L);
				}
			} else {
				finalizeStatus(state, bean);
			}
			try {
				uowb.saveUOW(true);
				uowb.clearState();
				result.setFocus(bean);
			} catch (UnitOfWorkException ex) {
				Throwable nested = ex.getDeepestNestedException();
				if ((nested instanceof ServiceObjectException)) {
					String reasonCode = nested.getMessage();

					String errorMessage = getTextMessage("WMEXP_EJB_ERROR", new Object[] { reasonCode }, state.getLocale());

					state.getRequest().getSession().setAttribute("CAERRORMESSAGE", errorMessage);
				} else {
					throwUserException(ex, "ERROR_FINALIZING", null);
				}
			}
		}
		ctx.getServiceManager().getUserContext().put("POSTFINALIZE", "closeModalDialog98");
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER", "Exiting headerNormalForm()", 100L);
	}

	private boolean isZero(ArrayList items) {
		if (items == null)
			return true;
		if (items.size() == 0) {
			return false;
		}
		return false;
	}

	private void finalizeStatus(StateInterface state, BioBean bean) throws EpiException {
		BioCollectionBean childBiocollection = (BioCollectionBean) bean.get("INTERNALTRANSFERDETAIL");
		for (int j = 0; j < childBiocollection.size(); j++) {
			BioBean detailBean = (BioBean) childBiocollection.elementAt(j);
			if (!detailBean.getValue("STATUS").toString().equals("9")) {
				detailBean.set("STATUS", "9");
				_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER", "New detail Status: " + detailBean.get("STATUS").toString(), 100L);
			}
			this.fromStorer = ((String) detailBean.getValue(FROM_STORER));
			this.fromItem = ((String) detailBean.getValue(FROM_ITEM));
			SkuSNConfDTO snInfo = SkuSNConfDAO.getSkuSNConf(this.fromStorer, this.fromItem);
			serialNumberSave(state, detailBean, snInfo.getSNum_EndToEnd().equals("1"));
			bean.addToBioCollection("INTERNALTRANSFERDETAIL", detailBean);
		}
		if (!bean.get("STATUS").toString().equals("9")) {
			bean.set("STATUS", "9");
			_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER", "New Status was not 9: " + bean.get("STATUS").toString(), 100L);
		}
	}

	private void serialNumberSave(StateInterface state, BioBean bean, boolean isSerial) throws DataBeanException, EpiDataException {
		if (isSerial) {
			String loc = bean.getValue(FROM_LOC).toString();
			String lot = bean.getValue(FROM_LOT).toString();
			String id = bean.getValue(FROM_ID).toString();
			String qryStr = LLI_BIO + "." + ITEM + "='" + this.fromItem + "' AND " + LLI_BIO + "." + LOC + "='" + loc + "' AND " + LLI_BIO + "." + LOT + "='" + lot + "' AND " + LLI_BIO + "." + ID + "='" + id + "'";

			Query qry = new Query(LLI_BIO, qryStr, null);
			BioCollectionBean bcBean = state.getDefaultUnitOfWork().getBioCollectionBean(qry);
			if (bcBean.size() > 0) {
				String transKey = bean.getValue(TRANSFER_KEY).toString();
				String transLineNo = bean.getValue(TRANSFER_LINE_NUMBER).toString();

				String whereClause = TABLE + "." + TRANSFER_KEY + "='" + transKey + "' AND " + TABLE + "." + TRANSFER_LINE_NUMBER + "='" + transLineNo + "'";
				qry = new Query(TABLE, whereClause, null);
				BioCollectionBean serialFocus = state.getDefaultUnitOfWork().getBioCollectionBean(qry);
				for (int index = 0; index < serialFocus.size(); index++) {
					UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
					BioBean bioTemp = (BioBean) serialFocus.elementAt(index);
					QBEBioBean temp = buildNewInternalTransferSerial(uowb, bioTemp);
					bean.addBioCollectionLink(TDS_LINK, temp);
				}
				clearTemp(transKey, transLineNo);
			}
		}
	}

	private QBEBioBean buildNewInternalTransferSerial(UnitOfWorkBean uowb, BioBean serialTemp) throws DataBeanException {
		QBEBioBean itrnTransSerial = uowb.getQBEBioWithDefaults(ITRN_TRANS_SERIAL);
		itrnTransSerial.set(TRANSFER_KEY, serialTemp.get(TRANSFER_KEY));
		itrnTransSerial.set(TRANSFER_LINE_NUMBER, serialTemp.get(TRANSFER_LINE_NUMBER));
		itrnTransSerial.set(STORERKEY, serialTemp.get(STORERKEY));
		itrnTransSerial.set(ITEM, serialTemp.get(ITEM));
		String temp = serialTemp.get(LOT).toString().trim();
		itrnTransSerial.set(LOT, temp);
		itrnTransSerial.set(LOC, serialTemp.get(LOC));
		itrnTransSerial.set(ID, serialTemp.get(ID));
		itrnTransSerial.set(SERIALNUMBER, serialTemp.get(SERIALNUMBER));
		itrnTransSerial.set(QTY, serialTemp.get(QTY));
		itrnTransSerial.set(DATA2, serialTemp.get(DATA2));
		itrnTransSerial.set(DATA3, serialTemp.get(DATA3));
		itrnTransSerial.set(DATA4, serialTemp.get(DATA4));
		itrnTransSerial.set(DATA5, serialTemp.get(DATA5));
		Double netweight = (Double) serialTemp.get(NETWEIGHT);
		if (netweight == null)
			itrnTransSerial.set(NETWEIGHT, new Double(0.0D));
		else
			itrnTransSerial.set(NETWEIGHT, serialTemp.get(NETWEIGHT));
		return itrnTransSerial;
	}

	private void clearTemp(String transKey, String transLineNo) throws DPException {
		String delStr = "DELETE FROM TDSTEMP WHERE TRANSFERKEY='" + transKey + "' AND TRANSFERLINENUMBER='" + transLineNo + "'";
		WmsWebuiValidationDeleteImpl.delete(delStr);
	}

	private void changeSkuxlocType(StateInterface state, String transKey) throws UserException {
		System.out.println(" in  here :" + transKey);
		Connection qqConnection = null;
		PreparedStatement preStatement = null;
		try {
			DataSource dataSource = SimpleJDBCUtil.getDataSource(state.getRequest().getSession().getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString());
			qqConnection = dataSource.getConnection();
			String sql = "update skuxloc  set skuxloc.LOCATIONTYPE = (select LOCATIONTYPE from loc b where b.loc = skuxloc.loc) where  skuxloc.loc IN ( " + " select  DISTINCT TOLOC from TRANSFERDETAIL where TRANSFERKEY=" + transKey + " )";
			System.out.println("sql:" + sql);
			preStatement = qqConnection.prepareStatement(sql);
			preStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DBResourceException(e);
		} finally {
			SimpleJDBCUtil.release(qqConnection, preStatement, null);
		}

	}

}