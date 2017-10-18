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

package com.ssaglobal.scm.wms.wm_load_planning.action;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;
import java.util.Iterator;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
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

public class LPCreateLoadIDAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LPCreateLoadIDAction.class);
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
	protected int execute(ActionContext context, ActionResult result)
	throws EpiException
	{

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		//return super.execute(context, result);
		//Food Enhancements - 3PL - Trailer Temperature Capture - Sreedhar Kethireddy  - DEC -20-2010 - Start
		_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of LPCreateLoadIDAction", SuggestedCategory.NONE);
		StateInterface state = context.getState();
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		//String testMessage = mda.getLocalizedTextMessage("NotValidEntry", locale);

		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		RuntimeFormInterface toolbarForm = state.getCurrentRuntimeForm();
		_log.debug("LOG_DEBUG_EXTENSION", "Toolbar Form " + toolbarForm.getName(), SuggestedCategory.NONE);
		RuntimeFormInterface shellForm = toolbarForm.getParentForm(state);
		_log.debug("LOG_DEBUG_EXTENSION", "Shell Form " + shellForm.getName(), SuggestedCategory.NONE);
		SlotInterface headerSlot = shellForm.getSubSlot("wm_load_planning_template_slot1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		_log.debug("LOG_DEBUG_EXTENSION", "Header Form " + headerForm.getName(), SuggestedCategory.NONE);

		RuntimeListForm headerListForm = null;
		if (headerForm.isListForm())
		{
			headerListForm = (RuntimeListForm) headerForm;
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Not a list form", SuggestedCategory.NONE);
			return RET_CANCEL;
		}

		DataBean headerListFocus = headerListForm.getFocus();

		BioCollectionBean headerListFormCollection = null;
		if (headerListFocus.isBioCollection())
		{
			headerListFormCollection = (BioCollectionBean) headerListFocus;
			_log.debug("LOG_DEBUG_EXTENSION", "!@# It is a BioCollectionBean, size: " + headerListFormCollection.size(), SuggestedCategory.NONE);
		}



		ArrayList items = headerListForm.getAllSelectedItems();
		ArrayList<String> trailers= new ArrayList<String>();
		ArrayList<String> routes=new ArrayList<String>();

		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		if(userContext.get("CREATELOADCONT").toString().equalsIgnoreCase("YES")){
			if(userContext.get("DIFFLOADCONT") == null)
			{
				int j=0;
				for (Iterator it = items.iterator(); it.hasNext();)
				{
					BioBean selectedLoad = (BioBean) it.next();
					trailers.add(j, getTrailerNumber(selectedLoad.getValue("ORDERKEY").toString(),uow).trim());
					routes.add(j, selectedLoad.getValue("ROUTE").toString().trim());
					j++;
				}

				int size1=trailers.size();
				int size2=routes.size();
				String tr1=trailers.get(0);
				String rt1=routes.get(0);
				for(int k=1;k<size1;k++)
				{
					if(!tr1.equalsIgnoreCase(trailers.get(k)))
					{
						if(rt1.equalsIgnoreCase(routes.get(k)))
						{
							context.setNavigation("closeModalDialog209");
							return RET_CONTINUE;
						}
					}
				}


			}else if(userContext.get("DIFFLOADCONT").toString().equalsIgnoreCase("NO")){
				userContext.remove("DIFFLOADCONT");
				userContext.remove("CREATELOADCONT");
				return RET_CONTINUE;
			}

		}else{
			userContext.remove("DIFFLOADCONT");
			userContext.remove("CREATELOADCONT");
			return RET_CONTINUE;
		}

		//iterate items
		try
		{
			for (Iterator it = items.iterator(); it.hasNext();)
			{
				BioBean selectedLoad = (BioBean) it.next();
				String loadPlanningKey = selectedLoad.getValue("LOADPLANNINGKEY").toString();
				Object loadId = selectedLoad.getValue("LOADID");
				Object route = selectedLoad.getValue("ROUTE");
				Object stop = selectedLoad.getValue("STOP");
				_log.debug("LOG_DEBUG_EXTENSION", "! Selected Item - OrderKey: " + selectedLoad.getValue("ORDERKEY") + " +++ LoadPlanningKey"	+ loadPlanningKey, SuggestedCategory.NONE);

				//prepare stored procedure
				WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				Array params = new Array();
				params.add(new TextData(loadPlanningKey));
				actionProperties.setProcedureParameters(params);
				actionProperties.setProcedureName("NSPLPCREATELOAD");

				if (!((isEmpty(route)) || (isEmpty(stop))))
				{
					_log.debug("LOG_DEBUG_EXTENSION", "!@# Running SP", SuggestedCategory.NONE);
					//run stored procedure
					EXEDataObject results = null;
					try
					{
						results = WmsWebuiActionsImpl.doAction(actionProperties);
					} catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
						userContext.remove("DIFFLOADCONT");
						userContext.remove("CREATELOADCONT");
						return RET_CANCEL;
					}
					if (results.getColumnCount() != 0)
					{
						_log.debug("LOG_DEBUG_EXTENSION", "---Results", SuggestedCategory.NONE);
						for (int i = 1; i < results.getColumnCount(); i++)
						{
							_log.debug("LOG_DEBUG_EXTENSION", " " + i + " @ " + results.getAttribute(i).name + " " + results.getAttribute(i).value.getAsString(), SuggestedCategory.NONE);
						}
						_log.debug("LOG_DEBUG_EXTENSION", "----------", SuggestedCategory.NONE);
					}

					if (isEmpty(loadId))
					{
						_log.debug("LOG_DEBUG_EXTENSION", "!@# Setting LOADID & REMARKS", SuggestedCategory.NONE);
						selectedLoad.setValue("LOADID", results.getAttribValue(2).getAsString());

						selectedLoad.setValue("REMARKS", results.getAttribValue(3).getAsString());
					}
					else
					{
						_log.debug("LOG_DEBUG_EXTENSION", "!@# Setting REMARKS", SuggestedCategory.NONE);
						String remarks = mda.getLocalizedTextMessage("WMTXT_LP_NOT_UPDATED", locale);
						selectedLoad.setValue("REMARKS", remarks);
					}
				}
				else
				{
					_log.debug("LOG_DEBUG_EXTENSION", "!@# Didn't run SP", SuggestedCategory.NONE);
					String remarks = mda.getLocalizedTextMessage("WMTXT_LP_NOT_CREATED_UPDATED", locale);
					selectedLoad.setValue("REMARKS", remarks);
				}

				//Save Validation
				LPSaveValidation.saveValidation(headerListForm, selectedLoad);

			}
			//Save Results
			uow.saveUOW();
			uow.clearState();
			userContext.remove("DIFFLOADCONT");
			userContext.remove("CREATELOADCONT");

			result.setFocus(headerListFocus);

		} catch (RuntimeException e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			userContext.remove("DIFFLOADCONT");
			userContext.remove("CREATELOADCONT");
			return RET_CANCEL;
		} 
		//Food Enhancements - 3PL - Trailer Temperature Capture - Sreedhar Kethireddy  - DEC -20-2010 - End
		return RET_CONTINUE;
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
	 * @throws EpiException 
	 */
	protected int execute(ModalActionContext context, ActionResult result) throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of LPCreateLoadIDAction", SuggestedCategory.NONE);
		StateInterface state = context.getState();
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		//String testMessage = mda.getLocalizedTextMessage("NotValidEntry", locale);

		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		RuntimeFormInterface toolbarForm = context.getSourceForm();
		_log.debug("LOG_DEBUG_EXTENSION", "Toolbar Form " + toolbarForm.getName(), SuggestedCategory.NONE);
		RuntimeFormInterface shellForm = toolbarForm.getParentForm(state);
		_log.debug("LOG_DEBUG_EXTENSION", "Shell Form " + shellForm.getName(), SuggestedCategory.NONE);
		SlotInterface headerSlot = shellForm.getSubSlot("wm_load_planning_template_slot1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		_log.debug("LOG_DEBUG_EXTENSION", "Header Form " + headerForm.getName(), SuggestedCategory.NONE);

		RuntimeListForm headerListForm = null;
		if (headerForm.isListForm())
		{
			headerListForm = (RuntimeListForm) headerForm;
		}
		else
		{
			_log.debug("LOG_DEBUG_EXTENSION", "!@# Not a list form", SuggestedCategory.NONE);
			return RET_CANCEL;
		}

		DataBean headerListFocus = headerListForm.getFocus();

		BioCollectionBean headerListFormCollection = null;
		if (headerListFocus.isBioCollection())
		{
			headerListFormCollection = (BioCollectionBean) headerListFocus;
			_log.debug("LOG_DEBUG_EXTENSION", "!@# It is a BioCollectionBean, size: " + headerListFormCollection.size(), SuggestedCategory.NONE);
		}
		try
		{
			ArrayList items = headerListForm.getAllSelectedItems();
			//iterate items
			for (Iterator it = items.iterator(); it.hasNext();)
			{
				BioBean selectedLoad = (BioBean) it.next();
				String loadPlanningKey = selectedLoad.getValue("LOADPLANNINGKEY").toString();
				Object loadId = selectedLoad.getValue("LOADID");
				Object route = selectedLoad.getValue("ROUTE");
				Object stop = selectedLoad.getValue("STOP");
				_log.debug("LOG_DEBUG_EXTENSION", "! Selected Item - OrderKey: " + selectedLoad.getValue("ORDERKEY") + " +++ LoadPlanningKey"	+ loadPlanningKey, SuggestedCategory.NONE);

				//prepare stored procedure
				WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				Array params = new Array();
				params.add(new TextData(loadPlanningKey));
				actionProperties.setProcedureParameters(params);
				actionProperties.setProcedureName("NSPLPCREATELOAD");

				if (!((isEmpty(route)) || (isEmpty(stop))))
				{
					_log.debug("LOG_DEBUG_EXTENSION", "!@# Running SP", SuggestedCategory.NONE);
					//run stored procedure
					EXEDataObject results = null;
					try
					{
						results = WmsWebuiActionsImpl.doAction(actionProperties);
					} catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
						return RET_CANCEL;
					}
					if (results.getColumnCount() != 0)
					{
						_log.debug("LOG_DEBUG_EXTENSION", "---Results", SuggestedCategory.NONE);
						for (int i = 1; i < results.getColumnCount(); i++)
						{
							_log.debug("LOG_DEBUG_EXTENSION", " " + i + " @ " + results.getAttribute(i).name + " " + results.getAttribute(i).value.getAsString(), SuggestedCategory.NONE);
						}
						_log.debug("LOG_DEBUG_EXTENSION", "----------", SuggestedCategory.NONE);
					}

					if (isEmpty(loadId))
					{
						_log.debug("LOG_DEBUG_EXTENSION", "!@# Setting LOADID & REMARKS", SuggestedCategory.NONE);
						selectedLoad.setValue("LOADID", results.getAttribValue(2).getAsString());

						selectedLoad.setValue("REMARKS", results.getAttribValue(3).getAsString());
					}
					else
					{
						_log.debug("LOG_DEBUG_EXTENSION", "!@# Setting REMARKS", SuggestedCategory.NONE);
						String remarks = mda.getLocalizedTextMessage("WMTXT_LP_NOT_UPDATED", locale);
						selectedLoad.setValue("REMARKS", remarks);
					}
				}
				else
				{
					_log.debug("LOG_DEBUG_EXTENSION", "!@# Didn't run SP", SuggestedCategory.NONE);
					String remarks = mda.getLocalizedTextMessage("WMTXT_LP_NOT_CREATED_UPDATED", locale);
					selectedLoad.setValue("REMARKS", remarks);
				}

				//Save Validation
				LPSaveValidation.saveValidation(headerListForm, selectedLoad);

			}
			//Save Results
			uow.saveUOW();
			uow.clearState();
			result.setFocus(headerListFocus);

		} catch (RuntimeException e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		} 

		return RET_CONTINUE;
	}

	private boolean isNull(Object attributeValue)
			throws EpiDataException
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

	private boolean isEmpty(Object attributeValue)
			throws EpiDataException
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


	//Food Enhancements - 3PL - Trailer Temperature Capture - Sreedhar Kethireddy  - DEC -20-2010 - Start
	private String getTrailerNumber(String receiptKey,UnitOfWorkBean uow)
	{
		final String bio3 = "wm_orders";
		final String attribute3 = "ORDERKEY";
		int size3=0;
		String trailernumber="";
		BioCollectionBean listCollection3 = null;

		String receiptKeyValue;
		Object objreceiptKeyValue = receiptKey;
		if (objreceiptKeyValue == null){
			receiptKeyValue = "";
		}else
		{
			receiptKeyValue = objreceiptKeyValue.toString();
			try {
				String sQueryString = bio3 + "." + attribute3 + " = '" + receiptKeyValue + "'";
				Query BioQuery = new Query(bio3,sQueryString,null);

				listCollection3 = uow.getBioCollectionBean(BioQuery);
				size3=listCollection3.size();
				if(size3>0)
				{
					trailernumber=listCollection3.get("0").getValue("TrailerNumber")==null?"":listCollection3.get("0").getValue("TrailerNumber").toString();
				}

			} catch(EpiException e) {
				// Handle Exceptions 
				e.printStackTrace();

			} 
		}
		return trailernumber;
	}
	//Food Enhancements - 3PL - Trailer Temperature Capture - Sreedhar Kethireddy  - DEC -20-2010 - End
}


/*
 IF NOT( (ISNULL(ls_route) or ls_route = '' or ls_route = ' ') OR (ISNULL(ls_stop) or ls_stop = '' or ls_stop = ' ')) THEN
 ll_rtn = gnv_app.of_GetForte().of_Execute("NSPLPCREATELOAD", ls_args, ls_result[]) 
 
 IF ISNULL(ls_loadid) or ls_loadid = '' or ls_loadid = ' ' THEN
 lds_source.setitem(ll_row,"loadid", ls_result[2])
 lds_source.setitem(ll_row,"remarks", ls_result[3])
 ELSE
 lds_source.setitem(ll_row,"remarks", 'Not Updated')
 END IF
 ELSE
 ll_rtn = 0
 lds_source.setitem(ll_row,"remarks", 'Not Create/Updated')            
 END IF



 */
