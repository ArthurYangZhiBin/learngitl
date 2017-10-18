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
package com.ssaglobal.scm.wms.wm_table_validation.ui;

// Import 3rd party packages and classes
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Import Epiphany packages and classes
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.exceptions.FormException;

public class NonNegativeValidationAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	private static final String ERROR_MESSAGE = "WMEXP_POSITIVE_VALIDATION";

	protected int execute(ActionContext context, ActionResult result)throws EpiException{
		//		Parameters
		RuntimeFormWidgetInterface sourceWidget = context.getSourceWidget();
		Object tempValue = sourceWidget.getValue();
		if (tempValue != null){
			double sourceWidgetValue;
			try{
				sourceWidgetValue = Double.parseDouble(tempValue.toString());
			}catch(NumberFormatException e){
				Pattern pattern = Pattern.compile("\\,");
				Matcher matcher = pattern.matcher(tempValue.toString());
				sourceWidgetValue = Double.parseDouble(matcher.replaceAll(""));
			}


			// If value < 0, return RET_CANCEL
			if (sourceWidgetValue < 0){
				String[] parameters = new String[2];

				parameters[0] = colonStrip(readLabel(sourceWidget));
				parameters[1] = tempValue.toString();
				
				throw new FormException(ERROR_MESSAGE, parameters);
			}
		}
		// Result Found
		return RET_CONTINUE;
	}
	
	private String colonStrip(String label){
		Pattern pattern = Pattern.compile("\\:");
		Matcher matcher = pattern.matcher(label);
		return 	matcher.replaceAll("");
	}
	
	private String readLabel(RuntimeFormWidgetInterface widgetName){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widgetName.getLabel("label",locale);
	}
}
