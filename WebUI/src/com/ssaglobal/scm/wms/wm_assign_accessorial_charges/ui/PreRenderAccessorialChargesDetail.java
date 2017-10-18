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
package com.ssaglobal.scm.wms.wm_assign_accessorial_charges.ui;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.LocaleUtil;

public class PreRenderAccessorialChargesDetail extends FormExtensionBase{
	 protected static ILoggerCategory _log = LoggerFactory.getInstance(PreRenderAccessorialChargesDetail.class);
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
	throws UserException{
		
		 _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Executing PreRenderAccessorialChargesDetail",100L);
		String formName = null;
		RuntimeFormInterface shellForm=null;
		boolean checkSessionVals= false;
		StateInterface state = context.getState();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface toggleForm = toolbar.getParentForm(state);
		
		if(toggleForm.getName().equals("wms_list_shell"))
		{
			shellForm= toolbar.getParentForm(state);
			checkSessionVals= true;
		}
		else
		{
			shellForm = toggleForm.getParentForm(state);
		}

		SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");		//HC
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		RuntimeListFormInterface listForm = (RuntimeListFormInterface) headerForm;
		
		DataBean detailFocus = form.getFocus();
		if(detailFocus.isTempBio())
		{detailFocus = (QBEBioBean)detailFocus;}
		else
		{detailFocus= (BioBean)detailFocus;}
		
		if(detailFocus.isTempBio())
		{
		form.getFormWidgetByName("BILLEDUNITS").setProperty(RuntimeFormInterface.PROP_READONLY, Boolean.FALSE);
		DataBean listFocus = listForm.getFocus();
		if (listFocus instanceof BioCollection){
			formName= getFormTitle(listForm);
			
			ArrayList itemsSelected = listForm.getAllSelectedItems();
			if(itemsSelected != null && itemsSelected.size() ==1)
			{
				 _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Items have been selected",100L);
				Iterator bioBeanIter = itemsSelected.iterator();
				try{
					BioBean bean= null;
						for(; bioBeanIter.hasNext(); )
						{
							bean = (BioBean)bioBeanIter.next();
							AssignAccessorialDataObject obj = new AssignAccessorialDataObject();
							obj= setParams(bean, formName, obj);
														
							//state.getRequest().getSession().setAttribute("POTENTIAL", obj);
							setParamsInFocus(detailFocus, formName, obj);
							detailFocus.setValue("CHARGETYPE", "AC");
							if(checkSessionVals)
							{
								if(state.getServiceManager().getUserContext().containsKey("NEWACC"))
								{
									AccessorialDetailObject object = (AccessorialDetailObject) state.getServiceManager().getUserContext().get("NEWACC");
									String base= object.getBase();
									String desc= object.getDescrip();
									String glDist= object.getGldist();
									String units= object.getMasterUnits();
									String tax= object.getTaxGroup();
									String rate= object.getRate();
								
									
									
									_log.debug("LOG_SYSTEM_OUT","\n\nACCESSORIAL: " +desc,100L);
									
									if(!isNull(base))
										detailFocus.setValue("BASE", object.getBase());
									if(!isNull(desc))
										detailFocus.setValue("ACCESSORIALDETAILKEY", object.getDescrip());
									if(!isNull(glDist))
										detailFocus.setValue("GLDISTRIBUTIONKEY", object.getGldist());
									if(!isNull(units))
										detailFocus.setValue("MASTERUNITS", object.getMasterUnits());
									if(!isNull(tax))
										detailFocus.setValue("TAXGROUPKEY", object.getTaxGroup());
									if(!isNull(rate))
										detailFocus.setValue("RATE", object.getRate());
									
									context.getServiceManager().getUserContext().remove("NEWACC");									
								}
							}
						}
				}
				catch(RuntimeException e)
				{
					e.printStackTrace();}	
			}
			else if(itemsSelected ==null)
			{
				String[] param = new String[1];
				param[0] = formName;
				throw new FormException("WMEXP_NOT_SELECTED",param);
			}
			else if(itemsSelected.size() > 1)
			{
				String[] param = new String[1];
				param[0] = formName;
				throw new FormException("WMEXP_TOO_MANY_ITEMS",  param);
			}

			
			
		}
	}
		_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Exiting PreRenderAccessorialChargesDetail",100L);
	return RET_CONTINUE;	
	}
	  private boolean isNull(String widget) {
			// TODO Auto-generated method stub  
			boolean value;
			if(widget == null)			
				value=true;
			else if(widget.matches("\\s*"))
				value=true;
			else
				value=false;
			
			return value;
		}
	private void setParamsInFocus(DataBean detailFocus, String formName, AssignAccessorialDataObject obj) {
		// TODO Auto-generated method stub
		_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","In setParamsInFocus",100L);
		if(formName.endsWith("Detail"))
		{
			//set Storer & Item
			if(formName.equals("Adjustment Detail"))
			{
				detailFocus.setValue("LOT", obj.getlot());
			}		
			detailFocus.setValue("STORERKEY", obj.getStorer());
			detailFocus.setValue("SKU", obj.getItem());	
			detailFocus.setValue("SOURCEKEY", obj.getSourceKey());
			detailFocus.setValue("SOURCETYPE", obj.getSourceType());
			detailFocus.setValue("BILLEDUNITS", obj.getBilledUnits());
			
			//_log.debug("LOG_SYSTEM_OUT","\n***Owner: " +obj.getStorer() +"\t*Item: " +obj.getItem(),100L);
			//_log.debug("LOG_SYSTEM_OUT","\n***FormName:" +formName,100L);	
		}
		else
		{
			detailFocus.setValue("STORERKEY" , obj.getStorer());
			detailFocus.setValue("SOURCEKEY", obj.getSourceKey());
			detailFocus.setValue("SOURCETYPE", obj.getSourceType());
			detailFocus.setValue("BILLEDUNITS", obj.getBilledUnits());
		}
		_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Leaving setParamsInFocus",100L);	
	}

	private AssignAccessorialDataObject setParams(BioBean bean, String formName, AssignAccessorialDataObject obj) {
		_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","In setParams",100L);
		// TODO Auto-generated method stub
		String storerVal = null;
		String skuVal = null;
		String lotVal = null;
		String lineNumVal = null;
		String keyVal = null;
		String key = null;
		String typeVal= "Accessorial";
		String billedUnits= "0";
		
		if(formName.endsWith("Detail"))
		{
			//set Storer & Item
			if(!formName.equals("Transfer Detail"))
			{
			storerVal= bean.get("STORERKEY").toString();
			skuVal = bean.get("SKU").toString();		
				if(formName.equals("Shipment Order Detail"))
				{
					lineNumVal = bean.get("ORDERLINENUMBER").toString();
					keyVal = bean.get("ORDERKEY").toString();
					typeVal = typeVal+"ORDER";
					billedUnits = bean.get("SHIPPEDQTY").toString();
				}
				else if(formName.equals("Receipt Detail"))
				{
					lineNumVal = bean.get("RECEIPTLINENUMBER").toString();
					keyVal = bean.get("RECEIPTKEY").toString();
					typeVal = typeVal+"RECEIPT";
					billedUnits= bean.get("QTYRECEIVED").toString();
				}
				else if(formName.equals("Adjustment Detail"))
				{
					lineNumVal = bean.get("ADJUSTMENTLINENUMBER").toString();
					keyVal = bean.get("ADJUSTMENTKEY").toString();
					typeVal= typeVal+"ADJUSTMENT";
					lotVal = bean.get("LOT").toString();
					obj.setlot(lotVal);
					billedUnits= bean.get("QTY").toString();
				}
				
			}
			else
			{
				storerVal = bean.get("FROMSTORERKEY").toString();
				skuVal = bean.get("FROMSKU").toString();
				lineNumVal = bean.get("TRANSFERLINENUMBER").toString();
				keyVal = bean.get("TRANSFERKEY").toString();
				typeVal = typeVal+"TRANSFER";
				billedUnits= bean.get("TOQTY").toString();
			}
			
		
			
			key= keyVal+lineNumVal;
			obj.setStorer(storerVal);
			obj.setItem(skuVal);
			obj.setSourceKey(key);
			obj.setSourceType(typeVal);
			obj.setBilledUnits(roundingVal(billedUnits));
			
			}
		else
		{
			if(!formName.equals("Transfer"))
			{
				storerVal = bean.get("STORERKEY").toString();	
				if(formName.equals("Shipment Order"))
				{
				keyVal = bean.get("ORDERKEY").toString();
				typeVal = typeVal+"ORDER";
				}
				else if(formName.equals("Receipt"))
				{
				keyVal = bean.get("RECEIPTKEY").toString();
				typeVal = typeVal+"RECEIPT";
				}
				else if(formName.equals("Adjustment"))
				{
				keyVal = bean.get("ADJUSTMENTKEY").toString();
				typeVal= typeVal+"ADJUSTMENT";
				}
			}
			else
			{
				storerVal = bean.get("FROMSTORERKEY").toString();
				keyVal = bean.get("TRANSFERKEY").toString();
				typeVal = typeVal+"TRANSFER";
			}
			obj.setStorer(storerVal);
			obj.setSourceKey(keyVal);
			obj.setSourceType(typeVal);
			obj.setBilledUnits(billedUnits);
		}
		_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Leaving setParams",100L);
		return obj;
	}

	private String getFormTitle(RuntimeListFormInterface listForm) {
		_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","In getFormTitle",100L);
		// TODO Auto-generated method stub
		final String ADJUSTMENT = "adjustment_list_view";
		final String ADJUSTMENTDETAIL = "adjustment_detail_list_view";
		final String ORDER = "shipmentorders_list_view";
		final String ORDERDETAIL = "shipmentorders_detail_list_view";
		final String RECEIPT = "receipts_list_view";
		final String RECEIPTDETAIL = "receipts_detail_list_view";
		final String TRANSFER = "transfer_list_view";
		final String TRANSFERDETAIL = "transfer_detail_list_view";
		String name= null;
		
		String listName= listForm.getName(); 
		
		if(listName.endsWith(ADJUSTMENT))
		{name= "Adjustment";}
		else if(listName.endsWith(ADJUSTMENTDETAIL))
		{name= "Adjustment Detail";}
		else if(listName.endsWith(ORDER))
		{name= "Shipment Order";}
		else if(listName.endsWith(ORDERDETAIL))
		{name= "Shipment Order Detail";}
		else if(listName.endsWith(RECEIPT))
		{name= "Receipt";}
		else if(listName.endsWith(RECEIPTDETAIL))
		{name= "Receipt Detail";}
		else if(listName.endsWith(TRANSFER))
		{name= "Transfer";}
		else if(listName.endsWith(TRANSFERDETAIL))
		{name= "Transfer Detail";}
		
		_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Leaving getFormTitle",100L);
		return name;
	}
	private String roundingVal(String attribute)
	{
		String roundedNum= "";
		if (attribute != null)
		{
			NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0);
			nf.setMaximumFractionDigits(2);
			
			try
			{
				roundedNum = nf.format(nf.parse(attribute));
			} catch (ParseException e)
			{
				roundedNum = attribute;
			}
		}
return roundedNum;
	
	}	
	
}