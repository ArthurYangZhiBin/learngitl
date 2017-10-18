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

package com.infor.scm.waveplanning.common.attributeDomain;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.metadata.DropdownContentsContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;

import java.util.ArrayList;
import java.util.List;

import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
*/
public class WPOrderStatusAttributeDomain extends com.epiphany.shr.ui.metadata.AttributeDomainExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPOrderStatusAttributeDomain.class);

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
	protected int execute(DropdownContentsContext context, List values, List labels) throws EpiException
	{
		StateInterface state = context.getState();
		Query loadBiosQryA = new Query("wm_orderstatussetup", "", "");
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		BioCollectionBean codeList = uow.getBioCollectionBean(loadBiosQryA);
		
		for (int i = 0; i < codeList.size(); i++)
		{
			Bio code = codeList.elementAt(i);
			final Object codeValue = code.get("CODE");
			values.add(codeValue);
			final Object descValue = code.get("DESCRIPTION");
			labels.add(descValue);
			//Wave Planning Orders remove leading 0, so we have to remove to get the code to match
			//ex 09 becomes 9
			if(((String) codeValue).startsWith("0"))
			{
				String truncatedCode = ((String) codeValue).substring(1);
				values.add(truncatedCode);
				labels.add(descValue);
			}
		}
		
		return RET_CONTINUE;
	}

	
}