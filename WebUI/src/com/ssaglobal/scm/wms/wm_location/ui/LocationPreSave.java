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
package com.ssaglobal.scm.wms.wm_location.ui;

//Import 3rd party packages and classes
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.BioAttributeTypes;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.LocaleUtil;

public class LocationPreSave extends ActionExtensionBase{
	@Override
	protected int execute(ActionContext context, ActionResult result)throws UserException, EpiException{
		StateInterface state = context.getState();
		QBEBioBean qbe = null;
		String errorText = "", table = "wm_location", widget = "LOC";
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		String[] parameter = new String[1];
		
		//Verify number values are of correct type and positive
		RuntimeFormWidgetInterface[] widgets = new RuntimeFormWidgetInterface[9];
		RuntimeFormInterface form = state.getRuntimeForm(state.getCurrentRuntimeForm().getParentForm(state).getSubSlot("list_slot_2"), null);
		//merged from delta
		RuntimeFormInterface dimensionsForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "", "wm_location_dimensions", state);
		RuntimeFormInterface positionForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "", "wm_location_position", state);
		RuntimeFormInterface capacityForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "", "wm_location_capacity", state);
		if(form.getName().equals("Blank") || dimensionsForm == null || positionForm == null || capacityForm == null){
			throw new FormException("WMEXP_NO_SAVE_AVAILABLE", null);
		}	
		//end of merge
		
		widgets[0] = form.getFormWidgetByName("LOCLEVEL");
		widgets[1] = dimensionsForm.getFormWidgetByName("LENGTH");
		widgets[2] = dimensionsForm.getFormWidgetByName("WIDTH");
		widgets[3] = dimensionsForm.getFormWidgetByName("HEIGHT");
		widgets[4] = capacityForm.getFormWidgetByName("CUBICCAPACITY");
		widgets[5] = capacityForm.getFormWidgetByName("WEIGHTCAPACITY");
		widgets[6] = capacityForm.getFormWidgetByName("StackLimit");
		widgets[7] = capacityForm.getFormWidgetByName("FootPrint");
		widgets[8] = form.getFormWidgetByName("INTERLEAVINGSEQUENCE");
		
		for(int i=0; i<widgets.length; i++){
			errorText = greaterThanZero(widgets[i], errorText);
		}
		
		//produce error for invalid numeric values
		if(!(errorText.equals(""))){
			parameter[0]=errorText;
			throw new FormException("WMEXP_LESS_THAN_ZERO", parameter);
		}
		
		//ensure unique primary key value
		DataBean focus = form.getFocus();
		if(focus.isTempBio()){
			RuntimeFormWidgetInterface locWidget = form.getFormWidgetByName(widget);
			String queryString = table+"."+widget+"= '"+locWidget.getDisplayValue()+"'";
			Query qry = new Query(table, queryString, null);
			BioCollectionBean list = uow.getBioCollectionBean(qry);
			if(list!=null){
				if(list.size()!=0){
					parameter[0]=colonStrip(readLabel(locWidget));
					throw new FormException("WMEXP_CCPS_EXISTS", parameter);
				}
			}
		}
		
		//If location type equals speed-pick or sort and LoseID = 0 block save and display LoseID is required
		//Overpick Change RM 7/16/2009
		//remove speed-pick from validation
		RuntimeFormWidgetInterface locTypeWidget = form.getFormWidgetByName("LOCATIONTYPE");
		String loseID = form.getFormWidgetByName("LOSEID").getDisplayValue();
		String locType = locTypeWidget.getDropdownContents().getValue(locTypeWidget.getDropdownContext(), locTypeWidget.getDisplayValue());
		//		if((locType.equals("SPEED-PICK") || locType.equals("SORT")) && loseID.equals("0")){
		if ((locType.equals("SORT")) && loseID.equals("0"))
		{
			throw new FormException("WMEXP_LOC_LOSEID_REQ", null);
		}

		//Enter facility into facility field
		if(focus.isTempBio()){
			qbe = (QBEBioBean)focus;
			HttpSession session = state.getRequest().getSession();
			EpnyUserContext userContext = context.getServiceManager().getUserContext();

			String connection = userContext.get(SetIntoHttpSessionAction.DB_CONNECTION).toString();
			if(connection==null){
				connection = session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();
			}
			qbe.set("FACILITY", connection);
		}

		//Calculate Check Digit
		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		Array params = new Array();
		//Store parameters for stored procedure call
		params.add(new TextData(form.getFormWidgetByName("LOC").getDisplayValue()));
		//Set actionProperties for stored procedure call
		actionProperties.setProcedureParameters(params);
		actionProperties.setProcedureName("NSPCHECKDIGIT");
		try{
			//Run stored procedure
			WmsWebuiActionsImpl.doAction(actionProperties);
		}catch(WebuiException e){
			throw new UserException(e.getMessage(), new Object[] {});
		}
		return RET_CONTINUE;
	}

	private String greaterThanZero(RuntimeFormWidgetInterface widget, String text)throws FormException{
		String[] parameter = new String[1];
		String widgetLabel = colonStrip(readLabel(widget));
		String number = widget.getDisplayValue();
		if(widget.getAttributeType()==BioAttributeTypes.INT_TYPE){
			if(number!=null){
				if(number.matches(".*\\..*")){
					parameter[0] = widgetLabel;
					throw new FormException("WMEXP_NON_INTEGER", parameter);
				}
				number = commaStrip(number);
				int value = Integer.parseInt(number);
				if(value<0){
					if(text.equals("")){
						text += widgetLabel;
					}else{
						text += ", "+widgetLabel;
					}
				}
			}
		}else{
			if(number!=null){
				float value = Float.parseFloat(commaStrip(number));
				if(value<0){
					if(text.equals("")){
						text += widgetLabel;
					}else{
						text += ", "+widgetLabel;
					}
				}
			}else{
				parameter[0] = widgetLabel;
				throw new FormException("WMEXP_NULL_PRIMARY_FIELD", parameter);
			}
		}			
		return text;
	}

	private String colonStrip(String label){
		Pattern pattern = Pattern.compile("\\:");
		Matcher matcher = pattern.matcher(label);
		return 	matcher.replaceAll("");
	}

	private String commaStrip(String number){
		NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW
		String numberS;
		try{
			numberS = nf.parse(number).toString();
		} catch (ParseException e){
			e.printStackTrace();
			Pattern pattern = Pattern.compile("\\,");
			Matcher matcher = pattern.matcher(number);
			return matcher.replaceAll("");
		}
		return numberS;
	}

	private String readLabel(RuntimeFormWidgetInterface widget){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widget.getLabel("label",locale);
	}
}