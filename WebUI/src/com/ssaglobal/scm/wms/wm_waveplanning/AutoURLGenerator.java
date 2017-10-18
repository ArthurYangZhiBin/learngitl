package com.ssaglobal.scm.wms.wm_waveplanning;

import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionObjectInterface;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.customization.FormExtensionBase;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;

import java.util.HashMap;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Properties;

public class AutoURLGenerator extends FormExtensionBase
{
  private static final String CTX_WP = "/wave";
  private static final String DELIM_SLASH = "/";
  private static final String IFRAME_NAME = "wm_waveplanning_iframe";
  private static final String MENUITEM_WP_HOME = "wm_waveplanning_menu";
  private static final String MENUITEM_WP_ORDERFILTERS_QUERYBUILDERS = "wm_waveplanning_orderfilters_querybuilders_menuitem";
  private static final String MENUITEM_WP_ORDERFILTERS_GRAPHICALFILTERS = "wm_waveplanning_orderfilters_graphicalfilters_menuitem";
  private static final String MENUITEM_WP_ORDERFILTERS_SAVEDFILTERS = "wm_waveplanning_orderfilters_savedfilters_menuitem";
  private static final String MENUITEM_WP_WAVEMGMT_CONFIRMWAVE = "wm_waveplanning_wavemanagement_confirmwave_menuitem";
  private static final String MENUITEM_WP_WAVEMGMT_WAVEMAINTENANCE = "wm_waveplanning_wavemanagement_wavemaintenance_menuitem";
  private static final String MENUITEM_WP_RPTS_CASELABELS = "wm_waveplanning_reports_caselabels_menuitem";
  private static final String MENUITEM_WP_RPTS_ASSISTEDPICKTICKET = "wm_waveplanning_reports_assistedpickticket_menuitem";
  private static final String MENUITEM_WP_RPTS_DIRECTEDPICKTICKET = "wm_waveplanning_reports_directedpickticket_menuitem";
  private static final String MENUITEM_WP_RPTS_PICKLIST = "wm_waveplanning_reports_picklist_menuitem";
  private static final String MENUITEM_WP_RPTS_PACKSLIP = "wm_waveplanning_reports_packslip_menuitem";
  private static final String MENUITEM_WP_RPTS_COMMERCIALINVOICE = "wm_waveplanning_reports_commercialinvoice_menuitem";
  private static final String MENUITEM_WP_RPTS_CATCHWEIGHTDATALIST = "wm_waveplanning_reports_catchweightdatalist_menuitem";
  private static final String MENUITEM_WP_RPTS_CATCHWEIGHTPACKSLIP = "wm_waveplanning_reports_catchweightpackslip_menuitem";
  private static final String MENUITEM_WP_MA_GENERATEDALERTS = "wm_waveplanning_monitoringandalerts_generatedalerts_menuitem";
  private static final String MENUITEM_WP_OPER_ARCHIVING = "wm_waveplanning_operations_archiving_menuitem";
  private static final String MENUITEM_WP_OPER_ORDERSYNC = "wm_waveplanning_operations_ordersynchronization_menuitem";
  private static final String MENUITEM_WP_OPER_SKUSYNC = "wm_waveplanning_operations_skusynchronization_menuitem";
  private static final String MENUITEM_WP_CONF_SCHED_PROCESSGROUPS = "wm_waveplanning_configuration_scheduler_processgroups_menuitem";
  private static final String MENUITEM_WP_CONF_SCHED_SCHEDULEDPROCESSGROUPS = "wm_waveplanning_configuration_scheduler_scheduledprocessgroups_menuitem";
  private static final String MENUITEM_WP_CONF_SCHED_SCHEDULEDTASKGROUPS = "wm_waveplanning_configuration_scheduler_scheduledtaskgroups_menuitem";
  private static final String MENUITEM_WP_CONF_SCHED_ADMIN = "wm_waveplanning_configuration_scheduler_administration_menuitem";
  private static final String MENUITEM_WP_CONF_CONNECTIVITY = "wm_waveplanning_configuration_connectivity_menuitem";
  private static final String MENUITEM_WP_CONF_APPLICATION = "wm_waveplanning_configuration_application_menuitem";
  private static final String MENUITEM_WP_CONF_LABORDATA = "wm_waveplanning_configuration_labordata_menuitem";
  private static final String MENUITEM_WP_CONF_ARCHIVINGFACILITY = "wm_waveplanning_configuration_archivingfacility_menuitem";
  private static final String MENUITEM_WP_CONF_ARCHIVINGDB = "wm_waveplanning_configuration_archivingdatabase_menuitem";
  private static final String ACTION_WP_HOME = "/home.do";
  private static final String ACTION_WP_ORDERFILTERS_QUERYBUILDERS = "/orderfilter/filterheader.do";
  private static final String ACTION_WP_ORDERFILTERS_GRAPHICALFILTERS = "/orderfilter/criterialist.do";
  private static final String ACTION_WP_ORDERFILTERS_SAVEDFILTERS = "/orderfilter/filterslist.do?action=list&dispatch=all&status=3";
  private static final String ACTION_WP_WAVEMGMT_CONFIRMWAVE = "/wavemgmt/confirmwaveslist.do?action=waveslist&status=2";
  private static final String ACTION_WP_WAVEMGMT_WAVEMAINTENANCE = "/wavemgmt/maintenancewaveslist.do?action=waveslist&status=3";
  private static final String ACTION_WP_RPTS_CASELABELS = "/selectionCriteria.do?action=caseLabels";
  private static final String ACTION_WP_RPTS_ASSISTEDPICKTICKET = "/selectionCriteria.do?action=assistedPickTicket";
  private static final String ACTION_WP_RPTS_DIRECTEDPICKTICKET = "/selectionCriteria.do?action=directedPickTicket";
  private static final String ACTION_WP_RPTS_PICKLIST = "/selectionCriteria.do?action=pickList";
  private static final String ACTION_WP_RPTS_PACKSLIP = "/selectionCriteria.do?action=packSlip";
  private static final String ACTION_WP_RPTS_COMMERCIALINVOICE = "/selectionCriteria.do?action=commercialInvoice";
  private static final String ACTION_WP_RPTS_CATCHWEIGHTDATALIST = "/selectionCriteria.do?action=catchWeightDataList";
  private static final String ACTION_WP_RPTS_CATCHWEIGHTPACKSLIP = "/selectionCriteria.do?action=catchWeightDataPackSlip";
  private static final String ACTION_WP_MA_GENERATEDALERTS = "/alertsList.do";
  private static final String ACTION_WP_OPER_ARCHIVING = "/readOnlyArchivingConfigDetails.do?action=readOnlyArchivingConfigDetails";
  private static final String ACTION_WP_OPER_ORDERSYNC = "/wavemgmt/orderdownload.do";
  private static final String ACTION_WP_OPER_SKUSYNC = "/wavemgmt/skudownload.do";
  private static final String ACTION_WP_CONF_SCHED_PROCESSGROUPS = "/EntSchedulerProcessGroups.do";
  private static final String ACTION_WP_CONF_SCHED_SCHEDULEDPROCESSGROUPS = "/EntSchedulerScheduledProcessGroups.do";
  private static final String ACTION_WP_CONF_SCHED_SCHEDULEDTASKGROUPS = "/EntSchedulerScheduledTaskGroups.do";
  private static final String ACTION_WP_CONF_SCHED_ADMIN = "/EntSchedulerAdmin.do";
  private static final String ACTION_WP_CONF_CONNECTIVITY = "/connectConfig.do?action=facilityConfigurationList";
  private static final String ACTION_WP_CONF_APPLICATION = "/appConfig.do";
  private static final String ACTION_WP_CONF_LABORDATA = "/laborConfig.do";
  private static final String ACTION_WP_CONF_ARCHIVINGFACILITY = "/archivingConfigList.do?action=archivingConfigList";
  private static final String ACTION_WP_CONF_ARCHIVINGDB = "/archiveDbConfigDetails.do?action=archiveDbConfigDetails";
  protected static ILoggerCategory _log = LoggerFactory.getInstance(AutoURLGenerator.class);
  private static HashMap menuActionMap;

  private static synchronized void loadMenuActionMap(UIRenderContext context)
  {
    if (menuActionMap != null)
      return;

    String contextPath = context.getState().getRequest().getContextPath();
    StringTokenizer strtok = new StringTokenizer(contextPath, "/");
    String url = "/".concat(strtok.nextToken()).concat("/wave");
    menuActionMap = new HashMap();
    menuActionMap.put("wm_waveplanning_menu", url.concat("/home.do"));
    menuActionMap.put("wm_waveplanning_orderfilters_querybuilders_menuitem", url.concat("/orderfilter/filterheader.do"));
    menuActionMap.put("wm_waveplanning_orderfilters_graphicalfilters_menuitem", url.concat("/orderfilter/criterialist.do"));
    menuActionMap.put("wm_waveplanning_orderfilters_savedfilters_menuitem", url.concat("/orderfilter/filterslist.do?action=list&dispatch=all&status=3"));
    menuActionMap.put("wm_waveplanning_wavemanagement_confirmwave_menuitem", url.concat("/wavemgmt/confirmwaveslist.do?action=waveslist&status=2"));
    menuActionMap.put("wm_waveplanning_wavemanagement_wavemaintenance_menuitem", url.concat("/wavemgmt/maintenancewaveslist.do?action=waveslist&status=3"));
    menuActionMap.put("wm_waveplanning_reports_caselabels_menuitem", url.concat("/selectionCriteria.do?action=caseLabels"));
    menuActionMap.put("wm_waveplanning_reports_assistedpickticket_menuitem", url.concat("/selectionCriteria.do?action=assistedPickTicket"));
    menuActionMap.put("wm_waveplanning_reports_directedpickticket_menuitem", url.concat("/selectionCriteria.do?action=directedPickTicket"));
    menuActionMap.put("wm_waveplanning_reports_picklist_menuitem", url.concat("/selectionCriteria.do?action=pickList"));
    menuActionMap.put("wm_waveplanning_reports_packslip_menuitem", url.concat("/selectionCriteria.do?action=packSlip"));
    menuActionMap.put("wm_waveplanning_reports_commercialinvoice_menuitem", url.concat("/selectionCriteria.do?action=commercialInvoice"));
    menuActionMap.put("wm_waveplanning_reports_catchweightdatalist_menuitem", url.concat("/selectionCriteria.do?action=catchWeightDataList"));
    menuActionMap.put("wm_waveplanning_reports_catchweightpackslip_menuitem", url.concat("/selectionCriteria.do?action=catchWeightDataPackSlip"));
    menuActionMap.put("wm_waveplanning_monitoringandalerts_generatedalerts_menuitem", url.concat("/alertsList.do"));
    menuActionMap.put("wm_waveplanning_operations_archiving_menuitem", url.concat("/readOnlyArchivingConfigDetails.do?action=readOnlyArchivingConfigDetails"));
    menuActionMap.put("wm_waveplanning_operations_ordersynchronization_menuitem", url.concat("/wavemgmt/orderdownload.do"));
    menuActionMap.put("wm_waveplanning_operations_skusynchronization_menuitem", url.concat("/wavemgmt/skudownload.do"));
    menuActionMap.put("wm_waveplanning_configuration_scheduler_processgroups_menuitem", url.concat("/EntSchedulerProcessGroups.do"));
    menuActionMap.put("wm_waveplanning_configuration_scheduler_scheduledprocessgroups_menuitem", url.concat("/EntSchedulerScheduledProcessGroups.do"));
    menuActionMap.put("wm_waveplanning_configuration_scheduler_scheduledtaskgroups_menuitem", url.concat("/EntSchedulerScheduledTaskGroups.do"));
    menuActionMap.put("wm_waveplanning_configuration_scheduler_administration_menuitem", url.concat("/EntSchedulerAdmin.do"));
    menuActionMap.put("wm_waveplanning_configuration_connectivity_menuitem", url.concat("/connectConfig.do?action=facilityConfigurationList"));
    menuActionMap.put("wm_waveplanning_configuration_application_menuitem", url.concat("/appConfig.do"));
    menuActionMap.put("wm_waveplanning_configuration_labordata_menuitem", url.concat("/laborConfig.do"));
    menuActionMap.put("wm_waveplanning_configuration_archivingfacility_menuitem", url.concat("/archivingConfigList.do?action=archivingConfigList"));
    menuActionMap.put("wm_waveplanning_configuration_archivingdatabase_menuitem", url.concat("/archiveDbConfigDetails.do?action=archiveDbConfigDetails"));
  
    Properties props = System.getProperties();
	
	String filePathUrl = props.getProperty("jboss.server.config.url");
	String filePath = filePathUrl.substring(filePathUrl.indexOf("/")+1)+"customize-url.properties";
	List<List> list = PropertyUtil.readProperties(filePath);
	for(List listDetail:list){
		String key = listDetail.get(0).toString();		
		menuActionMap.put(key, listDetail.get(1).toString());
	}  
  }

  protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form)
    throws EpiException
  {
    try
    {
      /*loadMenuActionMap(context);
      String menuItemName = context.getActionObject().getName();
      String url = (String)menuActionMap.get(menuItemName);
      _log.debug("LOG_SYSTEM_OUT", "*********" + url + "*********", 100L);

      RuntimeFormWidgetInterface wpIFrameForm = form.getFormWidgetByName("wm_waveplanning_iframe");
      wpIFrameForm.setProperty("src", url);*/
      
      loadMenuActionMap(context);
	   String menuItemName = context.getActionObject().getName();
	   String url = (String) menuActionMap.get(menuItemName);
      String currentConnection = context.getState().getRequest().getSession().getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();
	   RuntimeFormWidgetInterface wpIFrameForm = form.getFormWidgetByName(AutoURLGenerator.IFRAME_NAME);
	   //wpIFrameForm.setProperty(wpIFrameForm.PROP_SRC, url);
	   String userName = context.getState().getUser().getName();
	   EpnyUserContext userContext = EpnyServiceManagerServer.getInstance().getUserContext();
	   String wmWhseID = (userContext.get(SetIntoHttpSessionAction.DB_USERID)).toString();
	   if(null!=url && url.indexOf("?")<0)
	   {
	     wpIFrameForm.setProperty(wpIFrameForm.PROP_SRC, url+"?dataSource="+currentConnection+"&userid=" + userName+ "&whseid=" + wmWhseID.toUpperCase());
	   }else
	   {
		   wpIFrameForm.setProperty(wpIFrameForm.PROP_SRC, url+"&dataSource="+currentConnection+"&userid=" + userName+ "&whseid=" + wmWhseID.toUpperCase());
		   
	   }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return 1;
    }

    return 0;
  }

  protected int preRenderForm(ModalUIRenderContext context, RuntimeNormalFormInterface form)
    throws EpiException
  {
    return 0;
  }

  protected int modifySubSlots(UIRenderContext context, RuntimeFormExtendedInterface form)
    throws EpiException
  {
    return 0;
  }

  protected int modifySubSlots(ModalUIRenderContext context, RuntimeFormExtendedInterface form)
    throws EpiException
  {
    return 0;
  }

  protected int setFocusInForm(UIRenderContext context, RuntimeFormInterface form)
    throws EpiException
  {
    return 0;
  }

  protected int setFocusInForm(ModalUIRenderContext context, RuntimeFormInterface form)
    throws EpiException
  {
    return 0;
  }

  protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form)
    throws EpiException
  {
    return 0;
  }

  protected int preRenderListForm(ModalUIRenderContext context, RuntimeListFormInterface form)
    throws EpiException
  {
    return 0;
  }

  protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form)
    throws EpiException
  {
    return 0;
  }

  protected int modifyListValues(ModalUIRenderContext context, RuntimeListFormInterface form)
    throws EpiException
  {
    return 0;
  }
}