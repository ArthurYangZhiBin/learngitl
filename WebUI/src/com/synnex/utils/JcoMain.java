package com.synnex.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sap.mw.jco.JCO;
import com.sap.mw.jco.JCO.ParameterList;
import com.sap.mw.jco.JCO.Structure;

public class JcoMain {
	
   public JcoMessage read(JcoUtil jcoUtil,JcoMessage jcoMessage,Map inputParam,int seq)
   {
	  //List<JcoDataMessage> jcoDataMessages=new ArrayList<JcoDataMessage>();
	  return jcoUtil.execute(jcoMessage,inputParam,seq,null,null);
   }
   
   public JcoMessage readAndT(JcoUtil jcoUtil,JcoMessage jcoMessage,Map inputParam,int seq,String inputTableName,List inputTableMap)
   {
	  return jcoUtil.execute(jcoMessage,inputParam,seq,inputTableName,inputTableMap);
   }
   
   
}
