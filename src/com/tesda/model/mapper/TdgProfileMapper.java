/*
 * Object Name : TdgProfileMapper.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		2:29:22 PM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.model.mapper;

import com.tesda.model.DO.TdgProfileDO;
import com.tesda.model.DTO.TestDataGenerateDTO;

/**
 * @author vkrish14
 *
 */
public interface TdgProfileMapper{

	TdgProfileDO convertTestDataGenerateDTOToTdgProfileDO(TestDataGenerateDTO testDataGenerateDTO);
}
