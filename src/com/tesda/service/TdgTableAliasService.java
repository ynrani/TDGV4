/*
 * Object Name : TdgTableAliasService.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		11:30:00 AM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.service;

import java.util.List;

import com.tesda.model.DTO.TdgTableAliasDTO;

/**
 * @author vkrish14
 *
 */
public interface TdgTableAliasService{

	Long getTdgTableAliasRecordsCount(String userid);

	List<TdgTableAliasDTO> getTdgTableAliasRecordsForPagination(int offSet, int recordsperpage,
			boolean b, String userid);

	void deleteTableAliasById(String aliasid);

	String saveTableAliasData(TdgTableAliasDTO tdgTableAliasDTO);

	String testTableAliasConnection(TdgTableAliasDTO tdgTableAliasDTO);

	TdgTableAliasDTO getTdgTableAliasDetails(String reqaliasid, String username);
}
