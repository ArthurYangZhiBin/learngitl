/**
 * ---Begin Copyright Notice---20090105T1353
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2010 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */
package com.ssaglobal.scm.wms.wm_item.ui;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;

/**
 * TODO Document CheckEnableDisableAdvCW class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class CheckEnableDisableAdvCW extends com.epiphany.shr.ui.view.customization.FormExtensionBase{
    /**
     * This class is used to Enable Disable Fields depending upon the item adv catch weight configuration 
     * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
     * and service
     * @param form the form that is about to be rendered
     */
    protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
            throws EpiException {

       try {
			boolean advCatchWeightChecked=false;
	   
            // retrieving state information
			DataBean focus = form.getFocus();
			if (focus instanceof BioBean) {
				focus = (BioBean) focus;
			} else if (focus instanceof QBEBioBean) {
				focus = (QBEBioBean) focus;				
			}
			Object cwFlag = focus.getValue("enableadvcwgt");
			
			if((isUnchecked(cwFlag))){
				advCatchWeightChecked = false;
			}else{
				advCatchWeightChecked = true;
			}
			
	  
			
		
			if(advCatchWeightChecked)
			{	
				form.getFormWidgetByName("advcwttrackby").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				form.getFormWidgetByName("stduom").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				form.getFormWidgetByName("catchgrosswgt").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				form.getFormWidgetByName("stdgrosswgt1").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				form.getFormWidgetByName("catchnetwgt").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				form.getFormWidgetByName("stdnetwgt1").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				form.getFormWidgetByName("catchtarewgt").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
				form.getFormWidgetByName("tarewgt1").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);			
				form.getFormWidgetByName("ZERODEFAULTWGTFORPICK").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);			
	
			}else{//no persistent catch weight
				form.getFormWidgetByName("advcwttrackby").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				form.getFormWidgetByName("stduom").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				form.getFormWidgetByName("catchgrosswgt").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				form.getFormWidgetByName("stdgrosswgt1").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				form.getFormWidgetByName("catchnetwgt").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				form.getFormWidgetByName("stdnetwgt1").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				form.getFormWidgetByName("catchtarewgt").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				form.getFormWidgetByName("tarewgt1").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);			
				form.getFormWidgetByName("ZERODEFAULTWGTFORPICK").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);			
			
			}	
			
			
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }
	/*
	 * This method is used to check whether check box is checked or not
	 * 
	 */       
	public boolean isUnchecked(Object attributeValue) {
		if(!isEmpty(attributeValue)){
			if (attributeValue.toString().matches("[0Nn]")) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}
	/*
	 * This method is used to check whether attribute value empty or not
	 * 
	 */
	
	protected boolean isEmpty(Object attributeValue) {
		if (attributeValue == null)	{
			return true;
		} else if (attributeValue.toString().matches("\\s*")) {
			return true;
		} else {
			return false;
		}
	}

}
