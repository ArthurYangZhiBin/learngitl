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
package com.ssaglobal.scm.wms.wm_facilitytransfer.ui;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class OwnerValidation extends com.epiphany.shr.ui.action.ActionExtensionBase {


	/**
	 * The code within the execute method will be run on the WidgetRender.
	 * <P>
         * @param state The StateInterface for this extension
         * @param widget The RuntimeFormWidgetInterface for this extension's widget
         * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 */
	protected static ILoggerCategory _log = LoggerFactory.getInstance(OwnerValidation.class);

	protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		   final String BIO = "wm_storer";
		   final String ATTRIBUTE_LABEL = "Owner";
		   final String TYPE = "1";
		   int size = 0;
		   String displayValue = context.getSourceWidget().getDisplayValue().toString();
		   BioCollectionBean listCollection = null;
	    try {
	    		String sQueryString = "(wm_storer.STORERKEY ~= '%" + displayValue + "%' AND  wm_storer.TYPE ~= '%" + TYPE + "%')";
	    	   _log.debug("LOG_SYSTEM_OUT","sQueryString = "+ sQueryString,100L);
	    	   Query BioQuery = new Query(BIO,sQueryString,null);
	    	   UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
	           listCollection = uow.getBioCollectionBean(BioQuery);
	           size = listCollection.size();
	          
	        
        } catch(EpiException e) {
            
            // Handle Exceptions 
		    e.printStackTrace();
		    return RET_CANCEL;
		    
	    } 
        if (size == 0) {
        	RuntimeFormInterface FormName = context.getState().getCurrentRuntimeForm();
	    	String WidgetName = context.getSourceWidget().getName();
	    	String[] ErrorParem = new String[2];
     	   	ErrorParem[0]= displayValue;
     	   	ErrorParem[1] = ATTRIBUTE_LABEL;
     	   	FieldException UsrExcp = new FieldException(FormName, WidgetName,"NotValidEntry", ErrorParem);
     	   	throw UsrExcp;
     	   
        }
        else
        {
        	
     	   _log.debug("LOG_SYSTEM_OUT","Entered Owner is Valid ",100L);
        }
	    return RET_CONTINUE;
		
	}
	    protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {

		       try {
		           // Add your code here to process the event
		           
		        } catch(Exception e) {
		            
		            // Handle Exceptions 
		          e.printStackTrace();
		          return RET_CANCEL;          
		       } 
		       
		       return RET_CONTINUE;
		    }
}

