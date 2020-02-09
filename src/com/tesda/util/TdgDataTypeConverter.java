/*
 * Object Name : TdgDataTypeConverter.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		2:31:59 PM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vkrish14
 *
 */
public class TdgDataTypeConverter{
	
	public static Map<String,List<Integer>> convertObjectToInteger(Map<String,List<Object>> mapValues){
		Map<String,List<Integer>> mapResult = new HashMap<String,List<Integer>>();
		for( Map.Entry<String, List<Object>> entrySet : mapValues.entrySet() ){
			List<Integer> lstValues = new ArrayList<Integer>();
			for(Object obj : entrySet.getValue())
				lstValues.add(Integer.parseInt(obj+""));
			mapResult.put(entrySet.getKey(), lstValues);
			
		}
		return mapResult;
	}
	
	public static Map<String,List<Long>> convertObjectToLong(Map<String,List<Object>> mapValues){
		Map<String,List<Long>> mapResult = new HashMap<String,List<Long>>();
		for( Map.Entry<String, List<Object>> entrySet : mapValues.entrySet() ){
			List<Long> lstValues = new ArrayList<Long>();
			for(Object obj : entrySet.getValue())
				lstValues.add(Long.parseLong(obj+""));
			mapResult.put(entrySet.getKey(), lstValues);
			
		}
		return mapResult;
	}
	
	public static Map<String,List<Float>> convertObjectToFloat(Map<String,List<Object>> mapValues){
		Map<String,List<Float>> mapResult = new HashMap<String,List<Float>>();
		for( Map.Entry<String, List<Object>> entrySet : mapValues.entrySet() ){
			List<Float> lstValues = new ArrayList<Float>();
			for(Object obj : entrySet.getValue())
				lstValues.add(Float.parseFloat(obj+""));
			mapResult.put(entrySet.getKey(), lstValues);
			
		}
		return mapResult;
	}
	
	public static Map<String,List<Double>> convertObjectToDouble(Map<String,List<Object>> mapValues){
		Map<String,List<Double>> mapResult = new HashMap<String,List<Double>>();
		for( Map.Entry<String, List<Object>> entrySet : mapValues.entrySet() ){
			List<Double> lstValues = new ArrayList<Double>();
			for(Object obj : entrySet.getValue())
				lstValues.add(Double.parseDouble(obj+""));
			mapResult.put(entrySet.getKey(), lstValues);
			
		}
		return mapResult;
	}
	
	
	public static Map<String,List<Date>> convertObjectToDate(Map<String,List<Object>> mapValues,String dateFormat){
		Map<String,List<Date>> mapResult = new HashMap<String,List<Date>>();
		for( Map.Entry<String, List<Object>> entrySet : mapValues.entrySet() ){
			List<Double> lstValues = new ArrayList<Double>();
			for(Object obj : entrySet.getValue())
				lstValues.add(Double.parseDouble(obj+""));
			//mapResult.put(entrySet.getKey(), lstValues);
			
		}
		return mapResult;
	}
}
