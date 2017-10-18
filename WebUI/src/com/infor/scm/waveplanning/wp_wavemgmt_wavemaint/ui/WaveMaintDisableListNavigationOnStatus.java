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

package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeListRowInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.infor.scm.waveplanning.common.util.BioAttributeUtil;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class WaveMaintDisableListNavigationOnStatus extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{

	
	/**
	 * Called in response to the modifyListValues event on a list form. Subclasses  must override this in order
	 * to customize the display values of a list form
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form) throws EpiException
	{

		try
		{
			StateInterface state = context.getState();
			UnitOfWorkBean uow = state.getDefaultUnitOfWork();
			String totalDesc = "Total";
			BioCollectionBean rs = uow.getBioCollectionBean(new Query("wm_codesdetail",
					"wm_codesdetail.LISTNAME='WSVIEW' and wm_codesdetail.DESCRIPTION ='5'", null));
			for (int i = 0; i < rs.size(); i++)
			{
				final BioBean statusBean = rs.get("" + i);
				totalDesc = BioAttributeUtil.getString((DataBean) statusBean, "STATUS");
			}
			// Add your code here to process the event
			RuntimeListRowInterface[] listRows = form.getRuntimeListRows();
			for (int i = 0; i < listRows.length; i++)
			{
				RuntimeFormWidgetInterface detailButton = listRows[i].getFormWidgetByName("DETAIL");
				RuntimeFormWidgetInterface statusWidget = listRows[i].getFormWidgetByName("STATUS");
				RuntimeFormWidgetInterface unitsWidget = listRows[i].getFormWidgetByName("UNITS");
				RuntimeFormWidgetInterface percentWidget = listRows[i].getFormWidgetByName("PERCENTVAL");
				
				double unitsValue = 0; 
				final String statusValue = statusWidget.getDisplayValue();
				
				try
				{
					unitsValue = Double.parseDouble(unitsWidget.getDisplayValue());
				} catch (NumberFormatException e)
				{
					unitsValue = 0;
				}

				
				if (statusValue.equals(totalDesc))
				{
					//detailButton.setBooleanProperty(RuntimeFormWidgetInterface.PROP_HIDDEN, true);
					detailButton.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					detailButton.setProperty(RuntimeFormWidgetInterface.PROP_IMAGE,"images\\1x1clear.gif");
					statusWidget.setProperty("widget style", "font-weight: bold");
					unitsWidget.setProperty("widget style", "font-weight: bold");
					percentWidget.setProperty("widget style", "font-weight: bold");
				}
				
				if(unitsValue == 0)
				{
					detailButton.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					detailButton.setProperty(RuntimeFormWidgetInterface.PROP_IMAGE,"images\\1x1clear.gif");
				}

			}

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	
}
