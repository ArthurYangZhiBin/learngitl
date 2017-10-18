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

package com.ssaglobal.scm.wms.wm_pickdetail.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.service.dutilitymanagement.SerialNumberObj;
import com.ssaglobal.scm.wms.service.dutilitymanagement.dto.SerialNoDTO;
import com.ssaglobal.scm.wms.service.dutilitymanagement.dto.SkuSNConfDTO;
import com.ssaglobal.scm.wms.util.StringUtils;
import com.ssaglobal.scm.wms.util.dao.SkuSNConfDAO;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class SOCatchWeightCatchDataValidateSerialNumber extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(SOCatchWeightCatchDataValidateSerialNumber.class);

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
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		StateInterface state = context.getState();
		UnitOfWorkBean tuow = state.getTempUnitOfWork();

		DataBean cwcdFocus = state.getCurrentRuntimeForm().getFocus();

		String orderkey = (String) cwcdFocus.getValue("SOURCEKEY");
		String orderline = (String) cwcdFocus.getValue("SOURCELINENUMBER");
		String sku = (String) cwcdFocus.getValue("SKU");
		String owner = null;
		BioCollectionBean rs = tuow.getBioCollectionBean(new Query("wm_orderdetail", "wm_orderdetail.ORDERKEY = '" + orderkey + "' and wm_orderdetail.ORDERLINENUMBER = '" + orderline
				+ "' and wm_orderdetail.SKU = '" + sku + "'", null));
		for (int i = 0; i < rs.size(); i++)
		{
			owner = (String) rs.get("" + i).getValue("STORERKEY");
		}
		
		String serialNumberLong = context.getSourceWidget().getDisplayValue();

		ArrayList<?> serials = null;

		try
		{
			serials = getSerials(owner, sku, serialNumberLong);
		} catch (RuntimeException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			_log.error("LOG_ERROR_EXTENSION_SOCatchWeightValidateSerialNumber_execute", StringUtils.getStackTraceAsString(e), SuggestedCategory.NONE);
			throw new UserException("WMEXP_SN_FORMAT", new String[] { serialNumberLong });
		}

		if (serials == null || serials.size() == 0)
		{
			throw new UserException("WMEXP_SN_FORMAT", new String[] { serialNumberLong });
		}
		//Set OOTHER1 with parsed Serial

		cwcdFocus.setValue("OOTHER1", ((SerialNoDTO) serials.get(0)).getSerialnumber());

		_log.debug("LOG_DEBUG_EXTENSION_SOCatchWeightValidateSerialNumber_execute", "Serials " + serials, SuggestedCategory.NONE);

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
