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
package com.ssaglobal.scm.wms.wm_batch_picking.ui;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;

public class BatchPickingReleaseWave extends ActionExtensionBase{
	private final static String PROC_NAME = "NSPRELEASEWAVE";
	private final static String WAVE = "WAVEKEY";
	private final static String SHELL_SLOT = "list_slot_1";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(BatchPickingReleaseWave.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{
		//Find wavekey and doRelease variables
		StateInterface state = context.getState();
		String doRelease;
		boolean isAllocate = getParameterBoolean("isAllocate");
		if(isAllocate){
			doRelease = "N";
		}else{
			doRelease = "Y";
		}
		RuntimeFormInterface shell = state.getCurrentRuntimeForm().getParentForm(state).getParentForm(state);
		RuntimeFormInterface header = state.getRuntimeForm(shell.getSubSlot(SHELL_SLOT), null);
		String waveKey = header.getFormWidgetByName(WAVE).getDisplayValue();
		
		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		Array parms = new Array(); 
		parms.add(new TextData(waveKey));	
		parms.add(new TextData(""));
		parms.add(new TextData(""));
		parms.add(new TextData(doRelease));
		_log.debug("LOG_SYSTEM_OUT","\n\n--------NSPRELEASEWAVE Params------------",100L);
		_log.debug("LOG_SYSTEM_OUT","Param 1:"+waveKey,100L);
		_log.debug("LOG_SYSTEM_OUT","Param 2:",100L);
		_log.debug("LOG_SYSTEM_OUT","Param 3:",100L);
		_log.debug("LOG_SYSTEM_OUT","Param 4:"+doRelease,100L);
		_log.debug("LOG_SYSTEM_OUT","--------End NSPRELEASEWAVE Params------------\n\n",100L);
		actionProperties.setProcedureParameters(parms);
		actionProperties.setProcedureName(PROC_NAME);
		try{
			WmsWebuiActionsImpl.doAction(actionProperties);	
		}catch(Exception e){
			throw new UserException(e.getMessage(), new Object[] {});
		}
		result.setFocus(header.getFocus());
		return RET_CONTINUE;
	}
}