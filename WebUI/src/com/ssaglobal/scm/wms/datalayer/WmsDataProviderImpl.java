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

package com.ssaglobal.scm.wms.datalayer;

import java.rmi.RemoteException;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import com.agileitp.forte.framework.TextData;
import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.dp.shr.SQLCreator;
import com.epiphany.shr.data.dp.access.DPDataProviderImpl;
import com.epiphany.shr.data.dp.connection.DPConnection;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.dp.access.DPInsertSupport;
import com.epiphany.shr.data.dp.util.*;
import com.epiphany.shr.data.dp.properties.DPProperties;
import com.epiphany.shr.data.dp.sql.SQLDataProviderImpl;
import com.epiphany.shr.metadata.objects.dm.RecordSetType;
import com.epiphany.shr.metadata.objects.dm.DataSource;
import com.epiphany.shr.sf.EpnyServiceManager;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.db.DBFactory ;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.util.sql.* ;
import com.epiphany.shr.util.algorithm.Algorithms;
import com.ssaglobal.SsaException;
import com.ssaglobal.misc.BeanDisplayer;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.service.exeprocessmanager.TransactionServiceSORemote;
import com.ssaglobal.scm.wms.service.exeprocessmanager.TransactionServiceSORemoteHome;
import com.ssaglobal.util.EjbHelper;
import com.ssaglobal.scm.wms.navigation.*;		//HC
import com.ssaglobal.scm.wms.service.exeprocessmanager.TransactionServiceSORemoteHome;
import com.ssaglobal.scm.wms.uiextensions.KeyGenBioWrapper;
/**
 * <b>General Overview:</b>
 * <ul>
 *  <li>Map insert/update/delete to EJB calls</li>
 *  <li>Intercept fetch/query (or more likely getConnection()) methods to toggle by Warehouse in UserContext</li>
 *  <li>Implement some scheme for aggregation in queries.  One possibility: model it in Field definitions in
 *      Recordset definitions: properties to describe relationship to use, etc.  Then aggregation wouldn't be
 *      implied in Bio Queries, rather modelled in Recordset definition (just another Field value, mapped
 *      to attribute.</li>
 * </ul>
 * <b>TODO</b>
 * <ul>
 *  <li>Wire up actual EJB calls</li>
 *  <li>Introduce some way to set Warehouse in UI</li>
 * </ul>
 * <b>Platform TODO</b>
 * <ul>
 *  <li>Introduce new XXXStatement.toSQLWithParamString()-like methods that return fully executable SQL:
 *     fully quote-escaped, with all (including long) values, etc.</li>
 * </ul>
 */
public class WmsDataProviderImpl extends SQLDataProviderImpl
{

	public final static String WMS_DATA_PROVIDER_NAME = "WmsDataProviderImpl";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WmsDataProviderImpl.class);
	protected static final String internalCaller = "internalCall";
	public WmsDataProviderImpl()
	{
		super(WMS_DATA_PROVIDER_NAME);
		_log.info("EXP_1", "[WmsDataProviderImpl]::instatiated", SuggestedCategory.NONE);
	}

	public DPRecordKey insert(DPConnection conn, DPRecord record, DPInsertSpecification insertSpec) throws DPException
	{
		_log.info("EXP_1", "[WmsDataProviderImpl]::insert enteredA", SuggestedCategory.NONE);
		InsertStatement insertStmt = null;
//HC.b
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
//		String locale = userContext.getLocale();
		String locale = getBaseLocale(userContext);

		_log.debug("LOG_SYSTEM_OUT","\n\n:Locale " + locale,100L);
		_log.info("LOG_DEBUG_EXTENSION_WmsDataProviderImpl", "Locale " + locale, SuggestedCategory.NONE);
		String db_connection = (userContext.get(SetIntoHttpSessionAction.DB_CONNECTION)).toString();
		//String wmWhseID = (userContext.get(SetIntoHttpSessionAction.DB_USERID)).toString();
		String wmWhseID = (userContext.get("logInUserId")).toString();
//		StringTokenizer dbstring = new StringTokenizer(db_connection,"_");
//	     while (dbstring.hasMoreTokens()) {
//	    	 wmWhseID = dbstring.nextToken();
//	     }
//HC.e	
		try
		{
			SQLCreator sqllocal =  new SQLCreator(DBFactory.getDbNameFromDriverType(
					record.getRecordSetType().getDataSource().getPropertyAsString(DBFactory.SERVER_TYPE)));
			insertStmt = sqllocal.createInsert(record, insertSpec);
			String sql = insertStmt.toSQLWithParamString();
			infoDebugPrintOnNewLine1(" The SQL created is: ", sql);
			_log.debug("LOG_SYSTEM_OUT","Insert SQL = = " + sql,100L);
			TransactionServiceSORemote remote = EJBRemote.getRemote();
			_log.debug("LOG_SYSTEM_OUT","*********Got the Remote back = " + remote,100L);
			EXEDataObject obj = remote.insert(new TextData(wmWhseID),db_connection, new TextData(sql), true, null,internalCaller,locale);	//HC
			DPRecordKey recKey = record.getPrimaryKey();
			return recKey;
		}
		catch (Exception ex)
		{
			errorLogException(ex);
			throw new DPException(ex.getMessage(), ex);
		}
		finally
		{
			if (insertStmt != null)
				try { insertStmt.close(); } catch (SQLException ex) {} // Should not be thrown if no connection established
		}
	}

	public EXEDataObject executeUpdateSql(String sql) throws RemoteException, ServiceObjectException
	{
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
//		String locale = userContext.getLocale();
		String locale = getBaseLocale(userContext);
		_log.debug("LOG_SYSTEM_OUT","\n\n:Locale " + locale,100L);
		_log.info("LOG_DEBUG_EXTENSION_WmsDataProviderImpl", "Locale " + locale, SuggestedCategory.NONE);
		String db_connection = userContext.get(SetIntoHttpSessionAction.DB_CONNECTION).toString();
		//String wmWhseID = (userContext.get(SetIntoHttpSessionAction.DB_USERID)).toString();
		String wmWhseID = (userContext.get("logInUserId")).toString();
//		StringTokenizer dbstring = new StringTokenizer(db_connection,"_");
//	     while (dbstring.hasMoreTokens()) {
//	    	 wmWhseID = dbstring.nextToken();
//	     }
		try {
			TransactionServiceSORemote remote = EJBRemote.getRemote();
			return remote.update(new TextData(wmWhseID),db_connection, new TextData(sql),null, true,internalCaller,locale);
		}  catch (SsaException e) {
			errorLogException(e);
		} 
		return null;
	}
	
	public int update(DPConnection conn, DPRecord record, DPUpdateSpecification updateSpec) throws DPException
	{

		UpdateStatement updateStmt =null;
//HC.b
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
//		String locale = userContext.getLocale();
		String locale = getBaseLocale(userContext);

		_log.debug("LOG_SYSTEM_OUT","\n\n:Locale " + locale,100L);
		_log.info("LOG_DEBUG_EXTENSION_WmsDataProviderImpl", "Locale " + locale, SuggestedCategory.NONE);
		String db_connection = (userContext.get(SetIntoHttpSessionAction.DB_CONNECTION)).toString();
		//String wmWhseID = (userContext.get(SetIntoHttpSessionAction.DB_USERID)).toString();
		String wmWhseID = (userContext.get("logInUserId")).toString();
//		StringTokenizer dbstring = new StringTokenizer(db_connection,"_");
//	     while (dbstring.hasMoreTokens()) {
//	    	 wmWhseID = dbstring.nextToken();
//	     }
//HC.e	
		try
		{
			com.epiphany.shr.metadata.objects.generated.np.DataSource ds = record.getRecordSetType().getDataSource();
			SQLCreator sqllocal =  new SQLCreator(DBFactory.getDbNameFromDriverType(
					ds.getPropertyAsString(DBFactory.SERVER_TYPE)));
			Boolean quoteValue = (Boolean)ds.getPropertyAsBoolean(DataSource.IS_COLUMN_QUOTED);
			if (quoteValue != null)
				sqllocal.QuoteColumns(quoteValue.booleanValue());
//			if (updateSpec != null)
				updateStmt = sqllocal.createUpdate(record, updateSpec);
			String sql = updateStmt.toSQLWithParamString();
_log.debug("LOG_SYSTEM_OUT","updateSql="+sql,100L);
			TransactionServiceSORemote remote = EJBRemote.getRemote();
			EXEDataObject obj = remote.update(new TextData(wmWhseID),db_connection, new TextData(sql),null, true,internalCaller,locale);		//HC
			return obj.getRowCount();
		}
		catch (Exception ex)
		{
			errorLogException(ex);
			throw new DPException(ex.getMessage(), ex);
		}
		finally
		{
			if (updateStmt != null)
				try { updateStmt.close(); } catch (SQLException ex) {} // Should not be thrown if no connection established
		}
	}

	public int delete(DPConnection conn, DPRecord record, DPDeleteSpecification deleteSpec) throws DPException
	{
		DeleteStatement deleteStmt = null;
//  HC.b
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
//		String locale = userContext.getLocale();
		String locale = getBaseLocale(userContext);

		_log.debug("LOG_SYSTEM_OUT","\n\n:Locale " + locale,100L);
		_log.info("LOG_DEBUG_EXTENSION_WmsDataProviderImpl", "Locale " + locale, SuggestedCategory.NONE);
		String db_connection = (userContext.get(SetIntoHttpSessionAction.DB_CONNECTION)).toString();
		//String wmWhseID = (userContext.get(SetIntoHttpSessionAction.DB_USERID)).toString();
		String wmWhseID = (userContext.get("logInUserId")).toString();
//HC.e
		try
		{
			_log.debug("LOG_SYSTEM_OUT","In side Delete",100L);
			com.epiphany.shr.metadata.objects.generated.np.DataSource ds = record.getRecordSetType().getDataSource();
			_log.debug("LOG_SYSTEM_OUT","In side Delete DS = " + ds.toString(),100L);
			SQLCreator sqllocal =  new SQLCreator(DBFactory.getDbNameFromDriverType(
					ds.getPropertyAsString(DBFactory.SERVER_TYPE)));
			Boolean quoteValue = (Boolean)ds.getPropertyAsBoolean(DataSource.IS_COLUMN_QUOTED);
			if (quoteValue != null)
				sqllocal.QuoteColumns(quoteValue.booleanValue());
			if (deleteSpec != null && (deleteSpec.getQuery() != null)) {
				deleteStmt = sqllocal.createDelete(deleteSpec.getRecordSetType(), deleteSpec.getQuery());
			} else {
				deleteStmt = sqllocal.createDelete(record);
			}
			String sql = deleteStmt.toSQLWithParamString();
			_log.debug("LOG_SYSTEM_OUT","In side Delete sql = " + sql,100L);
			TransactionServiceSORemote remote = EJBRemote.getRemote();
			EXEDataObject obj = remote.delete(new TextData(wmWhseID),db_connection, new TextData(sql), null, true,internalCaller,locale);
			return obj.getReturnCode();
		}
		catch (Exception ex)
		{
			errorLogException(ex);
			throw new DPException(ex.getMessage(), ex);
		}
		finally
		{
			if (deleteStmt != null)
				try { deleteStmt.close(); } catch (SQLException ex) {} // Should not be thrown if no connection established
		}
	}

/*	private TransactionServiceSORemote getRemote() throws SsaException {
		try {
			_log.debug("LOG_SYSTEM_OUT","In side the getRemote",100L);
			ResourceBundle rbundle = ResourceBundle.getBundle("com.epiphany.shr.data.dp.sql.datasource");
			String provider = rbundle.getString("transactionserviceprovider");
			String factory = rbundle.getString("transactionservicefactory");
			//			HC			return (TransactionServiceSORemote) EjbHelper.getSession("usdawvssawm2:6840", null, null,
			return (TransactionServiceSORemote) EjbHelper.getSession(provider, null, null,
					factory,"TransactionServiceSORemote", TransactionServiceSORemoteHome.class);
		} catch (Exception x) {
			_log.info("*****"," it is in get remote exception ---->",SuggestedCategory.NONE);
			throw new SsaException("[getRemote]::Transaction service remote not accessible", x);
		}
	}
*/
   	//If the Locale does not have the country code attached to the language code then assign the default country for that language.
	public static String getBaseLocale(EpnyUserContext userContext){
		String locale = userContext.getLocale();
		if (locale.indexOf("_") == -1){
			if (locale.equalsIgnoreCase("en")){
				locale = locale + "_US";
			}
			if (locale.equalsIgnoreCase("de")){
				locale = locale + "_DE";
			}
			if (locale.equalsIgnoreCase("es")){
				locale = locale + "_ES";
			}
			if (locale.equalsIgnoreCase("nl")){
				locale = locale + "_NL";
			}
			if (locale.equalsIgnoreCase("ja")){
				locale = locale + "_JP";
			}
			if (locale.equalsIgnoreCase("pt")){
				locale = locale + "_BR";
			}
			if (locale.equalsIgnoreCase("zh")){
				locale = locale + "_CN";
			}
			if (locale.equalsIgnoreCase("fr")){
				locale = locale + "_FR";
			}
		}
		return locale;
	}
}


