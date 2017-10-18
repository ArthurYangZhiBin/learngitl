package com.ssaglobal.scm.wms.wm_shipmentorder.ui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.SsaAccessBase;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationInsertImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.uiextensions.KeyGenBioWrapper;

public class PopulatefromLPNAction extends ActionExtensionBase {
	protected static String TAB_0 = "tab 0";
	protected static String TAB_GROUP_SLOT = "tbgrp_slot";
	protected static String SLOT_1 = "list_slot_1";
	protected static String SLOT_2 = "list_slot_2";
	protected static String SETTINGS_TABLE = "wm_system_settings";
	/****************************************************************************
	 * Dec-03-2008 Krishna Kuchipudi: Modified createOrderAndOrderLineItems method, for the issue SDIS SCM-00000-05930
	 ****************************************************************************/

	protected int execute(ModalActionContext context, ActionResult result) throws EpiException {
		//Get the shell
		RuntimeFormInterface tabGroupShellForm = (context.getSourceForm().getParentForm(context.getState()));
		
			//Perform action
			result.setFocus(createOrderAndOrderLineItems(context, result, tabGroupShellForm));
		return RET_CONTINUE;
	}

	private DataBean createOrderAndOrderLineItems(ModalActionContext context, ActionResult result, RuntimeFormInterface tabGroupShellForm) throws EpiException {
        /**
         * MODIFICATION HISTORY
         * 10-14-2008  SCM  Updated set arguments to auto-fill UDF fields from PO (Machine#2044002_SDIS#05070)
         * Dec-03-2008 Krishna Kuchipudi: Modified for the issue SDIS SCM-00000-05930
         * 04/16/2009	HC	SCM-00000-05753
         * 					Get the tarrif key from the SKU and populate it in receiptdetail.

         */
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		BioBean soBioBean = null;
		RuntimeListFormInterface lpnListForm = (RuntimeListFormInterface)context.getModalBodyForm(0);
		
		SlotInterface headerSlot = tabGroupShellForm.getSubSlot(SLOT_1);		
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		SlotInterface tabGroupSlot = headerForm.getSubSlot(TAB_GROUP_SLOT);
		RuntimeFormInterface soForm = state.getRuntimeForm(tabGroupSlot, TAB_0);
		boolean firstLine = true;

		QBEBioBean soQBEBioBean = null;
		QBEBioBean soDetailQBEBioBean = null;
		BioBean soDetailBioBean = null;
		int maxLineNumber = 0;
		String newLineNumber = null;
		
		
		ArrayList arrayList=new ArrayList();
		arrayList=lpnListForm.getAllSelectedItems();
		
		if(arrayList==null || arrayList.size()==0)
		{
			throw new UserException("", new Object[]{"����ѡ��"});
		}
		
		
		//---2.check if new receipt
		if(soForm.getFocus() instanceof QBEBioBean){
			soQBEBioBean=((QBEBioBean)soForm.getFocus());
			soBioBean= uowb.getNewBio(soQBEBioBean);
			maxLineNumber = 0;
			
		}else
		{
			soBioBean = (BioBean)soForm.getFocus();
			maxLineNumber = getMaxLineNumber(soBioBean);
		}
		ArrayList<Temp_param> list=new ArrayList<Temp_param>();
        for (int j=0; j<arrayList.size(); j++){
        	BioBean poDetailBio = (BioBean)arrayList.get(j);
        	Temp_param temp_param=new Temp_param();
        	//receiptBioBean.get("ORDERKEY")
        	if((firstLine) & (soForm.getFocus() instanceof QBEBioBean)){
        		soBioBean.setValue("STORERKEY", poDetailBio.get("STORERKEY").toString());
        		SlotInterface detailSlot = tabGroupShellForm.getSubSlot(SLOT_2);		
        		RuntimeFormInterface detailForm = state.getRuntimeForm(detailSlot, null);
        		SlotInterface detailTabGroupSlot = detailForm.getSubSlot(TAB_GROUP_SLOT);
        		RuntimeFormInterface receiptDetailForm = state.getRuntimeForm(detailTabGroupSlot, TAB_0);
        		soDetailQBEBioBean = (QBEBioBean)receiptDetailForm.getFocus();
    			soDetailBioBean = uowb.getNewBio(soDetailQBEBioBean);
				firstLine = false;
			}else{
				soDetailBioBean = uowb.getNewBio("wm_orderdetail");	
			}
        	newLineNumber = generateNewNumber(maxLineNumber,uowb);
        	temp_param.orderlinenumber= newLineNumber;
        	temp_param.orderkey= soBioBean.get("ORDERKEY").toString();
        	temp_param.loc=poDetailBio.get("LOC").toString();
        	temp_param.id=poDetailBio.get("ID").toString();
        	soDetailBioBean.set("ORDERKEY",  soBioBean.get("ORDERKEY"));
        	soDetailBioBean.set("ORDERLINENUMBER",  newLineNumber);	
        	soDetailBioBean.set("STORERKEY", poDetailBio.get("STORERKEY"));
        	
        	soDetailBioBean.set("SKU", poDetailBio.get("SKU"));
        	temp_param.storerkey= poDetailBio.get("STORERKEY").toString();
        	temp_param.sku= poDetailBio.get("SKU").toString();
        	String sql="select sku.PACKKEY, s.PREALLOCATESTRATEGYKEY,s.ALLOCATESTRATEGYKEY,sku.ROTATEBY,sku.DEFAULTROTATION,p.PACKUOM3 from "
        		+" sku ,strategy s,pack p where sku.strategykey=s.strategykey and sku.packkey=p.packkey and sku.storerkey='"+poDetailBio.get("STORERKEY").toString()+"' and"
        		+" sku.sku='"+poDetailBio.get("SKU").toString()+"'";
        	
        	EXEDataObject results = WmsWebuiValidationSelectImpl.select(sql);
        	if(results!=null)
        	{
        		//String packkey=results.getAttribValue(1).getAsString();
        		//soDetailBioBean.set("PACKKEY",results.getAttribute(1));
        		temp_param.packkey= results.getAttribValue(1).getAsString();
        		
        		//String preall=results.getAttribValue(2).getAsString();
        		//soDetailBioBean.set("PREALLOCATESTRATEGYKEY", results.getAttribute(2));
        		
        		//String alloc=results.getAttribValue(3).getAsString();
        		//soDetailBioBean.set("ALLOCATESTRATEGYKEY",results.getAttribute(3));
        		
        		//String rotateby=results.getAttribValue(4).getAsString();
        		//soDetailBioBean.set("SKUROTATION",results.getAttribute(4));
        		
        		//String defaultrotation=results.getAttribValue(5).getAsString();
        		//soDetailBioBean.set("ROTATION", results.getAttribute(5));
        		
        		//String uom=results.getAttribValue(6).getAsString();
        		//soDetailBioBean.set("UOM", results.getAttribute(6));
        		temp_param.uom= results.getAttribValue(6).getAsString();
        		
        		
        	}
        	
        	/*
        	sql="select l.lottable01,l.lottable02,l.lottable03,l.lottable04,l.lottable05,l.lottable06,l.lottable07,l.lottable08,l.lottable09,l.lottable10"
        		+",l.lottable11,l.lottable12 from lotattribute l where l.lot='"+poDetailBio.get("LOT").toString()+"'";
        	results = WmsWebuiValidationSelectImpl.select(sql);
        	if(results!=null)
        	{
        		DateFormat dateFormatLong = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");  
        		soDetailBioBean.set("LOTTABLE01",results.getAttribute(1));
        		soDetailBioBean.set("LOTTABLE02",results.getAttribute(2));
        		soDetailBioBean.set("LOTTABLE03",results.getAttribute(3));
        		//String lot04=results.getAttribValue(1).getAsString();
        		String lot04=results.getAttribValue(4).getAsString();
        		DateTimeNullable lottable04 = new DateTimeNullable();
        		
        		if(null==lot04 || ("N/A").equals(lot04)|| (" ").equals(lot04))
        		{
        			
        			soDetailBioBean.set("LOTTABLE04",null);
        		}else
        		{
        			lottable04.setValue(lot04);
        			Date utcDate =lottable04.dateValue();
        			try{
        				soDetailBioBean.set("LOTTABLE04",dateFormatLong.format(utcDate));
        			}catch(Exception e){
        		
        			}
        			
        			
        		}
        		String lot05=results.getAttribValue(5).getAsString();
        		DateTimeNullable lottable05 = new DateTimeNullable();
        		if(null==lot05 || ("N/A").equals(lot05)|| (" ").equals(lot05))
        		{
        			
        			soDetailBioBean.set("LOTTABLE05",null);
        		}else
        		{
        			//soDetailBioBean.set("LOTTABLE05",new DateTimeNullable(lot05));
        			lottable05.setValue(lot05);
        			Date utcDate =lottable05.dateValue();
        			try{
        				soDetailBioBean.set("LOTTABLE05",dateFormatLong.format(utcDate));
        			}catch(Exception e){
        		
        			}
        		}
        		
        		//getDateTime(results.getAttribValue(4).getAsString()));
        		//soDetailBioBean.set("LOTTABLE05",getDateTime(results.getAttribValue(5).getAsString()));
        		soDetailBioBean.set("LOTTABLE06",results.getAttribute(6));
        		soDetailBioBean.set("LOTTABLE07",results.getAttribute(7));
        		soDetailBioBean.set("LOTTABLE08",results.getAttribute(8));
        		soDetailBioBean.set("LOTTABLE09",results.getAttribute(9));
        		soDetailBioBean.set("LOTTABLE10",results.getAttribute(10));
        		String lot11=results.getAttribValue(11).getAsString();
        		DateTimeNullable lottable11 = new DateTimeNullable();
        		if(null==lot11 || ("N/A").equals(lot11)|| (" ").equals(lot11))
        		{
        			
        			soDetailBioBean.set("LOTTABLE11",null);
        		}else
        		{
        			//soDetailBioBean.set("LOTTABLE11",new DateTimeNullable(lot11));
        			lottable11.setValue(lot11);
        			Date utcDate =lottable11.dateValue();
        			try{
        				//soDetailBioBean.set("LOTTABLE11",dateFormatLong.format(utcDate));
        			}catch(Exception e){
        		
        			}
        			
        		}
        		
        		String lot12=results.getAttribValue(12).getAsString();
        		DateTimeNullable lottable12 = new DateTimeNullable();
        		if(null==lot12 || ("N/A").equals(lot12)|| (" ").equals(lot12))
        		{
        			
        			soDetailBioBean.set("LOTTABLE12",null);
        		}else
        		{
        			//soDetailBioBean.set("LOTTABLE12",new DateTimeNullable(lot12));
        			lottable12.setValue(lot12);
        			Date utcDate =lottable12.dateValue();
        			try{
        				//soDetailBioBean.set("LOTTABLE12",dateFormatLong.format(utcDate));
        			}catch(Exception e){
        		
        			}
        			
        		}
        		//soDetailBioBean.set("LOTTABLE11",getDateTime(results.getAttribValue(11).getAsString()));
        		//soDetailBioBean.set("LOTTABLE12",getDateTime(results.getAttribValue(12).getAsString()));
        	}*/
        	soDetailBioBean.set("LOT", poDetailBio.get("LOT"));
        	temp_param.lot=poDetailBio.get("LOT").toString();
        	soDetailBioBean.set("OPENQTY", poDetailBio.get("QTYAVAILABLE"));
        	temp_param.qty=Double.parseDouble(poDetailBio.get("QTYAVAILABLE").toString());
        	soDetailBioBean.set("ORIGINALQTY", poDetailBio.get("QTYAVAILABLE"));
        	
        	soDetailBioBean.set("ALLOCATESTRATEGYTYPE","1");
        	list.add(temp_param);
        	
        	// 2012-07-03
		    // Modified by Will Pu
		    // 新增更新
	    	String lottable02 = "", lottable09 = "", lottable10 = "";
	    	
		    HttpSession session = context.getState().getRequest().getSession();

			String facilityName = session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();

			String query = "SELECT LOTTABLE02, LOTTABLE09, LOTTABLE10 FROM LOTATTRIBUTE " +
					"WHERE SKU = '" + temp_param.sku + "' AND STORERKEY = '" + temp_param.storerkey + "' AND LOT = '" + temp_param.lot + "'";

			try {
				SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();

				ResultSet rs = appAccess.getResultSet(facilityName.toUpperCase(), query, new Object[0]);

				if (rs.next()) {
					lottable02 = rs.getString(1);
					lottable09 = rs.getString(2);
					lottable10 = rs.getString(3);
				}
				
				rs.close();
			} catch (SQLException e) {
			}
			
			String uom = "";
			
			query = "SELECT PACKUOM3 FROM PACK WHERE PACKKEY = '" + temp_param.packkey + "'";

			try {
				SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();

				ResultSet rs = appAccess.getResultSet(facilityName.toUpperCase(), query, new Object[0]);

				if (rs.next()) {
					uom = rs.getString(1);
				}

				rs.close();
			} catch (SQLException e) {
			}
			
			soDetailBioBean.set("PACKKEY", temp_param.packkey);
			soDetailBioBean.set("UOM", uom == null ? " " : uom);
			soDetailBioBean.set("LOTTABLE02", lottable02);
			soDetailBioBean.set("LOTTABLE09", lottable09);
			soDetailBioBean.set("EXTERNORDERKEY", lottable09);
			soDetailBioBean.set("LOTTABLE10", lottable10);
			soDetailBioBean.set("SALLLINENO", lottable10);
			
        	maxLineNumber+=1;
		}
		
	    uowb.saveUOW(true);
	    
	    for (Temp_param temp_param : list){
        	//BioBean poDetailBio = (BioBean)arrayList.get(j);
	    	KeyGenBioWrapper replenishments = new KeyGenBioWrapper();
			String pickedetailkey=replenishments.getKey("PICKDETAILKEY");
        	String insertSql = "INSERT INTO pickdetail " +
			"(SKU, LOC, LOT, PACKKEY, PICKDETAILKEY, UOM, " +
			"ID, QTY, STATUS, PICKHEADERKEY, ORDERKEY, " +
			"ORDERLINENUMBER, UOMQTY, CARTONGROUP, STORERKEY) " +
			"VALUES ('"+temp_param.sku+"', '"+temp_param.loc+"','" +temp_param.lot+"', '"+temp_param.packkey+"', '"+pickedetailkey+"', '6', " +
			" '"+temp_param.id+"',  "+temp_param.qty+", '0', ' ', '"+temp_param.orderkey+"', '"+temp_param.orderlinenumber+"', " +
			" 1.0, '', '"+temp_param.storerkey+"')";
        	
			try{
				WmsWebuiValidationInsertImpl.insert(insertSql);
				
			}catch(Exception e){
				//throw e;
				throw new UserException("", e.getMessage());
				
			}
	    }
		return (DataBean)soDetailBioBean;
	}
	
	private String generateNewNumber(int size, UnitOfWorkBean uowb)throws EpiException{
		String lineNumber = "";
		size+=1;
		String zeroPadding=null;
		String queryString = SETTINGS_TABLE+".CONFIGKEY = 'ZEROPADDEDKEYS'";
		Query bioQuery = new Query(SETTINGS_TABLE, queryString, null);
		BioCollectionBean bcBean = uowb.getBioCollectionBean(bioQuery);
		try{
			zeroPadding = bcBean.elementAt(0).get("NSQLVALUE").toString();
		}catch(EpiDataException e1){
			e1.printStackTrace();
		}
		if(zeroPadding.equalsIgnoreCase("1")){
			if(size<10000){
				lineNumber+="0";
				if(size<1000){
					lineNumber+="0";
					if(size<100){
						lineNumber+="0";
						if(size<10){
							lineNumber+="0";
						}
					}
				}
			}
		}

		lineNumber+=size;
		return lineNumber;
	}
	private int getMaxLineNumber(BioBean objBioBeanOrder)throws EpiException{
		Object max = null;
		BioCollectionBean ReceiptDetailBiocollection = (BioCollectionBean)objBioBeanOrder.get("ORDER_DETAIL");
		if(ReceiptDetailBiocollection.size()!=0){
			max = findMax(ReceiptDetailBiocollection, "ORDERLINENUMBER");
		}else{
			max = "0";
		}
		int size = Integer.parseInt(max.toString());
		return size;
	}
	private long findMax(BioCollection bioCollection, String field) throws EpiDataException{
		TreeSet<Long> tree = new TreeSet<Long>();
		for(int index = 0; index<bioCollection.size(); index++){				
			Object bioFieldValueObj = bioCollection.elementAt(index).get(field);
			try{
				long tempBioLongValue = Long.parseLong(bioFieldValueObj.toString());
				Long bioFieldValue = new Long(tempBioLongValue);
				tree.add(bioFieldValue);
			}catch(NumberFormatException e1){					
			}
		}
		return ((Long)tree.last()).longValue();
	}

	class Temp_param{
		public String lot;
		public String sku;
		public String storerkey;
		public String id;
		public String orderkey;
		public String orderlinenumber;
		public double qty;
		public String packkey;
		public String uom;
		public String loc;
		
	}
}
