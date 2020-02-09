/*
 * Object Name : TdgProfileDAO.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		2:27:34 PM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.dao;

import com.tesda.model.DO.TdgProfileDO;

/**
 * @author vkrish14
 *
 */
public interface TdgProfileDAO{

	String saveProfileData(TdgProfileDO convertTestDataGenerateDTOToTdgProfileDO);
}
