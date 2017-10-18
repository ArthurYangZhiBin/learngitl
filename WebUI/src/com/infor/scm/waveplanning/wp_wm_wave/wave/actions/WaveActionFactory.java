package com.infor.scm.waveplanning.wp_wm_wave.wave.actions;
import com.infor.scm.waveplanning.wp_wm_wave.wave.*;

public class WaveActionFactory {
	public static WMWaveActionInterface getWaveAction(String action){
		if(WPConstants.CREATE_WAVE.equalsIgnoreCase(action)){
			return new CreateWave();
		}
		if(WPConstants.CONSOLIDATE_WAVE.equalsIgnoreCase(action)){
			return new ConsolidateWave();
		}
		if(WPConstants.UNCONSOLIDATE_WAVE.equalsIgnoreCase(action)){
			return new UnconsolidateWave();
		}
		if(WPConstants.PREALLOCATE_WAVE.equalsIgnoreCase(action)){
			return new PreallocateWave();
		}
		if(WPConstants.ALLOCATE_WAVE.equalsIgnoreCase(action)){
			return new AllocateWave();
		}
		if(WPConstants.UNALLOCATE_WAVE.equalsIgnoreCase(action)){
			return new UnallocateWave();
		}
		if(WPConstants.RELEASE_WAVE.equalsIgnoreCase(action)){
			return new ReleaseWave();
		}
		if(WPConstants.SHIP_WAVE.equalsIgnoreCase(action)){
			return new ShipWave();
		}
		if(WPConstants.MASS_UPDATE_ALL.equalsIgnoreCase(action)){
			return new MassUpdateAll();
		}
		return null;
	}
}
