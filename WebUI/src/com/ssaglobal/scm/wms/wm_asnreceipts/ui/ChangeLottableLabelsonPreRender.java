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
package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

//Epiphany Classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.ssaglobal.scm.wms.util.FormUtil;

public class ChangeLottableLabelsonPreRender extends FormExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ChangeLottableLabelsonPreRender.class);
	public ChangeLottableLabelsonPreRender(){
		
	}
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
	throws UserException{		
		
		try 
		{
			StateInterface state = context.getState();
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			DataBean headerFocus = form.getFocus();
			Object skuObj = headerFocus.getValue("SKU");
			Object storerObject = headerFocus.getValue("STORERKEY");
			if (skuObj != null && storerObject != null)
			{
				BioCollectionBean listCollection = null;
				String skuVal = skuObj.toString().trim();
				String storerKey = storerObject.toString().trim();
				_log.debug("LOG_SYSTEM_OUT","SKU ="+ skuObj.toString()+ "End",100L);
				String sQueryString = "(wm_sku.STORERKEY = '"+storerKey+"' AND wm_sku.SKU = '"+skuVal+"')";
				_log.debug("LOG_SYSTEM_OUT","sQueryString = "+ sQueryString,100L);
				Query bioQuery = new Query("wm_sku",sQueryString,null);
				listCollection = uowb.getBioCollectionBean(bioQuery);
				BioBean skuBio = (BioBean)listCollection.elementAt(0);
				form.getFormWidgetByName("LOTTABLE01").setLabel("label",skuBio.getString("LOTTABLE01LABEL")+":");
				form.getFormWidgetByName("LOTTABLE02").setLabel("label",skuBio.getString("LOTTABLE02LABEL")+":");
				form.getFormWidgetByName("LOTTABLE03").setLabel("label",skuBio.getString("LOTTABLE03LABEL")+":");
				form.getFormWidgetByName("LOTTABLE04").setLabel("label",skuBio.getString("LOTTABLE04LABEL")+":");
				form.getFormWidgetByName("LOTTABLE05").setLabel("label",skuBio.getString("LOTTABLE05LABEL")+":");
				form.getFormWidgetByName("LOTTABLE06").setLabel("label",skuBio.getString("LOTTABLE06LABEL")+":");
				form.getFormWidgetByName("LOTTABLE07").setLabel("label",skuBio.getString("LOTTABLE07LABEL")+":");
				form.getFormWidgetByName("LOTTABLE08").setLabel("label",skuBio.getString("LOTTABLE08LABEL")+":");
				form.getFormWidgetByName("LOTTABLE09").setLabel("label",skuBio.getString("LOTTABLE09LABEL")+":");
				form.getFormWidgetByName("LOTTABLE10").setLabel("label",skuBio.getString("LOTTABLE10LABEL")+":");
				form.getFormWidgetByName("LOTTABLE11").setLabel("label",skuBio.getString("LOTTABLE11LABEL")+":");				
				form.getFormWidgetByName("LOTTABLE12").setLabel("label",skuBio.getString("LOTTABLE12LABEL")+":");			
			}
	    } 
		catch(Exception e) 
		{     
	          _log.debug("LOG_SYSTEM_OUT","EXCEPTION:"+e.getMessage(),100L);
	    }
	    return RET_CONTINUE;
	}
	
}
