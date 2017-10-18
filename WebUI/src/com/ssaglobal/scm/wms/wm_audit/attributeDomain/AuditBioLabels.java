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

import org.apache.commons.lang.StringUtils;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.extensions.OAGlobalAudit;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.Metadata;
import com.epiphany.shr.metadata.objects.bio.BioType;
import com.epiphany.shr.metadata.objects.generated.np.Label;
import com.epiphany.shr.metadata.objects.generated.np.LabelFactory;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.metadata.AttributeDomainExtensionBase;
import com.epiphany.shr.ui.metadata.DropdownContentsContext;
import com.epiphany.shr.ui.model.UserInterface;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;

public class AuditBioLabels extends AttributeDomainExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(AuditBioLabels.class);
	
	@Override
	protected int execute(DropdownContentsContext context, List value, List labels) throws EpiException {			
		_log.debug("LOG_DEBUG_EXTENSION_COMPDOMCODELKUP","Executing ComputedDomCodeLkup",100L);
						
		StateInterface state = context.getState();		
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		sanityCheck(uow);
		//Defect1061.b
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		Object isEnterpriseObj = userContext == null?null:userContext.get(SetIntoHttpSessionAction.DB_ISENTERPRISE);		
		boolean isEnterprise = isEnterpriseObj != null && isEnterpriseObj.toString().equals("1")?true:false;
		Query auditbioQry = null;
		if(!isEnterprise){
			auditbioQry = new Query("wm_auditbios","","");
		}else{
			auditbioQry = new Query("wm_auditbios","wm_auditbios.ISENTERPRISE = '1'","");
		}
		//Defect1061.e
		//Query auditbioQry = new Query("wm_auditbios","","");
		BioCollection auditBios = uow.getBioCollectionBean(auditbioQry);
		LabelFactory fac = new LabelFactory(Metadata.getInstance().getMetaFactory());	
		UserInterface user = context.getState().getUser();
		for(int i = 0; i < auditBios.size(); i++){
			Bio auditBio = auditBios.elementAt(i);
			Label label = fac.getByObjectIdAndLabelTypeLookupIdAndLocaleId(	(String) auditBio.get("BIOID"),
																			"label",
																			user.getLocale().getLocaleIDString());
			if(label == null)
			{
				// In case the label is null, try getting the label based on only the language
				label = fac.getByObjectIdAndLabelTypeLookupIdAndLocaleId(	(String) auditBio.get("BIOID"),
																			"label",
																			user.getLocale().getJavaLocale().getLanguage());
			}
			
			if (label == null || StringUtils.isEmpty(label.getText())) {
				//going to use a query to get the bio name if all else fails
				try {
					BioCollectionBean rs = uow.getBioCollectionBean(new Query("meta_bio_class", "meta_bio_class.bio_class_id = '" + auditBio.get("BIOID") + "'", null));
					for(int j =0 ; j < rs.size(); j++) {
						labels.add(rs.get("" + j).getValue("bio_class_name"));
					}
				} catch (Exception e) {
					//if it all goes pear shaped
					labels.add("");
				}
			}
			else {
				labels.add(label.getText());
			}
			value.add(auditBio.get("BIOID"));
		}
		_log.debug("LOG_DEBUG_EXTENSION_COMPDOMCODELKUP","Leaving ComputedDomCodeLkup",100L);		
		return RET_CONTINUE;
	}

	private void sanityCheck(UnitOfWorkBean defaultUnitOfWork) throws EpiDataException {
		Query auditbioQry = new Query("wm_auditbios","","");
		BioCollection auditBios = defaultUnitOfWork.getBioCollectionBean(auditbioQry);
		
		String delimitedBioIdList = "";
		for(int i = 0; i < auditBios.size(); i++){
			if(delimitedBioIdList.length() == 0)
				delimitedBioIdList += "'"+auditBios.elementAt(i).get("BIOID")+"'";
			else
				delimitedBioIdList += ",'"+auditBios.elementAt(i).get("BIOID")+"'";			
		}

		Query auditedBioQry = null;
		if(delimitedBioIdList.length() > 0){
			
			auditedBioQry = new Query("meta_bio_class","meta_bio_class.row_level_audit_flag = 1 AND NOT (meta_bio_class.bio_class_id IN ("+delimitedBioIdList+"))","");
		}
		else{
			
			auditedBioQry = new Query("meta_bio_class","meta_bio_class.row_level_audit_flag = 1","");
		}
		BioCollection auditedBios = defaultUnitOfWork.getBioCollectionBean(auditedBioQry);
		
		if(auditedBios == null || auditedBios.size() == 0)
			return;
		
		for(int i = 0; i < auditedBios.size(); i++){			
			Bio auditBio = auditedBios.elementAt(i);
			BioType bioType = Metadata.getInstance().getBioType((String)auditBio.get("bio_class_name"));
			String bioDataSource = bioType.getPrimaryKeyRecordSetType().getDataSource().getName();

			if(bioDataSource != null && (bioDataSource.equalsIgnoreCase("PRD1") || bioDataSource.equalsIgnoreCase("ADMIN") || bioDataSource.equalsIgnoreCase("WP"))){
				
				OAGlobalAudit.addRecordToAuditBiosTable(defaultUnitOfWork.getUOW(), Metadata.getInstance().getBioType((String)auditBio.get("bio_class_name")));
			}
		}
		
	}
	
}
