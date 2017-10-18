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

package com.infor.scm.waveplanning.wp_wavemgmt_confirmwave.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationDeleteImpl;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class ConfirmWaveDeleteWave extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ConfirmWaveDeleteWave.class);


	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION_ConfirmWaveDeleteWave", "Entering ConfirmWaveDeleteWave",
				SuggestedCategory.NONE);
		StateInterface state = context.getState();
		RuntimeFormInterface currentRuntimeForm = state.getCurrentRuntimeForm();
		RuntimeFormInterface parentForm = currentRuntimeForm.getParentForm(state);

		if (parentForm.isListForm())
		{
			if (((RuntimeListFormInterface) parentForm).getAllSelectedItems() == null)
			{
				_log.error("LOG_ERROR_EXTENSION_ConfirmWaveConfirmWave_execute", "No Waves Selected",
						SuggestedCategory.NONE);
				throw new UserException("WPEXP_NO_WAVE_SELECTED", new Object[] {});
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION_ConfirmWaveDeleteWave", "Calling from List Form "
						+ parentForm.getName(), SuggestedCategory.NONE);
				ArrayList allSelectedItems = ((RuntimeListFormInterface) parentForm).getAllSelectedItems();
				BioBean bioBean;
				for (Iterator it = allSelectedItems.iterator(); it.hasNext();)
				{
					bioBean = (BioBean)it.next();
					String waveKey = bioBean.getString("WAVEKEY");
					WmsWebuiValidationDeleteImpl.delete("DELETE FROM WAVE WHERE WAVEKEY='"+waveKey+"'");
				}
				((RuntimeListFormInterface) parentForm).setSelectedItems(null);
			}
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION_ConfirmWaveDeleteWave", "Calling from Normal Form "
					+ currentRuntimeForm.getName(), SuggestedCategory.NONE);
			DataBean focus = currentRuntimeForm.getFocus();
			BioBean bioBean = (BioBean)focus;
			String waveKey = bioBean.getString("WAVEKEY");
			WmsWebuiValidationDeleteImpl.delete("DELETE FROM WAVE WHERE WAVEKEY='"+waveKey+"'");
		}
		_log.debug("LOG_DEBUG_EXTENSION_ConfirmWaveDeleteWave", "Leaving ConfirmWaveDeleteWave",
						SuggestedCategory.NONE);
		return RET_CONTINUE;
	}


}
