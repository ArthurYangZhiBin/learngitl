package com.synnex.utils;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sap.*;
import com.sap.mw.jco.IFunctionTemplate;
import com.sap.mw.jco.JCO;
import com.sap.mw.jco.JCO.ParameterList;
public abstract class JcoUtil implements JcoInterface {
	private String client;
	private String user;
	private String pwd;
	private String lang="EN";
	private String ashost;
	private String sysnr;
	private String repository_name;
	private String function_name;
	private String returnSchema;
	
	public JcoUtil(String client,String user,String pwd,String ashost,String sysnr,
			String repository_name,String function_name,String returnSchema)
	{
		this.client=client;
		this.user=user;
		this.pwd=pwd;
		this.ashost=ashost;
		this.sysnr=sysnr;
		this.repository_name=repository_name;
		this.function_name=function_name;
		this.returnSchema=returnSchema;
	}

	 public JcoMessage execute(JcoMessage jcoMessage,Map inputParam,int seq,String inputTableName,List inputTableMap)
	 {
		    
		    try
		    {
		    	/*
				List<JcoParameter> inputParameters=new ArrayList<JcoParameter>();
				JcoParameter jcoParameter=new JcoParameter("table","table","123");
				inputParameters.add(jcoParameter);*/
		    	//jcol��
				JCO.Client connection = JCO.createClient(client, user,pwd, lang, ashost,sysnr);
				connection.connect();
				
				JCO.Repository repository = new JCO.Repository(repository_name, connection);
				IFunctionTemplate ft = repository.getFunctionTemplate(function_name.toUpperCase());
				if (ft == null) {
					jcoMessage.setFlag(false);
			    	jcoMessage.setMsg("函数不存在");
			    	return jcoMessage;
				}
				
				JCO.Function function = ft.getFunction();
				
				//输入参数
				JCO.ParameterList importParameterList = function.getImportParameterList();
				inputParam(importParameterList, inputParam);
				
				/*
				//importParameterList.setValue("I_WERKS", "6000");
				//importParameterList.setValue("I_MATNR","00B0153");
				importParameterList.getField("I_MATNR").setValue("00B0153");
				//importParameterList.getField("I_WERKS").setValue("6000");
				 * 
				 */
				
				//输入表
				JCO.ParameterList  importTable=function.getTableParameterList();
				inputTable(importTable,inputTableName,inputTableMap);
						
				connection.execute(function);
				
				
				JCO.ParameterList tableParameterList = function.getTableParameterList();
				
				JCO.Table retTable= tableParameterList.getTable(returnSchema);
				
				//查询过程是否正确
				if(retTable!=null)
				{
					outer:for (int i = 0; i < retTable.getNumRows(); i++)
					{
						for (JCO.FieldIterator e = retTable.fields(); e.hasMoreElements();) {
						JCO.Field field = e.nextField();
						String name = field.getName();
						Object value = getFieldValue(field);
						if ("TYPE".equalsIgnoreCase(name)) {
							if (value != null && "S".equalsIgnoreCase(value.toString())) {
								jcoMessage.setFlag(true);
							} else {
								jcoMessage.setFlag(false);
							}
						}
						if ("MESSAGE".equalsIgnoreCase(name) && (value != null)) {
							//message = value.toString();
							jcoMessage.setMsg(value.toString());
						}
						
					  }
					  if (!jcoMessage.isFlag()) {
							break outer;
					   }
					  retTable.nextRow();
					}
				 // System.out.println(jcoMessage.isFalg());
				  System.out.println(jcoMessage.getMsg());
				
				   if(!jcoMessage.isFlag())
				    {
				    	System.out.print("数据异常:"+jcoMessage.getMsg());
				    	return jcoMessage;
				    }
				
				    //输出表
				    outputTable(tableParameterList,jcoMessage,seq);
				    
				    JCO.ParameterList outputParam = function.getExportParameterList();
					/*
					JCO.Table OT_INFO=tableParameterList.getTable("OT_INFO");
					
					if(OT_INFO!=null)
					{
						for (int i = 0; i < OT_INFO.getNumRows(); i++)
						{
							System.out.println(OT_INFO.getField("MATKL").getValue());
							OT_INFO.nextRow();
						}
					}*/
				    outputParam(outputParam);
				    
				    
					
					
				}else
				{
					jcoMessage.setFlag(false);
			    	jcoMessage.setMsg("错误"+returnSchema);
			    	return jcoMessage;
				}
				
				
				
				//jco�ر�
				if (connection != null
						&& (connection.getState() != JCO.STATE_DISCONNECTED)) {
					JCO.releaseClient(connection);
				}
		    	
		    }catch(Exception ex)
		    {
		    	jcoMessage.setFlag(false);
		    	jcoMessage.setMsg(ex.getMessage());
		    }
			
			
			
			
			
			
			
			
			return jcoMessage;
	 }

	 public  Object getFieldValue(JCO.Field field) {
			Object ov = field.getValue();
			int type=field.getType();
			if (ov == null || "".equals(ov)) {
				if(JCO.TYPE_DATE==type||JCO.TYPE_TIME==type){
					return null;
				}else{
					return " ";
				}
			}
			switch (type) {
			case JCO.TYPE_DATE:
				java.sql.Date date = (java.sql.Date) ov;
				return date;
			case JCO.TYPE_TIME:
				Time time = (Time) ov;
				return time;
			default:
				return ov.toString().trim();
			}
		}

	
	public void inputParam(ParameterList inputParam,Map parameters)
	{
		if(parameters!=null)
		{
			 Set<String> keys = parameters.keySet();
		     for (Iterator it = keys.iterator(); it.hasNext();) {
		            String key = (String) it.next();
		            inputParam.getField(key).setValue(parameters.get(key));
		      }
		}
	}

	
	public void inputTable(JCO.ParameterList inputTable,String inputTableName,List<Map> inputTableMap)
	{
		if(null!=inputTableName && !("").equals(inputTableName.trim()))
		{
			if(null!=inputTableMap && inputTableMap.size()>0)
			{
				JCO.Table OT_INFO=inputTable.getTable(inputTableName);
				String key="";
				for(Map map:inputTableMap)
				{
					 Set<String> keys = map.keySet();
				     for (Iterator it = keys.iterator(); it.hasNext();) {
				            key = (String) it.next();
				            OT_INFO.appendRow();
				            OT_INFO.getField(key).setValue(map.get(key).toString());
				      }
				     OT_INFO.nextRow();
				}
			}
		}
	}
	
	/*

	public abstract void outputParam(JCO.ParameterList outputParam);

	public abstract void outputTable(JCO.ParameterList outputTable,	JcoMessage jcoMessage);*/
}
