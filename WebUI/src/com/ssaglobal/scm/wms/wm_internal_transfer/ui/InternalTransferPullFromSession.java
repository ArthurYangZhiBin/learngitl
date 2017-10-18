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
package com.ssaglobal.scm.wms.wm_internal_transfer.ui;

//Import 3rd party packages and classes
import javax.servlet.http.HttpSession;

//Import Epiphany packages and classes
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.UOMDefaultValue;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;

public class InternalTransferPullFromSession extends ActionExtensionBase{
	//Widget name constants
	private final static String BIOREF = "BIO_REF";
	private final static String ITEM = "SKU";
	private final static String LPN = "ID";
	private final static String LOCATION = "LOC";
	private final static String LOT = "LOT";
	private final static String UOM = "UOM";
	private final static String PACK = "PACKKEY"; //	AW 10/07/2008 Machine#:2093019 SDIS:SCM-00000-05440
	private final static String QTY = "QTY";
	private final static String GROSSWGT = "GROSSWGT";
	private final static String NETWGT = "NETWGT";
	private final static String TAREWGT = "TAREWGT";
	
	//Error message constants
	private final static String ERROR_MESSAGE_LOST_BIOREF = "WMEXP_LOST_BIOREF";
	protected static ILoggerCategory _log = LoggerFactory.getInstance(InternalTransferPullFromSession.class);
	 
	protected int execute(ActionContext context, ActionResult result) throws FormException,EpiException{
		//Initialize local variables
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER_PULLFROMSESSION","Executing InternalTransferPullFromSession",100L);
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		DataBean focus = state.getCurrentRuntimeForm().getFocus();
		HttpSession session = state.getRequest().getSession();
		String toOrFrom = getParameter("toOrFrom").toString();
		final String tfSKU= toOrFrom + ITEM;
		final String tfLot= toOrFrom + LOT;
		final String tfLoc= toOrFrom + LOCATION;
		final String tfID= toOrFrom + LPN;
		final String tfUOM = toOrFrom + UOM;
		final String tfPack = toOrFrom + PACK ; //AW 10/07/2008 Machine#:2093019 SDIS:SCM-00000-05440
		final String tfQty = toOrFrom + QTY ;
		
		//Pull bioRef from session
		BioRef selectionRef = (BioRef)session.getAttribute(BIOREF);
		BioBean sessionBio = null;
		
		//Get associated bio
		try{
			sessionBio = uowb.getBioBean(selectionRef);
		}catch(Exception e){
			UOMDefaultValue.fillDropdown(state, tfUOM, UOMMappingUtil.PACK_STD);//AW 10/07/2008 Machine#:2093019 SDIS:SCM-00000-05440
			throw new FormException(ERROR_MESSAGE_LOST_BIOREF, null);
		}
		
		if(focus.isTempBio()) {
			QBEBioBean qbe = (QBEBioBean)focus;
			qbe.set(tfSKU, sessionBio.get(ITEM));
			qbe.set(tfID, sessionBio.get(LPN));
			qbe.set(tfLoc, sessionBio.get(LOCATION));
			qbe.set(tfLot, sessionBio.get(LOT));
			qbe.set(tfQty, sessionBio.get(QTY));
			
			if("FROM".equalsIgnoreCase(toOrFrom)){
				qbe.set(GROSSWGT, sessionBio.get(GROSSWGT));
				qbe.set(NETWGT, sessionBio.get(NETWGT));
				qbe.set(TAREWGT, sessionBio.get(TAREWGT));
				
				qbe.set("TOSKU", sessionBio.get(ITEM));
				qbe.set("TOLOC", sessionBio.get(LOCATION));
				qbe.set("TOID", sessionBio.get(LPN));
				qbe.set("TOQTY", sessionBio.get(QTY));
				qbe.set("TOLOT", " ");
				UOMDefaultValue.fillDropdown(state, "TOUOM", qbe.get("TOPACKKEY").toString());//	AW 10/07/2008 Machine#:2093019 SDIS:SCM-00000-05440
			}
			
			
			
			
			UOMDefaultValue.fillDropdown(state, tfUOM, qbe.get(tfPack).toString());//	AW 10/07/2008 Machine#:2093019 SDIS:SCM-00000-05440
		} else {
			BioBean bio = (BioBean)focus;
			bio.set(tfSKU, sessionBio.get(ITEM));
			bio.set(tfID, sessionBio.get(LPN));
			bio.set(tfLoc, sessionBio.get(LOCATION));
			bio.set(tfLot, sessionBio.get(LOT));
			bio.set(tfQty, sessionBio.get(QTY));
			
			
			if("FROM".equalsIgnoreCase(toOrFrom)){
				bio.set(GROSSWGT, sessionBio.get(GROSSWGT));
				bio.set(NETWGT, sessionBio.get(NETWGT));
				bio.set(TAREWGT, sessionBio.get(TAREWGT));
				
				bio.set("TOSKU", sessionBio.get(ITEM));
				bio.set("TOLOC", sessionBio.get(LOCATION));
				bio.set("TOID", sessionBio.get(LPN));
				bio.set("TOQTY", sessionBio.get(QTY));
				bio.set("TOLOT", " ");
				UOMDefaultValue.fillDropdown(state, "TOUOM", bio.get("TOPACKKEY").toString());//	AW 10/07/2008 Machine#:2093019 SDIS:SCM-00000-05440
			}

			UOMDefaultValue.fillDropdown(state, tfUOM, bio.get(tfPack).toString());//	AW 10/07/2008 Machine#:2093019 SDIS:SCM-00000-05440
		}
		result.setFocus(focus);	
		session.removeAttribute(BIOREF);
		_log.debug("LOG_DEBUG_EXTENSION_INTERNALTRANSFER_PULLFROMSESSION","Exiting InternalTransferPullFromSession",100L);
		return RET_CONTINUE;
	}
}