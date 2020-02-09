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

public class TdgDB2Constant extends TdgCentralConstant{
	public static final String NULLABLE = "N";
	public static final String DB2_SEQUENCE_VALUE = " SELECT NEXT VALUE FOR {0} AS IDGEN FROM SYSIBM.DUAL";
	public static final String DB2_PRIMARY_KEY = "SELECT NVL(MAX({0}),0) AS IDGEN FROM {1}";
	public static final String DB2_PRIMARY_KEY_FINAL = " (SELECT NVL(MAX(TO_NUMBER(REPLACE({0},'{#}',''))),0) AS IDGEN FROM {1} )";
	public static final String DB2_MAX_COLUMN_VALUE = "(SELECT NVL(MAX({0}),0) AS IDGEN FROM {1} )";
	public static final String DB2_GET_ALL_TABLES = "SELECT TABLE_NAME FROM sysibm.tables WHERE TABLE_SCHEMA='{0}'";
	public static final String DB2_GET_ALL_COLUMNS = "SELECT COLUMN_NAME,TABLE_NAME FROM sysibm.columns WHERE TABLE_SCHEMA='{0}'";
	public static final String DB2_GET_ALL_COLUMNS_BY_TAB = "select COLUMN_NAME,TABLE_NAME from sysibm.columns WHERE TABLE_NAME in ({1}) AND TABLE_SCHEMA='{0}'";
	public static final String DB2_GET_DATE_COLUMNS_OF_TABLE = "select COLUMN_NAME from sysibm.columns WHERE TABLE_SCHEMA='{0}' AND TABLE_NAME='{1}'  AND (DATA_TYPE LIKE 'TIMESTAMP' OR DATA_TYPE='DATE')";
	public static final String DB2_GET_COLS_TABS_SEQUENCE = "select Column_name,ordinal_position as COLUMN_ID from sysibm.columns where TABLE_SCHEMA='{0}' AND TABLE_NAME='{1}' order by ordinal_position";
	public static final String DB2_GET_PK_COLUMN_TYPE = "select cols.column_name,cols.data_type,cols.character_maximum_length,cols.numeric_precision,cols.datetime_Precision,cols.table_name from sysibm.columns cols,syscat.keycoluse keycol where cols.TABLE_SCHEMA='{0}' AND cols.COLUMN_NAME= keycol.COLNAME and keycol.CONSTNAME='{1}' AND cols.TABLE_NAME='{2}'";
	public static final String DB2_GET_ALL_CONSTRAINTS_COLUMNS = "select table_name,column_name,data_type,character_maximum_length,numeric_precision,datetime_Precision from sysibm.columns where TABLE_SCHEMA='{0}' AND COLUMN_NAME in (select colname from syscat.keycoluse where tabschema='{0}' and constname in (select name From sysibm.systabconst where CONSTRAINTYP in ('R','U','C') AND DEFINER='{0}'))";
	public static final String DB2_GET_SEQUENCE_OF_COLUMNS1 = "select table_name,column_name,data_type,character_maximum_length,numeric_precision,datetime_Precision from sysibm.columns where TABLE_SCHEMA='{0}'  AND table_name='{1}' order by ordinal_position";
	public static final String DB2_GET_SEQUENCE_OF_COLUMNS2 = "select table_name,column_name,data_type,character_maximum_length,numeric_precision,datetime_Precision from sysibm.columns where TABLE_SCHEMA='{0}' AND table_name='{1}'  and column_name != '{2}' order by ordinal_position";
	public static final String DB2_GET_TABLES_BY_COLUMNS = "select table_name,column_name,data_type,character_maximum_length,numeric_precision,datetime_Precision from sysibm.columns where TABLE_SCHEMA='{0}' AND column_name in ";
	
	public static final String DB2_GET_TABLE_NAME_BY_FK = "select tbname as table_name from sysibm.systabconst where definer='{0}' AND name in (select refkeyname From syscat.references where constname in (select constname from syscat.keycoluse where tabname=upper('{1}')  and colname=upper('{2}') and tabschema='{0}' )) ";
	
	public static final String DB2_GET_SEQUENCE_TABLES = "select reftabname as t_name from syscat.references where tabname='{1}' and tabschema='{0}'";
	public static final String DB2_GET_CONSTRAINTS_OF_TABLES = "select name as constraint_name,CONSTRAINTYP as constraint_type From sysibm.systabconst where DEFINER='{0}' AND CONSTRAINTYP in ('R','U','P','C') and tbname='{1}'";
	public static final String DB2_ONE_TO_ONE_RELATIONS_FIND_TABLES = "select *From (select colname as column_name ,tabname as table_name,constname as constraint_name from syscat.keycoluse where constname in (select name From sysibm.systabconst "
			+ " where definer='{0}' AND tbname in ({1}) and constraintyp IN('U','C'))) tt join "
			+ "(select colname as column_name,tabname as table_name,constname as constraint_name from syscat.keycoluse where constname in (select constname from syscat.references where tabname in ({1}) and tabschema='{0}')) ttt on tt.column_name= ttt.column_name";
			//+ " where TABLE_SCHEMA='{0}' AND table_name in ({1}) and CONSTRAINT_TYPE='R')))) ttt on tt.Column_name = ttt.column_name";
	public static final String DB2_CONSTRAINTS_RELATIONS_TABLES = "select reftabname as t_name from syscat.references where constname='{1}' and tabname='{2}' and tabschema='{0}'";
	public static final String DB2_GET_NOT_NULL_CONSTRAINTS_OF_TABLES = "select column_name,data_type,character_maximum_length,numeric_precision,datetime_Precision,table_name,is_nullable as nullable from sysibm.columns where TABLE_SCHEMA='{0}' AND table_name='{1}' and is_nullable='NO' and Column_name not in ( select colname from syscat.keycoluse where tabname='{1}' and tabschema='{0}') ";
	public static final String DB2_GET_COLUMNS_OF_TABS_APART_CONSTRAINTS = "select cols.column_name,cols.data_type,cols.character_maximum_length,cols.numeric_precision,cols.datetime_Precision,cols.table_name from sysibm.columns cols where cols.column_name not in(select keycol.COLNAME From syscat.keycoluse keycol where keycol.tabschema='{0}' AND keycol.tabname ='{1}') and cols.table_name='{1}' and cols.TABLE_SCHEMA='{0}'";
}
