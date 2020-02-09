package com.tesda.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.tesda.dao.DbConnectionDAO;
import com.tesda.dao.TdgOperationsDao;
import com.tesda.model.DO.DataConConnectionsDO;
import com.tesda.model.DTO.DbConnectionsDTO;
import com.tesda.model.DTO.TdgSchemaDTO;
import com.tesda.model.mapper.DbConnectionMapper;
import com.tesda.model.mapper.TdgOperationsMapper;
import com.tesda.service.DbConnectionService;
import com.tesda.util.AppConstant;


@Component
@Service("dbConnectionService")
public class DbConnectionServiceImpl extends TdgBaseServiceImpl implements DbConnectionService
{
	//private static Logger logger = Logger.getLogger(DbConnectionServiceImpl.class);

	@Autowired
	DbConnectionDAO dbConnectionDAO;

	@Autowired
	DbConnectionMapper dbConnectionMapper;
	
	@Autowired
	TdgOperationsDao tdgOperationsDao;
	@Autowired
	TdgOperationsMapper tdgOperationsMapper;

	@Override
	public String testConnection(DbConnectionsDTO dbConnectionsDTO)  {
		try {

			StringBuffer url = new StringBuffer();
			url = dbConnectionMapper.getUrl(dbConnectionsDTO, url);
			
			
			/*if(dbConnectionsDTO.getDbType().equalsIgnoreCase("SqlServer"))
			{*/
				if(dbConnectionsDTO.getAuthenticationType().equalsIgnoreCase("windowsAuthentication"))
				{
					Connection sqlConnection = DriverManager.getConnection(url.toString());
				}
				else{
					Connection oracleCon = DriverManager.getConnection(url.toString(),
							dbConnectionsDTO.getUser(), dbConnectionsDTO.getPass());
					if (null != oracleCon) {
						oracleCon.close();
					}
				}
			//}
			
			
			return AppConstant.SUCCESS_PING_MSG;
		} catch(Exception e){
			e.printStackTrace();
			return AppConstant.PING_FAIL_MSG;
		}
	}

	@Override
	public DbConnectionsDTO saveConnection(DbConnectionsDTO dbConnectionsDTO)
			 {
		try {

			DataConConnectionsDO dataConConnectionsDO = dbConnectionMapper
					.convertDbConnectionsDTOtoDataConConnectionsDO(dbConnectionsDTO);

			EntityManager managerUser = openEntityManager();
			dataConConnectionsDO = dbConnectionDAO
					.saveConnection(dataConConnectionsDO, managerUser);
			closeEntityManager(managerUser);
			dbConnectionsDTO = dbConnectionMapper.convertDataConConnectionsDOtoDbConnectionsDTO(
					dataConConnectionsDO, dbConnectionsDTO);
			return dbConnectionsDTO;
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public DbConnectionsDTO savedConnection(String dataConConnId)  {
		try {
			DbConnectionsDTO dbConnectionsDTO = new DbConnectionsDTO();
			EntityManager managerUser = openEntityManager();
			DataConConnectionsDO dataConConnectionsDO = dbConnectionDAO.savedConnection(
					dataConConnId, managerUser);
			closeEntityManager(managerUser);
			dbConnectionsDTO = dbConnectionMapper.convertDataConConnectionsDOtoDbConnectionsDTO(
					dataConConnectionsDO, dbConnectionsDTO);
			return dbConnectionsDTO;
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean deleteConnection(String conId)  {
		try {

			EntityManager managerUser = openEntityManager();
			boolean sts = dbConnectionDAO.deleteConnection(conId, managerUser);
			closeEntityManager(managerUser);
			return sts;
		} catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Long connectionsDashboardCount(String userId)  {
		try {

			EntityManager managerUser = openEntityManager();
			Long cnt = dbConnectionDAO.connectionsDashboardCount(userId, managerUser);
			closeEntityManager(managerUser);

			return cnt;
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public List<DbConnectionsDTO> connectionsDashboard(int offSet, int recordsperpage, boolean b,
			String userId)  {
		
		try {
			List<DbConnectionsDTO> dbConnectionsDTOs = null;
			EntityManager managerUser = openEntityManager();
			List<DataConConnectionsDO> dataConConnectionsDO = dbConnectionDAO.connectionsDashboard(
					offSet, recordsperpage, b, userId, managerUser);
			closeEntityManager(managerUser);
			dbConnectionsDTOs = dbConnectionMapper.convertDataConConnectionsDOstoDbConnectionsDTOs(
					dataConConnectionsDO, dbConnectionsDTOs);
			
			return dbConnectionsDTOs;
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public List<TdgSchemaDTO> fetchAllTdgSchemaDetails(){
		List<TdgSchemaDTO> listResult = null;
		EntityManager managerentity = null;
		try {
			managerentity = openEntityManager();
			if (managerentity != null) {
				listResult = tdgOperationsMapper.convertTdgSchemaDOToTdgSchemaDTO(tdgOperationsDao
						.fetchSchemaDetailsAll(managerentity));
			}
		} finally {
			if (managerentity != null) {
				closeEntityManager(managerentity);
			}
		}
		return listResult;
	}

}
