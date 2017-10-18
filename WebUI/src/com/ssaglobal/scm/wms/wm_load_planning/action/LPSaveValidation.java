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
package com.ssaglobal.scm.wms.wm_load_planning.action;

import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
public class LPSaveValidation
{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LPSaveValidation.class);
	
	static public void saveValidation(RuntimeListForm headerListForm, BioBean selectedLoad)
			throws EpiDataException, DPException, UserException
	{

		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		//Validate OutboundLane
		Object outboundLane = selectedLoad.getValue("OUTBOUNDLANE");
		outboundLane = outboundLane == null ? null : outboundLane.toString().toUpperCase();
		if (!(isEmpty(outboundLane)) && !(outboundLane.toString().equals("N/A")))
		{
			String query = "SELECT * FROM " + "LANE" + " WHERE " + "LANEKEY" + " = '" + outboundLane + "'";
			EXEDataObject sqlResults = WmsWebuiValidationSelectImpl.select(query);
			if (sqlResults.getRowCount() == 0)
			{
				//Invalid Value, throw error
				String[] parameters = new String[2];
				parameters[0] = outboundLane.toString();
				parameters[1] = headerListForm.getFormWidgetByName("OUTBOUNDLANE").getLabel("label", locale);
				_log.debug("LOG_DEBUG_EXTENSION", "!@ " + parameters[0] + " -- " + parameters[1], SuggestedCategory.NONE);
				throw new UserException("WMEXP_INVALID_VALUE", "WMEXP_INVALID_VALUE", parameters);
			}
		}
		else
		{
			//set to space
			selectedLoad.setValue("OUTBOUNDLANE", " ");

		}
		//Check Route, Stop, OutboundLane, ExternalLoadId for Blanks
		Object route = selectedLoad.getValue("ROUTE");
		Object stop = selectedLoad.getValue("STOP");
		Object externalLoadId = selectedLoad.getValue("EXTERNALLOADID");
		if ((isEmpty(route)))
		{
			selectedLoad.setValue("ROUTE", " ");
		}
		if ((isEmpty(stop)))
		{
			selectedLoad.setValue("STOP", " ");
		}
		if ((isEmpty(externalLoadId)))
		{
			selectedLoad.setValue("EXTERNALLOADID", " ");
		}

	}

	static public boolean isNull(Object attributeValue)
			throws EpiDataException
	{

		if (attributeValue == null)
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	static public boolean isEmpty(Object attributeValue)
			throws EpiDataException
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
