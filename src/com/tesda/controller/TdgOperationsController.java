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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
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
import org.springframework.web.servlet.ModelAndView;

import com.tesda.model.DTO.TdgDynamicPageContentDTO;
import com.tesda.model.DTO.TdgRequestListDTO;
import com.tesda.model.DTO.TestDataGenerateDTO;
import com.tesda.service.TdgOperationsService;
import com.tesda.service.TdgProfileService;
import com.tesda.util.CSVGenerator;
import com.tesda.util.PaginationUtil;
import com.tesda.util.TDGBaseUtil;
import com.tesda.util.TdgCentralConstant;

@Controller
public class TdgOperationsController extends BaseController{
	private static Logger logger = Logger.getLogger(TdgOperationsController.class);
	private static String strClassName = " [ TdgOperationsController ] ";
	@Resource(name = "tdgOperationsService")
	TdgOperationsService tdgOperationsService;
	
	@Resource(name = "tdgProfileService")
	TdgProfileService tdgProfileService;

	@RequestMapping(value = "/tdgOperationsDetails", method = RequestMethod.GET)
	public String generateDataDetails(
			@ModelAttribute("dynamicPageContent") TdgDynamicPageContentDTO dynamicPageContent,
			ModelMap model, HttpServletRequest request, HttpServletResponse response){
		String strMethodName = " [ schemaDetails() ]";
		if(TdgCentralConstant.SESSION_CONTINUE.equals(checkSession(request, response))){
		logger.info(strClassName + strMethodName + " inside of schemaDetails get method ");
		List<TdgDynamicPageContentDTO> listDynamicPageContentDTO = tdgOperationsService
				.getAllSchemaDetailsForDynamicPage();
		model.addAttribute("requestList", listDynamicPageContentDTO);
		logger.info(strClassName + strMethodName + " return from schemaDetails method ");
		return "tdgOperationsDetails";
		}else{
			return TdgCentralConstant.LOGIN_PAGE;
		}
	}

	@RequestMapping(value = "/tdgOperationsDetails", method = RequestMethod.POST)
	public @ResponseBody String generateDataDetails(
			@RequestParam(required = false, value = "reqVals") String reqVals, ModelMap model,
			@ModelAttribute("dynamicPageContent") TdgDynamicPageContentDTO dynamicPageContent,
			HttpServletRequest request, HttpServletResponse response){
		String strMethodName = " [ schemaDetails() ]";
		logger.info(strClassName + strMethodName + " inside of schemaDetails post method ");
		String strRequestValues = request.getParameter("reqVals");
		StringBuffer strSuccess = new StringBuffer("");
		List<TdgDynamicPageContentDTO> listDynamicPageContentDTO = null;
		//long lSelectedSchemaId = 0;
		long lRequestedCount = 0;
		
		if (strRequestValues != null && strRequestValues.contains("*")) {
			/**
			 * Going to fetch schema Details
			 */
		
			
			listDynamicPageContentDTO = tdgOperationsService.getAllSchemaDetailsForDynamicPage();
			//TestDataGenerateDTO testDataGenerateDTO = generateTestData(strRequestValues, dynamicPageContent, listDynamicPageContentDTO);
			TestDataGenerateDTO testDataGenerateDTO = TDGBaseUtil.generateTestData(strRequestValues, dynamicPageContent, listDynamicPageContentDTO);
				lRequestedCount = testDataGenerateDTO.getGenerateRecordsCount();
				if ("INSERT".equalsIgnoreCase(testDataGenerateDTO.getPopulationType()) && (lRequestedCount <= 0
						|| lRequestedCount > TdgCentralConstant.GENERATECOUNT_LIMIT)) {
					strSuccess
							.append(TdgCentralConstant.FAILED_MESSAGE)
							.append('#')
							.append(" Generate Count should be greater than 0 and less than "
									+ TdgCentralConstant.GENERATECOUNT_LIMIT);
				}
			
			if (!"DELETE".equalsIgnoreCase(testDataGenerateDTO.getPopulationType()) && (testDataGenerateDTO.getMapinputData() ==null || testDataGenerateDTO.getMapinputData().isEmpty())) {
				if (strSuccess.toString().contains(TdgCentralConstant.FAILED_MESSAGE)) {
					strSuccess
							.append("<br/>")
							.append(" Atleast one criteria is required apart from Generate Count");
				} else {
					strSuccess
							.append(TdgCentralConstant.FAILED_MESSAGE)
							.append('#')
							.append(" Atleast one criteria is required apart from Generate Count");
				}
			}
			
			logger.info("Requested count to generate the records :: " + lRequestedCount);
			if (!strSuccess.toString().contains(TdgCentralConstant.FAILED_MESSAGE)) {
				
				/**
				 * going to divide save or generate the data
				 */
				if("SAVE_PROFILE".equalsIgnoreCase(testDataGenerateDTO.getTdgFunctionType())){
					String strResponse = TdgCentralConstant.SUCCESS_MESSAGE;//tdgProfileService.saveTestData(testDataGenerateDTO);
					if (strResponse.contains(TdgCentralConstant.SUCCESS_MESSAGE)) {
						strSuccess.append(testDataGenerateDTO.getRequestParameterValue()).append("SAVE_PROFILE ");					
					} else {
						strSuccess.append("Error raised while saving profile.");
					}
				}/*else if("GENERATESAMPLE".equalsIgnoreCase(testDataGenerateDTO.getTdgFunctionType())){
					String strResponse = tdgProfileService.saveTestData(testDataGenerateDTO);
					if (strResponse.contains(TdgCentralConstant.SUCCESS_MESSAGE)) {
						strSuccess.append(strResponse.substring(strResponse.indexOf("#")+1, strResponse.length())).append(" profile is saved");					
					} else {
						strSuccess.append("Error raised while saving profile.");
					}
				}*/else{
					Map<String, List<String>> mapResult = null;
					if (!StringUtils.isEmpty(testDataGenerateDTO.getDictionaryName())) {
						mapResult = tdgOperationsService
								.retrieveManualDictionaryValues(testDataGenerateDTO.getDictionaryName(),testDataGenerateDTO.getSchemaname());
					}
					if (mapResult != null && !mapResult.isEmpty()) {
						testDataGenerateDTO.setMapDictionaryVals(mapResult);
					}
					if("GENERATESAMPLE".equalsIgnoreCase(testDataGenerateDTO.getTdgFunctionType())){
						testDataGenerateDTO.setGenerationType("CSV");
						if(testDataGenerateDTO.getGenerateRecordsCount() > 10)
							testDataGenerateDTO.setGenerateRecordsCount(10);
					}
					
				String strResponse = tdgOperationsService.generateTestData(testDataGenerateDTO);
				if("GENERATESAMPLE".equalsIgnoreCase(testDataGenerateDTO.getTdgFunctionType())){
					if(strResponse.contains(TdgCentralConstant.SUCCESS_MESSAGE)){
					/*String statusDes = strResponse.substring(strResponse.indexOf("#")+1,strResponse.length());

					response.setContentType("application/octet-stream");
					// response.setContentLength(2048);
					response.setHeader(
							"Content-Disposition",
							"attachment;filename=\""
									+ statusDes.substring(
											statusDes.lastIndexOf("/") + 1,
											statusDes.length()) + "\"");
					try {
						File f = new File(statusDes);
						byte[] arBytes = new byte[(int) f.length()];
						FileInputStream is = new FileInputStream(f);
						is.read(arBytes);
						ServletOutputStream op = response.getOutputStream();
						op.write(arBytes);
						op.flush();
						is.close();
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}*/
						strSuccess.append(strResponse.substring(strResponse.indexOf("#")+1));
				}else{
					strSuccess.append("0 record(s) are generated " );
				}
				}else{
				if (strResponse.contains(TdgCentralConstant.SUCCESS_MESSAGE)) {
					if(lRequestedCount <= 0 && !"INSERT".equalsIgnoreCase(testDataGenerateDTO.getPopulationType())){
						String result[] = strResponse.split("#");
						if(result.length == 3)
						//lRequestedCount=Long.parseLong();
						strSuccess.append(result[2]).append(" record(s) are generated and request id is : "+result[1]);
					}else
						strSuccess.append(lRequestedCount).append(" record(s) are generated " +(strResponse.contains("#") ? " and request id is : "+strResponse.split("#")[1] : ""));
										
				} else {
					strSuccess.append("0 record(s) are generated " +(strResponse.contains("#") ? " and request id is : "+strResponse.split("#")[1] : ""));
				}
				}
				}
				
			}
		} else {
			strSuccess
					.append(TdgCentralConstant.FAILED_MESSAGE)
					.append('#')
					.append("Generate Count should be greater than 0 and less than "
							+ TdgCentralConstant.GENERATECOUNT_LIMIT);
		}
		model.addAttribute("requestList", listDynamicPageContentDTO);
		model.addAttribute("dynamicPageContent", dynamicPageContent);
		logger.info(strClassName + strMethodName + " return from schemaDetails post method ");
		return strSuccess.toString();
	}
	
	
	
		
	public void downloadTdgCSVFlatFile(Map<String,List<Object[]>> mapRes, HttpServletResponse response){
		for(Map.Entry<String, List<Object[]>> mapEntry : mapRes.entrySet()){
		response.setContentType("text/csv");
		String disposition = "attachment; fileName="+mapEntry.getKey()+".csv";
		response.setHeader("Content-Disposition", disposition);							
			try {
				response.getWriter().append(
						CSVGenerator.getCSVForList(mapEntry.getValue()));
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
	}

	@RequestMapping(value = "/tdgDashBoardDetails", method = RequestMethod.GET)
	public String tdgDashBoardDetails(@RequestParam(value = "page", required = false) String page,
			@ModelAttribute("tdgRequestListDTO") TdgRequestListDTO tdgRequestListDTO,
			HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String strMethodName = " [ tdgDashBoardDetails() ]";
		logger.info(strClassName + strMethodName + " inside of tdgDashBoardDetails get method ");
		if (page != null) {
			Long totalRecords = 0L;
			PaginationUtil pagenation = new PaginationUtil();
			long lRequestId = tdgRequestListDTO != null && tdgRequestListDTO.getRequestid() != null ? tdgRequestListDTO
					.getRequestid() : 0;
			int recordsperpage = Integer.valueOf(TdgCentralConstant.PAGINATION_SIZE);
			int offSet = pagenation.getOffset(request, recordsperpage);
			totalRecords = tdgOperationsService
					.getReservedRecordsForRequestGeneratedCount(lRequestId);
			logger.debug(strClassName + strMethodName + " Total records found in server is :: "
					+ totalRecords);
			TdgRequestListDTO tempTdgRequestListDTO = tdgOperationsService.getDashBoardDetails(
					lRequestId, offSet, recordsperpage, true);
			pagenation.paginate(totalRecords, request, Double.valueOf(recordsperpage),
					recordsperpage);
			int noOfPages = (int) Math.ceil(totalRecords.doubleValue() / recordsperpage);
			request.setAttribute("noOfPages", noOfPages);
			tempTdgRequestListDTO.setRequestid(tdgRequestListDTO != null ? tdgRequestListDTO
					.getRequestid() : null);
			model.addAttribute("tdgRequestListDTO", tempTdgRequestListDTO);
		} else {
		}
		logger.info(strClassName + strMethodName + " return from tdgDashBoardDetails get method ");
		return "tdgDashBoard";
	}

	@RequestMapping(value = "/tdgDashBoardDetails", method = RequestMethod.POST)
	public String tdgDashBoardDetails(@RequestParam(value = "page", required = false) String page,
			@ModelAttribute("tdgRequestListDTO") TdgRequestListDTO tdgRequestListDTO,
			ModelMap model, HttpServletRequest request, HttpServletResponse response){
		String strMethodName = " [ tdgDashBoardDetails() ]";
		logger.info(strClassName + strMethodName + " inside of tdgDashBoardDetails post method ");
		Long totalRecords = 0L;
		PaginationUtil pagenation = new PaginationUtil();
		int recordsperpage = Integer.valueOf(TdgCentralConstant.PAGINATION_SIZE);
		int offSet = pagenation.getOffset(request, recordsperpage);
		long lRequestId = tdgRequestListDTO != null && tdgRequestListDTO.getRequestid() != null ? tdgRequestListDTO
				.getRequestid() : 0;
		totalRecords = tdgOperationsService.getReservedRecordsForRequestGeneratedCount(lRequestId);
		logger.debug(strClassName + strMethodName + " Total records found in server is :: "
				+ totalRecords);
		TdgRequestListDTO tempTdgRequestListDTO = tdgOperationsService.getDashBoardDetails(
				lRequestId, offSet, recordsperpage, true);
		pagenation.paginate(totalRecords, request, Double.valueOf(recordsperpage), recordsperpage);
		int noOfPages = (int) Math.ceil(totalRecords.doubleValue() / recordsperpage);
		request.setAttribute("noOfPages", noOfPages);
		tempTdgRequestListDTO.setRequestid(tdgRequestListDTO != null ? tdgRequestListDTO
				.getRequestid() : null);
		if (tempTdgRequestListDTO.getListTdgRequestListDTO() == null) {
			tempTdgRequestListDTO.setMessageConstant(TdgCentralConstant.FAILED_MESSAGE);
			tempTdgRequestListDTO.setMessage("No record(s) found for given criteria ");
		}
		model.addAttribute("tdgRequestListDTO", tempTdgRequestListDTO);
		logger.info(strClassName + strMethodName + " return from tdgDashBoardDetails post method ");
		return "tdgDashBoard";
	}

	@RequestMapping(value = "/tdgDependentDetails", method = RequestMethod.GET)
	public @ResponseBody String dependentValuesDetails(
			@RequestParam(required = false, value = "reqvalsDependent") String reqvalsDependent,
			ModelMap model, HttpServletRequest request, HttpServletResponse response){
		String strMethodName = " [ schemaDetails() ]";
		logger.info(strClassName + strMethodName + " inside of schemaDetails get method ");
		long lSelectedSchemaId = 0;
		String strResponse = "";
		String strConditionvalue = "";
		String strComponentName = "";
		if (reqvalsDependent != null && reqvalsDependent.contains("*")) {
			String[] strParentSplit = reqvalsDependent.split("\\*");
			for (int i = 0; i < strParentSplit.length; i++) {
				if (strParentSplit[i].contains(":")) {
					String[] strChildSplit = strParentSplit[i].split(":");
					if (strChildSplit.length > 0) {
						if (strChildSplit[0].contains("SCHEMA_ID")) {
							lSelectedSchemaId = Long.parseLong(strChildSplit[1]);
						} else if (strChildSplit[0].contains("COMPONENT_NAME")) {
							strComponentName = strChildSplit[1];
						} else {
							strConditionvalue = strChildSplit[1];
						}
					}
				}
			}
			strResponse = tdgOperationsService.getDynamicDependentValues(lSelectedSchemaId,
					strComponentName, strConditionvalue);
		}
		logger.info(strClassName + strMethodName + " return from schemaDetails method ");
		return strResponse;
	}
	
	
	
    //downloadFlatFiles	
	@RequestMapping(value = "/downloadFlatFiles", method = RequestMethod.GET)
	public void downloadFlatFiles(
			@RequestParam(value = "reqId", required = false) String reqId, ModelMap model,
 HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String strMethodName = " [ downloadTdgRequest() ]";
		logger.info(strClassName + strMethodName
				+ " inside of downloadTdgRequest get method ");
		TdgRequestListDTO tdgRequestListDTO = null;
		if (reqId != null) {
			tdgRequestListDTO = tdgOperationsService
					.getDashBoardRequestedRecords(Long.valueOf(request
							.getParameter("reqId")));
		}
		if (tdgRequestListDTO != null
				&& TdgCentralConstant.TDG_GENERATE_SUCCESS
						.equalsIgnoreCase(tdgRequestListDTO.getStatus())) {
			String statusDes = tdgRequestListDTO.getStatusdescription();

			response.setContentType("application/octet-stream");
			// response.setContentLength(2048);
			response.setHeader(
					"Content-Disposition",
					"attachment;filename=\""
							+ statusDes.substring(
									statusDes.lastIndexOf("/") + 1,
									statusDes.length()) + "\"");
			try {
				File f = new File(statusDes);
				byte[] arBytes = new byte[(int) f.length()];
				FileInputStream is = new FileInputStream(f);
				is.read(arBytes);
				ServletOutputStream op = response.getOutputStream();
				op.write(arBytes);
				op.flush();
				is.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		logger.info(strClassName + strMethodName
				+ " return from tdgDashBoardDetails get method ");
	}
	
	
	@RequestMapping(value = "/downloadSampleFlatFiles", method = RequestMethod.GET)
	public void downloadSampleFlatFiles(
			@RequestParam(value = "reqId", required = false) String reqId, ModelMap model,
 HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String strMethodName = " [ downloadTdgRequest() ]";
		logger.info(strClassName + strMethodName
				+ " inside of downloadTdgRequest get method ");
		//TdgRequestListDTO tdgRequestListDTO = null;
		if (reqId != null) {
			String statusDes = reqId;//System.getProperty("user.home")+("/Downloads/")+(reqId);
		
			//String statusDes = tdgRequestListDTO.getStatusdescription();

			response.setContentType("application/octet-stream");
			// response.setContentLength(2048);
			response.setHeader(
					"Content-Disposition",
					"attachment;filename=\""
							+ statusDes.substring(
									statusDes.lastIndexOf("/") + 1,
									statusDes.length()) + "\"");
			try {
				File f = new File(statusDes);
				byte[] arBytes = new byte[(int) f.length()];
				FileInputStream is = new FileInputStream(f);
				is.read(arBytes);
				ServletOutputStream op = response.getOutputStream();
				op.write(arBytes);
				op.flush();
				is.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		logger.info(strClassName + strMethodName
				+ " return from tdgDashBoardDetails get method ");
	}
	
	@RequestMapping(value = "/downloadTdgRequest", method = RequestMethod.GET)
	public ModelAndView downloadTdgRequest(
			@RequestParam(value = "dataFile", required = false) String dataFile,
			@RequestParam(value = "reqId", required = false) String reqId, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		String strMethodName = " [ downloadTdgRequest() ]";
		logger.info(strClassName + strMethodName + " inside of downloadTdgRequest get method ");
		TdgRequestListDTO tdgRequestListDTO = null;
		if (reqId != null) {
			tdgRequestListDTO = tdgOperationsService.getDashBoardRequestedRecords(Long
					.valueOf(request.getParameter("reqId")));
		}
		if (tdgRequestListDTO != null && tdgRequestListDTO.getConditions() != null
				&& dataFile != null && dataFile.equals(".xls")) {
			logger.info(strClassName + strMethodName
					+ " return from tdgDashBoardDetails get method ");
			return new ModelAndView("tdgExcelSearchResultListView", "tdgExcelSearchResultListDTOs",
					tdgRequestListDTO);
		} else if (tdgRequestListDTO != null && tdgRequestListDTO.getConditions() != null
				&& dataFile != null && dataFile.equals(".pdf")) {
			return new ModelAndView("pdfView", "tdgPdfSearchResultListDTOs", tdgRequestListDTO);
		}
		logger.info(strClassName + strMethodName + " return from tdgDashBoardDetails get method ");
		return null;
	}

	@RequestMapping(value = "/downloadTdgCSVRequest", method = RequestMethod.GET)
	public void downloadTdgCSVRequest(
			@RequestParam(value = "dataFile", required = false) String dataFile,
			@RequestParam(value = "reqId", required = false) String reqId, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) throws Exception{
		String strMethodName = " [ downloadTdgCSVRequest() ]";
		logger.info(strClassName + strMethodName + " inside of downloadTdgCSVRequest get method ");
		TdgRequestListDTO tdgRequestListDTO = null;
		if (reqId != null && dataFile != null && dataFile.equals(".csv")) {
			tdgRequestListDTO = tdgOperationsService.getDashBoardRequestedRecords(Long
					.valueOf(request.getParameter("reqId")));
			Map<String, String> colvalMap = new LinkedHashMap<String, String>();
			if (tdgRequestListDTO.getConditions() != null
					&& tdgRequestListDTO.getConditions().contains("#")) {
				String[] colvalue = tdgRequestListDTO.getConditions().split("\\#");
				for (int i = 0; i < colvalue.length; i++) {
					if (colvalue[i].contains(":")) {
						String[] strChildSplit = colvalue[i].split(":");
						if (strChildSplit.length > 1) {
							if (strChildSplit[0] != null && strChildSplit[1] != null) {
								colvalMap.put(strChildSplit[0], strChildSplit[1]);
							}
						} else if (strChildSplit.length == 1) {
							colvalMap.put(strChildSplit[0], null);
						} else {
							colvalMap.put(strChildSplit[i], null);
						}
					} else {
						colvalMap.put(colvalue[i], null);
					}
				}
			} else if (tdgRequestListDTO.getConditions() != null
					&& tdgRequestListDTO.getConditions().contains(":")) {
				String[] colvalue = tdgRequestListDTO.getConditions().split(":");
				if (colvalue.length > 1) {
					if (colvalue[0] != null && colvalue[1] != null) {
						colvalMap.put(colvalue[0], colvalue[1]);
					}
				} else if (colvalue.length == 1) {
					colvalMap.put(colvalue[0], null);
				}
			} else {
				colvalMap.put(tdgRequestListDTO.getConditions(), null);
			}
			response.setContentType("text/csv");
			String disposition = "attachment; fileName=downloadTdgRequest.csv";
			response.setHeader("Content-Disposition", disposition);
			response.getWriter().append(
					CSVGenerator.getCSV(colvalMap, tdgRequestListDTO.getRequestCount(),
							tdgRequestListDTO.getListGeneratedData()));
		}
	}
}
