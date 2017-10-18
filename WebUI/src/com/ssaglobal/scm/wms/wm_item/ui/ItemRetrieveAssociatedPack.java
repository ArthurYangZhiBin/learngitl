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
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.uiextensions.UOMDefaultValue;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440

public class ItemRetrieveAssociatedPack extends com.epiphany.shr.ui.view.customization.FormExtensionBase {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemRetrieveAssociatedPack.class);

	private static final String PACKKEY = "PACKKEY";
	private static final String BIO = "sku";
	private static final String ATTRIBUTE = "SKU";
	/**
	 * Modification HIstory
	 * 04/14/2009	AW	Machine#:2093019 SDIS:SCM-00000-05440
	 */
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {
		try {
			StateInterface state = context.getState();
			String itemAttribute = getParameterString("ITEMATTR");
			String packAttribute = getParameterString("PACKATTR");
			String uom = getParameterString("UOM", "UOM");
			boolean overwrite = getParameterBoolean("OVERWRITEEXISTINGPACK", true);
			// Add your code here to process the event
			DataBean focus = form.getFocus();

			if (focus.getValue(packAttribute) != null) {
				_log.debug("LOG_DEBUG_EXTENSION_ItemRetrieveAssociatedPack", "PACKKEY  exists", SuggestedCategory.NONE);
				if (overwrite == false)	{
					UOMDefaultValue.fillDropdown(state, uom, UOMMappingUtil.PACK_STD); //AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
					return RET_CONTINUE;
				}
			}

			String itemValue = (String) focus.getValue(itemAttribute);
			itemValue = itemValue == null ? null : itemValue.toUpperCase();
			_log.debug("LOG_DEBUG_EXTENSION", "$%^ SKU : " + itemValue, SuggestedCategory.NONE);
			//Query BIO wm_pack with SKU itemValue
			UnitOfWorkBean uow = context.getState().getTempUnitOfWork();
			String queryStatement = BIO + "." + ATTRIBUTE + " = '" + itemValue + "'";
			_log.debug("LOG_DEBUG_EXTENSION", "////query statement " + queryStatement, SuggestedCategory.NONE);
			Query query = new Query(BIO, queryStatement, null);
			BioCollection results = uow.getBioCollectionBean(query);
			if (results.size() == 1) {
				Bio resultBio = results.elementAt(0);
				_log.debug("LOG_DEBUG_EXTENSION", "\n!@# Setting " + packAttribute + " to " + resultBio.get(PACKKEY), SuggestedCategory.NONE);
				String temp = resultBio.get("PACKKEY").toString();
				form.getFormWidgetByName(packAttribute).setDisplayValue(temp);
				form.getFocus().setValue(packAttribute, temp);  //SRG 08/03/09 Bugaware# 9310
				UOMDefaultValue.fillDropdown(state, uom, temp);//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
			} else {
				//SKU does not exist
				UOMDefaultValue.fillDropdown(state, uom, UOMMappingUtil.PACK_STD); //AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
				_log.debug("LOG_DEBUG_EXTENSION", "Item(SKU) does not exist", SuggestedCategory.NONE);
				return RET_CANCEL;
			}
			uow.clearState();
		} catch (Exception e) {
			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}
		return RET_CONTINUE;
	}
}