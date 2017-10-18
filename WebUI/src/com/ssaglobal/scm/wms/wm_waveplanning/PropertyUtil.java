package com.ssaglobal.scm.wms.wm_waveplanning;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class PropertyUtil {
	
	private static final Log log = LogFactory.getLog(PropertyUtil.class);

	public static String readValue(String filePath, String key) {
		Properties props = new Properties();
		try {
			InputStream ips = new BufferedInputStream(new FileInputStream(filePath));
			props.load(ips);
			String value = props.getProperty(key);
			log.info(key + "=" + value);
			return value;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static List readProperties(String filePath) {
		List list = new ArrayList();
		Properties props = new Properties();
		try {
			InputStream ips = new BufferedInputStream(new FileInputStream(filePath));
			props.load(ips);
			Enumeration enum1 = props.propertyNames();
			while (enum1.hasMoreElements()) {
				List listDetail = new ArrayList();
				String key = (String) enum1.nextElement();
				String value = props.getProperty(key);
				
				listDetail.add(key);
				listDetail.add(value);
				list.add(listDetail);
				System.out.println(key + "=" + value);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static void writeProperties(String filePath, String paraKey,
			String paraValue) {
		Properties props = new Properties();
		try {
			OutputStream ops = new FileOutputStream(filePath);
			props.setProperty(paraKey, paraValue);
			props.store(ops, "set");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
