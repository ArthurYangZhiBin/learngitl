package com.ssaglobal.scm.wms.wm_shipfrom.ui;

import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.sap.mw.jco.JCO;
import com.sap.mw.jco.JCO.ParameterList;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.synnex.utils.JcoMain;
import com.synnex.utils.JcoMessage;
import com.synnex.utils.JcoUtil;

public class ShipReverse extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected int execute(ActionContext context, ActionResult result)
	throws EpiException {
	String shellFormName = "wm_list_shell_shipfrom";
	String generalFormTab = "wm_shipfrom_general";
	StateInterface state = context.getState();
	RuntimeFormInterface shellToolbar = state.getCurrentRuntimeForm();
	
	//General
	ArrayList tabIdentifiers = new ArrayList();
	tabIdentifiers.add("tab 0");
	RuntimeFormInterface generalForm = FormUtil.findForm(shellToolbar, shellFormName, generalFormTab, tabIdentifiers, state);
	String orderkey=generalForm.getFormWidgetByName("ORDERKEY").getDisplayValue();
	String externorderkey="";
	String validateAllocate=" select MBLNR,externorderkey from orders where orderkey='"+orderkey+"' and opstatus='2'";
	String mblnr="";
    EXEDataObject results=null;
    int count=0;
	try {
		results = WmsWebuiValidationSelectImpl.select(validateAllocate);
		
		
		if(results != null  && results.getRowCount()> 0)
		{
			count=Integer.parseInt(results.getAttribValue(0).getAsString());
			mblnr=results.getAttribValue(1).getAsString();
			externorderkey=results.getAttribValue(2).getAsString();
		}
	} catch (DPException e) {
		throw new UserException("", e.getMessage());
	}
	
	if(count==0)
	{
		throw new UserException("", "订单还未上传SAP，待上传后再进行冲销");
	}
	
	if(null==externorderkey || ("").equals(externorderkey))
	{
		throw new UserException("", "交货单为空不能冲销");
	}
	
	String reverse="1";
	
	JcoMessage jcoMessage=new JcoMessage();
	jcoMessage.setTask_name("出库单冲销");
	
	if(null==mblnr || ("").equals(mblnr))
	{
	    
		final String mblnr_ret;
		final String mjahr_ret;
		Map configMap=new HashMap();
		initJcoParam(jcoMessage,configMap);
		Map param=new HashMap();
		param.put("I_VBELN", externorderkey);
		param.put("I_TCODE", "VL09");
		param.put("I_BUDAT", new Date(Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DATE)));
		JcoMain jcoMain=new JcoMain();
		jcoMain.read(new JcoUtil(
				 getV(configMap, "JCO.CLINET"),
				 getV(configMap, "JCO.USER"),
				 getV(configMap, "JCO.PWD"),
				 getV(configMap, "JCO.ASHOST"),
				 getV(configMap, "JCO.SYSNR"),
				 getV(configMap, "JCO.REPOSITORY_NAME"),
			   "ZWS_REVERSE_GOODS_ISSUE","RETURN"){
			@Override
			public void inputTable(ParameterList inputTable,String inputTableName,List inputTableMap) {
			 
			}
			
			public void outputParam(ParameterList outputParam) {
				
			}

			public void outputTable(ParameterList outputTable,JcoMessage jcoMessage,int seq) {
				JCO.Table OT_INFO=outputTable.getTable("ES_EMKPF");
				
				if(OT_INFO!=null)
				{
					   if(OT_INFO.getNumRows()==0)
					   {
						 return;
					   }
						try {
							jcoMessage.getMap().put("MBLNR", OT_INFO.getField("MBLNR").getString());
							jcoMessage.getMap().put("MJAHR", OT_INFO.getField("MJAHR").getString());
						} catch (Exception e) {
							jcoMessage.setFlag(false);
							jcoMessage.setMsg(e.getMessage());
				}
				
			    }
			}
		   },jcoMessage,null,0);
	   
	}else
	{
		 reverse="0";
	}
	
	if(!jcoMessage.isFlag())
	{
		throw new UserException("", jcoMessage.getMsg());
	}
	
	
	InitialContext iniContext = null;
	DataSource dataSource = null;
	Connection conn = null;
	CallableStatement proc = null;
	PreparedStatement qqPrepStmt = null;
	ResultSet qqResultSet = null;
	
	try {
		iniContext = new InitialContext();
		String ds_name=context.getServiceManager().getUserContext().get(SetIntoHttpSessionAction.DB_USERID).toString();
		//Class.forName("net.sourceforge.jtds.jdbc.Driver");
		dataSource = (DataSource)iniContext.lookup("java:"+ds_name);
		conn = dataSource.getConnection(); // 得到连接
		proc = conn.prepareCall("{call pkg_reverse.prc_ship_reverse (?,?,?,?)}");	// 调用存储过程 
		proc.setString(1, orderkey); 		// 给输入参数传值
		proc.setString(2, reverse); 
		proc.setString(3, jcoMessage.getMap().get("MBLNR").toString()); 
		proc.setString(4, jcoMessage.getMap().get("MJAHR").toString()); 
		proc.execute();            		// 执行
	} catch (Exception e) {
		throw new UserException("", new Object[]{e.getMessage()}); 
	}finally{
		try{
        	if(proc!=null){proc.close();}
            if(conn!=null){conn.close();}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
	}
	
	
	return RET_CONTINUE;
	
	}
	
	public void initJcoParam(JcoMessage jcoMessage,Map returnMap)
	   {
		   Properties props=new Properties();
		   try{
			   InputStream ips=this.getClass().getResourceAsStream("/config.properties");
			   props.load(ips);
			   String client=props.getProperty("JCO.CLINET");
			   returnMap.put("JCO.CLINET", client);
			   
			   String user=props.getProperty("JCO.USER");
			   returnMap.put("JCO.USER", user);
			   
			   String ashost=props.getProperty("JCO.ASHOST");
			   returnMap.put("JCO.ASHOST", ashost);
			   
			   String sysnr=props.getProperty("JCO.SYSNR");
			   returnMap.put("JCO.SYSNR", sysnr);
			   
			   String pwd=props.getProperty("JCO.PWD");
			   returnMap.put("JCO.PWD", pwd);
			   
			   String repository_name=props.getProperty("JCO.REPOSITORY_NAME");
			   returnMap.put("JCO.REPOSITORY_NAME", repository_name);
			   
			   ips.close();
		   }catch(Exception ex)
		   {
			   jcoMessage.setFlag(false);
			   jcoMessage.setMsg(ex.getMessage());
		   }
	   }
	
	 public  String getV(Map map,String key)
	  {
		  return map.get(key).toString();
	  }
}
