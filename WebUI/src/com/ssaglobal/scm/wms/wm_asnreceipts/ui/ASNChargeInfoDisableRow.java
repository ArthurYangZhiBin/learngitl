package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeListRowInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.ui.action.UIRenderContext;

public class ASNChargeInfoDisableRow extends FormExtensionBase {

	public ASNChargeInfoDisableRow() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form) throws EpiException {
		try{
			RuntimeListRowInterface[] listRows = form.getRuntimeListRows();
			for (int i = 0; i < listRows.length; i++){
				//Get List Widget
				RuntimeFormWidgetInterface ClosedFlag = listRows[i].getFormWidgetByName("CLOSED_FLAG"); // 9 
				RuntimeFormWidgetInterface ChargeCode = listRows[i].getFormWidgetByName("CHARGE_CODE"); // 9
				RuntimeFormWidgetInterface ChargeComment = listRows[i].getFormWidgetByName("CHARGE_COMMENT"); // 9
				RuntimeFormWidgetInterface BillTo = listRows[i].getFormWidgetByName("BILL_TO_CUST"); // 9
				RuntimeFormWidgetInterface Qty = listRows[i].getFormWidgetByName("CHARGE_QTY"); // 9
				RuntimeFormWidgetInterface Rate = listRows[i].getFormWidgetByName("CHARGE_RATE"); // 9
				RuntimeFormWidgetInterface UOM = listRows[i].getFormWidgetByName("UOM_CODE"); // 9

				//Get Values
				String ClosedFlagValue = ClosedFlag.getDisplayValue();

				if (ClosedFlagValue.equalsIgnoreCase("1") ){
					ChargeCode.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					ChargeComment.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					BillTo.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					Qty.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					Rate.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					UOM.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
				}
				else{
					ChargeCode.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					ChargeComment.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					BillTo.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					Qty.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					Rate.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					UOM.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
				}
			}
		} catch (Exception e){

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}
		return RET_CONTINUE;
	}

}
