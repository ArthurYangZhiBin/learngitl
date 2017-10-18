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

package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.infor.scm.waveplanning.common.util.BioAttributeUtil;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class WaveMaintReturnToWhereWeNavigatedFrom extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WaveMaintReturnToWhereWeNavigatedFrom.class);

	private String CONTEXT_WAVEMGMT_ORIGIN = "WAVEMGMT_ORIGIN";

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
		_log.debug("LOG_DEBUG_EXTENSION_WaveMaintReturnToWhereWeNavigatedFrom_execute",
				"Entering WaveMaintReturnToWhereWeNavigatedFrom", SuggestedCategory.NONE);
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		String listNav = getParameterString("LISTNAV");
		String normalNav = getParameterString("NORMALNAV");

		EpnyUserContext userContext = context.getServiceManager().getUserContext();
		CONTEXT_WAVEMGMT_ORIGIN += state.getInteractionId();
		String origin = (String) userContext.get(CONTEXT_WAVEMGMT_ORIGIN);

		if (WaveMaintSetWhereWeNavigatedFrom.WAVEMGMT_ORIGIN_LIST.equals(origin))
		{
			context.setNavigation(listNav);
			//Query For Wave List
//			BioCollectionBean rs = uow.getBioCollectionBean(new Query("wm_wp_wave", "wp_wave.STATUS >= '3'", "wp_wave.WAVEKEY DESC"));
			BioCollectionBean rs = uow.getBioCollectionBean(new Query("wm_wp_wave", null, "wp_wave.WAVEKEY DESC"));
			result.setFocus(rs);
		}
		else if (WaveMaintSetWhereWeNavigatedFrom.WAVEMGMT_ORIGIN_NORMAL.equals(origin))
		{
			context.setNavigation(normalNav);
			//Query for Wave Record
			DataBean focus = state.getCurrentRuntimeForm().getFocus();
			int wavekey = BioAttributeUtil.getInt(focus, "WAVEKEY");
			BioCollectionBean rs = uow.getBioCollectionBean(new Query("wm_wp_wave", "wp_wave.WAVEKEY ='" + wavekey + "'",
					null));
			if (rs.size() == 1)
			{
				result.setFocus(rs.get("" + 0));
			}
			else
			{
				_log.error("LOG_ERROR_EXTENSION_WaveMaintReturnToWhereWeNavigatedFrom_execute", "Returned " + rs.size()
						+ " records, unable to navigate", SuggestedCategory.NONE);
				return RET_CANCEL;
			}
		}

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		_log.debug("LOG_DEBUG_EXTENSION_WaveMaintReturnToWhereWeNavigatedFrom_execute",
				"Leaving WaveMaintReturnToWhereWeNavigatedFrom", SuggestedCategory.NONE);
		return RET_CONTINUE;
	}

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked or
	 * a value entered in a form in a modal dialog
	 * Write code here if u want this to be called when the UI Action event is fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext} exposes information about the
	 * event, including the service and the user interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes information about the results
	 * of the action that has occurred, and enables your extension to modify them.</li>
	 * </ul>
	 */
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
	{

		try
		{
			// Add your code here to process the event

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
