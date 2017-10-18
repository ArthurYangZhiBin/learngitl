package com.ssaglobal.scm.wms.wm_automove.ui;

import java.util.Iterator;
import java.util.Set;

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.bio.BioAttributeMetadata;
import com.epiphany.shr.data.bio.BioMetadata;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.metadata.Metadata;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.metadata.objects.bio.BioType;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.GUIDFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.model.data.BioCollectionBean;

public class AMStrategySave extends SaveAction{
    protected static ILoggerCategory _log = LoggerFactory.getInstance(AMStrategySave.class);

    public AMStrategySave() { 
        _log.info("EXP_1","HeaderDetailSave Instantiated!!!",  SuggestedCategory.NONE);
    }
	protected int execute(ActionContext context, ActionResult result) throws UserException {	
		//Get user entered criteria
	String shellSlot1 = "list_slot_1";
	String shellSlot2 = "list_slot_2";
	String toggleFormSlot = "wm_automove_strategydetail_toggle_slot";
	String detailBiocollection = "AMSTRATEGYDETAIL";
	String detailFormTab = "automovedetail_normal";
		StateInterface state = context.getState();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		 
		//get header data
		SlotInterface headerSlot = shellForm.getSubSlot(shellSlot1);		
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		if(headerForm.isListForm()){
	    	String [] desc = new String[1];
	    	desc[0] = "";
	    	throw new UserException("List_Save_Error",desc);			
		}
		DataBean headerFocus = headerForm.getFocus();
		//get detail data
		SlotInterface detailSlot = shellForm.getSubSlot(shellSlot2);		
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);

		BioBean headerBioBean = null;
		if (headerFocus.isTempBio()) {//it is for insert header
		    try
			{
				headerBioBean = uow.getNewBio((QBEBioBean)headerFocus);
				setAuditGUID(headerBioBean);
				
			} catch (EpiException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				_log.error("LOG_ERROR_EXTENSION_HeaderDetailSave", e.getStackTraceAsString(), SuggestedCategory.NONE);
				throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
			}
		    
			DataBean detailFocus = detailForm.getFocus();
			if(detailFocus != null){
				headerBioBean.addBioCollectionLink(detailBiocollection, (QBEBioBean)detailFocus);		
				setAuditGUID(detailBiocollection, headerBioBean, detailFocus, uow);
			}
			
		} else {//it is for update header
		    headerBioBean = (BioBean)headerFocus;
			SlotInterface toggleSlot = detailForm.getSubSlot(toggleFormSlot);						
			RuntimeFormInterface detailTab = state.getRuntimeForm(toggleSlot, detailFormTab);		
			DataBean detailFocus = detailTab.getFocus();
			
			if (detailFocus != null && detailFocus.isTempBio()) {
			    headerBioBean.addBioCollectionLink(detailBiocollection, (QBEBioBean)detailFocus);		
			    setAuditGUID(detailBiocollection, headerBioBean, detailFocus, uow);

			} else {
				//if it is update detail
				try {
					if (((BioBean)detailFocus).hasBeenUpdated("LENGTHOFTIME")||((BioBean)detailFocus).hasBeenUpdated("ELEMENT") || 
							((BioBean)detailFocus).hasBeenUpdated("PUTAWAYSTRATEGYKEY") || ((BioBean)detailFocus).hasBeenUpdated("TASKTYPE")){
						String amStrategyKey = headerBioBean.getValue("AMSTRATEGYKEY").toString();
						Object stepNum = detailFocus.getValue("STEPNUMBER");
						Object amStrategyDetailKey = detailFocus.getValue("AMSTRATEGYDETAILKEY");

						String queryString = "wm_amstrategydetail.AMSTRATEGYKEY= '"+amStrategyKey+"' and wm_amstrategydetail.STEPNUMBER= '"+stepNum.toString()+"'";
						Query query = new Query("wm_amstrategydetail",queryString,null);

						BioCollectionBean amStrategyDetailCollection = uow.getBioCollectionBean(query);
						if (amStrategyDetailCollection.size()> 1 ){
							if (amStrategyDetailCollection.min("AMSTRATEGYDETAILKEY").toString().equalsIgnoreCase(amStrategyDetailKey.toString())){
								for(int index=0; index<amStrategyDetailCollection.size(); index++){
									BioBean amStrategyDetailBio = (BioBean)amStrategyDetailCollection.elementAt(index);
									amStrategyDetailBio.set("LENGTHOFTIME", detailFocus.getValue("LENGTHOFTIME"));
									amStrategyDetailBio.set("ELEMENT", detailFocus.getValue("ELEMENT"));
									amStrategyDetailBio.set("PUTAWAYSTRATEGYKEY", detailFocus.getValue("PUTAWAYSTRATEGYKEY"));
									amStrategyDetailBio.set("TASKTYPE", detailFocus.getValue("TASKTYPE"));
								}
							}
						}
					}	    	    	
				} catch(Exception e) {

					// Handle Exceptions 
					e.printStackTrace();
					return RET_CANCEL;          
				}
			}
		}
		try{
			uow.saveUOW(true);
		}catch (UnitOfWorkException e){
			e.printStackTrace();
			_log.error("LOG_ERROR_EXTENSION_HeaderDetailSave", e.getStackTraceAsString(), SuggestedCategory.NONE);
			_log.error("LOG_ERROR_EXTENSION_HeaderDetailSave", "IN UnitOfWorkException", SuggestedCategory.NONE);
			Throwable nested = ((UnitOfWorkException) e).findDeepestNestedException();
			if(nested != null)
			{
				_log.error("LOG_ERROR_EXTENSION_HeaderDetailSave", "Nested " + nested.getClass().getName(), SuggestedCategory.NONE);
				_log.error("LOG_ERROR_EXTENSION_HeaderDetailSave", "Message " + nested.getMessage(), SuggestedCategory.NONE);
			}
			if(nested instanceof ServiceObjectException)
			{
				String reasonCode = nested.getMessage();
				throwUserException(e, reasonCode, null);
			}
			else
			{
				throwUserException(e, "ERROR_SAVING_BIO", null);
			}

		} catch (EpiException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			_log.error("LOG_ERROR_EXTENSION_HeaderDetailSave", e.getStackTraceAsString(), SuggestedCategory.NONE);
			throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
		}
		uow.clearState();
	    result.setFocus(headerBioBean);
	return RET_CONTINUE;

}
	
	private void setAuditGUID(BioBean headerBioBean){
		BioType bioType = Metadata.getInstance().getBioType(headerBioBean.getTypeName());
        String tablename = bioType.getPrimaryKeyRecordSetType().getRecordSetPhysicalName();
        String guidField = tablename + "ID"; 
        try{
        	BioAttributeMetadata[] meta = headerBioBean.getBioAttributeMetadata();
        	for(int idx = 0; idx < meta.length; idx++){
                		
        		if (meta[idx].getName().equalsIgnoreCase(guidField)){
        			headerBioBean.set(meta[idx].getName(), GUIDFactory.getGUIDStatic());
        			break;
        		}
        	}
        }catch(Exception e){
        	_log.debug("LOG_SYSTEM_OUT","[HeaderDetailSave]Failed to setAuditGUID",100L);
        	e.printStackTrace();
        }
	}

	private void setAuditGUID(String bioAttrCollectionName, BioBean headerBioBean, 
			DataBean detailBioBean, UnitOfWorkBean uow){

        try{
        	BioAttributeMetadata[] meta = headerBioBean.getBioAttributeMetadata();
        	for(int idx = 0; idx < meta.length; idx++){
                		
        		if (meta[idx].getName().equalsIgnoreCase(bioAttrCollectionName)){
        			
        			String bioTypeName = meta[idx].getTargetBioType();
        			BioType bioType = Metadata.getInstance().getBioType(bioTypeName);
        	        String tablename = bioType.getPrimaryKeyRecordSetType().getRecordSetPhysicalName();

        	        String guidField = tablename + "ID";

        	        BioMetadata bioMeta = uow.getBioMetadata(bioTypeName);
        	        Set attributes = bioMeta.getAllAttributes();
        	        //Verify that GUID field exists in BIO detail
        	        for(Iterator itr = attributes.iterator(); itr.hasNext();){
        	        	
        	        	String attributeName = (String)itr.next();
        	        	
        	        	if (attributeName.equalsIgnoreCase(guidField)){
        	        	
            	        	QBEBioBean qbe = (QBEBioBean)detailBioBean;
                	        qbe.set(guidField.toUpperCase(), GUIDFactory.getGUIDStatic());
                	        break;
        	        	}

        	        	
        	        }//end for
        	        
        			break;
        		}//end if
        	}
        }catch(Exception e){
        	_log.debug("LOG_SYSTEM_OUT","[HeaderDetailSave]Failed to setAuditGUID",100L);
        	e.printStackTrace();
        }

	}

}
