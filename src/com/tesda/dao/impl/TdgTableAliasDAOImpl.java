/*
 * Object Name : TdgTableAliasDAOImpl.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		12:36:14 PM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.tesda.dao.TdgTableAliasDAO;
import com.tesda.model.DO.TdgTableAliasDO;
import com.tesda.util.TdgCentralConstant;

/**
 * @author vkrish14
 *
 */
@Component("tdgTableAliasDAO")
public class TdgTableAliasDAOImpl implements TdgTableAliasDAO{
	private static Logger logger = Logger.getLogger(TdgTableAliasDAOImpl.class);
	private static String strClassName = " [ TdgTableAliasDAOImpl ] ";
	/* (non-Javadoc)
	 * @see com.tesda.dao.TdgTableAliasDAO#searchTableAliasRecordsCount(java.lang.String, javax.persistence.EntityManager)
	 */
	@Override
	public Long searchTableAliasRecordsCount(String userid, EntityManager managerUser){
		Long count = 0L;
		if(!StringUtils.isEmpty(userid))
		count = (Long) managerUser.createQuery("SELECT count(*) FROM TdgTableAliasDO p Where p.userid ='" + userid
				+ "'").getSingleResult();
		else
			count = (Long) managerUser.createQuery("SELECT count(*) FROM TdgTableAliasDO p " ).getSingleResult();
		return count;
	}

	/* (non-Javadoc)
	 * @see com.tesda.dao.TdgTableAliasDAO#getTdgTableAliasRecordsForPagination(javax.persistence.EntityManager, int, int, boolean)
	 */
	@Override
	public List<TdgTableAliasDO> getTdgTableAliasRecordsForPagination(EntityManager managerentity,
			int offSet, int recordsperpage, boolean b,String userid){
		List<TdgTableAliasDO> listTableAlias;
		if(!StringUtils.isEmpty(userid))
		listTableAlias= managerentity
				.createQuery(
						"SELECT p FROM TdgTableAliasDO p where p.userid = '" + userid + "' ",TdgTableAliasDO.class)
				.setFirstResult(offSet).setMaxResults(recordsperpage).getResultList();
		else
			listTableAlias= managerentity
			.createQuery(
					"SELECT p FROM TdgTableAliasDO p ",TdgTableAliasDO.class)
			.setFirstResult(offSet).setMaxResults(recordsperpage).getResultList();
		return listTableAlias;
	}

	@Override
	public void deleteTableAliasById(String aliasid, EntityManager managerUser){
		managerUser.getTransaction().begin();
		Query q1 = managerUser
				.createNamedQuery("TdgTableAliasDO.deleteByAliasId").setParameter("aliasid", Long.parseLong(aliasid));
		q1.executeUpdate();
		managerUser.getTransaction().commit();
		
	}

	@Override
	public String saveTableAliasData(TdgTableAliasDO tdgTableAliasDO,
			EntityManager managerUser){
		String strMethodName = " [ saveTableAliasData() ] ";
		logger.info(strClassName + strMethodName + " inside saveTableAliasData method");
		String strMessage = TdgCentralConstant.FAILED_MESSAGE;
		try {
			if(tdgTableAliasDO.getAliasid() != 0){
				tdgTableAliasDO.setAliasid(tdgTableAliasDO.getAliasid());
			}else{
			Long no = (Long) managerUser.createQuery(
					"SELECT NVL(MAX(p.aliasid),0)  from TdgTableAliasDO p").getSingleResult();
			long nooId = (null != no ? no : 0) + 1;
			tdgTableAliasDO.setAliasid(nooId);
			}
			managerUser.getTransaction().begin();
			
			managerUser.merge(tdgTableAliasDO);
			logger.info(strClassName + strMethodName + " merged the table alias details in server");
			managerUser.getTransaction().commit();
			strMessage = TdgCentralConstant.SUCCESS_MESSAGE;
		} catch (Exception e) {
			if (managerUser != null) {
				managerUser.getTransaction().rollback();
			}
			logger.error(strClassName + strMethodName
					+ " got error while save the table alias details  ", e);
		}
		logger.info(strClassName + strMethodName + " return from saveSchemaDetails method");
		return strMessage;
	}

	@Override
	public String testTableAliasConnection(TdgTableAliasDO tdgTableAliasDO,EntityManager managerUser){

		String strMethodName = " [ testTableAliasConnection() ] ";
		logger.info(strClassName + strMethodName + " inside testTableAliasConnection method");
		String strMessage = TdgCentralConstant.FAILED_MESSAGE;
		try {
			List<TdgTableAliasDO> dos = managerUser.createNamedQuery("TdgTableAliasDO.findByAliasTabNameDictionary",TdgTableAliasDO.class).setParameter("schemaname", tdgTableAliasDO.getSchemaname())
					.setParameter("aliasname", tdgTableAliasDO.getAliasname()).setParameter("tabname", tdgTableAliasDO.getTabname()).getResultList();
			if(dos != null && !dos.isEmpty())
				strMessage= TdgCentralConstant.FAILED_MESSAGE;
			else
			strMessage = TdgCentralConstant.SUCCESS_MESSAGE;
		} catch (Exception e) {
			logger.error(strClassName + strMethodName
					+ " got error while testing the table alias details  ", e);
		}
		logger.info(strClassName + strMethodName + " return from testTableAliasConnection method");
		return strMessage;
	
	}

	@Override
	public TdgTableAliasDO getTdgTableAliasDetails(String reqaliasid, String username,EntityManager managerUser){

		String strMethodName = " [ getTdgTableAliasDetails() ] ";
		logger.info(strClassName + strMethodName + " inside getTdgTableAliasDetails method");
		if(StringUtils.isNotEmpty(username)){
			return managerUser.createNamedQuery("TdgTableAliasDO.findByIdUserid",TdgTableAliasDO.class).setParameter("userid", username).setParameter("aliasid", Long.parseLong(reqaliasid)).getSingleResult();
		}else{
			return managerUser.createNamedQuery("TdgTableAliasDO.findById",TdgTableAliasDO.class).setParameter("aliasid", Long.parseLong(reqaliasid)).getSingleResult();
		} 
	}
}
