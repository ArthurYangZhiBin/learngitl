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
package com.ssaglobal.scm.wms.wm_dropid.ui;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.uiextensions.ListSelectQueryAction;

public class DropIDSetNavigationForItem extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(DropIDSetNavigationForItem.class);

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @exception EpiException
	 * 
	 */
	
	/**
	 * Modification History
	 * 01/08/2009 AW 	Initial version
	 * 					SDIS: SCM-00000-06252 Machine: 2253832
	 * 					If Multiple SKU's were in the same CASEID, the sku should display "MIXED".
	 * 					On clicking the MIXED link, a details of the sku's should popup.
	 * 					Otherwise, a details of the item should display.
	 */
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{

		String bio = getParameterString("BIO");
		ArrayList attributes = (ArrayList) getParameter("ATTRIBUTE");
		ArrayList currentAttributes = (ArrayList) getParameter("ATTRIBUTEINCURRENTBIO");
		String nav =null;
		boolean isMixed = false;
		
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
			_log.error("LOG_ERROR_EXTENSION_DropIDSetNavigationForItem", "Unable to get Bio from List. BioRefString is " + bioRefString , SuggestedCategory.NONE);
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
			_log.debug("LOG_DEBUG_EXTENSION_DropIDSetNavigationForItem", "Making a copy of attributes", SuggestedCategory.NONE);
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
			String att = currentAttributes.get(i).toString();
			Object selectedValue = bioBean.getValue(att);
			if (isEmpty(selectedValue))
			{
				_log.error("LOG_ERROR_EXTENSION_DropIDSetNavigationForItem", "Empty record, doing nothing", SuggestedCategory.NONE);
				return RET_CANCEL;
			}
			else if(selectedValue.toString().equalsIgnoreCase("MIXED"))
			{
				_log.debug("LOG_DEBUG_EXTENSION_DropIDSetNavigationForItem", "MIXED", SuggestedCategory.NONE);
				isMixed = true;
				nav = "clickEvent1354";
				i= currentAttributes.size();
			}
			else
			{
				_log.debug("LOG_DEBUG_EXTENSION_DropIDSetNavigationForItem", "Selected " + selectedValue, SuggestedCategory.NONE);
				pw.print(bio + "." + attributes.get(i) + " = '" + selectedValue + "'");
			}
			
	
			
		}
		pw.flush();
		sw.flush();
		
		
		if(!isMixed)
		{	
		String query = sw.toString();

		_log.debug("LOG_DEBUG_EXTENSION_DropIDSetNavigationForItem", "Query: " + query, SuggestedCategory.NONE);
		//query 
		BioCollectionBean collection = uowb.getBioCollectionBean(new Query(bio, query, null));
		if (collection.size() != 1)
		{
			_log.error("LOG_ERROR_EXTENSION", "Unable to return unique result for query: " + query + ". Size: "
					+ collection.size(), SuggestedCategory.NONE);
			return RET_CANCEL;
		}
		result.setFocus(collection.get(String.valueOf(0)));
		nav ="clickEvent1352";
		}
		else
		{
			String caseID = bioBean.getValue("CHILDID").toString();
			String qry = "wm_pickdetail.CASEID='" +caseID +"'";
			Query pickDetailBioQry = new Query("wm_pickdetail", qry, null);
			
			BioCollectionBean newFocus = uowb.getBioCollectionBean(pickDetailBioQry);
			if(newFocus.size()<1){
			return RET_CANCEL;
			}
			result.setFocus(newFocus);
			
		}
		
		context.setNavigation(nav);
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
