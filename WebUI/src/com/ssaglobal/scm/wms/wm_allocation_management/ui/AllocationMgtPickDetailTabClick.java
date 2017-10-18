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


package com.ssaglobal.scm.wms.wm_allocation_management.ui;

//Import 3rd party packages and classes

//Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.impl.ArrayListBioRefSupplier;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class AllocationMgtPickDetailTabClick extends com.epiphany.shr.ui.action.ActionExtensionBase {
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(AllocationMgtPickDetailTabClick.class);
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
		_log.debug("LOG_DEBUG_EXTENSION_ALLOCINVACTION","Executing AllocateInventoryAction",100L);			
		StateInterface state = context.getState();	
		RuntimeListFormInterface pickDetailListForm = (RuntimeListFormInterface)state.getCurrentRuntimeForm();
		RuntimeListFormInterface detailListForm = (RuntimeListFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_allocation_management_detail_list_view",state);
		RuntimeFormInterface detailForm = (RuntimeFormInterface)FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_allocation_management_header_detail_view",state);
		if(detailListForm == null || pickDetailListForm == null || detailForm == null)
			return RET_CONTINUE;
		if(detailListForm.getAllSelectedItems() == null || detailListForm.getAllSelectedItems().size() == 0){
			//Empty Out The Bio Collection
			ArrayListBioRefSupplier tempBioCollRefArray = new ArrayListBioRefSupplier("wm_pickdetail");	
			BioCollection bc = (state.getDefaultUnitOfWork().getUOW()).fetchBioCollection(tempBioCollRefArray);								
			DataBean db = (DataBean)(state.getDefaultUnitOfWork().getBioCollection(bc.getBioCollectionRef()));					
			pickDetailListForm.setFocus(db);
		}
		else{
			String sku = (String)detailForm.getFocus().getValue("SKU");
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			String criteria = "wm_pickdetail.SKU = '"+sku+"' AND (";			
			ArrayList selectedItems = detailListForm.getAllSelectedItems();
			for(int i = 0; i < selectedItems.size(); i++){
				String orderKey = (String)((Bio)selectedItems.get(i)).get("ORDERKEY");				
				if(i == 0)
					criteria += " wm_pickdetail.ORDERKEY = '"+orderKey+"' ";
				else
					criteria += " OR wm_pickdetail.ORDERKEY = '"+((Bio)selectedItems.get(i)).get("ORDERKEY")+"' ";
			}
			criteria += ")";
			_log.debug("LOG_SYSTEM_OUT","\n\n\n\n Criteria:"+criteria+"\n\n\n",100L);
			Query qry = new Query("wm_pickdetail",criteria,"");
			pickDetailListForm.setFocus(uow.getBioCollectionBean(qry));
		}
		return RET_CONTINUE;
		
	}		
}
