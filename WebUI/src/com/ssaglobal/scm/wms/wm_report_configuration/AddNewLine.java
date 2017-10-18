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
package com.ssaglobal.scm.wms.wm_report_configuration;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;

public class AddNewLine  extends SaveAction{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(AddNewLine.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{	
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		RuntimeFormInterface printerListForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wm_report_configuration_shell","wm_report_printer_list_view",state);
		BioCollectionBean printerBioCollection = (BioCollectionBean)printerListForm.getFocus();		
		RuntimeFormInterface printerForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wm_report_configuration_shell","wm_report_printer_detail_view",state);		
		try {
			if(printerForm != null){
				DataBean dataBean = printerForm.getFocus();
				String defaultFlag = dataBean.getValue("DEFAULTPRINTERFLAG")==null?"0":dataBean.getValue("DEFAULTPRINTERFLAG").toString();
				String printerName = dataBean.getValue("PRINTERNAME").toString();
				if(dataBean.isTempBio()){
					if("1".equalsIgnoreCase(defaultFlag)){
						clearFlag(printerBioCollection, printerName);
					}else{
						updateDefaultFlag(printerBioCollection);
					}
				}else{
					updateDefaultFlag(printerBioCollection);
				}
			}else{
				updateDefaultFlag(printerBioCollection);					
			}		
			uow.saveUOW(true);
			uow.clearState();
			Query qry = new Query("wm_reportprinter",null,null);
			BioCollectionBean bcb = uow.getBioCollectionBean(qry);
			result.setFocus(bcb);
		} catch (EpiException e) {
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}

		return RET_CONTINUE;	
	}
	
	private void updateDefaultFlag(BioCollectionBean bcb) throws EpiDataException{
		for (int i=0; i<bcb.size(); i++){
			BioBean printerLine = (BioBean)bcb.elementAt(i);
			if (printerLine.hasBeenUpdated("DEFAULTPRINTERFLAG")){
				if (printerLine.getString("DEFAULTPRINTERFLAG").equalsIgnoreCase("1")){
					String printerName = printerLine.getValue("PRINTERNAME").toString();
					clearFlag(bcb, printerName);
					break;
				}
										
			}

		}
		
	}
	
	private void clearFlag(BioCollectionBean bcb, String defaultPrinterName) throws EpiDataException{
		for (int i=0; i<bcb.size(); i++){
			BioBean printerLine = (BioBean)bcb.elementAt(i);
			String printerName = printerLine.getValue("PRINTERNAME").toString();
			if(!defaultPrinterName.equalsIgnoreCase(printerName)){
				printerLine.set("DEFAULTPRINTERFLAG","0");
			}
		}
		
	}

}
