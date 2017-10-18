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
package com.ssaglobal.scm.wms.wm_item.ui;

import java.util.List;

import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.metadata.AttributeDomainExtensionBase;
import com.epiphany.shr.ui.metadata.DropdownContentsContext;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;


/**
 * 
 * 02/25/2009	AW Machine:2275155 SDIS: SCM_00000-06330
 * 				   Initial version
 *
 */


public class ItemAssignLocReplUOMComputedAttrDomain extends AttributeDomainExtensionBase{

	private static String EACH = "EA";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemAssignLocReplUOMComputedAttrDomain.class);
	
	public int execute(DropdownContentsContext context, List values, List labels) throws EpiDataException{
		_log.debug("LOG_DEBUG_EXTENSION_ItemAssignLocReplUOMComputedAttrDomain","**Executing ItemAssignLocReplUOMComputedAttrDomain",100L);	
		String temp = context.getState().getCurrentRuntimeForm().getFormWidgetByName("PACKTEMP").getDisplayValue();
		String pack = temp==null ? temp : temp.toUpperCase();		
		
		_log.debug("LOG_DEBUG_EXTENSION_ItemAssignLocReplUOMComputedAttrDomain","**PACKTEMP: " +pack,100L);
		//Set query		
		 String qry =  "SELECT 2 AS CODE ,PACKUOM1 AS PACKUOM FROM PACK WHERE PACKUOM1 <> ' '  AND PACKKEY = '"+pack+"' UNION " +   
		  "SELECT 3 AS CODE ,PACKUOM2 AS PACKUOM FROM PACK WHERE PACKUOM2 <> ' '  AND PACKKEY = '"+pack+"' UNION " +   
		  "SELECT 6 AS CODE ,PACKUOM3 AS PACKUOM FROM PACK WHERE PACKUOM3 <> ' '  AND PACKKEY = '"+pack+"' UNION " +   
		  "SELECT 1 AS CODE ,PACKUOM4 AS PACKUOM FROM PACK WHERE PACKUOM4 <> ' '  AND PACKKEY = '"+pack+"' UNION " +   
		  "SELECT 4 AS CODE ,PACKUOM8 AS PACKUOM FROM PACK WHERE PACKUOM8 <> ' '  AND PACKKEY = '"+pack+"' UNION " +   
		  "SELECT 5 AS CODE ,PACKUOM9 AS PACKUOM FROM PACK WHERE PACKUOM9 <> ' '  AND PACKKEY = '"+pack+"'";
		 
		//Execute query
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(qry);
		
		//Build dropdown from results
		if(results.getRowCount()>0){
			for(int i=0; i<results.getRowCount(); i++){
				values.add(results.getAttribValue(1).getAsString());
				labels.add(results.getAttribValue(2).getAsString());
				results.getNextRow();
			}
		}else{
			values.add(EACH);
			labels.add(EACH);
		}
		return RET_CONTINUE;
	}
}