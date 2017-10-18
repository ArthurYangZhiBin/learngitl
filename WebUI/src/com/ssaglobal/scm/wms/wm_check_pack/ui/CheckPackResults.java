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
package com.ssaglobal.scm.wms.wm_check_pack.ui;

import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

public class CheckPackResults {

	enum ContainerType {
		CASE, DROP
	}

	protected static ILoggerCategory _log = LoggerFactory.getInstance(CheckPackResults.class);

	private ContainerType type;

	private String container;

	private String order;

	private BioCollectionBean pickDetails;

	private String carrier;

	private boolean foundResults;
	
	private boolean innerPackage = false;

	public CheckPackResults() {
		setFoundResults(false);
	}

	public String getCarrier() {
		return carrier;
	}

	public String getContainer() {
		return container;
	}

	public String getOrder() {
		return order;
	}

	public BioCollectionBean getPickDetails() {
		return pickDetails;
	}

	public ContainerType getType() {
		return type;
	}

	public boolean isFoundResults() {
		return foundResults;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public void setContainer(String container) {
		this.container = container;
	}

	public void setFoundResults(boolean foundResults) {
		this.foundResults = foundResults;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public void setPickDetails(BioCollectionBean pickDetails) {
		try {
			if (pickDetails.size() > 0) {
				setFoundResults(true);
			}
		} catch (EpiDataException e) {
			_log.error("LOG_ERROR_EXTENSION_CheckPackResults_setPickDetails", e.getErrorMessage(), SuggestedCategory.NONE);
			_log.error("LOG_ERROR_EXTENSION_CheckPackResults_setPickDetails", e.getStackTraceAsString(), SuggestedCategory.NONE);
		}
		this.pickDetails = pickDetails;
	}

	public void setType(ContainerType type) {
		this.type = type;
	}

	public boolean isInnerPackage() {
		return innerPackage;
	}

	public void setInnerPackage(boolean innerPackage) {
		this.innerPackage = innerPackage;
	}

}
