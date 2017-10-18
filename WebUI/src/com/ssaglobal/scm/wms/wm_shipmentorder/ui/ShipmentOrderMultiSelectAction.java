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
package com.ssaglobal.scm.wms.wm_shipmentorder.ui;

//Import 3rd party packages and classes
import java.util.ArrayList;
import java.util.Iterator;

//Import Epiphany packages and classes
import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.uiextensions.KeyGenBioWrapper;

public class ShipmentOrderMultiSelectAction extends ActionExtensionBase{
	private final static String WAVE_TABLE = "wm_wave";
	private final static String WD_TABLE = "wm_wavedetail";
	private final static String TASK_TABLE = "wm_taskdetail";
	
	private final static String WAVE = "WAVEKEY";
	private final static String WD = "WAVEDETAILKEY";
	private final static String ORDER = "ORDERKEY";
	private final static String BATCH = "BATCHFLAG";
	
	private final static String NAV_MULTI = "multiSelectMCE";
	private final static String NAV_SINGLE = "singleSelectMCE";
	private final static String ALLOCATE = "Allocate";
	private final static String RELEASE = "Release";
	private final static String SENDTOTM = "Send to TM";
	
	private final static String PROC_NAME_ALLOC = "NSPORDERPROCESSING";
	private final static String PROC_NAME_RELEASE = "NSPRELEASEWAVE";
	private final static String ERROR_MESSAGE_NO_SELECT = "WMEXP_NONE_SELECTED";
	private final static String ERROR_MESSAGE_PRE_BATCHED = "WMEXP_SO_AllocAction";
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ShipmentOrderMultiSelectAction.class);
	
	protected int execute(ActionContext context, ActionResult result) throws UserException, EpiException{
		//Identify if list_slot_1 is list form to process multi-select
		StateInterface state = context.getState();
		RuntimeFormInterface form = state.getRuntimeForm(context.getSourceWidget().getForm().getParentForm(state).getSubSlot("list_slot_1"), null);
		DataBean currentFocus = form.getFocus();

		if(currentFocus.isBioCollection()){
			RuntimeListFormInterface list = (RuntimeListFormInterface)form;
			result.setFocus(currentFocus);
			context.setNavigation(NAV_MULTI);
			ArrayList selected = list.getSelectedItems();
			if(isNull(selected)){
				//nothing to process
				throw new UserException(ERROR_MESSAGE_NO_SELECT, new Object[] {});
			}
			list.setSelectedItems(null);
			BioBean focus;
			
			String actionObjectName = context.getActionObject().getName();
			if(actionObjectName.equals(ALLOCATE)){
				//Multi-Select Allocate Action
				//Executes stored procedure name:NSPORDERPROCESSING params:orderkey, oskey, docarton, doroute, tblprefix, preallocateonly
				for(Iterator iterator = selected.iterator(); iterator.hasNext();){
					focus = (BioBean)iterator.next();
					String orderKey=null, osKey="", doCarton="Y", doRoute="N", tblPrefix="", preallocateOnly="N";
					String[] parameters = new String[1];
					parameters[0] = null;
					orderKey = focus.getValue(ORDER).toString();
					String batchFlag = focus.getValue(BATCH).toString();
					if(batchFlag.equals("1")){
						parameters[0]=orderKey;
					}else{
						WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
						Array params = new Array();
						//Store parameters for stored procedure call
						params.add(new TextData(orderKey));
						params.add(new TextData(osKey));
						params.add(new TextData(doCarton));
						params.add(new TextData(doRoute));
						params.add(new TextData(tblPrefix));
						params.add(new TextData(preallocateOnly));
						//Set actionProperties for stored procedure call
						actionProperties.setProcedureParameters(params);
						actionProperties.setProcedureName(PROC_NAME_ALLOC);
						try{
							//Run stored procedure
							WmsWebuiActionsImpl.doAction(actionProperties);
						}catch(WebuiException e){
							throw new UserException(e.getMessage(), new Object[] {});		
						}
					}
					if(parameters[0]!=null){
						throw new FormException(ERROR_MESSAGE_PRE_BATCHED, parameters);
					}
				}
				context.getState().getDefaultUnitOfWork().clearState(); //Clearing the UOW State seems like it forces a reload and updates the display
			}else if(actionObjectName.equals(RELEASE)){
				//Multi-Select Release action
				String waveKey, success="0", err="0", doRelease="Y";
				
				for(Iterator iterator = selected.iterator(); iterator.hasNext();){
					waveKey=null;
					focus = (BioBean)iterator.next();
					String tempStr = focus.getValue(ORDER).toString();
					//Search wavedetail for existing records matching orderkey
					UnitOfWorkBean uow = state.getDefaultUnitOfWork();
					String queryString = WD_TABLE+"."+ORDER+"='"+tempStr+"'";
					Query waveQry = new Query(WD_TABLE, queryString, null);
					BioCollectionBean waveList = uow.getBioCollectionBean(waveQry);
					int size = waveList.size();
					
					if(size>0){
						int index=0;
						boolean notFound=true;
						BioCollectionBean temp;
						//Verify if any existing record only hold the matching orderkey
						while(index<size && notFound){
							queryString = WD_TABLE+"."+WAVE+"='"+waveList.get(""+index).get(WAVE)+"'";
							waveQry = new Query(WD_TABLE, queryString, null);
							temp = uow.getBioCollectionBean(waveQry);
							if(temp.size()==1){
								//If current orderkey is individual for wavekey record search task detail with orderkey and wavekey
								queryString = TASK_TABLE+"."+ORDER+"='"+waveList.get(""+index).get(ORDER)+"' and wm_taskdetail."+WAVE+"='"+waveList.get(""+index).get(WAVE)+"'";
								waveQry = new Query(TASK_TABLE, queryString, null);
								temp = uow.getBioCollectionBean(waveQry);
								//If no matching records found in task detail, use wavekey
								if(temp.size()==0){
									notFound=false;						
									waveKey=waveList.get(""+index).get(WAVE).toString();
								}
							}
							index++;
						}	
					}

					//If found use wavekey else create new and use
					if(waveKey==null){
						BioBean bio = (BioBean)focus;
						UnitOfWork unitOfWork = bio.getUnitOfWork();
						Bio waveBio = unitOfWork.createBio(WAVE_TABLE);

						//Generate new wavekey
						KeyGenBioWrapper wrapper = new KeyGenBioWrapper();
						String newWaveKey = wrapper.getKey(WAVE);
			   			
			   			//Set required values
						waveBio.set(WAVE, newWaveKey);
						unitOfWork.save();
						
						//create wave detail
						UnitOfWork unitOfWork2 = bio.getUnitOfWork();
						Bio waveDetailBio = unitOfWork2.createBio(WD_TABLE);
						waveDetailBio.set(ORDER, focus.getValue(ORDER));
						waveDetailBio.set(WD, "0000000001");
						waveDetailBio.set(WAVE, newWaveKey);
						unitOfWork2.save();
						
						//Pass new wave key for stored procedure
						waveKey=newWaveKey;
					}
					
					//Execute Stored Procedure
					WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
					Array params = new Array();
					//Store parameters for stored procedure call
					params.add(new TextData(waveKey));
					params.add(new TextData(success));
					params.add(new TextData(err));
					params.add(new TextData(doRelease));
					//Set actionProperties for stored procedure call
					actionProperties.setProcedureParameters(params);
					actionProperties.setProcedureName(PROC_NAME_RELEASE);
					try{
						//Run stored procedure
						WmsWebuiActionsImpl.doAction(actionProperties);
					}catch (WebuiException e) {
						throw new UserException(e.getMessage(), new Object[] {});
					}
				}
			}else if(actionObjectName.equals(SENDTOTM)){
				
				UnitOfWorkBean uow = state.getDefaultUnitOfWork();
				for(Iterator iterator = selected.iterator(); iterator.hasNext();){
					focus = (BioBean)iterator.next();
					focus.setValue("CARRIERROUTESTATUS", "NEW");
				}
				uow.saveUOW(false);
				uow.clearState();		
			}else{
				//Action object not recognized (This should never execute)
				_log.error("LOG_ERROR_EXTENSION_SHIPMENTORDERMULTISELECTACTION", "("+actionObjectName+") not recognized", 100L);
			}
		}else{
			//Form view, proceed to modal dialog box for confirmation
			context.setNavigation(NAV_SINGLE);
			return RET_CONTINUE;
		}	

		return RET_CANCEL_EXTENSIONS;
	}
	
	private boolean isNull(Object attributeValue){
		if (attributeValue == null){
			return true;
		}else{
			return false;
		}
	}
}