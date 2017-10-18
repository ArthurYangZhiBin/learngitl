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

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
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
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationInsertImpl;


public class SetFocusForCharges extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(SetFocusForCharges.class);
	protected static final String internalCaller = "internalCall";
	
	  protected int execute(ActionContext context, ActionResult result)
      throws UserException
  {
	
    _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Executing SetFocusForCharges",100L);
	QBEBioBean tempBio;
	BioCollectionBean newFocus= null;
	//AccessorialDetailObject obj = new AccessorialDetailObject();
	
	StateInterface state = context.getState();		 
	RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
	RuntimeFormInterface form = toolbar.getParentForm(state);
	RuntimeFormInterface shellForm = (form.getParentForm(state)).getParentForm(state);
	SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");		//HC
	RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
	RuntimeListFormInterface hdrlistForm = (RuntimeListFormInterface) headerForm;
	
		
	//creating temp bio for new accessorial charges rec.
	UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
	try {
		tempBio = uow.getQBEBioWithDefaults("wm_accessorial_charges");
	} catch (DataBeanException e) {
		e.printStackTrace();
		throw new UserException("ERROR_CREATE_TMP_BIO", new String[] { "wm_accessorial_charges" });
	}

//	get and set values from accessorialdetail
	if(form.isListForm())
	{
		RuntimeListFormInterface listForm = (RuntimeListFormInterface) form;
		DataBean listFocus = listForm.getFocus();
		if (listFocus instanceof BioCollection){			
			ArrayList itemsSelected = listForm.getAllSelectedItems();
			if(itemsSelected != null && itemsSelected.size() >0)
			{
				//set values
				BioBean bean= null;
							
				///////////////////////////////////////////////////////////////////////
				//////////////////////////////////////////////////////////////////////
				//functionality changes new code
				 
				_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","***Records Returned: " +itemsSelected.size(),100L);
	
				DataBean hdrListFocus = hdrlistForm.getFocus();
				if (hdrListFocus instanceof BioCollection){
					String formName= getFormTitle(hdrlistForm);
					///////////////////////////////////////////
					String attStr="";
					String valStr="";
					String baseAtt="INSERT INTO accumulatedcharges (";
					String baseVal="VALUES (";
					String insertQry= "";
					String focusQry= "";
					String lineType= "";
					
					ArrayList hdrItemsSelected = hdrlistForm.getAllSelectedItems();
					if(hdrItemsSelected != null && hdrItemsSelected.size() ==1)
					{
						 _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Items have been selected",100L);
						Iterator hdrBioBeanIter = hdrItemsSelected.iterator();
						try{
							BioBean hdrBean= null;
								for(; hdrBioBeanIter.hasNext(); )
								{
									attStr="";
									valStr="";
									
									hdrBean = (BioBean)hdrBioBeanIter.next();
									AssignAccessorialDataObject obj = new AssignAccessorialDataObject();
									obj= setParams(hdrBean, formName, obj);
									focusQry = getQuery(hdrlistForm, hdrBean);
								
								   _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Query: " +focusQry,100L);
								    context.getServiceManager().getUserContext().put("QUERY", focusQry);
								    Query bioQry = new Query("wm_accessorial_charges", focusQry, null);
								    newFocus = uow.getBioCollectionBean(bioQry);
									
								    context.getServiceManager().getUserContext().put("QUERY", focusQry);

									  if(formName.endsWith("Detail"))
										{
											//set Storer & Item
											if(formName.equals("Adjustment Detail"))
											{
												//_log.debug("LOG_SYSTEM_OUT","\n\nLOT: " +obj.getlot(),100L);
												baseAtt = baseAtt + "LOT, ";
												baseVal = baseVal +  "'" +obj.getlot() +"', ";
											}		
											
											//_log.debug("LOG_SYSTEM_OUT","\n\n STORERKEY: " +obj.getStorer(),100L);
											baseAtt = baseAtt + "STORERKEY, ";
											baseVal = baseVal +  "'" +obj.getStorer() +"', ";
												
											//_log.debug("LOG_SYSTEM_OUT","\n\nSKU: " +obj.getItem(),100L);
											baseAtt = baseAtt + "SKU, ";
											baseVal = baseVal +  "'" +obj.getItem() +"', ";
											
											//_log.debug("LOG_SYSTEM_OUT","\n\nSOURCEKEY: " +obj.getSourceKey(),100L);
											baseAtt = baseAtt + "SOURCEKEY, ";
											baseVal = baseVal +  "'" +obj.getSourceKey() +"', ";
											
											//_log.debug("LOG_SYSTEM_OUT","\n\nSOURCETYPE: " +obj.getSourceType(),100L);
											baseAtt = baseAtt + "SOURCETYPE, ";
											baseVal = baseVal +  "'" +obj.getSourceType() +"', ";
											
											//_log.debug("LOG_SYSTEM_OUT","\n***Owner: " +obj.getStorer() +"\t*Item: " +obj.getItem(),100L);
											//_log.debug("LOG_SYSTEM_OUT","\n***FormName:" +formName,100L);	
										}
										else
										{
										
											//_log.debug("LOG_SYSTEM_OUT","\n\nSTORERKEY: " +obj.getStorer(),100L);
											baseAtt = baseAtt + "STORERKEY, ";
											baseVal = baseVal +  "'" +obj.getStorer() +"', ";

											//_log.debug("LOG_SYSTEM_OUT","\n\nSOURCEKEY: " +obj.getSourceKey(),100L);
											baseAtt = baseAtt + "SOURCEKEY, ";
											baseVal = baseVal +  "'" +obj.getSourceKey() +"', ";

											//_log.debug("LOG_SYSTEM_OUT","\n\nSOURCETYPE: " +obj.getSourceType(),100L);
											baseAtt = baseAtt + "SOURCETYPE, ";
											baseVal = baseVal +  "'" +obj.getSourceType() +"', ";

										}
									  baseAtt = baseAtt + "CHARGETYPE, ";
									  baseVal= baseVal + "'AC', ";
									  
									  baseAtt = baseAtt + "BILLEDUNITS, ";
									  baseVal= baseVal + obj.getBilledUnits() + ", ";

									 
									  //_log.debug("LOG_SYSTEM_OUT","\n\n\n******baseAtt: " +baseAtt,100L);
									  //_log.debug("LOG_SYSTEM_OUT","\n\n\n******baseVal: " +baseVal,100L);
									
									
									
									//getStorerAndSku(formName, obj);
									//detailFocus.setValue("CHARGETYPE", "AC");
									
									
									/////////////////////////detail accessorials
									
									try {
									Iterator bioBeanIter = itemsSelected.iterator();
									int count =0;
									
										BioBean bio;				
										for(; bioBeanIter.hasNext();){
											
											
											bio = (BioBean)bioBeanIter.next();
									     		
											
											
											
											//_log.debug("LOG_SYSTEM_OUT","\n\n\n******BILLEDUNITS******: " +obj.getBilledUnits(),100L);
											 
											String newKey= getKeyVal();
											String line= getLineType(hdrlistForm, obj.getSourceKey(), newKey,  uow, count);
											
											
											
											Object base= bio.get("BASE");
											Object descr= bio.getValue("DESCRIP");
											Object glDist= bio.get("GLDISTRIBUTIONKEY");
											Object units= bio.get("MASTERUNITS");
											Object taxGroup= bio.get("TAXGROUPKEY");
											Object rate= bio.get("RATE");
											Object accDetKey = bio.get("ACCESSORIALDETAILKEY");

											
											attStr= "LINETYPE, ";
											valStr= "'" +line +"', ";
											
											attStr= attStr + "ACCUMULATEDCHARGESKEY, ";
											valStr = valStr + "'" +newKey +"', ";
											
											if(!isNull(base))
											{	
											attStr= attStr + "BASE, ";
											valStr= valStr + "'" +base.toString() +"', ";
											}
											if(!isNull(descr)){												
												attStr= attStr + "ACCESSORIALDETAILKEY, ";
												valStr= valStr + "'" +accDetKey.toString() +"', ";
											}
											if(!isNull(glDist))
											{								
												attStr= attStr + "GLDISTRIBUTIONKEY, ";
												valStr= valStr + "'" +glDist.toString() +"', ";
											}
											if(!isNull(units)){											
												attStr= attStr + "MASTERUNITS, ";
												valStr= valStr +units.toString() +", ";
											}
											if(!isNull(taxGroup)){												
												attStr= attStr + "TAXGROUPKEY, ";
												valStr= valStr + "'" +taxGroup.toString() +"', ";
											}
											if(!isNull(rate)){											
												attStr= attStr + "RATE, ";
												valStr= valStr  +rate.toString() +", ";	
												
												attStr= attStr + "DEBIT) ";
												valStr = valStr + calcDebit(obj.getBilledUnits(), rate.toString()) +") ";
											}
											/*
											if(!isNull(accDetKey)){
												attStr= attStr + "ACCESSORIALDETAILKEY)";
												valStr= valStr + "'" +accDetKey.toString() +"') ";
												_log.debug("LOG_SYSTEM_OUT","\n\n***ACCESSORIALDETAILKEY: " +accDetKey.toString(),100L);
											}
											*/
											
															
											 _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Base:" +base.toString(),100L);
											 _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Acc:" +accDetKey.toString(),100L);
											 _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","GL Dist:" +glDist.toString(),100L);
											 _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Masterunits:" +units.toString(),100L);
											 _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Tax Group:" +taxGroup.toString(),100L);
											 _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Rate:" +rate.toString(),100L);
											 //context.getServiceManager().getUserContext().put("NEWACC", obj);
											 
											
											
											insertQry= baseAtt + attStr +baseVal + valStr;
											
											//_log.debug("LOG_SYSTEM_OUT","\n***INSERT: " +insertQry,100L);
											
											 if (insertQry != null && insertQry != "")
											 {
											 WmsWebuiValidationInsertImpl.insert(insertQry.toString());
											 }
											
											count++;											
											}
											
												
										} catch (EpiDataException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
										}									
									
								}
						}
						catch(RuntimeException e)
						{
							e.printStackTrace();}	
					}
					else if(hdrItemsSelected ==null)
						{
							String[] param = new String[1];
							param[0] = formName;
							throw new FormException("WMEXP_NOT_SELECTED",param);
						}
						else if(hdrItemsSelected.size() > 1)
						{
							String[] param = new String[1];
							param[0] = formName;
							throw new FormException("WMEXP_TOO_MANY_ITEMS",  param);
						}
				
				
			}		
				
	
				
			}
			else if(itemsSelected ==null)
			{
				throw new FormException("WMEXP_SELECT_POTENTIAL",new Object[0]);
			}
		
		}//
	}
	 result.setFocus(newFocus);	
	_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Exiting SetFocusForCharges",100L);
	return RET_CONTINUE;
  }
	  


	private String getLineType(RuntimeListFormInterface form, String srcKey, String accKey, UnitOfWorkBean uow, int ct) throws UserException{
		// TODO Auto-generated method stub
		

		String lType= ""; 
		if(form.getName().equals("wm_assign_accessorial_charges_adjustment_list_view") || form.getName().equals("wm_assign_accessorial_charges_adjustment_detail_list_view"))
		{
			try
			{
			String key = accKey;
			String adKey = srcKey;
			String query = "wm_accessorial_charges.SOURCETYPE='AccessorialADJUSTMENT' and wm_accessorial_charges.SOURCEKEY='"+adKey +"'";
			
			_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Query: " +query,100L);
			Query qry = new Query("wm_accessorial_charges", query, null);
			BioCollectionBean newFocus = uow.getBioCollectionBean(qry);
			
			try {
				 if(newFocus.size()<1 && ct==0){
					lType=  "NA";
					_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","New rec NA Linetype",100L);
				}
				else 
				{	
						_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","New rec TA Linetype",100L);
					lType= "TA";

				}
			} catch (EpiDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			catch(NullPointerException e)
			{
				String[] param = new String[1];
				param[0] = form.getName();
				throw new UserException("WMEXP_NOT_SELECTED", param);
			}
			
		}
		else
		{
			lType= "N";
		}
			
	return lType;
	}



	private String calcDebit(String units, String rate) {
		// TODO Auto-generated method stub
		
		double debitVal;
		debitVal= Double.parseDouble(units) * Double.parseDouble(rate);
		
		return Double.toString(debitVal);
	}



	private String getKeyVal() {
		// TODO Auto-generated method stub
 			int rowCount = 0;
   			TextData myKey = null;
   			Array argArray = new Array();
   			EXEDataObject edo =null;
   			argArray.add(new TextData("ACCUMULATEDCHARGES"));
   			argArray.add(new TextData("10"));
   			argArray.add(new TextData("1"));
   			//   		Added following code to get the db_connection information from the usre context

   			EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
//			String locale = userContext.getLocale();
   			String locale = getBaseLocale(userContext);
   			String db_connection = userContext.get(SetIntoHttpSessionAction.DB_CONNECTION).toString();
   			String wmWhseID = (userContext.get(SetIntoHttpSessionAction.DB_USERID)).toString();

  			try {
   				TransactionServiceSORemote remote = EJBRemote.getRemote();
   				edo = remote.executeProcedure(new TextData(wmWhseID),db_connection,new TextData("GETKEYP1S1"),argArray,null,internalCaller,locale);		//HC
   				rowCount = edo.getRowCount();
   				if (edo._rowsReturned())
   				{
   				  myKey = edo.getAttribValue(new TextData("keyname")).asTextData();
   				}

   			} catch (SsaException x) {
   				_log.error("EXP_1","Could not get the remote...",SuggestedCategory.NONE);
   				_log.error(new EpiException("EXP_1", "SsaException nested in EpiException...", x.getCause()));
            
   			} catch (Exception exc) {
            	exc.printStackTrace();
            	EpiException x = new EpiException("EXP_1", "Unknown", exc);
            	_log.error(x);
            }
   			
   			_log.debug("LOG_SYSTEM_OUT","\n\n********New key: " +myKey.toString(),100L);
   			return myKey.toString();
	}
	  	//If the Locale does not have the country code attached to the language code then assign the default country for that language.
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
		
		private AssignAccessorialDataObject setParams(BioBean bean, String formName, AssignAccessorialDataObject obj) {
			_log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","In setParams",100L);
			// TODO Auto-generated method stub
			String billedUnits= "0";
			String storerVal = null;
			String skuVal = null;
			String lotVal = null;
			String lineNumVal = null;
			String keyVal = null;
			String key = null;
			String typeVal= "Accessorial";
			
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
						billedUnits = bean.get("SHIPPEDQTY").toString();
						typeVal = typeVal+"ORDER";
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
		
		
		
		
		private String getQuery(RuntimeFormInterface currentList, BioBean bioBean) {
			 _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Executing getQuery()",100L);	
			// TODO Auto-generated method stub
			
			final String ADJUSTMENT = "adjustment_list_view";
			final String ORDER = "shipmentorders_list_view";
			final String RECEIPT = "receipts_list_view";
			final String TRANSFER = "transfer_list_view";
			
			final String ADJUSTMENTDETAIL = "adjustment_detail_list_view";			
			final String ORDERDETAIL = "shipmentorders_detail_list_view";			
			final String RECEIPTDETAIL = "receipts_detail_list_view";
			final String TRANSFERDETAIL = "transfer_detail_list_view";
			
			String formName= currentList.getName();
			String query = null;
			String key = null;
			String keyName = null;
			String typeVal= "Accessorial";
			String line= null;
			String lineVal= null;
			String billedUnits= null;
			int detailFlag=0;
			
		
			
			if(formName.endsWith(ADJUSTMENT) )
			{
				typeVal= typeVal+"ADJUSTMENT";
				keyName = "ADJUSTMENTKEY";				       
			}
			else if(formName.endsWith(ORDER))
			{
				keyName = "ORDERKEY";
				typeVal= typeVal+"ORDER";
			}
			else if(formName.endsWith(RECEIPT))
			{
				keyName = "RECEIPTKEY";
				typeVal= typeVal+"RECEIPT";
			}
			else if(formName.endsWith(TRANSFER))
			{
				keyName = "TRANSFERKEY";
				typeVal = typeVal+"TRANSFER";
			}
			else if(formName.endsWith(ADJUSTMENTDETAIL))
			{
				typeVal= typeVal+"ADJUSTMENT";
				keyName = "ADJUSTMENTKEY";
				line = "ADJUSTMENTLINENUMBER";
				detailFlag=1;
			}
			else if(formName.endsWith(ORDERDETAIL))
			{
				keyName = "ORDERKEY";
				typeVal= typeVal+"ORDER";
				line = "ORDERLINENUMBER";
				detailFlag=1;
			}
			else if(formName.endsWith(RECEIPTDETAIL))
			{
				keyName = "RECEIPTKEY";
				typeVal= typeVal+"RECEIPT";
				line = "RECEIPTLINENUMBER";
				detailFlag=1;
			}
			else if(formName.endsWith(TRANSFERDETAIL))
			{
				keyName = "TRANSFERKEY";
				typeVal = typeVal+"TRANSFER";
				line = "TRANSFERLINENUMBER";
				detailFlag=1;
			}

			
			
			
			
			
			key = bioBean.getValue(keyName).toString();
			if(detailFlag==0)
			{query= "wm_accessorial_charges.SOURCETYPE='" +typeVal+"' and " +"wm_accessorial_charges.SOURCEKEY ~=" +" '" +key +"%'";
			}
			else
			{
				lineVal= bioBean.getValue(line).toString();
				key= key+lineVal;
				query= "wm_accessorial_charges.SOURCETYPE='" +typeVal+"' and " +"wm_accessorial_charges.SOURCEKEY =" +" '" +key +"'";
			}
			
			
	        _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Query: " +query,100L);
	    	
	        _log.debug("LOG_DEBUG_EXTENSION_ASSIGN_ACC_CHARGES","Leaving getQuery()",100L);
			return query;
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
	private boolean isNull(Object widget) {
			// TODO Auto-generated method stub  
			boolean value;
			if(widget == null)			
				value=true;				
			else if(widget.toString().matches("\\s*"))
				value=true;
			else
				value=false;
			
			
			return value;
		}
}
