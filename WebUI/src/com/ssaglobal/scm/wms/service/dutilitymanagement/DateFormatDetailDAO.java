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
package com.ssaglobal.scm.wms.service.dutilitymanagement;

import java.util.ArrayList;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.ssaglobal.scm.wms.service.dutilitymanagement.datecodeformat.DateFormatDetailDTO;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class DateFormatDetailDAO 
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(DateFormatDetailDAO.class);
	public static ArrayList<DateFormatDetailDTO> getDateFormatDetail(ActionContext context, String dateCodeFormat) throws EpiException
	{		
		ArrayList<DateFormatDetailDTO> dateFormatDetails = new ArrayList<DateFormatDetailDTO>();
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();	
		
		BioCollectionBean listCollection = null;
		String sQueryString = "(wm_dateformatdetail.DATECODEFORMAT = '"+dateCodeFormat+"' ) ";

		_log.debug("LOG_SYSTEM_OUT","sQueryString = "+ sQueryString,100L);
		Query bioQuery = new Query( "wm_dateformatdetail", sQueryString, null );
		listCollection = uowb.getBioCollectionBean(bioQuery);
		
		for ( int idx = 0; idx < listCollection.size(); ++idx )
		{
        	DateFormatDetailDTO dateFormatDetail = new DateFormatDetailDTO();
        	BioBean row = (BioBean)listCollection.elementAt(idx);
        	
        	dateFormatDetail.setDateElement(row.getString("DATEELEMENT"));
        	dateFormatDetail.setBegPosition( Integer.parseInt(row.getString("BEGPOSITION")) );
        	dateFormatDetail.setEndPosition( Integer.parseInt(row.getString("ENDPOSITION")) );
        	dateFormatDetail.setDateCodeFormateSequence( Integer.parseInt(row.getString("DATECODEFORMATSEQUENCE")));
        	dateFormatDetail.setFieldType(row.getString("FIELDTYPE"));
        	dateFormatDetail.setDateType(row.getString("DATETYPE"));
        	dateFormatDetail.setHowToProcess(row.getString("HOWTOPROCESS"));
        	dateFormatDetails.add(dateFormatDetail);     
		}
        return dateFormatDetails;
	}
}
