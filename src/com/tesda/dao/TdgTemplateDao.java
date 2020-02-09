/*
 * Object Name : TdgTemplateDao.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		9:40:15 AM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.persistence.EntityManager;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author vkrish14
 *
 */
public interface TdgTemplateDao{
	/**
	 * This method is used to get all the table names in sequence based on their relation
	 * @param jdbcTemplate
	 * @param tableName
	 * @return
	 */
	public List<Object> generateSequenceOfTables(JdbcTemplate jdbcTemplate, String tableName,boolean isDuplicateCall);

	/**
	 * This method is used to get all table names
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public List<String> getAllTables(String url, String username, String password);

	/**
	 * This is used to get all the column names from the database
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public List<String> getAllCols(String url, String username, String password);

	/**
	 * This method is used to get all the constraint column names,table names,it's data type and
	 * length
	 * @param url
	 * @param username
	 * @param password
	 * @return
	 */
	public List<Map<String, Object>> getAllConstraintCols(String url, String username,
			String password);

	/**
	 * This method is used to get all the table names based on its related schema
	 * @param jdbcTemplate
	 * @param tableName
	 * @return
	 */
	List<Object> getTables(JdbcTemplate jdbcTemplate, String tableName);

	/**
	 * This method is used to get all the constraint column names,table names,it's data type and
	 * length based on passed column names
	 * @param jdbcTemplate
	 * @param listcolumnsName
	 * @return
	 */
	List<Map<String, Object>> getTableNamesByColsName(JdbcTemplate jdbcTemplate,
			List<String> listcolumnsName);
	

	/**
	 * This method is used to get jdbc template
	 * @param strUrl
	 * @param strUsername
	 * @param strPassword
	 * @return
	 */
	JdbcTemplate getTemplate(String strUrl, String strUsername, String strPassword);

	public List<String> getColsByTabs(String strUrl, String strName, String strPass,
			List<String> listPassedTabs);

	Set<String> getTableNamesFromCols(JdbcTemplate jdbcTemplate, Map<String, Object> mapCols);

	Set<String> getTableNamesFromCols(JdbcTemplate jdbcTemplate, List<String> listCols);

	@SuppressWarnings("rawtypes")
	Vector generatePkValues(JdbcTemplate template, String query,
			Map<String, Map<String, String>> mapSequences, long iRecords, String strTableName);

	List<Map<String, Object>> getSequenceColsByTableName(JdbcTemplate jdbcTemplate,
			String strTBName, String strPKColumnName);

	int getHighestHashValue(List<Object> listTableNamewithHash);

	Set<String> getTableNameInsert(List<Object> listTableNamewithHash, int iHashCount);

	List<Object> containsCheck(List<Object> listObj, String strValue);

	boolean containsAllCheck(List<List<Object>> listObjectFinal, List<Object> listTemp);

	List<List<Object>> chechSublist(List<List<Object>> listObjectFinal, List<Object> listTemp);

	List<List<Object>> toCheckrelations(List<Object> listObject);

	List<Object> toCheckrelations(List<Object> listObject, List<Object> listResult);

	List<Map<String, Object>> getConstraintRelationTable(JdbcTemplate jdbcTemplate,
			String strConstraintName, String strTableName);

	List<Map<String, Object>> getOntToOneTableInformation(JdbcTemplate jdbcTemplate,
			String tableName);

	List<Map<String, Object>> getPkColumnType(JdbcTemplate jdbcTemplate, String constraint_name,
			String tableName);

	List<Map<String, Object>> getNotNullConstraintsOfTable(JdbcTemplate jdbcTemplate,
			String tableName);

	List<Map<String, Object>> getConstraintsOfTable(JdbcTemplate jdbcTemplate, String tableName);

	Map<String, String> getTableNameByFkName(JdbcTemplate jdbcTemplate, String strTBName,
			Map<String, String> mapFkColumns);

	void cleanupDataSource(JdbcTemplate template);

	String getDbType();

	public List<String> getAllTables(JdbcTemplate jdbcTemplate);

	public String getMaxValue(JdbcTemplate jdbcTemplate, String strDbSeq);

	public Map<String, Integer> getTableColumnsInSequence(JdbcTemplate jdbcTemplate,
			String strTableName);

	public List<String> getDateColumns(JdbcTemplate jdbcTemplate, String strTableName);
	
	List<String> fetchAllManualDictionaries(EntityManager managerentity);
	
	public List<Map<String,Object>> getAutoIncrementColumns(JdbcTemplate template,String strTableName);

	Set<String> getComputedColumns(JdbcTemplate jdbcTemplate, String strTableName);
}
