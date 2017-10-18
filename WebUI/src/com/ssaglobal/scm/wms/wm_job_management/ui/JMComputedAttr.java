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
package com.ssaglobal.scm.wms.wm_job_management.ui;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.epiphany.shr.data.error.EpiDataException;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class JMComputedAttr implements ComputedAttributeSupport
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(JMComputedAttr.class);

	public JMComputedAttr()
    {
    }

    public Object get(Bio bio, String attributeName, boolean isOldValue)
        throws EpiDataException
    {

       //Qty Ordered on the ASN Detail Receipts tab    
    	_log.debug("LOG_SYSTEM_OUT","**********In side WORKORDER COMPUTED *******Inside the IF = "+ attributeName,100L);
    	
       if("QUANTITY".equalsIgnoreCase(attributeName)){
          	BioCollection workOrderCollection = bio.getBioCollection("WORKORDER");
          	_log.debug("LOG_SYSTEM_OUT","biocollection workorder= "+workOrderCollection.toString(),100L);
          	_log.debug("LOG_SYSTEM_OUT","**********In side WORKORDER COMPUTED *******Inside the IF = "+ workOrderCollection.size(),100L);
          	if (workOrderCollection.size()!= 0){
           		_log.debug("LOG_SYSTEM_OUT","**********TOTALQTYEXPECTED*******Inside the IF",100L);

           		Object objqty = workOrderCollection.sum("QUANTITY");	//Sum of qty Expected
           		_log.debug("LOG_SYSTEM_OUT","**********TOTALQTYEXPECTED*******"+ objqty,100L);
         	   try
        	   {
       				return LocaleUtil.formatValues(objqty.toString(),LocaleUtil.TYPE_QTY);//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
        	   }catch (Exception e)
        	   {
    			// Handle Exceptions 
        		   e.printStackTrace();
        	   }
           	}
          }
       return "";
    }

    public boolean supportsSet(String bioTypeName, String attributeName)
    {
        return true;
    }

    public void set(Bio bio1, String s, Object obj, boolean flag)
        throws EpiDataException
    {
    }

}
