package com.ssaglobal.scm.wms.wm_task_details.ui;

//Import 3rd party packages and classes
import java.util.ArrayList;
import java.util.Iterator;

// Import Epiphany packages and classes
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

public class TaskDisableEnableBasedOnAM extends com.epiphany.shr.ui.view.customization.FormExtensionBase {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(TaskDisableEnableBasedOnAM.class);

	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {
		_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of TaskDisableEnableBasedOnAM", SuggestedCategory.NONE);
		try	{
			StateInterface state = context.getState();
			DataBean currentFormFocus = state.getFocus();
			if(currentFormFocus instanceof BioBean) {
				currentFormFocus = (BioBean) currentFormFocus;
			}

			Object amStrategyKey = currentFormFocus.getValue("AMSTRATEGEYKEY");
			Object amStepNum = currentFormFocus.getValue("STEP");
			Object taskStatus = currentFormFocus.getValue("STATUS");
			if (((amStrategyKey != null) && (amStepNum != null))&&
				( taskStatus.toString().equalsIgnoreCase("S")||taskStatus.toString().equalsIgnoreCase("0"))){
				form.getFormWidgetByName("RELEASEDATE").setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
			}
		} catch(Exception e) {
			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}
		return RET_CONTINUE;
	}

	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) throws EpiException {
		try	{
			// Add your code here to process the event
		} catch(Exception e) {
			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}
		return RET_CONTINUE;
	}
}