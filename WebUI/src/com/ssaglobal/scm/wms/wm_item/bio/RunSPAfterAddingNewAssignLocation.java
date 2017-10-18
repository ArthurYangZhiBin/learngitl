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

package com.ssaglobal.scm.wms.wm_item.bio;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.io.PrintWriter;
import java.io.StringWriter;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.data.beans.BioServiceFactory;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
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

public class RunSPAfterAddingNewAssignLocation extends com.epiphany.shr.data.bio.extensions.BioExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(RunSPAfterAddingNewAssignLocation.class);

	/**
	 * Returning CANCEL from these will NOT stop the update
	 * It is called after the transaction is committed
	 *  To do any saving,  the client must be called on a new UnitOfWork.
	 * <P>
	 * @param context The EpnyServiceContext for this BioExtension instance
	 * @param bioChanged The BioRef for this BioExtension instance
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
	 * @exception EpiException 
	 */
	protected int bioAfterUpdate(EpnyServiceContext context, BioRef bioChanged) throws EpiException
	{

		// Replace the following line with your code,
		//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		//	 as appropriate
		// Leave the line if the code is not going to handle 
		//  bioAfterUpdate events

		_log.debug("LOG_DEBUG_EXTENSION_RunSPAfterAddingNewAssignLocation", "After Update", SuggestedCategory.NONE);

		return runSP(context, bioChanged);
	}

	/**
	 * Returning CANCEL from these will NOT stop the insert
	 * It is called after the transaction is committed
	 *  To do any saving,  the client must be called on a new UnitOfWork.
	 * <P>
	 * @param context The EpnyServiceContext for this BioExtension instance
	 * @param bioInserted The BioRef for this BioExtension instance
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
	 * @exception EpiException 
	 */
	protected int bioAfterInsert(EpnyServiceContext context, BioRef bioInserted) throws EpiException
	{

		_log.debug("LOG_DEBUG_EXTENSION_RunSPAfterAddingNewAssignLocation", "After Insert", SuggestedCategory.NONE);

		return runSP(context, bioInserted);
	}

	private int runSP(EpnyServiceContext context, BioRef bioRef) throws EpiException
	{
		/*
		 * After saving assign location you need to call this stored procedure:

		 NSP_ADDPICKLOCATION

		 Paremeters:    storerkey,sku,loc

		 */
		_log.debug("LOG_DEBUG_EXTENSION_RunSPAfterAddingNewAssignLocation", "Retrieving Bio", SuggestedCategory.NONE);
		Bio bio = BioServiceFactory.getInstance().create("webui").getUnitOfWork().fetchBio(bioRef);

		String storerKey = bio.getString("STORERKEY") == null ? null :  bio.getString("STORERKEY").toUpperCase();
		String sku = bio.getString("SKU") == null ? null : bio.getString("SKU").toUpperCase();
		String loc = bio.getString("LOC") == null ? null : bio.getString("LOC").toUpperCase();
		
		

		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		Array params = new Array();
		params.add(new TextData(storerKey));
		params.add(new TextData(sku));
		params.add(new TextData(loc));
		_log.debug("LOG_DEBUG_EXTENSION_RunSPAfterAddingNewAssignLocation", storerKey + " " + sku + " " + loc, SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION_RunSPAfterAddingNewAssignLocation", "Params " + params.toString(), SuggestedCategory.NONE);
		actionProperties.setProcedureParameters(params);
		actionProperties.setProcedureName("NSP_ADDPICKLOCATION");
		try
		{
			EXEDataObject results = WmsWebuiActionsImpl.doAction(actionProperties);
			displayResults(results);
			_log.debug("LOG_DEBUG_EXTENSION_RunSPAfterAddingNewAssignLocation", "Results " + results.toString(), SuggestedCategory.NONE);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return RET_CONTINUE;
	}

	private void displayResults(EXEDataObject results)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		pw.println("" + results.getRowCount() + " x " + results.getColumnCount() + "\n");
		if (results.getColumnCount() != 0)
		{
			pw.println("---Results");
			for (int i = 1; i < results.getColumnCount() + 1; i++)
			{
				try
				{
					pw.println(" " + i + " @ " + results.getAttribute(i).name + " "
							+ results.getAttribute(i).value.getAsString());
				} catch (Exception e)
				{
					pw.println(e.getMessage());
				}
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION", sw.toString(), SuggestedCategory.NONE);
	}
}
