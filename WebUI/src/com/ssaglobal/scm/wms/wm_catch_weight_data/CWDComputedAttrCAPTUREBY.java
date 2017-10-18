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
package com.ssaglobal.scm.wms.wm_catch_weight_data;

import java.util.List;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.metadata.AttributeDomainExtensionBase;
import com.epiphany.shr.ui.metadata.DropdownContentsContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.epiphany.shr.data.bio.Query;

/**
 * Modification History
 * 10/21/2008	AW  Machine#:2093019 SDIS:SCM-00000-05440
 * 					Initial version. The previous version of catch weight data could only 
 * 					be captured by EA, CS or PL. This computed attribute domain was added 
 * 					to set the values based on the packkey.
 * 
 */
public class CWDComputedAttrCAPTUREBY extends AttributeDomainExtensionBase{

	private static String LOTXIDHEADER_BIO = "wm_lotxidheader";
	private static String SKU_BIO = "sku";
	
	private static String SOURCEKEY = "SOURCEKEY";
	private static String SRCLINENUM = "SOURCELINENUMBER";
	private static String SKU = "SKU";
	private static String STORERKEY = "STORERKEY";
	private static String PACKKEY = "PACKKEY";
	
	public int execute(DropdownContentsContext context, List values, List labels) throws EpiDataException, EpiException{

		String pack = getPack(context);
		
		String query = "SELECT '1' AS CODE, PACKUOM1 FROM PACK WHERE PACKUOM1 <> ' ' AND PACKKEY='" +pack +"' UNION " +
		"SELECT '0' AS CODE, PACKUOM3 FROM PACK WHERE PACKUOM3 <> ' ' AND PACKKEY='" +pack +"' UNION " +
		"SELECT '2' AS CODE, PACKUOM4 FROM PACK WHERE PACKUOM4 <> ' ' AND PACKKEY='" +pack +"'";
		
		//Execute query
		EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
		
		//Build dropdown from results
		if(results.getRowCount()>0){
			for(int i=0; i<results.getRowCount(); i++){
				values.add(results.getAttribValue(1).getAsString());
				labels.add(results.getAttribValue(2).getAsString());
								
				results.getNextRow();
			}
		}
		return RET_CONTINUE;
	}
		

	private String getPack(DropdownContentsContext context) throws EpiException
	{
				
		RuntimeFormInterface form= context.getState().getCurrentRuntimeForm();
		String pack ="";
		String sourceKey ="";
		String sourceLineNum= "";
		String sku="";
		Bio bio = null;
		DataBean focus = form.getFocus();
		if(focus instanceof BioCollection)
		{
			BioCollection bioColl = (BioCollection)focus;
			bio = bioColl.elementAt(0);
		}
		else if(focus instanceof BioBean)
		{
			bio = (Bio)focus;
		}
		
			sourceKey =bio.get(SOURCEKEY).toString();
			sourceLineNum = bio.get(SRCLINENUM).toString();
			sku = bio.get(SKU).toString();
			
			
			String query = LOTXIDHEADER_BIO + "." +SOURCEKEY + "='" +sourceKey + "' AND " + 
						   LOTXIDHEADER_BIO + "." +SRCLINENUM + "='" +sourceLineNum + "' AND " +
						   LOTXIDHEADER_BIO + "." +SKU + "='" +sku +"'";
			Query qry = new Query(LOTXIDHEADER_BIO, query, "");
			UnitOfWorkBean uowb = context.getState().getDefaultUnitOfWork();
			BioCollectionBean listCollection = uowb.getBioCollectionBean(qry);
			
			if(listCollection.size() >0)
			{
				String owner = listCollection.elementAt(0).getString(STORERKEY);				
				String skuQuery = SKU_BIO + "." +STORERKEY + "='" +owner + "' AND " + 
				   SKU_BIO + "." +SKU + "='" +sku + "' " ;
				
				Query skuQry = new Query(SKU_BIO, skuQuery, "");
				BioCollectionBean skuList = uowb.getBioCollectionBean(skuQry);
					
				if(skuList.size() == 1)
				  pack = skuList.elementAt(0).getString(PACKKEY);	
					
				
			}
			
		return pack;
	}
}