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


package com.ssaglobal.scm.wms.util.upload;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.metadata.DropdownContentsContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;

import java.util.List;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
*/
public class TemplateIdLookup extends com.epiphany.shr.ui.metadata.AttributeDomainExtensionBase {

	/**
     * Fires whenever the values and/or labels for an attribute domain are requested.  The base list
     * of labels and values are provided and may be filtered, modified, replaced, etc as desired.
     * <ul>
     * <li>{@link com.epiphany.shr.ui.metadata.DropdownContentsContext DropdownContentsContext} exposes information
     * about the context in which the attribute domain is being used, including the service and the user interface
     * {@link com.epiphany.shr.ui.state.StateInterface state} and
     * {@link com.epiphany.shr.ui.view.RuntimeFormWidgetInterface form widget}.</li>
     * @param context the {@link com.epiphany.shr.ui.metadata.DropdownContentsContext DropdownContentsContext}
     * @param values the list of values to be modified
     * @param labels the corresponding list of labels to be modified
     */
    protected int execute(DropdownContentsContext context, List values, List labels)
        throws EpiException
    {

    	StateInterface state = context.getState();
    	UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
    	EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
    	
    	
		String dbEnterprise =(userContext.get(SetIntoHttpSessionAction.DB_ISENTERPRISE)).toString();
    	
		String stmt=null;
		stmt =" codelkup.LISTNAME = 'TEMPLATE'";

		/**
		if(dbEnterprise.equals("1"))
			stmt =" wm_template.enterprisecontextflag='1' ";
		else
			stmt = " wm_template.whsecontextflag='1' ";
		**/
    	//Query query = new Query("wm_template", stmt, null);
		Query query = new Query("codelkup", stmt, null);
    	BioCollectionBean bioCollection = uowb.getBioCollectionBean(query);
    	
    	//_log.debug("LOG_SYSTEM_OUT","[TemplateIdLookup]BioCollection size::"+bioCollection.size(),100L);
    	
    	for(int i = 0; i < bioCollection.size(); i++){
    		BioBean templateBio = (BioBean)bioCollection.elementAt(i);
    		//String templateid =  templateBio.getString("templateid");
    		String templateid =  templateBio.getString("SHORT");
    		String description = templateBio.getString("DESCRIPTION");
    		String code = templateBio.getString("CODE");
    		
    		if (templateid != null){
    			if(templateid.charAt(3)=='0'){
    				
    				if(dbEnterprise.equals("1")){
    					values.add(code);
    					labels.add(description);
    				}
    			}else if(templateid.charAt(3)=='1'){
    				
    				if(dbEnterprise.equals("0")){
    					values.add(code);
    					labels.add(description);
    					
    				}
    			}else if(templateid.charAt(3)=='2'){
					values.add(code);
					labels.add(description);
    				
    			}
    				
    			//values.add(templateid);
    			//labels.add(templateid);
    		}
    	}
    	return RET_CONTINUE;
    }

    /**
     * Fires whenever the values and/or labels for a hierarchical attribute domain are requested.  The base list
     * of labels and values are provided and may be filtered, modified, replaced, etc as desired.  The filter list
     * provides the list of selections in the hierarchy to be used to determine the result.
     * <ul>
     * <li>{@link com.epiphany.shr.ui.metadata.DropdownContentsContext DropdowContentsContext} exposes information
     * about the context in which the attribute domain is being used, including the service and the user interface
     * {@link com.epiphany.shr.ui.state.StateInterface state} and
     * {@link com.epiphany.shr.ui.view.RuntimeFormWidgetInterface form widget}.</li>
     * @param context the {@link com.epiphany.shr.ui.metadata.DropdownContentsContext DropdownContentsContext}
     * @param values the list of values to be modified
     * @param labels the corresponding list of labels to be modified
     * @param filter the list of currently selected widget values on which this dropdown depends
     */
    protected int execute(DropdownContentsContext context, List values, List labels, List filter)
        throws EpiException
    {
        // Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
        return RET_CONTINUE;
    }
}