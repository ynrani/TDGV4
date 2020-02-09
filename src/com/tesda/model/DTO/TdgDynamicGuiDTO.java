/*
 * Object Name : TdgDynamicGuiDTO.java
 * Modification Block
 * ---------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ---------------------------------------------------------------------
 * 	1.	  vkrish14		Jun 15, 2015			NA             Created
 * ---------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.model.DTO;

import java.util.Comparator;
import java.util.Map;

public class TdgDynamicGuiDTO extends AbstractBaseDTO implements Comparator<TdgDynamicGuiDTO>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long columnId;
	private String columnName;
	private String columnType;
	private String columnValues;
	private String columnLabel;
	private Map<String, String> mapValues;
	private boolean manualDictionaryContains;
	private String columnCondition;
	
	public String getColumnName(){
		return columnName;
	}

	public void setColumnName(String columnName){
		this.columnName = columnName;
	}

	public String getColumnType(){
		return columnType;
	}

	public void setColumnType(String columnType){
		this.columnType = columnType;
	}

	public String getColumnValues(){
		return columnValues;
	}

	public void setColumnValues(String columnValues){
		this.columnValues = columnValues;
	}

	public String getColumnLabel(){
		return columnLabel;
	}

	public void setColumnLabel(String columnLabel){
		this.columnLabel = columnLabel;
	}

	public Map<String, String> getMapValues(){
		return mapValues;
	}

	public void setMapValues(Map<String, String> mapValues){
		this.mapValues = mapValues;
	}

	public boolean isManualDictionaryContains(){
		return manualDictionaryContains;
	}

	public void setManualDictionaryContains(boolean manualDictionaryContains){
		this.manualDictionaryContains = manualDictionaryContains;
	}

	

	@Override
	public int compare(TdgDynamicGuiDTO o1, TdgDynamicGuiDTO o2){
		return o1.columnId
                .compareTo(o2.columnId);
	}

	public Long getColumnId(){
		return columnId;
	}

	public void setColumnId(Long columnId){
		this.columnId = columnId;
	}

	public String getColumnCondition(){
		return columnCondition;
	}

	public void setColumnCondition(String columnCondition){
		this.columnCondition = columnCondition;
	}

	
}
