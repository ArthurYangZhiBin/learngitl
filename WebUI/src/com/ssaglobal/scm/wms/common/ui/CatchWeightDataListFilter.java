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

package com.ssaglobal.scm.wms.common.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

/**
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected
 * to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class CatchWeightDataListFilter extends com.epiphany.shr.ui.view.customization.FormExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(CatchWeightDataListFilter.class);

	/**
	 * Called in response to the pre-render event on a list form. Write code to customize the properties of a list form
	 * dynamically, change the bio collection being displayed in the form or filter the bio collection
	 * 
	 * @param context
	 *            exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	@Override
	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) throws EpiException {

		try {

			boolean inboundOutboundFlag = getParameterBoolean("INBOUNDOUTBOUND", true);
			_log.debug(	"LOG_DEBUG_EXTENSION_CatchWeightDataListFilter_preRenderListForm",
						"INBOUNDOUTBOUND Flag is " + inboundOutboundFlag,
						SuggestedCategory.NONE);
			if (inboundOutboundFlag == true) {
				_log.debug(	"LOG_DEBUG_EXTENSION_CatchWeightDataListFilter_preRenderListForm",
							"Going to filter IOFLAG = I",
							SuggestedCategory.NONE);
				// filter IOFLAG = I
				BioCollectionBean listFocus = (BioCollectionBean) form.getFocus();
				_log.debug(	"LOG_DEBUG_EXTENSION_CatchWeightDataListFilter_preRenderListForm",
							"Original size " + listFocus.size(),
							SuggestedCategory.NONE);
				Query filterQry = null;

				String bioType = listFocus.getDataType();
				filterQry = new Query(bioType, bioType + ".IOFLAG = 'I'", null);
				

				BioCollection newFocus = listFocus.filter(filterQry);
				
				_log.debug(	"LOG_DEBUG_EXTENSION_CatchWeightDataListFilter_preRenderListForm",
							"Filtered size " + newFocus.size(),
							SuggestedCategory.NONE);

				form.setFocus((DataBean) newFocus);
			} else {
				_log.debug(	"LOG_DEBUG_EXTENSION_CatchWeightDataListFilter_preRenderListForm",
							"Going to filter IOFLAG = O",
							SuggestedCategory.NONE);
				// filter IOFLAG = O
				BioCollectionBean listFocus = (BioCollectionBean) form.getFocus();
				_log.debug(	"LOG_DEBUG_EXTENSION_CatchWeightDataListFilter_preRenderListForm",
							"Original size " + listFocus.size(),
							SuggestedCategory.NONE);
				Query filterQry = null;

				String bioType = listFocus.getDataType();
				filterQry = new Query(bioType, bioType + ".IOFLAG = 'O'", null);

				BioCollection newFocus = listFocus.filter(filterQry);
				_log.debug(	"LOG_DEBUG_EXTENSION_CatchWeightDataListFilter_preRenderListForm",
							"Filtered size " + newFocus.size(),
							SuggestedCategory.NONE);

				form.setFocus((DataBean) newFocus);
			}

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * Called in response to the pre-render event on a list form in a modal dialog. Write code to customize the
	 * properties of a list form dynamically, change the bio collection being displayed in the form or filter the bio
	 * collection
	 * 
	 * @param context
	 *            exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state},
	 *            service information and modal dialog context
	 * @param form
	 *            the form that is about to be rendered
	 */
	@Override
	protected int preRenderListForm(ModalUIRenderContext context, RuntimeListFormInterface form) throws EpiException {

		try {
			// Add your code here to process the event

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * Called in response to the modifyListValues event on a list form. Subclasses must override this in order to
	 * customize the display values of a list form
	 * 
	 * @param context
	 *            exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	@Override
	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form) throws EpiException {

		try {
			// Add your code here to process the event

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * Called in response to the modifyListValues event on a list form in a modal dialog. Subclasses must override this
	 * in order to customize the display values of a list form
	 * 
	 * @param context
	 *            exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state},
	 *            service information and modal dialog context
	 * @param form
	 *            the form that is about to be rendered
	 */
	@Override
	protected int modifyListValues(ModalUIRenderContext context, RuntimeListFormInterface form) throws EpiException {

		try {
			// Add your code here to process the event

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}
}
