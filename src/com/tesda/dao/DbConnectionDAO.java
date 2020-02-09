package com.tesda.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.tesda.model.DO.DataConConnectionsDO;


public interface DbConnectionDAO
{

	public DataConConnectionsDO saveConnection(DataConConnectionsDO dataConConnectionsDO,
			EntityManager managerUser) ;

	public List<DataConConnectionsDO> connectionsDashboard(int offSet, int recordsperpage,
			boolean b, String userId, EntityManager managerUser) ;

	public DataConConnectionsDO savedConnection(String dataConConnId, EntityManager managerUser)
			;

	public Long connectionsDashboardCount(String userId, EntityManager managerUser)
			;

	public boolean deleteConnection(String conId, EntityManager managerUser) ;

}
