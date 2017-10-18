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

package com.ssaglobal.scm.wms.wm_automove.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.data.bio.Query;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class ValidateAMStrategyWidgets extends com.epiphany.shr.ui.action.ActionExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateAMStrategyWidgets.class);

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
		
		_log.debug("LOG_DEBUG_EXTENSION_ValidateStep","Executing ValidateStepandDisable",100L);
		RuntimeFormInterface detailform = context.getState().getCurrentRuntimeForm();
		RuntimeFormInterface headerForm = FormUtil.findForm(context.getState().getCurrentRuntimeForm(), "wms_list_shell", "wm_automove_strategy_normal_view", context.getState());
		DataBean detailFocus = detailform.getFocus();
		
		UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();
		BioCollectionBean amStrategyDetailCollection = null;
		if (context.getSourceWidget().getName().equals("STEPNUMBER")){
			
			String stepNum = context.getSourceWidget().getDisplayValue().toString();
			String amStrategyKey = headerForm.getFocus().getValue("AMSTRATEGYKEY").toString();
			
			String queryString = "wm_amstrategydetail.AMSTRATEGYKEY= '"+amStrategyKey+"' and wm_amstrategydetail.STEPNUMBER= '"+stepNum+"'";
			Query query = new Query("wm_amstrategydetail",queryString,null);
			
			amStrategyDetailCollection = uowb.getBioCollectionBean(query);
			if (amStrategyDetailCollection.size()>= 1 ){
				detailFocus.setValue("LENGTHOFTIME", amStrategyDetailCollection.get("0").get("LENGTHOFTIME"));
				detailFocus.setValue("ELEMENT", amStrategyDetailCollection.get("0").get("ELEMENT"));
				detailFocus.setValue("PUTAWAYSTRATEGYKEY", amStrategyDetailCollection.get("0").get("PUTAWAYSTRATEGYKEY"));
				detailFocus.setValue("TASKTYPE", amStrategyDetailCollection.get("0").get("TASKTYPE"));
				
				detailform.getFormWidgetByName("LENGTHOFTIME").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
				detailform.getFormWidgetByName("ELEMENT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
				detailform.getFormWidgetByName("PUTAWAYSTRATEGYKEY").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
				detailform.getFormWidgetByName("TASKTYPE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
			}else{
				detailform.getFormWidgetByName("LENGTHOFTIME").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
				detailform.getFormWidgetByName("ELEMENT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
				detailform.getFormWidgetByName("PUTAWAYSTRATEGYKEY").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
				detailform.getFormWidgetByName("TASKTYPE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
			}
		}
		else if (context.getSourceWidget().getName().equals("AMCRITERIA")){
			String amCriteria = context.getSourceWidget().getValue().toString();
			if(amCriteria.equalsIgnoreCase("0")){
				detailFocus.setValue("OPERATOR", null);
				detailFocus.setValue("VALUE1", null);
				detailFocus.setValue("VALUE2", null);
				
				detailform.getFormWidgetByName("OPERATOR").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
				detailform.getFormWidgetByName("VALUE1").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
				detailform.getFormWidgetByName("VALUE2").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
			}else{
				detailform.getFormWidgetByName("OPERATOR").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
				detailform.getFormWidgetByName("VALUE1").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
				detailform.getFormWidgetByName("VALUE2").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
			}
		}
		else if (context.getSourceWidget().getName().equals("OPERATOR")){
			String operator = context.getSourceWidget().getValue().toString();
			if(!(operator.equalsIgnoreCase("BETWEEN"))){
				detailform.getFormWidgetByName("VALUE2").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
			}else{
				detailform.getFormWidgetByName("VALUE2").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.FALSE);
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
