package com.ssaglobal.scm.wms.wm_serial_discrepancy.ui;

//Import 3rd party packages and classes
import java.util.Iterator;

import com.agileitp.forte.framework.Array;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsImpl;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiActionsProperties;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.data.bio.Query;
import java.util.ArrayList;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationUpdateImpl;

public class IntransitSerialNumberMoving extends ActionExtensionBase {

	protected int execute(ModalActionContext context, ActionResult result) throws EpiException {
		//05/17/2011 FW  Added Serial Transaction Discrepancy Screen (Def311963)
		StateInterface state = context.getState();
		RuntimeFormInterface shellForm = (context.getSourceForm().getParentForm(context.getState()));
		
		while(!(shellForm.getName().equals("wms_list_shell"))){
			shellForm = shellForm.getParentForm(state);
		}
		
		SlotInterface slot1 = shellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(slot1, null);
		
		SlotInterface slot2 = shellForm.getSubSlot("list_slot_2");
		RuntimeListFormInterface detailListForm = (RuntimeListFormInterface)state.getRuntimeForm(slot2, null);
		
		String storer = headerForm.getFormWidgetByName("STORERKEY").getValue().toString();
		String sku = headerForm.getFormWidgetByName("SKU").getValue().toString();
		String tolot = headerForm.getFormWidgetByName("LOT").getValue().toString();
		String toloc = headerForm.getFormWidgetByName("LOC").getValue().toString();
		String toid = headerForm.getFormWidgetByName("ID").getValue().toString();
		Double inventoryQty = Double.parseDouble(headerForm.getFormWidgetByName("QTY").getValue().toString());
		
		BioCollectionBean serialInventory = (BioCollectionBean)detailListForm.getFocus();
		
		int serialCount = serialInventory.size();
		
		RuntimeListFormInterface serialInventoryListForm = (RuntimeListFormInterface)context.getModalBodyForm(0);
		ArrayList selectedItems = serialInventoryListForm.getSelectedItems();
		
		if ((serialCount + selectedItems.size()) > inventoryQty.intValue()){
			//show error messge 
			String[] parameters = new String[1];
			parameters[0] = "moving";
			throw new FormException("WMEXP_TOO_MANY_ITEMS", parameters); //Too many records selected in {}
		}
		
		if(selectedItems != null && selectedItems.size() > 0)
		{
			Iterator bioBeanIter = selectedItems.iterator();
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			BioBean bio;
			String fromlot = null;
			String fromloc = null;
			String fromid = null;
			
			//get itrnkey
			String qry1 = null;
			String lot = headerForm.getFormWidgetByName("LOT").getValue().toString();
			String loc = headerForm.getFormWidgetByName("LOC").getValue().toString();
			String id = headerForm.getFormWidgetByName("ID").getValue().toString();
			
			qry1 =        " wm_itrn.STORERKEY = '" + storer + "'";
			qry1 = qry1 + " AND wm_itrn.SKU =  '" + sku + "'";
			qry1 = qry1 + " AND wm_itrn.LOT =  '" + tolot + "'";
			qry1 = qry1 + " AND wm_itrn.TOLOC = '" + toloc + "'";
			qry1 = qry1 + " AND wm_itrn.TOID = '" + toid + "'";
			qry1 = qry1 + " AND wm_itrn.TRANTYPE = 'MV'";
			qry1 = qry1 + " AND wm_itrn.INTRANSIT = '0'";
			
			Query itrnQry = new Query("wm_itrn", qry1, "wm_itrn.EDITDATE DESC");  //order by editdate desc
			BioCollectionBean itrnQryFocus = uow.getBioCollectionBean(itrnQry);
			if(itrnQryFocus.size()<1){
				String[] parameters = new String[1];
				parameters[0] = "serial#";
				throw new FormException("EXP_ECS_CANNOT_FIND_FILE", parameters); //not found in serial#
			}
			
			String itrnKey = itrnQryFocus.get("0").getString("ITRNKEY").toString();				
							
			for(; bioBeanIter.hasNext();){
				bio = (BioBean)bioBeanIter.next();
				
				fromlot = bio.getValue("LOT").toString();
				fromloc = bio.getValue("LOC").toString();
				fromid  = bio.getValue("ID").toString();
				
				//Call backend to do serial moving
				WmsWebuiActionsProperties actionProperties = new WmsWebuiActionsProperties();
				Array parms = new Array();
				//pass the following parameters to call backend's procedure - "NSPRFSM01"
			
				//sendDelimiter,ptcid,userid,taskId,databasename,appflag,recordType,server,
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				
				//storerkey,sku,sourcekey,sourcelinenumber,lot,loc,fromloc,toloc,id,fromid,
				parms.add(new TextData(storer));
				parms.add(new TextData(sku));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData(bio.getValue("LOT").toString()));
				parms.add(new TextData(fromloc)); 
				parms.add(new TextData(fromloc));
				parms.add(new TextData(toloc));
				parms.add(new TextData(fromid)); 
				parms.add(new TextData(fromid));
				
				//caseid,dropid,uom1,packkey1,uom2,packkey2,qty,pickdetailkey,other2,other3,
				parms.add(new TextData(""));
				parms.add(new TextData(toid)); //dropid
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData(bio.getValue("QTY").toString()));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				
				//other4,other5,printerid,totalwgt,counter,rcv_counter,totallinenumber,data1,wgt1,data2,
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData("0"));
				parms.add(new TextData("0"));
				parms.add(new TextData("2")); //totallinenumber
				parms.add(new TextData(bio.getValue("SERIALNUMBER").toString()));
				parms.add(new TextData(""));
//				parms.add(new TextData(bio.getValue("DATA2").toString()));
				if (bio.getValue("DATA2") == null){
					parms.add(new TextData(""));
				}else {
					parms.add(new TextData(bio.getValue("DATA2").toString()));
				}

				
				//wgt2,data3,wgt3,data4,wgt4,data5,wgt5,data6,wgt6,data7,
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				
				//wgt7,data8,wgt8,data9,wgt9,data10,wgt10,processtype,trantype,itrnkey,lot,action
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData(""));
				parms.add(new TextData("move")); //processtype
				parms.add(new TextData("MV"));   //trantype
				parms.add(new TextData(itrnKey));
				parms.add(new TextData(tolot));
				parms.add(new TextData("update")); //action
				
				actionProperties.setProcedureParameters(parms);
				actionProperties.setProcedureName("NSPRFSM01");
				
				try {
					WmsWebuiActionsImpl.doAction(actionProperties);
				}  catch (WebuiException e) {
					// TODO Auto-generated catch block
					e.getMessage();
					UserException UsrExcp = new UserException(e.getMessage(), new Object[]{});
		 	   		throw UsrExcp;
				}
				parms = null;
				actionProperties = null;
			}
			if ((serialCount + selectedItems.size()) == inventoryQty.intValue()){
				//check serial discrepancy and update itrn to '1' if it is ok
				qry1 = null;
				qry1 =        " wm_serialinventory.STORERKEY = '" + storer + "'";
				qry1 = qry1 + " AND wm_serialinventory.SKU = '" + sku + "'";
				qry1 = qry1 + " AND wm_serialinventory.LOT = '" + tolot + "'";
				qry1 = qry1 + " AND wm_serialinventory.LOC = '" + toloc + "'";
				qry1 = qry1 + " AND wm_serialinventory.ID = '" + toid + "'";

				Query serialinventoryQry = new Query("wm_serialinventory", qry1, null);
				BioCollectionBean serialinventoryFocus = uow.getBioCollectionBean(serialinventoryQry);

				if ((serialCount + serialinventoryFocus.size()) == inventoryQty.intValue()){
					WmsWebuiValidationUpdateImpl.update("UPDATE ITRN SET INTRANSIT = '1' WHERE Itrnkey = '" + itrnKey +"'"); 
				}
			}
		}
			
		return RET_CONTINUE;
	}
}