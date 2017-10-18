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

import java.util.ArrayList;
import java.util.Iterator;

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.exceptions.FormException;
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
import com.ssaglobal.scm.wms.util.FormUtil;

public class PutOnHoldAction  extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PutOnHoldAction.class);
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		  _log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Executing PutOnHoldAction ",100L);
		StateInterface state = context.getState();
		RuntimeListFormInterface listForm = (RuntimeListFormInterface) FormUtil.findForm(
																							state.getCurrentRuntimeForm(),
																							"wms_list_shell",
																							"wm_accumulated_charges_list_view",
																							state);
		ArrayList items = listForm.getAllSelectedItems();
		if (isZero(items))
		{
			_log.debug("LOG_SYSTEM_OUT","\n\t" + "NOTHING SELECTED" + "\n",100L);
			throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
		}
	return RET_CONTINUE;	
	}

	
	
	
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
	{
		  _log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Executing PutOnHoldAction ",100L);
		final String BIO = "wm_accumulated_charges";
		String status= null;
		String lineType = null;
		String accChargesKey = null;
		String query = null;
		
		
		StateInterface state = ctx.getState();
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
			
			
			
			if (listFocus instanceof BioCollection){  
				
				
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
							
					      	status= bean.getValue("STATUS").toString();
		                			    			                	
							if(status.equals("0"))
							{
								//checkAdjust
								lineType = bean.get("LINETYPE").toString();
								if(!isAdjust(lineType))
								{
									//perform On Hold operation
									//_log.debug("LOG_SYSTEM_OUT","\n\n***Putting Charge on Hold ***\n\n",100L);
									bean.set("STATUS", "H");
									accChargesKey = bean.get("ACCUMULATEDCHARGESKEY").toString();
									//_log.debug("LOG_SYSTEM_OUT","\n\n***Accumulated Charges Key: " +accChargesKey,100L);
									
									
									//check if records exist where referencekey= accumulatedchargeskey
									query = BIO +".REFERENCEKEY='" +accChargesKey +"'";
									//_log.debug("LOG_SYSTEM_OUT","****Rec. with reference key Query: " +query,100L);
									Query qry = new Query(BIO, query, null);
									BioCollectionBean newFocus = uowb.getBioCollectionBean(qry);
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
											String refStatus = refBean.getValue("STATUS").toString();
											//_log.debug("LOG_SYSTEM_OUT","\n*** Status value:" +refStatus +"***",100L);
											refBean.setValue("STATUS", "H");
											
										}
									}
									
								}
								
							}
						}
					
					
						try{
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
								throwUserException(ex, "ERROR_FINALIZING", null);
							}
								
						}	
					}catch(RuntimeException e)
					{
						e.printStackTrace();}				
				}//end if
				
			}
		}
			catch (RuntimeException e1)
			{
			e1.printStackTrace();
			}
			
			  _log.debug("LOG_DEBUG_EXTENSION_ACC_CHARGES","Exiting PutOnHoldAction ",100L);
			return RET_CONTINUE;
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
