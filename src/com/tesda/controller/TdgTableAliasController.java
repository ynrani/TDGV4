/*
 * Object Name : TdgTableAliasController.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		11:28:24 AM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tesda.model.DTO.TdgTableAliasDTO;
import com.tesda.service.TdgTableAliasService;
import com.tesda.util.AppConstant;
import com.tesda.util.PaginationUtil;
import com.tesda.util.TdgCentralConstant;

/**
 * @author vkrish14
 *
 */
@Controller
public class TdgTableAliasController{
	private static Logger logger = Logger.getLogger(TdgTableAliasController.class);
	private static String strClassName = " [ TdgTableAliasController ] ";
	
	@Resource(name = "tdgTableAliasService")
	TdgTableAliasService tdgTableAliasService;
	
	/*@RequestMapping(value = "/editTdgTablealias", method = RequestMethod.GET)
	public String editTableAlias(@ModelAttribute("userdo") TdmUserDTO userdo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response){
		String strMethodName = " [ editTableAlias() ]";
		logger.info(strClassName + strMethodName + " inside of editTableAlias method ");
		String userId = request.getParameter("userId");
		String button = "Update User";
		userdo = tdgTableAliasService.getEditAlias(userId);
		model.addAttribute("userdo", userdo);
		model.addAttribute("Button", button);
		logger.info(strClassName + strMethodName + " return from editTableAlias method ");
		return "createNewUser";
	}

	@RequestMapping("/createalias")
	public String createTableAlias(@ModelAttribute("userdo") TdmUserDTO userdo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response){
		String strMethodName = " [ createTableAlias() ]";
		logger.info(strClassName + strMethodName + " inside of createTableAlias method ");
		String button = "Create User";
		userdo.setCreated(true);
		model.addAttribute("userdo", userdo);
		model.addAttribute("Button", button);
		logger.info(strClassName + strMethodName + " rturn from the createTableAlias method ");
		return "createNewUser";
	}

	@RequestMapping(value = "/deleteTdgTablealias")
	public String daleteTableAlias(@ModelAttribute("userdo") TdmUserDTO userdo, ModelMap model,
			HttpServletRequest request, HttpServletResponse response){
		String strMethodName = " [ daleteTableAlias() ]";
		logger.info(strClassName + strMethodName + " inside of daleteTableAlias method ");
		tDMAdminService.deleteUserByUserId(request.getParameter("userId"));
		logger.info(strClassName + strMethodName + " return from daleteTableAlias method");
		return "redirect:testdaAdmin";
	}*/


	@RequestMapping(value="/createalias",method=RequestMethod.GET)
	public String createGetTableAlias(@ModelAttribute("tdgTableAliasDTO") TdgTableAliasDTO tdgTableAliasDTO, ModelMap model,
			HttpServletRequest request, HttpServletResponse response){
		String strMethodName = " [ createGetTableAlias() ]";
		logger.info(strClassName + strMethodName + " inside of createGetTableAlias method ");
		model.addAttribute("tdgTableAliasDTO", tdgTableAliasDTO);
		logger.info(strClassName + strMethodName + " return from the createGetTableAlias method ");
		return "tableAliasAdd";
	}
	
	@RequestMapping(value="/editTdgTablealias",method=RequestMethod.GET)
	public String editGetTableAlias( ModelMap model,@ModelAttribute("tdgTableAliasDTO") TdgTableAliasDTO tdgTableAliasDTO,
			@RequestParam(value = "reqaliasid", required = true) String reqaliasid,
			HttpServletRequest request, HttpServletResponse response){
		String strMethodName = " [ editGetTableAlias() ]";
		logger.info(strClassName + strMethodName + " inside of editGetTableAlias method ");
		//User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//tdgTableAliasDTO= tdgTableAliasService.getTdgTableAliasDetails(reqaliasid,user.getUsername());
		tdgTableAliasDTO= tdgTableAliasService.getTdgTableAliasDetails(reqaliasid,null);
		model.addAttribute("tdgTableAliasDTO", tdgTableAliasDTO);
		logger.info(strClassName + strMethodName + " return from the editGetTableAlias method ");
		return "tableAliasEdit";
	}

	@RequestMapping(value="/editTdgTablealias",method=RequestMethod.POST)
	public String editPostTableAlias(@ModelAttribute("tdgTableAliasDTO") TdgTableAliasDTO tdgTableAliasDTO, ModelMap model,
			@RequestParam(value = AppConstant.TEST_CON, required = false) String testCon,
			@RequestParam(value = AppConstant.CREATE_CON, required = false) String saveCon,
			HttpServletRequest request, HttpServletResponse response){
		String strMethodName = " [ createPostTableAlias() ]";
		String pingSuccessMsg = null;
		String saveSuccessMsg = null;
		logger.info(strClassName + strMethodName + " inside of createPostTableAlias method ");
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		tdgTableAliasDTO.setUserid(user.getUsername());
		if (null != testCon) {
			pingSuccessMsg = tdgTableAliasService.testTableAliasConnection(tdgTableAliasDTO);
		} else if (null != saveCon) {
			saveSuccessMsg = tdgTableAliasService.saveTableAliasData(tdgTableAliasDTO);
			if(TdgCentralConstant.SUCCESS_MESSAGE.equals(saveSuccessMsg)){
			saveSuccessMsg = AppConstant.SUCCESS_SAVE_MSG;
			pingSuccessMsg = TdgCentralConstant.SUCCESS_MESSAGE;
			}
			else
				saveSuccessMsg = "Failed to save data.";
		}
		//String strResponse = tdgTableAliasService.saveTableAliasData(tdgTableAliasDTO);
		if(!TdgCentralConstant.SUCCESS_MESSAGE.equals(pingSuccessMsg)){
			/*tdgTableAliasDTO.setMessageConstant(TdgCentralConstant.SUCCESS_MESSAGE);
			tdgTableAliasDTO.setMessage("Table Alias value added successfully.");*/
			pingSuccessMsg = "Alias name is not valid...";
		}else{
			/*tdgTableAliasDTO.setMessageConstant(TdgCentralConstant.FAILED_MESSAGE);
			tdgTableAliasDTO.setMessage("Failed to add Table Alias values.");*/
			pingSuccessMsg = "Alias name is valid...";
		}
		
		logger.info(strClassName + strMethodName + " rturn from the createPostTableAlias method ");
		if (null != saveCon) {
			return "redirect:aliasdashboard";
		}
		model.addAttribute("tdgTableAliasDTO", tdgTableAliasDTO);
		model.addAttribute(AppConstant.STATUS, pingSuccessMsg);
		model.addAttribute(AppConstant.SAVE_STS, saveSuccessMsg);
		model.addAttribute(AppConstant.BTN, false);
		return "tableAliasEdit";
	}
	
	@RequestMapping(value="/createalias",method=RequestMethod.POST)
	public String createPostTableAlias(@ModelAttribute("tdgTableAliasDTO") TdgTableAliasDTO tdgTableAliasDTO, ModelMap model,
			@RequestParam(value = AppConstant.TEST_CON, required = false) String testCon,
			@RequestParam(value = AppConstant.CREATE_CON, required = false) String saveCon,
			HttpServletRequest request, HttpServletResponse response){
		String strMethodName = " [ createPostTableAlias() ]";
		String pingSuccessMsg = null;
		String saveSuccessMsg = null;
		logger.info(strClassName + strMethodName + " inside of createPostTableAlias method ");
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		tdgTableAliasDTO.setUserid(user.getUsername());
		if (null != testCon) {
			pingSuccessMsg = tdgTableAliasService.testTableAliasConnection(tdgTableAliasDTO);
		} else if (null != saveCon) {
			saveSuccessMsg = tdgTableAliasService.saveTableAliasData(tdgTableAliasDTO);
			if(TdgCentralConstant.SUCCESS_MESSAGE.equals(saveSuccessMsg)){
			saveSuccessMsg = AppConstant.SUCCESS_SAVE_MSG;
			pingSuccessMsg = TdgCentralConstant.SUCCESS_MESSAGE;
			}
			else
				saveSuccessMsg = "Failed to save data.";
		}
		//String strResponse = tdgTableAliasService.saveTableAliasData(tdgTableAliasDTO);
		if(!TdgCentralConstant.SUCCESS_MESSAGE.equals(pingSuccessMsg)){
			/*tdgTableAliasDTO.setMessageConstant(TdgCentralConstant.SUCCESS_MESSAGE);
			tdgTableAliasDTO.setMessage("Table Alias value added successfully.");*/
			pingSuccessMsg = "Alias name is not valid...";
		}else{
			/*tdgTableAliasDTO.setMessageConstant(TdgCentralConstant.FAILED_MESSAGE);
			tdgTableAliasDTO.setMessage("Failed to add Table Alias values.");*/
			pingSuccessMsg = "Alias name is valid...";
		}
		
		logger.info(strClassName + strMethodName + " rturn from the createPostTableAlias method ");
		if (null != saveCon) {
			return "redirect:aliasdashboard";
		}
		model.addAttribute("tdgTableAliasDTO", tdgTableAliasDTO);
		model.addAttribute(AppConstant.STATUS, pingSuccessMsg);
		model.addAttribute(AppConstant.SAVE_STS, saveSuccessMsg);
		model.addAttribute(AppConstant.BTN, false);
		return "tableAliasAdd";
	}

	@RequestMapping(value = "/deleteTdgTablealias")
	public String deleteTableAlias(@RequestParam(value = "reqaliasid", required = true) String aliasid,
			HttpServletRequest request, HttpServletResponse response){
		String strMethodName = " [ deleteTableAlias() ]";
		logger.info(strClassName + strMethodName + " inside of deleteTableAlias method ");
		tdgTableAliasService.deleteTableAliasById(aliasid);
		logger.info(strClassName + strMethodName + " return from deleteTableAlias method");
		return "redirect:aliasdashboard";
	}


	@RequestMapping(value = "/aliasdashboard", method = RequestMethod.GET)
	public String tdgTableAliasDashboard(
			@RequestParam(value = "page", required = false) String page, ModelMap model,
			HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("tdgTableAliasDTO") TdgTableAliasDTO tdgTableAliasDTO){
		String strMethodName = " [ tdgTableAliasDashboard() ]";
		logger.info(strClassName + strMethodName
				+ " inside of tdgTableAliasDashboard get method ");
		try {
			//User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			Long totalRecords = 0L;
			PaginationUtil pagenation = new PaginationUtil();
			int recordsperpage = Integer.valueOf(10);
			int offSet = pagenation.getOffset(request, recordsperpage);
			totalRecords = tdgTableAliasService.getTdgTableAliasRecordsCount(null);
			List<TdgTableAliasDTO> tdgTableAliasDTOList = tdgTableAliasService
					.getTdgTableAliasRecordsForPagination(offSet, recordsperpage, true,null);
			pagenation.paginate(totalRecords, request, Double.valueOf(recordsperpage),
					recordsperpage);
			int noOfPages = (int) Math.ceil(totalRecords.doubleValue() / recordsperpage);
			request.setAttribute("noOfPages", noOfPages);
			model.addAttribute("tdgTableAliasDTOList", tdgTableAliasDTOList);
			model.addAttribute("tdgTableAliasDTO", tdgTableAliasDTO);
			return "tdgTableAliasDashboard";
		} catch (Exception e) {
			logger.error(strClassName + " " + e.getMessage());
			return "tdgTableAliasDashboard";
		}
	}
}
