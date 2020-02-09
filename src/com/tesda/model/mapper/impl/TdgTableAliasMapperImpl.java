/*
 * Object Name : TdgTableAliasMapperImpl.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		12:29:26 PM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.model.mapper.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.tesda.model.DO.TdgTableAliasDO;
import com.tesda.model.DTO.TdgTableAliasDTO;
import com.tesda.model.mapper.TdgTableAliasMapper;

/**
 * @author vkrish14
 *
 */

@Component
@Service("tdgTableAliasMapper")
public class TdgTableAliasMapperImpl implements TdgTableAliasMapper{
	/* (non-Javadoc)
	 * @see com.tesda.model.mapper.TdgTableAliasMapper#convertTdgTableAliasDOToTdgTableAliasDTO(java.util.List)
	 */
	@Override
	public List<TdgTableAliasDTO> convertTdgTableAliasDOToTdgTableAliasDTO(
			List<TdgTableAliasDO> tdgTableAliasRecordsForPagination){
		List<TdgTableAliasDTO> listTdgTableAliasDTO = new ArrayList<TdgTableAliasDTO>();	
		if(tdgTableAliasRecordsForPagination !=null && !tdgTableAliasRecordsForPagination.isEmpty()){
			for(TdgTableAliasDO dos : tdgTableAliasRecordsForPagination)
			listTdgTableAliasDTO.add(convertTdgTableAliasDOToTdgTableAliasDTO(dos));
		}
		return listTdgTableAliasDTO;
	}
	
	@Override
	public TdgTableAliasDTO convertTdgTableAliasDOToTdgTableAliasDTO(TdgTableAliasDO tdgTableAliasDO){
		TdgTableAliasDTO tdgTableAliasDTO = new TdgTableAliasDTO();
		if(tdgTableAliasDO != null){
			try {
				BeanUtils.copyProperties(tdgTableAliasDTO, tdgTableAliasDO);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tdgTableAliasDTO;
	}

	@Override
	public TdgTableAliasDO convertTdgTableAliasDTOToTdgTableAliasDO(TdgTableAliasDTO tdgTableAliasDTO){
		TdgTableAliasDO tdgTableAliasDO = new TdgTableAliasDO();
		if(tdgTableAliasDO != null){
			try {
				BeanUtils.copyProperties(tdgTableAliasDO, tdgTableAliasDTO);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return tdgTableAliasDO;
	}
}
