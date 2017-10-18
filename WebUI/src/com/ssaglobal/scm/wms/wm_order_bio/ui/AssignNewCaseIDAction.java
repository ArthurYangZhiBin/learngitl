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


package com.ssaglobal.scm.wms.wm_order_bio.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.servlet.http.HttpSession;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.eai.exception.EAIError;
import com.epiphany.shr.metadata.objects.FormSlot;
import com.epiphany.shr.metadata.objects.ScreenSlot;
import com.epiphany.shr.metadata.objects.Slot;
import com.epiphany.shr.metadata.objects.TabIdentifier;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.action.util.ActionUtil;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.EpnyControllerState;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeForm;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.SsaException;
import com.ssaglobal.scm.wms.datalayer.WmsDataProviderImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.service.baseobjects.RFReturnContext;
import com.ssaglobal.scm.wms.service.exeprocessmanager.TransactionServiceSORemote;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.uiextensions.KeyGenBioWrapper;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class AssignNewCaseIDAction extends ListSelector {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(AssignNewCaseIDAction.class);
	protected static final String internalCaller = "internalCall";
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
	   java.util.HashMap selectedItemsMap = null;	  	  
		  StateInterface state = context.getState();
		  HttpSession session = state.getRequest().getSession();
		  TabIdentifier tab = (TabIdentifier)getParameter("tab identifier");
		  String targetSlotType = (String)getParameter("target slot type");
	      FormSlot fs = (FormSlot)getParameter("form slot");
	      ScreenSlot ss = (ScreenSlot)getParameter("screen slot");
	      boolean cascade = false;
		  ArrayList listForms = new ArrayList();	  	  
		  if(targetSlotType.equalsIgnoreCase("SELF") || targetSlotType.equalsIgnoreCase("PARENT")){
			  selectedItemsMap = getSelectedItemsMap(state, targetSlotType, null, tab, cascade, listForms);
		  }else if(fs != null){
			  selectedItemsMap = getSelectedItemsMap(state, targetSlotType, fs, tab, cascade, listForms);
		  }else if(ss != null){
			  selectedItemsMap = getSelectedItemsMap(state, targetSlotType, ss, tab, cascade, listForms);
		  }
		  if(selectedItemsMap != null)
	          result.setSelectedItems(selectedItemsMap);
	      ArrayList selectedItems = result.getSelectedItems();	
	      Object caseID = session.getAttribute(SetCaseIDInSession.CASEID);	
	      session.removeAttribute(SetCaseIDInSession.CASEID);
  	  if(selectedItems != null && selectedItems.size() > 0 && caseID != null)
        {		  		 
            Iterator bioBeanIter = selectedItems.iterator();    
            UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
            try
            {
                BioBean bio;               
                for(; bioBeanIter.hasNext();){                    	 
                    bio = (BioBean)bioBeanIter.next();  
                    String batchContainerDetail = new KeyGenBioWrapper().getKey("BATCHCONTAINERDETAIL");
                    String pickDetailKey = bio.getString("PICKDETAILKEY").toUpperCase();       
                    String containerDetailId = bio.getString("CONTAINERDETAILID").toUpperCase();                    
                    String sql = "UPDATE LABELCONTAINERDETAIL SET BATCHCONTAINERDETAIL = '" + batchContainerDetail + "' WHERE PICKDETAILKEY = '" + pickDetailKey + "' AND CONTAINERDETAILID = '" + containerDetailId + "'";                   
                    new WmsDataProviderImpl().executeUpdateSql(sql);
                    Array parms = new Array(); 
                    parms.add(new TextData(caseID.toString()));
                    parms.add(new TextData(batchContainerDetail));
                    WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
          		  	actionProperties.setProcedureParameters(parms);
          		  	actionProperties.setProcedureName("NSPNewCaseIdP1S1");          		  	
					EXEDataObject dobj = WmsWebuiActionsImpl.doAction(actionProperties);					       		 
                }
                uowb.saveUOW();
                if(listForms.size() <= 0)
                    listForms = (ArrayList)getTempSpaceHash().get("SELECTED_LIST_FORMS");
                clearBuckets(listForms);
                result.setSelectedItems(null);
            }
            catch(EpiException ex)
            {            	
            	String args[] = new String[0];
				String errorMsg = getTextMessage("ERROR_EXECUTING_ACTION",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
            } 
			catch (Exception e) {				
				throw new UserException(e.getLocalizedMessage(),new Object[0]);
			}
        }	 
  	  else{
  		String args[] = new String[0];							
		String errorMsg = getTextMessage("WMEXP_NOTHING_SELECTED",args,state.getLocale());
		throw new UserException(errorMsg,new Object[0]);
  	  }
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
    	
         return RET_CONTINUE;
    }
}
