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

package com.ssaglobal.scm.wms.wm_cyclecount.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.epiphany.shr.ui.exceptions.FormException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class CCLoadDetailValuesFromHeaderOnNew extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{
	private static String OWNER = "STORERKEY";

	private static String ITEM = "SKU";

	private static String LOCATION = "LOC";

	private static String SYSQUANTITY = "SYSQTY";

	private static final String QTYSELECTED = "QTYSELECTED";
	/**
	 * Called in response to the pre-render event on a form. Write code to
	 * customize the properties of a form. All code that initializes the
	 * properties of a form that is being displayed to a user for the first time
	 * belong here. This is not executed even if the form is re-displayed to the
	 * end user on subsequent actions.
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
			throws EpiException
	{		
		try
		{
			// Get Handle on Form
			StateInterface state = context.getState();
			DataBean currentFormFocus = state.getFocus();
			if (currentFormFocus instanceof BioBean)
			{	
				//currentFormFocus = (BioBean) currentFormFocus;
				return RET_CONTINUE;
			}

			// Starting From			
			// Get Toggle
			RuntimeNormalFormInterface toggleForm = (RuntimeNormalFormInterface) form.getParentForm(context.getState());			
			if (toggleForm.getName().equalsIgnoreCase("wms_list_shell"))
			{				
				return RET_CONTINUE;
			}

			// Get Shell
			RuntimeNormalFormInterface shellForm = (RuntimeNormalFormInterface) toggleForm.getParentForm(context.getState());			

			// Retrieve the slot of the Header Form
			SlotInterface headerListSlot = shellForm.getSubSlot("list_slot_1");

			// Retrieve the Header Detail Form
			RuntimeFormInterface headerDetailForm = context.getState().getRuntimeForm(headerListSlot, "");

			// Get Focus of the Header Detail Form
			DataBean ccHeaderForm = headerDetailForm.getFocus();
			if (ccHeaderForm instanceof BioBean)
			{
				ccHeaderForm = (BioBean) ccHeaderForm;
			}

			// Retrieve values for Owner, Item, and Location
			String ownerValue;
			String itemValue;
			String locationValue;
			Object tempOwnerValue = null;
			Object tempItemValue = null;
			Object tempLocationValue = null;
			tempOwnerValue = ccHeaderForm.getValue(OWNER);
			tempItemValue = ccHeaderForm.getValue(ITEM);
			tempLocationValue = ccHeaderForm.getValue(LOCATION);
			if ((tempOwnerValue == null) || (tempItemValue == null) || (tempLocationValue == null))
			{				
				return RET_CONTINUE;
			}
			else
			{
				ownerValue = tempOwnerValue.toString().toUpperCase();
				itemValue = tempItemValue.toString().toUpperCase();
				locationValue = tempLocationValue.toString().toUpperCase();			
			}

			//Query SKU for Description
			String querySku = "SELECT DESCR FROM SKU WHERE SKU = '" + itemValue + "' AND STORERKEY = '" + ownerValue + "'";
			EXEDataObject resultsSku = WmsWebuiValidationSelectImpl.select(querySku);
			String description = null;
			if (resultsSku.getRowCount() == 1)
			{
				description = resultsSku.getAttribValue(1).getAsString();				

			}

			// Query LOTXLOCXID
			String queryLot = "SELECT SUM(QTY) " + "FROM LOTXLOCXID " + "WHERE (SKU = '" + itemValue + "') AND (LOC = '"
					+ locationValue + "') AND (STORERKEY = '" + ownerValue + "') ";
			EXEDataObject resultsLot = WmsWebuiValidationSelectImpl.select(queryLot);
			double sysQty = 0;
			if (resultsLot.getRowCount() == 1)
			{
				String result = resultsLot.getAttribValue(1).getAsString();
				if (!result.equalsIgnoreCase("N/A"))
				{
					sysQty = Double.parseDouble(result);					
				}
			}

			// Put Values in the Bio
			currentFormFocus.setValue(OWNER, ownerValue);
			currentFormFocus.setValue(ITEM, itemValue);
			currentFormFocus.setValue(LOCATION, locationValue);
			currentFormFocus.setValue(SYSQUANTITY, new Double(sysQty));
			currentFormFocus.setValue("ADJQTY", new Double(sysQty));
			state.getCurrentRuntimeForm().getFormWidgetByName(QTYSELECTED).setValue("0");
			// Put Value on the form
			//form.getFormWidgetByName("DESCRIPTION").setDisplayValue(description);

		} catch (Exception e)
		{

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
