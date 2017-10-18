package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;

import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
//import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
//import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.ssaglobal.SsaException;
import com.ssaglobal.scm.wms.datalayer.EJBRemote;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.service.exeprocessmanager.TransactionServiceSORemote;
import com.ssaglobal.scm.wms.wm_table_validation.ccf.NumericValidationCCF;

public class ValidateBeforeSaveAppend extends com.epiphany.shr.ui.action.ActionExtensionBase {
	protected int execute( ActionContext context, ActionResult result ) throws EpiException, UserException {
//		System.out.println("it is in HeaderDetailSave******DDDDkkkkkkkkkkkvvvvvv"); 		
		String shellSlot1 = "list_slot_1";
		String shellSlot2 = "list_slot_2";
		
		StateInterface state = context.getState();
//		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();				//get the toolbar
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);				//get the Shell form
		SlotInterface headerSlot = shellForm.getSubSlot(shellSlot1);				//Get slot1
		SlotInterface detailSlot = shellForm.getSubSlot(shellSlot2);				//Get slot2
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);	//Get form in slot1
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);	//Get form in slot1

		DataBean headerFocus = headerForm.getFocus();								//Get the header form focus
		
		String[] process = new String[1];

		String tid = UUID.randomUUID().toString();
		String storerkey =  "";
		try{
			BioBean bio = (BioBean)headerFocus;	
			storerkey = (String)bio.get("STORERKEY");
		}catch (Exception e){
			QBEBioBean bio = (QBEBioBean)headerFocus;
			storerkey = (String)bio.get("STORERKEY");
		}
		
		HttpSession session = state.getRequest().getSession();
		String db_connection = session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();
		String wmwhseid = session.getAttribute(SetIntoHttpSessionAction.DB_USERID).toString();
		String currentConnection = session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();
		
		int isCheck = 0;
		//检查收货验证模版，判断超收是否属于硬错误
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			InitialContext initialContext = new InitialContext();
			javax.sql.DataSource dataSource = (javax.sql.DataSource) initialContext.lookup("java:jdbc/"+db_connection.toUpperCase());
			connection = dataSource.getConnection();
			statement = connection.prepareStatement("SELECT COUNT(1) FROM STORER A, RECEIPTVALIDATION B WHERE A.RECEIPTVALIDATIONTEMPLATE=B.RECEIPTVALIDATIONKEY AND B.OVERAGEHARDERROR=1 AND A.TYPE=1 AND A.STORERKEY='"+storerkey+"' ");			
			resultSet = statement.executeQuery();
			if(resultSet.next()){
				isCheck = resultSet.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
				statement.close();
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(isCheck>0){
			if (headerFocus.isBio() ) {
				SlotInterface toggleSlot = detailForm.getSubSlot("wm_receiptdetail_toggle");
				int formNumber = state.getSelectedFormNumber(toggleSlot);
				RuntimeFormInterface detail = null;
				if(formNumber==0){
					//detail = state.getRuntimeForm(toggleSlot, "wms_ASN_Line_List_View");//getSubSlot(DETAIL_LIST_SLOT), null);
			        RuntimeListFormInterface detailListTab  = (RuntimeListFormInterface) state.findForm(detailForm, "wms_ASN_Line_List_View");
			        BioCollectionBean detailListCollection = (BioCollectionBean) detailListTab.getFocus();
			        int bioListSize = detailListCollection.size();
					for(int i = 0; i < bioListSize; i++)
					{
						BioBean detailBio = (BioBean)detailListCollection.get("" + i);
						String recqty = (String)detailBio.getValue("RECEIVEDQTY");
						java.math.BigDecimal expqty = (java.math.BigDecimal)detailBio.getValue("QTYEXPECTED");
						String lineno = (String)detailBio.getValue("RECEIPTLINENUMBER");
						String receiptkey = (String)detailBio.getValue("RECEIPTKEY");
						String sku = (String)detailBio.getValue("SKU");
						
						double drecqty = 0;
						double dexpqty = 0;
						drecqty = NumericValidationCCF.parseNumber(recqty);
						dexpqty = expqty.doubleValue();
						
						String mysql = "INSERT INTO TEMP_RECEIPT (TID, RECEIPTKEY, STORERKEY, SKU, QTYRECEIVED, QTYEXPECTED)  ";
						mysql += "	VALUES ('"+tid+"', '"+receiptkey+"', '"+storerkey+"', '"+sku+"', "+recqty+", "+expqty+")  ";
						
						try {
							InitialContext initialContext = new InitialContext();
							javax.sql.DataSource dataSource = (javax.sql.DataSource) initialContext.lookup("java:jdbc/"+currentConnection.toUpperCase());
							connection = dataSource.getConnection();
							statement = connection.prepareStatement(mysql);			
							statement.executeUpdate();
							
						} catch (Exception e) {
								e.printStackTrace();
						} finally {
							try {
								resultSet.close();
								statement.close();
								connection.close();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}	
			    		
					}
					String skus = "";
					try {
						InitialContext initialContext = new InitialContext();
						javax.sql.DataSource dataSource = (javax.sql.DataSource) initialContext.lookup("java:jdbc/"+currentConnection.toUpperCase());
						connection = dataSource.getConnection();
						statement = connection.prepareStatement("SELECT SKU FROM TEMP_RECEIPT WHERE TID='"+tid+"' GROUP BY SKU, STORERKEY HAVING SUM(QTYRECEIVED)>SUM(QTYEXPECTED) ");			
						resultSet = statement.executeQuery();
						while(resultSet.next()){
							String tempSku = "";
							tempSku = resultSet.getString("SKU");
							skus += tempSku+"\t";
						}
					} catch (Exception e) {
							e.printStackTrace();
					} finally {
						try {
							resultSet.close();
							statement.close();
							connection.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}	
					if (skus.length()>0){
		        		throw new UserException("有物料超出预期收货量:"+skus, process);//明细行{0}不允许超收;
					}
				}else{
					int count = 0;
					
//					RuntimeFormInterface lotform =  state.findForm(detailForm, "wms_Lottables_Detail_view");
					RuntimeFormInterface detform =  state.findForm(detailForm, "wms_ASN_Line_Detail_view");
					DataBean detailFocus = detform.getFocus();
					String receiptkey = "";
					String sku = "";
					try{
						BioBean detailBio = (BioBean)detailFocus;	
						receiptkey = (String)detailBio.get("RECEIPTKEY");
						sku = (String)detailBio.get("SKU");
					}catch(Exception e){
						QBEBioBean detailBio = (QBEBioBean)detailFocus;	
						receiptkey = (String)detailBio.get("RECEIPTKEY");
						sku = (String)detailBio.get("SKU");
					}
					
//					RuntimeFormWidgetInterface lot08 = lotform.getFormWidgetByName("LOTTABLE08");
//					RuntimeFormWidgetInterface recqty = detform.getFormWidgetByName("RECEIVEDQTY");
//					RuntimeFormWidgetInterface expqty = detform.getFormWidgetByName("QTYEXPECTED");
//					RuntimeFormWidgetInterface lineno = detform.getFormWidgetByName("RECEIPTLINENUMBER");
//					RuntimeFormWidgetInterface sku = detform.getFormWidgetByName("SKU");
//					RuntimeFormWidgetInterface receiptkey = detform.getFormWidgetByName("RECEIPTKEY");
					
					String recqty = (String) detform.getFormWidgetByName("RECEIVEDQTY").getValue();
					try {
						InitialContext initialContext = new InitialContext();
						javax.sql.DataSource dataSource = (javax.sql.DataSource) initialContext.lookup("java:jdbc/"+currentConnection.toUpperCase());
						connection = dataSource.getConnection();
						statement = connection.prepareStatement("SELECT COUNT(1) FROM RECEIPTDETAIL WHERE RECEIPTKEY='"+receiptkey+"' AND SKU='"+sku+"' GROUP BY SKU HAVING "+recqty+">SUM(QTYEXPECTED) ");			
						resultSet = statement.executeQuery();
						if(resultSet.next()){
							count = resultSet.getInt(1);
						}
					} catch (Exception e) {
							e.printStackTrace();
					} finally {
						try {
							resultSet.close();
							statement.close();
							connection.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}	
	//				double drecqty = 0;
	//				double dexpqty = 0;
	//				drecqty = NumericValidationCCF.parseNumber(recqty.getDisplayValue());
	//				dexpqty = NumericValidationCCF.parseNumber(expqty.getDisplayValue());
					
					if (count>0){
	//	        		process[0] = lineno.getDisplayValue();
		        		throw new UserException("物料超出预期收货量:"+sku, process);//明细行{0}不允许超收;
					}
				}
			}
		}
		result.setFocus(headerFocus);
		return RET_CONTINUE;
	}
}
