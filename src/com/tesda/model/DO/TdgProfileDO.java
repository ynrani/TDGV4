package com.tesda.model.DO;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the TDG_PROFILES database table.
 * 
 */
@Entity
@Table(name="TDG_PROFILES")
@NamedQuery(name="TdgProfileDO.findAll", query="SELECT t FROM TdgProfileDO t ORDER BY t.profileid DESC")
public class TdgProfileDO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long profileid;

	private String actionby;

	private String comments;

	private String inputdata;

	@Column(name="NEXT_RUN_DATE")
	private String nextRunDate;

	@Column(name="PROFILE_NAME")
	private String profileName;

	private long schemaid;

	public TdgProfileDO() {
	}

	public long getProfileid() {
		return this.profileid;
	}

	public void setProfileid(long profileid) {
		this.profileid = profileid;
	}

	public String getActionby() {
		return this.actionby;
	}

	public void setActionby(String actionby) {
		this.actionby = actionby;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getInputdata() {
		return this.inputdata;
	}

	public void setInputdata(String inputdata) {
		this.inputdata = inputdata;
	}

	public String getNextRunDate() {
		return this.nextRunDate;
	}

	public void setNextRunDate(String nextRunDate) {
		this.nextRunDate = nextRunDate;
	}

	public String getProfileName() {
		return this.profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public long getSchemaid() {
		return this.schemaid;
	}

	public void setSchemaid(long schemaid) {
		this.schemaid = schemaid;
	}

}