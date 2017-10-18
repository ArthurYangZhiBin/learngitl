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

package com.ssaglobal.scm.wms.wm_audit.attributeDomain;

import java.util.List;

import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.extensions.OAGlobalAudit;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.Metadata;
import com.epiphany.shr.metadata.objects.generated.np.Label;
import com.epiphany.shr.metadata.objects.generated.np.LabelFactory;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.metadata.AttributeDomainExtensionBase;
import com.epiphany.shr.ui.metadata.DropdownContentsContext;
import com.epiphany.shr.ui.model.UserInterface;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class AuditBioAttrLabels extends AttributeDomainExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(AuditBioAttrLabels.class);
	
	@Override
	protected int execute(DropdownContentsContext context, List value, List labels) throws EpiException {			
		_log.debug("LOG_DEBUG_EXTENSION_COMPDOMCODELKUP","Executing ComputedDomCodeLkup",100L);
						
		StateInterface state = context.getState();		
		DataBean focus = state.getCurrentRuntimeForm().getFocus();		
		String bioId = getBioIdFromFocus(focus);
		EXEDataObject bioAttrToLabelMappings = getBioAttrToLabelMappingsForRecordSet(bioId,context);
		if(bioAttrToLabelMappings == null || bioAttrToLabelMappings.getRowCount() < 1){
			populateBioAttrToLabelTable(focus,state);			
			bioAttrToLabelMappings = getBioAttrToLabelMappingsForRecordSet(bioId,context);
		}
		populateLabelAndValueListsFromBioAttrToLabelMappings(bioAttrToLabelMappings,value,labels,context);
		_log.debug("LOG_DEBUG_EXTENSION_COMPDOMCODELKUP","Leaving ComputedDomCodeLkup",100L);		
		return RET_CONTINUE;
	}
	
	private void populateBioAttrToLabelTable(DataBean focus, StateInterface state) throws EpiDataException {
		Bio auditedBio = getAudedBioFromFocus(focus,state);
		if(auditedBio == null)
			return;
		OAGlobalAudit.populateBioAttrToLabelTable(auditedBio,Metadata.getInstance().getBioType(auditedBio.getTypeName()));
	}	

	private Bio getAudedBioFromFocus(DataBean focus, StateInterface state) throws EpiDataException {
		Bio auditedBio = null;
		String auditedBioName = null;
		if(focus.isBio())
		{
			auditedBioName = ((Bio)focus).getBio("row_level_audit").getBio("audit").getString("bio_name");
		} else
			if (focus.isBioCollection() && ((BioCollection) focus).size() > 0) {
			auditedBioName = ((BioCollection)focus).elementAt(0).getBio("row_level_audit").getBio("audit").getString("bio_name");
		}
		
		if(auditedBioName == null)
			return null;
		
		RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();		
		
		while(currentForm != null){
			if(currentForm.getFocus().isBio() && ((Bio)currentForm.getFocus()).getTypeName().equals(auditedBioName)){
				return (Bio)currentForm.getFocus();
			}
			currentForm = currentForm.getParentForm(state);
		}
		return auditedBio;
	}

	private void populateLabelAndValueListsFromBioAttrToLabelMappings(
			EXEDataObject bioAttrToLabelMappings,List value, List labels,DropdownContentsContext context) {
		
		if(bioAttrToLabelMappings == null)
			return;
		LabelFactory fac = new LabelFactory(Metadata.getInstance().getMetaFactory());	
		UserInterface user = context.getState().getUser();
		for(int i = 1; i < bioAttrToLabelMappings.getRowCount() + 1; i++){
			bioAttrToLabelMappings.setRow(i);
			Object attrIdObj = bioAttrToLabelMappings.getAttribValue(new TextData("ATTRIBUTEID"));
			String attrId = attrIdObj == null?null:attrIdObj.toString();
			Object labelIdObj = bioAttrToLabelMappings.getAttribValue(new TextData("LABELID"));
			String labelId = labelIdObj == null?null:labelIdObj.toString();								
			if(attrId != null && labelId != null){					
				Label label = fac.getByObjectIdAndLabelTypeLookupIdAndLocaleId(	labelId,
																				"label",
																				user.getLocale().getLocaleIDString());
				if (label == null) {
					// In case the label is null, try getting the label based on only the language
					label = fac.getByObjectIdAndLabelTypeLookupIdAndLocaleId(	labelId,
																				"label",
																				user.getLocale().getJavaLocale().getLanguage());
				}
				if(label != null){
					labels.add(label.getText());
					value.add(attrId);
				}
			}
		}		
		
	}

	private EXEDataObject getBioAttrToLabelMappingsForRecordSet(
			String bioId,DropdownContentsContext context) throws EpiDataException {
		Query bioQry = new Query("wm_pl_db","wm_pl_db.db_enterprise = 1","");
		BioCollection enterpriseConnection = context.getState().getDefaultUnitOfWork().getBioCollectionBean(bioQry);		
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		if(userContext.get(SetIntoHttpSessionAction.DB_CONNECTION) == null)
			return null;
		String currDBConn = userContext == null?null:(userContext.get(SetIntoHttpSessionAction.DB_CONNECTION)).toString(); 		
		userContext.put(SetIntoHttpSessionAction.DB_CONNECTION, enterpriseConnection.elementAt(0).get("db_name")); 	
		String bioAttrToLabelSelect = "SELECT ATTRIBUTEID,LABELID FROM BIOATTRLABELS WHERE BIOID = '"+bioId+"' ORDER BY ATTRIBUTEID";
		EXEDataObject bioAttrToLabelMappings = null;
		try {
			bioAttrToLabelMappings = WmsWebuiValidationSelectImpl.select(bioAttrToLabelSelect);													
		} catch (DPException e) {			
		}finally{
			userContext.put(SetIntoHttpSessionAction.DB_CONNECTION, currDBConn);
		}
		return bioAttrToLabelMappings;
	}

	private String getBioIdFromBioName(String bioName) {
		return Metadata.getInstance().getBioType(bioName).getID().toString();	
	}

	private String getBioNameFromFocus(DataBean focus) throws EpiDataException{
		String bioName = null;
		
		if(focus.isBio())
			bioName = ((Bio)focus).getBio("row_level_audit").getBio("audit").getString("bio_name");
		else if(focus.isBioCollection())
			bioName = ((BioCollection)focus).elementAt(0).getBio("row_level_audit").getBio("audit").getString("bio_name");
		
		return bioName;
	}
	
	private String getBioIdFromFocus(DataBean focus) throws EpiDataException{
		if(focus == null)
			return null;
		
		if(focus.isBioCollection() && ((BioCollection)focus).size() == 0)
			return null;
		
		String bioName = getBioNameFromFocus(focus);			
		if(bioName == null)
			return null;
		
		return getBioIdFromBioName(bioName);
	}
}
