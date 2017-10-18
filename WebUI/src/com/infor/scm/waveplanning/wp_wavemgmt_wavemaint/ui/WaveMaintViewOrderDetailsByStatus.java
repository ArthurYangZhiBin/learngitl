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

package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.UUID;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.date.DateUtil;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.infor.scm.waveplanning.common.util.BioAttributeUtil;
import com.infor.scm.waveplanning.common.util.WPUserUtil;
import com.infor.scm.waveplanning.common.util.WPUtil;
import com.infor.scm.waveplanning.wp_wavemgmt.util.WPWaveMgmtUtil;
import com.infor.scm.waveplanning.common.WavePlanningConstants;
import com.infor.scm.waveplanning.common.WavePlanningUtils;
//import com.ssaglobal.scm.waveplanning.wavemgmt.bd.WaveBD;
//import com.ssaglobal.scm.waveplanning.wavemgmt.vo.WaveStatusVO;
//import com.ssaglobal.scm.waveplanning.wavemgmt.vo.WaveVO;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class WaveMaintViewOrderDetailsByStatus extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WaveMaintViewOrderDetailsByStatus.class);

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @exception EpiException
	 */
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
/*

		//Navigations
		final String notstartedNavigation = getParameterString("NOTSTARTEDNAV");//"clickEvent1386";
		final String preallocatedNavigation = getParameterString("PREALLOCNAV");//"clickEvent1388";
		final String allocatedNavigation = getParameterString("ALLOCNAV");//"clickEvent1389";

		if (status == 0) //Not Started
		{
			_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewOrderDetailsByStatus_execute", "Starting Not Started",
					SuggestedCategory.NONE);
			try
			{
				WaveVO waveVO = waveBD.getWaveDetails(waveid);
				if (wmsName.equalsIgnoreCase(WavePlanningConstants.WMS_2000))
				{
					waveVO.setDcID(Short.parseShort(facility));
				}
				//List of NotStarted Orderlines
				ArrayList notStartedList = waveBD.getNotStartedWaveStatusDetails(facility, waveVO);

				//Synchronize Step
				removeExtraOrderLines(waveid, status, facility, interactionID, uow, notStartedList);

				//insert or update records in wp_wavestatusvotemp
				for (int i = 0; i < notStartedList.size(); i++)
				{
					WaveStatusVO orderLine = (WaveStatusVO) notStartedList.get(i);

					String orderKey = orderLine.getOrderKey();
					String orderLineNo = orderLine.getOrderLineNo();
					//should return 1 record
					BioCollectionBean rs = uow.getBioCollectionBean(new Query("wp_wavestatusvotemp",
							"wp_wavestatusvotemp.WAVEKEY = '" + waveid + "' and wp_wavestatusvotemp.STATUS = '"
									+ status + "' and wp_wavestatusvotemp.ORDERKEY = '" + orderKey
									+ "' and wp_wavestatusvotemp.ORDERLINENO = '" + orderLineNo
									+ "' and wp_wavestatusvotemp.WHSEID ='" + facility
									+ "' and wp_wavestatusvotemp.INTERACTIONID = '" + interactionID + "'", null));

					if (rs.size() == 0)
					{
						//insert new record
						_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewOrderDetailsByStatus_execute",
								"Inserting New Record Into WaveStatusVOTemp " + waveid + " " + status + " "
										+ facility + " " + orderKey + " " + orderLineNo, SuggestedCategory.NONE);
						
						QBEBioBean orderLineQBE = uow.getQBEBioWithDefaults("wp_wavestatusvotemp");

						orderLineQBE.set("INTERACTIONID", interactionID);
						orderLineQBE.set("SERIALKEY", UUID.randomUUID().toString());

						orderLineQBE.set("WAVEKEY", new Integer(waveid));
						orderLineQBE.set("STATUS", new Integer(status));
						orderLineQBE.set("WHSEID", facility);
						orderLineQBE.set("ORDERKEY", orderKey);
						orderLineQBE.set("ORDERLINENO", orderLineNo);

						orderLineQBE.set("SKU", orderLine.getSku());
						orderLineQBE.set("SKUDESCRIPTION", orderLine.getSkuDescription());
						orderLineQBE.set("STORERKEY", orderLine.getStorerKey());
						orderLineQBE.set("ORDEREDQTY", orderLine.getOrderedQty());
						orderLineQBE.set("OPENQTY", orderLine.getOpenQty());
						orderLineQBE.set("SHIPPEDQTY", orderLine.getShippedQty());
						orderLineQBE.set("ADJUSTEDQTY", orderLine.getAdjustedQty());
						orderLineQBE.set("PREQTY", orderLine.getPreQty());
						orderLineQBE.set("ALLOCATEDQTY", orderLine.getAllocatedQty());
						orderLineQBE.set("PICKEDQTY", orderLine.getPickedQty());
						orderLineQBE.set("UOM", orderLine.getUom());
						orderLineQBE.set("PACKKEY", orderLine.getPackKey());
						orderLineQBE.set("PICKCODE", orderLine.getPickCode());
						orderLineQBE.set("CARTONGROUP", orderLine.getCartonGroup());
						orderLineQBE.set("LOT", orderLine.getLot());
						orderLineQBE.set("FACILITY", orderLine.getFacility());
						orderLineQBE.set("OSTATUS", orderLine.getStatus());
						orderLineQBE.set("UOMQTY", orderLine.getUomQty());
						orderLineQBE.set("OADDDATE", orderLine.getAddDate());

						orderLineQBE.set("ADDDATE", currentDate);
						orderLineQBE.set("ADDWHO", userId);
						orderLineQBE.set("EDITDATE", currentDate);
						orderLineQBE.set("EDITWHO", userId);
						orderLineQBE.save();

					}
					else
					{
						_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewOrderDetailsByStatus_execute",
								"Updating Existing Record Into WaveStatusVOTemp " + waveid + " " + status + " "
										+ facility + " " + orderKey + " " + orderLineNo, SuggestedCategory.NONE);
						//update existing record
						for (int j = 0; j < rs.size(); j++)
						{
							BioBean orderLineBio = rs.get("" + j);
							orderLineBio.set("SKU", orderLine.getSku());
							orderLineBio.set("SKUDESCRIPTION", orderLine.getSkuDescription());
							orderLineBio.set("STORERKEY", orderLine.getStorerKey());
							orderLineBio.set("ORDEREDQTY", orderLine.getOrderedQty());
							orderLineBio.set("OPENQTY", orderLine.getOpenQty());
							orderLineBio.set("SHIPPEDQTY", orderLine.getShippedQty());
							orderLineBio.set("ADJUSTEDQTY", orderLine.getAdjustedQty());
							orderLineBio.set("PREQTY", orderLine.getPreQty());
							orderLineBio.set("ALLOCATEDQTY", orderLine.getAllocatedQty());
							orderLineBio.set("PICKEDQTY", orderLine.getPickedQty());
							orderLineBio.set("UOM", orderLine.getUom());
							orderLineBio.set("PACKKEY", orderLine.getPackKey());
							orderLineBio.set("PICKCODE", orderLine.getPickCode());
							orderLineBio.set("CARTONGROUP", orderLine.getCartonGroup());
							orderLineBio.set("LOT", orderLine.getLot());
							orderLineBio.set("FACILITY", orderLine.getFacility());
							orderLineBio.set("OSTATUS", orderLine.getStatus());
							orderLineBio.set("UOMQTY", orderLine.getUomQty());
							orderLineBio.set("OADDDATE", orderLine.getAddDate());

							orderLineBio.set("EDITDATE", currentDate);
							orderLineBio.set("EDITWHO", userId);
							orderLineBio.save();

						}

					}

				}

			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			context.setNavigation(notstartedNavigation);

		}
		else if (status == 1) //Pre-Allocated
		{
			_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewOrderDetailsByStatus_execute", "Starting Pre Allocated",
					SuggestedCategory.NONE);
			try
			{
				WaveVO waveVO = waveBD.getWaveDetails(waveid);
				//List of preallocated orderlines
				ArrayList preAllocatedList = waveBD.getPreAllocatedWaveStatusDetails(facility, waveVO);

				//Synchronize Step
				removeExtraOrderLines(waveid, status, facility, interactionID, uow, preAllocatedList);

				for (int i = 0; i < preAllocatedList.size(); i++)
				{
					WaveStatusVO orderLine = (WaveStatusVO) preAllocatedList.get(i);

					String orderKey = orderLine.getOrderKey();
					String orderLineNo = orderLine.getOrderLineNo();
					BioCollectionBean rs = uow.getBioCollectionBean(new Query("wp_wavestatusvotemp",
							"wp_wavestatusvotemp.WAVEKEY = '" + waveid + "' and wp_wavestatusvotemp.STATUS = '"
									+ status + "' and wp_wavestatusvotemp.ORDERKEY = '" + orderKey
									+ "' and wp_wavestatusvotemp.ORDERLINENO = '" + orderLineNo
									+ "' and wp_wavestatusvotemp.WHSEID ='" + facility
									+ "' and wp_wavestatusvotemp.INTERACTIONID = '" + interactionID + "'", null));


					if (rs.size() == 0)
					{
						_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewOrderDetailsByStatus_execute",
								"Inserting New Record Into WaveStatusVOTemp " + waveid + " " + status + " "
										+ facility + " " + orderKey + " " + orderLineNo, SuggestedCategory.NONE);
						//insert new record
						QBEBioBean orderLineQBE = uow.getQBEBioWithDefaults("wp_wavestatusvotemp");

						orderLineQBE.set("INTERACTIONID", interactionID);
						orderLineQBE.set("SERIALKEY", UUID.randomUUID().toString());

						orderLineQBE.set("WAVEKEY", new Integer(waveid));
						orderLineQBE.set("STATUS", new Integer(status));
						orderLineQBE.set("WHSEID", facility);
						orderLineQBE.set("ORDERKEY", orderKey);
						orderLineQBE.set("ORDERLINENO", orderLineNo);

						orderLineQBE.set("SKU", orderLine.getSku());
						orderLineQBE.set("SKUDESCRIPTION", orderLine.getSkuDescription());
						orderLineQBE.set("STORERKEY", orderLine.getStorerKey());
						orderLineQBE.set("ORDEREDQTY", orderLine.getOrderedQty());
						orderLineQBE.set("OPENQTY", orderLine.getOpenQty());
						orderLineQBE.set("SHIPPEDQTY", orderLine.getShippedQty());
						orderLineQBE.set("ADJUSTEDQTY", orderLine.getAdjustedQty());
						orderLineQBE.set("PREQTY", orderLine.getPreQty());
						orderLineQBE.set("ALLOCATEDQTY", orderLine.getAllocatedQty());
						orderLineQBE.set("PICKEDQTY", orderLine.getPickedQty());
						orderLineQBE.set("UOM", orderLine.getUom());
						orderLineQBE.set("PACKKEY", orderLine.getPackKey());
						orderLineQBE.set("PICKCODE", orderLine.getPickCode());
						orderLineQBE.set("CARTONGROUP", orderLine.getCartonGroup());
						orderLineQBE.set("LOT", orderLine.getLot());
						orderLineQBE.set("FACILITY", orderLine.getFacility());
						orderLineQBE.set("OSTATUS", orderLine.getStatus());
						orderLineQBE.set("UOMQTY", orderLine.getUomQty());
						orderLineQBE.set("SHIPGROUP1", orderLine.getShipGroup1());
						orderLineQBE.set("SHIPGROUP2", orderLine.getShipGroup2());
						orderLineQBE.set("SHIPGROUP3", orderLine.getShipGroup3());
						orderLineQBE.set("LOTTABLE1", orderLine.getLottable1());
						orderLineQBE.set("LOTTABLE2", orderLine.getLottable2());
						orderLineQBE.set("LOTTABLE3", orderLine.getLottable3());
						orderLineQBE.set("LOTTABLE4", orderLine.getLottable4());
						orderLineQBE.set("LOTTABLE5", orderLine.getLottable5());
						orderLineQBE.set("LOTTABLE6", orderLine.getLottable6());
						orderLineQBE.set("LOTTABLE7", orderLine.getLottable7());
						orderLineQBE.set("LOTTABLE8", orderLine.getLottable8());
						orderLineQBE.set("LOTTABLE9", orderLine.getLottable9());
						orderLineQBE.set("LOTTABLE10", orderLine.getLottable10());
						orderLineQBE.set("PRESTGY", orderLine.getPreStgy());
						orderLineQBE.set("ALLOCATESTRATEGYKEY", orderLine.getAllocateStrategyKey());
						orderLineQBE.set("OADDDATE", orderLine.getAddDate());

						orderLineQBE.set("ADDDATE", currentDate);
						orderLineQBE.set("ADDWHO", userId);
						orderLineQBE.set("EDITDATE", currentDate);
						orderLineQBE.set("EDITWHO", userId);
						orderLineQBE.save();
					}
					else
					{
						_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewOrderDetailsByStatus_execute",
								"Updating Existing Record Into WaveStatusVOTemp " + waveid + " " + status + " "
										+ facility + " " + orderKey + " " + orderLineNo, SuggestedCategory.NONE);
						//update existing record
						for (int j = 0; j < rs.size(); j++)
						{
							BioBean orderLineBio = rs.get("" + i);

							orderLineBio.set("SKU", orderLine.getSku());
							orderLineBio.set("SKUDESCRIPTION", orderLine.getSkuDescription());
							orderLineBio.set("STORERKEY", orderLine.getStorerKey());
							orderLineBio.set("ORDEREDQTY", orderLine.getOrderedQty());
							orderLineBio.set("OPENQTY", orderLine.getOpenQty());
							orderLineBio.set("SHIPPEDQTY", orderLine.getShippedQty());
							orderLineBio.set("ADJUSTEDQTY", orderLine.getAdjustedQty());
							orderLineBio.set("PREQTY", orderLine.getPreQty());
							orderLineBio.set("ALLOCATEDQTY", orderLine.getAllocatedQty());
							orderLineBio.set("PICKEDQTY", orderLine.getPickedQty());
							orderLineBio.set("UOM", orderLine.getUom());
							orderLineBio.set("PACKKEY", orderLine.getPackKey());
							orderLineBio.set("PICKCODE", orderLine.getPickCode());
							orderLineBio.set("CARTONGROUP", orderLine.getCartonGroup());
							orderLineBio.set("LOT", orderLine.getLot());
							orderLineBio.set("FACILITY", orderLine.getFacility());
							orderLineBio.set("OSTATUS", orderLine.getStatus());
							orderLineBio.set("UOMQTY", orderLine.getUomQty());
							orderLineBio.set("SHIPGROUP1", orderLine.getShipGroup1());
							orderLineBio.set("SHIPGROUP2", orderLine.getShipGroup2());
							orderLineBio.set("SHIPGROUP3", orderLine.getShipGroup3());
							orderLineBio.set("LOTTABLE1", orderLine.getLottable1());
							orderLineBio.set("LOTTABLE2", orderLine.getLottable2());
							orderLineBio.set("LOTTABLE3", orderLine.getLottable3());
							orderLineBio.set("LOTTABLE4", orderLine.getLottable4());
							orderLineBio.set("LOTTABLE5", orderLine.getLottable5());
							orderLineBio.set("LOTTABLE6", orderLine.getLottable6());
							orderLineBio.set("LOTTABLE7", orderLine.getLottable7());
							orderLineBio.set("LOTTABLE8", orderLine.getLottable8());
							orderLineBio.set("LOTTABLE9", orderLine.getLottable9());
							orderLineBio.set("LOTTABLE10", orderLine.getLottable10());
							orderLineBio.set("PRESTGY", orderLine.getPreStgy());
							orderLineBio.set("ALLOCATESTRATEGYKEY", orderLine.getAllocateStrategyKey());
							orderLineBio.set("OADDDATE", orderLine.getAddDate());

							orderLineBio.set("EDITDATE", currentDate);
							orderLineBio.set("EDITWHO", userId);
							orderLineBio.save();
						}
					}
				}
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			context.setNavigation(preallocatedNavigation);

		}
		else
		{

			if (status == 2) //Allocated
			{
				_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewOrderDetailsByStatus_execute", "Starting  Allocated",
						SuggestedCategory.NONE);
				try
				{
					WaveVO waveVO = waveBD.getWaveDetails(waveid);
					//List of allocated orderlines
					ArrayList allocatedList = waveBD.getAllocatedWaveStatusDetails(facility, waveVO);

					//Synchronize Step
					removeExtraOrderLines(waveid, status, facility, interactionID, uow, allocatedList);

					for (int i = 0; i < allocatedList.size(); i++)
					{
						WaveStatusVO orderLine = (WaveStatusVO) allocatedList.get(i);

						String orderKey = orderLine.getOrderKey();
						String orderLineNo = orderLine.getOrderLineNo();
						BioCollectionBean rs = uow.getBioCollectionBean(new Query("wp_wavestatusvotemp",
								"wp_wavestatusvotemp.WAVEKEY = '" + waveid + "' and wp_wavestatusvotemp.STATUS = '"
										+ status + "' and wp_wavestatusvotemp.ORDERKEY = '" + orderKey
										+ "' and wp_wavestatusvotemp.ORDERLINENO = '" + orderLineNo
										+ "' and wp_wavestatusvotemp.WHSEID ='" + facility
										+ "' and wp_wavestatusvotemp.INTERACTIONID = '" + interactionID + "'", null));

						if (rs.size() == 0)
						{
							_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewOrderDetailsByStatus_execute",
									"Inserting New Record Into WaveStatusVOTemp " + waveid + " " + status + " "
											+ facility + " " + orderKey + " " + orderLineNo, SuggestedCategory.NONE);
							//insert new record
							QBEBioBean orderLineQBE = uow.getQBEBioWithDefaults("wp_wavestatusvotemp");
							orderLineQBE.set("SERIALKEY", UUID.randomUUID().toString());

							orderLineQBE.set("INTERACTIONID", interactionID);

							orderLineQBE.set("WAVEKEY", new Integer(waveid));
							orderLineQBE.set("STATUS", new Integer(status));
							orderLineQBE.set("WHSEID", facility);
							orderLineQBE.set("ORDERKEY", orderKey);
							orderLineQBE.set("ORDERLINENO", orderLineNo);

							orderLineQBE.set("SKU", orderLine.getSku());
							orderLineQBE.set("SKUDESCRIPTION", orderLine.getSkuDescription());
							orderLineQBE.set("STORERKEY", orderLine.getStorerKey());
							orderLineQBE.set("ORDEREDQTY", orderLine.getOrderedQty());
							orderLineQBE.set("OPENQTY", orderLine.getOpenQty());
							orderLineQBE.set("SHIPPEDQTY", orderLine.getShippedQty());
							orderLineQBE.set("ADJUSTEDQTY", orderLine.getAdjustedQty());
							orderLineQBE.set("PREQTY", orderLine.getPreQty());
							orderLineQBE.set("ALLOCATEDQTY", orderLine.getAllocatedQty());
							orderLineQBE.set("PICKEDQTY", orderLine.getPickedQty());
							orderLineQBE.set("PICKCODE", orderLine.getPickCode());
							orderLineQBE.set("CARTONGROUP", orderLine.getCartonGroup());
							orderLineQBE.set("LOT", orderLine.getLot());
							orderLineQBE.set("FACILITY", orderLine.getFacility());
							orderLineQBE.set("OSTATUS", orderLine.getStatus());
							orderLineQBE.set("UOMQTY", orderLine.getUomQty());
							orderLineQBE.set("SHIPGROUP1", orderLine.getShipGroup1());
							orderLineQBE.set("SHIPGROUP2", orderLine.getShipGroup2());
							orderLineQBE.set("SHIPGROUP3", orderLine.getShipGroup3());
							orderLineQBE.set("PICKDETAILNO", orderLine.getPickDetailNo());
							orderLineQBE.set("CASEID", orderLine.getCaseId());
							orderLineQBE.set("DROPID", orderLine.getDropId());
							orderLineQBE.set("LOCATION", orderLine.getLocation());
							orderLineQBE.set("CARTONTYPE", orderLine.getCartonType());
							orderLineQBE.set("SEQUENCENO", orderLine.getSequenceNo());
							orderLineQBE.set("PACKKEY", orderLine.getPackKey());
							orderLineQBE.set("UOM", orderLine.getUom());
							orderLineQBE.set("DOCARTONIZE", orderLine.isDoCartonize());
							orderLineQBE.set("DOREPLENISH", orderLine.isDoReplenish());
							orderLineQBE.set("DROPLOCATION", orderLine.getDropLocation());
							orderLineQBE.set("REPLENISHZONE", orderLine.getReplenishZone());
							orderLineQBE.set("LOTTABLE1", orderLine.getLottable1());
							orderLineQBE.set("LOTTABLE2", orderLine.getLottable2());
							orderLineQBE.set("LOTTABLE3", orderLine.getLottable3());
							orderLineQBE.set("LOTTABLE4", orderLine.getLottable4());
							orderLineQBE.set("LOTTABLE5", orderLine.getLottable5());
							orderLineQBE.set("LOTTABLE6", orderLine.getLottable6());
							orderLineQBE.set("LOTTABLE7", orderLine.getLottable7());
							orderLineQBE.set("LOTTABLE8", orderLine.getLottable8());
							orderLineQBE.set("LOTTABLE9", orderLine.getLottable9());
							orderLineQBE.set("LOTTABLE10", orderLine.getLottable10());
							orderLineQBE.set("PRESTGY", orderLine.getPreStgy());
							orderLineQBE.set("ALLOCATESTRATEGYKEY", orderLine.getAllocateStrategyKey());

							orderLineQBE.set("ADDDATE", currentDate);
							orderLineQBE.set("ADDWHO", userId);
							orderLineQBE.set("EDITDATE", currentDate);
							orderLineQBE.set("EDITWHO", userId);
							orderLineQBE.save();
						}
						else
						{
							//update existing record
							_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewOrderDetailsByStatus_execute",
									"Updating Existing Record Into WaveStatusVOTemp " + waveid + " " + status + " "
											+ facility + " " + orderKey + " " + orderLineNo, SuggestedCategory.NONE);
							for (int j = 0; j < rs.size(); j++)
							{
								BioBean orderLineBio = rs.get("" + i);

								orderLineBio.set("SKU", orderLine.getSku());
								orderLineBio.set("SKUDESCRIPTION", orderLine.getSkuDescription());
								orderLineBio.set("STORERKEY", orderLine.getStorerKey());
								orderLineBio.set("ORDEREDQTY", orderLine.getOrderedQty());
								orderLineBio.set("OPENQTY", orderLine.getOpenQty());
								orderLineBio.set("SHIPPEDQTY", orderLine.getShippedQty());
								orderLineBio.set("ADJUSTEDQTY", orderLine.getAdjustedQty());
								orderLineBio.set("PREQTY", orderLine.getPreQty());
								orderLineBio.set("ALLOCATEDQTY", orderLine.getAllocatedQty());
								orderLineBio.set("PICKEDQTY", orderLine.getPickedQty());
								orderLineBio.set("PICKCODE", orderLine.getPickCode());
								orderLineBio.set("CARTONGROUP", orderLine.getCartonGroup());
								orderLineBio.set("LOT", orderLine.getLot());
								orderLineBio.set("FACILITY", orderLine.getFacility());
								orderLineBio.set("OSTATUS", orderLine.getStatus());
								orderLineBio.set("UOMQTY", orderLine.getUomQty());
								orderLineBio.set("SHIPGROUP1", orderLine.getShipGroup1());
								orderLineBio.set("SHIPGROUP2", orderLine.getShipGroup2());
								orderLineBio.set("SHIPGROUP3", orderLine.getShipGroup3());
								orderLineBio.set("PICKDETAILNO", orderLine.getPickDetailNo());
								orderLineBio.set("CASEID", orderLine.getCaseId());
								orderLineBio.set("DROPID", orderLine.getDropId());
								orderLineBio.set("LOCATION", orderLine.getLocation());
								orderLineBio.set("CARTONTYPE", orderLine.getCartonType());
								orderLineBio.set("SEQUENCENO", orderLine.getSequenceNo());
								orderLineBio.set("PACKKEY", orderLine.getPackKey());
								orderLineBio.set("UOM", orderLine.getUom());
								orderLineBio.set("DOCARTONIZE", orderLine.isDoCartonize());
								orderLineBio.set("DOREPLENISH", orderLine.isDoReplenish());
								orderLineBio.set("DROPLOCATION", orderLine.getDropLocation());
								orderLineBio.set("REPLENISHZONE", orderLine.getReplenishZone());
								orderLineBio.set("LOTTABLE1", orderLine.getLottable1());
								orderLineBio.set("LOTTABLE2", orderLine.getLottable2());
								orderLineBio.set("LOTTABLE3", orderLine.getLottable3());
								orderLineBio.set("LOTTABLE4", orderLine.getLottable4());
								orderLineBio.set("LOTTABLE5", orderLine.getLottable5());
								orderLineBio.set("LOTTABLE6", orderLine.getLottable6());
								orderLineBio.set("LOTTABLE7", orderLine.getLottable7());
								orderLineBio.set("LOTTABLE8", orderLine.getLottable8());
								orderLineBio.set("LOTTABLE9", orderLine.getLottable9());
								orderLineBio.set("LOTTABLE10", orderLine.getLottable10());
								orderLineBio.set("PRESTGY", orderLine.getPreStgy());
								orderLineBio.set("ALLOCATESTRATEGYKEY", orderLine.getAllocateStrategyKey());

								orderLineBio.set("EDITDATE", currentDate);
								orderLineBio.set("EDITWHO", userId);
								orderLineBio.save();
							}
						}
					}
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				context.setNavigation(allocatedNavigation);
			}
			else if (status == 3) //Picked
			{
				_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewOrderDetailsByStatus_execute", "Starting Picked",
						SuggestedCategory.NONE);
				try
				{
					WaveVO waveVO = waveBD.getWaveDetails(waveid);
					//List of picked orderlines
					ArrayList pickedList = waveBD.getPickedWaveStatusDetails(facility, waveVO);

					//Synchronize Step
					removeExtraOrderLines(waveid, status, facility, interactionID, uow, pickedList);

					for (int i = 0; i < pickedList.size(); i++)
					{
						WaveStatusVO orderLine = (WaveStatusVO) pickedList.get(i);

						String orderKey = orderLine.getOrderKey();
						String orderLineNo = orderLine.getOrderLineNo();

						final String query = "wp_wavestatusvotemp.WAVEKEY = '" + waveid
								+ "' and wp_wavestatusvotemp.STATUS = '" + status
								+ "' and wp_wavestatusvotemp.ORDERKEY = '" + orderKey
								+ "' and wp_wavestatusvotemp.ORDERLINENO = '" + orderLineNo
								+ "' and wp_wavestatusvotemp.WHSEID ='" + facility
								+ "' and wp_wavestatusvotemp.INTERACTIONID = '" + interactionID + "'";
						_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewOrderDetailsByStatus_execute",
								"Querying for WaveStatusVOTemp Record " + query, SuggestedCategory.NONE);
						BioCollectionBean rs = uow.getBioCollectionBean(new Query("wp_wavestatusvotemp", query, null));

						if (rs.size() == 0)
						{
							_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewOrderDetailsByStatus_execute",
									"Inserting New Record Into WaveStatusVOTemp " + waveid + " " + status + " "
											+ facility + " " + orderKey + " " + orderLineNo, SuggestedCategory.NONE);
							//insert new record
							QBEBioBean orderLineQBE = uow.getQBEBioWithDefaults("wp_wavestatusvotemp");

							orderLineQBE.set("INTERACTIONID", interactionID);
							orderLineQBE.set("SERIALKEY", UUID.randomUUID().toString());

							orderLineQBE.set("WAVEKEY", new Integer(waveid));
							orderLineQBE.set("STATUS", new Integer(status));
							orderLineQBE.set("WHSEID", facility);
							orderLineQBE.set("ORDERKEY", orderKey);
							orderLineQBE.set("ORDERLINENO", orderLineNo);

							orderLineQBE.set("SKU", orderLine.getSku());
							orderLineQBE.set("SKUDESCRIPTION", orderLine.getSkuDescription());
							orderLineQBE.set("STORERKEY", orderLine.getStorerKey());
							orderLineQBE.set("ORDEREDQTY", orderLine.getOrderedQty());
							orderLineQBE.set("OPENQTY", orderLine.getOpenQty());
							orderLineQBE.set("SHIPPEDQTY", orderLine.getShippedQty());
							orderLineQBE.set("ADJUSTEDQTY", orderLine.getAdjustedQty());
							orderLineQBE.set("PREQTY", orderLine.getPreQty());
							orderLineQBE.set("ALLOCATEDQTY", orderLine.getAllocatedQty());
							orderLineQBE.set("PICKEDQTY", orderLine.getPickedQty());
							orderLineQBE.set("PICKCODE", orderLine.getPickCode());
							orderLineQBE.set("CARTONGROUP", orderLine.getCartonGroup());
							orderLineQBE.set("LOT", orderLine.getLot());
							orderLineQBE.set("FACILITY", orderLine.getFacility());
							orderLineQBE.set("OSTATUS", orderLine.getStatus());
							orderLineQBE.set("UOMQTY", orderLine.getUomQty());
							orderLineQBE.set("SHIPGROUP1", orderLine.getShipGroup1());
							orderLineQBE.set("SHIPGROUP2", orderLine.getShipGroup2());
							orderLineQBE.set("SHIPGROUP3", orderLine.getShipGroup3());
							orderLineQBE.set("PICKDETAILNO", orderLine.getPickDetailNo());
							orderLineQBE.set("CASEID", orderLine.getCaseId());
							orderLineQBE.set("DROPID", orderLine.getDropId());
							orderLineQBE.set("LOCATION", orderLine.getLocation());
							orderLineQBE.set("CARTONTYPE", orderLine.getCartonType());
							orderLineQBE.set("SEQUENCENO", orderLine.getSequenceNo());
							orderLineQBE.set("PACKKEY", orderLine.getPackKey());
							orderLineQBE.set("UOM", orderLine.getUom());
							orderLineQBE.set("DOCARTONIZE", orderLine.isDoCartonize());
							orderLineQBE.set("DOREPLENISH", orderLine.isDoReplenish());
							orderLineQBE.set("DROPLOCATION", orderLine.getDropLocation());
							orderLineQBE.set("REPLENISHZONE", orderLine.getReplenishZone());
							orderLineQBE.set("LOTTABLE1", orderLine.getLottable1());
							orderLineQBE.set("LOTTABLE2", orderLine.getLottable2());
							orderLineQBE.set("LOTTABLE3", orderLine.getLottable3());
							orderLineQBE.set("LOTTABLE4", orderLine.getLottable4());
							orderLineQBE.set("LOTTABLE5", orderLine.getLottable5());
							orderLineQBE.set("LOTTABLE6", orderLine.getLottable6());
							orderLineQBE.set("LOTTABLE7", orderLine.getLottable7());
							orderLineQBE.set("LOTTABLE8", orderLine.getLottable8());
							orderLineQBE.set("LOTTABLE9", orderLine.getLottable9());
							orderLineQBE.set("LOTTABLE10", orderLine.getLottable10());
							orderLineQBE.set("PRESTGY", orderLine.getPreStgy());
							orderLineQBE.set("ALLOCATESTRATEGYKEY", orderLine.getAllocateStrategyKey());

							orderLineQBE.set("ADDDATE", currentDate);
							orderLineQBE.set("ADDWHO", userId);
							orderLineQBE.set("EDITDATE", currentDate);
							orderLineQBE.set("EDITWHO", userId);
							orderLineQBE.save();
						}
						else
						{
							//update existing record
							_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewOrderDetailsByStatus_execute",
									"Updating Existing Record Into WaveStatusVOTemp " + waveid + " " + status + " "
											+ facility + " " + orderKey + " " + orderLineNo, SuggestedCategory.NONE);
							for (int j = 0; j < rs.size(); j++)
							{
								BioBean orderLineBio = rs.get("" + i);

								orderLineBio.set("SKU", orderLine.getSku());
								orderLineBio.set("SKUDESCRIPTION", orderLine.getSkuDescription());
								orderLineBio.set("STORERKEY", orderLine.getStorerKey());
								orderLineBio.set("ORDEREDQTY", orderLine.getOrderedQty());
								orderLineBio.set("OPENQTY", orderLine.getOpenQty());
								orderLineBio.set("SHIPPEDQTY", orderLine.getShippedQty());
								orderLineBio.set("ADJUSTEDQTY", orderLine.getAdjustedQty());
								orderLineBio.set("PREQTY", orderLine.getPreQty());
								orderLineBio.set("ALLOCATEDQTY", orderLine.getAllocatedQty());
								orderLineBio.set("PICKEDQTY", orderLine.getPickedQty());
								orderLineBio.set("PICKCODE", orderLine.getPickCode());
								orderLineBio.set("CARTONGROUP", orderLine.getCartonGroup());
								orderLineBio.set("LOT", orderLine.getLot());
								orderLineBio.set("FACILITY", orderLine.getFacility());
								orderLineBio.set("OSTATUS", orderLine.getStatus());
								orderLineBio.set("UOMQTY", orderLine.getUomQty());
								orderLineBio.set("SHIPGROUP1", orderLine.getShipGroup1());
								orderLineBio.set("SHIPGROUP2", orderLine.getShipGroup2());
								orderLineBio.set("SHIPGROUP3", orderLine.getShipGroup3());
								orderLineBio.set("PICKDETAILNO", orderLine.getPickDetailNo());
								orderLineBio.set("CASEID", orderLine.getCaseId());
								orderLineBio.set("DROPID", orderLine.getDropId());
								orderLineBio.set("LOCATION", orderLine.getLocation());
								orderLineBio.set("CARTONTYPE", orderLine.getCartonType());
								orderLineBio.set("SEQUENCENO", orderLine.getSequenceNo());
								orderLineBio.set("PACKKEY", orderLine.getPackKey());
								orderLineBio.set("UOM", orderLine.getUom());
								orderLineBio.set("DOCARTONIZE", orderLine.isDoCartonize());
								orderLineBio.set("DOREPLENISH", orderLine.isDoReplenish());
								orderLineBio.set("DROPLOCATION", orderLine.getDropLocation());
								orderLineBio.set("REPLENISHZONE", orderLine.getReplenishZone());
								orderLineBio.set("LOTTABLE1", orderLine.getLottable1());
								orderLineBio.set("LOTTABLE2", orderLine.getLottable2());
								orderLineBio.set("LOTTABLE3", orderLine.getLottable3());
								orderLineBio.set("LOTTABLE4", orderLine.getLottable4());
								orderLineBio.set("LOTTABLE5", orderLine.getLottable5());
								orderLineBio.set("LOTTABLE6", orderLine.getLottable6());
								orderLineBio.set("LOTTABLE7", orderLine.getLottable7());
								orderLineBio.set("LOTTABLE8", orderLine.getLottable8());
								orderLineBio.set("LOTTABLE9", orderLine.getLottable9());
								orderLineBio.set("LOTTABLE10", orderLine.getLottable10());
								orderLineBio.set("PRESTGY", orderLine.getPreStgy());
								orderLineBio.set("ALLOCATESTRATEGYKEY", orderLine.getAllocateStrategyKey());

								orderLineBio.set("EDITDATE", currentDate);
								orderLineBio.set("EDITWHO", userId);
								orderLineBio.save();
							}
						}
					}
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				context.setNavigation(allocatedNavigation);
			}
			else if (status == 4) //Shipped
			{
				_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewOrderDetailsByStatus_execute", "Starting Shipped",
						SuggestedCategory.NONE);
				try
				{
					WaveVO waveVO = waveBD.getWaveDetails(waveid);
					//List of shipped orderlines
					ArrayList shippedList = waveBD.getShippedWaveStatusDetails(facility, waveVO);

					//Synchronize Step
					removeExtraOrderLines(waveid, status, facility, interactionID, uow, shippedList);

					for (int i = 0; i < shippedList.size(); i++)
					{
						WaveStatusVO orderLine = (WaveStatusVO) shippedList.get(i);

						String orderKey = orderLine.getOrderKey();
						String orderLineNo = orderLine.getOrderLineNo();
						BioCollectionBean rs = uow.getBioCollectionBean(new Query("wp_wavestatusvotemp",
								"wp_wavestatusvotemp.WAVEKEY = '" + waveid + "' and wp_wavestatusvotemp.STATUS = '"
										+ status + "' and wp_wavestatusvotemp.ORDERKEY = '" + orderKey
										+ "' and wp_wavestatusvotemp.ORDERLINENO = '" + orderLineNo
										+ "' and wp_wavestatusvotemp.WHSEID ='" + facility
										+ "' and wp_wavestatusvotemp.INTERACTIONID = '" + interactionID + "'", null));
						if (rs.size() == 0)
						{
							_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewOrderDetailsByStatus_execute",
									"Inserting New Record Into WaveStatusVOTemp " + waveid + " " + status + " "
											+ facility + " " + orderKey + " " + orderLineNo, SuggestedCategory.NONE);
							//insert new record
							QBEBioBean orderLineQBE = uow.getQBEBioWithDefaults("wp_wavestatusvotemp");

							orderLineQBE.set("INTERACTIONID", interactionID);
							orderLineQBE.set("SERIALKEY", UUID.randomUUID().toString());

							orderLineQBE.set("WAVEKEY", new Integer(waveid));
							orderLineQBE.set("STATUS", new Integer(status));
							orderLineQBE.set("WHSEID", facility);
							orderLineQBE.set("ORDERKEY", orderKey);
							orderLineQBE.set("ORDERLINENO", orderLineNo);

							orderLineQBE.set("SKU", orderLine.getSku());
							orderLineQBE.set("SKUDESCRIPTION", orderLine.getSkuDescription());
							orderLineQBE.set("STORERKEY", orderLine.getStorerKey());
							orderLineQBE.set("ORDEREDQTY", orderLine.getOrderedQty());
							orderLineQBE.set("OPENQTY", orderLine.getOpenQty());
							orderLineQBE.set("SHIPPEDQTY", orderLine.getShippedQty());
							orderLineQBE.set("ADJUSTEDQTY", orderLine.getAdjustedQty());
							orderLineQBE.set("PREQTY", orderLine.getPreQty());
							orderLineQBE.set("ALLOCATEDQTY", orderLine.getAllocatedQty());
							orderLineQBE.set("PICKEDQTY", orderLine.getPickedQty());
							orderLineQBE.set("PICKCODE", orderLine.getPickCode());
							orderLineQBE.set("CARTONGROUP", orderLine.getCartonGroup());
							orderLineQBE.set("LOT", orderLine.getLot());
							orderLineQBE.set("FACILITY", orderLine.getFacility());
							orderLineQBE.set("OSTATUS", orderLine.getStatus());
							orderLineQBE.set("UOMQTY", orderLine.getUomQty());
							orderLineQBE.set("SHIPGROUP1", orderLine.getShipGroup1());
							orderLineQBE.set("SHIPGROUP2", orderLine.getShipGroup2());
							orderLineQBE.set("SHIPGROUP3", orderLine.getShipGroup3());
							orderLineQBE.set("PICKDETAILNO", orderLine.getPickDetailNo());
							orderLineQBE.set("CASEID", orderLine.getCaseId());
							orderLineQBE.set("DROPID", orderLine.getDropId());
							orderLineQBE.set("LOCATION", orderLine.getLocation());
							orderLineQBE.set("CARTONTYPE", orderLine.getCartonType());
							orderLineQBE.set("SEQUENCENO", orderLine.getSequenceNo());
							orderLineQBE.set("PACKKEY", orderLine.getPackKey());
							orderLineQBE.set("UOM", orderLine.getUom());
							orderLineQBE.set("DOCARTONIZE", orderLine.isDoCartonize());
							orderLineQBE.set("DOREPLENISH", orderLine.isDoReplenish());
							orderLineQBE.set("DROPLOCATION", orderLine.getDropLocation());
							orderLineQBE.set("REPLENISHZONE", orderLine.getReplenishZone());
							orderLineQBE.set("LOTTABLE1", orderLine.getLottable1());
							orderLineQBE.set("LOTTABLE2", orderLine.getLottable2());
							orderLineQBE.set("LOTTABLE3", orderLine.getLottable3());
							orderLineQBE.set("LOTTABLE4", orderLine.getLottable4());
							orderLineQBE.set("LOTTABLE5", orderLine.getLottable5());
							orderLineQBE.set("LOTTABLE6", orderLine.getLottable6());
							orderLineQBE.set("LOTTABLE7", orderLine.getLottable7());
							orderLineQBE.set("LOTTABLE8", orderLine.getLottable8());
							orderLineQBE.set("LOTTABLE9", orderLine.getLottable9());
							orderLineQBE.set("LOTTABLE10", orderLine.getLottable10());
							orderLineQBE.set("PRESTGY", orderLine.getPreStgy());
							orderLineQBE.set("ALLOCATESTRATEGYKEY", orderLine.getAllocateStrategyKey());

							orderLineQBE.set("ADDDATE", currentDate);
							orderLineQBE.set("ADDWHO", userId);
							orderLineQBE.set("EDITDATE", currentDate);
							orderLineQBE.set("EDITWHO", userId);
							orderLineQBE.save();
						}
						else
						{
							_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewOrderDetailsByStatus_execute",
									"Updating Existing Record Into WaveStatusVOTemp " + waveid + " " + status + " "
											+ facility + " " + orderKey + " " + orderLineNo, SuggestedCategory.NONE);
							//update existing record
							for (int j = 0; j < rs.size(); j++)
							{
								BioBean orderLineBio = rs.get("" + i);

								orderLineBio.set("SKU", orderLine.getSku());
								orderLineBio.set("SKUDESCRIPTION", orderLine.getSkuDescription());
								orderLineBio.set("STORERKEY", orderLine.getStorerKey());
								orderLineBio.set("ORDEREDQTY", orderLine.getOrderedQty());
								orderLineBio.set("OPENQTY", orderLine.getOpenQty());
								orderLineBio.set("SHIPPEDQTY", orderLine.getShippedQty());
								orderLineBio.set("ADJUSTEDQTY", orderLine.getAdjustedQty());
								orderLineBio.set("PREQTY", orderLine.getPreQty());
								orderLineBio.set("ALLOCATEDQTY", orderLine.getAllocatedQty());
								orderLineBio.set("PICKEDQTY", orderLine.getPickedQty());
								orderLineBio.set("PICKCODE", orderLine.getPickCode());
								orderLineBio.set("CARTONGROUP", orderLine.getCartonGroup());
								orderLineBio.set("LOT", orderLine.getLot());
								orderLineBio.set("FACILITY", orderLine.getFacility());
								orderLineBio.set("OSTATUS", orderLine.getStatus());
								orderLineBio.set("UOMQTY", orderLine.getUomQty());
								orderLineBio.set("SHIPGROUP1", orderLine.getShipGroup1());
								orderLineBio.set("SHIPGROUP2", orderLine.getShipGroup2());
								orderLineBio.set("SHIPGROUP3", orderLine.getShipGroup3());
								orderLineBio.set("PICKDETAILNO", orderLine.getPickDetailNo());
								orderLineBio.set("CASEID", orderLine.getCaseId());
								orderLineBio.set("DROPID", orderLine.getDropId());
								orderLineBio.set("LOCATION", orderLine.getLocation());
								orderLineBio.set("CARTONTYPE", orderLine.getCartonType());
								orderLineBio.set("SEQUENCENO", orderLine.getSequenceNo());
								orderLineBio.set("PACKKEY", orderLine.getPackKey());
								orderLineBio.set("UOM", orderLine.getUom());
								orderLineBio.set("DOCARTONIZE", orderLine.isDoCartonize());
								orderLineBio.set("DOREPLENISH", orderLine.isDoReplenish());
								orderLineBio.set("DROPLOCATION", orderLine.getDropLocation());
								orderLineBio.set("REPLENISHZONE", orderLine.getReplenishZone());
								orderLineBio.set("LOTTABLE1", orderLine.getLottable1());
								orderLineBio.set("LOTTABLE2", orderLine.getLottable2());
								orderLineBio.set("LOTTABLE3", orderLine.getLottable3());
								orderLineBio.set("LOTTABLE4", orderLine.getLottable4());
								orderLineBio.set("LOTTABLE5", orderLine.getLottable5());
								orderLineBio.set("LOTTABLE6", orderLine.getLottable6());
								orderLineBio.set("LOTTABLE7", orderLine.getLottable7());
								orderLineBio.set("LOTTABLE8", orderLine.getLottable8());
								orderLineBio.set("LOTTABLE9", orderLine.getLottable9());
								orderLineBio.set("LOTTABLE10", orderLine.getLottable10());
								orderLineBio.set("PRESTGY", orderLine.getPreStgy());
								orderLineBio.set("ALLOCATESTRATEGYKEY", orderLine.getAllocateStrategyKey());

								orderLineBio.set("EDITDATE", currentDate);
								orderLineBio.set("EDITWHO", userId);
								orderLineBio.save();
							}
						}
					}
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				context.setNavigation(allocatedNavigation);
			}
		}
		uow.saveUOW(true);
*/
		return RET_CONTINUE;
	}

	private void removeExtraOrderLines(int waveid, int status, String facility, String interactionID, UnitOfWorkBean uow, ArrayList orderLineList) throws EpiDataException, EpiException
	{
/*		ArrayList orderKeysAndLines = new ArrayList();

		for (int i = 0; i < orderLineList.size(); i++)
		{
			final WaveStatusVO orderLine = (WaveStatusVO) orderLineList.get(i);
			orderKeysAndLines.add(orderLine.getOrderKey() + "," + orderLine.getOrderLineNo());
		}
		BioCollectionBean allOrderLines = uow.getBioCollectionBean(new Query("wp_wavestatusvotemp",
				"wp_wavestatusvotemp.WAVEKEY = '" + waveid + "' and wp_wavestatusvotemp.STATUS = '" + status
						+ "'  and wp_wavestatusvotemp.WHSEID ='" + facility
						+ "'  and wp_wavestatusvotemp.INTERACTIONID = '" + interactionID + "'", null));
		for (int i = 0; i < allOrderLines.size(); i++)
		{
			final BioBean orderLine = allOrderLines.get("" + i);
			orderLine.delete();

		}
		uow.saveUOW(true);
		
		*/
	}

}
