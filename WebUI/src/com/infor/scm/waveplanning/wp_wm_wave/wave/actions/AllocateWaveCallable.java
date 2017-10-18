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

/**
 * TODO Document AllocateWaveCallable class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
import java.util.concurrent.*;

import com.agileitp.forte.framework.TextData;
import com.infor.scm.waveplanning.wp_wm_wave.wave.*;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
public class AllocateWaveCallable implements Callable{
	WaveInputObj waveInput;
	
	public WaveInputObj getWaveInput() {
		return waveInput;
	}

	public void setWaveInput(WaveInputObj waveInput) {
		this.waveInput = waveInput;
	}

	public Integer call () throws WebuiException {
		waveInput.setProcedureName(WPConstants.ALLOCATE);
		String waveKey = waveInput.getWaveKey();
		waveInput.getProcedureParametes().add(new TextData(waveKey));
		waveInput.getProcedureParametes().add(new TextData("0"));
		waveInput.getProcedureParametes().add(new TextData("0"));
		waveInput.getProcedureParametes().add(new TextData("N"));
		EXEDataObject edo = WPStoreProcedureCallWrapper.doAction(waveInput);
		return 1;
	}

}
