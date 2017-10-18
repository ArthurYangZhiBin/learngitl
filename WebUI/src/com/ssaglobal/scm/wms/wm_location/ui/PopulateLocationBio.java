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


package com.ssaglobal.scm.wms.wm_location.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import javax.servlet.http.HttpSession;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class PopulateLocationBio extends com.epiphany.shr.ui.action.ActionExtensionBase {


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

      // Replace the following line with your code,
      // returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
      // as appropriate

	   StateInterface state= context.getState();
	   HttpSession session = state.getRequest().getSession();
	   DataBean locationDataBean=(DataBean) session.getAttribute("locationDataBean");
	   
	   //_log.debug("LOG_SYSTEM_OUT","jpuente LOCATION:"+locationDataBean.getValue("LOC"),100L);
	   //UnitOfWorkBean uob = context.getState().getDefaultUnitOfWork();
	   //BioBean newLocationBioBean = (BioBean) detailForm.getFocus();
	   QBEBioBean newLocationBioBean = (QBEBioBean) result.getFocus();
	   
	   newLocationBioBean.set("ABC", locationDataBean.getValue("ABC"));
	   newLocationBioBean.set("COMMINGLELOT", locationDataBean.getValue("COMMINGLELOT"));
	   newLocationBioBean.set("COMMINGLESKU", locationDataBean.getValue("COMMINGLESKU"));
	   if (locationDataBean.getValue("CUBE")!=null && locationDataBean.getValue("CUBE")!="")
		   newLocationBioBean.set("CUBE", locationDataBean.getValue("CUBE"));
	   
	   newLocationBioBean.set("CUBICCAPACITY", locationDataBean.getValue("CUBICCAPACITY"));
	   newLocationBioBean.set("FACILITY", locationDataBean.getValue("FACILITY"));
	   if (locationDataBean.getValue("FootPrint")!=null && locationDataBean.getValue("FootPrint")!="")
		   newLocationBioBean.set("FootPrint", locationDataBean.getValue("FootPrint"));
	   
       newLocationBioBean.set("HEIGHT", locationDataBean.getValue("HEIGHT"));
       newLocationBioBean.set("LENGTH", locationDataBean.getValue("LENGTH"));
       newLocationBioBean.set("LOC", "");
	   //newLocationBioBean.set("LOCATION_TO_AREADETAIL", locationDataBean.getValue("LOCATION_TO_AREADETAIL"));
	   newLocationBioBean.set("LOCATIONCATEGORY", locationDataBean.getValue("LOCATIONCATEGORY"));
	   newLocationBioBean.set("LOCATIONFLAG", locationDataBean.getValue("LOCATIONFLAG"));
	   newLocationBioBean.set("LOCATIONHANDLING", locationDataBean.getValue("LOCATIONHANDLING"));
	   newLocationBioBean.set("LOCATIONTYPE", locationDataBean.getValue("LOCATIONTYPE"));
	   newLocationBioBean.set("LOCGROUPID", locationDataBean.getValue("LOCGROUPID"));
	   if (locationDataBean.getValue("LOCLEVEL")!=null && locationDataBean.getValue("LOCLEVEL")!="")
		   newLocationBioBean.set("LOCLEVEL", locationDataBean.getValue("LOCLEVEL"));
	   //newLocationBioBean.set("LOGICALLOCATION", locationDataBean.getValue("LOGICALLOCATION"));
	   newLocationBioBean.set("LOGICALLOCATION", "");
	   newLocationBioBean.set("LOSEID", locationDataBean.getValue("LOSEID"));
	   newLocationBioBean.set("OPTLOC", locationDataBean.getValue("OPTLOC"));
	   
	   if (locationDataBean.getValue("PICKMETHOD")!=null && locationDataBean.getValue("PICKMETHOD")!="")
		   newLocationBioBean.set("PICKMETHOD", locationDataBean.getValue("PICKMETHOD"));
	   
	   if (locationDataBean.getValue("PICKZONE")!=null && locationDataBean.getValue("PICKZONE")!="")
		   newLocationBioBean.set("PICKZONE", locationDataBean.getValue("PICKZONE"));
	   
	   if (locationDataBean.getValue("PUTAWAYZONE")!=null && locationDataBean.getValue("PUTAWAYZONE")!="")
		   newLocationBioBean.set("PUTAWAYZONE", locationDataBean.getValue("PUTAWAYZONE"));
	   
	   if (locationDataBean.getValue("SECTION")!=null && locationDataBean.getValue("SECTION")!="")
		   newLocationBioBean.set("SECTION", locationDataBean.getValue("SECTION"));
	   
	   if (locationDataBean.getValue("SECTIONKEY")!=null && locationDataBean.getValue("SECTIONKEY")!="")
		   newLocationBioBean.set("SECTIONKEY", locationDataBean.getValue("SECTIONKEY"));
	   
	   //newLocationBioBean.set("SERIALKEY", locationDataBean.getValue("SERIALKEY"));
	   if (locationDataBean.getValue("StackLimit")!=null && locationDataBean.getValue("StackLimit")!="")
		   newLocationBioBean.set("StackLimit", locationDataBean.getValue("StackLimit"));
	   
	   if (locationDataBean.getValue("STATUS")!=null && locationDataBean.getValue("STATUS")!="")
		   newLocationBioBean.set("STATUS", locationDataBean.getValue("STATUS"));
	   
	   newLocationBioBean.set("WEIGHTCAPACITY", locationDataBean.getValue("WEIGHTCAPACITY"));
	   newLocationBioBean.set("WHSEID", locationDataBean.getValue("WHSEID"));
	   newLocationBioBean.set("WIDTH", locationDataBean.getValue("WIDTH"));
	   newLocationBioBean.set("WEIGHTCAPACITY", locationDataBean.getValue("WEIGHTCAPACITY"));
	   if (locationDataBean.getValue("XCOORD")!=null && locationDataBean.getValue("XCOORD")!="")
		   newLocationBioBean.set("XCOORD", locationDataBean.getValue("XCOORD"));
	   if (locationDataBean.getValue("YCOORD")!=null && locationDataBean.getValue("YCOORD")!="")
		   newLocationBioBean.set("YCOORD", locationDataBean.getValue("YCOORD"));
	   if (locationDataBean.getValue("ZCOORD")!=null && locationDataBean.getValue("ZCOORD")!="")
		   newLocationBioBean.set("ZCOORD", locationDataBean.getValue("ZCOORD"));
	   
	   
       //_log.debug("LOG_SYSTEM_OUT","jpuente Assigend values",100L);
       result.setFocus(newLocationBioBean);


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
