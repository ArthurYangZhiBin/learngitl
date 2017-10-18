package com.ssaglobal.scm.wms.wm_adjustment.ui;

import java.util.HashMap;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.common.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.SessionUtil;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;
import com.ssaglobal.scm.wms.wm_cyclecount.ui.CalculateAdvCatchWeightsHelper;

public class AdjustmentWeightCalc extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(AdjustmentWeightCalc.class);
	protected int execute(ActionContext context, ActionResult result)throws EpiException{
		RuntimeFormWidgetInterface source = context.getSourceWidget();
		String sourceName = source.getName();
		QBEBioBean focus = (QBEBioBean)source.getForm().getFocus();

		if(sourceName.equals("ACTUALGROSSWGT")){
			double dblNewVal = 0;
			double dblchgVal = 0;
			double dbladjVal = 0;
			Object newValue = source.getForm().getFormWidgetByName("ACTUALGROSSWGT").getValue();
			Object adjVal = source.getForm().getFormWidgetByName("ADJGROSSWGT").getValue();
			double dblOldVal = ((Double)SessionUtil.getInteractionSessionAttribute("OLDTRGWT",context.getState())).doubleValue();
			if (newValue!=null && newValue.toString().trim().length() > 0){
				dblNewVal = (new Double(newValue.toString())).doubleValue();
			}
			if (adjVal!=null && adjVal.toString().trim().length() > 0){
				dbladjVal = (new Double(adjVal.toString())).doubleValue();
			}
			if(dblNewVal>dblOldVal) dblchgVal=dblNewVal-dblOldVal;
			else
			{
				dblchgVal=dblOldVal-dblNewVal;
				dblchgVal=dblchgVal* (-1);
			}
			focus.setValue("ADJGROSSWGT", dbladjVal + dblchgVal);
			SessionUtil.setInteractionSessionAttribute("OLDTRGWT",  dblNewVal, context.getState());
			SessionUtil.setInteractionSessionAttribute("OLDADGWT",  dbladjVal + dblchgVal, context.getState());
		}
		if(sourceName.equals("ACTUALNETWGT")){
			double dblNewVal = 0;
			double dblchgVal = 0;
			double dbladjVal = 0;
			Object newValue = source.getForm().getFormWidgetByName("ACTUALNETWGT").getValue();
			double dblOldVal = ((Double)SessionUtil.getInteractionSessionAttribute("OLDTRNWT",context.getState())).doubleValue();
			Object adjVal = source.getForm().getFormWidgetByName("ADJNETWGT").getValue();
			if (newValue!=null && newValue.toString().trim().length() > 0){
				dblNewVal = (new Double(newValue.toString())).doubleValue();
			}
			if (adjVal!=null && adjVal.toString().trim().length() > 0){
				dbladjVal = (new Double(adjVal.toString())).doubleValue();
			}
			if(dblNewVal>dblOldVal) dblchgVal=dblNewVal-dblOldVal;
			else
			{
				dblchgVal=dblOldVal-dblNewVal;
				dblchgVal=dblchgVal* (-1);
			}
			focus.setValue("ADJNETWGT", dbladjVal + dblchgVal);
		    SessionUtil.setInteractionSessionAttribute("OLDTRNWT", dblNewVal, context.getState());
		    SessionUtil.setInteractionSessionAttribute("OLDADNWT", dbladjVal + dblchgVal, context.getState());
		}
		if(sourceName.equals("ACTUALTAREWGT")){
			double dblNewVal = 0;
			double dblchgVal = 0;
			double dbladjVal = 0;
			Object newValue = source.getForm().getFormWidgetByName("ACTUALTAREWGT").getValue();
			double dblOldVal = ((Double)SessionUtil.getInteractionSessionAttribute("OLDTRTWT",context.getState())).doubleValue();
			Object adjVal = source.getForm().getFormWidgetByName("ADJTAREWGT").getValue();
			if (newValue!=null && newValue.toString().trim().length() > 0){
				dblNewVal = (new Double(newValue.toString())).doubleValue();
			}
			if (adjVal!=null && adjVal.toString().trim().length() > 0){
				dbladjVal = (new Double(adjVal.toString())).doubleValue();
			}
			if(dblNewVal>dblOldVal) dblchgVal=dblNewVal-dblOldVal;
			else
			{
				dblchgVal=dblOldVal-dblNewVal;
				dblchgVal=dblchgVal* (-1);
			}
			focus.setValue("ADJTAREWGT", dbladjVal + dblchgVal);
			SessionUtil.setInteractionSessionAttribute("OLDTRTWT",  dblNewVal, context.getState());
		    SessionUtil.setInteractionSessionAttribute("OLDADTWT", dbladjVal + dblchgVal, context.getState());
		}
		if(sourceName.equals("ADJGROSSWGT")){
			double dblNewVal = 0;
			double dblchgVal = 0;
			double dbltrgVal = 0;
			Object newValue = source.getForm().getFormWidgetByName("ADJGROSSWGT").getValue();
			double dblOldVal =((Double)SessionUtil.getInteractionSessionAttribute("OLDADGWT",context.getState())).doubleValue();
			Object trgVal = source.getForm().getFormWidgetByName("ACTUALGROSSWGT").getValue();
			if (newValue!=null && newValue.toString().trim().length() > 0){
				dblNewVal = (new Double(newValue.toString())).doubleValue();
			}
			if (trgVal!=null && trgVal.toString().trim().length() > 0){
				dbltrgVal = (new Double(trgVal.toString())).doubleValue();
			}
			if(dblNewVal>dblOldVal) dblchgVal=dblNewVal-dblOldVal;
			else
			{
				dblchgVal=dblOldVal-dblNewVal;
				dblchgVal=dblchgVal* (-1);
			}
			focus.setValue("ACTUALGROSSWGT", dbltrgVal + dblchgVal);
			SessionUtil.setInteractionSessionAttribute("OLDADGWT",  dblNewVal, context.getState());
		    SessionUtil.setInteractionSessionAttribute("OLDTRGWT", dbltrgVal + dblchgVal, context.getState());
		}
		if(sourceName.equals("ADJNETWGT")){
			double dblNewVal = 0;
			double dblchgVal = 0;
			double dbltrgVal = 0;
			Object newValue = source.getForm().getFormWidgetByName("ADJNETWGT").getValue();
			double dblOldVal =((Double)SessionUtil.getInteractionSessionAttribute("OLDADNWT",context.getState())).doubleValue();
			Object trgVal = source.getForm().getFormWidgetByName("ACTUALNETWGT").getValue();
			if (newValue!=null && newValue.toString().trim().length() > 0){
				dblNewVal = (new Double(newValue.toString())).doubleValue();
			}
			if (trgVal!=null && trgVal.toString().trim().length() > 0){
				dbltrgVal = (new Double(trgVal.toString())).doubleValue();
			}
			if(dblNewVal>dblOldVal) dblchgVal=dblNewVal-dblOldVal;
			else
			{
				dblchgVal=dblOldVal-dblNewVal;
				dblchgVal=dblchgVal* (-1);
			}
			focus.setValue("ACTUALNETWGT", dbltrgVal + dblchgVal);
			SessionUtil.setInteractionSessionAttribute("OLDADNWT",  dblNewVal, context.getState());
		    SessionUtil.setInteractionSessionAttribute("OLDTRNWT", dbltrgVal + dblchgVal, context.getState());
		}
		if(sourceName.equals("ADJTAREWGT")){
			double dblNewVal = 0;
			double dblchgVal = 0;
			double dbltrgVal = 0;
			Object newValue = source.getForm().getFormWidgetByName("ADJTAREWGT").getValue();
			double dblOldVal =((Double)SessionUtil.getInteractionSessionAttribute("OLDADTWT",context.getState())).doubleValue();
			Object trgVal = source.getForm().getFormWidgetByName("ACTUALTAREWGT").getValue();
			if (newValue!=null && newValue.toString().trim().length() > 0){
				dblNewVal = (new Double(newValue.toString())).doubleValue();
			}
			if (trgVal!=null && trgVal.toString().trim().length() > 0){
				dbltrgVal = (new Double(trgVal.toString())).doubleValue();
			}
			if(dblNewVal>dblOldVal) dblchgVal=dblNewVal-dblOldVal;
			else
			{
				dblchgVal=dblOldVal-dblNewVal;
				dblchgVal=dblchgVal* (-1);
			}
			focus.setValue("ACTUALTAREWGT", dbltrgVal + dblchgVal);
			SessionUtil.setInteractionSessionAttribute("OLDADTWT",  dblNewVal, context.getState());
		    SessionUtil.setInteractionSessionAttribute("OLDTRTWT", dbltrgVal + dblchgVal, context.getState());
		}
		return RET_CONTINUE;
	}
	
}
