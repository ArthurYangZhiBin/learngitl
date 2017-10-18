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
package com.ssaglobal.scm.wms.wm_pack.ui;

import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.QBEBioBean;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.duplicate.IDuplicate;
import com.ssaglobal.scm.wms.util.duplicate.PopulateBase;
import com.ssaglobal.scm.wms.util.duplicate.PopulateBioType;
import com.ssaglobal.scm.wms.wm_owner.ListDeleteOwner;

public class PopulatePack extends PopulateBase{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(PopulatePack.class);

	public PopulatePack(){
		
	}
	
	public PopulateBioType ValidateAndLoad(DataBean dupDataBean, QBEBioBean newBioBean) {
		// TODO Auto-generated method stub
		PopulateBioType populateBioType = new PopulateBioType();
		 
		populateBioType.setStatus(true);
		populateBioType.setqbeBioBean(newBioBean);
		
		_log.debug("LOG_SYSTEM_OUT","jp Inside PopulatePack.ValidateAndLoad",100L);
		
		if (dupDataBean.getValue("CARTONIZEUOM1")!=null && ((String) dupDataBean.getValue("CARTONIZEUOM1")).trim().length()>0)
			populateBioType.getqbeBioBean().set("CARTONIZEUOM1",dupDataBean.getValue("CARTONIZEUOM1"));
		
		if (dupDataBean.getValue("CARTONIZEUOM2")!=null && ((String) dupDataBean.getValue("CARTONIZEUOM2")).trim().length()>0)
			populateBioType.getqbeBioBean().set("CARTONIZEUOM2",dupDataBean.getValue("CARTONIZEUOM2"));
		
		if (dupDataBean.getValue("CARTONIZEUOM3")!=null && ((String) dupDataBean.getValue("CARTONIZEUOM3")).trim().length()>0)
			populateBioType.getqbeBioBean().set("CARTONIZEUOM3",dupDataBean.getValue("CARTONIZEUOM3"));
		
		if (dupDataBean.getValue("CARTONIZEUOM4")!=null && ((String) dupDataBean.getValue("CARTONIZEUOM4")).trim().length()>0)
			populateBioType.getqbeBioBean().set("CARTONIZEUOM4",dupDataBean.getValue("CARTONIZEUOM4"));
		
		if (dupDataBean.getValue("CARTONIZEUOM8")!=null && ((String) dupDataBean.getValue("CARTONIZEUOM8")).trim().length()>0)
			populateBioType.getqbeBioBean().set("CARTONIZEUOM8",dupDataBean.getValue("CARTONIZEUOM8"));
		
		if (dupDataBean.getValue("CARTONIZEUOM9")!=null && ((String) dupDataBean.getValue("CARTONIZEUOM9")).trim().length()>0)
			populateBioType.getqbeBioBean().set("CARTONIZEUOM9",dupDataBean.getValue("CARTONIZEUOM9"));
		
		if (dupDataBean.getValue("CASECNT")!=null )
			populateBioType.getqbeBioBean().set("CASECNT",dupDataBean.getValue("CASECNT"));
		
		if (dupDataBean.getValue("CUBE")!=null )
			populateBioType.getqbeBioBean().set("CUBE",dupDataBean.getValue("CUBE"));
		
		if (dupDataBean.getValue("CUBEUOM1")!=null )
			populateBioType.getqbeBioBean().set("CUBEUOM1",dupDataBean.getValue("CUBEUOM1"));
		
		if (dupDataBean.getValue("CUBEUOM2")!=null )
			populateBioType.getqbeBioBean().set("CUBEUOM2",dupDataBean.getValue("CUBEUOM2"));
		
		if (dupDataBean.getValue("CUBEUOM3")!=null )
			populateBioType.getqbeBioBean().set("CUBEUOM3",dupDataBean.getValue("CUBEUOM3"));
		
		if (dupDataBean.getValue("CUBEUOM4")!=null )
			populateBioType.getqbeBioBean().set("CUBEUOM4",dupDataBean.getValue("CUBEUOM4"));
		
		if (dupDataBean.getValue("FILTERVALUEUOM1")!=null )
			populateBioType.getqbeBioBean().set("FILTERVALUEUOM1",dupDataBean.getValue("FILTERVALUEUOM1"));
		
		if (dupDataBean.getValue("FILTERVALUEUOM2")!=null )
			populateBioType.getqbeBioBean().set("FILTERVALUEUOM2",dupDataBean.getValue("FILTERVALUEUOM2"));
		
		if (dupDataBean.getValue("FILTERVALUEUOM3")!=null )
			populateBioType.getqbeBioBean().set("FILTERVALUEUOM3",dupDataBean.getValue("FILTERVALUEUOM3"));
		
		if (dupDataBean.getValue("FILTERVALUEUOM4")!=null )
			populateBioType.getqbeBioBean().set("FILTERVALUEUOM4",dupDataBean.getValue("FILTERVALUEUOM4"));
		
		if (dupDataBean.getValue("FILTERVALUEUOM5")!=null )
			populateBioType.getqbeBioBean().set("FILTERVALUEUOM5",dupDataBean.getValue("FILTERVALUEUOM5"));
		
		if (dupDataBean.getValue("FILTERVALUEUOM6")!=null )
			populateBioType.getqbeBioBean().set("FILTERVALUEUOM6",dupDataBean.getValue("FILTERVALUEUOM6"));
		
		if (dupDataBean.getValue("FILTERVALUEUOM7")!=null )
			populateBioType.getqbeBioBean().set("FILTERVALUEUOM7",dupDataBean.getValue("FILTERVALUEUOM7"));
		
		if (dupDataBean.getValue("FILTERVALUEUOM8")!=null )
			populateBioType.getqbeBioBean().set("FILTERVALUEUOM8",dupDataBean.getValue("FILTERVALUEUOM8"));
		
		if (dupDataBean.getValue("FILTERVALUEUOM9")!=null )
			populateBioType.getqbeBioBean().set("FILTERVALUEUOM9",dupDataBean.getValue("FILTERVALUEUOM9"));
		
		if (dupDataBean.getValue("GROSSWGT")!=null )
			populateBioType.getqbeBioBean().set("GROSSWGT",dupDataBean.getValue("GROSSWGT"));
		
		if (dupDataBean.getValue("HEIGHTUOM1")!=null )
			populateBioType.getqbeBioBean().set("HEIGHTUOM1",dupDataBean.getValue("HEIGHTUOM1"));
		
		if (dupDataBean.getValue("HEIGHTUOM2")!=null )
			populateBioType.getqbeBioBean().set("HEIGHTUOM2",dupDataBean.getValue("HEIGHTUOM2"));
		
		if (dupDataBean.getValue("HEIGHTUOM3")!=null )
			populateBioType.getqbeBioBean().set("HEIGHTUOM3",dupDataBean.getValue("HEIGHTUOM3"));
		
		if (dupDataBean.getValue("HEIGHTUOM4")!=null )
			populateBioType.getqbeBioBean().set("HEIGHTUOM4",dupDataBean.getValue("HEIGHTUOM4"));
		
		if (dupDataBean.getValue("HEIGHTUOM8")!=null )
			populateBioType.getqbeBioBean().set("HEIGHTUOM8",dupDataBean.getValue("HEIGHTUOM8"));
		
		if (dupDataBean.getValue("HEIGHTUOM9")!=null )
			populateBioType.getqbeBioBean().set("HEIGHTUOM9",dupDataBean.getValue("HEIGHTUOM9"));
		
		if (dupDataBean.getValue("IndicatorDigitUOM1")!=null )
			populateBioType.getqbeBioBean().set("IndicatorDigitUOM1",dupDataBean.getValue("IndicatorDigitUOM1"));
		
		if (dupDataBean.getValue("IndicatorDigitUOM1")!=null )
			populateBioType.getqbeBioBean().set("IndicatorDigitUOM2",dupDataBean.getValue("IndicatorDigitUOM2"));
		
		if (dupDataBean.getValue("IndicatorDigitUOM1")!=null )
			populateBioType.getqbeBioBean().set("IndicatorDigitUOM3",dupDataBean.getValue("IndicatorDigitUOM3"));
		
		if (dupDataBean.getValue("IndicatorDigitUOM1")!=null )
			populateBioType.getqbeBioBean().set("IndicatorDigitUOM4",dupDataBean.getValue("IndicatorDigitUOM4"));
		
		if (dupDataBean.getValue("IndicatorDigitUOM1")!=null )
			populateBioType.getqbeBioBean().set("IndicatorDigitUOM5",dupDataBean.getValue("IndicatorDigitUOM5"));
		
		if (dupDataBean.getValue("IndicatorDigitUOM1")!=null )
			populateBioType.getqbeBioBean().set("IndicatorDigitUOM6",dupDataBean.getValue("IndicatorDigitUOM6"));
		
		if (dupDataBean.getValue("IndicatorDigitUOM1")!=null )
			populateBioType.getqbeBioBean().set("IndicatorDigitUOM7",dupDataBean.getValue("IndicatorDigitUOM7"));
		
		if (dupDataBean.getValue("IndicatorDigitUOM1")!=null )
			populateBioType.getqbeBioBean().set("IndicatorDigitUOM8",dupDataBean.getValue("IndicatorDigitUOM8"));
		
		if (dupDataBean.getValue("IndicatorDigitUOM1")!=null )
			populateBioType.getqbeBioBean().set("IndicatorDigitUOM9",dupDataBean.getValue("IndicatorDigitUOM9"));
		
		if (dupDataBean.getValue("INNERPACK")!=null )
			populateBioType.getqbeBioBean().set("INNERPACK",dupDataBean.getValue("INNERPACK"));
		
		
		if (dupDataBean.getValue("ISWHQTY1")!=null && ((String) dupDataBean.getValue("ISWHQTY1")).trim().length()>0)
			populateBioType.getqbeBioBean().set("ISWHQTY1",dupDataBean.getValue("ISWHQTY1"));
		
		if (dupDataBean.getValue("ISWHQTY2")!=null && ((String) dupDataBean.getValue("ISWHQTY2")).trim().length()>0)
		populateBioType.getqbeBioBean().set("ISWHQTY2",dupDataBean.getValue("ISWHQTY2"));
		
		if (dupDataBean.getValue("ISWHQTY3")!=null && ((String) dupDataBean.getValue("ISWHQTY3")).trim().length()>0)
			populateBioType.getqbeBioBean().set("ISWHQTY3",dupDataBean.getValue("ISWHQTY3"));
		
		if (dupDataBean.getValue("ISWHQTY4")!=null && ((String) dupDataBean.getValue("ISWHQTY4")).trim().length()>0)
			populateBioType.getqbeBioBean().set("ISWHQTY4",dupDataBean.getValue("ISWHQTY4"));
		
		if (dupDataBean.getValue("ISWHQTY5")!=null && ((String) dupDataBean.getValue("ISWHQTY5")).trim().length()>0)
			populateBioType.getqbeBioBean().set("ISWHQTY5",dupDataBean.getValue("ISWHQTY5"));
		
		if (dupDataBean.getValue("ISWHQTY6")!=null && ((String) dupDataBean.getValue("ISWHQTY6")).trim().length()>0)
			populateBioType.getqbeBioBean().set("ISWHQTY6",dupDataBean.getValue("ISWHQTY6"));
		
		if (dupDataBean.getValue("ISWHQTY7")!=null && ((String) dupDataBean.getValue("ISWHQTY7")).trim().length()>0)
			populateBioType.getqbeBioBean().set("ISWHQTY7",dupDataBean.getValue("ISWHQTY7"));
		
		if (dupDataBean.getValue("ISWHQTY8")!=null && ((String) dupDataBean.getValue("ISWHQTY8")).trim().length()>0)
			populateBioType.getqbeBioBean().set("ISWHQTY8",dupDataBean.getValue("ISWHQTY8"));
		
		if (dupDataBean.getValue("ISWHQTY9")!=null && ((String) dupDataBean.getValue("ISWHQTY9")).trim().length()>0)
			populateBioType.getqbeBioBean().set("ISWHQTY9",dupDataBean.getValue("ISWHQTY9"));
		
		if (dupDataBean.getValue("LENGTHUOM1")!=null )
			populateBioType.getqbeBioBean().set("LENGTHUOM1",dupDataBean.getValue("LENGTHUOM1"));
		
		if (dupDataBean.getValue("LENGTHUOM2")!=null )
			populateBioType.getqbeBioBean().set("LENGTHUOM2",dupDataBean.getValue("LENGTHUOM2"));
		
		if (dupDataBean.getValue("LENGTHUOM3")!=null )
			populateBioType.getqbeBioBean().set("LENGTHUOM3",dupDataBean.getValue("LENGTHUOM3"));
		
		if (dupDataBean.getValue("LENGTHUOM4")!=null )
			populateBioType.getqbeBioBean().set("LENGTHUOM4",dupDataBean.getValue("LENGTHUOM4"));
		
		if (dupDataBean.getValue("LENGTHUOM8")!=null )
			populateBioType.getqbeBioBean().set("LENGTHUOM8",dupDataBean.getValue("LENGTHUOM8"));
		
		if (dupDataBean.getValue("LENGTHUOM9")!=null )
			populateBioType.getqbeBioBean().set("LENGTHUOM9",dupDataBean.getValue("LENGTHUOM9"));
		
		if (dupDataBean.getValue("NETWGT")!=null )
			populateBioType.getqbeBioBean().set("NETWGT",dupDataBean.getValue("NETWGT"));
		
		if (dupDataBean.getValue("OTHERUNIT1")!=null )
			populateBioType.getqbeBioBean().set("OTHERUNIT1",dupDataBean.getValue("OTHERUNIT1"));
		
		if (dupDataBean.getValue("OTHERUNIT2")!=null )
			populateBioType.getqbeBioBean().set("OTHERUNIT2",dupDataBean.getValue("OTHERUNIT2"));

		
		populateBioType.getqbeBioBean().set("PACKDESCR",null);
		populateBioType.getqbeBioBean().set("PACKKEY",null);
		
		if (dupDataBean.getValue("PACKUOM1")!=null && ((String) dupDataBean.getValue("PACKUOM1")).trim().length()>0)
			populateBioType.getqbeBioBean().set("PACKUOM1",dupDataBean.getValue("PACKUOM1"));
		
		if (dupDataBean.getValue("PACKUOM2")!=null && ((String) dupDataBean.getValue("PACKUOM2")).trim().length()>0)
			populateBioType.getqbeBioBean().set("PACKUOM2",dupDataBean.getValue("PACKUOM2"));
		
		if (dupDataBean.getValue("PACKUOM3")!=null && ((String) dupDataBean.getValue("PACKUOM3")).trim().length()>0)
			populateBioType.getqbeBioBean().set("PACKUOM3",dupDataBean.getValue("PACKUOM3"));
		
		if (dupDataBean.getValue("PACKUOM4")!=null && ((String) dupDataBean.getValue("PACKUOM4")).trim().length()>0)
			populateBioType.getqbeBioBean().set("PACKUOM4",dupDataBean.getValue("PACKUOM4"));	
		
		if (dupDataBean.getValue("PACKUOM5")!=null && ((String) dupDataBean.getValue("PACKUOM5")).trim().length()>0)
			populateBioType.getqbeBioBean().set("PACKUOM5",dupDataBean.getValue("PACKUOM5"));
		
		if (dupDataBean.getValue("PACKUOM6")!=null && ((String) dupDataBean.getValue("PACKUOM6")).trim().length()>0)
			populateBioType.getqbeBioBean().set("PACKUOM6",dupDataBean.getValue("PACKUOM6"));
		
		if (dupDataBean.getValue("PACKUOM7")!=null && ((String) dupDataBean.getValue("PACKUOM7")).trim().length()>0)
			populateBioType.getqbeBioBean().set("PACKUOM7",dupDataBean.getValue("PACKUOM7"));
		
		if (dupDataBean.getValue("PACKUOM8")!=null && ((String) dupDataBean.getValue("PACKUOM8")).trim().length()>0)
			populateBioType.getqbeBioBean().set("PACKUOM8",dupDataBean.getValue("PACKUOM8"));
		
		if (dupDataBean.getValue("PACKUOM9")!=null && ((String) dupDataBean.getValue("PACKUOM9")).trim().length()>0)
			populateBioType.getqbeBioBean().set("PACKUOM9",dupDataBean.getValue("PACKUOM9"));
		
		if (dupDataBean.getValue("PALLET")!=null )
			populateBioType.getqbeBioBean().set("PALLET",dupDataBean.getValue("PALLET"));
		
		if (dupDataBean.getValue("PALLETHI")!=null )
			populateBioType.getqbeBioBean().set("PALLETHI",dupDataBean.getValue("PALLETHI"));
		
		if (dupDataBean.getValue("PALLETTI")!=null )
			populateBioType.getqbeBioBean().set("PALLETTI",dupDataBean.getValue("PALLETTI"));
		
		if (dupDataBean.getValue("PALLETWOODHEIGHT")!=null )
			populateBioType.getqbeBioBean().set("PALLETWOODHEIGHT",dupDataBean.getValue("PALLETWOODHEIGHT"));
		
		if (dupDataBean.getValue("PALLETWOODLENGTH")!=null )
			populateBioType.getqbeBioBean().set("PALLETWOODLENGTH",dupDataBean.getValue("PALLETWOODLENGTH"));
		
		if (dupDataBean.getValue("PALLETWOODWIDTH")!=null )
			populateBioType.getqbeBioBean().set("PALLETWOODWIDTH",dupDataBean.getValue("PALLETWOODWIDTH"));
		
		if (dupDataBean.getValue("QTY")!=null )
			populateBioType.getqbeBioBean().set("QTY",dupDataBean.getValue("QTY"));	
		
		if (dupDataBean.getValue("REPLENISHUOM1")!=null && ((String) dupDataBean.getValue("REPLENISHUOM1")).trim().length()>0)
			populateBioType.getqbeBioBean().set("REPLENISHUOM1",dupDataBean.getValue("REPLENISHUOM1"));
		
		if (dupDataBean.getValue("REPLENISHUOM2")!=null && ((String) dupDataBean.getValue("REPLENISHUOM2")).trim().length()>0)
			populateBioType.getqbeBioBean().set("REPLENISHUOM2",dupDataBean.getValue("REPLENISHUOM2"));
			
		if (dupDataBean.getValue("REPLENISHUOM3")!=null && ((String) dupDataBean.getValue("REPLENISHUOM3")).trim().length()>0)
			populateBioType.getqbeBioBean().set("REPLENISHUOM3",dupDataBean.getValue("REPLENISHUOM3"));
		
		if (dupDataBean.getValue("REPLENISHUOM4")!=null && ((String) dupDataBean.getValue("REPLENISHUOM4")).trim().length()>0)
		populateBioType.getqbeBioBean().set("REPLENISHUOM4",dupDataBean.getValue("REPLENISHUOM4"));
		
		if (dupDataBean.getValue("REPLENISHUOM8")!=null && ((String) dupDataBean.getValue("REPLENISHUOM8")).trim().length()>0)
			populateBioType.getqbeBioBean().set("REPLENISHUOM8",dupDataBean.getValue("REPLENISHUOM8"));
		
		if (dupDataBean.getValue("REPLENISHUOM9")!=null && ((String) dupDataBean.getValue("REPLENISHUOM9")).trim().length()>0)
			populateBioType.getqbeBioBean().set("REPLENISHUOM9",dupDataBean.getValue("REPLENISHUOM9"));
		
		if (dupDataBean.getValue("REPLENISHZONE1")!=null && ((String) dupDataBean.getValue("REPLENISHZONE1")).trim().length()>0)
			populateBioType.getqbeBioBean().set("REPLENISHZONE1",dupDataBean.getValue("REPLENISHZONE1"));
		
		if (dupDataBean.getValue("REPLENISHZONE2")!=null && ((String) dupDataBean.getValue("REPLENISHZONE2")).trim().length()>0)
			populateBioType.getqbeBioBean().set("REPLENISHZONE2",dupDataBean.getValue("REPLENISHZONE2"));
		
		if (dupDataBean.getValue("REPLENISHZONE3")!=null && ((String) dupDataBean.getValue("REPLENISHZONE3")).trim().length()>0)
			populateBioType.getqbeBioBean().set("REPLENISHZONE3",dupDataBean.getValue("REPLENISHZONE3"));
		
		if (dupDataBean.getValue("REPLENISHZONE4")!=null && ((String) dupDataBean.getValue("REPLENISHZONE4")).trim().length()>0)
			populateBioType.getqbeBioBean().set("REPLENISHZONE4",dupDataBean.getValue("REPLENISHZONE4"));
		
		if (dupDataBean.getValue("REPLENISHZONE8")!=null && ((String) dupDataBean.getValue("REPLENISHZONE8")).trim().length()>0)
			populateBioType.getqbeBioBean().set("REPLENISHZONE8",dupDataBean.getValue("REPLENISHZONE8"));
		
		if (dupDataBean.getValue("REPLENISHZONE9")!=null && ((String) dupDataBean.getValue("REPLENISHZONE9")).trim().length()>0)
			populateBioType.getqbeBioBean().set("REPLENISHZONE9",dupDataBean.getValue("REPLENISHZONE9"));

		//10/14/2010 FW:  Commented out copying WHSEID for wrong WHSEID issue (Incedent4000422_Defect284242) -- Start
		/*
		if (dupDataBean.getValue("WHSEID")!=null && ((String) dupDataBean.getValue("WHSEID")).trim().length()>0)
			populateBioType.getqbeBioBean().set("WHSEID",dupDataBean.getValue("WHSEID"));
		*/
		//10/14/2010 FW:  Commented out copying WHSEID for wrong WHSEID issue (Incedent4000422_Defect284242) -- End
		
		if (dupDataBean.getValue("WIDTHUOM1")!=null )
			populateBioType.getqbeBioBean().set("WIDTHUOM1",dupDataBean.getValue("WIDTHUOM1"));
		
		if (dupDataBean.getValue("WIDTHUOM2")!=null )
			populateBioType.getqbeBioBean().set("WIDTHUOM2",dupDataBean.getValue("WIDTHUOM2"));
		
		if (dupDataBean.getValue("WIDTHUOM3")!=null )
			populateBioType.getqbeBioBean().set("WIDTHUOM3",dupDataBean.getValue("WIDTHUOM3"));
		
		if (dupDataBean.getValue("WIDTHUOM4")!=null )
			populateBioType.getqbeBioBean().set("WIDTHUOM4",dupDataBean.getValue("WIDTHUOM4"));
		
		if (dupDataBean.getValue("WIDTHUOM8")!=null )
			populateBioType.getqbeBioBean().set("WIDTHUOM8",dupDataBean.getValue("WIDTHUOM8"));
		
		if (dupDataBean.getValue("WIDTHUOM9")!=null )
			populateBioType.getqbeBioBean().set("WIDTHUOM9",dupDataBean.getValue("WIDTHUOM9"));
		
		
		
		return populateBioType; 
	}

}
