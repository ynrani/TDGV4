/*
\ * Object Name : TdgCentralConstant.java
 * Modification Block
 * ---------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ---------------------------------------------------------------------
 * 	1.	  vkrish14		Jun 15, 2015			NA             Created
 * ---------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

enum dataTypes {
	NUMBER, VARCHAR2, VARCHAR;
}

public class TdgCentralConstant{
	public static final int PAGINATION_SIZE = 10;
	public static final int UNIQUE_RANDOMN_LENGTH = 10;
	public static final int GENERATECOUNT_LIMIT = 500000;
	public static final int BATCH_CHUNK_LIMIT = 10000;
	public static final boolean SEQUENCE_INSERT_FROM_DICTIONARY = false;
	//public static final String TDG_NOT_NULL_CONSTRAINT = "0";
	public static final String TDG_NOT_NULL_CONSTRAINT = "  ";
	/**
	 * For oracle
	 */
	public static final String INSERT_QUERY = "INSERT_QUERY";
	public static final String UPDATE_QUERY_WITH_WHERE="UPDATE_QUERY_WITH_WHERE";
	public static final String UPDATE_QUERY="UPDATE_QUERY";
	public static final String DELETE_QUERY_WITH_WHERE="DELETE_QUERY_WITH_WHERE";
	public static final String DELETE_QUERY="DELETE_QUERY";
	//public static final int BATCH_SIZE = 1000;
	public static final String ONE_TO_ONE = "ONE_TO_ONE";
	public static final String ONE_TO_MANY = "ONE_TO_MANY";
	public static final String MANY_TO_ONE = "MANY_TO_ONE";
	public static final String MANY_TO_MANY = "MANY_TO_MANY";
	public static final String SELECT_FOR_PK_VALUES = " SELECT {0} FROM {1} WHERE 1=1  ";
	/* For oracle */
	public static final String PRIMARY = "P";
	public static final String FOREIGN = "R";
	public static final String UNIQUE = "U";
	public static final String NOT_NULL = "C";
	public static final String ORACLE_NUMBER = "NUMBER";
	public static final String ORACLE_VARCHAR = "VARCHAR";
	public static final String ORACLE_VARCHAR2 = "VARCHAR2";
	public static final String ORACLE_CHAR = "CHAR";
	public static final String ORACLE_DATE = "DATE";
	public static final String CONSTRAINT_TYPE = "CONSTRAINT_TYPE";
	public static final String DATA_LENGTH = "DATA_LENGTH";
	public static final String DATA_TYPE = "DATA_TYPE";
	public static final String TABLE_NAME = "TABLE_NAME";
	public static final String COLUMN_NAME = "COLUMN_NAME";
	public static final String CONSTRAINT_NAME = "CONSTRAINT_NAME";
	public static final String SEQUENCE_VALUE = "SEQUENCE_VALUE";
	
	//following are used in master dictionary creation purpose also in GUI
	public static final String SCHEMA_URL = "SCHEMA_URL";
	public static final String SCHEMA_NAME = "SCHEMA_NAME";
	public static final String SCHEMA_PASS = "SCHEMA_PASS";
	public static final String SCHEMA_SEQUENCE_PREFIX_TABS = "SCHEMA_SEQUENCE_PREFIX_TABS";
	public static final String SCHEMA_MASTER_TABS = "SCHEMA_MASTER_TABS";
	public static final String SCHEMA_DEPENDS_DBS = "SCHEMA_DEPENDS_DBS";
	public static final String SCHEMA_PASSED_TABS = "SCHEMA_PASSED_TABS";
	public static final String SCHEMA_DATE_FORMATE = "SCHEMA_DATE_FORMATE";
	public static final String SCHEMA_REGEX_COLUMNS = "SCHEMA_REGEX_COLUMNS";	
	public static final String SCHEMA_BUSINESS_RULES= "SCHEMA_BUSINESS_RULES";
	public static final String SCHEMA_REQUESTED_COLUMNS = "SCHEMA_REQUESTED_COLUMNS";
	public static final String SCHEMA_COLUMNS_HEADERS = "SCHEMA_COLUMNS_HEADERS";
	public static final String SCHEMA_DB_TYPE_HEADERS = "SCHEMA_COLUMNS_HEADERS";
	
	
	public static final String SCHEMA_GENERATION_TYPE_DB = "DB";
	public static final String SCHEMA_GENERATION_TYPE_DB_AND_XLS = "DBXSL";
	public static final String SCHEMA_GENERATION_TYPE_XLS = "XLS";
	public static final String SCHEMA_GENERATION_TYPE_CSV = "CSV";
	public static final String SCHEMA_GENERATION_TYPE_XML = "XML";
	public static final String GENERATION_TYPE_SEPERATOR=",";
	
	//TDG Reserved symbols
	
	public static final String TDG_COLON=":";
	public static final String TDG_HASH="#";
	public static final String TDG_AUTOINCREMENT = "TDG_AIT";
	public static final String TDG_DOLLOR="$";
	public static final String TDG_MT="TDG_MT";
	public static final String TDG_DOT="TDG_DOT";
	public static final String TDG_CAMA= ",";
	
	
	/* GUI related constants */
	public static final String REGEX = "REGEX";
	public static final String REGEXSEQUENCE = "REGEXSEQUENCE";
	public static final String TDG_DB_SEQ = "TDG_DB_SEQ";
	public static final String GREATER_THAN = "G_T";
	public static final String LESSER_THAN = "L_T";
	public static final String BETWEEN_VALULES= "B_T";
	public static final String UPPER_VALULES= "TDG_UPPER";
	public static final String LOWER_VALULES= "TDG_LOWER";
	
	public static final String GUI_TEXT_BOX = "TEXTBOX";
	public static final String GUI_RADIO_BOX = "RADIOBOX";
	public static final String GUI_CHECK_BOX = "CHECKBOX";
	public static final String GUI_DATE_BOX = "DATEBOX";
	public static final String SUCCESS_MESSAGE = "SUCCESS";
	public static final String FAILED_MESSAGE = "FAILED";
	public static final String DB_TYPE_ORACLE = "Oracle";
	public static final String DB_TYPE_MYSQL = "MySQL";
	public static final String DB_TYPE_DB2 = "DB2";
	public static final String DB_TYPE_SQLSERVER = "SQLServer";
	public static final String DB_TYPE_POSTGRES = "Postgres";
	public static final String DB_TYPE_DERBY = "Derby";
	public static final String DB_TYPE_TERADATA = "Teradata";
	
	public static final String CLASS_NAME = "Tdg{0}Constant";
	public static final String PRIMARY_KEY = "PRIMARY_KEY";
	public static final String IS_NULLABLE = "NO";
	public static final String GET_TABLES = "GET_TABLES";
	public static final String MAX_COLUMN_VALUE="MAX_COLUMN_VALUE";
	public static final String GET_ALL_TABLES = "GET_ALL_TABLES";
	public static final String GET_ALL_COLUMNS = "GET_ALL_COLUMNS";
	public static final String GET_ALL_CONSTRAINTS_COLUMNS = "GET_ALL_CONSTRAINTS_COLUMNS";
	public static final String GET_TABLES_BY_COLUMNS = "GET_TABLES_BY_COLUMNS";
	public static final String GET_TABLE_NAME_BY_FK = "GET_TABLE_NAME_BY_FK";
	public static final String GET_SEQUENCE_TABLES = "GET_SEQUENCE_TABLES";
	public static final String GET_CONSTRAINTS_OF_TABLES = "GET_CONSTRAINTS_OF_TABLES";
	public static final String ONE_TO_ONE_RELATIONS_FIND_TABLES = "ONE_TO_ONE_RELATIONS_FIND_TABLES";
	public static final String CONSTRAINTS_RELATIONS_TABLES = "CONSTRAINTS_RELATIONS_TABLES";
	public static final String GET_SEQUENCE_OF_COLUMNS1 = "GET_SEQUENCE_OF_COLUMNS1";
	public static final String GET_SEQUENCE_OF_COLUMNS2 = "GET_SEQUENCE_OF_COLUMNS2";
	public static final String GET_PK_COLUMN_TYPE = "GET_PK_COLUMN_TYPE";
	public static final String GET_NNK_COLUMN_TYPE = "GET_NNK_COLUMN_TYPE";
	public static final String GET_COLUMNS_OF_TABS_APART_CONSTRAINTS = "GET_COLUMNS_OF_TABS_APART_CONSTRAINTS";
	public static final String GET_ALL_COLUMNS_BY_TAB = "GET_ALL_COLUMNS_BY_TAB";
	public static final String GET_COLS_TABS_SEQUENCE="GET_COLS_TABS_SEQUENCE";
	public static final String GET_DATE_COLUMNS_OF_TABLE = "GET_DATE_COLUMNS_OF_TABLE";
	public static final String GET_AUTO_INCREMENT_COLUMNS="GET_AUTO_INCREMENT_COLUMNS";
	
	// Data base related constants added here
	public static final String ORACLE = "ORACLE";
	public static final String MYSQL = "MYSQL";
	public static final String DB2 = "DB2";
	public static final String SQLSERVER = "SQLSERVER";
	public static final String POSTGRES = "POSTGRES";
	public static final String DERBY = "DERBY";
	public static final String TERADATA = "TERADATA";
	public static final String INSERT="INSERT";
	public static final String VALUES="VALUES";
	public static final String UPDATE="UPDATE";
	public static final String SET="SET";
	public static final String DELETE="DELETE";
	//End of databases
	public static final String GET_NOT_NULL_CONSTRAINTS_OF_TABLES = "GET_NOT_NULL_CONSTRAINTS_OF_TABLES";
	public static final String IS_NULLABLE_NAME = "IS_NULLABLE";
	public static final String NULLABLE_NAME = "NULLABLE";
	public static final String DYNAMIC_PAGE_DETAILS = "DYNAMIC_PAGE_DETAILS";
	public static final String MANUAL_DICTIONARY = "MANUAL_DICTIONARY";
	public static final String TDG_REQUEST_GEN_LIST = "TDG_REQUEST_GEN_LIST";
	
	public static final String TDG_GENERATE_SUCCESS_MESSAGE = "{0} records are generated and reflected tables are {1}.";
	public static final String TDG_GENERATE_SUCCESS = "Success";
	public static final String TDG_GENERATE_FAILED = "Failed";
	
	public static final String TDG_GENERATED_REQUEST = "{0} are generated and request id in dashboard is: {1}";
	public static final String PRIMARY_KEY_FINAL = "PRIMARY_KEY_FINAL";
	public static final String COLUMN_ID = "COLUMN_ID";
	
	public static final String TDG_METRICS_QUERY1="select generationtype,count(1) from TDG_REQUEST_List group by GENERATIONTYPE";
	public static final String SESSION_CONTINUE = "SESSION_EXIST";
	public static final String LOGIN_PAGE = "/login";
	
	
	public static final List<String> TDG_MAIL_TYPES = Arrays.asList("@gmail.com","@yahoo.com","@chubb.com");
	public static final String TDG_MNLDT_SEQ="TDG_MNLDT_SEQ";
	public static final String CHUBB_COMPUTED_COLUMNS="CHUBB_COMPUTED_COLUMNS";
	public static final String CHUBB_UNQ_DEF ="CHUBB_UNQ_DEF";
	public static final String TDG_DB_SEQ_VCHR ="TDG_DB_SEQ_VCHR";
	/**
	 * 
	 * @param strValue
	 * @param vector
	 * @return
	 */
	public static String getReplacedValue(String strValue, Vector<String> vector){
		if (vector != null && !vector.isEmpty()) {
			String[] strRep = new String[vector.size()];
			String[] strRepVal = new String[vector.size()];
			for (int i = 0; i < strRep.length; i++) {
				strRep[i] = "{" + i + "}";
				strRepVal[i] = String.valueOf(vector.get(i));
			}
			return StringUtils.replaceEach(strValue, strRep, strRepVal);
		} else {
			return strValue;
		}
	}

	public static String generateString(String strType, String strColumnName,
			List<String> listValues){
		StringBuffer strBuffer = new StringBuffer(" AND ").append(strColumnName);
		/* if(String.valueOf(dataTypes.NUMBER).equals(strType)){ */
		if (listValues != null) {
			if (!listValues.isEmpty() && listValues.size() == 1) {
				// strBuffer.append(" = ").append(listValues.get(0));
				strBuffer.append(" = ").append('?');
			} else {
				strBuffer.append("IN (");
				for (int i = 0; i < listValues.size(); i++) {
					// strBuffer.append(listValues.get(i));
					strBuffer.append('?');
					if (i != 0 && i != listValues.size()) {
						strBuffer.append(',');
					}
					if (i == listValues.size()) {
						strBuffer.append(')');
					}
				}
			}
		}
		return strBuffer.toString();
	}

	public static String getSpecificDBQuery(String strDBType, String strSuffixConstant,
			Vector<String> vctValues){
		String strResponse = "";
		if (TdgCentralConstant.DB_TYPE_ORACLE.equals(strDBType)) {
			Vector<String> vct = new Vector<String>();
			vct.add(TdgCentralConstant.DB_TYPE_ORACLE);
			strResponse = getResponse(vct, TdgCentralConstant.ORACLE, vctValues, strSuffixConstant);
		} else if (TdgCentralConstant.DB_TYPE_MYSQL.equals(strDBType)) {
			Vector<String> vct = new Vector<String>();
			vct.add(TdgCentralConstant.DB_TYPE_MYSQL);
			strResponse = getResponse(vct, TdgCentralConstant.MYSQL, vctValues, strSuffixConstant);
		} else if (TdgCentralConstant.DB_TYPE_DB2.equals(strDBType)) {
			Vector<String> vct = new Vector<String>();
			vct.add(TdgCentralConstant.DB_TYPE_DB2);
			strResponse = getResponse(vct, TdgCentralConstant.DB2, vctValues, strSuffixConstant);
		}else if (TdgCentralConstant.DB_TYPE_SQLSERVER.equals(strDBType)) {
			Vector<String> vct = new Vector<String>();
			vct.add(TdgCentralConstant.DB_TYPE_SQLSERVER);
			strResponse = getResponse(vct, TdgCentralConstant.SQLSERVER, vctValues, strSuffixConstant);
			System.out.println("query is"+strResponse+"and name is"+strSuffixConstant);
		}else if (TdgCentralConstant.DB_TYPE_POSTGRES.equals(strDBType)) {
			Vector<String> vct = new Vector<String>();
			vct.add(TdgCentralConstant.DB_TYPE_POSTGRES);
			strResponse = getResponse(vct, TdgCentralConstant.POSTGRES, vctValues, strSuffixConstant);
		}
		return strResponse;
	}

	private static String getResponse(Vector<String> vct, String strDBValue,
			Vector<String> vctValues, String strSuffixConstant){
		String strResponse = "";
		String className = "com.tesda.util."
				+ TdgCentralConstant.getReplacedValue(TdgCentralConstant.CLASS_NAME, vct);
		@SuppressWarnings("rawtypes")
		Class clas = null;
		try {
			clas = Class.forName(className);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		if (clas != null) {
			Field[] fieldsArrays = clas.getDeclaredFields();
			if (fieldsArrays != null && fieldsArrays.length > 0) {
				for (int i = 0; i < fieldsArrays.length; i++) {
					try {
						String strFieldName = fieldsArrays[i].getName().toString();
						if (strFieldName != null
								&& strFieldName.equals(strDBValue + "_" + strSuffixConstant)) {
							strResponse = TdgCentralConstant.getReplacedValue(
									fieldsArrays[i].get(fieldsArrays[i].getName()).toString(),
									vctValues);
							break;
						}
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return strResponse;
	}
	
	
	public static <K,V> HashMap<V,K> reverse(Map<K,V> map) {
	    HashMap<V,K> rev = new HashMap<V, K>();
	    for(Map.Entry<K,V> entry : map.entrySet())
	        rev.put(entry.getValue(), entry.getKey());
	    return rev;
	}
}
