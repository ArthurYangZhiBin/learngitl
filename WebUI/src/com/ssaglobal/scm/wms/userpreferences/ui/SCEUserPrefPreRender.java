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
package com.ssaglobal.scm.wms.userpreferences.ui;

import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.util.config.ConfigService;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.UserInterface;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.sf.util.UserDataBio;
import com.epiphany.shr.adminconsole.userpreferences.extension.helper.UserPreferenceHelper;
import com.epiphany.shr.adminconsole.userpreferences.extension.helper.IUserPreferenceTemplateConstants;
import com.epiphany.shr.adminconsole.userpreferences.extension.ui.IUserPreferences;

public class SCEUserPrefPreRender  extends FormExtensionBase
{
    private static ILoggerCategory _log = LoggerFactory.getInstance(SCEUserPrefPreRender.class);
    public static final String SCE_USERPREF="sce_user_pref";
    protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException, FormException
    {
        boolean isAdmin=UserPreferenceHelper.isUserAdmin(context.getServiceManager().getUserContext());
        String form_name=form.getName();
        UserInterface loggedInUser=null;
        loggedInUser=context.getState().getUser();
        UserDataBio udb=null;
        udb= getUser(context);
        IUserPreferences iup=null;
        if(null!=udb)
        {
        	if(SCE_USERPREF.equals(form_name))
        		iup=new SCEUserPreferences();
        }
        else
        {
            _log.error("LOG_USERPREFERENCEPRERENDER_5","Error: targetUser is null.", SuggestedCategory.NONE);
        }
        if(null!=iup)
            iup.preRender(form,udb,isAdmin);

        return RET_CONTINUE;
    }
    public static UserDataBio getUser(UIRenderContext context)
    {
        UserDataBio udb=null;
        String targetUser=null;
        try
        {
        	targetUser=getUserInFocus(context);
            udb=UserDataBio.getUserDataBio(targetUser);
        }
        catch (EpiException e)
        {
            _log.error("EXP_USERPREFERENCEHELPER_GETUSER_ERROR", "Exception: {0}", SuggestedCategory.NONE, e);
        }
        return udb;
    }
    
    private static String getUserInFocus(UIRenderContext context)
    {
        DataBean db=null;
        String targetUser=null;
        db = context.getState().getAncestorFocus(1);
        if (db == null) {
            db = context.getState().getAncestorFocus(2);
            if (db == null) {
                db = context.getState().getAncestorFocus(3);            
                if (db == null) 
                    return null;
            }	
        }

        if ("agent_user".equals(db.getDataType()))
        {
            if(db instanceof BioBean)
            {
                BioBean bb=(BioBean)db;
                //targetUser=(String)bb.get("agent_name");
                bb=(BioBean)bb.get("user_data");
                targetUser=(String)bb.get("user_name");
            }
        }
        else if ("user_data".equals(db.getDataType()))
        {
            if(db instanceof BioBean)
            {
                BioBean bb=(BioBean)db;
                bb = (BioBean) bb.get("e_user");
                targetUser=(String)bb.get("sso_name");
            }
        }
        return targetUser;
    }
    
}
