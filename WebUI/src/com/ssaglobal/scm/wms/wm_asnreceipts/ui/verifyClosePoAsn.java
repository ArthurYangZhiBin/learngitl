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
package com.ssaglobal.scm.wms.wm_asnreceipts.ui;


import java.util.ArrayList;
import java.util.Iterator;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.action.ModalActionContext;
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
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;

public class verifyClosePoAsn extends ListSelector {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(verifyClosePoAsn.class);
	protected int execute(ModalActionContext context, ActionResult args) throws EpiException {
		_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Action1 \n\n",100L);
		StateInterface state = context.getState();

		RuntimeFormInterface toolbar = context.getSourceForm();    
		RuntimeFormInterface headerForm = toolbar.getParentForm(state);
		_log.debug("LOG_SYSTEM_OUT","headerForm"+ headerForm.getName(),100L);
		DataBean headerFocus = headerForm.getFocus();
		RuntimeListFormInterface headerListForm = (RuntimeListFormInterface)headerForm;
		ArrayList selectedItems = headerListForm.getSelectedItems();
		if(selectedItems != null && selectedItems.size() > 0){
			Iterator bioBeanIter = selectedItems.iterator();
			BioBean bio;
			for(; bioBeanIter.hasNext();){
				bio = (BioBean)bioBeanIter.next();
				String sourceKey = bio.getString("Sourcekey");
				String sourceType = bio.getString("SourceType");
				_log.debug("LOG_SYSTEM_OUT","\n\nSOURCEKEY:"+sourceKey+"\n\n",100L);
				WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				Array parms = new Array(); 
				parms.add(new TextData(sourceKey));
				actionProperties.setProcedureParameters(parms);
				if (sourceType.equalsIgnoreCase("ASN")){
					actionProperties.setProcedureName("NSPRECEIPTVERIFIEDCLOSE");
				}else{
					actionProperties.setProcedureName("NSPPOVERIFIEDCLOSE");
				}
				try {
					WmsWebuiActionsImpl.doAction(actionProperties);

				} catch (Exception e) {
					e.getMessage();
					UserException UsrExcp = new UserException(e.getMessage(), new Object[]{});
		 	   		throw UsrExcp;
				}

			}
		}
		_log.debug("LOG_SYSTEM_OUT","\n\nExecuting Action10 \n\n",100L);
		return RET_CONTINUE;
	}	

}
