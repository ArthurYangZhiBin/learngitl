 /******************************************************
 *
 *                       NOTICE
 *
 *   THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS
 *   CONFIDENTIAL INFORMATION OF INFOR AND SHALL NOT
 *   BE DISCLOSED WITHOUT PRIOR WRITTEN PERMISSION.
 *   LICENSED CUSTOMERS MAY COPY AND ADAPT THIS
 *   SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH
 *   THE TERMS OF THEIR SOFTWARE LICENSE AGREEMENT.
 *   ALL OTHER RIGHTS RESERVED.
 *
 *   COPYRIGHT (c) 2008 INFOR. ALL RIGHTS RESERVED.
 *   THE WORD AND DESIGN MARKS SET FORTH HEREIN ARE
 *   TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 *   AND/OR RELATED AFFILIATES AND SUBSIDIARIES. ALL
 *   RIGHTS RESERVED. ALL OTHER TRADEMARKS LISTED
 *   HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE
 *   OWNERS. WWW.INFOR.COM.
 *
 ******************************************************/



package com.ssaglobal.scm.wms.wm_cyclecount.ui;

// 16/Dec/2009: A new class added for 3PL Enhancements Catch Weight Changes - Seshu
// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.agileitp.forte.framework.DataValue;
import com.agileitp.forte.framework.TextData;
import com.agileitp.forte.genericdbms.DBHelper;
import com.agileitp.forte.genericdbms.DBResourceException;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;

/**
* 
* @return int RET_CONTINUE, RET_CANCEL
*/

public class CalculateAdvCatchWeightsHelper {
	private static final String isEnabled = "1";
   public HashMap getCalculatedWeightsLot( String storerKey, String sku, double qty) throws EpiException { 
		   				   
	   EXEDataObject results = null;
	   
	   String query = "SELECT IBSUMCWFLG,PACKKEY, STDUOM, TAREWGT1, STDNETWGT1, STDGROSSWGT1 FROM SKU " +
							"WHERE STORERKEY = '" + storerKey + "' and SKU = '" + sku + "'";   
	   try{
		   results = WmsWebuiValidationSelectImpl.select(query);		   
	   }catch(Exception e){
		   e.printStackTrace();
	   }
	   
	   DataValue skuPackKey=results.getAttribValue(new TextData("PACKKEY"));
	   DataValue uom=results.getAttribValue(new TextData("STDUOM"));
	   DataValue tareWgt=results.getAttribValue(new TextData("TAREWGT1"));   
	   DataValue netWgt=results.getAttribValue(new TextData("STDNETWGT1"));
	   DataValue grossWgt=results.getAttribValue(new TextData("STDGROSSWGT1"));
	   
	   double skuTare= new Double(tareWgt.toString()).doubleValue();
	   double skuNet= new Double(netWgt.toString()).doubleValue();
	   double skuGross= new Double(grossWgt.toString()).doubleValue();	   
	   
	   String stdUOM=uom.toString();
	   
	   double pQty = getPackQty(stdUOM, skuPackKey.toString());
	   
	   if(pQty == 0)
	   {
		   //Throw exception as the STDUOM defined on SKU settings is invalid.
		   String[] parameters = new String[1];
		   throw new UserException("WMEXP_CC_INVALID_WGTS_UOM", parameters);
	   }
	   
	   double gross = skuGross * (qty / pQty);
	   double net= skuNet * (qty / pQty);
	   double tare = skuTare * (qty / pQty);   
	   
	   HashMap wgts = new HashMap();
	   wgts.put("GROSSWEIGHT", new Double(gross));
	   wgts.put("NETWEIGHT", new Double(net));
	   wgts.put("TAREWEIGHT", new Double(tare));
	   
	   return wgts;
   }
   public HashMap getCalculatedWeightsLPN( String storerKey, String sku, String loc, String lot, String lpn, double qty) { 
		
		EXEDataObject results = null;
		
		String query = "SELECT SUM(QTY) AS QTY, SUM(GROSSWGT) AS GROSSWGT, SUM(NETWGT) AS NETWGT, " +
           		"SUM(TAREWGT) AS TAREWGT FROM LOTXLOCXID ";
						  
		String where = "WHERE STORERKEY = '" + storerKey + "' and SKU = '" + sku + "' AND LOC = '" + loc + "' ";
		
		if(lot != null && lot.trim().length() !=0)           
       	where = where + " and LOT = '" + lot + "' ";
       
       if(lpn != null && lpn.trim().length() !=0)
       	where = where + " and ID = '" + lpn + "' ";
       
       query = query + where;
		
		try{
		results = WmsWebuiValidationSelectImpl.select(query);		   
		}catch(Exception e){
		e.printStackTrace();
		}
		
		DataValue actqty=results.getAttribValue(new TextData("QTY"));  
		DataValue grossWgt=results.getAttribValue(new TextData("GROSSWGT"));
		DataValue netWgt=results.getAttribValue(new TextData("NETWGT"));
		DataValue tareWgt=results.getAttribValue(new TextData("TAREWGT"));  
		
		double stdTare1 = 0;
		double stdNet1 = 0;
		double stdGross1 = 0;
		double actualQty = 0;
		
		if(!grossWgt.getNull())
			stdGross1 = new Double(grossWgt.toString()).doubleValue();
		if(!netWgt.getNull())
			stdNet1 = new Double(netWgt.toString()).doubleValue();
		if(!tareWgt.getNull())
			stdTare1 = new Double(tareWgt.toString()).doubleValue();
		
		if(!actqty.getNull())
			actualQty = new Double(actqty.toString()).doubleValue();
		
		double gross = 0;
		double net = 0;
		double tare = 0;
		HashMap wgts = new HashMap();
		if(actualQty != 0)
		{
			gross = (stdGross1 / actualQty) * qty;
			net= (stdNet1 / actualQty) * qty;
			tare = (stdTare1 / actualQty) * qty;
			wgts.put("GROSSWEIGHT", new Double(gross));
			wgts.put("NETWEIGHT", new Double(net));
			wgts.put("TAREWEIGHT", new Double(tare));
		}
		else
		{
			try{
			wgts = getCalculatedWeightsLot(storerKey, sku, qty);
			}catch(Exception e){
			}
		}
		
		return wgts;
 }
   
   public HashMap getCalculatedWeightsLot( String storerKey, String sku, double qty, String qtyUOM, String packKey) throws EpiException { 
						
		EXEDataObject results = null;
		
		String query = "SELECT IBSUMCWFLG,PACKKEY, STDUOM, TAREWGT1, STDNETWGT1, STDGROSSWGT1 FROM SKU " +
						"WHERE STORERKEY = '" + storerKey + "' and SKU = '" + sku + "'";   
		try{
		results = WmsWebuiValidationSelectImpl.select(query);		   
		}catch(Exception e){
		e.printStackTrace();
		}
		
		DataValue skuPackKey=results.getAttribValue(new TextData("PACKKEY"));
		DataValue uom=results.getAttribValue(new TextData("STDUOM"));
		DataValue tareWgt=results.getAttribValue(new TextData("TAREWGT1"));   
		DataValue netWgt=results.getAttribValue(new TextData("STDNETWGT1"));
		DataValue grossWgt=results.getAttribValue(new TextData("STDGROSSWGT1"));
		
		double skuGross = 0;
		double skuNet = 0;
		double skuTare = 0;
		
		if(!tareWgt.getNull())
			skuTare= new Double(tareWgt.toString()).doubleValue();
		if(!netWgt.getNull())
			skuNet= new Double(netWgt.toString()).doubleValue();
		if(!grossWgt.getNull())
			skuGross= new Double(grossWgt.toString()).doubleValue();	   
		
		String stdUOM=uom.toString();
		if(stdUOM == null || "".equalsIgnoreCase(stdUOM)|| " ".equalsIgnoreCase(stdUOM)
				|| "N/A".equalsIgnoreCase(stdUOM)){//this item was not set up with advanced catch weight, then set stdUOM=EA
			stdUOM = "EA";
		}
		double skuPackQty = getPackQty(stdUOM, skuPackKey.toString());
		double packQty = getPackQty(qtyUOM, packKey);
		
		if(skuPackQty == 0)
		{
			// Throw exception as the STDUOM defined on SKU settings is invalid.
			String[] parameters = new String[1];
			throw new UserException("WMEXP_CC_INVALID_WGTS_UOM", parameters);
		}
		
		double gross = skuGross * ((qty * packQty) / skuPackQty);
		double net= skuNet * ((qty * packQty) / skuPackQty);
		double tare = skuTare * ((qty * packQty) / skuPackQty);   
		
		HashMap wgts = new HashMap();
		wgts.put("GROSSWEIGHT", new Double(gross));
		wgts.put("NETWEIGHT", new Double(net));
		wgts.put("TAREWEIGHT", new Double(tare));
		
		return wgts;
   }
 
   
   //defect 1224public HashMap getCalculatedWeightsByLotLocID( String lot, String loc, String id, double qty, String qtyUOM, String packKey) throws EpiException { 
	   public HashMap getCalculatedWeightsByLotLocID( String lot, String loc, String id, double qty, String qtyUOM, String packKey, UnitOfWorkBean uowb) throws EpiException {		//defect1224		
			EXEDataObject results = null;
			String qry = "SELECT STORERKEY, SKU,QTY, GROSSWGT, NETWGT, TAREWGT FROM LOTXLOCXID " +
						"WHERE LOT = '" + lot + "' AND LOC = '" + loc + "' AND ID='"+id+"'";		
			HashMap wgts = new HashMap();
			
			//if qty adjusted to 0, set all weights to 0
			if(Math.abs(qty)<0.000001){
				wgts.put("GROSSWEIGHT", new Double(0));
				wgts.put("NETWEIGHT", new Double(0));
				wgts.put("TAREWEIGHT", new Double(0));				
			}
			
			
					
			results = WmsWebuiValidationSelectImpl.select(qry);	
			DataValue storerKey = results.getAttribValue(new TextData("STORERKEY"));
			DataValue sku = results.getAttribValue(new TextData("SKU"));
			DataValue qtyObj=results.getAttribValue(new TextData("QTY"));   
			DataValue tareWeightObj=results.getAttribValue(new TextData("TAREWGT"));   
			DataValue netWeightObj=results.getAttribValue(new TextData("NETWGT"));
			DataValue grossWeightObj=results.getAttribValue(new TextData("GROSSWGT"));
			double grossWeight = 0;
			double netWeight = 0;
			double tareWeight = 0;
			double lotlocidQty = 0;
			if(qtyObj!=null){
				lotlocidQty = Double.parseDouble(qtyObj.toString());
			}
			if(grossWeightObj!=null){
				grossWeight = Double.parseDouble(grossWeightObj.toString());
			}
			if(netWeightObj!=null){
				netWeight = Double.parseDouble(netWeightObj.toString());
			}
			if(tareWeightObj!=null){
				tareWeight = Double.parseDouble(tareWeightObj.toString());
			}
			
			//getting data from sku table
			qry = "SELECT IBSUMCWFLG,PACKKEY, STDUOM, TAREWGT1, STDNETWGT1, STDGROSSWGT1 FROM SKU " +
			"WHERE STORERKEY = '" + storerKey + "' and SKU = '" + sku + "'"; 
			results = WmsWebuiValidationSelectImpl.select(qry);
			DataValue skuPackKey=results.getAttribValue(new TextData("PACKKEY"));
			DataValue uom=results.getAttribValue(new TextData("STDUOM"));
			DataValue tareWgt=results.getAttribValue(new TextData("TAREWGT1"));   
			DataValue netWgt=results.getAttribValue(new TextData("STDNETWGT1"));
			DataValue grossWgt=results.getAttribValue(new TextData("STDGROSSWGT1"));

			
			if(Math.abs(lotlocidQty)<0.000001){//it is 0, using standard to calculate
				
				double skuGross = 0;
				double skuNet = 0;
				double skuTare = 0;
				
				if(!tareWgt.getNull())
					skuTare= new Double(tareWgt.toString()).doubleValue();
				if(!netWgt.getNull())
					skuNet= new Double(netWgt.toString()).doubleValue();
				if(!grossWgt.getNull())
					skuGross= new Double(grossWgt.toString()).doubleValue();	   
				
				String stdUOM=uom.toString();
				//defect1224if(stdUOM == null || "".equalsIgnoreCase(stdUOM)|| " ".equalsIgnoreCase(stdUOM)){//this item was not set up with advanced catch weight, then set stdUOM=EA
				if(stdUOM == null || "".equalsIgnoreCase(stdUOM)|| " ".equalsIgnoreCase(stdUOM) || "N/A".equalsIgnoreCase(stdUOM)){		//defect1224
																																		//this item was not set up with advanced catch weight, then set stdUOM=EA
					stdUOM = UOMMappingUtil.getPACKUOM3Val(skuPackKey.toString(),uowb); 												//defect1224
				}
				
				double skuPackQty = getPackQty(stdUOM, skuPackKey.toString());
				double packQty = getPackQty(qtyUOM, packKey);
				
				if(skuPackQty == 0)
				{
					// Throw exception as the STDUOM defined on SKU settings is invalid.
					String[] parameters = new String[1];
					throw new UserException("WMEXP_CC_INVALID_WGTS_UOM", parameters);
				}
				
				double gross = skuGross * ((qty * packQty) / skuPackQty);
				double net= skuNet * ((qty * packQty) / skuPackQty);
				double tare = skuTare * ((qty * packQty) / skuPackQty);   
				wgts.put("GROSSWEIGHT", new Double(gross));
				wgts.put("NETWEIGHT", new Double(net));
				wgts.put("TAREWEIGHT", new Double(tare));
				
			}else{//using lotxlocxid to calculate
				
				String stdUOM=uom.toString();
				//this item was not set up with advanced catch weight, then set stdUOM=EA
//defect1224	if(stdUOM == null || "".equalsIgnoreCase(stdUOM)|| " ".equalsIgnoreCase(stdUOM)){//this item was not set up with advanced catch weight, then set stdUOM=EA
				if(stdUOM == null || "".equalsIgnoreCase(stdUOM)|| " ".equalsIgnoreCase(stdUOM)|| "N/A".equalsIgnoreCase(stdUOM)){		//defect1224
					stdUOM = UOMMappingUtil.getPACKUOM3Val(skuPackKey.toString(),uowb);				//defect1224
				}

				double skuPackQty = getPackQty(stdUOM, skuPackKey.toString());
				double packQty = getPackQty(qtyUOM, packKey);
				
				if(skuPackQty == 0)
				{
					// Throw exception as the STDUOM defined on SKU settings is invalid.
					String[] parameters = new String[1];
					throw new UserException("WMEXP_CC_INVALID_WGTS_UOM", parameters);
				}
				
				double gross = grossWeight * (qty * packQty) / (lotlocidQty*skuPackQty);
				double net= netWeight * (qty * packQty) / (lotlocidQty*skuPackQty);
				double tare = tareWeight * (qty * packQty) / (lotlocidQty*skuPackQty);   
				wgts.put("GROSSWEIGHT", new Double(gross));
				wgts.put("NETWEIGHT", new Double(net));
				wgts.put("TAREWEIGHT", new Double(tare));
								
			}
			return wgts;
  }

  
   
   
   public HashMap getCalculatedWeightsLPN( String storerKey, String sku, String lot, String loc, String lpn, double qty, String qtyUOM, String packKey) { 
		
		EXEDataObject results = null;
		
		String query = "SELECT SUM(QTY) AS QTY, SUM(GROSSWGT) AS GROSSWGT, SUM(NETWGT) AS NETWGT, " +
            		"SUM(TAREWGT) AS TAREWGT FROM LOTXLOCXID ";
						  
		String where = "WHERE STORERKEY = '" + storerKey + "' and SKU = '" + sku + "' AND LOC = '" + loc + "' ";
		
		if(lot != null && lot.trim().length() !=0)           
        	where = where + " and LOT = '" + lot + "' ";
        
        if(lpn != null && lpn.trim().length() !=0)
        	where = where + " and ID = '" + lpn + "' ";
        
        query = query + where;
		
		try{
		results = WmsWebuiValidationSelectImpl.select(query);		   
		}catch(Exception e){
		e.printStackTrace();
		}
		
		DataValue actqty=results.getAttribValue(new TextData("QTY"));  
		DataValue grossWgt=results.getAttribValue(new TextData("GROSSWGT"));
		DataValue netWgt=results.getAttribValue(new TextData("NETWGT"));
		DataValue tareWgt=results.getAttribValue(new TextData("TAREWGT"));  
		
		double stdTare1= new Double(tareWgt.toString()).doubleValue();
		double stdNet1= new Double(netWgt.toString()).doubleValue();
		double stdGross1= new Double(grossWgt.toString()).doubleValue();
		
		double actualQty= new Double(actqty.toString()).doubleValue();
		
		double gross = 0;
		double net = 0;
		double tare = 0;		
		double packQty = getPackQty(qtyUOM, packKey);
		HashMap wgts = new HashMap();
		if(actualQty != 0)
		{
			gross = (stdGross1 / actualQty) * (qty * packQty);
			net= (stdNet1 / actualQty) * (qty * packQty);
			tare = (stdTare1 / actualQty) * (qty * packQty);
			wgts.put("GROSSWEIGHT", new Double(gross));
			wgts.put("NETWEIGHT", new Double(net));
			wgts.put("TAREWEIGHT", new Double(tare));
		}
		else
		{
			try{
				wgts = getCalculatedWeightsLot(storerKey, sku, qty, qtyUOM, packKey);
				}catch(Exception e){
			}
		}
		
		
		
		
		return wgts;
  }
   
   
   
   public String isAdvCatchWeightEnabled(String storerKey, String sku)
   {
	   EXEDataObject results = null;	   
	   String enableAdvCatchWeight=null;
	   String query = "SELECT IBSUMCWFLG FROM SKU " +
							"WHERE STORERKEY = '" + storerKey + "' and SKU = '" + sku + "'";   
	   try{
		   results = WmsWebuiValidationSelectImpl.select(query);
		   DataValue enableAdvCatchWgt=results.getAttribValue(new TextData("IBSUMCWFLG"));
		   if(enableAdvCatchWgt != null)
			   enableAdvCatchWeight = enableAdvCatchWgt.toString();
	   }catch(Exception e){
		   e.printStackTrace();
	   }
	   
	   return enableAdvCatchWeight;
   }
   
   
   public boolean isEndToEndSerialInboundCatchWeight(String storerKey, String sku)
   {
	   EXEDataObject results = null;	   
	   String query = "SELECT ICWFLAG, SNUM_ENDTOEND FROM SKU " +
							"WHERE STORERKEY = '" + storerKey + "' and SKU = '" + sku + "'";   
	   try{
		   results = WmsWebuiValidationSelectImpl.select(query);
		   DataValue isInboundCatchWgt=results.getAttribValue(new TextData("ICWFLAG"));
		   DataValue isEndToEndSerialCatchWgt=results.getAttribValue(new TextData("SNUM_ENDTOEND"));
		   if(isInboundCatchWgt != null && isEndToEndSerialCatchWgt != null){
			   if(isEnabled.equalsIgnoreCase(isInboundCatchWgt.toString())
					   && isEnabled.equalsIgnoreCase(isEndToEndSerialCatchWgt.toString())){
				   return true;
			   }
			   
		   }
		   return false;
	   }catch(Exception e){
		   e.printStackTrace();
	   }
	   
	   return false;
   } 
   
   public HashMap getEnabledWeightInfo(String storerKey, String sku)
   {
	   EXEDataObject results = null;	   
	   HashMap enabledWeightInfo= new HashMap();
	   enabledWeightInfo.put("catchgrosswgt","0");
	   enabledWeightInfo.put("catchnetwgt","0");
	   enabledWeightInfo.put("catchtarewgt","0");
	   
	   String query = "SELECT catchgrosswgt,catchnetwgt,catchtarewgt  FROM SKU " +
							"WHERE STORERKEY = '" + storerKey + "' and SKU = '" + sku + "'";   
	   try{
		   results = WmsWebuiValidationSelectImpl.select(query);
		   
		   
		   if((results.getAttribValue(new TextData("catchgrosswgt"))!=null)&&(results.getAttribValue(new TextData("catchgrosswgt")).toString().equals("1")))	   
			   enabledWeightInfo.put("catchgrosswgt","1");
		   		   
		   if((results.getAttribValue(new TextData("catchnetwgt"))!=null)&&(results.getAttribValue(new TextData("catchnetwgt")).toString().equals("1")))	   
			   enabledWeightInfo.put("catchnetwgt","1");

		   if((results.getAttribValue(new TextData("catchtarewgt"))!=null)&&(results.getAttribValue(new TextData("catchtarewgt")).toString().equals("1")))
			   enabledWeightInfo.put("catchtarewgt","1");

		   
	   }catch(Exception e){
		   e.printStackTrace();
	   }
	   
	   return enabledWeightInfo;
	   
	   
   }
   
   public String getAdvCatchWeightTrackBy(String storerKey, String sku)
   {
	   EXEDataObject results = null;	   
	   String trackBy=null;
	   String query = "SELECT ADVCWTTRACKBY FROM SKU " +
							"WHERE STORERKEY = '" + storerKey + "' and SKU = '" + sku + "'";   
	   try{
		   results = WmsWebuiValidationSelectImpl.select(query);
		   DataValue advCatchWgtTrackBy=results.getAttribValue(new TextData("ADVCWTTRACKBY"));
		   trackBy = advCatchWgtTrackBy.toString();
	   }catch(Exception e){
		   e.printStackTrace();
	   }
	   
	   return trackBy;
   }
   
   public double getPackQty(String stdUOM, String packKey)
   {
	   
	   String packQtyColumn="";
	   
	   if(stdUOM.equalsIgnoreCase("EA"))
	   {
		   packQtyColumn="QTY";
	   }
	   else if(stdUOM.equalsIgnoreCase("CS"))
	   {
		   packQtyColumn="CASECNT";
	   }
	   else if(stdUOM.equalsIgnoreCase("IP"))
	   {
		   packQtyColumn="INNERPACK";
	   }
	   else if(stdUOM.equalsIgnoreCase("PL"))
	   {
		   packQtyColumn="PALLET";
	   }
	   else if(stdUOM.equalsIgnoreCase("CF"))
	   {		
		   packQtyColumn="CUBE";
	   }
	   else if(stdUOM.equalsIgnoreCase("LB"))
	   {		
		   packQtyColumn="GROSSWGT";
	   }
	   else if(stdUOM.equalsIgnoreCase("KG"))
	   {		
		   packQtyColumn="NETWGT";
	   }
	   else if(stdUOM.equalsIgnoreCase("IN"))
	   {		
		   packQtyColumn="OTHERUNIT1";
	   }
	   else if(stdUOM.equalsIgnoreCase("M"))
	   {		   
		   packQtyColumn="OTHERUNIT2";
	   }   
	   
	   
	   
	   
		//Krishna Kuchipudi: CatchWeight Enhancements - Starts
//defect1224 if(packQtyColumn.length()== 0){
		if(packQtyColumn.trim().length()== 0){		//defect1224
			String packUOMSerial = "PACKUOM";
			String finalPackUOMSerial = "";
			EXEDataObject results = null;
			for(int i=1;i<=9;i++){
				packUOMSerial = "PACKUOM"+i;
				try {
					String qry = "SELECT PACKKEY FROM PACK WHERE PACKKEY = '"+packKey+"' and "+packUOMSerial+" = '"+stdUOM+"'";
					results   =  WmsWebuiValidationSelectImpl.select(qry);	   
					int size  = results.getRowCount();
					if(size>=1){
						finalPackUOMSerial = "PACKUOM"+i;
					}
				} catch (DPException dpException) {
					dpException.printStackTrace();
				} 
			}//For loop ends


			if(finalPackUOMSerial.equalsIgnoreCase("PACKUOM3"))
			{
				packQtyColumn="QTY";
			}
			else if(finalPackUOMSerial.equalsIgnoreCase("PACKUOM2"))
			{
				packQtyColumn="INNERPACK";
			}
			else if(finalPackUOMSerial.equalsIgnoreCase("PACKUOM1"))
			{
				packQtyColumn="CASECNT";
			}

			else if(finalPackUOMSerial.equalsIgnoreCase("PACKUOM4"))
			{
				packQtyColumn="PALLET";
			}
			else if(finalPackUOMSerial.equalsIgnoreCase("PACKUOM5"))
			{		
				packQtyColumn="CUBE";
			}
			else if(finalPackUOMSerial.equalsIgnoreCase("PACKUOM6"))
			{		
				packQtyColumn="GROSSWGT ";
			}
			else if(finalPackUOMSerial.equalsIgnoreCase("PACKUOM7"))
			{		
				packQtyColumn="NETWGT";
			}
			else if(finalPackUOMSerial.equalsIgnoreCase("PACKUOM8"))
			{		
				packQtyColumn="OTHERUNIT1";
			}
			else if(finalPackUOMSerial.equalsIgnoreCase("PACKUOM9"))
			{		   
				packQtyColumn="OTHERUNIT2";
			} 
		}//if loop ends

		//Krishna Kuchipudi: CatchWeight Enhancements - Ends

	   
	   
	   
	   String packQuery = "SELECT "+packQtyColumn+" FROM PACK WHERE PACKKEY = '" + packKey + "'";  
	   double pQty = 0;
	   
	   try{
		   EXEDataObject results1 = WmsWebuiValidationSelectImpl.select(packQuery);	   
		   DataValue packQty = results1.getAttribValue(new TextData(packQtyColumn));
		   pQty = new Double(packQty.toString()).doubleValue();
	   }catch(Exception e){}
	   
	   return pQty;
   }
   
	public boolean isEmpty(Object attributeValue) {
		if (attributeValue == null)	{
			return true;
		} else if (attributeValue.toString().matches("\\s*")) {
			return true;
		} else {
			return false;
		}
	}
}
