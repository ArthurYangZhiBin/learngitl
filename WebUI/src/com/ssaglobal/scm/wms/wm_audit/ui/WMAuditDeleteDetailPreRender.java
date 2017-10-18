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
package com.ssaglobal.scm.wms.wm_audit.ui;

import java.util.Iterator;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.metadata.Metadata;
import com.epiphany.shr.metadata.objects.generated.np.Label;
import com.epiphany.shr.metadata.objects.generated.np.LabelFactory;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.UserInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.wm_asnreceipts.ui.wm_asnreceipts_computedattr;




public class WMAuditDeleteDetailPreRender extends FormExtensionBase{
	
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WMAuditDeleteDetailPreRender.class);
	protected int preRenderListForm(UIRenderContext context,
			RuntimeListFormInterface form) throws EpiException {		
		if(form.getFocus() == null || ((BioCollection)form.getFocus()).size() == 0)
			return RET_CONTINUE;	
		
		UserInterface user = context.getState().getUser();				
		Iterator widgetItr = ((RuntimeListForm)form).getFormWidgetsUnrestricted();			
		while(widgetItr.hasNext()){
			RuntimeFormWidgetInterface widget = (RuntimeFormWidgetInterface)widgetItr.next();
			String widgetAttr = widget.getAttribute();
			if(widgetAttr != null && widgetAttr.indexOf("VALUE") != -1){				
				String labelAttrName = widgetAttr.replaceFirst("VALUE", "LABEL");
				String objectId = (String)((BioCollection)form.getFocus()).elementAt(0).get(labelAttrName);
				if(objectId != null){			
					LabelFactory fac = new LabelFactory(Metadata.getInstance().getMetaFactory());			
					_log.debug("LOG_SYSTEM_OUT","\n\nCalling ID:"+objectId+"\n\n",100L);
					Label label = fac.getByObjectIdAndLabelTypeLookupIdAndLocaleId(objectId, "label", user.getLocale().getLocaleIDString());
					if(label != null)
						widget.setLabel("label", label.getText());
//defect1061.b
					else{
						label = fac.getByObjectIdAndLabelTypeLookupIdAndLocaleId(objectId, "label", "en");
						if(label != null)
								widget.setLabel("label", label.getText());
							else
								widget.setLabel("label", objectId);
					}
//defect1061.e					
				}
				else{
					 ((RuntimeListForm)form).hideColumn(widget.getName());					
				}
			}
		}
		return RET_CONTINUE;	
	}
	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
	throws UserException{
		UserInterface user = context.getState().getUser();				
		Iterator widgetItr = form.getFormWidgets();
		while(widgetItr.hasNext()){
			RuntimeFormWidgetInterface widget = (RuntimeFormWidgetInterface)widgetItr.next();
			String widgetAttr = widget.getAttribute();
			if(widgetAttr != null){
				String labelAttrName = widgetAttr.replaceFirst("VALUE", "LABEL");
				String objectId = (String)form.getFocus().getValue(labelAttrName);
				if(objectId != null){			
					LabelFactory fac = new LabelFactory(Metadata.getInstance().getMetaFactory());							
					Label label = fac.getByObjectIdAndLabelTypeLookupIdAndLocaleId(objectId, "label", user.getLocale().getLocaleIDString());
					if(label != null)
						widget.setLabel("label", label.getText());
//defect1061.b
					else{
						label = fac.getByObjectIdAndLabelTypeLookupIdAndLocaleId(objectId, "label", "en");
						if(label != null)
								widget.setLabel("label", label.getText());
							else
								widget.setLabel("label", objectId);
					}
//defect1061.e
				}
				else{
					widget.setBooleanProperty(widget.PROP_HIDDEN, true);
				}
			}
		}
		return RET_CONTINUE;	
	}
}
