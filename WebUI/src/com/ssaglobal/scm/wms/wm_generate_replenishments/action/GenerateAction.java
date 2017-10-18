package com.ssaglobal.scm.wms.wm_generate_replenishments.action;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.DataValue;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManager;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDOAttrib;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.ReportUtil;
import com.ssaglobal.scm.wms.wm_table_validation.ccf.NumericValidationCCF;
import java.util.HashMap;
import java.util.Iterator;

public class GenerateAction extends ActionExtensionBase
{
  protected static ILoggerCategory _log = LoggerFactory.getInstance(GenerateAction.class);
  RuntimeFormInterface searchForm;
  private static final String REPORTID = "CRPT75";
  protected LocaleInterface locale;

  protected int execute(ActionContext context, ActionResult result)
    throws EpiException
  {
    StateInterface state = context.getState();

    this.locale = state.getLocale();

    this.searchForm = FormUtil.findForm(state.getCurrentRuntimeForm(), "wms_list_shell", "wm_generate_replenishments_search_view_form", state);

    for (Iterator it = this.searchForm.getFormWidgets(); it.hasNext(); )
    {
      try
      {
        RuntimeFormWidgetInterface widget = (RuntimeFormWidgetInterface)it.next();

        _log.debug("LOG_DEBUG_EXTENSION", "Widget : " + widget.getName(), 0L);
        _log.debug("LOG_DEBUG_EXTENSION", "Display : " + widget.getDisplayValue(), 0L);
        _log.debug("LOG_DEBUG_EXTENSION", "Value : " + widget.getValue(), 0L);
      }
      catch (Exception e) {
        _log.debug("LOG_DEBUG_EXTENSION", "Exception " + e.getClass().getName() + " = " + e.getMessage(), 0L);
      }
    }

    String ownerStart = this.searchForm.getFormWidgetByName("OWNERSTART").getDisplayValue().toUpperCase();
    if (ownerStart.equals(""))
    {
      ownerStart = "0";
    }
    String ownerEnd = this.searchForm.getFormWidgetByName("OWNEREND").getDisplayValue().toUpperCase();
    String zoneStart = this.searchForm.getFormWidgetByName("ZONESTART").getDisplayValue().toUpperCase();
    String zoneEnd = this.searchForm.getFormWidgetByName("ZONEEND").getDisplayValue().toUpperCase();
    String itemStart = this.searchForm.getFormWidgetByName("ITEMSTART").getDisplayValue().toUpperCase();
    String itemEnd = this.searchForm.getFormWidgetByName("ITEMEND").getDisplayValue().toUpperCase();
    String ptrStart = this.searchForm.getFormWidgetByName("PTRSTART").getDisplayValue().toUpperCase();
    String ptrEnd = this.searchForm.getFormWidgetByName("PTREND").getDisplayValue().toUpperCase();
    String priorityStart = this.searchForm.getFormWidgetByName("PRIORITYSTART").getDisplayValue().toUpperCase();
    String priorityEnd = this.searchForm.getFormWidgetByName("PRIORITYEND").getDisplayValue().toUpperCase();
    String retrieve = this.searchForm.getFormWidgetByName("RETRIEVE").getDisplayValue().toUpperCase();
    Object radioButton = this.searchForm.getFormWidgetByName("RADIOBUTTON").getValue();

    runSP(ownerStart, ownerEnd, zoneStart, zoneEnd, itemStart, itemEnd, ptrStart, ptrEnd, priorityStart, priorityEnd, retrieve, radioButton);

    generateReportURL(context, state, ownerStart, ownerEnd, zoneStart, zoneEnd, itemStart, itemEnd, ptrStart, ptrEnd, priorityStart, priorityEnd, retrieve, radioButton);

    return 0;
  }

  private void generateReportURL(ActionContext context, StateInterface state, String ownerStart, String ownerEnd, String zoneStart, String zoneEnd, String itemStart, String itemEnd, String ptrStart, String ptrEnd, String priorityStart, String priorityEnd, String retrieve, Object radioButton)
    throws UserException
  {
    if (retrieve.equalsIgnoreCase("0")) {
      retrieve = String.valueOf(2147483646);
    }
    if (ReportUtil.getReportServerType(state) == "BIRT")
    {
      if ("1".equals(radioButton))
      {
        radioButton = "ALLUOM";
      }
      else if ("2".equals(radioButton))
      {
        radioButton = "PALLETS";
      }
      else if ("3".equals(radioButton))
      {
        radioButton = "OTHERUOM";
      }
    }
    HashMap parametersAndValues = new HashMap();
    parametersAndValues.put("p_OwnerStart", ownerStart);
    parametersAndValues.put("p_OwnerEnd", ownerEnd);
    parametersAndValues.put("p_ZoneStart", zoneStart);
    parametersAndValues.put("p_ZoneEnd", zoneEnd);
    parametersAndValues.put("p_ItemStart", itemStart);
    parametersAndValues.put("p_ItemEnd", itemEnd);
    parametersAndValues.put("p_PickToLocStart", ptrStart);
    parametersAndValues.put("p_PickToLocEnd", ptrEnd);
    parametersAndValues.put("p_PriorityStart", priorityStart);
    parametersAndValues.put("p_PriorityEnd", priorityEnd);
    parametersAndValues.put("p_pTasks", retrieve);
    parametersAndValues.put("p_pUOM", radioButton);

    String reportURL = ReportUtil.retrieveReportURL(state, "CRPT75", parametersAndValues);
    _log.info("LOG_INFO_EXTENSION_GenerateAction", "Report URL " + reportURL, 0L);

    EpnyUserContext userCtx = context.getServiceManager().getUserContext();
    userCtx.put(ReportUtil.REPORTURL, reportURL);
  }

  private void runSP(String ownerStart, String ownerEnd, String zoneStart, String zoneEnd, String itemStart, String itemEnd, String ptrStart, String ptrEnd, String priorityStart, String priorityEnd, String retrieve, Object radioButton)
    throws UserException
  {
    WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
    Array params = new Array();
    params.add(new TextData(ownerStart));
    params.add(new TextData(ownerEnd));
    params.add(new TextData(zoneStart));
    params.add(new TextData(zoneEnd));
    params.add(new TextData(itemStart));
    params.add(new TextData(itemEnd));
    params.add(new TextData(ptrStart));
    params.add(new TextData(ptrEnd));
    params.add(new TextData(priorityStart));
    params.add(new TextData(priorityEnd));
    params.add(new TextData(retrieve));
    params.add(new TextData(radioButton.toString()));
    actionProperties.setProcedureParameters(params);
    actionProperties.setProcedureName("NSP_REPLREPORT");

    EXEDataObject results = null;
    try
    {
      results = WmsWebuiActionsImpl.doAction(actionProperties);
      if (results.getColumnCount() != 0)
      {
        _log.debug("LOG_DEBUG_EXTENSION", "---Results " + results.getColumnCount(), 0L);
        for (int i = 1; i <= results.getColumnCount(); i++)
        {
          _log.debug("LOG_DEBUG_EXTENSION", " " + i + " @ " + results.getAttribute(i).name + " " + results.getAttribute(i).value.getAsString(), 0L);
        }
      }

    }
    catch (WebuiException e)
    {
      _log.debug("LOG_DEBUG_EXTENSION", "\t\t" + e.getMessage(), 0L);
      throw new UserException(e.getMessage(), new Object[0]);
    }
    catch (Exception e1)
    {
      e1.printStackTrace();
    }
  }

  boolean isNull(Object attributeValue)
  {
    if (attributeValue == null)
    {
      return true;
    }

    return false;
  }

  private boolean isEmpty(Object attributeValue)
  {
    if (attributeValue == null)
    {
      return true;
    }
    if (attributeValue.toString().matches("\\s*"))
    {
      return true;
    }

    return false;
  }

  protected void priorityValidation(String attributeName, String attributeValue)
    throws UserException
  {
    if (isNull(attributeValue))
    {
      return;
    }
    double value = NumericValidationCCF.parseNumber(attributeValue);
    _log.debug("LOG_DEBUG_EXTENSION", "Value of " + attributeName + " - " + value, 0L);
    if (Double.isNaN(value))
    {
      String[] parameters = new String[2];
      parameters[0] = retrieveLabel(attributeName);
      parameters[1] = attributeValue;
      throw new UserException("WMEXP_FORM_NON_NUMERIC", parameters);
    }
    if ((value < 1.0D) || (value > 9.0D))
    {
      String[] parameters = new String[2];
      parameters[0] = retrieveLabel(attributeName);
      parameters[1] = attributeValue;
      throw new UserException("WMEXP_REPLENISHMENT_PRIORITY", parameters);
    }
  }

  protected void itemValidation(String attributeName, String attributeValue)
    throws EpiDataException, UserException
  {
    String table = "SKU";
    String tableAttribute = "SKU";
    if (!verifySingleAttribute(attributeValue, table, tableAttribute))
    {
      String[] parameters = new String[2];
      parameters[0] = retrieveLabel(attributeName);
      parameters[1] = attributeValue;
      throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
    }
  }

  protected void zoneValidation(String attributeName, String attributeValue) throws EpiDataException, UserException
  {
    String table = "PUTAWAYZONE";
    String tableAttribute = "PUTAWAYZONE";
    if (!verifySingleAttribute(attributeValue, table, tableAttribute))
    {
      String[] parameters = new String[2];
      parameters[0] = retrieveLabel(attributeName);
      parameters[1] = attributeValue;
      throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
    }
  }

  protected boolean verifySingleAttribute(String attributeValue, String table, String tableAttribute) throws EpiDataException
  {
    if (isEmpty(attributeValue))
    {
      return true;
    }
    String query = "SELECT * FROM " + table + " WHERE " + tableAttribute + " = '" + attributeValue.toString() + "'";
    _log.debug("LOG_DEBUG_EXTENSION", "Query\n" + query, 0L);
    EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
    if (results.getRowCount() == 1)
    {
      return true;
    }

    return false;
  }

  protected void ownerValidation(String attributeName, String attributeValue)
    throws EpiDataException, UserException
  {
    if (!verifyStorer(attributeValue, 1))
    {
      String[] parameters = new String[2];
      parameters[0] = retrieveLabel(attributeName);
      parameters[1] = attributeValue;
      throw new UserException("WMEXP_WIDGET_DOES_NOT_EXIST", parameters);
    }
  }

  protected boolean verifyStorer(String attributeValue, int type)
    throws DPException
  {
    if (isEmpty(attributeValue))
    {
      return true;
    }
    String query = "SELECT * FROM STORER WHERE (STORERKEY = '" + attributeValue + "') AND (TYPE = '" + type + "')";
    EXEDataObject results = WmsWebuiValidationSelectImpl.select(query);
    if (results.getRowCount() == 1)
    {
      return true;
    }

    return false;
  }

  protected String retrieveLabel(String widgetName)
  {
    return this.searchForm.getFormWidgetByName(widgetName).getLabel("label", this.locale);
  }
}