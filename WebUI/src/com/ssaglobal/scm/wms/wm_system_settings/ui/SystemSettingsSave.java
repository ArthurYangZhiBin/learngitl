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

package com.ssaglobal.scm.wms.wm_system_settings.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.metadata.Metadata;
import com.epiphany.shr.metadata.objects.generated.np.DataSource;

import com.epiphany.shr.data.dp.connection.DPConnection;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.db.DBFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.ssaglobal.scm.wms.datalayer.WmsDataProviderImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationUpdateImpl;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.navigation.SetIntoUserContextAction;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;
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

public class SystemSettingsSave extends SaveAction
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(SystemSettingsSave.class);

	SimpleDateFormat mssqlFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss:SSS");

	SimpleDateFormat oraFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

	SimpleDateFormat db2Format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

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
		/*
		 * updateSql=UPDATE nsqlconfig  SET EDITDATE = CONVERT(DATETIME,'2007-02-07 22:25:08:199',21), 
		 * NSQLVALUE = '1', EDITWHO = 'rm' WHERE (CONFIGKEY = 'ALLOWDECIMALVALUESINQTY')
		 */

		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		EpnyUserContext userCtx = context.getServiceManager().getUserContext();
		HttpSession session = state.getRequest().getSession();

		//O90
		//MSS
		String serverType = (String) session.getAttribute(SetIntoHttpSessionAction.DB_TYPE);
		if (serverType == null)
		{
			serverType = (String) userCtx.get(SetIntoHttpSessionAction.DB_TYPE);
		}
		_log.debug("LOG_DEBUG_EXTENSION_SystemSettingsSave", "ServerType : " + serverType, SuggestedCategory.NONE);
		String editWho = (String) userCtx.get("logInUserId");
		_log.debug("LOG_DEBUG_EXTENSION_SystemSettingsSave", "editwho : " + editWho, SuggestedCategory.NONE);

		mssqlFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		oraFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		db2Format.setTimeZone(TimeZone.getTimeZone("GMT"));

		RuntimeFormInterface headerForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_system_settings_list_view", state);
		BioCollectionBean colHeader = (BioCollectionBean) headerForm.getFocus();
		try
		{
			for (int i = 0; i < colHeader.size(); i++)
			{
				BioBean selected = colHeader.get(String.valueOf(i));
				List updates = selected.getUpdatedAttributes();
				if (selected.hasBeenUpdated("NSQLVALUE"))
				{
					_log.debug("LOG_DEBUG_EXTENSION", i + " " + selected.getValue("CONFIGKEY") + " "
							+ updates.toString(), SuggestedCategory.NONE);

					//write sql statement
					String sql = constructSqlUpdateStmt(serverType, editWho, selected);
					EXEDataObject results = WmsWebuiValidationUpdateImpl.update(sql);
				}

			}
		} catch (EpiDataException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		RuntimeFormInterface detailForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_system_settings_NB_list_view", state);
		BioCollectionBean colDetail = (BioCollectionBean) detailForm.getFocus();
		try
		{
			for (int i = 0; i < colDetail.size(); i++)
			{
				BioBean selected = colDetail.get(String.valueOf(i));
				List updates = selected.getUpdatedAttributes();
				if (selected.hasBeenUpdated("NSQLVALUE"))
				{
					_log.debug("LOG_DEBUG_EXTENSION", i + " " + selected.getValue("CONFIGKEY") + " "
							+ updates.toString(), SuggestedCategory.NONE);

					//write sql statement
					String sql = constructSqlUpdateStmt(serverType, editWho, selected);
					EXEDataObject results = WmsWebuiValidationUpdateImpl.update(sql);
				}
			}
		} catch (EpiDataException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			_log.error("LOG_ERROR_EXTENSION_SystemSettingsSave", e.getStackTraceAsString(), SuggestedCategory.NONE);
			throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
			
		}

		
		

		uowb.clearState();
		// Seems I have to force a save to clear the state completeley
		try
		{
			uowb.saveUOW(false);
		} catch (EpiException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		_log.debug("LOG_DEBUG_EXTENSION_SystemSettingsSave", "closing uow", SuggestedCategory.NONE);
		uowb.getUOW().close();
		return RET_CONTINUE;
	}

	private String constructSqlUpdateStmt(String serverType, String editWho, BioBean selected) throws EpiDataException, UserException
	{
		String sql = null;
		if(serverType == null)
		{
			_log.error("LOG_DEBUG_EXTENSION_SystemSettingsSave", "ServerType is null: " + serverType, SuggestedCategory.NONE);
			throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
		}
		else
		{
			String nsqlValue = selected.getString("NSQLVALUE") == null ? null : selected.getString("NSQLVALUE").toUpperCase();
			if (serverType.equalsIgnoreCase("MSS"))
			{
				sql = "UPDATE nsqlconfig  SET EDITDATE = CONVERT(DATETIME,'"
						+ mssqlFormat.format(Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTime())
						+ "',21), NSQLVALUE = '" + nsqlValue + "', EDITWHO = '" + editWho
						+ "' WHERE CONFIGKEY = '" + selected.getString("CONFIGKEY") + "'";
			}
			else if (serverType.equalsIgnoreCase("O90"))
			{
				sql = "UPDATE nsqlconfig  SET EDITDATE = to_date('"
						+ oraFormat.format(Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTime())
						+ "','YYYY-MM-DD HH24:MI:SS'), NSQLVALUE = '" + nsqlValue + "', EDITWHO = '"
						+ editWho + "' WHERE CONFIGKEY = '" + selected.getString("CONFIGKEY") + "'";
			}
			else if (serverType.equalsIgnoreCase("ODBC"))
			{
				sql = "UPDATE nsqlconfig  SET EDITDATE = '"
						+ db2Format.format(Calendar.getInstance(TimeZone.getTimeZone("GMT")).getTime())
						+ "', NSQLVALUE = '" + nsqlValue + "', EDITWHO = '" + editWho
						+ "' WHERE CONFIGKEY = '" + selected.getString("CONFIGKEY") + "'";
			}
			else
			{
				_log.error("LOG_DEBUG_EXTENSION_SystemSettingsSave", "ServerType is not found " + serverType, SuggestedCategory.NONE);
				throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_SystemSettingsSave", "SQL Statement " + sql, SuggestedCategory.NONE);
		return sql;
	}
}
