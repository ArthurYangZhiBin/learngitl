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
import com.ssaglobal.scm.wms.util.DBLanguageUtil;
import com.ssaglobal.scm.wms.util.StringUtils;

/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 */
public class TableComputedAttrDom extends
		com.epiphany.shr.ui.metadata.AttributeDomainExtensionBase {

	private static ILoggerCategory log = LoggerFactory
			.getInstance(TableComputedAttrDom.class);

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

		String bioName = getParameterString("BIO");
		String codeAttribute = getParameterString("CODE");
		String descAttribute = getParameterString("DESC");
		String filterAttribute = getParameterString("FILTER");
		List<String> ignoreList = (ArrayList) getParameter("IGNORE");
		String sortOrders = getParameterString("SORT", bioName + "."
				+ descAttribute + " ASC");
		boolean distinct = getParameterBoolean("DISTINCT", false);
		String whereClause = getParameterString("WHERE");

		getCodeLookup(context, values, labels, new ArrayList(), bioName,
				codeAttribute, descAttribute, filterAttribute, ignoreList,
				sortOrders, distinct, whereClause);
		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		return RET_CONTINUE;
	}

	private void getCodeLookup(DropdownContentsContext context, List values,
			List labels, List filter, String bioName, String codeAttribute,
			String descAttribute, String filterAttribute,
			List<String> ignoreList, String sortOrders, boolean distinct,
			String whereClause) throws EpiDataException, UserException {

		StateInterface state = context.getState();
		String qry = "NOT(" + bioName + "." + codeAttribute + " IS NULL) ";

		if (ignoreList != null) {
			String ignoreQuery = "";
			ignoreQuery = appendListToQuery(bioName, codeAttribute, ignoreList,
					ignoreQuery, "!=");
			qry += ignoreQuery;
		}

		if (filter != null) {
			String filterQuery = "";
			filterQuery = appendListToQuery(bioName, filterAttribute, filter,
					filterQuery, "=");
			qry += filterQuery;
		}

		if (!StringUtils.isEmpty(whereClause)) {
			qry += " AND " + whereClause;
		}

		UnitOfWorkBean uow = state.getTempUnitOfWork();

		BioCollectionBean rs = uow.getBioCollectionBean(new Query(bioName, qry,
				sortOrders));

		Map<Object, Object> localeMap = new HashMap<Object, Object>();
		//if code and desc are the same, the dropdown is displaying code and code, no need to get locale for that scenario
		if (!codeAttribute.equals(descAttribute)) {
			localeMap = getCodelkupDBTranslations(bioName, descAttribute, state);
		}

		try {
			if (distinct == true) {
				for (int i = 0; i < rs.size(); i++) {
					BioBean code = rs.get("" + i);
					if (values.contains(code.getValue(codeAttribute))) {
						continue;
					}

					Object key = code.getValue(codeAttribute);
					values.add(key.toString());
					Object value = localeMap.get(key);
					if (value == null) {
						labels.add(code.getValue(descAttribute));
					} else {
						labels.add(value);
					}
				}
			} else {
				for (int i = 0; i < rs.size(); i++) {
					BioBean code = rs.get("" + i);

					Object key = code.getValue(codeAttribute);
					values.add(key.toString());
					Object value = localeMap.get(key);
					if (value == null) {
						labels.add(code.getValue(descAttribute));
					} else {
						labels.add(value);
					}
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

	private Map<Object, Object> getCodelkupDBTranslations(String bioName,
			String descAttribute, StateInterface state)
			throws EpiDataException, UserException {
		Locale locale = DBLanguageUtil.getLocale(state);
		String tableName = DBLanguageUtil.getPhysicalName(bioName, state);
		Query localeQuery = new Query("wm_translationlist",
				"wm_translationlist.TBLNAME = '" + tableName
						+ "' and wm_translationlist.LOCALE = '"
						+ LocaleUtils.getCodeStringFromLocale(locale)
						+ "' and wm_translationlist.COLUMNNAME = '"
						+ descAttribute + "'", null);
		log.debug("CodesComputedAttrDom_getCodeLookup", "Locale query "
				+ localeQuery.getQueryExpression(),
				SuggestedCategory.APP_EXTENSION);
		BioCollectionBean localeRs = state.getTempUnitOfWork()
				.getBioCollectionBean(localeQuery);
		String localeCode = getParameterString("CODELOCALE", "CODE");
		return DBLanguageUtil.generateLocaleMap(localeCode, localeRs);
	}

	private String appendListToQuery(String bioName, String attribute,
			List<String> list, String query, String operator) {

		for (int i = 0; i < list.size(); i++) {
			query += " AND " + bioName + "." + attribute + " " + operator
					+ " '" + list.get(i) + "'";
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

		String bioName = getParameterString("BIO");
		String codeAttribute = getParameterString("CODE");
		String descAttribute = getParameterString("DESC");
		String filterAttribute = getParameterString("FILTER");
		List<String> ignoreList = (ArrayList) getParameter("IGNORE");
		String sortOrders = getParameterString("SORT", bioName + "."
				+ descAttribute + " ASC");
		boolean distinct = getParameterBoolean("DISTINCT", false);
		String whereClause = getParameterString("WHERE");

		getCodeLookup(context, values, labels, filter, bioName, codeAttribute,
				descAttribute, filterAttribute, ignoreList, sortOrders,
				distinct, whereClause);

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		return RET_CONTINUE;
	}
}