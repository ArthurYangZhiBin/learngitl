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

package com.ssaglobal.scm.wms.wm_trailer.ui;

// Import 3rd party packages and classes

import java.util.GregorianCalendar;

//Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.FormUtil;

/*
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected to be
 * provided from the meta <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class TrailerSave extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(TrailerSave.class);

	/**
	 * The code within the execute method will be run from a UIAction specified
	 * in metadata.
	 * <P>
	 * 
	 * @param context
	 *            The ActionContext for this extension
	 * @param result
	 *            The ActionResult for this extension (contains the focus and
	 *            perspective for this UI Extension)
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * 
	 * @exception EpiException
	 */
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{


		//context.setNavigation("clickEventSave");
		//RuntimeFormWidgetInterface sourceWidget = context.getSourceWidget();
		//String name = sourceWidget.getName();
		//Navigation navigation = context.getNavigation();
		boolean checkLength = true;
		if (context.getNavigation() == null)
		{
			checkLength = false;
		}
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		//UnitOfWorkBean uow1 = state.getDefaultUnitOfWork();
		//BioBean poBioBean = null;
		String userid = (String)state.getUser().getName();
		int listsize = 0;
		RuntimeFormInterface form = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_trailer_detail_view", state);
		RuntimeFormInterface listForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_trailer_list_view", state);
		//String status=form.getFocus().getValue("TRAILERSTATUS").toString();

		if(form.getFocus() instanceof QBEBioBean)
		{
			if((form.getFocus().getValue("TRAILERSTATUS").toString()).equalsIgnoreCase("7OP")){
				form.getFocus().setValue("OPENTIME", new GregorianCalendar());
				form.getFocus().setValue("OPENUSER", userid);
			}

			if((form.getFocus().getValue("TRAILERSTATUS").toString()).equalsIgnoreCase("5COMP")){
				form.getFocus().setValue("CLOSETIME", new GregorianCalendar());
				form.getFocus().setValue("CLOSEUSER", userid);
			}

			if((form.getFocus().getValue("TRAILERSTATUS").toString()).equalsIgnoreCase("2CIN")){
				form.getFocus().setValue("ARRIVALTIME", new GregorianCalendar());
				form.getFocus().setValue("DOOR", "YARD");
			}

			if((form.getFocus().getValue("TRAILERSTATUS").toString()).equalsIgnoreCase("4COUT")){
				form.getFocus().setValue("DEPARTURETIME", new GregorianCalendar());
				form.getFocus().setValue("DOOR", " ");
			}
			form.getFocus().save();
		}


		if (listForm != null && listForm.isListForm())
		{
			BioCollectionBean list = (BioCollectionBean) ((RuntimeListFormInterface) listForm).getFocus();
			listsize = list.size();
			for (int i = 0; i < list.size(); i++)
			{
				BioBean row = list.get("" + i);
				if(row.getUpdatedAttributes()!= null && row.getUpdatedAttributes().size()>0)
					if (row.hasBeenUpdated("CARRIERCODE"))
					{
						Object attributeValue = row.getValue("CARRIERCODE");
						if (!isEmpty(attributeValue))
						{
							attributeValue = attributeValue == null ? null : attributeValue.toString().toUpperCase();
							String type = "3";
							String query = "wm_storer.STORERKEY = '" + attributeValue + "' and wm_storer.TYPE = '" + type + "'";
							BioCollectionBean rs = state.getTempUnitOfWork().getBioCollectionBean(new Query("wm_storer", query, null));
							_log.debug("LOG_DEBUG_EXTENSION", "Query\n" + query, SuggestedCategory.NONE);
							if (rs.size() < 1)
							{
								String[] parameters = new String[3];
								parameters[0] = retrieveLabel(listForm, "CARRIERCODE");
								parameters[1] = attributeValue.toString();
								throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
							}

						}
						else
						{
							throw new UserException("WMEXP_NO_BLANKS_ALLOWED", new Object[] { retrieveLabel(listForm, "CARRIER") });
						}
					}
				if (row.hasBeenUpdated("DOOR"))
				{
					Object attributeValue = row.getValue("DOOR");
					if (!isEmpty(attributeValue))
					{
						attributeValue = attributeValue == null ? null : attributeValue.toString().toUpperCase();
						//String type = "DOOR";
						String query = "wm_location.LOC = '" + attributeValue + "' and ( wm_location.LOCATIONTYPE = 'DOOR' OR wm_location.LOCATIONTYPE = 'STAGED' )";
						BioCollectionBean rs = state.getTempUnitOfWork().getBioCollectionBean(new Query("wm_location", query, null));
						_log.debug("LOG_DEBUG_EXTENSION", "Query\n" + query, SuggestedCategory.NONE);
						if (rs.size() < 1)
						{
							String[] parameters = new String[3];
							parameters[0] = retrieveLabel(listForm, "DOOR");
							parameters[1] = attributeValue.toString();
							throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
						}
					}
					else
					{
						throw new UserException("WMEXP_NO_BLANKS_ALLOWED", new Object[] { retrieveLabel(listForm, "CARRIER") });
					}

				}

				if (row.hasBeenUpdated("TRAILERSTATUS"))
				{
					Object attributeValue = row.getValue("TRAILERSTATUS");
					if (!isEmpty(attributeValue))
					{
						attributeValue = attributeValue == null ? null : attributeValue.toString().toUpperCase();
						if((attributeValue.toString()).equalsIgnoreCase("7OP")){
							row.set("OPENUSER", userid); 
							row.set("OPENTIME", new GregorianCalendar());
						}
						if((attributeValue.toString()).equalsIgnoreCase("5COMP")){
							row.set("CLOSEUSER", userid); 
							row.set("CLOSETIME", new GregorianCalendar());
						}
						if((attributeValue.toString()).equalsIgnoreCase("2CIN")){
							row.set("DOOR", "YARD") ;
							row.set("ARRIVALTIME", new GregorianCalendar());
						}
						if((attributeValue.toString()).equalsIgnoreCase("4COUT")){
							row.set("DOOR", " ") ;
							row.set("DEPARTURETIME", new GregorianCalendar());
						}

					}
				}
			}
		}

		RuntimeFormInterface detailForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_trailer_detail_view", state);

		if (detailForm != null)
		{
			if (listsize ==0){
				if(detailForm.getFocus().isBio())
				{
					if((((BioBean)detailForm.getFocus())).hasBeenUpdated("TRAILERSTATUS"))
					{
						if((detailForm.getFocus().getValue("TRAILERSTATUS").toString()).equalsIgnoreCase("7OP")){
							detailForm.getFocus().setValue("OPENTIME", new GregorianCalendar());
							detailForm.getFocus().setValue("OPENUSER", userid);
						}

						if((detailForm.getFocus().getValue("TRAILERSTATUS").toString()).equalsIgnoreCase("5COMP")){
							detailForm.getFocus().setValue("CLOSETIME", new GregorianCalendar());
							detailForm.getFocus().setValue("CLOSEUSER", userid);
						}

						if((detailForm.getFocus().getValue("TRAILERSTATUS").toString()).equalsIgnoreCase("2CIN")){
							detailForm.getFocus().setValue("ARRIVALTIME", new GregorianCalendar());
							detailForm.getFocus().setValue("DOOR", "YARD");
						}

						if((detailForm.getFocus().getValue("TRAILERSTATUS").toString()).equalsIgnoreCase("4COUT")){
							detailForm.getFocus().setValue("DEPARTURETIME", new GregorianCalendar());
							detailForm.getFocus().setValue("DOOR", " ");
						}
						detailForm.getFocus().save();
					}
				}
			}
		}

		try{
			//listForm.getFocus().save();
			uow.saveUOW(true);
			uow.clearState();

			//   result.setFocus(detailForm.getFocus());
		}
		catch(Exception e){
			String log = "in exception";
		}


		return RET_CONTINUE;
	}

	protected boolean isEmpty(Object attributeValue)
	{
		if (attributeValue == null)
		{
			return true;
		}
		else if (attributeValue.toString().matches("\\s*"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	String retrieveLabel(RuntimeFormInterface form, String widgetName)
	{
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return form.getFormWidgetByName(widgetName).getLabel("label", locale);
	}

}
