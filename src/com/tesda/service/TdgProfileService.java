/*
 * Object Name : TdgProfileService.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		11:05:58 AM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.service;

import com.tesda.model.DTO.TestDataGenerateDTO;

/**
 * @author vkrish14
 *
 */
public interface TdgProfileService{

	String saveTestData(TestDataGenerateDTO testDataGenerateDTO);
}
