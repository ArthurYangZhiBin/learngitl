package com.ssaglobal.scm.wms.wm_trailer.ui;


import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.exceptions.FormException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.state.StateInterface;
import java.util.ArrayList;

public class TrailerPreDeleteValidation extends ActionExtensionBase{
	protected int execute(ActionContext context, ActionResult result) throws EpiException{
		StateInterface state = context.getState();
		String table1 = "receipt";
		String table2 = "wm_orders", widgetName = "TRAILERKEY";
		String[] parameter = new String[2];
		//Find location value selected for delete
		RuntimeFormInterface shellForm = state.getCurrentRuntimeForm().getParentForm(state);
		RuntimeListFormInterface listForm = (RuntimeListFormInterface)state.getRuntimeForm(shellForm.getSubSlot("list_slot_1"),null);
		ArrayList selected = listForm.getAllSelectedItems();

		if (selected==null || selected.isEmpty() )
			throw new FormException("WMEXP_RECORD_NOT_SEL", null);

		DataBean bean = (DataBean)selected.get(0);
		String trailerValue = bean.getValue(widgetName).toString();

		//Query against RECEIPT table for dependent records
		UnitOfWorkBean uow = state.getTempUnitOfWork();
		String queryString1 = table1+".TrailerNumber = '"+trailerValue+"' ";		
		BioCollectionBean list1 = state.getTempUnitOfWork().getBioCollectionBean(new Query(table1, queryString1, null));
		//If dependent records exist, block delete
		//list1.getSize();
		if(list1.size() >0){
			parameter[0]=trailerValue;
			parameter[1]="ASN";
			throw new FormException("WMEXP_TRAILER_DELETE_VALIDATION_ASN", parameter);
		}

		String queryString2 = table2+".TrailerNumber = '"+trailerValue+"' ";
		Query qry2 = new Query(table2, queryString2, null);
		BioCollectionBean list2 = uow.getBioCollectionBean(qry2);
		//If dependent records exist, block delete
		if(list2.size() >0){
			parameter[0]=trailerValue;
			parameter[1]="shipment order";
			throw new FormException("WMEXP_TRAILER_DELETE_VALIDATION_SO", parameter);
		}
		return RET_CONTINUE;
	}
}