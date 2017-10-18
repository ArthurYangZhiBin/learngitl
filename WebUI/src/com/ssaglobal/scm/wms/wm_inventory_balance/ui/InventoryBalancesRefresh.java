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
import java.util.ArrayList;
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
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;


public class InventoryBalancesRefresh extends ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(InventoryBalancesRefresh.class);
	public static String DB_CONNECTION = "dbConnectionName";
	public static String DB_USERID =	"dbUserName";
	public static String DB_PASSWORD =	"dbPassword";
	public static String DB_DATABASE = "dbDatabase";
	public static String DB_ISENTERPRISE = "dbIsEnterprise";
	
	public static final TextData TEST_DATA_LOC = new TextData("Loc");
	public static final TextData TEST_DATA_STS = new TextData("Status");
	public static final TextData TEST_DATA_COH = new TextData("CubeOnHand");
	public static final TextData TEST_DATA_LCC = new TextData("LocCubeCapacity");
	public static final TextData TEST_DATA_QTY = new TextData("Qty");
	
	private static int OFFSET = 0;
	
	protected int execute(ActionContext context, ActionResult result) throws UserException,EpiException{		
		StateInterface state = context.getState();
		ArrayList tabs = new ArrayList();
		tabs.add("wm_inventory_balance_data_tab");
		
		_log.debug("LOG_DEBUG_EXTENSION_INVBALREF","jpdebug:calling buildFocusFromQuery", 100L);
		
		BioCollectionBean bcb = buildFocusFromQuery(state, state.getTempUnitOfWork());
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
	
	public BioCollectionBean buildFocusFromQuery(StateInterface state, UnitOfWorkBean uowb)
		throws UserException, EpiDataException{
		String qry = "";
		HttpSession session = state.getRequest().getSession();
		ArrayListBioRefSupplier tempBioCollRefArray = new ArrayListBioRefSupplier("wm_multifacbal_ib");
		//HelperBio helper = state.getDefaultUnitOfWork().getUOW().createHelperBio("wm_multifacbal_ib");  
		HelperBio helper = uowb.getUOW().createHelperBio("wm_multifacbal_ib");
		//qry = (String)((HashMap)session.getAttribute(context.getState().getInteractionId())).get("INV_BAL_LOC_QRY");		
		qry = (String)((HashMap)session.getAttribute(state.getInteractionId())).get("INV_BAL_LOC_QRY");
		
		_log.debug("LOG_DEBUG_EXTENSION_INVBALREF","jpdebug:query:"+qry, 100L);
		if(qry != null){                
			try {
				EXEDataObject collection = WmsWebuiValidationSelectImpl.select(qry);
				_log.debug("LOG_DEBUG_EXTENSION_INVBALREF","Building Temp Bio Collection of size 2:"+collection.getRowCount(),100L);
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

					dataObjectToBio(collection, TEST_DATA_LOC, bio, "LOCATION");
					dataObjectToBio(collection, TEST_DATA_STS, bio, "STATUS");
					dataObjectToBio(collection, TEST_DATA_COH, bio, "CUBEONHAND");
					dataObjectToBio(collection, TEST_DATA_LCC, bio, "CUBECAPACITY");
					dataObjectToBio(collection, TEST_DATA_QTY, bio, "QTY");
					
					tempBioCollRefArray.add(bio.getBioRef());
					collection.getNextRow();
				}
			} catch (EpiDataException e) {
				e.printStackTrace();				
			} 
			try {
				//BioCollectionEpistub tempBioColl = (BioCollectionEpistub)state.getDefaultUnitOfWork().getUOW().fetchBioCollection(tempBioCollRefArray);
				BioCollectionEpistub tempBioColl = (BioCollectionEpistub)uowb.getUOW().fetchBioCollection(tempBioCollRefArray);
				_log.debug("LOG_DEBUG_EXTENSION_INVBALREF","Bio Coll:"+tempBioColl,100L);				
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