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
package com.ssaglobal.scm.wms.wm_ws_defaults.favorites;

//Import 3rd party packages and classes
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.login.SSOManager;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeMenuFormWidgetInterface;
import com.epiphany.shr.util.GUIDFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.SecurityUtil;
import com.ssaglobal.scm.wms.util.UserUtil;
import com.ssaglobal.scm.wms.util.upload.ImportFileNew;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsQueryAction;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultsUtil;

public class FavoritesGoAction extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(FavoritesGoAction.class);
	@Override
	protected int execute(ActionContext context, ActionResult result) throws UserException{

		_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Executing FavoritesGoAction",100L);
		StateInterface state = context.getState();
		//SM 07-12-07 Changed findForm parameter "Service Top" to "WM Service Top" to match UI metadata changes for Solution Console fix
		RuntimeFormInterface form = FormUtil.findForm(state.getCurrentRuntimeForm(),"","WM Service Top",state);
		//SM End update
		RuntimeMenuFormWidgetInterface widgetFavorites = (RuntimeMenuFormWidgetInterface)form.getFormWidgetByName("FAVORITES");
		List profiles = context.getServiceManager().getUserContext().getUserProfileIDs();
		BioRef selectedFavoriteRef = widgetFavorites.getSelectedItem();
		String favoriteValue = "";
		UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();
		try {
			String uid = UserUtil.getUserId(state);
			Query qry = new Query("userfavorites","userfavorites.USERID = '"+uid+"'","");
			BioCollection bc = uowb.getBioCollectionBean(qry);
			for(int i = 0; i < bc.size(); i++){
				if(bc.elementAt(i).getBioRef().equals(selectedFavoriteRef)){
					favoriteValue = bc.elementAt(i).getString("SCREEN");
				}
			}
		} catch (BioNotFoundException e1) {
			e1.printStackTrace();
			return RET_CANCEL;
		} catch (EpiDataException e1) {
			e1.printStackTrace();
			return RET_CANCEL;
		}
		_log.debug("LOG_DEBUG_EXTENSION_FAVLISTPREREN","favoriteValue:"+favoriteValue,100L);
		String fav = favoriteValue == null?"":favoriteValue.toString();
		String dbName = (String)state.getRequest().getSession().getAttribute(SetIntoHttpSessionAction.DB_CONNECTION);
		//Check Permissions To Make Sure User Can View This Screen
//		Build FAVORITESPROFILEMAPPING table query and get records for this user's profiles
		if(!SecurityUtil.isAdmin(state)){
			String bioQryStr = "";
			Iterator profileItr = profiles.iterator();
			while(profileItr.hasNext()){
				if(bioQryStr.length() == 0)
					bioQryStr += "(wm_favoritesprofilemapping.PROFILEID = '"+profileItr.next().toString().toUpperCase()+"' ";
				else
					bioQryStr += " OR wm_favoritesprofilemapping.PROFILEID = '"+profileItr.next().toString().toUpperCase()+"' ";
			}
			bioQryStr += ") AND wm_favoritesprofilemapping.SCREENID = '"+fav+"'";

			if(dbName.equalsIgnoreCase("enterprise"))
				bioQryStr += " AND wm_favoritesprofilemapping.ISENTERPRISE = '1'";
			else
				bioQryStr += " AND wm_favoritesprofilemapping.ISENTERPRISE = '0'";

			_log.debug("LOG_DEBUG_EXTENSION_FAVLISTPREREN","FAVORITESPROFILEMAPPING QRY:"+bioQryStr,100L);
			BioCollectionBean bioCollectionTemp = uowb.getBioCollectionBean(new Query("wm_favoritesprofilemapping",bioQryStr,""));
			try {
				if(bioCollectionTemp == null || bioCollectionTemp.size() == 0){
					String args[] = new String[0];
					String errorMsg = getTextMessage("WMEXP_FAV_INVALID_PERMISSION",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			} catch (EpiDataException e) {
				e.printStackTrace();
				return RET_CANCEL;
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","fav:"+fav,100L);
		if(dbName.equalsIgnoreCase("enterprise")){
			if(fav.length() > 0){
				//Navigate to Accessorial
				if(fav.equals("ACCES")){
					Query loadBiosQry = new Query("wm_accessorial","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav clickEvent873",100L);
					context.setNavigation("clickEvent873");
				}
				//Navigate to GL Distribution
				else if(fav.equals("GLDIST")){
					Query loadBiosQry = new Query("wm_gldistribution","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav clickEvent879",100L);
					context.setNavigation("clickEvent879");
				}
				//				Navigate to Tariff
				else if(fav.equals("TARIFF")){
					Query loadBiosQry = new Query("wm_tariff","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav clickEvent874",100L);
					context.setNavigation("clickEvent874");
				}
				//				Navigate to Billing Date
				else if(fav.equals("BILLINGDATE")){
					Query loadBiosQry = new Query("wm_lotxbilldate","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav changeEvent115",100L);
					context.setNavigation("changeEvent115");
				}
				//				Navigate to ITEM
//defect1076				else if(fav.equals("ITEM")){
				else if(fav.equals("ITEMENT")){					//defect1076
					Query loadBiosQry = new Query("sku","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav changeEvent100",100L);
					context.setNavigation("changeEvent100");
				}
				//				Navigate to OWNER
				else if(fav.equals("OWNER")){
					Query loadBiosQry = new Query("wm_storer","wm_storer.TYPE='1'","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav changeEvent103",100L);
					context.setNavigation("changeEvent103");
				}
				//				Navigate to Pack Charge Code
				else if(fav.equals("PACKCHRGCODE")){
					Query loadBiosQry = new Query("wm_packcharge","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav changeEvent159",100L);
					context.setNavigation("changeEvent159");
				}
				//				Navigate to Cycle Class
				else if(fav.equals("CYCLECLASS")){
					Query loadBiosQry = new Query("wm_ccsetup","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent435",100L);
					context.setNavigation("menuClickEvent435");
				}
				//				Navigate to Cartonization
				else if(fav.equals("CARTONIZATION")){
					Query loadBiosQry = new Query("wm_cartonization","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent440",100L);
					context.setNavigation("menuClickEvent440");
				}
				//				Navigate to Pack
				else if(fav.equals("PACK")){
					Query loadBiosQry = new Query("wm_pack","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent441",100L);
					context.setNavigation("menuClickEvent441");
				}
				//				Navigate to Receipt Validation
				else if(fav.equals("RECEIPTVAL")){
					Query loadBiosQry = new Query("wm_receiptvalidation","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent442",100L);
					context.setNavigation("menuClickEvent442");
				}
				//				Navigate to Packing
				else if(fav.equals("PACKING")){
					Query loadBiosQry = new Query("wm_packingvalidationtemplate","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent443",100L);
					context.setNavigation("menuClickEvent443");
				}
				//				Navigate to HazMat Codes
				else if(fav.equals("HAZMAT")){
					Query loadBiosQry = new Query("wm_hazmatcodes","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav configEvent445",100L);
					context.setNavigation("configEvent445");
				}
				//				Navigate to Chart Of Accts
				else if(fav.equals("CHTOFACCTS")){
					Query loadBiosQry = new Query("wm_chartofaccounts","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav billingEvent448",100L);
					context.setNavigation("billingEvent448");
				}
				//				Navigate to Tax Rate
				else if(fav.equals("TAXRATE")){
					Query loadBiosQry = new Query("wm_taxrate","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav billingEvent449",100L);
					context.setNavigation("billingEvent449");
				}
				//				Navigate to Tax Group
				else if(fav.equals("TAXGRP")){
					Query loadBiosQry = new Query("wm_taxgroup","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav billingEvent450",100L);
					context.setNavigation("billingEvent450");
				}
				//				Navigate to Billing Services
				else if(fav.equals("BILLINGSERV")){
					Query loadBiosQry = new Query("wm_services","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav billingEvent451",100L);
					context.setNavigation("billingEvent451");
				}
				//				Navigate to Calandar Group
				else if(fav.equals("CALGRP")){
					Query loadBiosQry = new Query("wm_calendar","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav billingEvent452",100L);
					context.setNavigation("billingEvent452");
				}
				//				Navigate to Chart Of Accts
				else if(fav.equals("CONTAINBILL")){
					Query loadBiosQry = new Query("wm_containerbilling","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav billingEvent453",100L);
					context.setNavigation("billingEvent453");
				}
				//				Navigate to Multi Facility Balances
				else if(fav.equals("MULTIFACBAL")){
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav executionEvent454",100L);
					context.setNavigation("executionEvent454");
				}
				//				Navigate to Multi Facility Setup
				else if(fav.equals("MULTIFACSET")){
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav configEvent455",100L);
					context.setNavigation("configEvent455");
				}
				//				Navigate to Personalization
				else if(fav.equals("PERSONALIZE")){
					WSDefaultsQueryAction.executeExtension(context,result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav configEvent457",100L);
					context.setNavigation("configEvent457");
				}
				//				Navigate to Permissions
				else if(fav.equals("PERMISSION")){
					Query loadBiosQry = new Query("wm_meta_user_role","wm_meta_user_role.user_role_name != 'administrator'","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav adminEvent458",100L);
					context.setNavigation("adminEvent458");
				}
				//				Navigate to Conditional Validation
				else if(fav.equals("CONDVALID")){
					Query loadBiosQry = new Query("wm_conditionalvalidation", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent492",100L);
					context.setNavigation("menuClickEvent492");
				}
				//				Navigate to Setup Bill Of Material
				else if (fav.equals("SETUPBOM")) {
					Query loadBiosQry = new Query("wm_setup_billofmaterial", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent491", 100L);
					context.setNavigation("menuClickEvent491");
				}
				//				Navigate to Lottable Validation
				else if(fav.equals("LOTTVALID")){
					Query loadBiosQry = new Query("wm_lottablevalidation", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav lottableClickEvent503",100L);
					context.setNavigation("lottableClickEvent503");
				}
				//				Navigate to Pack Charge Code
				else if(fav.equals("PACKCC")){
					Query loadBiosQry = new Query("wm_packcharge", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent492",100L);
					context.setNavigation("billClickEvent501");
				}
				//				Navigate to Code
				else if(fav.equals("CODE")){
					Query loadBiosQry = new Query("wm_codes", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav adminClickEvent519",100L);
					context.setNavigation("adminClickEvent519");
				}
				//				Navigate to CC Discrepancy Handling
				else if(fav.equals("CCDISCHAND")){
					Query loadBiosQry = new Query("wm_cc_discrepancy_handling","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent530",100L);
					context.setNavigation("menuClickEvent530");
				}
				//				Navigate to Facility Transfer Inquiry
				else if(fav.equals("FACILITYTRANSFERINQ")){
					Query loadBiosQry = new Query("wm_intransit","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav facilitytransferInqClickEvent546",100L);
					context.setNavigation("facilitytransferInqClickEvent546");
				}
				//				Navigate to Customer
				else if(fav.equals("CUSTOMER")){
					Query loadBiosQry = new Query("wm_storer","wm_storer.TYPE='2'","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav customerClickEvent540",100L);
					context.setNavigation("customerClickEvent540");
				}
				//				Navigate to Vendor
				else if(fav.equals("VENDOR")){
					Query loadBiosQry = new Query("wm_storer","wm_storer.TYPE='5'","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav vendorClickEvent541",100L);
					context.setNavigation("vendorClickEvent541");
				}
				//				Navigate to Carrier
				else if(fav.equals("CARRIER")){
					Query loadBiosQry = new Query("wm_storer","wm_storer.TYPE='3'","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav carrierClickEvent542",100L);
					context.setNavigation("carrierClickEvent542");
				}
				//				Navigate to Bill To
				else if(fav.equals("BILLTO")){
					Query loadBiosQry = new Query("wm_storer","wm_storer.TYPE='4'","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav billtoClickEvent543",100L);
					context.setNavigation("billtoClickEvent543");
				}
				//Navigate to Date Format
				else if (fav.equals("DATEFORMAT")) {
					Query loadBiosQry = new Query("wm_dateformat", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent718", 100L);
					context.setNavigation("menuClickEvent718");
				}
				//Navigate to CC Discrepancy
				else if (fav.equals("CCDISC")) {
					Query loadBiosQry = new Query("wm_cc_discrepancy_handling", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent719", 100L);
					context.setNavigation("menuClickEvent719");
				}
				//Navigate to LOGS
				else if (fav.equals("IMPLOGS")) {
					Query loadBiosQry = new Query("wm_importfile", "wm_importfile.STATUS IN ('2','3','4','5')", "wm_importfile.SUBMITDATE DESC");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent726", 100L);
					context.setNavigation("menuClickEvent726");
				}
				//Navigate to SUBMIT JOBS
				else if (fav.equals("SUBMITJOBS")) {
					try {
						ImportFileNew.setupImportFileNew(context, result);
					} catch (EpiDataException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (DataBeanException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (EpiException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent727", 100L);
					context.setNavigation("menuClickEvent727");
				}
				//Navigate to FACILITY ACTIVATION
				else if (fav.equals("FACACTIV")) {
					Query loadBiosQry = new Query("wm_pl_db", "wm_pl_db.db_enterprise !=1", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent728", 100L);
					context.setNavigation("menuClickEvent728");
				}
				//Navigate to CREATE PROCESS GROUP
				else if (fav.equals("SCHEDCREATE")) {
					Query loadBiosQry = new Query("wp_es_processgroups", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent729", 100L);
					context.setNavigation("menuClickEvent729");
				}
				//Navigate to MAINTAIN PROCESS GROUP
				else if (fav.equals("SCHEDMAINT")) {
					Query loadBiosQry = new Query("wp_es_spg", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent730", 100L);
					context.setNavigation("menuClickEvent730");
				}
				//Navigate to SCHEDULED TASKS
				else if (fav.equals("SCHEDTASKS")) {
					Query loadBiosQry = new Query("wp_es_stg", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent731", 100L);
					context.setNavigation("menuClickEvent731");
				}
				//Navigate to START/STOP SCHEDULER
				else if (fav.equals("SCHEDSTRSTP")) {
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent732", 100L);
					context.setNavigation("menuClickEvent732");
				}
				
				
				//TM Screens
				//Navigate to TMS Carrier Route
				else if(fav.equals("TMSCARRIERROUTE")){
					Query loadBiosQry = new Query("lm_carrier_route","lm_carrier_route.C_IS_ACTIVE=True","lm_carrier_route.C_UPDATED_DATE_TIME DESC");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav TMSCARRIERROUTE",100L);
					context.setNavigation("TMSCARRIERROUTE");	
				}
				//Navigate to Transportation -> Carrier Configuration
				else if(fav.equals("TMSCARRIERCONFIG")){
					Query loadBiosQry = new Query("lm_wm_storer","lm_wm_storer.TYPE='3' AND lm_wm_storer.KSHIP_CARRIER != '1'","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav " + fav,100L);
					context.setNavigation("TMSCARRIERCONFIG");
				}                                               
				//Navigate to Transportation -> Carrier Exceptions Profile
				else if(fav.equals("TMSEXCEPTIONHOURS")){
					Query loadBiosQry = new Query("lm_exception_profile","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav " + fav,100L);
					context.setNavigation("TMSEXCEPTIONHOURS");
				}                                               
				//Navigate to Transportation -> Carrier Hours Profile
				else if(fav.equals("TMSHOURS")){
					Query loadBiosQry = new Query("lm_profile","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav " + fav,100L);
					context.setNavigation("TMSHOURS");
				}                                               
				//Navigate to Transportation -> Rating and Routing
				else if(fav.equals("TMSRATEROUTE")){
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav " + fav,100L);
					context.setNavigation("TMSRATEROUTE");
				}                                               
				//Navigate to Transportation -> Shipment
				else if(fav.equals("TMSSHIPMENT")){
					Query loadBiosQry = new Query("lm_shipment","","lm_shipment.C_UPDATED_DATE_TIME DESC");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav " + fav,100L);
					context.setNavigation("TMSSHIPMENT");
				}
				//Navigate to Transportation -> Configuration
				else if(fav.equals("TMSCONFIGURATION")){
		            Query loadBiosQry = new Query("lm_config_rule_set","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
		            _log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav " + fav,100L);
		            context.setNavigation("TMSCONFIGURATION");
				}
			}else{
				return RET_CANCEL;
			}
		}else{
			if(fav.length() > 0){
//				Navigate to Accessorial
				if(fav.equals("ACCES")){
					Query loadBiosQry = new Query("wm_accessorial","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav clickEvent873",100L);
					context.setNavigation("clickEvent873");
				}
//				Navigate to GL Distribution
				else if(fav.equals("GLDIST")){
					Query loadBiosQry = new Query("wm_gldistribution","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav clickEvent879",100L);
					context.setNavigation("clickEvent879");
				}
//				Navigate to Tariff
				else if(fav.equals("TARIFF")){
					Query loadBiosQry = new Query("wm_tariff","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav clickEvent874",100L);
					context.setNavigation("clickEvent874");
				}
//				Navigate to ITEM
				else if(fav.equals("ITEM")){
					Query loadBiosQry = new Query("sku","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav changeEvent100",100L);
					context.setNavigation("changeEvent100");
				}
//				Navigate to LOCATION
				else if(fav.equals("LOCATION")){
					Query loadBiosQry = new Query("wm_location","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav changeEvent102",100L);
					context.setNavigation("changeEvent102");
				}
//				Navigate to OWNER
				else if(fav.equals("OWNER")){
					Query loadBiosQry = new Query("wm_storer","wm_storer.TYPE='1'","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav changeEvent103",100L);
					context.setNavigation("changeEvent103");
				}
//				Navigate to PO
				else if(fav.equals("PO")){
					Query loadBiosQry = new Query("wm_po","","wm_po.POKEY DESC");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav changeEvent105",100L);
					context.setNavigation("changeEvent105");
				}
//				Navigate to ASNRECEIPT
				else if(fav.equals("ASNRECEIPT")){
					Query loadBiosQry = new Query("receipt","","receipt.RECEIPTKEY DESC");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav changeEvent106",100L);
					context.setNavigation("changeEvent106");
				}
//				Navigate to Catch Weight/Data
				else if(fav.equals("CATCHWD")){
					Query loadBiosQry = new Query("wm_lotxidheader","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav changeEvent107",100L);
					context.setNavigation("changeEvent107");
				}
//				Navigate to Shipment Order
				else if(fav.equals("SHIPMENTORDER")){
					Query loadBiosQry = new Query("wm_orders","","wm_orders.ORDERKEY DESC");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav changeEvent108",100L);
					context.setNavigation("changeEvent108");
				}
//				Navigate to Case Manifest
				else if(fav.equals("CASEMAN")){
					Query loadBiosQry = new Query("wm_case_manifest","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav changeEvent109",100L);
					context.setNavigation("changeEvent109");
				}
//				Navigate to Unallocate Order Detail Lines
				else if(fav.equals("UNALLORDERDET")){
					Query loadBiosQry = new Query("wm_orderdetail","wm_orderdetail.STATUS < '9'","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav changeEvent110",100L);
					context.setNavigation("changeEvent110");
				}
//				Navigate to Unallocate Shipment Orders
				else if(fav.equals("UNALLSHIPORD")){
					Query loadBiosQry = new Query("wm_orders","wm_orders.STATUS < '9'","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav changeEvent111",100L);
					context.setNavigation("changeEvent111");
				}
//				Navigate to Unallocate Pick Details
				else if(fav.equals("UNALLPICKDET")){
					Query loadBiosQry = new Query("wm_pickdetail","wm_pickdetail.ORDERSTATUS < '9'","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav changeEvent112",100L);
					context.setNavigation("changeEvent112");
				}
//				Navigate to Mass Ship Lines
				else if(fav.equals("MASSSHIPLINES")){
					Query loadBiosQry = new Query("wm_pickdetail","wm_pickdetail.ORDERSTATUS < '9'","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav changeEvent113",100L);
					context.setNavigation("changeEvent113");
				}
//				Navigate to Mass Ship Orders
				else if(fav.equals("MASSSHIPORD")){
					Query loadBiosQry = new Query("wm_orders","wm_orders.STATUS < '9'","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav changeEvent114",100L);
					context.setNavigation("changeEvent114");
				}
//				Navigate to Billing Date
				else if(fav.equals("BILLINGDATE")){
					Query loadBiosQry = new Query("wm_lotxbilldate","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav changeEvent115",100L);
					context.setNavigation("changeEvent115");
				}
//				Navigate to Trannship
				else if(fav.equals("TRANNSHIP")){
					Query loadBiosQry = new Query("wm_transship_t","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav changeEvent117",100L);
					context.setNavigation("changeEvent117");
				}
//				Navigate to Flow Thru Order
				else if(fav.equals("FLOWTHRUORDER")){
					Query loadBiosQry = new Query("wm_xorders_to","","wm_xorders_to.ORDERKEY DESC");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav changeEvent118",100L);
					context.setNavigation("changeEvent118");
				}
//				Navigate to Lane
				else if(fav.equals("LANE")){
					Query loadBiosQry = new Query("wm_lane_ftl","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav changeEvent119",100L);
					context.setNavigation("changeEvent119");
				}
//				Navigate to Opportunistic Allocation
				else if(fav.equals("OPPORALL")){
					Query loadBiosQry = new Query("wm_sku_oa","wm_sku_oa.FLOWTHRUITEM = 'Y'","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav changeEvent120",100L);
					context.setNavigation("changeEvent120");
				}
//				Navigate to Flow Thru Apportionment
				else if(fav.equals("FLOWTHRUAPP")){
					Query loadBiosQry = new Query("wm_storer","wm_storer.TYPE='1'","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav changeEvent121",100L);
					context.setNavigation("changeEvent121");
				}
//				Navigate to Invoice Processing
				else if(fav.equals("INVPROC")){
					Query loadBiosQry = new Query("wm_invoice_processing","","");										
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav changeEvent158",100L);
					context.setNavigation("changeEvent158");
				}
//				Navigate to Pack Charge Code
				else if(fav.equals("PACKCHRGCODE")){
					Query loadBiosQry = new Query("wm_packcharge","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav changeEvent159",100L);
					context.setNavigation("changeEvent159");
				}
//				Navigate to Cycle Class
				else if(fav.equals("CYCLECLASS")){
					Query loadBiosQry = new Query("wm_ccsetup","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent435",100L);
					context.setNavigation("menuClickEvent435");
				}
//				Navigate to LOADPLANNING
				else if(fav.equals("LOADPLAN")){
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav clickEvent880",100L);
					context.setNavigation("clickEvent880");
				}
//				Navigate to QC Verify
				else if(fav.equals("QCVERIFY")){
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent437",100L);
					context.setNavigation("menuClickEvent437");
				}
//				Navigate to ORDER STATUS
				else if(fav.equals("ORDERSTATUS")){
					Query loadBiosQry = new Query("wm_orderstatussetup","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent438",100L);
					context.setNavigation("menuClickEvent438");
				}
//				Navigate to Class Restrictions
				else if(fav.equals("CLASSREST")){
					Query loadBiosQry = new Query("wm_classrestriction","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent439",100L);
					context.setNavigation("menuClickEvent439");
				}
//				Navigate to Cartonization
				else if(fav.equals("CARTONIZATION")){
					Query loadBiosQry = new Query("wm_cartonization","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent440",100L);
					context.setNavigation("menuClickEvent440");
				}
//				Navigate to Pack
				else if(fav.equals("PACK")){
					Query loadBiosQry = new Query("wm_pack","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent441",100L);
					context.setNavigation("menuClickEvent441");
				}
//				Navigate to Receipt Validation
				else if(fav.equals("RECEIPTVAL")){
					Query loadBiosQry = new Query("wm_receiptvalidation","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent442",100L);
					context.setNavigation("menuClickEvent442");
				}
//				Navigate to Packing
				else if(fav.equals("PACKING")){
					Query loadBiosQry = new Query("wm_packingvalidationtemplate","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent443",100L);
					context.setNavigation("menuClickEvent443");
				}
//				Navigate to USER
				else if(fav.equals("USER")){
					Query loadBiosQry = new Query("wm_taskmanageruser","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent444",100L);
					context.setNavigation("menuClickEvent444");
				}
//				Navigate to SECTION
				else if(fav.equals("SECTION")){
					Query loadBiosQry = new Query("wm_section","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent445",100L);
					context.setNavigation("menuClickEvent445");
				}
//				Navigate to EQUIPMENT
				else if(fav.equals("EQUIPMENT")){
					Query loadBiosQry = new Query("wm_equipmentprofile","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav rmenuClickEvent446",100L);
					context.setNavigation("rmenuClickEvent446");
				}
				else if(fav.equals("INBOUNDCHT")){
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent442",100L);
					context.setNavigation("chartClickEvent442");
				}
				else if(fav.equals("OUTBOUNDCHT")){
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent443",100L);
					context.setNavigation("chartClickEvent443");
				}
				else if(fav.equals("EXECUTIONCHT")){
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent444",100L);
					context.setNavigation("chartClickEvent444");
				}
//				Navigate to Allocate Strategy
				else if(fav.equals("ALLOCSTRAT")){
					Query loadBiosQry = new Query("wm_allocatestrategy","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav configEvent442",100L);
					context.setNavigation("configEvent442");
				}
//				Navigate to Preallocate Strategy
				else if(fav.equals("PREALLOCSTRAT")){
					Query loadBiosQry = new Query("wm_preallocatestrategy","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav configEvent443",100L);
					context.setNavigation("configEvent443");
				}
//				Navigate to Putaway Strategy
				else if(fav.equals("PUTAWAYSTRAT")){
					Query loadBiosQry = new Query("wm_putaway_strategy","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav configEvent444",100L);
					context.setNavigation("configEvent444");
				}
//				Navigate to HazMat Codes
				else if(fav.equals("HAZMAT")){
					Query loadBiosQry = new Query("wm_hazmatcodes","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav configEvent445",100L);
					context.setNavigation("configEvent445");
				}
//				Navigate to Demand Allocation
				else if(fav.equals("DEMANDALLOC")){
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav outboundEvent446",100L);
					context.setNavigation("outboundEvent446");
				}
//				Navigate to Container Details
				else if(fav.equals("CONTDET")){
					Query loadBiosQry = new Query("wm_labelcontainerdetail","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav outboundEvent447",100L);
					context.setNavigation("outboundEvent447");
				}
//				Navigate to Chart Of Accts
				else if(fav.equals("CHTOFACCTS")){
					Query loadBiosQry = new Query("wm_chartofaccounts","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav billingEvent448",100L);
					context.setNavigation("billingEvent448");
				}
//				Navigate to Tax Rate
				else if(fav.equals("TAXRATE")){
					Query loadBiosQry = new Query("wm_taxrate","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav billingEvent449",100L);
					context.setNavigation("billingEvent449");
				}
//				Navigate to Tax Group
				else if(fav.equals("TAXGRP")){
					Query loadBiosQry = new Query("wm_taxgroup","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav billingEvent450",100L);
					context.setNavigation("billingEvent450");
				}
//				Navigate to Billing Services
				else if(fav.equals("BILLINGSERV")){
					Query loadBiosQry = new Query("wm_services","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav billingEvent451",100L);
					context.setNavigation("billingEvent451");
				}
//				Navigate to Calandar Group
				else if(fav.equals("CALGRP")){
					Query loadBiosQry = new Query("wm_calendar","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav billingEvent452",100L);
					context.setNavigation("billingEvent452");
				}
//				Navigate to Chart Of Accts
				else if(fav.equals("CONTAINBILL")){
					Query loadBiosQry = new Query("wm_containerbilling","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav billingEvent453",100L);
					context.setNavigation("billingEvent453");
				}
//				Navigate to Inventory Balances
				else if(fav.equals("IVBLA")){
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav executionEvent456",100L);
					context.setNavigation("executionEvent456");
				}
//				Navigate to Personalization
				else if(fav.equals("PERSONALIZE")){
					WSDefaultsQueryAction.executeExtension(context,result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav configEvent457",100L);
					context.setNavigation("configEvent457");
				}
//				Navigate to REASON
				else if(fav.equals("REASON")){
					Query loadBiosQry = new Query("wm_taskmanagerreason","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav prodClickEvent447",100L);
					context.setNavigation("prodClickEvent447");
				}
//				Navigate to LOADSTATUS
				else if(fav.equals("LOADSTATUS")){
					Query loadBiosQry = new Query("wm_loadhdr","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav loadClickEvent462",100L);
					context.setNavigation("loadClickEvent462");
				}
//				Navigate to PALLETMANIFEST
				else if(fav.equals("PALLETMAN")){
					Query loadBiosQry = new Query("wm_palletmanifest","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav conClickEvent463",100L);
					context.setNavigation("conClickEvent463");
				}
//				Navigate to CONTAINERMANIFEST
				else if(fav.equals("CONTAINERMAN")){
					Query loadBiosQry = new Query("wm_containermanifest","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav conClickEvent464",100L);
					context.setNavigation("conClickEvent464");
				}
//				Navigate to Master Bill of Lading
				else if(fav.equals("MBOL")){
					Query loadBiosQry = new Query("wm_mbol","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav conClickEvent465",100L);
					context.setNavigation("conClickEvent465");
				}
//				Navigate to INVENTORY MOVE
				else if (fav.equals("MOVE")) {
					Query loadBiosQry = new Query("wm_lotxlocxid", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav invClickEvent478", 100L);
					context.setNavigation("invClickEvent478");
				}
//				Navigate to INVENTORY Holds
				else if (fav.equals("HOLDS")) {
					Query loadBiosQry = new Query("wm_inventoryhold_bio", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav invClickEvent479", 100L);
					context.setNavigation("invClickEvent479");
				}
//				Navigate to Cycle Count
				else if (fav.equals("CYCLECOUNT")) {
					Query loadBiosQry = new Query("wm_cc", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav invClickEvent480", 100L);
					context.setNavigation("invClickEvent480");
				}
//				Navigate to Generate
				else if (fav.equals("GENERATE")) {
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav repClickEvent481", 100L);
					context.setNavigation("repClickEvent481");
				}
//				Navigate to Maintain
				else if (fav.equals("MAINTAIN")) {
					Query loadBiosQry = new Query("wm_maintain_replenishment", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav repClickEvent482", 100L);
					context.setNavigation("repClickEvent482");
				}
//				Navigate to Task
				else if (fav.equals("TASKDETAIL")) {
					Query loadBiosQry = new Query("wm_taskdetail", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav prodClickEvent483", 100L);
					context.setNavigation("prodClickEvent483");
				}
//				Navigate to DROP ID
				else if (fav.equals("DROPID")) {
					Query loadBiosQry = new Query("wm_dropid", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav prodClickEvent484", 100L);
					context.setNavigation("prodClickEvent484");
				}
//				Navigate to Unassigned Work
				else if (fav.equals("UNWORK")) {
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav supClickEvent485", 100L);
					context.setNavigation("supClickEvent485");
				}
//				Navigate to Assigned Work
				else if (fav.equals("ASSNWORK")) {
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav supClickEvent486", 100L);
					context.setNavigation("supClickEvent486");
				}
//				Navigate to Indirect Activity
				else if (fav.equals("INACTIVITY")) {
					Query loadBiosQry = new Query("wm_userattendance", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav supClickEvent487", 100L);
					context.setNavigation("supClickEvent487");
				}
//				Navigate to User Activity
				else if (fav.equals("USERACT")) {
					//needs to be new bio action
					QBEBioBean tempRequestBio;
					try {
						tempRequestBio = uowb.getQBEBioWithDefaults("wm_userattendance");
					} catch (DataBeanException e) {
						e.printStackTrace();
						throw new UserException("ERROR_CREATE_TMP_BIO", new String[] { "wm_userattendance" });
					}
					result.setFocus(tempRequestBio);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav userClickEvent488", 100L);
					context.setNavigation("userClickEvent488");
				}
//				Navigate to Activity History
				else if (fav.equals("ACTHIST")) {
					Query loadBiosQry = new Query("wm_useractivity", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav userClickEvent489", 100L);
					context.setNavigation("userClickEvent489");
				}
//				Navigate to Pick Detail
				else if (fav.equals("PICKDETAIL")) {
					Query loadBiosQry = new Query("wm_pickdetail", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav outClickEvent490", 100L);
					context.setNavigation("outClickEvent490");
				}
//				Navigate to Setup Bill Of Material
				else if (fav.equals("SETUPBOM")) {
					Query loadBiosQry = new Query("wm_setup_billofmaterial", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent491", 100L);
					context.setNavigation("menuClickEvent491");
				}
//				Navigate to Conditional Validation
				else if(fav.equals("CONDVALID")){					
					Query loadBiosQry = new Query("wm_conditionalvalidation", "", "");			
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent492",100L);
					context.setNavigation("menuClickEvent492");
				}
//				Navigate to Sortation Station
				else if(fav.equals("SORTSTATION")){					
					Query loadBiosQry = new Query("wm_sortationstation", "", "");			
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent493",100L);
					context.setNavigation("menuClickEvent493");
				}
//				Navigate to Release CC 
				else if(fav.equals("RELEASECC")){
						//needs to be new bio action
						QBEBioBean tempBio;	
						try {
							tempBio = uowb.getQBEBioWithDefaults("wm_ccgen");
						} catch (DataBeanException e) {
							e.printStackTrace();
							throw new UserException("ERROR_CREATE_TMP_BIO", new String[] { "wm_ccgen" });
						}
						result.setFocus(tempBio);
						_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent494", 100L);
						context.setNavigation("menuClickEvent494");					
				}
//				Navigate to Barcode Configuration
				else if(fav.equals("BARCODECONFIG")){					
					Query loadBiosQry = new Query("wm_barcodeconfig", "", "");			
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent495",100L);
					context.setNavigation("menuClickEvent495");
				}
//				Navigate to Release CC History
				else if(fav.equals("RELEASECCHISTORY")){					
					Query loadBiosQry = new Query("wm_ccreleasehistory", "", "");			
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent496",100L);
					context.setNavigation("menuClickEvent496");
				}
//				Navigate to Operation
				else if(fav.equals("OPERATION")){					
					Query loadBiosQry = new Query("wm_operation", "", "");			
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent497",100L);
					context.setNavigation("menuClickEvent497");
				}
//				Navigate to Task Dispatch
				else if(fav.equals("TASKDISPATCH")){					
					Query loadBiosQry = new Query("wm_taskdispatch", "", "");			
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent498",100L);
					context.setNavigation("menuClickEvent498");
				}
//				Navigate to Lot Attribute
				else if(fav.equals("LOTATTRIBUTE")){					
					Query loadBiosQry = new Query("wm_lot_attribute", "", "");			
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent499",100L);
					context.setNavigation("menuClickEvent499");
				}
//				Navigate to Inventory Transaction
				else if(fav.equals("INVTRANS")){					
					Query loadBiosQry = new Query("wm_itrn", "", "");			
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent500",100L);
					context.setNavigation("menuClickEvent500");
				}
//				Navigate to Inventory Request To Count
				else if(fav.equals("REQTOCOUNT")){					
					Query loadBiosQry = new Query("wm_cclst", "", "");			
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent501",100L);
					context.setNavigation("menuClickEvent501");
				}
//				Navigate to Internal Transfer
				else if(fav.equals("INTERNALTRANSFER")){					
					Query loadBiosQry = new Query("wm_internal_transfer", "", "");			
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);;
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent502",100L);
					context.setNavigation("menuClickEvent502");
				}
//				Navigate to Lottable Validation
				else if(fav.equals("LOTTVALID")){					
					Query loadBiosQry = new Query("wm_lottablevalidation", "", "");					
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav lottableClickEvent503",100L);
					context.setNavigation("lottableClickEvent503");
				}
//				Navigate to Order Sequence Strategy
				else if(fav.equals("ORDSEQSTRATEGY")){					
					Query loadBiosQry = new Query("opporderstrategy", "", "");					
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent504",100L);
					context.setNavigation("menuClickEvent504");
				}
//				Navigate to BOM
				else if(fav.equals("BOM")){					
					Query loadBiosQry = new Query("wm_billofmaterial", "", "");					
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav bomClickEvent505",100L);
					context.setNavigation("bomClickEvent505");
				}
//				Navigate to Routing
				else if(fav.equals("ROUTING")){					
					Query loadBiosQry = new Query("wm_routing", "", "");				
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav routingClickEvent506",100L);
					context.setNavigation("routingClickEvent506");
				}
//				Navigate to Assign Accessorial Charges
				else if(fav.equals("AACHARGES")){					
					Query loadBiosQry = new Query("wm_aac_adjustment", "", "");				
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav aacClickEvent507",100L);
					context.setNavigation("aacClickEvent507");
				}
//				Navigate to Accumulated Charges
				else if(fav.equals("ACCCHARGES")){					
					Query loadBiosQry = new Query("wm_accumulated_charges", "", "");				
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav acccClickEvent508",100L);
					context.setNavigation("acccClickEvent508");
				}		
//				Navigate to Pack Charge Code
				else if(fav.equals("PACKCC")){					
					Query loadBiosQry = new Query("wm_packcharge", "", "");			
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent492",100L);
					context.setNavigation("billClickEvent501");
				}
//				Navigate to Load Schedule
				else if(fav.equals("LOADSCH")){					
					Query loadBiosQry = new Query("wm_loadschedule", "", "");	
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav clickEvent509",100L);
					context.setNavigation("clickEvent509");
				}
//				Navigate to Global Preferences
				else if(fav.equals("GLOBALPREF")){					
					Query loadBiosQry = new Query("wm_global_preferences", "", "");			
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav clickEvent510",100L);
					context.setNavigation("clickEvent510");
				}
//				Navigate to Preview Invoices
				else if(fav.equals("PREVIEWINVOICES")){					
					Query loadBiosQry = new Query("wm_preview_invoice", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav billingClickEvent511",100L);
					context.setNavigation("billingClickEvent511");
				}
//				Navigate to Flow Thru Apportionment
				else if(fav.equals("FLOWTHRUAPP")){
					Query loadBiosQry = new Query("wm_storer", "wm_storer.TYPE='1'", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav xdockClickEvent514",100L);
					context.setNavigation("xdockClickEvent514");
				}
//				Navigate to Receipt Reversal
				else if(fav.equals("RECEIPTREVERSAL")){
					String queryString = "DPE('SQL','@[wm_receiptreversal.ADJUSTMENTKEY] in (SELECT DISTINCT ADJUSTMENTKEY FROM ADJUSTMENTDETAIL WHERE REASONCODE = \\'UNRECEIVE\\')')";
					Query loadBiosQry = new Query("wm_receiptreversal", queryString, null);
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent515", 100L);
					context.setNavigation("menuClickEvent515");
				}
//				Navigate to Area
				else if(fav.equals("AREA")){
					Query loadBiosQry = new Query("wm_area", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav areaClickEvent516",100L);
					context.setNavigation("areaClickEvent516");
				}
//				Navigate to System Configuration
				else if(fav.equals("SYSCONF")){
					Query loadBiosQry = new Query("wm_server_configuration", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav adminClickEvent518",100L);
					context.setNavigation("adminClickEvent518");
				}
//				Navigate to System Settings
				else if(fav.equals("SYSSETTINGS")){
					Query loadBiosQry = new Query("wm_system_settings", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav adminClickEvent517",100L);
					context.setNavigation("adminClickEvent517");
				}
//				Navigate to Code
				else if(fav.equals("CODE")){
					Query loadBiosQry = new Query("wm_codes", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav adminClickEvent519",100L);
					context.setNavigation("adminClickEvent519");
				}
//				Navigate to Trannship ASN
				else if(fav.equals("TRANNSHIPASN")){
					Query loadBiosQry = new Query("wm_transasn","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav transshipASNEvent498",100L);
					context.setNavigation("transshipASNEvent498");
				}
//				Navigate to SSCC
				else if (fav.equals("SSCC")) {
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav rfidClickEvent520", 100L);
					context.setNavigation("rfidClickEvent520");
				}
//				Navigate to SGTIN
				else if (fav.equals("SGTIN")) {
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav rfidClickEvent521", 100L);
					context.setNavigation("rfidClickEvent521");
				}
//				Navigate to Indirect Activity Config
				else if(fav.equals("INDACTCONFIG")){
					Query loadBiosQry = new Query("wm_indirectactivity","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav execClickEvent522",100L);
					context.setNavigation("execClickEvent522");
				}
//				Navigate to Label Configuration
				else if(fav.equals("LABELCONFIG")){
					Query loadBiosQry = new Query("wm_label_configuration","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav adminClickEvent523",100L);
					context.setNavigation("adminClickEvent523");
				}
//				Navigate to Label Printers
				else if(fav.equals("LABELPRINT")){
					Query loadBiosQry = new Query("wm_label_printer","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav adminClickEvent524",100L);
					context.setNavigation("adminClickEvent524");
				}
//				Navigate to Work Center
				else if(fav.equals("WORKCENTER")){
					Query loadBiosQry = new Query("wm_work_center","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav workorderClickEvent525",100L);
					context.setNavigation("workorderClickEvent525");
				}
//				Navigate to Adjustment
				else if(fav.equals("ADJUSTMENT")){
					String queryString = "DPE('SQL','@[wm_adjustment.ADJUSTMENTKEY] in (SELECT DISTINCT ADJUSTMENTKEY FROM ADJUSTMENTDETAIL WHERE REASONCODE != \\'UNRECEIVE\\')')";
					Query loadBiosQry = new Query("wm_adjustment", queryString, null);
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent526", 100L);
					context.setNavigation("menuClickEvent526");
				}
//				Navigate to Physical Parameters
				else if(fav.equals("PHYSICALPARAMETERS")){
					Query loadBiosQry = new Query("wm_physical_parameters","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent527",100L);
					context.setNavigation("menuClickEvent527");
				}
//				Navigate to Batch Picking
				else if(fav.equals("BATCHPICKING")){
					Query loadBiosQry = new Query("wm_wave","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent528",100L);
					context.setNavigation("menuClickEvent528");
				}
//				Navigate to Strategy
				else if(fav.equals("STRATEGY")){
					Query loadBiosQry = new Query("wm_strategy","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent529",100L);
					context.setNavigation("menuClickEvent529");
				}
//				Navigate to CC Discrepancy Handling
				else if(fav.equals("CCDISCHAND")){
					Query loadBiosQry = new Query("wm_cc_discrepancy_handling","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent530",100L);
					context.setNavigation("menuClickEvent530");
				}
//				Navigate to Zone
				else if(fav.equals("ZONE")){
					Query loadBiosQry = new Query("wm_zone","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent531",100L);
					context.setNavigation("menuClickEvent531");
				}
				//Navigate to Wave Labels
				else if (fav.equals("WAVELABELS")) {
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent533", 100L);
					context.setNavigation("menuClickEvent533");
				}
//				Navigate to Customer
				else if(fav.equals("CUSTOMER")){
					Query loadBiosQry = new Query("wm_storer","wm_storer.TYPE='2'","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav customerClickEvent540",100L);
					context.setNavigation("customerClickEvent540");
				}
//				Navigate to Vendor
				else if(fav.equals("VENDOR")){
					Query loadBiosQry = new Query("wm_storer","wm_storer.TYPE='5'","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav vendorClickEvent541",100L);
					context.setNavigation("vendorClickEvent541");
				}
//				Navigate to Carrier
				else if(fav.equals("CARRIER")){
					Query loadBiosQry = new Query("wm_storer","wm_storer.TYPE='3'","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav carrierClickEvent542",100L);
					context.setNavigation("carrierClickEvent542");
				}
//				Navigate to bioll TO
				else if(fav.equals("BILLTO")){
					Query loadBiosQry = new Query("wm_storer","wm_storer.TYPE='4'","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav billtoClickEvent543",100L);
					context.setNavigation("billtoClickEvent543");
				}
//				Navigate to ASN Lookup
				else if(fav.equals("ASNLOOKUP")){
					Query loadBiosQry = new Query("receiptdetail","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav asnlookupClickEvent536",100L);
					context.setNavigation("asnlookupClickEvent536");
				}
//				Navigate to Verify Close ASN / PO
				else if(fav.equals("VERIFYCLOSEPOASN")){
					Query loadBiosQry = new Query("wm_vverifiedclosed","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav verifycloseClickEvent537",100L);
					context.setNavigation("verifycloseClickEvent537");
				}
//				Navigate to QC Packing
				else if(fav.equals("QCPACKING")){
					Query loadBiosQry = new Query("wm_packout","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav qcpackingClickEvent538",100L);
					context.setNavigation("qcpackingClickEvent538");
				}
//				Navigate to Facility Transfer
				else if(fav.equals("FACILITYTRANSFER")){
					Query loadBiosQry = new Query("wm_orders","wm_orders.TYPE = '11'","wm_orders.ORDERKEY DESC");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav facilitytransferClickEvent535",100L);
					context.setNavigation("facilitytransferClickEvent535");
				}
//				Navigate to Inv Hold Code
				else if(fav.equals("INVHOLDCODE")){
					Query loadBiosQry = new Query("wm_inventoryholdcode","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav holdcodeClickEvent539",100L);
					context.setNavigation("holdcodeClickEvent539");
				}
//				Navigate to Work Order
				else if(fav.equals("WORKORDER")){
					Query loadBiosQry = new Query("wm_workorder","","wm_workorder.WORKORDERKEY DESC");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav workorderClickEvent544",100L);
					context.setNavigation("workorderClickEvent544");
				}
//				Navigate to Job Management
				else if(fav.equals("JOB")){
					Query loadBiosQry = new Query("wm_jm_wogroup","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav jobClickEvent545",100L);
					context.setNavigation("jobClickEvent545");
				}
//				Navigate to Integration Logs
				else if(fav.equals("INTEGRATIONLOGS")){
					Query loadBiosQry = new Query("wm_transmitlog","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav integrationLogClickEvent547",100L);
					context.setNavigation("integrationLogClickEvent547");
				}
//				Navigate to Flow Thru Allocation
				else if(fav.equals("FLOWTHRUALLOC")){
					Query loadBiosQry = new Query("wm_vwmallocationmgt","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav integrationLogClickEvent547",100L);
					context.setNavigation("flowThruAllocEvent600");
				}
//				Navigate to Flow Thru Order Conversion
				else if(fav.equals("FLOWTHRUORDCON")){
					Query loadBiosQry = new Query("wm_xorders","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav integrationLogClickEvent547",100L);
					context.setNavigation("flowThruOrderConEvent601");
				}
//				Navigate to Load Maintenance
				else if(fav.equals("LOADMAIN")){
					Query loadBiosQry = new Query("wm_loadhdr_auto_pk","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav integrationLogClickEvent547",100L);
					context.setNavigation("loadMainEvent602");
				}
//				Navigate to Review Physical
				else if(fav.equals("REVIEWPHYSICAL")){
					Query loadBiosQry = new Query("wm_review_physical","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav reviewPhysicalEvent553",100L);
					context.setNavigation("reviewPhysicalEvent553");
				}
//				Navigate to Strategy Code
				else if(fav.equals("STRATEGYCODE")){
					Query loadBiosQry = new Query("wm_strategy_code","","");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent652", 100L);
					context.setNavigation("menuClickEvent652");
				}
//				Navigate to Order Suspension
				else if(fav.equals("SHIPORDERSUSPEND")){
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Setting Nav menuClickEvent668", 100L);
					context.setNavigation("menuClickEvent668");
				}
				//Navigate to Loading Reports
				else if (fav.equals("LOADREPORTS")) {
					try {
						result.setFocus(SecurityUtil
								.getUserAccessableReports(context.getState(), "", "wm_pbsrpt_reports.CATEGORY_ID = 10 AND wm_pbsrpt_reports.CANACCESS = 'WEBUI'", result.getFocus()));
					} catch (EpiDataException e) {
						e.printStackTrace();
						Query loadBiosQry = new Query("wm_pbsrpt_reports", "wm_pbsrpt_reports.CATEGORY_ID = 10 AND wm_pbsrpt_reports.CANACCESS = 'WEBUI'", "");
						result.setFocus(uowb.getBioCollectionBean(loadBiosQry));
					}
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent709", 100L);
					context.setNavigation("menuClickEvent709");
				}
				//Navigate to Serial Inventory
				else if (fav.equals("SERIALINV")) {
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent710", 100L);
					context.setNavigation("menuClickEvent710");
				}
				//Navigate to Held Inventory
				else if (fav.equals("HELDINV")) {
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent711", 100L);
					context.setNavigation("menuClickEvent711");
				}
				//Navigate to Serial Transactions
				else if (fav.equals("SERIALTRANS")) {
					Query loadBiosQry = new Query("wm_itrnserial", "", "wm_itrnserial.ADDDATE DESC");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent712", 100L);
					context.setNavigation("menuClickEvent712");
				}
				//Navigate to Remaining Work
				else if (fav.equals("REMWORK")) {
					Query loadBiosQry = new Query("wm_vRemainingWork", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent713", 100L);
					context.setNavigation("menuClickEvent713");
				}
				//Navigate to User Order Performance
				else if (fav.equals("USRORDERPERF")) {
					Query loadBiosQry = new Query("wm_vorderperformance", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent714", 100L);
					context.setNavigation("menuClickEvent714");
				}
				//Navigate to User & Group Daily Performance
				else if (fav.equals("USRGRPPERF")) {
					Query loadBiosQry = new Query("wm_user_group_dummy_bio", "wm_user_group_dummy_bio.CONFIGKEY = 'MONITORPRODUCTIVITY'", "");
					BioBean bio = null;
					try {
						bio = (BioBean) uowb.getBioCollectionBean(loadBiosQry).elementAt(0);
					} catch (EpiDataException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					result.setFocus(bio);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent715", 100L);
					context.setNavigation("menuClickEvent715");
				}
				//Navigate to Work Tracker
				else if (fav.equals("WORKTRACKER")) {
					Query loadBiosQry = new Query("wm_vassignmentlistview", "", "wm_vassignmentlistview.ASSGNNUMBER ASC");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent716", 100L);
					context.setNavigation("menuClickEvent716");
				}
				//Navigate to Hold Allocation
				else if (fav.equals("HOLDALLOC")) {
					Query loadBiosQry = new Query("wm_holdallocationmatrix", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent717", 100L);
					context.setNavigation("menuClickEvent717");
				}
				//Navigate to Date Format
				else if (fav.equals("DATEFORMAT")) {
					Query loadBiosQry = new Query("wm_dateformat", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent718", 100L);
					context.setNavigation("menuClickEvent718");
				}
				//Navigate to CC Discrepancy
				else if (fav.equals("CCDISC")) {
					Query loadBiosQry = new Query("wm_cc_discrepancy_handling", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent719", 100L);
					context.setNavigation("menuClickEvent719");
				}
				//Navigate to Task Dispatch
				else if (fav.equals("TASKDISP")) {
					Query loadBiosQry = new Query("wm_taskdispatch", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent720", 100L);
					context.setNavigation("menuClickEvent720");
				}
				//Navigate to Audit
				else if (fav.equals("AUDIT")) {
					Query loadBiosQry = new Query("wm_auditbios", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent721", 100L);
					context.setNavigation("menuClickEvent721");
				}
				//Navigate to REPORT CONFIGURATION
				else if (fav.equals("RPTCONF")) {
					Query loadBiosQry = new Query("wm_pbsrpt_reports", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent722", 100L);
					context.setNavigation("menuClickEvent722");
				}
				//Navigate to REPORT PRINTER CONFIGURATION
				else if (fav.equals("RPTPRNCONF")) {
					Query loadBiosQry = new Query("wm_reportprinter", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent723", 100L);
					context.setNavigation("menuClickEvent723");
				}
				//Navigate to FILE UPLOAD
				else if (fav.equals("FILEUPLOAD")) {
					Query loadBiosQry = new Query("wm_fileupload", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent724", 100L);
					context.setNavigation("menuClickEvent724");
				}
				//Navigate to FACILITY
				else if (fav.equals("FACILITY")) {
					Query loadBiosQry = new Query("wm_storer", "wm_storer.TYPE = '7'", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent725", 100L);
					context.setNavigation("menuClickEvent725");
				}
				//Navigate to LOGS
				else if (fav.equals("IMPLOGS")) {
					Query loadBiosQry = new Query("wm_importfile", "wm_importfile.STATUS IN ('2','3','4','5')", "wm_importfile.SUBMITDATE DESC");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent726", 100L);
					context.setNavigation("menuClickEvent726");
				}
				//Navigate to SUBMIT JOBS
				else if (fav.equals("SUBMITJOBS")) {
					try {
						ImportFileNew.setupImportFileNew(context, result);
					} catch (EpiDataException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (DataBeanException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (EpiException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent727", 100L);
					context.setNavigation("menuClickEvent727");
				}
				//Navigate to CROSSDOCK REPORTS
				else if (fav.equals("XDOCKREPORTS")) {

					try {
						result.setFocus(SecurityUtil
								.getUserAccessableReports(context.getState(), "", "wm_pbsrpt_reports.CATEGORY_ID = 5 AND wm_pbsrpt_reports.CANACCESS = 'WEBUI'", result.getFocus()));
					} catch (EpiDataException e) {
						e.printStackTrace();
						Query loadBiosQry = new Query("wm_pbsrpt_reports", "wm_pbsrpt_reports.CATEGORY_ID = 5 AND wm_pbsrpt_reports.CANACCESS = 'WEBUI'", "");
						result.setFocus(uowb.getBioCollectionBean(loadBiosQry));
					}
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent733", 100L);
					context.setNavigation("menuClickEvent733");
				}
				//Navigate to QUERY BUILDER
				else if (fav.equals("WPQUERY")) {

					wpNewTemp(result, state, uowb);

					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent734", 100L);
					context.setNavigation("menuClickEvent734");
				}
				//Navigate to GRAPHICAL BUILDER
				else if (fav.equals("WPGRAPHICAL")) {

					wpNewTemp(result, state, uowb);

					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent735", 100L);
					context.setNavigation("menuClickEvent735");
				}
				//Navigate to SAVED FILTERS
				else if (fav.equals("WPSAVED")) {
					Query loadBiosQry = new Query("wp_filter", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent736", 100L);
					context.setNavigation("menuClickEvent736");
				}
				//Navigate to MAINTAIN WAVES
				else if (fav.equals("WPMAINTAIN")) {
					wpAddCookie(state);
					Query loadBiosQry = new Query("wp_wave", "wp_wave.STATUS >= '3'", "wp_wave.WAVEKEY DESC");
					setBC(wpQuery(uowb.getBioCollectionBean(loadBiosQry), "WHSEID", true, false, null, state), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent737", 100L);
					context.setNavigation("menuClickEvent737");
				}
				//Navigate to SYNCHRONIZATION LOG
				else if (fav.equals("WPSYNCLOG")) {
					wpAddCookie(state);
					Query loadBiosQry = new Query("wp_generatedalerts", "", "wp_generatedalerts.EVENTTIME DESC");
					setBC(wpQuery(uowb.getBioCollectionBean(loadBiosQry), "FACILITYID", true, false, null, state), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent738", 100L);
					context.setNavigation("menuClickEvent738");
				}
				//Navigate to PRINT LABELS
				else if (fav.equals("WPPRINT")) {
					QBEBioBean caseLabels = null;
					try {
						caseLabels = uowb.getQBEBioWithDefaults("wp_case_labels");
					} catch (DataBeanException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					result.setFocus(caseLabels);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent739", 100L);
					context.setNavigation("menuClickEvent739");
				}
				//Navigate to CONFIRM WAVES
				else if (fav.equals("WPCONFIRM")) {
					wpAddCookie(state);
					Query loadBiosQry = new Query("wp_wave", "wp_wave.STATUS <= '2'", "wp_wave.WAVEKEY DESC");
					setBC(wpQuery(uowb.getBioCollectionBean(loadBiosQry), "WHSEID", true, false, null, state), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent740", 100L);
					context.setNavigation("menuClickEvent740");
				}
				//Navigate to APPLICATION
				else if (fav.equals("WPAPP")) {
					wpAddCookie(state);
					Query qry = new Query("wp_configuration", null, null);  
					try {
						if (uowb.getBioCollectionBean(qry).size() > 0) {
							BioBean newFocus = uowb.getBioCollectionBean(qry).get("0");
							BioBean newBioBean = uowb.getBioBean(newFocus.getBioRef());
							result.setFocus(newBioBean);
						} else {
							QBEBioBean tempQBE = uowb.getQBEBioWithDefaults("wp_configuration");
							result.setFocus(tempQBE);
						}
					} catch (BioNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (EpiDataException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (DataBeanException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent741", 100L);
					context.setNavigation("menuClickEvent741");
				}
				//Navigate to LABOR DATA
				else if (fav.equals("WPLABOR")) {
					Query loadBiosQry = new Query("wp_labordata", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent742", 100L);
					context.setNavigation("menuClickEvent742");
				}
				//Navigate to REPORTS
				else if (fav.equals("REPORTS")) {
					try {
						result.setFocus(SecurityUtil.getUserAccessableReports(context.getState(), "wm_pbsrpt_reports.RPT_TITLE",
								"(NOT(wm_pbsrpt_reports.RPT_URL IS NULL) OR (wm_pbsrpt_reports.RPT_URL != '')) AND wm_pbsrpt_reports.CANACCESS = 'WEBUI'", result.getFocus()));
					} catch (EpiDataException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Query loadBiosQry = new Query("wm_pbsrpt_reports", "(NOT(wm_pbsrpt_reports.RPT_URL IS NULL) OR (wm_pbsrpt_reports.RPT_URL != '')) AND wm_pbsrpt_reports.CANACCESS = 'WEBUI'",
								"wm_pbsrpt_reports.RPT_TITLE");
						result.setFocus(uowb.getBioCollectionBean(loadBiosQry));
					}
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent743", 100L);
					context.setNavigation("menuClickEvent743");
				}
				//Navigate to PARCEL PACKAGE DETAILS
				else if (fav.equals("PARCELPKG")) {
					Query loadBiosQry = new Query("wm_spscase", "", "wm_spscase.ADDDATE DESC");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent744", 100L);
					context.setNavigation("menuClickEvent744");
				}
				//Navigate to CHECK PACK
				else if (fav.equals("CHECKPACK")) {
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent745", 100L);
					context.setNavigation("menuClickEvent745");
				}
				//Navigate to MASS INTERNAL TRANSFER
				else if (fav.equals("MASSINTTRAN")) {
					Query loadBiosQry = new Query("wm_vITLotxLocxId_Lottables", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent746", 100L);
					context.setNavigation("menuClickEvent746");
				}
				//Navigate to APPOINTMENT
				else if (fav.equals("APPGRAPH")) {
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent747", 100L);
					context.setNavigation("menuClickEvent747");
				}
				//Navigate to APPOINTMENT LIST
				else if (fav.equals("APPLIST")) {
					Query loadBiosQry = new Query("wm_appointment", "", "wm_appointment.GMTSTARTDATEANDTIME DESC");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent748", 100L);
					context.setNavigation("menuClickEvent748");
				}
				//Navigate to PRINT CARRIER COMPLIANT LABELS
				else if (fav.equals("WPPRINTCARLBL")) {
					QBEBioBean caseLabels = null;
					try {
						caseLabels = uowb.getQBEBioWithDefaults("wp_case_labels");
					} catch (DataBeanException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					result.setFocus(caseLabels);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent749", 100L);
					context.setNavigation("menuClickEvent749");
				}
				//Navigate to Inbound QC
				else if (fav.equals("INBOUNDQC")) {
					Query loadBiosQry = new Query("receiptdetail", "receiptdetail.QCREQUIRED = '1'", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent760", 100L);
					context.setNavigation("menuClickEvent760");
				}
				//Navigate to Inbound Activity
				else if (fav.equals("INBOUNDACT")) {
					Query loadBiosQry = new Query("wm_ibactheader", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent761", 100L);
					context.setNavigation("menuClickEvent761");
				}
				//Navigate to Outbound Activity
				else if (fav.equals("OUTBOUNDACT")) {
					Query loadBiosQry = new Query("wm_obactheader", "", "");
					setBC(uowb.getBioCollectionBean(loadBiosQry), result);
					_log.debug("LOG_DEBUG_EXTENSION_FAVSGO", "Setting Nav menuClickEvent762", 100L);
					context.setNavigation("menuClickEvent762");
				}

			}else{
				return RET_CANCEL;
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_FAVSGO","Leaving FavoritesGoAction",100L);
		return RET_CONTINUE;
	}

	private void wpNewTemp(ActionResult result, StateInterface state, UnitOfWorkBean uowb) throws UserException {
		String SESSION_KEY_DEFAULT_FILTER_RECORDS_KEY = "wp.session.key.default.filter.records.key";
		Query qry = new Query("wp_filter", "wp_filter.USERDEFAULT = 1 AND wp_filter.USERID = '" + getUserId(state) + "'", "");
		BioCollection bc = uowb.getBioCollectionBean(qry);
		try {
			if (bc != null && bc.size() > 0) {
				String uniqueId = GUIDFactory.getGUIDStatic();
				Bio filterHeader = bc.elementAt(0);
				BioCollection filterDetails = filterHeader.getBioCollection("FILTERDETAILS");
				if (filterDetails != null && filterDetails.size() > 0) {
					for (int i = 0; i < filterDetails.size(); i++) {
						Bio filterDetail = filterDetails.elementAt(i);
						QBEBioBean newTempRecord = uowb.getQBEBioWithDefaults("wp_querybuilderdetail");
						newTempRecord.set("OPERATOR", filterDetail.get("OPERATOR"));
						newTempRecord.set("INTERACTIONID", uniqueId);
						newTempRecord.set("FIRSTVALUE", filterDetail.get("FIRSTVALUE"));
						newTempRecord.set("SECONDVALUE", filterDetail.get("SECONDVALUE"));
						newTempRecord.set("ORDERFIELD", filterDetail.get("ORDERFIELD"));
						newTempRecord.set("ANDOR", filterDetail.get("ANDOR"));
						newTempRecord.save();
					}
				}
				QBEBioBean newTempRecord = uowb.getQBEBioWithDefaults("querybuildertemp");
				newTempRecord.set("INTERACTIONID", uniqueId);
				newTempRecord.set("ORDERSMAX", filterHeader.get("MAXORDERS"));
				newTempRecord.set("CUBEMAX", filterHeader.get("MAXCUBE"));
				newTempRecord.set("INCLUDERF", filterHeader.get("RFID_STND"));
				newTempRecord.set("ORDERLINESMAX", filterHeader.get("MAXORDERLINES"));
				newTempRecord.set("WEIGHTMAX", filterHeader.get("MAXWEIGHT"));
				newTempRecord.set("CASES", filterHeader.get("MAXCASES"));
				newTempRecord.save();
				try {
					uowb.saveUOW(true);
				} catch (EpiException e) {
					e.printStackTrace();
					String args[] = new String[0];
					String errorMsg = getTextMessage("WMEXP_SYS_EXP", args, state.getLocale());
					throw new UserException(errorMsg, new Object[0]);
				}
				qry = new Query("querybuildertemp", "querybuildertemp.INTERACTIONID = '" + uniqueId + "' ", "");

				BioCollectionBean tempRecords = uowb.getBioCollectionBean(qry);
				BioRef focusRef = tempRecords.elementAt(0).getBioRef();
				result.setFocus(uowb.getBioBean(focusRef));
				state.getRequest().getSession().setAttribute(SESSION_KEY_DEFAULT_FILTER_RECORDS_KEY, uniqueId);
			} else {
				result.setFocus(uowb.getQBEBioWithDefaults("querybuildertemp"));
			}
		} catch (EpiDataException e) {
			e.printStackTrace();
			String args[] = new String[0];
			String errorMsg = getTextMessage("WMEXP_SYS_EXP", args, state.getLocale());
			throw new UserException(errorMsg, new Object[0]);
		} catch (DataBeanException e) {
			e.printStackTrace();
			String args[] = new String[0];
			String errorMsg = getTextMessage("WMEXP_SYS_EXP", args, state.getLocale());
			throw new UserException(errorMsg, new Object[0]);
		}
	}
	
	private void wpAddCookie(StateInterface state) {
		try {
			HttpSession session = state.getRequest().getSession();

			// get CurrentFacility
			String CurrentConnection = session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();
			String CurrentFacility = session.getAttribute(SetIntoHttpSessionAction.DB_USERID).toString();
			String currentFacilityDEtails = CurrentConnection + "~~" + CurrentFacility;
			// _log.debug("LOG_SYSTEM_OUT","**************Current connection = "+CurrentConnection,100L);
			// _log.debug("LOG_SYSTEM_OUT","**************Current Facility = "+CurrentFacility,100L);
			// _log.debug("LOG_SYSTEM_OUT","**************Current Facility Details = "+currentFacilityDEtails,100L);
			HttpServletResponse res = state.getResponse();
			_log.debug("LOG_SYSTEM_OUT", "\n\nadding cookie...\n\n", 100L);
			_log.debug("LOG_SYSTEM_OUT", "\n\n**************************************************************************\n\n", 100L);
			Cookie myCookie = new Cookie("WM_FACILITYDETAILS", currentFacilityDEtails);
			myCookie.setPath("/");
			_log.debug("LOG_SYSTEM_OUT", "cookie domain:" + SSOManager.getCookieDomain(), 100L);
			myCookie.setDomain(SSOManager.getCookieDomain());
			res.addCookie(myCookie);
			Integer ownerLockFlag = WSDefaultsUtil.getOwnerLockFlag(state);
			boolean isOwnerLocked = (ownerLockFlag == null || ownerLockFlag.intValue() == 0) ? false : true;
			if (isOwnerLocked) {
				ArrayList lockedOwners = WSDefaultsUtil.getLockedOwners(state);
				if (lockedOwners != null && lockedOwners.size() > 0) {
					String lockedOwnersList = "";
					for (int i = 0; i < lockedOwners.size(); i++) {
						if (i > 0)
							lockedOwnersList += ",";
						lockedOwnersList += lockedOwners.get(i);
					}
					Cookie defaultOwnerCookie = new Cookie("WM_DEFAULTOWNER", lockedOwnersList);
					defaultOwnerCookie.setPath("/");
					defaultOwnerCookie.setDomain(SSOManager.getCookieDomain());
					res.addCookie(defaultOwnerCookie);
				}
			}

		} catch (Exception e) {

			// Handle Exceptions 
			e.printStackTrace();

		}

	}

	private BioCollectionBean wpQuery(BioCollectionBean bioCollectionBean, String facilityAttrName, boolean filterByCurrentFacility, boolean filterByCurrentUser, String userAttrName,
			StateInterface state) {
		BioCollectionBean incomingCollection = bioCollectionBean;
		boolean filterFacility = filterByCurrentFacility;
		if (filterFacility == true) {
			Query facilityQuery = null;
			String facility = getFacility(state.getRequest());
			if (facility == null) {
				_log.error("LOG_ERROR_EXTENSION_WPQueryAction_execute",
						"Facility is null - doing nothing", SuggestedCategory.NONE);
				return bioCollectionBean;
			}
			String facilityName = facilityAttrName;
			facilityQuery = new Query(incomingCollection.getBioTypeName(), incomingCollection.getBioTypeName()
																			+ "." + facilityName + " = '" + facility + "'", null);
			try {
				incomingCollection.filterInPlace(facilityQuery);
			} catch (EpiDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		boolean filterUser = filterByCurrentUser;
		if (filterUser == true) {
			Query userQuery = null;
			String userId = getUserId(state);
			if (userId == null) {
				_log.error("LOG_ERROR_EXTENSION_WPQueryAction_execute",
						"Userid is null - doing nothing", SuggestedCategory.NONE);
				return bioCollectionBean;
			}
			String userIDName = userAttrName;
			userQuery = new Query(incomingCollection.getBioTypeName(), incomingCollection.getBioTypeName() + "."
																		+ userIDName + " = '" + userId + "'", null);
			try {
				incomingCollection.filterInPlace(userQuery);
			} catch (EpiDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return incomingCollection;
	}

	private void setBC(BioCollectionBean bioCollection, ActionResult result){
		bioCollection.setEmptyList(true);
		result.setFocus(bioCollection);
	}

	public static String getUserId(StateInterface state) {
		_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL", "In getUserId()", 100L);
		if (state == null) {
			_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL", "State Is Null...", 100L);
			_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL", "Exiting getUserId()", 100L);
			return null;
		}
		String userId = (String) state.getServiceManager().getUserContext().get("logInUserId");
		_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL", "Got User Id " + userId + " From get(logInUserId)...", 100L);
		if (userId == null || userId.length() == 0) {
			_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL", "get(logInUserId) returned empty...", 100L);
			String userAttr = state.getServiceManager().getUserContext().getUserName();
			_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL", "Got " + userAttr + " From getUserName()", 100L);
			String[] userAttrAry = userAttr.split(",");
			if (userAttrAry == null || userAttrAry.length == 0) {
				_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL", "Could Not Find User ID...", 100L);
				_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL", "Exiting getUserId()", 100L);
				return null;
			}
			for (int i = 0; i < userAttrAry.length; i++) {
				String[] keyValueAry = userAttrAry[i].split("=");
				if (keyValueAry.length != 2) {
					_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL", "Could Not Find User ID...", 100L);
					_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL", "Exiting getUserId()", 100L);
					return null;
				}
				if (keyValueAry[0].equalsIgnoreCase("uid")) {
					_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL", "Found User Id:" + keyValueAry[1] + "...", 100L);
					_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL", "Exiting getUserId()", 100L);
					return keyValueAry[1];
				}
			}

		} else {
			_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL", "Found User Id:" + userId + "...", 100L);
			_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL", "Exiting getUserId()", 100L);
			return userId;
		}
		_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL", "Could Not Find User ID...", 100L);
		_log.debug("LOG_DEBUG_JAVACLASS_USERUTIL", "Exiting getUserId()", 100L);
		return null;
	}

	public static String getFacility(HttpServletRequest request) {

		return getCurrentFacilityDetailsFromRequest(request)[1];
	}

	private static String[] getCurrentFacilityDetailsFromRequest(HttpServletRequest request) {
		String[] facilityDetailsArray = { null, null };

		// lets read the cookies and extract the one that we need
		String facilityDetails = null;
		Cookie[] cookies = request.getCookies();
		for (int i = 0; i < cookies.length; i++) {
			String name = cookies[i].getName();
			if ("WM_FACILITYDETAILS".equalsIgnoreCase(name)) {
				facilityDetails = cookies[i].getValue();
				break;
			}
		}

		// facilityDetails if found is expected in the format "connection string~~facility". Separate out.
		StringTokenizer strtok = new StringTokenizer(facilityDetails, "~~");
		int countOfTokens = strtok.countTokens();
		// we should have got 2, first is connection string and second is facility
		if (countOfTokens == 2) {
			facilityDetailsArray[0] = strtok.nextToken();
			facilityDetailsArray[1] = strtok.nextToken();
		}

		return facilityDetailsArray;
	}

}