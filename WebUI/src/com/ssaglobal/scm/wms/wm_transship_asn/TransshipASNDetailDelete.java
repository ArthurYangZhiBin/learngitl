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
package com.ssaglobal.scm.wms.wm_transship_asn;
import java.util.ArrayList;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;


public class TransshipASNDetailDelete extends ActionExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(TransshipASNDetailDelete.class);
	
	protected int execute(ActionContext context, ActionResult result) throws UserException{		
		
		_log.debug("LOG_DEBUG_EXTENSION_TRANSASNDETAILDEL","Executing TransshipASNDetailDelete",100L);		
		StateInterface state = context.getState();	
		
		//Get Detail Form
		RuntimeFormInterface detailForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_transship_asn_detail_list_view",state);
		if(detailForm != null)
			_log.debug("LOG_DEBUG_EXTENSION_TRANSASNDETAILDEL","Found Detail Form:"+detailForm.getName(),100L);			
		else
			_log.debug("LOG_DEBUG_EXTENSION_TRANSASNDETAILDEL","Found Detail Form:Null",100L);			
		
		//validate header form fields		
		if(detailForm != null){
			ArrayList selectedItems = ((RuntimeListFormInterface)detailForm).getSelectedItems();
			if(selectedItems == null || selectedItems.size() == 0){
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_NOTHING_SELECTED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
			BioBean bio = (BioBean)selectedItems.get(0);
			try {
				String qry = "wm_transasnd.TRANSASNKEY = '"+bio.getString("TRANSASNKEY").toUpperCase()+"' AND (wm_transasnd.LINENUMBER = '"+bio.getString("LINENUMBER").toUpperCase()+"' ";
				for(int i = 1; i < selectedItems.size(); i++){
					bio = (BioBean)selectedItems.get(i);
					qry += " OR wm_transasnd.LINENUMBER = '"+bio.getString("LINENUMBER").toUpperCase()+"' ";
				}
				qry += ")";
				_log.debug("LOG_DEBUG_EXTENSION_TRANSASNDETAILDEL","Query:"+qry,100L);				
				Query loadBiosQry = new Query("wm_transasnd", qry, "");
				UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
				BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);
				if(bioCollection != null && bioCollection.size() > 0){
					for(int i = 0; i < bioCollection.size(); i++){
						bioCollection.elementAt(i).delete();
					}
				}				
				uow.saveUOW();
				((RuntimeListFormInterface)detailForm).setSelectedItems(null);
			} catch (EpiDataException e) {
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_DELETE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			} catch (EpiException e) {
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_DELETE_FAILED",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
		}	
		else{
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_NOTHING_SELECTED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		_log.debug("LOG_DEBUG_EXTENSION_TRANSASNDETAILDEL","Leaving TransshipASNDetailDelete",100L);		
		return RET_CONTINUE;
		
	}	
}