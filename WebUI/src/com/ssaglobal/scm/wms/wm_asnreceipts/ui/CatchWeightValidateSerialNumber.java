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

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.service.dutilitymanagement.SerialNumberObj;
import com.ssaglobal.scm.wms.service.dutilitymanagement.dto.SerialNoDTO;
import com.ssaglobal.scm.wms.service.dutilitymanagement.dto.SkuSNConfDTO;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.StringUtils;
import com.ssaglobal.scm.wms.util.dao.SkuSNConfDAO;

public class CatchWeightValidateSerialNumber extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(CatchWeightValidateSerialNumber.class);

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @exception EpiException
	 */
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		StateInterface state = context.getState();
		//retrieve Storer & Sku Info
//		RuntimeFormInterface receiptForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "receipt_detail_view", state);
//		DataBean receiptFocus = receiptForm.getFocus();

		ArrayList<String> tabs = new ArrayList<String>();
		tabs.add("wm_receiptdetail_detail_view");
		tabs.add("tab 0");
		RuntimeFormInterface receiptDetailForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wms_ASN_Line_Detail_view", tabs, state);
		DataBean receiptDetailFocus = receiptDetailForm.getFocus();

		if (receiptDetailFocus.getValue("SKU") == null || receiptDetailFocus.getValue("STORERKEY") == null)
		{
			throw new UserException("WMEXP_CWCD_OWNERITEM_REQ", new Object[] {});
		}

		String sku = (String) receiptDetailFocus.getValue("SKU");
		String owner = (String) receiptDetailFocus.getValue("STORERKEY");
		String serialNumberLong = context.getSourceWidget().getDisplayValue();

		ArrayList<?> serials = null;

		try
		{
			serials = getSerials(owner, sku, serialNumberLong);
		} catch (RuntimeException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			_log.error("LOG_ERROR_EXTENSION_CatchWeightValidateSerialNumber_execute", StringUtils.getStackTraceAsString(e), SuggestedCategory.NONE);
			throw new UserException("WMEXP_SN_FORMAT", new String[] { serialNumberLong });
		}

		if(serials == null || serials.size() == 0)
		{
			throw new UserException("WMEXP_SN_FORMAT", new String[] { serialNumberLong });
		}
		//Set IOTHER1 with parsed Serial
		DataBean cwcdFocus = state.getCurrentRuntimeForm().getFocus();
		cwcdFocus.setValue("IOTHER1", ((SerialNoDTO) serials.get(0)).getSerialnumber());
		
		_log.debug("LOG_DEBUG_EXTENSION_CatchWeightValidateSerialNumber_execute", "Serials " + serials, SuggestedCategory.NONE);

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}

	public ArrayList<?> getSerials(String storerkey, String sku, String serial)
	{

		SkuSNConfDTO skuConf = SkuSNConfDAO.getSkuSNConf(storerkey, sku);

        

        SerialNumberObj serialNumber = new SerialNumberObj(skuConf);

        serialNumber.setStorerkey(storerkey);

        serialNumber.setSku(sku);

        

        

        ArrayList<?> list = serialNumber.getValidSerialNos(serial);

        return list;



	}

}
