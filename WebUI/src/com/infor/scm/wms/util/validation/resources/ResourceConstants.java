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
package com.infor.scm.wms.util.validation.resources;

import com.infor.scm.wms.util.validation.util.MessageUtil;

public class ResourceConstants{
	public static final String KEY_ERROR_VALUE_MUST_BE_GREATER_THAN_OR_EQUAL_ZERO = "generic.error.value.must.be.greater.than.or.equal.zero";
	public static final String KEY_ERROR_VALUE_MUST_BE_GREATER_THAN_ZERO = "generic.error.value.must.be.greater.than.zero";
	public static final String KEY_ERROR_VALUE_DOES_NOT_EXIST = "generic.error.value.does.not.exist";
	public static final String KEY_ERROR_VALUE_MUST_NOT_EXIST = "generic.error.value.must.not.exist";
	public static final String KEY_ERROR_NON_NUMERIC = "generic.error.non.numeric";
	public static final String KEY_ERROR_REQUIRED = "generic.error.field.required";
	public static final String KEY_ERROR_FIELD1_GREATER_THAN_FIELD2 = "generic.error.field1.greater.than.field2";
	public static final String KEY_ERROR_FIELD_LESS_THAN_VALUE = "generic.error.field.less.then.value";
	public static final String KEY_ERROR_LENGTH = "generic.error.length";
	public static final String KEY_ERROR_ATTR_DOM = "generic.error.attribute.domain";
	public static final String KEY_ERROR_VALUE_MUST_BE_BETWEEN_ZERO_AND_SEVEN = "generic.error.value.must.be.between.zero.and.seven";
	public static final String KEY_ERROR_VALUE_MUST_BE_BETWEEN_ZERO_AND_EIGHT = "generic.error.value.must.be.between.zero.and.eight";
	public static final String KEY_ERROR_NON_ALPHA_NUMERIC = "generic.error.non.alphanumeric";
	public static final String KEY_ERROR_NON_CURRENCY = "generic.error.non.currency";
	public static final String KEY_ERROR_NOT_ALL_NINE = "generic.error.not.all.nine";
	public static final String KEY_ERROR_LENGTH_NOT_COMPLIES = "generic.error.length.not.complies";
	public static final String KEY_ERROR_NOT_COMPLIES_FORMAT = "generic.error.not.complies.format";
	public static final String KEY_ERROR_MUST_BE_ZERO_OR_ONE = "generic.error.must.be.zero.or.one";
	
	public static final String KEY_ITEM_SCREEN_ERROR_DUPLICATE_ITEM = "item.screen.error.duplicate.item";
	public static final String KEY_ITEM_SCREEN_ERROR_CW_LABEL_EMPTY = "item.screen.error.cw.label.empty";
	public static final String KEY_ITEM_SCREEN_SNUM_POSITION_AND_SNUM_DELIMETER_CONFLICT = "item.screen.error.snumposition.snumdelimeter.conflict";
	public static final String KEY_ITEM_SCREEN_ERROR_SNUM_MASK_INVALID_FORMAT = "item.screen.error.snum.mask.invalid.format";
	public static final String KEY_ITEM_SCREEN_ERROR_SNUM_QUANTITY_GREATER_ZERO_WHEN_AUTOINCREMENT_ON = "item.screen.error.snum.quantity.greater.zero.when.autoincrement.on";
	public static final String KEY_ITEM_SCREEN_ERROR_SNUMLONG_FIXED_MUST_BE_GREATER_THAN_SNUM_LENGTH ="item.screen.error.snumlong.greater.than.snum.length";
	
	public static final String KEY_SCREEN_ITEM_FIELD_OWNER = "screen.item.field.owner";
	public static final String KEY_SCREEN_ITEM_FIELD_ITEM = "screen.item.field.item";
	public static final String KEY_SCREEN_ITEM_FIELD_DESCRIPTION = "screen.item.field.description";
	public static final String KEY_SCREEN_ITEM_FIELD_PACK = "screen.item.field.pack";
	public static final String KEY_SCREEN_ITEM_FIELD_CARTON_GROUP = "screen.item.field.carton.group";
	public static final String KEY_SCREEN_ITEM_FIELD_TARIFF = "screen.item.field.tariff";
	public static final String KEY_SCREEN_ITEM_FIELD_ITEM_REFERENCE = "screen.item.field.item.reference";
	public static final String KEY_SCREEN_ITEM_FIELD_CUBE = "screen.item.field.cube";
	public static final String KEY_SCREEN_ITEM_FIELD_GROSS_WEIGHT = "screen.item.field.gross.weight";
	public static final String KEY_SCREEN_ITEM_FIELD_NET_WEIGHT = "screen.item.field.net.weight";
	public static final String KEY_SCREEN_ITEM_FIELD_TARE_WEIGHT = "screen.item.field.tare.weight";
	public static final String KEY_SCREEN_ITEM_FIELD_CATCH_WEIGHT = "screen.item.field.catch.weight";
	public static final String KEY_SCREEN_ITEM_FIELD_INBOUND_SHELF_LIFE = "screen.item.field.inbound.shelf.life";
	public static final String KEY_SCREEN_ITEM_FIELD_OUTBOUND_SHELF_LIFE = "screen.item.field.outbound.shelf.life";
	public static final String KEY_SCREEN_ITEM_FIELD_SHELF_LIFE_CODE_TYPE = "screen.item.field.shelf.life.code.type";
	
	public static final String KEY_SCREEN_ITEM_FIELD_LOTTABLE_VALIDATION = "screen.item.field.lottable.validation";
	public static final String KEY_SCREEN_ITEM_FIELD_RF_DEFAULT_RECEIVING_PACK = "screen.item.field.rf.default.receiving.pack";
	public static final String KEY_SCREEN_ITEM_FIELD_ON_RECEIPT_COPY_PACK = "screen.item.field.on.receipt.copy.pack";
	public static final String KEY_SCREEN_ITEM_FIELD_RF_DEFAULT_RECEIVING_UOM = "screen.item.field.rf.default.receiving.uom";
	public static final String KEY_SCREEN_ITEM_FIELD_ITEM_GROUP_1 = "screen.item.field.item.group.1";
	public static final String KEY_SCREEN_ITEM_FIELD_ITEM_GROUP_2 = "screen.item.field.item.group.2";
	public static final String KEY_SCREEN_ITEM_FIELD_HAZMAT_CODE = "screen.item.field.hazmat.code";
	public static final String KEY_SCREEN_ITEM_FIELD_SHIPPABLE_CONTAINER = "screen.item.field.shippable.container";
	public static final String KEY_SCREEN_ITEM_FIELD_VERTICAL_STORAGE = "screen.item.field.vertical.storage";
	public static final String KEY_SCREEN_ITEM_FIELD_TRANSPORTATION_MODE = "screen.item.field.transportation.mode";
	public static final String KEY_SCREEN_ITEM_FIELD_FREIGHT_CLASS = "screen.item.field.freight.class";
	public static final String KEY_SCREEN_ITEM_FIELD_CLASS = "screen.item.field.class";
	public static final String KEY_SCREEN_ITEM_FIELD_LOTTABLE01 = "screen.item.field.lottable01";
	public static final String KEY_SCREEN_ITEM_FIELD_LOTTABLE02 = "screen.item.field.lottable02";
	public static final String KEY_SCREEN_ITEM_FIELD_LOTTABLE03 = "screen.item.field.lottable03";
	public static final String KEY_SCREEN_ITEM_FIELD_LOTTABLE04 = "screen.item.field.lottable04";
	public static final String KEY_SCREEN_ITEM_FIELD_LOTTABLE05 = "screen.item.field.lottable05";
	public static final String KEY_SCREEN_ITEM_FIELD_LOTTABLE06 = "screen.item.field.lottable06";
	public static final String KEY_SCREEN_ITEM_FIELD_LOTTABLE07 = "screen.item.field.lottable07";
	public static final String KEY_SCREEN_ITEM_FIELD_LOTTABLE08 = "screen.item.field.lottable08";
	public static final String KEY_SCREEN_ITEM_FIELD_LOTTABLE09 = "screen.item.field.lottable09";
	public static final String KEY_SCREEN_ITEM_FIELD_LOTTABLE10 = "screen.item.field.lottable10";
	
	public static final String KEY_SCREEN_ITEM_FIELD_LOTTABLE11 = "screen.item.field.lottable11";
	public static final String KEY_SCREEN_ITEM_FIELD_LOTTABLE12 = "screen.item.field.lottable12";
	
	public static final String KEY_SCREEN_ITEM_FIELD_BARCODE01 = "screen.item.field.barcode01";
	public static final String KEY_SCREEN_ITEM_FIELD_BARCODE02 = "screen.item.field.barcode02";
	public static final String KEY_SCREEN_ITEM_FIELD_BARCODE03 = "screen.item.field.barcode03";
	public static final String KEY_SCREEN_ITEM_FIELD_BARCODE04 = "screen.item.field.barcode04";
	public static final String KEY_SCREEN_ITEM_FIELD_BARCODE05 = "screen.item.field.barcode05";
	public static final String KEY_SCREEN_ITEM_FIELD_BARCODE06 = "screen.item.field.barcode06";
	public static final String KEY_SCREEN_ITEM_FIELD_BARCODE07 = "screen.item.field.barcode07";
	public static final String KEY_SCREEN_ITEM_FIELD_BARCODE08 = "screen.item.field.barcode08";
	public static final String KEY_SCREEN_ITEM_FIELD_BARCODE09 = "screen.item.field.barcode09";
	public static final String KEY_SCREEN_ITEM_FIELD_BARCODE010 = "screen.item.field.barcode10";
	
	//SRG -- Catch Weight Capture -- Begin
	public static final String KEY_SCREEN_ITEM_FIELD_ENABLEADVCWGT = "screen.item.field.enableadvcwgt";
	public static final String KEY_SCREEN_ITEM_FIELD_CATCHGROSSWGT = "screen.item.field.catchgrosswgt";
	public static final String KEY_SCREEN_ITEM_FIELD_CATCHNETWGT = "screen.item.field.catchnetwgt";
	public static final String KEY_SCREEN_ITEM_FIELD_CATCHTAREWGT = "screen.item.field.catchtarewgt";
	public static final String KEY_SCREEN_ITEM_FIELD_ZERODEFAULTWGTFORPICK = "screen.item.field.zerodefaultwgtforpick";
//	public static final String KEY_SCREEN_ITEM_FIELD_ADVCWTTRACKBY = "screen.item.field.advcwttrackby";
	public static final String KEY_SCREEN_ITEM_FIELD_TAREWGT1 = "screen.item.field.tarewgt1";
	public static final String KEY_SCREEN_ITEM_FIELD_STDNETWGT1 = "screen.item.field.stdnetwgt1";
	public static final String KEY_SCREEN_ITEM_FIELD_STDGROSSWGT1 = "screen.item.field.stdgrosswgt1";
	public static final String KEY_SCREEN_ITEM_FIELD_STDUOM = "screen.item.field.stduom";
	//SRG -- Catch Weight Capture -- End
	
	public static final String KEY_SCREEN_ITEM_FIELD_INBOUND_CATCH_WEIGHT = "screen.item.field.inbound.catch.weight";
	public static final String KEY_SCREEN_ITEM_FIELD_INBOUND_NO_ENTRY_OF_TOTAL_WEIGHT = "screen.item.field.inbound.no.entry.of.total.weight";
	public static final String KEY_SCREEN_ITEM_FIELD_INBOUND_AVERAGE_WEIGHT = "screen.item.field.inbound.average.weight";
	public static final String KEY_SCREEN_ITEM_FIELD_INBOUND_TARE_WEIGHT = "screen.item.field.inbound.tare.weight";
	public static final String KEY_SCREEN_ITEM_FIELD_INBOUND_TOLERANCE = "screen.item.field.inbound.tolerance";
	public static final String KEY_SCREEN_ITEM_FIELD_INBOUND_CATCH_WEIGHT_BY = "screen.item.field.inbound.catch.weight.by";
	public static final String KEY_SCREEN_ITEM_FIELD_INBOUND_CATCH_DATA = "screen.item.field.inbound.catch.data";
	public static final String KEY_SCREEN_ITEM_FIELD_INBOUND_SERIAL_NUMBER = "screen.item.field.inbound.serial.number";
	public static final String KEY_SCREEN_ITEM_FIELD_INBOUND_OTHER_2 = "screen.item.field.inbound.other.2";
	public static final String KEY_SCREEN_ITEM_FIELD_INBOUND_OTHER_3 = "screen.item.field.inbound.other.3";
	public static final String KEY_SCREEN_ITEM_FIELD_OUTBOUND_CATCH_WEIGHT = "screen.item.field.outbound.catch.weight";
	public static final String KEY_SCREEN_ITEM_FIELD_OUTBOUND_CATCH_DATA = "screen.item.field.outbound.catch.data";
	public static final String KEY_SCREEN_ITEM_FIELD_OUTBOUND_NO_ENTRY_OF_TOTAL_WEIGHT = "screen.item.field.outbound.catch.no.entry.of.total.weight";
	public static final String KEY_SCREEN_ITEM_FIELD_OUTBOUND_AVERAGE_WEIGHT = "screen.item.field.outbound.average.weight";
	public static final String KEY_SCREEN_ITEM_FIELD_OUTBOUND_TARE_WEIGHT = "screen.item.field.outbound.tare.weight";
	public static final String KEY_SCREEN_ITEM_FIELD_OUTBOUND_TOLERANCE = "screen.item.field.outbound.tolerance";
	public static final String KEY_SCREEN_ITEM_FIELD_OUTBOUND_CATCH_WEIGHT_BY = "screen.item.field.outbound.catch.weight.by";
	public static final String KEY_SCREEN_ITEM_FIELD_OUTBOUND_SERIAL_NUMBER = "screen.item.field.outbound.serial.number";
	public static final String KEY_SCREEN_ITEM_FIELD_OUTBOUND_OTHER_2 = "screen.item.field.outbound.other.2";
	public static final String KEY_SCREEN_ITEM_FIELD_OUTBOUND_OTHER_3 = "screen.item.field.outbound.other.3";
	public static final String KEY_SCREEN_ITEM_FIELD_ALLOW_CUSTOMER_OVERRIDE = "screen.item.field.allow.customer.override";
	public static final String KEY_SCREEN_ITEM_FIELD_CATCH_WHEN = "screen.item.field.catch.when";
	public static final String KEY_SCREEN_ITEM_FIELD_CATCH_QTY1 = "screen.item.field.catch.qty.1";
	public static final String KEY_SCREEN_ITEM_FIELD_CATCH_QTY2 = "screen.item.field.catch.qty.2";
	public static final String KEY_SCREEN_ITEM_FIELD_CATCH_QTY3 = "screen.item.field.catch.qty.3";
	
	public static final String KEY_SCREEN_ITEM_FIELD_HOLD_CODE_ON_RF_RECEIPT = "screen.item.field.hold.code.on.rf.receipt";
	public static final String KEY_SCREEN_ITEM_FIELD_ITEM_TYPE = "screen.item.field.item.type";
	public static final String KEY_SCREEN_ITEM_FIELD_RECEIPT_VALIDATION = "screen.item.field.receipt.validation";
	public static final String KEY_SCREEN_ITEM_FIELD_STACK_LIMIT = "screen.item.field.stack.limit";
	public static final String KEY_SCREEN_ITEM_FIELD_MAX_PALLETS_PER_ZONE = "screen.item.field.max.pallets.per.zone";
	public static final String KEY_SCREEN_ITEM_FIELD_MANUAL_SETUP_REQUIRED = "screen.item.field.manual.setup.required";
	public static final String KEY_SCREEN_ITEM_FIELD_PUTAWAY_ZONE = "screen.item.field.putaway.zone";
	public static final String KEY_SCREEN_ITEM_FIELD_PUTAWAY_LOCATION = "screen.item.field.putaway.location";
	public static final String KEY_SCREEN_ITEM_FIELD_INBOUND_QC_LOCATION = "screen.item.field.inbound.qc.location";
	public static final String KEY_SCREEN_ITEM_FIELD_OUTBOUND_QC_LOCATION = "screen.item.field.outbound.qc.location";
	public static final String KEY_SCREEN_ITEM_FIELD_RETURN_LOCATION = "screen.item.field.return.location";
	public static final String KEY_SCREEN_ITEM_FIELD_PUTAWAY_STATEGY = "screen.item.field.putaway.strategy";
	public static final String KEY_SCREEN_ITEM_FIELD_STATEGY = "screen.item.field.strategy";
	public static final String KEY_SCREEN_ITEM_FIELD_ROTATION = "screen.item.field.rotation";
	public static final String KEY_SCREEN_ITEM_FIELD_ROTATE_BY = "screen.item.field.rotate.by";
	public static final String KEY_SCREEN_ITEM_FIELD_DATE_CODE_DAYS = "screen.item.field.date.code.days";
	public static final String KEY_SCREEN_ITEM_FIELD_SERIAL_NUMBER_START = "screen.item.field.serial.number.start";
	public static final String KEY_SCREEN_ITEM_FIELD_SERIAL_NUMBER_NEXT = "screen.item.field.serial.number.next";
	public static final String KEY_SCREEN_ITEM_FIELD_SERIAL_NUMBER_END = "screen.item.field.serial.number.end";
	public static final String KEY_SCREEN_ITEM_FIELD_OPPORTUNISTIC = "screen.item.field.oppertunistic";
	public static final String KEY_SCREEN_ITEM_FIELD_CONVEYABLE = "screen.item.field.conveyable";
	public static final String KEY_SCREEN_ITEM_FIELD_VERIFY_LOTTABLE_4_5 = "screen.item.field.verify.lottable.4.5";
	public static final String KEY_SCREEN_ITEM_FIELD_MINIMUM_WAVE_QTY = "screen.item.field.minimum.wave.qty";
	public static final String KEY_SCREEN_ITEM_FIELD_BULK_CARTON_GROUP = "screen.item.field.bulk.carton.group";
	public static final String KEY_SCREEN_ITEM_FIELD_ALLOW_CONSOLIDATION = "screen.item.field.allow.consolidation";
	
	public static final String KEY_SCREEN_ITEM_FIELD_CYCLE_CLASS = "screen.item.field.cycle.class";
	public static final String KEY_SCREEN_ITEM_FIELD_LAST_CYCLE_COUNT = "screen.item.field.last.cycle.count";
	public static final String KEY_SCREEN_ITEM_FIELD_CC_DESCREPENCY_HANDLING_RULE = "screen.item.field.cc.discrepancy.handling.rule";
	public static final String KEY_SCREEN_ITEM_FIELD_QTY_TO_REORDER = "screen.item.field.quantity.to.reorder";
	public static final String KEY_SCREEN_ITEM_FIELD_COST_TO_ORDER = "screen.item.field.cost.to.order";
	public static final String KEY_SCREEN_ITEM_FIELD_PURCHASE_PRICE_PER_UNIT = "screen.item.field.purchase.price.per.unit";
	public static final String KEY_SCREEN_ITEM_FIELD_REORDER_POINT = "screen.item.field.reorder.point";
	public static final String KEY_SCREEN_ITEM_FIELD_RETAIL_PRICE_PER_UNIT = "screen.item.field.retail.price.per.unit";
	public static final String KEY_SCREEN_ITEM_FIELD_CARRYING_PER_UNIT = "screen.item.field.carrying.per.unit";
	//jp begin - 91EE

	public static final String KEY_SCREEN_ITEM_FIELD_SNUM_AUTOINCREMENT = "screen.item.field.snum.autoincrement";
	public static final String KEY_SCREEN_ITEM_FIELD_SNUM_DELIM_COUNT = "screen.item.field.snum.delim.count";
	public static final String KEY_SCREEN_ITEM_FIELD_SNUM_DELIMETER = "screen.item.field.snum.delimeter";
	public static final String KEY_SCREEN_ITEM_FIELD_SNUM_ENDTOEND = "screen.item.field.snum.endtoend";
	public static final String KEY_SCREEN_ITEM_FIELD_SNUM_INCR_LENGTH = "screen.item.field.snum.incr.length";
	public static final String KEY_SCREEN_ITEM_FIELD_SNUM_INCR_POS = "screen.item.field.snum.incr.pos";
	public static final String KEY_SCREEN_ITEM_FIELD_SNUM_LENGTH = "screen.item.field.snum.length";
	public static final String KEY_SCREEN_ITEM_FIELD_SNUM_MASK = "screen.item.field.snum.mask";
	public static final String KEY_SCREEN_ITEM_FIELD_SNUM_POSITION = "screen.item.field.snum.position";
	public static final String KEY_SCREEN_ITEM_FIELD_SNUM_QUANTITY = "screen.item.field.snum.quantity";
	public static final String KEY_SCREEN_ITEM_FIELD_SNUMLONG_DELIMETER = "screen.item.field.snumlong.delimeter";
	public static final String KEY_SCREEN_ITEM_FIELD_SNUMLONG_FIXED = "screen.item.field.snumlong.fixed";
	public static final String KEY_SCREEN_ITEM_FIELD_TOEXPIREDAYS = "screen.item.field.toexpiredays";
	public static final String KEY_SCREEN_ITEM_FIELD_TODELIVERBYDAYS = "screen.item.field.todeliverbydays";
	public static final String KEY_SCREEN_ITEM_FIELD_TOBESTBYDAYS = "screen.item.field.tobestbydays";
	public static final String KEY_SCREEN_ITEM_FIELD_NONSTOCKEDINDICATOR = "screen.item.field.nonstockedindicator";
	public static final String KEY_SCREEN_ITEM_FIELD_ICD1UNIQUE = "screen.item.field.icd1unique";
	public static final String KEY_SCREEN_ITEM_FIELD_OCD1UNIQUE = "screen.item.field.ocd1unique";
	public static final String KEY_SCREEN_ITEM_FIELD_CWFLAG = "screen.item.field.cwflag";
	//jp end - 91EE
	public static final String KEY_SCREEN_ITEM_UDF1 = "screen.item.field.udf1";
	public static final String KEY_SCREEN_ITEM_UDF2 = "screen.item.field.udf2";
	public static final String KEY_SCREEN_ITEM_UDF3 = "screen.item.field.udf3";
	public static final String KEY_SCREEN_ITEM_UDF4 = "screen.item.field.udf4";
	public static final String KEY_SCREEN_ITEM_UDF5 = "screen.item.field.udf5";
	public static final String KEY_SCREEN_ITEM_UDF6 = "screen.item.field.udf6";
	public static final String KEY_SCREEN_ITEM_UDF7 = "screen.item.field.udf7";
	public static final String KEY_SCREEN_ITEM_UDF8 = "screen.item.field.udf8";
	public static final String KEY_SCREEN_ITEM_UDF9 = "screen.item.field.udf9";
	public static final String KEY_SCREEN_ITEM_UDF10 = "screen.item.field.udf10";
	public static final String KEY_SCREEN_ITEM_PICKING_INSTRUCTION = "screen.item.field.picking.instruction";
	public static final String KEY_SCREEN_ITEM_NOTES = "screen.item.field.notes";
	
	public static final String KEY_SCREEN_ITEM_FIELD_VOICEGROUPINGID = "screen.item.field.voicegroupingid";
	public static final String KEY_SCREEN_ITEM_FIELD_COUNTSEQUENCE = "screen.item.field.countsequence";
	
	
	//Assign Location
	public static final String KEY_ITEM_ASSIGN_LOC_SCREEN_ERROR_DUPLICATE_ASSIGN_LOC = "item.screen.error.duplicate.assign.loc";
	public static final String KEY_SCREEN_ITEM_ASSIGN_LOC_LOCATION_TYPE = "screen.item.assign.loc.field.location.type";
	public static final String KEY_SCREEN_ITEM_ASSIGN_LOC_LOCATION = "screen.item.assign.loc.field.location";
	public static final String KEY_SCREEN_ITEM_ASSIGN_LOC_REPLENISHMENT_PRIORITY = "screen.item.assign.loc.field.replenishment.priority";
	public static final String KEY_SCREEN_ITEM_ASSIGN_LOC_REPLENISHMENT_OVERRIDE = "screen.item.assign.loc.field.replenishment.override";
	public static final String KEY_SCREEN_ITEM_ASSIGN_LOC_MINIMUM_CAPACITY = "screen.item.assign.loc.field.minimum.capacity";
	public static final String KEY_SCREEN_ITEM_ASSIGN_LOC_MAXIMUM_CAPACITY = "screen.item.assign.loc.field.maximum.capacity";
	public static final String KEY_SCREEN_ITEM_ASSIGN_LOC_MINIMUM_REPLENISHMENT_UOM = "screen.item.assign.loc.field.minimum.replenishment.uom";
	public static final String KEY_SCREEN_ITEM_ASSIGN_LOC_UOM_QTY_TO_BE_REPLENISHED = "screen.item.assign.loc.field.uom.qty.to.be.replenished";
	public static final String KEY_SCREEN_ITEM_ASSIGN_LOC_ALLOW_REPLENISHMENT_FROM_CASE = "screen.item.assign.loc.field.allow.replenishment.from.case";
	public static final String KEY_SCREEN_ITEM_ASSIGN_LOC_ALLOW_REPLENISHMENT_FROM_BULK = "screen.item.assign.loc.field.allow.replenishment.from.bulk";
	public static final String KEY_SCREEN_ITEM_ASSIGN_LOC_ALLOW_REPLENISHMENT_FROM_PIECE = "screen.item.assign.loc.field.allow.replenishment.from.piece";
	
	
	//Alternate Item
	public static final String KEY_ITEM_ALT_ITEM_SCREEN_ERROR_DUPLICATE_ALT_ITEM = "item.screen.error.duplicate.alt.item";
	public static final String KEY_SCREEN_ITEM_ALT_ITEM_OWNER = "screen.item.alt.item.field.owner";
	public static final String KEY_SCREEN_ITEM_ALT_ITEM_ITEM = "screen.item.alt.item.field.item";
	public static final String KEY_SCREEN_ITEM_ALT_ITEM_ALTERNATE_ITEM = "screen.item.alt.item.field.alternate.item";
	public static final String KEY_SCREEN_ITEM_ALT_ITEM_PACK = "screen.item.alt.item.field.pack";
	public static final String KEY_SCREEN_ITEM_ALT_ITEM_VENDOR = "screen.item.alt.item.field.vendor";
	public static final String KEY_SCREEN_ITEM_ALT_ITEM_DEFAULT_RECEIVING_UOM = "screen.item.alt.item.field.default.receiving.uom";
	public static final String KEY_SCREEN_ITEM_ALT_ITEM_TYPE = "screen.item.alt.item.field.type";
	public static final String KEY_SCREEN_ITEM_ALT_ITEM_UDF1 = "screen.item.alt.item.field.udf1";
	public static final String KEY_SCREEN_ITEM_ALT_ITEM_UDF2 = "screen.item.alt.item.field.udf2";
	public static final String KEY_SCREEN_ITEM_ALT_ITEM_UDF3 = "screen.item.alt.item.field.udf3";
	public static final String KEY_SCREEN_ITEM_ALT_ITEM_UDF4 = "screen.item.alt.item.field.udf4";
	public static final String KEY_SCREEN_ITEM_ALT_ITEM_UDF5 = "screen.item.alt.item.field.udf5";
	
	public static final String KEY_SCREEN_ITEM_SUB_ITEM_OWNER = "screen.item.sub.item.field.owner";
	public static final String KEY_SCREEN_ITEM_SUB_ITEM_ITEM = "screen.item.sub.item.field.item";
	public static final String KEY_SCREEN_ITEM_SUB_ITEM_PACK = "screen.item.sub.item.field.item.pack";
	public static final String KEY_SCREEN_ITEM_SUB_ITEM_UNITS = "screen.item.sub.item.field.item.units";
	public static final String KEY_SCREEN_ITEM_SUB_SUBSTITUTE_ITEM = "screen.item.sub.item.field.item.item";
	public static final String KEY_SCREEN_ITEM_SUB_SUBSTITUTE_PACK = "screen.item.sub.item.field.item.pack";
	public static final String KEY_SCREEN_ITEM_SUB_SUBSTITUTE_UNITS = "screen.item.sub.item.field.item.units";
	
	
	//Pack fields
	public static final String KEY_SCREEN_PACK_FIELD_CASECNT = "screen.pack.field.casecnt";
	public static final String KEY_SCREEN_PACK_FIELD_CUBE = "screen.pack.field.cube";
	public static final String KEY_SCREEN_PACK_FIELD_CUBEUOM1 = "screen.pack.field.cubeuom1";
	public static final String KEY_SCREEN_PACK_FIELD_CUBEUOM2 = "screen.pack.field.cubeuom2";
	public static final String KEY_SCREEN_PACK_FIELD_CUBEUOM3 = "screen.pack.field.cubeuom3";
	public static final String KEY_SCREEN_PACK_FIELD_CUBEUOM4 = "screen.pack.field.cubeuom4";
	public static final String KEY_SCREEN_PACK_FIELD_FILTERVALUEUOM1 = "screen.pack.field.filtervalueuom1";
	public static final String KEY_SCREEN_PACK_FIELD_FILTERVALUEUOM2 = "screen.pack.field.filtervalueuom2";
	public static final String KEY_SCREEN_PACK_FIELD_FILTERVALUEUOM3 = "screen.pack.field.filtervalueuom3";
	public static final String KEY_SCREEN_PACK_FIELD_FILTERVALUEUOM4 = "screen.pack.field.filtervalueuom4";
	public static final String KEY_SCREEN_PACK_FIELD_FILTERVALUEUOM5 = "screen.pack.field.filtervalueuom5";
	public static final String KEY_SCREEN_PACK_FIELD_FILTERVALUEUOM6 = "screen.pack.field.filtervalueuom6";
	public static final String KEY_SCREEN_PACK_FIELD_FILTERVALUEUOM7 = "screen.pack.field.filtervalueuom7";
	public static final String KEY_SCREEN_PACK_FIELD_FILTERVALUEUOM8 = "screen.pack.field.filtervalueuom8";
	public static final String KEY_SCREEN_PACK_FIELD_FILTERVALUEUOM9 = "screen.pack.field.filtervalueuom9";
	public static final String KEY_SCREEN_PACK_FIELD_GROSSWGT = "screen.pack.field.grosswgt";
	public static final String KEY_SCREEN_PACK_FIELD_HEIGHTUOM1 = "screen.pack.field.heightuom1";
	public static final String KEY_SCREEN_PACK_FIELD_HEIGHTUOM2 = "screen.pack.field.heightuom2";
	public static final String KEY_SCREEN_PACK_FIELD_HEIGHTUOM3 = "screen.pack.field.heightuom3";
	public static final String KEY_SCREEN_PACK_FIELD_HEIGHTUOM4 = "screen.pack.field.heightuom4";
	public static final String KEY_SCREEN_PACK_FIELD_HEIGHTUOM8 = "screen.pack.field.heightuom8";
	public static final String KEY_SCREEN_PACK_FIELD_HEIGHTUOM9 = "screen.pack.field.heightuom9";
	public static final String KEY_SCREEN_PACK_FIELD_INDICATORDIGITUOM1 = "screen.pack.field.indicatordigituom1";
	public static final String KEY_SCREEN_PACK_FIELD_INDICATORDIGITUOM2 = "screen.pack.field.indicatordigituom2";
	public static final String KEY_SCREEN_PACK_FIELD_INDICATORDIGITUOM3 = "screen.pack.field.indicatordigituom3";
	public static final String KEY_SCREEN_PACK_FIELD_INDICATORDIGITUOM4 = "screen.pack.field.indicatordigituom4";
	public static final String KEY_SCREEN_PACK_FIELD_INDICATORDIGITUOM5 = "screen.pack.field.indicatordigituom5";
	public static final String KEY_SCREEN_PACK_FIELD_INDICATORDIGITUOM6 = "screen.pack.field.indicatordigituom6";
	public static final String KEY_SCREEN_PACK_FIELD_INDICATORDIGITUOM7 = "screen.pack.field.indicatordigituom7";
	public static final String KEY_SCREEN_PACK_FIELD_INDICATORDIGITUOM8 = "screen.pack.field.indicatordigituom8";
	public static final String KEY_SCREEN_PACK_FIELD_INDICATORDIGITUOM9 = "screen.pack.field.indicatordigituom9";
	public static final String KEY_SCREEN_PACK_FIELD_INNERPACK = "screen.pack.field.innerpack";
	public static final String KEY_SCREEN_PACK_FIELD_LENGTHUOM1 = "screen.pack.field.lengthuom1";
	public static final String KEY_SCREEN_PACK_FIELD_LENGTHUOM2 = "screen.pack.field.lengthuom2";
	public static final String KEY_SCREEN_PACK_FIELD_LENGTHUOM3 = "screen.pack.field.lengthuom3";
	public static final String KEY_SCREEN_PACK_FIELD_LENGTHUOM4 = "screen.pack.field.lengthuom4";
	public static final String KEY_SCREEN_PACK_FIELD_LENGTHUOM8 = "screen.pack.field.lengthuom8";
	public static final String KEY_SCREEN_PACK_FIELD_LENGTHUOM9 = "screen.pack.field.lengthuom9";
	public static final String KEY_SCREEN_PACK_FIELD_NETWGT = "screen.pack.field.netwgt";
	public static final String KEY_SCREEN_PACK_FIELD_OTHERUNIT1 = "screen.pack.field.otherunit1";
	public static final String KEY_SCREEN_PACK_FIELD_OTHERUNIT2 = "screen.pack.field.otherunit2";
	public static final String KEY_SCREEN_PACK_FIELD_PALLET = "screen.pack.field.pallet";
	public static final String KEY_SCREEN_PACK_FIELD_PALLETTI = "screen.pack.field.palletti";
	public static final String KEY_SCREEN_PACK_FIELD_PALLETHI = "screen.pack.field.pallethi";
	public static final String KEY_SCREEN_PACK_FIELD_PALLETWOODHEIGHT = "screen.pack.field.palletwoodheight";
	public static final String KEY_SCREEN_PACK_FIELD_PALLETWOODLENGTH = "screen.pack.field.palletwoodlength";
	public static final String KEY_SCREEN_PACK_FIELD_PALLETWOODWIDTH = "screen.pack.field.palletwoodwidth";
	public static final String KEY_SCREEN_PACK_FIELD_QTY = "screen.pack.field.qty";
	public static final String KEY_SCREEN_PACK_FIELD_WIDTHUOM1 = "screen.pack.field.widthuom1";
	public static final String KEY_SCREEN_PACK_FIELD_WIDTHUOM2 = "screen.pack.field.widthuom2";
	public static final String KEY_SCREEN_PACK_FIELD_WIDTHUOM3 = "screen.pack.field.widthuom3";
	public static final String KEY_SCREEN_PACK_FIELD_WIDTHUOM4 = "screen.pack.field.widthuom4";
	public static final String KEY_SCREEN_PACK_FIELD_WIDTHUOM8 = "screen.pack.field.widthuom8";
	public static final String KEY_SCREEN_PACK_FIELD_WIDTHUOM9 = "screen.pack.field.widthuom9";
	public static final String KEY_SCREEN_PACK_FIELD_PACKKEY = "screen.pack.field.packkey";
	public static final String KEY_SCREEN_PACK_FIELD_PACKUOM1 = "screen.pack.field.packuom1";
	public static final String KEY_SCREEN_PACK_FIELD_PACKUOM2 = "screen.pack.field.packuom2";
	public static final String KEY_SCREEN_PACK_FIELD_PACKUOM3 = "screen.pack.field.packuom3";
	public static final String KEY_SCREEN_PACK_FIELD_PACKUOM4 = "screen.pack.field.packuom4";
	public static final String KEY_SCREEN_PACK_FIELD_PACKUOM5 = "screen.pack.field.packuom5";
	public static final String KEY_SCREEN_PACK_FIELD_PACKUOM6 = "screen.pack.field.packuom6";
	public static final String KEY_SCREEN_PACK_FIELD_PACKUOM7 = "screen.pack.field.packuom7";
	public static final String KEY_SCREEN_PACK_FIELD_PACKUOM8 = "screen.pack.field.packuom8";
	public static final String KEY_SCREEN_PACK_FIELD_PACKUOM9 = "screen.pack.field.packuom9";
	public static final String KEY_SCREEN_PACK_FIELD_CARTONIZE1 = "screen.pack.field.cartonize1";
	public static final String KEY_SCREEN_PACK_FIELD_CARTONIZE2 = "screen.pack.field.cartonize2";
	public static final String KEY_SCREEN_PACK_FIELD_CARTONIZE3 = "screen.pack.field.cartonize3";
	public static final String KEY_SCREEN_PACK_FIELD_CARTONIZE4 = "screen.pack.field.cartonize4";	
	public static final String KEY_SCREEN_PACK_FIELD_CARTONIZE8 = "screen.pack.field.cartonize8";
	public static final String KEY_SCREEN_PACK_FIELD_CARTONIZE9 = "screen.pack.field.cartonize9";
	public static final String KEY_SCREEN_PACK_FIELD_ISWHQTY1 = "screen.pack.field.iswhqty1";
	public static final String KEY_SCREEN_PACK_FIELD_ISWHQTY2 = "screen.pack.field.iswhqty2";
	public static final String KEY_SCREEN_PACK_FIELD_ISWHQTY3 = "screen.pack.field.iswhqty3";
	public static final String KEY_SCREEN_PACK_FIELD_ISWHQTY4 = "screen.pack.field.iswhqty4";
	public static final String KEY_SCREEN_PACK_FIELD_ISWHQTY5 = "screen.pack.field.iswhqty5";
	public static final String KEY_SCREEN_PACK_FIELD_ISWHQTY6 = "screen.pack.field.iswhqty6";
	public static final String KEY_SCREEN_PACK_FIELD_ISWHQTY7 = "screen.pack.field.iswhqty7";
	public static final String KEY_SCREEN_PACK_FIELD_ISWHQTY8 = "screen.pack.field.iswhqty8";
	public static final String KEY_SCREEN_PACK_FIELD_ISWHQTY9 = "screen.pack.field.iswhqty9";
	public static final String KEY_SCREEN_PACK_FIELD_PACKDESCR = "screen.pack.field.packdescr";
	public static final String KEY_SCREEN_PACK_FIELD_REPLENISHUOM1 = "screen.pack.field.replenishuom1";
	public static final String KEY_SCREEN_PACK_FIELD_REPLENISHUOM2 = "screen.pack.field.replenishuom2";
	public static final String KEY_SCREEN_PACK_FIELD_REPLENISHUOM3 = "screen.pack.field.replenishuom3";
	public static final String KEY_SCREEN_PACK_FIELD_REPLENISHUOM4 = "screen.pack.field.replenishuom4";
	public static final String KEY_SCREEN_PACK_FIELD_REPLENISHUOM8 = "screen.pack.field.replenishuom8";
	public static final String KEY_SCREEN_PACK_FIELD_REPLENISHUOM9 = "screen.pack.field.replenishuom9";
	public static final String KEY_SCREEN_PACK_FIELD_REPLENISHZONE1 = "screen.pack.field.replenishzone1";
	public static final String KEY_SCREEN_PACK_FIELD_REPLENISHZONE2 = "screen.pack.field.replenishzone2";
	public static final String KEY_SCREEN_PACK_FIELD_REPLENISHZONE3 = "screen.pack.field.replenishzone3";
	public static final String KEY_SCREEN_PACK_FIELD_REPLENISHZONE4 = "screen.pack.field.replenishzone4";
	public static final String KEY_SCREEN_PACK_FIELD_REPLENISHZONE8 = "screen.pack.field.replenishzone8";
	public static final String KEY_SCREEN_PACK_FIELD_REPLENISHZONE9 = "screen.pack.field.replenishzone9";
	
	public static final String KEY_PACK_SCREEN_ERROR_DUPLICATE_PACKKEY = "pack.screen.error.duplicate.pack";
	public static final String KEY_PACK_SCREEN_ERROR_DUPLICATE_PACKUOM = "pack.screen.error.duplicate.packuom";
	
	
	
	//Zone screen
	public static final String KEY_SCREEN_ZONE_FIELD_AISLEEND = "screen.zone.field.aisleend";
	public static final String KEY_SCREEN_ZONE_FIELD_AISLESTART = "screen.zone.field.aislestart";
	public static final String KEY_SCREEN_ZONE_FIELD_CLEAN_LOCATION = "screen.zone.field.clean_location";
	public static final String KEY_SCREEN_ZONE_FIELD_CREATEASSIGNMENTS = "screen.zone.field.createassignments";
	public static final String KEY_SCREEN_ZONE_FIELD_DESCR = "screen.zone.field.descr";
	public static final String KEY_SCREEN_ZONE_FIELD_INLOC = "screen.zone.field.inloc";
	public static final String KEY_SCREEN_ZONE_FIELD_LABORMAXCASECNT = "screen.zone.field.labormaxcasecnt";
	public static final String KEY_SCREEN_ZONE_FIELD_LABORMAXCUBE = "screen.zone.field.labormaxcube";
	public static final String KEY_SCREEN_ZONE_FIELD_LABORMAXWEIGHT = "screen.zone.field.labormaxweight";
	public static final String KEY_SCREEN_ZONE_FIELD_MAXCASECNT = "screen.zone.field.maxcasecnt";
	public static final String KEY_SCREEN_ZONE_FIELD_MAXCUBE = "screen.zone.field.maxcube";
	public static final String KEY_SCREEN_ZONE_FIELD_MAXPALLETSPERSKU = "screen.zone.field.maxpalletspersku";
	public static final String KEY_SCREEN_ZONE_FIELD_MAXPICKINGCONTAINERS = "screen.zone.field.maxpickingcontainers";
	public static final String KEY_SCREEN_ZONE_FIELD_MAXPICKLINES = "screen.zone.field.maxpicklines";
	public static final String KEY_SCREEN_ZONE_FIELD_MAXWEIGHT = "screen.zone.field.maxweight";
	public static final String KEY_SCREEN_ZONE_FIELD_OUTLOC = "screen.zone.field.outloc";
	public static final String KEY_SCREEN_ZONE_FIELD_PICKCC = "screen.zone.field.pickcc";
	public static final String KEY_SCREEN_ZONE_FIELD_PICKTOLOC = "screen.zone.field.picktoloc";
	public static final String KEY_SCREEN_ZONE_FIELD_POSVERMETHOD = "screen.zone.field.posvermethod";
	public static final String KEY_SCREEN_ZONE_FIELD_PUTAWAYZONE = "screen.zone.field.putawayzone";
	public static final String KEY_SCREEN_ZONE_FIELD_QTYCC = "screen.zone.field.qtycc";
	public static final String KEY_SCREEN_ZONE_FIELD_REPLENISHMENT_FLAG = "screen.zone.field.replenishment_flag";
	public static final String KEY_SCREEN_ZONE_FIELD_REPLENISHMENT_HOTLEVEL = "screen.zone.field.replenishment_hotlevel";
	public static final String KEY_SCREEN_ZONE_FIELD_REPLENISHMENTMETHOD = "screen.zone.field.replenishmentmethod";
	public static final String KEY_SCREEN_ZONE_FIELD_SERIALKEY = "screen.zone.field.serialkey";
	public static final String KEY_SCREEN_ZONE_FIELD_SLOTEND = "screen.zone.field.slotend";
	public static final String KEY_SCREEN_ZONE_FIELD_SLOTSTART = "screen.zone.field.slotstart";
	public static final String KEY_SCREEN_ZONE_FIELD_TOP_OFF = "screen.zone.field.top_off";
	public static final String KEY_SCREEN_ZONE_FIELD_UOM1PICKMETHOD = "screen.zone.field.uom1pickmethod";
	public static final String KEY_SCREEN_ZONE_FIELD_UOM2PICKMETHOD = "screen.zone.field.uom2pickmethod";
	public static final String KEY_SCREEN_ZONE_FIELD_UOM3PICKMETHOD = "screen.zone.field.uom3pickmethod";
	public static final String KEY_SCREEN_ZONE_FIELD_UOM4PICKMETHOD = "screen.zone.field.uom4pickmethod";
	public static final String KEY_SCREEN_ZONE_FIELD_UOM5PICKMETHOD = "screen.zone.field.uom5pickmethod";
	public static final String KEY_SCREEN_ZONE_FIELD_UOM6PICKMETHOD = "screen.zone.field.uom6pickmethod";
	public static final String KEY_SCREEN_ZONE_FIELD_WHSEID = "screen.zone.field.whseid";
	public static final String KEY_SCREEN_ZONE_FIELD_ZONEBREAK = "screen.zone.field.zonebreak";
	
	public static final String KEY_SCREEN_ZONE_FIELD_AUTOPRINTADDRLABEL = "screen.zone.field.autoprintaddrlabel";
	public static final String KEY_SCREEN_ZONE_FIELD_AUTOPRINTCARTONCONTENT = "screen.zone.field.autoprintcartoncontent";
	public static final String KEY_SCREEN_ZONE_FIELD_DEFAULTLABELPRINTER = "screen.zone.field.defaultlabelprinter";
	public static final String KEY_SCREEN_ZONE_FIELD_DEFAULTREPORTPRINTER = "screen.zone.field.defaultreportprinter";
	public static final String KEY_SCREEN_ZONE_FIELD_ABANDONLOC = "screen.zone.field.abandonloc";
	
	
	public static final String KEY_ZONE_SCREEN_ERROR_DUPLICATE_PUTAWAYZONE = "zone.screen.error.duplicate.zone";
	
	
	//Location screen
	
	public static final String KEY_SCREEN_LOCATION_FIELD_ABC = "screen.location.field.abc";
	public static final String KEY_SCREEN_LOCATION_FIELD_COMMINGLELOT = "screen.location.field.comminglelot";
	public static final String KEY_SCREEN_LOCATION_FIELD_COMMINGLESKU = "screen.location.field.comminglesku";
	public static final String KEY_SCREEN_LOCATION_FIELD_CUBE = "screen.location.field.cube";
	public static final String KEY_SCREEN_LOCATION_FIELD_CUBICCAPACITY = "screen.location.field.cubiccapacity";
	public static final String KEY_SCREEN_LOCATION_FIELD_FACILITY = "screen.location.field.facility";
	public static final String KEY_SCREEN_LOCATION_FIELD_FOOTPRINT = "screen.location.field.footprint";
	public static final String KEY_SCREEN_LOCATION_FIELD_HEIGHT = "screen.location.field.height";
	public static final String KEY_SCREEN_LOCATION_FIELD_LENGTH = "screen.location.field.length";
	public static final String KEY_SCREEN_LOCATION_FIELD_LOC = "screen.location.field.loc";
	public static final String KEY_SCREEN_LOCATION_FIELD_LOCATIONCATEGORY = "screen.location.field.locationcategory";
	public static final String KEY_SCREEN_LOCATION_FIELD_LOCATIONFLAG = "screen.location.field.locationflag";
	public static final String KEY_SCREEN_LOCATION_FIELD_LOCATIONHANDLING = "screen.location.field.locationhandling";
	public static final String KEY_SCREEN_LOCATION_FIELD_LOCATIONTYPE = "screen.location.field.locationtype";
	public static final String KEY_SCREEN_LOCATION_FIELD_LOCGROUPID = "screen.location.field.locgroupid";
	public static final String KEY_SCREEN_LOCATION_FIELD_LOCLEVEL = "screen.location.field.loclevel";
	public static final String KEY_SCREEN_LOCATION_FIELD_LOGICALLOCATION = "screen.location.field.logicallocation";
	public static final String KEY_SCREEN_LOCATION_FIELD_LOSEID = "screen.location.field.loseid";
	public static final String KEY_SCREEN_LOCATION_FIELD_OPTLOC = "screen.location.field.optloc";
	public static final String KEY_SCREEN_LOCATION_FIELD_PICKMETHOD = "screen.location.field.pickmethod";
	public static final String KEY_SCREEN_LOCATION_FIELD_PICKZONE = "screen.location.field.pickzone";
	public static final String KEY_SCREEN_LOCATION_FIELD_PUTAWAYZONE = "screen.location.field.putawayzone";
	public static final String KEY_SCREEN_LOCATION_FIELD_SECTION = "screen.location.field.section";
	public static final String KEY_SCREEN_LOCATION_FIELD_SECTIONKEY = "screen.location.field.sectionkey";
	public static final String KEY_SCREEN_LOCATION_FIELD_SERIALKEY = "screen.location.field.serialkey";
	public static final String KEY_SCREEN_LOCATION_FIELD_STACKLIMIT = "screen.location.field.stacklimit";
	public static final String KEY_SCREEN_LOCATION_FIELD_STATUS = "screen.location.field.status";
	public static final String KEY_SCREEN_LOCATION_FIELD_WEIGHTCAPACITY = "screen.location.field.weightcapacity";
	public static final String KEY_SCREEN_LOCATION_FIELD_WHSEID = "screen.location.field.whseid";
	public static final String KEY_SCREEN_LOCATION_FIELD_WIDTH = "screen.location.field.width";
	public static final String KEY_SCREEN_LOCATION_FIELD_XCOORD = "screen.location.field.xcoord";
	public static final String KEY_SCREEN_LOCATION_FIELD_YCOORD = "screen.location.field.ycoord";
	public static final String KEY_SCREEN_LOCATION_FIELD_ZCOORD = "screen.location.field.zcoord";
	public static final String KEY_SCREEN_LOCATION_FIELD_INTERLEAVINGSEQUENCE = "screen.location.field.interleavingsequence";
	public static final String KEY_SCREEN_LOCATION_FIELD_ORIENTATION = "screen.location.field.orientation";
	
	public static final String KEY_SCREEN_LOCATION_FIELD_AUTOSHIP = "screen.location.field.autoship";
	
	public static final String KEY_LOCATION_SCREEN_ERROR_DUPLICATE_LOCATION = "location.screen.error.duplicate.location";
	public static final String KEY_LOCATION_SCREEN_ERROR_LOSEID_REQUIRED_FOR_SPEEDPICK_OR_SORT_LOCATION_TYPE = "location.screen.error.loseid.required.for.speedpick.or.sort.location.type";
	
	//PAZONEEQUIPMENTEXCLUDEDETAIL
	public static final String KEY_SCREEN_PAZONEEQUIPMENTEXCLUDE_FIELD_DESCR = "screen.pazoneequipmentexcludedetail.field.descr";
	public static final String KEY_SCREEN_PAZONEEQUIPMENTEXCLUDE_FIELD_EQUIPMENTPROFILEKEY = "screen.pazoneequipmentexcludedetail.field.equipmentprofilekey";
	public static final String KEY_SCREEN_PAZONEEQUIPMENTEXCLUDE_FIELD_PUTAWAYZONE = "screen.pazoneequipmentexcludedetail.field.putawayzone";
	
	
	public static final String KEY_PAZONEEQUIPMENTEXCLUDE_SCREEN_ERROR_DUPLICATE_PUTAWAYZONEEQUIPMENTPROFILEKEY="pazoneequipmentexcludedetail.screen.error.duplicate.zoneequipment";
	
	
	
	//Owner screen
	public static final String KEY_SCREEN_OWNER_FIELD_ADDRESS1 = "screen.owner.field.address1";
	public static final String KEY_SCREEN_OWNER_FIELD_ADDRESS2 = "screen.owner.field.address2";
	public static final String KEY_SCREEN_OWNER_FIELD_ADDRESS3 = "screen.owner.field.address3";
	public static final String KEY_SCREEN_OWNER_FIELD_ADDRESS4 = "screen.owner.field.address4";
	public static final String KEY_SCREEN_OWNER_FIELD_ALLOWAUTOCLOSEFORASN = "screen.owner.field.allowautocloseforasn";
	public static final String KEY_SCREEN_OWNER_FIELD_ALLOWAUTOCLOSEFORPO = "screen.owner.field.allowautocloseforpo";
	public static final String KEY_SCREEN_OWNER_FIELD_ALLOWAUTOCLOSEFORPS = "screen.owner.field.allowautocloseforps";
	public static final String KEY_SCREEN_OWNER_FIELD_ALLOWCOMMINGLEDLPN = "screen.owner.field.allowcommingledlpn";
	public static final String KEY_SCREEN_OWNER_FIELD_ALLOWDUPLICATELICENSEPLATES = "screen.owner.field.allowduplicatelicenseplates";
	public static final String KEY_SCREEN_OWNER_FIELD_ALLOWOVERSHIPMENT = "screen.owner.field.allowovershipment";
	public static final String KEY_SCREEN_OWNER_FIELD_ALLOWSINGLESCANRECEIVING = "screen.owner.field.allowsinglescanreceiving";
	public static final String KEY_SCREEN_OWNER_FIELD_ALLOWSYSTEMGENERATEDLPN = "screen.owner.field.allowsystemgeneratedlpn";
	public static final String KEY_SCREEN_OWNER_FIELD_APPLICATIONID = "screen.owner.field.applicationid";
	public static final String KEY_SCREEN_OWNER_FIELD_APPORTIONRULE = "screen.owner.field.apportionrule";
	public static final String KEY_SCREEN_OWNER_FIELD_AUTOCLOSEASN = "screen.owner.field.autocloseasn";
	public static final String KEY_SCREEN_OWNER_FIELD_AUTOCLOSEPO = "screen.owner.field.autoclosepo";
	public static final String KEY_SCREEN_OWNER_FIELD_AUTOPRINTLABELLPN = "screen.owner.field.autoprintlabellpn";
	public static final String KEY_SCREEN_OWNER_FIELD_AUTOPRINTLABELPUTAWAY = "screen.owner.field.autoprintlabelputaway";
	public static final String KEY_SCREEN_OWNER_FIELD_B_ADDRESS1 = "screen.owner.field.b_address1";
	public static final String KEY_SCREEN_OWNER_FIELD_B_ADDRESS2 = "screen.owner.field.b_address2";
	public static final String KEY_SCREEN_OWNER_FIELD_B_ADDRESS3 = "screen.owner.field.b_address3";
	public static final String KEY_SCREEN_OWNER_FIELD_B_ADDRESS4 = "screen.owner.field.b_address4";
	public static final String KEY_SCREEN_OWNER_FIELD_B_CITY = "screen.owner.field.b_city";
	public static final String KEY_SCREEN_OWNER_FIELD_B_COMPANY = "screen.owner.field.b_company";
	public static final String KEY_SCREEN_OWNER_FIELD_B_CONTACT1 = "screen.owner.field.b_contact1";
	public static final String KEY_SCREEN_OWNER_FIELD_B_CONTACT2 = "screen.owner.field.b_contact2";
	public static final String KEY_SCREEN_OWNER_FIELD_B_COUNTRY = "screen.owner.field.b_country";
	public static final String KEY_SCREEN_OWNER_FIELD_B_EMAIL1 = "screen.owner.field.b_email1";
	public static final String KEY_SCREEN_OWNER_FIELD_B_EMAIL2 = "screen.owner.field.b_email2";
	public static final String KEY_SCREEN_OWNER_FIELD_B_FAX1 = "screen.owner.field.b_fax1";
	public static final String KEY_SCREEN_OWNER_FIELD_B_FAX2 = "screen.owner.field.b_fax2";
	public static final String KEY_SCREEN_OWNER_FIELD_B_ISOCNTRYCODE = "screen.owner.field.b_isocntrycode";
	public static final String KEY_SCREEN_OWNER_FIELD_B_PHONE1 = "screen.owner.field.b_phone1";
	public static final String KEY_SCREEN_OWNER_FIELD_B_PHONE2 = "screen.owner.field.b_phone2";
	public static final String KEY_SCREEN_OWNER_FIELD_B_STATE = "screen.owner.field.b_state";
	public static final String KEY_SCREEN_OWNER_FIELD_B_ZIP = "screen.owner.field.b_zip";
	public static final String KEY_SCREEN_OWNER_FIELD_BARCODECONFIGKEY = "screen.owner.field.barcodeconfigkey";
	public static final String KEY_SCREEN_OWNER_FIELD_CALCULATEPUTAWAYLOCATION = "screen.owner.field.calculateputawaylocation";
	public static final String KEY_SCREEN_OWNER_FIELD_CARTONGROUP = "screen.owner.field.cartongroup";
	public static final String KEY_SCREEN_OWNER_FIELD_CASELABELTYPE = "screen.owner.field.caselabeltype";
	public static final String KEY_SCREEN_OWNER_FIELD_CCADJBYRF = "screen.owner.field.ccadjbyrf";
	public static final String KEY_SCREEN_OWNER_FIELD_CCDISCREPANCYRULE = "screen.owner.field.ccdiscrepancyrule";
	public static final String KEY_SCREEN_OWNER_FIELD_CCSKUXLOC = "screen.owner.field.ccskuxloc";
	public static final String KEY_SCREEN_OWNER_FIELD_CITY = "screen.owner.field.city";
	public static final String KEY_SCREEN_OWNER_FIELD_COMPANY = "screen.owner.field.company";
	public static final String KEY_SCREEN_OWNER_FIELD_CONTACT1 = "screen.owner.field.contact1";
	public static final String KEY_SCREEN_OWNER_FIELD_CONTACT2 = "screen.owner.field.contact2";
	public static final String KEY_SCREEN_OWNER_FIELD_COUNTRY = "screen.owner.field.country";
	public static final String KEY_SCREEN_OWNER_FIELD_CREATEPATASKONRFRECEIPT = "screen.owner.field.createpataskonrfreceipt";
	public static final String KEY_SCREEN_OWNER_FIELD_CREDITLIMIT = "screen.owner.field.creditlimit";
	public static final String KEY_SCREEN_OWNER_FIELD_CWOFLAG = "screen.owner.field.cwoflag";
	public static final String KEY_SCREEN_OWNER_FIELD_DEFAULTPACKINGLOCATION = "screen.owner.field.defaultpackinglocation";
	public static final String KEY_SCREEN_OWNER_FIELD_DEFAULTPUTAWAYSTRATEGY = "screen.owner.field.defaultputawaystrategy";
	public static final String KEY_SCREEN_OWNER_FIELD_DEFAULTQCLOC = "screen.owner.field.defaultqcloc";
	public static final String KEY_SCREEN_OWNER_FIELD_DEFAULTQCLOCOUT = "screen.owner.field.defaultqclocout";
	public static final String KEY_SCREEN_OWNER_FIELD_DEFAULTRETURNSLOC = "screen.owner.field.defaultreturnsloc";
	public static final String KEY_SCREEN_OWNER_FIELD_DEFAULTROTATION = "screen.owner.field.defaultrotation";
	public static final String KEY_SCREEN_OWNER_FIELD_DEFAULTSKUROTATION = "screen.owner.field.defaultskurotation";
	public static final String KEY_SCREEN_OWNER_FIELD_DEFAULTSTRATEGY = "screen.owner.field.defaultstrategy";
	public static final String KEY_SCREEN_OWNER_FIELD_DESCRIPTION = "screen.owner.field.description";
	public static final String KEY_SCREEN_OWNER_FIELD_DUPCASEID = "screen.owner.field.dupcaseid";
	public static final String KEY_SCREEN_OWNER_FIELD_EMAIL1 = "screen.owner.field.email1";
	public static final String KEY_SCREEN_OWNER_FIELD_EMAIL2 = "screen.owner.field.email2";
	public static final String KEY_SCREEN_OWNER_FIELD_ENABLEOPPXDOCK = "screen.owner.field.enableoppxdock";
	public static final String KEY_SCREEN_OWNER_FIELD_ENABLEPACKINGDEFAULT = "screen.owner.field.enablepackingdefault";
	public static final String KEY_SCREEN_OWNER_FIELD_FAX1 = "screen.owner.field.fax1";
	public static final String KEY_SCREEN_OWNER_FIELD_FAX2 = "screen.owner.field.fax2";
	public static final String KEY_SCREEN_OWNER_FIELD_GENERATEPACKLIST = "screen.owner.field.generatepacklist";
	public static final String KEY_SCREEN_OWNER_FIELD_INSPECTATPACK = "screen.owner.field.inspectatpack";
	public static final String KEY_SCREEN_OWNER_FIELD_ISOCNTRYCODE = "screen.owner.field.isocntrycode";
	public static final String KEY_SCREEN_OWNER_FIELD_LPNBARCODEFORMAT = "screen.owner.field.lpnbarcodeformat";
	public static final String KEY_SCREEN_OWNER_FIELD_LPNBARCODESYMBOLOGY = "screen.owner.field.lpnbarcodesymbology";
	public static final String KEY_SCREEN_OWNER_FIELD_LPNLENGTH = "screen.owner.field.lpnlength";
	public static final String KEY_SCREEN_OWNER_FIELD_LPNROLLBACKNUMBER = "screen.owner.field.lpnrollbacknumber";
	public static final String KEY_SCREEN_OWNER_FIELD_LPNSTARTNUMBER = "screen.owner.field.lpnstartnumber";
	public static final String KEY_SCREEN_OWNER_FIELD_MAXIMUMORDERS = "screen.owner.field.maximumorders";
	public static final String KEY_SCREEN_OWNER_FIELD_MINIMUMPERCENT = "screen.owner.field.minimumpercent";
	public static final String KEY_SCREEN_OWNER_FIELD_MULTIZONEPLPA = "screen.owner.field.multizoneplpa";
	public static final String KEY_SCREEN_OWNER_FIELD_NEXTLPNNUMBER = "screen.owner.field.nextlpnnumber";
	public static final String KEY_SCREEN_OWNER_FIELD_NOTES1 = "screen.owner.field.notes1";
	public static final String KEY_SCREEN_OWNER_FIELD_NOTES2 = "screen.owner.field.notes2";
	public static final String KEY_SCREEN_OWNER_FIELD_OPPORDERSTRATEGYKEY = "screen.owner.field.opporderstrategykey";
	public static final String KEY_SCREEN_OWNER_FIELD_ORDERBREAKDEFAULT = "screen.owner.field.orderbreakdefault";
	public static final String KEY_SCREEN_OWNER_FIELD_ORDERDATEENDDAYS = "screen.owner.field.orderdateenddays";
	public static final String KEY_SCREEN_OWNER_FIELD_ORDERDATESTARTDAYS = "screen.owner.field.orderdatestartdays";
	public static final String KEY_SCREEN_OWNER_FIELD_ORDERTYPERESTRICT01 = "screen.owner.field.ordertyperestrict01";
	public static final String KEY_SCREEN_OWNER_FIELD_ORDERTYPERESTRICT02 = "screen.owner.field.ordertyperestrict02";
	public static final String KEY_SCREEN_OWNER_FIELD_ORDERTYPERESTRICT03 = "screen.owner.field.ordertyperestrict03";
	public static final String KEY_SCREEN_OWNER_FIELD_ORDERTYPERESTRICT04 = "screen.owner.field.ordertyperestrict04";
	public static final String KEY_SCREEN_OWNER_FIELD_ORDERTYPERESTRICT05 = "screen.owner.field.ordertyperestrict05";
	public static final String KEY_SCREEN_OWNER_FIELD_ORDERTYPERESTRICT06 = "screen.owner.field.ordertyperestrict06";
	public static final String KEY_SCREEN_OWNER_FIELD_PACKINGVALIDATIONTEMPLATE = "screen.owner.field.packingvalidationtemplate";
	public static final String KEY_SCREEN_OWNER_FIELD_PHONE1 = "screen.owner.field.phone1";
	public static final String KEY_SCREEN_OWNER_FIELD_PHONE2 = "screen.owner.field.phone2";
	public static final String KEY_SCREEN_OWNER_FIELD_PICKCODE = "screen.owner.field.pickcode";
	public static final String KEY_SCREEN_OWNER_FIELD_PISKUXLOC = "screen.owner.field.piskuxloc";
	public static final String KEY_SCREEN_OWNER_FIELD_RECEIPTVALIDATIONTEMPLATE = "screen.owner.field.receiptvalidationtemplate";
	public static final String KEY_SCREEN_OWNER_FIELD_ROLLRECEIPT = "screen.owner.field.rollreceipt";
	public static final String KEY_SCREEN_OWNER_FIELD_SCAC_CODE = "screen.owner.field.scac_code";
	public static final String KEY_SCREEN_OWNER_FIELD_SERIALKEY = "screen.owner.field.serialkey";
	public static final String KEY_SCREEN_OWNER_FIELD_SKUSETUPREQUIRED = "screen.owner.field.skusetuprequired";
	public static final String KEY_SCREEN_OWNER_FIELD_SSCC1STDIGIT = "screen.owner.field.sscc1stdigit";
	public static final String KEY_SCREEN_OWNER_FIELD_STATE = "screen.owner.field.state";
	public static final String KEY_SCREEN_OWNER_FIELD_STATUS = "screen.owner.field.status";
	public static final String KEY_SCREEN_OWNER_FIELD_STORERKEY = "screen.owner.field.storerkey";
	public static final String KEY_SCREEN_OWNER_FIELD_SUSR1 = "screen.owner.field.susr1";
	public static final String KEY_SCREEN_OWNER_FIELD_SUSR2 = "screen.owner.field.susr2";
	public static final String KEY_SCREEN_OWNER_FIELD_SUSR3 = "screen.owner.field.susr3";
	public static final String KEY_SCREEN_OWNER_FIELD_SUSR4 = "screen.owner.field.susr4";
	public static final String KEY_SCREEN_OWNER_FIELD_SUSR5 = "screen.owner.field.susr5";
	public static final String KEY_SCREEN_OWNER_FIELD_SUSR6 = "screen.owner.field.susr6";
	public static final String KEY_SCREEN_OWNER_FIELD_TITLE1 = "screen.owner.field.title1";
	public static final String KEY_SCREEN_OWNER_FIELD_TITLE2 = "screen.owner.field.title2";
	public static final String KEY_SCREEN_OWNER_FIELD_TRACKINVENTORYBY = "screen.owner.field.trackinventoryby";
	public static final String KEY_SCREEN_OWNER_FIELD_TYPE = "screen.owner.field.type";
	public static final String KEY_SCREEN_OWNER_FIELD_UCCVENDORNUMBER = "screen.owner.field.uccvendornumber";
	public static final String KEY_SCREEN_OWNER_FIELD_VAT = "screen.owner.field.vat";
	public static final String KEY_SCREEN_OWNER_FIELD_WHSEID = "screen.owner.field.whseid";
	public static final String KEY_SCREEN_OWNER_FIELD_ZIP = "screen.owner.field.zip";
	
	public static final String KEY_SCREEN_OWNER_FIELD_ALMINIMUMCHARGE = "screen.owner.field.alminimumcharge";
	public static final String KEY_SCREEN_OWNER_FIELD_ALMINIMUMGLDIST = "screen.owner.field.alminimumgldist";
	public static final String KEY_SCREEN_OWNER_FIELD_ALMINIMUMTAXGROUP = "screen.owner.field.alminimumtaxgroup";
	public static final String KEY_SCREEN_OWNER_FIELD_BILLINGGROUP = "screen.owner.field.billinggroup";
	public static final String KEY_SCREEN_OWNER_FIELD_HIMINIMUMINVOICECHARGE = "screen.owner.field.himinimuminvoicecharge";
	public static final String KEY_SCREEN_OWNER_FIELD_HIMINIMUMINVOICEGLDIST = "screen.owner.field.himinimuminvoicegldist";
	public static final String KEY_SCREEN_OWNER_FIELD_HIMINIMUMINVOICETAXGROUP = "screen.owner.field.himinimuminvoicetaxgroup";
	public static final String KEY_SCREEN_OWNER_FIELD_HIMINIMUMRECEIPTCHARGE = "screen.owner.field.himinimumreceiptcharge";
	public static final String KEY_SCREEN_OWNER_FIELD_HIMINIMUMRECEIPTGLDIST = "screen.owner.field.himinimumreceiptgldist";
	public static final String KEY_SCREEN_OWNER_FIELD_HIMINIMUMRECEIPTTAXGROUP = "screen.owner.field.himinimumreceipttaxgroup";
	public static final String KEY_SCREEN_OWNER_FIELD_HOMINIMUMSHIPMENTCHARGE = "screen.owner.field.hominimumshipmentcharge";
	public static final String KEY_SCREEN_OWNER_FIELD_HOMINIMUMSHIPMENTGLDIST = "screen.owner.field.hominimumshipmentgldist";
	public static final String KEY_SCREEN_OWNER_FIELD_HOMINIMUMSHIPMENTTAXGROUP = "screen.owner.field.hominimumshipmenttaxgroup";
	public static final String KEY_SCREEN_OWNER_FIELD_INVOICEBATCH = "screen.owner.field.invoicebatch";
	public static final String KEY_SCREEN_OWNER_FIELD_INVOICENUMBERSTRATEGY = "screen.owner.field.invoicenumberstrategy";
	public static final String KEY_SCREEN_OWNER_FIELD_ISMINIMUMINVOICECHARGE = "screen.owner.field.isminimuminvoicecharge";
	public static final String KEY_SCREEN_OWNER_FIELD_ISMINIMUMINVOICEGLDIST = "screen.owner.field.isminimuminvoicegldist";
	public static final String KEY_SCREEN_OWNER_FIELD_ISMINIMUMINVOICETAXGROUP = "screen.owner.field.isminimuminvoicetaxgroup";
	public static final String KEY_SCREEN_OWNER_FIELD_ISMINIMUMRECEIPTCHARGE = "screen.owner.field.isminimumreceiptcharge";
	public static final String KEY_SCREEN_OWNER_FIELD_ISMINIMUMRECEIPTGLDIST = "screen.owner.field.isminimumreceiptgldist";
	public static final String KEY_SCREEN_OWNER_FIELD_ISMINIMUMRECEIPTTAXGROUP = "screen.owner.field.isminimumreceipttaxgroup";
	public static final String KEY_SCREEN_OWNER_FIELD_RSMINIMUMINVOICECHARGE = "screen.owner.field.rsminimuminvoicecharge";
	public static final String KEY_SCREEN_OWNER_FIELD_RSMINIMUMINVOICEGLDIST = "screen.owner.field.rsminimuminvoicegldist";
	public static final String KEY_SCREEN_OWNER_FIELD_RSMINIMUMINVOICETAXGROUP = "screen.owner.field.rsminimuminvoicetaxgroup";
	
	public static final String KEY_SCREEN_OWNER_FIELD_CREATEOPPXDTASKS = "screen.owner.field.createoppxdtasks";
	public static final String KEY_SCREEN_OWNER_FIELD_ISSUEOPPXDTASKS = "screen.owner.field.issueoppxdtasks";
	public static final String KEY_SCREEN_OWNER_FIELD_OPPXDPICKFROM = "screen.owner.field.oppxdpickfrom";
	public static final String KEY_SCREEN_OWNER_FIELD_OBXDSTAGE = "screen.owner.field.obxdstage";
	public static final String KEY_SCREEN_OWNER_FIELD_SPSUOMWEIGHT = "screen.owner.field.spsuomweight";
	public static final String KEY_SCREEN_OWNER_FIELD_SPSUOMDIMENSION = "screen.owner.field.spsuomdimension";
	public static final String KEY_SCREEN_OWNER_FIELD_DEFRPLNSORT = "screen.owner.field.defrplnsort";
	public static final String KEY_SCREEN_OWNER_FIELD_DEFDAPICKSORT = "screen.owner.field.defdapicksort";
	
	
	public static final String KEY_SCREEN_OWNER_FIELD_CONSIGNEEKEY = "screen.owner.field.consigneekey";
	
	public static final String KEY_OWNER_SCREEN_ERROR_DUPLICATE_OWNER="owner.screen.error.duplicate.owner";
	
	//Customer Screen
	public static final String KEY_SCREEN_CUSTOMER_FIELD_STORERKEY = "screen.customer.field.storerkey";
	public static final String KEY_CUSTOMER_SCREEN_ERROR_DUPLICATE_CUSTOMER="customer.screen.error.duplicate.customer";

	//BillTO Screen
	public static final String KEY_SCREEN_BILLTO_FIELD_STORERKEY = "screen.billto.field.storerkey";
	public static final String KEY_BILLTO_SCREEN_ERROR_DUPLICATE_BILLTO="billto.screen.error.duplicate.billto";

	//Carrier
	public static final String KEY_SCREEN_CARRIER_FIELD_STORERKEY = "screen.carrier.field.storerkey";
	public static final String KEY_SCREEN_CARRIER_FIELD_KSHIP_CARRIER = "screen.carrier.field.kshipcarrier";
	public static final String KEY_CARRIER_SCREEN_ERROR_DUPLICATE_CARRIER="carrier.screen.error.duplicate.carrier";

	//Vendor
	public static final String KEY_SCREEN_VENDOR_FIELD_STORERKEY = "screen.vendor.field.storerkey";
	public static final String KEY_VENDOR_SCREEN_ERROR_DUPLICATE_VENDOR="vendor.screen.error.duplicate.vendor";

	//Receipt
	public static final String KEY_SCREEN_RECEIPT_FIELD_ACTUALSHIPDATE = "screen.receipt.field.actualshipdate";
	public static final String KEY_SCREEN_RECEIPT_FIELD_ADDDATE = "screen.receipt.field.adddate";
	public static final String KEY_SCREEN_RECEIPT_FIELD_ADDWHO = "screen.receipt.field.addwho";
	public static final String KEY_SCREEN_RECEIPT_FIELD_ADVICEDATE = "screen.receipt.field.advicedate";
	public static final String KEY_SCREEN_RECEIPT_FIELD_ADVICENUMBER = "screen.receipt.field.advicenumber";
	public static final String KEY_SCREEN_RECEIPT_FIELD_ALLOWAUTORECEIPT = "screen.receipt.field.allowautoreceipt";
	public static final String KEY_SCREEN_RECEIPT_FIELD_ARRIVALDATETIME = "screen.receipt.field.arrivaldatetime";
	public static final String KEY_SCREEN_RECEIPT_FIELD_BILLEDCONTAINERQTY = "screen.receipt.field.billedcontainerqty";
	public static final String KEY_SCREEN_RECEIPT_FIELD_CARRIERADDRESS1 = "screen.receipt.field.carrieraddress1";
	public static final String KEY_SCREEN_RECEIPT_FIELD_CARRIERADDRESS2 = "screen.receipt.field.carrieraddress2";
	public static final String KEY_SCREEN_RECEIPT_FIELD_CARRIERCITY = "screen.receipt.field.carriercity";
	public static final String KEY_SCREEN_RECEIPT_FIELD_CARRIERCOUNTRY = "screen.receipt.field.carriercountry";
	public static final String KEY_SCREEN_RECEIPT_FIELD_CARRIERKEY = "screen.receipt.field.carrierkey";
	public static final String KEY_SCREEN_RECEIPT_FIELD_CARRIERNAME = "screen.receipt.field.carriername";
	public static final String KEY_SCREEN_RECEIPT_FIELD_CARRIERPHONE = "screen.receipt.field.carrierphone";
	public static final String KEY_SCREEN_RECEIPT_FIELD_CARRIERREFERENCE = "screen.receipt.field.carrierreference";
	public static final String KEY_SCREEN_RECEIPT_FIELD_CARRIERSTATE = "screen.receipt.field.carrierstate";
	public static final String KEY_SCREEN_RECEIPT_FIELD_CARRIERZIP = "screen.receipt.field.carrierzip";
	public static final String KEY_SCREEN_RECEIPT_FIELD_CLOSEDDATE = "screen.receipt.field.closeddate";
	public static final String KEY_SCREEN_RECEIPT_FIELD_CONTAINERKEY = "screen.receipt.field.containerkey";
	public static final String KEY_SCREEN_RECEIPT_FIELD_CONTAINERQTY = "screen.receipt.field.containerqty";
	public static final String KEY_SCREEN_RECEIPT_FIELD_CONTAINERTYPE = "screen.receipt.field.containertype";
	public static final String KEY_SCREEN_RECEIPT_FIELD_DESTINATIONCOUNTRY = "screen.receipt.field.destinationcountry";
	public static final String KEY_SCREEN_RECEIPT_FIELD_DRIVERNAME = "screen.receipt.field.drivername";
	public static final String KEY_SCREEN_RECEIPT_FIELD_EDITDATE = "screen.receipt.field.editdate";
	public static final String KEY_SCREEN_RECEIPT_FIELD_EDITWHO = "screen.receipt.field.editwho";
	public static final String KEY_SCREEN_RECEIPT_FIELD_EFFECTIVEDATE = "screen.receipt.field.effectivedate";
	public static final String KEY_SCREEN_RECEIPT_FIELD_EXPECTEDRECEIPTDATE = "screen.receipt.field.expectedreceiptdate";
	public static final String KEY_SCREEN_RECEIPT_FIELD_EXTERNALRECEIPTKEY2 = "screen.receipt.field.externalreceiptkey2";
	public static final String KEY_SCREEN_RECEIPT_FIELD_EXTERNRECEIPTKEY = "screen.receipt.field.externreceiptkey";
	public static final String KEY_SCREEN_RECEIPT_FIELD_FORTE_FLAG = "screen.receipt.field.forte_flag";
	public static final String KEY_SCREEN_RECEIPT_FIELD_GROSSWEIGHT = "screen.receipt.field.grossweight";
	public static final String KEY_SCREEN_RECEIPT_FIELD_INCOTERMS = "screen.receipt.field.incoterms";
	public static final String KEY_SCREEN_RECEIPT_FIELD_LOTTABLEMATCHREQUIRED = "screen.receipt.field.lottablematchrequired";
	public static final String KEY_SCREEN_RECEIPT_FIELD_NOTES = "screen.receipt.field.notes";
	public static final String KEY_SCREEN_RECEIPT_FIELD_OPENQTY = "screen.receipt.field.openqty";
	public static final String KEY_SCREEN_RECEIPT_FIELD_ORIGINCOUNTRY = "screen.receipt.field.origincountry";
	public static final String KEY_SCREEN_RECEIPT_FIELD_PACKINGSLIPNUMBER = "screen.receipt.field.packingslipnumber";
	public static final String KEY_SCREEN_RECEIPT_FIELD_PLACEOFDELIVERY = "screen.receipt.field.placeofdelivery";
	public static final String KEY_SCREEN_RECEIPT_FIELD_PLACEOFDISCHARGE = "screen.receipt.field.placeofdischarge";
	public static final String KEY_SCREEN_RECEIPT_FIELD_PLACEOFISSUE = "screen.receipt.field.placeofissue";
	public static final String KEY_SCREEN_RECEIPT_FIELD_PLACEOFLOADING = "screen.receipt.field.placeofloading";
	public static final String KEY_SCREEN_RECEIPT_FIELD_POKEY = "screen.receipt.field.pokey";
	public static final String KEY_SCREEN_RECEIPT_FIELD_RECEIPTDATE = "screen.receipt.field.receiptdate";
	public static final String KEY_SCREEN_RECEIPT_FIELD_RECEIPTGROUP = "screen.receipt.field.receiptgroup";
	public static final String KEY_SCREEN_RECEIPT_FIELD_RECEIPTID = "screen.receipt.field.receiptid";
	public static final String KEY_SCREEN_RECEIPT_FIELD_RECEIPTKEY = "screen.receipt.field.receiptkey";
	public static final String KEY_SCREEN_RECEIPT_FIELD_RMA = "screen.receipt.field.rma";
	public static final String KEY_SCREEN_RECEIPT_FIELD_SCHEDULEDSHIPDATE = "screen.receipt.field.scheduledshipdate";
	public static final String KEY_SCREEN_RECEIPT_FIELD_SERIALKEY = "screen.receipt.field.serialkey";
	public static final String KEY_SCREEN_RECEIPT_FIELD_SHIPFROMADDRESSLINE1 = "screen.receipt.field.shipfromaddressline1";
	public static final String KEY_SCREEN_RECEIPT_FIELD_SHIPFROMADDRESSLINE2 = "screen.receipt.field.shipfromaddressline2";
	public static final String KEY_SCREEN_RECEIPT_FIELD_SHIPFROMADDRESSLINE3 = "screen.receipt.field.shipfromaddressline3";
	public static final String KEY_SCREEN_RECEIPT_FIELD_SHIPFROMADDRESSLINE4 = "screen.receipt.field.shipfromaddressline4";
	public static final String KEY_SCREEN_RECEIPT_FIELD_SHIPFROMCITY = "screen.receipt.field.shipfromcity";
	public static final String KEY_SCREEN_RECEIPT_FIELD_SHIPFROMCONTACT = "screen.receipt.field.shipfromcontact";
	public static final String KEY_SCREEN_RECEIPT_FIELD_SHIPFROMEMAIL = "screen.receipt.field.shipfromemail";
	public static final String KEY_SCREEN_RECEIPT_FIELD_SHIPFROMISOCOUNTRY = "screen.receipt.field.shipfromisocountry";
	public static final String KEY_SCREEN_RECEIPT_FIELD_SHIPFROMPHONE = "screen.receipt.field.shipfromphone";
	public static final String KEY_SCREEN_RECEIPT_FIELD_SHIPFROMSTATE = "screen.receipt.field.shipfromstate";
	public static final String KEY_SCREEN_RECEIPT_FIELD_SHIPFROMZIP = "screen.receipt.field.shipfromzip";
	public static final String KEY_SCREEN_RECEIPT_FIELD_SIGNATORY = "screen.receipt.field.signatory";
	public static final String KEY_SCREEN_RECEIPT_FIELD_STATUS = "screen.receipt.field.status";
	public static final String KEY_SCREEN_RECEIPT_FIELD_STORERKEY = "screen.receipt.field.storerkey";
	public static final String KEY_SCREEN_RECEIPT_FIELD_SUPPLIERCODE = "screen.receipt.field.suppliercode";
	public static final String KEY_SCREEN_RECEIPT_FIELD_SUPPLIERNAME = "screen.receipt.field.suppliername";
	public static final String KEY_SCREEN_RECEIPT_FIELD_SUSR1 = "screen.receipt.field.susr1";
	public static final String KEY_SCREEN_RECEIPT_FIELD_SUSR2 = "screen.receipt.field.susr2";
	public static final String KEY_SCREEN_RECEIPT_FIELD_SUSR3 = "screen.receipt.field.susr3";
	public static final String KEY_SCREEN_RECEIPT_FIELD_SUSR4 = "screen.receipt.field.susr4";
	public static final String KEY_SCREEN_RECEIPT_FIELD_SUSR5 = "screen.receipt.field.susr5";
	public static final String KEY_SCREEN_RECEIPT_FIELD_TERMSNOTE = "screen.receipt.field.termsnote";
	public static final String KEY_SCREEN_RECEIPT_FIELD_TOTALVOLUME = "screen.receipt.field.totalvolume";
	public static final String KEY_SCREEN_RECEIPT_FIELD_TRACKINVENTORYBY = "screen.receipt.field.trackinventoryby";
	public static final String KEY_SCREEN_RECEIPT_FIELD_TRAILERNUMBER = "screen.receipt.field.trailernumber";
	public static final String KEY_SCREEN_RECEIPT_FIELD_TRAILEROWNER = "screen.receipt.field.trailerowner";
	public static final String KEY_SCREEN_RECEIPT_FIELD_TRANSPORTATIONMODE = "screen.receipt.field.transportationmode";
	public static final String KEY_SCREEN_RECEIPT_FIELD_TYPE = "screen.receipt.field.type";
	public static final String KEY_SCREEN_RECEIPT_FIELD_VEHICLEDATE = "screen.receipt.field.vehicledate";
	public static final String KEY_SCREEN_RECEIPT_FIELD_VEHICLENUMBER = "screen.receipt.field.vehiclenumber";
	public static final String KEY_SCREEN_RECEIPT_FIELD_WAREHOUSEREFERENCE = "screen.receipt.field.warehousereference";
	public static final String KEY_SCREEN_RECEIPT_FIELD_WHSEID = "screen.receipt.field.whseid";
	
	

	public static final String KEY_RECEIPT_SCREEN_ERROR_DUPLICATE_RECEIPT="receipt.screen.error.duplicate.receipt";
	
	//Receiptdetail
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_ADDDATE = "screen.receiptdetail.field.adddate";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_ADDWHO = "screen.receiptdetail.field.addwho";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_ALTSKU = "screen.receiptdetail.field.altsku";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_CASECNT = "screen.receiptdetail.field.casecnt";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_CONDITIONCODE = "screen.receiptdetail.field.conditioncode";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_CONTAINERKEY = "screen.receiptdetail.field.containerkey";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_CUBE = "screen.receiptdetail.field.cube";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_DATERECEIVED = "screen.receiptdetail.field.datereceived";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_DISPOSITIONCODE = "screen.receiptdetail.field.dispositioncode";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_DISPOSITIONTYPE = "screen.receiptdetail.field.dispositiontype";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_EDITDATE = "screen.receiptdetail.field.editdate";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_EDITWHO = "screen.receiptdetail.field.editwho";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_EFFECTIVEDATE = "screen.receiptdetail.field.effectivedate";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_EXTENDEDPRICE = "screen.receiptdetail.field.extendedprice";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_EXTERNALLOT = "screen.receiptdetail.field.externallot";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_EXTERNLINENO = "screen.receiptdetail.field.externlineno";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_EXTERNRECEIPTKEY = "screen.receiptdetail.field.externreceiptkey";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_FORTE_FLAG = "screen.receiptdetail.field.forte_flag";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_GROSSWGT = "screen.receiptdetail.field.grosswgt";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_ID = "screen.receiptdetail.field.id";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_INNERPACK = "screen.receiptdetail.field.innerpack";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_IPSKEY = "screen.receiptdetail.field.ipskey";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_LOTTABLE01 = "screen.receiptdetail.field.lottable01";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_LOTTABLE02 = "screen.receiptdetail.field.lottable02";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_LOTTABLE03 = "screen.receiptdetail.field.lottable03";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_LOTTABLE04 = "screen.receiptdetail.field.lottable04";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_LOTTABLE05 = "screen.receiptdetail.field.lottable05";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_LOTTABLE06 = "screen.receiptdetail.field.lottable06";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_LOTTABLE07 = "screen.receiptdetail.field.lottable07";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_LOTTABLE08 = "screen.receiptdetail.field.lottable08";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_LOTTABLE09 = "screen.receiptdetail.field.lottable09";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_LOTTABLE10 = "screen.receiptdetail.field.lottable10";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_LOTTABLE11 = "screen.receiptdetail.field.lottable11";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_LOTTABLE12 = "screen.receiptdetail.field.lottable12";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_MATCHLOTTABLE = "screen.receiptdetail.field.matchlottable";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_NETWGT = "screen.receiptdetail.field.netwgt";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_NOTES = "screen.receiptdetail.field.notes";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_OTHERUNIT1 = "screen.receiptdetail.field.otherunit1";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_OTHERUNIT2 = "screen.receiptdetail.field.otherunit2";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_PACKINGSLIPQTY = "screen.receiptdetail.field.packingslipqty";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_PACKKEY = "screen.receiptdetail.field.packkey";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_PALLET = "screen.receiptdetail.field.pallet";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_PALLETID = "screen.receiptdetail.field.palletid";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_POKEY = "screen.receiptdetail.field.pokey";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_POLINENUMBER = "screen.receiptdetail.field.polinenumber";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_QCAUTOADJUST = "screen.receiptdetail.field.qcautoadjust";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_QCQTYINSPECTED = "screen.receiptdetail.field.qcqtyinspected";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_QCQTYREJECTED = "screen.receiptdetail.field.qcqtyrejected";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_QCREJREASON = "screen.receiptdetail.field.qcrejreason";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_QCREQUIRED = "screen.receiptdetail.field.qcrequired";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_QCSTATUS = "screen.receiptdetail.field.qcstatus";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_QCUSER = "screen.receiptdetail.field.qcuser";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_QTYADJUSTED = "screen.receiptdetail.field.qtyadjusted";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_QTYEXPECTED = "screen.receiptdetail.field.qtyexpected";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_QTYRECEIVED = "screen.receiptdetail.field.qtyreceived";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_QTYREJECTED = "screen.receiptdetail.field.qtyrejected";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_REASONCODE = "screen.receiptdetail.field.reasoncode";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_RECEIPTDETAILID = "screen.receiptdetail.field.receiptdetailid";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_RECEIPTKEY = "screen.receiptdetail.field.receiptkey";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_RECEIPTLINENUMBER = "screen.receiptdetail.field.receiptlinenumber";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_RETURNCONDITION = "screen.receiptdetail.field.returncondition";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_RETURNREASON = "screen.receiptdetail.field.returnreason";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_RETURNTYPE = "screen.receiptdetail.field.returntype";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_RMA = "screen.receiptdetail.field.rma";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_SERIALKEY = "screen.receiptdetail.field.serialkey";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_SKU = "screen.receiptdetail.field.sku";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_STATUS = "screen.receiptdetail.field.status";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_STORERKEY = "screen.receiptdetail.field.storerkey";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_SUPPLIERKEY = "screen.receiptdetail.field.supplierkey";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_SUPPLIERNAME = "screen.receiptdetail.field.suppliername";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_SUSR1 = "screen.receiptdetail.field.susr1";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_SUSR2 = "screen.receiptdetail.field.susr2";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_SUSR3 = "screen.receiptdetail.field.susr3";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_SUSR4 = "screen.receiptdetail.field.susr4";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_SUSR5 = "screen.receiptdetail.field.susr5";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_TARIFFKEY = "screen.receiptdetail.field.tariffkey";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_TOID = "screen.receiptdetail.field.toid";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_TOLOC = "screen.receiptdetail.field.toloc";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_TOLOT = "screen.receiptdetail.field.tolot";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_TYPE = "screen.receiptdetail.field.type";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_UNITPRICE = "screen.receiptdetail.field.unitprice";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_UOM = "screen.receiptdetail.field.uom";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_VESSELKEY = "screen.receiptdetail.field.vesselkey";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_VOYAGEKEY = "screen.receiptdetail.field.voyagekey";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_WHSEID = "screen.receiptdetail.field.whseid";
	public static final String KEY_SCREEN_RECEIPTDETAIL_FIELD_XDOCKKEY = "screen.receiptdetail.field.xdockkey";
	
	public static final String KEY_RECEIPTDETAIL_SCREEN_ERROR_DUPLICATE_RECEIPT="receipt.screen.error.duplicate.receipt";
	public static final String KEY_SCREEN_ITEM_FIELD_DAPICKSORT = "screen.item.field.dapicksort";
	public static final String KEY_SCREEN_ITEM_FIELD_RPLNSORT = "screen.item.field.rplnsort";
	public static final String KEY_SCREEN_OWNER_FIELD_REQREASONSHORTSHIP = "screen.owner.field.reqreasonshortship";
	public static final String KEY_SCREEN_OWNER_FIELD_EXPLODELPNLENGTH = "screen.owner.field.explodelpnlength";
	public static final String KEY_SCREEN_OWNER_FIELD_CONTAINEREXCHANGEFLAG = "screen.owner.field.containerexchangeflag";
	public static final String KEY_SCREEN_OWNER_FIELD_FLOWTHRUTASKCONTROL = "screen.owner.field.flowthrutaskcontrol";
	public static final String KEY_SCREEN_OWNER_FIELD_FLOWTHRULABELPRINTCONTROL = "screen.owner.field.flowthrulabelprintcontrol";
	public static final String KEY_SCREEN_OWNER_FIELD_CARTONIZEFLOWTHRUORDERSDEFAULT = "screen.owner.field.cartonizeflowthruordersdefault";
	public static final String KEY_SCREEN_ITEM_FIELD_CARTONIZEFT = "screen.item.field.cartonizeft";
	
	//SRG: 9.2 Upgrade -- Start
	//Owner
	public static final String KEY_SCREEN_OWNER_FIELD_ADDRESS5 = "screen.owner.field.address5";
	public static final String KEY_SCREEN_OWNER_FIELD_ADDRESS6 = "screen.owner.field.address6";
	public static final String KEY_SCREEN_OWNER_FIELD_ADDRESSOVERWRITEINDICATOR = "screen.owner.field.addressoverwriteindicator";
	public static final String KEY_SCREEN_OWNER_FIELD_ARCORP = "screen.owner.field.arcorp";
	public static final String KEY_SCREEN_OWNER_FIELD_ARDEPT = "screen.owner.field.ardept";
	public static final String KEY_SCREEN_OWNER_FIELD_ARACCT = "screen.owner.field.aracct";
	public static final String KEY_SCREEN_OWNER_FIELD_MEASURECODE = "screen.owner.field.measurecode";
	public static final String KEY_SCREEN_OWNER_FIELD_WGTUOM = "screen.owner.field.wgtuom";
	public static final String KEY_SCREEN_OWNER_FIELD_DIMENUOM = "screen.owner.field.dimenuom";
	public static final String KEY_SCREEN_OWNER_FIELD_CUBEUOM = "screen.owner.field.cubeuom";
	public static final String KEY_SCREEN_OWNER_FIELD_CURRCODE = "screen.owner.field.currcode";
	public static final String KEY_SCREEN_OWNER_FIELD_TAXEXEMPT = "screen.owner.field.taxexempt";
	public static final String KEY_SCREEN_OWNER_FIELD_TAXEXEMPTCODE = "screen.owner.field.taxexemptcode";
	public static final String KEY_SCREEN_OWNER_FIELD_RECURCODE = "screen.owner.field.recurcode";
	public static final String KEY_SCREEN_OWNER_FIELD_DUNSID = "screen.owner.field.dunsid";
	public static final String KEY_SCREEN_OWNER_FIELD_TAXID = "screen.owner.field.taxid";
	public static final String KEY_SCREEN_OWNER_FIELD_QFSURCHARGE = "screen.owner.field.qfsurcharge";
	public static final String KEY_SCREEN_OWNER_FIELD_BFSURCHARGE = "screen.owner.field.bfsurcharge";
	public static final String KEY_SCREEN_OWNER_FIELD_INVOICETERMS = "screen.owner.field.invoiceterms";
	public static final String KEY_SCREEN_OWNER_FIELD_INVOICELEVEL = "screen.owner.field.invoicelevel";
	public static final String KEY_SCREEN_OWNER_FIELD_NONNEGLEVEL = "screen.owner.field.nonneglevel";
	public static final String KEY_SCREEN_OWNER_FIELD_DEFFTTASKCONTROL = "screen.owner.field.deffttaskcontrol";
	public static final String KEY_SCREEN_OWNER_FIELD_DEFFTLABELPRINT = "screen.owner.field.defftlabelprint";
	public static final String KEY_SCREEN_OWNER_FIELD_CARTONIZEFTDFLT = "screen.owner.field.cartonizeftdflt";		
	public static final String KEY_SCREEN_OWNER_FIELD_AMSTRATEGYKEY = "screen.owner.field.amstrategykey";
	public static final String KEY_SCREEN_OWNER_FIELD_SPSACCOUNTNUM = "screen.owner.field.spsaccountnum";
	public static final String KEY_SCREEN_OWNER_FIELD_SPSCOSTCENTER = "screen.owner.field.spscostcenter";
	public static final String KEY_SCREEN_OWNER_FIELD_SPSRETURNLABEL = "screen.owner.field.spsreturnlabel";
	public static final String KEY_SCREEN_OWNER_FIELD_OWNERPREFIX = "screen.owner.field.ownerprefix";	
	public static final String KEY_SCREEN_OWNER_FIELD_EXPLODENEXTLPNNUMBER = "screen.owner.field.explodenextlpnnumber";
	public static final String KEY_SCREEN_OWNER_FIELD_EXPLODELPNROLLBACKNUMBER = "screen.owner.field.explodelpnrollbacknumber";
	public static final String KEY_SCREEN_OWNER_FIELD_EXPLODELPNSTARTNUMBER = "screen.owner.field.explodelpnstartnumber";
	public static final String KEY_SCREEN_OWNER_FIELD_ACCOUNTINGENTITY = "screen.owner.field.accountingentity";
	public static final String KEY_SCREEN_OWNER_FIELD_PARENT = "screen.owner.field.parent";
	public static final String KEY_ERROR_VALUE_MUST_BE_YES_OR_NO = "generic.error.value.must.be.yes.or.no";
	
	//Item
	public static final String KEY_SCREEN_ITEM_FIELD_NMFCCLASS = "screen.item.field.nmfcclass";
	public static final String KEY_SCREEN_ITEM_FIELD_MATEABILITYCODE = "screen.item.field.mateabilitycode";
	public static final String KEY_SCREEN_ITEM_FIELD_FILLQTYUOM = "screen.item.field.fillqtyuom";	
	public static final String KEY_SCREEN_ITEM_FIELD_IBSUMCWFLG = "screen.item.field.ibsumcwflg";
	public static final String KEY_SCREEN_ITEM_FIELD_OBSUMCWFLG = "screen.item.field.obsumcwflg";
	public static final String KEY_SCREEN_ITEM_FIELD_SHOWRFCWONTRANS = "screen.item.field.showrfcwontrans";	
	public static final String KEY_SCREEN_ITEM_FIELD_RECURCODE = "screen.item.field.recurcode";
	public static final String KEY_SCREEN_ITEM_FIELD_WGTUOM = "screen.item.field.wgtuom";
	public static final String KEY_SCREEN_ITEM_FIELD_DIMENUOM = "screen.item.field.dimenuom";
	public static final String KEY_SCREEN_ITEM_FIELD_CUBEUOM = "screen.item.field.cubeuom";
	public static final String KEY_SCREEN_ITEM_FIELD_STORAGETYPE = "screen.item.field.storagetype";
	public static final String KEY_SCREEN_ITEM_FIELD_AUTORELEASELPNBY = "screen.item.field.autoreleaselpnby";
	public static final String KEY_SCREEN_ITEM_FIELD_HOURSTOHOLDLPN = "screen.item.field.hourstoholdlpn";
	public static final String KEY_SCREEN_ITEM_FIELD_LOTHOLDCODE = "screen.item.field.lotholdcode";
	public static final String KEY_SCREEN_ITEM_FIELD_AUTORELEASELOTBY = "screen.item.field.autoreleaselotby";
	public static final String KEY_SCREEN_ITEM_FIELD_HOURSTOHOLDLOT = "screen.item.field.hourstoholdlot";
	public static final String KEY_SCREEN_ITEM_FIELD_AMSTRATEGYKEY = "screen.item.field.amstrategykey";
	public static final String KEY_SCREEN_ITEM_FIELD_PUTAWAYCLASS = "screen.item.field.paclass";
	public static final String KEY_SCREEN_ITEM_FIELD_TEMPFORASN = "screen.item.field.tempforasn";
	
	//Customer Screen
	public static final String KEY_SCREEN_SHIPTO_FIELD_STORERKEY = "screen.shipto.field.storerkey";
	public static final String KEY_SHIPTO_SCREEN_ERROR_DUPLICATE_SHIPTO = "shipto.screen.error.duplicate.shipto";
	//Ship From Screen
	public static final String KEY_SCREEN_SHIPFROM_FIELD_STORERKEY = "screen.shipfrom.field.storerkey";
	public static final String KEY_SHIPFROM_SCREEN_ERROR_DUPLICATE_SHIPFROM = "shipfrom.screen.error.duplicate.shipfrom";
	
	//Freight Bill To Screen
	public static final String KEY_SCREEN_FREIGHTBILLTO_FIELD_STORERKEY = "screen.freightbillto.field.storerkey";
	public static final String KEY_FREIGHTBILLTO_SCREEN_ERROR_DUPLICATE_FREIGHTBILLTO = "freightbillto.screen.error.duplicate.freightbillto";
	
	//Owner Bill To Screen
	public static final String KEY_SCREEN_OWNERBILLTO_FIELD_STORERKEY = "screen.ownerbillto.field.storerkey";
	public static final String KEY_OWNERBILLTO_SCREEN_ERROR_DUPLICATE_OWNERBILLTO = "ownerbillto.screen.error.duplicate.ownerbillto";
	//SRG: 9.2 Upgrade -- End
}


