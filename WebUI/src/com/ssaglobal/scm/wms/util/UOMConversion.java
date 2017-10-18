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
package com.ssaglobal.scm.wms.util;


import com.agileitp.forte.framework.DecimalData;
import com.agileitp.forte.framework.DataValue;
import com.agileitp.forte.framework.DoubleData;
import com.agileitp.forte.framework.TextData;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;



public class UOMConversion {

	public static String uomConversion(TextData fromUOM, TextData toUOM, TextData fromQty, TextData packKey, StateInterface state, UnitOfWork uow, boolean useState) throws EpiDataException{
        /**************************************************************************************
         * Programmer:        Ameera Wassay
         * Created        :        03/06/98
         * Purpose        :        UOM Conversion
         **************************************************************************************
         * 05/19/2009	AW		SDIS:SCM-00000-06871 Machine:2353530
         * 						Initial version
		 *						This method does the uom conversion. The logic is the same as    
	     *						com.ssaglobal.scm.wms.service.baseobjects.InventoryHelper.uomConversion
         *************************************************************************************/

		Bio packBio = getPackBio(packKey, state, uow, useState);
		
		
        return convert(fromUOM, toUOM, fromQty, packKey, packBio);
    }

	private static String convert(TextData fromUOM, TextData toUOM,
			TextData fromQty, TextData packKey, Bio packBio) {
		DataValue tmpQty = null;

        DecimalData qqDecimalData = new DecimalData();

        qqDecimalData.setScale(5);

        DecimalData toqty = qqDecimalData;

        DecimalData qqDecimalData1 = new DecimalData();

        qqDecimalData1.setScale(5);

        DecimalData fromqty = qqDecimalData1;

        DecimalData qqDecimalData2 = new DecimalData();

        qqDecimalData2.setScale(5);

        DecimalData convertedQty = qqDecimalData2;

        double conversionFactor = 0;

        DoubleData aDec = new DoubleData(0);

        TextData fromuom = null;

        TextData touom = null;
        
        TextData toQty = null;

        tmpQty = toQty;
        
        
        
        String casecnt = "";
        
        String packuom1 = "";
        
        String packuom2 = "";
        
        String packuom3 = "";
        
        String packuom4 = "";
        
        String packuom5 = "";
        
        String packuom6 = "";
        
        String packuom7 = "";
        
        String packuom8 = "";
        
        String packuom9 = "";
        
        String innerpack = "";
        
		String pallet = "";
		
		String cube = "";
		
		String grosswgt ="";
        
        String netwgt = "";
        
        String otherunit1 = "";
        
        String otherunit2 = "";

        
        
        if (tmpQty != null) {
            toqty.setValue(tmpQty);
        }

        tmpQty = fromQty;

        if (tmpQty != null) {
            fromqty.setValue(tmpQty);
        }

        if (toUOM == null) {
            //fw
            //Start PMT# SLIM-5DMCXR
            toUOM = new TextData("6");
        }

        fromuom = fromUOM.getTextValue().toUpper().trimLeading().trimTrailing();
        touom = toUOM.getTextValue().toUpper().trimLeading().trimTrailing();
        toQty= fromQty;

        if (fromuom == null ||
            fromuom.isEqual(" ", true).getValue() ||
           (touom.getTextValue().isEqual(fromuom, true)).getValue() ||
             packKey == null ||
            (touom.getTextValue().isEqual(" ", true)).getValue() ||
            fromqty.getValue() == 0) {
 
            toQty =fromQty;

            return toQty.getAsString();
        }

        
		
		if(packBio == null) {
			return null;
		}
        
		try {
			
				
				casecnt = packBio.get("CASECNT").toString();
				
				
				
				pallet = packBio.get("PALLET").toString();
				cube = packBio.get("CUBE").toString();
				grosswgt = packBio.get("GROSSWGT").toString();
				netwgt = packBio.get("NETWGT").toString();
				otherunit1 = packBio.get("OTHERUNIT1").toString();
				otherunit2 = packBio.get("OTHERUNIT2").toString();
				innerpack = packBio.get("INNERPACK").toString();
				packuom1 = packBio.get("PACKUOM1").toString();
				packuom2 = packBio.get("PACKUOM2").toString();
				packuom3 = packBio.get("PACKUOM3").toString();
				packuom4 = packBio.get("PACKUOM4").toString();
				packuom5 = packBio.get("PACKUOM5").toString();
				packuom6 = packBio.get("PACKUOM6").toString();
				packuom7 = packBio.get("PACKUOM7").toString();
				packuom8 = packBio.get("PACKUOM8").toString();
				packuom9 = packBio.get("PACKUOM9").toString();
			
		} catch (EpiDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        /* If the fromuom is empty then we will just set it to the PackUOM3. */

        //IF theInboundDO.IsEmptyOrBlank('fromuom')
        //THEN
        //theInboundDO.SetAttribValue('touom', theOutDO.GetAttribValue('PackUOM3'));
        //return theContext;
        //END IF;
		
		if(fromuom.getAsString().equalsIgnoreCase(packuom1) ||
				((fromuom.getAsString() !=null) &&
				  fromuom.getAsString().equals("2")))
		{
			conversionFactor = Double.parseDouble(casecnt);
			convertedQty.setValue(fromqty.getValue() * conversionFactor);
			fromqty = convertedQty;
		}       
        else if ((fromuom.getAsString().equalsIgnoreCase(packuom2) ||
                ((fromuom.getValue() != null) &&
                fromuom.getValue().equals("3")))) {
            aDec.setValue(innerpack);
            convertedQty.setValue(fromqty.getValue() * aDec.getValue());
            fromqty = convertedQty;
        } else if ((fromuom.getAsString().equalsIgnoreCase(packuom3) ||
                ((fromuom.getValue() != null) &&
                fromuom.getValue().equals("6")) ||
                ((fromuom.getValue() != null) &&
                fromuom.getValue().equals("7")))) {
            convertedQty.setValue(fromqty.getValue());
            fromqty = convertedQty;
        } else if ((fromuom.getAsString().equalsIgnoreCase(packuom4) ||
                ((fromuom.getValue() != null) &&
                fromuom.getValue().equals("1")))) {
            aDec.setValue(pallet);
            convertedQty.setValue(fromqty.getValue() * aDec.getValue());
            fromqty = convertedQty;
        } else if ((fromuom.getAsString().equalsIgnoreCase(packuom5))) {
            aDec.setValue(cube);
            DecimalData qqDecimalData3 = new DecimalData();
            qqDecimalData3.setScale(5);
            DecimalData tmpholderCUBE = qqDecimalData3;
            tmpholderCUBE.setValue(fromqty.getValue() * aDec.getValue());         
            convertedQty.setValue(tmpholderCUBE);
            fromqty = convertedQty;
        } else if ((fromuom.getAsString().equalsIgnoreCase(packuom6))) {
            aDec.setValue(grosswgt);
            DecimalData qqDecimalData3 = new DecimalData();
            qqDecimalData3.setScale(5);
            DecimalData tmpholderGROSSWGT = qqDecimalData3;
            tmpholderGROSSWGT.setValue(fromqty.getValue() * aDec.getValue());           
            convertedQty.setValue(tmpholderGROSSWGT);
            fromqty = convertedQty;
        } else if ((fromuom.getAsString().equalsIgnoreCase(packuom7))) {
            aDec.setValue(netwgt);
            DecimalData qqDecimalData3 = new DecimalData();
            qqDecimalData3.setScale(5);
            DecimalData tmpholderNETWGT = qqDecimalData3;
            tmpholderNETWGT.setValue(fromqty.getValue() * aDec.getValue());
            convertedQty.setValue(tmpholderNETWGT);

            fromqty = convertedQty;
        } else if ((fromuom.getAsString().equalsIgnoreCase(packuom8) ||
                ((fromuom.getValue() != null) &&
                fromuom.getValue().equals("4")))) {
            aDec.setValue(otherunit1);
            DecimalData qqDecimalData3 = new DecimalData();
            qqDecimalData3.setScale(5);
            DecimalData tmpholderOTHERUNIT1 = qqDecimalData3;
            tmpholderOTHERUNIT1.setValue(fromqty.getValue() * aDec.getValue());
            convertedQty.setValue(tmpholderOTHERUNIT1);
            fromqty = convertedQty;
        } else if ((fromuom.getAsString().equalsIgnoreCase(packuom9) ||
                ((fromuom.getValue() != null) &&
                fromuom.getValue().equals("5")))) {
            aDec.setValue(otherunit2);
           DecimalData qqDecimalData3 = new DecimalData();
            qqDecimalData3.setScale(5);
            DecimalData tmpholderOTHERUNIT2 = qqDecimalData3;
            tmpholderOTHERUNIT2.setValue(fromqty.getValue() * aDec.getValue());
            convertedQty.setValue(tmpholderOTHERUNIT2);   	
            fromqty = convertedQty;
        } else {

        	//throw exception
            //    FObject.NX(this, "Invalid From-UOM for conversion (%1)"),
               
             
        }

        //IF theInboundDO.IsEmpty('touom')
        //OR (theInboundDO.GetAttribValue('touom').TextValue.IsEqual('0', TRUE))
        //THEN
        //theInboundDO.SetAttribValue('toqty', theInboundDO.GetAttribValue('fromqty'));
        //ELSEIF (toUOM.IsEqual(theOutDO.GetAttribValue('PackUOM1').TextValue) OR ToUOM = '2') THEN
        if ((touom.getAsString().equals(packuom1) ||
                ((touom.getValue() != null) && touom.getValue().equals("2")))) {
            conversionFactor = Double.parseDouble(casecnt);
            convertedQty.setValue(fromqty.getValue() / conversionFactor);           
            toqty = convertedQty;
        } else if ((touom.getAsString().equals(packuom2) ||
                ((touom.getValue() != null) && touom.getValue().equals("3")))) {
            conversionFactor = Double.parseDouble(innerpack);
            convertedQty.setValue(fromqty.getValue() / conversionFactor);
            toqty = convertedQty;
        } else if ((touom.getAsString().equals(packuom3) ||
                ((touom.getValue() != null) && touom.getValue().equals("6")) ||
                ((touom.getValue() != null) && touom.getValue().equals("7")))) {
            toqty = fromqty;                
        } else if ((touom.getAsString().equals(packuom4) ||
                ((touom.getValue() != null) && touom.getValue().equals("1")))) {
            conversionFactor = Double.parseDouble(pallet);
            convertedQty.setValue(fromqty.getValue() / conversionFactor);
            toqty = convertedQty;
        } else if (touom.getAsString().equals(packuom5)) {
            conversionFactor = Double.parseDouble(cube);
            convertedQty.setValue(fromqty.getValue() / conversionFactor);
            toqty = convertedQty;
        } else if (touom.getAsString().equalsIgnoreCase(packuom6)) {
            conversionFactor = Double.parseDouble(grosswgt);
            convertedQty.setValue(fromqty.getValue() / conversionFactor);
            toqty = convertedQty;
        } else if (touom.getAsString().equalsIgnoreCase(packuom7)) {
            conversionFactor = Double.parseDouble(netwgt);
            convertedQty.setValue(fromqty.getValue() / conversionFactor);
            toqty = convertedQty;
        } else if ((touom.getAsString().equalsIgnoreCase(packuom8) ||
                ((touom.getValue() != null) && touom.getValue().equals("4")))) {
            conversionFactor = Double.parseDouble(otherunit1);
            convertedQty.setValue(fromqty.getValue() / conversionFactor);
            toqty = convertedQty;
        } else if ((touom.getAsString().equalsIgnoreCase(packuom9) ||
                ((touom.getValue() != null) && touom.getValue().equals("5")))) {
            conversionFactor = Double.parseDouble(otherunit2);
            convertedQty.setValue(fromqty.getValue() / conversionFactor);
            toqty = convertedQty;
        } else {
        	//throw exception
             //   FObject.NX(this, "Invalid To-UOM for conversion (%1)"),
               
        }

        return convertedQty.getAsString();
	}

	private static Bio getPackBio(TextData packKey, StateInterface state,
			UnitOfWork uow, boolean useState) throws EpiDataException {
		Query qry = null;
        UnitOfWorkBean uowb = null;		
        BioCollection bioColl = null;
        Bio packBio = null;

        qry = new Query(UOMMappingUtil.packBio, UOMMappingUtil.packBio +"." +UOMMappingUtil.pack +" = '" + packKey.getAsString().toUpperCase()+"'", "");
		if (useState) {
			uowb = state.getDefaultUnitOfWork();
			bioColl = uowb.getBioCollectionBean(qry);
		} else {
			bioColl = uow.findByQuery(qry);
		}

		if (bioColl.size() < 0) {
			return null;
		} else {
			packBio = bioColl.elementAt(0);
		}
		return packBio;
	}

	public static String uomConversion(TextData fromUOM, TextData toUOM,
			TextData fromQty, TextData packKey, Bio packBio) {
		return convert(fromUOM, toUOM, fromQty, packKey, packBio);
	}

	
	
	
	
	
}
