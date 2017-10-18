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

import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioAttributeMetadata;
import com.epiphany.shr.data.bio.BioAttributeTypes;
import com.epiphany.shr.data.bio.BioMappingTypes;
import com.epiphany.shr.ui.util.export.ColumnInfo;
import com.epiphany.shr.ui.util.export.ExportHandler;
import com.epiphany.shr.util.algorithm.StringAlgorithms;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;


// Referenced classes of package com.epiphany.shr.ui.util.export:
//            ColumnInfo, ExportHandler

public class ExcelExportHandlerExtended
    implements ExportHandler
{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ExcelExportHandlerExtended.class);
	public ExcelExportHandlerExtended(OutputStream out, String templateFilename){

		this(out);
		this.templateFilename = templateFilename;

		
		//Loads template into memory
		ByteArrayOutputStream baos = copyFileToBAOS(templateFilename);
		
		try{
			POIFSFileSystem poiInput = new POIFSFileSystem(new ByteArrayInputStream(baos.toByteArray()));
	
			this.workbook = new HSSFWorkbook(poiInput);

			dateCellStyle = workbook.createCellStyle();
			dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(POIDATEFORMAT));

			_log.debug("LOG_SYSTEM_OUT","[ExcelExportExtended]Loaded template into memory",100L);
		}catch(IOException e){
			e.printStackTrace();
		}
        
		getDataSheet();	
	}
	
    public ExcelExportHandlerExtended(OutputStream out)
    {
        columnInfos = new ColumnInfo[0];
        workbook = new HSSFWorkbook();
        sheetNumber = 0;
        rowNumber = 0;
        this.out = out;
        //addSheet();

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

    private void initialize(){
    	
    }
    private void addSheet()
    {
        //sheet = workbook.createSheet("Sheet" + (sheetNumber + 1));
    	sheet = workbook.createSheet(DATASHEET);
    	sheetNumber++;
        rowNumber = 0;
    }
    
    /**
     * Searches for DATASHEET in loaded template in memory
     *
     */
    private void getDataSheet(){
    	
    	sheet = workbook.getSheet(DATASHEET);
    	sheetNumber++;
        
    	
    	if (sheet ==null){
    		_log.debug("LOG_SYSTEM_OUT","Missing :"+DATASHEET +" sheet on template file.",100L);
    	}else{
    		rowNumber = DATASHEETSTARTROW;
    		_log.debug("LOG_SYSTEM_OUT","[ExcelExportHandlerExtended]Found Datasheet in template. First row position:"+rowNumber,100L);
    	}
    }
    
    private void addDefaultFormatSheet()
    {
    
    	sheet = workbook.createSheet(FORMATSHEET);
    	rowNumber = 0;
    	
    	HSSFRow row = sheet.createRow(rowNumber++);
    	//HSSFCell cell = row.createCell((short)0); //SRG POI 3.7 Upgrade
    	HSSFCell cell = row.createCell((int)0);
    	cell.setCellValue("Date Format");
    	//HSSFCell cellDateFormat = row.createCell((short)1);
    	HSSFCell cellDateFormat = row.createCell((int)1);
    	cellDateFormat.setCellValue(DATEFORMAT);
    }

    
    private void addFormatSheet(){
    	
    	HSSFSheet targetSheet = workbook.createSheet(FORMATSHEET);
    	
    	InputStream is = null;
    	
    	try{
    		is = new FileInputStream(this.templateFilename);
    		POIFSFileSystem fs = new POIFSFileSystem(is);
    		HSSFWorkbook srcWorkbook = new HSSFWorkbook(fs);
    		
    		HSSFSheet formatSheet = srcWorkbook.getSheet(FORMATSHEET);
    		if (formatSheet==null){
    			
    			_log.debug("LOG_SYSTEM_OUT","Sheet not found:"+FORMATSHEET+". Adding default FORMATSHEET.",100L);
    			addDefaultFormatSheet();
    			return;
    		}else{
    			
    			_log.debug("LOG_SYSTEM_OUT","EXPORTHELPEREXTENDED::Format Sheet found",100L);
    			
    			for(Iterator itr = formatSheet.rowIterator(); itr.hasNext();){
    				HSSFRow srcRow = (HSSFRow) itr.next();
    				if(srcRow==null)
    					continue;
    				
    				
    				//copyRow(targetSheet, srcRow, POIDATEFORMAT );
    				copyRow(targetSheet, srcRow );
    				
    			}
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
    	
    	
    }
    
    
    private void copyRow(HSSFSheet targetSheet, HSSFRow srcRow){
		
		
		//short rownum = (new Integer(targetSheet.getPhysicalNumberOfRows())).shortValue();
		short rownum = (short)targetSheet.getPhysicalNumberOfRows();
		HSSFRow dstRow  = targetSheet.createRow(rownum);
		short numCells = (short)srcRow.getPhysicalNumberOfCells();

	

		short i = 0;

		for(Iterator itr = srcRow.cellIterator(); itr.hasNext();){
			
			try{

				HSSFCell srcCell = (HSSFCell)itr.next();
				//HSSFCell dstCell = dstRow.createCell(srcCell.getCellNum()); //SRG POI 3.7 Upgrade
				HSSFCell dstCell = dstRow.createCell(srcCell.getColumnIndex());
				
				if (srcCell==null){
					dstCell.setCellValue("");
					continue;
				}
				
				if (srcCell.getCellType()==HSSFCell.CELL_TYPE_STRING){
					dstCell.setCellValue(srcCell.getStringCellValue());
				}else if(srcCell.getCellType()==HSSFCell.CELL_TYPE_BOOLEAN){
					dstCell.setCellValue(srcCell.getBooleanCellValue());
					
				}else if (HSSFDateUtil.isCellDateFormatted(srcCell)) {
					Date date =srcCell.getDateCellValue();
					


					dstCell.setCellValue(date);
					dstCell.setCellStyle(dateCellStyle);
					
					
				}else if (srcCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
					dstCell.setCellValue(srcCell.getNumericCellValue());
					
				}else if (srcCell.getCellType()==HSSFCell.CELL_TYPE_BLANK){
					dstCell.setCellValue("");
				}else
					dstCell.setCellValue("ERROR");
				
			}catch(Exception e){
				e.printStackTrace();
				
			}
			
		}
	
		
	}

    private void copyRow(HSSFSheet targetSheet, HSSFRow srcRow, String dateFormat){
		
		
		//short rownum = (new Integer(targetSheet.getPhysicalNumberOfRows())).shortValue();
		short rownum = (short)targetSheet.getPhysicalNumberOfRows();
		HSSFRow dstRow  = targetSheet.createRow(rownum);
		short numCells = (short)srcRow.getPhysicalNumberOfCells();
		
		if (dateFormat==null){
			//dateFormat ="m/d/yy h:mm";
			dateFormat = POIDATEFORMAT;
		}

		short i = 0;
		//for (i = 0 ; i < numCells;i++){
		for(Iterator itr = srcRow.cellIterator(); itr.hasNext();){
			
			try{
				//HSSFCell srcCell = srcRow.getCell(i);
				HSSFCell srcCell = (HSSFCell)itr.next();
				//HSSFCell dstCell = dstRow.createCell(i);
				//HSSFCell dstCell = dstRow.createCell(srcCell.getCellNum()); //SRG POI 3.7 Upgrade
				HSSFCell dstCell = dstRow.createCell(srcCell.getColumnIndex());
				
				if (srcCell==null){
					dstCell.setCellValue("");
					continue;
				}
				
				if (srcCell.getCellType()==HSSFCell.CELL_TYPE_STRING){
					dstCell.setCellValue(srcCell.getStringCellValue());
				}else if(srcCell.getCellType()==HSSFCell.CELL_TYPE_BOOLEAN){
					dstCell.setCellValue(srcCell.getBooleanCellValue());
					
				}else if (HSSFDateUtil.isCellDateFormatted(srcCell)) {
					Date date =srcCell.getDateCellValue();
					
					HSSFCellStyle cellStyle = workbook.createCellStyle();
					cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(dateFormat));
					dstCell.setCellValue(date);
					dstCell.setCellStyle(cellStyle);
					
					
				}else if (srcCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
					dstCell.setCellValue(srcCell.getNumericCellValue());
					
				}else if (srcCell.getCellType()==HSSFCell.CELL_TYPE_BLANK){
					dstCell.setCellValue("");
				}else
					dstCell.setCellValue("ERROR");
				
			}catch(Exception e){
				e.printStackTrace();
				
			}
			
		}
	
		
	}

    public boolean canStreamOutput()
    {
        return false;
    }

    public void setOutputStream(OutputStream outputstream)
    {
    }

    public void processHeader(ColumnInfo newColumnInfos[])
    {
    		//Remove not DIRECT mapping attributes
    		//this.columnInfos = columnInfos;
    		this.columnInfos = newColumnInfos;
        
    }

    
    public void processHeaderBAK(ColumnInfo newColumnInfos[])
    {
    	try{
    		
    		//Remove not DIRECT mapping attributes
    		//this.columnInfos = columnInfos;
    		this.columnInfos = newColumnInfos;
    		//this.columnInfos = cleanColumnInfos(newColumnInfos);
    		
    		
    		//Header Columns
    		HSSFRow row = sheet.createRow(rowNumber++);
    		int columnCount = columnInfos.length;
    		
    		
    		//HSSFCell cellMessage = row.createCell((short)0); //SRG POI 3.7 Upgrade
    		HSSFCell cellMessage = row.createCell((int)0);
    		cellMessage.setCellValue("MESSAGES");
    		
    		for(int i = 0; i < columnCount; i++)
    		{
    			String header = columnInfos[i].getAttributeName();
    			//HSSFCell cell = row.createCell((short)(i+1));
    			HSSFCell cell = row.createCell((int)(i+1));
    			cell.setCellValue(header);
    			cell.setCellType(1);
    			cell.setCellStyle(headerCellStyle);
    			//sheet.setColumnWidth((short)(i+1), (short)5000);
    			sheet.setColumnWidth((int)(i+1), (int)5000);
    		}

    		
    		
    		//Descriptive Header 
    		HSSFRow rowDesc = sheet.createRow(rowNumber++);
    		//HSSFCell cellMessageDesc = rowDesc.createCell((short)0); //SRG POI 3.7 Upgrade
    		HSSFCell cellMessageDesc = rowDesc.createCell((int)0);
    		cellMessageDesc.setCellValue("MESSAGES");
    		
    		for(int i = 0; i < columnCount; i++)
    		{
    			//String header = columnInfos[i].getWidgetHeader();
    			
    			String header=null;
    			
    			Object object = columnInfos[i];
    			if (object instanceof ColumnInfoExtended){
    				ColumnInfoExtended columnInfoExtended = (ColumnInfoExtended)columnInfos[i];
    				header = columnInfoExtended.getDescription();
    			}
    			
    			
    			if(header == null)
    				header = columnInfos[i].getAttributeName();
    			
    			//HSSFCell cell = rowDesc.createCell((short)(i+1)); //SRG POI 3.7 Upgrade
    			HSSFCell cell = rowDesc.createCell((int)(i+1));
    			cell.setCellValue(header);
    			cell.setCellType(1);
    			cell.setCellStyle(headerCellStyle);
    			//sheet.setColumnWidth((short)(i+1), (short)5000);
    			sheet.setColumnWidth((int)(i+1), (int)5000);
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
        
    }

    
    /**
     * Remove attributes that are not DIRECT mapping
     * @param columnInfos
     * @return
     */
    private ColumnInfo[] cleanColumnInfos(ColumnInfo[] columnInfos) {
    	ColumnInfo[] newColumnInfos = null;
    	
    	List columns = new ArrayList();
    	
    	if (columnInfos==null)
    		return null;
    	
    	//_log.debug("LOG_SYSTEM_OUT","columnInfo size:"+columnInfos.length,100L);
    	
    	try{
    		for(int i = 0; i < columnInfos.length; i++){
    			ColumnInfo columnInfo = columnInfos[i];
    			BioAttributeMetadata meta = columnInfo.getAttributeMetadata();
    			
    			if(meta.getMappingType() == BioMappingTypes.MAP_DIRECT){
    				columns.add(columnInfo);
    			}
    		}
    		
    		newColumnInfos =(ColumnInfo[])columns.toArray(new ColumnInfo[0]);
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	//_log.debug("LOG_SYSTEM_OUT","columnInfo NEW size:"+newColumnInfos.length,100L);
    	
		return newColumnInfos;
		
	}

	public void preProcessData(int i)
    {
    }

    public void processData(Bio bio, Object rawValues[], String formattedValues[])
    {
    	try{
    		if(rowNumber == MAX_ROWS_PER_SHEET)
    		{
    			//addSheet();
    			//processHeader(columnInfos);
                _logger.error("EXP_EXCEL_POST_PROCESS", "Error processing excel export, exceeded max number of rows per Data sheet", null);
    			return;
    		}

    		HSSFRow row = sheet.createRow(rowNumber++);
    		int columnCount = columnInfos.length;
    		
    		//Message Column
    		//HSSFCell cellMessage = row.createCell((short)0); //SRG POI 3.7 Upgrade
    		HSSFCell cellMessage = row.createCell((int)0);
    		//cellMessage.setEncoding((short)1); //SRG POI 3.7 automatically handles Unicode without forcing the encoding
    		cellMessage.setCellValue("");
    		
    		for(int i = 0; i < columnCount; i++)
    		{

    				
    			ColumnInfo column = columnInfos[i];
    			
    			//_log.debug("LOG_SYSTEM_OUT","[ExportHelperExtended]BIO column:"+column.getAttributeName(),100L);
    			
    			if(column.getAttributeMetadata().getMappingType() == BioMappingTypes.MAP_DIRECT){
        			//HSSFCell cell = row.createCell((short)(i+1));
    				HSSFCell cell = row.createCell((int)(i+1));
        			//cell.setEncoding((short)1); //SRG POI 3.7 automatically handles Unicode without forcing the encoding
    				
    				if(column.getAttributeType()== BioAttributeTypes.DATE_TYPE){
    					//Saving dates as date types
    					
    					//_log.debug("LOG_SYSTEM_OUT","[ExcelExportHandlerExtended]::"+column.getAttributeName(),100L);
    					//_log.debug("LOG_SYSTEM_OUT","[ExcelExportHandlerExtended]::val:"+formattedValues[i]+".",100L);
    					if(rawValues[i]==null || formattedValues[i]=="")
    						cell.setCellValue("");
    					else{
    						
        					//HSSFCellStyle style = workbook.createCellStyle();
        					//style.setDataFormat(HSSFDataFormat.getBuiltinFormat(POIDATEFORMAT));
        				
    						//cell.setCellStyle(style);
    						
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
        	//jp Start
        	//addFormatSheet();
        	//jp End
        	
            workbook.write(out);
            out.flush();
        }
        catch(IOException ioe)
        {
            _logger.error("EXP_EXCEL_POST_PROCESS", "Error post processing excel export", ioe);
        }
    }

	/**
	 * Copy the contents of file to a BinaryArrayOutputStream
	 * 
	 * @param source
	 * @param destination
	 */
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

    
    private static ILoggerCategory _logger;
    protected static int MAX_ROWS_PER_SHEET = 65500;
    protected static String DATASHEET = "Data";
    protected static String FORMATSHEET = "Validations";
    private static final int DATASHEETSTARTROW=2; //Data is written begining at this row , 4
    private static final String POIDATEFORMAT = "m/d/yy h:mm";
    private static final String DATEFORMAT = "MM/dd/yyyy";
    private OutputStream out;
    private ColumnInfo columnInfos[];
    private HSSFWorkbook workbook;
    private HSSFSheet sheet;
    private HSSFCellStyle headerCellStyle;
    private HSSFCellStyle dateCellStyle;
    private int sheetNumber;
    private int rowNumber;
    private String templateFilename;
    private static int COPYBUFFERSIZE = 512;
    
    static 
    {
        _logger = LoggerFactory.getInstance(com.epiphany.shr.ui.util.export.ExcelExportHandler.class);
    }
}
