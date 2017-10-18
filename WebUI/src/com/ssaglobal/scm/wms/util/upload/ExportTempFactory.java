/*******************************************************************************
 *                         NOTICE                            
 *                                                                                
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS             
 * CONFIDENTIAL INFORMATION OF INFOR AND/OR ITS AFFILIATES   
 * OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED WITHOUT PRIOR  
 * WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND       
 * ADAPT THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH  
 * THE TERMS OF THEIR SOFTWARE LICENSE AGREEMENT.            
 * ALL OTHER RIGHTS RESERVED.                                                     
 *                                                           
 * (c) COPYRIGHT 2009 INFOR.  ALL RIGHTS RESERVED.           
 * THE WORD AND DESIGN MARKS SET FORTH HEREIN ARE            
 * TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR          
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS        
 * RESERVED.  ALL OTHER TRADEMARKS LISTED HEREIN ARE         
 * THE PROPERTY OF THEIR RESPECTIVE OWNERS.                  
 *******************************************************************************/
package com.ssaglobal.scm.wms.util.upload;

import java.util.ArrayList;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.HelperBio;
import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.data.bio.impl.ArrayListBioRefSupplier;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.extensions.ExtensionBaseclass;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.multifacilityt_balances_charts.MultifacbalDTO;
import com.ssaglobal.scm.wms.wm_inventory_balance.ui.InventoryBalancesRefresh;
import com.ssaglobal.scm.wms.wm_inventory_balance.ui.InventoryBalancesRefreshLPN;

public class ExportTempFactory {

	//public UnitOfWorkBean uowb = null;
	//public UnitOfWork uow = null;
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ExportTempFactory.class);
	
	public BioCollectionBean getTempCollection(String template, StateInterface state, 
			UnitOfWorkBean uowb, UnitOfWork uow)
		throws EpiDataException, EpiException{
		BioCollectionBean bioCollectionBean = null;
		
		
		if(template.equalsIgnoreCase("MFBALTPL")){
    		ArrayList<MultifacbalDTO> multifacbalList = 
    			(ArrayList<MultifacbalDTO>)state.getRequest().getSession().getAttribute("MFDataBean");

    		bioCollectionBean = (BioCollectionBean)loadBioFromList(multifacbalList, state, uowb, uow);

    	}else if(template.equalsIgnoreCase("MFBIBTPL")){


    		_log.debug("LOG_DEBUG_EXTENSION_EXPORTOXLSEXT","jpdebug:invoking InventoryBalancesRefresh().buildFocusFromQuery", 100L);
    		
    		
    		bioCollectionBean = new InventoryBalancesRefresh().buildFocusFromQuery(
    				state, uowb);

    	}else if(template.equalsIgnoreCase("MFBLPNTPL")){


    		bioCollectionBean = new InventoryBalancesRefreshLPN().buildFocusFromQuery(
    				state, uowb, false);
    	}

		return bioCollectionBean;
	}
	
	
	 
    private DataBean loadBioFromList(ArrayList<MultifacbalDTO> list, StateInterface state, 
    		UnitOfWorkBean uowb, UnitOfWork uow) throws EpiDataException{
    	if(list==null || list.size()==0)
    		return null;
    	
    	
    	DataBean db = null;
    	ArrayListBioRefSupplier tempBioCollRefArray = new ArrayListBioRefSupplier("wm_multifacbal");	
		HelperBio helper = null;
		
		
		//uowb = state.getTempUnitOfWork();
		//UnitOfWork uow=null;
		//uow = uowb.getUOW();

		try {
			helper = uow.createHelperBio("wm_multifacbal");
		} catch (EpiDataException e1) {
			e1.printStackTrace();
			String args[] = new String[0]; 
			String errorMsg = ExtensionBaseclass.getTextMessage("WMEXP_SYS_EXP",args,state.getLocale());
			//throw new UserException(errorMsg,new Object[0]);
		}
    	
		Bio bio = null;
		int idx=0;
    	for (MultifacbalDTO multifacbalDTO : list){
    		bio = uow.createBio(helper);
    		bio.set("OWNER", multifacbalDTO.getOwner());
    		bio.set("ONHAND", multifacbalDTO.getOnhand());
    		bio.set("QTYINTRANSIT", multifacbalDTO.getQtyintransit());
    		bio.set("PICKED", multifacbalDTO.getPicked());
    		bio.set("ITEM", multifacbalDTO.getItem());
    		bio.set("ALLOCATED", multifacbalDTO.getAllocated());
    		bio.set("WAREHOUSE", multifacbalDTO.getWarehouse());
    		bio.set("DISTRIBUTIONCENTER", multifacbalDTO.getDistributioncenter());
    		bio.set("DIVISION", multifacbalDTO.getDivision());
    		bio.set("AVALIABLE", multifacbalDTO.getAvaliable());
    		bio.set("SERIALKEY", new Integer(++idx));
    		
			tempBioCollRefArray.add(bio.getBioRef());
			BioCollection bc = (uow).fetchBioCollection(tempBioCollRefArray);								
			db = (DataBean)(uowb.getBioCollection(bc.getBioCollectionRef()));

    	}
    	
    	return db;
    	
    }
    
    
}
