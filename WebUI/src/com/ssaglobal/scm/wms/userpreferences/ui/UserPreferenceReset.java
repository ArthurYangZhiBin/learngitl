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

import java.util.Iterator;
import java.util.List;

import com.epiphany.common.shared.extension.helper.ApplicationUtil;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceManager;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.sf.util.UserDataBio;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.UserInterface;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeSlot;
import com.epiphany.shr.util.GUID;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;

public class UserPreferenceReset extends ActionExtensionBase {
    //Screen Settings Form Widget Names
//    public static final String EPNYLISTCOLORDER          ="epnylistColOrder";
//   public static final String EPNYLISTCOLWIDTHS         ="epnylistColWidths";
//    public static final String EPNYLISTFILTER            ="epnylistfilter";
    public static final String EPNYSHELLPREFERENCES      ="epnyShellPreferences";
//    public static final String ADMIN_PROFILE			 ="Administrator Tools";
    static String [] ScreenSettingsPreferences={EPNYSHELLPREFERENCES};
	protected static ILoggerCategory _log = LoggerFactory.getInstance(UserPreferenceReset.class);
    
    //HC
    protected int execute(ActionContext context, ActionResult result) throws EpiException {
        StateInterface state = context.getState();
        RuntimeFormInterface parentForm = state.getCurrentRuntimeForm();
        if (parentForm != null) {
        	parentForm = FormUtil.findForm(parentForm,"wms_list_shell","wm_ws_defaults_header_detail",context.getState());
        }
        if(parentForm == null)
        	return RET_CONTINUE;
        parentForm.getFormWidgetByName("RESETSHELLPREF").getDisplayValue();
        _log.debug("LOG_SYSTEM_OUT","DISPLAY VALUE = "+ parentForm.getFormWidgetByName("RESETSHELLPREF").getDisplayValue(),100L);
        parentForm.getFormWidgetByName("RESETSHELLPREF").getValue();
        _log.debug("LOG_SYSTEM_OUT","GET VALUE = "+ parentForm.getFormWidgetByName("RESETSHELLPREF").getValue(),100L);
        //iterate thru the form slots
        UserInterface loggedInUser = state.getUser();
        UserDataBio udb = getUser(context);
        if (parentForm.getFormWidgetByName("RESETSHELLPREF").getValue().equals("1")){
        	_log.debug("LOG_SYSTEM_OUT","Inside reseting",100L);
            reset(udb);
            udb.save();//This would persist the data.        	
        }
        return RET_CONTINUE;
    }
    //HC
    
    public static UserDataBio getUser(UIRenderContext context)
    {
        //ApplicationUtil.debug("UserPreferenceHelper.class","LOG_UserPreferenceHelper_65","Printing from getUser()");
        UserDataBio udb=null;
        String targetUser=null;
        try
        {
            if(isFormInTabGroup(context))//Administrator is viewing user preferences for another user.
            {
                targetUser=getUserInFocus(context);
                udb=UserDataBio.getUserDataBio(targetUser);
            }
            else//Logged in user is looking at his own preferences.
            {
                udb = UserDataBio.getUserDataBio();
            }


        }
        catch (EpiException e)
        {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
        return udb;
    }
    
    public static boolean isFormInTabGroup(UIRenderContext context)
    {
        boolean chk=false;
        StateInterface state=context.getState();
        RuntimeFormInterface form =state.getCurrentRuntimeForm();
        //Now go 2 levels up
        int count=0;
        while(count<2&&null!=form)
        {
            form=form.getParentForm(state);
            count++;
        }
        if(null!=form)
        {
            chk=true;
        }
        return chk;

    }
    
    private static String getUserInFocus(UIRenderContext context)
    {
        DataBean db=null;
        String targetUser=null;
        db=context.getState().getAncestorFocus(2);
        if(null!=db&&"agent_user".equals(db.getDataType()))
        {
            if(db instanceof BioBean)
            {
                BioBean bb=(BioBean)db;
                bb=(BioBean)bb.get("user_data");
                targetUser=(String)bb.get("user_name");
            }
        }
        return targetUser;
    }
    
    public void reset(UserDataBio udb) throws EpiException
    {
    	for(int i=0,count=ScreenSettingsPreferences.length;i<count;i++)
    	{
    		udb.deletePreference(ScreenSettingsPreferences[i]);
        }
    }
}
