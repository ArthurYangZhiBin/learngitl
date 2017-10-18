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
package com.infor.scm.waveplanning.common.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.beans.BioService;
import com.epiphany.shr.data.beans.BioServiceFactory;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.email.error.EmailException;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.sync.services.common.SimpleEmailSender;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.SsaAccessBase;
import com.infor.scm.waveplanning.common.WavePlanningUtils;


public class WPUtil
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPUtil.class);
	
	public static String DB_CONNECTION = "dbConnectionName";
	public static String DB_USERID =	"dbUserName";
	public static String DB_PASSWORD =	"dbPassword";
	public static String DB_DATABASE = "dbDatabase";
	public static String DB_ISENTERPRISE = "dbIsEnterprise";
	public static String DB_SERVER = "dbServer";
	public static String DB_TYPE = "dbType";
	
	public WPUtil()
    {
    }
	
	
	public static boolean sendEmail(String subject, String message, List toAddresses) throws EmailException{
		SimpleEmailSender sender = new SimpleEmailSender();	
		return sender.sendSimpleMessage(subject,message,toAddresses);
	}
	public static boolean sendEmail(String message, String address) throws EmailException{
		ArrayList toAddresses = new ArrayList();
		toAddresses.add(address);
		return sendEmail("",message,toAddresses);
	}
	public static boolean sendEmail(String subject, String message, String address) throws EmailException{
		ArrayList toAddresses = new ArrayList();
		toAddresses.add(address);
		return sendEmail(subject,message,toAddresses);
	}
	public static boolean sendEmailToCurrentUser(String subject, String message, StateInterface state) throws EmailException, DataBeanException{
		SimpleEmailSender sender = new SimpleEmailSender();	
		ArrayList toAddresses = new ArrayList();
		toAddresses.add(WPUserUtil.getCurrentUserEmail(state));
		return sender.sendSimpleMessage(subject,message,toAddresses);
	}
//	public static String getRegistryAttributeValue(String attributename) throws GenException
//    {
//        // Get the default instance of Registry Service
//        RegistryService m_registry = RegistryService.getDefaultInstance(new Context());
//        // Get a solution
//        Solution solution = m_registry.getSolution("SSAWMS");
//        // Get a component
//        Component component=solution.getComponent("WavePlanning");
//        // Get a attribute
//        Attribute attribute=component.getAttribute(attributename);
//        // Get a attribute value
//        Object obj=attribute.getValue();
//        // Get an String value
//        String attributeValue=obj.toString();
//        return attributeValue;
//    }
	
	 public  static java.util.Date getUtilDate(String p_dateString,String p_format) throws Exception {
			String formatString = null;
			
				formatString = p_format;
				SimpleDateFormat sdf = new SimpleDateFormat(formatString);
				ParsePosition pos = new ParsePosition(0);
				java.util.Date d = sdf.parse(p_dateString,pos);
				return d;
			
	     }	
	 
	
	 
	 public static String getFacility(HttpServletRequest request)
	    {
	        
	        return getCurrentFacilityDetailsFromRequest(request)[1];
	    }
	 
    public static String getFacilityConnectionString(HttpServletRequest request)
    {
    	/**
    	 * Anand on 03/18/08.. this method is used to get facility connection string as it is required for direct JDBC callas
    	 */
	    {
	        
	        return getCurrentFacilityDetailsFromRequest(request)[0].toUpperCase();
	    }    	
    }
	 
	 private static String[] getCurrentFacilityDetailsFromRequest(HttpServletRequest request) {
			String[] facilityDetailsArray = {null, null};

	        // lets read the cookies and extract the one that we need
	        String facilityDetails = null;
	        Cookie[] cookies = request.getCookies();
	        for (int i=0; i<cookies.length; i++) {
	        	String name = cookies[i].getName();
	        	if ("WM_FACILITYDETAILS".equalsIgnoreCase(name)) {
	        		facilityDetails = cookies[i].getValue();
	        		break;
	        	}
	        }
	        
	        // facilityDetails if found is expected in the format "connection string~~facility". Separate out.
	        StringTokenizer strtok = new StringTokenizer(facilityDetails, "~~");
	        int countOfTokens = strtok.countTokens();
	        // we should have got 2, first is connection string and second is facility
	        if (countOfTokens == 2) {
	        	facilityDetailsArray[0] = strtok.nextToken();
	        	facilityDetailsArray[1] = strtok.nextToken();
	        }
	        
	        return facilityDetailsArray;
		}

	 public static BioCollection getCodeLkupCollection(String listName, StateInterface state) throws EpiDataException{
		 EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
//		 String locale = WPUserUtil.getBaseLocale(userContext);
//		 _log.debug("LOG_DEBUG_EXTENSION_COMPDOMCODELKUP","locale:"+locale,100L);									
//		 String qry = "wm_codesdetail.LISTNAME = '"+listName+"' AND wm_codesdetail.LOCALE = '"+locale+"'";
		 String qry = "wm_codesdetail.LISTNAME = '"+listName+"'";
		 Query loadBiosQryA = new Query("wm_codesdetail", qry, "");			
		 _log.debug("LOG_DEBUG_EXTENSION_COMPDOMCODELKUP","CODELKUP QRY:"+qry,100L);				
		 UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		 BioCollectionBean codeList = uow.getBioCollectionBean(loadBiosQryA);		
//		 if(codeList == null || codeList.size() == 0){
//			 locale = state.getLocale().getJavaLocale().toString();
//			 qry = "wm_codesdetail.LISTNAME = '"+listName+"' AND wm_codesdetail.LOCALE = '"+locale+"'";				
//			 loadBiosQryA = new Query("wm_codesdetail", qry, "");	
//			 _log.debug("LOG_DEBUG_EXTENSION_COMPDOMCODELKUP","CODELKUP QRY:"+qry,100L);	
//			 codeList = uow.getBioCollectionBean(loadBiosQryA);
//			 if(codeList == null || codeList.size() == 0){
//				 locale = "en";
//				 qry = "wm_codesdetail.LISTNAME = '"+listName+"' AND wm_codesdetail.LOCALE = '"+locale+"'";					
//				 loadBiosQryA = new Query("wm_codesdetail", qry, "");	
//				 _log.debug("LOG_DEBUG_EXTENSION_COMPDOMCODELKUP","CODELKUP QRY:"+qry,100L);	
//				 codeList = uow.getBioCollectionBean(loadBiosQryA);
//			 }			
//		 }
		 return codeList;
	 }
	 
//	 public static String getValidLocaleForList(String listName, StateInterface state) throws EpiDataException{
//		 EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
//		 String locale = WPUserUtil.getBaseLocale(userContext);
//		 _log.debug("LOG_DEBUG_EXTENSION_COMPDOMCODELKUP","locale:"+locale,100L);									
//		 String qry = "wm_codesdetail.LISTNAME = '"+listName+"' AND wm_codesdetail.LOCALE = '"+locale+"'";
//		 Query loadBiosQryA = new Query("wm_codesdetail", qry, "");			
//		 _log.debug("LOG_DEBUG_EXTENSION_COMPDOMCODELKUP","CODELKUP QRY:"+qry,100L);				
//		 UnitOfWorkBean uow = state.getDefaultUnitOfWork();
//		 BioCollectionBean codeList = uow.getBioCollectionBean(loadBiosQryA);		
//		 if(codeList == null || codeList.size() == 0){
//			 locale = state.getLocale().getJavaLocale().toString();
//			 qry = "wm_codesdetail.LISTNAME = '"+listName+"' AND wm_codesdetail.LOCALE = '"+locale+"'";				
//			 loadBiosQryA = new Query("wm_codesdetail", qry, "");	
//			 _log.debug("LOG_DEBUG_EXTENSION_COMPDOMCODELKUP","CODELKUP QRY:"+qry,100L);	
//			 codeList = uow.getBioCollectionBean(loadBiosQryA);
//			 if(codeList == null || codeList.size() == 0){
//				 locale = "en";				 
//			 }			
//		 }
//		 return locale;
//	 }
	
	 public static String getDBDependentDate(java.util.Date dateToBeConverted) 
		{
			
			if(dateToBeConverted!=null)
			{	
				SimpleDateFormat    sdf         		= null;
				String              output      		= null;				
				sdf = new SimpleDateFormat(WavePlanningUtils.dbDateFormat);
				output = sdf.format(dateToBeConverted);
				return(output);
			}
			else
			{
				return null;
			}
		}	
	 public static String getOAHome() 
	 {

		 return SsaAccessBase.getConfig("wavePlanning","waveplanningConfig").getValue("OAHome");

	 }
	 
	 public static String getWaveConfigValue(String key) 
	 {

		 return SsaAccessBase.getConfig("wavePlanning","waveplanningConfig").getValue(key);

	 }
	 
	 public static UnitOfWork getUnitOfWork() throws EpiException{		
		 BioServiceFactory serviceFactory;
		 BioService bioService;		
		 serviceFactory = BioServiceFactory.getInstance();
		 bioService = serviceFactory.create("webui");
		 return bioService.getUnitOfWork();	 				
	 }
	 
	 public static String getSchemaNameOfWarehouseConnection(StateInterface state)	{
		 HttpSession session = state.getRequest().getSession();
		 String schemaName = (String) session.getAttribute(DB_USERID);
		 if(schemaName == null)
		 {
			 return "";
		 }
		 return schemaName;
	 }
}