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


import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import com.epiphany.shr.ui.action.ActionExtensionBase;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.SsaAccessBase;


public class CancelReceiptDetails extends ActionExtensionBase {

    /**
     * @param context
     * @param result
     * @return int
     * @throws EpiException
     */
	protected int execute(ModalActionContext context, ActionResult args) throws EpiException {

		StateInterface state = context.getState();
		HttpSession session = state.getRequest().getSession();
		String receiptKey = session.getAttribute("RECEIPTKEY").toString();
		

		String facilityName = session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();
    	String theSQL = "DELETE FROM RECEIPTDETAIL WHERE WHSEID = 'DUP' AND RECEIPTKEY = '"+receiptKey+"' ";
    		
    	try 
    	{
    		SsaAccessBase appAccess = SQLDPConnectionFactory.getAppAccess();
    		appAccess.executeUpdate(facilityName.toUpperCase(), theSQL, new Object[0]);
    	} 
    	catch (SQLException e) 
    	{			
			e.printStackTrace();
			throw new UserException("WMEXP_RECEIPT_DUP_DELETE", new Object[1]);
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
			throw new UserException("WMEXP_RECEIPT_DUP_DELETE", new Object[1]);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new UserException("WMEXP_RECEIPT_DUP_DELETE", new Object[1]);
		}

		session.removeAttribute("RECEIPTKEY");
		session.removeAttribute("RECEIPTLINENUMBER");

        return RET_CONTINUE;
    }
    
}