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
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.BioUtil;
import com.ssaglobal.scm.wms.wm_asnreceipts.ui.CatchWeightDataDefaultValues;

/**
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected
 * to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class SOCatchWeightCatchDataDefaultValues extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(SOCatchWeightCatchDataDefaultValues.class);

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * 
	 * @param context
	 *            The ActionContext for this extension
	 * @param result
	 *            The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * 
	 * @exception EpiException
	 */
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{

		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		DataBean pickDetailFocus = null;
		//value of 4 will find the wm_pickdetail record
		for (int i = 0; i < 6; i++)
		{
			DataBean ancestorFocus = state.getAncestorFocus(i);
			if (ancestorFocus.getDataType().equals("wm_pickdetail"))
			{
				pickDetailFocus = ancestorFocus;
				break;
			}
		}
		//These fields should always exist
		String orderKey = (String) pickDetailFocus.getValue("ORDERKEY");
		String orderLine = (String) pickDetailFocus.getValue("ORDERLINENUMBER");
		String pickDetailKey = (String)pickDetailFocus.getValue("PICKDETAILKEY");
_log.debug("LOG_SYSTEM_OUT","*****pickdetailkey="+pickDetailKey,100L);
		DataBean newCWCD = result.getFocus();
		setNewValues(uow, pickDetailFocus, orderKey, orderLine, pickDetailKey,
				newCWCD);

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}

	public void setNewValues(UnitOfWorkBean uow, DataBean pickDetailFocus,
			String orderKey, String orderLine, String pickDetailKey,
			DataBean newCWCD) throws EpiDataException, UserException,
			DataBeanException {
		CatchWeightDataDefaultValues.setLOTXIDKEY(newCWCD, uow, orderKey, orderLine, CatchWeightDataDefaultValues.TYPE_OUTBOUND, pickDetailKey);
		CatchWeightDataDefaultValues.setLOTXIDLINENUMBER(newCWCD, uow, orderKey, orderLine, CatchWeightDataDefaultValues.TYPE_OUTBOUND);

		newCWCD.setValue("SOURCEKEY", orderKey);
		newCWCD.setValue("SOURCELINENUMBER", orderLine);
		newCWCD.setValue("PICKDETAILKEY", pickDetailFocus.getValue("PICKDETAILKEY"));
		newCWCD.setValue("LOT", pickDetailFocus.getValue("LOT"));
		newCWCD.setValue("ID", getLPN(pickDetailFocus));
		newCWCD.setValue("OQTY", "1");

		//set Required
		newCWCD.setValue("SKU", pickDetailFocus.getValue("SKU"));

		//Set CaptureBy
		BioCollectionBean rs = uow.getBioCollectionBean(new Query("sku", "sku.STORERKEY = '" + pickDetailFocus.getValue("STORERKEY") + "' and sku.SKU = '" + pickDetailFocus.getValue("SKU") + "'",
				null));
		if (rs.size() == 0)
		{
			throw new UserException("WMEXP_VALIDATESKU", new Object[] { pickDetailFocus.getValue("SKU"), pickDetailFocus.getValue("STORERKEY") });
		}
		newCWCD.setValue("CAPTUREBY", rs.get("0").getValue("OCWBY"));
		newCWCD.setValue("PACK", rs.get("0").getValue("PACKKEY"));

		//set outbound
		newCWCD.setValue("IOFLAG", "O");

		_log.debug("LOG_DEBUG_EXTENSION_SOCatchWeightCatchDataDefaultValues_execute", BioUtil.getBioAsString(newCWCD, uow.getBioMetadata(newCWCD.getDataType())), SuggestedCategory.NONE);
	}

	private Object getLPN(DataBean pickDetailFocus)
	{
		return pickDetailFocus.getValue("ID") == null ? " " : pickDetailFocus.getValue("ID");
	}

}
