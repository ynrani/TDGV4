/*
 * Object Name : TdgTemplateDaoImpl.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		9:42:26 AM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Component;

import com.tesda.dao.TdgTemplateDao;
import com.tesda.util.TdgCentralConstant;
import com.tesda.util.TdgCoreConverter;

/**
 * @author vkrish14
 *
 */
@Component("tdgTemplateDao")
public class TdgTemplateDaoImpl implements TdgTemplateDao{
	private static Logger logger = Logger.getLogger(TdgTemplateDaoImpl.class);
	private static String strClassName = " [ TdgTemplateDaoImpl ] ";
	private String dbType = "";
	private String schemaName = "";
	SingleConnectionDataSource dataSource = null;
	private Set<Object> setDuplicateCycleCheck = null;
	
	/**
	 * This method is used to fetch tables in sequence order to dump the data into respective tables
	 */
	@Override
	public List<Object> generateSequenceOfTables(JdbcTemplate jdbcTemplate, String tableName,boolean isDuplicateCall){
		List<Object> listObject = new ArrayList<Object>();
		if(!isDuplicateCall){
			setDuplicateCycleCheck = new HashSet<Object>();
		}
		if(!setDuplicateCycleCheck.contains(tableName.replaceAll("#","").trim())){
		
		tableName += "#";
		listObject.add(tableName.toUpperCase());
		String temptableName = tableName.substring(0, tableName.indexOf("#"));
		Vector<String> vct = new Vector<String>(getVectorVals());
		
		/*to remove hard-coded dbo and adding code for sqlserver bcoz schema.table name*/
		if(temptableName.contains(".")){
		String[] tabsWithSchema = temptableName.split("[.]");
		
		StringBuffer comma1 = new StringBuffer();
		comma1.append('\'').append(tabsWithSchema[0]).append('\'');
		StringBuffer comma2 = new StringBuffer();
		comma2.append('\'').append(tabsWithSchema[1]).append('\'');
			vct.add(comma1.toString());
			vct.add(comma2.toString());
			/*till here*/
			
		}
		else{
		
		vct.add(temptableName);
		}
		logger.info(" Going for table " + temptableName);
		List<Map<String, Object>> listValues = jdbcTemplate.queryForList(TdgCentralConstant
				.getSpecificDBQuery(getDbType(), TdgCentralConstant.GET_SEQUENCE_TABLES, vct));
		for (Map<String, Object> mapValues : listValues) {
			setDuplicateCycleCheck.add(temptableName);
			listObject
					.add(generateSequenceOfTables(jdbcTemplate, ((String) mapValues.entrySet()
							.iterator().next().getValue())
							+ tableName.substring(tableName.indexOf("#"), tableName.length())
									.toUpperCase(),true));
		}
		setDuplicateCycleCheck.add(temptableName);
		}
		return listObject;
	}

	/**
	 * This method is used to fetch all the tables which are exist in respective schema of oracle
	 */
	@Override
	public List<String> getAllTables(String url, String username, String password){
		String strMethodName = " [ getAllTables() ] ";
		logger.info(strClassName + strMethodName + " inside getAllTables method");
		
		List<String> listTableNames = new ArrayList<String>();
		List<String> listURLs = new ArrayList<String>();
		List<String> listUserNames = new ArrayList<String>();
		List<String> listPasswords = new ArrayList<String>();
		//try {
			if(url != null && url.contains("#")){
			if (url != null && url.contains("#")) {
				listURLs.addAll(Arrays.asList(url.split("#")));
			} else {
				listURLs.add(url);
			}
			if (username != null && username.contains("#")) {
				listUserNames.addAll(Arrays.asList(username.split("#")));
			} else {
				listUserNames.add(username);
			}
			if (password != null && password.contains("#")) {
				listPasswords.addAll(Arrays.asList(password.split("#")));
			} else {
				listPasswords.add(password);
			}
			if (!listURLs.isEmpty() && !listUserNames.isEmpty() && !listPasswords.isEmpty()) {
				for (int i = 0; i < listURLs.size(); i++) {
					String urlTemp = listURLs.get(i);
					String userNameTemp = listUserNames.get(i);
					String passTemp = listPasswords.get(i);
					JdbcTemplate template = getTemplate(urlTemp, userNameTemp, passTemp);
					try{
					//JdbcTemplate template = getTemplate(urlTemp, userNameTemp, passTemp);
					List<Map<String, Object>> listString = template.queryForList(TdgCentralConstant
							.getSpecificDBQuery(getDbType(), TdgCentralConstant.GET_ALL_TABLES,
									getVectorVals()));
					for (Map<String, Object> mapValues : listString) {
						listTableNames.add(mapValues.get("TABLE_NAME").toString().toUpperCase());
					}
					}finally{
						cleanupDataSource(template);
					}
				}
			}
			}else{
				JdbcTemplate template = getTemplate(url, username, password);
				try{
				//JdbcTemplate template = getTemplate(urlTemp, userNameTemp, passTemp);
				List<Map<String, Object>> listString = template.queryForList(TdgCentralConstant
						.getSpecificDBQuery(getDbType(), TdgCentralConstant.GET_ALL_TABLES,
								getVectorVals()));
				for (Map<String, Object> mapValues : listString) {
					listTableNames.add(mapValues.get("TABLE_NAME").toString().toUpperCase());
				}
				}finally{
					cleanupDataSource(template);
				}
			}
			
			logger.info(strClassName + strMethodName + " got all tables list from passed schema");
			logger.info(strClassName + strMethodName + " return getAllTables method");
		//} /*finally {
			//cleanupDataSource(template);
		//}*/
		return listTableNames;
	}

	/**
	 * This method is used to fetch all the columns which are exist in respective schema of oracle
	 */
	@Override
	public List<String> getAllCols(String url, String username, String password){
		String strMethodName = " [ getAllCols() ] ";
		logger.info(strClassName + strMethodName + " inside getAllCols method");
		List<String> listResult = getColsByTabs(url, username, password, null);
		return listResult;
	}

	@Override
	public List<String> getColsByTabs(String url, String username, String password,
			List<String> lstTabNames){
		List<String> listResult = new ArrayList<String>();
		List<String> listURLs = new ArrayList<String>();
		List<String> listUserNames = new ArrayList<String>();
		List<String> listPasswords = new ArrayList<String>();
		int iCount = 1;
		StringBuffer strBuffer = new StringBuffer();
		//String string1="";
		boolean bTabsPassed = false;
		if (lstTabNames != null && !lstTabNames.isEmpty()) {
			bTabsPassed = true;
			for (String string : lstTabNames) {		
				//string1 = string;
				strBuffer.append(string);
				if (iCount != lstTabNames.size()) {
					strBuffer.append(',');
				}
				iCount++;
				
			}
		}
		if (url != null && url.contains("#")) {
			listURLs.addAll(Arrays.asList(url.split("#")));
		} else {
			listURLs.add(url);
		}
		if (username != null && username.contains("#")) {
			listUserNames.addAll(Arrays.asList(username.split("#")));
		} else {
			listUserNames.add(username);
		}
		if (password != null && password.contains("#")) {
			listPasswords.addAll(Arrays.asList(password.split("#")));
		} else {
			listPasswords.add(password);
		}
		if (!listURLs.isEmpty() && !listUserNames.isEmpty() && !listPasswords.isEmpty()) {
			for (int i = 0; i < listURLs.size(); i++) {
				String urlTemp = listURLs.get(i);
				String userNameTemp = listUserNames.get(i);
				String passTemp = listPasswords.get(i);
				if (bTabsPassed) {
					listResult.addAll(getSpecificAllColsByTabs(urlTemp, userNameTemp, passTemp,
							strBuffer.toString()));
				} else {
					listResult.addAll(getSpecificAllCols(urlTemp, userNameTemp, passTemp));
				}
			}
		}
		return listResult;
	}
	
	private Set<String> getSpecificAllCols(String url, String username, String password){
		String strMethodName = " [ getSpecificAllCols() ] ";
		JdbcTemplate template = getTemplate(url, username, password);
		Set<String> listResult = new HashSet<String>();
		try {
			List<Map<String, Object>> listString = template.queryForList(TdgCentralConstant
					.getSpecificDBQuery(getDbType(), TdgCentralConstant.GET_ALL_COLUMNS,
							getVectorVals()));
			for (Map<String, Object> map : listString) {
				//listResult.add(String.valueOf(map.get("COLUMN_NAME")).toUpperCase());
				//listResult.add(String.valueOf(map.get("TABLE_NAME")).toUpperCase()+"."+String.valueOf(map.get("COLUMN_NAME")).toUpperCase());
				listResult.add(String.valueOf(map.get("COLUMN_NAME")).toUpperCase());
			}
			logger.info(strClassName + strMethodName + " got all columns list from passed schema");
			logger.info(strClassName + strMethodName + " return getAllCols method");
		} finally {
			cleanupDataSource(template);
		}
		return listResult;
	}

	private Set<String> getSpecificAllColsByTabs(String url, String username, String password,
			String tabs){
		String strMethodName = " [ getSpecificAllCols() ] ";
		JdbcTemplate template = getTemplate(url, username, password);
		Set<String> listResult = new HashSet<String>();
		try {
			Vector<String> vct = new Vector<String>();
			
			if(getDbType().equals(TdgCentralConstant.DB_TYPE_POSTGRES)){
				for(String str: getVectorVals()){
					vct.add(str.toLowerCase());
				}
				StringBuffer tabNameWithInvertedComma = new StringBuffer();
				tabNameWithInvertedComma.append('\'').append(tabs).append('\'');
				vct.add(tabNameWithInvertedComma.toString().toLowerCase());
			}else{
				vct.addAll(getVectorVals());
				if(getDbType().equals(TdgCentralConstant.DB_TYPE_SQLSERVER)){
					
				if(tabs.contains(".")){
					
					String[] tabsWithSchema = tabs.split("[.]");
					StringBuffer comma1 = new StringBuffer();
					comma1.append('\'').append(tabsWithSchema[0]).append('\'');
						vct.add(comma1.toString());
				StringBuffer comma = new StringBuffer();
				comma.append('\'').append(tabsWithSchema[1]).append('\'');
						vct.add(comma.toString());

				//vct.add(tabsWithSchema[0].replaceAll("\'",""));
				//vct.add(tabsWithSchema[0].replaceAll("\'",""));
				//vct.add(tabsWithSchema[0]);
				//comma.append('\'').append(tabsWithSchema[1]);
				//vct.add(tabsWithSchema[1]);
				//vct.add(tabsWithSchema[1].replaceAll("\'",""));
				}
				}
				else{
					StringBuffer comma3 = new StringBuffer();
					comma3.append('\'').append(tabs).append('\'');
					vct.addAll(getVectorVals());
					vct.add(comma3.toString());
				}
			}
			List<Map<String, Object>> listString = template
					.queryForList(TdgCentralConstant.getSpecificDBQuery(getDbType(),
							TdgCentralConstant.GET_ALL_COLUMNS_BY_TAB, vct));
			for (Map<String, Object> map : listString) {
				listResult.add(String.valueOf(map.get("COLUMN_NAME")).toUpperCase());
				//listResult.add(String.valueOf(map.get("TABLE_NAME")).toUpperCase()+"."+String.valueOf(map.get("COLUMN_NAME")).toUpperCase());
			}
			logger.info(strClassName + strMethodName + " got all columns list from passed schema");
			logger.info(strClassName + strMethodName + " return getAllCols method");
		} finally {
			cleanupDataSource(template);
		}
		return listResult;
	}
	
	
	/**
	 * This method is used to fetch all the constraints columns which refers to Foreign key and
	 * unique constraints in oracle
	 */
	@Override
	public List<Map<String, Object>> getAllConstraintCols(String url, String username,
			String password){
		String strMethodName = " [ getAllConstraintCols() ] ";
		logger.info(strClassName + strMethodName + " inside getAllConstraintCols method");
		JdbcTemplate template = getTemplate(url, username, password);
		List<Map<String, Object>> listString = null;
		try {
			listString = TdgCoreConverter.getConvertedResult(template, TdgCentralConstant
					.getSpecificDBQuery(getDbType(),
							TdgCentralConstant.GET_ALL_CONSTRAINTS_COLUMNS, getVectorVals()),
					getDbType());
			logger.info(strClassName + strMethodName + " return getAllConstraintCols method");
		} finally {
			cleanupDataSource(template);
		}
		return listString;
	}

	
	/**
	 * This method is used to fetch all the tables are in sequence which are exist in respective
	 * schema of oracle
	 */
	@Override
	public List<Object> getTables(JdbcTemplate jdbcTemplate, String tableName){
		String strMethodName = " [ getTables() ] ";
		logger.info(strClassName + strMethodName + " inside getTables method");
		List<Object> listObject = new ArrayList<Object>();
		Vector<String> vct = new Vector<String>(getVectorVals());
		vct.add(tableName);
		List<Map<String, Object>> listValues = jdbcTemplate.queryForList(TdgCentralConstant
				.getSpecificDBQuery(getDbType(), TdgCentralConstant.GET_TABLES, vct));// generic
																						// solution
		logger.info(strClassName + strMethodName + " got foriegn key constraints of the table : "
				+ tableName);
		for (Map<String, Object> mapValues : listValues) {
			listObject.add(generateSequenceOfTables(jdbcTemplate, ((String) mapValues.entrySet()
					.iterator().next().getValue())
					+ tableName.substring(tableName.indexOf("#"), tableName.length()),false));
		}
		logger.info(strClassName + strMethodName + " return from getTables method");
		return listObject;
	}

	
	
	@Override
	public List<Map<String, Object>> getTableNamesByColsName(JdbcTemplate jdbcTemplate,
			List<String> listcolumnsName){
		String strMethodName = " [ getTableNamesByColsName() ] ";
		logger.info(strClassName + strMethodName + " inside getTableNamesByColsName method");
		List<Map<String, Object>> listString = null;
		boolean bPostgresCheck = false;
		if(TdgCentralConstant.DB_TYPE_POSTGRES.equals(getDbType())){
			bPostgresCheck = true;
		}
		StringBuffer strBuffer = new StringBuffer(TdgCentralConstant.getSpecificDBQuery(
				getDbType(), TdgCentralConstant.GET_TABLES_BY_COLUMNS, getVectorVals()))
				.append("( ");
		if (listcolumnsName != null && !listcolumnsName.isEmpty()) {
			int iCount = 0;
			for (String strColsName : listcolumnsName) {
				iCount++;
				/**
				 * checking for multiple column names check
				 */
				if (strColsName.contains("#")) {
					String[] strArray = strColsName.split("#");
					for (int i = 0; i < strArray.length; i++) {
						if (!bPostgresCheck) {
							strBuffer.append(" '").append(strArray[i].toUpperCase()).append('\'');
						} else {
							strBuffer.append(" '").append(strArray[i].toLowerCase()).append('\'');
						}
						if (i != strArray.length - 1) {
							strBuffer.append(',');
						}
					}
					if (strArray != null && strArray.length > 0) {
						strBuffer.append(',');
					}
				} else {
					if (!bPostgresCheck) {
						strBuffer.append(" '").append(strColsName.toUpperCase()).append('\'');
					} else {
						strBuffer.append(" '").append(strColsName.toLowerCase()).append('\'');
					}
					if (iCount != listcolumnsName.size()) {
						strBuffer.append(',');
					}
				}
				if (iCount == listcolumnsName.size()) {
					strBuffer.append(')');
				}
			}
			listString = TdgCoreConverter.getConvertedResult(jdbcTemplate, strBuffer.toString(),
					getDbType());
		}
		logger.info(strClassName + strMethodName + " return from getTableNamesByColsName method");
		return listString;
	}


	@Override
	public JdbcTemplate getTemplate(String strUrl, String strUsername, String strPassword){
		String strMethodName = " [ getTemplate() ] ";
		logger.info(strClassName + strMethodName + " inside getTemplate method");
		if(strUsername.equalsIgnoreCase("wa"))
		{
			dataSource = new SingleConnectionDataSource(strUrl, true);
		}
		else
		dataSource = new SingleConnectionDataSource(strUrl, strUsername, strPassword, true);
		dataSource.setAutoCommit(false);
		DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(
				dataSource);
		// dataSourceTransactionManager.commit(false);
		dataSourceTransactionManager.setRollbackOnCommitFailure(true);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceTransactionManager.getDataSource());
		if (strUrl != null) {
			if (strUrl.toLowerCase().contains("oracle")) {
				setDbType(TdgCentralConstant.DB_TYPE_ORACLE);
				setSchemaName("");
			} else if (strUrl.toLowerCase().contains("mysql")) {
				setDbType(TdgCentralConstant.DB_TYPE_MYSQL);
				String strArrays[] = strUrl.split("\\/");
				if (strArrays != null && strArrays.length > 0) {
					setSchemaName(strArrays[strArrays.length - 1].toUpperCase());
				} else {
					setSchemaName("TEST");// set for default now
				}
			} else if (strUrl.toLowerCase().contains("db2")) {
				setDbType(TdgCentralConstant.DB_TYPE_DB2);
				if(strUrl.contains("currentSchema=")){
					String[] strVals = strUrl.split("currentSchema=");
					setSchemaName(strVals[1].substring(0,strVals[1].indexOf(";")).toUpperCase());
				}else{
				setSchemaName("");
				}
			}
			else if (strUrl.toLowerCase().contains("sqlserver")) {
				setDbType(TdgCentralConstant.DB_TYPE_SQLSERVER);
			//	System.out.println(getSchemaName());
				setSchemaName("");
			}else if (strUrl.toLowerCase().contains("postgres")) {
				setDbType(TdgCentralConstant.DB_TYPE_POSTGRES);
				setSchemaName("");
			}
		}
		logger.info(strClassName + strMethodName + " return from getTemplate method");
		return jdbcTemplate;
	}

	@Override
	public String getDbType(){
		return dbType;
	}

	public void setDbType(String dbType){
		this.dbType = dbType;
	}
	
	private Vector<String> getVectorVals(){
		Vector<String> vct = new Vector<String>();
		if (getSchemaName() != null && !"".equals(getSchemaName())) {
			vct.add(getSchemaName());
		}
		return vct;
	}

	public String getSchemaName(){
		return schemaName;
	}

	public void setSchemaName(String schemaName){
		this.schemaName = schemaName;
	}
	
	@Override
	public void cleanupDataSource(JdbcTemplate template){
		try {
			if (template != null && !template.getDataSource().getConnection().isClosed()) {
				// dataSource.destroy();
				template.getDataSource().getConnection().close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Set<String> getTableNamesFromCols(JdbcTemplate jdbcTemplate, Map<String, Object> mapCols){
		String strMethodName = " [ getTableNamesFromCols() ] ";
		logger.info(strClassName + strMethodName + " inside getTableNamesFromCols method");
		Set<String> stTabs = new HashSet<String>();
		if (mapCols != null && !mapCols.isEmpty()) {
			List<String> listCols = new ArrayList<String>();
			for (String strKey : mapCols.keySet()) {
				listCols.add(strKey);
			}
			List<Map<String, Object>> lstResult = getTableNamesByColsName(jdbcTemplate, listCols);
			for (Map<String, Object> map : lstResult) {
				stTabs.add(String.valueOf(map.get("TABLE_NAME")).toUpperCase());
			}
		}
		logger.info(strClassName + strMethodName + " return getTableNamesFromCols method");
		return stTabs;
	}

	@Override
	public Set<String> getTableNamesFromCols(JdbcTemplate jdbcTemplate, List<String> listCols){
		String strMethodName = " [ getTableNamesFromCols() ] ";
		logger.info(strClassName + strMethodName + " inside getTableNamesFromCols method");
		Set<String> stTabs = new HashSet<String>();
		if (listCols != null && !listCols.isEmpty()) {
			List<Map<String, Object>> lstResult = getTableNamesByColsName(jdbcTemplate, listCols);
			for (Map<String, Object> map : lstResult) {
				stTabs.add(String.valueOf(map.get("TABLE_NAME")).toUpperCase());
			}
		}
		logger.info(strClassName + strMethodName + " return getTableNamesFromCols method");
		return stTabs;
	}
	
	
	@SuppressWarnings({ "rawtypes" })
	@Override
	public Vector generatePkValues(JdbcTemplate template, String query,
			Map<String, Map<String, String>> mapSequences, long iRecords, String strTableName){
		Vector<Object> vct = new Vector<Object>();
		boolean isNumber = true;
		if (mapSequences == null || !mapSequences.containsKey(strTableName)) {
			List<Map<String, Object>> listPkVal = null;
			String strPrefix = "";
			long lPkValue = 0L;
			long lLength = 1L;
			try {
				/*listPkVal = getPrimaryKeyValue(template, query);
				lPkValue = Long.parseLong(listPkVal.get(0).get("IDGEN") + "");*/
				//one going fire with all prefixes set 
				listPkVal = getPrimaryKeyValue(template, query.replaceAll("#", ""));
				lPkValue = Long.parseLong(listPkVal.get(0).get("IDGEN") + "");
			} catch (Exception se) {
				logger.error(se.getMessage());
				if (listPkVal != null && !listPkVal.isEmpty()) {
					String strPk = listPkVal.get(0).get("IDGEN") + "";
					Pattern pt = Pattern.compile("[A-Za-z]");
					if (pt.matcher(strPk).find()) {
						String str = strPk.replaceAll("[A-Za-z]", "#");
						strPrefix = strPk.substring(0, str.lastIndexOf("#") + 1);
						lLength = strPk.substring(str.lastIndexOf("#") + 1, str.length()).length();
						if (str.contains("#")) {
							listPkVal = getPrimaryKeyValue(template, query.replaceAll("#", "strPrefix"));
							lPkValue = Long.parseLong(listPkVal.get(0).get("IDGEN") + "");
							str = strPk.replaceAll("[A-Za-z]", "#");
							strPrefix = strPk.substring(0, str.lastIndexOf("#") + 1);
							lLength = strPk.substring(str.lastIndexOf("#") + 1, str.length()).length();
							lPkValue = Long.parseLong("".equalsIgnoreCase(strPk.substring(
									str.lastIndexOf("#") + 1, str.length())) ? "0" : strPk
									.substring(str.lastIndexOf("#") + 1, str.length())) + 1;
						} else {
							lPkValue = Long.parseLong(strPk) + 1;
						}
					} else {
					}
				}
			}
			//need to check for replace varchar or char types in db			
			if (lLength == 1) {
				lPkValue++;
			}
			String strGeneratedValue = strPrefix + String.format("%0" + lLength + "d", lPkValue);
			Pattern pt = Pattern.compile("[A-Za-z]");
			if (pt.matcher(strGeneratedValue).find() && !strGeneratedValue.startsWith("0"))
				isNumber = false;
			if(isNumber){
			vct.add(Integer.parseInt(strGeneratedValue));
			}else{
				vct.add(strGeneratedValue);
			}
			for (int i = 1; i < iRecords; i++) {
				if (isNumber) {
					vct.add(Integer.parseInt(strPrefix
							+ (String.format("%0" + lLength + "d", ++lPkValue))));
				} else {
					vct.add(strPrefix + (String.format("%0" + lLength + "d", ++lPkValue)));
				}
			}
		} else {
			// need to implement for sequence
			for (Map.Entry<String, Map<String, String>> mapEntry : mapSequences.entrySet()) {
				if (strTableName.toUpperCase().equals(mapEntry.getKey().toUpperCase())) {
					Vector<String> vctTemp = new Vector<String>(getVectorVals());
					vctTemp.add(mapEntry.getValue().entrySet().iterator().next().getValue());
					String strPrefix = mapEntry.getValue().entrySet().iterator().next().getKey();
					for (int i = 1; i <= iRecords; i++) {
						vct.add(strPrefix
								+ template.queryForLong(TdgCentralConstant.getSpecificDBQuery(
										getDbType(), TdgCentralConstant.SEQUENCE_VALUE, vctTemp)));
					}
					break;
				}
			}
		}
		return vct;
	}
	
	private List<Map<String, Object>> getPrimaryKeyValue(JdbcTemplate jdbcTemplate, String strquery){
		List<Map<String, Object>> listString = jdbcTemplate.queryForList(strquery);
		return listString;
	}
	
	@Override
	public List<Map<String, Object>> getSequenceColsByTableName(JdbcTemplate jdbcTemplate,
			String strTBName, String strPKColumnName){
		List<Map<String, Object>> listString = null;
		Vector<String> vct = new Vector<String>(getVectorVals());
		/*to remove hard-coded dbo and adding code for sqlserver bcoz schema.table name*/
		if(strTBName.contains(".")){
		String[] tabsWithSchema = strTBName.split("[.]");
		
		StringBuffer comma1 = new StringBuffer();
		comma1.append('\'').append(tabsWithSchema[0]).append('\'');
		StringBuffer comma2 = new StringBuffer();
		comma2.append('\'').append(tabsWithSchema[1]).append('\'');
			vct.add(comma1.toString());
			vct.add(comma2.toString());
			/*till here*/
			
		}
		else{
		vct.add(strTBName);
		}
		if (strPKColumnName != null) {
			
			vct.add(strPKColumnName);
			listString = TdgCoreConverter.getConvertedResult(jdbcTemplate, TdgCentralConstant
					.getSpecificDBQuery(getDbType(), TdgCentralConstant.GET_SEQUENCE_OF_COLUMNS2,
							vct), getDbType());
		} else {
			listString = TdgCoreConverter.getConvertedResult(jdbcTemplate, TdgCentralConstant
					.getSpecificDBQuery(getDbType(), TdgCentralConstant.GET_SEQUENCE_OF_COLUMNS1,
							vct), getDbType());
		}
		return listString;
	}
	

	@Override
	public Map<String, String> getTableNameByFkName(JdbcTemplate jdbcTemplate, String strTBName,
			Map<String, String> mapFkColumns){
		Map<String, String> mapValues = new HashMap<String, String>();
		StringBuffer strQuery = null;
		Vector<String> vct = null;
		for (Map.Entry<String, String> val : mapFkColumns.entrySet()) {
			vct = new Vector<String>(getVectorVals());
			/*to remove hard-coded dbo and adding code for sqlserver bcoz schema.table name*/
			if(strTBName.contains(".")){
			String[] tabsWithSchema = strTBName.split("[.]");
			
			StringBuffer comma1 = new StringBuffer();
			comma1.append('\'').append(tabsWithSchema[0]).append('\'');
			StringBuffer comma2 = new StringBuffer();
			comma2.append('\'').append(tabsWithSchema[1]).append('\'');
				vct.add(comma1.toString());
				vct.add(comma2.toString());
				/*till here*/
				
			}
			else{
				vct.add(strTBName);
			}
			
			
			vct.add(val.getKey());
			strQuery = new StringBuffer(TdgCentralConstant.getSpecificDBQuery(getDbType(),
					TdgCentralConstant.GET_TABLE_NAME_BY_FK, vct));
			List<Map<String, Object>> listString = jdbcTemplate.queryForList(strQuery.toString());
			mapValues.put(val.getKey(), String.valueOf(listString.get(0).get("TABLE_NAME"))
					.toUpperCase());
		}
		return mapValues;
	}

	
	@Override
	public List<Map<String, Object>> getConstraintsOfTable(JdbcTemplate jdbcTemplate,
			String tableName){
		Vector<String> vct = new Vector<String>(getVectorVals());
		/*to remove hard-coded dbo and adding code for sqlserver bcoz schema.table name*/
		if(tableName.contains(".")){
		String[] tabsWithSchema = tableName.split("[.]");
		
		StringBuffer comma1 = new StringBuffer();
		comma1.append('\'').append(tabsWithSchema[0]).append('\'');
		StringBuffer comma2 = new StringBuffer();
		comma2.append('\'').append(tabsWithSchema[1]).append('\'');
			vct.add(comma1.toString());
			vct.add(comma2.toString());
			/*till here*/
			
		}
		else{
		vct.add(tableName);
		}
		List<Map<String, Object>> listValues = TdgCoreConverter.getConvertedResult(jdbcTemplate,
				TdgCentralConstant.getSpecificDBQuery(getDbType(),
						TdgCentralConstant.GET_CONSTRAINTS_OF_TABLES, vct), getDbType());
		return listValues;
	}

	@Override
	public List<Map<String, Object>> getNotNullConstraintsOfTable(JdbcTemplate jdbcTemplate,
			String tableName){
		Vector<String> vct = new Vector<String>(getVectorVals());
		
		
		/*to remove hard-coded dbo and adding code for sqlserver bcoz schema.table name*/
		if(tableName.contains(".")){
		String[] tabsWithSchema = tableName.split("[.]");
		
		StringBuffer comma1 = new StringBuffer();
		comma1.append('\'').append(tabsWithSchema[0]).append('\'');
		StringBuffer comma2 = new StringBuffer();
		comma2.append('\'').append(tabsWithSchema[1]).append('\'');
			vct.add(comma1.toString());
			vct.add(comma2.toString());
			/*till here*/
			
		}

		else{
		vct.add(tableName);
		}
		String strQuery = TdgCentralConstant.getSpecificDBQuery(getDbType(),
				TdgCentralConstant.GET_NOT_NULL_CONSTRAINTS_OF_TABLES, vct);
		List<Map<String, Object>> listValues = new ArrayList<Map<String, Object>>();
		if (strQuery != null && !"".equals(strQuery)) {
			listValues.addAll(TdgCoreConverter.getConvertedResult(jdbcTemplate, strQuery,
					getDbType()));
		}
		return listValues;
	}

	@Override
	public List<Map<String, Object>> getPkColumnType(JdbcTemplate jdbcTemplate,
			String constraint_name, String tableName){
		Vector<String> vct = new Vector<String>(getVectorVals());
		vct.add(constraint_name);
		
		
	
		/*to remove hard-coded dbo and adding code for sqlserver bcoz schema.table name*/
		if(tableName.contains(".")){
		String[] tabsWithSchema = tableName.split("[.]");
		
		StringBuffer comma1 = new StringBuffer();
		comma1.append('\'').append(tabsWithSchema[0]).append('\'');
		StringBuffer comma2 = new StringBuffer();
		comma2.append('\'').append(tabsWithSchema[1]).append('\'');
			vct.add(comma1.toString());
			vct.add(comma2.toString());
			/*till here*/
			
		}
		else{
			vct.add(tableName);
		}
		List<Map<String, Object>> listValues = TdgCoreConverter.getConvertedResult(jdbcTemplate,
				TdgCentralConstant.getSpecificDBQuery(getDbType(),
						TdgCentralConstant.GET_PK_COLUMN_TYPE, vct), getDbType());
		return listValues;
	}

	@Override
	public List<Map<String, Object>> getOntToOneTableInformation(JdbcTemplate jdbcTemplate,
			String tableName){
		Vector<String> vct = new Vector<String>(getVectorVals());
		vct.add(tableName);
		List<Map<String, Object>> listValues = jdbcTemplate.queryForList(TdgCentralConstant
				.getSpecificDBQuery(getDbType(),
						TdgCentralConstant.ONE_TO_ONE_RELATIONS_FIND_TABLES, vct));
		return listValues;
	}

	@Override
	public List<Map<String, Object>> getConstraintRelationTable(JdbcTemplate jdbcTemplate,
			String strConstraintName, String strTableName){
		Vector<String> vct = new Vector<String>(getVectorVals());
		vct.add(strConstraintName);
		vct.add(strTableName);
		List<Map<String, Object>> listValues = jdbcTemplate.queryForList(TdgCentralConstant
				.getSpecificDBQuery(getDbType(), TdgCentralConstant.CONSTRAINTS_RELATIONS_TABLES,
						vct));
		return listValues;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> toCheckrelations(List<Object> listObject, List<Object> listResult){
		if (listResult == null) {
			listResult = new ArrayList<Object>();
		}
		for (Object obj : listObject) {
			if (obj instanceof List) {
				toCheckrelations((List<Object>) obj, listResult);
			} else {
				listResult.add(obj);
			}
		}
		return listResult;
	}

	@Override
	public List<List<Object>> toCheckrelations(List<Object> listObject){
		List<List<Object>> listObjectFinal = new ArrayList<List<Object>>();
		List<Object> listObj = new ArrayList<Object>();
		for (Object obj : listObject) {
			List<Object> listTemp = new ArrayList<Object>(listObj);
			List<Object> duplicateCheck = containsCheck(listObj, String.valueOf(obj));
			if (duplicateCheck != null) {
				if (!containsAllCheck(listObjectFinal, listTemp)) {
					listObjectFinal.add(listTemp);
					listObjectFinal = chechSublist(listObjectFinal, listTemp);
				}
				listObj = duplicateCheck;
				List<Object> listTmp = new ArrayList<Object>(listObj);
				listTmp.add(obj);
				listObj.add(obj);
				if (!containsAllCheck(listObjectFinal, listTmp)) {
					listObjectFinal.add(listTmp);
					listObjectFinal = chechSublist(listObjectFinal, listTemp);
				}
			} else {
				listObj.add(obj);
			}
		}
		// if()
		listObjectFinal = chechSublist(listObjectFinal, listObj);
		return listObjectFinal;
	}

	@Override
	public List<List<Object>> chechSublist(List<List<Object>> listObjectFinal,
			List<Object> listTemp){
		List<List<Object>> listResult = new ArrayList<List<Object>>();
		if (listObjectFinal != null) {
			listResult.addAll(listObjectFinal);
			for (List<Object> listObj : listObjectFinal) {
				if (listTemp.containsAll(listObj) && listTemp.size() != listObj.size()) {
					listResult.remove(listObj);
					if (!containsAllCheck(listResult, listTemp))
						listResult.add(listTemp);
				}
			}
		}
		if (listObjectFinal == null || listObjectFinal.isEmpty()) {
			listResult.add(listTemp);
		}
		return listResult;
	}

	@Override
	public boolean containsAllCheck(List<List<Object>> listObjectFinal, List<Object> listTemp){
		for (List<Object> listObj : listObjectFinal) {
			if (listObj.containsAll(listTemp)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Object> containsCheck(List<Object> listObj, String strValue){
		List<Object> listResult = null;
		for (Object obj : listObj) {
			String strValueList = String.valueOf(obj);
			if (strValueList.substring(strValueList.indexOf("#")).length() == strValue.substring(
					strValue.indexOf("#")).length()) {
				listResult = listObj.subList(0, listObj.indexOf(obj));
				return listResult;
			}
		}
		return listResult;
	}

	@Override
	public Set<String> getTableNameInsert(List<Object> listTableNamewithHash, int iHashCount){
		Set<String> setTableNames = new HashSet<String>();
		for (Object objTbName : listTableNamewithHash) {
			String strTbName = String.valueOf(objTbName);
			if (strTbName.substring(strTbName.indexOf("#"), strTbName.length()).length() == iHashCount) {
				setTableNames.add(strTbName.substring(0, strTbName.indexOf("#")));
			}
		}
		return setTableNames;
	}

	@Override
	public int getHighestHashValue(List<Object> listTableNamewithHash){
		int i = 0;
		for (Object objTbName : listTableNamewithHash) {
			String strTbName = String.valueOf(objTbName);
			int j = strTbName.substring(strTbName.indexOf("#"), strTbName.length()).length();
			if (j > i) {
				i = j;
			}
		}
		return i;
	}

	@Override
	public List<String> getAllTables(JdbcTemplate template){
		String strMethodName = " [ getTables() ] ";
		List<String> listTableNames = new ArrayList<String>();
		try {
			List<Map<String, Object>> listString = template.queryForList(TdgCentralConstant
					.getSpecificDBQuery(getDbType(), TdgCentralConstant.GET_ALL_TABLES,
							getVectorVals()));
			for (Map<String, Object> mapValues : listString) {
				listTableNames.add(mapValues.get("TABLE_NAME").toString().toUpperCase());
			}
			logger.info(strClassName + strMethodName + " got all tables list from passed schema");
			logger.info(strClassName + strMethodName + " return getAllTables method");
		} finally {
			//cleanupDataSource(template);
		}
		return listTableNames;
	}

	@Override
	public String getMaxValue(JdbcTemplate jdbcTemplate, String strDbSeq){
		System.out.println("without long"+String.valueOf(jdbcTemplate.queryForObject(strDbSeq,Object.class))); //2.65228071E8
		/*for long value*/
	
		
		String strValue = String.valueOf(jdbcTemplate.queryForObject(strDbSeq,Object.class));
		 Pattern pattern = Pattern.compile(".*[a-zA-Z]+.*");
		 
	      //String str = "a123";
	      Matcher matcher = pattern.matcher(strValue);
	 
	      if (matcher.matches()) {
		 strValue = String.valueOf(jdbcTemplate.queryForLong(strDbSeq));
		System.out.println("checkvalue"+strValue);
	      }//265228071 
		return strValue;
		
	}

	@Override
	public Map<String, Integer> getTableColumnsInSequence(JdbcTemplate jdbcTemplate,
			String strTableName){
		Map<String, Integer> mapString = new HashMap<String,Integer>();
		Vector<String> vct = new Vector<String>(getVectorVals());
		/*to remove hard-coded dbo and adding code for sqlserver bcoz schema.table name*/
		if(strTableName.contains(".")){
		String[] tabsWithSchema = strTableName.split("[.]");
		
		StringBuffer comma1 = new StringBuffer();
		comma1.append('\'').append(tabsWithSchema[0]).append('\'');
		StringBuffer comma2 = new StringBuffer();
		comma2.append('\'').append(tabsWithSchema[1]).append('\'');
			vct.add(comma1.toString());
			vct.add(comma2.toString());
			/*till here*/
			
		}
		else{
			vct.add(strTableName);
		}
		
		
		//needs to check null
		List<Map<String, Object>> listString = TdgCoreConverter.getConvertedResult(jdbcTemplate, TdgCentralConstant
					.getSpecificDBQuery(getDbType(), TdgCentralConstant.GET_COLS_TABS_SEQUENCE,
							vct), getDbType());
		for (Map<String, Object> mapValues : listString) {
			mapString.put(mapValues.get("COLUMN_NAME").toString().toUpperCase(),Integer.parseInt(String.valueOf(mapValues.get("COLUMN_ID"))));
		}
		return sortByComparator(mapString);
	}
	
	private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap) {

		// Convert Map to List
		List<Map.Entry<String, Integer>> list =
			new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1,
                                           Map.Entry<String, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		// Convert sorted map back to a Map
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Integer> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	

	@Override
	public List<String> getDateColumns(JdbcTemplate jdbcTemplate, String strTableName){
		List<String> lstString = new ArrayList<String>();
		Vector<String> vct = new Vector<String>(getVectorVals());
		vct.add(strTableName);
		//needs to check null
		List<Map<String, Object>> listString = TdgCoreConverter.getConvertedResult(jdbcTemplate, TdgCentralConstant
					.getSpecificDBQuery(getDbType(), TdgCentralConstant.GET_DATE_COLUMNS_OF_TABLE,
							vct), getDbType());
		for (Map<String, Object> mapValues : listString) {
			lstString.add(mapValues.get("COLUMN_NAME").toString().toUpperCase());
		}
		return lstString;
	}

	@Override
	public List<String> fetchAllManualDictionaries(EntityManager managerentity){
		final List<String> lstString = new ArrayList<String>();
		Session hibernateSession = managerentity.unwrap(Session.class);

	    hibernateSession.doWork(new org.hibernate.jdbc.Work() {

	        @Override
	        public void execute(Connection connection) throws SQLException {
	            // do whatever you need to do with the connection
	        	ResultSet rs = connection.createStatement().executeQuery("select TABLE_NAME from USER_ALL_TABLES WHERE TABLE_NAME LIKE 'MANUAL_%'");
	        	while(rs.next())
	    			lstString.add(rs.getString("TABLE_NAME"));
	    		}
	    });
		
		return lstString;
	}
	
	@Override
	public List<Map<String, Object>> getAutoIncrementColumns(JdbcTemplate template,String strTableName){
		String strMethodName = " [ getAutoIncrementColumns() ] ";
		//JdbcTemplate template = getTemplate(url, username, password);
		List<Map<String, Object>> listResult = new ArrayList<Map<String, Object>>();
		try {
			Vector<String> vct = new Vector<String>(getVectorVals());
			/*to remove hard-coded dbo and adding code for sqlserver bcoz schema.table name*/
			if(strTableName.contains(".")){
			String[] tabsWithSchema = strTableName.split("[.]");
			
			StringBuffer comma1 = new StringBuffer();
			comma1.append('\'').append(tabsWithSchema[0]).append('\'');
			StringBuffer comma2 = new StringBuffer();
			comma2.append('\'').append(tabsWithSchema[1]).append('\'');
				vct.add(comma1.toString());
				vct.add(comma2.toString());
				/*till here*/
				
			}
			else
			vct.add(strTableName);
			listResult = template.queryForList(TdgCentralConstant
					.getSpecificDBQuery(getDbType(), TdgCentralConstant.GET_AUTO_INCREMENT_COLUMNS,
							vct));
			
			logger.info(strClassName + strMethodName + " got all autoincremnt columns list from passed schema");
			logger.info(strClassName + strMethodName + " return getAutoIncrementColumns method");
		} catch(Exception e){
			logger.error(e);
		}finally {
			//cleanupDataSource(template);
		}
		return listResult;
	}
	
	@Override
	public Set<String> getComputedColumns(JdbcTemplate jdbcTemplate,
			String strTableName) {
		Set<String> result = new HashSet<String>();
		Vector<String> vct = new Vector<String>(getVectorVals());
		vct.add(strTableName);
		List<Map<String, Object>> listString = jdbcTemplate
				.queryForList(TdgCentralConstant.getSpecificDBQuery(
						getDbType(), TdgCentralConstant.CHUBB_COMPUTED_COLUMNS,
						vct));
		for (Map<String, Object> mapValues : listString) {
			result.add(mapValues.get("COLUMN_NAME").toString()
					.toUpperCase());
		}
		return result;
	}
	}


	/*@Override
	public Vector<String> getVectorVals(){
		Vector<String> vct = new Vector<String>();
		if (getSchemaName() != null && !"".equals(getSchemaName())) {
			vct.add(getSchemaName());
		}
		return vct;
	}*/

//}
