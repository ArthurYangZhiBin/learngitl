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
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationInsertImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.UserUtil;


public class AddUserToWSDefaults extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(AddUserToWSDefaults.class);
	protected static final String internalCaller = "internalCall";
	public static int executeExtension(ActionContext context, ActionResult result) throws UserException{
		
		_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Executing WSDefaultsQueryAction",100L);		
		StateInterface state = context.getState();
		String uid = UserUtil.getUserId(state);
		//RM Store Previous Connection Information
		String previousDBNameSession = (String)state.getRequest().getSession().getAttribute(BuiildDefaultCache.DB_CONNECTION);
		String previousDBNameContext = (String)EpnyServiceManagerServer.getInstance().getUserContext().get(BuiildDefaultCache.DB_CONNECTION);
		//check records in WSDEFAULTSSCREEN table and if there is a difference between the number of records
		//and the number of screens avaliable in the SCREENS table then repopulate the table.
		_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Checking if WSDEFAULTSCREEN and SCREENS are in sync...",100L);				
		String loadRecordsQry = "";
		Query DatasourceQuery = new Query("wm_pl_db","wm_pl_db.isActive = '1'",null);		//8050
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
		
		
		//Get list of facility datasources for iteration
		BioCollectionBean dataSources = uow.getBioCollectionBean(DatasourceQuery);
		
		DatasourceQuery = new Query("wm_pl_db","wm_pl_db.db_enterprise = 1",null);
		BioCollectionBean enterpriseDataSources = uow.getBioCollectionBean(DatasourceQuery);
		try {
			if(enterpriseDataSources == null || enterpriseDataSources.size() == 0){				
				_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Could Not Find Enterprise Data Source In pl_db table. Exiting...",100L);
				return RET_CONTINUE;
			}
		} catch (EpiDataException e1) {
			return RET_CONTINUE;
		}
		String enterpriseDSName = "";
		try {
			enterpriseDSName = (String)enterpriseDataSources.elementAt(0).get("db_name");
		} catch (EpiDataException e1) {
			return RET_CONTINUE;
		}
		if(enterpriseDSName == null || enterpriseDSName.length() == 0){
			_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Could Not Find Enterprise Data Source In pl_db table. Exiting...",100L);
			return RET_CONTINUE;
		}		
		try {
			if(dataSources == null || dataSources.size() == 0){
				return RET_CONTINUE;
			}
		} catch (EpiDataException e2) {
			e2.printStackTrace();				
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
				
		int datasourcesSize = 0;
		try {
			datasourcesSize = dataSources.size();
		} catch (EpiDataException e2) {
			e2.printStackTrace();				
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
				
		for(int i = 0; i < datasourcesSize; i++){
		//for(int i = 0; i < 1; i++){
		
			String dbName = "";
			try {
				dbName = (String)dataSources.elementAt(i).get("db_name");
			} catch (EpiDataException e2) {
				continue;
			}
			if(dbName == null || dbName.length() == 0)
				continue;
			try {
				WSDefaultsHeaderPrerenderAction.setIntoUserContextAndSession(context,dbName);
			} catch (Exception e2) {
				continue;
			}			
			if(dbName.equalsIgnoreCase("enterprise"))				
				loadRecordsQry = "SELECT * FROM WSDEFAULTS WHERE WSDEFAULTS.USERID = '"+uid+"' AND WSDEFAULTS.ISENTERPRISE = '1'";
			else
				loadRecordsQry = "SELECT * FROM WSDEFAULTS WHERE WSDEFAULTS.USERID = '"+uid+"' AND WSDEFAULTS.ISENTERPRISE = '0'";
	
			EXEDataObject defaultsCollection = null;		
			try {
				
				defaultsCollection = WmsWebuiValidationSelectImpl.select(loadRecordsQry);				
			
			} catch (DPException e2) {
				continue;
			}	
			
			
			if(defaultsCollection.getRowCount() == 0){								
								
				try
				{	
					String insertQry = "INSERT INTO WSDEFAULTS ";
					//Make the user a clone of the global default record...
					if(!dbName.equalsIgnoreCase("enterprise")){
						try {
							WSDefaultsHeaderPrerenderAction.setIntoUserContextAndSession(context,enterpriseDSName);
						} catch (Exception e) {						
							e.printStackTrace();
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Exiting WSDefaultsQueryAction",100L);							
							throw new UserException(errorMsg,new Object[0]);
						}
					}									
					String getEntDefaultRecord = "SELECT * FROM WSDEFAULTS WHERE WSDEFAULTS.USERID = '"+uid+"' AND WSDEFAULTS.ISENTERPRISE = '1'";
					EXEDataObject entDefaultRecords =  WmsWebuiValidationSelectImpl.select(getEntDefaultRecord);
					
					String facility = null;

					if(entDefaultRecords != null && entDefaultRecords.getRowCount() > 0){
						facility = entDefaultRecords.getAttribValue(new TextData("FACILITY")).toString();
					}										

					try {
						WSDefaultsHeaderPrerenderAction.setIntoUserContextAndSession(context,dbName);
					} catch (Exception e) {						
						e.printStackTrace();
						String args[] = new String[0]; 
						String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
						_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Exiting WSDefaultsQueryAction",100L);							
						throw new UserException(errorMsg,new Object[0]);
					}
					//Create a new default record		        	
					String globalDefaultQry = "SELECT * FROM WSDEFAULTS WHERE WSDEFAULTS.USERID = 'XXXXXXXXXX'";		        	
					EXEDataObject globalDefaultRecords =  WmsWebuiValidationSelectImpl.select(globalDefaultQry);					
					_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Created new default record...",100L);					
					if(globalDefaultRecords != null && globalDefaultRecords.getRowCount() > 0){
						_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Found global default record...",100L);								
						String columnClause = "USERID";
						String valueClause = "'"+uid+"'";
						columnClause += ",ISENTERPRISE";
						if(dbName.equalsIgnoreCase("enterprise"))
							valueClause += ",'1'";					
						else
							valueClause += ",'0'";		
						//Bio globalDefaultRec = globalDefaultRecords.elementAt(0);
						Object storer = globalDefaultRecords.getAttribValue(new TextData("STORERKEY"));
						if(storer != null && storer.toString().equalsIgnoreCase("n/a"))
							storer = null;
						if(storer != null){
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Setting storerkey:"+storer,100L);														
							columnClause = "STORERKEY";
							valueClause = "'"+storer+"'";
						}

						Object receiveLoc = globalDefaultRecords.getAttribValue(new TextData("RECEIVELOC"));
						if(receiveLoc != null && receiveLoc.toString().equalsIgnoreCase("n/a"))
							receiveLoc = null;
						if(receiveLoc != null){
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Setting RECEIVELOC:"+receiveLoc,100L);																				
							columnClause += ",RECEIVELOC";																					
							valueClause += ",'"+receiveLoc+"'";											
						}

						Object pack = globalDefaultRecords.getAttribValue(new TextData("PACK"));
						if(pack != null && pack.toString().equalsIgnoreCase("n/a"))
							pack = null;
						if(pack != null){
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Setting PACK:"+pack,100L);							
							columnClause += ",PACK";
							valueClause += ",'"+pack+"'";
						}

						Object pickCode = globalDefaultRecords.getAttribValue(new TextData("PICKCODE"));
						if(pickCode != null && pickCode.toString().equalsIgnoreCase("n/a"))
							pickCode = null;
						if(pickCode != null){
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Setting PICKCODE:"+pickCode,100L);							
							columnClause += ",PICKCODE";
							valueClause += ",'"+pickCode+"'";
						}

						Object startSlot = globalDefaultRecords.getAttribValue(new TextData("STARTSLOT"));
						if(startSlot != null && startSlot.toString().equalsIgnoreCase("n/a"))
							startSlot = null;
						if(startSlot != null){
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Setting STARTSLOT:"+startSlot,100L);		
							columnClause += ",STARTSLOT";
							valueClause += ","+new Integer(startSlot.toString())+"";
						}

						Object endSlot = globalDefaultRecords.getAttribValue(new TextData("ENDSLOT"));
						if(endSlot != null && endSlot.toString().equalsIgnoreCase("n/a"))
							endSlot = null;
						if(endSlot != null){
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Setting ENDSLOT:"+endSlot,100L);							
							columnClause += ",ENDSLOT";																					
							valueClause += ","+new Integer(endSlot.toString())+"";
						}					

						Object startBay = globalDefaultRecords.getAttribValue(new TextData("STARTBAY"));
						if(startBay != null && startBay.toString().equalsIgnoreCase("n/a"))
							startBay = null;
						if(startBay != null){
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Setting STARTBAY:"+startBay,100L);							
							columnClause += ",STARTBAY";																				
							valueClause += ","+new Integer(startBay.toString())+"";
						}

						Object endBay = globalDefaultRecords.getAttribValue(new TextData("ENDBAY"));
						if(endBay != null && endBay.toString().equalsIgnoreCase("n/a"))
							endBay = null;
						if(endBay != null){
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Setting ENDBAY:"+endBay,100L);							
							columnClause += ",ENDBAY";																					
							valueClause += ","+new Integer(endBay.toString())+"";
						}

						Object numRecToKeep = globalDefaultRecords.getAttribValue(new TextData("NUMBEROFRECORDSTOKEEP"));
						if(numRecToKeep != null && numRecToKeep.toString().equalsIgnoreCase("n/a"))
							numRecToKeep = null;
						if(numRecToKeep != null){
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Setting NUMBEROFRECORDSTOKEEP:"+numRecToKeep,100L);							
							columnClause += ",NUMBEROFRECORDSTOKEEP";														
							valueClause += ","+new Integer(numRecToKeep.toString())+"";
						}

						Object dynamicRecFlag = globalDefaultRecords.getAttribValue(new TextData("DYNAMICRECORDINGFLAG"));
						if(dynamicRecFlag != null && dynamicRecFlag.toString().equalsIgnoreCase("n/a"))
							dynamicRecFlag = null;
						if(dynamicRecFlag != null){
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Setting DYNAMICRECORDINGFLAG:"+dynamicRecFlag,100L);							
							columnClause += ",DYNAMICRECORDINGFLAG";
							valueClause += ","+new Integer(dynamicRecFlag.toString())+"";
						}

						Object ownerLockFlag = globalDefaultRecords.getAttribValue(new TextData("OWNERLOCKFLAG"));
						if(ownerLockFlag != null && ownerLockFlag.toString().equalsIgnoreCase("n/a"))
							ownerLockFlag = null;
						if(ownerLockFlag != null){
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Setting OWNERLOCKFLAG:"+ownerLockFlag,100L);							
							columnClause += ",OWNERLOCKFLAG";																				
							valueClause += ","+new Integer(ownerLockFlag.toString())+"";
						}

						if(facility != null && !facility.equalsIgnoreCase("n/a")){
							_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Setting facility:"+facility,100L);		
							columnClause += ",FACILITY";																					
							valueClause += ",'"+facility+"'";
						}
						insertQry += "("+columnClause+")" + " VALUES "+"("+valueClause+")";
					}						
					WmsWebuiValidationInsertImpl.insert(insertQry);										
				}
				catch (EpiException e) {
					e.printStackTrace();				
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
					_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Exiting WSDefaultsQueryAction",100L);
					throw new UserException(errorMsg,new Object[0]);
				}				
			}

		}


		// If user was assigned to a facility then resore that assignemnt
		if(previousDBNameSession != null && previousDBNameSession.length() > 0){			

			try {
				WSDefaultsHeaderPrerenderAction.setIntoUserContextAndSession(context,previousDBNameSession);
			} catch (Exception e) {						
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
				_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Exiting WSDefaultsQueryAction",100L);							
				throw new UserException(errorMsg,new Object[0]);
			}		
		}
		else{
			//RM Resetting connection info if the user does not have a default database
			if(previousDBNameContext != null && previousDBNameContext.length() > 0) {
				try {
					WSDefaultsHeaderPrerenderAction.setIntoUserContextAndSession(context,previousDBNameContext);
				} catch (Exception e) {						
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
					_log.debug("LOG_DEBUG_EXTENSION_WSDEFQRY","Exiting WSDefaultsQueryAction",100L);							
					throw new UserException(errorMsg,new Object[0]);
				}	
			}
			context.getState().getRequest().getSession().removeAttribute(SetIntoHttpSessionAction.DB_CONNECTION.toString());
		}
		return RET_CONTINUE;
	}
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		
		return AddUserToWSDefaults.executeExtension(context,result);
		
	}	
		
}