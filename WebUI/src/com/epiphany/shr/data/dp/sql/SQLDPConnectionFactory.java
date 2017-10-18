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

package com.epiphany.shr.data.dp.sql;

import com.epiphany.shr.data.dp.connection.DPConnection;
import com.epiphany.shr.data.dp.connection.DPXAConnectionFactory;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.metadata.objects.generated.np.CodePage;
import com.epiphany.shr.metadata.objects.generated.np.DataSource;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.db.DBFactory;
//import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.util.config.ConfigConstants;
import com.epiphany.shr.util.config.ConfigService;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.navigation.*;

import com.ssaglobal.SsaAccessBase;

//import java.util.StringTokenizer;
//import java.util.ResourceBundle;

import java.util.Properties;
import java.util.Map;
import java.util.Iterator;
//import java.io.*;
import com.ssaglobal.scm.wms.datalayer.*;

public class SQLDPConnectionFactory extends SsaAccessBase implements DPXAConnectionFactory
{

  protected DataSource _dataSource;
  protected static ILoggerCategory _log = LoggerFactory.getInstance(SQLDPConnectionFactory.class);
  private static SsaAccessBase appAccess;
  
  public SQLDPConnectionFactory()
  {
	  super();
	  if (appAccess == null)
		  appAccess = this;
  }

  public void init(DataSource dataSource) throws DPException
  {
	 
    _dataSource = dataSource;

    if (_dataSource == null)
        throw new DPException( "EXP_DP_NODATASOURCESPECIFIED",
					"No value for was specified for data source.",null);


  }

  public static SsaAccessBase getAppAccess(){
	  return appAccess;
  }

  private static boolean _useAppsvrDataSourceCFlagSet = ConfigService.getInstance().isComponentFlagSet(ConfigConstants.USE_APPSVR_DATA_SOURCE);

 /**
   * Get a JDBC connection using the properties set in the datasource.
   * @exception java.sql.DPException
   */
  public DPConnection getConnection() throws DPException
  {	 
    return getConnection(false);
  }

  public DPConnection getXAConnection() throws DPException
  {	 
    return getConnection(true);
  }
  public String defaultModuleName() {
	  return "webUI";
  }

  protected DPConnection getConnection(boolean XA) throws DPException
  {	  	 
    String serverType = _dataSource.getPropertyAsString(DBFactory.SERVER_TYPE);    
    if ( serverType == null )
    throw new DPException( "EXP_DP_NOSERVERTYPESPECIFIED",
                    "No value for {0} was specified for data source {1}",
                    new String[] { DBFactory.SERVER_TYPE, _dataSource.getName()});
    if (DBFactory.getConnectionType(serverType) ==null) // If the serverType is not a valid value
        throw new DPException( "EXP_DP_INVALID_SERVERTYPESPECIFIED",
                    "The server type value {0} specified for data source {1} is invalid. ",
                      new String[] { serverType, _dataSource.getName()});
    Map dataSourceProps = _dataSource.getAllProperties();
    Properties prop = new Properties();
    CodePage codePage = _dataSource.getCodePage();
    if (codePage != null)
        prop.put(DBFactory.CODE_PAGE, DBFactory.getCodePageNameToUse(DBFactory.getConnectionType(serverType), codePage));
          
    prop.put(DBFactory.XA, new Boolean(XA));
    for (Iterator i = dataSourceProps.entrySet().iterator(); i.hasNext(); )
    {
        Map.Entry entry = (Map.Entry) i.next();
        Object key = entry.getKey();
        Object value = entry.getValue();
        if (key != null && value != null)
        prop.put(key, value);
    }
    
    String dsName = prop.getProperty(DBFactory.DS_NAME);    
//  This part of the code added to change the Datasource and it's properties at runtime if the Datasourse is for WMS
// HC************************************************
    try{
    	//mark ma added **************************************
 //   	WebuiConfig webConfig = new WebuiPropertyMappingObject().getWebuiConfig();
//		String defaultDataSource = webConfig.getDefaultdatasource();
		//SsaAccessBase appAccess = new SQLDPConnectionFactory();
		String defaultDataSource = appAccess.getValue("webUIConfig","defaultDatasource");
		String wmsDatabaseServerName = appAccess.getValue("webUIConfig","wmsdatabaseservername");
		String defaultDataSource_wp = appAccess.getValue("webUIConfig","defaultDatasource_wp");
//    	ResourceBundle rbundle = ResourceBundle.getBundle("com.epiphany.shr.data.dp.sql.datasource");	
		//_log.debug("LOG_SYSTEM_OUT","\n\nServer Type:"+serverType+"\n\n",100L);
//		_log.debug("LOG_SYSTEM_OUT","\n\nServer Type@@@@:"+serverType+"\n\n",100L);
		if(serverType.equalsIgnoreCase("mssql_datadirect")){			
	        if (dsName.equals(defaultDataSource) || dsName.equals(defaultDataSource_wp)) 
	        {   	        	
	        	PowerLockScrambler password = new PowerLockScrambler();
	        	//defect246.b
	        	EpnyServiceManagerServer esms = EpnyServiceManagerServer.getInstance();
	        	EpnyUserContext userContext = esms.getUserContext();
//	        	EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
	        	if (userContext.isEmpty()){
	        		esms.revertStoredUserContext();
	        		userContext = esms.getUserContext();
	        	}
	        	//defect246.e
	        	if (! userContext.isEmpty()) {
	        		//_log.debug("LOG_SYSTEM_OUT","User Ctx is not empty..",100L);
	        		String db_isenterprise = (userContext.get(SetIntoHttpSessionAction.DB_ISENTERPRISE)).toString();
	            	if(db_isenterprise.equals("1"))
	            	{	            		
	            		dsName = defaultDataSource;
	            		
	            	}
	            	else
	            	{	            		
	            		dsName = (userContext.get(SetIntoHttpSessionAction.DB_CONNECTION)).toString();
	            	}
	            	
	        		String db_name = (userContext.get(SetIntoHttpSessionAction.DB_DATABASE)).toString().toUpperCase();	//new Database name
	        		String db_uid = (userContext.get(SetIntoHttpSessionAction.DB_USERID)).toString();		//new database user
	            	String db_password = password.descramble((userContext.get(SetIntoHttpSessionAction.DB_PASSWORD)).toString());		//new database passowrd	            	
	            	prop.setProperty(DBFactory.DS_NAME,dsName.toUpperCase());
	            	prop.setProperty(DBFactory.DATABASE_NAME,db_name);
	            	prop.setProperty(DBFactory.DATABASE_USER,db_uid);
	            	prop.setProperty(DBFactory.DATABASE_PASSWORD,db_password);	            	
	        	}  	        	
	        }
		}	if(serverType.equalsIgnoreCase("mssql_jtds")){			
	        if (dsName.equals(defaultDataSource) || dsName.equals(defaultDataSource_wp)) 
	        {   	        	
	        	PowerLockScrambler password = new PowerLockScrambler();
	        	//defect246.b
	        	EpnyServiceManagerServer esms = EpnyServiceManagerServer.getInstance();
	        	EpnyUserContext userContext = esms.getUserContext();
//	        	EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
	        	if (userContext.isEmpty()){
	        		esms.revertStoredUserContext();
	        		userContext = esms.getUserContext();
	        	}
	        	//defect246.e
	        	if (! userContext.isEmpty()) {
	        	//	_log.debug("LOG_SYSTEM_OUT","User Ctx is not empty..",100L);
	        		String db_isenterprise = (userContext.get(SetIntoHttpSessionAction.DB_ISENTERPRISE)).toString();
	            	if(db_isenterprise.equals("1"))
	            	{	            		
	            		dsName = defaultDataSource;
	            		
	            	}
	            	else
	            	{	            		
	            		dsName = (userContext.get(SetIntoHttpSessionAction.DB_CONNECTION)).toString();
	            	}
	            	
	        		String db_name = (userContext.get(SetIntoHttpSessionAction.DB_DATABASE)).toString().toUpperCase();	//new Database name
	        		String db_uid = (userContext.get(SetIntoHttpSessionAction.DB_USERID)).toString();		//new database user
	            	String db_password = password.descramble((userContext.get(SetIntoHttpSessionAction.DB_PASSWORD)).toString());		//new database passowrd	            	
	            	prop.setProperty(DBFactory.DS_NAME,dsName.toUpperCase());
	            	prop.setProperty(DBFactory.DATABASE_NAME,db_name);
	            	prop.setProperty(DBFactory.DATABASE_USER,db_uid);
	            	prop.setProperty(DBFactory.DATABASE_PASSWORD,db_password);	            	
	        	}  	        	
	        }
		}
		else if(serverType.equalsIgnoreCase("oracle_thin")){
			//_log.debug("LOG_SYSTEM_OUT","ds is oracle...",100L);
	        if (dsName.equals(defaultDataSource) || dsName.equals(defaultDataSource_wp)) 
	        {   
		        PowerLockScrambler password = new PowerLockScrambler();
	        	//defect246.b
	        	EpnyServiceManagerServer esms = EpnyServiceManagerServer.getInstance();
	        	EpnyUserContext userContext = esms.getUserContext();
//	        	EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
	        	if (userContext.isEmpty()){
	        		esms.revertStoredUserContext();
	        		userContext = esms.getUserContext();
	        	}
	        	//defect246.e
	        	if (! userContext.isEmpty()) {
	        		String db_isenterprise = (userContext.get(SetIntoHttpSessionAction.DB_ISENTERPRISE)).toString();
	            	if(db_isenterprise.equals("1"))
	            	{
	            		dsName = defaultDataSource;
	            		
	            	}
	            	else
	            	{
	            		dsName = (userContext.get(SetIntoHttpSessionAction.DB_CONNECTION)).toString();
	            	}
	        		String db_name = (userContext.get(SetIntoHttpSessionAction.DB_DATABASE)).toString().toUpperCase();	//new Database name
	        		String db_uid = (userContext.get(SetIntoHttpSessionAction.DB_USERID)).toString();		//new database user
	            	String db_password = password.descramble((userContext.get(SetIntoHttpSessionAction.DB_PASSWORD)).toString());		//new database passowrd
	            	prop.setProperty(DBFactory.DS_NAME,dsName.toUpperCase());
	            	prop.setProperty(DBFactory.DATABASE_NAME,db_name);
	            	prop.setProperty(DBFactory.DATABASE_USER,db_uid);
	            	prop.setProperty(DBFactory.DATABASE_PASSWORD,db_password);
	            	
	               	prop.setProperty(DBFactory.SERVER_NAME,wmsDatabaseServerName);
	            	prop.setProperty(DBFactory.DATABASE_SID,db_name);
	            	prop.setProperty(DBFactory.SCHEMA_NAME,db_uid);
	        	}
	        }
			
		}
		else if(serverType.equalsIgnoreCase("db2_app")){
			//_log.debug("LOG_SYSTEM_OUT","ds is db2...",100L);			
	        if (dsName.equals(defaultDataSource) || dsName.equals(defaultDataSource_wp)) 
	        {   
		        PowerLockScrambler password = new PowerLockScrambler();
	        	//defect246.b
	        	EpnyServiceManagerServer esms = EpnyServiceManagerServer.getInstance();
	        	EpnyUserContext userContext = esms.getUserContext();
//	        	EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
	        	if (userContext.isEmpty()){
	        		esms.revertStoredUserContext();
	        		userContext = esms.getUserContext();
	        	}
	        	//defect246.e
	        	if (! userContext.isEmpty()) {
	        		String db_isenterprise = (userContext.get(SetIntoHttpSessionAction.DB_ISENTERPRISE)).toString();
	            	if(db_isenterprise.equals("1"))
	            	{
	            		dsName = defaultDataSource;
	            		
	            	}
	            	else
	            	{
	            		dsName = (userContext.get(SetIntoHttpSessionAction.DB_CONNECTION)).toString();
	            	}
	        		String db_name = (userContext.get(SetIntoHttpSessionAction.DB_DATABASE)).toString().toUpperCase();	//new Database name
	        		String db_uid = (userContext.get(SetIntoHttpSessionAction.DB_USERID)).toString();		//new database user
	            	String db_password = password.descramble((userContext.get(SetIntoHttpSessionAction.DB_PASSWORD)).toString());		//new database passowrd
	            	prop.setProperty(DBFactory.DS_NAME,dsName.toUpperCase());
	            	prop.setProperty(DBFactory.DATABASE_NAME,db_name);
	            	prop.setProperty(DBFactory.DATABASE_USER,db_uid);
	            	prop.setProperty(DBFactory.DATABASE_PASSWORD,db_password);	            	
	        	}
	        }
			
		}
    }catch(Exception e)
    {
    	e.printStackTrace();
    }
// HC************************************************ 
//_log.debug("LOG_SYSTEM_OUT","ds name="+prop.getProperty(DBFactory.DS_NAME),100L);
//_log.debug("LOG_SYSTEM_OUT","database name="+prop.getProperty(DBFactory.DATABASE_NAME),100L);
//_log.debug("LOG_SYSTEM_OUT","USERID="+prop.getProperty(DBFactory.DATABASE_USER),100L);
//_log.debug("LOG_SYSTEM_OUT","dB PASSWORD="+prop.getProperty(DBFactory.DATABASE_PASSWORD),100L);
//_log.debug("LOG_SYSTEM_OUT","serverType="+serverType,100L);

    boolean isServerProcess = "true".equals(System.getProperty("epny.serverprocess"));
    if ((dsName != null) && ConfigService.getInstance().isMobileServer() && isServerProcess)
        return new SQLDPConnection(dsName);
    else
        return new SQLDPConnection(DBFactory.getConnectionType(serverType),  prop);
   }
}
