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
package com.ssaglobal.scm.wms.uiextensions;

import java.text.DecimalFormat;
import java.util.TreeSet;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class SerializedDefaultValueGenerator extends FormExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(SerializedDefaultValueGenerator.class);
	private static String KEYTEMPLATE = "00000"; 
	
	public SerializedDefaultValueGenerator()	{
	}
	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws FormException {
		DataBean focus = form.getFocus();
		
		//Preform only on new detail forms
		if(focus.isTempBio())
		{
			String key = "";
			Object max = null;
			RuntimeFormInterface detailTab;
			SlotInterface toggleSlot = null;
			StateInterface state = context.getState();
			String generatedField = getParameterString("fieldName");
			String toggleFormName = getParameterString("toggleFormName");
			String toggleFormSlot = getParameterString("toggleSlot");
			String detailFormTab = getParameterString("listTabID");
			boolean isShipmentOrder = getParameterBoolean("isShipmentOrder");
//HC			DecimalFormat template = new DecimalFormat(KEYTEMPLATE);
			
//HC.b
			String zeroPadding=null;
			String sQueryString = "(wm_system_settings.CONFIGKEY = 'ZEROPADDEDKEYS')";
//			_log.debug("LOG_SYSTEM_OUT","sQueryString = "+ sQueryString,100L);
			Query bioQuery = new Query("wm_system_settings",sQueryString,null);
			UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();
			BioCollectionBean selCollection = uowb.getBioCollectionBean(bioQuery);
			try {
				zeroPadding = selCollection.elementAt(0).get("NSQLVALUE").toString();
			} catch (EpiDataException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			DecimalFormat template = null;
			if (zeroPadding.equalsIgnoreCase("0")){
				template = new DecimalFormat("0");
			}else{
				template = new DecimalFormat(KEYTEMPLATE);
			}
//HC.e			
			//New from detail toolbar
			if(context.getSourceWidget().getForm().getParentForm(state).getName().equals(toggleFormName)){
				//Detail view not in tab group
				if(isShipmentOrder){
					detailTab = getShipmentOrderDetailList(form, state, toggleFormName, toggleFormSlot, detailFormTab);
				}else{
					if(form.getParentForm(state).getName().equals(toggleFormName)){
						toggleSlot = form.getParentForm(state).getSubSlot(toggleFormSlot);
					}else{//Detail view in tab group
						toggleSlot = form.getParentForm(state).getParentForm(state).getSubSlot(toggleFormSlot);
					}
					detailTab = state.getRuntimeForm(toggleSlot, detailFormTab);
				}

				DataBean detailFocus = detailTab.getFocus();
				BioCollectionBean bioFocus = (BioCollectionBean)detailFocus;
				//Find max line number of list view
				try {
					if(bioFocus.size()!=0){
//						max = bioFocus.max(generatedField);
						max = findMax(bioFocus, generatedField);
						_log.debug("LOG_SYSTEM_OUT","MAX = "+ max,100L);
					}else{
						max = "0";
					}
				}catch(Exception e){
					e.printStackTrace();
					return RET_CANCEL;
				}
			}else{ //New from header toolbar
				max = "0";
			}
			//Generate Number
			double size = Double.parseDouble(max.toString());
			size+=1;
			QBEBioBean qbe = (QBEBioBean) focus;
			if(size>99999){
				form.getFormWidgetByName(generatedField).setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
				throw new FormException("WMEXP_VALUE_GEN_FAIL", null);
//				_log.debug("LOG_SYSTEM_OUT","**********System unable to create new line number: Maximum record number exceeded.**********",100L);
			}else{
				key = template.format(size);
			}
			//Store value
			qbe.setValue(generatedField, key);
		}
	return RET_CONTINUE;	
	}
	
	private RuntimeFormInterface getShipmentOrderDetailList(RuntimeFormInterface form, StateInterface state, String toggleFormName, String toggleFormSlot, String detailFormTab){
		SlotInterface toggleSlot;
		String soContainerSlot = getParameterString("soContainerSlot");
		if(form.getParentForm(state).getName().equals(toggleFormName)){
			toggleSlot = form.getParentForm(state).getSubSlot(toggleFormSlot);
		}else{//Detail view in tab group
			toggleSlot = form.getParentForm(state).getParentForm(state).getSubSlot(toggleFormSlot);
		}
		return state.getRuntimeForm(state.getRuntimeForm(toggleSlot, detailFormTab).getSubSlot(soContainerSlot), null);
	}
	
	private long findMax(BioCollection bioCollection, String field) throws EpiDataException{
		long max = 0;
		TreeSet tree = new TreeSet();
		for(int i = 0; i < bioCollection.size(); i++){				
			Object bioFieldValueObj = bioCollection.elementAt(i).get(field);
			try {
				long tempBioLongValue = Long.parseLong(bioFieldValueObj.toString());
				Long bioFieldValue = new Long(tempBioLongValue);
				tree.add(bioFieldValue);
			} catch (NumberFormatException e1) {					
			}
		}
		max = ((Long)tree.last()).longValue();
		return max;
	}
}