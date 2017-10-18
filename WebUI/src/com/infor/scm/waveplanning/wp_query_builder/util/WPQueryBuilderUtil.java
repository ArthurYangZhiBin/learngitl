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
package com.infor.scm.waveplanning.wp_query_builder.util;

//Import 3rd party packages and classes
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.TimeZone;

//Import Epiphany packages and classes
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.Query;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.model.UserBean;
import com.epiphany.shr.ui.model.UserInterface;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.state.StateInterface;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.infor.scm.waveplanning.common.WavePlanningConstants;
import com.infor.scm.waveplanning.common.WavePlanningUtils;

//import com.ssaglobal.enterpriseScheduler.implementations.helper.ESUtilityFunctionsImpl;
//import com.ssaglobal.enterpriseScheduler.interfaces.helper.ESDataInterface;
//import com.ssaglobal.enterpriseScheduler.interfaces.helper.ESServiceLocatorInterface;



public class WPQueryBuilderUtil
{
	protected static ILoggerCategory _log = LoggerFactory.getInstance(WPQueryBuilderUtil.class);
	public WPQueryBuilderUtil()
    {
    }
	
	public static ArrayList getWhereClause(BioCollection criteria,  QueryBuilderInputObj input) throws Exception{
 		// Lines Added by Rajesh Thangavelu On 12.5.2006
 		// Logs message for entering 'getWhereClause' Function...	
		if(criteria == null || criteria.size() == 0){
			ArrayList returnValue = new ArrayList();
			returnValue.add(new ArrayList());
			returnValue.add("");
			returnValue.add("");
			return returnValue;
		}
		
		String wclause = "";
		String colNames[]= new String[criteria.size()];
		String operators[]=new String[criteria.size()];
		String val1[]=new String[criteria.size()];
		String val2[]=new String[criteria.size()];
		String cond[]=new String[criteria.size()];
		HashMap<String, String>  columnsMap = QueryBuilderFilterOrderColumnsSingleton.getQueryBuilderFilterOrderColumnsSingleton(input.getCtx()).getColumnsMap();
		for(int i = 0; i < criteria.size(); i++){
			Bio bio = criteria.elementAt(i);
//			colNames[i]=getColFromOrderField((String) bio.get("ORDERFIELD"));
			colNames[i]=columnsMap.get((String) bio.get("ORDERFIELD"));
			String operator = (String) bio.get("OPERATOR");
			if("NOTBETWEEN".equals(operator))
			{
				operator = "NOT BETWEEN";
			}
			else if("ISNOTNULL".equals(operator))
			{
				operator = "IS NOT NULL";	
			}
			operators[i]=operator;
			val1[i]=(String) bio.get("FIRSTVALUE");
			val2[i]=(String) bio.get("SECONDVALUE");
			cond[i]=(String) bio.get("ANDOR"); 
		}
		ArrayList tablesAndWhere = new ArrayList();
		ArrayList tables = new ArrayList();
		StringTokenizer table = null;
		String tab = null;
		//28/01/06
		String unparsedWclause = "";
		String value1 = "";
		String value2 = "";
		//28/01/06
		if (colNames != null) {
			if (!(colNames[0].equalsIgnoreCase("")))
			{
				StringTokenizer stk=null;
				String col=null;
				String typ=null;

				ArrayList likeList=new ArrayList();
				likeList.add("LIKE");
				likeList.add("NOT LIKE");

				ArrayList nullList=new ArrayList();
				nullList.add("IS NULL");
				nullList.add("IS NOT NULL");

				ArrayList betwList=new ArrayList();
				betwList.add("BETWEEN");
				betwList.add("NOT BETWEEN");
				
				ArrayList inList=new ArrayList();
				inList.add("IN");
				inList.add("NOT IN");
				
				boolean isTodayEqual = false;
				
				for(int i=0;i<colNames.length;i++)
				{
					stk=null;
					stk=new StringTokenizer(colNames[i],"&");
					col=stk.nextToken();
					table = new StringTokenizer(col, ".");
					tab = table.nextToken();
					if (!(tables.contains(tab)))
						tables.add(tab);
					typ=(stk.nextToken()).trim();

					value1 = val1[i];
					if (col.equalsIgnoreCase("orders.orderdate") ||
						col.equalsIgnoreCase("orders.requestedshipdate") ||
						col.equalsIgnoreCase("orders.deliverydate") ||
						col.equalsIgnoreCase("orders.deliverydate2")) {
							if("=".equalsIgnoreCase(operators[i])
									&& value1.startsWith("today")){
									isTodayEqual = true;
									operators[i]="BETWEEN";
	//							value1 = addOneDay(value1);
								
							}
							value1 = getDateValue(value1, input);
						
					}				

					wclause=wclause+" "+col+" "+operators[i];
					//28/01/06
					unparsedWclause=unparsedWclause+" "+col+" "+operators[i];
					//28/01/06
					if(likeList.contains(operators[i].trim()))
					{
						if(typ.equalsIgnoreCase("string")) {
							unparsedWclause=unparsedWclause+" \\'"+value1+"\\' ";
							value1 = value1.replaceAll("'", "''");
							wclause=wclause+" \\'"+value1+"\\' ";
						} else {
							wclause=wclause+" "+value1+" ";
							unparsedWclause=unparsedWclause+" "+value1+" ";
						}
					}
					else if(nullList.contains(operators[i].trim()))
					{
						wclause=wclause+" ";
						unparsedWclause=unparsedWclause+" ";
					}
					else if(betwList.contains(operators[i].trim()))
					{
						value2 = val2[i];
						if (col.equalsIgnoreCase("orders.orderdate") ||
								col.equalsIgnoreCase("orders.requestedshipdate") ||
								col.equalsIgnoreCase("orders.deliverydate") ||
								col.equalsIgnoreCase("orders.deliverydate2")) {
								if(isTodayEqual){
									value2 = addOneDay(val1[i]);
								}
								value2 = getDateValue(value2, input);
						}
						if(typ.equalsIgnoreCase("string")) {
							unparsedWclause=unparsedWclause+" \\'"+value1+"\\' And \\'"+value2+"\\' ";
							value1 = value1.replaceAll("'", "''");
							value2 = value2.replaceAll("'", "''");
							wclause=wclause+" \\'"+value1+"\\' And \\'"+value2+"\\' ";
						} else {
							wclause=wclause+" "+value1+" And "+value2+" ";
							unparsedWclause=unparsedWclause+" "+value1+" And "+value2+" ";
						}
					}
					else if(inList.contains(operators[i].trim()))
					{
						if(typ.equalsIgnoreCase("string")) {
							String fval = "";
							String fval1 = "";
							String parsedValue = "";
							if (!(value1.trim().equals(""))) { 
								String [] spValue = value1.split(",");
								for (int s=0;s<spValue.length;s++){
									parsedValue = spValue[s].trim();
//									fval1 = fval1 + parsedValue + "\\',\\'";
									if(s < spValue.length-1){
										fval1 = fval1 + parsedValue + "\\',\\'";
									}else{
										fval1 = fval1 + parsedValue + "\\'";
									}

									parsedValue = parsedValue.replaceAll("'", "''");
									if(s < spValue.length-1){
										fval = fval + parsedValue + "\\',\\'";
									}else{
										fval = fval + parsedValue + "\\'";
									}
								}
								if (value1.charAt(value1.length()-1) == ',') {
									fval = fval + "\\',\\'";
									fval1 = fval1 + "\\',\\'";
								}
//								fval1 = fval1.substring(0, fval.length()-3);
//								fval = fval.substring(0, fval.length()-3);
								fval1 = fval1.substring(0, fval.length()-2);
								fval = fval.substring(0, fval.length()-2);
							}
							unparsedWclause=unparsedWclause+" (\\'"+fval1+"\\') ";
							wclause=wclause+" (\\'"+fval+"\\') ";
						}
						else {
							wclause=wclause+" ("+value1+") ";
							unparsedWclause=unparsedWclause+" ("+value1+") ";
						}
					}
					else
					{
						if(typ.equalsIgnoreCase("string")) {
							if (value1.indexOf('.') > 0) {
								unparsedWclause=unparsedWclause+" "+value1+" ";
								value1 = value1.replaceAll("'", "''");
								wclause=wclause+" "+value1+" ";
							} else {
								unparsedWclause=unparsedWclause+" '"+value1+"' ";
								value1 = value1.replaceAll("'", "''");
								wclause=wclause+" \\'"+value1+"\\' ";
							}
						} else {
							wclause=wclause+" "+value1+" ";
							unparsedWclause=unparsedWclause+" "+value1+" ";
						}
					}
					if(i!=(colNames.length-1)) {
						unparsedWclause=unparsedWclause+cond[i]+"  ";
						wclause=wclause+cond[i]+"  ";
					}
				}
			}
		}
		 
		tablesAndWhere.add(tables);
		tablesAndWhere.add(wclause);
		tablesAndWhere.add(unparsedWclause);

 		// Lines Added by Rajesh Thangavelu On 12.5.2006
 		// Logs message for exiting 'getWhereClause' Function... 		

		return tablesAndWhere;
	}

	
	public static String addOneDay(String day){
		if(day.startsWith("today")){
			int indexOfPlus = day.indexOf("+"); 
			int indexOfMinus = day.indexOf("-");
			int index = -1;
			int oneMoreDay = 0;
			if(indexOfPlus != -1){
				index = indexOfPlus;
			}else if(indexOfMinus != -1){
				index = indexOfMinus;
			}
			System.out.println("index="+index);
			System.out.println("length="+day.length());
			day = day.trim();
			if(index == -1){
				int ind = day.indexOf(":");
				if(ind == -1){
					day = day+"+1";
				}else{
					String [] dayDetail = day.split(" ");
					day=dayDetail[0]+"+1 "+dayDetail[1];
				}
			}else{
					int ind = day.indexOf(":");
					if(ind == -1){
						String numberOfDays = day.substring(index+1, day.length());
						if(indexOfPlus != -1){
							oneMoreDay = Integer.parseInt(numberOfDays)+1;
						}else if(indexOfMinus != -1){
							oneMoreDay = Integer.parseInt(numberOfDays)-1;
						}
						
						if(indexOfPlus != -1){
							day = day.substring(0, index)+"+"+oneMoreDay;
						}else if(indexOfMinus != -1){
							day = day.substring(0, index)+"-"+oneMoreDay;
						}
					}else{
						int spaceIndex = day.lastIndexOf(" ");
						String dayPart = day.substring(0, spaceIndex);
						String timePart = day.substring(spaceIndex+1, day.length());
						String numberOfDays = dayPart.substring(index+1, dayPart.length());
						if(indexOfPlus != -1){
							oneMoreDay = Integer.parseInt(numberOfDays)+1;
						}else if(indexOfMinus != -1){
							oneMoreDay = Integer.parseInt(numberOfDays)-1;
						}
						if(indexOfPlus != -1){
							dayPart = dayPart.substring(0, index)+"+"+oneMoreDay;
						}else if(indexOfMinus != -1){
							dayPart = dayPart.substring(0, index)+"-"+oneMoreDay;
						}
						day = dayPart +" "+timePart;							
					}
				}
			
		}
		return day;
	}
	
	 public static HashMap<String, String> convertDate(String day, QueryBuilderInputObj input){
		 	HashMap<String, String> hashMap = new HashMap<String, String>();
		 	Calendar currentDate = null;
			String strDay = day.trim();
			boolean hasTime = false;
			if(strDay.startsWith("today")){
				if(QueryBuilderConstants.RETRIEVE_QUERY_CALL_TYPE.equalsIgnoreCase(input.getCallType())){
					currentDate = Calendar.getInstance(input.getUserTimeZone());
					String date = "";
					String time = "00:00:00";
					int indexofcomma = strDay.indexOf(":");
					if(indexofcomma != -1){
						date = strDay.substring(0, strDay.lastIndexOf(" ")).replaceAll(" ", "");
						time = strDay.substring(strDay.lastIndexOf(" ")).trim();
						hasTime = true;
					}else{
						date = strDay.replaceAll(" ", "");
					}
					
	//				System.out.println("****date="+date+" Time="+time+"ll");
					if(date.length()> 5){					
							String sign = date.substring(5,6);
							String postFix = date.substring(6);
							String strNumOfDays = postFix;
							if("-".equalsIgnoreCase(sign)){
								strNumOfDays = "-"+postFix;
							}
							int numOfDays = Integer.parseInt(strNumOfDays);						
							currentDate.add(Calendar.DATE,numOfDays);
					}				
					if(hasTime){
						boolean hasSecond = false;
						String [] dateHolder = time.split(":");
						String strHour = dateHolder[0].length()==1?"0"+dateHolder[0]:dateHolder[0];
						String strMinute = dateHolder[1].length()==1?"0"+dateHolder[1]:dateHolder[1];
						String strSecond = "";
						if(dateHolder.length == 3){
							strSecond = dateHolder[2].length()==1?"0"+dateHolder[2]:dateHolder[2];
							hasSecond = true;
						}else{
							strSecond = "00";
						}
						time = strHour+":"+strMinute+":"+strSecond;
	//System.out.println("hour="+strHour+"  minute="+strMinute+"  second="+strSecond);					
						
					}

					SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
					String gmtTimeStr = convertToGMTString(format.format(currentDate.getTime())+" "+time, input.getUserTimeZone());
//		System.out.println("****gmtTime="+gmtTimeStr);
					String [] gmtSplit = gmtTimeStr.split(" ");
//					System.out.println("****gmtDate="+gmtSplit[0]+"  gmtTime="+gmtSplit[1]);			
					String gmtDate = gmtSplit[0];
					String gmtTime = gmtSplit[1];
					hashMap.put("DATE", gmtDate);			
					hashMap.put("TIME", gmtTime);
					
										
					
					if(!"00:00:00".equalsIgnoreCase(gmtTime)){
						hashMap.put("HASTIME", "TRUE");
					}else{
						hashMap.put("HASTIME", "FALSE");
					}
//					System.out.println("date="+hashMap.get("DATE")+"  time="+hashMap.get("TIME"));
				}else{//it is save filter
					String [] split = strDay.split(" ");
					hashMap.put("DATE",split[0]);
					if(split.length == 1){
						hashMap.put("HASTIME", "FALSE");					
					}else{
						String time = split[1].trim();
						if("00:00:00".equalsIgnoreCase(time)){
							hashMap.put("HASTIME", "FALSE");
						}else{
							hashMap.put("HASTIME", "TRUE");
							String [] timeList = time.split(":");
							if(timeList.length==2){
								time = time+":00";
							}
							hashMap.put("TIME", time);
						}
					}					
				} 
			}else{//absolute date
				if(QueryBuilderConstants.RETRIEVE_QUERY_CALL_TYPE.equalsIgnoreCase(input.getCallType())){
					String [] split = strDay.split(" ");
					String dateStr = split[0];
					hashMap.put("DATE",split[0]);
					if(split.length == 1){
						hashMap.put("HASTIME", "FALSE");					
					}else{
						String time = split[1].trim();
						if("00:00:00".equalsIgnoreCase(time)){
							hashMap.put("HASTIME", "FALSE");
						}else{
							hashMap.put("HASTIME", "TRUE");
							String [] timeList = time.split(":");
							if(timeList.length==2){
								time = time+":00";
							}
							
//							String gmtStringDateTime = convertToGMTString(dateStr+" "+time, input.getUserTimeZone());
//							String [] splitGmt = gmtStringDateTime.split(" ");
//							hashMap.put("DATE",splitGmt[0]);
//							hashMap.put("TIME", splitGmt[1]);
							hashMap.put("TIME", time);
							hashMap.put("HASTIME", "TRUE");
							
						}
					}
					
//					System.out.println("*** in absolute time   date="+split[0]+"   time="+hashMap.get("TIME"));
				}else{//save filter with absolute date and time		
//					System.out.println("**** it is absolut time filter saving ******");
					String [] split = strDay.split(" ");
					String date = split[0];
					if(split.length == 1){
						date = date +" 00:00:00";					
					}else{
						String time = split[1].trim();
						if(!"00:00:00".equalsIgnoreCase(time)){							
							String [] timeList = time.split(":");
							if(timeList.length==2){
								time = time+":00";
							}
							date = date +" "+time;
						}else{
							date = date +" 00:00:00";
						}
					}
					String gmtStringDateTime = convertToGMTString(date, input.getUserTimeZone());
					String [] splitGmt = gmtStringDateTime.split(" ");
					hashMap.put("DATE",splitGmt[0]);
					hashMap.put("TIME", splitGmt[1]);
					hashMap.put("HASTIME", "TRUE");
				}
			}
		 
		 return hashMap;
	 }	
		public static Date convertToGmt( Date date, TimeZone tz)
		{
		   Date ret = new Date( date.getTime() - tz.getRawOffset() );

		   // if we are now in DST, back off by the delta.  Note that we are checking the GMT date, this is the KEY.
		   if ( tz.inDaylightTime( ret ))
		   {
		      Date dstDate = new Date( ret.getTime() - tz.getDSTSavings() );

		      // check to make sure we have not crossed back into standard time
		      // this happens when we are on the cusp of DST (7pm the day before the change for PDT)
		      if ( tz.inDaylightTime( dstDate ))
		      {
		         ret = dstDate;
		      }
		   }

		   return ret;
	   }
		
		public static String convertToGMTString(String datetime, TimeZone tz){
			try{
				DateFormat dateFormatLong = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");  
				Date localDate = dateFormatLong.parse(datetime);
				Date gmtDate = convertToGmt(localDate, tz);
				return dateFormatLong.format(gmtDate);
			}catch(Exception e){
				e.printStackTrace();
			}
			return datetime;
		}
		/*
		 * Input: dateTime String with format MM/DD/YY hh:mm:ss
		 * return dateTime with MM/DD/YY hh:mm:ss of GMT
		 */
/*		public static String convertToGMTString(String datetime, TimeZone tz){
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
			ParsePosition pos = new ParsePosition(0);
			java.util.Date dd = sdf.parse(datetime,pos);
			System.out.println("first ="+dd.getTime());
			Date ss = convertToGmt(dd, tz);
			long time = ss.getTime();
			Calendar cl = Calendar.getInstance(tz);
			cl.setTimeInMillis(time);
			int year = cl.get(Calendar.YEAR);
			int month = cl.get(Calendar.MONTH);
			int date = cl.get(Calendar.DATE);
			int hour = cl.get(Calendar.HOUR_OF_DAY);
			int minute = cl.get(Calendar.MINUTE);
			int second = cl.get(Calendar.SECOND);
System.out.println("year="+year+" month="+month+" date="+date+"    houre="+hour+" minute="+minute+" second="+second);			
			String dateTime = "";
			int actualMonth = month+1;
			if(actualMonth < 10){
				dateTime="0"+actualMonth;
			}else{
				dateTime = ""+actualMonth;
			}
			if(date<10){
				dateTime = dateTime+"/0"+date;
			}else{
				dateTime = dateTime+"/"+date;
			}
			dateTime=dateTime+"/"+year;
			if(hour<10){
				dateTime = dateTime+" 0"+hour;
			}else{
				dateTime = dateTime+" "+hour;
			}
			if(minute<10){
				dateTime = dateTime+":0"+minute;
			}else{
				dateTime = dateTime+":"+minute;
			}
			if(second<10){
				dateTime = dateTime+":0"+second;
			}else{
				dateTime = dateTime+":"+second;
			}
			
			System.out.println("gmt ="+dateTime);
			return dateTime;
		}
*/
	//end ***************************************************************************************
	public static String getDateValue(String dateValue, QueryBuilderInputObj input) throws Exception {

 		// Lines Added by Rajesh Thangavelu On 12.5.2006
 		// Logs message for entering 'getDateValue' Function...
//		Object[] PassedFunctionParameters = {(Object)dateValue}; 		
		String dateVal = "";
		String finalValue = "";
		String mm = null;
		String dd = null;
		String yy = null;
		boolean datePresent = false;
		StringTokenizer val1Tkn1 = null;
		StringTokenizer val1Tkn = new StringTokenizer(dateValue,",");
		int cnt = 0;
		
		while (val1Tkn.hasMoreTokens()) {
			dateVal = val1Tkn.nextToken();
			//mark added **************************
				HashMap<String, String> hashMap = null;
				String hasTime = "false";
				String time ="";
				if(!input.isFromSaveFilter()){
					hashMap = convertDate(dateVal, input);
				
					String date = hashMap.get("DATE");
					time = hashMap.get("TIME");
					hasTime = hashMap.get("HASTIME");
					if("FALSE".equalsIgnoreCase(hasTime)){
						dateVal=date;
					}else{
						dateVal=date+" "+time;
					}
				}else{
					String [] split = dateVal.split(" ");
					if(split.length == 2){
						time = split[1].trim();
						hasTime = "true";
					}
				}
			//end *********************************
			// DF# 110611.sn
			dateValue = dateVal;
			if (dateVal.indexOf('/') > 0) {
				val1Tkn1 = new StringTokenizer(dateVal,"/");
				if (val1Tkn1.hasMoreTokens()) {
					mm = val1Tkn1.nextToken();
				}
				if (val1Tkn1.hasMoreTokens()) {
					dd = val1Tkn1.nextToken();
				}
				if (val1Tkn1.hasMoreTokens()) {
					yy = val1Tkn1.nextToken();
					datePresent = true;
				}
				if (datePresent) {
					if (mm.length() == 1) {
						mm = "0" + mm;
					}
					if (dd.length() == 1) {
						dd = "0" + dd;
					}
					if (yy.length() == 1) {
						yy = "200" + yy;
					}
					else if (yy.length() == 2) {
						yy = "20" + yy;
					}
					dateValue = getDBDependentDate(getUtilDate(dateValue, "MM/dd/yy"));
					
					//mark ma added *****************************************************
//					if("TRUE".equalsIgnoreCase(hasTime)){
//						dateValue = dateValue+" "+ time;
//					}
					//end ***************************************************************
				}
			}
			// DF# 110611.en
			if (cnt == 0)
				finalValue = dateValue;
			else
				finalValue = finalValue + "," + dateValue;
	 	 
//added bug# 9179
/*			ESDataInterface esDataIf = (ESDataInterface) ESUtilityFunctionsImpl.getServiceLocator().getServiceProvider(ESServiceLocatorInterface.HLP_DATA);				
			if (esDataIf.getDbType()==ESDataInterface.DB_TYPE_ORACLE){
				finalValue = "to_date('"+finalValue+"','MM/dd/yyyy HH24:MI:SS')";
			}*/
//end bug# 9179
			
			cnt = cnt + 1;
		}


		return finalValue;
	}
	
	public static String getDBDependentDate(java.util.Date dateToBeConverted)
	{
		
		if(dateToBeConverted!=null)
		{	
			SimpleDateFormat    sdf         		= null;
			String              output      		= null;
			StringBuffer		dateAsStrForOracle 	= null;
			sdf = new SimpleDateFormat(WavePlanningUtils.dbDateFormat);
			output = sdf.format(dateToBeConverted);
			return(output);
		}
		else
		{
			return null;
		}
	}	
	
	
	 public  static java.util.Date getUtilDate(String p_dateString,String p_format) throws Exception {
			String formatString = null;
			
				formatString = p_format;
				SimpleDateFormat sdf = new SimpleDateFormat(formatString);
				ParsePosition pos = new ParsePosition(0);
				java.util.Date d = sdf.parse(p_dateString,pos);
				return d;
			
	     }	
	 
	 
	 private static String getColFromOrderField(String orderField){
		 if(orderField.equalsIgnoreCase("ORDERNUM")){
			 return "orders.ORDERKEY&string";
		 }
		 else if(orderField.equalsIgnoreCase("OWNER")){
			 return "orders.STORERKEY&string";
		 }
		 else if(orderField.equalsIgnoreCase("AON")){
			 return "orders.EXTERNORDERKEY&string";
		 }
		 else if(orderField.equalsIgnoreCase("ORDERDATE")){
			 return "orders.ORDERDATE&string";
		 }
		 else if(orderField.equalsIgnoreCase("DLVRDATE")){
			 return "orders.DELIVERYDATE&string";
		 }
		 else if(orderField.equalsIgnoreCase("DLVRDATE2")){
			 return "orders.DELIVERYDATE2&string";
		 }
		 else if(orderField.equalsIgnoreCase("PRIORITY")){
			 return "orders.PRIORITY&string";
		 }
		 else if(orderField.equalsIgnoreCase("CUSTOMER")){
			 return "orders.CONSIGNEEKEY&string";
		 }
		 else if(orderField.equalsIgnoreCase("DOOR")){
			 return "orders.DOOR&string";
		 }
		 else if(orderField.equalsIgnoreCase("ROUTE")){
			 return "orders.ROUTE&string";
		 }
		 else if(orderField.equalsIgnoreCase("STOP")){
			 return "orders.STOP&string";
		 }
		 else if(orderField.equalsIgnoreCase("STAGE")){
			 return "orders.STAGE&string";
		 }
		 else if(orderField.equalsIgnoreCase("ORDRGRP")){
			 return "orders.ORDERGROUP&string";
		 }
		 else if(orderField.equalsIgnoreCase("CARRIER")){
			 return "orders.CARRIERCODE&string";
		 }
		 else if(orderField.equalsIgnoreCase("TRANSMODE")){
			 return "orders.TRANSPORTATIONMODE&string";
		 }
		 else if(orderField.equalsIgnoreCase("LOADID")){
			 return "orders.loadid&string";
		 }
		 else if(orderField.equalsIgnoreCase("RSD")){
			 return "orders.REQUESTEDSHIPDATE&string";
		 }
		 else if(orderField.equalsIgnoreCase("OHT")){
			 return "orders.OHTYPE&string";
		 }
		 else if(orderField.equalsIgnoreCase("ODRTYPE")){
			 return "orders.TYPE&string";
		 }
		 else if(orderField.equalsIgnoreCase("SUSR1")){
			 return "orders.SUSR1&string";
		 }
		 else if(orderField.equalsIgnoreCase("SUSR2")){
			 return "orders.SUSR2&string";
		 }
		 else if(orderField.equalsIgnoreCase("SUSR3")){
			 return "orders.SUSR3&string";
		 }
		 else if(orderField.equalsIgnoreCase("SUSR4")){
			 return "orders.SUSR4&string";
		 }
		 else if(orderField.equalsIgnoreCase("SUSR5")){
			 return "orders.SUSR5&string";
		 }
		 else if(orderField.equalsIgnoreCase("STATE")){
			 return "orders.STATE&string";
		 }
		 else if(orderField.equalsIgnoreCase("ZIP")){
			 return "orders.ZIPCODE&string";
		 }
		 else if(orderField.equalsIgnoreCase("CO")){
			 return "orders.C_COMPANY&string";
		 }
		 else if(orderField.equalsIgnoreCase("ITEM")){
			 return "orderdetail.SKU&string";
		 }
		 else if(orderField.equalsIgnoreCase("ITEMGRP")){
			 return "sku.skugroup&string";
		 }
		 else if(orderField.equalsIgnoreCase("ITEMGRP1")){
			 return "sku.skugroup1&string";
		 }
		 else if(orderField.equalsIgnoreCase("SUSPIND")){
			 return "orders.suspendedindicator&string";
		 }

		return null;
	 }
	 
	 
	 
	 public static String getQuery(/*String formType, */String whereClause, String facility, BioCollection detailRecords, String getWaveOrders, String rfidFlag, QueryBuilderInputObj input) throws Exception{
		 StringBuffer query = new StringBuffer();
		 String tableWhere = "";
		 ArrayList tables = new ArrayList();
		 ArrayList tablesAndWhere = new ArrayList();
		 String standardWhere = "";		 
		 String wmsName 	= WavePlanningUtils.wmsName;
//Harish:Machine2475030_SDIS07549		 standardWhere = " where (orders.status<=9 or orders.status in (12, 14, 15, 16, 17, 19))";
		 standardWhere = " where (orders.status < 95 and orders.TYPE <> \\'91\\')";	//Harish:Machine2475030_SDIS07549
		 if (getWaveOrders.equals("off")) {
			 standardWhere = standardWhere + " and orders.ORDERKEY NOT IN (SELECT  wavedetail.ORDERKEY" +
			 " from wavedetail where wavedetail.WHSEID=\\'" + facility + "\\')";
		 }

		 if (wmsName.equalsIgnoreCase(WavePlanningConstants.WMS_4000)) {
			 if (rfidFlag.equalsIgnoreCase("1"))
				 standardWhere = standardWhere + " and orders.RFIDFLAG = \\'1\\'";
			 else if (rfidFlag.equalsIgnoreCase("0"))
				 standardWhere = standardWhere + " and orders.RFIDFLAG = \\'0\\'";
			 standardWhere = standardWhere + " and orders.WHSEID=\\'" + facility.toUpperCase() + "\\'";
		 }
		 else {
			 standardWhere = standardWhere + " and orders.DC_ID =\\'" + facility + "\\'";
		 }

		 //query.append("select " + WavePlanningConstants.ORDERHEADER_SELECT_FIELDS + " ");
		 query.append("select orders.serialkey ");

//		 if(formType.equals("SQL")){
//			 if ((whereClause.trim().equals(""))) {				 
//				 
//				 
//				 query.append("from wp_orderheader where (wp_orderheader.status<=9 or wp_orderheader.status in (12, 14, 15, 16, 17, 19)) ");				 
//				 //Subodh: Check to see if orders on wave have to be displayed
//				 if (getWaveOrders.equals("off")) {						
//					 query.append(" and (wp_orderheader.ORDERKEY NOT IN (SELECT  wp_wavedetails.ORDERKEY" +
//							 " from wp_wavedetails where wp_wavedetails.WHSEID=\\'" + facility + "\\'))");
//				 }									
//				 
//				 if (wmsName.equalsIgnoreCase(WavePlanningConstants.WMS_4000)) {
//					 query.append(" and wp_orderheader.WHSEID =\\'" + facility + "\\'");
//				 }
//				 else {
//					 query.append(" and wp_orderheader.DC_ID =\\'" + facility + "\\'");
//				 }
//				 //query.append(" order by wp_orderheader.route, wp_orderheader.orderkey");
//			 }
//			 else{
//				 query = query.append(whereClause);
//			 }
//		 }
//		 else{
			 
//			 if(formType.equals("QRY")){

				 query.append(" from orders");
				 tablesAndWhere = WPQueryBuilderUtil.getWhereClause(detailRecords, input);//changed by mark
				 tables = (ArrayList)tablesAndWhere.get(0);
				 for (int i=0;i<tables.size();i++) {
					 if (!(((String)tables.get(i)).equalsIgnoreCase("orders")))
						 tableWhere = tableWhere + "," + (String)tables.get(i);
				 }
				 if (tables.indexOf("sku") != -1) {
					 standardWhere = standardWhere +
					 " and orders.ORDERKEY = orders.ORDERKEY" +
					 " and orderdetail.SKU = sku.SKU";
					 if (wmsName.equalsIgnoreCase(WavePlanningConstants.WMS_4000)) {
							standardWhere = standardWhere +
											" and orders.WHSEID = orderdetail.WHSEID";
						}
						else if (wmsName.equalsIgnoreCase(WavePlanningConstants.WMS_2000)) {
							standardWhere = standardWhere +
											" and orders.DC_ID = orderdetail.DC_ID";
						}
					 if (tables.indexOf("orderdetail") == -1)
						 tableWhere = tableWhere + "," + "orderdetail";
				 } else if (tables.indexOf("orderdetail") != -1) {
					 standardWhere = standardWhere + " and orders.ORDERKEY = orderdetail.ORDERKEY";
					 if (wmsName.equalsIgnoreCase(WavePlanningConstants.WMS_4000)) {
							standardWhere = standardWhere +
											" and orders.WHSEID = orderdetail.WHSEID";
						}
						else if (wmsName.equalsIgnoreCase(WavePlanningConstants.WMS_2000)) {
							standardWhere = standardWhere +
											" and orders.DC_ID = orderdetail.DC_ID";
						}
				 }
//			 }
//			 else{
//				 
//				 query.append(" from wp_orderheader");
//					String wclause = whereClause;
//					if (wclause.indexOf("wp_sku.skugroup") != -1 || wclause.indexOf("wp_sku.skugroup") != -1 ||
//							wclause.indexOf("wp_sku.skugroup") != -1) {
//						tableWhere = tableWhere + ", wp_orderline, wp_sku";
//						standardWhere = standardWhere +
//										" and wp_orderheader.ORDERKEY = wp_orderline.ORDERKEY" +
//										" and wp_orderline.SKU = wp_sku.SKU";			
//						if (wmsName.equalsIgnoreCase(WavePlanningConstants.WMS_4000)) {
//							standardWhere = standardWhere +
//											" and wp_orderheader.WHSEID = wp_orderline.WHSEID";
//						}
//						else if (wmsName.equalsIgnoreCase(WavePlanningConstants.WMS_2000)) {
//							standardWhere = standardWhere +
//											" and wp_orderheader.DC_ID = wp_orderline.DC_ID";
//						}
//					} else if (wclause.indexOf("wp_orderline") != -1 || wclause.indexOf("wp_orderline") != -1 ||
//							wclause.indexOf("wp_orderline") != -1) {
//						tableWhere = tableWhere + ", wp_orderline";
//						standardWhere = standardWhere + " and wp_orderheader.ORDERKEY = wp_orderline.ORDERKEY";		
//						if (wmsName.equalsIgnoreCase(WavePlanningConstants.WMS_4000)) {
//							standardWhere = standardWhere +
//											" and wp_orderheader.WHSEID = wp_orderline.WHSEID";
//						}
//						else if (wmsName.equalsIgnoreCase(WavePlanningConstants.WMS_2000)) {
//							standardWhere = standardWhere +
//											" and wp_orderheader.DC_ID = wp_orderline.DC_ID";
//						}
//					}
//			 }					
			 query.append(tableWhere);			 
			 query.append(standardWhere);			 
			 if (!(whereClause.trim().equals(""))) {				
				 String newWclause = whereClause;				
				 newWclause = getDBSpecificDateQuery(newWclause);
				 query.append(" and (" + newWclause + ")");			
			 }
			 
			 //query.append(" order by wp_orderheader.route, wp_orderheader.orderkey");
//		 }
		 	
		 return query.toString();
	 }
	 
	 
	 public static String getDBSpecificDateQuery(String queryString) {

	 		// Lines Added by Rajesh Thangavelu On 23.6.2006
	 		// Logs message for entering 'getDBSpecificDateQuery' Function...
			Object[] PassedFunctionParameters = {(Object)queryString};	 		

			String sourceDBVendor = WavePlanningUtils.sourceDatabaseVendor;
			if (sourceDBVendor.equalsIgnoreCase("mssql")) {
				queryString = queryString.replaceAll("orders.orderdate", "CAST(CONVERT(varchar,orders.orderdate,101) AS DATETIME)");
				queryString = queryString.replaceAll("orders.deliverydate2", "CAST(CONVERT(varchar,orders.deliverydate2,101) AS DATETIME)");
				if (queryString.indexOf(" orders.deliverydate") != -1)
					queryString = queryString.replaceAll(" orders.deliverydate", " CAST(CONVERT(varchar,orders.deliverydate,101) AS DATETIME)");
				else if(queryString.indexOf("(orders.deliverydate") != -1
						&& queryString.charAt(queryString.indexOf("(orders.deliverydate")+28) != '2')
					queryString = queryString.replaceAll("(orders.deliverydate", "(CAST(CONVERT(varchar,orders.deliverydate,101) AS DATETIME)");
				else if(queryString.indexOf("orders.deliverydate ") != -1)
					queryString = queryString.replaceAll("orders.deliverydate ", "CAST(CONVERT(varchar,orders.deliverydate,101) AS DATETIME) ");
				queryString = queryString.replaceAll("orders.requestedshipdate", "CAST(CONVERT(varchar,orders.requestedshipdate,101) AS DATETIME)");
			}
			else if (sourceDBVendor.equalsIgnoreCase("oracle")) {
				queryString = queryString.replaceAll("orders.orderdate", "TO_DATE(orders.orderdate)");
				queryString = queryString.replaceAll("orders.deliverydate2", "TO_DATE(orders.deliverydate2)");
	 			if (queryString.indexOf(" orders.deliverydate") != -1)
					queryString = queryString.replaceAll(" orders.deliverydate", " TO_DATE(orders.deliverydate)");
	 			else if(queryString.indexOf("(orders.deliverydate") != -1
					&& queryString.charAt(queryString.indexOf("(orders.deliverydate")+28) != '2')
					queryString = queryString.replaceAll("orders.deliverydate", "TO_DATE(orders.deliverydate)");
	 			else if(queryString.indexOf("orders.deliverydate ") != -1)
					queryString = queryString.replaceAll("orders.deliverydate ", "TO_DATE(orders.deliverydate) ");
				queryString = queryString.replaceAll("orders.requestedshipdate", "TO_DATE(orders.requestedshipdate)");
			}
			else if (sourceDBVendor.equalsIgnoreCase("db2") || sourceDBVendor.equalsIgnoreCase("informix")) {
				queryString = queryString.replaceAll("orders.orderdate", "DATE(orders.orderdate)");
				queryString = queryString.replaceAll("orders.deliverydate2", "DATE(orders.deliverydate2)");
	 			if (queryString.indexOf(" orders.deliverydate") != -1)
					queryString = queryString.replaceAll(" orders.deliverydate", " DATE(orders.deliverydate)");
	 			else if(queryString.indexOf("(orders.deliverydate") != -1
					&& queryString.charAt(queryString.indexOf("(orders.deliverydate")+28) != '2')
					queryString = queryString.replaceAll("orders.deliverydate", "DATE(orders.deliverydate)");
	 			else if(queryString.indexOf("orders.deliverydate ") != -1)
					queryString = queryString.replaceAll("orders.deliverydate ", "DATE(orders.deliverydate) ");
				queryString = queryString.replaceAll("orders.requestedshipdate", "DATE(orders.requestedshipdate)");
			}

			return queryString;
		}	 	 
	 public static BioCollectionBean getShipmentOrders(QueryBuilderInputObj input) throws EpiDataException{
		 //		 public static BioCollectionBean getShipmentOrders(String userQry, int maxOrders, int maxOrderLines, double maxCube, double maxWeight, double maxCases, StateInterface state) throws EpiDataException{
		 String userQry = input.getUserQry();
		 int maxOrders = input.getMaxOrders();
		 int maxOrderLines = input.getMaxOrderLines();
		 double maxCube = input.getMaxCube();
		 double maxWeight = input.getMaxWeight();
		 double maxCases = input.getMaxCases();
		 StateInterface state = input.getState();
//System.out.println("***** it is new getshippment order*******");		 
		 Query query = new Query("wm_wp_orders","DPE('SQL','@[wm_wp_orders.SERIALKEY] IN ("+userQry+")')","");
		 BioCollectionBean bc = state.getDefaultUnitOfWork().getBioCollectionBean(query);	
		 int orderCount = 0;
		 int orderLineSum = 0;
		 double cubeSum = 0;
		 double weightSum = 0;
		 double casesSum = 0;
		 String prevRoute = "";
		 //order limit parameters
		 double minEachOrderLines = input.getMinEachOrderLines();
		 double maxEachOrderLines = input.getMaxEachOrderLines();
		 double minEachOrderQty = input.getMinEachOrderQty();
		 double maxEachOrderQty = input.getMaxEachOrderQty();
		 double minEachOrderCube = input.getMinEachOrderCube();
		 double maxEachOrderCube = input.getMaxEachOrderCube();
		 double minEachOrderWeight = input.getMinEachOrderWeight();
		 double maxEachOrderWeight = input.getMaxEachOrderWeight();
		 ArrayList <String> tempSerialHolder = new ArrayList<String>();
		 
//		 System.out.println("***** it is new getshippment order size="+bc.size());		 
		 
		 for(int i = 0; i < bc.size(); i++){
			 Bio order = bc.elementAt(i);
			 
			 //Get current record values
			 int currRecordOrderLineTotal = order.get("TOTALORDERLINES")==null?0:Integer.parseInt(order.get("TOTALORDERLINES").toString());
			 double currRecordCubeTotal = order.get("TOTALCUBE")==null?0.0:Double.parseDouble(order.get("TOTALCUBE").toString());
			 double currRecordWeightTotal = order.get("TOTALGROSSWGT")==null?0.0:Double.parseDouble(order.get("TOTALGROSSWGT").toString());
			 double currRecordCasesTotal = order.get("TOTALQTY")==null?0.0:Double.parseDouble(order.get("TOTALQTY").toString());
//System.out.println("  curorderlines="+currRecordOrderLineTotal);
//System.out.println("  curordercube="+currRecordCubeTotal);
//System.out.println("  curorderweight="+currRecordWeightTotal);
//System.out.println("  curorderqty="+currRecordCasesTotal);

			 String currRecordRoute = order.get("ROUTE")==null?"":order.get("ROUTE").toString();
			 //Add to totals
			 orderCount++;
			 orderLineSum += currRecordOrderLineTotal;
			 cubeSum += currRecordCubeTotal;
			 weightSum += currRecordWeightTotal;
			 casesSum += currRecordCasesTotal;
			 
			 
			 //Stop looping if any max values have been reached (except cases max)
			 //Only do so if this record's route is not the same as the previous record's route
			 if(currRecordRoute != prevRoute){
				 if(maxOrders > 0 && orderCount >= maxOrders){
					 break;
				 }
				 if(maxOrderLines > 0 && orderLineSum >= maxOrderLines){
					 break;
				 }
				 if(maxCube > 0 && cubeSum >= maxCube){ 
					 if( cubeSum > maxCube){
						 orderCount--;
						 orderLineSum -= currRecordOrderLineTotal;
						 cubeSum -= currRecordCubeTotal;
						 weightSum -= currRecordWeightTotal;
						 casesSum -= currRecordCasesTotal;
					 }
					 break;
				 }
				 if(maxWeight > 0 && weightSum >= maxWeight){
					 if (weightSum > maxWeight){
						 orderCount--;
						 orderLineSum -= currRecordOrderLineTotal;
						 cubeSum -= currRecordCubeTotal;
						 weightSum -= currRecordWeightTotal;
						 casesSum -= currRecordCasesTotal;
					 }
					 break;
				 }
			 }
			 //If this record's route is the same as the previous record's route then check the cases max			 
			 else{
				 if(maxCases > 0 && casesSum >= maxCases){
					 break;
				 }
			 }
			 
			 
			 
			 //check order limits
			 boolean out = false;
			if(currRecordCasesTotal < minEachOrderQty || currRecordCasesTotal > maxEachOrderQty){
					 out = true;
			}
			if(currRecordOrderLineTotal < minEachOrderLines || currRecordOrderLineTotal > maxEachOrderLines){
					 out = true;
			}
			if(currRecordWeightTotal < minEachOrderWeight || currRecordWeightTotal > maxEachOrderWeight){
					 out = true;
			}
			if(currRecordCubeTotal < minEachOrderCube || currRecordCubeTotal > maxEachOrderCube){
					 out = true;
				 }
			if(out){
				 tempSerialHolder.add(order.getString("SERIALKEY"));
				 orderLineSum -= currRecordOrderLineTotal;
				 cubeSum -= currRecordCubeTotal;
				 weightSum -= currRecordWeightTotal;
				 casesSum -= currRecordCasesTotal;
			}
			 
			 prevRoute = currRecordRoute;
		 }
		 
		 
		 
		 //use last value of loop index to create a new bio query that will only select that number of records.
			if(WavePlanningUtils.sourceDatabaseVendor.equalsIgnoreCase(WavePlanningConstants.DB_TYPE_STRING_ORACLE)){
				String replacementStr = "where rownum  <= "+orderCount+" AND ";
				userQry = userQry.replaceFirst("where ",replacementStr);
			}
			else if(WavePlanningUtils.sourceDatabaseVendor.equalsIgnoreCase(WavePlanningConstants.DB_TYPE_STRING_MSSQL)) { 
				 userQry = userQry.replaceFirst("select","select TOP "+orderCount+" ");		
			}

		 query = new Query("wm_wp_orders","DPE('SQL','@[wm_wp_orders.SERIALKEY] IN ("+userQry+")')","");
		 bc = state.getDefaultUnitOfWork().getBioCollectionBean(query);			 

		 //mark added ****************
		 //getShipmentOrders(state, bc, tempSerialHolder);
/*		 //use last value of loop index to create a new bio query that will only select that number of records.
		 userQry = userQry.replaceFirst("select","select TOP "+orderCount+" ");		
		 query = new Query("wp_orderheader","DPE('SQL','@[wp_orderheader.SERIALKEY] IN ("+userQry+")')","");
		 bc = state.getDefaultUnitOfWork().getBioCollectionBean(query);	
*/		 
		 return getShipmentOrders(state, bc, tempSerialHolder);		
	 }

	 
	 public static BioCollectionBean getShipmentOrders(StateInterface state, BioCollectionBean bc, ArrayList <String> serialKeys) throws EpiDataException{
		StringBuffer qry = new StringBuffer();
		int size = serialKeys.size();
		if(size == 0){
			return bc;
		}
//		System.out.println("*****order size=" +size);
		for(int i=0;i<size;i++){
			if(i==0){
				if(size == 1){
					qry.append("'"+serialKeys.get(i)+"'");
				}else{
					qry.append("'"+serialKeys.get(i)+"',");
				}
			}else if( i != size-1){
				qry.append("'"+serialKeys.get(i)+"',");
			}else{//i==size-1
				qry.append("'"+serialKeys.get(i)+"'");
			}
		}
		
		Query query = new Query("orders","NOT (orders.SERIALKEY IN ("+qry.toString()+"))","");

		 bc.filterInPlace(query);			 

		 return bc;
	 }
/*	 public static BioCollectionBean getShipmentOrders(String userQry, int maxOrders, int maxOrderLines, double maxCube, double maxWeight, double maxCases, StateInterface state) throws EpiDataException{
//		 public static BioCollectionBean getShipmentOrders(String userQry, int maxOrders, int maxOrderLines, double maxCube, double maxWeight, double maxCases, StateInterface state) throws EpiDataException{
		 		 
		 Query query = new Query("wp_orderheader","DPE('SQL','@[wp_orderheader.SERIALKEY] IN ("+userQry+")')","");
		 BioCollectionBean bc = state.getDefaultUnitOfWork().getBioCollectionBean(query);	
		 int orderCount = 0;
		 int orderLineSum = 0;
		 double cubeSum = 0;
		 double weightSum = 0;
		 double casesSum = 0;
		 String prevRoute = "";

		 
		 for(int i = 0; i < bc.size(); i++){
			 Bio order = bc.elementAt(i);
			 
			 //Get current record values
			 int currRecordOrderLineTotal = order.get("TOTALORDERLINES")==null?0:Integer.parseInt(order.get("TOTALORDERLINES").toString());
			 double currRecordCubeTotal = order.get("TOTALCUBE")==null?0.0:Double.parseDouble(order.get("TOTALCUBE").toString());
			 double currRecordWeightTotal = order.get("TOTALGROSSWGT")==null?0.0:Double.parseDouble(order.get("TOTALGROSSWGT").toString());
			 double currRecordCasesTotal = order.get("TOTALQTY")==null?0.0:Double.parseDouble(order.get("TOTALQTY").toString());
System.out.println("  curorderlines="+currRecordOrderLineTotal);
System.out.println("  curordercube="+currRecordCubeTotal);
System.out.println("  curorderweight="+currRecordWeightTotal);
System.out.println("  curorderqty="+currRecordCasesTotal);

			 String currRecordRoute = order.get("ROUTE")==null?"":order.get("ROUTE").toString();
			 //Add to totals
			 orderCount++;
			 orderLineSum += currRecordOrderLineTotal;
			 cubeSum += currRecordCubeTotal;
			 weightSum += currRecordWeightTotal;
			 casesSum += currRecordCasesTotal;
			 
			 
			 //Stop looping if any max values have been reached (except cases max)
			 //Only do so if this record's route is not the same as the previous record's route
			 if(currRecordRoute != prevRoute){
				 if(maxOrders > 0 && orderCount >= maxOrders){
					 break;
				 }
				 if(maxOrderLines > 0 && orderLineSum >= maxOrderLines){
					 break;
				 }
				 if(maxCube > 0 && cubeSum >= maxCube){ 
					 if( cubeSum > maxCube){
						 orderCount--;
						 orderLineSum -= currRecordOrderLineTotal;
						 cubeSum -= currRecordCubeTotal;
						 weightSum -= currRecordWeightTotal;
						 casesSum -= currRecordCasesTotal;
					 }
					 break;
				 }
				 if(maxWeight > 0 && weightSum >= maxWeight){
					 if (weightSum > maxWeight){
						 orderCount--;
						 orderLineSum -= currRecordOrderLineTotal;
						 cubeSum -= currRecordCubeTotal;
						 weightSum -= currRecordWeightTotal;
						 casesSum -= currRecordCasesTotal;
					 }
					 break;
				 }
			 }
			 //If this record's route is the same as the previous record's route then check the cases max			 
			 else{
				 if(maxCases > 0 && casesSum >= maxCases){
					 break;
				 }
			 }		 
			 prevRoute = currRecordRoute;
		 }
		 
		 
		 
		 //use last value of loop index to create a new bio query that will only select that number of records.
			if(WavePlanningUtils.sourceDatabaseVendor.equalsIgnoreCase(WavePlanningConstants.DB_TYPE_STRING_ORACLE)){
				String replacementStr = "where rownum  <= "+orderCount+" AND ";
				userQry = userQry.replaceFirst("where ",replacementStr);
			}
			else if(WavePlanningUtils.sourceDatabaseVendor.equalsIgnoreCase(WavePlanningConstants.DB_TYPE_STRING_MSSQL)) { 
				 userQry = userQry.replaceFirst("select","select TOP "+orderCount+" ");		
			}

		 query = new Query("wp_orderheader","DPE('SQL','@[wp_orderheader.SERIALKEY] IN ("+userQry+")')","");
		 bc = state.getDefaultUnitOfWork().getBioCollectionBean(query);			 

		 
/*		 //use last value of loop index to create a new bio query that will only select that number of records.
		 userQry = userQry.replaceFirst("select","select TOP "+orderCount+" ");		
		 query = new Query("wp_orderheader","DPE('SQL','@[wp_orderheader.SERIALKEY] IN ("+userQry+")')","");
		 bc = state.getDefaultUnitOfWork().getBioCollectionBean(query);	
*/		 
//		 return bc;		
//	 }
	 public static TimeZone getTimeZone(StateInterface state) 

     {

            UserInterface user = state.getUser();

            

            if(user instanceof UserBean)

            {

                   Object timeZoneObj = ((UserBean)user).getRequestAttribute("browser time zone");

                   if(timeZoneObj instanceof String)

                   {

                         TimeZone userTimeZone = TimeZone.getTimeZone(timeZoneObj.toString());

                         return userTimeZone;

                   }

            }

            return Calendar.getInstance().getTimeZone();

     }

	 
	 
}