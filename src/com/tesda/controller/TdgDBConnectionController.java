
package com.tesda.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tesda.model.DTO.DbConnectionsDTO;
import com.tesda.model.DTO.TdgSchemaDTO;
import com.tesda.service.DbConnectionService;
import com.tesda.util.AppConstant;
import com.tesda.util.PaginationUtil;



@Controller
public class TdgDBConnectionController
{

	//private static Logger logger = Logger.getLogger(TdgDBConnectionController.class);

	@Resource(name = "dbConnectionService")
	DbConnectionService dbConnectionService;

	/**
	 * 
	 * @param id
	 * @param dbConnectionsDTO
	 * @param modelmap
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = AppConstant.DB_CON, method = RequestMethod.GET)
	public String conGet(@RequestParam(value = "id", required = false) String id,
			@ModelAttribute("dbConnectionsDTO") DbConnectionsDTO dbConnectionsDTO,
			ModelMap modelmap, HttpServletRequest request, HttpServletResponse response) {
		String sqlAuthenticationChecked = "true";
		
		try {
			if (null != id) {
				dbConnectionsDTO = dbConnectionService.savedConnection(id);
				if(dbConnectionsDTO.getAuthenticationType().equalsIgnoreCase("windowsAuthentication"))
				{
					dbConnectionsDTO.setUser("wa");
					dbConnectionsDTO.setPass("wa");
					 sqlAuthenticationChecked = "false";
				}
				modelmap.addAttribute(AppConstant.BTN, false);
			} else {
				modelmap.addAttribute(AppConstant.BTN, true);
			}

		} catch (Exception baseEx) {
			baseEx.printStackTrace();
			// responseMsg = passcodes and get msg from properties file by passing key as
			// baseEx.getErrorCode();
			modelmap.addAttribute(AppConstant.BTN, true);
			modelmap.addAttribute("dbConnectionsDTO", dbConnectionsDTO);
			return AppConstant.DB_CON_VIEW;
		}
		modelmap.addAttribute("dbConnectionsDTO", dbConnectionsDTO);
		modelmap.addAttribute("sqlAuthenticationChecked", sqlAuthenticationChecked);
		return AppConstant.DB_CON_VIEW;
	}

	/**
	 * 
	 * @param testCon
	 * @param saveCon
	 * @param dbConnectionsDTO
	 * @param modelmap
	 * @param request
	 * @param response
	 * @return
	 * @throws BaseException
	 */
	@RequestMapping(value = AppConstant.DB_CON, method = RequestMethod.POST)
	public String conPOST(
			@RequestParam(value = AppConstant.TEST_CON, required = false) String testCon,
			@RequestParam(value = AppConstant.CREATE_CON, required = false) String saveCon,
			@ModelAttribute("dbConnectionsDTO") DbConnectionsDTO dbConnectionsDTO,
			ModelMap modelmap, HttpServletRequest request, HttpServletResponse response)
			 {
		
		String pingSuccessMsg = null;
		String saveSuccessMsg = null;
		
		String sqlAuthenticationChecked = "true";
		if(dbConnectionsDTO.getAuthenticationType().equalsIgnoreCase("windowsAuthentication"))
		{
			dbConnectionsDTO.setUser("wa");
			dbConnectionsDTO.setPass("wa");
			 sqlAuthenticationChecked = "false";
		}
		
		
		try {
			dbConnectionsDTO.setUserId((String) request.getSession().getAttribute(
					"UserId"));
			if (null != testCon) {
				pingSuccessMsg = dbConnectionService.testConnection(dbConnectionsDTO);
				
			} else if (null != saveCon) {
				dbConnectionsDTO = dbConnectionService.saveConnection(dbConnectionsDTO);
				saveSuccessMsg = AppConstant.SUCCESS_SAVE_MSG;
			}

		} catch (Exception baseEx){
			baseEx.printStackTrace();
			// responseMsg = passcodes and get msg from properties file by passing key as
			// baseEx.getErrorCode();
			modelmap.addAttribute(AppConstant.STATUS, AppConstant.PING_FAIL_MSG);
			modelmap.addAttribute(AppConstant.SAVE_STS, AppConstant.SAVE_FAIL_MSG);
			modelmap.addAttribute(AppConstant.BTN, true);
			modelmap.addAttribute("sqlAuthenticationChecked", sqlAuthenticationChecked);
			
			return AppConstant.DB_CON_VIEW;
		}
		if (null != saveCon) {
			return AppConstant.DB_CON_LIST_RED;
		}
		modelmap.addAttribute(AppConstant.STATUS, pingSuccessMsg);
		modelmap.addAttribute(AppConstant.SAVE_STS, saveSuccessMsg);
		modelmap.addAttribute(AppConstant.BTN, false);
		modelmap.addAttribute("sqlAuthenticationChecked", sqlAuthenticationChecked);
		return AppConstant.DB_CON_VIEW;
		

	}

	/**
	 * 
	 * @param page
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws BaseException
	 */
	@RequestMapping(value = AppConstant.DB_CON_LIST, method = RequestMethod.GET)
	public String connectionsDashboard(
			@RequestParam(value = "page", required = false) String page, ModelMap model,
			HttpServletRequest request, HttpServletResponse response){

		Long totalRecords = 0L;
		PaginationUtil pagenation = new PaginationUtil();
		int recordsperpage = 10;
		try {
			int offSet = pagenation.getOffset(request, recordsperpage);
			totalRecords = dbConnectionService.connectionsDashboardCount((String) request
					.getSession().getAttribute(AppConstant.SESSION_UID));
			List<DbConnectionsDTO> dbConnectionsDTOs = dbConnectionService.connectionsDashboard(
					offSet, recordsperpage, true,
					(String) request.getSession().getAttribute(AppConstant.SESSION_UID));
			List<DbConnectionsDTO> lstResult = new ArrayList<DbConnectionsDTO>();
			List<TdgSchemaDTO> lstDTO = dbConnectionService.fetchAllTdgSchemaDetails();
			boolean bCheck = false;
			if(dbConnectionsDTOs != null)
			for(DbConnectionsDTO dbDto : dbConnectionsDTOs){
				DbConnectionsDTO conDTO = dbDto;
				if(lstDTO != null)
				for(TdgSchemaDTO schemaDto : lstDTO){
					if(schemaDto.getDataconnections() != null && schemaDto.getDataconnections().equals(dbDto.getDisplayName())){						
						conDTO.setDisplayName(dbDto.getDisplayName());
						lstResult.add(conDTO);
						bCheck = true;
						break;
					}
				}
				if(!bCheck)
				lstResult.add(conDTO);
				bCheck = false;
			}
			pagenation.paginate(totalRecords, request, (double) recordsperpage, recordsperpage);
			int noOfPages = (int) Math.ceil(totalRecords.doubleValue() / recordsperpage);
			request.setAttribute("page", noOfPages);
			model.addAttribute(AppConstant.DB_CONN_DTLS, lstResult);
			
			return AppConstant.DB_CON_LIST_VIEW;
		} catch (Exception baseEx) {
			baseEx.printStackTrace();
					return AppConstant.DB_CON_LIST_VIEW;
			}
			// responseMsg = passcodes and get msg from properties file by passing key as
			// baseEx.getErrorCode();
			//return AppConstant.DB_CON_LIST_VIEW;
		//}
	}

	@RequestMapping(value = AppConstant.DB_CON_DELETE, method = RequestMethod.GET)
	public String deleteConnection(
			@RequestParam(value = "id", required = false) String conId,
			ModelMap modelmap, HttpServletRequest request, HttpServletResponse response) {
		
		//boolean sts = false;
		try {
			if (null != conId) {
				dbConnectionService.deleteConnection(conId);
			}
		} catch (Exception baseEx) {

			baseEx.printStackTrace();
			// responseMsg = passcodes and get msg from properties file by passing key as
			// baseEx.getErrorCode();
			return AppConstant.DB_CON_LIST_RED;
		}
		return AppConstant.DB_CON_LIST_RED;
	}

}
