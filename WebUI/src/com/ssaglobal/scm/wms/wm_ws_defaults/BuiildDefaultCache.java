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
package com.ssaglobal.scm.wms.wm_ws_defaults;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.dbl.DBLQueryString;
import com.epiphany.shr.extensions.ExtensionBaseclass;
import com.epiphany.shr.metadata.objects.Navigation;
import com.epiphany.shr.metadata.objects.Perspective;
import com.epiphany.shr.metadata.objects.bio.BioType;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.element.Bucket;
import com.epiphany.shr.ui.element.UIBeanBucket;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.GenericEpnyStateImpl;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.taggen.FormGen;
import com.epiphany.shr.ui.view.RuntimeForm;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.EpiRuntimeException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.UserUtil;


public class BuiildDefaultCache extends ActionExtensionBase{
	public static String DB_CONNECTION = "dbConnectionName";
	protected int execute(ActionContext context, ActionResult result)
	throws UserException, EpiDataException
	{		
		StateInterface state = context.getState();
		HttpSession session = state.getRequest().getSession();
		String dbName = (String)session.getAttribute(DB_CONNECTION);
		if(dbName != null && dbName.length() > 0 ){
			if(!dbName.equalsIgnoreCase("enterprise")){
				String uid = UserUtil.getUserId(state);	
				Query loadBiosQry = new Query("wsdefaults", "wsdefaults.USERID = '"+uid+"' AND wsdefaults.ISENTERPRISE = '0'", null);		
				UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();	
				uow.clearState();
				BioCollection userDefaultCollection = uow.getBioCollectionBean(loadBiosQry);
				Bio userDefault = userDefaultCollection == null || userDefaultCollection.size() == 0?null:userDefaultCollection.elementAt(0);
				loadBiosQry = new Query("wsdefaultsdetail", "wsdefaultsdetail.USERID = '"+uid+"' AND wsdefaultsdetail.ISSELECTED = '1' AND wsdefaultsdetail.ISENTERPRISE = '0'", null);	
				BioCollection userFilterDefaultCollection = uow.getBioCollectionBean(loadBiosQry);
				loadBiosQry = new Query("wsdefaults", "wsdefaults.USERID = 'XXXXXXXXXX' ", null);	
				BioCollection globalDefaultCollection = uow.getBioCollectionBean(loadBiosQry);
				Bio globalDefault = globalDefaultCollection == null || globalDefaultCollection.size() == 0?null:globalDefaultCollection.elementAt(0);
				loadBiosQry = new Query("wsdefaultsdetail", "wsdefaultsdetail.USERID = 'XXXXXXXXXX' ", null);	
				BioCollection globalFilterDefaultCollection = uow.getBioCollectionBean(loadBiosQry);
				loadBiosQry = new Query("wsdefaultsdetail", "wsdefaultsdetail.USERID = '"+uid+"' AND wsdefaultsdetail.TYPE = 'STORER' AND wsdefaultsdetail.ISENTERPRISE = '0'", null);	
				BioCollection lockedOwnerCollection = uow.getBioCollectionBean(loadBiosQry);
				
				loadBiosQry = new Query("wsdefaultsdetail", "wsdefaultsdetail.USERID = '"+uid+"' AND wsdefaultsdetail.TYPE = 'CUSTOM' AND wsdefaultsdetail.ISENTERPRISE = '0'", null);	
				BioCollection lockedCustomerCollection = uow.getBioCollectionBean(loadBiosQry);
				
				loadBiosQry = new Query("wsdefaultsdetail", "wsdefaultsdetail.USERID = '"+uid+"' AND wsdefaultsdetail.TYPE = 'BILLTO' AND wsdefaultsdetail.ISENTERPRISE = '0'", null);	
				BioCollection lockedBillToCollection = uow.getBioCollectionBean(loadBiosQry);
				
				loadBiosQry = new Query("wsdefaultsdetail", "wsdefaultsdetail.USERID = '"+uid+"' AND wsdefaultsdetail.TYPE = 'CARRIER' AND wsdefaultsdetail.ISENTERPRISE = '0'", null);	
				BioCollection lockedCarrierCollection = uow.getBioCollectionBean(loadBiosQry);
				
				loadBiosQry = new Query("wsdefaultsdetail", "wsdefaultsdetail.USERID = '"+uid+"' AND wsdefaultsdetail.TYPE = 'VENDOR' AND wsdefaultsdetail.ISENTERPRISE = '0'", null);	
				BioCollection lockedVendorCollection = uow.getBioCollectionBean(loadBiosQry);
				WSDefaultsUtil.buildCache(state,globalFilterDefaultCollection,userFilterDefaultCollection,userDefault,globalDefault,lockedOwnerCollection,lockedCustomerCollection,lockedCarrierCollection,lockedBillToCollection,lockedVendorCollection);
			}
			else{
				String uid = UserUtil.getUserId(state);	
				Query loadBiosQry = new Query("wsdefaults", "wsdefaults.USERID = '"+uid+"' AND wsdefaults.ISENTERPRISE = '1'", null);		
				UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();									
				BioCollection userDefaultCollection = uow.getBioCollectionBean(loadBiosQry);
				Bio userDefault = userDefaultCollection == null || userDefaultCollection.size() == 0?null:userDefaultCollection.elementAt(0);
				loadBiosQry = new Query("wsdefaultsdetail", "wsdefaultsdetail.USERID = '"+uid+"' AND wsdefaultsdetail.ISSELECTED = '1' AND wsdefaultsdetail.ISENTERPRISE = '1'", null);	
				BioCollection userFilterDefaultCollection = uow.getBioCollectionBean(loadBiosQry);
				loadBiosQry = new Query("wsdefaults", "wsdefaults.USERID = 'XXXXXXXXXX' ", null);	
				BioCollection globalDefaultCollection = uow.getBioCollectionBean(loadBiosQry);
				Bio globalDefault = globalDefaultCollection == null || globalDefaultCollection.size() == 0?null:globalDefaultCollection.elementAt(0);
				loadBiosQry = new Query("wsdefaultsdetail", "wsdefaultsdetail.USERID = 'XXXXXXXXXX' ", null);	
				BioCollection globalFilterDefaultCollection = uow.getBioCollectionBean(loadBiosQry);
				loadBiosQry = new Query("wsdefaultsdetail", "wsdefaultsdetail.USERID = '"+uid+"' AND wsdefaultsdetail.TYPE = 'STORER' AND wsdefaultsdetail.ISENTERPRISE = '1'", null);	
				BioCollection lockedOwnerCollection = uow.getBioCollectionBean(loadBiosQry);
				
				loadBiosQry = new Query("wsdefaultsdetail", "wsdefaultsdetail.USERID = '"+uid+"' AND wsdefaultsdetail.TYPE = 'CUSTOM' AND wsdefaultsdetail.ISENTERPRISE = '1'", null);	
				BioCollection lockedCustomerCollection = uow.getBioCollectionBean(loadBiosQry);
				
				loadBiosQry = new Query("wsdefaultsdetail", "wsdefaultsdetail.USERID = '"+uid+"' AND wsdefaultsdetail.TYPE = 'BILLTO' AND wsdefaultsdetail.ISENTERPRISE = '1'", null);	
				BioCollection lockedBillToCollection = uow.getBioCollectionBean(loadBiosQry);
				
				loadBiosQry = new Query("wsdefaultsdetail", "wsdefaultsdetail.USERID = '"+uid+"' AND wsdefaultsdetail.TYPE = 'CARRIER' AND wsdefaultsdetail.ISENTERPRISE = '1'", null);	
				BioCollection lockedCarrierCollection = uow.getBioCollectionBean(loadBiosQry);
				
				loadBiosQry = new Query("wsdefaultsdetail", "wsdefaultsdetail.USERID = '"+uid+"' AND wsdefaultsdetail.TYPE = 'VENDOR' AND wsdefaultsdetail.ISENTERPRISE = '1'", null);	
				BioCollection lockedVendorCollection = uow.getBioCollectionBean(loadBiosQry);
				
				WSDefaultsUtil.buildCache(state,globalFilterDefaultCollection,userFilterDefaultCollection,userDefault,globalDefault,lockedOwnerCollection,lockedCustomerCollection,lockedCarrierCollection,lockedBillToCollection,lockedVendorCollection);
			}
		}
		else{
			WSDefaultsUtil.destroyCache(state);
		}		
		return RET_CONTINUE;
	}
	
}