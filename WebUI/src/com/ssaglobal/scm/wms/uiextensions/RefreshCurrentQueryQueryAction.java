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

package com.ssaglobal.scm.wms.uiextensions;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.HelperBio;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.EpiRuntimeException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class RefreshCurrentQueryQueryAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(RefreshCurrentQueryQueryAction.class);
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

		String listForm = getParameterString("list_form");
		String detailForm = getParameterString("detail_form");
		String shellForm = (getParameter("shell_form") == null) ? "wms_list_shell" : getParameterString("shell_form");

		StateInterface state = context.getState();
		RuntimeListFormInterface list = (RuntimeListFormInterface) FormUtil.findForm(state.getCurrentRuntimeForm(),
																						shellForm, listForm, state);

		BioCollectionBean listFocus = (BioCollectionBean) list.getFocus();
		_log.debug("LOG_SYSTEM_OUT","Bean size " + listFocus.size(),100L);
		Query originalQuery = listFocus.getQuery();
		_log.debug("LOG_SYSTEM_OUT","Original Query " + originalQuery.getQueryExpression(),100L);
		listFocus.filterInPlace(originalQuery);

		//if the header is blank, create a new biobean to display in the list
		if (listFocus.size() == 0)
		{
			_log.debug("LOG_SYSTEM_OUT","Trying to set results",100L);
			RuntimeFormInterface detail = FormUtil.findForm(state.getCurrentRuntimeForm(), shellForm, detailForm, state);
			_log.debug("LOG_SYSTEM_OUT",detailForm,100L);
			_log.debug("LOG_SYSTEM_OUT",detail.getName(),100L);
			DataBean detailFocus = detail.getFocus();
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			BioCollectionBean currentCollection;

			if (originalQuery.getQueryExpression() != null)
			{
				//if an empty header list is the result of a bad query, get a new collection
				_log.debug("LOG_SYSTEM_OUT","Getting new collection",100L);
				Query newQuery = new Query(originalQuery.getBioTypeName(), "", null);
				currentCollection = uow.getBioCollectionBean(newQuery);
			}
			else
			{
				//if it's because of an initial empty list, re-use collection
				currentCollection = (BioCollectionBean) list.getFocus();
			}

			QBEBioBean bio = createNewQBE(state, currentCollection.getBioTypeName());
			// Set Filter Row
			ArrayList primaryKey = (ArrayList) getParameter("primary_key");
			for (Iterator it = primaryKey.iterator(); it.hasNext();)
			{
				String key = it.next().toString();
				bio.set(key, detailFocus.getValue(key));
			}
			//
			_log.debug("LOG_SYSTEM_OUT",bio.getQueryWithWildcards().getQueryExpression(),100L);
			//System.out.println("Created QBEBioBean of Type:" + currentCollection.getBioTypeName() + "..." + "Size "
			//		+ currentCollection.size());
			_log.debug("LOG_SYSTEM_OUT","Created QBEBioBean of Type:" + currentCollection.getBioTypeName() + "..." + "Size " + currentCollection.size(),100L);
			BioCollectionBean newCollection = (BioCollectionBean) currentCollection.filter(bio.getQueryWithWildcards());
			BioCollectionBean newCollectionB = (BioCollectionBean) currentCollection.filter(bio.getQueryWithWildcards());
			_log.debug("LOG_SYSTEM_OUT","NewCollection " + newCollection.size(),100L);
			_log.debug("LOG_SYSTEM_OUT","NewCollectionB " + newCollectionB.size(),100L);
			_log.debug("LOG_SYSTEM_OUT","CurrentCollection " + currentCollection.size(),100L);
			newCollectionB.copyFrom(currentCollection);
			currentCollection.copyFrom(newCollection);
			_log.debug("LOG_SYSTEM_OUT","NewCollection " + newCollection.size(),100L);
			_log.debug("LOG_SYSTEM_OUT","NewCollectionB " + newCollectionB.size(),100L);
			_log.debug("LOG_SYSTEM_OUT","CurrentCollection " + currentCollection.size(),100L);
			newCollection = currentCollection;
			bio.setBaseBioCollectionForQuery(newCollectionB);
			newCollection.setQBEBioBean(bio);
			newCollection.filterInPlace(bio.getQueryWithWildcards());
			result.setFocus(newCollection);
		}
		else
		{
			//if the current biocollection does not contain the recently modified item - refilter
			RuntimeFormInterface detail = FormUtil.findForm(state.getCurrentRuntimeForm(), shellForm, detailForm, state);
			DataBean detailFocus = detail.getFocus();
			if (detailFocus instanceof BioBean)
			{
				_log.debug("LOG_SYSTEM_OUT","detailFocus is a BioBean",100L);

				BioRef detailRef = ((BioBean) detailFocus).getBioRef();
				ArrayList listRefs = listFocus.getBioRefs();
				if (listRefs.contains(detailRef.getBioRefString()))
				{
					_log.debug("LOG_SYSTEM_OUT","\n\t" + "The Bio is There" + "\n",100L);
				}
				else
				{
					//requery or add paramters
					//make a qbe bean out of the current query and append?
					
				}

			}

			//reuse the current biocollection
			result.setFocus(listFocus);
		}
		_log.debug("LOG_SYSTEM_OUT","END------------------>",100L);
		return RET_CONTINUE;
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
