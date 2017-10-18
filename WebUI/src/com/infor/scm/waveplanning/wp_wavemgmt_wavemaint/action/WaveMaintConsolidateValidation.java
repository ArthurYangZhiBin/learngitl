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

package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.infor.scm.waveplanning.common.util.BioAttributeUtil;
import com.infor.scm.waveplanning.common.util.NSQLConfigUtil;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class WaveMaintConsolidateValidation extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WaveMaintConsolidateValidation.class);

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
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return super.execute(context, result);
	}

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked or
	 * a value entered in a form in a modal dialog
	 * Write code here if u want this to be called when the UI Action event is fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext} exposes information about the
	 * event, including the service and the user interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes information about the results
	 * of the action that has occurred, and enables your extension to modify them.</li>
	 * </ul>
	 */
	@Override
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
	{

		try
		{
			//Ensure a record is selected
			RuntimeListFormInterface consolidatedList = (RuntimeListFormInterface) ctx.getModalBodyForm(0);
			ArrayList selectedItems = consolidatedList.getSelectedItems();
			if (selectedItems == null || selectedItems.size() == 0)
			{
				_log.error("LOG_ERROR_EXTENSION_WaveMaintConsolidateValidation_execute", "No Sku's Selected",
						SuggestedCategory.NONE);
				throw new UserException("WPEXP_WAVE_CONSOLIDATE_SELECTSKU", new Object[] {});
			}

			BioCollectionBean allSkus = (BioCollectionBean) consolidatedList.getFocus();
			ArrayList locs = new ArrayList(selectedItems.size());

			//Overpick Change - Remove validation for requiring different SpeedLine Locations
			//9429. Speed pick location assignment - need to allow editing
			//toggle behaviour depending on flag
			NSQLConfigUtil commingleSpeedLineLoc = new NSQLConfigUtil(ctx.getState(), "COMMINGLESPEEDLINELOC");
			if (commingleSpeedLineLoc.isOff()) {
				//new functionality is disabled so location is checked
				for (int i = 0; i < allSkus.size(); i++) {
					final BioBean sku = allSkus.get("" + i);
					final String locValue = BioAttributeUtil.getString((DataBean) sku, "CONSOLLOC");
					if (locValue != null && !locValue.matches("\\s*")) {
						if (!locs.contains(locValue)) {
							locs.add(locValue);
						} else {
							_log.error("LOG_ERROR_EXTENSION_WaveMaintConsolidateValidation_execute",
									"Duplicate speed location assigned " + locValue, SuggestedCategory.NONE);
							throw new UserException("WPEXP_WAVE_CONSOLIDATE_SAMELOC", new Object[] { locValue });
						}
					}
				}
			}


			//ArrayList locs = new ArrayList(selectedItems.size());
			for (int i = 0; i < selectedItems.size(); i++)
			{
				final BioBean row = (BioBean) selectedItems.get(i);
				final String locValue = BioAttributeUtil.getString((DataBean) row, "CONSOLLOC");
				//if a record is selected, make sure the consolloc is not null
				if (locValue == null || locValue.matches("\\s*"))
				{
					_log.error("LOG_ERROR_EXTENSION_WaveMaintConsolidateValidation_execute",
							"No location selected for " + row.getValue("SKU"), SuggestedCategory.NONE);
					throw new UserException("WPEXP_WAVE_CONSOLIDATE_SELECTLOC", new Object[] { row.getValue("SKU") });
				}

				//ensure that the consolloc is not duplicated in the list, each item's consolloc has to be unique
//				if (!locs.contains(locValue))
//				{
//					locs.add(locValue);
//				}
//				else
//				{
//					_log.error("LOG_ERROR_EXTENSION_WaveMaintConsolidateValidation_execute",
//							"Duplicate speed location assigned " + locValue, SuggestedCategory.NONE);
//					throw new UserException("WPEXP_WAVE_CONSOLIDATE_SAMELOC", new Object[] { locValue });
//				}
			}
			
			
			
			
			
			

		} catch (RuntimeException e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}
		
		
		
		
		
		

		return RET_CONTINUE;
	}
}
