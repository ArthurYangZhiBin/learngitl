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
package com.ssaglobal.scm.wms.wm_dropid.computed;


import com.epiphany.shr.data.beans.BioServiceFactory;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;


/**
 * Modification History
 * 01/08/2009 AW 	Initial version
 * 					SDIS: SCM-00000-06252 Machine: 2253832
 * 					If Multiple SKU's were in the same CASEID, the sku should display "MIXED".
 * 					Otherwise, the sku should display.
 */



public class DropIDDetailSetItemValue implements ComputedAttributeSupport
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(DropIDDetailSetItemValue.class);

	public Object get(Bio bio, String attributeName, boolean isOldValue) throws EpiDataException
	{
		String itemNew = null;
		String ownerVal =null;
		String itemVal =null;
		String val = null;
		UnitOfWork uow;
		
		_log.debug("LOG_DEBUG_EXTENSION_DropIDDetailSetItemValue", "DropIDDetailSetItemValue " + bio.getString("DROPID") + " "
				+ bio.getString("CHILDID"), SuggestedCategory.NONE);

		try
		{
			uow = BioServiceFactory.getInstance().create("webui").getUnitOfWork();
		} catch (EpiException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			_log.error("LOG_ERROR_EXTENSION_DropIDDetailSetItemValue", e.getStackTraceAsString(), SuggestedCategory.NONE);
			return null;
		}
		//Query PICKDETAIL based on CASEID
		String caseID = bio.getString("CHILDID") == null ? null : bio.getString("CHILDID").toUpperCase();

		BioCollection collection = uow.findByQuery(new Query("wm_pickdetail", "wm_pickdetail.CASEID = '" + caseID + "'", null));
		if (collection.size() == 0) {
			return "";
		}
		if(attributeName.equalsIgnoreCase("OWNERTEMP"))
		{
		ownerVal = collection.elementAt(0).get("STORERKEY").toString(); 
		 _log.debug("LOG_DEBUG_EXTENSION_DropIDDetailSetItemValue", "Owner Val: " +ownerVal, SuggestedCategory.NONE);
		val = ownerVal;
		}
		else if(attributeName.equalsIgnoreCase("ITEMTEMP"))
		{
			itemVal = collection.elementAt(0).get("SKU").toString(); 
			 _log.debug("LOG_DEBUG_EXTENSION_DropIDDetailSetItemValue", "Item Val: " +itemVal, SuggestedCategory.NONE);
			
		if(collection.size() >1)
			itemNew = "MIXED";
		else
			itemNew= itemVal;

		val = itemNew;
		}
	    _log.debug("LOG_DEBUG_EXTENSION_DropIDDetailSetItemValue", "Exiting DropIDDetailSetItemValue", SuggestedCategory.NONE);

		return val;
	}

	public void set(Bio bio, String attributeName, Object attributeValue, boolean isOldValue) throws EpiDataException
	{
		
	}

	public boolean supportsSet(String bioTypeName, String attributeName)
	{
		return false;
	}

}
