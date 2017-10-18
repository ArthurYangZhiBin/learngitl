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

import javax.servlet.http.HttpSession;

import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.GUIDFactory;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeForm;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class GenerateReceiptDetails extends ActionExtensionBase {

    /**
     * @param context
     * @return result
     * @throws EpiException
     */
	protected int execute(ModalActionContext context, ActionResult result) throws EpiException {

		StateInterface state = context.getState();
		HttpSession session = state.getRequest().getSession();
		RuntimeForm topForm = context.getModalTopForm();
        
		String numLines = topForm.getFormWidgetByName("num_lines").getDisplayValue(); 
		
		if ( isEmpty(numLines) || !IsNumeric(numLines) )
		{
			UserException UsrExcp = new UserException("WMEXP_DUP_NUMLINES", new Object[]{});
 	   		throw UsrExcp;
		}
		int numDuplicates = Integer.parseInt(numLines);
		if(numDuplicates == 0){
			UserException UsrExcp = new UserException("WMEXP_ZERO_ENTERED", new Object[]{});
 	   		throw UsrExcp;			
		}
    	
    	// Retrieve receipt key from session
    	// ---------------------------------
		String receiptKey = session.getAttribute("RECEIPTKEY").toString();
		String receiptLineNumber = session.getAttribute("RECEIPTLINENUMBER").toString();

    	BioCollectionBean listCollection = null;
    	UnitOfWorkBean uowb = state.getDefaultUnitOfWork();	
    	
    	// Select the receipt line the user wants to duplicate
    	// ---------------------------------------------------
    	String sQueryString = "(receiptdetail.RECEIPTKEY = '"+receiptKey+"' AND receiptdetail.RECEIPTLINENUMBER = '"+receiptLineNumber+"') ";
    	Query bioQuery = new Query( "receiptdetail", sQueryString, null );
    	listCollection = uowb.getBioCollectionBean(bioQuery);
    	BioBean existingReceiptRecord = null;
    	for ( int idx = 0; idx < listCollection.size(); ++idx ) 
    	{
    		existingReceiptRecord = (BioBean)listCollection.elementAt(idx); 
            break;
    	}
    	
    	// Determine the highest line number for this receiptkey
    	// -----------------------------------------------------
    	int currentLine = 1;
    	String selectMaxLine = "SELECT MAX(RECEIPTLINENUMBER) AS LINEMAX FROM receiptdetail WHERE receiptdetail.RECEIPTKEY = '"+receiptKey+"' ";
		EXEDataObject lineMaxColletion = null;		
		try {
			lineMaxColletion = WmsWebuiValidationSelectImpl.select(selectMaxLine);		
			if(lineMaxColletion != null && lineMaxColletion.getRowCount() > 0){
				currentLine = Integer.parseInt(lineMaxColletion.getAttribValue(new TextData("LINEMAX")).toString());
			}	
		} catch (DPException e2) {
		}	
		
		for ( int i = 0; i < numDuplicates; i++ )
		{
	    	// Create a duplicate of the existing receipt line
	    	// -----------------------------------------------
	    	QBEBioBean duplicateQBE = uowb.getQBEBioWithDefaults("receiptdetail");
			currentLine++;
			String lineNum = formatLineNumber(currentLine, context);
	    	duplicateRecord(duplicateQBE, existingReceiptRecord, receiptKey, lineNum);
		}
            
    	uowb.saveUOW(true);
    	
    	// Re-query records temporary dup records to display to the user
    	// -------------------------------------------------------------
    	String query = "receiptdetail.RECEIPTKEY = '"+receiptKey+"' AND receiptdetail.WHSEID = 'DUP' ";
		Query receiptQuery = new Query("receiptdetail", query , null);
        BioCollectionBean results = uowb.getBioCollectionBean(receiptQuery);
        result.setFocus(results);

        return RET_CONTINUE;
    }
    

	private void duplicateRecord(QBEBioBean newLineQBE, BioBean origLine, String receiptKey, String lineNumber)
	{
		newLineQBE.set("RECEIPTKEY", receiptKey);
    	newLineQBE.set("STATUS", "0");
    	newLineQBE.set("RECEIPTLINENUMBER", lineNumber);

    	newLineQBE.set("WHSEID", "DUP");
    	newLineQBE.set("RECEIPTDETAILID", GUIDFactory.getGUIDStatic());
    	
    	newLineQBE.set("ALTSKU", origLine.get("ALTSKU"));
    	newLineQBE.set("CASECNT", origLine.get("CASECNT"));
    	newLineQBE.set("CONDITIONCODE", origLine.get("CONDITIONCODE"));
    	newLineQBE.set("CONTAINERKEY", origLine.get("CONTAINERKEY"));
    	newLineQBE.set("CUBE", origLine.get("CUBE"));
    	newLineQBE.set("DATERECEIVED", origLine.get("DATERECEIVED"));
    	newLineQBE.set("DISPOSITIONCODE", origLine.get("DISPOSITIONCODE"));
    	newLineQBE.set("DISPOSITIONTYPE", origLine.get("DISPOSITIONTYPE"));
    	newLineQBE.set("FORTE_FLAG", origLine.get("FORTE_FLAG"));
    	newLineQBE.set("GROSSWGT", origLine.get("GROSSWGT"));
    	newLineQBE.set("ID", origLine.get("ID"));
    	newLineQBE.set("INNERPACK", origLine.get("INNERPACK"));
    	newLineQBE.set("IPSKey", origLine.get("IPSKey"));
    	newLineQBE.set("LOTTABLE01", origLine.get("LOTTABLE01"));
    	newLineQBE.set("LOTTABLE02", origLine.get("LOTTABLE02"));
    	newLineQBE.set("LOTTABLE03", origLine.get("LOTTABLE03"));
    	newLineQBE.set("LOTTABLE04", origLine.get("LOTTABLE04"));
    	newLineQBE.set("LOTTABLE05", origLine.get("LOTTABLE05"));
    	newLineQBE.set("LOTTABLE06", origLine.get("LOTTABLE06"));
    	newLineQBE.set("LOTTABLE07", origLine.get("LOTTABLE07"));
    	newLineQBE.set("LOTTABLE08", origLine.get("LOTTABLE08"));
    	newLineQBE.set("LOTTABLE09", origLine.get("LOTTABLE09"));
    	newLineQBE.set("LOTTABLE10", origLine.get("LOTTABLE10"));
    	newLineQBE.set("LOTTABLE11", origLine.get("LOTTABLE11"));
    	newLineQBE.set("LOTTABLE12", origLine.get("LOTTABLE12"));
    	newLineQBE.set("MatchLottable", origLine.get("MatchLottable"));
    	newLineQBE.set("NETWGT", origLine.get("NETWGT"));
    	newLineQBE.set("TAREWGT", origLine.get("TAREWGT"));
    	newLineQBE.set("NOTES", origLine.get("NOTES"));
    	newLineQBE.set("OTHERUNIT1", origLine.get("OTHERUNIT1"));
    	newLineQBE.set("OTHERUNIT2", origLine.get("OTHERUNIT2"));
    	newLineQBE.set("PACKINGSLIPQTY", origLine.get("PACKINGSLIPQTY"));
    	newLineQBE.set("PACKKEY", origLine.get("PACKKEY"));
    	newLineQBE.set("PALLET", origLine.get("PALLET"));
    	newLineQBE.set("PALLETID", origLine.get("PALLETID"));
    	newLineQBE.set("POKEY", origLine.get("POKEY"));
    	newLineQBE.set("POLineNumber", origLine.get("POLineNumber"));
    	newLineQBE.set("QCAUTOADJUST", origLine.get("QCAUTOADJUST"));
    	newLineQBE.set("QCQTYINSPECTED", "0");
    	newLineQBE.set("QCQTYREJECTED", "0");
    	newLineQBE.set("QCREJREASON", origLine.get("QCREJREASON"));
    	newLineQBE.set("QCREQUIRED", origLine.get("QCREQUIRED"));
    	newLineQBE.set("QCSTATUS", origLine.get("QCSTATUS"));
    	newLineQBE.set("QCUSER", origLine.get("QCUSER"));
    	newLineQBE.set("QTYADJUSTED", "0");
    	newLineQBE.set("QTYEXPECTED", origLine.get("QTYEXPECTED"));
    	newLineQBE.set("QTYRECEIVED", "0");
    	newLineQBE.set("QTYREJECTED", "0");
    	newLineQBE.set("REASONCODE", origLine.get("REASONCODE"));
    	newLineQBE.set("RETURNCONDITION", origLine.get("RETURNCONDITION"));
    	newLineQBE.set("RETURNREASON", origLine.get("RETURNREASON"));
    	newLineQBE.set("RETURNTYPE", origLine.get("RETURNTYPE"));
    	newLineQBE.set("RMA", origLine.get("RMA"));
    	newLineQBE.set("SKU", origLine.get("SKU"));
    	newLineQBE.set("STORERKEY", origLine.get("STORERKEY"));
    	newLineQBE.set("SupplierKey", origLine.get("SupplierKey"));
    	newLineQBE.set("SupplierName", origLine.get("SupplierName"));
    	newLineQBE.set("SUSR1", origLine.get("SUSR1"));
    	newLineQBE.set("SUSR2", origLine.get("SUSR2"));
    	newLineQBE.set("SUSR3", origLine.get("SUSR3"));
    	newLineQBE.set("SUSR4", origLine.get("SUSR4"));
    	newLineQBE.set("SUSR5", origLine.get("SUSR5"));
    	newLineQBE.set("TARIFFKEY", origLine.get("TARIFFKEY"));
    	newLineQBE.set("TOLOC", origLine.get("TOLOC"));
    	newLineQBE.set("TOLOT", origLine.get("TOLOT"));
    	newLineQBE.set("TYPE", origLine.get("TYPE"));
    	newLineQBE.set("UNITPRICE", origLine.get("UNITPRICE"));
    	newLineQBE.set("UOM", origLine.get("UOM"));
    	newLineQBE.set("VESSELKEY", origLine.get("VESSELKEY"));
    	newLineQBE.set("VOYAGEKEY", origLine.get("VOYAGEKEY"));
    	newLineQBE.set("XDOCKKEY", origLine.get("XDOCKKEY"));
	}
	

    private String formatLineNumber(int currentLine,ModalActionContext context)throws EpiException{
    	String lineNumber = "";
		
		String zeroPadding=null;
		String sQueryString = "(wm_system_settings.CONFIGKEY = 'ZEROPADDEDKEYS')";
		Query bioQuery = new Query("wm_system_settings",sQueryString,null);
		UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();
		BioCollectionBean selCollection = uowb.getBioCollectionBean(bioQuery);
		try {
			zeroPadding = selCollection.elementAt(0).get("NSQLVALUE").toString();
		} catch (EpiDataException e1) {
			e1.printStackTrace();
		}
		if (zeroPadding.equalsIgnoreCase("1")){
			if(currentLine<10000){
				lineNumber+="0";
				if(currentLine<1000){
					lineNumber+="0";
					if(currentLine<100){
						lineNumber+="0";
						if(currentLine<10){
							lineNumber+="0";
						}
					}
				}
			}
		}
		lineNumber+=currentLine;
		return lineNumber;
	}
    
	private boolean isEmpty(Object attributeValue) throws EpiDataException
	{

		if (attributeValue == null)
		{
			return true;
		}
		else if (attributeValue.toString().matches("\\s*"))
		{
			return true;
		}
		else if (attributeValue.toString().equalsIgnoreCase("null"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	private Boolean IsNumeric(String val)
	{
		Boolean retVal = Boolean.TRUE;
		
		try
		{
			Integer.parseInt(val);
		}
		catch (NumberFormatException e)
		{
			retVal = Boolean.FALSE;
		}
		
		return retVal;
	}
}