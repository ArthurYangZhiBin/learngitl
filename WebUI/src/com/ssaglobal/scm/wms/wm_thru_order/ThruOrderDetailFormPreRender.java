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
package com.ssaglobal.scm.wms.wm_thru_order;

import java.math.BigDecimal;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeWidget;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.util.FormUtil;

public class ThruOrderDetailFormPreRender extends FormExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ThruOrderDetailFormPreRender.class);
	public ThruOrderDetailFormPreRender()
    {
    }

	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
    throws UserException
    {	
		_log.debug("LOG_DEBUG_EXTENSION_THRUORDERDETFORMPREREN","Executing ThruOrderDetailFormPreRender",100L);		
		StateInterface state = context.getState();
		HttpSession session = state.getRequest().getSession();
		DataBean focus = form.getFocus();
		if(focus.isTempBio()){
			QBEBioBean qbe = (QBEBioBean)focus;
			RuntimeFormInterface headerForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_thru_order_header_detail_view",state);
			Object orderKeyObj = headerForm.getFormWidgetByName("ORDERKEY");
			_log.debug("LOG_DEBUG_EXTENSION_THRUORDERDETFORMPREREN","orderKeyObj:"+orderKeyObj,100L);			
			if(orderKeyObj != null && ((RuntimeWidget)orderKeyObj).getDisplayValue() != null && ((RuntimeWidget)orderKeyObj).getDisplayValue().length() > 0){
				String orderKey = ((RuntimeWidget)orderKeyObj).getDisplayValue();
				Integer sequence = new Integer(orderKey);
				if(qbe.get("SEQUENCE") == null){
					qbe.set("SEQUENCE",sequence);
				}
				else{
					String qbeSequence = qbe.get("SEQUENCE").toString();
					if(!qbeSequence.equals(sequence.toString())){
						_log.debug("LOG_DEBUG_EXTENSION_THRUORDERDETFORMPREREN","Setting Sequence:"+sequence,100L);						
						qbe.set("SEQUENCE",sequence);
					}
				}
			}
		}
		if(session.getAttribute("THRUORDERORIGQTY") != null){			
			String origQty = (String)session.getAttribute("THRUORDERORIGQTY");
			_log.debug("LOG_DEBUG_EXTENSION_THRUORDERDETFORMPREREN","Found session attribute THRUORDERORIGQTY:"+origQty,100L);			
			RuntimeFormWidgetInterface widgetOpenQty = form.getFormWidgetByName("OPENQTY");
			RuntimeFormWidgetInterface widgetQtyToProcess = form.getFormWidgetByName("QTYTOPROCESS");
			widgetOpenQty.setDisplayValue(origQty);
			widgetQtyToProcess.setDisplayValue(origQty);
		}
		
		// IF ProcQty > 0 or Shipped Qty > 0 or Conv Qty > 0 then Original Qty is read only
		BigDecimal procQty = focus.getValue("PROCESSEDQTY") == null?new BigDecimal(0):(BigDecimal)focus.getValue("PROCESSEDQTY");
		BigDecimal shippedQty = focus.getValue("SHIPPEDQTY") == null?new BigDecimal(0):(BigDecimal)focus.getValue("SHIPPEDQTY");
		BigDecimal convQty = focus.getValue("CONVERTEDQTY") == null?new BigDecimal(0):(BigDecimal)focus.getValue("CONVERTEDQTY");
		_log.debug("LOG_DEBUG_EXTENSION_THRUORDERDETFORMPREREN","Got procQty:"+procQty,100L);
		_log.debug("LOG_DEBUG_EXTENSION_THRUORDERDETFORMPREREN","Got shippedQty:"+shippedQty,100L);
		_log.debug("LOG_DEBUG_EXTENSION_THRUORDERDETFORMPREREN","Got convQty:"+convQty,100L);
		if(procQty.doubleValue() > 0 || shippedQty.doubleValue() > 0 || convQty.doubleValue() > 0){
			RuntimeFormWidgetInterface widgetOpenQty = form.getFormWidgetByName("ORIGINALQTY");
			widgetOpenQty.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
		}else{
			RuntimeFormWidgetInterface widgetOpenQty = form.getFormWidgetByName("ORIGINALQTY");
			widgetOpenQty.setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
		}
		_log.debug("LOG_DEBUG_EXTENSION_THRUORDERDETFORMPREREN","Exiting ThruOrderDetailFormPreRender",100L);		
		return RET_CONTINUE;
	}
		
}