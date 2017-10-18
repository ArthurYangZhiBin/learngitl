package com.infor.scm.waveplanning.common;

public final class WavePlanningConstants {
   
	
	public WavePlanningConstants()
	{
	}
	public static final String STYLESHEET = "/SSAGlobal/WebTop/skins/default/default.css";
	public static final String IMAGES = "/SSAGlobal/WebTop/skins/default/images/";	
	public static final String JAVASCRIPT = "/SSAGlobal/WebTop/js/waveplanning.js";
	public static final int LIST_PAGE_SIZE = 10;	
	public static final String REQUEST_ACTION_MODIFY = "Modify";
	public static final String REQUEST_ACTION_QUERY = "Query";
	public static final String REQUEST_ACTION_DOWNLOAD_ORDERS = "downloadorders";
	public static final String REQUEST_ACTION_WAVE_LIST = "waveslist";
	public static final String REQUEST_ACTION_WAVE_ADD_ORDERS = "addorderswave";
	public static final String REQUEST_ACTION_WAVE_REMOVE_ORDERS = "removeorderswave";
	public static final String REQUEST_ACTION_SELECT_FILTER = "selectfilter";
	public static final String REQUEST_ACTION_SELECTED_FILTER = "selectedfilter";
	public static final String REQUEST_ACTION_ORDER_EDIT = "orderedit";
	public static final String REQUEST_ACTION_ORDER_LINE_EDIT = "orderlineedit";
	public static final String REQUEST_ACTION_WAVE_ACTION = "waveaction";
	public static final String REQUEST_ACTION_WAVE_VIEW = "waveview";
	public static final String REQUEST_ACTION_MASS_UPDATE = "massupdate";
	public static final String REQUEST_ACTION_MASS_UPDATE_ORDERS = "massupdateorders";
	public static final String REQUEST_ACTION_GOTO_FILTER = "gotofilter";
	public static final String REQUEST_ACTION_EDIT_HEADER = "EditHeader";
	public static final String REQUEST_ACTION_EDIT_DETAIL = "EditDetail";	
	public static final String REQUEST_ACTION = "action";
	public static final String REQUEST_ACTION_RELOAD = "Reload";
	public static final String REQUEST_ACTION_NEW = "New";
	public static final String REQUEST_ACTION_ADD = "Add";
	public static final String FROM               = "FROM";
	public static final String REQUEST_ACTION_INSERT = "Insert";
	public static final String REQUEST_ACTION_EDIT = "Edit";
	public static final String REQUEST_ACTION_UPDATE = "Update";
	public static final String REQUEST_ACTION_DELETE = "Delete";
	public static final String REQUEST_ACTION_LIST = "List";
	public static final String REQUEST_ACTION_REMOVE = "Remove";
	public static final String REQUEST_ACTION_DETAILS = "Details";
	public static final String REQUEST_ACTION_SAVE = "save";
	public static final String REQUEST_ACTION_SELECT = "select";
	public static final String REQUEST_ACTION_REFRESH = "refresh";
	public static final String REQUEST_ACTION_DISPLAY = "Display";
	public static final String REQUEST_ACTION_ENTER = "Enter";
	public static final String REQUEST_ACTION_UNDO = "Undo";
	public static final String REQUEST_ACTION_APPLY = "Apply";
	public static final String REQUEST_ACTION_SCHEDULE = "Schedule";
	public static final String REQUEST_ACTION_ORDER_LIST = "orderslist";
	public static final String REQUEST_ACTION_TASK_WAVE = "taskListforWave";
	public static final String REQUEST_ACTION_TASK_ORDER = "taskListforOrder";
	public static final String REQUEST_ACTION_TASK_DETAIL_FORWAVE = "taskDetailforWave";
	public static final String REQUEST_ACTION_TASK_DETAIL_FORORDER = "taskDetailforOrder";	
	public static final String REQUEST_ACTION_WAVE_SUMMARY = "waveSummary";
	public static final String REQUEST_ACTION_WAVE_STATUS = "waveStatus";
	public static final String REQUEST_ACTION_WAVE_ORDER_STATUS = "waveOrderStatus";
	public static final String REQUEST_ACTION_SHIPMENT_ORDERS = "shipmentOrders";	
	public static final String REQUEST_ACTION_DETAIL_TASKDETAILKEY="taskDetailKey";//venkat
	public static final String REQUEST_ACTION_DETAIL_PREVIOUSTASK="pickprevioustask";//venkat
	public static final String REQUEST_ACTION_DETAIL_NEXTTASK="picknexttask";//venkat
	public static final String REQUEST_ACTION_OPERATION="operation";//venkat
	public static final String REQUEST_ACTION_TASKDETAIL="taskdetail";//venkat
	public static final String REQUEST_ACTION_INVENTORYDETAIL="inventorydetail";//venkat
	public static final String REQUEST_ACTION_TASK_DETAIL = "taskDetail";
	public static final String REQUEST_ACTION_INVENTORY_DETAIL = "inventoryDetail";	
	public static final String REQUEST_ACTION_FILTER_DETAIL = "filterDetail";	
	public static final String REQUEST_ACTION_NOT_STARTED = "Not Started";
	public static final String REQUEST_ACTION_NOT_STARTED_DETAILS = "notStartedDetails";
	public static final String REQUEST_ACTION_PRE_ALLOCATED = "Pre-Allocated";
	public static final String REQUEST_ACTION_PRE_ALLOCATED_DETAILS = "preAllocatedDetails";
	public static final String REQUEST_ACTION_PRE_ALLOCATED_LOTTABLE = "preAllocatedLottable";
	public static final String REQUEST_ACTION_ALLOCATED_DETAILS = "allocatedDetails";
	public static final String REQUEST_ACTION_ALLOCATED_LOTTABLE = "allocatedLottable";
	public static final String REQUEST_ACTION_PICKED_DETAILS = "pickedDetails";
	public static final String REQUEST_ACTION_PICKED_LOTTABLE = "pickedLottable";
	public static final String REQUEST_ACTION_SHIPPED_DETAILS = "shippedDetails";
	public static final String REQUEST_ACTION_SHIPPED_LOTTABLE = "shippedLottable";				
	public static final String REQUEST_ACTION_ALLOCATED = "Allocated";
	public static final String REQUEST_ACTION_PICKED = "Picked";
	public static final String REQUEST_ACTION_SHIPPED = "Shipped";
	public static final String REQUEST_ACTION_INPROCESS = "Inprocess";
	public static final String REQUEST_ACTION_COMPLETE = "Complete";
	public static final String REQUEST_ACTION_NOT_SHIPPED = "NotShipped";
	public static final String REQUEST_ACTION_INPROCESS_DETAILS = "inProcessDetails";
	public static final String REQUEST_ACTION_COMPLETE_DETAILS = "completeDetails";
	public static final String REQUEST_ACTION_NOT_SHIPPED_DETAILS = "notShippedDetails";
	public static final String REQUEST_ACTION_NOT_STARTED_LOTTABLE = "notStartedLottable";
	public static final String REQUEST_ACTION_ALL_NOTSHIPPED_SKUS = "allNotShippedSkus";
	public static final String REQUEST_ACTION_REASONCODE_DETAILS = "reasonCodebyNSO";
	public static final String REQUEST_ACTION_VIEWALL_ORDERS = "viewAllOrders";
	public static final String REQUEST_ACTION_NOT_SHIPPEDSKUSBY_REASONCODE = "notShippedSkusbyReasonCode";
	public static final String REQUEST_ACTION_NOT_SHIPPEDSKUAND_REASONCODE = "notShippedSkuandReasonCode";	
	public static final String REASON_CODE = "reasonCode";
	public static final String DISPLAY_TYPE="displayType";
	public static final String DISPLAY_CONTENT="displayContent";	
	public static final String ERROR_MESSAGES = "ErrorMessages";
	public static final String STACK_TRACE = "StackTrace";	
	public static final String FILTER_ID = "filterId";
	public static final String FROM_DATE = "fromDate";
	public static final String TO_DATE = "toDate";
	public static final String STATUS_PARAM = "statusParam";
	public static final String FILTER_TYPE = "type";
	public static final String WHICH_PAGE = "Page";	
	public static final String FILTER_ADD = "/orderfilter/addfilter.do";
	public static final String FILTER_SAVE = "/orderfilter/savefilter.do";
	public static final String FILTER_EDIT_HEADER = "/orderfilter/editfilterheader.do";	
	public static final String FILTER_EDIT = "/orderfilter/editfilter.do";
	public static final String FILTER_UPDATE = "/orderfilter/updatefilter.do";
	public static final String FILTER_DELETE = "/orderfilter/deletefilter.do";
	public static final String QUERY_FILTER = "/orderfilter/queryfilter.do";	
	public static final String SQL_FILTER = "/orderfilter/sqlfilter.do";
	public static final String FILTER_SCHEDULE = "/orderfilter/schedulefilter.do";
	public static final String SHIPMENTORDERS_LIST_EDIT_FILTER = "/editfiltertoorderfilter/shipmentorderslist.do";
	public static final String SHIPMENTORDERS_LIST = "/orderfilter/shipmentorderslist.do";
	public static final String SHCEDULEDFILTERS = "/scheduledFilters.do";
	public static final String SCHEDULERWAITTIME_SAVE = "/schedulerWaitTime.do";
	public static final String SCHEDULERWAITTIME = "/schedulerWaitTime.do";	
	public static final String TASKLIST_BY_WAVE = "/taskList.do";
	public static final String TASKLIST_BY_ORDER = "/taskList.do";
	public static final String TASK_DETAIL = "/taskList.do";
	public static final String WAVESTATUS = "/waveStatus.do";
	public static final String SCHEDULED_TASKS = "/scheduledTasks.do";
	public static final String FACILITY_CONFIG = "/facility.do";
	public static final String APP_CONFIG = "/appConfig.do";
	public static final String CONNECTIVITY_CONFIG = "/connectConfig.do";
	public static final String LABORDATA_CONFIG = "/laborConfig.do";	
	public static final String FILTERS_LIST = "FILTERSLIST";
	public static final String FILTER_VO = "FILTERVO";
	public static final String FILTER_LIST = "/orderfilter/filterslist.do";
	public static final String GOTO_FILTER = "/orderfilter/gotofilter.do";
	public static final String FILTER_HEADER = "/orderfilter/filterheader.do";
	public static final String STATUS = "status";
	public static final String ORDER_KEY = "orderKey";
	public static final String WAVE_ID = "waveKey";
	public static final String ORDER_ID = "orderKey";
	public static final String SKU = "sku";
	public static final String DESCRIPTION = "description";
	public static final String ORDER_LINE_NO = "orderLineNo";
	public static final String SHIPMENT_ORDERS_LIST = "SHIPMENTORDERSLIST";
	public static final String TOTAL_ORDERS_LIST = "TOTALLIST";
	public static final String SHIPMENTORDER_LIST = "/wavemgmt/shipmentorderslist.do";
	public static final String WAVE_STATUS = "status";
	public static final String WAVE_STATUS_TOBECONFIRMED = "To be Confirmed";
	public static final String WAVE_STATUS_CONFIRMED = "Confirmed";
	public static final String WAVE_STATUS_MODIFIED = "Modified";
	public static final String WAVES_LIST = "WAVESLIST";
	public static final String WAVE_DETAILS = "WAVEDETAILS";
	public static final String ORDER_DOWNLOAD = "/wavemgmt/orderdownload.do";
	public static final String DOWNLOAD_ORDERS = "/wavemgmt/downloadorders.do";
	public static final String WAVE_CREATE = "/wavemgmt/createwave.do";
	public static final String WAVE_LIST = "/wavemgmt/waveslist.do";
    public static final String CONFIRM_WAVE_LIST = "/wavemgmt/confirmwaveslist.do";
    public static final String MAINTENANCE_WAVE_LIST = "/wavemgmt/maintenancewaveslist.do";	
	public static final String WAVE_EDIT = "/wavemgmt/editwave.do";
	public static final String WAVE_DELETE = "/wavemgmt/deletewave.do";
	public static final String WAVE_UPDATE = "/wavemgmt/updatewave.do";
	public static final String MASS_UPDATE_ALL = "updateall";
	public static final String MASS_UPDATE_BLANKS = "updateblanks";
	public static final String WAVE_MASS_UPDATE = "/wavemgmt/massupdate.do";
	public static final String WAVE_MASS_UPDATE_ORDERS = "/wavemgmt/massupdateorders.do";
	public static final String WAVE_CONFIRM = "/wavemgmt/confirm.do";
	public static final String WAVE_ACTION = "/wavemgmt/waveaction.do";
	public static final String WAVE_VIEW = "/wavemgmt/waveview.do";
	public static final String WAVE_ADD_ORDERS = "/wavemgmt/addorderstowave.do";
	public static final String WAVE_REMOVE_ORDERS = "/wavemgmt/removeordersfromwave.do";
	public static final String SELECT_FILTER = "/wavemgmt/selectfilter.do";
	public static final String SELECTED_FILTER = "/wavemgmt/selectedfilter.do";
	public static final String ORDER_DETAILS = "ORDERDETAILS";
	public static final String ORDER_LINE_DETAILS = "ORDERLINEDETAILS";
	public static final String ORDER_EDIT = "/wavemgmt/editorder.do";
	public static final String ORDER_UPDATE = "/wavemgmt/updateorder.do";	
	public static final String ORDER_LINE_EDIT = "/wavemgmt/editorderline.do";
	public static final String CRITERIA_LIST = "/orderfilter/criterialist.do";
	public static final String DISPLAY_CHART = "/displaychart.do";
	public static final String GRAPHICAL_HEADER = "/orderfilter/graphicalheader.do";
	public static final String GRAPHICAL_FILTER_ADD = "/graphicalfilter/addfilter.do";
	public static final String GRAPHICAL_SHIPMENTORDERS_LIST = "/graphicalfilter/shipmentorderslist.do";
	public static final String WAVE_SUMMARY = "/wavemgmt/wavesummary.do";
	public static final int WAVE_STATUSES_TOBECONFIRMED = 1; 
	public static final int WAVE_STATUSES_MODIFIED = 2;
	public static final int WAVE_STATUSES_CONFIRMED = 3;	
	public static final int WAVE_INPROCESS_COMPLETED = 0;
	public static final int WAVE_INPROCESS_CONFIRM = 1;
	public static final int WAVE_INPROCESS_CONSOLIDATE = 2;
	public static final int WAVE_INPROCESS_UNCONSOLIDATE = 3;
	public static final int WAVE_INPROCESS_PREALLOCATE = 4;
	public static final int WAVE_INPROCESS_ALLOCATE = 5;
	public static final int WAVE_INPROCESS_UNALLOCATE = 6;
	public static final int WAVE_INPROCESS_RELEASE = 7;
	public static final int WAVE_INPROCESS_SHIP = 8;

	//HC.b - Added new status to update the status of the wave correctly.
	public static final int WAVE_STATUSES_ALLOCATED = 4;
	public static final int WAVE_STATUSES_RELEASED = 5;
	public static final int WAVE_STATUSES_SHIPPED = 6;
	//HC.e

	public static final int WAVE_CONFIRMED = 10;
	public static final int WAVE_CONSOLIDATED = 20;
	public static final int WAVE_UNCONSOLIDATED = 30;
	public static final int WAVE_PREALLOCATED = 40;
	public static final int WAVE_ALLOCATED = 50;
	public static final int WAVE_UNALLOCATED = 60;
	public static final int WAVE_RELEASED = 70;
	public static final int WAVE_SHIPPED = 80;
 	public static final int WAVE_MASTERSCHEDULE = 210;
	public static final int WAVE_ASSIGNDOOR = 220;

	
	public static final int WAVE_CONFIRM_FAILED = -1;
	public static final int WAVE_CONSOLIDATE_FAILED = -2;
	public static final int WAVE_UNCONSOLIDATE_FAILED = -3;
	public static final int WAVE_PREALLOCATE_FAILED = -4;
	public static final int WAVE_ALLOCATE_FAILED = -5;
	public static final int WAVE_UNALLOCATE_FAILED = -6;
	public static final int WAVE_RELEASE_FAILED = -7;
	public static final int WAVE_SHIP_FAILED = -8;
	public static final int WAVE_MASTERSCHEDULE_FAILED = -21;
	public static final int WAVE_ASSIGNDOOR_FAILED = -22;



	// Scheduler Constants Start
	public static final String dateFormatyyyyMMdd = "yyyy-MM-dd";
	public static final String dateFormatMMddyyyy = "yyyy-MM-dd";
	public static final String MONDAY = "Monday";
	public static final String TUESDAY = "Tuesday";
	public static final String WEDNESDAY = "Wednesday";
	public static final String THURSDAY = "Thursday";
	public static final String FRIDAY = "Friday";
	public static final String SATURDAY = "Saturday";
	public static final String SUNDAY = "Sunday";
	public static final String SCH_FILTERID = "FILTERID";
	public static final String SCH_FILTERNAME = "FILTERNAME";
	public static final String SCH_TASKID = "TASKID";
	public static final String SCH_PROCESSID = "PROCESSID";
	public static final String SCH_EXECUTIONTIME = "EXECUTIONTIME";
	public static final String SCH_EXECUTED = "EXECUTED";
	public static final String SCH_FROMDATE = "FROMDATE";
	public static final String SCH_TODATE = "TODATE";
	public static final String SCH_SERIALKEY = "SERIALKEY";
	public static final String SCH_EXECUTIONDAY = "EXECUTIONDAY";
	// Scheduler Constants End
		
	/* Home Page start */
	public static final String  alert_16_imagepath="/SSAGlobal/WebTop/skins/default/images/Alert_16.gif";
	public static final String  filter_imagepath="/SSAGlobal/WebTop/skins/default/images/filter_GIF.gif";
	public static final String  wavemanagement_imagepath="/SSAGlobal/WebTop/skins/default/images/wavemanagement_GIF.gif";
	public static final String  reports_imagepath="/SSAGlobal/WebTop/skins/default/images/reports_GIF.gif";
	public static final String  alerts_imagepath="/SSAGlobal/WebTop/skins/default/images/alerts_GIF.gif";
	public static final String  configuration_imagepath="/SSAGlobal/WebTop/skins/default/images/configuration_GIF.gif";
	public static final String  operations_imagepath="/SSAGlobal/WebTop/skins/default/images/operations_GIF.gif";	
	/* Home Page End */

	/* Reports Start */
    
    public static final String REPORTS_REQUEST_ACTION ="reports_action";
    public static final int    REPORTS_PAGE_SIZE=1;
    public static final String PRINT="/SSAGlobal/WebTop/skins/default/images/print.gif";
    public static final String SSALOGO="/SSAGlobal/WebTop/skins/default/images/ssalogo1.gif";
    public static final String HELP="/SSAGlobal/WebTop/skins/default/images/help-bt.gif";
    public static final String SELECTION_CRITERIA="/selectionCriteria.do";
    public static final String PRINT_CRITERIA="/printCriteria.do";
    public static final String SHOW_REPORT="/showReport.do";
    public static final String GENERATE_REPORT_FOR_CASELABEL="/generateReportForCaseLabels.do";
    public static final String REQUEST_ACTION_SELECTION_CRITERIA="selectionCriteria";
    public static final String REQUEST_ACTION_PRINT_CRITERIA="printCriteria";
    public static final String REQUEST_ACTION_SHOW_REPORT="showReport";
    public static final String REQUEST_ACTION_GENERATE_REPORT="generateReport";
    public static final String REQUEST_ACTION_CASELABELS="caseLabels";
    public static final String REQUEST_ACTION_ASSISTEDPICKTICKET="assistedPickTicket";
    public static final String REQUEST_ACTION_DIRECTEDPICKTICKET="directedPickTicket";
    public static final String REQUEST_ACTION_PICKLIST="pickList";
    public static final String REQUEST_ACTION_PACKSLIP="packSlip";
    public static final String REQUEST_ACTION_COMMERCIALINVOICE="commercialInvoice";
    public static final String REQUEST_ACTION_CATCHWEIGHTDATALIST="catchWeightDataList";
    public static final String REQUEST_ACTION_CATCHWEIGHTDATAPACKSLIP="catchWeightDataPackSlip";
    public static final String REQUEST_ACTION_CURRENTPAGE="currentPage";
    public static final String ALERT="/SSAGlobal/WebTop/skins/default/images/alert.gif";
    public static final String MESSAGE="/SSAGlobal/WebTop/skins/default/images/message.gif";
    public static final String INFORMATION="/SSAGlobal/WebTop/skins/default/images/information.gif";    
    public static final String zoombutton_next="/SSAGlobal/WebTop/skins/default/images/Next-dis.gif";
    public static final String zoombutton_prev="/SSAGlobal/WebTop/skins/default/images/Prev-dis.gif";
    
    /* Reports End */
    // Personalization
    public static final String PERSONALIZATION_IMAGEPATH="/SSAGlobal/WebTop/skins/default/images/customize_columns.gif";
	
	// Engine start
	public static final int ENGINE_METHOD_WAVE_CONFIRM 						= 1000;
	public static final int ENGINE_METHOD_WAVE_CONSOLIDATE 					= 1001;
	public static final int ENGINE_METHOD_WAVE_UNCONSOLIDATE 				= 1002;
	public static final int ENGINE_METHOD_WAVE_PREALLOCATE 					= 1003;
	public static final int ENGINE_METHOD_WAVE_ALLOCATE 					= 1004;
	public static final int ENGINE_METHOD_WAVE_UNALLOCATE 					= 1005;
	public static final int ENGINE_METHOD_WAVE_RELEASE 						= 1006;
	public static final int ENGINE_METHOD_WAVE_SHIP 						= 1007;
	public static final int ENGINE_METHOD_WAVE_MASSUPDATE 					= 1008;
	public static final int ENGINE_METHOD_WAVE_DELETE 						= 1009;
	public static final int ENGINE_METHOD_SHIPMENT_DOWNLOAD_ALL_4000		= 1010;
	public static final int ENGINE_METHOD_SHIPMENT_DOWNLOAD_ALL_2000		= 1011;
	public static final int ENGINE_METHOD_SHIPMENT_ORDER_SYNCHRONISE_4000	= 1012;
	public static final int ENGINE_METHOD_SHIPMENT_ORDER_SYNCHRONISE_2000	= 1013;
	public static final int ENGINE_METHOD_SHIPMENT_ORDER_DOWNLOAD_ALL		= 1014;
	public static final int ENGINE_METHOD_SKU_SYNCHRONISE					= 1015;
	public static final int ENGINE_METHOD_SKU_DOWNLOAD_ALL					= 1016;
	public static final int ENGINE_METHOD_ASSIGN_DOOR  = 1017;
	public static final int ENGINE_METHOD_GET_MASTER_SCHEDULE  = 1018;
	
	//Engine End
	
	// john latest addition
	public static final String DEFAULT_JAVASCRIPT = "/SSAGlobal/WebTop/skins/default/default.js";
	public static final String FORMELEMENT_JAVASCRIPT = "/SSAGlobal/WebTop/skins/default/formElements.js";

	public static final String PERSONALIZATION_TABLE = "/wavemgmt/personalization.do";
	public static final String PERSONALIZE_TABLE = "/wavemgmt/personalize.do";
	public static final String PERSONALIZATION_UPDATE = "/wavemgmt/updatepersonalization.do";
	public static final String REQUEST_ACTION_PERSONALIZATION = "personalization";
	public static final String REQUEST_ACTION_PERSONALIZE = "personalize";
	public static final String REQUEST_ACTION_UPDATE_PERSONALIZATION = "updatePersonalization";
	public static final String REQUEST_ACTION_PERSONALIZE_ACTION = "personalizeaction";
	public static final String ORDERHEADER_SELECT_FIELDS = "distinct wp_orderheader.serialkey, wp_orderheader.whseid, " +
															"wp_orderheader.orderkey, " +
															"wp_orderheader.dc_id, wp_orderheader.segmentid, " +
															"wp_orderheader.storerkey, wp_orderheader.externorderkey, " +
															"wp_orderheader.orderdate, wp_orderheader.deliverydate, " +
															"wp_orderheader.priority, wp_orderheader.customer, " +
															"wp_orderheader.door, wp_orderheader.route, " +
															"wp_orderheader.stop, wp_orderheader.status, " +
															"wp_orderheader.carrier, wp_orderheader.countryoforigin, " +
															"wp_orderheader.countrydestination, " +
															"wp_orderheader.ordergroup, wp_orderheader.stage, " +
															"wp_orderheader.transportationmode, " +
															"wp_orderheader.transportationservice, " +
															"wp_orderheader.loadid, wp_orderheader.requestedshipdate, " +
															"wp_orderheader.actualshipdate, wp_orderheader.rfidflag, " +
															"wp_orderheader.carriercode, wp_orderheader.carriername, " +
															"wp_orderheader.shipmentordertype, wp_orderheader.totalqty, " +
															"wp_orderheader.totalgrosswgt, wp_orderheader.totalnetwgt, " +
															"wp_orderheader.totalcube, wp_orderheader.totalorderlines, " +
															"wp_orderheader.state, wp_orderheader.zipcode, " +
															"wp_orderheader.company, wp_orderheader.invoicenumber, " +
															"wp_orderheader.totalunits, wp_orderheader.totalcasesship, " +
															"wp_orderheader.split_orders, wp_orderheader.createuser, " +
															"wp_orderheader.batchflag, wp_orderheader.batch, " +
															"wp_orderheader.sequence, wp_orderheader.checkpointid";


	public static final String ORDERLINE_SELECT_FIELDS = "distinct wp_orderline.whseid, wp_orderline.orderkey, " +
														 "wp_orderline.orderlinenumber, wp_orderline.dc_id, " +
														 "wp_orderline.segmentid, wp_orderline.storerkey, " +
														 "wp_orderline.sku, " +
														 "wp_orderline.originalqty, wp_orderline.openqty, " +
														 "wp_orderline.shippedqty, wp_orderline.qtypreallocated, " +
														 "wp_orderline.qtyallocated, wp_orderline.qtypicked, " +
														 "wp_orderline.uom, wp_orderline.packkey, wp_orderline.cartongroup, " +
														 "wp_orderline.lot, wp_orderline.status, wp_orderline.unitprice, " +
														 "wp_orderline.lottable01, wp_orderline.lottable02, " +
														 "wp_orderline.lottable03, wp_orderline.lottable04, " +
														 "wp_orderline.lottable05, wp_orderline.lottable06, " +
														 "wp_orderline.lottable07, wp_orderline.lottable08, " +
														 "wp_orderline.lottable09, wp_orderline.lottable10, " +
														 "wp_orderline.oktosubstitute, wp_orderline.actualshipdate, " +
														 "wp_orderline.carrier, wp_orderline.shipgroup1, " +
														 "wp_orderline.shipgroup2, wp_orderline.shipgroup3, " +
														 "wp_orderline.ordersplitflag, wp_orderline.ordertype, " +
														 "wp_orderline.commoditytype, wp_orderline.primselloc, " +
														 "wp_orderline.secselloc, wp_orderline.primshipunits, " +
														 "wp_orderline.secshipunits, wp_orderline.ecrflag, " +
														 "wp_orderline.buyrsvflag, wp_orderline.picktype, " +
														 "wp_orderline.whsecost, wp_orderline.retailprice, " +
														 "wp_orderline.retailcost, wp_orderline.priceadjustedflag, " +
														 "wp_orderline.subtype, wp_orderline.outofstockqty, " +
														 "wp_orderline.markoutqty, wp_orderline.shipqty, " +
														 "wp_orderline.shipunitscase, wp_orderline.pickgroupid";
	
    public static final String REQUEST_ACTION_UPDATE_ORDER = "updateorder";
    public static final String WHSE_ID = "whseId";
    public static final String SEGMENT_ID = "segmentId";
    public static final String SERIAL_KEY = "serialKey";
    public static final String WP_FACILITY = "wp_facility";
    public static final int WAVE_INPROCESS_MASTERSCHEDULE = 21;
	public static final int WAVE_INPROCESS_ASSIGNDOOR = 22;
	// john latest addition
	// venki latest
    public static final String FACILITYID="facilityID";
    public static final String REQUEST_ACTION_FACILITYCONFIGURATIONLIST="facilityConfigurationList";
    public static final String REQUEST_ACTION_FACILITYCONFIGURATIONDETAILS="facilityConfigurationDetails";
    public static final String REQUEST_ACTION_ADDFACILITYCONFIGURATIONDETAILS="addFacilityConfigurationDetails";
    public static final String REQUEST_ACTION_REMOVEFACILITYCONFIGURATIONDETAILS="removeFacilityConfigurationDetails";
    public static final String REQUEST_ACTION_SAVEFACILITYCONFIGURATIONDETAILS="saveFacilityConfigurationDetails";
    public static final String REQUEST_ACTION_UPDATEFACILITYCONFIGURATIONDETAILS="updateFacilityConfigurationDetails";
    public static final String FACILITYCONFIGURATIONLIST="/connectConfig.do";
    public static final String FACILITYCONFIGURATIONDETAILS="/facilityConfigurationDetails.do";
    public static final String ADDFACILITYCONFIGURATIONDETAILS="/addFacilityConfigurationDetails.do";
    public static final String REMOVEFACILITYCONFIGURATIONDETAILS="/removeFacility.do";
    public static final String SAVEFACILITYCONFIGURATIONDETAILS="/saveFacilityConfigurationDetails.do";
    public static final String UPDATEFACILITYCONFIGURATIONDETAILS="/updateFacilityConfigurationDetails.do";
    public static final int SHIPMENT_ORDER_STATUS_SEQ = 1;
	public static final int SHIPMENT_ORDER_STATUS_TRP = 2;
	public static final int SHIPMENT_ORDER_STATUS_HLD = 3;
	public static final int SHIPMENT_ORDER_STATUS_SPL = 4;
	public static final int SHIPMENT_ORDER_STATUS_CAN = 5;
	public static final int SHIPMENT_ORDER_STATUS_REL = 21;
	public static final int SHIPMENT_ORDER_STATUS_REX = 22;
	public static final int SHIPMENT_ORDER_STATUS_EXT = 23;
	public static final int SHIPMENT_ORDER_STATUS_PRT = 24;
	public static final int SHIPMENT_ORDER_STATUS_BND = 25;
	public static final int SHIPMENT_ORDER_STATUS_ARC = 26;
	public static final int SHIPMENT_ORDER_STATUS_CLO = 95;
	public static final String DOWNLOAD_ALL = "All";
	public static final String DOWNLOAD_NEW = "New";
	public static final String WMS_2000 = "SSAWMS2000";
	public static final String WMS_4000 = "SSAWMS4000";
	public static final String PROCESS_CONFIRM = "Confirm";
	public static final String PROCESS_PREALLOCATE = "Preallocate";
	public static final String PROCESS_ALLOCATE = "Allocate";
	public static final String PROCESS_UNALLOCATE = "Unallocate";
	public static final String PROCESS_TOCONSOLIDATE = "ToConsolidate";
	public static final String PROCESS_CONSOLIDATE = "Consolidate";
	public static final String PROCESS_UNCONSOLIDATE = "Unconsolidate";
	public static final String PROCESS_RELEASE = "Release";
	public static final String PROCESS_SHIP = "Ship";
	public static final String PROCESS_MASTERSCHEDULE = "MasterSchedule";
	public static final String PROCESS_ASSIGNDOOR = "AssignDoor";
	
	public static final String HTML_TABLE = "htmlTable";
	public static final String JSP_PAGE = "jspPage";
	
	public static final String ORDER_COUNT = "ordercount";
	public static final String ITEM_COUNT = "itemcount";	
	public static final String APP_CONFIG_NAME = "waveplanningConfig";
	public static final String WMS_API_DATE_FORMAT = "wmsapidateformat";	
   
    
    /**
     * Creating the instace of Global.
     * Self instance, Singleton pattern with public access.
     */
     public static WavePlanningConstants global = new WavePlanningConstants();
     /**
     * Creating the instace of SC Date Format constants.
     *  Singleton pattern with public access.
     */
     public static WPDateFormat dateFormats = global.new WPDateFormat();
     /**
     * This is an inner class which holds format of dates.
     * DATE_TIME : yyyy'-'MM'-'dd'T'HH':'mm':'ssZ (2005-02-14T17:34:46+0530)
     * DATE_ONLY : yyyy'-'MM'-'dd' (2005-02-14)
     * @see                        
     */
     public class WPDateFormat {
            public final String DATE_TIME  = "yyyy-MM-dd HH:mm";
            public final String DATE_ONLY  = "yyyy-MM-dd";
            public final String DATE_VIEW  = "MM/dd/yyyy";
     }


    public static final String ASSIGN_DOOR = "/wavemgmt/assigndoor.do";
    public static final String REQUEST_ACTION_ASSIGN_DOOR = "assigndoor";
    public static final String DOOR_GROUP = "doorGroup";
	public static final String REQUEST_ACTION_DOWNLOAD_SKUS = "downloadskus";
	public static final String SKU_DOWNLOAD = "/wavemgmt/skudownload.do";
	public static final String DOWNLOAD_SKUS = "/wavemgmt/downloadskus.do";

	public static final int ENGINE_METHOD_SKU_SYNCHRONISE_4000				= 1015;
	public static final int ENGINE_METHOD_SKU_DOWNLOAD_ALL_4000				= 1016;
	public static final int ENGINE_METHOD_SKU_SYNCHRONISE_2000				= 1017;
	public static final int ENGINE_METHOD_SKU_DOWNLOAD_ALL_2000				= 1018;


	// DF# 108609.n
	public static final String NO_SORT = "NoSort";
	public static final String QUERY_FILTER_UPDATE = "/orderfilter/updatequeryfilter.do";
	public static final String SQL_FILTER_UPDATE = "/orderfilter/updatesqlfilter.do";
	
    	public static final String HELP_PAGE = "helpPage";
    	public static final String ONLINE_HELP = "/help.do";
    	public static final String REQUEST_ACTION_OUTOF_STOCK_REPORT = "viewOutOfStockReport";
    	public static final String ALERTS_LIST = "/alertsList.do";
		  public static final String REQUEST_ACTION_DELETE_ALERTS = "deleteAlerts";


    public static final String TASK_STATUS_PENDING = "Pending";
    public static final String TASK_STATUS_INPROCESS = "InProcess";
    public static final String TASK_STATUS_AWAITING_SEQUENCE = "Awaiting Sequence";
    public static final String TASK_STATUS_COMPLETED = "Completed";
    public static final String TASK_STATUS_HELD_BY_USER = "Held By User";
    public static final String TASK_STATUS_REJECTED = "Rejected";
    public static final String TASK_STATUS_HELD_BY_SYSTEM = "Held By System";
    public static final String TASK_STATUS_CANCELLED = "Cancelled";
    
    public static final String TASK_TYPE_CC = "Cycle Counts";
    public static final String TASK_TYPE_CO = "Consolidations";
    public static final String TASK_TYPE_CR = "Cherry Pick Replenishment";
    public static final String TASK_TYPE_GM = "General Message";
    public static final String TASK_TYPE_LD = "Loading";
    public static final String TASK_TYPE_MV = "Moves";
    public static final String TASK_TYPE_OM = "Optimize Move";
    public static final String TASK_TYPE_PA = "Putaways";
    public static final String TASK_TYPE_PIA = "Physical Inventory A Count";
    public static final String TASK_TYPE_PIB = "Physical Inventory B Count";
    public static final String TASK_TYPE_PK = "Picks";
    public static final String TASK_TYPE_PP = "Paper Putaway";
    public static final String TASK_TYPE_QC = "Quality Control/Inspection";
    public static final String TASK_TYPE_RP = "Replenishment";
    public static final String TASK_TYPE_SP = "Stocker Putaway";
    public static final String TASK_TYPE_WC = "Work Center Move";
    public static final String TASK_TYPE_XD = "Cross Docks";
    
    public static final String UOM_1 = "Full Pallet";
    public static final String UOM_2 = "Full Case";
    public static final String UOM_3 = "InnerPack";
    public static final String UOM_4 = "Other1";
    public static final String UOM_5 = "Other2";
    public static final String UOM_6 = "Piece/Each";
    public static final String UOM_7 = "Piece/Each(Special)";
    public static final String UOM_8 = "Any Special";
    
    public static final String HP = "Highest Priority";
    public static final String MP = "Medium Priority";
	
	public static final	String 	DB_TYPE_STRING_MSSQL				= "MSSQL";
	public static final	String 	DB_TYPE_STRING_ORACLE				= "ORACLE";
	public static final	String 	DB_TYPE_STRING_INFORMIX				= "INFORMIX";
	public static final	String 	DB_TYPE_STRING_DB2					= "DB2";
	public static final String  REMOTELINK_NAME                     = "REMOTELINK";
	
	public static final String COOKIE_WM_FACILITYDETAILS            = "WM_FACILITYDETAILS";	//QA20070108
	public static final String COOKIE_WM_FACILITYDETAILS_DELIM      = "~~";					//QA20070108
	public static final String COOKIE_WM_DEFAULT_OWNERS             = "WM_DEFAULTOWNER";    //Subodh: New Enhancement
}
