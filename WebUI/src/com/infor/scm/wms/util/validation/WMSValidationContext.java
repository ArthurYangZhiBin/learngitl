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
package com.infor.scm.wms.util.validation;

import com.infor.scm.wms.util.datalayer.driver.DataLayerConnection;

public class WMSValidationContext{
	private String userName;	
	private String password;
	private String facilityConnection;
	private String isEnterprise;
	private String databaseName;
	private String databaseUser;
	private String databasePassword;
	private String oaServerConnectionString;
	private DataLayerConnection connection = null;
	private String namingFactory = null;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getFacilityConnection() {
		return facilityConnection;
	}
	public void setFacilityConnection(String facilityConnection) {
		this.facilityConnection = facilityConnection;
	}
	public String getIsEnterprise() {
		return isEnterprise;
	}
	public void setIsEnterprise(String isEnterprise) {
		this.isEnterprise = isEnterprise;
	}
	public String getDatabaseName() {
		return databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	public String getDatabaseUser() {
		return databaseUser;
	}
	public void setDatabaseUser(String databaseUser) {
		this.databaseUser = databaseUser;
	}
	public String getDatabasePassword() {
		return databasePassword;
	}
	public void setDatabasePassword(String databasePassword) {
		this.databasePassword = databasePassword;
	}
	public String getOaServerConnectionString() {
		return oaServerConnectionString;
	}
	public void setOaServerConnectionString(String oaServerConnectionString) {
		this.oaServerConnectionString = oaServerConnectionString;
	}
	public DataLayerConnection getConnection() {
		return connection;
	}
	public void setConnection(DataLayerConnection connection) {
		this.connection = connection;
	}
	public String getNamingFactory() {
		return namingFactory;
	}
	public void setNamingFactory(String namingFactory) {
		this.namingFactory = namingFactory;
	}
	
}