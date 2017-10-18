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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.model.UserBean;
import com.epiphany.shr.ui.model.UserInterface;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.state.EpnyInProcStateImpl;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.SsaAccessBase;
import com.ssaglobal.base.Config;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;

// TODO: Auto-generated Javadoc
/**
 * The Class ReportUtil.
 */
public class ReportUtil
{

	private static final String _NO_OFFSET = "0";

	private static final String BIC_REPORT = "BIC_REPORT";

	/** The Constant PARAM_KEY_TMZ. */
	private static final String PARAM_KEY_TMZ = "&__offsetMin=";
	
	private static final String PARAM_KEY_TMZID = "&__timezone=";
	
	private static final String COG_PARAM_KEY_TMZ = "&p_tzoffset=";	//Incident2631884_Defect150016

	/** The _log. */
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ReportUtil.class);

	/** The REPORTURL. */
	static public String REPORTURL = "REPORTURL";

	/** The cognos date format. */
	static public SimpleDateFormat cognosDateFormat = new SimpleDateFormat("yyyy-MM-dd"); //AW 07/06/2009 SDIS:04718

	/** The cognos time format. */
	static public SimpleDateFormat cognosTimeFormat = new SimpleDateFormat("hh:mm:ss");

	/** The birt date format. */
	static public DateFormat birtDateFormat = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("en", "US"));

	/** The Constant SERVER_TYPE_BIRT. */
	public static final String SERVER_TYPE_BIRT = "BIRT";

	/** The Constant SERVER_TYPE_COGNOS. */
	public static final String SERVER_TYPE_COGNOS = "COGNOS";

	/** The Constant PARAM_KEY_UID. */
	private static final String PARAM_KEY_UID = "&p_uid=";

	/** The Constant PARAM_KEY_LOCALE. */
	private static final String PARAM_KEY_LOCALE = "&p_locale=";

	/** The Constant PARAM_KEY_OUTLOCALE. */
	private static final String PARAM_KEY_OUTLOCALE = "&outputLocale=";

	/** The Constant PARAM_KEY_DBCONN. */
	private static final String PARAM_KEY_DBCONN = "&p_conn=";

	/** The Constant PARAM_KEY_SCHEMA. */
	private static final String PARAM_KEY_SCHEMA = "&p_SCHEMA=";

	/** The Constant PARAM_KEY_DB. */
	private static final String PARAM_KEY_DB = "&p_DATABASE=";

	/** The Constant PARAM_KEY_BIRT_LOCALE. */
	private static final String PARAM_KEY_BIRT_LOCALE = "&__locale=";
	
	/** The Constant PARAM_KEY_BILLCONN. */
	private static final String PARAM_KEY_BILLCONN = "&p_CONN=";

	/** The Constant PARAM_KEY_SESSION. */
	private static final String PARAM_KEY_SESSION = "&_id=";
	
	private static final String PARAM_DB_TYPE = "&dbtype=";
	
	/** Constant to define pdf as the report format */
	private static final String PARAM_VALUE_FORMAT_PDF = "pdf";
	
	/** Constant to define PostScript ps as the report format */
	private static final String PARAM_VALUE_FORMAT_PS = "postscript";
	
	/** Constant to define os.name system property */
	public static final String SYSTEM_PROPERTY_KEY_OS_NAME = "os.name";
	
	/** Constant to define value for os.name system property on AIX */
	public static final String SYSTEM_PROPERTY_VALUE_OS_NAME_AIX = "AIX";

	/** Constant to define value for os.name system property on Linux */
	public static final String SYSTEM_PROPERTY_VALUE_OS_NAME_LINUX = "Linux";

	/** Constant to define webUIConfig.xml configuration file */
	private static final String WEBUICONFIG_FILENAME = "webUIConfig";

	/** Constant to define configuration key for background printing report file format */
	private static final String WEBUICONFIG_KEY_BACKGROUNDPRINTFILEFORMAT = "BGPrintFileFormat";
	


	/**
	 * Append db conn to report url.
	 *
	 * @param url the url
	 * @param state the state
	 * @return the string
	 */
	static public String appendDbConnToReportUrl(String url, StateInterface state)
	{
		HttpSession session = state.getRequest().getSession();
		String dbConn = (String) session.getAttribute("dbConnectionName");
		url += "&p_conn=" + dbConn;
		return url;
	}

	/**
	 * Append p locale to report url.
	 *
	 * @param url the url
	 * @param state the state
	 * @return the string
	 */
	static public String appendPLocaleToReportUrl(String url, StateInterface state)
	{
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		String locale = state.getLocale().getJavaLocale().getLanguage();
		url += "&p_locale=" + locale;
		return url;
	}

	/**
	 * Gets the language country locale.
	 *
	 * @param locale the locale
	 * @return the language country locale
	 */
	public static Locale getLanguageCountryLocale(Locale locale)
	{
		_log.debug("LOG_DEBUG_EXTENSION_ReportUtil_getLanguageCountryLocale", "Processing " + locale, SuggestedCategory.NONE);
		String[] split = locale.toString().split("_");
		if (split.length == 3)
		{
			_log.debug("LOG_DEBUG_EXTENSION_ReportUtil_getLanguageCountryLocale", "Returning Locale with VARIANT removed", SuggestedCategory.NONE);
			// Special Case for nl_NL
			final String language = split[0];
			if (language.equalsIgnoreCase("nl")) {
				return new Locale(locale.getLanguage(), "BE");
			}
			return new Locale(locale.getLanguage(), locale.getCountry());
		}
		else if (split.length == 1)
		{
			_log.debug("LOG_DEBUG_EXTENSION_ReportUtil_getLanguageCountryLocale", "Going to append Default Country to Language Only Locale", SuggestedCategory.NONE);
			final String language = split[0];
			if (language.equalsIgnoreCase("en"))
			{
				return new Locale(locale.getLanguage(), "US");
			}
			if (language.equalsIgnoreCase("de"))
			{
				return new Locale(locale.getLanguage(), "DE");
			}
			if (language.equalsIgnoreCase("es"))
			{
				return new Locale(locale.getLanguage(), "ES");
			}
			if (language.equalsIgnoreCase("nl"))
			{
				// Workaround for BIRT
				return new Locale(locale.getLanguage(), "BE");
			}
			if (language.equalsIgnoreCase("ja"))
			{
				return new Locale(locale.getLanguage(), "JP");
			}
			if (language.equalsIgnoreCase("pt"))
			{
				return new Locale(locale.getLanguage(), "BR");
			}
			if (language.equalsIgnoreCase("zh"))
			{
				return new Locale(locale.getLanguage(), "CN");
			}
			if (language.equalsIgnoreCase("fr"))
			{
				return new Locale(locale.getLanguage(), "FR");
			}
			_log.debug("LOG_DEBUG_EXTENSION_ReportUtil_getLanguageCountryLocale", "Locale not one of 'supported' locales", SuggestedCategory.NONE);
		}
		_log.debug("LOG_DEBUG_EXTENSION_ReportUtil_getLanguageCountryLocale", "Returning original Locale", SuggestedCategory.NONE);
		// Special Case for nl_NL
		final String language = split[0];
		if (language.equalsIgnoreCase("nl")) {
			return new Locale(locale.getLanguage(), "BE");
		}
		return locale;
	}

	/**
	 * Gets the language locale string.
	 *
	 * @param locale the locale
	 * @return the language locale string
	 */
	public static String getLanguageLocaleString(Locale locale)
	{
		if(locale == null)
		{
			return Locale.getDefault().getLanguage();
		}
		_log.debug("LOG_DEBUG_EXTENSION_ReportUtil_getLanguageLocaleString", "Processing " + locale, SuggestedCategory.NONE);
		if(locale.getLanguage().equals("pt"))
		{
			return "pt_BR";
		}
		_log.debug("LOG_DEBUG_EXTENSION_ReportUtil_getLanguageLocaleString", "Returning original Locale", SuggestedCategory.NONE);
		return locale.getLanguage();
	}

	/**
	 * Gets the locale.
	 *
	 * @param locale the locale
	 * @return the locale
	 */
	private static Locale getLocale(String locale)
	{
		String[] split = locale.split("_");
		if (split.length == 1)
		{
			return new Locale(locale);
		}
		else if (split.length == 2)
		{
			return new Locale(split[0], split[1]);
		}
		else if (split.length == 3)
		{
			return new Locale(split[0], split[1], split[2]);
		}
		else
		{
			return Locale.getDefault();
		}

	}

	/**
	 * Gets the report server type.
	 *
	 * @param state the state
	 * @return the report server type
	 */
	public static String getReportServerType(StateInterface state)
	{
		Query serverTypeQuery = new Query("wm_system_settings", "wm_system_settings.CONFIGKEY = 'BirtReportsInstalled' and wm_system_settings.NSQLVALUE = '1'", null);
		BioCollectionBean rs = state.getDefaultUnitOfWork().getBioCollectionBean(serverTypeQuery);
		String serverType = SERVER_TYPE_COGNOS;
		try
		{
			if (rs.size() == 1)
			{
				serverType = SERVER_TYPE_BIRT;
			}
		} catch (EpiDataException e1)
		{
			e1.printStackTrace();
			_log.error(e1);
		}
		_log.debug("LOG_DEBUG_EXTENSION_ReportUtil", "Server Type " + serverType, SuggestedCategory.NONE);
		return serverType;
	}

	/**
	 * Gets the time zone.
	 *
	 * @param state the state
	 * @return the time zone
	 */
	public static TimeZone getTimeZone(StateInterface state) 
	{
		UserInterface user = state.getUser();

		if(user instanceof UserBean)
		{
			Object timeZoneObj = ((UserBean)user).getRequestAttribute("browser time zone");
			if(timeZoneObj instanceof String)
			{
				TimeZone userTimeZone = TimeZone.getTimeZone(timeZoneObj.toString());
				return userTimeZone;
			}
		}
		return Calendar.getInstance().getTimeZone();
	}


	/**
	 * Checks if is bIRT.
	 *
	 * @param state the state
	 * @return true, if is bIRT
	 */
	public static boolean isBIRT(StateInterface state) {
		if (ReportUtil.getReportServerType(state).equalsIgnoreCase(ReportUtil.SERVER_TYPE_BIRT)) {
			return true;
		}
		return false;
	}





	/**
	 * Checks if is cOGNOS.
	 *
	 * @param state the state
	 * @return true, if is cOGNOS
	 */
	public static boolean isCOGNOS(StateInterface state) {
		if (ReportUtil.getReportServerType(state).equalsIgnoreCase(ReportUtil.SERVER_TYPE_COGNOS)) {
			return true;
		}
		return false;
	}

	/**
	 * Offset in minutes.
	 *
	 * @param userTimeZone the user time zone
	 * @return the int
	 */
	private static int offsetInMinutes(TimeZone userTimeZone) 
	{
		int offset = userTimeZone.getOffset(System.currentTimeMillis());
		int offsetInSeconds = offset / 1000;
		int offsetInMinutes = offsetInSeconds / 60;
		return offsetInMinutes;
	}

	/**
	 * Prints the locale.
	 *
	 * @param locale the locale
	 * @return the string
	 */
	private static String printLocale(Locale locale)
	{
		String sLocale = "";
		if (locale == null)
		{
			return sLocale;
		}
		if ((locale.getLanguage() != null) && !locale.getLanguage().matches("\\s*"))
		{
			sLocale += locale.getLanguage();
		}
		if ((locale.getCountry() != null) && !locale.getCountry().matches("\\s*"))
		{
			sLocale += "_" + locale.getCountry();
		}
		if ((locale.getVariant() != null) && !locale.getVariant().matches("\\s*"))
		{
			sLocale += "_" + locale.getVariant();
		}
		return sLocale;

	}

	/**
	 * Prints the pattern.
	 */
	private static void printPattern()
	{
		final Locale jLocale = Locale.getDefault();
		DateFormat s = DateFormat.getDateInstance(DateFormat.SHORT, jLocale);
		_log.debug("LOG_SYSTEM_OUT",jLocale + "\t" + ((SimpleDateFormat) s).toPattern() + "\t" + printLocale(jLocale),100L);

	}

	/**
	 * Prints the pattern.
	 *
	 * @param locale the locale
	 */
	private static void printPattern(String locale)
	{
		final Locale jLocale = getLocale(locale);
		DateFormat s = DateFormat.getDateInstance(DateFormat.SHORT, jLocale);
		_log.debug("LOG_SYSTEM_OUT",locale + "\t" + ((SimpleDateFormat) s).toPattern() + "\t" + printLocale(jLocale),100L);
	}

	/**
	 * Retrieve date format.
	 *
	 * @param state the state
	 * @return the date format
	 */
	static public DateFormat retrieveDateFormat(StateInterface state)
	{
		DateFormat format;
		if ((getReportServerType(state).equalsIgnoreCase(SERVER_TYPE_BIRT)))
		{
			String shortOriginalPattern = ((SimpleDateFormat) birtDateFormat).toPattern();
			String longYearPatternString = shortOriginalPattern.replaceFirst("yy", "yyyy");
			format = new SimpleDateFormat(longYearPatternString);
		}
		else
		{
			format = cognosDateFormat;
		}
		return format;
	}

	/**
	 * Retrieve report pdfurl.
	 *
	 * @param state the state
	 * @param reportID the report id
	 * @param parametersAndValues the parameters and values
	 * @return the string
	 */
	static public String retrieveReportPDFURL(StateInterface state, String reportID, HashMap parametersAndValues)
	{
		StringBuffer reportURL = new StringBuffer();

		reportURL.append(retrieveReportURLStart(state, reportID));
		if ((reportURL == null) || reportURL.toString().equals("null") || reportURL.toString().matches("\\s*"))
		{
			return null;
		}
		reportURL.append(retrieveReportURLEnd(state, parametersAndValues));
		//reportURL.append("&__format=pdf");
		reportURL.append("&__format=");
		reportURL.append(retrieveBackgroundPrintReportFormat());
		_log.debug("LOG_DEBUG_EXTENSION_ReportUtil", "PDF URL " + reportURL.toString(), SuggestedCategory.NONE);
		return reportURL.toString();
	}
	
	/**
	 * Retrieves the format to be used for creating the report during the background printing application flow
	 * 
	 *  The logic used is:
	 *  First check if we have setup a parameter to specify the background printing report format. Return this if available.
	 *  If not then check OS. For AIX return ps i.e. PostScript for other return pdf
	 *  
	 * @return
	 */
	public static String retrieveBackgroundPrintReportFormat() {
		// check if we have setup a parameter to specify the background printing report format
		SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();
		String bgFileFormat = appAccess.getValue(WEBUICONFIG_FILENAME, WEBUICONFIG_KEY_BACKGROUNDPRINTFILEFORMAT);
		
		// if we find a configured value use it
		// otherwise let's check what OS we are running on
		if (bgFileFormat != null) {
			// found it
			_log.debug("LOG_DEBUG_EXTENSION_ReportUtil", "BGPrintFileFormat found in webUIConfig:" + bgFileFormat, SuggestedCategory.NONE);
			return bgFileFormat;
		} else {
			// it is not configured. In this case let's check what OS we are running under.
			// if AIX or Linux, we need to return PostScript format i.e. the value postscript
			// otherwise return pdf. Right now let's check specifically for AIX and Linux since that is 
			// the only variation we have. This will need to be revisited as we add support for
			// other platforms. 
			// the format type that is being returned is based on the background printing 
			// implementation where we rely on GhostScript. In UNIX (AIX and Linux), it has been found
			// that postscript appears to be better in the printed output.
			String osname = System.getProperty(SYSTEM_PROPERTY_KEY_OS_NAME);
			_log.debug("LOG_DEBUG_EXTENSION_ReportUtil", "os.name system property is:" + osname, SuggestedCategory.NONE);
			if (SYSTEM_PROPERTY_VALUE_OS_NAME_AIX.equalsIgnoreCase(osname) || SYSTEM_PROPERTY_VALUE_OS_NAME_LINUX.equalsIgnoreCase(osname)) {
				_log.debug("LOG_DEBUG_EXTENSION_ReportUtil", "format returned is:" + PARAM_VALUE_FORMAT_PS, SuggestedCategory.NONE);
				return PARAM_VALUE_FORMAT_PS;
			} else {
				_log.debug("LOG_DEBUG_EXTENSION_ReportUtil", "format returned is:" + PARAM_VALUE_FORMAT_PDF, SuggestedCategory.NONE);
				return PARAM_VALUE_FORMAT_PDF;
			}
		}
	}

	/**
	 * Retrieve report url.
	 *
	 * @param state the state
	 * @param reportid the reportid
	 * @param parametersAndValues the parameters and values
	 * @return the string
	 * @throws UserException the user exception
	 */
	public static String retrieveReportURL(StateInterface state,
			String reportid, HashMap parametersAndValues) throws UserException {
		return retrieveReportURL(state, reportid, parametersAndValues, ActiveFlag.NULL);
	}

	/**
	 * Retrieve report url.
	 *
	 * @param state 	StateInterface
	 * @param reportID Report ID, found in the PBSRPT_REPORTS table, RPT_ID
	 * @param parametersAndValues HashMap of Parameters and Values,
	 * Example:
	 * HashMap paramsAndValues = new HashMap();
	 * paramsAndValues.put("p_pStartDate",pStartDate +"%2000%3a00%3a00.000" );
	 * paramsAndValues.put("p_pEndDate", pEndDate +"%2000%3a00%3a00.000");
	 * paramsAndValues.put("p_pInvoiceStart", invoiceStartS);
	 * paramsAndValues.put("p_pInvoiceEnd", invoiceEndS);
	 * paramsAndValues.put("p_pCustomer",pCustomerEnd);
	 * @param workflowFlag the suppress printing
	 * @return Returns the report URL as a string or null if unable to generate url
	 * @throws UserException the user exception
	 */
	static public String retrieveReportURL(StateInterface state, String reportID, HashMap parametersAndValues, ActiveFlag workflowFlag) throws UserException
	{
		StringBuffer reportURL = new StringBuffer();
		String servertype = getReportServerType(state);
		reportURL.append(retrieveReportURLStart(state, reportID));
		if ((reportURL == null) || reportURL.toString().equals("null") || reportURL.toString().matches("\\s*"))
		{
			return null;
		}
		reportURL.append(retrieveReportURLEnd(state, parametersAndValues));
		_log.debug("LOG_DEBUG_EXTENSION_ReportUtil", reportURL.toString(), SuggestedCategory.NONE);
		try
		{
			ReportPrintUtil.runAndPrintRpt(state, reportID, parametersAndValues, servertype, REPORTURL, workflowFlag);

		} catch (EpiDataException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return reportURL.toString();
	}

	//mark ma added for wave packing list printing***********************************************************	
	/**
	 * Retrieve report url.
	 *
	 * @param state the state
	 * @param reportID the report id
	 * @param parametersAndValues the parameters and values
	 * @param reportType the report type
	 * @return the string
	 * @throws UserException the user exception
	 */
	static public String retrieveReportURL(StateInterface state, String reportID, HashMap parametersAndValues, String reportType) throws UserException
	{
		StringBuffer reportURL = new StringBuffer();
		String servertype = getReportServerType(state);
		reportURL.append(retrieveReportURLStart(state, reportID));
		if ((reportURL == null) || reportURL.toString().equals("null") || reportURL.toString().matches("\\s*"))
		{
			return null;
		}
		reportURL.append(retrieveReportURLEnd(state, parametersAndValues));
		_log.debug("LOG_DEBUG_EXTENSION_ReportUtil", reportURL.toString(), SuggestedCategory.NONE);
		try
		{
			ReportPrintUtil.runAndPrintRpt(state, reportID, parametersAndValues, servertype, REPORTURL, reportType);

		} catch (EpiDataException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return reportURL.toString();
	}
	//end *********************************************************************************************

	/**
	 * Retrieve report url end.
	 *
	 * @param state the state
	 * @param parametersAndValues the parameters and values
	 * @return the string
	 */
	static public String retrieveReportURLEnd(StateInterface state, HashMap parametersAndValues)
	{
		StringBuffer reportURL = new StringBuffer();
		for (Iterator it = parametersAndValues.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry) it.next();
			String parameter = (String) entry.getKey();
			//Ensure the parameter starts with an &
			if (!parameter.startsWith("&"))
			{
				parameter = "&" + parameter;
			}
			Object value = entry.getValue();
			if (value instanceof ArrayList) {
				ArrayList multiParameter = (ArrayList) value;
				for(int i = 0; i < multiParameter.size(); i++)
				{
					try {
						reportURL.append(parameter + "=" + URLEncoder.encode((String) multiParameter.get(i), "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						_log.error("ReportUtil_retrieveReportURLEnd", "Error encoding URL parameter:" + parameter + "=" + multiParameter.get(i),
								SuggestedCategory.APP_EXTENSION);
						e.printStackTrace();
						reportURL.append(parameter + "=" + multiParameter.get(i));
					}	
				}

			}else
			{
				try {
					reportURL.append(parameter + "=" + URLEncoder.encode((String) value, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					_log.error("ReportUtil_retrieveReportURLEnd", "Error encoding URL parameter:" + parameter + "=" + value,
							SuggestedCategory.APP_EXTENSION);
					e.printStackTrace();
					reportURL.append(parameter + "=" + value);
				}
			}
			//_log.debug("LOG_DEBUG_EXTENSION_ReportUtil", reportURL.toString(), SuggestedCategory.NONE);
		}

		_log.debug("LOG_DEBUG_EXTENSION_ReportUtil", reportURL.toString(), SuggestedCategory.NONE);
		return reportURL.toString();
	}

	/**
	 * Retrieve report url start.
	 *
	 * @param state the state
	 * @param reportID the report id
	 * @return the string
	 */
	static public String retrieveReportURLStart(StateInterface state, String reportID)
	{
		StringBuffer reportURL = new StringBuffer();
		String serverType = getReportServerType(state);

		//Cognos Server URL
		SsaAccessBase appAccess = new SQLDPConnectionFactory();
		String cognosServerURL = appAccess.getValue("webUIConfig", "cognosServerURL");
		if(serverType == SERVER_TYPE_BIRT)
		{
			//need to perform some magic to handle different instances
			//get User's Language

			String usersLang = getLanguageLocaleString(state.getLocale().getJavaLocale());
			Config wmbirtConfig = SsaAccessBase.getConfig("WMBirt", "WMBirtConfig");
			String defaultURL = wmbirtConfig.getValue("lang.default");
			String langURL = wmbirtConfig.getValue("lang."+usersLang);
			if(langURL != null)
			{
				cognosServerURL = langURL;
			}
			else if(defaultURL != null)
			{
				cognosServerURL = defaultURL;
			}
			else
			{
				//do nothing - use cognosServerURL
			}

		}
		reportURL.append(cognosServerURL);
		_log.debug("LOG_DEBUG_EXTENSION_ReportUtil", "1: " + reportURL.toString(), SuggestedCategory.NONE);



		//Actual Report URL
		Query rptQuery = new Query("wm_pbsrpt_reports", "wm_pbsrpt_reports.RPT_ID = '" + reportID + "'", null);
		BioCollectionBean rpts = state.getDefaultUnitOfWork().getBioCollectionBean(rptQuery);
		String report_url = null;
		try
		{
			if (rpts.size() != 1)
			{
				_log.debug("LOG_DEBUG_EXTENSION_ReportUtil", "No unique record for the reportID was found " + reportID, SuggestedCategory.NONE);
				return null;
			}
			if (serverType == SERVER_TYPE_BIRT)
			{
				report_url = rpts.get(_NO_OFFSET).getValue("BIRTRPT_URL").toString();
			}
			else
			{
				report_url = rpts.get(_NO_OFFSET).getValue("RPT_URL").toString();
			}
		} catch (EpiDataException e)
		{
			_log.error(e);
			e.printStackTrace();
			return null;
		}
		reportURL.append(report_url);
		_log.debug("LOG_DEBUG_EXTENSION_ReportUtil", "2: " + reportURL.toString(), SuggestedCategory.NONE);

		//Options
		//report_url = cognosServerURL + report_url + "&run.prompt=false&ui.header=false&p_DATABASE=";
		if (serverType == SERVER_TYPE_BIRT)
		{
			reportURL.append("&__showtitle=false");
		}
		else
		{
			reportURL.append("&run.prompt=false&ui.header=false");
		}
		_log.debug("LOG_DEBUG_EXTENSION_ReportUtil", "3: " + reportURL.toString(), SuggestedCategory.NONE);

		//Database
		//reportURL.append("&p_DATABASE=");
		reportURL.append(PARAM_KEY_DB);
		HttpSession session = state.getRequest().getSession();
		Object objDatabaseName = session.getAttribute("dbDatabase");
		String databaseName = objDatabaseName == null ? "" : (String) objDatabaseName;
		//report_url = report_url + databaseName;
		reportURL.append(databaseName);
		_log.debug("LOG_DEBUG_EXTENSION_ReportUtil", "4: " + reportURL.toString(), SuggestedCategory.NONE);

		//Schema
		//reportURL.append("&p_SCHEMA=");
		reportURL.append(PARAM_KEY_SCHEMA);
		_log.debug("LOG_DEBUG_EXTENSION_ReportUtil", "5: " + reportURL.toString(), SuggestedCategory.NONE);
		Object objSchemaId = session.getAttribute("dbUserName");
		String schemaId = objSchemaId == null ? "" : (String) objSchemaId;
		reportURL.append(schemaId.toUpperCase());
		_log.debug("LOG_DEBUG_EXTENSION_ReportUtil", "6: " + reportURL.toString(), SuggestedCategory.NONE);

		//Connection
		Object objConnection = session.getAttribute("dbConnectionName");
		String connectionName = objConnection == null ? "" : (String) objConnection;
		reportURL.append(PARAM_KEY_DBCONN + connectionName);
		
		//Billing Connection
		reportURL.append(PARAM_KEY_BILLCONN);
		reportURL.append(BIC_REPORT);

		//Username
		//reportURL.append("&p_uid=" + UserUtil.getUserId(state));
		reportURL.append(PARAM_KEY_UID + UserUtil.getUserId(state));
		_log.debug("LOG_DEBUG_EXTENSION_ReportUtil", "7: " + reportURL.toString(), SuggestedCategory.NONE);

		//Locale Stuff 
		String locale = state.getLocale().getJavaLocale().getLanguage();
		//reportURL.append("&outputLocale=" + locale);
		reportURL.append(PARAM_KEY_OUTLOCALE + locale);
		try {
			reportURL.append(PARAM_KEY_LOCALE + DBLanguageUtil.normalizeLocale(state.getLocale().getJavaLocale(), state).toString());
		} catch (Exception e) {
			e.printStackTrace();
			_log.error("ReportUtil_retrieveReportURLStart", "Error appending p_locale ",
					SuggestedCategory.APP_EXTENSION);
		}
		if (serverType == SERVER_TYPE_BIRT)
		{
			//			printPattern();
			//			printPattern("nl");
			//			printPattern("nl_DE");
			//			printPattern("es");
			//			printPattern("es_MX");
			//			printPattern("fr");
			//			printPattern("fr_FR");
			//			printPattern("en");
			//			printPattern("en_US");
			//			printPattern("en_GB");
			//			printPattern("en_IN");
			final Locale birtLocale = getLanguageCountryLocale(state.getLocale().getJavaLocale());
			reportURL.append(PARAM_KEY_BIRT_LOCALE + birtLocale);
		}

		if(serverType == SERVER_TYPE_BIRT)
		{
			TimeZone userTimeZone = getTimeZone(state);
			reportURL.append(PARAM_KEY_TMZ + _NO_OFFSET);
			
			reportURL.append(PARAM_KEY_TMZID + userTimeZone.getID());
		}
		//Incident2631884_Defect150016.b
		else if(serverType == SERVER_TYPE_COGNOS)
		{
			TimeZone userTimeZone = getTimeZone(state);
			reportURL.append(COG_PARAM_KEY_TMZ + offsetInMinutes(userTimeZone));
		}
		
		if (serverType == SERVER_TYPE_BIRT) {
			String id = state.getRequest().getSession().getId();
			if (state instanceof EpnyInProcStateImpl) {
				id = ((EpnyInProcStateImpl) state).getSessionId();
			}
			reportURL.append(PARAM_KEY_SESSION + id);
		}
		//Incident2631884_Defect150016.e
		//_log.debug("LOG_DEBUG_EXTENSION_ReportUtil", "8: " + reportURL.toString(), SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION_ReportUtil", reportURL.toString(), SuggestedCategory.NONE);

		reportURL.append(PARAM_DB_TYPE + getDBType(state));		//HC
		
		return reportURL.toString();

	}
	public static String getDBType(StateInterface state) {
		
		EpnyUserContext userCtx = state.getServiceManager().getUserContext();
		HttpSession session = state.getRequest().getSession();
		//O90 - Oracle
		//MSS - SQL Server
		String serverType = (String) session.getAttribute(SetIntoHttpSessionAction.DB_TYPE);
		if (serverType == null)
		{
			serverType = (String) userCtx.get(SetIntoHttpSessionAction.DB_TYPE);
		}
		return serverType;
	}
}
