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
package com.ssaglobal.scm.wms.datalayer;

import java.math.BigInteger;

/**
 * @author Harish Chandiramani
 * @version 1.0.0
 */
public class PowerLockScrambler {

	private static String _ScrambleString   = "~{[}u;Ce83KX%:VIm!|gs]_aL-QEOpx<UlzZjBq6#1($\\\"FS5H0'cM&>Po.NGA*Jr)Y Dv/t9kd?^fni,hR2Wy=`+4T@7wb";
	private static String _DescrambleString = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";

	/**
	 * Constructor for PowerLockScrambler
	 */
	public PowerLockScrambler() {
		super();
	}

	/**
	 * Descrambles a password using the PowerLock algorhythm.
	 * 
	 * @param pScrambledString The scrambled password to be descrambled
	 * @return The descrambled value of the string passed in
	 */
	public static String descramble(String pScrambledString) {
		String l_DecryptPass;
		String l_Work;
		int l_Position;
		int l_Length;
		int l_Multiplier;
		int l_Offset;

		//-------------------------------------------------------------------
		//	"powerlock" is the default password for logging in to the
		// security administration program and "changeme" is the default for
		// application users, so they do not need to be decrypted. 
		//-------------------------------------------------------------------

		if (pScrambledString.equals("powerlock") == false && pScrambledString.equals("changeme") == false) {

			l_Length = pScrambledString.length();

			if (1 <= l_Length && l_Length <= 3) {
				l_Multiplier = 1;
			} else if (4 <= l_Length && l_Length <= 6) {
				l_Multiplier = 2;
			} else if (7 <= l_Length && l_Length <= 9) {
				l_Multiplier = 3;
			} else {
				l_Multiplier = 4;
			}

			l_DecryptPass= "";

			for (int l_Count = 1; l_Count <= l_Length; l_Count++) {
				l_Offset = l_Count * l_Multiplier;
				l_Work = pScrambledString.substring(l_Count - 1, l_Count);
				
				l_Position = 0;
				for (int counter = 1; counter <= _ScrambleString.length(); counter++) {
					if (l_Work.equals(_ScrambleString.substring(counter - 1, counter))) {
						l_Position = counter;
						break;
					}
				}

				l_Position -= l_Offset;
				l_Position -= 2;
				
				while (l_Position < 0) {
					l_Position += 95;
				}
				
				l_Work = _DescrambleString.substring(l_Position, l_Position + 1);
				l_DecryptPass += l_Work;
				if (1 <= l_Multiplier && l_Multiplier <= 3) {
					l_Multiplier ++;
				} else {
					l_Multiplier = 1;
				}
			}

		} else {
		   return pScrambledString;
		}
		
		return l_DecryptPass;
	}


	/**
	 * Scrambles a password using the PowerLock algorhythm.
	 * 
	 * @param pPlainTextString The password to be scrambled
	 * @return The scrambled value of the string passed in
	 */
	public static String scramble(String pPlainTextString) {

		String l_EncryptPass;
		String l_Work;
		int l_Position;
		int l_Length;
		int l_Multiplier;
		int l_Offset;

		//-------------------------------------------------------------------
		//	"powerlock" is the default password for logging in to the
		// security administration program and "changeme" is the default for
		// application users, so they do not need to be encrypted. 
		//-------------------------------------------------------------------

		if (pPlainTextString.equals("powerlock") == false && pPlainTextString.equals("changeme") == false) {

			l_Length = pPlainTextString.length();

			if (1 <= l_Length && l_Length <= 3) {
				l_Multiplier = 1;
			} else if (4 <= l_Length && l_Length <= 6) {
				l_Multiplier = 2;
			} else if (7 <= l_Length && l_Length <= 9) {
				l_Multiplier = 3;
			} else {
				l_Multiplier = 4;
			}

			l_EncryptPass = "";

			for (int l_Count = 1; l_Count <= l_Length; l_Count++) {
				l_Offset = l_Count * l_Multiplier;
				l_Work = pPlainTextString.substring(l_Count - 1, l_Count);

				l_Position = 0;
				for (int counter = 1; counter <= _DescrambleString.length(); counter++) {
					if (l_Work.equals(_DescrambleString.substring(counter - 1, counter))) {
						l_Position = counter;
						break;
					}
				}

				
				l_Position += l_Offset;
				BigInteger modNum = new BigInteger(String.valueOf(l_Position));
				BigInteger modVal = new BigInteger(String.valueOf("95"));
				l_Position = modNum.mod(modVal).intValue();
		
				/* 05/19/00 MJS          Bug Fix # CAGA-4KFVZ4 */
				/* Mod l_Position by 95 again to get the 0 based index */
				modNum = new BigInteger(String.valueOf(l_Position));
				l_Position = modNum.mod(modVal).intValue();
		
				l_Work = _ScrambleString.substring(l_Position, l_Position + 1);
				l_EncryptPass += l_Work;
				if (1 <= l_Multiplier && l_Multiplier <= 3) {
					l_Multiplier = l_Multiplier + 1;
				} else {
					l_Multiplier = 1;
				}
			}

		} else {
		   return pPlainTextString;
		}

		return l_EncryptPass;
	}
}

