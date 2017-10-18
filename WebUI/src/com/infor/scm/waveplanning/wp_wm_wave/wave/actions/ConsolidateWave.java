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
/**
 * TODO Document ConsolidateWave class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class ConsolidateWave implements WMWaveActionInterface{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ConsolidateWave.class);
	public int validate(WaveValidateInputObj input)throws UserException{return WPConstants.VALIDATION_PASSED;}
	public EXEDataObject doWaveAction(WaveInputObj input)throws WebuiException{
		input.setProcedureName(WPConstants.CONSOLIDATE);
		input.getProcedureParametes().add(new TextData(input.getStorerKey()));
		input.getProcedureParametes().add(new TextData(input.getSku()));
		input.getProcedureParametes().add(new TextData(input.getOrderType()));
		input.getProcedureParametes().add(new TextData(input.getConsolloc()));
		input.getProcedureParametes().add(new TextData(input.getMaxlocqty()));
		input.getProcedureParametes().add(new TextData(input.getWavetotal()));
		input.getProcedureParametes().add(new TextData(input.getMinwaveqty()));
		input.getProcedureParametes().add(new TextData(input.getPieceloc()));
		input.getProcedureParametes().add(new TextData(input.getCaseloc()));
		input.getProcedureParametes().add(new TextData(input.getInclude()));
		input.getProcedureParametes().add(new TextData(input.getLoctype()));
		input.getProcedureParametes().add(new TextData(input.getWaveKey()));
		
		EXEDataObject edo = WPStoreProcedureCallWrapper.doAction(input);
		return edo;
	}
}
