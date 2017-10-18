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
package com.infor.scm.waveplanning.wp_wm_wave.wave.actions;
import com.infor.scm.waveplanning.wp_wm_wave.wave.*;

import java.util.concurrent.Callable;
import java.util.concurrent.*;

/**
 * TODO Document RunningInBackgroundWrapper class.
 *
 * @author <a
 *         href="http://wiki.infor.com/confluence/display/InforArchitecture/Infor+IDE">
 *         Infor IDE Team</a>
 */
public class RunningInBackgroundWrapper {
	public static void execute(WaveInputObj waveInput, String action){
		Callable callable = null;;
		if(WPConstants.ALLOCATE_WAVE.equalsIgnoreCase(action)){
			AllocateWaveCallable allocateWaveCallable = new AllocateWaveCallable();
			allocateWaveCallable.setWaveInput(waveInput);
			callable = allocateWaveCallable;
			
		}
		if(WPConstants.RELEASE_WAVE.equalsIgnoreCase(action)){
			ReleaseWaveCallable releaseWaveCallable = new ReleaseWaveCallable();
			releaseWaveCallable.setWaveInput(waveInput);
			callable = releaseWaveCallable;
		}
		if(WPConstants.SHIP_WAVE.equalsIgnoreCase(action)){
			ShipWaveCallable shipWaveCallable = new ShipWaveCallable();
			shipWaveCallable.setWaveInput(waveInput);
			callable = shipWaveCallable;
		}
		FutureTask<Callable> task = new FutureTask(callable);
		ExecutorService es = Executors.newSingleThreadExecutor ();
		es.submit (task);

	}


}
