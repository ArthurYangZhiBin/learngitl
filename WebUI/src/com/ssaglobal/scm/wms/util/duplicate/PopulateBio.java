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


package com.ssaglobal.scm.wms.util.duplicate;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;

import com.ssaglobal.scm.wms.wm_carrier.ui.PopulateCarrier;
import com.ssaglobal.scm.wms.wm_customer.ui.PopulateCustomer;
import com.ssaglobal.scm.wms.wm_item.ui.PopulateItem;
import com.ssaglobal.scm.wms.wm_owner.ui.PopulateOwner;
import com.ssaglobal.scm.wms.wm_pack.ui.PopulatePack;
import com.ssaglobal.scm.wms.wm_vendor.ui.PopulateVendor;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class PopulateBio extends com.epiphany.shr.ui.action.ActionExtensionBase {


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
   protected static ILoggerCategory _log = LoggerFactory.getInstance(PopulateBio.class);
   protected int execute( ActionContext context, ActionResult result ) throws EpiException {

	   String parameter =super.getParameter("BIO").toString();
	   
	   StateInterface state= context.getState();
	   HttpSession session = state.getRequest().getSession();
	   DataBean dupDataBean=(DataBean) session.getAttribute("dupDataBean");
	   if (dupDataBean==null){
		   _log.debug("LOG_SYSTEM_OUT","Could not find data bean",100L);
		   throw new FormException ("",null);
	   }
	   
	   DataBean tmpDataBean = result.getFocus();
	   QBEBioBean newBioBean;
	   if (!tmpDataBean.isTempBio())
		   throw new FormException("WMEXP_TEMP_NOTFOUND",null);
	   else{
		   newBioBean = (QBEBioBean) tmpDataBean;
		   IDuplicate dupType = null;
		   
		   //Find the right Object
		    if (parameter.equalsIgnoreCase("pack")==true){
		    	dupType = new PopulatePack();
		    } else if(parameter.equalsIgnoreCase("item")){
		    	dupType = new PopulateItem();
		    }else if(parameter.equalsIgnoreCase("owner")){
		    	dupType = new PopulateOwner();
		    }else if (parameter.equalsIgnoreCase("customer")){
		    	dupType = new PopulateCustomer();
		    }else if (parameter.equalsIgnoreCase("vendor")){
		    	dupType = new PopulateVendor();
		    }else if (parameter.equalsIgnoreCase("carrier")){
		    	dupType = new PopulateCarrier();
		    }else{
		    	_log.debug("LOG_SYSTEM_OUT","PopulateBio error, BIO not found",100L);
		    	throw new FormException("WMEXP_UNDEFINED_BIO",null);
		    }
		    
		    //Copy from Old Bio to New one
	    	PopulateBioType populate = dupType.ValidateAndLoad(dupDataBean,newBioBean);
	    	if (populate.getStatus()==false){
	    		_log.debug("LOG_SYSTEM_OUT","Failed to load widget:"+populate.getWidgetName(),100L);
	    		throw new FormException("WMEXP_POPULATE_FAILED",null);
	    	}
	    	else{
	    		newBioBean=populate.getqbeBioBean() ;
	    	}
		    
	   }	   
	   
	   result.setFocus(newBioBean);

	   //_log.debug("LOG_SYSTEM_OUT","jpg end of PopulateBio",100L);
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
