/**
 * ---Begin Copyright Notice---20090105T1353
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2010 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */
package com.infor.scm.waveplanning.wp_wavemgmt.action;

import com.epiphany.shr.data.bio.BioCollectionRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationUpdateImpl;

/**
 * TODO Document WaveShipFromPickDetail class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class WaveShipFromPickDetail extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WaveShipFromPickDetail.class);

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
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		String actionType = getParameterString("ACTIONTYPE"); 
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();

		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeListFormInterface listForm = (RuntimeListFormInterface)toolbar.getParentForm(state);
		RuntimeFormInterface tab = listForm.getParentForm(state);
		EpnyUserContext userContext = context.getServiceManager().getUserContext();
		String waveKey = userContext.get("WAVEMGMT_WAVEKEY").toString();
		if(listForm == null){
			throw new UserException("WPEXP_DELETE_NO_SELECT", new Object[]{});
		}
	
		String listName = listForm.getName().trim();
		java.util.ArrayList selected = listForm.getAllSelectedItems();
		int size = selected.size();
		if(size == 0){
			throw new UserException("WPEXP_DELETE_NO_SELECT", new Object[]{});
		}


		BioBean selectedItem = null;
	
		//iterate items
		String pickDetailKey = "";
		String status = "";
		boolean allocated = false;
		boolean picked = false;
		StringBuffer qry = new StringBuffer();
		qry.append("PICKDETAILKEY IN (");
		for (int i=0;i<size;i++)		{
			selectedItem = (BioBean)selected.get(i);
			pickDetailKey = selectedItem.getString("PICKDETAILKEY");
			status = selectedItem.getString("STATUS");
			if("0".equalsIgnoreCase(status) ||"1".equalsIgnoreCase(status)){//0 allocated, 1 released
				allocated = true;
			}
			if("5".equalsIgnoreCase(status)){
				picked = true;
			}

			if(i<size-1){
				qry.append("'"+pickDetailKey+"',");
			}else{
				qry.append("'"+pickDetailKey+"'");					
			}			
		}
		qry.append(")");
		
		String upateQry = "";
		if("PICKDETAILSHIP".equalsIgnoreCase(actionType)){
			upateQry = "UPDATE PICKDETAIL SET STATUS = '9' WHERE "+qry.toString();
		}else if("PICKDETAILPICK".equalsIgnoreCase(actionType)){
			upateQry = "UPDATE PICKDETAIL SET STATUS = '5' WHERE "+qry.toString();			
		}
		WmsWebuiValidationUpdateImpl.update(upateQry);
    	Query qryReload = new Query("wm_wp_wave","wm_wp_wave.WAVEKEY='"+waveKey+"'", null);			 		
		BioCollectionBean resultCollection = uow.getBioCollectionBean(qryReload);	
		BioCollectionRef bcRef = null;
		
		if(allocated){
			bcRef = resultCollection.get("0").getBioCollection("ALLOCATEDPICKDETAILBIOCOLLECTION").getBioCollectionRef();
		}else if(picked){
			bcRef = resultCollection.get("0").getBioCollection("PICKEDPICKDETAILBIOCOLLECTION").getBioCollectionRef();			
		}		
		result.setFocus(uow.getBioCollection(bcRef));
		uow.clearState();


		return RET_CANCEL;
	}
		
				

}
