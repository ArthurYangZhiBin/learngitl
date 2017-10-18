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

package com.ssaglobal.scm.wms.wm_load_schedule.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.ssaglobal.scm.wms.wm_load_planning.ui.LPEnableToolbar;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class LoadScheduleLoadCustomerName extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	private static final String WM_STORER = "wm_storer";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LoadScheduleLoadCustomerName.class);

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

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		_log.debug("LOG_SYSTEM_OUT","!@#!@# Start of LoadScheduleLoadCustomerName",100L);

		StateInterface state = context.getState();
		DataBean currentFormFocus = state.getFocus();

		if (currentFormFocus instanceof BioBean)
		{
			currentFormFocus = (BioBean) currentFormFocus;
		}

		String customerValue = (String) currentFormFocus.getValue("CONSIGNEEKEY");
		_log.debug("LOG_SYSTEM_OUT","!@# CONSIGNEEKEY - " + customerValue,100L);

		//Query STORER Table to retrieve Company
		UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();

		String storerQueryStatement = WM_STORER + "." + "STORERKEY" + " = '" + customerValue + "' AND " + WM_STORER + "."
				+ "TYPE" + " = '" + "2" + "'";
		_log.debug("LOG_SYSTEM_OUT","//// Query " + storerQueryStatement,100L);
		Query storerQuery = new Query(WM_STORER, storerQueryStatement, null);
		BioCollectionBean storerResults = uow.getBioCollectionBean(storerQuery);

		// Display results
		String customerName;
		_log.debug("LOG_SYSTEM_OUT","////// Size of storer results " + storerResults.size(),100L);
		if ((storerResults.size() == 1))
		{

			customerName = storerResults.elementAt(0).get("COMPANY").toString();
		}
		else if (storerResults.size() <= 0){
			String parameters[] = new String[1];
			parameters[0]=customerValue;
			throw new FormException("WMEXP_CONSIGNEE_VALIDATION", parameters);
		}else
		{
			//throw new EpiException("EXP", "Non unique results returned");
			String parameters[] = new String[1];
			parameters[0]=customerValue;
			throw new FormException("WMEXP_DUPLICATE_CUSTOMER", parameters);
			
		}
		_log.debug("LOG_SYSTEM_OUT","/// Company" + customerName,100L);
		currentFormFocus.setValue("CUSTOMERNAME", customerName);
		return RET_CONTINUE;
	}

}
