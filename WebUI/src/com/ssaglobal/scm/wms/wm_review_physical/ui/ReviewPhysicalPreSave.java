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
package com.ssaglobal.scm.wms.wm_review_physical.ui;

//Import Epiphany classes
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;

public class ReviewPhysicalPreSave extends ActionExtensionBase{

	//Static form reference strings
	private static String SHELL_SLOT = "list_slot_2";

	//Static table names
	private static String OWNER_TABLE = "wm_storer";
	private static String ITEM_TABLE = "sku";
	private static String LOT_TABLE = "wm_lot_ib";
	private static String LOC_TABLE = "wm_location";
	private static String ID_TABLE = "wm_id";
	private static String PACK_TABLE = "wm_pack";
	private static String PHYSICAL_TABLE = "wm_review_physical";
	
	//Static Attribute names
	private static String TEAM = "TEAM";
	private static String OWNER = "STORERKEY";
	private static String ITEM = "SKU";
	private static String LOT = "LOT";
	private static String LOC = "LOC";
	private static String PACK = "PACKKEY";
	private static String INV_TAG = "INVENTORYTAG";
	private static String ID = "ID";
	private static String QTY = "QTY";
	private static String UOM = "UOM";
	private static String TYPE = "TYPE=1";

	//Static error messages
	private static String ERROR_MESSAGE_TEAM = "WMEXP_RP_TEAM_VALIDATION";
	private static String ERROR_MESSAGE_NULL = "WMEXP_NULL_PRIMARY_FIELD";
	private static String ERROR_MESSAGE_UNDER_ZERO = "WMEXP_NOT_GREATER_THAN_ZERO";
	//Incident2886690_Defect150416	private static String ERROR_MESSAGE_NO_SAVE = "WMEXP_NO_SAVE_INVALID_DATA";
	private static String ERROR_MESSAGE_NO_SAVE = "WXEXP_NO_SAVE_INVALID_DATA";	//Incident2886690_Defect150416
	private static String ERROR_MESSAGE_DUPLICATE = "WMEXP_RP_EXISTS";
	
	protected int execute(ActionContext context, ActionResult result) throws UserException, EpiDataException, EpiException{
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		RuntimeFormInterface shell = context.getSourceWidget().getForm().getParentForm(state);
		RuntimeFormInterface form = state.getRuntimeForm(shell.getSubSlot(SHELL_SLOT), null);
		String[] parameter = new String[1];
		BioCollectionBean list;
		String queryString;
		Query query;
		DataBean focus = form.getFocus();
		
		//Verify Team
		RuntimeFormWidgetInterface teamWidget = form.getFormWidgetByName(TEAM);
		String team = teamWidget.getDisplayValue();
		if(team!=null){
			if(!team.equals("A") && !team.equals("B")){
				throw new FormException(ERROR_MESSAGE_TEAM, null);
			}
		}else{
			parameter[0] = colonStrip(readLabel(teamWidget));
			throw new FormException(ERROR_MESSAGE_NULL, parameter);
		}
		
		//Verify Owner
		RuntimeFormWidgetInterface ownerWidget = form.getFormWidgetByName(OWNER);
		String owner = ownerWidget.getDisplayValue();
		if(owner!=null){
			//Search storer table
			queryString = OWNER_TABLE+"."+OWNER+"='"+owner+"' AND "+OWNER_TABLE+"."+TYPE;
			query = new Query(OWNER_TABLE, queryString, null);
			list = uowb.getBioCollectionBean(query);
			if(list.size()<1){
				parameter[0] = colonStrip(readLabel(ownerWidget));
				throw new FormException(ERROR_MESSAGE_NO_SAVE, parameter);
			}
		}else{
			parameter[0] = colonStrip(readLabel(ownerWidget));
			throw new FormException(ERROR_MESSAGE_NULL, parameter);
		}
		
		//Verify Item
		RuntimeFormWidgetInterface itemWidget = form.getFormWidgetByName(ITEM);
		String item = itemWidget.getDisplayValue();
		if(item!=null){
			queryString = ITEM_TABLE+"."+ITEM+"='"+item+"' AND "+ITEM_TABLE+"."+OWNER+"='"+owner+"'";
			query = new Query(ITEM_TABLE, queryString, null);
			list = uowb.getBioCollectionBean(query);
			if(list.size()<1){
				parameter[0] = colonStrip(readLabel(itemWidget));
				throw new FormException(ERROR_MESSAGE_NO_SAVE, parameter);
			}
		}else{
			parameter[0] = colonStrip(readLabel(itemWidget));
			throw new FormException(ERROR_MESSAGE_NULL, parameter);
		}
		
		//Verify Lot
		RuntimeFormWidgetInterface lotWidget = form.getFormWidgetByName(LOT);
		String lot = lotWidget.getDisplayValue();
		if(lot!=null){
			queryString = LOT_TABLE+"."+LOT+"='"+lot+"'";
			query = new Query(LOT_TABLE, queryString, null);
			list = uowb.getBioCollectionBean(query);
			if(list.size()<0){
				parameter[0] = colonStrip(readLabel(lotWidget));
				throw new FormException(ERROR_MESSAGE_NO_SAVE, parameter);
			}
		}else{
			parameter[0] = colonStrip(readLabel(lotWidget));
			throw new FormException(ERROR_MESSAGE_NULL, parameter);
		}
		
		//Verify Loc
		RuntimeFormWidgetInterface locWidget = form.getFormWidgetByName(LOC);
		String loc = locWidget.getDisplayValue();
		if(loc!=null){
			queryString = LOC_TABLE+"."+LOC+"='"+loc+"'";
			query = new Query(LOC_TABLE, queryString, null);
			list = uowb.getBioCollectionBean(query);
			if(list.size()<0){
				parameter[0] = colonStrip(readLabel(locWidget));
				throw new FormException(ERROR_MESSAGE_NO_SAVE, parameter);
			}
		}else{
			parameter[0] = colonStrip(readLabel(locWidget));
			throw new FormException(ERROR_MESSAGE_NULL, parameter);
		}
		
		//Verify ID
		RuntimeFormWidgetInterface idWidget = form.getFormWidgetByName(ID);
		String id = idWidget.getDisplayValue();
		if(id!=null){
			queryString = ID_TABLE+"."+ID+"='"+id+"'";
			query = new Query(ID_TABLE, queryString, null);
			list = uowb.getBioCollectionBean(query);
			if(list.size()<1){
				parameter[0] = colonStrip(readLabel(idWidget));
				throw new FormException(ERROR_MESSAGE_NO_SAVE, parameter);
			}
		}else{
			//Set nulls to space for save
			setSpace(idWidget, focus);
		}
		
		//Verify Pack
		RuntimeFormWidgetInterface packWidget = form.getFormWidgetByName(PACK);
		String pack = packWidget.getDisplayValue();
		if(pack!=null){
			queryString = PACK_TABLE+"."+PACK+"='"+pack+"'";
			query = new Query(PACK_TABLE, queryString, null);
			list = uowb.getBioCollectionBean(query);
			if(list.size()<1){
				parameter[0] = colonStrip(readLabel(packWidget));
				throw new FormException(ERROR_MESSAGE_NO_SAVE, parameter);
			}
		}else{
			parameter[0] = colonStrip(readLabel(packWidget));
			throw new FormException(ERROR_MESSAGE_NULL, parameter);
		}

		//Verify Qty
		RuntimeFormWidgetInterface qtyWidget = form.getFormWidgetByName(QTY);
		String qtyString = commaStrip(qtyWidget.getDisplayValue());
		if(qtyString!=null){
			double qty = Double.parseDouble(qtyString);
			if(qty<0){
				parameter[0] = colonStrip(readLabel(qtyWidget));
				throw new FormException(ERROR_MESSAGE_UNDER_ZERO, parameter);
			}
		}else{
			parameter[0] = colonStrip(readLabel(qtyWidget));
			throw new FormException(ERROR_MESSAGE_NULL, parameter);
		}
		
		//Set nulls to space for save
		RuntimeFormWidgetInterface invTagWidget = form.getFormWidgetByName(INV_TAG);
		if(invTagWidget.getDisplayValue()==null){
			setSpace(invTagWidget, focus);
		}
		
		//Verify unique primary key
		if(focus.isTempBio()){
			queryString = PHYSICAL_TABLE+"."+TEAM+"='"+team+"' AND "+PHYSICAL_TABLE+"."+OWNER+"='"+owner+"' AND "+PHYSICAL_TABLE+"."+ITEM+
				"='"+item+"' AND "+PHYSICAL_TABLE+"."+LOT+"='"+lot+"' AND "+PHYSICAL_TABLE+"."+LOC+"='"+loc+"' AND "+PHYSICAL_TABLE+"."+ID+
				"='"+get(ID, focus)+"' AND "+PHYSICAL_TABLE+"."+INV_TAG+"='"+get(INV_TAG, focus)+"'";
			query = new Query(PHYSICAL_TABLE, queryString, null);
			list = uowb.getBioCollectionBean(query);
			if(list.size()>0){
				parameter[0] = colonStrip(readLabel(teamWidget))+", "+colonStrip(readLabel(ownerWidget))+", "+
					colonStrip(readLabel(itemWidget))+", "+colonStrip(readLabel(lotWidget))+", "+colonStrip(readLabel(locWidget))+
					", "+colonStrip(readLabel(idWidget))+", "+colonStrip(readLabel(invTagWidget));
				throw new FormException(ERROR_MESSAGE_DUPLICATE, null);
			}
		}
		
		//Convert UOM if necessary
		String uom = get(UOM, focus);
//		AW 10/14/2008 start Machine#:2093019 SDIS:SCM-00000-05440
		if(!uom.equals(UOMMappingUtil.getPACKUOM3Val(pack, uowb))){
			qtyString = UOMMappingUtil.convertUOMQty(uom, UOMMappingUtil.UOM_EA, qtyString, pack, state, UOMMappingUtil.uowNull, true);//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
		//AW 10/14/2008 end Machine#:2093019 SDIS:SCM-00000-05440
		}
		if(focus.isTempBio()){
			QBEBioBean qbe = (QBEBioBean)focus;
			qbe.set(QTY, qtyString);
		}else{
			BioBean bio = (BioBean)focus;
			bio.set(QTY, qtyString);
		}

		return RET_CONTINUE;
	}

	private String get(String name, DataBean focus){
		if(focus.isTempBio()){
			QBEBioBean qbe = (QBEBioBean) focus;
			return qbe.get(name).toString();
		}else{
			BioBean bio = (BioBean) focus;
			return bio.get(name).toString();
		}
	}
	
	private void setSpace(RuntimeFormWidgetInterface widget, DataBean focus){
		if(focus.isTempBio()){
			QBEBioBean qbe = (QBEBioBean)focus;
			qbe.set(widget.getName(), " ");
		}else{
			BioBean bio = (BioBean)focus;
			bio.set(widget.getName(), " ");
		}		
	}
	
	private String colonStrip(String label){
		Pattern pattern = Pattern.compile("\\:");
		Matcher matcher = pattern.matcher(label);
		return 	matcher.replaceAll("");
	}
	
	private String readLabel(RuntimeFormWidgetInterface widget){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widget.getLabel("label",locale);
	}
	
	private String commaStrip(String number){
		Pattern pattern = Pattern.compile("\\,");
		Matcher matcher = pattern.matcher(number);
		return matcher.replaceAll("");
	}
}