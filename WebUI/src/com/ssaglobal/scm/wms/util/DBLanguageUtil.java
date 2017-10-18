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
package com.ssaglobal.scm.wms.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.internationalization.LocaleUtils;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;

public class DBLanguageUtil {
	protected static ILoggerCategory log = LoggerFactory
			.getInstance(DBLanguageUtil.class);

	public static Map<Object, Object> generateLocaleMap(
			BioCollectionBean localeRs) throws EpiDataException {
		return generateLocaleMap("CODE", localeRs);
	}

	public static Map<Object, Object> generateLocaleMap(String localeCode,
			BioCollectionBean localeRs) throws EpiDataException {
		Map<Object, Object> localeMap = new HashMap<Object, Object>();
		try {
			for (int i = 0; i < localeRs.size(); i++) {
				BioBean localeEntry = localeRs.get("" + i);
				Object key = localeEntry.getValue(localeCode);
				Object value = localeEntry.getValue("DESCRIPTION");
				localeMap.put(key, value);
			}
		} catch (com.epiphany.shr.data.error.BioCollInsufficientElementsException e) {
			log.error("LOG_ERROR_EXTENSION",
					"Out of bounds error purposely ignored"
							+ e.getErrorMessage(), SuggestedCategory.NONE);
			log.error("LOG_ERROR_EXTENSION", e.getStackTraceAsString(),
					SuggestedCategory.NONE);
		}
		return localeMap;
	}

	public static Locale getLocale(StateInterface state)
			throws EpiDataException, UserException {
		HttpSession session = state.getRequest().getSession();
		Locale locale = (Locale) session
				.getAttribute(SetIntoHttpSessionAction.DB_LOCALE);
		if (locale == null) {
			locale = normalizeLocale(state.getUser().getLocale()
					.getJavaLocale(), state);
		}
		return locale;
	}

	public static String getPhysicalName(String bioName, StateInterface state)
			throws EpiDataException {
		Query bioRSQuery = new Query("wm_bio_class",
				"wm_bio_class.bio_class_name = '" + bioName + "'", null);
		BioCollectionBean bioRS = state.getTempUnitOfWork()
				.getBioCollectionBean(bioRSQuery);
		try {
			for (int i = 0; i < bioRS.size(); i++) {
				BioBean bioClass = bioRS.get("" + i);
				Query rsQuery = new Query("wm_data_recordset",
						"wm_data_recordset.data_recordset_id = '"
								+ bioClass.getValue("data_recordset_id") + "'",
						null);
				BioCollectionBean recordSetResults = state.getTempUnitOfWork()
						.getBioCollectionBean(rsQuery);
				for (int j = 0; j < recordSetResults.size(); j++) {
					BioBean recordSetInfo = recordSetResults.get("" + j);
					return BioAttributeUtil.getString(recordSetInfo,
							"recordset_physical_name");

				}
			}
		} catch (com.epiphany.shr.data.error.BioCollInsufficientElementsException e) {
			log.error("LOG_ERROR_EXTENSION",
					"Out of bounds error purposely ignored"
							+ e.getErrorMessage(), SuggestedCategory.NONE);
			log.error("LOG_ERROR_EXTENSION", e.getStackTraceAsString(),
					SuggestedCategory.NONE);
		}

		return "";
	}

	public static String getPhysicalName(String bioName, UnitOfWork uow)
			throws EpiDataException {
		Query bioRSQuery = new Query("wm_bio_class",
				"wm_bio_class.bio_class_name = '" + bioName + "'", null);
		BioCollection bioRS = uow.findByQuery(bioRSQuery);
		try {
			for (int i = 0; i < bioRS.size(); i++) {
				Bio bioClass = bioRS.elementAt(i);
				Query rsQuery = new Query("wm_data_recordset",
						"wm_data_recordset.data_recordset_id = '"
								+ bioClass.get("data_recordset_id") + "'", null);
				BioCollection recordSetResults = uow.findByQuery(rsQuery);
				for (int j = 0; j < recordSetResults.size(); j++) {
					Bio recordSetInfo = recordSetResults.elementAt(j);
					return BioUtil.getString(recordSetInfo,
							"recordset_physical_name");

				}
			}
		} catch (com.epiphany.shr.data.error.BioCollInsufficientElementsException e) {
			log.error("LOG_ERROR_EXTENSION",
					"Out of bounds error purposely ignored"
							+ e.getErrorMessage(), SuggestedCategory.NONE);
			log.error("LOG_ERROR_EXTENSION", e.getStackTraceAsString(),
					SuggestedCategory.NONE);
		}

		return "";
	}

	private static List<Locale> getSupportedLocales(StateInterface state)
			throws EpiDataException {
		BioCollectionBean rs = CodelkupUtil.getCodeLookupCollection("LOCALE",
				state);
		List<Locale> supportedLocales = new ArrayList<Locale>();
		try {
			for (int i = 0; i < rs.size(); i++) {
				BioBean code = rs.get("" + i);
				supportedLocales.add(LocaleUtils
						.getLocaleFromString(BioAttributeUtil.getString(code,
								"CODE")));
			}
		} catch (com.epiphany.shr.data.error.BioCollInsufficientElementsException e) {
			log.error("LOG_ERROR_EXTENSION",
					"Out of bounds error purposely ignored"
							+ e.getErrorMessage(), SuggestedCategory.NONE);
			log.error("LOG_ERROR_EXTENSION", e.getStackTraceAsString(),
					SuggestedCategory.NONE);
		}
		return supportedLocales;
	}

	/**
	 * Normalize locale to supported Locale
	 * 
	 * @param locale
	 *            the locale
	 * @param state
	 *            the state
	 * @return the locale
	 * @throws EpiDataException
	 * @throws UserException
	 */
	public static Locale normalizeLocale(Locale locale, StateInterface state)
			throws EpiDataException, UserException {
		List<Locale> supportedLocales = getSupportedLocales(state);
		if (supportedLocales.contains(locale)) {
			log.debug("DBLanguageUtil_normalizeLocale", "Locale is supported "
					+ locale, SuggestedCategory.APP_EXTENSION);
			return locale;
		} else {
			log
					.debug(
							"DBLanguageUtil_normalizeLocale",
							"Locale is not supported, going to try and figure this out",
							SuggestedCategory.APP_EXTENSION);
			Locale attempt1 = new Locale(locale.getLanguage(), locale
					.getCountry(), locale.getVariant());
			if (supportedLocales.contains(attempt1)) {
				log.debug("DBLanguageUtil_normalizeLocale",
						"Locale is supported " + attempt1,
						SuggestedCategory.APP_EXTENSION);
				return attempt1;
			}
			Locale attempt2 = new Locale(locale.getLanguage(), locale
					.getCountry());
			if (supportedLocales.contains(attempt2)) {
				log.debug("DBLanguageUtil_normalizeLocale",
						"Locale is supported " + attempt2,
						SuggestedCategory.APP_EXTENSION);
				return attempt2;
			}

			Locale attempt3 = new Locale(locale.getLanguage());
			if (supportedLocales.contains(attempt3)) {
				log.debug("DBLanguageUtil_normalizeLocale",
						"Locale is supported " + attempt3,
						SuggestedCategory.APP_EXTENSION);
				return attempt3;
			}
			Locale en = new Locale("en");
			if (supportedLocales.contains(en)) {
				log.debug("DBLanguageUtil_normalizeLocale",
						"Default Locale, english " + en,
						SuggestedCategory.APP_EXTENSION);
				return en;
			}
			log.error("DBLanguageUtil_normalizeLocale",
					"English Locale not found, just going to take the first",
					SuggestedCategory.APP_EXTENSION);
			if (supportedLocales.size() > 0) {
				Locale defaultLocale = supportedLocales.get(0);
				log.debug("DBLanguageUtil_normalizeLocale",
						"Using failsafe locale " + defaultLocale,
						SuggestedCategory.APP_EXTENSION);
				return defaultLocale;
			}
			log.error("DBLanguageUtil_normalizeLocale",
					"No Supported Locale found",
					SuggestedCategory.APP_EXTENSION);
			throw new UserException(
					"WMEXP_LOCALE_UNSUPPORTED",
					new Object[] { LocaleUtils.getCodeStringFromLocale(locale) });
		}
	}

}
