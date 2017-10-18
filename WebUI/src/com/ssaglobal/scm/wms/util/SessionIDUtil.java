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
package com.ssaglobal.scm.wms.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Properties;

import javax.servlet.http.Cookie;

import com.epiphany.shr.sf.EpnyServiceManager;
import com.epiphany.shr.sf.util.EpnyServiceManagerServer;
import com.epiphany.shr.sf.util.EpnyUserContext;
import com.epiphany.shr.sso.SessionID;
import com.epiphany.shr.sso.SessionObject;
import com.epiphany.shr.sso.client.ConfigAdapter;
import com.epiphany.shr.sso.exception.InvalidSession;
import com.epiphany.shr.sso.exception.SessionExpired;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.util.config.ConfigService;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.EpiSecurityException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;

public class SessionIDUtil {

	private static com.epiphany.shr.sso.client.ComponentRegistry _ssoComponentRegistry;

	private static boolean _bSSODisabled;

	private Properties _clientProps;

	private final SessionID sessionId;

	private final UIRenderContext context;

	protected static ILoggerCategory _log = LoggerFactory.getInstance(SessionIDUtil.class);

	private static class SSOConfigAdapter implements ConfigAdapter {

		public String getValue(String Name, String DefaultValue) {
			if (Name.equals("com.epiphany.shr.sso.ClientName")) {
				String name = (String) _clientProps.get("com.epiphany.shr.sso.client.Name");
				if (name != null)
					return name;
			} else
				if (Name.equals("com.epiphany.shr.sso.ClientPrivateKey")) {
					String key = (String) _clientProps.get("com.epiphany.shr.sso.client.Key");
					if (key != null)
						return key;
				}
			return DefaultValue;
		}

		private final Properties _clientProps;

		SSOConfigAdapter(String clientFileName) {
			_clientProps = new Properties();
			try {
				File fileObj = new File(clientFileName);
				LineNumberReader is = new LineNumberReader(new InputStreamReader(new FileInputStream(fileObj), "UTF-8"));
				for (String line = is.readLine(); line != null; line = is.readLine()) {
					if (line.startsWith("com.epiphany.shr.sso.client.Name")) {
						String val = line.substring("com.epiphany.shr.sso.client.Name".length() + 1);
						_clientProps.setProperty("com.epiphany.shr.sso.client.Name", val);
						continue;
					}
					if (line.startsWith("com.epiphany.shr.sso.client.Key")) {
						String val = line.substring("com.epiphany.shr.sso.client.Key".length() + 1);
						_clientProps.setProperty("com.epiphany.shr.sso.client.Key", val);
					}
				}

				is.close();
			} catch (Exception Ex) {
				Ex.printStackTrace();
			}
		}
	}

	static {
		_log = LoggerFactory.getInstance(com.epiphany.shr.sf.util.EpnyUserContextManager.class);
		_ssoComponentRegistry = new com.epiphany.shr.sso.client.ComponentRegistry();
		String ssoPassThru = ConfigService.getInstance().safeGetProperty("FrontEnd", "sso-passthru-enabled");
		_bSSODisabled = Boolean.valueOf(ssoPassThru).booleanValue();
		if (!_bSSODisabled)
			try {
				String clientKeysFileName = ConfigService.getInstance().safeGetProperty("ServiceManager",
																						"sso-keys-filename");
				if (clientKeysFileName == null || clientKeysFileName.length() == 0)
					throw new EpiException("EXP_SF_SSO_ERR_INIT_CLIENT_KEYS", "No SSO Client keys are specified");
				SSOConfigAdapter cfa = new SSOConfigAdapter(clientKeysFileName);
				_ssoComponentRegistry.initialize(cfa);
			} catch (Throwable t) {
				_log.error(new EpiException("EXP_SF_SSO_INIT_COMP_REG", "SSO ComponentRegistry could not be initialized", 131072L, t));
			}
	}

	public SessionIDUtil(UIRenderContext context) {
		this.context = context;
		sessionId = _ssoComponentRegistry.getSessionID();
	}

	public String ssoTokenInfo(Serializable serializable) {
		StringWriter tokenInfo = new StringWriter();
		SessionObject materialize = null;
		try {
			materialize = sessionId.materialize((String) serializable);
		} catch (InvalidSession e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SessionExpired e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		tokenInfo.write("Generating Session " + materialize.generatingSession + "\n");
		tokenInfo.write("Session ID " + materialize.sessionID + "\n");
		tokenInfo.write("Session Ticket " + materialize.sessionTicket + "\n");
		tokenInfo.write("User ID " + materialize.userID + "\n");
		tokenInfo.write("Expiry Date " + materialize.expiryDate + "\n");

		return tokenInfo.toString();
	}

	public String renew(String ssoToken, Date start, Date end) throws InvalidSession, SessionExpired, EpiSecurityException {
		_log.info(	"LOG_INFO_EXTENSION_SessionIDUtil_renew",
					"Creating new SSO Token based on " + ssoToken + " With new Start and End Times: " + start + ", and " + end,
					SuggestedCategory.NONE);
		String newToken = sessionId.renew(ssoToken, start, end);
		EpnyServiceManager serviceManager = context.getServiceManager();
		EpnyUserContext userContext = serviceManager.getUserContext();
		if (serviceManager instanceof EpnyServiceManagerServer) {
			EpnyServiceManagerServer es = (EpnyServiceManagerServer) serviceManager;
			_log.info(	"LOG_DEBUG_EXTENSION_SessionIDUtil_renew",
						"Validating New SSO Token " + newToken,
						SuggestedCategory.NONE);
			es.validateToken((String) userContext.getSecurityHandle());
		}
		

		return newToken;
	}

	public boolean setSessionIDIntoCookie(String newToken) {
		Cookie sessionIDCookie = getSessionIDCookie();
		if (sessionIDCookie != null) {
			// expire existing cookie
			sessionIDCookie.setMaxAge(0);
			context.getState().getResponse().addCookie(sessionIDCookie);
			// set new cookie
			sessionIDCookie.setMaxAge(-1);
			try {
				sessionIDCookie.setValue(URLEncoder.encode(newToken, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				sessionIDCookie.setValue(newToken);
			}
			context.getState().getResponse().addCookie(sessionIDCookie);
			context.getServiceManager().getUserContext().setSecurityHandle(newToken);
			// set Session Timeout
			context.getState().getRequest().getSession().setMaxInactiveInterval(-1);
			return true;
		}
		return false;
	}

	private Cookie getSessionIDCookie() {
		Cookie[] cookies = context.getState().getRequest().getCookies();
		Cookie sessionIDCookie = null;
		for (Cookie c : cookies) {
			if ("com.epiphany.SessionID".equals(c.getName())) {
				_log.info(	"LOG_INFO_EXTENSION_SessionIDUtil_setSessionIDIntoCookie",
							"Found Session Cookie " + c,
							SuggestedCategory.NONE);
				sessionIDCookie = c;
			}
		}
		return sessionIDCookie;
	}

	private Cookie getSessionCookie() {
		Cookie[] cookies = context.getState().getRequest().getCookies();
		Cookie sessionCookie = null;
		for (Cookie c : cookies) {
			if ("JSESSIONID".equals(c.getName())) {
				sessionCookie = c;
			}
		}
		return sessionCookie;
	}
}
