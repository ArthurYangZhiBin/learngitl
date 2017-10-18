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

package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.infor.scm.waveplanning.common.util.BioAttributeUtil;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class WaveMaintTaskPrevNextTaskDetail extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	private String CONTEXT_WAVEMGMT_TASKVIEW_TYPE = "WAVEMGMT_TASKVIEW_TYPE";
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
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();

		EpnyUserContext userContext = context.getServiceManager().getUserContext();
		final String interactionId = state.getInteractionId();
		CONTEXT_WAVEMGMT_TASKVIEW_TYPE += interactionId;
		String viewType = (String) userContext.get(CONTEXT_WAVEMGMT_TASKVIEW_TYPE);
		
		

		boolean waveQueryFlag = getParameterBoolean("WAVEORORDER");
		boolean prevActionFlag = getParameterBoolean("PREVORNEXT");
		
		if("WAVE".equals(viewType))
		{
			waveQueryFlag = true;
		}
		else
		{
			waveQueryFlag = false;
		}

		DataBean focus = state.getCurrentRuntimeForm().getFocus();
		String taskKey = BioAttributeUtil.getString(focus, "TASKDETAILKEY");

		Query taskQuery;
		if (waveQueryFlag == true)
		{
			String waveKey = BioAttributeUtil.getString(focus, "WAVEKEY");
			taskQuery = new Query("wm_taskdetail", "wm_taskdetail.WAVEKEY = '" + waveKey + "'", null);
		}
		else
		{
			String orderKey = BioAttributeUtil.getString(focus, "ORDERKEY");
			taskQuery = new Query("wm_taskdetail", "wm_taskdetail.ORDERKEY = '" + orderKey + "'", null);
		}

		BioCollectionBean taskResults = uow.getBioCollectionBean(taskQuery);
		int pos = 0;
		for (int i = 0; i < taskResults.size(); i++)
		{
			BioBean task = taskResults.get("" + i);
			if (taskKey.equals(BioAttributeUtil.getString((DataBean) task, "TASKDETAILKEY")))
			{
				pos = i;
			}
		}

		if (prevActionFlag == true)
		{
			if (pos == 0)
			{
				result.setFocus(taskResults.get("" + pos));
			}
			else
			{
				result.setFocus(taskResults.get("" + --pos));
			}
		}
		else
		{
			if (pos == taskResults.size() - 1)
			{
				result.setFocus(taskResults.get("" + pos));
			}
			else
			{
				result.setFocus(taskResults.get("" + ++pos));
			}
		}
		

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

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
