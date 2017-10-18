 /******************************************************
 *
 *                       NOTICE
 *
 *   THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS
 *   CONFIDENTIAL INFORMATION OF INFOR AND SHALL NOT
 *   BE DISCLOSED WITHOUT PRIOR WRITTEN PERMISSION.
 *   LICENSED CUSTOMERS MAY COPY AND ADAPT THIS
 *   SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH
 *   THE TERMS OF THEIR SOFTWARE LICENSE AGREEMENT.
 *   ALL OTHER RIGHTS RESERVED.
 *
 *   COPYRIGHT (c) 2008 INFOR. ALL RIGHTS RESERVED.
 *   THE WORD AND DESIGN MARKS SET FORTH HEREIN ARE
 *   TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 *   AND/OR RELATED AFFILIATES AND SUBSIDIARIES. ALL
 *   RIGHTS RESERVED. ALL OTHER TRADEMARKS LISTED
 *   HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE
 *   OWNERS. WWW.INFOR.COM.
 *
 ******************************************************/
 


package com.ssaglobal.scm.wms.wm_cyclecount.ccf;

// 16/Dec/2009: New class added for 3PL Enhancements Catch Weight Changes - Seshu
// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.HashMap;
import java.util.Iterator;

import com.agileitp.forte.framework.DataValue;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.ccf.*;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

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
public class CCDisableEnableWeightsOnSkuChangeCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase {


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
		/*
		 * 16/Dec/2009: 3PL Enhancements Catch Weight Changes - Seshu
		 */	
		RuntimeFormInterface detailForm=null;		
		
		if(form !=null && form.getName().equalsIgnoreCase("wm_cyclecount_detail_header_view"))
		{
			RuntimeFormInterface form1=form.getParentForm(state);
			SlotInterface headerSlot = form1.getSubSlot("list_slot_2");			
			RuntimeFormInterface toggleForm = state.getRuntimeForm(headerSlot, null);
			
			if(toggleForm.getName().equalsIgnoreCase("wm_cyclecount_detail_toggle"))
			{
				SlotInterface toggleSlot = toggleForm.getSubSlot("wm_cyclecount_detail_toggle"); 
				RuntimeFormInterface detailTab = state.getRuntimeForm(toggleSlot, "wm_cyclecount_detail_toggle_tab"); 		
				
				for(Iterator itr = detailTab.getSubSlotsIterator(); itr.hasNext();){
					SlotInterface slot =(SlotInterface)itr.next();
					RuntimeFormInterface  form2 = state.getRuntimeForm(slot, null);				
					
					if(form2.getName().equalsIgnoreCase("wm_cyclecount_detail_view")){
						detailForm = form2;					
						break;
					}				
				}
			}
			else
				detailForm=toggleForm;
			
			if (detailForm==null)
				return RET_CONTINUE;		
		}
		else
			detailForm=form;			
		
		String sku=null;
		String owner=null;
		
		HashMap widgetNamesAndValues = new HashMap();
		String[] widgetValues = (String[]) params.get("widgetValue");
		String[] widgetNames = (String[]) params.get("widgetName");

		for (int i = 0; i < widgetValues.length; i++)
			widgetNamesAndValues.put(widgetNames[i], widgetValues[i]);

		owner=widgetNamesAndValues.get("STORERKEY").toString();			
		sku = params.get("fieldValue") == null ? null
				: params.get("fieldValue").toString().toUpperCase();		
		
		String query = "SELECT IBSUMCWFLG, CATCHGROSSWGT, CATCHNETWGT, CATCHTAREWGT, " +
				" TAREWGT1, STDNETWGT1, STDGROSSWGT1 FROM SKU " +
				"WHERE STORERKEY = '" + owner + "' and SKU = '" + sku + "'";		
		
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		DataValue val=results.getAttribValue(new TextData("IBSUMCWFLG"));
		
		if(detailForm !=null && detailForm.getName().equalsIgnoreCase("wm_cyclecount_detail_view"))
		{			
			if(val==null || val.toString().equalsIgnoreCase("0"))
			{				
				setProperty(detailForm.getFormWidgetByName("ACTUALGROSSWGT"),RuntimeFormWidgetInterface.PROP_HIDDEN, "true");
				setProperty(detailForm.getFormWidgetByName("ACTUALNETWGT"),RuntimeFormWidgetInterface.PROP_HIDDEN, "true");
				setProperty(detailForm.getFormWidgetByName("ACTUALTAREWGT"),RuntimeFormWidgetInterface.PROP_HIDDEN, "true");
				setProperty(detailForm.getFormWidgetByName("ADJGROSSWGT"),RuntimeFormWidgetInterface.PROP_HIDDEN, "true");
				setProperty(detailForm.getFormWidgetByName("ADJNETWGT"),RuntimeFormWidgetInterface.PROP_HIDDEN, "true");
				setProperty(detailForm.getFormWidgetByName("ADJTAREWGT"),RuntimeFormWidgetInterface.PROP_HIDDEN, "true");				
			}
			else
			{
				setProperty(detailForm.getFormWidgetByName("ACTUALGROSSWGT"),RuntimeFormWidgetInterface.PROP_HIDDEN, "false");
				setProperty(detailForm.getFormWidgetByName("ACTUALNETWGT"),RuntimeFormWidgetInterface.PROP_HIDDEN, "false");
				setProperty(detailForm.getFormWidgetByName("ACTUALTAREWGT"),RuntimeFormWidgetInterface.PROP_HIDDEN, "false");
				setProperty(detailForm.getFormWidgetByName("ADJGROSSWGT"),RuntimeFormWidgetInterface.PROP_HIDDEN, "false");
				setProperty(detailForm.getFormWidgetByName("ADJNETWGT"),RuntimeFormWidgetInterface.PROP_HIDDEN, "false");
				setProperty(detailForm.getFormWidgetByName("ADJTAREWGT"),RuntimeFormWidgetInterface.PROP_HIDDEN, "false");			
			}
			
		}
		
		return RET_CONTINUE;
	
	}
}
