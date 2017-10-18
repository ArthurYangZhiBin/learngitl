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

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.SsaAccessBase;

// TODO: Auto-generated Javadoc
/**
 * The Class ReportPrintUtil.
 */
public class ReportPrintUtil {

	/** The _log. */
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ReportPrintUtil.class);
	
	/** token for printer name */
	private static final String TOKEN_PRINTERNAME = "%%PRINTER%%";
	/** token for file name */
	private static final String TOKEN_FILENAME = "%%FILENAME%%";
	/** token for options */
	private static final String TOKEN_POPTIONS = "%%OPTIONS%%";
	/** space */
	private static final String TOKEN_SPACE = " ";
	/** the constant PARAM_KEY_POPTIONS_LANSCAPE  */
	private static final String PARAM_KEY_POPTIONS_LANDSCAPE = "&poptions=l";
	/** default value to be used for TOKEN_POPTIONS */
	private static final String POPTIONS_VALUE_DEFAULT = "-portrait";
	/** landscape format for TOKEN_POPTIONS */
	private static final String POPTIONS_VALUE_LANDSCAPE = "-landscape";
	/** Constant to define Unix shell command */
	private static final String UNIX_SHELL_CMD = "/bin/ksh";
	/** Constant to define shell command option */
	private static final String UNIX_SHELL_CMD_OPTION = "-c";


	/**
	 * Retrieve cognos server url.
	 *
	 * @return the string
	 */
	static public String retrieveCognosServerURL()
	{
		//Cognos Server URL
		SsaAccessBase appAccess = new SQLDPConnectionFactory();
		String cognosServerURL = appAccess.getValue("webUIConfig", "cognosServerURL");
		return cognosServerURL;
	}

	/**
	 * Retrieveprinter.
	 *
	 * @param rptsBioCollection the rpts bio collection
	 * @param wsdefaultsBioCollection the wsdefaults bio collection
	 * @param rptPrinterBioCollection the rpt printer bio collection
	 * @param rptUsrPrinterBioCollection the rpt usr printer bio collection
	 * @return the string
	 * @throws UserException the user exception
	 * @throws EpiDataException the epi data exception
	 */
	static public String retrieveprinter(BioCollectionBean rptsBioCollection,BioCollectionBean wsdefaultsBioCollection,BioCollectionBean rptPrinterBioCollection,BioCollectionBean rptUsrPrinterBioCollection) throws UserException, EpiDataException
	{
		Object rptUsrPrinter=null;		//Report by user Printer
		String rptUsrPrinterStr=null;
		Object rptPrintobj=null;		//Report Level Printer
		String rptPrintobjStr = null;
		Object usrPrintobj=null;		//User Level Printer
		String usrPrintobjStr=null;
		Object sysPrintobj=null;		//System Level Printer
		String sysPrintobjStr=null;
		String selectedPrinter = null;
		String usrLevelPrinterFlag=null; 
		if (rptUsrPrinterBioCollection.size()!=0){
			rptUsrPrinter = rptUsrPrinterBioCollection.get("0").getValue("PRINTERNAME");
			if (rptUsrPrinter != null){
				rptUsrPrinterStr = rptUsrPrinter.toString();
			}
		}

		if (rptsBioCollection.size()!=0){
			_log.debug("LOG_SYSTEM_OUT","found record for the Report",100L);
			rptPrintobj = rptsBioCollection.get("0").getValue("DEFAULTPRINTER");
			if (rptPrintobj != null){
				rptPrintobjStr = rptPrintobj.toString();
			}
		}

		if (wsdefaultsBioCollection.size()!=0){
			_log.debug("LOG_SYSTEM_OUT","found record for the user",100L);
			usrPrintobj = wsdefaultsBioCollection.get("0").getValue("DEFAULTPRINTER");
			usrLevelPrinterFlag = wsdefaultsBioCollection.get("0").getValue("USRLEVELPRNFLAG").toString();
			if (usrPrintobj != null){
				usrPrintobjStr = usrPrintobj.toString();
			}
		}

		if (rptPrinterBioCollection.size()!=0){
			_log.debug("LOG_SYSTEM_OUT","found record for the System Printer",100L);
			sysPrintobj = rptPrinterBioCollection.get("0").getValue("PRINTERNAME");
			if (sysPrintobj != null){
				sysPrintobjStr = sysPrintobj.toString();
				_log.debug("LOG_SYSTEM_OUT","found record for the System Printer"+ sysPrintobjStr,100L);
			}
		}

		if (rptUsrPrinterStr!=null){
			selectedPrinter = rptUsrPrinterStr;		//Printer at the User by Report level 
		}else{
			if (usrLevelPrinterFlag.equalsIgnoreCase("1")){
				if ((usrPrintobjStr!=null)&&(!usrPrintobjStr.equalsIgnoreCase(""))){
					selectedPrinter = usrPrintobjStr;	//Printer set at the User level
				}else{
					if ((rptPrintobjStr!= null)&&(!rptPrintobjStr.equalsIgnoreCase(""))){
						selectedPrinter = rptPrintobjStr;	//Printer set at the Report Level
					}else{
						if ((sysPrintobjStr != null)&& (! sysPrintobjStr.equalsIgnoreCase(""))){
							selectedPrinter = sysPrintobjStr;	//Printer set at the System level
						}else{
							_log.debug("LOG_DEBUG_EXTENSION_ReportUtil", "No Default Printer found " , SuggestedCategory.NONE);
							UserException UsrExcp = new UserException("WMEXP_NO_DEFAULTPRINTER", new Object[]{});
							throw UsrExcp;
						}
					}

				}

			}else{
				if ((rptPrintobjStr!= null)&&(!rptPrintobjStr.equalsIgnoreCase(""))){
					selectedPrinter = rptPrintobjStr;	//Printer set at the report level
				}else{
					if ((usrPrintobjStr!=null)&&(!usrPrintobjStr.equalsIgnoreCase(""))){
						selectedPrinter = usrPrintobjStr;	//Printer  set at the User Level
					}else{
						if ((sysPrintobjStr != null)&& (! sysPrintobjStr.equalsIgnoreCase(""))){
							selectedPrinter = sysPrintobjStr;	//Printer set at the system level
						}else{
							_log.debug("LOG_DEBUG_EXTENSION_ReportUtil", "No Default Printer found " , SuggestedCategory.NONE);
							UserException UsrExcp = new UserException("WMEXP_NO_DEFAULTPRINTER", new Object[]{});
							throw UsrExcp;
						}
					}
				}
			}
		}
		_log.debug("LOG_SYSTEM_OUT","REPORT Printer = "+ selectedPrinter,100L);
		return selectedPrinter;
	}

	/**
	 * Retrieve report search path.
	 *
	 * @param rptsBioCollection the rpts bio collection
	 * @return the string
	 */
	static public String retrieveReportSearchPath(BioCollectionBean rptsBioCollection)
	{
		String report_url = null;
		try
		{
			if (rptsBioCollection.size() != 1)
			{
				_log.debug("LOG_DEBUG_EXTENSION_ReportUtil", "No unique record for the reportID was found " , SuggestedCategory.NONE);
				return null;
			}
			report_url = rptsBioCollection.get("0").getValue("RPT_URL").toString();
		} catch (EpiDataException e)
		{
			_log.debug(e);
			e.printStackTrace();
			return null;
		}
		_log.debug("LOG_SYSTEM_OUT","REPORT URL = "+ report_url,100L);
		return report_url;
	}

	/**
	 * Prints the report cmd.
	 *
	 * @param rptsBioCollection the rpts bio collection
	 * @param wsdefaultsBioCollection the wsdefaults bio collection
	 * @param rptPrinterBioCollection the rpt printer bio collection
	 * @param rptUsrPrinterBioCollection the rpt usr printer bio collection
	 * @param state the state
	 * @param paramsAndValues the params and values
	 * @param serverType the server type
	 * @throws InterruptedException the interrupted exception
	 * @throws UserException the user exception
	 * @throws EpiDataException the epi data exception
	 */
	public static void PrintReportCmd(BioCollectionBean rptsBioCollection,BioCollectionBean wsdefaultsBioCollection,BioCollectionBean rptPrinterBioCollection,BioCollectionBean rptUsrPrinterBioCollection, StateInterface state, HashMap paramsAndValues, String serverType) throws InterruptedException, UserException, EpiDataException{

		//		String cognosDefaultPrinter = appAccess.getValue("webUIConfig", "cognosReportPrinter");
		String printer = null;
		printer = retrieveprinter(rptsBioCollection, wsdefaultsBioCollection, rptPrinterBioCollection, rptUsrPrinterBioCollection);

		printReport(rptsBioCollection, state, paramsAndValues, serverType, printer);

	}

	/**
	 * Prints the report cmd.
	 *
	 * @param rptsBioCollection the rpts bio collection
	 * @param state the state
	 * @param paramsAndValues the params and values
	 * @param serverType the server type
	 * @param printer the printer
	 * @throws EpiDataException the epi data exception
	 */
	public static void PrintReportCmd(BioCollectionBean rptsBioCollection, StateInterface state, HashMap paramsAndValues, String serverType, String printer) throws EpiDataException {
		printReport(rptsBioCollection, state, paramsAndValues, serverType, printer);
	}

	/**
	 * Prints the report.
	 *
	 * @param rptsBioCollection the rpts bio collection
	 * @param state the state
	 * @param paramsAndValues the params and values
	 * @param serverType the server type
	 * @param printer the printer
	 * @throws EpiDataException the epi data exception
	 */
	private static void printReport(BioCollectionBean rptsBioCollection, StateInterface state, HashMap paramsAndValues, String serverType, String printer) throws EpiDataException {
		String cognos_URL = retrieveCognosServerURL();
		String reportPath = retrieveReportSearchPath(rptsBioCollection);
		String parameters = retrieveReportURLEnd(state, paramsAndValues);
		SsaAccessBase appAccess = new SQLDPConnectionFactory();
		if(serverType.equalsIgnoreCase("BIRT")){
			_log.debug("LOG_SYSTEM_OUT","****PDF URL="+ReportUtil.retrieveReportPDFURL(state,rptsBioCollection.get("0").getValue("RPT_ID").toString(), paramsAndValues),100L);
			prnBirtRpt(state, ReportUtil.retrieveReportPDFURL(state, rptsBioCollection.get("0").getValue("RPT_ID").toString(), paramsAndValues), printer);
		}else{
			String cmd = appAccess.getValue("webUIConfig", "BGPrintCmd") + " " + cognos_URL + " " + reportPath + " " + parameters + " " + printer;
			_log.debug("LOG_SYSTEM_OUT","COMMAND = "+ cmd.toString(),100L);
			try {

				Process p = Runtime.getRuntime().exec(cmd);

				BufferedReader stdInput = new BufferedReader(new 
						InputStreamReader(p.getInputStream()));

				BufferedReader stdError = new BufferedReader(new 
						InputStreamReader(p.getErrorStream()));

				// read the output from the command
				String line = null;
				_log.debug("LOG_SYSTEM_OUT","Here is the standard output of the command:\n",100L);
				while ((line = stdInput.readLine()) != null) {
					_log.debug("LOG_SYSTEM_OUT",line,100L);
				}

				// read any errors from the attempted command

				_log.debug("LOG_SYSTEM_OUT","Here is the standard error of the command (if any):\n",100L);
				while ((line = stdError.readLine()) != null) {
					_log.debug("LOG_SYSTEM_OUT",line,100L);
				}

			}
			catch (IOException e) {
				_log.debug("LOG_SYSTEM_OUT","exception happened - here's what I know: ",100L);
				e.printStackTrace();
			}
		}

		_log.debug("LOG_SYSTEM_OUT","COMMAND EXECUTED",100L);
		//		proc.waitFor();
	}

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
		reportURL.append("&p_DATABASE=");
		//Database
		HttpSession session = state.getRequest().getSession();
		Object objDatabaseName = session.getAttribute("dbDatabase");
		String databaseName = objDatabaseName == null ? "" : (String) objDatabaseName;
		//report_url = report_url + databaseName;
		reportURL.append(databaseName);

		//Schema
		//report_url = report_url + "&p_SCHEMA=";
		reportURL.append("&p_SCHEMA=");

		//Username
		Object objUserId = session.getAttribute("dbUserName");
		String userId = objUserId == null ? "" : (String) objUserId;
		//report_url = report_url + userId;
		reportURL.append(userId.toUpperCase());

		for (Iterator it = parametersAndValues.entrySet().iterator(); it.hasNext();)
		{
			Map.Entry entry = (Map.Entry) it.next();
			String parameter = (String) entry.getKey();
			//Ensure the parameter starts with an &
			if (!parameter.startsWith("&"))
			{
				parameter = "&" + parameter;
			}
			String value = (String) entry.getValue();
			reportURL.append(parameter + "=" + value);
			_log.debug("LOG_DEBUG_EXTENSION_ReportUtil", reportURL.toString(), SuggestedCategory.NONE);
		}

		//Locale Stuff 
		String locale = state.getLocale().getJavaLocale().getLanguage();
		reportURL.append("&outputLocale=" + locale);

		//Username
		reportURL.append("&p_uid=" + UserUtil.getUserId(state));
		//_log.debug("LOG_DEBUG_EXTENSION_ReportUtil", "7: " + reportURL.toString(), SuggestedCategory.NONE);


		return reportURL.toString();
	}

	/**
	 * Run and print rpt.
	 *
	 * @param state the state
	 * @param rptId the rpt id
	 * @param paramsAndValues the params and values
	 * @param serverType the server type
	 * @param sessionParam the session param
	 * @throws EpiDataException the epi data exception
	 * @throws UserException the user exception
	 * @throws InterruptedException the interrupted exception
	 */
	static public void runAndPrintRpt(StateInterface state,String rptId, HashMap paramsAndValues, String serverType, String sessionParam) throws EpiDataException, UserException, InterruptedException{
		runAndPrintRpt(state, rptId, paramsAndValues, serverType, sessionParam, ActiveFlag.NULL);
	}

	/**
	 * Run and print rpt.
	 *
	 * @param state 	StateInterface
	 * @param rptId the rpt id
	 * @param paramsAndValues the params and values
	 * @param serverType the server type
	 * @param sessionParam the session param
	 * @param suppressPrinting the suppress printing
	 * @return Returns the report URL as a string or null if unable to generate url
	 * @throws EpiDataException the epi data exception
	 * @throws UserException the user exception
	 * @throws InterruptedException the interrupted exception
	 */
	static public void runAndPrintRpt(StateInterface state,String rptId, HashMap paramsAndValues, String serverType, String sessionParam, ActiveFlag workflowFlag) throws EpiDataException, UserException, InterruptedException{
		//Get the Biocollections for wsdefaults
		Query wsdefaultsQuery = new Query("wsdefaults", "wsdefaults.USERID = '" + UserUtil.getUserId(state) + "' AND wsdefaults.ISENTERPRISE = '0'", null);
		BioCollectionBean wsdefaultsBioCollection = state.getDefaultUnitOfWork().getBioCollectionBean(wsdefaultsQuery);

		//Get the Biocollections for pbsrpt_reports
		Query rptQuery = new Query("wm_pbsrpt_reports", "wm_pbsrpt_reports.RPT_ID = '" + rptId + "'", null);
		BioCollectionBean rptsBioCollection = state.getDefaultUnitOfWork().getBioCollectionBean(rptQuery);

		//Get the Biocollections for reportprinter
		Query rptPrinterQuery = new Query("wm_reportprinter", "wm_reportprinter.DEFAULTPRINTERFLAG = '1'", null);
		BioCollectionBean rptPrinterBioCollection = state.getDefaultUnitOfWork().getBioCollectionBean(rptPrinterQuery);

		//Get the Biocollections for reportbyuserprinter
		Query rptUsrPrinterQuery = new Query("wm_report_printerbyuser", "wm_report_printerbyuser.USERID= '" + UserUtil.getUserId(state) + "' and wm_report_printerbyuser.RPT_ID = '" + rptId + "'", null);
		BioCollectionBean rptUsrPrinterBioCollection = state.getDefaultUnitOfWork().getBioCollectionBean(rptUsrPrinterQuery);


		if (checkRptPrintEnabled(wsdefaultsBioCollection)){
			Object rptPrintobj=null;
			rptPrintobj = rptsBioCollection.get("0").getValue("PRINTOPTION");
			if (rptPrintobj != null){
				_log.debug("ReportPrintUtil_runAndPrintRpt", "Workflow Flag " + workflowFlag.toString() + " Print Option " + rptPrintobj,
						SuggestedCategory.APP_EXTENSION);
				if (rptPrintobj.toString().equalsIgnoreCase("DISPRN")){
					//if workflow flag is set to false, suppress printing
					if(!ActiveFlag.FALSE.equals(workflowFlag)) {
						PrintReportCmd(rptsBioCollection,wsdefaultsBioCollection,rptPrinterBioCollection,rptUsrPrinterBioCollection,state,paramsAndValues,serverType);	
					} else {
						_log.debug("ReportPrintUtil_runAndPrintRpt", "Suppressing printing",
								SuggestedCategory.APP_EXTENSION);
					}
					EpnyUserContext userCtx = state.getServiceManager().getUserContext();
					userCtx.put("printOption", "DISPRN");
				}
				if (rptPrintobj.toString().equalsIgnoreCase("DIS")){
					//WorkFlowFlag is set to true, force printing
					if(ActiveFlag.TRUE.equals(workflowFlag))	{
						_log.debug("ReportPrintUtil_runAndPrintRpt", "Forcing printing",
								SuggestedCategory.APP_EXTENSION);
						PrintReportCmd(rptsBioCollection,wsdefaultsBioCollection,rptPrinterBioCollection,rptUsrPrinterBioCollection,state,paramsAndValues,serverType);
					}
					EpnyUserContext userCtx = state.getServiceManager().getUserContext();
					userCtx.put("printOption", "DIS");
				}
				if (rptPrintobj.toString().equalsIgnoreCase("PRN")){
					//if workflow flag is set to false, suppress printing
					if(!ActiveFlag.FALSE.equals(workflowFlag)) {
						
						PrintReportCmd(rptsBioCollection,wsdefaultsBioCollection,rptPrinterBioCollection,rptUsrPrinterBioCollection,state,paramsAndValues,serverType);
					}
					else{
						_log.debug("ReportPrintUtil_runAndPrintRpt", "Suppressing printing",
								SuggestedCategory.APP_EXTENSION);
					}
					EpnyUserContext userCtx = state.getServiceManager().getUserContext();
					userCtx.put("printOption", "PRN");
				}
			}else{
				//Safety check
				//Not possible to get to this branch in normal operation
				EpnyUserContext userCtx = state.getServiceManager().getUserContext();
				userCtx.put("printOption", "DIS");
			}
		}else{
			EpnyUserContext userCtx = state.getServiceManager().getUserContext();
			userCtx.put("printOption", "DIS");
		}
	}



	//mark ma added for wave packing list printing***********************************************************
	/**
	 * Run and print rpt.
	 *
	 * @param state the state
	 * @param rptId the rpt id
	 * @param paramsAndValues the params and values
	 * @param serverType the server type
	 * @param sessionParam the session param
	 * @param reportType the report type
	 * @throws EpiDataException the epi data exception
	 * @throws UserException the user exception
	 * @throws InterruptedException the interrupted exception
	 */
	static public void runAndPrintRpt(StateInterface state,String rptId, HashMap paramsAndValues, String serverType, String sessionParam, String reportType) throws EpiDataException, UserException, InterruptedException{
		//Get the Biocollections for wsdefaults
		Query wsdefaultsQuery = new Query("wsdefaults", "wsdefaults.USERID = '" + UserUtil.getUserId(state) + "' AND wsdefaults.ISENTERPRISE = '0'", null);
		BioCollectionBean wsdefaultsBioCollection = state.getDefaultUnitOfWork().getBioCollectionBean(wsdefaultsQuery);

		//Get the Biocollections for pbsrpt_reports
		Query rptQuery = new Query("wm_pbsrpt_reports", "wm_pbsrpt_reports.RPT_ID = '" + rptId + "'", null);
		BioCollectionBean rptsBioCollection = state.getDefaultUnitOfWork().getBioCollectionBean(rptQuery);

		//Get the Biocollections for reportprinter
		Query rptPrinterQuery = new Query("wm_reportprinter", "wm_reportprinter.DEFAULTPRINTERFLAG = '1'", null);
		BioCollectionBean rptPrinterBioCollection = state.getDefaultUnitOfWork().getBioCollectionBean(rptPrinterQuery);

		//Get the Biocollections for reportbyuserprinter
		Query rptUsrPrinterQuery = new Query("wm_report_printerbyuser", "wm_report_printerbyuser.USERID= '" + UserUtil.getUserId(state) + "' and wm_report_printerbyuser.RPT_ID = '" + rptId + "'", null);
		BioCollectionBean rptUsrPrinterBioCollection = state.getDefaultUnitOfWork().getBioCollectionBean(rptUsrPrinterQuery);


		if (checkRptPrintEnabled(wsdefaultsBioCollection)){
			PrintReportCmd(rptsBioCollection,wsdefaultsBioCollection,rptPrinterBioCollection,rptUsrPrinterBioCollection,state,paramsAndValues,serverType);
			EpnyUserContext userCtx = state.getServiceManager().getUserContext();
			userCtx.put("printOption", "PRN");
		}else{
			throw new UserException("WPEXP_WAVE_PACKING_LIST_PRINTING_NOT_DEFINED", new Object[]{});
		}
	}

	//end *********************************************************************************************





	/**
	 * Check rpt print enabled.
	 *
	 * @param wsdefaultsBioCollection the wsdefaults bio collection
	 * @return true, if successful
	 * @throws EpiDataException the epi data exception
	 */
	static public boolean checkRptPrintEnabled(BioCollectionBean wsdefaultsBioCollection) throws EpiDataException{
		//Check if the background printing flag is set for the user
		Object rptPrintobj = wsdefaultsBioCollection.get("0").getValue("BACKGROUNDPRNFLAG");
		if ((rptPrintobj != null)&&(rptPrintobj.toString().equalsIgnoreCase("1"))){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * Prn birt rpt.
	 *
	 * @param state the state
	 * @param birtRptURL the birt rpt url
	 * @param printer the printer
	 */
	static public void prnBirtRpt(StateInterface state,String birtRptURL, String printer){
		try {
			String line=null;

			//path for saving pdf files
			String fileSeparator = System.getProperties().getProperty("file.separator");
			SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();
			String oahome = appAccess.getValue("webUIConfig","OAHome");
			String path = oahome+fileSeparator+"shared"+fileSeparator+"webroot"+fileSeparator+"app"+fileSeparator+"rptPDFtmp";
			String userId = (String)state.getServiceManager().getUserContext().get("logInUserId");
			//clear the temp files for this session
			File tempFolder = new File(path);
			File [] files = tempFolder.listFiles();
			File tempFile = null;
			String tempFileName = "";
			int size = files.length;
			if(size != 0){
				for(int i=0; i<size; i++){
					tempFile = files[i];									
					tempFileName = tempFile.getName();
					if(tempFileName.startsWith(userId)){
						tempFile.delete();
					}					
				}
			}

			URL urls = new URL(birtRptURL);
			
			// we were earlier always creating pdf extension. Now we need to check based on rules as defined in ReportUtil.retrieveBackgroundPrintReportFormat
			String localFileName = userId +"_"+System.currentTimeMillis()+ "." + ReportUtil.retrieveBackgroundPrintReportFormat();
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path+fileSeparator+localFileName));
			InputStream input = urls.openStream();
			byte[] buffer = new byte[1024];
			int read;
			while ((read = input.read(buffer, 0, buffer.length)) != -1) {
				out.write(buffer, 0, read);
			}
			input.close();
			out.close();
			_log.debug("LOG_SYSTEM_OUT","Done...........",100L);
			// String cmd = appAccess.getValue("webUIConfig", "BGPrintCmd")+" -p "+printer+" "+path+fileSeparator+localFileName;
			String cmd = parseBackgroundPrintCmd(birtRptURL, appAccess.getValue("webUIConfig", "BGPrintCmd"), printer, path+fileSeparator+localFileName);			
			_log.debug("LOG_SYSTEM_OUT",cmd,100L);

			// check if OS is other than Windows as in that case we need to create shell for the background print command
			// maybe we need to use Apache Exec instead?
			String osname = System.getProperty(ReportUtil.SYSTEM_PROPERTY_KEY_OS_NAME);
			String[] cmdUnix = { UNIX_SHELL_CMD, UNIX_SHELL_CMD_OPTION, cmd };
			java.lang.Process p = null;
			if (ReportUtil.SYSTEM_PROPERTY_VALUE_OS_NAME_AIX.equalsIgnoreCase(osname) || ReportUtil.SYSTEM_PROPERTY_VALUE_OS_NAME_LINUX.equalsIgnoreCase(osname)) {
				p = Runtime.getRuntime().exec(cmdUnix);
			} else {
				p = Runtime.getRuntime().exec(cmd);	
			}							

			BufferedReader stdInput = new BufferedReader(new 
					InputStreamReader(p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new 
					InputStreamReader(p.getErrorStream()));

			// read the output from the command

			_log.debug("LOG_SYSTEM_OUT","Here is the standard output of the command:\n",100L);
			while ((line = stdInput.readLine()) != null) {
				_log.debug("LOG_SYSTEM_OUT",line,100L);
			}

			// read any errors from the attempted command

			_log.debug("LOG_SYSTEM_OUT","Here is the standard error of the command (if any):\n",100L);
			while ((line = stdError.readLine()) != null) {
				_log.debug("LOG_SYSTEM_OUT",line,100L);
			}



		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			_log.error("Print Error", "Print error", e);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			_log.error("Print Error", "Print error", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			_log.error("Print Error", "Print error", e);
		}
	}
	
	/**
	 * Parses the background print command that is in the <code>webUIconfig.xml</code>.
	 * The parameter name is <code>BGPrintCmd</code>. We are expecting the parameter value
	 * to be of the form
	 * <p><code>
	 * gsprint -printer %%PRINTER%% -portrait %%FILENAME%%
	 * </code></p>
	 * 
	 * @param birtRptURL the URL for the BIRT report to be executed
	 * @param printCommand the print command from the configuration file
	 * @param printer the name of the printer to which the file needs to be printed
	 * @param file the name of the file to be printed
	 * @return string that contains the background print command with the replaced tokens 
	 */
	private static String parseBackgroundPrintCmd(String birtRptURL, String printCommand, String printer, String file) {
		
		// variable to store replaced tokens
		StringBuffer newcmd1 = new StringBuffer();
		// split on printer name token
		String[] arr1 = printCommand.split(TOKEN_PRINTERNAME);
		// assemble back
		newcmd1.append(arr1[0]);
		//newcmd1.append(TOKEN_SPACE);
		newcmd1.append(printer);
		//newcmd1.append(TOKEN_SPACE);
		// we may not have another token based on the location of the printer name token
		if (arr1.length > 1) {
			newcmd1.append(arr1[1]);	
		}
		
		// now let's replace the file name token
		// variable to store replaced tokens
		StringBuffer newcmd2 = new StringBuffer();
		// split on file name token
		arr1 = newcmd1.toString().split(TOKEN_FILENAME);
		// assemble back
		newcmd2.append(arr1[0]);
		//newcmd2.append(TOKEN_SPACE);
		newcmd2.append(file);
		//newcmd2.append(TOKEN_SPACE);
		// we may not have another token based on the location of the file name token
		if (arr1.length > 1) {
			newcmd2.append(arr1[1]);	
		}
		
		
		// check if TOKEN_POPTIONS is present in the command. Do replacement logic only if found.
		if (!(newcmd2.toString().indexOf(TOKEN_POPTIONS) == -1)) {
			// parse the url for printing options token. this will tell us whether the report should be printed
			// in portrait or landscape format. The token/parameter in the url we are looking of is "poptions".
			// If we don't find it, then default the format to "-portrait".
			boolean isPortrait = false;
			// check if poptions is part of url and is set to landscape
			if (birtRptURL.toLowerCase().indexOf(PARAM_KEY_POPTIONS_LANDSCAPE) == -1) {
				isPortrait = true;
			}

			// variable to store replaced tokens
			StringBuffer newcmd3 = new StringBuffer();
			// split on options token
			arr1 = newcmd2.toString().split(TOKEN_POPTIONS);
			// assemble back
			newcmd3.append(arr1[0]);
			if (isPortrait) {
				newcmd3.append(POPTIONS_VALUE_DEFAULT);
			} else {
				newcmd3.append(POPTIONS_VALUE_LANDSCAPE);
			}	
			// we may not have another token based on the location of the poptions token
			if (arr1.length > 1) {
				newcmd3.append(arr1[1]);	
			}
			
			return newcmd3.toString();
		} else {
			// we should come here only in AIX right now
			_log.debug("LOG_DEBUG_EXTENSION_ReportPrintUtil", "Options token not found in command:" + newcmd2.toString(), SuggestedCategory.NONE);
			return newcmd2.toString();
		}
	}
}
