/*
 * Object Name : TdgTableAliasDAO.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		12:09:28 PM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.tesda.model.DO.TdgTableAliasDO;

/**
 * @author vkrish14
 *
 */
public interface TdgTableAliasDAO{

	Long searchTableAliasRecordsCount(String userid, EntityManager managerUser);

	List<TdgTableAliasDO> getTdgTableAliasRecordsForPagination(EntityManager managerentity,
			int offSet, int recordsperpage, boolean b, String userid);

	void deleteTableAliasById(String aliasid, EntityManager managerUser);

	String saveTableAliasData(TdgTableAliasDO convertTdgTableAliasDTOToTdgTableAliasDO,
			EntityManager managerUser);

	String testTableAliasConnection(TdgTableAliasDO convertTdgTableAliasDTOToTdgTableAliasDO,
			EntityManager managerUser);

	TdgTableAliasDO getTdgTableAliasDetails(String reqaliasid, String username,
			EntityManager managerUser);
}
