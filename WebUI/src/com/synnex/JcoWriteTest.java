package com.synnex;

import java.sql.Time;
import java.util.Date;

import com.sap.mw.jco.IFunctionTemplate;
import com.sap.mw.jco.JCO;
import com.sap.mw.jco.JCO.Structure;

public class JcoWriteTest {
	public static void main(String args[])
	{
		JcoWriteTest jcoUtil=new JcoWriteTest();
		jcoUtil.getConnection();
	}
	 public void getConnection()
	 {
			String client="400";
			String user="ZRFC_WMS";
			String pwd="123456789op";
			String lang="zh";
			String ashost="172.16.168.246";
			String sysnr="00";
			String repository_name="PSPL";
			String function_name="ZBAPI_GOODSMVT_CREATE1";
			boolean result=false;
			String message="";
			
			/*
			List<JcoParameter> inputParameters=new ArrayList<JcoParameter>();
			JcoParameter jcoParameter=new JcoParameter("table","table","123");
			inputParameters.add(jcoParameter);*/
			
			//jco连接
			JCO.Client connection = JCO.createClient(client, user,
					pwd, lang, ashost,sysnr);
			
			connection.connect();
			
			JCO.Repository repository = new JCO.Repository(repository_name, connection);
			IFunctionTemplate ft = repository.getFunctionTemplate(function_name.toUpperCase());
			if (ft == null) {
				//抛异常
			}
			JCO.Function function = ft.getFunction();
			
			
			
			//输入参数
			JCO.ParameterList importParameterList = function.getImportParameterList();
			//importParameterList.setValue("I_WERKS", "6000");
			//importParameterList.setValue("I_MATNR","00B0153");
			//Structure parem=new Structure();
			//importParameterList.getStructure("GOODSMVT_HEADER");
			Structure structure=importParameterList.getStructure("GOODSMVT_HEADER");
			structure.getField("PSTNG_DATE").setValue(new Date());
			structure.getField("DOC_DATE").setValue(new Date());
			structure.getField("PR_UNAME").setValue("alex");
			structure.getField("HEADER_TXT").setValue("123");
			
			JCO.Structure goodsMvtCode = importParameterList.getStructure("GOODSMVT_CODE");
            goodsMvtCode.setValue("03", "GM_CODE");    //03:MB1A 货物提取

			
			JCO.Table importTable=function.getTableParameterList().getTable("GOODSMVT_ITEM");
			importTable.appendRow();
			
			/*importTable.setValue("00B0014", "MATERIAL");  //物料号
			importTable.setValue("7100", "PLANT");  //物料号
			importTable.setValue("7100", "STGE_LOC");  //库存地点
			importTable.setValue("123", "BATCH");  //批号
			importTable.setValue(12, "ENTRY_QNT");  //数量
			importTable.setValue("PC", "ENTRY_UOM");  //基本单位*/
			
			importTable.setValue("SP102920", "MATERIAL");   //物料号
			importTable.setValue("6000", "PLANT");      //工厂
			importTable.setValue("1000", "STGE_LOC");   //库存地点
			importTable.setValue("", "BATCH");      //物料批次
			importTable.setValue("1", "ENTRY_QNT");  //盘盈数量
			importTable.setValue("PC", "ENTRY_UOM");  //基本单位
			
			
			importTable.appendRow();
			importTable.setValue("SP102365", "MATERIAL");   //物料号
			importTable.setValue("6000", "PLANT");      //工厂
			importTable.setValue("1000", "STGE_LOC");   //库存地点
			importTable.setValue("", "BATCH");      //物料批次
			importTable.setValue("-1", "ENTRY_QNT");  //盘亏数量
			importTable.setValue("PC", "ENTRY_UOM");  //基本单位
			
			connection.execute(function);
			
			
			JCO.ParameterList tableParameterList = function.getTableParameterList();
			
			JCO.Table retTable= tableParameterList.getTable("RETURN");
			
			//输出参数
			if(retTable!=null)
			{
				outer:for (int i = 0; i < retTable.getNumRows(); i++)
				{
					for (JCO.FieldIterator e = retTable.fields(); e.hasMoreElements();) {
					JCO.Field field = e.nextField();
					String name = field.getName();
					Object value = getFieldValue(field);
					System.out.println(field.getName() + ":\t" + field.getString());
					if ("TYPE".equalsIgnoreCase(name)) {
						if (value != null && "S".equalsIgnoreCase(value.toString())) {
							result = true;
						} else {
							result = false;
						}
					}
					if ("MESSAGE".equalsIgnoreCase(name) && (value != null)) {
						message = value.toString();
					}
					
				  }
					if (!result) {
						break outer;
					}
				  retTable.nextRow();
				}
			
			    //参数传递正确执行数据集获取
				//表
				
				
			}else
			{
				
			}
			System.out.println(result);
			System.out.println(message);
			
			//输出参数
            JCO.ParameterList output = function.getExportParameterList();
            System.out.println("盘盈产生的物料凭证: "+output.getValue("MATERIALDOCUMENT_PY"));
            System.out.println("盘亏产生的物料凭证: "+output.getValue("MATERIALDOCUMENT_PK"));

            
			
			
			
			
			
			
			
			//jco关闭
			if (connection != null
					&& (connection.getState() != JCO.STATE_DISCONNECTED)) {
				JCO.releaseClient(connection);
			}
			
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
}
