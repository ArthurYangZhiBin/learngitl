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

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
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
import com.ssaglobal.scm.wms.datalayer.WmsDataProviderImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.uiextensions.KeyGenBioWrapper;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.UserUtil;
import com.ssaglobal.scm.wms.wm_facility.LoginNavigationPicker;

public class WSDefaultHeaderDetailSave extends SaveAction{
	public static String DB_CONNECTION = "dbConnectionName";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WSDefaultHeaderDetailSave.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Executing WSDefaultHeaderDetailSave",100L);		
		StateInterface state = context.getState();	
		ArrayList tabList = new ArrayList();		
		tabList.add("tab 0");
		tabList.add("tab 1");
		tabList.add("tab 2");
		tabList.add("tab 3");
		RuntimeFormInterface defaultDataForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_ws_defaults_header_detail",tabList,state);		
		RuntimeFormInterface prefilterDetailForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_ws_defaults_detail_detail",tabList,state);					
		RuntimeFormInterface prefilterListForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_ws_defaults_detail_list",tabList,state);
		RuntimeListFormInterface favoritesListForm = (RuntimeListFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_ws_defaults_favorites_list_form",tabList,state);
		RuntimeFormInterface rptPrnbyUserDetailForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_report_printerbyuser_detail_view",tabList,state);
		String uid = UserUtil.getUserId(state);
		
		if(defaultDataForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Found Default Data Form:"+defaultDataForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Found Default Data Form:Null",100L);			
		if(prefilterDetailForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Found PreFilter Form:"+prefilterDetailForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Found PreFilter Form:Null",100L);			
		if(favoritesListForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Found Favorites List Form:"+favoritesListForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Found Favorites List Form:Null",100L);			
		if(prefilterListForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Found PreFilter List Form:"+prefilterListForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Found PreFilter List Form:Null",100L);			
		if(rptPrnbyUserDetailForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Found Printer Form Form:"+rptPrnbyUserDetailForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Found Printer Form Form:Null",100L);			

		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		
		//get default data focus				
		DataBean defaultDetailFocus = defaultDataForm == null?null:defaultDataForm.getFocus();		
		
		//If user has changed favorites then save changes here
		String oldDbName = (String)state.getRequest().getSession().getAttribute(DB_CONNECTION);	
/*		try {
			//HC
			if(favoritesListForm != null && favoritesListForm.getFocus().isBioCollection()){							
				LoginNavigationPicker.setIntoUserContextAndSession(context,"enterprise");				
				BioCollectionBean favorites = (BioCollectionBean)favoritesListForm.getFocus();
				if(favorites != null && favorites.size() > 0){
					HttpSession session = state.getRequest().getSession();
					ArrayList addFavs = (ArrayList)session.getAttribute(WSDefaultPersistScreensInContext.SESSION_KEY_ADD_FAV); 
					ArrayList delFavs = (ArrayList)session.getAttribute(WSDefaultPersistScreensInContext.SESSION_KEY_REM_FAV);
					if(addFavs == null)
						addFavs = new ArrayList();
					if(delFavs == null)
						delFavs = new ArrayList();
					for(int i = 0; i < favorites.size(); i++){
						com.epiphany.shr.data.bio.Bio favorite = favorites.elementAt(i);
						_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","SCREENCODE:"+favorite.getString("SCREENCODE"),100L);						
						_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","ISSELECTED:"+favorite.getString("ISSELECTED"),100L);						
						if(favorite.getString("ISSELECTED") != null && favorite.getString("ISSELECTED").equals("1")){
							if(!addFavs.contains(favorite.getString("SCREENCODE")))
								addFavs.add(favorite.getString("SCREENCODE"));							
						}
						if(favorite.getString("ISSELECTED") != null && favorite.getString("ISSELECTED").equals("0")){
							if(!delFavs.contains(favorite.getString("SCREENCODE")))
								delFavs.add(favorite.getString("SCREENCODE"));							
						}
					}
					if(addFavs.size() > 0 || delFavs.size() > 0)
						context.setNavigation("clickEvent884");
					Query loadBiosQry = new Query("userfavorites", "userfavorites.USERID = '"+uid+"'", null);				
					BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);	
					if(bioCollection != null && bioCollection.size() > 0){
						_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Deleting Favs:"+delFavs,100L);						
						for(int i = 0; i < bioCollection.size(); i++){
							com.epiphany.shr.data.bio.Bio favorite = bioCollection.elementAt(i);
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Should Delete Favorite:"+favorite.getString("SCREEN")+" ?",100L);							
							if(delFavs.contains(favorite.getString("SCREEN"))){
								_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Deleting Fav...",100L);								
								favorite.delete();
							}else if(addFavs.contains(favorite.getString("SCREEN"))){
								addFavs.remove(favorite.getString("SCREEN"));
							}
						}
					}
					_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Adding Favs:"+addFavs,100L);					
					for(int i = 0; i < addFavs.size(); i++){						
						String screenCode = (String)addFavs.get(i);
						QBEBioBean newRecord = uow.getQBEBioWithDefaults("userfavorites");
						_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Adding Fav...",100L);						
						newRecord.set("USERID",uid);
						newRecord.set("SCREEN",screenCode);
						newRecord.save();
					}				
				}
				LoginNavigationPicker.setIntoUserContextAndSession(context,oldDbName);	
			}
		} catch (EpiDataException e1) {
			e1.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} catch (DataBeanException e1) {
			e1.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} catch (Exception e) {
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}	finally{
			try {
				LoginNavigationPicker.setIntoUserContextAndSession(context,oldDbName);
			} catch (Exception e) {
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}	
		}*/
	
		try {
			if (defaultDetailFocus != null && defaultDetailFocus.isTempBio()) {
				//it is for insert header
				_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","inserting header ******",100L);				
				defaultDetailFocus.setValue("USERID",uid);
				if(oldDbName.equalsIgnoreCase("enterprise"))
					defaultDetailFocus.setValue("ISENTERPRISE","1");				
				else
					defaultDetailFocus.setValue("ISENTERPRISE","0");				
			} else {
				//it is for update header
				_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","updating default values ******",100L);				  				
				if (prefilterDetailForm != null && prefilterDetailForm.getFocus() != null && prefilterDetailForm.getFocus().isTempBio()) {
					_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","*****detaiFocus is tempbio="+prefilterDetailForm.getFocus().isTempBio(),100L);					
					String wsDetailsKey = new KeyGenBioWrapper().getKey("wsdefaultsdetail");
					_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","inserting filter value ******",100L);
					_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Setting Detail USERID:"+uid,100L);
					_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Setting Detail PK:"+wsDetailsKey,100L);					
					if(!((QBEBioBean)prefilterDetailForm.getFocus()).isEmpty()){
						prefilterDetailForm.getFocus().setValue("USERID",uid);
						prefilterDetailForm.getFocus().setValue("WSDEFAULTSDETAILKEY",wsDetailsKey);
						if(oldDbName.equalsIgnoreCase("enterprise"))
							prefilterDetailForm.getFocus().setValue("ISENTERPRISE","1");
						else
							prefilterDetailForm.getFocus().setValue("ISENTERPRISE","0");
					}
				} 
				
				//Logic for List Quick Add
				if(prefilterListForm != null && prefilterListForm.getFocus() != null){
					QBEBioBean newRow = ((RuntimeListFormInterface)prefilterListForm).getQuickAddRowBean();
					if(newRow != null){
						_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Found New Row:"+newRow,100L);
						_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Got New Row Filter Value:"+newRow.get("FILTERVALUE"),100L);
						_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Got New Row Type:"+newRow.get("TYPE"),100L);
						_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","inserting filter value ******",100L);
						_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Setting Detail USERID:"+uid,100L);					
						String wsDetailsKey = new KeyGenBioWrapper().getKey("wsdefaultsdetail"); 
						_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Setting Detail PK:"+wsDetailsKey,100L);						
						if(!newRow.isEmpty()){					
							((RuntimeListFormInterface)prefilterListForm).getQuickAddRowBean().setValue("USERID",uid);
							((RuntimeListFormInterface)prefilterListForm).getQuickAddRowBean().setValue("WSDEFAULTSDETAILKEY",wsDetailsKey);
							if(oldDbName.equalsIgnoreCase("enterprise"))
								((RuntimeListFormInterface)prefilterListForm).getQuickAddRowBean().setValue("ISENTERPRISE","1");
							else
								((RuntimeListFormInterface)prefilterListForm).getQuickAddRowBean().setValue("ISENTERPRISE","0");
						}
					}
					else
						_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","New Row Is Null...",100L);						
				}
				//it is for Inserting Printer by User
				_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Inserting Printer bu User ******",100L);				  				
				if (rptPrnbyUserDetailForm != null && rptPrnbyUserDetailForm.getFocus() != null && rptPrnbyUserDetailForm.getFocus().isTempBio()) {
					_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","*****detaiFocus is tempbio="+rptPrnbyUserDetailForm.getFocus().isTempBio(),100L);					
					String wsDetailsKey = new KeyGenBioWrapper().getKey("wsdefaultsdetail");
					_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","inserting filter value ******",100L);
					_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Setting Detail USERID:"+uid,100L);
					_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Setting Detail PK:"+wsDetailsKey,100L);					
					if(!((QBEBioBean)rptPrnbyUserDetailForm.getFocus()).isEmpty()){
						rptPrnbyUserDetailForm.getFocus().setValue("USERID",uid);
					}
				} 
				_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","updating detail ******",100L);				
				
			}
			
			try {
				uow.saveUOW(true);
			} catch (EpiException e) {
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
									
			
			//save facility value at enterprise lvl
			if(defaultDataForm != null){
				HttpSession session = state.getRequest().getSession();
				String dbConnection = (String)session.getAttribute(DB_CONNECTION);			
				try {
					if(favoritesListForm != null && favoritesListForm.getFocus().isBioCollection()){	//HC
						LoginNavigationPicker.setIntoUserContextAndSession(context,"ENTERPRISE");
						//HC.b	
						BioCollectionBean favorites = (BioCollectionBean)favoritesListForm.getFocus();
						if(favorites != null && favorites.size() > 0){
//							HttpSession session = state.getRequest().getSession();
							ArrayList addFavs = (ArrayList)session.getAttribute(WSDefaultPersistScreensInContext.SESSION_KEY_ADD_FAV); 
							ArrayList delFavs = (ArrayList)session.getAttribute(WSDefaultPersistScreensInContext.SESSION_KEY_REM_FAV);
							if(addFavs == null)
								addFavs = new ArrayList();
							if(delFavs == null)
								delFavs = new ArrayList();
							for(int i = 0; i < favorites.size(); i++){
								com.epiphany.shr.data.bio.Bio favorite = favorites.elementAt(i);
								_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","SCREENCODE:"+favorite.getString("SCREENCODE"),100L);						
								_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","ISSELECTED:"+favorite.getString("ISSELECTED"),100L);						
								if(favorite.getString("ISSELECTED") != null && favorite.getString("ISSELECTED").equals("1")){
									if(!addFavs.contains(favorite.getString("SCREENCODE")))
										addFavs.add(favorite.getString("SCREENCODE"));							
								}
								if(favorite.getString("ISSELECTED") != null && favorite.getString("ISSELECTED").equals("0")){
									if(!delFavs.contains(favorite.getString("SCREENCODE")))
										delFavs.add(favorite.getString("SCREENCODE"));							
								}
							}
							if(addFavs.size() > 0 || delFavs.size() > 0)
								context.setNavigation("clickEvent884");
							Query loadBiosQry = new Query("userfavorites", "userfavorites.USERID = '"+uid+"'", null);				
							BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);	
							if(bioCollection != null && bioCollection.size() > 0){
								_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Deleting Favs:"+delFavs,100L);						
								for(int i = 0; i < bioCollection.size(); i++){
									com.epiphany.shr.data.bio.Bio favorite = bioCollection.elementAt(i);
									_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Should Delete Favorite:"+favorite.getString("SCREEN")+" ?",100L);							
									if(delFavs.contains(favorite.getString("SCREEN"))){
										_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Deleting Fav...",100L);								
										favorite.delete();
									}else if(addFavs.contains(favorite.getString("SCREEN"))){
										addFavs.remove(favorite.getString("SCREEN"));
									}
								}
							}
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Adding Favs:"+addFavs,100L);					
							for(int i = 0; i < addFavs.size(); i++){						
								String screenCode = (String)addFavs.get(i);
								QBEBioBean newRecord = uow.getQBEBioWithDefaults("userfavorites");
								_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Adding Fav...",100L);						
								newRecord.set("USERID",uid);
								newRecord.set("SCREEN",screenCode);
								newRecord.save();
							}				
						}
					}
//HC.e					
//					Check for a record using the enterprise connection. If none exists create one using the global defaults.
					//This is to ensure the update of the facility is readable with the enterprise connection
					String qryStr = "SELECT * FROM WSDEFAULTS WHERE USERID = '"+uid+"'";
					EXEDataObject entResult = WmsWebuiValidationSelectImpl.select(qryStr);					
					//If no record then create one using global defaults				
					if(entResult == null || entResult.getRowCount() == 0){						
						Query bioQry = new Query("wsdefaults","wsdefaults.USERID = 'XXXXXXXXXX'","");
						BioCollection globalDefaultRecords =  uow.getBioCollectionBean(bioQry);		        	
						QBEBioBean tempRequestBio = uow.getQBEBioWithDefaults("wsdefaults");		            
						tempRequestBio.set("USERID",uid);
						tempRequestBio.set("ISENTERPRISE","1");
						_log.debug("LOG_SYSTEM_OUT","Creating New Default Record",100L);
						_log.debug("LOG_DEBUG_EXTENSION_WSDEFSAVE","Created new default record...",100L);
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
						}
						if(defaultDetailFocus.getValue("FACILITY") != null){
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Setting facility:"+defaultDetailFocus.getValue("FACILITY"),100L);							
							tempRequestBio.set("FACILITY",defaultDetailFocus.getValue("FACILITY"));
						}						
						tempRequestBio.save();
//HC					uow.saveUOW(true);
					}
					uow.saveUOW(true);	//HC
					String sql = null;
					if(defaultDetailFocus.getValue("FACILITY") != null)
//HC-defaultScreen.b
//						sql = "UPDATE WSDEFAULTS SET FACILITY = '"+defaultDetailFocus.getValue("FACILITY")+"' WHERE USERID = '"+uid+"'";
						if (defaultDetailFocus.getValue("DEFAULTSCREEN")== null){
							sql = "UPDATE WSDEFAULTS SET FACILITY = '"+defaultDetailFocus.getValue("FACILITY")+"',DEFAULTSCREEN = null WHERE USERID = '"+uid+"'";  ////HC-defaultScreen							
						}else{
							sql = "UPDATE WSDEFAULTS SET FACILITY = '"+defaultDetailFocus.getValue("FACILITY")+"',DEFAULTSCREEN = '"+defaultDetailFocus.getValue("DEFAULTSCREEN")+"' WHERE USERID = '"+uid+"'";  ////HC-defaultScreen
						}
//HC-defaultScreen.e
					else
//						sql = "UPDATE WSDEFAULTS SET FACILITY = NULL WHERE USERID = '"+uid+"'";
						sql = "UPDATE WSDEFAULTS SET FACILITY = NULL, DEFAULTSCREEN = NULL WHERE USERID = '"+uid+"'";
//HC-defaultScreen.e					
					new WmsDataProviderImpl().executeUpdateSql(sql);
					LoginNavigationPicker.setIntoUserContextAndSession(context,dbConnection);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				uow.clearState();
				result.setFocus(defaultDetailFocus);
			}
			else{
				Query loadBiosQry = null;
				if(oldDbName.equalsIgnoreCase("enterprise"))
					loadBiosQry = new Query("wsdefaults", "wsdefaults.USERID = '"+uid+"' AND wsdefaults.ISENTERPRISE = '1'", null);											
				else
					loadBiosQry = new Query("wsdefaults", "wsdefaults.USERID = '"+uid+"' AND wsdefaults.ISENTERPRISE = '0'", null);
				BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);				
				if(bioCollection.size() > 0){
					result.setFocus((DataBean)bioCollection.elementAt(0));
				}else{
					QBEBioBean tempRequestBio = null;
					UnitOfWorkBean uowb = state.getTempUnitOfWork();
					try
					{
						tempRequestBio = uowb.getQBEBioWithDefaults("wsdefaults");
						tempRequestBio.set("USERID",uid);
						if(oldDbName.equalsIgnoreCase("enterprise"))
				           	tempRequestBio.set("ISENTERPRISE","1");
				        else
				           	tempRequestBio.set("ISENTERPRISE","0");
					}
					catch(DataBeanException ex)
					{
						throwUserException(ex, "ERROR_CREATE_TMP_BIO", new String[] {
								"wsdefaults"
						});
					}
					result.setFocus(tempRequestBio);
				}
			}
		} catch (EpiDataException e) {
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} 									
		return RET_CONTINUE;	
	}
}
