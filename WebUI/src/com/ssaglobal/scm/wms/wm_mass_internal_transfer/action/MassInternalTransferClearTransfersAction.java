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

package com.ssaglobal.scm.wms.wm_mass_internal_transfer.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.EpiRuntimeException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.FormUtil;

/*
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected to be
 * provided from the meta <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class MassInternalTransferClearTransfersAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(MassInternalTransferClearTransfersAction.class);

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
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{

		_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of MassInternalTransferClearTransfersAction", SuggestedCategory.NONE);

		StateInterface state = context.getState();

		UnitOfWorkBean uow = state.getDefaultUnitOfWork();

		RuntimeListFormInterface imListForm = (RuntimeListFormInterface) FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_internal_transfer_mass_list_view", state);

		QBEBioBean filter = ((RuntimeListForm) imListForm).getFilterRowBean();
		boolean showEmptyList = getParameterBoolean("SHOWEMPTYLIST");
		String query_string = getParameterString("QUERY_STRING");
		BioCollectionBean collection = uow.getBioCollectionBean(new Query(filter.getDataType(), query_string, null));
		_log.debug("LOG_DEBUG_EXTENSION_InternalTransferMassResetFilter", "Created QBEBioBean of Type:" + collection.getBioTypeName() + "..." + "Size " + collection.size(), SuggestedCategory.NONE);

		BioCollectionBean newCollection = (BioCollectionBean) collection.filter(filter.getQueryWithWildcards());
		BioCollectionBean newCollectionB = (BioCollectionBean) collection.filter(filter.getQueryWithWildcards());
		newCollectionB.copyFrom(collection);
		collection.copyFrom(newCollection);
		newCollection = collection;
		filter.setBaseBioCollectionForQuery(newCollectionB);
		newCollection.setQBEBioBean(filter);
		newCollection.filterInPlace(filter.getQueryWithWildcards());

		if (showEmptyList == true)
		{
			_log.debug("LOG_DEBUG_EXTENSION_InternalTransferMassResetFilter", "Showing Empty List", SuggestedCategory.NONE);
			newCollection.setEmptyList(true);
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION_InternalTransferMassResetFilter", "Not Showing Empty List", SuggestedCategory.NONE);
			newCollection.setEmptyList(false);
		}

		result.setFocus(newCollection);
		imListForm.setSelectedItems(null);

		return RET_CONTINUE;
	}

	private boolean isEmpty(Object attributeValue)
	{

		if (attributeValue == null)
		{
			return true;
		}
		else if (attributeValue.toString().matches("\\s*"))
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	private boolean isNull(Object attributeValue)
	{

		if (attributeValue == null)
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	private QBEBioBean createNewQBE(StateInterface state, String bioType)
	{
		UnitOfWorkBean tempUowb = state.getDefaultUnitOfWork();
		QBEBioBean qbe;
		try
		{
			qbe = tempUowb.getQBEBio(bioType);
		} catch (DataBeanException ex)
		{
			Object args[] = { bioType };
			throw new EpiRuntimeException("EXP_INVALID_QUERY_TYPE_QACTION", "A QBE Bio could not be created for bio type {0}", args);
		}
		return qbe;
	}

}
