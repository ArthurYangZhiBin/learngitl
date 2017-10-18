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

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.view.View;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.GenericEpnyStateImpl;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.EpiRuntimeException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.navigation.SetIntoUserContextAction;
import com.ssaglobal.scm.wms.util.UserUtil;


public class WSListFormPrerenderAction extends FormExtensionBase{
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WSListFormPrerenderAction.class);
	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) throws EpiException {
		String CONTEXT_FILTER = "HeaderDetailFilter";
		_log.debug("LOG_DEBUG_EXTENSION_WSLISTFORMPREREN","Executing WSListFormPrerenderAction",100L);		
		GenericEpnyStateImpl state = (GenericEpnyStateImpl)context.getState();			
		ArrayList  bioAttrNames = (ArrayList)getParameter("bioAttrNames");
		ArrayList bioAttrTypes = (ArrayList)getParameter("bioAttrTypes");		
		String formCode = (String)getParameter("formCode");
		//If this is not the first navigation here then reset fliter
		SetIntoUserContextAction.initSessionContextData(state);
		String lastAccessedForm = (String)state.getRequest().getSession().getAttribute("LAST_ACCESSED_FORM");		
		if(lastAccessedForm == null){
			state.getRequest().getSession().setAttribute("LAST_ACCESSED_FORM",form.getName());
			//Parameters are either invalid or are such that the developer want no action to take place
			if(bioAttrNames == null || bioAttrTypes == null || (bioAttrNames.size() != bioAttrTypes.size()) || bioAttrNames.size() == 0){
				_log.debug("LOG_DEBUG_EXTENSION_WSLISTFORMPREREN","Exiting Without Performing Any Actions...",100L);	
				_log.debug("LOG_DEBUG_EXTENSION_WSLISTFORMPREREN","Exiting WSListFormPrerenderAction",100L);			
				return RET_CONTINUE;
			}
			String uid = UserUtil.getUserId(state);
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();	
			Query loadBiosQry = null;
			String oldDbName = (String)state.getRequest().getSession().getAttribute(BuiildDefaultCache.DB_CONNECTION);
			
			if(formCode != null && formCode.length() > 0){
				_log.debug("LOG_DEBUG_EXTENSION_WSLISTFORMPREREN","Creating wsdefaultsscreens query",100L);
				if(oldDbName.equalsIgnoreCase("enterprise"))
					loadBiosQry = new Query("wsdefaultsscreens", "wsdefaultsscreens.USERID = '"+uid+"' AND wsdefaultsscreens.FORMNAME = '"+formCode.toUpperCase()+"' AND wsdefaultsscreens.ISENTERPRISE = '1'", null);
				else
					loadBiosQry = new Query("wsdefaultsscreens", "wsdefaultsscreens.USERID = '"+uid+"' AND wsdefaultsscreens.FORMNAME = '"+formCode.toUpperCase()+"' AND wsdefaultsscreens.ISENTERPRISE = '0'", null);										
				BioCollection defaultScreenCollection = uow.getBioCollectionBean(loadBiosQry);
				_log.debug("LOG_DEBUG_EXTENSION_WSLISTFORMPREREN","Got defaultScreenCollection...",100L);
				_log.debug("LOG_DEBUG_EXTENSION_WSLISTFORMPREREN","is pre-filter on?...",100L);
				if(defaultScreenCollection != null && defaultScreenCollection.size() > 0){
					if(defaultScreenCollection.elementAt(0).get("ISSELECTED") != null && defaultScreenCollection.elementAt(0).get("ISSELECTED").equals("0")){
						_log.debug("LOG_DEBUG_EXTENSION_WSLISTFORMPREREN","User Turned Off Prefilter, exiting...",100L);
						return RET_CONTINUE;
					}
				}
				_log.debug("LOG_DEBUG_EXTENSION_WSLISTFORMPREREN","did user set prefilter?...",100L);
				if(defaultScreenCollection == null || defaultScreenCollection.size() == 0 ){			
					_log.debug("LOG_DEBUG_EXTENSION_WSLISTFORMPREREN","User Default Screen Record Not Found, retrieving global...",100L);			
					loadBiosQry = new Query("wsdefaultsscreens", "wsdefaultsscreens.USERID = 'XXXXXXXXXX' AND wsdefaultsscreens.FORMNAME = '"+formCode.toUpperCase()+"' AND wsdefaultsscreens.ISSELECTED = '1'", null);
					defaultScreenCollection = uow.getBioCollectionBean(loadBiosQry);				
				}				
				if(defaultScreenCollection == null || defaultScreenCollection.size() == 0){
					_log.debug("LOG_DEBUG_EXTENSION_WSLISTFORMPREREN","Exiting Without Performing Any Actions, Default Screen Record Not Found...",100L);			
					_log.debug("LOG_DEBUG_EXTENSION_WSLISTFORMPREREN","Exiting WSListFormPrerenderAction",100L);
					return RET_CONTINUE;	}
			}
			
			
			_log.debug("LOG_DEBUG_EXTENSION_WSLISTFORMPREREN","is cache built?...",100L);
			if(!WSDefaultsUtil.isCacheAvaliable(state)){ 
				_log.debug("LOG_DEBUG_EXTENSION_WSLISTFORMPREREN","Exiting Without Performing Any Actions Default Record Not Found...",100L);			
				_log.debug("LOG_DEBUG_EXTENSION_WSLISTFORMPREREN","Exiting WSListFormPrerenderAction",100L);
				return RET_CONTINUE;
			}
			
			_log.debug("LOG_DEBUG_EXTENSION_WSLISTFORMPREREN","Got session variable LAST_ACCESSED_FORM:"+lastAccessedForm,100L);
			
			if(form.getFocus() != null && form.getFocus().isBioCollection()){			
				BioCollectionBean collection = (BioCollectionBean)form.getFocus();
				QBEBioBean bio = createNewQBE(state,collection.getBioTypeName());
				_log.debug("LOG_DEBUG_EXTENSION_WSLISTFORMPREREN","Created QBEBioBean of Type:"+collection.getBioTypeName()+"...",100L);			
				//iterate thru bio attr list and populate with defaults if a default is avaliable
				for(int i = 0; i < bioAttrNames.size(); i++){
					_log.debug("LOG_DEBUG_EXTENSION_WSLISTFORMPREREN","Populating bios With Defaults...",100L);				
					String bioAttrName = (String)bioAttrNames.get(i);
					String bioAttrType = (String)bioAttrTypes.get(i);
					//Is a default avaliable for this type?...
					if(WSDefaultsUtil.doesCacheHaveType(bioAttrType,state)){				
						String value = WSDefaultsUtil.getPreFilterValueByType(bioAttrType,state);
						_log.debug("LOG_DEBUG_EXTENSION_WSLISTFORMPREREN","Type "+bioAttrType+" Has Default Value "+value+" In Cache...",100L);					
						bio.set(bioAttrName,value);	
					}
					else{
						_log.debug("LOG_DEBUG_EXTENSION_WSLISTFORMPREREN","Type "+bioAttrType+" Has No Default Value In Cache...",100L);					
					}
					
				}		
				
				//If filter bio was not changed i.e. no values were in the cache for the columns on this screen then do not filter.
				if(bio.isEmpty()){
					_log.debug("LOG_DEBUG_EXTENSION_WSLISTFORMPREREN","No Filter Values Found For This Screen, Exiting...",100L);
					return RET_CONTINUE;
				}				
				
				boolean showEmptyList = getParameterBoolean("show empty list");			
				
				//The following is a modified version of a block of code found inside of the QueryAction Extension.
				//It is a little confusing but the steps seem to be necessary to pre-filter a biocollection properly
				//It seems if any are skipped then either the form render events get messed up or the pre-filter becomes unclearable
				BioCollectionBean newCollection = (BioCollectionBean)collection.filter(bio.getQueryWithWildcards());
				BioCollectionBean newCollectionB = (BioCollectionBean)collection.filter(bio.getQueryWithWildcards());		
				newCollectionB.copyFrom(collection);
				collection.copyFrom(newCollection);			
				newCollection = collection;
				bio.setBaseBioCollectionForQuery(newCollectionB);
				newCollection.setQBEBioBean(bio);						
				newCollection.filterInPlace(bio.getQueryWithWildcards());			
				if(showEmptyList){
					newCollection.setEmptyList(true);
				}
				else{
					newCollection.setEmptyList(false);
				}
				
				//Saving Filter into UserContext
				//Extension called from List Form - save the current filter into the usercontext
				_log.debug("LOG_DEBUG_EXTENSION_WSLISTFORMPREREN", "Saving Filter in the PreRender Extension", SuggestedCategory.NONE);
				CONTEXT_FILTER += context.getState().getInteractionId();
				_log.debug("LOG_DEBUG_EXTENSION_WSLISTFORMPREREN", "Context: " + CONTEXT_FILTER, SuggestedCategory.NONE);
				View vw = View.createView(bio.getQueryBioStub());
				String viewData = vw.getRefString();				
				EpnyUserContext userContext = context.getServiceManager().getUserContext();
				userContext.put(CONTEXT_FILTER, viewData);
				_log.debug("LOG_DEBUG_EXTENSION_WSLISTFORMPREREN", "Saving the Filter " + vw.toString() + ":" + viewData + " into context " + CONTEXT_FILTER, SuggestedCategory.NONE);
			}
		}
		_log.debug("LOG_DEBUG_EXTENSION_WSLISTFORMPREREN","Exiting WSListFormPrerenderAction",100L);	
		return RET_CONTINUE;
		
	}
	
	private QBEBioBean createNewQBE(StateInterface state, String bioType)
	{
		UnitOfWorkBean tempUowb = state.getDefaultUnitOfWork();
		QBEBioBean qbe;
		try
		{
			qbe = tempUowb.getQBEBio(bioType);
		}
		catch(DataBeanException ex)
		{
			Object args[] = {
					bioType
			};
			throw new EpiRuntimeException("EXP_INVALID_QUERY_TYPE_QACTION", "A QBE Bio could not be created for bio type {0}", args);
		}
		return qbe;
	}
}