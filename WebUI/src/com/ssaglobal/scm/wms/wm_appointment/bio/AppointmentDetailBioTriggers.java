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

package com.ssaglobal.scm.wms.wm_appointment.bio;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.ui.model.UserBean;
import com.epiphany.shr.ui.model.UserInterface;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.util.BioUtil;
import com.ssaglobal.scm.wms.util.StringUtils;

/*
 * Descriptive Text to describe the extension you should state the event being trapped and list any parameters expected to be
 * provided from the meta <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class AppointmentDetailBioTriggers extends com.epiphany.shr.data.bio.extensions.BioExtensionBase
{
	static HashMap<String, String> receiptCarrierMapping;

	static HashMap<String, String> wmordersCarrierMapping;

	static HashMap<String, String> wmloadidCarrierMapping;

	static
	{
		if (receiptCarrierMapping == null)
		{
			receiptCarrierMapping = new HashMap<String, String>();
			receiptCarrierMapping.put("CARRIERKEY", "STORERKEY");
			receiptCarrierMapping.put("CARRIERNAME", "COMPANY");
			receiptCarrierMapping.put("CARRIERADDRESS1", "ADDRESS1");
			receiptCarrierMapping.put("CARRIERADDRESS2", "ADDRESS2");
			receiptCarrierMapping.put("CARRIERCITY", "CITY");
			receiptCarrierMapping.put("CARRIERSTATE", "STATE");
			receiptCarrierMapping.put("CARRIERZIP", "ZIP");
			receiptCarrierMapping.put("CarrierCountry", "COUNTRY");
			receiptCarrierMapping.put("CarrierPhone", "PHONE1");
		}

		if (wmordersCarrierMapping == null)
		{
			wmordersCarrierMapping = new HashMap<String, String>();
			wmordersCarrierMapping.put("CarrierCode", "STORERKEY");
			wmordersCarrierMapping.put("CarrierName", "COMPANY");
			wmordersCarrierMapping.put("CarrierAddress1", "ADDRESS1");
			wmordersCarrierMapping.put("CarrierAddress2", "ADDRESS2");
			wmordersCarrierMapping.put("CarrierCity", "CITY");
			wmordersCarrierMapping.put("CarrierState", "STATE");
			wmordersCarrierMapping.put("CarrierZip", "ZIP");
			wmordersCarrierMapping.put("CarrierCountry", "COUNTRY");
			wmordersCarrierMapping.put("CarrierPhone", "PHONE1");
		}

		if (wmloadidCarrierMapping == null)
		{
			wmloadidCarrierMapping = new HashMap<String, String>();
			wmloadidCarrierMapping.put("CARRIERID", "STORERKEY");
		}
	}

	/**
	 * Returning CANCEL from this will stop the update
	 * Creating new bios or changing bios in the Bio's unit of work
	 * will cause them to be added to the Bios to be processed 
	 * in this UnitOfWork.Insert();
	     * <P>
	 * @param context The EpnyServiceContext for this BioExtension instance
	 * @param bio The Bio for this BioExtension instance
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
	     * @exception EpiException 
	 */
	@Override
	protected int bioBeforeUpdate(EpnyServiceContext context, Bio bio) throws EpiException
	{

		updateRelatedAppointmentDetailDocuments(bio);

		// Replace the following line with your code,
		//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		//	 as appropriate
		// Leave the line if the code is not going to handle 
		//  bioBeforeUpdate events

		return RET_CONTINUE;
	}

	/**
	 * Returning CANCEL from this will stop the operation 
	 * Cannot make any updates to the unit of work (create new bios/update existing bios) 
	 * during this extension
	     * <P>
	 * @param context The EpnyServiceContext for this BioExtension instance
	 * @param bio The Bio for this BioExtension instance
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
	     * @exception EpiException 
	 */
	@Override
	protected int bioFinalBeforeUpdate(EpnyServiceContext context, Bio bio) throws EpiException
	{

		// Replace the following line with your code,
		//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		//	 as appropriate
		// Leave the line if the code is not going to handle 
		//  bioBeforeUpdate events

		return super.bioFinalBeforeUpdate(context, bio);
	}

	/**
	 * Returning CANCEL from this will stop the insert
	 * Creating new bios or changing bios in the Bio's unit of work
	 * will cause them to be added to the Bios to be processed 
	 * in this UnitOfWork.save();
	     * <P>
	 * @param context The EpnyServiceContext for this BioExtension instance
	 * @param bio The Bio for this BioExtension instance
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
	     * @exception EpiException 
	 */
	@Override
	protected int bioBeforeInsert(EpnyServiceContext context, Bio bio) throws EpiException
	{
		updateRelatedAppointmentDetailDocuments(bio);

		// Replace the following line with your code,
		//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		//	 as appropriate
		// Leave the line if the code is not going to handle 
		//  bioBeforeInsert events

		return RET_CONTINUE;
	}

	private void updateRelatedAppointmentDetailDocuments(Bio bio) throws EpiDataException
	{
		Bio appBio = (Bio) bio.get("APPOINTMENT");
		//if bio type is INBOUND
		//update receipt.ArrivalDateTime,DOOR,APPOINTMENTKEY
		if (isInbound(bio))
		{
			BioCollection rs = bio.getUnitOfWork().findByQuery(new Query("receipt", "receipt.RECEIPTKEY = '" + bio.get("SOURCEKEY") + "'", null));
			for (int i = 0; i < rs.size(); i++)
			{
				Bio associatedReceipt = rs.elementAt(i);

				associatedReceipt.set("APPOINTMENTKEY", appBio.get("APPOINTMENTKEY"));
				associatedReceipt.set("DOOR", appBio.get("DOOR"));
//				associatedReceipt.set("ArrivalDateTime", appBio.get("GMTSTARTDATEANDTIME"));
				associatedReceipt.set("EXPECTEDRECEIPTDATE", appBio.get("GMTSTARTDATEANDTIME"));
				associatedReceipt.set("TRANSPORTATIONSTATUS", appBio.get("STATUS"));
				associatedReceipt.set("TrailerNumber", appBio.get("TRAILER"));
				associatedReceipt.set("TrailerType", appBio.get("TrailerType"));

				//update carrier info
				String carrier = BioUtil.getString(appBio, "CARRIER");
				updateCarrier(associatedReceipt, carrier, receiptCarrierMapping);

			}
		}

		//if bio type is OUTBOUND
		//update wm_orders.DeliveryDate2,DOOR,APPOINTMENTKEY
		//if ORDER has LOADID, update loadhdr.APPOINTMENTTIME,DOOR, APPOINTMENTKEY
		if (isOutbound(bio))
		{
			String status = appBio.getString("STATUS");
			//Defect1126 Calendar c = Calendar.getInstance();
			//Defect1126 Date currentDate = convertToGmt( new Date(), c.getTimeZone());

			BioCollection rs = bio.getUnitOfWork().findByQuery(new Query("wm_orders", "wm_orders.ORDERKEY = '" + bio.get("SOURCEKEY") + "'", null));
			for (int i = 0; i < rs.size(); i++)
			{
				Bio associatedOrder = rs.elementAt(i);

				associatedOrder.set("APPOINTMENTKEY", appBio.get("APPOINTMENTKEY"));
				associatedOrder.set("DOOR", appBio.get("DOOR"));
//				associatedOrder.set("DELIVERYDATE2", appBio.get("GMTENDDATEANDTIME"));
//defect1279	associatedOrder.set("PLANNEDSHIPDATE", appBio.get("GMTENDDATEANDTIME"));
				associatedOrder.set("PLANNEDSHIPDATE", appBio.get("GMTSTARTDATEANDTIME"));		//defect1279
				if("4COUT".compareTo(status.toUpperCase())<=0){
					//defect1126 associatedOrder.set("DepDateTime",currentDate);
					associatedOrder.set("DepDateTime",new GregorianCalendar());			//Defect1126
				}
				if("2CIN".equalsIgnoreCase(status)){
					//defect1126 associatedOrder.set("ACTUALARRIVALDATE",new currentDate);
					associatedOrder.set("ACTUALARRIVALDATE",new GregorianCalendar());	//Defect1126	
				}
				
				associatedOrder.set("TrailerNumber", appBio.get("TRAILER"));
				associatedOrder.set("TrailerType", appBio.get("TrailerType"));
				
				//update carrier info
				String carrier = BioUtil.getString(appBio, "CARRIER");
				updateCarrier(associatedOrder, carrier, wmordersCarrierMapping);

				if (!StringUtils.isEmpty((String) associatedOrder.get("LOADID")))
				{
					//query LOADHDR
					BioCollection associatedLoad = bio.getUnitOfWork().findByQuery(new Query("wm_loadhdr", "wm_loadhdr.LOADID = '" + associatedOrder.get("LOADID") + "'", null));
					for (int j = 0; j < associatedLoad.size(); j++)
					{
						Bio load = associatedLoad.elementAt(i);
						load.set("APPOINTMENTKEY", appBio.get("APPOINTMENTKEY"));
						load.set("DOOR", appBio.get("DOOR"));
//defect1279			load.set("APPOINTMENTTIME", appBio.get("GMTENDDATEANDTIME"));
						load.set("APPOINTMENTTIME", appBio.get("GMTSTARTDATEANDTIME"));		//defect1279				
						load.set("TrailerType", appBio.get("TrailerType"));
						updateCarrier(load, carrier, wmloadidCarrierMapping);
					}

				}

			}
		}
	}
	
	public Date convertToGmt( Date date, TimeZone tz)
	{
	   Date ret = new Date( date.getTime() - tz.getRawOffset() );

	   // if we are now in DST, back off by the delta.  Note that we are checking the GMT date, this is the KEY.
	   if ( tz.inDaylightTime( ret ))
	   {
	      Date dstDate = new Date( ret.getTime() - tz.getDSTSavings() );

	      // check to make sure we have not crossed back into standard time
	      // this happens when we are on the cusp of DST (7pm the day before the change for PDT)
	      if ( tz.inDaylightTime( dstDate ))
	      {
	         ret = dstDate;
	      }
	   }

	   return ret;
   }
	
	 public TimeZone getTimeZone(StateInterface state) 

     {

            UserInterface user = state.getUser();

            

            if(user instanceof UserBean)

            {

                   Object timeZoneObj = ((UserBean)user).getRequestAttribute("browser time zone");

                   if(timeZoneObj instanceof String)

                   {

                         TimeZone userTimeZone = TimeZone.getTimeZone(timeZoneObj.toString());

                         return userTimeZone;

                   }

            }

            return Calendar.getInstance().getTimeZone();

     }

	private void updateCarrier(Bio associatedBio, String carrier, HashMap<String, String> carrierMapping) throws EpiDataException
	{
		BioCollection rs = associatedBio.getUnitOfWork().findByQuery(new Query("wm_storer", "wm_storer.STORERKEY = '" + carrier + "' " + " and " + "wm_storer.TYPE = '3'", null));
		for (int i = 0; i < rs.size(); i++)
		{
			Bio carrierBio = rs.elementAt(i);
			for (String associatedAttribute : carrierMapping.keySet())
			{
				String carrierAttribute = carrierMapping.get(associatedAttribute);
				associatedBio.set(associatedAttribute, carrierBio.get(carrierAttribute));
			}
		}

	}

	private boolean isOutbound(Bio bio)
	{
		int sourceType = BioUtil.getInt(bio, "SOURCETYPE");
		if (sourceType == 1)
		{
			return true;
		}
		return false;
	}

	private boolean isInbound(Bio bio)
	{
		int sourceType = BioUtil.getInt(bio, "SOURCETYPE");
		if (sourceType == 0)
		{
			return true;
		}
		return false;
	}

	/**
	* Returning CANCEL from this will stop the operation
	* Cannot make any updates to the unit of work (create new bios/update existing bios)
	* during this extension
	    * <P>
	* @param context The EpnyServiceContext for this BioExtension instance
	* @param bio The Bio for this BioExtension instance
	* 
	* @return int RET_CONTINUE, RET_CANCEL
	* 
	    * @exception EpiException 
	*/
	@Override
	protected int bioFinalBeforeInsert(EpnyServiceContext context, Bio bio) throws EpiException
	{

		// Replace the following line with your code,
		//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		//	 as appropriate
		// Leave the line if the code is not going to handle 
		//  bioBeforeInsert events

		return super.bioFinalBeforeInsert(context, bio);
	}

	/**
	 * Returning CANCEL from this will stop the delete
	 * Creating new bios or changing bios in the Bio's unit of work
	 * will cause them to be added to the Bios to be processed 
	 * in this UnitOfWork.save();
	     * <P>
	 * @param context The EpnyServiceContext for this BioExtension instance
	 * @param bio The Bio for this BioExtension instance
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
	     * @exception EpiException 
	 */
	@Override
	protected int bioBeforeDelete(EpnyServiceContext context, Bio bio) throws EpiException
	{

		//if bio type is INBOUND
		//update receipt.ArrivalDateTime,DOOR,APPOINTMENTKEY
		if (isInbound(bio))
		{
			String query = "receipt.RECEIPTKEY = '" + bio.get("SOURCEKEY") + "'";
			BioCollection rs = bio.getUnitOfWork().findByQuery(new Query("receipt", query, null));
			for (int i = 0; i < rs.size(); i++)
			{
				Bio associatedReceipt = rs.elementAt(i);
				associatedReceipt.set("APPOINTMENTKEY", null);
				associatedReceipt.set("DOOR", null);
//				associatedReceipt.set("ArrivalDateTime", associatedReceipt.get("EFFECTIVEDATE"));
				associatedReceipt.set("EXPECTEDRECEIPTDATE", null);
				associatedReceipt.set("TRANSPORTATIONSTATUS", null);
				associatedReceipt.set("TrailerNumber", null);
				associatedReceipt.set("TrailerType", null);
			}
		}

		//if bio type is OUTBOUND
		//update wm_orders.DeliveryDate2,DOOR,APPOINTMENTKEY
		//if ORDER has LOADID, update loadhdr.APPOINTMENTTIME,DOOR, APPOINTMENTKEY
		if (isOutbound(bio))
		{
			String query = "wm_orders.ORDERKEY = '" + bio.get("SOURCEKEY") + "'";
			BioCollection rs = bio.getUnitOfWork().findByQuery(new Query("wm_orders", query, null));
			for (int i = 0; i < rs.size(); i++)
			{
				Bio associatedOrder = rs.elementAt(i);

				associatedOrder.set("APPOINTMENTKEY", null);
				associatedOrder.set("DOOR", "");
				associatedOrder.set("DELIVERYDATE2", associatedOrder.get("DELIVERYDATE"));
				associatedOrder.set("TrailerNumber", null);
				associatedOrder.set("TrailerType", null);

				if (!StringUtils.isEmpty((String) associatedOrder.get("LOADID")))
				{
					//query LOADHDR
					BioCollection associatedLoad = bio.getUnitOfWork().findByQuery(new Query("wm_loadhdr", "wm_loadhdr.LOADID = '" + associatedOrder.get("LOADID") + "'", null));
					for (int j = 0; j < associatedLoad.size(); j++)
					{
						Bio load = associatedLoad.elementAt(i);
						load.set("APPOINTMENTKEY", null);
						load.set("DOOR", null);
						load.set("APPOINTMENTTIME", load.get("DEPARTURETIME"));
						load.set("TrailerType", null);
					}
				}

			}
		}

		// Replace the following line with your code,
		//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		//	 as appropriate
		// Leave the line if the code is not going to handle 
		//  bioBeforeDelete events

		return RET_CONTINUE;
	}

	/**
	 * Returning CANCEL from this will stop the operation 
	 * Cannot make any updates to the unit of work (create new bios/update existing bios) 
	 * during this extension
	 * <P>
	* @param context The EpnyServiceContext for this BioExtension instance
	* @param bio The Bio for this BioExtension instance
	* 
	* @return int RET_CONTINUE, RET_CANCEL
	* 
	 * @exception EpiException 
	*/
	@Override
	protected int bioFinalBeforeDelete(EpnyServiceContext context, Bio bio) throws EpiException
	{

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		// Leave the line if the code is not going to handle 
		// bioBeforeDelete events

		return super.bioFinalBeforeDelete(context, bio);
	}

	/**
	 * Returning CANCEL from these will NOT stop the update
	 * It is called after the transaction is committed
	 *  To do any saving,  the client must be called on a new UnitOfWork.
	     * <P>
	 * @param context The EpnyServiceContext for this BioExtension instance
	 * @param bioChanged The BioRef for this BioExtension instance
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
	     * @exception EpiException 
	 */
	@Override
	protected int bioAfterUpdate(EpnyServiceContext context, BioRef bioChanged) throws EpiException
	{

		// Replace the following line with your code,
		//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		//	 as appropriate
		// Leave the line if the code is not going to handle 
		//  bioAfterUpdate events

		return super.bioAfterUpdate(context, bioChanged);
	}

	/**
	 * Returning CANCEL from these will NOT stop the insert
	 * It is called after the transaction is committed
		 *  To do any saving,  the client must be called on a new UnitOfWork.
	     * <P>
	 * @param context The EpnyServiceContext for this BioExtension instance
	 * @param bioInserted The BioRef for this BioExtension instance
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
	     * @exception EpiException 
	 */
	@Override
	protected int bioAfterInsert(EpnyServiceContext context, BioRef bioInserted) throws EpiException
	{

		// Replace the following line with your code,
		//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		//	 as appropriate
		// Leave the line if the code is not going to handle 
		//  bioAfterInsert events

		return super.bioAfterInsert(context, bioInserted);
	}

	/**
	 * Returning CANCEL from these will NOT stop the delete
	 * It is called after the transaction is committed
	 *  To do any saving,  the client must be called on a new UnitOfWork.
	     * <P>
	 * @param context The EpnyServiceContext for this BioExtension instance
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
	     * @exception EpiException 
	 */
	@Override
	protected int bioAfterDelete(EpnyServiceContext context) throws EpiException
	{

		// Replace the following line with your code,
		//   returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		//	 as appropriate
		// Leave the line if the code is not going to handle 
		//  bioAfterDelete events

		return super.bioAfterDelete(context);
	}
}
