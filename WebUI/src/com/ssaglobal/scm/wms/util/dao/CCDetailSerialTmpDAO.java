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

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsDataProviderImpl;
import com.ssaglobal.scm.wms.util.dto.CCDetailSerialTmpDTO;
import com.ssaglobal.scm.wms.wm_adjustment.ui.AdjustmentDetailSerialTmpDTO;

public class CCDetailSerialTmpDAO {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CCDetailSerialTmpDAO.class);

	public static void insertCCDetailSerialTmp(CCDetailSerialTmpDTO ccDetailSerial){
		
		String stmt ="INSERT INTO ccdetailserialtmp (serialnumber, " +
		" cckey, ccdetailkey, storerkey," +
		" sku, lot, id, loc, qty, data2, data3, data4, data5, grossweight, netweight, serialnumberlong)" +
		" VALUES (";
		
		stmt = stmt + setQuote(ccDetailSerial.getSerialnumber())+"," +
		setQuote(ccDetailSerial.getCcKey())+","+
		setQuote(ccDetailSerial.getCcDetailKey())+"," +
		setQuote(ccDetailSerial.getStorerkey())+","+
		setQuote(ccDetailSerial.getSku())+","+
		setQuote(ccDetailSerial.getLot())+","+
		setQuote(ccDetailSerial.getId())+","+
		setQuote(ccDetailSerial.getLoc())+","+
		ccDetailSerial.getQty()+","+
		setQuote(ccDetailSerial.getData2())+","+
		setQuote(ccDetailSerial.getData3())+","+
		setQuote(ccDetailSerial.getData4())+","+
		setQuote(ccDetailSerial.getData5())+","+
		setQuote(ccDetailSerial.getGrossweight())+","+
		setQuote(ccDetailSerial.getNetweight())+","+
		setQuote(ccDetailSerial.getSerialnumberlong())+")";
		
		
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

	public static void updateCCDetailSerialTmp(CCDetailSerialTmpDTO ccDetailSerial){
		String stmt = "UPDATE ccdetailserialtmp SET qty="+ ccDetailSerial.getQty() +
						" WHERE serialnumber="+setQuote(ccDetailSerial.getSerialnumber()) +
						" AND cckey ="+setQuote(ccDetailSerial.getCcKey())+
						" AND ccdetailkey = "+ setQuote(ccDetailSerial.getCcDetailKey());
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


	public static void deleteCCDetailSerialTmp(CCDetailSerialTmpDTO ccDetailSerial){
		
		String stmt ="DELETE FROM ccdetailserialtmp " +
				" WHERE serialnumber = " +setQuote(ccDetailSerial.getSerialnumber()) +
				" AND cckey = " + setQuote(ccDetailSerial.getCcKey()) +
				" AND ccdetailkey =" + setQuote(ccDetailSerial.getCcDetailKey());
		
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
	

	public static  String setQuote(String column){
		if(column==null)
			return "null";
		else
			return "'"+column+"'";
		
	}

}
