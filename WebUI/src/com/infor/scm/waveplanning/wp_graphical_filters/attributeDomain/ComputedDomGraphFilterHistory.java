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

package com.infor.scm.waveplanning.wp_graphical_filters.attributeDomain;

import java.util.HashMap;
import java.util.List;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.metadata.AttributeDomainExtensionBase;
import com.epiphany.shr.ui.metadata.DropdownContentsContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class ComputedDomGraphFilterHistory extends AttributeDomainExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ComputedDomGraphFilterHistory.class);
	public ComputedDomGraphFilterHistory()
	{
	}
	protected int execute(DropdownContentsContext context, List value, List labels) throws EpiException {			
		_log.debug("LOG_DEBUG_EXTENSION_COMPDOMCODELKUP","Executing ComputedDomGraphFilterHistory",100L);
		StateInterface state = context.getState();		
		HashMap codeMap = new HashMap();
		String listName = "ORDRFLD";		
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
//		String locale = getBaseLocale(userContext);
//		_log.debug("LOG_DEBUG_EXTENSION_COMPDOMCODELKUP","locale:"+locale,100L);						
		
//		String qry = "wm_codesdetail.LISTNAME = '"+listName+"' AND wm_codesdetail.LOCALE = '"+locale+"'";				
		String qry = "wm_codesdetail.LISTNAME = '"+listName+"' and wm_codesdetail.ACTIVE = '1' ";
		Query loadBiosQryA = new Query("wm_codesdetail", qry, "wm_codesdetail.SEQUENCE ASC, wm_codesdetail.DESCRIPTION ASC");			
		_log.debug("LOG_DEBUG_EXTENSION_COMPDOMCODELKUP","CODELKUP QRY:"+qry,100L);				
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		BioCollectionBean codeList = uow.getBioCollectionBean(loadBiosQryA);		
//		if(codeList == null || codeList.size() == 0){
//			locale = context.getLocale().getJavaLocale().toString();
//			qry = "wm_codesdetail.LISTNAME = '"+listName+"' AND wm_codesdetail.LOCALE = '"+locale+"'";		
//			loadBiosQryA = new Query("wm_codesdetail", qry, "");	
//			_log.debug("LOG_DEBUG_EXTENSION_COMPDOMCODELKUP","CODELKUP QRY:"+qry,100L);	
//			codeList = uow.getBioCollectionBean(loadBiosQryA);
//			if(codeList == null || codeList.size() == 0){
//				locale = "en";
//				qry = "wm_codesdetail.LISTNAME = '"+listName+"' AND wm_codesdetail.LOCALE = '"+locale+"'";				
//				loadBiosQryA = new Query("wm_codesdetail", qry, "");	
//				_log.debug("LOG_DEBUG_EXTENSION_COMPDOMCODELKUP","CODELKUP QRY:"+qry,100L);	
//				codeList = uow.getBioCollectionBean(loadBiosQryA);
//			}			
//		}
		
		for(int i = 0; i < codeList.size(); i++){
			codeMap.put(codeList.elementAt(i).get("SHORT"), codeList.elementAt(i).get("DESCRIPTION"));
		}
		
		Query fullCriteriaListQry = new Query("wp_graphicalfilter_temp","wp_graphicalfilter_temp.INTERACTIONID = '"+state.getInteractionId()+"'","wp_graphicalfilter_temp.SERIALKEY");
		BioCollection fullCriteriaList = state.getDefaultUnitOfWork().getBioCollectionBean(fullCriteriaListQry); 
		for(int i = 0; i < fullCriteriaList.size(); i++){
			String criteriaType = (String)fullCriteriaList.elementAt(i).get("FILTERTYPE");
			if(!value.contains(criteriaType)){
				value.add(criteriaType);							
				labels.add(codeMap.get(criteriaType));
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_COMPDOMCODELKUP","Leaving ComputedDomGraphFilterHistory",100L);		
		return RET_CONTINUE;
	}
	
	public static String getBaseLocale(EpnyUserContext userContext){
		String locale = userContext.getLocale();
		if (locale.indexOf("_") == -1){
			if (locale.equalsIgnoreCase("en")){
				locale = locale + "_US";
			}
			if (locale.equalsIgnoreCase("de")){
				locale = locale + "_DE";
			}
			if (locale.equalsIgnoreCase("es")){
				locale = locale + "_ES";
			}
			if (locale.equalsIgnoreCase("nl")){
				locale = locale + "_NL";
			}
			if (locale.equalsIgnoreCase("ja")){
				locale = locale + "_JP";
			}
			if (locale.equalsIgnoreCase("pt")){
				locale = locale + "_BR";
			}
			if (locale.equalsIgnoreCase("zh")){
				locale = locale + "_CN";
			}
			if (locale.equalsIgnoreCase("fr")){
				locale = locale + "_FR";
			}
		}
		return locale;
	}
}
