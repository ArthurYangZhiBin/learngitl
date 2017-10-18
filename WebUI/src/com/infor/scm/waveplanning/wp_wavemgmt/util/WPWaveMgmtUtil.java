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
package com.infor.scm.waveplanning.wp_wavemgmt.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;

public class WPWaveMgmtUtil
{

	public static void deleteRecordsInWPAddOrderTempForASpecificInteraction(StateInterface state, final String interactionId, String facility, Integer waveKey) throws EpiDataException, EpiException
	{
		UnitOfWorkBean uow;
		final Query deleteQuery = new Query("wp_addordertemp", "wp_addordertemp.INTERACTIONID = '" + interactionId
				+ "' and wp_addordertemp.WAVEKEY = '" + waveKey + "' and wp_addordertemp.WHSEID ='" + facility + "'",
				null);
		uow = state.getDefaultUnitOfWork();
		BioCollectionBean deleteSet = uow.getBioCollectionBean(deleteQuery);
		for (int i = 0; i < deleteSet.size(); i++)
		{
			deleteSet.get("" + i).delete();
		}
		uow.saveUOW(true);
	}

	public static void clearTempTableBasedOnAddDate(StateInterface state, final String tempTable) throws EpiDataException, EpiException
	{
		UnitOfWorkBean uow;
		uow = state.getDefaultUnitOfWork();
		GregorianCalendar gc = new GregorianCalendar();
		gc.add(Calendar.DAY_OF_WEEK, -2);
		Query qry = new Query(tempTable, tempTable + ".ADDDATE < @DATE['" + gc.getTimeInMillis() + "']", "");
		BioCollectionBean criteriaCollection = uow.getBioCollectionBean(qry);
		if (criteriaCollection != null)
		{
			for (int i = 0; i < criteriaCollection.size(); i++)
			{
				criteriaCollection.get("" + i).delete();
			}
		}
		uow.saveUOW();
	}
	
	public static void clearTempTableBasedOnEditDate(StateInterface state, final String tempTable) throws EpiDataException, EpiException
	{
		UnitOfWorkBean uow;
		uow = state.getDefaultUnitOfWork();
		GregorianCalendar gc = new GregorianCalendar();
		gc.add(Calendar.DAY_OF_WEEK, -2);
		Query qry = new Query(tempTable, tempTable + ".EDITDATE < @DATE['" + gc.getTimeInMillis() + "']", "");
		BioCollectionBean criteriaCollection = uow.getBioCollectionBean(qry);
		if (criteriaCollection != null)
		{
			for (int i = 0; i < criteriaCollection.size(); i++)
			{
				criteriaCollection.get("" + i).delete();
			}
		}
		uow.saveUOW();
	}
	
	public static void clearTempTableBasedOnAddDate(StateInterface state, final String tempTable, final String interactionID) throws EpiDataException, EpiException
	{
		UnitOfWorkBean uow;
		uow = state.getDefaultUnitOfWork();
		GregorianCalendar gc = new GregorianCalendar();
		gc.add(Calendar.DAY_OF_WEEK, -2);
		Query qry = new Query(tempTable, tempTable + ".ADDDATE < @DATE['" + gc.getTimeInMillis() + "'] and " + tempTable + ".INTERACTIONID != '" + interactionID + "'", "");
		BioCollectionBean criteriaCollection = uow.getBioCollectionBean(qry);
		if (criteriaCollection != null)
		{
			for (int i = 0; i < criteriaCollection.size(); i++)
			{
				criteriaCollection.get("" + i);
			}
		}
		uow.saveUOW();
	}

}
