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

package com.ssaglobal.scm.wms.wm_pickdetail.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeForm;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.service.dutilitymanagement.SerialNumberObj;
import com.ssaglobal.scm.wms.service.dutilitymanagement.dto.SerialNoDTO;
import com.ssaglobal.scm.wms.service.dutilitymanagement.dto.SkuSNConfDTO;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.StringUtils;
import com.ssaglobal.scm.wms.util.dao.SerialInventoryDAO;
import com.ssaglobal.scm.wms.util.dao.SkuSNConfDAO;

/*
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected to be
 * provided from the meta <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class SOCatchWeightDuplicateListSaveVerification extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(SOCatchWeightDuplicateListSaveVerification.class);

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
	@Override
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
	{

		try
		{
			RuntimeFormWidgetInterface sourceWidget = ctx.getSourceWidget();
			RuntimeForm modalBodyForm = ctx.getModalBodyForm(0);
			StateInterface state = ctx.getState();
			RuntimeFormInterface parentForm = ctx.getSourceForm().getParentForm(state).getParentForm(state).getParentForm(state);
			//retrieve Storer & Sku Info
			
			ArrayList tabs = new ArrayList<String>();
			//			tabs.add("tbgrp_slot");
			tabs.add("tab 0");
			RuntimeFormInterface pickDetailForm = FormUtil.findForm(ctx.getSourceForm(), parentForm.getName(), "wm_pickdetail_detail_view", tabs, state);
			DataBean pickDetailFocus = pickDetailForm.getFocus();
			if (pickDetailFocus.getValue("SKU") == null || pickDetailFocus.getValue("STORERKEY") == null)
			{
				throw new UserException("WMEXP_CWCD_OWNERITEM_REQ", new Object[] {});
			}

			String sku = (String) pickDetailFocus.getValue("SKU");
			String owner = (String) pickDetailFocus.getValue("STORERKEY");

			if (modalBodyForm.isListForm())
			{
				RuntimeListFormInterface dupList = (RuntimeListFormInterface) modalBodyForm;
				DataBean dupListBean = dupList.getFocus();
				if (dupListBean.isBioCollection())
				{
					BioCollectionBean dupCollection = (BioCollectionBean) dupListBean;
					for (int i = 0; i < dupCollection.size(); i++)
					{
						BioBean dupCWCD = dupCollection.get("" + i);
						Object snlObject = dupCWCD.getValue("SERIALNUMBERLONG");
						if (snlObject != null && !StringUtils.isEmpty(snlObject.toString()))
						{

							String serialNumberLong = (String) snlObject;

							ArrayList<?> serials = null;

							try
							{
								serials = getSerials(owner, sku, serialNumberLong);
								//jp.answerlink.281034.begin
								String lot = BioAttributeUtil.getString(pickDetailFocus, "LOT");
								String loc = BioAttributeUtil.getString(pickDetailFocus, "LOC");
								String id = BioAttributeUtil.getString(pickDetailFocus, "ID");
								
								SerialInventoryDAO siDAO = new SerialInventoryDAO();
								if(!siDAO.isAvailable(state, owner, sku, lot, loc, id, ((SerialNoDTO) serials.get(0)).getSerialnumber())){
									throw new UserException("WMEXP_SN_NOT_EXISTS", new String[] { serialNumberLong });
								}
								//jp.answerlink.281034.end

							} catch (RuntimeException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
								_log.error("LOG_ERROR_EXTENSION_CatchWeightValidateSerialNumber_execute", StringUtils.getStackTraceAsString(e), SuggestedCategory.NONE);
								throw new UserException("WMEXP_SN_FORMAT", new String[] { serialNumberLong });
							}

							if (serials == null || serials.size() == 0)
							{
								throw new UserException("WMEXP_SN_FORMAT", new String[] { serialNumberLong });
							}
							//Set IOTHER1 with parsed Serial
							dupCWCD.setValue("OOTHER1", ((SerialNoDTO) serials.get(0)).getSerialnumber());

						}
/*						else if (snlObject == null || StringUtils.isEmpty(snlObject.toString()))
						{
							if (sourceWidget.getName().equals("SAVEANDCLOSE"))
							{
								throw new UserException("WMEXP_REQ", new Object[] { modalBodyForm.getFormWidgetByName("SERIALNUMBERLONG").getLabel(RuntimeFormWidgetInterface.LABEL_LABEL,
									state.getLocale()) });
							}
						}*/
					}
				}
			}

		} catch (RuntimeException e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	public ArrayList<?> getSerials(String storerkey, String sku, String serial)
	{

		SkuSNConfDTO skuConf = SkuSNConfDAO.getSkuSNConf(storerkey, sku);

        SerialNumberObj serialNumber = new SerialNumberObj(skuConf);

        serialNumber.setStorerkey(storerkey);

        serialNumber.setSku(sku);

        ArrayList<?> list = serialNumber.getValidSerialNos(serial);

        return list;
	}
}
