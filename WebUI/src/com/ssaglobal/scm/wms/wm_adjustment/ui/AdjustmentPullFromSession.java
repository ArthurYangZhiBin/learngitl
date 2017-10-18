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
package com.ssaglobal.scm.wms.wm_adjustment.ui;

//Import 3rd party packages and classes
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpSession;

//Import Epiphany packages and classes
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.wm_cyclecount.ui.CalculateAdvCatchWeightsHelper;

public class AdjustmentPullFromSession extends ActionExtensionBase{
	private final static String BIOREF = "SELECTED_BIO_REF";
	private final static String ITEM = "SKU";
	private final static String LPN = "ID";
	private final static String LOCATION = "LOC";
	private final static String LOT = "LOT";
	private final static String QTY = "QTY";
	private final static String QTYALLOC = "QTYALLOCATED";
	private static final String KEY = "ORDERKEY";
	private static final String LINE_NO = "ORDERLINENUMBER";
	private static final String ORIGINAL_QTY = "ORIGINALQTY";
	private static final String OPEN_QTY = "OPENQTY"; //jp.answerlink.149400
	private final static String ERROR_MESSAGE_LOST_BIOREF = "WMEXP_LOST_BIOREF";
	private final static String SOURCE = "SOURCE";
	private final static String TOGGLE = "ToggleQty";
	
	protected int execute(ActionContext context, ActionResult result) throws FormException, EpiDataException{
		//Initialize local variables
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		QBEBioBean qbe = (QBEBioBean)state.getCurrentRuntimeForm().getFocus();
		HttpSession session = state.getRequest().getSession();
		String source =(String)session.getAttribute(SOURCE);
		
		
		if (source.compareToIgnoreCase("wm_adjustment_lotxlocxid_lookup_list_view")==0){

			//Pull bioRef from session
			BioRef selectionRef = (BioRef)session.getAttribute(BIOREF);
			BioBean bio = null;
			
			//Get associated bio
			try{
				bio = uowb.getBioBean(selectionRef);
			}catch(Exception e){
				throw new FormException(ERROR_MESSAGE_LOST_BIOREF, null);
			}
			
			qbe.set(ITEM, bio.get(ITEM));
			qbe.set(LPN, bio.get(LPN));
			qbe.set(LOCATION, bio.get(LOCATION));
			qbe.set(LOT, bio.get(LOT));
			
			//SM 11-28-07 SCM-00000-03677: Auto Population of Qty field
			boolean toggleQty = getParameterBoolean(TOGGLE);
			if(toggleQty){
				double existingQty = Double.parseDouble(commaStrip(qbe.get(QTY).toString()));
				if(existingQty == 0){
					String key = qbe.get(KEY).toString();
					String lineNo = qbe.get(LINE_NO).toString();
					double tempQty = 0.0;
					double pdAvailableQty = Double.parseDouble(commaStrip(bio.get(QTY).toString())) - Double.parseDouble(commaStrip(bio.get(QTYALLOC).toString()));
					//jp.answerlink.213796.begin
					//double remainingUnpickedQty = getOrigQty(key, lineNo) - findSumOfPicked(key, lineNo);
					double remainingUnpickedQty = getRemainingQty(key, lineNo);
					//jp.answerlink.213796.begin

					tempQty = pdAvailableQty < remainingUnpickedQty ? pdAvailableQty : remainingUnpickedQty ; 
					qbe.set(QTY, ""+tempQty);
					// 08/Dec/2009: Seshu - 3PL Enhancements Catch Weight Outbound Changes - Starts
					RuntimeFormInterface form = state.getCurrentRuntimeForm();
					if(form.getName().equalsIgnoreCase("wm_pickdetail_detail_view"))
					{
						String storerKey = qbe.getValue("STORERKEY").toString();
						String sku = qbe.getValue("SKU").toString();

						CalculateAdvCatchWeightsHelper helper = new CalculateAdvCatchWeightsHelper();
						String enableAdvCatchWgt = helper.isAdvCatchWeightEnabled(storerKey, sku);

						if(enableAdvCatchWgt != null && enableAdvCatchWgt.equalsIgnoreCase("1"))
						{
						   HashMap actualWgts = new HashMap();
						   String loc = qbe.get(LOCATION).toString();
						   String lot = qbe.get(LOT).toString();
						   String id = qbe.get(LPN).toString();
						   actualWgts = helper.getCalculatedWeightsLPN(storerKey, sku, loc, lot, id, tempQty);

						   Double actualGWT = (Double)actualWgts.get("GROSSWEIGHT");
						   Double actualNWT = (Double)actualWgts.get("NETWEIGHT");
						   Double actualTWT = (Double)actualWgts.get("TAREWEIGHT");

						   qbe.setValue("GROSSWGT", actualGWT);
						   qbe.setValue("NETWGT", actualNWT);
						   qbe.setValue("TAREWGT", actualTWT);
						}
					}
					// 3PL Enhancements Catch Weight Outbound Changes - Ends
				}
			}
			//SM 11-28-07 SCM-00000-03677: End Update
			
			result.setFocus(qbe);
			session.removeAttribute(BIOREF);
			
		}
		return RET_CONTINUE;
	}
	
	//SM 11-28-07 SCM-00000-03677
	private String commaStrip(String number){
		NumberFormat nf = NumberFormat.getInstance();
		try{
			number = nf.parse(number).toString();
		} catch (ParseException e){
			Pattern pattern = Pattern.compile("\\,");
			Matcher matcher = pattern.matcher(number);
			return matcher.replaceAll("");
		}
		return number;
	}
	
	private double findSumOfPicked(String key, String orderLineNumber) throws EpiDataException{
		String query = "SELECT "+KEY+", "+LINE_NO+", SUM(QTY) FROM PICKDETAIL GROUP BY "+KEY+", "+LINE_NO+" HAVING ("+KEY+"='"+key+"') AND ("+LINE_NO+"='"+orderLineNumber+"')";
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		double sumQty = 0;
		if (results.getRowCount() == 1){
			sumQty = Double.parseDouble(results.getAttribValue(3).getAsString());
		}
		return sumQty;
	}
	
	private double getOrigQty(String key, String orderLineNumber) throws EpiDataException{
		//jp.answerlink.149400.begin
		//Changed to use Open Qty instead of original qty.
		String query = "SELECT "+OPEN_QTY+" FROM ORDERDETAIL WHERE "+KEY+"='"+key+"' AND "+LINE_NO+"='"+orderLineNumber+"'";
		//jp.answerlink.149400.end
		
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		double originalQty = 0;
		if (results.getRowCount() == 1)	{
			originalQty = Double.parseDouble(results.getAttribValue(1).getAsString());
		}
		return originalQty;
	}
	//SM 11-28-07 SCM-00000-03677: End Update
	
	private double getRemainingQty(String key, String orderLineNumber) throws EpiDataException{
		String query = "SELECT originalqty + adjustedqty - shippedqty - qtyallocated - qtypicked " +
						"FROM ORDERDETAIL WHERE "+KEY+"='"+key+"' AND "+LINE_NO+"='"+orderLineNumber+"'";

		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		double remainQty = 0;
		if (results.getRowCount() == 1)	{
			remainQty = Double.parseDouble(results.getAttribValue(1).getAsString());
		}
		return remainQty;

	}

}

