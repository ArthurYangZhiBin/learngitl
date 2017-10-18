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

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.service.baseobjects.FulfillLogicException;
import com.ssaglobal.scm.wms.service.dutilitymanagement.dto.SerialNoDTO;

/**
 * Saves records in temporarily in AdjustmentDetailSerialTmp table
* @return int RET_CONTINUE, RET_CANCEL
*/

public class AdjustmentDetailSerialSave extends com.epiphany.shr.ui.action.ActionExtensionBase {
	
	private static final String SESSIONATTRIB_ADJUSTMENTDETAILSERIALTMP ="ADJUSTMENTDETAILSERIALTMP";
	private static final double DEFAULTQTY = 1.0;
	private static final String PARAM_JUST_VALIDATE="JUST_VALIDATE";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(AdjustmentDetailSerialSave.class);
	
	protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		
		return super.execute( context, result );
	}
	
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {
		
		
		
		StateInterface state = ctx.getState();
		
		AdjustmentDetailSerialTmpDTO adjDetailSerial = (AdjustmentDetailSerialTmpDTO)state.getRequest().getSession().getAttribute(SESSIONATTRIB_ADJUSTMENTDETAILSERIALTMP);
		
		//state.getRequest().getSession().removeAttribute(SESSIONATTRIB_ADJUSTMENTDETAILSERIALTMP);
		
		
		RuntimeFormInterface modal = ctx.getState().getCurrentRuntimeForm();
		
		
		RuntimeFormWidgetInterface widget=null;
		
		widget =modal.getFormWidgetByName("SERIALNUMBER");
		if(widget!=null && widget.getValue()!=null)
			adjDetailSerial.setSerialnumber( widget.getValue().toString());
		
		widget =modal.getFormWidgetByName("SERIALNUMBERLONG");
		if(widget!=null && widget.getValue()!=null)
			adjDetailSerial.setSerialnumberlong( widget.getValue().toString());
		
		
		widget =modal.getFormWidgetByName("DATA2");
		if(widget!=null && widget.getValue()!=null)
			adjDetailSerial.setData2( widget.getValue().toString());
		
		widget =modal.getFormWidgetByName("DATA3");
		if(widget!=null && widget.getValue()!=null)
			adjDetailSerial.setData3( widget.getValue().toString());
		
		widget =modal.getFormWidgetByName("DATA4");
		if(widget!=null && widget.getValue()!=null)
			adjDetailSerial.setData4( widget.getValue().toString());
		
		widget =modal.getFormWidgetByName("DATA5");
		if(widget!=null && widget.getValue()!=null)
			adjDetailSerial.setData5( widget.getValue().toString());
		
		widget =modal.getFormWidgetByName("NETWEIGHT");
		if(widget!=null && widget.getValue()!=null)
			adjDetailSerial.setNetweight( widget.getValue().toString());
		
		adjDetailSerial.setQty((new Double(DEFAULTQTY)).toString());
		
		
		if(validateFields(adjDetailSerial)){
			AdjustmentDetailSerialTmpDAO.insertAdjustmentDetailSerialTmp(adjDetailSerial);
		}

		//state.getDefaultUnitOfWork().removeBio((BioBean)state.getFocus());
		return RET_CONTINUE;
	}
	
	private boolean validateFields(AdjustmentDetailSerialTmpDTO adjDetailSerial) throws UserException{
		
		
		String serialnumberMasked = adjDetailSerial.getSerialnumberlong();
		String serialnumber = null;
		
		/**
		if((adjDetailSerial.getSerialnumber()==null || adjDetailSerial.getSerialnumber().trim().length()==0)){
			if(adjDetailSerial.getSerialnumberlong()==null ||adjDetailSerial.getSerialnumberlong().trim().length()==0){
				throw new UserException("WMEXP_MISSING_SERIAL", new Object[] {});	
			}else{
				serialnumberMasked = adjDetailSerial.getSerialnumberlong();
			}
		}else{
			serialnumberMasked = adjDetailSerial.getSerialnumber();
			
			if(adjDetailSerial.getSerialnumberlong()==null ||adjDetailSerial.getSerialnumberlong().trim().length()==0){
				
			}else{
				System.out.println("[AdjustmentDetailSerialSave]SN:"+adjDetailSerial.getSerialnumber()+
						" SNL:"+adjDetailSerial.getSerialnumberlong());
				throw new UserException("WMEXP_TWO_SERIALS", new Object[] {});
			}
			
		}
		**/
		
		if(serialnumberMasked==null || serialnumberMasked.trim().length()==0){
			String[] params = new String[0];
			throw new UserException("WMEXP_MISSING_SERIAL", params);
		}
		try{
			AdjustmentHelper helper = new AdjustmentHelper();
			ArrayList serials = helper.getSerials(adjDetailSerial.getStorerkey(), adjDetailSerial.getSku(), serialnumberMasked);
			if(serials.size()<=0){
				String[] params = new String[1];
				params[0]=serialnumberMasked;
				throw new UserException("WMEXP_INVALID_SERIAL", params);
			}else{
				SerialNoDTO serialNoDTO = (SerialNoDTO)serials.get(0); 
				serialnumber = serialNoDTO.getSerialnumber();
				adjDetailSerial.setSerialnumber(serialnumber);
				_log.debug("LOG_SYSTEM_OUT","[AdjustmentDetailSerialSave]SerialNumber:"+serialnumber,100L);
			}
		}catch(FulfillLogicException e){
			e.printStackTrace();
			String[] params = new String[1];
			params[0]=e.getMessage();
			throw new UserException("WMEXP_COMMON_INVALID_SERIAL", params);
			
		}
		
		
		return true;
	}
	
	
	
	
	
}
