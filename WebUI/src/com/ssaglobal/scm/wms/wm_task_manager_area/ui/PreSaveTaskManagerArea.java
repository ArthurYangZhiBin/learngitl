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
package com.ssaglobal.scm.wms.wm_task_manager_area.ui;

import com.agileitp.forte.framework.Array;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationInsertImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class PreSaveTaskManagerArea extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(PreSaveTaskManagerArea.class);

	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException, UserException
	{

		_log.debug("LOG_DEBUG_EXTENSION_AREA", "Executing PreSaveTaskManagerArea", 100L);
		String shellSlot1 = "list_slot_1";
		String shellSlot2 = "list_slot_2";
		String[] param = new String[1];
		String query = null;
		Array params = new Array();
		String autoAssign = "1";
		String skipAll = "1";
		String qtyVerif = "1";
		String workIDLength = "-1";
		String noContTrack = "0";
		BioBean headerBioBean = null;
		QBEBioBean detailQBEBioBean = null;

		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();

		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm(); //get the toolbar
		RuntimeFormInterface shellForm = toolbar.getParentForm(state); //get the Shell form
		SlotInterface headerSlot = shellForm.getSubSlot(shellSlot1); //Get slot1

		SlotInterface detailSlot = shellForm.getSubSlot(shellSlot2); //Set slot 2
		RuntimeFormInterface tabGroupForm = state.getRuntimeForm(detailSlot, null); //Get the form at slot2
		SlotInterface tabGroupSlot = tabGroupForm.getSubSlot("tbgrp_slot");
		RuntimeFormInterface detailFormSubSlot = state.getRuntimeForm(tabGroupSlot, "tab 0");
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailFormSubSlot.getSlot(), null);
		RuntimeFormInterface subSlotForm = state.getRuntimeForm(detailForm.getSubSlot("wm_task_manager_area_form_slot"), null);
		RuntimeFormInterface headerForm = state.getRuntimeForm(tabGroupSlot, "tab 1");

		if (subSlotForm.isListForm())
		{
			context.getSourceWidget().setProperty(RuntimeFormInterface.PROP_READONLY, Boolean.TRUE);
			//throw new UserException("SKU_VALIDATION", new Object[1]); //ignoring this case- added functionality to allow save
		}
		else
		{
			DataBean focus = subSlotForm.getFocus();
			if (focus.isTempBio())
			{
				focus = focus;
			}
			else
			{
				focus = focus;
			}
			RuntimeFormWidgetInterface areaKey = subSlotForm.getFormWidgetByName("AREAKEY");
			RuntimeFormWidgetInterface zone = subSlotForm.getFormWidgetByName("PUTAWAYZONE");

			boolean keyIsNull = checkNull(areaKey);
			boolean zoneIsNull = checkNull(zone);

			if (keyIsNull)
			{
				param[0] = "Area Key";
				throw new UserException("WMEXP_REQ", param);
			}
			if (zoneIsNull)
			{
				param[0] = "Zone";
				throw new UserException("WMEXP_REQ", param);
			}

			//WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
			String areaKeyStr = focus.getValue("AREAKEY").toString();
			String zoneVal = focus.getValue("PUTAWAYZONE").toString();
			_log.debug("LOG_SYSTEM_OUT", "\n\n****AREAKEY val: " + areaKeyStr, 100L);

			String newArea = checkAreaKey(areaKeyStr);
			areaKey.setDisplayValue(newArea);
			focus.setValue("AREAKEY", newArea);

			EXEDataObject dataSelectObject;
			EXEDataObject dataInsertObject;

			DataBean headerFocus = headerForm.getFocus();
			QBEBioBean qbeHeader = null;
			boolean exists = recordExists("area", newArea);
			if (exists)
			{
				//detail
				if (focus instanceof QBEBioBean)
				{
					detailQBEBioBean = (QBEBioBean) focus;
					//validateDetail(detailQBEBioBean);
					String selectQry = "SELECT * from areadetail WHERE AREAKEY='" + newArea + "' and PUTAWAYZONE='" + zoneVal + "'";

					try
					{
						dataSelectObject = WmsWebuiValidationSelectImpl.select(selectQry);
						if (dataSelectObject.getCount() < 1)
						{
							//no duplicates insert record in areadetail
							String insertQry = "INSERT INTO areadetail (AREAKEY, PUTAWAYZONE) VALUES ('" + newArea + "', '" + zoneVal + "')";
							try
							{
								dataInsertObject = WmsWebuiValidationInsertImpl.insert(insertQry);
							} catch (DPException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
						else
						{
							//duplicate throw error
							String[] parameters = new String[2];
							parameters[0] = newArea;
							parameters[1] = zoneVal;
							throw new UserException("WMEXP_DUP_AREADETAIL", parameters);
						}
					} catch (DPException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}

			/*
			Iterator widgets= headerForm.getFormWidgets();
			while (widgets.hasNext() ) {
					Object obj = widgets.next();
					RuntimeFormWidgetInterface headerWidget = (RuntimeFormWidgetInterface)obj;
					if(!headerWidget.getName().equals("LocDesc") && !headerWidget.getName().equals("AisleDesc") && !headerWidget.getName().equals("placeholder"))
					{
						
						boolean widgetIsNull= checkNull(headerWidget);
						if(widgetIsNull)
						{
						headerFocus.setValue(headerWidget.getName(), "0");
						}
						_log.debug("LOG_SYSTEM_OUT","\n*Widget Name: " +headerWidget.getName()+"\t*Widget Value: " +headerWidget.getValue().toString(),100L);
						params.add(new TextData(headerWidget.getValue().toString()));
					}	
			}
			params.add(new TextData(areaKeyStr));
			params.add(new TextData(autoAssign));
			params.add(new TextData(skipAll));
			params.add(new TextData(qtyVerif));
			params.add(new TextData(workIDLength));
			params.add(new TextData(noContTrack));
					
			actionProperties.setProcedureParameters(params);
			actionProperties.setProcedureName("NSPUPDATEAREA");
			try {
				WmsWebuiActionsImpl.doAction(actionProperties);
			} catch (WebuiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			
			_log.debug("LOG_SYSTEM_OUT","******\n\n\n%%%%%\tMessage " + e.getMessage(),100L);
			
			
			}*/

			result.setFocus(headerFocus);
		}

		_log.debug("LOG_DEBUG_EXTENSION_AREA", "Exiting PreSaveTaskManagerArea", 100L);
		return RET_CONTINUE;
	}

	private String checkAreaKey(String keyStr) throws UserException
	{

		// TODO Auto-generated method stub
		/* kkuchipu SCM-00000-04386
		 *  commented if loop entirely and added below line to convert Area to upper string. 
		 *  User will be able to enter special characters in Area Key.
		 */
		keyStr = keyStr.toUpperCase();
		/*
		if(keyStr.matches("[a-zA-Z0-9]+"))
		{
			keyStr= keyStr.toUpperCase();
		}
		else 
		{
		 String [] param = new String[2];
		 param[0] = keyStr;
		 param[1] = "Area Key";
		 throw new UserException("WMEXP_SP_CHARS", param);
		}
		*/

		return keyStr;
	}

	private boolean recordExists(String table, String areaKeyStr)
	{
		// TODO Auto-generated method stub
		String sql = "select * from " + table + " where AREAKEY='" + areaKeyStr + "'";
		EXEDataObject dataObject;
		try
		{
			dataObject = WmsWebuiValidationSelectImpl.select(sql);
			if (dataObject.getCount() > 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		} catch (DPException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	private boolean checkNull(RuntimeFormWidgetInterface widget)
	{
		// TODO Auto-generated method stub
		if (widget.getDisplayValue() == null)
			return true;
		else if (widget.getValue().toString().matches("\\s*"))
			return true;
		else
			return false;
	}
}
