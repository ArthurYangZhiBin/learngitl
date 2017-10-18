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
package com.ssaglobal.scm.wms.wm_accumulated_charges.action;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.SsaException;
import com.ssaglobal.scm.wms.datalayer.EJBRemote;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.service.exeprocessmanager.TransactionServiceSORemote;
import com.ssaglobal.scm.wms.uiextensions.KeyGenBioWrapper;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.LocaleUtil;

public class ZeroTotalAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ZeroTotalAction.class);
	protected static final String internalCaller = "internalCall";
	
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		  _log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Executing ZeroTotalAction",100L);
		StateInterface state = context.getState();
		RuntimeListFormInterface listForm = (RuntimeListFormInterface) FormUtil.findForm(
																							state.getCurrentRuntimeForm(),
																							"wms_list_shell",
																							"wm_accumulated_charges_list_view",
																							state);
		ArrayList items = listForm.getAllSelectedItems();
		if (isZero(items))
		{
			throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
		}
		  _log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Exiting ZeroTotalAction",100L);
	return RET_CONTINUE;	
	}

	
	
	
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
	{
		  _log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Executing ZeroTotalAction",100L);
		final String BIO = "wm_accumulated_charges";
		String chargeType= null;
		String lineType = null;
		String accChargesKey = null;
		String status = null;
		String refKey = null;
		String query = null;
		double totalVal= 0;
		double debitVal, creditVal;
		double parentDebit, parentCredit;
		double ZERO= 0;
		
		StateInterface state = ctx.getState();
		NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW 06/15/06
		try
		{

			state.closeModal(true);
			RuntimeFormInterface modalForm = state.getCurrentRuntimeForm();
			RuntimeFormInterface toolbarForm = ctx.getSourceForm();
			RuntimeFormInterface shellForm = toolbarForm.getParentForm(state);
	
			SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
			RuntimeFormInterface formSlot = state.getRuntimeForm(headerSlot, null);
			
			SlotInterface slot= formSlot.getSubSlot("list_slot");
			RuntimeFormInterface form = state.getRuntimeForm(slot, null);
			
			RuntimeListFormInterface listForm = (RuntimeListFormInterface) form; 
			
			
			DataBean listFocus = listForm.getFocus();

			if (listFocus instanceof BioCollection){  /*if biocollection*/
								
				ArrayList itemsSelected = listForm.getAllSelectedItems();

				if(itemsSelected != null && itemsSelected.size()>0)
				{
				Iterator bioBeanIter = itemsSelected.iterator();
				UnitOfWorkBean uowb= state.getDefaultUnitOfWork();
				try{
					BioBean bean= null;
						for(; bioBeanIter.hasNext(); )
						{
							bean = (BioBean)bioBeanIter.next();
							
					      	chargeType = bean.getValue("CHARGETYPE").toString();
		                	//_log.debug("LOG_SYSTEM_OUT","\nChargeType is: " +chargeType,100L);
		                			    			                	
							if(!chargeType.equals("SP"))
							{
								//checkAdjust
								lineType = bean.get("LINETYPE").toString();
								status = bean.get("STATUS").toString();
								if(!isAdjust(lineType))
								{
									//perform Zero Total operation
									//_log.debug("LOG_SYSTEM_OUT","\n\n***Zero Totalling charges ***\n\n",100L);
									accChargesKey = bean.get("ACCUMULATEDCHARGESKEY").toString();
									refKey = bean.get("REFERENCEKEY").toString();
									//_log.debug("LOG_SYSTEM_OUT","\n\n***Accumulated Charges Key: " +accChargesKey +"\t**Ref Key: " +refKey,100L);
									if(!accChargesKey.equals(refKey))
									{
										//_log.debug("LOG_SYSTEM_OUT","\n** Calculating charges **",100L);
										//check if records exist where referencekey= accumulatedchargeskey
										query = BIO +".REFERENCEKEY='" +accChargesKey +"'";
										//_log.debug("LOG_SYSTEM_OUT","****Rec. with reference key Query: " +query,100L);
										Query qry = new Query(BIO, query, null);
										BioCollectionBean newFocus = uowb.getBioCollectionBean(qry);
										parentDebit = Double.parseDouble(bean.get("DEBIT").toString());
										parentCredit = Double.parseDouble(bean.get("CREDIT").toString());
										totalVal = totalVal + parentDebit - parentCredit; 
										//_log.debug("LOG_SYSTEM_OUT","\n*** Total before adding child records: " +Double.toString(totalVal),100L);
										
										if(newFocus.size()<1){
												//_log.debug("LOG_SYSTEM_OUT","No records with references to this charge",100L);
											}
										else
										{
									
											
											for(int i= 0; i< newFocus.size(); i++)
											{
												String index = "" +i;
												//_log.debug("LOG_SYSTEM_OUT","\n*** Index value:" +index +"***",100L);
												BioBean refBean = newFocus.get(index);
												String refDebit = refBean.getValue("DEBIT").toString();
												String refCredit = refBean.getValue("CREDIT").toString();
												//_log.debug("LOG_SYSTEM_OUT","\n*** Debit: " +refDebit +"\tCredit: " +refCredit,100L);
												debitVal = Double.parseDouble(refDebit);
												creditVal = Double.parseDouble(refCredit);
												
												totalVal = totalVal + debitVal - creditVal;
												//_log.debug("LOG_SYSTEM_OUT","\n**Current Total: " +Double.toString(totalVal),100L);
												
											}
											
											//_log.debug("LOG_SYSTEM_OUT","\n**Final Total: " +Double.toString(totalVal),100L);
											if(!(totalVal == ZERO))
											{																						
												BioBean newBioBean = null;
												newBioBean = uowb.getNewBio("wm_accumulated_charges");
												//insert record
													//generate new accummulatedchargeskey
													//set other values 
												if(totalVal < 0)
												{
													String deb = nf.format(Math.abs(totalVal));
													newBioBean.setValue("DEBIT", deb);
													newBioBean.setValue("CREDIT", "0");
												
												}
												else
												{
													String cred = nf.format(totalVal);
													newBioBean.setValue("CREDIT", cred);
													newBioBean.setValue("DEBIT", "0");													
												}
												
												//lineType
												if(lineType.equals("N"))
												{
													//set new LineType "NA"
													newBioBean.setValue("LINETYPE", "NA");												
												}
												else
												{
													newBioBean.setValue("LINETYPE", "TA");												
												}
												
												//get Status
												if(status.equals("9"))
												{
													newBioBean.setValue("STATUS", "0");
												
												}
												else
												{
													newBioBean.setValue("STATUS", status);
												
												}
												//getting Key value
												String keyVal = AssignKey(ctx);
												
												//_log.debug("LOG_SYSTEM_OUT","*** Debit: " +newBioBean.getValue("DEBIT"),100L);
												//_log.debug("LOG_SYSTEM_OUT","*** Credit: " +newBioBean.getValue("CREDIT"),100L);		
												//_log.debug("LOG_SYSTEM_OUT","*** LineType: " +newBioBean.getValue("LINETYPE"),100L);
												//_log.debug("LOG_SYSTEM_OUT","*** Status: " +newBioBean.getValue("STATUS"),100L);
												//_log.debug("LOG_SYSTEM_OUT","*** AccumulatedChargesKey: " +keyVal,100L);
												//_log.debug("LOG_SYSTEM_OUT","*** ReferenceKey: " +accChargesKey,100L);
												newBioBean.setValue("ACCUMULATEDCHARGESKEY", keyVal);
												newBioBean.setValue("REFERENCEKEY", accChargesKey);
												newBioBean.set("CHARGETYPE", bean.getValue("CHARGETYPE"));
												newBioBean.set("STORERKEY", bean.getValue("STORERKEY"));
												newBioBean.set("SKU", bean.getValue("SKU"));
												newBioBean.set("LOT", bean.getValue("LOT"));
												newBioBean.set("DESCRIP", bean.getValue("DESCRIP"));
												newBioBean.set("PRINTCOUNT", bean.getValue("PRINTCOUNT"));
												newBioBean.set("SERVICEKEY", bean.getValue("SERVICEKEY"));
												newBioBean.set("ID", bean.getValue("ID"));
												newBioBean.set("UOMSHOW", bean.getValue("UOMSHOW"));
												newBioBean.set("TARIFFKEY", bean.getValue("TARIFFKEY"));
												newBioBean.set("TARIFFDETAILKEY", bean.getValue("TARIFFDETAILKEY"));
												newBioBean.set("TAXGROUPKEY", bean.getValue("TAXGROUPKEY"));
												newBioBean.set("RATE", bean.getValue("RATE"));
												newBioBean.set("BASE", bean.getValue("BASE"));
												newBioBean.set("MASTERUNITS", bean.getValue("MASTERUNITS"));
												newBioBean.set("SYSTEMGENERATEDCHARGE", bean.getValue("SYSTEMGENERATEDCHARGE"));
												newBioBean.set("BILLEDUNITS", bean.getValue("BILLEDUNITS"));
												newBioBean.set("SOURCEKEY", bean.getValue("SOURCEKEY"));
												newBioBean.set("SOURCETYPE", bean.getValue("SOURCETYPE"));
												newBioBean.set("ACCESSORIALDETAILKEY", bean.getValue("ACCESSORIALDETAILKEY"));
												newBioBean.set("GLDISTRIBUTIONKEY", bean.getValue("GLDISTRIBUTIONKEY"));
												newBioBean.set("INVOICEBATCH", bean.getValue("INVOICEBATCH"));
												newBioBean.set("INVOICEKEY", bean.getValue("INVOICEKEY"));
												newBioBean.set("COSTRATE", bean.getValue("COSTRATE"));
												newBioBean.set("COSTBASE", bean.getValue("COSTBASE"));
												newBioBean.set("COSTMASTERUNITS", bean.getValue("COSTMASTERUNITS"));
												newBioBean.set("COSTUOMSHOW", bean.getValue("COSTUOMSHOW"));
												newBioBean.set("COSTSYSTEMGENERATEDCHARGE", bean.getValue("COSTSYSTEMGENERATEDCHARGE"));
												newBioBean.set("COST", bean.getValue("COST"));
												newBioBean.set("COSTUNITS", bean.getValue("COSTUNITS"));
												newBioBean.set("INVOICEDATE", bean.getValue("INVOICEDATE"));
												/*
												 * 
												 * 
												 */
												
											}
										}
										
										
									}
									
				
									
								}
								
							}
						}
					
					
						try{
							//_log.debug("LOG_SYSTEM_OUT","\n\n%%%%%%%%%%In try block",100L);
						uowb.saveUOW(true);
						uowb.clearState();
						args.setFocus(bean);
						}catch(UnitOfWorkException ex)
						{
									
							Throwable nested = ((UnitOfWorkException) ex).getDeepestNestedException();
							//_log.debug("LOG_SYSTEM_OUT","\t\n\n\n\n^^^^^&&&Nested " + nested.getClass().getName(),100L);
							//_log.debug("LOG_SYSTEM_OUT","\tMessage " + nested.getMessage(),100L);
							
							if(nested instanceof ServiceObjectException)
							{
								//_log.debug("LOG_SYSTEM_OUT","\n\n***Message caught" + nested.getMessage(),100L);
								String reasonCode = nested.getMessage();
								//replace terms like Storer and Commodity								
								//throwUserException(ex, reasonCode, null);
								String errorMessage = getTextMessage("WMEXP_EJB_ERROR", new Object[] {reasonCode}, state.getLocale());
								//Set Error Message into Session
								state.getRequest().getSession().setAttribute("CAERRORMESSAGE", errorMessage);
							}
							else
							{
								//_log.debug("LOG_SYSTEM_OUT","\n IN ELSE " ,100L);
								throwUserException(ex, "ERROR_FINALIZING", null);
							}
								
						}
					}catch(RuntimeException e)
					{
						//_log.debug("LOG_SYSTEM_OUT","\n\n******* IN EPIEXCEPTION " ,100L);
						e.printStackTrace();}				
				}//end if
				
			}
		}
			catch (RuntimeException e1)
			{
				//_log.debug("LOG_SYSTEM_OUT","\n\n\n^^^^^ In runtime",100L);
			e1.printStackTrace();
			}
			  _log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Exiting ZeroTotalAction",100L);
			return RET_CONTINUE;
		}			
			
			

  	public String AssignKey(UIRenderContext context){
   		String widgetName = context.getActionObject().getName();
   		String keyName = "ACCUMULATEDCHARGESKEY";
 
   			int rowCount = 0;
   			TextData myKey = null;
   			Array argArray = new Array();
   			EXEDataObject edo =null;
   			argArray.add(new TextData("ACCUMULATEDCHARGES"));
   			argArray.add(new TextData("10"));
   			argArray.add(new TextData("1"));
   			//   		Added following code to get the db_connection information from the usre context

   			EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
   			String db_connection = userContext.get(SetIntoHttpSessionAction.DB_CONNECTION).toString();
   			String wmWhseID = (userContext.get(SetIntoHttpSessionAction.DB_USERID)).toString();

  			try {
   				TransactionServiceSORemote remote = EJBRemote.getRemote();
   				edo = remote.executeProcedure(new TextData(wmWhseID),db_connection,new TextData("GETKEYP1S1"),argArray,null,internalCaller,null);		//HC
   				rowCount = edo.getRowCount();
   				if (edo._rowsReturned())
   				{
   				  myKey = edo.getAttribValue(new TextData("keyname")).asTextData();
   			//	  _log.debug("LOG_SYSTEM_OUT","\n\n\n*******myKeyVal: " +myKey.toString(),100L);
   				}

   			} catch (SsaException x) {
   				_log.error("EXP_1","Could not get the remote...",SuggestedCategory.NONE);
   				_log.error(new EpiException("EXP_1", "SsaException nested in EpiException...", x.getCause()));
            
   			} catch (Exception exc) {
            	exc.printStackTrace();
            	EpiException x = new EpiException("EXP_1", "Unknown", exc);
            	_log.error(x);
            }

   	
   			return myKey.toString();
   	} 	
	


	private boolean isAdjust(String lineType) {
		// TODO Auto-generated method stub
		 if(lineType.equals("NA") || lineType.equals("TA"))
		 {
			 _log.debug("LOG_SYSTEM_OUT","\n***check Adjustment- true",100L);
			 return true;
		 }
		 else
		 {
			 return false;
		 }
	}




	private boolean isZero(ArrayList items) {
		// TODO Auto-generated method stub
		if (items == null)
		{
			return true;
		}
		else if (((ArrayList) items).size() == 0)
		{
			return false;
		}
		else
		{
			return false;
		}
		
	}

}
