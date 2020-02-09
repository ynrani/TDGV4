/*
 * Object Name : TdgDictionariesController.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		11:00:27 AM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.controller;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tesda.model.DTO.DbConnectionsDTO;
import com.tesda.model.DTO.TdgGuiDetailsDTO;
import com.tesda.model.DTO.TdgMasterDictionaryDTO;
import com.tesda.model.DTO.TdgSchemaDTO;
import com.tesda.service.TdgDictionariesService;
import com.tesda.util.AppConstant;
import com.tesda.util.TdgCentralConstant;

/**
 * @author vkrish14
 *
 */
@Controller
public class TdgDictionariesController{
	private static Logger logger = Logger.getLogger(TdgDictionariesController.class);
	private static String strClassName = " [ TdgDictionariesController ] ";
	public static final String DB_ERROR = "Database connection error....kindly check database details...";
	@Resource(name = "tdgDictionariesService")
	TdgDictionariesService tdgDictionariesService;
	List<DbConnectionsDTO> lstAvailableConnections = null;
	TdgMasterDictionaryDTO tdgMasterDictionaryDTOTemp = null;
	
	
	@RequestMapping(value = "/tdgaCreateMasterDictionary",  method = RequestMethod.GET)
	public String createMasterDictionary(
			@ModelAttribute("tdgMasterDictionaryDTO")TdgMasterDictionaryDTO tdgMasterDictionaryDTO, ModelMap model){
		logger.info(strClassName+" ~ createMasterDictionary ~ inside method");
		lstAvailableConnections = tdgDictionariesService.getAvailableConnections();
		List<String> lstCons = new ArrayList<String>();
		if(lstAvailableConnections != null && !lstAvailableConnections.isEmpty()){
			for(DbConnectionsDTO dto : lstAvailableConnections){
				lstCons.add(dto.getDisplayName());
			}
		}
		if(model.get("schemaId") == null && tdgMasterDictionaryDTO != null && StringUtils.isNotEmpty(tdgMasterDictionaryDTO.getConSelected()) && StringUtils.isNotEmpty(tdgMasterDictionaryDTO.getDictionaryName())){
			//tdgMasterDictionaryDTO.setTdgConnections(lstCons);
			if(tdgMasterDictionaryDTOTemp !=  null && !tdgMasterDictionaryDTOTemp.isEditFlag()){
			tdgMasterDictionaryDTOTemp = tdgMasterDictionaryDTO;
			model.addAttribute("tdgMasterDictionaryDTO", tdgMasterDictionaryDTO);
			}else{
				model.addAttribute("tdgMasterDictionaryDTO", tdgMasterDictionaryDTOTemp);
			}
			logger.info(strClassName+" ~ createMasterDictionary ~ returned from method");
			return "redirect:tdgaNextCreateMasterDictionary"; 
			//return "tdgCreateDictionaryPage2";
		}else{
			if(model.get("schemaId") != null){
				if(tdgMasterDictionaryDTOTemp == null)
					tdgMasterDictionaryDTOTemp = tdgMasterDictionaryDTO;
				tdgMasterDictionaryDTO.setEditFlag(true);
				long lSchemaId = Long.parseLong(model.get("schemaId").toString());
				TdgSchemaDTO schemaDTO = tdgDictionariesService
						.getSchemaDetails(lSchemaId);
				//String url = schemaDTO.getUrl().replaceAll("#", "TDGCON");
				String url = schemaDTO.getDataconnections().replaceAll("#", "TDGCON");
				StringBuffer strFinalUrl = new StringBuffer();
				if(url.contains("TDGCON")){
					String[] strUrls = url.split("TDGCON");
					for(int i =0;i<strUrls.length;i++){
						for(DbConnectionsDTO dto : lstAvailableConnections){
							//if(strUrls[i].equalsIgnoreCase(getUrl(dto).toString())){
							if(strUrls[i].equalsIgnoreCase(dto.getDisplayName())){
								if(strFinalUrl.length()>0)
									strFinalUrl.append("TDGCON");
								strFinalUrl.append(dto.getDisplayName());break;
							}
						}
					}
				}else{
					for(DbConnectionsDTO dto : lstAvailableConnections){
						if(url.equalsIgnoreCase(dto.getDisplayName())){
							strFinalUrl.append(dto.getDisplayName());break;
						}
					}
				}
				tdgMasterDictionaryDTO.setConSelected(strFinalUrl.toString());
				//tdgMasterDictionaryDTOTemp = tdgMasterDictionaryDTO;
				//started being fetch update records of respective dictionary 
				if(StringUtils.isNotEmpty(schemaDTO.getBusinessrules()))
				tdgMasterDictionaryDTOTemp.setBusinessRules(schemaDTO.getBusinessrules().endsWith(";")?schemaDTO.getBusinessrules().substring(0, schemaDTO.getBusinessrules().lastIndexOf(";")):schemaDTO.getBusinessrules());
				else
				tdgMasterDictionaryDTOTemp.setBusinessRules("");
				tdgMasterDictionaryDTOTemp.setDateFormates(schemaDTO.getDateformate());
				if(StringUtils.isNotEmpty(schemaDTO.getColumnsdepends()))
				tdgMasterDictionaryDTOTemp.setDependentDbs(schemaDTO.getColumnsdepends().endsWith(";")?schemaDTO.getColumnsdepends().substring(0, schemaDTO.getColumnsdepends().lastIndexOf(";")):schemaDTO.getColumnsdepends());
				else
					tdgMasterDictionaryDTOTemp.setDependentDbs("");
				tdgMasterDictionaryDTOTemp.setDictionaryName(schemaDTO.getSchemaname());
				tdgMasterDictionaryDTOTemp.setEditFlag(true);
				if(StringUtils.isNotEmpty(schemaDTO.getSchemamastertables()))
				tdgMasterDictionaryDTOTemp.setMasterTabs(schemaDTO.getSchemamastertables().endsWith(";")?(schemaDTO.getSchemamastertables().substring(0, schemaDTO.getSchemamastertables().lastIndexOf(";"))):schemaDTO.getSchemamastertables());
				else
					tdgMasterDictionaryDTOTemp.setMasterTabs("");
				if(StringUtils.isNotEmpty(tdgMasterDictionaryDTOTemp.getMasterTabs())){
					if( !tdgMasterDictionaryDTOTemp.getMasterTabs().contains("TDG_MASTER_TABS,"))
						tdgMasterDictionaryDTOTemp.setMasterTabs("TDG_MASTER_TABS,"+tdgMasterDictionaryDTOTemp.getMasterTabs());
					else
						tdgMasterDictionaryDTOTemp.setMasterTabs(tdgMasterDictionaryDTOTemp.getMasterTabs());
				}
				if(StringUtils.isNotEmpty(schemaDTO.getSchemapasstabs()))
				tdgMasterDictionaryDTOTemp.setPassedTabs(schemaDTO.getSchemapasstabs().endsWith(";")?""+schemaDTO.getSchemapasstabs().substring(0, schemaDTO.getSchemapasstabs().lastIndexOf(";")):schemaDTO.getSchemapasstabs());
				else
					tdgMasterDictionaryDTOTemp.setPassedTabs("");
				if(StringUtils.isNotEmpty(tdgMasterDictionaryDTOTemp.getPassedTabs())){
					if( !tdgMasterDictionaryDTOTemp.getPassedTabs().contains("TDG_PASSED_TABS,"))
						tdgMasterDictionaryDTOTemp.setPassedTabs("TDG_PASSED_TABS,"+tdgMasterDictionaryDTOTemp.getPassedTabs());
					else
						tdgMasterDictionaryDTOTemp.setPassedTabs(tdgMasterDictionaryDTOTemp.getPassedTabs());
					
				}
				if(StringUtils.isNotEmpty(schemaDTO.getSeqtableprefix()))
				tdgMasterDictionaryDTOTemp.setSequencePrefixTabs(schemaDTO.getSeqtableprefix().endsWith(";")?schemaDTO.getSeqtableprefix().substring(0, schemaDTO.getSeqtableprefix().lastIndexOf(";")):schemaDTO.getSeqtableprefix());
				else
					tdgMasterDictionaryDTOTemp.setSequencePrefixTabs("");
				tdgMasterDictionaryDTOTemp.setTdgConnections(lstCons);
				if(strFinalUrl != null && StringUtils.isNotEmpty(strFinalUrl.toString())){
				tdgMasterDictionaryDTOTemp.setConSelected(strFinalUrl.toString());
				}
				StringBuffer strBuffer = new StringBuffer();
				//String strPassedTabs = props.getProperty(TdgCentralConstant.SCHEMA_PASSED_TABS);
				List<String> listPassedTabs = new ArrayList<String>();
				if (StringUtils.isNotEmpty(schemaDTO.getSchemapasstabs())) {
					if (schemaDTO.getSchemapasstabs().contains(";")) {
						String strArrays[] = schemaDTO.getSchemapasstabs().split(";");
						if (strArrays.length >= 1) {
							if (strArrays[0].contains(",")) {
								String[] strMasterTabs = strArrays[0].split(",");
								for (String strVal : strMasterTabs) {
									listPassedTabs.add(strVal.toUpperCase().trim());
								}
							} else {
								listPassedTabs.add(strArrays[0].toUpperCase().trim());
							}
						}
					}
				}
				for(TdgGuiDetailsDTO dtos : schemaDTO.getTdgGuiDetailsDTOs()){
					//StringBuffer strBuffer = new StringBuffer();
					//name+"#"+displaylabel+"#"+type+"#"+values
					if(dtos.getColumnname() != null){
					if(strBuffer.length()>0)
						strBuffer.append("$");
					strBuffer.append(dtos.getColumnname().contains("#") ? dtos.getColumnname().replaceAll("#", "TDG_MT") : dtos.getColumnname()).append("#").append(StringUtils.isNotEmpty(dtos.getColumnCondition())?dtos.getColumnCondition():"No").append("#").append(dtos.getColumnLabel()).append("#").append(dtos.getColumnType()).append("#").append(StringUtils.isNotEmpty(dtos.getColumnValues()) ? dtos.getColumnValues() : "");
					}
				}
				tdgMasterDictionaryDTOTemp.setEditSchemeDetails(strBuffer.toString());
				List<String> listColumnsFetched = tdgDictionariesService.getColsByTabs(schemaDTO.getUrl(),schemaDTO.getUsername(),schemaDTO.getPassword(),listPassedTabs);
				tdgMasterDictionaryDTOTemp.setListColumns(listColumnsFetched);
				tdgMasterDictionaryDTO= tdgMasterDictionaryDTOTemp;
				//end of fetching
			}else{
				tdgMasterDictionaryDTOTemp= tdgMasterDictionaryDTO;
			}
		/*lstAvailableConnections = tdgDictionariesService.getAvailableConnections();
		List<String> lstCons = new ArrayList<String>();
		if(lstAvailableConnections != null && !lstAvailableConnections.isEmpty()){
			for(DbConnectionsDTO dto : lstAvailableConnections){
				lstCons.add(dto.getDisplayName());
			}
		}*/
		tdgMasterDictionaryDTO.setTdgConnections(lstCons);
			
		model.addAttribute("tdgMasterDictionaryDTO", tdgMasterDictionaryDTO);
		logger.info(strClassName+" ~ createMasterDictionary ~ returned from method");
		return "tdgCreateDictionaryPage1";
		}
		//logger.info(strClassName+" ~ createMasterDictionary ~ returned from method");
		//return "tdgCreateDictionaryPage1";
	}

	@RequestMapping(value = "/tdgaNextCreateMasterDictionary", method = RequestMethod.GET)
	public String createNextStepMasterDictionary(
			@ModelAttribute("tdgMasterDictionaryDTO") TdgMasterDictionaryDTO tdgMasterDictionaryDTO,
			ModelMap model){
		logger.info(strClassName + " ~ createNextStepMasterDictionary ~ inside method");
		// lstAvailableConnections = tdgDictionariesService.getAvailableConnections();
		// List<String> lstCons = new ArrayList<String>();
		if(tdgMasterDictionaryDTO !=null){
			tdgMasterDictionaryDTO = tdgMasterDictionaryDTOTemp;
			/*if(StringUtils.isNotEmpty(tdgMasterDictionaryDTOTemp.getPassedTabs()) || StringUtils.isNotEmpty(tdgMasterDictionaryDTOTemp.getMasterTabs()) || StringUtils.isNotEmpty(tdgMasterDictionaryDTOTemp.getRequiredCols())){
				tdgMasterDictionaryDTO = tdgMasterDictionaryDTOTemp;
			}else{*/
				if(lstAvailableConnections == null || lstAvailableConnections.isEmpty()){
					lstAvailableConnections = tdgDictionariesService.getAvailableConnections();			
				}
				if(tdgMasterDictionaryDTO.getListTables() == null || tdgMasterDictionaryDTO.getListTables().isEmpty()){
				StringBuffer url = new StringBuffer();
				StringBuffer username = new StringBuffer();
				StringBuffer password = new StringBuffer();
				//tdgMasterDictionaryDTO  = tdgMasterDictionaryDTOTemp;
				if(tdgMasterDictionaryDTO.getConSelected().trim().contains("TDGCON")){
					String[] strSplits = tdgMasterDictionaryDTO.getConSelected().split("TDGCON");
					for(int i=0;i<strSplits.length;i++){
						for(DbConnectionsDTO dto : lstAvailableConnections){
							if(dto.getDisplayName().equalsIgnoreCase(strSplits[i])){
								if(url.length() > 0){
									url.append("#");
									username.append("#");
									password.append("#");
								}
								url.append(getUrl(dto));
								username.append(dto.getUser());
								password.append(dto.getPass());
								break;
							}
						}
					}
				}else{
					for(DbConnectionsDTO dto : lstAvailableConnections){
						if(dto.getDisplayName().equalsIgnoreCase(tdgMasterDictionaryDTO.getConSelected())){						
							url.append(getUrl(dto));
							username.append(dto.getUser());
							password.append(dto.getPass());
							break;
						}
					}
				}
				//List<String> listTables = tdgDictionariesService.getAllTabs(url.toString(),username.toString(),password.toString());
				List<String> listFinalTabs = new ArrayList<String>();
				listFinalTabs.add("All");
				listFinalTabs.addAll(tdgDictionariesService.getAllTabs(url.toString(),username.toString(),password.toString()));
				tdgMasterDictionaryDTO.setListTables(listFinalTabs);
				}
			//}
			/*if(StringUtils.isNotEmpty(tdgMasterDictionaryDTOTemp.getPassedTabs()) || StringUtils.isNotEmpty(tdgMasterDictionaryDTOTemp.getMasterTabs()) || StringUtils.isNotEmpty(tdgMasterDictionaryDTOTemp.getRequiredCols())){
				tdgMasterDictionaryDTO = tdgMasterDictionaryDTOTemp;
			}else{
				if(lstAvailableConnections == null || lstAvailableConnections.isEmpty()){
					lstAvailableConnections = tdgDictionariesService.getAvailableConnections();			
				}
				StringBuffer url = new StringBuffer();
				StringBuffer username = new StringBuffer();
				StringBuffer password = new StringBuffer();
				tdgMasterDictionaryDTO  = tdgMasterDictionaryDTOTemp;
				if(tdgMasterDictionaryDTO.getConSelected().trim().contains("TDGCON")){
					String[] strSplits = tdgMasterDictionaryDTO.getConSelected().split("TDGCON");
					for(int i=0;i<strSplits.length;i++){
						for(DbConnectionsDTO dto : lstAvailableConnections){
							if(dto.getDisplayName().equalsIgnoreCase(strSplits[i])){
								if(url.length() > 0){
									url.append("#");
									username.append("#");
									password.append("#");
								}
								url.append(getUrl(dto));
								username.append(dto.getUser());
								password.append(dto.getPass());
								break;
							}
						}
					}
				}else{
					for(DbConnectionsDTO dto : lstAvailableConnections){
						if(dto.getDisplayName().equalsIgnoreCase(tdgMasterDictionaryDTO.getConSelected())){						
							url.append(getUrl(dto));
							username.append(dto.getUser());
							password.append(dto.getPass());
							break;
						}
					}
				}
				//List<String> listTables = tdgDictionariesService.getAllTabs(url.toString(),username.toString(),password.toString());
				List<String> listFinalTabs = new ArrayList<String>();
				listFinalTabs.add("All");
				listFinalTabs.addAll(tdgDictionariesService.getAllTabs(url.toString(),username.toString(),password.toString()));
				tdgMasterDictionaryDTO.setListTables(listFinalTabs);
			}*/
		}
		model.addAttribute("tdgMasterDictionaryDTO", tdgMasterDictionaryDTO);
		logger.info(strClassName + " ~ createNextStepMasterDictionary ~ returned from method");
		return "tdgCreateDictionaryPage2";
	}

	@RequestMapping(value = "/tdgaNextCreateMasterDictionary", method = RequestMethod.POST)
	public String createNextStepMasterDictionary2(@RequestParam(required = false, value = "reqVals") String reqVals,
			@ModelAttribute("tdgMasterDictionaryDTO") TdgMasterDictionaryDTO tdgMasterDictionaryDTO,
			ModelMap model){
		//System.out.println(tdgMasterDictionaryDTOTemp);
		//User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//if(StringUtils.isEmpty(reqVals)){
		try{
		List<String> listTabs = new ArrayList<String>();
		if(tdgMasterDictionaryDTOTemp != null && StringUtils.isNotEmpty(tdgMasterDictionaryDTOTemp.getPassedTabs())){
			if(tdgMasterDictionaryDTOTemp.getPassedTabs().contains(",")){
				String[] splits = tdgMasterDictionaryDTOTemp.getPassedTabs().split(",");
				for(int i=0;i<splits.length;i++){
					if(!"All".equalsIgnoreCase(splits[i]) && !"TDG_PASSED_TABS".equalsIgnoreCase(splits[i]))
					{
					
					if(splits[i].contains("."))
					{
						//String[] tableWithSchema =splits[i].split("[.]");
						
						
						listTabs.add(splits[i]);
						
					}
					}
					}
			}else
			{
				if(tdgMasterDictionaryDTOTemp.getPassedTabs().contains(".")){
				String[] tableWithSchema = tdgMasterDictionaryDTOTemp.getPassedTabs().split("[.]");
			
					listTabs.add(tdgMasterDictionaryDTOTemp.getPassedTabs());
				
				//listTabs.add(tableWithSchema[1]);
				}
				
				//listTabs.add(tdgMasterDictionaryDTOTemp.getPassedTabs());
			}
			}
		StringBuffer url = new StringBuffer();
		StringBuffer username = new StringBuffer();
		StringBuffer password = new StringBuffer();
		//tdgMasterDictionaryDTO  = tdgMasterDictionaryDTOTemp;
		if(tdgMasterDictionaryDTOTemp.getConSelected().trim().contains("TDGCON")){
			String[] strSplits = tdgMasterDictionaryDTOTemp.getConSelected().split("TDGCON");
			for(int i=0;i<strSplits.length;i++){
				for(DbConnectionsDTO dto : lstAvailableConnections){
					if(dto.getDisplayName().equalsIgnoreCase(strSplits[i])){
						if(url.length() > 0){
							url.append("#");
							username.append("#");
							password.append("#");
						}
						url.append(getUrl(dto));
						username.append(dto.getUser());
						password.append(dto.getPass());
						break;
					}
				}
			}
		}else{
			for(DbConnectionsDTO dto : lstAvailableConnections){
				if(dto.getDisplayName().equalsIgnoreCase(tdgMasterDictionaryDTOTemp.getConSelected())){						
					url.append(getUrl(dto));
					username.append(dto.getUser());
					password.append(dto.getPass());
					break;
				}
			}
		}
		List<String> listColumnsFetched = tdgDictionariesService.getColsByTabs(url.toString(),username.toString(),password.toString(),listTabs);
		tdgMasterDictionaryDTO.setListColumns(listColumnsFetched);
		tdgMasterDictionaryDTOTemp.setListColumns(listColumnsFetched);
		}catch(Exception e){
			tdgMasterDictionaryDTO.setMessageConstant(TdgCentralConstant.FAILED_MESSAGE);
			tdgMasterDictionaryDTO.setMessage(TdgDictionariesController.DB_ERROR);
		}
		model.addAttribute("tdgMasterDictionaryDTO", tdgMasterDictionaryDTO);
		logger.info(strClassName + " ~ createNextStepMasterDictionary2 ~ returned from method");
		return "tdgCreateDictionaryPage2";
		}
		//}/*else{
			
			/*
			TdgSchemaDTO tdgSchemaDTO = new TdgSchemaDTO();
			Set<TdgGuiDetailsDTO> setTdgGuiDetailsDTOs = new HashSet<TdgGuiDetailsDTO>();
			tdgSchemaDTO.setUrl(url.toString());
			tdgSchemaDTO.setUsername(username.toString());
			tdgSchemaDTO.setPassword(password.toString());
			tdgSchemaDTO.setColumnsdepends(tdgMasterDictionaryDTOTemp.getDependentDbs());
			tdgSchemaDTO.setSchemamastertables(tdgMasterDictionaryDTOTemp.getMasterTabs());
			tdgSchemaDTO.setSeqtableprefix(tdgMasterDictionaryDTOTemp.getSequencePrefixTabs());
			tdgSchemaDTO.setSchemapasstabs(tdgMasterDictionaryDTOTemp.getPassedTabs());
			tdgSchemaDTO.setUserid(user.getUsername());
			tdgSchemaDTO.setDateformate(tdgMasterDictionaryDTOTemp.getDateFormates());
			tdgSchemaDTO.setRequiredcolumns(tdgMasterDictionaryDTOTemp.getRequiredCols());
			tdgSchemaDTO.setBusinessrules(tdgMasterDictionaryDTOTemp.getBusinessRules());
			tdgSchemaDTO.setSchemaname(tdgMasterDictionaryDTOTemp.getDictionaryName());
			if(reqVals.contains("$")){
				String[] strColumns = reqVals.split("$");
				for(int i=0;i<strColumns.length;i++){
					TdgGuiDetailsDTO tdgGuiDetailsDTO = new TdgGuiDetailsDTO();
					if(strColumns[i].contains("#")){
						String[] strValues = strColumns[i].split("#");
						//for(int j=0;j<4;j++){
							tdgGuiDetailsDTO.setColumnname(strValues[0]);
							tdgGuiDetailsDTO.setColumnLabel(strValues[1]);
							tdgGuiDetailsDTO.setColumnType(strValues[2]);
							if(strValues.length == 4)
								tdgGuiDetailsDTO.setColumnValues(strValues[3]);
							
						//}
					}
				}
			}else{
				TdgGuiDetailsDTO tdgGuiDetailsDTO = new TdgGuiDetailsDTO();
				if(reqVals.contains("#")){
					String[] strValues = reqVals.split("#");
					//for(int j=0;j<4;j++){
						tdgGuiDetailsDTO.setColumnname(strValues[0]);
						tdgGuiDetailsDTO.setColumnLabel(strValues[1]);
						tdgGuiDetailsDTO.setColumnType(strValues[2]);
						if(strValues.length == 4)
							tdgGuiDetailsDTO.setColumnValues(strValues[3]);
			}			
			tdgSchemaDTO.setTdgGuiDetailsDTOs(setTdgGuiDetailsDTOs);
			String strMessage = tdgDictionariesService.saveTdgSchemaDetails(tdgSchemaDTO);
			tdgMasterDictionaryDTO.set
			model.addAttribute("tdgMasterDictionaryDTO", tdgMasterDictionaryDTO);
			logger.info(strClassName + " ~ createMasterDictionary ~ returned from method");
			return "tdgCreateDictionaryPage2";
			}*/
		//}
		
		@RequestMapping(value = "/tdgaFinalCreateMasterDictionary", method = RequestMethod.GET)
		public @ResponseBody String createFinalStepMasterDictionary2(@RequestParam(required = false, value = "reqVals") String reqVals,
				@ModelAttribute("tdgMasterDictionaryDTO") TdgMasterDictionaryDTO tdgMasterDictionaryDTO,
				ModelMap model){
			//System.out.println(tdgMasterDictionaryDTOTemp);
			StringBuffer strSuccess = new StringBuffer();
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(StringUtils.isNotEmpty(reqVals)){
				StringBuffer url = new StringBuffer();
				StringBuffer username = new StringBuffer();
				StringBuffer password = new StringBuffer();
				//tdgMasterDictionaryDTO  = tdgMasterDictionaryDTOTemp;
				if(tdgMasterDictionaryDTOTemp.getConSelected().trim().contains("TDGCON")){
					String[] strSplits = tdgMasterDictionaryDTOTemp.getConSelected().split("TDGCON");
					for(int i=0;i<strSplits.length;i++){
						for(DbConnectionsDTO dto : lstAvailableConnections){
							if(dto.getDisplayName().equalsIgnoreCase(strSplits[i])){
								if(url.length() > 0){
									url.append("#");
									username.append("#");
									password.append("#");
								}
								url.append(getUrl(dto));
								username.append(dto.getUser());
								password.append(dto.getPass());
								break;
							}
						}
					}
				}else{
					for(DbConnectionsDTO dto : lstAvailableConnections){
						if(dto.getDisplayName().equalsIgnoreCase(tdgMasterDictionaryDTOTemp.getConSelected())){						
							url.append(getUrl(dto));
							username.append(dto.getUser());
							password.append(dto.getPass());
							break;
						}
					}
				}
				TdgSchemaDTO tdgSchemaDTO = new TdgSchemaDTO();
				Set<TdgGuiDetailsDTO> setTdgGuiDetailsDTOs = new LinkedHashSet<TdgGuiDetailsDTO>();
				tdgSchemaDTO.setUrl(url.toString());
				tdgSchemaDTO.setUsername(username.toString());
				tdgSchemaDTO.setPassword(password.toString());
				tdgSchemaDTO.setColumnsdepends(tdgMasterDictionaryDTOTemp.getDependentDbs());
				tdgSchemaDTO.setSchemamastertables(tdgMasterDictionaryDTOTemp.getMasterTabs()+";");
				tdgSchemaDTO.setSeqtableprefix(tdgMasterDictionaryDTOTemp.getSequencePrefixTabs()+";");
				tdgSchemaDTO.setSchemapasstabs(tdgMasterDictionaryDTOTemp.getPassedTabs()+";");
				tdgSchemaDTO.setUserid(user.getUsername());
				tdgSchemaDTO.setDateformate(tdgMasterDictionaryDTOTemp.getDateFormates());
				tdgSchemaDTO.setRequiredcolumns(tdgMasterDictionaryDTOTemp.getRequiredCols());
				tdgSchemaDTO.setBusinessrules(tdgMasterDictionaryDTOTemp.getBusinessRules());
				tdgSchemaDTO.setSchemaname(tdgMasterDictionaryDTOTemp.getDictionaryName());
				tdgSchemaDTO.setDataconnections(tdgMasterDictionaryDTOTemp.getConSelected());
				if(reqVals.contains("TDG_DOLLOR")){
					String[] strColumns = reqVals.split("TDG_DOLLOR");
					for(int i=0;i<strColumns.length;i++){
						TdgGuiDetailsDTO tdgGuiDetailsDTO = new TdgGuiDetailsDTO();
						if(strColumns[i].contains("TDG_HASH")){
							String[] strValues = strColumns[i].split("TDG_HASH");
							//for(int j=0;j<4;j++){
								tdgGuiDetailsDTO.setColumnname(strValues[0].contains("TDG_MT") ? strValues[0].replaceAll("TDG_MT", "#"):strValues[0]);
								tdgGuiDetailsDTO.setColumnCondition(strValues[1]);
								tdgGuiDetailsDTO.setColumnLabel(strValues[2]);
								tdgGuiDetailsDTO.setColumnType(strValues[3]);
								if(strValues.length == 5)
									tdgGuiDetailsDTO.setColumnValues(strValues[4]);
								
							//}
						}else{
							if(strColumns[i].startsWith("TDG_DATE_FORMAT")){
								tdgSchemaDTO.setDateformate(strColumns[i].substring("TDG_DATE_FORMAT".length()));
							}else if(strColumns[i].startsWith("TDG_SEQUENCE_PREFIX_TABS")){
								tdgSchemaDTO.setSeqtableprefix(strColumns[i].substring("TDG_SEQUENCE_PREFIX_TABS".length()));
							}else if(strColumns[i].startsWith("TDG_BUSINESS_RULES")){
								tdgSchemaDTO.setBusinessrules(strColumns[i].substring("TDG_BUSINESS_RULES".length()));
							}else if(strColumns[i].startsWith("TDG_DEPENDENT_DBS")){
								tdgSchemaDTO.setColumnsdepends(strColumns[i].substring("TDG_DEPENDENT_DBS".length()));
							}
							
						}
						setTdgGuiDetailsDTOs.add(tdgGuiDetailsDTO);
					}
				}else{
					TdgGuiDetailsDTO tdgGuiDetailsDTO = new TdgGuiDetailsDTO();
					if(reqVals.contains("TDG_HASH")){
						String[] strValues = reqVals.split("TDG_HASH");
						//for(int j=0;j<4;j++){
							tdgGuiDetailsDTO.setColumnname(strValues[0]);
							tdgGuiDetailsDTO.setColumnCondition(strValues[1]);
							tdgGuiDetailsDTO.setColumnLabel(strValues[2]);
							tdgGuiDetailsDTO.setColumnType(strValues[3]);
							if(strValues.length == 5)
								tdgGuiDetailsDTO.setColumnValues(strValues[3]);
					}
					setTdgGuiDetailsDTOs.add(tdgGuiDetailsDTO);
				}
				tdgSchemaDTO.setTdgGuiDetailsDTOs(setTdgGuiDetailsDTOs);
				String strResponse = tdgDictionariesService.saveTdgSchemaDetails(tdgSchemaDTO);
				
				if (strResponse.contains(TdgCentralConstant.SUCCESS_MESSAGE)) {
					tdgSchemaDTO.setMessageConstant(TdgCentralConstant.SUCCESS_MESSAGE);
					strSuccess.append(tdgMasterDictionaryDTOTemp.getDictionaryName()).append(" master dictionary is created");					
				} else {
					tdgSchemaDTO.setMessageConstant(TdgCentralConstant.FAILED_MESSAGE);
					strSuccess.append("error while creating in master dictionary");
				}
			}
				model.addAttribute("tdgMasterDictionaryDTO", tdgMasterDictionaryDTO);
				logger.info(strClassName + " ~ createFinalStepMasterDictionary2 ~ returned from method");
				return strSuccess.toString();
				//}
			//}
		
	}
			
	public StringBuffer getUrl(DbConnectionsDTO dbConnectionsDTO){
		StringBuffer url = new StringBuffer();
		try {
			if (null != dbConnectionsDTO) {
				// build url start
				if (StringUtils.isNotEmpty(dbConnectionsDTO.getDbType())) {
					if (dbConnectionsDTO.getDbType().equalsIgnoreCase("Oracle")) {
						// driver class
						//Class.forName(AppConstant.ORA_DRIVER);
						// driver url
						url.append(AppConstant.ORA_URL);
						// Host name
						url.append(dbConnectionsDTO.getHostName());
						// port number
						url.append(':');
						url.append(dbConnectionsDTO.getPort());
						// sid/service/db name
						url.append(':');
						url.append(dbConnectionsDTO.getSid());
					} else if (dbConnectionsDTO.getDbType().equalsIgnoreCase("SqlServer")) {
						// driver class
						//Class.forName(AppConstant.SQL_SERVER_DRIVER);
						// driver url
						url.append(AppConstant.SQL_SERVER_URL);
						// Host name
						url.append(dbConnectionsDTO.getHostName());
						// port number
						url.append(':');
						url.append(dbConnectionsDTO.getPort());
						// sid/service/db name
						url.append(";databaseName=");
						url.append(dbConnectionsDTO.getSid());
						url.append(";");
						if(dbConnectionsDTO.getAuthenticationType().equalsIgnoreCase("windowsAuthentication")){
							
							
							//jdbc:sqlserver://MyServer:1433;databaseName=MyDBName;integratedSecurity=true;
							//jdbc:sqlserver://[DB_URL]:[DB_PORT];databaseName=[DB_NAME];integratedSecurity=true;
							
							
						url.append("integratedSecurity=true; SSPI=true;");	
						
						
						}
					} else if (dbConnectionsDTO.getDbType().equalsIgnoreCase("MySql")) {
						// driver class
						//Class.forName(AppConstant.MYSQL_DRIVER);
						// driver url
						url.append(AppConstant.MYSQL_URL);
						// Host name
						url.append(dbConnectionsDTO.getHostName());
						// port number
						url.append(':');
						url.append(dbConnectionsDTO.getPort());
						// sid/service/db name
						url.append('/');
						url.append(dbConnectionsDTO.getSid());
					} else if (dbConnectionsDTO.getDbType().equalsIgnoreCase("DB2")) {
						// driver class
						//Class.forName(AppConstant.DB2_DRIVER);
						// driver url
						// jdbc:db2://<hostname>:50000/SAMPLE:currentSchema=USRES;
						url.append(AppConstant.DB2_URL);
						// Host name
						url.append(dbConnectionsDTO.getHostName());
						// port number
						url.append(':');
						url.append(dbConnectionsDTO.getPort());
						// sid/service/db name
						//url.append("/SAMPLE:currentSchema=");
						url.append("/"+dbConnectionsDTO.getSid());
						url.append(":currentSchema="+dbConnectionsDTO.getSchemaname()+";");
					}
				}// build url end
			}
			return url;
		} catch (Exception otherEx) {
			// releaseEntityMgrForRollback(entityManager);
			otherEx.printStackTrace();
			return null;
		}
	}
	@RequestMapping(value = "/addPassedTabs", method = RequestMethod.POST )
	public String addRequiredTabsPost(
			@ModelAttribute("tdgMasterDictionaryDTO") TdgMasterDictionaryDTO tdgMasterDictionaryDTO, ModelMap model,
			HttpServletRequest request, HttpServletResponse response){
		String strMethodName = " [ addRequiredTabsPost() ]";
		logger.info(strClassName + strMethodName
				+ " inside of addRequiredTabsPost get method ");
		return RequiredTabs(tdgMasterDictionaryDTO, model, request, response);
	}
	@RequestMapping(value = "/addPassedTabs", method = RequestMethod.GET )
	public String addRequiredTabsGet(
			@ModelAttribute("tdgMasterDictionaryDTO") TdgMasterDictionaryDTO tdgMasterDictionaryDTO, ModelMap model,
			HttpServletRequest request, HttpServletResponse response){
		String strMethodName = " [ addRequiredTabsGet() ]";
		logger.info(strClassName + strMethodName
				+ " inside of addRequiredTabsGet get method ");
				return RequiredTabs(tdgMasterDictionaryDTO, model, request, response);
	}
	
	private String RequiredTabs(TdgMasterDictionaryDTO tdgMasterDictionaryDTO, ModelMap model,
			HttpServletRequest request, HttpServletResponse response){
		boolean bException =false;
		if(StringUtils.isNotEmpty(tdgMasterDictionaryDTO.getFlagSelected())){
			if("TDG_PASSED_TABS".equalsIgnoreCase(tdgMasterDictionaryDTO.getFlagSelected())){
				try{
				if(StringUtils.isNotEmpty(tdgMasterDictionaryDTOTemp.getPassedTabs()))
				//|| "TDG_MASTER_TABS".equalsIgnoreCase(tdgMasterDictionaryDTO.getSelectedTabs()) || "TDG_REQUIRED_COLS".equalsIgnoreCase(tdgMasterDictionaryDTO.getSelectedTabs())){
				tdgMasterDictionaryDTOTemp.setSelectedTabs(tdgMasterDictionaryDTOTemp.getPassedTabs());
				//else
					tdgMasterDictionaryDTOTemp.setFlagSelected("TDG_PASSED_TABS");
				}catch(Exception e){
					e.printStackTrace();
					bException =true;
				}
				tdgMasterDictionaryDTO=  tdgMasterDictionaryDTOTemp;
				if(bException){
					tdgMasterDictionaryDTO.setMessageConstant(TdgCentralConstant.FAILED_MESSAGE);
					tdgMasterDictionaryDTO.setMessage("Database connection error....kindly check database details...");
				}
				//tdgMasterDictionaryDTO.setSelectedTabs("");
				model.addAttribute("tdgMasterDictionaryDTO", tdgMasterDictionaryDTO);
				
				return "addTables";				
			}else if("TDG_MASTER_TABS".equalsIgnoreCase(tdgMasterDictionaryDTO.getFlagSelected())){
				try{
				if(StringUtils.isNotEmpty(tdgMasterDictionaryDTOTemp.getMasterTabs()))
				//|| "TDG_MASTER_TABS".equalsIgnoreCase(tdgMasterDictionaryDTO.getSelectedTabs()) || "TDG_REQUIRED_COLS".equalsIgnoreCase(tdgMasterDictionaryDTO.getSelectedTabs())){
				tdgMasterDictionaryDTOTemp.setSelectedTabs(tdgMasterDictionaryDTOTemp.getMasterTabs());
				//else
					tdgMasterDictionaryDTOTemp.setFlagSelected("TDG_MASTER_TABS");
				}catch(Exception e){
					bException = true;
					e.printStackTrace();
				}
				tdgMasterDictionaryDTO=  tdgMasterDictionaryDTOTemp;
				//tdgMasterDictionaryDTO.setSelectedTabs("");
				if(bException){
					tdgMasterDictionaryDTO.setMessageConstant(TdgCentralConstant.FAILED_MESSAGE);
					tdgMasterDictionaryDTO.setMessage("Database connection error....kindly check database details...");
				}
				model.addAttribute("tdgMasterDictionaryDTO", tdgMasterDictionaryDTO);
				
				return "addTables";				
			}else if("TDG_REQUIRED_COLS".equalsIgnoreCase(tdgMasterDictionaryDTO.getFlagSelected())){
				try{
				if(StringUtils.isNotEmpty(tdgMasterDictionaryDTOTemp.getRequiredCols()))
				//|| "TDG_MASTER_TABS".equalsIgnoreCase(tdgMasterDictionaryDTO.getSelectedTabs()) || "TDG_REQUIRED_COLS".equalsIgnoreCase(tdgMasterDictionaryDTO.getSelectedTabs())){
				tdgMasterDictionaryDTOTemp.setSelectedTabs(tdgMasterDictionaryDTOTemp.getRequiredCols());
				//else
					tdgMasterDictionaryDTOTemp.setFlagSelected("TDG_REQUIRED_COLS");
				}catch(Exception e){
					e.printStackTrace();
					bException = true;
				}
				tdgMasterDictionaryDTO=  tdgMasterDictionaryDTOTemp;
				//tdgMasterDictionaryDTO.setSelectedTabs("");
				if(bException){
					tdgMasterDictionaryDTO.setMessageConstant(TdgCentralConstant.FAILED_MESSAGE);
					tdgMasterDictionaryDTO.setMessage("Database connection error....kindly check database details...");
				}
				model.addAttribute("tdgMasterDictionaryDTO", tdgMasterDictionaryDTO);
				
				return "addTables";				
			}else{
			//tdgMasterDictionaryDTOTemp.setSelectedTabs(tdgMasterDictionaryDTO.getSelectedTabs());
			/*if("TDG_PASSED_TABS".equalsIgnoreCase(tdgMasterDictionaryDTOTemp.getSelectedTabs())){
				tdgMasterDictionaryDTOTemp.setPassedTabs(tdgMasterDictionaryDTO.getPassedTabs());
			}else if("TDG_MASTER_TABS".equalsIgnoreCase(tdgMasterDictionaryDTOTemp.getSelectedTabs())){
				tdgMasterDictionaryDTOTemp.setMasterTabs(tdgMasterDictionaryDTO.getMasterTabs());
			}else if("TDG_REQUIRED_COLS".equalsIgnoreCase(tdgMasterDictionaryDTOTemp.getSelectedTabs())){
				tdgMasterDictionaryDTOTemp.setRequiredCols(tdgMasterDictionaryDTO.getMasterTabs());					
			}*/
				if(tdgMasterDictionaryDTO.getFlagSelected().startsWith("TDG_PASSED_TABS"))
					tdgMasterDictionaryDTOTemp.setPassedTabs(tdgMasterDictionaryDTO.getSelectedTabs());
				else if(tdgMasterDictionaryDTO.getFlagSelected().startsWith("TDG_MASTER_TABS")){
					tdgMasterDictionaryDTOTemp.setMasterTabs(tdgMasterDictionaryDTO.getSelectedTabs());
				}else if(tdgMasterDictionaryDTO.getFlagSelected().startsWith("TDG_REQUIRED_COLS")){
					tdgMasterDictionaryDTOTemp.setRequiredCols(tdgMasterDictionaryDTO.getSelectedTabs());					
				}
			tdgMasterDictionaryDTOTemp.setSelectedTabs("");
			tdgMasterDictionaryDTO=  tdgMasterDictionaryDTOTemp;
			//tdgMasterDictionaryDTO.setSelectedTabs("");
			model.addAttribute("tdgMasterDictionaryDTO", tdgMasterDictionaryDTO);
			
			return "redirect:tdgaNextCreateMasterDictionary";
			}
		}else{
			/*tdgMasterDictionaryDTO=  tdgMasterDictionaryDTOTemp;
			tdgMasterDictionaryDTO.setSelectedTabs("");
			model.addAttribute("tdgMasterDictionaryDTO", tdgMasterDictionaryDTO);			
			return "addTables";*/
			tdgMasterDictionaryDTOTemp.setSelectedTabs("");
			tdgMasterDictionaryDTO=  tdgMasterDictionaryDTOTemp;
			//tdgMasterDictionaryDTO.setSelectedTabs("");
			model.addAttribute("tdgMasterDictionaryDTO", tdgMasterDictionaryDTO);
			
			return "redirect:tdgaNextCreateMasterDictionary";
		}
	}
	
	
	@RequestMapping(value = "/addMasterTabs", method = RequestMethod.GET)
	public String addMasterTabs(
			@ModelAttribute("tdgMasterDictionaryDTO") TdgMasterDictionaryDTO tdgMasterDictionaryDTO, ModelMap model,
			HttpServletRequest request, HttpServletResponse response){
		String strMethodName = " [ addMasterTabs() ]";
		logger.info(strClassName + strMethodName
				+ " inside of addMasterTabs get method ");
		tdgMasterDictionaryDTO=  tdgMasterDictionaryDTOTemp;
		model.addAttribute("tdgMasterDictionaryDTO", tdgMasterDictionaryDTO);
		return "addTables";
	}
}
