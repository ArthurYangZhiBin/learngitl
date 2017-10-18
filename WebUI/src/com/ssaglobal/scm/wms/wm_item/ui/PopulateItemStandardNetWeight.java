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
 * TODO Document PopulateItemStandardNetWeight class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class PopulateItemStandardNetWeight extends ActionExtensionBase{
	protected int execute( ActionContext context, ActionResult result ) throws EpiException, UserException {

		boolean isNew = false;
		
		RuntimeFormInterface currentForm = context.getState().getCurrentRuntimeForm();
		DataBean dataBean = currentForm.getFocus();
		if(dataBean.isTempBio()){
			isNew = true;
		}
		double wdPNetWeight = BioAttributeUtil.getDouble(dataBean, "stdnetwgt1");
		double wdGrossWeight = BioAttributeUtil.getDouble(dataBean, "AVGCASEWEIGHT");
		double wdOGrossWeight = BioAttributeUtil.getDouble(dataBean, "OAVGCASEWEIGHT");

		if(isNew || Math.abs(wdPNetWeight)<0.00001){
			dataBean.setValue("stdnetwgt1", dataBean.getValue("STDNETWGT").toString());
		}
		if(isNew || Math.abs(wdGrossWeight)<0.00001){
			dataBean.setValue("AVGCASEWEIGHT", dataBean.getValue("STDNETWGT").toString());
		}
		if(isNew || Math.abs(wdOGrossWeight)<0.00001){
			dataBean.setValue("OAVGCASEWEIGHT", dataBean.getValue("STDNETWGT").toString());
		}

		result.setFocus(dataBean);
		return RET_CONTINUE;
	}
}
