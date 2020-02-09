/*
 * Object Name : TdgDB2Constant.java
 * Modification Block
 * ---------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ---------------------------------------------------------------------
 * 	1.	  vkrish14		Jun 15, 2015			NA             Created
 * ---------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.util;

public class TdgOldDB2Constant extends TdgCentralConstant{
	public static final String NULLABLE = "N";
	public static final String DB2_SEQUENCE_VALUE = " SELECT NEXT VALUE FOR {0} AS IDGEN FROM SYSIBM.DUAL";
	public static final String DB2_PRIMARY_KEY = "SELECT NVL(MAX({0}),0) AS IDGEN FROM {1}";
	public static final String DB2_PRIMARY_KEY_FINAL = " (SELECT NVL(MAX(TO_NUMBER(REPLACE({0},'{#}',''))),0) AS IDGEN FROM {1} )";
	public static final String DB2_MAX_COLUMN_VALUE = "(SELECT NVL(MAX({0}),0) AS IDGEN FROM {1} )";
	public static final String DB2_GET_ALL_TABLES = "SELECT TABLE_NAME FROM USER_ALL_TABLES WHERE TABLE_SCHEMA='{0}'";
	public static final String DB2_GET_ALL_COLUMNS = "SELECT COLUMN_NAME,TABLE_NAME FROM USER_TAB_COLUMNS WHERE TABLE_SCHEMA='{0}'";
	public static final String DB2_GET_ALL_COLUMNS_BY_TAB = "select COLUMN_NAME,TABLE_NAME from USER_TAB_COLUMNS WHERE TABLE_NAME in ({1}) AND TABLE_SCHEMA='{0}'";
	public static final String DB2_GET_DATE_COLUMNS_OF_TABLE = "select COLUMN_NAME from USER_TAB_COLUMNS WHERE TABLE_SCHEMA='{0}' AND TABLE_NAME='{1}'  AND (DATA_TYPE LIKE 'TIMESTAMP' OR DATA_TYPE='DATE')";
	public static final String DB2_GET_COLS_TABS_SEQUENCE = "select Column_name,COLUMN_ID from USER_TAB_COLUMNS where TABLE_SCHEMA='{0}' AND TABLE_NAME='{1}' order by COLUMN_ID";
	public static final String DB2_GET_PK_COLUMN_TYPE = "select cols.column_name,cols.data_type,DATA_PRECISION,CHAR_LENGTH from USER_TAB_COLUMNS cols,USER_CONS_COLUMNS where cols.TABLE_SCHEMA='{0}' AND cols.COLUMN_NAME= USER_CONS_COLUMNS.COLUMN_NAME and USER_CONS_COLUMNS.CONSTRAINT_NAME='{1}' AND cols.TABLE_NAME='{2}'";
	public static final String DB2_GET_ALL_CONSTRAINTS_COLUMNS = "select table_name,column_name,data_type,DATA_PRECISION,CHAR_LENGTH from USER_TAB_COLUMNS where TABLE_SCHEMA='{0}' AND COLUMN_NAME in (select column_name from USER_CONS_COLUMNS where constraint_name in (select constraint_name From user_constraints where CONSTRAINT_TYPE in ('R','U','C')))";
	public static final String DB2_GET_SEQUENCE_OF_COLUMNS1 = "select Column_name,DATA_TYPE,DATA_PRECISION,CHAR_LENGTH from USER_TAB_COLUMNS where TABLE_SCHEMA='{0}'  AND table_name='{1}' order by COLUMN_ID";
	public static final String DB2_GET_SEQUENCE_OF_COLUMNS2 = "select Column_name,DATA_TYPE,DATA_PRECISION,CHAR_LENGTH from USER_TAB_COLUMNS where TABLE_SCHEMA='{0}' AND table_name='{1}'  and column_name != '{2}' order by COLUMN_ID";
	public static final String DB2_GET_TABLES_BY_COLUMNS = "select Column_name,DATA_TYPE,DATA_PRECISION,CHAR_LENGTH,table_name from USER_TAB_COLUMNS where TABLE_SCHEMA='{0}' AND column_name in ";
	public static final String DB2_GET_TABLE_NAME_BY_FK = "select table_name from user_constraints where TABLE_SCHEMA='{0}' AND CONSTRAINT_NAME in (select r_constraint_name From USER_CONSTRAINTS where CONSTRAINT_NAME in (select CONSTRAINT_NAME from USER_CONS_COLUMNS where TABLE_NAME='{1}'  AND COLUMN_NAME='{2}' )) ";
	public static final String DB2_GET_SEQUENCE_TABLES = "select table_name from USER_CONSTRAINTS where TABLE_SCHEMA='{0}' AND CONSTRAINT_NAME in (select R_constraint_name From user_constraints where table_name='{1}' and CONSTRAINT_TYPE='R') ";
	public static final String DB2_GET_CONSTRAINTS_OF_TABLES = "select constraint_name,constraint_type From user_constraints where TABLE_SCHEMA='{0}' AND CONSTRAINT_TYPE in ('R','U','P','C') and table_name='{1}' ";
	public static final String DB2_ONE_TO_ONE_RELATIONS_FIND_TABLES = "select *from (select column_name,table_name,constraint_name from (select column_name,table_name,constraint_name from ALL_CONS_COLUMNS where CONSTRAINT_NAME in (select constraint_name From user_constraints"
			+ " where TABLE_SCHEMA='{0}' AND table_name in ({1}) and CONSTRAINT_TYPE IN('U','C')))) tt join "
			+ " (select *From (select column_name,table_name,constraint_name from (select column_name,table_name,constraint_name from ALL_CONS_COLUMNS where CONSTRAINT_NAME in (select constraint_name From user_constraints "
			+ " where TABLE_SCHEMA='{0}' AND table_name in ({1}) and CONSTRAINT_TYPE='R')))) ttt on tt.Column_name = ttt.column_name";
	public static final String DB2_CONSTRAINTS_RELATIONS_TABLES = "select table_name from user_constraints where constraint_name in (select R_CONSTRAINT_NAME from USER_CONSTRAINTS where TABLE_SCHEMA='{0}' AND CONSTRAINT_NAME='{1}' AND TABLE_NAME='{2}') ";
	public static final String DB2_GET_NOT_NULL_CONSTRAINTS_OF_TABLES = "select column_name,data_type,DATA_PRECISION,CHAR_LENGTH,table_name,nullable from USER_TAB_COLUMNS where TABLE_SCHEMA='{0}' AND table_name='{1}' and nullable='N' and Column_name not in ( select column_name from USER_CONS_COLUMNS where table_name='{1}') ";
	public static final String DB2_GET_COLUMNS_OF_TABS_APART_CONSTRAINTS = "select Column_name,DATA_TYPE,DATA_PRECISION,CHAR_LENGTH From USER_TAB_COLS where column_name not in(select column_name From USER_CONS_COLUMNS where TABLE_SCHEMA='{0}' AND TABLE_NAME ='{1}') and table_name='{1}'";
}
