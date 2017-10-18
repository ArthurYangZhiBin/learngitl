package com.ssaglobal.scm.wms.wm_appointment;

import com.epiphany.shr.data.dp.exception.DPException;
import com.ssaglobal.scm.wms.datalayer.WmsWebuiValidationInsertImpl;

public class ITRNTrailerStatus {

	/******************************************************************
	 * Programmer:     Sreedhar Kethireddy
	 * Created:        10/21/2010
	 * Purpose:        To insert updated trailer status into trailerstatus table
	 *******************************************************************
	 * Modification History
	 *********************************************************************************/
	public void trailerStatusInsert(String trailerKey,String trailer,String trailerType,String trailerStatus,String userid)
	{
		String insertQuery = "INSERT INTO TRAILERSTATUS (TRAILERKEY,TRAILER,TRAILERTYPE,TRAILERSTATUS,ACTIVITYUSER) VALUES " +
		"('" +trailerKey+ "', '" + trailer + "','"+trailerType+"', '"+trailerStatus+"','" + userid+ "')";

		try {
			WmsWebuiValidationInsertImpl.insert(insertQuery);
		} catch (DPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
