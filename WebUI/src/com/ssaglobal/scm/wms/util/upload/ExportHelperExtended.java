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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.epiphany.shr.data.beans.BioService;
import com.epiphany.shr.data.beans.ejb.BioServiceUtil;
import com.epiphany.shr.data.bio.Bio;
import com.epiphany.shr.data.bio.BioAttributeMetadata;
import com.epiphany.shr.data.bio.BioCollection;
import com.epiphany.shr.data.bio.BioMappingTypes;
import com.epiphany.shr.data.bio.UnitOfWork;
import com.epiphany.shr.data.dp.sql.SQLDPConnectionFactory;
import com.epiphany.shr.data.error.EpiDataException;
import com.epiphany.shr.ui.state.EpnyControllerState;
import com.epiphany.shr.ui.util.export.ColumnInfo;
import com.epiphany.shr.ui.util.export.ExportHelper;
import com.epiphany.shr.ui.view.RuntimeListForm;
import com.epiphany.shr.util.exceptions.EpiException;
import com.epiphany.shr.util.logging.ILoggerCategory;
import com.epiphany.shr.util.logging.LoggerFactory;
import com.ssaglobal.SsaAccessBase;

public class ExportHelperExtended extends ExportHelper{

	protected static ILoggerCategory _log = LoggerFactory.getInstance(ExportHelperExtended.class);
	public ExportHelperExtended(EpnyControllerState state) {
		super(state);

	}
	
    public ExportHelperExtended(EpnyControllerState state, RuntimeListForm listForm)
    {
        super(state);
        this.setColumnInfos(null);
        //initializeColumnInfos(listForm);
        
    }

    public ExportHelperExtended(EpnyControllerState state, RuntimeListForm listForm, String template)
    {
        super(state);
        this.setColumnInfos(null);
        this.template = template;
        
    }

    public void exportBioCollection(BioCollection bioCollection)
    {

    	try{
    		Bio firstBio = bioCollection.elementAt(0);
    		initializeColumnInfos(xform(firstBio));
    		filterColumnInfosWithTemplate();
    		setColumnInfos(helperColumnInfos);

    		super.exportBioCollection(bioCollection);
    		
    	}catch(EpiDataException e){
    		_logger.error("EXP_EXPORT_BIO_COL", "Error exporting BioCollection", e);
    		e.printStackTrace();
    	}
    	
    	/***
        long start;
        BioService bioSvc;
        start = System.currentTimeMillis();
        bioSvc = null;
        int size;
        size = bioCollection == null ? 0 : bioCollection.size();
        if(size == 0)
            return;
        try
        {
            Bio firstBio = bioCollection.elementAt(0);
            initializeColumnInfos(xform(firstBio));
            fireProcessHeader();
            firePreProcessData(size);
            for(int i = 0; i < size; i++)
            {
                Bio bio = bioCollection.elementAt(i);
                exportBioInternal(xform(bio));
            }

            firePostProcessData();
        }
        catch(EpiException e)
        {
            _logger.error("EXP_EXPORT_BIO_COL", "Error exporting BioCollection", e);
        }
        finally{
        cleanup(bioSvc);
        long end = System.currentTimeMillis();
        	_logger.debug("LOG_EXPORT_TIME", "Time for preparing export: {0}", 268435456L, new Long(end - start));
        }
        **/
    }

    
    private void initializeColumnInfos(Bio bio)
    {
        Map attributes = getBioAttributes(bio);
        List columns = new ArrayList();
        
        if(helperColumnInfos != null)
        {
            for(int i = 0; i < helperColumnInfos.length; i++)
            {
                BioAttributeMetadata attributeMetadata = (BioAttributeMetadata)attributes.get(helperColumnInfos[i].getAttributeName());
                helperColumnInfos[i].setAttributeMetadata(attributeMetadata);
            }

        } else
        {
            for(Iterator iter = attributes.keySet().iterator(); iter.hasNext();)
            {
                BioAttributeMetadata attributeMetadata = (BioAttributeMetadata)attributes.get(iter.next());
                if(attributeMetadata.getMappingType() == BioMappingTypes.MAP_DIRECT){
                	//columns.add(new ColumnInfo(attributeMetadata));
                	columns.add(new ColumnInfoExtended(attributeMetadata));
                }
            }

            //helperColumnInfos = (ColumnInfo[])columns.toArray(new ColumnInfo[0]);
            helperColumnInfos = (ColumnInfoExtended[])columns.toArray(new ColumnInfoExtended[0]);
        }
    }

    /***
    private void initializeColumnInfosBAK(Bio bio)
    {
        Map attributes = getBioAttributes(bio);
        if(helperColumnInfos != null)
        {
            for(int i = 0; i < helperColumnInfos.length; i++)
            {
                BioAttributeMetadata attributeMetadata = (BioAttributeMetadata)attributes.get(helperColumnInfos[i].getAttributeName());
                helperColumnInfos[i].setAttributeMetadata(attributeMetadata);
            }

        } else
        {
            helperColumnInfos = new ColumnInfo[attributes.size()];
            
            int i = 0;
            for(Iterator iter = attributes.keySet().iterator(); iter.hasNext();)
            {
                BioAttributeMetadata attributeMetadata = (BioAttributeMetadata)attributes.get(iter.next());
                helperColumnInfos[i] = new ColumnInfo(attributeMetadata);
                i++;
            }

        }
    }
**/
    
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

    private Bio xform(Bio bio)
    {
        if(getTransformationName() == null)
            return bio;
        else
            return Bio2Bio(bio, getTransformationName());
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

    /**
     * Removes from BIO columns notfound in XLS template and 
     * replaces contents in helperColumnInfos array
     *
     */
    private void filterColumnInfosWithTemplate(){
    	//HashMap templateColumns = getColumnDescriptions();
    	List templateColumns = getColumnDescriptions();
    	
    	
    	ArrayList filteredColumns = new ArrayList();
    	
    	if (templateColumns==null || templateColumns.size()==0)
    		return;
    	
    	//Set columns = templateColumns.keySet();
    	
    	//_log.debug("LOG_SYSTEM_OUT","filterColumnInfosWithTemplate:helperColumnInfos.size"+helperColumnInfos.length,100L);
    	
    	//for(Iterator itr = columns.iterator(); itr.hasNext();){
    	for(Iterator itr = templateColumns.iterator(); itr.hasNext();){
    		//String column = (String)itr.next();
    		DbColumn dbColumn = (DbColumn)itr.next();
    		String column = dbColumn.getName();
    		
    		//_log.debug("LOG_SYSTEM_OUT","filterColumnInfosWithTemplate::looking for:"+column,100L);
    		
    		for(int i=0; i < helperColumnInfos.length; i++){
    			ColumnInfoExtended columnInfoExtended = (ColumnInfoExtended)helperColumnInfos[i];
    			if (column.compareToIgnoreCase(columnInfoExtended.getAttributeName())==0){

    				//_log.debug("LOG_SYSTEM_OUT","filterCOlumnInfosWithTemplate:found a match:"+columnInfoExtended.getAttributeName(),100L);

    				//columnInfoExtended.setDescription((String)templateColumns.get(column));
    				columnInfoExtended.setDescription(dbColumn.getDescription());
    				filteredColumns.add(columnInfoExtended);
    				break;
    			}
    		}//end for
    		
    		
    	}//end iterator
    	
    	if (filteredColumns.size()>0)
    		this.helperColumnInfos = (ColumnInfoExtended[])filteredColumns.toArray(new ColumnInfoExtended[0]);
    	
    	
    }
    
    /***
     * Reads the template and saves ColumnNames/Description pairs in a HashMap
     * @return
     */
    private List getColumnDescriptions(){
    	
    	String filename = getTemplateFilename(this.template);
    	
    	//HashMap columns = new HashMap();
    	List columns = new ArrayList();
    	
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
    						//columns.put(column, desc);
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
    	
    	
    	//_log.debug("LOG_SYSTEM_OUT","ExportHelperExtended::columns size::"+columns.size(),100L);
    	
    	return columns;
    }

    public static String getTemplateFilename(String template){
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

    	//jp.sdis.7212.begin
    	//filename = filename.toLowerCase();
    	//jp.sdis.7212.end

    	_log.debug("LOG_SYSTEM_OUT","[ExportHelperExtended]template:"+filename,100L);
    	return filename;
    }
    
    public static String formatPath(String path){
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
    }
    
    private static final String DATASHEET = "Data";
    private static final int MASTERHEADERROW = 0; //Header row with column names
    private static final int MASTERHEADERDESCROW = 1; //Description row
    private static ILoggerCategory _logger;
    private ColumnInfoExtended helperColumnInfos[];
    private String template = null;
    static 
    {
        _logger = LoggerFactory.getInstance(com.epiphany.shr.ui.util.export.ExportHelper.class);
    }

    
}
