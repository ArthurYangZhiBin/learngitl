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

package com.ssaglobal.scm.wms.wm_pack_charge.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.Hashtable;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
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

public class PackChargeLoadPackKey extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PackChargeLoadPackKey.class);

	RuntimeFormInterface form;

	DataBean formFocus;

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
		Hashtable packLookup = new Hashtable(13, 0.75f);
		packLookup.put("PACKUOM1", "CASE");
		packLookup.put("PACKUOM2", "INNERPACK");
		packLookup.put("PACKUOM3", "EACH");
		packLookup.put("PACKUOM4", "PALLET");
		packLookup.put("PACKUOM5", "CUBE");
		packLookup.put("PACKUOM6", "GROSSWGT");
		packLookup.put("PACKUOM7", "NETWGT");
		packLookup.put("PACKUOM8", "OTHERUNIT1");
		packLookup.put("PACKUOM9", "OTHERUNIT2");

		/*SELECT     PACKKEY, PACKDESCR, PACKUOM3, QTY, PACKUOM1, CASECNT, PACKUOM2, INNERPACK, PACKUOM4, PALLET, PACKUOM5, CUBE, PACKUOM6, 
		 GROSSWGT, PACKUOM7, NETWGT, PACKUOM8, OTHERUNIT1, PACKUOM9, OTHERUNIT2*/

		Hashtable qtyLookup = new Hashtable(13, 0.75f);
		qtyLookup.put("PACKUOM1", "CASECNT");
		qtyLookup.put("PACKUOM2", "INNERPACK");
		qtyLookup.put("PACKUOM3", "QTY");
		qtyLookup.put("PACKUOM4", "PALLET");
		qtyLookup.put("PACKUOM5", "CUBE");
		qtyLookup.put("PACKUOM6", "GROSSWGT");
		qtyLookup.put("PACKUOM7", "NETWGT");
		qtyLookup.put("PACKUOM8", "OTHERUNIT1");
		qtyLookup.put("PACKUOM9", "OTHERUNIT2");

		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();

		form = state.getCurrentRuntimeForm();
		formFocus = state.getFocus();

		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "PackChargeLoadPackKey" + "\n", SuggestedCategory.NONE);
		//Retrieve PackKey
		String packKey = (String) context.getSourceWidget().getValue();
		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "PackKey: " + packKey + "\n", SuggestedCategory.NONE);
		//Get PackKey from PACK Table
		Query packQry = new Query("wm_pack", "wm_pack.PACKKEY = '" + packKey + "'", null);
		BioCollectionBean results = uowb.getBioCollectionBean(packQry);
		BioBean packBean;
		if (results.size() == 1)
		{
			packBean = results.get("0");
		}
		else
		{
			return RET_CANCEL;
		}

		//Enable/Disable Widgets based on PackKey and set Value
		String packPrefix = "PACKUOM";
		for (int i = 1; i <= 9; i++)
		{

			String packUOM = packPrefix + i;
			String formPrefix = (String) packLookup.get(packUOM);
			_log.debug("LOG_DEBUG_EXTENSION", "\n\tPrefix: " + formPrefix + "\n", SuggestedCategory.NONE);

			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + packUOM + "\n", SuggestedCategory.NONE);
			Object uomValue = packBean.getValue(packUOM);
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + uomValue + "\n", SuggestedCategory.NONE);
			if (!isEmpty(uomValue))
			{
				//get quantity
				String qtyField = (String) qtyLookup.get(packUOM);
				Object packQty = packBean.getValue(qtyField);
				_log.debug("LOG_DEBUG_EXTENSION", "\n\tPack Qty: " + packQty + "\n", SuggestedCategory.NONE);
				//populate widget
				formFocus.setValue(formPrefix + "PACKUOM", uomValue); //set Localized Name using Attribute Domain for the Pack
				formFocus.setValue(formPrefix + "QUANTITY", packQty);

				//enable associated widgets in preformrender
			}
			else
			{
				//set defaults
				formFocus.setValue(formPrefix + "PACKUOM", "");
				formFocus.setValue(formPrefix + "QUANTITY", "0");

				//disable associated widgets in preformrender
			}
			//clear widgets
			clearWidgets(formPrefix);
		}

		result.setFocus(formFocus);

		return RET_CONTINUE;
	}

	protected void clearWidgets(String prefix)
	{
		//skip FROM1

		form.getFormWidgetByName(prefix + "FROM2").setDisplayValue("");
		form.getFormWidgetByName(prefix + "FROM3").setDisplayValue("");
		form.getFormWidgetByName(prefix + "TO1").setDisplayValue("");
		form.getFormWidgetByName(prefix + "TO2").setDisplayValue("");
		//skip FROM3
		for (int i = 1; i <= 3; i++)
		{
			form.getFormWidgetByName(prefix + "CHARGE" + i).setDisplayValue("");
		}
		form.getFormWidgetByName(prefix + "COST").setDisplayValue("");
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
