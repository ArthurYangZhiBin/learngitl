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
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FieldException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;

public class TableValidationAction extends com.epiphany.shr.ui.action.ActionExtensionBase {
	protected int execute(ActionContext context, ActionResult result) throws EpiException {
		// Parameters
		final String BIO = getParameterString("BIO");
		final String ATTRIBUTE = getParameterString("ATTRIBUTE");
		final String WIDGET = getParameter("WIDGET") == null ? ATTRIBUTE : getParameterString("WIDGET");
		final String ERROR_MESSAGE = getParameter("ERROR_MESSAGE") == null ? "WMEXP_NOT_A_VALID"
				: getParameterString("ERROR_MESSAGE");

		RuntimeFormWidgetInterface sourceWidget = context.getSourceWidget();
		String sourceWidgetValue = sourceWidget.getDisplayValue();

		if (sourceWidgetValue != null) {
			sourceWidgetValue = sourceWidgetValue.toUpperCase();
		}
		DataBean focus = context.getState().getFocus();
		if (focus.isTempBio()) {
			QBEBioBean qbe = (QBEBioBean) focus;
			qbe.set(WIDGET, sourceWidgetValue);
		} else {
			BioBean bio = (BioBean) focus;
			bio.set(WIDGET, sourceWidgetValue);
		}

		// Query Bio to see if Attribute exists
		String queryStatement = null;
		Query query = null;
		BioCollection results = null;

		try {
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
			queryStatement = BIO + "." + ATTRIBUTE + " = '" + sourceWidgetValue + "'";
			query = new Query(BIO, queryStatement, null);
			results = uow.getBioCollectionBean(query);
		} catch (Exception e) {
			throw new EpiException("QUERY_ERROR", "Error preparing search query" + queryStatement);
		}

		// If BioCollection size equals 0, return RET_CANCEL
		if (results.size() == 0) {
			if (focus.isTempBio()) {
				QBEBioBean qbe = (QBEBioBean) focus;
				qbe.set(WIDGET, null);
				result.setFocus(qbe);
			} else {
				BioBean bio = (BioBean) focus;
				bio.set(WIDGET, null);
				result.setFocus(bio);
			}
			String[] parameters = new String[2];
			parameters[0] = sourceWidgetValue;
			parameters[1] = colonStrip(readLabel(sourceWidget));
			throw new FieldException(context.getState().getCurrentRuntimeForm(), context.getSourceWidget().getName(), ERROR_MESSAGE, parameters);
		}
		// Result Found
		return RET_CONTINUE;
	}

	public String colonStrip(String label) {
		Pattern pattern = Pattern.compile("\\:");
		Matcher matcher = pattern.matcher(label);
		return matcher.replaceAll("");
	}

	public String readLabel(RuntimeFormWidgetInterface widget) {
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widget.getLabel("label", locale);
	}

}
