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
package com.ssaglobal.scm.wms.util;

import java.util.Locale;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.UnitOfWork; //05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;//AW 03/25/2008 Machine#:2093019 SDIS:SCM-00000-05440
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;//AW 03/25/2008 Machine#:2093019 SDIS:SCM-00000-05440
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;//AW 03/25/2008 Machine#:2093019 SDIS:SCM-00000-05440
import com.agileitp.forte.framework.Array;//AW 03/25/2008 Machine#:2093019 SDIS:SCM-00000-05440
import com.agileitp.forte.framework.TextData;//AW 03/25/2008 Machine#:2093019 SDIS:SCM-00000-05440
import com.epiphany.shr.ui.model.data.BioCollectionBean;//AW 03/25/2008 Machine#:2093019 SDIS:SCM-00000-05440
import com.epiphany.shr.ui.state.StateInterface;//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
import com.ssaglobal.scm.wms.util.UOMConversion;//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530

/*
 * Modification history
 * 
 * 09/10/2008	AW	Initial version. Added in for SDIS:SCM-00000-05605 Machine:2111399 
 * 04/23/2009	AW		SDIS:SCM-00000-06871 Machine:2353530
 *						Changed code to allow qty values for the Currency Locale
 *						something other than dollar.
 * 
 */
public class UOMMappingUtil {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(UOMMappingUtil.class);
	
	
	public static String pack = "PACKKEY";		//AW 04/28/09
	public static String packBio = "wm_pack"; //AW 04/28/09
	public static final String PACK_STD = "STD"; //AW 03/25/2008 Machine#:2093019 SDIS:SCM-00000-05440
	public static final String UOM_EA = "6";//AW 03/25/2008 Machine#:2093019 SDIS:SCM-00000-05440
	public static final StateInterface stateNull = null;//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
	public static final UnitOfWork uowNull = null;//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
	
	public static String getUOMAttribute(String uomInput) {
		// TODO Auto-generated method stub
		   String uom= null;
		   
		   switch(Integer.parseInt(uomInput))
		   {
		   case 1:
			  uom = "PACKUOM4";
			  break;
		   case 2:
				  uom = "PACKUOM1";
				  break;
		   case 3:
				  uom = "PACKUOM2";
				  break;
		   case 4:
				  uom = "PACKUOM8";
				  break;
		   case 5:
				  uom = "PACKUOM9";
				  break;
		   case 6:
				  uom = "PACKUOM3";
				  break;
		   case 7:
				  uom = "PACKUOM3";
				  break;
		   case 8:
				  uom = "PACKUOM4";
				  break;
			default: break;	  
				  
		   }
		   
		return uom;
	}

	   public static String getUOM(String uomAttr, String packVal, UnitOfWorkBean uow) throws EpiException {
			// TODO Auto-generated method stub
			   String queryStatement = null;
			   Query query = null;
			   BioCollection results = null;
			   String UOM = null;
				try
				{
				queryStatement = packBio + "." +pack +"= '" + packVal +"'" ;							 
				query = new Query(packBio, queryStatement, null);
				results = uow.getBioCollectionBean(query);
				}catch (Exception e)
				{
				e.printStackTrace();
				throw new EpiException("WMEXP_UNASSIGNWORK_DELETE", "");
				}
				if(results.size() == 1)
				{
					   Bio resultBio = results.elementAt(0);
					   UOM = resultBio.get(uomAttr).toString();

				}
				else
				{
					return null;
				}
	   
			   
			   
			   
			return UOM;
		}

	   public static String getPACKUOM3Val(String packVal) 
	   {
		    String packuom3 = "";
	    	String stmt = "SELECT PACKUOM3 FROM PACK WHERE PACK.PACKKEY = '"+packVal+"' ";
			EXEDataObject coll = null;		
			try {
				coll = WmsWebuiValidationSelectImpl.select(stmt);		
				if(coll != null && coll.getRowCount() > 0){
					packuom3 = coll.getAttribValue(new TextData("PACKUOM3")).toString();
				}	
			} catch (DPException e2) {
			}	
			
			return packuom3;
	   }
	   
	   /**
	    * 09/26/2008	AW	Machine#:2093019 SDIS:SCM-00000-05440 
	    * 				Initial version
	    */
	   public static String getPACKUOM3Val(String packVal, UnitOfWorkBean uow) throws EpiException
	   {
		   String queryStatement = null;
		   Query query = null;
		   BioCollection results = null;
		   String UOM3 = null;
		   String PACKUOM3 = "PACKUOM3";
			try
			{
			queryStatement = packBio + "." +pack +"= '" + packVal +"'" ;							 
			query = new Query(packBio, queryStatement, null);
			results = uow.getBioCollectionBean(query);
			}catch (Exception e)
			{
			e.printStackTrace();
			throw new EpiException("WMEXP_UNASSIGNWORK_DELETE", "");
			}
			if(results.size() == 1)
			{
				   Bio resultBio = results.elementAt(0);
				   UOM3 = resultBio.get(PACKUOM3).toString();

			}
			else
			{
				return null;
			}
		   return UOM3;
	   }
	   
		/*
		 * 04/23/2009	AW		Modified method
		 * 						SDIS:SCM-00000-06871 Machine:2353530
		 *						Changed code to allow qty values for the Currency Locale
		 *						something other than dollar.
		 *						This method calls backend method for UOM conversion
		 *05/19/2009	AW		SDIS:SCM-00000-06871 Machine:2353530
		 *						The uom conversion will now be done in the front end.
		 *						UOMConversion.uomConversion method takes care of this.
		 *						Changes were made accordingly.  
	     *
		 */	
		public static String convertUOMQty(String fromuom, String touom, String unitQty, String packKey, StateInterface state, UnitOfWork uow, boolean useState) {
			
			Array parms = new Array(); 
			parms.add(new TextData(LocaleUtil.resetQtyToDecimalForBackend(unitQty)));	//from Qty
			parms.add(new TextData(fromuom));	//from UOM
			parms.add(new TextData(touom));		//to UOM
			parms.add(new TextData(packKey));	//Pack key		
			try {
				unitQty = UOMConversion.uomConversion(new TextData(fromuom),new TextData(touom),new TextData(LocaleUtil.resetQtyToDecimalForBackend(unitQty)),new TextData(packKey), state, uow, useState);
			}catch (Exception e) {
				e.printStackTrace();
			}
			return unitQty;
		}
		

		public static String isNull(BioCollectionBean list, String widgetName) throws EpiException{
			String result=null;
			if(list.size() > 0)
			{	
				if(result!=list.get("0").get(widgetName)){
				result=list.get("0").get(widgetName).toString();
				}
			}
			return result;
		}
		
		
		
		/*
		 * 04/23/2009	AW		Initial version
		 * 						SDIS:SCM-00000-06871 Machine:2353530
		 *						Changed code to allow qty values for the Currency Locale
		 *						something other than dollar.
		 *						Wrapper for doing UOM conversions and number formatting
	  	 * 05/19/2009	AW		SDIS:SCM-00000-06871 Machine:2353530
		 *						The uom conversion will now be done in the front end.
		 *						UOMConversion.uomConversion method takes care of this.
		 *						Changes were made accordingly.  
		 *03/22/2010	AW		Infor365:217417
	     *
		 */
		public static String numberFormaterConverter(Locale locale, String fromuom, String touom, String unitQty, String packKey, StateInterface state, UnitOfWork uow, boolean useState ) throws EpiException{
			String qtyApp = LocaleUtil.resetQtyToDecimalForBackend(unitQty);
			String qtyConverted = convertUOMQty(fromuom, touom, qtyApp, packKey, state, uow, useState);			
			return LocaleUtil.checkLocaleAndParseQty(qtyConverted, LocaleUtil.TYPE_QTY); //AW Infor365:217417 03/22/10
		
		}

		public static String getPACKUOM3Val(Bio packBio) {
			String packuom3 = "";
	    	packuom3 = BioUtil.getString(packBio, "PACKUOM3");
			
			return packuom3;
		}

		public static String numberFormaterConverter(Locale currencyLocale,
				String fromuom, String touom, String unitQty, String packKey,
				Bio packBio) {
			String qtyApp = LocaleUtil.resetQtyToDecimalForBackend(unitQty);
			String qtyConverted = convertUOMQty(fromuom, touom, qtyApp, packKey, packBio);
			return LocaleUtil.checkLocaleAndParseQty(qtyConverted, LocaleUtil.TYPE_QTY); 
		}

		public static String convertUOMQty(String fromuom, String touom,
				String unitQty, String packKey, Bio packBio) {
//			Array parms = new Array(); 
//			parms.add(new TextData(LocaleUtil.resetQtyToDecimalForBackend(unitQty)));	//from Qty
//			parms.add(new TextData(fromuom));	//from UOM
//			parms.add(new TextData(touom));		//to UOM
//			parms.add(new TextData(packKey));	//Pack key		
			try {
				unitQty = UOMConversion.uomConversion(new TextData(fromuom),new TextData(touom),new TextData(LocaleUtil.resetQtyToDecimalForBackend(unitQty)),new TextData(packKey), packBio);
			}catch (Exception e) {
				e.printStackTrace();
			}
			return unitQty;
		}
}
