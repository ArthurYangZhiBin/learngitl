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

package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.infor.scm.waveplanning.common.util.BioAttributeUtil;
import com.infor.scm.waveplanning.common.util.WPUserUtil;
import com.infor.scm.waveplanning.common.util.WPUtil;
import com.infor.scm.waveplanning.wp_wavemgmt.util.WPWaveMgmtUtil;
import com.infor.scm.waveplanning.common.WavePlanningConstants;
import com.infor.scm.waveplanning.common.WavePlanningException;
import com.infor.scm.waveplanning.common.WavePlanningUtils;
//import com.ssaglobal.scm.waveplanning.wavemgmt.bd.WaveBD;
//import com.ssaglobal.scm.waveplanning.wavemgmt.vo.ShipmentOrderSummary;
//import com.ssaglobal.scm.waveplanning.wavemgmt.vo.WaveOrderSummaryCollection;
//import com.ssaglobal.scm.waveplanning.wavemgmt.vo.WaveVO;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.data.bio.BioCollection;
import com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.ui.OrderSummaryObj;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class WaveMaintViewSummary extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WaveMaintViewSummary.class);

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
		_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewSummary_execute", "Entering WaveMaintViewSummary",
				SuggestedCategory.NONE);
		StateInterface state = context.getState();
		RuntimeFormInterface currentRuntimeForm = state.getCurrentRuntimeForm();
		RuntimeFormInterface parentForm = currentRuntimeForm.getParentForm(state);
		int waveKey = 0;
		final String facility = WPUtil.getFacility(state.getRequest());
		final String interactionId = state.getInteractionId();
		final String userId = WPUserUtil.getUserId(state);
		final GregorianCalendar currentDate = new GregorianCalendar();

		//Clean Temp Records
		WPWaveMgmtUtil.clearTempTableBasedOnEditDate(state, "wp_wavestatusheader");
		WPWaveMgmtUtil.clearTempTableBasedOnEditDate(state, "wp_shipmentordersummaryvotemp");
		
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
					processSummaryRecords(state, waveKey, facility, interactionId, userId, currentDate, focus);

				}
				((RuntimeListFormInterface) parentForm).setSelectedItems(null);
			}
		}
		else
		{
			final DataBean focus = currentRuntimeForm.getFocus();
			waveKey = BioAttributeUtil.getInt(focus, "WAVEKEY");
			processSummaryRecords(state, waveKey, facility, interactionId, userId, currentDate, focus);
		}

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		final Query waveStatusHeaderQuery = new Query("wp_wavestatusheader", "wp_wavestatusheader.WAVEKEY = '"
				+ waveKey + "' and wp_wavestatusheader.INTERACTIONID = '" + interactionId + "'", null);
		_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewSummary_execute", "Querying for wp_wavestatusheader record "
				+ waveStatusHeaderQuery, SuggestedCategory.NONE);
		BioCollectionBean rs = uow.getBioCollectionBean(waveStatusHeaderQuery);
		for (int i = 0; i < rs.size(); i++)
		{
			result.setFocus(rs.get("" + i));
		}
		_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewSummary_execute", "Leaving WaveMaintViewSummary",
				SuggestedCategory.NONE);

		return RET_CONTINUE;
	}

	public static void processSummaryRecords(StateInterface state, int waveKey, final String facility, final String interactionId, final String userId, final GregorianCalendar currentDate, final DataBean focus) throws UserException
	{
/*		try
		{
		//Insert or Update wp_wavestatusheader record
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			BioCollectionBean rs = uow.getBioCollectionBean(new Query("wp_wavestatusheader",
					"wp_wavestatusheader.WAVEKEY = '" + waveKey + "' and wp_wavestatusheader.INTERACTIONID = '"
							+ interactionId + "' and wp_wavestatusheader.WHSEID = '" + facility + "'", null));

			if (rs.size() == 0)
			{
				_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewSummary_processSummaryRecords",
						"Inserting wp_wavestatusheader record ", SuggestedCategory.NONE);
				QBEBioBean headerQBE = uow.getQBEBioWithDefaults("wp_wavestatusheader");

				headerQBE.set("INTERACTIONID", interactionId);
				headerQBE.set("WAVEKEY", new Integer(waveKey));
//				headerQBE.set("DC_ID", BioAttributeUtil.getString(focus, "DC_ID"));
				headerQBE.set("WHSEID", facility);

				headerQBE.set("ADDDATE", currentDate);
				headerQBE.set("ADDWHO", userId);
				headerQBE.set("EDITDATE", currentDate);
				headerQBE.set("EDITWHO", userId);
				headerQBE.save();
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewSummary_processSummaryRecords",
						"Updating wp_wavestatusheader record", SuggestedCategory.NONE);
				for (int i = 0; i < rs.size(); i++)
				{
					BioBean row = rs.get(String.valueOf(i));

					row.set("EDITDATE", currentDate);
					row.set("EDITWHO", userId);
					row.save();
				}
			}

			uow.saveUOW(true);

			// Call Backend Process to perform summary function
			uow = state.getDefaultUnitOfWork();

			String createdDate = "";
			String wmsName = WavePlanningUtils.wmsName;
			if (wmsName.equalsIgnoreCase(WavePlanningConstants.WMS_4000))
			{
				WaveVO waveVO = new WaveVO();
				WaveBD waveBD = new WaveBD();
				waveVO = waveBD.getWaveDetails(waveKey);
				//waveForm.setWaveKey(""+waveVO.getWaveKey());
				//waveForm.setDescription(waveVO.getDescription());
				// DF# 109437
				//				String locale = WavePlanningUtils.getUserLocale(request, response);
				//				if (waveVO.getCreatedOn() != null)
				//				{
				//					createdDate = WavePlanningUtils.formatWPDate(waveVO.getCreatedOn(), locale);
				//				}
				//waveForm.setCreatedOn(createdDate);
				// DF# 109437
				//waveForm.setStatus(waveVO.getStatus());
				WaveOrderSummaryCollection orderSummaryCollection = waveBD.getWaveOrderSummary(facility, waveVO);

				//remove extra sos lines
				removeExtraSOSLines(waveKey, facility, interactionId, state, orderSummaryCollection);

				uow = state.getDefaultUnitOfWork();
				//request.setAttribute("WaveSummary", orderSummaryCollection);
				rs = uow.getBioCollectionBean(new Query("wp_shipmentordersummaryvotemp",
						"wp_shipmentordersummaryvotemp.WAVEKEY = '" + waveKey
								+ "' and wp_shipmentordersummaryvotemp.INTERACTIONID = '" + interactionId
								+ "' and wp_shipmentordersummaryvotemp.WHSEID = '" + facility + "'", null));
				if (rs.size() >= 1)
				{
					//update existing records
					for (int i = 0; i < orderSummaryCollection.getNumOfShipmentOrderSummaries(); i++)
					{
						final ShipmentOrderSummary orderSummary = orderSummaryCollection.getShipmentOrderSummary(i);
						BioCollectionBean rs2 = uow.getBioCollectionBean(new Query("wp_shipmentordersummaryvotemp",
								"wp_shipmentordersummaryvotemp.WAVEKEY = '" + waveKey
										+ "' and wp_shipmentordersummaryvotemp.INTERACTIONID = '" + interactionId
										+ "' and wp_shipmentordersummaryvotemp.WHSEID = '" + facility
										+ "' and wp_shipmentordersummaryvotemp.ORDERKEY = '"
										+ orderSummary.getOrderKey() + "'", null));
						if (rs2.size() == 0)
						{
							//insert
							QBEBioBean sosQBE = uow.getQBEBioWithDefaults("wp_shipmentordersummaryvotemp");
							insertSOS(waveKey, facility, interactionId, userId, currentDate, focus, orderSummary,
									sosQBE);
						}
						else
						{
							//update
							for (int j = 0; j < rs2.size(); j++)
							{
								BioBean sosBio = rs.get("" + i);
								updateSOS(userId, currentDate, orderSummary, sosBio);
							}
						}
					}

				}
				else
				{
					//insert
					for (int i = 0; i < orderSummaryCollection.getNumOfShipmentOrderSummaries(); i++)
					{
						final ShipmentOrderSummary sosRow = orderSummaryCollection.getShipmentOrderSummary(i);
						QBEBioBean sosQBE = uow.getQBEBioWithDefaults("wp_shipmentordersummaryvotemp");
						insertSOS(waveKey, facility, interactionId, userId, currentDate, focus, sosRow, sosQBE);

					}
				}
				uow.saveUOW(true);

			}
			//				if (wmsName.equalsIgnoreCase(WavePlanningConstants.WMS_2000))
			//				{
			//					//				WaveVO waveVO = waveBD.getWaveDetails(waveid);
			//					//				ArrayList waveSummaryList = new ArrayList();
			//					//				waveSummaryList = waveBD.get2000WaveSummaryList(facility, waveVO);
			//					//				request.setAttribute("WaveVO", waveVO);
			//					//				request.setAttribute("WaveSummary", waveSummaryList);
			//					//				result = "success_2000";
			//				}
		} catch (UnitOfWorkException e)
		{
			_log.error("LOG_ERROR_EXTENSION_WaveMaintViewSummary_execute", "Unit of Work Exception " + e.getMessage(),
					SuggestedCategory.NONE);
			e.printStackTrace();
			throw new UserException("WPEXP_SYS_EXP", new Object[] {});

		} catch (WavePlanningException e)
		{

			_log.error("LOG_ERROR_EXTENSION_ConfirmWaveConfirmWave", "WavePlanningException " + e.getMessage(),
					SuggestedCategory.NONE);
			e.printStackTrace();

			String args[] = new String[0];
			String errorMsg = getTextMessage("WPEXP_SYS_EXP", args, state.getLocale());
			throw new UserException(errorMsg, new Object[0]);

		} catch (Exception e)
		{
			_log.error("LOG_ERROR_EXTENSION_ConfirmWaveConfirmWave", "Generic Exception " + e.getMessage(),
					SuggestedCategory.NONE);
			e.printStackTrace();
			throw new UserException(e.getLocalizedMessage(), new Object[] {});
		}*/
	}
	public static void processSummaryRecords(StateInterface state, String waveKey,  final DataBean focus) throws UserException{
		try
		{
			final String facility = WPUtil.getFacility(state.getRequest());
			final String interactionId = state.getInteractionId();
			final String userId = WPUserUtil.getUserId(state);
			final GregorianCalendar currentDate = new GregorianCalendar();
		//Insert or Update wp_wavestatusheader record
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			BioCollectionBean rs = uow.getBioCollectionBean(new Query("wp_wavestatusheader",
					"wp_wavestatusheader.WAVEKEY = '" + waveKey + "' and wp_wavestatusheader.INTERACTIONID = '"
							+ interactionId + "' and wp_wavestatusheader.WHSEID = '" + facility + "'", null));

			if (rs.size() == 0)
			{
				_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewSummary_processSummaryRecords",
						"Inserting wp_wavestatusheader record ", SuggestedCategory.NONE);
				QBEBioBean headerQBE = uow.getQBEBioWithDefaults("wp_wavestatusheader");

				headerQBE.set("INTERACTIONID", interactionId);
				headerQBE.set("WAVEKEY", waveKey);
//				headerQBE.set("DC_ID", BioAttributeUtil.getString(focus, "DC_ID"));
				headerQBE.set("WHSEID", facility);

				headerQBE.set("ADDDATE", currentDate);
				headerQBE.set("ADDWHO", userId);
				headerQBE.set("EDITDATE", currentDate);
				headerQBE.set("EDITWHO", userId);
				headerQBE.save();
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION_WaveMaintViewSummary_processSummaryRecords",
						"Updating wp_wavestatusheader record", SuggestedCategory.NONE);
				for (int i = 0; i < rs.size(); i++)
				{
					BioBean row = rs.get(String.valueOf(i));

					row.set("EDITDATE", currentDate);
					row.set("EDITWHO", userId);
					row.save();
				}
			}

			uow.saveUOW(true);

			// Call Backend Process to perform summary function
			uow = state.getDefaultUnitOfWork();


				//remove extra sos lines
				ArrayList<OrderSummaryObj> orderSummaryList = com.infor.scm.waveplanning.wp_wm_wave.wave.WPUtil.getOrderSummary(waveKey);
				int size = orderSummaryList.size();
				removeExtraSOSLines(waveKey, facility, interactionId, state, orderSummaryList);

				uow = state.getDefaultUnitOfWork();
				//request.setAttribute("WaveSummary", orderSummaryCollection);
				rs = uow.getBioCollectionBean(new Query("wp_shipmentordersummaryvotemp",
						"wp_shipmentordersummaryvotemp.WAVEKEY = '" + waveKey
								+ "' and wp_shipmentordersummaryvotemp.INTERACTIONID = '" + interactionId
								+ "' and wp_shipmentordersummaryvotemp.WHSEID = '" + facility + "'", null));
				if (rs.size() >= 1)
				{
					//update existing records
					OrderSummaryObj orderSummaryObj = null;
					for (int i = 0; i < size; i++)
					{
						orderSummaryObj = orderSummaryList.get(i);
						BioCollectionBean rs2 = uow.getBioCollectionBean(new Query("wp_shipmentordersummaryvotemp",
								"wp_shipmentordersummaryvotemp.WAVEKEY = '" + waveKey
										+ "' and wp_shipmentordersummaryvotemp.INTERACTIONID = '" + interactionId
										+ "' and wp_shipmentordersummaryvotemp.WHSEID = '" + facility
										+ "' and wp_shipmentordersummaryvotemp.ORDERKEY = '"
										+ orderSummaryObj.getOrderKey() + "'", null));
						if (rs2.size() == 0)
						{
							//insert
							QBEBioBean sosQBE = uow.getQBEBioWithDefaults("wp_shipmentordersummaryvotemp");
							insertSOS(waveKey, facility, interactionId, userId, currentDate, focus, orderSummaryObj,
									sosQBE);
						}
						else
						{
							//update
							for (int j = 0; j < rs2.size(); j++)
							{
								BioBean sosBio = rs.get("" + i);
								updateSOS(userId, currentDate, orderSummaryObj, sosBio);
							}
						}
					}

				}
				else
				{
					//insert
					OrderSummaryObj orderSummaryObj = null;
					for (int i = 0; i < size; i++)
					{
						orderSummaryObj = orderSummaryList.get(i);
						QBEBioBean sosQBE = uow.getQBEBioWithDefaults("wp_shipmentordersummaryvotemp");
						insertSOS(waveKey, facility, interactionId, userId, currentDate, focus, orderSummaryObj, sosQBE);

					}
				}
				uow.saveUOW(true);

			
		} catch (UnitOfWorkException e)
		{
			_log.error("LOG_ERROR_EXTENSION_WaveMaintViewSummary_execute", "Unit of Work Exception " + e.getMessage(),
					SuggestedCategory.NONE);
			e.printStackTrace();
			throw new UserException("WPEXP_SYS_EXP", new Object[] {});

		}  catch (Exception e)
		{
			_log.error("LOG_ERROR_EXTENSION_ConfirmWaveConfirmWave", "Generic Exception " + e.getMessage(),
					SuggestedCategory.NONE);
			e.printStackTrace();
			throw new UserException(e.getLocalizedMessage(), new Object[] {});
		}	
	}

	private static void updateSOS(final String userId, final GregorianCalendar currentDate, final ShipmentOrderSummary orderSummary, BioBean sosBio)
	{
		sosBio.set("EXTERNORDERKEY", orderSummary.getExtrenOrderKey());

		sosBio.set("TOTALCASES", orderSummary.getTotalCases());
		sosBio.set("TOTALCUBE", orderSummary.getTotalCube());
		sosBio.set("TOTALEACHES", orderSummary.getTotalEaches());
		sosBio.set("TOTALGROSSWEIGHT", orderSummary.getTotalGrossWeight());
		sosBio.set("TOTALNETWEIGHT", orderSummary.getTotalNetWeight());
		sosBio.set("TOTALPALLETS", orderSummary.getTotalPallets());

		sosBio.set("EDITDATE", currentDate);
		sosBio.set("EDITWHO", userId);
		sosBio.save();
	}
	private static void updateSOS(final String userId, final GregorianCalendar currentDate, OrderSummaryObj orderSummary, BioBean sosBio)
	{
		sosBio.set("EXTERNORDERKEY", orderSummary.getExternalOrderKey());

		sosBio.set("TOTALCASES", orderSummary.getTotalCases());
		sosBio.set("TOTALCUBE", orderSummary.getTotalCube());
		sosBio.set("TOTALEACHES", orderSummary.getTotalEaches());
		sosBio.set("TOTALGROSSWEIGHT", orderSummary.getTotalGrossWeight());
		sosBio.set("TOTALNETWEIGHT", orderSummary.getTotalNetWeight());
		sosBio.set("TOTALPALLETS", orderSummary.getTotalPallets());

		sosBio.set("EDITDATE", currentDate);
		sosBio.set("EDITWHO", userId);
		sosBio.save();
	}
	private static void insertSOS(int waveKey, final String facility, final String interactionId, final String userId, final GregorianCalendar currentDate, final DataBean focus, final ShipmentOrderSummary sosRow, QBEBioBean sosQBE)
	{
		sosQBE.set("INTERACTIONID", interactionId);
		sosQBE.set("WAVEKEY", new Integer(waveKey));
		sosQBE.set("DC_ID", BioAttributeUtil.getString(focus, "DC_ID"));
		sosQBE.set("WHSEID", facility);

		sosQBE.set("ORDERKEY", sosRow.getOrderKey());
		sosQBE.set("EXTERNORDERKEY", sosRow.getExtrenOrderKey());

		sosQBE.set("TOTALCASES", sosRow.getTotalCases());
		sosQBE.set("TOTALCUBE", sosRow.getTotalCube());
		sosQBE.set("TOTALEACHES", sosRow.getTotalEaches());
		sosQBE.set("TOTALGROSSWEIGHT", sosRow.getTotalGrossWeight());
		sosQBE.set("TOTALNETWEIGHT", sosRow.getTotalNetWeight());
		sosQBE.set("TOTALPALLETS", sosRow.getTotalPallets());

		sosQBE.set("ADDDATE", currentDate);
		sosQBE.set("ADDWHO", userId);
		sosQBE.set("EDITDATE", currentDate);
		sosQBE.set("EDITWHO", userId);
		sosQBE.save();
	}
	private static void insertSOS(String waveKey, final String facility, final String interactionId, final String userId, final GregorianCalendar currentDate, final DataBean focus, final OrderSummaryObj sosRow, QBEBioBean sosQBE)
	{
		sosQBE.set("INTERACTIONID", interactionId);
		sosQBE.set("WAVEKEY", waveKey);
//		sosQBE.set("DC_ID", BioAttributeUtil.getString(focus, "DC_ID"));
		sosQBE.set("WHSEID", facility);

		sosQBE.set("ORDERKEY", sosRow.getOrderKey());
		sosQBE.set("EXTERNORDERKEY", sosRow.getExternalOrderKey());

		sosQBE.set("TOTALCASES", sosRow.getTotalCases());
		sosQBE.set("TOTALCUBE", sosRow.getTotalCube());
		sosQBE.set("TOTALEACHES", sosRow.getTotalEaches());
		sosQBE.set("TOTALGROSSWEIGHT", sosRow.getTotalGrossWeight());
		sosQBE.set("TOTALNETWEIGHT", sosRow.getTotalNetWeight());
		sosQBE.set("TOTALPALLETS", sosRow.getTotalPallets());

		sosQBE.set("ADDDATE", currentDate);
		sosQBE.set("ADDWHO", userId);
		sosQBE.set("EDITDATE", currentDate);
		sosQBE.set("EDITWHO", userId);
		sosQBE.save();
	}

	private static void removeExtraSOSLines(String waveKey, final String facility, final String interactionId, StateInterface state, WaveOrderSummaryCollection orderSummaryCollection) throws EpiDataException, EpiException
	{
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		ArrayList orderKeys = new ArrayList(orderSummaryCollection.getNumOfShipmentOrderSummaries());
		for (int i = 0; i < orderSummaryCollection.getNumOfShipmentOrderSummaries(); i++)
		{
			final ShipmentOrderSummary sos = orderSummaryCollection.getShipmentOrderSummary(i);
			orderKeys.add(sos.getOrderKey());
		}

		BioCollectionBean allShipmentOrders = uow.getBioCollectionBean(new Query("wp_shipmentordersummaryvotemp",
				"wp_shipmentordersummaryvotemp.WAVEKEY = '" + waveKey + "' and wp_shipmentordersummaryvotemp.WHSEID ='"
						+ facility + "'  and wp_shipmentordersummaryvotemp.INTERACTIONID = '" + interactionId + "'",
				null));
		for (int i = 0; i < allShipmentOrders.size(); i++)
		{
			final BioBean sos = allShipmentOrders.get("" + i);
			final String key = BioAttributeUtil.getString((DataBean) sos, "ORDERKEY");
			if (!orderKeys.contains(key))
			{
				sos.delete();
			}
		}
		uow.saveUOW(true);
	}
	
	
	private static void removeExtraSOSLines(String waveKey, final String facility, final String interactionId, StateInterface state, ArrayList<OrderSummaryObj> orderSummaryList) throws EpiDataException, EpiException
	{
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();

		BioCollectionBean allShipmentOrders = uow.getBioCollectionBean(new Query("wp_shipmentordersummaryvotemp",
				"wp_shipmentordersummaryvotemp.WAVEKEY = '" + waveKey + "' and wp_shipmentordersummaryvotemp.WHSEID ='"
						+ facility + "'  and wp_shipmentordersummaryvotemp.INTERACTIONID = '" + interactionId + "'",
				null));
		for (int i = 0; i < allShipmentOrders.size(); i++)
		{
			final BioBean sos = allShipmentOrders.get("" + i);
			final String key = BioAttributeUtil.getString((DataBean) sos, "ORDERKEY");
			if (!orderSummaryList.contains(key))
			{
				sos.delete();
			}
		}
		uow.saveUOW(true);
	}
	
	
	private WaveOrderSummaryCollection getWaveOrderSummary(String waveKey, UnitOfWorkBean uow) throws EpiDataException{
		BioCollectionBean orderBeans = uow.getBioCollectionBean(new Query("wm_wp_wave",
				"wm_wp_wave.WAVEKEY = '" + waveKey, null));
		ShipmentOrderSummary sos = null;
		BioBean waveBioBean = orderBeans.get("0");
		BioCollection ordersBioCollection = waveBioBean.getBioCollection("ORDERBIOCOLLECTION");
		int size = ordersBioCollection.size();
		for(int i=0; i<size;i++){
			
		}
		
		
		return null;
	}
	
}
