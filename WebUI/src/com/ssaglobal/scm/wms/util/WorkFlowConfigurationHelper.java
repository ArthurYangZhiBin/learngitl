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
package com.ssaglobal.scm.wms.util;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

/**
 * The Class WorkFlowConfigurationHelper.
 */
public class WorkFlowConfigurationHelper {

	protected static ILoggerCategory log = LoggerFactory
			.getInstance(WorkFlowConfigurationHelper.class);

	public enum Type {
		PRTPARPT, PRNTPICK, PRNTPACK
	}

	/**
	 * Checks if is flag inactive.
	 * 
	 * @param prntpick
	 *            the prntpick
	 * @param outbound
	 *            the outbound
	 * @param orderKey
	 *            the order key
	 * @param tempUnitOfWork
	 *            the temp unit of work
	 * @return true, if is flag inactive
	 * @throws EpiDataException
	 */
	public static ActiveFlag getTypeStatus(Type type, String key,
			UnitOfWorkBean tempUnitOfWork) throws EpiDataException {
		log.debug("WorkFlowConfigurationHelper_getTypeStatus", "Type "
				+ type.toString(), SuggestedCategory.APP_EXTENSION);
		switch (type) {
		case PRTPARPT:
			String iActivityKey = getInboundActivityKey(key, tempUnitOfWork);
			log.debug("WorkFlowConfigurationHelper_getTypeStatus",
					"Activity Key " + iActivityKey,
					SuggestedCategory.APP_EXTENSION);
			if (iActivityKey == null) {
				log.debug("WorkFlowConfigurationHelper_getTypeStatus",
						"Flag is not set", SuggestedCategory.APP_EXTENSION);
				return ActiveFlag.NULL;
			}

			BioCollectionBean irs = tempUnitOfWork
					.getBioCollectionBean(new Query(
							"wm_ibactdetail",
							"wm_ibactdetail.IBACTKEY = '"
									+ iActivityKey
									+ "' and wm_ibactdetail.ACTIVE = '1' and wm_ibactdetail.TYPE = '"
									+ type.toString() + "'", null));
			if (irs.size() > 0) {
				log.debug("WorkFlowConfigurationHelper_getTypeStatus",
						"Flag is True", SuggestedCategory.APP_EXTENSION);
				return ActiveFlag.TRUE;
			} else {
				log.debug("WorkFlowConfigurationHelper_getTypeStatus",
						"Flag is False", SuggestedCategory.APP_EXTENSION);
				return ActiveFlag.FALSE;
			}

		case PRNTPACK:
		case PRNTPICK:
			String oActivityKey = getOutboundActivityKey(key, tempUnitOfWork);
			log.debug("WorkFlowConfigurationHelper_getTypeStatus",
					"Activity Key " + oActivityKey,
					SuggestedCategory.APP_EXTENSION);
			// OBACTDETAIL WHERE OBActKey = ? AND Active = '0' AND Type = ?

			if (oActivityKey == null) {
				log.debug("WorkFlowConfigurationHelper_getTypeStatus",
						"Flag is not set", SuggestedCategory.APP_EXTENSION);
				return ActiveFlag.NULL;
			}

			BioCollectionBean ors = tempUnitOfWork
					.getBioCollectionBean(new Query(
							"wm_obactdetail",
							"wm_obactdetail.OBACTKEY = '"
									+ oActivityKey
									+ "' and wm_obactdetail.ACTIVE = '1' and wm_obactdetail.TYPE = '"
									+ type.toString() + "'", null));

			if (ors.size() > 0) {
				log.debug("WorkFlowConfigurationHelper_getTypeStatus",
						"Flag is True", SuggestedCategory.APP_EXTENSION);
				return ActiveFlag.TRUE;
			} else {
				log.debug("WorkFlowConfigurationHelper_getTypeStatus",
						"Flag is False", SuggestedCategory.APP_EXTENSION);
				return ActiveFlag.FALSE;
			}

		default:
			// unsupported key
			throw new EnumConstantNotPresentException(
					WorkFlowConfigurationHelper.Type.class, type.toString());
		}
	}

	private static String getOutboundActivityKey(String key,
			UnitOfWorkBean tempUnitOfWork) throws EpiDataException {
		String owner = null;
		String customer = null;
		String facility = null;
		String activityKey = null;
		BioCollectionBean rs = tempUnitOfWork.getBioCollectionBean(new Query(
				"wm_orders", "wm_orders.ORDERKEY = '" + key + "'", null));
		for (int i = 0; i < rs.size(); i++) {
			BioBean order = rs.get("" + i);
			owner = BioAttributeUtil.getString(order, "STORERKEY");
			customer = BioAttributeUtil.getString(order, "CONSIGNEEKEY");
			facility = getFacility(tempUnitOfWork);
		}

		if (!StringUtils.isNull(facility)) {

			BioCollectionBean activities1 = tempUnitOfWork
					.getBioCollectionBean(new Query("wm_obactheader",
							"wm_obactheader.FACILITY = '" + facility
									+ "' and wm_obactheader.OWNER = '" + owner
									+ "' and wm_obactheader.CUSTOMER = '"
									+ customer + "'", null));

			BioCollectionBean activities2 = tempUnitOfWork
					.getBioCollectionBean(new Query(
							"wm_obactheader",
							"wm_obactheader.FACILITY = '"
									+ facility
									+ "' and wm_obactheader.OWNER = '"
									+ owner
									+ "' and (wm_obactheader.CUSTOMER = '"
									+ customer
									+ "' or wm_obactheader.CUSTOMER = '' or wm_obactheader.CUSTOMER = ' ')",
							null));

			BioCollectionBean activities3 = tempUnitOfWork
					.getBioCollectionBean(new Query(
							"wm_obactheader",
							"wm_obactheader.FACILITY = '"
									+ facility
									+ "' and (wm_obactheader.OWNER = '"
									+ owner
									+ "' or wm_obactheader.OWNER = '' or wm_obactheader.OWNER = ' ') and wm_obactheader.CUSTOMER = '"
									+ customer + "'", null));

			BioCollectionBean activities4 = tempUnitOfWork
					.getBioCollectionBean(new Query(
							"wm_obactheader",
							"wm_obactheader.FACILITY = '"
									+ facility
									+ "' and (wm_obactheader.OWNER = '"
									+ owner
									+ "' or wm_obactheader.OWNER = '' or wm_obactheader.OWNER = ' ') and (wm_obactheader.CUSTOMER = '"
									+ customer
									+ "' or wm_obactheader.CUSTOMER = '' or wm_obactheader.CUSTOMER = ' ')",
							null));

			if (activities1.size() > 0) {
				for (int i = 0; i < activities1.size(); i++) {
					activityKey = BioAttributeUtil.getString(activities1.get(""
							+ i), "OBACTKEY");
				}
			} else if (activities2.size() > 0) {
				for (int i = 0; i < activities2.size(); i++) {
					activityKey = BioAttributeUtil.getString(activities2.get(""
							+ i), "OBACTKEY");
				}
			} else if (activities3.size() > 0) {
				for (int i = 0; i < activities3.size(); i++) {
					activityKey = BioAttributeUtil.getString(activities3.get(""
							+ i), "OBACTKEY");
				}
			} else if (activities4.size() > 0) {
				for (int i = 0; i < activities4.size(); i++) {
					activityKey = BioAttributeUtil.getString(activities4.get(""
							+ i), "OBACTKEY");
				}
			}

		}

		return activityKey;
	}

	private static String getFacility(UnitOfWorkBean tempUnitOfWork)
			throws EpiDataException {
		BioCollectionBean rs = tempUnitOfWork.getBioCollectionBean(new Query(
				"wm_storer", "wm_storer.TYPE = '7'", null));
		String facility = null;
		for (int i = 0; i < rs.size(); i++) {
			facility = BioAttributeUtil.getString(rs.get("" + i), "STORERKEY");
		}
		return facility;
	}

	private static String getInboundActivityKey(String key,
			UnitOfWorkBean tempUnitOfWork) throws EpiDataException {
		String owner = null;
		String customer = null;
		String facility = null;
		String activityKey = null;
		BioCollectionBean rs = tempUnitOfWork.getBioCollectionBean(new Query(
				"receipt", "receipt.RECEIPTKEY = '" + key + "'", null));
		for (int i = 0; i < rs.size(); i++) {
			BioBean order = rs.get("" + i);
			owner = BioAttributeUtil.getString(order, "STORERKEY");
			customer = BioAttributeUtil.getString(order, "SUPPLIERCODE");
			facility = getFacility(tempUnitOfWork);
		}

		if (!StringUtils.isNull(facility)) {

			BioCollectionBean activities1 = tempUnitOfWork
					.getBioCollectionBean(new Query("wm_ibactheader",
							"wm_ibactheader.FACILITY = '" + facility
									+ "' and wm_ibactheader.OWNER = '" + owner
									+ "' and wm_ibactheader.SUPPLIER = '"
									+ customer + "'", null));

			BioCollectionBean activities2 = tempUnitOfWork
					.getBioCollectionBean(new Query(
							"wm_ibactheader",
							"wm_ibactheader.FACILITY = '"
									+ facility
									+ "' and wm_ibactheader.OWNER = '"
									+ owner
									+ "' and (wm_ibactheader.SUPPLIER = '"
									+ customer
									+ "' or wm_ibactheader.SUPPLIER = '' or wm_ibactheader.SUPPLIER = ' ')",
							null));

			BioCollectionBean activities3 = tempUnitOfWork
					.getBioCollectionBean(new Query(
							"wm_ibactheader",
							"wm_ibactheader.FACILITY = '"
									+ facility
									+ "' and (wm_ibactheader.OWNER = '"
									+ owner
									+ "' or wm_ibactheader.OWNER = '' or wm_ibactheader.OWNER = ' ') and wm_ibactheader.SUPPLIER = '"
									+ customer + "'", null));

			BioCollectionBean activities4 = tempUnitOfWork
					.getBioCollectionBean(new Query(
							"wm_ibactheader",
							"wm_ibactheader.FACILITY = '"
									+ facility
									+ "' and (wm_ibactheader.OWNER = '"
									+ owner
									+ "' or wm_ibactheader.OWNER = '' or wm_ibactheader.OWNER = ' ') and (wm_ibactheader.SUPPLIER = '"
									+ customer
									+ "' or wm_ibactheader.SUPPLIER = '' or wm_ibactheader.SUPPLIER = ' ')",
							null));

			if (activities1.size() > 0) {
				for (int i = 0; i < activities1.size(); i++) {
					activityKey = BioAttributeUtil.getString(activities1.get(""
							+ i), "IBACTKEY");
				}
			} else if (activities2.size() > 0) {
				for (int i = 0; i < activities2.size(); i++) {
					activityKey = BioAttributeUtil.getString(activities2.get(""
							+ i), "IBACTKEY");
				}
			} else if (activities3.size() > 0) {
				for (int i = 0; i < activities3.size(); i++) {
					activityKey = BioAttributeUtil.getString(activities3.get(""
							+ i), "IBACTKEY");
				}
			} else if (activities4.size() > 0) {
				for (int i = 0; i < activities4.size(); i++) {
					activityKey = BioAttributeUtil.getString(activities4.get(""
							+ i), "IBACTKEY");
				}
			}

		}

		return activityKey;
	}
}
