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
package com.ssaglobal.scm.wms.shared_app;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.SsaAccessBase;

/**
 * Parses sso_config.xml file and gets the Authentication mechanism info from
 * sso:directory tag.
 * 
 * @author fpuente
 *
 */
public class XMLParser {

	
	protected static ILoggerCategory log = LoggerFactory.getInstance(XMLParser.class);
	
	private String directoryType = null;
	
	private Document document;
	
	
	private void openDoc(Object fileObject) {

    	log.debug("LOG_DEBUG_EXTENSION_XMLPARSER","OpenDoc:Start", 100L);
    	
    	
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
           DocumentBuilder builder = factory.newDocumentBuilder();

           if (fileObject instanceof String)
        	   document = builder.parse( new File((String)fileObject) );
           else if ( fileObject instanceof InputStream)
        	   document = builder.parse((InputStream)fileObject );
        	   

           
           
        } catch (SAXException sxe) {
           // Error generated during parsing)
           Exception  x = sxe;
           if (sxe.getException() != null)
               x = sxe.getException();
           log.error("LOG_DEBUG_EXTENSION_XMLPARSER","OpenDoc:SAXException", x);
           return;
           
        } catch (ParserConfigurationException e) {
            // Parser with specified options can't be built
        	log.error("LOG_DEBUG_EXTENSION_XMLPARSER","OpenDoc:ParserConfigurationException", e);
        	return;
        	
        } catch (IOException e) {
        	// I/O error
        	log.error("LOG_DEBUG_EXTENSION_XMLPARSER","OpenDoc:IOException", e);
        	return;

        }
        
        read();
        
    	log.debug("LOG_DEBUG_EXTENSION_XMLPARSER","OpenDoc:End", 100L);
        
    } 

	private void read(){
		NodeList list = document.getElementsByTagName("sso:directory");
		for(int idx=0; idx < list.getLength(); idx++){
			Node node = list.item(idx);
		
			if (node.getNodeType()==Node.ELEMENT_NODE){
				NamedNodeMap attributes = node.getAttributes();
				Node attr =attributes.getNamedItem("xsi:type");
				directoryType = attr.getNodeValue();
						
			}
		}
		
		
	}

    private String getFilename(){
		String fileSeparator = System.getProperties().getProperty("file.separator");
		SsaAccessBase appAccess = new SQLDPConnectionFactory();
		String oahome = appAccess.getValue("webUIConfig","OAHome");

		String path = null;
		
		if(oahome == null)
			return null;
		else
			path = oahome + fileSeparator + "shared" + fileSeparator + 
				"etc" + fileSeparator ;
	
    	
    	String filename = formatPath(path)+ "sso_config.xml";

    	log.debug("LOG_DEBUG_EXTENSION_XMLPARSER","[CheckAuthMechanism]getFilename:"+filename, 100L);
    	
    	return filename;
    }
    
    public String formatPath(String path){
		String newPath="";

		Pattern pattern = Pattern.compile("\\.");
		
		Matcher matcher = pattern.matcher(path);
		
		int start = 0;
		
		while(matcher.find()){
			String substring = path.substring(start,matcher.start()-1);
			newPath += "\\" + substring;
			start = matcher.end();
		}
		if (start < path.length()-1){
			newPath += path.substring(start);
		}
		
		
		return newPath;
	}

    
	public String getDirectoryType() {

		//String filename = "C:\\infor903wms\\wm\\oa\\shared\\etc\\sso_config.xml";
    	String filename = getFilename();
		openDoc(filename);  
		
		return directoryType;
	}


	
	
}

