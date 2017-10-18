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
package com.ssaglobal.scm.wms.wm_task_manager_area.ui;

import java.util.ArrayList;
import java.util.Iterator;

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.metadata.objects.FormSlot;
import com.epiphany.shr.metadata.objects.ScreenSlot;
import com.epiphany.shr.metadata.objects.TabIdentifier;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationInsertImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationDeleteImpl;

public class SetDetailsOnDetailDelete extends ListSelector{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(SetDetailsOnDetailDelete.class);
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION_AREA","Executing SetDetailsOnDetailDelete",100L);		
		String key= null;
		RuntimeFormInterface currentRuntimeForm = context.getState().getCurrentRuntimeForm();
		if (currentRuntimeForm != null)
		{
			RuntimeFormInterface parentForm = currentRuntimeForm.getParentForm(context.getState());
			if (parentForm != null)
			{
				RuntimeFormInterface parentParentForm = parentForm.getParentForm(context.getState());
				if (parentParentForm != null)
				{
					String parentParentFormName = parentParentForm.getName();
					if (parentParentFormName != null && parentParentFormName.equals("cti_call_customer_search"))
						return 1;
				}
			}
		}
		String targetSlotType = (String) getParameter("target slot type");
		FormSlot fs = (FormSlot) getParameter("form slot");
		ScreenSlot ss = (ScreenSlot) getParameter("screen slot");
		boolean cascade = getParameterBoolean("cascade", false);
		TabIdentifier tab = (TabIdentifier) getParameter("tab identifier");
		int formNumber = 0;
		StateInterface state = context.getState();
		java.util.HashMap selectedItemsMap = null;
		ArrayList listForms = new ArrayList();
		if (targetSlotType.equals("SELF") || targetSlotType.equals("PARENT"))
			selectedItemsMap = getSelectedItemsMap(state, targetSlotType, null, tab, cascade, listForms);
		else if (fs != null)
			selectedItemsMap = getSelectedItemsMap(state, targetSlotType, fs, tab, cascade, listForms);
		else if (ss != null)
			selectedItemsMap = getSelectedItemsMap(state, targetSlotType, ss, tab, cascade, listForms);
		if (selectedItemsMap != null)
			result.setSelectedItems(selectedItemsMap);
		ArrayList selectedItems = result.getSelectedItems();
		if (selectedItems != null && selectedItems.size() > 0)
		{
			Iterator bioBeanIter = selectedItems.iterator();
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			try
			{
				BioBean bio;
				BioBean detailBio;
				for (; bioBeanIter.hasNext(); bio.delete())
					{bio = (BioBean) bioBeanIter.next();										
					key= bio.getValue("AREAKEY").toString();				
					
					String q= "wm_areadetail.AREAKEY='" +key +"'";
					Query query= new Query("wm_areadetail", q, null);				
					BioCollectionBean newFocus = uowb.getBioCollectionBean(query);
						if(newFocus.size()<1){
							throw new FormException("WMEXP_NO_DETAIL_RECORDS", new Object[1]);
							}
						else
						{
							//delete detail bio
							
							// String val= "" +i;	
							// DataBean focus = (DataBean)newFocus.get(val);
							// detailBio= (BioBean)focus;
							// String detKey= detailBio.getValue("AREAKEY").toString();
							// String zone= detailBio.getValue("PUTAWAYZONE").toString();
							// 
							// detailBio.delete();
							 
							String deleteQuery= "delete from areadetail where areakey=" +"'" +key +"'";
							EXEDataObject dataDeleteObject;
							try {
							dataDeleteObject = WmsWebuiValidationDeleteImpl.delete(deleteQuery);
							} catch (DPException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							}
						}
					
					}
//				
				uowb.saveUOW();
				if (listForms.size() <= 0)
					listForms = (ArrayList) getTempSpaceHash().get("SELECTED_LIST_FORMS");
				clearBuckets(listForms);
				result.setSelectedItems(null);
			} catch (UnitOfWorkException ex)
			{
				
				Throwable nested = ((UnitOfWorkException) ex).getDeepestNestedException();
				//_log.debug("LOG_SYSTEM_OUT","\tNested " + nested.getClass().getName(),100L);
				//_log.debug("LOG_SYSTEM_OUT","\tMessage " + nested.getMessage(),100L);
				
				if(nested instanceof ServiceObjectException)
				{
					String reasonCode = nested.getMessage();
					throwUserException(ex, reasonCode, null);
				}
				else
				{
					throwUserException(ex, "ERROR_DELETING_BIO", null);
				}
				
			} catch (EpiException ex)
			{
				//_log.debug("LOG_SYSTEM_OUT","\n\t" + "IN EPIEXCEPTION" + "\n",100L);
				throwUserException(ex, "ERROR_DELETING_BIO", null);
			}
		}
		else
		{
			throw new UserException("WMEXP_NO_ITEMS_SELECTED", new Object[1]);
		}
		
	//	_log.debug("LOG_SYSTEM_OUT","\n***KEY: " +key,100L);
	//	AreaDataObject obj = new AreaDataObject();
	//	obj.setAreaKey(key);
	//	context.getServiceManager().getUserContext().put("formChoice", obj);
		
		_log.debug("LOG_DEBUG_EXTENSION_AREA","Exiting SetDetailsOnDetailDelete",100L);
		return 0;
	}
}


/*	
 * 
 *
 * 		_log.debug("LOG_SYSTEM_OUT","\n\nAfterDELETE",100L);
	
	
				
				*/
