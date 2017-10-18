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

package com.ssaglobal.scm.wms.wm_inventory_move.action;

// Import 3rd party packages and classes
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.math.BigDecimal;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpSession;

// Import Epiphany packages and classes
import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.wm_table_validation.ccf.NumericValidationCCF;
import com.ssaglobal.scm.wms.util.UserUtil;
//Krishna Kuchipudi: Dec-03-2009 3PL Enhancements -End to End catch weight tracking starts
import com.ssaglobal.scm.wms.wm_cyclecount.ui.CalculateAdvCatchWeightsHelper;
//Krishna Kuchipudi: Dec-03-2009: 3PL Enhancements -End to End catch weight Tracking ends


/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class InventoryMoveExecuteMovesAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(InventoryMoveExecuteMovesAction.class);

	EpnyUserContext userContext;
	ActionContext actionContext;
	String contextVariable;
	String contextVariableArgs;
	RuntimeListFormInterface imListForm;
	LocaleInterface locale;
	BioBean selectedMove;
	String sessionVariable;
	String sessionObjectValue;
	static String TO_QTY = "QUANTITYTOMOVE";
	
	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * @throws EpiException 
	 */
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		actionContext = context;

		_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of InventoryMoveExecuteMovesAction", SuggestedCategory.NONE);
		
		_log.debug("LOG_SYSTEM_OUT","\n\nBKLECKA: inside InventoryMoveExecuteMovesAction\n\n",100L);

		StateInterface state = context.getState();
		String interactionID = state.getInteractionId();
		String contextVariablePrefix = "INVMOVEERROR";
		contextVariable = contextVariablePrefix + interactionID;
		contextVariableArgs = contextVariablePrefix + "ARGS" + interactionID;

		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		locale = mda.getLocale(userLocale);

		UnitOfWorkBean uow = state.getDefaultUnitOfWork();

		imListForm = (RuntimeListFormInterface) FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_inventory_move__view", state);

		//SM 07/31/07 ISSUE #SCM-00000-02843 Multiple Execute Moves actions fail
		//All selected BioRefs
		
		//Identify list position from session object
		String contextVariableSuffix = "WINDOWSTART";
		sessionVariable = interactionID + contextVariableSuffix;
		HttpSession session = context.getState().getRequest().getSession();
		sessionObjectValue = (String)session.getAttribute(sessionVariable);
		int winStart = Integer.parseInt(sessionObjectValue);
		
		//Iterate viewed list records
		int winSize = imListForm.getWindowSize();
		BioCollectionBean bcb = (BioCollectionBean)imListForm.getFocus();
		int bcSize = bcb.size();
		int cycle = (bcSize-winStart)<winSize ? (bcSize-winStart) : winSize ;
		for( int index = 0; index < cycle; index++ )
		{
			selectedMove = (BioBean)bcb.elementAt(index+winStart);
			
			double toQty = Double.parseDouble(selectedMove.getValue(TO_QTY).toString());
			if( toQty > 0 )
			{
				
				_log.debug("LOG_DEBUG_EXTENSION", "###! ID " + selectedMove.getValue("ID"), SuggestedCategory.NONE);
				_log.debug("LOG_DEBUG_EXTENSION", "###! QA " + selectedMove.getValue("QTYALLOCATED"), SuggestedCategory.NONE);
				
				//valid location
				if (requiredField("TOLOCATION", selectedMove) == false){
					return RET_CONTINUE;
				}
				if (validation("TOLOCATION", "LOC", "LOC", selectedMove) == false){
					return RET_CONTINUE;
				}

				if (greaterThanOrEqualToZeroValidation("QUANTITYTOMOVE", selectedMove) == false){
					return RET_CONTINUE;
				}
				if (lessThanValidation("QUANTITYTOMOVE", "QTY", selectedMove) == false){
					return RET_CONTINUE;
				}
				//Krishna Kuchipudi: Dec-03-2009: 3PL Enhancements -End to End Catch Weight related code starts
				String enableAdvCatchWeight=null;

				String storerkey1 = (String)selectedMove.get("STORERKEY");
				String sku1 = (String)selectedMove.get("SKU");

				CalculateAdvCatchWeightsHelper CalculateAdvCatchWeightsHelper1 = new CalculateAdvCatchWeightsHelper();

				enableAdvCatchWeight = CalculateAdvCatchWeightsHelper1.isAdvCatchWeightEnabled(storerkey1,sku1);

//				if(enableAdvCatchWeight.equalsIgnoreCase("1")){


					Object TOGROSSWGT 	 = selectedMove.getValue("GROSSWGT");
					Object TONETWGT	 = selectedMove.getValue("NETWGT");
					Object TOTAREWGT     = selectedMove.getValue("TAREWGT");

					String ToGrossWGT = TOGROSSWGT.toString();
					String ToNetWGT 	= TONETWGT.toString();
					String ToTareWGT = TOTAREWGT.toString();



					double ToGROSSWT = NumericValidationCCF.parseNumber(ToGrossWGT);
					double ToNETWT = NumericValidationCCF.parseNumber(ToNetWGT);
					double ToTAREWT = NumericValidationCCF.parseNumber(ToTareWGT);
					
					if(Math.abs(ToGROSSWT-ToNETWT-ToTAREWT)>0.000001){
						actionContext.setNavigation("menuClickEvent833");
						return RET_CONTINUE;
						
//						String[] parameters = new String[1];
//						throw new UserException("WMEXP_CC_MV_WGTS_EQU", parameters);
					}

//				}

				//Krishna Kuchipudi: Dec-03-2009: 3PL Enhancements -End to End Catch Weight related code ends

				Object fromID = selectedMove.getValue("ID") == null ? null : selectedMove.getValue("ID").toString().toUpperCase();
				Object toID = selectedMove.getValue("TOLPN") == null ? null : selectedMove.getValue("TOLPN").toString().toUpperCase();
				Object toLoc = selectedMove.getValue("TOLOCATION") == null ? null : selectedMove.getValue("TOLOCATION").toString().toUpperCase();
				if (!isEmpty(fromID)){
					//WM9.2_AutoMoveChanges.b
					if (userContext.get("moveContinue") == null){
						String amStrategyQuery = "SELECT  AMSTRATEGYKEY, TASKDETAILKEY FROM TASKDETAIL WHERE FROMID = '"+fromID+"'AND STATUS in ('0', 's')";
						EXEDataObject resultsAmStrategy = WmsWebuiValidationSelectImpl.select(amStrategyQuery);
						String amStrategyKey = null;
						String taskDetailkey = null;
						if (resultsAmStrategy.getRowCount() > 0){
							if (resultsAmStrategy.getAttribValue(1) != null){
								amStrategyKey = resultsAmStrategy.getAttribValue(1).getAsString();
								taskDetailkey = resultsAmStrategy.getAttribValue(2).getAsString();
								userContext.put("cTaskdetailkey", taskDetailkey);
								userContext.put("cAMstrategy", amStrategyKey);
								userContext.put(contextVariable, "WMEXP_AUTOMV_MOVE");
								actionContext.setNavigation("menuClickEvent919");
								return RET_CONTINUE;
							}
						}
					}
					//WM9.2_AutoMoveChanges.e
					String fromIDQuery = "SELECT INVENTORYHOLDKEY FROM INVENTORYHOLD WHERE id = '"+fromID+"' AND Hold = '1'";
					EXEDataObject resultsFromId = WmsWebuiValidationSelectImpl.select(fromIDQuery);
					boolean fromIDOnHold = resultsFromId.getRowCount() > 0 ? true : false;
					if ((fromIDOnHold == true)){ 			//if fromID is on hold
						String toIDQuery = "SELECT INVENTORYHOLDKEY FROM INVENTORYHOLD WHERE id = '"+toID+"' AND Hold = '1'";
						EXEDataObject resultsToId = WmsWebuiValidationSelectImpl.select(toIDQuery);
						boolean toIDOnHold = resultsToId.getRowCount() > 0 ? true : false;
						if (toIDOnHold == true && (!isEmpty(toID))){		 //if toID is on hold
							//check location
							String locQuery = "SELECT LOSEID, LocationType FROM LOC " + " WHERE LOC.Loc='" + toLoc + "'";
							EXEDataObject resultsLoc = WmsWebuiValidationSelectImpl.select(locQuery);
							String loseId = null;
							String locType = null;
							if (resultsLoc.getRowCount() > 0){
								loseId = resultsLoc.getAttribValue(1).getAsString();
								locType = resultsLoc.getAttribValue(2).getAsString();
							}

							if (loseId.equals("1")){ 			//if loseid is set
								//PB_NMOVE_HOLDIDMOVENA_1
								result.setFocus(imListForm.getFocus());
								//throw new UserException("WMEXP_IM_HOLD_1", new Object[] {});

								userContext.put(contextVariable, "WMEXP_IM_HOLD_1");
								actionContext.setNavigation("menuClickEvent565");
								return RET_CONTINUE;
							}else if (locType.equalsIgnoreCase("CASE") || locType.equalsIgnoreCase("PICK")){ //if location type is CASE or PICK
								//get NSQL flags
								String allowQuery = "SELECT NSQLVALUE FROM NSQLCONFIG WHERE NSQLCONFIG.Configkey ='ALLOWOVERALLOCATIONS' ";
								EXEDataObject resultsAllow = WmsWebuiValidationSelectImpl.select(allowQuery);
								String allowOverAllocation = null;
								if (resultsAllow.getRowCount() > 0){
									allowOverAllocation = resultsAllow.getAttribValue(1).getAsString();
								}

								String autoQuery = "SELECT NSQLVALUE FROM NSQLCONFIG WHERE NSQLCONFIG.Configkey ='AUTOLOSEIDWITHOVERALLOCATION' ";
								EXEDataObject resultsAuto = WmsWebuiValidationSelectImpl.select(autoQuery);
								String autoLoseidWithOverAllocation = null;
								if (resultsAuto.getRowCount() > 0){
									autoLoseidWithOverAllocation = resultsAuto.getAttribValue(1).getAsString();
								}

								if (allowOverAllocation.equals("1") && autoLoseidWithOverAllocation.equals("1")){ //if both flags are set
									//PB_NMOVE_HOLDIDMOVENA_2
									result.setFocus(imListForm.getFocus());
									//throw new UserException("WMEXP_IM_HOLD_2", new Object[] {});

									userContext.put(contextVariable, "WMEXP_IM_HOLD_2");
									actionContext.setNavigation("menuClickEvent565");
									return RET_CONTINUE;
								}
							}
						}else{
							//if toID is not hold
							//PB_NMOVE_HOLDIDMOVENA_3
							result.setFocus(imListForm.getFocus());
							//throw new UserException("WMEXP_IM_HOLD_3", new Object[] {});

							userContext.put(contextVariable, "WMEXP_IM_HOLD_3");
							actionContext.setNavigation("menuClickEvent565");
							return RET_CONTINUE;
						}
					}
				}
				
				BigDecimal qty = (BigDecimal)selectedMove.get("QTY");
				BigDecimal qtyToMove = (BigDecimal)selectedMove.get("QUANTITYTOMOVE");
				
				if ( qtyToMove.compareTo(qty) != 0 )				
				{
					// not moving the full LPN qty.  check if this is an end to end serial sku
					String partStr = "SELECT SNUM_ENDTOEND from SKU where SKU = '"+retrieveData("SKU")+"' and STORERKEY = '"+retrieveData("STORERKEY")+"'";
					_log.debug("LOG_SYSTEM_OUT",partStr,100L);
					EXEDataObject resultsFromSku = WmsWebuiValidationSelectImpl.select(partStr);
					
					if (resultsFromSku.getRowCount() > 0 && resultsFromSku.getAttribValue(1).getAsString().equals("1") )
					{
						// its end to end serial and a partial qty. 
						// Check if we have TransferDetailSerial bios to match this row.
						String storerkey = (String)selectedMove.get("STORERKEY");
						String sku = (String)selectedMove.get("SKU");
						String lot = (String)selectedMove.get("LOT");
						String loc = (String)selectedMove.get("LOC");
						String id = (String)selectedMove.get("ID");
						String key = storerkey.trim()+"_"+sku.trim()+"_"+lot.trim()+"_"+loc.trim()+"_"+id.trim()+"_SERIALS";
						String cache = (String)session.getAttribute(key);
						
						if (cache == null)
						{
							// need to handle serial numbers for this line yet.
							session.setAttribute("InventoryMove_QTYTOMOVE", qtyToMove.toString());
							session.setAttribute("InventoryMove_QTYSELECTED", "0");
							actionContext.setNavigation("menuClickEvent666");
							result.setFocus(selectedMove);
							return RET_CONTINUE;
						}
						else
						{
							//TODO:  clear out any other wm_serialmove records for this key.
							
							// user entered something on the other page.  put it in the DB
							String serials[] = cache.split("\\|");
							
							if (qtyToMove.doubleValue() != serials.length)
							{								
								result.setFocus(imListForm.getFocus());
								userContext.put(contextVariable, "WM_EXP_NOTENOUGH_SERIALS_SELECTED");
								actionContext.setNavigation("menuClickEvent565");
								return RET_CONTINUE;								
							}
							
							for ( int idx = 0; idx < serials.length; ++idx)
							{								
								QBEBioBean  serial = uow.getQBEBioWithDefaults("wm_serialmove");
								serial.set("STORERKEY", storerkey);
								serial.set("SKU", sku);
								serial.set("LOT", lot);
								serial.set("LOC", loc);
								serial.set("ID", id);
								serial.set("SERIALNUMBER", serials[idx]);	
								serial.save();
							}
							
							session.removeAttribute(key);
							uow.saveUOW(true);
						}
					}
				}				

				_log.debug("LOG_DEBUG_EXTENSION", "&^^ Preparing Stored Procedure", SuggestedCategory.NONE);
				//stored proc
				TextData nullData = new TextData("null");
				TextData zeroData = new TextData("0");

				//prepare stored procedure
				WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				Array params = new Array();
				/*
				 * nspItrnAddMove", {ls_null, as_storerkey, as_sku, as_lot, as_id, as_loc, &
			 	as_toloc, as_toid, ls_status, ls_lottable01, &
			 	ls_lottable02, ls_lottable03, gnv_app.of_GetForte().of_date(String(ldt_lottable04)), &
			 	gnv_app.of_GetForte().of_date(String(ldt_lottable05)), ls_lottable06,ls_lottable07, &
			 	ls_lottable08, ls_lottable09, ls_lottable10, "0", "0", String(al_toqty), &
			 	"0", "0", "0", "0", "0", "0", "", "Console Move", as_topackkey, &
			 	as_touom, "1", gnv_app.of_GetForte().of_date(String(ldt_effectivedate)), ls_itrnkey, &
			 	String(li_success), String(ll_err)}, ls_result[])
				 */
				int temp;
				params.add(nullData);
				params.add(retrieveData("STORERKEY"));
				params.add(retrieveData("SKU"));
				params.add(retrieveData("LOT"));
				params.add(retrieveData("ID"));
				params.add(retrieveData("LOC"));
				params.add(retrieveData("TOLOCATION"));
				params.add(retrieveData("TOLPN"));
				
				//add to fix bug 8681************************
				String lot = retrieveData("LOT").toString().trim();
				String loc = retrieveData("LOC").toString().trim();
				String ID = retrieveData("ID").toString().trim();
				if("".equalsIgnoreCase(ID)){
					ID = " ";
				}
				if(this.isOnlyLocOnHold(loc,lot,ID)){
					params.add(new TextData("OK"));
				}else{
					params.add(retrieveData("STATUS")); //status					
				}
				//end ****************************************
				
				
				for(temp=0; temp<10; temp++)
				{
					params.add(nullData); //lottable1-10
				}
				params.add(zeroData);
				params.add(zeroData);
				params.add(retrieveData("QUANTITYTOMOVE"));
				for(temp=0; temp<6; temp++)
				{
					params.add(zeroData);
				}
				params.add(new TextData(""));
				params.add(new TextData("Console Move"));
				params.add(retrieveData("TOPACK"));
				params.add(retrieveData("TOUOM"));
				params.add(new TextData("1"));
				//WM9.2_AutoMoveChanges.b
				if (userContext.get("moveContinue") != null){
					params.add(nullData);		//EffectiveDate
					params.add(new TextData(userContext.get("moveContinue").toString()));		//dummy1 //yes no flag
					params.add(new TextData(userContext.get("cTaskdetailkey").toString()));		//dummy2 // Taksdetail key
					params.add(new TextData(userContext.get("cAMstrategy").toString()));		//dummy3 // AMstrategykey
				}else{
					for(temp=0; temp<4; temp++)
					{
						params.add(nullData);  //itrnkey, success, err
					}
				}
				//WM9.2_AutoMoveChanges.e
				//Krishna Kuchipudi: Dec-03-2009: 3PL Enhancements -Added the below weight fields--Starts
				String locQuery = "SELECT GROSSWGT,NETWGT,TAREWGT, QTY FROM LOTXLOCXID WHERE lot = '"+lot+"' AND loc = '"+loc+"' AND id = '"+ID+"'";
				EXEDataObject rs = WmsWebuiValidationSelectImpl.select(locQuery);

				double grossWeight = 0;
				double netWeight = 0;
				double tareWeight = 0;
				double quantity = 0;
				if (rs.getRowCount() > 0){
					grossWeight = Double.parseDouble(rs.getAttribValue(1).getAsString());
					netWeight = Double.parseDouble(rs.getAttribValue(2).getAsString());
					tareWeight = Double.parseDouble(rs.getAttribValue(3).getAsString());
					quantity = Double.parseDouble(rs.getAttribValue(4).getAsString());
				}
				if(Math.abs(quantity) > 0.000001){
					if(Math.abs(grossWeight-ToGROSSWT) < 0.000001
							&& Math.abs(netWeight-ToNETWT) < 0.000001
							&& Math.abs(tareWeight-ToTAREWT) < 0.000001){
						
						double rate = toQty/quantity;
						grossWeight = grossWeight*rate;
						netWeight = netWeight*rate;
						tareWeight = tareWeight*rate;
						selectedMove.set("GROSSWGT",new Double(grossWeight).toString());
						selectedMove.set("NETWGT",new Double(netWeight).toString());
						selectedMove.set("TAREWGT",new Double(tareWeight).toString());
					}
				}
				

				
				
				
				
				params.add(retrieveData("GROSSWGT"));
				params.add(retrieveData("NETWGT"));
				params.add(retrieveData("TAREWGT"));
				//Krishna Kuchipudi: Dec-03-2009: 3PL Enhancements -Added the below weight fields--Ends

				actionProperties.setProcedureParameters(params);
				actionProperties.setProcedureName("nspItrnAddMove");
				_log.debug("LOG_DEBUG_EXTENSION", "&^^ Running Stored Procedure", SuggestedCategory.NONE);
				//run stored procedure
				EXEDataObject resultsProc = null;
				try
				{
					resultsProc = WmsWebuiActionsImpl.doAction(actionProperties);
				}
				catch(Exception e)
				{
					_log.debug("LOG_DEBUG_EXTENSION", "\n!\n!\n!\n!\n!" + e.getClass().getName(), SuggestedCategory.NONE);
					result.setFocus(imListForm.getFocus());

					Pattern errorPattern = Pattern.compile("\\d*:\\d*:([\\w\\s]*)$");
					String exceptionMessage = e.getMessage();
					Matcher matcher = errorPattern.matcher(exceptionMessage);
					if (matcher.find())
					{
						exceptionMessage = matcher.group(1);
					}
					//throw new UserException(exceptionMessage, new Object[] {});

					userContext.put(contextVariable, exceptionMessage);
					actionContext.setNavigation("menuClickEvent565");
					return RET_CONTINUE;
				}
				//	Krishna Kuchipudi: 3PL Enhancements: April 20 -2010 :Starts
				String query = "SELECT GROSSWGT,NETWGT,TAREWGT FROM LOTXLOCXID WHERE lot = '"+lot+"' AND loc = '"+loc+"' AND id = '"+ID+"'";
				EXEDataObject resultsLoc1 = WmsWebuiValidationSelectImpl.select(query);

				String GROSSWGT1 = "0";
				String NETWGT1 = "0";
				String TAREWGT1 = "0";
				if (resultsLoc1.getRowCount() > 0){
					GROSSWGT1 = resultsLoc1.getAttribValue(1).getAsString();
					NETWGT1 = resultsLoc1.getAttribValue(2).getAsString();
					TAREWGT1 = resultsLoc1.getAttribValue(3).getAsString();
				}
				selectedMove.set("GROSSWGT",GROSSWGT1);
				selectedMove.set("NETWGT",NETWGT1);
				selectedMove.set("TAREWGT",TAREWGT1);

				//Krishna Kuchipudi: 3PL Enhancements: April 20 -2010 : Ends

				_log.debug("LOG_DEBUG_EXTENSION", "&^^ Results of Stored Procedure", SuggestedCategory.NONE);
				//handle results
				if (resultsProc.getColumnCount() != 0)
				{
					_log.debug("LOG_DEBUG_EXTENSION", "---Results", SuggestedCategory.NONE);
					for (int i = 1; i < resultsProc.getColumnCount(); i++)
					{
						try
						{
							_log.debug("LOG_DEBUG_EXTENSION", " "+i+" @ "+resultsProc.getAttribute(i).name+" "+resultsProc.getAttribute(i).value.getAsString(), SuggestedCategory.NONE);
						}
						catch(Exception e)
						{
							_log.debug("LOG_DEBUG_EXTENSION", e.getClass().getName(), SuggestedCategory.NONE);
							return RET_CANCEL;
						}
					}
					_log.debug("LOG_DEBUG_EXTENSION", "----------", SuggestedCategory.NONE);
				}
			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","BKLECKA: "+selectedMove.get("SERIALKEY")+" HAS NOT BEEN UPDATED",100L);	
			}
		}
		uow.saveUOW();
		uow.clearState();
		//SM 07/31/07 ISSUE #SCM-00000-02843 Multiple Execute Moves actions fail
		imListForm.setSelectedItems(null);
		//SM 07/31/07 End change
		result.setFocus(imListForm.getFocus());
		//WM9.2_AutoMoveChanges.b
		userContext.remove("moveContinue");
		userContext.remove("cTaskdetailkey");
		userContext.remove("cAMstrategy");
		//WM9.2_AutoMoveChanges.e
		return RET_CONTINUE;
	}
	
	/*
	 * to fix bug#8681
	 */
	private boolean isOnlyLocOnHold(String loc, String lot, String ID) throws EpiException{
		boolean locOnHold = false;
		if(!"".equalsIgnoreCase(loc)){
			String locQuery = "SELECT INVENTORYHOLDKEY FROM INVENTORYHOLD WHERE loc = '"+loc+"' AND Hold = '1'";
			EXEDataObject resultsLoc = WmsWebuiValidationSelectImpl.select(locQuery);
			if(resultsLoc.getRowCount() > 0){
				locOnHold = true;
			}
		}
		boolean lotOnHold = false;
		if(!"".equalsIgnoreCase(lot)){
			String lotQuery = "SELECT INVENTORYHOLDKEY FROM INVENTORYHOLD WHERE lot = '"+lot+"' AND Hold = '1'";
			EXEDataObject resultsLot = WmsWebuiValidationSelectImpl.select(lotQuery);
			if(resultsLot.getRowCount() > 0){
				lotOnHold = true;
			}
		}

		
		boolean idOnHold = false;
		if(!"".equalsIgnoreCase(ID)){
			String idQuery = "SELECT INVENTORYHOLDKEY FROM INVENTORYHOLD WHERE id = '"+ID+"' AND Hold = '1'";
			EXEDataObject resultsID = WmsWebuiValidationSelectImpl.select(idQuery);
			if(resultsID.getRowCount() > 0){
				idOnHold = true;
			}
		}
		
		if(locOnHold && !lotOnHold && !idOnHold){//means only loc on hold
			return true;
		}
		return false;
	}
	
	
	TextData retrieveData(String name){
		Object value = selectedMove.getValue(name);
		if (isNull(value)){
			return new TextData("");
		}else{
			return new TextData(value.toString().toUpperCase());
		}
	}

	protected boolean lessThanValidation(String smallerAttributeName, String largerAttributeName, BioBean focus) throws UserException {
		if (numericValidation(smallerAttributeName, focus) == false){
			return false;
		}
		if (numericValidation(largerAttributeName, focus) == false){
			return false;
		}
		//if it passes numericValidation, you can parse the number
		Object smallerTempValue = focus.getValue(smallerAttributeName);
		Object largerTempValue = focus.getValue(largerAttributeName);
		if (isNull(smallerTempValue) || isNull(largerTempValue)){
			return true;
		}

		String smallerAttributeValue = smallerTempValue.toString();
		String largerAttributeValue = largerTempValue.toString();

		double smallerValue = NumericValidationCCF.parseNumber(smallerAttributeValue);
		double largerValue = NumericValidationCCF.parseNumber(largerAttributeValue);

		if (Double.isNaN(smallerValue)){
			String[] parameters = new String[2];
			parameters[0] = imListForm.getFormWidgetByName(smallerAttributeName).getLabel("label", locale);
			parameters[1] = smallerAttributeValue;
			//throw new UserException("WMEXP_FORM_NON_NUMERIC", parameters);

			userContext.put(contextVariable, "WMEXP_FORM_NON_NUMERIC");
			userContext.put(contextVariableArgs, parameters);
			actionContext.setNavigation("menuClickEvent565");

			return false;
		}
		if (Double.isNaN(largerValue)){
			String[] parameters = new String[2];
			parameters[0] = imListForm.getFormWidgetByName(largerAttributeName).getLabel("label", locale);
			parameters[1] = largerAttributeValue;
			//throw new UserException("WMEXP_FORM_NON_NUMERIC", parameters);

			userContext.put(contextVariable, "WMEXP_FORM_NON_NUMERIC");
			userContext.put(contextVariableArgs, parameters);
			actionContext.setNavigation("menuClickEvent565");

			return false;
		}
		if (smallerValue > largerValue){
			//throw exception
			String[] parameters = new String[2];
			parameters[0] = imListForm.getFormWidgetByName(smallerAttributeName).getLabel("label", locale);
			parameters[1] = imListForm.getFormWidgetByName(largerAttributeName).getLabel("label", locale);
			//throw new UserException("WMEXP_LESSEQUAL", parameters);

			userContext.put(contextVariable, "WMEXP_LESSEQUAL");
			userContext.put(contextVariableArgs, parameters);
			actionContext.setNavigation("menuClickEvent565");

			return false;
		}
		return true;
	}

	protected boolean numericValidation(String attributeName, BioBean focus) throws UserException {
		Object attributeValue = focus.getValue(attributeName);
		if (!isEmpty(attributeValue) && (NumericValidationCCF.isNumber(attributeValue.toString()) == false)){
			//throw exception
			String[] parameters = new String[2];
			parameters[0] = imListForm.getFormWidgetByName(attributeName).getLabel("label", locale);
			parameters[1] = attributeValue.toString();
			//throw new UserException("WMEXP_FORM_NON_NUMERIC", parameters);

			userContext.put(contextVariable, "WMEXP_FORM_NON_NUMERIC");
			userContext.put(contextVariableArgs, parameters);
			actionContext.setNavigation("menuClickEvent565");

			return false;
		}
		return true;
	}

	protected boolean greaterThanOrEqualToZeroValidation(String attributeName, BioBean focus) throws UserException {
		if (numericValidation(attributeName, focus) == false){
			return false;
		}
		//if it passes numericValidation, you can parse the number
		Object tempValue = focus.getValue(attributeName);
		if (isNull(tempValue)){
			return true;
		}
		String attributeValue = tempValue.toString();
		double value = NumericValidationCCF.parseNumber(attributeValue);
		_log.debug("LOG_DEBUG_EXTENSION", "Value of " + attributeName + " - " + value, SuggestedCategory.NONE);
		if (Double.isNaN(value)){
			String[] parameters = new String[2];
			parameters[0] = imListForm.getFormWidgetByName(attributeName).getLabel("label", locale);
			parameters[1] = attributeValue.toString();

			//throw new UserException("WMEXP_FORM_NON_NUMERIC", parameters);

			userContext.put(contextVariable, "WMEXP_FORM_NON_NUMERIC");
			userContext.put(contextVariableArgs, parameters);
			actionContext.setNavigation("menuClickEvent565");

			return false;
		}else if (value < 0){
			String[] parameters = new String[2];
			parameters[0] = imListForm.getFormWidgetByName(attributeName).getLabel("label", locale);
			parameters[1] = attributeValue.toString();

			//throw new UserException("WMEXP_FORM_NONNEG_VALIDATION", parameters);

			userContext.put(contextVariable, "WMEXP_FORM_NONNEG_VALIDATION");
			userContext.put(contextVariableArgs, parameters);
			actionContext.setNavigation("menuClickEvent565");

			return false;
		}
		return true;
	}

	boolean requiredField(String attribute, BioBean focus) throws UserException {
		Object attributeValue = focus.getValue(attribute);
		_log.debug("LOG_DEBUG_EXTENSION", "" + attributeValue, SuggestedCategory.NONE);
		if (isEmpty(attributeValue)){
			//throw exception
			String[] parameters = new String[1];
			parameters[0] = imListForm.getFormWidgetByName(attribute).getLabel("label", locale);
			
			//SM 07/31/07 ISSUE #SCM-00000-02843 Multiple Execute Moves actions fail (Record specific error message)
			parameters[0] = parameters[0]+": "+imListForm.getFormWidgetByName("STORERKEY").getLabel("label", locale)+"("+focus.get("STORERKEY").toString()+"), ";
			parameters[0] = parameters[0]+imListForm.getFormWidgetByName("SKU").getLabel("label", locale)+"("+focus.get("SKU").toString()+"), ";
			parameters[0] = parameters[0]+imListForm.getFormWidgetByName("LOT").getLabel("label", locale)+"("+focus.get("LOT").toString()+"), ";
			parameters[0] = parameters[0]+imListForm.getFormWidgetByName("LOC").getLabel("label", locale)+"("+focus.get("LOC").toString()+"), ";
			parameters[0] = parameters[0]+imListForm.getFormWidgetByName("ID").getLabel("label", locale)+"("+focus.get("ID").toString()+")";
			//SM 07/31/07 End change
			
			//throw new UserException("WMEXP_REQFIELD", parameters);

			userContext.put(contextVariable, "WMEXP_REQFIELD");
			userContext.put(contextVariableArgs, parameters);
			actionContext.setNavigation("menuClickEvent565");
			return false;
		}
		return true;
	}

	protected boolean validation(String attributeName, String table, String tableAttribute, BioBean focus) throws EpiDataException, UserException{
		if (verifySingleAttribute(attributeName, table, tableAttribute, focus) == false){
			//throw exception
			String[] parameters = new String[2];
			parameters[0] = imListForm.getFormWidgetByName(attributeName).getLabel("label", locale);
			parameters[1] = focus.getValue(attributeName).toString();

			//throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);

			userContext.put(contextVariable, "WMEXP_WIDGET_DOES_NOT_EXIST");
			userContext.put(contextVariableArgs, parameters);
			actionContext.setNavigation("menuClickEvent565");

			return false;
		}
		return true;
	}

	protected boolean verifySingleAttribute(String attributeName, String table, String tableAttribute, BioBean focus) throws EpiDataException{
		Object attributeValue = focus.getValue(attributeName);
		if (isEmpty(attributeValue)){
			return true; //Do Nothing
		}
		attributeValue = attributeValue == null ? null : attributeValue.toString().toUpperCase();
		String query = "SELECT * FROM " + table + " WHERE " + tableAttribute + " = '" + attributeValue.toString() + "'";
		_log.debug("LOG_DEBUG_EXTENSION", "Query\n" + query, SuggestedCategory.NONE);
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() == 1){
			//value exists, verified
			return true;
		}else{
			//value does not exist
			return false;
		}
	}

	private boolean isEmpty(Object attributeValue){
		if (attributeValue == null){
			return true;
		}else if (attributeValue.toString().matches("\\s*")){
			return true;
		}else{
			return false;
		}
	}

	private boolean isNull(Object attributeValue){
		if (attributeValue == null){
			return true;
		}else{
			return false;
		}
	}
}