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
import javax.servlet.http.HttpSession;

import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
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
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.util.UserUtil;
import com.ssaglobal.scm.wms.wm_facility.LoginNavigationPicker;


public class ValidateUserScreenList extends ActionExtensionBase{
	public static String DB_CONNECTION = "dbConnectionName";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateUserScreenList.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATTEUSRSCRNLST","Executing ValidateUserScreenList",100L);		
		StateInterface state = context.getState();
		HttpSession session = state.getRequest().getSession();
		String uid = UserUtil.getUserId(state);
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATTEUSRSCRNLST","Form:"+context.getState().getCurrentRuntimeForm().getName(),100L);		
		Query DatasourceQuery = new Query("wm_pl_db","wm_pl_db.isActive = '1'",null);		//8050
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
		BioCollectionBean DataSources = uow.getBioCollectionBean(DatasourceQuery);
		String oldDbName = (String)session.getAttribute(DB_CONNECTION);
		
		try {
			for(int j = 0; j < DataSources.size(); j++){
				String dbName = DataSources.elementAt(j).get("db_name").toString();
				if(dbName != null){
					try {
						LoginNavigationPicker.setIntoUserContextAndSession(context,dbName);
					} catch (Exception e2) {
						e2.printStackTrace();
						String args[] = new String[0]; 
						String errorMsg = getTextMessage("WMEXP_SYS_ERROR",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);					
					}
					//check records in WSDEFAULTSSCREEN table and if there is a difference between the number of records
					//and the number of screens avaliable in the SCREENS table then repopulate the table.
					_log.debug("LOG_DEBUG_EXTENSION_VALIDATTEUSRSCRNLST","Checking if WSDEFAULTSCREEN and SCREENS are in sync...",100L);					
					String sql = "";
					if(dbName.equalsIgnoreCase("enterprise"))
						sql = "SELECT * FROM WSDEFAULTSSCREENS WHERE USERID = '"+uid+"' AND ISENTERPRISE = '1'";
					else
						sql = "SELECT * FROM WSDEFAULTSSCREENS WHERE USERID = '"+uid+"' AND ISENTERPRISE = '0'";
					//Query loadBiosQry = new Query("wsdefaultsscreens", "wsdefaultsscreens.USERID = '"+uid+"'", null);												
					//BioCollection defaultScreensCollection = uow.getBioCollectionBean(loadBiosQry);
					EXEDataObject defaultScreens = WmsWebuiValidationSelectImpl.select(sql);
					Query loadBiosQry = new Query("screens", "screens.CANPREFILTER = '1'", null);
					BioCollection formCodeCollection = uow.getBioCollectionBean(loadBiosQry);
					try {
						if(formCodeCollection != null && defaultScreens != null && formCodeCollection.size() != defaultScreens.getRowCount()){
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATTEUSRSCRNLST","Not in sync...",100L);							
							String queryString = "";
							for(int i = 0; i < defaultScreens.getRowCount();i++){
								//Bio bio = defaultScreens.elementAt(i);				
								queryString += " AND screens.SCREENCODE != '"+defaultScreens.getAttribValue(new TextData("FORMNAME")).getAsString().toUpperCase()+"' ";
								defaultScreens.getNextRow();
							}							
							loadBiosQry = new Query("screens", "screens.CANPREFILTER = '1'"+queryString, null);							
							formCodeCollection = uow.getBioCollectionBean(loadBiosQry);
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATTEUSRSCRNLST","Query:"+loadBiosQry.getQueryExpression(),100L);
							_log.debug("LOG_DEBUG_EXTENSION_VALIDATTEUSRSCRNLST","Inserting "+formCodeCollection.size()+" Records...",100L);							
							if(formCodeCollection != null && formCodeCollection.size() > 0){
								for(int i = 0; i < formCodeCollection.size(); i++){
									Bio bio = formCodeCollection.elementAt(i);
									QBEBioBean newRecord = uow.getQBEBioWithDefaults("wsdefaultsscreens");
									newRecord.set("FORMNAME",bio.get("SCREENCODE"));
									newRecord.set("ISSELECTED","1");
									if(dbName.equalsIgnoreCase("enterprise"))
										newRecord.set("ISENTERPRISE","1");
									else
										newRecord.set("ISENTERPRISE","0");
									newRecord.set("USERID",uid);
									newRecord.save();
									_log.debug("LOG_DEBUG_EXTENSION_VALIDATTEUSRSCRNLST","Saving new record...",100L);									
									uow.saveUOW(true);
									uow.clearState();
								}
							}
						}
					} catch (EpiDataException e1) {
						e1.printStackTrace();
						try {
							LoginNavigationPicker.setIntoUserContextAndSession(context,oldDbName);
						} catch (Exception e) {
							e.printStackTrace();
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);						
						}									
						String args[] = new String[0]; 
						String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					} catch (DataBeanException e1) {
						e1.printStackTrace();	
						try {
							LoginNavigationPicker.setIntoUserContextAndSession(context,oldDbName);
						} catch (Exception e) {
							e.printStackTrace();
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);						
						}									
						String args[] = new String[0]; 
						String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					} catch (EpiException e) {
						e.printStackTrace();
						try {
							LoginNavigationPicker.setIntoUserContextAndSession(context,oldDbName);
						} catch (Exception e1) {
							e1.printStackTrace();
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);						
						}									
						String args[] = new String[0]; 
						String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
						throw new UserException(errorMsg,new Object[0]);
					}
				}
			}
		} catch (EpiDataException e) {
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);	
		} 
		try {
			LoginNavigationPicker.setIntoUserContextAndSession(context,oldDbName);
		} catch (Exception e) {
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);						
		}
		_log.debug("LOG_DEBUG_EXTENSION_VALIDATTEUSRSCRNLST","Exiting ValidateUserScreenList",100L);		
		return RET_CONTINUE;
		
	}	
}