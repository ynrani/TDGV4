/*
 * Object Name : TestDataGenerateDTO.java
 * Modification Block
 * ---------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ---------------------------------------------------------------------
 * 	1.	  vkrish14		Jun 15, 2015			NA             Created
 * ---------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.model.DTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestDataGenerateDTO extends AbstractBaseDTO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long schemaId;
	private String userId;
	private String username;
	private String url;
	private String schemaname;
	private String password;
	private String columnsdepends;
	private Map<String, Object> mapinputData;
	private Map<String, Object> mapinputConditionData;
	private String condition;
	private long lGenerateRecordsCount;
	private String schemamastertables;
	private String seqtableprefix;
	private boolean dataConditional;
	private String dateFormate;
	private Set<String> dataConditionalTabNames;
	private boolean sequenceOrder;
	private Map<String, List<String>> mapDictionaryVals = new HashMap<String, List<String>>();
	private List<String> guiDictionaryColumns;
	private String generationType;
	private List<String> requiredSequenceColumns;
	private boolean isRequiredAllColumns =false;
	private String tdgFunctionType;
	private String csvFileSeperator;
	private String dictionaryName;
	private String requestParameterValue;
	private String columnsWithHeader;
	private String profileName;
	private String populationType;
	private long totalRecordsGenerated=0;
	
	public String getUsername(){
		return username;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUrl(){
		return url;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getPassword(){
		return password;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public Map<String, Object> getMapinputData(){
		return mapinputData;
	}

	public void setMapinputData(Map<String, Object> mapinputData){
		this.mapinputData = mapinputData;
	}

	public long getGenerateRecordsCount(){
		return lGenerateRecordsCount;
	}

	public void setGenerateRecordsCount(long generateRecordsCount){
		this.lGenerateRecordsCount = generateRecordsCount;
	}

	public long getSchemaId(){
		return schemaId;
	}

	public void setSchemaId(long schemaId){
		this.schemaId = schemaId;
	}

	public String getUserId(){
		return userId;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getCondition(){
		return condition;
	}

	public void setCondition(String condition){
		this.condition = condition;
	}

	public String getSchemaname(){
		return schemaname;
	}

	public void setSchemaname(String schemaname){
		this.schemaname = schemaname;
	}

	public String getSchemamastertables(){
		return schemamastertables;
	}

	public void setSchemamastertables(String schemamastertables){
		this.schemamastertables = schemamastertables;
	}

	public String getSeqtableprefix(){
		return seqtableprefix;
	}

	public void setSeqtableprefix(String seqtableprefix){
		this.seqtableprefix = seqtableprefix;
	}

	public String getColumnsdepends(){
		return columnsdepends;
	}

	public void setColumnsdepends(String columnsdepends){
		this.columnsdepends = columnsdepends;
	}

	public Map<String, List<String>> getMapDictionaryVals(){
		return mapDictionaryVals;
	}

	public void setMapDictionaryVals(Map<String, List<String>> mapDictionaryVals){
		this.mapDictionaryVals = mapDictionaryVals;
	}

	public boolean isDataConditional(){
		return dataConditional;
	}

	public void setDataConditional(boolean dataConditional){
		this.dataConditional = dataConditional;
	}

	public Set<String> getDataConditionalTabNames(){
		return dataConditionalTabNames;
	}

	public void setDataConditionalTabNames(Set<String> dataConditionalTabNames){
		this.dataConditionalTabNames = dataConditionalTabNames;
	}

	public List<String> getGUIDictionaryColumns(){
		return guiDictionaryColumns;
	}

	public void setGUIDictionaryColumns(List<String> gUIDictionaryColumns){
		guiDictionaryColumns = gUIDictionaryColumns;
	}

	public String getDateFormate(){
		return dateFormate;
	}

	public void setDateFormate(String dateFormate){
		this.dateFormate = dateFormate;
	}

	public boolean isSequenceOrder(){
		return sequenceOrder;
	}

	public void setSequenceOrder(boolean sequenceOrder){
		this.sequenceOrder = sequenceOrder;
	}

	public String getGenerationType() {
		return generationType;
	}

	public void setGenerationType(String generationType) {
		this.generationType = generationType;
	}

	public List<String> getRequiredSequenceColumns() {
		return requiredSequenceColumns;
	}

	public void setRequiredSequenceColumns(List<String> requiredSequenceColumns) {
		this.requiredSequenceColumns = requiredSequenceColumns;
	}

	public boolean isRequiredAllColumns(){
		return isRequiredAllColumns;
	}

	public void setRequiredAllColumns(boolean isRequiredAllColumns){
		this.isRequiredAllColumns = isRequiredAllColumns;
	}

	public String getTdgFunctionType(){
		return tdgFunctionType;
	}

	public void setTdgFunctionType(String tdgFunctionType){
		this.tdgFunctionType = tdgFunctionType;
	}

	public String getCsvFileSeperator(){
		return csvFileSeperator;
	}

	public void setCsvFileSeperator(String csvFileSeperator){
		this.csvFileSeperator = csvFileSeperator;
	}

	public String getDictionaryName(){
		return dictionaryName;
	}

	public void setDictionaryName(String dictionaryName){
		this.dictionaryName = dictionaryName;
	}

	public String getRequestParameterValue(){
		return requestParameterValue;
	}

	public void setRequestParameterValue(String requestParameterValue){
		this.requestParameterValue = requestParameterValue;
	}

	public String getColumnsWithHeader(){
		return columnsWithHeader;
	}

	public void setColumnsWithHeader(String columnsWithHeader){
		this.columnsWithHeader = columnsWithHeader;
	}

	public String getProfileName(){
		return profileName;
	}

	public void setProfileName(String profileName){
		this.profileName = profileName;
	}

	public Map<String, Object> getMapinputConditionData(){
		return mapinputConditionData;
	}

	public void setMapinputConditionData(Map<String, Object> mapinputConditionData){
		this.mapinputConditionData = mapinputConditionData;
	}

	public String getPopulationType(){
		return populationType;
	}

	public void setPopulationType(String populationType){
		this.populationType = populationType;
	}

	public long getTotalRecordsGenerated(){
		return totalRecordsGenerated;
	}

	public void setTotalRecordsGenerated(long totalRecordsGenerated){
		this.totalRecordsGenerated = totalRecordsGenerated;
	}
}
