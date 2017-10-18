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
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeListRowInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
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

public class CCDisplayValuesForReadonlyFields extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CCDisplayValuesForReadonlyFields.class);

	/**
	 * Called in response to the pre-render event on a form. Write code
	 * to customize the properties of a form. All code that initializes the properties of a form that is
	 * being displayed to a user for the first time belong here. This is not executed even if the form
	 * is re-displayed to the end user on subsequent actions.
	 *
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException
	{
		//display values for Sku.Price, Sku.Descr
		try
		{
			//Starting From
			//_log.debug("LOG_DEBUG_EXTENSION", "\n////- 1 " + form.getName(), SuggestedCategory.NONE);
			// Get Toggle
			RuntimeNormalFormInterface toggleForm = (RuntimeNormalFormInterface) form.getParentForm(context.getState());
			//_log.debug("LOG_DEBUG_EXTENSION", "\n////-- 2 " + toggleForm.getName(), SuggestedCategory.NONE);
			if (toggleForm.getName().equalsIgnoreCase("wms_list_shell"))
			{
				//_log.debug("LOG_DEBUG_EXTENSION", "New Header as well, doing nothing", SuggestedCategory.NONE);
				return RET_CONTINUE;
			}

			// Get Shell
			RuntimeNormalFormInterface shellForm = (RuntimeNormalFormInterface) toggleForm.getParentForm(context.getState());
			//_log.debug("LOG_DEBUG_EXTENSION", "\n////--- 3 " + shellForm.getName(), SuggestedCategory.NONE);

			// Retrieve the slot of the Header Form
			SlotInterface headerListSlot = shellForm.getSubSlot("list_slot_1");

			// Retrieve the Header Detail Form
			RuntimeFormInterface headerDetailForm = context.getState().getRuntimeForm(headerListSlot, "");
			//_log.debug("LOG_DEBUG_EXTENSION", "\n//// Name of Header Detail Form " + headerDetailForm.getName(), SuggestedCategory.NONE);

			// Get Focus of the Header Detail Form
			DataBean ccHeaderForm = headerDetailForm.getFocus();
			if (ccHeaderForm instanceof BioBean)
			{
				ccHeaderForm = (BioBean) ccHeaderForm;
			}

			// Retrieve value of Item - Header
			String itemValue;
			String ownerValue;
			Object tempItemValue = null;
			Object tempStorerValue = null;

			tempItemValue = ccHeaderForm.getValue("SKU");
			tempStorerValue = ccHeaderForm.getValue("STORERKEY");
			if ((isEmpty(tempItemValue)) || isEmpty(tempStorerValue))
			{
				_log.debug("LOG_DEBUG_EXTENSION", "/// Values not set or this is a brand new record, Doing Nothing", SuggestedCategory.NONE);
				return RET_CONTINUE;
			}
			else
			{
				itemValue = tempItemValue.toString().toUpperCase();
				ownerValue = tempStorerValue.toString().toUpperCase();
				_log.debug("LOG_DEBUG_EXTENSION", "//// Values Item: " + itemValue, SuggestedCategory.NONE);
				_log.debug("LOG_DEBUG_EXTENSION", "//// Values Owner: " + itemValue, SuggestedCategory.NONE);
			}

			String query = "SELECT DESCR, PRICE FROM SKU WHERE SKU = '" + itemValue + "' AND STORERKEY = '"
					+ ownerValue + "'";
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			String tempDescr;
			String tempPrice;
			if (results.getRowCount() == 1)
			{
				tempDescr = results.getAttribValue(1).getAsString();
				tempPrice = results.getAttribValue(2).getAsString();
				_log.debug("LOG_DEBUG_EXTENSION", "Values retrieved, " + tempDescr + " - " + tempPrice, SuggestedCategory.NONE);
				form.getFormWidgetByName("DESCRIPTION").setDisplayValue(tempDescr);
				if (!(tempPrice.equalsIgnoreCase("N/A")))
				{
					form.getFormWidgetByName("COST").setDisplayValue(tempPrice);
				}
			}

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * Called in response to the modifyListValues event on a list form. Subclasses  must override this in order
	 * to customize the display values of a list form
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form) throws EpiException
	{

		try
		{

			RuntimeListRowInterface[] listRows = form.getRuntimeListRows();

			_log.debug("LOG_DEBUG_EXTENSION", "!*() Form - " + form.getName() + " # of Rows " + listRows.length, SuggestedCategory.NONE);
			for (int i = 0; i < listRows.length; i++)
			{
				//Get List Widgets
				RuntimeFormWidgetInterface item = listRows[i].getFormWidgetByName("SKU");
				RuntimeFormWidgetInterface descr = listRows[i].getFormWidgetByName("DESCRIPTION");

				//Get Values
				String itemValue = item.getDisplayValue();

				String query = "SELECT DESCR FROM SKU WHERE SKU = '" + itemValue + "'";
				EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
				String tempDescr;
				if (results.getRowCount() == 1)
				{
					tempDescr = results.getAttribValue(1).getAsString();
					_log.debug("LOG_DEBUG_EXTENSION", "Values retrieved, " + tempDescr, SuggestedCategory.NONE);
					descr.setDisplayValue(tempDescr);
				}
			}

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	private boolean isNull(Object attributeValue) throws EpiDataException
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

	private boolean isEmpty(Object attributeValue) throws EpiDataException
	{

		if (attributeValue == null)
		{
			return true;
		}
		else if (attributeValue.toString().matches("\\s*"))
		{
			return true;
		}
		else if (attributeValue.toString().equalsIgnoreCase("null"))
		{
			return true;
		}
		else
		{
			return false;
		}

	}
}
