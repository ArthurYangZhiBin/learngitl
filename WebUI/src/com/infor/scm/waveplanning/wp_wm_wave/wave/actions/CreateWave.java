package com.infor.scm.waveplanning.wp_wm_wave.wave.actions;

import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.wp_wm_wave.wave.*;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;



public class CreateWave implements WMWaveActionInterface{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CreateWave.class);
	public int validate(WaveValidateInputObj input)throws UserException{
		try{
			RuntimeListFormInterface form = input.getForm();
			if(form == null){
				return WPConstants.NO_ORDER_TO_CREATE_WAVE;
			}
			DataBean focus = form.getFocus();
			if(focus == null || !focus.isBioCollection() || ((BioCollection)focus).size() == 0){
				return WPConstants.NO_ORDER_TO_CREATE_WAVE;
			}
			input.setOrderSize(((BioCollection)focus).size());
			return WPConstants.VALIDATION_PASSED;
		}catch(EpiDataException e){
			_log.error("LOG_INFO_EXTENSION_WAVECREATE","There is exception from collectio.size():"+e.getMessage(),100L);
			throw new UserException("WPEXP_ACTION_SYS_ERROR", new Object[0]);
		}
	}
	public EXEDataObject doWaveAction(WaveInputObj input)throws WebuiException{
		input.setProcedureName(WPConstants.CREATEWAVE);		
		String orderKeys = input.getOrderKeys();
		String waveKey = input.getWaveKey();
		String waveDesc = input.getWaveDesc();
		input.getProcedureParametes().add(new TextData(orderKeys));
		input.getProcedureParametes().add(new TextData(waveDesc));
		input.getProcedureParametes().add(new TextData(waveKey));
		EXEDataObject edo = WPStoreProcedureCallWrapper.doAction(input);
		return edo;
	}
	

}
