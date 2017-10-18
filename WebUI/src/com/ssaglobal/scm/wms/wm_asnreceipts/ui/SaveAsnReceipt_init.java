package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.agileitp.forte.framework.internal.ServiceObjectException;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.UnitOfWorkException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.GUIDFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.uiextensions.KeyGenBioWrapper;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;
import com.ssaglobal.scm.wms.wm_cyclecount.ui.CalculateAdvCatchWeightsHelper;
import com.ssaglobal.scm.wms.wm_table_validation.ccf.NumericValidationCCF;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.apache.commons.configuration.Configuration;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.DateTimeNullable;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.dp.exception.DPException;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.data.error.EpiDataInvalidAttrException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.model.UserInterface;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.GUIDFactory;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.SsaAccessBase;
import com.ssaglobal.base.Config;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.service.dutilitymanagement.SerialNumberObj;
import com.ssaglobal.scm.wms.service.dutilitymanagement.dto.SerialNoDTO;
import com.ssaglobal.scm.wms.service.dutilitymanagement.dto.SkuSNConfDTO;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.FormUtil;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.util.StringUtils;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;
import com.ssaglobal.scm.wms.util.dao.SkuSNConfDAO;
import com.ssaglobal.scm.wms.wm_cyclecount.ui.CalculateAdvCatchWeightsHelper;

import com.ssaglobal.scm.wms.wm_table_validation.ccf.NumericValidationCCF;
public class SaveAsnReceipt_init extends ActionExtensionBase {
	protected int execute(ActionContext context, ActionResult result ) throws EpiException, UserException {		
		String shellSlot1 = "list_slot_1";
		String shellSlot2 = "list_slot_2";
		String toggleFormSlot = "wm_receiptdetail_toggle";
		String detailBiocollection = "RECEIPTDETAILS";

		String detailListTabName = "wm_receiptdetail_list_view";
		ArrayList parms = new ArrayList(); 		
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();

		/*得到主toolbar*/
		RuntimeFormInterface toolbar = FormUtil.findForm(state.getCurrentRuntimeForm(), "", "wm_shell_list_asn_receipts Toolbar", state);									//get the toolbar
		String error = toolbar.getError();
		if ( error != null )
		{
			context.setNavigation("clickEventNormal");			
			return RET_CONTINUE;
		}

		RuntimeFormInterface shellForm = toolbar.getParentForm(state);			//get the Shell form <wms_list_shell>
		DataBean headerFocus = state.getRuntimeForm(shellForm.getSubSlot(shellSlot1), null).getFocus();	//Get the header form focus bio<receipt>
		
		RuntimeFormInterface detailForm = state.getRuntimeForm(shellForm.getSubSlot(shellSlot2), null);	//Get the form at slot2
		RuntimeFormInterface detailTab= null;		
		
		RuntimeListFormInterface detailListTab = null;		//Detail List
		if (detailForm.getName().equalsIgnoreCase("wm_receiptdetail_toggle_view"))
		{	//if the slot is populated by toggle form then		
			//detailTab = state.getRuntimeForm(detailForm.getSubSlot(toggleFormSlot), detailFormTab);
			int formNum = state.getSelectedFormNumber(detailForm.getSubSlot(toggleFormSlot));
			detailTab = state.getRuntimeForm(detailForm.getSubSlot(toggleFormSlot), formNum);
			detailListTab = (RuntimeListFormInterface) state.getRuntimeForm(detailForm.getSubSlot(toggleFormSlot), detailListTabName);
			parms.add("wm_receiptdetail_detail_view");
		}else if(detailForm.getName().equalsIgnoreCase("wms_tbgrp_shell"))
		{
			//新增ASN订单并且有收货量时，更新值
			if(headerFocus.isTempBio()) 
			{
				RuntimeFormInterface detailTabNew_tab0=null;
				RuntimeFormInterface detailTabNew_tab1=null;
				detailTabNew_tab0=state.getRuntimeForm(detailForm.getSubSlot("tbgrp_slot"), "tab 0");
				String expectedQty = detailTabNew_tab0.getFormWidgetByName("EXPECTEDQTY").getDisplayValue();
				double dexpqty = NumericValidationCCF.parseNumber(expectedQty);
				if(dexpqty>0)
				{
					detailTabNew_tab1=state.getRuntimeForm(detailForm.getSubSlot("tbgrp_slot"), "tab 1");
					SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd"); //制定日期格式
					Calendar c=Calendar.getInstance();
					//Date date=new Date(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE));
					//c.setTime(date);
					detailTabNew_tab1.setProperty("LOTTABLE04", new Date(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE)));
					detailTabNew_tab1.setProperty("LOTTABLE11", new Date(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE)));
					c.add(Calendar.MONTH,1); //将当前日期加一个月
					String validityDate=df.format(c.getTime()); 
					detailTabNew_tab1.setProperty("LOTTABLE05", new Date(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE)));
					String lot=detailTabNew_tab1.getProperty("LOTTABLE01").toString();
					
					if(lot==null || ("").equals(lot.trim()))
					{
						try {
							lot=getlotId(detailTabNew_tab1.getProperty("LOTTABLE01").toString(),detailTabNew_tab1.getProperty("LOTTABLE02").toString(),
									detailTabNew_tab1.getProperty("LOTTABLE03").toString(),
									(Date)detailTabNew_tab1.getProperty("LOTTABLE04"),
									(Date)detailTabNew_tab1.getProperty("LOTTABLE05"),
									detailTabNew_tab1.getProperty("LOTTABLE06").toString(),
									detailTabNew_tab1.getProperty("LOTTABLE07").toString(),detailTabNew_tab1.getProperty("LOTTABLE08").toString(),
									detailTabNew_tab1.getProperty("LOTTABLE09").toString(),detailTabNew_tab1.getProperty("LOTTABLE010").toString(),
									(Date)detailTabNew_tab1.getProperty("LOTTABLE11"),
									(Date)detailTabNew_tab1.getProperty("LOTTABLE12"),
									//detailTabNew_tab1.getProperty("LOTTABLE011"),detailTabNew_tab1.getProperty("LOTTABLE012"),
									detailTabNew_tab1.getProperty("STORERKEY").toString(),detailTabNew_tab1.getProperty("SKU").toString());
						} catch (Exception e) {
							
							throw new UserException("WPEXE_APP_ERROR", new Object[]{e.getMessage()});
						}
						detailTabNew_tab1.setProperty("LOTTABLE01", lot);
					}
					
				}
			}
			
		}
		
		/*
		state.getRuntimeForm(detailForm.getSubSlot("tbgrp_slot"), "tab 0").getName()
		SlotInterface tabGroupSlot = getTabGroupSlot(state);detailForm.getName()*/
		DataBean detailFocus = null;
		BioBean headerBioBean = null;
		if(headerFocus.isBioCollection()) 
		{			
			
		} // end saving from asn header list view
		else
		{
			//saving from asn header detail view
			if(headerFocus.isTempBio()) 
			{
				
				
			}
			else
			{//it is for update header
			  headerBioBean = (BioBean)headerFocus;
				
			}
		}

		//************************** LPN Validation**************************
		if(detailForm.getName().equalsIgnoreCase("wm_receiptdetail_toggle_view"))
		{	//if the slot is populated by toggle form then
			if(!detailTab.getName().equals("wms_ASN_Line_List_View"))
			{
				
			}
		}
		else
		{
		}



		

		return RET_CONTINUE;
	}
	
	private String getlotId(String lot01,String lot02,String lot03,Date lot04,Date lot05,String lot06,
			String lot07,String lot08,String lot09,String lot10,Date lot11,Date lot12,
			String storerkey,String sku) throws Exception
	{
		
		StringBuffer sb=new StringBuffer();
		sb.append("select lot from lotattribute where storerkey='"+storerkey+"' and sku='"+sku+"' ");
		
		 if(null!=lot01 && !("").equals(lot01.trim()))
		 {
			 sb.append(" and Lottable01='"+lot01+"'");
		 }
		 if(null!=lot02 && !("").equals(lot02.trim()))
		 {
			 sb.append(" and Lottable02='"+lot02+"'");
		 }
		 if(null!=lot03 && !("").equals(lot03.trim()))
		 {
			 sb.append(" and Lottable03='"+lot03+"'");
		 }
		 SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		 if(null!=lot04)
		 {
			 String lot04Str=df.format(lot04);
			 sb.append(" and Lottable04 between to_date('"+lot04Str+"','yyyy-mm-dd'),'yyyy-mm-dd') and to_date('"+lot04Str+"' || ' 23:59:59','yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss')");
		 }
		 
		 if(null!=lot05)
		 {
			 String lot05Str=df.format(lot05);
			 sb.append(" and Lottable05 between to_date('"+lot05Str+"','yyyy-mm-dd'),'yyyy-mm-dd') and to_date('"+lot05Str+"' || ' 23:59:59','yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss')");
		 }
		 
		 if(null!=lot06 && !("").equals(lot06.trim()))
		 {
			 sb.append(" and Lottable06='"+lot06+"'");
		 }
		 if(null!=lot07 && !("").equals(lot07.trim()))
		 {
			 sb.append(" and Lottable07='"+lot07+"'");
		 }
		 if(null!=lot08 && !("").equals(lot08.trim()))
		 {
			 sb.append(" and Lottable08='"+lot08+"'");
		 }
		 
		 if(null!=lot09 && !("").equals(lot09.trim()))
		 {
			 sb.append(" and Lottable09='"+lot09+"'");
		 }
		 if(null!=lot10 && !("").equals(lot10.trim()))
		 {
			 sb.append(" and Lottable10='"+lot10+"'");
		 }
		 
		 if(null!=lot11)
		 {
			 String lot11Str=df.format(lot11);
			 sb.append(" and Lottable11 between to_date('"+lot11Str+"','yyyy-mm-dd'),'yyyy-mm-dd') and to_date('"+lot11Str+"' || ' 23:59:59','yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss')");
		 }
		 
		 if(null!=lot12)
		 {
			 String lot12Str=df.format(lot12);
			 sb.append(" and Lottable12 between to_date('"+lot12Str+"','yyyy-mm-dd'),'yyyy-mm-dd') and to_date('"+lot12Str+"' || ' 23:59:59','yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss')");
		 }
		  InitialContext iniContext = null;
			DataSource dataSource = null;
			Connection conn = null;
			//CallableStatement proc = null;
			PreparedStatement qqPrepStmt = null;
			ResultSet qqResultSet = null;
			String lot="";
			boolean flag=true;
		try {

			iniContext = new InitialContext();
			dataSource = (DataSource)iniContext.lookup("java:"+"HOMEPAGE");
			conn = dataSource.getConnection(); // 得到连接
			qqPrepStmt = conn.prepareStatement(sb.toString());
			qqResultSet=qqPrepStmt.executeQuery();
			if(qqResultSet.next())
			{
				lot=qqResultSet.getString(1);
				flag=false;
			}
			
			if(flag)
			{
				String sql="insert into LOTATTRIBUTE(STORERKEY,SKU,LOT,LOTTABLE01,LOTTABLE02,LOTTABLE03,LOTTABLE04,LOTTABLE05,LOTTABLE06,LOTTABLE07,LOTTABLE08,LOTTABLE09,LOTTABLE10,LOTTABLE11,LOTTABLE12) "
					+" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				qqPrepStmt = conn.prepareStatement(sql);
				int i=1;
				KeyGenBioWrapper keyGenBioWrapper = new KeyGenBioWrapper();
				lot=keyGenBioWrapper.getKey("LOT");
				qqPrepStmt.setString(i++, storerkey);qqPrepStmt.setString(i++, sku);
				qqPrepStmt.setString(i++, lot);
				qqPrepStmt.setString(i++, lot01);qqPrepStmt.setString(i++, lot02);
				qqPrepStmt.setString(i++, lot03);qqPrepStmt.setDate(i++, lot04);
				qqPrepStmt.setDate(i++, lot05);qqPrepStmt.setString(i++, lot06);
				qqPrepStmt.setString(i++, lot07);qqPrepStmt.setString(i++, lot08);
				qqPrepStmt.setString(i++, lot09);qqPrepStmt.setString(i++, lot10);
				qqPrepStmt.setDate(i++, lot11);qqPrepStmt.setDate(i++, lot12);
				qqPrepStmt.execute();
			}
		} catch (Exception e) {
           throw e;

		}finally{
			try{
				if(qqResultSet!=null){qqResultSet.close();}
	        	if(qqPrepStmt!=null){qqPrepStmt.close();}
	            if(conn!=null){conn.close();}
        	}catch(Exception e){
        		throw e;
        	}
		}
		return lot;
	}

	private SlotInterface getTabGroupSlot(StateInterface state)
	{
		//Common 
		RuntimeFormInterface shellToolbar = FormUtil.findForm(state.getCurrentRuntimeForm(), "", "wm_shell_list_asn_receipts Toolbar", state);
		RuntimeFormInterface shellForm = shellToolbar.getParentForm(state);
		SlotInterface detailSlot = shellForm.getSubSlot("list_slot_2");
		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
		SlotInterface tabGroupSlot = detailForm.getSubSlot("tbgrp_slot");
		return tabGroupSlot;
	}
}
