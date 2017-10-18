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
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.UserUtil;


public class WSDefaultsHeaderPrerenderAction extends FormExtensionBase{	
	public static String DB_CONNECTION = "dbConnectionName";
	public static String DB_USERID =	"dbUserName";
	public static String DB_PASSWORD =	"dbPassword";
	public static String DB_DATABASE = "dbDatabase";
	public static String DB_ISENTERPRISE = "dbIsEnterprise";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WSDefaultsHeaderPrerenderAction.class);
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {
		_log.debug("LOG_DEBUG_EXTENSION_WSDEFHEADPREREN","Executing WSDefaultsHeaderPrerenderAction",100L);		
		Query loadBiosQry = new Query("wsdefaults", "wsdefaults.USERID = 'XXXXXXXXXX' ", null);		
		String uid = UserUtil.getUserId(context.getState());
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
		BioCollection defaultCollection = uow.getBioCollectionBean(loadBiosQry);
		if(defaultCollection != null && defaultCollection.size() > 0){					
			Bio bio = defaultCollection.elementAt(0);
			if(bio.get("NUMBEROFRECORDSTOKEEP") == null){
				_log.debug("LOG_DEBUG_EXTENSION_WSDEFHEADPREREN","Default Record Found But NUMBEROFRECORDSTOKEEP is null, No Action Taken...",100L);
				_log.debug("LOG_DEBUG_EXTENSION_WSDEFHEADPREREN","Exiting WSDefaultsScreenListPrerenderAction",100L);
				return RET_CONTINUE;
			}
			form.getFormWidgetByName("NUMBEROFRECORDSTOKEEP").setDisplayValue(bio.get("NUMBEROFRECORDSTOKEEP").toString());
		}
		else{
			_log.debug("LOG_DEBUG_EXTENSION_WSDEFHEADPREREN","Default Record Not Found, NUMBEROFRECORDSTOKEEP will not be populated...",100L);			
		}
		HttpSession session = context.getState().getRequest().getSession();
		String dbName = (String)session.getAttribute(DB_CONNECTION);
		try {
			setIntoUserContextAndSession(context,"enterprise");
/*			String sql = "SELECT * FROM WSDEFAULTS WHERE USERID = '"+uid+"' AND FACILITY IS NOT NULL";
			EXEDataObject enterpriseCollection = WmsWebuiValidationSelectImpl.select(sql);
			_log.debug("LOG_DEBUG_EXTENSION_WSDEFHEADPREREN","Got Enterprise Collection:"+enterpriseCollection,100L);			
			if(enterpriseCollection != null){
				_log.debug("LOG_DEBUG_EXTENSION_WSDEFHEADPREREN","Got Enterprise Collection size:"+enterpriseCollection.getCount(),100L);				
				if(enterpriseCollection.getCount() > 0){
					String facility = enterpriseCollection.getAttribValue(new TextData("FACILITY")).toString();	
					_log.debug("LOG_DEBUG_EXTENSION_WSDEFHEADPREREN","Got Facility:"+facility,100L);					
					if(facility != null && facility.length() > 0){
						form.getFocus().setValue("FACILITY",facility);
					}
				}
			}*/
			setIntoUserContextAndSession(context,dbName);
		} catch (Exception e) {			
			e.printStackTrace();
		}finally{
			try {
				setIntoUserContextAndSession(context,dbName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_WSDEFHEADPREREN","Exiting WSDefaultsScreenListPrerenderAction",100L);
		return RET_CONTINUE;
	}
	
	static public void setIntoUserContextAndSession(UIRenderContext context, String facility) throws Exception{
		HttpSession session = context.getState().getRequest().getSession();
		String sQueryString = "(wm_pl_db.db_name = '"+facility+"')";
		_log.debug("LOG_DEBUG_EXTENSION_WSDEFHEADPREREN","sQueryString = "+ sQueryString,100L);		
		Query DatasourceQuery = new Query("wm_pl_db",sQueryString,null);
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
		BioCollectionBean DataSource = uow.getBioCollectionBean(DatasourceQuery);
		
			int size = DataSource.size();
			_log.debug("LOG_DEBUG_EXTENSION_WSDEFHEADPREREN","size = "+ size,100L);			
			session.setAttribute(DB_CONNECTION, facility);
			for(int i=0; i < size; i++){
				String UserId = DataSource.elementAt(i).get("db_logid").toString();
				String Password = DataSource.elementAt(i).get("db_logpass").toString();
				String DatabaseName = DataSource.elementAt(i).get("db_database").toString();
				String IsEnterprise = DataSource.elementAt(i).get("db_enterprise").toString();
				session.setAttribute(DB_USERID,UserId );
				session.setAttribute(DB_PASSWORD,Password );
				session.setAttribute(DB_DATABASE,DatabaseName);
				session.setAttribute(DB_ISENTERPRISE,IsEnterprise);
				
				_log.debug("LOG_DEBUG_EXTENSION_WSDEFHEADPREREN","[DataSource is :" + facility,100L);
				_log.debug("LOG_DEBUG_EXTENSION_WSDEFHEADPREREN","[DatabaseName is :" + DatabaseName,100L);
				_log.debug("LOG_DEBUG_EXTENSION_WSDEFHEADPREREN","[UserID is :" + UserId,100L);
				_log.debug("LOG_DEBUG_EXTENSION_WSDEFHEADPREREN","[Password is :" + Password,100L);
				_log.debug("LOG_DEBUG_EXTENSION_WSDEFHEADPREREN","[IsEnterprise is :" + IsEnterprise,100L);				
			}			
				
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		userContext.put(SetIntoHttpSessionAction.DB_CONNECTION, session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION));
		userContext.put(SetIntoHttpSessionAction.DB_USERID, session.getAttribute(SetIntoHttpSessionAction.DB_USERID));
		userContext.put(SetIntoHttpSessionAction.DB_PASSWORD, session.getAttribute(SetIntoHttpSessionAction.DB_PASSWORD));
		userContext.put(SetIntoHttpSessionAction.DB_DATABASE, session.getAttribute(SetIntoHttpSessionAction.DB_DATABASE));
		userContext.put(SetIntoHttpSessionAction.DB_ISENTERPRISE, session.getAttribute(SetIntoHttpSessionAction.DB_ISENTERPRISE));
	}
}