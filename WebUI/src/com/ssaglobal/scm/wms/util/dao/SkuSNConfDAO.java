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
package com.ssaglobal.scm.wms.util.dao;

import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject.GetIntegerOutputParam;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject.GetStringOutputParam;
import com.ssaglobal.scm.wms.service.dutilitymanagement.dto.SkuSNConfDTO;

public class SkuSNConfDAO {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(SkuSNConfDAO.class);

	public static SkuSNConfDTO getSkuSNConf(String storerkey, String sku){
		SkuSNConfDTO skuSNConf = new SkuSNConfDTO();
		
		String stmt = "SELECT SNum_Position, SNum_Length, SNum_Delimiter, SNum_Delim_Count, " +
                      " SNum_Quantity, SNum_Incr_Pos, SNum_Incr_Length, SNum_EndToEnd, SNum_Mask, " +
                      " SNumLong_Delimiter, SNumLong_Fixed, SNum_AutoIncrement, " +
                      " ICD1Unique, OCD1Unique " +
                      " FROM SKU " +
                      " WHERE Storerkey = '"+storerkey +"' " +
                      " AND Sku = '"+sku+"'";

		try {
			String tmp ="";
			EXEDataObject skuConf = WmsWebuiValidationSelectImpl.select(stmt);
			GetIntegerOutputParam paramInt= null;
			GetStringOutputParam paramString = null;
			
			paramString= skuConf.getString(new TextData("SNum_Position"), new String());
			tmp = paramString.pResult.trim();
			int length = tmp.length();
			if(length == 0){
				skuSNConf.setSNum_Position(1);
			}else{//length > 0
				int dotIndex = tmp.indexOf(".");
				if(dotIndex != -1){
					skuSNConf.setSNum_Position(Integer.parseInt(tmp.substring(0, dotIndex)));
				}else{
					skuSNConf.setSNum_Position(Integer.parseInt(tmp));
				}
			}
//			skuSNConf.setSNum_Position(paramString.pResult.trim().length()>0 ? Integer.parseInt(paramString.pResult) : 1);

			paramString= skuConf.getString(new TextData("SNum_Length"), new String());
			tmp = paramString.pResult.trim();
			length = tmp.length();
			if(length == 0){
				skuSNConf.setSNum_Length(0);
			}else{//length > 0
				int dotIndex = tmp.indexOf(".");
				if(dotIndex != -1){
					skuSNConf.setSNum_Length(Integer.parseInt(tmp.substring(0, dotIndex)));
				}else{
					skuSNConf.setSNum_Length(Integer.parseInt(tmp));
				}
			}
//			skuSNConf.setSNum_Length(paramString.pResult.trim().length()>0 ? Integer.parseInt(paramString.pResult) : 0);

			paramString= skuConf.getString(new TextData("SNum_Delimiter"), skuSNConf.getSNum_Delimiter());
			skuSNConf.setSNum_Delimiter(paramString.pResult);

			paramString= skuConf.getString(new TextData("SNum_Delim_Count"), new String());
			tmp = paramString.pResult.trim();
			length = tmp.length();
			if(length == 0){
				skuSNConf.setSNum_Delim_Count(0);
			}else{//length > 0
				int dotIndex = tmp.indexOf(".");
				if(dotIndex != -1){
					skuSNConf.setSNum_Delim_Count(Integer.parseInt(tmp.substring(0, dotIndex)));
				}else{
					skuSNConf.setSNum_Delim_Count(Integer.parseInt(tmp));
				}
			}
//			skuSNConf.setSNum_Delim_Count(paramString.pResult.trim().length()>0 ? Integer.parseInt(paramString.pResult) : 0);

			paramString= skuConf.getString(new TextData("SNum_Quantity"), new String());
			tmp = paramString.pResult.trim();
			length = tmp.length();
			if(length == 0){
				skuSNConf.setSNum_Quantity(1);
			}else{//length > 0
				int dotIndex = tmp.indexOf(".");
				if(dotIndex != -1){
					skuSNConf.setSNum_Quantity(Integer.parseInt(tmp.substring(0, dotIndex)));
				}else{
					skuSNConf.setSNum_Quantity(Integer.parseInt(tmp));
				}
			}
//			skuSNConf.setSNum_Quantity(paramString.pResult.trim().length()>0 ? Integer.parseInt(paramString.pResult) : 1);

			paramString= skuConf.getString(new TextData("SNum_Incr_Pos"), new String());
			tmp = paramString.pResult.trim();
			length = tmp.length();
			if(length == 0){
				skuSNConf.setSNum_Incr_Pos(0);
			}else{//length > 0
				int dotIndex = tmp.indexOf(".");
				if(dotIndex != -1){
					skuSNConf.setSNum_Incr_Pos(Integer.parseInt(tmp.substring(0, dotIndex)));
				}else{
					skuSNConf.setSNum_Incr_Pos(Integer.parseInt(tmp));
				}
			}
//			skuSNConf.setSNum_Incr_Pos(paramString.pResult.trim().length()>0 ? Integer.parseInt(paramString.pResult) : 0);

			paramString= skuConf.getString(new TextData("SNum_Incr_Length"), new String());
			tmp = paramString.pResult.trim();
			length = tmp.length();
			if(length == 0){
				skuSNConf.setSNum_Incr_Length(0);
			}else{//length > 0
				int dotIndex = tmp.indexOf(".");
				if(dotIndex != -1){
					skuSNConf.setSNum_Incr_Length(Integer.parseInt(tmp.substring(0, dotIndex)));
				}else{
					skuSNConf.setSNum_Incr_Length(Integer.parseInt(tmp));
				}
			}
//			skuSNConf.setSNum_Incr_Length(paramString.pResult.trim().length()>0 ? Integer.parseInt(paramString.pResult) : 0);

			paramString= skuConf.getString(new TextData("SNum_EndToEnd"), new String());
			skuSNConf.setSNum_EndToEnd(paramString.pResult);

			paramString= skuConf.getString(new TextData("SNum_Mask"), skuSNConf.getSNum_Mask());
			skuSNConf.setSNum_Mask(paramString.pResult);

			paramString= skuConf.getString(new TextData("SNumLong_Delimiter"), skuSNConf.getSNumLong_Delimiter());
			skuSNConf.setSNumLong_Delimiter(paramString.pResult);

			/**
			paramInt = skuConf.getInteger(new TextData("SNumLong_Fixed"), skuSNConf.getSNumLong_Fixed());
			skuSNConf.setSNumLong_Fixed(paramInt.pResult);
			**/

			paramString= skuConf.getString(new TextData("SNumLong_Fixed"), new String());
			if(paramString==null || paramString.pResult==null 
					|| paramString.pResult.trim().length()==0 || paramString.pResult.equalsIgnoreCase("N/A"))
				skuSNConf.setSNumLong_Fixed(0);
			else{	
				tmp = paramString.pResult.trim();
				length = tmp.length();
				if(length == 0){
					skuSNConf.setSNumLong_Fixed(0);
				}else{//length > 0
					int dotIndex = tmp.indexOf(".");
					if(dotIndex != -1){
						skuSNConf.setSNumLong_Fixed(Integer.parseInt(tmp.substring(0, dotIndex)));
					}else{
						skuSNConf.setSNumLong_Fixed(Integer.parseInt(tmp));
					}
				}
//				skuSNConf.setSNumLong_Fixed(Integer.parseInt(paramString.pResult));
			}
			
			_log.debug("LOG_SYSTEM_OUT","PARAMSTRING Fixed:"+paramString,100L);
			/**
			paramInt = skuConf.getInteger(new TextData("SNum_AutoIncrement"), skuSNConf.getSNum_AutoIncrement());
			skuSNConf.setSNum_AutoIncrement(paramInt.pResult);
			**/
			
			paramString= skuConf.getString(new TextData("SNum_AutoIncrement"), new String());
			if(paramString==null || paramString.pResult==null 
					|| paramString.pResult.trim().length()==0  || paramString.pResult.equalsIgnoreCase("N/A"))
				skuSNConf.setSNum_AutoIncrement(0);
			else{	
				tmp = paramString.pResult.trim();
				length = tmp.length();
				if(length == 0){
					skuSNConf.setSNum_AutoIncrement(0);
				}else{//length > 0
					int dotIndex = tmp.indexOf(".");
					if(dotIndex != -1){
						skuSNConf.setSNum_AutoIncrement(Integer.parseInt(tmp.substring(0, dotIndex)));
					}else{
						skuSNConf.setSNum_AutoIncrement(Integer.parseInt(tmp));
					}
				}
//				skuSNConf.setSNum_AutoIncrement(Integer.parseInt(paramString.pResult));
			}

			paramString= skuConf.getString(new TextData("ICD1Unique"), skuSNConf.getICD1Unique());
			skuSNConf.setICD1Unique(paramString.pResult);
			
			paramString= skuConf.getString(new TextData("OCD1Unique"), skuSNConf.getOCD1Unique());
			skuSNConf.setOCD1Unique(paramString.pResult);

		} catch (DPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return skuSNConf;
	}
	
	public static boolean isSkuSerialNumberEndtoEnd(String storerkey, String sku){
		SkuSNConfDTO skuConf = SkuSNConfDAO.getSkuSNConf(storerkey, sku);
		
		_log.debug("LOG_SYSTEM_OUT","[isSkuSerialNumberEndToEnd]:"+skuConf.getSNum_EndToEnd(),100L);
		if(skuConf.getSNum_EndToEnd().equalsIgnoreCase("1"))
			return true;
		else
			return false;
		
	}

}
