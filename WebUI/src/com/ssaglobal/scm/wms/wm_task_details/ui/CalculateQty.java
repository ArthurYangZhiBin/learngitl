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
package com.ssaglobal.scm.wms.wm_task_details.ui;


import java.util.HashMap;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;
import com.ssaglobal.scm.wms.wm_cyclecount.ui.CalculateAdvCatchWeightsHelper;
import com.ssaglobal.scm.wms.wm_table_validation.ccf.NumericValidationCCF;

public class CalculateQty extends com.epiphany.shr.ui.action.ActionExtensionBase{
	private static String SKU = "SKU";
	private static String Owner = "STORERKEY";
	private static String qtyBioWidget = "QTY";
	private static String qtyUOMBioWidget = "UOMQTY";
	private static String uomWidget = "UOM";
	private static String pack = "PACKKEY";
	
	private static String skuBio = "wm_sku";


	private static String[] errorParam = new String[2];
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CalculateQty.class);
	

   /**
    * The code within the execute method will be run from a UIAction specified in metadata.
    * <P>
    * @param context The ActionContext for this extension
    * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
    *
    * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
    *
    * @exception EpiException
    */
   protected int execute( ActionContext context, ActionResult result ) throws EpiException {
	   
	   String skuVal = null;
	   String ownerVal = null;
	   String packVal= null;
	   String uomInput = null;
	   String qty = null;
	   
	   DataBean headerBean = context.getState().getCurrentRuntimeForm().getFocus();
	   UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
	   RuntimeFormInterface currentForm = context.getState().getCurrentRuntimeForm();
	   
	   if(context.getSourceWidget().getName().equals(qtyUOMBioWidget)){
		  qty = context.getSourceWidget().getValue().toString();
		   if(headerBean.getValue(uomWidget) !=null){
			   uomInput =headerBean.getValue(uomWidget).toString();
			   }
		   else
			   return RET_CANCEL;
	   }
	   else if(context.getSourceWidget().getName().equals(uomWidget)){
			  qty = currentForm.getFormWidgetByName(qtyUOMBioWidget).getDisplayValue();
			  uomInput = context.getSourceWidget().getValue().toString();
	   }
	   		 
		   
		   ownerVal = currentForm.getFormWidgetByName(Owner).getDisplayValue();
		   skuVal = currentForm.getFormWidgetByName(SKU).getDisplayValue();
			
		   packVal = getPack(ownerVal, skuVal, uow);
		   
		   
		   if(uomInput !=null && !uomInput.equals(""))
		   {
			   //String convQty = convertFromUOMQty(uomInput,qty, packVal);
			   String convQty = UOMMappingUtil.convertUOMQty(uomInput, UOMMappingUtil.UOM_EA, qty, packVal, context.getState(), UOMMappingUtil.uowNull, true);
			   //headerBean.setValue(qtyBioWidget, convQty);


			   //Krishna Kuchipudi: Dec-03-2009: 3PL Enhancements -Advanced Catch Weight tracking - starts
			   String enableAdvCatchWeight=null;
			   	double ToGROSSWT1 = 0;
				double ToNETWT1 = 0;
				double ToTAREWT1 = 0;
				 String TaskType = "";
			   //String TASKDETAILKEY = uow.getValue("TASKDETAILKEY").toString();
			//	 TaskType = uow.getValue("TASKTYPE").toString();

		//  if(TaskType.equalsIgnoreCase("PP") ){
			   CalculateAdvCatchWeightsHelper CalculateAdvCatchWeightsHelper1 = new CalculateAdvCatchWeightsHelper();
				enableAdvCatchWeight = CalculateAdvCatchWeightsHelper1.isAdvCatchWeightEnabled(ownerVal,skuVal);
				if(enableAdvCatchWeight.equalsIgnoreCase("1")){

					String FromLoc  = currentForm.getFormWidgetByName("FROMLOC").getDisplayValue();
					 String LOT = currentForm.getFormWidgetByName("LOT").getDisplayValue();
					  if(FromLoc !=null && !FromLoc.equalsIgnoreCase("") && LOT !=null && !LOT.equalsIgnoreCase("")){
						  //FromLoc is filled
						  String FromID = currentForm.getFormWidgetByName("FROMID").getDisplayValue();
						  Double qty1 = Double.valueOf(convQty);
						  HashMap wgts = new HashMap();
						  wgts = CalculateAdvCatchWeightsHelper1.getCalculatedWeightsLPN(ownerVal, skuVal, FromLoc, LOT, FromID, qty1);
						  ToGROSSWT1= (Double)wgts.get("GROSSWEIGHT");
						  ToNETWT1= (Double)wgts.get("NETWEIGHT");
						  ToTAREWT1= (Double)wgts.get("TAREWEIGHT");
					  }

				}
		//	   }
				//Krishna Kuchipudi: Dec-03-2009: 3PL Enhancements -Advanced Catch Weight tracking - Ends
			   if(headerBean instanceof QBEBioBean){
				   QBEBioBean qbeheader = (QBEBioBean)headerBean;
				   qbeheader.setValue(qtyBioWidget, convQty);
				   //Krishna Kuchipudi: Dec-03-2009: 3PL Enhancements -Advanced Catch Weight tracking -Starts
				   qbeheader.setValue("GROSSWGT", Double.toString(ToGROSSWT1));
				   qbeheader.setValue("NETWGT", Double.toString(ToNETWT1));
				   qbeheader.setValue("TAREWGT", Double.toString(ToTAREWT1));
		   			//Krishna Kuchipudi: Dec-03-2009: 3PL Enhancements - Advanced Catch Weight tracking -Ends
		   		}else if(headerBean instanceof BioBean){
		   			BioBean bioheader = (BioBean)headerBean;
		   			bioheader.setValue(qtyBioWidget, convQty);
		   			//Krishna Kuchipudi: Dec-03-2009: 3PL Enhancements - Advanced Catch Weight tracking -Starts
		   			bioheader.setValue("GROSSWGT", Double.toString(ToGROSSWT1));
		   			bioheader.setValue("NETWGT", Double.toString(ToNETWT1));
		   			bioheader.setValue("TAREWGT", Double.toString(ToTAREWT1));
		   			//Krishna Kuchipudi: Dec-03-2009: 3PL Enhancements -Advanced Catch Weight tracking -Ends
		   		}
		   }
		   else
		   {
			   errorParam[0] = skuVal;
			   throw new UserException("WMEXP_INVALID_UOM", errorParam);
		   }  
		   currentForm.getFormWidgetByName("PACKKEY").setDisplayValue(packVal);
      return super.execute( context, result );
   }
   


private String getPack(String ownerVal, String skuVal, UnitOfWorkBean uow) throws EpiException {
	   String queryStatement = null;
	   Query query = null;
	   BioCollection results = null;
	   String packVal = null;
		try
		{
		queryStatement = skuBio + ".STORERKEY = '" + ownerVal +"'" + 
						 " AND " +skuBio +"." +SKU +"='" +skuVal +"'";
		query = new Query(skuBio, queryStatement, null);
		results = uow.getBioCollectionBean(query);
		}catch (Exception e)
		{
		e.printStackTrace();
		throw new EpiException("QUERY_ERROR", "Error preparing search query" + queryStatement);
		}
		if(results.size() == 1)
		{
			   Bio resultBio = results.elementAt(0);
			   packVal = resultBio.get(pack).toString();

		}
		else
		{
			//Error msg Invalid Storer/SKU combination
			errorParam[0]= ownerVal;
			errorParam[1]= skuVal;
			throw new UserException("WMEXP_INVALID_OWNER_ITEM_COMB", errorParam);
		}

	   
	   
	   
	return packVal;
}

/**
    * Fires in response to a UI action event, such as when a widget is clicked or
    * a value entered in a form in a modal dialog
    * Write code here if u want this to be called when the UI Action event is fired from a modal window
    * <ul>
    * <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext} exposes information about the
    * event, including the service and the user interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
    * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes information about the results
    * of the action that has occurred, and enables your extension to modify them.</li>
    * </ul>
    */
    protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {

       try {
           // Add your code here to process the event
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }

}
