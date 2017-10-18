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


package com.ssaglobal.scm.wms.wm_serial_number.ui;

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
import com.ssaglobal.scm.wms.util.dao.SerialInventoryDAO;
import com.ssaglobal.scm.wms.util.dto.CCDetailSerialTmpDTO;
import com.ssaglobal.scm.wms.util.dto.SerialInventoryDTO;
import com.ssaglobal.scm.wms.wm_adjustment.ui.AdjustmentDetailSerialTmpDTO;
import com.ssaglobal.scm.wms.wm_adjustment.ui.AdjustmentHelper;


public class SNDetailSerialValidate extends com.epiphany.shr.ui.action.ActionExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(SNDetailSerialValidate.class);

    protected int execute( ActionContext context, ActionResult result ) throws EpiException {

      return super.execute( context, result );
   }
   
    protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {

		StateInterface state = ctx.getState();
		
		String sessionAttr = (String)getParameter("SESSION_ATTRIBUTE");
		
		ISerialNumber detailSerial = new SerialNumberImpl().getSerialNumberInstance(state, sessionAttr);
		if(detailSerial==null){
			_log.debug("LOG_SYSTEM_OUT","[SNDetailSerialValidate]Cannot cast session attribute",100L);
			return RET_CANCEL;
		}
		//state.getRequest().getSession().removeAttribute(SESSIONATTRIB_ADJUSTMENTDETAILSERIALTMP);
		
		
		RuntimeFormInterface modal = ctx.getState().getCurrentRuntimeForm();
		
		
		RuntimeFormWidgetInterface widget=null;
		
		widget =modal.getFormWidgetByName("SERIALNUMBER");
		if(widget!=null && widget.getValue()!=null)
			detailSerial.setSerialnumber( widget.getValue().toString());
		
		widget =modal.getFormWidgetByName("SERIALNUMBERLONG");
		if(widget!=null && widget.getValue()!=null)
			detailSerial.setSerialnumberlong( widget.getValue().toString());
		
		_log.debug("LOG_SYSTEM_OUT","[SNDetailSerialValidate]source widget name:"+ctx.getSourceWidget().getName()+"\n\n",100L);
		
		String serialnumberMasked = detailSerial.getSerialnumberlong();
		
		String serialnumber = extractSerialNumber(detailSerial, serialnumberMasked);
		
		if(serialnumber!=null){

			ISerialNumber detailSerialTmp = new SerialNumberImpl().getSerialNumberInstance(state, sessionAttr);
			if(detailSerialTmp==null){
				_log.debug("LOG_SYSTEM_OUT","[SNDetailSerialValidate]Cannot cast session attribute",100L);
				return RET_CANCEL;
			}

			//CCDetailSerialTmpDTO adjDetailSerialTmp = new CCDetailSerialTmpDTO();
			detailSerialTmp.setStorerkey(detailSerial.getStorerkey());
			detailSerialTmp.setSku(detailSerial.getSku());
			detailSerialTmp.setSerialnumber(serialnumber);
			
			SerialInventoryDTO serialInventory = new SerialInventoryDAO().findSerialInventory(detailSerialTmp);
			if(serialInventory!=null){
				String[] params = new String[1];
				params[0] = serialnumber;
				throw new UserException ("WMEXP_DUPLICATE_SERIAL", params);
			}else{
				modal.getFocus().setValue("SERIALNUMBER", serialnumber);
				enableWidgets(true, modal);
			}
		}else
			enableWidgets(false, modal);

		return RET_CONTINUE;

    }
    
    
    
	private void enableWidgets(boolean option, RuntimeFormInterface modal){
		
		modal.getFormWidgetByName("DATA2").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, !option);
		modal.getFormWidgetByName("DATA3").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, !option);
		modal.getFormWidgetByName("DATA4").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, !option);
		modal.getFormWidgetByName("DATA5").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, !option);
		modal.getFormWidgetByName("NETWEIGHT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, !option);
		
	
	}
	private String extractSerialNumber(ISerialNumber adjDetailSerial, String serialnumberMasked) throws UserException{
		
		
		
		String serialnumber = null;
		
		
		try{
			AdjustmentHelper helper = new AdjustmentHelper();
			ArrayList serials = helper.getSerials(adjDetailSerial.getStorerkey(), adjDetailSerial.getSku(), serialnumberMasked);
			if(serials.size()<=0){
				String[] params = new String[1];
				params[0]=serialnumberMasked;
				throw new UserException("WMEXP_INVALID_SERIAL", params);
			}else{
				SerialNoDTO serialnoDTO = (SerialNoDTO)serials.get(0);
				serialnumber = serialnoDTO.getSerialnumber();
				//serialnumber = (String)serials.get(0);
				
				_log.debug("LOG_SYSTEM_OUT","[SNDetailSerialValidate]SerialNumber:"+serialnumber,100L);
			}
		}catch(FulfillLogicException e){
			e.printStackTrace();
			String[] params = new String[1];
			params[0]=e.getMessage();
			throw new UserException("WMEXP_COMMON_INVALID_SERIAL", params);
			
		}
		
		
		return serialnumber;
	}

}
