package com.ssaglobal.scm.wms.wm_adjustment.ui;

import java.util.GregorianCalendar;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.ActionExtensionBase;

public class AdjustmentDetailChargeInfoBillCheckAction extends ActionExtensionBase {
	/******************************************************************
	 * Programmer     :        Krishna Kuchipudi
	 * Created        :       18 May 2010
	 * Purpose        :        WM 9 3PL Enhancements - Misc Charges Enhancement
	 *******************************************************************
	 * Modification History
	 *
	 * 
	 ******************************************************************/

	protected static ILoggerCategory _log = LoggerFactory.getInstance(AdjustmentDetailChargeInfoBillCheckAction.class);

	protected int execute(ActionContext context, ActionResult result) throws UserException, EpiDataException, EpiException{
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();  //SRG
		RuntimeListFormInterface form1 = (RuntimeListFormInterface) state.getCurrentRuntimeForm().getParentForm(state);
		//SRG 12/15/2010: -- Start
		/*String ADJUSTMENTKEY=null;
		String ADJUSTMENTLINENUMBER=null;
		Config conf  = SsaAccessBase.getConfig("cognosClient","defaults");
		Configuration wmDateConfig = conf.getConfiguration();		
		String ejbDateFormat = wmDateConfig.getString("parm");
		_log.debug("LOG_SYSTEM_OUT","ejbDateFormat = "+ ejbDateFormat,100L);
		SimpleDateFormat ejbFormat = new SimpleDateFormat(ejbDateFormat.toString());
		Date currentDateTime = new Date(System.currentTimeMillis());
		String dateVal = ejbFormat.format(currentDateTime);*/
		//SRG 12/15/2010: -- End

		String userLoginId = state.getUser().getName();

		if(form1.isListForm()){

			BioCollectionBean focus  = (BioCollectionBean) form1.getFocus();
			//SRG 12/15/2010: Instead of making direct updates use OA framework to execute update statements
			/*if (focus.size() >0)
			{

				Bio bio = focus.elementAt(0);
				ADJUSTMENTKEY = bio.get("TRAN_KEY1").toString();
				ADJUSTMENTLINENUMBER = bio.get("TRAN_KEY2").toString();
				String mySql = "UPDATE BILL_CHARGEINSTRUCT SET BILL_CHECK = 1, BILL_CHECK_UID = '"+userLoginId+"', BILL_CHECK_DT ='"+ dateVal+"' WHERE TYPE = 'AJ' AND TRAN_KEY1 = '"+ADJUSTMENTKEY+"' AND TRAN_KEY2 ='"+ADJUSTMENTLINENUMBER+"'";
				try
				{
					WmsWebuiValidationUpdateImpl.update(mySql);	
				} 
				catch (DPException e1)
				{
					e1.printStackTrace();
					throw new UserException("WMEXP_RECEIPT_DUP_SAVE", new Object[1]);
				}
			}*/
			for(int i=0; i < focus.size(); i++ ){
				Bio bio = focus.elementAt(i);
				bio.set("BILL_CHECK", 1);
				bio.set("BILL_CHECK_UID", userLoginId);
				bio.set("BILL_CHECK_DT", new GregorianCalendar());
			}
			uowb.saveUOW();
		}
		//return 0; //SRG
		return RET_CONTINUE; //SRG		
	}
}
