package com.ssaglobal.scm.wms.wm_inventory_move.action;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;


public class AutoMoveConfirmAction extends com.epiphany.shr.ui.action.ActionExtensionBase {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(AutoMoveConfirmAction.class);
	protected int execute(ModalActionContext context, ActionResult result) throws EpiException {
		_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Action1 \n\n",100L);
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		if (context.getSourceWidget().getName().equals("YES")){
			userContext.put("moveContinue", "YES");
		}else{
			userContext.put("moveContinue", "NO");
		}
		
		return RET_CONTINUE;
	}	
}
