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

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.data.error.EpiDataError;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ListSelectQueryAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ListSelectQueryAction.class);

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @exception EpiException
	 */
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{

		String bio = getParameterString("BIO");
		ArrayList attributes = (ArrayList) getParameter("ATTRIBUTE");
		ArrayList currentAttributes = (ArrayList) getParameter("ATTRIBUTEINCURRENTBIO");

		StateInterface state = context.getState();
		String bioRefString = state.getBucketValueString("listTagBucket");
		BioRef bioRef;
		try
		{
			bioRef = BioRef.createBioRefFromString(bioRefString);
		} catch (NullPointerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			_log.error("LOG_ERROR_EXTENSION_ListSelectQueryAction", "Unable to get Bio from List. BioRefString is " + bioRefString , SuggestedCategory.NONE);
			return RET_CANCEL;
		}
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		com.epiphany.shr.ui.model.data.BioBean bioBean = null;

		try
		{
			bioBean = uowb.getBioBean(bioRef);
		} catch (BioNotFoundException bioEx)
		{
			_logger.error(bioEx);
			throw new FormException("ERROR_GET_SEL_BIO_LIST", null);
		}
		
		if(currentAttributes == null || currentAttributes.size() == 0)
		{
			_log.debug("LOG_DEBUG_EXTENSION_ListSelectQueryAction", "Making a copy of attributes", SuggestedCategory.NONE);
			currentAttributes = new ArrayList(attributes);
		}
		
		//build query
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		for(int i = 0; i < currentAttributes.size(); i++)
		{
			if(i >= 1)
			{
				pw.print(" and ");
			}
			Object selectedValue = bioBean.getValue(currentAttributes.get(i).toString());
			if (isEmpty(selectedValue))
			{
				_log.error("LOG_ERROR_EXTENSION_ListSelectQueryAction", "Empty record, doing nothing", SuggestedCategory.NONE);
				return RET_CANCEL;
			}
			_log.debug("LOG_DEBUG_EXTENSION", "Selected " + selectedValue, SuggestedCategory.NONE);
			pw.print(bio + "." + attributes.get(i) + " = '" + selectedValue + "'");
			
		}
		if(StringUtils.isNotBlank(getParameterString("STORER_TYPE"))){
			pw.print(" AND (" + bio + "." + "TYPE = '" + getParameterString("STORER_TYPE") + "')");
		}
		pw.flush();
		sw.flush();
		String query = sw.toString();
		//_log.debug("LOG_SYSTEM_OUT",query,100L);

		_log.debug("LOG_DEBUG_EXTENSION_ListSelectQueryAction", "Query: " + query, SuggestedCategory.NONE);
		//query 
		BioCollectionBean collection = uowb.getBioCollectionBean(new Query(bio, query, null));
		if (collection.size() != 1)
		{
			_log.error("LOG_ERROR_EXTENSION", "Unable to return unique result for query: " + query + ". Size: "
					+ collection.size(), SuggestedCategory.NONE);
			return RET_CANCEL;
		}
		result.setFocus(collection.get(String.valueOf(0)));

		return RET_CONTINUE;
	}

	protected boolean isEmpty(Object attributeValue)
	{

		if (attributeValue == null)
		{
			return true;
		}
		else if (attributeValue.toString().matches("\\s*"))
		{
			return true;
		}
		else
		{
			return false;
		}

	}

}
