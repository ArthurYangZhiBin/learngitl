/**
 * ---Begin Copyright Notice---20090105T1353
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2010 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */
package com.infor.scm.waveplanning.wp_wm_wave.wave.actions;

import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.wp_wm_wave.wave.*;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.agileitp.forte.framework.TextData;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.data.bio.Query;

/**
 * TODO Document PreallocateWave class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class PreallocateWave implements WMWaveActionInterface{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PreallocateWave.class);
	public int validate(WaveValidateInputObj input) throws UserException{
		if(com.infor.scm.waveplanning.wp_wm_wave.wave.WPUtil.validateUtil(input) == WPConstants.NO_ORDER_TO_PREALLOCATE){
			throw new UserException("WPEXP_PREALLOCATEWAVE_NO_ORDERS", new Object[0]);
		}
		return WPConstants.VALIDATION_PASSED;			
	}

	public EXEDataObject doWaveAction(WaveInputObj input)throws WebuiException{
		input.setProcedureName(WPConstants.PREALLOCATE);
		String waveKey = input.getWaveKey();
		input.getProcedureParametes().add(new TextData(waveKey));
		EXEDataObject edo = WPStoreProcedureCallWrapper.doAction(input);
		return edo;
	}
}
