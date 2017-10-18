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

public class MassInternalTransferMassUpdateDataObject
{

	private String storerkey;

	private String sku;

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

	private String MassUpdateProcessed;

	public String getStorerkey()
	{
		return storerkey;
	}

	public void setStorerkey(String storerkey)
	{
		this.storerkey = storerkey;
	}

	public String getSku()
	{
		return sku;
	}

	public void setSku(String sku)
	{
		this.sku = sku;
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

	public String getMassUpdateProcessed()
	{
		return MassUpdateProcessed;
	}

	public void setMassUpdateProcessed(String massUpdateProcessed)
	{
		MassUpdateProcessed = massUpdateProcessed;
	}

	
}
