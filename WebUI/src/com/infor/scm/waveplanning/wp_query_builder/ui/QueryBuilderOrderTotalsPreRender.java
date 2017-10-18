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
package com.infor.scm.waveplanning.wp_query_builder.ui;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class QueryBuilderOrderTotalsPreRender extends FormExtensionBase{	
	
	public static final String NEW_WAVE_DESC="WAVE_DESCRIPTION";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(QueryBuilderOrderTotalsPreRender.class);
	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {
		
		RuntimeFormWidgetInterface totalCube = form.getFormWidgetByName("TOTALCUBE");
		RuntimeFormWidgetInterface totalLines = form.getFormWidgetByName("TOTALLINES");
		RuntimeFormWidgetInterface totalNetWeight = form.getFormWidgetByName("TOTALNETWEIGHT");
		RuntimeFormWidgetInterface totalOrders = form.getFormWidgetByName("TOTALORDERS");
		RuntimeFormWidgetInterface totalQty = form.getFormWidgetByName("TOTALQTY");
		RuntimeFormWidgetInterface newWaveDesc = form.getFormWidgetByName("WAVEDESC");
		
		
		if(form.getFocus() == null || !form.getFocus().isBioCollection() || ((BioCollection)form.getFocus()).size() == 0){
			totalCube.setDisplayValue("0");
			totalLines.setDisplayValue("0");
			totalNetWeight.setDisplayValue("0");
			totalOrders.setDisplayValue("0");
			totalQty.setDisplayValue("0");
			return RET_CONTINUE;
		}
		
		BioCollectionBean focus = (BioCollectionBean)form.getFocus();
				
		Object totalCubeObj =  focus.sum("TOTALCUBE");
		Object totalNetWeightObj =  focus.sum("TOTALGROSSWGT");
		Object totalQtyObj =  focus.sum("TOTALQTY");
		
		totalCube.setDisplayValue(totalCubeObj == null?"0":totalCubeObj.toString());
		totalNetWeight.setDisplayValue(totalNetWeightObj == null?"0":totalNetWeightObj.toString());
		totalQty.setDisplayValue(totalQtyObj == null?"0":totalQtyObj.toString());		
		totalOrders.setDisplayValue(""+focus.size());	
		
		//get new wave description
//		StateInterface state = context.getState();
//		String newWaveDescription = state.getRequest().getSession().getAttribute(NEW_WAVE_DESC)==null?""
//				:state.getRequest().getSession().getAttribute(NEW_WAVE_DESC).toString();
//		newWaveDesc.setDisplayValue(newWaveDescription);
		
		//fix bug 504 to comment out 
//		newWaveDesc.setDisplayValue(" "); fix bug 
//		state.getRequest().getSession().removeAttribute(NEW_WAVE_DESC);
		
		int lineCount = 0;
		for(int i = 0; i < focus.size(); i++){
			Bio order = focus.elementAt(i); 
			BioCollection detailLines = order.getBioCollection("ORDERLINECOLLECTION");
			if(detailLines != null)
				lineCount += detailLines.size();
		}
		totalLines.setDisplayValue(""+lineCount);	
		
		return RET_CONTINUE;
	}
	
	
}