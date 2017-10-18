/*******************************************************************************
 *                         NOTICE                            
 *                                                                                
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS             
 * CONFIDENTIAL INFORMATION OF INFOR AND/OR ITS AFFILIATES   
 * OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED WITHOUT PRIOR  
 * WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND       
 * ADAPT THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH  
 * THE TERMS OF THEIR SOFTWARE LICENSE AGREEMENT.            
 * ALL OTHER RIGHTS RESERVED.                                                     
 *                                                           
 * (c) COPYRIGHT 2009 INFOR.  ALL RIGHTS RESERVED.           
 * THE WORD AND DESIGN MARKS SET FORTH HEREIN ARE            
 * TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR          
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS        
 * RESERVED.  ALL OTHER TRADEMARKS LISTED HEREIN ARE         
 * THE PROPERTY OF THEIR RESPECTIVE OWNERS.                  
 *******************************************************************************/
package com.ssaglobal.scm.wms.wm_receiptreversal.ui;

// Import 3rd party packages and classes
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Import Epiphany packages and classes
import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.data.bio.Query;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationDeleteImpl;

public class ReceiptReversalASNAutoLoad extends ActionExtensionBase{
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		//Initialize Local Variables
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
		String asnValue = currentForm.getFormWidgetByName("RECEIPTKEY").getDisplayValue();
		QBEBioBean qbe = (QBEBioBean)currentForm.getFocus();
		
		//If input value is not null
		if(asnValue!=null){
			asnValue = asnValue.toUpperCase();
			currentForm.getFormWidgetByName("RECEIPTKEY").setValue(asnValue);
			//Verify ASN Number Exists
			String queryString = "receipt.RECEIPTKEY='"+asnValue+"'";
			Query qry = new Query("receipt", queryString, null);
			BioCollectionBean list = uowb.getBioCollectionBean(qry);
			if(list.size()!=1){
				String[] parameters = new String[1];
				parameters[0]= colonStrip(readLabel(currentForm.getFormWidgetByName("RECEIPTKEY")));
				throw new FormException("WMEXP_SO_ILQ_Invalid", parameters);
			}
			//Verify ASN Number Status
			// 2012-08-14
			// Modified by Will Pu
			// 新增需求，已上传SAP(status='11')状态的也可以回转收货
			String status = list.get("0").get("STATUS").toString();
			if(!status.equals("5")&&!status.equals("9")&&!status.equals("11")){
				String[] parameters = new String[1];
				parameters[0]= colonStrip(readLabel(currentForm.getFormWidgetByName("RECEIPTKEY")));
				throw new FormException("WMEXP_RR_INVALIDSTATUS", parameters);
			}
			
			//Set Owner
			qbe.set("STORERKEY", list.get("0").get("STORERKEY").toString());
			
			//Clear Temp Table
			String deleteString = "DELETE FROM RR_TEMP WHERE SERIALKEY LIKE '%'";
			WmsWebuiValidationDeleteImpl.delete(deleteString);
			
			//Run Receipt Reversal Stored Procedure
			WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
			Array params = new Array(); 
			params.add(new TextData(asnValue));
			actionProperties.setProcedureParameters(params);
			actionProperties.setProcedureName("NSP_RECEIPTREVERSAL");
			try {
				WmsWebuiActionsImpl.doAction(actionProperties);
			}catch (WebuiException e) {
				throw new UserException(e.getMessage(), new Object[] {});
			}
			
			//Set Retail Bio Collection
			Query query = new Query("wm_rr_temp", "", null);
			result.setFocus(uowb.getBioCollectionBean(query));
			context.setNavigation("changeEvent78");
		}else{
			//Reset owner
			qbe.set("STORERKEY", null);
			context.setNavigation("changeEvent79");
		}
		return RET_CONTINUE;
	}
	
	private String colonStrip(String label){
		Pattern pattern = Pattern.compile("\\:");
		Matcher matcher = pattern.matcher(label);
		return 	matcher.replaceAll("");
	}
	
	private String readLabel(RuntimeFormWidgetInterface widgetName){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widgetName.getLabel("label",locale);
	}
}