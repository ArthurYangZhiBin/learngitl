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
package com.infor.scm.waveplanning.wp_query_builder.ui;
import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.infor.scm.waveplanning.common.util.WPUserUtil;
import com.infor.scm.waveplanning.wp_graphical_filters.action.WPGraphicalFilterSendToQueryBuilder;
import com.infor.scm.waveplanning.wp_query_builder.action.WPQueryBuilderNewTempRecord;
import com.infor.scm.waveplanning.wp_wavemgmt_confirmwave.action.ConfirmWaveAddOrderNavSelect;
import com.infor.scm.waveplanning.common.WavePlanningConstants;
import com.infor.scm.waveplanning.common.WavePlanningUtils;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

public class QueryBuilderInitialFormPreRender extends FormExtensionBase{	
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(QueryBuilderInitialFormPreRender.class);
	
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {
		
		if (WavePlanningUtils.wmsName.equals(WavePlanningConstants.WMS_2000)) {
			form.getFormWidgetByName("CASES").setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, false);
			form.getFormWidgetByName("INCLUDERF").setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, true);
		}
		else if (WavePlanningUtils.wmsName.equals(WavePlanningConstants.WMS_4000)){
			form.getFormWidgetByName("CASES").setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, true);
			form.getFormWidgetByName("INCLUDERF").setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, false);
		}
		
		//If any records were loaded by the graphical filter then change those to have this interaction id.
		final HttpSession session = context.getState().getRequest().getSession();
		String graphicalFilterKey = (String)session.getAttribute(WPGraphicalFilterSendToQueryBuilder.SESSION_KEY_GRAPHICAL_FILTER_RECORDS_KEY);
		session.removeAttribute(WPGraphicalFilterSendToQueryBuilder.SESSION_KEY_GRAPHICAL_FILTER_RECORDS_KEY);
		if(graphicalFilterKey != null){
			Query graphicalFilterRecordsQry = new Query("querybuildertemp","querybuildertemp.INTERACTIONID = '"+graphicalFilterKey+"'","");
			BioCollectionBean graphicalFilterRecords = context.getState().getDefaultUnitOfWork().getBioCollectionBean(graphicalFilterRecordsQry);
			if(graphicalFilterRecords != null){
				for(int i = 0; i < graphicalFilterRecords.size(); i++){
					graphicalFilterRecords.elementAt(i).set("INTERACTIONID", context.getState().getInteractionId());
				}
				context.getState().getDefaultUnitOfWork().saveUOW(false);
			}
			
		}
		
		//If any records were loaded by the default filter then change those to have this interaction id.
//		String defaultFilterKey = (String)session.getAttribute(WPQueryBuilderNewTempRecord.SESSION_KEY_DEFAULT_FILTER_RECORDS_KEY);
//		session.removeAttribute(WPQueryBuilderNewTempRecord.SESSION_KEY_DEFAULT_FILTER_RECORDS_KEY);
//		if(defaultFilterKey != null){
/*			Query defaultFilterRecordsQry = new Query("querybuildertemp","querybuildertemp.INTERACTIONID = '"+defaultFilterKey+"'","");
			BioCollectionBean defaultFilterRecords = context.getState().getDefaultUnitOfWork().getBioCollectionBean(defaultFilterRecordsQry);
			if(defaultFilterRecords != null){
				String interactionId = context.getState().getInteractionId();
				for(int i = 0; i < defaultFilterRecords.size(); i++){
					defaultFilterRecords.elementAt(i).set("INTERACTIONID",interactionId );
				}
				context.getState().getDefaultUnitOfWork().saveUOW(false);
			}
*/			
//		}
		
		//If navigating from the Wave Mgmt screens, pull the WaveKey from the session and place it somewhere safe based on this screen's interaction id
		Integer waveKey = (Integer) session.getAttribute(ConfirmWaveAddOrderNavSelect.SESSION_KEY_WAVEMGMT_WAVEKEY_ADD);
		_log.debug("LOG_DEBUG_EXTENSION_QueryBuilderInitialFormPreRender_preRenderForm", "Putting " + waveKey + " Into the Session ",
				SuggestedCategory.NONE);
		session.removeAttribute(ConfirmWaveAddOrderNavSelect.SESSION_KEY_WAVEMGMT_WAVEKEY_ADD);
		if(waveKey != null)
		{
			WPUserUtil.setInteractionSessionAttribute(ConfirmWaveAddOrderNavSelect.SESSION_KEY_WAVEMGMT_WAVEKEY_ADD, waveKey, context.getState());
		}
		
		return RET_CONTINUE;
	}
	
	
}