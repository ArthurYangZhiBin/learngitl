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

package com.ssaglobal.scm.wms.wm_task_details.ui;

// Import 3rd party packages and classes
import java.util.ArrayList;
import java.util.Iterator;

// Import Epiphany packages and classes
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

public class TaskDisableBasedOnCondition extends com.epiphany.shr.ui.view.customization.FormExtensionBase {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(TaskDisableBasedOnCondition.class);

	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {
		_log.debug("LOG_DEBUG_EXTENSION", "!@# Start of TaskDisableBasedOnCondition", SuggestedCategory.NONE);
		ArrayList attrToDisable = (ArrayList) getParameter("ATTRTODISABLE");
		String dependentAttr = getParameterString("DEPENDENTATTR");
		ArrayList conditions = (ArrayList) getParameter("CONDITIONS");
		boolean toggle = getParameterBoolean("READONLY");
		printParameters(attrToDisable, dependentAttr, conditions);

		try	{
			StateInterface state = context.getState();
			DataBean currentFormFocus = state.getFocus();
			if(currentFormFocus instanceof BioBean) {
				currentFormFocus = (BioBean) currentFormFocus;
			}

			String dependentAttrValue;
			try	{
				dependentAttrValue = currentFormFocus.getValue(dependentAttr).toString();
			} catch(NullPointerException e) {
				_log.debug("LOG_DEBUG_EXTENSION", "!@# Returning, new record", SuggestedCategory.NONE);
				return RET_CONTINUE;
			}

			for(Iterator itOuter = conditions.iterator(); itOuter.hasNext();) {
				String condition = itOuter.next().toString();
				if(dependentAttrValue.equalsIgnoreCase(condition)) {
					_log.debug("LOG_DEBUG_EXTENSION", "!@# Dependent Attribute matches condition, " + condition
							+ " disabling widgets", SuggestedCategory.NONE);
					for(Iterator itInner = attrToDisable.iterator(); itInner.hasNext();) {
						String widgetName = itInner.next().toString();
						_log.debug("LOG_DEBUG_EXTENSION", "!@@ " + widgetName, SuggestedCategory.NONE);
						RuntimeFormWidgetInterface widget = form.getFormWidgetByName(widgetName);
						widget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, toggle);
					}
				} else {
					_log.debug("LOG_DEBUG_EXTENSION", "!@# Dependet Attribute didn't match " + condition, SuggestedCategory.NONE);
				}
			}
		} catch(Exception e) {
			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}
		return RET_CONTINUE;
	}

	private void printParameters(ArrayList attrToDisable, String dependentAttr, ArrayList conditions) {
		_log.debug("LOG_DEBUG_EXTENSION", "!@# Dependent Attribute - " + dependentAttr, SuggestedCategory.NONE);
		_log.debug("LOG_DEBUG_EXTENSION", "!@# Conditions ", SuggestedCategory.NONE);
		for(Iterator it = conditions.iterator(); it.hasNext();) {
			_log.debug("LOG_DEBUG_EXTENSION", "! Condtion : " + it.next(), SuggestedCategory.NONE);
		}
		_log.debug("LOG_DEBUG_EXTENSION", "!@# Widgets to Disable", SuggestedCategory.NONE);
		for(Iterator it = attrToDisable.iterator(); it.hasNext();) {
			_log.debug("LOG_DEBUG_EXTENSION", "! Widget : " + it.next(), SuggestedCategory.NONE);
		}
	}

	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) throws EpiException {
		try	{
			// Add your code here to process the event
		} catch(Exception e) {
			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}
		return RET_CONTINUE;
	}
}