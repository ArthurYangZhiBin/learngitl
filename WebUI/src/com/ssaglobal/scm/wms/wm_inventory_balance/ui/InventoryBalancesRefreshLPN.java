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
package com.ssaglobal.scm.wms.wm_inventory_balance.ui;
//import com.epiphany.common.shared.extension.helper.ApplicationUtil;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.beans.ejb.BioCollectionEpistub;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.HelperBio;
import com.epiphany.shr.data.bio.impl.ArrayListBioRefSupplier;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;


public class InventoryBalancesRefreshLPN extends ActionExtensionBase{	
	public static String DB_CONNECTION = "dbConnectionName";
	public static String DB_USERID =	"dbUserName";
	public static String DB_PASSWORD =	"dbPassword";
	public static String DB_DATABASE = "dbDatabase";
	public static String DB_ISENTERPRISE = "dbIsEnterprise";
	
	public static final TextData TEST_DATA_LPN = new TextData("Id");
	public static final TextData TEST_DATA_STS = new TextData("Status");
	public static final TextData TEST_DATA_PACK = new TextData("Packkey");
	public static final TextData TEST_DATA_TI = new TextData("PutawayTI");
	public static final TextData TEST_DATA_HI = new TextData("PutawayHI");
	public static final TextData TEST_DATA_QTY = new TextData("qty");
	public static final String TABLE_ID = "ID";
	public static final String TABLE_PACK = "Pack";
	public static final String DB_COL_LPN = "Id";
	public static final String DB_COL_STS = "Status";
	public static final String DB_COL_PACK = "Packkey";
	public static final String DB_COL_TI = "PalletTI";
	public static final String DB_COL_HI = "PalletHI";
	public static final String DB_COL_QTY = "qty";
	
	private static int OFFSET = 0;	
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(InventoryBalancesRefreshLPN.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException,EpiException{
		_log.debug("LOG_DEBUG_EXTENSION_INVBALREFLPN","Executing InventoryBalancesRefreshLPN",100L);		
		boolean doSort = getParameterBoolean("doSort");
		_log.debug("LOG_DEBUG_EXTENSION_INVBALREFLPN","doSort:"+doSort,100L);		
		_log.debug("LOG_DEBUG_EXTENSION_INVBALREFLPN","Exiting InventoryBalancesRefreshLPN",100L);
		
		StateInterface state = context.getState();       
		BioCollectionBean bcb = buildFocusFromQuery(state, state.getTempUnitOfWork(), doSort);
		if(bcb!=null){
			BioCollectionBean focus = (BioCollectionBean) result.getFocus();
			focus.copyFrom(bcb);
		}
		return RET_CONTINUE;
	}
	
	private void dataObjectToBio(EXEDataObject collection, TextData collAttr, Bio bio, String bioAttr) {
		Object attrValue = collection.getAttribValue(collAttr);
		if(attrValue != null)
			try {
				bio.set(bioAttr, attrValue.toString());
			} catch (EpiDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	protected String getSortOrder(StateInterface state){
		String colName = state.getBucketValueString("sortColName");
        String oldColName = state.getBucketValueString("sortOldColName");
        String sortOrder = null;
        if(colName.equalsIgnoreCase(oldColName))
        {
            sortOrder = state.getBucketValueString("sortOrder");
        } else
        {
            sortOrder = (String)state.getCurrentRuntimeForm().getProperty("sorting");
            if(sortOrder.equalsIgnoreCase("No Sort"))
                sortOrder = "NOSORT";
            else
            if(sortOrder.equalsIgnoreCase("Ascending First"))
                sortOrder = "ASC";
            else
            if(sortOrder.equalsIgnoreCase("Descending First"))
                sortOrder = "DESC";
        }
        return sortOrder;
	}
	
	private String getTableForColumn(String col){
		String table = "";
		if(col.equalsIgnoreCase(TEST_DATA_HI.getAsString()) || col.equalsIgnoreCase(TEST_DATA_TI.getAsString()))
			return TABLE_PACK;		
		else
			return TABLE_ID;
	}
	
	private String getDBColumn(String col){
		if(col.equalsIgnoreCase(TEST_DATA_HI.getAsString()))
			return DB_COL_HI;
		else if(col.equalsIgnoreCase(TEST_DATA_LPN.getAsString()))
			return DB_COL_LPN;
		else if(col.equalsIgnoreCase(TEST_DATA_PACK.getAsString()))
			return DB_COL_PACK;
		else if(col.equalsIgnoreCase(TEST_DATA_QTY.getAsString()))
			return DB_COL_QTY;
		else if(col.equalsIgnoreCase(TEST_DATA_STS.getAsString()))
			return DB_COL_STS;
		else if(col.equalsIgnoreCase(TEST_DATA_TI.getAsString()))
			return DB_COL_TI;
		else
			return "";
	}
	
	
	public BioCollectionBean buildFocusFromQuery(StateInterface state, UnitOfWorkBean uowb, boolean doSort)
		throws UserException, EpiDataException{
		
		String qry = "";
		HttpSession session = state.getRequest().getSession();
		ArrayListBioRefSupplier tempBioCollRefArray = new ArrayListBioRefSupplier("wm_multifacbal_ib");
		//HelperBio helper = state.getDefaultUnitOfWork().getUOW().createHelperBio("wm_multifacbal_ib");  
		HelperBio helper = uowb.getUOW().createHelperBio("wm_multifacbal_ib");
		//qry = (String)((HashMap)session.getAttribute(context.getState().getInteractionId())).get("INV_BAL_LPN_QRY");		
		qry = (String)((HashMap)session.getAttribute(state.getInteractionId())).get("INV_BAL_LPN_QRY");
		if(doSort){
			String sortOrder = getSortOrder(state);
			if(sortOrder != null && sortOrder.length() > 0){
				RuntimeListForm form = (RuntimeListForm)state.getCurrentRuntimeForm();
				String colName = state.getBucketValueString("sortColName");
				String oldColName = state.getBucketValueString("sortOldColName");
				form.setBooleanProperty("listSortDone", true);
                form.setBooleanProperty("showSortImage", true);
                if(sortOrder.equalsIgnoreCase("ASC"))
                    state.setBucketValue("sortOrder", "DESC");
                else
                if(sortOrder.equalsIgnoreCase("DESC"))
                    state.setBucketValue("sortOrder", "ASC");
                state.setBucketValue("sortOldColName", colName);
                String qryForProcessing = qry.toUpperCase();
                String orderBy = getTableForColumn(colName)+"."+getDBColumn(colName);
                _log.debug("LOG_DEBUG_EXTENSION_INVBALREFLPN","Initial Qry:"+qry,100L);
                //Remove Old Order By...
                if(qryForProcessing.indexOf("ORDER BY") != -1){
                	String[] splitQryOnOrderBy = qryForProcessing.split("ORDER BY");
                	if(splitQryOnOrderBy.length == 2){
                		if(splitQryOnOrderBy[1].indexOf(oldColName) != -1){
                			if(splitQryOnOrderBy[1].indexOf(oldColName+" ASC") != -1){
                				int startIdx = splitQryOnOrderBy[0].length()+8+splitQryOnOrderBy[1].indexOf(oldColName+" ASC") - 1;
                				int endIdx = startIdx + oldColName.length() + 4;
                				qry = qry.substring(0,startIdx)+qry.substring(endIdx,qry.length() - 1);
                			}
                			else if(splitQryOnOrderBy[1].indexOf(oldColName+" DESC") != -1){
                				int startIdx = splitQryOnOrderBy[0].length()+8+splitQryOnOrderBy[1].indexOf(oldColName+" ASC") - 1;
                				int endIdx = startIdx + oldColName.length() + 5;
                				qry = qry.substring(0,startIdx)+qry.substring(endIdx,qry.length() - 1);
                			}
                			else{
                				int startIdx = splitQryOnOrderBy[0].length()+8+splitQryOnOrderBy[1].indexOf(oldColName+" ASC") - 1;
                				int endIdx = startIdx + oldColName.length();
                				qry = qry.substring(0,startIdx)+qry.substring(endIdx,qry.length() - 1);
                			}
                		}
                	}
                }
                _log.debug("LOG_DEBUG_EXTENSION_INVBALREFLPN","Qry After Remove Order By:"+qry,100L);                
                //Append New Order By...
                if(qryForProcessing.indexOf("ORDER BY") == -1){
                	qry += " ORDER BY "+orderBy+" "+sortOrder;
                }
                else{                
                	qry += orderBy+" "+sortOrder;                	
                }
                _log.debug("LOG_DEBUG_EXTENSION_INVBALREFLPN","final Qry:"+qry,100L);                
			}
		}
		if(qry != null){			
			try {
				EXEDataObject collection = WmsWebuiValidationSelectImpl.select(qry);
				_log.debug("LOG_DEBUG_EXTENSION_INVBALREFLPN","Building Temp Bio Collection of size 2:"+collection.getRowCount(),100L);				
				Bio bio = null;
				
				//jp.7050.begin
				OFFSET += collection.getRowCount();
				//jp.7050.end

				for(int i = 0; i < collection.getRowCount(); i++){
					//bio = state.getDefaultUnitOfWork().getUOW().createBio(helper);
					bio = uowb.getUOW().createBio(helper);
					
					//jp.7050.begin
					bio.set("SERIALKEY",new Integer(i + OFFSET));
					//jp.7050.end
					
					dataObjectToBio(collection, TEST_DATA_LPN, bio, "LPN");
					dataObjectToBio(collection, TEST_DATA_STS, bio, "STATUS");
					dataObjectToBio(collection, TEST_DATA_PACK, bio, "PACK");
					dataObjectToBio(collection, TEST_DATA_TI, bio, "TI");
					dataObjectToBio(collection, TEST_DATA_HI, bio, "HI");
					dataObjectToBio(collection, TEST_DATA_QTY, bio, "ONHAND");
					
					tempBioCollRefArray.add(bio.getBioRef());
					collection.getNextRow();
				}
			} catch (EpiDataException e) {
				e.printStackTrace();				
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			} 
			try {
				//BioCollectionEpistub tempBioColl = (BioCollectionEpistub)state.getDefaultUnitOfWork().getUOW().fetchBioCollection(tempBioCollRefArray);
				BioCollectionEpistub tempBioColl = (BioCollectionEpistub)uowb.getUOW().fetchBioCollection(tempBioCollRefArray);
				_log.debug("LOG_DEBUG_EXTENSION_INVBALREFLPN","Bio Coll:"+tempBioColl,100L);				
				//BioCollectionBean bcb = state.getDefaultUnitOfWork().getBioCollection(tempBioColl.getBioCollectionRef());
				BioCollectionBean bcb = uowb.getBioCollection(tempBioColl.getBioCollectionRef());
				//BioCollectionBean focus = (BioCollectionBean) result.getFocus();
				//focus.copyFrom(bcb);
				return bcb;
			} catch (EpiDataException e) {
				e.printStackTrace();
				String args[] = new String[0]; 
				String errorMsg = getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
				throw new UserException(errorMsg,new Object[0]);
			}
		}
		
		return null;

	}
}