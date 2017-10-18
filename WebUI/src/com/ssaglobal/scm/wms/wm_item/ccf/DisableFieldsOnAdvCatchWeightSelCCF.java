/****************************************************************************
 *               Copyright (c) 1999-2003 E.piphany, Inc.                    *
 *                          ALL RIGHTS RESERVED                             *
 *                                                                          *
 *     THIS PROGRAM CONTAINS PROPRIETARY INFORMATION OF E.PIPHANY, INC.     *
 *     ----------------------------------------------------------------     *
 *                                                                          *
 * THIS PROGRAM CONTAINS THE CONFIDENTIAL AND/OR PROPRIETARY INFORMATION    *
 * OF E.PIPHANY, INC.  ANY DUPLICATION, MODIFICATION, DISTRIBUTION, PUBLIC  *
 * PERFORMANCE, OR PUBLIC DISPLAY OF THIS PROGRAM, OR ANY PORTION OR        *
 * DERIVATION THEREOF WITHOUT THE EXPRESS WRITTEN CONSENT OF                *
 * E.PIPHANY, INC. IS STRICTLY PROHIBITED.  USE OR DISCLOSURE OF THIS       *
 * PROGRAM DOES NOT CONVEY ANY RIGHTS TO REPRODUCE, DISCLOSE OR DISTRIBUTE  *
 * ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT CONTAINS IN  *
 * WHOLE OR IN PART ANY ASPECT OF THIS PROGRAM.                             *
 *                                                                          *
 ****************************************************************************
 */


package com.ssaglobal.scm.wms.wm_item.ccf;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.ui.action.ccf.*;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

/*
 * You should extend com.epiphany.shr.ui.action.ccf.JSCommandExtensionBase if your client extension is
 * not a CCFSendAllWidgetsValuesExtension extension
 */
public class DisableFieldsOnAdvCatchWeightSelCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase {

//Added for WM 9 3PL Enhancements - Catch weights tracking 
	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param RuntimeFormInterface form              The form in which the widget fired the client event that triggered the CCF event
	 * @param RuntimeFormWidgetInterface formWidget  The form widget that fired the client event that triggered the CCF event
	 * @param HashMap params                         Additional CCF event parameters, based on which client extension was called
         * 
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * 
         * @exception EpiException 
	 */
	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params)
		throws EpiException {

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
			String Storer ="";
			String Item ="";
			String interationID = formWidget.getDropdownContext().getState().getInteractionId().toString();
			DataBean focus = form.getFocus();
			if (focus instanceof BioBean) {
				focus = (BioBean) focus;
				Storer = focus.getValue("STORERKEY").toString();
				Item  =  focus.getValue("SKU").toString();
			} else if (focus instanceof QBEBioBean) {
				focus = (QBEBioBean) focus;
			}
			//	System.out.println("::::::::::::bucket value::::::"+formWidget.getDropdownContext().getState().get);
		//return super.execute( context, result );
		HttpSession advCatchWeightSess= formWidget.getDropdownContext().getState().getRequest().getSession();
		
		final String widgetName = (getParameter("WIDGETNAME") != null) ? getParameterString("WIDGETNAME")
				: params.get("formWidgetName").toString();
		//System.out.println("! on " + widgetName );

		HashMap widgetNamesAndValues = new HashMap();
		retrieveWidgetNamesAndValues(params, widgetNamesAndValues);
//		StateInterface state = context.getState();
		
		try
		{
			if((widgetNamesAndValues.get("enableadvcwgt") != null))
			{	
				Object cwFlag = widgetNamesAndValues.get("enableadvcwgt");	
				//int flag =  Integer.parseInt(cwFlag.toString().trim());
				//focus.setValue("enableadvcwgt",flag);
				
				if((isUnchecked(cwFlag))){					
					advCatchWeightSess.setAttribute("advCatchWeightSess"+Storer+Item+interationID,"disabled");
					
/*					form.getFormWidgetByName("advcwttrackby").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					form.getFormWidgetByName("stduom").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					focus.setValue("catchgrosswgt", "0");
					form.getFormWidgetByName("catchgrosswgt").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					form.getFormWidgetByName("stdgrosswgt1").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					
					focus.setValue("catchnetwgt", "0");
					form.getFormWidgetByName("catchnetwgt").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					form.getFormWidgetByName("stdnetwgt1").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					
					focus.setValue("catchtarewgt", "0");
					form.getFormWidgetByName("catchtarewgt").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					form.getFormWidgetByName("tarewgt1").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					
					String packKeyFocus = focus.getValue("PACKKEY").toString();
					String packkey=form.getFormWidgetByName("PACKKEY").getValue().toString();
					form.getFormWidgetByName("PACKKEY").setDisplayValue(packKeyFocus);
*/					
					
					
					
					setProperty(form.getFormWidgetByName("advcwttrackby"), RuntimeFormWidgetInterface.PROP_READONLY, "true"); //Not Editable
					setProperty(form.getFormWidgetByName("stduom"), RuntimeFormWidgetInterface.PROP_READONLY, "true"); //Not Editable
					
					setValue(form.getFormWidgetByName("catchgrosswgt"), new String("0")); //not Checked
					setProperty(form.getFormWidgetByName("catchgrosswgt"), RuntimeFormWidgetInterface.PROP_READONLY, "true"); //Not Editable
					setProperty(form.getFormWidgetByName("stdgrosswgt1"), RuntimeFormWidgetInterface.PROP_READONLY, "true"); //Not Editable
					setValue(form.getFormWidgetByName("catchnetwgt"), new String("0")); //not Checked
					setProperty(form.getFormWidgetByName("catchnetwgt"), RuntimeFormWidgetInterface.PROP_READONLY, "true"); //Not Editable
					setProperty(form.getFormWidgetByName("stdnetwgt1"), RuntimeFormWidgetInterface.PROP_READONLY, "true"); //Not Editable
					setValue(form.getFormWidgetByName("catchtarewgt"), new String("0")); //not Checked
					setProperty(form.getFormWidgetByName("catchtarewgt"), RuntimeFormWidgetInterface.PROP_READONLY, "true"); //Not Editable
					setProperty(form.getFormWidgetByName("tarewgt1"), RuntimeFormWidgetInterface.PROP_READONLY, "true"); //Not Editable
					setProperty(form.getFormWidgetByName("ZERODEFAULTWGTFORPICK"), RuntimeFormWidgetInterface.PROP_READONLY, "true"); //Not Editable


									
					
					
					
					
				}else{//Persistent catch weight enabled					
					advCatchWeightSess.setAttribute("advCatchWeightSess"+Storer+Item+interationID,"enabled");

/*					form.getFormWidgetByName("advcwttrackby").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					form.getFormWidgetByName("stduom").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
//					focus.setValue("catchgrosswgt", "0");
					form.getFormWidgetByName("catchgrosswgt").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					form.getFormWidgetByName("stdgrosswgt1").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
//					focus.setValue("catchnetwgt", "0");
					form.getFormWidgetByName("catchnetwgt").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					form.getFormWidgetByName("stdnetwgt1").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
//					focus.setValue("catchtarewgt", "0");
					form.getFormWidgetByName("catchtarewgt").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
					form.getFormWidgetByName("tarewgt1").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);			
*/
					
					setProperty(form.getFormWidgetByName("advcwttrackby"), RuntimeFormWidgetInterface.PROP_READONLY, "false"); //Editable
					setProperty(form.getFormWidgetByName("stduom"), RuntimeFormWidgetInterface.PROP_READONLY, "false"); //Editable
					
					setValue(form.getFormWidgetByName("catchgrosswgt"), new String("0")); //not Checked
					setProperty(form.getFormWidgetByName("catchgrosswgt"), RuntimeFormWidgetInterface.PROP_READONLY, "false"); //Editable
					setProperty(form.getFormWidgetByName("stdgrosswgt1"), RuntimeFormWidgetInterface.PROP_READONLY, "false"); //Editable
					setValue(form.getFormWidgetByName("catchnetwgt"), new String("0")); //not Checked
					setProperty(form.getFormWidgetByName("catchnetwgt"), RuntimeFormWidgetInterface.PROP_READONLY, "false"); // Editable
					setProperty(form.getFormWidgetByName("stdnetwgt1"), RuntimeFormWidgetInterface.PROP_READONLY, "false"); //Editable
					setValue(form.getFormWidgetByName("catchtarewgt"), new String("0")); //not Checked
					setProperty(form.getFormWidgetByName("catchtarewgt"), RuntimeFormWidgetInterface.PROP_READONLY, "false"); //Editable
					setProperty(form.getFormWidgetByName("tarewgt1"), RuntimeFormWidgetInterface.PROP_READONLY, "false"); //Editable
					setProperty(form.getFormWidgetByName("ZERODEFAULTWGTFORPICK"), RuntimeFormWidgetInterface.PROP_READONLY, "false"); //Editable
					
					
					
					
					
				}
				
			}	
			/*
			if ((widgetNamesAndValues.get("enableadvcwgt") != null)&& (widgetNamesAndValues.get("enableadvcwgt").toString().length()>0))
			{
				System.out.println(">>>>>>>>>>>>>>>>>>enabled>>>>>>>>>>>>>>>>>>>>>>");
				advCatchWeightSess.setAttribute("advCatchWeightSess","enabled");
			}
			else
			{	
				System.out.println(">>>>>>>>>>>>>>>>>>disabled>>>>>>>>>>>>>>>>>>>>>>");
				advCatchWeightSess.setAttribute("advCatchWeightSess","disabled");
			}
		  */	
		} catch (NullPointerException e)
		{
			System.out.println("Unable to retrieve widget value, not set, doing nothing");
			return RET_CONTINUE;
		}
		
		return RET_CONTINUE;
	
	}
	
	private void retrieveWidgetNamesAndValues(HashMap params, HashMap widgetNamesAndValues)
	{
		String[] widgetValues = (String[]) params.get("widgetValue");
		String[] widgetNames = (String[]) params.get("widgetName");
		for (int i = 0; i < widgetValues.length; i++)
		{
			//System.out.println("# " + widgetNames[i] + " " + widgetValues[i]);
			widgetNamesAndValues.put(widgetNames[i], widgetValues[i]);
		}
	}
	
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
