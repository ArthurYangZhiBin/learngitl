package com.ssaglobal.scm.wms.wm_shipmentorder.ui;


import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class PrintLabelPopupPrerender extends FormExtensionBase{
	private static final String LABEL_NAME ="LABEL_NAME";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PrintLabelPopupPrerender.class);
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		String  labelName = "";
		Object labelNameObj = state.getRequest().getSession().getAttribute(LABEL_NAME);
		if(labelNameObj != null){
			labelName = labelNameObj.toString();
		}
		if("Carton Label".equalsIgnoreCase(labelName)
				||"Case Label".equalsIgnoreCase(labelName)
				||"Assignment Label".equalsIgnoreCase(labelName)
				||"Batch Pick Label".equalsIgnoreCase(labelName)){
			labelName = "wavecase_id";
		}

		String printerName = getDefaultPrinter(uow, labelName);
		form.getFormWidgetByName("printers").setValue(printerName);
		form.getFormWidgetByName("copies").setDisplayValue("1");
//		state.getRequest().removeAttribute(LABEL_NAME);
		return RET_CONTINUE;
	}
	
	private String getDefaultPrinter(UnitOfWorkBean uow, String labelName) throws EpiException{
		Query qry = new Query("wm_label_configuration", "wm_label_configuration.LABELNAME='"+labelName+"'", null);		
		BioCollectionBean bcb = uow.getBioCollectionBean(qry);
		String printerType = bcb.get("0").getString("PRINTERTYPE");		
		qry = new Query("wm_label_printer", "wm_label_printer.PRINTERTYPE='"+printerType+"'", "wm_label_printer.PRINTERNAME DESC");		
		bcb = uow.getBioCollectionBean(qry);
		if(bcb != null && bcb.size() != 0){
			return bcb.get("0").getString("PRINTERNAME");
		}
		return null;
	}
}
