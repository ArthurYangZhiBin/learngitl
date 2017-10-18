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
package com.infor.scm.wms.util.datalayer.resultwrappers;

import java.security.InvalidParameterException;

import com.epiphany.shr.data.beans.ejb.BioCollectionEpistub;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.error.EpiDataException;
import com.infor.scm.wms.util.datalayer.WMSDataLayerException;

public class DataLayerResultWrapper extends BaseResultWrapper{

	public DataLayerResultWrapper (Object result) throws WMSDataLayerException{
		
		if(result != null){			
			if(!(result instanceof BioCollectionEpistub) )
				throw new InvalidParameterException("ERROR: Expected a BioCollection, instead got "+result.getClass().getName());			
		}
		this.result = result;
		setSize();
	}
	
	private Bio get(int index) throws WMSDataLayerException {
		Bio record = null;
		if(index < 0 || index > (size - 1) ){
			throw new IndexOutOfBoundsException("ERROR: index "+index+" is not valid for a BioCollection of size "+size);
		}		
		try {
			record = ((BioCollection)result).elementAt(index);
		} catch (EpiDataException e) {	
			throw new WMSDataLayerException(e);			
		}
		return record;
	}
	
	
	protected void setSize() throws WMSDataLayerException {
		if(result == null){
			size = 0;
		}
		else{
			try {
				size = ((BioCollection)result).size();
			} catch (EpiDataException e) {
				throw new WMSDataLayerException(e);		
			}
		}
	}

	public Object getValue(int index, Object field) throws WMSDataLayerException {
		Object value = null;
		try {
			value = get(index).get(field.toString());
		} catch (EpiDataException e) {
			throw new WMSDataLayerException(e);
		}
		return value;
	}

	
}