/*
 * Object Name : TdgUsersDO.java
 * Modification Block
 * ---------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ---------------------------------------------------------------------
 * 	1.	  vkrish14		Jun 15, 2015			NA             Created
 * ---------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.model.DTO;

import java.util.List;



public class TdgTableAliasDTO extends BaseDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long aliasid;
	private String tabname;
	private String schemaname;
	private String aliasname;
	private String userid;
	private String dbname;
	private List<String> tdgConnections;
	public long getAliasid(){
		return aliasid;
	}
	public void setAliasid(long aliasid){
		this.aliasid = aliasid;
	}
	public String getTabname(){
		return tabname;
	}
	public void setTabname(String tabname){
		this.tabname = tabname;
	}
	public String getSchemaname(){
		return schemaname;
	}
	public void setSchemaname(String schemaname){
		this.schemaname = schemaname;
	}
	public String getAliasname(){
		return aliasname;
	}
	public void setAliasname(String aliasname){
		this.aliasname = aliasname;
	}
	public String getUserid(){
		return userid;
	}
	public void setUserid(String userid){
		this.userid = userid;
	}
	public String getDbname(){
		return dbname;
	}
	public void setDbname(String dbname){
		this.dbname = dbname;
	}
	public List<String> getTdgConnections(){
		return tdgConnections;
	}
	public void setTdgConnections(List<String> tdgConnections){
		this.tdgConnections = tdgConnections;
	}
	
}
