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

public class DataLayerQueryList{
	public static final String QUERY_PARAMETER = "!WMS_QRY_PARAM_PLACEHOLDER!";
	//Storer Table Queries
	public static final DataLayerQueryData QUERY_STORER_BY_STORERKEY = new DataLayerQueryData (DataLayerTableList.TABLE_STORER,DataLayerTableList.TABLE_STORER+"."+DataLayerColumnList.COLUMN_STORER_STORERKEY+" = '"+QUERY_PARAMETER+"'",null);
	public static final DataLayerQueryData QUERY_STORER_BY_STORERKEY_AND_TYPE = new DataLayerQueryData (DataLayerTableList.TABLE_STORER,DataLayerTableList.TABLE_STORER+"."+DataLayerColumnList.COLUMN_STORER_STORERKEY+" = '"+QUERY_PARAMETER+"' AND "+DataLayerTableList.TABLE_STORER+"."+DataLayerColumnList.COLUMN_STORER_TYPE+" = '"+QUERY_PARAMETER+"'",null);
	
	//Pack Table Queries
	public static final DataLayerQueryData QUERY_PACK_BY_PACKKEY = new DataLayerQueryData (DataLayerTableList.TABLE_PACK,DataLayerTableList.TABLE_PACK+"."+DataLayerColumnList.COLUMN_PACK_PACKKEY+" = '"+QUERY_PARAMETER+"'",null);
	public static final DataLayerQueryData QUERY_PACK_BY_PACKKEY_PACKUOM1 = new DataLayerQueryData (DataLayerTableList.TABLE_PACK,DataLayerTableList.TABLE_PACK+"."+DataLayerColumnList.COLUMN_PACK_PACKKEY+" = '"+QUERY_PARAMETER+"' AND "+DataLayerTableList.TABLE_PACK+"."+DataLayerColumnList.COLUMN_PACK_PACKUOM1+" = '"+QUERY_PARAMETER+"'",null);
	public static final DataLayerQueryData QUERY_PACK_BY_PACKKEY_PACKUOM2 = new DataLayerQueryData (DataLayerTableList.TABLE_PACK,DataLayerTableList.TABLE_PACK+"."+DataLayerColumnList.COLUMN_PACK_PACKKEY+" = '"+QUERY_PARAMETER+"' AND "+DataLayerTableList.TABLE_PACK+"."+DataLayerColumnList.COLUMN_PACK_PACKUOM2+" = '"+QUERY_PARAMETER+"'",null);
	public static final DataLayerQueryData QUERY_PACK_BY_PACKKEY_PACKUOM3 = new DataLayerQueryData (DataLayerTableList.TABLE_PACK,DataLayerTableList.TABLE_PACK+"."+DataLayerColumnList.COLUMN_PACK_PACKKEY+" = '"+QUERY_PARAMETER+"' AND "+DataLayerTableList.TABLE_PACK+"."+DataLayerColumnList.COLUMN_PACK_PACKUOM3+" = '"+QUERY_PARAMETER+"'",null);
	public static final DataLayerQueryData QUERY_PACK_BY_PACKKEY_PACKUOM4 = new DataLayerQueryData (DataLayerTableList.TABLE_PACK,DataLayerTableList.TABLE_PACK+"."+DataLayerColumnList.COLUMN_PACK_PACKKEY+" = '"+QUERY_PARAMETER+"' AND "+DataLayerTableList.TABLE_PACK+"."+DataLayerColumnList.COLUMN_PACK_PACKUOM4+" = '"+QUERY_PARAMETER+"'",null);
	public static final DataLayerQueryData QUERY_PACK_BY_PACKKEY_PACKUOM5 = new DataLayerQueryData (DataLayerTableList.TABLE_PACK,DataLayerTableList.TABLE_PACK+"."+DataLayerColumnList.COLUMN_PACK_PACKKEY+" = '"+QUERY_PARAMETER+"' AND "+DataLayerTableList.TABLE_PACK+"."+DataLayerColumnList.COLUMN_PACK_PACKUOM5+" = '"+QUERY_PARAMETER+"'",null);
	public static final DataLayerQueryData QUERY_PACK_BY_PACKKEY_PACKUOM6 = new DataLayerQueryData (DataLayerTableList.TABLE_PACK,DataLayerTableList.TABLE_PACK+"."+DataLayerColumnList.COLUMN_PACK_PACKKEY+" = '"+QUERY_PARAMETER+"' AND "+DataLayerTableList.TABLE_PACK+"."+DataLayerColumnList.COLUMN_PACK_PACKUOM6+" = '"+QUERY_PARAMETER+"'",null);
	public static final DataLayerQueryData QUERY_PACK_BY_PACKKEY_PACKUOM7 = new DataLayerQueryData (DataLayerTableList.TABLE_PACK,DataLayerTableList.TABLE_PACK+"."+DataLayerColumnList.COLUMN_PACK_PACKKEY+" = '"+QUERY_PARAMETER+"' AND "+DataLayerTableList.TABLE_PACK+"."+DataLayerColumnList.COLUMN_PACK_PACKUOM7+" = '"+QUERY_PARAMETER+"'",null);
	public static final DataLayerQueryData QUERY_PACK_BY_PACKKEY_PACKUOM8 = new DataLayerQueryData (DataLayerTableList.TABLE_PACK,DataLayerTableList.TABLE_PACK+"."+DataLayerColumnList.COLUMN_PACK_PACKKEY+" = '"+QUERY_PARAMETER+"' AND "+DataLayerTableList.TABLE_PACK+"."+DataLayerColumnList.COLUMN_PACK_PACKUOM8+" = '"+QUERY_PARAMETER+"'",null);
	public static final DataLayerQueryData QUERY_PACK_BY_PACKKEY_PACKUOM9 = new DataLayerQueryData (DataLayerTableList.TABLE_PACK,DataLayerTableList.TABLE_PACK+"."+DataLayerColumnList.COLUMN_PACK_PACKKEY+" = '"+QUERY_PARAMETER+"' AND "+DataLayerTableList.TABLE_PACK+"."+DataLayerColumnList.COLUMN_PACK_PACKUOM9+" = '"+QUERY_PARAMETER+"'",null);
	
	
	//Item Table Queries
	public static final DataLayerQueryData QUERY_ITEM_BY_SKU = new DataLayerQueryData (DataLayerTableList.TABLE_ITEM,DataLayerTableList.TABLE_ITEM+"."+DataLayerColumnList.COLUMN_ITEM_SKU+" = '"+QUERY_PARAMETER+"'",null);
	public static final DataLayerQueryData QUERY_ITEM_BY_SKU_STORER = new DataLayerQueryData (DataLayerTableList.TABLE_ITEM,DataLayerTableList.TABLE_ITEM+"."+DataLayerColumnList.COLUMN_ITEM_SKU+" = '"+QUERY_PARAMETER+"' AND "+DataLayerTableList.TABLE_ITEM+"."+DataLayerColumnList.COLUMN_ITEM_STORER+" = '"+QUERY_PARAMETER+"'",null);
	
	//Hazmat Code Table Queries
	public static final DataLayerQueryData QUERY_HAZMAT_CODE_BY_HAZMATCODESKEY = new DataLayerQueryData (DataLayerTableList.TABLE_HAZMATCODES,DataLayerTableList.TABLE_HAZMATCODES+"."+DataLayerColumnList.COLUMN_HAZMATCODES_HAZMATCODESKEY+" = '"+QUERY_PARAMETER+"'",null);
	
	//Location Table Queries
	public static final DataLayerQueryData QUERY_LOCATION_BY_KEY = new DataLayerQueryData (DataLayerTableList.TABLE_LOCATION,DataLayerTableList.TABLE_LOCATION+"."+DataLayerColumnList.COLUMN_LOCATION_LOC+" = '"+QUERY_PARAMETER+"' ",null);
	public static final DataLayerQueryData QUERY_LOCATION_PICKTO_STAGED_BY_KEY = new DataLayerQueryData (DataLayerTableList.TABLE_LOCATION,DataLayerTableList.TABLE_LOCATION+"."+DataLayerColumnList.COLUMN_LOCATION_LOC+" = '"+QUERY_PARAMETER+"' AND "+DataLayerTableList.TABLE_LOCATION+"."+DataLayerColumnList.COLUMN_LOCATION_LOCATIONTYPE+"= 'PICKTO' OR "+DataLayerTableList.TABLE_LOCATION+"."+DataLayerColumnList.COLUMN_LOCATION_LOCATIONTYPE+"= 'STAGED'",null);
	public static final DataLayerQueryData QUERY_LOCATION_PUTAWAY_BY_KEY = new DataLayerQueryData (DataLayerTableList.TABLE_LOCATION,DataLayerTableList.TABLE_LOCATION+"."+DataLayerColumnList.COLUMN_LOCATION_LOC+" = '"+QUERY_PARAMETER+"' AND "+DataLayerTableList.TABLE_LOCATION+"."+DataLayerColumnList.COLUMN_LOCATION_LOCATIONTYPE+" != 'PICKTO' AND "+DataLayerTableList.TABLE_LOCATION+"."+DataLayerColumnList.COLUMN_LOCATION_LOCATIONTYPE+" != 'STAGED' ",null);
	
	//Sku X Loc Table Queries
	public static final DataLayerQueryData QUERY_SKU_X_LOCATION_BY_LOC_SKU_STORERKEY = new DataLayerQueryData (DataLayerTableList.TABLE_SKUXLOC,DataLayerTableList.TABLE_SKUXLOC+"."+DataLayerColumnList.COLUMN_SKUXLOC_LOC+" = '"+QUERY_PARAMETER+"' AND "+DataLayerTableList.TABLE_SKUXLOC+"."+DataLayerColumnList.COLUMN_SKUXLOC_SKU+" = '"+QUERY_PARAMETER+"' AND "+DataLayerTableList.TABLE_SKUXLOC+"."+DataLayerColumnList.COLUMN_SKUXLOC_STORERKEY+" = '"+QUERY_PARAMETER+"'",null);
	
	//AltSku Table Queries
	public static final DataLayerQueryData QUERY_ALTSKU_BY_ALTSKU_STORERKEY = new DataLayerQueryData (DataLayerTableList.TABLE_ALTSKU,DataLayerTableList.TABLE_ALTSKU+"."+DataLayerColumnList.COLUMN_ALTSKU_ALTSKU+" = '"+QUERY_PARAMETER+"' AND "+DataLayerTableList.TABLE_ALTSKU+"."+DataLayerColumnList.COLUMN_ALTSKU_STORERKEY+" = '"+QUERY_PARAMETER+"' ",null);
	public static final DataLayerQueryData QUERY_ALTSKU_BY_SERIALKEY = new DataLayerQueryData (DataLayerTableList.TABLE_ALTSKU,DataLayerTableList.TABLE_ALTSKU+"."+DataLayerColumnList.COLUMN_ALTSKU_SERIALKEY+" = '"+QUERY_PARAMETER+"' ",null);
	
	//SubstituteSku Table Queries
	public static final DataLayerQueryData QUERY_SUBSTITUTESKU_BY_SKU_STORERKEY_SUBSTITUTESKU = new DataLayerQueryData (DataLayerTableList.TABLE_SUBSTITUTESKU,DataLayerTableList.TABLE_SUBSTITUTESKU+"."+DataLayerColumnList.COLUMN_SUBSTITUTESKU_SKU+" = '"+QUERY_PARAMETER+"' AND "+DataLayerTableList.TABLE_SUBSTITUTESKU+"."+DataLayerColumnList.COLUMN_SUBSTITUTESKU_STORERKEY+" = '"+QUERY_PARAMETER+"' AND "+DataLayerTableList.TABLE_SUBSTITUTESKU+"."+DataLayerColumnList.COLUMN_SUBSTITUTESKU_SUBSTITUTESKU+" = '"+QUERY_PARAMETER+"'",null);
	
	//Codelkup Table Queries
	public static final DataLayerQueryData QUERY_CODELKUP_BY_LISTNAME_CODE = new DataLayerQueryData (DataLayerTableList.TABLE_CODELKUP,DataLayerTableList.TABLE_CODELKUP+"."+DataLayerColumnList.COLUMN_CODELKUP_LISTNAME+" = '"+QUERY_PARAMETER+"' AND "+DataLayerTableList.TABLE_CODELKUP+"."+DataLayerColumnList.COLUMN_CODELKUP_CODE+" = '"+QUERY_PARAMETER+"'",null);
	
	//Tariff Table Queries
	public static final DataLayerQueryData QUERY_TARIFF_BY_TARIFFEKY = new DataLayerQueryData (DataLayerTableList.TABLE_TARIFF,DataLayerTableList.TABLE_TARIFF+"."+DataLayerColumnList.COLUMN_TARIFF_TARIFFKEY+" = '"+QUERY_PARAMETER+"' ",null);
	
	//Cartonization Table Queries
	public static final DataLayerQueryData QUERY_CARTONIZATION_BY_CARTONIZATIONGROUP = new DataLayerQueryData (DataLayerTableList.TABLE_CARTONIZATION,DataLayerTableList.TABLE_CARTONIZATION+"."+DataLayerColumnList.COLUMN_CARTONIZATION_CARTONIZATIONGROUP+" = '"+QUERY_PARAMETER+"' ",null);
	
	//Lottable Validation Table Queries
	public static final DataLayerQueryData QUERY_LOTTABLEVALIDATION_BY_LOTTABLEVALIDATIONKEY = new DataLayerQueryData (DataLayerTableList.TABLE_LOTTABLEVALIDATION,DataLayerTableList.TABLE_LOTTABLEVALIDATION+"."+DataLayerColumnList.COLUMN_LOTTABLEVALIDATION_LOTTABLEVALIDATIONKEY+" = '"+QUERY_PARAMETER+"' ",null);
	
	//Inventory Hold Code Table Queries
	public static final DataLayerQueryData QUERY_INVENTORYHOLDCODE_BY_CODE = new DataLayerQueryData (DataLayerTableList.TABLE_INVENTORYHOLDCODE,DataLayerTableList.TABLE_INVENTORYHOLDCODE+"."+DataLayerColumnList.COLUMN_INVENTORYHOLDCODE_CODE+" = '"+QUERY_PARAMETER+"' ",null);
	
	//Receipt Validation Table Queries
	public static final DataLayerQueryData QUERY_RECEIPTVALIDATION_BY_RECEIPTVALIDATIONKEY = new DataLayerQueryData (DataLayerTableList.TABLE_RECEIPTVALIDATION,DataLayerTableList.TABLE_RECEIPTVALIDATION+"."+DataLayerColumnList.COLUMN_RECEIPTVALIDATION_RECEIPTVALIDATIONKEY+" = '"+QUERY_PARAMETER+"' ",null);
	
	//Putaway Zone Table Queries
	public static final DataLayerQueryData QUERY_PUTAWAYZONE_BY_PUTAWAYZONE = new DataLayerQueryData (DataLayerTableList.TABLE_PUTAWAYZONE,DataLayerTableList.TABLE_PUTAWAYZONE+"."+DataLayerColumnList.COLUMN_PUTAWAYZONE_PUTAWAYZONE+" = '"+QUERY_PARAMETER+"' ",null);
	
	//Putaway Strategy Table Queries
	public static final DataLayerQueryData QUERY_PUTAWAYSTRATEGY_BY_PUTAWAYSTRATEGYKEY = new DataLayerQueryData (DataLayerTableList.TABLE_PUTAWAYSTRATEGY,DataLayerTableList.TABLE_PUTAWAYSTRATEGY+"."+DataLayerColumnList.COLUMN_PUTAWAYSTRATEGY_PUTAWAYSTRATEGYKEY+" = '"+QUERY_PARAMETER+"' ",null);
	
	//Strategy Table Queries
	public static final DataLayerQueryData QUERY_STRATEGY_BY_STRATEGYKEY = new DataLayerQueryData (DataLayerTableList.TABLE_STRATEGY,DataLayerTableList.TABLE_STRATEGY+"."+DataLayerColumnList.COLUMN_STRATEGY_STRATEGYKEY+" = '"+QUERY_PARAMETER+"' ",null);
	
	//CCSetup Table Queries
	public static final DataLayerQueryData QUERY_CCSETUP_BY_CLASSID = new DataLayerQueryData (DataLayerTableList.TABLE_CCSETUP,DataLayerTableList.TABLE_CCSETUP+"."+DataLayerColumnList.COLUMN_CCSETUP_CLASSID+" = '"+QUERY_PARAMETER+"' ",null);
	
	//CCAdjRules Table Queries
	public static final DataLayerQueryData QUERY_CCADJRULES_BY_CCADJRULEKEY = new DataLayerQueryData (DataLayerTableList.TABLE_CCADJRULES,DataLayerTableList.TABLE_CCADJRULES+"."+DataLayerColumnList.COLUMN_CCADJRULES_CCADJRULEKEY+" = '"+QUERY_PARAMETER+"' ",null);
	
	//Equipmentprofile
	public static final DataLayerQueryData QUERY_EQUIPMENTPROFILE_BY_EQUIPMENTPROFILEKEY = new DataLayerQueryData(DataLayerTableList.TABLE_EQUIPMENTPROFILE,DataLayerTableList.TABLE_EQUIPMENTPROFILE+"."+DataLayerColumnList.COLUMN_EQUIPMENTPROFILE_EQUIPMENTPROFILEKEY+" = '"+QUERY_PARAMETER+"' ", null );
	
	//Pazoneequipmentexcludedetail
	public static final DataLayerQueryData QUERY_PAZONEEQUIPMENTEXCLUDEDETAIL_BY_PUTAWAYZONE_AND_EQUIPMENTPROFILEKEY = 
		new DataLayerQueryData(DataLayerTableList.TABLE_PAZONEEQUIPMENTEXCLUDEDETAIL,
				DataLayerTableList.TABLE_PAZONEEQUIPMENTEXCLUDEDETAIL+"."+DataLayerColumnList.COLUMN_PAZONEEQUIPMENTEXCLUDEDETAIL_PUTAWAYZONE+" = '"+QUERY_PARAMETER+"' AND "+DataLayerTableList.TABLE_PAZONEEQUIPMENTEXCLUDEDETAIL+"."+DataLayerColumnList.COLUMN_PAZONEEQUIPMENTEXCLUDEDETAIL_EQUIPMENTPROFILEKEY+" = '"+QUERY_PARAMETER+"'", null );
	

	//OppOrderStrategy Table Queries
	public static final DataLayerQueryData QUERY_OPPORDERSTRATEGY_BY_OPPORDERSTRATEGYKEY = 
		new DataLayerQueryData (DataLayerTableList.TABLE_OPPORDERSTRATEGY,DataLayerTableList.TABLE_OPPORDERSTRATEGY+"."+DataLayerColumnList.COLUMN_OPPORDERSTRATEGY_OPPORDERSTRATEGYKEY+" = '"+QUERY_PARAMETER+"' ",null);

	
	//Taxgroup key
	public static final DataLayerQueryData QUERY_TAXGROUP_BY_TAXGROUPKEY = 
		new DataLayerQueryData (DataLayerTableList.TABLE_TAXGROUP,DataLayerTableList.TABLE_TAXGROUP+"."+DataLayerColumnList.COLUMN_TAXGROUP_TAXGROUPKEY+" = '"+QUERY_PARAMETER+"' ",null);

	//Gldistribution Table Queries
	public static final DataLayerQueryData QUERY_GLDISTRIBUTION_BY_GLDISTRIBUTIONKEY = 
		new DataLayerQueryData (DataLayerTableList.TABLE_GLDISTRIBUTION,DataLayerTableList.TABLE_GLDISTRIBUTION+"."+DataLayerColumnList.COLUMN_GLDISTRIBUTION_GLDISTRIBUTIONKEY+" = '"+QUERY_PARAMETER+"' ",null);

	
	//Packing validation template Table Queries
	public static final DataLayerQueryData QUERY_PACKINGVALIDATIOINTEMPLATE_BY_PACKINGVALIDATIONTEMPLATEKEY = 
		new DataLayerQueryData (DataLayerTableList.TABLE_PACKINGVALIDATIONTEMPLATE,DataLayerTableList.TABLE_PACKINGVALIDATIONTEMPLATE+"."+DataLayerColumnList.COLUMN_PACKINGVALIDATIONTEMPLATE_PACKINGVALTEMPLATEKEY+" = '"+QUERY_PARAMETER+"' ",null);

	
	//Barcode config Table Queries
	public static final DataLayerQueryData QUERY_BARCODECONFIG_BY_BARCODECONFIGKEY = 
		new DataLayerQueryData (DataLayerTableList.TABLE_BARCODECONFIG,DataLayerTableList.TABLE_BARCODECONFIG+"."+DataLayerColumnList.COLUMN_BARCODECONFIG_BARCODECONFIGKEY+" = '"+QUERY_PARAMETER+"' ",null);

	//Purchase Order Queries
	public static final DataLayerQueryData QUERY_PURCHASEORDER_BY_POKEY = 
		new DataLayerQueryData (DataLayerTableList.TABLE_PURCHASEORDER,
				DataLayerTableList.TABLE_PURCHASEORDER+"."+DataLayerColumnList.COLUMN_PURCHASEORDER_POKEY+" = '"+QUERY_PARAMETER+"'",null);
	
	//Receipt Queries
	public static final DataLayerQueryData QUERY_RECEIPT_BY_RECEIPTKEY = 
		new DataLayerQueryData (DataLayerTableList.TABLE_RECEIPT,
				DataLayerTableList.TABLE_RECEIPT+"."+DataLayerColumnList.COLUMN_RECEIPT_RECEIPTKEY+" = '"+QUERY_PARAMETER+"'",null);

	//Receiptdetail Queries
	public static final DataLayerQueryData QUERY_RECEIPTDETAIL_BY_RECEIPTKEY_AND_LINENUMBER = 
		new DataLayerQueryData (DataLayerTableList.TABLE_RECEIPTDETAIL,
				DataLayerTableList.TABLE_RECEIPTDETAIL+"."+DataLayerColumnList.COLUMN_RECEIPTDETAIL_RECEIPTKEY+" = '"+QUERY_PARAMETER+"' AND " +
				DataLayerTableList.TABLE_RECEIPTDETAIL+"."+DataLayerColumnList.COLUMN_RECEIPTDETAIL_RECEIPTLINENUMBER+" = '"+QUERY_PARAMETER+"'  ",null);
	
	//SRG: 9.2 Upgrade -- Start
	//AMSTRATEGY Queries
	public static final DataLayerQueryData QUERY_AMSTRATEGY_BY_AMSTRATEGYKEY = new DataLayerQueryData (DataLayerTableList.TABLE_AMSTRATEGY,DataLayerTableList.TABLE_AMSTRATEGY+"."+DataLayerColumnList.COLUMN_AMSTRATEGY_AMSTRATEGYKEY+" = '"+QUERY_PARAMETER+"' ",null);
	//SRG: 9.2 Upgrade -- End

}