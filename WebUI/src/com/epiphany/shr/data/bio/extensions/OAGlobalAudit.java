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

package com.epiphany.shr.data.bio.extensions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;

import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioAttributeMetadata;
import com.epiphany.shr.data.bio.BioAttributeTypes;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.BioMappingTypes;
import com.epiphany.shr.data.bio.BioUtil;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.Metadata;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.metadata.objects.bio.AttributeType;
import com.epiphany.shr.metadata.objects.bio.BioType;
import com.epiphany.shr.metadata.objects.bio.FieldMappedAttributeType;
import com.epiphany.shr.metadata.objects.dm.Field;
import com.epiphany.shr.metadata.objects.dm.RecordSetType;
import com.epiphany.shr.metadata.objects.dm.RelationshipTypeElement;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.sf.util.UserDataBio;
import com.epiphany.shr.util.GUIDFactory;
import com.epiphany.shr.util.algorithm.StringGen;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationDeleteImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationInsertImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.UserUtil;


/**
 * OAGlobalAudit generates row and field level audit information for bios.
 *
 *  @metaref bio_class;audit;ISharedConstants;Bio used to store audit information for every bio-type in the system.
 *  @metaref bio_class;row_level_audit;ISharedConstants;Bio used to store row-level audit information.
 *  @metaref bio_class;field_level_audit;ISharedConstants;Bio used to store field-level audit information.
 *  @metaref bio_class;routable;ISharedConstants;Routable bio interface assures availability of routing attributes.
 *  @metaref bio_attribute;audit.user_updated;ISharedConstants;Bio attribute showing which user updated the bio.
 *  @metaref bio_attribute;audit.date_updated;ISharedConstants;Bio attribute showing when the bio was updated.
 *  @metaref bio_attribute;row_level_audit.user_updated;ISharedConstants;Bio attribute showing which user updated the bio.
 *  @metaref bio_attribute;row_level_audit.date_updated;ISharedConstants;Bio attribute showing when the bio was updated.
 *  @metaref bio_attribute;row_level_audit.audit_detail;ISharedConstants;Bio attribute containing a summary of the changes made.
 *  @metaref bio_attribute;field_level_audit.bio_attribute_name;ISharedConstants;The changed bio attribute.
 *  @metaref bio_attribute;field_level_audit.data_type_lkp;ISharedConstants;The type of value stored in this bio attribute.
 *  @metaref bio_attribute;field_level_audit.column_name;ISharedConstants;The column name where this bio attribute is stored.
 *  @metaref bio_attribute;field_level_audit.old_value;ISharedConstants;The previous value of this bio attribute.
 *  @metaref bio_attribute;field_level_audit.new_value;ISharedConstants;The new value of this bio attribute.
 *  @metaref bio_attribute;workflow_task.assigned_agent_id;ISharedConstants;Used to retrieve the agent name for storage.
 *  @metaref bio_attribute;workflow_task.assigned_group_queue_id;ISharedConstants;Used to retrieve the group_queue name for storage.
 *  @metaref bio_attribute;portal_user_data.individual_full_name;*;Bio attribute that stores the portal users full name.
 */

/**
 * ***************************************************************************************************************************
 * MODIFICATION HISTORY
 * ***************************************************************************************************************************
 * Date              Developer            Comments
 * ***************************************************************************************************************************
 * April 9, 2010     Anindhya Sharma      After some audit records were created any insert, update or delete appeared to hang.
 *                                        In the logs the select for nslqconfig appeared multiple times. The slowdown happened
 *                                        after the purge time value was passed. As part of this fix, the purge logic is being 
 *                                        removed and will be handled separately using archive scripts.
 *                                        Defect: 269131 and Ticket: 3745045
 *                                        Code marked with 269131    
 *                                        
 * 
 * ***************************************************************************************************************************
 */


public class OAGlobalAudit extends com.epiphany.shr.data.bio.extensions.BioExtensionBase {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(OAGlobalAudit.class);
	
	private static final String OWNING_BIO_AUDIT_ATTRIBUTE_NAME = "audit";
	private static final String AUDIT_BIO_NAME = "audit";
	private static final String AUDIT_BIO_NAME_ATTRIBUTE = "bio_name";
	private static final String AUDIT_DATE_INSERTED_ATTRIBUTE = "date_inserted";
	private static final String AUDIT_USER_INSERTED_ATTRIBUTE = "user_inserted";
	private static final String AUDIT_DATE_UPDATED_ATTRIBUTE = "date_updated";
	private static final String AUDIT_USER_UPDATED_ATTRIBUTE = "user_updated";
	private static final String ROW_LEVEL_AUDIT_TEXT = "row_level_audit_text";
	private static final String ROW_LEVEL_AUDIT_CREATED = "row_level_audit_created";
	private static final String ROW_LEVEL_AUDIT_BIO_NAME = "row_level_audit";
	private static final String FIELD_LEVEL_AUDIT_BIO_NAME = "field_level_audit";
	private static final String RLA_DATE_UPDATED_ATTRIBUTE = "date_updated";
	private static final String RLA_USER_UPDATED_ATTRIBUTE = "user_updated";
	private static final String RLA_RELATED_TBL_NAME_ATTRIBUTE = "related_tbl_name";
	private static final String RLA_AUDIT_DETAIL_ATTRIBUTE = "audit_detail";
	private static final String FLA_BIO_ATTRIBUTE_NAME_ATTRIBUTE = "bio_attribute_name";
	private static final String FLA_DATA_TYPE_LKP_ATTRIBUTE = "data_type_lkp";
	private static final String FLA_COLUMN_NAME_ATTRIBUTE = "column_name";
	private static final String FLA_OLD_VALUE_ATTRIBUTE = "old_value";
	private static final String FLA_NEW_VALUE_ATTRIBUTE = "new_value";
	private static final String DATE_CREATED_ATTRIBUTE = "date_created";
	private static final String USER_CREATED_ATTRIBUTE = "user_created";
	private static final String AUDIT_INDIVIDUAL_FULL_NAME = "individual_full_name";
	private static final String AUDIT_WHSEID = "WHSEID";
	private static final String AUDIT_RELATED_STRING_ID = "related_string_id";
	private static HashMap warehouseTable = null;    
	public static String DB_CONNECTION = "dbConnectionName";
	public static String DB_USERID =	"dbUserName";
	public static String DB_PASSWORD =	"dbPassword";
	public static String DB_DATABASE = "dbDatabase";
	public static String DB_ISENTERPRISE = "dbIsEnterprise";
	private static Vector populatedBios = new Vector();	
	private static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");
	protected int bioBeforeDelete(EpnyServiceContext context, Bio bio)
			throws EpiException {
		_log.debug("LOG_SYSTEM_OUT","\n\nIn Before Delete!!!\n\n",100L);	
		
		// 269131.b
		BioType bioType = Metadata.getInstance().getBioType(bio.getTypeName());
		if (!bioType.getAuditFlag()) {
			return RET_CONTINUE;
		}
		// 269131.e

		
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		Object isEnterpriseObj = userContext == null?null:userContext.get(SetIntoHttpSessionAction.DB_ISENTERPRISE);		
		boolean isEnterprise = isEnterpriseObj != null && isEnterpriseObj.toString().equals("1")?true:false;
		UnitOfWork unitOfWork = bio.getUnitOfWork();
		String warehouse = (userContext == null || userContext.get(SetIntoHttpSessionAction.DB_USERID) == null)?null:(userContext.get(SetIntoHttpSessionAction.DB_USERID)).toString();
		
		/* 269131.d
		if(warehouse != null)
			purgeAuditRecords(bio.getUnitOfWork());
		*/
		
		boolean isGlobalAuditingOnForCurrentWarehouse = isGlobalAuditingOnForWarehouse(warehouse,unitOfWork);
			
		if(!isEnterprise){
			if(!isGlobalAuditingOnForCurrentWarehouse){
				return RET_CONTINUE;	
			}
		}
				
		// 269131.d BioType bioType = Metadata.getInstance().getBioType(bio.getTypeName());
		
		//If auditing is not implemented on this BIO then do nothing
		if (! bioType.getAttributeNames().contains(OWNING_BIO_AUDIT_ATTRIBUTE_NAME) ||
				bio.getTypeName().equals(AUDIT_BIO_NAME) ||
				bio.getTypeName().equals(ROW_LEVEL_AUDIT_BIO_NAME) ||
				bio.getTypeName().equals(FIELD_LEVEL_AUDIT_BIO_NAME)
		)
		{
			return super.bioAfterDelete( context, bio );
		}
		
		boolean isAuditingOnForBio = warehouse != null?isAuditingOnForBio(bio,unitOfWork):true;		
		
		/* 269131.d
		if (!bioType.getAuditFlag()) 		
			return RET_CONTINUE;
		*/
		//Defect1061.b
		if(isAuditingOnForBio)
			recordDelete(context,bio,warehouse);
		//Defect1061.e
		
/*
 * Defect1061.b
 *  if(!isEnterprise){
			if(isAuditingOnForBio)
				recordDelete(context,bio,warehouse);
		}else{
			_log.debug("LOG_SYSTEM_OUT","\n\nAbout to iterate whses!!!\n\n",100L);	
			Iterator warehouseItr = getWarehouses(unitOfWork).keySet().iterator();			
			while(warehouseItr.hasNext()){
				String tempWarehouse = (String) warehouseItr.next();
				if(isGlobalAuditingOnForWarehouse(tempWarehouse,unitOfWork) && isAuditingOnForBio(bioType, tempWarehouse, unitOfWork)){
					_log.debug("LOG_SYSTEM_OUT","\n\nrecording delete!!!\n\n",100L);	
					recordDelete(context,bio,tempWarehouse);
				}
			}
		}
* Defect1061.e
		*/
		
		return RET_CONTINUE;	
	}
	
	private boolean isAuditingOnForBio(Bio bio, UnitOfWork unitOfWork) throws EpiDataException {
		
		Query bioQry = new Query("wm_auditbios","wm_auditbios.BIOID = '"+Metadata.getInstance().getBioType(bio.getTypeName()).getID()+"'","");
				
		BioCollection auditBios = unitOfWork.findByQuery(bioQry);
		if(auditBios == null || auditBios.size() == 0){			
			addRecordToAuditBiosTable(unitOfWork,Metadata.getInstance().getBioType(bio.getTypeName()));
		}
		auditBios = unitOfWork.findByQuery(bioQry);
		
		if(auditBios == null || auditBios.size() == 0)
			return false;
		
		if(auditBios.elementAt(0).get("DOAUDIT").equals("1"))
			return true;
		return false;
	}

	
	
	public static synchronized void addRecordToAuditBiosTable(UnitOfWork uow,BioType bioType)  {
		
		String bioId = bioType.getID().toString();
		//Defect1061.b
		String isEnt = null;
		if(bioType.getName().equals("sku")){
			isEnt = "1";
		}else{
			isEnt = "0";
		}
		//Defect1061.e
		 
		String selectSql = "SELECT * FROM AUDITBIOS WHERE BIOID = '"+bioId+"'";		
		//Defect1061 String insertSql = "INSERT INTO AUDITBIOS (AUDITBIOSID,BIOID,DOAUDIT) VALUES ('"+GUIDFactory.getGUIDStatic()+"','"+bioId+"','0')";
		String insertSql = "INSERT INTO AUDITBIOS (AUDITBIOSID,BIOID,DOAUDIT,ISENTERPRISE) VALUES ('"+GUIDFactory.getGUIDStatic()+"','"+bioId+"','0','"+isEnt+"')";	//Defect1061
		try {
			EXEDataObject records = WmsWebuiValidationSelectImpl.select(selectSql);
			
			if(records.getRowCount() > 0)
				return;
			
			WmsWebuiValidationInsertImpl.insert(insertSql);
			
		} catch (DPException e) {			
			e.printStackTrace();
		}
	}

	/*
	 * 269131.d
	private void purgeAuditRecords(UnitOfWork unitOfWork) throws EpiDataException {
		String auditPurgeThresholdStr = SsaAccessBase.getConfig("webUI","webUIConfig").getValue("auditRecordsPurgeAfter");
		Integer auditPurgeThresholdInt = new Integer("-"+auditPurgeThresholdStr);
		GregorianCalendar gc = new GregorianCalendar();
		gc.add(Calendar.DAY_OF_WEEK,auditPurgeThresholdInt.intValue());	
		Query auditRecordQry = new Query("audit","audit.date_updated < @DATE['"+gc.getTimeInMillis()+"']","");
		BioCollection auditRecords = unitOfWork.findByQuery(auditRecordQry);
		
		//Delete meta-data audit records that are older than the threshold set in config file
		for(int i = 0; i < auditRecords.size(); i++){
			BioCollection rowAuditRecords = auditRecords.elementAt(i).getBioCollection("row_level_audits");
			BioCollection fieldAuditRecords = auditRecords.elementAt(i).getBioCollection("field_level_audits");
			for(int j = 0; j < fieldAuditRecords.size(); j++){
				fieldAuditRecords.elementAt(j).delete();
			}
			for(int j = 0; j < rowAuditRecords.size(); j++){
				rowAuditRecords.elementAt(j).delete();
			}
			auditRecords.elementAt(i).delete();
		}
		
		auditRecordQry = new Query("wm_audit_delete","wm_audit_delete.EDITDATE < @DATE['"+gc.getTimeInMillis()+"']","");
		auditRecords = unitOfWork.findByQuery(auditRecordQry);
		
		//Delete warehouse audit records that are older than the threshold set in config file
		for(int i = 0; i < auditRecords.size(); i++){			
			auditRecords.elementAt(i).delete();
		}
	}
	*/

	private void recordDelete(EpnyServiceContext context, Bio bio,
			String warehouse) throws EpiDataException {
		//HelperBio auditBio = null;		
		BioType bioType = Metadata.getInstance().getBioType(bio.getTypeName());		
		HashMap bioAttributesToRecord = getBioAttributesToRecord(bio, warehouse, bioType);
		TreeSet orderedBioAttributes = new TreeSet(bioAttributesToRecord.keySet());
		HashMap keyAttributesToRecord = getBioKeyAttributesToRecord(bio, warehouse, bioType);
		Iterator bioAttributeNameItr = orderedBioAttributes.iterator();
		Iterator keyAttributeNameItr = keyAttributesToRecord.keySet().iterator();
		ArrayList largeFields = new ArrayList();
		ArrayList skippedAttributes = new ArrayList();
		String colList = "";
		String valList = "";
		
		if(keyAttributeNameItr.hasNext()){
			//auditBio = bio.getUnitOfWork().createHelperBioWithDefaults("wm_audit_delete");
			colList += "GUID";
			valList += "'"+GUIDFactory.getGUIDStatic()+"'";
			colList += ",BIOID";
			valList += ",'"+bioType.getID().toString()+"'";
			
//			auditBio.set("GUID", GUIDFactory.getGUIDStatic());
//			auditBio.set("BIOID", bioType.getID().toString());
			for(int i = 1;keyAttributeNameItr.hasNext();i++){
				String auditBioLabelAttr = "KEY"+i+"LABEL";
				String auditBioValueAttr = "KEY"+i+"VALUE";
				String attributeToRecordName = (String)keyAttributeNameItr.next();
				String attributeToRecordLabel = (String)keyAttributesToRecord.get(attributeToRecordName);
				if(attributeToRecordLabel == null)
					attributeToRecordLabel = attributeToRecordName;				
				String attributeToRecordValue = getStringValueOfBioAttribute(bio.get(attributeToRecordName));
				colList += ","+auditBioLabelAttr;
				valList += ",'"+attributeToRecordLabel+"'";
				colList += ","+auditBioValueAttr;
				valList += ",'"+attributeToRecordValue+"'";
//				auditBio.set(auditBioLabelAttr, attributeToRecordLabel);
//				auditBio.set(auditBioValueAttr, attributeToRecordValue);
			}
		}		
		int index = 1;
		for(;bioAttributeNameItr.hasNext() && index < 101;index++){			
			String auditBioLabelAttr = "FIELD"+index+"LABEL";
			String auditBioValueAttr = "FIELD"+index+"VALUE";
			String attributeToRecordName = (String)bioAttributeNameItr.next();			
			String attributeToRecordValue = getStringValueOfBioAttribute(bio.get(attributeToRecordName));
			while(bioAttributeNameItr.hasNext() && (attributeToRecordValue == null || attributeToRecordValue.trim().length() == 0)){
				skippedAttributes.add(attributeToRecordName);
				attributeToRecordName = (String)bioAttributeNameItr.next();
				attributeToRecordValue = getStringValueOfBioAttribute(bio.get(attributeToRecordName));
			}
			String attributeToRecordLabel = (String)bioAttributesToRecord.get(attributeToRecordName);
			if(attributeToRecordLabel == null)
				attributeToRecordLabel = attributeToRecordName;
			
			if(attributeToRecordValue != null && attributeToRecordValue.length() > 51){
				largeFields.add(attributeToRecordName);
				index--;
				continue;
			}
			colList += ","+auditBioLabelAttr;
			valList += ",'"+attributeToRecordLabel+"'";
			colList += ","+auditBioValueAttr;
			valList += ",'"+attributeToRecordValue+"'";
//			auditBio.set(auditBioLabelAttr, attributeToRecordLabel);
//			auditBio.set(auditBioValueAttr, attributeToRecordValue);
		}
		Iterator skippedAttributesItr = skippedAttributes.iterator();
		for(;skippedAttributesItr.hasNext() && index < 101;index++){	
			String auditBioLabelAttr = "FIELD"+index+"LABEL";
			String auditBioValueAttr = "FIELD"+index+"VALUE";
			String attributeToRecordName = (String)skippedAttributesItr.next();
			String attributeToRecordValue = getStringValueOfBioAttribute(bio.get(attributeToRecordName));
			String attributeToRecordLabel = (String)bioAttributesToRecord.get(attributeToRecordName);
			if(attributeToRecordLabel == null)
				attributeToRecordLabel = attributeToRecordName;
			
			if(attributeToRecordValue != null && attributeToRecordValue.length() > 51){
				largeFields.add(attributeToRecordName);
				index--;
				continue;
			}
			colList += ","+auditBioLabelAttr;
			valList += ",'"+attributeToRecordLabel+"'";
			colList += ","+auditBioValueAttr;
			valList += ",'"+attributeToRecordValue+"'";
//			auditBio.set(auditBioLabelAttr, attributeToRecordLabel);
//			auditBio.set(auditBioValueAttr, attributeToRecordValue);
		}
		for(int i = 1; i < largeFields.size() && i < 3; i++){			
			String auditBioLabelAttr = "LARGEFIELD"+i+"LABEL";
			String auditBioValueAttr = "LARGEFIELD"+i+"VALUE";
			String attributeToRecordName = (String)largeFields.get(i);
			String attributeToRecordLabel = (String)bioAttributesToRecord.get(attributeToRecordName);
			if(attributeToRecordLabel == null)
				attributeToRecordLabel = attributeToRecordName;
			String attributeToRecordValue = getStringValueOfBioAttribute(bio.get(attributeToRecordName));
			colList += ","+auditBioLabelAttr;
			valList += ",'"+attributeToRecordLabel+"'";
			colList += ","+auditBioValueAttr;
			valList += ",'"+attributeToRecordValue+"'";
//			auditBio.set(auditBioLabelAttr, attributeToRecordLabel);
//			auditBio.set(auditBioValueAttr, attributeToRecordValue);
		}
		_log.debug("LOG_SYSTEM_OUT","\n\nCreating SQL!!!\n\n",100L);
		if(colList.length() > 0){
			
			EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
			String currDBConn = userContext == null?null:(userContext.get(SetIntoHttpSessionAction.DB_CONNECTION)).toString();			
			//auditBio.set("DELETEWHO", UserUtil.getUserId());
			colList += ",DELETEWHO";
			valList += ",'"+UserUtil.getUserId()+"'";
			String insertSql = "INSERT INTO AUDITDELETES ("+colList+") VALUES ("+valList+")";
			_log.debug("LOG_SYSTEM_OUT","\n\nInsert SQL:"+insertSql+"\n\n",100L);
			//userContext.put(DB_CONNECTION,getWarehouses(bio.getUnitOfWork()).get(warehouse)); 	
			String warehouseConn = (String)getWarehouses(bio.getUnitOfWork()).get(warehouse);
			WmsWebuiValidationInsertImpl.insert(insertSql, warehouseConn, warehouse);
//			bio.getUnitOfWork().createBio(auditBio);
//			bio.getUnitOfWork().save();
//			userContext.put(DB_CONNECTION,currDBConn);
		}
	}

	private HashMap getBioKeyAttributesToRecord(Bio bio, String warehouse,BioType bioType) throws EpiDataException {
		HashMap keyAttr = new HashMap();
		HashMap bioAttrToLabelMappings = getBioAttrToLabelMappings(bio,bioType);
		String[] PKFieldNames = bioType.getPrimaryKeyRecordSetType().getPrimaryKeyFieldSet().getFieldNames();
		for(int i = 0; i < PKFieldNames.length; i++){
			keyAttr.put(PKFieldNames[i], bioAttrToLabelMappings.get(PKFieldNames[i]));
		}
		return keyAttr;
	}

	private HashMap getBioAttrToLabelMappings(Bio bio,BioType bioType) throws EpiDataException {
		HashMap bioAttr = new HashMap();
		String bioId = bioType.getID().toString();
		Query bioQry = new Query("wm_pl_db","wm_pl_db.db_enterprise = 1","");
		BioCollection enterpriseConnection = bio.getUnitOfWork().findByQuery(bioQry);
		
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		if(userContext.get(SetIntoHttpSessionAction.DB_CONNECTION) == null)
			return bioAttr;
		String currDBConn = userContext == null?null:(userContext.get(SetIntoHttpSessionAction.DB_CONNECTION)).toString(); 		
		userContext.put(DB_CONNECTION, enterpriseConnection.elementAt(0).get("db_name")); 	
		String bioAttrToLabelSelect = "SELECT ATTRIBUTEID,LABELID FROM BIOATTRLABELS WHERE BIOID = '"+bioId+"'";
		
		try {
			EXEDataObject bioAttrToLabelMappings = WmsWebuiValidationSelectImpl.select(bioAttrToLabelSelect);
			
			if( bioAttrToLabelMappings.getRowCount() < 1){
				populateBioAttrToLabelTable(bio,bioType);
				bioAttrToLabelMappings = WmsWebuiValidationSelectImpl.select(bioAttrToLabelSelect);
			}
							
			for(int i = 1; i < bioAttrToLabelMappings.getRowCount() + 1; i++){
				bioAttrToLabelMappings.setRow(i);
				Object attrIdObj = bioAttrToLabelMappings.getAttribValue(new TextData("ATTRIBUTEID"));
				String attrId = attrIdObj == null?null:attrIdObj.toString();
				Object labelIdObj = bioAttrToLabelMappings.getAttribValue(new TextData("LABELID"));
				String labelId = labelIdObj == null?null:labelIdObj.toString();
								
				if(attrId != null && labelId != null)
					bioAttr.put(attrId, labelId);
			}			
		} catch (DPException e) {			
		}
		userContext.put(DB_CONNECTION, currDBConn); 	

		return bioAttr;
	}

	private HashMap getBioAttributesToRecord(Bio bio, String warehouse,BioType bioType) throws EpiDataException {		
		HashMap bioAttrToLabelMappings = getBioAttrToLabelMappings(bio,bioType);
		HashMap bioAttr = new HashMap();

		BioAttributeMetadata[] bioAttributes = bio.getBioAttributeMetadata();
		for(int i = 0; i < bioAttributes.length; i++){
			String label = (String)bioAttrToLabelMappings.get(bioAttributes[i].getName());
			if(label != null && bioAttributes[i].getMappingType() == BioMappingTypes.MAP_DIRECT){					
				if(		!bioAttributes[i].getName().equalsIgnoreCase("WHSEID") 
						&& !bioAttributes[i].getName().equalsIgnoreCase("ADDWHO")
						&& !bioAttributes[i].getName().equalsIgnoreCase("EDITWHO")
						&& !bioAttributes[i].getName().equalsIgnoreCase("ADDDATE")
						&& !bioAttributes[i].getName().equalsIgnoreCase("EDITDATE")
						&& !bioAttributes[i].getName().equalsIgnoreCase("SERIALKEY"))
					bioAttr.put(bioAttributes[i].getName(),label);
			}
		}

		return bioAttr;
	}

	public static synchronized void populateBioAttrToLabelTable(Bio bio, BioType bioType) throws EpiDataException {
		
		String bioId = bioType.getID().toString();
		Vector populatedBioAttributeNames = new Vector();		
		if(populatedBios.contains(bioId))
			return;
		Query enterpriseConnQry = new Query("wm_pl_db","","");
		BioCollection allConnections = bio.getUnitOfWork().findByQuery(enterpriseConnQry);
		BioCollection enterprisConnection = allConnections.filter(new Query ("wm_pl_db","wm_pl_db.db_enterprise = 1",""));
		//Defect1061 BioCollection defaultDataSourceConnection = allConnections.filter(new Query ("wm_pl_db","wm_pl_db.db_logid = '"+enterprisConnection.elementAt(0).get("db_logid")+"' AND wm_pl_db.db_enterprise = 0",""));
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		
		if(userContext.get(SetIntoHttpSessionAction.DB_CONNECTION) == null)
			return;
		String currDBConn = userContext == null?null:(userContext.get(SetIntoHttpSessionAction.DB_CONNECTION)).toString(); 
		BioAttributeMetadata[] bioAttributeMetadataAry = bio.getBioAttributeMetadata();
		String delimitedBioAttrNameList = "";
		for(int i = 0; i < bioAttributeMetadataAry.length; i++){
			if(delimitedBioAttrNameList.length() == 0)
				delimitedBioAttrNameList += "'"+bioAttributeMetadataAry[i].getName()+"'";
			else
				delimitedBioAttrNameList += ",'"+bioAttributeMetadataAry[i].getName()+"'";			
		}

		Query bioQry = new Query("meta_bio_attribute","meta_bio_attribute.bio_attribute_name IN ("+delimitedBioAttrNameList+") AND meta_bio_attribute.bio_class_id = '"+bioId+"'","");
		BioCollection bc = bio.getUnitOfWork().findByQuery(bioQry);
		
		for(int i = 0; i < bc.size(); i++){
			
			String bioAttrName = (String)bc.elementAt(i).get("bio_attribute_name");
			if(populatedBioAttributeNames.contains(bioAttrName))
				continue;
			bioQry = new Query("meta_bio_display_attribute","meta_bio_display_attribute.bio_attribute_id = '"+bc.elementAt(i).get("bio_attribute_id")+"'","");
			BioCollection bioDisplayAttrRecords = bio.getUnitOfWork().findByQuery(bioQry);

			String delemitedBioDisplayAttrIdList = "";
			for(int j = 0; j < bioDisplayAttrRecords.size(); j++){
				if(delemitedBioDisplayAttrIdList.length() == 0)
					delemitedBioDisplayAttrIdList += "'"+bioDisplayAttrRecords.elementAt(j).get("bio_display_attribute_id")+"'";
				else
					delemitedBioDisplayAttrIdList += ",'"+bioDisplayAttrRecords.elementAt(j).get("bio_display_attribute_id")+"'";			
			}
			
			if(delemitedBioDisplayAttrIdList.length() > 0){

				bioQry = new Query("meta_form_widget","meta_form_widget.bio_display_attribute_id IN ("+delemitedBioDisplayAttrIdList+")","");
				BioCollection widgets = bio.getUnitOfWork().findByQuery(bioQry);
				if(widgets != null && widgets.size() > 0){
					Bio widget = widgets.elementAt(0);
					//Defect1061 userContext.put(DB_CONNECTION, defaultDataSourceConnection.elementAt(0).get("db_name"));
					userContext.put(DB_CONNECTION, enterprisConnection.elementAt(0).get("db_name"));		//Defect1061
					WmsWebuiValidationInsertImpl.insert("INSERT INTO BIOATTRLABELS (BIOATTRLABELSID,ATTRIBUTEID,LABELID,BIOID) VALUES ('"+GUIDFactory.getGUIDStatic()+"','"+bioAttrName+"','"+widget.get("form_widget_id")+"','"+bioId+"')");
					userContext.put(DB_CONNECTION, currDBConn); 	
					populatedBioAttributeNames.add(bioAttrName);
				}
			}
			
		}
		populatedBios.add(bioId);
	}

	protected int bioBeforeUpdate( EpnyServiceContext context, Bio bio )
	throws EpiException
	{			
		// 269131.b
		BioType bioType = Metadata.getInstance().getBioType(bio.getTypeName());
		if (!bioType.getAuditFlag()) {
			return RET_CONTINUE;
		}
		// 269131.e

		String bioTypeName = bio.getTypeName();						
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		Object isEnterpriseObj = userContext == null?null:userContext.get(SetIntoHttpSessionAction.DB_ISENTERPRISE);		
		boolean isEnterprise = isEnterpriseObj != null && isEnterpriseObj.toString().equals("1")?true:false;
		UnitOfWork unitOfWork = bio.getUnitOfWork();
		String warehouse = (userContext == null || userContext.get(SetIntoHttpSessionAction.DB_USERID) == null)?null:(userContext.get(SetIntoHttpSessionAction.DB_USERID)).toString();
		/* 269131.d
		if(warehouse != null)
			purgeAuditRecords(bio.getUnitOfWork());
		*/
		
		boolean isGlobalAuditingOnForCurrentWarehouse = isGlobalAuditingOnForWarehouse(warehouse,unitOfWork);		
		//If we are at the warehouse level then do not proceed if global auditing is off for this warehouse
		if(!isEnterprise){
			if(!isGlobalAuditingOnForCurrentWarehouse){
				return RET_CONTINUE;	
			}
		}

		// 269131.d BioType bioType = Metadata.getInstance().getBioType(bioTypeName);
		
		//If auditing is not implemented on this BIO then do nothing
		if (! bioType.getAttributeNames().contains(OWNING_BIO_AUDIT_ATTRIBUTE_NAME) ||
				bioTypeName.equals(AUDIT_BIO_NAME) ||
				bioTypeName.equals(ROW_LEVEL_AUDIT_BIO_NAME) ||
				bioTypeName.equals(FIELD_LEVEL_AUDIT_BIO_NAME)
		)
		{
			return super.bioBeforeUpdate( context, bio );
		}
		boolean isAuditingOnForBio = warehouse != null?isAuditingOnForBio(bio,unitOfWork):true;
		
		if (bioType.getAuditFlag() && !isAuditingOnForBio) 		
			return RET_CONTINUE;	
		
		Bio auditBio = null;

		Calendar now = Calendar.getInstance();

		// even when Audit is disabled we still have to write out the
		// audit bio as it contains the created/modified information
		// displayed on screen in the grey box		
		if(isGlobalAuditingOnForCurrentWarehouse){
			
			if (BioUtil.isInitialized(bio, OWNING_BIO_AUDIT_ATTRIBUTE_NAME))
			{
				auditBio = bio.getBio(OWNING_BIO_AUDIT_ATTRIBUTE_NAME);
				if (auditBio == null) {
					//jp.answerlink.289260.begin
					//defect1061 auditBio = createAuditBio(context, unitOfWork, bioType, bioTypeName, bio);
					auditBio = createAuditBio(context, unitOfWork, bioType, bioTypeName,warehouse, bio);		//defect1061
					//jp.answerlink.289260.end
					bio.set(OWNING_BIO_AUDIT_ATTRIBUTE_NAME, auditBio);
				}
			}
			else
			{
				//jp.answerlink.289260.begin
				//defect1061 auditBio = createAuditBio(context, unitOfWork, bioType, bioTypeName,bio);
				auditBio = createAuditBio(context, unitOfWork, bioType, bioTypeName,warehouse, bio);		//defect1061
				//jp.answerlink.289260.end
				bio.set(OWNING_BIO_AUDIT_ATTRIBUTE_NAME, auditBio);
			}
			auditBio.set(RLA_USER_UPDATED_ATTRIBUTE,getSafeUserName(context,auditBio));
			auditBio.set(AUDIT_DATE_UPDATED_ATTRIBUTE, now );
			if (bioType.getAuditFlag()) {				
				createRowLevelAudit(context, unitOfWork, bio, bioType, auditBio);
			}
		}		

		BioCollection bc = null;

		if(isEnterprise ){	    	
			Query bioQry = null;
			if(auditBio != null)
				bioQry = new Query(AUDIT_BIO_NAME,AUDIT_BIO_NAME+"."+AUDIT_RELATED_STRING_ID+" = '"+auditBio.get(AUDIT_RELATED_STRING_ID)+"' AND "+AUDIT_BIO_NAME+"."+AUDIT_WHSEID+" != '"+auditBio.get(AUDIT_WHSEID)+"'","");
			else
				bioQry = new Query(AUDIT_BIO_NAME,AUDIT_BIO_NAME+"."+AUDIT_RELATED_STRING_ID+" = '"+bio.get(getRelatedField(bioType))+"'","");
				
			bc = unitOfWork.findByQuery(bioQry);
			Iterator warehouseItr = getWarehouses(unitOfWork).keySet().iterator();			
			while(warehouseItr.hasNext()){
				
				String tempWarehouse = (String) warehouseItr.next();
				if(tempWarehouse.equals(warehouse) || !isGlobalAuditingOnForWarehouse(tempWarehouse,unitOfWork) || !isAuditingOnForBio(bioType,tempWarehouse,unitOfWork))
					continue;

				BioCollection auditRecord = bc.filter(new Query(AUDIT_BIO_NAME,AUDIT_BIO_NAME+"."+AUDIT_WHSEID+" = '"+tempWarehouse+"'",""));
				if(auditRecord == null || auditRecord.size() == 0){
					Bio newAuditBio = createAuditBio(context, unitOfWork, bioType, bioTypeName,tempWarehouse,bio);

					newAuditBio.set(RLA_USER_UPDATED_ATTRIBUTE, getSafeUserName(context,newAuditBio));
					newAuditBio.set(AUDIT_DATE_UPDATED_ATTRIBUTE, now);

					if (bioType.getAuditFlag()){							
						createRowLevelAudit(context, unitOfWork, bio, bioType, newAuditBio);
					}
				}
				else{

					auditRecord.elementAt(0).set(RLA_USER_UPDATED_ATTRIBUTE, getSafeUserName(context,auditRecord.elementAt(0)));
					auditRecord.elementAt(0).set(AUDIT_DATE_UPDATED_ATTRIBUTE, now);

					if (bioType.getAuditFlag()){						
						createRowLevelAudit(context, unitOfWork, bio, bioType, auditRecord.elementAt(0));
					}
				}
			}
		}
		
		return RET_CONTINUE;
	}


	private boolean isAuditingOnForBio(BioType bio, String tempWarehouse, UnitOfWork uow) throws EpiDataException {
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		if(userContext.get(SetIntoHttpSessionAction.DB_CONNECTION) == null)
			return true;
		boolean isOn = false;		
		String currDBConn = userContext == null?null:(userContext.get(SetIntoHttpSessionAction.DB_CONNECTION)).toString(); 		
		userContext.put(DB_CONNECTION, getWarehouses(uow).get(tempWarehouse)); 			
		
		try {
			String bioAuditSelect = "SELECT DOAUDIT FROM AUDITBIOS WHERE BIOID = '"+bio.getID()+"' AND DOAUDIT = '1'";
			isOn = (WmsWebuiValidationSelectImpl.select(bioAuditSelect).getRowCount() > 0);
		} catch (DPException e) {
			throw e;
		}finally{
			userContext.put(DB_CONNECTION, currDBConn); 	
		}
			
		return isOn;
	}

	/**
	 * bioAfterInsert will insert the Audit bios only if it cannot do it during the bioBeforeInsert.
	 */
	protected int bioBeforeInsert( EpnyServiceContext context, Bio bio ) throws EpiException
	{    	
		// 269131.b
		Bio auditBio = null;
		String bioTypeName = bio.getTypeName();        
		BioType bioType = Metadata.getInstance().getBioType(bioTypeName);
		if (!bioType.getAuditFlag()) {     			
			return RET_CONTINUE;          
		}
		// 269131.b

		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();    
		Object isEnterpriseObj = userContext == null?null:userContext.get(SetIntoHttpSessionAction.DB_ISENTERPRISE);		
		boolean isEnterprise = isEnterpriseObj != null && isEnterpriseObj.toString().equals("1")?true:false;		
		String warehouse = (userContext == null || userContext.get(SetIntoHttpSessionAction.DB_USERID) == null)?null:(userContext.get(SetIntoHttpSessionAction.DB_USERID)).toString();
		
		/* 269131.d
		if(warehouse != null)
			purgeAuditRecords(bio.getUnitOfWork());
		*/
		
		UnitOfWork unitOfWork = bio.getUnitOfWork();			
		
		boolean isGlobalAuditingOnForCurrentWarehouse = isGlobalAuditingOnForWarehouse(warehouse,unitOfWork);
		//If we are at the warehouse level then do not proceed if global auditing is off for this warehouse
		if(!isEnterprise){
			if(!isGlobalAuditingOnForCurrentWarehouse){
				return RET_CONTINUE;	
			}
		}				

		/*	269131.d	
		Bio auditBio = null;
		String bioTypeName = bio.getTypeName();        
		BioType bioType = Metadata.getInstance().getBioType(bioTypeName);
		if (! bioType.getAuditFlag()) {     			
			return RET_CONTINUE;          
		}
		*/
		
		if (! bioType.getAttributeNames().contains(OWNING_BIO_AUDIT_ATTRIBUTE_NAME) ||
				bioTypeName.equals(AUDIT_BIO_NAME) ||
				bioTypeName.equals(ROW_LEVEL_AUDIT_BIO_NAME) ||
				bioTypeName.equals(FIELD_LEVEL_AUDIT_BIO_NAME) )
		{
			return super.bioBeforeInsert( context, bio );
		}      
		boolean isAuditingOnForBio = warehouse != null?isAuditingOnForBio(bio,unitOfWork):true;
		if(!isAuditingOnForBio)
			return RET_CONTINUE;	
		
		if(isGlobalAuditingOnForCurrentWarehouse){
			auditBio = createAuditBio(context, unitOfWork, bioType, bioTypeName, warehouse,bio);						
			Bio rowLevelAuditBio =  unitOfWork.createBio(ROW_LEVEL_AUDIT_BIO_NAME);
			StringBuffer rowLevelAuditText = new StringBuffer();
			LocaleInterface loc = getAuditLocaleInterface(context,bio);
			rowLevelAuditText.append(getUserDefinedText(ROW_LEVEL_AUDIT_CREATED,null,loc));
			setRowLevelAuditAttributes(rowLevelAuditBio, auditBio, context, rowLevelAuditText.toString());
		}        

		if(isEnterprise){   		
			createAuditBioCopyForOtherWarehouses(context, unitOfWork, bioType, bioTypeName, auditBio, bio);
		}   	 	
		//unitOfWork.save();       
		return RET_CONTINUE;
	}

	protected Bio createAuditBio( EpnyServiceContext context, UnitOfWork unitOfWork,
			BioType bioType, String bioTypeName, Bio auditedBio )
	throws EpiException
	{
		Bio audit = null;
		String recordSetName = bioType.getPrimaryKeyRecordSetType().getName();
		Calendar now = Calendar.getInstance();

		audit = unitOfWork.createBio(AUDIT_BIO_NAME);
		audit.set(RLA_RELATED_TBL_NAME_ATTRIBUTE, recordSetName);
		audit.set(AUDIT_BIO_NAME_ATTRIBUTE, bioTypeName );
		audit.set(AUDIT_USER_INSERTED_ATTRIBUTE,getSafeUserName(context,audit));
		audit.set(AUDIT_DATE_INSERTED_ATTRIBUTE, now);

		//jp.answerlink.289260.begin
		//If audited bio does not have a GUID, we generate one and set it up
		String guid = null;
		if (auditedBio.get(getRelatedField(bioType))==null){
			guid = GUIDFactory.getGUIDStatic();
			auditedBio.set(getRelatedField(bioType), guid);
		}else{
			guid = (auditedBio.get(getRelatedField(bioType))).toString();
		}

		//bio.set(AUDIT_RELATED_STRING_ID,auditedBio.get(getRelatedField(bioType)));
		audit.set(AUDIT_RELATED_STRING_ID,guid);
		//jp.answerlink.289260.end

		return audit;
	}

	protected Bio createAuditBio( EpnyServiceContext context, UnitOfWork unitOfWork,
			BioType bioType, String bioTypeName, String warehouse, Bio auditedBio)
	throws EpiException
	{
		//jp.answerlink.289260.begin
		//Bio bio = createAuditBio(context, unitOfWork, bioType, bioTypeName, warehouse);
		Bio bio = createAuditBio(context, unitOfWork, bioType, bioTypeName, auditedBio);
		//bio.set(AUDIT_RELATED_STRING_ID,auditedBio.get(getRelatedField(bioType)));
		
		if(warehouse != null)
			bio.set(AUDIT_WHSEID, warehouse);
		else
			bio.set(AUDIT_WHSEID, "NOWHSEID");
		//jp.answerlink.289260.end

		return bio;
	}
	
	/*
	protected Bio createAuditBio( EpnyServiceContext context, UnitOfWork unitOfWork,
			BioType bioType, String bioTypeName, String warehouse )
	throws EpiException
	{
		Bio bio = createAuditBio(context, unitOfWork, bioType, bioTypeName);
		if(warehouse != null)
			bio.set(AUDIT_WHSEID, warehouse);
		else
			bio.set(AUDIT_WHSEID, "NOWHSEID");
		return bio;
	}
	*/
	protected void createAuditBioCopyForOtherWarehouses( EpnyServiceContext context, UnitOfWork unitOfWork,
			BioType bioType, String bioTypeName, Bio auditBio, Bio auditedBio)
	throws EpiException
	{		
		
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();		
		String warehouse = (userContext.get(SetIntoHttpSessionAction.DB_USERID)).toString();				
		Iterator warehousesItr = getWarehouses(unitOfWork).keySet().iterator();
		if(warehousesItr.hasNext()){
		
			while(warehousesItr.hasNext()){
				String tempWarehouse = (String)warehousesItr.next();

				if(!isGlobalAuditingOnForWarehouse(tempWarehouse,unitOfWork) || !isAuditingOnForBio(bioType,tempWarehouse,unitOfWork))
					continue;

				if(tempWarehouse.equals(warehouse)){					
					continue;
				}	
								
				Bio audit = createAuditBio(context, unitOfWork, bioType, bioTypeName,tempWarehouse,auditedBio);																
				createRowLevelAudit(context, unitOfWork, auditedBio, bioType, audit);				
			}	
		}		
	}


	protected void createRowLevelAudit( EpnyServiceContext context,
			UnitOfWork unitOfWork,
			Bio auditedBio,
			BioType auditedBioType,
			Bio auditBio)
	throws EpiException
	{
		
		StringBuffer rowLevelAuditText = new StringBuffer();

		boolean bioIsDirty = false;

		Bio rowLevelAuditBio=null;// =  unitOfWork.createBio(ROW_LEVEL_AUDIT_BIO_NAME);
		Iterator updatedAttributes = auditedBio.getUpdatedAttributes().iterator();

		if (updatedAttributes != null)
		{
			String msgparams[] = {"null","null","null"};
			LocaleInterface loc = getAuditLocaleInterface(context,auditedBio);
			while (updatedAttributes.hasNext())
			{
				String attributeName =  updatedAttributes.next().toString();
				AttributeType attributeType = auditedBioType.getAttributeType(attributeName,true);

				if (attributeType instanceof FieldMappedAttributeType)
				{
					String bioAttributeCurrValueStr = getAttributeValueString(auditedBio, attributeName, false);
					String bioAttributeOldValueStr  = getAttributeValueString(auditedBio, attributeName, true);

					bioIsDirty = true;

					// Special case for S&S
					// For routable BIO's the assigned_agent_id
					// and the group_queue_id should be reversed to
					// the displayed value
					if (attributeName.equals("assigned_agent_id") && (attributeType.getAttributeDomain()==null?true:false) ) {
						bioAttributeCurrValueStr = getAgentName(unitOfWork , bioAttributeCurrValueStr);
						if (bioAttributeOldValueStr != null) {
							bioAttributeOldValueStr = getAgentName(unitOfWork , bioAttributeOldValueStr);
						}
					}

					if (attributeName.equals("assigned_group_queue_id")&& (attributeType.getAttributeDomain()==null?true:false) ) {
						bioAttributeCurrValueStr = getQueueNameForRouting(unitOfWork , bioAttributeCurrValueStr);
						if (bioAttributeOldValueStr != null) {
							bioAttributeOldValueStr = getQueueNameForRouting(unitOfWork , bioAttributeOldValueStr);
						}
					}

					FieldMappedAttributeType fieldMappedAttributeType = (FieldMappedAttributeType) attributeType;
					Field field = fieldMappedAttributeType.getField();

					// Retrieve the text message named in the ROW_LEVEL_AUDIT_TEXT constant
					// and provide the parameter substitution values
					// localized text should be like "{0} has changed from {1} to {2}"
					//
					// {0} = AttributeName
					// {1) = Old Value
					// {2) = New Value
					msgparams[0] = attributeName;
					msgparams[1] =  (bioAttributeOldValueStr != null &&  bioAttributeOldValueStr.length() > 0) ?  bioAttributeOldValueStr :null;
					msgparams[2] = (bioAttributeCurrValueStr != null &&  bioAttributeCurrValueStr.length() > 0) ? bioAttributeCurrValueStr : null;

					String userDefinedText = getUserDefinedText(ROW_LEVEL_AUDIT_TEXT,msgparams,loc);
					rowLevelAuditText.append(userDefinedText);

					if (attributeType.getAuditFlag())
					{
						// needs to use the ACTUAL DB value NOT the mapped uservalue
						bioAttributeCurrValueStr = getAttributeValueString(auditedBio, attributeName, false, false);
						bioAttributeOldValueStr  = getAttributeValueString(auditedBio, attributeName, true, false);
						if (rowLevelAuditBio == null) {
							rowLevelAuditBio =  unitOfWork.createBio(ROW_LEVEL_AUDIT_BIO_NAME);
						}
						createFieldLevelAuditBio(unitOfWork, rowLevelAuditBio,
								attributeType,field,
								bioAttributeOldValueStr,
								bioAttributeCurrValueStr);
					}
				}
			}
		}

		if (bioIsDirty) { // write out audit for changed rows only.
			if (rowLevelAuditBio == null) {
				rowLevelAuditBio =  unitOfWork.createBio(ROW_LEVEL_AUDIT_BIO_NAME);
			}
			setRowLevelAuditAttributes(rowLevelAuditBio, auditBio, context, rowLevelAuditText.toString());
		}
		return;
	}

	protected void createFieldLevelAuditBio(UnitOfWork unitOfWork,
			Bio rowLevelAuditBio,
			AttributeType attributeType,
			Field field,
			String bioAttributeOldValueStr,
			String bioAttributeCurrValueStr)
	throws EpiException
	{
		//defect:1131.b
		int dataType = field.getDataType();
		boolean isUserModified = false;
		isUserModified =isUserModifiled(dataType, bioAttributeOldValueStr,bioAttributeCurrValueStr);
		if(isUserModified){
		//defect:1131.e

			Bio fieldLevelAuditBio  = unitOfWork.createBio(FIELD_LEVEL_AUDIT_BIO_NAME);

			fieldLevelAuditBio.set(ROW_LEVEL_AUDIT_BIO_NAME, rowLevelAuditBio);
			String name = attributeType.getName();
			fieldLevelAuditBio.set(FLA_BIO_ATTRIBUTE_NAME_ATTRIBUTE, name);

			// ApplicationUtil.debug(_log, "LOG_GLOBALAUDIT_1", "field data_type = "+field.getDataType()+" for "+FLA_DATA_TYPE_LKP_ATTRIBUTE);
			//defect:1131 	int dataType = field.getDataType();
			fieldLevelAuditBio.setLabel(FLA_DATA_TYPE_LKP_ATTRIBUTE,
					new Integer(dataType), false, fieldLevelAuditBio.LABEL_TYPE_CODE_INT);
			String physicalName = field.getPhysicalName();
			fieldLevelAuditBio.set(FLA_COLUMN_NAME_ATTRIBUTE, physicalName);
			fieldLevelAuditBio.set(FLA_OLD_VALUE_ATTRIBUTE, bioAttributeOldValueStr);
			fieldLevelAuditBio.set(FLA_NEW_VALUE_ATTRIBUTE, bioAttributeCurrValueStr);
		}		//defect:1131
		return;
	}

	//defect:1131.b
	protected boolean isUserModifiled(int fieldDataType, String oldValue, String newValue){
		if("".equalsIgnoreCase(oldValue.trim())&& !"".equalsIgnoreCase(newValue.trim())){
			return true;
		}
		switch(fieldDataType){
		case BioAttributeTypes.STRING_TYPE:
			if(oldValue.trim().equals(newValue.trim())){
				return false;
			}else{
				return true;
			}
		case BioAttributeTypes.INT_TYPE:
			
			if(Integer.parseInt(oldValue.trim())==Integer.parseInt(newValue.trim())){
				return false;
			}else{
				return true;
			}
		case BioAttributeTypes.FLOAT_TYPE:
			if(Float.parseFloat(oldValue.trim()) ==Float.parseFloat(newValue.trim())){
				return false;
			}else{
				return true;
			}
		case BioAttributeTypes.DECIMAL_TYPE:
			if(Double.parseDouble(oldValue.trim()) == Double.parseDouble(newValue.trim())){
				return false;
			}else{
				return true;
			}
		case BioAttributeTypes.DATE_TYPE:
			if(oldValue.trim().equals(newValue.trim())){
				return false;
			}else{
				return true;
			}
		}
		return false;
	}
	//defect:1131.e
	
	
	
	protected void setRowLevelAuditAttributes(Bio rowLevelAuditBio, Bio auditBio,
			EpnyServiceContext context,
			String rowLevelAuditText)
	throws EpiException
	{
		Calendar now = Calendar.getInstance();

		rowLevelAuditBio.set(AUDIT_BIO_NAME, auditBio);
		String safeUserName = getSafeUserName(context,auditBio);
		rowLevelAuditBio.set(RLA_USER_UPDATED_ATTRIBUTE, safeUserName);
		rowLevelAuditBio.set(RLA_DATE_UPDATED_ATTRIBUTE, now);
		rowLevelAuditBio.set(RLA_AUDIT_DETAIL_ATTRIBUTE, rowLevelAuditText);

		return;
	}

	protected String getSafeUserName(EpnyServiceContext context, Bio bio) throws EpiException{

		// workaround for null context when running as a service
		// problem displays when email creates an interaction
		// also some cases of empty user names being returned

		String userName  = "NONE";
		EpnyUserContext userContext;
		Bio agent_user;

		if (context != null){
			userName  = context.getUserName(); // gets the login userid
			if ( (userContext = context.getUserContext()) != null)
				if ((agent_user = userContext.getAgentUserBio(bio.getUnitOfWork())) != null)  {

					String agentName =  agent_user.getString("agent_name");
					if (agentName != null && agentName.length() > 0) {
						userName = agentName;
					}
				}
				else {
					Bio userDataBio = (Bio)UserDataBio.getUserData(bio.getUnitOfWork(), userName);
					if (userDataBio != null) {
						if (UserDataBio.isPortal()) {
							if (BioUtil.isInitialized(userDataBio, AUDIT_INDIVIDUAL_FULL_NAME)) {
								String portalUserFullName = userDataBio.get(AUDIT_INDIVIDUAL_FULL_NAME).toString();
								if (portalUserFullName != null && portalUserFullName.length() > 0) {
									userName = portalUserFullName;
								}
							}
						}
					}
				}
		}

		return userName;
	}

	private LocaleInterface getAuditLocaleInterface(EpnyServiceContext context, Bio bio) {

		// Fetch the locale from the extension parameter
		String localeParameter = getParameterString("Audit Locale");
		LocaleInterface loc = Metadata.getInstance().getLocale( localeParameter );

		if (loc == null ) // get user locale if no extension parameter is provided
		{
			if (context != null) {
				loc = getUserLocale(context);
			}
			if (loc == null){ // if null and no parameter fallback to "en"
				loc = Metadata.getInstance().getLocale( "en" );
			}
		}

		return loc;

	}

	/**
	 * Returns the latest or old value, as type String, of the specified attribute of the 
	 * specified bio. 
	 * If the attribute has an attribute domain, returns the label rather than the GUID for
	 * that attribute when the hasAttributeDomain parameter is set to true.
	 * <P>
	 * @param bio The Bio whose attriubte String value is desired.
	 * @param attributeName The name of the attribute whose value in String format is 
	 * @param oldValue If true, then return the old value, if false return the current value. 
	 * @param hasAttributeDomain If true, then returns the label for this attribute by using
	 * the attribute domain for this attribute.  If false, returns the actual value for this
	 * attribute.
	 *
	 * @return String
	 * 
	 * @exception EpiException 
	 */
	private String getAttributeValueString(Bio bio, String attributeName, 
			boolean oldValue, boolean hasAttributeDomain)
	throws EpiException
	{
		Object bioAttributeValue = null;
		String bioAttributeValueStr = null;

		if (BioUtil.isInitialized(bio, attributeName)) {
			if (hasAttributeDomain) {
				bioAttributeValue = bio.getLabel(attributeName, oldValue);
			} else { 
				bioAttributeValue = bio.get(attributeName, oldValue);
			}
		}

		// need to test for and reformat dates for audit trail
		if ((bioAttributeValue != null) && (bio.getBioAttributeMetadata(attributeName).getType() == BioAttributeTypes.DATE_TYPE))
		{
			Calendar tmpDate = (Calendar) bioAttributeValue; 
			bioAttributeValue = formatGMTDate(tmpDate);					//defect:1131

			//defect:1131 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
			//defect:1131 bioAttributeValue = simpleDateFormat.format(tmpDate.getTime());
		}

		if (bioAttributeValue != null ) {
			bioAttributeValueStr = bioAttributeValue.toString();
		} else {
			bioAttributeValueStr = new String();
		}     

		return bioAttributeValueStr;              
	}

	
	private String getAttributeValueString(Bio bio, String attributeName, boolean oldValue)
	throws EpiException
	{
		boolean hasAttributeDomain = false;

		BioType bioType = Metadata.getInstance().getBioType(bio.getTypeName());
		AttributeType attributeType = bioType.getAttributeType(attributeName);

		BioType superclass = bioType;
		while (attributeType == null && superclass.isSubclass()) {
			superclass = bioType.getSuperclass();
			attributeType = superclass.getAttributeType(attributeName);
		}

		if (attributeType instanceof FieldMappedAttributeType)
		{
			if (attributeType.getAttributeDomain() != null) { 
				hasAttributeDomain = true;  
			}
		}
		return getAttributeValueString(bio,attributeName,oldValue,hasAttributeDomain);
	}

	//defect:1131.b
	private String formatGMTDate(Calendar cal){
		if(cal == null){
			return new String();
		}
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		
		StringBuffer gmtStr=new StringBuffer();
		gmtStr.append(year+"-");
		if(month < 10){
			gmtStr.append("0"+month);
		}else{
			gmtStr.append(month);
		}
		gmtStr.append("-");
		if(day < 10){
			gmtStr.append("0"+day);
		}else{
			gmtStr.append(day);
		}
		gmtStr.append(" ");
		if(hour < 10){
			gmtStr.append("0"+hour);
		}else{
			gmtStr.append(hour);
		}
		gmtStr.append(":");
		if(minute < 10){
			gmtStr.append("0"+minute);
		}else{
			gmtStr.append(minute);
		}
		gmtStr.append(":");
		if(second < 10){
			gmtStr.append("0"+second);
		}else{
			gmtStr.append(second);
		}
		
		return gmtStr.toString();
	}
	//defect:1131.e
		
	
	/**
	 * Translates an agent ID to an agent name. This is only valid for S&S
	 * @param unitOfWork
	 * @param agentID
	 * @return
	 */
	private static String getAgentName (UnitOfWork unitOfWork, String agentID) {

		if (agentID == null || agentID.length()==0) {
			return agentID;
		}

		String agentQry = StringGen.concat("agent.agent_id='", agentID, "'");
		Query agentQuery = new Query("agent", agentQry, null);
		try {
			BioCollection agents = unitOfWork.findByQuery(agentQuery);
			if (agents.size() == 1) {
				return agents.elementAt(0).get("agent_name").toString();
			} else {
				return null;
			}
		}
		catch (EpiException e) {
			return null;
		}
	}

	/**
	 * Gets the queue name given a queue ID.  Only valid for S&S.
	 * @param unitOfWork
	 * @param queueID
	 * @return
	 */
	private static String getQueueNameForRouting (UnitOfWork unitOfWork, String queueID){
		//This will take a query id and return the queue name
		if (queueID == null || queueID.length()==0) {
			return queueID;
		}

		final String queryText = StringGen.concat("group_queue.group_queue_id = '", queueID, "'");
		Query Q_Query = new Query("group_queue", queryText, "");
		try {
			BioCollection Q_Coll = unitOfWork.findByQuery(Q_Query);
			return Q_Coll.elementAt(0).get("reference_code").toString();
		}
		catch (EpiException e) {
			return null;
		}
	}

	private static HashMap getWarehouses(UnitOfWork unitOfWork) throws EpiDataException{
		if(warehouseTable == null){    			

//			Query dbQry = new Query("wm_pl_db","wm_pl_db.db_enterprise = 0",""); 
			Query dbQry = new Query("wm_pl_db",null,""); 
			BioCollection dbs = unitOfWork.findByQuery(dbQry);
			HashMap tempWarehseTable = new HashMap();
			if(dbs != null && dbs.size() > 0){
				for(int i = 0; i < dbs.size(); i++){
					tempWarehseTable.put(dbs.elementAt(i).get("db_logid"),dbs.elementAt(i).get("db_name"));
				}
			}    			
			warehouseTable = tempWarehseTable;
		}
		return warehouseTable;
	}

	private boolean isGlobalAuditingOnForWarehouse(String warehouse, UnitOfWork uow) throws EpiDataException{    	 
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		if(userContext.get(SetIntoHttpSessionAction.DB_CONNECTION) == null)
			return true;
		String currDBConn = userContext == null?null:(userContext.get(SetIntoHttpSessionAction.DB_CONNECTION)).toString(); 		
		userContext.put(DB_CONNECTION, getWarehouses(uow).get(warehouse)); 	
		String nsqlConfigSelect = "SELECT NSQLValue FROM nsqlconfig WHERE ConfigKey = 'GLOBAL_AUDITING' AND NSQLValue = '1'";
		boolean isOn = false;
		try {
			isOn = (WmsWebuiValidationSelectImpl.select(nsqlConfigSelect).getRowCount() > 0);
		} catch (DPException e) {			
		}
		userContext.put(DB_CONNECTION, currDBConn); 	
		if(isOn == false)
			return isOn;
		
		//If facility is inactive don't audit
		Query dbQry = new Query("wm_pl_db","wm_pl_db.db_logid = '"+warehouse+"' AND wm_pl_db.isActive = 1",""); 
		BioCollection dbs = uow.findByQuery(dbQry);
		isOn = dbs.size() > 0;
		return isOn;
	}

	private String getRelatedField(BioType bioType){
		
		HashMap relationships = Metadata.getInstance().getRelationshipsBySource(bioType.getPrimaryKeyRecordSetType().getRecordSetPhysicalName()); 
		ArrayList sourceToAuditRelationships = (ArrayList)relationships.get("audit_data");
		RelationshipTypeElement rte = (RelationshipTypeElement)sourceToAuditRelationships.get(0);
		String[] fieldNames = rte.getSourceFieldSet().getFieldNames();
		for(int i = 0; i < fieldNames.length; i++){
			if(!fieldNames[i].equalsIgnoreCase("WHSEID"))
				return fieldNames[i];
		}
		return fieldNames[0];
	}

	private String getStringValueOfBioAttribute(Object bioAttr){
		if(bioAttr == null)
			return null;
		
		if(bioAttr instanceof GregorianCalendar){			
			return dateFormat.format(((GregorianCalendar)bioAttr).getTime());
		}
		
		return bioAttr.toString();
	}
	
	protected int bioAfterDelete(EpnyServiceContext context, Bio bio) throws EpiException {
		
		return bioAfterDeleteWrapper(bio);
	}
	public int bioAfterDeleteWrapper( Bio bio) throws EpiException {
		_log.debug("LOG_SYSTEM_OUT","\n\nIn After Delete!!!\n\n",100L);	
		
		// 269131.b
		BioType bioType = Metadata.getInstance().getBioType(bio.getTypeName());
		if (!bioType.getAuditFlag()) {
			return RET_CONTINUE;
		}
		// 269131.e
		
		EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
		Object isEnterpriseObj = userContext == null?null:userContext.get(SetIntoHttpSessionAction.DB_ISENTERPRISE);		
		boolean isEnterprise = isEnterpriseObj != null && isEnterpriseObj.toString().equals("1")?true:false;
		UnitOfWork unitOfWork = bio.getUnitOfWork();
		String warehouse = (userContext == null || userContext.get(SetIntoHttpSessionAction.DB_USERID) == null)?null:(userContext.get(SetIntoHttpSessionAction.DB_USERID)).toString();
		
		/* 269131.d
		if(warehouse != null)
			purgeAuditRecords(bio.getUnitOfWork());
		*/
		
		boolean isGlobalAuditingOnForCurrentWarehouse = isGlobalAuditingOnForWarehouse(warehouse,unitOfWork);
			
		if(!isEnterprise){
			if(!isGlobalAuditingOnForCurrentWarehouse){
				return RET_CONTINUE;	
			}
		}
				
		// 269131.d BioType bioType = Metadata.getInstance().getBioType(bio.getTypeName());
		
		//If auditing is not implemented on this BIO then do nothing
		
		if (! bioType.getAttributeNames().contains(OWNING_BIO_AUDIT_ATTRIBUTE_NAME) ||
				bio.getTypeName().equals(AUDIT_BIO_NAME) ||
				bio.getTypeName().equals(ROW_LEVEL_AUDIT_BIO_NAME) ||
				bio.getTypeName().equals(FIELD_LEVEL_AUDIT_BIO_NAME)
		)
		{
			//return super.bioAfterDelete( context, bio );
			return RET_CONTINUE;
		}
		
		
		boolean isAuditingOnForBio = warehouse != null?isAuditingOnForBio(bio,unitOfWork):true;		
		
		/* 269131.d
		if (!bioType.getAuditFlag()) 		
			return RET_CONTINUE;
		*/					
		
		if(!isEnterprise){
			if(isAuditingOnForBio)
				verifyDelete(bio,warehouse);
		}else{
			_log.debug("LOG_SYSTEM_OUT","\n\nAbout to iterate whses!!!\n\n",100L);	
			Iterator warehouseItr = getWarehouses(unitOfWork).keySet().iterator();			
			while(warehouseItr.hasNext()){
				String tempWarehouse = (String) warehouseItr.next();
				if(isGlobalAuditingOnForWarehouse(tempWarehouse,unitOfWork) && isAuditingOnForBio(bioType, tempWarehouse, unitOfWork)){
					_log.debug("LOG_SYSTEM_OUT","\n\nverifying delete!!!\n\n",100L);	
					verifyDelete(bio,tempWarehouse);
				}
			}
		}
		
		return RET_CONTINUE;	
	}

	public void verifyDelete(Bio bio,
			String warehouse) throws EpiDataException {

		BioType bioType = Metadata.getInstance().getBioType(bio.getTypeName());		

		//BioMetadata meta = bio.getBioMetadata();
		//Metadata metadata = bioType.getMetadata();

		//RecordSetType type = metadata.getPhysicalRecordSetType(bio.getTypeName());
		RecordSetType type = bioType.getPrimaryKeyRecordSetType();
		
		//String name =type.getName();
		//String otn = type.getObjectTypeName();
		String tableName = type.getRecordSetPhysicalName();
		//String rsn =type.getRecordSetName();
		//String tvn = type.getTableViewName();

		//String tableName = type.getTableName();

		
		HashMap bioAttributesToRecord = getBioAttributesToRecord(bio, warehouse, bioType);
		TreeSet orderedBioAttributes = new TreeSet(bioAttributesToRecord.keySet());
		HashMap keyAttributesToRecord = getBioKeyAttributesToRecord(bio, warehouse, bioType);
		Iterator bioAttributeNameItr = orderedBioAttributes.iterator();
		Iterator keyAttributeNameItr = keyAttributesToRecord.keySet().iterator();
		ArrayList largeFields = new ArrayList();
		ArrayList skippedAttributes = new ArrayList();
		String colList = "";
		String valList = "";
		String whereClause = "";


		for(int i = 1;keyAttributeNameItr.hasNext();i++){
			String auditBioLabelAttr = "KEY"+i+"LABEL";
			String auditBioValueAttr = "KEY"+i+"VALUE";
			String attributeToRecordName = (String)keyAttributeNameItr.next();
			String attributeToRecordLabel = (String)keyAttributesToRecord.get(attributeToRecordName);
			if(attributeToRecordLabel == null)
				attributeToRecordLabel = attributeToRecordName;				
			String attributeToRecordValue = getStringValueOfBioAttribute(bio.get(attributeToRecordName));
		
			whereClause += attributeToRecordName + " = '" + attributeToRecordValue + "' ";
			if(keyAttributeNameItr.hasNext())
				whereClause += " AND ";
			

			colList += " AND " + auditBioValueAttr + " = '" + attributeToRecordValue + "'";
			//colList += " AND " + auditBioLabelAttr + "'" + attributeToRecordLabel + "'";
			
			//colList += ","+auditBioLabelAttr;
			//valList += ",'"+attributeToRecordLabel+"'";
			//colList += ","+auditBioValueAttr;
			//valList += ",'"+attributeToRecordValue+"'";
		}

		
		
					
		String selectSql = "SELECT 1 FROM " + tableName ;
		if (whereClause.length()>0){
			selectSql += " WHERE " + whereClause;
		}
		try {
			EXEDataObject records = WmsWebuiValidationSelectImpl.select(selectSql);
			
			if(records.getRowCount() > 0){
				String bioId = bioType.getID().toString();
				
				String deleteSql = "DELETE FROM AUDITDELETES WHERE BIOID = '" + bioId + "' ";
				
				deleteSql = deleteSql  + colList;
				
				WmsWebuiValidationDeleteImpl.delete(deleteSql);
			}
			
			
		} catch (DPException e) {			
			e.printStackTrace();
		}

	}

}
