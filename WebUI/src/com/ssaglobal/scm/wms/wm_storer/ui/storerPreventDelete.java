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
package com.ssaglobal.scm.wms.wm_storer.ui;

import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.wm_shipmentorder.ui.ShipmentOrderFillPrevUOM;

public class storerPreventDelete extends com.epiphany.shr.ui.action.ActionExtensionBase
{

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
	protected static ILoggerCategory _log = LoggerFactory.getInstance(storerPreventDelete.class);

	protected int execute(ActionContext context, ActionResult result)
			throws EpiException
	{
		String shellFormName = getParameter("SHELLFORMNAME").toString();
		String listFormName = getParameter("LISTFORMNAME").toString();

		StateInterface state = context.getState();
		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();

		RuntimeFormInterface ownerForm = FormUtil.findForm(shellToolbar, shellFormName, listFormName, state);

		RuntimeListFormInterface ownerList = null;
		if (ownerForm.isListForm())
		{
			_log.debug("LOG_SYSTEM_OUT","@#$ A List",100L);
			ownerList = (RuntimeListFormInterface) ownerForm;
		}
		else
		{
			_log.debug("LOG_SYSTEM_OUT","@#$ Not A List",100L);
			return RET_CANCEL;
		}

		DataBean listFormFocus = ownerList.getFocus();
		BioCollectionBean ownerCollection = null;
		if (listFormFocus.isBioCollection())
		{
			ownerCollection = (BioCollectionBean) listFormFocus;
			_log.debug("LOG_SYSTEM_OUT","!@# It is a BioCollectionBean, size: " + ownerCollection.size(),100L);
		}

		ArrayList owners = ownerList.getAllSelectedItems();
		//iterate items
		for (Iterator owner = owners.iterator(); owner.hasNext();)
		{
			BioBean temp = (BioBean) owner.next();
			_log.debug("LOG_SYSTEM_OUT","! Items Arraylist" + temp.getValue("STORERKEY"),100L);
		}

		BioBean selectedOwner = (BioBean) owners.get(0);
		String owner = selectedOwner.getValue("STORERKEY").toString();

		//query STORER_UDF and STORERLABELS
		String queryLabels = "SELECT * FROM STORERLABELS WHERE STORERKEY = '" + owner + "'";
		_log.debug("LOG_SYSTEM_OUT",queryLabels,100L);
		EXEDataObject resultsLabels = WmsWebuiValidationSelectImpl.select(queryLabels);
		if (resultsLabels.getRowCount() > 0)
		{
			throw new UserException("WMEXP_OWNER_DELETE_LABELS", new Object[] {});
		}

		String queryUDF = "SELECT * FROM STORER_UDF WHERE STORERKEY = '" + owner + "'";
		_log.debug("LOG_SYSTEM_OUT",queryUDF,100L);
		EXEDataObject resultsUDF = WmsWebuiValidationSelectImpl.select(queryUDF);
		if (resultsUDF.getRowCount() > 0)
		{
			throw new UserException("WMEXP_OWNER_DELETE_UDF", new Object[] {});
		}

		String queryLot = "SELECT * FROM LOTATTRIBUTE WHERE STORERKEY = '" + owner + "'";
		_log.debug("LOG_SYSTEM_OUT",queryLot,100L);
		EXEDataObject resultsLot = WmsWebuiValidationSelectImpl.select(queryLot);
		if (resultsLot.getRowCount() > 0)
		{
			throw new UserException("WMEXP_OWNER_DELETE_LOT", new Object[] {});
		}
		return RET_CONTINUE;
	}

}
