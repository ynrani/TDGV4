/*
 * Object Name : TdgFlatFileController.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		5:26:21 PM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.tesda.model.DTO.FileUploadDTO;
import com.tesda.service.TDMAdminService;
import com.tesda.service.impl.TdgAsyncServiceImpl;
import com.tesda.util.TDGFlatFileSupport;
import com.tesda.util.TdgCentralConstant;
import com.tesda.util.TdgExcelOperationsUtil;

/**
 * @author vkrish14
 *
 */
@Controller
public class TdgFlatFileController{
	private static Logger logger = Logger.getLogger(TdgFlatFileController.class);
	private static String strClassName = " [ TdgFlatFileController ] ";
	@Resource(name = "tdgAsyncServiceImpl")
	TdgAsyncServiceImpl tdgAsyncServiceImpl;
	@Resource(name = "tDMAdminService")
	TDMAdminService tDMAdminService;
	
	@RequestMapping(value = "/tdgUploadFiles", method = RequestMethod.GET)
	public String displayForm(
			@ModelAttribute("uploadDTO") FileUploadDTO uploadDTO,
			Model map) {
		map.addAttribute("uploadDTO", uploadDTO);
		return "tdgFlatFilesUpload";
	}
	
	@RequestMapping(value = "/tdgUploadmtcFiles", method = RequestMethod.GET)
	public String displaymtcForm(
			@ModelAttribute("uploadDTO") FileUploadDTO uploadDTO,
			Model map) {
		uploadDTO = new FileUploadDTO();
		uploadDTO.setManualDictionary("MASTER");
		map.addAttribute("uploadDTO", uploadDTO);
		return "tdgFlatFilesmtcUpload";
	}
	
	@RequestMapping(value = "/tdgUploadmdFiles", method = RequestMethod.GET)
	public String displaymdForm(
			@ModelAttribute("uploadDTO") FileUploadDTO uploadDTO,
			Model map) {
		uploadDTO = new FileUploadDTO();
		uploadDTO.setManualDictionary("MANUAL");
		map.addAttribute("uploadDTO", uploadDTO);
		return "tdgFlatFilesmdUpload";
	}
	
	@RequestMapping(value = "/tdgUploadFiles", method = RequestMethod.POST)
	public String saveFlatFiles(
			@ModelAttribute("uploadDTO") FileUploadDTO uploadDTO,
					Model map) {
		logger.info(strClassName + " inside saveFlatFiles method");
		List<MultipartFile> files = uploadDTO.getFiles();
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		/*List<String> lstExtenstionCheck = new ArrayList<String>();
		lstExtenstionCheck.add(".xlsx");
		lstExtenstionCheck.add(".xls");*/

		//List<String> fileNames = new ArrayList<String>();
		boolean bFileType = false;
		
		Map<String,List<String>> mapResult = new HashMap<String,List<String>>();
		if(null != files && files.size() > 0) {
			for (MultipartFile multipartFile : files) {
				if(!multipartFile.getOriginalFilename().toLowerCase().endsWith(".xlsx") && !multipartFile.getOriginalFilename().toLowerCase().endsWith(".xls"))
					bFileType = true;
					break;
				}
				//Handle file content - multipartFile.getInputStream()
			if (!bFileType) {
				if (uploadDTO.getManualDictionary().equalsIgnoreCase("MASTER")) {
					//StringBuffer strFailedFiles = new StringBuffer("");
					TDGFlatFileSupport flatFileRead = new TDGFlatFileSupport();
					for (MultipartFile multipartFile : files) {
						try {
							Map<String, List<String>> mapRs = flatFileRead.readFile(
									multipartFile.getOriginalFilename(),
									multipartFile.getInputStream());
							mapResult.putAll(mapRs);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(!mapResult.isEmpty())
						try{
						tDMAdminService.saveTablesDetails(mapResult);
						FileUploadDTO dto = new FileUploadDTO();
						dto.setMessageConstant(TdgCentralConstant.SUCCESS_MESSAGE);
						dto.setMessage("Tables are created in system.....");
						map.addAttribute("uploadDTO", dto);
						}catch(Exception e){
							uploadDTO.setMessageConstant(TdgCentralConstant.FAILED_MESSAGE);
							uploadDTO.setMessage("Sheet name(s) related tables are already exist in the system");
							map.addAttribute("uploadDTO", uploadDTO);
						}
					return "tdgFlatFilesUpload";
				} else if(uploadDTO.getManualDictionary().equalsIgnoreCase("MANUAL")){
					// needs to call manual dictionary upload call
					TdgExcelOperationsUtil tdgExcelValues = new TdgExcelOperationsUtil();
					StringBuffer strFailedFiles = new StringBuffer("");
					for (MultipartFile multipartFile : files) {
						try {
							Map<String, List<String>> mapRs = tdgExcelValues.readFile(
									multipartFile.getOriginalFilename(),
									multipartFile.getInputStream());
							String tabName = multipartFile.getOriginalFilename().toUpperCase();
							if (tabName.toUpperCase().contains(".")) {
								tabName = tabName.substring(0, tabName.indexOf(".")).toUpperCase();
							}
							tDMAdminService.saveManualDictionaryDetails(
									"MANUAL_"+tabName, mapRs, null);
							tdgAsyncServiceImpl.doDumpManualDictionaryValues("MANUAL_"+tabName, mapRs);
						} catch (IOException e) {
							if(strFailedFiles.toString().trim().isEmpty())
							strFailedFiles.append(" Following files got fail while upload into TDG are ").append(multipartFile.getName());
							else
								strFailedFiles.append(",").append(multipartFile.getName());
							e.printStackTrace();
						}catch(Exception e){
							strFailedFiles.append(" Manual Dictionary names are already exist");
						}
					}
					if(strFailedFiles.toString().trim().isEmpty()){
						FileUploadDTO dto = new FileUploadDTO();
						dto.setMessageConstant(TdgCentralConstant.SUCCESS_MESSAGE);
						dto.setMessage("Manual Dictionaries are uploaded sucessfully...");
						map.addAttribute("uploadDTO", dto);
						return "tdgFlatFilesUpload";
					}else{
						uploadDTO.setMessageConstant(TdgCentralConstant.FAILED_MESSAGE);
						uploadDTO.setMessage(strFailedFiles.toString());
						map.addAttribute("uploadDTO", uploadDTO);
						return "tdgFlatFilesUpload";
					}
						
				}else{

					// needs to call manual dictionary upload call
					TdgExcelOperationsUtil tdgExcelValues = new TdgExcelOperationsUtil();
					StringBuffer strFailedFiles = new StringBuffer("");
					for (MultipartFile multipartFile : files) {
						try {
							Map<String, List<String>> mapRs = tdgExcelValues.readFile(
									multipartFile.getOriginalFilename(),
									multipartFile.getInputStream());
							
							tdgAsyncServiceImpl.doDumpTableAliasValues(mapRs,user.getUsername());
						} catch (IOException e) {
							if(strFailedFiles.toString().trim().isEmpty())
							strFailedFiles.append(" Following files got fail while upload into TDG are ").append(multipartFile.getName());
							else
								strFailedFiles.append(",").append(multipartFile.getName());
							e.printStackTrace();
						}catch(Exception e){
							strFailedFiles.append(" Details already Exist");
						}
					}
					if(strFailedFiles.toString().trim().isEmpty()){
						FileUploadDTO dto = new FileUploadDTO();
						dto.setMessageConstant(TdgCentralConstant.SUCCESS_MESSAGE);
						dto.setMessage("Table alias uploaded sucessfully...");
						map.addAttribute("uploadDTO", dto);
						return "tdgFlatFilesUpload";
					}else{
						uploadDTO.setMessageConstant(TdgCentralConstant.FAILED_MESSAGE);
						uploadDTO.setMessage(strFailedFiles.toString());
						map.addAttribute("uploadDTO", uploadDTO);
						return "tdgFlatFilesUpload";
					}
						
				
					
				}
			}else{
				uploadDTO.setMessageConstant(TdgCentralConstant.FAILED_MESSAGE);
				uploadDTO.setMessage("Atleast one file is required or files should be excel format.....");
				map.addAttribute("uploadDTO", uploadDTO);
				return "tdgFlatFilesUpload";
			}
		}
		map.addAttribute("uploadDTO", uploadDTO);
		logger.info(strClassName + " return from saveFlatFiles method");
		return "tdgFlatFilesUpload";
	}
	
	@RequestMapping(value = "/tdgUploadmtcFiles", method = RequestMethod.POST)
	public String saveFlatmtcFiles(
			@ModelAttribute("uploadDTO") FileUploadDTO uploadDTO,
					Model map) {
		logger.info(strClassName + " inside saveFlatFiles method");
		List<MultipartFile> files = uploadDTO.getFiles();
		//User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		/*List<String> lstExtenstionCheck = new ArrayList<String>();
		lstExtenstionCheck.add(".xlsx");
		lstExtenstionCheck.add(".xls");*/

		//List<String> fileNames = new ArrayList<String>();
		boolean bFileType = false;
		
		Map<String,List<String>> mapResult = new HashMap<String,List<String>>();
		if(null != files && files.size() > 0) {
			for (MultipartFile multipartFile : files) {
				if(!multipartFile.getOriginalFilename().toLowerCase().endsWith(".xlsx") && !multipartFile.getOriginalFilename().toLowerCase().endsWith(".xls"))
					bFileType = true;
					break;
				}
				//Handle file content - multipartFile.getInputStream()
			if (!bFileType) {
				if (uploadDTO.getManualDictionary().equalsIgnoreCase("MASTER")) {
					//StringBuffer strFailedFiles = new StringBuffer("");
					TDGFlatFileSupport flatFileRead = new TDGFlatFileSupport();
					for (MultipartFile multipartFile : files) {
						try {
							Map<String, List<String>> mapRs = flatFileRead.readFile(
									multipartFile.getOriginalFilename(),
									multipartFile.getInputStream());
							mapResult.putAll(mapRs);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(!mapResult.isEmpty())
						try{
						tDMAdminService.saveTablesDetails(mapResult);
						FileUploadDTO dto = new FileUploadDTO();
						dto.setMessageConstant(TdgCentralConstant.SUCCESS_MESSAGE);
						dto.setMessage("Tables are created in system.....");
						map.addAttribute("uploadDTO", dto);
						}catch(Exception e){
							uploadDTO.setMessageConstant(TdgCentralConstant.FAILED_MESSAGE);
							uploadDTO.setMessage("Sheet name(s) related tables are already exist in the system");
							map.addAttribute("uploadDTO", uploadDTO);
						}
					return "tdgFlatFilesmtcUpload";
				}
			}else{
				uploadDTO.setMessageConstant(TdgCentralConstant.FAILED_MESSAGE);
				uploadDTO.setMessage("Atleast one file is required or files should be excel format.....");
				map.addAttribute("uploadDTO", uploadDTO);
				return "tdgFlatFilesmtcUpload";
			}
		}
		map.addAttribute("uploadDTO", uploadDTO);
		logger.info(strClassName + " return from saveFlatFiles method");
		return "tdgFlatFilesmtcUpload";
	}
	
	@RequestMapping(value = "/tdgUploadmdFiles", method = RequestMethod.POST)
	public String saveFlatmdFiles(
			@ModelAttribute("uploadDTO") FileUploadDTO uploadDTO,
					Model map) {
		logger.info(strClassName + " inside saveFlatFiles method");
		List<MultipartFile> files = uploadDTO.getFiles();
		//User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		/*List<String> lstExtenstionCheck = new ArrayList<String>();
		lstExtenstionCheck.add(".xlsx");
		lstExtenstionCheck.add(".xls");*/

		//List<String> fileNames = new ArrayList<String>();
		boolean bFileType = false;
		
		//Map<String,List<String>> mapResult = new HashMap<String,List<String>>();
		if(null != files && files.size() > 0) {
			for (MultipartFile multipartFile : files) {
				if(!multipartFile.getOriginalFilename().toLowerCase().endsWith(".xlsx") && !multipartFile.getOriginalFilename().toLowerCase().endsWith(".xls"))
					bFileType = true;
					break;
				}
				//Handle file content - multipartFile.getInputStream()
			if (!bFileType) {
				if(uploadDTO.getManualDictionary().equalsIgnoreCase("MANUAL")){
					// needs to call manual dictionary upload call
					TdgExcelOperationsUtil tdgExcelValues = new TdgExcelOperationsUtil();
					StringBuffer strFailedFiles = new StringBuffer("");
					for (MultipartFile multipartFile : files) {
						try {
							Map<String, List<String>> mapRs = tdgExcelValues.readFile(
									multipartFile.getOriginalFilename(),
									multipartFile.getInputStream());
							String tabName = multipartFile.getOriginalFilename().toUpperCase();
							if (tabName.toUpperCase().contains(".")) {
								tabName = tabName.substring(0, tabName.indexOf(".")).toUpperCase();
							}
							tDMAdminService.saveManualDictionaryDetails(
									"MANUAL_"+tabName, mapRs, null);
							tdgAsyncServiceImpl.doDumpManualDictionaryValues("MANUAL_"+tabName, mapRs);
						} catch (IOException e) {
							if(strFailedFiles.toString().trim().isEmpty())
							strFailedFiles.append(" Following files got fail while upload into TDG are ").append(multipartFile.getName());
							else
								strFailedFiles.append(",").append(multipartFile.getName());
							e.printStackTrace();
						}catch(Exception e){
							strFailedFiles.append(" Manual Dictionary names are already exist");
						}
					}
					if(strFailedFiles.toString().trim().isEmpty()){
						FileUploadDTO dto = new FileUploadDTO();
						dto.setMessageConstant(TdgCentralConstant.SUCCESS_MESSAGE);
						dto.setMessage("Manual Dictionaries are uploaded sucessfully...");
						map.addAttribute("uploadDTO", dto);
						return "tdgFlatFilesmdUpload";
					}else{
						uploadDTO.setMessageConstant(TdgCentralConstant.FAILED_MESSAGE);
						uploadDTO.setMessage(strFailedFiles.toString());
						map.addAttribute("uploadDTO", uploadDTO);
						return "tdgFlatFilesmdUpload";
					}
				}
						
			}else{
				uploadDTO.setMessageConstant(TdgCentralConstant.FAILED_MESSAGE);
				uploadDTO.setMessage("Atleast one file is required or files should be excel format.....");
				map.addAttribute("uploadDTO", uploadDTO);
				return "tdgFlatFilesmdUpload";
			}
		}
		map.addAttribute("uploadDTO", uploadDTO);
		logger.info(strClassName + " return from saveFlatFiles method");
		return "tdgFlatFilesmdUpload";
	}
}
