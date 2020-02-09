package com.tesda.service;

import java.util.List;

import com.tesda.model.DTO.DbConnectionsDTO;
import com.tesda.model.DTO.TdgSchemaDTO;

public interface DbConnectionService
{

	public String testConnection(DbConnectionsDTO dbConnectionsDTO);

	public DbConnectionsDTO saveConnection(DbConnectionsDTO dbConnectionsDTO);

	public DbConnectionsDTO savedConnection(String dataConConnId);

	public boolean deleteConnection(String conId);

	public Long connectionsDashboardCount(String attribute);

	public List<DbConnectionsDTO> connectionsDashboard(int offSet, int recordsperpage, boolean b,
			String attribute);

	public List<TdgSchemaDTO> fetchAllTdgSchemaDetails();

}
