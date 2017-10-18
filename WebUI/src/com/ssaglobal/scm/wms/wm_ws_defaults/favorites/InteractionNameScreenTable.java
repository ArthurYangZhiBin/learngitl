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

import java.util.HashMap;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.objects.EpnyLocale;
import com.epiphany.shr.metadata.objects.Navigation;
import com.epiphany.shr.metadata.objects.generated.np.NavigationFactory;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;

public class InteractionNameScreenTable {
	private static HashMap _INSTable = new HashMap();
	private static InteractionNameScreenTable _instance = null;
	protected static ILoggerCategory _log = LoggerFactory.getInstance(InteractionNameScreenTable.class);
	private InteractionNameScreenTable(){
		
	}
	
	private static void addCurrentLocaleToTable(ActionContext context){
		_log.debug("LOG_DEBUG_CLASS_INTNAMESCREEN","Executing InteractionNameScreenTable.addCurrentLocaleToTable()",100L);	
		Query loadBiosQry = new Query("screens", "", "");
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();									
		BioCollection bioCollection = uow.getBioCollectionBean(loadBiosQry);			
		if(!(state.getLocale() instanceof EpnyLocale)){
			_log.debug("LOG_DEBUG_CLASS_INTNAMESCREEN","Local is not of type EpnyLocale...",100L);
			_log.debug("LOG_DEBUG_CLASS_INTNAMESCREEN","Leaving InteractionNameScreenTable.addCurrentLocaleToTable()",100L);			
			return;
		}
		EpnyLocale locale = (EpnyLocale)state.getLocale();
		try {
			if(bioCollection != null && bioCollection.size() > 0){
				HashMap navToScreenMap = new HashMap();
				for(int i = 0; i < bioCollection.size(); i++){				
					_log.debug("LOG_DEBUG_CLASS_INTNAMESCREEN","Got Bio "+i+" ...",100L);
					Bio screen = bioCollection.elementAt(i);
					String screenKey = screen.getString("SCREENCODE");
					String navigationId = screen.getString("NAVID");
					_log.debug("LOG_DEBUG_CLASS_INTNAMESCREEN","Got SCREENCODE "+screenKey+" ...",100L);
					_log.debug("LOG_DEBUG_CLASS_INTNAMESCREEN","Got NAVID "+navigationId+" ...",100L);
					Navigation navigation = new NavigationFactory(context.getNavigation().getBaseMetaObject().getMetaFactory()).getById(navigationId).getWrapper();
					String label = navigation.getLabel("interaction name",locale);				
					navToScreenMap.put(label,screenKey);
				}
				_log.debug("LOG_DEBUG_CLASS_INTNAMESCREEN","Built Map A:"+navToScreenMap+"...",100L);				
				_INSTable.put(locale.getLocaleIDString(),navToScreenMap);
				_log.debug("LOG_DEBUG_CLASS_INTNAMESCREEN","Built Map B:"+_INSTable+"...",100L);			
			}
		} catch (EpiDataException e) {			
			e.printStackTrace();
		}
		_log.debug("LOG_DEBUG_CLASS_INTNAMESCREEN","Leaving InteractionNameScreenTable.addCurrentLocaleToTable()",100L);
	}
	
	public static String getScreenCodeForCurrInteraction(ActionContext context){
		_log.debug("LOG_DEBUG_CLASS_INTNAMESCREEN","Executing InteractionNameScreenTable.getScreenCodeForCurrInteraction()",100L);		
		StateInterface state = context.getState();
		if(!(state.getLocale() instanceof EpnyLocale)){
			_log.debug("LOG_DEBUG_CLASS_INTNAMESCREEN","Local is not of type EpnyLocale...",100L);
			_log.debug("LOG_DEBUG_CLASS_INTNAMESCREEN","Leaving InteractionNameScreenTable.getScreenCodeForCurrInteraction()",100L);
			return null;
		}
		EpnyLocale locale = (EpnyLocale)state.getLocale();
		if(!_INSTable.containsKey(locale.getLocaleIDString())){
			addCurrentLocaleToTable(context);
		}
		HashMap navToScreenMap = (HashMap)_INSTable.get(locale.getLocaleIDString());
		if(navToScreenMap != null){
			String interactionName = state.getInteractionName();
			String screenCode = (String)navToScreenMap.get(interactionName);
			return screenCode;
		}
		_log.debug("LOG_DEBUG_CLASS_INTNAMESCREEN","Leaving InteractionNameScreenTable.getScreenCodeForCurrInteraction()",100L);
		return null;
	}
}