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

import com.epiphany.shr.data.beans.BioService;
import com.epiphany.shr.data.beans.BioServiceFactory;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.infor.scm.waveplanning.common.util.BioAttributeUtil;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

public class WPWMPreallocatedPickDetailBioCollection implements ComputedAttributeSupport
{
	private static final String SORT_ORDER = "wm_preallocatepickdetail.ORDERKEY ASC, wm_preallocatepickdetail.ORDERLINENUMBER ASC";

	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPWMPreallocatedPickDetailBioCollection.class);

	public Object get(Bio bio, String attributeName, boolean isOldValue) throws EpiDataException
	{
		BioServiceFactory serviceFactory;
		BioService bioService = null;
		UnitOfWork uow = null;
		boolean serviceFailed = false;
		try
		{
			serviceFactory = BioServiceFactory.getInstance();
			bioService = serviceFactory.create("webui");
			uow = bioService.getUnitOfWork();
		} catch (Exception e)
		{
			serviceFailed = true;
			if (uow != null)
			{
				uow.close();
			}
			if (bioService != null)
			{
				bioService.remove();
			}
		}

		if (serviceFailed == false)
		{
			try
			{
				//retrieve WaveKey
				String waveKey = BioAttributeUtil.getString(bio, "WAVEKEY");
				//query wm_pickdetail by orderkey
				StringBuilder listOfOrderKeys = WPWMComputedUtil.retrieveOrderKeys(uow, waveKey);
				_log.debug("LOG_INFO_EXTENSION_WPWMPreallocatedPickDetailBioCollection_get", "OrderKeys = " + listOfOrderKeys,
						SuggestedCategory.NONE);
				final String orderQuery = "wm_preallocatepickdetail.ORDERKEY IN (" + listOfOrderKeys + ")";
				_log.debug("LOG_INFO_EXTENSION_WPWMPreallocatedPickDetailBioCollection_get", orderQuery,
						SuggestedCategory.NONE);
				Query orderDetailQuery = new Query("wm_preallocatepickdetail", orderQuery, SORT_ORDER);
				BioCollection rs = uow.findByQuery(orderDetailQuery);
				return rs;
			} catch (UnitOfWorkException sql)
			{

				uow.close();
				throw sql;
			} finally
			{
				if (uow != null)
				{
					uow.close();
				}

				if (bioService != null)
				{
					bioService.remove();
				}

			}
		}
		else
		{

			return null;
		}
	}

	public void set(Bio arg0, String arg1, Object arg2, boolean arg3) throws EpiDataException
	{
		// TODO Auto-generated method stub

	}

	public boolean supportsSet(String arg0, String arg1)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
