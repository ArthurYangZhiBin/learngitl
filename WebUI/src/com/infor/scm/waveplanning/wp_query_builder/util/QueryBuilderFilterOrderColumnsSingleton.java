/**
 * ---Begin Copyright Notice---20090105T1353
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2010 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */
package com.infor.scm.waveplanning.wp_query_builder.util;

import java.util.HashMap;
import java.util.StringTokenizer;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.util.exceptions.EpiException;

/**
 * TODO Document QueryBuilderFilterOrderColumnsSingleton class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class QueryBuilderFilterOrderColumnsSingleton {
	
	private static QueryBuilderFilterOrderColumnsSingleton columns = null;
	private HashMap<String, String> columnsMap = new HashMap<String, String>();
	private static final String ORDERHEADER = "H";
	private static final String ORDERDETAIL = "D";
	
	
	private QueryBuilderFilterOrderColumnsSingleton(ActionContext ctx) throws EpiException{
		UnitOfWorkBean uow = ctx.getState().getTempUnitOfWork();
		Query qry = new Query("wm_codesdetail", "wm_codesdetail.LISTNAME = 'ORDRFLD'", null);
		BioCollectionBean results = uow.getBioCollectionBean(qry);
		int size = results.size();
		BioBean bioBean;
		String columnName = "";
		for(int i=0;i<size;i++){
			bioBean = results.get(""+i);
			columnName = bioBean.getString("SHORT");
			columnsMap.put(columnName, this.getColumsWithTableName(columnName));
		}

		
	}
	public static QueryBuilderFilterOrderColumnsSingleton getQueryBuilderFilterOrderColumnsSingleton(ActionContext ctx) throws EpiException{
		if (columns != null){
			return columns;
		}else{
			return new QueryBuilderFilterOrderColumnsSingleton(ctx);
		}
	}
	public HashMap<String, String> getColumnsMap() {
		return columnsMap;
	}

	public void setColumnsMap(HashMap<String, String> columnsMap) {
		this.columnsMap = columnsMap;
	}

	private String getColumsWithTableName(String column){
		StringTokenizer st = new StringTokenizer(column, "_");
		 String headerOrDetail = st.nextToken().trim();
		 String columnName = st.nextToken().trim();
		 if(ORDERHEADER.equalsIgnoreCase(headerOrDetail)){
			 return "orders."+columnName+"&String";
		 }
		 if(ORDERDETAIL.equalsIgnoreCase(headerOrDetail)){
			 return "orderdetail."+columnName+"&String";
		 }
		return null;
	}

}
