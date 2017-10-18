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
package com.ssaglobal.scm.wms.wm_customer.ui;

import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.duplicate.IDuplicate;
import com.ssaglobal.scm.wms.util.duplicate.PopulateBase;
import com.ssaglobal.scm.wms.util.duplicate.PopulateBioType;

public class PopulateCustomer extends PopulateBase  {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PopulateCustomer.class);
	public PopulateBioType ValidateAndLoad(DataBean dupDataBean, QBEBioBean newBioBean) {
		// TODO Auto-generated method stub
		PopulateBioType populateBioType = new PopulateBioType();
		populateBioType.setStatus(true);

		try{

			//newBioBean.set("ADDDATE", dupDataBean.getValue("ADDDATE"));
			if (dupDataBean.getValue("ADDRESS1") != null && dupDataBean.getValue("ADDRESS1").toString().trim().length()>0)
				newBioBean.set("ADDRESS1", dupDataBean.getValue("ADDRESS1"));
			
			if (dupDataBean.getValue("ADDRESS2") != null && dupDataBean.getValue("ADDRESS2").toString().trim().length()>0)
				newBioBean.set("ADDRESS2", dupDataBean.getValue("ADDRESS2"));
			
			if (dupDataBean.getValue("ADDRESS3") != null && dupDataBean.getValue("ADDRESS3").toString().trim().length()>0)
				newBioBean.set("ADDRESS3", dupDataBean.getValue("ADDRESS3"));
			
			if (dupDataBean.getValue("ADDRESS4") != null && dupDataBean.getValue("ADDRESS4").toString().trim().length()>0)
				newBioBean.set("ADDRESS4", dupDataBean.getValue("ADDRESS4"));
			
			
			if (dupDataBean.getValue("B_ADDRESS1") != null && dupDataBean.getValue("B_ADDRESS1").toString().trim().length()>0)
				newBioBean.set("B_ADDRESS1", dupDataBean.getValue("B_ADDRESS1"));
			
			if (dupDataBean.getValue("B_ADDRESS2") != null && dupDataBean.getValue("B_ADDRESS2").toString().trim().length()>0)
				newBioBean.set("B_ADDRESS2", dupDataBean.getValue("B_ADDRESS2"));
			
			if (dupDataBean.getValue("B_ADDRESS3") != null && dupDataBean.getValue("B_ADDRESS3").toString().trim().length()>0)
				newBioBean.set("B_ADDRESS3", dupDataBean.getValue("B_ADDRESS3"));
			
			if (dupDataBean.getValue("B_ADDRESS4") != null && dupDataBean.getValue("B_ADDRESS4").toString().trim().length()>0)
				newBioBean.set("B_ADDRESS4", dupDataBean.getValue("B_ADDRESS4"));
			
			if (dupDataBean.getValue("B_CITY") != null && dupDataBean.getValue("B_CITY").toString().trim().length()>0)
				newBioBean.set("B_CITY", dupDataBean.getValue("B_CITY"));
			/**
			if (dupDataBean.getValue("B_COMPANY") != null && dupDataBean.getValue("B_COMPANY").toString().trim().length()>0)
				newBioBean.set("B_COMPANY", dupDataBean.getValue("B_COMPANY"));
			**/
			if (dupDataBean.getValue("B_CONTACT1") != null && dupDataBean.getValue("B_CONTACT1").toString().trim().length()>0)
				newBioBean.set("B_CONTACT1", dupDataBean.getValue("B_CONTACT1"));
			
			if (dupDataBean.getValue("B_CONTACT2") != null && dupDataBean.getValue("B_CONTACT2").toString().trim().length()>0)
				newBioBean.set("B_CONTACT2", dupDataBean.getValue("B_CONTACT2"));
			
			if (dupDataBean.getValue("B_COUNTRY") != null && dupDataBean.getValue("B_COUNTRY").toString().trim().length()>0)
				newBioBean.set("B_COUNTRY", dupDataBean.getValue("B_COUNTRY"));
			
			if (dupDataBean.getValue("B_EMAIL1") != null && dupDataBean.getValue("B_EMAIL1").toString().trim().length()>0)
				newBioBean.set("B_EMAIL1", dupDataBean.getValue("B_EMAIL1"));
			
			if (dupDataBean.getValue("B_EMAIL2") != null && dupDataBean.getValue("B_EMAIL2").toString().trim().length()>0)
				newBioBean.set("B_EMAIL2", dupDataBean.getValue("B_EMAIL2"));
			
			if (dupDataBean.getValue("B_FAX1") != null && dupDataBean.getValue("B_FAX1").toString().trim().length()>0)
				newBioBean.set("B_FAX1", dupDataBean.getValue("B_FAX1"));
			
			if (dupDataBean.getValue("B_FAX2") != null && dupDataBean.getValue("B_FAX2").toString().trim().length()>0)
				newBioBean.set("B_FAX2", dupDataBean.getValue("B_FAX2"));
			
			if (dupDataBean.getValue("B_ISOCNTRYCODE") != null && dupDataBean.getValue("B_ISOCNTRYCODE").toString().trim().length()>0)
				newBioBean.set("B_ISOCNTRYCODE", dupDataBean.getValue("B_ISOCNTRYCODE"));
			
			if (dupDataBean.getValue("B_PHONE1") != null && dupDataBean.getValue("B_PHONE1").toString().trim().length()>0)
				newBioBean.set("B_PHONE1", dupDataBean.getValue("B_PHONE1"));
			
			if (dupDataBean.getValue("B_PHONE2") != null && dupDataBean.getValue("B_PHONE2").toString().trim().length()>0)
				newBioBean.set("B_PHONE2", dupDataBean.getValue("B_PHONE2"));
			
			if (dupDataBean.getValue("B_STATE") != null && dupDataBean.getValue("B_STATE").toString().trim().length()>0)
				newBioBean.set("B_STATE", dupDataBean.getValue("B_STATE"));

			if (dupDataBean.getValue("B_ZIP") != null && dupDataBean.getValue("B_ZIP").toString().trim().length()>0)
				newBioBean.set("B_ZIP", dupDataBean.getValue("B_ZIP"));

			
			if (dupDataBean.getValue("CITY") != null && dupDataBean.getValue("CITY").toString().trim().length()>0)
				newBioBean.set("CITY", dupDataBean.getValue("CITY"));
			
			
			if (dupDataBean.getValue("COMPANY") != null && dupDataBean.getValue("COMPANY").toString().trim().length()>0)
				newBioBean.set("COMPANY", dupDataBean.getValue("COMPANY"));
			
			
			if (dupDataBean.getValue("CONTACT1") != null && dupDataBean.getValue("CONTACT1").toString().trim().length()>0)
				newBioBean.set("CONTACT1", dupDataBean.getValue("CONTACT1"));
			
			if (dupDataBean.getValue("CONTACT2") != null && dupDataBean.getValue("CONTACT2").toString().trim().length()>0)
				newBioBean.set("CONTACT2", dupDataBean.getValue("CONTACT2"));
			
			if (dupDataBean.getValue("COUNTRY") != null && dupDataBean.getValue("COUNTRY").toString().trim().length()>0)
				newBioBean.set("COUNTRY", dupDataBean.getValue("COUNTRY"));
			
			if (dupDataBean.getValue("CWOFLAG") != null && dupDataBean.getValue("CWOFLAG").toString().trim().length()>0)
				newBioBean.set("CWOFLAG", dupDataBean.getValue("CWOFLAG"));
			
			
			if (dupDataBean.getValue("DESCRIPTION") != null && dupDataBean.getValue("DESCRIPTION").toString().trim().length()>0)
				newBioBean.set("DESCRIPTION", dupDataBean.getValue("DESCRIPTION"));
			
			/**
			if (dupDataBean.getValue("EDITDATE") != null && dupDataBean.getValue("EDITDATE").toString().trim().length()>0)
				newBioBean.set("EDITDATE", dupDataBean.getValue("EDITDATE"));
			if (dupDataBean.getValue("EDITWHO") != null && dupDataBean.getValue("EDITWHO").toString().trim().length()>0)
				newBioBean.set("EDITWHO", dupDataBean.getValue("EDITWHO"));
			**/
			
			if (dupDataBean.getValue("EMAIL1") != null && dupDataBean.getValue("EMAIL1").toString().trim().length()>0)
				newBioBean.set("EMAIL1", dupDataBean.getValue("EMAIL1"));
			
			if (dupDataBean.getValue("EMAIL2") != null && dupDataBean.getValue("EMAIL2").toString().trim().length()>0)
				newBioBean.set("EMAIL2", dupDataBean.getValue("EMAIL2"));
			
		
			if (dupDataBean.getValue("FAX1") != null && dupDataBean.getValue("FAX1").toString().trim().length()>0)
				newBioBean.set("FAX1", dupDataBean.getValue("FAX1"));
			
			if (dupDataBean.getValue("FAX2") != null && dupDataBean.getValue("FAX2").toString().trim().length()>0)
				newBioBean.set("FAX2", dupDataBean.getValue("FAX2"));
			
			if (dupDataBean.getValue("ISOCNTRYCODE") != null && dupDataBean.getValue("ISOCNTRYCODE").toString().trim().length()>0)
				newBioBean.set("ISOCNTRYCODE", dupDataBean.getValue("ISOCNTRYCODE"));
			
			
			if (dupDataBean.getValue("NOTES1") != null && dupDataBean.getValue("NOTES1").toString().trim().length()>0)
				newBioBean.set("NOTES1", dupDataBean.getValue("NOTES1"));
			
			if (dupDataBean.getValue("PHONE1") != null && dupDataBean.getValue("PHONE1").toString().trim().length()>0)
				newBioBean.set("PHONE1", dupDataBean.getValue("PHONE1"));
			
			if (dupDataBean.getValue("PHONE2") != null && dupDataBean.getValue("PHONE2").toString().trim().length()>0)
				newBioBean.set("PHONE2", dupDataBean.getValue("PHONE2"));
			
			if (dupDataBean.getValue("SCAC_CODE") != null && dupDataBean.getValue("SCAC_CODE").toString().trim().length()>0)
				newBioBean.set("SCAC_CODE", dupDataBean.getValue("SCAC_CODE"));
			
			/**
			if (dupDataBean.getValue("SERIALKEY") != null && dupDataBean.getValue("SERIALKEY").toString().trim().length()>0)
				newBioBean.set("SERIALKEY", dupDataBean.getValue("SERIALKEY"));
			**/
			
			if (dupDataBean.getValue("STATE") != null && dupDataBean.getValue("STATE").toString().trim().length()>0)
				newBioBean.set("STATE", dupDataBean.getValue("STATE"));
			
			/**
			if (dupDataBean.getValue("STORERKEY") != null && dupDataBean.getValue("STORERKEY").toString().trim().length()>0)
				newBioBean.set("STORERKEY", dupDataBean.getValue("STORERKEY"));
			**/
			newBioBean.set("STORERKEY","");
			
			if (dupDataBean.getValue("SUSR1") != null && dupDataBean.getValue("SUSR1").toString().trim().length()>0)
				newBioBean.set("SUSR1", dupDataBean.getValue("SUSR1"));
			
			if (dupDataBean.getValue("SUSR2") != null && dupDataBean.getValue("SUSR2").toString().trim().length()>0)
				newBioBean.set("SUSR2", dupDataBean.getValue("SUSR2"));
			
			if (dupDataBean.getValue("SUSR3") != null && dupDataBean.getValue("SUSR3").toString().trim().length()>0)
				newBioBean.set("SUSR3", dupDataBean.getValue("SUSR3"));
			
			if (dupDataBean.getValue("SUSR4") != null && dupDataBean.getValue("SUSR4").toString().trim().length()>0)
				newBioBean.set("SUSR4", dupDataBean.getValue("SUSR4"));
			
			if (dupDataBean.getValue("SUSR5") != null && dupDataBean.getValue("SUSR5").toString().trim().length()>0)
				newBioBean.set("SUSR5", dupDataBean.getValue("SUSR5"));
			
			if (dupDataBean.getValue("TYPE") != null && dupDataBean.getValue("TYPE").toString().trim().length()>0)
				newBioBean.set("TYPE", dupDataBean.getValue("TYPE"));
			
			if (dupDataBean.getValue("VAT") != null && dupDataBean.getValue("VAT").toString().trim().length()>0)
				newBioBean.set("VAT", dupDataBean.getValue("VAT"));
			
			if (dupDataBean.getValue("ZIP") != null && dupDataBean.getValue("ZIP").toString().trim().length()>0)
				newBioBean.set("ZIP", dupDataBean.getValue("ZIP"));
			
		}catch(Exception e){
			
			_log.debug("LOG_SYSTEM_OUT","Error populating Customer."+e.getMessage(),100L);
			e.printStackTrace();
			populateBioType.setStatus(false);
		}
		
		populateBioType.setqbeBioBean(newBioBean);
		return populateBioType;
	}


}
