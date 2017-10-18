package com.infor.scm.waveplanning.wp_wavemgmt_wavemaint.action;
import java.util.ArrayList;

import com.epiphany.shr.ui.action.ActionContext;
import com.epiphany.shr.ui.action.ActionResult;
import com.epiphany.shr.ui.action.ModalActionContext;
import com.epiphany.shr.ui.model.data.BioBean;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.model.data.DataBean;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.exceptions.UserException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.epiphany.shr.util.logging.SuggestedCategory;
import com.infor.scm.waveplanning.common.util.BioAttributeUtil;
import com.infor.scm.waveplanning.common.util.NSQLConfigUtil;
import com.infor.scm.waveplanning.wp_wm_wave.wave.WPConstants;
import com.infor.scm.waveplanning.wp_wm_wave.wave.WaveInputObj;
import com.infor.scm.waveplanning.wp_wm_wave.wave.actions.WMWaveActionInterface;
import com.infor.scm.waveplanning.wp_wm_wave.wave.actions.WaveActionFactory;
import com.ssaglobal.scm.wms.service.baseobjects.EXEDataObject;
import com.ssaglobal.scm.wms.WebUIException.WebuiException;

public class ConsolidateWaveEXE extends com.epiphany.shr.ui.action.ActionExtensionBase{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ConsolidateWaveEXE.class);

	/**
	 * The code within the execute method will be run from a UIAction specified in metadata.
	 * <P>
	 * @param context The ActionContext for this extension
	 * @param result The ActionResult for this extension (contains the focus and perspective for this UI Extension)
	 *
	 * @return int RET_CONTINUE, RET_CANCEL, RET_CANCEL_EXTENSIONS
	 *
	 * @exception EpiException
	 */
	@Override
	protected int execute(ActionContext context, ActionResult result) throws EpiException
	{

		// Replace the following line with your code,
		// returning RET_CONTINUE, RET_CANCEL, or RET_CANCEL_EXTENSIONS
		// as appropriate

		return super.execute(context, result);
	}

	/**
	 * Fires in response to a UI action event, such as when a widget is clicked or
	 * a value entered in a form in a modal dialog
	 * Write code here if u want this to be called when the UI Action event is fired from a modal window
	 * <ul>
	 * <li>{@link com.epiphany.shr.ui.action.ModalActionContext ModalActionContext} exposes information about the
	 * event, including the service and the user interface {@link com.epiphany.shr.ui.state.StateInterface state}.</li>
	 * <li>{@link com.epiphany.shr.ui.action.ActionResult ActionResult} exposes information about the results
	 * of the action that has occurred, and enables your extension to modify them.</li>
	 * </ul>
	 */
	@Override
	protected int execute(ModalActionContext ctx, ActionResult args) throws EpiException
	{

		try
		{
			//Ensure a record is selected
			RuntimeListFormInterface consolidatedList = (RuntimeListFormInterface) ctx.getModalBodyForm(0);
			ArrayList selectedItems = consolidatedList.getSelectedItems();
			WaveInputObj input = null;
			WMWaveActionInterface actionInterface = WaveActionFactory.getWaveAction(WPConstants.CONSOLIDATE_WAVE);
			
			for (int i = 0; i < selectedItems.size(); i++)
			{
				final BioBean row = (BioBean) selectedItems.get(i);
				input = new WaveInputObj();
				input.setStorerKey(row.getString("STORERKEY"));
				input.setSku(row.getString("SKU"));
				input.setOrderType(row.getString("ORDERTYPE"));
				input.setConsolloc(row.getString("CONSOLLOC"));
				input.setMaxlocqty(Double.parseDouble(row.getString("MAXLOCQTY")));
				input.setWavetotal(Double.parseDouble(row.getString("WAVETOTAL")));
				input.setMinwaveqty(Double.parseDouble(row.getString("MINIMUMWAVEQTY")));
				input.setPieceloc(row.getString("PICKPIECE"));
				input.setCaseloc(row.getString("PICKCASE"));
				input.setInclude(row.getString("INCLUDE"));
				input.setLoctype("SPEED-PICK");
				input.setWaveKey(row.getString("WAVEKEY"));
				
				EXEDataObject edo = actionInterface.doWaveAction(input);
				
			}

		} catch (WebuiException e)
		{

			e.printStackTrace();
			String [] ex = new String[1];
			ex[0]=e.getMessage();
			throw new UserException("WPEXE_APP_ERROR", ex);			
		}

		return RET_CONTINUE;
	}
}
