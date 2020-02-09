/*
 * Object Name : TDGFlatFileSupport.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		12:21:16 PM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author vkrish14
 *
 */
public class TDGFlatFileSupport extends TDGBaseUtil{
	
	private static Logger logger = Logger.getLogger(TdgExcelOperationsUtil.class);
	private static String strClassName = " [ TDGFlatFileSupport ] ";
	private String fqnName;
	
	public Map<String, List<String>> readFile(String originalFilename, InputStream inputStream){
		Map<String, List<String>> mapColumnsvalues = null;
		if (!StringUtils.isEmpty(originalFilename)) {
			if (originalFilename.endsWith(".xlsx") || originalFilename.endsWith(".XLSX")) {
				mapColumnsvalues = readXLSXFile(inputStream);
			} else if (originalFilename.endsWith(".xls") || originalFilename.endsWith(".XLS")) {
				mapColumnsvalues = readXLSFile(inputStream);
			}
		}
		return mapColumnsvalues;
	}
	
	
	public Map<String, List<String>> readXLSXFile(InputStream excelFileToRead){
		String strMethodName = " [ readXLSXFile() ]";
		Map<String, List<String>> listIndexBased = new HashMap<String, List<String>>();
		logger.info(strClassName + strMethodName + " inside of readXLSXFile get method ");
		try {
			if (excelFileToRead != null) {
				@SuppressWarnings("resource")
				XSSFWorkbook xssfWorkbook = new XSSFWorkbook(excelFileToRead);
				if (xssfWorkbook != null) {
					int iNumOfIndexes = xssfWorkbook.getNumberOfSheets();
					for (int i = 0; i < iNumOfIndexes; i++) {
						XSSFSheet sheet = xssfWorkbook.getSheetAt(i);
						String sheetName = xssfWorkbook.getSheetName(i);
						XSSFRow row;
						XSSFCell cell;
						List<String> listColumnValues = new ArrayList<String>();
						for (int j = 0; j < 1; j++) {
							row = sheet.getRow(j);
							if(row != null)
							/*List<String> listColumnValues = new ArrayList<String>();*/
							for (int k = 0; k < row.getLastCellNum() + 1; k++) {
								cell = row.getCell(k);
								if (null != cell) {
									if (j == 0) {
										listColumnValues.add(cell.getStringCellValue()
												.toUpperCase());
									}
								}
							}
						}
						if(!listColumnValues.isEmpty())
						listIndexBased.put(sheetName, listColumnValues);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (excelFileToRead != null) {
				try {
					excelFileToRead.close();
				} catch (IOException e) {
					logger.error("Error occured while close the file ", e);
				}
			}
		}
		
		logger.info(strClassName + strMethodName + " return from readXLSXFile method ");
		return listIndexBased;
	}
	

	public Map<String, List<String>> readFile(){
		InputStream excelFileToRead = null;
		Map<String, List<String>> mapColumnsvalues = null;
		try {
			if (!StringUtils.isEmpty(getFqnName())) {
				if (getFqnName().endsWith(".xlsx")) {
					excelFileToRead = new FileInputStream(getFqnName());
					mapColumnsvalues = readXLSXFile(excelFileToRead);
				} else if (getFqnName().endsWith(".xls")) {
					mapColumnsvalues = readXLSFile(excelFileToRead);
				}
			}
		} catch (FileNotFoundException e) {
			logger.error("File is not exist.... ", e);
		}
		return mapColumnsvalues;
	}

	public Map<String, List<String>> readXLSFile(InputStream excelFileToRead){
		String strMethodName = " [ readXLSFile() ]";
		Map<String, List<String>> listIndexBased = new HashMap<String, List<String>>();
		try {
			if (excelFileToRead != null) {
				@SuppressWarnings("resource")
				HSSFWorkbook wb = new HSSFWorkbook(excelFileToRead);
				if(wb != null){
					int iSheetCount = wb.getNumberOfSheets();
					for(int i =0;i<iSheetCount;i++){
						String strSheetName = wb.getSheetName(i);
						HSSFSheet sheet = wb.getSheetAt(i);
						HSSFRow row;
						HSSFCell cell;
						List<String> listColumnValues = new ArrayList<String>();
						for (int j = 0; j < 1; j++) {
							row = sheet.getRow(j);
							if(row != null)
							for (int k = 0; k < row.getLastCellNum() + 1; k++) {
								cell = row.getCell(k);
								if (null != cell) {
									if (j == 0) {
										listColumnValues.add(cell.getStringCellValue().toUpperCase());
									}
								}
							}
						}
						if(!listColumnValues.isEmpty())
						listIndexBased.put(strSheetName,listColumnValues);
					}
				}				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (excelFileToRead != null) {
				try {
					excelFileToRead.close();
				} catch (IOException e) {
					logger.error("Error occured while close the file ", e);
				}
			}
		}
		
		logger.info(strClassName + strMethodName + " return from readXLSFile method ");
		return listIndexBased;
	}


	public String getFqnName(){
		return fqnName;
	}


	public void setFqnName(String fqnName){
		this.fqnName = fqnName;
	}

}
