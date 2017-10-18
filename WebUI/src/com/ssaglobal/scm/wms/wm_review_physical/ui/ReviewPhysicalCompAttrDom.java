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
package com.ssaglobal.scm.wms.wm_review_physical.ui;

import java.util.ArrayList;
import java.util.List;

import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.metadata.AttributeDomainExtensionBase;
import com.epiphany.shr.ui.metadata.DropdownContentsContext;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class ReviewPhysicalCompAttrDom extends AttributeDomainExtensionBase{
	private static String EACH = "EA";
	private static String UOM = "UOM";
	private static String PACK = "PACK";
	private static String SUFFIX = "KEY";
	
	public int execute(DropdownContentsContext context, List values, List labels) throws EpiDataException{
		//Get passed parameters
		ArrayList ignoredPrefixes = (ArrayList) getParameter("ignoredPrefixes");
		ArrayList ignoredSuffixes = (ArrayList) getParameter("ignoredSuffixes");
		
		//Determine pack name
		String packName = buildPackName(context.getFormWidget().getName(), ignoredPrefixes, ignoredSuffixes);
		String temp = context.getState().getCurrentRuntimeForm().getFormWidgetByName(packName).getDisplayValue();
		String pack = temp==null ? temp : temp.toUpperCase();
		
		//Set query
		String query = "SELECT PACKUOM1 AS PACKUOM FROM PACK WHERE PACKUOM1 <> ' ' AND PACKKEY = '"+pack+"' UNION " +
				"SELECT PACKUOM2 AS PACKUOM FROM PACK WHERE PACKUOM2 <> ' ' AND PACKKEY = '"+pack+"' UNION " +
				"SELECT PACKUOM3 AS PACKUOM FROM PACK WHERE PACKUOM3 <> ' ' AND PACKKEY = '"+pack+"' UNION " +
				"SELECT PACKUOM4 AS PACKUOM FROM PACK WHERE PACKUOM4 <> ' ' AND PACKKEY = '"+pack+"' UNION " +
				"SELECT PACKUOM5 AS PACKUOM FROM PACK WHERE PACKUOM5 <> ' ' AND PACKKEY = '"+pack+"' UNION " +
				"SELECT PACKUOM6 AS PACKUOM FROM PACK WHERE PACKUOM6 <> ' ' AND PACKKEY = '"+pack+"' UNION " +
				"SELECT PACKUOM7 AS PACKUOM FROM PACK WHERE PACKUOM7 <> ' ' AND PACKKEY = '"+pack+"' UNION " +
				"SELECT PACKUOM8 AS PACKUOM FROM PACK WHERE PACKUOM8 <> ' ' AND PACKKEY = '"+pack+"' UNION " +
				"SELECT PACKUOM9 AS PACKUOM FROM PACK WHERE PACKUOM9 <> ' ' AND PACKKEY = '"+pack+"'";
		
		//Execute query
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		
		//Build dropdown from results
		if(results.getRowCount()>0){
			for(int i=0; i<results.getRowCount(); i++){
				values.add(results.getAttribValue(1).getAsString());
				labels.add(results.getAttribValue(1).getAsString());
				results.getNextRow();
			}
		}else{
			values.add(EACH);
			labels.add(EACH);
		}
		return RET_CONTINUE;
	}
		
	private String buildPackName(String widgetName, ArrayList ignoredPrefixes, ArrayList ignoredSuffixes){
		//Logic for dynamically determining pack name 
		int index = 0;
		String packName = null, prefix = widgetName.replaceAll(UOM, "");
		boolean usePrefix = prefix.equalsIgnoreCase("") ? false : true;
		boolean useSuffix = true;
		while(usePrefix && index<ignoredPrefixes.size()){
			if(widgetName.equals(ignoredPrefixes.get(index).toString())){
				usePrefix = false;
			}
			index++;
		}
		index = 0;
		while(index<ignoredSuffixes.size()){
			if(widgetName.equals(ignoredSuffixes.get(index).toString())){
				useSuffix = false;
			}
			index++;
		}
		packName = usePrefix ? prefix + PACK : PACK;
		if(useSuffix){
			packName = packName + SUFFIX;
		}
		return packName;
	}
}