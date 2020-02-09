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
package com.tesda.model.DO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "TDG_TABLE_ALIAS")
@NamedQueries({
		@NamedQuery(name = "TdgTableAliasDO.findAll", query = "SELECT t FROM TdgTableAliasDO t"),
		@NamedQuery(name = "TdgTableAliasDO.findBySchemaName", query = "SELECT t FROM TdgTableAliasDO t WHERE t.schemaname=:schemaname"),
		@NamedQuery(name = "TdgTableAliasDO.findByIdUserid", query = "SELECT t FROM TdgTableAliasDO t WHERE t.schemaname=:schemaname AND t.userid=:userid AND t.aliasid=:aliasid" ),
		@NamedQuery(name = "TdgTableAliasDO.findByAliasTabNameDictionary", query = "SELECT t FROM TdgTableAliasDO t WHERE t.schemaname=:schemaname AND t.aliasname=:aliasname AND t.tabname=:tabname"),
		@NamedQuery(name = "TdgTableAliasDO.deleteByAliasId", query = "DELETE FROM TdgTableAliasDO t WHERE t.aliasid=:aliasid"),
		@NamedQuery(name = "TdgTableAliasDO.findById", query = "SELECT t FROM TdgTableAliasDO t WHERE t.aliasid=:aliasid") })
public class TdgTableAliasDO extends BaseDO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ALIAS_ID")
	private long aliasid;
	@Column(name = "TABNAME")
	private String tabname;
	@Column(name = "SCHEMA_NAME")
	private String schemaname;
	@Column(name = "ALIAS_NAME")
	private String aliasname;
	private String userid;
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
	
}
