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

package com.ssaglobal.scm.wms.wm_asnreceipts.bio;

// Import 3rd party packages and classes

// Import Epiphany packages and classes
import java.util.List;

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioRef;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.sf.EpnyServiceContext;
import com.epiphany.shr.util.exceptions.EpiException;
import com.ssaglobal.scm.wms.util.BioUtil;
import com.ssaglobal.scm.wms.util.StringUtils;

/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class CheckNull extends com.epiphany.shr.data.bio.extensions.BioExtensionBase {

	/**
	 * Returning CANCEL from this will stop the update Creating new bios or
	 * changing bios in the Bio's unit of work will cause them to be added to
	 * the Bios to be processed in this UnitOfWork.Insert();
	 * <P>
	 * 
	 * @param context
	 *            The EpnyServiceContext for this BioExtension instance
	 * @param bio
	 *            The Bio for this BioExtension instance
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
	 * @exception EpiException
	 */
	protected int bioBeforeUpdate(EpnyServiceContext context, Bio bio) throws EpiException {

		List<String> attributes = (List<String>) getParameter("ATTRIBUTES");
		for (String attr : attributes) {
			checkNull(bio, attr);
		}

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		// Leave the line if the code is not going to handle
		// bioBeforeUpdate events

		return super.bioBeforeUpdate(context, bio);
	}

	private void checkNull(Bio bio, String attr) throws EpiDataException {
		String value = BioUtil.getString(bio, attr);
		if (StringUtils.isEmpty(value)) {
			bio.set(attr, " ");
		} else {
			bio.set(attr, value.trim());
		}

	}

	/**
	 * Returning CANCEL from this will stop the operation Cannot make any
	 * updates to the unit of work (create new bios/update existing bios) during
	 * this extension
	 * <P>
	 * 
	 * @param context
	 *            The EpnyServiceContext for this BioExtension instance
	 * @param bio
	 *            The Bio for this BioExtension instance
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
	 * @exception EpiException
	 */
	protected int bioFinalBeforeUpdate(EpnyServiceContext context, Bio bio) throws EpiException {

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		// Leave the line if the code is not going to handle
		// bioBeforeUpdate events

		return super.bioFinalBeforeUpdate(context, bio);
	}

	/**
	 * Returning CANCEL from this will stop the insert Creating new bios or
	 * changing bios in the Bio's unit of work will cause them to be added to
	 * the Bios to be processed in this UnitOfWork.save();
	 * <P>
	 * 
	 * @param context
	 *            The EpnyServiceContext for this BioExtension instance
	 * @param bio
	 *            The Bio for this BioExtension instance
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
	 * @exception EpiException
	 */
	protected int bioBeforeInsert(EpnyServiceContext context, Bio bio) throws EpiException {

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		// Leave the line if the code is not going to handle
		// bioBeforeInsert events

		return super.bioBeforeInsert(context, bio);
	}

	/**
	 * Returning CANCEL from this will stop the operation Cannot make any
	 * updates to the unit of work (create new bios/update existing bios) during
	 * this extension
	 * <P>
	 * 
	 * @param context
	 *            The EpnyServiceContext for this BioExtension instance
	 * @param bio
	 *            The Bio for this BioExtension instance
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
	 * @exception EpiException
	 */
	protected int bioFinalBeforeInsert(EpnyServiceContext context, Bio bio) throws EpiException {

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		// Leave the line if the code is not going to handle
		// bioBeforeInsert events

		return super.bioFinalBeforeInsert(context, bio);
	}

	/**
	 * Returning CANCEL from this will stop the delete Creating new bios or
	 * changing bios in the Bio's unit of work will cause them to be added to
	 * the Bios to be processed in this UnitOfWork.save();
	 * <P>
	 * 
	 * @param context
	 *            The EpnyServiceContext for this BioExtension instance
	 * @param bio
	 *            The Bio for this BioExtension instance
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
	 * @exception EpiException
	 */
	protected int bioBeforeDelete(EpnyServiceContext context, Bio bio) throws EpiException {

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		// Leave the line if the code is not going to handle
		// bioBeforeDelete events

		return super.bioBeforeDelete(context, bio);
	}

	/**
	 * Returning CANCEL from this will stop the operation Cannot make any
	 * updates to the unit of work (create new bios/update existing bios) during
	 * this extension
	 * <P>
	 * 
	 * @param context
	 *            The EpnyServiceContext for this BioExtension instance
	 * @param bio
	 *            The Bio for this BioExtension instance
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
	 * @exception EpiException
	 */
	protected int bioFinalBeforeDelete(EpnyServiceContext context, Bio bio) throws EpiException {

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		// Leave the line if the code is not going to handle
		// bioBeforeDelete events

		return super.bioFinalBeforeDelete(context, bio);
	}

	/**
	 * Returning CANCEL from these will NOT stop the update It is called after
	 * the transaction is committed To do any saving, the client must be called
	 * on a new UnitOfWork.
	 * <P>
	 * 
	 * @param context
	 *            The EpnyServiceContext for this BioExtension instance
	 * @param bioChanged
	 *            The BioRef for this BioExtension instance
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
	 * @exception EpiException
	 */
	protected int bioAfterUpdate(EpnyServiceContext context, BioRef bioChanged) throws EpiException {

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		// Leave the line if the code is not going to handle
		// bioAfterUpdate events

		return super.bioAfterUpdate(context, bioChanged);
	}

	/**
	 * Returning CANCEL from these will NOT stop the insert It is called after
	 * the transaction is committed To do any saving, the client must be called
	 * on a new UnitOfWork.
	 * <P>
	 * 
	 * @param context
	 *            The EpnyServiceContext for this BioExtension instance
	 * @param bioInserted
	 *            The BioRef for this BioExtension instance
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
	 * @exception EpiException
	 */
	protected int bioAfterInsert(EpnyServiceContext context, BioRef bioInserted)
			throws EpiException {

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		// Leave the line if the code is not going to handle
		// bioAfterInsert events

		return super.bioAfterInsert(context, bioInserted);
	}

	/**
	 * Returning CANCEL from these will NOT stop the delete It is called after
	 * the transaction is committed To do any saving, the client must be called
	 * on a new UnitOfWork.
	 * <P>
	 * 
	 * @param context
	 *            The EpnyServiceContext for this BioExtension instance
	 * 
	 * @return int RET_CONTINUE, RET_CANCEL
	 * 
	 * @exception EpiException
	 */
	protected int bioAfterDelete(EpnyServiceContext context) throws EpiException {

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate
		// Leave the line if the code is not going to handle
		// bioAfterDelete events

		return super.bioAfterDelete(context);
	}
}
