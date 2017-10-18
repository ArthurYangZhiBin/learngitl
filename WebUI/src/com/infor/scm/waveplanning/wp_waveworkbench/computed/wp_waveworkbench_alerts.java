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
package com.infor.scm.waveplanning.wp_waveworkbench.computed;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;



import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.computed.ComputedAttributeSupport;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.metadata.objects.dbl.function.GetTextMessage;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.infor.scm.waveplanning.common.util.WPUserUtil;

public class wp_waveworkbench_alerts implements ComputedAttributeSupport
{

	private StateInterface state = null;

	public Object get(Bio bio, String alerts, boolean val) throws EpiDataException
	{

		Locale localeObj = WPUserUtil.getUserLocale();

		Object eventDetails = bio.get("EVENTDETAILS");
		String eventDet = "";
		String output = "";
		String userLocale = EpnyServiceManagerFactory.getInstance().getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		if (eventDetails != null)
		{
			eventDet = eventDetails.toString();
			StringTokenizer stoken = new StringTokenizer(eventDet, ",");
			String message = stoken.nextToken();

			if (stoken.hasMoreElements())
			{
				final int tokenCount = stoken.countTokens();
				Object[] arguments = new Object[tokenCount + 1];
				String eventValue = mda.getLocalizedTextMessage(stoken.nextToken(), locale);
				arguments[0] = eventValue;
				int index = 1;
				if (stoken.hasMoreElements())
				{
					while (index < tokenCount)
					{
						arguments[index] = stoken.nextElement();
						index++;
					}
				}
				arguments[index] = "null";
				output = mda.getLocalizedTextMessage(message, arguments, locale);
			}

		}
		return output;

		/*
		//NEEDS TO BE REWRITTEN WITHOUT USING THE RESOURCE BUNDLE
		
		ResourceBundle messages = ResourceBundle.getBundle("com.ssaglobal.scm.waveplanning.resources.WavePlanning" , localeObj);
		
		if (eventDetails != null) {
			eventDet = eventDetails.toString();			
			StringTokenizer stoken=new StringTokenizer(eventDet,",");
			MessageFormat formatter = new MessageFormat("");
			formatter.setLocale(localeObj);
			
			if(stoken.hasMoreElements()) {	
				formatter.applyPattern(messages.getString(stoken.nextToken()));
				if(stoken.hasMoreElements()) {
					int tokencount=stoken.countTokens();
					String eventvalue=messages.getString(stoken.nextToken());
				  	Object[] messageArguments=new Object[tokencount+1];
					messageArguments[0]=eventvalue;
					int index=1;	
				 	if(stoken.hasMoreElements()) {								
				 		while(index<tokencount) {
				 			messageArguments[index]=stoken.nextElement();
							index++;
						}							 
				 	}
					//this extrac null value is used when engine returns null as value
					messageArguments[index]="null";
					output=formatter.format(messageArguments);								 
				}	    	
			}			
		}		
		*/
		//		return output;
		//return "FIXME!!";
	}

	public boolean supportsSet(String arg0, String arg1)
	{
		return false;
	}

	public void set(Bio arg0, String arg1, Object arg2, boolean arg3) throws EpiDataException
	{
		// TODO Auto-generated method stub

	}

	public StateInterface getState()
	{
		return state;
	}

	public void setState(StateInterface state)
	{
		this.state = state;
	}

}
