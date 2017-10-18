package com.synnex.utils;

import java.util.List;
import java.util.Map;

import com.sap.mw.jco.JCO;

public interface JcoInterface {
   public void inputParam(JCO.ParameterList importParam,Map parameters);
   public void inputTable(JCO.ParameterList inputTable,String inputTableName,List<Map> inputTableMap);
   public void outputParam(JCO.ParameterList outputParam);
   public void outputTable(JCO.ParameterList outputTable,JcoMessage jcoMessage,int seq);
}
