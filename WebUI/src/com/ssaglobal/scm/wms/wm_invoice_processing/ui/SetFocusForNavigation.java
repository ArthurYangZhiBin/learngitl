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
package com.ssaglobal.scm.wms.wm_invoice_processing.ui;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.DataValue;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.exceptions.FormException;
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
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;

public class SetFocusForNavigation extends com.epiphany.shr.ui.action.ActionExtensionBase implements Serializable
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(SetFocusForNavigation.class);
	protected int execute(ActionContext context, ActionResult result) throws EpiException, UserException
	{
	  	 _log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Executing SetFocusForNavigation",100L);
		StateInterface state = context.getState();
		if (context.getServiceManager().getUserContext().containsKey("InvoicePreFilter"))
		{
			try
			{
				RunBillingDataObject obj = (RunBillingDataObject) context.getServiceManager().getUserContext().get(
																													"InvoicePreFilter");
				String fromOwner = obj.getOwnerStart();
				String toOwner = obj.getOwnerEnd();
				String fromBilling = obj.getBillingStart();
				String toBilling = obj.getBillingEnd();
				String chargeType = obj.getChargeType();
				Date date= obj.getDate();
				long dateMil = date.getTime();
				SimpleDateFormat ejbFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				String dateVal = ejbFormat.format(date);
				_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","***Date Val: " +dateVal,100L);
				
				String chargeQueryString= "";
				
				
				Query loadBiosQry = null;
				Query loadChargeTypeQry= null;
				Query loadDateQry= null;
				
				if (!isEmpty(fromOwner))
				{
					loadBiosQry = new Query("wm_invoice_processing", "wm_invoice_processing.STORERKEY >='" + fromOwner
							+ "' and wm_invoice_processing.STORERKEY <='" + toOwner + "'", null);
				}
				else
				{
				//	loadBiosQry = new Query("wm_invoice_processing", "wm_invoice_processing.BILLINGGROUP >='"
				//			+ fromBilling + "' and wm_invoice_processing.BILLINGGROUP <='" + toBilling + "'", null);
						
					String sql= "SELECT accumulatedcharges.accumulatedchargeskey " +
							"FROM accumulatedcharges, storerbilling " +
							"WHERE accumulatedcharges.storerkey = storerbilling.storerkey " + 
							"AND storerbilling.billinggroup between '" +fromBilling +"' and '" +toBilling +"' " +  
							"ORDER BY accumulatedcharges.storerkey";
					
					_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Join Query: " +sql,100L);					
					EXEDataObject dataObject = WmsWebuiValidationSelectImpl.select(sql);
					
					if(dataObject.getCount() > 1)
					{						
					Array val = new Array();
					val = dataObject.getAttributes();
					int size = dataObject.getCount();
					//_log.debug("LOG_SYSTEM_OUT","\n Attributes size" +val.size() ,100L);
					String sqlString= "";
					for(int i =0; i< size-1 ; i++)
					{
						//_log.debug("LOG_SYSTEM_OUT","\n " +dataObject.getAttribValue(new TextData("accumulatedchargeskey")).toString(),100L);						
						dataObject.deleteCurrentRow();											
						
						if(i == 0)
						{
							sqlString = sqlString + " wm_invoice_processing.ACCUMULATEDCHARGESKEY ='" +dataObject.getAttribValue(new TextData("accumulatedchargeskey")).toString() +"'";
						}
						else
						{
							sqlString = sqlString + " OR wm_invoice_processing.ACCUMULATEDCHARGESKEY ='" +dataObject.getAttribValue(new TextData("accumulatedchargeskey")).toString() +"'";
						}
					}
					//_log.debug("LOG_SYSTEM_OUT","\n***Final String: " +sqlString,100L);
					_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","**Final String: " +sqlString,100L);
					loadBiosQry = new Query("wm_invoice_processing", sqlString, null);
					}
					else
					{
						String errorMessage = getTextMessage("WMEXP_NO_INVOICES", new Object[1], state.getLocale());
					}
					
				}
				//_log.debug("LOG_SYSTEM_OUT","\n\t" + loadBiosQry.getQueryExpression() + "\n",100L);
				_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","**Query: " +loadBiosQry.getQueryExpression(),100L);
				UnitOfWorkBean uow = state.getDefaultUnitOfWork();
				BioCollectionBean newFocus = uow.getBioCollectionBean(loadBiosQry);				
				//_log.debug("LOG_SYSTEM_OUT","\n\n No. of records returned: " + newFocus.size(),100L);
				_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","** No. of records returned: " + newFocus.size(),100L);
				if (newFocus.size() < 1)
				{
					String errorMessage = getTextMessage("WMEXP_NO_INVOICES", new Object[1], state.getLocale());
					//Set Error Message into Session
					state.getRequest().getSession().setAttribute("INVPROCERRORMSG", errorMessage);
				}	
				
				
				String[] chType = chargeType.split ("x");
				for (int i=0; i < chType.length-1; i++)
				  {
						if(i==0)
							chargeQueryString = chargeQueryString +"wm_invoice_processing.CHARGETYPE='" +chType[i] +"'";
						else
							chargeQueryString = chargeQueryString +" OR wm_invoice_processing.CHARGETYPE='" +chType[i] +"'";
				  }
				//_log.debug("LOG_SYSTEM_OUT","\n\n**** chargeQueryString: " +chargeQueryString,100L);
				_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","** chargeQueryString: " +chargeQueryString,100L);
				
				loadChargeTypeQry= new Query("wm_invoice_processing", chargeQueryString, null);
				BioCollectionBean finalFocus= (BioCollectionBean)newFocus.filter(loadChargeTypeQry);
				//_log.debug("LOG_SYSTEM_OUT","\n\n*****Final focus size: " +finalFocus.size(),100L);
				_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","**Final focus size: " +finalFocus.size(),100L);
				if (finalFocus.size() < 1)
				{
					//throw new UserException("WMEXP_NO_INVOICES", new Object[1]);
					String errorMessage = getTextMessage("WMEXP_NO_INVOICES", new Object[1], state.getLocale());
					//Set Error Message into Session
					state.getRequest().getSession().setAttribute("INVPROCERRORMSG", errorMessage);

				}				
				
				//issue fix
				//loadDateQry= new Query("wm_invoice_processing", "wm_invoice_processing.BILLTHRUDATE <=@DATE['" + dateMil + "']", null);
				loadDateQry= new Query("wm_invoice_processing", "wm_invoice_processing.BILLFROMDATE <=@DATE['" + dateMil + "']", null);
				//_log.debug("LOG_SYSTEM_OUT","\n\n**** Query Expression: " +loadDateQry.getQueryExpression(),100L);
				_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","** Query Expression: " +loadDateQry.getQueryExpression(),100L);
				BioCollectionBean dateFocus= (BioCollectionBean)finalFocus.filter(loadDateQry);
				//_log.debug("LOG_SYSTEM_OUT","\n\n*****Date focus size: " +dateFocus.size(),100L);
				_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","**Date focus size: " +dateFocus.size(),100L);
				if (dateFocus.size() < 1)
				{
					//throw new UserException("WMEXP_NO_INVOICES", new Object[1]);
					String errorMessage = getTextMessage("WMEXP_NO_INVOICES", new Object[1], state.getLocale());
					//Set Error Message into Session
					state.getRequest().getSession().setAttribute("INVPROCERRORMSG", errorMessage);

				}
				result.setFocus(dateFocus);
				
			} catch (Exception x)
			{
				x.printStackTrace();
				context.getServiceManager().getUserContext().remove("InvoicePreFilter");
				throw new UserException("System_Error", new Object[1]);
			}
		}
		context.getServiceManager().getUserContext().remove("InvoicePreFilter");
		_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Exiting SetFocusForNavigation",100L);
		return RET_CONTINUE;
	}

	private boolean isEmpty(Object attributeValue)

	{

		if (attributeValue == null)
		{
			return true;
		}
		else if (attributeValue.toString().matches("\\s*"))
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
	{
		try
		{
			_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Executing SetFocusForNavigation",100L);
			StateInterface state = ctx.getState();
			try
			{

				state.closeModal(true);
				RuntimeFormInterface modalForm = state.getCurrentRuntimeForm();
				RuntimeFormInterface toolbarForm = ctx.getSourceForm();
				RuntimeFormInterface shellForm = toolbarForm.getParentForm(state);
				SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
				RuntimeFormInterface listForm = (RuntimeFormInterface) state.getRuntimeForm(headerSlot, null);
				//DataBean listFocus = listForm.getFocus();
			} catch (RuntimeException e1)
			{
				e1.printStackTrace();
				return RET_CANCEL;
			}

			Query loadBiosQry = new Query("wm_invoice_processing", "wm_invoice_processing.STORERKEY >'ABC'", null);
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			BioCollectionBean newFocus = uow.getBioCollectionBean(loadBiosQry);
			if (newFocus.size() < 1)
			{
				throw new FormException("SKU_VALIDATION", new Object[1]);
			}
			args.setFocus(newFocus);
		} catch (Exception e)
		{
			e.printStackTrace();
			return RET_CANCEL;
		}
		_log.debug("LOG_DEBUG_EXTENSION_INV_PROC","Executing SetFocusForNavigation",100L);
		return RET_CONTINUE;
	}
}
