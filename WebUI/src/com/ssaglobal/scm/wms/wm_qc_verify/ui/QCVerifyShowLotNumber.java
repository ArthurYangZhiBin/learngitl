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

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
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

public class QCVerifyShowLotNumber extends com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(QCVerifyShowLotNumber.class);
	/**
	 * The code within the execute method will be run on the WidgetRender.
	 * <P>
	 * @param state The StateInterface for this extension
	 * @param widget The RuntimeFormWidgetInterface for this extension's widget
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 */
	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget)
	{
		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "QCVerifyShowLotNumber" + "\n", SuggestedCategory.NONE);;
		try
		{

			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			RuntimeFormInterface entryForm = state.getCurrentRuntimeForm();
			String item = entryForm.getFormWidgetByName("ITEM").getDisplayValue();
			if (isEmpty(item))
			{
				return RET_CONTINUE;
			}
			item = item == null ? null : item.toUpperCase();

			//Get ContainerID from Search
			RuntimeFormInterface searchForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_qc_verify_template",
																"wm_qc_verify_search_view", state);
			String containerID = searchForm.getFormWidgetByName("CONTAINERID").getDisplayValue();
			containerID = containerID == null ? null : containerID.toUpperCase();

			Query searchQry = new Query("wm_qcverifydetail", "wm_qcverifydetail.CONTAINERID = '" + containerID
					+ "' and wm_qcverifydetail.SKU = '" + item + "'", null);
			BioCollectionBean results = uowb.getBioCollectionBean(searchQry);
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + results.size() + "\n", SuggestedCategory.NONE);;
			if (results.size() == 0)
			{
				return RET_CANCEL;
			}
			else if (results.size() == 1)
			{
				//hide lot
				widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, true);
			}
			else if (results.size() > 1)
			{
				//show lot
				widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, false);
			}

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;

		}

		return RET_CONTINUE;

	}

	private boolean isEmpty(Object attributeValue)
	{

		if (attributeValue == null)
		{
			return true;
		}
		else if (attributeValue.toString().matches("\\s*"))
		{
			return true;
		}
		else
		{
			return false;
		}

	}

}
