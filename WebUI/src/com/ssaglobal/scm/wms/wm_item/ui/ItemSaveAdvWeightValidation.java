/****************************************************************************
 *               Copyright (c) 1999-2003 E.piphany, Inc.                    *
 *                          ALL RIGHTS RESERVED                             *
 *                                                                          *
 *     THIS PROGRAM CONTAINS PROPRIETARY INFORMATION OF E.PIPHANY, INC.     *
 *     ----------------------------------------------------------------     *
 *                                                                          *
 * THIS PROGRAM CONTAINS THE CONFIDENTIAL AND/OR PROPRIETARY INFORMATION    *
 * OF E.PIPHANY, INC.  ANY DUPLICATION, MODIFICATION, DISTRIBUTION, PUBLIC  *
 * PERFORMANCE, OR PUBLIC DISPLAY OF THIS PROGRAM, OR ANY PORTION OR        *
 * DERIVATION THEREOF WITHOUT THE EXPRESS WRITTEN CONSENT OF                *
 * E.PIPHANY, INC. IS STRICTLY PROHIBITED.  USE OR DISCLOSURE OF THIS       *
 * PROGRAM DOES NOT CONVEY ANY RIGHTS TO REPRODUCE, DISCLOSE OR DISTRIBUTE  *
 * ITS CONTENTS, OR TO MANUFACTURE, USE, OR SELL ANYTHING THAT CONTAINS IN  *
 * WHOLE OR IN PART ANY ASPECT OF THIS PROGRAM.                             *
 *                                                                          *
 ****************************************************************************
 */


package com.ssaglobal.scm.wms.wm_item.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.infor.scm.waveplanning.common.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.wm_table_validation.ccf.NumericValidationCCF;


/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class ItemSaveAdvWeightValidation extends com.epiphany.shr.ui.action.ActionExtensionBase {

//Added for WM 9 3PL Enhancements - Catch weight tracking 
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
	private static String  ADV_CATCH_WEIGHT_TAB_ID = "tab 4";
	private static final String IS_CHECKED = "1";

   protected int execute( ActionContext context, ActionResult result ) throws EpiException {

      // Replace the following line with your code,
      // returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
      // as appropriate
		StateInterface state = context.getState();
		RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();
		

		RuntimeFormInterface shellForm = shellToolbar.getParentForm(state);
		SlotInterface listSlot = shellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface listForm = state.getRuntimeForm(listSlot, null);

		ArrayList tabIdentifiers = new ArrayList();
		tabIdentifiers.add(ADV_CATCH_WEIGHT_TAB_ID);
		DataBean focus=null;
		///finding the adv weight tracking fields
		RuntimeFormInterface generalForm = FormUtil.findForm(shellToolbar, "wms_list_shell", "wm_item_advweight_tracking_detail_view", tabIdentifiers, state);
		if(!isNull(generalForm)){

			focus = generalForm.getFocus();
			if (focus instanceof BioBean) {
				focus = (BioBean) focus;
			} else if (focus instanceof QBEBioBean) {
				focus = (QBEBioBean) focus;
			}
			String IBSUMCWFLGStr = BioAttributeUtil.getString(focus, "IBSUMCWFLG");
			String OBSUMCWFLGStr = BioAttributeUtil.getString(focus, "OBSUMCWFLG");
			String SHOWRFCWONTRANSStr = BioAttributeUtil.getString(focus, "SHOWRFCWONTRANS");
			
			String isICWFLG =  BioAttributeUtil.getString(focus, "ICWFLAG");
			String isOCWFLG =  BioAttributeUtil.getString(focus, "OCWFLAG");
			
			String isSNUM_ENDTOEND = this.isEmpty(focus.getValue("SNUM_ENDTOEND"))?"0":focus.getValue("SNUM_ENDTOEND").toString();
			
			//check SMUN_ENDTOEND and ICWFLAG, if both of them are checked, then IBSUMCWFLGStr,OBSUMCWFLGStr and SHOWRFCWONTRANSStr should be 0
			if((IS_CHECKED.equalsIgnoreCase(isSNUM_ENDTOEND))
					&& IS_CHECKED.equalsIgnoreCase(isICWFLG)){
				if(IS_CHECKED.equalsIgnoreCase(IBSUMCWFLGStr)
						|| IS_CHECKED.equalsIgnoreCase(OBSUMCWFLGStr)
						|| IS_CHECKED.equalsIgnoreCase(SHOWRFCWONTRANSStr)){
					throw new UserException("WMEXP_ITEM_ADVCWCHECK_SNUM_ICWFLG", new Object[]{null});
				}
			}
			
			//if ICWFLAG checked,  IBSUMCWFLG should be 0. if OCWGLAG checked, OBSUMCWFLG should be 0
			if(IS_CHECKED.equalsIgnoreCase(isICWFLG)){
				if(IS_CHECKED.equalsIgnoreCase(IBSUMCWFLGStr)){
					throw new UserException("WMEXP_ITEM_ADVCWCHECK_ICWFLG_IBSUMCWFL", new Object[]{null});
				}
			}
			if(IS_CHECKED.equalsIgnoreCase(isOCWFLG)){
				if(IS_CHECKED.equalsIgnoreCase(OBSUMCWFLGStr)){
					throw new UserException("WMEXP_ITEM_ADVCWCHECK_OCWFLG_OBSUMCWFL", new Object[]{null});
				}
			}
			
			
			
			if(IS_CHECKED.equalsIgnoreCase(IBSUMCWFLGStr)
					|| IS_CHECKED.equalsIgnoreCase(OBSUMCWFLGStr)
					|| IS_CHECKED.equalsIgnoreCase(SHOWRFCWONTRANSStr)){
				
				String catchgrosswgt = this.isEmpty(focus.getValue("catchgrosswgt"))?"0":focus.getValue("catchgrosswgt").toString();
				String catchnetwgt = this.isEmpty(focus.getValue("catchnetwgt"))?"0":focus.getValue("catchnetwgt").toString();
				String catchtarewgt = this.isEmpty(focus.getValue("catchtarewgt"))?"0":focus.getValue("catchtarewgt").toString();		
				if(!IS_CHECKED.equalsIgnoreCase(catchgrosswgt)
						&& !IS_CHECKED.equalsIgnoreCase(catchnetwgt)
						&& !IS_CHECKED.equalsIgnoreCase(catchtarewgt)){
					throw new UserException("WMEXP_ITEM_ADVCWCHECK_GROSS_NET_TARE", new Object[]{null});
			
				}else{				
					//check for equilibrium
					double gross=0;
					double net=0;
					double tare=0;
					double sum=0;
					String attributeValue =null; 
					

					
					//retrieve std gross wgt
					if(focus.getValue("stdgrosswgt1")!=null)
					{
						attributeValue =focus.getValue("stdgrosswgt1").toString();						
						gross= NumericValidationCCF.parseNumber(attributeValue);
					}	
					//retrieve std net weight
					if(focus.getValue("stdnetwgt1")!=null)
					{	
						attributeValue =focus.getValue("stdnetwgt1").toString();						
						net= NumericValidationCCF.parseNumber(attributeValue);
					}					
					//retrieve tare weight
					if(focus.getValue("tarewgt1")!=null)
					{
						attributeValue =focus.getValue("tarewgt1").toString();						
						tare= NumericValidationCCF.parseNumber(attributeValue);
					}	
					sum=net+tare;
					

					if(Math.abs(gross - sum)<0.000009) 
					{
						//do nothing
					}
					else
					{	
						//if gross weight sum is not equal to net and tare weight  throw an exception
						String[] parameters = new String[2];
						parameters[0] = "";
						throw new UserException("WMEXP_ITEM_WEIGHTSEQUALLCHECK", new Object[]{null});					
					}
						
					if(gross == 0 && net == 0 && tare == 0){
						throw new UserException("WMEXP_ITEM_CW_WEIGHT_CHECK", new Object[]{null});											
					}	
				}
			}
				
		}

	   
      return RET_CONTINUE;
   }
   
	boolean isNull(Object attributeValue){
		if(attributeValue == null){
			return true;
		}else{
			return false;
		}
	}
	public boolean isUnchecked(Object attributeValue) {
		if(!isEmpty(attributeValue)){
			if (attributeValue.toString().matches("[0Nn]")) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}
	protected boolean isEmpty(Object attributeValue) {
		if (attributeValue == null)	{
			return true;
		} else if (attributeValue.toString().matches("\\s*")) {
			return true;
		} else {
			return false;
		}
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
