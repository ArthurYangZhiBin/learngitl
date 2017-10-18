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
package com.infor.scm.waveplanning.common;

/**
 * TODO Document WavePlanningUtils class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.epiphany.shr.ui.state.StateInterface;
import com.infor.scm.waveplanning.common.util.WPUserUtil;
import com.infor.scm.waveplanning.common.util.WPUtil;
import com.ssaglobal.cs.util.algorithm.CipherUtil;
import com.ssaglobal.cs.util.config.ConfigService;
import com.ssaglobal.cs.util.logging.SuggestedCategory;




/** 
 * Write the overview of this class
 *For example:
 * <pre>
 *    Insert the example code here
 * </pre>
 *
 * @version 1.0 
 * @since <<insert Service Name>> 1.0
 * @Created Nov 29, 2005
 * @author VVoora
 * @see "list related classes"
 * 
 */

public class WavePlanningUtils{
    
	public static String wmsName                    =   null;
	public static String dbDateFormat               =   null;  
	public static String schedulerAdminName         =   null;
	public static String schedulerAdminEmailId      =   null;
	public static String onlineHelpLocation         =   null;
	public static int WP_LIST_PAGE_SIZE             =   10;
	public static int ES_LIST_PAGE_SIZE             =   10;
	public static int noOfRecordsPerReport          =   10;
	public static int noOfAlertsInHomePage          =   10;
	public static String appServerVendor            =   null;
	public static String sourceDatabaseVendor       =   null;
	public static String archiveDBVendor            =   null;
	public static String xmlPath                    =   null;
	public static String wpDatabaseName             =   null;
	public static String wms4000ConfigParameter     =   null;
	
	//added for sheduler 
	public static String scheduler_user = null;
	
	//public static String orderCount                 =   null;
	//public static String itemCount                  =   null;
	//public static String wmsapidateformat           =   null;
	
	static{
		
			String pageSize        = null;
			wmsName                = WPUtil.getWaveConfigValue("wp_wmsbackend");
			dbDateFormat           = WPUtil.getWaveConfigValue("dbdateformat");
			schedulerAdminName     = WPUtil.getWaveConfigValue("ESAdminName");
			schedulerAdminEmailId  = WPUtil.getWaveConfigValue("ESAdminEmailID");
			onlineHelpLocation     = WPUtil.getWaveConfigValue("wp_onlinehelp_location");
			pageSize               = WPUtil.getWaveConfigValue("wp_listpagesize");
			if(pageSize!=null && pageSize.length()!=0)
			 WP_LIST_PAGE_SIZE     = Integer.parseInt(pageSize);
			pageSize               = null;
			pageSize               = WPUtil.getWaveConfigValue("es_listpagesize"); 
			if(pageSize!=null && pageSize.length()!=0)
			 ES_LIST_PAGE_SIZE     = Integer.parseInt(pageSize);	
			pageSize               = null;
			pageSize               = WPUtil.getWaveConfigValue("wp_reports_recordsperpagecount");
			if(pageSize!=null && pageSize.length()!=0)
			 noOfRecordsPerReport  = Integer.parseInt(pageSize);
			pageSize               = null;
			pageSize = WPUtil.getWaveConfigValue("wp_homepagealertscount");
			if(pageSize!=null && pageSize.length()!=0)
			 noOfAlertsInHomePage  = Integer.parseInt(pageSize);
			appServerVendor        = WPUtil.getWaveConfigValue("wp_appservervendor");
			sourceDatabaseVendor   = WPUtil.getWaveConfigValue("wp_dbvendor");
			archiveDBVendor        = WPUtil.getWaveConfigValue("wp_archivedbvendor");
			xmlPath                = WPUtil.getWaveConfigValue("wp_xmlpath");	
			wpDatabaseName         = WPUtil.getWaveConfigValue("wpDatabaseName");
			wms4000ConfigParameter = WPUtil.getWaveConfigValue("wms4000ConfigParameter");
			//orderCount             = WPUtil.getWaveConfigValue("wp_ordercount");
			//itemCount              = WPUtil.getWaveConfigValue("wp_itemcount");
			//wmsapidateformat       = WPUtil.getWaveConfigValue("wms_apidateformat");
			
			scheduler_user = WPUtil.getWaveConfigValue("SchedulerUser");
		
	}
    
    
   
    
    

    public static String getCurrentUserID(StateInterface state)
    {        
        return WPUserUtil.getUserId(state);
    }
    public static String getFacility(HttpServletRequest request,HttpServletResponse response)
    {
       return WPUtil.getFacility(request);
    }
    public static ArrayList getFacilityListOfUser(HttpServletRequest request,HttpServletResponse response)
    {
        ArrayList fac=new ArrayList();
        fac.add(getFacility(request, response));
        return fac;
    }
    
    /* Method to change the Webcontext Facility */ 
    
//    public static void updateFacility(HttpServletRequest request,HttpServletResponse response,String facility)
//    {
////        HttpSession session=request.getSession();
////        WebContext ctxt = (WebContext)session.getAttribute(Constants.SSA_WEB_CONTEXT);
////        ctxt.setAttribute(WavePlanningConstants.WP_FACILITY,facility);
//    }
//    public static void removeFacilityFormWebContext(HttpServletRequest request,HttpServletResponse response)
//    {
////        HttpSession session=request.getSession();
////        WebContext ctxt = (WebContext)session.getAttribute(Constants.SSA_WEB_CONTEXT);
////        ctxt.removeAttribute(WavePlanningConstants.WP_FACILITY);
//    }
////    public static String getDefaultFacility(HttpServletRequest request,HttpServletResponse response)
//    {
//        String facility = "";
//        try
//        {
//            HttpSession session=request.getSession();
//            WebContext ctxt = (WebContext)session.getAttribute(Constants.SSA_WEB_CONTEXT);
//            User user=ctxt.getCurrentUser();
//            if(user.getAttributeValue("defaultfacility")!=null)
//            {
//                facility=(String)user.getAttributeValue("defaultfacility");
//            }  
//        }
//        catch(Exception exe)
//        {
//			Log objLog = LogFactory.getLog(WavePlanningUtils.class);
//	    	objLog.error(exe.getMessage());
//        }
//        return facility;
//    }
//    public static void updateDefaultFacility(HttpServletRequest request,HttpServletResponse response,String defaultFacility)
//    {
//		try{
//			
//			HttpSession session=request.getSession();
//	        WebContext ctxt = (WebContext)session.getAttribute(Constants.SSA_WEB_CONTEXT);
//			UserManagementService ums = UserManagementService.getInstanceByProxy((UserManagementProxy)ctxt.getServiceProxy(ServicesConstants.UMS_SERVICEID));
//	        ums.bind(ctxt.getContextObject());
//	        //Only Default UserStore working 
//	        UserStore userstore = ums.getDefaultUserStore();
//	        User user = userstore.getUserBySubject(ctxt.getSubject());
//			
//			if(user!= null)
//			 {
//				 user.setAttributeValue("defaultfacility", defaultFacility);
//				 ctxt.setCurrentUser(user);
//				 String hierarchyDN = user. getHierarchyDN ( );
//				 Hierarchy hierarchy = userstore.getHierarchy(hierarchyDN);        
//				 hierarchy.modifyUser(user);
//				 ums.unbind();
//			 }
//		}
//		catch(Exception e)
//		{
//			Log objLog = LogFactory.getLog(WavePlanningUtils.class);
//	    	objLog.error(e.getMessage());
//		}
//    }
    
    /* Method to get the current UserLocale */
    
    public static String getUserLocale(HttpServletRequest request,HttpServletResponse response)
    {               
        return WPUserUtil.getUserLocale().toString();
        // QA20070107.ecn
    }
    
    /* Method to get Locale specific DateTime */
    
    public static String formatWPDateTime(String valueToFormat, 
    		String locale)throws Exception {
    			String formattedStr = "";
    			Locale localeObj = null;
    			java.util.Date date = null;
				String l1 = null;
				String l2 = null;
    				try {	
						l1=locale.substring(0,2);
						l2=locale.substring(3,5);
    					localeObj = new Locale(l1,l2);
    					if(valueToFormat!=null && !"".equals(valueToFormat.trim()))
    					{
    						SimpleDateFormat sdf = new SimpleDateFormat(WavePlanningConstants.dateFormats.DATE_TIME);
    						ParsePosition pos = new ParsePosition(0);
    						date = sdf.parse(valueToFormat,pos);
    						formattedStr = formatDateTime(date ,localeObj);	
    					}
    				}catch(Exception e) {						
    					throw e;
    				}
    			return formattedStr;	
    		} 
    
    
    public static String formatDateTime(Object valueToFormat, 
    		java.util.Locale locale)throws Exception {
    		String formattedStr = "";
    		Format format = null;
    		try {
    		    
    			format = DateFormat.getDateTimeInstance(DateFormat.SHORT,DateFormat.MEDIUM,locale);
    			if(format != null)
    				formattedStr = format.format(valueToFormat);
    			else
    				formattedStr = valueToFormat.toString();
    		}catch(Exception e) {				
    			throw e;
    		}
    		return formattedStr;
    	} 
    
    /* Method to get Locale specific Date */    
    
    public static String formatWPDate(String valueToFormat,String locale)throws Exception {
    		String formattedStr = "";
    		java.util.Locale localeObj = null;
    		java.util.Date date = null;
			String l1 = null;
			String l2 = null;
    			try {	
					
					l1=locale.substring(0,2);
					l2=locale.substring(3,5);
					localeObj = new Locale(l1,l2);
    				
    				if(valueToFormat!=null && !"".equals(valueToFormat.trim()))
    				{
    					date = getUtilDate(valueToFormat,WavePlanningConstants.dateFormats.DATE_ONLY);
    					formattedStr = formatDate(date ,localeObj);		
    				}
    				
    			}catch(Exception e) {					
    				throw new Exception(e);
    			}
    		return formattedStr;	
    	} 
    
    public  static java.util.Date getUtilDate(String p_dateString,String p_format) throws Exception {
		String formatString = null;
		try {
			formatString = p_format;
			SimpleDateFormat sdf = new SimpleDateFormat(formatString);
			ParsePosition pos = new ParsePosition(0);
			java.util.Date d = sdf.parse(p_dateString,pos);
			return d;
		}catch(Exception ex){			
		    throw ex;
		}
     }	
    
    public static String formatDate(Object valueToFormat, 
    		java.util.Locale locale)throws Exception {
    			String formattedStr = "";
    			Format format = null;
    			try {
    				format = DateFormat.getDateInstance(DateFormat.SHORT,locale);
    				if(format != null)
    					formattedStr = format.format(valueToFormat);
    				else
    					formattedStr = valueToFormat.toString();
    				  
    			}catch(Exception e) {					
    				throw new Exception(e);
    			}
    			return formattedStr;
    	}
     
    
    
    /* Method to get Locale Specific Number */
    
    public static String formatWPNumber(String valueToFormat, String locale)throws Exception {
    		String formattedStr = "";
    		java.util.Locale localeObj = null;
    		Double value = null; 
    		String l1 = null;
			String l2 = null;
    			try {
					if(locale!=null && locale.length()!=0){
					l1=locale.substring(0,2);
					l2=locale.substring(3,5);
					localeObj = new Locale(l1,l2);
    				if(valueToFormat!=null && !"".equals(valueToFormat.trim()))
    				{
    					value = new Double(valueToFormat);
    					formattedStr = formatNumber(value , localeObj);
    				}
				}
    			}catch(Exception e) {					
    				throw e;
    			}
    			return formattedStr;	
    	} 
    public static String formatNumber(Object valueToFormat, 
    		java.util.Locale locale)throws Exception {
    		String formattedStr = "";
    		NumberFormat format = null;
    			try {
    				
    				format = NumberFormat.getNumberInstance(locale);
    				format.setMaximumFractionDigits(2);
    				format.setMinimumFractionDigits(2);
    				if(format != null)
    				{
    					return format.format(valueToFormat);
    				}
    				else{
    					return valueToFormat.toString();
    				}
    			}catch(Exception e) {					
    				throw new Exception(e);
    			}
    			
    		} 
    
    /* Method to Changing Date from one Format to another Format */
    
    public  static String getDate(String p_date,String p_fromFormat) 
	         throws Exception {
        
		java.util.Date date = null;
		Format formatter = null;
		String strDate = null;
		
		try {
		    formatter = new SimpleDateFormat(WavePlanningConstants.dateFormats.DATE_ONLY);
			date = getUtilDate(p_date,p_fromFormat);
			strDate = formatter.format(date);
			return strDate;
		}catch(Exception ex){			
		    throw ex;
		}
    }
   
    /* Method to get Locale Neutral Double */
    
    public  static String getDouble(String value, Locale locale)
	{
		Double result = null;
	
		if(value != null && !value.equals(""))
		{
			NumberFormat formatter = null;
			if(locale != null)
				formatter = NumberFormat.getNumberInstance(locale);
			else
				formatter = NumberFormat.getNumberInstance(Locale.getDefault());
			formatter.setMaximumFractionDigits(2);
			formatter.setMinimumFractionDigits(2);
			ParsePosition pos1 = new ParsePosition(0);
			Number tempNum = formatter.parse(value,pos1);
			value = formatter.format(tempNum);
			ParsePosition pos2 = new ParsePosition(0);
			Number number = formatter.parse(value,pos2);
			if(pos2.getErrorIndex() == -1 && pos2.getIndex() == value.length() && number.doubleValue() >= -1.7976931348623157E+308D && number.doubleValue() <= 1.7976931348623157E+308D)
				result = new Double(number.doubleValue());
		}
		if(result != null)
			return result.toString();
		else
			return "";
	}
	public  static boolean validateDate(String date) {
	       boolean dateflag=true;
		   try{
	           SimpleDateFormat formatter=new SimpleDateFormat("MM/dd/yyyy");
	           Date d=formatter.parse(date);
			   int month=Integer.parseInt(date.substring(0,2));
			   int day = Integer.parseInt(date.substring(3,5));
			   int year = Integer.parseInt(date.substring(6,10));
			   if(month > 0 && month < 13 && day >0 && day <= 31)
			   {
				   dateflag=true;
			   }
			   else
			   {
				   dateflag=false;
			   }
			   
	       }catch(Exception exe)
	       {
			   dateflag = false;
	       }
		   return dateflag;
	}
    
    /* Method to  get Locale Specific double */
    
    public  static String getLocaleSpecificDouble(String value, Locale locale)
	{
		String result = "";
		if(value != null && !value.equals(""))
		{
			NumberFormat formatter = null;
			if(locale != null)
				formatter = NumberFormat.getNumberInstance(locale);
			else
				formatter = NumberFormat.getNumberInstance(Locale.getDefault());
			formatter.setMaximumFractionDigits(2);
			formatter.setMinimumFractionDigits(2);
			result = formatter.format(Double.parseDouble(value));
		}
		return result;
	}
    public static String getCurrentSystemDate()
    {
        SimpleDateFormat formatter=new SimpleDateFormat(WavePlanningConstants.dateFormats.DATE_TIME);
        String currentSystemDate=formatter.format(new Date());
        return currentSystemDate;
        
    }
	public static boolean isUserExist(HttpServletRequest request,HttpServletResponse response,String value)
	 {
	   boolean isExist = false;
	   try {
				if(WPUserUtil.getUser(value) != null)
					isExist = true;
	    }
	  catch(Exception e) {
		  
      }
	 return isExist;
	}
	public static String getInDateStorageFormat(java.util.Date dateTimeToBeConverted) 
	{
		
		if(dateTimeToBeConverted!=null)
		{	
			SimpleDateFormat    sdf         		= null;
			String              output      		= null;
			StringBuffer		dateAsStrForOracle 	= null;
			
	        
			sdf = new SimpleDateFormat(WavePlanningUtils.dbDateFormat + " HH:mm:ss");
			output = sdf.format(dateTimeToBeConverted);
	
			if (WavePlanningUtils.sourceDatabaseVendor.equalsIgnoreCase(WavePlanningConstants.DB_TYPE_STRING_ORACLE))
			{
				dateAsStrForOracle = new StringBuffer("TO_DATE('");
				dateAsStrForOracle.append(output);
				dateAsStrForOracle.append("', '");
				dateAsStrForOracle.append("dd-MM-yyyy" + " hh24:MI:ss");
				dateAsStrForOracle.append("')");
				return(dateAsStrForOracle.toString());
			}
			else
			{
				System.out.println("In else block ");
				return(output);
			}
		}
		else
		{
			System.out.println("returning null  ");
			return null;
		}
	}	
	public static String getDBDependentDate(java.util.Date dateToBeConverted) 
	{
		
		if(dateToBeConverted!=null)
		{	
			SimpleDateFormat    sdf         		= null;
			String              output      		= null;
			StringBuffer		dateAsStrForOracle 	= null;
			sdf = new SimpleDateFormat(WavePlanningUtils.dbDateFormat);
			output = sdf.format(dateToBeConverted);
			return(output);
		}
		else
		{
			return null;
		}
	}	
	public static java.sql.Date getSQLDate(Date utilDate)
	{
		return new java.sql.Date(utilDate.getTime());
	}
	public static Timestamp getSQLDateTime(Date utilDate)
	{
		// Transition Bug.o
		//return new Timestamp(utilDate.getTime());
		// Transition Bug.sn
		SimpleDateFormat jdbcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		jdbcFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		Timestamp tsGMTNow;
		tsGMTNow = Timestamp.valueOf(jdbcFormat.format(utilDate));
//		Timestamp dateTime = new Timestamp(utilDate.getTime());
		tsGMTNow.setNanos(0);
		return tsGMTNow;
		// Transition Bug.en
	}
	
	/*Subodh: Added new method getSQLDtTime() for fixing the issue# SCM-00000-05697 
	since the method getSQLDateTime() got modified from previous version */
	public static Timestamp getSQLDtTime(Date utilDate)
	{
		Timestamp dateTime = new Timestamp(utilDate.getTime());
		dateTime.setNanos(0);
		return dateTime;		
	}
	
	public static String getLocalizedDatePattern(String userLocale)
	{
		String l1 = null;
		String l2 = null;
		Locale localeObj = null;
		if(userLocale!=null && userLocale.length()!=0)
		 {	
			l1=userLocale.substring(0,2);
			l2=userLocale.substring(3,5);
			localeObj = new Locale(l1,l2);
		 }	
		else
		 {
			localeObj = new Locale("en","US");                                                     
		 }	
		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT,localeObj);
		SimpleDateFormat sDateFormat = (SimpleDateFormat)dateFormat;
		String datePattern = sDateFormat.toLocalizedPattern();	  
		return datePattern;
	}
	
    // QA20070108.b
	/**
	 * Retrieve the current connection and facility details from the cookie set in the request.
	 * The first element in the array will be the connection string. The second element in the 
	 * array will be the current facility/warehouse for the user.
	 * 
	 * @param request
	 * @return
	 */
	private static String[] getCurrentFacilityDetailsFromRequest(HttpServletRequest request) {
		String[] facilityDetailsArray = {null, null};

        // lets read the cookies and extract the one that we need
        String facilityDetails = null;
        Cookie[] cookies = request.getCookies();
        for (int i=0; i<cookies.length; i++) {
        	String name = cookies[i].getName();
        	if (WavePlanningConstants.COOKIE_WM_FACILITYDETAILS.equalsIgnoreCase(name)) {
        		facilityDetails = cookies[i].getValue();
        		break;
        	}
        }
        
        // facilityDetails if found is expected in the format "connection string~~facility". Separate out.
        StringTokenizer strtok = new StringTokenizer(facilityDetails, WavePlanningConstants.COOKIE_WM_FACILITYDETAILS_DELIM);
        int countOfTokens = strtok.countTokens();
        // we should have got 2, first is connection string and second is facility
        if (countOfTokens == 2) {
        	facilityDetailsArray[0] = strtok.nextToken();
        	facilityDetailsArray[1] = strtok.nextToken();
        }
        
        return facilityDetailsArray;
	}
	// QA20070108.e
	
	
	//Subodh: New Enhancement
	public static String getDefaultOwner(HttpServletRequest request,HttpServletResponse response)
    {
        HttpSession session=request.getSession();
        String defaultOwners = getDefaultOwnerForUserFromRequest(request);        
        return defaultOwners;
    }
	
	
	/**
	 * Retrieve the default Owner from the cookie set in the request for the current user.	 
	 * 
	 * @param request
	 * @return
	 */
	private static String getDefaultOwnerForUserFromRequest(HttpServletRequest request) {
		// lets read the cookies and extract the one that we need
        String defaultOwners = null;        
        Cookie[] cookies = request.getCookies();
        for (int i=0; i<cookies.length; i++) {
        	String name = cookies[i].getName();
        	if (WavePlanningConstants.COOKIE_WM_DEFAULT_OWNERS.equalsIgnoreCase(name)) {
        		defaultOwners = cookies[i].getValue();
        		break;
        	}
        }
        if (defaultOwners != null) {
        	//Owners if found is expected in the format "owner1,owner2,owner3,....".           
        	String comma = ",";
        	String singleQuote = "'";
        	String replaceStr = "','";
        	defaultOwners = defaultOwners.replaceAll(comma, replaceStr);
        	defaultOwners = singleQuote + defaultOwners + singleQuote;
        }
        return defaultOwners;
	}
	
}
