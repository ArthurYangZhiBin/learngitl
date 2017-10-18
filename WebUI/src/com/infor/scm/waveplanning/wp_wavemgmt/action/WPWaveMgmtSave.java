/**
 * ---Begin Copyright Notice---20090105T1353
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2010 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */
package com.infor.scm.waveplanning.wp_wavemgmt.action;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Set;

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.bio.BioAttributeMetadata;
import com.epiphany.shr.data.bio.BioMetadata;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.metadata.Metadata;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.metadata.objects.bio.BioType;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.SaveAction;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.GUIDFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.epiphany.shr.ui.model.data.*;
import com.epiphany.shr.data.bio.*;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationUpdateImpl;
/**
 * TODO Document WPWaveMgmtSave class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class WPWaveMgmtSave extends SaveAction{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPWaveMgmtSave.class);
	protected int execute(ActionContext context, ActionResult result) throws UserException {
		StateInterface state = context.getState();

		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface tabShell = toolbar.getParentForm(state);
		DataBean dataBean = tabShell.getFocus();
		String name = tabShell.getName();
		String waveKey ="";
			try {	
//				uow.saveUOW(true);
				
					if(dataBean.isBio()){
						
						//start handing orderdetail tab updates
						BioBean bioBean = (BioBean)dataBean;
						waveKey = bioBean.getString("WAVEKEY");
						BioCollection orderDetailBC = bioBean.getBioCollection("ORDERDETAILBIOCOLLECTION");
						if(orderDetailBC != null){
							int size = orderDetailBC.size();
							if(size > 0){
								ArrayList<String> orderDetailUpdates = new ArrayList<String>();
								Bio bio = null;
								BioBean  bb = null;
								for(int i=0;i<size;i++){
									bio = orderDetailBC.elementAt(i);
									bb = uow.getBioBean(bio);
									String orderKey = "";
									String orderLine = "";
									double openQty = 0.0;
									if(bb.isDirty()){
										bb.setValue("UOMADJOPENQTY", "92.00000");
										orderKey = bb.getString("ORDERKEY");
										orderLine = bb.getString("ORDERLINENUMBER");
										Object openQtyObj = bb.getValue("UOMADJOPENQTY", false);
										
										openQty = Double.parseDouble(openQtyObj.toString());
//										openQty = Double.parseDouble(bb.getString("UOMADJOPENQTY"));
										orderDetailUpdates.add("UPDATE ORDERDETAIL SET OPENQTY="+openQty+" WHERE ORDERKEY='"+orderKey+"' " +
												"AND ORDERLINENUMBER='"+orderLine+"'");
									}
								}
								int dirtySize = orderDetailUpdates.size();
								for(int k=0;k<dirtySize;k++){
									WmsWebuiValidationUpdateImpl.update(orderDetailUpdates.get(k));
								}
							}
						}
			
					}
					

					
			    	Query qry = new Query("wm_wp_wave","wm_wp_wave.WAVEKEY='"+waveKey+"'", null);			 		
					BioCollectionBean resultCollection = uow.getBioCollectionBean(qry);	
					uow.clearState();
					result.setFocus(resultCollection.get("0"));

			} catch (UnitOfWorkException e) {
				e.printStackTrace();
				Throwable nested = (e).findDeepestNestedException();
				if (nested != null) {
					_log.error(	"LOG_ERROR_EXTENSION_WPHeaderDetailSave",
								"Nested " + nested.getClass().getName(),
								SuggestedCategory.NONE);
					_log.error(	"LOG_ERROR_EXTENSION_WPHeaderDetailSave",
								"Message " + nested.getMessage(),
								SuggestedCategory.NONE);
				}
				if (nested instanceof ServiceObjectException) {
					String reasonCode = nested.getMessage();
					throwUserException(e, reasonCode, null);
				} else {
					throwUserException(e, "ERROR_SAVING_BIO", null);
				}

			} catch (EpiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				_log.error("LOG_ERROR_EXTENSION_WWPWaveMgmtSave", e.getStackTraceAsString(), SuggestedCategory.NONE);
				throw new UserException("WMEXP_SAVE_FAILED", new Object[] {});
			}
			return RET_CONTINUE;


	}

}
