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
package com.ssaglobal.scm.wms.wm_internal_transfer.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionObjectInterface;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.service.dutilitymanagement.DateFormatCustomDAO;
import com.ssaglobal.scm.wms.service.dutilitymanagement.DateFormatDAO;
import com.ssaglobal.scm.wms.service.dutilitymanagement.DateFormatDetailDAO;
import com.ssaglobal.scm.wms.service.dutilitymanagement.datecodeformat.DateFormatConversion;
import com.ssaglobal.scm.wms.service.dutilitymanagement.datecodeformat.DateFormatCustomDTO;
import com.ssaglobal.scm.wms.service.dutilitymanagement.datecodeformat.DateFormatDTO;
import com.ssaglobal.scm.wms.service.dutilitymanagement.datecodeformat.DateFormatDetailDTO;
import com.ssaglobal.scm.wms.util.FormUtil;

public class InternalTransferConvertLottable extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(InternalTransferConvertLottable.class);
	
	private Boolean IsNumeric(String val)
	{
		Boolean retVal = Boolean.TRUE;
		
		try
		{
			Integer.parseInt(val);
		}
		catch (NumberFormatException e)
		{
			retVal = Boolean.FALSE;
		}
		
		return retVal;
	}
	
	private BioBean getSkuBIO( ActionContext context )
	{
		try
		{
			StateInterface state = context.getState();
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();		
			final String TopLevelForm = "wm_internal_transfer_detail_detail_view";
			final String ToForm = "wm_internal_transfer_detail_detail_to_slot";
			
			RuntimeFormInterface LottablesDetailForm = state.getCurrentRuntimeForm();
			
			RuntimeFormInterface detailForm = FormUtil.findForm(LottablesDetailForm.getParentForm(state),"",TopLevelForm,state);
			String detailFormName = detailForm.getName();
			
			RuntimeFormInterface toForm = FormUtil.findForm(LottablesDetailForm.getParentForm(state),"",ToForm,state);
			String toFormName = toForm.getName();
			RuntimeFormWidgetInterface skuVal= toForm.getFormWidgetByName("TOSKU");

			RuntimeFormWidgetInterface toLotWidget = toForm.getFormWidgetByName("TOLOT");
			toLotWidget.setDisplayValue(" ");
			toLotWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);


			DataBean toLotFocus = toForm.getFocus();
			toLotFocus.setValue("TOLOT", " ");
			toForm.setFocus(toLotFocus);
			
			

			RuntimeFormInterface shellForm= detailForm.getParentForm(state);
			while(!shellForm.getName().equals("wms_list_shell")){
				shellForm = shellForm.getParentForm(state);
			}

			
			SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
			RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
			String headerFormName = headerForm.getName();
			
			RuntimeFormWidgetInterface storerVal= headerForm.getFormWidgetByName("TOSTORERKEY");
			
			
		
			Object skuObj = skuVal.getValue();
			Object storerObject = storerVal.getValue();
			if (skuObj != null && storerObject != null)
			{
				BioCollectionBean listCollection = null;
				String sku = skuObj.toString().trim();
				String storerKey = storerObject.toString().trim();
				_log.debug("LOG_SYSTEM_OUT","SKU ="+ skuObj.toString()+ "End",100L);
				String sQueryString = "(wm_sku.STORERKEY = '"+storerKey+"' AND wm_sku.SKU = '"+sku+"')";
				_log.debug("LOG_SYSTEM_OUT","sQueryString = "+ sQueryString,100L);
				Query bioQuery = new Query("wm_sku",sQueryString,null);
				listCollection = uowb.getBioCollectionBean(bioQuery);
				BioBean skuBio = (BioBean)listCollection.elementAt(0);
				return skuBio;
			}
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			return null;
		}		
		
		return null;
	}

	private BioBean getLottable( ActionContext context, String valkey ) throws EpiException
	{
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		BioCollectionBean listCollection = null;
		String sQueryString = "(wm_lottablevalidationdetail.LOTTABLEVALIDATIONKEY = '"+valkey+"' ) ";

		_log.debug("LOG_SYSTEM_OUT","sQueryString = "+ sQueryString,100L);
		Query bioQuery = new Query("wm_lottablevalidationdetail",sQueryString,null);
		listCollection = uowb.getBioCollectionBean(bioQuery);
		
		if ( listCollection.size() > 0 )
		{
			return (BioBean)listCollection.elementAt(0);
		}
		
		return null;
	}

	private BioBean getCodeLookup( ActionContext context, String code ) throws EpiException
	{
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		BioCollectionBean listCollection = null;
		String sQueryString = "(codelkup.CODE = '"+code+"' and codelkup.LISTNAME = 'LOTCONPROC') ";

		_log.debug("LOG_SYSTEM_OUT","sQueryString = "+ sQueryString,100L);
		Query bioQuery = new Query("codelkup",sQueryString,null);
		listCollection = uowb.getBioCollectionBean(bioQuery);
		
		if ( listCollection.size() > 0 )
		{
			return (BioBean)listCollection.elementAt(0);
		}
		
		return null;
	}
	
	private BioCollectionBean getCustomDateFormat( ActionContext context, String code ) throws EpiException
	{
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		BioCollectionBean listCollection = null;		
		String sQueryString = "(wm_dateformat.DATEFORMATCODE = '"+code+"' ) ";
		Query bioQuery = new Query("wm_dateformat",sQueryString,null);
		listCollection = uowb.getBioCollectionBean(bioQuery);
		
		String custCode = ((BioBean)listCollection.elementAt(0)).getString("CUSTOMCODEFORMAT");

		sQueryString = "(wm_dateformatcustom.CUSTOMCODEFORMAT = '"+custCode+"' ) ";
		_log.debug("LOG_SYSTEM_OUT","sQueryString = "+ sQueryString,100L);
		bioQuery = new Query("wm_dateformatcustom",sQueryString,null);
		listCollection = uowb.getBioCollectionBean(bioQuery);
		
		if ( listCollection.size() > 0 )
		{
			return listCollection;
		}
		
		return null;
	}
	
	public String convertCodeToDate( ActionContext context, String value, ArrayList<DateFormatDetailDTO> dateFmt ) throws EpiException
	{
		Calendar cal = Calendar.getInstance();
		int length = value.length();
		
		String newDate = "";
		String year = "";
		String month = "";
		String day = "";
		_log.debug("LOG_SYSTEM_OUT","Incoming field value is :" + value,100L);
		
		for ( int idx = 0; idx < dateFmt.size(); ++idx )
		{
			DateFormatDetailDTO row = dateFmt.get(idx);
				
			// values can be Y(year), M(month), D(day) or N(none)
			String dateEle = row.getDateElement();
				
			// values can be G(gregorian), J(julian) or C(custom)
			String dateType = row.getDateType();
				
			// values can be C(character) or N(numeric)
			String fldType = row.getFieldType();
				
			// values can be C(convert) or V(validate)
			String howToProc = row.getHowToProcess();
			
			// adjusting the begin value to be ZERO based for string manipulations
			int begin = row.getBegPosition()-1;
			int end = row.getEndPosition();
				
			if (begin > end || end > length)
			{
				_log.debug("LOG_SYSTEM_OUT","begin ["+begin+"]",100L);
				_log.debug("LOG_SYSTEM_OUT","end ["+end+"]",100L);
				throw new UserException("WMEXP_INVALIDDATESTRING", new Query[0]);
			}
			
			BioCollectionBean list = null;
			if ( dateType.equalsIgnoreCase("C"))
			{
				list = getCustomDateFormat(context,row.getDateCodeFormat());
			}
			
			String sub = value.substring(begin, end);
			_log.debug("LOG_SYSTEM_OUT","processing next row : "+sub+" | "+dateEle+" | "+dateType+" | "+fldType+" | "+howToProc+" | "+begin+" | "+end,100L);

			Boolean isNum = IsNumeric(sub);
			if ( (fldType.equalsIgnoreCase("C") && isNum) || (fldType.equalsIgnoreCase("N") && !isNum) )
			{
				_log.debug("LOG_SYSTEM_OUT","fieldType ["+fldType+"] isNum ["+isNum+"] ",100L);
				throw new UserException("WMEXP_INVALIDDATESTRING", new Query[0]);
			}			
			
			if (dateEle.equalsIgnoreCase("Y"))
			{
				if ( dateType.equalsIgnoreCase("G"))
				{
					year = sub;
				}
				else if ( dateType.equalsIgnoreCase("J") )
				{					
				}
			}
			else if (dateEle.equalsIgnoreCase("M"))
			{
				if ( dateType.equalsIgnoreCase("G"))
				{
					month = sub;
				}
				else if ( dateType.equalsIgnoreCase("C"))
				{	
					for ( int sIdx = 0; sIdx < list.size(); ++sIdx)
					{
						if ( ((BioBean)list.elementAt(sIdx)).getString("MONTHDESCRIPTION").equalsIgnoreCase(sub))
						{
							month = ((BioBean)list.elementAt(sIdx)).getString("MONTHELEMENT");
							break;
						}
					}
				}				
			}
			else if (dateEle.equalsIgnoreCase("D"))
			{
				if ( dateType.equalsIgnoreCase("G"))
				{
					day = sub;
				}
				else if ( dateType.equalsIgnoreCase("J"))
				{					
				}
			}
		}
		
		try
		{
			_log.debug("LOG_SYSTEM_OUT","year ["+year+"] month ["+month+"] day ["+day+"]",100L);
			cal.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day) );
		}
		catch ( NumberFormatException e )
		{
			_log.debug("LOG_SYSTEM_OUT","bad date",100L);
			throw new UserException("WMEXP_INVALIDDATESTRING", new Object[0]);
		}
		
		newDate = cal.toString();
		_log.debug("LOG_SYSTEM_OUT","Date Value is : "+newDate,100L);
		return newDate;
	}
	
	private void checkCodeConvert( ActionContext context, String widget, BioBean val, BioBean sku ) throws EpiException
	{
		_log.debug("LOG_SYSTEM_OUT","After field logic for " + widget,100L);
		
		String rc = val.getString(widget+"RECEIPTCONVERSION");
		_log.debug("LOG_SYSTEM_OUT","Conversion is ["+rc+"]",100L);
		if ( rc != null && ! rc.equalsIgnoreCase("NONE"))
		{
			BioBean code = getCodeLookup(context,val.getString(widget+"RECEIPTCONVERSION"));	
			if ( code != null )
			{	
				_log.debug("LOG_SYSTEM_OUT","CODE: "+code.getString("CODE"),100L);
				
				GregorianCalendar calendar = null;   
	            ArrayList<DateFormatCustomDTO> dateFormatCustomList = null;        
	            ArrayList<DateFormatDetailDTO> dateFormatDetails = DateFormatDetailDAO.getDateFormatDetail(context, val.getString(widget+"DATEFORMAT") );
	            DateFormatDTO dateFormat = DateFormatDAO.getDateFormat(context, val.getString(widget+"DATEFORMAT"));

	            if ( dateFormat.getCustomCodeFormat() == null )
	            {
	            	dateFormatCustomList = null;
	            }
	            else
	            {
	            	dateFormatCustomList = DateFormatCustomDAO.getDateFormatCustoms(context, dateFormat.getCustomCodeFormat());
	            }            

	            DateFormatConversion converter = new DateFormatConversion();
				StateInterface state = context.getState();					
				RuntimeFormInterface detailForm = state.getCurrentRuntimeForm();
				DataBean detailFocus = detailForm.getFocus();
				
	            calendar = converter.setDateFromCodeFormat(detailForm.getFormWidgetByName(widget).getDisplayValue(), val.getString(widget+"DATEFORMAT"), dateFormatDetails, dateFormatCustomList);
				
	            if ( calendar != null )
	            {
					detailFocus.setValue(code.getString("SHORT").toUpperCase(), calendar);
	            }
	            else
	            {
	            	detailForm.getFormWidgetByName(widget).setProperty("cursor focus widget","true");		
	            	throw new UserException("WMEXP_INVALIDDATESTRING", new Object[0]);
	            }
	            
	            Object lottable04 = detailFocus.getValue("LOTTABLE04");
	            Object lottable05 = detailFocus.getValue("LOTTABLE05");
	            Object lottable11 = detailFocus.getValue("LOTTABLE11");
	            Object lottable12 = detailFocus.getValue("LOTTABLE12");

				int toExpireDays = 0;
				int toDeliverByDays = 0;
				int toBestByDays = 0;
				
				String sQueryString = "sku.STORERKEY = '" + sku.getString("STORERKEY") + "' AND  sku.SKU = '"+sku.getString("SKU")+"' ";
				BioCollectionBean listCollection = null;
				Query bioQuery = new Query("sku",sQueryString,null);
				UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();
				listCollection = uowb.getBioCollectionBean(bioQuery);
				if (listCollection.size()!= 0){
					toExpireDays = Integer.parseInt(listCollection.get("0").get("TOEXPIREDAYS").toString());
					toDeliverByDays = Integer.parseInt(listCollection.get("0").get("TODELIVERBYDAYS").toString());
					toBestByDays = Integer.parseInt(listCollection.get("0").get("TOBESTBYDAYS").toString());
				}
				

				// first check to see if we have an mfg date.  if so,
				// adjust the other dates based on the mfg dates and the
				// appropriate intervals, unless a date is already provided.
				//
				if (lottable04 != null)
				{

					lottable05 = adjustLottableDays(lottable04, lottable05, toExpireDays, true );
					lottable11 = adjustLottableDays(lottable04, lottable11, toDeliverByDays, true );
					lottable12 = adjustLottableDays(lottable04, lottable12, toBestByDays, true );
				
				}

				// next check to see if we have a expiration date.  if so,
				// first set the mfg date, then adjust the other dates based 
				// on the mfg date and the appropriate intervals, unless a 
				// date is already provided.
				//
				if (lottable05 != null )
				{
					lottable04 = adjustLottableDays(lottable05, lottable04, toExpireDays, false );
					lottable11 = adjustLottableDays(lottable04, lottable11, toDeliverByDays, true );
					lottable12 = adjustLottableDays(lottable04, lottable12, toBestByDays, true );
				}

				// next check to see if we have an best by date...
				//
				if (lottable12 != null )
				{
					lottable04 = adjustLottableDays(lottable12, lottable04, toBestByDays, false );
					lottable05 = adjustLottableDays(lottable04, lottable05, toExpireDays, true );
					lottable11 = adjustLottableDays(lottable04, lottable11, toDeliverByDays, true );
				}

				// finally check to see if we have a delivery by date...
				//
				if (lottable11 != null )
				{
					lottable04 = adjustLottableDays(lottable11, lottable04, toDeliverByDays, false );
					lottable05 = adjustLottableDays(lottable04, lottable05, toExpireDays, true );
					lottable12 = adjustLottableDays(lottable04, lottable12, toBestByDays, true );
				}

				detailFocus.setValue("LOTTABLE04", lottable04);
				detailFocus.setValue("LOTTABLE05", lottable05);
				detailFocus.setValue("LOTTABLE11", lottable11);
				detailFocus.setValue("LOTTABLE12", lottable12);
				
			}
		}		
	}


	public Object adjustLottableDays( Object fromDate, Object toDate, int toDays, boolean addDays)
	{

		if ( toDate == null && toDays > 0 )
		{
			GregorianCalendar gcFromDate, gcToDate;
			gcFromDate = (GregorianCalendar)fromDate;
			gcToDate = (GregorianCalendar)toDate;
			gcToDate = (GregorianCalendar)gcFromDate.clone();
			
			if ( addDays )
				gcToDate.add(GregorianCalendar.DATE, toDays);
			else
				gcToDate.add(GregorianCalendar.DATE, toDays * -1 );
			
			toDate = gcToDate;
		}

		return toDate;
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
   protected int execute( ActionContext context, ActionResult result ) throws UserException 
   {
       try 
       {
			ActionObjectInterface obj = context.getActionObject();
			String widget = obj.getName();
			StateInterface state = context.getState();					
			RuntimeFormInterface detailForm = state.getCurrentRuntimeForm();
			RuntimeFormWidgetInterface currentWidget = detailForm.getFormWidgetByName(widget);
			if ( currentWidget.getDisplayValue() != null && !currentWidget.getDisplayValue().equals("") )
			{
				BioBean sku = getSkuBIO( context );
				if (sku == null)
				{
					return RET_CONTINUE;
				}
				
				BioBean val = getLottable(context, sku.getString("LOTTABLEVALIDATIONKEY"));	
				if ( val == null )
				{
					return RET_CONTINUE;
				}
			
				checkCodeConvert(context,widget,val,sku);
			}
       } 
       catch (EpiException e)
       {
    	   e.printStackTrace();
    	   throw new UserException("WMEXP_INVALIDDATESTRING", new Object[0]);
       }
       catch(Exception e) 
       {            
          e.printStackTrace();
          throw new UserException("WMEXP_INVALIDDATESTRING", new Object[0]);    
       } 
       
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
           // Add your code here to process the event
           
       } 
       catch(Exception e) 
       {            
          // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }
}
