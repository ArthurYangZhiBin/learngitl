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

package com.ssaglobal.scm.wms.wm_appointment.chart;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.EpiRuntimeException;
import com.ssaglobal.scm.wms.util.ReportUtil;
import com.ssaglobal.scm.wms.util.SessionUtil;
import com.ssaglobal.scm.wms.wm_appointment.chart.util.AppointmentSessionObject;

/*
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected to be
 * provided from the meta <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class AppointmentGraphQuery extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	public static final String APPOINTMENT_DEFAULT = "APPOINTMENT_DEFAULT";

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

		StateInterface state = context.getState();
		// String date = state.getURLParameter("tooltip");
		AppointmentSessionObject appObj = (AppointmentSessionObject) SessionUtil.getInteractionSessionAttribute(AppointmentSessionObject.APP_SESSION_OBJECT, state);
		//AppObj will have date/time/door
		Date date = appObj.getDateCal().getTime();
		String door = appObj.getDoor();
		DateFormat dateShortFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, state.getUser().getLocale().getJavaLocale());
		DateFormat timeShortFormat = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT, state.getUser().getLocale().getJavaLocale());

		DateFormat bioDF = new SimpleDateFormat("yyyy/MM/dd");

		UnitOfWorkBean uow = state.getTempUnitOfWork();

		Query newQuery = new Query("wm_appointment", "", "wm_appointment.GMTSTARTDATEANDTIME DESC");
		BioCollectionBean oldCollection = uow.getBioCollectionBean(newQuery);
		QBEBioBean bio = createNewQBE(state, "wm_appointment");
		//Set Filter
		String dateString = dateShortFormat.format(date) + "|" + ReportUtil.getTimeZone(state).getID() + "|" + state.getUser().getLocale().getJavaLocale() + "|#EDATE#|"
				+ ((SimpleDateFormat) dateShortFormat).toPattern();
		bio.setValue("GMTSTARTDATEANDTIME", dateString);
		bio.setValue("DOOR", door);
		BioCollectionBean newCollection = (BioCollectionBean) oldCollection.filter(bio.getQueryWithWildcards());
		//BioCollectionBean newCollectionB = (BioCollectionBean) oldCollection.filter(bio.getQueryWithWildcards());
		//		newCollectionB.copyFrom(oldCollection);
		//		oldCollection.copyFrom(newCollection);
		//		newCollection = oldCollection;
		bio.setBaseBioCollectionForQuery(oldCollection);
		newCollection.setQBEBioBean(bio);
		newCollection.filterInPlace(bio.getQueryWithWildcards());
		result.setFocus(newCollection);

		//Set Default Data
		HttpSession session = state.getRequest().getSession();
		session.setAttribute(APPOINTMENT_DEFAULT, appObj);
		

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return RET_CONTINUE;
	}

	private QBEBioBean createNewQBE(StateInterface state, String bioType)
	{
		UnitOfWorkBean tempUowb = state.getDefaultUnitOfWork();
		QBEBioBean qbe;
		try
		{
			qbe = tempUowb.getQBEBio(bioType);
		} catch (DataBeanException ex)
		{
			Object args[] = { bioType };
			throw new EpiRuntimeException("EXP_INVALID_QUERY_TYPE_QACTION", "A QBE Bio could not be created for bio type {0}", args);
		}
		return qbe;
	}

	/*
	//		
	String queryString = "wm_appointment.GMTSTARTDATEANDTIME >= @DATE['yyyy/MM/dd HH:mm:ss','" + bioDF.format(date) + " 00:00:00'] and "
			+ "wm_appointment.GMTSTARTDATEANDTIME <= @DATE['yyyy/MM/dd HH:mm:ss','" + bioDF.format(date) + " 23:59:59']";

	Query query = new Query("wm_appointment", queryString, "wm_appointment.USERSTARTDATEANDTIME DESC, wm_appointment.GMTSTARTDATEANDTIME DESC");


	BioCollectionBean collection = uow.getBioCollectionBean(new Query("wm_appointment", null, null));
	QBEBioBean bio = createNewQBE(state, collection.getBioTypeName());
	BioCollectionBean newCollection = (BioCollectionBean) collection.filter(bio.getQueryWithWildcards());
	BioCollectionBean newCollectionB = (BioCollectionBean) collection.filter(bio.getQueryWithWildcards());
	newCollectionB.copyFrom(collection);
	collection.copyFrom(newCollection);
	newCollection = collection;
	bio.setBaseBioCollectionForQuery(newCollectionB);
	newCollection.setQBEBioBean(bio);
	newCollection.filterInPlace(query);
	result.setFocus(newCollection);
	
	*/
	/*
	String qry = "wm_appointment.CARRIER = 'A'";
	Query loadBiosQry = new Query("wm_appointment", qry, null);

	BioCollectionBean collection = state.getDefaultUnitOfWork().getBioCollectionBean(new Query("wm_appointment", null, null));
	QBEBioBean bio = createNewQBE(state, collection.getBioTypeName());
	BioCollectionBean newCollection = (BioCollectionBean) collection.filter(bio.getQueryWithWildcards());
	BioCollectionBean newCollectionB = (BioCollectionBean) collection.filter(bio.getQueryWithWildcards());
	newCollectionB.copyFrom(collection);
	collection.copyFrom(newCollection);
	newCollection = collection;
	bio.setBaseBioCollectionForQuery(newCollectionB);
	newCollection.setQBEBioBean(bio);
	newCollection.filterInPlace(loadBiosQry);
	result.setFocus(newCollection);
	*/

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked
	 * or a value entered in a form in a modal dialog Write code here if u want
	 * this to be called when the UI Action event is fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext
	 * ModalActionContext} exposes information about the event, including the
	 * service and the user interface
	 * {@link com.epiphany.shr.ui.state.StateInterface state}.</li> <li>
	 * {@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes
	 * information about the results of the action that has occurred, and
	 * enables your extension to modify them.</li>
	 * </ul>
	 */
	@Override
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
	{

		try
		{
			// Add your code here to process the event

		} catch (Exception e)
		{

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

}
