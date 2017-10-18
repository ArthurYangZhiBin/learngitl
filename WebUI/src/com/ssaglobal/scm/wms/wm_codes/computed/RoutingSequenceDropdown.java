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
 * (c) COPYRIGHT 2011 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */
package com.ssaglobal.scm.wms.wm_codes.computed;

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

/**
 * TODO Document RoutingSequenceDropdown class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class RoutingSequenceDropdown extends
		com.epiphany.shr.ui.metadata.AttributeDomainExtensionBase {

	private static ILoggerCategory log = LoggerFactory
			.getInstance(RoutingSequenceDropdown.class);

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

		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getTempUnitOfWork();
		String bioName = "wm_routingdetail";
		String codeAttribute = "OPERATIONDEFNID";
		String descAttribute = "SEQUENCE";
		String sortOrders = getParameterString("SORT", bioName + "."
				+ descAttribute + " ASC");

		String query = "NOT(" + bioName + "." + codeAttribute + " IS NULL) ";
		BioCollectionBean rs = uow.getBioCollectionBean(new Query(bioName, query,
				null));
		Map<Object, Object> localeMap = new HashMap<Object, Object>();
		//if code and desc are the same, the dropdown is displaying code and code, no need to get locale for that scenario
		if (!codeAttribute.equals(descAttribute)) {
			localeMap = getCodelkupDBTranslations(bioName, descAttribute, state);
		}
		for (int i = 0; i < rs.size(); i++) {
			BioBean code = rs.get("" + i);
			Object key = code.getValue(codeAttribute);
			Object value = localeMap.get(key);			
			if (values.contains(key)) {
				continue;
			}
			if(key instanceof Integer){
				values.add(key);
			}else{
				values.add(key.toString());
			}
			if (value == null) {
				labels.add(code.getValue(descAttribute));
			} else {
				labels.add(value);
			}
		}
		return RET_CONTINUE;
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


}
