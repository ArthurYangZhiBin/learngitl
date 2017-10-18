package com.infor.scm.waveplanning.wp_wm_wave.wave;

import javax.servlet.http.HttpSession;


import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationSelectImpl;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.infor.scm.waveplanning.wp_wavemgmt.util.WPWaveMgmtUtil;
import com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.ui.OrderSummaryObj;
import java.util.ArrayList;
import com.agileitp.forte.framework.TextData;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;

import com.infor.scm.waveplanning.wp_wm_wave.wave.*;
import com.infor.scm.waveplanning.wp_wm_wave.wave.actions.CreateWave;

public class WPUtil {
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPUtil.class);
	public static String getBaseLocale(EpnyUserContext userContext){
		String locale = userContext.getLocale();
		if (locale.indexOf("_") == -1){
			if (locale.equalsIgnoreCase("en")){
				locale = locale + "_US";
			}
			if (locale.equalsIgnoreCase("de")){
				locale = locale + "_DE";
			}
			if (locale.equalsIgnoreCase("es")){
				locale = locale + "_ES";
			}
			if (locale.equalsIgnoreCase("nl")){
				locale = locale + "_NL";
			}
			if (locale.equalsIgnoreCase("ja")){
				locale = locale + "_JP";
			}
			if (locale.equalsIgnoreCase("pt")){
				locale = locale + "_BR";
			}
			if (locale.equalsIgnoreCase("zh")){
				locale = locale + "_CN";
			}
			if (locale.equalsIgnoreCase("fr")){
				locale = locale + "_FR";
			}
		}
		return locale;
	}
	
	public static boolean isOracle(StateInterface state){
		HttpSession session = state.getRequest().getSession();
		String dbServerType = session.getAttribute(SetIntoHttpSessionAction.DB_TYPE).toString();
		if(WPConstants.DB_TYPE_ORACLE.equalsIgnoreCase(dbServerType)){
			return true;
		}
		return false;
	}
	
	public static boolean isSQL(StateInterface state){
		HttpSession session = state.getRequest().getSession();
		String dbServerType = session.getAttribute(SetIntoHttpSessionAction.DB_TYPE).toString();
		if(WPConstants.DB_TYPE_SQL.equalsIgnoreCase(dbServerType)){
			return true;
		}
		return false;
	}
	
	
	public static ArrayList<OrderSummaryObj> getOrderSummary(String waveKey) throws UserException{
		try{
			ArrayList <OrderSummaryObj> orderSummaryList = new ArrayList<OrderSummaryObj>();
			WaveInputObj waveInputObj = new WaveInputObj();
			waveInputObj.setProcedureName(WPConstants.GETWAVEORDERSUMMARY);
			waveInputObj.getProcedureParametes().add(new TextData(waveKey));
			EXEDataObject edo = WPStoreProcedureCallWrapper.doAction(waveInputObj);
			OrderSummaryObj orderSummaryObj = null;
			String orderKey = "";
			double totalEaches = 0.0;
			double totalCases = 0.0;
			double totalPallets = 0.0;
			double totalCube = 0.0;
			double totalNetWeight = 0.0;
			double totalGrossWeight = 0.0;
			
			
			
	        final int qqLoopTest = edo.getRowCount();
	        for (int I = 1; I <= qqLoopTest; I = I + 1) {
	            edo.setRow(I);
	            orderSummaryObj = new OrderSummaryObj();
	            EXEDataObject.GetStringOutputParam qqGetStringOutputParam = edo.getString(new TextData(
	                        "OrderKey"), orderKey);
	            orderKey = qqGetStringOutputParam.pResult;
	            orderSummaryObj.setOrderKey(orderKey);
	            
	            EXEDataObject.GetDoubleOutputParam qqGetDoubleOutputTotalEaches = edo.getDouble(new TextData("TotalEaches"), totalEaches);
	            totalEaches = qqGetDoubleOutputTotalEaches.pResult;
	            orderSummaryObj.setTotalEaches(totalEaches);

	            
	            EXEDataObject.GetDoubleOutputParam qqGetDoubleOutputTotalCases = edo.getDouble(new TextData("TotalCases"), totalCases);
	            totalCases = qqGetDoubleOutputTotalCases.pResult;
	            orderSummaryObj.setTotalCases(totalCases);

	            
	            EXEDataObject.GetDoubleOutputParam qqGetDoubleOutputTotalPallets = edo.getDouble(new TextData("TotalPallets"), totalPallets);
	            totalPallets = qqGetDoubleOutputTotalPallets.pResult;
	            orderSummaryObj.setTotalPallets(totalPallets);
	            

	            EXEDataObject.GetDoubleOutputParam qqGetDoubleOutputTotalCube = edo.getDouble(new TextData("TotalCube"), totalCube);
	            totalCube = qqGetDoubleOutputTotalCube.pResult;
	            orderSummaryObj.setTotalCube(totalCube);

	            
	            EXEDataObject.GetDoubleOutputParam qqGetDoubleOutputTotalNetWeight = edo.getDouble(new TextData("TotalNetWeight"), totalNetWeight);
	            totalNetWeight = qqGetDoubleOutputTotalNetWeight.pResult;
	            orderSummaryObj.setTotalNetWeight(totalNetWeight);

	            
	            EXEDataObject.GetDoubleOutputParam qqGetDoubleOutputTotalGrossWeight = edo.getDouble(new TextData("TotalGrossWeight"), totalGrossWeight);
	            totalGrossWeight = qqGetDoubleOutputTotalGrossWeight.pResult;
	            orderSummaryObj.setTotalGrossWeight(totalGrossWeight);
	            orderSummaryList.add(orderSummaryObj);
	        }
	        return orderSummaryList;
		}catch(WebuiException ex){
			ex.printStackTrace();
			throw new UserException(ex.getMessage(), new Object[0]);
		}
	}
	
	
	
	public static void populateConsolidate(StateInterface state, String waveKey) throws UserException{
		try{
			//clean up temp tables
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			WPWaveMgmtUtil.clearTempTableBasedOnAddDate(state, "wp_speedlinelocationswaveheadervotemp");
			WPWaveMgmtUtil.clearTempTableBasedOnAddDate(state, "wp_eligibleconsolidatedskulistwaveheadervotemp");
			final String interactionId = state.getInteractionId();
			final Query speedLineQuery = new Query("wp_speedlinelocationswaveheadervotemp",
					"wp_speedlinelocationswaveheadervotemp.INTERACTIONID = '" + interactionId
							+ "' and wp_speedlinelocationswaveheadervotemp.WAVEKEY = '" + waveKey + "'", null);
			BioCollectionBean rs = uow.getBioCollectionBean(speedLineQuery);
			for (int i = 0; i < rs.size(); i++)
			{
				final BioBean row = rs.get("" + i);
				row.delete();
			}
			uow.saveUOW();
			//populate speed line temp table
	        String loc ="";
	        String locDesc = "";
	        QBEBioBean qbeSpeed = null;
	        String sql = "SELECT LOC.LOC Loc, LOC.LOC Description FROM LOC WHERE UPPER(LocationType) = 'SPEED-PICK' AND LOC NOT IN ( Select LOC From SKUxLOC)";
	        EXEDataObject edoSpeedLine = WmsWebuiValidationSelectImpl.select(sql);
	        final int qqLoopSp = edoSpeedLine.getRowCount();
	        for (int I = 1; I <= qqLoopSp; I = I + 1) {
	        	edoSpeedLine.setRow(I);
	            qbeSpeed = uow.getQBEBioWithDefaults("wp_speedlinelocationswaveheadervotemp");
	            EXEDataObject.GetStringOutputParam qqGetStringOutputParamLoc = edoSpeedLine.getString(new TextData("Loc"), loc);
	            loc = qqGetStringOutputParamLoc.pResult;

	            EXEDataObject.GetStringOutputParam qqGetStringOutputParamLocDesc = edoSpeedLine.getString(new TextData("Description"), locDesc);
	            locDesc = qqGetStringOutputParamLocDesc.pResult;

	            
	            
	            if(loc != null && !"".equalsIgnoreCase(loc)){
					qbeSpeed.setValue("INTERACTIONID", interactionId);
					qbeSpeed.setValue("WAVEKEY", waveKey);
					qbeSpeed.setValue("LOC", loc);
					qbeSpeed.setValue("DESCRIPTION", locDesc);
					qbeSpeed.save();
	            }

	        }
			
	        uow.saveUOW(true);			
			
			
	        
	        
	        
			final Query eligibleSkusQuery = new Query("wp_eligibleconsolidatedskulistwaveheadervotemp",
					"wp_eligibleconsolidatedskulistwaveheadervotemp.INTERACTIONID = '" + interactionId
							+ "' and wp_eligibleconsolidatedskulistwaveheadervotemp.WAVEKEY = '" + waveKey + "'",
					null);
			rs = uow.getBioCollectionBean(eligibleSkusQuery);
			for (int i = 0; i < rs.size(); i++)
			{
				final BioBean row = rs.get("" + i);
				row.delete();
			}
			uow.saveUOW();

			//populate eligible sku temp table
			WaveInputObj wpInput = new WaveInputObj();
			wpInput.setWaveKey(waveKey);
			wpInput.setProcedureName(WPConstants.GETCONDOLIDATEDSKU);
			wpInput.getProcedureParametes().add(new TextData(waveKey));
			wpInput.getProcedureParametes().add(new TextData("WaveCall"));
			EXEDataObject edo = WPStoreProcedureCallWrapper.doAction(wpInput);
			String sku = "";
			String storerKey = "";
			String descr = "";
			String orderType = "";
			String consolLoc = "";
			double maxLocQty = 0.0;
			double waveTotal = 0.0;
			double minimunWaveQty = 0.0;
			String pickPiece = "";
			String pickCase = "";
			String assigned = "";
			QBEBioBean qbeSku = null;
	        final int qqLoopTest = edo.getRowCount();
	        for (int I = 1; I <= qqLoopTest; I = I + 1) {
	            edo.setRow(I);
	            EXEDataObject.GetStringOutputParam qqGetStringOutputParamSku = edo.getString(new TextData("Sku"), sku);
	            sku = qqGetStringOutputParamSku.pResult;
	            
	            EXEDataObject.GetStringOutputParam qqGetStringOutputParamStorerKey = edo.getString(new TextData("StorerKey"), storerKey);
	            storerKey = qqGetStringOutputParamStorerKey.pResult;
	            
	            EXEDataObject.GetStringOutputParam qqGetStringOutputParamDescr = edo.getString(new TextData("Descr"), descr);
	            descr = qqGetStringOutputParamDescr.pResult;

	            EXEDataObject.GetStringOutputParam qqGetStringOutputParamOrderType = edo.getString(new TextData("OrderType"), orderType);
	            orderType = qqGetStringOutputParamOrderType.pResult;
	            
	            EXEDataObject.GetStringOutputParam qqGetStringOutputParamConsolLoc = edo.getString(new TextData("ConsolLoc"), consolLoc);
	            consolLoc = qqGetStringOutputParamConsolLoc.pResult;
	            
	            EXEDataObject.GetDoubleOutputParam qqGetDoubleOutputMaxLocQty = edo.getDouble(new TextData("MaxLocQty"), maxLocQty);
	            maxLocQty = qqGetDoubleOutputMaxLocQty.pResult;
	            
	            EXEDataObject.GetDoubleOutputParam qqGetDoubleOutputWaveTotal = edo.getDouble(new TextData("WaveTotal"), waveTotal);
	            waveTotal = qqGetDoubleOutputWaveTotal.pResult;

	            EXEDataObject.GetDoubleOutputParam qqGetDoubleOutputMinimunWaveQty = edo.getDouble(new TextData("MinimunWaveQty"), minimunWaveQty);
	            minimunWaveQty = qqGetDoubleOutputMinimunWaveQty.pResult;

	            
	            EXEDataObject.GetStringOutputParam qqGetStringOutputParamPickPiece = edo.getString(new TextData("PickPiece"), pickPiece);
	            pickPiece = qqGetStringOutputParamPickPiece.pResult;
	            
	            EXEDataObject.GetStringOutputParam qqGetStringOutputParamPickCase = edo.getString(new TextData("PickCase"), pickCase);
	            pickCase = qqGetStringOutputParamPickCase.pResult;
	            
	            EXEDataObject.GetStringOutputParam qqGetStringOutputParamAssigned = edo.getString(new TextData("Assigned"), assigned);
	            assigned = qqGetStringOutputParamAssigned.pResult;
	            
	            //populate into temp table
				if((sku != null)&&(!(sku.trim().equalsIgnoreCase("")))){
					qbeSku = uow.getQBEBioWithDefaults("wp_eligibleconsolidatedskulistwaveheadervotemp");
					qbeSku.setValue("INTERACTIONID", interactionId);
					qbeSku.setValue("WAVEKEY", waveKey);
					qbeSku.setValue("INCLUDE", "1");
					qbeSku.setValue("STORERKEY", storerKey);
					qbeSku.setValue("SKU", sku);
					qbeSku.setValue("DESCR",descr);
					qbeSku.setValue("ORDERTYPE", orderType);
					qbeSku.setValue("CONSOLLOC", consolLoc);
					qbeSku.setValue("MAXLOCQTY", maxLocQty);
					qbeSku.setValue("WAVETOTAL", waveTotal);
					qbeSku.setValue("MINIMUMWAVEQTY", minimunWaveQty);
					qbeSku.setValue("PICKPIECE", pickPiece);
					qbeSku.setValue("PICKCASE", pickCase);
					qbeSku.setValue("ASSIGNED", assigned);
//					qbeSku.setValue("SERIALKEY", eligibleSku.getSerialKey());
					qbeSku.save();
				}            
	            
	        }
			
	        uow.saveUOW(true);
	        uow.clearState();
			
			
			
			
		}catch(WebuiException e){
			e.printStackTrace();
			throw new UserException(e.getMessage(), new Object[0]);
		}catch(EpiException ex){
			ex.printStackTrace();
			throw new UserException(ex.getMessage(), new Object[0]);
		}		
	}
	
	
	public static int validateUtil(WaveValidateInputObj input) throws UserException{
		final String BIO = "wm_wp_wavedetail";
		BioCollectionBean listCollection = null;
		int size = 0;
		String waveKey = input.getWaveKey();
		ActionContext context = input.getContext();
		String action = input.getAction();
		try {
			String sQueryString = "("+BIO+".WAVEKEY = '" + waveKey +"')";
			Query bioQuery = new Query(BIO, sQueryString, null);
			UnitOfWorkBean uow = context.getState().getDefaultUnitOfWork();
			listCollection = uow.getBioCollectionBean(bioQuery);
			size = listCollection.size();
			if (size == 0) {
				if(WPConstants.PREALLOCATE_WAVE.equalsIgnoreCase(action)){
					return WPConstants.NO_ORDER_TO_PREALLOCATE;
				}
				if(WPConstants.ALLOCATE_WAVE.equalsIgnoreCase(action)){
					return WPConstants.NO_ORDER_TO_ALLOCATE;
				}
				if(WPConstants.UNALLOCATE_WAVE.equalsIgnoreCase(action)){
					return WPConstants.NO_ORDER_TO_UNALLOCATE;
				}
				if(WPConstants.RELEASE_WAVE.equalsIgnoreCase(action)){
					return WPConstants.NO_ORDER_TO_RELEASE;
				}
				if(WPConstants.SHIP_WAVE.equalsIgnoreCase(action)){
					return WPConstants.NO_ORDER_TO_SHIP;
				}
			}
			return WPConstants.VALIDATION_PASSED;
		} catch (EpiException e) {
			// Handle Exceptions
			e.printStackTrace();
			_log.error("LOG_ERROR_WAVE_ACTION","There is exception from collectio.size():"+e.getMessage(),100L);
			throw new UserException("WPEXP_ACTION_SYS_ERROR", new Object[0]);
		}
				
	}
	
}
			
		

