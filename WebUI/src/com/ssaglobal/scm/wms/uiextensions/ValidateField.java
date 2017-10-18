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
package com.ssaglobal.scm.wms.uiextensions;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeMenuElementInterface;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;

public class ValidateField extends com.epiphany.shr.ui.action.ActionExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidateField.class);

	/**
	 * The code within the execute method will be run on the WidgetRender.
	 * <P>
         * @param state The StateInterface for this extension
         * @param widget The RuntimeFormWidgetInterface for this extension's widget
         * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 */
	   protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		   final String BIO = getParameterString("BIO");
		   final String ATTRIBUTE = getParameterString("ATTRIBUTE");
		   final String ATTRIBUTE_LABEL = getParameterString("ATTRIBUTE_LABEL");
		   _log.debug("LOG_SYSTEM_OUT","BIO = " + BIO,100L);
		   _log.debug("LOG_SYSTEM_OUT","ATTRIBUTE = " + ATTRIBUTE,100L);
		   _log.debug("LOG_SYSTEM_OUT","ATTRIBUTE_LABEL = " + ATTRIBUTE_LABEL,100L);
		   
		   int size = 0;
		   String displayValue = context.getSourceWidget().getDisplayValue().toString();
	    try {
	    	
	    	
	    		String sQueryString = "(" + BIO + "." + ATTRIBUTE + " ~= '%" + displayValue + "%')";
	    	   _log.debug("LOG_SYSTEM_OUT","sQueryString = "+ sQueryString,100L);
	    	   Query BioQuery = new Query(BIO,sQueryString,null);
	    	   UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
	           BioCollectionBean listCollection = uow.getBioCollectionBean(BioQuery);
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
     	   	_log.debug("LOG_SYSTEM_OUT","Location is not present in the loc table = " + ATTRIBUTE_LABEL,100L);
     	   	FieldException UsrExcp = new FieldException(FormName, WidgetName,"NotValidEntry", ErrorParem);
     	   	throw UsrExcp;
     	   
        }
        else
        {
     	   _log.debug("LOG_SYSTEM_OUT","Entered Location is Valid ",100L);
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

