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


package com.ssaglobal.scm.wms.wm_adjustment.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.math.BigDecimal;

import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject.GetStringOutputParam;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.dao.SerialInventoryDAO;
import com.ssaglobal.scm.wms.util.dto.SerialInventoryDTO;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class AdjustmentDetailSerialListRender extends com.epiphany.shr.ui.action.ActionExtensionBase {


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
	protected static ILoggerCategory _log = LoggerFactory.getInstance(AdjustmentDetailSerialListRender.class);
   protected int execute( ActionContext context, ActionResult result ) throws EpiException {
	   
	   _log.debug("LOG_SYSTEM_OUT","[AdjustmentDetailSerialListRender]:START",100L);
	   
	   StateInterface state = context.getState();
	   UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
	   RuntimeFormExtendedInterface form = (RuntimeFormExtendedInterface)state.getCurrentRuntimeForm().getParentForm(state).getParentForm(state);
	   SlotInterface slotSerialInventory = (SlotInterface)  form.getSubSlot(AdjustmentHelper.SLOT_SERIALINVENTORYLIST);
	   
	   String adjustmentLineNumber = (String)form.getFormWidgetByName("ADJUSTMENTLINENUMBER").getValue();
	   
	   //RuntimeFormInterface parent =form.getParentForm(state);
	   RuntimeFormInterface parent = FormUtil.findForm((RuntimeFormInterface)form, "wms_list_shell", "wms_list_shell", state);
	   SlotInterface headerSlot = parent.getSubSlot(AdjustmentHelper.SLOT_LISTSLOT1);
	   RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot,"wm_adjustment_detail_view");
	   String adjustmentKey = headerForm.getFormWidgetByName("ADJUSTMENTKEY").getValue().toString();
	   
	   //jp 8624 Begin
	   //new AdjustmentHelper().refreshAdjustmentDetailSerialList(uowb, form, state, slotSerialInventory, adjustmentKey, adjustmentLineNumber);
	   EXEDataObject adjustmentDetailSerialList = getAdjustmentDetailSerialList(adjustmentKey, adjustmentLineNumber);
	   
	  
	   /*
	   for(int idx =0; idx < adjustmentDetailSerialList.getRowCount(); idx++){
	   
		   String serialNumber=null;
		   String qty = null;
		   
		   GetStringOutputParam param1 = adjustmentDetailSerialList.getString(new TextData("SERIALNUMBER"), serialNumber);
		   serialNumber = param1.pResult;
		   
		   GetStringOutputParam param2 = adjustmentDetailSerialList.getString(new TextData("QTY"), qty);
		   qty = param2.pResult;
		   
		   adjustmentDetailSerialList.getNextRow();
			
			
			_log.debug("LOG_SYSTEM_OUT","[AdjustmentDetailSerialListRender]jpdebug DBDATA: SN:"+serialNumber +" qty:"+qty,100L);
			
	   }
		*/	
			
			
	   RuntimeFormInterface adjustmentDetailSerialListForm = state.getRuntimeForm(slotSerialInventory, AdjustmentHelper.FORM_WM_ADJUSTMENTDETAILSERIALTMP_LIST_VIEW);
	   _log.debug("LOG_SYSTEM_OUT","[AdjustmentDetailSerialListRender]:calling compareWidgetAndPersistentData ",100L);
	   
	   compareWidgetAndPersistentData(adjustmentDetailSerialList, (BioCollectionBean)adjustmentDetailSerialListForm.getFocus());
	   //jp 8624 end
	   new AdjustmentHelper().refreshQtySelected((UIRenderContext)context);
	   result.setFocus(refreshAdjustmentDetailSerialList(uowb, form, state, slotSerialInventory, adjustmentKey, adjustmentLineNumber));
	   _log.debug("LOG_SYSTEM_OUT","[AdjustmentDetailSerialListRender]:END",100L);
	   return RET_CONTINUE;   
	   }
   
	public BioCollectionBean refreshAdjustmentDetailSerialList(UnitOfWorkBean uowb, RuntimeFormExtendedInterface form, 
			StateInterface state, SlotInterface slotSerialInventory, String adjustmentKey, String adjustmentLineNumber){
		
		String whereClause = "wm_adjustmentdetailserialtmp.ADJUSTMENTKEY = '"+adjustmentKey+"' " +
							 " AND wm_adjustmentdetailserialtmp.ADJUSTMENTLINENUMBER='"+adjustmentLineNumber+"' ";
		
		Query queryAdj = new Query(AdjustmentHelper.BIOCLASS_ADJUTMENTDETAILSERIALTMP, whereClause, null);
		
		BioCollectionBean adjDetailSerialTmpList = uowb.getBioCollectionBean(queryAdj);
		return adjDetailSerialTmpList;
	}

	private EXEDataObject getAdjustmentDetailSerialList(String adjustmentKey, String adjustmentLineNumber){
		String stmt = "SELECT ADJUSTMENTKEY, ADJUSTMENTLINENUMBER, STORERKEY, SKU, LOT, SERIALNUMBER, QTY " +
						" FROM adjustmentdetailserialtmp " +
						" WHERE adjustmentKey = '"+adjustmentKey+"'"+
						" AND adjustmentLineNumber = '" +adjustmentLineNumber +"'";
		
		try {
			EXEDataObject adjustmentDetailSerialList = WmsWebuiValidationSelectImpl.select(stmt);
			return adjustmentDetailSerialList;
		} catch (DPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
    
    private void compareWidgetAndPersistentData(EXEDataObject adjustmentDetailSerialList, BioCollectionBean dirtyList) throws UserException{
    	_log.debug("LOG_SYSTEM_OUT","[AdjustmentDetailSerialListRender]compareWidgetAndPersistentData START",100L);
    	
    	
    	try {
			for( int idx = 0; idx < dirtyList.size(); idx++){
				BioBean bio = (BioBean)dirtyList.elementAt(idx);
				BigDecimal qty = (BigDecimal)bio.get("QTY");
				String serialNumber = bio.getString("SERIALNUMBER");
				_log.debug("LOG_SYSTEM_OUT","[AdjustmentDetailSerialListRender]jpdebug: SN:"+serialNumber +" qty:"+qty,100L);
				
				/**
				//Skip records added in adjustmentDetailSerialTmp not in SerialInventory
				SerialInventoryDAO serialInventoryDAO = new SerialInventoryDAO();
				AdjustmentDetailSerialTmpDTO adjustmentDetailSerialTmpDTO = new AdjustmentDetailSerialTmpDTO();
				adjustmentDetailSerialTmpDTO.setSerialnumber(serialNumber);
				adjustmentDetailSerialTmpDTO.setStorerkey(bio.getString("STORERKEY"));
				adjustmentDetailSerialTmpDTO.setSku(bio.getString("SKU"));
				SerialInventoryDTO serialInventoryDTO =serialInventoryDAO.findSerialInventory(adjustmentDetailSerialTmpDTO);
				
				if (serialInventoryDTO!=null)
					continue;
				**/
				
				//Compare current dirty record with corresponding record in adjustmentDetailSerialTmp
				adjustmentDetailSerialList.setRow(1);
				
				for(int dbIdx =1; dbIdx <= adjustmentDetailSerialList.getRowCount(); dbIdx++){
					String dbSerialNumber=null;
					String dbQty = null;
					String dbAdjustmentKey = null;
					String dbAdjustmentLineNumber = null;
					
					GetStringOutputParam param1 = adjustmentDetailSerialList.getString(new TextData("SERIALNUMBER"), dbSerialNumber);
					dbSerialNumber = param1.pResult;
					
					GetStringOutputParam param2 = adjustmentDetailSerialList.getString(new TextData("QTY"), dbQty);
					dbQty = param2.pResult;

					GetStringOutputParam param3 = adjustmentDetailSerialList.getString(new TextData("ADJUSTMENTKEY"), dbAdjustmentKey);
					dbAdjustmentKey = param3.pResult;

					GetStringOutputParam param4 = adjustmentDetailSerialList.getString(new TextData("ADJUSTMENTLINENUMBER"), dbAdjustmentLineNumber);
					dbAdjustmentLineNumber = param4.pResult;

					
					   
					   
					if (serialNumber.equalsIgnoreCase(dbSerialNumber)){
						_log.debug("LOG_SYSTEM_OUT","[AdjustmentDetailSerialListRender]SN:"+serialNumber +" Qty:"+qty+" DBQty:"+dbQty,100L);
						if(qty.doubleValue()==Double.valueOf(dbQty)){
							_log.debug("LOG_SYSTEM_OUT","[AdjustmentDetailSerialListRender]SN:"+serialNumber +" quantities are the same",100L);
						}else{
							_log.debug("LOG_SYSTEM_OUT","[AdjustmentDetailSerialListRender]SN:"+serialNumber +" quantities are different: Mem Qty:"+qty+" DB Qty:"+dbQty,100L);
							
							if (validQtyChange(qty.doubleValue(), Double.valueOf(dbQty) )){
								AdjustmentDetailSerialTmpDTO adjDetailSerial = new AdjustmentDetailSerialTmpDTO();
								adjDetailSerial.setSerialnumber(dbSerialNumber);
								adjDetailSerial.setAdjustmentkey(dbAdjustmentKey);
								adjDetailSerial.setAdjustmentlinenumber(dbAdjustmentLineNumber);
								adjDetailSerial.setQty(String.valueOf(qty));
								
								
								AdjustmentDetailSerialTmpDAO.updateAdjustmentDetailSerialTmp(adjDetailSerial);
							}else{
								
								bio.set("QTY", BigDecimal.valueOf(Double.parseDouble(dbQty)));
								
								String[] params = new String[3];
								params[0] = dbSerialNumber;
								params[1] = dbQty;
								params[2] = qty.toString();
								
								throw new UserException("WMEXP_INVALID_SERIAL_QTY", params);
							}
						}
						break;
					}
					
					adjustmentDetailSerialList.getNextRow();

				}//end for
			}
		} catch (EpiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private boolean validQtyChange(double qty, double dbQty){
    
    	//Removed record back to 0
    	if (dbQty<0 && qty==0)
    		return true;
    	
    	/**
    	//Removing record
    	if (dbQty==0 && qty<0)
    		return true;
    	**/
    	return false;
    }
}
