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


package com.ssaglobal.scm.wms.wm_qc_verify.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.CCFActionContext;
import com.epiphany.shr.ui.action.ccf.*;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
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
public class QCVerifyEntryCCF extends com.epiphany.shr.ui.action.ccf.SendAllWidgetsValuesExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(QCVerifyEntryCCF.class);
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
	protected int execute(RuntimeFormInterface form, RuntimeFormWidgetInterface formWidget, HashMap params) throws EpiException
	{

		CCFActionContext ctx = getCCFActionContext();
		StateInterface state = ctx.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();

		//query QCVERIFYDETAIL for description
		//Get ContainerID from Search
		HashMap allWidgets = getWidgetsValues(params);
		String containerID = null;
		containerID = returnContainerID(allWidgets);
		containerID = containerID == null ? null : containerID.toUpperCase();
		//Get Item from Entry
		String item = (String) params.get("fieldValue");
		item = item == null ? null : item.toUpperCase();
		
		Query searchQry = new Query("wm_qcverifydetail", "wm_qcverifydetail.CONTAINERID = '" + containerID
				+ "' and wm_qcverifydetail.SKU = '" + item + "'", null);
		BioCollectionBean results = uowb.getBioCollectionBean(searchQry);

		if (results.size() == 0)
		{
			//
			return RET_CANCEL;
		}
		else if (results.size() == 1)
		{
			//set description and hide lot
			setValue(form.getFormWidgetByName("DESCRIPTION"), (String) results.get("0").getValue("SKUDESCR"));
			setValue(form.getFormWidgetByName("QUANTITY"), "1");
			setProperty(form.getFormWidgetByName("LOTNUMBER"), RuntimeFormWidgetInterface.PROP_HIDDEN, "true");
		}
		else if (results.size() > 1)
		{
			//set description and show lot
			setValue(form.getFormWidgetByName("DESCRIPTION"), (String) results.get("0").getValue("SKUDESCR"));
			setValue(form.getFormWidgetByName("QUANTITY"), "1");
			setProperty(form.getFormWidgetByName("LOTNUMBER"), RuntimeFormWidgetInterface.PROP_HIDDEN, "false");
		}

		return RET_CONTINUE;

	}

	private String returnContainerID(HashMap allWidgets)
	{
		String containerID = null;
		for (Iterator it = allWidgets.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry) it.next();
			RuntimeFormWidgetInterface key = (RuntimeFormWidgetInterface) entry.getKey();
			Object value = entry.getValue();
			try
			{
				if ((key.getName().equals("CONTAINERID"))
						&& (key.getForm().getName().equals("wm_qc_verify_search_view")))
				{
					containerID = (String) value;
				}

			} catch (NullPointerException e)
			{
				_log.error("LOG_ERROR_EXTENSION", "****Exception:" + e.getMessage(), SuggestedCategory.NONE);;
			}

		}
		return containerID;
	}
}
