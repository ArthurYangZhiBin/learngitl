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
package com.infor.scm.waveplanning.wp_labor_data.ui;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;


public class LaborDataPreSave extends SaveAction{
	protected int execute(ActionContext context, ActionResult result) throws UserException{
		String[] parameters = new String[2];
		parameters[0] = "";
		parameters[1] = "";
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		RuntimeFormInterface shellForm = state.getCurrentRuntimeForm().getParentForm(state);
		RuntimeListFormInterface listForm = (RuntimeListFormInterface)state.getRuntimeForm(shellForm.getSubSlot("slot1"), null);
		DataBean focus = listForm.getFocus();
		BioCollectionBean bc = (BioCollectionBean)focus;
		try{
			for(int index=0; index<bc.size(); index++){
				BioBean bio = bc.get(""+index);
				if(bio.hasBeenUpdated("CASEPICKRATE") || bio.hasBeenUpdated("PALLETPICKRATE") || bio.hasBeenUpdated("PIECEPICKRATE")){
					String whse = bio.get("WHSEID").toString();
					String temp1 = bio.get("PIECEPICKRATE").toString();
					String temp2 = bio.get("CASEPICKRATE").toString();
					String temp3 = bio.get("PALLETPICKRATE").toString();
					try{
						int tempOne = Integer.parseInt(temp1);
						int tempTwo = Integer.parseInt(temp2);
						int tempThree = Integer.parseInt(temp3);
						if(tempOne<0 || tempTwo<0 || tempThree<0){
							//throw exception
							parameters[0] = append(parameters[0], whse);
						}
					}catch(NumberFormatException noExp){
						//Exception parsing integer inputs
						parameters[1] = append(parameters[1], whse);
					}
				}
			}
		}catch(EpiDataException exp){
			//Exception from size() or get() methods, should not be encountered
		}
		QBEBioBean qbe = listForm.getQuickAddRowBean();
		try{
			String whse = qbe.get("WHSEID").toString();
			//qbe.set("WHSEID", whse.toUpperCase());
			String temp1 = qbe.get("PIECEPICKRATE").toString();
			String temp2 = qbe.get("CASEPICKRATE").toString();
			String temp3 = qbe.get("PALLETPICKRATE").toString();
			try{
				int tempOne = Integer.parseInt(temp1);
				int tempTwo = Integer.parseInt(temp2);
				int tempThree = Integer.parseInt(temp3);
				if(tempOne<0 || tempTwo<0 || tempThree<0){
					//throw exception
					parameters[0] = append(parameters[0], whse);
				}
			}catch(NumberFormatException noExp){
				//Exception parsing integer inputs
				parameters[1] = append(parameters[1], whse);
			}
		}catch(NullPointerException exp){
			//No new record, ignore exception
		}
		
		//Block save for any exceptions found
		if(!parameters[0].equals("") || !parameters[1].equals("")){
			throw new UserException("WMEXP_WP_LABORDATASAVE", parameters);
		}
		try{
			uowb.saveUOW(true);			
		}catch(EpiException exp){
			//Encountered error during save
		}
		Query qry = new Query("wp_labordata", null, null);
		result.setFocus(uowb.getBioCollectionBean(qry));
		return RET_CONTINUE;
	}
	
	private String append(String previous, String appender){
		if(!previous.equals("")){
			previous = previous + ", ";
		}		
		previous = previous+appender;
		return previous;
	}
}