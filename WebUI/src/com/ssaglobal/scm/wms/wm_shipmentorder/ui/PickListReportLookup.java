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
package com.ssaglobal.scm.wms.wm_shipmentorder.ui;

import java.util.List;

import com.epiphany.shr.ui.metadata.AttributeDomainExtensionBase;
import com.epiphany.shr.ui.metadata.DropdownContentsContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class PickListReportLookup extends AttributeDomainExtensionBase
{
	private final static String SHELL_SLOT = "list_slot_1";

	private final static String TAB_GROUP_SLOT = "tbgrp_slot";

	private final static String TAB_0 = "tab 0";

	protected int execute(DropdownContentsContext context, List value, List labels) throws EpiException {		

		StateInterface state = context.getState();
		RuntimeFormInterface shell = state.getCurrentRuntimeForm().getParentForm(state);
		
		RuntimeFormInterface headerForm = state.getRuntimeForm(shell.getSubSlot("tbgrp_slot"), "tab 0");
		RuntimeFormInterface shipForm = state.getRuntimeForm(shell.getSubSlot("tbgrp_slot"), "tab 1");

		RuntimeFormWidgetInterface storerKey = headerForm.getFormWidgetByName("STORERKEY");
		String currentStorerValue = storerKey.getDisplayValue();
		RuntimeFormWidgetInterface consigneeKey = shipForm.getFormWidgetByName("CONSIGNEEKEY");
		String currentConsigneeValue = consigneeKey.getDisplayValue();
		

		String query = "SELECT pbsrpt_reports.rpt_id, pbsrpt_reports.rpt_title "+
						"FROM storer_reports, pbsrpt_reports "+
						"WHERE storer_reports.customReportType = 1 "+
						"AND storer_reports.storerkey = '"+currentStorerValue+"' "+
						"AND ( storer_reports.consigneekey = '"+currentConsigneeValue+"' OR storer_reports.consigneekey = 'DEFAULT' ) "+
						"AND storer_reports.rpt_id = pbsrpt_reports.rpt_id ";
		
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		for(int idx =0; idx < results.getRowCount(); idx++)
		{
			String rpt_id = results.getAttribValue(1).getAsString();
			String rpt_title = results.getAttribValue(2).getAsString();

			value.add(rpt_id);
			labels.add(rpt_title);
			
			results.getNextRow();
		}
		
		return RET_CONTINUE;
	}	
}
