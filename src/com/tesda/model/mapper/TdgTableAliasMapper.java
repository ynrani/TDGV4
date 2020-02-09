/*
 * Object Name : TdgTableAliasMapper.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		12:09:49 PM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.model.mapper;

import java.util.List;

import com.tesda.model.DO.TdgTableAliasDO;
import com.tesda.model.DTO.TdgTableAliasDTO;

/**
 * @author vkrish14
 *
 */
public interface TdgTableAliasMapper{

	List<TdgTableAliasDTO> convertTdgTableAliasDOToTdgTableAliasDTO(
			List<TdgTableAliasDO> tdgTableAliasRecordsForPagination);

	TdgTableAliasDTO convertTdgTableAliasDOToTdgTableAliasDTO(TdgTableAliasDO tdgTableAliasDO);

	TdgTableAliasDO convertTdgTableAliasDTOToTdgTableAliasDO(TdgTableAliasDTO tdgTableAliasDTO);
}
