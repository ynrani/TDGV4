/*
 * Object Name : TdgDictionariesService.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		11:04:10 AM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.service;

import java.util.List;

import com.tesda.model.DTO.DbConnectionsDTO;
import com.tesda.model.DTO.TdgSchemaDTO;

/**
 * @author vkrish14
 *
 */
public interface TdgDictionariesService{

	String saveTdgSchemaDetails(TdgSchemaDTO tdgSchemaDTO);

	List<DbConnectionsDTO> getAvailableConnections();

	List<String> getAllTabs(String url, String username, String password);

	List<String> getColsByTabs(String string, String string2, String string3, List<String> listTabs);

	TdgSchemaDTO getSchemaDetails(long lSchemaId);
}
