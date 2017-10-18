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
package com.ssaglobal.scm.wms.util.deleteverification;


	import com.epiphany.shr.ui.action.ActionExtensionBase;
	import com.epiphany.shr.ui.action.ActionContext;
	import com.epiphany.shr.ui.action.ActionResult;
	import com.epiphany.shr.util.exceptions.EpiException;
	import com.epiphany.shr.util.exceptions.UserException;
	import com.epiphany.shr.ui.state.StateInterface;
	import com.epiphany.shr.ui.model.data.BioCollectionBean;
	import com.epiphany.shr.ui.model.data.DataBean;
	import com.epiphany.shr.ui.view.RuntimeListFormInterface;
	import com.epiphany.shr.ui.exceptions.FormException;
	import java.util.ArrayList;
	import java.util.Iterator;
	import com.epiphany.shr.util.logging.ILoggerCategory;
	import com.epiphany.shr.util.logging.LoggerFactory;
	public class DeleteChildVerification extends ActionExtensionBase
	{

		protected static ILoggerCategory _log = LoggerFactory.getInstance(DeleteChildVerification.class);
		
		protected int execute(ActionContext context, ActionResult result) throws EpiException
		{
			String childAttributeName = (String) getParameter("ChildAttributeName");
			String parentName = (String) getParameter("ParentName");
			String childTabName = (String) getParameter("ChildTabName");
			
			StateInterface state = context.getState();
			String[] parameter = new String[2];
			parameter[0]= parentName;
			parameter[1]= childTabName;
			

			RuntimeListFormInterface form = (RuntimeListFormInterface) state.getRuntimeForm(state.getCurrentRuntimeForm().getParentForm(state).getSubSlot("list_slot_1"), null);

			ArrayList items;
			try
			{
				items = form.getAllSelectedItems();
				if (items.isEmpty())
				{
					throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
				}
			} catch (NullPointerException e)
			{
				throw new UserException("WMEXP_NONE_SELECTED", new Object[] {});
			}
			for (Iterator it = items.iterator(); it.hasNext();)
			{
				DataBean key = (DataBean) it.next();
				Object keyValue = key.getValue(childAttributeName);
				if(keyValue != null && keyValue instanceof BioCollectionBean){
					int size = ((BioCollectionBean)keyValue).size();
					if(size > 0){
						throw new FormException("WMEXP_DELETE_CHILD_VERIFICATION", parameter);						
					}
				}
								
			}

			return RET_CONTINUE;
		}
	}

