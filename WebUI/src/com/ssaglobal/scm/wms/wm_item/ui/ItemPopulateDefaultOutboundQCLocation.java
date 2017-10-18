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
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class ItemPopulateDefaultOutboundQCLocation extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemPopulateDefaultOutboundQCLocation.class);

	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {
		DataBean focus = form.getFocus();
		//RuntimeFormWidgetInterface outboundQCWidget = form.getFormWidgetByName("QCLOCOUT");
		try {
			if(isEmpty(focus.getValue("QCLOCOUT"))) {
				Object owner = focus.getValue("STORERKEY");
				owner = owner == null ? null : owner.toString().toUpperCase();
				//query owner for default qc outbound location
				String query = "SELECT DEFAULTQCLOCOUT FROM STORER WHERE STORERKEY = '"+owner+"'";
				_log.debug("LOG_DEBUG_EXTENSION", "///QUERY " + query, SuggestedCategory.NONE);
				EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
				if(results.getRowCount() != 0) {
					focus.setValue("QCLOCOUT", results.getAttribValue(1).getAsString());
				}
			}
		} catch(Exception e) {
			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}
		return RET_CONTINUE;
	}

	private boolean isEmpty(Object attributeValue) {
		if(attributeValue == null) {
			return true;
		} else if(attributeValue.toString().matches("\\s*")){
			return true;
		} else {
			return false;
		}
	}
}