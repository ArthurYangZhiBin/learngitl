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

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.text.DecimalFormat;
import java.util.ArrayList;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.uiextensions.KeyGenBioWrapper;
import com.ssaglobal.scm.wms.util.BioUtil;
import com.ssaglobal.scm.wms.util.FormUtil;

public class CatchWeightDataDefaultValues extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	public static final String TYPE_INBOUND = "inbound";

	public static final String TYPE_OUTBOUND = "outbound";

	protected static ILoggerCategory _log = LoggerFactory.getInstance(CatchWeightDataDefaultValues.class);

	private static String KEYTEMPLATE = "00000";

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @exception EpiException
	 */
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		DataBean newCWCD = result.getFocus();

		//get Parent Records
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		RuntimeFormInterface receiptForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "receipt_detail_view", state);
		DataBean receiptFocus = receiptForm.getFocus();

		ArrayList<String> tabs = new ArrayList<String>();
		tabs.add("wm_receiptdetail_detail_view");
		tabs.add("tab 0");
		RuntimeFormInterface receiptDetailForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wms_ASN_Line_Detail_view", tabs, state);
		DataBean receiptDetailFocus = receiptDetailForm.getFocus();

		//Key Stuff
		String receiptKey = getReceiptKey(receiptFocus);
		String receiptLineNumber = getReceiptLineNumber(receiptDetailFocus);

		BioCollectionBean rs;
		setLOTXIDKEY(newCWCD, uow, receiptKey, receiptLineNumber, TYPE_INBOUND);
		setLOTXIDLINENUMBER(newCWCD, uow, receiptKey, receiptLineNumber, TYPE_INBOUND);

		//set Defaults
		newCWCD.setValue("SOURCEKEY", receiptKey);
		newCWCD.setValue("SOURCELINENUMBER", receiptLineNumber);
		newCWCD.setValue("LOT", "");
		newCWCD.setValue("ID", getLPN(receiptDetailFocus));
		newCWCD.setValue("IQTY", "1");
		//		newCWCD.setValue("CASEID", value);
		//		newCWCD.setValue("CAPTUREBY", value);
		//		newCWCD.setValue("ETWEIGHT", "0");
		//		newCWCD.setValue("WGT", "0");

		//set Required
		newCWCD.setValue("SKU", receiptDetailFocus.getValue("SKU"));

		//Set CaptureBy
		rs = uow.getBioCollectionBean(new Query("sku", "sku.STORERKEY = '" + receiptDetailFocus.getValue("STORERKEY") + "' and sku.SKU = '" + receiptDetailFocus.getValue("SKU") + "'", null));
		if (rs.size() == 0)
		{
			throw new UserException("WMEXP_VALIDATESKU", new Object[] { receiptDetailFocus.getValue("SKU"), receiptDetailFocus.getValue("STORERKEY") });
		}
		newCWCD.setValue("CAPTUREBY", rs.get("0").getValue("ICWBY"));
		newCWCD.setValue("PACK", rs.get("0").getValue("PACKKEY"));

		//set inbound
		newCWCD.setValue("IOFLAG", "I");

		_log.debug("LOG_DEBUG_EXTENSION_CatchWeightDataDefaultValues_execute", BioUtil.getBioAsString(newCWCD, uow.getBioMetadata(newCWCD.getDataType())), SuggestedCategory.NONE);
		
		result.setFocus(newCWCD);
		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}

	private Object getLPN(DataBean receiptDetailFocus) 
	{
		return receiptDetailFocus.getValue("TOID") == null ? " " : receiptDetailFocus.getValue("TOID");
	}

	public static String generateLineNumber(int max, UnitOfWorkBean uow) throws UserException
	{
		String lineNumber;
		String zeroPadding = null;
		String sQueryString = "(wm_system_settings.CONFIGKEY = 'ZEROPADDEDKEYS')";
		Query bioQuery = new Query("wm_system_settings", sQueryString, null);
		BioCollectionBean selCollection = uow.getBioCollectionBean(bioQuery);
		try
		{
			zeroPadding = selCollection.elementAt(0).get("NSQLVALUE").toString();
		} catch (EpiDataException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DecimalFormat template = null;
		if (zeroPadding.equalsIgnoreCase("0"))
		{
			template = new DecimalFormat("0");
		}
		else
		{
			template = new DecimalFormat(KEYTEMPLATE);
		}

		max += 1;
		if (max > 99999)
		{
			throw new UserException("WMEXP_VALUE_GEN_FAIL", new Object[] {});
		}
		else
		{
			lineNumber = template.format(max);
		}

		return lineNumber;
	}

	public static void setLOTXIDKEY(DataBean newCWCD, UnitOfWorkBean uow, String receiptKey, String receiptLineNumber, String type) throws EpiDataException
	{
		//is there an existing lotxidheader record  
		//if so, use it's value for lotxidkey
		//if not, keygen lotxidkey
		String query = null;
		if (TYPE_INBOUND.equals(type))
		{
			//inbound
			query = "lotxiddetail.SOURCEKEY = '" + receiptKey + "' and lotxiddetail.SOURCELINENUMBER = '" + receiptLineNumber + "' and lotxiddetail.IOFLAG = 'I'";
		}
		else
		{
			//outbound
			query = "lotxiddetail.SOURCEKEY = '" + receiptKey + "' and lotxiddetail.SOURCELINENUMBER = '" + receiptLineNumber + "' and lotxiddetail.IOFLAG = 'O'";
		}
		BioCollectionBean rs = uow
				.getBioCollectionBean(new Query("lotxiddetail", query, null));
		if (rs.size() == 0)
		{
			//keygen
			newCWCD.setValue("LOTXIDKEY", new KeyGenBioWrapper().getKey("lotxidheader"));
		}
		else
		{
			//existing value
			newCWCD.setValue("LOTXIDKEY", rs.get("0").getValue("LOTXIDKEY"));
		}
	}

	
	//used only by SO
	public static void setLOTXIDKEY(DataBean newCWCD, UnitOfWorkBean uow, String receiptKey, String receiptLineNumber, String type, String pickDetailKey) throws EpiDataException
	{
		//is there an existing lotxidheader record  
		//if so, use it's value for lotxidkey
		//if not, keygen lotxidkey
		String query = null;
		if (TYPE_INBOUND.equals(type))
		{
			//inbound
			query = "lotxiddetail.SOURCEKEY = '" + receiptKey + "' and lotxiddetail.SOURCELINENUMBER = '" + receiptLineNumber + "' and lotxiddetail.IOFLAG = 'I'"+" and lotxiddetail.PICKDETAILKEY = '"+pickDetailKey+"'";
		}
		else
		{
			//outbound
			query = "lotxiddetail.SOURCEKEY = '" + receiptKey + "' and lotxiddetail.SOURCELINENUMBER = '" + receiptLineNumber + "' and lotxiddetail.IOFLAG = 'O'"+" and lotxiddetail.PICKDETAILKEY = '"+pickDetailKey+"'";
		}
		BioCollectionBean rs = uow
				.getBioCollectionBean(new Query("lotxiddetail", query, null));
		if (rs.size() == 0)
		{
			//keygen
			newCWCD.setValue("LOTXIDKEY", new KeyGenBioWrapper().getKey("lotxidheader"));
		}
		else
		{
			//existing value
			newCWCD.setValue("LOTXIDKEY", rs.get("0").getValue("LOTXIDKEY"));
		}
	}
	
	
	public static void setLOTXIDLINENUMBER(DataBean newCWCD, UnitOfWorkBean uow, String receiptKey, String receiptLineNumber, String type) throws EpiDataException, UserException
	{
		BioCollectionBean rs;
		//is there an existing lotxiddetail record
		String query = null;
		if(TYPE_INBOUND.equals(type))
		{
			query = "lotxiddetail.SOURCEKEY = '" + receiptKey + "' and lotxiddetail.SOURCELINENUMBER = '" + receiptLineNumber + "' and lotxiddetail.IOFLAG = 'I'";
		}
		else
		{
			query = "lotxiddetail.SOURCEKEY = '" + receiptKey + "' and lotxiddetail.SOURCELINENUMBER = '" + receiptLineNumber + "' and lotxiddetail.IOFLAG = 'O'";
		}
		rs = uow.getBioCollectionBean(new Query("lotxiddetail", query, null));
		//if so, increment it's value for lotxidlinenumber
		//else create
		int max;
		if (rs.size() == 0)
		{
			max = 0;
		}
		else
		{
			max = Integer.parseInt(rs.max("LOTXIDLINENUMBER").toString());
		}
		newCWCD.setValue("LOTXIDLINENUMBER", generateLineNumber(max, uow));
	}

	private String getReceiptLineNumber(DataBean receiptDetailFocus) throws UserException
	{
		final Object receiptLineNumberObject = receiptDetailFocus.getValue("RECEIPTLINENUMBER");
		if (receiptLineNumberObject == null)
		{
			throw new UserException("WMEXP_LOTXIDDETAIL_RECEIPTLINE", new Object[] {});
		}
		return receiptLineNumberObject.toString();
	}

	private String getReceiptKey(DataBean receiptFocus) throws UserException
	{
		final Object receiptKeyObject = receiptFocus.getValue("RECEIPTKEY");
		if (receiptKeyObject == null)
		{
			throw new UserException("WMEXP_LOTXIDDETAIL_RECEIPTKEY", new Object[] {});
		}
		return receiptKeyObject.toString();
	}

}
