package com.infor.scm.waveplanning.wp_saved_filters.action;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;

public class FilterListViewReset extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(FilterListViewReset.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		_log.debug("LOG_DEBUG_EXTENSION_LISTVIEW","Executing FilterListViewReset",100L);			
		StateInterface state = context.getState();	
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		uow.clearState();
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		String wmWhseID = (userContext.get(SetIntoHttpSessionAction.DB_USERID)).toString();
		Query qry = new Query("wp_filter","wp_filter.FACILITY = '"+wmWhseID+"'","");
		BioCollectionBean bc = uow.getBioCollectionBean(qry);
		result.setFocus(bc);
		return RET_CONTINUE;
	}

}
