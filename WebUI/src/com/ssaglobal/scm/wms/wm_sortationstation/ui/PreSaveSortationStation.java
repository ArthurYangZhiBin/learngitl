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
package com.ssaglobal.scm.wms.wm_sortationstation.ui;

import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.uiextensions.HeaderDetailSave;

public class PreSaveSortationStation extends ActionExtensionBase{
	  protected static ILoggerCategory _log = LoggerFactory.getInstance(PreSaveSortationStation.class);
	  
public PreSaveSortationStation() { 
    _log.info("EXP_1","PreSaveSortationStation Instantiated!!!",  SuggestedCategory.NONE);
}
protected int execute(ActionContext context, ActionResult result) throws UserException, EpiException {
	//SM 08/13/07 ISSUE #6834: PACK, OWNER, LOCATION, ITEM dropdown converted to lookups
	//SM 08/13/07 Adjusted logic for lookup functionality
	//SM 08/13/07 System.outs commented out as well.
	//_log.debug("LOG_SYSTEM_OUT","\n\n*******In PreSaveSortationStation************\n\n",100L);
	_log.debug("LOG_DEBUG_EXTENSION_PRESAVE_SORTATIONSTATION","**Executing PreSaveSortationStation",100L);	
	
	RuntimeFormInterface detailForm= null;		
	StateInterface state = context.getState();
	RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
	//_log.debug("LOG_SYSTEM_OUT","*** TOOLBAR NAME: " +toolbar.getName(),100L);
	RuntimeFormInterface shellForm = toolbar.getParentForm(state);
	//_log.debug("LOG_SYSTEM_OUT","*** shellForm:" +shellForm.getName(),100L);
	
	SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
	RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
	DataBean headerFocus = headerForm.getFocus();
	String sortKey= (String)headerForm.getFormWidgetByName("SORTATIONSTATIONKEY").getValue();
    
		SlotInterface shellSlot = shellForm.getSubSlot("list_slot_2");	
		detailForm= state.getRuntimeForm(shellSlot, null);
			//New Record
		    if (!detailForm.getName().equals("wm_sortationstation_detail_view"))
		    {
				RuntimeFormInterface toggleForm = state.getRuntimeForm(shellSlot, null);
				detailForm = state.getRuntimeForm(toggleForm.getSubSlot("wm_sortationstation_detail_toggle"), "wm_sortationstation_detail_toggole_tab"  );
				//_log.debug("LOG_SYSTEM_OUT","***Detail Form Name: " +detailForm.getName(),100L);	
		    }

		    
		    _log.debug("LOG_DEBUG_EXTENSION_PRESAVE_SORTATIONSTATION","**DetailForm Name: " +detailForm.getName(),100L);

		//PRIMARY KEY VALIDATION FOR NEW RECORD  
		if(headerFocus.isTempBio()){
			//SM 07/30/07 ISSUE #6834 Convert location dropdown to popup widget: added validation logic
			checkValidLoc(sortKey);
			//SM 07/30/07 End edit
			checkPK(sortKey);
		}
		
		DataBean detailFocus = detailForm.getFocus();
		if(detailFocus.isTempBio())
		{
			detailFocus = (QBEBioBean)detailFocus;
			
			String loc ="";
			loc= (String)detailForm.getFormWidgetByName("SORTLOCATION").getValue();
//			_log.debug("LOG_SYSTEM_OUT","*** loc is " +loc,100L);
			_log.debug("LOG_DEBUG_EXTENSION_PRESAVE_SORTATIONSTATION","**Location: " +loc,100L);
			loc = loc.toUpperCase(); 
			
			//checkValidLoc(loc);
			checkLocDuplicates(loc, sortKey);
			detailFocus.setValue("SORTLOCATION", loc);
		}
  
	
    	return RET_CONTINUE;
}


private void checkLocDuplicates(String loc, String sortKey) throws DPException, UserException{
	// TODO Auto-generated method stub
	_log.debug("LOG_DEBUG_EXTENSION_PRESAVE_SORTATIONSTATION","**Executing checkLocDuplicates " +loc,100L);
	String query = "SELECT * " + "FROM SORTATIONSTATIONDETAIL " 
	+ "WHERE (SORTLOCATION = '" + loc + "') "
	+ "AND (SORTATIONSTATIONKEY = '" + sortKey + "') ";
	
    //_log.debug("LOG_SYSTEM_OUT","***QUERY:" + query,100L);
	_log.debug("LOG_DEBUG_EXTENSION_PRESAVE_SORTATIONSTATION","**QUERY:" + query,100L);
    EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);

    	//Duplicate record
    	if (results.getRowCount() >= 1)
    	{	
//    		_log.debug("LOG_SYSTEM_OUT","*** Record Exists",100L);
			String [] param = new String[2];
			param[0] = loc;
			param[1] = sortKey;
			throw new UserException("WMEXP_SORT_LOC_VALID", param);					 			
    	}
    	_log.debug("LOG_DEBUG_EXTENSION_PRESAVE_SORTATIONSTATION","**Exiting checkLocDuplicates ",100L);
}
private void checkValidLoc(String loc) throws DPException, UserException{
	// TODO Auto-generated method stub
	String queryLoc = "SELECT * " + "FROM LOC " 
	+ "WHERE (LOC = '" + loc + "') AND LOCATIONTYPE = 'SORT' ";
	
//	  _log.debug("LOG_SYSTEM_OUT","***QUERYLoc:" + queryLoc,100L);
	  EXEDataObject resultsLoc = WmsWebuiValidationSelectImpl.select(queryLoc);

	    	//Duplicate record
	    	if (resultsLoc.getRowCount() < 1)
	    	{	
//	    		_log.debug("LOG_SYSTEM_OUT","*** Invalid Location",100L);
				String [] param = new String[1];
				param[0] = loc;
				throw new UserException("WMEXP_SORT_INVALID_LOC", param);
	    	}
}
private void checkPK(String sortKey) throws DPException, UserException{
	// TODO Auto-generated method stub
	_log.debug("LOG_DEBUG_EXTENSION_PRESAVE_SORTATIONSTATION","**Executing checkPK " ,100L);
	String queryPK = "SELECT * " + "FROM SORTATIONSTATION " 
	+ "WHERE (SORTATIONSTATIONKEY = '" + sortKey + "') ";
	
    //_log.debug("LOG_SYSTEM_OUT","***QUERY PK: " + queryPK,100L);
	_log.debug("LOG_DEBUG_EXTENSION_PRESAVE_SORTATIONSTATION","**QUERY:" + queryPK,100L);
    EXEDataObject resultsPK = WmsWebuiValidationSelectImpl.select(queryPK);

    	//Duplicate record
    	if (resultsPK.getRowCount() >= 1)
    	{	
    		_log.debug("LOG_DEBUG_EXTENSION_PRESAVE_SORTATIONSTATION","**PK Record exists",100L);
    		//_log.debug("LOG_SYSTEM_OUT","***PK Record Exists",100L);
			String [] param = new String[1];
			param[0] = sortKey;
			throw new UserException("WMEXP_SORTKEY_VALID", param);
    	}
}

}
