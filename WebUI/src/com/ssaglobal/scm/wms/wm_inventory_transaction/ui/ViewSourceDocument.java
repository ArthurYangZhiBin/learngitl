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
package com.ssaglobal.scm.wms.wm_inventory_transaction.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.DataValue;

import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.eai.exception.EAIError;
import com.epiphany.shr.metadata.objects.FormSlot;
import com.epiphany.shr.metadata.objects.ScreenSlot;
import com.epiphany.shr.metadata.objects.TabIdentifier;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;

import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class ViewSourceDocument extends ListSelector implements Serializable{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ViewSourceDocument.class);

	protected int execute(ActionContext context, ActionResult result) throws EpiException {
		_log.debug("LOG_SYSTEM_OUT","\n\n*******In ViewSourceDocument************\n\n",100L);
		
		ViewSourceDocDataObject data = new ViewSourceDocDataObject();
		boolean validate = false;
		
		java.util.HashMap selectedItemsMap = null;	  	  
		StateInterface state = context.getState();
		TabIdentifier tab = (TabIdentifier)getParameter("tab identifier");
		String targetSlotType = (String)getParameter("target slot type");
		FormSlot fs = (FormSlot)getParameter("form slot");
		ScreenSlot ss = (ScreenSlot)getParameter("screen slot");
		boolean cascade = false;
		ArrayList listForms = new ArrayList();
		selectedItemsMap = getSelectedItemsMap(state, targetSlotType, fs, tab, cascade, listForms);		
		if(selectedItemsMap != null)
			result.setSelectedItems(selectedItemsMap);
		ArrayList selectedItems = result.getSelectedItems();		
		RuntimeFormInterface searchForm = context.getSourceWidget().getForm();
		searchForm = searchForm.getParentForm(state);
		
		_log.debug("LOG_SYSTEM_OUT","\n\n Name of searchForm is " + searchForm.getName(),100L);
		
		if(selectedItems != null && selectedItems.size() > 0)
		{
		    if(selectedItems.size()> 1){
		    	String [] param = new String[1];
				param[0] = "Action: View Source Document";
		    	throw new UserException("WMEXP_SINGLE_SELECT_VALIDATION",param);
		    }
			
			Iterator bioBeanIter = selectedItems.iterator();
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			try
			{
				BioBean bio;
				for(; bioBeanIter.hasNext();){
					bio = (BioBean)bioBeanIter.next();
					String sourceType = bio.getString("SOURCETYPE");
					_log.debug("LOG_SYSTEM_OUT","\n\n****Source Type:"+sourceType+"\n\n",100L);
					
					//String sourcekey = bio.getString("SOURCEKEY");
					//_log.debug("LOG_SYSTEM_OUT","\n\n****Source Type:"+sourcekey+"\n\n",100L);	
					
					Object srcKey = bio.getValue("SOURCEKEY");
					if( srcKey != null && srcKey.toString().length() == 15)
					{
					String key = srcKey.toString().substring(0, 10);
					String lineNum = srcKey.toString().substring(10, 15);
					
					_log.debug("LOG_SYSTEM_OUT","\n\n#### key: " +key +"\n\n***line num: " +lineNum,100L);
						
			  		String sql = "SELECT description FROM codelkup " 
					+ "WHERE (listname = 'ITRNSTYPE') "
					+ "AND (short = '" + sourceType + "') ";
								  		
					_log.debug("LOG_SYSTEM_OUT","\n\n** Query is: " +sql,100L);
					EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
					
						if(dataObject.getCount()> 0)
						{
							DataValue dv= dataObject.getValueAt(1, "description");
							_log.debug("LOG_SYSTEM_OUT","\n\n#####value" +dv.toString()+"####\n\n",100L);
							checkIfDocExists(dv.toString());
					
							data.setKey(key);
							data.setLineNumber(lineNum);
							data.setSourceType(dv.toString());
					
							
									
									try{
										_log.debug("LOG_SYSTEM_OUT","\n** Checking for existing records and setting values",100L);
										validate = ViewSourceDocUtil.setValues(data);
										context.getServiceManager().getUserContext().put("viewSrc", data);
									}catch(DPException dpE){
										dpE.printStackTrace();
										throw new UserException("System_Error",new Object[1] );			
									}
									if(!validate){
										String [] param = new String[4];
										param[0] = data.getkeyAtt();
					     				param[1] = data.getKey();
					     				param[2] = data.getLNAtt();
					     				param[3] = data.getLineNumber();
										throw new UserException("WMEXP_NO_SOURCE_DOCUMENT", param);			
									}	
							//ViewSourceDocUtil.setValues(data);
							//context.getServiceManager().getUserContext().put("viewSrc", data);
						}
						else
						{			
							String [] param = new String[1];
							param[0] = sourceType;
							throw new UserException("WMEXP_NO_SOURCEDOC", param);	
						}
					}
					else
					{
						String [] param = new String[1];
						param[0] = sourceType;
						throw new UserException("WMEXP_NO_SOURCEDOC", param);
					}
				}
				//uowb.saveUOW();
				
				if(listForms.size() <= 0)
					listForms = (ArrayList)getTempSpaceHash().get("SELECTED_LIST_FORMS");
				clearBuckets(listForms);
				result.setSelectedItems(null);
			}
			catch(EpiException ex)
			{
				throwUserException(ex, "ERROR_EXECUTING_ACTION", null);
			}
		}
		else
		{
			throw new UserException("WMEXP_NO_CHECK", new Object[1]);
		}
		
		return RET_CONTINUE;
	}

	private void checkIfDocExists(String strType) throws UserException{
		// TODO Auto-generated method stub
		final String ADJUSTMENT= "Adjustment Line - Add";
		final String REC_REVERSAL= "Receipt Reversal";
		final String RECEIPT= "Receipt Line - Add";
		final String PICKING= "Picking - Update";
		final String TRANSFER= "Transfer Line - Add";
		_log.debug("LOG_SYSTEM_OUT","\n\n*** type is: " +strType,100L);
		
		if(!(strType.equals(ADJUSTMENT)) && !(strType.equals(REC_REVERSAL)) && !(strType.equals(RECEIPT)) && !(strType.equals(PICKING)) && !(strType.equals(TRANSFER))                      )
		{
			String [] param = new String[1];
			param[0] = strType;
			throw new UserException("WMEXP_NO_SOURCEDOC", param);
		}
		
	}
}
