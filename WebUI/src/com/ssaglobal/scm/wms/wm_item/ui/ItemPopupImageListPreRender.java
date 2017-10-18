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
package com.ssaglobal.scm.wms.wm_item.ui;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeListRowInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.uiextensions.CleanSession;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.SecurityUtil;
import com.ssaglobal.scm.wms.util.UserUtil;
import com.ssaglobal.scm.wms.wm_ws_defaults.WSDefaultPersistScreensInContext;


public class ItemPopupImageListPreRender extends FormExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemPopupImageListPreRender.class);
	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form) throws EpiException {		
		StateInterface state = context.getState();
		HttpSession session = state.getRequest().getSession();		
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		String sku = (String)session.getAttribute("CURRENT_ITEM_PK");
		if(sku == null || sku.length() == 0)
			return RET_CONTINUE;
		Query bioQry = new Query("wm_fileattachmapping","wm_fileattachmapping.SCREEN = 'ITEM' AND wm_fileattachmapping.RECORDID = '"+sku+"'","");
		BioCollection attachedImages = uow.getBioCollectionBean(bioQry);
		ArrayList attachedImageKeys = new ArrayList();
		RuntimeListRowInterface[] listRows = form.getRuntimeListRows();
		BioCollectionBean focus = (BioCollectionBean)form.getFocus();
		if(attachedImages == null || attachedImages.size() == 0){
			for(int i = 0; i < listRows.length; i++){			
				Bio bio = focus.elementAt(i);										
				bio.set("ISSELECTED","0");				
			}
			return RET_CONTINUE;
		}
		for(int i = 0; i < attachedImages.size(); i++){
			attachedImageKeys.add(attachedImages.elementAt(i).get("FILEID"));
		}
		
				
		for(int i = 0; i < listRows.length; i++){			
			Bio bio = focus.elementAt(i);			
			if(attachedImageKeys.contains(bio.get("FILEUPLOADPK")))
				bio.set("ISSELECTED","1");
			else{				
				bio.set("ISSELECTED","0");
			}
		}
		return RET_CONTINUE;
	}
	
}