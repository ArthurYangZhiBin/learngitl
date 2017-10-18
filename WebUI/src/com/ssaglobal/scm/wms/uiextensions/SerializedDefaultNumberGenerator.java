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
import java.util.Iterator;
import java.util.TreeSet;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class SerializedDefaultNumberGenerator extends FormExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(SerializedDefaultNumberGenerator.class);
	public SerializedDefaultNumberGenerator()
	{
	}
	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws UserException
	{
		String key = "";
		DataBean focus = null;
		QBEBioBean qbe = null;
		Object max = null;
		String shellSlot1 = (String)getParameter("shellSlot1");		
		String generatedField = getParameterString("fieldName");		
		String bioCollection = getParameterString("bioCollection");		
		String headerOrDetail = getParameterString("headerOrDetail");		
		String bioField = getParameterString("bioField");		
		String bioName = getParameterString("bioName");		
		String keySize = getParameterString("keySize");		
		String queryString = getParameterString("queryString");		
		_log.debug("LOG_DEBUG_EXTENSION_SDNG","Parameter shellSlot1: "+shellSlot1,100L);	
		_log.debug("LOG_DEBUG_EXTENSION_SDNG","Parameter fieldName: "+generatedField,100L);	
		_log.debug("LOG_DEBUG_EXTENSION_SDNG","Parameter bioCollection: "+bioCollection,100L);	
		_log.debug("LOG_DEBUG_EXTENSION_SDNG","Parameter headerOrDetail: "+headerOrDetail,100L);	
		_log.debug("LOG_DEBUG_EXTENSION_SDNG","Parameter fieldName: "+bioField,100L);	
		_log.debug("LOG_DEBUG_EXTENSION_SDNG","Parameter bioName: "+bioName,100L);	
		_log.debug("LOG_DEBUG_EXTENSION_SDNG","Parameter keySize: "+keySize,100L);	
		_log.debug("LOG_DEBUG_EXTENSION_SDNG","Parameter queryString: "+queryString,100L);
		focus = form.getFocus();
		
		//Preform only on new detail forms
		if(focus.isTempBio())
		{
			BioCollection bioFocus = null;					
			if(headerOrDetail.equalsIgnoreCase("header")){
				Query loadBiosQry = new Query(bioName, queryString, null);
				UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
				bioFocus = uow.getBioCollectionBean(loadBiosQry);			
			}
			else{
				StateInterface state = context.getState();
				RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
				RuntimeFormInterface toggleForm = toolbar.getParentForm(state);
				RuntimeFormInterface shellForm = toggleForm.getParentForm(state);
				if(shellForm == null)
					shellForm = toggleForm;
				_log.debug("LOG_DEBUG_EXTENSION_SDNG","shellForm:"+shellForm.getName(),100L);				
				Iterator itr = shellForm.getSubSlotsIterator();
				while(itr.hasNext()){
					SlotInterface slot = (SlotInterface)itr.next();
					_log.debug("LOG_DEBUG_EXTENSION_SDNG","Slot:"+slot.getName(),100L);					
				}
				SlotInterface headerSlot = shellForm.getSubSlot(shellSlot1);		//HC
				RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
				if(headerForm.isListForm()){
			    	String [] desc = new String[1];
			    	desc[0] = "";
			    	return RET_CONTINUE;			
				}
				try {
					DataBean headerFocus = headerForm.getFocus();
					if(headerFocus.isTempBio()){						
						bioFocus = null;
					}else{
						bioFocus = ((BioBean)headerFocus).getBioCollection(bioCollection);
					}
				} catch (EpiDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return RET_CONTINUE;
				}
			}
			//Find max line number of list view
			try {
				if(bioFocus != null && bioFocus.size()!=0){					
					_log.debug("LOG_DEBUG_EXTENSION_SDNG","Dynamically finding max",100L);		
					max = new Long(findMax(bioFocus,bioField));
				}else{
					_log.debug("LOG_DEBUG_EXTENSION_SDNG","Statically finding max",100L);					
					max = "0";
				}
			}catch(Exception e){
				e.printStackTrace();
				return RET_CANCEL;
			}
			
			//Generate Number
			long keyInt = Integer.parseInt(max.toString());
			keyInt+=1;
			qbe = (QBEBioBean) focus;
			int keySizeInt = Integer.parseInt(keySize);
			
			if(keySizeInt != -1){
				String keySizeString = "";
				for (int i = 0; i < keySizeInt; i++){
					keySizeString += "9";
				}
				if(keyInt > Long.parseLong(keySizeString)){
					_log.debug("LOG_DEBUG_EXTENSION_SDNG","**********System unable to create new line number: Maximum record number exceeded.**********",100L);					
					form.getFormWidgetByName(generatedField).setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
					return RET_CANCEL;
				}else{
					key = ""+keyInt;
//HC.b
					String zeroPadding=null;
					String sQueryString = "(wm_system_settings.CONFIGKEY = 'ZEROPADDEDKEYS')";
					Query bioQuery = new Query("wm_system_settings",sQueryString,null);
					UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();
					BioCollectionBean selCollection = uowb.getBioCollectionBean(bioQuery);
					try {
						zeroPadding = selCollection.elementAt(0).get("NSQLVALUE").toString();
					} catch (EpiDataException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (zeroPadding.equalsIgnoreCase("1")){
						for(int i = key.length(); i < keySizeInt; i++){
							key = "0"+key;
						}
					}
//HC.e
//HC					for(int i = key.length(); i < keySizeInt; i++){
//HC						key = "0"+key;
//HC					}
				}
			}
			else{
				key = ""+keyInt;
			}
			//Store value			
			_log.debug("LOG_DEBUG_EXTENSION_SDNG","Value from previous code line: "+key,100L);
			qbe.setValue(generatedField, key);
			_log.debug("LOG_DEBUG_EXTENSION_SDNG","Value stored in field",100L);			
		}
	return RET_CONTINUE;	
	}
	
	private long findMax(BioCollection bioCollection, String field) throws EpiDataException{
		long max = 0;
		
		try {
			Object maxObj = bioCollection.max(field);
			max = Long.parseLong(maxObj.toString());
		} catch (NumberFormatException e) {			
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
		}
		
		return max;
	}
}