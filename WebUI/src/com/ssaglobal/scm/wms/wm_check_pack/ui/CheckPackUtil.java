/*******************************************************************************
 *                         NOTICE                            
 *                                                                                
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS             
 * CONFIDENTIAL INFORMATION OF INFOR AND/OR ITS AFFILIATES   
 * OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED WITHOUT PRIOR  
 * WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND       
 * ADAPT THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH  
 * THE TERMS OF THEIR SOFTWARE LICENSE AGREEMENT.            
 * ALL OTHER RIGHTS RESERVED.                                                     
 *                                                           
 * (c) COPYRIGHT 2009 INFOR.  ALL RIGHTS RESERVED.           
 * THE WORD AND DESIGN MARKS SET FORTH HEREIN ARE            
 * TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR          
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS        
 * RESERVED.  ALL OTHER TRADEMARKS LISTED HEREIN ARE         
 * THE PROPERTY OF THEIR RESPECTIVE OWNERS.                  
 *******************************************************************************/
package com.ssaglobal.scm.wms.wm_check_pack.ui;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.wm_check_pack.ui.CheckPackResults.ContainerType;

public class CheckPackUtil {

	public static final String CHECK_PACK_MULTIPLE = "MULTIPLE";

	class Dimension {
		private Double height;
		private Double width;
		private Double length;
		private Double weight;

		public Dimension() {
			height = 0.0;
			width = 0.0;
			length = 0.0;
			weight = 0.0;
		}

		public Double getHeight() {
			return height;
		}

		public Double getLength() {
			return length;
		}

		public Double getWeight() {
			return weight;
		}

		public Double getWidth() {
			return width;
		}

		public void setHeight(double height) {
			this.height = height;
		}

		public void setLength(double length) {
			this.length = length;
		}

		public void setWeight(double weight) {
			this.weight = weight;
		}

		public void setWidth(double width) {
			this.width = width;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "Height " + height + "\n Width " + width + "\nLength " + length + "\nWeight " + weight;
		}

	}

	protected static ILoggerCategory _log = LoggerFactory.getInstance(CheckPackUtil.class);

	public static Dimension getCartonDimensions(UIRenderContext context, CheckPackResults results) {
		Dimension dimension = new CheckPackUtil().new Dimension();

		try {
			StateInterface state = context.getState();
			UnitOfWorkBean tuow = state.getTempUnitOfWork();
			Object cartonType = null;
			if (results.getType() == ContainerType.DROP) {
				BioCollectionBean rs = tuow.getBioCollectionBean(new Query("wm_dropid", "wm_dropid.DROPID = '" + results.getContainer() + "'", null));
				for (int i = 0; i < rs.size(); i++) {
					cartonType = BioAttributeUtil.getString(rs.get("" + i), "CARTONTYPE");
				}
				_log.debug("LOG_DEBUG_EXTENSION_CheckPackUtil_getCartonDimensions", "CartonType from DROP " + cartonType, SuggestedCategory.NONE);

			}
			if (cartonType == null || results.getType() == ContainerType.CASE) {
				Object[] cartonTypes = results.getPickDetails().getAttributes(new String[] { "CARTONTYPE" });
				if (cartonTypes.length > 0) {
					Object[] tmp = (Object[]) cartonTypes[0];
					cartonType = tmp[0];
				}
				_log.debug("LOG_DEBUG_EXTENSION_CheckPackUtil_getCartonDimensions", "CartonType from PICKDETAIL " + cartonType, SuggestedCategory.NONE);
			}

			BioCollectionBean rs = tuow.getBioCollectionBean(new Query("wm_cartonization", "wm_cartonization.CARTONTYPE = '" + cartonType + "'", null));
			for (int i = 0; i < rs.size(); i++) {
				BioBean carton = rs.get("" + i);
				dimension.setHeight(BioAttributeUtil.getDouble(carton, "HEIGHT"));
				dimension.setWidth(BioAttributeUtil.getDouble(carton, "WIDTH"));
				dimension.setLength(BioAttributeUtil.getDouble(carton, "LENGTH"));
				dimension.setWeight(BioAttributeUtil.getDouble(carton, "TAREWEIGHT"));

			}

		} catch (EpiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dimension;
	}

	public static Dimension getContentsDimensions(UIRenderContext context, CheckPackResults results) {
		Dimension dimension = new CheckPackUtil().new Dimension();

		try {
			StateInterface state = context.getState();
			UnitOfWorkBean tuow = state.getTempUnitOfWork();
			Object[] skuInformation = results.getPickDetails().getAttributes(new String[] { "STORERKEY", "SKU", "QTY" });
			for (int i = 0; i < skuInformation.length; i++) {
				Object[] tmp = (Object[]) skuInformation[i];
				String storer = (String) tmp[0];
				String sku = (String) tmp[1];
				BigDecimal qty = (BigDecimal) tmp[2];
				String itemQuery = "sku.STORERKEY = '" + storer + "' and sku.SKU = '" + sku + "'";
				_log.debug("LOG_DEBUG_EXTENSION_CheckPackUtil_getContentsDimensions", itemQuery, SuggestedCategory.NONE);
				BioCollectionBean rs = tuow.getBioCollectionBean(new Query("sku", itemQuery, null));
				for (int j = 0; j < rs.size(); j++) {
					BioBean itemBio = rs.get("" + j);
					double grossWgt = BioAttributeUtil.getDouble(itemBio, "STDGROSSWGT");
					_log.debug("LOG_DEBUG_EXTENSION_CheckPackUtil_getContentsDimensions", "Adding " + qty + " * " + grossWgt, SuggestedCategory.NONE);
					dimension.setWeight(dimension.getWeight() + (qty.doubleValue() * grossWgt));
				}
			}

		} catch (EpiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dimension;
	}

	public static CheckPackResults search(UIRenderContext context, String container) throws EpiDataException {

		CheckPackResults results = new CheckPackResults();
		results.setContainer(container);
		results.setFoundResults(false);

		StateInterface state = context.getState();
		UnitOfWorkBean tuow = state.getTempUnitOfWork();

		//determine if container is caseid or dropid
		//search dropid
		_log.debug("LOG_DEBUG_EXTENSION_CheckPackUtil_search", "Searching for " + container, SuggestedCategory.NONE);

		final String dropidDetailQuery = "wm_dropiddetail.DROPID = '" + container + "'";
		_log.debug("LOG_DEBUG_EXTENSION_CheckPackUtil_search", dropidDetailQuery, SuggestedCategory.NONE);
		BioCollectionBean dropIDDetailRS = tuow.getBioCollectionBean(new Query("wm_dropiddetail", dropidDetailQuery, null));
		if (dropIDDetailRS.size() == 0) {
			//Check to see if CASE
			{
				final String caseidQuery = "wm_pickdetail.CASEID = '" + container + "'";
				_log.debug("LOG_DEBUG_EXTENSION_CheckPackUtil_search", caseidQuery, SuggestedCategory.NONE);
				BioCollectionBean caseRS = tuow.getBioCollectionBean(new Query("wm_pickdetail", caseidQuery, null));
				if (caseRS.size() == 0) {
					_log.error("LOG_ERROR_EXTENSION_CheckPackUtil_search", "Container is not a CASE or DROP, invalid Container", SuggestedCategory.NONE);
					return results;
				}
				for(int i = 0; i < caseRS.size(); i++) {
					BioBean pickBean = caseRS.get("" + i);
					String dropId = BioAttributeUtil.getString(pickBean, "DROPID");
					if(!StringUtils.isBlank(dropId)){
						results.setInnerPackage(true);
						return results;
					}
				}
			}
			_log.debug("LOG_DEBUG_EXTENSION_CheckPackUtil_search", "Container is a CASEID", SuggestedCategory.NONE);
			results.setType(CheckPackResults.ContainerType.CASE);

			final String pickDetailQuery = "wm_pickdetail.CASEID = '" + container + "'";
			_log.debug("LOG_DEBUG_EXTENSION_CheckPackUtil_search", pickDetailQuery, SuggestedCategory.NONE);
			BioCollectionBean pickRS = tuow.getBioCollectionBean(new Query("wm_pickdetail", pickDetailQuery, null));
			_log.debug("LOG_DEBUG_EXTENSION_CheckPackUtil_search", "Before Filter " + pickRS.size(), SuggestedCategory.NONE);
			pickRS.filterInPlace(new Query("wm_pickdetail", "wm_pickdetail.STATUS >= '5' and wm_pickdetail.QTY > '0'", null));
			_log.debug("LOG_DEBUG_EXTENSION_CheckPackUtil_search", "After Filter " + pickRS.size(), SuggestedCategory.NONE);
			results.setPickDetails(pickRS);
		} else {
			_log.debug("LOG_DEBUG_EXTENSION_CheckPackUtil_search", "Container is a DROPID", SuggestedCategory.NONE);
			results.setType(CheckPackResults.ContainerType.DROP);
			String caseIDs = " ";
			for (int i = 0; i < dropIDDetailRS.size(); i++) {
				if (i >= 1) {
					caseIDs += ", ";
				}
				caseIDs += " '" + BioAttributeUtil.getString(dropIDDetailRS.get("" + i), "CHILDID") + "' ";
			}
			_log.debug("LOG_DEBUG_EXTENSION_CheckPackUtil_search", "CaseIDs " + caseIDs, SuggestedCategory.NONE);

			final String pickDetailQuery = "wm_pickdetail.CASEID IN (" + caseIDs + ")";
			BioCollectionBean pickRS = tuow.getBioCollectionBean(new Query("wm_pickdetail", pickDetailQuery, null));
			_log.debug("LOG_DEBUG_EXTENSION_CheckPackUtil_search", "Before Filter " + pickRS.size(), SuggestedCategory.NONE);
			pickRS.filterInPlace(new Query("wm_pickdetail", "wm_pickdetail.STATUS >= '5' and wm_pickdetail.QTY > '0'", null));
			_log.debug("LOG_DEBUG_EXTENSION_CheckPackUtil_search", "After Filter " + pickRS.size(), SuggestedCategory.NONE);
			results.setPickDetails(pickRS);
		}

		return results;
	}

	public static String getCarrier(UIRenderContext context, String order) {
		StateInterface state = context.getState();
		UnitOfWorkBean tuow = state.getTempUnitOfWork();
		BioCollectionBean rs = tuow.getBioCollectionBean(new Query("wm_orders", "wm_orders.ORDERKEY = '" + order + "'", null));
		String carrier = null;
		try {
			for (int i = 0; i < rs.size(); i++) {
				BioBean orderBio = rs.get("" + i);
				carrier = BioAttributeUtil.getString(orderBio, "CarrierCode");
			}
		} catch (EpiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return carrier;
	}


	public static String getOrder(CheckPackResults results) {
		String orderkey = null;
		try {
			Object[] orders = results.getPickDetails().getAttributes(new String[] { "ORDERKEY" });
			Set<String> orderTrack = new HashSet<String>();
			for (int i = 0; i < orders.length; i++) {
				Object[] tmp = (Object[]) orders[i];
				orderkey = (String) tmp[0];
				orderTrack.add(orderkey);
			}
			//SPECIAL CASE
			if (orderTrack.size() > 1) {
				orderkey = CHECK_PACK_MULTIPLE;
			}

		} catch (EpiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return orderkey;
	}

	public static SPSInfo getSPSInfo(UIRenderContext context, String carrier) throws EpiDataException {
		SPSInfo spsInfo = new CheckPackUtil().new SPSInfo();
		spsInfo.setValid(false);
		StateInterface state = context.getState();
		UnitOfWorkBean tuow = state.getTempUnitOfWork();
		BioCollectionBean rs = tuow.getBioCollectionBean(new Query("wm_spscarrier", "wm_spscarrier.CARRIER = '" + carrier + "'", null));
		for (int i = 0; i < rs.size(); i++) {

			BioBean spsCarrier = rs.get("" + i);
			String scacCode = BioAttributeUtil.getString(spsCarrier, "SCAC_CODE");
			String spsType = BioAttributeUtil.getString(spsCarrier, "SPSTYPE");
			int smallOrLTL = BioAttributeUtil.getInt(spsCarrier, "SMALLORLTL");
			int actWgtOrSystemWgt = BioAttributeUtil.getInt(spsCarrier, "ACTWGTORSYSTEMWGT");
			//LTL Carriers have a hardcoded service of GEN
			if(smallOrLTL == 1 && StringUtils.isBlank(spsType)) {
				spsType = "GEN";
			}

			if (StringUtils.isEmpty(scacCode) || StringUtils.isEmpty(spsType)) {
				spsInfo.setValid(false);
			} else {
				spsInfo.setValid(true);
				spsInfo.setCarrier(carrier);
				spsInfo.setScacCode(scacCode);
				spsInfo.setSpsType(spsType);
				spsInfo.setSmallOrLTL(smallOrLTL);
				spsInfo.setActWgtOrSystemWgt(actWgtOrSystemWgt);
			}

		}
		return spsInfo;
	}

	class SPSInfo {
		private String carrier;
		private String scacCode;
		private String spsType;
		private int smallOrLTL;
		private int actWgtOrSystemWgt;
		private boolean valid;

		public SPSInfo() {
			carrier = null;
			scacCode = null;
			spsType = null;
			smallOrLTL = 0;
			actWgtOrSystemWgt = 0;
			valid = false;
		}

		public String getCarrier() {
			return carrier;
		}

		public void setCarrier(String carrier) {
			this.carrier = carrier;
		}

		public String getScacCode() {
			return scacCode;
		}

		public void setScacCode(String scacCode) {
			this.scacCode = scacCode;
		}

		public String getSpsType() {
			return spsType;
		}

		public void setSpsType(String spsType) {
			this.spsType = spsType;
		}

		public int getSmallOrLTL() {
			return smallOrLTL;
		}

		public void setSmallOrLTL(int smallOrLTL) {
			this.smallOrLTL = smallOrLTL;
		}

		public int getActWgtOrSystemWgt() {
			return actWgtOrSystemWgt;
		}

		public void setActWgtOrSystemWgt(int actWgtOrSystemWgt) {
			this.actWgtOrSystemWgt = actWgtOrSystemWgt;
		}

		public boolean isValid() {
			return valid;
		}

		public void setValid(boolean valid) {
			this.valid = valid;
		}

	}

	public static String getNotes(UIRenderContext context, String order) {
		StateInterface state = context.getState();
		UnitOfWorkBean tuow = state.getTempUnitOfWork();
		String notes = null;
		BioCollectionBean rs = tuow.getBioCollectionBean(new Query("wm_orders", "wm_orders.ORDERKEY = '" + order + "'", null));

		try {
			for (int i = 0; i < rs.size(); i++) {
				BioBean orderBio = rs.get("" + i);
				notes = BioAttributeUtil.getString(orderBio, "NOTES");
			}
		} catch (EpiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return notes;
	}

}
