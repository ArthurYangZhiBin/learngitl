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

package com.infor.scm.waveplanning.wp_wavemgmt_confirmwave.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.GregorianCalendar;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.GUIDFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.infor.scm.waveplanning.common.util.BioAttributeUtil;
import com.infor.scm.waveplanning.common.util.WPUserUtil;
import com.infor.scm.waveplanning.common.util.WPUtil;
import com.infor.scm.waveplanning.wp_wavemgmt.util.WPWaveMgmtUtil;
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

public class ConfirmWaveAddOrderNavSelect extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ConfirmWaveAddOrderNavSelect.class);

	private String CONTEXT_WAVEMGMT_WAVEKEY = "WAVEMGMT_WAVEKEY";

	private String CONTEXT_WAVEMGMT_ADDORDERCHOICE = "WAVEMGMT_ADDORDERCHOICE";

	public static final String SESSION_KEY_DEFAULT_FILTER_RECORDS_KEY = "wp.session.key.default.filter.records.key";

	public static final String SESSION_KEY_WAVEMGMT_WAVEKEY_ADD = "ADD_WAVEMGMT_WAVEKEY";
	
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
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION_ConfirmWaveAddOrderNavSelect_execute", "Entering ConfirmWaveAddOrderNavSelect",
				SuggestedCategory.NONE);
		/*
		INSERT INTO wpuser.CODELKUP(WHSEID,LISTNAME,CODE,DESCRIPTION,SHORT,LONG_VALUE,LOCALE) VALUES ('dbo','ADDORDERS','QB','Query Builder','QB','Query Builder','en');
		INSERT INTO wpuser.CODELKUP(WHSEID,LISTNAME,CODE,DESCRIPTION,SHORT,LONG_VALUE,LOCALE) VALUES ('dbo','ADDORDERS','GF','Graphical Filter','GF','Graphical Filter','en');
		INSERT INTO wpuser.CODELKUP(WHSEID,LISTNAME,CODE,DESCRIPTION,SHORT,LONG_VALUE,LOCALE) VALUES ('dbo','ADDORDERS','SF','Saved Filter','SF','Saved Filter','en');
		INSERT INTO wpuser.CODELKUP(WHSEID,LISTNAME,CODE,DESCRIPTION,SHORT,LONG_VALUE,LOCALE) VALUES ('dbo','ADDORDERS','SO','Specific Order','SO','Specific Order','e
		 */
		final String selfNavigation = getParameterString("SELFNAVIGATION");//"closeModalDialog123";
		final String specificOrderNavigation = getParameterString("SONAVIGATION");//"closeModalDialog122";
		final String queryBuilderNavigation = getParameterString("QBNAVIGATION");
		final String graphicalFilterNavigation = getParameterString("GFNAVIGATION");
		final String savedFilterNavigation = getParameterString("SFNAVIGATION");

		try
		{
			StateInterface state = context.getState();
			EpnyUserContext userContext = context.getServiceManager().getUserContext();
			final String interactionId = state.getInteractionId();

			CONTEXT_WAVEMGMT_WAVEKEY += interactionId;
			CONTEXT_WAVEMGMT_ADDORDERCHOICE += interactionId;

			final GregorianCalendar currentDate = new GregorianCalendar();
			String userId = WPUserUtil.getUserId(state);
			String facility = WPUtil.getFacility(state.getRequest());

			UnitOfWorkBean uow = state.getDefaultUnitOfWork();

			String choice = (String) userContext.get(CONTEXT_WAVEMGMT_ADDORDERCHOICE);
			_log.debug("LOG_DEBUG_EXTENSION_ConfirmWaveAddOrderNavSelect_execute", "Choice is " + choice,
					SuggestedCategory.NONE);
			userContext.put(CONTEXT_WAVEMGMT_ADDORDERCHOICE, null);
			//navigate based on order choices

			context.setNavigation(selfNavigation);
			if ("SO".equals(choice))
			{
				//Delete temp records from wp_wavestatusheader
				WPWaveMgmtUtil.clearTempTableBasedOnEditDate(state, "wp_wavestatusheader");
				
				Integer waveKey = (Integer) userContext.get(CONTEXT_WAVEMGMT_WAVEKEY);
				_log.debug("LOG_DEBUG_EXTENSION_ConfirmWaveAddOrderNavSelect_execute", "wavekey = " + waveKey,
						SuggestedCategory.NONE);
				//Insert or Update wp_wavestatusheader record
				insertOrUpdateWaveStatusHeaderRecord(state, interactionId, currentDate, userId, facility, waveKey);

				//Delete Existing Records in the wp_addordertemp table for this interaction and wavekey
				WPWaveMgmtUtil.deleteRecordsInWPAddOrderTempForASpecificInteraction(state, interactionId, facility,
						waveKey);

				//Delete records in wp_addordertemp older than a day
				WPWaveMgmtUtil.clearTempTableBasedOnAddDate(state, "wp_addordertemp");

				uow = state.getDefaultUnitOfWork();
				final Query waveStatusHeaderQuery = new Query("wp_wavestatusheader", "wp_wavestatusheader.WAVEKEY = '"
						+ waveKey + "' and wp_wavestatusheader.INTERACTIONID = '" + interactionId
						+ "' and wp_wavestatusheader.WHSEID = '" + facility + "'", null);

				BioCollectionBean rs = uow.getBioCollectionBean(waveStatusHeaderQuery);
				for (int i = 0; i < rs.size(); i++)
				{
					result.setFocus(rs.get("" + i));
				}

				context.setNavigation(specificOrderNavigation);
			}
			else if ("QB".equals(choice))
			{
				//Perform save behaviour as in the original menu click

				//WPQueryBuilderNewTempRecord
				callWPQueryBuilderNewTempRecord(context, result);
				
				//Set the WaveKey into the session
				Integer waveKey = (Integer) userContext.get(CONTEXT_WAVEMGMT_WAVEKEY);
				HttpSession session = state.getRequest().getSession();
				session.setAttribute(SESSION_KEY_WAVEMGMT_WAVEKEY_ADD, waveKey);
				
				context.setNavigation(queryBuilderNavigation);
			}
			else if ("GF".equals(choice))
			{
				//Set the WaveKey into the session
				Integer waveKey = (Integer) userContext.get(CONTEXT_WAVEMGMT_WAVEKEY);
				HttpSession session = state.getRequest().getSession();
				session.setAttribute(SESSION_KEY_WAVEMGMT_WAVEKEY_ADD, waveKey);
				
				context.setNavigation(graphicalFilterNavigation);
			}
			else if ("SF".equals(choice))
			{
				
				//Set the WaveKey into the session
				Integer waveKey = (Integer) userContext.get(CONTEXT_WAVEMGMT_WAVEKEY);
				HttpSession session = state.getRequest().getSession();
				session.setAttribute(SESSION_KEY_WAVEMGMT_WAVEKEY_ADD, waveKey);
				
				//query filter table
				Query loadBiosQry = new Query("wp_filter","","");			
				uow = state.getDefaultUnitOfWork();									
				BioCollectionBean bioCollection = uow.getBioCollectionBean(loadBiosQry);
				//bioCollection.setEmptyList(true);
				result.setFocus(bioCollection);
				context.setNavigation(savedFilterNavigation);
			}
			else
			{

			}

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}
		_log.debug("LOG_DEBUG_EXTENSION_ConfirmWaveAddOrderNavSelect_execute", "Leaving ConfirmWaveAddOrderNavSelect",
				SuggestedCategory.NONE);
		return RET_CONTINUE;
	}

	private void callWPQueryBuilderNewTempRecord(ActionContext context, ActionResult result) throws UserException
	{
		StateInterface state;
		UnitOfWorkBean uow;
		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRSAVEQRY", "Executing WPQueryBuilderNewTempRecord", 100L);
		state = context.getState();
		uow = state.getDefaultUnitOfWork();
		Query qry = new Query("wp_filter", "wp_filter.USERDEFAULT = 1 AND wp_filter.USERID = '"
				+ WPUserUtil.getUserId(state) + "'", "");
		BioCollection bc = uow.getBioCollectionBean(qry);
		try
		{
			if (bc != null && bc.size() > 0)
			{
				String uniqueId = GUIDFactory.getGUIDStatic();
				Bio filterHeader = bc.elementAt(0);
				BioCollection filterDetails = filterHeader.getBioCollection("FILTERDETAILS");
				if (filterDetails != null && filterDetails.size() > 0)
				{
					for (int i = 0; i < filterDetails.size(); i++)
					{
						Bio filterDetail = filterDetails.elementAt(i);
						QBEBioBean newTempRecord = uow.getQBEBioWithDefaults("querybuildertemp");
						newTempRecord.set("OPERATOR", filterDetail.get("OPERATOR"));
						newTempRecord.set("INTERACTIONID", uniqueId);
						newTempRecord.set("ISHEADER", "0");
						newTempRecord.set("FIRSTVALUE", filterDetail.get("FIRSTVALUE"));
						newTempRecord.set("SECONDVALUE", filterDetail.get("SECONDVALUE"));
						newTempRecord.set("ORDERFIELD", filterDetail.get("ORDERFIELD"));
						newTempRecord.set("ANDOR", filterDetail.get("ANDOR"));
						newTempRecord.save();
					}
				}
				QBEBioBean newTempRecord = uow.getQBEBioWithDefaults("querybuildertemp");
				newTempRecord.set("INTERACTIONID", uniqueId);
				newTempRecord.set("ISHEADER", "1");
				newTempRecord.set("ORDERSMAX", filterHeader.get("MAXORDERS"));
				newTempRecord.set("CUBEMAX", filterHeader.get("MAXCUBE"));
				newTempRecord.set("INCLUDERF", filterHeader.get("RFID_STND"));
				newTempRecord.set("ORDERLINESMAX", filterHeader.get("MAXORDERLINES"));
				newTempRecord.set("WEIGHTMAX", filterHeader.get("MAXWEIGHT"));
				newTempRecord.set("CASES", filterHeader.get("MAXCASES"));
				newTempRecord.save();
				try
				{
					uow.saveUOW(true);
				} catch (EpiException e)
				{
					e.printStackTrace();
					String args[] = new String[0];
					String errorMsg = getTextMessage("WMEXP_SYS_EXP", args, state.getLocale());
					throw new UserException(errorMsg, new Object[0]);
				}
				qry = new Query("querybuildertemp",
						"querybuildertemp.ISHEADER = '1' AND querybuildertemp.INTERACTIONID = '" + uniqueId
								+ "' ", "");

				BioCollectionBean tempRecords = uow.getBioCollectionBean(qry);
				BioRef focusRef = tempRecords.elementAt(0).getBioRef();
				result.setFocus(uow.getBioBean(focusRef));
				state.getRequest().getSession().setAttribute(SESSION_KEY_DEFAULT_FILTER_RECORDS_KEY, uniqueId);
			}
			else
			{
				result.setFocus(uow.getQBEBioWithDefaults("querybuildertemp"));
			}
		} catch (EpiDataException e)
		{
			e.printStackTrace();
			String args[] = new String[0];
			String errorMsg = getTextMessage("WMEXP_SYS_EXP", args, state.getLocale());
			throw new UserException(errorMsg, new Object[0]);
		} catch (DataBeanException e)
		{
			e.printStackTrace();
			String args[] = new String[0];
			String errorMsg = getTextMessage("WMEXP_SYS_EXP", args, state.getLocale());
			throw new UserException(errorMsg, new Object[0]);
		}
	}

	/**
	 * @deprecated Use {@link WPWaveMgmtUtil#deleteRecordsInWPAddOrderTempForASpecificInteraction(StateInterface,String,String,Integer)} instead
	 */
	public static void deleteRecordsInWPAddOrderTempForASpecificInteraction(StateInterface state, final String interactionId, String facility, Integer waveKey) throws EpiDataException, EpiException
	{
		WPWaveMgmtUtil.deleteRecordsInWPAddOrderTempForASpecificInteraction(state, interactionId, facility, waveKey);
	}

	/**
	 * @deprecated Use {@link WPWaveMgmtUtil#clearTempTableBasedOnAddDate(StateInterface,String)} instead
	 */
	public static void clearTempTableBasedOnAddDate(StateInterface state, final String tempTable) throws EpiDataException, EpiException
	{
		WPWaveMgmtUtil.clearTempTableBasedOnAddDate(state, tempTable);
	}

	private void insertOrUpdateWaveStatusHeaderRecord(StateInterface state, final String interactionId, final GregorianCalendar currentDate, String userId, String facility, Integer waveKey) throws EpiDataException, DataBeanException, EpiException
	{
		UnitOfWorkBean uow;
		uow = state.getDefaultUnitOfWork();
		BioCollectionBean rs = uow.getBioCollectionBean(new Query("wp_wavestatusheader",
				"wp_wavestatusheader.WAVEKEY = '" + waveKey + "' and wp_wavestatusheader.INTERACTIONID = '"
						+ interactionId + "' and wp_wavestatusheader.WHSEID = '" + facility + "'", null));

		if (rs.size() == 0)
		{
			QBEBioBean headerQBE = uow.getQBEBioWithDefaults("wp_wavestatusheader");

			headerQBE.set("INTERACTIONID", interactionId);
			headerQBE.set("WAVEKEY", (waveKey));
			//headerQBE.set("DC_ID", BioAttributeUtil.getString(focus, "DC_ID"));
			headerQBE.set("WHSEID", facility);

			headerQBE.set("ADDDATE", currentDate);
			headerQBE.set("ADDWHO", userId);
			headerQBE.set("EDITDATE", currentDate);
			headerQBE.set("EDITWHO", userId);
			headerQBE.save();
		}
		else
		{
			for (int i = 0; i < rs.size(); i++)
			{
				BioBean row = rs.get(String.valueOf(i));

				row.set("EDITDATE", currentDate);
				row.set("EDITWHO", userId);
				row.save();
			}
		}

		uow.saveUOW(true);
	}
}
