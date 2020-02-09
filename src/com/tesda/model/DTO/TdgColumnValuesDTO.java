/*
 * Object Name : TdgColumnValuesDTO.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		10:18:05 AM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.model.DTO;

/**
 * @author vkrish14
 *
 */
public class TdgColumnValuesDTO{
	private String columnName;
	private String columnLabel;
	private String guiType;
	private String columnnValues;
	public String getColumnName(){
		return columnName;
	}
	public void setColumnName(String columnName){
		this.columnName = columnName;
	}
	public String getColumnLabel(){
		return columnLabel;
	}
	public void setColumnLabel(String columnLabel){
		this.columnLabel = columnLabel;
	}
	public String getGuiType(){
		return guiType;
	}
	public void setGuiType(String guiType){
		this.guiType = guiType;
	}
	public String getColumnnValues(){
		return columnnValues;
	}
	public void setColumnnValues(String columnnValues){
		this.columnnValues = columnnValues;
	}
	
}
