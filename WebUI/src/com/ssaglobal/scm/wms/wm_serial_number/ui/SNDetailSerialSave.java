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

import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.ssaglobal.scm.wms.service.baseobjects.FulfillLogicException;
import com.ssaglobal.scm.wms.service.dutilitymanagement.dto.SerialNoDTO;
import com.ssaglobal.scm.wms.util.dao.SerialInventoryDAO;
import com.ssaglobal.scm.wms.util.dto.SerialInventoryDTO;
import com.ssaglobal.scm.wms.wm_adjustment.ui.AdjustmentDetailSerialTmpDAO;
import com.ssaglobal.scm.wms.wm_adjustment.ui.AdjustmentDetailSerialTmpDTO;
import com.ssaglobal.scm.wms.wm_adjustment.ui.AdjustmentHelper;


public class SNDetailSerialSave extends com.epiphany.shr.ui.action.ActionExtensionBase {
	
	private static final double DEFAULTQTY = 1.0;
	protected static ILoggerCategory _log = LoggerFactory.getInstance(SNDetailSerialSave.class);
	
	protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		
		return super.execute( context, result );
	}
	
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {
		
		StateInterface state = ctx.getState();
		
		String sessionAttr = (String)getParameter("SESSION_ATTRIBUTE");
		
		ISerialNumber detailSerial = new SerialNumberImpl().getSerialNumberInstance(state, sessionAttr);
		if(detailSerial==null){
			_log.debug("LOG_SYSTEM_OUT","[SNDetailSerialSave]Cannot cast session attribute",100L);
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
		
		widget =modal.getFormWidgetByName("DATA2");
		if(widget!=null && widget.getValue()!=null)
			detailSerial.setData2( widget.getValue().toString());
		
		widget =modal.getFormWidgetByName("DATA3");
		if(widget!=null && widget.getValue()!=null)
			detailSerial.setData3( widget.getValue().toString());
		
		widget =modal.getFormWidgetByName("DATA4");
		if(widget!=null && widget.getValue()!=null)
			detailSerial.setData4( widget.getValue().toString());
		
		widget =modal.getFormWidgetByName("DATA5");
		if(widget!=null && widget.getValue()!=null)
			detailSerial.setData5( widget.getValue().toString());
		
		widget =modal.getFormWidgetByName("NETWEIGHT");
		if(widget!=null && widget.getValue()!=null)
			detailSerial.setNetweight( widget.getValue().toString());
		
		detailSerial.setQty((new Double(DEFAULTQTY)).toString());
		
		
		if(validateFields(detailSerial)){
			new SerialNumberImpl().insertSerial(detailSerial);
			//AdjustmentDetailSerialTmpDAO.insertAdjustmentDetailSerialTmp((AdjustmentDetailSerialTmpDTO)detailSerial);
		}
		
		//state.getDefaultUnitOfWork().removeBio((BioBean)state.getFocus());
		return RET_CONTINUE;
	}

	private boolean validateFields(ISerialNumber detailSerial)throws UserException {
		String serialnumberMasked = detailSerial.getSerialnumberlong();
		String serialnumber = null;
		
		
		if(serialnumberMasked==null || serialnumberMasked.trim().length()==0){
			String[] params = new String[0];
			throw new UserException("WMEXP_MISSING_SERIAL", params);
		}
		try{
			SerialNumberImpl helper = new SerialNumberImpl();
			ArrayList serials = helper.getSerials(detailSerial.getStorerkey(), detailSerial.getSku(), serialnumberMasked);
			if(serials.size()<=0){
				String[] params = new String[1];
				params[0]=serialnumberMasked;
				throw new UserException("WMEXP_INVALID_SERIAL", params);
			}else{
				SerialNoDTO serialNoDTO = (SerialNoDTO)serials.get(0); 
				serialnumber = serialNoDTO.getSerialnumber();
				detailSerial.setSerialnumber(serialnumber);
				_log.debug("LOG_SYSTEM_OUT","[SNDetailSerialSave]SerialNumber:"+serialnumber,100L);
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
