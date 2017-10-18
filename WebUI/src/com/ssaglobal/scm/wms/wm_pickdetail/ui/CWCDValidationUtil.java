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
package com.ssaglobal.scm.wms.wm_pickdetail.ui;

import java.util.ArrayList;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.service.dutilitymanagement.SerialNumberObj;
import com.ssaglobal.scm.wms.service.dutilitymanagement.dto.SerialNoDTO;
import com.ssaglobal.scm.wms.service.dutilitymanagement.dto.SkuSNConfDTO;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.NSQLConfigUtil;
import com.ssaglobal.scm.wms.util.StringUtils;
import com.ssaglobal.scm.wms.util.dao.SerialInventoryDAO;
import com.ssaglobal.scm.wms.util.dao.SkuSNConfDAO;
import com.ssaglobal.scm.wms.wm_pickdetail.util.CWCDState;
import com.ssaglobal.scm.wms.wm_pickdetail.util.PickDetailState;

public class CWCDValidationUtil {

	private static final String PICKDETAIL_DETAIL_VIEW = "pickdetail_detail_view";

	private static final String TAB_2 = "tab 2";

	private static final String WM_PICKDETAIL_CATCHWEIGHT_DATA_DETAIL_VIEW = "wm_pickdetail_catchweight_data_detail_view";

	private static final String WMS_LIST_SHELL = "wms_list_shell";

	private static final String DETAIL_TAB = "wm_shipmentorderdetail_detail_view";

	private static final String TAB_8 = "tab 8";

	protected static ILoggerCategory _log = LoggerFactory.getInstance(CWCDValidationUtil.class);

	StateInterface state;

	public enum Type {
		PICK, SO
	}

	Type type;

	public CWCDValidationUtil(StateInterface state, Type type) {
		super();
		this.state = state;
		this.type = type;

	}

	public void cwcdListValidation(BioBean pickDetailBean, int status) throws EpiDataException, UserException {
		// Validate New CWCD Record and Existing Records
		// make sure SNL is entered
		// make sure SNL is valid
		cwcdRecordValidation(pickDetailBean);

		// Validate Status Change
		if (status >= 5) {
			// Perform CWCD Validation
			int expectedCWCDCount = getExpectedCWCDCount(pickDetailBean);
			// getActualCWCDCount
			int actualCWCDCount = getActualCWCDCountFromList(pickDetailBean);

			_log.debug(	"LOG_DEBUG_EXTENSION_CWCDValidationUtil_execute",
						"CWCD Expected " + expectedCWCDCount,
						SuggestedCategory.NONE);
			_log.debug(	"LOG_DEBUG_EXTENSION_CWCDValidationUtil_execute",
						"CWCD Actual " + actualCWCDCount,
						SuggestedCategory.NONE);

			if (expectedCWCDCount > actualCWCDCount) {
				String totwgtFlag = getTotWgtFlag(	BioAttributeUtil.getString(pickDetailBean, "PICKDETAILKEY"),
													state.getTempUnitOfWork());
				_log.debug(	"LOG_DEBUG_EXTENSION_CWCDValidationUtil_cwcdListValidation",
							"TOTWGTFLAG " + totwgtFlag,
							SuggestedCategory.NONE);
				// check totwgtflag
				// totwgtflag may be Y, if that is the case, do not throw an exception
				// default is N
				if (!"Y".equalsIgnoreCase(totwgtFlag)) {
					// throw exception
					throw new UserException("WMEXP_SN_PICK_CWCD_REQ", new Object[] { pickDetailBean.getValue("SKU") });
				}
			}

		}

	}

	public void cwcdDetailValidation(StateInterface state, DataBean pickDetailFormFocus, int status) throws EpiDataException, UserException {
		// Validate New CWCD Record and Existing Records
		// make sure SNL is entered
		// make sure SNL is valid
		String sku = pickDetailFormFocus.getValue("SKU").toString();
		if(this.isEndToEndCheckedForSku(state, sku)){
			cwcdRecordValidation(pickDetailFormFocus);
		}

		// Validate Status Change
		if (status >= 5 && !Type.SO.equals(this.type) ) {
			// Perform CWCD Validation
			int expectedCWCDCount = getExpectedCWCDCount(pickDetailFormFocus);
			// getActualCWCDCount
			int actualCWCDCount = getActualCWCDCountFromDetail(pickDetailFormFocus);

			_log.debug(	"LOG_DEBUG_EXTENSION_PickDetailSaveValidation_execute",
						"Expected " + expectedCWCDCount,
						SuggestedCategory.NONE);
			_log.debug(	"LOG_DEBUG_EXTENSION_PickDetailSaveValidation_execute",
						"Actual " + actualCWCDCount,
						SuggestedCategory.NONE);
			if (expectedCWCDCount > actualCWCDCount) {
				String totwgtFlag = getTotWgtFlag(	BioAttributeUtil.getString(pickDetailFormFocus, "PICKDETAILKEY"),
													state.getTempUnitOfWork());
				// check totwgtflag
				// totwgtflag may be Y, if that is the case, do not throw an exception
				// default is N
				if (!"Y".equalsIgnoreCase(totwgtFlag)) {
					// throw exception
					throw new UserException("WMEXP_SN_PICK_CWCD_REQ", new Object[] { pickDetailFormFocus.getValue("SKU") });
				}
			}

		}

	}
	
	
	public boolean isEndToEndCheckedForSku(StateInterface state, String sku ) throws EpiDataException{
 		Query qry = new Query("sku", "sku.SKU='"+sku+"'", null);
 		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		BioCollectionBean resultCollection = uowb.getBioCollectionBean(qry);
		BioBean bioBean = resultCollection.get(""+0);
		if(!"1".equalsIgnoreCase(bioBean.getString("SNUM_ENDTOEND"))){
			return false;
		}		
		return true;
	}

	/**
	 * Returns the number of Expected CWCD Records If AllowCatchWeightData is off, it will return 0 If OCDCATCHWGEN
	 * doesn't equal PICK, it will return 0
	 * 
	 * @param pickDetailBean
	 *            DOCUMENT ME!
	 * @param pContext
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 * @throws EpiDataException
	 */
	public int getExpectedCWCDCount(DataBean pickDetailBean) throws EpiDataException {
		/***************************************************************************************************************
		 * Programmer: Kristine Chan Created : 19 Feb 2004 Purpose : Validate that CatchData/CatchWeight has been
		 * entered for all details before shipping PMT# RLIS-5RUNYD
		 * ****************************************************************** Modification History 19 Feb 2004 COP
		 * Original 08 Oct 2008 RVM Modified for OA
		 **************************************************************************************************************/
		String orderkey = null;

		String pickdetailkey = null;

		String sku = null;

		String storerkey = null;

		String ocdflag = null;

		String ocwflag = null;

		String ocwby = null;

		String packkey = null;

		String totwgtflag = null;

		String OCDCATCHWHEN = null;

		int li_dbcount = 0;

		int li_reqcount = 0;

		double ld_uomqty = 0;

		double ld_shipqty = 0;

		double ld_tempcount = 0;

		Double casecnt = null;

		Double pallet = null;

		// From PickDetail

		BioAttributeUtil pickDetail = new BioAttributeUtil(pickDetailBean);

		orderkey = pickDetail.getString("ORDERKEY");

		pickdetailkey = pickDetail.getString("PICKDETAILKEY");

		sku = pickDetail.getString("SKU");

		storerkey = pickDetail.getString("STORERKEY");

		// I think this is in Eaches
		ld_shipqty = pickDetail.getDouble("QTY");

		String configKey = "AllowCatchWeightData";
		NSQLConfigUtil allowCatchWeightData = new NSQLConfigUtil(state, configKey);

		if (allowCatchWeightData.isOff() == true) {
			// CatchWeightData is not being tracked
			return 0;
		} else {
			UnitOfWorkBean uow = state.getTempUnitOfWork();
			BioCollectionBean rs = uow.getBioCollectionBean(new Query("sku", "sku.STORERKEY = '" + storerkey + "' and sku.SKU = '" + sku + "'", null));
			BioBean skuBioBean = rs.get("" + 0);
			ocdflag = skuBioBean.getString("OCDFLAG");
			ocwflag = skuBioBean.getString("OCWFLAG");
			ocwby = skuBioBean.getString("OCWBY");
			packkey = skuBioBean.getString("PACKKEY");
			OCDCATCHWHEN = skuBioBean.getString("OCDCATCHWHEN");

			rs = uow.getBioCollectionBean(new Query("wm_pack", "wm_pack.PACKKEY = '" + packkey + "'", null));
			BioAttributeUtil packBioBean = new BioAttributeUtil(rs.get("" + 0));
			casecnt = packBioBean.getDouble("CASECNT", 1);
			pallet = packBioBean.getDouble("PALLET", 1);

			if ((!((ocdflag != null) && ocdflag.equals("1") && OCDCATCHWHEN.equals("PICK"))) && (!((ocwflag != null) && ocwflag.equals("1")))) {
				// catchdata/catchweight not needed to be captured
				return 0;
			} else {
				// rs = uow.getBioCollectionBean(new Query("lotxiddetail", "lotxiddetail.SOURCEKEY = '" + orderkey + "'
				// and lotxiddetail.SKU = '" + sku + "' and lotxiddetail.PICKDETAILKEY = '"
				// + pickdetailkey + "'", null));
				// li_dbcount = rs.size();

				// Subodh: SCM-00000-03462: Get totwgtflag from LotxIDHeader table.
				totwgtflag = getTotWgtFlag(pickdetailkey, uow);

				// if (li_dbcount <= 0)
				// {
				// // 0 catchdata/weight keyed - error
				// return -1;
				// }
				// else
				// {

				if (((ocwby != null) && ocwby.equals("1"))) {
					ld_uomqty = casecnt;
				} else
					if (((ocwby != null) && ocwby.equals("2"))) {
						ld_uomqty = pallet;
					} else {
						ld_uomqty = 1;
					}

				// compare total keyed in and total lines expected
				li_reqcount = ((int) (ld_shipqty / ld_uomqty));
				ld_tempcount = ld_shipqty / ld_uomqty;
				// Handle (decimal) values
				if (ld_tempcount > li_reqcount) {
					li_reqcount = li_reqcount + 1;
				}

				// reqcount is the expected cwcd count
				return li_reqcount;
				// if (li_reqcount > li_dbcount)
				// {
				// if (totwgtflag.equals("N"))
				// { //Subodh: SCM-00000-03462
				// return -1;
				// }
				// }

			}

		}

		// }

		// return 1;
	}

	private int getActualCWCDCountFromList(BioBean pickDetailBean) throws EpiDataException {
		int actualCWCDCount = 0;
		Object cwcdRecords = pickDetailBean.getValue("LOTXIDDETAIL");
		if (cwcdRecords != null && cwcdRecords instanceof BioCollectionBean) {
			actualCWCDCount += ((BioCollectionBean) cwcdRecords).size();
		}
		if (type == Type.PICK) {

			// Need to account for Possible QBEBioBean on Detail
			// this is because if you change the record in the detail, it will show up as changed from the list

			ArrayList<String> tabs = getPickCWCDFindFormTabs();
			RuntimeFormInterface cwcdDetailForm = FormUtil.findForm(state.getCurrentRuntimeForm(),
																	"wms_list_shell",
																	"wm_pickdetail_catchweight_data_detail_view",
																	tabs,
																	state);
			DataBean cwcdDetailFocus = cwcdDetailForm == null ? null : cwcdDetailForm.getFocus();
			String listPickDetailKey = pickDetailBean.getString("PICKDETAILKEY");
			if (cwcdDetailFocus != null && cwcdDetailFocus instanceof QBEBioBean) {
				String detailPickDetailKey = BioAttributeUtil.getString(cwcdDetailFocus, "PICKDETAILKEY");
				if (listPickDetailKey.equals(detailPickDetailKey)) {
					actualCWCDCount++;
				}
			}
		}
		return actualCWCDCount;
	}

	private int getActualCWCDCountFromDetail(DataBean pickDetailFormFocus) throws EpiDataException {
		PickDetailState pickDetailState = PickDetailState.NONE;
		CWCDState cwcdState = CWCDState.NONE;
		ArrayList<String> tabs = getPickCWCDFindFormTabs();
		RuntimeFormInterface cwcdDetailForm = FormUtil.findForm(state.getCurrentRuntimeForm(),
																"wms_list_shell",
																"wm_pickdetail_catchweight_data_detail_view",
																tabs,
																state);
		if (pickDetailFormFocus.isTempBio()) {
			pickDetailState = PickDetailState.NEW;
		} else
			if (pickDetailFormFocus.isBio()) {
				pickDetailState = PickDetailState.UPDATE;
			} else {
				pickDetailState = PickDetailState.NONE;
			}

		DataBean cwcdDetailFocus = cwcdDetailForm == null ? null : cwcdDetailForm.getFocus();
		if (cwcdDetailFocus != null && cwcdDetailFocus.isTempBio()) {
			cwcdState = CWCDState.NEW;
		} else
			if (cwcdDetailFocus != null && cwcdDetailFocus.isBio()) {
				cwcdState = CWCDState.UPDATE;
			} else {
				cwcdState = CWCDState.NONE;
			}

		int actualCWCDCount = 0;
		if (pickDetailState == PickDetailState.NEW) {
			if (cwcdState == CWCDState.NEW) {
				actualCWCDCount = 1;
			}
		} else
			if (pickDetailState == PickDetailState.UPDATE) {
				if (cwcdState == CWCDState.NEW) {
					actualCWCDCount += 1;
				}
			}
		Object cwcdRecords = pickDetailFormFocus.getValue("LOTXIDDETAIL");
		if (cwcdRecords != null && cwcdRecords instanceof BioCollectionBean) {
			actualCWCDCount += ((BioCollectionBean) cwcdRecords).size();
		}
		return actualCWCDCount;
	}

	private void cwcdRecordValidation(DataBean pickDetailFormFocus) throws UserException, EpiDataException {
		{
			ArrayList<String> tabs = getPickCWCDFindFormTabs();
			RuntimeFormInterface cwcdDetailForm = FormUtil.findForm(state.getCurrentRuntimeForm(),
																	"wms_list_shell",
																	"wm_pickdetail_catchweight_data_detail_view",
																	tabs,
																	state);
			DataBean cwcdDetailFocus = cwcdDetailForm == null ? null : cwcdDetailForm.getFocus();
			//jp.answerlink.281034.begin
			//if (cwcdDetailFocus != null && cwcdDetailFocus instanceof QBEBioBean) {
			if (cwcdDetailFocus != null ) {
			//jp.answerlink.281034.end

				ArrayList<?> serials = null;
				String serialNumberLong = BioAttributeUtil.getString(cwcdDetailFocus, "SERIALNUMBERLONG");
/*				if (StringUtils.isEmpty(serialNumberLong)) {
					throw new UserException("WMEXP_SN_REQ", new Object[] { BioAttributeUtil.getString(	cwcdDetailFocus,
																										"SOURCEKEY"), BioAttributeUtil.getString(	cwcdDetailFocus,
																																					"SOURCELINENUMBER") });
				}
*/
				String owner = BioAttributeUtil.getString(pickDetailFormFocus, "STORERKEY");
				String sku = BioAttributeUtil.getString(pickDetailFormFocus, "SKU");
				try {
					serials = getSerials(owner, sku, serialNumberLong);
				} catch (RuntimeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					_log.error(	"LOG_ERROR_EXTENSION_CWCDValidationUtil_execute",
								StringUtils.getStackTraceAsString(e),
								SuggestedCategory.NONE);
					throw new UserException("WMEXP_SN_FORMAT", new String[] { serialNumberLong });
				}

				if (serials == null || serials.size() == 0) {
					throw new UserException("WMEXP_SN_FORMAT", new String[] { serialNumberLong });
				}

				//jp.answerlink.281034.begin
				if(Type.SO.equals(this.type)){
					//Validate that SerialNumber exists in SerialInventory for the given ID
					String lot = BioAttributeUtil.getString(pickDetailFormFocus, "LOT");
					String loc = BioAttributeUtil.getString(pickDetailFormFocus, "LOC");
					String id = BioAttributeUtil.getString(pickDetailFormFocus, "ID");
					
					SerialInventoryDAO siDAO = new SerialInventoryDAO();
					if(!siDAO.isAvailable(state, owner, sku, lot, loc, id, ((SerialNoDTO) serials.get(0)).getSerialnumber())){
						throw new UserException("WMEXP_SN_NOT_EXISTS", new String[] { serialNumberLong });
					}
				}else{
					// Set OOTHER1 with parsed Serial
					cwcdDetailFocus.setValue("OOTHER1", ((SerialNoDTO) serials.get(0)).getSerialnumber());
				}
				//jp.answerlink.281034.end

				_log.debug(	"LOG_DEBUG_EXTENSION_CWCDValidationUtil_execute",
							"Serials " + serials,
							SuggestedCategory.NONE);

			}
		}
/*		{
			Object cwcdRecords = pickDetailFormFocus.getValue("LOTXIDDETAIL");
			if (cwcdRecords != null && cwcdRecords instanceof BioCollectionBean) {
				for (int i = 0; i < ((BioCollectionBean) cwcdRecords).size(); i++) {
					BioBean cwcdRecord = ((BioCollectionBean) cwcdRecords).get("" + i);
					String snl = cwcdRecord.getString("SERIALNUMBERLONG");
					if (StringUtils.isEmpty(snl)) {
						throw new UserException("WMEXP_SN_REQ", new Object[] { BioAttributeUtil.getString(	cwcdRecord,
																											"SOURCEKEY"), BioAttributeUtil.getString(	cwcdRecord,
																																						"SOURCELINENUMBER") });
					}
				}
			}
		}*/

	}

	private String getTotWgtFlag(String pickdetailkey, UnitOfWorkBean uow) throws EpiDataException {
		String totwgtflag = null;
		BioCollectionBean rs;
		rs = uow.getBioCollectionBean(new Query("lotxidheader", "lotxidheader.PICKDETAILKEY = '" + pickdetailkey + "'", null));
		if (rs.size() > 0) {
			totwgtflag = rs.get("" + 0).getString("TOTWGTFLAG");
		}
		return totwgtflag;
	}

	public ArrayList<?> getSerials(String storerkey, String sku, String serial) {

		SkuSNConfDTO skuConf = SkuSNConfDAO.getSkuSNConf(storerkey, sku);

		SerialNumberObj serialNumber = new SerialNumberObj(skuConf);

		serialNumber.setStorerkey(storerkey);

		serialNumber.setSku(sku);

		ArrayList<?> list = serialNumber.getValidSerialNos(serial);

		return list;

	}

	private ArrayList<String> getPickCWCDFindFormTabs() {
		if (type == Type.SO) {
			ArrayList<String> tabs = new ArrayList<String>();
			tabs.add(TAB_2);
			tabs.add(TAB_8);
			tabs.add(DETAIL_TAB);
			tabs.add(PICKDETAIL_DETAIL_VIEW);
			return tabs;
		} else {
			ArrayList<String> tabs = new ArrayList<String>();
			tabs.add(TAB_2);
			tabs.add(PICKDETAIL_DETAIL_VIEW);
			return tabs;
		}

	}

}
