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
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.action.ModalUIRenderContext;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeListRowInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.infor.scm.waveplanning.common.util.BioAttributeUtil;
import com.infor.scm.waveplanning.common.util.NSQLConfigUtil;

/**
* Descriptive Text to describe the extension
* you should state the event being trapped and
* list any parameters expected to be provided from
* the meta
* <P>
* @return int RET_CONTINUE, RET_CANCEL
*/

public class WaveMaintConsolidateDisableConsolidatedItems extends
		com.epiphany.shr.ui.view.customization.FormExtensionBase
{

	/**
	 * Called in response to the pre-render event on a list form. Write code
	 * to customize the properties of a list form dynamically, change the bio collection being displayed
	 * in the form or filter the bio collection
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	@Override
	protected int preRenderListForm(UIRenderContext context, RuntimeListFormInterface form) throws EpiException
	{

		try
		{
			// Add your code here to process the event

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * Called in response to the pre-render event on a list form in a modal dialog. Write code
	 * to customize the properties of a list form dynamically, change the bio collection being displayed
	 * in the form or filter the bio collection
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state},
	 * service information and modal dialog context
	 * @param form the form that is about to be rendered
	 */
	@Override
	protected int preRenderListForm(ModalUIRenderContext context, RuntimeListFormInterface form) throws EpiException
	{

		try
		{
			// Add your code here to process the event

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * Called in response to the modifyListValues event on a list form. Subclasses  must override this in order
	 * to customize the display values of a list form
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	@Override
	protected int modifyListValues(UIRenderContext context, RuntimeListFormInterface form) throws EpiException
	{

		try
		{
			// Add your code here to process the event

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	/**
	 * Called in response to the modifyListValues event on a list form in a modal dialog. Subclasses  must override this in order
	 * to customize the display values of a list form
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state},
	 * service information and modal dialog context
	 * @param form the form that is about to be rendered
	 */
	@Override
	protected int modifyListValues(ModalUIRenderContext context, RuntimeListFormInterface form) throws EpiException
	{

		try
		{
			StateInterface state = context.getState();
			BioCollectionBean allSkus = (BioCollectionBean) form.getFocus();
			//9429. Speed pick location assignment - need to allow editing
			NSQLConfigUtil commingleSpeedLineLoc = new NSQLConfigUtil(state, "COMMINGLESPEEDLINELOC");

			RuntimeListRowInterface[] listRows = form.getRuntimeListRows();
			for (int i = 0; i < listRows.length; i++)
			{
				RuntimeFormWidgetInterface ownerWdgt = listRows[i].getFormWidgetByName("STORERKEY");
				RuntimeFormWidgetInterface skuWdgt = listRows[i].getFormWidgetByName("SKU");
				RuntimeFormWidgetInterface speedLocWdgt = listRows[i].getFormWidgetByName("CONSOLLOC");
				RuntimeFormWidgetInterface maxLocWdgt = listRows[i].getFormWidgetByName("MAXLOCQTY");
				RuntimeFormWidgetInterface assignedWdgt = listRows[i].getFormWidgetByName("ASSIGNED");
				
				//get speedLoc from bio
				final String owner = ownerWdgt.getDisplayValue();
				final String sku = skuWdgt.getDisplayValue();
				String speedLocValueFromBio = null; 
				speedLocValueFromBio = getSpeedLocValueFromBio(allSkus, owner, sku);

				//final String speedLoc = speedLocWdgt.getDisplayValue();
				final String assignedValue = assignedWdgt.getDisplayValue();

				//toggle behaviour depending on flag
				if (commingleSpeedLineLoc.isOn()) {
					//new behaviour
					//allow editing
					speedLocWdgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					maxLocWdgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
				} else {
					//if speedLoc is not empty, disable
					if ((speedLocValueFromBio != null && !speedLocValueFromBio.matches("\\s*")) && assignedValue.equals("1")) {
						speedLocWdgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
						maxLocWdgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);
					} else {
						speedLocWdgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
						maxLocWdgt.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
					}
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

	private String getSpeedLocValueFromBio(BioCollectionBean allSkus, final String owner, final String sku) throws EpiDataException
	{
		String speedLocValueFromBio = null;
		for(int j = 0; j < allSkus.size(); j++)
		{
			final BioBean skuBio = allSkus.get(""+j);
			skuBio.getValue("CONSOLLOC", true);
			skuBio.getValue("CONSOLLOC", false);
			String skuValueFromBio = BioAttributeUtil.getString((DataBean) skuBio, "SKU");
			String storerValueFromBio = BioAttributeUtil.getString((DataBean) skuBio, "STORERKEY");
			if(owner.equals(storerValueFromBio) && sku.equals(skuValueFromBio))
			{
				speedLocValueFromBio = BioAttributeUtil.getString((DataBean) skuBio, "CONSOLLOC");
			}
		}
		return speedLocValueFromBio;
	}
}
