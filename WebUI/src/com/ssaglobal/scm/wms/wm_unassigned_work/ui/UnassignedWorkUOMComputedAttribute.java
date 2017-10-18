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
package com.ssaglobal.scm.wms.wm_unassigned_work.ui;

import java.util.List;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.metadata.DropdownContentsContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.util.exceptions.EpiException;

public class UnassignedWorkUOMComputedAttribute extends com.epiphany.shr.ui.metadata.AttributeDomainExtensionBase
{
	protected int execute(DropdownContentsContext context, List values, List labels) throws EpiException
	{
		try
		{
			Query codelkupQuery = new Query("codelkup", "codelkup.LISTNAME = '" + "codelkup.LISTNAME = 'CWUOM'", null);
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
			BioCollectionBean results = uow.getBioCollectionBean(codelkupQuery);
			for (int i = 0; i < results.size(); i++)
			{
				values.add(results.get(String.valueOf(i)).getValue("CODE").toString());
				values.add(results.get(String.valueOf(i)).getValue("DESCRIPTION").toString());
			}

		} catch (Exception e)
		{
			e.printStackTrace();
			return RET_CANCEL;
		}
		return RET_CONTINUE;
	}

}
