/**
 * ---Begin Copyright Notice---20090105T1353
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2010 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */



package com.ssaglobal.scm.wms.wm_shipmentorder.entry.ui;

// Import 3rd party packages and classes

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.interfaces.LocaleInterface;
import com.epiphany.shr.sf.EpnyServiceManagerFactory;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.model.data.UnitOfWorkBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.view.RuntimeFormInterface;
import com.epiphany.shr.ui.view.RuntimeFormWidgetInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.ssaglobal.scm.wms.uiextensions.UOMDefaultValue;
import com.ssaglobal.scm.wms.util.BioAttributeUtil;
import com.ssaglobal.scm.wms.util.UOMMappingUtil;

// TODO: Auto-generated Javadoc
/**
 * Descriptive Text to describe the extension you should state the event being
 * trapped and list any parameters expected to be provided from the meta
 * <P>
 * .
 * 
 * @return int RET_CONTINUE, RET_CANCEL
 */

public class OrdersEntryItemValidation extends
		com.epiphany.shr.ui.action.ActionExtensionBase {

	/** The ITEM. */
	private static String ITEM = "SKU";
	
	/** The STRATEGY. */
	private static String STRATEGY = "STRATEGYKEY";
	
	/** The OWNER. */
	private static String OWNER = "STORERKEY";
	
	/** The UOM. */
	private static String UOM = "UOM";
	
	/** The ITE m_ description. */
	private static String ITEM_DESCRIPTION = "SKU_DESCRIPTION";
	
	/** The ITE m_ descriptio n_ source. */
	private static String ITEM_DESCRIPTION_SOURCE = "DESCR";
	
	/** The PACK. */
	private static String PACK = "PACKKEY";
	
	/** The TARIFF. */
	private static String TARIFF = "TARIFFKEY";
	
	/** The ITE m_ rotation. */
	private static String ITEM_ROTATION = "SKUROTATION";
	
	/** The ITE m_ rotatio n_ source. */
	private static String ITEM_ROTATION_SOURCE = "ROTATEBY";
	
	/** The ROTATION. */
	private static String ROTATION = "ROTATION";
	
	/** The ROTATIO n_ source. */
	private static String ROTATION_SOURCE = "DEFAULTROTATION";
	
	/** The SHELFLIFE. */
	private static String SHELFLIFE = "SHELFLIFE";
	
	/** The ALLO c_ strat. */
	private static String ALLOC_STRAT = "ALLOCATESTRATEGYKEY";
	
	/** The PREALLO c_ strat. */
	private static String PREALLOC_STRAT = "PREALLOCATESTRATEGYKEY";
	
	/** The ALLO c_ stra t_ type. */
	private static String ALLOC_STRAT_TYPE = "ALLOCATESTRATEGYTYPE";
	
	/** The CONSIGNEEKEY. */
	private static String CONSIGNEEKEY = "CONSIGNEEKEY";
	
	/** The Constant DAPICKSORT. */
	private static final String DAPICKSORT = "DAPICKSORT";
	
	/** The SKU. */
	private static String SKU = "SKU";

	/** The TYPE. */
	private static String TYPE = "TYPE";

	/** The TYP e_ prealloc. */
	private static String TYPE_PREALLOC = "PREAL";
	
	/** The MI n_ shel f_ life. */
	private static String MIN_SHELF_LIFE = "MINIMUMSHELFLIFE";

	// Static error message names
	/** The ERRO r_ invali d_ foreign. */
	private static String ERROR_INVALID_FOREIGN = "WMEXP_SO_ILQ_FOREIGN_INVALID";
	
	/** The ERRO r_ n o_ records. */
	private static String ERROR_NO_RECORDS = "WMEXP_SO_ICAF_NOT_FOUND";

	/** The ITE m_ table. */
	private static String ITEM_TABLE = "sku";
	
	/** The STRATEG y_ table. */
	private static String STRATEGY_TABLE = "wm_strategy";
	
	/** The ALLO c_ stra t_ table. */
	private static String ALLOC_STRAT_TABLE = "wm_allocatestrategy";
	
	/** The CON d_ vali d_ table. */
	private static String COND_VALID_TABLE = "wm_conditionalvalidation";

	/**
	 * Checks if is empty.
	 *
	 * @param attributeValue the attribute value
	 * @return true, if is empty
	 * @throws EpiDataException the epi data exception
	 */
	private static boolean isEmpty(Object attributeValue)
			throws EpiDataException {
		if (attributeValue == null) {
			return true;
		} else if (attributeValue.toString().matches("\\s*")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Colon strip.
	 *
	 * @param label the label
	 * @return the string
	 */
	public String colonStrip(String label) {
		Pattern pattern = Pattern.compile("\\:");
		Matcher matcher = pattern.matcher(label);
		return matcher.replaceAll("");
	}

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked
	 * or a value entered in a form in a modal dialog Write code here if u want
	 * this to be called when the UI Action event is fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext
	 * ModalActionContext} exposes information about the event, including the
	 * service and the user interface
	 *
	 * @param ctx the ctx
	 * @param args the args
	 * @return the int
	 * @throws EpiException the epi exception
	 * {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes
	 * information about the results of the action that has occurred, and
	 * enables your extension to modify them.</li>
	 * </ul>
	 */
	@Override
	protected int execute(ModalActionContext ctx, ActionResult args)
			throws EpiException {

		StateInterface state = ctx.getState();
		RuntimeFormInterface form = state.getCurrentRuntimeForm();
		DataBean focus = form.getFocus();
		UnitOfWorkBean uow = state.getDefaultUnitOfWork();

		BioAttributeUtil.setUppercase(focus, "SKU");
		String item = BioAttributeUtil.getString(focus, "SKU");
		String owner = BioAttributeUtil.getString(focus, "STORERKEY");

		String itemQry = ITEM_TABLE + "." + ITEM + "='" + item + "' AND "
				+ ITEM_TABLE + "." + OWNER + "='" + owner + "'";
		Query itemQuery = new Query(ITEM_TABLE, itemQry, null);
		BioCollectionBean itemBio = uow.getBioCollectionBean(itemQuery);
		if (itemBio.size() < 1) {
			// No records returned for primary key
			UOMDefaultValue.fillDropdown(state, UOM, UOMMappingUtil.PACK_STD);
			throw new UserException(ERROR_INVALID_FOREIGN, new Object[] {
					colonStrip(readLabel(form.getFormWidgetByName("SKU"))),
					owner });
		} else {
			// Should return one record (query on primary key)
			// New record

			focus.setValue(ITEM, item);
			focus.setValue(OWNER, owner);
			focus.setValue(ITEM_DESCRIPTION, isNull(itemBio,
					ITEM_DESCRIPTION_SOURCE));
			focus.setValue(PACK, isNull(itemBio, PACK));
			focus.setValue(TARIFF, isNull(itemBio, TARIFF));
			focus
					.setValue(ITEM_ROTATION, isNull(itemBio,
							ITEM_ROTATION_SOURCE));
			focus.setValue(DAPICKSORT, isNull(itemBio, DAPICKSORT));
			focus.setValue(ROTATION, isNull(itemBio, ROTATION_SOURCE));
			if (OrdersEntryUtil.savingDetailOnly(ctx)) {
				String shelfLife = getShelfLife(OrdersEntryUtil
						.getOrdersHeaderForm(ctx).getFocus(), owner, itemBio,
						uow);
				focus.setValue(SHELFLIFE, shelfLife);
			} else {
				String shelfLife = getShelfLife(OrdersEntryUtil
						.getParentFocus(ctx), owner, itemBio, uow);
				focus.setValue(SHELFLIFE, shelfLife);
			}

			UOMDefaultValue.fillDropdown(state, UOM, isNull(itemBio, PACK));// AW
			String stratQry = STRATEGY_TABLE + "." + STRATEGY + "='"
					+ itemBio.get("0").get(STRATEGY).toString() + "'";
			Query strategyQuery = new Query(STRATEGY_TABLE, stratQry, null);
			BioCollectionBean stratBio = uow
					.getBioCollectionBean(strategyQuery);

			if (stratBio.size() < 1) {
				String parameter = colonStrip(readLabel(form
						.getFormWidgetByName(ALLOC_STRAT)))
						+ ", "
						+ colonStrip(readLabel(form
								.getFormWidgetByName(PREALLOC_STRAT)))
						+ ", "
						+ colonStrip(readLabel(form
								.getFormWidgetByName(ALLOC_STRAT_TYPE)));
				throw new UserException(ERROR_NO_RECORDS,
						new String[] { parameter });
			} else {
				focus.setValue(ALLOC_STRAT, isNull(stratBio, ALLOC_STRAT));
				focus
						.setValue(PREALLOC_STRAT, isNull(stratBio,
								PREALLOC_STRAT));
				// Query allocate strategy table for data points
				String allocStratQry = ALLOC_STRAT_TABLE + "." + ALLOC_STRAT
						+ "='" + stratBio.get("0").get(ALLOC_STRAT).toString()
						+ "'";
				Query allocateStrategyQuery = new Query(ALLOC_STRAT_TABLE,
						allocStratQry, null);
				BioCollectionBean allocStratBio = uow
						.getBioCollectionBean(allocateStrategyQuery);
				if (allocStratBio.size() < 1) {
					String parameter = colonStrip(readLabel(form
							.getFormWidgetByName(ALLOC_STRAT_TYPE)));
					throw new UserException(ERROR_NO_RECORDS, parameter);
				} else {
					focus.setValue(ALLOC_STRAT_TYPE, isNull(allocStratBio,
							ALLOC_STRAT_TYPE));
				}
			}
			
			
			
			form.getFormWidgetByName("LOTTABLE01").setLabel("label",isNull(itemBio,"LOTTABLE01LABEL")+":");
			form.getFormWidgetByName("LOTTABLE02").setLabel("label",isNull(itemBio,"LOTTABLE02LABEL")+":");
			form.getFormWidgetByName("LOTTABLE03").setLabel("label",isNull(itemBio,"LOTTABLE03LABEL")+":");
			form.getFormWidgetByName("LOTTABLE04").setLabel("label",isNull(itemBio,"LOTTABLE04LABEL")+":");
			form.getFormWidgetByName("LOTTABLE05").setLabel("label",isNull(itemBio,"LOTTABLE05LABEL")+":");
			form.getFormWidgetByName("LOTTABLE06").setLabel("label",isNull(itemBio,"LOTTABLE06LABEL")+":");
			form.getFormWidgetByName("LOTTABLE07").setLabel("label",isNull(itemBio,"LOTTABLE07LABEL")+":");
			form.getFormWidgetByName("LOTTABLE08").setLabel("label",isNull(itemBio,"LOTTABLE08LABEL")+":");
			form.getFormWidgetByName("LOTTABLE09").setLabel("label",isNull(itemBio,"LOTTABLE09LABEL")+":");
			form.getFormWidgetByName("LOTTABLE10").setLabel("label",isNull(itemBio,"LOTTABLE10LABEL")+":");
			form.getFormWidgetByName("LOTTABLE11").setLabel("label",isNull(itemBio,"LOTTABLE11LABEL")+":");
			form.getFormWidgetByName("LOTTABLE12").setLabel("label",isNull(itemBio,"LOTTABLE12LABEL")+":");
		}

		return RET_CONTINUE;
	}

	/**
	 * Gets the shelf life.
	 *
	 * @param headerFocus the header focus
	 * @param owner the owner
	 * @param itemBio the item bio
	 * @param uowb the uowb
	 * @return the shelf life
	 * @throws EpiException the epi exception
	 */
	public String getShelfLife(DataBean headerFocus, String owner,
			BioCollectionBean itemBio, UnitOfWorkBean uowb) throws EpiException {
		// TODO Auto-generated method stub
		String item = null;
		Object customer = null;
		String itemShelfLife = null;
		String condValQry = null;
		String shelfLife = null;
		Query condValQuery = null;
		Query subQuery = null;
		BioCollectionBean condValBio = null;
		BioCollectionBean condValSubBio = null;

		customer = headerFocus.getValue(CONSIGNEEKEY);
		item = isNull(itemBio, SKU);
		itemShelfLife = isNull(itemBio, SHELFLIFE);
		shelfLife = itemShelfLife;

		if (!isEmpty(customer)) {
			condValQry = COND_VALID_TABLE + "." + CONSIGNEEKEY + "='"
					+ customer.toString() + "'" + " AND " + COND_VALID_TABLE
					+ "." + TYPE + "='" + TYPE_PREALLOC + "'";

			condValQuery = new Query(COND_VALID_TABLE, condValQry, null);
			condValBio = uowb.getBioCollectionBean(condValQuery);

			if (condValBio.size() >= 1) {
				// check storer & sku
				if (!isEmpty(owner) && !isEmpty(item)) {
					subQuery = new Query(COND_VALID_TABLE, COND_VALID_TABLE
							+ "." + ITEM + "='" + item + "' AND "
							+ COND_VALID_TABLE + "." + OWNER + "='" + owner
							+ "'", null);
					condValSubBio = condValBio.filterBean(subQuery);

					if (condValSubBio.size() == 0) {
						subQuery = new Query(COND_VALID_TABLE, COND_VALID_TABLE
								+ "." + ITEM + "='" + item + "' AND "
								+ COND_VALID_TABLE + "." + OWNER + "=' '", null);
						condValSubBio = condValBio.filterBean(subQuery);

						if (condValSubBio.size() == 0) {
							subQuery = new Query(COND_VALID_TABLE,
									COND_VALID_TABLE + "." + ITEM + "=' ' AND "
											+ COND_VALID_TABLE + "." + OWNER
											+ "=' '", null);
							condValSubBio = condValBio.filterBean(subQuery);

							if (condValSubBio.size() == 1) {
								shelfLife = isNull(condValSubBio,
										MIN_SHELF_LIFE);
							}

						} else {
							shelfLife = isNull(condValSubBio, MIN_SHELF_LIFE);
						}

					} else {
						shelfLife = isNull(condValSubBio, MIN_SHELF_LIFE);
					}
				}

			} else {
				// sku shelflife
			}

		}
		return shelfLife;
	}

	/**
	 * Checks if is null.
	 *
	 * @param focus the focus
	 * @param widgetName the widget name
	 * @return the string
	 * @throws EpiException the epi exception
	 */
	public String isNull(BioCollectionBean focus, String widgetName)
			throws EpiException {
		String result = null;
		if (result != focus.get("0").get(widgetName)) {
			result = focus.get("0").get(widgetName).toString();
		}
		return result;
	}

	/**
	 * Read label.
	 *
	 * @param widget the widget
	 * @return the string
	 */
	public String readLabel(RuntimeFormWidgetInterface widget) {
		String userLocale = EpnyServiceManagerFactory.getInstance()
				.getUserContext().getLocale(true);
		MetaDataAccess mda = MetaDataAccess.getInstance();
		LocaleInterface locale = mda.getLocale(userLocale);
		return widget.getLabel("label", locale);
	}

}
