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
import java.util.Iterator;
import java.util.StringTokenizer;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.GenericException;
import com.agileitp.forte.framework.TextData;
import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.metadata.objects.FormSlot;
import com.epiphany.shr.metadata.objects.ScreenSlot;
import com.epiphany.shr.metadata.objects.TabIdentifier;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.SsaException;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.exeprocessmanager.TransactionServiceSORemote;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.EJBRemote;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class FilterPicksByAreaKey extends ActionExtensionBase {
	
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
	  StateInterface state = context.getState();
	  //RuntimeListFormInterface listForm = (RuntimeListFormInterface) FormUtil.findForm(state.getCurrentRuntimeForm(), null, "wm_unallocate_pick_details_list_view", state);
	  DataBean focusObj = result.getFocus();
	  
	  //if(listForm == null)
		 // return RET_CONTINUE;      
	  if(focusObj == null)
		  return RET_CONTINUE;
	  if(!focusObj.isBioCollection())
		  return RET_CONTINUE;
	  
	  BioCollectionBean focus = (BioCollectionBean)focusObj;
	 // QBEBioBean filterRow = ((RuntimeListForm)listForm).getFilterRowBean();
	  
	  //if(filterRow == null)
		 // return RET_CONTINUE;
	  
	  String areaKey = (String)state.getRequest().getSession().getAttribute("AREAKEY");
	  
	  if(areaKey == null || areaKey.length() == 0)
		  return RET_CONTINUE;
	  
	  state.getRequest().getSession().removeAttribute("AREAKEY");
	  UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();	
	  
	  //Get Area Detail records for this area key
	  Query loadBiosQry = new Query("wm_areadetail", "wm_areadetail.AREAKEY = '"+areaKey.toUpperCase()+"'", "");
	  BioCollection areaDetailsCollection = uow.getBioCollectionBean(loadBiosQry);
	  if(areaDetailsCollection == null || areaDetailsCollection.size() == 0)
		  return RET_CONTINUE;
	  
	  //Get Locations related to the Area Details by PUTAWAYZONE
	  String qryStr = "";
	  for(int i = 0; i < areaDetailsCollection.size(); i++){
		  Bio record = areaDetailsCollection.elementAt(i);
		  String putawayZone = (String)record.get("PUTAWAYZONE");
		  if(putawayZone != null && putawayZone.length() > 0){
			  if(qryStr.length() == 0)
				  qryStr += " wm_location.PUTAWAYZONE = '"+putawayZone+"' ";
			  else
				  qryStr += " OR wm_location.PUTAWAYZONE = '"+putawayZone+"' ";
		  }
	  }
	  loadBiosQry = new Query("wm_location", qryStr, "");
	  BioCollection locationCollection = uow.getBioCollectionBean(loadBiosQry);
	  if(locationCollection == null || locationCollection.size() == 0)
		  return RET_CONTINUE;
	  
	  //Filter by Locations
	  qryStr = "";
	  for(int i = 0; i < locationCollection.size(); i++){
		  Bio record = locationCollection.elementAt(i);
		  String location = (String)record.get("LOC");
		  if(location != null && location.length() > 0){
			  if(qryStr.length() == 0)
				  qryStr += " wm_pickdetail.LOC = '"+location+"' ";
			  else
				  qryStr += " OR wm_pickdetail.LOC = '"+location+"' ";
		  }
	  }
	  loadBiosQry = new Query("wm_pickdetail", qryStr, "");
	  focus.filterInPlace(loadBiosQry);
	  result.setFocus(focus);
	  return RET_CONTINUE;
   }
      
}
