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
package com.infor.scm.waveplanning.wp_graphical_filters.action;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.EpiDataRefOnUnsavedBioException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.common.util.WPFormUtil;
import com.infor.scm.waveplanning.common.util.WPUserUtil;


public class WPGraphicalGetSavedFilterFromSessionPK extends SaveAction{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPGraphicalGetSavedFilterFromSessionPK.class);
	public static final String SESSION_KEY_SAVED_FILTER = "wp.sess.key.saved.filter";
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_WPGRPHFLTRPROCEED","Executing WPGraphicalFilterProceed",100L);		
		StateInterface state = context.getState();
		Integer filterPK = (Integer)WPUserUtil.getInteractionSessionAttribute(SESSION_KEY_SAVED_FILTER, state);
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		if(filterPK == null){
			try {
				result.setFocus(uow.getQBEBioWithDefaults("wp_filter"));
			} catch (DataBeanException e) {
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
			return RET_CONTINUE;
		}
		
		Query bioQry = new Query("wp_filter","wp_filter.FILTERID = "+filterPK.toString(),"");
		BioCollectionBean filters = uow.getBioCollectionBean(bioQry);
		try {
			result.setFocus(uow.getBioBean(filters.elementAt(0).getBioRef()));
		} catch (BioNotFoundException e) {
			try {
				result.setFocus(uow.getQBEBioWithDefaults("wp_filter"));
			} catch (DataBeanException e1) {
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
		} catch (EpiDataRefOnUnsavedBioException e) {
			try {
				result.setFocus(uow.getQBEBioWithDefaults("wp_filter"));
			} catch (DataBeanException e1) {
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
		} catch (EpiDataException e) {
			try {
				result.setFocus(uow.getQBEBioWithDefaults("wp_filter"));
			} catch (DataBeanException e1) {
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
		}
		
		return RET_CONTINUE;
	}
}
