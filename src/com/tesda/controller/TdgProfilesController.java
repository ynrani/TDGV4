/*
 * Object Name : TdgOperationsController.java
 * Modification Block
 * ---------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ---------------------------------------------------------------------
 * 	1.	  vkrish14		Jun 15, 2015			NA             Created
 * ---------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tesda.model.DTO.TdgProfileDTO;
import com.tesda.model.DTO.TdgSchemaDTO;
import com.tesda.service.TdgProfileService;

@Controller
public class TdgProfilesController{
	private static Logger logger = Logger.getLogger(TdgProfilesController.class);
	private static String strClassName = " [ TdgProfilesController ] ";
	
	@Resource(name = "tdgProfileService")
	TdgProfileService tdgProfileService;

	@RequestMapping(value = "/tdgProfilerDetails", method = RequestMethod.GET)
	public String getProfilerDetails(
			@RequestParam(required = false, value = "reqParam") String reqVals, ModelMap model,
			@ModelAttribute("tdgProfileDTO") TdgProfileDTO tdgProfileDTO,
			HttpServletRequest request, HttpServletResponse response){
		String strMethodName = " [ getProfilerDetails() ]";
		if(tdgProfileDTO == null){
			tdgProfileDTO = new TdgProfileDTO();
		}
		if(StringUtils.isNotEmpty(reqVals)){
		/*String array[] = reqVals.split("SCHEMA_ID");
		tdgProfileDTO.setInputdata(reqVals);
		String schemaid=array[0].substring(array[0].lastIndexOf("*")+1).substring(0,array[0].substring(array[0].lastIndexOf("*")+1).indexOf(":"));*/
		//TdgSchemaDTO schemaDTO = tdgProfileService.getSchemaDetailsById(Long.parseLong(schemaid));
		//tdgProfileDTO.setDatabaseType(schemaDTO.getD);
		}
		logger.info(strClassName + strMethodName + " inside of getProfilerDetails get method ");
		model.addAttribute("tdgProfileDTO", tdgProfileDTO);
		logger.info(strClassName + strMethodName + " return from getProfilerDetails method ");
		return "profilerDetails";
	}

	@RequestMapping(value = "/tdgProfilerDetails", method = RequestMethod.POST)
	public @ResponseBody String getProfilerDetailsPost(
			@RequestParam(required = false, value = "reqVals") String reqVals, ModelMap model,
			@ModelAttribute("tdgProfileDTO") TdgProfileDTO tdgProfileDTO,
			HttpServletRequest request, HttpServletResponse response){
		if(tdgProfileDTO == null){
			tdgProfileDTO = new TdgProfileDTO();
		}
		model.addAttribute("tdgProfileDTO", tdgProfileDTO);
		return "profilerDetails";
	}
	
	

	}
