/*
 * Object Name : TdgDictionaryDaoImpl.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		2:01:53 PM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.tesda.dao.TdgDictionaryDao;
import com.tesda.model.DO.DataConConnectionsDO;
import com.tesda.util.AppConstant;

/**
 * @author vkrish14
 *
 */
@Component("tdgDictionaryDao")
public class TdgDictionaryDaoImpl implements TdgDictionaryDao{

	@Override
	public List<DataConConnectionsDO> getDBConnections(EntityManager entityManager){
		List<DataConConnectionsDO> listResult = entityManager.createNamedQuery("DataConConnectionsDO.findAll",DataConConnectionsDO.class).getResultList();
		// TODO Auto-generated method stub
		return listResult;
	}
	
	@Override
	public List<String> checkDataConnections(String userId,String url,String username,String password,EntityManager entityManager){
		//List<String> listResult = e
		/*List<String> lstUrls = new ArrayList<String>();
		List<String> lstUserNames = new ArrayList<String>();
		List<String> lstPasswords = new ArrayList<String>();*/
		List<DataConConnectionsDO> listConnections= getDBConnections(entityManager);
		List<String> lstResult = new ArrayList<String>();
		/*for(DataConConnectionsDO dos: listConnections){
			lstResult.add(getUrl(dos));
		}*/
		if(url.contains("#")){
			String[] strSplitUrls = url.split("#");
			String[] strSplitUserNames = username.split("#");
			String[] strSplitPass = password.split("#");
			entityManager.getTransaction().begin();
			for(int i=0;i<strSplitUrls.length;i++){
				for(DataConConnectionsDO dos: listConnections){
					if(getUrl(dos).equals(strSplitUrls[i]+"#"+strSplitUserNames[i]+"#"+strSplitPass[i])){
						lstResult.add(dos.getDisplayName());
						break;
					}
					//lstResult.add(getUrl(dos));
				}
				if(lstResult.size() != i+1){
					DataConConnectionsDO dataConConnectionsDO = new DataConConnectionsDO();
					if(strSplitUrls[i].contains("oracle")){
					dataConConnectionsDO.setDbType("Oracle");
					dataConConnectionsDO.setHostName(strSplitUrls[i].replaceAll(AppConstant.ORA_URL, "").substring(0,strSplitUrls[i].replaceAll(AppConstant.ORA_URL, "").indexOf(":")));
					dataConConnectionsDO.setPortNo(Long.parseLong(strSplitUrls[i].replaceAll(AppConstant.ORA_URL+dataConConnectionsDO.getHostName()+":","").substring(0,strSplitUrls[i].replaceAll(AppConstant.ORA_URL+dataConConnectionsDO.getHostName()+":","").indexOf(":"))));
					dataConConnectionsDO.setSid(strSplitUrls[i].replaceAll(AppConstant.ORA_URL+dataConConnectionsDO.getHostName()+":"+dataConConnectionsDO.getPortNo()+":", "").trim());
					
					}
					else if(strSplitUrls[i].contains("sqlServer")||strSplitUrls[i].contains("sqlserver")){
						dataConConnectionsDO.setDbType("SqlServer");
						dataConConnectionsDO.setHostName(strSplitUrls[i].replaceAll(AppConstant.SQL_SERVER_URL, "").substring(0,strSplitUrls[i].replaceAll(AppConstant.SQL_SERVER_URL, "").indexOf(":")));
						
						try{
							dataConConnectionsDO.setPortNo(Long.parseLong(strSplitUrls[i].replaceAll(AppConstant.SQL_SERVER_URL+dataConConnectionsDO.getHostName()+":","").substring(0,url.replaceAll(AppConstant.SQL_SERVER_URL+dataConConnectionsDO.getHostName()+":","").indexOf(":"))));
							}catch(Exception e){
								dataConConnectionsDO.setPortNo(Long.parseLong(strSplitUrls[i].replaceAll(AppConstant.SQL_SERVER_URL+dataConConnectionsDO.getHostName()+":","").substring(0,url.replaceAll(AppConstant.SQL_SERVER_URL+dataConConnectionsDO.getHostName()+":","").indexOf(";"))));
							}
						dataConConnectionsDO.setSid(strSplitUrls[i].replaceAll(AppConstant.SQL_SERVER_URL+dataConConnectionsDO.getHostName()+":"+dataConConnectionsDO.getPortNo()+";databaseName=", "").substring(0, strSplitUrls[i].replaceAll(AppConstant.SQL_SERVER_URL+dataConConnectionsDO.getHostName()+":"+dataConConnectionsDO.getPortNo()+";databaseName=", "").indexOf(";")));
					}
					else if(strSplitUrls[i].contains("db2")){
						dataConConnectionsDO.setDbType("DB2");
						dataConConnectionsDO.setHostName(strSplitUrls[i].replaceAll(AppConstant.DB2_URL, "").substring(0,strSplitUrls[i].replaceAll(AppConstant.DB2_URL, "").indexOf(":")));
						dataConConnectionsDO.setPortNo(Long.parseLong(strSplitUrls[i].replaceAll(AppConstant.DB2_URL+dataConConnectionsDO.getHostName()+":","").substring(0,strSplitUrls[i].replaceAll(AppConstant.DB2_URL+dataConConnectionsDO.getHostName()+":","").indexOf("/"))));
						dataConConnectionsDO.setSid(strSplitUrls[i].substring(strSplitUrls[i].indexOf("=")+1,strSplitUrls[i].length()));
					}
					else if(strSplitUrls[i].contains("mysql")){
						dataConConnectionsDO.setDbType("MySql");
						dataConConnectionsDO.setHostName(strSplitUrls[i].replaceAll(AppConstant.MYSQL_URL, "").substring(0,strSplitUrls[i].replaceAll(AppConstant.MYSQL_URL, "").indexOf(":")));
						dataConConnectionsDO.setPortNo(Long.parseLong(strSplitUrls[i].replaceAll(AppConstant.MYSQL_URL+dataConConnectionsDO.getHostName()+":","").substring(0,strSplitUrls[i].replaceAll(AppConstant.MYSQL_URL+dataConConnectionsDO.getHostName()+":","").indexOf("/"))));
						dataConConnectionsDO.setSid(strSplitUrls[i].substring(strSplitUrls[i].indexOf("/")+1));
					}
					/*dataConConnectionsDO.setHostName(dbConnectionsDTO.getHostName());
					dataConConnectionsDO.setPortNo(Long.parseLong(dbConnectionsDTO.getPort()));
					dataConConnectionsDO.setSid(dbConnectionsDTO.getSid());
					dataConConnectionsDO.setUserName(dbConnectionsDTO.getUser());
					dataConConnectionsDO.setPassWord(dbConnectionsDTO.getPass());*/
					dataConConnectionsDO.setActive("Y");
					dataConConnectionsDO.setUserName(strSplitUserNames[i]);
					dataConConnectionsDO.setPassWord(strSplitPass[i]);
					dataConConnectionsDO.setActionBy(userId);
					dataConConnectionsDO.setActionDt(new Timestamp(new Date().getTime()));
					dataConConnectionsDO.setDisplayName(dataConConnectionsDO.getHostName()+dataConConnectionsDO.getUserName());
					
					entityManager.persist(dataConConnectionsDO);
					lstResult.add(dataConConnectionsDO.getDisplayName());
				}
				/*if(lstResult.contains(strSplitUrls[i]+"#"+strSplitUserNames[i]+"#"+strSplitPass[i])){
					
				}*/
			}
			entityManager.getTransaction().commit();
		}else{
			for(DataConConnectionsDO dos: listConnections){
				if(getUrl(dos).equals(url+"#"+username+"#"+password)){
					lstResult.add(dos.getDisplayName());
					break;
				}
				//lstResult.add(getUrl(dos));
			}
			if(lstResult.size() == 0){
				DataConConnectionsDO dataConConnectionsDO = new DataConConnectionsDO();
				entityManager.getTransaction().begin();
				if(url.contains("oracle")){
				dataConConnectionsDO.setDbType("Oracle");
				dataConConnectionsDO.setHostName(url.replaceAll(AppConstant.ORA_URL, "").substring(0,url.replaceAll(AppConstant.ORA_URL, "").indexOf(":")));
				dataConConnectionsDO.setPortNo(Long.parseLong(url.replaceAll(AppConstant.ORA_URL+dataConConnectionsDO.getHostName()+":","").substring(0,url.replaceAll(AppConstant.ORA_URL+dataConConnectionsDO.getHostName()+":","").indexOf(":"))));
				dataConConnectionsDO.setSid(url.replaceAll(AppConstant.ORA_URL+dataConConnectionsDO.getHostName()+":"+dataConConnectionsDO.getPortNo()+":", "").trim());
				
				}
				else if(url.contains("sqlserver")){
					dataConConnectionsDO.setDbType("SqlServer");
					dataConConnectionsDO.setHostName(url.replaceAll(AppConstant.SQL_SERVER_URL, "").substring(0,url.replaceAll(AppConstant.SQL_SERVER_URL, "").indexOf(":")));
					if(username.contains("wa"))
					{
						dataConConnectionsDO.setAuthType("windowsAuthentication");
					}
					else
						dataConConnectionsDO.setAuthType("sqlAuthentication");
					try{
					dataConConnectionsDO.setPortNo(Long.parseLong(url.replaceAll(AppConstant.SQL_SERVER_URL+dataConConnectionsDO.getHostName()+":","").substring(0,url.replaceAll(AppConstant.SQL_SERVER_URL+dataConConnectionsDO.getHostName()+":","").indexOf(":"))));
					}catch(Exception e){
						dataConConnectionsDO.setPortNo(Long.parseLong(url.replaceAll(AppConstant.SQL_SERVER_URL+dataConConnectionsDO.getHostName()+":","").substring(0,url.replaceAll(AppConstant.SQL_SERVER_URL+dataConConnectionsDO.getHostName()+":","").indexOf(";"))));
					}
					dataConConnectionsDO.setSid(url.replaceAll(AppConstant.SQL_SERVER_URL+dataConConnectionsDO.getHostName()+":"+dataConConnectionsDO.getPortNo()+";databaseName=", "").substring(0, url.replaceAll(AppConstant.SQL_SERVER_URL+dataConConnectionsDO.getHostName()+":"+dataConConnectionsDO.getPortNo()+";databaseName=", "").indexOf(";")));
				}
				else if(url.contains("db2")){
					dataConConnectionsDO.setDbType("DB2");
					dataConConnectionsDO.setHostName(url.replaceAll(AppConstant.DB2_URL, "").substring(0,url.replaceAll(AppConstant.DB2_URL, "").indexOf(":")));
					dataConConnectionsDO.setPortNo(Long.parseLong(url.replaceAll(AppConstant.DB2_URL+dataConConnectionsDO.getHostName()+":","").substring(0,url.replaceAll(AppConstant.DB2_URL+dataConConnectionsDO.getHostName()+":","").indexOf("/"))));
					dataConConnectionsDO.setSid(url.substring(url.indexOf("=")+1,url.length()));
				}
				else if(url.contains("mysql")){
					dataConConnectionsDO.setDbType("MySql");
					dataConConnectionsDO.setHostName(url.replaceAll(AppConstant.MYSQL_URL, "").substring(0,url.replaceAll(AppConstant.MYSQL_URL, "").indexOf(":")));
					dataConConnectionsDO.setPortNo(Long.parseLong(url.replaceAll(AppConstant.MYSQL_URL+dataConConnectionsDO.getHostName()+":","").substring(0,url.replaceAll(AppConstant.MYSQL_URL+dataConConnectionsDO.getHostName()+":","").indexOf("/"))));
					dataConConnectionsDO.setSid(url.substring(url.indexOf("/")+1));
				}
				/*dataConConnectionsDO.setHostName(dbConnectionsDTO.getHostName());
				dataConConnectionsDO.setPortNo(Long.parseLong(dbConnectionsDTO.getPort()));
				dataConConnectionsDO.setSid(dbConnectionsDTO.getSid());
				dataConConnectionsDO.setUserName(dbConnectionsDTO.getUser());
				dataConConnectionsDO.setPassWord(dbConnectionsDTO.getPass());*/
				dataConConnectionsDO.setActive("Y");
				dataConConnectionsDO.setUserName(username);
				dataConConnectionsDO.setPassWord(password);
				dataConConnectionsDO.setActionBy(userId);
				dataConConnectionsDO.setActionDt(new Timestamp(new Date().getTime()));
				dataConConnectionsDO.setDisplayName(dataConConnectionsDO.getHostName()+dataConConnectionsDO.getUserName());
				dataConConnectionsDO.setAuthType("Authentication");
				entityManager.persist(dataConConnectionsDO);
				lstResult.add(dataConConnectionsDO.getDisplayName());
			//}
			/*if(lstResult.contains(strSplitUrls[i]+"#"+strSplitUserNames[i]+"#"+strSplitPass[i])){
				
			}*/
				entityManager.getTransaction().commit();
		}
		
		}
		return lstResult;
	}
	
	
	public String getUrl(DataConConnectionsDO dbConnectionsDTO){
		StringBuffer url = new StringBuffer();
		try {
			if (null != dbConnectionsDTO) {
				// build url start
				if (StringUtils.isNotEmpty(dbConnectionsDTO.getDbType())) {
					if (dbConnectionsDTO.getDbType().equalsIgnoreCase("Oracle")) {
						// driver class
						//Class.forName(AppConstant.ORA_DRIVER);
						// driver url
						url.append(AppConstant.ORA_URL);
						// Host name
						url.append(dbConnectionsDTO.getHostName());
						// port number
						url.append(':');
						url.append(dbConnectionsDTO.getPortNo());
						// sid/service/db name
						url.append(':');
						url.append(dbConnectionsDTO.getSid());
					} else if (dbConnectionsDTO.getDbType().equalsIgnoreCase("SqlServer")) {
						// driver class
						//Class.forName(AppConstant.SQL_SERVER_DRIVER);
						// driver url
						url.append(AppConstant.SQL_SERVER_URL);
						// Host name
						url.append(dbConnectionsDTO.getHostName());
						// port number
						url.append(':');
						url.append(dbConnectionsDTO.getPortNo());
						// sid/service/db name
						url.append(";databaseName=");
						url.append(dbConnectionsDTO.getSid());
						url.append(";");
					} else if (dbConnectionsDTO.getDbType().equalsIgnoreCase("MySql")) {
						// driver class
						//Class.forName(AppConstant.MYSQL_DRIVER);
						// driver url
						url.append(AppConstant.MYSQL_URL);
						// Host name
						url.append(dbConnectionsDTO.getHostName());
						// port number
						url.append(':');
						url.append(dbConnectionsDTO.getPortNo());
						// sid/service/db name
						url.append('/');
						url.append(dbConnectionsDTO.getSid());
					} else if (dbConnectionsDTO.getDbType().equalsIgnoreCase("DB2")) {
						// driver class
						//Class.forName(AppConstant.DB2_DRIVER);
						// driver url
						// jdbc:db2://<hostname>:50000/SAMPLE:currentSchema=USRES;
						url.append(AppConstant.DB2_URL);
						// Host name
						url.append(dbConnectionsDTO.getHostName());
						// port number
						url.append(':');
						url.append(dbConnectionsDTO.getPortNo());
						// sid/service/db name
						//url.append("/SAMPLE:currentSchema=");
						url.append(dbConnectionsDTO.getSid());
					}
				}// build url end
			}
			return url.toString()+"#"+dbConnectionsDTO.getUserName()+"#"+dbConnectionsDTO.getPassWord();
		} catch (Exception otherEx) {
			// releaseEntityMgrForRollback(entityManager);
			otherEx.printStackTrace();
			return null;
		}
	}
}
