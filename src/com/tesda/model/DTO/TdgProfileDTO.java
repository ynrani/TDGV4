package com.tesda.model.DTO;




public class TdgProfileDTO extends AbstractBaseDTO{
	private static final long serialVersionUID = 1L;

	private long profileid;

	private String actionby;

	private String comments;

	private String inputdata;

	private String nextRunDate;

	private String profileName;

	private long schemaid;
	
	private String dictionaryName;
	
	private String databaseType;

	public TdgProfileDTO() {
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

	public String getDictionaryName(){
		return dictionaryName;
	}

	public void setDictionaryName(String dictionaryName){
		this.dictionaryName = dictionaryName;
	}

	public String getDatabaseType(){
		return databaseType;
	}

	public void setDatabaseType(String databaseType){
		this.databaseType = databaseType;
	}

}