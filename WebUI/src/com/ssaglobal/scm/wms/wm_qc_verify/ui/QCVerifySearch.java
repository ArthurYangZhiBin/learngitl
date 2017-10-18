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
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.HelperBio;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.UOWIllegalSaveException;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeForm;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class QCVerifySearch extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(QCVerifySearch.class);
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
		UnitOfWork uow = state.getDefaultUnitOfWork().getUOW();

		RuntimeFormInterface searchForm = state.getCurrentRuntimeForm();

		String containerID = searchForm.getFormWidgetByName("CONTAINERID").getDisplayValue().toUpperCase();
		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Value " + containerID + "\n", SuggestedCategory.NONE);;
		//Check if CONTAINERID exists in QCVERIFY
		Query verifyQuery = new Query("wm_qcverify", "wm_qcverify.CONTAINERID = '" + containerID + "'", null);
		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Query = " + verifyQuery.getQueryExpression() + "\n", SuggestedCategory.NONE);;
		BioCollectionBean verifyResults = uowb.getBioCollectionBean(verifyQuery);
		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + verifyResults.size() + "\n", SuggestedCategory.NONE);;
		for (int i = 0; i < verifyResults.size(); i++)
		{
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Result " + i + " "	+ verifyResults.get(String.valueOf(i)).getValue("CONTAINERID") + "\n", SuggestedCategory.NONE);;
		}
		if (verifyResults.size() == 1)
		{
			//if CONTAINERID exists, display associated records
			BioBean verifyBio = verifyResults.get("0");
			_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "Returning " + verifyBio.getValue("CONTAINERID") + "\n", SuggestedCategory.NONE);;

			Query syncQuery = new Query("wm_qcverifydetail", "wm_qcverifydetail.CONTAINERID = '" + containerID + "'", null);
			BioCollectionBean syncResults = uowb.getBioCollectionBean(syncQuery);

			//Query DB
			String type = (String) verifyBio.getValue("TYPE");
			String syncSQLQuery;
			if (type.equals("CASE"))
			{
				//Case Query
				syncSQLQuery = "SELECT STORERKEY, SKU, LOT, SUM(QTY) AS QTYPICKED, STATUS " + "FROM PICKDETAIL "
						+ "WHERE (CASEID = '" + containerID + "') " + "GROUP BY STORERKEY, SKU, LOT, STATUS ";
			}
			else
			{
				//Drop Query
				syncSQLQuery = "SELECT STORERKEY, SKU, LOT, SUM(QTY) AS QTYPICKED, STATUS " + "FROM PICKDETAIL "
						+ "WHERE (DROPID = '" + containerID + "') " + "GROUP BY STORERKEY, SKU, LOT, STATUS ";
			}
			EXEDataObject syncSQLResults = WmsWebuiValidationSelectImpl.select(syncSQLQuery);

			//synchronize
			//number of records = add/subtract
			if (syncResults.size() > syncSQLResults.getRowCount())
			{
				//need to remove records
				_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "need to remove records from qcverifydetail" + "\n", SuggestedCategory.NONE);;
				for (int i = 0; i < syncResults.size(); i++)
				{
					BioBean selected = syncResults.get(String.valueOf(i));
					if (existsInSQLResults(syncSQLResults, (String) selected.getValue("SKU"),
											(String) selected.getValue("LOT")) == false)
					{
						selected.delete();
					}
				}
				uowb.saveUOW(true);
			}
			if (syncResults.size() < syncSQLResults.getRowCount())
			{
				//need to add records
				_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + "need to add records" + "\n", SuggestedCategory.NONE);;
				for (int i = 0; i < syncSQLResults.getRowCount(); i++)
				{
					String sqlItem = syncSQLResults.getAttribValue(new TextData("SKU")).getAsString();
					String sqlLot = syncSQLResults.getAttribValue(new TextData("LOT")).getAsString();
					if (existsInQueryResults(syncResults, sqlItem, sqlLot) == false)
					{
						HelperBio detailHelper = uow.createHelperBio("wm_qcverifydetail");
						detailHelper.set("CONTAINERID", containerID);
						detailHelper.set("STORERKEY",
											syncSQLResults.getAttribValue(new TextData("STORERKEY")).getAsString());
						detailHelper.set("SKU", sqlItem);
						detailHelper.set("LOT", sqlLot);
						detailHelper.set("QTYPICKED",
											syncSQLResults.getAttribValue(new TextData("QTYPICKED")).getAsString());
						uow.createBio(detailHelper);

						
					}
					syncSQLResults.getNextRow();
				}
				uowb.saveUOW(true);
			}
			

			//requery to see changes
			syncResults = uowb.getBioCollectionBean(syncQuery);
			
			//check to be sure qty's are correct
			for (int i = 0; i < syncSQLResults.getRowCount(); i++)
			{
				String item = syncSQLResults.getAttribValue(2).getAsString();
				String lot = syncSQLResults.getAttribValue(3).getAsString();
				String qtyPicked = syncSQLResults.getAttribValue(4).getAsString();

				//get BioBean
				BioBean detailBean = getBioBean(syncResults, item, lot);
				if (detailBean == null)
				{
					return RET_CANCEL;
				}
				else
				{
					if (Double.parseDouble(qtyPicked) != (Double.parseDouble(detailBean.getValue("QTYPICKED").toString())))
					{
						detailBean.setValue("QTYPICKED", qtyPicked);
					}

				}

				syncSQLResults.getNextRow();
			}
			uowb.saveUOW(true);
			result.setFocus(verifyBio);

		}
		else
		{
			//else insert QCVERIFYDETAIL records based on CONTAINERID

			/// First search
			//CASEID
			//DROPID
			String caseQuery = "SELECT STORERKEY, SKU, LOT, SUM(QTY) AS QTYPICKED, STATUS " + "FROM PICKDETAIL "
					+ "WHERE (CASEID = '" + containerID + "') " + "GROUP BY STORERKEY, SKU, LOT, STATUS ";
			_log.debug("LOG_DEBUG_EXTENSION", "///QUERY " + caseQuery, SuggestedCategory.NONE);;
			EXEDataObject caseResults = WmsWebuiValidationSelectImpl.select(caseQuery);
			if (caseResults.getRowCount() > 0)
			{
				//insert records into table
				insertPicksIntoQCVerifyTables(result, uowb, uow, containerID, caseResults, "CASE");
			}
			else
			{
				//search by dropid
				String dropQuery = "SELECT STORERKEY, SKU, LOT, SUM(QTY) AS QTYPICKED, STATUS " + "FROM PICKDETAIL "
						+ "WHERE (DROPID = '" + containerID + "') " + "GROUP BY STORERKEY, SKU, LOT, STATUS ";
				_log.debug("LOG_DEBUG_EXTENSION", "///QUERY " + dropQuery, SuggestedCategory.NONE);;
				EXEDataObject dropResults = WmsWebuiValidationSelectImpl.select(dropQuery);
				if (dropResults.getRowCount() > 0)
				{
					insertPicksIntoQCVerifyTables(result, uowb, uow, containerID, dropResults, "DROP");
				}
				else
				{
					//No Results
					throw new UserException("WMEXP_QC_NOTFOUND", new Object[] {});
				}
			}

		}

		return RET_CONTINUE;
	}

	private void insertPicksIntoQCVerifyTables(ActionResult result, UnitOfWorkBean uowb, UnitOfWork uow, String containerID, EXEDataObject results, String type) throws EpiDataException, UserException, UnitOfWorkException, UOWIllegalSaveException
	{
		//create new QCVERIFY record - set result
		HelperBio qcHelper = uow.createHelperBio("wm_qcverify");
		qcHelper.set("CONTAINERID", containerID);
		qcHelper.set("TYPE", type);

		//insert records
		//--spin through caseResults
		boolean shipped = true;
		boolean picked = true;
		for (int i = 0; i < results.getRowCount(); i++)
		{
			HelperBio detailHelper = uow.createHelperBio("wm_qcverifydetail");
			detailHelper.set("CONTAINERID", containerID);
			detailHelper.set("STORERKEY", results.getAttribValue(1));
			detailHelper.set("SKU", results.getAttribValue(2));
			detailHelper.set("LOT", results.getAttribValue(3));
			detailHelper.set("QTYPICKED", results.getAttribValue(4).getAsString());
			uow.createBio(detailHelper);
			//status validation
			int status = results.getAttribValue(5).asI4();
			if (!(status >= 5))
			{
				//set picked to false because one record is not picked
				picked = false;
			}
			if (status != 9)
			{
				//set shipped to false because one record is not shipped
				shipped = false;
			}
			setStatus(qcHelper, shipped, picked);

			results.getNextRow();
		}

		//
		Bio qcBio = uow.createBio(qcHelper);
		result.setFocus(uowb.getBioBean(qcBio));
		//
		uow.save();
	}

	private boolean existsInSQLResults(EXEDataObject results, String item, String lot)
	{

		for (int i = 0; i < results.getRowCount(); i++)
		{
			String sqlItem = results.getAttribValue(new TextData("SKU")).getAsString();
			String sqlLot = results.getAttribValue(new TextData("LOT")).getAsString();
			if (sqlItem.equals(item) && sqlLot.equals(lot))
			{
				return true;
			}
			results.getNextRow();
		}
		return false;
	}

	private boolean existsInQueryResults(BioCollectionBean results, String item, String lot) throws EpiDataException
	{
		_log.debug("LOG_DEBUG_EXTENSION", "\n\t" + item + " - " + lot + "\n", SuggestedCategory.NONE);;
		for (int i = 0; i < results.size(); i++)
		{
			String colItem = (String) results.get(String.valueOf(i)).getValue("SKU");
			String colLot = (String) results.get(String.valueOf(i)).getValue("LOT");
			
			if (colItem.equals(item) && colLot.equals(lot))
			{
				return true;
			}
		}
		return false;
	}

	private BioBean getBioBean(BioCollectionBean syncResults, String item, String lot) throws EpiDataException
	{
		BioBean detailBean = null;
		for (int j = 0; j < syncResults.size(); j++)
		{
			BioBean selected = syncResults.get(String.valueOf(j));
			if (((String) selected.getValue("SKU")).equals(item) && ((String) selected.getValue("LOT")).equals(lot))
			{
				detailBean = selected;
				continue;
			}
		}
		return detailBean;
	}

	private void setStatus(HelperBio qcHelper, boolean shipped, boolean picked) throws EpiDataException, UserException
	{
		if (picked == true)
		{
			if (shipped == true)
			{
				qcHelper.set("STATUS", "9");
			}
			else
			{
				qcHelper.set("STATUS", "0");
			}
		}
		else
		{
			//invalid results
			throw new UserException("WMEXP_QC_NOTPICKED", new Object[] {});
		}
	}

}
