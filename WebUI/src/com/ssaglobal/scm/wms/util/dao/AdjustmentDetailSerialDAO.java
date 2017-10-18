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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import com.agileitp.forte.framework.TextData;
import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsDataProviderImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject.GetIntegerOutputParam;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject.GetStringOutputParam;
import com.ssaglobal.scm.wms.util.dto.AdjustmentDetailSerialDTO;
import com.ssaglobal.scm.wms.wm_adjustment.ui.AdjustmentDetailSerialTmpDTO;

public class AdjustmentDetailSerialDAO {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(AdjustmentDetailSerialDAO.class);

	public static void insertAdjustmentDetailSerial(AdjustmentDetailSerialDTO adjDetailSerial){
		
		String stmt ="INSERT INTO adjustmentdetailserial (serialnumber, " +
		" adjustmentkey, adjustmentlinenumber, storerkey," +
		" sku, lot, id, loc, qty, data2, data3, data4, data5, grossweight, netweight)" +
		" VALUES (";
		
		stmt = stmt + setQuote(adjDetailSerial.getSerialnumber())+"," +
		setQuote(adjDetailSerial.getAdjustmentkey())+","+
		setQuote(adjDetailSerial.getAdjustmentlinenumber())+"," +
		setQuote(adjDetailSerial.getStorerkey())+","+
		setQuote(adjDetailSerial.getSku())+","+
		setQuote(adjDetailSerial.getLot())+","+
		setQuote(adjDetailSerial.getId())+","+
		setQuote(adjDetailSerial.getLoc())+","+
		adjDetailSerial.getQty()+","+
		setQuote(adjDetailSerial.getData2())+","+
		setQuote(adjDetailSerial.getData3())+","+
		setQuote(adjDetailSerial.getData4())+","+
		setQuote(adjDetailSerial.getData5())+","+
		setQuote(adjDetailSerial.getGrossweight())+","+
		setQuote(adjDetailSerial.getNetweight())+")";
		//setQuote(adjDetailSerial.getSerialnumberlong())+")";
		
		
		_log.debug("LOG_SYSTEM_OUT","stmt:"+stmt,100L);
		try {
			new WmsDataProviderImpl().executeUpdateSql(stmt);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public static void updateAdjustmentDetailSerial(AdjustmentDetailSerialDTO adjDetailSerial){
		String stmt = "UPDATE adjustmentdetailserial SET qty="+ adjDetailSerial.getQty() +
						" WHERE serialnumber="+setQuote(adjDetailSerial.getSerialnumber()) +
						" AND adjustmentkey ="+setQuote(adjDetailSerial.getAdjustmentkey())+
						" AND adjustmentlinenumber = "+ setQuote(adjDetailSerial.getAdjustmentlinenumber());
		_log.debug("LOG_SYSTEM_OUT","stmt:"+stmt,100L);
		try {
			new WmsDataProviderImpl().executeUpdateSql(stmt);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public AdjustmentDetailSerialDTO findAdjustmentDetailSerialTmp(String adjustmentKey, String adjustmentLineNumber, String serialnumber){
		
		AdjustmentDetailSerialDTO adjDetailSerialDTO = new AdjustmentDetailSerialDTO();
		
		String stmt = "SELECT adjustmentkey, adjustmentlinenumber, " +
		" data2, data3, data4, data5, grossweight, id, loc, "+
		" lot, netweight, qty, serialnumber, serialnumberlong, "+
		" sku, storerkey" +
		" FROM adjustmentDetailSerial "+
		" WHERE adjustmentKey = "+setQuote(adjustmentKey)+
		" AND adjustmentLineNumber = "+setQuote(adjustmentLineNumber) +
		" AND serialnumber = "+setQuote(serialnumber);
		
		try {
			EXEDataObject adjDetailSerial = WmsWebuiValidationSelectImpl.select(stmt);
			GetIntegerOutputParam paramInt= null;
			GetStringOutputParam paramString = null;
			
			if(adjDetailSerial.getRowCount()>0){
				
				paramString= adjDetailSerial.getString(new TextData("adjustmentkey"), new String());
				adjDetailSerialDTO.setAdjustmentkey(paramString.pResult.trim());
				
				paramString= adjDetailSerial.getString(new TextData("adjustmentlinenumber"), new String());
				adjDetailSerialDTO.setAdjustmentlinenumber(paramString.pResult.trim());
				
				paramString= adjDetailSerial.getString(new TextData("data2"), new String());
				adjDetailSerialDTO.setData2(paramString.pResult.trim());
				
				paramString= adjDetailSerial.getString(new TextData("data3"), new String());
				adjDetailSerialDTO.setData3(paramString.pResult.trim());
				
				paramString= adjDetailSerial.getString(new TextData("data4"), new String());
				adjDetailSerialDTO.setData4(paramString.pResult.trim());
				
				paramString= adjDetailSerial.getString(new TextData("data5"), new String());
				adjDetailSerialDTO.setData5(paramString.pResult.trim());
				
				paramString= adjDetailSerial.getString(new TextData("grossweight"), new String());
				adjDetailSerialDTO.setGrossweight(paramString.pResult.trim());
				
				paramString= adjDetailSerial.getString(new TextData("id"), new String());
				adjDetailSerialDTO.setId(paramString.pResult.trim());
				
				paramString= adjDetailSerial.getString(new TextData("loc"), new String());
				adjDetailSerialDTO.setLoc(paramString.pResult.trim());
				
				paramString= adjDetailSerial.getString(new TextData("lot"), new String());
				adjDetailSerialDTO.setLot(paramString.pResult.trim());
				
				paramString= adjDetailSerial.getString(new TextData("netweight"), new String());
				adjDetailSerialDTO.setNetweight(paramString.pResult.trim());
				
				paramString= adjDetailSerial.getString(new TextData("qty"), new String());
				adjDetailSerialDTO.setQty(paramString.pResult.trim());
				
				paramString= adjDetailSerial.getString(new TextData("serialnumber"), new String());
				adjDetailSerialDTO.setSerialnumber(paramString.pResult.trim());
				
				paramString= adjDetailSerial.getString(new TextData("serialnumberlong"), new String());
				adjDetailSerialDTO.setSerialnumberlong(paramString.pResult.trim());
				
				paramString= adjDetailSerial.getString(new TextData("sku"), new String());
				adjDetailSerialDTO.setSku(paramString.pResult.trim());
				
				paramString= adjDetailSerial.getString(new TextData("storerkey"), new String());
				adjDetailSerialDTO.setStorerkey(paramString.pResult.trim());
				
			}else{
				adjDetailSerialDTO = null;
			}
			
			
		} catch (DPException e) {
			
			e.printStackTrace();
		}
		return adjDetailSerialDTO;
		
	}
	public List<AdjustmentDetailSerialDTO> findAdjustmentDetailSerialTmp(String adjustmentKey, String adjustmentLineNumber){
		List<AdjustmentDetailSerialDTO> list = new ArrayList<AdjustmentDetailSerialDTO>();
		
		String stmt = "SELECT adjustmentkey, adjustmentlinenumber, " +
					" data2, data3, data4, data5, grossweight, id, loc, "+
					" lot, netweight, qty, serialnumber, serialnumberlong, "+
					" sku, storerkey" +
					" FROM adjustmentDetailSerial "+
					" WHERE adjustmentKey = "+setQuote(adjustmentKey)+
					" AND adjustmentLineNumber = "+setQuote(adjustmentLineNumber);
		try {
			EXEDataObject adjDetailSerial = WmsWebuiValidationSelectImpl.select(stmt);
			GetIntegerOutputParam paramInt= null;
			GetStringOutputParam paramString = null;
			while(true){
				AdjustmentDetailSerialDTO adjDetailSerialDTO = new AdjustmentDetailSerialDTO();
				
				paramString= adjDetailSerial.getString(new TextData("adjustmentkey"), new String());
				adjDetailSerialDTO.setAdjustmentkey(paramString.pResult.trim());

				paramString= adjDetailSerial.getString(new TextData("adjustmentlinenumber"), new String());
				adjDetailSerialDTO.setAdjustmentlinenumber(paramString.pResult.trim());

				paramString= adjDetailSerial.getString(new TextData("data2"), new String());
				adjDetailSerialDTO.setData2(paramString.pResult.trim());

				paramString= adjDetailSerial.getString(new TextData("data3"), new String());
				adjDetailSerialDTO.setData3(paramString.pResult.trim());
				
				paramString= adjDetailSerial.getString(new TextData("data4"), new String());
				adjDetailSerialDTO.setData4(paramString.pResult.trim());

				paramString= adjDetailSerial.getString(new TextData("data5"), new String());
				adjDetailSerialDTO.setData5(paramString.pResult.trim());

				paramString= adjDetailSerial.getString(new TextData("grossweight"), new String());
				adjDetailSerialDTO.setGrossweight(paramString.pResult.trim());

				paramString= adjDetailSerial.getString(new TextData("id"), new String());
				adjDetailSerialDTO.setId(paramString.pResult.trim());

				paramString= adjDetailSerial.getString(new TextData("loc"), new String());
				adjDetailSerialDTO.setLoc(paramString.pResult.trim());

				paramString= adjDetailSerial.getString(new TextData("lot"), new String());
				adjDetailSerialDTO.setLot(paramString.pResult.trim());

				paramString= adjDetailSerial.getString(new TextData("netweight"), new String());
				adjDetailSerialDTO.setNetweight(paramString.pResult.trim());

				paramString= adjDetailSerial.getString(new TextData("qty"), new String());
				adjDetailSerialDTO.setQty(paramString.pResult.trim());

				paramString= adjDetailSerial.getString(new TextData("serialnumber"), new String());
				adjDetailSerialDTO.setSerialnumber(paramString.pResult.trim());

				paramString= adjDetailSerial.getString(new TextData("serialnumberlong"), new String());
				adjDetailSerialDTO.setSerialnumberlong(paramString.pResult.trim());

				paramString= adjDetailSerial.getString(new TextData("sku"), new String());
				adjDetailSerialDTO.setSku(paramString.pResult.trim());

				paramString= adjDetailSerial.getString(new TextData("storerkey"), new String());
				adjDetailSerialDTO.setStorerkey(paramString.pResult.trim());

				list.add(adjDetailSerialDTO);
				
				if(!adjDetailSerial.getNextRow())
					break;
			}
		} catch (DPException e) {

			e.printStackTrace();
		}
		return list;
	}


	private static  String setQuote(String column){
		if(column==null)
			return "null";
		else
			return "'"+column+"'";
		
	}
}
