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
package com.infor.scm.waveplanning.wp_graphical_filters.action;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.common.util.WPUtil;
import com.infor.scm.waveplanning.common.WavePlanningUtils;
import com.infor.scm.waveplanning.wp_graphical_filters.util.WPGraphFilterUtil;


public class WPGraphFilterTmpExtension extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPGraphFilterTmpExtension.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException{
		
		StateInterface state = context.getState();
		String tooltip = state.getURLParameter("tooltip");
		if("".equals(tooltip) && com.infor.scm.waveplanning.wp_wm_wave.wave.WPUtil.isOracle(state)){
			tooltip = " ";
		}
		//state.getRequest().getSession().setAttribute("LAST_ACCESSED_FORM","TURNOFF");
		_log.debug("LOG_DEBUG_EXTENSION_WPGRPHFLTRPROCEED","Executing WPGraphicalFilterSendToQueryBuilder",100L);				
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		String uniqueId = state.getInteractionId();
		//Get temp table records corresponding to criteria selections the usr has made
		//
		//Transfer these to the query builder's temp table
		Query tempTableQry = new Query("wp_graphicalfilter_temp","wp_graphicalfilter_temp.INTERACTIONID = '"+state.getInteractionId()+"' AND wp_graphicalfilter_temp.ISSELECTED = 1","wp_graphicalfilter_temp.SERIALKEY");
		BioCollectionBean tempTableRecords = uow.getBioCollectionBean(tempTableQry);		
		try {			
			for(int i = 0; i < tempTableRecords.size(); i++){
				Bio tempTableRecord = tempTableRecords.elementAt(i);
				String filterType = (String)tempTableRecord.get("FILTERTYPE");
				String filterValue = (String)tempTableRecord.get("ADDITIONALFILTER");
				QBEBioBean newQueryBuilderRecord = uow.getQBEBioWithDefaults("wp_querybuilderdetail");
				tempTableQry = new Query("wp_graphicalfilter_temp","wp_graphicalfilter_temp.FILTERTYPE = '"+filterType+"'","");
				BioCollection collectionOfEqualTypes = tempTableRecords.filter(tempTableQry);
				if(collectionOfEqualTypes != null && collectionOfEqualTypes.size() > 1){
					newQueryBuilderRecord.set("OPERATOR", "IN");
					filterValue = (String)collectionOfEqualTypes.elementAt(0).get("ADDITIONALFILTER");
					for(int j = 1; j < collectionOfEqualTypes.size(); j++){
						filterValue += ","+(String)collectionOfEqualTypes.elementAt(j).get("ADDITIONALFILTER");
					}
					tempTableRecords.filterInPlace(new Query("wp_graphicalfilter_temp","wp_graphicalfilter_temp.FILTERTYPE != '"+filterType+"'","wp_graphicalfilter_temp.SERIALKEY"));
					i--;
				}			
				else{
					if(WPGraphFilterUtil.columnIsDate(filterType)){
						System.out.println("***** it is date ******");
						newQueryBuilderRecord.set("OPERATOR", "BETWEEN");

						
						DateFormat dateFormatShort = DateFormat.getDateInstance(DateFormat.SHORT);  
						Date firstValueDate = new java.text.SimpleDateFormat(WavePlanningUtils.dbDateFormat).parse(filterValue);
						java.util.Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
						cal.setTime(firstValueDate);
						cal.add(Calendar.DATE,1);
						newQueryBuilderRecord.set("SECONDVALUE", WPUtil.getDBDependentDate(cal.getTime()));
						
					}else{
						newQueryBuilderRecord.set("OPERATOR", "=");
					}
				}
				newQueryBuilderRecord.set("INTERACTIONID", uniqueId);				
				newQueryBuilderRecord.set("FIRSTVALUE", filterValue);		
				newQueryBuilderRecord.set("ORDERFIELD", filterType);					
				newQueryBuilderRecord.set("ANDOR", "AND");				
				newQueryBuilderRecord.save();
			}			
			tempTableQry = new Query("wp_graphicalfilter_temp","wp_graphicalfilter_temp.INTERACTIONID = '"+state.getInteractionId()+"' AND wp_graphicalfilter_temp.DODISPLAY = 1 AND wp_graphicalfilter_temp.LABEL = '"+tooltip+"'","wp_graphicalfilter_temp.SERIALKEY");			
			tempTableRecords = uow.getBioCollectionBean(tempTableQry);
			
			QBEBioBean newQueryBuilderRecord = uow.getQBEBioWithDefaults("wp_querybuilderdetail");			
			String filterType = (String)tempTableRecords.elementAt(0).get("FILTERTYPE");
			String filterValue = (String)tempTableRecords.elementAt(0).get("ADDITIONALFILTER");
			if(tempTableRecords != null && tempTableRecords.size() > 1){
				newQueryBuilderRecord.set("OPERATOR", "IN");				
				for(int j = 1; j < tempTableRecords.size(); j++){
					filterValue += ","+(String)tempTableRecords.elementAt(j).get("ADDITIONALFILTER");
				}				
			}			
			else{
				if(WPGraphFilterUtil.columnIsDate(filterType)){
					newQueryBuilderRecord.set("OPERATOR", "BETWEEN");

					
					DateFormat dateFormatShort = DateFormat.getDateInstance(DateFormat.SHORT);  
					Date firstValueDate = new java.text.SimpleDateFormat(WavePlanningUtils.dbDateFormat).parse(filterValue);
					java.util.Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
					cal.setTime(firstValueDate);
					cal.add(Calendar.DATE,1);
					newQueryBuilderRecord.set("SECONDVALUE", WPUtil.getDBDependentDate(cal.getTime()));
					
				}else{
					newQueryBuilderRecord.set("OPERATOR", "=");
				}
			}
			newQueryBuilderRecord.set("INTERACTIONID", uniqueId);			
			newQueryBuilderRecord.set("FIRSTVALUE", filterValue);		
			newQueryBuilderRecord.set("ORDERFIELD", filterType);					
			newQueryBuilderRecord.set("ANDOR", "AND");				
			newQueryBuilderRecord.save();
					
			newQueryBuilderRecord.save();
		} catch (EpiDataException e1) {
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while saving...",100L);
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e1.getStackTraceAsString(),100L);
			e1.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} catch (DataBeanException e1) {
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while saving...",100L);
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e1.getStackTraceAsString(),100L);
			e1.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}catch(ParseException parEx){
			parEx.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
			
		}
		
		try {
			uow.saveUOW(true);
		} catch (EpiException e) {
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE","Error occured while saving...",100L);
			_log.error("LOG_DEBUG_EXTENSION_WPQRYBLDRREMOVE",e.getStackTraceAsString(),100L);
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WPEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		
		//Place interaction ID in session so Query Builder screen can find the newly inserted records.
		//state.getRequest().getSession().setAttribute(WPGraphicalFilterSendToQueryBuilder.SESSION_KEY_GRAPHICAL_FILTER_RECORDS_KEY, uniqueId);
		return RET_CONTINUE;	

}

}