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


package com.ssaglobal.scm.wms.wm_reports.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.ssaglobal.scm.wms.datalayer.*;
import com.ssaglobal.scm.wms.util.ReportUtil;
import com.ssaglobal.scm.wms.util.UserUtil;
//import com.ssaglobal.scm.wms.JFreeChartReport.*;
//import com.microsoft.jdbc.sqlserver.*;
import com.ssaglobal.SsaAccessBase;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/
    
public class autoGeneratReportURL extends com.epiphany.shr.ui.view.customization.FormExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(autoGeneratReportURL.class);
	
	private static final String PARAM_KEY_UID = "p_uid";
	private static final String PARAM_KEY_LOCALE = "p_locale";
	private static final String PARAM_KEY_OUTLOCALE = "outputLocale";
	private static final String PARAM_KEY_DBCONN = "p_conn";
	private static final String PARAM_KEY_SCHEMA = "p_SCHEMA";
	private static final String PARAM_KEY_DB = "p_DATABASE";
    /**
     * Called in response to the pre-render event on a form. Write code
     * to customize the properties of a form. All code that initializes the properties of a form that is
     * being displayed to a user for the first time belong here. This is not executed even if the form
     * is re-displayed to the end user on subsequent actions.
     *
     * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
     * and service
     * @param form the form that is about to be rendered
     */
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
	throws EpiException {


		try {
			StateInterface state = context.getState();
			BioBean reportBio = (BioBean)form.getFocus();
			String rptID = reportBio.get("RPT_ID").toString();
			_log.debug("LOG_DEBUG_EXTENSION_autoGeneratReportURL_preRenderForm", "Report ID" + rptID, SuggestedCategory.NONE);
			String report_url = ReportUtil.retrieveReportURLStart(state, rptID);
			
/*			
			String uid = UserUtil.getUserId(context.getState());
			_log.debug("LOG_SYSTEM_OUT","**************UserID = "+uid,100L);
			String userLocale = context.getState().getUser().getLocale().getJavaLocale().getLanguage().toString();
			//getting cognos server URL
			//			WebuiConfig webConfig = new WebuiPropertyMappingObject().getWebuiConfig();
			//			String cognosServerURL = webConfig.getCognosServerURL();

			SsaAccessBase appAccess = new SQLDPConnectionFactory();
			String cognosServerURL = appAccess.getValue("webUIConfig","cognosServerURL");


			StateInterface state = context.getState();

			BioBean reportBio = (BioBean)form.getFocus();
			String report_url = ReportUtil.retrieveReportURLStart(state, reportBio.get("RPT_ID").toString());
			//    	   String report_url = "http://usdadvaraveti1.ssainternal.net/cognos8/cgi-bin/cognos.cgi?b_action=xts.run&m=portal/report-viewer.xts&ui.action=run&ui.object=%2fcontent%2fpackage%5b%40name%3d%27Metadata%20for%20WM%204000%27%5d%2freport%5b%40name%3d%27Available%20Work%20Summary%20by%20Zone%27%5d";
			//    	   String report_url = "http://usdadvaraveti1.ssainternal.net/cognos8/cgi-bin/cognos.cgi?b_action=xts.run&m=portal/report-viewer.xts&ui.action=run&ui.object=%2fcontent%2fpackage%5b%40name%3d%27Metadata%20for%20WM%204000%27%5d%2freport%5b%40name%3d%27Available%20Work%20Summary%20by%20Zone%27%5d&run.prompt=false&ui.header=false&p_DATABASE=";




			String report_url = reportBio.get("RPT_URL").toString();
			report_url = cognosServerURL+report_url+"&run.prompt=false&ui.header=false&"+PARAM_KEY_DB+"=";
			HttpSession session = state.getRequest().getSession();
			Object objConnection = session.getAttribute("dbConnectionName");
			String connectionName = objConnection==null?"":(String)objConnection;
			Object objDatabaseName = session.getAttribute("dbDatabase");
			String databaseName = objDatabaseName==null?"":(String)objDatabaseName;
			report_url=report_url+databaseName;

			report_url=report_url+"&"+PARAM_KEY_SCHEMA+"=";
			Object objUserId = session.getAttribute("dbUserName");
			String userId = objUserId==null?"":(String)objUserId;
			//			report_url=report_url+userId;
			report_url=report_url+userId.toUpperCase();
			report_url += "&"+PARAM_KEY_UID+"="+uid;
			report_url += "&"+PARAM_KEY_LOCALE+"="+userLocale;
			report_url += "&"+PARAM_KEY_OUTLOCALE+"="+userLocale;
			report_url += "&"+PARAM_KEY_DBCONN+"="+connectionName;

*/
			_log.debug("LOG_SYSTEM_OUT","**********%%%%%%using new report URL= "+report_url,100L);
			RuntimeFormWidgetInterface reportFrame = form.getFormWidgetByName("cognos_view");
			reportFrame.setProperty(reportFrame.PROP_SRC,report_url);

		} catch(Exception e) {

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;          
		} 

		return RET_CONTINUE;
	}

    /**
     * Called in response to the pre-render event on a form in a modal window. Write code
     * to customize the properties of a form. This code is re-executed everytime a form is redisplayed
     * to the end user
     * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
     * and service
     * @param form the form that is about to be rendered
     */
    protected int preRenderForm(ModalUIRenderContext context, RuntimeNormalFormInterface form)
            throws EpiException {

       try {
           // Add your code here to process the event
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }

    /**
     * Called in response to the modifySubSlot event on a form. Write code
     * to change the contents of the slots in this form. This code is re-executed everytime irrespective of
     * whether the form is re-displayed to the user or not.
     * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
     * and service
     * @param form the form that is about to be rendered
     */
    protected int modifySubSlots(UIRenderContext context, RuntimeFormExtendedInterface form)
            throws EpiException {

       try {
           // Add your code here to process the event
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }


    /**
     * Called in response to the modifySubSlot event on a form in a modal window. Write code
     * to change the contents of the slots in this form. This code is re-executed everytime irrespective of
     * whether the form is re-displayed to the user or not.
     * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
     * and service
     * @param form the form that is about to be rendered
     */
    protected int modifySubSlots(ModalUIRenderContext context, RuntimeFormExtendedInterface form)
            throws EpiException {

       try {
           // Add your code here to process the event
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }

    /**
     * Called in response to the setFocusInForm event on a form. Write code
     * to change the focus of this form. This code is executed everytime irrespective of whether the form
     * is being redisplayed or not.
     * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
     * and service
     * @param form the form that is about to be rendered
     */
    protected int setFocusInForm(UIRenderContext context, RuntimeFormInterface form)
            throws EpiException {

       try {
           // Add your code here to process the event
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }

    /**
     * Called in response to the setFocusInForm event on a form in a modal window. Write code
     * to change the focus of this form. This code is executed everytime irrespective of whether the form
     * is being redisplayed or not.
     * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
     * and service
     * @param form the form that is about to be rendered
     */
    protected int setFocusInForm(ModalUIRenderContext context, RuntimeFormInterface form)
            throws EpiException {

       try {
           // Add your code here to process the event
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }

    /**
     * Called in response to the pre-render event on a list form. Write code
     * to customize the properties of a list form dynamically, change the bio collection being displayed
     * in the form or filter the bio collection
     * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
     * and service
     * @param form the form that is about to be rendered
     */
    protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form)
            throws EpiException {

       try {
           // Add your code here to process the event
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }

    /**
     * Called in response to the pre-render event on a list form in a modal dialog. Write code
     * to customize the properties of a list form dynamically, change the bio collection being displayed
     * in the form or filter the bio collection
     * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state},
     * service information and modal dialog context
     * @param form the form that is about to be rendered
     */
    protected int preRenderListForm(ModalUIRenderContext context, RuntimeListFormInterface form)
            throws EpiException {

       try {
           // Add your code here to process the event
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }


    /**
     * Called in response to the modifyListValues event on a list form. Subclasses  must override this in order
     * to customize the display values of a list form
     * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
     * and service
     * @param form the form that is about to be rendered
     */
    protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form)
            throws EpiException {

       try {
           // Add your code here to process the event
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }

    /**
     * Called in response to the modifyListValues event on a list form in a modal dialog. Subclasses  must override this in order
     * to customize the display values of a list form
     * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state},
     * service information and modal dialog context
     * @param form the form that is about to be rendered
     */
    protected int modifyListValues(ModalUIRenderContext context, RuntimeListFormInterface form)
            throws EpiException {

       try {
           // Add your code here to process the event
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }
}

