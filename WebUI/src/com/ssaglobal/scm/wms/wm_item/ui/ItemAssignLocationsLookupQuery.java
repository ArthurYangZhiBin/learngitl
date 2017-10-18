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

package com.ssaglobal.scm.wms.wm_item.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.EpiRuntimeException;
import com.epiphany.shr.util.exceptions.UserException;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ItemAssignLocationsLookupQuery extends com.epiphany.shr.ui.action.ActionExtensionBase
{

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

		String[] parameters = new String[2];
		String widgetName = "LOCATIONTYPE";
		String qry = "wm_location.LOCATIONTYPE = '";
		StateInterface state = context.getState();
		String value = null;

		RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
		DataBean currentFocus = currentForm.getFocus();
		if (currentFocus instanceof QBEBioBean)
		{
			currentFocus = (QBEBioBean) currentFocus;
		}
		else if (currentFocus instanceof BioBean)
		{
			currentFocus = (BioBean) currentFocus;
		}
		value = currentFocus.getValue(widgetName).toString();
		

		//Get all Locations
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		Query newQuery = new Query("wm_location", "", null);
		BioCollectionBean oldCollection = uow.getBioCollectionBean(newQuery);

		//Create new Query By Example
		QBEBioBean bio = createNewQBE(state, "wm_location");
		//Set Filter
		bio.setValue("LOCATIONTYPE", value);
		BioCollectionBean newCollection = (BioCollectionBean) oldCollection.filter(bio.getQueryWithWildcards());
		//BioCollectionBean newCollectionB = (BioCollectionBean) oldCollection.filter(bio.getQueryWithWildcards());
		//		newCollectionB.copyFrom(oldCollection);
		//		oldCollection.copyFrom(newCollection);
		//		newCollection = oldCollection;
		bio.setBaseBioCollectionForQuery(oldCollection);
		newCollection.setQBEBioBean(bio);
		newCollection.filterInPlace(bio.getQueryWithWildcards());
		result.setFocus(newCollection);

		//		qry = qry + value + "'";
		//		_log.debug("LOG_SYSTEM_OUT","QUERY \n" + qry,100L);
		//		Query loadBiosQry = new Query("wm_location", qry, null);
		//		BioCollectionBean newFocus = uow.getBioCollectionBean(loadBiosQry);
		//		if (newFocus.size() < 1)
		//		{
		//			parameters[0] = colonStrip(readLabel(currentForm, widgetName));
		//			parameters[1] = currentForm.getFormWidgetByName(widgetName).getDisplayValue();
		//			throw new UserException("WMEXP_ITEM_EMPTY_LOOKUP", parameters);
		//			//			loadBiosQry = new Query("wm_location", "", null);
		//			//			uow = state.getDefaultUnitOfWork();
		//			//			newFocus = uow.getBioCollectionBean(loadBiosQry);
		//		}
		//		result.setFocus(newFocus);

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

	public String colonStrip(String label)
	{
		Pattern pattern = Pattern.compile("\\:");
		Matcher matcher = pattern.matcher(label);
		return matcher.replaceAll("");
	}

	public String readLabel(RuntimeFormInterface form, String widgetName)
	{
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return form.getFormWidgetByName(widgetName).getLabel("label", locale);
	}

}
