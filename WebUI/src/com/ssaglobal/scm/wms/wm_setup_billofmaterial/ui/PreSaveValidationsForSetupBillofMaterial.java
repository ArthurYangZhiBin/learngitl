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
package com.ssaglobal.scm.wms.wm_setup_billofmaterial.ui;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.metadata.interfaces.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.uiextensions.HeaderDetailSave;

public class PreSaveValidationsForSetupBillofMaterial extends ActionExtensionBase{
	  protected static ILoggerCategory _log = LoggerFactory.getInstance(PreSaveValidationsForSetupBillofMaterial.class);
	  
	  public PreSaveValidationsForSetupBillofMaterial() { 
	      _log.info("EXP_1","PreSaveValidationsForSetupBillofMaterial!!!",  SuggestedCategory.NONE);
	  }
	  protected int execute(ActionContext context, ActionResult result) throws UserException, EpiException {
	  	//_log.debug("LOG_SYSTEM_OUT","\n\n*******Validating Records************\n\n",100L);

	  			
	  	StateInterface state = context.getState();
	  	RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
	  	//_log.debug("LOG_SYSTEM_OUT","\n\n*** TOOLBAR NAME: " +toolbar.getName() +"\n\n",100L);
	  	RuntimeFormInterface shellForm = toolbar.getParentForm(state);
	  	//_log.debug("LOG_SYSTEM_OUT","\n\n^&* shellForm:" +shellForm.getName(),100L);
	  	
	  	SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
	  	RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
	  	//_log.debug("LOG_SYSTEM_OUT","\n\n ****Name of detail form " +detailForm.getName(),100L);
	  	
	    String storerVal= (String)detailForm.getFormWidgetByName("STORERKEY").getValue();
	    DataBean focus = detailForm.getFocus();
	    
	    if(focus.isTempBio())
	    {	
	    //Query for Storer Table Validation
	  	String query = "SELECT * " + "FROM STORER " 
	  	+ "WHERE (STORERKEY = '" + storerVal + "') "
	  	+ "AND (TYPE = '1') ";
	  	
	     // _log.debug("LOG_SYSTEM_OUT","///QUERY \n" + query,100L);
	      EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);

	      	//Valid Record- Valid STORERKEY value
	      	if (results.getRowCount() >= 1)
	      	{	
	      		//_log.debug("LOG_SYSTEM_OUT","\n*** Record Exists - Validation Passed\n\n ",100L);	      		
	      	}
	      	else
	      	{
	      		//_log.debug("LOG_SYSTEM_OUT","\n *** Record Does Not exist- Save Disallowed \n\n",100L);
	      		String [] param = new String[1];
				param[0] = storerVal;
	      		throw new UserException("WMEXP_INVALID_OWNER", param);	
	      	}	
	  	
	      	
		     String skuVal= (String)detailForm.getFormWidgetByName("SKU").getValue();
		     String compSkuVal= (String)detailForm.getFormWidgetByName("COMPONENTSKU").getValue();
		    //Query for SKU Table Validation
		  	String queryItem = "SELECT * " + "FROM SKU " 
		  	+ "WHERE (SKU = '" + skuVal + "') ";
		  	
		      //_log.debug("LOG_SYSTEM_OUT","///QUERY \n" + query,100L);
		      EXEDataObject resultItem = WmsWebuiValidationSelectImpl.select(queryItem);

		      	//Valid Record- Valid SKU value
		      	if (resultItem.getRowCount() >= 1)
		      	{	
		      		//_log.debug("LOG_SYSTEM_OUT","\n*** SKU Record Exists - Validation Passed\n\n ",100L);	      		
		      	}
		      	else
		      	{
		      		//_log.debug("LOG_SYSTEM_OUT","\n *** Item Record Does Not exist- save disallowed ",100L);
		      		String [] param = new String[1];
					param[0] = skuVal;
		      		throw new UserException("WMEXP_INVALID_ITEM", param);  		
		      	}	
	      	
	      	
	      	
		    //Query for SKU, Owner Combination Validation
		  	String querySKU = "SELECT * " + "FROM SKU " 
		  	+ "WHERE (STORERKEY = '" + storerVal + "') "
		  	+ "AND (SKU = '" + skuVal + "') ";
		  	
		     // _log.debug("LOG_SYSTEM_OUT","///QUERY \n" + querySKU,100L);
		      EXEDataObject resultsSKU = WmsWebuiValidationSelectImpl.select(querySKU);

		      	//Valid Record- Valid STORERKEY value
		      	if (resultsSKU.getRowCount() >= 1)
		      	{	
		      		//_log.debug("LOG_SYSTEM_OUT","\n***SKU - Owner Combination Exists - Valid Combination\n\n",100L);
		      	}
		      	else
		      	{
		      		//_log.debug("LOG_SYSTEM_OUT","\n\n***SKU-Owner Combination does not exist in SKU table - Validation Failed",100L);
		      		String [] param = new String[2];
					param[0] = storerVal;
					param[1] = skuVal;
		      		throw new UserException("WMEXP_INVALID_OWNER_ITEM_COMB", param);
		      		
		      	}	
	      		      	
			    //Query for ComponentSKU Table Validation
		      	String compSKUVal= (String)detailForm.getFormWidgetByName("COMPONENTSKU").getValue();	
			  	String queryCompItem = "SELECT * " + "FROM SKU " 
			  	+ "WHERE (SKU = '" + compSKUVal + "') ";
			  	
			      //_log.debug("LOG_SYSTEM_OUT","///QUERY \n" + query,100L);
			      EXEDataObject resultCompItem = WmsWebuiValidationSelectImpl.select(queryCompItem);

			      	//Valid Record- Valid SKU value
			      	if (resultCompItem.getRowCount() >= 1)
			      	{	
			      		//_log.debug("LOG_SYSTEM_OUT","\n\n***Component SKU Record Exists - Validation Passed\n ",100L);	      		
			      	}
			      	else
			      	{
			      		//_log.debug("LOG_SYSTEM_OUT","\n ***Component Item Record Does Not exist - Save Disallowed\n\n ",100L);
			      		String [] param = new String[1];
						param[0] = compSKUVal;
			      		throw new UserException("WMEXP_INVALID_COMP_ITEM", param);			      		  			
			      	}	
		      	
		      	
	      	//Validate ComponentSKU
			    
				    //Query for SKU Table Validation
				  	String queryCompSKU = "SELECT * " + "FROM SKU " 
				  	+ "WHERE (STORERKEY = '" + storerVal + "') "
				  	+ "AND (SKU = '" + compSKUVal + "') ";
				  	
				      //_log.debug("LOG_SYSTEM_OUT","///QUERY \n" + queryCompSKU,100L);
				      EXEDataObject resultsCompSKU = WmsWebuiValidationSelectImpl.select(queryCompSKU);

				      	//Valid Record- Valid STORERKEY value
				      	if (resultsCompSKU.getRowCount() >= 1)
				      	{	
				      		//_log.debug("LOG_SYSTEM_OUT","\n***Component SKU- Owner Combination Exists\n\n",100L);
				      	}
				      	else
				      	{
				      		//_log.debug("LOG_SYSTEM_OUT","***Component SKU-Owner Combination is does not exist - Validation Failed\n\n",100L);		 
				      		String [] param = new String[2];
							param[0] = storerVal;
							param[1] = compSKUVal;
				      		throw new UserException("WMEXP_INVALID_OWNER_COMPITEM_COMB", param); 			
				      	}	   	
	      	
	      	
				      	//Validate Sequence Combination
				      	Object seqVal = detailForm.getFormWidgetByName("SEQUENCE").getValue();
				      	if (seqVal !=null && !seqVal.toString().equalsIgnoreCase(""))
				      	{
				      		String seqStrVal= (String)seqVal;
				      		//_log.debug("LOG_SYSTEM_OUT","\n** Sequence Value is " +seqStrVal,100L);
				      		
						  	String querySeq = "SELECT * " + "FROM BILLOFMATERIAL " 
						  	+ "WHERE (STORERKEY = '" + storerVal + "') "
						  	+ "AND (SKU = '" + skuVal + "') "
						  	+ "AND (SEQUENCE = '" + seqStrVal + "') ";
						  	
						      //_log.debug("LOG_SYSTEM_OUT","///QUERY \n" + querySeq,100L);
						      EXEDataObject resultsSeq = WmsWebuiValidationSelectImpl.select(querySeq);

						      	//Valid Record- Valid STORERKEY value
						      	if (resultsSeq.getRowCount() >= 1)
						      	{	
						      	//	_log.debug("LOG_SYSTEM_OUT","\n***Sequence, Owner, Item Combination Exists\n\n",100L);
						      		String [] param = new String[3];
									param[0] = storerVal;
									param[1] = skuVal;
									param[2] = seqStrVal;
						      		throw new UserException("WMEXP_INVALID_COMB", param);
						      	}
						      	else
						      	{
						      		//_log.debug("LOG_SYSTEM_OUT","***Sequence - SKU-Owner Combination is Unique - Validation Passed\n\n",100L);		 
						  			
						      	}	   	
				      		
				      	}
				      	
				      	//Validate Sequence Combination
	
						  	String queryComb = "SELECT * " + "FROM BILLOFMATERIAL " 
						  	+ "WHERE (STORERKEY = '" + storerVal + "') "
						  	+ "AND (SKU = '" + skuVal + "') "
						  	+ "AND (COMPONENTSKU = '" + compSkuVal + "') ";
						  	
						     // _log.debug("LOG_SYSTEM_OUT","***QUERY: " + queryComb,100L);
						      EXEDataObject resultsSeq = WmsWebuiValidationSelectImpl.select(queryComb);

						      	//Valid Record- Valid STORERKEY value
						      	if (resultsSeq.getRowCount() >= 1)
						      	{	
						      	//	_log.debug("LOG_SYSTEM_OUT","***Owner, Item, Component Item Combination Exists. Duplicates are not allowed",100L);
						      		String [] param = new String[3];
									param[0] = storerVal;
									param[1] = skuVal;
									param[2] = compSkuVal;
						      		throw new UserException("WMEXP_INVALID_COMB", param);
						      	}
						      	else
						      	{
						      		//_log.debug("LOG_SYSTEM_OUT","***Sequence - SKU-Owner Combination is Unique - Validation Passed\n\n",100L);		 						  			
						      	}	   					      
	    }		      	
				      	
				        return RET_CONTINUE;     	
	  	
	  }

	
	  }

