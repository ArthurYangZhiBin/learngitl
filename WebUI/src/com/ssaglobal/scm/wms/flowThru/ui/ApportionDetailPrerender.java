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
package com.ssaglobal.scm.wms.flowThru.ui;

import java.util.Iterator;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.ssaglobal.scm.wms.util.FormUtil;

public class ApportionDetailPrerender extends FormExtensionBase
{
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
	{
		StateInterface state = context.getState();
		DataBean detailFormDataBean = form.getFocus();

		//Get Apportion Rule from Header
		RuntimeFormInterface headerForm = FormUtil.findForm(form, "wms_list_shell", "wm_apportionment_rules_header_view", state);
		//
		DataBean headerFocus = headerForm.getFocus();
		BioBean headerBioFocus = (BioBean) headerFocus;
		String apportionRule = (String) headerBioFocus.get("APPORTIONRULE");

		/*
		 0	None	
		 1	Ascending Order	
		 2	Descending Order
		 3	Percentage Based
		 4	Preferred Customer	
		 */
		if (form.getName().equals("wm_apportionment_rules_detail_toggle Toolbar"))
		{
			//Disable New Buttons on Toolbar based on Apportion Rule
			if (apportionRule.equalsIgnoreCase("0") || apportionRule.equalsIgnoreCase("1")
					|| apportionRule.equalsIgnoreCase("2"))
			{
				form.getFormWidgetByName("NEW").setBooleanProperty("readonly", true);

			}

		}
		else
		{

			if (apportionRule.equalsIgnoreCase("0") || apportionRule.equalsIgnoreCase("1")
					|| apportionRule.equalsIgnoreCase("2"))
			{
				for (Iterator it = form.getFormWidgets(); it.hasNext();)
				{
					RuntimeFormWidgetInterface widget = ((RuntimeFormWidgetInterface) it.next());
					if (widget.getType().equalsIgnoreCase("image button"))
					{
						widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, true);
					}

					widget.setBooleanProperty("readonly", true);
				}
				//form.setProperty("readonly", "true");
			}
			else if (apportionRule.equalsIgnoreCase("3"))
			{
				form.getFormWidgetByName("SEQUENCE").setBooleanProperty("readonly", true);
				try
				{
					if (((String) form.getFocus().getValue("ALLOWOVERSHIPMENT")).equals("1"))
					{
						form.getFormWidgetByName("MAXIMUMPEROVER").setBooleanProperty("readonly", false);
					}
					else
					{
						form.getFormWidgetByName("MAXIMUMPEROVER").setBooleanProperty("readonly", true);
						form.getFormWidgetByName("MAXIMUMPEROVER").setDisplayValue("0");
						form.getFocus().setValue("ALLOWOVERSHIPMENT", "0");
					}
				} catch (NullPointerException e)
				{
					//New Form
					form.getFormWidgetByName("MAXIMUMPEROVER").setBooleanProperty("readonly", true);
					form.getFormWidgetByName("MAXIMUMPEROVER").setDisplayValue("0");
					form.getFocus().setValue("ALLOWOVERSHIPMENT", "0");
				}

			}
			else if (apportionRule.equalsIgnoreCase("4"))
			{
				form.getFormWidgetByName("SEQUENCE").setBooleanProperty("readonly", false);
				form.getFormWidgetByName("ALLOWOVERSHIPMENT").setProperty("readonly", "true");
				form.getFormWidgetByName("MAXIMUMPEROVER").setProperty("readonly", "true");
			}
		}
		return RET_CONTINUE;
	}
}
