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
package com.ssaglobal.scm.wms.wm_load_maintenance;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.StringUtils;

public class LMHeaderPrerenderAction extends FormExtensionBase {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(LMHeaderPrerenderAction.class);

	@Override
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException {
		_log.debug("LOG_DEBUG_EXTENSION_LMHEADPREREN", "Executing IBHeaderPrerenderAction", 100L);
		StateInterface state = context.getState();
		UnitOfWorkBean tuow = state.getTempUnitOfWork();
		//		UnitOfWorkBean uow = state.getDefaultUnitOfWork();
		DataBean focus = form.getFocus();

		String trailerType = BioAttributeUtil.getString(focus, "TRAILERTYPE");
		// Set default Value
		if (focus.isTempBio()) {
			// Trailer Type
			if (StringUtils.isEmpty(trailerType)) {
				// query CARTONIZATION, get first record with DISPLAYRFLOAD=1
				// and
				// USESEQUENCE=1
				BioCollectionBean rs = tuow.getBioCollectionBean(new Query("wm_cartonization", "wm_cartonization.DISPLAYRFLOAD = '1' and wm_cartonization.USESEQUENCE = '1'", null));
				if (rs.size() > 0) {
					// get first record
					focus.setValue("TRAILERTYPE", rs.get("0").getValue("CARTONTYPE"));
					trailerType = BioAttributeUtil.getString(focus, "TRAILERTYPE");
				}
			}

		}

		// Set Default Values if the trailerType
		BioCollectionBean rs = tuow.getBioCollectionBean(new Query("wm_cartonization", "wm_cartonization.CARTONTYPE = '" + trailerType + "'", null));
		for (int i = 0; i < rs.size(); i++) {
			BioBean carton = rs.get("" + i);
			focus.setValue("MAXWEIGHT", carton.getValue("MAXWEIGHT"));
			focus.setValue("MAXCUBE", carton.getValue("CUBE"));
			focus.setValue("MAXIDS", carton.getValue("MAXCOUNT"));
		}

		// Status
		Object statusObj = form.getFocus().getValue("STATUS");
		_log.debug("LOG_DEBUG_EXTENSION_LMHEADPREREN", "statusObj:" + statusObj, 100L);
		String status = statusObj == null ? "" : statusObj.toString();
		_log.debug("LOG_DEBUG_EXTENSION_LMHEADPREREN", "status:" + status, 100L);

		if (status.length() > 0) {
			if (status.equals("9")) {
				disableWidgets(form);
			} else {
				enableWidgets(form);
			}
		} else {
			enableWidgets(form);
		}
		_log.debug("LOG_DEBUG_EXTENSION_LMHEADPREREN", "Exiting IBHeaderPrerenderAction", 100L);

		return RET_CONTINUE;
	}

	private void disableWidgets(RuntimeNormalFormInterface form) {
		if("wm_load_maintenance_detail_form".equals(form.getName())){
			form.getFormWidgetByName("EXTERNALID").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("ROUTE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("DOOR").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("STATUS").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("ACTUALARRIVALDATE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("CARRIERID").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("TRAILERID").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("LOADCLOSETIME").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("APPOINTMENTTIME").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("SEALNUMBER").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("TRAILERTYPE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			//Food Enhancements - 3PL - Trailer Temperature Capture - Sreedhar Kethireddy  - NOV-02-2010 - Start
			form.getFormWidgetByName("TrailerDescription").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			//Food Enhancements - 3PL - Trailer Temperature Capture - Sreedhar Kethireddy  - NOV-02-2010 - End
			form.getFormWidgetByName("DRIVERNAME").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("LOADUSR1").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("LOADUSR2").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("LOADUSR3").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
//			form.getFormWidgetByName("APPTSTATUS").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("BOLNUMBER").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("PRONUMBER").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
//			form.getFormWidgetByName("ACTUALSHIPDATE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			
		} else if("wm_load_maintenance_detail_capacity_tab".equals(form.getName())) {
			form.getFormWidgetByName("LOADSEQUENCE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			// Load Enhancements
//			form.getFormWidgetByName("PICKEDCUBE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
//			form.getFormWidgetByName("PICKEDWEIGHT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
//			form.getFormWidgetByName("PICKEDUNITS").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("MAXWEIGHT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("MAXCUBE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("MAXIDS").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("SHIPTO").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			
			
			form.getFormWidgetByName("PMTTERM").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("TRANSPORTATIONMODE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("LOADUSR4").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
			form.getFormWidgetByName("LOADUSR5").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
		} else {
			
		}
		//		form.getFormWidgetByName("LOADEDIDS").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.TRUE);
	}

	private void enableWidgets(RuntimeNormalFormInterface form) {
		if("wm_load_maintenance_detail_form".equals(form.getName())){
			form.getFormWidgetByName("EXTERNALID").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("ROUTE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("DOOR").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("STATUS").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("ACTUALARRIVALDATE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("DEPARTURETIME").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("CARRIERID").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("TRAILERID").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("LOADCLOSETIME").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("APPOINTMENTTIME").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("SEALNUMBER").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("TRAILERTYPE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			//Food Enhancements - 3PL - Trailer Temperature Capture - Sreedhar Kethireddy  - NOV-02-2010 - Start
			form.getFormWidgetByName("TrailerDescription").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			//Food Enhancements - 3PL - Trailer Temperature Capture - Sreedhar Kethireddy  - NOV-02-2010 - End
			
			form.getFormWidgetByName("DRIVERNAME").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("LOADUSR1").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("LOADUSR2").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("LOADUSR3").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
//			form.getFormWidgetByName("APPTSTATUS").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("BOLNUMBER").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("PRONUMBER").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
//			form.getFormWidgetByName("ACTUALSHIPDATE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			
		} else if("wm_load_maintenance_detail_capacity_tab".equals(form.getName())) {
			form.getFormWidgetByName("LOADSEQUENCE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			// Load Enhancements
//			form.getFormWidgetByName("PICKEDCUBE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
//			form.getFormWidgetByName("PICKEDWEIGHT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
//			form.getFormWidgetByName("PICKEDUNITS").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("MAXWEIGHT").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("MAXCUBE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("MAXIDS").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("SHIPTO").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			
			form.getFormWidgetByName("PMTTERM").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("TRANSPORTATIONMODE").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("LOADUSR4").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
			form.getFormWidgetByName("LOADUSR5").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
		} else {
			
		}
		//		form.getFormWidgetByName("LOADEDIDS").setProperty(RuntimeFormWidgetInterface.PROP_READONLY, Boolean.FALSE);
	}
}