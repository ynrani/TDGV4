/*
 * Object Name : TdgDictionaryDTO.java
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

import org.springframework.web.multipart.MultipartFile;

public class TdgDictionaryDTO extends AbstractBaseDTO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient MultipartFile maltiPartFile;
	private String schemaid;
	private List<String> listManualDictionaries;
	private String manualDictionary;

	public MultipartFile getMaltiPartFile(){
		return maltiPartFile;
	}

	public void setMaltiPartFile(MultipartFile maltiPartFile){
		this.maltiPartFile = maltiPartFile;
	}

	public String getSchemaid(){
		return schemaid;
	}

	public void setSchemaid(String schemaid){
		this.schemaid = schemaid;
	}

	public List<String> getListManualDictionaries(){
		return listManualDictionaries;
	}

	public void setListManualDictionaries(List<String> listManualDictionaries){
		this.listManualDictionaries = listManualDictionaries;
	}

	public String getManualDictionary(){
		return manualDictionary;
	}

	public void setManualDictionary(String manualDictionary){
		this.manualDictionary = manualDictionary;
	}
}
