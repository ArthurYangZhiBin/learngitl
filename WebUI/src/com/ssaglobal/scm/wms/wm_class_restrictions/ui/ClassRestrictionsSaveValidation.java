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


package com.ssaglobal.scm.wms.wm_class_restrictions.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.ssaglobal.scm.wms.wm_table_validation.ui.FormValidation;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
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

public class ClassRestrictionsSaveValidation extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ClassRestrictionsSaveValidation.class);
	String itemClass;

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

		_log.debug("LOG_DEBUG_EXTENSION", "ClassRestrictionsSaveValidation" , SuggestedCategory.NONE);;
		
		StateInterface state = context.getState();

		//Header Validation
		RuntimeFormInterface crHeader = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell",
															"wm_class_restrictions_header_detail_view", state);

		if (!isNull(crHeader))
		{

			CRHeader crHeaderValidation = new CRHeader(crHeader, state);
			crHeaderValidation.run();
		}

		//Detail Validation
		ArrayList tabs = new ArrayList();
		tabs.add("wm_class_restrictions_detail_detail");
		RuntimeFormInterface crDetail = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell",
															"wm_class_restrictions_detail_detail_view", tabs, state);

		if (!isNull(crDetail))
		{
			_log.debug("LOG_DEBUG_EXTENSION", "" + crDetail.getName() + "", SuggestedCategory.NONE);;
			CRDetail crDetailValidation = new CRDetail(crDetail, state);
			crDetailValidation.run();
		}

		return RET_CONTINUE;
	}

	public class CRHeader extends FormValidation
	{
		public CRHeader(RuntimeFormInterface f, StateInterface st)
		{
			super(f, st);
			//set itemClass
			itemClass = isNull(focus.getValue("ITEMCLASS"))  ? null : focus.getValue("ITEMCLASS").toString().toUpperCase();
		}

		public void run() throws DPException, UserException
		{
			//prevent duplicates
			if (isInsert)
			{
				crHDuplication();
				//check class
				checkClass(itemClass);

			}

		}

		void checkClass(String itemClass) throws DPException, UserException
		{

			String query = "SELECT * FROM SKU WHERE CLASS = '" + itemClass + "'";
			_log.debug("LOG_DEBUG_EXTENSION", "///QUERY " + query, SuggestedCategory.NONE);;
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if (results.getRowCount() == 0)
			{
				//class needs to exist in sku
				String[] parameters = new String[2];
				parameters[0] = retrieveLabel("ITEMCLASS");
				parameters[1] = itemClass;
				throw new UserException("WMEXP_INVALIDCLASS", parameters);
			}

		}

		void crHDuplication() throws DPException, UserException
		{
			String query = "SELECT * FROM CLASSRESTRICTION WHERE ITEMCLASS = '" + itemClass + "'";
			_log.debug("LOG_DEBUG_EXTENSION", "///QUERY " + query, SuggestedCategory.NONE);;
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if (results.getRowCount() >= 1)
			{
				//				value exists, throw exception
				String[] parameters = new String[2];
				parameters[0] = retrieveLabel("ITEMCLASS");
				parameters[1] = itemClass;
				throw new UserException("WMEXP_DEFAULT_DUPLICATE", parameters);
			}
		}

	}

	public class CRDetail extends FormValidation
	{
		String restrictedClass;

		public CRDetail(RuntimeFormInterface f, StateInterface st)
		{
			super(f, st);
			restrictedClass = isNull(focus.getValue("RESTRICTEDCLASS")) ? null : focus.getValue("RESTRICTEDCLASS").toString().toUpperCase();

		}

		public void run() throws DPException, UserException
		{
			
			if (isInsert)
			{
				//prevent duplicates
				crDDuplication();
				//check class
				checkClass(restrictedClass);
			}

		}

		void crDDuplication() throws DPException, UserException
		{
			String query = "SELECT * FROM CLASSRESTRICTIONDETAIL WHERE ITEMCLASS = '" + itemClass
					+ "' AND RESTRICTEDCLASS = '" + restrictedClass + "'";
			_log.debug("LOG_DEBUG_EXTENSION", "///QUERY " + query, SuggestedCategory.NONE);;
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if (results.getRowCount() >= 1)
			{
				//value exists, throw exception
				String[] parameters = new String[2];
				parameters[0] = retrieveLabel("RESTRICTEDCLASS");
				parameters[1] = restrictedClass;
				throw new UserException("WMEXP_DEFAULT_DUPLICATE", parameters);
			}
		}

		void checkClass(String itemClass) throws DPException, UserException
		{

			String query = "SELECT * FROM SKU WHERE CLASS = '" + itemClass + "'";
			_log.debug("LOG_DEBUG_EXTENSION", "///QUERY " + query, SuggestedCategory.NONE);;
			EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
			if (results.getRowCount() == 0)
			{
				//class needs to exist in sku
				String[] parameters = new String[2];
				parameters[0] = retrieveLabel("RESTRICTEDCLASS");
				parameters[1] = itemClass;
				throw new UserException("WMEXP_INVALIDCLASS", parameters);
			}

		}

	}

	boolean isNull(Object attributeValue)
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
