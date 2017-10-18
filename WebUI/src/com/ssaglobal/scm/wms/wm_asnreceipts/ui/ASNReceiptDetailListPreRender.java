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
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;


public class ASNReceiptDetailListPreRender extends FormExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ASNReceiptDetailListPreRender.class);

	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) throws EpiException
	/*
	 * Krishna Kuchipudi: 14-April-2010: 3PL Enhancements -Added this bethod to hide the advanced weight fields in ASN list view and to be displayed when
	 *                    user duplicates the ASN records.
	 *
	 *
	 * */
	{

		form.hideColumn("GROSSWGT");
		form.hideColumn("TAREWGT");
		form.hideColumn("NETWGT");
		return RET_CONTINUE;
	}

	protected int preRenderListForm(ModalUIRenderContext context, RuntimeListFormInterface form) throws EpiException
	{
		form.hideColumn("Detail");
		
		return RET_CONTINUE;
	}
	
}