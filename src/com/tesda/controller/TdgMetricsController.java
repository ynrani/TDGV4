/*
 * Object Name : TdgMetricsController.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		3:17:14 PM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.ibm.db2.jcc.am.m;
import com.tesda.model.DTO.MetricsDetailsDTO;
import com.tesda.service.TdgMetricsService;

/**
 * @author vkrish14
 *
 */
@Controller
public class TdgMetricsController{
	private static Logger logger = Logger.getLogger(TdgMetricsController.class);
	private static String strClassName = " [ TdgMetricsController ] ";
	
	@Resource(name = "tdgMetricsService")
	TdgMetricsService tdgMetricsService;
	
	
	@RequestMapping(value = "/metricsDetails", method = RequestMethod.GET)
	public String metricsDetails(@ModelAttribute("metricsDetailsDTO") MetricsDetailsDTO metricsDetailsDTO,
			ModelMap model, HttpServletRequest request, HttpServletResponse response){
		String strMethodName = " [ metricsDetails() ]";
		
		logger.info(strClassName + strMethodName + " inside of metricsDetails get method ");
		
		//tdgMetricsService.
		
		Gson gsonObj = new Gson();
		Map<Object,Object> map = null;
		List<Map<Object,Object>> list = new ArrayList<Map<Object,Object>>();

		/*map = new HashMap<Object,Object>(); map.put("x", 0);  map.put("y", 101.3); list.add(map);
		map = new HashMap<Object,Object>(); map.put("x", 152);  map.put("y", 99.49); list.add(map);
		map = new HashMap<Object,Object>(); map.put("x", 457);  map.put("y", 95.91); list.add(map);
		map = new HashMap<Object,Object>(); map.put("x", 762);  map.put("y", 92.46); list.add(map);
		map = new HashMap<Object,Object>(); map.put("x", 914);  map.put("y", 90.81); list.add(map);
		map = new HashMap<Object,Object>(); map.put("x", 1067);  map.put("y", 89.15); list.add(map);
		map = new HashMap<Object,Object>(); map.put("x", 2743);  map.put("y", 72.4); list.add(map);
		map = new HashMap<Object,Object>(); map.put("x", 3048);  map.put("y", 69.64); list.add(map);
		map = new HashMap<Object,Object>(); map.put("x", 4572);  map.put("y", 57.16); list.add(map);
		map = new HashMap<Object,Object>(); map.put("x", 6096);  map.put("y", 46.61); list.add(map);
		map = new HashMap<Object,Object>(); map.put("x", 7620);  map.put("y", 37.65); list.add(map);
		map = new HashMap<Object,Object>(); map.put("x", 9144);  map.put("y", 30.13); list.add(map);
		map = new HashMap<Object,Object>(); map.put("x", 10668);  map.put("y", 23.93); list.add(map);
		map = new HashMap<Object,Object>(); map.put("x", 12192);  map.put("y", 18.82); list.add(map);
		map = new HashMap<Object,Object>(); map.put("x", 18288);  map.put("y", 7.24); list.add(map);
		map = new HashMap<Object,Object>(); map.put("x", 21336);  map.put("y", 4.49); list.add(map);
		map = new HashMap<Object,Object>(); map.put("x", 24384);  map.put("y", 2.8); list.add(map);
		map = new HashMap<Object,Object>(); map.put("x", 27432);  map.put("y", 1.76);list.add(map);	*/
		map = new HashMap<Object,Object>(); map.put("y", 17363);  map.put("label", "Jan"); list.add(map);
		map = new HashMap<Object,Object>(); map.put("y", 28726);  map.put("label", "Feb"); list.add(map);
		map = new HashMap<Object,Object>(); map.put("y", 35000);  map.put("label", "Oct"); list.add(map);
		//String dataPoints1 = gsonObj.toJson(list);

		List<Map<Object,Object>> list2 = new ArrayList<Map<Object,Object>>();
		map = new HashMap<Object,Object>(); map.put("y", 72.48);  map.put("legendText", "DB");  map.put("label", "DB"); list2.add(map);
		map = new HashMap<Object,Object>(); map.put("y", 10.39);  map.put("legendText", "XLS");  map.put("label", "XLS"); list2.add(map);
		map = new HashMap<Object,Object>(); map.put("y", 7.78);  map.put("legendText", "CSV");  map.put("label", "CSV"); list2.add(map);
		map = new HashMap<Object,Object>(); map.put("y", 7.14);  map.put("legendText", "XLSFL");  map.put("label", "XLSFL"); list2.add(map);
		map = new HashMap<Object,Object>(); map.put("y", 0.22);  map.put("legendText", "CSVFL");  map.put("label", "CSVFL"); list2.add(map);
		map = new HashMap<Object,Object>(); map.put("y", 0.15);  map.put("legendText", "XML");  map.put("label", "XML"); list2.add(map);	
		//String dataPoints2 = gsonObj.toJson(list);

		List<Map<Object,Object>> list3 = new ArrayList<Map<Object,Object>>();
		map = new HashMap<Object,Object>(); map.put("y", 134251);  map.put("label", "DB"); list3.add(map);
		map = new HashMap<Object,Object>(); map.put("y", 267017);  map.put("label", "XLS"); list3.add(map);
		map = new HashMap<Object,Object>(); map.put("y", 175200);  map.put("label", "CSV"); list3.add(map);
		map = new HashMap<Object,Object>(); map.put("y", 154580);  map.put("label", "XLSFL"); list3.add(map);
		map = new HashMap<Object,Object>(); map.put("y", 116000);  map.put("label", "CSVFL"); list3.add(map);
		map = new HashMap<Object,Object>(); map.put("y", 97800);  map.put("label", "XML"); list3.add(map);	
		//String dataPoints3 = gsonObj.toJson(list);

		List<Map<Object,Object>> list4 = new ArrayList<Map<Object,Object>>();
		map = new HashMap<Object,Object>(); map.put("x", 883593000000L);  map.put("y", 1872000); list4.add(map);
		map = new HashMap<Object,Object>(); map.put("x", 915129000000L);  map.put("y", 2140000); list4.add(map);
		map = new HashMap<Object,Object>(); map.put("x", 946665000000L);  map.put("y", 7289000); list4.add(map);
		map = new HashMap<Object,Object>(); map.put("x", 978287400000L);  map.put("y", 4830000); list4.add(map);
		map = new HashMap<Object,Object>(); map.put("x", 1009823400000L);  map.put("y", 2009000); list4.add(map);
		map = new HashMap<Object,Object>(); map.put("x", 1041359400000L);  map.put("y", 2840000); list4.add(map);
		map = new HashMap<Object,Object>(); map.put("x", 1072895400000L);  map.put("y", 2396000); list4.add(map);
		map = new HashMap<Object,Object>(); map.put("x", 1104517800000L);  map.put("y", 1613000); list4.add(map);
		map = new HashMap<Object,Object>(); map.put("x", 1136053800000L);  map.put("y", 2821000); list4.add(map);
		map = new HashMap<Object,Object>(); map.put("x", 1167589800000L);  map.put("y", 2000000); list4.add(map);
		map = new HashMap<Object,Object>(); map.put("x", 1199125800000L);  map.put("y", 1397000);list4.add(map);	
		//String dataPoints4 = gsonObj.toJson(list);
		
		metricsDetailsDTO.setDataPoint1(list);
		metricsDetailsDTO.setDataPoint2(list2);
		metricsDetailsDTO.setDataPoint3(list3);
		metricsDetailsDTO.setDataPoint4(list4);
		logger.info(strClassName + strMethodName + " return from metricsDetails method ");
		model.addAttribute("metricsDetailsDTO", metricsDetailsDTO);
		return "metricsDetails";
	}
	
	@RequestMapping(value = "/metricslive", method = RequestMethod.GET)
	public String metricsLive(@ModelAttribute("metricsDetailsDTO") MetricsDetailsDTO metricsDetailsDTO,
			ModelMap model, HttpServletRequest request, HttpServletResponse response){
		String strMethodName = " [ metricslive() ]";
		
		model.addAttribute("metricsDetailsDTO", metricsDetailsDTO);
		logger.info(strClassName + strMethodName + " return from metricsDetails method ");
		return "metricsLive";
	}
}
