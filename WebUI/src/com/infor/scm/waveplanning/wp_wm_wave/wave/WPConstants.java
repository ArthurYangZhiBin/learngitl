/**
 * ---Begin Copyright Notice---20090105T1353
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2010 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */
package com.infor.scm.waveplanning.wp_wm_wave.wave;

public class WPConstants {
	//for wave action
	public static final String 	CREATE_WAVE="CREATEWAVE";
	public static final String 	GET_CONSOLIDATED_SKU="GETCONSOLIDATEDSKU";
	public static final String 	CONSOLIDATE_WAVE="CONSOLIDATEWAVE";
	public static final String 	UNCONSOLIDATE_WAVE="UNCONSOLIDATEWAVE";
	public static final String 	PREALLOCATE_WAVE="PREALLOCATEWAVE";
	public static final String 	ALLOCATE_WAVE="ALLOCATEWAVE";
	public static final String 	UNALLOCATE_WAVE="UNALLOCATEWAVE";
	public static final String 	RELEASE_WAVE="RELEASEWAVE";
	public static final String 	SHIP_WAVE="SHIPWAVE";
	public static final String 	MASS_UPDATE_ALL="MASSUPDATEALL";
	
	
	
    public static final String DB_CONNECTION = "dbConnectionName";
    public static final String INTERNAL_CALLER = "internalCall";
    
    
 // wave store procedure name
    public static final String CREATEWAVE = "NSPCREATEWAVE";
    public static final String CONSOLIDATE = "ASSIGNCONSOLIDATEDLOC";
    public static final String UNCONSOLIDATE = "NSPUNCONSOLIDATE";
    public static final String PREALLOCATE = "NSPPREALLOCATEWAVE";
    public static final String ALLOCATE = "NSPRELEASEWAVE";
    public static final String UNALLOCATE = "NSPUNALLOCATEWAVE";
    public static final String RELEASE = "NSPRELEASEWAVE";
    public static final String SHIP = "NSPSHIPWAVE";   
    public static final String MASSUPDATEALL = "NSPMASSUPDATEALL";
 
	
	
	//for wave function
	public static final String BATCH_ORDERS = "BATCHORDERS";
	public static final String UNBATCH_ORDERS = "UNBATCHORDERS";
	public static final String WORK_ORDER_ELIGIBLE_BY_ORDER  = "WORKORDER_ELIGIBLE_BY_ORDER";
	public static final String WORK_ORDER_ELIGIBLE_BY_ITEM  = "WORKORDER_ELIGIBLE_BY_ITEM";

	
	//for label printing
	public static final String CASE_LABELS = "CASELABELS";
	public static final String CARRIER_COMPLIANT_LABELS = "CARRIER_COMPLIANT_LABELS";
	public static final String ASSIGNMENT_LABELS = "ASSIGNMENTLABELS";
	public static final String BATCH_PICK_LABELS = "BATCH_PICK_LABELS";
	public static final String PICK_TICKETS = "PICK_TICKETS";
	public static final String PICK_LISTS = "PICK_LISTS";
	public static final String ORDER_SHEETS = "ORDER_SHEETS";
	public static final String PACKING_LIST = "PACKING_LIST";
	
	
	//for DB Server Type.
	public static final String DB_TYPE_ORACLE = "O90";
	public static final String DB_TYPE_SQL = "MSS";
	
	
	//for ship wave
	public static final String IGNORE_INCOMPLETE_TASKS="pendingtaskdetails";
	public static final String IGNORE_UNSORTED_BATCH_PICKS="unsortedpickcount";
	
	
	//Store procedure name for wave order summary
	public static final String GETWAVEORDERSUMMARY = "NSPGETORDERSUMMARY";
	
	//store procedure name for get eligible sku for consolidate
	public static final String GETCONDOLIDATEDSKU = "GETCONSOLIDATEDSKU";
	
	
	//validation constants
	public static final int VALIDATION_PASSED = -1;
	public static final int NO_ORDER_TO_CREATE_WAVE = 0;
	public static final int NO_ORDER_TO_PREALLOCATE = 1;
	public static final int NO_ORDER_TO_ALLOCATE = 2;
	public static final int NO_ORDER_TO_UNALLOCATE = 3;
	public static final int NO_ORDER_TO_RELEASE = 4;
	public static final int NO_ORDER_TO_SHIP = 5;
	
	
	//wave allocated, picked, taskdetail delete 
	public static final String 	DELETE_ALLOCATED = "PICKDETAIL";
	public static final String 	DELETE_PICKED = "PICKDETAIL";
	public static final String 	DELETE_TASKDETAIL = "TASKDETAIL";
	public static final String 	DELETE_BATCHMOVE = "BATCHMOVE";
	
      

}
