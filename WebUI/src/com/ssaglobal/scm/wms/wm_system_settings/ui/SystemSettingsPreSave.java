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

package com.ssaglobal.scm.wms.wm_system_settings.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes

import java.util.ArrayList;
import java.util.regex.Pattern;

import org.apache.log4j.NDC;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class SystemSettingsPreSave extends com.epiphany.shr.ui.action.ActionExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(SystemSettingsPreSave.class);

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
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();

		RuntimeFormInterface headerForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_system_settings_list_view", state);
		BioCollectionBean colHeader = (BioCollectionBean) headerForm.getFocus();

		RuntimeFormInterface detailForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_system_settings_NB_list_view", state);
		BioCollectionBean colDetail = (BioCollectionBean) detailForm.getFocus();

		for (int i = 0; i < colDetail.size(); i++)
		{
			BioBean selected = colDetail.get(String.valueOf(i));
			if (selected.getValue("CONFIGKEY").toString().equals("DEFAULTRECEIVELOCATION"))
			{
				//check location
				String loc = selected.getValue("NSQLVALUE") == null ? null
						: selected.getValue("NSQLVALUE").toString().toUpperCase();
				String query = "wm_location.LOC = '"+ loc + "'";
				_log.debug("LOG_DEBUG_EXTENSION_SystemSettingsPreSave", query, SuggestedCategory.NONE);
				BioCollectionBean results = uowb.getBioCollectionBean(new Query("wm_location", query, null));
				_log.debug("LOG_DEBUG_EXTENSION_SystemSettingsPreSave", "" +results.size(), SuggestedCategory.NONE);
				if (results.size() == 0)
				{
					//throw exception
					String[] parameters = new String[2];
					parameters[0] = selected.getValue("NSQLDESCRIP").toString();
					parameters[1] = loc;
					throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
				}
			}else if(selected.getValue("CONFIGKEY").toString().equals("LABORRPTTIME")){//mark ma added on 11/17/2008
				String laborCutOffTime = selected.getValue("NSQLVALUE") == null ? null
						: selected.getValue("NSQLVALUE").toString();
				if(laborCutOffTime == null || "".equalsIgnoreCase(laborCutOffTime)){
					throw new UserException("WMEXP_NO_LABOR_VALUE_ENTERED", new Object[]{});					
				}else{
					if(!isTimeFormatted(laborCutOffTime)){
						throw new UserException("WMEXP_NO_LABOR_VALUE_NOT_FORMATED",new Object[] {});
					}
				}
			}
			else
			{
				//ensure it's an int
				String value = selected.getValue("NSQLVALUE") == null ? null
						: selected.getValue("NSQLVALUE").toString();

				int nValue;
				try
				{
					nValue = Integer.parseInt(value);
				} catch (NumberFormatException e)
				{
					throw new UserException("WMEXP_NON_INTEGER", new Object[] { selected.getValue("NSQLDESCRIP") });
				}

				if (selected.getValue("CONFIGKEY").toString().equals("COOLBIOCHECK")
						|| selected.getValue("CONFIGKEY").toString().equals("OPPORTUNISTICCROSSDOCK"))
				{
					//ensure it's 0,1,2
					if (nValue != 0)
					{
						if (nValue != 1)
						{
							if (nValue != 2)
							{
								throw new UserException("WMEXP_SS_012", new Object[] { selected.getValue("NSQLDESCRIP") });
							}
						}
					}
					
					

				}

			}
		}

		return RET_CONTINUE;
	}
	
	
	public boolean isTimeFormatted(String time){

		boolean isFormated = Pattern.matches("\\d\\d:\\d\\d", time);
		if(isFormated){
			ArrayList<String> tmp = this.getHourMinute(time);
			String hour = tmp.get(0);
			String minute = tmp.get(1);
			int hourInt = Integer.parseInt(hour);
			int minuteInt = Integer.parseInt(minute);
			if(hourInt >=24 || hourInt < 0){
				return false;
			}else{
				if(minuteInt >=60 || minuteInt < 0){
					return false;
				}else{
					return true;
				}
			}
		}
		return false;
	}
	
	public ArrayList<String> getHourMinute(String time){
		//array[0] holds hour, array[1] holds minute
		if(time != null && !time.equalsIgnoreCase("")){
			ArrayList<String> temp = new ArrayList<String>();
			int index = time.indexOf(":");
			String hour = time.substring(0,index);
			String minute = time.substring(index+1);
			hour = this.takeOffZero(hour);
			minute = this.takeOffZero(minute);
			temp.add(0,hour);
			temp.add(1, minute);
			return temp;
		}
		return null;
	}
	public String takeOffZero(String time){
		
		if(time.charAt(0)=='0'){
			time = time.substring(time.length()-1);
		}
		return time;
	}


}
