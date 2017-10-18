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

package com.ssaglobal.scm.wms.shared_app;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.ssaglobal.SsaAccessBase;
import com.ssaglobal.scm.wms.wms_app_security.LimitUserToCurrent;


/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class CheckAuthMechanism extends com.epiphany.shr.ui.action.ActionExtensionBase {

	
	private static final String OA_DB_AUTH_MECHANISM = "DB_Directory_Specification";
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CheckAuthMechanism.class);
	
   /**
    * The code within the execute method will be run from a UIAction specified in metadata.
    * <P>
    * @param context The ActionContext for this extension
    * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
    *
    * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
    *
    * @exception EpiException
    */
	protected int execute( ActionContext context, ActionResult result ) throws EpiException {

		_log.debug("LOG_DEBUG_EXTENSION_CHECK_AUTH_MECH","[CheckAuthMechanism]::Start", 100L);
		
		if(validate()){
			context.setNavigation("menuClickEvent646");
		}else
			context.setNavigation("menuClickEvent650");

		return super.execute( context, result );
	}
   
   /**
    * Fires in response to a UI action event, such as when a widget is clicked or
    * a value entered in a form in a modal dialog
    * Write code here if u want this to be called when the UI Action event is fired from a modal window
    * <ul>
    * <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext} exposes information about the
    * event, including the service and the user interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
    * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes information about the results
    * of the action that has occurred, and enables your extension to modify them.</li>
    * </ul>
    */
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {

		try {
			
			if(validate()){
				ctx.setNavigation("menuClickEvent646");
			}else
				ctx.setNavigation("menuClickEvent650");

		} catch(Exception e) {

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;          
		} 

		return RET_CONTINUE;
	}

    private String getAuthMechanism(){
    	return new XMLParser().getDirectoryType();
    }
    
    private boolean validate() throws UserException{
    	 if(OA_DB_AUTH_MECHANISM.equalsIgnoreCase(getAuthMechanism())){
      	    return true;
      	   //throw new UserException("WMEXP_NOT_DB_AUTH_MECHANISM", new String[0]);
         }else
        	 return false;
    }
    

}

