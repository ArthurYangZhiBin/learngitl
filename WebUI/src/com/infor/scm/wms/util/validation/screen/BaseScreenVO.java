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
package com.infor.scm.wms.util.validation.screen;

import java.lang.reflect.InvocationTargetException;

import com.infor.scm.wms.util.datalayer.WMSDataLayerException;

public abstract class BaseScreenVO {
	
	public Object getValueOfColumn(String column) throws WMSDataLayerException{
		String getterName = "get";
		getterName += column.substring(0, 1).toUpperCase();
		if(column.length() > 1)
			getterName += column.substring(1).toLowerCase();
		
		try {
			return this.getClass().getMethod(getterName, null).invoke(this, null);
		} catch (IllegalArgumentException e) {
			throw new WMSDataLayerException(e);
		} catch (SecurityException e) {
			throw new WMSDataLayerException(e);
		} catch (IllegalAccessException e) {
			throw new WMSDataLayerException(e);
		} catch (InvocationTargetException e) {
			throw new WMSDataLayerException(e);
		} catch (NoSuchMethodException e) {
			throw new WMSDataLayerException(e);
		}
	}
	
	public void setValueOfColumn(String column, Object value) throws WMSDataLayerException{
		String setterName = "set";
		setterName += column.substring(0, 1).toUpperCase();
		if(column.length() > 1)
			setterName += column.substring(1).toLowerCase();
		
		Class[] setterParamClasses = {value.getClass()};
		Object[] setterParams = {value};
		try {
			this.getClass().getMethod(setterName, setterParamClasses).invoke(this, setterParams);
		} catch (IllegalArgumentException e) {
			throw new WMSDataLayerException(e);
		} catch (SecurityException e) {
			throw new WMSDataLayerException(e);
		} catch (IllegalAccessException e) {
			throw new WMSDataLayerException(e);
		} catch (InvocationTargetException e) {
			throw new WMSDataLayerException(e);
		} catch (NoSuchMethodException e) {
			throw new WMSDataLayerException(e);
		}
	}
}