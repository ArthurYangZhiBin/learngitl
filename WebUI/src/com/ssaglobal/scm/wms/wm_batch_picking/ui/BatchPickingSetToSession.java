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

import javax.servlet.http.HttpSession;

import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.uiextensions.KeyGenBioWrapper;

public class BatchPickingSetToSession extends ActionExtensionBase{	
	private final static String BIOREF = "BIOREF";
	private final static String HEADER = "HEADER";
	private final static String WAVEKEY = "WAVEKEY";
	private final static String IGNORE_CONFIRMED = "IGNORE_CONFIRMED";
	
	private final static String OS_TABLE = "wm_orderselection";
	private final static String WAVE_TABLE = "wm_wave";
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		//Initialize local variables
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		HttpSession session = state.getRequest().getSession();
		
		//Retrieve passed parameters
		BioBean bio =(BioBean)state.getFocus();
		BioRef bioRef =	bio.getBioRef();
		
		//Set into session
		session.setAttribute(BIOREF, bioRef);
		
		if(bio.getDataType().equals(OS_TABLE)){
			//Build and save new batch picking header
			KeyGenBioWrapper wrapper = new KeyGenBioWrapper();
			String newKey = wrapper.getKey(WAVEKEY);
			QBEBioBean newWave = uowb.getQBEBioWithDefaults(WAVE_TABLE);
			newWave.set(WAVEKEY, newKey);
			uowb.saveUOW(true);
			
			session.setAttribute(HEADER, newKey);
		}else{
			session.setAttribute(IGNORE_CONFIRMED, Boolean.FALSE);
		}
		return RET_CONTINUE;
	}
	
}