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
package com.ssaglobal.scm.wms.wm_review_physical.ui;

//Import 3rd party packages and classes
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.uiextensions.UOMDefaultValue;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;

public class ReviewPhysicalFieldValidation extends ActionExtensionBase{
	//Static Table Names
	private static String ITEM_TABLE = "sku";
	private static String PACK_TABLE = "wm_pack";
	
	//Static Attribute Names
	private static String ITEM = "SKU";
	private static String OWNER = "STORERKEY";
	private static String PACK = "PACKKEY";
	private static String UOM = "UOM";
	private static String DEFAULT_UOM = "PACKUOM3";
	
	
	//Static Error Names
	private static String ERROR_NULL_VALUE = "WMEXP_SO_ILQ_Null";
	private static String ERROR_FOREIGN_INVALID = "WMEXP_SO_ILQ_FOREIGN_INVALID";
	private static String ERROR_MESSAGE = "WMEXP_RP_TEAM_VALIDATION";
	
	//Perform field level validations on Review Physical screen
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		RuntimeFormWidgetInterface source = context.getSourceWidget();
		String sourceName = source.getName();
		DataBean focus = source.getForm().getFocus();
		if(sourceName.equals("TEAM")){
			//Team Validation
			String sourceValue = source.getDisplayValue();
			if(sourceValue!=null){
				sourceValue = sourceValue.toUpperCase();
				if(sourceValue.equals("A") || sourceValue.equals("B")){
					if(focus.isTempBio()){
						QBEBioBean qbe = (QBEBioBean)focus;
						qbe.set("TEAM", sourceValue);
						result.setFocus(qbe);
					}else{
						BioBean bio = (BioBean)focus;
						bio.set("TEAM", sourceValue);
						result.setFocus(bio);
					}
				}else{
					throw new FormException(ERROR_MESSAGE, null);
				}
			}
		} else{
			//Item validation
			StateInterface state = context.getState();
			UnitOfWorkBean uowb = state.getDefaultUnitOfWork();//AW 10/14/2008 Machine#:2093019 SDIS:SCM-00000-05440
			RuntimeFormInterface currentForm = state.getCurrentRuntimeForm();
			RuntimeFormWidgetInterface itemWidget = currentForm.getFormWidgetByName(ITEM);
			String newItemValue = itemWidget.getDisplayValue();
			if(newItemValue!=null){
				newItemValue = newItemValue.toUpperCase();
				String foreign[] = new String[2];
				RuntimeFormWidgetInterface ownerWidget = currentForm.getFormWidgetByName(OWNER);
				String ownerValue = ownerWidget.getDisplayValue();
				if(ownerValue==null){
					foreign[0]=colonStrip(readLabel(ownerWidget));
					throw new FormException(ERROR_NULL_VALUE, foreign);
				}
				String query = ITEM_TABLE+"."+ITEM+"='"+newItemValue+"' and "+ITEM_TABLE+"."+OWNER+"='"+ownerValue+"'";
				Query qry = new Query(ITEM_TABLE, query, null);
				BioCollectionBean list = uowb.getBioCollectionBean(qry);
				if(list.size()<1){
					UOMDefaultValue.fillDropdown(state, UOM, UOMMappingUtil.PACK_STD); //AW 10/14/2008 Machine#:2093019 SDIS:SCM-00000-05440
					foreign[0]=colonStrip(readLabel(itemWidget));
					foreign[1]=colonStrip(readLabel(ownerWidget));
					throw new FormException(ERROR_FOREIGN_INVALID, foreign);
				}
				String packValue = list.get("0").getValue(PACK).toString();
				query = PACK_TABLE+"."+PACK+"='"+packValue+"'";
				qry = new Query(PACK_TABLE, query, null);
				BioCollectionBean packList = uowb.getBioCollectionBean(qry);
				String uomValue = packList.get("0").getValue(DEFAULT_UOM).toString();
				if(focus.isTempBio()){
					QBEBioBean qbe = (QBEBioBean)focus;
					qbe.set(PACK, packValue);
					qbe.set(UOM, uomValue);
					qbe.set(ITEM, newItemValue);
				}else{
					BioBean bio = (BioBean)focus;
					bio.set(PACK, packValue);
					bio.set(UOM, uomValue);
					bio.set(ITEM, newItemValue);
				}
			}else{
				if(focus.isTempBio()){
					QBEBioBean qbe = (QBEBioBean)focus;
					qbe.set(PACK, UOMMappingUtil.PACK_STD);//AW 10/14/2008 Machine#:2093019 SDIS:SCM-00000-05440
					qbe.set(UOM, UOMMappingUtil.getPACKUOM3Val(UOMMappingUtil.PACK_STD, uowb)); //AW 10/14/2008 Machine#:2093019 SDIS:SCM-00000-05440
				}else{
					BioBean bio = (BioBean)focus;
					bio.set(PACK, UOMMappingUtil.PACK_STD);//AW 10/14/2008 Machine#:2093019 SDIS:SCM-00000-05440
					bio.set(UOM, UOMMappingUtil.getPACKUOM3Val(UOMMappingUtil.PACK_STD, uowb));//AW 10/14/2008 Machine#:2093019 SDIS:SCM-00000-05440
				}
			}
		}
		// Result Found
		return RET_CONTINUE;
	}
	
	private String colonStrip(String label){
		Pattern pattern = Pattern.compile("\\:");
		Matcher matcher = pattern.matcher(label);
		return 	matcher.replaceAll("");
	}
	
	private String readLabel(RuntimeFormWidgetInterface widget){
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widget.getLabel("label",locale);
	}
}