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
package com.ssaglobal.scm.wms.wm_shipmentorder_suspend.ui;

import java.io.Serializable;
import java.util.GregorianCalendar;

public class OrderSuspensionCriteriaVO implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8460292922153438300L;

	String lot;

	String lottable01;

	String lottable02;

	String lottable03;

	GregorianCalendar lottable04; //date

	GregorianCalendar lottable05; //date

	String lottable06;

	String lottable07;

	String lottable08;

	String lottable09;

	String lottable10;

	GregorianCalendar lottable11; //date

	GregorianCalendar lottable12; //date

	String orderkey;

	String sku;

	String status;

	String consigneekey;

	String suspendedindicator;

	public String getLot()
	{
		return lot;
	}

	public void setLot(String lot)
	{
		this.lot = lot;
	}

	public String getLottable01()
	{
		return lottable01;
	}

	public void setLottable01(String lottable01)
	{
		this.lottable01 = lottable01;
	}

	public String getLottable02()
	{
		return lottable02;
	}

	public void setLottable02(String lottable02)
	{
		this.lottable02 = lottable02;
	}

	public String getLottable03()
	{
		return lottable03;
	}

	public void setLottable03(String lottable03)
	{
		this.lottable03 = lottable03;
	}

	public GregorianCalendar getLottable04()
	{
		return lottable04;
	}

	public void setLottable04(GregorianCalendar lottable04)
	{
		this.lottable04 = lottable04;
	}

	public GregorianCalendar getLottable05()
	{
		return lottable05;
	}

	public void setLottable05(GregorianCalendar lottable05)
	{
		this.lottable05 = lottable05;
	}

	public String getLottable06()
	{
		return lottable06;
	}

	public void setLottable06(String lottable06)
	{
		this.lottable06 = lottable06;
	}

	public String getLottable07()
	{
		return lottable07;
	}

	public void setLottable07(String lottable07)
	{
		this.lottable07 = lottable07;
	}

	public String getLottable08()
	{
		return lottable08;
	}

	public void setLottable08(String lottable08)
	{
		this.lottable08 = lottable08;
	}

	public String getLottable09()
	{
		return lottable09;
	}

	public void setLottable09(String lottable09)
	{
		this.lottable09 = lottable09;
	}

	public String getLottable10()
	{
		return lottable10;
	}

	public void setLottable10(String lottable10)
	{
		this.lottable10 = lottable10;
	}

	public GregorianCalendar getLottable11()
	{
		return lottable11;
	}

	public void setLottable11(GregorianCalendar lottable11)
	{
		this.lottable11 = lottable11;
	}

	public GregorianCalendar getLottable12()
	{
		return lottable12;
	}

	public void setLottable12(GregorianCalendar lottable12)
	{
		this.lottable12 = lottable12;
	}

	public String getOrderkey()
	{
		return orderkey;
	}

	public void setOrderkey(String orderkey)
	{
		this.orderkey = orderkey;
	}

	public String getSku()
	{
		return sku;
	}

	public void setSku(String sku)
	{
		this.sku = sku;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}


	public String getSuspendedindicator()
	{
		return suspendedindicator;
	}

	public void setSuspendedindicator(String suspendedindicator)
	{
		this.suspendedindicator = suspendedindicator;
	}

	public String getConsigneekey() {
		return consigneekey;
	}

	public void setConsigneekey(String consigneekey) {
		this.consigneekey = consigneekey;
	}

}
