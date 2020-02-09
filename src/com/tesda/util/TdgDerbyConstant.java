/*
 * Object Name : TdgOracleConstant.java
 * Modification Block
 * ---------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ---------------------------------------------------------------------
 * 	1.	  vkrish14		Jun 15, 2015			NA             Created
 * ---------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.util;

public class TdgDerbyConstant extends TdgCentralConstant{
	public static final String DERBY_DATE_FORMATE = "SELECT VALUE FROM NLS_SESSION_PARAMETERS WHERE PARAMETER='NLS_DATE_FORMAT'";
	public static final String DERBY_SEQUENCE_VALUE = " SELECT {0}.NEXTVAL AS IDGEN FROM DUAL ";
	public static final String DERBY_PRIMARY_KEY = " (SELECT cast(coalesce(MAX({0}),0) as int) AS IDGEN FROM {1} )";
	public static final String DERBY_PRIMARY_KEY_FINAL = " (SELECT cast(coalesce(cast(replace(cast(max({0}) as char),'{#}','') as int),0) as int) AS IDGEN FROM {1} )";
	public static final String DERBY_MAX_COLUMN_VALUE = "( SELECT cast(coalesce(MAX({0}),0) as int) AS IDGEN from {1} )";
	public static final String DERBY_GET_ALL_TABLES = "select tablename as table_name from sys.systables where schemaid in (select schemaid from sys.sysschemas where schemaname='APP')";
	public static final String DERBY_GET_ALL_COLUMNS = "select tablename as table_name,columnname as column_name from sys.systables t,sys.syscolumns  where tableid=referenceid and schemaid in (select schemaid from sys.sysschemas where schemaname='APP')";
	public static final String DERBY_GET_ALL_COLUMNS_BY_TAB = "select tablename as table_name,columnname as column_name from sys.systables t,sys.syscolumns  where tableid=referenceid and schemaid in (select schemaid from sys.sysschemas where schemaname='APP') and tablename in ({1})";
	
	public static final String DERBY_GET_PK_COLUMN_TYPE = "select distinct(iku.column_name),ic.data_type,ic.character_maximum_length,ic.numeric_precision,ic.datetime_Precision,iku.table_name from information_schema.key_column_usage iku,information_schema.columns ic where iku.table_name=ic.table_name and iku.column_name=ic.column_name and iku.constraint_name=lower('{0}') and iku.table_name=lower('{1}')";
	public static final String DERBY_GET_ALL_CONSTRAINTS_COLUMNS = "select distinct(cc.column_name),cc.data_type,cc.character_maximum_length,cc.numeric_precision,cc.datetime_Precision,cc.table_name,cc.is_nullable from information_schema.columns cc,information_schema.table_constraints tc where (tc.constraint_type in ('PRIMARY KEY','FOREIGN KEY') or cc.is_nullable='NO')";
	public static final String DERBY_GET_SEQUENCE_OF_COLUMNS1 = "select column_name,data_type,character_maximum_length,numeric_precision,datetime_Precision,table_name from information_schema.columns where  table_name=lower('{0}') order by ordinal_position ";
	public static final String DERBY_GET_SEQUENCE_OF_COLUMNS2 = "select column_name,data_type,character_maximum_length,numeric_precision,datetime_Precision,table_name from information_schema.columns where table_name=lower('{0}')  and column_name != '{1}' order by ordinal_position ";
	public static final String DERBY_GET_TABLES_BY_COLUMNS = "select column_name,data_type,character_maximum_length,numeric_precision,datetime_Precision,table_name from information_schema.columns where column_name in ";
	
	public static final String DERBY_GET_TABLE_NAME_BY_FK = "select table_name from information_schema.table_constraints where constraint_name in (select unique_constraint_name from information_schema.referential_constraints where constraint_name in (select constraint_name from information_schema.key_column_usage where table_name=lower('{0}') and column_name=lower('{1}')))";
	public static final String DERBY_GET_SEQUENCE_TABLES = "select table_name from information_schema.table_constraints where CONSTRAINT_NAME in (select unique_constraint_name From information_schema.referential_constraints where constraint_name in (select constraint_name From information_schema.table_constraints where table_name=lower('{0}') and CONSTRAINT_TYPE='FOREIGN KEY'))";
	public static final String DERBY_GET_CONSTRAINTS_OF_TABLES = "select constraint_name,constraint_type From information_schema.table_constraints where table_name=lower('{0}')";
	public static final String DERBY_ONE_TO_ONE_RELATIONS_FIND_TABLES = "select * from (select column_name,table_name,Constraint_name from information_schema.key_column_usage where constraint_name in (select constraint_name from information_schema.table_constraints where constraint_type='UNIQUE')) "
			+ "tt join (select column_name,table_name,Constraint_name from information_schema.key_column_usage where constraint_name in (select constraint_name from information_schema.table_constraints where constraint_type='FOREIGN KEY')) ttt on tt.column_name=ttt.column_name";
	public static final String DERBY_CONSTRAINTS_RELATIONS_TABLES = "select table_name from information_schema.table_constraints where constraint_name in (select rc.unique_constraint_name from information_schema.table_constraints tc,information_schema.referential_constraints rc where rc.constraint_name=tc.constraint_name and tc.constraint_name=lower('{0}') and tc.table_name=lower('{1}'))";
	// Specific case we had included this constant
	public static final String DERBY_GET_NOT_NULL_CONSTRAINTS_OF_TABLES = "select distinct(cc.column_name),cc.data_type,cc.character_maximum_length,cc.numeric_precision,cc.datetime_Precision,cc.table_name,cc.is_nullable from information_schema.columns cc,information_schema.table_constraints tc where cc.table_name=lower('{0}') and (tc.constraint_type in ('UNIQUE','FOREIGN KEY') or cc.is_nullable='NO') and tc.constraint_type !='PRIMERY KEY'";
	public static final String DERBY_GET_COLUMNS_OF_TABS_APART_CONSTRAINTS = "select distinct(column_name),data_type,character_maximum_length,numeric_precision,datetime_Precision,table_name,is_nullable from information_schema.columns where column_name not in(select distinct(cc.column_name) from information_schema.columns cc,information_schema.table_constraints tc where cc.table_name = lower('{0}') and (tc.constraint_type in ('PRIMARY KEY','FOREIGN KEY') or cc.is_nullable='NO')) and table_name=lower('{0}')";

}
