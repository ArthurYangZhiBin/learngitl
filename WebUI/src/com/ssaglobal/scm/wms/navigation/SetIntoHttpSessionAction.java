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
package com.ssaglobal.scm.wms.navigation;


import java.util.Locale;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.EpnyControllerState;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeMenuFormWidgetInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.DBLanguageUtil;
import com.ssaglobal.scm.wms.util.FormUtil;

public class SetIntoHttpSessionAction extends ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(SetIntoHttpSessionAction.class);
    public static String DB_CONNECTION = "dbConnectionName";
	public static String DB_USERID =	"dbUserName";
	public static String DB_PASSWORD =	"dbPassword";
	public static String DB_DATABASE = "dbDatabase";
	public static String DB_ISENTERPRISE = "dbIsEnterprise";
	public static String DB_SERVER = "dbServer";
	public static String DB_TYPE = "dbType";
	//
	public static String DB_LOCALE = "dbLocale";

	protected int execute(ActionContext context, ActionResult result){	

        EpnyControllerState state = (EpnyControllerState) context.getState();
        HttpSession session = state.getRequest().getSession();
//      SM 07-12-07 Changed findForm parameter "Service Top" to "WM Service Top" to match UI metadata adjustments for Solution Console fix
		RuntimeFormInterface form = FormUtil.findForm(state.getCurrentRuntimeForm(),"","WM Service Top",state);	
		//SM End update
		RuntimeMenuFormWidgetInterface widgetFacility = (RuntimeMenuFormWidgetInterface)form.getFormWidgetByName("FACILITYMENU");
		
		BioRef selectedFacilityRef = widgetFacility.getSelectedItem();
		String facilityValue = "";
		try {
			BioBean facilityRecord = state.getDefaultUnitOfWork().getBioBean(selectedFacilityRef);
			facilityValue = facilityRecord.getString("db_name");
		} catch (BioNotFoundException e1) {			
			e1.printStackTrace();
			return RET_CANCEL;  
		} catch (EpiDataException e1) { 
			e1.printStackTrace();
			return RET_CANCEL;  
		}
		
		String sQueryString = "(wm_pl_db.db_name ~= '" + facilityValue + "')";
		_log.debug("LOG_SYSTEM_OUT","sQueryString = "+ sQueryString,100L);
		Query DatasourceQuery = new Query("wm_pl_db",sQueryString,null);
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
		BioCollectionBean DataSource = uow.getBioCollectionBean(DatasourceQuery);
		try {
			int size = DataSource.size();
			_log.debug("LOG_SYSTEM_OUT","size = "+ size,100L);
			session.setAttribute(DB_CONNECTION, facilityValue);
			for(int i=0; i < size; i++){
				String UserId = DataSource.elementAt(i).get("db_logid").toString();
				String Password = DataSource.elementAt(i).get("db_logpass").toString();
				String DatabaseName = DataSource.elementAt(i).get("db_database").toString();
				String IsEnterprise = DataSource.elementAt(i).get("db_enterprise").toString();
				String server = DataSource.elementAt(i).get("db_server").toString();
				String serverType = DataSource.elementAt(i).get("db_dbms").toString();
				Locale locale = DBLanguageUtil.normalizeLocale(state.getUser().getLocale().getJavaLocale(), state);
				session.setAttribute(DB_USERID,UserId );
				session.setAttribute(DB_PASSWORD,Password );
				session.setAttribute(DB_DATABASE,DatabaseName);
				session.setAttribute(DB_ISENTERPRISE,IsEnterprise);
				session.setAttribute(DB_SERVER,server);
				session.setAttribute(DB_TYPE, serverType);
				session.setAttribute(DB_LOCALE, locale);
				
				_log.debug("LOG_SYSTEM_OUT","[DataSource is :" + facilityValue,100L);
				_log.debug("LOG_SYSTEM_OUT","[DatabaseName is :" + DatabaseName,100L);
				_log.debug("LOG_SYSTEM_OUT","[UserID is :" + UserId,100L);
				_log.debug("LOG_SYSTEM_OUT","[Password is :" + Password,100L);
				_log.debug("LOG_SYSTEM_OUT","[IsEnterprise is :" + IsEnterprise,100L);
				_log.debug("LOG_SYSTEM_OUT","[IsEnterprise is :" + server,100L);
				_log.debug("LOG_SYSTEM_OUT","[Type is :" + serverType,100L);
				_log.debug("LOG_SYSTEM_OUT","[Locale is :" + locale,100L);
				
			}
			return RET_CONTINUE;
		 }
		 catch(Exception e) {
	            
	            // Handle Exceptions 
	          e.printStackTrace();
	          return RET_CANCEL;          
	       } 
		} 
}