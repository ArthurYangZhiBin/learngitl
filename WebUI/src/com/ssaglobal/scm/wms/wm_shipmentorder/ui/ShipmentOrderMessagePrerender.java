/**
 * ---Begin Copyright Notice---20090105T1353
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2010 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */

package com.ssaglobal.scm.wms.wm_shipmentorder.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.SessionUtil;

// TODO: Auto-generated Javadoc
/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * .
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ShipmentOrderMessagePrerender extends
		com.epiphany.shr.ui.view.customization.FormExtensionBase {

	public static final String SO_MESSAGE = "SO_MESSAGE";
	public static final String SO_ARGS = "SO_ARGS";
	protected static ILoggerCategory log = LoggerFactory
			.getInstance(ShipmentOrderMessagePrerender.class);

	/**
	 * Called in response to the pre-render event on a form in a modal window.
	 * Write code to customize the properties of a form. This code is
	 * re-executed everytime a form is redisplayed to the end user
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 * @return the int
	 * @throws EpiException
	 *             the epi exception
	 */
	protected int preRenderForm(ModalUIRenderContext context,
			RuntimeNormalFormInterface form) throws EpiException {

		try {
			StateInterface state = context.getState();
			RuntimeFormWidgetInterface messageWidget = form
					.getFormWidgetByName("MESSAGE");
			String message = (String) SessionUtil
					.getInteractionSessionAttribute(SO_MESSAGE, state);
			log.info("ShipmentOrderMessagePrerender_preRenderForm", "Message "
					+ message, SuggestedCategory.APP_EXTENSION);

			Object[] args = (Object[]) SessionUtil
					.getInteractionSessionAttribute(SO_ARGS, state);
			log.info("ShipmentOrderMessagePrerender_preRenderForm", "Args "
					+ args, SuggestedCategory.APP_EXTENSION);
			if (message == null || args == null) {
				// error

			}
			messageWidget.setLabel(RuntimeFormWidgetInterface.LABEL_VALUE,
					getTextMessage(message, args, state.getLocale()));
			messageWidget.setLabel(RuntimeFormWidgetInterface.LABEL_LABEL,
					getTextMessage(message, args, state.getLocale()));

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

}
