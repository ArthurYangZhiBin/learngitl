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

package com.ssaglobal.scm.wms.wm_flow_thru_lane;

// Import 3rd party packages and classes

// Import Epiphany packages and classes

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.error.EpiDataReadUninitializedException;
import com.epiphany.shr.data.bio.Query;
import com.ssaglobal.SsaException;
import com.ssaglobal.scm.wms.datalayer.EJBRemote;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.service.exeprocessmanager.TransactionServiceSORemote;

/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class AssignLaneMgtKey extends com.epiphany.shr.data.bio.extensions.BioExtensionBase
{
	protected static final String internalCaller = "internalCall";	

	protected static ILoggerCategory _log = LoggerFactory.getInstance(AssignLaneMgtKey.class);

	public AssignLaneMgtKey()
	{
		_log.info("EXP_1", "AssignLaneMgtKey has been instantiated...", SuggestedCategory.NONE);

	}
	
	/**
	 * Returning CANCEL from this will stop the insert Creating new bios or
	 * changing bios in the Bio's unit of work will cause them to be added to
	 * the Bios to be processed in this UnitOfWork.save();
	 * <P>
	 * 
	 * @param context
	 *            The EpnyServiceContext for this BioExtension instance
	 * @param bio
	 *            The Bio for this BioExtension instance
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
	 * @exception EpiException
	 */
	protected int bioBeforeInsert(EpnyServiceContext context, Bio bio) throws EpiException
	{

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		// Leave the line if the code is not going to handle
		// bioBeforeInsert events
		TextData myKey = null;
			Array argArray = new Array();
			EXEDataObject edo =null;
			argArray.add(new TextData("LANEMGT"));
			argArray.add(new TextData("10"));
			argArray.add(new TextData("1"));
			EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
   			String db_connection = userContext.get(SetIntoHttpSessionAction.DB_CONNECTION).toString();
   			String wmWhseID = (userContext.get(SetIntoHttpSessionAction.DB_USERID)).toString();

  			try {
   				TransactionServiceSORemote remote = EJBRemote.getRemote();
   				edo = remote.executeProcedure(new TextData(wmWhseID),db_connection,new TextData("GETKEYP1S1"),argArray,null,internalCaller,null);		//HC   				
   				if (edo._rowsReturned())
   				{
   				  myKey = edo.getAttribValue(new TextData("keyname")).asTextData();
   				}

   			} catch (SsaException x) {
   				_log.error("EXP_1","Could not get the remote...",SuggestedCategory.NONE);
   				_log.error(new EpiException("EXP_1", "SsaException nested in EpiException...", x.getCause()));
            
   			} catch (Exception exc) {
            	exc.printStackTrace();
            	EpiException x = new EpiException("EXP_1", "Unknown", exc);
            	_log.error(x);
            }
		bio.set("LANEGENNUMBER",myKey.toString());
		return RET_CONTINUE;
	}
}