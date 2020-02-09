package com.tesda.model.mapper.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.picketbox.util.StringUtil;
import org.springframework.stereotype.Service;

import com.tesda.model.DO.DataConConnectionsDO;
import com.tesda.model.DTO.DbConnectionsDTO;
import com.tesda.model.mapper.DbConnectionMapper;
import com.tesda.util.AppConstant;
import com.tesda.util.TdgCentralConstant;


@Service("dbConnectionMapper")
public class DbConnectionMapperImpl implements DbConnectionMapper
{
	//private static Logger logger = Logger.getLogger(DbConnectionMapperImpl.class);

	@Override
	public DataConConnectionsDO convertDbConnectionsDTOtoDataConConnectionsDO(
			DbConnectionsDTO dbConnectionsDTO) {
		DataConConnectionsDO dataConConnectionsDO = null;
		if (null != dbConnectionsDTO) {
			dataConConnectionsDO = new DataConConnectionsDO();
			dataConConnectionsDO.setDbType(dbConnectionsDTO.getDbType());
			dataConConnectionsDO.setHostName(dbConnectionsDTO.getHostName());
			dataConConnectionsDO.setPortNo(Long.parseLong(dbConnectionsDTO.getPort()));
			dataConConnectionsDO.setSid(dbConnectionsDTO.getSid());
			dataConConnectionsDO.setUserName(dbConnectionsDTO.getUser());
			dataConConnectionsDO.setPassWord(dbConnectionsDTO.getPass());
			dataConConnectionsDO.setActive("Y");
			dataConConnectionsDO.setActionBy(dbConnectionsDTO.getUserId());
			dataConConnectionsDO.setActionDt(new Timestamp(new Date().getTime()));
			dataConConnectionsDO.setDisplayName(dbConnectionsDTO.getDisplayName());
			dataConConnectionsDO.setAuthType(dbConnectionsDTO.getAuthenticationType());
			if (StringUtil.isNotNull(dbConnectionsDTO.getConId())) {
				dataConConnectionsDO.setDataConConnId(Long.parseLong(dbConnectionsDTO.getConId()));
			}
		}
		return dataConConnectionsDO;
	}

	@Override
	public DbConnectionsDTO convertDataConConnectionsDOtoDbConnectionsDTO(
			DataConConnectionsDO dataConConnectionsDO, DbConnectionsDTO dbConnectionsDTO) {
		if (null != dataConConnectionsDO) {
			dbConnectionsDTO.setDbType(dataConConnectionsDO.getDbType());
			dbConnectionsDTO.setHostName(dataConConnectionsDO.getHostName());
			dbConnectionsDTO.setPort(String.valueOf(dataConConnectionsDO.getPortNo()));
			if("Oracle".equalsIgnoreCase(dataConConnectionsDO.getDbType())){
				dbConnectionsDTO.setSid(dataConConnectionsDO.getSid());
			}else{
				if(TdgCentralConstant.DB_TYPE_DB2.equalsIgnoreCase(dataConConnectionsDO.getDbType())){
					//jdbc:db2://DIN51002964:50000/SAMPLE:currentSchema=USRES;
					if(!dataConConnectionsDO.getSid().contains(":currentSchema=")){
						dbConnectionsDTO.setSid(dataConConnectionsDO.getSid());
					}else{
					String strSplit[] = dataConConnectionsDO.getSid().split(":currentSchema=");
					dbConnectionsDTO.setSid(strSplit[0]);
					dbConnectionsDTO.setSchemaname(strSplit[1].substring(0, strSplit[1].length()-1));
					//System.out.println(strSplit);
					}
				}else if(TdgCentralConstant.DB_TYPE_MYSQL.equalsIgnoreCase(dataConConnectionsDO.getDbType())){
					if(!dataConConnectionsDO.getSid().contains("/")){
						dbConnectionsDTO.setSid(dataConConnectionsDO.getSid());
					}else{
						String strSplit[] = dataConConnectionsDO.getSid().split("/");
						dbConnectionsDTO.setSid(strSplit[0]);
						dbConnectionsDTO.setSchemaname(strSplit[1]);
					}
					//jdbc:mysql://IN-PNQ-COE07:3306/zurichdb
				}else if(TdgCentralConstant.DB_TYPE_SQLSERVER.equalsIgnoreCase(dataConConnectionsDO.getDbType())){
					//jdbc:jtds:sqlserver://IN-PNQ-COE13:1433;databaseName=PC_GUIDEWIRE;
					if(!dataConConnectionsDO.getSid().contains(";databaseName=")){
						dbConnectionsDTO.setSid(dataConConnectionsDO.getSid());
					}else{
						String strSplit[] = dataConConnectionsDO.getSid().split(";databaseName=");
						dbConnectionsDTO.setSid(strSplit[0]);
						dbConnectionsDTO.setSchemaname(strSplit[1].substring(0, strSplit[1].length()-1));
					}
				}
			}
			
			dbConnectionsDTO.setUser(dataConConnectionsDO.getUserName());
			dbConnectionsDTO.setPass(dataConConnectionsDO.getPassWord());
			dbConnectionsDTO.setUserId(dataConConnectionsDO.getActionBy());
			dbConnectionsDTO.setConId(String.valueOf(dataConConnectionsDO.getDataConConnId()));
			dbConnectionsDTO.setDisplayName(dataConConnectionsDO.getDisplayName());
			dbConnectionsDTO.setAuthenticationType(dataConConnectionsDO.getAuthType());
			if (dataConConnectionsDO.getActive().equalsIgnoreCase("Y")) {
				dbConnectionsDTO.setActive("Yes");
			}
		}
		return dbConnectionsDTO;
	}
	
	

	@Override
	public List<DbConnectionsDTO> convertDataConConnectionsDOstoDbConnectionsDTOs(
			List<DataConConnectionsDO> dataConConnectionsDOs,
			List<DbConnectionsDTO> dbConnectionsDTOs) {
		DbConnectionsDTO dbConnectionsDTO = null;
		if (null != dataConConnectionsDOs) {
			if (null == dbConnectionsDTOs) {
				dbConnectionsDTOs = new ArrayList<DbConnectionsDTO>();
			}
			for (DataConConnectionsDO dataConConnectionsDO : dataConConnectionsDOs) {
				dbConnectionsDTO = new DbConnectionsDTO();
				dbConnectionsDTO = convertDataConConnectionsDOtoDbConnectionsDTO(
						dataConConnectionsDO, dbConnectionsDTO);
				dbConnectionsDTOs.add(dbConnectionsDTO);
			}
		}
		return dbConnectionsDTOs;
	}

	@Override
	public StringBuffer getUrl(DbConnectionsDTO dbConnectionsDTO, StringBuffer url)
			 {

		try {
			if (null != dbConnectionsDTO) {
				// build url start
				if (StringUtils.isNotEmpty(dbConnectionsDTO.getDbType())) {
					if (dbConnectionsDTO.getDbType().equalsIgnoreCase("Oracle")) {
						// driver class
						Class.forName(AppConstant.ORA_DRIVER);
						// driver url
						url.append(AppConstant.ORA_URL);
						// Host name
						url.append(dbConnectionsDTO.getHostName());
						// port number
						url.append(':');
						url.append(dbConnectionsDTO.getPort());
						// sid/service/db name
						url.append(':');
						url.append(dbConnectionsDTO.getSid());
					} else if (dbConnectionsDTO.getDbType().equalsIgnoreCase("SqlServer")) {
						//in case of sql authentication
					
						// driver class
						Class.forName(AppConstant.SQL_SERVER_DRIVER);
						// driver url
						url.append(AppConstant.SQL_SERVER_URL);
						// Host name
						url.append(dbConnectionsDTO.getHostName());
						// port number
						url.append(':');
						url.append(dbConnectionsDTO.getPort());
						// sid/service/db name
						url.append(";databaseName=");
						url.append(dbConnectionsDTO.getSid());
						url.append(";");
						System.out.println("sql server url"+url);
						 	//sql auth till here
					
						if(dbConnectionsDTO.getAuthenticationType().equalsIgnoreCase("windowsAuthentication")){
							
							System.out.println("before append url"+url);
							//jdbc:sqlserver://MyServer:1433;databaseName=MyDBName;integratedSecurity=true;
							//jdbc:sqlserver://[DB_URL]:[DB_PORT];databaseName=[DB_NAME];integratedSecurity=true;
							
							
						url.append("integratedSecurity=true; SSPI=true;");	
						
						System.out.println("windowsAuthentication url"+url);
						}
						
						
					} else if (dbConnectionsDTO.getDbType().equalsIgnoreCase("MySql")) {
						// driver class
						Class.forName(AppConstant.MYSQL_DRIVER);
						// driver url
						url.append(AppConstant.MYSQL_URL);
						// Host name
						url.append(dbConnectionsDTO.getHostName());
						// port number
						url.append(':');
						url.append(dbConnectionsDTO.getPort());
						// sid/service/db name
						url.append('/');
						url.append(dbConnectionsDTO.getSid());
					} else if (dbConnectionsDTO.getDbType().equalsIgnoreCase("DB2")) {
						// driver class
						Class.forName(AppConstant.DB2_DRIVER);
						// driver url
						// jdbc:db2://<hostname>:50000/SAMPLE:currentSchema=USRES;
						url.append(AppConstant.DB2_URL);
						// Host name
						url.append(dbConnectionsDTO.getHostName());
						// port number
						url.append(':');
						url.append(dbConnectionsDTO.getPort());
						// sid/service/db name
						//url.append("/SAMPLE:currentSchema=");
						url.append("/"+dbConnectionsDTO.getSid());
						url.append(":currentSchema="+dbConnectionsDTO.getSchemaname()+";");
						//url.append(";");
					}
				}// build url end
			}
			return url;
		} catch (Exception otherEx) {
			// releaseEntityMgrForRollback(entityManager);
		otherEx.printStackTrace();
		return null;
		}
	}

}
