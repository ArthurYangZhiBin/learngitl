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
package com.ssaglobal.scm.wms.wm_review_physical.ui;

//Import 3rd party packages and classes
import javax.servlet.http.HttpSession;

//Import Epiphany packages and classes
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;

public class ReviewPhysicalPullFromSession extends ActionExtensionBase{
	private final static String BIOREF = "SELECTED_BIO_REF";
	private final static String ITEM = "SKU";
	private final static String LPN = "ID";
	private final static String LOCATION = "LOC";
	private final static String LOT = "LOT";
	private final static String ERROR_MESSAGE_LOST_BIOREF = "WMEXP_LOST_BIOREF";
	private final static String SOURCE = "SOURCE";
	
	protected int execute(ActionContext context, ActionResult result) throws FormException, EpiDataException{
		//Initialize local variables
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
//		QBEBioBean qbe = (QBEBioBean)state.getCurrentRuntimeForm().getFocus();
		BioBean qbe = (BioBean)state.getCurrentRuntimeForm().getFocus();
		HttpSession session = state.getRequest().getSession();
		String source =(String)session.getAttribute(SOURCE);
		
		if (source.compareToIgnoreCase("wm_review_physical_lotxlocxid_lookup_list_view")==0){

			//Pull bioRef from session
			BioRef selectionRef = (BioRef)session.getAttribute(BIOREF);
			BioBean bio = null;
			
			//Get associated bio
			try{
				bio = uowb.getBioBean(selectionRef);
			}catch(Exception e){
				throw new FormException(ERROR_MESSAGE_LOST_BIOREF, null);
			}
			
//			qbe.set(ITEM, bio.get(ITEM));
			qbe.set(LPN, bio.get(LPN));
//			qbe.set(LOCATION, bio.get(LOCATION));
			qbe.set(LOT, bio.get(LOT));
			
			result.setFocus(qbe);
			session.removeAttribute(BIOREF);
		}
		return RET_CONTINUE;
	}
}

