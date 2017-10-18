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

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;

/*
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected to be
 * provided from the meta <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class TrailerDetailPreRender extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{

	/**
	 * Called in response to the pre-render event on a form. Write code to
	 * customize the properties of a form. All code that initializes the
	 * properties of a form that is being displayed to a user for the first time
	 * belong here. This is not executed even if the form is re-displayed to the
	 * end user on subsequent actions.
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	@Override
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException
	{

		try
		{
			// Add your code here to process the event
			StateInterface state = context.getState();

			DataBean focus = form.getFocus();

			//convertDateToHMS(form, focus);

			//	setDefaultValues(form, state, focus);

		} catch (Exception e)
		{

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}




	@Override
	protected int modifySubSlots(UIRenderContext context, RuntimeFormExtendedInterface form) throws EpiException
	{
		StateInterface state = context.getState();
		SlotInterface subSlot = form.getSubSlot("LIST_SLOT");
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		DataBean focus = form.getFocus();
		if (focus.isTempBio())
		{
			form.setSpecialFormType(state, subSlot, "", "blank");
			return RET_CONTINUE;
		}
		int type = BioAttributeUtil.getInt(focus, "SOURCETYPE");
		String TrailerKey = BioAttributeUtil.getString(focus, "TRAILERKEY");

		BioCollectionBean trailerDetails = null;

		String query = "";
		if (type == 0)
		{

			query += " receipt.TrailerNumber = '" + TrailerKey + "' ";
			BioCollectionBean rs = uow.getBioCollectionBean(new Query("receipt", query, null));
			if((rs != null)&& (rs.size()==0))
			{
				QBEBioBean bio = uow.getQBEBio("receipt");
				form.setFocus(state, subSlot, "", bio, "wm_trailer_asn_list_view perspective");
			}
			else
			{

				form.setFocus(state, subSlot, "", rs, "wm_trailer_asn_list_view perspective");
			}
		}
		else if (type == 1)
		{


			query += " wm_orders.TrailerNumber = '" + TrailerKey + "' ";
			BioCollectionBean rs = uow.getBioCollectionBean(new Query("wm_orders", query, null));
			if((rs != null)&& (rs.size()==0))
			{
				QBEBioBean bio = uow.getQBEBio("wm_orders");
				form.setFocus(state, subSlot, "", bio, "wm_trailer_order_list_view perspective");
			}
			else
			{

				form.setFocus(state, subSlot, "", rs, "wm_trailer_order_list_view perspective");
			}


			/*
			trailerDetails = (BioCollectionBean) focus.getValue("wm_orders");

			for (int i = 0; i < trailerDetails.size(); i++)
			{
				BioBean apptDetail = trailerDetails.get("" + i);
				if (BioAttributeUtil.getInt(apptDetail, "SOURCETYPE") == 1)
				{
					if (i > 0)
					{
						query += " OR ";
					}
					query += " wm_orders.ORDERKEY = '" + apptDetail.getValue("SOURCEKEY") + "' ";
				}

			}

			if ("".equals(query) || trailerDetails == null || trailerDetails.size() == 0)
			{
				QBEBioBean bio = uow.getQBEBio("wm_orders");
				form.setFocus(state, subSlot, "", bio, "wm_trailer_order_list_view perspective");
			}
			else
			{
				BioCollectionBean rs = uow.getBioCollectionBean(new Query("wm_orders", query, null));
				form.setFocus(state, subSlot, "", rs, "wm_trailer_order_list_view perspective");
			}
			 */}
		else
		{
			form.setSpecialFormType(state, subSlot, "", "blank");
		}

		return RET_CONTINUE;
	}
}
