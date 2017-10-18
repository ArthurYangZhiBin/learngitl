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
package com.epiphany.shr.util.db;

import com.epiphany.shr.util.config.ConfigService;
import com.epiphany.shr.util.naming.NamingService;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.data.server.*;
import com.epiphany.shr.metadata.objects.generated.np.CodePage;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ddtek.jdbc.extensions.ExtEmbeddedConnection; // For DataDirect Driver unlocking

import javax.sql.DataSource;
import java.util.Properties;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * A factory for making JDBC object wrappers.  This may using object pooling internally.
 */
public class DBFactory
{
    public final static String SERVER_TYPE = "serverType";

    public final static String DATABASE_USER = "databaseUser";
	
	// CPZ: Moved to ConfigService to break compile time dependency of ConfigService to DBFactory
	public final static String DATABASE_PASSWORD = ConfigService.DATABASE_PASSWORD;

    //Sql Db Specific
    public final static String SERVER_NAME = "serverName";
    public final static String SERVER_PORT = "serverPort";
    public final static String DATABASE_NAME = "databaseName";
    public final static String SCHEMA_NAME = "schemaName";
    public final static String DATABASE_PATH = "databasePath";
    //Non Studio properties
    public final static String CODE_PAGE = "codePage";
    public final static String XA = "XA";
    //Oracle Specific
    public final static String DATABASE_SID = "sid";

    //JDBC Specific
    public final static String DATABASE_URL = "URL";
    public final static String DRIVER_CLASSNAME = "driverClassName";
    public final static String SET_SCHEMA_CMD = "setSchemaCmd";
    public final static String TEST_TABLE_NAME = "testTableName";

    public final static String DATABASE_ADMIN_USER = "adminUser";
	
	// CPZ: Moved to ConfigService to break compile time dependency of ConfigService to DBFactory
	public final static String DATABASE_ADMIN_PASSWORD = ConfigService.DATABASE_ADMIN_PASSWORD;

    public final static String DS_NAME = "dataSourceName";
    public final static String IS_CLUSTER_DS_NAME = "isClusterDSName";
    public final static String TABLESPACE_NAME = "tablespace";
    public final static String TEMP_TABLESPACE_NAME = "tempTablespace";

    public static final String ORACLE_DEFAULT_SID = "ORCL";
    public static final String ORACLE_DEFAULT_PORT = "1521";
    public static final String MSSQL_DEFAULT_PORT = "1433";
    
    public static final String ORACLE_THIN_TYPE = "oracle_thin";
    public static final String ORACLE_OCI_TYPE = "oracle_oci";

    public static final String DB2_NET_TYPE = "db2_net";
    public static final String DB2_APP_TYPE = "db2_app";
    public static final String DB2_CODESET = "codeset";
    public static final String DB2_TERRITORY = "territory";

    public static final String DB2_SCRIPT_SHELL_CMD = "db2.script.shellCmd";
    public static final String DB2_SCRIPT_CREATE_DB = "db2.script.createDB2DB";
    public static final String DB2_SCRIPT_OUTPUT_REDIRECT = "db2.script.outputRedirect";
    public static final String DB2_SCRIPT_ATTACH_DB = "db2.script.attachDB2";

    public static final String MSSQL_WEBLOGIC_TYPE = "mssql_weblogic";
    public static final String SQL92_JDBC_TYPE = "JDBC";
    public static final String MSSQL_MERANT_TYPE ="mssql_merant";
    public static final String MSSQL_DATADIRECT_TYPE = "mssql_datadirect";
    public static final String MQSQL_COLLATE =  "collate";
    public static final String MSSQL_JTDS_TYPE ="mssql_jtds";
    
    // informix modifications
    public static final String INFORMIX_COLLATE =  "collate";
    public static final String INFORMIX_SQLI = "informix_sqli";
    public static final String INFORMIX_DYNAMIC_SERVER_NAME = "dynamicServerName";
    public static final String INFORMIX_DBSPACE = "dbSpace";
    
    public static final String DATADIRECT_EPIPHANY_PASSWORD = "epiphanyjdbc";

    public static boolean _serverAvailable = false;
    public static boolean _serverStatusChecked = false;

    public static DriverType getConnectionType(String type) {

        if (type == null){
            return null;
        }else if (type.equals(ORACLE_THIN_TYPE)) {
            return DriverType.ORACLE_THIN;
        } else if (type.equals(ORACLE_OCI_TYPE)) {
            return DriverType.ORACLE_OCI;
        } else if (type.equals(DB2_NET_TYPE)) {
            return DriverType.DB2_NET;
        } else if (type.equals(DB2_APP_TYPE)) {
            return DriverType.DB2_APP;
        } else if (type.equals(MSSQL_MERANT_TYPE)) {
            return DriverType.MSSQL_MERANT;
        } else if (type.equals(MSSQL_WEBLOGIC_TYPE)) {
            return DriverType.MSSQL_WEBLOGIC;
        } else if (type.equals(MSSQL_DATADIRECT_TYPE)) {
            return DriverType.MSSQL_DATADIRECT;
        }else if(type.equals(MSSQL_JTDS_TYPE)){
        	return DriverType.MSSQL_JTDS;
        }else if (type.equals(SQL92_JDBC_TYPE)) {
            return DriverType.SQL92_JDBC;
        }
        else if(type.equals(INFORMIX_SQLI))
        	return DriverType.INFORMIX_DRIVER;
        return null;
    }

    /**
     * An enumeration of supported JDBC driver types.
     */
    public static class DriverType
    {
        public static final DriverType ORACLE_THIN = new DriverType(ORACLE_THIN_TYPE);
        public static final DriverType ORACLE_OCI = new DriverType(ORACLE_OCI_TYPE);
        public static final DriverType DB2_NET = new DriverType(DB2_NET_TYPE);
        public static final DriverType DB2_APP = new DriverType(DB2_APP_TYPE);
        public static final DriverType MSSQL_WEBLOGIC = new DriverType(MSSQL_WEBLOGIC_TYPE);
        public static final DriverType MSSQL_MERANT = new DriverType(MSSQL_MERANT_TYPE);
        public static final DriverType MSSQL_DATADIRECT = new DriverType(MSSQL_DATADIRECT_TYPE);
        public static final DriverType MSSQL_JTDS = new DriverType(MSSQL_JTDS_TYPE);
        public static final DriverType SQL92_JDBC = new DriverType(SQL92_JDBC_TYPE);
        public static final DriverType INFORMIX_DRIVER = new DriverType(INFORMIX_SQLI);


        private String typeName = null;

        private DriverType(String typeName) {
            this.typeName = typeName;
        }

        public String getType() {
            return typeName;
        }
    }

    // Provider a driver Type and it will give you a uniform Db Name
    public static String getDbNameFromDriverType(String type) {

        if (type == null){
            return DBDatabaseMetaData.UNKNOWN_PRODUCT_NAME;
        }else if (type.equals(ORACLE_THIN_TYPE) || type.equals(ORACLE_OCI_TYPE)) {
            return DBDatabaseMetaData.ORACLE_PRODUCT_NAME;
        } else if (type.equals(DB2_NET_TYPE) || type.equals(DB2_APP_TYPE)) {
            return DBDatabaseMetaData.DB2_PRODUCT_NAME;
        } else if (type.equals(MSSQL_MERANT_TYPE) || type.equals(MSSQL_WEBLOGIC_TYPE) || type.equals(MSSQL_DATADIRECT_TYPE) || type.equals(MSSQL_JTDS_TYPE)) {
            return DBDatabaseMetaData.MSSQL_PRODUCT_NAME;
        } else if (type.equals(SQL92_JDBC_TYPE)) {
            return DBDatabaseMetaData.UNKNOWN_PRODUCT_NAME;
        }else if (type.equals(INFORMIX_SQLI)) {
            return DBDatabaseMetaData.INFORMIX_PRODUCT_NAME;
        }
        return DBDatabaseMetaData.UNKNOWN_PRODUCT_NAME;
    }


    protected static ILoggerCategory _log = LoggerFactory.getInstance(DBFactory.class);

    protected static final DBFactory _instance = new DBFactory();

	private static final String RAC_CONNECT_URL = "rac-connect-url";
	private static final String RAC_DRIVER_CLASS = "rac-driver-class";

    /**
     * Use getInstance to get an instance of this class
     */
    protected DBFactory()
    {}

    /**
     * @return an instace of this DBFactory
     */
    public static DBFactory getInstance()
    {
        return _instance;
    }

    /**
     * Get a JDBC connection from a javax.sql.DataSource given the DataSource
     * name.
     * @param dataSourceName the JNDI name of the DataSource.
     * @return the Connection
     */
    public java.sql.Connection getConnectionFromDataSource(String dataSourceName)
        throws java.sql.SQLException, EpiException {
        javax.sql.DataSource ds = null;
        String serverProcess = System.getProperty("epny.serverprocess");
        if (serverProcess != null) {
            ds = (javax.sql.DataSource) NamingService.lookupDataSource(dataSourceName);
        }

        _log.debug("LOG_DBFACTORYGETCONNECTIONFROMDATASOURCE", "Trying to get a connection from the data source {0}", SuggestedCategory.DB_CONNECTION, dataSourceName);
        if(ds == null) {
            _log.error("EXP_DATASOURCE_IS_NULL", "Unable to get a datasource named {0}", SuggestedCategory.DB_CONNECTION, new Object[]{dataSourceName});
            if(ConfigService.getInstance().isMobileServer()) {
                System.exit(1);
            } else {
                throw new SQLException("Datasource is null");
            }
        }

        return getConnection(ds);
    }

    /**
     * This provides a named java DataSource in either the app server or when running outside
     * the app server.  Instead of getting a connection, get a DataSource that can provide connections.
     * The goal of this method is to serve to provide pooled DataSources both in and out
     * of the app server, thus making connection usage and behavior transparent.<BR>
     *
     * Another goal of this method is to serve as truely the only method that an application
     * would need, namely to provide access to named databases.  This matches the access pattern
     * in J2EE, namely looking up DataSources by name (in JNDI typically)<BR>
     *
     * The 2 main places connections are configured are in the database (table data_source) and in
     * the main config file.  This is to get around the bootstrapping problem
     * @param dataSourceName the name of the datasource as in data_source table.
     *   It can be prefixed with java:/ to lookup in JNDI
     * @return a javax.sql.DataSource object from which one gets connections
     * @throws SQLException
     * @throws EpiException
     */
    public DataSource getDataSource(String dataSourceName) throws SQLException, EpiException {
        //  TODO:  now, lets use this guy!
        //  Initial implementation should:  get one single connection and 
        return null;
    }

    /**
     * 
     */
    public java.sql.Connection getConnectionForApplication(String applicationName) //, String applicationVersion)
        throws java.sql.SQLException ,EpiException
    {
        //String applicationCategory = applicationName + "_" + applicationVersion;
        Application app = ApplicationManager.getApplication(applicationName);
        if (app == null) throw new IllegalArgumentException(applicationName+" was not found by the application manager.  Cannot open a database connection.");
        Properties dbProps = app.getDBProps();

        String dsName = (String) dbProps.get(DS_NAME);
        if (dsName == null) {
            dsName = applicationName;
        }

        javax.sql.DataSource ds = null;

        /* DataSource is being used only for jboss */
        if("jboss".equalsIgnoreCase(System.getProperty("epny.appsvr"))) {
            String serverProcess = System.getProperty("epny.serverprocess");
            if(serverProcess != null) {
                ds = (javax.sql.DataSource) NamingService.lookup(dsName);
            }
        }

        if(ds == null) {
            return getNonDSConnection(dbProps);//, applicationVersion);
        }
        return getConnection(ds);
    }

    /**
     * Appears to be the main call to get a connection.
     * @param type
     * @param prop
     * @return
     * @throws java.sql.SQLException ,EpiException
     */
    public java.sql.Connection getConnection(DriverType type, Properties prop)
            throws java.sql.SQLException, EpiException {
        String serverProcess = System.getProperty("epny.serverprocess");
        String currServer = System.getProperty("epny.appsvr");

        java.sql.Connection conn = null;
        String setSchemaSQL ="";
        if (serverProcess != null && (currServer.startsWith("weblogic") || currServer.startsWith("websphere"))) {
//            if (_log.isEnabledFor(ILoggerCategory.DEBUG)) {
//                _log.debug("LOG_DBFactoryPooledConnection", "Trying to use PooledConnection", SuggestedCategory.DB_CONNECTION);
//           }
            conn = DBPooledConnectionFactory.getInstance().getPooledConnection(type, prop);
        }

        if (serverProcess != null && (currServer.equals("jboss")))
        {
            javax.sql.DataSource ds = null;
            String dataSourceName = prop.getProperty(DS_NAME);
          
            if (dataSourceName != null)
            	ds = (javax.sql.DataSource) NamingService.lookup("java:jdbc/"+ dataSourceName);
            if (ds != null)
            	conn = ds.getConnection();
        }

        if (conn == null) {
//            if (_log.isEnabledFor(ILoggerCategory.DEBUG)) {
//                _log.debug("LOG_DBFactoryNonPooledConnection", "Could not use PooledConnection.  Defaulting to using a non pooled connection", SuggestedCategory.DB_CONNECTION);
//            }
            conn = getNonPooledConnection(type, prop);

        }

        setSchemaSQL = getSetSchemaSql(type, prop);
        if (conn != null && !setSchemaSQL.equals("") )
        {
//            _log.info("LOG_SET_SCHEMA_CMD", "The set schema Command being sent is : {0}", SuggestedCategory.DB_CONNECTION, setSchemaSQL);
            PreparedStatement pstmt = conn.prepareStatement(setSchemaSQL);
            pstmt.execute();
            pstmt.close();
        }
        if (conn != null && type.equals(DriverType.INFORMIX_DRIVER)) // wrapping connection object for informix as certain methods are not supported.(like resultSet.getBytes())
        	conn = newDBConnection(conn);
        return conn;
    }

    /**
     * Return the SQL used for a specific database to change the schema.
     * @param type
     * @param dbProps
     * @return
     */
    private String getSetSchemaSql(DriverType type, Properties dbProps) {
        if (type == null)
            type = (DriverType) dbProps.get(DBFactory.SERVER_TYPE);

        if (type.equals(DriverType.SQL92_JDBC )) {
            return dbProps.getProperty(DBFactory.SET_SCHEMA_CMD,"");
        }

        String schemaDBName = dbProps.getProperty(DBFactory.SCHEMA_NAME,"" );
        String dbUser = dbProps.getProperty(DBFactory.DATABASE_USER,"" );
        // If there is a schema name and if it happens to be different from the userName only then run alter session
        if (!schemaDBName.equals("") && !schemaDBName.equalsIgnoreCase(dbUser))
            if(type.equals(DriverType.ORACLE_THIN) || type.equals(DriverType.ORACLE_OCI))
                return "ALTER SESSION SET CURRENT_SCHEMA =  " + schemaDBName ;
            else if (type.equals(DriverType.DB2_NET) || type.equals(DriverType.DB2_APP))
                return "SET SCHEMA " + schemaDBName ;

        // No Need for Use Database on sql server as it is redundant. If needed do: return "USE " + schemaDBName ;
        return "";
    }

    private java.sql.Connection getConnection(javax.sql.DataSource ds) throws java.sql.SQLException, EpiException {
        java.sql.Connection conn = null;

        if(ds != null) {
            conn = ds.getConnection();
//            if (conn instanceof org.jboss.resource.adapter.jdbc.local.LocalConnection) {
//                org.jboss.resource.adapter.jdbc.local.LocalConnection lcon = (org.jboss.resource.adapter.jdbc.local.LocalConnection)conn;
//                Connection ucon = lcon.getUnderlyingConnection();
//                if (ucon instanceof ExtEmbeddedConnection) {
//                     ExtEmbeddedConnection embeddedCon = (ExtEmbeddedConnection)ucon;
//                     boolean unlocked = embeddedCon.unlock(DATADIRECT_EPIPHANY_PASSWORD);
//                     if(unlocked == false) {
//                         _log.error("DATADIRECT_JDBC_DRIVER_NOT_UNLOCKED",
//                                 "The DataDirect JDBC driver was not unlocked",
//                                 SuggestedCategory.DB_CONNECTION);
//                     }
//                }
//            }
        }
        if(conn == null) {
            _log.error("LOG_DBFACTORY.GETCONNECTION2", "The connection from the datasource is null", SuggestedCategory.DB_CONNECTION);
            throw new SQLException("The connection from the datasource is null");
        }
        return conn;

    }

    private java.sql.Connection getNonDSConnection(Properties dbProps) throws java.sql.SQLException, EpiException{

        DriverType dtype = (DriverType) dbProps.get(DBFactory.SERVER_TYPE);

        return getConnection(dtype, dbProps);
    }

    public java.sql.Connection getNonPooledConnection(DriverType type, Properties dbProps )
        throws java.sql.SQLException, EpiException {

    	/*
    	 * Patch for Oracle RAC and any other odd-ball driver URLs we may need to support
    	 */
    	String url = (String) System.getProperty(DBFactory.RAC_CONNECT_URL);
    	String driverClass = (String) System.getProperty(DBFactory.RAC_DRIVER_CLASS);
        if ((url != null) && (driverClass != null)) {
        	try {
				Class.forName(driverClass);
			} catch (ClassNotFoundException e) {
				throw new EpiException("DRIVEREXCEPT", "Exception registering driver class", e);
			}
        	String user = (String) dbProps.getProperty(DBFactory.DATABASE_USER,"");
            String password = (String) dbProps.getProperty(DBFactory.DATABASE_PASSWORD,"");
            Connection conn = java.sql.DriverManager.getConnection(url, user, password);
            conn = newDBConnection(conn);
            return conn;
        }
        // end odd-ball connect URL patch

        if (type == null) {
            type = (DriverType) dbProps.get(DBFactory.SERVER_TYPE);
        }

        if (type.equals(DriverType.SQL92_JDBC )) {
            return getConnectionJDBC(type, dbProps);
        } else if(type.equals(DriverType.ORACLE_THIN) || type.equals(DriverType.ORACLE_OCI)) {
            // NOTE: sqlnet -> servername
            return getConnectionOracle(type, dbProps);
        } else if (type.equals(DriverType.DB2_NET) || type.equals(DriverType.DB2_APP)) {
            return getConnectionDb2(type, dbProps);
        } else if (type.equals(DriverType.MSSQL_MERANT)) {
            return getConnectionMerant(type, dbProps);
        } else if (type.equals(DriverType.MSSQL_DATADIRECT)) {
            return getConnectionDataDirect(type, dbProps);
        }
        else if (type.equals(DriverType.MSSQL_JTDS)) {
            return getConnectionJtds(type, dbProps);
        }
        else if (type.equals(DriverType.INFORMIX_DRIVER))
        	return getConnectionInformix(type, dbProps);
        else {
            return getConnectionMssql(type, dbProps);
        }
    }

    public static String getCodePageNameToUse(DriverType type, CodePage codePage)
    {
        String codePageNameToUse ="";
        String codePageName = "";
        if (codePage != null)
        {
            codePageName = codePage.getCodePageName();
            if (type.equals(DriverType.ORACLE_THIN) || type.equals(DriverType.ORACLE_OCI))
                {
                codePageNameToUse = codePage.getOracleName();
                // temporary - Can remove the below - RK
                if (codePageNameToUse.equals(""))
                    codePageNameToUse ="UTF8";
                }
            else if (type.equals(DriverType.DB2_NET) || type.equals(DriverType.DB2_APP))
                codePageNameToUse = codePage.getDb2Name();
            else
                codePageNameToUse = codePage.getSqlserverName();
            _log.debug("LOG_DBFACTORYGETCODEPAGENAMETOUSE", "Using Code page name: {0} the code page string: {1}", SuggestedCategory.DB_CONNECTION, new String[] {codePageName, codePageNameToUse });
        }
        else
            _log.debug("LOG_DBFACTORYGETCODEPAGENAMETOUSE_NO", "No Code page specified.", SuggestedCategory.DB_CONNECTION);

        if (codePageNameToUse == null)
            codePageNameToUse="";

        return codePageNameToUse;
    }
    /**
     * Get a JDBC connection from the java.sql.DriverManager.  The prefered
     * method is to use getConnection("dataSourceJNDIName").
     * @param jdbcDriverType the type of JDBC driver to use (can be null).
     * @return the Connection
     */
    private java.sql.Connection getConnectionMssql(
                    DBFactory.DriverType jdbcDriverType,
                    Properties dbProps )
        throws java.sql.SQLException ,EpiException
    {
        java.sql.Connection conn = null;
        String serverName ="";
        String dbName ="";
        String user ="";
        try  {

            serverName = (String) dbProps.get(DBFactory.SERVER_NAME);
            String serverPort = null;
            if ((dbProps.get(DBFactory.SERVER_PORT) != null))
                   serverPort =  (dbProps.get(DBFactory.SERVER_PORT)).toString();
            dbName = (String) dbProps.get(DBFactory.DATABASE_NAME);
            user = (String) dbProps.getProperty(DBFactory.DATABASE_USER, "");
            String password = (String) dbProps.getProperty(DBFactory.DATABASE_PASSWORD, "");
            String codePageName = dbProps.getProperty(DBFactory.CODE_PAGE ,"");


            String driverClassName = "weblogic.jdbc.mssqlserver4.Driver";
            String url = "jdbc:weblogic:mssqlserver4:[" + dbName + "]@" + stripSQLServerNamedInstance(serverName) + ":" + ((serverPort == null) ? MSSQL_DEFAULT_PORT : serverPort.toString());
            Properties props = new Properties();
            props.put("user", user);
            props.put("password", password);
            if("bluestone".equalsIgnoreCase(System.getProperty("epny.appsvr")) ||
               "websphere".equalsIgnoreCase(System.getProperty("epny.appsvr")) ||
               "jboss".equalsIgnoreCase(System.getProperty("epny.appsvr"))) {
                props.put("ocifileprop", "jdbcKona/MSSQLServer4-Epiphany");
                props.put("ocilogprop", "epiph.txt");
            }

                Class.forName(driverClassName);
            _log.debug("LOG_DBFACTORY_MSSQL_CON", "Using JDBC Driver: {0}: URL: {1}", SuggestedCategory.DB_CONNECTION, new String[] {driverClassName, url});
            if (! codePageName.equals(""))
            {
                props.put("weblogic.codeset", codePageName);
                _log.debug("LOG_DBFACTORY.GETCONNECTIONMSSQL", "Added code page name to connection property: {0}", SuggestedCategory.DB_CONNECTION, new String[]  {codePageName});
            }
            conn = java.sql.DriverManager.getConnection(url, props);
            conn = newDBConnection(conn);

            } catch (SQLException ex) {
                throw ex;
            } catch (Exception ex) {
                //_log.error("LOG_ERROR_STACK_TRACE_getConnectionMssql", "Error stack Trace.", SuggestedCategory.DB_CONNECTION, ex );
                throw new EpiException("EXP_GETCONNECTIONMSSQL",
                    "There was an Exception in DbFactory.getConnectionMssql() for serverName {0}, DbName{1}, user {2} ",
                    ex, new Object[] {serverName,dbName,user});
            }

        return conn;
    }

    /**
     * Get a JDBC connection from the java.sql.DriverManager.  The prefered
     * method is to use getConnection("dataSourceJNDIName").
     * @param jdbcDriverType the type of JDBC driver to use.
     * @return the Connection
     */
    private java.sql.Connection getConnectionDb2(
                    DBFactory.DriverType jdbcDriverType,
                    Properties dbProps )
        throws java.sql.SQLException, EpiException
    {
        java.sql.Connection conn = null;
        String serverName ="";
        String dbName ="";
        String user ="";
        try  {
            serverName = (String) dbProps.get(DBFactory.SERVER_NAME);
            String serverPort = null;
            if ((dbProps.get(DBFactory.SERVER_PORT) != null))
                   serverPort =  (dbProps.get(DBFactory.SERVER_PORT)).toString();
            dbName = (String) dbProps.get(DBFactory.DATABASE_NAME);
            user = (String) dbProps.get(DBFactory.DATABASE_USER);
            String password = (String) dbProps.get(DBFactory.DATABASE_PASSWORD);

            String driverClassName = null;
            String url = null;

            if(jdbcDriverType == DBFactory.DriverType.DB2_NET)
            {
                driverClassName = "COM.ibm.db2.jdbc.net.DB2Driver";
                url = "jdbc:db2://" + serverName + ":" + ((serverPort == null) ? "0" : serverPort.toString()) + "/" + dbName;
                Class.forName(driverClassName);
            }
            else // DBFactory.DriverType.DB2_APP
            {
                driverClassName = "COM.ibm.db2.jdbc.app.DB2Driver";
                url = "jdbc:db2:" + dbName;
                Class.forName(driverClassName);
            }
            _log.debug("LOG_DBFACTORYGETCONNECTIONDB2", "Using JDBC Driver: {0}: URL: {1}", SuggestedCategory.DB_CONNECTION, new String[] {driverClassName, url});
            conn = java.sql.DriverManager.getConnection(url, user, password);
            conn = newDBConnection(conn);
        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            //_log.error("LOG_ERROR_STACK_TRACE_getConnectionDB2", "Error stack Trace.", SuggestedCategory.DB_CONNECTION, ex );
             throw new EpiException("EXP_GETCONNECTIONDB2",
                    "There was an Exception in DbFactory.getConnectionDB2() for serverName {0}, DbName{1}, user {2} ",
                    ex, new Object[] {serverName,dbName,user});
        }
        return conn;
    }

    /**
     * Get a JDBC connection from the java.sql.DriverManager.  The prefered
     * method is to use getConnection("dataSourceJNDIName").
     * @param jdbcDriverType the type of JDBC driver to use.
     * @return the Connection
     */
    private java.sql.Connection getConnectionOracle(
                    DBFactory.DriverType jdbcDriverType,
                    Properties dbProps)
        throws java.sql.SQLException, EpiException
    {

        java.sql.Connection conn = null;
        String user  = null;
        String sid = null;
        String schemaName = null;
        String codePageName = null;
        String url = null;
        String serverPort = null;
        String sqlNetName ="";
        try{
            sqlNetName = (String) dbProps.get(DBFactory.SERVER_NAME); //Also the serverName
            if ((dbProps.get(DBFactory.SERVER_PORT) != null))
                   serverPort =  (dbProps.get(DBFactory.SERVER_PORT)).toString();
            user = (String) dbProps.getProperty(DBFactory.DATABASE_USER,"");
            schemaName = (String) dbProps.get(DBFactory.SCHEMA_NAME);
            // Backward compatibility....
            if (schemaName == null)
            {
                schemaName = (String) dbProps.get(DBFactory.DATABASE_NAME);
                if (schemaName !=null)
                    _log.debug("LOG_SCHEMA_NAME_MISSING", "Schema Name is blank hence using dbName {0}:", SuggestedCategory.DB_CONNECTION, new String[] {schemaName});
            }
            String password = (String) dbProps.getProperty(DBFactory.DATABASE_PASSWORD,"");
            sid = (String) dbProps.get(DBFactory.DATABASE_SID);
            codePageName = (String) dbProps.getProperty(DBFactory.CODE_PAGE ,"");

            url = null;
            String driverClassName = "oracle.jdbc.OracleDriver";

            Properties props = new Properties();
            props.put("user", user);
            props.put("password", password);
            if (! codePageName.equals(""))
            {
                props.put("weblogic.codeset", codePageName);
                _log.debug("LOG_DBFACTORYGETCONNECTIONORACLE", "Added code page name to connection property: {0}:", SuggestedCategory.DB_CONNECTION, new String[] {codePageName});
            }

            if (jdbcDriverType == DBFactory.DriverType.ORACLE_THIN)
            {
                sid = sid != null ? sid : ORACLE_DEFAULT_SID;
                serverPort = serverPort != null ? serverPort : ORACLE_DEFAULT_PORT;
                url = "jdbc:oracle:thin:@" + sqlNetName + ":" + serverPort + ":" + sid;
            }
            else // DBFactory.DriverType.ORACLE_OCI
                url = "jdbc:oracle:oci:@" + sqlNetName;
            _log.debug("LOG_DBFACTORY_JDBC", " Using JDBC Driver: {0}: URL: {1}", SuggestedCategory.DB_CONNECTION, new String[] {driverClassName, url});
            try{
            Class.forName(driverClassName);
            }catch(ClassNotFoundException cnfe){
               _log.error("EXP_CANNOT_FIND_ORACLE_DRIVERS, COPY THE CLASSES12.JAR FROM YOUR ORACLE INSTANCE",
                       "Cannot find the Oracle driver classes , copy the classes12.jar from your oracle instance  to shared\\lib", cnfe);
            }

            conn = java.sql.DriverManager.getConnection(url, props);
            conn = newDBConnection(conn);
        } catch (SQLException ex) {
            _log.debug("LOG_FAILED_GETTING_CONNECTION_IN_ORACLE", "Failed getting Oracle Database " +
                    "connection with user= {0}, sid = {1}, Database server port = {2}, url = {3}, schema name/ databasename = {4} ",SuggestedCategory.DB_CONNECTION,
                    new Object[]{user, sid, serverPort, url, schemaName});
            throw ex;
        } catch (Exception ex) {
            //_log.error("LOG_ERROR_STACK_TRACE_getConnectionOracle", "Error stack Trace.", SuggestedCategory.DB_CONNECTION, ex );
            throw new EpiException("EXP_GETCONNECTIONORACLE",
                    "There was an Exception in DbFactory.getConnectionOracle() for serverName {0}, SchemaName{1}, user {2} ",
                    ex, new Object[] {sqlNetName,schemaName,user});
        }
        return conn;
    }
    /**
     * Get a JDBC connection from the java.sql.DriverManager.  The prefered
     * method is to use getConnection("dataSourceJNDIName").
     * @param jdbcDriverType the type of JDBC driver to use.
     * @return the Connection
     */
    private java.sql.Connection getConnectionInformix(
                    DBFactory.DriverType driverType,
                    Properties dbProps)
        throws java.sql.SQLException, EpiException
    {
    	
    	 java.sql.Connection conn = null;
         String serverName ="";
         String dbName1 ="";
         String dynamicServerName="";
         String user ="";
         try  {
        	 dynamicServerName =(String) dbProps.getProperty(DBFactory.INFORMIX_DYNAMIC_SERVER_NAME);
             serverName = (String) dbProps.get(DBFactory.SERVER_NAME);
             String serverPort = null;
             if ((dbProps.get(DBFactory.SERVER_PORT) != null))
                    serverPort =  (dbProps.get(DBFactory.SERVER_PORT)).toString();
             dbName1 = (String) dbProps.get(DBFactory.DATABASE_NAME);
             user = (String) dbProps.get(DBFactory.DATABASE_USER);
             String password = (String) dbProps.get(DBFactory.DATABASE_PASSWORD);

             String driverClassName = "com.informix.jdbc.IfxDriver";
             String url = null;

             if(driverType == DBFactory.DriverType.INFORMIX_DRIVER)
             {
                url = "jdbc:informix-sqli://" + serverName + ":" + ((serverPort == null) ? "0" : serverPort.toString()) + "/" + dbName1 +":"+"informixserver="+ dynamicServerName;
                Class.forName(driverClassName);
             }
                  _log.debug("LOG_DBFACTORYGETCONNECTIONDB2", "Using JDBC Driver: {0}: URL: {1}", SuggestedCategory.DB_CONNECTION, new String[] {driverClassName, url});
             conn = java.sql.DriverManager.getConnection(url, user, password);
             conn = newDBConnection(conn);
         } catch (SQLException ex) {
             throw ex;
         } catch (Exception ex) {
             throw new EpiException("EXP_GETCONNECTION_INFORMIX",
                     "There was an Exception in DbFactory.getConnectionInformix() for serverName {0}, DbName{1}, user {2} ",
                     ex, new Object[] {serverName,dbName1,user});
         }
        return conn;
    }
    
    /**
     * Get a JDBC connection from the java.sql.DriverManager.  The prefered
     * method is to use getConnection("dataSourceJNDIName").
     * @param jdbcDriverType the type of JDBC driver to use (can be null).
     * @return the Connection
     */
    private java.sql.Connection getConnectionJDBC(
                    DBFactory.DriverType jdbcDriverType,
                    Properties dbProps )
        throws java.sql.SQLException, EpiException
    {
        java.sql.Connection conn = null;
        String user ="";
        String url ="";
        String driverClassName ="";
        try  {
            user = (String) dbProps.get(DBFactory.DATABASE_USER);
            String password = (String) dbProps.get(DBFactory.DATABASE_PASSWORD);
            url = (String) dbProps.get(DBFactory.DATABASE_URL);
            driverClassName = (String) dbProps.get(DBFactory.DRIVER_CLASSNAME);
            _log.debug("LOG_DBFACTORYJDBC", " Using JDBC Driver: {0}: URL: {1}", SuggestedCategory.DB_CONNECTION, new String[] {driverClassName, url});
            Class.forName(driverClassName);
            conn = java.sql.DriverManager.getConnection(url, dbProps);
            conn = newDBConnection(conn);
        } catch (SQLException ex) {
            throw ex;
         } catch (Exception ex) {
            //_log.error("LOG_ERROR_STACK_TRACE_getConnectionJDBC", "Error stack Trace.", SuggestedCategory.DB_CONNECTION, ex );
            throw new EpiException("EXP_GETCONNECTIONJDBC",
                    "There was an Exception in DbFactory.getConnectionJDBC() for url {0}, driverClassName{1}, user {2} ",
                    ex, new Object[] {url,driverClassName,user});
        }
        return conn;
    }

    /**
     * Get a JDBC connection from the java.sql.DriverManager.  The prefered
     * method is to use getConnection("dataSourceJNDIName").
     * @param jdbcDriverType the type of JDBC driver to use (can be null).
     * @return the Connection
     */
    private java.sql.Connection getConnectionMerant(
                    DBFactory.DriverType jdbcDriverType,
                    Properties dbProps )
            throws java.sql.SQLException, EpiException {
        java.sql.Connection conn = null;
        String serverName ="";
        String dbName ="";
        String user ="";
        try {
            serverName = (String) dbProps.get(DBFactory.SERVER_NAME);
            String serverPort = null;
            if ((dbProps.get(DBFactory.SERVER_PORT) != null)) {
                serverPort =  (dbProps.get(DBFactory.SERVER_PORT)).toString();
            }
            dbName = (String) dbProps.get(DBFactory.DATABASE_NAME);
            user = (String) dbProps.getProperty(DBFactory.DATABASE_USER,"");
            String password = (String) dbProps.getProperty(DBFactory.DATABASE_PASSWORD,"");
            String codePageName = (String) dbProps.get(DBFactory.CODE_PAGE );

            String driverClassName = "com.merant.sequelink.jdbc.SequeLinkDriver";
            String url = "jdbc:sequelink://"+stripSQLServerNamedInstance(serverName)+":"+ ((serverPort == null) ? MSSQL_DEFAULT_PORT : serverPort.toString()) +";databaseName=" + dbName;
            Properties props = new Properties();
            props.put("user", user);
            props.put("password", password);

            Class.forName(driverClassName);
            _log.debug("LOG_DBFACTORYMSSQL", "Using JDBC Driver: {0}: URL: {1}", SuggestedCategory.DB_CONNECTION, new String[] {driverClassName, url});
            conn = java.sql.DriverManager.getConnection(url, props);
            conn = newDBConnection(conn);
        } catch  (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
            //_log.error("LOG_ERROR_STACK_TRACE_getConnectionMerant", "Error stack Trace.", SuggestedCategory.DB_CONNECTION, ex );
             throw new EpiException("EXP_GETCONNECTIONMERANT",
                    "There was an Exception in DbFactory.getConnectionMerant() for serverName {0}, DbName{1}, user {2} ",
                    ex, new Object[] {serverName,dbName,user});
        }
        return conn;
    }


    private java.sql.Connection getConnectionDataDirect(
                    DBFactory.DriverType jdbcDriverType,
                    Properties dbProps )
            throws java.sql.SQLException, EpiException {
        java.sql.Connection conn = null;
        String serverName ="";
        String dbName ="";
        String user ="";
        try  {
            serverName = (String) dbProps.get(DBFactory.SERVER_NAME);
            String serverPort = null;
            if ((dbProps.get(DBFactory.SERVER_PORT) != null))
                   serverPort =  (dbProps.get(DBFactory.SERVER_PORT)).toString();
            dbName = (String) dbProps.get(DBFactory.DATABASE_NAME);
            user = (String) dbProps.getProperty(DBFactory.DATABASE_USER,"");
            String password = (String) dbProps.getProperty(DBFactory.DATABASE_PASSWORD,"");
            String codePageName = (String) dbProps.get(DBFactory.CODE_PAGE );

            String driverClassName = "com.epiphany.jdbc.sqlserver.SQLServerDriver";
            String url = "jdbc:epiphany:sqlserver://"+serverName+":"+((serverPort == null) ? MSSQL_DEFAULT_PORT : serverPort.toString()) + ";databaseName=" + dbName;
            Properties props = new Properties();
            props.put("user", user);
            props.put("password", password);
          
            Class.forName(driverClassName);
            _log.debug("LOG_DBFACTORYGETCONNECTIONMSSQL", "Using JDBC Driver: {0}: URL: {1}", SuggestedCategory.DB_CONNECTION, new String[] {driverClassName, url});
            conn = java.sql.DriverManager.getConnection(url, props);
            if (conn instanceof ExtEmbeddedConnection) {
                 ExtEmbeddedConnection embeddedCon = (ExtEmbeddedConnection)conn;
                 boolean unlocked = embeddedCon.unlock(DATADIRECT_EPIPHANY_PASSWORD);
            }
            conn = newDBConnection(conn);
        } catch (SQLException ex) {
            throw ex;
        } catch (Exception ex) {
           // _log.error("LOG_ERROR_STACK_TRACE_getConnectionDataDirect", "Error stack Trace. {0}", SuggestedCategory.DB_CONNECTION, ex );
            throw new EpiException("EXP_GETCONNECTIONDATADIRECT",
                    "There was an Exception in DbFactory.getConnectionDataDirect() for serverName {0}, DbName{1}, user {2} ",
                    ex, new Object[] {serverName,dbName,user});
        }
        return conn;
    }
    private java.sql.Connection getConnectionJtds(
            DBFactory.DriverType jdbcDriverType,
            Properties dbProps )
    throws java.sql.SQLException, EpiException {
java.sql.Connection conn = null;
String serverName ="";
String dbName ="";
String user ="";
try  {
    serverName = (String) dbProps.get(DBFactory.SERVER_NAME);
    String serverPort = null;
    if ((dbProps.get(DBFactory.SERVER_PORT) != null))
           serverPort =  (dbProps.get(DBFactory.SERVER_PORT)).toString();
    dbName = (String) dbProps.get(DBFactory.DATABASE_NAME);
    user = (String) dbProps.getProperty(DBFactory.DATABASE_USER,"");
    String password = (String) dbProps.getProperty(DBFactory.DATABASE_PASSWORD,"");
    String codePageName = (String) dbProps.get(DBFactory.CODE_PAGE );

    String driverClassName = "net.sourceforge.jtds.jdbc.Driver";
    String url = "jdbc:jtds:sqlserver://"+serverName+":"+((serverPort == null) ? MSSQL_DEFAULT_PORT : serverPort.toString()) + ";databaseName=" + dbName;
    Properties props = new Properties();
    props.put("user", user);
    props.put("password", password);

    Class.forName(driverClassName);
    _log.debug("LOG_DBFACTORYGETCONNECTIONMSSQL", "Using JDBC Driver: {0}: URL: {1}", SuggestedCategory.DB_CONNECTION, new String[] {driverClassName, url});
    
    conn = java.sql.DriverManager.getConnection(url, props);
    conn = newDBConnection(conn);
} catch (SQLException ex) {
    throw ex;
} catch (Exception ex) {
   // _log.error("LOG_ERROR_STACK_TRACE_getConnectionDataDirect", "Error stack Trace. {0}", SuggestedCategory.DB_CONNECTION, ex );
    throw new EpiException("EXP_GETCONNECTIONJTDS",
            "There was an Exception in DbFactory.getConnectionDataDirect() for serverName {0}, DbName{1}, user {2} ",
            ex, new Object[] {serverName,dbName,user});
}
return conn;
}
    /**
     * Helper method
     */
    public static String stripSQLServerNamedInstance(String serverName) {
        int index = serverName != null ? serverName.lastIndexOf('\\') : -1;
        return index != -1 ? serverName.substring(0, index) : serverName;
    }


    /**
     * Get a JDBC connection from a javax.sql.DataSource given the ConfigService
     * parameter name of the DataSource.
     * @param dataSourceConfigParamName the ConfigService parameter name of the
     *      DataSource (i.e. "Database::META-DATA-DS")
     * @return the Connection
     */
    /**
    public java.sql.Connection getConnectionUsingConfigParam(String dataSourceConfigParamName)
        throws java.sql.SQLException
    {
        return getConnection(ConfigService.getInstance().safeGetProperty(dataSourceConfigParamName));
    }
    **/


    //-------------------- internal methods used by wrapped objects only ----------------------

    /**
     * Return a DataSource object
     * @param ds the DataSource object to wrap
     * @return the wrapped DataSource object
     */
    public javax.sql.DataSource newDBDataSource(javax.sql.DataSource ds)
    { return new DBDataSource(ds); }

    public java.sql.Connection newDBConnection(java.sql.Connection con)
    { return new DBConnection(con); }

    protected java.sql.DatabaseMetaData newDBDatabaseMetaData(java.sql.DatabaseMetaData meta)
    { return new DBDatabaseMetaData(meta); }

    protected java.sql.Statement newDBStatement(java.sql.Statement stmt, java.sql.Connection con)
    { return new DBStatement(stmt, con); }

    protected java.sql.PreparedStatement newDBPreparedStatement(java.sql.PreparedStatement stmt, java.sql.Connection con)
    { return new DBPreparedStatement(stmt, con); }

    protected java.sql.CallableStatement newDBCallableStatement(java.sql.CallableStatement stmt, java.sql.Connection con)
    { return new DBCallableStatement(stmt, con); }

    protected java.sql.ResultSet newDBResultSet(java.sql.ResultSet rs, java.sql.Statement stmt)
    { return new DBResultSet(rs, stmt); }


}
