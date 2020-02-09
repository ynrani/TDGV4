/*
 * Object Name : FileUploadDTO.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		5:27:34 PM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.model.DTO;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author vkrish14
 *
 */
public class FileUploadDTO extends AbstractBaseDTO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<MultipartFile> files;
	private String manualDictionary;

	public List<MultipartFile> getFiles(){
		return files;
	}

	public void setFiles(List<MultipartFile> files){
		this.files = files;
	}

	public String getManualDictionary(){
		return manualDictionary;
	}

	public void setManualDictionary(String manualDictionary){
		this.manualDictionary = manualDictionary;
	}
}
