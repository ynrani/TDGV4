/*
 * Object Name : TdgCoreConverter.java
 * Modification Block
 * ---------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ---------------------------------------------------------------------
 * 	1.	  vkrish14		Jun 15, 2015			NA             Created
 * ---------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

public class TdgCoreConverter extends TdgCentralConstant{
	public static List<Map<String, Object>> getConvertedResult(JdbcTemplate template,
			String strQuery, String dbType){
		List<Map<String, Object>> lstResult = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> listRes = template.queryForList(strQuery);
		if (dbType != null && (TdgCentralConstant.DB_TYPE_MYSQL.equals(dbType) || TdgCentralConstant.DB_TYPE_SQLSERVER.equals(dbType) || TdgCentralConstant.DB_TYPE_POSTGRES.equals(dbType) || TdgCentralConstant.DB_TYPE_DB2.equals(dbType))) {
			for (Map<String, Object> map : listRes) {
				// column_name,data_type,character_maximum_length,numeric_precision,datetime_Precision
				Map<String, Object> mapRs = new HashMap<String, Object>();
				for (Map.Entry<String, Object> mapEntry : map.entrySet()) {
					if (mapEntry.getKey().toUpperCase().equals(TdgMySQLConstant.DATA_TYPE)) {
						if (map.get(TdgMySQLConstant.CHARACTER_MAXIMUM_LENGTH) != null) {
							mapRs.put(TdgCentralConstant.DATA_LENGTH,
									map.get(TdgMySQLConstant.CHARACTER_MAXIMUM_LENGTH));
							mapRs.put(TdgCentralConstant.DATA_TYPE,
									TdgCentralConstant.ORACLE_VARCHAR);
						} else if (map.get(TdgMySQLConstant.NUMERIC_PRECISION) != null) {
							mapRs.put(TdgCentralConstant.DATA_LENGTH,
								map.get(TdgMySQLConstant.NUMERIC_PRECISION));
							mapRs.put(TdgCentralConstant.DATA_TYPE, TdgMySQLConstant.ORACLE_NUMBER);
						}else if (map.get(TdgMySQLConstant.DATETIME_PRECISION) != null) {
							mapRs.put(TdgCentralConstant.DATA_LENGTH,
									map.get(TdgMySQLConstant.DATETIME_PRECISION));
							mapRs.put(TdgCentralConstant.DATA_TYPE, TdgCentralConstant.ORACLE_DATE);
						}
						//following are added for bit and geography type of sql server 2012 bit data type and geography data type
						else{							
							mapRs.put(TdgCentralConstant.DATA_LENGTH,
									1);//static code either 0 or 1 for bit type of sql server 
								mapRs.put(TdgCentralConstant.DATA_TYPE, TdgMySQLConstant.ORACLE_NUMBER);
						}
					} else if (mapEntry.getKey().toUpperCase()
							.equals(TdgMySQLConstant.CONSTRAINT_TYPE)) {
						String strType = map.get(TdgMySQLConstant.CONSTRAINT_TYPE).toString();
						if (strType.equals(TdgMySQLConstant.PRIMARY)
								|| strType.equals(TdgMySQLConstant.PRIMARY_KEY)) {
							mapRs.put(TdgCentralConstant.CONSTRAINT_TYPE,
									TdgCentralConstant.PRIMARY);
						} else if (TdgMySQLConstant.FOREIGN.equals(strType)
								|| TdgMySQLConstant.FOREIGN_KEY.equals(strType)) {
							mapRs.put(TdgCentralConstant.CONSTRAINT_TYPE,
									TdgCentralConstant.FOREIGN);
						} else if (TdgMySQLConstant.UNIQUE.equals(strType)
								|| TdgMySQLConstant.UNIQUE_KEY.equals(strType)) {
							mapRs.put(TdgCentralConstant.CONSTRAINT_TYPE, TdgCentralConstant.UNIQUE);
						} else if (TdgMySQLConstant.IS_NULLABLE.equals(strType)
								|| TdgOldDB2Constant.NULLABLE.equals(strType)) {
							mapRs.put(TdgCentralConstant.CONSTRAINT_TYPE,
									TdgCentralConstant.NOT_NULL);
						}
					} else if (TdgCentralConstant.TABLE_NAME
							.equals(mapEntry.getKey().toUpperCase())) {
						mapRs.put(TdgCentralConstant.TABLE_NAME, mapEntry.getValue().toString()
								.toUpperCase());
					} else if (TdgCentralConstant.COLUMN_NAME.equals(mapEntry.getKey()
							.toUpperCase())) {
						mapRs.put(TdgCentralConstant.COLUMN_NAME, mapEntry.getValue().toString()
								.toUpperCase());
					} else if (TdgCentralConstant.COLUMN_ID.equals(mapEntry.getKey()
							.toUpperCase())) {
						mapRs.put(TdgCentralConstant.COLUMN_ID, mapEntry.getValue().toString()
								.toUpperCase());
					} else if (TdgCentralConstant.CONSTRAINT_NAME.equals(mapEntry.getKey()
							.toUpperCase())) {
						mapRs.put(TdgCentralConstant.CONSTRAINT_NAME,
								map.get(TdgCentralConstant.CONSTRAINT_NAME).toString()
										.toUpperCase());
					} else if (TdgCentralConstant.IS_NULLABLE_NAME.equals(mapEntry.getKey()
							.toUpperCase())) {
						mapRs.put(TdgCentralConstant.IS_NULLABLE_NAME,
								map.get(TdgCentralConstant.IS_NULLABLE_NAME).toString()
										.toUpperCase());
					} else if (TdgCentralConstant.NULLABLE_NAME.equals(mapEntry.getKey()
							.toUpperCase())) {
						mapRs.put(TdgCentralConstant.IS_NULLABLE_NAME,
								TdgCentralConstant.IS_NULLABLE);
					}
				}
				lstResult.add(mapRs);
			}
			//below code commented for DB2 11 support version 
		/*} else if (dbType != null && TdgCentralConstant.DB_TYPE_DB2.equals(dbType)) {
			for (Map<String, Object> map : listRes) {
				// column_name,data_type,character_maximum_length,numeric_precision,datetime_Precision
				Map<String, Object> mapRs = new HashMap<String, Object>();
				for (Map.Entry<String, Object> mapEntry : map.entrySet()) {
					mapRs.put(mapEntry.getKey(), mapEntry.getValue());
					if (TdgCentralConstant.NULLABLE_NAME.equals(mapEntry.getKey().toUpperCase())) {
						mapRs.put(TdgCentralConstant.IS_NULLABLE_NAME,
								TdgCentralConstant.IS_NULLABLE);
					}
					if (mapRs.containsKey(TdgOracleConstant.CHARACTER_LENGTH)
							&& (mapRs.get(TdgOracleConstant.CHARACTER_LENGTH) != null && !"0"
									.equals(mapRs.get(TdgOracleConstant.CHARACTER_LENGTH) + ""))) {
						mapRs.put(TdgCentralConstant.DATA_LENGTH,
								mapRs.get(TdgOracleConstant.CHARACTER_LENGTH));
						if (!map.containsKey(TdgCentralConstant.DATA_TYPE)
								&& !"DATE".equalsIgnoreCase(map.get(TdgCentralConstant.DATA_TYPE)
										+ ""))
							mapRs.put(TdgCentralConstant.DATA_TYPE,
									TdgCentralConstant.ORACLE_VARCHAR);
					} else if (mapRs.containsKey(TdgOracleConstant.DATA_PRECISION)
							&& (mapRs.get(TdgOracleConstant.DATA_PRECISION) != null && !"0"
									.equals(mapRs.get(TdgOracleConstant.DATA_PRECISION) + ""))) {
						mapRs.put(TdgCentralConstant.DATA_LENGTH,
								mapRs.get(TdgOracleConstant.DATA_PRECISION));
						mapRs.put(TdgCentralConstant.DATA_TYPE, TdgMySQLConstant.ORACLE_NUMBER);
					} else if (mapRs.containsKey(TdgMySQLConstant.DATETIME_PRECISION)
							&& (mapRs.get(TdgMySQLConstant.DATETIME_PRECISION) != null && !"0"
									.equals(mapRs.get(TdgMySQLConstant.DATETIME_PRECISION) + ""))) {
						mapRs.put(TdgCentralConstant.DATA_LENGTH,
								mapRs.get(TdgMySQLConstant.DATETIME_PRECISION));
						mapRs.put(TdgCentralConstant.DATA_TYPE, TdgCentralConstant.ORACLE_DATE);
					}
				}
				lstResult.add(mapRs);
			}*/
		} else {
			if (dbType != null && (TdgCentralConstant.DB_TYPE_ORACLE.equals(dbType))) {
				for (Map<String, Object> mapRs : listRes) {
					if (mapRs.containsKey(TdgOracleConstant.CHARACTER_LENGTH)
							&& (mapRs.get(TdgOracleConstant.CHARACTER_LENGTH) != null && !"0"
									.equals(mapRs.get(TdgOracleConstant.CHARACTER_LENGTH) + ""))) {
						mapRs.put(TdgCentralConstant.DATA_LENGTH,
								mapRs.get(TdgOracleConstant.CHARACTER_LENGTH));
						if (!mapRs.containsKey(TdgCentralConstant.DATA_TYPE)
								&& !"DATE".equalsIgnoreCase(mapRs.get(TdgCentralConstant.DATA_TYPE)
										+ ""))
							mapRs.put(TdgCentralConstant.DATA_TYPE,
									TdgCentralConstant.ORACLE_VARCHAR);
					} else if (mapRs.containsKey(TdgOracleConstant.DATA_PRECISION)
							&& (mapRs.get(TdgOracleConstant.DATA_PRECISION) != null && !"0"
									.equals(mapRs.get(TdgOracleConstant.DATA_PRECISION) + ""))) {
						mapRs.put(TdgCentralConstant.DATA_LENGTH,
								mapRs.get(TdgOracleConstant.DATA_PRECISION));
						mapRs.put(TdgCentralConstant.DATA_TYPE, TdgMySQLConstant.ORACLE_NUMBER);
					} else if (mapRs.containsKey(TdgMySQLConstant.DATETIME_PRECISION)
							&& (mapRs.get(TdgMySQLConstant.DATETIME_PRECISION) != null && !"0"
									.equals(mapRs.get(TdgMySQLConstant.DATETIME_PRECISION) + ""))) {
						mapRs.put(TdgCentralConstant.DATA_LENGTH,
								mapRs.get(TdgMySQLConstant.DATETIME_PRECISION));
						mapRs.put(TdgCentralConstant.DATA_TYPE, TdgCentralConstant.ORACLE_DATE);
					}else if (mapRs.containsKey(TdgOracleConstant.DATA_TYPE)
							&& mapRs.get(TdgOracleConstant.DATA_PRECISION) == null && "0"
									.equals(mapRs.get(TdgOracleConstant.CHARACTER_LENGTH) + "")){
						mapRs.put(TdgCentralConstant.DATA_LENGTH,
								22);
						mapRs.put(TdgCentralConstant.DATA_TYPE, TdgMySQLConstant.ORACLE_NUMBER);
					}
					lstResult.add(mapRs);
				}
			} else {
				lstResult.addAll(listRes);
			}
		}
		return lstResult;
	}

	public static List<Map<String, Object>> getConvertInputResult(JdbcTemplate template,
			String strQuery, String dbType){
		String strTemp = "";
		List<Map<String, Object>> lstResult = null;
		if (dbType != null && TdgCentralConstant.DB_TYPE_MYSQL.equals(dbType)) {
			strTemp = "";
		} else {
			strTemp = strQuery;
		}
		lstResult = getConvertedResult(template, strTemp, dbType);
		return lstResult;
	}
}
