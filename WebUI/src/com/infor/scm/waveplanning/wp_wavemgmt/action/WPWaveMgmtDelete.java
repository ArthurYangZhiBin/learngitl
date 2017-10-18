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

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.BioCollectionRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.infor.scm.waveplanning.common.util.WPUserUtil;
import com.infor.scm.waveplanning.wp_wavemgmt_confirmwave.action.ConfirmWaveAddOrderNavSelect;
import com.epiphany.shr.ui.view.*;
import com.infor.scm.waveplanning.wp_wm_wave.wave.WPConstants;
import com.epiphany.shr.ui.model.data.BioBean;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationDeleteImpl;
import com.epiphany.shr.sf.util.EpnyUserContext;

/**
 * TODO Document WPWaveMgmtDelete class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class WPWaveMgmtDelete extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPWaveMgmtDelete.class);

	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		String delete = getParameterString("DELETENAME"); 
		StateInterface state = context.getState();

		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeListFormInterface listForm = (RuntimeListFormInterface)toolbar.getParentForm(state);
		RuntimeFormInterface tab = listForm.getParentForm(state);
	
		String listName = listForm.getName().trim();
		boolean allocated = false;
		boolean picked = false;
		java.util.ArrayList selected = listForm.getAllSelectedItems();
		int size = selected.size();
		if(size == 0){
			throw new UserException("WPEXP_DELETE_NO_SELECT", new Object[]{});
		}
		EpnyUserContext userContext = context.getServiceManager().getUserContext();
		String waveKey = userContext.get("WAVEMGMT_WAVEKEY").toString();
		String keyName = "";
		String tableName = "";
		String orderKey ="";
		String orderLine = "";
		String status = "";
		StringBuffer qry = new StringBuffer();
		if("wp_wavemgmt_wavemaint_pickdetail_list_view".equalsIgnoreCase(listName)){
			keyName = "PICKDETAILKEY";
			tableName ="PICKDETAIL";			
			BioBean selectedBB = null;
			String pickDetailKey = "";
			qry.append(keyName+" IN (");
			String statusQry = "";
			for(int i=0;i<size;i++){
				selectedBB = (BioBean)selected.get(i);
				
				status = selectedBB.getString("STATUS");
				if("0".equalsIgnoreCase(status) || "1".equalsIgnoreCase(status)){//0 allocated, 1 released
					allocated = true;
				}
				if("5".equalsIgnoreCase(status)){
					picked = true;
				}
				pickDetailKey = selectedBB.getString("PICKDETAILKEY");
				if(i<size-1){
					qry.append("'"+pickDetailKey+"',");
				}else{
					qry.append("'"+pickDetailKey+"'");					
				}
			}
			qry.append(")");

		}
		if(WPConstants.DELETE_TASKDETAIL.equalsIgnoreCase(delete)
				|| WPConstants.DELETE_BATCHMOVE.equalsIgnoreCase(delete)){
			keyName = "TASKDETAILKEY";
			tableName ="TASKDETAIL";
			BioBean selectedBB = null;
			String taskDetailKey = "";
			qry.append(keyName+" IN (");
			for(int i=0;i<size;i++){
				selectedBB = (BioBean)selected.get(i);
				taskDetailKey = selectedBB.getString("TASKDETAILKEY");
				if(i<size-1){
					qry.append("'"+taskDetailKey+"',");
				}else{
					qry.append("'"+taskDetailKey+"'");					
				}
			}
			qry.append(")");			
		}
		
		
		
		
		
		
		String deleteQry = "DELETE FROM "+tableName +" WHERE "+qry.toString();
		WmsWebuiValidationDeleteImpl.delete(deleteQry);
    	Query qryReload = new Query("wm_wp_wave","wm_wp_wave.WAVEKEY='"+waveKey+"'", null);			 		
		BioCollectionBean resultCollection = uow.getBioCollectionBean(qryReload);	
		BioCollectionRef bcRef = null;
		if(allocated){
			bcRef = resultCollection.get("0").getBioCollection("ALLOCATEDPICKDETAILBIOCOLLECTION").getBioCollectionRef();
		}else if(picked){
			bcRef = resultCollection.get("0").getBioCollection("PICKEDPICKDETAILBIOCOLLECTION").getBioCollectionRef();			
		}else if(WPConstants.DELETE_TASKDETAIL.equalsIgnoreCase(delete)){
			bcRef = resultCollection.get("0").getBioCollection("TASKCOLLECTION").getBioCollectionRef();
		}else if(WPConstants.DELETE_BATCHMOVE.equalsIgnoreCase(delete)){
			bcRef = resultCollection.get("0").getBioCollection("TASKDETAIL").getBioCollectionRef();
		}
		
		result.setFocus(uow.getBioCollection(bcRef));
		uow.clearState();

		return RET_CONTINUE;

	}

}
