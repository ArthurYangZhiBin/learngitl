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
package com.ssaglobal.scm.wms.wm_asnreceipts.ui;
import java.math.BigDecimal;
import java.text.NumberFormat;

import com.epiphany.shr.data.beans.BioServiceFactory;//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.UnitOfWork;//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.util.exceptions.EpiException;//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
import com.ssaglobal.scm.wms.wm_table_validation.ccf.NumericValidationCCF;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.scm.wms.util.LocaleUtil;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;







public class wm_asnreceipts_computedattr implements ComputedAttributeSupport
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(wm_asnreceipts_computedattr.class);
	public wm_asnreceipts_computedattr()
    {
    }

    public Object get(Bio bio, String attributeName, boolean isOldValue)
        throws EpiDataException
    {
        _log.debug("LOG_DEBUG_EXTENSION", "**Starting wm_asnreceipts_computedattr" , SuggestedCategory.NONE);//02/20/2009 AW Machine:2296026 SDIS:SCM-00000-06431

        //05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530 start
        	UnitOfWork uow; 
        	
    		try
    		{
    			uow = BioServiceFactory.getInstance().create("webui").getUnitOfWork();
    		} catch (EpiException e)
    		{
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    			return null;
    		}
        	//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530 end

    		NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); 		//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
    		String qty = ""; //02/20/2009 AW Machine:2296026 SDIS:SCM-00000-06431
    	//Total Qty Expected on the ASN Receipts tab
        if("TOTALQTYEXPECTED".equalsIgnoreCase(attributeName)){
        	BioCollection receiptdetailCollection = bio.getBioCollection("RECEIPTDETAILS");
        	if (receiptdetailCollection.size()!= 0){
        		_log.debug("LOG_SYSTEM_OUT","**********TOTALQTYEXPECTED*******Inside the IF",100L);
        		Object objqtyExp = receiptdetailCollection.sum("QTYEXPECTED");	//Sum of qty Expected
        		_log.debug("LOG_SYSTEM_OUT","**********TOTALQTYEXPECTED*******"+ objqtyExp.toString(),100L);
//        		return objqtyExp.toString();
        		return objqtyExp;
        	}else{
         	   try
        	   {
       				Number unformattedValue = nf.parse("0");
       				String formattedValue = nf.format(unformattedValue);
       				_log.debug("LOG_SYSTEM_OUT","\n\n!@# Formatted Value " + formattedValue,100L);
       				return formattedValue;
        	   }catch (Exception e)
        	   {
    			// Handle Exceptions 
        		   e.printStackTrace();
        	   }
        	}
       }
        //Total Qty Received on the ASN Receipts tab        
       if("TOTALQTYRECEIVED".equalsIgnoreCase(attributeName)){
    	   BioCollection receiptdetailCollection = bio.getBioCollection("RECEIPTDETAILS");
       	   if (receiptdetailCollection.size()!= 0){
       		   Object objqtyRec = receiptdetailCollection.sum("QTYRECEIVED");	//Sum of qty Received
       		   _log.debug("LOG_SYSTEM_OUT","**********TOTALQTYRECEIVED*******"+ objqtyRec.toString(),100L);
//       		   return objqtyRec.toString();
       		   return objqtyRec;
       	   }else{
         	   try
        	   {
       				Number unformattedValue = nf.parse("0");
       				String formattedValue = nf.format(unformattedValue);
       				_log.debug("LOG_SYSTEM_OUT","\n\n!@# Formatted Value " + formattedValue,100L);
       				return formattedValue;
        	   }catch (Exception e)
        	   {
    			// Handle Exceptions 
        		   e.printStackTrace();
        	   }
        	}
       }
       //Qty Received on the ASN Detail Receipts tab        
       if("EXPECTEDQTY".equalsIgnoreCase(attributeName)){
    	   String qtyExpected = bio.get("QTYEXPECTED").toString();
    	    _log.debug("LOG_SYSTEM_OUT","**********QTYExpected before conversion*******"+ qtyExpected,100L);
    	   String uom = bio.get("UOM").toString();
    	   String packkey = bio.get("PACKKEY").toString();
    	   Object objqtyExpConv = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA, uom,qtyExpected, packkey, UOMMappingUtil.stateNull, uow, false);//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
    	   qty= objqtyExpConv.toString(); //AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
//      	 AW SDIS: SCM-00000-06871 Machine:2353530 04/15/2009 start
    	   try
    	   {    		
    			Number unformattedValue = nf.parse(LocaleUtil.checkLocaleAndParseQty(qty, LocaleUtil.TYPE_QTY)); //AW Infor365:217417 03/22/10
   				_log.debug("LOG_DEBUG_EXTENSION", "!@#!@# Unformatted Value" +unformattedValue, SuggestedCategory.NONE);//02/20/2009 AW Machine:2296026 SDIS:SCM-00000-06431

   				String formattedValue = nf.format(unformattedValue);
   				_log.debug("LOG_SYSTEM_OUT","\n\n!@# Formatted Value " + formattedValue,100L);
   				return formattedValue;
    	   }catch (Exception e)
    	   {
			// Handle Exceptions 
    		   e.printStackTrace();
    	   }
       }

       //Qty Expected on the ASN Detail Receipts tab        
       if("RECEIVEDQTY".equalsIgnoreCase(attributeName)){
    	   String qtyReceived = bio.get("QTYRECEIVED").toString();
    	   _log.debug("LOG_SYSTEM_OUT","**********QTYReceived before conversion*******"+ qtyReceived,100L);
    	   String uom = bio.get("UOM").toString();
    	   String packkey = bio.get("PACKKEY").toString();
    	   Object objqtyRecConv = UOMMappingUtil.convertUOMQty(UOMMappingUtil.UOM_EA,uom,qtyReceived, packkey, UOMMappingUtil.stateNull, uow, false);//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
    	   qty= objqtyRecConv.toString();//AW 04/23/2009 SDIS:SCM-00000-06871 Machine:2353530
    	   try
    	   {
    		    Number unformattedValue = nf.parse(LocaleUtil.checkLocaleAndParseQty(qty, LocaleUtil.TYPE_QTY)); //AW Infor365:217417 03/22/10  				
    		    _log.debug("LOG_DEBUG_EXTENSION", "!@#!@# Unformatted Value" +unformattedValue, SuggestedCategory.NONE);//02/20/2009 AW Machine:2296026 SDIS:SCM-00000-06431
   				String formattedValue = nf.format(unformattedValue);
   				_log.debug("LOG_SYSTEM_OUT","\n\n!@# Formatted Value " + formattedValue,100L);
   				return formattedValue;
    	   }catch (Exception e)
    	   {
			// Handle Exceptions 
    		   e.printStackTrace();
    	   }
       }

       // Total Expected on the Summary View Tab
       if("TOTALUOMQTYEXP".equalsIgnoreCase(attributeName)){
    	   BioCollection receiptdetailCollection = bio.getBioCollection("VRECEIPTDETAILUOMCONV");
       	   if (receiptdetailCollection.size()!= 0){
       		   Object objqtyExp = receiptdetailCollection.sum("UOMQTYEXPECTED");
       		   _log.debug("LOG_SYSTEM_OUT","**********TOTALUOMQTYEXP*******"+ objqtyExp.toString(),100L);
//       		   return objqtyExp.toString();
       		   return objqtyExp;
       	   }else{
         	   try
        	   {
       				Number unformattedValue = nf.parse("0");
       				String formattedValue = nf.format(unformattedValue);
       				_log.debug("LOG_SYSTEM_OUT","\n\n!@# Formatted Value " + formattedValue,100L);
       				return formattedValue;
        	   }catch (Exception e)
        	   {
    			// Handle Exceptions 
        		   e.printStackTrace();
        	   }
        	}
       }
       // Total Received on the Summary View Tab       
       if("TOTALUOMQTYREC".equalsIgnoreCase(attributeName)){
    	   BioCollection receiptdetailCollection = bio.getBioCollection("VRECEIPTDETAILUOMCONV");
       	   if (receiptdetailCollection.size()!= 0){
       		   Object objqtyRec = receiptdetailCollection.sum("UOMQTYRECEIVED");
       		   _log.debug("LOG_SYSTEM_OUT","**********TOTALUOMQTYREC*******"+ objqtyRec.toString(),100L);
//       		   return objqtyRec.toString();
       		   return objqtyRec;
       	   }else{
         	   try
        	   {
       				Number unformattedValue = nf.parse("0");
       				String formattedValue = nf.format(unformattedValue);
       				_log.debug("LOG_SYSTEM_OUT","\n\n!@# Formatted Value " + formattedValue,100L);
       				return formattedValue;
        	   }catch (Exception e)
        	   {
    			// Handle Exceptions 
        		   e.printStackTrace();
        	   }
        	}
       }
       
       // Total Cube on the Summary View Tab       
       if("TOTALCUBE".equalsIgnoreCase(attributeName)){
    	   BioCollection receiptdetailCollection = bio.getBioCollection("RECEIPTDETAILS");
       	   if (receiptdetailCollection.size()!= 0){
       		   int size = receiptdetailCollection.size();
       		   String[] localAttributs = new String[2];
       		   localAttributs[0]="SKUBIO.STDCUBE";
       		   localAttributs[1]= "QTYRECEIVED";
       		   Object[] localCollection  = receiptdetailCollection.getAttributes(localAttributs);
       		   double totalCube = 0;
       		   for(int i=0; i < size; i++){
       			   Object [] tmp = (Object[])localCollection[i];
       			   BigDecimal stdCube = (BigDecimal) tmp[0];
       			   BigDecimal qtyReceived = (BigDecimal)tmp[1];
       			   totalCube = totalCube + (stdCube.doubleValue() * qtyReceived.doubleValue());
       		   }
//       		   return new Double(totalCube).toString();
       		return new Double(totalCube);
       	   }else{
         	   try
        	   {
       				Number unformattedValue = nf.parse("0");
       				String formattedValue = nf.format(unformattedValue);
       				_log.debug("LOG_SYSTEM_OUT","\n\n!@# Formatted Value " + formattedValue,100L);
       				return formattedValue;
        	   }catch (Exception e)
        	   {
    			// Handle Exceptions 
        		   e.printStackTrace();
        	   }
        	}
       }

       // Total Gross Wgt on the Summary View Tab       
       if("TOTALGROSSWGT".equalsIgnoreCase(attributeName)){
    	   BioCollection receiptdetailCollection = bio.getBioCollection("RECEIPTDETAILS");
       	   if (receiptdetailCollection.size()!= 0){
       		   int size = receiptdetailCollection.size();
       		   String[] localAttributs = new String[2];
       		   localAttributs[0]="SKUBIO.STDGROSSWGT";
       		   localAttributs[1]= "QTYRECEIVED";
       		   Object[] localCollection  = receiptdetailCollection.getAttributes(localAttributs);
       		   double totalGrosswgt = 0;
       		   for(int i=0; i < size; i++){
       			   Object [] tmp = (Object[])localCollection[i];
       			   BigDecimal stdGrosswgt = (BigDecimal) tmp[0];
       			   BigDecimal qtyReceived = (BigDecimal)tmp[1];
       			   totalGrosswgt = totalGrosswgt + (stdGrosswgt.doubleValue() * qtyReceived.doubleValue());
       		   }
//       		   return new Double(totalGrosswgt).toString();
       		return new Double(totalGrosswgt);
       	   }else{
         	   try
        	   {
       				Number unformattedValue = nf.parse("0");
       				String formattedValue = nf.format(unformattedValue);
       				_log.debug("LOG_SYSTEM_OUT","\n\n!@# Formatted Value " + formattedValue,100L);
       				return formattedValue;
        	   }catch (Exception e)
        	   {
    			// Handle Exceptions 
        		   e.printStackTrace();
        	   }
        	}
       }
       
       // Total Net wgt on the Summary View Tab       
       if("TOTALNETWEIGHT".equalsIgnoreCase(attributeName)){
    	   _log.debug("LOG_SYSTEM_OUT","******Inside TOTAL CUBE before biocollection***",100L);
       	   BioCollection receiptdetailCollection = bio.getBioCollection("RECEIPTDETAILS");
       	   if (receiptdetailCollection.size()!= 0){
       		   _log.debug("LOG_SYSTEM_OUT","******Inside TOTAL CUBE after***",100L);
       		   int size = receiptdetailCollection.size();
       		   String[] localAttributs = new String[2];
       		   localAttributs[0]="SKUBIO.STDNETWGT";
       		   localAttributs[1]= "QTYRECEIVED";
       		   Object[] localCollection  = receiptdetailCollection.getAttributes(localAttributs);
       		   double totalNetwgt = 0;
       		   for(int i=0; i < size; i++){
       			   Object [] tmp = (Object[])localCollection[i];
       			   BigDecimal stdNetwgt = (BigDecimal) tmp[0];
       			   BigDecimal qtyReceived = (BigDecimal)tmp[1];
       			   totalNetwgt = totalNetwgt + (stdNetwgt.doubleValue() * qtyReceived.doubleValue());
       		   }
//       		   return new Double(totalNetwgt).toString();
       		return new Double(totalNetwgt);
       	   }else{
         	   try
        	   {
       				Number unformattedValue = nf.parse("0");
       				String formattedValue = nf.format(unformattedValue);
       				_log.debug("LOG_SYSTEM_OUT","\n\n!@# Formatted Value " + formattedValue,100L);
       				return formattedValue;
        	   }catch (Exception e)
        	   {
    			// Handle Exceptions 
        		   e.printStackTrace();
        	   }
        	}
       } 
       // OVER on the Item Summary View       
       if("OVER".equalsIgnoreCase(attributeName)){
    	   double Over =  0;
    	   Object objqtyRec = bio.get("QtyReceived");
    	   Object objqtyExp = bio.get("QtyExpected");
    	   double QtyReceived  = ((BigDecimal)objqtyRec).doubleValue();
    	   double QtyExpected  = ((BigDecimal)objqtyExp).doubleValue();
    	   if (QtyReceived > QtyExpected){
    		   	Over = QtyReceived - QtyExpected;
    	   }
//    	   return String.valueOf(Over);
    	   return new Double(Over);
       }

       // SHORT on the Item Summary View       
       if("SHORT".equalsIgnoreCase(attributeName)){
    	   double Short = 0;	
    	   Object objqtyRec = bio.get("QtyReceived");
    	   Object objqtyExp = bio.get("QtyExpected");
    	   double QtyReceived  = ((BigDecimal)objqtyRec).doubleValue();
    	   double QtyExpected  = ((BigDecimal)objqtyExp).doubleValue();
    	   if (QtyExpected > QtyReceived){
   		   	Short = QtyExpected - QtyReceived ;
    	   }
//    	   return String.valueOf(Short);
    	   return new Double(Short);
       }
       
       
        /*
         * 博士伦二期开发新增差异字段的计算 
         * @Date:2017.8.17
         */
       if("DIFFERENTIATION".equalsIgnoreCase(attributeName)){
			 BioCollection receiptdetailCollection1 = bio.getBioCollection("RECEIPTDETAILS"); //连接到明细表
	        	if (receiptdetailCollection1.size()!= 0){
	        		_log.debug("LOG_SYSTEM_OUT","**********DIFFERENTIATION*******Inside the IF",100L);
	        	
	        		//得到总共的预期量
	        		Object objqtyExp = receiptdetailCollection1.sum("QTYEXPECTED");	//Sum of qty Expected
	        		//得到总共的接受量
	        		 Object objqtyRec = receiptdetailCollection1.sum("QTYRECEIVED");	//Sum of qty Received
	        		 //强转
	        		 BigDecimal objqtyExp_d=(BigDecimal)objqtyExp;
	        		 BigDecimal objqtyRec_d=(BigDecimal)objqtyRec;
	        		 BigDecimal resulet =objqtyExp_d.subtract(objqtyRec_d);
	        		 Object resulet_o=(Object)resulet;
	        		_log.debug("LOG_SYSTEM_OUT","**********DIFFERENTIATION*******"+ resulet_o.toString(),100L);
//	        		return objqtyExp.toString();
	        		return resulet_o;
	        	}else{
	          	   try
	        	   {
	       				Number unformattedValue = nf.parse("0");
	       				String formattedValue = nf.format(unformattedValue);
	       				_log.debug("LOG_SYSTEM_OUT","\n\n!@# Formatted Value " + formattedValue,100L);
	       				return formattedValue;
	        	   }catch (Exception e)
	        	   {
	    			// Handle Exceptions 
	        		   e.printStackTrace();
	        	   }
	        	}
       }
       
       return "";
    }

    public boolean supportsSet(String bioTypeName, String attributeName)
    {
        return true;
    }

    public void set(Bio bio, String attributeName, Object attributeValue, boolean flag)
        throws EpiDataException
    {
        //05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530 start
    	UnitOfWork uow= null; 
    	
		try
		{
			uow = BioServiceFactory.getInstance().create("webui").getUnitOfWork();
		} catch (EpiException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			_log.error("LOG_ERROR_EXTENSION_DropIDDetailSetItemValue", e.getStackTraceAsString(), SuggestedCategory.NONE);
		}
    	//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530 end

        //Qty Received on the ASN Detail List Receipts tab        
        if("EXPECTEDQTY".equalsIgnoreCase(attributeName) && flag == false){
        	final String recDetailType = bio.get("TYPE").toString();
			final String uom = bio.get("UOM").toString();
			final String packkey = bio.get("PACKKEY").toString();
        	String expectedQty = attributeValue == null ? "0" : attributeValue.toString();
			//convert to EA
//			if(expectedQty == null) {
//				throw new UserException("WMEXP_EXPQTY_NAN", new Object[]{});
//			}
			double dexpqty = NumericValidationCCF.parseNumber(expectedQty);

			if(! (recDetailType.equalsIgnoreCase("4") || recDetailType.equalsIgnoreCase("5"))){
				final String qtyExpected = UOMMappingUtil.convertUOMQty(uom, UOMMappingUtil.UOM_EA, new Double(dexpqty).toString(), packkey, UOMMappingUtil.stateNull, uow, false);//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
//				final String qtyExpected = UOMMappingUtil.convertUOMQty(uom, UOMMappingUtil.UOM_EA, expectedQty, packkey, UOMMappingUtil.stateNull, uow, false);//05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
				bio.set("QTYEXPECTED",new BigDecimal(qtyExpected));
			}
        }
        //Qty Expected on the ASN Detail List Receipts tab        
        else if("RECEIVEDQTY".equalsIgnoreCase(attributeName) && flag == false){
        	final String recDetailType = bio.get("TYPE").toString();
			final String uom = bio.get("UOM").toString();
			final String packkey = bio.get("PACKKEY").toString();
        	String receivedQty = attributeValue == null ? "0" : attributeValue.toString();
			double drecqty = NumericValidationCCF.parseNumber(receivedQty);
			if(! (recDetailType.equalsIgnoreCase("4") || recDetailType.equalsIgnoreCase("5"))){
				final String qtyReceived = UOMMappingUtil.convertUOMQty(uom, UOMMappingUtil.UOM_EA, new Double(drecqty).toString(), packkey.toString(),UOMMappingUtil.stateNull, uow, false); //05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
//				final String qtyReceived = UOMMappingUtil.convertUOMQty(uom, UOMMappingUtil.UOM_EA, receivedQty, packkey.toString(),UOMMappingUtil.stateNull, uow, false); //05/19/2009 AW SDIS:SCM-00000-06871 Machine:2353530
				bio.set("QTYRECEIVED",new BigDecimal(qtyReceived));

			}
        }
    }
 
}
