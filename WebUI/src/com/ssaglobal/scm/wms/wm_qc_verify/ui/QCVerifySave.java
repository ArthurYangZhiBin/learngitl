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


package com.ssaglobal.scm.wms.wm_qc_verify.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.text.NumberFormat;
import java.text.ParseException;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class QCVerifySave extends com.epiphany.shr.ui.action.ActionExtensionBase
{

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
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();

		//Get ContainerID from Search
		RuntimeFormInterface searchForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wm_qc_verify_template",
															"wm_qc_verify_search_view", state);

		String containerID = (String) searchForm.getFormWidgetByName("CONTAINERID").getDisplayValue().toUpperCase();
		//Get Item and Lot from Entry
		RuntimeFormInterface entryForm = state.getCurrentRuntimeForm();
		String item = ((String) entryForm.getFormWidgetByName("ITEM").getDisplayValue()).toUpperCase();
		String lot = ((String) entryForm.getFormWidgetByName("LOTNUMBER").getDisplayValue()).toUpperCase();
		String qty = (String) entryForm.getFormWidgetByName("QUANTITY").getDisplayValue();

		if (isEmpty(item) || isEmpty(qty))
		{
			throw new UserException("WMEXP_QC_REQD", new Object[] {});
		}

		double qtyValue = parseDouble(qty, 0);

		//Search based on attributes
		Query searchQry = null;
		if (isEmpty(lot))
		{
			searchQry = new Query("wm_qcverifydetail", "wm_qcverifydetail.CONTAINERID = '" + containerID
					+ "' and wm_qcverifydetail.SKU = '" + item + "'", null);
		}
		else
		{
			searchQry = new Query("wm_qcverifydetail", "wm_qcverifydetail.CONTAINERID = '" + containerID
					+ "' and wm_qcverifydetail.SKU = '" + item + "' and wm_qcverifydetail.LOT = '" + lot + "'", null);
		}
		BioCollectionBean results = uowb.getBioCollectionBean(searchQry);
		if (results.size() == 1)
		{
			double limit = isEmpty(results.get("0").get("QTYPICKED")) ? 0
					: parseDouble(results.get("0").get("QTYPICKED").toString(), 0);

			double prevQuantity = isEmpty(results.get("0").get("QTYVERIFIED")) ? 0
					: parseDouble(results.get("0").get("QTYVERIFIED").toString(), 0);

			if (prevQuantity + qtyValue > limit)
			{
				throw new UserException("WMEXP_QC_OVERALLOCATE", new Object[] { new Double(limit) });
			}
			//Update if one record returned
			results.get("0").set("QTYVERIFIED", String.valueOf(prevQuantity + qtyValue));
			uowb.saveUOW(true);
		}
		else if (results.size() > 1)
		{
			throw new UserException("WMEXP_QC_LOT", new Object[] {});
		}
		else
		{
			throw new UserException("WMEXP_QC_SAVE", new Object[] {});
			//return RET_CANCEL;
		}

		//
		//update status
		Query statusQry = new Query("wm_qcverifydetail", "wm_qcverifydetail.CONTAINERID = '" + containerID + "'", null);
		BioCollectionBean statusResults = uowb.getBioCollectionBean(statusQry);
		boolean setStatus = determineStatus(statusResults);

		//update qcverify record
		Query qcQry = new Query("wm_qcverify", "wm_qcverify.CONTAINERID = '" + containerID + "'", null);
		BioCollectionBean qcResults = uowb.getBioCollectionBean(qcQry);

		BioBean qcBean = qcResults.get("0");
		if (setStatus == true)
		{
			qcBean.set("STATUS", "1");
			uowb.saveUOW(true);
		}

		//

		return RET_CONTINUE;
	}

	double parseDouble(String value, double defaultValue)
	{
		NumberFormat nf = NumberFormat.getInstance();
		double tempValue;

		try
		{
			tempValue = nf.parse(value).doubleValue();
		} catch (ParseException e)
		{
			e.printStackTrace();
			tempValue = defaultValue;
		}

		return tempValue;
	}

	boolean determineStatus(BioCollectionBean collection) throws EpiDataException
	{
		boolean verified = true;

		for (int i = 0; i < collection.size(); i++)
		{
			BioBean selected = collection.get(String.valueOf(i));

			if (isEmpty(selected.getValue("QTYVERIFIED")))
			{
				//set verified to false because one record is not verified
				verified = false;
			}

		}

		if (verified == true)
		{
			//return "1";
			return true;
		}
		else
		{
			return false;
		}

	}

	private boolean isEmpty(Object attributeValue)
	{

		if (attributeValue == null)
		{
			return true;
		}
		else if (attributeValue.toString().matches("\\s*"))
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked or
	 * a value entered in a form in a modal dialog
	 * Write code here if u want this to be called when the UI Action event is fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext} exposes information about the
	 * event, including the service and the user interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes information about the results
	 * of the action that has occurred, and enables your extension to modify them.</li>
	 * </ul>
	 */
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
	{

		try
		{
			// Add your code here to process the event

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
