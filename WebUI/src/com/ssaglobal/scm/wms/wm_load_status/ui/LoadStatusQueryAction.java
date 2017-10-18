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

package com.ssaglobal.scm.wms.wm_load_status.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes

import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.error.BioNotFoundException;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.action.*;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class LoadStatusQueryAction extends com.epiphany.shr.ui.action.ActionExtensionBase
{

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @exception EpiException
	 */
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{
		StateInterface state = context.getState();

		String bioRefString = state.getBucketValueString("listTagBucket");
		BioRef bioRef = BioRef.createBioRefFromString(bioRefString);
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		BioBean selectedRecord = null;
		try
		{
			selectedRecord = uowb.getBioBean(bioRef);

		} catch (BioNotFoundException bioEx)
		{
			_logger.error(bioEx);
			throw new FormException("ERROR_GET_SEL_BIO_LIST", null);
		}

		Query qry = new Query("wm_loadstop", "wm_loadstop.LOADID = '" + selectedRecord.getValue("LOADID") + "'", "wm_loadstop.STOP ASC");
		BioCollectionBean stopCollection = uowb.getBioCollectionBean(qry);
		
		result.setFocus((DataBean)stopCollection);

		return RET_CONTINUE;
	}

	/*
	 *  StateInterface state = context.getState();
	 String bioRefString = state.getBucketValueString("listTagBucket");
	 BioRef bioRef = BioRef.createBioRefFromString(bioRefString);
	 UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
	 com.epiphany.shr.ui.model.data.BioBean bioBean = null;
	 try
	 {
	 bioBean = uowb.getBioBean(bioRef);
	 result.setFocus(bioBean);
	 }
	 catch(BioNotFoundException bioEx)
	 {
	 _logger.error(bioEx);
	 throw new FormException("ERROR_GET_SEL_BIO_LIST", null);
	 }
	 */

}
