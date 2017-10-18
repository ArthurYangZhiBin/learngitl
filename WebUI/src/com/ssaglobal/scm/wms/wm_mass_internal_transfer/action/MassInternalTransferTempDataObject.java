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
package com.ssaglobal.scm.wms.wm_mass_internal_transfer.action;

import java.util.Calendar;

public class MassInternalTransferTempDataObject
{
	private String fromstorerkey;

	private String fromsku;

	private String tostorerkey;

	private String tosku;
	
	private String frompackkey;

	private String topackkey;

	private Object id;

	private String lot;

	private String loc;

	private Object qty;

	private String lottable01;

	private String lottable02;

	private String lottable03;

	private Calendar lottable04; //date

	private Calendar lottable05; //date

	private String lottable06;

	private String lottable07;

	private String lottable08;

	private String lottable09;

	private String lottable10;
	
	private Calendar lottable11; //date

	private Calendar lottable12; //date

	public String getFromstorerkey()
	{
		return fromstorerkey;
	}

	public void setFromstorerkey(String fromstorerkey)
	{
		this.fromstorerkey = fromstorerkey;
	}

	public String getFromsku()
	{
		return fromsku;
	}

	public void setFromsku(String fromsku)
	{
		this.fromsku = fromsku;
	}

	public String getTostorerkey()
	{
		return tostorerkey;
	}

	public void setTostorerkey(String tostorerkey)
	{
		this.tostorerkey = tostorerkey;
	}

	public String getTosku()
	{
		return tosku;
	}

	public void setTosku(String tosku)
	{
		this.tosku = tosku;
	}

	public String getFrompackkey()
	{
		return frompackkey;
	}

	public void setFrompackkey(String frompackkey)
	{
		this.frompackkey = frompackkey;
	}

	public String getTopackkey()
	{
		return topackkey;
	}

	public void setTopackkey(String topackkey)
	{
		this.topackkey = topackkey;
	}

	public Object getId()
	{
		return id;
	}

	public void setId(Object id)
	{
		this.id = id;
	}

	public String getLot()
	{
		return lot;
	}

	public void setLot(String lot)
	{
		this.lot = lot;
	}

	public String getLoc()
	{
		return loc;
	}

	public void setLoc(String loc)
	{
		this.loc = loc;
	}

	public Object getQty()
	{
		return qty;
	}

	public void setQty(Object qty)
	{
		this.qty = qty;
	}

	public String getLottable01()
	{
		return lottable01;
	}

	public void setLottable01(String lottable01)
	{
		this.lottable01 = notNullCheck(lottable01);
	}

	public String getLottable02()
	{
		return lottable02;
	}

	public void setLottable02(String lottable02)
	{
		this.lottable02 = notNullCheck(lottable02);
	}

	public String getLottable03()
	{
		return lottable03;
	}

	public void setLottable03(String lottable03)
	{
		this.lottable03 = notNullCheck(lottable03);
	}

	public Calendar getLottable04()
	{
		return lottable04;
	}

	public void setLottable04(Calendar lottable04)
	{
		this.lottable04 = lottable04;
	}

	public Calendar getLottable05()
	{
		return lottable05;
	}

	public void setLottable05(Calendar lottable05)
	{
		this.lottable05 = lottable05;
	}

	public String getLottable06()
	{
		return lottable06;
	}

	public void setLottable06(String lottable06)
	{
		this.lottable06 = notNullCheck(lottable06);
	}

	public String getLottable07()
	{
		return lottable07;
	}

	public void setLottable07(String lottable07)
	{
		this.lottable07 = notNullCheck(lottable07);
	}

	public String getLottable08()
	{
		return lottable08;
	}

	public void setLottable08(String lottable08)
	{
		this.lottable08 = notNullCheck(lottable08);
	}

	public String getLottable09()
	{
		return lottable09;
	}

	public void setLottable09(String lottable09)
	{
		this.lottable09 = notNullCheck(lottable09);
	}

	public String getLottable10()
	{
		return lottable10;
	}

	public void setLottable10(String lottable10)
	{
		this.lottable10 = notNullCheck(lottable10);
	}

	public Calendar getLottable11()
	{
		return lottable11;
	}

	public void setLottable11(Calendar lottable11)
	{
		this.lottable11 = lottable11;
	}

	public Calendar getLottable12()
	{
		return lottable12;
	}

	public void setLottable12(Calendar lottable12)
	{
		this.lottable12 = lottable12;
	}
	
	private String notNullCheck(String val)
	{
		if (val == null)
		{
			return " ";
		}
		return val;
	}
	
	//02/21/2011 FW  Added code to insert masterUnit of pack instead of 'EA' into transferdetail table (Incident4288675_Defect300004) -- Start 
	private String uom = null;

	public void setMasterUnit(String uom)
	{
		this.uom = uom;
	}
	
	public String getMasterUnit()
	{
		return uom;
	}
	//02/21/2011 FW  Added code to insert masterUnit of pack instead of 'EA' into transferdetail table (Incident4288675_Defect300004) -- End
}
