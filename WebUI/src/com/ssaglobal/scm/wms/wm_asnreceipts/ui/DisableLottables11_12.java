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


package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
//import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.ssaglobal.scm.wms.util.FormUtil;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class DisableLottables11_12 extends com.epiphany.shr.ui.action.ActionExtensionBase 
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(DisableLottables11_12.class);

	/**
	 * Gets the sku bio.
	 *
	 * @param state the state
	 * @return the sku bio
	 */
	private BioBean getSkuBIO( StateInterface state )
	{
		try
		{
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();		
			//			final String TopLevelForm = getParameterString("TopLevelForm");
			RuntimeFormInterface LottablesDetailFrom = state.getCurrentRuntimeForm();
			DataBean detailFocus = LottablesDetailFrom.getFocus();
			//			RuntimeFormInterface headerForm = FormUtil.findForm(LottablesDetailFrom.getParentForm(state),"",TopLevelForm,state);
			//			_log.debug("LOG_SYSTEM_OUT","HEADER FORM = "+ headerForm.getName(),100L);
			//			DataBean headerFocus = headerForm.getFocus();
			Object skuObj = detailFocus.getValue("SKU");
			Object storerObject = detailFocus.getValue("STORERKEY");
			if (skuObj != null && storerObject != null)
			{
				BioCollectionBean listCollection = null;
				String skuVal = skuObj.toString().trim();
				String storerKey = storerObject.toString().trim();
				_log.debug("LOG_SYSTEM_OUT","SKU ="+ skuObj.toString()+ "End",100L);
				String sQueryString = "(wm_sku.STORERKEY = '"+storerKey+"' AND wm_sku.SKU = '"+skuVal+"')";
				_log.debug("LOG_SYSTEM_OUT","sQueryString = "+ sQueryString,100L);
				Query bioQuery = new Query("wm_sku",sQueryString,null);
				listCollection = uowb.getBioCollectionBean(bioQuery);
				BioBean skuBio = (BioBean)listCollection.elementAt(0);
				return skuBio;
			}
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			return null;
		}		

		return null;
	}

	/**
	 * Gets the lottable.
	 *
	 * @param state the state
	 * @param valkey the valkey
	 * @return the lottable
	 * @throws EpiException the epi exception
	 */
	private BioBean getLottable( StateInterface state, String valkey ) throws EpiException
	{

		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		BioCollectionBean listCollection = null;
		String sQueryString = "(wm_lottablevalidationdetail.LOTTABLEVALIDATIONKEY = '"+valkey+"' ) ";

		_log.debug("LOG_SYSTEM_OUT","sQueryString = "+ sQueryString,100L);
		Query bioQuery = new Query("wm_lottablevalidationdetail",sQueryString,null);
		listCollection = uowb.getBioCollectionBean(bioQuery);

		if ( listCollection.size() > 0 )
		{
			return (BioBean)listCollection.elementAt(0);
		}

		return null;
	}

	/**
	 * Gets the code lookup.
	 *
	 * @param state the state
	 * @param code the code
	 * @return the code lookup
	 * @throws EpiException the epi exception
	 */
	private BioBean getCodeLookup( StateInterface state, String code ) throws EpiException
	{
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		BioCollectionBean listCollection = null;
		String sQueryString = "(codelkup.CODE = '"+code+"' and codelkup.LISTNAME = 'LOTCONPROC') ";

		_log.debug("LOG_SYSTEM_OUT","sQueryString = "+ sQueryString,100L);
		Query bioQuery = new Query("codelkup",sQueryString,null);
		listCollection = uowb.getBioCollectionBean(bioQuery);

		if ( listCollection.size() > 0 )
		{
			return (BioBean)listCollection.elementAt(0);
		}

		return null;
	}

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
	protected int execute( ActionContext context, ActionResult result ) throws EpiException 
	{
		try
		{			
			disableLottable11_12(context.getState());
		} 
		catch(Exception e) 
		{	            
			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;          
		} 

		return RET_CONTINUE;
	}

	/**
	 * Disable lottable11_12.
	 *
	 * @param state the state
	 * @throws EpiException the epi exception
	 * @throws EpiDataException the epi data exception
	 */
	public void disableLottable11_12(StateInterface state) throws EpiException,
	EpiDataException {
		BioBean sku = getSkuBIO( state );

		// we have a valid SKU record.
		if ( sku != null )
		{	
			BioBean val = getLottable( state, sku.getString("LOTTABLEVALIDATIONKEY"));

			// that SKU record has a valid LOTTABLEVALDATION defined for it
			if ( val != null )
			{
				String[] fields = { "01", "02", "03", "06", "07", "08", "09", "10" };
				for ( int idx = 0; idx < fields.length; ++idx)
				{
					String conv = val.getString("LOTTABLE"+fields[idx]+"RECEIPTCONVERSION");
					if ( conv != "")
					{
						BioBean code = getCodeLookup(state,conv);
						if ( code != null )
						{
							String fld = code.getString("short");
							if ("lottable11".equals(fld))
							{

								RuntimeFormInterface detailForm = state.getCurrentRuntimeForm();					
								detailForm.getFormWidgetByName("LOTTABLE11").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);
							}
							else if ("lottable12".equals(fld))
							{

								RuntimeFormInterface detailForm = state.getCurrentRuntimeForm();					
								detailForm.getFormWidgetByName("LOTTABLE12").setProperty(RuntimeFormWidgetInterface.PROP_READONLY,Boolean.TRUE);								
							}							
						}
					}
				}
			}
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
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException 
	{
		try 
		{
			// Add your code here to process the event

		} 
		catch(Exception e) 
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;          
		} 

		return RET_CONTINUE;
	}
}
