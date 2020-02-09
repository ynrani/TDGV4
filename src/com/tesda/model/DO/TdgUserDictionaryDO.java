/*
 * Object Name : TdmUserDO.java
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

/**
 * The persistent class for the TDM_USERS database table.
 * 
 */
@Entity
@Table(name = "USER_ACCESS_DICTIONARY")
@NamedQueries({
		@NamedQuery(name = "TdgUserDictionaryDO.findAll", query = "SELECT t FROM TdgUserDictionaryDO t"),
		@NamedQuery(name = "TdgUserDictionaryDO.findByUserId", query = "SELECT t FROM TdgUserDictionaryDO t WHERE t.userId =:userId") })
public class TdgUserDictionaryDO extends BaseDO{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private long id;
	
	@Column(name = "USER_ID")
	private String userId;

	@Column(name = "DICTIONARY_NAME")
	private String dictionaryName;	
	
	@Column(name = "ACTION_BY")
	private String actionby;	
	
	public TdgUserDictionaryDO() {
	}

	public long getId(){
		return id;
	}

	public void setId(long id){
		this.id = id;
	}

	public String getUserId(){
		return userId;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getDictionaryName(){
		return dictionaryName;
	}

	public void setDictionaryName(String dictionaryName){
		this.dictionaryName = dictionaryName;
	}

	public String getActionby(){
		return actionby;
	}

	public void setActionby(String actionby){
		this.actionby = actionby;
	}
	
	
}