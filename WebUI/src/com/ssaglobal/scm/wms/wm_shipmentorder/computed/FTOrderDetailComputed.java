package com.ssaglobal.scm.wms.wm_shipmentorder.computed;

import java.text.NumberFormat;

import com.epiphany.shr.data.beans.BioService;
import com.epiphany.shr.data.beans.ejb.BioServiceUtil;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.BioUtil;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.util.StringUtils;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;

public class FTOrderDetailComputed implements ComputedAttributeSupport {

	protected static ILoggerCategory log = LoggerFactory
			.getInstance(FTOrderDetailComputed.class);

	
	public Object get(Bio bio, String name, boolean flag)
			throws EpiDataException {
		BioService service = null;
		UnitOfWork uow = null;

		try {
			service = BioServiceUtil.getBioService("webui");
			uow = service.getUnitOfWork();

			NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY,
					0, 0);

			log.debug(
					"LOG_DEBUG_EXTENSION_ShipmentOrderUOMADJComputedAttribute",
					name, SuggestedCategory.NONE);
			double value = 0;
			if ("FTQTYPROCESSED".equals(name)) {
				value = BioUtil.getDouble(bio, "SHIPPEDQTY")
						+ BioUtil.getDouble(bio, "QTYALLOCATED")
						+ BioUtil.getDouble(bio, "QTYPICKED");
			} else {
				value = BioUtil.getDouble(bio, "OPENQTY")
						+ BioUtil.getDouble(bio, "QTYALLOCATED")
						+ BioUtil.getDouble(bio, "QTYPICKED")
						+ BioUtil.getDouble(bio, "QTYPREALLOCATED");
			}

			log.debug(
					"LOG_DEBUG_EXTENSION_ShipmentOrderUOMADJComputedAttribute",
					"" + value, SuggestedCategory.NONE);
			String uom = BioUtil.getString(bio, "UOM");
			log.debug(
					"LOG_DEBUG_EXTENSION_ShipmentOrderUOMADJComputedAttribute",
					uom, SuggestedCategory.NONE);
			String pack = BioUtil.getString(bio, "PACKKEY");
			String uom3 = UOMMappingUtil.getPACKUOM3Val(pack);

			if (!uom.equalsIgnoreCase(uom3)) {
				if (value != 0) {
					try {
						String test = UOMMappingUtil.numberFormaterConverter(
								LocaleUtil.getCurrencyLocale(),
								UOMMappingUtil.UOM_EA, uom,
								StringUtils.convertToString(value), pack,
								UOMMappingUtil.stateNull, uow, false);
						return LocaleUtil.formatValues(test,
								LocaleUtil.TYPE_QTY);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					return nf.format(0);

				}
			} else {
				try {
					log
							.debug(
									"LOG_DEBUG_EXTENSION_ShipmentOrderUOMADJComputedAttribute Formatted val:",
									LocaleUtil.formatValues(
											StringUtils.convertToString(value),
											LocaleUtil.TYPE_QTY),
									SuggestedCategory.NONE);
					return LocaleUtil.formatValues(StringUtils.convertToString(value),
							LocaleUtil.TYPE_QTY); // AW
					// Infor365:217417
				} catch (Exception e) {
				}
			}

		} catch (EpiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (uow != null) {
				uow.close();
			}
			if (service != null) {
				service.remove();
			}
		}
		return null;
	}

	
	public void set(Bio bio, String s, Object obj, boolean flag)
			throws EpiDataException {
		// TODO Auto-generated method stub

	}

	
	public boolean supportsSet(String s, String s1) {
		// TODO Auto-generated method stub
		return true;
	}

}
