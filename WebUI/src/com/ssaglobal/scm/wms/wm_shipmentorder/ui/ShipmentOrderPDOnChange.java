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

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
public class ShipmentOrderPDOnChange extends ActionExtensionBase {
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ShipmentOrderPDOnChange.class);
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		BioBean bio = (BioBean)context.getState().getFocus();
		int status = Integer.parseInt(bio.get("STATUS").toString());
		_log.debug("LOG_DEBUG_EXTENSION", "STATUS " + status, SuggestedCategory.NONE);;

		if (5 <= status && status < 9){
			String query = "SELECT CONFIGKEY, NSQLVALUE FROM NSQLCONFIG WHERE CONFIGKEY = 'DOMOREWHENPICKED'";
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if ((results.getRowCount() != 1)){
				//do nothing
				return RET_CONTINUE;
			}

			int nsqlValue = Integer.parseInt(results.getAttribValue(2).getAsString());

			if (nsqlValue == 0){
				Object tempToLocValue = bio.get("TOLOC");
				Object tempLocValue = bio.get("LOC");

				if (isEmpty(tempToLocValue)){
					_log.debug("LOG_DEBUG_EXTENSION", "!@# TOLOC is empty", SuggestedCategory.NONE);;
					if (isEmpty(tempLocValue)){
						_log.debug("LOG_DEBUG_EXTENSION", "!## LOC is empty", SuggestedCategory.NONE);;
					}
					else{
						String locValue = tempLocValue.toString();
						//check type of location
						query = "SELECT LOC.LOC, PUTAWAYZONE.PICKTOLOC "
								+ "FROM LOC INNER JOIN PUTAWAYZONE ON LOC.PUTAWAYZONE = PUTAWAYZONE.PUTAWAYZONE "
								+ "WHERE LOC.LOC = '" + locValue + "'";
						results = WmsWebuiValidationSelectImpl.select(query);
						_log.debug("LOG_DEBUG_EXTENSION", "Row Count" + results.getRowCount(), SuggestedCategory.NONE);;
						if (results.getRowCount() == 1){
							String locationType = results.getAttribValue(2).getAsString();
							_log.debug("LOG_DEBUG_EXTENSION", " LocType " + locationType, SuggestedCategory.NONE);;
							if (!(locationType.equalsIgnoreCase("PICKTO"))){
								_log.debug("LOG_DEBUG_EXTENSION", "!!!!Raising Error, Loc must be of type PICKTO", SuggestedCategory.NONE);;
								throw new EpiException("WMEXP_PICK_LOC", "WMEXP_PICK_LOC", new Object[] {});
							}
						}
						else{
							_log.debug("LOG_DEBUG_EXTENSION", "!! Location Not Found", SuggestedCategory.NONE);;
						}
					}
				}
				else{
					String toLocValue = tempToLocValue.toString();
					_log.debug("LOG_DEBUG_EXTENSION", "Setting TOLOC to LOC", SuggestedCategory.NONE);;
					bio.set("LOC", toLocValue);
					bio.set("TOLOC", "");
				}
			}
		}

		//		Retrieve Status Value
		//		if 9>status>=5
		//			check for DoMoreWhenPicked in NSQLConfig
		//				if NSQLValue  = 0
		//					if toloc not blank
		//						then assign toloc to location
		//						toloc become "_"
		//					if toloc is blank
		//						if loc is not PICKTO type
		//							raise error

		return RET_CONTINUE;
	}

	private boolean isEmpty(Object attributeValue) throws EpiDataException{
		if (attributeValue == null){
			return true;
		}else if (attributeValue.toString().matches("\\s*")){
			return true;
		}else{
			return false;
		}
	}
}