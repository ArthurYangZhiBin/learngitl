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
import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FileUtil;

public class PrerenderItemImage extends FormWidgetExtensionBase {

	protected static ILoggerCategory _log = LoggerFactory.getInstance(PrerenderItemImage.class);

	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget) throws UserException, EpiDataException{ 
		DataBean focus = widget.getForm().getFocus();
	    UnitOfWorkBean uow = state.getDefaultUnitOfWork();	    
	    EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
	    
		String usrLocale = getLocaleWOCountry(userContext);
		String defaultLocale = state.getLocale().getLocaleIDString();
		if(defaultLocale.indexOf("_") != -1)
			defaultLocale = defaultLocale.split("_")[0]; 
	    
		_log.debug("LOG_SYSTEM_OUT","\n\nusrLocale:"+usrLocale+"\n\n",100L);
		_log.debug("LOG_SYSTEM_OUT","\n\ndefaultLocale:"+defaultLocale+"\n\n",100L);
	    
		//Get Image For This User's Locale
	    Query bioQry = new Query("wm_fileattachmapping","wm_fileattachmapping.SCREEN = 'Item' AND wm_fileattachmapping.RECORDID = '"+focus.getValue("SKU")+"' AND wm_fileattachmapping.LOCALE = '"+usrLocale+"'","");
	    BioCollection records = uow.getBioCollectionBean(bioQry);
	    if(records != null && records.size() > 0){
	    	String url = FileUtil.generateFileRequest((String)records.elementAt(0).get("FILEID"),state,Boolean.TRUE);
		    widget.setProperty(RuntimeFormWidgetInterface.PROP_SRC,url);
		    return RET_CONTINUE;
	    }
	    //If Image Does Not Exist For This User's Locale Then Get Image For Default Locale
	    else{
	    	bioQry = new Query("wm_fileattachmapping","wm_fileattachmapping.SCREEN = 'Item' AND wm_fileattachmapping.RECORDID = '"+focus.getValue("SKU")+"' AND wm_fileattachmapping.LOCALE = '"+defaultLocale+"'","");
	    	records = uow.getBioCollectionBean(bioQry);
	    	if(records != null && records.size() > 0){
		    	String url = FileUtil.generateFileRequest((String)records.elementAt(0).get("FILEID"),state,Boolean.TRUE);
			    widget.setProperty(RuntimeFormWidgetInterface.PROP_SRC,url);
			    return RET_CONTINUE;
		    }
	    	//If No Image Exists For The User's Locale Or The Default Locale Then Get any Attached Image.
	    	else{
	    		bioQry = new Query("wm_fileattachmapping","wm_fileattachmapping.SCREEN = 'Item' AND wm_fileattachmapping.RECORDID = '"+focus.getValue("SKU")+"'","");
		    	records = uow.getBioCollectionBean(bioQry);
		    	if(records != null && records.size() > 0){
			    	String url = FileUtil.generateFileRequest((String)records.elementAt(0).get("FILEID"),state,Boolean.TRUE);
				    widget.setProperty(RuntimeFormWidgetInterface.PROP_SRC,url);
				    return RET_CONTINUE;
			    }
	    	}
	    }
	    widget.setProperty(RuntimeFormWidgetInterface.PROP_HIDDEN,Boolean.TRUE);
	    return RET_CONTINUE;
	    
	    
	}
	
	public static String getLocaleWOCountry(EpnyUserContext userContext){
		String locale = userContext.getLocale();
		if (locale.indexOf("_") != -1){
			locale = locale.split("_")[0];
		}
		return locale;
	}
}