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
import java.util.Hashtable;
import java.util.Iterator;

import com.epiphany.shr.data.beans.BioService;
import com.epiphany.shr.data.beans.BioServiceFactory;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.infor.scm.waveplanning.common.util.BioAttributeUtil;
import com.infor.scm.waveplanning.common.util.WPUserUtil;
import com.infor.scm.waveplanning.common.util.WPUtil;
import com.infor.scm.waveplanning.wp_wavemgmt.util.WPWaveMgmtUtil;
import com.infor.scm.waveplanning.common.WavePlanningConstants;
import com.infor.scm.waveplanning.common.WavePlanningUtils;

/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class WaveMaintViewStatus extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WaveMaintViewStatus.class);

	public static String SESSION_KEY_WAVEMGMT_WAVEKEY_VIEW = "VIEW_WAVEMGMT_WAVEKEY";

	//private static final String SORT_ORDER = "wm_orderdetail.ORDERKEY ASC, wm_orderdetail.ORDERLINENUMBER ASC";

	public static final Hashtable<String, String> packQtyLookup = new Hashtable<String, String>(13, 0.75f);
	static
	{
		packQtyLookup.put("PACKUOM1", "CASECNT");
		packQtyLookup.put("PACKUOM2", "INNERPACK");
		packQtyLookup.put("PACKUOM3", "QTY");
		packQtyLookup.put("PACKUOM4", "PALLET");
		packQtyLookup.put("PACKUOM5", "CUBE");
		packQtyLookup.put("PACKUOM6", "GROSSWGT");
		packQtyLookup.put("PACKUOM7", "NETWGT");
		packQtyLookup.put("PACKUOM8", "OTHERUNIT1");
		packQtyLookup.put("PACKUOM9", "OTHERUNIT2");
	}

	/**
	 * The code within the execute method will be run from a UIAction specified
	 * in metadata.
	 * <P>
	 * 
	 * @param context
	 *                The ActionContext for this extension
	 * @param result
	 *                The ActionResult for this extension (contains the focus
	 *                and perspective for this UI Extension)
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * 
	 * @exception EpiException
	 */
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewStatus_execute", "Entering WaveMaintViewStatus",
				SuggestedCategory.NONE);
		StateInterface state = context.getState();
		RuntimeFormInterface currentRuntimeForm = state.getCurrentRuntimeForm();
		RuntimeFormInterface parentForm = currentRuntimeForm.getParentForm(state);
		int waveKey = 0;
		final String facility = WPUtil.getFacility(state.getRequest());

		//Clean Temp Records
		WPWaveMgmtUtil.clearTempTableBasedOnEditDate(state, "wp_wavestatus");
		WPWaveMgmtUtil.clearTempTableBasedOnEditDate(state, "wp_wavestatusheader");
		WPUserUtil.setInteractionSessionAttribute(SESSION_KEY_WAVEMGMT_WAVEKEY_VIEW, null, state);

		if (parentForm.isListForm())
		{
			if (((RuntimeListFormInterface) parentForm).getAllSelectedItems() == null)
			{
				throw new UserException("WPEXP_NO_WAVE_SELECTED", new Object[] {});
			}
			else if (((RuntimeListFormInterface) parentForm).getAllSelectedItems().size() > 1)
			{
				throw new UserException("WPEXP_MULTI_WAVE_SELECTED", new Object[] {});
			}
			else
			{
				ArrayList allSelectedItems = ((RuntimeListFormInterface) parentForm).getAllSelectedItems();
				for (Iterator it = allSelectedItems.iterator(); it.hasNext();)
				{

					final DataBean focus = (DataBean) it.next();
					waveKey = BioAttributeUtil.getInt(focus, "WAVEKEY");
					populateWaveStatus(state, facility, focus);
					WPUserUtil.setInteractionSessionAttribute(SESSION_KEY_WAVEMGMT_WAVEKEY_VIEW, new Integer(waveKey),
							state);
				}
				((RuntimeListFormInterface) parentForm).setSelectedItems(null);
			}
		}
		else
		{
			final DataBean focus = currentRuntimeForm.getFocus();
			waveKey = BioAttributeUtil.getInt(focus, "WAVEKEY");
			populateWaveStatus(state, facility, focus);
			WPUserUtil.setInteractionSessionAttribute(SESSION_KEY_WAVEMGMT_WAVEKEY_VIEW, new Integer(waveKey), state);
		}

		//navigate to status form
		final String interactionId = state.getInteractionId();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		final Query waveStatusHeaderQuery = new Query("wp_wavestatusheader", "wp_wavestatusheader.WAVEKEY = '"
				+ waveKey + "' and wp_wavestatusheader.INTERACTIONID = '" + interactionId + "'", null);
		BioCollectionBean rs = uow.getBioCollectionBean(waveStatusHeaderQuery);
		for (int i = 0; i < rs.size(); i++)
		{
			result.setFocus(rs.get("" + i));
		}

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewStatus_execute", "Leaving WaveMaintViewStatus",
				SuggestedCategory.NONE);
		return RET_CONTINUE;
	}

	public static void populateWaveStatus(StateInterface state, String facility, DataBean focus) throws UserException
	{
		_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewStatus_populateWaveStatus", "Entering populateWaveStatus",
				SuggestedCategory.NONE);
		try
		{
			String wmsName = WavePlanningUtils.wmsName;
			Object waveKeyObj = focus.getValue("WAVEKEY");
			String wmsWaveKey = "";
			if(waveKeyObj!=null){
				wmsWaveKey = waveKeyObj.toString();
			}

//			int waveid = 0;
//			waveid = BioAttributeUtil.getInt(focus, "WAVEKEY");
			final GregorianCalendar currentDate = new GregorianCalendar();
			final String userId = WPUserUtil.getUserId(state);
			final String interactionId = state.getInteractionId();

			//			WaveBD waveBD = new WaveBD();
			//			WaveVO waveVO = waveBD.getWaveDetails(waveid);
			//			ArrayList waveStatus = new ArrayList();
			ArrayList<Double> waveStatusLocal = new ArrayList<Double>();
			if (wmsName.equalsIgnoreCase(WavePlanningConstants.WMS_4000))
			{
				//				waveStatus = waveBD.getAllTotalsWaveStatusDetails(facility, waveVO);
				{
					BioServiceFactory serviceFactory;
					BioService bioService = null;
					UnitOfWork uow = null;
					try
					{
						serviceFactory = BioServiceFactory.getInstance();
						bioService = serviceFactory.create("webui", UnitOfWork.TRANSACTION_NONE);
						uow = bioService.getUnitOfWork();
					} catch (Exception e)
					{
						if (uow != null)
						{
							uow.close();
						}
						if (bioService != null)
						{
							bioService.remove();
						}
						uow = state.getTempUnitOfWork().getUOW();
					}

					try
					{
						//Calculate WaveStatus locally
						//Get OrderKeys associated with Wave
//						String wmsWaveKey = BioAttributeUtil.getString(focus, "WMSWAVEID");
						_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewStatus_populateWaveStatus", "External WaveKey "
								+ wmsWaveKey, SuggestedCategory.NONE);
						Query waveDetailQuery = new Query("wm_wavedetail", "wm_wavedetail.WAVEKEY = '" + wmsWaveKey
								+ "'", null);
						BioCollection rs = uow.findByQuery(waveDetailQuery);
						ArrayList<String> orderKeys = new ArrayList<String>(rs.size());
						for (int i = 0; i < rs.size(); i++)
						{
							Bio wavedetail = rs.elementAt(i);
							orderKeys.add(BioAttributeUtil.getString(wavedetail, "ORDERKEY"));
						}
						StringBuilder listOfOrderKeys = new StringBuilder();
						//flatten orderKeys
						for (int i = 0; i < orderKeys.size(); i++)
						{
							if (i > 0)
							{
								listOfOrderKeys.append("," + "'" + orderKeys.get(i) + "'");
							}
							else
							{
								listOfOrderKeys.append("'" + orderKeys.get(i) + "'");
							}
						}
						_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewStatus_populateWaveStatus", "OrderKeys = "
								+ listOfOrderKeys, SuggestedCategory.NONE);
						//Query OrderDetail Table to retrieve Collection
						final String orderQuery = "wm_orderdetail.ORDERKEY IN (" + listOfOrderKeys + ")";
						_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewStatus_populateWaveStatus", orderQuery,
								SuggestedCategory.NONE);
						Query orderDetailQuery = new Query("wm_orderdetail", orderQuery, null);
						rs = uow.findByQuery(orderDetailQuery);

						double preAllocSum = 0;
						double allocSum = 0;
						double pickedSum = 0;
						double shippedSum = 0;
						double openSum = 0;
						double eachesSum = 0;
						double casesSum = 0;
						double palletsSum = 0;
						Hashtable<String, Double[]> packCaseAndPalletCounts = new Hashtable<String, Double[]>();

						//Iterate through Collection - Summing OPENQTY(CALCULATED) QTYPREALLOCATED QTYALLOCATED QTYPICKED SHIPPEDQTY
						//OPENQTY(CALCULATED) = OPENQTY -(QTYPREALLOCATED + QTYALLOCATED + QTYPICKED)
						//Then Calculate TotalEaches,TotalCases,TotalPallets
						for (int i = 0; i < rs.size(); i++)
						{
							final Bio orderDetail = rs.elementAt(i);
							double orderOpen = BioAttributeUtil.getDouble(orderDetail, "OPENQTY");
							double orderPreAlloc = BioAttributeUtil.getDouble(orderDetail, "QTYPREALLOCATED");
							double orderAlloc = BioAttributeUtil.getDouble(orderDetail, "QTYALLOCATED");
							double orderPicked = BioAttributeUtil.getDouble(orderDetail, "QTYPICKED");
							double orderShipped = BioAttributeUtil.getDouble(orderDetail, "SHIPPEDQTY");
							openSum += (orderOpen - (orderPreAlloc + orderAlloc + orderPicked));
							preAllocSum += orderPreAlloc;
							allocSum += orderAlloc;
							pickedSum += orderPicked;
							shippedSum += orderShipped;
							//						_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewStatus_populateWaveStatus", BioAttributeUtil
							//								.getString((DataBean) orderDetail, "ORDERKEY")
							//								+ ", "
							//								+ BioAttributeUtil.getString((DataBean) orderDetail, "ORDERLINENUMBER")
							//								+ " = "
							//								+ orderOpen
							//								+ ", "
							//								+ orderPreAlloc
							//								+ ", "
							//								+ orderAlloc
							//								+ ", "
							//								+ orderPicked
							//								+ ", "
							//								+ orderShipped, SuggestedCategory.NONE);

							//Get UOM, PACKKEY
							final String packKey = BioAttributeUtil.getString(orderDetail, "PACKKEY");
							//Get Count for Case and Pallet based on PackKey
							double caseCount = 0;
							double palletCount = 0;

							if (packCaseAndPalletCounts.get(packKey) == null)
							{
								//Find Case and Pallet Counts
								_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewStatus_populateWaveStatus",
										"Finding Case/Pallet Counts for PackKey " + packKey, SuggestedCategory.NONE);
								Query packQuery = new Query("wm_pack", "wm_pack.PACKKEY = '" + packKey + "'", null);
								BioCollection packRS = uow.findByQuery(packQuery);
								for (int j = 0; j < packRS.size(); j++)
								{
									Bio packBean = packRS.elementAt(j);
									for (String packField : packQtyLookup.keySet())
									{
										String packUOMValue = BioAttributeUtil.getString(packBean, packField);
										if ("CS".equals(packUOMValue) || "CA".equalsIgnoreCase(packUOMValue))
										{
											//Get CaseCount from the assoc. pack qty field
											caseCount = BioAttributeUtil.getDouble(packBean, packQtyLookup
													.get(packField));
										}
										else if ("PL".equals(packUOMValue))
										{
											//Get PalletCount from the assoc. pack qty field
											palletCount = BioAttributeUtil.getDouble(packBean, packQtyLookup
													.get(packField));
										}
									}
								}
								packCaseAndPalletCounts.put(packKey, new Double[] { caseCount, palletCount });
							}
							else
							{
								_log
										.debug("LOG_DEBUG_EXTENSION_WaveMaintViewStatus_populateWaveStatus",
												"Retrieveing Case/Pallet Counts for PackKey " + packKey,
												SuggestedCategory.NONE);
								caseCount = packCaseAndPalletCounts.get(packKey)[0];
								palletCount = packCaseAndPalletCounts.get(packKey)[1];
							}
							//						_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewStatus_populateWaveStatus", "Pack: " + packKey
							//								+ " Case - " + caseCount + " Pallet - " + palletCount, SuggestedCategory.NONE);
							//Calculate TotalEaches,TotalCases,TotalPallets
							int orderPallets = 0;
							int orderCases = 0;
							int orderEaches;
							double remainqty = orderOpen - orderPicked;
							if ((palletCount > 0))
							{
								orderPallets = ((int) (remainqty / palletCount));
								remainqty = remainqty - (orderPallets * palletCount);
							}

							if ((caseCount > 0))
							{
								orderCases = ((int) (remainqty / caseCount));
								remainqty = remainqty - (orderCases * caseCount);
							}
							orderEaches = (int) remainqty;

							eachesSum += orderEaches;
							casesSum += orderCases;
							palletsSum += orderPallets;

						}

						waveStatusLocal.add(openSum);
						waveStatusLocal.add(preAllocSum);
						waveStatusLocal.add(allocSum);
						waveStatusLocal.add(pickedSum);
						waveStatusLocal.add(shippedSum);

						waveStatusLocal.add(eachesSum);
						waveStatusLocal.add(casesSum);
						waveStatusLocal.add(palletsSum);
					} finally
					{
						if (uow != null)
						{
							uow.close();
						}
						if (bioService != null)
						{
							bioService.remove();
						}
					}
				}
				_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewStatus_populateWaveStatus", "WaveStatus "
						+ waveStatusLocal, SuggestedCategory.NONE);
				/* 
				 * wavestatus structure
				 * 	ELEMENT	|	FIELD
				 * 	0		|	NOT STARTED
				 * 	1		|	PRE-ALLOCATED
				 * 	2		|	ALLOCATED
				 * 	3		|	PICKED
				 * 	4		|	SHIPPED
				 * 	5		|	TOTAL EACHES
				 * 	6		|	TOTAL CASES
				 * 	7		|	TOTAL PALLETS
				 * 
				 */
				double openQty = waveStatusLocal.get(0);
				double preAllocatedQty = waveStatusLocal.get(1);
				double allocatedQty = waveStatusLocal.get(2);
				double pickedQty = waveStatusLocal.get(3);
				double shippedQty = waveStatusLocal.get(4);
				double totalEaches = waveStatusLocal.get(5);
				double totalCases = waveStatusLocal.get(6);
				double totalPallets = waveStatusLocal.get(7);
				double total = openQty + preAllocatedQty + allocatedQty + pickedQty + shippedQty;

				//Insert or Update wp_wavestatusheader record
				UnitOfWorkBean uow = state.getDefaultUnitOfWork();
				BioCollectionBean rs = uow.getBioCollectionBean(new Query("wp_wavestatusheader",
						"wp_wavestatusheader.WAVEKEY = '" + wmsWaveKey + "' and wp_wavestatusheader.INTERACTIONID = '"
								+ interactionId + "' and wp_wavestatusheader.WHSEID = '" + facility + "'", null));

				if (rs.size() == 0)
				{
					QBEBioBean headerQBE = uow.getQBEBioWithDefaults("wp_wavestatusheader");

					headerQBE.set("INTERACTIONID", interactionId);
					headerQBE.set("WAVEKEY", wmsWaveKey);
//					headerQBE.set("DC_ID", BioAttributeUtil.getString(focus, "DC_ID"));
					headerQBE.set("WHSEID", facility);

					headerQBE.set("TOTALEACHES", new Double(totalEaches));
					headerQBE.set("TOTALCASES", new Double(totalCases));
					headerQBE.set("TOTALPALLETS", new Double(totalPallets));

					headerQBE.set("ADDDATE", currentDate);
					headerQBE.set("ADDWHO", userId);
					headerQBE.set("EDITDATE", currentDate);
					headerQBE.set("EDITWHO", userId);
					headerQBE.save();
				}
				else
				{
					for (int i = 0; i < rs.size(); i++)
					{
						BioBean row = rs.get(String.valueOf(i));

						row.set("TOTALEACHES", new Double(totalEaches));
						row.set("TOTALCASES", new Double(totalCases));
						row.set("TOTALPALLETS", new Double(totalPallets));

						row.set("EDITDATE", currentDate);
						row.set("EDITWHO", userId);
						row.save();
					}
				}

				uow.saveUOW(true);

				double openPercent = 0;
				double preAllocatedPercent = 0;
				double allocatedPercent = 0;
				double pickedPercent = 0;
				double shippedPercent = 0;

				if (openQty > 0)
				{
					openPercent = (openQty / total) * 100;
				}
				if (preAllocatedQty > 0)
				{
					preAllocatedPercent = (preAllocatedQty / total) * 100;
				}
				if (allocatedQty > 0)
				{
					allocatedPercent = (allocatedQty / total) * 100;
				}
				if (pickedQty > 0)
				{
					pickedPercent = (pickedQty / total) * 100;
				}
				if (shippedQty > 0)
				{
					shippedPercent = (shippedQty / total) * 100;
				}
				//insert or update records in wavestatus table

				uow = state.getDefaultUnitOfWork();
				rs = uow.getBioCollectionBean(new Query("wp_wavestatus", "wp_wavestatus.WAVEKEY = '" + wmsWaveKey
						+ "' and wp_wavestatus.INTERACTIONID = '" + interactionId + "'", null));
				if (rs.size() >= 1)
				{
					//update
					_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewStatus_populateWaveStatus",
							"Updating status records for " + wmsWaveKey, SuggestedCategory.NONE);
					for (int i = 0; i < rs.size(); i++)
					{
						BioBean row = rs.get(String.valueOf(i));
						int status = BioAttributeUtil.getInt((DataBean) row, "STATUS");
						switch (status)
						{
							case 0:
								updateExistingRecord(openQty, openPercent, currentDate, userId, row);
								break;
							case 1:
								updateExistingRecord(preAllocatedQty, preAllocatedPercent, currentDate, userId, row);
								break;
							case 2:
								updateExistingRecord(allocatedQty, allocatedPercent, currentDate, userId, row);
								break;
							case 3:
								updateExistingRecord(pickedQty, pickedPercent, currentDate, userId, row);
								break;
							case 4:
								updateExistingRecord(shippedQty, shippedPercent, currentDate, userId, row);
								break;
							case 5:
								updateExistingRecord(total, 100, currentDate, userId, row);
								break;
							default:
								break;
						}

					}

				}
				else
				{
					//insert
					_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewStatus_populateWaveStatus",
							"Inserting status records for " + wmsWaveKey, SuggestedCategory.NONE);
					//method that takes a QBE, status, qty, percent and saves it
					QBEBioBean openQBE = uow.getQBEBioWithDefaults("wp_wavestatus");
					insertNewRecord(focus, 0, wmsWaveKey, openQty, openPercent, currentDate, userId, facility,
							interactionId, openQBE);

					QBEBioBean preQBE = uow.getQBEBioWithDefaults("wp_wavestatus");
					insertNewRecord(focus, 1, wmsWaveKey, preAllocatedQty, preAllocatedPercent, currentDate, userId,
							facility, interactionId, preQBE);

					QBEBioBean allocQBE = uow.getQBEBioWithDefaults("wp_wavestatus");
					insertNewRecord(focus, 2, wmsWaveKey, allocatedQty, allocatedPercent, currentDate, userId, facility,
							interactionId, allocQBE);

					QBEBioBean pickQBE = uow.getQBEBioWithDefaults("wp_wavestatus");
					insertNewRecord(focus, 3, wmsWaveKey, pickedQty, pickedPercent, currentDate, userId, facility,
							interactionId, pickQBE);

					QBEBioBean shipQBE = uow.getQBEBioWithDefaults("wp_wavestatus");
					insertNewRecord(focus, 4, wmsWaveKey, shippedQty, shippedPercent, currentDate, userId, facility,
							interactionId, shipQBE);

					QBEBioBean totQBE = uow.getQBEBioWithDefaults("wp_wavestatus");
					insertNewRecord(focus, 5, wmsWaveKey, total, 100, currentDate, userId, facility, interactionId, totQBE);

				}

				uow.saveUOW(true);
			}
		} catch (UnitOfWorkException e)
		{
			_log.error("LOG_ERROR_EXTENSION_WaveMaintViewStatus_execute", "Unit of Work Exception " + e.getMessage(),
					SuggestedCategory.NONE);
			e.printStackTrace();
			throw new UserException("WPEXP_SYS_EXP", new Object[] {});

		}
		//		catch (WavePlanningException e)
		//		{
		//
		//			_log.error("LOG_ERROR_EXTENSION_ConfirmWaveConfirmWave", "WavePlanningException " + e.getMessage(),
		//					SuggestedCategory.NONE);
		//			e.printStackTrace();
		//
		//			String args[] = new String[0];
		//			String errorMsg = getTextMessage("WPEXP_SYS_EXP", args, state.getLocale());
		//			throw new UserException(errorMsg, new Object[0]);
		//
		//		} 
		catch (Exception e)
		{
			_log.error("LOG_ERROR_EXTENSION_ConfirmWaveConfirmWave", "Generic Exception " + e.getMessage(),
					SuggestedCategory.NONE);
			e.printStackTrace();
		}

		_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewStatus_populateWaveStatus", "Leaving populateWaveStatus",
				SuggestedCategory.NONE);
	}

	private static void updateExistingRecord(double qty, double percent, final GregorianCalendar currentDate, final String userId, BioBean row)
	{
		row.setValue("UNITS", new Double(qty));
		row.setValue("PERCENTVAL", new Double(percent));
		row.setValue("EDITDATE", currentDate);
		row.setValue("EDITWHO", userId);
		row.save();
	}

/*	private static void insertNewRecord(DataBean focus, int status, int waveid, double openQty, double openPercent, final GregorianCalendar currentDate, final String userId, String facility, String interactionID, QBEBioBean waveStatusQBE)
	{
		waveStatusQBE.set("INTERACTIONID", interactionID);
		waveStatusQBE.set("WAVEKEY", new Integer(waveid));
		waveStatusQBE.set("DC_ID", BioAttributeUtil.getString(focus, "DC_ID"));
		waveStatusQBE.set("WHSEID", facility);
		waveStatusQBE.set("STATUS", new Integer(status));
		waveStatusQBE.set("UNITS", new Double(openQty));
		waveStatusQBE.set("PERCENTVAL", new Double(openPercent));
		waveStatusQBE.set("ADDDATE", currentDate);
		waveStatusQBE.set("ADDWHO", userId);
		waveStatusQBE.set("EDITDATE", currentDate);
		waveStatusQBE.set("EDITWHO", userId);
		waveStatusQBE.save();
	}
	*/
	private static void insertNewRecord(DataBean focus, int status, String waveKey, double openQty, double openPercent, final GregorianCalendar currentDate, final String userId, String facility, String interactionID, QBEBioBean waveStatusQBE)
	{
		waveStatusQBE.set("INTERACTIONID", interactionID);
		waveStatusQBE.set("WAVEKEY", waveKey);
//		waveStatusQBE.set("DC_ID", BioAttributeUtil.getString(focus, "DC_ID"));
		waveStatusQBE.set("WHSEID", facility);
		waveStatusQBE.set("STATUS", new Integer(status));
		waveStatusQBE.set("UNITS", new Double(openQty));
		waveStatusQBE.set("PERCENTVAL", new Double(openPercent));
		waveStatusQBE.set("ADDDATE", currentDate);
		waveStatusQBE.set("ADDWHO", userId);
		waveStatusQBE.set("EDITDATE", currentDate);
		waveStatusQBE.set("EDITWHO", userId);
		waveStatusQBE.save();
	}

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked
	 * or a value entered in a form in a modal dialog Write code here if u want
	 * this to be called when the UI Action event is fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext}
	 * exposes information about the event, including the service and the user
	 * interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes
	 * information about the results of the action that has occurred, and
	 * enables your extension to modify them.</li>
	 * </ul>
	 */
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
	{

		try
		{
			// Add your code here to process the event

		} catch (Exception e)
		{

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
