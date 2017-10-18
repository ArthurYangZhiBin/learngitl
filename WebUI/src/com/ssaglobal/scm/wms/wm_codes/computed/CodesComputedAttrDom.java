/**
 * ---Begin Copyright Notice---20090105T1353
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2010 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */

/******************************************************
 *
 *                       NOTICE
 *
 *   THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS
 *   CONFIDENTIAL INFORMATION OF INFOR AND SHALL NOT
 *   BE DISCLOSED WITHOUT PRIOR WRITTEN PERMISSION.
 *   LICENSED CUSTOMERS MAY COPY AND ADAPT THIS
 *   SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH
 *   THE TERMS OF THEIR SOFTWARE LICENSE AGREEMENT.
 *   ALL OTHER RIGHTS RESERVED.
 *
 *   COPYRIGHT (c) 2008 INFOR. ALL RIGHTS RESERVED.
 *   THE WORD AND DESIGN MARKS SET FORTH HEREIN ARE
 *   TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 *   AND/OR RELATED AFFILIATES AND SUBSIDIARIES. ALL
 *   RIGHTS RESERVED. ALL OTHER TRADEMARKS LISTED
 *   HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE
 *   OWNERS. WWW.INFOR.COM.
 *
 ******************************************************/

package com.ssaglobal.scm.wms.wm_codes.computed;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.metadata.DropdownContentsContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.internationalization.LocaleUtils;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.DBLanguageUtil;
import com.ssaglobal.scm.wms.util.StringUtils;

/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 */
public class CodesComputedAttrDom extends
		com.epiphany.shr.ui.metadata.AttributeDomainExtensionBase {

	protected static ILoggerCategory log = LoggerFactory
			.getInstance(CodesComputedAttrDom.class);

	/**
	 * Fires whenever the values and/or labels for an attribute domain are
	 * requested. The base list of labels and values are provided and may be
	 * filtered, modified, replaced, etc as desired.
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.metadata.DropdownContentsContext
	 * DropdownContentsContext} exposes information about the context in which
	 * the attribute domain is being used, including the service and the user
	 * interface {@link com.epiphany.shr.ui.state.StateInterface state} and
	 * {@link com.epiphany.shr.ui.view.RuntimeFormWidgetInterface form widget}.</li>
	 * 
	 * @param context
	 *            the
	 *            {@link com.epiphany.shr.ui.metadata.DropdownContentsContext
	 *            DropdownContentsContext}
	 * @param values
	 *            the list of values to be modified
	 * @param labels
	 *            the corresponding list of labels to be modified
	 */
	protected int execute(DropdownContentsContext context, List values,
			List labels) throws EpiException {

		String listName = getParameterString("LISTNAME");
		List<String> ignoreList = (ArrayList) getParameter("IGNORE");
		String whereClause = getParameterString("WHERE");
		String sortOrders = getParameterString("SORT",
				"wm_codesdetail.SEQUENCE ASC, wm_codesdetail.DESCRIPTION ASC");

		getCodeLookup(context, values, labels, new ArrayList(), listName,
				ignoreList, sortOrders, whereClause);

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		return RET_CONTINUE;
	}

	/**
	 * Gets the code lookup.
	 * 
	 * @param context
	 *            the context
	 * @param values
	 *            the values
	 * @param labels
	 *            the labels
	 * @param filter
	 *            the filter
	 * @param listName
	 *            the list name
	 * @param ignoreList
	 *            the ignore list
	 * @param sortOrders
	 *            the sort orders
	 * @param whereClause
	 * @return the code lookup
	 * @throws EpiDataException
	 *             the epi data exception
	 * @throws UserException
	 */
	public void getCodeLookup(DropdownContentsContext context, List values,
			List labels, List filter, String listName, List<String> ignoreList,
			String sortOrders, String whereClause) throws EpiDataException,
			UserException {
		StateInterface state = context.getState();
		BioCollectionBean rs = getCodeLookupCollection(filter, listName,
				ignoreList, sortOrders, whereClause, state);

		// Get Translation List Strings
		Map<Object, Object> localeMap = getCodelkupDBTranslations(listName,
				state);
		

		try {
			for (int i = 0; i < rs.size(); i++) {
				BioBean code = rs.get("" + i);
				Object key = code.getValue("CODE");
				values.add(key);
				Object value = localeMap.get(key);
				if(value == null){
					labels.add(code.getValue("DESCRIPTION"));	
				} else {
					labels.add(value);
				}
				
			}
		} catch (com.epiphany.shr.data.error.BioCollInsufficientElementsException e) {
			log.error("LOG_ERROR_EXTENSION",
					"Out of bounds error purposely ignored"
							+ e.getErrorMessage(), SuggestedCategory.NONE);
			log.error("LOG_ERROR_EXTENSION", e.getStackTraceAsString(),
					SuggestedCategory.NONE);
		}
	}

	private Map<Object, Object> getCodelkupDBTranslations(String listName,
			StateInterface state) throws EpiDataException, UserException {
		Locale locale = DBLanguageUtil.getLocale(state);
		Query localeQuery = new Query(
				"wm_translationlist",
				"wm_translationlist.TBLNAME = 'CODELKUP' and wm_translationlist.LOCALE = '"
						+ LocaleUtils.getCodeStringFromLocale(locale)
						+ "' and wm_translationlist.JOINKEY1 = '" + listName
						+ "' and wm_translationlist.COLUMNNAME = 'DESCRIPTION'",
				null);
		log.debug("CodesComputedAttrDom_getCodeLookup", "Locale query " + localeQuery.getQueryExpression(),
				SuggestedCategory.APP_EXTENSION);
		BioCollectionBean localeRs = state.getTempUnitOfWork().getBioCollectionBean(localeQuery);
		return DBLanguageUtil.generateLocaleMap(localeRs);
	}

	public BioCollectionBean getCodeLookupCollection(List filter,
			String listName, List<String> ignoreList, String sortOrders,
			String whereClause, StateInterface state) {
		String qry = "wm_codesdetail.LISTNAME = '" + listName
				+ "' and wm_codesdetail.ACTIVE = '1'";

		if (ignoreList != null) {
			String ignoreQuery = "";
			ignoreQuery = appendListToQuery(ignoreList, ignoreQuery, "!=");
			qry += ignoreQuery;
		}

		if (filter != null) {
			String filterQuery = "";
			filterQuery = appendListToQuery(filter, filterQuery, "=");
			qry += filterQuery;
		}

		if (!StringUtils.isEmpty(whereClause)) {
			qry += " and " + whereClause;
		}

		UnitOfWorkBean uow = state.getTempUnitOfWork();

		BioCollectionBean rs = uow.getBioCollectionBean(new Query(
				"wm_codesdetail", qry, sortOrders));
		return rs;
	}

	private String appendListToQuery(List<String> list, String query,
			String operator) {
		for (int i = 0; i < list.size(); i++) {
			query += " AND wm_codesdetail.CODE " + operator + " '"
					+ list.get(i) + "'";
		}
		return query;
	}

	/**
	 * Fires whenever the values and/or labels for a hierarchical attribute
	 * domain are requested. The base list of labels and values are provided and
	 * may be filtered, modified, replaced, etc as desired. The filter list
	 * provides the list of selections in the hierarchy to be used to determine
	 * the result.
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.metadata.DropdownContentsContext
	 * DropdowContentsContext} exposes information about the context in which
	 * the attribute domain is being used, including the service and the user
	 * interface {@link com.epiphany.shr.ui.state.StateInterface state} and
	 * {@link com.epiphany.shr.ui.view.RuntimeFormWidgetInterface form widget}.</li>
	 * 
	 * @param context
	 *            the
	 *            {@link com.epiphany.shr.ui.metadata.DropdownContentsContext
	 *            DropdownContentsContext}
	 * @param values
	 *            the list of values to be modified
	 * @param labels
	 *            the corresponding list of labels to be modified
	 * @param filter
	 *            the list of currently selected widget values on which this
	 *            dropdown depends
	 */
	protected int execute(DropdownContentsContext context, List values,
			List labels, List filter) throws EpiException {

		String listName = getParameterString("LISTNAME");
		String whereClause = getParameterString("WHERE");
		List<String> ignoreList = (ArrayList) getParameter("IGNORE");
		String sortOrders = getParameterString("SORT",
				"wm_codesdetail.SEQUENCE ASC, wm_codesdetail.DESCRIPTION ASC");

		getCodeLookup(context, values, labels, filter, listName, ignoreList,
				sortOrders, whereClause);

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		return RET_CONTINUE;
	}
}