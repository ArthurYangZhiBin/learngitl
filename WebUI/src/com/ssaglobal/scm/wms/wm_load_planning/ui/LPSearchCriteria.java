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
package com.ssaglobal.scm.wms.wm_load_planning.ui;

import java.io.Serializable;
import java.util.Calendar;


public class LPSearchCriteria implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7852071833725163033L;

	private String sellerS;

	private String sellerE;

	private String custS;

	private String custE;

	private String ohTypeS;

	private String ohTypeE;

	private String orderS;

	private String orderE;

	private Calendar oDateS;

	private Calendar oDateE;

	private Calendar dDateS;

	private Calendar dDateE;

	private String flowTS;

	private String flowTE;

	private String transS;

	private String transE;
	
	private String routeS;
	
	private String routeE;
	
	private String stopS;
	
	private String stopE;
	
	private String loadIdS;
	
	private String loadIdE;
	
	private String externalLoadIdS;
	
	private String externalLoadIdE;
	
	private String outboundLaneS;
	
	private String outboundLaneE;
	
	private String transShipASNS;
	
	private String transShipASNE;
	
	private String typeS;
	
	private String typeE;
	

	public LPSearchCriteria(String ss, String se, String cs, String ce, String ohs, String ohe, String os, String oe,
			Calendar ods, Calendar ode, Calendar ds, Calendar de, String fs, String fe, String ts, String te,
			String routeS, String routeE, String stopS, String stopE, String loadIdS, String loadIdE,
			String externalLoadIdS, String externalLoadIdE, String outboundLaneS, String outboundLaneE,
			String transShipASNS, String transShipASNE, String typeS, String typeE)
	{
		sellerS = ss;
		sellerE = se;
		custS = cs;
		custE = ce;
		ohTypeS = ohs;
		ohTypeE = ohe;
		orderS = os;
		orderE = oe;
		oDateS = ods;
		oDateE = ode;
		dDateS = ds;
		dDateE = de;
		flowTS = fs;
		flowTE = fe;
		transS = ts;
		transE = te;
		this.routeE = routeE;
		this.routeS = routeS;
		this.stopS = stopS;
		this.stopE = stopE;
		this.loadIdS = loadIdS;
		this.loadIdE = loadIdE;
		this.externalLoadIdS = externalLoadIdS;
		this.externalLoadIdE = externalLoadIdE;
		this.outboundLaneS = outboundLaneS;
		this.outboundLaneE = outboundLaneE;
		this.transShipASNS = transShipASNS;
		this.transShipASNE = transShipASNE;
		this.typeS = typeS;
		this.typeE = typeE;
		
	}

	public void setSellerS(String sellerS)
	{
		this.sellerS = sellerS;
	}

	public String getSellerS()
	{
		return sellerS;
	}

	public void setSellerE(String sellerE)
	{
		this.sellerE = sellerE;
	}

	public String getSellerE()
	{
		return sellerE;
	}

	public void setCustS(String custS)
	{
		this.custS = custS;
	}

	public String getCustS()
	{
		return custS;
	}

	public void setCustE(String custE)
	{
		this.custE = custE;
	}

	public String getCustE()
	{
		return custE;
	}

	public void setOhTypeS(String ohTypeS)
	{
		this.ohTypeS = ohTypeS;
	}

	public String getOhTypeS()
	{
		return ohTypeS;
	}

	public void setOhTypeE(String ohTypeE)
	{
		this.ohTypeE = ohTypeE;
	}

	public String getOhTypeE()
	{
		return ohTypeE;
	}

	public void setOrderS(String orderS)
	{
		this.orderS = orderS;
	}

	public String getOrderS()
	{
		return orderS;
	}

	public void setOrderE(String orderE)
	{
		this.orderE = orderE;
	}

	public String getOrderE()
	{
		return orderE;
	}

	public void setODateS(Calendar oDateS)
	{
		this.oDateS = oDateS;
	}

	public Calendar getODateS()
	{
		return oDateS;
	}

	public void setODateE(Calendar oDateE)
	{
		this.oDateE = oDateE;
	}

	public Calendar getODateE()
	{
		return oDateE;
	}

	public void setDDateS(Calendar dDateS)
	{
		this.dDateS = dDateS;
	}

	public Calendar getDDateS()
	{
		return dDateS;
	}

	public void setDDateE(Calendar dDateE)
	{
		this.dDateE = dDateE;
	}

	public Calendar getDDateE()
	{
		return dDateE;
	}

	public void setFlowTS(String flowTS)
	{
		this.flowTS = flowTS;
	}

	public String getFlowTS()
	{
		return flowTS;
	}

	public void setFlowTE(String flowTE)
	{
		this.flowTE = flowTE;
	}

	public String getFlowTE()
	{
		return flowTE;
	}

	public void setTransS(String transS)
	{
		this.transS = transS;
	}

	public String getTransS()
	{
		return transS;
	}

	public void setTransE(String transE)
	{
		this.transE = transE;
	}

	public String getTransE()
	{
		return transE;
	}
	
	public void setRouteS(String routeS){
		this.routeS = routeS;
	}
	
	public String getRouteS()
	{
		return this.routeS;
	}
	
	public void setRouteE(String routeE){
		this.routeS = routeE;
	}
	
	public String getRouteE()
	{
		return this.routeE;
	}

	
	public void setStopS(String stopS)
	{
		this.stopS = stopS;
	}
	
	public String getStopS()
	{
		return this.stopS;
	}

	public void setStopE(String stopE)
	{
		this.stopE = stopE;
	}
	
	public String getStopE()
	{
		return this.stopE;
	}

	public void setLoadIdS(String loadIdS)
	{
		this.loadIdS = loadIdS;
	}
	
	public String getLoadIdS()
	{
		return this.loadIdS;
	}

	public void setLoadIdE(String loadIdE)
	{
		this.loadIdE = loadIdE;
	}
	
	public String getLoadIdE()
	{
		return this.loadIdE;
	}

	public void setExternalLoadIdS(String externalLoadIdS)
	{
		this.externalLoadIdS = externalLoadIdS;
	}
	
	public String getExternalLoadIdS()
	{
		return this.externalLoadIdS;
	}

	public void setExternalLoadIdE(String externalLoadIdE)
	{
		this.externalLoadIdE = externalLoadIdE;
	}
	
	public String getExternalLoadIdE()
	{
		return this.externalLoadIdE;
	}

	public void setOutboundLaneS(String outboundLaneS)
	{
		this.outboundLaneS = outboundLaneS;
	}
	
	public String getOutboundLaneS()
	{
		return this.outboundLaneS;
	}

	public void setOutboundLaneE(String outboundLaneE)
	{
		this.outboundLaneE = outboundLaneE;
	}
	
	public String getOutboundLaneE()
	{
		return this.outboundLaneE;
	}

	public void setTransShipASNS(String transShipASNS)
	{
		this.transShipASNS = transShipASNS;
	}
	
	public String getTransShipASNS()
	{
		return this.transShipASNS;
	}

	public void setTransShipASNE(String transShipASNE)
	{
		this.transShipASNE = transShipASNE;
	}
	
	public String getTransShipASNE()
	{
		return this.transShipASNE;
	}

	public String getTypeS() {
		return typeS;
	}

	public void setTypeS(String typeS) {
		this.typeS = typeS;
	}

	public String getTypeE() {
		return typeE;
	}

	public void setTypeE(String typeE) {
		this.typeE = typeE;
	}

}
