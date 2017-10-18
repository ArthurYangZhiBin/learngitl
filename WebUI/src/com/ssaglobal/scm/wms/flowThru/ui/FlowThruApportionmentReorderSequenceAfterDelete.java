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

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.epiphany.shr.data.bio.Query;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class FlowThruApportionmentReorderSequenceAfterDelete extends ActionExtensionBase
{

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
		RuntimeFormInterface toggleToolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface ftaHeader = FormUtil.findForm(toggleToolbar, "wm_list_shell_apportionment_rules",
															"wm_apportionment_rules_header_view", state);
		//retrieve Header Storer Value
		String headerStorerValue = (String) ftaHeader.getFocus().getValue("STORERKEY");
		headerStorerValue = headerStorerValue == null ? null : headerStorerValue.toUpperCase();
		//query based on Storer
		String query = "wm_apportionstrategy.STORERKEY = '" + headerStorerValue + "'";
		Query detailQuery = new Query("wm_apportionstrategy", query, "wm_apportionstrategy.SEQUENCE ASC");
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		BioCollectionBean detailCollection = uowb.getBioCollectionBean(detailQuery);
		//reorder
		for (int i = 0; i < detailCollection.size(); i++)
		{
			Object previousSequence = detailCollection.get(String.valueOf(i)).getValue("SEQUENCE");
			Object consigneeKey = detailCollection.get(String.valueOf(i)).getValue("CONSIGNEEKEY");
			consigneeKey = consigneeKey == null ? null : consigneeKey.toString().toUpperCase();
			String updateQuery = "UPDATE apportionstrategy  SET SEQUENCE = " + (i + 1) + " WHERE (((STORERKEY = '"
					+ headerStorerValue + "') AND (CONSIGNEEKEY = '" + consigneeKey + "')) AND (SEQUENCE = "
					+ previousSequence + "))";
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(updateQuery);
			
		}



		return RET_CONTINUE;
	}
}
