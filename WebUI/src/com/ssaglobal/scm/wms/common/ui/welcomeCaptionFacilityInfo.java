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
package com.ssaglobal.scm.wms.common.ui;

import java.text.MessageFormat;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.model.UserInterface;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.EpnyControllerState;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;

public class welcomeCaptionFacilityInfo extends FormWidgetExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(welcomeCaptionFacilityInfo.class);

    public welcomeCaptionFacilityInfo()
    {
    }

    protected int execute(StateInterface state, RuntimeFormWidgetInterface widget)
        throws EpiException
    {
        if(((EpnyControllerState)state).isOldRequest())
            return 0;
        UserInterface user = state.getUser();
        LocaleInterface locale = user.getLocale();
        String disconnectedUsrStr = " ";
        String strMsg = widget.getLabel("label", locale);
        String facility = getFacility(state);
        if (! facility.equalsIgnoreCase("")){
//            strMsg = strMsg + " {0} {1}";
            String arguments[] = {
            		facility, disconnectedUsrStr
            };
            MessageFormat mf = new MessageFormat(strMsg);
            mf.setLocale(locale.getJavaLocale());
            StringBuffer result = new StringBuffer();
            mf.format(arguments, result, null);
            widget.setLabel("label", result.toString());
        }else{
            widget.setLabel("label", " ");
        }
        //_log.debug("LOG_SYSTEM_OUT","\n\n\n\nFacility Str:"+result.toString()+"\n\n\n\n",100L);
        return 0;
    }

    private String getFacility(StateInterface state)
        throws EpiException
    {
    	String facilityName;
    	EpnyControllerState EpnyContState = (EpnyControllerState) state;
        HttpSession session = EpnyContState.getRequest().getSession();
		if (session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION) == null){
			facilityName= "";
		}
		else{
//7674.b			
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			BioCollectionBean listCollection = null;
			String sQueryString = "(wm_pl_db.db_name = '"+session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString()+"')";
			_log.debug("LOG_SYSTEM_OUT","sQueryString = "+ sQueryString,100L);
			Query bioQuery = new Query("wm_pl_db",sQueryString,null);
			listCollection = uowb.getBioCollectionBean(bioQuery);
			BioBean pldbBio = (BioBean)listCollection.elementAt(0);
			facilityName = pldbBio.getString("db_alias");
//7674.e
		}
        return facilityName;
    }

    public static final String DISCONNECTED_LABEL_KEY = "TXT_DISCONNECTED_USER";
    public static final String REGISTRATIONMODE = "SelfRegisterMode";
    public static final String REGISTRATION_AUTOMATIC = "automatic";
    public static final String REGISTRATION_PROMPT = "prompt";
    public static final String REGISTRATION_RESTRICTED = "restricted";
}
