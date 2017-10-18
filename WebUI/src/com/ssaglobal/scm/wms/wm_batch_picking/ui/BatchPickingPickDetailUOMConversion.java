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
package com.ssaglobal.scm.wms.wm_batch_picking.ui;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.state.StateInterface;//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeListRowInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;

public class BatchPickingPickDetailUOMConversion extends FormExtensionBase{

	
	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) throws EpiException
	{
		BioCollectionBean listRows = (BioCollectionBean) form.getFocus();
		for (int i = 0; i < listRows.size(); i++){		
			String uom = listRows.get(String.valueOf(i)).getValue("UOM",true).toString();
			Object packVal = (listRows.get(String.valueOf(i)));//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
			if(packVal !=null){//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
				String pack = listRows.get(String.valueOf(i)).getString("PACKKEY");//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
				String uom3 = UOMMappingUtil.getPACKUOM3Val(pack, context.getState().getDefaultUnitOfWork());//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440									
			if(!uom.equals(uom3)){//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440

				String qty =  listRows.get(String.valueOf(i)).getValue("QTY", true).toString();		
				//change values
				listRows.get(String.valueOf(i)).setValue("QTY", (nonZero(uom, qty, pack, context.getState())));//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
			}
			}//AW 04/14/2009 Machine#:2093019 SDIS:SCM-00000-05440
		}
		
		return RET_CONTINUE;
	}
		
	//Method to skip stored procedure when applicable
	public String nonZero(String uom, String qty, String pack, StateInterface state) throws UserException{
		double temp = Double.parseDouble(commaStrip(qty));
		if(temp!=0.0){
			return UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA, uom, commaStrip(qty), pack, state,UOMMappingUtil.uowNull, true); //05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
		}else{
			return "0.00000";
		}
	}
	
	private String commaStrip(String number){
		NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW
		String numberS;
		try
		{
			numberS = nf.parse(number).toString();
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Pattern pattern = Pattern.compile("\\,");
			Matcher matcher = pattern.matcher(number);
			return matcher.replaceAll("");
		}
		return numberS;
	}
	
}