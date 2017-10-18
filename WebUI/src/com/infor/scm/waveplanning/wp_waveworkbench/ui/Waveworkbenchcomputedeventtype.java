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
package com.infor.scm.waveplanning.wp_waveworkbench.ui;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.metadata.AttributeDomainExtensionBase;
import com.epiphany.shr.ui.metadata.DropdownContentsContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.common.util.WPUserUtil;

public class Waveworkbenchcomputedeventtype extends AttributeDomainExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(Waveworkbenchcomputedeventtype.class);
	public Waveworkbenchcomputedeventtype()
	{
	}
	protected int execute(DropdownContentsContext context, List value, List labels) throws EpiException {		
		_log.debug("LOG_DEBUG_EXTENSION_COMPDOMFAC","Executing Waveworkbenchcomputedeventtype",100L);
		StateInterface state = context.getState();
		Locale localeObj = WPUserUtil.getUserLocale(state);	
		value.add("event.EngineError");
		labels.add(getEventTypefromBundle("event.EngineError",state, localeObj));	
		value.add("event.ordersyncall");		
		labels.add(getEventTypefromBundle("event.ordersyncall",state, localeObj));
		value.add("event.ordersyncnew");
		labels.add(getEventTypefromBundle("event.ordersyncnew",state, localeObj));		
		value.add("event.wavecreation");
		labels.add(getEventTypefromBundle("event.wavecreation",state, localeObj));		
		value.add("event.waveconfirmation");
		labels.add(getEventTypefromBundle("event.waveconfirmation",state, localeObj));		
		value.add("event.waverelease");
		labels.add(getEventTypefromBundle("event.waverelease",state, localeObj));		
		value.add("event.skuDownloadAll");
		labels.add(getEventTypefromBundle("event.skuDownloadAll",state, localeObj));		
		value.add("event.skuDownloadModified");
		labels.add(getEventTypefromBundle("event.skuDownloadModified",state, localeObj));
		value.add("event.archiving");
		labels.add(getEventTypefromBundle("event.archiving",state, localeObj));		
		value.add("event.wavedeletion");
		labels.add(getEventTypefromBundle("event.wavedeletion",state, localeObj));
		value.add("event.skusyncall");
		labels.add(getEventTypefromBundle("event.skusyncall",state, localeObj));		
		value.add("event.skusyncnew");
		labels.add(getEventTypefromBundle("event.skusyncnew",state, localeObj));		
		value.add("event.waveconsolidation");
		labels.add(getEventTypefromBundle("event.waveconsolidation",state, localeObj));
		value.add("event.waveunconsolidation");
		labels.add(getEventTypefromBundle("event.waveunconsolidation",state, localeObj));
		value.add("event.wavepreallocation");
		labels.add(getEventTypefromBundle("event.wavepreallocation",state, localeObj));
		value.add("event.waveallocation");
		labels.add(getEventTypefromBundle("event.waveallocation",state, localeObj));
		value.add("event.waveunallocation");
		labels.add(getEventTypefromBundle("event.waveunallocation",state, localeObj));
		value.add("event.shipwave");
		labels.add(getEventTypefromBundle("event.shipwave",state, localeObj));			
		_log.debug("LOG_DEBUG_EXTENSION_COMPDOMFAC","Leaving Waveworkbenchcomputedeventtype",100L);		
		return RET_CONTINUE;
	}
	
	private String getEventTypefromBundle(String eventType, StateInterface state, Locale localeObj){
			
		ResourceBundle messages = ResourceBundle.getBundle("com.ssaglobal.scm.waveplanning.resources.WavePlanning" , localeObj);
		String output = "";
				
		if (eventType != null) {	   		
			output = messages.getString(eventType);
			return output;
		}
		else {
			return "";
		}
			
	}
}
