 /******************************************************
 *
 *                       NOTICE
 *
 *   THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS
 *   CONFIDENTIAL INFORMATION OF INFOR AND SHALL NOT
 *   BE DISCLOSED WITHOUT PRIOR WRITTEN PERMISSION.
 *   LICENSED CUSTOMERS MAY COPY AND ADAPT THIS
 *   SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH
 *   THE TERMS OF THEIR SOFTWARE LICENSE AGREEMENT.
 *   ALL OTHER RIGHTS RESERVED.
 *
 *   COPYRIGHT (c) 2008 INFOR. ALL RIGHTS RESERVED.
 *   THE WORD AND DESIGN MARKS SET FORTH HEREIN ARE
 *   TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 *   AND/OR RELATED AFFILIATES AND SUBSIDIARIES. ALL
 *   RIGHTS RESERVED. ALL OTHER TRADEMARKS LISTED
 *   HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE
 *   OWNERS. WWW.INFOR.COM.
 *
 ******************************************************/



package com.ssaglobal.scm.wms.wm_asnreceipts.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;

import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;

import com.epiphany.shr.metadata.interfaces.SlotInterface;
import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.navigation.SetIntoHttpSessionAction;
import com.ssaglobal.scm.wms.util.UserUtil;
import com.ssaglobal.scm.wms.wm_table_validation.ccf.NumericValidationCCF;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class ValidationASNReceiveALLAppend extends com.epiphany.shr.ui.action.ActionExtensionBase {


   /**
    * The code within the execute method will be run from a UIAction specified in metadata.
    * <P>
    * @param context The ActionContext for this extension
    * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
    *
    * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
    *
    * @exception EpiException
    */
	public static final String STORERKEY = "STORERKEY";
	public static String RECEIVEALLCOUNT = "receiveall";
	public static String TABLE = "sku";
	public static String ITEM = "SKU";
	
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ValidationASNReceiveALLAppend.class);

   protected int execute( ActionContext context, ActionResult result ) throws EpiException {

      // Replace the following line with your code,
      // returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
      // as appropriate
		String[] params = new String[1];
		params[0] = null;
		StateInterface state = context.getState();
		UnitOfWorkBean uowb = state.getDefaultUnitOfWork();
		HttpSession session = state.getRequest().getSession();
		String userid = UserUtil.getUserId();
		RuntimeFormInterface toolbar = state.getCurrentRuntimeForm();
		RuntimeFormInterface shellForm = toolbar.getParentForm(state);
		SlotInterface headerSlot = shellForm.getSubSlot("list_slot_1");
		RuntimeFormInterface headerForm = state.getRuntimeForm(headerSlot, null);
		DataBean headerFocus = headerForm.getFocus();
        BioBean bio = (BioBean)headerFocus;		
        NumberFormat nf = NumberFormat.getInstance();
		BioCollectionBean detailList = (BioCollectionBean)bio.get("RECEIPTDETAILS");
		String storerkey = (String)bio.get("STORERKEY");
		int count = 0;
			
		String db_connection = session.getAttribute(SetIntoHttpSessionAction.DB_CONNECTION).toString();
		
		//检查收货验证模版，判断超收是否属于硬错误
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			InitialContext initialContext = new InitialContext();
			javax.sql.DataSource dataSource = (javax.sql.DataSource) initialContext.lookup("java:jdbc/"+db_connection.toUpperCase());
			connection = dataSource.getConnection();
			statement = connection.prepareStatement("SELECT COUNT(1) FROM STORER A, RECEIPTVALIDATION B WHERE A.RECEIPTVALIDATIONTEMPLATE=B.RECEIPTVALIDATIONKEY AND B.OVERAGEHARDERROR=1 AND A.TYPE=1 AND A.STORERKEY='"+storerkey+"'");			
			resultSet = statement.executeQuery();
			if(resultSet.next()){
				count = resultSet.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				resultSet.close();
				statement.close();
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//查询异常数据
        String skus = "";
        for (int j=0; j<detailList.size(); j++){
        	
        	BioBean asnDetailLineBio = (BioBean)detailList.elementAt(j);
        	String receiptkey = asnDetailLineBio.get("receiptkey").toString();
        	String recqty = (String)asnDetailLineBio.getValue("RECEIVEDQTY");
			java.math.BigDecimal expqty = (java.math.BigDecimal)asnDetailLineBio.getValue("QTYEXPECTED");
			String lineno = (String)asnDetailLineBio.getValue("RECEIPTLINENUMBER");
			
			double drecqty = 0;
			double dexpqty = 0;
			drecqty = NumericValidationCCF.parseNumber(recqty);
			dexpqty = expqty.doubleValue();
			
        	String sku  = asnDetailLineBio.get("SKU").toString();
        	
        	if(drecqty>dexpqty){
        		skus += sku+"\t";
        	}	
        }
		if(skus.length()>0 && count>0){
			throw new UserException("有物料超出预收量:"+skus,new Object[0]);
		}
        
        return super.execute( context, result );
   }
   
   /**
    * Fires in response to a UI action event, such as when a widget is clicked or
    * a value entered in a form in a modal dialog
    * Write code here if u want this to be called when the UI Action event is fired from a modal window
    * <ul>
    * <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext} exposes information about the
    * event, including the service and the user interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
    * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes information about the results
    * of the action that has occurred, and enables your extension to modify them.</li>
    * </ul>
    */
    protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException {

       try {
           // Add your code here to process the event
           
        } catch(Exception e) {
            
            // Handle Exceptions 
          e.printStackTrace();
          return RET_CANCEL;          
       } 
       
       return RET_CONTINUE;
    }
}
