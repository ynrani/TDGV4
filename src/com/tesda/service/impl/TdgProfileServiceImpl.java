/*
 * Object Name : TdgProfileServiceImpl.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		11:08:01 AM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.service.impl;

import javax.persistence.EntityManager;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.tesda.dao.TdgProfileDAO;
import com.tesda.model.DTO.TestDataGenerateDTO;
import com.tesda.model.mapper.TdgProfileMapper;
import com.tesda.service.TdgProfileService;
import com.tesda.util.TdgCentralConstant;

/**
 * @author vkrish14
 *
 */
@Component
@Service("tdgProfileService")
public class TdgProfileServiceImpl extends TdgBaseServiceImpl implements TdgProfileService{

	private static Logger logger = Logger.getLogger(TdgProfileServiceImpl.class);
	private static String strClassName = " [ TdgProfileServiceImpl ] ";
	@Autowired
	TdgProfileDAO tdgProfileDAO;
	@Autowired
	TdgProfileMapper tdgProfileMapper;
	@Override
	public String saveTestData(TestDataGenerateDTO testDataGenerateDTO){
		String strMethodName = "  [ saveTestData() ] ";
		logger.info(strClassName + strMethodName + " inside saveTestData method");
		EntityManager managerentity = null;
		try {
			managerentity = openEntityManager();
			if (managerentity != null) {
				//TdgProfileDO profileDO = tdgProfileMapper.convertTestDataGenerateDTOToTdgProfileDO(testDataGenerateDTO);
				String strStatus = tdgProfileDAO.saveProfileData(tdgProfileMapper.convertTestDataGenerateDTOToTdgProfileDO(testDataGenerateDTO));
				return strStatus;
			}
		} finally {
			if (managerentity != null) {
				closeEntityManager(managerentity);
			}
		}
		logger.info(strClassName + strMethodName + " return from saveTestData method");
		return TdgCentralConstant.FAILED_MESSAGE;
	}
}
