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

package com.infor.scm.waveplanning.wp_labor_data.computed;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.metadata.DropdownContentsContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;

import java.util.List;

/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 */
public class WPWMFacilityList extends com.epiphany.shr.ui.metadata.AttributeDomainExtensionBase {

	/**
	 * Fires whenever the values and/or labels for an attribute domain are
	 * requested. The base list of labels and values are provided and may be
	 * filtered, modified, replaced, etc as desired.
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.metadata.DropdownContentsContext DropdownContentsContext}
	 * exposes information about the context in which the attribute domain is
	 * being used, including the service and the user interface
	 * {@link com.epiphany.shr.ui.state.StateInterface state} and
	 * {@link com.epiphany.shr.ui.view.RuntimeFormWidgetInterface form widget}.</li>
	 * 
	 * @param context
	 *            the
	 *            {@link com.epiphany.shr.ui.metadata.DropdownContentsContext DropdownContentsContext}
	 * @param values
	 *            the list of values to be modified
	 * @param labels
	 *            the corresponding list of labels to be modified
	 */
	protected int execute(DropdownContentsContext context, List values, List labels) throws EpiException {

		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getTempUnitOfWork();
		BioCollectionBean rs = uow.getBioCollectionBean(new Query("wm_pl_db", "wm_pl_db.db_enterprise != 1", null));
		for (int i = 0; i < rs.size(); i++) {
			BioBean facilityRow = rs.get("" + i);
			final Object dbLogid = facilityRow.getValue("db_logid");
			final Object dbAlias = facilityRow.getValue("db_alias");
			values.add(dbLogid);
			labels.add(dbAlias);
		}
		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		return RET_CONTINUE;
	}

	
}