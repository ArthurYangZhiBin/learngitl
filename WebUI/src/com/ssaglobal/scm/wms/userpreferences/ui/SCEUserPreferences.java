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


import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.sf.util.UserDataBio;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.adminconsole.userpreferences.extension.ui.IUserPreferences;

public class SCEUserPreferences implements IUserPreferences
{
    static String [] RegionalPreferences={"lm_carrier"};
    public void preRender(RuntimeFormInterface form,UserDataBio udb,boolean isAdmin)  throws EpiException
    {
        RuntimeFormWidgetInterface fw=null;

        for(int i=0,count=RegionalPreferences.length;i<count;i++)
        {
            fw=form.getFormWidgetByName(RegionalPreferences[i]);
            fw.setValue(udb.getPreference(RegionalPreferences[i]));
        }
    }

    public void save(RuntimeFormInterface form,UserDataBio udb,boolean isAdmin) throws EpiException
    {
        RuntimeFormWidgetInterface fw=null;
        String val;
        for(int i=0,count=RegionalPreferences.length;i<count;i++)
        {
            fw=form.getFormWidgetByName(RegionalPreferences[i]);
            val=(String)fw.getValue();
            udb.setPreference(RegionalPreferences[i],val);
        }
    }

    public void reset(RuntimeFormInterface form,UserDataBio udb,boolean isAdmin) throws EpiException
    {
        RuntimeFormWidgetInterface fw=null;
        String val;
        for(int i=0,count=RegionalPreferences.length;i<count;i++)
        {
            fw=form.getFormWidgetByName(RegionalPreferences[i]);
            val=(String)fw.getValue();
            udb.deletePreference(RegionalPreferences[i]);
        }
    }
}