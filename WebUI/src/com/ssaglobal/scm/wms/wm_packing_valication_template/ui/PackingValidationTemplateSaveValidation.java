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


package com.ssaglobal.scm.wms.wm_packing_valication_template.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.ssaglobal.scm.wms.wm_table_validation.ui.FormValidation;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
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

public class PackingValidationTemplateSaveValidation extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PackingValidationTemplateSaveValidation.class);
	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @exception EpiException
	 */
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{

		StateInterface state = context.getState();

		//Detail Validation
		RuntimeFormInterface pvtDetail = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell",
															"wm_packing_validation_template_detail_view", state);
		if (!isNull(pvtDetail))
		{
			PVTDetail pvtDetailValidation = new PVTDetail(pvtDetail, state);
			pvtDetailValidation.run();
		}

		return RET_CONTINUE;
	}

	public class PVTDetail extends FormValidation
	{
		String pvtKey;
		public PVTDetail(RuntimeFormInterface f, StateInterface st)
		{
			super(f, st);
			pvtKey = (String) focus.getValue("PACKINGVALTEMPLATEKEY") == null ? null : focus.getValue("PACKINGVALTEMPLATEKEY").toString().toUpperCase();
		}

		public void run() throws DPException, UserException
		{
			if(isInsert == true)
			{
				pvtDuplication();
			}
			
		}
		
		void pvtDuplication() throws DPException, UserException
		{
			String query = "SELECT * FROM PACKINGVALIDATIONTEMPLATE WHERE PACKINGVALTEMPLATEKEY = '" + pvtKey + "'";
			_log.debug("LOG_DEBUG_EXTENSION", "///QUERY " + query, SuggestedCategory.NONE);
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if(results.getRowCount() >= 1)
			{
//				value exists, throw exception
				String[] parameters = new String[2];
				parameters[0] = retrieveLabel("PACKINGVALTEMPLATEKEY");
				parameters[1] = pvtKey;
				throw new UserException("WMEXP_DEFAULT_DUPLICATE", parameters);
			}
		}
		

	}

	boolean isNull(Object attributeValue)
	{
		if (attributeValue == null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}
