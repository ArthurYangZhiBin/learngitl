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

package com.ssaglobal.scm.wms.wm_load_maintenance;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
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
import com.ssaglobal.scm.wms.formextensions.ListFormDeleteAction;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class LPNListFormDeleteAction extends com.epiphany.shr.ui.action.ActionExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ListFormDeleteAction.class);

	@Override
	protected int execute(ActionContext context, ActionResult result) throws UserException {

		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION", "Executing ListFormDeleteAction", 100L);
		StateInterface state = context.getState();
		String targetFormName = (String) getParameter("targetFormName");
		ArrayList tabs = (ArrayList) getParameter("tabs");

		//Loading Enhancements
		ArrayList lpns = new ArrayList();
		double deletedLPNCube = 0;
		double deletedLPNWeight = 0;
		double deletedLPNUnits = 0;
		double deletedLPNs = 0;

		//Get Target Form		
		RuntimeFormInterface form = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", targetFormName, tabs, state);
		if (form != null)
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION", "Found Target Form:" + form.getName(), 100L);
		else
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION", "Found Target Form:Null", 100L);

		//If form is avaliable and items are selected then delete		
		if (form != null &&
			form.isListForm() &&
			((RuntimeListFormInterface) form).getSelectedItems() != null &&
			((RuntimeListFormInterface) form).getSelectedItems().size() > 0) {
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION", "deleteing items...", 100L);
			Iterator selectedItemsItr = ((RuntimeListFormInterface) form).getSelectedItems().iterator();
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			BioBean loadUnitDetail = null;
			try {
				while (selectedItemsItr.hasNext()) {
					loadUnitDetail = (BioBean) selectedItemsItr.next();
					deletedLPNCube += BioAttributeUtil.getDouble(loadUnitDetail, "OUTUNITCUBE");
					deletedLPNWeight += BioAttributeUtil.getDouble(loadUnitDetail, "OUTUNITWEIGHT");
					deletedLPNUnits += BioAttributeUtil.getDouble(loadUnitDetail, "OUTUNITS");
					deletedLPNs++;
					lpns.add(BioAttributeUtil.getString(loadUnitDetail, "UNITID"));

					loadUnitDetail.delete();
				}

				//update loadhdr
				RuntimeFormInterface loadHdrForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_load_maintenance_detail_form", state);
				DataBean loadHdrFocus = loadHdrForm.getFocus();
				double totalCube = BioAttributeUtil.getDouble(loadHdrFocus, "TOTALCUBE");
				double totalUnits = BioAttributeUtil.getDouble(loadHdrFocus, "TOTALUNITS");
				double totalWeight = BioAttributeUtil.getDouble(loadHdrFocus, "TOTALWEIGHT");
				double loadedIds = BioAttributeUtil.getDouble(loadHdrFocus, "LOADEDIDS");
				loadHdrFocus.setValue("TOTALCUBE", totalCube - deletedLPNCube);
				loadHdrFocus.setValue("TOTALUNITS", totalUnits - deletedLPNUnits);
				loadHdrFocus.setValue("TOTALWEIGHT", totalWeight - deletedLPNWeight);
				loadHdrFocus.setValue("LOADEDIDS", loadedIds - deletedLPNs);
				loadHdrFocus.save();
				String loadid = BioAttributeUtil.getString(loadHdrFocus, "LOADID");

				//delete dropiddetail where dropid = loadid and childid in (lpns)
				String queryLpns = "";
				for (int i = 0; i < lpns.size(); i++) {
					if (i >= 1) {
						queryLpns += ", ";
					}
					queryLpns += " '" + lpns.get(i) + "' ";

				}

				String dropIdDetailQuery = "wm_dropiddetail.DROPID = '" + loadid + "' and wm_dropiddetail.CHILDID IN (" + queryLpns + ")";
				BioCollectionBean rs = uow.getBioCollectionBean(new Query("wm_dropiddetail", dropIdDetailQuery, null));
				for (int i = 0; i < rs.size(); i++) {
					BioBean dropIdDetail = rs.get("" + i);
					dropIdDetail.delete();
				}

				uow.saveUOW();
				((RuntimeListFormInterface) form).setSelectedItems(null);
			} catch (EpiDataException e) {
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION", "Leaving ListFormDeleteAction", 100L);
				e.printStackTrace();
				String args[] = new String[0];
				String errorMsg = getTextMessage("ERROR_DELETING_BIO", args, state.getLocale());
				throw new UserException(errorMsg, new Object[0]);
			} catch (EpiException e) {
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION", "Leaving ListFormDeleteAction", 100L);
				e.printStackTrace();
				String args[] = new String[0];
				String errorMsg = getTextMessage("ERROR_DELETING_BIO", args, state.getLocale());
				throw new UserException(errorMsg, new Object[0]);
			}
		} else {
			if (form == null) {
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION", "Could Not Find Form " + targetFormName + "...", 100L);
			} else {
				_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION", "Nothing selected...", 100L);
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION", "Leaving ListFormDeleteAction", 100L);
		return RET_CONTINUE;

	}
}
