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
import com.epiphany.shr.ui.action.ActionContext;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.service.dutilitymanagement.datecodeformat.DateFormatCustomDTO;
import com.agileitp.forte.framework.TextData;

public class DateFormatCustomDAO 
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(DateFormatCustomDAO.class);
	private DateFormatCustomDAO()
	{		
	}
	
	public static ArrayList<DateFormatCustomDTO> getDateFormatCustoms(ActionContext context, String customCodeFormat) throws EpiException
	{	
       
		ArrayList<DateFormatCustomDTO> dateFormatCustomList = new ArrayList<DateFormatCustomDTO>();
		String sQueryString = " SELECT * FROM dateformatcustom where CUSTOMCODEFORMAT = '"+customCodeFormat+"' ";
		EXEDataObject testObject = WmsWebuiValidationSelectImpl.select(sQueryString);
		_log.debug("LOG_SYSTEM_OUT","sQueryString = "+ sQueryString,100L);

		for(int idx = 1; idx < testObject.getRowCount() + 1; idx++){
			testObject.setRow(idx);
          	DateFormatCustomDTO dateFormatCustom = new DateFormatCustomDTO();
            
			Object monthElementObj = testObject.getAttribValue(new TextData("MONTHELEMENT"));
			String monthElement = monthElementObj == null?null:monthElementObj.toString();
          	
			Object monthDescObj = testObject.getAttribValue(new TextData("MONTHDESCRIPTION"));
			String monthDesc = monthDescObj == null?null:monthDescObj.toString();
			
           	dateFormatCustom.setMonthElement(Integer.parseInt(monthElement));
            dateFormatCustom.setMonthDescription(monthDesc);
           	dateFormatCustomList.add(dateFormatCustom); 

		} 

        return dateFormatCustomList;
	}
}
