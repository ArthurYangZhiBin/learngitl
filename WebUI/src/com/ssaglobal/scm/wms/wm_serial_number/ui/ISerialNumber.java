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
package com.ssaglobal.scm.wms.wm_serial_number.ui;

import java.util.Date;

public interface ISerialNumber {

	public String getId();
	public void setId(String id);
	public String getLoc();
	public void setLoc(String loc);
	public String getLot();
	public void setLot(String lot);

	public String getQty();
	public void setQty(String qty);
	public String getSerialnumber();
	public void setSerialnumber(String serialnumber);
	public String getSerialnumberlong();
	public void setSerialnumberlong(String serialnumberlong) ;
	public String getSku() ;
	public void setSku(String sku);
	public String getStorerkey();
	public void setStorerkey(String storerkey);
	public String getData2();
	public void setData2(String data2);
	public String getData3();
	public void setData3(String data3);
	public String getData4();
	public void setData4(String data4);
	public String getData5();
	public void setData5(String data5);
	public String getGrossweight();
	public void setGrossweight(String grossweight);
	public String getNetweight();
	public void setNetweight(String netweight);

}
