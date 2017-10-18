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


package com.ssaglobal.scm.wms.wm_date_format.ui;

// Import 3rd party packages and classes
import java.util.ArrayList;
import java.math.BigDecimal;

import javax.servlet.http.HttpSession;

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.ssaglobal.scm.wms.service.dutilitymanagement.*;
import com.ssaglobal.scm.wms.service.dutilitymanagement.datecodeformat.*;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class SaveDateFormat extends com.epiphany.shr.ui.action.ActionExtensionBase 
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(SaveDateFormat.class);
	private void checkDateFormatHeader(ActionContext context, DataBean dateFormat) throws UserException, EpiException
	{
		try
		{
			String inputDateCodeDesc = (String)dateFormat.getValue("DATECODEDESC");
			
			if ( inputDateCodeDesc.trim().length() > 50 )
			{
				_log.debug("LOG_SYSTEM_OUT","["+inputDateCodeDesc+"]",100L);
				_log.debug("LOG_SYSTEM_OUT","["+inputDateCodeDesc.trim()+"]",100L);
				throw new UserException("WMEXP_DF_DESCTOOLONG", new Object[0]);
			}			
			
			if ( dateFormat.isTempBio() )
			{
				// a new record, so check for the new conditions
			    
			    //check for DATECODEFORMAT being used in the DB already.
			    String inputDateCodeFormat = (String)dateFormat.getValue("DATECODEFORMAT");
			    DateFormatDTO existingDateFormat = DateFormatDAO.getDateFormat(context, inputDateCodeFormat);
			    String existingDateCodeFormat = existingDateFormat.getDateCodeFormat();
			    if ( existingDateCodeFormat != null && existingDateCodeFormat.equals(inputDateCodeFormat) )
			    {
			    	throw new UserException("WM_DF_DCINUSE", new Object[0]);
			    }  
			}
			else
			{
				// existing record
			}			
		}
		catch (UserException e)
		{
			throw e;
		}
		catch (EpiException e)
		{
			e.printStackTrace();
			throw e;
		}
	}

	private void checkDateFormatDetail(ActionContext context, RuntimeFormInterface toolbar, DataBean dateFormat, DataBean dateFormatDetail, String dateCodeFormat) throws UserException, EpiException
	{
		try
		{
			String dateEle = (String)dateFormatDetail.getValue("DATEELEMENT");				
			String fldType = (String)dateFormatDetail.getValue("FIELDTYPE");
			String dateType = (String)dateFormatDetail.getValue("DATETYPE");
			boolean haveMonth = dateEle.equals("M") ? true : false;
			boolean haveDay = dateEle.equals("D") ? true : false;;
			boolean haveYear = dateEle.equals("Y") ? true : false;;
			String howTo = (String)dateFormatDetail.getValue("HOWTOPROCESS");
			ArrayList<DateFormatDetailDTO> existingDateFormatDetails = DateFormatDetailDAO.getDateFormatDetail(context, dateCodeFormat);

			if ( !dateEle.equals("M") && !dateEle.equals("D") && !dateEle.equals("Y") && !dateEle.equals("N") )
			{
				throw new UserException("WMEXP_DF_INVALIDDATEELEMENT", new Object[0]);
			}
			
			if ( !fldType.equals("C") && !fldType.equals("N") )
			{
				throw new UserException("WMEXP_DF_INVALIDFLDTYPE", new Object[0]);
			}	
			
			if ( dateEle.equals("M") && !( dateType.equals("G") || dateType.equals("J") || dateType.equals("C") ) )
			{
				throw new UserException("WMEXP_DF_INVALIDDATETYPE1", new Object[0]);
			}
			
			if ( dateEle.equals("D") && dateEle.equals("Y") && !( dateType.equals("G") || dateType.equals("J")) )
			{
				throw new UserException("WMEXP_DF_INVALIDDATETYPE2", new Object[0]);
			}
			
			if ( (dateEle.equals("M") || dateEle.equals("D") || dateEle.equals("Y")) && !(howTo.equals("V") || howTo.equals("C")) )
			{
				throw new UserException("WMEXP_DF_INVALIDPROCESS1", new Object[0]);
			}
			else if ( dateEle.equals("N") && !howTo.equals("V"))
			{
				throw new UserException("WMEXP_DF_INVALIDPROCESS2", new Object[0]);
			}	
			
			if (dateType != null)
			{
				if ( dateType.equals("C") )
				{
					StateInterface state = context.getState();
					HttpSession session = state.getRequest().getSession();
					
					String custCode = (String)session.getAttribute("CUSTOMCODE");
					
					_log.debug("LOG_SYSTEM_OUT","CUSTOMCODE?",100L);
					_log.debug("LOG_SYSTEM_OUT",custCode,100L);
					dateFormat.setValue("CUSTOMCODEFORMAT", custCode);
				}
			}
			else
			{
				// if the widget value is null, set it to blank as the db field doesn't allow nulls
				dateFormatDetail.setValue("DATETYPE", " ");
			}
			
			// adding a new detail record.	
			int beg = 0;
			int end = 0;
			Object begPos = dateFormatDetail.getValue("BEGPOSITION");
			Object endPos = dateFormatDetail.getValue("ENDPOSITION");
			
			if (begPos instanceof Integer)
			{
				beg = ((Integer)begPos).intValue();	
				end = ((Integer)endPos).intValue();
			}
			else if (begPos instanceof BigDecimal)
			{
				beg = ((BigDecimal)begPos).intValue();	
				end = ((BigDecimal)endPos).intValue();
			}
			
			if ( beg > end )
			{
				throw new UserException("WMEXP_DF_INVALIDPOS", new Object[0]);
			}	
			
			String fullDateType = dateType != null && dateType != "C" ? dateType : "NONE";
			for ( int idx = 0; idx < existingDateFormatDetails.size(); ++idx )
			{
				// we have existing records! need to make extra checks against
				DateFormatDetailDTO row = existingDateFormatDetails.get(idx);
				String rowDateType = row.getDateType();
				
				if (fullDateType.equals("NONE") && rowDateType != null && (rowDateType.equals("G") || rowDateType.equals("J")) )
				{
					fullDateType = rowDateType;
				}
				
				int currSeq = 0;
				Object tempSeq = dateFormatDetail.getValue("DATECODEFORMATSEQUENCE");
				if (tempSeq instanceof BigDecimal)
				{
					currSeq = ((BigDecimal)tempSeq).intValue();
				}
				else
				{
					currSeq = ((Integer)tempSeq).intValue();
				}
				
				if ( row.getDateCodeFormateSequence() == currSeq)
				{
					continue;
				}
				
				haveMonth = row.getDateElement().equals("M") ? true : haveMonth;
				haveDay = row.getDateElement().equals("D") ? true : haveDay;
				haveYear = row.getDateElement().equals("Y") ? true : haveYear;
				
				// new record DATEELEMENT can not be defined within a saved record
				if ( row.getDateElement().equals(dateEle))
				{
					throw new UserException("WMEXP_DF_DUPDATEELEMENT", new Object[0]);
				}
				
				int rBeg = row.getBegPosition();
				int rEnd = row.getEndPosition();
				
				if (( beg >= rBeg && beg <= rEnd ) || ( end >= rBeg && end <= rEnd ) )
				{
					throw new UserException("WMEXP_DF_INVALIDPOS", new Object[0] );
				}	
				
				if (dateType != null && !dateType.equals(row.getDateType()) && !(dateType.equals("C") || row.getDateType().equals("C")) && !(dateEle.equals("N") || row.getDateElement().equals("N")))
				{
					throw new UserException("WMEXP_DF_INVALIDDATETYPE3", new String[]{row.getDateType()});
				}
			}	
			
			if ( ((!fullDateType.equals("J") && haveMonth && haveDay && haveYear) || ( fullDateType.equals("J") && haveDay && haveYear )) )
			{
				_log.debug("LOG_SYSTEM_OUT","enable test button",100L);
				// need the test and active buttons to be enabled.
				toolbar.getFormWidgetByName("TEST").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);		
			}
			else
			{
				_log.debug("LOG_SYSTEM_OUT","disable test button",100L);
				toolbar.getFormWidgetByName("TEST").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);								
			}
		}
		catch (UserException e)
		{
			throw e;
		}
		catch (EpiException e)
		{
			e.printStackTrace();
			throw e;
		}		
	}
	
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
	protected int execute( ActionContext context, ActionResult result ) throws EpiException, UserException
	{
			StateInterface state = context.getState();
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			
			// the active form will be the toolbar since the button that is
			// activating this extension resides there.
			//
			// the parent of the toolbar will be the WMS_LIST_SHELL
			//
			//list_slot_1 in the WMS_LIST_SHELL contains the parent form, which contains
			// the bio for the wm_dateformat			
			//-RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();

			RuntimeFormInterface toolbar = FormUtil.findForm(state.getCurrentRuntimeForm(),"wms_list_shell","wm_list_shell_date_format Toolbar",state);
			RuntimeFormInterface shellForm = toolbar.getParentForm(state);
			SlotInterface header = shellForm.getSubSlot("list_slot_1");
			SlotInterface detail = shellForm.getSubSlot("list_slot_2");
			RuntimeFormInterface headerForm = state.getRuntimeForm(header, null);
			DataBean headerFocus = headerForm.getFocus();
			RuntimeFormInterface detailForm = state.getRuntimeForm(detail, null);
			DataBean detailFocus = detailForm.getFocus();
			
			checkDateFormatHeader(context, headerFocus);

			BioBean headerBioBean = null;
			if ( headerFocus.isTempBio() )
			{
				// its a new record, which means the detail is also new.				
			    headerBioBean = uowb.getNewBio((QBEBioBean)headerFocus);
			    RuntimeFormInterface detailTab = state.getRuntimeForm(detail, "Detail");
				detailFocus = detailTab.getFocus();
				
				if( detailFocus != null )
				{					
					checkDateFormatDetail(context, toolbar, headerFocus, detailFocus, (String)headerFocus.getValue("DATECODEFORMAT"));
				    headerBioBean.addBioCollectionLink("Formatdetail", (QBEBioBean)detailFocus);
				}
			}	
			else
			{
				// header is existing.  detail might be new.
				//it is for update header				
			    headerBioBean = (BioBean)headerFocus;
			    SlotInterface toggleSlot = detailForm.getSubSlot("wm_date_format_detail_toggle_slot");					
			    RuntimeFormInterface detailTab = state.getRuntimeForm(toggleSlot, "Detail");
				detailFocus = detailTab.getFocus();
				
				if ( detailFocus != null )
				{
					_log.debug("LOG_SYSTEM_OUT","have a detail focus...",100L);
					checkDateFormatDetail( context, toolbar, headerFocus, detailFocus, (String)headerFocus.getValue("DATECODEFORMAT") );
					if (detailFocus.isTempBio())
					{
						detailFocus = (QBEBioBean)detailFocus;
						headerBioBean.addBioCollectionLink("Formatdetail", (QBEBioBean)detailFocus);
					}
				}
				else
				{
					_log.debug("LOG_SYSTEM_OUT","no detail focus",100L);
				}
			}

			try
			{ 
				// if the user is making a change, 
				// force a retest of the date conversion
				// prior to saving
				headerFocus.setValue("CONVERTEDDATE", null);
				headerFocus.setValue("ACTIVE", 0);
				toolbar.getFormWidgetByName("ACTIVATE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);								
				toolbar.getFormWidgetByName("DEACTIVATE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);								

				
				uowb.saveUOW(true);
				uowb.clearState();
				result.setFocus(headerBioBean);
			}
			catch (UnitOfWorkException ex)
			{			 
				Throwable nested = ((UnitOfWorkException) ex).getDeepestNestedException();
					
				if(nested instanceof ServiceObjectException)
				{
					String reasonCode = nested.getMessage();
					throwUserException(ex, reasonCode, null);
				}
				else
				{
					throwUserException(ex, "ERROR_DELETING_BIO", null);
				}				
			} 
			catch (EpiException ex)
			{
				throwUserException(ex, "ERROR_DELETING_BIO", null);
			}
						
			//enable the test button

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
	*/
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException 
	{
		try 
		{
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
			return RET_CANCEL;          
		} 

		return RET_CONTINUE;
	}
}
