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
package com.ssaglobal.scm.wms.util.upload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.epiphany.shr.data.beans.BioService;
import com.epiphany.shr.data.beans.ejb.BioCollectionEpistub;
import com.epiphany.shr.data.beans.ejb.BioServiceUtil;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioAttributeMetadata;
import com.epiphany.shr.data.bio.BioAttributeTypes;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.BioCollectionRef;
import com.epiphany.shr.data.bio.BioMappingTypes;
import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.data.bioxform.BioTransformServiceBean;
import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.metadata.Metadata;
import com.epiphany.shr.metadata.objects.EpnyLocale;
import com.epiphany.shr.metadata.objects.bio.AttributeType;
import com.epiphany.shr.ui.model.data.BioCollectionBean;
import com.epiphany.shr.ui.state.EpnyControllerState;
import com.epiphany.shr.ui.util.BioAttributeFormatter;
import com.epiphany.shr.ui.util.MetaDataAccess;
import com.epiphany.shr.ui.util.export.ColumnInfo;
import com.epiphany.shr.ui.util.export.ExportHandler;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.util.algorithm.StringAlgorithms;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.SsaAccessBase;

public class ExportToXLSExtendedImpl {
	
	private String template = null;
	private String detailCollectionName = null;
	protected static ILoggerCategory _log = LoggerFactory.getInstance(ExportToXLSExtendedImpl.class);
	
	private EpnyControllerState state;
	private DateFormat dateFormat;
	private String transformationMapName;
	protected ExportHandler exportHandler;
	
	//Handler Attributes
	private HSSFWorkbook workbook;
	private HSSFCellStyle headerCellStyle;
	private OutputStream out;
	private HSSFCellStyle dateCellStyle;
	
	
	private int detailRowCount ;
	//private static ILoggerCategory _logger;
	//private static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	private static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	private static final int MASTERHEADERROW = 0; //Header row with column names
	private static final int MASTERHEADERDESCROW = 1; //Description row
	private static final int DETAILHEADERROW = 0; //Header row with column names
	private static final int DETAILHEADERDESCROW = 1; //Descriptions row
	
	private static final String POIDATEFORMAT = "m/d/yy h:mm";
	private static int COPYBUFFERSIZE = 512;
	protected  static int MAX_ROWS_PER_SHEET = 65500;
	protected  static String DATASHEET = "Data";
	protected  static String DETAILDATASHEET = "Detail";
	protected  static String FORMATSHEET = "Validations";
	private  static  final int DATASHEETSTARTROW=2; //Data is written begining at this row , 4
	private  static final int DETAILDATASHEETSTARTROW=2; //Data is written begining at this row , 4
	protected static ILoggerCategory _logger = LoggerFactory.getInstance(ExportToXLSExtendedImpl.class);
	
	private static final int BIO_ATTRIBUTE_TYPE_DECIMAL = 4;
	private static final int BIO_ATTRIBUTE_TYPE_FLOAT = 3;

	/**
	 static 
	 {
	 _logger = LoggerFactory.getInstance(com.epiphany.shr.ui.util.export.ExportHelper.class);
	 }
	 */
	
	
	public ExportToXLSExtendedImpl(){
		
		
	}
	
	
	
	public ExportToXLSExtendedImpl(EpnyControllerState state)
	{
		dateFormat = DEFAULT_DATE_FORMAT;
		this.state = state;
	}
	
	
	public ExportToXLSExtendedImpl(EpnyControllerState state, RuntimeListForm listForm, String template, String detailCollectionName)
	{
		this(state);
		this.template = template;
		this.detailCollectionName = detailCollectionName;
		_log.debug("LOG_SYSTEM_OUT","[ExportToXlsExtendedImpl]Start:)Template:"+template,100L);
	}
	
	//public static String getTemplateFilename(String template){
	public String getTemplateFilename(String template){
		String fileSeparator = System.getProperties().getProperty("file.separator");
		SsaAccessBase appAccess = new SQLDPConnectionFactory();
		String oahome = appAccess.getValue("webUIConfig","OAHome");
		
		String path = null;
		
		if(oahome == null)
			return null;
		else
			//SRG: 9.2 Upgrade -- Start
			//path = oahome + fileSeparator + "wm" + fileSeparator + 
			//"wmwebui" + fileSeparator + "templates" + fileSeparator  ;
			path = oahome + fileSeparator + "sce" + fileSeparator + 
			"sceui" + fileSeparator + "templates" + fileSeparator  ;
			//SRG: 9.2 Upgrade -- End
		
		
		
		String filename = formatPath(path)+ "template-" + template.toLowerCase() + ".xls";
		
		_log.debug("LOG_SYSTEM_OUT","[ExportHelperExtended]template:"+filename,100L);
		return filename;
	}
	
	
	//public static String formatPath(String path){
	public   String formatPath(String path){
		String newPath="";
		
		Pattern pattern = Pattern.compile("\\.");
		
		Matcher matcher = pattern.matcher(path);
		
		int start = 0;
		
		while(matcher.find()){
			String substring = path.substring(start,matcher.start()-1);
			newPath += "\\" + substring;
			start = matcher.end();
		}
		if (start < path.length()-1){
			newPath += path.substring(start);
		}
		
		
		return newPath;
	}
	
	
	public DateFormat getDateFormat()
	{
		return dateFormat;
	}
	
	public void setDateFormat(DateFormat dateFormat)
	{
		this.dateFormat = dateFormat;
	}
	
	public String getTransformationName()
	{
		return transformationMapName;
	}
	
	public void setTransformationName(String transformationMapName)
	{
		this.transformationMapName = transformationMapName;
	}
	
	
	
	public void exportData(ByteArrayOutputStream baos, BioCollectionBean bioCollectionBean){

		exportData(baos, bioCollectionBean, null);
	}
	
	//jp 8925.begin
	public void exportData(ByteArrayOutputStream baos, BioCollectionBean bioCollectionBean, UnitOfWork uow){
		
		detailRowCount = DETAILDATASHEETSTARTROW;
		
		initializeHandler(baos);
		HSSFSheet dataSheet = getSheetFromWorkbook(DATASHEET);
		SheetWrapper sheetWrapper = new SheetWrapper(dataSheet,DATASHEETSTARTROW );
		//jp 8925.begin
		exportBioCollectionBean (bioCollectionBean, sheetWrapper, uow);
		//jp 8925.end
		postProcessData();
	}
	//jp 8925.end
	
	public void initializeHandler(OutputStream out)
	{
		
		_log.debug("LOG_SYSTEM_OUT","[ExportToXlsExtendedImpl]initializeHandler:start",100L);
		
		this.out = out;
		
		workbook = new HSSFWorkbook();
		
		initializeCellStylesAndFonts();
		
		loadTemplateInMemory();
		
		_log.debug("LOG_SYSTEM_OUT","[ExportToXlsExtendedImpl]initializeHandler:end",100L);
	}
	
	private void initializeCellStylesAndFonts(){
		headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setWrapText(true);
		headerCellStyle.setAlignment((short)1);
		headerCellStyle.setBorderBottom((short)2);
		headerCellStyle.setBorderTop((short)2);
		headerCellStyle.setBorderRight((short)2);
		headerCellStyle.setBorderLeft((short)0);
		HSSFFont font = workbook.createFont();
		headerCellStyle.setFont(font);
		font.setBoldweight((short)700);
		font.setFontName("Arial");
		font.setFontHeight((short)200);
		
	}
	
	private void loadTemplateInMemory(){
		String templateFilename = getTemplateFilename (template);
		
		_log.debug("LOG_SYSTEM_OUT","[ExportToXlsExtendedImpl]initializeHandler:templateFileName:"+templateFilename,100L);
		
		
		//Loads template into memory
		ByteArrayOutputStream baosTmp = copyFileToBAOS(templateFilename);
		
		try{
			POIFSFileSystem poiInput = new POIFSFileSystem(new ByteArrayInputStream(baosTmp.toByteArray()));
			
			this.workbook = new HSSFWorkbook(poiInput);
			
			dateCellStyle = workbook.createCellStyle();
			dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(POIDATEFORMAT));
			
			_log.debug("LOG_SYSTEM_OUT","[ExportToXlsExtendedImpl]Loaded template into memory",100L);
		}catch(IOException e){
			e.printStackTrace();
		}
		
		
		
		
	}
	private ByteArrayOutputStream copyFileToBAOS(String source){
		InputStream input = null;
		ByteArrayOutputStream output =null;
		
		try{
			byte[] buffer = new byte[COPYBUFFERSIZE];
			
			input =new FileInputStream(new File(source));
			
			output = new ByteArrayOutputStream();
			
			int offset =0;
			while(true){
				int byteCount = input.read(buffer,offset,buffer.length);
				
				if (byteCount<=0)
					break;
				else
					output.write(buffer);
				
				
			}
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
			
		}catch (IOException e){
			e.printStackTrace();
		}finally{
			try{
				input.close();
				
			}catch(IOException e){
				e.printStackTrace();				
			}
		}
		
		return output;
	}
	
	private HSSFSheet getSheetFromWorkbook(String sheetName){
		HSSFSheet genericSheet = workbook.getSheet(sheetName);
		if(genericSheet==null){
			_log.debug("LOG_SYSTEM_OUT","Missing :"+sheetName +" sheet on template file.",100L);
			return null;
		}else{
			
			_log.debug("LOG_SYSTEM_OUT","[ExcelExportHandlerExtended]Found "+sheetName+" in template.",100L);
		}
		return genericSheet;
	}
	
	public void exportBioCollectionBean(BioCollectionBean bioCollectionBean, SheetWrapper sheetWrapper)
	{
		exportBioCollectionBean(bioCollectionBean, sheetWrapper, null);
	}
	
	public void exportBioCollectionBean(BioCollectionBean bioCollectionBean, SheetWrapper sheetWrapper, UnitOfWork uow)
	{
		boolean masterFlag = true;
		
		exportBioCollectionRef(bioCollectionBean.getBioCollectionRef(), sheetWrapper, masterFlag, uow);
		
	}
	
	public void exportBioCollectionRef(BioCollectionRef bioCollectionRef, SheetWrapper sheetWrapper, boolean masterFlag)
	{
		exportBioCollectionRef(bioCollectionRef, sheetWrapper, masterFlag, null);
	}
	
	public void exportBioCollectionRef(BioCollectionRef bioCollectionRef, SheetWrapper sheetWrapper, boolean masterFlag, 
			UnitOfWork uow)
	{
		BioService bioSvc = null;
		if(bioCollectionRef == null)
		{
			cleanup(bioSvc);
			return;
		}
		try{
			//jp 8925.begin
			//bioSvc = BioServiceUtil.getBioService();
			//UnitOfWork uow = bioSvc.getUnitOfWork();

			if(uow==null){
				bioSvc = BioServiceUtil.getBioService();
				uow = bioSvc.getUnitOfWork();
			}
			//jp 8925.end
			
			BioCollection bioCollection = uow.fetchBioCollection(bioCollectionRef);
			exportBioCollection(bioCollection, sheetWrapper, masterFlag);
			cleanup(bioSvc);
		}catch(EpiException e){
			_logger.error("EXP_EXPORT_BIO_COL_REF", "Error exporting BioCollectionRef", e);
			_log.debug("LOG_SYSTEM_OUT","Error exporting BioCollectionRef",100L);
		}
	}
	
	public void exportBioCollection(BioCollection bioCollection, SheetWrapper sheetWrapper, boolean masterFlag)
	{
		_log.debug("LOG_SYSTEM_OUT","[ExportToXlsExtendedImpl]exportBioCollection:start",100L);
	
		long start;
		BioService bioSvc;
		start = System.currentTimeMillis();
		bioSvc = null;
		int size;
		try {
			size = bioCollection == null ? 0 : bioCollection.size();
			if(size == 0)
				return;
			
			_log.debug("LOG_SYSTEM_OUT","[ExportToXlsExtendedImpl]testing bioCollection size...:"+ bioCollection.size(),100L);
			
			Bio firstBio = bioCollection.elementAt(0);
			
			ColumnInfo[] columnInfos = initializeColumnInfos(xform(firstBio));
			
			TemplateColumnList templateColumnList = filterColumnInfosWithTemplate(columnInfos, masterFlag);
			
			columnInfos = templateColumnList.getColumnInfos();
			
			for(int i = 0; i < size; i++)
			{
				Bio bio = bioCollection.elementAt(i);
				exportBioInternal(xform(bio), sheetWrapper, columnInfos, masterFlag, 
						templateColumnList.getTemplateOnlyColumns(),
						templateColumnList.getTemplateColumns());
				
				//jp Refactor. begin . Process Children
				if (masterFlag && detailCollectionName !=null  && detailCollectionName.trim().length()>0){
					BioCollectionEpistub stub = (BioCollectionEpistub)bio.get(detailCollectionName);
					BioCollectionRef bioCollectionRef = stub.getBioCollectionRef();
					SheetWrapper detailSheetWrapper = new SheetWrapper(getSheetFromWorkbook(DETAILDATASHEET),detailRowCount);
					
					exportBioCollectionRef(bioCollectionRef,detailSheetWrapper, !masterFlag);
				}
				//jp end
			}
			
		}
		catch(EpiDataException e){
			_logger.error("EXP_EXPORT_BIO_COL", "Error exporting BioCollection", e);
			_log.debug("LOG_SYSTEM_OUT","Error exporting BioCollection",100L);
		}
		
		catch(EpiException e)
		{
			_logger.error("EXP_EXPORT_BIO_COL", "Error exporting BioCollection", e);
			_log.debug("LOG_SYSTEM_OUT","Error exporting BioCollection",100L);
		}
		
		
		_log.debug("LOG_SYSTEM_OUT","[ExportToXlsExtendedImpl]exportBioCollection:end",100L);
	}
	
	
	private void cleanup(BioService bioSvc)
	{
		if(bioSvc != null)
		{
			try
			{
				bioSvc.getUnitOfWork().close();
			}
			catch(Exception e)
			{
				_logger.warn("LOG_EXPORT_UOW_CLOSE", "Error closing UOW", e);
			}
			try
			{
				bioSvc.cleanup();
			}
			catch(Exception e)
			{
				_logger.warn("LOG_EXPORT_BIOSVC_CLEANUP", "Error cleaning up BioSvc", e);
			}
		}
	}
	
	private Bio xform(Bio bio)
	{
		if(getTransformationName() == null)
			return bio;
		else
			return Bio2Bio(bio, getTransformationName());
		
	}
	
	
	//public static Bio Bio2Bio(Bio bio, String mapName)
	public Bio Bio2Bio(Bio bio, String mapName)
	{
		if(mapName == null || mapName.length() == 0)
			return bio;
		BioTransformServiceBean transformService = new BioTransformServiceBean();
		Bio transformedBio = null;
		try
		{
			transformedBio = (Bio)transformService.transform(bio, mapName);
		}
		catch(EpiException e)
		{
			_logger.error("EXP_BIO_TRANSFORM", "Error transforming Bio", e);
		}
		return transformedBio;
	}
	
	private ColumnInfo[] initializeColumnInfos(Bio bio)
	{
		_log.debug("LOG_SYSTEM_OUT","[ExportToXlsExtendedImpl]initializeColumnInfos:start",100L);
		_log.debug("LOG_SYSTEM_OUT","[ExportToXlsExtendedImpl]initializeColumnInfos:bioname:"+bio.getTypeName(),100L);
		Map attributes = getBioAttributes(bio);
		
		ColumnInfo[] columnInfos=null;
		
		List columns = new ArrayList();
		
		if(columnInfos != null)
		{
			for(int i = 0; i < columnInfos.length; i++)
			{
				BioAttributeMetadata attributeMetadata = (BioAttributeMetadata)attributes.get(columnInfos[i].getAttributeName());
				columnInfos[i].setAttributeMetadata(attributeMetadata);
			}
			
		} else
		{
			for(Iterator iter = attributes.keySet().iterator(); iter.hasNext();)
			{
				BioAttributeMetadata attributeMetadata = (BioAttributeMetadata)attributes.get(iter.next());
				if(attributeMetadata.getMappingType() == BioMappingTypes.MAP_DIRECT ){
					
					columns.add(new ColumnInfoExtended(attributeMetadata));
					
				}
				
			}
			
			
			columnInfos = (ColumnInfoExtended[])columns.toArray(new ColumnInfoExtended[0]);
		}
		
		return columnInfos;
	}
	
	private Map getBioAttributes(Bio bio)
	{
		Map map;
		BioService bioSvc;
		map = new TreeMap();
		bioSvc = null;
		try{
			bioSvc = BioServiceUtil.getBioService();
			UnitOfWork uow = bioSvc.getUnitOfWork();
			getBioAttributesRecursively(bio, uow, map);
			cleanup(bioSvc);
		}catch(EpiException e){
			_logger.error("EXP_EXPORT_GET_ATTRS", "Error getting Bio attributes", e);
			cleanup(bioSvc);
		}catch(Exception e){
			cleanup(bioSvc);
			e.printStackTrace();
		}
		
		return map;
	}
	
	private void getBioAttributesRecursively(Bio bio, UnitOfWork uow, Map map)
	throws EpiDataException
	{
		if(bio.getBioMetadata().getSuperclass() != null)
		{
			Bio superBio = uow.createBio(bio.getBioMetadata().getSuperclass());
			getBioAttributesRecursively(superBio, uow, map);
		}
		BioAttributeMetadata attributes[] = bio.getBioAttributeMetadata();
		for(int i = 0; i < attributes.length; i++)
		{
			int type = attributes[i].getType();
			if(type > 0 && type < 10 && type != 5)
				map.put(attributes[i].getName(), attributes[i]);
		}
		
	}
	public void processData(Bio bio, Object rawValues[], String formattedValues[], SheetWrapper sheetWrapper,
			ColumnInfo[] columnInfos, boolean masterFlag, List<DbColumn> templateOnlyColumns,
			List<DbColumn> templateColumns)
	{
		try{
			int rowNumber = sheetWrapper.getRowNumber();
			HSSFSheet sheet = sheetWrapper.getSheet();
			
			if(rowNumber == MAX_ROWS_PER_SHEET)
			{
				_logger.error("EXP_EXCEL_POST_PROCESS", "Error processing excel export, exceeded max number of rows per Data sheet", null);
				return;
			}
			
			
			if (masterFlag){
				//sheetWrapper.setRowNumber(rowNumber+1);
				sheetWrapper.incrementRowNumber();
			}else{
				
				sheetWrapper.setRowNumber(++detailRowCount);
			}
			
			HSSFRow row = sheet.createRow(rowNumber);
			int columnOffset = 0;
			int columnCount = columnInfos.length;
			
			//Message Column
			//HSSFCell cellMessage = row.createCell((short)0); //SRG POI 3.7 Upgrade
			//cellMessage.setEncoding((short)1); //SRG POI 3.7 Upgrade
			HSSFCell cellMessage = row.createCell((int)0); 			
			cellMessage.setCellValue("");
			columnOffset = 1;
			
			//Generic column for MasterDetail templates
			if (detailCollectionName !=null  && detailCollectionName.trim().length()>0){
				//HSSFCell cellGeneric = row.createCell((short)1);
				HSSFCell cellGeneric = row.createCell((int)1);
				//cellGeneric.setEncoding((short)1); //SRG POI 3.7 Upgrade
				cellGeneric.setCellValue("");
				columnOffset = 2;
			}
			
			//List<DbColumn> templateColumns = getColumnDescriptions();
			int xlsIndex = 0;
			
			for(int i = 0; i < columnCount; i++)
			{
				
				xlsIndex = i + columnOffset;
				
				ColumnInfo column = columnInfos[i];
			
				//Skip template only columns 
				DbColumn dbColumn = (DbColumn)templateColumns.get(i + columnOffset);
				//_log.debug("LOG_SYSTEM_OUT","Searching :"+dbColumn.getName(),100L);
				if(templateOnlyColumns.contains(dbColumn)){
					_log.debug("LOG_SYSTEM_OUT","Skipping:"+dbColumn.getName(),100L);
					columnOffset++;
					continue; 
				}
				
				
				
				/*
				 System.out.println("[ExportToXlsExtendedImpl]processData:BIO column:"+column.getAttributeName()+
				 "\tType:"+column.getAttributeMetadata().getType());
				 */
				if(column.getAttributeMetadata().getMappingType() == BioMappingTypes.MAP_DIRECT){
					//jp Refactor begin
					//HSSFCell cell = row.createCell((short)(i+1));
					//HSSFCell cell = row.createCell((short)(xlsIndex));
					HSSFCell cell = row.createCell((int)(xlsIndex));
					//jp Refactor end
					//cell.setEncoding((short)1); //SRG POI 3.7 Upgrade
					
					if(column.getAttributeType()== BioAttributeTypes.DATE_TYPE){
						//Saving dates as date types
						
						if(rawValues[i]==null || formattedValues[i]=="")
							cell.setCellValue("");
						else{
							
							
							cell.setCellValue(((GregorianCalendar)rawValues[i]).getTime());
							cell.setCellStyle(dateCellStyle);
						}
						
					}
					else
						cell.setCellValue(format(formattedValues[i]));
					
					
					
				}//Map DIRECT    			
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private String format(String s)
	{
		if(s == null || s == "")
			return "";
		else
			return StringAlgorithms.replace(s, "\r\n", "\n");
	}
	
	public void postProcessData()
	{
		try
		{
			workbook.write(out);
			out.flush();
		}
		catch(IOException ioe)
		{
			_logger.error("EXP_EXCEL_POST_PROCESS", "Error post processing excel export", ioe);
		}
	}
	
	//private ColumnInfo[] filterColumnInfosWithTemplate(ColumnInfo[] columnInfos, boolean masterFlag){
	private TemplateColumnList filterColumnInfosWithTemplate(ColumnInfo[] columnInfos, boolean masterFlag){
		List<DbColumn> templateColumns = getColumnDescriptions(workbook, masterFlag);
		
		List<DbColumn> templateOnlyColumns = new ArrayList<DbColumn>();
		
		ArrayList filteredColumns = new ArrayList();
		
		if (templateColumns==null || templateColumns.size()==0){
			//return columnInfos;
			return new TemplateColumnList(columnInfos, templateOnlyColumns, templateColumns);
		}
		for(Iterator itr = templateColumns.iterator(); itr.hasNext();){
			DbColumn dbColumn = (DbColumn)itr.next();
			String column = dbColumn.getName();
			
			//_log.debug("LOG_SYSTEM_OUT","filterColumnInfosWithTemplate::looking for:"+column,100L);
			boolean found = false;
			for(int i=0; i < columnInfos.length; i++){
				ColumnInfoExtended columnInfoExtended = (ColumnInfoExtended)columnInfos[i];
				if (column.compareToIgnoreCase(columnInfoExtended.getAttributeName())==0){
					
					//_log.debug("LOG_SYSTEM_OUT","filterCOlumnInfosWithTemplate:found a match:"+columnInfoExtended.getAttributeName(),100L);
					
					found=true;
					columnInfoExtended.setDescription(dbColumn.getDescription());
					filteredColumns.add(columnInfoExtended);
					break;
				}
			}//end for
			
			//Save template columns not found in BIO in List
			if(!found){
				_log.debug("LOG_SYSTEM_OUT","NOTFOUND:"+dbColumn.getName(),100L);
				templateOnlyColumns.add(dbColumn);
			}
			
		}//end iterator
		
		if (filteredColumns.size()>0)
			columnInfos = (ColumnInfoExtended[])filteredColumns.toArray(new ColumnInfoExtended[0]);
		
		TemplateColumnList templateColumnList  = new TemplateColumnList(columnInfos, 
					templateOnlyColumns, templateColumns);
		//return columnInfos;
		return templateColumnList;
	}
	
	private List<DbColumn> getColumnDescriptions(){
		
		String filename = getTemplateFilename(this.template);
		
		List<DbColumn> columns = new ArrayList<DbColumn>();
		
		InputStream is = null;
		
		try{
			is = new FileInputStream(filename);
			POIFSFileSystem fs = new POIFSFileSystem(is);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			
			HSSFSheet sheet = wb.getSheet(DATASHEET);
			if (sheet==null){
				
				_log.debug("LOG_SYSTEM_OUT","Sheet not found:"+DATASHEET,100L);
				return null;
			}else{
				
				_log.debug("LOG_SYSTEM_OUT","EXPORTHELPEREXTENDED::Sheet found",100L);
				
				HSSFRow headerRow = sheet.getRow(MASTERHEADERROW);
				HSSFRow descRow = sheet.getRow(MASTERHEADERDESCROW);
				
				if (headerRow==null || descRow==null){
					return null;
				}
				
				for(int i=0; i < headerRow.getPhysicalNumberOfCells(); i++){
					//HSSFCell headerCell = headerRow.getCell((short)i); //SRG POI 3.7 Upgrade
					//HSSFCell descCell = descRow.getCell((short)i);
					HSSFCell headerCell = headerRow.getCell((int)i); //SRG POI 3.7 Upgrade
					HSSFCell descCell = descRow.getCell((int)i);
					
					
					if (headerCell!=null && 
							headerCell.getCellType()==HSSFCell.CELL_TYPE_STRING &&
							descCell != null &&
							descCell.getCellType() == HSSFCell.CELL_TYPE_STRING)
					{
						
						
						String column = headerCell.getStringCellValue();
						String desc = descCell.getStringCellValue();
						
						
						if(column.trim().length()>0 && desc.trim().length()>0){
							columns.add(new DbColumn(column, desc));
						}
						
					}
				}//end for
			}
			
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(is!=null){
				try{
					is.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		
		return columns;
	}
	
	private List<DbColumn> getColumnDescriptions(HSSFWorkbook wb, boolean masterFlag){
		
		
		List<DbColumn> columns = new ArrayList<DbColumn>();
		
		
		try{
			
			if (wb==null){
				_log.debug("LOG_SYSTEM_OUT","Workbook is null",100L);
				return null;
			}
			
			String sheetName = masterFlag ? DATASHEET : DETAILDATASHEET;
			int headerRowNumber = masterFlag ? MASTERHEADERROW : DETAILHEADERROW;
			int headerDescRowNumber = masterFlag ?  MASTERHEADERDESCROW : DETAILHEADERDESCROW;
			
			
			HSSFSheet sheet = wb.getSheet(sheetName);
			if (sheet==null){
				
				_log.debug("LOG_SYSTEM_OUT","Sheet not found:"+sheetName,100L);
				return null;
			}else{
				
				_log.debug("LOG_SYSTEM_OUT","[ExportToXlsExtendedImpl]::Sheet found",100L);
				
				HSSFRow headerRow = sheet.getRow(headerRowNumber);
				HSSFRow descRow = sheet.getRow(headerDescRowNumber);
				
				if (headerRow==null || descRow==null){
					//_log.debug("LOG_SYSTEM_OUT","Header or Descrow is null.",100L);
					return null;
				}
				
				for(int i=0; i < headerRow.getPhysicalNumberOfCells(); i++){
					//HSSFCell headerCell = headerRow.getCell((short)i); //SRG POI 3.7 Upgrade
					//HSSFCell descCell = descRow.getCell((short)i);
					HSSFCell headerCell = headerRow.getCell((int)i); //SRG POI 3.7 Upgrade
					HSSFCell descCell = descRow.getCell((int)i);
					
					//_log.debug("LOG_SYSTEM_OUT","ExportHelperExtended::Reading cell number :"+i,100L);
					
					if (headerCell!=null && 
							headerCell.getCellType()==HSSFCell.CELL_TYPE_STRING &&
							descCell != null &&
							descCell.getCellType() == HSSFCell.CELL_TYPE_STRING)
					{
						
						
						String column = headerCell.getStringCellValue();
						String desc = descCell.getStringCellValue();
						
						//_log.debug("LOG_SYSTEM_OUT","ExportHelperExtended::column::"+column+" desc:"+desc,100L);
						
						if(column.trim().length()>0 && desc.trim().length()>0){
							columns.add(new DbColumn(column, desc));
						}
						
					}
				}//end for
			}
			
			
		}catch(RuntimeException e){
			e.printStackTrace();
		}        	
		
		_log.debug("LOG_SYSTEM_OUT","[ExportToXlsExtendedImpl]::columns size::"+columns.size(),100L);
		
		return columns;
		
	}
	private void exportBioInternal(Bio bio, SheetWrapper sheetWrapper, ColumnInfo[] columnInfos, 
			boolean masterFlag, List<DbColumn> templateOnlyColumns,
			List<DbColumn> templateColumns)
	{
		Object rawValues[] = new Object[columnInfos.length];
		String formattedValues[] = new String[columnInfos.length];
		BioAttributeFormatter bioAttributeFormatter = state.getBioAttributeFormatter();
		for(int i = 0; i < columnInfos.length; i++)
			try
		{
				ColumnInfo column = columnInfos[i];
				rawValues[i] = bio.get(column.getAttributeName());
				if(rawValues[i] == null)
					continue;
				if(column.isWidgetDropdown())
				{
					formattedValues[i] = column.getWidgetLabel(rawValues[i].toString());
					continue;
				}
				if(column.isWidgetChecked())
				{
					formattedValues[i] = "0".equals(rawValues[i].toString().trim()) ? "No" : "Yes";
					continue;
				}
				if(column.getAttributeMetadata() == null)
				{
					formattedValues[i] = rawValues[i].toString();
					continue;
				}
				if(column.hasAttributeDomain())
				{
					
					//_log.debug("LOG_SYSTEM_OUT","\t\tATTRIBUTENAME:"+column.getAttributeName(),100L);
					//BioAttributeMetadata attributeMetadata = column.getAttributeMetadata();
					//_log.debug("LOG_SYSTEM_OUT","\t\tAttribute Domain Name:" +attributeMetadata.getAttributeDomainName(),100L);
					//_log.debug("LOG_SYSTEM_OUT","\t\tcolumn value:"+(String)rawValues[i],100L);
					//formattedValues[i] = (String)bio.getLabel((String)rawValues[i]);
					formattedValues[i] = rawValues[i].toString();
					continue;
				}
				if(bioAttributeFormatter != null && column.getAttributeType() != 1)
				{
					String ldtat = BioAttributeFormatter.getLocalizedDataTypeAttributeTypeName(column.getBioType(), column.getAttributeName());
					Object ldt;
					ldt = ldtat == null ? null : (ldt = bio.get(ldtat));
					//jp.answerlink.277082.begin
					setCurrencyLocale(column);
					//jp.answerlink.277082.end

					formattedValues[i] = bioAttributeFormatter.format(rawValues[i], column.getBioType(), column.getAttributeName(), column.getWidget(), state, ldt);
					continue;
				}
				if(column.getAttributeType() == 6)
					formattedValues[i] = getDateFormat().format(((GregorianCalendar)rawValues[i]).getTime());
				else
					formattedValues[i] = rawValues[i].toString();
		}
		catch(EpiException e)
		{
			_logger.error("EXP_EXPORT_BIO", "Error exporting Bio", e);
		}
		
		
		processData(bio, rawValues, formattedValues, sheetWrapper, columnInfos, masterFlag, 
				templateOnlyColumns, templateColumns);
	}
	
	//jp.answerlink.277082.begin
	private void setCurrencyLocale(ColumnInfo column){
		String bioType = column.getBioType();
		String attributeName = column.getAttributeName();
		AttributeType at = MetaDataAccess.getInstance().getBioAttributeType(bioType, attributeName);
	    int type = at.getAttributeType();
	    EpnyLocale epnyLocale = at.getCurrencyLocale();
	    if(epnyLocale==null && (type == BIO_ATTRIBUTE_TYPE_DECIMAL || type == BIO_ATTRIBUTE_TYPE_FLOAT)){
	    	Locale locale = Locale.getDefault();
	    	EpnyLocale systemLocale = Metadata.getInstance().getLocale(locale.getLanguage()+"_"+locale.getCountry());
	    	
	    	//EpnyLocale systemLocale = Metadata.getInstance().getLocale("en_GB");
	    	_logger.debug("JPDEBUG","jpdebug:system locale:"+systemLocale.getName(),100L);
	    	at.setCurrencyLocale(systemLocale);
	    }
	   
	}
	//jp.answerlink.277082.end
	

	private class DbColumn{
		
		private String name = null;
		private String description = null;
		
		DbColumn(String _name, String _description){
			name = _name;
			description = _description;
		}
		
		protected String getName(){
			return name;
		}
		
		protected String getDescription(){
			return description;
		}

		@Override
		public boolean equals(Object obj) {
			if(obj instanceof DbColumn){
				DbColumn tmpDbColumn=(DbColumn) obj;
				return this.name.equalsIgnoreCase(tmpDbColumn.name);
			}else
				return false;
			
		}
		
		
	}
	
	private class SheetWrapper{
		private HSSFSheet sheet;
		private int rowNumber=0;
		
		//static int detailRowCount ;
		
		SheetWrapper(HSSFSheet _sheet, int _rowNumber){
			sheet = _sheet;
			rowNumber = _rowNumber;
		}
		
		protected int getRowNumber() {
			return rowNumber;
		}
		
		protected void setRowNumber(int rowNumber) {
			this.rowNumber = rowNumber;
		}
		
		protected HSSFSheet getSheet() {
			return sheet;
		}
		
		protected void setSheet(HSSFSheet sheet) {
			this.sheet = sheet;
		}
		
		protected void incrementRowNumber(){
			this.rowNumber++;
		}
		
	}
	
	private class TemplateColumnList {
		private ColumnInfo[] columnInfos;
		private List<DbColumn> templateOnlyColumns;
		
		private List<DbColumn> templateColumns;
		
		private TemplateColumnList(){
			
		}

		protected TemplateColumnList(ColumnInfo[] _columnInfos, 
				List<DbColumn> _templateOnlyColumns,
				List<DbColumn> _templateColumns){
			columnInfos = _columnInfos;
			templateOnlyColumns = _templateOnlyColumns;
			templateColumns = _templateColumns;
		}
		protected ColumnInfo[] getColumnInfos() {
			return columnInfos;
		}

		protected void setColumnInfos(ColumnInfoExtended[] columnInfos) {
			this.columnInfos = columnInfos;
		}

		protected List<DbColumn> getTemplateOnlyColumns() {
			return templateOnlyColumns;
		}

		protected void setTemplateOnlyColumns(List<DbColumn> templateOnlyColumns) {
			this.templateOnlyColumns = templateOnlyColumns;
		}

		protected List<DbColumn> getTemplateColumns() {
			return templateColumns;
		}

		protected void setTemplateColumns(List<DbColumn> templateColumns) {
			this.templateColumns = templateColumns;
		}
		
	}
	
	//Class end
	
}
