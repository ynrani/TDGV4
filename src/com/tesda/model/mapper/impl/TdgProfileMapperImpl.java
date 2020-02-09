/*
 * Object Name : TdgProfileMapperImpl.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		1:49:37 PM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.model.mapper.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.tesda.model.DO.TdgProfileDO;
import com.tesda.model.DTO.TestDataGenerateDTO;
import com.tesda.model.mapper.TdgProfileMapper;

/**
 * @author vkrish14
 *
 */

@Component
@Service("tdgProfileMapper")
public class TdgProfileMapperImpl implements TdgProfileMapper{

	@Override
	public TdgProfileDO convertTestDataGenerateDTOToTdgProfileDO(
			TestDataGenerateDTO testDataGenerateDTO){
		TdgProfileDO tdgProfileDO = new TdgProfileDO();
		if(testDataGenerateDTO != null && testDataGenerateDTO.getSchemaId() != 0 && StringUtils.isNotEmpty(testDataGenerateDTO.getRequestParameterValue())){
			tdgProfileDO.setInputdata(testDataGenerateDTO.getRequestParameterValue());
			tdgProfileDO.setProfileName(testDataGenerateDTO.getProfileName());
			tdgProfileDO.setSchemaid(testDataGenerateDTO.getSchemaId());
		}
		return tdgProfileDO;
	}
}
