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
import java.util.Iterator;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.util.UserUtil;


public class WSDefaultsQueryAction extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WSDefaultsQueryAction.class);
	
	public static int executeExtension(ActionContext context, ActionResult result) throws UserException{
		_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Executing WSDefaultsQueryAction",100L);		
		StateInterface state = context.getState();
		String uid = UserUtil.getUserId(state);				
		String oldDbName = (String)state.getRequest().getSession().getAttribute(BuiildDefaultCache.DB_CONNECTION);	
		//check records in WSDEFAULTSSCREEN table and if there is a difference between the number of records
		//and the number of screens avaliable in the SCREENS table then repopulate the table.
		_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Checking if WSDEFAULTSCREEN and SCREENS are in sync...",100L);		
		Query loadBiosQry = null;
		if(oldDbName.equalsIgnoreCase("enterprise"))
			loadBiosQry = new Query("wsdefaultsscreens", "wsdefaultsscreens.USERID = '"+uid+"' AND wsdefaultsscreens.ISENTERPRISE = '1'", null);				
		else
			loadBiosQry = new Query("wsdefaultsscreens", "wsdefaultsscreens.USERID = '"+uid+"' AND wsdefaultsscreens.ISENTERPRISE = '0'", null);
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
		BioCollection defaultScreensCollection = uow.getBioCollectionBean(loadBiosQry);
		loadBiosQry = new Query("screens", "screens.CANPREFILTER = '1'", null);
		BioCollection formCodeCollection = uow.getBioCollectionBean(loadBiosQry);
		try {
			if(formCodeCollection != null && defaultScreensCollection != null && formCodeCollection.size() != defaultScreensCollection.size()){
				_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Not in sync...",100L);				
				String queryString = "";
				for(int i = 0; i < defaultScreensCollection.size();i++){
					Bio bio = defaultScreensCollection.elementAt(i);				
					queryString += " AND screens.SCREENCODE != '"+bio.getString("FORMNAME").toUpperCase()+"' ";				
				}
				loadBiosQry = new Query("screens", "screens.CANPREFILTER = '1'"+queryString, null);
				formCodeCollection = uow.getBioCollectionBean(loadBiosQry);
				
				if(formCodeCollection != null && formCodeCollection.size() > 0){
					for(int i = 0; i < formCodeCollection.size(); i++){
						Bio bio = formCodeCollection.elementAt(i);
						QBEBioBean newRecord = uow.getQBEBioWithDefaults("wsdefaultsscreens");
						newRecord.set("FORMNAME",bio.get("SCREENCODE"));
						newRecord.set("ISSELECTED","1");
						newRecord.set("USERID",uid);
						if(oldDbName.equalsIgnoreCase("enterprise"))
							newRecord.set("ISENTERPRISE","1");
						else
							newRecord.set("ISENTERPRISE","0");
						newRecord.save();
						_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Saving new record...",100L);										
						uow.saveUOW(true);
					}
				}
			}
		} catch (EpiDataException e1) {
			e1.printStackTrace();				
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} catch (DataBeanException e1) {
			e1.printStackTrace();				
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} catch (EpiException e) {
			e.printStackTrace();				
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}		
		if(oldDbName.equalsIgnoreCase("enterprise"))
			loadBiosQry = new Query("wsdefaults", "wsdefaults.USERID = '"+uid+"' AND wsdefaults.ISENTERPRISE = '1'", null);											
		else
			loadBiosQry = new Query("wsdefaults", "wsdefaults.USERID = '"+uid+"' AND wsdefaults.ISENTERPRISE = '0'", null);											
		BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
		try {
			if(bioCollection.size() > 0){				
				result.setFocus((DataBean)bioCollection.elementAt(0));
				
			//If user has not set up defaults yet...
			}else{
				QBEBioBean tempRequestBio = null;							
				try
				{	
					//Create a new default record
					Query globalDefaultQry = new Query("wsdefaults","wsdefaults.USERID = 'XXXXXXXXXX'","");		        	
					BioCollection globalDefaultRecords =  uow.getBioCollectionBean(globalDefaultQry);		        	
					tempRequestBio = uow.getQBEBioWithDefaults("wsdefaults");		            
					tempRequestBio.set("USERID",uid);
					if(oldDbName.equalsIgnoreCase("enterprise"))
						tempRequestBio.set("ISENTERPRISE","1");
					else
						tempRequestBio.set("ISENTERPRISE","0");
					_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Created new default record...",100L);					
					//Make the user a clone of the global default record...
					if(!oldDbName.equalsIgnoreCase("enterprise")){
						try {
							WSDefaultsHeaderPrerenderAction.setIntoUserContextAndSession(context,"enterprise");
						} catch (Exception e) {						
							e.printStackTrace();
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Exiting WSDefaultsQueryAction",100L);							
							throw new UserException(errorMsg,new Object[0]);
						}
					}
					Query getEntDefaultRecord = new Query("wsdefaults","wsdefaults.USERID = '"+uid+"' AND wsdefaults.ISENTERPRISE = '1'","");
					BioCollectionBean entDefaultRecords = uow.getBioCollectionBean(getEntDefaultRecord);
					String facility = null;
					
					if(entDefaultRecords != null && entDefaultRecords.size() > 0){
						facility = (String)entDefaultRecords.elementAt(0).get("FACILITY");
					}										
					
					if(globalDefaultRecords != null && globalDefaultRecords.size() > 0){
						_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Found global default record...",100L);						
						Bio globalDefaultRec = globalDefaultRecords.elementAt(0);
						if(globalDefaultRec.get("STORERKEY") != null){
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Setting storerkey:"+globalDefaultRec.get("STORERKEY"),100L);							
							tempRequestBio.set("STORERKEY",globalDefaultRec.get("STORERKEY"));
						}
						if(globalDefaultRec.get("RECEIVELOC") != null){
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Setting RECEIVELOC:"+globalDefaultRec.get("RECEIVELOC"),100L);							
							tempRequestBio.set("RECEIVELOC",globalDefaultRec.get("RECEIVELOC"));
						}
						if(globalDefaultRec.get("PACK") != null){
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Setting PACK:"+globalDefaultRec.get("PACK"),100L);							
							tempRequestBio.set("PACK",globalDefaultRec.get("PACK"));
						}
						if(globalDefaultRec.get("PICKCODE") != null){
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Setting PICKCODE:"+globalDefaultRec.get("PICKCODE"),100L);							
							tempRequestBio.set("PICKCODE",globalDefaultRec.get("PICKCODE"));
						}						
						if(globalDefaultRec.get("STARTSLOT") != null){
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Setting STARTSLOT:"+globalDefaultRec.get("STARTSLOT"),100L);							
							tempRequestBio.set("STARTSLOT",globalDefaultRec.get("STARTSLOT"));
						}
						if(globalDefaultRec.get("ENDSLOT") != null){
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Setting ENDSLOT:"+globalDefaultRec.get("ENDSLOT"),100L);							
							tempRequestBio.set("ENDSLOT",globalDefaultRec.get("ENDSLOT"));
						}
						if(globalDefaultRec.get("STARTBAY") != null){
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Setting STARTBAY:"+globalDefaultRec.get("STARTBAY"),100L);							
							tempRequestBio.set("STARTBAY",globalDefaultRec.get("STARTBAY"));
						}
						if(globalDefaultRec.get("ENDBAY") != null){
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Setting ENDBAY:"+globalDefaultRec.get("ENDBAY"),100L);							
							tempRequestBio.set("ENDBAY",globalDefaultRec.get("ENDBAY"));
						}
						if(globalDefaultRec.get("NUMBEROFRECORDSTOKEEP") != null){
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Setting NUMBEROFRECORDSTOKEEP:"+globalDefaultRec.get("NUMBEROFRECORDSTOKEEP"),100L);							
							tempRequestBio.set("NUMBEROFRECORDSTOKEEP",globalDefaultRec.get("NUMBEROFRECORDSTOKEEP"));
						}
						if(globalDefaultRec.get("DYNAMICRECORDINGFLAG") != null){
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Setting DYNAMICRECORDINGFLAG:"+globalDefaultRec.get("DYNAMICRECORDINGFLAG"),100L);							
							tempRequestBio.set("DYNAMICRECORDINGFLAG",globalDefaultRec.get("DYNAMICRECORDINGFLAG"));
						}
						if(globalDefaultRec.get("OWNERLOCKFLAG") != null){
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Setting OWNERLOCKFLAG:"+globalDefaultRec.get("OWNERLOCKFLAG"),100L);							
							tempRequestBio.set("OWNERLOCKFLAG",globalDefaultRec.get("OWNERLOCKFLAG"));
						}
						if(facility != null){
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Setting facility:"+facility,100L);							
							tempRequestBio.set("FACILITY",facility);
						}
					}
					tempRequestBio.save();
					if(!oldDbName.equalsIgnoreCase("enterprise")){
						try {
							WSDefaultsHeaderPrerenderAction.setIntoUserContextAndSession(context,oldDbName);
						} catch (Exception e) {						
							e.printStackTrace();
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Exiting WSDefaultsQueryAction",100L);							
							throw new UserException(errorMsg,new Object[0]);
						}
					}
					uow.saveUOW(true);					
					Query getUserDefaultsQry = new Query("wsdefaults","wsdefaults.USERID = '"+uid+"'","");
					BioCollectionBean userDefColl = uow.getBioCollectionBean(getUserDefaultsQry);
					if(userDefColl != null && userDefColl.size() > 0){
						_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","New Default Record Created Successfully...",100L);						
						_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Screens Size:"+userDefColl.elementAt(0).getBioCollection("WSDEFAULTSCREENS").size(),100L);						
						result.setFocus((DataBean)userDefColl.elementAt(0));
					}
					else{
						_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","New Default Record Not Created...",100L);							
						String args[] = new String[0]; 
						String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
						_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Exiting WSDefaultsQueryAction",100L);						
						throw new UserException(errorMsg,new Object[0]);
					}		            
					
				}
				catch(DataBeanException ex)
				{
					ex.printStackTrace();				
					String args[] = {"wsdefaults"}; 
					String errorMsg = getTextMessage("ERROR_CREATE_TMP_BIO",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				} catch (EpiException e) {
					e.printStackTrace();				
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
					_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Exiting WSDefaultsQueryAction",100L);
					throw new UserException(errorMsg,new Object[0]);
				}
				//result.setFocus(tempRequestBio);
			}
		} catch (EpiDataException e) {
			e.printStackTrace();				
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Exiting WSDefaultsQueryAction",100L);
		return RET_CONTINUE;
	}
	
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		
		return WSDefaultsQueryAction.executeExtension(context,result);
		
	}	
}