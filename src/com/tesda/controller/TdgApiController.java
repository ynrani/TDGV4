package com.tesda.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tesda.model.DTO.TdgDynamicPageContentDTO;
import com.tesda.model.DTO.TestDataGenerateDTO;
import com.tesda.service.TdgOperationsService;
import com.tesda.service.TdgProfileService;
import com.tesda.util.TDGBaseUtil;
import com.tesda.util.TdgCentralConstant;



@Controller
@Scope("session")
public class TdgApiController{
	
	private static Logger logger = Logger.getLogger(TdgApiController.class);
	private static String strClassName = " [ TdgApiController ] ";
	@Resource(name = "tdgOperationsService")
	TdgOperationsService tdgOperationsService;
	
	@Resource(name = "tdgProfileService")
	TdgProfileService tdgProfileService;
	
	
	@RequestMapping(value = "/generateRecords", method = RequestMethod.GET)
	public @ResponseBody String findRestFullServices(@RequestParam(value="GenerateType",required=false) String generateType,
			@RequestParam(value="dictionaryName",required=true) String dictionaryName,@RequestParam(value="NoOfRecords",required=true) String numOfRecords,HttpServletRequest request, HttpServletResponse response)
	{
		String status = "Generated records successfully";
		List<TdgDynamicPageContentDTO> listDynamicPageContentDTO = null;
		//:GIVENNAME*:SHORTNAME*:SWITCHTOWINDOW*TDGSEQUENCE:MNEMONIC*:ID*,
		//:2CSVFileSeperator*10:2GenerateCount*59:tabs-2SCHEMA_ID*:tabs-2DEPENDS_ON*XLS:2GenerateType*NO:2RequiredAllColumns*GENERATE:TDGFUNCTIONTYPE
		String reqValue="";
		try
		{
			if(StringUtils.isNotEmpty(numOfRecords) && StringUtils.isNotEmpty(dictionaryName))
			{
				
				if(StringUtils.isEmpty(dictionaryName))
					dictionaryName="TK24";
				if(StringUtils.isNotEmpty(generateType))
					reqValue =generateType+":GenerateType";
				else
					reqValue ="XLS:GenerateType";
				reqValue="TDGSEQUENCE:MNEMONIC*"+Long.parseLong(numOfRecords)+":GenerateCount"+"*"+reqValue;
				listDynamicPageContentDTO = tdgOperationsService.getSchemaDetailsForDynamicPage(dictionaryName);
				
				TestDataGenerateDTO testDataGenerateDTO = TDGBaseUtil.generateTestData(reqValue, null, listDynamicPageContentDTO);
				//begin
				
				//end
				Map<String, List<String>> mapResult = null;
				if (!StringUtils.isEmpty(testDataGenerateDTO.getDictionaryName())) {
					mapResult = tdgOperationsService
							.retrieveManualDictionaryValues(testDataGenerateDTO.getDictionaryName(),testDataGenerateDTO.getSchemaname());
				}
				if (mapResult != null && !mapResult.isEmpty()) {
					testDataGenerateDTO.setMapDictionaryVals(mapResult);
				}
				
				String strResponse = tdgOperationsService.generateTestData(testDataGenerateDTO);
				if(strResponse.contains(TdgCentralConstant.SUCCESS_MESSAGE)){
					status = numOfRecords +"record(s) are generated";
				}else{
					status = "0 record(s) are generated ";
				}
				
			}else{
				return "Dictionary Name and NoOfRecords parameter value is required";
			}
			return status;
		}
		catch (Exception baseEx)
		{
			baseEx.printStackTrace();
			return "NoOfRecords parameter value is required";
		}
	}
	
	@RequestMapping(value = "/gen", method = RequestMethod.GET)
	public @ResponseBody String testservice(HttpServletRequest request, HttpServletResponse response){
		String status = "Generated records successfully";
		List<TdgDynamicPageContentDTO> listDynamicPageContentDTO = null;
		//:GIVENNAME*:SHORTNAME*:SWITCHTOWINDOW*TDGSEQUENCE:MNEMONIC*:ID*,
		//:2CSVFileSeperator*10:2GenerateCount*59:tabs-2SCHEMA_ID*:tabs-2DEPENDS_ON*XLS:2GenerateType*NO:2RequiredAllColumns*GENERATE:TDGFUNCTIONTYPE
		String reqValue="";
		try
		{
			/*if(StringUtils.isNotEmpty(numOfRecords) && StringUtils.isNotEmpty(dictionaryName))
			{*/
				
				//if(StringUtils.isEmpty(dictionaryName))
					String dictionaryName="TK24";
				//if(StringUtils.isNotEmpty(generateType))
					//reqValue =":GenerateType";
				//else
					reqValue ="XLS:GenerateType";
				reqValue="TDG_DB_SEQ:MNEMONIC*1000:GenerateCount"+"*"+reqValue+"*dictionaryname:TK24";
				listDynamicPageContentDTO = tdgOperationsService.getSchemaDetailsForDynamicPage(dictionaryName);
				reqValue=reqValue+"*"+listDynamicPageContentDTO.get(0).getSchemaId()+":SCHEMA_ID";
				TestDataGenerateDTO testDataGenerateDTO = TDGBaseUtil.generateTestData(reqValue, null, listDynamicPageContentDTO);
				//begin
				
				//end
				Map<String, List<String>> mapResult = null;
				if (!StringUtils.isEmpty(testDataGenerateDTO.getDictionaryName())) {
					mapResult = tdgOperationsService
							.retrieveManualDictionaryValues(testDataGenerateDTO.getDictionaryName(),testDataGenerateDTO.getSchemaname());
				}
				if (mapResult != null && !mapResult.isEmpty()) {
					testDataGenerateDTO.setMapDictionaryVals(mapResult);
				}
				
				String strResponse = tdgOperationsService.generateTestData(testDataGenerateDTO);
				if(strResponse.contains(TdgCentralConstant.SUCCESS_MESSAGE)){
					status = "1000 record(s) are generated";
				}else{
					status = "0 record(s) are generated ";
				}
				
			/*}else{
				return "Dictionary Name and NoOfRecords parameter value is required";
			}*/
			return status;
		}
		catch (Exception baseEx)
		{
			baseEx.printStackTrace();
			return "NoOfRecords parameter value is required";
		}
}
	@RequestMapping(value = "/gene", method = RequestMethod.POST)
	public @ResponseBody String testservice1(HttpServletRequest request, HttpServletResponse response){
		return "test1";
}
	
	@RequestMapping(value = "/generate", method = RequestMethod.GET)
	public @ResponseBody String findRestServices(HttpServletRequest request, HttpServletResponse response)
	{
		String status = "Generated records successfully";
		List<TdgDynamicPageContentDTO> listDynamicPageContentDTO = null;
		//:GIVENNAME*:SHORTNAME*:SWITCHTOWINDOW*TDGSEQUENCE:MNEMONIC*:ID*,
		//:2CSVFileSeperator*10:2GenerateCount*59:tabs-2SCHEMA_ID*:tabs-2DEPENDS_ON*XLS:2GenerateType*NO:2RequiredAllColumns*GENERATE:TDGFUNCTIONTYPE
		String reqValue="";
		try
		{
			/*if(StringUtils.isNotEmpty(numOfRecords) && StringUtils.isNotEmpty(dictionaryName))
			{*/
				
				//if(StringUtils.isEmpty(dictionaryName))
					String dictionaryName="TK24";
				//if(StringUtils.isNotEmpty(generateType))
					//reqValue =":GenerateType";
				//else
					reqValue ="XLS:GenerateType";
				reqValue="TDGSEQUENCE:MNEMONIC*1000:GenerateCount"+"*"+reqValue;
				listDynamicPageContentDTO = tdgOperationsService.getSchemaDetailsForDynamicPage(dictionaryName);
				
				TestDataGenerateDTO testDataGenerateDTO = TDGBaseUtil.generateTestData(reqValue, null, listDynamicPageContentDTO);
				//begin
				
				//end
				Map<String, List<String>> mapResult = null;
				if (!StringUtils.isEmpty(testDataGenerateDTO.getDictionaryName())) {
					mapResult = tdgOperationsService
							.retrieveManualDictionaryValues(testDataGenerateDTO.getDictionaryName(),testDataGenerateDTO.getSchemaname());
				}
				if (mapResult != null && !mapResult.isEmpty()) {
					testDataGenerateDTO.setMapDictionaryVals(mapResult);
				}
				
				String strResponse = tdgOperationsService.generateTestData(testDataGenerateDTO);
				if(strResponse.contains(TdgCentralConstant.SUCCESS_MESSAGE)){
					status = "1000 record(s) are generated";
				}else{
					status = "0 record(s) are generated ";
				}
				
			/*}else{
				return "Dictionary Name and NoOfRecords parameter value is required";
			}*/
			return status;
		}
		catch (Exception baseEx)
		{
			baseEx.printStackTrace();
			return "NoOfRecords parameter value is required";
		}
	}
}