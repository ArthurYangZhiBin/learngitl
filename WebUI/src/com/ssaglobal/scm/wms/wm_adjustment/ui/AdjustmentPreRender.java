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
package com.ssaglobal.scm.wms.wm_adjustment.ui;

//Import 3rd party packages and classes
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Import Epiphany packages and classes
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.ssaglobal.scm.wms.uiextensions.UOMDefaultValue;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;
import com.ssaglobal.scm.wms.wm_cyclecount.ui.CalculateAdvCatchWeightsHelper;

public class AdjustmentPreRender extends FormExtensionBase{
	//Static form and tab names
	private static String SHELL = "wms_list_shell";
	private static String SLOT1 = "list_slot_1";
	
	//Static table names
	private static String TABLE_LLI = "wm_lotxlocxid";
	
	//Static attribute names
	private static String LOT = "LOT";
	private static String LOCATION = "LOC";
	private static String LPN = "ID";
	private static String UOM = "UOM";
	private static String ITEM = "SKU";
	private static String OWNER = "STORERKEY";
	private static String QTY = "QTY";
	private static String T_QTY = "TARGETQTY";
	private static String C_QTY = "CURRENTQTY";
	private static String OC_QTY = "ORIGCURRQTY";
	private static String OCH_QTY = "ORIGCHANGEQTY";
	
	//Static constants
	private static String ZERO = "0";
	private static String BLANK = "";
	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException{
		//Fill values based on previously entered data
		//Find current lot and location values
		DataBean focus = form.getFocus();
		String lot = null;
		String loc = null;
		RuntimeFormInterface headerForm = FormUtil.findForm(form, "wms_list_shell", "wm_adjustment_detail_view", context.getState());
		RuntimeFormWidgetInterface owner = headerForm.getFormWidgetByName(OWNER);
		RuntimeFormWidgetInterface skuWidget = form.getFormWidgetByName(ITEM);
		CalculateAdvCatchWeightsHelper helper = new CalculateAdvCatchWeightsHelper();

		//IF the record is unsaved, check for null values (Manual reset of Item)
		if(focus.isTempBio()){
			QBEBioBean qbe = (QBEBioBean)focus;
			Object lotObj = qbe.get(LOT);
			Object locObj = qbe.get(LOCATION);
			if(lotObj!=lot){
				lot = lotObj.toString();
			}
			if(locObj!=loc){
				loc = locObj.toString();
			}
			
			//if endtoend serial catch weight and inbound catch weight were set, then disable all weight fields
			if(!isEmpty(owner.getValue()) 
					&& !isEmpty(skuWidget.getValue())){
				boolean isEndToEndSerialInboundCatchWeight = helper.isEndToEndSerialInboundCatchWeight
													(owner.getValue().toString(), skuWidget.getValue().toString());	   
				if(isEndToEndSerialInboundCatchWeight){
					disableEnableWeightFields(form, true);
				}else{
					disableEnableWeightFields(form, false);
				}				
			}
			
			
		}else{
			//Record is saved, lot and location CANNOT be null/updated (According to business logic)
			BioBean bio = (BioBean)focus;
			lot = bio.get(LOT).toString();
			loc = bio.get(LOCATION).toString();	
			disableEnableWeightFields(form, true);
			
		}		
		String source = context.getSourceWidget().getName();

		//Skip query if lot and location are NOT null and the source widget is NOT UOM, QTY, or TARGETQTY 
		if(lot!=null && loc!=null && !source.equals(UOM) && !source.equals(QTY) && !source.equals(T_QTY)){
			String id = form.getFormWidgetByName(LPN).getDisplayValue();
			if(id==null){
				id=BLANK;
			}
			//Build query
			StateInterface state = context.getState();
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			String queryString = TABLE_LLI+"."+LOT+"='"+lot+"' and "+TABLE_LLI+"."+LOCATION+"='"+loc+"' and "+TABLE_LLI+"."+LPN+"='"+id+"'";
			Query qry = new Query(TABLE_LLI, queryString, null);
			BioCollectionBean list = uowb.getBioCollectionBean(qry);
			//If no match is found and LPN is not null, reset quantities
			if(list.size()<1 && !(id.equals(BLANK))){
				form.getFormWidgetByName(C_QTY).setValue(ZERO);
				form.getFormWidgetByName(OC_QTY).setValue(ZERO);
				form.getFormWidgetByName(OCH_QTY).setValue(ZERO);
			}else {
				//Set quantity to found value
				if(list.size()!=0){
					form.getFormWidgetByName(C_QTY).setValue(isNull(list, QTY));
					form.getFormWidgetByName(OC_QTY).setValue(isNull(list, QTY));
					if(focus.isTempBio()){
						QBEBioBean qbe = (QBEBioBean)focus;
						qbe.set(ITEM, isNull(list, ITEM));
						packAutoPopulate(state, qbe.get(ITEM).toString());
					}
				}			
			}
		}
		//Forced reset by manual change of Item
		if(lot==null || loc==null){
			form.getFormWidgetByName(C_QTY).setValue(BLANK);
			form.getFormWidgetByName(T_QTY).setValue(ZERO);
		}
		return RET_CONTINUE;
	}
	
	public String isNull(BioCollectionBean focus, String widgetName) throws EpiException{
		String result=null;
		if(result!=focus.get(ZERO).get(widgetName)){
			result=focus.get(ZERO).get(widgetName).toString();
		}
		return result;
	}

	public int packAutoPopulate(StateInterface state, String value) throws EpiException{
		RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
		RuntimeFormInterface headerForm = FormUtil.findForm(currentForm, "wms_list_shell", "wm_adjustment_detail_view", state);
		RuntimeFormWidgetInterface owner = headerForm.getFormWidgetByName(OWNER);
		BioCollectionBean list = PackAutoPopulate.findItemOwnerCombo(state, value, owner.getDisplayValue(), ITEM);
		if(list.size()>0){
			PackAutoPopulate.populatePack(state, list);
		}else{
			UOMDefaultValue.fillDropdown(state, UOM, UOMMappingUtil.PACK_STD); //AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
		}
		return RET_CONTINUE;
	}
	
	public String colonStrip(String label){
		Pattern pattern = Pattern.compile("\\:");
		Matcher matcher = pattern.matcher(label);
		return 	matcher.replaceAll(BLANK);
	}
	
	public String readLabel(RuntimeFormWidgetInterface widget){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widget.getLabel("label",locale);
	}
	
	
	private void disableEnableWeightFields(RuntimeNormalFormInterface form, boolean isDisable){
		if(isDisable){
			form.getFormWidgetByName("ACTUALGROSSWGT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("ACTUALNETWGT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("ACTUALTAREWGT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			
			form.getFormWidgetByName("ADJGROSSWGT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("ADJNETWGT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("ADJTAREWGT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		}else{
			form.getFormWidgetByName("ACTUALGROSSWGT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("ACTUALNETWGT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("ACTUALTAREWGT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			
			form.getFormWidgetByName("ADJGROSSWGT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("ADJNETWGT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("ADJTAREWGT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
		}
	}
	public boolean isEmpty(Object attributeValue) {
		if (attributeValue == null)	{
			return true;
		} else if (attributeValue.toString().matches("\\s*")) {
			return true;
		} else {
			return false;
		}
	}	

	
	
}