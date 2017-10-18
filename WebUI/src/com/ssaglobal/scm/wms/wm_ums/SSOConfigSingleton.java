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
package com.ssaglobal.scm.wms.wm_ums;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import com.ssaglobal.SsaAccessBase;

public class SSOConfigSingleton {
	private String domainName;
	private String directoryServerType;
	private static SSOConfigSingleton ssoConfig = null;
	private String userBaseDn;
	private String userIdAttributeName;
	private SSOConfigSingleton(){
		this.setDomainNameDirectoryServerType();
	}
	public static SSOConfigSingleton getSSOConfigSingleton(){
		if(ssoConfig == null){
			return new SSOConfigSingleton();
		}else{
			return ssoConfig;
		}
	}
	
	private void setDomainNameDirectoryServerType(){
		try{
			String fileSeparator = System.getProperties().getProperty("file.separator");
//			SsaAccessBase appAccess = new SQLDPConnectionFactory();
//			String oaHome = appAccess.getValue("webUIConfig","OAHome");
			String shared_root= System.getProperty("shared.root");
			String strFileName = shared_root+fileSeparator+"etc"+fileSeparator+"sso_config.xml";
			File file = new File(strFileName);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			NodeList nodeLst = doc.getElementsByTagName("sso:directory");
			String directoryServerType = "";
			String domainName = "";
			for (int s = 0; s < nodeLst.getLength(); s++) {

			    Node fstNode = nodeLst.item(s);
			    NamedNodeMap attributesMap = fstNode.getAttributes();
			    directoryServerType=attributesMap.getNamedItem("xsi:type").getNodeValue();
			    
			    if (directoryServerType.equalsIgnoreCase(WMUMSConstants.NTLM_DIRECTORY_SERVICE))
			    {
				    if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
				  
				      Element fstElmnt = (Element) fstNode;
				      NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("sso:domainName");
				      Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
				      NodeList fstNm = fstNmElmnt.getChildNodes();
				      domainName= ((Node) fstNm.item(0)).getNodeValue();
				      
				    }
				}
			    if (directoryServerType.equalsIgnoreCase(WMUMSConstants.ADS_DIRECTORY_SERVICE))
			    {
				    if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
				      //get user base Dn
				      Element fstElmnt = (Element) fstNode;
				      NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("sso:user_base_dn");
				      Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
				      NodeList fstNm = fstNmElmnt.getChildNodes();
				      userBaseDn= ((Node) fstNm.item(0)).getNodeValue();
				     
				      //get userid attribute name
				      NodeList userIdAttributeNameNodeList = fstElmnt.getElementsByTagName("sso:userid_attribute_name");
				      Element userIdElemnet = (Element) userIdAttributeNameNodeList.item(0);
				      NodeList userIdElemnetNodeList = userIdElemnet.getChildNodes();
				      userIdAttributeName= ((Node) userIdElemnetNodeList.item(0)).getNodeValue();
				      
//_log.debug("LOG_SYSTEM_OUT"," base dn="+userBaseDn+"   useridsttributename="+userIdAttributeName,100L);			      
				    }
				}
			}
//		    _log.debug("LOG_SYSTEM_OUT","domain Name: "  + domainName + "  directory server type="+directoryServerType,100L);
			this.domainName = domainName;
			this.directoryServerType = directoryServerType;
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	
	public String getDomainName(){
		return this.domainName;
	}
	public String getDirectoryServerType(){
		return this.directoryServerType;
	}
	public String getUserBaseDn(){
		return this.userBaseDn;
	}
	public String getUserIdAttributeName(){
		return this.userIdAttributeName;
	}
}
