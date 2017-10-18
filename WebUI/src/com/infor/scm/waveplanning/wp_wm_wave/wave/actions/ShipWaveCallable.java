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

import java.util.concurrent.Callable;

import com.agileitp.forte.framework.TextData;
import com.infor.scm.waveplanning.wp_wm_wave.wave.WPConstants;
import com.infor.scm.waveplanning.wp_wm_wave.wave.WPStoreProcedureCallWrapper;
import com.infor.scm.waveplanning.wp_wm_wave.wave.WaveInputObj;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

/**
 * TODO Document ShipWaveCallable class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class ShipWaveCallable implements Callable{
	WaveInputObj waveInput;
	
	public WaveInputObj getWaveInput() {
		return waveInput;
	}

	public void setWaveInput(WaveInputObj waveInput) {
		this.waveInput = waveInput;
	}

	public Integer call () throws WebuiException {
		String waveKey = waveInput.getWaveKey();
		waveInput.setProcedureName(WPConstants.SHIP);
		waveInput.getProcedureParametes().add(new TextData(waveKey));
		waveInput.getProcedureParametes().add(new TextData("NO"));
		waveInput.getProcedureParametes().add(new TextData("NO"));			
		EXEDataObject edo = WPStoreProcedureCallWrapper.doAction(waveInput);
		return 1;
	}
}
