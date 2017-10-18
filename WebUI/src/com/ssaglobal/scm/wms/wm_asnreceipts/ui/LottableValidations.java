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
package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

//Import 3rd party packages and classes

//Import Epiphany packages and classes
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.extensions.ExtensionBaseclass;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;	//Incident3268898_Defect275563

public class LottableValidations {
	public LottableValidations(){
	}
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LottableValidations.class);	//Incident3268898_Defect275563
	public static void validateDetails(UnitOfWorkBean uowb, BioBean bio, boolean flag) throws EpiException {
		//Method available to other classes, initiates lottable validations for flag delimited sets
		BioCollectionBean list = (BioCollectionBean)bio.get("RECEIPTDETAILS");
		for(int index=0; index<list.size(); index++) {
			BioBean currentBio = list.get(index+"");
			if(flag) {
				//Lottable validations for LPN Details
				BioCollectionBean lpnList = (BioCollectionBean)currentBio.get("LPNDETAILS");
				for(int index2=0; index2<lpnList.size(); index2++){
					BioBean lpnCurrentBio = lpnList.get(index2+"");
					executeValidations(uowb, bio, lpnCurrentBio, flag);
				}
			} else {
				//Lottable validations for Receipt Details
				executeValidations(uowb, bio, currentBio, flag);
			}
		}
	}
	
	private static void executeValidations(UnitOfWorkBean uowb, BioBean bio, BioBean currentBio, boolean flag) throws EpiException{
		String lvKey = getFieldValueFromItem(currentBio.getValue("STORERKEY").toString(), currentBio.getValue("SKU").toString(), "LOTTABLEVALIDATIONKEY", uowb);
		BioCollectionBean lvList = getLottableValidation(lvKey, uowb, false);
		checkLottableValidations(lvList, bio, currentBio, uowb, flag);
	}
	
	private static void checkLottableValidations(BioCollectionBean lvList, BioBean bio, BioBean currentBio, UnitOfWorkBean uowb, boolean flag) throws EpiException {
		Object olottable01=null, olottable02=null, olottable03=null, olottable04=null, olottable05=null, 
		olottable06=null, olottable07=null, olottable08=null, olottable09=null, olottable10 = null;
		//Incident3268898_Defect27556.b
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		String db_type = userContext.get(SetIntoHttpSessionAction.DB_TYPE).toString();
		//Incident3268898_Defect27556.e
		String[] lottables = new String[10];
		String lvKey = lvList.get("0").getValue("LOTTABLEVALIDATIONKEY").toString();
		if(bio.get("ALLOWAUTORECEIPT")==null) {
			bio.set("ALLOWAUTORECEIPT", "0");
		}
		olottable01 = currentBio.getValue("LOTTABLE01");
		olottable02 = currentBio.getValue("LOTTABLE02");
		olottable03 = currentBio.getValue("LOTTABLE03");
		//Incident3268898_Defect275563		olottable04 = currentBio.getValue("LOTTABLE04");
		//Incident3268898_Defect275563		olottable05 = currentBio.getValue("LOTTABLE05");
		//Incident3268898_Defect275563.b
		//When sending dates as parametrs to a stored procedure call, appserver expects it to be in a certain date format
		//and it is different for MSS and ORACLE.
		olottable04 = currentBio.getValue("LOTTABLE04");
		olottable05 = currentBio.getValue("LOTTABLE05");

		Calendar olottable04_cal = (Calendar)currentBio.getValue("LOTTABLE04");
		Calendar olottable05_cal = (Calendar)currentBio.getValue("LOTTABLE05");
		_log.debug("LOG_SYSTEM_OUT","**********db_type = "+ db_type ,100L);
		if (db_type.equalsIgnoreCase("MSS")){
			//if DB type is MS SQLSERVER the date string will be sent as CONVERT(DATETIME,'2006-02-02 00:03:26:344',21)
			if (olottable04 != null) {
				olottable04 = "CONVERT(DATETIME,'"+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(olottable04_cal.getTime())+"', 21)";
				_log.debug("LOG_SYSTEM_OUT","MSS**********olottable04 = "+ olottable04.toString() ,100L);
			}
			if (olottable05 != null) {
				olottable05 = "CONVERT(DATETIME,'"+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(olottable05_cal.getTime())+"', 21)";
				_log.debug("LOG_SYSTEM_OUT","MSS**********olottable05 = "+ olottable05.toString() ,100L);

			}
		}else{
			//if DB type is oracel the date string will be sent as TO_DATE('2010-06-21 00:00:00', 'DD-MON-YYYY HH24:MI:SS')
			if (olottable04 != null) {
				olottable04 = "TO_DATE('"+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(olottable04_cal.getTime())+"','YYYY-MM-DD HH24:MI:SS')";
				_log.debug("LOG_SYSTEM_OUT","ORACLE**********olottable04 = "+ olottable04 ,100L);
			}
			if (olottable05 != null) {
				olottable05 = "TO_DATE('"+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(olottable05_cal.getTime())+"','YYYY-MM-DD HH24:MI:SS')";
				_log.debug("LOG_SYSTEM_OUT","ORACLE**********olottable05 = "+ olottable05 ,100L);
			}
		}
		//Incident3268898_Defect275563.e
		olottable06 = currentBio.getValue("LOTTABLE06");
		olottable07 = currentBio.getValue("LOTTABLE07");
		olottable08 = currentBio.getValue("LOTTABLE08");
		olottable09 = currentBio.getValue("LOTTABLE09");
		olottable10 = currentBio.getValue("LOTTABLE10");
		String receiptLineNumber = currentBio.getValue("RECEIPTLINENUMBER").toString();
		String owner = currentBio.getValue("STORERKEY").toString();
		String item = currentBio.getValue("SKU").toString();

		String lot1man = lvList.get("0").getValue("LOTTABLE01ONRFRECEIPTMANDATORY").toString();
		String lot2man = lvList.get("0").getValue("LOTTABLE02ONRFRECEIPTMANDATORY").toString();
		String lot3man = lvList.get("0").getValue("LOTTABLE03ONRFRECEIPTMANDATORY").toString();
		String lot4man = lvList.get("0").getValue("LOTTABLE04ONRFRECEIPTMANDATORY").toString();
		String lot5man = lvList.get("0").getValue("LOTTABLE05ONRFRECEIPTMANDATORY").toString();
		String lot6man = lvList.get("0").getValue("LOTTABLE06ONRFRECEIPTMANDATORY").toString();
		String lot7man = lvList.get("0").getValue("LOTTABLE07ONRFRECEIPTMANDATORY").toString();
		String lot8man = lvList.get("0").getValue("LOTTABLE08ONRFRECEIPTMANDATORY").toString();
		String lot9man = lvList.get("0").getValue("LOTTABLE09ONRFRECEIPTMANDATORY").toString();
		String lot10man = lvList.get("0").getValue("LOTTABLE10ONRFRECEIPTMANDATORY").toString();
		Array params = new Array(); 
		boolean expFound = false;
		if ((lot1man.equalsIgnoreCase("1"))&&((olottable01 == null) || (olottable01.toString().trim().equals("")))){
			expFound = true;
			params.add("Lottable01");
		}
		if ((lot2man.equalsIgnoreCase("1"))&&((olottable02 == null) || (olottable02.toString().trim().equals("")))){
			expFound = true;
			params.add("Lottable02");
		}
		if ((lot3man.equalsIgnoreCase("1"))&&((olottable03 == null) || (olottable03.toString().trim().equals("")))){
			expFound = true;
			params.add("Lottable03");
		}
		if ((lot4man.equalsIgnoreCase("1"))&&((olottable04 == null) || (olottable04.toString().trim().equals("")))){
			expFound = true;
			params.add("Lottable04");
		}
		if ((lot5man.equalsIgnoreCase("1"))&&((olottable05 == null) || (olottable05.toString().trim().equals("")))){
			expFound = true;
			params.add("Lottable05");
		}
		if ((lot6man.equalsIgnoreCase("1"))&&((olottable06 == null) || (olottable06.toString().trim().equals("")))){
			expFound = true;
			params.add("Lottable06");
		}
		if ((lot7man.equalsIgnoreCase("1"))&&((olottable07 == null) || (olottable07.toString().trim().equals("")))){
			expFound = true;
			params.add("Lottable07");
		}
		if ((lot8man.equalsIgnoreCase("1"))&&((olottable08 == null) || (olottable08.toString().trim().equals("")))){
			expFound = true;
			params.add("Lottable08");			
		}
		if ((lot9man.equalsIgnoreCase("1"))&&((olottable09 == null) || (olottable09.toString().trim().equals("")))){
			expFound = true;
			params.add("Lottable09");
		}
		if ((lot10man.equalsIgnoreCase("1"))&&((olottable10 == null) || (olottable10.toString().trim().equals("")))){
			expFound = true;
			params.add("Lottable10");
		}
		if(expFound) {
			//Encountered exception, build error message
			String missingLottables="";
			for(int i=1; i<=params.size() ; i++){
				missingLottables = i==params.size() ? missingLottables+params.get(i)+":" : missingLottables+params.get(i)+", ";
			}
			String strMsg = "";
			String[] errorParam = new String[3];
			errorParam[0]= receiptLineNumber;
			errorParam[1] = owner;
			errorParam[2] = item;
			strMsg = flag ? missingLottables + ExtensionBaseclass.getTextMessage("WMEXP_LOTTABLE_LPNDETAIL_REQ", errorParam, getLocale()) 
					: missingLottables + ExtensionBaseclass.getTextMessage("WMEXP_LOTTABLE_REQUIRED", errorParam, getLocale());
			throw new UserException(strMsg, new Object[]{});
		}

		lottables[0] =  olottable01 != null ? olottable01.toString() : "";
		lottables[1] =  olottable02 != null ? olottable02.toString() : "";
		lottables[2] =  olottable03 != null ? olottable03.toString() : "";
		lottables[3] =  olottable04 != null ? olottable04.toString() : "";
		lottables[4] =  olottable05 != null ? olottable05.toString() : "";
		lottables[5] =  olottable06 != null ? olottable06.toString() : "";
		lottables[6] =  olottable07 != null ? olottable07.toString() : "";
		lottables[7] =  olottable08 != null ? olottable08.toString() : "";
		lottables[8] =  olottable09 != null ? olottable09.toString() : "";
		lottables[9] =  olottable10 != null ? olottable10.toString() : "";

		BioCollectionBean lvDetailList =  getLottableValidation(lvKey, uowb, true);
		String onReceiptCopyPackkey = getFieldValueFromItem(owner, item, "ONRECEIPTCOPYPACKKEY", uowb);
		processLottables(lvDetailList, owner, item, lottables, onReceiptCopyPackkey);

	}
	
	private static void processLottables(BioCollectionBean lvDetailList, String owner, String sku, String[] lottables, String onReceiptCopyPackkey)throws EpiException {
		int numLottables = 10;
		int currentLottable=0;
		String lottableName = "";
		
		for(int i=0; i< lvDetailList.size(); i++) {
			for(int index=0; index<20; index++){
				String name = index%10==9 ? "LOTTABLE"+((index%10)+1)+"RECEIPT" : "LOTTABLE0"+((index%10)+1)+"RECEIPT";
				name = index<10 ? name+"VALIDATION" : name+"CONVERSION"; 
				String procName = lvDetailList.elementAt(i).get(name).toString();
				if(!procName.equalsIgnoreCase("NONE")){
					currentLottable = (index%numLottables)+1;
					lottableName = currentLottable<10 ? "Lottable0"+currentLottable : "Lottable"+currentLottable;
					if((lottableName.equalsIgnoreCase("Lottable01")) && (onReceiptCopyPackkey.equalsIgnoreCase("1"))) {
						procName = "NONE";
					}
					
					//Call Stored procedure for the Lottable.
					WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
					Array params = new Array(); 
					params.add(new TextData("TRUE"));
					params.add(new TextData(lottableName));
					for(int index2=0; index2<10; index2++) {
						params.add(new TextData(lottables[index2]));
					}
					params.add(new TextData(owner));
					params.add(new TextData(sku));

					actionProperties.setProcedureParameters(params);
					actionProperties.setProcedureName(procName);
					try {
						WmsWebuiActionsImpl.doAction(actionProperties);
					} catch(WebuiException e) {
						throw new UserException(e.getMessage(), new Object[]{});
					}
				}
			}
		}
	}
	
	private static BioCollectionBean getLottableValidation(String lvKey, UnitOfWorkBean uowb, boolean isDetail) throws EpiException{
		//Query for lottable validation records
		String table = isDetail ? "wm_lottablevalidationdetail" : "wm_lottablevalidation";
		String qry = table+".LOTTABLEVALIDATIONKEY='"+lvKey+"'";
		Query query = new Query(table, qry, null);
		BioCollectionBean listCollection = uowb.getBioCollectionBean(query);
		if (listCollection.size()!= 0){
			return listCollection;
		} else {
			return null;
		}
	}
		
	private static String getFieldValueFromItem(String owner, String item, String fieldName, UnitOfWorkBean uowb) throws EpiException {
		//Query for item records
		String qry = "sku.STORERKEY='"+owner+"' AND  sku.SKU='"+item+"'";
		Query BioQuery = new Query("sku", qry, null);
		BioCollectionBean listCollection = uowb.getBioCollectionBean(BioQuery);
		if(listCollection.size()!= 0) {
			return listCollection.get("0").get(fieldName).toString();
		} else {
			return "";
		}
	}
	
	private static LocaleInterface getLocale() {
		//Find locale
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		return mda.getLocale(userLocale);
	}
}