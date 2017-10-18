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
package com.ssaglobal.scm.wms.wm_internal_transfer.ui;

//Import 3rd party packages and classes

//Import Epiphany packages and classes
import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.wm_adjustment.ui.PackAutoPopulate;
import com.ssaglobal.scm.wms.uiextensions.UOMDefaultValue;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;
import com.ssaglobal.scm.wms.wm_adjustment.ui.PackAutoPopulate;

public class PreRenderFromTOForms extends FormExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PreRenderFromTOForms.class);

	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {		
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Executing PreRenderFromTOForms",100L);
		String toFrom = getParameterString("toFrom");
		String item = toFrom+"SKU";
		String qty = toFrom+"QTY";
		String uom = toFrom+"UOM";
		String pack = toFrom+"PACKKEY";
		String owner = toFrom+"STORERKEY";
		
		StateInterface state = context.getState();
		RuntimeFormInterface shell = form.getParentForm(state);
		while(!shell.getName().equalsIgnoreCase("wms_list_shell")){
			shell = shell.getParentForm(state);
		}
		RuntimeFormInterface header = state.getRuntimeForm(shell.getSubSlot("list_slot_1"), null);
		RuntimeFormWidgetInterface ownerWidget = header.getFormWidgetByName(owner);
		DataBean focus = form.getFocus();
		if(focus.isTempBio()){
			QBEBioBean qbe = (QBEBioBean)focus;
			String itemVal = qbe.get(item)==null ? null : qbe.get(item).toString();
			setPack(state, itemVal, ownerWidget.getDisplayValue(), pack, owner, uom, item);
		}else{
			BioBean bio = (BioBean)form.getFocus();
			String itemVal = bio.get(item)==null ? null : bio.get(item).toString();
			setPack(state, itemVal, ownerWidget.getDisplayValue(), pack, owner, uom, item);

			String qtyVal = bio.getValue(qty).toString();
			String uomVal = bio.get(uom).toString();
			String packkeyVal = bio.get(pack)==null ? null : bio.get(pack).toString();
			/*  WM 9 3PL Enhancements - Catch weights -This Modification is done as part of CatchWgt's tracking for Internal
			 * Transfer requirement - Phani S Dec 04 2009
			 * The setValue is commented instead used setDisplayValue*/
			 form.getFormWidgetByName(qty).setDisplayValue(UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA, uomVal, qtyVal, packkeyVal,context.getState(),UOMMappingUtil.uowNull,true));
			//form.getFormWidgetByName(qty).setValue(UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA, uomVal, qtyVal, packkeyVal,context.getState(),UOMMappingUtil.uowNull,true));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
		}
		
		

		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER","Exiting PreRenderFromTOForms",100L);
		return RET_CONTINUE;
	}
	
	private void setPack(StateInterface state, String itemVal, String ownerVal, String pack, String owner, String uom, String item) throws EpiException{
		BioCollectionBean list = PackAutoPopulate.findItemOwnerCombo(state, itemVal, ownerVal, item);
		if(list.size()>0){
			PackAutoPopulate.populatePack(state, list, pack, owner, uom);
		}else{
			UOMDefaultValue.fillDropdown(state, uom, UOMMappingUtil.isNull(list, pack));//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
		}
	}
	

}
