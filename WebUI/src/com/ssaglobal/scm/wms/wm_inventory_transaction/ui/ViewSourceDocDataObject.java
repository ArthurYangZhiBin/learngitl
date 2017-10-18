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
package com.ssaglobal.scm.wms.wm_inventory_transaction.ui;

import java.io.Serializable;

public class ViewSourceDocDataObject implements Serializable{

		private String key;
		private String keyAtt;
		private String lineNumber;
		private String lineNumAtt;
		private String sourceType;
	    private String query;
		private String bioName;
		private String perspective;
		
		
		
		public String getKey(){
			return this.key;
		}
		public void setKey(String key){
			this.key = key;
		}
		public String getLineNumber(){
			return this.lineNumber;
		}
		public void setLineNumber(String lineNumber){
			this.lineNumber = lineNumber;
		}
		public String getSourceType(){
			return this.sourceType;
		}
		public void setSourceType(String sourceType){
			this.sourceType = sourceType;
		}
		public String getQuery(){
			return this.query;
		}
		public void setQuery(String query){
			this.sourceType = query;
		}
		public String getbio(){
			return this.bioName;
		}
		public void setbio(String bio){
			this.bioName = bio;
		}
		public String getPerspective(){
			return this.perspective;
		}
		public void setPerspective(String perspective){
			this.perspective = perspective;
		}
		public String getkeyAtt(){
			return this.keyAtt;
		}
		public void setkeyAtt(String keyAttr){
			this.keyAtt = keyAttr;
		}
		public String getLNAtt(){
			return this.lineNumAtt;
		}
		public void setLNAtt(String LNAtt){
			this.lineNumAtt = LNAtt;
		}

		
	}

	

