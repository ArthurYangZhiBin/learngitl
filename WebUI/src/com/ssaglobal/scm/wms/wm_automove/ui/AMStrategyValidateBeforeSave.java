package com.ssaglobal.scm.wms.wm_automove.ui;

import java.util.ArrayList;

import com.ssaglobal.scm.wms.util.FormUtil;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;

public class AMStrategyValidateBeforeSave extends ActionExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(AMStrategyValidateBeforeSave.class);

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
	protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		Object amCriteria = null;
		Object lengthOftime = null;
		Object timeElement = null;
		Object value1 = null;
		Object value2 = null;
		Object operator	= null;
		try {
			_log.debug("LOG_DEBUG_EXTENSION_ValidateStep","Executing AMStrategyDetailPreRender",100L);
			BioCollectionBean amStrategyDetailCollection = null;
			UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();

			ArrayList tabs = new ArrayList(); 
			tabs.add("automovedetail_normal");
			RuntimeFormInterface detailForm = FormUtil.findForm(context.getState().getCurrentRuntimeForm(), "wms_list_shell", "wm_automove_strategydetail_normal_view", tabs, context.getState());
			DataBean detailFocus = detailForm.getFocus();
			amCriteria = detailFocus.getValue("AMCRITERIA");
			lengthOftime = detailFocus.getValue("LENGTHOFTIME");
			timeElement = detailFocus.getValue("ELEMENT");
			value1 = detailFocus.getValue("VALUE1");
			value2 = detailFocus.getValue("VALUE2");
			operator = detailFocus.getValue("OPERATOR");
		} catch(Exception e) {

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;          
		} 
		if (amCriteria != null){
			if (amCriteria.toString().equalsIgnoreCase("0")){
				if ((lengthOftime == null)||(timeElement == null)){
					throw new UserException("WMEXP_AUTOMV_REQ", new Object[]{});
				}
			}else{
				if ((operator != null)){
					if ((operator.toString().equalsIgnoreCase("BETWEEN"))){
						if ((value1 == null)||(value2 == null)){
							throw new UserException("WMEXP_AUTOMV_REQ1", new Object[]{});
						}
					}else{
						if(value1 == null){
							throw new UserException("WMEXP_AUTOMV_REQ2", new Object[]{});
						}
					}
				}else{
					throw new UserException("WMEXP_AUTOMV_REQ3", new Object[]{});
				}
			}
		}
		return super.execute( context, result );
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
    protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {

       try {
           // Add your code here to process the event
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }
}

