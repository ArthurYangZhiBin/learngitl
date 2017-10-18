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

package com.ssaglobal.scm.wms.wm_item.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeListRowInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.ssaglobal.scm.wms.common.ui.UOMMapping;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ItemUOMMappingPreRender extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{

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
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemUOMMappingPreRender.class);

	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
			throws EpiException
	{
		_log.debug("LOG_SYSTEM_OUT","\n*\n*\n*\n*\n*!@#Start of ItemUOMMappingPreRender",100L);
		DataBean currentFormFocus = form.getFocus();

		try
		{

			String tempUOM = currentFormFocus.getValue("REPLENISHMENTUOM").toString();
			_log.debug("LOG_SYSTEM_OUT","REPLENISHMENTUOM " + tempUOM,100L);
			int uom;
			try
			{
				uom = Integer.parseInt(tempUOM);
			} catch (NumberFormatException e)
			{
				_log.debug("LOG_SYSTEM_OUT","!@# REPLENISHMENTUOM is not a #",100L);
				return RET_CONTINUE;
			}
			_log.debug("LOG_SYSTEM_OUT","UOM " + uom,100L);
			switch (uom)
			{
			case UOMMapping.PL:
				_log.debug("LOG_SYSTEM_OUT","Setting to PL",100L);
				currentFormFocus.setValue("REPLENISHMENTUOM", "PL");
				break;
			case UOMMapping.CS:
				_log.debug("LOG_SYSTEM_OUT","Setting to CS",100L);
				currentFormFocus.setValue("REPLENISHMENTUOM", "CS");
				break;
			case UOMMapping.EA:
				_log.debug("LOG_SYSTEM_OUT","Setting to EA",100L);
				currentFormFocus.setValue("REPLENISHMENTUOM", "EA");
				break;
			default:
				//
				break;
			}
			


		} catch (NullPointerException e)
		{

			// Handle Exceptions 
			//e.printStackTrace();
			_log.debug("LOG_SYSTEM_OUT","New Record, doing nothing",100L);
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * Called in response to the pre-render event on a list form. Write code
	 * to customize the properties of a list form dynamically, change the bio collection being displayed
	 * in the form or filter the bio collection
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form)
			throws EpiException
	{

		_log.debug("LOG_SYSTEM_OUT","\n*\n*\n*\n*\n*!@#Start of ItemUOMMappingPreRender",100L);
		try
		{
			BioCollectionBean listFocus = (BioCollectionBean) form.getFocus();

			_log.debug("LOG_SYSTEM_OUT","!*() Form - " + form.getName() + " # of Rows " + listFocus.size(),100L);
			//Iterate through List, changing UOM values (1,2,6) to their counterparts (PL,CS,EA)
			for (int i = 0; i < listFocus.size(); i++)
			{
				Bio selectedRow = listFocus.elementAt(i);
				BioBean selectedBean = listFocus.get(String.valueOf(i));
				//Get Value
				String tempUOM = selectedBean.getValue("REPLENISHMENTUOM").toString();
				_log.debug("LOG_SYSTEM_OUT","REPLENISHMENTUOM " + tempUOM,100L);
				int uom;
				try
				{
					uom = Integer.parseInt(tempUOM);
				} catch (NumberFormatException e)
				{
					_log.debug("LOG_SYSTEM_OUT","!@# REPLENISHMENTUOM is not a #",100L);
					return RET_CONTINUE;
				}
				_log.debug("LOG_SYSTEM_OUT","UOM " + uom,100L);
				switch (uom)
				{
				case UOMMapping.PL:
					_log.debug("LOG_SYSTEM_OUT","Setting to PL",100L);
					selectedRow.set("REPLENISHMENTUOM", "PL");
					selectedBean.set("REPLENISHMENTUOM", "PL");
					break;
				case UOMMapping.CS:
					_log.debug("LOG_SYSTEM_OUT","Setting to CS",100L);
					selectedRow.set("REPLENISHMENTUOM", "CS");
					selectedBean.set("REPLENISHMENTUOM", "CS");
					break;
				case UOMMapping.EA:
					_log.debug("LOG_SYSTEM_OUT","Setting to EA",100L);
					selectedRow.set("REPLENISHMENTUOM", "EA");
					selectedBean.set("REPLENISHMENTUOM", "EA");
					break;
				default:
					//
					break;
				}
			}

			form.setFocus(listFocus);

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
	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form)
			throws EpiException
	{
		_log.debug("LOG_SYSTEM_OUT","\n\n\n\n!@#Start of ItemUOMMappingPreRender",100L);
		try
		{
			BioCollectionBean listFocus = (BioCollectionBean) form.getFocus();

			_log.debug("LOG_SYSTEM_OUT","!*() Form - " + form.getName() + " # of Rows " + listFocus.size(),100L);
			//Iterate through List, changing UOM values (1,2,6) to their counterparts (PL,CS,EA)
			for (int i = 0; i < listFocus.size(); i++)
			{
				Bio selectedRow = listFocus.elementAt(i);
				//Get Value
				String tempUOM = selectedRow.get("REPLENISHMENTUOM").toString();
				_log.debug("LOG_SYSTEM_OUT","REPLENISHMENTUOM " + tempUOM,100L);
				int uom;
				try
				{
					uom = Integer.parseInt(tempUOM);
				} catch (NumberFormatException e)
				{
					_log.debug("LOG_SYSTEM_OUT","!@# REPLENISHMENTUOM is not a #",100L);
					return RET_CONTINUE;
				}
				_log.debug("LOG_SYSTEM_OUT","UOM " + uom,100L);
				switch (uom)
				{
				case UOMMapping.PL:
					_log.debug("LOG_SYSTEM_OUT","Setting to PL",100L);
					selectedRow.set("REPLENISHMENTUOM", "PL");
					break;
				case UOMMapping.CS:
					_log.debug("LOG_SYSTEM_OUT","Setting to CS",100L);
					selectedRow.set("REPLENISHMENTUOM", "CS");
					break;
				case UOMMapping.EA:
					_log.debug("LOG_SYSTEM_OUT","Setting to EA",100L);
					selectedRow.set("REPLENISHMENTUOM", "EA");
					break;
				default:
					//
					break;
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

}
