/*
 * Object Name : TDGBaseUtil.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		1:44:15 PM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.tesda.model.DTO.TdgDynamicGuiDTO;
import com.tesda.model.DTO.TdgDynamicPageContentDTO;
import com.tesda.model.DTO.TestDataGenerateDTO;

/**
 * @author vkrish14
 *
 */
public class TDGBaseUtil{
	private static Logger logger = Logger.getLogger(TDGBaseUtil.class);
	private static String strClassName = " [ TDGBaseUtil ] ";
	
	public static TestDataGenerateDTO generateTestData(String strRequestValues,TdgDynamicPageContentDTO dynamicPageContent,List<TdgDynamicPageContentDTO> listDynamicPageContentDTO){
		TestDataGenerateDTO testDataGenerateDTO = new TestDataGenerateDTO();	

		String strMethodName = " [ generateTestData() ]";
		logger.info(strClassName + strMethodName + " inside of generateTestData post method ");
		String userId="demo";
		if(!strRequestValues.contains("TK24")){
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			userId = user.getUsername();
		}
		StringBuffer strSuccess = new StringBuffer("");
		long lSelectedSchemaId = 0;
		long lRequestedCount = 0;
		String strCountValue = "";
		List<String> lstDictionaryCols = new ArrayList<String>();
		String strGenerationType = "";
		String strCSVFileSeperator =",";
			String[] strParentSplit = strRequestValues.split("\\*");
			Map<String, Object> mapConditions = new HashMap<String, Object>();
			Map<String, Object> mapConditionsPassed = new HashMap<String, Object>();
			Map<String, Object> mapUpdateDeletePassed = new HashMap<String, Object>();
			String strRequiredAllColumns = "NO";
			System.out.println("----strParentSplit----"+strParentSplit);
			for (int i = 0; i < strParentSplit.length; i++) {
				if (strParentSplit[i].contains(":")) {
					String[] strChildSplit = strParentSplit[i].split(":");
					if (strChildSplit.length ==2 && StringUtils.isNotEmpty(strChildSplit[0]) ) {
						if (strChildSplit[1].contains("SCHEMA_ID")) {
							lSelectedSchemaId = Long.parseLong(strChildSplit[0]);
						}
						System.out.println("----strChildSplit[1]----"+strChildSplit[1]);
						if (strChildSplit[1].contains("GenerateCount")) {
							strCountValue = strChildSplit[0];
						} else if(strChildSplit[1].contains("GenerateType")){
							strGenerationType = strChildSplit[0];
						}else if (strChildSplit[1].contains("TDGFUNCTIONTYPE")) {
							testDataGenerateDTO.setTdgFunctionType(strChildSplit[0]);
						}else if(strChildSplit[1].contains("RequiredAllColumns")){
							strRequiredAllColumns = strChildSplit[0];
						}else if(strChildSplit[1].contains("PopulateType")){
							testDataGenerateDTO.setPopulationType(strChildSplit[0]);
						}else if(strChildSplit[1].contains("CSVFileSeperator")){
							strCSVFileSeperator = strChildSplit[0];
						}else if (!strChildSplit[1].contains("SCHEMA_ID")) {
							if (strChildSplit[1].toUpperCase().contains(
									TdgCentralConstant.MANUAL_DICTIONARY)
									&& !StringUtils.isEmpty(strChildSplit[0])) {
								lstDictionaryCols.add(strChildSplit[1].toUpperCase().substring(
										0,
										strChildSplit[1].toUpperCase().lastIndexOf(
												TdgCentralConstant.MANUAL_DICTIONARY)));
							}else {
								if(mapConditions.containsKey(strChildSplit[1].toUpperCase()) && !mapConditions.get(strChildSplit[1].toUpperCase()).equals(strChildSplit[0])){
									mapConditions.put(strChildSplit[1].toUpperCase(), mapConditions.get(strChildSplit[1].toUpperCase())+"#"+strChildSplit[0]);
								}else{
									mapConditions.put(strChildSplit[1].toUpperCase(), strChildSplit[0]);
								}
							}
						}
					}
				}
			}
			
			
			/**
			 * Going to fetch schema Details
			 */
			StringBuffer strBufferCondition = new StringBuffer("");
			String strUrl = "";
			String strPass = "";
			String strName = "";
			String strSchemaName = "";
			String strMasterTables = "";
			String strSequencePrefix = "";
			String strColumnDependes = "";
			String strDictionaryName = "";
			String strPassedTabs = "";
			String strDateFormate = "";
			String strBusinessRules = "";
			String strRequiredColumns = "";
			int i = 1;
			for (TdgDynamicPageContentDTO dynamicDTO : listDynamicPageContentDTO) {
				if (lSelectedSchemaId == dynamicDTO.getSchemaId()) {
					strUrl = dynamicDTO.getUrl();
					strPass = dynamicDTO.getPassword();
					strName = dynamicDTO.getName();
					strSchemaName = dynamicDTO.getSchemaname();
					strMasterTables = dynamicDTO.getSchemamastertables();
					strSequencePrefix = dynamicDTO.getSeqtableprefix();
					strColumnDependes = dynamicDTO.getColumnsdepends();
					strDictionaryName = dynamicDTO.getManualdictionary();
					strPassedTabs = dynamicDTO.getSchemapasstabs();
					strDateFormate = dynamicDTO.getDateformate();
					strBusinessRules = dynamicDTO.getBusinessrules();
					strRequiredColumns = dynamicDTO.getRequiredcolumns();
					List<TdgDynamicGuiDTO> listGuiDTOs = dynamicDTO.getListDynamicPojo();
					for (TdgDynamicGuiDTO dynamicGui : listGuiDTOs) {
						if (dynamicGui.getColumnName() != null && (mapConditions.containsKey(dynamicGui.getColumnName().toUpperCase())||mapConditions.containsKey("TDG_IP_COND"+dynamicGui.getColumnName().toUpperCase()))) {
							boolean bBreakCheck = false;
							for (Map.Entry<String, Object> mapEntry : mapConditions.entrySet()) {
								//if (mapEntry.getKey().equalsIgnoreCase("TDG_IP_COND"+dynamicGui.getColumnName().toUpperCase())||mapEntry.getKey().equalsIgnoreCase(dynamicGui.getColumnName().toUpperCase())) {
								if (mapEntry.getKey().equalsIgnoreCase("TDG_IP_COND"+dynamicGui.getColumnName().toUpperCase())) {
									if("Yes".equalsIgnoreCase(dynamicGui.getColumnCondition()) && mapEntry.getKey().contains("TDG_IP_COND")){
										mapUpdateDeletePassed.put(dynamicGui.getColumnName(), mapEntry.getValue());
									}
									if (!bBreakCheck && mapEntry.getKey().equalsIgnoreCase(dynamicGui.getColumnName().toUpperCase())) {
										strBufferCondition.append(dynamicGui.getColumnLabel()).append(
												':');
										if (dynamicGui.getColumnValues() != null
												&& dynamicGui.getColumnValues().contains(":")) {
											strBufferCondition.append(
													dynamicGui.getMapValues().get(mapEntry.getValue()))
													.append('#');
										} else {
											strBufferCondition.append(mapEntry.getValue()).append('#');
										}
										bBreakCheck = true;
									}
								}								
							}							
						}
					}
					//going for append the business rules conditions
					if(strBusinessRules != null && !strBusinessRules.equals(";")){
						if(strBusinessRules.contains("#")){
							String[] strArray = strBusinessRules.split("#");
							for(int iRules=0;iRules<strArray.length;iRules++){
								if(strArray[iRules].contains(":")){
									String[] strBusinessConditions = strArray[iRules].split(":");
									mapConditions.put(strBusinessConditions[0].toUpperCase(), strBusinessConditions[1].contains(";")?(strBusinessConditions[1].substring(0, strBusinessConditions[1].length()-1).contains("TDG_COLON") ? strBusinessConditions[1].substring(0, strBusinessConditions[1].length()-1).replaceAll("TDG_COLON", ":") : strBusinessConditions[1].substring(0, strBusinessConditions[1].length()-1)) : (strBusinessConditions[1].contains("TDG_COLON") ? strBusinessConditions[1].replaceAll("TDG_COLON",":"): strBusinessConditions[1]));
									//mapConditions.put(strBusinessConditions[0].toUpperCase(),mapConditions.get(strBusinessConditions[0].toUpperCase()).);
								}
							}
						}else if(strBusinessRules.contains(":")){
							String[] strBusinessConditions = strBusinessRules.endsWith(";") ? strBusinessRules.substring(0,strBusinessRules.length()).split(":") : strBusinessRules.split(":");
							mapConditions.put(strBusinessConditions[0].toUpperCase(), strBusinessConditions[1].contains(";")?(strBusinessConditions[1].substring(0, strBusinessConditions[1].length()-1).contains("TDG_COLON") ? strBusinessConditions[1].substring(0, strBusinessConditions[1].length()-1).replaceAll("TDG_COLON", ":") : strBusinessConditions[1].substring(0, strBusinessConditions[1].length()-1)) : (strBusinessConditions[1].contains("TDG_COLON") ? strBusinessConditions[1].replaceAll("TDG_COLON",":"): strBusinessConditions[1]));
							//mapConditions.put(strBusinessConditions[0].toUpperCase(), strBusinessConditions[1].contains(";")?(strBusinessConditions[1].substring(0, strBusinessConditions[1].length()-1).contains("TDG_COLON") ? strBusinessConditions[1].substring(0, strBusinessConditions[1].length()-1).replaceAll("TDG_CAMA", ":") : strBusinessConditions[1].substring(0, strBusinessConditions[1].length()-1)) : (strBusinessConditions[1].contains("TDG_CAMA") ? strBusinessConditions[1].replaceAll("TDG_CAMA",":"): strBusinessConditions[1]));
						}			
					}
					break;
				}
			}
			//below code is added for sequence of columns and required columns
			List<String> lstRequiredColumns = new ArrayList<String>();
			if(strRequiredColumns != null && !strRequiredColumns.trim().equals(";") && !strRequiredColumns.trim().equals("")){
				if(strRequiredColumns.contains("#")){
					String[] splitedVals = strRequiredColumns.split("#");
					for(int j = 0 ;j<splitedVals.length; j++){
						lstRequiredColumns.add(splitedVals[j]);
					}
				}else if (strRequiredColumns.contains(",")){
					String[] splitedVals = strRequiredColumns.split(",");
					for(int j = 0 ;j<splitedVals.length; j++){
						lstRequiredColumns.add(splitedVals[j]);
					}
				}else{
					lstRequiredColumns.add(strRequiredColumns);
				}
				if(!lstRequiredColumns.isEmpty() && lstRequiredColumns.get(lstRequiredColumns.size()-1).contains(";")){
					String strFilter = lstRequiredColumns.get(lstRequiredColumns.size()-1).substring(0, lstRequiredColumns.get(lstRequiredColumns.size()-1).indexOf(";"));
					lstRequiredColumns.remove(lstRequiredColumns.size()-1);
					lstRequiredColumns.add(strFilter);
					
				}
			}
			//end
			
			Set<String> listPassedTabs = new LinkedHashSet<String>();
			if (StringUtils.isNotEmpty(strPassedTabs)) {
				if (strPassedTabs.contains(";")) {
					String strArrays[] = strPassedTabs.split(";");
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
			String strCondition = strBufferCondition.toString();
			if (strCondition != null && strCondition.contains("#")
					&& strCondition.lastIndexOf("#") == strCondition.length() - 1) {
				strCondition = strCondition.substring(0, strCondition.lastIndexOf("#"));
			}
			logger.info("Condition criteria is :: " + strCondition);
			try {
				lRequestedCount = Long.parseLong(strCountValue);
				
			} catch (NumberFormatException ne) {
				logger.error(ne.getMessage());
				
			}
			if (mapConditions != null) {
				boolean bCheck = false;
				for (Map.Entry<String, Object> mapValue : mapConditions.entrySet()) {
					if (null != mapValue.getValue() && !"".equals(mapValue.getValue())
							&& !"".equals(mapValue.getValue().toString().trim())) {
						bCheck = true;
						//mapConditionsPassed needs to check for . operator
						if (mapValue.getKey().contains("#") ||mapValue.getKey().contains("TDG_MT")) {//added TDG_MT because of not replacing
							String strArray[] = mapValue.getKey().split("#");
							if(strArray == null ||strArray.length <= 1)
								strArray = mapValue.getKey().split("TDG_MT");
							for (int ii = 0; ii < strArray.length; ii++) {
								if (lstDictionaryCols != null
										&& !lstDictionaryCols.contains(strArray[ii])) {
									if(strArray[ii].contains(".")){
										String tempVal[] = strArray[ii].split("\\.");
										if(!mapUpdateDeletePassed.containsKey(tempVal[1]))
									mapConditionsPassed.put(tempVal[1], mapValue.getValue());
									listPassedTabs.add(tempVal[0]);
									}else{
										if(!mapUpdateDeletePassed.containsKey(strArray[ii]))
										mapConditionsPassed.put(strArray[ii], mapValue.getValue());
									}
								}
							}
						} else {
							if (lstDictionaryCols != null
									&& !lstDictionaryCols.contains(mapValue.getKey())) {
								//mapConditionsPassed.put(mapValue.getKey(), mapValue.getValue());
								if(mapValue.getKey().contains(".")){
									String tempVal[] = mapValue.getKey().split("\\.");
									if(!mapUpdateDeletePassed.containsKey(tempVal[1]))
								mapConditionsPassed.put(tempVal[1], mapValue.getValue());
								listPassedTabs.add(tempVal[0]);
								}else{
									if(!mapValue.getKey().contains("TDG_IP_COND"))
									//if(!mapUpdateDeletePassed.containsKey(mapValue.getKey()))
									mapConditionsPassed.put(mapValue.getKey(), mapValue.getValue());
								}
							}
						}
					}
				}
				if (lstDictionaryCols != null && !lstDictionaryCols.isEmpty()) {
					bCheck = true;
				}
				if (!bCheck) {
					if (strSuccess.toString().contains(TdgCentralConstant.FAILED_MESSAGE)) {
						strSuccess
								.append("<br/>")
								.append(i)
								.append(". Atleast one criteria is required apart from Generate Count");
					} else {
						strSuccess
								.append(TdgCentralConstant.FAILED_MESSAGE)
								.append('#')
								.append(i)
								.append(". Atleast one criteria is required apart from Generate Count");
					}
				}
			}
			logger.info("Requested count to generate the records :: " + lRequestedCount);
			testDataGenerateDTO.setGenerateRecordsCount(lRequestedCount);
			if (!strSuccess.toString().contains(TdgCentralConstant.FAILED_MESSAGE)) {
				testDataGenerateDTO.setGenerationType(strGenerationType);
				testDataGenerateDTO.setMapinputData(mapConditionsPassed);
				testDataGenerateDTO.setMapinputConditionData(mapUpdateDeletePassed);
//				testDataGenerateDTO.setGenerateRecordsCount(lRequestedCount);
				testDataGenerateDTO.setPassword(strPass);
				testDataGenerateDTO.setUsername(strName);
				testDataGenerateDTO.setUrl(strUrl);
				testDataGenerateDTO.setSchemaId(lSelectedSchemaId);
				testDataGenerateDTO.setUserId(userId);
				testDataGenerateDTO.setCondition(strCondition);
				testDataGenerateDTO.setSchemaname(strSchemaName);
				testDataGenerateDTO.setSchemamastertables(strMasterTables);
				testDataGenerateDTO.setSeqtableprefix(strSequencePrefix);
				//lstDictionaryCols needs to check for . operator
				testDataGenerateDTO.setGUIDictionaryColumns(lstDictionaryCols);
				testDataGenerateDTO.setDateFormate(strDateFormate);
				testDataGenerateDTO.setColumnsdepends(strColumnDependes);
				testDataGenerateDTO.setSequenceOrder(false);
				testDataGenerateDTO.setCsvFileSeperator(strCSVFileSeperator);
				testDataGenerateDTO.setDictionaryName(strDictionaryName);
				testDataGenerateDTO.setRequestParameterValue(strRequestValues);
				if("YES".equalsIgnoreCase(strRequiredAllColumns)){
					testDataGenerateDTO.setRequiredAllColumns(true);
				}
				if(!lstRequiredColumns.isEmpty())
					testDataGenerateDTO.setRequiredSequenceColumns(lstRequiredColumns);
				if (!listPassedTabs.isEmpty()) {
					listPassedTabs.remove("ALL");
					listPassedTabs.remove("TDG_PASSED_TABS");
					testDataGenerateDTO.setDataConditionalTabNames(listPassedTabs);
				}
				
				if(StringUtils.isNotEmpty(strDateFormate)){
					strDateFormate = strDateFormate.replaceAll("TDG_DATE_FORMAT,", "");
					testDataGenerateDTO.setDateFormate(strDateFormate);
				}
				if(StringUtils.isNotEmpty(strSequencePrefix)){
					strSequencePrefix = strSequencePrefix.replaceAll("TDG_SEQUENCE_PREFIX_TABS,", "");
					testDataGenerateDTO.setSeqtableprefix(strSequencePrefix);
				}
				/*if(strColumns[i].startsWith("TDG_BUSINESS_RULES")){
					tdgSchemaDTO.setBusinessrules(strColumns[i].substring("TDG_BUSINESS_RULES".length()));
				}*/
				if(StringUtils.isNotEmpty(strColumnDependes)){
					strColumnDependes = strColumnDependes.replaceAll("TDG_DEPENDENT_DBS,", "");
					testDataGenerateDTO.setColumnsdepends(strColumnDependes);
				}
				/**
				 * Going for read the data dictionary values
				 */
				/*Map<String, List<String>> mapResult = null;
				if (!StringUtils.isEmpty(strDictionaryName)) {
					mapResult = tdgOperationsService
							.retrieveManualDictionaryValues(strDictionaryName);
				}
				if (mapResult != null && !mapResult.isEmpty()) {
					testDataGenerateDTO.setMapDictionaryVals(mapResult);
				}*/				
			}
		return testDataGenerateDTO;
	}

}
