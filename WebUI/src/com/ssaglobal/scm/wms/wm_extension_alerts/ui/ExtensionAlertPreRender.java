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

package com.ssaglobal.scm.wms.wm_extension_alerts.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;

import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import com.epiphany.shr.ui.action.UIRenderContext;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.ui.view.RuntimeNormalFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.ssaglobal.SsaAccessBase;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;

/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class ExtensionAlertPreRender extends
		com.epiphany.shr.ui.view.customization.FormExtensionBase {

	private static final String XML_TMP_FOLDER = "xmlTMP";
	private static final String XML = ".xml";
	protected static ILoggerCategory log = LoggerFactory
			.getInstance(ExtensionAlertPreRender.class);

	/**
	 * Called in response to the pre-render event on a form. Write code to
	 * customize the properties of a form. All code that initializes the
	 * properties of a form that is being displayed to a user for the first time
	 * belong here. This is not executed even if the form is re-displayed to the
	 * end user on subsequent actions.
	 * 
	 * @param context
	 *            exposes information about user interface,
	 *            {@link com.epiphany.shr.ui.state.StateInterface state} and
	 *            service
	 * @param form
	 *            the form that is about to be rendered
	 */
	protected int preRenderForm(UIRenderContext context,
			RuntimeNormalFormInterface form) throws EpiException {

		try {
			final String userId = (String) context.getServiceManager()
					.getUserContext().get("logInUserId");
			final String sessionId = context.getState().getRequest()
					.getSession().getId();
			
			//Setup Folder
			File xmlFolder = getXMLDirectory();
			final String filePrefix = userId + "_";
			deleteFilesUsingPrefix(filePrefix, xmlFolder);

			//Create File
			DataBean eventLogFocus = form.getFocus();
			final String fileName = filePrefix + sessionId
					+ System.currentTimeMillis() + XML;
			final String detailedText = BioAttributeUtil.getString(
					eventLogFocus, "DETAILEDTEXT");
			writeXMLFile(fileName, xmlFolder, detailedText);

			//Set IFrame
			RuntimeFormWidgetInterface xmlWidget = form
					.getFormWidgetByName("XML");
			final String fileSeparator = System.getProperties().getProperty(
					"file.separator");
			final String src = XML_TMP_FOLDER + fileSeparator + fileName;
			log.debug("ExtensionAlertPreRender_preRenderForm",
					"Setting src to " + src, SuggestedCategory.APP_EXTENSION);
			xmlWidget.setProperty("src", src);

		} catch (Exception e) {

			// Handle Exceptions
			log.error("ExtensionAlertPreRender_preRenderForm", e.getMessage(),
					SuggestedCategory.APP_EXTENSION);
			log.error("ExtensionAlertPreRender_preRenderForm", e.getMessage(),
					e);
			e.printStackTrace();
			return RET_CANCEL;
		}

		return RET_CONTINUE;
	}

	private void writeXMLFile(final String fileName, File xmlFolder,
			String detailedText) throws IOException {

		File xmlFile = new File(xmlFolder, fileName);
		log.debug("ExtensionAlertPreRender_writeXMLFile", "Writing file "
				+ xmlFile.getAbsolutePath(), SuggestedCategory.APP_EXTENSION);
		BufferedWriter out = new BufferedWriter(new FileWriter(xmlFile));
		out.write(detailedText);
		out.close();
	}

	private void deleteFilesUsingPrefix(final String filePrefix, File xmlFolder) {
		log.debug("ExtensionAlertPreRender_deletePreviousFiles",
				"Deleting previous files", SuggestedCategory.APP_EXTENSION);
		File[] previousFiles = xmlFolder.listFiles(new FilenameFilter() {

			
			public boolean accept(File dir, String name) {
				if (name.startsWith(filePrefix)) {
					return true;
				}
				return false;
			}
		});
		for (File f : previousFiles) {
			log.debug("ExtensionAlertPreRender_deletePreviousFiles",
					"Deleting " + f.getAbsolutePath(),
					SuggestedCategory.APP_EXTENSION);
			f.delete();

		}
	}

	private File getXMLDirectory() {
		String fileSeparator = System.getProperties().getProperty(
				"file.separator");
		SsaAccessBase appAccess = new SQLDPConnectionFactory();
		String oahome = appAccess.getValue("webUIConfig", "OAHome");
		String path = oahome + fileSeparator + "shared" + fileSeparator
				+ "webroot" + fileSeparator + "app" + fileSeparator
				+ XML_TMP_FOLDER;
		log.debug("ExtensionAlertPreRender_getXMLDirectory",
				"Creating XML temp directory " + path,
				SuggestedCategory.APP_EXTENSION);
		File xmlFolder = new File(path);
		xmlFolder.mkdir();
		return xmlFolder;
	}
}
