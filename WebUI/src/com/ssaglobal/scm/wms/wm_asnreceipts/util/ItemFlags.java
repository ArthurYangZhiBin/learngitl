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
/**
 * 
 */
package com.ssaglobal.scm.wms.wm_asnreceipts.util;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.util.exceptions.UserException;

public class ItemFlags
{
	boolean icwFlag;

	boolean icdFlag;

	boolean end2endFlag;

	boolean icd1uniqueFlag;
	
	boolean isDalaysNumCaptureEnabled;
	
	public boolean isDalaysNumCaptureEnabled() {
		return isDalaysNumCaptureEnabled;
	}

	public void setDalaysNumCaptureEnabled(boolean isDalaysNumCaptureEnabled) {
		this.isDalaysNumCaptureEnabled = isDalaysNumCaptureEnabled;
	}

	//SRG 09/29/10: Quality Center: Defect# 88 --- Begin
	boolean enableAdvCWgt;
	public boolean isEnableAdvCWgt() {
		return enableAdvCWgt;
	}

	public void setEnableAdvCWgt(boolean enableAdvCWgt) {
		this.enableAdvCWgt = enableAdvCWgt;
	}

	boolean catchGrossWgt;
	public boolean isCatchGrossWgt() {
		return catchGrossWgt;
	}

	public void setCatchGrossWgt(boolean catchGrossWgt) {
		this.catchGrossWgt = catchGrossWgt;
	}

	boolean catchNetWgt;
	public boolean isCatchNetWgt() {
		return catchNetWgt;
	}

	public void setCatchNetWgt(boolean catchNetWgt) {
		this.catchNetWgt = catchNetWgt;
	}

	boolean catchTareWgt;	

	public boolean isCatchTareWgt() {
		return catchTareWgt;
	}

	public void setCatchTareWgt(boolean catchTareWgt) {
		this.catchTareWgt = catchTareWgt;
	}
	//SRG 09/29/10: Quality Center: Defect# 88 --- End

	public boolean isIcwFlag()
	{
		return icwFlag;
	}

	public void setIcwFlag(boolean icwFlag)
	{
		this.icwFlag = icwFlag;
	}

	public boolean isIcdFlag()
	{
		return icdFlag;
	}

	public void setIcdFlag(boolean icdFlag)
	{
		this.icdFlag = icdFlag;
	}

	public boolean isEnd2endFlag()
	{
		return end2endFlag;
	}

	public void setEnd2endFlag(boolean end2endFlag)
	{
		this.end2endFlag = end2endFlag;
	}

	public boolean isIcd1uniqueFlag()
	{
		return icd1uniqueFlag;
	}

	public void setIcd1uniqueFlag(boolean icd1uniqueFlag)
	{
		this.icd1uniqueFlag = icd1uniqueFlag;
	}	
	

	public static ItemFlags getItemFlag(UnitOfWorkBean uow, DataBean receiptDetailFocus) throws EpiDataException, UserException
	{
		String storer = receiptDetailFocus.getValue("STORERKEY").toString();
		String sku = receiptDetailFocus.getValue("SKU").toString();
		// get Flags
		BioCollectionBean results = uow.getBioCollectionBean(new Query("sku", "sku.STORERKEY = '" + storer + "' and sku.SKU = '" + sku + "'", null));
		if (results == null || results.size() != 1)
		{
			// throw Exception
			throw new UserException("WMEXP_VALIDATESKU", new Object[] { sku, storer });
		}
		ItemFlags itemFlags = new ItemFlags(results.get("" + 0));
		return itemFlags;
	}
	//get sytem flag DELAYSNUMCAPTURE FROM NSQLCONFIG
	public static boolean getDelaysNumCaptureFlag(UnitOfWorkBean uow) throws EpiDataException, UserException
	{
		// get Flag
		Query qry = new Query("wm_system_settings", "wm_system_settings.CONFIGKEY = 'DELAYSNUMCAPTURE'", null);
		BioCollectionBean results = uow.getBioCollectionBean(qry);
		int size = results.size();
		DataBean delaysNumCapDB = results.get("" + 0);
		if("1".equalsIgnoreCase(delaysNumCapDB.getValue("NSQLVALUE").toString())){
			return true;
		}else{
			return false;
		}
	}
	public ItemFlags(DataBean skuBio)
	{
		final Object icwFlagValue = skuBio.getValue("ICWFLAG");
		if (icwFlagValue != null)
		{
			icwFlag = icwFlagValue.toString().equals("1") ? true : false;
		}
		else
		{
			icwFlag = false;
		}

		final Object icdFlagValue = skuBio.getValue("ICDFLAG");
		if (icdFlagValue != null)
		{
			icdFlag = icdFlagValue.toString().equals("1") ? true : false;
		}
		else
		{
			icdFlag = false;
		}

		final Object sNumEnd2EndValue = skuBio.getValue("SNUM_ENDTOEND");
		if (sNumEnd2EndValue != null)
		{
			end2endFlag = sNumEnd2EndValue.toString().equals("1") ? true : false;
		}
		else
		{
			end2endFlag = false;
		}

		final Object icd1UniqueValue = skuBio.getValue("ICD1UNIQUE");
		if (icd1UniqueValue != null)
		{
			icd1uniqueFlag = icd1UniqueValue.toString().equals("1") ? true : false;
		}
		else
		{
			icd1uniqueFlag = false;
		}
		
		//SRG 09/29/10: Quality Center: Defect# 88 --- Begin
		final Object enableAdvCWgtValue = skuBio.getValue("IBSUMCWFLG");
		if (enableAdvCWgtValue != null)
		{
			enableAdvCWgt = enableAdvCWgtValue.toString().equals("1") ? true : false;
		}
		else
		{
			enableAdvCWgt = false;
		}
		
		final Object catchGrossWgtValue = skuBio.getValue("catchgrosswgt");
		if (catchGrossWgtValue != null)
		{
			catchGrossWgt = catchGrossWgtValue.toString().equals("1") ? true : false;
		}
		else
		{
			catchGrossWgt = false;
		}
		
		final Object catchNetWgtValue = skuBio.getValue("catchnetwgt");
		if (catchNetWgtValue != null)
		{
			catchNetWgt = catchNetWgtValue.toString().equals("1") ? true : false;
		}
		else
		{
			catchNetWgt = false;
		}
		final Object catchTareWgtValue = skuBio.getValue("catchtarewgt");
		if (catchTareWgtValue != null)
		{
			catchTareWgt = catchTareWgtValue.toString().equals("1") ? true : false;
		}
		else
		{
			catchTareWgt = false;
		}
		//SRG 09/29/10: Quality Center: Defect# 88 --- End

	}
}