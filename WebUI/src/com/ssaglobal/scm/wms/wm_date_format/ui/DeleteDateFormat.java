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

// Import Epiphany packages and classes
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.*;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import java.util.*;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.util.exceptions.UserException;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class DeleteDateFormat extends ListSelector 
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(DeleteDateFormat.class);

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
	protected int execute( ActionContext context, ActionResult result ) throws EpiException 
	{
		StateInterface state = context.getState();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface parent = toolbar.getParentForm(state);
		SlotInterface listSlot = parent.getSubSlot("list_slot_1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(listSlot, null);
		ArrayList select = new ArrayList();
		if ( headerForm.isListForm() )
		{
			RuntimeListFormInterface headerList = (RuntimeListFormInterface)headerForm;
			select = headerList.getAllSelectedItems();
		}
		else
		{
			BioBean headerFocus = (BioBean)headerForm.getFocus();
			select.add(headerFocus);
		}
		
		if (select != null && select.size() > 0) 
		{
			Iterator bioBeanIter = select.iterator();
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
			try 
			{
				BioBean bio;
				for (; bioBeanIter.hasNext(); )
				{
					bio = (BioBean) bioBeanIter.next();
					if ( bio.getActualTypeName().equals("wm_dateformat") )
					{
						String dateCode = bio.getString("DATECODEFORMAT");
						_log.debug("LOG_SYSTEM_OUT","HEADER - "+dateCode,100L);
												
						// is DATECODEFORMAT used in LOTTABLEVALIDATIONDETAIL in the LOTTABLE**RECEIPTCONVERSION fields
						String lottableQuery = "wm_lottablevalidationdetail.LOTTABLE01DATEFORMAT = '"+dateCode+"' OR "+
											   "wm_lottablevalidationdetail.LOTTABLE02DATEFORMAT = '"+dateCode+"' OR "+
											   "wm_lottablevalidationdetail.LOTTABLE03DATEFORMAT = '"+dateCode+"' OR "+
											   "wm_lottablevalidationdetail.LOTTABLE06DATEFORMAT = '"+dateCode+"' OR "+
											   "wm_lottablevalidationdetail.LOTTABLE07DATEFORMAT = '"+dateCode+"' OR "+
											   "wm_lottablevalidationdetail.LOTTABLE08DATEFORMAT = '"+dateCode+"' OR "+
											   "wm_lottablevalidationdetail.LOTTABLE09DATEFORMAT = '"+dateCode+"' OR "+
											   "wm_lottablevalidationdetail.LOTTABLE10DATEFORMAT = '"+dateCode+"' ";
						Query lotBioQuery = new Query("wm_lottablevalidationdetail", lottableQuery, null);
						BioCollectionBean lottableList = uowb.getBioCollectionBean(lotBioQuery);
						if ( lottableList.size() > 0 )
						{
							throw new UserException("WMEXP_DF_CODEINUSE", new Object[0]);
						}
						
						// is CUSTOMCODEFORMAT used in any wm_dateformat records besides this current one.
						String custQuery = "wm_dateformat.CUSTOMCODEFORMAT = '"+bio.getString("CUSTOMCODEFORMAT")+"' AND wm_dateformat.DATECODEFORMAT != '"+dateCode+"'";
						Query custBioQuery = new Query( "wm_dateformat", custQuery, null );
						BioCollectionBean custList = uowb.getBioCollectionBean(custBioQuery);
						boolean deleteCust = custList.size() == 0;
						
						// no dependencies found.  DELETE all the children.
						//delete wm_dateformatdetail
						String detailDeleteQuery = "wm_dateformatdetail.DATECODEFORMAT = '"+dateCode+"' ";
						Query bioQuery = new Query("wm_dateformatdetail", detailDeleteQuery, null);
						BioCollectionBean detailList = uowb.getBioCollectionBean(bioQuery);
						
						for ( int idx = 0; idx < detailList.size(); ++idx )
						{
							BioBean row = (BioBean)detailList.elementAt(idx);
							row.delete();
						}
						
						//delete wm_dateformatcustom
						if ( deleteCust )
						{
							String customDeleteQuery = "wm_dateformatcustom.CUSTOMCODEFORMAT = '"+bio.getString("CUSTOMCODEFORMAT")+"'";
							Query bioDeleteCustQuery = new Query("wm_dateformatcustom", customDeleteQuery, null);
							BioCollectionBean custDetailList = uowb.getBioCollectionBean(bioDeleteCustQuery);
							
							for ( int idx = 0; idx < custDetailList.size(); ++idx)
							{
								BioBean row = (BioBean)custDetailList.elementAt(idx);
								row.delete();
							}
						}
					}
					
					bio.delete();
				}
				
				uowb.saveUOW();
				result.setSelectedItems(null);
			} 
			catch (UnitOfWorkException ex) 
			{
				ex.printStackTrace();
				_log.error("LOG_ERROR_EXTENSION_DeleteDateFormat_execute", "IN UnitOfWorkException", SuggestedCategory.NONE);
				_log.error("LOG_ERROR_EXTENSION_DeleteDateFormat_execute", ex.getStackTraceAsString(), SuggestedCategory.NONE);
		
				Throwable nested = ((UnitOfWorkException) ex).findDeepestNestedException();
		
				if (nested instanceof ServiceObjectException) 
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
				_log.debug("LOG_SYSTEM_OUT","\n\t" + "IN EPIEXCEPTION" + "\n",100L);
				_log.error("LOG_ERROR_EXTENSION_DeleteDateFormat_execute", "IN EPIEXCEPTION", SuggestedCategory.NONE);
				_log.error("LOG_ERROR_EXTENSION_DeleteDateFormat_execute", ex.getStackTraceAsString(), SuggestedCategory.NONE);
				throwUserException(ex, "ERROR_DELETING_BIO", null);
			}
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
    protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {

       try {
           // Add your code here to process the event
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }
}
