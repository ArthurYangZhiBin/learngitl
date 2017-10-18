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

public class WebuiConfig {
	private String defaultdatasource;
	private String transactionserviceprovider_server;
	private String transactionserviceprovider_port;
	private String transactionservicefactory;
	private String homepageurl;
	private String cognosserverurl;

	public void setDefaultdatasource(String defaultdatasource){
		this.defaultdatasource = defaultdatasource;
	}
	public String getDefaultdatasource(){
		return this.defaultdatasource;
	}
	public void setTransactionserviceprovider_server(String transactionserviceprovider_server){
		this.transactionserviceprovider_server = transactionserviceprovider_server;
	}
	public String getTransactionserviceprovider_server(){
		return this.transactionserviceprovider_server;
	}
	public void setTransactionserviceprovider_port(String transactionserviceprovider_port){
		this.transactionserviceprovider_port = transactionserviceprovider_port;
	}
	public String getTransactionserviceprovider_port(){
		return this.transactionserviceprovider_port;
	}
	public void setTransactionservicefactory(String transactionservicefactory){
		this.transactionservicefactory = transactionservicefactory;
	}
	public String getTransactionservicefactory(){
		return this.transactionservicefactory;
	}
	public String getHomepageURL() {
		return homepageurl;
	}
	public void setHomepageURL(String homepageurl) {
		this.homepageurl = homepageurl;
	}
	public void setCognosServerURL(String cognosserverurl){
		this.cognosserverurl = cognosserverurl;
	}
	public String getCognosServerURL(){
		return this.cognosserverurl;
	}
}
