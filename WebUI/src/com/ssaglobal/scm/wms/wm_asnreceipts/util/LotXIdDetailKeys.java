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
package com.ssaglobal.scm.wms.wm_asnreceipts.util;

import java.util.ArrayList;

public class LotXIdDetailKeys
{

	ArrayList<LotXIdDetailKey> keys;

	public LotXIdDetailKeys()
	{
		keys = new ArrayList();
	}

	public void add(Object lotxidkey, Object lotxidlinenumber)
	{
		keys.add(new LotXIdDetailKey(lotxidkey, lotxidlinenumber));
	}

	public String getQueryString()
	{
		StringBuffer query = new StringBuffer();
		for (int i = 0; i < keys.size(); i++)
		{
			if (i > 0)
			{
				query.append(" or ");
			}
			query.append(" (lotxiddetail.LOTXIDKEY = '" + keys.get(i).lotxidkey + "' and lotxiddetail.LOTXIDLINENUMBER = '" + keys.get(i).lotxidlinenumber + "') ");

		}

		return query.toString();
	}

	class LotXIdDetailKey
	{

		Object lotxidkey;

		Object lotxidlinenumber;

		public LotXIdDetailKey(Object lkey, Object llnum)
		{
			this.lotxidkey = lkey;
			this.lotxidlinenumber = llnum;
		}

	}

}
