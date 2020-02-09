/*
 * Object Name : TdgDictionariesServiceImpl.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		11:09:46 AM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tesda.dao.TdgDictionaryDao;
import com.tesda.dao.TdgOperationsDao;
import com.tesda.dao.TdgTemplateDao;
import com.tesda.model.DO.DataConConnectionsDO;
import com.tesda.model.DTO.DbConnectionsDTO;
import com.tesda.model.DTO.TdgSchemaDTO;
import com.tesda.model.mapper.DbConnectionMapper;
import com.tesda.model.mapper.TdgOperationsMapper;
import com.tesda.service.TdgDictionariesService;

/**
 * @author vkrish14
 *
 */
@Component
@Service("tdgDictionariesService")
@Transactional(propagation = Propagation.REQUIRED)
public class TdgDictionariesServiceImpl extends TdgBaseServiceImpl implements TdgDictionariesService{
	
	@Autowired
	TdgOperationsDao tdgOperationsDao;
	@Autowired
	TdgOperationsMapper tdgOperationsMapper;

	@Autowired
	TdgDictionaryDao tdgDictionaryDao;
	
	@Autowired
	TdgTemplateDao tdgTemplateDao;
	
	@Autowired
	DbConnectionMapper dbConnectionMapper;
	@Override
	public String saveTdgSchemaDetails(TdgSchemaDTO tdgSchemaDTO){
		String strResult = null;
		EntityManager managerentity = null;
		try {
			managerentity = openEntityManager();
			if (managerentity != null) {
				strResult = tdgOperationsDao.saveSchemaDetails(
						tdgOperationsMapper.convertTdgSchemaDTOToTdgSchemaDO(tdgSchemaDTO),
						managerentity);
			}
		} finally {
			if (managerentity != null) {
				closeEntityManager(managerentity);
			}
		}
		return strResult;
	}
	
	
	@Override
	public List<DbConnectionsDTO> getAvailableConnections(){
		EntityManager managerentity = null;
		List<DbConnectionsDTO> returnResult = null;
		try {
			managerentity = openEntityManager();
			if (managerentity != null) {
				List<DataConConnectionsDO> listResult = tdgDictionaryDao
						.getDBConnections(managerentity);
				returnResult = dbConnectionMapper.convertDataConConnectionsDOstoDbConnectionsDTOs(
						listResult, null);
			}
		} finally {
			if (managerentity != null) {
				closeEntityManager(managerentity);
			}
		}
		return returnResult;
	}
	@Override
	public List<String> getAllTabs(String url, String username, String password){
		List<String> lstTables = new ArrayList<String>();
		try {
			lstTables.addAll(tdgTemplateDao.getAllTables(url, username, password));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return lstTables;
	}
	@Override
	public List<String> getColsByTabs(String strUrl, String strName, String strPass,
			List<String> listPassedTabs){
		return tdgTemplateDao.getColsByTabs(strUrl, strName, strPass, listPassedTabs);
	}
	@Override
	public TdgSchemaDTO getSchemaDetails(long lSchemaId){
		EntityManager managerentity = null;
		TdgSchemaDTO dto = null;
		try {
			managerentity = openEntityManager();
			if (managerentity != null) {
				dto = tdgOperationsMapper.convertTdgSchemaDOToTdgSchemaDTO(tdgOperationsDao.fetchSchemaDetailsById(lSchemaId, managerentity).get(0));
			}
		} finally {
			if (managerentity != null) {
				closeEntityManager(managerentity);
			}
		}
		return dto;
	}
}
