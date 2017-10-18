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
package com.ssaglobal.scm.wms.wm_shipmentorder.ui;

//Import 3rd party packages and classes
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeListRowInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;

/**
 * 
 * Modification History
 * 01/15/2009	AW SDIS: SCM-00000-05544 Machine:2115527
 * 				   While deleting the last line from a shipment order, 
 * 				   an exception was being thrown.	
 * 04/14/2009	AW  
 * 				Machine#:2093019 
 * 				SDIS:SCM-00000-05440
 * 04/23/2009	AW		SDIS:SCM-00000-06871 Machine:2353530
 *						Changed code to allow qty values for the Currency Locale
 *						something other than dollar.
 * 05/19/2009   AW      SDIS:SCM-00000-06871 Machine:2353530
 * 		     			UOM conversion is now done in the front end.
 *                      Changes were made accordingly.
 * 07/15/09		MA		SDIS:SCM-00000-07461. pick detail line# look up now has no exception when oder has line with ship completed lines.
 * 03/22/2009	AW		Infor365:217417
 * 						In certain locales, the precision was defaulting to 3
 */

public class ShipmentOrderModifyListValues extends FormExtensionBase{
	private final static String STATUS = "STATUS";
	private final static String IS_SUB = "ISSUBSTITUTE";
	private final static String QTY = "OPENQTY";
	
	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) throws EpiException {
		BioCollectionBean listRows = (BioCollectionBean) form.getFocus();
		
		if(listRows.size() > 0){ //AW 09/15/2008 SDIS: SCM-00000-05544 Machine:2115527
		String orderkey = (String)listRows.get("0").getValue("ORDERKEY");
		StateInterface state = context.getState();
		//SCM-00000-07461******
		Query query = new Query("wm_orderdetail", "wm_orderdetail.ORDERKEY='"+ orderkey+"'", null);
		//END *****************
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		int numOrderDetails = uowb.getBioCollectionBean(query).size();
		
		
		for (int i = 0; i < numOrderDetails; i++){
			//		Adjust open qty for UOM
//			String uom = listRows.get(String.valueOf(i)).getValue("UOM",true) == null ? 
//					listRows.get(String.valueOf(i)).getString("UOM") : listRows.get(String.valueOf(i)).getValue("UOM",true).toString();
			String uom = listRows.get(String.valueOf(i)).getValue("UOM",true).toString();
			Object packVal = listRows.get(String.valueOf(i)).getValue("PACKKEY", true);//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
			if(packVal != null){//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
				String uom3 = UOMMappingUtil.getPACKUOM3Val(packVal.toString(), uowb);//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
				if(!uom.equals(uom3)){//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
				//get values for changes
				String pack = packVal.toString();//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
				String qty =  listRows.get(String.valueOf(i)).getValue("OPENQTY", true).toString();
				Bio packBio = (Bio) listRows.get(String.valueOf(i)).getValue("PACKBIO");
				//change values
				listRows.get(String.valueOf(i)).setValue("OPENQTY", LocaleUtil.checkLocaleAndParseQty(nonZero(uom, qty, pack, packBio), LocaleUtil.TYPE_QTY));//AW Infor365:217417 03/22/10
			}
			}//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
		}
		} //AW 09/15/2008 SDIS: SCM-00000-05544 Machine:2115527
		return RET_CONTINUE;
	}
	
	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form) throws UserException{
		RuntimeListRowInterface[] listRows = form.getRuntimeListRows();
		if(listRows.length > 0){ //AW 09/15/2008 SDIS: SCM-00000-05544 Machine:2115527
		for(int i = 0; i < listRows.length; i++){
			//moved logic to a prerender extension based on feedback from san mateo
//			//Adjust open qty for UOM
//			String uom = listRows[i].getFormWidgetByName("UOM").getDisplayValue();
//			if(!uom.equals("EA")){
//				//get values for changes
//				String pack = listRows[i].getFormWidgetByName("PACKKEY").getDisplayValue();
//				String qty =  listRows[i].getFormWidgetByName("OPENQTY").getDisplayValue();
//				
//				//change values
//				listRows[i].getFormWidgetByName("OPENQTY").setDisplayValue(nonZero(uom, qty, pack));
//				_log.debug("LOG_SYSTEM_OUT","Using setValue",100L);
//			}
			
			//Enable disable open qty
			RuntimeFormWidgetInterface status = listRows[i].getFormWidgetByName(STATUS); // 9 
			RuntimeFormWidgetInterface isSubstitute = listRows[i].getFormWidgetByName(IS_SUB); // 9
			RuntimeFormWidgetInterface quantity = listRows[i].getFormWidgetByName(QTY); // 9

			//Get Values
			String statusValue = status.getDisplayValue();
			String isSubstituteValue = isSubstitute.getDisplayValue();

			if(!(isSubstituteValue.equals("0")) || statusValue.equals("Shipped Complete")){
				quantity.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
			}else{
				quantity.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
			}
			
		}
		}//AW 09/15/2008 SDIS: SCM-00000-05544 Machine:2115527
		return RET_CONTINUE;
	}
	/**
	 * Description: Method to skip stored procedure when applicable
	 * Modification History
	 * 04/14/2009	AW  Machine#:2093019 SDIS:SCM-00000-05440
     * 05/19/2009   AW      SDIS:SCM-00000-06871 Machine:2353530
	 * 						UOM conversion is now done in the front end.
	 *                      Changes were made accordingly. 
	 */
	//Method to skip stored procedure when applicable
	public String nonZero(String uom, String qty, String pack, Bio packBio) throws UserException{
		double temp = Double.parseDouble(commaStrip(qty));
		if(temp!=0.0){
			return UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA, uom, commaStrip(qty), pack, packBio); //05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
		}else{
			return "0.00000";
		}
	}
	
	private String commaStrip(String number){
		NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW
		String numberS;
		try{
			numberS = nf.parse(number).toString();
		} catch(ParseException e){
			e.printStackTrace();
			Pattern pattern = Pattern.compile("\\,");
			Matcher matcher = pattern.matcher(number);
			return matcher.replaceAll("");
		}
		return numberS;
	}
	
}