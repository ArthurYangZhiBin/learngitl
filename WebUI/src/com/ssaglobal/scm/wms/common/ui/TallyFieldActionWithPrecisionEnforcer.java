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


package com.ssaglobal.scm.wms.common.ui;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.text.NumberFormat;

import com.epiphany.common.shared.extension.helper.ApplicationUtil;
import com.epiphany.shr.metadata.objects.bio.FieldMappedAttributeType;
import com.epiphany.shr.metadata.objects.bio.RelMappedAttributeType;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.scm.wms.util.LocaleUtil;

/**
 * The TallyFieldAction will compute the sum of a given field over a given
 * biocollection. The result is stored in the text form widget associated with
 * this action instance.
 * 
 * The preRenderWidget event triggers the TallyFieldAction.execute() method.
 * 
 * The parameters expected by the TallyFieldAction.execute() method include the
 * 'collectionForTally' and the 'attributeForTally' parameters. The
 * 'collectionForTally' property value specifies the name of the bio collection
 * attribute (associated with the form). The 'attributeForTally' property
 * specifies the name of the bio attribute involved in the tally by all bios in
 * the bio collection (referenced by 'collectionForTally'.)
 * If no name is specified for the 'collectionForTally' parameter, the extension
 * assumes that the bean associated with the form is the collection to use.
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class TallyFieldActionWithPrecisionEnforcer extends
		com.epiphany.shr.ui.view.customization.FormWidgetExtensionBase {

	private ILoggerCategory logger = ApplicationUtil
			.getLogger(TallyFieldActionWithPrecisionEnforcer.class);
	protected static ILoggerCategory _log = LoggerFactory.getInstance(TallyFieldActionWithPrecisionEnforcer.class);

	/**
	 * This method will compute the sum of the fields associated with all bios
	 * in a given bio collection.
	 * 
	 * This method is driven by two properties specified with the action,
	 * 'attributeForTally' and 'collectionForTally'. The 'collectionForTally'
	 * property value specifies the name of the bio collection attribute
	 * (associated with the form). The 'attributeForTally' property specifies
	 * the name of the bio attribute involved in the tally by all bios in the
	 * bio collection (referenced by 'collectionForTally'.)
	 * 
	 * Without valid values for these properties, an appropriate error message
	 * is logged and the computation fails.
	 * 
	 * If these two properties are valid, the tally is excuted and the result is
	 * rendered in the widget associated with this action.
	 * <P>
	 * 
	 * @param state
	 *            The StateInterface for this extension
	 * @param widget
	 *            The RuntimeFormWidgetInterface for this extension's widget
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 */
	protected int execute(StateInterface state, RuntimeFormWidgetInterface widget) {

		NumberFormat nf = LocaleUtil.getNumberFormat(LocaleUtil.TYPE_QTY, 0, 0); //AW
		try {

			// Fetch the attribute names for the attribute and the bio
			// collection.
			// Fetch and validate the attributeForTally property.
			// Note: The check of 'attrField' to be a valid number is probably
			// done in the sum() method below. This needs testing.
			FieldMappedAttributeType attrField = getAttributeForTally();
			if (attrField == null) {
				return RET_CANCEL;
			}

			BioCollectionBean bioCollBean = getBioCollectionForTally(state);
			if (bioCollBean == null) {
				return RET_CANCEL;
			}

			// Process the tally and report that to the widget.
			Object sum;
			if (bioCollBean.size() != 0)
			{
				 sum = bioCollBean.sum(attrField.getName());
			}
			else
			{
				sum = new Float(0.0);
			}
			Number unformattedValue = nf.parse(sum.toString());
			_log.debug("LOG_SYSTEM_OUT","\n\n!@# Unformatted Value " + unformattedValue,100L);

			String formattedValue = nf.format(unformattedValue);
			_log.debug("LOG_SYSTEM_OUT","\n\n!@# Formatted Value " + formattedValue,100L);
			widget.setDisplayValue(formattedValue);
			widget.setValue(formattedValue);
			widget.setProperty(RuntimeFormWidgetInterface.PROP_READONLY, "true");

		} catch (Exception e) {

			// Handle Exceptions
			e.printStackTrace();
			return RET_CANCEL;

		}

		return RET_CONTINUE;

	}

	private FieldMappedAttributeType getAttributeForTally() {
		Object attrObject = getParameter("attributeForTally");
		if (attrObject == null) {
			logger.error("",
						 "TallyFieldAction: 'attributeForTally' property not specified. Tally action failed.",
						 0);
			return null;
		}
		if (!(attrObject instanceof FieldMappedAttributeType)) {
			logger.error(
						  "",
						  "TallyFieldAction: 'attributeForTally' property value must be a field mapped attribute type. Tally action faield.",
						  0);
			return null;
		}
		FieldMappedAttributeType attrField = (FieldMappedAttributeType) attrObject;
		return attrField;
	}

	private BioCollectionBean getBioCollectionForTally(StateInterface state) {
		Object attrCollObject = getParameter("collectionForTally");
		DataBean focus = state.getFocus();
		if (focus == null) {
			// error! No Bean associated with form. Tally could not be
			// performed.
			return null;
		}

		if (attrCollObject == null) {
			// if no attribute was specified as a reference to the collection,
			// assume that the form associated with
			// this widget is a biocollection. Use this collection to
			// compute the tally.

			if (focus instanceof BioCollectionBean) {
				attrCollObject = focus;
				return (BioCollectionBean) focus;
			} else {
				// error! No Bean associated with form was not a Bio Collection.
				return null;
			}

		} else {
			// if a name is specified for the bio collection, assume
			// that the bio collection to tally is an aggregate
			// of the focus bean. Navigate through the focus bean, using the attribute name
			// specified, to obtain and return the bio collection.
			if (!(attrCollObject instanceof RelMappedAttributeType)) {
				logger.error(
							  "",
							  "TallyFieldAction: 'collectionForTally' property value must be the attribute name of a BIOCollection. Tally action failed.",
							  0);
				return null;
			} else {
				RelMappedAttributeType attribCollection = (RelMappedAttributeType) attrCollObject;
				String collName = attribCollection.getName();
				Object bioCollObject = focus.getValue(collName);

				if (!(bioCollObject instanceof BioCollectionBean)) {
					logger.error(
								  "",
								  "TallyFieldAction: Object specified by 'collectionForTally' property ("
											+ collName
											+ ") is not a BIO Collection. Computation failed.",
								  0);
					return null;
				}
				BioCollectionBean bioCollBean = (BioCollectionBean) bioCollObject;
				return bioCollBean;
			}
		}
	}
}
