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
package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.ui;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;

/**
 * TODO Document ShippedWidgetPrerender class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class ShippedWidgetPrerender extends FormWidgetExtensionBase{
	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget) throws EpiException{
		RuntimeFormInterface pickDetailListForm = widget.getForm().getParentForm(state);
		DataBean dataBean = pickDetailListForm.getFocus();
		BioCollectionBean bcb = null;
		BioBean bioBean = null;
		if(dataBean != null && dataBean.isBioCollection()){
			bcb = (BioCollectionBean)dataBean;
			int size = bcb.size();
			if(size == 0){
				widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
			}else{
				bioBean = bcb.get(""+0);
				String status = bioBean.getValue("STATUS").toString();
				if("9".equalsIgnoreCase(status)){//shipped
					widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
				}
				if("5".equalsIgnoreCase(status)){//picked
					if("Pick".equalsIgnoreCase(widget.getName())){
						widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					}
					if("Delete".equalsIgnoreCase(widget.getName())){
						widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					}
					if("Ship".equalsIgnoreCase(widget.getName())){
						widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					}
				}
				if("0".equalsIgnoreCase(status)){//picked
					if("Pick".equalsIgnoreCase(widget.getName())){
						widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					}
					if("Delete".equalsIgnoreCase(widget.getName())){
						widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					}
					if("Ship".equalsIgnoreCase(widget.getName())){
						widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					}
				}
				
				
			}
		}
		

		return RET_CONTINUE;
	}

}
