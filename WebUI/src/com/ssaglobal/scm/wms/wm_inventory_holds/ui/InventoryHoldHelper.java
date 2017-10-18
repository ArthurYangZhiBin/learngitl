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
package com.ssaglobal.scm.wms.wm_inventory_holds.ui;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class InventoryHoldHelper {
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(InventoryHoldHelper.class);
	
	private static String BIOCLASS_INVENTORYHOLD = "wm_inventoryhold_bio";
	
	public String getInventoryHoldHold(UnitOfWorkBean uowb, String lotLocIdLabel, 
				String lotLocIdValue, String holdCode){
		String holdActive=null;
		try{
			//String whereClause = BIOCLASS_INVENTORYHOLD+".LOT ='" + lot + "'" +
			String whereClause = BIOCLASS_INVENTORYHOLD+"." + lotLocIdLabel +"='" + lotLocIdValue + "'" +
			" AND " + BIOCLASS_INVENTORYHOLD + ".STATUS='"+holdCode+"'"; 
			Query query = new Query(BIOCLASS_INVENTORYHOLD, whereClause, "" );
			BioCollectionBean collection = uowb.getBioCollectionBean(query);
			if(collection==null ||collection.size()==0){
				return null;
			}
			
			Bio inventoryHoldBio = collection.elementAt(0);
			if(inventoryHoldBio==null){
				return null;
			}else{
				holdActive=(String)inventoryHoldBio.get("HOLD");
			}
		}catch (EpiDataException e){
			e.printStackTrace();
		}
		return holdActive;
	}
	
	public void runInventoryHoldSP(String inventoryHoldKey, String lot, String loc, String id, String status, String hold, String comment)
	throws UserException {
		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		Array params = new Array();
		params.add(new TextData((String) inventoryHoldKey));
		params.add(new TextData((String) lot));
		params.add(new TextData((String) loc));
		params.add(new TextData((String) id));
		params.add(new TextData((String) status));
		params.add(new TextData((String) hold));
		//Krishna Kuchipudi - Aug-18-2010 - 3PL - Food Enhancements - Starts
		params.add(new TextData((String) comment));
		//Krishna Kuchipudi - Aug-18-2010 - 3PL - Food Enhancements - Ends
		actionProperties.setProcedureParameters(params);
		actionProperties.setProcedureName("nspInventoryHoldResultSet");
		//run stored procedure
		EXEDataObject holdResults = null;
		try
		{
			holdResults = WmsWebuiActionsImpl.doAction(actionProperties);
		} catch (WebuiException e)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "" + "CATCH BLOCK 1" + "\n", SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION", "" + e.getMessage() + "\n", SuggestedCategory.NONE);
			throw new UserException(e.getMessage(), new Object[] {});
		} 
		displayResults(holdResults);
	}
	
	public void displayResults(EXEDataObject results)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		pw.println("" + results.getRowCount() + " x " + results.getColumnCount() + "\n");
		if (results.getColumnCount() != 0)
		{
			
			pw.println("---Results");
			for (int i = 1; i < results.getColumnCount() + 1; i++)
			{
				try
				{
					pw.println(" " + i + " @ " + results.getAttribute(i).name + " "
							+ results.getAttribute(i).value.getAsString());
				} catch (Exception e)
				{
					pw.println(e.getMessage());
				}
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION", sw.toString(), SuggestedCategory.NONE);
	}
	
}
