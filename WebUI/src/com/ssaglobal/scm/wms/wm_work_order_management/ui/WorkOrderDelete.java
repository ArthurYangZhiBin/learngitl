package com.ssaglobal.scm.wms.wm_work_order_management.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.metadata.objects.FormSlot;
import com.epiphany.shr.metadata.objects.ScreenSlot;
import com.epiphany.shr.metadata.objects.TabIdentifier;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationDeleteImpl;

public class WorkOrderDelete extends ListSelector{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WorkOrderDelete.class);
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
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
			
			HashSet groupIds = new HashSet();

			try
			{
				BioBean bio;
				for (; bioBeanIter.hasNext(); bio.delete()){
					bio = (BioBean) bioBeanIter.next();
					groupIds.add(bio.get("GROUPID").toString());
				}
				uowb.saveUOW();
				this.deleteGroupIds(uowb, groupIds);
				if (listForms.size() <= 0)
					listForms = (ArrayList) getTempSpaceHash().get("SELECTED_LIST_FORMS");
				clearBuckets(listForms);
				result.setSelectedItems(null);
			} catch (UnitOfWorkException ex)
			{
				_log.error("LOG_ERROR_EXTENSION_WorkOrderDelete_UOWException", ex.getStackTraceAsString(), SuggestedCategory.NONE);

				Throwable nested = ((UnitOfWorkException) ex).getDeepestNestedException();
				
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
				_log.error("LOG_ERROR_EXTENSION_WorkOrderDelete_EpiException", ex.getStackTraceAsString(), SuggestedCategory.NONE);
				throwUserException(ex, "ERROR_DELETING_BIO", null);
			}
			
			
		}
		return 0;
	}
	
	
	private void deleteGroupIds(UnitOfWorkBean uowb, HashSet groupIds) throws EpiException{
		int groupId = 0;
		Iterator iterator = groupIds.iterator();
		while(iterator.hasNext()){
			groupId = Integer.parseInt(iterator.next().toString());
			Query query = new Query("wm_workorder", "wm_workorder.GROUPID="+groupId, null);
			BioCollectionBean collection = uowb.getBioCollectionBean(query);
			int size = collection.size();
			if(size == 0){
				String qry = " DELETE FROM WOGROUP WHERE GROUPID="+groupId;
				WmsWebuiValidationDeleteImpl.delete(qry);
			}
			
		}		

	}
	
	
	
	
	
	
	
	
}
