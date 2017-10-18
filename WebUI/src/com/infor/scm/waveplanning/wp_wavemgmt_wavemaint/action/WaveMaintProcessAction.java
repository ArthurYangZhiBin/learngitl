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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
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
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.wm_ums.User;
import com.infor.scm.waveplanning.wp_wavemgmt.util.WPWaveMgmtUtil;
import com.infor.scm.waveplanning.wp_wm_wave.wave.*;
import com.infor.scm.waveplanning.wp_wm_wave.wave.actions.WMWaveActionInterface;
import com.infor.scm.waveplanning.wp_wm_wave.wave.actions.WaveActionFactory;

import com.infor.scm.waveplanning.common.WavePlanningConstants;
import com.infor.scm.waveplanning.common.WavePlanningException;
import com.agileitp.forte.framework.TextData;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.infor.scm.waveplanning.wp_wm_wave.wave.WaveValidateInputObj;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class WaveMaintProcessAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WaveMaintProcessAction.class);

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
	protected int execute(ActionContext context, ActionResult result) throws UserException
	{

		StateInterface state = context.getState();
		RuntimeFormInterface currentRuntimeForm = state.getCurrentRuntimeForm();
		RuntimeFormInterface parentForm = currentRuntimeForm.getParentForm(state);
		String waveKey = "";
		String action = getParameterString("ACTION");
		if (parentForm.isListForm())
		{
			if (((RuntimeListFormInterface) parentForm).getAllSelectedItems() == null)
			{
				_log.error("LOG_ERROR_EXTENSION_WaveMaintProcessAction_execute", "No Waves Selected",
						SuggestedCategory.NONE);
				throw new UserException("WPEXP_NO_WAVE_SELECTED", new Object[] {});
			}
			else if (((RuntimeListFormInterface) parentForm).getAllSelectedItems().size() > 1)
			{
				throw new UserException("WPEXP_MULTI_WAVE_SELECTED", new Object[] {});
			}
			else
			{//only one wave is selected
				_log.debug("LOG_DEBUG_EXTENSION_WaveMaintProcessAction", "Called from List Form "
						+ parentForm.getName(), SuggestedCategory.NONE);
				ArrayList allSelectedItems = ((RuntimeListFormInterface) parentForm).getAllSelectedItems();
				EXEDataObject edo=null;
				for (Iterator it = allSelectedItems.iterator(); it.hasNext();)
				{
					final DataBean focus = (DataBean) it.next();
					if (focus.getValue("WAVEKEY") != null)
					{
						waveKey=focus.getValue("WAVEKEY").toString();
						
						
						if(WPConstants.GET_CONSOLIDATED_SKU.equalsIgnoreCase(action)){
							com.infor.scm.waveplanning.wp_wm_wave.wave.WPUtil.populateConsolidate(state, waveKey);
							final Query eligibleSkusQuery = new Query("wp_eligibleconsolidatedskulistwaveheadervotemp",
									"wp_eligibleconsolidatedskulistwaveheadervotemp.INTERACTIONID = '" + state.getInteractionId()
											+ "' and wp_eligibleconsolidatedskulistwaveheadervotemp.WAVEKEY = '" + waveKey + "'",
									null);
							UnitOfWorkBean uow = state.getDefaultUnitOfWork();
							BioCollectionBean rs = uow.getBioCollectionBean(eligibleSkusQuery);
							try{
								if (rs.size()!= 0){				
									result.setFocus(rs);
									return RET_CONTINUE;
								}else{
									String args[] = new String[0];
									String errorMsg = getTextMessage("WPEXP_NOSKUTOCONSOL", args, state.getLocale());
									throw new UserException(errorMsg, new Object[0]);
								}
							}catch(EpiException e){
								e.printStackTrace();
								return RET_CANCEL;
							}
						
						
						}
						
						
						
						
						ActionInputObj actionInput = new ActionInputObj();
						actionInput.setWaveKey(waveKey);
						actionInput.setAction(action);
						edo = processWave(actionInput, context);
					}					
				}
				
				
				if(WPConstants.PREALLOCATE_WAVE.equalsIgnoreCase(action)){
					context.setNavigation("menuClickEvent756");
				}
				if(WPConstants.ALLOCATE_WAVE.equalsIgnoreCase(action)){
					context.setNavigation("menuClickEvent757");
				}			
				if(WPConstants.UNALLOCATE_WAVE.equalsIgnoreCase(action)){
					context.setNavigation("menuClickEvent758");
				}			
				if(WPConstants.RELEASE_WAVE.equalsIgnoreCase(action)){
					context.setNavigation("menuClickEvent759");
				}			
				if(WPConstants.SHIP_WAVE.equalsIgnoreCase(action)){
					
					if(edo.isAttrib(new TextData(WPConstants.IGNORE_INCOMPLETE_TASKS))){
						context.setNavigation("menuClickEvent752");
						return RET_CONTINUE;
					}else if(edo.isAttrib(new TextData(WPConstants.IGNORE_INCOMPLETE_TASKS))){
						context.setNavigation("menuClickEvent753");
						return RET_CONTINUE;
					}
					context.setNavigation("menuClickEvent760");

				}
				Query qry = new Query("wm_wp_wave", null, "wm_wp_wave.WAVEKEY DESC");
		 		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
				BioCollectionBean resultCollection = uowb.getBioCollectionBean(qry);			
				uowb.clearState();
				result.setFocus(resultCollection); 
				
//				((RuntimeListFormInterface) parentForm).setSelectedItems(null);
			}
			
		}
		else
		{
			DataBean focus = parentForm.getFocus();
			String wavekey = focus.getValue("WAVEKEY")==null?"":focus.getValue("WAVEKEY").toString();
			if(WPConstants.GET_CONSOLIDATED_SKU.equalsIgnoreCase(action)){
				com.infor.scm.waveplanning.wp_wm_wave.wave.WPUtil.populateConsolidate(state, wavekey);
				final Query eligibleSkusQuery = new Query("wp_eligibleconsolidatedskulistwaveheadervotemp",
						"wp_eligibleconsolidatedskulistwaveheadervotemp.INTERACTIONID = '" + state.getInteractionId()
								+ "' and wp_eligibleconsolidatedskulistwaveheadervotemp.WAVEKEY = '" + wavekey + "'",
						null);
				UnitOfWorkBean uow = state.getDefaultUnitOfWork();
				BioCollectionBean rs = uow.getBioCollectionBean(eligibleSkusQuery);
				try{
					if (rs.size()!= 0){				
						result.setFocus(rs);
						return RET_CONTINUE;
					}else{
						String args[] = new String[0];
						String errorMsg = getTextMessage("WPEXP_NOSKUTOCONSOL", args, state.getLocale());
						throw new UserException(errorMsg, new Object[0]);
					}
				}catch(EpiException e){
					e.printStackTrace();
					return RET_CANCEL;
				}
			
			
			}
			
			
			
			_log.debug("LOG_DEBUG_EXTENSION_WaveMaintProcessAction", "Called from Normal Form "
					+ parentForm.getName(), SuggestedCategory.NONE);
			EXEDataObject edo = null;
			if (focus.getValue("WAVEKEY") != null)
			{
				waveKey=focus.getValue("WAVEKEY").toString();
				ActionInputObj actionInput = new ActionInputObj();
				actionInput.setWaveKey(waveKey);
				actionInput.setAction(action);
				edo = processWave(actionInput, context);
			}
			
			
			
			
			
			String slotName = parentForm.getParentForm(state).getName();
			if(WPConstants.PREALLOCATE_WAVE.equalsIgnoreCase(action)){
				if("wp_wavemgmt_wavemaint_wave_header_detail_temp".equalsIgnoreCase(slotName)){
					context.setNavigation("menuClickEvent754");
				}else{//it is running from wave maintenance wave detail page
					context.setNavigation("menuClickEvent755");
				}
			}
			if(WPConstants.ALLOCATE_WAVE.equalsIgnoreCase(action)){
				if("wp_wavemgmt_wavemaint_wave_header_detail_temp".equalsIgnoreCase(slotName)){
					context.setNavigation("menuClickEvent598");
				}else{//it is running from wave maintenance wave detail page
					context.setNavigation("menuClickEvent752");
				}
			}			
			if(WPConstants.UNALLOCATE_WAVE.equalsIgnoreCase(action)){
				if("wp_wavemgmt_wavemaint_wave_header_detail_temp".equalsIgnoreCase(slotName)){
					context.setNavigation("menuClickEvent599");
				}else{//it is running from wave maintenance wave detail page
					context.setNavigation("menuClickEvent753");
				}
			}			
			if(WPConstants.RELEASE_WAVE.equalsIgnoreCase(action)){
				if("wp_wavemgmt_wavemaint_wave_header_detail_temp".equalsIgnoreCase(slotName)){
					context.setNavigation("menuClickEvent600");
				}else{//it is running from wave maintenance wave detail page
					context.setNavigation("menuClickEvent754");
				}
			}			
			if(WPConstants.SHIP_WAVE.equalsIgnoreCase(action)){
				
				if(edo.isAttrib(new TextData(WPConstants.IGNORE_INCOMPLETE_TASKS))){
					context.setNavigation("menuClickEvent752");
					return RET_CONTINUE;
				}else if(edo.isAttrib(new TextData(WPConstants.IGNORE_INCOMPLETE_TASKS))){
					context.setNavigation("menuClickEvent753");
					return RET_CONTINUE;
				}
				
				if("wp_wavemgmt_wavemaint_wave_header_detail_temp".equalsIgnoreCase(slotName)){
					context.setNavigation("menuClickEvent751");
				}else{//it is running from wave maintenance wave detail page
					context.setNavigation("menuClickEvent755");
				}
			}	
			
			
			
			Query qry = new Query("wm_wp_wave", "wm_wp_wave.WAVEKEY='"+waveKey+"'", null);
	 		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			BioCollectionBean resultCollection = uowb.getBioCollectionBean(qry);			
			uowb.clearState();
			try{
				result.setFocus(resultCollection.get("0")); 
			}catch(EpiDataException e){
				e.printStackTrace();
			}
			

		}		
		return RET_CONTINUE;
	}
	
	
	
	public EXEDataObject processWave(ActionInputObj actionInput, ActionContext context) throws UserException{
		String waveKey = actionInput.getWaveKey();
		String action = actionInput.getAction();
		
		WMWaveActionInterface actionInterface = WaveActionFactory.getWaveAction(action);
		
		//do validation
		WaveValidateInputObj waveValObj = new WaveValidateInputObj();
		waveValObj.setWaveKey(waveKey);
		waveValObj.setContext(context);
		waveValObj.setAction(action);
		actionInterface.validate(waveValObj); //if not passed, it will throw UserException
		
		//start doing wave action		
		WaveInputObj wpInput = new WaveInputObj();
		wpInput.setWaveKey(waveKey);
		

		if(WPConstants.SHIP_WAVE.equalsIgnoreCase(action)){
			wpInput.getProcedureParametes().add(new TextData(waveKey));
			wpInput.getProcedureParametes().add(new TextData("NO"));
			wpInput.getProcedureParametes().add(new TextData("NO"));			
		}
		try{
			EXEDataObject edo = actionInterface.doWaveAction(wpInput);
			return edo;			
		}catch(WebuiException e){
			e.printStackTrace();
			String [] ex = new String[1];
			ex[0]=e.getMessage();
			throw new UserException("WPEXE_APP_ERROR", ex);			
		}
	}

	
	

	
	
	public void processWave(StateInterface state, String facility, User user, int wid, ActionResult result, String facilityConnectionString) throws UserException
	{
/*		try
		{
			//			int wid = 0;
			int process = 0;
			String pValue = "";
			WaveVO waveVO = new WaveVO();
			WaveBD waveBD = new WaveBD();
			// get action
			String action = getParameterString("ACTION");

			//			if (focus.getValue("WAVEKEY") != null)
			//			{
			//				wid = Integer.parseInt(focus.getValue("WAVEKEY").toString());
			//			}

			_log.debug("LOG_DEBUG_EXTENSION_WaveMaintProcessAction_execute", "WaveKey " + wid, SuggestedCategory.NONE);
			if (wid > 0)
			{
				waveVO = waveBD.getWaveDetails(wid);
				process = waveVO.getInProcess();
				pValue = "" + process;
			}

			if (pValue.equals("21") || pValue.equals("22"))
			{
				pValue = pValue + "0";

			}
			if (pValue.indexOf('-') != -1 || pValue.indexOf('0') != -1)
			{
				if (action.equalsIgnoreCase(WavePlanningConstants.PROCESS_TOCONSOLIDATE))
				{
					ArrayList productLocations = new ArrayList();
					productLocations.add(waveBD.getProductLocationforConsolidation(facility, waveVO));
					productLocations.add(waveVO);

					//add to temp tables
					WPWaveMgmtUtil.clearTempTableBasedOnAddDate(state, "wp_speedlinelocationswaveheadervotemp");
					WPWaveMgmtUtil
							.clearTempTableBasedOnAddDate(state, "wp_eligibleconsolidatedskulistwaveheadervotemp");

					ProductLocationForConsolidationVO prodLoc = (ProductLocationForConsolidationVO) productLocations
							.get(0);
					UnitOfWorkBean uow = state.getDefaultUnitOfWork();

					//get speedline locations
					SpeedLineLocationsWaveVO speedLines = prodLoc.getSpeedLineLocationsWaveVO();

					//get speedline locations from the db based on interaction, wavekey
					final String interactionId = state.getInteractionId();
					final Query speedLineQuery = new Query("wp_speedlinelocationswaveheadervotemp",
							"wp_speedlinelocationswaveheadervotemp.INTERACTIONID = '" + interactionId
									+ "' and wp_speedlinelocationswaveheadervotemp.WAVEKEY = '" + wid + "'", null);
					BioCollectionBean rs = uow.getBioCollectionBean(speedLineQuery);
					_log.debug("LOG_DEBUG_EXTENSION_WaveMaintProcessAction_processWave", "Deleting records "
							+ speedLineQuery, SuggestedCategory.NONE);
					for (int i = 0; i < rs.size(); i++)
					{
						final BioBean row = rs.get("" + i);
						row.delete();
					}
					uow.saveUOW();

					//insert valid speed lines
					uow = state.getDefaultUnitOfWork();
					for (int i = 0; i < speedLines.getNumOfWaveHeaders(); i++)
					{
						//HC 04/20/2010 - Restricting the insert in to wp_speedlinelocationswaveheadervotemp if location is empty or null
						if((speedLines.getWaveHeader(i).getLoc()!= null)&&(!(speedLines.getWaveHeader(i).getLoc().trim().equalsIgnoreCase("")))){		//HC
							QBEBioBean qbeSpeed = uow.getQBEBioWithDefaults("wp_speedlinelocationswaveheadervotemp");
							qbeSpeed.setValue("INTERACTIONID", interactionId);
							qbeSpeed.setValue("WAVEKEY", new Integer(wid));
							qbeSpeed.setValue("LOC", speedLines.getWaveHeader(i).getLoc());
							qbeSpeed.setValue("DESCRIPTION", speedLines.getWaveHeader(i).getDescription());
							qbeSpeed.save();
						}		//HC
						
					}
					uow.saveUOW(true);

					//get eligible skus
					EligibleConsolidatedSkuListWaveVO eligibleSkus = prodLoc.getEligibleConsolidatedSkuListWaveVO();
					uow = state.getDefaultUnitOfWork();

					//get eligible skus from the db based on interaction, wavekey
					final Query eligibleSkusQuery = new Query("wp_eligibleconsolidatedskulistwaveheadervotemp",
							"wp_eligibleconsolidatedskulistwaveheadervotemp.INTERACTIONID = '" + interactionId
									+ "' and wp_eligibleconsolidatedskulistwaveheadervotemp.WAVEKEY = '" + wid + "'",
							null);
					rs = uow.getBioCollectionBean(eligibleSkusQuery);
					_log.debug("LOG_DEBUG_EXTENSION_WaveMaintProcessAction_processWave", "Deleting records "
							+ eligibleSkusQuery, SuggestedCategory.NONE);
					for (int i = 0; i < rs.size(); i++)
					{
						final BioBean row = rs.get("" + i);
						row.delete();
					}
					uow.saveUOW();

					uow = state.getDefaultUnitOfWork();
					for (int i = 0; i < eligibleSkus.getNumOfWaveHeaders(); i++)
					{
						final EligibleConsolidatedSkuListWaveHeaderVO eligibleSku = eligibleSkus.getWaveHeader(i);
						//HC 04/20/2010 - Restricting the insert in to wp_eligibleconsolidatedskulistwaveheadervotemp if sku is empty or null
						if((eligibleSku.getSku()!= null)&&(!(eligibleSku.getSku().trim().equalsIgnoreCase("")))){		//HC
						QBEBioBean qbeSku = uow.getQBEBioWithDefaults("wp_eligibleconsolidatedskulistwaveheadervotemp");
						qbeSku.setValue("INTERACTIONID", interactionId);
						qbeSku.setValue("WAVEKEY", new Integer(wid));
						qbeSku.setValue("INCLUDE", eligibleSku.getInclude());
						qbeSku.setValue("STORERKEY", eligibleSku.getStorerKey());
						qbeSku.setValue("SKU", eligibleSku.getSku());
						qbeSku.setValue("DESCR", eligibleSku.getDescr());
						qbeSku.setValue("ORDERTYPE", eligibleSku.getOrderType());
						qbeSku.setValue("CONSOLLOC", eligibleSku.getConsolloc());
						qbeSku.setValue("MAXLOCQTY", eligibleSku.getMaxlocqty());
						qbeSku.setValue("WAVETOTAL", eligibleSku.getWaveTotal());
						qbeSku.setValue("MINIMUMWAVEQTY", eligibleSku.getMinimumWaveQty());
						qbeSku.setValue("PICKPIECE", eligibleSku.getPickpiece());
						qbeSku.setValue("PICKCASE", eligibleSku.getPickcase());
						qbeSku.setValue("ASSIGNED", eligibleSku.getAssigned());
						qbeSku.setValue("SERIALKEY", eligibleSku.getSerialKey());
						qbeSku.save();
						}	//HC
					}
					uow.saveUOW(true);

					uow = state.getDefaultUnitOfWork();
					rs = uow.getBioCollectionBean(eligibleSkusQuery);
					//HC 04/20/2010 -Throw a message "There are no items that meet consolidation criteria on this wave." if the biocolleciton is empty
					if (rs.size()!= 0){				//HC
						result.setFocus(rs);
					//HC.b
					}else{
						String args[] = new String[0];
						String errorMsg = getTextMessage("WPEXP_NOSKUTOCONSOL", args, state.getLocale());
						throw new UserException(errorMsg, new Object[0]);
					}
					//HC.b
					return;

					//request.setAttribute("PRODUCTLOCATIONS", productLocations);
					//result = "consolidate";
				}
				else
				{
					if (action.equalsIgnoreCase(WavePlanningConstants.PROCESS_CONFIRM))
					{
						waveVO = waveBD.getShipmentOrderKeys(waveVO);
					}
					else if (action.equalsIgnoreCase(WavePlanningConstants.PROCESS_CONSOLIDATE))
					{

						//						int j = 0;
						ArrayList consolidatedList = new ArrayList();
						ConsolidatedProductLocationVO productLocationVO = null;
						RuntimeFormInterface toolbarForm = state.getCurrentRuntimeForm();
						RuntimeListFormInterface skuList = (RuntimeListFormInterface) toolbarForm.getParentForm(state);
						ArrayList selectedItems = skuList.getSelectedItems();
						for (int i = 0; i < selectedItems.size(); i++)
						{
							final DataBean sku = (BioBean) selectedItems.get(i);
							productLocationVO = new ConsolidatedProductLocationVO();
							productLocationVO.setSku(BioAttributeUtil.convertToString(sku, "SKU"));
							productLocationVO.setStorer(BioAttributeUtil.convertToString(sku, "STORERKEY"));
							productLocationVO.setSpeedLocation(BioAttributeUtil.convertToString(sku, "CONSOLLOC"));
							productLocationVO.setMaxQuantity(BioAttributeUtil.convertToString(sku, "MAXLOCQTY"));
							productLocationVO.setOrderType(BioAttributeUtil.convertToString(sku, "ORDERTYPE"));
							productLocationVO.setPickPiece(BioAttributeUtil.convertToString(sku, "PICKPIECE"));
							productLocationVO.setPickCase(BioAttributeUtil.convertToString(sku, "PICKCASE"));
							productLocationVO.setWaveTotal(BioAttributeUtil.convertToString(sku, "WAVETOTAL"));
							productLocationVO.setMinWaveQty(BioAttributeUtil.convertToString(sku, "MINIMUMWAVEQTY"));
							consolidatedList.add(productLocationVO);
						}
						waveVO.setConsolidatedList(consolidatedList);

					}
					waveVO.setUser(user);
					waveBD.processWave(facility, waveVO, action, true, facilityConnectionString); //Anand on 04/17/08 Added facilityConnectionString
					int status = waveVO.getStatus();
					//					if (status > 0 && status <= WavePlanningConstants.WAVE_STATUSES_MODIFIED)
					//						result = "confirmsuccess";
					//					else if (status == WavePlanningConstants.WAVE_STATUSES_CONFIRMED)
					//						result = "maintenancesuccess";
				}

			}
		} catch (SQLException e)
		{

			_log.error("LOG_ERROR_EXTENSION_ConfirmWaveConfirmWave", "SQLException " + e.getMessage(),
					SuggestedCategory.NONE);
			e.printStackTrace();
		} catch (WavePlanningException e)
		{

			_log.error("LOG_ERROR_EXTENSION_ConfirmWaveConfirmWave", "WavePlanningException " + e.getMessage(),
					SuggestedCategory.NONE);
			e.printStackTrace();

			String args[] = new String[0];
			String errorMsg = getTextMessage("WPEXP_SYS_EXP", args, state.getLocale());
			throw new UserException(errorMsg, new Object[0]);

		}
//		HC 04/20/2010 -Throw a message "There are no items that meet consolidation criteria on this wave." if the biocolleciton is empty
		//HC.b
		catch (UserException e)
		{
			_log.error("LOG_ERROR_EXTENSION_ConfirmWaveConfirmWave", "User Exception " + e.getMessage(),
					SuggestedCategory.NONE);
			e.printStackTrace();
			throw new UserException(e.getErrorName(), new Object[0]);
		} 
		//HC.e
		catch (Exception e)
		{
			_log.error("LOG_ERROR_EXTENSION_ConfirmWaveConfirmWave", "Generic Exception " + e.getMessage(),
					SuggestedCategory.NONE);
			e.printStackTrace();
			String args[] = new String[0];
			String errorMsg = getTextMessage("WPEXP_SYS_EXP", args, state.getLocale());
			throw new UserException(errorMsg, new Object[0]);
		}*/
	}

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked or
	 * a value entered in a form in a modal dialog
	 * Write code here if u want this to be called when the UI Action event is fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext} exposes information about the
	 * event, including the service and the user interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes information about the results
	 * of the action that has occurred, and enables your extension to modify them.</li>
	 * </ul>
	 */
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
	{

		try
		{
			StateInterface state = ctx.getState();

			RuntimeFormInterface listForm = ctx.getModalBodyForm(0);
			String facility = WPUtil.getFacility(state.getRequest());
			User user = null;

			final DataBean focus = listForm.getFocus();
			if (focus.isBioCollection() && ((BioCollectionBean) focus).size() >= 1)
			{
				BioCollectionBean rows = (BioCollectionBean) focus;
				BioBean firstRow = rows.get("0");
				int wid = BioAttributeUtil.getInt((DataBean) firstRow, "WAVEKEY");
				// Anand on 04/17/08 pass facility Connectionstring for Performance issue				
				//processWave(state, facility, user, wid, args);
//				processWave(state, facility, user, wid, args, WPUtil.getFacilityConnectionString(state.getRequest()));				
				// Anand ends				
				
			}
			else
			{
				_log.error("LOG_ERROR_EXTENSION_WaveMaintProcessAction_execute", "No Items to Consolidate",
						SuggestedCategory.NONE);
				throw new UserException("WPEXP_WAVE_CONSOLIDATE_SELECTSKU", new Object[] {});
			}

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
