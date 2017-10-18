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

package com.ssaglobal.scm.wms.wm_cartonization.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
//import java.math.BigDecimal;
//import com.epiphany.shr.data.bio.BioCollectionRef;
//import com.epiphany.shr.data.bio.Query;
//import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.StringUtils;
import com.ssaglobal.scm.wms.wm_table_validation.ccf.NumericValidationCCF;
import com.ssaglobal.scm.wms.wm_table_validation.ui.FormValidation;
/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class CartonizationSaveValidationAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(CartonizationSaveValidationAction.class);
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
	protected int execute(ActionContext context, ActionResult result)
			throws EpiException
	{

		_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of CartonizationSaveValidationAction", SuggestedCategory.NONE);;

		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);

		StateInterface state = context.getState();

		RuntimeFormInterface shellForm = retrieveShellForm(state);
		RuntimeFormInterface detailForm = retrieveDetailForm(state, shellForm);
		//retrieve list form
		RuntimeListFormInterface listForm = (RuntimeListFormInterface) retrieveListForm(state, shellForm);

		// Iterate over our listform's collection of BIOs...
		BioCollectionBean listFocus = (BioCollectionBean) listForm.getFocus();
		for (int i = 0; i < listFocus.size(); i++)
		{
			BioBean cartonRecord = listFocus.get("" + i);
			if (cartonRecord.hasBeenUpdated("CUBE"))
			{
				//Validate nonnegative and not null
				Object cube = cartonRecord.getValue("CUBE");
				final String label = listForm.getFormWidgetByName("CUBE").getLabel("label", locale);
				if (cube == null)
				{
					throw new UserException("WMEXP_NO_BLANKS_ALLOWED", new Object[] { label });
				}
				FormValidation.isNegative(cube.toString(), label);

			}
			if (cartonRecord.hasBeenUpdated("MAXWEIGHT"))
			{
				Object maxWeight = cartonRecord.getValue("MAXWEIGHT");
				final String label = listForm.getFormWidgetByName("MAXWEIGHT").getLabel("label", locale);
				if (maxWeight == null)
				{
					throw new UserException("WMEXP_NO_BLANKS_ALLOWED", new Object[] { label });
				}
				FormValidation.isNegative(maxWeight.toString(), label);
			}
			if (cartonRecord.hasBeenUpdated("MAXCOUNT"))
			{
				Object maxCount = cartonRecord.getValue("MAXCOUNT");
				final String label = listForm.getFormWidgetByName("MAXCOUNT").getLabel("label", locale);
				if (maxCount == null)
				{
					throw new UserException("WMEXP_NO_BLANKS_ALLOWED", new Object[] { label });
				}
				FormValidation.isNegative(maxCount.toString(), listForm.getFormWidgetByName("MAXCOUNT").getLabel(
						"label", locale));
				//range of MaxCount +-10e16-1
				double maxCountValue = NumericValidationCCF.parseNumber(maxCount.toString());
				double maxValue = 10e16 - 1; //99999999999999999
				_log.debug("LOG_DEBUG_EXTENSION", "Max Count " + maxCount.toString(), SuggestedCategory.NONE);
				_log.debug("LOG_DEBUG_EXTENSION", "Parsed " + maxCountValue, SuggestedCategory.NONE);
				_log.debug("LOG_DEBUG_EXTENSION", "Max Value " + maxValue, SuggestedCategory.NONE);

				if (maxCountValue >= (maxValue))
				{
					String[] parameters = new String[1];
					parameters[0] = listForm.getFormWidgetByName("MAXCOUNT").getLabel("label", locale);
					throw new UserException("WMEXP_TOO_LARGE", parameters);
				}
			}
			if (cartonRecord.hasBeenUpdated("LENGTH"))
			{
				Object length = cartonRecord.getValue("LENGTH");
				final String label = listForm.getFormWidgetByName("LENGTH").getLabel("label", locale);
				if (length == null)
				{
					throw new UserException("WMEXP_NO_BLANKS_ALLOWED", new Object[] { label });
				}
				FormValidation.isNegative(length.toString(), label);
			}
			if (cartonRecord.hasBeenUpdated("WIDTH"))
			{
				Object width = cartonRecord.getValue("WIDTH");
				final String label = listForm.getFormWidgetByName("WIDTH").getLabel("label", locale);
				if (width == null)
				{
					throw new UserException("WMEXP_NO_BLANKS_ALLOWED", new Object[] { label });
				}
				FormValidation.isNegative(width.toString(), label);
			}
			if (cartonRecord.hasBeenUpdated("HEIGHT"))
			{
				Object height = cartonRecord.getValue("HEIGHT");
				final String label = listForm.getFormWidgetByName("HEIGHT").getLabel("label", locale);
				if (height == null)
				{
					throw new UserException("WMEXP_NO_BLANKS_ALLOWED", new Object[] { label });
				}
				FormValidation.isNegative(height.toString(), label);
			}
		}

		//Detail Form Validation
		if (!detailForm.getName().equalsIgnoreCase("Blank"))
		{
			DataBean detailFocus = detailForm.getFocus();

			boolean isInsert = false;
			if (detailFocus instanceof BioBean)
			{
				detailFocus = detailFocus;
				isInsert = false;
			}
			else if (detailFocus instanceof QBEBioBean)
			{
				detailFocus = detailFocus;
				isInsert = true;
			}

			//Duplication Validation
			if (isInsert)
			{
				//Check for duplicates
				//Detail View - New - Cannot have duplicate Carton Group & Carton Type.  Cannot have duplicate Carton Group & User Sequence
				//CARTONIZATIONGROUP && USESEQUENCE
				Object cGroup = isNull(detailFocus.getValue("CARTONIZATIONGROUP")) ? null : detailFocus.getValue("CARTONIZATIONGROUP").toString().toUpperCase();
				Object cType = isNull(detailFocus.getValue("CARTONTYPE")) ? null : detailFocus.getValue("CARTONTYPE").toString().toUpperCase();
				Object useSeq = detailFocus.getValue("USESEQUENCE");
				if (isNull(useSeq))
				{
					useSeq = "1";
				}
				String queryType = "SELECT * FROM CARTONIZATION WHERE (CARTONIZATIONGROUP = '" + cGroup	+ "') AND (CARTONTYPE = '"
						 + cType + "')";
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Query\n" + queryType, SuggestedCategory.NONE);;
				EXEDataObject resultsType = WmsWebuiValidationSelectImpl.select(queryType);
				if (resultsType.getRowCount() > 0)
				{
					throw new UserException("WMEXP_CARTONIZATION_DUPLICATE", new Object[] {});
				}
				String querySeq = "SELECT * FROM CARTONIZATION WHERE (CARTONIZATIONGROUP = '" + cGroup + "') AND (USESEQUENCE = '"
						+ useSeq + "')";
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Query\n" + querySeq, SuggestedCategory.NONE);;
				EXEDataObject resultsSeq = WmsWebuiValidationSelectImpl.select(querySeq);
				if (resultsSeq.getRowCount() > 0)
				{
					throw new UserException("WMEXP_CARTONIZATION_DUPLICATE_SEQUENCE", new Object[] {});
				}

			}

			//Validate Nonnegative
			Object cube = detailFocus.getValue("CUBE");
			Object maxWeight = detailFocus.getValue("MAXWEIGHT");
			Object maxCount = detailFocus.getValue("MAXCOUNT");
			Object length = detailFocus.getValue("LENGTH");
			Object width = detailFocus.getValue("WIDTH");
			Object height = detailFocus.getValue("HEIGHT");
			
			// Seshu	3PL Enhancements - Container Exchange 1/12/2009 Starts
			Object containereXchangeFlag = detailFocus.getValue("CONTAINEREXCHANGEFLAG");
			if(containereXchangeFlag != null && containereXchangeFlag.toString().equalsIgnoreCase("1"))
			{
				String cartonDesc = detailFocus.getValue("CARTONDESCRIPTION").toString();
				BioCollectionBean listCollection = null;
				int size = 0;
				try {        			  
					String sQueryString = "(wm_cartonization.CARTONDESCRIPTION = '" + cartonDesc + "' AND wm_cartonization.CONTAINEREXCHANGEFLAG = 1)";    	   
   	  	    	  	Query BioQuery = new Query("wm_cartonization",sQueryString,null);
   	  	    	  	UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
   	  	    	  	listCollection = uow.getBioCollectionBean(BioQuery);  
   	  	    	  	size = listCollection.size();
   	  	          
        		  } catch(Exception e) {  	        
   	  		        // Handle Exceptions 
   	  			    e.printStackTrace();
   	  			    return RET_CANCEL;  		    
        		  }
        		  if (size > 0) {
      		    	String[] ErrorParem = new String[2];
      		 	   	ErrorParem[0]= cartonDesc;
      		 	   	ErrorParem[1] = "Carton";
      		 	   	FieldException UsrExcp = new FieldException(detailForm, "CARTONDESCRIPTION","WMEXP_CNTRXCHANGE_CNTR_EXISTS", ErrorParem);
      		 	   	throw UsrExcp; 	   
        		  }
			}
			// Seshu 3PL Enhancements - Container Exchange Ends

			if (!isEmpty(cube))
			{
				FormValidation.isNegative(cube.toString(), detailForm.getFormWidgetByName("CUBE").getLabel("label", locale));
			}

			if (!isEmpty(maxWeight))
			{
				FormValidation.isNegative(maxWeight.toString(), detailForm.getFormWidgetByName("MAXWEIGHT").getLabel("label", locale));
			}

			if (!isEmpty(maxCount))
			{
				FormValidation.isNegative(maxCount.toString(), detailForm.getFormWidgetByName("MAXCOUNT").getLabel("label", locale));
				//range of MaxCount +-10e16-1
				double maxCountValue = NumericValidationCCF.parseNumber(maxCount.toString());
				double maxValue = 10e16 - 1; //99999999999999999
				_log.debug("LOG_DEBUG_EXTENSION", "Max Count " + maxCount.toString(), SuggestedCategory.NONE);;
				_log.debug("LOG_DEBUG_EXTENSION", "Parsed " + maxCountValue, SuggestedCategory.NONE);;
				_log.debug("LOG_DEBUG_EXTENSION", "Max Value " + maxValue, SuggestedCategory.NONE);;

				if (maxCountValue >= (maxValue))
				{
					String[] parameters = new String[1];
					parameters[0] = removeTrailingColon(detailForm.getFormWidgetByName("MAXCOUNT").getLabel("label", locale));
					throw new UserException("WMEXP_TOO_LARGE", parameters);
				}
			}

			if (!isEmpty(length))
			{
				FormValidation.isNegative(length.toString(), detailForm.getFormWidgetByName("LENGTH").getLabel("label", locale));
			}

			if (!isEmpty(width))
			{
				FormValidation.isNegative(width.toString(), detailForm.getFormWidgetByName("WIDTH").getLabel("label", locale));
			}

			if (!isEmpty(height))
			{
				FormValidation.isNegative(height.toString(), detailForm.getFormWidgetByName("HEIGHT").getLabel("label", locale));
			}

			//Validate Defaults
			validateDefaultFlag("DEFAULTPALLETCARTON", state, detailFocus, detailForm);
			validateDefaultFlag("DEFAULTOTHERCARTON", state, detailFocus, detailForm);
		}
		return RET_CONTINUE;
	}

	private void validateDefaultFlag(String defaultFlag, StateInterface state, DataBean focus, RuntimeFormInterface form) throws EpiDataException, UserException {
		_log.debug("LOG_DEBUG_EXTENSION_CartonizationSaveValidationAction_validateDefaultFlag", "Checking Flag " + defaultFlag, SuggestedCategory.NONE);
		String cartKey = BioAttributeUtil.getString(focus, "CARTONIZATIONKEY");
		int defaultFlagValue = BioAttributeUtil.getInt(focus, defaultFlag);
		if (defaultFlagValue == 1) {
			//ensure that there are no other records with this default set to 1
			UnitOfWorkBean tuow = state.getTempUnitOfWork();
			BioCollectionBean rs = tuow.getBioCollectionBean(new Query("wm_cartonization", "wm_cartonization.CARTONIZATIONKEY != '" + cartKey + "'" + " and " +
																							"wm_cartonization." + defaultFlag + " = '1'", null));
			if (rs.size() >= 1) {
				BioBean other = rs.get("0");
				_log.error("LOG_ERROR_EXTENSION_CartonizationSaveValidationAction_execute", "Unable to set default, an other record is set to default", SuggestedCategory.NONE);
				throw new UserException("WMEXP_CART_DEFAULT", new Object[] { StringUtils.removeTrailingColon(FormUtil.getWidgetLabel(state, form.getFormWidgetByName(defaultFlag))),
						BioAttributeUtil.getString(other, "CARTONDESCRIPTION") });
			}
		}
	}

	private RuntimeFormInterface retrieveShellForm(StateInterface state)
	{
		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();
		_log.debug("LOG_DEBUG_EXTENSION", "\n1'''Current form  = " + shellToolbar.getName(), SuggestedCategory.NONE);;

		RuntimeFormInterface shellForm = shellToolbar.getParentForm(state);
		_log.debug("LOG_DEBUG_EXTENSION", "\n2'''Current form  = " + shellForm.getName(), SuggestedCategory.NONE);;
		return shellForm;
	}

	private RuntimeFormInterface retrieveDetailForm(StateInterface state, RuntimeFormInterface shellForm)
	{
		//Detail
		SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");

		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
		_log.debug("LOG_DEBUG_EXTENSION", "\n3'''Current form  = " + detailForm.getName(), SuggestedCategory.NONE);;

		return detailForm;
	}

	private RuntimeFormInterface retrieveListForm(StateInterface state, RuntimeFormInterface shellForm)
	{
		//List
		SlotInterface listSlot = shellForm.getSubSlot("list_slot_1");

		RuntimeFormInterface listForm = state.getRuntimeForm(listSlot, null);
		_log.debug("LOG_DEBUG_EXTENSION", "\n3'''Current form  = " + listForm.getName(), SuggestedCategory.NONE);;

		return listForm;
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

	protected String removeTrailingColon(String label)
	{
		if (label.endsWith(":"))
		{
			label = label.substring(0, label.length() - 1);
		}
		return label;
	}

}
