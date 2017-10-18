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
 * (c) COPYRIGHT 2011 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */
package com.ssaglobal.scm.wms.wm_item.ui;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;

/**
 * TODO Document PopulateItemtTareWeight class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class PopulateItemtTareWeight extends ActionExtensionBase{
	protected int execute( ActionContext context, ActionResult result ) throws EpiException, UserException {
/*		boolean isNew = false;
		RuntimeFormInterface currentForm = context.getState().getCurrentRuntimeForm();
		DataBean dataBean = currentForm.getFocus();
		if(dataBean.isTempBio()){
			isNew = true;
		}

		Object obj = dataBean.getValue("TAREWEIGHT");
		String tareWeightStr = dataBean.getValue("TAREWEIGHT")==null?"0":dataBean.getValue("TAREWEIGHT").toString();
		String otareWeightStr = dataBean.getValue("OTAREWEIGHT")==null?"0":dataBean.getValue("OTAREWEIGHT").toString();
		String sttareWeightStr = dataBean.getValue("TAREWGT1 ")==null?"0":dataBean.getValue("TAREWGT1").toString();
		Double tareWeight = Double.parseDouble(tareWeightStr);
		Double obTareWeight = Double.parseDouble(otareWeightStr);
		Double stTareWeight = Double.parseDouble(sttareWeightStr);
		
		if(isNew){
			dataBean.setValue("TAREWEIGHT", dataBean.getValue("TARE").toString());
			dataBean.setValue("OTAREWEIGHT", dataBean.getValue("TARE").toString());
			dataBean.setValue("TAREWGT1", dataBean.getValue("TARE").toString());
		}else{//update
			if(Math.abs(tareWeight) < 0.00001){
				dataBean.setValue("TAREWEIGHT", dataBean.getValue("TARE").toString());
			}
			if(Math.abs(obTareWeight) < 0.00001){
				dataBean.setValue("OTAREWEIGHT", dataBean.getValue("TARE").toString());
			}
			if(Math.abs(stTareWeight) < 0.00001){
				dataBean.setValue("TAREWGT1", dataBean.getValue("TARE").toString());
			}			
		}
		
*/		
		boolean isNew = false;
		RuntimeFormInterface currentForm = context.getState().getCurrentRuntimeForm();
		DataBean dataBean = currentForm.getFocus();
		if(dataBean.isTempBio()){
			isNew = true;
		}
		double wdTareWeight = BioAttributeUtil.getDouble(dataBean, "TAREWEIGHT");
		double wdOTareWeight = BioAttributeUtil.getDouble(dataBean, "OTAREWEIGHT");
		double pTareWeight = BioAttributeUtil.getDouble(dataBean, "TAREWGT1");
		if(Math.abs(wdTareWeight)<0.00001){
			dataBean.setValue("TAREWEIGHT", dataBean.getValue("TARE").toString());
		}
		if(isNew || Math.abs(wdOTareWeight)<0.00001){
			dataBean.setValue("OTAREWEIGHT", dataBean.getValue("TARE").toString());
		}
		if(isNew || Math.abs(pTareWeight)<0.00001){
			dataBean.setValue("TAREWGT1", dataBean.getValue("TARE").toString());
		}
		

		result.setFocus(dataBean);
		return RET_CONTINUE;
	}
}
