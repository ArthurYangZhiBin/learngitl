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
package com.infor.scm.waveplanning.wp_query_builder.action;

//Import 3rd party packages and classes

//Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.GUIDFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.common.util.WPUserUtil;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.epiphany.shr.ui.model.data.BioBean;

public class WPQueryBuilderNewTempRecord extends SaveAction{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPQueryBuilderNewTempRecord.class);
//	public static final String SESSION_KEY_DEFAULT_FILTER_RECORDS_KEY = "wp.session.key.default.filter.records.key";
	public static final String HAS_DEFAULT_FILTER="HAS_DEFAULT_FILTER";
	protected int execute(ActionContext context, ActionResult result) throws UserException{
		_log.debug("LOG_DEBUG_EXTENSION_WPQRYBLDRSAVEQRY","Executing WPQueryBuilderNewTempRecord",100L);
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
//		Query qry = new Query("wp_filter","wp_filter.USERDEFAULT = 1 AND wp_filter.USERID = '"+WPUserUtil.getUserId(state)+"'","");
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		String wmWhseID = (userContext.get(SetIntoHttpSessionAction.DB_USERID)).toString();
		Query qry = new Query("wp_filter","wp_filter.USERDEFAULT = 1 AND wp_filter.FACILITY = '"+wmWhseID+"'","");
		BioCollection bc = uow.getBioCollectionBean(qry);
		try {
			if(bc != null && bc.size() > 0){
				String uniqueId = GUIDFactory.getGUIDStatic();
				Bio filterHeader =  bc.elementAt(0);
				BioCollection filterDetails = filterHeader.getBioCollection("FILTERDETAILS");
				if(filterDetails != null && filterDetails.size() > 0){
					for(int i = 0; i < filterDetails.size(); i++){
						Bio filterDetail = filterDetails.elementAt(i);
						QBEBioBean newTempRecord = uow.getQBEBioWithDefaults("wp_querybuilderdetail");
						newTempRecord.set("OPERATOR", filterDetail.get("OPERATOR"));
						newTempRecord.set("INTERACTIONID", uniqueId);
						newTempRecord.set("FIRSTVALUE", filterDetail.get("FIRSTVALUE"));
						newTempRecord.set("SECONDVALUE", filterDetail.get("SECONDVALUE"));
						newTempRecord.set("ORDERFIELD", filterDetail.get("ORDERFIELD"));
						newTempRecord.set("ANDOR", filterDetail.get("ANDOR"));
						newTempRecord.save();
					}
				}
				QBEBioBean newTempRecord = uow.getQBEBioWithDefaults("querybuildertemp");
				newTempRecord.set("INTERACTIONID", uniqueId);
				newTempRecord.set("ORDERSMAX", filterHeader.get("MAXORDERS"));
				newTempRecord.set("CUBEMAX", filterHeader.get("MAXCUBE"));
				newTempRecord.set("INCLUDERF", filterHeader.get("RFID_STND"));
				newTempRecord.set("ORDERLINESMAX", filterHeader.get("MAXORDERLINES"));
				newTempRecord.set("WEIGHTMAX", filterHeader.get("MAXWEIGHT"));
				newTempRecord.set("CASES", filterHeader.get("MAXCASES"));

				//08/18/2010 FW:  Added code to populate more fields for default filter (Incident3894961_Defect277724) -- Start
				newTempRecord.set("INCLUDEORDERS", filterHeader.get("INCLUDEORDERS"));
				Integer i = 0;
				i = (int)Double.parseDouble( filterHeader.get("TOTALQTYSTART")==null?"0":filterHeader.get("TOTALQTYSTART").toString());
				newTempRecord.set("TOTALQTYSTART", i.toString());
				i = (int)Double.parseDouble( filterHeader.get("TOTALQTYEND")==null?"0":filterHeader.get("TOTALQTYEND").toString());
				newTempRecord.set("TOTALQTYEND", i.toString());
				i = (int)Double.parseDouble( filterHeader.get("TOTALLINESSTART")==null?"0":filterHeader.get("TOTALLINESSTART").toString());
				newTempRecord.set("TOTALLINESSTART", i.toString());
				i = (int)Double.parseDouble( filterHeader.get("TOTALLINESEND")==null?"0":filterHeader.get("TOTALLINESEND").toString());
				newTempRecord.set("TOTALLINESEND", i.toString());
				i = (int)Double.parseDouble( filterHeader.get("TOTALCUBESTART")==null?"0":filterHeader.get("TOTALCUBESTART").toString());
				newTempRecord.set("TOTALCUBESTART", i.toString());
				i = (int)Double.parseDouble( filterHeader.get("TOTALCUBEEND")==null?"0":filterHeader.get("TOTALCUBEEND").toString());
				newTempRecord.set("TOTALCUBEEND", i.toString());
				i = (int)Double.parseDouble( filterHeader.get("TOTALWEIGHTSTART")==null?"0":filterHeader.get("TOTALWEIGHTSTART").toString());
				newTempRecord.set("TOTALWEIGHTSTART", i.toString());
				i = (int)Double.parseDouble( filterHeader.get("TOTALWEIGHTEND")==null?"0":filterHeader.get("TOTALWEIGHTEND").toString());
				newTempRecord.set("TOTALWEIGHTEND", i.toString());
				//08/18/2010 FW:  Added code to populate more fields for default filter (Incident3894961_Defect277724) -- End

				newTempRecord.save();
				try {
					uow.saveUOW(true);
					uow.clearState();
				} catch (EpiException e) {
					e.printStackTrace();
					String args[] = new String[0];
					String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
				qry = new Query("querybuildertemp","querybuildertemp.INTERACTIONID = '"+uniqueId+"' ","");

/*				BioCollectionBean tempRecords = uow.getBioCollectionBean(qry);
				BioRef focusRef = tempRecords.elementAt(0).getBioRef();
				result.setFocus(uow.getBioBean(focusRef));
*/
				BioBean newFocus = uow.getBioCollectionBean(qry).get("0");
				result.setFocus(newFocus);
//				state.getRequest().getSession().setAttribute(SESSION_KEY_DEFAULT_FILTER_RECORDS_KEY, uniqueId);
				state.getRequest().getSession().setAttribute(HAS_DEFAULT_FILTER,uniqueId);
			}
			else{
				result.setFocus(uow.getQBEBioWithDefaults("querybuildertemp"));
				state.getRequest().getSession().setAttribute(HAS_DEFAULT_FILTER,null);
						}
		} catch (EpiDataException e) {
			e.printStackTrace();
			String args[] = new String[0];
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} catch (DataBeanException e) {
			e.printStackTrace();
			String args[] = new String[0];
			String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		return RET_CONTINUE;
	}
}