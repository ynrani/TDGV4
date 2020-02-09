package com.tesda.model.mapper;

import java.util.List;

import com.tesda.model.DO.DataConConnectionsDO;
import com.tesda.model.DTO.DbConnectionsDTO;


public interface DbConnectionMapper
{

	public DataConConnectionsDO convertDbConnectionsDTOtoDataConConnectionsDO(
			DbConnectionsDTO dbConnectionsDTO);

	public DbConnectionsDTO convertDataConConnectionsDOtoDbConnectionsDTO(
			DataConConnectionsDO dataConConnectionsDO, DbConnectionsDTO dbConnectionsDTO);

	public StringBuffer getUrl(DbConnectionsDTO dbConnectionsDTO, StringBuffer url);

	public List<DbConnectionsDTO> convertDataConConnectionsDOstoDbConnectionsDTOs(
			List<DataConConnectionsDO> dataConConnectionsDO,
			List<DbConnectionsDTO> dbConnectionsDTOs);
}
