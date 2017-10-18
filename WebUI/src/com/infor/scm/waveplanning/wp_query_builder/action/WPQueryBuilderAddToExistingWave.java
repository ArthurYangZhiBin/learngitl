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
package com.infor.scm.waveplanning.wp_query_builder.action;
import java.util.ArrayList;
import java.util.Hashtable;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.common.util.WPFormUtil;
import com.infor.scm.waveplanning.common.WavePlanningConstants;
import com.infor.scm.waveplanning.common.WavePlanningUtils;


public class WPQueryBuilderAddToExistingWave extends ActionExtensionBase{
	public static final String SESSION_KEY_HASHTABLE = "qry.builder.session.key.hashtable";
	public static final String HASH_KEY_ORDERS = "qry.builder.hash.key.orders";
	public static final String HASH_KEY_SEGMENTS = "qry.builder.hash.key.segments";
	public static final String HASH_KEY_WAREHOUSES = "qry.builder.hash.key.warehses";
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPQueryBuilderAddToExistingWave.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Executing WPQueryBuilderAddToExistingWave",100L);		
		StateInterface state = context.getState();
		
		//Remove Old Order List From Session If Present
		state.getRequest().getSession().removeAttribute(SESSION_KEY_HASHTABLE);
		
		//Get Order List Form
		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Getting Form wm_wp_query_builder_shipment_orders_screen...",100L);
		RuntimeListFormInterface form = (RuntimeListFormInterface)WPFormUtil.findForm(state.getCurrentRuntimeForm(),"","wm_wp_query_builder_shipment_orders_screen_1",state);
		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Got form:"+form,100L);

		if(form == null){
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		
		//Get Focus Of Form (collection of orders)
		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Getting Form Focus...",100L);
		DataBean focus = form.getFocus(); 
		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Got Focus:"+focus,100L);
		
		//Ensure focus is a non-empty collection
		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Validating Focus...",100L);
		try {
			if(focus == null || !focus.isBioCollection() || ((BioCollection)focus).size() == 0){
				_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Focus Not valid...",100L);
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WPEXP_NO_ORDERS_TO_ADD_TO_WAVE",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
		} catch (EpiDataException e) {			
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		BioCollection orders = (BioCollection)focus;
		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Focus Is Valid...",100L);
		
		Hashtable table = new Hashtable();
		
		//Create an Array List of Order Keys using the orders in the form's focus
			ArrayList orderKeys = new ArrayList();
			_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Populating Order Key List...",100L);
			try {
				for(int i = 0; i < orders.size(); i++){
					orderKeys.add(orders.elementAt(i).get("ORDERKEY"));
				}
			} catch (EpiDataException e) {
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
			table.put(HASH_KEY_ORDERS, orderKeys);
			_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Order Key List:"+orderKeys,100L);
		
				
		//Place Order Key collection in session for use later.
		state.getRequest().getSession().setAttribute(SESSION_KEY_HASHTABLE, table);
		return RET_CONTINUE;	
	}
}
