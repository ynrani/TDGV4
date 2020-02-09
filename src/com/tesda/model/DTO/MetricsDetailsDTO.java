package com.tesda.model.DTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MetricsDetailsDTO{
	private List<Map<Object,Object>> dataPoint1 = new ArrayList<Map<Object,Object>>();
	private List<Map<Object,Object>> dataPoint2 = new ArrayList<Map<Object,Object>>();
	private List<Map<Object,Object>> dataPoint3= new ArrayList<Map<Object,Object>>();
	private List<Map<Object,Object>> dataPoint4= new ArrayList<Map<Object,Object>>();
	public List<Map<Object, Object>> getDataPoint1(){
		return dataPoint1;
	}
	public void setDataPoint1(List<Map<Object, Object>> dataPoint1){
		this.dataPoint1 = dataPoint1;
	}
	public List<Map<Object, Object>> getDataPoint2(){
		return dataPoint2;
	}
	public void setDataPoint2(List<Map<Object, Object>> dataPoint2){
		this.dataPoint2 = dataPoint2;
	}
	public List<Map<Object, Object>> getDataPoint3(){
		return dataPoint3;
	}
	public void setDataPoint3(List<Map<Object, Object>> dataPoint3){
		this.dataPoint3 = dataPoint3;
	}
	public List<Map<Object, Object>> getDataPoint4(){
		return dataPoint4;
	}
	public void setDataPoint4(List<Map<Object, Object>> dataPoint4){
		this.dataPoint4 = dataPoint4;
	}
		
}
