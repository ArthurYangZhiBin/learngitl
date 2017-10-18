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

package com.ssaglobal.scm.wms.wm_ws_defaults.favorites;

import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.jobscheduler.JobSchedulerHelper;
import com.epiphany.shr.jobscheduler.beans.ScheduleGroup;
import com.epiphany.shr.ui.metadata.AttributeDomainExtensionBase;
import com.epiphany.shr.ui.metadata.DropdownContentsContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.util.UserUtil;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.quartz.JobDetail;
import org.quartz.Scheduler;

public class ComputedAttrDomFavorites extends AttributeDomainExtensionBase
{
	
	public ComputedAttrDomFavorites()
	{
	}
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ComputedAttrDomFavorites.class);
	protected int execute(DropdownContentsContext context, List value, List labels)
	throws EpiException
	{
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Executing ComputedAttrDomFavorites",100L);		
		try
		{
			String uid = UserUtil.getUserId(context.getState());
			Query loadBiosQry = null;			
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();														
			HttpSession session = context.getState().getRequest().getSession();
//HC-defaultScreen.b
//			String dbName = (String)session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION);
			String temp = context.getState().getCurrentRuntimeForm().getFormWidgetByName("FACILITY").getDisplayValue();
			String dbName = temp==null ? temp : temp.toUpperCase();
////HC-defaultScreen.e
			if(dbName == null || dbName.length() == 0)
				return RET_CONTINUE;
			if(dbName.equalsIgnoreCase("enterprise")){
				loadBiosQry = new Query("screens","screens.ISENTERPRISE = '1'AND screens.DEFAULTSCREENFLAG = '1'","screens.SCREENDESC");
			}
			else{
				loadBiosQry = new Query("screens","screens.ISENTERPRISE = '0'AND screens.DEFAULTSCREENFLAG = '1'","screens.SCREENDESC");
			}									
			_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Query:"+loadBiosQry.getQueryExpression(),100L);			
			BioCollectionBean bioCollection = uow.getBioCollectionBean(loadBiosQry);			
			for(int i = 0; i < bioCollection.size(); i++){
				labels.add(bioCollection.elementAt(i).get("SCREENDESC").toString());
				value.add(bioCollection.elementAt(i).get("SCREENCODE").toString());				
			}
		}
		catch(Exception e)
		{
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_FAVORITES_CANNOT_RETRIEVE",args,context.getState().getLocale());
			throw new UserException(errorMsg,new Object[0]);			
		}
		_log.debug("LOG_DEBUG_EXTENSION_CLEANSESSION","Leaving ComputedAttrDomFavorites",100L);		
		return 0;
	}
}
