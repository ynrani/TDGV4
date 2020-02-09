/*
 * Object Name : TdgMasterDictionaryDTO.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		10:19:45 AM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.model.DTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vkrish14
 *
 */
public class TdgMasterDictionaryDTO extends BaseDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> tdgConnections = new ArrayList<String>();	
	private String schemaUrls;
	private String schemaNames;
	private String schemaPasswords;
	private String passedTabs="";
	private String requiredCols="";
	private String sequencePrefixTabs="";
	private String businessRules="";
	private String dependentDbs="";
	private String dateFormates="";
	private String masterTabs="";
	private String dictionaryName;
	private List<String> selectedNames = new ArrayList<String>();
	private String conSelected;
	private List<String> listTables = new ArrayList<String>();
	private List<TdgColumnValuesDTO> columndDtos = new ArrayList<TdgColumnValuesDTO>();
	private List<String> listSelectedTabs = new ArrayList<String>();
	private String selectedTabs;
	private List<String> listColumns;
	private boolean editFlag;
	private String editSchemeDetails;
	private String flagSelected;
	
	public List<String> getTdgConnections(){
		return tdgConnections;
	}
	public void setTdgConnections(List<String> tdgConnections){
		this.tdgConnections = tdgConnections;
	}
	
	public List<TdgColumnValuesDTO> getColumndDtos(){
		return columndDtos;
	}
	public void setColumndDtos(List<TdgColumnValuesDTO> columndDtos){
		this.columndDtos = columndDtos;
	}
	public String getSchemaUrls(){
		return schemaUrls;
	}
	public void setSchemaUrls(String schemaUrls){
		this.schemaUrls = schemaUrls;
	}
	public String getSchemaNames(){
		return schemaNames;
	}
	public void setSchemaNames(String schemaNames){
		this.schemaNames = schemaNames;
	}
	public String getSchemaPasswords(){
		return schemaPasswords;
	}
	public void setSchemaPasswords(String schemaPasswords){
		this.schemaPasswords = schemaPasswords;
	}
	public String getPassedTabs(){
		return passedTabs;
	}
	public void setPassedTabs(String passedTabs){
		this.passedTabs = passedTabs;
	}
	public String getRequiredCols(){
		return requiredCols;
	}
	public void setRequiredCols(String requiredCols){
		this.requiredCols = requiredCols;
	}
	public String getSequencePrefixTabs(){
		return sequencePrefixTabs;
	}
	public void setSequencePrefixTabs(String sequencePrefixTabs){
		this.sequencePrefixTabs = sequencePrefixTabs;
	}
	public String getBusinessRules(){
		return businessRules;
	}
	public void setBusinessRules(String businessRules){
		this.businessRules = businessRules;
	}
	public String getDependentDbs(){
		return dependentDbs;
	}
	public void setDependentDbs(String dependentDbs){
		this.dependentDbs = dependentDbs;
	}
	public String getDateFormates(){
		return dateFormates;
	}
	public void setDateFormates(String dateFormates){
		this.dateFormates = dateFormates;
	}
	public String getDictionaryName(){
		return dictionaryName;
	}
	public void setDictionaryName(String dictionaryName){
		this.dictionaryName = dictionaryName;
	}
	public List<String> getSelectedNames(){
		return selectedNames;
	}
	public void setSelectedNames(List<String> selectedNames){
		this.selectedNames = selectedNames;
	}
	public String getConSelected(){
		return conSelected;
	}
	public void setConSelected(String conSelected){
		this.conSelected = conSelected;
	}
	public List<String> getListTables(){
		return listTables;
	}
	public void setListTables(List<String> listTables){
		this.listTables = listTables;
	}
	public String getMasterTabs(){
		return masterTabs;
	}
	public void setMasterTabs(String masterTabs){
		this.masterTabs = masterTabs;
	}
	public List<String> getListSelectedTabs(){
		return listSelectedTabs;
	}
	public void setListSelectedTabs(List<String> listSelectedTabs){
		this.listSelectedTabs = listSelectedTabs;
	}
	public String getSelectedTabs(){
		return selectedTabs;
	}
	public void setSelectedTabs(String selectedTabs){
		this.selectedTabs = selectedTabs;
	}
	public List<String> getListColumns(){
		return listColumns;
	}
	public void setListColumns(List<String> listColumns){
		this.listColumns = listColumns;
	}
	public boolean isEditFlag(){
		return editFlag;
	}
	public void setEditFlag(boolean editFlag){
		this.editFlag = editFlag;
	}
	public String getEditSchemeDetails(){
		return editSchemeDetails;
	}
	public void setEditSchemeDetails(String editSchemeDetails){
		this.editSchemeDetails = editSchemeDetails;
	}
	public String getFlagSelected(){
		return flagSelected;
	}
	public void setFlagSelected(String flagSelected){
		this.flagSelected = flagSelected;
	}
	
}
