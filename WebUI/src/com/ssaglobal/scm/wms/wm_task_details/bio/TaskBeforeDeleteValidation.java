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

package com.ssaglobal.scm.wms.wm_task_details.bio;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class TaskBeforeDeleteValidation extends com.epiphany.shr.data.bio.extensions.BioExtensionBase
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(TaskBeforeDeleteValidation.class);

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
	protected int bioBeforeDelete(EpnyServiceContext context, Bio bio) throws EpiException
	{

		//retrieve status
		String statusValue = bio.get("STATUS").toString();
		//if status = 3 or 9, cancel delete
		if (statusValue.equalsIgnoreCase("3"))
		{
			_log.debug("LOG_DEBUG_EXTENSION", "! Status = " + statusValue + " Canceling Delete", SuggestedCategory.NONE);
			String[] parameters = prepareParameters(bio);
			throw new UserException("WMEXP_TASK_DELETE_INPROCESS", parameters);
		}
		else if (statusValue.equalsIgnoreCase("9"))
		{
			_log.debug("LOG_DEBUG_EXTENSION", "! Status = " + statusValue + " Canceling Delete", SuggestedCategory.NONE);
			String[] parameters = prepareParameters(bio);
			throw new UserException("WMEXP_TASK_DELETE_COMPLETE", parameters);
		}

		_log.debug("LOG_DEBUG_EXTENSION", "!@# Processing Delete of " + bio.get("TASKDETAILKEY"), SuggestedCategory.NONE);
		return RET_CONTINUE;
	}

	private String[] prepareParameters(Bio bio) throws EpiDataException
	{
		String[] parameters = new String[1];
		parameters[0] = bio.get("TASKDETAILKEY").toString();
		return parameters;
	}

}
