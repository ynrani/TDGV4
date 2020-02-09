/*
 * Object Name : TdgCustomDaoImp.java
 * Modification Block
 * ---------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ---------------------------------------------------------------------
 * 	1.	  vkrish14		Jun 15, 2015			NA             Created
 * ---------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.dao.impl;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

import javax.persistence.EntityManager;

import nl.flotsam.xeger.Xeger;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tesda.dao.TdgCustomUpdateDao;
import com.tesda.dao.TdgOperationsDao;
import com.tesda.dao.TdgTemplateDao;
import com.tesda.model.DO.TdgRequestListDO;
import com.tesda.model.DTO.TestDataGenerateDTO;
import com.tesda.util.CSVGenerator;
import com.tesda.util.ExcelUtils;
import com.tesda.util.GenerateRandom;
import com.tesda.util.TdgAutoIncrementUtil;
import com.tesda.util.TdgCentralConstant;
import com.tesda.util.TdgXmlUtil;
import com.tesda.util.ZipUtil;

@Component("tdgCustomUpdateDao")
public class TdgCustomUpdateDaoImp implements TdgCustomUpdateDao{
	private static Logger logger = Logger.getLogger(TdgCustomUpdateDaoImp.class);
	private static String strClassName = " [ TdgCustomUpdateDaoImp ] ";
	private Map<String, List<String>> mapConditionValues = null;
	private StringBuffer strStatusDescription = null;
	private List<Object> lstFindedTables = null;
	//following is added fro flat file generation of excel and csv
	private StringBuffer strFlatFilesPaths = null;
	private long lGeneratedReords =0;
	private String strTempPath = null;
	@Autowired
	TdgOperationsDao tdgOperationsDao;
	@Autowired
	TdgTemplateDao tdgTemplateDao;
	//the following parameter is added for sequence insert from data dictionary which is uploaded as manually
	List<Integer> lstSequence= new ArrayList<Integer>();
	/**
	 * This method is used to get the jdbc template of respective schema
	 */
	SingleConnectionDataSource dataSource = null;
	private Map<String, Vector<String>> mapDependentValues;
	
	List<String> lstGeneratedFiles = new ArrayList<String>();

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public String generateTestData(TestDataGenerateDTO testDataGenenarteDTO,
			EntityManager managerentity){
		String strMethodName = " [ generateTestData() ] ";
		logger.info(strClassName + strMethodName + " inside generateTestData method");
		String strSuccess = TdgCentralConstant.FAILED_MESSAGE;
		strStatusDescription = new StringBuffer();
		lstGeneratedFiles = new ArrayList<String>();
		List<JdbcTemplate> listTemplates = new ArrayList<JdbcTemplate>();
		mapConditionValues = new HashMap<String, List<String>>();
		strFlatFilesPaths = new StringBuffer();
		/**
		 * Support multiple database inject for depends
		 */
		List<String> listURLs = new ArrayList<String>();
		List<String> listUserNames = new ArrayList<String>();
		List<String> listPasswords = new ArrayList<String>();
		List<String> listDateFormates = new ArrayList<String>();
		if (testDataGenenarteDTO.getUrl() != null && testDataGenenarteDTO.getUrl().contains("#")) {
			listURLs.addAll(Arrays.asList(testDataGenenarteDTO.getUrl().split("#")));
		} else {
			listURLs.add(testDataGenenarteDTO.getUrl());
		}
		if (testDataGenenarteDTO.getUsername() != null
				&& testDataGenenarteDTO.getUsername().contains("#")) {
			listUserNames.addAll(Arrays.asList(testDataGenenarteDTO.getUsername().split("#")));
		} else {
			listUserNames.add(testDataGenenarteDTO.getUsername());
		}
		if (testDataGenenarteDTO.getPassword() != null
				&& testDataGenenarteDTO.getPassword().contains("#")) {
			listPasswords.addAll(Arrays.asList(testDataGenenarteDTO.getPassword().split("#")));
		} else {
			listPasswords.add(testDataGenenarteDTO.getPassword());
		}
		if (testDataGenenarteDTO.getDateFormate() != null
				&& testDataGenenarteDTO.getDateFormate().contains("#")) {
			listDateFormates
					.addAll(Arrays.asList(testDataGenenarteDTO.getDateFormate().split("#")));
		} else {
			listDateFormates.add(testDataGenenarteDTO.getDateFormate());
		}
		/**
		 * Map dependent values for reverse engineering to fetch values
		 */
		Map<String, String> mapReverseDependents = new HashMap<String, String>();
		if (testDataGenenarteDTO.getColumnsdepends() != null
				&& testDataGenenarteDTO.getColumnsdepends().contains(";")) {
			String strArrays[] = testDataGenenarteDTO.getColumnsdepends().split(";");
			for (int i = 0; i < strArrays.length; i++) {
				if (strArrays[i] != null && strArrays[i].contains("#")) {
					String strColsArrays[] = strArrays[i].split("#");
					mapReverseDependents.put(strColsArrays[1], strColsArrays[0]);
				}
			}
		}
		if (!listURLs.isEmpty() && !listUserNames.isEmpty() && !listPasswords.isEmpty() ) {
			//added below code for zip file creation
			try{
			/*if(!testDataGenenarteDTO.getGenerationType().equalsIgnoreCase("DB")){
			File mkdir = new File(System.getProperty("java.io.tmpdir")+"/TestDataGenerate"+(new Random().nextInt(100000000)));
			if(mkdir.mkdir()){
				logger.info(strClassName + strMethodName + " temp file path is created....");
				strTempPath = mkdir.getAbsolutePath();
			}
			}*/
			//end
			
			Map<String, Vector<String>> mapDependsColumns = new HashMap<String, Vector<String>>();
			for (int i = 0; i < listURLs.size(); i++) {
				
				String urlTemp = listURLs.get(i);
				String userNameTemp = listUserNames.get(i);
				String passTemp = listPasswords.get(i);
				JdbcTemplate jdbcTemplate = tdgTemplateDao.getTemplate(urlTemp, userNameTemp, passTemp);
				listTemplates.add(jdbcTemplate);
				testDataGenenarteDTO.setDateFormate(listDateFormates.get(i));
				Map<String, Vector<String>> tmpDependsColumns = generateTestForMutlipleDatabasesData(
						testDataGenenarteDTO, managerentity, jdbcTemplate, i, mapDependsColumns,
						mapReverseDependents);
				if (tmpDependsColumns.containsKey(TdgCentralConstant.FAILED_MESSAGE)) {
					strSuccess = TdgCentralConstant.FAILED_MESSAGE;
					break;
				} else {
					mapDependsColumns.putAll(tmpDependsColumns);
				}
				if (i == listURLs.size() - 1
						&& !tmpDependsColumns.containsKey(TdgCentralConstant.FAILED_MESSAGE)) {
					strSuccess = TdgCentralConstant.SUCCESS_MESSAGE;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
			// final insert for trace purpose of the tdg history
			if(testDataGenenarteDTO.getGenerateRecordsCount() <=0)
				testDataGenenarteDTO.setGenerateRecordsCount(lGeneratedReords);
			TdgRequestListDO tdgRequestListDO = new TdgRequestListDO();
			tdgRequestListDO.setReqschemaid(testDataGenenarteDTO.getSchemaId());
			tdgRequestListDO.setUserid(testDataGenenarteDTO.getUserId());
			tdgRequestListDO.setSchemaname(testDataGenenarteDTO.getSchemaname());
			tdgRequestListDO.setUserid(testDataGenenarteDTO.getUserId());
			tdgRequestListDO.setGenerationType(testDataGenenarteDTO.getGenerationType());
			if(strFlatFilesPaths != null){
				tdgRequestListDO.setFlatFilesPath(strFlatFilesPaths.toString());
			}
			if (testDataGenenarteDTO.isDataConditional()) {
				List<String> dataconditionVal = new ArrayList<String>(testDataGenenarteDTO.getDataConditionalTabNames());
				tdgOperationsDao.doDeleteDataConditionalValues(
						dataconditionVal,
						testDataGenenarteDTO.getGenerateRecordsCount(), managerentity);
				Map<String, List<String>> mapInput = testDataGenenarteDTO
						.getMapDictionaryVals();
				if (mapInput != null && !mapInput.isEmpty()) {
					if (mapConditionValues.isEmpty()) {
						mapConditionValues = new HashMap<String, List<String>>();
					}
					for (Map.Entry<String, List<String>> mapEntry : mapInput.entrySet()) {
						if (!mapConditionValues.containsKey(mapEntry.getKey())) {
							List<String> listParams = new ArrayList<String>();
							for (int i = 0; i < testDataGenenarteDTO.getGenerateRecordsCount(); i++) {
								listParams.add(mapEntry.getValue().get(i));
							}
							mapConditionValues.put(mapEntry.getKey(), listParams);
						}
					}
				}
			}
			StringBuffer strBuffer = new StringBuffer();
			if(testDataGenenarteDTO.getRequiredSequenceColumns() != null && !testDataGenenarteDTO.getRequiredSequenceColumns().isEmpty()){
				int iCount = 1;
				for (String strValue : testDataGenenarteDTO.getRequiredSequenceColumns()) {
					strBuffer.append(strValue);
					if (iCount != testDataGenenarteDTO.getRequiredSequenceColumns().size()) {
						strBuffer.append('#');
					}
					iCount++;
				}
			}else{			
			if (!mapConditionValues.isEmpty()) {
				int iCount = 1;
				for (Map.Entry<String, List<String>> mapentry : mapConditionValues.entrySet()) {
					strBuffer.append(mapentry.getKey());
					if (iCount != mapConditionValues.size()) {
						strBuffer.append('#');
					}
					iCount++;
				}
			}			
			}
			tdgRequestListDO.setConditions(strBuffer.toString());
			String strDestinationFolder = "";
			if (TdgCentralConstant.SUCCESS_MESSAGE.equals(strSuccess)) {
				if("INSERT".equals(testDataGenenarteDTO.getPopulationType()))			
			    tdgRequestListDO.setRequestCount(testDataGenenarteDTO.getGenerateRecordsCount());
				else if(!"INSERT".equals(testDataGenenarteDTO.getPopulationType()))
					if(testDataGenenarteDTO.getGenerateRecordsCount() <=0)
				    tdgRequestListDO.setRequestCount(1);
					else
						tdgRequestListDO.setRequestCount(testDataGenenarteDTO.getGenerateRecordsCount());
						
			    if(lstGeneratedFiles.isEmpty()){
			    @SuppressWarnings("rawtypes")
				Vector vct = new Vector();
			    vct.add(testDataGenenarteDTO.getGenerateRecordsCount());
			    vct.addElement(strStatusDescription);
			    
			    tdgRequestListDO.setStatusdescription(TdgCentralConstant.getReplacedValue(TdgCentralConstant.TDG_GENERATE_SUCCESS_MESSAGE, vct));
			    }else{
			    	tdgRequestListDO.setStatusdescription(strStatusDescription.toString());
			    }
			    
			    if(!TdgCentralConstant.SCHEMA_GENERATION_TYPE_DB.equalsIgnoreCase(testDataGenenarteDTO.getGenerationType())){
			 // going to generate the zip file of generated flat files
			    strDestinationFolder = System.getProperty("user.home")+("/Downloads/GeneratedFiles")+(new Random().nextInt(10000000))+".zip";
			    

			    //else{
			    	ZipUtil zipUtil = new ZipUtil(strTempPath,strDestinationFolder);
					zipUtil.generateZipFile(zipUtil);
			    //}
				tdgRequestListDO.setStatusdescription(strDestinationFolder);
			    }
			    tdgRequestListDO.setStatus(TdgCentralConstant.TDG_GENERATE_SUCCESS);
			    
			}else{
				tdgRequestListDO.setRequestCount(0);
				tdgRequestListDO.setStatusdescription(strStatusDescription.toString());
				tdgRequestListDO.setStatus(TdgCentralConstant.TDG_GENERATE_FAILED);
				try{
					for(String strFileNames : lstGeneratedFiles){
						File file = new File(strFileNames);
						if(file.exists() && file.delete())
							logger.info(strClassName + strMethodName
									+ " generated file is deleted ");
					}
				}catch(Exception e){
					logger.error(strClassName + strMethodName
							+ " error occured while delete the files ::  ", e);
				}
				
			}
			
			
			
			//strSuccess = TdgCentralConstant.FAILED_MESSAGE;setStatus<th align="center"  bgcolor="#E3EFFB" scope="col" class="whitefont">Status</th><th align="center"  bgcolor="#E3EFFB" scope="col" class="whitefont">Status</th><td align="left">${tdgRequestListDTOs.status}</td><td align="left">${tdgRequestListDTOs.status}</td>
			if(!"GENERATESAMPLE".equalsIgnoreCase(testDataGenenarteDTO.getTdgFunctionType())){
			String response = tdgOperationsDao.saveDashBoardDetails(tdgRequestListDO,
					mapConditionValues, managerentity);
			strSuccess = strSuccess+"#"+response+"#"+lGeneratedReords;
			
			}else{
				strSuccess = strSuccess+"#"+strDestinationFolder;
			}
			try {
				for (JdbcTemplate template : listTemplates) {
					if (!strSuccess.contains(TdgCentralConstant.SUCCESS_MESSAGE)) {
						template.getDataSource().getConnection().rollback();
					} else {
						template.getDataSource().getConnection().commit();
					}
					tdgTemplateDao.cleanupDataSource(template);
				}
			} catch (SQLException e1) {
				logger.error(strClassName + strMethodName
						+ " error occured while rollback the transaction ::  ", e1);
			}finally{
				if(strTempPath != null){
					File file = new File(strTempPath);
					if(file.exists() && file.delete()){
						logger.info(strClassName + strMethodName
								+ " tmp path of files are deleted");
					}
				}
			}
		}
		return strSuccess;
	}
	
	
	public Map<String, Vector<String>> generateTestForMutlipleDatabasesData(
			TestDataGenerateDTO testDataGenenarteDTO, EntityManager managerentity,
			JdbcTemplate jdbcTemplate, int iCurrentSchema,
			Map<String, Vector<String>> mapInputDepends, Map<String, String> mapReverseDependents){
		String strMethodName = " [ generateTestForMutlipleDatabasesData() ] ";
		String strSuccess = TdgCentralConstant.FAILED_MESSAGE;
		Map<String, Vector<String>> mapDependsColumns = new HashMap<String, Vector<String>>();
		try {
			Map<String, Object> mapObjects = testDataGenenarteDTO.getMapinputData();
			List<String> listColumnNames = new ArrayList<String>();
			/**
			 * The following code is added for multiple databases columns dependent values in
			 * dictionary
			 */
			if (testDataGenenarteDTO.getColumnsdepends() != null
					&& testDataGenenarteDTO.getColumnsdepends().contains(";")) {
				String strArrays[] = testDataGenenarteDTO.getColumnsdepends().split(";");
				for (int i = 0; i < strArrays.length; i++) {
					if (strArrays[i] != null && strArrays[i].contains("#")) {
						String strColsArrays[] = strArrays[i].split("#");
						if (!testDataGenenarteDTO.getMapinputData().containsKey(
								strColsArrays[iCurrentSchema])) {
							mapObjects.put(strColsArrays[iCurrentSchema], "");
						}
						listColumnNames.add(strColsArrays[iCurrentSchema]);
					}
					testDataGenenarteDTO.setMapinputData(mapObjects);
				}
			}
			/**
			 * End the support
			 */
			List<String> lstExistingTables = tdgTemplateDao.getAllTables(jdbcTemplate);
			List<String> listTabNames = new ArrayList<String>();
			if (testDataGenenarteDTO.getDataConditionalTabNames() != null
					&& !testDataGenenarteDTO.getDataConditionalTabNames().isEmpty()) {
				listTabNames.addAll(testDataGenenarteDTO.getDataConditionalTabNames());
			} else {
				listTabNames.addAll(tdgTemplateDao.getTableNamesFromCols(jdbcTemplate,
						testDataGenenarteDTO.getMapinputData()));
				if (testDataGenenarteDTO.getMapinputData() != null
						&& testDataGenenarteDTO.getMapinputData().isEmpty()) {
					listTabNames.addAll(tdgTemplateDao.getTableNamesFromCols(jdbcTemplate,
							testDataGenenarteDTO.getGUIDictionaryColumns()));
				}
			}
			List<List<List<Object>>> listR = new ArrayList<List<List<Object>>>();
			List<List<Object>> listResultVal = new ArrayList<List<Object>>();
			List<String> mapOnetoOnerelation = new ArrayList<String>();
			List<String> listExistVals = new ArrayList<String>();
			lstFindedTables = new ArrayList<Object>();
			for (String strTableName : listTabNames) {
				if (!listExistVals.contains(strTableName) && !lstFindedTables.contains(strTableName) && lstExistingTables != null && lstExistingTables.contains(strTableName)) {
					List<Object> listResult = tdgTemplateDao.generateSequenceOfTables(jdbcTemplate, strTableName,false);
					lstFindedTables.addAll(listResult);
					List<Object> listResultValTemp = tdgTemplateDao.toCheckrelations(listResult, null);
					listResultVal.add(listResultValTemp);
					List<List<Object>> listRTemp = tdgTemplateDao.toCheckrelations(listResultValTemp);
					listR.add(listRTemp);
					for (List<Object> listobj : listRTemp) {
						Collections.reverse(listobj);
						StringBuffer strBuffer = new StringBuffer();
						int i = 0;
						for (Object obj : listobj) {
							i++;
							listExistVals.add(String.valueOf(obj).substring(0,
									String.valueOf(obj).indexOf("#")));
							strBuffer
									.append('\'')
									.append(String.valueOf(obj).substring(0,
											String.valueOf(obj).indexOf("#"))).append('\'');
							if (i != listobj.size()) {
								strBuffer.append(',');
							}
						}
						List<Map<String, Object>> listMapping = tdgTemplateDao.getOntToOneTableInformation(
								jdbcTemplate, strBuffer.toString());
						for (Map<String, Object> mapValues : listMapping) {
							String strValue = String.valueOf(mapValues.get("CONSTRAINT_NAME"));
							String strTable = String.valueOf(mapValues.get("TABLE_NAME"));
							List<Map<String, Object>> listMappingValues = tdgTemplateDao.getConstraintRelationTable(
									jdbcTemplate, strValue, strTable);
							for (Map<String, Object> mapValuesTemp : listMappingValues) {
								mapOnetoOnerelation.add(String.valueOf(
										mapValuesTemp.get("TABLE_NAME")).toUpperCase()
										+ "#" + strTable.toUpperCase());
							}
						}
					}
				}
			}
			if (logger.isDebugEnabled())
				logger.debug(strClassName + strMethodName + " relation found map is "
						+ mapOnetoOnerelation);
			/**
			 * Going to get the master tables list
			 */
			List<String> listMasterTabs = new ArrayList<String>();
			if (testDataGenenarteDTO.getSchemamastertables() != null
					&& testDataGenenarteDTO.getSchemamastertables().contains(";")) {
				String strArrays[] = testDataGenenarteDTO.getSchemamastertables().split(";");
				if (strArrays.length >= 1) {
					if (strArrays[0].contains(",")) {
						String[] strMasterTabs = strArrays[0].split(",");
						for (String strVal : strMasterTabs) {
							listMasterTabs.add(strVal.toUpperCase().trim());
						}
					} else {
						listMasterTabs.add(strArrays[0].toUpperCase().trim());
					}
				}
			}
			Map<String, Object> mapConditions = testDataGenenarteDTO.getMapinputData();
			Map<String, Object> mapConditionsPassed = doRegexOperations(mapConditions, testDataGenenarteDTO);

			Map<String, List<String>> mapDicitionaryVals = testDataGenenarteDTO.getMapDictionaryVals();
			testDataGenenarteDTO.setMapDictionaryVals(doRegexOperationsListObjects(mapDicitionaryVals, testDataGenenarteDTO));
			
			/**
			 * Going for fetching sequence names with tables
			 * later modified for prefix support for other columns also based on constraints types
			 */
			// Map<String, Map<String, String>> mapTableWithSequence = new HashMap<String,
			// Map<String, String>>();
			Map<String, Map<String, Map<String, String>>> mapTableWithSequence = new HashMap<String, Map<String, Map<String, String>>>();
			String str = testDataGenenarteDTO.getSeqtableprefix();
			if (str != null && str.contains(";")) {
				String strArray[] = str.split(";");
				if (strArray != null && strArray.length > 0) {
					for (int i = 0; i < strArray.length; i++) {
						if (strArray[i].contains(",")) {
							// the pattern of the splitting is
							// TableName1,column1,Prefix1,Sequence1;TableName2,column2,Prefix2,Sequence2;
							String strSequenceArray[] = strArray[i].split(",");
							if (strSequenceArray != null && strSequenceArray.length > 0) {
								Map<String, String> map = new HashMap<String, String>();
								Map<String, Map<String, String>> mapColumnWise = new HashMap<String, Map<String, String>>();
								map.put(strSequenceArray[2], strSequenceArray[3]);
								mapColumnWise.put(strSequenceArray[1], map);
								mapTableWithSequence.put(strSequenceArray[0], mapColumnWise);
							}
						}
					}
				}
			}
			List<String> listDataConditionalTabNames = new ArrayList<String>();
			if(testDataGenenarteDTO.isDataConditional())
				listDataConditionalTabNames.addAll(testDataGenenarteDTO
							.getDataConditionalTabNames());
			Map<String, Vector<String>> tmpDependsColumns = generateQueriesAndDumping(
					jdbcTemplate,
					listR,
					mapOnetoOnerelation,
					mapConditionsPassed,
					listResultVal,
					testDataGenenarteDTO.getGenerateRecordsCount(),
					listTabNames,
					listMasterTabs,
					mapTableWithSequence,
					listColumnNames,
					mapInputDepends,
					mapReverseDependents,
					testDataGenenarteDTO.getMapDictionaryVals(),
					testDataGenenarteDTO.isDataConditional(),
					testDataGenenarteDTO.isDataConditional() ? listDataConditionalTabNames : null, testDataGenenarteDTO);
			if (!mapDependsColumns.containsKey(TdgCentralConstant.FAILED_MESSAGE)) {
				mapDependsColumns.putAll(tmpDependsColumns);
			} else {
				Vector<String> vctVals = new Vector<String>();
				mapDependsColumns.put(strSuccess, vctVals);
			}
		} catch (SQLException e) {
			Vector<String> vctVals = new Vector<String>();
			mapDependsColumns.put(TdgCentralConstant.FAILED_MESSAGE, vctVals);
			strStatusDescription = new StringBuffer();
			strStatusDescription.append(e.getErrorCode()).append(" :: ").append(e.getSQLState()).append(" ==> ").append(e.getMessage().substring(0, e.getMessage().length() > 99 ?  100 : e.getMessage().length()));
			logger.error(strClassName + strMethodName
					+ " error occured while dumping the data into server is Sql Exception ::  ", e);
		}catch(StackOverflowError se){
			Vector<String> vctVals = new Vector<String>();
			mapDependsColumns.put(TdgCentralConstant.FAILED_MESSAGE, vctVals);
			strStatusDescription = new StringBuffer();
			strStatusDescription.append(" Caused by ==> ").append(se.getMessage().substring(0, se.getMessage().length() > 99 ?  100 : se.getMessage().length()));
			logger.error(strClassName + strMethodName
					+ " error occured while dumping the data into server  ", se);
		} catch (Exception e2) {
			Vector<String> vctVals = new Vector<String>();
			strStatusDescription = new StringBuffer();
			strStatusDescription.append(" Caused by ==> ").append(e2 != null ? e2.getMessage().substring(0, e2.getMessage().length() > 99 ?  100 : e2.getMessage().length()) : "Passed values not sufficient...");
			mapDependsColumns.put(TdgCentralConstant.FAILED_MESSAGE, vctVals);
			logger.error(strClassName + strMethodName
					+ " error occured while dumping the data into server  ", e2);
		}
		logger.info(strClassName + strMethodName + " return from generateTestData method");
		return mapDependsColumns;
	}
	
	//below code is used as utility for all operations for regex
	private Map<String,Object> doRegexOperations(Map<String, Object> mapConditions,TestDataGenerateDTO testDataGenenarteDTO){
		Map<String, Object> mapConditionsPassed = new HashMap<String, Object>();
		if (mapConditions != null && !mapConditions.isEmpty()) {
			for (Map.Entry<String, Object> mapValue : mapConditions.entrySet()) {
				if (null != mapValue.getValue() && !"".equals(mapValue.getValue())
						&& !"".equals(mapValue.getValue().toString().trim())) {
					if (mapValue.getKey().contains("#")) {
						String strArray[] = mapValue.getKey().split("#");
						for (int ii = 0; ii < strArray.length; ii++) {
							if (mapValue.getValue().toString().contains("#")) {
								String strArrays[] = mapValue.getValue().toString().split("#");
								List<Object> lst = new ArrayList<Object>();
								for (int i2 = 0; i2 < strArrays.length; i2++) {
									lst.add(strArray[i2]);
								}
								mapConditionsPassed.put(mapValue.getKey(), lst);
							} else if(mapValue.getValue().toString().contains(TdgCentralConstant.REGEX)){
								List<Object> lst = new ArrayList<Object>();
								/*if(mapValue.getValue().toString().contains(TdgCentralConstant.LESSER_THAN)){
									//dd/MMM/yyyy hh:mm:ss
									if(mapValue.getValue().toString().contains("/")){
										
										
									}else{
										// number generation for lesser than values
										
									}
								}else if(mapValue.getValue().toString().contains(TdgCentralConstant.GREATER_THAN)){
									
								}else*/ if(mapValue.getValue().toString().contains(TdgCentralConstant.REGEXSEQUENCE)){
									String strFinalString = mapValue.getValue().toString().substring(13);//getValue().toString().substring(13).split(TdgCentralConstant.REGEXSEQUENCE);
									if(mapValue.getValue().toString().contains("/")){											
										long beginTime = Timestamp.valueOf(strFinalString.replaceAll("/", "-")+ " 00:00:00").getTime();
										//long endTime = Timestamp.valueOf(splitValues[1].replaceAll("/", "-")+ " 00:00:00").getTime();
										//long diff = endTime - beginTime +1;
										SimpleDateFormat simpleFormat = new SimpleDateFormat(testDataGenenarteDTO.getDateFormate());// SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
										for(long i =0;i<testDataGenenarteDTO.getGenerateRecordsCount();i++){
											lst.add(simpleFormat.format(new Date(beginTime+(long)(i))));
										}
									}else{
										//commented below code for sequence values generation for all the type of data whether its number,alphabet,alphanumeric formats....
										/*int iHigh = Integer.parseInt(strFinalString);
										//int iLow = Integer.parseInt(splitValues[0]);
										for(long i =0;i<testDataGenenarteDTO.getGenerateRecordsCount();i++){
											lst.add(iHigh+i);
										}*/
										lst.addAll(TdgAutoIncrementUtil.generateAutoIncrementValues(strFinalString, testDataGenenarteDTO.getGenerateRecordsCount()));
									}
								}else if(mapValue.getValue().toString().contains(TdgCentralConstant.BETWEEN_VALULES)){
									String[] splitValues = mapValue.getValue().toString().substring(5).split(TdgCentralConstant.BETWEEN_VALULES);
									//for date between values 
									if(mapValue.getValue().toString().contains("/")){											
										long beginTime = Timestamp.valueOf(splitValues[0].replaceAll("/", "-")+ " 00:00:00").getTime();
										long endTime = Timestamp.valueOf(splitValues[1].replaceAll("/", "-")+ " 00:00:00").getTime();
										long diff = endTime - beginTime +1;
										SimpleDateFormat simpleFormat = new SimpleDateFormat(testDataGenenarteDTO.getDateFormate());// SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
										for(long i =0;i<testDataGenenarteDTO.getGenerateRecordsCount();i++){
											lst.add(simpleFormat.format(new Date(beginTime+(long)(Math.random() * diff))));
										}
									}else{
										int iHigh = Integer.parseInt(splitValues[1]);
										int iLow = Integer.parseInt(splitValues[0]);
										for(long i =0;i<testDataGenenarteDTO.getGenerateRecordsCount();i++){
											lst.add(new Random().nextInt(iHigh - iLow) + iLow);
										}
									}
									
								}else{
								Xeger generator = new Xeger(mapValue.getValue().toString().substring(5));									
								for(long i =0;i<testDataGenenarteDTO.getGenerateRecordsCount();i++){
									lst.add(generator.generate());
								}
								}
								mapConditionsPassed.put(mapValue.getKey(), lst);
							}else {
								mapConditionsPassed.put(strArray[ii], mapValue.getValue());
							}
						}
					} else {
						if (mapValue.getValue().toString().contains("#")) {
							String strArray[] = mapValue.getValue().toString().split("#");
							List<Object> lst = new ArrayList<Object>();
							for (int ii = 0; ii < strArray.length; ii++) {
								lst.add(strArray[ii]);
							}
							mapConditionsPassed.put(mapValue.getKey(), lst);
						} else if(mapValue.getValue().toString().contains(TdgCentralConstant.REGEX)){
							List<Object> lst = new ArrayList<Object>();
							if(mapValue.getValue().toString().contains(TdgCentralConstant.REGEXSEQUENCE)){
								String strFinalString = mapValue.getValue().toString().substring(13);//getValue().toString().substring(13).split(TdgCentralConstant.REGEXSEQUENCE);
								if(mapValue.getValue().toString().contains("/")){											
									long beginTime = Timestamp.valueOf(strFinalString.replaceAll("/", "-")+ " 00:00:00").getTime();
									//long endTime = Timestamp.valueOf(splitValues[1].replaceAll("/", "-")+ " 00:00:00").getTime();
									//long diff = endTime - beginTime +1;
									SimpleDateFormat simpleFormat = new SimpleDateFormat(testDataGenenarteDTO.getDateFormate());// SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
									for(long i =0;i<testDataGenenarteDTO.getGenerateRecordsCount();i++){
										lst.add(simpleFormat.format(new Date(beginTime+(long)(i))));
									}
								}else{
									//commented below code for sequence values generation for all the type of data whether its number,alphabet,alphanumeric formats....
									/*int iHigh = Integer.parseInt(strFinalString);
									//int iLow = Integer.parseInt(splitValues[0]);
									for(long i =0;i<testDataGenenarteDTO.getGenerateRecordsCount();i++){
										lst.add(iHigh+i);
									}*/
									lst.addAll(TdgAutoIncrementUtil.generateAutoIncrementValues(strFinalString, testDataGenenarteDTO.getGenerateRecordsCount()));
								}
							}else if(mapValue.getValue().toString().contains(TdgCentralConstant.BETWEEN_VALULES)){
								String[] splitValues = mapValue.getValue().toString().substring(5).split(TdgCentralConstant.BETWEEN_VALULES);
								//for date between values 
								if(mapValue.getValue().toString().contains("/")){											
									long beginTime = Timestamp.valueOf(splitValues[0].replaceAll("/", "-")+ " 00:00:00").getTime();
									long endTime = Timestamp.valueOf(splitValues[1].replaceAll("/", "-")+ " 00:00:00").getTime();
									long diff = endTime - beginTime +1;
									SimpleDateFormat simpleFormat = new SimpleDateFormat(testDataGenenarteDTO.getDateFormate() != null ? testDataGenenarteDTO.getDateFormate() : "");
									for(long i =0;i<testDataGenenarteDTO.getGenerateRecordsCount();i++){
										lst.add(simpleFormat.format(new Date(beginTime+(long)(Math.random() * diff))));
									}
								}else{
									int iHigh = Integer.parseInt(splitValues[1]);
									int iLow = Integer.parseInt(splitValues[0]);
									for(long i =0;i<testDataGenenarteDTO.getGenerateRecordsCount();i++){
										lst.add(new Random().nextInt(iHigh - iLow) + iLow);
									}
								}
							} else {
								Xeger generator = new Xeger(mapValue.getValue().toString().substring(5));
								for (long i = 0; i < testDataGenenarteDTO.getGenerateRecordsCount(); i++) {
									lst.add(generator.generate());
								}
							}
							mapConditionsPassed.put(mapValue.getKey(), lst);
						}else {
							mapConditionsPassed.put(mapValue.getKey(), mapValue.getValue());
						}
					}
				}
			}
		}
		return mapConditionsPassed;
	}

	//below code is used as utility for all operations for regex
		private Map<String,List<String>> doRegexOperationsListObjects(Map<String, List<String>> mapConditions,TestDataGenerateDTO testDataGenenarteDTO){
			Map<String, List<String>> mapConditionsPassed = new HashMap<String, List<String>>();
			//try{
			if (mapConditions != null && !mapConditions.isEmpty()) {
				for (Map.Entry<String, List<String>> mapValue : mapConditions.entrySet()) {
					try{
					if (null != mapValue.getValue() && mapValue.getValue().get(0) != null && !"".equals(mapValue.getValue().get(0))
							&& !"".equals(mapValue.getValue().get(0).trim())) {
						if (mapValue.getKey().contains("#")) {
							String strArray[] = mapValue.getKey().split("#");
							for (int ii = 0; ii < strArray.length; ii++) {
								if (mapValue.getValue().get(0).contains("#")) {
									String strArrays[] = mapValue.getValue().get(0).split("#");
									List<String> lst = new ArrayList<String>();
									for (int i2 = 0; i2 < strArrays.length; i2++) {
										lst.add(strArray[i2]);
									}
									mapConditionsPassed.put(mapValue.getKey(), lst);
								} else if(mapValue.getValue().get(0).contains(TdgCentralConstant.REGEX)){
									List<String> lst = new ArrayList<String>();
									/*if(mapValue.getValue().toString().contains(TdgCentralConstant.LESSER_THAN)){
										//dd/MMM/yyyy hh:mm:ss
										if(mapValue.getValue().toString().contains("/")){
											
											
										}else{
											// number generation for lesser than values
											
										}
									}else if(mapValue.getValue().toString().contains(TdgCentralConstant.GREATER_THAN)){
										
									}else*/ if(mapValue.getValue().get(0).contains(TdgCentralConstant.REGEXSEQUENCE)){
										String strFinalString = mapValue.getValue().get(0).substring(13);//getValue().toString().substring(13).split(TdgCentralConstant.REGEXSEQUENCE);
										if(mapValue.getValue().get(0).contains("/")){											
											long beginTime = Timestamp.valueOf(strFinalString.replaceAll("/", "-")+ " 00:00:00").getTime();
											//long endTime = Timestamp.valueOf(splitValues[1].replaceAll("/", "-")+ " 00:00:00").getTime();
											//long diff = endTime - beginTime +1;
											SimpleDateFormat simpleFormat = new SimpleDateFormat(testDataGenenarteDTO.getDateFormate());// SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
											for(long i =0;i<testDataGenenarteDTO.getGenerateRecordsCount();i++){
												lst.add(simpleFormat.format(new Date(beginTime+(long)(i))));
											}
										}else{
											//commented below code for sequence values generation for all the type of data whether its number,alphabet,alphanumeric formats....
											/*int iHigh = Integer.parseInt(strFinalString);
											//int iLow = Integer.parseInt(splitValues[0]);
											for(long i =0;i<testDataGenenarteDTO.getGenerateRecordsCount();i++){
												lst.add(iHigh+i);
											}*/
											lst.addAll(TdgAutoIncrementUtil.generateAutoIncrementValues(strFinalString, testDataGenenarteDTO.getGenerateRecordsCount()));
										}
									}else if(mapValue.getValue().get(0).contains(TdgCentralConstant.BETWEEN_VALULES)){
										String[] splitValues = mapValue.getValue().get(0).substring(5).split(TdgCentralConstant.BETWEEN_VALULES);
										//for date between values 
										if(mapValue.getValue().get(0).contains("/")){											
											long beginTime = Timestamp.valueOf(splitValues[0].replaceAll("/", "-")+ " 00:00:00").getTime();
											long endTime = Timestamp.valueOf(splitValues[1].replaceAll("/", "-")+ " 00:00:00").getTime();
											long diff = endTime - beginTime +1;
											SimpleDateFormat simpleFormat = new SimpleDateFormat(testDataGenenarteDTO.getDateFormate());// SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
											for(long i =0;i<testDataGenenarteDTO.getGenerateRecordsCount();i++){
												lst.add(simpleFormat.format(new Date(beginTime+(long)(Math.random() * diff))));
											}
										}else{
											int iHigh = Integer.parseInt(splitValues[1]);
											int iLow = Integer.parseInt(splitValues[0]);
											for(long i =0;i<testDataGenenarteDTO.getGenerateRecordsCount();i++){
												lst.add(new Random().nextInt(iHigh - iLow) + iLow+"");
											}
										}
										
									}else{
										lst.addAll(mapValue.getValue());
									}
									mapConditionsPassed.put(mapValue.getKey(), lst);
								}else {
									mapConditionsPassed.put(strArray[ii], mapValue.getValue());
								}
							}
						} else {
							if (mapValue.getValue().get(0).contains("#")) {
								String strArray[] = mapValue.getValue().get(0).split("#");
								List<String> lst = new ArrayList<String>();
								for (int ii = 0; ii < strArray.length; ii++) {
									lst.add(strArray[ii]);
								}
								mapConditionsPassed.put(mapValue.getKey(), lst);
							} else if(mapValue.getValue().get(0).contains(TdgCentralConstant.REGEX)){
								List<String> lst = new ArrayList<String>();
								if(mapValue.getValue().get(0).contains(TdgCentralConstant.REGEXSEQUENCE)){
									String strFinalString = mapValue.getValue().get(0).substring(13);//getValue().toString().substring(13).split(TdgCentralConstant.REGEXSEQUENCE);
									if(mapValue.getValue().get(0).contains("/")){											
										long beginTime = Timestamp.valueOf(strFinalString.replaceAll("/", "-")+ " 00:00:00").getTime();
										//long endTime = Timestamp.valueOf(splitValues[1].replaceAll("/", "-")+ " 00:00:00").getTime();
										//long diff = endTime - beginTime +1;
										SimpleDateFormat simpleFormat = new SimpleDateFormat(testDataGenenarteDTO.getDateFormate());// SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
										for(long i =0;i<testDataGenenarteDTO.getGenerateRecordsCount();i++){
											lst.add(simpleFormat.format(new Date(beginTime+(long)(i))));
										}
									}else{
										//commented below code for sequence values generation for all the type of data whether its number,alphabet,alphanumeric formats....
										/*int iHigh = Integer.parseInt(strFinalString);
										//int iLow = Integer.parseInt(splitValues[0]);
										for(long i =0;i<testDataGenenarteDTO.getGenerateRecordsCount();i++){
											lst.add(iHigh+i);
										}*/
										lst.addAll(TdgAutoIncrementUtil.generateAutoIncrementValues(strFinalString, testDataGenenarteDTO.getGenerateRecordsCount()));
									}
								}else if(mapValue.getValue().get(0).contains(TdgCentralConstant.BETWEEN_VALULES)){
									String[] splitValues = mapValue.getValue().get(0).substring(5).split(TdgCentralConstant.BETWEEN_VALULES);
									//for date between values 
									if(mapValue.getValue().get(0).contains("/")){											
										long beginTime = Timestamp.valueOf(splitValues[0].replaceAll("/", "-")+ " 00:00:00").getTime();
										long endTime = Timestamp.valueOf(splitValues[1].replaceAll("/", "-")+ " 00:00:00").getTime();
										long diff = endTime - beginTime +1;
										SimpleDateFormat simpleFormat = new SimpleDateFormat(testDataGenenarteDTO.getDateFormate());
										for(long i =0;i<testDataGenenarteDTO.getGenerateRecordsCount();i++){
											lst.add(simpleFormat.format(new Date(beginTime+(long)(Math.random() * diff))));
										}
									}else{
										int iHigh = Integer.parseInt(splitValues[1]);
										int iLow = Integer.parseInt(splitValues[0]);
										for(long i =0;i<testDataGenenarteDTO.getGenerateRecordsCount();i++){
											lst.add(new Random().nextInt(iHigh - iLow) + iLow+"");
										}
									}
								}else{
									lst.addAll(mapValue.getValue());
								}
								mapConditionsPassed.put(mapValue.getKey(), lst);
							}else {
								mapConditionsPassed.put(mapValue.getKey(), mapValue.getValue());
							}
						}
					}
				
			}catch(Exception e){
				e.printStackTrace();
			}
				}
			}
			return mapConditionsPassed;
		}
		
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Vector<String>> generateQueriesAndDumping(JdbcTemplate jdbcTemplate,
			List<List<List<Object>>> listResultTabs, List<String> listRelations,
			Map<String, Object> mapConditions, List<List<Object>> listResultVal,
			long iRequiredRecordCount, List<String> lstTabsNames, List<String> listMasterTabs,
			Map<String, Map<String, Map<String, String>>> mapTableWithSequence,
			List<String> listConditionColNames, Map<String, Vector<String>> mapInputDepends,
			Map<String, String> mapReverseDependents,
			Map<String, List<String>> mapDataDictionaryVals, boolean isDataConditional,
			List<String> listDataDictionaryTabNames, TestDataGenerateDTO testDataGenenarteDTO)
			throws SQLException{
		String strMethodName = " [ generateQueriesAndDumping() ] ";
		logger.info(strClassName + strMethodName + " inside generateQueriesAndDumping method");
		String strMessage = TdgCentralConstant.SUCCESS_MESSAGE;
		Map<String, String> mapTables = new HashMap<String, String>();
		//Map<String, String> mapFindTables = new HashMap<String, String>();
		Map<String, Map<String, String>> mapPkWithTables = new HashMap<String, Map<String, String>>();
		Map<String, Map<String, String>> mapUkWithTables = new HashMap<String, Map<String, String>>();
		Map<String, Map<String, String>> mapFkWithTables = new HashMap<String, Map<String, String>>();
		Map<String, Map<String, String>> mapNNKWithTables = new HashMap<String, Map<String, String>>();
		Map<String, Map<String, String>> mapFetchPkWithTables = new HashMap<String, Map<String, String>>();
		Map<String, Map<Integer, String>> hmapValues = new HashMap<String, Map<Integer, String>>();
		Map<String, String> mapTableWithPk = new HashMap<String, String>();
		Map<String, Vector<String>> mapPks = new HashMap<String, Vector<String>>();
		Map<String, Vector<String>> mapDepends = new HashMap<String, Vector<String>>();
		Map<String, String> mapConditionWithPKs = new HashMap<String, String>();
		Map<String, Set<String>> mapTabsWithTables = new HashMap<String,Set<String>>();
		
		//Following parameter is used to arrange columns in sequence which given from datadictionary
		List<String> lstSequenceColsAndRequired = testDataGenenarteDTO.getRequiredSequenceColumns();
		//end
		
		//below variable is going to be used for filtering purpose
		Map<String,String> mapFilterCondition= new HashMap<String,String>();
		
		//Following parameter is used to add fixed length based on database length
		//Map<String,List>
		boolean isBreaked = false;
		
		//check excel and csv file generation
		/*boolean isCSV_OR_EXCEL = false;
		if(TdgCentralConstant.SCHEMA_GENERATION_TYPE_XLS.equals(testDataGenenarteDTO.getGenerationType()) || TdgCentralConstant.SCHEMA_GENERATION_TYPE_XLS.equals(testDataGenenarteDTO.getGenerationType()))
			isCSV_OR_EXCEL = true;*/
		int iIteratorCount = 0;
		if (logger.isDebugEnabled())
			logger.debug(strClassName + strMethodName + " total table to dump the data is :: "
					+ listResultTabs);
		if (listResultTabs == null || listResultTabs.isEmpty()) {
			strMessage = TdgCentralConstant.FAILED_MESSAGE;
			Vector<String> vctVals = new Vector<String>();
			mapDepends.put(strMessage, vctVals);
		}
		if (testDataGenenarteDTO != null && testDataGenenarteDTO.getGUIDictionaryColumns() != null
				&& !testDataGenenarteDTO.getGUIDictionaryColumns().isEmpty()) {
			for (String strColName : testDataGenenarteDTO.getGUIDictionaryColumns()) {
				if (!mapConditionValues.containsKey(strColName)) {
					List<String> lstValues = new ArrayList<String>();
					mapConditionValues.put(strColName, lstValues);
				}
			}
		}
		//for condition check of not empty passed tables from dictionary
		
		boolean isConditiontableExist = false;
		for (List<List<Object>> listResultTables : listResultTabs) {
			for (List<Object> listChildTabs : listResultTables) {
				
				for (Object obj : listChildTabs) {
					isBreaked = false;
					//int i = 0;
					Map<Integer, String> mapPositions = new HashMap<Integer, String>();
					List<String> listUkCheck = new ArrayList<String>();
					String strTableName = String.valueOf(obj)
							.substring(0, String.valueOf(obj).indexOf("#")).toUpperCase();
					if (logger.isDebugEnabled())
						logger.debug(strClassName + strMethodName + " current table is  :: "
								+ strTableName);
					/**
					 * Break the loop if data conditional dictionary not contains the table name
					 */
					if (isDataConditional && !listDataDictionaryTabNames.contains(strTableName)) {
						isBreaked = true;
					}
					/**
					 * Need to check for master table names for escape the insert values
					 * 18/9/2015 added passed tables filter
					 */
					if( (testDataGenenarteDTO.getDataConditionalTabNames() == null || testDataGenenarteDTO.getDataConditionalTabNames().isEmpty()) && !mapPks.containsKey(strTableName)){
						isConditiontableExist = true;
						Set<String> setTemp = new HashSet<String>();
						setTemp.add(strTableName);
						testDataGenenarteDTO.setDataConditionalTabNames(setTemp);
					}
					if(isConditiontableExist && testDataGenenarteDTO.getDataConditionalTabNames() != null  && !testDataGenenarteDTO.getDataConditionalTabNames().contains(strTableName) ){
						Set<String> setTemp = testDataGenenarteDTO.getDataConditionalTabNames();
						setTemp.add(strTableName);
						testDataGenenarteDTO.setDataConditionalTabNames(setTemp);
					}
					if (!mapPks.containsKey(strTableName) && !listMasterTabs.contains(strTableName)
							&& !isBreaked && testDataGenenarteDTO.getDataConditionalTabNames().contains(strTableName) && !mapTables.containsKey(strTableName)) {
						Map<String,Integer> mapTabColumnSequences = tdgTemplateDao.getTableColumnsInSequence(jdbcTemplate,strTableName);
						List<String> lstDateColumns = new ArrayList<String>();
						if(testDataGenenarteDTO.isRequiredAllColumns())
							lstDateColumns.addAll(tdgTemplateDao.getDateColumns(jdbcTemplate,strTableName));
						//The below list contains final columns to be updated
						List<String> lstIdentifiedcolumns = new ArrayList<String>();
						StringBuffer strUpdateQuery = new StringBuffer();
						if("UPDATE".equalsIgnoreCase(testDataGenenarteDTO.getPopulationType()))
							strUpdateQuery.append("UPDATE "
								+ strTableName + " SET ");
						if("DELETE".equalsIgnoreCase(testDataGenenarteDTO.getPopulationType()))
							strUpdateQuery.append("DELETE FROM "
								+ strTableName);
						List<Map<String, Object>> listValues = tdgTemplateDao.getConstraintsOfTable(jdbcTemplate,
								strTableName);
						//auto increment columns checking 
						List<Map<String,Object>> listAutoincrementColumns = tdgTemplateDao.getAutoIncrementColumns(jdbcTemplate,strTableName);
						List<Map<String, Object>> listPKValues = new ArrayList<Map<String, Object>>();
						List<Map<String, Object>> listUKValues = new ArrayList<Map<String, Object>>();
						List<Map<String, Object>> listNNKValues = new ArrayList<Map<String, Object>>();
						List<Map<String, Object>> listFKValues = new ArrayList<Map<String, Object>>();

						for (Map<String, Object> mapResult : listValues) {
							String strPkContraintName = String.valueOf(mapResult
									.get("CONSTRAINT_NAME"));
							if ("P".equals(mapResult.get("CONSTRAINT_TYPE"))) {
								listPKValues.addAll(tdgTemplateDao.getPkColumnType(jdbcTemplate,
										strPkContraintName, strTableName));
								/**
								 * This map is used to composite primary key values
								 */
							} else if ("U".equals(mapResult.get("CONSTRAINT_TYPE"))) {
								listUKValues.addAll(tdgTemplateDao.getPkColumnType(jdbcTemplate,
										strPkContraintName, strTableName));
							} else if ("R".equals(mapResult.get("CONSTRAINT_TYPE"))) {
								listFKValues.addAll(tdgTemplateDao.getPkColumnType(jdbcTemplate,
										strPkContraintName, strTableName));
							} else if ("C".equals(mapResult.get("CONSTRAINT_TYPE"))) {
								listNNKValues.addAll(tdgTemplateDao.getPkColumnType(jdbcTemplate,
										strPkContraintName, strTableName));
							}
						}
						// Going for getting not null constraints apart for
						// mysql support
						List<Map<String, Object>> listNNValues = tdgTemplateDao.getNotNullConstraintsOfTable(
								jdbcTemplate, strTableName);
						if (listNNValues != null && !listNNValues.isEmpty()) {
							List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
							for (Map<String, Object> mapResult : listNNValues) {
								if (TdgCentralConstant.IS_NULLABLE.equals(mapResult
										.get("IS_NULLABLE"))) {
									listMap.add(mapResult);
								}
							}
							if (!listMap.isEmpty())
								listNNKValues.addAll(listMap);
						}
						
						/**
						 * Going for getting the Primary key values
						 */
						//boolean bDuplicateCheck = false;
						for (Map<String, Object> mapResult : listPKValues) {
							// if (!mapConditions.isEmpty()) {
							// listUkCheck.add(String.valueOf(mapResult.get("COLUMN_NAME")));
							if (!listUkCheck.contains(String.valueOf(mapResult.get("COLUMN_NAME")))) {
								if(mapFilterCondition.isEmpty())
								mapFilterCondition.put(strTableName,String.valueOf(mapResult.get("COLUMN_NAME")));
								listUkCheck.add(String.valueOf(mapResult.get("COLUMN_NAME")));
								
								//added for sequence
								/*if(lstSequenceColsAndRequired != null && !lstSequenceColsAndRequired.isEmpty()){
									if(lstSequenceColsAndRequired.contains(String.valueOf(mapResult.get("COLUMN_NAME")))){
								   // mapPositions.put(++i, String.valueOf(mapResult.get("COLUMN_NAME")));
								    lstIdentifiedcolumns.add(String.valueOf(mapResult.get("COLUMN_NAME")));
								  strInsertQuery.append(' ')
									.append(String.valueOf(mapResult.get("COLUMN_NAME")))
									.append(' ');
									}
								}else{*/
									//mapPositions.put(++i, String.valueOf(mapResult.get("COLUMN_NAME")));
								if(mapConditions.containsKey(String.valueOf(mapResult.get("COLUMN_NAME"))))
									lstIdentifiedcolumns.add(String.valueOf(mapResult.get("COLUMN_NAME"))); //commented for update
								/*	strInsertQuery.append(' ')
									.append(String.valueOf(mapResult.get("COLUMN_NAME")))
									.append(' ');
								}*/
								/**
								 * Check is table contains primary key or not
								 */
								/*if (String.valueOf(mapResult.get("COLUMN_NAME")) != null
										&& "".equals(String.valueOf(mapResult.get("COLUMN_NAME")))) {
									--i;
								}*/
								mapTableWithPk.put(
										strTableName,
										String.valueOf(mapResult.get("DATA_TYPE")) + "#"
												+ String.valueOf(mapResult.get("DATA_LENGTH")));
								/*if (i != listPKValues.size()) {
									strInsertQuery.append(" , ");
								}
								if (i > 0) {*/
									mapConditionWithPKs.put(strTableName,
											String.valueOf(mapResult.get("COLUMN_NAME")));
								//}
							} /*else {
								bDuplicateCheck = true;
							}*/
						}
						/*if (bDuplicateCheck) {
							String strValue = strInsertQuery.toString().substring(0,
									strInsertQuery.toString().lastIndexOf(','));
							strInsertQuery = new StringBuffer(strValue);
						}*/
						/**
						 * Added for composite primary key values
						 */
						Map<String, String> mapUkValues = new HashMap<String, String>();
						int iRecurseCount = 0;
						for (Map<String, Object> mapResult : listPKValues) {
							if (!listUkCheck.contains((mapResult.get("COLUMN_NAME")))
									&& listPKValues.size() > 1 && iRecurseCount > 0) {
								mapUkValues.put(
										String.valueOf(mapResult.get("COLUMN_NAME")),
										String.valueOf(mapResult.get("DATA_TYPE")) + "#"
												+ String.valueOf(mapResult.get("DATA_LENGTH")));
							}
							iRecurseCount++;
						}
						
						/**
						 * Going for getting the unique key values
						 */
						for (Map<String, Object> mapResult : listUKValues) {
							if (!listUkCheck.contains((mapResult.get("COLUMN_NAME")))) {
								if(mapFilterCondition.isEmpty())
									mapFilterCondition.put(strTableName,String.valueOf(mapResult.get("COLUMN_NAME")));
								mapUkValues.put(
										String.valueOf(mapResult.get("COLUMN_NAME")),
										String.valueOf(mapResult.get("DATA_TYPE")) + "#"
												+ String.valueOf(mapResult.get("DATA_LENGTH")));
								listUkCheck.add(String.valueOf(mapResult.get("COLUMN_NAME")));
								
								//added for sequence
								/*if(lstSequenceColsAndRequired != null && !lstSequenceColsAndRequired.isEmpty()){
									if(lstSequenceColsAndRequired.contains(String.valueOf(mapResult.get("COLUMN_NAME")))){
								  //mapPositions.put(++i, String.valueOf(mapResult.get("COLUMN_NAME")));
								  lstIdentifiedcolumns.add(String.valueOf(mapResult.get("COLUMN_NAME")));
								  if (i-1 > 0) {
										strInsertQuery.append(" , ");
									}
									strInsertQuery.append(' ')
											.append(String.valueOf(mapResult.get("COLUMN_NAME")))
											.append(' ');
									}
								}else{*/
									//mapPositions.put(++i, String.valueOf(mapResult.get("COLUMN_NAME")));
								if(mapConditions.containsKey(String.valueOf(mapResult.get("COLUMN_NAME"))))
									lstIdentifiedcolumns.add(String.valueOf(mapResult.get("COLUMN_NAME")));
									lstDateColumns.remove(String.valueOf(mapResult.get("COLUMN_NAME")));
									/*if (i-1 > 0) {
										strInsertQuery.append(" , ");
									}
									strInsertQuery.append(' ')
											.append(String.valueOf(mapResult.get("COLUMN_NAME")))
											.append(' ');
								}*/
								//mapPositions.put(++i, String.valueOf(mapResult.get("COLUMN_NAME")));
							}
						}
						/**
						 * Going for getting the Not null key values
						 */
						Map<String, String> mapNNkValues = new HashMap<String, String>();
						for (Map<String, Object> mapResult : listNNKValues) {
							if(mapFilterCondition.isEmpty())
								mapFilterCondition.put(strTableName,String.valueOf(mapResult.get("COLUMN_NAME")));
							if (!mapConditions.containsKey(mapResult.get("COLUMN_NAME"))
									&& !listUkCheck.contains((mapResult.get("COLUMN_NAME")))) {
								mapNNkValues.put(
										String.valueOf(mapResult.get("COLUMN_NAME")),
										String.valueOf(mapResult.get("DATA_TYPE")).contains(
												"TIMESTAMP")
												|| String.valueOf(mapResult.get("DATA_TYPE"))
														.contains("DATE") ? "DATE" : String
												.valueOf(mapResult.get("DATA_TYPE"))
												+ "#"
												+ String.valueOf(mapResult.get("DATA_LENGTH")));
								listUkCheck.add(String.valueOf(mapResult.get("COLUMN_NAME")));								
								//added for sequence
								/*if(lstSequenceColsAndRequired != null && !lstSequenceColsAndRequired.isEmpty()){
									if(lstSequenceColsAndRequired.contains(String.valueOf(mapResult.get("COLUMN_NAME")))){
								 // mapPositions.put(++i, String.valueOf(mapResult.get("COLUMN_NAME")));
								  lstIdentifiedcolumns.add(String.valueOf(mapResult.get("COLUMN_NAME")));
								  if (i-1 > 0) {
										strInsertQuery.append(" , ");
									}
									strInsertQuery.append(' ')
											.append(String.valueOf(mapResult.get("COLUMN_NAME")))
											.append(' ');
									}
								}else{
									//mapPositions.put(++i, String.valueOf(mapResult.get("COLUMN_NAME")));
									 * 
*/									
								if(mapConditions.containsKey(String.valueOf(mapResult.get("COLUMN_NAME"))))
								lstIdentifiedcolumns.add(String.valueOf(mapResult.get("COLUMN_NAME")));//commented for update
									lstDateColumns.remove(String.valueOf(mapResult.get("COLUMN_NAME")));
									/*if (i-1 > 0) {
										strInsertQuery.append(" , ");
									}
									strInsertQuery.append(' ')
											.append(String.valueOf(mapResult.get("COLUMN_NAME")))
											.append(' ');
								}*/
								//mapPositions.put(++i, String.valueOf(mapResult.get("COLUMN_NAME")));
							}
						}
						
						
						/**
						 * Going for getting the foreign key values
						 */
						Map<String, String> mapFkValues = new HashMap<String, String>();
						for (Map<String, Object> mapResult : listFKValues) {
							if(mapFilterCondition.isEmpty())
								mapFilterCondition.put(strTableName,String.valueOf(mapResult.get("COLUMN_NAME")));
							mapFkValues.put(
									String.valueOf(mapResult.get("COLUMN_NAME")),
									String.valueOf(mapResult.get("DATA_TYPE")) + "#"
											+ String.valueOf(mapResult.get("DATA_LENGTH")));
							listUkCheck.add(String.valueOf(mapResult.get("COLUMN_NAME")));
							
							//added for sequence
							/*if(lstSequenceColsAndRequired != null && !lstSequenceColsAndRequired.isEmpty()){
								if(lstSequenceColsAndRequired.contains(String.valueOf(mapResult.get("COLUMN_NAME")))){*/
							if(mapConditions.containsKey(String.valueOf(mapResult.get("COLUMN_NAME"))))
									lstIdentifiedcolumns.add(String.valueOf(mapResult.get("COLUMN_NAME")));//commented for update
									lstDateColumns.remove(String.valueOf(mapResult.get("COLUMN_NAME")));
							  //mapPositions.put(++i, String.valueOf(mapResult.get("COLUMN_NAME")));
							 /* if (i-1 > 0) {
									strInsertQuery.append(" , ");
								}
								strInsertQuery.append(' ')
										.append(String.valueOf(mapResult.get("COLUMN_NAME")))
										.append(' ');
								}
							}else{*/
								//mapPositions.put(++i, String.valueOf(mapResult.get("COLUMN_NAME")));
								/*lstIdentifiedcolumns.add(String.valueOf(mapResult.get("COLUMN_NAME")));
								if (i-1 > 0) {
									strInsertQuery.append(" , ");
								}
								strInsertQuery.append(' ')
										.append(String.valueOf(mapResult.get("COLUMN_NAME")))
										.append(' ');
							}*/
							//mapPositions.put(++i, String.valueOf(mapResult.get("COLUMN_NAME")));
						}
						/**
						 * Going for adding the condition query values
						 */
						List<Map<String, Object>> mapColumnsOfTab = tdgTemplateDao.getSequenceColsByTableName(
								jdbcTemplate, strTableName, null);
						/* 1.Check the input values which is passed from GUI level */
						if (mapConditions != null && !mapConditions.isEmpty()) {
							for (Map<String, Object> mapValues : mapColumnsOfTab) {
								for (Map.Entry<String, Object> mapEntryVals : mapConditions
										.entrySet()) {
									if (!mapConditionValues.containsKey(mapEntryVals.getKey())) {
										List<String> lstValues = new ArrayList<String>();
										mapConditionValues.put(mapEntryVals.getKey(), lstValues);
									}
									if (!listUkCheck.contains(mapValues.get("COLUMN_NAME")
											.toString().toUpperCase())) {
										if (mapValues.get("COLUMN_NAME").toString().toUpperCase()
												.equals(mapEntryVals.getKey())) {
											listUkCheck.add(String.valueOf(mapValues
													.get("COLUMN_NAME").toString().toUpperCase()));
											/**
											 * going for check date formate
											 */
											if (String.valueOf(mapValues.get("DATA_TYPE"))
													.contains("TIMESTAMP")
													|| String.valueOf(mapValues.get("DATA_TYPE"))
															.contains("DATE")) {
												try {
													DateFormat dateFormat = new SimpleDateFormat(
															"d/MMM/yyyy");
													Date date = dateFormat.parse(mapEntryVals
															.getValue() + "");
													dateFormat = new SimpleDateFormat(
															testDataGenenarteDTO.getDateFormate());
													String strDate = dateFormat.format(date) + "";
													mapEntryVals.setValue(strDate);
												} catch (ParseException pe) {
													/*mapEntryVals.setValue(new SimpleDateFormat(testDataGenenarteDTO.getDateFormate()).format(mapEntryVals
															.getValue() + ""));*/
													logger.info(strClassName + strMethodName
															+ " Date formate issue was raised...");
												}
											}
											
											//added for sequence
											/*if(lstSequenceColsAndRequired != null && !lstSequenceColsAndRequired.isEmpty()){
												if(lstSequenceColsAndRequired.contains(String.valueOf(mapValues.get("COLUMN_NAME")))){
											  
											  if (i > 0) {
													strInsertQuery.append(" , ");
												}
												strInsertQuery
														.append(' ')
														.append(String.valueOf(mapValues
																.get("COLUMN_NAME"))).append(' ');
												}												
											}else{
												if (i > 0) {
													strInsertQuery.append(" , ");
												}
												strInsertQuery
														.append(' ')
														.append(String.valueOf(mapValues
																.get("COLUMN_NAME"))).append(' ');
											}*/
											//mapPositions.put(++i, String.valueOf(mapValues.get("COLUMN_NAME")));
											lstIdentifiedcolumns.add(String.valueOf(mapValues.get("COLUMN_NAME")));
											lstDateColumns.remove(String.valueOf(mapValues.get("COLUMN_NAME")));
											//mapPositions.put(++i,
													//String.valueOf(mapValues.get("COLUMN_NAME")));
										}
									}
								}
							}
						}
						/* 2. Check for dictionary contains columns which is being related to table
						 * then add also */
						if (!mapDataDictionaryVals.isEmpty()) {
							for (Map<String, Object> mapValues : mapColumnsOfTab) {
								for (Map.Entry<String, List<String>> mapEntryVals : mapDataDictionaryVals
										.entrySet()) {
									if (!listUkCheck.contains(mapValues.get("COLUMN_NAME")
											.toString().toUpperCase())) {
										if (mapValues.get("COLUMN_NAME").toString().toUpperCase()
												.equalsIgnoreCase(mapEntryVals.getKey()) || (strTableName+TdgCentralConstant.TDG_DOT+mapValues.get("COLUMN_NAME").toString().toUpperCase())
												.equalsIgnoreCase(mapEntryVals.getKey())) {
											
											//added for sequence
											/*if(lstSequenceColsAndRequired != null && !lstSequenceColsAndRequired.isEmpty()){
												if(lstSequenceColsAndRequired.contains(String.valueOf(mapValues.get("COLUMN_NAME")))){
											  //mapPositions.put(++i, String.valueOf(mapValues.get("COLUMN_NAME")));
											  if (i > 0) {
													strInsertQuery.append(" , ");
												}
											  //mapPositions.put(++i, String.valueOf(mapValues.get("COLUMN_NAME")));
											  lstIdentifiedcolumns.add(String.valueOf(mapValues.get("COLUMN_NAME")));
												strInsertQuery
														.append(' ')
														.append(String.valueOf(mapValues
																.get("COLUMN_NAME"))).append(' ');
												}
											}else{
												//mapPositions.put(++i, String.valueOf(mapValues.get("COLUMN_NAME")));
												if (i > 0) {
													strInsertQuery.append(" , ");
												}*/
												//mapPositions.put(++i, String.valueOf(mapValues.get("COLUMN_NAME")));
												lstIdentifiedcolumns.add(String.valueOf(mapValues.get("COLUMN_NAME")));
												lstDateColumns.remove(String.valueOf(mapValues.get("COLUMN_NAME")));
												/*strInsertQuery
														.append(' ')
														.append(String.valueOf(mapValues
																.get("COLUMN_NAME"))).append(' ');
											}*/
											/*mapPositions.put(++i,
													String.valueOf(mapValues.get("COLUMN_NAME")));*/
										}
									}
								}
							}
						}
						/**
						 * Added for multiple databases inject of the columns based on
						 * dictionary details
						 */
						for (String strColunmName : listConditionColNames) {
							/*if (!strInsertQuery.toString().contains(" " + strColunmName + " ")
									&& !listUkCheck.contains(strColunmName)) {*/
							if (!lstIdentifiedcolumns.contains(" " + strColunmName + " ")
									&& !listUkCheck.contains(strColunmName)) {
								for (Map<String, Object> mapValues : mapColumnsOfTab) {
									if (mapValues.get("COLUMN_NAME").toString().toUpperCase()
											.equals(strColunmName)) {
										
										mapNNkValues.put(
												String.valueOf(mapValues.get("COLUMN_NAME")),
												String.valueOf(mapValues.get("DATA_TYPE"))
														.contains("TIMESTAMP")
														|| String.valueOf(
																mapValues.get("DATA_TYPE"))
																.contains("DATE") ? "DATE" : String
														.valueOf(mapValues.get("DATA_TYPE"))
														+ "#"
														+ String.valueOf(mapValues
																.get("DATA_LENGTH")));
										listUkCheck
												.add(String.valueOf(mapValues.get("COLUMN_NAME")));
										
										//added for sequence
										/*if(lstSequenceColsAndRequired != null && !lstSequenceColsAndRequired.isEmpty()){
											if(lstSequenceColsAndRequired.contains(String.valueOf(mapValues.get("COLUMN_NAME")))){
										 // mapPositions.put(++i, String.valueOf(mapValues.get("COLUMN_NAME")));
										  lstIdentifiedcolumns.add(String.valueOf(mapValues.get("COLUMN_NAME")));
										  if (i-1 > 0) {
												strInsertQuery.append(" , ");
											}
											strInsertQuery
													.append(' ')
													.append(String.valueOf(mapValues.get("COLUMN_NAME")))
													.append(' ');
											}
										}else{*/
											//mapPositions.put(++i, String.valueOf(mapValues.get("COLUMN_NAME")));
											lstIdentifiedcolumns.add(String.valueOf(mapValues.get("COLUMN_NAME")));
											lstDateColumns.remove(String.valueOf(mapValues.get("COLUMN_NAME")));
											/*if (i-1 > 0) {
												strInsertQuery.append(" , ");
											}
											strInsertQuery
													.append(' ')
													.append(String.valueOf(mapValues.get("COLUMN_NAME")))
													.append(' ');
										}*/
										/*mapPositions.put(++i,
												String.valueOf(mapValues.get("COLUMN_NAME")));*/
									}
								}
							}
						}
						
						//needs to check date columns for if GUI selected by All columns option
						if(!lstDateColumns.isEmpty()){
							
						}
						
						//end of Date columns check
						
						//Now going for adding query as per sequence
						//if(testDataGenenarteDTO.isRequiredAllColumns()){
						int iColumnSequencCount = 0 ;
						Set<String> setColumns = new HashSet<String>();
						mapTabsWithTables.put(strTableName, setColumns);
						if(testDataGenenarteDTO.getGenerationType().equalsIgnoreCase("DB"))
							for(Map.Entry<String, Integer> mapFinalCols: mapTabColumnSequences.entrySet()){
								//mapPositions.put(mapFinalCols.getValue(), mapFinalCols.getKey());
								//if(strInsertQuery.toString().contains(" , ")){
								if(listAutoincrementColumns != null && !listAutoincrementColumns.isEmpty()){
									for(Map<String,Object> mapObject : listAutoincrementColumns){
										if(mapObject.get("COLUMN_NAME").toString().equalsIgnoreCase(mapFinalCols.getKey()) && mapObject.get("TABLE_NAME").toString().equalsIgnoreCase(strTableName))
											break;
										else{
											if(testDataGenenarteDTO.isRequiredAllColumns()){
												mapPositions.put(mapFinalCols.getValue(), mapFinalCols.getKey());
											strUpdateQuery.append(mapFinalCols.getKey()).append(" = ? , ");
											}else{
											if(lstSequenceColsAndRequired != null && !lstSequenceColsAndRequired.isEmpty() && lstSequenceColsAndRequired.contains(mapFinalCols.getKey())){
												mapPositions.put(++iColumnSequencCount,mapFinalCols.getKey());
												strUpdateQuery.append(mapFinalCols.getKey()).append(" = ? , ");
											}else if(lstIdentifiedcolumns.contains(mapFinalCols.getKey())){
													mapPositions.put(++iColumnSequencCount,mapFinalCols.getKey());
													strUpdateQuery.append(mapFinalCols.getKey()).append(" = ? , ");
												}
													
												//if(strInsertQuery.toString().contains(" , "))
													
										}
											//adding below code for TDG_DB_SEQ check columns in table specific
											setColumns.add(mapFinalCols.getKey());
											break;
										}
									}
								}else{
									if(testDataGenenarteDTO.isRequiredAllColumns()){
										mapPositions.put(mapFinalCols.getValue(), mapFinalCols.getKey());
									strUpdateQuery.append(mapFinalCols.getKey()).append(" =?, ");
									}else{
									if(lstSequenceColsAndRequired != null && !lstSequenceColsAndRequired.isEmpty() && lstSequenceColsAndRequired.contains(mapFinalCols.getKey())){
										mapPositions.put(++iColumnSequencCount,mapFinalCols.getKey());
										strUpdateQuery.append(mapFinalCols.getKey()).append(" =?, ");
									}else if(lstIdentifiedcolumns.contains(mapFinalCols.getKey())){
											mapPositions.put(++iColumnSequencCount,mapFinalCols.getKey());
											strUpdateQuery.append(mapFinalCols.getKey()).append(" =?, ");
										}
											
										//if(strInsertQuery.toString().contains(" , "))
											
								}
									//adding below code for TDG_DB_SEQ check columns in table specific
									setColumns.add(mapFinalCols.getKey());
								}
							}
						else{
							for(Map.Entry<String, Integer> mapFinalCols: mapTabColumnSequences.entrySet()){
								//mapPositions.put(mapFinalCols.getValue(), mapFinalCols.getKey());
								//if(strInsertQuery.toString().contains(" , ")){
								//if(listAutoincrementColumns)
									if(testDataGenenarteDTO.isRequiredAllColumns()){
										mapPositions.put(mapFinalCols.getValue(), mapFinalCols.getKey());
									strUpdateQuery.append(mapFinalCols.getKey()).append(" =?, ");
									}else{
									if(lstSequenceColsAndRequired != null && !lstSequenceColsAndRequired.isEmpty() && lstSequenceColsAndRequired.contains(mapFinalCols.getKey())){
										mapPositions.put(++iColumnSequencCount,mapFinalCols.getKey());
										strUpdateQuery.append(mapFinalCols.getKey()).append(" =?, ");
									}else if(lstIdentifiedcolumns.contains(mapFinalCols.getKey())){
											mapPositions.put(++iColumnSequencCount,mapFinalCols.getKey());
											strUpdateQuery.append(mapFinalCols.getKey()).append(" =?, ");
										}
											
										//if(strInsertQuery.toString().contains(" , "))
											
								}
									//adding below code for TDG_DB_SEQ check columns in table specific
									setColumns.add(mapFinalCols.getKey());
							}
						}
						
						//end of sequence alignment 
						String strVal = strUpdateQuery.toString();
						if (strVal.trim().endsWith(",")) {
							//String strVal = strUpdateQuery.toString();
							strUpdateQuery = new StringBuffer(strUpdateQuery.toString().substring(
									0, strVal.lastIndexOf(","))).append(' ');
						}
						//strUpdateQuery.append(" WHERE ");
						/*if(testDataGenenarteDTO.getMapinputConditionData() != null && !testDataGenenarteDTO.getMapinputConditionData().isEmpty()){
							strUpdateQuery.append(" WHERE ");
							for(Map.Entry<String, Object> mapinputconditions: testDataGenenarteDTO.getMapinputConditionData().entrySet()){
								if(mapinputconditions.getValue().toString().contains(","))
									strUpdateQuery.append(mapinputconditions.getKey()).append(" IN (?").append(")");
								else									
								strUpdateQuery.append(mapinputconditions.getKey()).append("=?").append(",");
								
							}
						}*/
						/*strUpdateQuery = new StringBuffer(strUpdateQuery.toString().substring(
								0, strUpdateQuery.toString().lastIndexOf(","))).append(' ');*/
						mapTables.put(strTableName, strUpdateQuery.toString());
						//mapFindTables.put(strTableName, strFindQuery.toString());
						mapPkWithTables.put(strTableName, mapTableWithPk);
						mapUkWithTables.put(strTableName, mapUkValues);
						mapFkWithTables.put(strTableName, mapFkValues);
						mapNNKWithTables.put(strTableName, mapNNkValues);
						hmapValues.put(strTableName, mapPositions);
						
						//added below code for date format columns
						if(!lstDateColumns.isEmpty()){
							Map<String, String> mapDummyVal = new HashMap<String, String>();
							for(String columnName : lstDateColumns){
								mapDummyVal.put(columnName, "DATE");
							}
							mapFetchPkWithTables.put(strTableName, mapDummyVal);
						}
						//end of date format code
						// adding dummy columns values were deprecated
						/*if (strInsertQuery.toString().contains("(?)")) {
							Map<String, String> mapDummyVal = new HashMap<String, String>();
							List<Map<String, Object>> listString = tdgTemplateDao.getSequenceColsByTableName(
									jdbcTemplate,
									strTableName,
									strInsertQuery.substring(strInsertQuery.indexOf("(") + 1,
											strInsertQuery.indexOf(")")).trim());
							for (Map<String, Object> map : listString) {
								mapDummyVal.put(
										String.valueOf(map.get("COLUMN_NAME")),
										String.valueOf(map.get("DATA_TYPE")).contains("TIMESTAMP")
												|| String.valueOf(map.get("DATA_TYPE")).contains(
														"DATE") ? "DATE" : String.valueOf(map
												.get("DATA_TYPE"))
												+ "#"
												+ String.valueOf(map.get("DATA_LENGTH")));
								if (!map.isEmpty()) {
									String columnName = String.valueOf(map.get("COLUMN_NAME"));
									if (columnName != null) {
										StringBuffer strBuffer = new StringBuffer(
												strInsertQuery.substring(0,
														strInsertQuery.indexOf("(")));
										StringBuffer strSubQueryInsert = new StringBuffer(
												strInsertQuery.substring(
														strInsertQuery.indexOf("("),
														strInsertQuery.indexOf(")")));
										strSubQueryInsert.append(' ').append(" , ")
												.append(columnName).append(") ");
										strBuffer
												.append(strSubQueryInsert)
												.append(strInsertQuery.substring(
														strInsertQuery.indexOf(" VALUES ("),
														strInsertQuery.length() - 1).replaceAll(
														"\\?", "?,?")).append(')');
										strInsertQuery = new StringBuffer(strBuffer);
										mapPositions = hmapValues.get(strTableName);
										mapPositions.put(2, String.valueOf(map.get("COLUMN_NAME")));
										mapTables.put(strTableName, strInsertQuery.toString());
										break;
									}
								}
							}
							mapFetchPkWithTables.put(strTableName, mapDummyVal);
						}*/
						//hmapValues.put(strTableName, mapPositions);
						if (logger.isDebugEnabled()) {
							logger.debug(strClassName + strMethodName + " generated query is  :: "
									+ strUpdateQuery.toString());
						}
						
						
					}
				}
			}
			if (!isBreaked) {
				int iHashValue = tdgTemplateDao.getHighestHashValue(listResultVal.get(iIteratorCount));
				if (iHashValue <= 0) {
					strMessage = TdgCentralConstant.FAILED_MESSAGE;
				}
				List<String> listColumnsName = new ArrayList<String>();
				if (mapConditions != null) {
					for (String strKey : mapConditions.keySet()) {
						if (!strKey.contains("DEPENDS_ON"))
							listColumnsName.add(strKey);
					}
				}
				List<Map<String, Object>> listColumnsNameWthTable = tdgTemplateDao.getTableNamesByColsName(
						jdbcTemplate, listColumnsName);
				List<String> tabColsInputCond = new ArrayList<String>();
				for(Map.Entry<String, Object> mapinputconditions: testDataGenenarteDTO.getMapinputConditionData().entrySet()){
					tabColsInputCond.add(mapinputconditions.getKey());
				}
				List<Map<String,Object>> listinputColsConditions = tdgTemplateDao.getTableNamesByColsName(jdbcTemplate, tabColsInputCond);
				/**
				 * Going to make transaction
				 */
				logger.info(strClassName + strMethodName
						+ " going to insert the data into server in sequence.....");
				for (int i = iHashValue; i >= 1; i--) {
					Set<String> listFinalSequence = tdgTemplateDao.getTableNameInsert(
							listResultVal.get(iIteratorCount), i);
					
					for (String tbName : listFinalSequence) {
						//added below if condition to filter the passed tables
						if(listFinalSequence != null && testDataGenenarteDTO.getDataConditionalTabNames().contains(tbName)){
						boolean bUniqCheck = false;
						boolean bDataConditional = false;
						/**
						 * Need to check for master table names for escape the insert values
						 */
						if (listDataDictionaryTabNames != null
								&& !listDataDictionaryTabNames.contains(tbName)
								&& isDataConditional) {
							bDataConditional = true;
						}
						if (!mapPks.containsKey(tbName) && !listMasterTabs.contains(tbName)
								&& !bDataConditional) {
							Map<Integer, String> mapPositions = hmapValues.get(tbName);
							Map<String, String> mapPkNames = mapPkWithTables.get(tbName);
							Map<String, String> mapUkNames = mapUkWithTables.get(tbName);
							Map<String, String> mapNNkNames = mapNNKWithTables.get(tbName);
							Map<String, String> mapDummyNames = mapFetchPkWithTables.get(tbName);
							Map<String, String> mapFkNames = mapFkWithTables.get(tbName);
							Map<String, String> mpFkValues = new HashMap<String, String>();
							Map<String, String> mpTabConditions = new HashMap<String, String>();							
							
							
							StringBuffer strCountQuery = new StringBuffer("SELECT COUNT(1) FROM ").append(tbName);
							StringBuffer strFindQuery = new StringBuffer("SELECT "+mapFilterCondition.get(tbName)+" ");
							//String FinderColumn = "";
							/*if (!mapPkNames.isEmpty()){
								strFindQuery.append(mapConditionWithPKs.get(tbName));
								//FinderColumn="PK";
							}else if(!mapUkNames.isEmpty()){
								for (Map.Entry<String, String> mapEntry : mapUkNames.entrySet()) {
									if(!mapConditions.containsKey(mapEntry.getKey())){
										strFindQuery.append(mapEntry.getKey());
										//FinderColumn="PK";
										break;
									}
								}
							}else if(!mapNNkNames.isEmpty()){
								for (Map.Entry<String, String> mapEntry : mapNNkNames.entrySet()) {
									if(!mapConditions.containsKey(mapEntry.getKey())){
										strFindQuery.append(mapEntry.getKey());
										break;
									}
								}
							}else if(!mapFkNames.isEmpty()){
								for (Map.Entry<String, String> mapEntry : mapFkNames.entrySet()) {
									if(!mapConditions.containsKey(mapEntry.getKey())){
										strFindQuery.append(mapEntry.getKey());
										break;
									}
								}
							}else if(!mapDummyNames.isEmpty()){
								for (Map.Entry<String, String> mapEntry : mapDummyNames.entrySet()) {
									if(!mapConditions.containsKey(mapEntry.getKey())){
										strFindQuery.append(mapEntry.getKey());
										break;
									}
								}
							}*/
							
							strFindQuery.append(" FROM ").append(tbName);
							//end fetch query generation
						
							//following list used to match existing columns in respective tables
							List<String> listCols = new ArrayList<String>();
							if (listColumnsNameWthTable != null
									&& !listColumnsNameWthTable.isEmpty()) {
								/*strFindQuery.append(" WHERE ");
								strCountQuery.append(" WHERE ");*/
								for (Map<String, Object> mapResult : listColumnsNameWthTable) {
									//listCols.add(String.valueOf(mapResult.get("COLUMN_NAME")));
									if (tbName.equals(mapResult.get("TABLE_NAME"))) {
										if(mapConditions
												.get(mapResult.get("COLUMN_NAME")) instanceof List){
											StringBuffer strBuffer = new StringBuffer();
											int iCount = 0;
											List<Object> lst = (List<Object>)mapConditions.get(mapResult.get("COLUMN_NAME"));
											for(Object obj: lst){
												iCount++;
												if(obj != null && !"".equalsIgnoreCase(obj+""))
												strBuffer.append(obj);
												if(iCount != lst.size()){
													strBuffer.append("#");
												}
											}
											mpTabConditions.put(String.valueOf(mapResult
													.get("COLUMN_NAME")), strBuffer.toString());
											/*if(strFindQuery.toString().trim().endsWith("=?"))
												strFindQuery.append(" AND ");
											strFindQuery.append(mapResult.get("COLUMN_NAME")).append("=?");
											if(strCountQuery.toString().trim().endsWith("=?"))
												strCountQuery.append(" AND ");
											strCountQuery.append(mapResult.get("COLUMN_NAME")).append("=?");*/
										}else{
										mpTabConditions.put(String.valueOf(mapResult
												.get("COLUMN_NAME")), (String) mapConditions
												.get(mapResult.get("COLUMN_NAME")));
										/*if(strFindQuery.toString().trim().endsWith("=?"))
											strFindQuery.append(" AND ");
										strFindQuery.append(mapResult.get("COLUMN_NAME")).append("=?");
										if(strCountQuery.toString().trim().endsWith("=?"))
											strCountQuery.append(" AND ");
										strCountQuery.append(mapResult.get("COLUMN_NAME")).append("=?");
										}*/
										}
									}
								}
							}
							
							if (listinputColsConditions != null
									&& !listinputColsConditions.isEmpty()) {
								strFindQuery.append(" WHERE ");
								strCountQuery.append(" WHERE ");
								int iLoop = 0;
								for (Map<String, Object> mapResult : listinputColsConditions) {
									//listCols.add(String.valueOf(mapResult.get("COLUMN_NAME")));
									String strVal = String.valueOf(testDataGenenarteDTO.getMapinputConditionData().get(mapResult.get("COLUMN_NAME")));
									/*if(mapResult.get("DATA_TYPE").equals("CHAR")){
										//String strVal = String.valueOf(testDataGenenarteDTO.getMapinputConditionData().get(mapResult.get("COLUMN_NAME")));
										strVal = TdgAutoIncrementUtil.fixedLength(String.valueOf(testDataGenenarteDTO.getMapinputConditionData().get(mapResult.get("COLUMN_NAME"))), Integer.parseInt(mapResult.get("CHAR_LENGTH").toString()));
									}*/
									if(!strVal.equals("ISNULL") && !strVal.equals("IS NULL"))
									if(strVal.contains(",,")){
										String[] strSplit = strVal.split(",,");
										StringBuffer strbuffer = new StringBuffer("(");
										
										for(int ij=0;ij<strSplit.length;ij++){
											if(mapResult.get("DATA_TYPE").equals("NUMBER")){
												if(ij!=0){
													strbuffer.append(",");
												strbuffer.append(strSplit[ij]);	
												}
											}else if(mapResult.get("DATA_TYPE").toString().contains("CHAR")){
												if(mapResult.get("DATA_TYPE").equals("CHAR")){
													strSplit[ij] = TdgAutoIncrementUtil.fixedLength(strSplit[ij], Integer.parseInt(mapResult.get("CHAR_LENGTH").toString()));
												}
												if(ij!=0)
													strbuffer.append(",");
												strbuffer.append("'"+strSplit[ij]+"'");	
											}
											
										}
										strbuffer.append(")");
										strVal= " IN "+strbuffer.toString();
									}else{
										if(mapResult.get("DATA_TYPE").equals("NUMBER"))
											strVal="="+testDataGenenarteDTO.getMapinputConditionData().get(mapResult.get("COLUMN_NAME"));
										else{
											if(mapResult.get("DATA_TYPE").equals("CHAR"))
												strVal = "='"+TdgAutoIncrementUtil.fixedLength(strVal, Integer.parseInt(mapResult.get("CHAR_LENGTH").toString()))+"'";
											else
												strVal="='"+testDataGenenarteDTO.getMapinputConditionData().get(mapResult.get("COLUMN_NAME"))+"'";
										}
									}
									if (tbName.equals(mapResult.get("TABLE_NAME"))) {
										if(iLoop > 0){
											strCountQuery.append( " AND ");
											strFindQuery.append( " AND ");
										}
											
										if(mapConditions
												.get(mapResult.get("COLUMN_NAME")) instanceof List){
											StringBuffer strBuffer = new StringBuffer();
											int iCount = 0;
											List<Object> lst = (List<Object>)mapConditions.get(mapResult.get("COLUMN_NAME"));
											for(Object obj: lst){
												iCount++;
												if(obj != null && !"".equalsIgnoreCase(obj+""))
												strBuffer.append(obj);
												if(iCount != lst.size()){
													strBuffer.append("#");
												}
											}
											mpTabConditions.put(String.valueOf(mapResult
													.get("COLUMN_NAME")), strBuffer.toString());
											if(strFindQuery.toString().trim().contains(" WHERE "))
												strFindQuery.append(" AND ");
											//strFindQuery.append(mapResult.get("COLUMN_NAME")).append("=?");
											if(strCountQuery.toString().trim().contains(" WHERE "))
												strCountQuery.append(" AND ");
											//strCountQuery.append(mapResult.get("COLUMN_NAME")).append("=?");
											strCountQuery.append(mapResult.get("COLUMN_NAME")).append(" ").append(strVal);
											strFindQuery.append(mapResult.get("COLUMN_NAME")).append(" ").append(strVal);
										}else{
										mpTabConditions.put(String.valueOf(mapResult
												.get("COLUMN_NAME")), (String) mapConditions
												.get(mapResult.get("COLUMN_NAME")));
										strCountQuery.append(mapResult.get("COLUMN_NAME")).append(" ").append(strVal);
										strFindQuery.append(mapResult.get("COLUMN_NAME")).append(" ").append(strVal);
										/*if(strFindQuery.toString().trim().endsWith("=?"))
											strFindQuery.append(" AND ");
										strFindQuery.append(mapResult.get("COLUMN_NAME")).append("=?");
										if(strCountQuery.toString().trim().endsWith("=?"))
											strCountQuery.append(" AND ");
										strCountQuery.append(mapResult.get("COLUMN_NAME")).append("=?");
										}*/
										}
										iLoop++;
									}
								}
								
							}
							
								//below code for query generation
								for (Map<String, Object> mapResult : listinputColsConditions) {
									if (tbName.equals(mapResult.get("TABLE_NAME"))) {
										listCols.add(String.valueOf(mapResult.get("COLUMN_NAME")));
									}
								}
								//end of code
							
							//below code to find values based on condition
								long iTotRecords = jdbcTemplate.queryForInt(strCountQuery.toString());
								/*if((iRequiredRecordCount ==0 || iRequiredRecordCount <= 0) && iTotRecords >0 && iTotRecords<iRequiredRecordCount){
									iRequiredRecordCount = iTotRecords;
									testDataGenenarteDTO.setTotalRecordsGenerated(iTotRecords);
									lGeneratedReords=iTotRecords;
								}*/
							if(iRequiredRecordCount > 0 && iTotRecords < iRequiredRecordCount){
								iRequiredRecordCount = iTotRecords;
								testDataGenenarteDTO.setTotalRecordsGenerated(iTotRecords);
								lGeneratedReords= iTotRecords;
							}else if(iRequiredRecordCount == 0 && iTotRecords !=0){
								iRequiredRecordCount = iTotRecords;
								testDataGenenarteDTO.setTotalRecordsGenerated(iTotRecords);
								lGeneratedReords= iTotRecords;
							}
							
							
							//Based on FindQuery going to modify the query
							//end of query update
							String strQuery = "";//
							StringBuffer strUpdateQuery = new StringBuffer(mapTables.get(tbName));
							/*if(!mapPkNames.isEmpty()){
								strUpdateQuery.append(" WHERE ").append(mapConditionWithPKs.get(tbName)).append("=?");
								mapPositions.put(mapPositions.size()+1, mapConditionWithPKs.get(tbName));
							}else if(!mapUkNames.isEmpty()){
								strUpdateQuery.append(" WHERE ").append(mapUkNames.entrySet().iterator().next().get(tbName)).append("=?");
								mapPositions.put(mapPositions.size()+1, mapUkNames.get(tbName));
							}*/if(!mapFilterCondition.isEmpty()){
								strUpdateQuery.append(" WHERE ").append(mapFilterCondition.get(tbName)).append("=?");
								mapPositions.put(mapPositions.size()+1, mapConditionWithPKs.get(tbName));
							}else if(!listCols.isEmpty()){
								strUpdateQuery.append(" WHERE ");
								int iLoop = testDataGenenarteDTO.getMapinputConditionData() != null ? testDataGenenarteDTO.getMapinputConditionData().size() : 0;
								for(Map.Entry<String, Object> mapinputconditions: testDataGenenarteDTO.getMapinputConditionData().entrySet()){
									if(listCols.contains(mapinputconditions.getKey())){
										if(iLoop > 0)
											strUpdateQuery.append( " AND ");
									if(mapinputconditions.getValue().toString().contains(","))
										strUpdateQuery.append(mapinputconditions.getKey()).append(" IN (?").append(")");
									else									
									strUpdateQuery.append(mapinputconditions.getKey()).append(mapinputconditions.getValue().toString().contains("ISNULL") ? "IS NULL" : mapinputconditions.getValue().toString());
									iLoop++;
									mapPositions.put(mapPositions.size()+1, mapinputconditions.getKey());
									}									
								}
							}
							
							strQuery = strUpdateQuery.toString();
							
							/**
							 * going for get foreign key column table
							 */
							if (mapFkNames != null && !mapFkNames.isEmpty()) {
								mpFkValues = tdgTemplateDao.getTableNameByFkName(jdbcTemplate, tbName, mapFkNames);
							}
							Vector<String> vct = new Vector<String>();
							if (!mapPkNames.isEmpty()) {
								vct.add(mapConditionWithPKs.get(tbName));
								vct.add(tbName);
							}
							Map<String, List<String>> mpUkValues = new HashMap<String, List<String>>();
							Map<String, List<String>> mpNNkValues = new HashMap<String, List<String>>();
							Map<String, String> mpDummyValues = new HashMap<String, String>();
						//	Entry<String, String> entryPks = null;
							/*if (mapPkNames != null && !mapPkNames.isEmpty()) {
								entryPks = mapPkNames.entrySet().iterator().next();
							}*/
							/**
							 * Going for batch operations
							 */
							//String strQuery = mapTables.get(tbName);
							long lRecordsCount = iRequiredRecordCount;
							if (lstTabsNames.contains(tbName)) {
								lRecordsCount = iRequiredRecordCount;
							} else {
								for (String strValue : listRelations) {
									String strTabName = strValue
											.substring(0, strValue.indexOf("#"));
									if (strTabName.equals(tbName)) {
										bUniqCheck = true;
										break;
									}
								}
								if (!bUniqCheck) {
									lRecordsCount = 1;
								}
							}
							//for max value find
							String regexForNumbers = "\\d+";							
							/**
							 * Going to generate values based on constraint types which TDG Engine rectify
							 */
							List<String> lstGeneratedValues = new  ArrayList<String>();
							//GenerateRandom generateRandom = null;
							if (mapUkNames != null && !mapUkNames.isEmpty()) {
								for (Map.Entry<String, String> mapEntry : mapUkNames.entrySet()) {
									String strValue = mapEntry.getValue();
									String strKey = mapEntry.getKey();
									List<String> listGenValues = new ArrayList<String>();
									if ("DATE".equals(strValue) || "TIMESTAMP".equals(strValue)) {
										DateFormat dateFormat = new SimpleDateFormat(
												StringUtils.isEmpty(testDataGenenarteDTO
														.getDateFormate()) ? "dd/MMM/yyyy hh:mm:ss"
														: testDataGenenarteDTO.getDateFormate());
										Date date = new Date();
										String strDate = dateFormat.format(date) + "";
										for (int ii = 1; ii <= iRequiredRecordCount; ii++) {
											listGenValues.add(strDate);
										}
									} else if (mapDataDictionaryVals.containsKey(mapEntry.getKey())) {
										if (isDataConditional) {
											listGenValues.addAll(mapDataDictionaryVals.get(mapEntry
													.getKey()));
										} else {
											List<String> lstValues = new ArrayList<String>();
											if (mapConditions != null
													&& mapConditions.containsKey(mapEntry.getKey())) {
												//here added code for db_seq values for integrity purpose
												/*lstValues.add(mapConditions.get(mapEntry.getKey())
														+ "");*/
												//BEGIN DB SEQUENCE
												if(String.valueOf(mapConditions.get(mapEntry.getKey())).contains(TdgCentralConstant.TDG_DB_SEQ)){
													String strInputValue = String.valueOf(mapConditions.get(mapEntry.getKey())).substring(10);
													if(!StringUtils.isNotEmpty(strInputValue)){
													Vector<String> vt = new Vector<String>();
													vt.add(mapEntry.getKey());
													vt.add(tbName);
													String strDbSeq = TdgCentralConstant.getSpecificDBQuery(tdgTemplateDao.getDbType(),
															TdgCentralConstant.MAX_COLUMN_VALUE, vt);
													/*vctVal.addAll(tdgTemplateDao.generatePkValues(jdbcTemplate,
															strDbSeq, null, 1, tbName));*/
													String strVal = tdgTemplateDao.getMaxValue(jdbcTemplate,
															strDbSeq);
													if(strVal.matches(regexForNumbers))
														strDbSeq = TdgCentralConstant.getSpecificDBQuery(tdgTemplateDao.getDbType(),
																TdgCentralConstant.PRIMARY_KEY_FINAL, vt);
													lstValues.addAll(TdgAutoIncrementUtil.generateAutoIncrementValues(tdgTemplateDao.getMaxValue(jdbcTemplate,
															strDbSeq), (long)lRecordsCount+1));
													lstValues.remove(0);
													}else{
														lstValues.addAll(TdgAutoIncrementUtil.generateAutoIncrementValues(strInputValue, (long)lRecordsCount));
													}
													/*mapConditions.remove(mapEntry.getKey());
													mapConditions.put(mapEntry.getKey(), vctVal);*/
												}
												//END DB SEQUENCE
												
												
											} else {
												lstValues.addAll(mapDataDictionaryVals.get(mapEntry
														.getKey()));
											}
											//bifurcate the request based on sequence on random
											if(testDataGenenarteDTO.isSequenceOrder()){
											listGenValues.addAll(getSequenceValueFromList(lstValues,
													Integer.parseInt(strValue.substring(strValue
															.indexOf("#") + 1)),
													iRequiredRecordCount,false));
											}else{
												listGenValues.addAll(getRandomValueFromList(lstValues,
														Integer.parseInt(strValue.substring(strValue
																.indexOf("#") + 1)),
														iRequiredRecordCount,false));
											}
										}
									} else {
										Map<String, Map<String, String>> mapResult = mapTableWithSequence
												.get(tbName);
										if (mapUkNames != null && mapResult != null
												&& mapResult.containsKey(strKey)) {
											Map<String, String> mapSequences = mapResult
													.get(strKey);
											Map<String, Map<String, String>> mapRes = new HashMap<String, Map<String, String>>();
											mapRes.put(tbName, mapSequences);
											listGenValues.addAll(tdgTemplateDao.generatePkValues(jdbcTemplate,
													null, mapRes, lRecordsCount, tbName));
										} else {
											if(String.valueOf(mapConditions.get(mapEntry.getKey())).contains(TdgCentralConstant.TDG_DB_SEQ)){
												String strInputValue = String.valueOf(mapConditions.get(mapEntry.getKey())).substring(10);
												if(!StringUtils.isNotEmpty(strInputValue)){
												Vector<String> vt = new Vector<String>();
												vt.add(mapEntry.getKey());
												vt.add(tbName);
												String strDbSeq = TdgCentralConstant.getSpecificDBQuery(tdgTemplateDao.getDbType(),
														TdgCentralConstant.MAX_COLUMN_VALUE, vt);
												/*vctVal.addAll(tdgTemplateDao.generatePkValues(jdbcTemplate,
														strDbSeq, null, 1, tbName));*/
												String strVal = tdgTemplateDao.getMaxValue(jdbcTemplate,
														strDbSeq);
												if(strVal.matches(regexForNumbers))
													strDbSeq = TdgCentralConstant.getSpecificDBQuery(tdgTemplateDao.getDbType(),
															TdgCentralConstant.PRIMARY_KEY_FINAL, vt);
												listGenValues.addAll(TdgAutoIncrementUtil.generateAutoIncrementValues(tdgTemplateDao.getMaxValue(jdbcTemplate,
														strDbSeq), (long)lRecordsCount+1));
												listGenValues.remove(0);
												}else{
													listGenValues.addAll(TdgAutoIncrementUtil.generateAutoIncrementValues(strInputValue, (long)lRecordsCount));
												}
												/*mapConditions.remove(mapEntry.getKey());
												mapConditions.put(mapEntry.getKey(), vctVal);*/
											}else{
												Vector<String> vt = new Vector<String>();
												vt.add(mapEntry.getKey());
												vt.add(tbName);
												String strDbSeq = TdgCentralConstant.getSpecificDBQuery(tdgTemplateDao.getDbType(),
														TdgCentralConstant.MAX_COLUMN_VALUE, vt);
												/*vctVal.addAll(tdgTemplateDao.generatePkValues(jdbcTemplate,
														strDbSeq, null, 1, tbName));*/
												String strVal = tdgTemplateDao.getMaxValue(jdbcTemplate,
														strDbSeq);
												if(strVal.matches(regexForNumbers))
													strDbSeq = TdgCentralConstant.getSpecificDBQuery(tdgTemplateDao.getDbType(),
															TdgCentralConstant.PRIMARY_KEY_FINAL, vt);
												listGenValues.addAll(TdgAutoIncrementUtil.generateAutoIncrementValues(tdgTemplateDao.getMaxValue(jdbcTemplate,
														strDbSeq), (long)lRecordsCount+1));
												listGenValues.remove(0);
												//remove below for auto increment purpose
											/*generateRandom = new GenerateRandom(strValue.substring(
													0, strValue.indexOf("#")),
													Integer.parseInt(strValue.substring(strValue
															.indexOf("#") + 1)),
													iRequiredRecordCount);
											listGenValues = generateRandom.generateRandomString();*/
											}
										}
									}
									mpUkValues.put(mapEntry.getKey(), listGenValues);
									if (logger.isDebugEnabled())
										logger.debug(strClassName + strMethodName
												+ "generated unique constraint values :: "
												+ listGenValues.size());
									lstGeneratedValues.add(mapEntry.getKey());
								}
							}
							if (mapNNkNames != null && !mapNNkNames.isEmpty()) {
								int iSequnceCount = 0;
								for (Map.Entry<String, String> mapEntry : mapNNkNames.entrySet()) {
									String strValue = mapEntry.getValue();
									String strKey = mapEntry.getKey();
									List<String> listGenValues = new ArrayList<String>();
									if(!lstGeneratedValues.contains(mapEntry.getKey())){
									if ("DATE".equals(strValue) || "TIMESTAMP".equals(strValue)) {
										DateFormat dateFormat = new SimpleDateFormat(
												StringUtils.isEmpty(testDataGenenarteDTO
														.getDateFormate()) ? "dd/MMM/yyyy hh:mm:ss"
														: testDataGenenarteDTO.getDateFormate());
										Date date = new Date();
										String strDate = dateFormat.format(date) + "";
										//Not null constraint not required generate the all values
										/*for (int ii = 1; ii <= iRequiredRecordCount; ii++) {
											listGenValues.add(strDate);
										}*/								
										listGenValues.add(strDate);
									} else if (mapDataDictionaryVals.containsKey(mapEntry.getKey()) || mapDataDictionaryVals.containsKey(tbName+TdgCentralConstant.TDG_DOT+mapEntry.getKey())) {
										if (isDataConditional) {
											listGenValues.addAll(mapDataDictionaryVals.get(mapEntry
													.getKey()));
										} else {
											List<String> lstValues = new ArrayList<String>();
											if (mapConditions != null
													&& mapConditions.containsKey(mapEntry.getKey())) {
												/**
												 * Going to fetch random values from dictionary
												 */
												if(mapConditions.get(mapEntry.getKey()) instanceof List){
													List<Object> lst = (List<Object>) mapConditions.get(mapEntry.getKey());
													//bifurcate the request based on sequence on random
													if(testDataGenenarteDTO.isSequenceOrder()){
														if(lstSequence.isEmpty()){
															lstValues.add(lst.get(iSequnceCount)+"");
															iSequnceCount++;
														}else{
															int iIndex = (int)(Math.random() * lst.size());
															lstSequence.add(iIndex);
															lstValues.add(lst.get(iIndex)+"");
														}
													}else{
														lstValues.add(lst.get((int)(Math.random() * lst.size()))
																+ "");
													}
													
												}else{
													/*lstValues.add(mapConditions.get(mapEntry.getKey())
															+ "");*/
													//BEGIN DB SEQUENCE
													if(String.valueOf(mapConditions.get(mapEntry.getKey())).contains(TdgCentralConstant.TDG_DB_SEQ)){
														String strInputValue = String.valueOf(mapConditions.get(mapEntry.getKey())).substring(10);
														if(!StringUtils.isNotEmpty(strInputValue)){
														Vector<String> vt = new Vector<String>();
														vt.add(mapEntry.getKey());
														vt.add(tbName);
														String strDbSeq = TdgCentralConstant.getSpecificDBQuery(tdgTemplateDao.getDbType(),
																TdgCentralConstant.MAX_COLUMN_VALUE, vt);
														/*vctVal.addAll(tdgTemplateDao.generatePkValues(jdbcTemplate,
																strDbSeq, null, 1, tbName));*/
														String strVal = tdgTemplateDao.getMaxValue(jdbcTemplate,
																strDbSeq);
														if(strVal.matches(regexForNumbers))
															strDbSeq = TdgCentralConstant.getSpecificDBQuery(tdgTemplateDao.getDbType(),
																	TdgCentralConstant.PRIMARY_KEY_FINAL, vt);
														lstValues.addAll(TdgAutoIncrementUtil.generateAutoIncrementValues(tdgTemplateDao.getMaxValue(jdbcTemplate,
																strDbSeq), (long)lRecordsCount+1));
														lstValues.remove(0);
														}else{
															lstValues.addAll(TdgAutoIncrementUtil.generateAutoIncrementValues(strInputValue, (long)lRecordsCount));
														}
														/*mapConditions.remove(mapEntry.getKey());
														mapConditions.put(mapEntry.getKey(), vctVal);*/
													}
													//END DB SEQUENCE
												}
												
											} else {
												if(mapDataDictionaryVals.containsKey(tbName+TdgCentralConstant.TDG_DOT+mapEntry.getKey()))
													lstValues.addAll(mapDataDictionaryVals.get(tbName+TdgCentralConstant.TDG_DOT+mapEntry.getKey()));
												else
												lstValues.addAll(mapDataDictionaryVals.get(mapEntry
														.getKey()));
											}
											
											listGenValues.addAll(lstValues);
											//bifurcate the request based on sequence on random
											/*if(testDataGenenarteDTO.isSequenceOrder()){
											listGenValues.addAll(getSequenceValueFromList(lstValues,
													Integer.parseInt(strValue.substring(strValue
															.indexOf("#") + 1)),
													iRequiredRecordCount,false));
											}else{
												listGenValues.addAll(getRandomValueFromList(lstValues,
														Integer.parseInt(strValue.substring(strValue
																.indexOf("#") + 1)),
														iRequiredRecordCount,false));
											}*/
										}
									} else {
										Map<String, Map<String, String>> mapResult = mapTableWithSequence
												.get(tbName);
										if (mapUkNames != null && mapResult != null
												&& mapResult.containsKey(strKey)) {
											Map<String, String> mapSequences = mapResult
													.get(strKey);
											Map<String, Map<String, String>> mapRes = new HashMap<String, Map<String, String>>();
											mapRes.put(tbName, mapSequences);
											/*listGenValues.addAll(generatePkValues(jdbcTemplate,
													null, mapRes, lRecordsCount, tbName));*/
											listGenValues.addAll(tdgTemplateDao.generatePkValues(jdbcTemplate,
													null, mapRes, 1, tbName));
										} else {
											/*generateRandom = new GenerateRandom(strValue.substring(
													0, strValue.indexOf("#")),
													Integer.parseInt(strValue.substring(strValue
															.indexOf("#") + 1) != null && !"null".equals(strValue.substring(strValue
															.indexOf("#") + 1)) ? strValue.substring(strValue
																	.indexOf("#") + 1) : "20"),
													iRequiredRecordCount);
											listGenValues = generateRandom.generateRandomString();*/
											if(String.valueOf(mapConditions.get(mapEntry.getKey())).contains(TdgCentralConstant.TDG_DB_SEQ)){
												String strInputValue = String.valueOf(mapConditions.get(mapEntry.getKey())).substring(10);
												if(!StringUtils.isNotEmpty(strInputValue)){
												Vector<String> vt = new Vector<String>();
												vt.add(mapEntry.getKey());
												vt.add(tbName);
												String strDbSeq = TdgCentralConstant.getSpecificDBQuery(tdgTemplateDao.getDbType(),
														TdgCentralConstant.MAX_COLUMN_VALUE, vt);
												/*vctVal.addAll(tdgTemplateDao.generatePkValues(jdbcTemplate,
														strDbSeq, null, 1, tbName));*/
												String strVal = tdgTemplateDao.getMaxValue(jdbcTemplate,
														strDbSeq);
												if(strVal.matches(regexForNumbers))
													strDbSeq = TdgCentralConstant.getSpecificDBQuery(tdgTemplateDao.getDbType(),
															TdgCentralConstant.PRIMARY_KEY_FINAL, vt);
												listGenValues.addAll(TdgAutoIncrementUtil.generateAutoIncrementValues(tdgTemplateDao.getMaxValue(jdbcTemplate,
														strDbSeq), (long)lRecordsCount+1));
												listGenValues.remove(0);
												}else{
													listGenValues.addAll(TdgAutoIncrementUtil.generateAutoIncrementValues(strInputValue, (long)lRecordsCount));
												}
												/*mapConditions.remove(mapEntry.getKey());
												mapConditions.put(mapEntry.getKey(), vctVal);*/
											}else
											listGenValues.add(TdgCentralConstant.TDG_NOT_NULL_CONSTRAINT);
										}
									}
									mpNNkValues.put(mapEntry.getKey(), listGenValues);
									if (logger.isDebugEnabled())
										logger.debug(strClassName + strMethodName
												+ "generated unique constraint values :: "
												+ listGenValues);
									lstGeneratedValues.add(mapEntry.getKey());
								}
							}
								
							}
							if (mapDummyNames != null && !mapDummyNames.isEmpty()) {
								DateFormat dateFormat = new SimpleDateFormat(
										StringUtils.isEmpty(testDataGenenarteDTO
												.getDateFormate()) ? "dd/MMM/yyyy hh:mm:ss"
												: testDataGenenarteDTO.getDateFormate());
								Date date = new Date();
								String strDate = dateFormat.format(date) + "";
								for(Map.Entry<String, String> mapDummyVals : mapDummyNames.entrySet()){
									mpDummyValues.put((mapDummyVals.getKey()), strDate);
								}
								/*Entry<String, String> entryVals = mapDummyNames.entrySet()
										.iterator().next();
								String strValue = entryVals.getValue();
								String strKeyValue = entryVals.getKey();
								if ("DATE".equals(strValue) || "TIMESTAMP".equals(strValue)) {
									DateFormat dateFormat = new SimpleDateFormat(
											StringUtils.isEmpty(testDataGenenarteDTO
													.getDateFormate()) ? "dd/MMM/yyyy hh:mm:ss"
													: testDataGenenarteDTO.getDateFormate());
									Date date = new Date();
									String strDate = dateFormat.format(date) + "";
									mpDummyValues.put((entryVals.getKey()), strDate);
								} else if (mapDataDictionaryVals.containsKey(strKeyValue)) {
									if (isDataConditional) {
										mpDummyValues.put(entryVals.getKey(), mapDataDictionaryVals
												.get(strKeyValue).get(0));
									} else {
										Map<String, Map<String, String>> mapResult = mapTableWithSequence
												.get(tbName);
										if (mapUkNames != null && mapResult != null
												&& mapResult.containsKey(strKeyValue)) {
											Map<String, String> mapSequences = mapResult
													.get(strKeyValue);
											Map<String, Map<String, String>> mapRes = new HashMap<String, Map<String, String>>();
											mapRes.put(tbName, mapSequences);
											mpDummyValues.put(
													entryVals.getKey(),
													tdgTemplateDao.generatePkValues(jdbcTemplate, null, mapRes,
															lRecordsCount, tbName).get(0)
															+ "");
										} else {
											generateRandom = new GenerateRandom(strValue.substring(
													0, strValue.indexOf("#")),
													Integer.parseInt(strValue.substring(strValue
															.indexOf("#") + 1)), 1);
											List<String> listGenValues = generateRandom
													.generateRandomString();
											mpDummyValues.put((entryVals.getKey()),
													listGenValues.get(0));
										}
									}
								}else {
									generateRandom = new GenerateRandom(strValue.substring(
											0, strValue.indexOf("#")),
											Integer.parseInt(strValue.substring(strValue
													.indexOf("#") + 1)), 1);
									List<String> listGenValues = generateRandom
											.generateRandomString();
									mpDummyValues.put((entryVals.getKey()),
											listGenValues.get(0));
								}*/
							}
							
							
							//End of constraint related values
							/**
							 * Going to generate primary key value if sequence don't know
							 */
							@SuppressWarnings("rawtypes")
							Vector vctValues = new Vector();
							@SuppressWarnings("rawtypes")
							Map<String,Vector> mapPkVals = new HashMap<String,Vector>();
							if(!"GENERATESAMPLE".equalsIgnoreCase(testDataGenenarteDTO.getTdgFunctionType())){
							
						}else{
							for(int iGen=0;iGen<lRecordsCount;iGen++)
								vctValues.add("Will Generate");
						}
							if(!vct.isEmpty() && vct.get(0) != null)
							mapPkVals.put(vct.get(0), vctValues);
							//End of generation of primary key values
							
							//begin for DB_SEQ values generation for condition values
							Map<String,Object> mapTempConditions = new HashMap<String,Object>(mapConditions);
							for(Map.Entry<String, Object> mapEntry : mapTempConditions.entrySet()){
								if(!mapPkVals.containsKey(mapEntry.getKey()) && String.valueOf(mapEntry.getValue()).contains(TdgCentralConstant.TDG_DB_SEQ) && !mapUkNames.containsKey(mapEntry.getKey()) && !mapNNkNames.containsKey(mapEntry.getKey()) && mapTabsWithTables.get(tbName).contains(mapEntry.getKey())){
									String strInputValue = String.valueOf(mapTempConditions.get(mapEntry.getKey())).substring(10);
									@SuppressWarnings("rawtypes")
									Vector vctVal = new Vector();
									if(!StringUtils.isNotEmpty(strInputValue)){
									
									Vector<String> vt = new Vector<String>();
									vt.add(mapEntry.getKey());
									vt.add(tbName);
									String strDbSeq = TdgCentralConstant.getSpecificDBQuery(tdgTemplateDao.getDbType(),
											TdgCentralConstant.MAX_COLUMN_VALUE, vt);
									/*vctVal.addAll(tdgTemplateDao.generatePkValues(jdbcTemplate,
											strDbSeq, null, 1, tbName));*/
									String strVal = tdgTemplateDao.getMaxValue(jdbcTemplate,
											strDbSeq);
									if(strVal.matches(regexForNumbers))
									strDbSeq = TdgCentralConstant.getSpecificDBQuery(tdgTemplateDao.getDbType(),
											TdgCentralConstant.PRIMARY_KEY_FINAL, vt);
									vctVal.addAll(TdgAutoIncrementUtil.generateAutoIncrementValues(tdgTemplateDao.getMaxValue(jdbcTemplate,strDbSeq), (long)lRecordsCount+1));
									vctVal.remove(0);
									//below code to check varchar type max number values in db
									if(mapPkVals.containsKey(mapEntry.getKey())){
										vctValues.clear();
										vctValues.addAll(vctVal);
									}
											//mapPkVals.put(key, value)
									}else{
										vctVal.addAll(TdgAutoIncrementUtil.generateAutoIncrementValues(strInputValue, (long)lRecordsCount));
									}
									mapConditions.remove(mapEntry.getKey());
									mapConditions.put(mapEntry.getKey(), vctVal);
								}
							}
							//end of DB_SEQ values
							
							/**
							 * Going to push the data into DB in chunk wise
							 */
							//going to fetch batch wise unique values from db
                            jdbcTemplate.setFetchSize(TdgCentralConstant.BATCH_CHUNK_LIMIT);
                            List<Object> listKeys = jdbcTemplate.queryForList(strFindQuery.toString(),Object.class);
                            //List<Object> listKeys= listResult.get(0);
                            //end of batch fetch
                            
                            
							int iLoop = 1;
							int iMod = 0;
							int iElement = 0;
							if(lRecordsCount > TdgCentralConstant.BATCH_CHUNK_LIMIT){
		                    iLoop = (int) (lRecordsCount / TdgCentralConstant.BATCH_CHUNK_LIMIT);
                            iMod = (int) (lRecordsCount % TdgCentralConstant.BATCH_CHUNK_LIMIT);
							}
							int iNoOfTimes = 0;
                            if(iMod > 0){
                                iLoop++;
                            }
                           // List<Object[]> listParameters = null;
                            for(int i2 = 0;i2 < iLoop;i2++){
                                if(iMod > 0 && i2 == iLoop - 1){
                                    iNoOfTimes = iMod;
                                }else{
                                	if(lRecordsCount > TdgCentralConstant.BATCH_CHUNK_LIMIT){
                                    iNoOfTimes = TdgCentralConstant.BATCH_CHUNK_LIMIT;
                                	}else{
                                		iNoOfTimes = (int) lRecordsCount;
                                	}
                                }
                                List<Object[]> listParameters  = new ArrayList<Object[]>();
                                
                                for(int iRecord = 0;iRecord < iNoOfTimes;iRecord++){
                                	
        							//for (int iRecords = 0; iRecords < lRecordsCount; iRecords++) {
        								//introduced for sync values of each row from manual dictionary
        								int iDictionaryPickedRowId = 0;
        								if (listRelations != null) {
        									Object[] obj = new Object[mapPositions.size()];
        									for (int k = 0; k < mapPositions.size(); k++) {
        										//if (k == 0 && !vctValues.isEmpty()) {
        										//mapConditionWithPKs.get(tbName)
        										if(mapPkVals.containsKey(mapPositions.get(k+1)) && !mapPkVals.get(mapPositions.get(k+1)).isEmpty()){
        											obj[k] = vctValues.get(iElement);
        										} else if (mpFkValues != null
        												&& mpFkValues.containsKey(mapPositions.get(k + 1))) {
        											List<String> listFks = mapPks.get(mpFkValues
        													.get(mapPositions.get(k + 1)));
        											if (listFks != null && !listFks.isEmpty()) {
        												if (listFks.size() == 1) {
        													obj[k] = listFks.get(0);
        												} else {
        													obj[k] = listFks.get(iElement);
        												}
        											} else if (mapDataDictionaryVals != null
        													&& mapDataDictionaryVals
        															.containsKey(mapPositions.get(k + 1))){
        													//&& isDataConditional) { // removed for fk values are there in manual dictionaries
        												obj[k] = mapDataDictionaryVals.get(
        														mapPositions.get(k + 1)).get(iElement);
        											}else{
        												//if(TdgCentralConstant.SCHEMA_GENERATION_TYPE_DB.equals(testDataGenenarteDTO.getGenerationType())){
        												//obj[k] = 0;
        												obj[k] = null;
        											}
        										} else if (mapInputDepends != null
        												&& !mapInputDepends.isEmpty()
        												&& mapInputDepends.containsKey(mapPositions
        														.get(k + 1))) {
        											Vector<String> vctDepends = mapInputDepends
        													.get(mapPositions.get(k + 1));
        											if (vctDepends != null && !vctDepends.isEmpty()) {
        												if (vctDepends.size() == 1) {
        													obj[k] = vctDepends.get(0);
        												} else {
        													obj[k] = vctDepends.get(iElement);
        												}
        											}// mapReverseDependents
        										} else if (mapReverseDependents != null
        												&& !mapReverseDependents.isEmpty()
        												&& mapReverseDependents.containsKey(mapPositions
        														.get(k + 1))) {
        											Vector<String> vctDepends = mapInputDepends
        													.get(mapReverseDependents.get(mapPositions
        															.get(k + 1)));
        											if (vctDepends != null && !vctDepends.isEmpty()) {
        												if (vctDepends.size() == 1) {
        													obj[k] = vctDepends.get(0);
        												} else {
        													obj[k] = vctDepends.get(iElement);
        												}
        											}// mapReverseDependents
        										} else if (mapDummyNames != null
        												&& mapDummyNames.containsKey(mapPositions
        														.get(k + 1))) {
        											obj[k] = mpDummyValues.get(mapPositions.get(k + 1));
        										} else if (mpUkValues != null
        												&& mpUkValues.containsKey(mapPositions.get(k + 1))) {
        											if(mpUkValues.get(mapPositions.get(k + 1)) != null && mpUkValues.get(mapPositions.get(k + 1)).size() ==1){
        												obj[k] = mpUkValues.get(mapPositions.get(k + 1)).get(
        														0);
        											}else{
        											obj[k] = mpUkValues.get(mapPositions.get(k + 1)).get(
        													iElement);
        											}
        										} else if (mpNNkValues != null
        												&& mpNNkValues.containsKey(mapPositions.get(k + 1))) {
        											//System.out.println(mapPositions.get(k + 1));
        											if(mpNNkValues.get(mapPositions.get(k + 1)) != null && mpNNkValues.get(mapPositions.get(k + 1)).size() ==1){
        												obj[k] = mpNNkValues.get(mapPositions.get(k + 1)).get(
        														0);
        											}else{

        												if(mpNNkValues != null && mpNNkValues.size() != lRecordsCount){
        													obj[k] = mpNNkValues.get(mapPositions.get(k + 1)).get((int)(Math.random() * (mpNNkValues.get(mapPositions.get(k + 1)).size())));
        												}else{
        											obj[k] = mpNNkValues.get(mapPositions.get(k + 1)).get(
        													iElement);
        												}
        											/*
        												if(mpNNkValues != null && mapNNkNames.size() != lRecordsCount){
        													obj[k] = mpNNkValues.get(mapPositions.get(k + 1)).get((int)(Math.random() * mapNNkNames.size()));
        												}else{
        											obj[k] = mpNNkValues.get(mapPositions.get(k + 1)).get(
        													iElement);
        												}
        											*/}
        										} else if (mapConditions != null
        												&& mapConditions.containsKey(mapPositions
        														.get(k + 1))) {
        											if(mapConditions.get(mapPositions.get(k + 1)) instanceof List){
        												/**
        												 * Going to fetch random values from dictionary
        												 */
        												List<Object> lst = (List<Object>) mapConditions.get(mapPositions.get(k + 1));
        												
        												if (!testDataGenenarteDTO.isSequenceOrder()) {
        													if(lst.size() >= iNoOfTimes){
        														if(lst.size() == lRecordsCount)
        															obj[k]= lst.get(iElement);
        														else
        														obj[k] = lst.get(iRecord);
        													}else
        													obj[k] = lst.get((int) (Math.random() * lst
        															.size()));
        												}else{
        													if(iDictionaryPickedRowId == 0)
        													iDictionaryPickedRowId = !lstSequence.isEmpty() ?lstSequence.get(iElement):(int)(Math.random() * lst.size());
        													obj[k] = lst.get(iDictionaryPickedRowId);
        												}
        												
        											}else{
        												obj[k] = mapConditions.get(mapPositions.get(k + 1));
        											}
        										} else if (mapDataDictionaryVals != null
        												&& mapDataDictionaryVals.containsKey(tbName+TdgCentralConstant.TDG_DOT+mapPositions.get(k + 1))) {
        											if (isDataConditional) {
        												obj[k] = mapDataDictionaryVals.get(tbName+TdgCentralConstant.TDG_DOT+mapPositions.get(k + 1)).get(iElement);
        											} else {
        												if(iDictionaryPickedRowId == 0 && !mapDataDictionaryVals.isEmpty())
        													iDictionaryPickedRowId = !lstSequence.isEmpty() ?lstSequence.get(iElement):(int)(Math.random() * mapDataDictionaryVals
        															.get(tbName+TdgCentralConstant.TDG_DOT+mapPositions.get(k + 1))
        															.size());
        												/**
        												 * Going to fetch random values from dictionary
        												 */
        												obj[k] = mapDataDictionaryVals
        														.get(tbName+TdgCentralConstant.TDG_DOT+mapPositions.get(k + 1))
        														.get(iDictionaryPickedRowId);
        											}
        										}else if (mapDataDictionaryVals != null
        												&& mapDataDictionaryVals.containsKey(mapPositions
        														.get(k + 1))) {
        											if (isDataConditional) {
        												obj[k] = mapDataDictionaryVals.get(
        														mapPositions.get(k + 1)).get(iElement);
        											} else {
        												if(iDictionaryPickedRowId == 0 && !mapDataDictionaryVals.isEmpty())
        													iDictionaryPickedRowId = !lstSequence.isEmpty() ?lstSequence.get(iElement):(int)(Math.random() * mapDataDictionaryVals
        															.get(mapPositions.get(k + 1))
        															.size());
        												/**
        												 * Going to fetch random values from dictionary
        												 */
        												obj[k] = mapDataDictionaryVals
        														.get(mapPositions.get(k + 1))
        														.get(iDictionaryPickedRowId);
        											}
        										} else {
        											if(!StringUtils.isEmpty(mapFilterCondition.get(tbName)))
        											obj[k] = listKeys.get((int) ((i2*TdgCentralConstant.BATCH_CHUNK_LIMIT)+iRecord));
        											else{
        												obj[k] = testDataGenenarteDTO.getMapinputConditionData().get(mapPositions.get(k + 1));
        											}
        											/*if (mapDataDictionaryVals != null
        													&& !mapDataDictionaryVals.isEmpty()) {
        												if(iDictionaryPickedRowId == 0)
        													iDictionaryPickedRowId = !lstSequence.isEmpty() ?lstSequence.get(iElement):(int)(Math.random() * mapDataDictionaryVals
        															.get(mapPositions.get(k + 1))
        															.size());
        												*//**
        												 * Going to fetch random values from dictionary
        												 *//*
        												obj[k] = mapDataDictionaryVals
        														.get(mapPositions.get(k + 1))
        														.get(iDictionaryPickedRowId);
        											}else{*/
        												//obj[k] = 0;
        											//}
        										}
        										/**
        										 * Going for save condition values
        										 */
        										if (listConditionColNames.contains(mapPositions.get(k + 1))) {
        											if (mapDepends.get(mapPositions.get(k + 1)) != null) {
        												Vector<String> vctDependentValues = mapDepends
        														.get(mapPositions.get(k + 1));
        												if (lRecordsCount >= vctDependentValues.size()) {
        													vctDependentValues.add(obj[k] + "");
        												}
        											} else {
        												Vector<String> vctDependentValues = new Vector<String>();
        												vctDependentValues.add(obj[k] + "");
        												mapDepends.put(mapPositions.get(k + 1),
        														vctDependentValues);
        											}
        										}
        										if (mapConditionValues.containsKey(mapPositions.get(k + 1))) {
        											List<String> lstValues = mapConditionValues
        													.get(mapPositions.get(k + 1));
        											lstValues.add(obj[k] + "");
        										}
        									}									
        									// here need to check the conditional values for concatenation or suffix or prefix the values of generated column values at dynamically
        									for (int k = 0; k < mapPositions.size(); k++) {
        										String strFindValue = obj[k] != null ? obj[k].toString() : "";
        										if(strFindValue.contains("$")){
        											StringBuffer strBuffer = new StringBuffer();
        											String[] splitsvalues = strFindValue.split("\\$");
        											for(int iSize=0;iSize<splitsvalues.length;iSize++){
        												// need to check the validation of column name based on position
        												//splitsvalues[iSize] is nothing but column name basically here(assumption)
        												String strCondition = splitsvalues[iSize].contains("{") ? splitsvalues[iSize].substring(0,splitsvalues[iSize].indexOf("{")) : splitsvalues[iSize];
        												if(mapPositions.containsValue(strCondition)){
        													for (Map.Entry<Integer, String> mapEntry : mapPositions.entrySet()) {
        														if(mapEntry.getValue().equals(strCondition)){
        															String strTemp = obj[mapEntry.getKey()-1].toString();
        															strBuffer.append(splitsvalues[iSize].contains("{") ? strTemp.substring(Integer.parseInt(splitsvalues[iSize].substring(splitsvalues[iSize].indexOf("{")+1,splitsvalues[iSize].indexOf(",")) != null ? splitsvalues[iSize].substring(splitsvalues[iSize].indexOf("{")+1,splitsvalues[iSize].indexOf(",")).trim() : "0"),Integer.parseInt(splitsvalues[iSize].substring(splitsvalues[iSize].indexOf(",")+1,splitsvalues[iSize].indexOf("}")) != null ? splitsvalues[iSize].substring(splitsvalues[iSize].indexOf(",")+1,splitsvalues[iSize].indexOf("}")).trim() : "0")) : strTemp);
        															//need to verify the break statement of map position values
        															break;
        														}
        													}
        												}else{
        													strBuffer.append(splitsvalues[iSize]);
        												}
        												//strBuffer.append(splitsvalues[iSize]);												
        											}
        											//finally set the result to obj parameter which is going to insert the data
        											//obj[k] = strBuffer.toString();
        											
        											//Going to check the dashboard related stuff values			
        										if(mapConditionValues.containsKey(mapPositions.get(k+1))){
        											List<String> lstValues = mapConditionValues.get(mapPositions.get(k+1));
        											if(lstValues.contains(obj[k].toString())){
        												lstValues.remove(strFindValue);
        												lstValues.add(strBuffer.toString());												
        												}
        											mapConditionValues.put(mapPositions.get(k+1), lstValues);
        											}
        										obj[k] = strBuffer.toString();
        										}else if(strFindValue.contains("{") && strFindValue.contains("}")){
        											StringBuffer strBuffer = new StringBuffer();
        											String strCondition = strFindValue.contains("{") ? strFindValue.substring(0,strFindValue.indexOf("{")) : strFindValue;
    												if(mapPositions.containsValue(strCondition)){
    													for (Map.Entry<Integer, String> mapEntry : mapPositions.entrySet()) {
    														if(mapEntry.getValue().equals(strCondition)){
    															String strTemp = obj[mapEntry.getKey()-1].toString();
    															strBuffer.append(strFindValue.contains("{") ? strTemp.substring(Integer.parseInt(strFindValue.substring(strFindValue.indexOf("{")+1,strFindValue.indexOf(",")) != null ? strFindValue.substring(strFindValue.indexOf("{")+1,strFindValue.indexOf(",")).trim() : "0"),Integer.parseInt(strFindValue.substring(strFindValue.indexOf(",")+1,strFindValue.indexOf("}")) != null ? strFindValue.substring(strFindValue.indexOf(",")+1,strFindValue.indexOf("}")).trim() : "0")) : strTemp);
    															//need to verify the break statement of map position values
    															break;
    														}
    													}
    												}else{
    													strBuffer.append(strFindValue);
    												}
    												if(mapConditionValues.containsKey(mapPositions.get(k+1))){
            											List<String> lstValues = mapConditionValues.get(mapPositions.get(k+1));
            											if(lstValues.contains(obj[k].toString())){
            												lstValues.remove(strFindValue);
            												lstValues.add(strBuffer.toString());												
            												}
            											mapConditionValues.put(mapPositions.get(k+1), lstValues);
            											}
            										obj[k] = strBuffer.toString();
        										}
        									}									
        									listParameters.add(obj);
        								}
        							//}
        							bUniqCheck = false;
        							if (logger.isDebugEnabled()) {
        								logger.debug(strClassName + strMethodName
        										+ " Query going to be fire is  :: " + strQuery);
        								logger.debug(strClassName + strMethodName
        										+ "parameters for batch update are  :: " + listParameters);
        							}
        							
        							iElement++;
                                }
                                
                                /**
    							 * Going to check the generation type of test data
    							 */
    							if(TdgCentralConstant.SCHEMA_GENERATION_TYPE_DB.equals(testDataGenenarteDTO.getGenerationType())){
    							/**
    							 * Going to fire the result
    							 */
    								try{
    							int[] iBatchResult = jdbcTemplate.batchUpdate(strQuery, listParameters);
    							logger.info(strClassName + strMethodName
    									+ " batch updation is done and its result is :: "
    									+ iBatchResult.length+" and table name is --> "+tbName);
    								}catch(Exception e){
    									logger.info("batch update error occurred : "+e.getMessage());
    									logger.info("Values passed are : "+listParameters.toString());
    									throw e;
    								}
    							
    							}else if(testDataGenenarteDTO.getGenerationType().contains(TdgCentralConstant.SCHEMA_GENERATION_TYPE_XLS)){
    								try {
    									//added below code to set fixed length
    									Map<String, String> mapColumnsOfTab = new HashMap<String,String>();
    									if(testDataGenenarteDTO.getGenerationType().endsWith("FL")){
    										List<Map<String,Object>> mapColumnsOfTabs = tdgTemplateDao.getSequenceColsByTableName(
    											jdbcTemplate, tbName, null);
    										
    										for (Map<String, Object> mapValues : mapColumnsOfTabs) {
    											mapColumnsOfTab.put(mapValues.get("COLUMN_NAME")
    														.toString().toUpperCase(),String.valueOf(mapValues.get("DATA_LENGTH")));
    										}
    									}
    									//end of coding
    									String strFilename = strTempPath +"/"+ tbName;				    									
    									//String strFilename = "\\\\in-pnq-coe30\\ATDG/"+ tbName+(new Random().nextInt(100000000));
    									logger.info("file path is : "+strFilename);
    									//Going to append Headers as column names in List<Object[]>
    									List<Object[]> lstObject = new ArrayList<Object[]>();
    									//1.add headers
    									/*if(testDataGenenarteDTO.getSchemaname().equals("TK24")){
    										int[] iBatchResult = jdbcTemplate.batchUpdate(strQuery, listParameters);
    										if (i2 == 0) {
												Object[] objHeaders = new Object[mapPositions
														.size()];
												for (int iSize = 0; iSize < mapPositions.size(); iSize++) {
													if("ID".equalsIgnoreCase(mapPositions.get(iSize + 1))){
														objHeaders[iSize] = "_rowid";
													}else if("SwitchToWindow".equalsIgnoreCase(mapPositions.get(iSize + 1))){
														objHeaders[iSize] = "SwitchToWindow";
													}else if("givenname".equalsIgnoreCase(mapPositions.get(iSize + 1))){
														objHeaders[iSize] = "Type@givename";
													}else if("fullname".equalsIgnoreCase(mapPositions.get(iSize + 1))){
														objHeaders[iSize] = "Type@FullName";
													}else if("shortname".equalsIgnoreCase(mapPositions.get(iSize + 1))){
														objHeaders[iSize] = "Type@ShortName";
													}else if("mnemonic".equalsIgnoreCase(mapPositions.get(iSize + 1))){
														objHeaders[iSize] = "Type@Mnemonic";
													}else if("gender".equalsIgnoreCase(mapPositions.get(iSize + 1))){
														objHeaders[iSize] = "Select@Gender";
													}else if("title".equalsIgnoreCase(mapPositions.get(iSize + 1))){
														objHeaders[iSize] = "Select@title";
													}else if("sector".equalsIgnoreCase(mapPositions.get(iSize + 1))){
														objHeaders[iSize] = "Select@Sector";
													}else if("language".equalsIgnoreCase(mapPositions.get(iSize + 1))){
														objHeaders[iSize] = "Select@Language";
													}else{
													objHeaders[iSize] = mapPositions.get(iSize + 1);
													}
												}
												lstObject.add(objHeaders);
											}*/
    									//}else{
											if (i2 == 0) {
												Object[] objHeaders = new Object[mapPositions
														.size()];
												for (int iSize = 0; iSize < mapPositions.size(); iSize++) {
													objHeaders[iSize] = mapPositions.get(iSize + 1);
												}
												lstObject.add(objHeaders);
											}
    									//}
    									//2.add generated values
    									lstObject.addAll(listParameters);
    									
    									//3.generate the excel file
    									//following for IDFC formate
    									//ExcelUtils.doGenerateExcelXLSFile(lstObject, strFilename+".xls", mapPositions.size(),mapPositions,lstSequenceColsAndRequired);
    									//following for Enterprise version
											if (i2 == 0) {
												ExcelUtils.doGenerateExcelXLSFile(lstObject,
														strFilename + ".xls", mapPositions.size(),
														mapPositions, null, false,mapColumnsOfTab);
											} else {
												ExcelUtils.doGenerateExcelXLSFile(lstObject,
														strFilename + ".xls", mapPositions.size(),
														mapPositions, null, true,mapColumnsOfTab);
											}
											
											/*if(testDataGenenarteDTO.getSchemaname().equals("TK24")){
												
												if (i2 == 0) {
													ExcelUtils.doGenerateExcelXLSFile(lstObject,
															"\\\\in-pnq-coe30\\ATDG\\" + tbName+new Random().nextInt(100000000)+".xls", mapPositions.size(),
															mapPositions, null, false,mapColumnsOfTab);
												} else {
													ExcelUtils.doGenerateExcelXLSFile(lstObject,
															"\\\\in-pnq-coe30\\ATDG\\" + tbName+new Random().nextInt(100000000) + ".xls", mapPositions.size(),
															mapPositions, null, true,mapColumnsOfTab);
												}
												
											}*/
    								
    									
    									
    									//follwoing for flat file
    									/*if(strFlatFilesPaths.toString().isEmpty())
    									strFlatFilesPaths.append(System.getProperty("user.home")).append("/Downloads/").append(strFilename+".xls");
    									else
    										strFlatFilesPaths.append("#").append(System.getProperty("user.home")).append("/Downloads/").append(strFilename+".xls");*/
    									if(StringUtils.isEmpty(strStatusDescription.toString())){
    										strStatusDescription.append(" Generated sucessfully and created file(s) are : ").append(strFilename);
    										}else{
    											strStatusDescription.append(",").append(strFilename);
    										}
    									
    								} catch (Exception e) {
    									e.printStackTrace();
    								} 
    							  }else if(TdgCentralConstant.SCHEMA_GENERATION_TYPE_XML.equals(testDataGenenarteDTO.getGenerationType())){
    								  String strFilename = strTempPath +"/"+ tbName;    									    									
  									
  									//Going to append Headers as column names in List<Object[]>
  									List<Object[]> lstObject = new ArrayList<Object[]>();
  									//1.add headers
											if (i2 == 0) {
												Object[] objHeaders = new Object[mapPositions
														.size()];
												for (int iSize = 0; iSize < mapPositions.size(); iSize++) {
													objHeaders[iSize] = mapPositions.get(iSize + 1);
												}
												lstObject.add(objHeaders);
											}
  									//2.add generated values
  									lstObject.addAll(listParameters);
  									
  									//3.generate the excel file
  									//following for IDFC formate
  									//ExcelUtils.doGenerateExcelXLSFile(lstObject, strFilename+".xls", mapPositions.size(),mapPositions,lstSequenceColsAndRequired);
  									//following for Enterprise version
											if (i2 == 0) {
												TdgXmlUtil.doGenerateXMLFile(lstObject,
														strFilename + ".xml", mapPositions.size(),
														mapPositions,null, false,tbName);
											} else {
												TdgXmlUtil.doGenerateXMLFile(lstObject,
														strFilename + ".xml", mapPositions.size(),
														mapPositions, null, true,tbName);
											}
  								
  									
  									
  									//follwoing for flat file
  									/*if(strFlatFilesPaths.toString().isEmpty())
  									strFlatFilesPaths.append(System.getProperty("user.home")).append("/Downloads/").append(strFilename+".xls");
  									else
  										strFlatFilesPaths.append("#").append(System.getProperty("user.home")).append("/Downloads/").append(strFilename+".xls");*/
  									if(StringUtils.isEmpty(strStatusDescription.toString())){
  										strStatusDescription.append(" Generated sucessfully and created file(s) are : ").append(strFilename);
  										}else{
  											strStatusDescription.append(",").append(strFilename);
  										}
    							  }else if(testDataGenenarteDTO.getGenerationType().contains(TdgCentralConstant.SCHEMA_GENERATION_TYPE_CSV)){
    								  
    								//added below code to set fixed length
  									Map<String, String> mapColumnsOfTab = new HashMap<String,String>();
  									if(testDataGenenarteDTO.getGenerationType().endsWith("FL")){
  										List<Map<String,Object>> mapColumnsOfTabs = tdgTemplateDao.getSequenceColsByTableName(
  											jdbcTemplate, tbName, null);
  										
  										for (Map<String, Object> mapValues : mapColumnsOfTabs) {
  											mapColumnsOfTab.put(mapValues.get("COLUMN_NAME")
  														.toString().toUpperCase(),String.valueOf(mapValues.get("DATA_LENGTH")));
  										}
  									}
  									//end of coding
    								  String strFilename = strTempPath +"/"+ tbName+".csv";
    									
    									//Going to append Headers as column names in List<Object[]>
    									List<Object[]> lstObject = new ArrayList<Object[]>();
										if (i2 == 0) {
											// 1.add headers
											Object[] objHeaders = new Object[mapPositions.size()];
											for (int iSize = 0; iSize < mapPositions.size(); iSize++) {
												objHeaders[iSize] = mapPositions.get(iSize + 1);
											}
											lstObject.add(objHeaders);
										}
    									//2.add generated values
    									lstObject.addAll(listParameters);
    									File file = new File(strFilename);
    									/*if(strFlatFilesPaths.toString().isEmpty())
    										strFlatFilesPaths.append(System.getProperty("user.home")).append("/Downloads/").append(strFilename+".csv");
    										else
    											strFlatFilesPaths.append("#").append(System.getProperty("user.home")).append("/Downloads/").append(strFilename+".csv");*/
    									
    								FileWriterWithEncoding filewriter = null;
    								try {
    									if (i2 == 0) {
    									filewriter = new FileWriterWithEncoding(file,"UTF-8");
    									}else{
    										filewriter = new FileWriterWithEncoding(file,"UTF-8",true);
    									}
    									//following is for IDFC
    									String strCSVSeperator = testDataGenenarteDTO.getCsvFileSeperator();
    									CharSequence cach =null;
    									if(lstSequenceColsAndRequired != null && !lstSequenceColsAndRequired.isEmpty())
    										cach = new StringBuffer(CSVGenerator.getCSVForObjects(mapPositions, iRequiredRecordCount, listParameters,lstSequenceColsAndRequired,mapColumnsOfTab,strCSVSeperator));
    									//follwing Enterprise version
    									else
    									cach = new StringBuffer(CSVGenerator.getCSVForList(lstObject,mapColumnsOfTab,strCSVSeperator));
    									if(cach != null)
    									filewriter.append(cach);
    								} catch (IOException e) {
    									e.printStackTrace();
    								}finally{
    									if(filewriter != null){
    										try {
    											filewriter.close();
    										} catch (IOException e) {
    											e.printStackTrace();
    										}
    									}
    									if(StringUtils.isEmpty(strStatusDescription.toString())){
    									strStatusDescription.append(" Generated sucessfully and created file(s) are : ").append(file.getAbsolutePath());
    									}else{
    										strStatusDescription.append(",").append(file.getAbsolutePath());
    									}
    								}
    								
    								
    							}
    							
    							if(strStatusDescription != null && strStatusDescription.length() > 0){
    								strStatusDescription.append(',');
    							}
    							strStatusDescription.append(tbName);
                            }
                            
                            
                            
							
							
							/**
							 * Going to batch update in
							 */
							/**
							 * Going to fetch the primary key values
							 * 
							 */
							// 1. check from dummy name insert
							// if (i != 1) {
							StringBuffer strBuffer = new StringBuffer(
									TdgCentralConstant.getReplacedValue(
											TdgCentralConstant.SELECT_FOR_PK_VALUES, vct));
							String strType = mapTableWithPk.get(tbName);
							Vector<String> listPKs = new Vector<String>();
							if (StringUtils.isNotEmpty(strType) && strType.contains("#")) {
								if (mapPkVals != null && mapPkVals.get(mapConditionWithPKs.get(tbName)).isEmpty()) {
									if (mapDummyNames != null && !mapDummyNames.isEmpty()) {
										String strkey = mapDummyNames.entrySet().iterator().next()
												.getKey();
										List<String> listVals = new ArrayList<String>();
										listVals.add(mapDummyNames.get(strkey));
										strBuffer.append(TdgCentralConstant.generateString(
												strType.substring(0, strType.indexOf("#")), strkey,
												listVals));
										Object[] obj = new Object[listVals.size()];
										for (int k = 0; k < listVals.size(); k++) {
											obj[k] = mpDummyValues.get(strkey);
										}
										List<Map<String, Object>> listResult = jdbcTemplate
												.queryForList(strBuffer.toString(), obj);
										for (Map<String, Object> map : listResult) {
											listPKs.add(String.valueOf(map.get(mapPositions.get(1))));
										}
										mapPks.put(tbName, listPKs);
									} else if (mpTabConditions != null
											&& !mpTabConditions.isEmpty()) {
										int iCount = 0;
										List<List<String>> lstListVals = new ArrayList<List<String>>();
										for (Entry<String, String> strMapKey : mpTabConditions
												.entrySet()) {
											List<String> listVals = new ArrayList<String>();
											listVals.add(String.valueOf(mpTabConditions
													.get(strMapKey.getKey())));
											strBuffer.append(TdgCentralConstant.generateString(
													strType.substring(0, strType.indexOf("#")),
													strMapKey.getKey(), listVals));
											lstListVals.add(listVals);
											iCount++;
										}
										Object[] obj = new Object[iCount];
										for (int k = 0; k < lstListVals.size(); k++) {
											for (int j = 0; j < lstListVals.get(k).size(); j++) {
												obj[k] = lstListVals.get(k).get(j);
											}
										}
										List<Map<String, Object>> listResult = jdbcTemplate
												.queryForList(strBuffer.toString(), obj);
										for (Map<String, Object> map : listResult) {
											listPKs.add(String.valueOf(map.get(mapPositions.get(1))));
										}
										mapPks.put(tbName, listPKs);
									} else if (mpFkValues != null && !mpFkValues.isEmpty()) {
										for (Entry<String, String> strMapKey : mpFkValues
												.entrySet()) {
											List<String> lstFkKeys = mapPks.get(mpFkValues
													.get(strMapKey));
											strBuffer.append(TdgCentralConstant.generateString("",
													strMapKey.getKey(), lstFkKeys));
											Object[] obj = new Object[lstFkKeys.size()];
											for (int j = 0; j < lstFkKeys.size(); j++) {
												obj[j] = lstFkKeys.get(j);
											}
											List<Map<String, Object>> listResult = jdbcTemplate
													.queryForList(strBuffer.toString(), obj);
											for (Map<String, Object> map : listResult) {
												listPKs.add(String.valueOf(map.get(mapPositions
														.get(1))));
											}
											mapPks.put(tbName, listPKs);
											break;
										}
									} else if (mpUkValues != null && !mpUkValues.isEmpty()) {
										for (Entry<String, List<String>> strMapKey : mpUkValues
												.entrySet()) {
											List<String> lstFkKeys = mpUkValues.get(mpFkValues
													.get(strMapKey));
											strBuffer.append(TdgCentralConstant.generateString("",
													strMapKey.getKey(), lstFkKeys));
											Object[] obj = new Object[lstFkKeys.size()];
											for (int j = 0; j < lstFkKeys.size(); j++) {
												obj[j] = lstFkKeys.get(j);
											}
											List<Map<String, Object>> listResult = jdbcTemplate
													.queryForList(strBuffer.toString(), obj);
											for (Map<String, Object> map : listResult) {
												listPKs.add(String.valueOf(map.get(mapPositions
														.get(1))));
											}
											mapPks.put(tbName, listPKs);
											break;
										}
									}
								} else {
									mapPks.put(tbName, mapPkVals.get(mapConditionWithPKs.get(tbName)));
								}
							} else {
								mapPks.put(tbName, mapPkVals.get(mapConditionWithPKs.get(tbName)));
							}
							if (logger.isDebugEnabled())
								logger.debug(strClassName + strMethodName
										+ " inserted records primary keys are :: "
										+ mapPks.get(tbName));
							// }
						}
					}//end of filter closer
					}
				}
				iIteratorCount++;
			}
			
			/*if(!isConditiontableExist){
				testDataGenenarteDTO.setDataConditionalTabNames(null);
			}*/
		}
		logger.debug(strClassName + strMethodName + " return from generateQueriesAndDumping");
		return mapDepends;
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	private Vector generatePkByConditionValues(String trim, long lRecordsCount){
		Vector vct = new Vector();
		for (int i = 1; i <= lRecordsCount; i++) {
			vct.add(trim + i);
		}
		return vct;
	}

	public Map<String, Vector<String>> getMapDependentValues(){
		return mapDependentValues;
	}

	public void setMapDependentValues(Map<String, Vector<String>> mapDependentValues){
		this.mapDependentValues = mapDependentValues;
	}

	/**
	 * This method is used to get random values from list
	 * @param listPassedVals
	 * @param iRequiredCount
	 * @return
	 */
	private List<String> getRandomValueFromList(List<String> listPassedVals, int length,
			long iRequiredCount, boolean isUniqueConstraint){
		List<String> lstResult = new ArrayList<String>();
		if (listPassedVals != null && !listPassedVals.isEmpty()) {
			int iListSize = listPassedVals.size();
			GenerateRandom generateRandom = new GenerateRandom(TdgCentralConstant.ORACLE_NUMBER,
					length, 1);
			int iPassedValueLength = length;
			String strVal = "";
			for (int i = 0; i < iRequiredCount; i++) {
				if (isUniqueConstraint) {
					Random r = new Random();
					strVal = listPassedVals.get(r.nextInt(iListSize));
					iPassedValueLength = length - strVal.length();
					if (iPassedValueLength > TdgCentralConstant.UNIQUE_RANDOMN_LENGTH) {
						generateRandom.setiLength(TdgCentralConstant.UNIQUE_RANDOMN_LENGTH);
					} else {
						generateRandom.setiLength(iPassedValueLength);
					}
					lstResult.add(listPassedVals.get((int) ThreadLocalRandom.current().nextInt(
							iListSize))
							+ gen(generateRandom));
				} else {
					lstResult.add(listPassedVals.get((int) ThreadLocalRandom.current().nextInt(
							iListSize)));
				}
			}
		}
		return lstResult;
	}

	
	/**
	 * This method is used to fetch sequence values form passed dictionary or generated values
	 * @param listPassedVals
	 * @param length
	 * @param iRequiredCount
	 * @param isUniqueConstraint
	 * @return
	 */
	private List<String> getSequenceValueFromList(List<String> listPassedVals, int length,
			long iRequiredCount, boolean isUniqueConstraint){
		List<String> lstResult = new ArrayList<String>();
		if (listPassedVals != null && !listPassedVals.isEmpty()) {
			int iListSize = listPassedVals.size();
			GenerateRandom generateRandom = new GenerateRandom(TdgCentralConstant.ORACLE_NUMBER,
					length, 1);
			int iPassedValueLength = length;
			String strVal = "";
			List<Integer> lstSequeceVals = new ArrayList<Integer>(lstSequence);
			for (int i = 0; i < iRequiredCount; i++) {
				if (isUniqueConstraint) {
					Random r = new Random();
					strVal = listPassedVals.get(r.nextInt(iListSize));
					iPassedValueLength = length - strVal.length();
					if (iPassedValueLength > TdgCentralConstant.UNIQUE_RANDOMN_LENGTH) {
						generateRandom.setiLength(TdgCentralConstant.UNIQUE_RANDOMN_LENGTH);
					} else {
						generateRandom.setiLength(iPassedValueLength);
					}
					if (lstSequeceVals.isEmpty()) {
						int iSequence = ThreadLocalRandom.current().nextInt(iListSize);
						lstSequence.add(iSequence);
						lstResult.add(listPassedVals.get(iSequence) + gen(generateRandom));
					} else {
						lstResult.add(listPassedVals.get(lstSequence.get(i)) + gen(generateRandom));
					}
				} else {
					if (lstSequeceVals.isEmpty()) {
						int iSequence = ThreadLocalRandom.current().nextInt(iListSize);
						lstSequence.add(iSequence);
						lstResult.add(listPassedVals.get(iSequence));
					} else {
						lstResult.add(listPassedVals.get(lstSequence.get(i)));
					}
				}
			}
		}
		return lstResult;
	}
	/*private List<String> getSequenceValueFromList(List<String> listPassedVals, int length,
			long iRequiredCount, boolean isUniqueConstraint){
		List<String> lstResult = new ArrayList<String>();
		if (listPassedVals != null && !listPassedVals.isEmpty()) {
			int iListSize = listPassedVals.size();
			GenerateRandom generateRandom = new GenerateRandom(TdgCentralConstant.ORACLE_NUMBER,
					length, 1);
			int iPassedValueLength = length;
			String strVal = "";
			if(lstSequence.isEmpty())
			for (int i = 0; i < iRequiredCount; i++) {
				lstSequence.add(ThreadLocalRandom.current().nextInt(iListSize));
			}
			for(int j=0;j<lstSequence.size();j++){
				// strVal = listPassedVals.get(ThreadLocalRandom.current().nextInt(iListSize));
				// strVal = listPassedVals.get((int) (Math.random() * iListSize));
				if (isUniqueConstraint) {
					Random r = new Random();
					strVal = listPassedVals.get(r.nextInt(iListSize));
					iPassedValueLength = length - strVal.length();
					if (iPassedValueLength > TdgCentralConstant.UNIQUE_RANDOMN_LENGTH) {
						generateRandom.setiLength(TdgCentralConstant.UNIQUE_RANDOMN_LENGTH);
					} else {
						generateRandom.setiLength(iPassedValueLength);
					}
					lstResult.add(listPassedVals
							.get(lstSequence.get(j))
							+ gen(generateRandom));
				} else {
					lstResult.add(listPassedVals
							.get(lstSequence.get(j)));
				}
			}
		}
		return lstResult;
	}*/
	private String gen(GenerateRandom generateRandom){
		List<String> listGenValues = generateRandom.generateRandomString();
		return listGenValues != null && !listGenValues.isEmpty() ? listGenValues.get(0) : "";
	}
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Map<String,Map<String,List<Object[]>>> generateFlatTestData(TestDataGenerateDTO testDataGenenarteDTO,
			EntityManager managerentity){
		String strMethodName = " [ generateTestData() ] ";
		logger.info(strClassName + strMethodName + " inside generateTestData method");
		String strSuccess = TdgCentralConstant.FAILED_MESSAGE;
		Map<String,Map<String,List<Object[]>>> mapResponse = new HashMap<String, Map<String,List<Object[]>>>();
		strStatusDescription = new StringBuffer();
		lstGeneratedFiles = new ArrayList<String>();
		List<JdbcTemplate> listTemplates = new ArrayList<JdbcTemplate>();
		mapConditionValues = new HashMap<String, List<String>>();
		Map<String, List<Object[]>> mapResultOfFlatFile = new HashMap<String, List<Object[]>>();
		/**
		 * Support multiple database inject for depends
		 */
		List<String> listURLs = new ArrayList<String>();
		List<String> listUserNames = new ArrayList<String>();
		List<String> listPasswords = new ArrayList<String>();
		List<String> listDateFormates = new ArrayList<String>();
		if (testDataGenenarteDTO.getUrl() != null && testDataGenenarteDTO.getUrl().contains("#")) {
			listURLs.addAll(Arrays.asList(testDataGenenarteDTO.getUrl().split("#")));
		} else {
			listURLs.add(testDataGenenarteDTO.getUrl());
		}
		if (testDataGenenarteDTO.getUsername() != null
				&& testDataGenenarteDTO.getUsername().contains("#")) {
			listUserNames.addAll(Arrays.asList(testDataGenenarteDTO.getUsername().split("#")));
		} else {
			listUserNames.add(testDataGenenarteDTO.getUsername());
		}
		if (testDataGenenarteDTO.getPassword() != null
				&& testDataGenenarteDTO.getPassword().contains("#")) {
			listPasswords.addAll(Arrays.asList(testDataGenenarteDTO.getPassword().split("#")));
		} else {
			listPasswords.add(testDataGenenarteDTO.getPassword());
		}
		if (testDataGenenarteDTO.getDateFormate() != null
				&& testDataGenenarteDTO.getDateFormate().contains("#")) {
			listDateFormates
					.addAll(Arrays.asList(testDataGenenarteDTO.getDateFormate().split("#")));
		} else {
			listDateFormates.add(testDataGenenarteDTO.getDateFormate());
		}
		/**
		 * Map dependent values for reverse engineering to fetch values
		 */
		Map<String, String> mapReverseDependents = new HashMap<String, String>();
		if (testDataGenenarteDTO.getColumnsdepends() != null
				&& testDataGenenarteDTO.getColumnsdepends().contains(";")) {
			String strArrays[] = testDataGenenarteDTO.getColumnsdepends().split(";");
			for (int i = 0; i < strArrays.length; i++) {
				if (strArrays[i] != null && strArrays[i].contains("#")) {
					String strColsArrays[] = strArrays[i].split("#");
					mapReverseDependents.put(strColsArrays[1], strColsArrays[0]);
				}
			}
		}
		if (!listURLs.isEmpty() && !listUserNames.isEmpty() && !listPasswords.isEmpty()) {
			Map<String, Vector<String>> mapDependsColumns = new HashMap<String, Vector<String>>();
			for (int i = 0; i < listURLs.size(); i++) {
				String urlTemp = listURLs.get(i);
				String userNameTemp = listUserNames.get(i);
				String passTemp = listPasswords.get(i);
				JdbcTemplate jdbcTemplate = tdgTemplateDao.getTemplate(urlTemp, userNameTemp, passTemp);
				listTemplates.add(jdbcTemplate);
				testDataGenenarteDTO.setDateFormate(listDateFormates.get(i));
				Map<String, Vector<String>> tmpDependsColumns = generateTestForMutlipleDatabasesData(
						testDataGenenarteDTO, managerentity, jdbcTemplate, i, mapDependsColumns,
						mapReverseDependents);
				if (tmpDependsColumns.containsKey(TdgCentralConstant.FAILED_MESSAGE)) {
					strSuccess = TdgCentralConstant.FAILED_MESSAGE;
					break;
				} else {
					mapDependsColumns.putAll(tmpDependsColumns);
				}
				if (i == listURLs.size() - 1
						&& !tmpDependsColumns.containsKey(TdgCentralConstant.FAILED_MESSAGE)) {
					strSuccess = TdgCentralConstant.SUCCESS_MESSAGE;
				}
			}
			
			// final insert for trace purpose of the tdg history
			TdgRequestListDO tdgRequestListDO = new TdgRequestListDO();
			tdgRequestListDO.setReqschemaid(testDataGenenarteDTO.getSchemaId());
			tdgRequestListDO.setUserid(testDataGenenarteDTO.getUserId());
			tdgRequestListDO.setSchemaname(testDataGenenarteDTO.getSchemaname());
			tdgRequestListDO.setUserid(testDataGenenarteDTO.getUserId());
			

			if (testDataGenenarteDTO.isDataConditional()) {
				List<String> dataconditionVal = new ArrayList<String>(testDataGenenarteDTO.getDataConditionalTabNames());
				tdgOperationsDao.doDeleteDataConditionalValues(
						
						dataconditionVal,
						testDataGenenarteDTO.getGenerateRecordsCount(), managerentity);
				Map<String, List<String>> mapInput = testDataGenenarteDTO
						.getMapDictionaryVals();
				if (mapInput != null && !mapInput.isEmpty()) {
					if (mapConditionValues.isEmpty()) {
						mapConditionValues = new HashMap<String, List<String>>();
					}
					for (Map.Entry<String, List<String>> mapEntry : mapInput.entrySet()) {
						if (!mapConditionValues.containsKey(mapEntry.getKey())) {
							List<String> listParams = new ArrayList<String>();
							for (int i = 0; i < testDataGenenarteDTO.getGenerateRecordsCount(); i++) {
								listParams.add(mapEntry.getValue().get(i));
							}
							mapConditionValues.put(mapEntry.getKey(), listParams);
						}
					}
				}
			}
			StringBuffer strBuffer = new StringBuffer();
			if (!mapConditionValues.isEmpty()) {
				int iCount = 1;
				for (Map.Entry<String, List<String>> mapentry : mapConditionValues.entrySet()) {
					strBuffer.append(mapentry.getKey());
					if (iCount != mapConditionValues.size()) {
						strBuffer.append('#');
					}
					iCount++;
				}
			}			
			
			tdgRequestListDO.setConditions(strBuffer.toString());
			
			if (TdgCentralConstant.SUCCESS_MESSAGE.equals(strSuccess)) {				
			    tdgRequestListDO.setRequestCount(testDataGenenarteDTO.getGenerateRecordsCount());
			    if(lstGeneratedFiles.isEmpty()){
			    @SuppressWarnings("rawtypes")
				Vector vct = new Vector();
			    vct.add(testDataGenenarteDTO.getGenerateRecordsCount());
			    vct.addElement(strStatusDescription);
			    
			    tdgRequestListDO.setStatusdescription(TdgCentralConstant.getReplacedValue(TdgCentralConstant.TDG_GENERATE_SUCCESS_MESSAGE, vct));
			    }else{
			    	tdgRequestListDO.setStatusdescription(strStatusDescription.toString());
			    }
			    tdgRequestListDO.setStatus(TdgCentralConstant.TDG_GENERATE_SUCCESS);
			    
			    //for success message of flat files
			    mapResponse.put(TdgCentralConstant.TDG_GENERATE_SUCCESS, mapResultOfFlatFile);
			}else{
				tdgRequestListDO.setRequestCount(0);
				tdgRequestListDO.setStatusdescription(strStatusDescription.toString());
				tdgRequestListDO.setStatus(TdgCentralConstant.TDG_GENERATE_FAILED);
				//for failed message of flat files
			    mapResponse.put(TdgCentralConstant.TDG_GENERATE_FAILED, mapResultOfFlatFile);
				try{
					for(String strFileNames : lstGeneratedFiles){
						File file = new File(strFileNames);
						if(file.exists() && file.delete())
							logger.info(strClassName + strMethodName
									+ " generated file is deleted ");
					}
				}catch(Exception e){
					logger.error(strClassName + strMethodName
							+ " error occured while delete the files ::  ", e);
				}
				
			}
			//strSuccess = TdgCentralConstant.FAILED_MESSAGE;setStatus<th align="center"  bgcolor="#E3EFFB" scope="col" class="whitefont">Status</th><th align="center"  bgcolor="#E3EFFB" scope="col" class="whitefont">Status</th><td align="left">${tdgRequestListDTOs.status}</td><td align="left">${tdgRequestListDTOs.status}</td>
			tdgOperationsDao.saveDashBoardDetails(tdgRequestListDO,
					mapConditionValues, managerentity);
			try {
				for (JdbcTemplate template : listTemplates) {
					if (!TdgCentralConstant.SUCCESS_MESSAGE.equals(strSuccess)) {
						template.getDataSource().getConnection().rollback();
					} else {
						template.getDataSource().getConnection().commit();
					}
					tdgTemplateDao.cleanupDataSource(template);
				}
			} catch (SQLException e1) {
				logger.error(strClassName + strMethodName
						+ " error occured while rollback the transaction ::  ", e1);
			}
		}
		return mapResponse;
	}
}
