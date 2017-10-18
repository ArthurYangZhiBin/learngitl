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
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpSession; //AW 09/21/10 Incident:3997553 Defect:283808

import com.epiphany.shr.data.bio.Query;//AW 09/21/10 Incident:3997553 Defect:283808
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer; //AW 09/21/10 Incident:3997553 Defect:283808
import com.epiphany.shr.sf.util.EpnyUserContext; //AW 09/21/10 Incident:3997553 Defect:283808
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean; //AW 09/21/10 Incident:3997553 Defect:283808
import com.epiphany.shr.ui.model.data.BioCollectionBean; //AW 09/21/10 Incident:3997553 Defect:283808
import com.epiphany.shr.ui.model.data.UnitOfWorkBean; //AW 09/21/10 Incident:3997553 Defect:283808
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeForm;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface; //AW 09/21/10 Incident:3997553 Defect:283808
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.wm_item.ui.Tab;
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

public class IHSaveValidationAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(IHSaveValidationAction.class);

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @exception EpiException
	 * AW 09/21/10 Incident:3997553 Defect:283808
	 */
	protected int execute(ActionContext context, ActionResult result) throws EpiException, UserException
	{
		StateInterface state = context.getState();
		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();
		//Header Validation
		//None
		//AW 09/21/10 Incident:3997553 Defect:283808 start		
		RuntimeListFormInterface ihList = (RuntimeListFormInterface)FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_inventory_hold_list_view", state);
		
		if (!isNull(ihList)) {
			headerListValidation(ihList, context);
			
		}
		
		//AW 09/21/10 Incident:3997553 Defect:283808 end
		
		//Detail
		RuntimeFormInterface ihDetail = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_inventory_hold_detail_view", state);
		if (!isNull(ihDetail))
		{
			IHForm ihValidation = new IHForm(ihDetail, state);
			ihValidation.run(context); //AW 09/21/10 Incident:3997553 Defect:283808
		}

		return RET_CONTINUE;
	}

	/**
	 * 
	 * @param ihList
	 * @param context
	 * @throws UserException
	 * @throws EpiDataException
	 * AW 09/21/10 Incident:3997553 Defect:283808
	 */
	private void headerListValidation(RuntimeListFormInterface ihList,
			ActionContext context) throws UserException, EpiDataException {
		// TODO Auto-generated method stub

		String sessionVariable;
		String sessionObjectValue;
		BioBean selectedHold;
		
		
		String interactionID = context.getState().getInteractionId();
		
		String contextVariableSuffix = "WINDOWSTART";
		sessionVariable = interactionID + contextVariableSuffix;
		HttpSession session = context.getState().getRequest().getSession();
		sessionObjectValue = (String)session.getAttribute(sessionVariable);
		int winStart = Integer.parseInt(sessionObjectValue);
		
		//Iterate viewed list records
		int winSize = ihList.getWindowSize();
		BioCollectionBean bcb = (BioCollectionBean)ihList.getFocus();
		int bcSize = bcb.size();
		int cycle = (bcSize-winStart)<winSize ? (bcSize-winStart) : winSize ;
		for( int index = 0; index < cycle; index++ )
		{
			selectedHold = (BioBean)bcb.elementAt(index+winStart);
			
			String holdVal = selectedHold.getValue("HOLD").toString();
			Object loc = selectedHold.getValue("LOC");
		
		
			//validate if the hold can be unchecked
			if(!isNull(loc) && holdVal.equalsIgnoreCase("0")) {
	
				canInvBeRemovedFromHold(loc.toString(), context);

			}
		}

		
	}
/**
 * 
 * @param loc
 * @param context
 * @throws EpiDataException
 * @throws UserException
 * AW 09/21/10 Incident:3997553 Defect:283808
 */
	private void canInvBeRemovedFromHold(String loc, ActionContext context) throws EpiDataException, UserException {
		// TODO Auto-generated method stub
		String[] param = new String[1];
		BioCollectionBean listCollection = null;
		int size = 0;
		
		if (!locationIsOnHold(loc, context)){					    
		}	
		else {
			param[0] = loc;
			 throw new UserException("WMEXP_INVENTORY_HOLD_ON_LOC", param);
		}
			
		
	}
/**
 * 
 * @param loc
 * @param context
 * @return
 * @throws EpiDataException
 * AW 09/21/10 Incident:3997553 Defect:283808
 */
	private boolean locationIsOnHold(String loc, ActionContext context) throws EpiDataException {
		// TODO Auto-generated method stub
		BioCollectionBean listCollection = null;
		BioCollectionBean listCollectionITRN = null;
		int size = 0;
				    
    		String query = "(wm_location.LOC = '" + loc + "' AND  wm_location.LOCATIONFLAG in ( 'HOLD', 'DAMAGE' ))";
    	   _log.debug("LOG_SYSTEM_OUT","query = "+ query,100L);
    	   Query bioQuery = new Query("wm_location",query,null);
    	   UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
           listCollection = uow.getBioCollectionBean(bioQuery);
           size = listCollection.size();
        
           if( size > 0) {
        	   //location is on hold
        	   //check if another ITRNHOLD record exists for that loc
       		   String queryITRN = "(wm_inventoryhold_bio.LOC = '" + loc + "' AND  wm_inventoryhold_bio.HOLD = '1')";
	    	   _log.debug("LOG_SYSTEM_OUT","query = "+ query,100L);
	    	   Query bioQueryITRN = new Query("wm_inventoryhold_bio",queryITRN,null);
	    	   listCollectionITRN = uow.getBioCollectionBean(bioQueryITRN);
	           size = listCollectionITRN.size();
	        
	           if( size > 1) {
	        	   //allow delete
	        	  // return true;
	        	   return false;
	           }
	           else {
	        	   //throw new UserException("WMEXP_INVENTORY_HOLD_ON_SELECTED", new Object[] {});
	        	   return true;
	           }
        	   
        	   
           }
           else {
        	   return false;	   
           }

	}

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

	class IHForm extends Tab
	{
		Object lotValue;

		Object lpnValue;

		Object locValue;
		
		Object holdValue; //AW 09/21/10 Incident:3997553 Defect:283808

		public IHForm(RuntimeFormInterface f, StateInterface st)
		{
			super(f, st);
			_log.debug("LOG_DEBUG_EXTENSION", "%%% Start of " + form.getName() + " Validation", SuggestedCategory.NONE);
			lotValue = focus.getValue("LOT") == null ? null : focus.getValue("LOT").toString().toUpperCase();
			lpnValue = focus.getValue("ID") == null ? null : focus.getValue("ID").toString().toUpperCase();
			locValue = focus.getValue("LOC") == null ? null : focus.getValue("LOC").toString().toUpperCase();
			holdValue = focus.getValue("HOLD") == null ? null : focus.getValue("HOLD").toString(); //AW 09/21/10 Incident:3997553 Defect:283808
		}

		void run(ActionContext context) throws DPException, EpiException
		{

			//Check Required Fields
			requiredFieldsValidation();
			if (isInsert)
			{
				ihDuplication();
			}
			lotValidation("LOT");
			lpnValidation("ID");
			locValidation("LOC");
			removeFromHoldValidation(context);//AW 09/21/10 Incident:3997553 Defect:283808
		}
		
		/**
		 * 
		 * @param context
		 * @throws EpiDataException
		 * @throws UserException
		 * AW 09/21/10 Incident:3997553 Defect:283808
		 */
		private void removeFromHoldValidation(ActionContext context) throws EpiDataException, UserException {
			// TODO Auto-generated method stub
			String[] param = new String[1];
			Object locValue = focus.getValue("LOC");
			if (!isEmpty(locValue)) {			
			locValue = locValue == null ? null : locValue.toString().toUpperCase();
			
			if (!locationIsOnHold(locValue.toString(), context)){					    
			}	
			else {
				param[0] = locValue.toString();
				 throw new UserException("WMEXP_INVENTORY_HOLD_ON_LOC", param);
			}
			}
		}

		private void ihDuplication() throws DPException, UserException
		{
			Object statusValue = focus.getValue("STATUS") == null ? null
					: focus.getValue("STATUS").toString().toUpperCase();
			ArrayList whereClauses = new ArrayList();

			StringBuffer query = new StringBuffer("SELECT * FROM INVENTORYHOLD WHERE ");

			if (!isEmpty(lotValue))
			{
				whereClauses.add(" (LOT = '" + lotValue + "') ");
			}
			if (!isEmpty(lpnValue))
			{
				whereClauses.add(" (ID = '" + lpnValue + "') ");
			}
			if (!isEmpty(locValue))
			{
				whereClauses.add(" (LOC = '" + locValue + "') ");
			}
			if (!isEmpty(statusValue))
			{
				whereClauses.add(" (STATUS = '" + statusValue + "') ");
			}
			for (int i = 0; i < whereClauses.size(); i++)
			{
				if (i > 0)
				{
					query.append(" AND ");
				}
				query.append(whereClauses.get(i));
			}
			_log.debug("LOG_DEBUG_EXTENSION", "QUERY\n" + query, SuggestedCategory.NONE);
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query.toString());
			if (results.getRowCount() >= 1)
			{
				//value exists, throw exception
				throw new UserException("WMEXP_IH_DUPLICATE", new Object[] {});
			}

		}

		private void requiredFieldsValidation() throws UserException
		{
			if (isEmpty(lotValue) && isEmpty(lpnValue) && isEmpty(locValue))
			{
				form.getFormWidgetByName("LOT").setProperty("label class", "epnyReqdMissing");
				form.getFormWidgetByName("ID").setProperty("label class", "epnyReqdMissing");
				form.getFormWidgetByName("LOC").setProperty("label class", "epnyReqdMissing");
				//throw exception
				throw new UserException("WMEXP_IH_REQUIRED", new Object[] {});
			}

			if ((!isEmpty(locValue) && isEmpty(lpnValue) && isEmpty(lotValue))
					|| ((isEmpty(locValue)) && !(isEmpty(lpnValue)) && (isEmpty(lotValue)))
					|| ((isEmpty(locValue)) && (isEmpty(lpnValue)) && !(isEmpty(lotValue))))
			{
				//Passes
			}
			else
			{
				throw new UserException("WMEXP_IH_MANY", new Object[] {});
			}

		}

		protected void lotValidation(String attributeName) throws EpiDataException, UserException
		{
			String table = "LOT";
			String tableAttribute = "LOT";
			if (verifySingleAttribute(attributeName, table, tableAttribute) == false)
			{
				//throw exception
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel(attributeName);
				parameters[1] = focus.getValue(attributeName).toString();
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
			}
		}

		protected void lpnValidation(String attributeName) throws EpiDataException, UserException
		{
			String table = "LOTXLOCXID";
			String tableAttribute = "ID";
			if (verifyLPN(attributeName, table, tableAttribute) == false)
			{
				//throw exception
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel(attributeName);
				parameters[1] = focus.getValue(attributeName).toString();
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
			}
		}
		
		protected boolean verifyLPN(String attributeName, String table, String tableAttribute) throws EpiDataException
		{
			Object attributeValue = focus.getValue(attributeName);
			if (isEmpty(attributeValue))
			{
				return true; //Do Nothing
			}
			attributeValue = attributeValue == null ? null : attributeValue.toString().toUpperCase();
			String query = "SELECT * FROM " + table + " WHERE " + tableAttribute + " = '" + attributeValue + "' AND QTY > 0";
			_log.debug("LOG_DEBUG_EXTENSION", "Query\n" + query, SuggestedCategory.NONE);
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if (results.getRowCount() == 1)
			{
				//value exists, verified
				return true;
			}
			else
			{
				//value does not exist
				return false;
			}

		}

		protected void locValidation(String attributeName) throws EpiDataException, UserException
		{
			String table = "LOC";
			String tableAttribute = "LOC";
			if (verifySingleAttribute(attributeName, table, tableAttribute) == false)
			{
				//throw exception
				String[] parameters = new String[3];
				parameters[0] = retrieveLabel(attributeName);
				parameters[1] = focus.getValue(attributeName).toString();
				parameters[2] = retrieveFormTitle();
				throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
			}
		}
	}

}
