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

package com.ssaglobal.scm.wms.wm_view_inventory_hold.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes

import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.util.StringUtils;

/**
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected
 * to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ViewInventoryHoldSearchPreRender extends com.epiphany.shr.ui.view.customization.FormExtensionBase {

	private static final String RANGE_END = "ZZZZZZZZZZ";

	private static final String RANGE_START = "0";

	private static final String RANGE_START_1 = "1";

	private static final String RANGE_START_EMPTY = "";

	private static final String RANGE_END_9 = "9999999";

	/**
	 * Called in response to the pre-render event on a form. Write code to customize the properties of a form. All code
	 * that initializes the properties of a form that is being displayed to a user for the first time belong here. This
	 * is not executed even if the form is re-displayed to the end user on subsequent actions.
	 * 
	 * @param context
	 *            exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	@Override
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {

		try {
			// Add your code here to process the event

			if (StringUtils.isEmpty(form.getFormWidgetByName("STORERSTART").getDisplayValue())) {
				form.getFormWidgetByName("STORERSTART").setValue(RANGE_START);
			}
			else {
				form.getFormWidgetByName("STORERSTART").setValue(form.getFormWidgetByName("STORERSTART").getDisplayValue().toUpperCase());
			}

			if (StringUtils.isEmpty(form.getFormWidgetByName("STOREREND").getDisplayValue())) {
				form.getFormWidgetByName("STOREREND").setValue(RANGE_END);
			}
			else {
				form.getFormWidgetByName("STOREREND").setValue(form.getFormWidgetByName("STOREREND").getDisplayValue().toUpperCase());
			}

			if (StringUtils.isEmpty(form.getFormWidgetByName("SKUSTART").getDisplayValue())) {
				form.getFormWidgetByName("SKUSTART").setValue(RANGE_START);
			}
			else {
				form.getFormWidgetByName("SKUSTART").setValue(form.getFormWidgetByName("SKUSTART").getDisplayValue().toUpperCase());
			}

			if (StringUtils.isEmpty(form.getFormWidgetByName("SKUEND").getDisplayValue())) {
				form.getFormWidgetByName("SKUEND").setValue(RANGE_END);
			}
			else {
				form.getFormWidgetByName("SKUEND").setValue(form.getFormWidgetByName("SKUEND").getDisplayValue().toUpperCase());
			}

			if (StringUtils.isEmpty(form.getFormWidgetByName("LOTSTART").getDisplayValue())) {
				form.getFormWidgetByName("LOTSTART").setValue(RANGE_START);
			}
			else {
				form.getFormWidgetByName("LOTSTART").setValue(form.getFormWidgetByName("LOTSTART").getDisplayValue().toUpperCase());
			}

			if (StringUtils.isEmpty(form.getFormWidgetByName("LOTEND").getDisplayValue())) {
				form.getFormWidgetByName("LOTEND").setValue(RANGE_END);
			}
			else {
				form.getFormWidgetByName("LOTEND").setValue(form.getFormWidgetByName("LOTEND").getDisplayValue().toUpperCase());
			}

			if (StringUtils.isEmpty(form.getFormWidgetByName("LOCSTART").getDisplayValue())) {
				form.getFormWidgetByName("LOCSTART").setValue(RANGE_START);
			}
			else {
				form.getFormWidgetByName("LOCSTART").setValue(form.getFormWidgetByName("LOCSTART").getDisplayValue().toUpperCase());
			}

			if (StringUtils.isEmpty(form.getFormWidgetByName("LOCEND").getDisplayValue())) {
				form.getFormWidgetByName("LOCEND").setValue(RANGE_END);
			}
			else {
				form.getFormWidgetByName("LOCEND").setValue(form.getFormWidgetByName("LOCEND").getDisplayValue().toUpperCase());
			}

			if (StringUtils.isEmpty(form.getFormWidgetByName("IDSTART").getDisplayValue())) {
				form.getFormWidgetByName("IDSTART").setValue(RANGE_START_EMPTY);
			}
			else {
				form.getFormWidgetByName("IDSTART").setValue(form.getFormWidgetByName("IDSTART").getDisplayValue().toUpperCase());
			}

			if (StringUtils.isEmpty(form.getFormWidgetByName("IDEND").getDisplayValue())) {
				form.getFormWidgetByName("IDEND").setValue(RANGE_END);
			}
			else {
				form.getFormWidgetByName("IDEND").setValue(form.getFormWidgetByName("IDEND").getDisplayValue().toUpperCase());
			}

			if (StringUtils.isEmpty(form.getFormWidgetByName("QTYSTART").getDisplayValue())) {
				form.getFormWidgetByName("QTYSTART").setValue(RANGE_START_1);
			}
			else {
				form.getFormWidgetByName("QTYSTART").setValue(form.getFormWidgetByName("QTYSTART").getDisplayValue().toUpperCase());
			}

			if (StringUtils.isEmpty(form.getFormWidgetByName("QTYEND").getDisplayValue())) {
				form.getFormWidgetByName("QTYEND").setValue(RANGE_END_9);
			}
			else {
				form.getFormWidgetByName("QTYEND").setValue(form.getFormWidgetByName("QTYEND").getDisplayValue().toUpperCase());
			}

			if (StringUtils.isEmpty(form.getFormWidgetByName("HOLDREASONSTART").getDisplayValue())) {
				form.getFormWidgetByName("HOLDREASONSTART").setValue(RANGE_START_EMPTY);
			}
			else {
				form.getFormWidgetByName("HOLDREASONSTART").setValue(form.getFormWidgetByName("HOLDREASONSTART").getDisplayValue().toUpperCase());
			}

			if (StringUtils.isEmpty(form.getFormWidgetByName("HOLDREASONEND").getDisplayValue())) {
				form.getFormWidgetByName("HOLDREASONEND").setValue(RANGE_END);
			}
			else {
				form.getFormWidgetByName("HOLDREASONEND").setValue(form.getFormWidgetByName("HOLDREASONEND").getDisplayValue().toUpperCase());
			}

			if (StringUtils.isEmpty(form.getFormWidgetByName("LOTTABLE01START").getDisplayValue())) {
				form.getFormWidgetByName("LOTTABLE01START").setValue(RANGE_START_EMPTY);
			}
			else {
				form.getFormWidgetByName("LOTTABLE01START").setValue(form.getFormWidgetByName("LOTTABLE01START").getDisplayValue().toUpperCase());
			}

			if (StringUtils.isEmpty(form.getFormWidgetByName("LOTTABLE01END").getDisplayValue())) {
				form.getFormWidgetByName("LOTTABLE01END").setValue(RANGE_END);
			}
			else {
				form.getFormWidgetByName("LOTTABLE01END").setValue(form.getFormWidgetByName("LOTTABLE01END").getDisplayValue().toUpperCase());
			}

			if (StringUtils.isEmpty(form.getFormWidgetByName("LOTTABLE02START").getDisplayValue())) {
				form.getFormWidgetByName("LOTTABLE02START").setValue(RANGE_START_EMPTY);
			}
			else {
				form.getFormWidgetByName("LOTTABLE02START").setValue(form.getFormWidgetByName("LOTTABLE02START").getDisplayValue().toUpperCase());
			}

			if (StringUtils.isEmpty(form.getFormWidgetByName("LOTTABLE02END").getDisplayValue())) {
				form.getFormWidgetByName("LOTTABLE02END").setValue(RANGE_END);
			}
			else {
				form.getFormWidgetByName("LOTTABLE02END").setValue(form.getFormWidgetByName("LOTTABLE02END").getDisplayValue().toUpperCase());
			}

			if (StringUtils.isEmpty(form.getFormWidgetByName("LOTTABLE03START").getDisplayValue())) {
				form.getFormWidgetByName("LOTTABLE03START").setValue(RANGE_START_EMPTY);
			}
			else {
				form.getFormWidgetByName("LOTTABLE03START").setValue(form.getFormWidgetByName("LOTTABLE03START").getDisplayValue().toUpperCase());
			}

			if (StringUtils.isEmpty(form.getFormWidgetByName("LOTTABLE03END").getDisplayValue())) {
				form.getFormWidgetByName("LOTTABLE03END").setValue(RANGE_END);
			}
			else {
				form.getFormWidgetByName("LOTTABLE03END").setValue(form.getFormWidgetByName("LOTTABLE03END").getDisplayValue().toUpperCase());
			}

			// if (StringUtils.isEmpty(form.getFormWidgetByName("LOTTABLE04START").getDisplayValue())) {
			// form.getFormWidgetByName("LOTTABLE04START").setValue(RANGE_START_EMPTY);
			// }
			//
			// if (StringUtils.isEmpty(form.getFormWidgetByName("LOTTABLE04END").getDisplayValue())) {
			// form.getFormWidgetByName("LOTTABLE04END").setValue(RANGE_END);
			// }
			//
			// if (StringUtils.isEmpty(form.getFormWidgetByName("LOTTABLE05START").getDisplayValue())) {
			// form.getFormWidgetByName("LOTTABLE05START").setValue(RANGE_START_EMPTY);
			// }
			//
			// if (StringUtils.isEmpty(form.getFormWidgetByName("LOTTABLE05END").getDisplayValue())) {
			// form.getFormWidgetByName("LOTTABLE05END").setValue(RANGE_END);
			// }

			if (StringUtils.isEmpty(form.getFormWidgetByName("LOTTABLE06START").getDisplayValue())) {
				form.getFormWidgetByName("LOTTABLE06START").setValue(RANGE_START_EMPTY);
			}
			else {
				form.getFormWidgetByName("LOTTABLE06START").setValue(form.getFormWidgetByName("LOTTABLE06START").getDisplayValue().toUpperCase());
			}

			if (StringUtils.isEmpty(form.getFormWidgetByName("LOTTABLE06END").getDisplayValue())) {
				form.getFormWidgetByName("LOTTABLE06END").setValue(RANGE_END);
			}
			else {
				form.getFormWidgetByName("LOTTABLE06END").setValue(form.getFormWidgetByName("LOTTABLE06END").getDisplayValue().toUpperCase());
			}

			if (StringUtils.isEmpty(form.getFormWidgetByName("LOTTABLE07START").getDisplayValue())) {
				form.getFormWidgetByName("LOTTABLE07START").setValue(RANGE_START_EMPTY);
			}
			else {
				form.getFormWidgetByName("LOTTABLE07START").setValue(form.getFormWidgetByName("LOTTABLE07START").getDisplayValue().toUpperCase());
			}

			if (StringUtils.isEmpty(form.getFormWidgetByName("LOTTABLE07END").getDisplayValue())) {
				form.getFormWidgetByName("LOTTABLE07END").setValue(RANGE_END);
			}
			else {
				form.getFormWidgetByName("LOTTABLE07END").setValue(form.getFormWidgetByName("LOTTABLE07END").getDisplayValue().toUpperCase());
			}

			if (StringUtils.isEmpty(form.getFormWidgetByName("LOTTABLE08START").getDisplayValue())) {
				form.getFormWidgetByName("LOTTABLE08START").setValue(RANGE_START_EMPTY);
			}
			else {
				form.getFormWidgetByName("LOTTABLE08START").setValue(form.getFormWidgetByName("LOTTABLE08START").getDisplayValue().toUpperCase());
			}

			if (StringUtils.isEmpty(form.getFormWidgetByName("LOTTABLE08END").getDisplayValue())) {
				form.getFormWidgetByName("LOTTABLE08END").setValue(RANGE_END);
			}
			else {
				form.getFormWidgetByName("LOTTABLE08END").setValue(form.getFormWidgetByName("LOTTABLE08END").getDisplayValue().toUpperCase());
			}

			if (StringUtils.isEmpty(form.getFormWidgetByName("LOTTABLE09START").getDisplayValue())) {
				form.getFormWidgetByName("LOTTABLE09START").setValue(RANGE_START_EMPTY);
			}
			else {
				form.getFormWidgetByName("LOTTABLE09START").setValue(form.getFormWidgetByName("LOTTABLE09START").getDisplayValue().toUpperCase());
			}

			if (StringUtils.isEmpty(form.getFormWidgetByName("LOTTABLE09END").getDisplayValue())) {
				form.getFormWidgetByName("LOTTABLE09END").setValue(RANGE_END);
			}
			else {
				form.getFormWidgetByName("LOTTABLE09END").setValue(form.getFormWidgetByName("LOTTABLE09END").getDisplayValue().toUpperCase());
			}

			if (StringUtils.isEmpty(form.getFormWidgetByName("LOTTABLE10START").getDisplayValue())) {
				form.getFormWidgetByName("LOTTABLE10START").setValue(RANGE_START_EMPTY);
			}
			else {
				form.getFormWidgetByName("LOTTABLE10START").setValue(form.getFormWidgetByName("LOTTABLE10START").getDisplayValue().toUpperCase());
			}

			if (StringUtils.isEmpty(form.getFormWidgetByName("LOTTABLE10END").getDisplayValue())) {
				form.getFormWidgetByName("LOTTABLE10END").setValue(RANGE_END);
			}
			else {
				form.getFormWidgetByName("LOTTABLE10END").setValue(form.getFormWidgetByName("LOTTABLE10END").getDisplayValue().toUpperCase());
			}

			// if (StringUtils.isEmpty(form.getFormWidgetByName("LOTTABLE11START").getDisplayValue())) {
			// form.getFormWidgetByName("LOTTABLE11START").setValue(RANGE_START_EMPTY);
			// }
			//
			// if (StringUtils.isEmpty(form.getFormWidgetByName("LOTTABLE11END").getDisplayValue())) {
			// form.getFormWidgetByName("LOTTABLE11END").setValue(RANGE_END);
			// }
			//
			// if (StringUtils.isEmpty(form.getFormWidgetByName("LOTTABLE12START").getDisplayValue())) {
			// form.getFormWidgetByName("LOTTABLE12START").setValue(RANGE_START_EMPTY);
			// }
			//
			// if (StringUtils.isEmpty(form.getFormWidgetByName("LOTTABLE12END").getDisplayValue())) {
			// form.getFormWidgetByName("LOTTABLE12END").setValue(RANGE_END);
			// }

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

}
