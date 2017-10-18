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

package com.ssaglobal.scm.wms.wm_wave_labels.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormExtendedInterface;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.action.ModalUIRenderContext;

/**
 * Descriptive Text to describe the extension
 * you should state the event being trapped and
 * list any parameters expected to be provided from
 * the meta
 * <P>
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class WaveLabelsPreRender extends com.epiphany.shr.ui.view.customization.FormExtensionBase
{

	/*
	 Wave Case Labels	0
	 Wave/Order.
	 Validate Wavekey
	 WAVE
	 Batch Case Labels	1
	 Wave/Order
	 Validate Wavekey
	 BATCH
	 CL B A				2
	 Wave/Order & Assn
	 Wavekey and Assn
	 WAVE_ASSIGNMENT
	 CL f OA				3
	 Wave/Order & Assn
	 Orderkey & Assn
	 ORD_ASSIGNMENT
	 CL f OO				4
	 Wave/Order
	 Orderkey
	 ORD
	 */
	/**
	 * Called in response to the pre-render event on a form. Write code
	 * to customize the properties of a form. All code that initializes the properties of a form that is
	 * being displayed to a user for the first time belong here. This is not executed even if the form
	 * is re-displayed to the end user on subsequent actions.
	 *
	 * @param context exposes information about user interface, {@link com.epiphany.shr.ui.state.StateInterface state}
	 * and service
	 * @param form the form that is about to be rendered
	 */
	protected int preRenderForm(UIRenderContext context, RuntimeNormalFormInterface form) throws EpiException
	{
		RuntimeFormWidgetInterface waveWidget = form.getFormWidgetByName("WAVEORDER");
		RuntimeFormWidgetInterface assnWidget = form.getFormWidgetByName("ASSNNUMBER");
		try
		{
			int reportID = 0;
			if (isEmpty(form.getFormWidgetByName("WAVELABELS").getValue()))
			{
				form.getFormWidgetByName("WAVELABELS").setValue("0");
			}
			else
			{
				reportID = Integer.parseInt(form.getFormWidgetByName("WAVELABELS").getValue().toString());
			}
			if (reportID == 0)
			{
				waveWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
				assnWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);

				waveWidget.setProperty("input type", "required");
				assnWidget.setProperty("input type", "normal");
				
				assnWidget.setDisplayValue("");
				assnWidget.setValue("");

				//				requiredField("WAVEORDER");
				//				validateWaveKey();
				//				keyType = "WAVE";
			}
			else if (reportID == 1)
			{
				waveWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
				assnWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);

				waveWidget.setProperty("input type", "required");
				assnWidget.setProperty("input type", "normal");
				
				assnWidget.setDisplayValue("");
				assnWidget.setValue("");

				//				requiredField("WAVEORDER");
				//				validateWaveKey();
				//				keyType = "BATCH";
			}
			else if (reportID == 2)
			{
				waveWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
				assnWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);

				waveWidget.setProperty("input type", "required");
				assnWidget.setProperty("input type", "required");
				//				requiredField("WAVEORDER");
				//				requiredField("ASSNNUMBER");
				//				validateWaveKey();
				//				validateAssnNumber();
				//				keyType = "WAVE_ASSIGNMENT";
			}
			else if (reportID == 3)
			{
				waveWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
				assnWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);

				waveWidget.setProperty("input type", "required");
				assnWidget.setProperty("input type", "required");
				//				requiredField("WAVEORDER");
				//				requiredField("ASSNNUMBER");
				//				validateOrderKey();
				//				validateAssnNumber();
				//				keyType = "ORD_ASSIGNMENT";
			}
			else if (reportID == 4)
			{
				waveWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, false);
				assnWidget.setBooleanProperty(RuntimeFormWidgetInterface.PROP_READONLY, true);

				waveWidget.setProperty("input type", "required");
				assnWidget.setProperty("input type", "normal");
				
				assnWidget.setDisplayValue("");
				assnWidget.setValue("");
				//				requiredField("WAVEORDER");
				//				validateOrderKey();
				//				keyType = "ORD";
			}

		} catch (Exception e)
		{

			// Handle Exceptions 
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	boolean isEmpty(Object attributeValue)
	{
		if (attributeValue == null)
		{
			return true;
		}
		else if (attributeValue.toString().matches("\\s*"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
