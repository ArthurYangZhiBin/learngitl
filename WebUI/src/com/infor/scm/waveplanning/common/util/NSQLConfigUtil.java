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
package com.infor.scm.waveplanning.common.util;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

public class NSQLConfigUtil
{
	

	private static final String IS_OFF = "' and wm_system_settings.NSQLVALUE = '0'";

	private static final String IS_ON = "' and wm_system_settings.NSQLVALUE = '1'";

	private static final String PREAMBLE = "wm_system_settings.CONFIGKEY = '";

	private static final String NSQLCONFIG = "wm_system_settings";

	protected static ILoggerCategory _log = LoggerFactory.getInstance(NSQLConfigUtil.class);
	
	private final UnitOfWorkBean uow;
	private String key;

	public NSQLConfigUtil(StateInterface state)
	{
		uow = state.getTempUnitOfWork();
	}
	
	public NSQLConfigUtil(StateInterface state, String key)
	{
		uow = state.getTempUnitOfWork();
		this.key = key;
	}

	public boolean isOn()
	{
		return isOn(key);
	}

	public boolean isOff()
	{
		return isOff(key);
	}
	
	public boolean isOn(String key)
	{
		Query query = new Query(NSQLCONFIG, PREAMBLE + key + IS_ON, null);
		BioCollectionBean rs = uow.getBioCollectionBean(query);
		try
		{
			if (rs.size() == 1)
			{
				return true;
			}
		} catch (EpiDataException e1)
		{
			e1.printStackTrace();
			_log.error(e1);
			return false;
		}
		return false;
	}

	public boolean isOff(String key)
	{
		Query query = new Query(NSQLCONFIG, PREAMBLE + key + IS_OFF, null);
		BioCollectionBean rs = uow.getBioCollectionBean(query);
		try
		{
			if (rs.size() == 1)
			{
				return true;
			}
		} catch (EpiDataException e1)
		{
			e1.printStackTrace();
			_log.error(e1);
			return false;
		}
		return false;
	}
}
