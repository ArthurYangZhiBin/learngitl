package com.infor.scm.waveplanning.wp_wm_wave.wave.actions;

import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.infor.scm.waveplanning.wp_wm_wave.wave.*;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;


public class ShipWave implements WMWaveActionInterface{
	public int validate(WaveValidateInputObj input) throws UserException{
		if(com.infor.scm.waveplanning.wp_wm_wave.wave.WPUtil.validateUtil(input) == WPConstants.NO_ORDER_TO_SHIP){
			throw new UserException("WPEXP_SHIPWAVE_NO_ORDERS", new Object[0]);
		}
		return WPConstants.VALIDATION_PASSED;			
	}
	public EXEDataObject doWaveAction(WaveInputObj input)throws WebuiException{
		input.setProcedureName(WPConstants.SHIP);
		EXEDataObject edo = WPStoreProcedureCallWrapper.doAction(input);
		return edo;
	}
}
