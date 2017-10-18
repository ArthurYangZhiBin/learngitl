package com.ssaglobal.scm.wms.wm_adjustment.ui;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.epiphany.shr.ui.action.ActionExtensionBase;

public class AdjustmentChargeInfoUpdate extends ActionExtensionBase {
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
		//SRG 12/16/2010: -- Start
		/*RuntimeListFormInterface form1 = (RuntimeListFormInterface) state.getCurrentRuntimeForm().getParentForm(state);		
		String ADJUSTMENTKEY=null;
		String ADJUSTMENTLINENUMBER=null;
		Config conf  = SsaAccessBase.getConfig("cognosClient","defaults");
		Configuration wmDateConfig = conf.getConfiguration();		
		String ejbDateFormat = wmDateConfig.getString("parm");
		_log.debug("LOG_SYSTEM_OUT","ejbDateFormat = "+ ejbDateFormat,100L);*/
		//SRG 12/16/2010: -- End

		//SRG 12/16/2010: Instead of making direct updates use OA framework to execute update statements
		/*if(form1.isListForm()){ 
			BioCollectionBean focus  = (BioCollectionBean) form1.getFocus();
			form1.getName();			
			if (focus.size() >0)
			{

				Bio bio = focus.elementAt(0);
				ADJUSTMENTKEY = bio.get("TRAN_KEY1").toString();
				ADJUSTMENTLINENUMBER = bio.get("TRAN_KEY2").toString();
			}

			for (int i = 0; i < focus.size(); i++){

				//Get List Widget
				Bio bio = focus.elementAt(i);
				String ChargeCommentValue =  (String)bio.get("CHARGE_COMMENT",false);				
				String ChargeCodeValue =  (String)bio.get("CHARGE_CODE",false);
				String BillToValue =  (String)bio.get("BILL_TO_CUST",false).toString().toUpperCase();	//SRG
				double  QtyValue =  NumericValidationCCF.parseNumber( bio.get("CHARGE_QTY",false).toString());
				double RateValue =  NumericValidationCCF.parseNumber(bio.get("CHARGE_RATE",false).toString());
				String UOMValue =  (String)bio.get("UOM_CODE",false);
				String ChargeFlagValue =  (String)bio.get("CHARGE_FLAG",false);				

				if((ChargeFlagValue ==  null) || (ChargeFlagValue.equalsIgnoreCase(" ")) || (!ChargeFlagValue.equalsIgnoreCase("1")) ){
					ChargeFlagValue = "0";
				}
				if(ChargeCommentValue == null){
					ChargeCommentValue = " ";
				}
				//SRG.b
				verifyStorer(BillToValue, 4);
				//SRG.e
				String mySql = "UPDATE BILL_CHARGEINSTRUCT SET BILL_TO_CUST = '"+BillToValue+"', UOM_CODE = '"+UOMValue+"', CHARGE_FLAG = "+ChargeFlagValue+", CHARGE_RATE = "+RateValue+", CHARGE_QTY = "+QtyValue+", CHARGE_COMMENT = '"+ChargeCommentValue+"'";
				String WhereClause = " Where TYPE = 'AJ' and  TRAN_KEY1 = '"+ADJUSTMENTKEY+"'AND TRAN_KEY2 ='"+ADJUSTMENTLINENUMBER+"'AND CHARGE_CODE ='"+ChargeCodeValue+"' ";
				
				try
				{
					WmsWebuiValidationUpdateImpl.update(mySql+WhereClause);	
				} 
				catch (DPException e1)
				{
					e1.printStackTrace();
					throw new UserException("WMEXP_RECEIPT_DUP_SAVE", new Object[1]);
				}				

			}			

		}*/
		//SRG 12/16/2010: -- Start
		RuntimeListFormInterface form1 = (RuntimeListFormInterface) state.getCurrentRuntimeForm().getParentForm(state);
		if(form1.isListForm()){ 
			BioCollectionBean focus  = (BioCollectionBean) form1.getFocus();
			form1.getName();
			for (int i = 0; i < focus.size(); i++){
				Bio bio = focus.elementAt(i);
				String BillToValue =  (String)bio.get("BILL_TO_CUST",false).toString().toUpperCase();				
				verifyStorer(BillToValue, 4);				
			}
		}		
		uowb.saveUOW();
		//return 0;
		return RET_CONTINUE;
		//SRG 12/15/2010: -- End
	}
	
	public void verifyStorer(String attributeValue, int type) throws DPException, UserException {
		attributeValue = attributeValue == null ? null : attributeValue.toString().toUpperCase();
		String query = "SELECT * FROM STORER WHERE (STORERKEY = '"+attributeValue+"') AND (TYPE = '"+type+"')";
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		if (results.getRowCount() != 1) {
			String[] parameters = new String[1];
			parameters[0] = attributeValue;
			throw new UserException("WMEXP_INVALID_BILLTOCUST", parameters);
		}
	}
}
