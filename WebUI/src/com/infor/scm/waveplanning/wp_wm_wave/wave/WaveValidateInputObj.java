package com.infor.scm.waveplanning.wp_wm_wave.wave;
import com.epiphany.shr.ui.view.RuntimeListFormInterface;
import com.epiphany.shr.ui.action.ActionContext;
public class WaveValidateInputObj {
	private int orderSize;
	private RuntimeListFormInterface form;
	
	private String waveKey;
	private ActionContext context;
	
	private String action;
	

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public int getOrderSize() {
		return orderSize;
	}

	public RuntimeListFormInterface getForm() {
		return form;
	}

	public void setForm(RuntimeListFormInterface form) {
		this.form = form;
	}

	public void setOrderSize(int orderSize) {
		this.orderSize = orderSize;
	}

	public String getWaveKey() {
		return waveKey;
	}

	public void setWaveKey(String waveKey) {
		this.waveKey = waveKey;
	}

	public ActionContext getContext() {
		return context;
	}

	public void setContext(ActionContext context) {
		this.context = context;
	}
}
