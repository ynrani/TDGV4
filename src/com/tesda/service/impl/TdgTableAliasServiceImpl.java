/*
 * Object Name : TdgTableAliasServiceImpl.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		12:06:54 PM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.service.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tesda.dao.TdgTableAliasDAO;
import com.tesda.model.DTO.TdgTableAliasDTO;
import com.tesda.model.mapper.TdgTableAliasMapper;
import com.tesda.service.TdgTableAliasService;
import com.tesda.util.TdgCentralConstant;

/**
 * @author vkrish14
 *
 */

@Component
@Service("tdgTableAliasService")
@Transactional(propagation = Propagation.REQUIRED)
public class TdgTableAliasServiceImpl extends TdgBaseServiceImpl implements TdgTableAliasService{
	
	@Autowired
	TdgTableAliasDAO tdgTableAliasDAO;
	@Autowired
	TdgTableAliasMapper tdgTableAliasMapper;
	/**
	 * @see com.tesda.service.TdgTableAliasService#getTdgTableAliasRecordsCount()
	 */
	@Override
	public Long getTdgTableAliasRecordsCount(String userid){		
		EntityManager managerUser = null;
		try {
			managerUser = openEntityManager();			
			return tdgTableAliasDAO.searchTableAliasRecordsCount(userid,managerUser);
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
			if(managerUser != null)
				closeEntityManager(managerUser);
		}
	}

	/* (non-Javadoc)
	 * @see com.tesda.service.TdgTableAliasService#getTdgTableAliasRecordsForPagination(int, int, boolean)
	 */
	@Override
	public List<TdgTableAliasDTO> getTdgTableAliasRecordsForPagination(int offSet,
			int recordsperpage, boolean b,String userid){
		List<TdgTableAliasDTO> listResult = null;
		EntityManager managerentity = null;
		try {
			managerentity = openEntityManager();
			if (managerentity != null) {
				listResult = tdgTableAliasMapper.convertTdgTableAliasDOToTdgTableAliasDTO(tdgTableAliasDAO
						.getTdgTableAliasRecordsForPagination(managerentity, offSet,
								recordsperpage, b,userid));
			}
		} finally {
			if (managerentity != null) {
				closeEntityManager(managerentity);
			}
		}
		return listResult;
	}

	@Override
	public void deleteTableAliasById(String aliasid){
		EntityManager managerUser = null;
		try {
			managerUser = openEntityManager();			
			tdgTableAliasDAO.deleteTableAliasById(aliasid,managerUser);
		} catch(Exception e){
			e.printStackTrace();
		}finally{
			if(managerUser != null)
				closeEntityManager(managerUser);
		}
		
	}

	@Override
	public String saveTableAliasData(TdgTableAliasDTO tdgTableAliasDTO){
		EntityManager managerUser = null;
		String status = TdgCentralConstant.FAILED_MESSAGE;
		try {
			managerUser = openEntityManager();			
			status= tdgTableAliasDAO.saveTableAliasData(tdgTableAliasMapper.convertTdgTableAliasDTOToTdgTableAliasDO(tdgTableAliasDTO),managerUser);
		} catch(Exception e){
			e.printStackTrace();
		}finally{
			if(managerUser != null)
				closeEntityManager(managerUser);
		}
		return status;
		
	}

	@Override
	public String testTableAliasConnection(TdgTableAliasDTO tdgTableAliasDTO){
		EntityManager managerUser = null;
		String status = TdgCentralConstant.FAILED_MESSAGE;
		try {
			managerUser = openEntityManager();			
			status= tdgTableAliasDAO.testTableAliasConnection(tdgTableAliasMapper.convertTdgTableAliasDTOToTdgTableAliasDO(tdgTableAliasDTO),managerUser);
		} catch(Exception e){
			e.printStackTrace();
		}finally{
			if(managerUser != null)
				closeEntityManager(managerUser);
		}
		return status;
	}

	@Override
	public TdgTableAliasDTO getTdgTableAliasDetails(String reqaliasid, String username){
		EntityManager managerUser = null;
		TdgTableAliasDTO tdgTableAliasDTO = null;
		try {
			managerUser = openEntityManager();			
			tdgTableAliasDTO= tdgTableAliasMapper.convertTdgTableAliasDOToTdgTableAliasDTO(tdgTableAliasDAO.getTdgTableAliasDetails(reqaliasid,username,managerUser));
		} catch(Exception e){
			e.printStackTrace();
		}finally{
			if(managerUser != null)
				closeEntityManager(managerUser);
		}
		return tdgTableAliasDTO;
	}
}
