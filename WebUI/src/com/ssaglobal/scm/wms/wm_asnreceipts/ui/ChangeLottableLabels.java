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
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class ChangeLottableLabels extends com.epiphany.shr.ui.action.ActionExtensionBase {


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
   protected static ILoggerCategory _log = LoggerFactory.getInstance(ChangeLottableLabels.class);
   protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		final String TopLevelForm = getParameterString("TopLevelForm");
		RuntimeFormInterface LottablesDetailFrom = state.getCurrentRuntimeForm();
		DataBean detailFocus = LottablesDetailFrom.getFocus();
		RuntimeFormInterface headerForm = FormUtil.findForm(LottablesDetailFrom.getParentForm(state),"",TopLevelForm,state);
		_log.debug("LOG_SYSTEM_OUT","HEADER FORM = "+ headerForm.getName(),100L);
		DataBean headerFocus = headerForm.getFocus();
		Object skuObj = detailFocus.getValue("SKU");
		Object storerObject = detailFocus.getValue("STORERKEY");
		if (skuObj != null && storerObject != null){
			BioCollectionBean listCollection = null;
			String skuVal = skuObj.toString().trim();
			String storerKey = storerObject.toString().trim();
			_log.debug("LOG_SYSTEM_OUT","SKU ="+ skuObj.toString()+ "End",100L);
			String sQueryString = "(wm_sku.STORERKEY = '"+storerKey+"' AND wm_sku.SKU = '"+skuVal+"')";
			_log.debug("LOG_SYSTEM_OUT","sQueryString = "+ sQueryString,100L);
			Query bioQuery = new Query("wm_sku",sQueryString,null);
			listCollection = uowb.getBioCollectionBean(bioQuery);
			BioBean skuBio = (BioBean)listCollection.elementAt(0);
			LottablesDetailFrom.getFormWidgetByName("LOTTABLE01").setLabel("label",skuBio.getString("LOTTABLE01LABEL")+":");
			LottablesDetailFrom.getFormWidgetByName("LOTTABLE02").setLabel("label",skuBio.getString("LOTTABLE02LABEL")+":");
			LottablesDetailFrom.getFormWidgetByName("LOTTABLE03").setLabel("label",skuBio.getString("LOTTABLE03LABEL")+":");
			LottablesDetailFrom.getFormWidgetByName("LOTTABLE04").setLabel("label",skuBio.getString("LOTTABLE04LABEL")+":");
			LottablesDetailFrom.getFormWidgetByName("LOTTABLE05").setLabel("label",skuBio.getString("LOTTABLE05LABEL")+":");
			LottablesDetailFrom.getFormWidgetByName("LOTTABLE06").setLabel("label",skuBio.getString("LOTTABLE06LABEL")+":");
			LottablesDetailFrom.getFormWidgetByName("LOTTABLE07").setLabel("label",skuBio.getString("LOTTABLE07LABEL")+":");
			LottablesDetailFrom.getFormWidgetByName("LOTTABLE08").setLabel("label",skuBio.getString("LOTTABLE08LABEL")+":");
			LottablesDetailFrom.getFormWidgetByName("LOTTABLE09").setLabel("label",skuBio.getString("LOTTABLE09LABEL")+":");
			LottablesDetailFrom.getFormWidgetByName("LOTTABLE10").setLabel("label",skuBio.getString("LOTTABLE10LABEL")+":");
			LottablesDetailFrom.getFormWidgetByName("LOTTABLE11").setLabel("label",skuBio.getString("LOTTABLE11LABEL")+":");
			LottablesDetailFrom.getFormWidgetByName("LOTTABLE12").setLabel("label",skuBio.getString("LOTTABLE12LABEL")+":");		
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
