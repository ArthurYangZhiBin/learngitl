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

import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.state.EpnyState;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeSlot;
import com.epiphany.shr.ui.model.UserInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.util.GUID;
import com.epiphany.shr.sf.util.UserDataBio;
import com.epiphany.shr.sf.EpnyServiceManager;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.adminconsole.userpreferences.extension.helper.UserPreferenceHelper;
import com.epiphany.shr.adminconsole.userpreferences.extension.ui.IUserPreferences;

import java.util.Iterator;

public class SCEUserPrefSave extends ActionExtensionBase
{
    private static ILoggerCategory _log = LoggerFactory.getInstance(SCEUserPrefSave.class);
    public static final String SCE_USERPREF="sce_user_pref";

    protected int execute(ActionContext context, ActionResult result) throws EpiException
    {
        try
        {
            boolean isAdmin=UserPreferenceHelper.isUserAdmin(context.getServiceManager().getUserContext());
            StateInterface state;
            RuntimeFormInterface parentForm;
            state=context.getState();
            parentForm=state.getCurrentRuntimeForm();
            parentForm=parentForm.getParentForm(state);
            //iterate thru the form slots
            String formName=parentForm.getName();
            UserInterface loggedInUser=null;
            loggedInUser=context.getState().getUser();
            UserDataBio udb=null;
            udb=UserPreferenceHelper.getUser(context);
            IUserPreferences iup=null;
            if(null!=udb)
            {
            	if(SCE_USERPREF.equals(formName))
            		iup=new SCEUserPreferences();
            	if(null!=iup)
                {
                    iup.save(parentForm,udb,UserPreferenceHelper.isUserAdmin(context.getServiceManager().getUserContext()));
                }
            	udb.save();//This would persist the data.
            }
            else
            {
                _log.error("LOG_USERPREFERENCESAVE_6","Error: targetUser is null.", SuggestedCategory.NONE);
            }


        } catch (Exception e) {
            e.printStackTrace();
            return RET_CANCEL;
            //todo add correct handling
        }
        return RET_CONTINUE;
    }
}
