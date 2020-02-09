/*
 * Object Name : TdgDictionaryDao.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		2:01:25 PM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.tesda.model.DO.DataConConnectionsDO;

/**
 * @author vkrish14
 *
 */
public interface TdgDictionaryDao{


	List<DataConConnectionsDO> getDBConnections(EntityManager entityManager);

	List<String> checkDataConnections(String userId, String url, String username, String password,
			EntityManager entityManager);
}
