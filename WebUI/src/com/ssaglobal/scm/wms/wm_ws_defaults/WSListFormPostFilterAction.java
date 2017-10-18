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
package com.ssaglobal.scm.wms.wm_ws_defaults;
import java.util.ArrayList;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.KeyGenBioWrapper;
import com.ssaglobal.scm.wms.util.UserUtil;


public class WSListFormPostFilterAction extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WSListFormPostFilterAction.class);
	private final int PRE_FILTER_THRESHOLD = 30;
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		_log.debug("LOG_DEBUG_EXTENSION_WSLISTFRMPOSTFLTR","Executing WSListFormPostFilterAction",100L);		
		StateInterface state = context.getState();		
		ArrayList  bioAttrNames = (ArrayList)getParameter("bioAttrNames");
		ArrayList bioAttrTypes = (ArrayList)getParameter("bioAttrTypes");
		int maxDefaultRecords = 0;
//		Parameters are either invalid or are such that the developer want no action to take place
		if(bioAttrNames == null || bioAttrTypes == null || (bioAttrNames.size() != bioAttrTypes.size()) || bioAttrNames.size() == 0){
			_log.debug("LOG_DEBUG_EXTENSION_WSLISTFRMPOSTFLTR","Exiting Without Performing Any Actions...",100L);			
			_log.debug("LOG_DEBUG_EXTENSION_WSLISTFRMPOSTFLTR","Exiting WSListFormPrerenderAction",100L);			
			return RET_CONTINUE;
		}
		
		//Check If User Has Set Defaults To Record Filter Values Dynamically
		String uid = UserUtil.getUserId(state);
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
		if(		!WSDefaultsUtil.isCacheAvaliable(state) ||
				WSDefaultsUtil.getDynamicRecordeingFlag(state) == null ||
				WSDefaultsUtil.getDynamicRecordeingFlag(state).toString().equals("0")){		
			_log.debug("LOG_DEBUG_EXTENSION_WSLISTFRMPOSTFLTR","User Has Not Set Up Defaults To Record Dynamically...",100L);
			_log.debug("LOG_DEBUG_EXTENSION_WSLISTFRMPOSTFLTR","Exiting WSListFormPrerenderAction",100L);
			return RET_CONTINUE;
		}
		
		//Get Max Records Number
		try {
			if(WSDefaultsUtil.getNumberOfRecordsToKeep(state) != null && WSDefaultsUtil.getNumberOfRecordsToKeep(state).length() > 0){
				maxDefaultRecords = Integer.parseInt(WSDefaultsUtil.getNumberOfRecordsToKeep(state));
				if(maxDefaultRecords > PRE_FILTER_THRESHOLD)
					maxDefaultRecords = PRE_FILTER_THRESHOLD;
			}
			
			else{				
				_log.debug("LOG_DEBUG_EXTENSION_WSLISTFRMPOSTFLTR","Could Not Find MAX NUMBER OF RECORDS TO KEEP In Cache...",100L);
				_log.debug("LOG_DEBUG_EXTENSION_WSLISTFRMPOSTFLTR","Exiting WSListFormPrerenderAction",100L);
				return RET_CONTINUE;
			}
		} catch (NumberFormatException e1) {			
			e1.printStackTrace();
			_log.debug("LOG_DEBUG_EXTENSION_WSLISTFRMPOSTFLTR","Could Not Find MAX NUMBER OF RECORDS TO KEEP...",100L);			
			_log.debug("LOG_DEBUG_EXTENSION_WSLISTFRMPOSTFLTR","Exiting WSListFormPrerenderAction",100L);
			return RET_CONTINUE;
		}
		
		//Load List with filter types
		ArrayList typeList = new ArrayList();
		Query loadBiosQry = new Query("codelkup", "codelkup.LISTNAME = 'WSTYPE'", null);	
		BioCollection codelkupCollection = uow.getBioCollectionBean(loadBiosQry);
		try {
			if(codelkupCollection == null || codelkupCollection.size() == 0){
				_log.debug("LOG_DEBUG_EXTENSION_WSLISTFRMPOSTFLTR","Table CODELKUP has no records with LISTNAME = WSTYPE...",100L);				
				_log.debug("LOG_DEBUG_EXTENSION_WSLISTFRMPOSTFLTR","Exiting WSListFormPrerenderAction",100L);
				return RET_CONTINUE;
			}
		} catch (EpiDataException e) {		
			e.printStackTrace();
			return RET_CONTINUE;
		}
		try {
			for(int i = 0; i < codelkupCollection.size(); i++){
				Bio bio =  codelkupCollection.elementAt(i);
				typeList.add(bio.get("CODE"));
			}
		} catch (EpiDataException e) {
			e.printStackTrace();
			return RET_CONTINUE;
		}
		
		com.epiphany.shr.ui.model.data.DataBean db = state.getBucketValueDataBean("filter");
		String oldDbName = (String)state.getRequest().getSession().getAttribute(BuiildDefaultCache.DB_CONNECTION);	
		for(int i = 0; i < bioAttrNames.size(); i++){
			String bioAttrName = (String)bioAttrNames.get(i);
			String bioAttrType = (String)bioAttrTypes.get(i);
			String bioAttrValue = (String)db.getValue(bioAttrName);
			
			//If filter value is null do not record it...
			if(bioAttrValue == null || bioAttrValue.length() == 0)
				continue;
			
			//Is this a valid type?
			if(!typeList.contains(bioAttrType)){
				_log.debug("LOG_DEBUG_EXTENSION_WSLISTFRMPOSTFLTR","Invalid Type:"+bioAttrType+"...",100L);				
				continue;
			}
			_log.debug("LOG_DEBUG_EXTENSION_WSLISTFRMPOSTFLTR","Got Value:"+bioAttrValue+" from bio attribute:"+bioAttrName+"...",100L);				
			//Is this a duplicate default record?
			BioCollection defaultsCollection;
			try {
				if(oldDbName.equalsIgnoreCase("enterprise"))
					loadBiosQry = new Query("wsdefaultsdetail", "wsdefaultsdetail.USERID = '"+uid.toUpperCase()+"' AND wsdefaultsdetail.TYPE = '"+bioAttrType.toUpperCase()+"' AND wsdefaultsdetail.FILTERVALUE = '"+bioAttrValue.toUpperCase()+"' AND wsdefaultsdetail.ISENTERPRISE = '1' ", null);
				else
					loadBiosQry = new Query("wsdefaultsdetail", "wsdefaultsdetail.USERID = '"+uid.toUpperCase()+"' AND wsdefaultsdetail.TYPE = '"+bioAttrType.toUpperCase()+"' AND wsdefaultsdetail.FILTERVALUE = '"+bioAttrValue.toUpperCase()+"' AND wsdefaultsdetail.ISENTERPRISE = '0'", null);
				defaultsCollection = uow.getBioCollectionBean(loadBiosQry);
				if(defaultsCollection == null || defaultsCollection.size() > 0){					
					_log.debug("LOG_DEBUG_EXTENSION_WSLISTFRMPOSTFLTR","Default Record Already Exists...",100L);
					continue;
				}
			} catch (EpiDataException e) {
				e.printStackTrace();
				return RET_CONTINUE;
			}
			if(oldDbName.equalsIgnoreCase("enterprise"))
				loadBiosQry = new Query("wsdefaultsdetail", "wsdefaultsdetail.USERID = '"+uid.toUpperCase()+"' AND wsdefaultsdetail.TYPE = '"+bioAttrType.toUpperCase()+"' AND wsdefaultsdetail.ISENTERPRISE = '1' ", "wsdefaultsdetail.EDITDATE ASC");
			else
				loadBiosQry = new Query("wsdefaultsdetail", "wsdefaultsdetail.USERID = '"+uid.toUpperCase()+"' AND wsdefaultsdetail.TYPE = '"+bioAttrType.toUpperCase()+"' AND wsdefaultsdetail.ISENTERPRISE = '0' ", "wsdefaultsdetail.EDITDATE ASC");
			defaultsCollection = uow.getBioCollectionBean(loadBiosQry);
			
			//If owner is locked and filter is of type STORER, BILLTO, CUSTOM, CARRIER, or VENDOR do nothing.
			if(WSDefaultsUtil.isOwnerLocked(state)){
				if(bioAttrType.equalsIgnoreCase("STORER") ||
					bioAttrType.equalsIgnoreCase("BILLTO") ||
					bioAttrType.equalsIgnoreCase("CUSTOM") ||
					bioAttrType.equalsIgnoreCase("CARRIER") ||
					bioAttrType.equalsIgnoreCase("VENDOR"))
					return RET_CONTINUE;
			}
			
			//If number of records has not reached max then add it, else replace the oldest record with it.
			try {
				if(defaultsCollection == null || defaultsCollection.size() < maxDefaultRecords){
					_log.debug("LOG_DEBUG_EXTENSION_WSLISTFRMPOSTFLTR","Max Size Not Yet Reached...",100L);					
					QBEBioBean tempRequestBio = uow.getQBEBioWithDefaults("wsdefaultsdetail");
					String wsDetailsKey = new KeyGenBioWrapper().getKey("wsdefaultsdetail"); 
					tempRequestBio.set("USERID",uid);
					tempRequestBio.set("TYPE",bioAttrType);
					tempRequestBio.set("FILTERVALUE",bioAttrValue);
					tempRequestBio.set("ISSELECTED","0");
					tempRequestBio.set("WSDEFAULTSDETAILKEY",wsDetailsKey);
					if(oldDbName.equalsIgnoreCase("enterprise"))
						tempRequestBio.set("ISENTERPRISE","1");
					else
						tempRequestBio.set("ISENTERPRISE","0");
					uow.saveUOW(true);
				}
				else{
					//If there are more than 1 record present then re query for only unselected records so that the selected record's
					//data does not get overwritten
					if(defaultsCollection.size() > 1){
						if(oldDbName.equalsIgnoreCase("enterprise"))
							loadBiosQry = new Query("wsdefaultsdetail", "wsdefaultsdetail.USERID = '"+uid.toUpperCase()+"' AND wsdefaultsdetail.TYPE = '"+bioAttrType.toUpperCase()+"' AND wsdefaultsdetail.ISSELECTED = '0' AND wsdefaultsdetail.ISENTERPRISE = '1'", "wsdefaultsdetail.EDITDATE ASC");
						else
							loadBiosQry = new Query("wsdefaultsdetail", "wsdefaultsdetail.USERID = '"+uid.toUpperCase()+"' AND wsdefaultsdetail.TYPE = '"+bioAttrType.toUpperCase()+"' AND wsdefaultsdetail.ISSELECTED = '0' AND wsdefaultsdetail.ISENTERPRISE = '0'", "wsdefaultsdetail.EDITDATE ASC");
						defaultsCollection = uow.getBioCollectionBean(loadBiosQry);
					}
					_log.debug("LOG_DEBUG_EXTENSION_WSLISTFRMPOSTFLTR","Max Size Reached...",100L);					
					Bio bio = defaultsCollection.elementAt(0);
					bio.delete();
					QBEBioBean tempRequestBio = uow.getQBEBioWithDefaults("wsdefaultsdetail");
					String wsDetailsKey = new KeyGenBioWrapper().getKey("wsdefaultsdetail"); 
					tempRequestBio.set("TYPE",bioAttrType);
					tempRequestBio.set("FILTERVALUE",bioAttrValue);
					tempRequestBio.set("USERID",uid);
					tempRequestBio.set("ISSELECTED","0");
					if(oldDbName.equalsIgnoreCase("enterprise"))
						tempRequestBio.set("ISENTERPRISE","1");
					else
						tempRequestBio.set("ISENTERPRISE","0");
					tempRequestBio.set("WSDEFAULTSDETAILKEY",wsDetailsKey);
					uow.saveUOW(true);
					
				}
			} catch (EpiDataException e) {
				e.printStackTrace();
				return RET_CONTINUE;
			} catch (DataBeanException e) {
				e.printStackTrace();
				return RET_CONTINUE;
			} catch (EpiException e) {
				e.printStackTrace();
				return RET_CONTINUE;
			}
			
		}		
		_log.debug("LOG_DEBUG_EXTENSION_WSLISTFRMPOSTFLTR","Exiting WSListFormPostFilterAction",100L);		
		return RET_CONTINUE;
		
	}			
}