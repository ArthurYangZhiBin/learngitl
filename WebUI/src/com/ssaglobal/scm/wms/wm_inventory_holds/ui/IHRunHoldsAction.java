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

package com.ssaglobal.scm.wms.wm_inventory_holds.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.DateTimeData;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.EpiDataInvalidAttrException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationUpdateImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class IHRunHoldsAction extends SaveAction
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(IHRunHoldsAction.class);

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
	protected int execute(ActionContext context, ActionResult result) throws UserException
	{
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		//run through header, calling the stored procedure
		RuntimeFormInterface ihHeader = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_inventory_hold_list_view", state);
		BioCollectionBean collection = (BioCollectionBean) ihHeader.getFocus();
		try
		{
			for (int i = 0; i < collection.size(); i++)
			{
				if (collection.get(String.valueOf(i)).hasBeenUpdated("HOLD"))
				{
					//call SP
					runInventoryHoldsSP(collection.get(String.valueOf(i)));
				}
				//Krishna Kuchipudi - Aug-18-2010 - 3PL - Food Enhancements - Starts
				if ((collection.get(String.valueOf(i)).hasBeenUpdated("HOURSTOHOLD")) || 
						(collection.get(String.valueOf(i)).hasBeenUpdated("AUTORELEASEDATE")) || 
						(collection.get(String.valueOf(i)).hasBeenUpdated("COMMENTS"))) {

					holdDateTimeUpdate(collection.get(String.valueOf(i)));
				}
				//Krishna Kuchipudi - Aug-18-2010 - 3PL - Food Enhancements - Ends
			}
		} catch (EpiDataInvalidAttrException e)
		{
			_log.error("LOG_ERROR_EXTENSION_IHRunHoldsAction", "From Header 1", SuggestedCategory.NONE);
			e.printStackTrace();
			_log.error("LOG_ERROR_EXTENSION_IHRunHoldsAction", e.getStackTraceAsString(), SuggestedCategory.NONE);
			throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
		} catch (EpiDataException e)
		{
			_log.error("LOG_ERROR_EXTENSION_IHRunHoldsAction", "From Header 2", SuggestedCategory.NONE);
			e.printStackTrace();
			_log.error("LOG_ERROR_EXTENSION_IHRunHoldsAction", e.getStackTraceAsString(), SuggestedCategory.NONE);
			throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
		} 

		//save detail only if new
		DataBean newRecord = null;
		RuntimeFormInterface ihDetail = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_inventory_hold_detail_view", state);
		if (!isNull(ihDetail))
		{
			//call SP
			if (ihDetail.getFocus().isTempBio())
			{
				try
				{
					runInventoryHoldsSP(ihDetail.getFocus());
				} catch (EpiException e1)
				{
					_log.error("LOG_ERROR_EXTENSION_IHRunHoldsAction", "From Detail", SuggestedCategory.NONE);
					e1.printStackTrace();
					_log.error("LOG_ERROR_EXTENSION_IHRunHoldsAction", e1.getStackTraceAsString(), SuggestedCategory.NONE);
					throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
				}
				try
				{
					//Query for newly created record
					//needed so that the record displayed on screen will reflect the status (BioBean/QBEBean) of the record accurately
					newRecord = (uowb.getBioCollectionBean(new Query("wm_inventoryhold_bio", "wm_inventoryhold_bio.INVENTORYHOLDKEY = '"
							+ ihDetail.getFocus().getValue("INVENTORYHOLDKEY") + "'", null))).get("0");
				} catch (EpiException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					_log.error("LOG_ERROR_EXTENSION_IHRunHoldsAction", getStackTraceAsString(e), SuggestedCategory.NONE);
					newRecord = null;
				}

			}

		}

		uowb.clearState();

		if (newRecord instanceof BioBean)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "newRecord is a BioBean", SuggestedCategory.NONE);
		}
		else if (newRecord instanceof QBEBioBean)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "newRecord is a QBEBioBean", SuggestedCategory.NONE);
		}
		else if (newRecord instanceof BioCollectionBean)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "newRecord is a BioCollectionBean", SuggestedCategory.NONE);
		}
		else if (newRecord == null)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "newRecord is null", SuggestedCategory.NONE);
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", newRecord.getClass().getName(), SuggestedCategory.NONE);
		}
		//returns newly created record
		result.setFocus(newRecord);
		return RET_CONTINUE;
	}

	static String getStackTraceAsString(Throwable t)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		pw.print(" [ ");
		pw.print(t.getClass().getName());
		pw.print(" ] ");
		pw.print(t.getMessage());
		t.printStackTrace(pw);
		pw.flush();
		sw.flush();
		return sw.toString(); 
	}

	private int runInventoryHoldsSP(DataBean holdBio) throws UserException 
	{
		_log.debug("LOG_DEBUG_EXTENSION", "" + "Running SP" + "\n", SuggestedCategory.NONE);

		//nspInventoryHoldResultSet",{as_inventoryholdkey, as_lot,as_loc,as_id,as_status,as_hold}

		Object inventoryHoldKey = holdBio.getValue("INVENTORYHOLDKEY");
		Object lot = isNull(holdBio.getValue("LOT")) ? "" : holdBio.getValue("LOT").toString().toUpperCase();
		Object loc = isNull(holdBio.getValue("LOC")) ? "" : holdBio.getValue("LOC").toString().toUpperCase();
		Object id = isNull(holdBio.getValue("ID")) ? "" : holdBio.getValue("ID").toString().toUpperCase();
		Object status = holdBio.getValue("STATUS");
		Object hold = isNull(holdBio.getValue("HOLD")) ? "0" : holdBio.getValue("HOLD");
		//Krishna Kuchipudi - Aug-18-2010 - 3PL - Food Enhancements - Starts
		Object comment = isNull(holdBio.getValue("COMMENTS")) ? "" : holdBio.getValue("COMMENTS").toString().toUpperCase();
		//Krishna Kuchipudi - Aug-18-2010 - 3PL - Food Enhancements - Ends
		if (hold == null)
		{
			hold = "0";
		}
		_log.debug("LOG_DEBUG_EXTENSION", "KEY " + inventoryHoldKey + "\n", SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION", "LOT " + lot + "\n", SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION", "LOC " + loc + "\n", SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION", "ID " + id + "\n", SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION", "STATUS " + status + "\n", SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION", "HOLD " + hold + "\n", SuggestedCategory.NONE);
		WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
		Array params = new Array();
		params.add(new TextData((String) inventoryHoldKey));
		params.add(new TextData((String) lot));
		params.add(new TextData((String) loc));
		params.add(new TextData((String) id));
		params.add(new TextData((String) status));
		params.add(new TextData((String) hold));
		//Krishna Kuchipudi - Aug-18-2010 - 3PL - Food Enhancements- Starts
		params.add(new TextData((String) comment));
		// Comment field is sent as an empty field.
		//Krishna Kuchipudi - Aug-18-2010 - 3PL - Food Enhancements - Ends
		actionProperties.setProcedureParameters(params);
		actionProperties.setProcedureName("nspInventoryHoldResultSet");
		//run stored procedure
		EXEDataObject holdResults = null;
		try
		{
			holdResults = WmsWebuiActionsImpl.doAction(actionProperties);
		} catch (WebuiException e)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "" + "CATCH BLOCK 1" + "\n", SuggestedCategory.NONE);
			_log.debug("LOG_DEBUG_EXTENSION", "" + e.getMessage() + "\n", SuggestedCategory.NONE);
			throw new UserException(e.getMessage(), new Object[] {});
		} 
		displayResults(holdResults);
		return RET_CONTINUE;
	}

	private void displayResults(EXEDataObject results)
	{
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		pw.println("" + results.getRowCount() + " x " + results.getColumnCount() + "\n");
		if (results.getColumnCount() != 0)
		{

			pw.println("---Results");
			for (int i = 1; i < results.getColumnCount() + 1; i++)
			{
				try
				{
					pw.println(" " + i + " @ " + results.getAttribute(i).name + " "
							+ results.getAttribute(i).value.getAsString());
				} catch (Exception e)
				{
					pw.println(e.getMessage());
				}
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION", sw.toString(), SuggestedCategory.NONE);
	}
	
	//Krishna Kuchipudi - Aug-18-2010 - 3PL - Food Enhancements - Starts
	private int holdDateTimeUpdate(DataBean holdBio) throws UserException {

		Object inventoryHoldKey = holdBio.getValue("INVENTORYHOLDKEY");
		Object hoursToHold = isNull(holdBio.getValue("HOURSTOHOLD")) ? "0": holdBio.getValue("HOURSTOHOLD");
		Object AUTORELEASEDATE = (isNull(holdBio.getValue("AUTORELEASEDATE")) ? "": holdBio.getValue("AUTORELEASEDATE"));
		Object Comment = (isNull(holdBio.getValue("COMMENTS")) ? "": holdBio.getValue("COMMENTS"));

		String sqlStmt = " Update InventoryHold set HourstoHold = '"+ hoursToHold + "', Comments = '"+Comment.toString()+"'";

		if(!AUTORELEASEDATE.equals(""))
		{
			DateTimeData dtd = new DateTimeData(((GregorianCalendar) AUTORELEASEDATE));
			Date date = null;
			SimpleDateFormat sdfSource = new SimpleDateFormat("dd-MMM-yyyy kk:mm:ss");
			try {
				date = sdfSource.parse(dtd.toString());
			} catch (ParseException e) {
				e.printStackTrace();
				throw new UserException("Date parse Error", new Object[1]);
			}

			SimpleDateFormat mssqlFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss:SSS");
			sqlStmt = sqlStmt+ ", AutoReleaseDate = '"+ mssqlFormat.format(date) + "'";
		}


		sqlStmt = sqlStmt+ " where InventoryHoldKey = '"+ inventoryHoldKey.toString() + "'";

		try {
			WmsWebuiValidationUpdateImpl.update(sqlStmt);
		} catch (DPException e1) {
			e1.printStackTrace();
			throw new UserException("Unable to update SQL Query Error", new Object[1]);
		}
		return RET_CONTINUE;
	}
	//Krishna Kuchipudi - Aug-18-2010 - 3PL - Food Enhancements - Ends

	private boolean isNull(Object attributeValue)
	{

		if (attributeValue == null)
		{
			return true;
		}
		else
		{
			return false;
		}

	}
}
