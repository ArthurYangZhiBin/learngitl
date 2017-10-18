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
package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.computed;

import java.util.ArrayList;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.data.error.EpiDataException;
import com.infor.scm.waveplanning.common.util.BioAttributeUtil;

public class WPWMComputedUtil
{

	public static StringBuilder retrieveOrderKeys(UnitOfWork uow, String waveKey) throws EpiDataException
	{
		Query waveDetailQuery = new Query("wm_wavedetail", "wm_wavedetail.WAVEKEY = '" + waveKey + "'", null);
		BioCollection rs = uow.findByQuery(waveDetailQuery);
		ArrayList<String> orderKeys = new ArrayList<String>(rs.size());
		for (int i = 0; i < rs.size(); i++)
		{
			Bio wavedetail = rs.elementAt(i);
			orderKeys.add(BioAttributeUtil.getString(wavedetail, "ORDERKEY"));
		}
		StringBuilder listOfOrderKeys = new StringBuilder();
		//flatten orderKeys
		for (int i = 0; i < orderKeys.size(); i++)
		{
			if (i > 0)
			{
				listOfOrderKeys.append("," + "'" + orderKeys.get(i) + "'");
			}
			else
			{
				listOfOrderKeys.append("'" + orderKeys.get(i) + "'");
			}
		}
		return listOfOrderKeys;
	}
}
