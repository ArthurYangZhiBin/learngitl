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

package com.ssaglobal.scm.wms.wm_receiptreversal.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.view.View;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ReceiptReversalResetFilter extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	private String CONTEXT_FILTER = "HeaderDetailFilter";

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ReceiptReversalResetFilter.class);

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
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		//Unique Context Variable
		
		
		StateInterface state = context.getState();
		_log.debug("LOG_DEBUG_EXTENSION_HeaderDetailResetFilter", "ID: " + state.getInteractionId(), SuggestedCategory.NONE);
		CONTEXT_FILTER += state.getInteractionId();
		_log.debug("LOG_DEBUG_EXTENSION_HeaderDetailResetFilter", "Context: " + CONTEXT_FILTER, SuggestedCategory.NONE);
		RuntimeFormInterface form = state.getCurrentRuntimeForm();
		EpnyUserContext userContext = context.getServiceManager().getUserContext();
		if (form.isListForm())
		{
			//Extension called from List Form - save the current filter into the usercontext
			_log.debug("LOG_DEBUG_EXTENSION_HeaderDetailResetFilter", "HeaderDetailResetFilter called from List Form", SuggestedCategory.NONE);
			QBEBioBean filter = ((RuntimeListForm) form).getFilterRowBean();
			View vw = View.createView(filter.getQueryBioStub());
			String viewData = vw.getRefString();
			userContext.put(CONTEXT_FILTER, viewData);
			_log.debug("LOG_DEBUG_EXTENSION_HeaderDetailResetFilter", "Saving the Filter " + vw.toString() + ":"
					+ viewData + " into context ", SuggestedCategory.NONE);
			return RET_CONTINUE;
		}
		else
		{
			//Parameters needed for all queries
			String orderBy = isEmpty(getParameterString("ORDERBYCLAUSE")) ? null : getParameterString("ORDERBYCLAUSE");
			String query = isEmpty(getParameterString("QUERY_STRING")) ? null : getParameterString("QUERY_STRING");
			
			//UPDATE: Defect 282270 - Unable to view records in Oracle environments
			query = "DPE('SQL','@[wm_receiptreversal.ADJUSTMENTKEY] in (SELECT DISTINCT ADJUSTMENTKEY FROM ADJUSTMENTDETAIL WHERE REASONCODE = \\'UNRECEIVE\\')')";
			//END UPDATE			
			
			_log.debug("LOG_DEBUG_EXTENSION_HeaderDetailResetFilter", "Parameters: " + "OrderBy " + orderBy
					+ "\n Query " + query, SuggestedCategory.NONE);

			//Extension called from Normal Form (toolbar) - load the filter from the usercontext
			_log.debug("LOG_DEBUG_EXTENSION_HeaderDetailResetFilter", "HeaderDetailResetFilter called from Normal Form", SuggestedCategory.NONE);
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			String viewData = (String) userContext.get(CONTEXT_FILTER);
			if (viewData == null)
			{
				_log.debug("LOG_DEBUG_EXTENSION_HeaderDetailResetFilter", "View is lost", SuggestedCategory.NONE);
				//If viewData is lost from the userContext, return all records
				String bioType;
				if (form.getFocus() == null)
				{
					_log.debug("LOG_DEBUG_EXTENSION_HeaderDetailResetFilter", "Form Focus is null, getting from Parameter", SuggestedCategory.NONE);
					bioType = getParameterString("LISTBIO");
					_log.debug("LOG_DEBUG_EXTENSION_HeaderDetailResetFilter", "BioType " + bioType, SuggestedCategory.NONE);
				}
				else
				{
					bioType = form.getFocus().getDataType();
					_log.debug("LOG_DEBUG_EXTENSION_HeaderDetailResetFilter", "BioType " + bioType, SuggestedCategory.NONE);
				}
				BioCollectionBean collection = (BioCollectionBean) uowb.getBioCollectionBean(new Query(bioType, query, orderBy));
				result.setFocus(collection);
				return RET_CONTINUE;
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION_HeaderDetailResetFilter", "Retrieved: " + viewData, SuggestedCategory.NONE);
			}

			View vw = View.createViewFromRefString(viewData);
			QBEBioBean filter = uowb.getQBEBio(vw.getHelperBio());

			//Test to see if the context is messing up
			String formBioType = form.getFocus() == null ? getParameterString("LISTBIO")
					: form.getFocus().getDataType();
			String viewBioType = filter.getDataType();
			_log.debug("LOG_DEBUG_EXTENSION_HeaderDetailResetFilter", "Form Bio Type: " + formBioType
					+ " and View Bio Type: " + viewBioType, SuggestedCategory.NONE);
			if (!formBioType.equals(viewBioType))
			{
				_log.debug("LOG_DEBUG_EXTENSION_HeaderDetailResetFilter", "Form and View Bio Types do not match, returning empty Query", SuggestedCategory.NONE);
				BioCollectionBean collection = (BioCollectionBean) uowb.getBioCollectionBean(new Query(formBioType, query, orderBy));
				result.setFocus(collection);
				return RET_CONTINUE;
			}

			//Requery and set filter
			boolean showEmptyList = getParameterBoolean("SHOWEMPTYLIST");
			BioCollectionBean collection = (BioCollectionBean) uowb.getBioCollectionBean(new Query(filter.getDataType(), query, orderBy));
			_log.debug("LOG_DEBUG_EXTENSION_HeaderDetailResetFilter", "Created QBEBioBean of Type:"
					+ collection.getBioTypeName() + "..." + "Size " + collection.size(), SuggestedCategory.NONE);
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
				_log.debug("LOG_DEBUG_EXTENSION_HeaderDetailResetFilter", "Showing Empty List", SuggestedCategory.NONE);
				newCollection.setEmptyList(true);
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION_HeaderDetailResetFilter", "Not Showing Empty List", SuggestedCategory.NONE);
				newCollection.setEmptyList(false);
			}
			result.setFocus(newCollection);
			return RET_CONTINUE;
		}
	}

	protected boolean isEmpty(Object attributeValue)
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
}
