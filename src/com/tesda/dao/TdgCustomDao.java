/*
 * Object Name : TdgCustomDao.java
 * Modification Block
 * ---------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ---------------------------------------------------------------------
 * 	1.	  vkrish14		Jun 15, 2015			NA             Created
 * ---------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.dao;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.persistence.EntityManager;

import org.springframework.jdbc.core.JdbcTemplate;

import com.tesda.model.DTO.TestDataGenerateDTO;

/**
 *
 */
public interface TdgCustomDao{


	/**
	 * This method is used to dump the data into DB
	 * @param jdbcTemplate
	 * @param listResultTabs
	 * @param listRelations
	 * @param mapConditions
	 * @param listResultVal
	 * @param iRequiredRecordCount
	 * @param lstTabsNames
	 * @param listMasterTabs
	 * @param mapTableWithSequence
	 * @param listColumnNames
	 * @param mapInputDepends
	 * @param mapReverseDependents
	 * @param mapDataDictionaryVals
	 *            (all the dictionary values in column_name and its related list of data)
	 * @param isDataConditional
	 *            (data conditional boolean value)
	 * @param listDataDictionaryTabNames
	 * @return
	 * @throws Exception
	 */
	Map<String, Vector<String>> generateQueriesAndDumping(JdbcTemplate jdbcTemplate,
			List<List<List<Object>>> listResultTabs, List<String> listRelations,
			Map<String, Object> mapConditions, List<List<Object>> listResultVal,
			long iRequiredRecordCount, List<String> lstTabsNames, List<String> listMasterTabs,
			Map<String, Map<String, Map<String, String>>> mapTableWithSequence,
			List<String> listColumnNames, Map<String, Vector<String>> mapInputDepends,
			Map<String, String> mapReverseDependents,
			Map<String, List<String>> mapDataDictionaryVals, boolean isDataConditional,
			List<String> listDataDictionaryTabNames, TestDataGenerateDTO testDataGenenarteDTO)
			throws Exception;

	/**
	 * This is used to get all the input data of the form from input and generates the required
	 * input data for database to inject.
	 * @param testDataGenenarteDTO
	 * @param managerentity
	 * @return
	 */
	String generateTestData(TestDataGenerateDTO testDataGenenarteDTO, EntityManager managerentity);
	

	/**
	 * This method is used to create a flat files instead of generate the in respective database
	 * @param testDataGenenarteDTO
	 * @param managerentity
	 * @return
	 */
	Map<String, Map<String, List<Object[]>>> generateFlatTestData(
			TestDataGenerateDTO testDataGenenarteDTO,
			EntityManager managerentity);
}
