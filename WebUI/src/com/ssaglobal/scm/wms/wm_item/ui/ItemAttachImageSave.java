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

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.metadata.GUIDFactory;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.DataBeanException;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormBasicInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.util.FormUtil;
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

public class ItemAttachImageSave extends SaveAction
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ItemAttachImageSave.class);
	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 * @throws UserException 
	 */
	protected int execute(ActionContext context, ActionResult result) throws UserException
	{
		StateInterface state = context.getState();
		HttpSession session = state.getRequest().getSession();
		RuntimeFormInterface popupForm = FormUtil.findForm(state.getCurrentRuntimeForm(),"","wm_item_select_image_popup",state);
		BioCollection focus = (BioCollection)popupForm.getFocus();
		String sku = (String)session.getAttribute("CURRENT_ITEM_PK");
		
		boolean doAttachFile = false;
		boolean doRemoveFile = true;	
		try {
			if(focus != null && focus.size() > 0){			
				UnitOfWorkBean uow = state.getDefaultUnitOfWork();
				//Load an array with the user-selected records
				ArrayList selectedItems = new ArrayList();
				ArrayList selectedLocales = new ArrayList();
				for(int i = 0; i < focus.size(); i++){				
					String isSelected = (String)focus.elementAt(i).get("ISSELECTED");		
					_log.debug("LOG_SYSTEM_OUT","\n\nisSelected:"+isSelected+"\n\n",100L);
					if(isSelected != null && isSelected.equals("1")){	
						_log.debug("LOG_SYSTEM_OUT","\n\nFound Selected\n\n",100L);
						selectedItems.add(focus.elementAt(i).get("FILEUPLOADPK"));						
						//Assert that selected records' locales are unique
						if(selectedLocales.contains(focus.elementAt(i).get("LOCALE"))){
							String args[] = new String[0]; 
							String errorMsg = getTextMessage("WMEXP_ATTACH_IMAGE_UNIQUE_LOCALE",args,state.getLocale());
							throw new UserException(errorMsg,new Object[0]);
						}
						selectedLocales.add(focus.elementAt(i).get("LOCALE"));
					}
				}
				
				//Get curently attached files
				Query bioQry = new Query("wm_fileattachmapping","wm_fileattachmapping.SCREEN = 'ITEM' and wm_fileattachmapping.RECORDID = '"+sku+"'","");
				BioCollection currentAttachedFiles = uow.getBioCollectionBean(bioQry);
				
				//Detach any files that have been unselected
				for(int i = 0; i < currentAttachedFiles.size(); i++){
					if(!selectedItems.contains(currentAttachedFiles.elementAt(i).get("FILEID"))){
						_log.debug("LOG_SYSTEM_OUT","\n\nDeattaching\n\n",100L);
						currentAttachedFiles.elementAt(i).delete();
					}
					else{
						_log.debug("LOG_SYSTEM_OUT","\n\nRemoving From Selected\n\n",100L);
						doRemoveFile = false;
						selectedItems.remove(currentAttachedFiles.elementAt(i).get("FILEID"));
					}
				}
				
				//Attach remaining selected files
				for(int i = 0; i < selectedItems.size(); i++){
					_log.debug("LOG_SYSTEM_OUT","\n\nAttaching\n\n",100L);
					QBEBioBean newAttachedFile = uow.getQBEBioWithDefaults("wm_fileattachmapping");
					newAttachedFile.set("FILEATTACHMAPPINGPK",GUIDFactory.getGUIDStatic());
					newAttachedFile.set("SCREEN","ITEM");
					newAttachedFile.set("FILEID",selectedItems.get(i));
					newAttachedFile.set("RECORDID",sku);
					doRemoveFile = false;
					doAttachFile = true;
				}
				
				_log.debug("LOG_SYSTEM_OUT","\n\nHERE2\n\n",100L);
				//Update the Item's HASIMAGE column.
				if(doRemoveFile){
					_log.debug("LOG_SYSTEM_OUT","\n\nRemoving File For SKU:"+sku+"\n\n",100L);
					bioQry = new Query("sku","sku.SKU = '"+sku+"'","");
					BioCollection currentItem = uow.getBioCollectionBean(bioQry);
					currentItem.elementAt(0).set("HASIMAGE","0");
				}
				else if(doAttachFile){
					_log.debug("LOG_SYSTEM_OUT","\n\nAttaching File For SKU:"+sku+"\n\n",100L);
					bioQry = new Query("sku","sku.SKU = '"+sku+"'","");
					BioCollection currentItem = uow.getBioCollectionBean(bioQry);
					currentItem.elementAt(0).set("HASIMAGE","1");
				}
				_log.debug("LOG_SYSTEM_OUT","\n\nAbout To Save\n\n",100L);
				try{
					uow.saveUOW(true);
				}catch (UnitOfWorkException e){
					_log.debug("LOG_SYSTEM_OUT","\n\t" + "IN UnitOfWorkException" + "\n",100L);
					
					Throwable nested = ((UnitOfWorkException) e).getDeepestNestedException();
					_log.debug("LOG_SYSTEM_OUT","\tNested " + nested.getClass().getName(),100L);
					_log.debug("LOG_SYSTEM_OUT","\tMessage " + nested.getMessage(),100L);
					
					if(nested instanceof ServiceObjectException)
					{
						String reasonCode = nested.getMessage();
						//replace terms like Storer and Commodity
						
						throwUserException(e, reasonCode, null);
					}
					else
					{
						throwUserException(e, "WMEXP_SAVE_FAILED", null);
					}

				} catch (EpiException e) {
					e.printStackTrace();
					String args[] = new String[0]; 
					String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
					throw new UserException(errorMsg,new Object[0]);
				}
			}
		} catch (EpiDataException e) {
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		} catch (DataBeanException e) {
			e.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = getTextMessage("WMEXP_SAVE_FAILED",args,state.getLocale());
			throw new UserException(errorMsg,new Object[0]);
		}
		session.removeAttribute("CURRENT_ITEM_PK");
		return RET_CONTINUE;
	}

}
