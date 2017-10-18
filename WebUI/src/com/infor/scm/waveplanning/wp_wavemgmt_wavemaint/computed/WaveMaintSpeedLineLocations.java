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

package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.computed;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.metadata.DropdownContentsContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.infor.scm.waveplanning.common.util.BioAttributeUtil;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
*/
public class WaveMaintSpeedLineLocations extends com.epiphany.shr.ui.metadata.AttributeDomainExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WaveMaintSpeedLineLocations.class);

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
		_log.debug("LOG_DEBUG_EXTENSION_WaveMaintSpeedLineLocations_execute", "Entering WaveMaintSpeedLineLocations",
				SuggestedCategory.NONE);
		StateInterface state = context.getState();
		DataBean focus = state.getCurrentRuntimeForm().getFocus();
		HashMap dropdownEntries = new HashMap();

		if (focus.isBioCollection() && ((BioCollectionBean) focus).size() >= 1)
		{
			BioCollectionBean rows = (BioCollectionBean) focus;
			BioBean firstRow = rows.get("0");
			String waveKey = firstRow.getString("WAVEKEY");
//				BioAttributeUtil.getInt((DataBean) firstRow, "WAVEKEY");
			String interactionId = state.getInteractionId();

			//get speed line locations based on current interactionid and wave
			Query locQuery = new Query("wp_speedlinelocationswaveheadervotemp",
					"wp_speedlinelocationswaveheadervotemp.INTERACTIONID = '" + interactionId
							+ "' and wp_speedlinelocationswaveheadervotemp.WAVEKEY = '" + waveKey + "'", "");
			BioCollectionBean rs = state.getDefaultUnitOfWork().getBioCollectionBean(locQuery);
			for (int i = 0; i < rs.size(); i++)
			{
				BioBean loc = rs.get("" + i);

				final String locValue = BioAttributeUtil.getString((DataBean) loc, "LOC");

				final String descValue = BioAttributeUtil.getString((DataBean) loc, "DESCRIPTION");
				if (locValue != null && !locValue.matches("\\s*"))
				{
					dropdownEntries.put(locValue, descValue);
//					values.add(locValue);
//					labels.add(descValue);
				}
			}

			//Add locations based on the current form
			//This is a workaround because OA will not display values not in the dropdown
			for (int i = 0; i < rows.size(); i++)
			{
				String locValue = BioAttributeUtil.getString((DataBean) rows.get("" + i), "CONSOLLOC");
				if (locValue != null && !locValue.matches("\\s*") && !dropdownEntries.keySet().contains(locValue))
				{
					dropdownEntries.put(locValue, locValue);
//					values.add(locValue);
//					labels.add(locValue);
				}
			}
			
			//Put dropdownEntries into the dropdown sorted
			Vector dropdownKeys = new Vector(dropdownEntries.keySet());
			for(Iterator it = dropdownKeys.iterator(); it.hasNext();)
			{
				final Object key = it.next();
				values.add(key);
				labels.add(dropdownEntries.get(key));
			}

		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION_WaveMaintSpeedLineLocations_execute", "No records on Consolidate Form",
					SuggestedCategory.NONE);
		}
		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		_log.debug("LOG_DEBUG_EXTENSION_WaveMaintSpeedLineLocations_execute", "Leaving WaveMaintSpeedLineLocations",
				SuggestedCategory.NONE);
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
	protected int execute(DropdownContentsContext context, List values, List labels, List filter) throws EpiException
	{
		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		return RET_CONTINUE;
	}
}