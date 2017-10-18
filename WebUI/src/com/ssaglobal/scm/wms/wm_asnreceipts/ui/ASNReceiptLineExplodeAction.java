/**
 * ---Begin Copyright Notice---20090105T1353
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2010 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */
package com.ssaglobal.scm.wms.wm_asnreceipts.ui;


import java.util.ArrayList;
import java.util.Iterator;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ListSelector;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.StringUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class ASNReceiptLineExplodeAction.
 *
 * @author pmegarav
 */
public class ASNReceiptLineExplodeAction extends ListSelector {

	protected static ILoggerCategory log = LoggerFactory
	.getInstance(ASNReceiptLineExplodeAction.class);

	/**
	 * Instantiates a new aSN receipt line explode action.
	 */
	public ASNReceiptLineExplodeAction() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.epiphany.shr.ui.action.ActionExtensionBase#execute(com.epiphany.shr.ui.action.ActionContext, com.epiphany.shr.ui.action.ActionResult)
	 */
	protected int execute( ActionContext context, ActionResult result ) throws EpiException {
		/******************************************************************
		 * Programmer:        	   Prithviraj Megaravalli
		 * Created        :        13/06/2007
		 * Purpose        :       3PL Enhancements -ASN Explode - Call the relevant EJB for each selected 
		 * 				order line so that the EJB can add the reocrd to Dropship table.
		 *******************************************************************
		 * Modification History
		 *
		 ******************************************************************/
		StateInterface state = context.getState();

		String labelName = " ";
		String printerName = " ";
		String copies = " ";
		String isLabelToPrinted = "0";
		//RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		//RuntimeFormInterface toggleform = toolbar.getParentForm(state);
		//RuntimeListForm BottomForm = (RuntimeListForm) toggleform 
		RuntimeListForm BottomForm= (RuntimeListForm) FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wms_ASN_Line_List_View", state);
		ArrayList selectedItems= BottomForm.getSelectedItems();

		if(selectedItems != null && selectedItems.size() > 0)
		{
			log.debug("ASNReceiptLineExplodeAction_execute", "\n\nExecuting Action2 \n\n", SuggestedCategory.APP_EXTENSION);
			Iterator bioBeanIter = selectedItems.iterator();
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			try
			{
				BioBean bio;
				for(; bioBeanIter.hasNext();){
					log.debug("ASNReceiptLineExplodeAction_execute", "\n\nExecuting Action3 \n\n", SuggestedCategory.APP_EXTENSION);
					bio = (BioBean)bioBeanIter.next();

					String ReceiptKey = bio.getString("RECEIPTKEY");
					String ReceiptLineNumber=bio.getString("RECEIPTLINENUMBER");
					
					if(!StringUtils.isEmpty(BioAttributeUtil.getString(bio, "TOID"))) {
						throw new UserException("WMEXP_EXPLODE_LPN", new String[]{ReceiptLineNumber});
					}
						

					log.debug("ASNReceiptLineExplodeAction_execute", "\n\nRECEIPTKEY:"+ReceiptKey+"\n\n", SuggestedCategory.APP_EXTENSION);
					log.debug("ASNReceiptLineExplodeAction_execute", "\n\nRECEIPTLINENUMBER:"+ReceiptLineNumber+"\n\n", SuggestedCategory.APP_EXTENSION);
					WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
					Array parms = new Array(); 
					parms.add(new TextData(ReceiptKey));
					parms.add(new TextData(ReceiptLineNumber));
					parms.add(new TextData(labelName));
					parms.add(new TextData(printerName));
					parms.add(new TextData(copies));
					parms.add(new TextData(isLabelToPrinted));
					actionProperties.setProcedureParameters(parms);
					actionProperties.setProcedureName("NSPEXPLODEASNLINE");
					try {
						WmsWebuiActionsImpl.doAction(actionProperties);
					} catch (Exception e) {
						e.getMessage();
						UserException UsrExcp = new UserException(e.getMessage(), new Object[]{});
						throw UsrExcp;
					}
				}	

				uowb.saveUOW();
				uowb.clearState();

			}
			catch(EpiException ex)
			{
				ex.getMessage();
				throwUserException(ex, "ERROR_EXECUTING_ACTION", null);
			}
		}

		log.debug("ASNReceiptLineExplodeAction_execute", "\n\nExecuting Action10 \n\n", SuggestedCategory.APP_EXTENSION);
		return RET_CONTINUE;

	}
}
