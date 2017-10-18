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
package com.infor.scm.wms.util.datalayer.driver;

import java.util.ArrayList;

import com.epiphany.shr.data.bio.HelperBio;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.wms.util.datalayer.WMSDataLayerException;
import com.infor.scm.wms.util.datalayer.resultwrappers.DataLayerResultWrapper;
import com.infor.scm.wms.util.validation.WMSValidationContext;
import com.infor.scm.wms.util.validation.screen.item.ItemScreenVO;

public class DataLayerInterface{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(DataLayerInterface.class);
	public static String DB_CONNECTION = "dbConnectionName";
	public static String DB_USERID =	"dbUserName";
	public static String DB_PASSWORD =	"dbPassword";
	public static String DB_DATABASE = "dbDatabase";
	public static String DB_ISENTERPRISE = "dbIsEnterprise";
	public static String DB_SERVER = "dbServer";
	public static String DB_TYPE = "dbType";
	public static String LOG_IN_USER_ID = "logInUserId";

	public static DataLayerResultWrapper getResult(DataLayerQueryData data, ArrayList parameters, WMSValidationContext context) throws WMSDataLayerException{				


		String bioQuery = addParamsToBioQuery(data.getBioQuery(), parameters);
		Query qry = new Query(data.getBioName(), bioQuery, data.getSortBy());

		return new DataLayerResultWrapper(new UnitOfWorkInterface().findByQuery(qry,context));

	}


	public static void insert(DataLayerUpdateData insertObject, WMSValidationContext context) throws WMSDataLayerException{		
		HelperBio bio = null;
		bio = new UnitOfWorkInterface().createHelperBioWithDefaults(insertObject.getBioName(),context);			
		bio = populateHelperBio(bio, insertObject);
		new UnitOfWorkInterface().saveHelperBio(bio, context);					

	}

	public static void insert(ArrayList insertObjects, WMSValidationContext context) throws WMSDataLayerException{	
		_log.debug("LOG_SYSTEM_OUT","\n\nEntering insert...\n\n",100L);
		ArrayList bios = new ArrayList();
		for(int i = 0; i < insertObjects.size(); i++){
			_log.debug("LOG_SYSTEM_OUT","\n\nIn Loop1\n\n",100L);
			HelperBio bio = null;
			DataLayerUpdateData insertObject = (DataLayerUpdateData)insertObjects.get(i);
			_log.debug("LOG_SYSTEM_OUT","\n\nIn Loop2\n\n",100L);
			bio = new UnitOfWorkInterface().createHelperBioWithDefaults(insertObject.getBioName(),context);
			_log.debug("LOG_SYSTEM_OUT","\n\nIn Loop3\n\n",100L);
			bio = populateHelperBio(bio, insertObject);
			_log.debug("LOG_SYSTEM_OUT","\n\nIn Loop4\n\n",100L);
			bios.add(bio);
		}
		_log.debug("LOG_SYSTEM_OUT","\n\nAbout to call save\n\n",100L);
		new UnitOfWorkInterface().saveHelperBios(bios, context);					

	}

	private static HelperBio populateHelperBio( HelperBio bio, DataLayerUpdateData insertObject) throws WMSDataLayerException{
		try {			
			if(insertObject.getBioName().equals(DataLayerTableList.TABLE_ITEM)){				
				ItemScreenVO itemScreen = (ItemScreenVO)insertObject.getScreenObject();	
				_log.debug("LOG_SYSTEM_OUT","\n\nCreating Helper Bio\n\n",100L);
				bio.set(DataLayerColumnList.COLUMN_ITEM_ABC, itemScreen.getCycleClass());
				bio.set(DataLayerColumnList.COLUMN_ITEM_ALLOWCONSOLIDATION, itemScreen.getAllowConsolidation());
				bio.set(DataLayerColumnList.COLUMN_ITEM_ALTSKU, itemScreen.getAltsku());
				bio.set(DataLayerColumnList.COLUMN_ITEM_AVGCASEWEIGHT, itemScreen.getInboundAverageWeight());
				bio.set(DataLayerColumnList.COLUMN_ITEM_BUSR1, itemScreen.getUdf6());
				bio.set(DataLayerColumnList.COLUMN_ITEM_BUSR2, itemScreen.getUdf7());
				bio.set(DataLayerColumnList.COLUMN_ITEM_BUSR3, itemScreen.getUdf8());
				bio.set(DataLayerColumnList.COLUMN_ITEM_BUSR4, itemScreen.getUdf9());
				bio.set(DataLayerColumnList.COLUMN_ITEM_BUSR5, itemScreen.getUdf10());
				bio.set(DataLayerColumnList.COLUMN_ITEM_BULKCARTONGROUP, itemScreen.getBulkCartonGroup());							
				bio.set(DataLayerColumnList.COLUMN_ITEM_CARRYCOST, itemScreen.getCarryingPerUnit());				
				bio.set(DataLayerColumnList.COLUMN_ITEM_CARTONGROUP, itemScreen.getCartonGroup());				
				bio.set(DataLayerColumnList.COLUMN_ITEM_CCDISCREPANCYRULE, itemScreen.getCcDiscrepancyHandlingRule());
				bio.set(DataLayerColumnList.COLUMN_ITEM_CLASS, itemScreen.getShippingTabClass());			
				bio.set(DataLayerColumnList.COLUMN_ITEM_CONVEYABLE, itemScreen.getConveyable());
				bio.set(DataLayerColumnList.COLUMN_ITEM_COST, itemScreen.getPuchasePricePerUnit());
				bio.set(DataLayerColumnList.COLUMN_ITEM_CWFLAG, itemScreen.getCatchWeight());
				bio.set(DataLayerColumnList.COLUMN_ITEM_CYCLECOUNTFREQUENCY, itemScreen.getCyclecountfrequency());
				bio.set(DataLayerColumnList.COLUMN_ITEM_DATECODEDAYS, itemScreen.getDateCodeDays());
				bio.set(DataLayerColumnList.COLUMN_ITEM_DEFAULTROTATION, itemScreen.getRotation());				
				bio.set(DataLayerColumnList.COLUMN_ITEM_DESCR, itemScreen.getDescription());
				bio.set(DataLayerColumnList.COLUMN_ITEM_EACHKEY, itemScreen.getEachkey());							
				bio.set(DataLayerColumnList.COLUMN_ITEM_EFFECSTARTDATE, itemScreen.getEffecstartdate());
				bio.set(DataLayerColumnList.COLUMN_ITEM_EFFECENDDATE, itemScreen.getEffecenddate());								
				bio.set(DataLayerColumnList.COLUMN_ITEM_FLOWTHRUITEM, itemScreen.getOppertunistic());				
				bio.set(DataLayerColumnList.COLUMN_ITEM_FREIGHTCLASS, itemScreen.getFreightClass());
				bio.set(DataLayerColumnList.COLUMN_ITEM_GUID, itemScreen.getGuid());
				bio.set(DataLayerColumnList.COLUMN_ITEM_HASIMAGE, itemScreen.getHasimage());
				bio.set(DataLayerColumnList.COLUMN_ITEM_HAZMATCODESKEY, itemScreen.getHazmatCode());				
				bio.set(DataLayerColumnList.COLUMN_ITEM_IOFLAG, itemScreen.getIoflag());
				bio.set(DataLayerColumnList.COLUMN_ITEM_ITEMREFERENCE, itemScreen.getItemReference());				
				bio.set(DataLayerColumnList.COLUMN_ITEM_ICDFLAG, itemScreen.getInboundCatchData());
				bio.set(DataLayerColumnList.COLUMN_ITEM_ICDLABEL1, itemScreen.getInboundSerialNumber());
				bio.set(DataLayerColumnList.COLUMN_ITEM_ICDLABEL2, itemScreen.getInboundOther2());
				bio.set(DataLayerColumnList.COLUMN_ITEM_ICDLABEL3, itemScreen.getInboundOther3());
				bio.set(DataLayerColumnList.COLUMN_ITEM_ICWBY, itemScreen.getInboundCatchWeightBy());
				bio.set(DataLayerColumnList.COLUMN_ITEM_ICWFLAG, itemScreen.getInboundCatchWeight()	);
				bio.set(DataLayerColumnList.COLUMN_ITEM_IDEWEIGHT, itemScreen.getInboundNoEntryOfTotalWeight());
				bio.set(DataLayerColumnList.COLUMN_ITEM_LASTCYCLECOUNT, itemScreen.getLastCycleCount());				
				bio.set(DataLayerColumnList.COLUMN_ITEM_LOTTABLE01LABEL, itemScreen.getLottable01());
				bio.set(DataLayerColumnList.COLUMN_ITEM_LOTTABLE02LABEL, itemScreen.getLottable02());
				bio.set(DataLayerColumnList.COLUMN_ITEM_LOTTABLE03LABEL, itemScreen.getLottable03());
				bio.set(DataLayerColumnList.COLUMN_ITEM_LOTTABLE04LABEL, itemScreen.getLottable04());
				bio.set(DataLayerColumnList.COLUMN_ITEM_LOTTABLE05LABEL, itemScreen.getLottable05());
				bio.set(DataLayerColumnList.COLUMN_ITEM_LOTTABLE06LABEL, itemScreen.getLottable06());
				bio.set(DataLayerColumnList.COLUMN_ITEM_LOTTABLE07LABEL, itemScreen.getLottable07());
				bio.set(DataLayerColumnList.COLUMN_ITEM_LOTTABLE08LABEL, itemScreen.getLottable08());
				bio.set(DataLayerColumnList.COLUMN_ITEM_LOTTABLE09LABEL, itemScreen.getLottable09());
				bio.set(DataLayerColumnList.COLUMN_ITEM_LOTTABLE10LABEL, itemScreen.getLottable10());
				bio.set(DataLayerColumnList.COLUMN_ITEM_LOTTABLEVALIDATIONKEY, itemScreen.getLottableValidation());			
				bio.set(DataLayerColumnList.COLUMN_ITEM_LOTXIDDETAILOTHERLABEL1, itemScreen.getLotxiddetailotherlabel1());
				bio.set(DataLayerColumnList.COLUMN_ITEM_LOTXIDDETAILOTHERLABEL2, itemScreen.getLotxiddetailotherlabel2());				
				bio.set(DataLayerColumnList.COLUMN_ITEM_LOTXIDDETAILOTHERLABEL3, itemScreen.getLotxiddetailotherlabel3());			
				bio.set(DataLayerColumnList.COLUMN_ITEM_MANUFACTURERSKU, itemScreen.getManufacturersku());				
				bio.set(DataLayerColumnList.COLUMN_ITEM_MINIMUMWAVEQTY, itemScreen.getMinimumWaveQuantity());			
				bio.set(DataLayerColumnList.COLUMN_ITEM_MANUALSETUPREQUIRED, itemScreen.getManualSetupRequired());
				bio.set(DataLayerColumnList.COLUMN_ITEM_MaxPalletsPerZone, itemScreen.getMaxPalletsPerZone());
				bio.set(DataLayerColumnList.COLUMN_ITEM_NETWGT, itemScreen.getNetWeight());
				bio.set(DataLayerColumnList.COLUMN_ITEM_NOTES1, itemScreen.getNotes());
				bio.set(DataLayerColumnList.COLUMN_ITEM_NOTES2, itemScreen.getPickingInstruction());
				bio.set(DataLayerColumnList.COLUMN_ITEM_ONRECEIPTCOPYPACKKEY, itemScreen.getOnReceiptCopyPack());
				bio.set(DataLayerColumnList.COLUMN_ITEM_OCDCATCHWHEN, itemScreen.getCatchWhen());
				bio.set(DataLayerColumnList.COLUMN_ITEM_OCDCATCHQTY1, itemScreen.getCatchQuantity1());
				bio.set(DataLayerColumnList.COLUMN_ITEM_OCDCATCHQTY2, itemScreen.getCatchQuantity2());
				bio.set(DataLayerColumnList.COLUMN_ITEM_OCDCATCHQTY3, itemScreen.getCatchQuantity3());				
				bio.set(DataLayerColumnList.COLUMN_ITEM_PACKKEY, itemScreen.getPack());				
				bio.set(DataLayerColumnList.COLUMN_ITEM_PRICE, itemScreen.getRetailPricePerUnit());			
				bio.set(DataLayerColumnList.COLUMN_ITEM_PUTAWAYLOC, itemScreen.getPutawayLocation());
				bio.set(DataLayerColumnList.COLUMN_ITEM_PUTAWAYZONE, itemScreen.getPutawayZone());
				bio.set(DataLayerColumnList.COLUMN_ITEM_PUTAWAYSTRATEGYKEY, itemScreen.getPutawayStrategy());				
				bio.set(DataLayerColumnList.COLUMN_ITEM_OAVGCASEWEIGHT, itemScreen.getOutboundAverageWeight());
				bio.set(DataLayerColumnList.COLUMN_ITEM_OACOVERRIDE, itemScreen.getAllowCustomerOverride());				
				bio.set(DataLayerColumnList.COLUMN_ITEM_QCLOC, itemScreen.getInboundQCLocation());				
				bio.set(DataLayerColumnList.COLUMN_ITEM_QCLOCOUT, itemScreen.getOutboundQCLocation());				
				bio.set(DataLayerColumnList.COLUMN_ITEM_OCWBY, itemScreen.getOutboundCatchWeightBy());
				bio.set(DataLayerColumnList.COLUMN_ITEM_OCWFLAG, itemScreen.getOutboundCatchWeight());
				bio.set(DataLayerColumnList.COLUMN_ITEM_OCDFLAG, itemScreen.getOutboundCatchData());
				bio.set(DataLayerColumnList.COLUMN_ITEM_OCDLABEL1, itemScreen.getOutboundSerialNumber());
				bio.set(DataLayerColumnList.COLUMN_ITEM_OCDLABEL2, itemScreen.getOutboundOther2());
				bio.set(DataLayerColumnList.COLUMN_ITEM_OCDLABEL3, itemScreen.getOutboundOther3());
				bio.set(DataLayerColumnList.COLUMN_ITEM_ODEWEIGHT, itemScreen.getOutboundNoEntryOfTotalWeight());
				bio.set(DataLayerColumnList.COLUMN_ITEM_OTAREWEIGHT, itemScreen.getOutboundWeightTabTareWeight());
				bio.set(DataLayerColumnList.COLUMN_ITEM_OTOLERANCEPCT, itemScreen.getOutboundTolerance());
				bio.set(DataLayerColumnList.COLUMN_ITEM_RECEIPTHOLDCODE, itemScreen.getHoldCodeOnRFReceipt());				
				bio.set(DataLayerColumnList.COLUMN_ITEM_RECEIPTVALIDATIONTEMPLATE, itemScreen.getReceiptValidation());
				bio.set(DataLayerColumnList.COLUMN_ITEM_RETURNSLOC, itemScreen.getReturnLocation());			
				bio.set(DataLayerColumnList.COLUMN_ITEM_REORDERPOINT, itemScreen.getReorderPoint());
				bio.set(DataLayerColumnList.COLUMN_ITEM_REORDERQTY, itemScreen.getQuantityToReorder());
				bio.set(DataLayerColumnList.COLUMN_ITEM_RFDEFAULTPACK, itemScreen.getRfDefaultReceivingPack());
				bio.set(DataLayerColumnList.COLUMN_ITEM_RFDEFAULTUOM, itemScreen.getRfDefaultReceivingUOM());
				bio.set(DataLayerColumnList.COLUMN_ITEM_STDORDERCOST, itemScreen.getCostToOrder());
				bio.set(DataLayerColumnList.COLUMN_ITEM_RETAILSKU, itemScreen.getRetailsku());				
				bio.set(DataLayerColumnList.COLUMN_ITEM_ROTATEBY, itemScreen.getRotateBy());				
				bio.set(DataLayerColumnList.COLUMN_ITEM_SERIALNUMBEREND, itemScreen.getSerialNumberEnd());
				bio.set(DataLayerColumnList.COLUMN_ITEM_SERIALNUMBERNEXT, itemScreen.getSerialNumberNext());
				bio.set(DataLayerColumnList.COLUMN_ITEM_SERIALNUMBERSTART, itemScreen.getSerialNumberStart());				
				bio.set(DataLayerColumnList.COLUMN_ITEM_SHELFLIFE, itemScreen.getInboundShelfLife());				
				bio.set(DataLayerColumnList.COLUMN_ITEM_SHELFLIFECODETYPE, itemScreen.getShelfLifeCodeType());
				bio.set(DataLayerColumnList.COLUMN_ITEM_SHELFLIFEONRECEIVING, itemScreen.getInboundShelfLife());
				bio.set(DataLayerColumnList.COLUMN_ITEM_SHIPPABLECONTAINER, itemScreen.getShippableContainer());
				bio.set(DataLayerColumnList.COLUMN_ITEM_SKU, itemScreen.getItem());
				bio.set(DataLayerColumnList.COLUMN_ITEM_STORER, itemScreen.getOwner());
				bio.set(DataLayerColumnList.COLUMN_ITEM_SKUGROUP, itemScreen.getItemGroup1());			
				bio.set(DataLayerColumnList.COLUMN_ITEM_SHELFLIFEINDICATOR, itemScreen.getShelfLifeIndicator());
				bio.set(DataLayerColumnList.COLUMN_ITEM_SKUGROUP2, itemScreen.getItemGroup2());				
				bio.set(DataLayerColumnList.COLUMN_ITEM_SKUTYPE, itemScreen.getItemType());
				bio.set(DataLayerColumnList.COLUMN_ITEM_StackLimit, itemScreen.getStackLimit());
				bio.set(DataLayerColumnList.COLUMN_ITEM_STDCUBE, itemScreen.getCube());
				bio.set(DataLayerColumnList.COLUMN_ITEM_STDGROSSWGT, itemScreen.getGrossWeight());
				bio.set(DataLayerColumnList.COLUMN_ITEM_STDNETWGT, itemScreen.getNetWeight());
				bio.set(DataLayerColumnList.COLUMN_ITEM_STRATEGYKEY, itemScreen.getStrategy());			
				bio.set(DataLayerColumnList.COLUMN_ITEM_SUSR1, itemScreen.getUdf1());
				bio.set(DataLayerColumnList.COLUMN_ITEM_SUSR2, itemScreen.getUdf2());
				bio.set(DataLayerColumnList.COLUMN_ITEM_SUSR3, itemScreen.getUdf3());
				bio.set(DataLayerColumnList.COLUMN_ITEM_SUSR4, itemScreen.getUdf4());
				bio.set(DataLayerColumnList.COLUMN_ITEM_SUSR5, itemScreen.getUdf5());				
				bio.set(DataLayerColumnList.COLUMN_ITEM_TARE, itemScreen.getTareWeight());			
				bio.set(DataLayerColumnList.COLUMN_ITEM_TAREWEIGHT, itemScreen.getInboundWeightTabTareWeight());				
				bio.set(DataLayerColumnList.COLUMN_ITEM_TARIFFKEY, itemScreen.getTariff());	
				bio.set(DataLayerColumnList.COLUMN_ITEM_TOLERANCEPCT, itemScreen.getInboundTolerance());
				bio.set(DataLayerColumnList.COLUMN_ITEM_TRANSPORTATIONMODE, itemScreen.getTransportationMode());
				bio.set(DataLayerColumnList.COLUMN_ITEM_TYPE, itemScreen.getType());
				bio.set(DataLayerColumnList.COLUMN_ITEM_VERT_STORAGE, itemScreen.getVerticalStorage());
				bio.set(DataLayerColumnList.COLUMN_ITEM_VERIFYLOT04LOT05, itemScreen.getVerifyLottable4and5());				
				bio.set(DataLayerColumnList.COLUMN_ITEM_WHSEID, itemScreen.getWhseid());					
				_log.debug("LOG_SYSTEM_OUT","\n\nDone Creating Helper Bio\n\n",100L);
			}						
		} catch (EpiDataException e) {
			throw new WMSDataLayerException(e);
		}		
		return bio;
	}
	
	private static String addParamsToBioQuery(String bioQuery, ArrayList parameters){

		for(int i = 0; i < parameters.size(); i++){
			bioQuery = bioQuery.replaceFirst(DataLayerQueryList.QUERY_PARAMETER, (String)parameters.get(i));
		}
		return bioQuery;
	}

}