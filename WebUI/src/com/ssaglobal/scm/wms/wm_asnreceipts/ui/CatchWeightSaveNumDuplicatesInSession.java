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
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeForm;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.SessionUtil;
import com.ssaglobal.scm.wms.util.StringUtils;

public class CatchWeightSaveNumDuplicatesInSession extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	public static final String ASN_CATCH_W_D_NUM_DUPLICATES = "ASN_CATCH_W_D_NUM_DUPLICATES";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CatchWeightSaveNumDuplicatesInSession.class);

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
		String theSQLStmt = "SELECT     LOTXIDHEADER.LOTXIDKEY, LOTXIDDETAIL.LOTXIDLINENUMBER, LOTXIDHEADER.STORERKEY, "
				+ "LOTXIDDETAIL.SKU, LOTXIDDETAIL.IOFLAG, LOTXIDDETAIL.IOTHER1, LOTXIDDETAIL.IOTHER2, " + "LOTXIDDETAIL.IOTHER3, LOTXIDDETAIL.IOTHER4, LOTXIDDETAIL.IOTHER5, LOTXIDDETAIL.LOT, "
				+ "LOTXIDDETAIL.ID, LOTXIDDETAIL.SOURCEKEY, LOTXIDDETAIL.SOURCELINENUMBER, " + "LOTXIDDETAIL.PICKDETAILKEY, LOTXIDDETAIL.VALIDATEFLAG, LOTXIDDETAIL.CAPTUREBY, "
				+ "LOTXIDDETAIL.CASEID" + "FROM         LOTXIDHEADER INNER JOIN" + "LOTXIDDETAIL ON LOTXIDHEADER.LOTXIDKEY = LOTXIDDETAIL.LOTXIDKEY"
				+ "WHERE     LOTXIDDETAIL.SOURCEKEY = ? AND LOTXIDDETAIL.SOURCELINENUMBER = ?";
		try
		{
			StateInterface state = ctx.getState();
			RuntimeForm mainPrompt = ctx.getModalBodyForm(0);
			RuntimeFormWidgetInterface dupWidget = mainPrompt.getFormWidgetByName("NUMDUPLICATES");
			String dupWidgetLabel = dupWidget.getLabel(RuntimeFormWidgetInterface.LABEL_LABEL, state.getLocale());
			String numOfDuplicates = dupWidget.getDisplayValue();
			if (StringUtils.isEmpty(numOfDuplicates))
			{
				throw new UserException("WM_EXP_NONNEG", new Object[] { dupWidgetLabel });
			}
			try
			{
				if (Integer.parseInt(numOfDuplicates) < 1)
				{
					throw new UserException("WM_EXP_NONNEG", new Object[] { dupWidgetLabel });
				}
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				_log.error("LOG_ERROR_EXTENSION_CatchWeightSaveNumDuplicatesInSession_execute", StringUtils.getStackTraceAsString(e), SuggestedCategory.NONE);
				throw new UserException("WM_EXP_NONNEG", new Object[] { dupWidgetLabel });
			}
			_log.debug("LOG_DEBUG_EXTENSION_CatchWeightSaveNumDuplicatesInSession_execute", "Number of Duplicates " + numOfDuplicates, SuggestedCategory.NONE);
			SessionUtil.setInteractionSessionAttribute(CatchWeightSaveNumDuplicatesInSession.ASN_CATCH_W_D_NUM_DUPLICATES, numOfDuplicates, state);

		} catch (RuntimeException e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
